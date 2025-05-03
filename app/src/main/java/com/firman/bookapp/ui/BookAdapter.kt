package com.firman.bookapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firman.bookapp.databinding.ItemBookBinding
import com.firman.core.domain.model.Book
import com.firman.bookapp.R // Pastikan R diimpor dengan benar

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var listData = ArrayList<Book>()
    var onItemClick: ((Book) -> Unit)? = null

    fun setData(newListData: List<Book>?) {
        if (newListData == null) return
        val diffCallback = BookDiffCallback(listData, newListData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listData.clear()
        listData.addAll(newListData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = listData[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int = listData.size

    fun mapToBook(doc: Map<String, Any>): Book {
        val id = doc["key"] as String
        val title = doc["title"] as String
        val authors = (doc["author_name"] as List<String>).takeIf { it.isNotEmpty() } ?: listOf("Unknown Author")
        val coverId = (doc["cover_i"] as? Int)?.let { it.toString() } ?: ""
        val coverUrl = if (coverId.isNotEmpty()) "https://covers.openlibrary.org/b/id/$coverId-L.jpg" else null
        val description = doc["description"] as? String
        val publishYear = (doc["first_publish_year"] as? Int)

        return Book(
            id = id,
            title = title,
            authors = authors,
            coverUrl = coverUrl,
            description = description,
            publishYear = publishYear,
            isFavorite = false
        )
    }


    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            with(binding) {
                tvTitle.text = book.title
                tvAuthors.text = book.authors.joinToString(", ")
                tvYear.text = book.publishYear?.toString() ?: "Unknown"

                Glide.with(itemView.context)
                    .load(book.coverUrl ?: R.drawable.ic_book_placeholder)
                    .placeholder(R.drawable.ic_book_placeholder)
                    .error(R.drawable.ic_book_placeholder)
                    .into(ivBook)
            }
        }

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(listData[position])
                }
            }
        }
    }
}

class BookDiffCallback(
    private val oldList: List<Book>,
    private val newList: List<Book>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
