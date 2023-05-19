plugins {
    id("com.android.application")
    id("kotlin-android")
    //id("kotlin-parcelize")
    //id("kotlin-kapt")
}

android {
    namespace = "com.example.emptyapp"
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
        buildConfig = true
        viewBinding = true
    }

    signingConfigs {
        create("a_debug") {
            storeFile = file("../virogu-test.jks")
            storePassword = "123456"
            keyAlias = "test"
            keyPassword = "123456"
        }

        create("a_release") {
            storeFile = file("../virogu-test.jks")
            storePassword = "123456"
            keyAlias = "test"
            keyPassword = "123456"
        }

        create("b") {
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
            //signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            //signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions.addAll(listOf("product"))

    productFlavors {
        create("aFlavor") {
            dimension = "product"
            manifestPlaceholders.put("UID", "UIDA")

            buildTypes {
                release {
                    isDebuggable = true
                    signingConfig = signingConfigs.getByName("a_release")
                    isMinifyEnabled = false
                    isShrinkResources = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }

                debug {
                    signingConfig = signingConfigs.getByName("a_debug")
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }
        }

        create("bFlavor") {
            manifestPlaceholders.put("UID", "UIDB")
            dimension = "product"
            signingConfig = signingConfigs.getByName("b")
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

    implementation("androidx.biometric:biometric-ktx:1.2.0-alpha03")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.4.+")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    implementation("io.coil-kt:coil:2.3.0")

    navigationAll()

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.ext_junit)
    androidTestImplementation(Libs.Test.espresso_core)

}

