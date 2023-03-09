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
import io.kvision.html.div
import io.kvision.html.p
import io.kvision.panel.SimplePanel
import org.koin.core.component.KoinComponent
import tech.vtitbid.vormi.ui.util.HeadingStyle
import tech.vtitbid.vormi.ui.util.displayHeading

class AboutPageComponent : Component<Unit>(), KoinComponent {

    override suspend fun SimplePanel.render() {
        div(className = "container-fluid px-2") {
            aboutUs()
            whatAreWeDoing()
            manifesto()
        }
    }

    private fun SimplePanel.aboutUs() {
        displayHeading(HeadingStyle.PAGE_HEADING) {
            +"О нас"
        }

        temporaryMock()
    }

    private fun SimplePanel.whatAreWeDoing() {
        displayHeading {
            +"Что делает ВОРМИ?"
        }

        temporaryMock()
    }

    private fun SimplePanel.manifesto() {
        displayHeading {
            +"Манифест ВОРМИ"
        }

        temporaryMock()
    }

    private fun SimplePanel.temporaryMock() {
        p(
            "Ea ut labore non. Perferendis mollitia veritatis culpa. " +
                    "Et velit nihil consectetur qui qui. Magni ea minus omnis est eius et. " +
                    "Consequuntur magnam ut natus id aut vero. " +
                    "Odit molestiae amet laudantium reprehenderit voluptas."
        )
    }
}