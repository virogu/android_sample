plugins {
    id("com.android.application")
    id("kotlin-android")
    //id("kotlin-parcelize")
    //id("kotlin-kapt")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "com.example.serialportdemo"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdkVersion
        versionCode = 1
        versionName = "1.0"

        ndk {
            abiFilters.add("arm64-v8a")
        }

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

    // 自定义多渠道打包 apk名称
    applicationVariants.all {
        val buildType = buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                if (buildType == "release") {
                    outputFileName = "SerialProtDemo-${versionName}.apk"
                }
            }
        }
    }

    // 多渠道配置
//    flavorDimensions("code")
//    productFlavors {
//        create("google")
//        create("baidu")
//        create("other")
//    }
//    productFlavors.all {
//        manifestPlaceholders["CHANNEL_VALUE"] = name
//    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        cmake {
            path("CMakeLists.txt")
        }
    }
}

dependencies {

    implementation(Libs.Common.appcompat)
    implementation(Libs.Common.core_ktx)
    implementation(Libs.Common.constraintLayout)
    implementation(Libs.Common.preference_ktx)

    implementation(Libs.Timber.timber)

    implementation(Libs.Kotlin.kotlin_stdlib)
    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)

    implementation(Libs.Material.material)
    implementation(Libs.Lifecycle.runtimeKtx)
    implementation(Libs.Lifecycle.viewModelKtx)
    implementation(Libs.Lifecycle.livedataKtx)
    //implementation(Libs.Lifecycle.extensions)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.ext_junit)
    androidTestImplementation(Libs.Test.espresso_core)

}

