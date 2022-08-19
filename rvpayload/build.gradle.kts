plugins {
    id("com.android.application")
    id("kotlin-android")
    //id("kotlin-parcelize")
    //id("kotlin-kapt")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "com.example.rvpayload"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdkVersion
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        //dataBinding = true
        viewBinding = true
    }

    signingConfigs {
        create("release") {
            storeFile = file("../virogu-test.jks")
            storePassword = "123456"
            keyAlias = "test"
            keyPassword = "123456"
        }

        getByName("debug") {
            storeFile = file("../virogu-test.jks")
            storePassword = "123456"
            keyAlias = "test"
            keyPassword = "123456"
        }
    }

    buildTypes {
        release {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(Libs.Common.appcompat)
    implementation(Libs.Common.core_ktx)
    implementation(Libs.Common.constraintLayout)

    implementation(Libs.Material.material)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.ext_junit)
    androidTestImplementation(Libs.Test.espresso_core)

}

