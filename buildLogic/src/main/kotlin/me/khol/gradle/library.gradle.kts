package me.khol.gradle

import org.gradle.api.credentials.PasswordCredentials

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
    id("me.khol.gradle.jacoco.android")
}

val publicationRelease = "release"

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 16
        targetSdk = 33

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

    publishing {
        /*
         * Executing `gradlew publishReleasePublicationToMavenLocal` will generate plain,
         * variant-free outputs, such as:
         * * my-lib-1.0.0-javadoc.jar
         * * my-lib-1.0.0-sources.jar
         * * my-lib-1.0.0.aar
         * * my-lib-1.0.0.module
         * * my-lib-1.0.0.pom
         */
        singleVariant(publicationRelease) {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>(publicationRelease) {
                from(components.getByName(publicationRelease))
                groupId = property("POM_GROUP_ID") as String
                artifactId = property("POM_ARTIFACT_ID") as String
                version = property("POM_VERSION") as String
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
        }
        repositories {
            maven {
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials(PasswordCredentials::class.java)
            }
        }
    }
}
