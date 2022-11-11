plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "me.khol.spdelegates"
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
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.strikt:strikt-core:0.34.1")
    testImplementation("org.robolectric:robolectric:4.9")
    testImplementation("androidx.test.ext:junit:1.1.4")
}
