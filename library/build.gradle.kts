plugins {
    id("com.android.library")
    id("kotlin-android")
    //id("kotlin-parcelize")
    //id("kotlin-kapt")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        //dataBinding = true
        //viewBinding = true
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
        jvmTarget = "${Versions.compileJavaVersion}"
    }
    sourceSets {
        //main {
        //    jniLibs.srcDirs += ['libs']
        //}
    }
}

dependencies {

    implementation(Libs.Common.appcompat)
    implementation(Libs.Common.core_ktx)
    implementation(Libs.Material.material)
    implementation(Libs.Common.constraintLayout)
    implementation(Libs.Common.preference_ktx)

    implementation(Libs.Kotlin.kotlin_stdlib)

    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)
    implementation(Libs.Lifecycle.runtimeKtx)
    implementation(Libs.Lifecycle.viewModelKtx)
    implementation(Libs.Lifecycle.livedataKtx)
    implementation(Libs.PermissionX.permissionX)
    //implementation(Libs.Lifecycle.extensions)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.ext_junit)
    androidTestImplementation(Libs.Test.espresso_core)

}

