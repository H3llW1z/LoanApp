package com.panassevich.panassevich.feature.loans.loanslist.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan

class LoanItemDiffCallback : DiffUtil.ItemCallback<Loan>() {

    override fun areItemsTheSame(oldItem: Loan, newItem: Loan): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Loan, newItem: Loan): Boolean =
        oldItem == newItem
}