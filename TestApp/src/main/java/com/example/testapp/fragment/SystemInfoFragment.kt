package com.example.testapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.testapp.R
import com.example.testapp.util.CpuRateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SystemInfoFragment : PreferenceFragmentCompat() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.background_color)
        )
        return view
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        findPreference<Preference>("refresh")?.setOnPreferenceClickListener {
            init()
            return@setOnPreferenceClickListener false
        }
        init()
    }

    private fun init() {
        lifecycleScope.launch(Dispatchers.Main) {
            "cpu_rate".preference?.summary = CpuRateUtil.getRate().toString()
        }
    }

    private val CharSequence.preference
        get() = findPreference<Preference>(this)

}