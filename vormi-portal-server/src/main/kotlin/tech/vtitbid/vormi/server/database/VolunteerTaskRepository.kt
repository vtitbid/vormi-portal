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

package tech.vtitbid.vormi.server.database

import dev.d1s.exkt.ktorm.ExportedSequence
import dev.d1s.exkt.ktorm.export
import dispatch.core.withIO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.database.Database
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import tech.vtitbid.vormi.server.entity.VolunteerTaskEntity
import java.util.*

interface VolunteerTaskRepository {

    suspend fun addVolunteerTask(volunteerTask: VolunteerTaskEntity): Result<VolunteerTaskEntity>

    suspend fun findVolunteerTask(id: UUID): Result<VolunteerTaskEntity>

    suspend fun findAllVolunteerTasks(limit: Int, offset: Int): Result<ExportedSequence<VolunteerTaskEntity>>

    suspend fun updateVolunteerTask(volunteerTask: VolunteerTaskEntity): Result<VolunteerTaskEntity>

    suspend fun removeVolunteerTask(volunteerTask: VolunteerTaskEntity): Result<Unit>
}

class DefaultVolunteerTaskRepository : VolunteerTaskRepository, KoinComponent {

    private val database by inject<Database>()

    override suspend fun findAllVolunteerTasks(limit: Int, offset: Int) =
        withIO {
            runCatching {
                database.volunteerTasks.export(limit, offset, sort = { it.createdAt.desc() })
            }
        }


    override suspend fun addVolunteerTask(volunteerTask: VolunteerTaskEntity) =
        withIO {
            runCatching {
                volunteerTask.apply {
                    setId()
                    setCreatedAt()
                    setUpdatedAt()

                    database.volunteerTasks.add(volunteerTask)
                }
            }
        }

    override suspend fun findVolunteerTask(id: UUID): Result<VolunteerTaskEntity> =
        withIO {
            runCatching {
                database.volunteerTasks.find {
                    it.id eq id
                } ?: error("Volunteer task not found")
            }
        }

    override suspend fun updateVolunteerTask(volunteerTask: VolunteerTaskEntity) =
        withIO {
            runCatching {
                volunteerTask.apply {
                    setUpdatedAt()
                    flushChanges()
                }
            }
        }

    override suspend fun removeVolunteerTask(volunteerTask: VolunteerTaskEntity) =
        withIO {
            runCatching {
                volunteerTask.delete()
                Unit
            }
        }
}