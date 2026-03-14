/*
 * Copyright (c) 2026 Legere Technologies LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Some of the mathematical logic in this file is a direct translation
 * of the C++ implementation in Google's Skia Graphics Library, which
 * is also licensed under a permissive open-source license.
 *
 */

import com.android.build.api.dsl.ManagedVirtualDevice
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlinx.kover)
    signing
}
group = "io.legere"
// Resolve version robustly
val ver = project.version.toString()
val finalVersion =
    if (ver == "unspecified") {
        val rootVer = project.rootProject.version.toString()
        if (rootVer == "unspecified") {
            error(
                "Project version is 'unspecified'. Check git-sensitive-semantic-versioning configuration or tags.",
            )
        }
        rootVer
    } else {
        ver
    }
version = finalVersion

//version = "1.0.0"
fun isHostArm(): Boolean {
    val osArch = System.getProperty("os.arch")
    return osArch.contains("aarch64") || osArch.contains("arm64")
}

kotlin {
    jvm()
    android {
        namespace = "io.legere.geokt"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {
            @Suppress("UnstableApiUsage")
            withDeviceTest {
                managedDevices {
                    allDevices {
                        create("pixel7GoogleApis", ManagedVirtualDevice::class) {
                            device = "Pixel 7"
                            apiLevel = 32
                            systemImageSource = "google-atd"
//                        systemImageSource = "google-atd"
//                        systemImageSource = "aosp-atd"
                            testedAbi =
                                if (isHostArm()) {
                                    // On ARM hosts, prefer arm64-v8a for this device if your app supports it
                                    "arm64-v8a"
                                } else {
                                    // On x86_64 hosts, use x86_64 to avoid translation warning
                                    "x86_64"
                                }
                        }
                    }
                }

            }

        }

        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }


    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.assertk)
        }
        @Suppress("unused")
        val androidHostTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.truth)
                implementation(libs.robolectric)
                implementation(libs.junit)
            }
        }
        @Suppress("unused")
        val androidDeviceTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.truth)
                implementation(libs.ext.junit)
                implementation(libs.espresso.core)
            }
        }
    }
}

kover {
    reports {
        filters {
            excludes {
                classes("*.BuildConfig", "*.R", "*.Manifest", $$"*$DefaultImpls")
            }
        }
    }
}



signing {
    val keyId = project.properties["signing.keyId"]?.toString() ?: ""
    val password = project.properties["signing.password"]?.toString() ?: ""
    if (keyId.isNotBlank() && password.isNotBlank()) {
        configure<PublishingExtension> {
            publications.withType<MavenPublication> {
                sign(this)
            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    signAllPublications()

    coordinates(group.toString(), "geokt", version.toString())

    pom {
        name = "GeoKt"
        description = "KMP Matrix, Rect, RectF, Point, PointF compatbile with Android's."
        inceptionYear = "2026"
        url = "https://github.com/johngray1965/GeoKt/"
        licenses {
            license {
                name = "Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "johngray1965"
                name = "John Gray"
                url = "scm:git@github.com:johngray1965/GeoKt.git"
            }
        }
        scm {
            url = "https://github.com/johngray1965/GeoKt"
            connection = "scm:git@github.com:johngray1965/GeoKt.git"
            developerConnection = "scm:git@github.com:johngray1965/GeoKt.git"
        }
    }
}
