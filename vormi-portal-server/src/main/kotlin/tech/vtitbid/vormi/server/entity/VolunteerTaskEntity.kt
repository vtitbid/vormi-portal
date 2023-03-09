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

package tech.vtitbid.vormi.server.entity

import dev.d1s.exkt.ktorm.UuidIdentifiedAndModificationTimestampAware
import org.ktorm.entity.Entity
import tech.vtitbid.vormi.commons.VolunteerTaskStatus

interface VolunteerTaskEntity : UuidIdentifiedAndModificationTimestampAware<VolunteerTaskEntity> {

    var title: String

    var description: String

    var status: VolunteerTaskStatus

    var bannerImageId: String?

    companion object : Entity.Factory<VolunteerTaskEntity>()
}

val VolunteerTaskEntity.asString
    get() = "VolunteerTaskEntity{title = $title, description = $description, status = $status, bannerImageId = $bannerImageId}"