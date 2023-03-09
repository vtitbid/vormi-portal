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

package tech.vtitbid.vormi.client

import io.ktor.http.*
import io.ktor.utils.io.*
import tech.vtitbid.vormi.commons.VolunteerTask
import tech.vtitbid.vormi.commons.VolunteerTaskBanner
import tech.vtitbid.vormi.commons.VolunteerTaskStatus

interface VormiPortalClient {

    suspend fun postVolunteerTask(title: String, description: String, status: VolunteerTaskStatus, bannerImageId: String? = null): Result<VolunteerTask>

    suspend fun getVolunteerTask(id: String): Result<VolunteerTask>

    suspend fun getVolunteerTasks(limit: Int, offset: Int): Result<VolunteerTasks>

    suspend fun putVolunteerTask(id: String, title: String, description: String, status: VolunteerTaskStatus, bannerImageId: String? = null): Result<VolunteerTask>

    suspend fun deleteVolunteerTask(id: String): Result<Unit>

    suspend fun postVolunteerTaskBanner(body: ByteReadChannel, contentLength: Long): Result<VolunteerTaskBanner>

    fun getVolunteerTaskBannerUrl(id: String): Url

    suspend fun getVolunteerTaskBanner(id: String): Result<VolunteerTaskBannerContents>

    suspend fun putVolunteerTaskBanner(id: String, body: ByteReadChannel, contentLength: Long): Result<VolunteerTaskBanner>

    suspend fun deleteVolunteerTaskBanner(id: String): Result<Unit>
}