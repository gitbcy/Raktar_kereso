package com.example.raktarkereso.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raktarkereso.data.InventoryItem
import com.example.raktarkereso.databinding.ItemSearchResultBinding

/**
 * Displays search results as: product name + "Raktár - Tároló egység".
 * e.g. "II. raktár - Középső jobb fent"
 */
class SearchResultAdapter : RecyclerView.Adapter<SearchResultAdapter.ResultViewHolder>() {

    private var items: List<InventoryItem> = emptyList()

    fun submitList(newItems: List<InventoryItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ResultViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InventoryItem) {
            binding.textProductName.text = item.productName
            binding.textLocation.text = "${item.warehouseName} - ${item.storageUnitName}"
        }
    }
}
