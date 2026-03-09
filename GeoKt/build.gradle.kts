import com.android.build.api.dsl.ManagedVirtualDevice
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlinx.kover)
}
group = "io.legere.geokt"
version = "1.0.0"
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
        val androidHostTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.truth)
                implementation(libs.robolectric)
                implementation(libs.junit)
            }
        }
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

mavenPublishing {
    publishToMavenCentral()

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
