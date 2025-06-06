package com.firman.favorite.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firman.bookapp.ui.detail.DetailActivity
import com.firman.core.domain.model.Book
import com.firman.favorite.databinding.FragmentFavoriteBinding
import com.firman.favorite.adapter.FavoriteBookAdapter
import com.firman.favorite.di.FavoriteModuleProvider
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        FavoriteModuleProvider.loadFavoriteModule()

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
            val context = requireContext()

            val detailIntent = Intent().apply {
                setClassName(
                    "com.firman.bookapp",
                    "com.firman.bookapp.ui.detail.DetailActivity"
                )
                putExtra(DetailActivity.EXTRA_BOOK_ID, selectedBook.id)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }


            context.startActivity(detailIntent)

        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error navigating to detail: ${e.message}", e)
        }
    }

    private fun observeData() {
        favoriteViewModel.favoriteBooks.observe(viewLifecycleOwner) { books ->
            bookAdapter.submitList(books)

            if (books.isNullOrEmpty()) {
                binding.rvFavorite.visibility = View.GONE
            } else {
                binding.rvFavorite.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}