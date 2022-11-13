plugins {
    id("me.khol.gradle.library")
    kotlin("plugin.serialization")
}

android {
    namespace = "me.khol.spdelegates.kotlinx.serialization"
}

dependencies {
    api(project(":core"))
    implementation(project(":common"))

    api(libs.kotlinx.serialization)

    testImplementation(libs.junit)
    testImplementation(libs.strikt)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
}
