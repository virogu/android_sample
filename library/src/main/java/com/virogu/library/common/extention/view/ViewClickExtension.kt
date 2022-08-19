@file:Suppress("UNCHECKED_CAST")

package com.virogu.library.common.extention.view

import android.os.SystemClock
import android.view.View
import com.virogu.library.R

/**
 * @Author Virogu
 * @Date 2022/4/3 上午 10:14
 */

/**
 * View 点击事件的扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    if (clickEnable()) {
        block(it as T)
    }
}

/**
 * 设置 View 多次点击间隔时间
 * @param intervalTime Long 间隔时间，单位ms，默认500ms
 * @return T
 */
fun <T : View> T.withTrigger(intervalTime: Long = 500): T {
    triggerDelay = intervalTime
    return this
}

/**
 * 带多次点击过滤的点击事件
 * @param intervalTime Long 间隔时间，单位ms，默认500ms
 * @param block: (T) -> Unit 函数
 * @return T
 */
fun <T : View> T.clickWithTrigger(intervalTime: Long = 500, block: (T) -> Unit) =
    withTrigger(intervalTime).click(block)

private var <T : View> T.triggerLastTime: Long
    get() = getTag(R.string.view_ext_tag_trigger_last_time) as Long? ?: 0
    set(value) {
        setTag(R.string.view_ext_tag_trigger_last_time, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = getTag(R.string.view_ext_tag_trigger_delay_time) as Long? ?: 0
    set(value) {
        setTag(R.string.view_ext_tag_trigger_delay_time, value)
    }

private fun <T : View> T.clickEnable(): Boolean = with(SystemClock.elapsedRealtime()) {
    if (this - triggerLastTime >= triggerDelay) {
        triggerLastTime = this
        true
    } else {
        false
    }
}