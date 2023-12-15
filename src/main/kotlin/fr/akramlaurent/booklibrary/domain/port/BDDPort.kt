package fr.akramlaurent.booklibrary.domain.port

import fr.akramlaurent.booklibrary.domain.model.Book

interface BDDPort {
    fun addBook(book: Book)
    fun listLibrary(): List<Book>
    fun reserveBook(book: Book)
}