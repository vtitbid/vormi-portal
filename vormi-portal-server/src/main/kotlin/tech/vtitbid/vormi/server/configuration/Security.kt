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
import dev.d1s.ktor.staticauth.staticAuthRealm
import dev.d1s.ktor.staticauth.staticAuthToken
import dev.d1s.ktor.staticauth.staticToken
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import org.koin.core.module.Module
import org.lighthousegames.logging.logging

object Security : ApplicationConfigurer {

    private val logger = logging()

    override fun Application.configure(module: Module, config: ApplicationConfig) {
        logger.d {
            "Configuring security..."
        }

        authentication {
            staticToken {
                token = config.staticAuthToken
                realm = config.staticAuthRealm
            }
        }
    }
}