package com.firman.favorite.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firman.core.domain.model.Book
import com.firman.favorite.databinding.FragmentFavoriteBinding
import com.firman.favorite.adapter.FavoriteBookAdapter
import com.firman.favorite.di.FavoriteModuleProvider
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    init {
        FavoriteModuleProvider.loadFavoriteModule()
    }

    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var bookAdapter: FavoriteBookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        observeData()
    }

    private fun setupAdapter() {
        bookAdapter = FavoriteBookAdapter { selectedBook ->
            navigateToDetail(selectedBook)
        }

        with(binding.rvFavorite) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = bookAdapter
        }
    }

    private fun navigateToDetail(selectedBook: Book) {
        try {
            // Make sure we have a valid context
            val context = requireContext()

            // Create the intent with the fully qualified class name
            val detailIntent = Intent().apply {
                setClassName(
                    "com.firman.bookapp",
                    "com.firman.bookapp.ui.detail.DetailActivity"
                )
                putExtra("EXTRA_BOOK_ID", selectedBook.id)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            // Start the activity
            context.startActivity(detailIntent)

        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error navigating to detail: ${e.message}", e)
            // You could show a toast or snackbar here to inform the user
        }
    }

    private fun observeData() {
        favoriteViewModel.favoriteBooks.observe(viewLifecycleOwner) { books ->
            bookAdapter.submitList(books)

            // Update empty state visibility if needed
            if (books.isNullOrEmpty()) {
                // Show empty state
                binding.rvFavorite.visibility = View.GONE
                // If you have an empty state view, show it here
            } else {
                binding.rvFavorite.visibility = View.VISIBLE
                // If you have an empty state view, hide it here
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}