plugins {
    id("com.android.application")
    id("kotlin-android")
    //id("kotlin-parcelize")
    //id("kotlin-kapt")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "com.example.emptyapp"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdkVersion
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        jniLibs.pickFirsts.add("lib/x86/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/x86_64/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/armeabi-v7a/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/arm64-v8a/libc++_shared.so")
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
    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(project(":library"))
    implementation(files("libs/xlog-release.aar"))

    implementation(Libs.Common.appcompat)
    implementation(Libs.Common.core_ktx)
    implementation(Libs.Common.constraintLayout)
    implementation(Libs.Kotlin.kotlin_stdlib)

    implementation(Libs.Timber.timber)

    implementation(Libs.Material.material)

    implementation(Libs.Tencent.mars_xlog)

    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)

    implementation(Libs.Lifecycle.runtimeKtx)
    implementation(Libs.Lifecycle.viewModelKtx)
    implementation(Libs.Lifecycle.livedataKtx)
    implementation(Libs.Lifecycle.extensions)

    navigationAll()

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.ext_junit)
    androidTestImplementation(Libs.Test.espresso_core)

}

