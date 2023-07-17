package com.panassevich.panassevich.feature.loans.loandetails.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.panassevich.panassevich.component.loans.commonclasses.ui.BaseFragment
import com.panassevich.panassevich.component.loans.resources.R
import com.panassevich.panassevich.feature.loans.loandetails.databinding.FragmentLoanDetailsBinding
import com.panassevich.panassevich.feature.loans.loandetails.presentation.LoanDetailsViewModel
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanStatus
import com.panassevich.panassevich.shared.loans.core.presentation.transportmodel.LoanTransportModel
import com.panassevich.panassevich.shared.loans.core.presentation.transportmodel.toEntity
import com.panassevich.panassevich.shared.loans.core.presentation.transportmodel.toTransportModel
import com.panassevich.panassevich.util.loans.ui.getDateOnly
import java.text.NumberFormat

class LoanDetailsFragment : BaseFragment<FragmentLoanDetailsBinding, LoanDetailsViewModel>(
    R.string.loan_details_title, LoanDetailsViewModel::class.java, FragmentLoanDetailsBinding::inflate
) {

    companion object {
        @JvmStatic
        fun newInstance(loan: Loan) = LoanDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(LOAN, loan.toTransportModel())
            }
        }

        private const val LOAN = "extra_loan"
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

        setUpPhoneClickListener()
        setUpBackButton()
        fillInfoFields(loan)
    }

    private fun fillInfoFields(loanInfo: Loan) {
        val numberFormat = NumberFormat.getInstance()

        with(binding) {
            textViewAmount.text =
                getString(R.string.amount_template, numberFormat.format(loanInfo.amount))
            textViewId.text = loanInfo.id.toString()
            textViewDate.text = getDateOnly(loanInfo.date)
            textViewName.text =
                getString(R.string.name_lastname_template, loanInfo.firstName, loanInfo.lastName)
            textViewPercent.text = getString(R.string.percent_template, loanInfo.percent)
            textViewPeriod.text = getString(R.string.period_days_only_template, loanInfo.period)
            textViewPhone.text = loanInfo.phoneNumber
            textViewStatus.text = when (loanInfo.status) {
                LoanStatus.REGISTERED -> getString(R.string.registered)
                LoanStatus.APPROVED -> getString(R.string.approved)
                LoanStatus.REJECTED -> getString(R.string.rejected)
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

    private fun setUpBackButton() {
        binding.buttonBack.setOnClickListener {
            viewModel.openLoansListScreen()
        }
    }
}