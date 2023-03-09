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

import dev.d1s.exkt.common.replaceIdPlaceholder
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import tech.vtitbid.vormi.commons.*

internal class DefaultVormiPortalClient(private val baseUrl: String, private val token: String?) : VormiPortalClient {

    private val httpClient =
        HttpClient {
            install(ContentNegotiation) {
                json()
            }

            defaultRequest {
                url(baseUrl)

                token?.let {
                    bearerAuth(it)
                }
            }
        }

    override suspend fun postVolunteerTask(title: String, description: String, status: VolunteerTaskStatus, bannerImageId: String?): Result<VolunteerTask> =
        runCatching {
            httpClient.post(Paths.PostVolunteerTask) {
                val body = VolunteerTaskModification(title, description, status, bannerImageId)

                setBody(body)
            }.body()
        }

    override suspend fun getVolunteerTask(id: String): Result<VolunteerTask> =
        runCatching {
            val path = Paths.GetVolunteerTask.replaceIdPlaceholder(id)

            httpClient.get(path).body()
        }

    override suspend fun getVolunteerTasks(limit: Int, offset: Int): Result<VolunteerTasks> =
        runCatching {
            httpClient.get(Paths.GetVolunteerTasks) {
                parameter(Paths.LimitQueryParameter, limit)
                parameter(Paths.OffsetQueryParameter, offset)
            }.body()
        }

    override suspend fun putVolunteerTask(id: String, title: String, description: String, status: VolunteerTaskStatus, bannerImageId: String?): Result<VolunteerTask> =
        runCatching {
            val path = Paths.PutVolunteerTask.replaceIdPlaceholder(id)

            httpClient.put(path) {
                val body = VolunteerTaskModification(title, description, status, bannerImageId)

                setBody(body)
            }.body()
        }

    override suspend fun deleteVolunteerTask(id: String): Result<Unit> =
        runCatching {
            val path = Paths.DeleteVolunteerTask.replaceIdPlaceholder(id)

            httpClient.delete(path)
        }

    override suspend fun postVolunteerTaskBanner(body: ByteReadChannel, contentLength: Long): Result<VolunteerTaskBanner> =
        runCatching {
            httpClient.post(Paths.PostVolunteerTaskBanner) {
                header(HttpHeaders.ContentType, ContentType.Image.JPEG)
                header(HttpHeaders.ContentLength, contentLength)

                setBody(body)
            }.body()
        }

    override fun getVolunteerTaskBannerUrl(id: String): Url =
        URLBuilder(baseUrl).apply {
            val path = Paths.GetVolunteerTaskBanner.replaceIdPlaceholder(id)

            path(path)
        }.build()

    override suspend fun getVolunteerTaskBanner(id: String): Result<VolunteerTaskBannerContents> =
        runCatching {
            val path = Paths.GetVolunteerTaskBanner.replaceIdPlaceholder(id)

            val response = httpClient.get(path)

            val channel = response.bodyAsChannel()
            val eTag = response.etag() ?: error("No ${HttpHeaders.ETag} header")
            val contentLength = response.contentLength() ?: error("No ${HttpHeaders.ContentLength} header")
            val lastModified = response.headers[HttpHeaders.LastModified] ?: error("No ${HttpHeaders.LastModified} header")

            VolunteerTaskBannerContents(id, channel, eTag, contentLength, lastModified)
        }

    override suspend fun putVolunteerTaskBanner(id: String, body: ByteReadChannel, contentLength: Long): Result<VolunteerTaskBanner> =
        runCatching {
            val path = Paths.PutVolunteerTaskBanner.replaceIdPlaceholder(id)

            httpClient.post(path) {
                header(HttpHeaders.ContentType, ContentType.Image.JPEG)
                header(HttpHeaders.ContentLength, contentLength)

                setBody(body)
            }.body()
        }

    override suspend fun deleteVolunteerTaskBanner(id: String): Result<Unit> =
        runCatching {
            val path = Paths.DeleteVolunteerTaskBanner.replaceIdPlaceholder(id)

            httpClient.delete(path)
        }
}