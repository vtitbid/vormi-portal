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

import dev.d1s.exkt.common.replacePlaceholders
import io.kvision.html.TAG
import io.kvision.html.div
import io.kvision.html.p
import io.kvision.html.tag
import io.kvision.panel.SimplePanel
import org.koin.core.component.KoinComponent
import tech.vtitbid.vormi.commons.Version
import tech.vtitbid.vormi.ui.util.constant.Brand

class FooterComponent : ConfigAwareComponent<Unit>(), KoinComponent {

    override suspend fun SimplePanel.render() {
        val applicationConfig = applicationConfig()

        tag(TAG.HR, className = "mt-5")

        div(className = "container mt-5 pb-3 text-center text-secondary") {
            val copyright = Brand.COPYRIGHT.replacePlaceholders("email" to applicationConfig.social.eMail)

            p(copyright)

            p("v${Version.VERSION_FULL}")
        }
    }
}