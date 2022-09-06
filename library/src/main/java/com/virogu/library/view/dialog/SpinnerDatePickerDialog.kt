package com.virogu.library.view.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.StringRes
import com.virogu.library.R
import java.util.*


class SpinnerDatePickerDialog(context: Context) : AlertDialog(context) {

    @SuppressLint("DiscouragedPrivateApi")
    class Builder(context: Context) {
        private val dialog: SpinnerDatePickerDialog = SpinnerDatePickerDialog(context)
        private val layout: View
        private val title: TextView
        private val btOk: Button
        private val btCancel: Button
        private val datePicker: DatePicker
        private var cancelable: Boolean = true
        private var onCancelListener: (() -> Unit)? = {}
        private var onPositiveListener: ((date: Calendar) -> Unit)? = {}

        init {
            layout = LayoutInflater.from(context).inflate(
                R.layout.lay_spinner_date_picker,
                null,
                false
            ).apply {
                title = findViewById(R.id.title)
                btOk = findViewById(R.id.btOk)
                btCancel = findViewById(R.id.btCancel)
                datePicker = findViewById(R.id.datePicker)
            }
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setView(layout, 0, 0, 0, 0)
            dialog.setOnShowListener {
                dialog.window?.apply {
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val params = attributes
                    params.gravity = Gravity.CENTER
                    params.width =
                        context.resources.getDimensionPixelOffset(R.dimen.date_picker_min_width)
                    attributes = params
                }
            }
        }

        fun setMinDate(minDate: Long): Builder {
            datePicker.minDate = minDate
            return this
        }

        fun setMaxDate(maxDate: Long): Builder {
            datePicker.maxDate = maxDate
            return this
        }

        fun setDate(date: Long): Builder {
            if (date > 0) {
                val calendar = Calendar.getInstance()
                calendar.time = Date(date)
                datePicker.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }
            return this
        }

        fun setTitle(title: String): Builder {
            this.title.text = title
            return this
        }

        fun setTitle(@StringRes resId: Int): Builder {
            this.title.setText(resId)
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setOnPositiveListener(onPositiveListener: ((date: Calendar) -> Unit)?): Builder {
            this.onPositiveListener = onPositiveListener
            return this
        }

        fun setOnCancelListener(onCancelListener: (() -> Unit)?): Builder {
            this.onCancelListener = onCancelListener
            return this
        }

        fun build(): SpinnerDatePickerDialog {
            dialog.setCancelable(cancelable)
            btOk.setOnClickListener {
                onPositiveListener?.also {
                    val calendar = Calendar.getInstance().apply {
                        set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
                    }
                    it.invoke(calendar)
                }
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