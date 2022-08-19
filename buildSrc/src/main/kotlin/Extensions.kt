import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.application() = this.apply {
    add("implementation", Libs.Common.appcompat)
    add("implementation", Libs.Common.core_ktx)
    add("implementation", Libs.Common.constraintLayout)
    add("implementation", Libs.Kotlin.kotlin_stdlib)
    add("implementation", Libs.Material.material)
}

fun DependencyHandlerScope.library() = this.apply {
    add("implementation", Libs.Common.appcompat)
    add("implementation", Libs.Common.core_ktx)
    add("implementation", Libs.Common.constraintLayout)
    add("implementation", Libs.Kotlin.kotlin_stdlib)
    add("implementation", Libs.Material.material)
}

fun DependencyHandlerScope.navigationCommon() = this.apply {
    add("implementation", Libs.Navigation.ui)
    add("implementation", Libs.Navigation.fragment)
}

fun DependencyHandlerScope.navigationAll() = this.apply {
    add("implementation", Libs.Navigation.ui)
    add("implementation", Libs.Navigation.fragment)
}

fun DependencyHandlerScope.coroutines() = this.apply {
    add("implementation", Libs.Coroutines.core)
    add("implementation", Libs.Coroutines.android)
}

fun DependencyHandlerScope.lifecycle() = this.apply {
    add("implementation", Libs.Lifecycle.runtimeKtx)
    add("implementation", Libs.Lifecycle.livedataKtx)
    add("implementation", Libs.Lifecycle.viewModelKtx)
    //add("implementation", Libs.Lifecycle.extensions)
}

fun DependencyHandlerScope.test() = this.apply {
    add("testImplementation", Libs.Test.junit)
    add("androidTestImplementation", Libs.Test.ext_junit)
    add("androidTestImplementation", Libs.Test.espresso_core)
}