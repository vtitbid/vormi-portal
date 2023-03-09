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

import dev.d1s.exkt.dto.DtoConverter
import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import tech.vtitbid.vormi.commons.VolunteerTask
import tech.vtitbid.vormi.commons.VolunteerTaskBanner
import tech.vtitbid.vormi.commons.VolunteerTaskModification
import tech.vtitbid.vormi.server.converter.VolunteerTaskBannerDtoConverter
import tech.vtitbid.vormi.server.converter.VolunteerTaskDtoConverter
import tech.vtitbid.vormi.server.converter.VolunteerTaskModificationDtoConverter
import tech.vtitbid.vormi.server.entity.VolunteerTaskBannerEntity
import tech.vtitbid.vormi.server.entity.VolunteerTaskEntity

object DtoConverters : ApplicationConfigurer {

    val VolunteerTaskDtoConverterQualifier = named("volunteer-task-dto-converter")
    val VolunteerTaskModificationDtoConverterQualifier = named("volunteer-task-modification-dto-converter")

    val VolunteerTaskBannerDtoConverterQualifier = named("volunteer-task-banner-dto-converter")

    override fun Application.configure(module: Module, config: ApplicationConfig) {
        module.singleOf<DtoConverter<VolunteerTaskEntity, VolunteerTask>>(::VolunteerTaskDtoConverter) {
            qualifier = VolunteerTaskDtoConverterQualifier
        }

        module.singleOf<DtoConverter<VolunteerTaskEntity, VolunteerTaskModification>>(::VolunteerTaskModificationDtoConverter) {
            qualifier = VolunteerTaskModificationDtoConverterQualifier
        }

        module.singleOf<DtoConverter<VolunteerTaskBannerEntity, VolunteerTaskBanner>>(::VolunteerTaskBannerDtoConverter) {
            qualifier = VolunteerTaskBannerDtoConverterQualifier
        }
    }
}