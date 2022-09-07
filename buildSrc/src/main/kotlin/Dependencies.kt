import org.gradle.api.JavaVersion

object Versions {
    const val compileSdk = 32
    const val minSdk = 21
    const val targetSdkVersion = 32

    const val kotlinVersion: String = "1.6.21"

    val compileJavaVersion: JavaVersion = JavaVersion.VERSION_11
}

object Libs {
    object Common {
        const val appcompat = "androidx.appcompat:appcompat:1.5.0"
        const val core_ktx = "androidx.core:core-ktx:1.8.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
        const val preference_ktx = "androidx.preference:preference-ktx:1.2.0"
    }

    object Recyclerview {
        const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
        const val selection = "androidx.recyclerview:recyclerview-selection:1.1.0"
    }

    object Paging {
        const val paging = "androidx.paging:paging-runtime-ktx:3.1.1"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val converter_gson = "com.squareup.retrofit2:converter-gson:$version"
    }

    object ViewPager {
        const val viewPager = "androidx.viewpager2:viewpager2:1.0.0"
    }

    object CardView {
        const val cardView = "androidx.cardview:cardview:1.0.0"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val ext_junit = "androidx.test.ext:junit:1.1.3"
        const val espresso_core = "androidx.test.espresso:espresso-core:3.4.0"
    }

    object Material {
        const val material = "com.google.android.material:material:1.6.1"
    }

    object Timber {
        const val timber = "com.jakewharton.timber:timber:5.0.1"
    }

    object Kotlin {
        // https://github.com/JetBrains/kotlin
        const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:1.6.0"
    }

    object Coroutines {
        private const val version = "1.6.1"

        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object Lifecycle {
        private const val version = "2.5.1"

        const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    }

    object PermissionX {
        const val permissionX = "com.guolindev.permissionx:permissionx:1.6.4"
    }

    object Navigation {
        private const val version = "2.5.1"

        const val ui = "androidx.navigation:navigation-ui-ktx:$version"
        const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
    }

    object Tencent {
        const val mars_xlog = "com.tencent.mars:mars-xlog:1.2.6"
    }

}