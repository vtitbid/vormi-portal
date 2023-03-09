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

package tech.vtitbid.vormi.server.route

import dev.d1s.exkt.dto.requiredDto
import dev.d1s.exkt.ktor.server.koin.configuration.Route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import tech.vtitbid.vormi.commons.Paths
import tech.vtitbid.vormi.server.service.VolunteerTaskBannerService
import tech.vtitbid.vormi.server.util.requireImageJpegContentType
import tech.vtitbid.vormi.server.util.requiredContentLengthHeader

class PostVolunteerTaskBannerRoute : Route, KoinComponent {

    override val qualifier = named("post-volunteer-task-banner-route")

    private val volunteerTaskBannerService by inject<VolunteerTaskBannerService>()

    override fun Routing.apply() {
        authenticate {
            requireImageJpegContentType {
                post(Paths.PostVolunteerTaskBanner) {
                    val contentLength = call.requiredContentLengthHeader

                    val bannerStream = call.receiveStream()

                    val createdBanner = volunteerTaskBannerService.createTaskBanner(bannerStream, contentLength, requireDto = true).getOrThrow()

                    call.response.apply {
                        status(HttpStatusCode.Created)
                    }

                    call.respond(createdBanner.requiredDto)
                }
            }
        }
    }
}