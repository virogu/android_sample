import Versions.composeVersion
import Versions.kotlinVersion
import org.gradle.api.JavaVersion

object Versions {
    const val compileSdk = 33
    const val minSdk = 21
    const val targetSdkVersion = 32

    const val composeVersion: String = "1.4.3"

    const val kotlinVersion: String = "1.8.20"

    val compileJavaVersion: JavaVersion = JavaVersion.VERSION_11
}

object Libs {
    object Common {
        const val appcompat = "androidx.appcompat:appcompat:1.6.1"
        const val core_ktx = "androidx.core:core-ktx:1.10.1"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
        const val preference_ktx = "androidx.preference:preference-ktx:1.2.0"
    }

    object Kotlinx {
        object Serialization {
            private const val version = "1.3.2"
            const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:$version"
            const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
        }
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${composeVersion}"
        const val ui_tool = "androidx.compose.ui:ui-tooling:${composeVersion}"
        const val ui_tool_preview = "androidx.compose.ui:ui-tooling-preview:${composeVersion}"
        const val material = "androidx.compose.material:material:${composeVersion}"
        const val activity = "androidx.activity:activity-compose:1.6.1"

        const val navigation = "androidx.navigation:navigation-compose:2.5.0"

        const val ui_test = "androidx.compose.ui:ui-test-junit4:${composeVersion}"

        object Accompanist {
            //https://google.github.io/accompanist/
            private const val accompanistVersion = "0.24.12-rc"

            //Navigation
            const val navigation_md =
                "com.google.accompanist:accompanist-navigation-material:$accompanistVersion"
            const val navigation_animation =
                "com.google.accompanist:accompanist-navigation-animation:$accompanistVersion"

            //Pager
            const val pager = "com.google.accompanist:accompanist-pager:$accompanistVersion"
            const val pager_indicators =
                "com.google.accompanist:accompanist-pager-indicators:$accompanistVersion"

            //SwipeRefresh
            const val swiperefresh =
                "com.google.accompanist:accompanist-swiperefresh:$accompanistVersion"

            //Placeholder
            const val placeholder_md =
                "com.google.accompanist:accompanist-placeholder-material:$accompanistVersion"

            //flowlayout
            const val flowlayout =
                "com.google.accompanist:accompanist-flowlayout:$accompanistVersion"
        }
    }

    object Room {
        private const val version = "2.5.2"
        const val runtime = "androidx.room:room-runtime:$version"
        const val ktx = "androidx.room:room-ktx:$version"
        const val compiler = "androidx.room:room-compiler:$version"
    }

    object DataStore {
        private const val version = "1.0.0"
        const val core = "androidx.datastore:datastore:$version"
        const val preferences = "androidx.datastore:datastore-preferences:$version"
    }

    object Recyclerview {
        private const val version = "1.3.0"
        const val recyclerview = "androidx.recyclerview:recyclerview:$version"
        const val selection = "androidx.recyclerview:recyclerview-selection:1.1.0"
    }

    object Paging {
        const val paging = "androidx.paging:paging-runtime-ktx:3.1.1"
        const val compose = "androidx.paging:paging-compose:3.2.0-rc01"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val converter_gson = "com.squareup.retrofit2:converter-gson:$version"
        const val converter_scalars = "com.squareup.retrofit2:converter-scalars:$version"
        const val kotlinx_serialization_converter =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
        const val okhttp3 = "com.squareup.okhttp3:okhttp:4.10.0"
    }

    object Coil {
        const val compose = "io.coil-kt:coil-compose:2.1.0"
    }

    object Work {
        private const val workVersion = "2.8.1"

        // (Java only)
        const val java = "androidx.work:work-runtime:$workVersion"

        // Kotlin + coroutines
        const val ktx = "androidx.work:work-runtime-ktx:$workVersion"
        const val rxjava2 = "androidx.work:work-rxjava2:$workVersion"
        const val gcm = "androidx.work:work-gcm$workVersion"
        const val testing = "androidx.work:work-testing:$workVersion"
        const val multi_process = "androidx.work:work-multiprocess:$workVersion"
    }

    object ViewPager {
        const val viewPager = "androidx.viewpager2:viewpager2:1.0.0"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val ext_junit = "androidx.test.ext:junit:1.1.3"
        const val espresso_core = "androidx.test.espresso:espresso-core:3.4.0"
    }

    object Material {
        const val material = "com.google.android.material:material:1.9.0"
    }

    object Timber {
        const val timber = "com.jakewharton.timber:timber:5.0.1"
    }

    object Kotlin {
        // https://github.com/JetBrains/kotlin
        const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    }

    object Coroutines {
        private const val version = "1.6.1"

        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object Lifecycle {
        private const val version = "2.6.1"

        const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    }

    object PermissionX {
        const val permissionX = "com.guolindev.permissionx:permissionx:1.6.1"
    }

    object Navigation {
        private const val version = "2.6.0"

        const val ui = "androidx.navigation:navigation-ui-ktx:$version"
        const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
    }

    object Tencent {
        const val mars_xlog = "com.tencent.mars:mars-xlog:1.2.6"
    }

    object PhilJay {
        const val MPAndroidChart = "com.github.PhilJay:MPAndroidChart:v3.1.0"
    }

    object Kodein {
        private const val version = "7.14.0"

        // https://github.com/Kodein-Framework/Kodein-DI
        const val di_jvm = "org.kodein.di:kodein-di-jvm:${version}"
        const val di_framework_android_x =
            "org.kodein.di:kodein-di-framework-android-x:${version}"
        const val di_conf_jvm = "org.kodein.di:kodein-di-conf-jvm:${version}"
    }

}