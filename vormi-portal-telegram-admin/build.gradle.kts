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

plugins {
    application
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    val logbackVersion: String by project
    val kmLogVersion: String by project

    val kredsVersion: String by project

    val tgbotapiVersion: String by project

    val hopliteVersion: String by project

    val koinVersion: String by project

    val dispatchVersion: String by project

    implementation(project(":vormi-portal-client-sdk"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.lighthousegames:logging:$kmLogVersion")

    implementation("io.github.crackthecodeabhi:kreds:$kredsVersion")

    implementation("dev.inmo:tgbotapi:$tgbotapiVersion")

    implementation("com.sksamuel.hoplite:hoplite-core:$hopliteVersion")

    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    implementation("com.rickbusarow.dispatch:dispatch-core:$dispatchVersion")
}

application {
    mainClass.set("tech.vtitbid.vormi.telegramadmin.MainKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
}