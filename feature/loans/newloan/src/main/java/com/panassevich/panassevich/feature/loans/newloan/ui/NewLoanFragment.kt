package com.panassevich.panassevich.feature.loans.newloan.ui

import android.animation.AnimatorSet
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ErrorEvent
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ScreenState
import com.panassevich.panassevich.component.loans.commonclasses.ui.BaseFragment
import com.panassevich.panassevich.component.loans.resources.R
import com.panassevich.panassevich.feature.loans.newloan.databinding.FragmentNewLoanBinding
import com.panassevich.panassevich.feature.loans.newloan.presentation.NewLoanViewModel
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanConditions
import com.panassevich.panassevich.util.loans.ui.shake
import com.panassevich.panassevich.util.loans.ui.shakeAnimator
import com.panassevich.panassevich.util.loans.ui.showSnackbar
import java.text.NumberFormat

class NewLoanFragment : BaseFragment<FragmentNewLoanBinding, NewLoanViewModel>(
    R.string.new_loan_title, NewLoanViewModel::class.java, FragmentNewLoanBinding::inflate
) {
    companion object {
        @JvmStatic
        fun newInstance() = NewLoanFragment()
    }

    private val numberFormat = NumberFormat.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButtons()
        setUpPhoneClickListener()
        observeState()
        observeErrorEvents()
    }

    private fun setUpButtons() {
        with(binding) {
            buttonTryAgain.setOnClickListener {
                viewModel.loadLoanConditions()
            }
            buttonSendRequest.setOnClickListener {
                viewModel.sendLoanCreateRequest(
                    editTextAmount.text.toString(),
                    editTextName.text.toString(),
                    editTextLastName.text.toString(),
                    editTextPhone.text.toString()
                )
            }
        }
    }

    private fun setUpPhoneClickListener() {
        binding.textViewBankPhone.setOnClickListener {
            Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse(getString(R.string.bank_phone_uri))
                startActivity(this)
            }
        }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            with(binding) {
                when (state) {
                    is ScreenState.Initial -> {
                        viewModel.loadLoanConditions()
                    }

                    ScreenState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        buttonSendRequest.isEnabled = false
                    }

                    is ScreenState.Content -> {
                        setErrorViewsVisibility(View.GONE)
                        progressBar.visibility = View.GONE
                        fillLoanConditionFields(state.content)
                        scrollViewContent.visibility = View.VISIBLE
                        buttonSendRequest.isEnabled = true
                    }

                    is ScreenState.Error -> {
                        buttonSendRequest.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        scrollViewContent.visibility = View.GONE
                        setErrorViewsVisibility(View.VISIBLE)
                    }

                    is ScreenState.Success -> {}
                }
            }
        }
    }

    private fun observeErrorEvents() {
        viewModel.errorEvent.observe(viewLifecycleOwner) { event ->
            val message = when (event) {
                is ErrorEvent.NetworkError -> {
                    getString(R.string.network_error_occurred)
                }

                is ErrorEvent.ServerError -> {
                    getString(R.string.server_error_occurred)
                }

                is ErrorEvent.EmptyFields -> {
                    shakeInputFields()
                    getString(R.string.fill_all_fields)
                }

                is ErrorEvent.MaxAmountExceeded -> {
                    binding.editTextAmount.shake()
                    getString(R.string.max_amount_exceeded)
                }

                else -> throw RuntimeException("Unexpected error event: $event")
            }
            view?.showSnackbar(message)
        }
    }

    private fun fillLoanConditionFields(loanConditions: LoanConditions) {
        with(binding) {
            textViewMaxAmount.text =
                getString(R.string.amount_template, numberFormat.format(loanConditions.maxAmount))
            textViewPercent.text = getString(R.string.percent_template, loanConditions.percent)
            textViewPeriod.text = getString(
                R.string.period_days_only_template,
                loanConditions.period
            )
        }

    }

    private fun shakeInputFields() {
        with(binding) {
            AnimatorSet().apply {
                val animator1 = shakeAnimator(editTextAmount)
                val animator2 = shakeAnimator(editTextName)
                val animator3 = shakeAnimator(editTextLastName)
                val animator4 = shakeAnimator(editTextPhone)
                playTogether(animator1, animator2, animator3, animator4)
                start()
            }
        }
    }

    private fun setErrorViewsVisibility(visibility: Int) {
        with(binding) {
            imageViewError.visibility = visibility
            textViewError.visibility = visibility
            buttonTryAgain.visibility = visibility
        }
    }
}