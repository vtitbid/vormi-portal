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

package tech.vtitbid.vormi.ui.client

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.vtitbid.vormi.client.VormiPortalClient
import tech.vtitbid.vormi.ui.config.ConfigFactory

interface VormiPortalClientFactory {

    suspend fun getClient(): VormiPortalClient
}

class DefaultVormiPortalClientFactory : VormiPortalClientFactory, KoinComponent {

    private val configFactory by inject<ConfigFactory>()

    private var client: VormiPortalClient? = null

    override suspend fun getClient(): VormiPortalClient {
        if (client == null) {
            val applicationConfig = configFactory.getConfig()

            client = VormiPortalClient(applicationConfig.backend.baseUrl)
        }

        return requireNotNull(client)
    }
}