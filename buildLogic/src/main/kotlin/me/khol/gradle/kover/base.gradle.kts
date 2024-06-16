package me.khol.gradle.kover

plugins {
    id("org.jetbrains.kotlinx.kover")
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "*.BuildConfig",
                )
            }
        }
    }
}
