buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.plugin.android)
        classpath(libs.plugin.kotlin)
        classpath(libs.plugin.kotlin.serialization)
    }
}

plugins {
    id("me.khol.gradle.kover.root")
}

dependencies {
    kover(project(":core"))
    kover(project(":moshi"))
    kover(project(":serialization"))
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
