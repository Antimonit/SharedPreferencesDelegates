plugins {
    id("me.khol.gradle.library")
}

android {
    namespace = "me.khol.spdelegates"
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.strikt:strikt-core:0.34.1")
    testImplementation("org.robolectric:robolectric:4.9")
    testImplementation("androidx.test.ext:junit:1.1.4")
}
