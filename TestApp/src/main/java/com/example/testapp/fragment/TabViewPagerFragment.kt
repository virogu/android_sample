package com.example.testapp.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AbsRvTabLayoutMediator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.testapp.databinding.FragmentTabViewPagerBinding
import com.example.testapp.databinding.TabMenuItemBinding
import com.virogu.base.fragment.BaseBindingFragment

class TabViewPagerFragment : BaseBindingFragment<FragmentTabViewPagerBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTabViewPagerBinding {
        return FragmentTabViewPagerBinding.inflate(inflater, container, false)
    }

    override fun FragmentTabViewPagerBinding.onViewCreated() {
        viewPager.adapter = object : FragmentStateAdapter(this@TabViewPagerFragment) {
            override fun getItemCount(): Int = 10

            override fun createFragment(position: Int): Fragment {
                return Fragment()
            }
        }
        rv1.layoutManager = LinearLayoutManager(requireContext())
        rv2.layoutManager = GridLayoutManager(requireContext(), 4)
        rv3.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val tab1 = object : AbsRvTabLayoutMediator<TabMenuItemBinding>(rv1, viewPager) {
            override fun onCreateView(parent: ViewGroup): TabMenuItemBinding {
                return TabMenuItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            @SuppressLint("SetTextI18n")
            override fun TabMenuItemBinding.onBindView(
                position: Int,
                isSelected: Boolean,
                payloads: Set<Any>
            ) {
                tvText.text = "menu${position + 1}"
            }
        }
        val tab2 = object : AbsRvTabLayoutMediator<TabMenuItemBinding>(rv2, viewPager) {
            override fun onCreateView(parent: ViewGroup): TabMenuItemBinding {
                return TabMenuItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            @SuppressLint("SetTextI18n")
            override fun TabMenuItemBinding.onBindView(
                position: Int,
                isSelected: Boolean,
                payloads: Set<Any>
            ) {
                tvText.text = "menu${position + 1}"
            }
        }
        val tab3 = object : AbsRvTabLayoutMediator<TabMenuItemBinding>(rv3, viewPager) {
            override fun onCreateView(parent: ViewGroup): TabMenuItemBinding {
                return TabMenuItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            @SuppressLint("SetTextI18n")
            override fun TabMenuItemBinding.onBindView(
                position: Int,
                isSelected: Boolean,
                payloads: Set<Any>
            ) {
                tvText.text = "menu${position + 1}"
            }
        }

        tab1.attach()
        tab2.attach()
        tab3.attach()
    }
}