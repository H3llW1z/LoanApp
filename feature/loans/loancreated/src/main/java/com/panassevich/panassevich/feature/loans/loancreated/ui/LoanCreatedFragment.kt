package com.panassevich.panassevich.feature.loans.loancreated.ui


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Property
import android.view.View
import com.panassevich.panassevich.component.loans.commonclasses.ui.BaseFragment
import com.panassevich.panassevich.component.loans.resources.R
import com.panassevich.panassevich.feature.loans.loancreated.databinding.FragmentLoanCreatedBinding
import com.panassevich.panassevich.feature.loans.loancreated.presentation.LoanCreatedViewModel
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import com.panassevich.panassevich.shared.loans.core.presentation.transportmodel.LoanTransportModel
import com.panassevich.panassevich.shared.loans.core.presentation.transportmodel.toEntity
import com.panassevich.panassevich.shared.loans.core.presentation.transportmodel.toTransportModel
import java.text.NumberFormat

class LoanCreatedFragment : BaseFragment<FragmentLoanCreatedBinding, LoanCreatedViewModel>(
    R.string.loans_title, LoanCreatedViewModel::class.java, FragmentLoanCreatedBinding::inflate
) {

    private val numberFormat = NumberFormat.getInstance()
    companion object {
        @JvmStatic
        fun newInstance(loan: Loan) = LoanCreatedFragment().apply {
            arguments = Bundle().apply {
                putParcelable(LOAN, loan.toTransportModel())
            }
        }

        private const val LOAN = "extra_loan"

        private const val ANIMATION_START_DELAY = 400L
        private const val ANIMATION_DURATION = 300L
    }

    private var _loan: Loan? = null
    private val loan get() = _loan ?: throw RuntimeException("Loan is null!")
    private fun parseArguments() {
        val args = requireArguments()
        if (args.containsKey(LOAN)) {
            _loan = (args.getParcelable(LOAN) as? LoanTransportModel)?.toEntity()
        } else {
            throw RuntimeException("Loan argument is absent")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillLoanInfoFields(loan)
        setUpButton()
        animateSuccessImageView()
    }

    private fun fillLoanInfoFields(loan: Loan) {
        with(binding) {
            textViewAmount.text =
                getString(R.string.amount_template, numberFormat.format(loan.amount))
            textViewPeriod.text = getString(R.string.period_days_only_template, loan.period)
        }
    }

    private fun setUpButton() {
        binding.buttonToMainScreen.setOnClickListener {
            viewModel.openLoansListScreen()
        }
    }

    private fun animateSuccessImageView() {
        fun scaleAnimator(target: View, property: Property<View, Float>) = ObjectAnimator
            .ofFloat(target, property, 0f, 1.3f, 1f)
            .setDuration(ANIMATION_DURATION).apply { startDelay = ANIMATION_START_DELAY }

        with(binding.imageViewSuccess) {
            val xScaleAnimator = scaleAnimator(this, View.SCALE_X)
            val yScaleAnimator = scaleAnimator(this, View.SCALE_Y)
            AnimatorSet().apply {
                playTogether(xScaleAnimator, yScaleAnimator)
                start()
            }
        }
    }

}