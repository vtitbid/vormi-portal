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

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.logger.SLF4JLogger
import tech.vtitbid.vormi.telegramadmin.bot.DefaultTelegramBot
import tech.vtitbid.vormi.telegramadmin.bot.TelegramBot
import tech.vtitbid.vormi.telegramadmin.bot.command.*
import tech.vtitbid.vormi.telegramadmin.config.ApplicationConfigFactory
import tech.vtitbid.vormi.telegramadmin.config.DefaultApplicationConfigFactory
import tech.vtitbid.vormi.telegramadmin.database.DefaultRedisClientFactory
import tech.vtitbid.vormi.telegramadmin.database.RedisClientFactory
import tech.vtitbid.vormi.telegramadmin.service.AuthenticationService
import tech.vtitbid.vormi.telegramadmin.service.DefaultAuthenticationService

fun setupDi() {
    startKoin {
        logger(SLF4JLogger())

        val mainModule = module {
            application()
            telegramBot()
            commandHolder()
            commands()
            applicationConfig()
            applicationConfigFactory()
            redisClientFactory()
            authenticationService()
        }

        modules(mainModule)
    }
}

private fun Module.application() {
    singleOf(::VormiPortalTelegramAdminApplication)
}

private fun Module.telegramBot() {
    singleOf<TelegramBot>(::DefaultTelegramBot)
}

private fun Module.commandHolder() {
    singleOf<CommandHolder>(::DefaultCommandHolder)
}

private fun Module.commands() {
    singleCommandOf(::CreateTaskCommand)
    singleCommandOf(::UpdateTaskCommand)
    singleCommandOf(::DeleteTaskCommand)
}

private fun Module.singleCommandOf(creator: () -> Command) {
    val command = creator()

    single(qualifier = command.qualifier) {
        command
    }
}

private fun Module.applicationConfig() {
    single {
        get<ApplicationConfigFactory>().config
    }
}

private fun Module.applicationConfigFactory() {
    singleOf<ApplicationConfigFactory>(::DefaultApplicationConfigFactory)
}

private fun Module.redisClientFactory() {
    singleOf<RedisClientFactory>(::DefaultRedisClientFactory)
}

private fun Module.authenticationService() {
    singleOf<AuthenticationService>(::DefaultAuthenticationService)
}