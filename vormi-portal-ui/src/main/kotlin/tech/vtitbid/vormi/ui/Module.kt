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

package tech.vtitbid.vormi.ui

import dev.d1s.exkt.kvision.component.Component
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.vtitbid.vormi.ui.client.DefaultVormiPortalClientFactory
import tech.vtitbid.vormi.ui.client.VormiPortalClientFactory
import tech.vtitbid.vormi.ui.component.*
import tech.vtitbid.vormi.ui.config.ConfigFactory
import tech.vtitbid.vormi.ui.config.DefaultConfigFactory


object Qualifier {

    val HeaderComponent = named("header-component")
    val NavigationComponent = named("navigation-component")
    val PageContentComponent = named("page-content-component")
    val AboutPageComponent = named("about-page-component")
    val JournalComponent = named("journal-component")
    val VolunteerTaskModalComponent = named("volunteer-task-modal-component")
    val FooterComponent = named("footer-component")
}

fun setupModule() {
    startKoin {
        modules(mainModule)
    }
}

private val mainModule = module {
    configFactory()
    components()
    vormiPortalClientFactory()
}

private fun Module.configFactory() {
    singleOf<ConfigFactory>(::DefaultConfigFactory)
}

private fun Module.components() {
    singleOf<Component.Root>(::RootComponent)

    singleOf<Component<*>>(::HeaderComponent) {
        qualifier = Qualifier.HeaderComponent
    }

    singleOf<Component<*>>(::NavigationComponent) {
        qualifier = Qualifier.NavigationComponent
    }

    singleOf<Component<*>>(::PageContentComponent) {
        qualifier = Qualifier.PageContentComponent
    }

    singleOf<Component<*>>(::AboutPageComponent) {
        qualifier = Qualifier.AboutPageComponent
    }

    singleOf<Component<*>>(::JournalComponent) {
        qualifier = Qualifier.JournalComponent
    }

    singleOf<Component<VolunteerTaskModalComponent.VolunteerTaskModalComponentConfig>>(::VolunteerTaskModalComponent) {
        qualifier = Qualifier.VolunteerTaskModalComponent
    }

    singleOf<Component<*>>(::FooterComponent) {
        qualifier = Qualifier.FooterComponent
    }
}

private fun Module.vormiPortalClientFactory() {
    singleOf<VormiPortalClientFactory>(::DefaultVormiPortalClientFactory)
}