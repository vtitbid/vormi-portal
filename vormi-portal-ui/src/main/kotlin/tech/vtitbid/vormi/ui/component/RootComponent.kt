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
import io.kvision.panel.SimplePanel
import io.kvision.utils.vh
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.vtitbid.vormi.ui.Qualifier
import tech.vtitbid.vormi.ui.util.Color

class RootComponent : Component.Root(), KoinComponent {

    private val headerComponent by inject<Component<*>>(Qualifier.HeaderComponent)

    private val pageContentComponent by inject<Component<*>>(Qualifier.PageContentComponent)

    override suspend fun SimplePanel.render() {
        customize()
        deployComponents()
    }

    private fun SimplePanel.customize() {
        minHeight = 100.vh

        background = Background(Color.Light)

        color = Color.Dark
    }

    private fun SimplePanel.deployComponents() {
        render(headerComponent)

        render(pageContentComponent)
    }
}