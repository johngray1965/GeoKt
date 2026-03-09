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

//publishOnCentral {
////    repoOwner.set("johngray1965")
////    projectLongName.set("Geometry classes for Kotlin Multiplatform Project")
////    projectDescription.set("Geometry classes for Kotlin Multiplatform Project")
//
//    repoOwner = "johngray1965"
////    projectDescription.set(rootProject.properties["POM_DESCRIPTION"] as String)
//// The following values are the default, if they are ok with you, just omit them
//    projectDescription = "Geometry classes for Kotlin Multiplatform Project"
//    projectLongName = "GeoKt"
//    licenseName = "Apache License, Version 2.0"
//    licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0"
//    projectUrl.set("https://github.com/johngray1965/GeoKt")
//    scmConnection.set("scm:git:https://github.com/johngray1965/GeoKt.git")
//
//    repository("https://maven.pkg.github.com/johngray1965/GeoKt".lowercase()) {
//        user.set(
//            System.getenv("GITHUB_USERNAME") ?: project.findProperty("GITHUB_USERNAME") as? String
//            ?: ""
//        )
//        password.set(
//            System.getenv("GITHUB_TOKEN") ?: project.findProperty("GITHUB_TOKEN") as? String ?: ""
//        )
//    }
//    publishing {
//        publications {
//            withType<MavenPublication> {
//                pom {
//                    developers {
//                        developer {
//                            id.set("johngray1965")
//                            name = "John Gray"
//                            email = "johngray1965@gmail.com"
//                            url = "https://github.com/johngray1965"
//                            organization = "Legere"
//                            organizationUrl = "https://legere.io"
//
//                        }
//                    }
//                }
//            }
//        }
//    }
//}


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
