/*
 *  Copyright 2023 VTITBiD.TECH Research Group <info@vtitbid.tech>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tech.vtitbid.vormi.server.objectstore

import dispatch.core.withIO
import io.ktor.server.config.*
import io.minio.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import tech.vtitbid.vormi.server.configuration.bannerImageBucket
import tech.vtitbid.vormi.server.entity.VolunteerTaskBannerEntity
import java.io.InputStream
import java.util.*
import kotlin.Result

interface VolunteerTaskBannerStore {

    suspend fun putTaskBannerObject(bannerId: UUID, bannerStream: InputStream, bannerSize: Long): Result<ObjectWriteResponse>

    suspend fun getTaskBannerObject(bannerId: UUID): Result<InputStream>

    suspend fun statTaskBannerObject(bannerId: UUID): Result<StatObjectResponse>

    suspend fun removeTaskBannerObject(bannerId: UUID): Result<Unit>
}

class DefaultVolunteerTaskBannerStore : VolunteerTaskBannerStore, KoinComponent {

    private val minioClient by inject<MinioClient>()

    private val config by inject<ApplicationConfig>()

    private val bannerImageBucket by lazy {
        config.bannerImageBucket
    }

    private val logger = logging()

    override suspend fun putTaskBannerObject(bannerId: UUID, bannerStream: InputStream, bannerSize: Long): Result<ObjectWriteResponse> = withIO {
        runCatching {
            logger.d {
                "Uploading task banner image with id $bannerId to bucket $bannerImageBucket..."
            }

            ensureBucketExists()

            val args = PutObjectArgs.builder()
                .defaultBucketAndBannerImageObject(bannerId)
                .contentType(VolunteerTaskBannerEntity.MIME_TYPE)
                .stream(bannerStream, bannerSize, BANNER_DEFAULT_PART_SIZE)
                .build()

            minioClient.putObject(args)
        }
    }

    override suspend fun getTaskBannerObject(bannerId: UUID): Result<InputStream> =
        withIO {
            runCatching {
                logger.d {
                    "Retrieving task banner image with id $bannerId from bucket $bannerImageBucket..."
                }

                val args = GetObjectArgs.builder()
                    .defaultBucketAndBannerImageObject(bannerId)
                    .build()

                minioClient.getObject(args)
            }
        }

    override suspend fun statTaskBannerObject(bannerId: UUID): Result<StatObjectResponse> =
        withIO {
            runCatching {
                logger.d {
                    "Retrieving info about banner image with id $bannerId..."
                }

                val args = StatObjectArgs.builder()
                    .defaultBucketAndBannerImageObject(bannerId)
                    .build()

                minioClient.statObject(args)
            }
        }

    override suspend fun removeTaskBannerObject(bannerId: UUID): Result<Unit> =
        withIO {
            runCatching {
                logger.d {
                    "Removing task banner image with id $bannerId from bucket $bannerImageBucket..."
                }

                val args = RemoveObjectArgs.builder()
                    .defaultBucketAndBannerImageObject(bannerId)
                    .build()

                minioClient.removeObject(args)
            }
        }

    private fun <B : ObjectArgs.Builder<B, A>, A : ObjectArgs> B.defaultBucketAndBannerImageObject(bannerId: UUID): B = apply {
        bucket(bannerImageBucket)
        `object`("${TASK_BANNER_IMAGES_PATH}/$bannerId")
    }

    private fun ensureBucketExists() {
        val bucketExistsArgs = BucketExistsArgs.builder()
            .bucket(bannerImageBucket)
            .build()

        val exists = minioClient.bucketExists(bucketExistsArgs)

        if (!exists) {
            val createBucketArgs = MakeBucketArgs.builder()
                .bucket(bannerImageBucket)
                .build()

            minioClient.makeBucket(createBucketArgs)
        }
    }

    private companion object {

        private const val BANNER_DEFAULT_PART_SIZE: Long = -1

        private const val TASK_BANNER_IMAGES_PATH = "task-banner-images"
    }
}