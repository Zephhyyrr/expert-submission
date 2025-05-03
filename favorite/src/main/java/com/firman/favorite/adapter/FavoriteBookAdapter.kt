package com.firman.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firman.core.domain.model.Book
import com.firman.favorite.databinding.ItemBookBinding

class FavoriteBookAdapter(private var onItemClick: (Book) -> Unit) :
    ListAdapter<Book, FavoriteBookAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class ViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.apply {
                tvItemTitle.text = book.title

                Glide.with(itemView.context)
                    .load(book.coverUrl)
                    .into(ivItemImage)

                tvAuthorsName.text = book.authors.joinToString(", ")

                if (book.publishYear != null) {
                    tvYear.text = book.publishYear.toString()
                } else {
                    tvYear.text = "Unknown"
                }

                root.setOnClickListener {
                    onItemClick(book)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }
        }
    }
}