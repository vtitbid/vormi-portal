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

package tech.vtitbid.vormi.telegramadmin.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.sources.EnvironmentVariablesPropertySource
import org.lighthousegames.logging.logging

interface ApplicationConfigFactory {

    val config: ApplicationConfig
}

class DefaultApplicationConfigFactory : ApplicationConfigFactory {

    private val log = logging()

    override val config = loadConfig()

    private fun loadConfig(): ApplicationConfig {
        log.i {
            "Loading config from environment variables..."
        }

        val propertySource = EnvironmentVariablesPropertySource(
            useUnderscoresAsSeparator = true,
            allowUppercaseNames = true
        )

        return ConfigLoaderBuilder.default()
            .addPropertySource(propertySource)
            .build()
            .loadConfigOrThrow()
    }
}