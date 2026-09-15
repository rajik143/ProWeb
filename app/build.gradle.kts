plugins {
    id("com.android.application")
}

android {
    namespace = "com.github.warren_bank.webmonkey"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.aistudio.webmonkey.kptmqz"
        minSdk = 21
        targetSdk = 35
        versionCode = 4130211
        versionName = "4.13.02"
    }

    signingConfigs {
        create("debugConfig") {
            storeFile = file("${rootDir}/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debugConfig")
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.webkit:webkit:1.12.1")
}
