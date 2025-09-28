/*
 * Copyright (c) 2025 Aurel Avramescu.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */

import net.researchgate.release.ReleaseExtension

plugins {
    kotlin("jvm") version "2.2.10"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
    id("net.researchgate.release") version "3.1.0"
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.4"
    id("signing")
    id("jacoco")
    id("dev.detekt") version "2.0.0-alpha.0"
}

detekt {
    toolVersion = "2.0.0-alpha.0"
    buildUponDefaultConfig = true
    config.setFrom(
        resources.text.fromString(
            """
            style:
              MaxLineLength:
                active: true
                maxLineLength: 150
            """.trimIndent(),
        ),
    )
}

description = "Small library for decoding/encoding NIST biometric files"
group = "eu.aagsolutions.img"

val slf4jVersion = "2.0.17"
val logbackVersion = "1.5.18"
val kotestVersion = "6.0.3"
val mockkVersion = "1.14.5"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

kotlin {
    jvmToolchain(21)
}

configure<ReleaseExtension> {
    ignoredSnapshotDependencies.set(listOf("net.researchgate:gradle-release"))
    with(git) {
        requireBranch.set("main")
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

centralPortal {
    username = System.getenv("MAVEN_USERNAME")
    password = System.getenv("MAVEN_PASSWORD")

    publishingType = net.thebugmc.gradle.sonatypepublisher.PublishingType.AUTOMATIC
    pom {
        group = "eu.aagsolutions.img"
        name = "nbis"
        url = "https://github.com/atdi/nbis"
        packaging = "jar"
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        scm {
            url.set("https://github.com/atdi/nbis")
            connection.set("scm:git:git://github.com/atdi/nbis.git")
            developerConnection.set("scm:git:ssh://github.com/atdi/nbis.git")
        }
        developers {
            developer {
                id.set("atdi")
                name.set("Aurel Avramescu")
                email.set("aav@aagsoltuions.eu")
                organization.set("https://www.aagsolutions.eu")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        System.getenv("GPG_PRIVATE_KEY"),
        System.getenv("GPG_PRIVATE_KEY_PASSWORD"),
    )
}
