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

package tech.vtitbid.vormi.telegramadmin

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import tech.vtitbid.vormi.telegramadmin.bot.TelegramBot
import tech.vtitbid.vormi.telegramadmin.database.RedisClientFactory

class VormiPortalTelegramAdminApplication : KoinComponent {

    private val log = logging()

    private val redisClientFactory by inject<RedisClientFactory>()

    private val telegramBot by inject<TelegramBot>()

    suspend fun run() {
        log.i {
            "VORMI Portal Telegram Administration Bot is starting..."
        }

        redisClientFactory.connect()

        val job = telegramBot.startTelegramBot()

        log.i {
            "VORMI Portal Telegram Administration Bot is ready!"
        }

        job.join()
    }
}