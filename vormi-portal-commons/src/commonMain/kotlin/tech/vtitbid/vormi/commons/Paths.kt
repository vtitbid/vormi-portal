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

package tech.vtitbid.vormi.commons

object Paths {

    const val IdParameter = "id"

    const val LimitQueryParameter = "limit"
    const val OffsetQueryParameter = "offset"

    const val PostVolunteerTask = "/tasks"
    const val GetVolunteerTask = "/tasks/{$IdParameter}"
    const val GetVolunteerTasks = "/tasks"
    const val PutVolunteerTask = "/tasks/{$IdParameter}"
    const val DeleteVolunteerTask = "/tasks/{$IdParameter}"

    const val PostVolunteerTaskBanner = "/tasks/banners"
    const val GetVolunteerTaskBanner = "/tasks/banners/{$IdParameter}"
    const val PutVolunteerTaskBanner = "/tasks/banners/{$IdParameter}"
    const val DeleteVolunteerTaskBanner = "/tasks/banners/{$IdParameter}"
}