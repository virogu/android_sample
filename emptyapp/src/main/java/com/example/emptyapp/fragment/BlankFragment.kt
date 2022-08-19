package com.example.emptyapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.emptyapp.databinding.FragmentBlankBinding
import timber.log.Timber

class BlankFragment : Fragment() {

    private lateinit var binding: FragmentBlankBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("$tag onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Timber.i("$tag onCreateView")
        binding = FragmentBlankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        Timber.i("$tag onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Timber.i("$tag onDestroy")
        super.onDestroy()
    }
}