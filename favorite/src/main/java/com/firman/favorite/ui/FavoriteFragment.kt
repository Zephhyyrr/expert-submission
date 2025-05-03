package com.firman.favorite.ui

import android.content.Intent
import android.os.Bundle
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
            val intent =
                Intent(activity, Class.forName("com.firman.bookapp.ui.detail.DetailActivity"))
            intent.putExtra("EXTRA_BOOK_ID", selectedBook.id)
            startActivity(intent)
        }

        with(binding.rvFavorite) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = bookAdapter
        }
    }

    private fun observeData() {
        favoriteViewModel.favoriteBooks.observe(viewLifecycleOwner) { books ->
            bookAdapter.submitList(books)
        }
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}
