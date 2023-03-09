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

import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.lighthousegames.logging.logging
import tech.vtitbid.vormi.server.database.DefaultVolunteerTaskRepository
import tech.vtitbid.vormi.server.database.VolunteerTaskRepository

object Repositories : ApplicationConfigurer {

    private val logger = logging()

    override fun Application.configure(module: Module, config: ApplicationConfig) {
        logger.d {
            "Configuring repositories..."
        }

        module.singleOf<VolunteerTaskRepository>(::DefaultVolunteerTaskRepository)
    }
}