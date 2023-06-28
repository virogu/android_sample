package com.virogu.library.view.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.google.android.material.textview.MaterialTextView
import com.virogu.library.R
import kotlinx.coroutines.*

private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

class CommonTipsDialog private constructor(context: Context) : AlertDialog(context) {

    @SuppressLint("DiscouragedPrivateApi")
    class Builder(private val context: Context) {

        private val dialog: CommonTipsDialog = CommonTipsDialog(context)
        private val layout: View
        private val title: MaterialTextView
        private val btOk: Button
        private val tvText: TextView
        private var cancelable: Boolean = true
        private var onShowListener: ((DialogInterface, CommonTipsDialog) -> Unit)? = null
        private var onDismissListener: ((DialogInterface, CommonTipsDialog) -> Unit)? = null
        private var onCloseListener: (() -> Unit)? = null
        private var textList: List<TextWithColor> = emptyList()
        private var btOkText: String = context.getString(android.R.string.ok)
        private var textGravity = Gravity.CENTER
        private var titleDrawable: Drawable? = null

        private var delaySecond = 0L

        private var job: Job? = null
        //private var warnTextPosition: List<Int> = emptyList()

        init {
            layout = LayoutInflater.from(context).inflate(
                R.layout.lay_common_tips_dialog,
                null,
                false
            ).apply {
                title = findViewById(R.id.title)
                btOk = findViewById(R.id.btOk)
                tvText = findViewById(R.id.tvText)
            }
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setView(layout, 0, 0, 0, 0)
        }

        fun setTitle(title: String): Builder {
            this.title.text = title
            return this
        }

        fun setTitle(@StringRes resId: Int): Builder {
            this.title.setText(resId)
            return this
        }

        fun setTitleDrawable(drawable: Drawable?, padding: Int = 4): Builder {
            this.titleDrawable = drawable
            this.title.compoundDrawablePadding = padding
            return this
        }

        fun setCloseBtText(@StringRes resId: Int): Builder {
            return setCloseBtText(context.getString(resId))
        }

        fun setCloseBtText(string: String): Builder {
            this.btOkText = string
            return this
        }

        fun setText(text: String): Builder {
            this.textList = listOf(TextWithColor(text))
            //this.warnTextPosition = emptyList()
            return this
        }

        fun setText(@StringRes resId: Int): Builder {
            return setText(context.getString(resId))
        }

        fun setText(textList: List<TextWithColor>): Builder {
            this.textList = textList
            return this
        }

        fun setTextGravity(gravity: Int): Builder {
            this.textGravity = gravity
            return this
        }

        fun setTextSize(size: Float): Builder {
            this.tvText.textSize = size
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        // second == 0 不做计时处理
        // second > 0  计时second秒之后才能关闭
        // second < 0  计时second秒之后自动关闭
        fun setDelaySecond(second: Long): Builder {
            this.delaySecond = second
            return this
        }

        fun setOnShowListener(listener: ((DialogInterface, CommonTipsDialog) -> Unit)?): Builder {
            this.onShowListener = listener
            return this
        }

        fun setOnDismissListener(listener: ((DialogInterface, CommonTipsDialog) -> Unit)?): Builder {
            this.onDismissListener = listener
            return this
        }

        fun setOnCloseListener(onCloseListener: (() -> Unit)?): Builder {
            this.onCloseListener = onCloseListener
            return this
        }

        @SuppressLint("SetTextI18n")
        fun build(): CommonTipsDialog {
            dialog.setCancelable(cancelable)
            tvText.gravity = textGravity
            tvText.text = buildSpannedString {
                textList.forEach {
                    color(it.textColor) {
                        append(it.text)
                    }
                }
            }
            btOk.text = btOkText
            btOk.setOnClickListener {
                runCatching {
                    if (dialog.isShowing) {
                        onCloseListener?.invoke()
                        dialog.dismiss()
                    }
                }
            }
            cancelJob()
            dialog.setOnDismissListener {
                cancelJob()
                onDismissListener?.invoke(it, dialog)
            }
            dialog.setOnShowListener {
                dialog.window?.apply {
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val params = attributes
                    params.gravity = Gravity.CENTER
                    params.width =
                        context.resources.getDimensionPixelOffset(R.dimen.common_dialog_min_width)
                    attributes = params
                }
                titleDrawable?.also { drawable ->
                    val fontMetrics = title.paint.fontMetrics
                    val iconSize = (fontMetrics.descent - fontMetrics.ascent).toInt()
                    drawable.setBounds(0, 0, iconSize, iconSize)
                    title.setCompoundDrawables(drawable, null, null, null)
                }
                when {
                    delaySecond > 0 -> {
                        dialog.setCancelable(false)
                        btOk.isEnabled = false
                        job = coroutineScope.launch(Dispatchers.Main) {
                            var times = delaySecond
                            while (times > 0 && isActive) {
                                btOk.text = "$btOkText ($times)"
                                delay(1000)
                                times--
                            }
                            btOk.text = btOkText
                            btOk.isEnabled = true
                        }
                    }

                    delaySecond < 0 -> {
                        btOk.isEnabled = true
                        job = coroutineScope.launch(Dispatchers.Main) {
                            var times = delaySecond
                            while (times < 0 && isActive) {
                                btOk.text = "$btOkText (${-times})"
                                delay(1000)
                                times++
                            }
                            btOk.text = btOkText
                            btOk.performClick()
                        }
                    }

                    else -> {
                        btOk.isEnabled = true
                        cancelJob()
                    }
                }
                onShowListener?.invoke(it, dialog)
            }
            return dialog
        }

        private fun cancelJob() {
            job?.cancel()
            job = null
        }

    }
}