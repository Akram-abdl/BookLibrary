package fr.akramlaurent.booklibrary.domain.usecase

import fr.akramlaurent.booklibrary.domain.exceptions.BookAlreadyReservedException
import fr.akramlaurent.booklibrary.domain.exceptions.BookNotFoundException
import fr.akramlaurent.booklibrary.domain.model.Book
import fr.akramlaurent.booklibrary.domain.port.BDDPort

class BookUseCase(private val bddPort: BDDPort) {
    fun addBook(title: String, author: String) = addBook(Book(title, author))

    fun addBook(book: Book) = bddPort.addBook(book)

    fun listLibrary(): List<Book> = bddPort.listLibrary()

    fun reserveBook(title: String, author: String) = reserveBook(Book(title, author))

    fun reserveBook(book: Book) {
        bddPort.listLibrary().firstOrNull { it.title == book.title && it.author == book.author }?.let {
            if (it.reserved) throw BookAlreadyReservedException()
            bddPort.reserveBook(book)
        } ?: throw BookNotFoundException()
    }
}