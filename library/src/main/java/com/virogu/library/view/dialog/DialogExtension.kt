package com.virogu.library.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.view.Gravity
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
    textGravity: Int = Gravity.CENTER,
    onClose: () -> Unit = {}
): CommonTipsDialog = makeCommonTipsDialog(
    msg = listOf(TextWithColor(msg)),
    delaySecond = delaySecond,
    title = title,
    drawable = drawable,
    btCloseText = btCloseText,
    cancelable = cancelable,
    textGravity = textGravity,
    onClose = onClose
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
    textGravity: Int = Gravity.CENTER,
    onClose: () -> Unit = {}
): CommonTipsDialog = CommonTipsDialog.Builder(this)
    .setTitle(title)
    .setTitleDrawable(drawable)
    .setText(msg)
    .setTextGravity(textGravity)
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
    textGravity: Int = Gravity.CENTER,
    onClose: () -> Unit = {}
): Unit = makeCommonTipsDialog(
    msg = msg,
    delaySecond = delaySecond,
    title = title,
    drawable = drawable,
    btCloseText = btCloseText,
    cancelable = cancelable,
    textGravity = textGravity,
    onClose = onClose
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
    textGravity: Int = Gravity.CENTER,
    onClose: () -> Unit = {}
): Unit = makeCommonTipsDialog(
    msg = msg,
    delaySecond = delaySecond,
    title = title,
    drawable = drawable,
    btCloseText = btCloseText,
    cancelable = cancelable,
    textGravity = textGravity,
    onClose = onClose
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
    textGravity: Int = Gravity.CENTER,
    onCancelListener: () -> Unit = {},
    onDismissListener: ((DialogInterface, CommonConfirmDialog) -> Unit)? = null,
    onPositiveListener: () -> Unit = {}
): CommonConfirmDialog = makeConfirmDialog(
    textList = listOf(TextWithColor(text)),
    title = title,
    drawable = drawable,
    textGravity = textGravity,
    onCancelListener = onCancelListener,
    onDismissListener = onDismissListener,
    onPositiveListener = onPositiveListener
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
    textGravity: Int = Gravity.CENTER,
    onCancelListener: () -> Unit = {},
    onDismissListener: ((DialogInterface, CommonConfirmDialog) -> Unit)? = null,
    onPositiveListener: () -> Unit = {},
): CommonConfirmDialog = CommonConfirmDialog.Builder(this)
    .setTitle(title)
    .setTitleDrawable(drawable)
    .setCancelable(false)
    .setTextGravity(textGravity)
    .setText(textList)
    .setOnCancelListener(onCancelListener)
    .setOnDismissListener(onDismissListener)
    .setOnPositiveListener(onPositiveListener)
    .build()

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
    textGravity: Int = Gravity.CENTER,
    onCancelListener: () -> Unit = {},
    onDismissListener: ((DialogInterface, CommonConfirmDialog) -> Unit)? = null,
    onPositiveListener: () -> Unit = {}
) = makeConfirmDialog(
    text = text,
    title = title,
    drawable = drawable,
    textGravity = textGravity,
    onCancelListener = onCancelListener,
    onDismissListener = onDismissListener,
    onPositiveListener = onPositiveListener
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
    textGravity: Int = Gravity.CENTER,
    onCancelListener: () -> Unit = {},
    onDismissListener: ((DialogInterface, CommonConfirmDialog) -> Unit)? = null,
    onPositiveListener: () -> Unit = {}
) = makeConfirmDialog(
    textList = textList,
    title = title,
    drawable = drawable,
    textGravity = textGravity,
    onDismissListener = onDismissListener,
    onCancelListener = onCancelListener,
    onPositiveListener = onPositiveListener
).show()