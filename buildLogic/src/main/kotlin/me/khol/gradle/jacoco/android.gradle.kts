package me.khol.gradle.jacoco

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.plugins.AppPlugin
import com.android.build.gradle.internal.plugins.LibraryPlugin
import me.khol.gradle.androidTestTaskName
import me.khol.gradle.classDirectories
import me.khol.gradle.sourceDirectories
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension

plugins {
    jacoco
}

plugins.withType<AppPlugin> {
    val extension = extensions.getByType<AppExtension>()
    extension.setUpRobolectricForJacoco()
    setUpAndroidVariants(extension.applicationVariants)
}

plugins.withType<LibraryPlugin> {
    val extension = extensions.getByType<LibraryExtension>()
    extension.setUpRobolectricForJacoco()
    setUpAndroidVariants(extension.libraryVariants)
}

/*
 * > If enabled this uses Jacoco to capture coverage and creates a report in the build directory.
 *
 * But we already configure Jacoco ourselves! Enabling this interferes with our Jacoco tasks and
 * leads to errors such as:
 *     java.lang.instrument.IllegalClassFormatException: Error while instrumenting <path_to_class>.
 *     Caused by: java.io.IOException: Error while instrumenting <path_to_class>.
 *     Caused by: java.lang.IllegalStateException: Cannot process instrumented class <path_to_class>. Please supply original non-instrumented classes.
 *
 * See https://issuetracker.google.com/issues/178015739
 *
 * This might be necessary to enable in the future if we want to gather coverage data from
 * instrumentation tests as well. But we do not support instrumentation tests at the moment.
 */
@Suppress("unused")
@Deprecated(
    message = "Do not enable testCoverageEnabled when running unit tests with Jacoco",
    replaceWith = ReplaceWith("")
)
fun BaseExtension.enableInstrumentationTestCoverage() {
    buildTypes {
        named("debug") {
            isTestCoverageEnabled = true
        }
    }
}

fun BaseExtension.setUpRobolectricForJacoco() {
    testOptions {
        unitTests.all {
            it.extensions.configure<JacocoTaskExtension> {
                // Enable code coverage for Robolectric tests using `AndroidJUnit4` runner.
                // See https://github.com/gradle/gradle/pull/575#issuecomment-189957212
                isIncludeNoLocationClasses = true
                // Also filter out weird weird exceptions introduced by the line above, such as:
                //     java.lang.NoClassDefFoundError: jdk/internal/reflect/GeneratedSerializationConstructorAccessor1
                //     java.lang.NoClassDefFoundError: jdk/internal/reflect/GeneratedSerializationConstructorAccessor2
                // This is most likely caused by Jacoco trying to access a class that was deleted
                // in Java 9.
                // See https://github.com/gradle/gradle/issues/5184#issuecomment-457865951
                excludes = listOf("jdk.internal.*")
            }
        }
    }
}

/*
 * Sets up Jacoco to work with either `com.android.application` or `com.android.library` plugin.
 * The configuration expects that Kotlin plugin is applied as well.
 *
 * UI tests are not included in the report and are not required to run in order to generate the
 * report. To support UI tests, enable `isTestCoverageEnabled = true` for the build types we want
 * to test and make JacocoReport task depend on `"create${variantName.capitalize()}CoverageReport"`
 * task.
 *
 * In case we use `isTestCoverageEnabled = true` we have to update `execPath` to:
 *   "$buildDir/outputs/unit_test_code_coverage/${variantName}UnitTest/${unitTestTask}.exec"
 */
fun setUpAndroidVariants(androidVariants: DomainObjectSet<out BaseVariant>) {
    androidVariants.all {
        val variant = this
        val variantName = variant.name
        val unitTestTask = androidTestTaskName(variantName)

        registerJacocoReportTask(
            name = variantName,
            unitTestTask = unitTestTask,
            classDirectories = variant.classDirectories(project),
            sourceDirectories = variant.sourceDirectories(),
        )
    }
}
