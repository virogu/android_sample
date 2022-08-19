@file:kotlin.jvm.JvmName("ToastKt")

package com.virogu.library.common.extention

/**
 * @Author Virogu
 * @Date 2021-11-04 下午 3:22:17
 */

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

private var toast: Toast? = null

/**
 * @param duration How long to display the message. {Toast.LENGTH_SHORT, Toast.LENGTH_LONG}.
 * @param context The context to use. By default applicationContext.
 * @param singleInstance `true` cancel the old toast before show a new toast. By default `false`.
 */
@JvmOverloads
fun Context.toast(
    msg: String,
    duration: Int = Toast.LENGTH_SHORT,
    singleInstance: Boolean = false
) {
    if (singleInstance) {
        toast?.cancel()
        toast = Toast.makeText(applicationContext, msg, duration)
        toast?.show()
    } else {
        Toast.makeText(applicationContext, msg, duration).show()
    }
}

/**
 * @param duration How long to display the message. {Toast.LENGTH_SHORT, Toast.LENGTH_LONG}.
 * @param context The context to use. By default applicationContext.
 * @param singleInstance `true` cancel the old toast before show a new toast. By default `false`.
 */
@JvmOverloads
fun Context.toast(
    @StringRes msg: Int,
    duration: Int = Toast.LENGTH_SHORT,
    singleInstance: Boolean = false
) = toast(getString(msg), duration, singleInstance)

/**
 * @param duration How long to display the message. {Toast.LENGTH_SHORT, Toast.LENGTH_LONG}.
 * @param context The context to use. By default applicationContext.
 */
@JvmOverloads
fun Context.singleToast(
    msg: String,
    duration: Int = Toast.LENGTH_SHORT
) = this.toast(msg, duration, true)

/**
 * @param duration How long to display the message. {Toast.LENGTH_SHORT, Toast.LENGTH_LONG}.
 * @param context The context to use. By default applicationContext.
 */
@JvmOverloads
fun Context.singleToast(
    @StringRes msg: Int,
    duration: Int = Toast.LENGTH_SHORT
) = this.toast(msg, duration, true)

@JvmOverloads
fun Fragment.singleToast(
    msg: String,
    duration: Int = Toast.LENGTH_SHORT
) = requireContext().toast(msg, duration, true)

@JvmOverloads
fun Fragment.singleToast(
    @StringRes msg: Int,
    duration: Int = Toast.LENGTH_SHORT
) = requireContext().toast(msg, duration, true)

@JvmOverloads
fun Fragment.toast(
    msg: String,
    duration: Int = Toast.LENGTH_SHORT,
    singleInstance: Boolean = false
) = requireContext().toast(msg, duration, singleInstance)

@JvmOverloads
fun Fragment.toast(
    @StringRes msg: Int,
    duration: Int = Toast.LENGTH_SHORT,
    singleInstance: Boolean = false
) = requireContext().toast(msg, duration, singleInstance)