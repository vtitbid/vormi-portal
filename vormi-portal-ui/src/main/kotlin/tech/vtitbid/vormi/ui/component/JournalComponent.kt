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
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.p
import io.kvision.panel.SimplePanel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.vtitbid.vormi.commons.VolunteerTask
import tech.vtitbid.vormi.commons.VolunteerTaskStatus
import tech.vtitbid.vormi.ui.Qualifier
import tech.vtitbid.vormi.ui.util.*

class JournalComponent : VormiPortalClientAwareComponent<Unit>(), KoinComponent {

    private val volunteerTaskModalComponent by inject<Component<VolunteerTaskModalComponent.VolunteerTaskModalComponentConfig>>(
        Qualifier.VolunteerTaskModalComponent
    )

    private val journalScope = MainScope()

    override suspend fun SimplePanel.render() {
        displayHeading(HeadingStyle.PAGE_HEADING, addReturnButton = true) {
            +"Журнал волонтёрства"
        }

        renderDescriptionAlert()

        val tasks = vormiPortalClient().getVolunteerTasks(1000, 1000).getOrThrow().elements

        renderPlannedTasks(tasks)
        renderInProgressTasks(tasks)
        renderCompletedTasks(tasks)

        render(volunteerTaskModalComponent)
    }

    private fun SimplePanel.renderDescriptionAlert() {
        alertBox("Публичный доступ к журналу волонтёрства ВОРМИ. Здесь, в реальном времени, можно наблюдать за выполнением задач волонтёрского отряда.")
    }

    private fun SimplePanel.renderPlannedTasks(tasks: List<VolunteerTask>) {
        renderTasks(tasks, "Запланированные задачи", VolunteerTaskStatus.PLANNED)
    }

    private fun SimplePanel.renderInProgressTasks(tasks: List<VolunteerTask>) {
        renderTasks(tasks, "Выполняющиеся задачи", VolunteerTaskStatus.IN_PROGRESS)
    }

    private fun SimplePanel.renderCompletedTasks(tasks: List<VolunteerTask>) {
        renderTasks(tasks, "Завершённые задачи", VolunteerTaskStatus.DONE)
    }

    private fun SimplePanel.renderTasks(tasks: List<VolunteerTask>, heading: String, status: VolunteerTaskStatus) {
        displayHeading {
            +heading
        }

        val filteredTasks = tasks.filter {
            it.status == status
        }

        val sortedAndFilteredTasks = filteredTasks.sortedByDescending {
            it.bannerImageId
        }

        grid {
            journalScope.launch {
                sortedAndFilteredTasks.forEach {
                    addTask(it)
                }
            }
        }
    }

    private inline fun SimplePanel.grid(crossinline block: SimplePanel.() -> Unit) {
        div(className = "row row-cols-1 row-cols-sm-2 row-cols-md-4") {
            block()
        }
    }

    private suspend fun SimplePanel.addTask(task: VolunteerTask) {
        val client = vormiPortalClient()

        card {
            background = Background(Color.BrighterLight)

            customizeBorderColor(task)

            setTaskBannerImage(client, task, "card-img-top")

            cardHeader {
                +task.title
            }

            cardBody {
                p(task.description, className = "card-text")
            }

            cardFooter {
                openVolunteerTaskModalButton(task)
            }
        }
    }

    private inline fun SimplePanel.card(crossinline block: SimplePanel.() -> Unit) {
        div(className = "col") {
            div(className = "card mb-5 shadow") {
                block()
            }
        }
    }

    private fun SimplePanel.customizeBorderColor(task: VolunteerTask) {
        when (task.status) {
            VolunteerTaskStatus.PLANNED -> {
                addCssClass("border-secondary")
            }

            VolunteerTaskStatus.IN_PROGRESS -> {
                addCssClass("border-primary")
            }

            VolunteerTaskStatus.DONE -> {
                addCssClass("border-success")
            }
        }
    }

    private inline fun SimplePanel.cardHeader(crossinline block: SimplePanel.() -> Unit) {
        div(className = "card-header") {
            block()
        }
    }

    private inline fun SimplePanel.cardBody(crossinline block: SimplePanel.() -> Unit) {
        div(className = "card-body") {
            block()
        }
    }

    private inline fun SimplePanel.cardFooter(crossinline block: SimplePanel.() -> Unit) {
        div(className = "card-footer text-bg-dark-subtle") {
            block()
        }
    }

    private fun SimplePanel.openVolunteerTaskModalButton(task: VolunteerTask) {
        button("", style = ButtonStyle.LINK) {
            infoCircleIcon()
            +"Подробнее про задачу"

            triggerVolunteerTaskModal(volunteerTaskModalComponent, task)
        }
    }
}