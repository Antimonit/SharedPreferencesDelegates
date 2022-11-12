plugins {
    id("me.khol.gradle.library")
    kotlin("plugin.serialization")
}

android {
    namespace = "me.khol.spdelegates.kotlinx.serialization"
}

dependencies {
    api(project(":core"))

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.strikt:strikt-core:0.34.1")
    testImplementation("org.robolectric:robolectric:4.9")
    testImplementation("androidx.test.ext:junit:1.1.4")
}
