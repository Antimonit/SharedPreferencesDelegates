package me.khol.gradle.kover

plugins {
    id("org.jetbrains.kotlinx.kover")
}

koverReport {
    filters {
        excludes {
            classes(
                "*.BuildConfig",
            )
        }
    }
}
