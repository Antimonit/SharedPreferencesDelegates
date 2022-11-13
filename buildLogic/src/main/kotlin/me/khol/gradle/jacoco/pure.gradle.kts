package me.khol.gradle.jacoco

import me.khol.gradle.classDirectories
import me.khol.gradle.sourceDirectories

plugins {
    jacoco
}

// It should be safe to assume that the predefined sourceSet is called 'main' and the default
// task for unit test is called 'test'.

val sourceSetName = "main"
val unitTestTask = "test"
val sourceSets = project.extensions.getByType<SourceSetContainer>()

registerJacocoReportTask(
    name = sourceSetName,
    unitTestTask = unitTestTask,
    classDirectories = sourceSets.classDirectories(project, sourceSetName),
    sourceDirectories = sourceSets.sourceDirectories(),
)
