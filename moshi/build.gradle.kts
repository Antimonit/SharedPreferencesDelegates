plugins {
    id("me.khol.gradle.library")
    kotlin("kapt")
}

android {
    namespace = "me.khol.spdelegates.moshi"
}

dependencies {
    api(project(":core"))

    api("com.squareup.moshi:moshi:1.14.0")
    kaptTest("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.strikt:strikt-core:0.34.1")
    testImplementation("org.robolectric:robolectric:4.9")
    testImplementation("androidx.test.ext:junit:1.1.4")
}
