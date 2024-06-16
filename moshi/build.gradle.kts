plugins {
    id("me.khol.gradle.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "me.khol.spdelegates.moshi"
}

dependencies {
    api(project(":core"))
    implementation(project(":common"))

    api(libs.moshi)
    kspTest(libs.moshi.codegen)

    testImplementation(libs.junit)
    testImplementation(libs.strikt)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.junit)
}
