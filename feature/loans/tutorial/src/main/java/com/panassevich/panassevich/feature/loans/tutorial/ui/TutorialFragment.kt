package com.panassevich.panassevich.feature.loans.tutorial.ui

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.panassevich.panassevich.component.loans.commonclasses.ui.BaseFragment
import com.panassevich.panassevich.component.loans.resources.R
import com.panassevich.panassevich.feature.loans.tutorial.databinding.FragmentTutorialBinding
import com.panassevich.panassevich.feature.loans.tutorial.presentation.TutorialViewModel

class TutorialFragment : BaseFragment<FragmentTutorialBinding, TutorialViewModel>(
    R.string.loans_title, TutorialViewModel::class.java, FragmentTutorialBinding::inflate
) {

    companion object {
        @JvmStatic
        fun newInstance() = TutorialFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupCircleIndicators()
        setupButton()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(viewModel.getInstructions())
    }

    private fun setupCircleIndicators() {
        TabLayoutMediator(binding.tabLayoutIndicator, binding.viewPager)
        { _, _ -> }.attach()
    }

    private fun setupButton() {
        binding.buttonNext.setOnClickListener {
            with(binding) {
                val currentItemIndex = viewPager.currentItem
                viewPager.adapter?.let { adapter ->
                    if (currentItemIndex != adapter.itemCount - 1) {
                        viewPager.currentItem = currentItemIndex + 1
                    } else {
                        viewModel.openLoansList()
                    }
                }

            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.viewPager.adapter?.let { adapter ->
                    binding.buttonNext.text = if (position == adapter.itemCount - 1) {
                        getString(R.string.to_main_screen)
                    } else {
                        getString(R.string.next)
                    }
                }
            }
        })
    }
}