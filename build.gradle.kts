plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
    alias(libs.plugins.benchmark) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.gitSemVer)
}

gitSemVer {
    minimumVersion.set("1.0.0")

    buildMetadataSeparator.set("-")
}
