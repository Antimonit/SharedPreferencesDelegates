package me.khol.gradle

import com.android.build.gradle.api.BaseVariant
import java.io.File
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.accessors.runtime.conventionOf
import org.gradle.kotlin.dsl.findPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun androidTestTaskName(buildVariant: String): String =
    "test${buildVariant.capitalize()}UnitTest"

internal fun coverageTaskName(testTask: String): String =
    "${testTask}Coverage"

/**
 * Classes to be ignored from Jacoco coverage.
 *
 * The list of compiled classes provided to Jacoco for coverage includes all compiled code
 * required for the program to run. That also includes generated classes that we typically
 * do not care about when measuring coverage since we do not write tests for such classes.
 *
 * Classes and packages can be excluded using standard * and ? wildcard syntax:
 * - `*` matches zero or more characters
 * - `**` matches zero or more directories
 * - `?` matches a single character
 *
 * After running `test<buildVariant>UnitTestCoverage` task, you can check the coverage in report:
 *     /build/reports/jacoco/test<buildVariant>UnitTestCoverage/html/index.html
 */
internal val jacocoFileFilter = listOf(
    // Generated Android
    "**/R.class",
    "**/BuildConfig.class",

    // Generated Data Binding
    "**/BR.class",
    "**/databinding/*Binding.class",
    "**/databinding/*BindingImpl.class",
    "**/databinding/*BindingImpl\$*.class",
    "**/DataBinderMapperImpl.class",
    "**/DataBinderMapperImpl\$InnerBrLookup.class",
    "**/DataBinderMapperImpl\$InnerLayoutIdLookup.class",
    "**/DataBindingTriggerClass.class",

    // Generated Moshi
    "**/*JsonAdapter.class",

    // Generated Hilt
    "hilt_aggregated_deps/*.class",
    "dagger/hilt/android/internal/earlyentrypoint/codegen/*.class",
    "dagger/hilt/internal/aggregatedroot/codegen/*.class",

    // Generated Navigation Safe Args
    "**/*FragmentArgs.class",
    "**/*FragmentArgs\$Companion.class",
    "**/*\$\$inlined\$navArgs\$*.class",
)

/////////////////////
// Android extensions

fun BaseVariant.classDirectories(project: Project): FileTree {
    /*
     * We should provide Jacoco with compiled .class files. Without this configuration, nothing
     * will show up in the report.
     */
    val javaClasses = project.fileTree("${project.buildDir}/intermediates/javac/${name}/classes/") {
        exclude(jacocoFileFilter)
    }
    val kotlinClasses = project.fileTree("${project.buildDir}/tmp/kotlin-classes/${name}") {
        exclude(jacocoFileFilter)
    }
    return javaClasses + kotlinClasses
}

/**
 * Source directories are not critical for Jacoco to work but providing source code allow us
 * to check which lines are covered and which are not directly in the generated report.
 *
 * Does not include generated files.
 */
fun BaseVariant.sourceDirectories(): List<Iterable<File>> {
    /*
     * [BaseVariant.sourceSets] recognize all flavor variations but only for Java
     * source sets. Kotlin source sets are not recognized by default.
     * This is fine as long as we put Kotlin code into Java source sets, which is
     * picked up by the Kotlin compiler as well, but we cannot enforce this.
     *
     * ...\mylibrary\src\main\java
     * ...\mylibrary\src\large\java
     * ...\mylibrary\src\red\java
     * ...\mylibrary\src\redLarge\java
     * ...\mylibrary\src\debug\java
     * ...\mylibrary\src\redLargeDebug\java
     */
    val javaSourceDirs = sourceSets.map { it.javaDirectories }
    /*
     * For Kotlin, we can utilize java directories resolved for the current variant
     * but we need to map it to a specific convention.
     */
    val kotlinSourceDirs = sourceSets.map {
        conventionOf(it).findPlugin(KotlinSourceSet::class)?.kotlin?.srcDirs ?: emptySet()
    }
    return javaSourceDirs + kotlinSourceDirs
}

//////////////////////////
// Android-free extensions

fun SourceSetContainer.classDirectories(project: Project, buildType: String): List<FileTree> {
    // The following will resolve directories in which .class files are stored, such as:
    // $rootDir\oldschool\build\classes\java\main
    // $rootDir\pure\build\classes\java\main
    // $rootDir\pure\build\classes\kotlin\main
    val classesDirs = find { it.name == buildType }!!.output.classesDirs
    return classesDirs.map {
        project.fileTree(it) {
            exclude(jacocoFileFilter)
        }
    }
}

/**
 * Note that this returns ALL source sets, including `test` source set.
 */
fun Collection<SourceSet>.sourceDirectories(): List<FileCollection> {
    // Although the name `allJava` suggests it works only for Java, it would be more fitting
    //  if it was called allJvm, since it also includes Kotlin Source Sets automatically
    return map { it.allJava.sourceDirectories }
}
