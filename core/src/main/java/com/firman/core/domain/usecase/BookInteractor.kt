package com.firman.core.domain.usecase

import com.firman.core.domain.model.Book
import com.firman.core.domain.repository.BookRepositoryImpl


class BookInteractor(private val bookRepository: BookRepositoryImpl) : BookUseCase {
    override fun searchBooks(query: String) = bookRepository.searchBooks(query)
    override fun getBookDetail(bookId: String) = bookRepository.getBookDetail(bookId)
    override fun getFavoriteBooks() = bookRepository.getFavoriteBooks()
    override suspend fun setFavoriteBook(book: Book, state: Boolean) =
        bookRepository.setFavoriteBook(book, state)
    override fun isFavorite(bookId: String) = bookRepository.isFavorite(bookId)
}