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

import io.kvision.core.TextDecoration
import io.kvision.core.TextDecorationLine
import io.kvision.html.*
import io.kvision.panel.SimplePanel
import io.kvision.panel.simplePanel
import tech.vtitbid.vormi.ui.util.constant.Path

enum class HeadingStyle(val level: Int, val makeBlue: Boolean) {

    PAGE_HEADING(4, true), PARAGRAPH_HEADING(6, false)
}

fun SimplePanel.displayHeading(
    headingStyle: HeadingStyle = HeadingStyle.PARAGRAPH_HEADING,
    addReturnButton: Boolean = false,
    block: SimplePanel.() -> Unit
) {
    container(headingStyle) {
        if (headingStyle.makeBlue) {
            color = Color.Blue
        }

        heading(headingStyle, block)

        if (addReturnButton) {
            returnButton()
        }
    }
}

private inline fun SimplePanel.container(headingStyle: HeadingStyle, crossinline block: SimplePanel.() -> Unit) {
    div(className = "container mt-5 ms-0 ps-0 d-flex align-content-center") {
        when (headingStyle) {
            HeadingStyle.PAGE_HEADING -> {
                addCssClass("mb-4")
            }

            HeadingStyle.PARAGRAPH_HEADING -> {
                addCssClass("mb-2")
            }
        }

        block()
    }
}

private inline fun SimplePanel.heading(headingStyle: HeadingStyle, crossinline block: SimplePanel.() -> Unit) {
    h1(className = "display-${headingStyle.level} flex-fill") {
        simplePanel {
            block()
        }
    }
}

private fun SimplePanel.returnButton() {
    returnButtonContainer {
        link("", Path.MAIN, className = "d-flex align-items-center") {
            textDecoration = TextDecoration(line = TextDecorationLine.NONE)

            button("Вернуться назад", style = ButtonStyle.OUTLINEDANGER, className = "shadow")
        }
    }
}

private inline fun SimplePanel.returnButtonContainer(crossinline block: SimplePanel.() -> Unit) {
    div(className = "flex-fill d-flex justify-content-end ms-3") {
        block()
    }
}