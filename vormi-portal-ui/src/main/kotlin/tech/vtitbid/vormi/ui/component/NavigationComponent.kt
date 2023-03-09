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

import dev.d1s.exkt.common.pathname
import dev.d1s.exkt.kvision.component.Component
import io.kvision.core.Background
import io.kvision.html.div
import io.kvision.html.link
import io.kvision.panel.SimplePanel
import org.koin.core.component.KoinComponent
import tech.vtitbid.vormi.ui.util.Color
import tech.vtitbid.vormi.ui.util.constant.Path

class NavigationComponent : Component<Unit>(), KoinComponent {

    override suspend fun SimplePanel.render() {
        navigationHstack {
            linkItem("О нас", Path.MAIN)
            linkItem("Журнал", Path.JOURNAL)
        }
    }

    private inline fun SimplePanel.navigationHstack(crossinline block: SimplePanel.() -> Unit) {
        div(className = "hstack gap-3 justify-content-center mb-5") {
            block()
        }
    }

    private fun SimplePanel.linkItem(label: String, url: String) {
        link(label, url, className = "btn btn-primary px-4 py-0 shadow") {
            role = "button"

            background = Background(Color.Blue)

            checkPathName(url)
        }
    }

    private fun SimplePanel.checkPathName(neededUrl: String) {
        if (pathname == neededUrl) {
            disable()
        }
    }

    private fun SimplePanel.disable() {
        addCssClass("disabled")
        tabindex = -1
        setAttribute("aria-disabled", "true")
    }
}