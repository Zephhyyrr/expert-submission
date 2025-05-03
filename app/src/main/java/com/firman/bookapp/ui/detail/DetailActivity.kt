package com.firman.bookapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.firman.bookapp.databinding.ActivityDetailBinding
import com.firman.core.data.Resource
import com.firman.core.domain.model.Book
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.firman.bookapp.R

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()

    companion object {
        const val EXTRA_BOOK_ID = "extra_book_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val bookId = intent.getStringExtra(EXTRA_BOOK_ID)
        if (bookId != null) {
            showLoading(true)
            detailViewModel.getBookDetail(bookId).observe(this) { result ->
                when (result) {
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        showLoading(false)
                        result.data?.let { showBookDetail(it) }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        binding.content.visibility = View.GONE
                    }
                }
            }
        } else {
            showLoading(false)
            binding.content.visibility = View.GONE
        }
    }

    private fun showBookDetail(book: Book) {
        binding.content.visibility = View.VISIBLE

        with(binding) {
            tvTitle.text = book.title ?: "No Title Available"
            tvAuthors.text = book.authors.joinToString(", ") ?: "No Author Available"
            tvPublishYear.text = book.publishYear?.toString() ?: "No Publish Year Available"
            tvDescription.text = book.description ?: "No Description Available"

            Log.d("DetailActivity", "Authors: ${book.authors}")
            Log.d("DetailActivity", "Publish Year: ${book.publishYear}")
            Log.d("DetailActivity", "Cover URL: ${book.coverUrl}")

            Glide.with(this@DetailActivity)
                .load(book.coverUrl)
                .placeholder(R.drawable.ic_book_placeholder)
                .into(ivCover)

            var isFavorite = book.isFavorite
            setFabState(isFavorite)

            fab.setOnClickListener {
                isFavorite = !isFavorite
                detailViewModel.setFavoriteBook(book, isFavorite)
                setFabState(isFavorite)
            }
        }
    }

    private fun setFabState(isFavorite: Boolean) {
        val iconRes = if (isFavorite) {
            R.drawable.ic_favorite_filled
        } else {
            R.drawable.ic_favorite_border
        }
        binding.fab.setImageResource(iconRes)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}