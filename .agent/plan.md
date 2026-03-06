# Project Plan

Create a Kotlin Multiplatform library named GeoKt that provides functionality similar to Android's Matrix, Rect, RectF, Point, and PointF classes. The library should be usable on at least Android and iOS.

## Project Brief

The user wants to create a Kotlin Multiplatform library named "GeoKt".

**Features**:
- Core Geometry Primitives: Implementation of `Point`, `PointF`, `Rect`, and `RectF` classes for representing 2D coordinates and rectangles.
- 2D Transformation Matrix: A robust `Matrix` class that supports common 2D transformations like translation, rotation, scaling, and skewing.
- Cross-Platform Compatibility: Designed as a Kotlin Multiplatform library, ensuring consistent behavior and API across Android and iOS targets.
- Interoperability: Provide mechanisms for easy conversion to and from platform-specific geometry types (e.g., Android's `android.graphics.Rect`).

**High-Level Tech Stack**:
- Kotlin Multiplatform
- Kotlin Coroutines
- KSP (Kotlin Symbol Processing)

## Implementation Steps

### Task_1_SetupKMPLibraryModule: Restructure the project into a Kotlin Multiplatform library named 'GeoKt'. Create a new KMP library module and move the core logic into the 'commonMain' source set. The existing 'app' module will serve as a sample consumer for the library.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - A new KMP library module 'GeoKt' is created.
  - The project structure is updated to support KMP (Android and iOS targets).
  - The 'app' module depends on the 'GeoKt' library.
- **StartTime:** 2026-03-06 11:18:27 EST

### Task_2_ImplementGeometryPrimitives: Implement the core geometry data classes (Point, PointF, Rect, RectF) in the 'commonMain' source set of the 'GeoKt' module.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Point, PointF, Rect, and RectF classes are defined in GeoKt/src/commonMain.
  - The classes contain the appropriate properties (e.g., x, y, left, top, right, bottom).
  - The library module compiles successfully.

### Task_3_ImplementMatrixClass: Implement the Matrix class in 'commonMain' with support for 2D transformations (translation, rotation, scaling, skewing) and core matrix operations.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Matrix class is defined in GeoKt/src/commonMain.
  - Methods for translate, rotate, scale, and skew are implemented.
  - The library module compiles successfully.

### Task_4_IntegrateAndVerify: Integrate the 'GeoKt' library into the Android 'app' module. Create a simple Jetpack Compose UI to demonstrate a visual transformation (e.g., rotating a rectangle) using the Matrix class to verify the library's functionality.
- **Status:** PENDING
- **Acceptance Criteria:**
  - The 'app' module successfully uses classes from the 'GeoKt' library.
  - A Composable in the 'app' module demonstrates at least one transformation from the Matrix class.
  - The app builds, installs, and runs without crashing.
  - The implemented UI must match the design provided in input_images/ui_design_geokt.jpg.

