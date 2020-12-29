plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf(".*jar"))))

    implementation(project(":injector"))
    implementation(project(":core:core-api"))
    implementation(project(":core:core-firebase"))

    implementation(Libraries.KOTLIN)
    implementation(Libraries.Coroutines.CORE)
    implementation(Libraries.Coroutines.ANDROID)

    // region UI.

    implementation(Libraries.COIL)

    // Navigation.
    implementation(Libraries.Android.Navigation)

    // Android.
    implementation(Libraries.Android.CORE)
    implementation(Libraries.Android.DESIGN)
    implementation(Libraries.Android.FRAGMENT)
    implementation(Libraries.Android.APPCOMPAT)
    implementation(Libraries.Android.CONSTRAINT_LAYOUT)

    // endregion

    // region DI.

    kapt(Libraries.Dagger.COMPILER)
    implementation(Libraries.Dagger.LIBRARY)

    // endregion

    // Architecture components.
    implementation(Libraries.Android.Lifecycle)

}