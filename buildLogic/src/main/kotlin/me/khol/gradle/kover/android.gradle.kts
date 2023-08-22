package me.khol.gradle.kover

plugins {
    id("me.khol.gradle.kover.base")
}

koverReport {
    defaults {
        // Tasks such as `koverXmlReport` will automatically include coverage for the debug variant.
        // If not configured, `koverXmlReport` will produce empty coverage.
        // We can still run `koverXmlReportDebug`, but then it is a bit more complicated to
        // configure from the root build.gradle.kts script.
        mergeWith("debug")
    }
}
