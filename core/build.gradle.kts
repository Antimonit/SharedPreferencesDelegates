plugins {
    id("me.khol.gradle.library")
}

android {
    namespace = "me.khol.spdelegates"
}

dependencies {
    implementation(project(":common"))

    testImplementation(libs.junit)
    testImplementation(libs.strikt)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.junit)
}
