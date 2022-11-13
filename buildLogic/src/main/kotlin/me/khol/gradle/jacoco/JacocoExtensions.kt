package me.khol.gradle.jacoco

import me.khol.gradle.coverageTaskName
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

internal fun Project.registerJacocoReportTask(
    name: String,
    unitTestTask: String,
    classDirectories: Any,
    sourceDirectories: Any,
    execPath: String = "$buildDir/jacoco/${unitTestTask}.exec"
): TaskProvider<JacocoReport> = tasks.register<JacocoReport>(coverageTaskName(unitTestTask)) {
    dependsOn(unitTestTask)
    group = "Reporting"
    description = "Generate Jacoco coverage reports for the $name build"

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    this.classDirectories.from(classDirectories)
    this.sourceDirectories.from(sourceDirectories)

    executionData(
        files(execPath)
    )
}
