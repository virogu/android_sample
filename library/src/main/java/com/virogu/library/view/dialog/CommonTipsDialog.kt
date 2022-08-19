package com.virogu.library.view.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.virogu.library.R
import kotlinx.coroutines.*

private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

class CommonTipsDialog(context: Context) : AlertDialog(context) {

    @SuppressLint("DiscouragedPrivateApi")
    class Builder(private val context: Context) {

        private val dialog: CommonTipsDialog = CommonTipsDialog(context)
        private val layout: View
        private val title: AppCompatTextView
        private val btOk: Button
        private val tvText: TextView
        private var cancelable: Boolean = true
        private var onCloseListener: (() -> Unit)? = {}
        private var textList: List<TextWithColor> = emptyList()
        private var btOkText: String = context.getString(android.R.string.ok)

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
            drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            this.title.compoundDrawablePadding = padding
            this.title.setCompoundDrawables(drawable, null, null, null)
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

        fun setOnCloseListener(onCloseListener: (() -> Unit)?): Builder {
            this.onCloseListener = onCloseListener
            return this
        }

        @SuppressLint("SetTextI18n")
        fun build(): CommonTipsDialog {
            dialog.setCancelable(cancelable)
            tvText.text = SpannableStringBuilder().apply {
                textList.forEach {
                    append(
                        it.text,
                        ForegroundColorSpan(it.textColor),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )
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
                when {
                    delaySecond > 0 -> {
                        dialog.setCancelable(false)
                        btOk.isClickable = false
                        job = coroutineScope.launch(Dispatchers.Main) {
                            var times = delaySecond
                            while (times > 0 && isActive) {
                                btOk.text = "$btOkText（$times）"
                                delay(1000)
                                times--
                            }
                            btOk.text = btOkText
                            btOk.isClickable = true
                        }
                    }
                    delaySecond < 0 -> {
                        btOk.isClickable = true
                        job = coroutineScope.launch(Dispatchers.Main) {
                            var times = delaySecond
                            while (times < 0 && isActive) {
                                btOk.text = "$btOkText（${-times}）"
                                delay(1000)
                                times++
                            }
                            btOk.text = btOkText
                            btOk.performClick()
                        }
                    }
                    else -> {
                        btOk.isClickable = true
                        cancelJob()
                    }
                }
            }
            return dialog
        }

        private fun cancelJob() {
            job?.cancel()
            job = null
        }

    }
}