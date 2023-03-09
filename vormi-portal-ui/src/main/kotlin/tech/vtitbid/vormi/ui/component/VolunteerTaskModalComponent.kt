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
import io.kvision.core.Background
import io.kvision.html.*
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.vtitbid.vormi.client.VormiPortalClient
import tech.vtitbid.vormi.commons.VolunteerTask
import tech.vtitbid.vormi.commons.VolunteerTaskStatus
import tech.vtitbid.vormi.ui.util.Color
import tech.vtitbid.vormi.ui.util.activityIcon
import tech.vtitbid.vormi.ui.util.setTaskBannerImage

class VolunteerTaskModalComponent :
    Component<VolunteerTaskModalComponent.VolunteerTaskModalComponentConfig>(::VolunteerTaskModalComponentConfig),
    KoinComponent {

    private val client by inject<VormiPortalClient>()

    override suspend fun SimplePanel.render() {
        boundModal { task ->
            background = Background(Color.BrighterLight)

            modalHeader {
                h1(task.title)
            }

            modalBody {
                setTaskBannerImage(client, task, "w-100 mb-3 rounded-3")

                p(task.description)
            }

            modalFooter {
                currentTaskStatus(task)

                closeButton()
            }
        }
    }

    private inline fun SimplePanel.boundModal(crossinline block: SimplePanel.(VolunteerTask) -> Unit) {
        div(className = "modal fade").bind(config.task) { task ->
            if (task != null) {
                id = ID
                tabindex = -1

                setAttribute("aria-labelledby", LABEL)
                setAttribute("aria-hidden", "true")

                modalDialogAndContent {
                    block(task)
                }
            }
        }
    }

    private inline fun SimplePanel.modalHeader(crossinline block: SimplePanel.() -> Unit) {
        div(className = "modal-header border-1 border-bottom border-secondary") {
            block()
        }
    }

    private inline fun SimplePanel.modalBody(crossinline block: SimplePanel.() -> Unit) {
        div(className = "modal-body") {
            block()
        }
    }

    private inline fun SimplePanel.modalFooter(crossinline block: SimplePanel.() -> Unit) {
        div(className = "modal-footer justify-content-between border-1 border-top border-secondary") {
            block()
        }
    }

    private fun SimplePanel.currentTaskStatus(task: VolunteerTask) {
        div {
            activityIcon()

            +"Текущий статус:"

            currentTaskStatusBadge(task)
        }
    }

    private fun SimplePanel.currentTaskStatusBadge(task: VolunteerTask) {
        span(className = "ms-2 badge") {
            customizeCurrentTaskStatusBadge(task)
        }
    }

    private fun SimplePanel.customizeCurrentTaskStatusBadge(task: VolunteerTask) {
        when (task.status) {
            VolunteerTaskStatus.PLANNED -> {
                addCssClass("text-bg-secondary")
                +"Запланировано"
            }

            VolunteerTaskStatus.IN_PROGRESS -> {
                addCssClass("text-bg-primary")
                +"В работе"
            }

            VolunteerTaskStatus.DONE -> {
                addCssClass("text-bg-success")
                +"Завершено"
            }
        }
    }

    private fun SimplePanel.closeButton() {
        button("Закрыть", style = ButtonStyle.DANGER) {
            setAttribute("data-bs-dismiss", "modal")
        }
    }

    private inline fun SimplePanel.modalDialogAndContent(crossinline block: SimplePanel.() -> Unit) {
        modalDialog {
            modalContent(block)
        }
    }

    private inline fun SimplePanel.modalDialog(crossinline block: SimplePanel.() -> Unit) {
        div(className = "modal-dialog modal-lg modal-dialog-centered") {
            block()
        }
    }

    private inline fun SimplePanel.modalContent(crossinline block: SimplePanel.() -> Unit) {
        div(className = "modal-content border-3 border-secondary") {
            block()
        }
    }

    class VolunteerTaskModalComponentConfig {

        val task = ObservableValue<VolunteerTask?>(null)
    }

    companion object {

        const val ID = "volunteerTaskModal"

        private const val LABEL = "${ID}Label"
    }
}

fun Button.triggerVolunteerTaskModal(
    modal: Component<VolunteerTaskModalComponent.VolunteerTaskModalComponentConfig>,
    task: VolunteerTask
) {
    setAttribute("data-bs-toggle", "modal")
    setAttribute("data-bs-target", "#${VolunteerTaskModalComponent.ID}")

    onClick {
        modal.apply {
            this.task.setState(task)
        }
    }
}