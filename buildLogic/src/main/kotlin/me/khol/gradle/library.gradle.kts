package me.khol.gradle

import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("me.khol.gradle.kover.android")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 16

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(
        groupId = property("POM_GROUP_ID") as String,
        artifactId = property("POM_ARTIFACT_ID") as String,
        version = property("POM_VERSION") as String,
    )

    pom {
        name.set("SharedPreferences Delegates")
        description.set("Type-safe delegates for Android SharedPreferences")
        inceptionYear.set("2022")
        url.set("https://github.com/Antimonit/SharedPreferencesDelegates")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("antimonit")
                name.set("David Khol")
                url.set("https://github.com/Antimonit/")
            }
        }
        scm {
            url.set("https://github.com/Antimonit/SharedPreferencesDelegates")
            connection.set("scm:git:git://github.com/Antimonit/SharedPreferencesDelegates.git")
            developerConnection.set("scm:git:ssh://git@github.com/Antimonit/SharedPreferencesDelegates.git")
        }
    }
}
