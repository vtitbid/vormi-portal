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

package tech.vtitbid.vormi.server.util

import io.ktor.http.*
import io.ktor.server.routing.*
import tech.vtitbid.vormi.server.entity.VolunteerTaskBannerEntity

fun Route.requireImageJpegContentType(build: Route. () -> Unit) {
    val requiredContentType = ContentType.parse(VolunteerTaskBannerEntity.MIME_TYPE)

    contentType(requiredContentType, build)
}