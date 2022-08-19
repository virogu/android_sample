plugins {
    commons.application
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "com.virogu.systemalert"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdkVersion
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Versions.compileJavaVersion
        targetCompatibility = Versions.compileJavaVersion
    }
    kotlinOptions {
        jvmTarget = Versions.compileJavaVersion.toString()
    }
    lint {
        abortOnError = false
    }
}

dependencies {
    application()
    implementation(project(":library"))
    implementation(Libs.Common.preference_ktx)
    implementation(Libs.Timber.timber)
    implementation(Libs.PermissionX.permissionX)
    implementation("io.mattcarroll.hover:hover:0.9.8")

    test()
}