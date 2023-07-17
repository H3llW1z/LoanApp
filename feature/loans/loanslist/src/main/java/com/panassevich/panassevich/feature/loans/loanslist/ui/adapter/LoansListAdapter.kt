package com.panassevich.panassevich.feature.loans.loanslist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.panassevich.panassevich.component.loans.resources.R
import com.panassevich.panassevich.feature.loans.loanslist.databinding.LoanItemBinding
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanStatus
import com.panassevich.panassevich.util.loans.ui.getDateOnly
import java.text.NumberFormat
import java.util.Locale

class LoansListAdapter : ListAdapter<Loan, LoanViewHolder>(LoanItemDiffCallback()) {

    var onItemClick: ((Loan) -> Unit)? = null

    private val numberFormat = NumberFormat.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        val binding =
            LoanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        val loanItem = currentList[position]
        bindItem(holder, loanItem)
    }

    private fun bindItem(holder: LoanViewHolder, loan: Loan) {

        onItemClick?.let { listener ->
            holder.binding.root.setOnClickListener { listener(loan) }
        }

        val colorResId: Int = when (loan.status) {
            LoanStatus.APPROVED -> R.color.color_approved
            LoanStatus.REJECTED -> R.color.color_rejected
            LoanStatus.REGISTERED -> R.color.color_registered
        }

        val color = ContextCompat.getColor(holder.itemView.context, colorResId)
        holder.binding.imageViewStatusIndicator.setColorFilter(color)

        with(holder.itemView.resources) {
            holder.binding.textViewAmount.text =
                String.format(
                    Locale.getDefault(),
                    getString(R.string.amount_template),
                    numberFormat.format(loan.amount)
                )

            holder.binding.textViewDate.text = String.format(
                Locale.getDefault(),
                getString(R.string.date_template),
                getDateOnly(loan.date)
            )

            holder.binding.textViewPeriod.text = String.format(
                Locale.getDefault(),
                getString(R.string.period_template),
                loan.period
            )

            holder.binding.textViewStatus.text = when (loan.status) {
                LoanStatus.REGISTERED -> getString(
                    R.string.registered
                )

                LoanStatus.APPROVED -> getString(
                    R.string.approved
                )

                LoanStatus.REJECTED -> getString(
                    R.string.rejected
                )
            }
        }

    }
}