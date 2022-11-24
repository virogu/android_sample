package com.virogu.base.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @author Virogu
 * @since 2022-11-17 15:09
 **/

abstract class BaseBindingFragment<VB : ViewBinding> : Fragment() {

    protected var binding: VB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getViewBinding(inflater, container).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.onViewCreated()
    }

    protected open fun VB.onViewCreated() {

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected fun hideKeyboard(
        view: View
    ) {
        activity?.window?.also { window ->
            WindowInsetsControllerCompat(
                window,
                view
            ).hide(WindowInsetsCompat.Type.ime())
        }
    }

}

abstract class BaseDialogBindingFragment<VB : ViewBinding> : DialogFragment() {

    protected var binding: VB? = null

    protected open val isFullScreen = false
    protected open val dimAmount = 0.5f
    protected open val alpha = 1.0f
    protected open val cancelable = false
    protected open val canceledOnTouchOutside = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(cancelable)
        dialog?.setCanceledOnTouchOutside(canceledOnTouchOutside)
        return getViewBinding(inflater, container).also {
            binding = it
        }.root
    }

    override fun onStart() {
        super.onStart()
        //透明背景
        dialog?.window?.also {
            if (isFullScreen) {
                it.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }?.attributes?.apply {
            gravity = Gravity.CENTER
            //窗口透明度
            alpha = this@BaseDialogBindingFragment.alpha
            //窗口背景调暗度
            dimAmount = this@BaseDialogBindingFragment.dimAmount
            flags = flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        }?.also {
            dialog?.window?.attributes = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.onViewCreated()
    }

    protected open fun VB.onViewCreated() {

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected fun hideKeyboard(
        view: View
    ) {
        activity?.window?.also { window ->
            WindowInsetsControllerCompat(
                window,
                view
            ).hide(WindowInsetsCompat.Type.ime())
        }
    }

}