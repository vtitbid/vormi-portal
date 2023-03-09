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

package tech.vtitbid.vormi.server.service

import dev.d1s.exkt.dto.DtoConverter
import dev.d1s.exkt.dto.ResultingEntityWithOptionalDto
import dev.d1s.exkt.dto.convertToDtoIf
import io.ktor.server.plugins.*
import io.minio.StatObjectResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import tech.vtitbid.vormi.commons.VolunteerTaskBanner
import tech.vtitbid.vormi.server.configuration.DtoConverters
import tech.vtitbid.vormi.server.entity.VolunteerTaskBannerEntity
import tech.vtitbid.vormi.server.objectstore.VolunteerTaskBannerStore
import java.io.InputStream
import java.time.ZonedDateTime
import java.util.*

interface VolunteerTaskBannerService {

    suspend fun createTaskBanner(
        bannerStream: InputStream,
        bannerSize: Long,
        requireDto: Boolean = false
    ): ResultingEntityWithOptionalDto<VolunteerTaskBannerEntity, VolunteerTaskBanner>

    suspend fun getTaskBanner(bannerId: UUID, requireDto: Boolean = false): ResultingEntityWithOptionalDto<VolunteerTaskBannerEntity, VolunteerTaskBanner>

    suspend fun updateTaskBanner(
        bannerId: UUID,
        bannerStream: InputStream,
        bannerSize: Long,
        requireDto: Boolean = false
    ): ResultingEntityWithOptionalDto<VolunteerTaskBannerEntity, VolunteerTaskBanner>

    suspend fun removeTaskBanner(bannerId: UUID): Result<Unit>
}

class DefaultVolunteerTaskBannerService : VolunteerTaskBannerService, KoinComponent {

    private val volunteerTaskBannerStore by inject<VolunteerTaskBannerStore>()

    private val volunteerTaskBannerDtoConverter by inject<DtoConverter<VolunteerTaskBannerEntity, VolunteerTaskBanner>>(qualifier = DtoConverters.VolunteerTaskBannerDtoConverterQualifier)

    private val logger = logging()

    override suspend fun createTaskBanner(
        bannerStream: InputStream,
        bannerSize: Long,
        requireDto: Boolean
    ): ResultingEntityWithOptionalDto<VolunteerTaskBannerEntity, VolunteerTaskBanner> =
        runCatching {
            logger.d {
                "Making task banner image..."
            }

            val bannerId = createBannerId()

            val banner = createTaskBannerWithId(bannerId, bannerStream, bannerSize)

            answer(banner, requireDto)
        }

    override suspend fun getTaskBanner(bannerId: UUID, requireDto: Boolean): ResultingEntityWithOptionalDto<VolunteerTaskBannerEntity, VolunteerTaskBanner> =
        runCatching {
            logger.d {
                "Obtaining task banner image with id $bannerId..."
            }

            val bannerStream = volunteerTaskBannerStore.getTaskBannerObject(bannerId).getOrThrowNotFoundException(bannerId)

            val (eTag, contentLength, lastModified) = getVolunteerTaskBannerStat(bannerId)

            val banner = VolunteerTaskBannerEntity(bannerId, bannerStream, eTag, contentLength, lastModified)

            answer(banner, requireDto)
        }

    override suspend fun updateTaskBanner(
        bannerId: UUID,
        bannerStream: InputStream,
        bannerSize: Long,
        requireDto: Boolean
    ): ResultingEntityWithOptionalDto<VolunteerTaskBannerEntity, VolunteerTaskBanner> =
        runCatching {
            logger.d {
                "Updating task banner image with id $bannerId..."
            }

            this.removeTaskBanner(bannerId).getOrThrowNotFoundException(bannerId)

            val banner = this.createTaskBannerWithId(bannerId, bannerStream, bannerSize)

            answer(banner, requireDto)
        }

    override suspend fun removeTaskBanner(bannerId: UUID) =
        runCatching {
            logger.d {
                "Removing task banner image with id $bannerId..."
            }

            checkBannerExists(bannerId)

            volunteerTaskBannerStore.removeTaskBannerObject(bannerId).getOrThrow()
        }

    private suspend fun createTaskBannerWithId(bannerId: UUID, bannerStream: InputStream, bannerSize: Long): VolunteerTaskBannerEntity {
        volunteerTaskBannerStore.putTaskBannerObject(bannerId, bannerStream, bannerSize).getOrThrow()

        val (eTag, contentLength, lastModified) = getVolunteerTaskBannerStat(bannerId)

        return VolunteerTaskBannerEntity(bannerId, bannerStream, eTag, contentLength, lastModified)
    }

    private fun answer(banner: VolunteerTaskBannerEntity, requireDto: Boolean) =
        banner to volunteerTaskBannerDtoConverter.convertToDtoIf(banner) {
            requireDto
        }

    private suspend fun checkBannerExists(bannerId: UUID) {
        volunteerTaskBannerStore.statTaskBannerObject(bannerId).getOrThrowNotFoundException(bannerId)
    }

    private fun createBannerId(): UUID {
        val uuid = UUID.randomUUID()

        logger.d {
            "Created banner id: $uuid"
        }

        return uuid
    }

    private suspend fun getVolunteerTaskBannerStat(bannerId: UUID): VolunteerTaskBannerStat {
        val bannerStatResponse = volunteerTaskBannerStore.statTaskBannerObject(bannerId).getOrThrowNotFoundException(bannerId)

        return VolunteerTaskBannerStat(bannerStatResponse)
    }

    private fun <T> Result<T>.getOrThrowNotFoundException(bannerId: UUID) = getOrElse {
        throw NotFoundException("Banner image with id $bannerId not found")
    }

    private data class VolunteerTaskBannerStat(
        val eTag: String,
        val contentLength: Long,
        val lastModified: ZonedDateTime
    ) {
        constructor(response: StatObjectResponse) : this(
            response.etag(),
            response.size(),
            response.lastModified()
        )
    }
}