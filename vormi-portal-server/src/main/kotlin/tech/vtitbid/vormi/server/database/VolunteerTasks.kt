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

package tech.vtitbid.vormi.server.database

import dev.d1s.exkt.ktorm.UuidIdentifiedAndModificationTimestampAwareEntities
import org.ktorm.schema.enum
import org.ktorm.schema.text
import tech.vtitbid.vormi.commons.VolunteerTaskStatus
import tech.vtitbid.vormi.server.entity.VolunteerTaskEntity

object VolunteerTasks : UuidIdentifiedAndModificationTimestampAwareEntities<VolunteerTaskEntity>(tableName = "volunteer_task") {

    val title = text("title").bindTo {
        it.title
    }

    val description = text("description").bindTo {
        it.description
    }

    val status = enum<VolunteerTaskStatus>("status").bindTo {
        it.status
    }

    val bannerImageId = text("banner_image_id").bindTo {
        it.bannerImageId
    }
}