# GeoKt - Geometery classes: 

Platform independent replacement for Android's with Immutable variants.

## Usage
This library is published to Maven Central. To use it, add the following to your build files.

### For a Kotlin Multiplatform Project
First, ensure `mavenCentral()` is in your repository list (usually in `settings.gradle.kts`):
```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
```

Then, add the dependency to the `commonMain` source set in your module's `build.gradle.kts`. The Kotlin Multiplatform plugin will automatically use the correct artifact for each target (Android, iOS, JVM, etc.).

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("io.legere:geokt:1.0.0")
            }
        }
    }
}
```

### For an Android-Only Project
Add `mavenCentral()` to your repository list (usually in `settings.gradle.kts`) and then add the following dependency to your app's `build.gradle.kts` file. Gradle's variant-aware dependency management will automatically select the correct artifact for Android.
```kotlin
dependencies {
    implementation("io.legere:geokt:1.0.0")
}
```

## Classes
 * KtMatrix - A 3x3 (2D) transformation matrix
 * KtRect  - An integer based rect class
 * KtRectF  - A float based rect class
 * KtPoint - An integer base point class
 * KtPointF  - a float base point class
