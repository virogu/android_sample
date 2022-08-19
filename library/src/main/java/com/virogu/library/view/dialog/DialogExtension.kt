package com.virogu.library.view.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.virogu.library.R

/**
 * @param msg the content of dialog
 * @param delaySecond second == 0 不做计时处理; second > 0 计时second秒之后才能关闭; second < 0 计时second秒之后自动关闭
 * @param title the title of a dialog
 * @param drawable icon before title
 * @param btCloseText text content on close button
 * @param cancelable can dialog be closed manually
 * @param onClose function when dialog closed
 *
 * @return AlertDialog
 */
fun Context.makeCommonTipsDialog(
    msg: String,
    delaySecond: Long = 0L,
    title: String = getString(R.string.tips),
    drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_baseline_warning),
    btCloseText: String = getString(R.string.close),
    cancelable: Boolean = false,
    onClose: () -> Unit = {}
): CommonTipsDialog = makeCommonTipsDialog(
    listOf(TextWithColor(msg)),
    delaySecond,
    title,
    drawable,
    btCloseText,
    cancelable,
    onClose
)

/**
 * @param msg the content of dialog
 * @param delaySecond second == 0 不做计时处理; second > 0 计时second秒之后才能关闭; second < 0 计时second秒之后自动关闭
 * @param title the title of a dialog
 * @param drawable icon before title
 * @param btCloseText text content on close button
 * @param cancelable can dialog be closed manually
 * @param onClose function when dialog closed
 *
 * @return AlertDialog
 */
fun Context.makeCommonTipsDialog(
    msg: List<TextWithColor>,
    delaySecond: Long = 0L,
    title: String = getString(R.string.tips),
    drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_baseline_warning),
    btCloseText: String = getString(R.string.close),
    cancelable: Boolean = false,
    onClose: () -> Unit = {}
): CommonTipsDialog = CommonTipsDialog.Builder(this)
    .setTitle(title)
    .setTitleDrawable(drawable)
    .setText(msg)
    .setCancelable(cancelable)
    .setCloseBtText(btCloseText)
    .setDelaySecond(delaySecond)
    .setOnCloseListener {
        onClose()
    }
    .build()

/**
 * @param msg the content of dialog
 * @param delaySecond second == 0 不做计时处理; second > 0 计时second秒之后才能关闭; second < 0 计时second秒之后自动关闭
 * @param title the title of a dialog
 * @param drawable icon before title
 * @param btCloseText text content on close button
 * @param cancelable can dialog be closed manually
 * @param onClose function when dialog closed
 *
 * @return Unit
 */
fun Context.showCommonTipsDialog(
    msg: String,
    delaySecond: Long = 0L,
    title: String = getString(R.string.tips),
    drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_baseline_warning),
    btCloseText: String = getString(R.string.close),
    cancelable: Boolean = false,
    onClose: () -> Unit = {}
): Unit = makeCommonTipsDialog(
    msg,
    delaySecond,
    title,
    drawable,
    btCloseText,
    cancelable,
    onClose
).show()

/**
 * @param msg the content of dialog
 * @param delaySecond second == 0 不做计时处理; second > 0 计时second秒之后才能关闭; second < 0 计时second秒之后自动关闭
 * @param title the title of a dialog
 * @param drawable icon before title
 * @param btCloseText text content on close button
 * @param cancelable can dialog be closed manually
 * @param onClose function when dialog closed
 *
 * @return Unit
 */
fun Context.showCommonTipsDialog(
    msg: List<TextWithColor>,
    delaySecond: Long = 0L,
    title: String = getString(R.string.tips),
    drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_baseline_warning),
    btCloseText: String = getString(R.string.close),
    cancelable: Boolean = false,
    onClose: () -> Unit = {}
): Unit = makeCommonTipsDialog(
    msg,
    delaySecond,
    title,
    drawable,
    btCloseText,
    cancelable,
    onClose
).show()

/**
 * @param text the content of dialog
 * @param title the title of a dialog
 * @param drawable icon before title
 * @param onPositiveListener function when positive button clicked
 *
 * @return AlertDialog
 */
fun Context.makeConfirmDialog(
    text: String,
    title: String = getString(R.string.tips),
    drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_baseline_warning),
    onPositiveListener: () -> Unit = {}
): CommonConfirmDialog = makeConfirmDialog(
    listOf(TextWithColor(text)),
    title,
    drawable,
    onPositiveListener
)

/**
 * @param textList the content of dialog
 * @param title the title of a dialog
 * @param drawable icon before title
 * @param onPositiveListener function when positive button clicked
 *
 * @return AlertDialog
 */
fun Context.makeConfirmDialog(
    textList: List<TextWithColor>,
    title: String = getString(R.string.tips),
    drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_baseline_warning),
    onPositiveListener: () -> Unit = {}
): CommonConfirmDialog = CommonConfirmDialog.Builder(this)
    .setTitle(title)
    .setTitleDrawable(drawable)
    .setCancelable(false)
    .setText(textList).setOnPositiveListener {
        onPositiveListener()
    }.build()

/**
 * @param text the content of dialog
 * @param title the title of a dialog
 * @param drawable icon before title
 * @param onPositiveListener function when positive button clicked
 *
 * @return Unit
 */
fun Context.showConfirmDialog(
    text: String,
    title: String = getString(R.string.tips),
    drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_baseline_warning),
    onPositiveListener: () -> Unit = {}
) = makeConfirmDialog(
    text,
    title,
    drawable,
    onPositiveListener
).show()

/**
 * @param textList the content of dialog
 * @param title the title of a dialog
 * @param drawable icon before title
 * @param onPositiveListener function when positive button clicked
 *
 * @return Unit
 */
fun Context.showConfirmDialog(
    textList: List<TextWithColor>,
    title: String = getString(R.string.tips),
    drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_baseline_warning),
    onPositiveListener: () -> Unit = {}
) = makeConfirmDialog(
    textList,
    title,
    drawable,
    onPositiveListener
).show()