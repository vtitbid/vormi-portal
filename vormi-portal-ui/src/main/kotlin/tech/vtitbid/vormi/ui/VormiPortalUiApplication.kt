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
import dev.d1s.exkt.kvision.component.render
import io.kvision.*
import io.kvision.panel.root
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VormiPortalUiApplication : Application(), KoinComponent {

    private val rootComponent by inject<Component.Root>()

    override fun start() {
        root(ROOT_ELEMENT_ID) {
            render(rootComponent)
        }
    }

    private companion object {

        private const val ROOT_ELEMENT_ID = "root"
    }
}

fun main() {
    setupModule()

    startApplication(
        ::VormiPortalUiApplication,
        module.hot,
        BootstrapModule,
        BootstrapCssModule,
        CoreModule
    )
}
