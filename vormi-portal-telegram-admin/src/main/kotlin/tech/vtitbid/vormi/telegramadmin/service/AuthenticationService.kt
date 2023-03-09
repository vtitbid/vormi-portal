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

package tech.vtitbid.vormi.telegramadmin.service

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import tech.vtitbid.vormi.telegramadmin.config.ApplicationConfig
import tech.vtitbid.vormi.telegramadmin.entity.AuthenticationResult
import tech.vtitbid.vormi.telegramadmin.entity.UserAuthenticationToken

interface AuthenticationService {

    suspend fun authenticate(userAuthenticationToken: UserAuthenticationToken): AuthenticationResult
}

class DefaultAuthenticationService : AuthenticationService, KoinComponent {

    private val config by inject<ApplicationConfig>()

    private val log = logging()

    override suspend fun authenticate(userAuthenticationToken: UserAuthenticationToken): AuthenticationResult {
        val realToken = config.bot.userAuthenticationToken

        log.d {
            "Trying to authenticate $userAuthenticationToken against $realToken"
        }

        val authenticated = userAuthenticationToken == realToken

        return AuthenticationResult(authenticated)
    }
}