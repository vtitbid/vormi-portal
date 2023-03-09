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

package tech.vtitbid.vormi.server.service

import dev.d1s.exkt.dto.DtoConverter
import dev.d1s.exkt.dto.ResultingEntityWithOptionalDto
import dev.d1s.exkt.dto.convertToDtoIf
import dev.d1s.exkt.ktorm.dto.ResultingExportedSequenceWithOptionalDto
import dev.d1s.exkt.ktorm.dto.convertExportedSequenceToDtoIf
import io.ktor.server.plugins.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import tech.vtitbid.vormi.commons.VolunteerTask
import tech.vtitbid.vormi.server.configuration.DtoConverters
import tech.vtitbid.vormi.server.database.VolunteerTaskRepository
import tech.vtitbid.vormi.server.entity.VolunteerTaskEntity
import tech.vtitbid.vormi.server.entity.asString
import tech.vtitbid.vormi.server.validation.orThrow
import tech.vtitbid.vormi.server.validation.validateVolunteerTaskEntity
import java.util.*

interface VolunteerTaskService {

    suspend fun createTask(task: VolunteerTaskEntity, requireDto: Boolean = false): ResultingEntityWithOptionalDto<VolunteerTaskEntity, VolunteerTask>

    suspend fun getTask(id: UUID, requireDto: Boolean = false): ResultingEntityWithOptionalDto<VolunteerTaskEntity, VolunteerTask>

    suspend fun getTasks(limit: Int, offset: Int, requireDto: Boolean = false): ResultingExportedSequenceWithOptionalDto<VolunteerTaskEntity, VolunteerTask>

    suspend fun updateTask(id: UUID, modification: VolunteerTaskEntity, requireDto: Boolean = false): ResultingEntityWithOptionalDto<VolunteerTaskEntity, VolunteerTask>

    suspend fun removeTask(id: UUID): Result<Unit>
}

class DefaultVolunteerTaskService : VolunteerTaskService, KoinComponent {

    private val volunteerTaskRepository by inject<VolunteerTaskRepository>()

    private val volunteerTaskDtoConverter by inject<DtoConverter<VolunteerTaskEntity, VolunteerTask>>(qualifier = DtoConverters.VolunteerTaskDtoConverterQualifier)

    private val volunteerTaskBannerService by inject<VolunteerTaskBannerService>()

    private val logger = logging()

    override suspend fun createTask(task: VolunteerTaskEntity, requireDto: Boolean): ResultingEntityWithOptionalDto<VolunteerTaskEntity, VolunteerTask> =
        runCatching {
            logger.d {
                "Creating task ${task.asString}..."
            }

            task.validate()

            val addedTask = volunteerTaskRepository.addVolunteerTask(task).getOrThrow()

            addedTask to volunteerTaskDtoConverter.convertToDtoIf(addedTask) {
                requireDto
            }
        }

    override suspend fun getTask(id: UUID, requireDto: Boolean): ResultingEntityWithOptionalDto<VolunteerTaskEntity, VolunteerTask> =
        runCatching {
            logger.d {
                "Obtaining task with id $id..."
            }

            val task = volunteerTaskRepository.findVolunteerTask(id).getOrElse {
                throw NotFoundException(it.message)
            }

            task to volunteerTaskDtoConverter.convertToDtoIf(task) {
                requireDto
            }
        }

    override suspend fun getTasks(limit: Int, offset: Int, requireDto: Boolean): ResultingExportedSequenceWithOptionalDto<VolunteerTaskEntity, VolunteerTask> =
        runCatching {
            logger.d {
                "Obtaining tasks with limit $limit and offset $offset"
            }

            val tasks = volunteerTaskRepository.findAllVolunteerTasks(limit, offset).getOrThrow()

            tasks to volunteerTaskDtoConverter.convertExportedSequenceToDtoIf(tasks) {
                requireDto
            }
        }

    override suspend fun updateTask(id: UUID, modification: VolunteerTaskEntity, requireDto: Boolean): ResultingEntityWithOptionalDto<VolunteerTaskEntity, VolunteerTask> =
        runCatching {
            logger.d {
                "Updating task $id with data ${modification.asString}..."
            }

            modification.validate()

            val (originalTask, _) = this.getTask(id).getOrThrow()

            originalTask.apply {
                this.title = modification.title
                this.description = modification.description
                this.status = modification.status
                this.bannerImageId = modification.bannerImageId
            }

            val updatedTask = volunteerTaskRepository.updateVolunteerTask(originalTask).getOrThrow()

            updatedTask to volunteerTaskDtoConverter.convertToDtoIf(updatedTask) {
                requireDto
            }
        }

    override suspend fun removeTask(id: UUID): Result<Unit> =
        runCatching {
            logger.d {
                "Removing task $id..."
            }

            val (task, _) = this.getTask(id).getOrThrow()

            volunteerTaskRepository.removeVolunteerTask(task)
        }

    private suspend fun VolunteerTaskEntity.validate() {
        validateVolunteerTaskEntity(this).orThrow()
        checkBannerImageId()
    }

    private suspend fun VolunteerTaskEntity.checkBannerImageId() {
        val rawBannerId = bannerImageId

        if (rawBannerId != null) {
            val bannerId = UUID.fromString(rawBannerId)

            volunteerTaskBannerService.getTaskBanner(bannerId).getOrElse {
                throw NotFoundException("Banner with id $bannerId not found")
            }
        }
    }
}
