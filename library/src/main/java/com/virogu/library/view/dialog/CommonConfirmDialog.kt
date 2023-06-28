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
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.google.android.material.textview.MaterialTextView
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

class CommonConfirmDialog private constructor(context: Context) : AlertDialog(context) {

    @SuppressLint("DiscouragedPrivateApi")
    class Builder(private val context: Context) {

        private val dialog: CommonConfirmDialog = CommonConfirmDialog(context)
        private val layout: View
        private val title: MaterialTextView
        private val btOk: Button
        private val btCancel: Button
        private val tvText: TextView
        private var cancelable: Boolean = true
        private var onShowListener: ((DialogInterface, CommonConfirmDialog) -> Unit)? = null
        private var onDismissListener: ((DialogInterface, CommonConfirmDialog) -> Unit)? = null
        private var onCancelListener: (() -> Unit)? = null
        private var onPositiveListener: (() -> Unit)? = null
        private var textList: List<TextWithColor> = emptyList()
        private var btOkText: String = context.getString(android.R.string.ok)
        private var btCancelText: String = context.getString(android.R.string.cancel)
        private var textGravity = Gravity.CENTER
        private var titleDrawable: Drawable? = null
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
            this.titleDrawable = drawable
            this.title.compoundDrawablePadding = padding
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

        fun setOnShowListener(listener: ((DialogInterface, CommonConfirmDialog) -> Unit)?): Builder {
            this.onShowListener = listener
            return this
        }

        fun setOnDismissListener(listener: ((DialogInterface, CommonConfirmDialog) -> Unit)?): Builder {
            this.onDismissListener = listener
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
                onShowListener?.invoke(it, dialog)
            }
            dialog.setOnDismissListener {
                onDismissListener?.invoke(it, dialog)
            }
            tvText.gravity = textGravity
            tvText.text = buildSpannedString {
                textList.forEach {
                    color(it.textColor) {
                        append(it.text)
                    }
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