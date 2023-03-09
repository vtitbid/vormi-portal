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

package tech.vtitbid.vormi.server.configuration

import io.ktor.server.config.*
import org.lighthousegames.logging.logging

private val logger = logging()

val ApplicationConfig.minioEndpoint get() = property("minio.endpoint").getString()
val ApplicationConfig.minioAccessKeyId get() = property("minio.access_key_id").getString()
val ApplicationConfig.minioSecretAccessKey get() = property("minio.secret_access_key").getString()

val ApplicationConfig.bannerImageBucket get() = property("bucket.banner_images").getString()

fun ApplicationConfig.preloadAllProperties() {
    logger.d {
        "Preloading config properties..."
    }

    minioEndpoint
    minioAccessKeyId
    minioSecretAccessKey
    bannerImageBucket
}