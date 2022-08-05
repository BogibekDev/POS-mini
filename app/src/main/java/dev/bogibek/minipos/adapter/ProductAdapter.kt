package dev.bogibek.minipos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.bogibek.minipos.databinding.ItemProductBinding
import dev.bogibek.minipos.model.Product

class ProductAdapter : ListAdapter<Product, ProductAdapter.VH>(DIFFER), Filterable {

    private var list: List<Product>? = null

    var onItemClick: ((Product) -> Unit)? = null
    var onItemLongClick: ((Product) -> Unit)? = null

    inner class VH(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val product = currentList[adapterPosition]
            binding.apply {
                tvName.text = product.name
                tvAmount.text = product.amount.toString()
                tvPrice.text = product.price.toString()
                tvDescription.text = product.note
                root.setOnClickListener {
                    onItemClick?.invoke(product)
                }
                root.setOnLongClickListener {
                    onItemLongClick?.invoke(product)
                    true
                }
            }
        }
    }

    fun mySubmitList(list: List<Product>) {
        this.list = list
        submitList(list)
    }

    object DIFFER : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind()
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    private var customFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Product>()
            if (p0 == null || p0.isEmpty()) {
                filteredList.addAll(list!!)
            } else {
                for (item in list!!) {
                    if (item.name!!.lowercase().contains(p0.toString().lowercase())
                        || item.barCode!!.lowercase().contains(p0.toString().lowercase())
                    ) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            submitList(p1?.values as MutableList<Product>)
        }

    }
}