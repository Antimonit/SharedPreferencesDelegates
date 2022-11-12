plugins {
    id("me.khol.gradle.library")
    kotlin("kapt")
}

android {
    namespace = "me.khol.spdelegates.moshi"
}

dependencies {
    api(project(":core"))

    api(libs.moshi)
    kaptTest(libs.moshi.codegen)

    testImplementation(libs.junit)
    testImplementation(libs.strikt)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.junit)
}
