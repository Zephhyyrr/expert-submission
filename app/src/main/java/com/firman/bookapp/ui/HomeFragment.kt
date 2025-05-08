package com.firman.bookapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firman.bookapp.databinding.FragmentHomeBinding
import com.firman.bookapp.ui.detail.DetailActivity
import com.firman.core.data.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var bookAdapter: BookAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupRecyclerView()
        observeBooks()
    }

    private fun setupAdapter() {
        bookAdapter = BookAdapter()
        bookAdapter?.onItemClick = { selectedBook ->
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_BOOK_ID, selectedBook.id)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.rvBooks.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = bookAdapter
        }
    }

    private fun observeBooks() {
        homeViewModel.books.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    result.data?.let {
                        bookAdapter?.setData(it)
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvBooks.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        bookAdapter?.clearData()
        binding.rvBooks.adapter = null
        bookAdapter?.onItemClick = null
        bookAdapter = null
        _binding = null
        super.onDestroyView()
    }
}