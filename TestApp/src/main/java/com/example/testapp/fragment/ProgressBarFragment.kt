package com.example.testapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.testapp.R
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProgressBarFragment : Fragment() {

    private lateinit var progress: CircularProgressIndicator
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progress_bar, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress = view.findViewById(R.id.circularProgressIndicator)
        textView = view.findViewById(R.id.textView)
        progress.showAnimationBehavior = BaseProgressIndicator.SHOW_OUTWARD
        val process = 48
        textView.text = "$process%"
        process.also {
            if (it > 0) {
                lifecycleScope.launch {
                    for (i in 0..process) {
                        delay(500L / it)
                        progress.progress = it * i / it
                    }
                }
            } else {
                progress.progress = 0
            }
        }
    }

}