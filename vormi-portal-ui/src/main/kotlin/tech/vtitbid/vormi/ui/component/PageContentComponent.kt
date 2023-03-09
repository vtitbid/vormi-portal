/*
 * Copyright 2022-2023 VTITBiD.TECH Research Group <info@vtitbid.tech>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.vtitbid.vormi.ui.component

import dev.d1s.exkt.common.pathname
import dev.d1s.exkt.kvision.component.Component
import dev.d1s.exkt.kvision.component.render
import io.kvision.html.div
import io.kvision.panel.SimplePanel
import io.kvision.utils.pc
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.vtitbid.vormi.ui.Qualifier
import tech.vtitbid.vormi.ui.util.constant.Path

class PageContentComponent : Component<Unit>(), KoinComponent {

    private val aboutPageComponent by inject<Component<*>>(Qualifier.AboutPageComponent)

    private val journalComponent by inject<Component<*>>(Qualifier.JournalComponent)

    private val footerComponent by inject<Component<*>>(Qualifier.FooterComponent)

    override suspend fun SimplePanel.render() {
        div(className = "container") {
            maxWidth = 60.pc

            route()

            render(footerComponent)
        }
    }

    private fun SimplePanel.route() {
        when (pathname) {
            Path.JOURNAL -> {
                render(journalComponent)
            }

            else -> {
                render(aboutPageComponent)
            }
        }
    }
}