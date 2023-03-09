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

package tech.vtitbid.vormi.ui.component

import dev.d1s.exkt.kvision.component.Component
import dev.d1s.exkt.kvision.component.render
import io.kvision.core.Background
import io.kvision.html.div
import io.kvision.html.h1
import io.kvision.html.image
import io.kvision.panel.SimplePanel
import io.kvision.utils.em
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.vtitbid.vormi.ui.Qualifier
import tech.vtitbid.vormi.ui.resource.ResourceLocation
import tech.vtitbid.vormi.ui.util.Color
import tech.vtitbid.vormi.ui.util.constant.Brand

class HeaderComponent : Component<Unit>(), KoinComponent {

    private val navigationComponent by inject<Component<*>>(Qualifier.NavigationComponent)

    override suspend fun SimplePanel.render() {
        div {
            container {
                vstack {
                    logo()
                    brandName()
                }
            }

            render(navigationComponent)
        }
    }

    private inline fun SimplePanel.container(crossinline block: SimplePanel.() -> Unit) {
        div(className = "container-fluid d-flex justify-content-center mt-0 mb-5 shadow-lg border-bottom border-4 border-danger") {
            background = Background(Color.BrighterLight)

            block()
        }
    }

    private inline fun SimplePanel.vstack(crossinline block: SimplePanel.() -> Unit) {
        div(className = "vstack align-items-center") {
            block()
        }
    }

    private fun SimplePanel.logo() {
        div {
            image(ResourceLocation.VORMI_LOGO_WITH_SHADOW) {
                width = 30.em
            }
        }
    }

    private fun SimplePanel.brandName() {
        div(className = "text-center") {
            color = Color.Blue

            h1(Brand.NAME_FULL_RUS, className = "fs-3 fw-bold text-break mb-5")
        }
    }
}