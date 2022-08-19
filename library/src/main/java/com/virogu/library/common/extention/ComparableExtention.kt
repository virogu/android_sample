@file:kotlin.jvm.JvmName("ComparableExtKt")

package com.virogu.library.common.extention

/**
 * @Author Virogu
 * @Date 2021-11-10 上午 10:31:02
 */

/**
 * 修正传入的值，将其限定在 min 和 max 之间
 * @param min   最小值
 * @param max   最大值
 * @return      修正后的结果
 * 例：
 *  1.fixArea(4,6) return 4
 *  8.fixArea(4,6) return 6
 *  5.fixArea(4,6) return 5
 */
fun <T> Comparable<T>.fixArea(min: T, max: T): T {
    return when {
        this < min -> {
            min
        }
        this > max -> {
            max
        }
        else -> {
            this as T
        }
    }
}

fun <T> Comparable<T>.fixArea(area: Pair<T, T>): T = this.fixArea(area.first, area.second)