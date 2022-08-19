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
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.virogu.library.R

data class TextWithColor(
    val text: String,
    @ColorInt val textColor: Int = NORMAL
) {
    companion object {
        val NORMAL = Color.parseColor("#212121")
        val RED = Color.parseColor("#FF0000")
    }
}

class CommonConfirmDialog(context: Context) : AlertDialog(context) {

    @SuppressLint("DiscouragedPrivateApi")
    class Builder(private val context: Context) {

        private val dialog: CommonConfirmDialog = CommonConfirmDialog(context)
        private val layout: View
        private val title: AppCompatTextView
        private val btOk: Button
        private val btCancel: Button
        private val tvText: TextView
        private var cancelable: Boolean = true
        private var onCancelListener: (() -> Unit)? = {}
        private var onPositiveListener: (() -> Unit)? = {}
        private var textList: List<TextWithColor> = emptyList()
        private var btOkText: String = context.getString(android.R.string.ok)
        private var btCancelText: String = context.getString(android.R.string.cancel)
        //private var warnTextPosition: List<Int> = emptyList()

        init {
            layout = LayoutInflater.from(context).inflate(
                R.layout.lay_common_confirm_dialog,
                null,
                false
            ).apply {
                title = findViewById(R.id.title)
                btOk = findViewById(R.id.btOk)
                btCancel = findViewById(R.id.btCancel)
                tvText = findViewById(R.id.tvText)
            }
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setView(layout, 0, 0, 0, 0)
            dialog.setOnShowListener {
                dialog.window?.apply {
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val params = attributes
                    params.gravity = Gravity.CENTER
                    params.width =
                        context.resources.getDimensionPixelOffset(R.dimen.common_dialog_min_width)
                    attributes = params
                }
            }
        }

        fun setTitle(title: String): Builder {
            this.title.text = title
            return this
        }

        fun setTitle(@StringRes resId: Int): Builder {
            this.title.setText(resId)
            return this
        }

        fun setPositionBtText(@StringRes resId: Int): Builder {
            return setPositionBtText(context.getString(resId))
        }

        fun setNegationBtText(@StringRes resId: Int): Builder {
            return setNegationBtText(context.getString(resId))
        }

        fun setTitleDrawable(drawable: Drawable?, padding: Int = 4): Builder {
            drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            this.title.compoundDrawablePadding = padding
            this.title.setCompoundDrawables(drawable, null, null, null)
            return this
        }

        fun setPositionBtText(string: String): Builder {
            this.btOkText = string
            return this
        }

        fun setNegationBtText(string: String): Builder {
            this.btCancelText = string
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

        fun setOnPositiveListener(onPositiveListener: (() -> Unit)?): Builder {
            this.onPositiveListener = onPositiveListener
            return this
        }

        fun setOnCancelListener(onCancelListener: (() -> Unit)?): Builder {
            this.onCancelListener = onCancelListener
            return this
        }

        fun build(): CommonConfirmDialog {
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
            btCancel.text = btCancelText
            btOk.setOnClickListener {
                onPositiveListener?.invoke()
                dialog.dismiss()
            }
            btCancel.setOnClickListener {
                onCancelListener?.invoke()
                dialog.dismiss()
            }
            return dialog
        }

    }
}