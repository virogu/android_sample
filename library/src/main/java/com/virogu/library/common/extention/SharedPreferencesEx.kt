@file:kotlin.jvm.JvmName("SharedPreferencesExtKt")

package com.virogu.library.common.extention

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

/**
 * @Author Virogu
 * @Date 2021-12-16 下午 2:57:05
 */

private val Context.sp get() = PreferenceManager.getDefaultSharedPreferences(applicationContext)

fun Context.put(key: String, value: Any) {
    sp.edit().apply {
        when (value) {
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            else -> throw IllegalArgumentException("Type Error, $value cannot be saved!")
        }
    }.apply()
}

@Suppress("UNCHECKED_CAST")
fun <T> Context.get(key: String, defaultValue: T): T = with(sp) {
    when (defaultValue) {
        is Long -> getLong(key, defaultValue) as T
        is Int -> getInt(key, defaultValue) as T
        is Boolean -> getBoolean(key, defaultValue) as T
        is String -> getString(key, defaultValue) as T
        is Float -> getFloat(key, defaultValue) as T
        else -> throw IllegalStateException("not support type!")
    }
}

fun Context.put(
    @StringRes key: Int,
    value: Any
) = put(getString(key), value)

fun <T> Context.get(
    @StringRes key: Int,
    defaultValue: T
): T = get(getString(key), defaultValue)

fun Fragment.put(
    key: String,
    value: Any
) = requireContext().put(key, value)

fun <T> Fragment.get(
    key: String,
    defaultValue: T
): T = requireContext().get(key, defaultValue)

fun Fragment.put(
    @StringRes key: Int,
    value: Any
) = put(getString(key), value)

fun <T> Fragment.get(
    @StringRes key: Int,
    defaultValue: T
): T = get(getString(key), defaultValue)

