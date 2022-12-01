package com.example.testapp.datepicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.Pair
import com.example.testapp.R
import com.example.testapp.databinding.FragmentDatePickerBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.virogu.base.fragment.BaseBindingFragment

class DatePickerFragment : BaseBindingFragment<FragmentDatePickerBinding>() {

    companion object {
        fun newInstance() = DatePickerFragment()
    }

    private var dateRangePiker: MaterialDatePicker<Pair<Long, Long>>? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDatePickerBinding {
        return FragmentDatePickerBinding.inflate(inflater, container, false)
    }

    override fun FragmentDatePickerBinding.onViewCreated() {
        bt1.setOnClickListener {
            dateRangePiker?.dismiss()
            dateRangePiker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("请选择日期范围")
                .setTheme(R.style.DatePicker_Orange)
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .setCalendarConstraints(CalendarConstraints.Builder().apply {

                }.build())
                .build()
            dateRangePiker?.show(childFragmentManager, "dateRangePicker")
        }
    }

}