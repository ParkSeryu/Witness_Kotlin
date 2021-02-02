package com.parkseryu.witness.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parkseryu.witness.databinding.RecyclerviewAnniversaryItemBinding
import com.parkseryu.witness.dto.AnniversaryEntity


class RecyclerViewAdapter(private val viewModel: HomeViewModel) :
    RecyclerView.Adapter<ViewHolder>() {
    var items = listOf<AnniversaryEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewAnniversaryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder(private val binding: RecyclerviewAnniversaryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: HomeViewModel, item: AnniversaryEntity) {
        binding.viewModel = viewModel
        binding.anniversaryEntity = item
        binding.executePendingBindings()
    }
}