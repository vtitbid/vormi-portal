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

package tech.vtitbid.vormi.ui.util

import io.kvision.core.Background
import io.kvision.html.TAG
import io.kvision.html.div
import io.kvision.html.tag
import io.kvision.panel.SimplePanel

fun SimplePanel.alertBox(content: String) {
    alert {
        background = Background(Color.BrighterLight)

        alertContent(content)
    }
}

private inline fun SimplePanel.alert(crossinline block: SimplePanel.() -> Unit) {
    div(className = "alert mb-5 border-3 border-secondary text-center shadow") {
        block()
    }
}

private fun SimplePanel.alertContent(content: String) {
    tag(TAG.BLOCKQUOTE, className = "blockquote") {
        +content
    }
}