package com.panassevich.panassevich.feature.loans.tutorial.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.panassevich.panassevich.feature.loans.tutorial.databinding.InstructionItemBinding
import com.panassevich.panassevich.feature.loans.tutorial.presentation.InstructionItem

class ViewPagerAdapter(private val instructions: List<InstructionItem>) :
    RecyclerView.Adapter<InstructionItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionItemViewHolder {
        val binding =
            InstructionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return InstructionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InstructionItemViewHolder, position: Int) {
        val instructionItem = instructions[position]
        with(holder.binding.root.resources) {
            holder.binding.instructionImage.setImageResource(instructionItem.imageId)
            holder.binding.textViewTitle.text = getString(instructionItem.titleId)
            holder.binding.textViewDescription.text = getString(instructionItem.descriptionId)
        }
    }

    override fun getItemCount(): Int = instructions.size
}