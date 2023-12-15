package fr.akramlaurent.booklibrary.infrastructure.driver.controller

import fr.akramlaurent.booklibrary.domain.model.Book
import fr.akramlaurent.booklibrary.domain.usecase.BookUseCase
import fr.akramlaurent.booklibrary.infrastructure.driver.dto.BookDTO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(private val bookUseCase: BookUseCase) {

    @GetMapping
    fun getLibrary(): List<Book> = bookUseCase.listLibrary()

    @PostMapping
    fun createBook(@RequestBody book: BookDTO) = bookUseCase.addBook(book.title, book.author)

    @PostMapping("/reserve")
    fun reserveBook(@RequestBody book: BookDTO) = bookUseCase.reserveBook(book.title, book.author)
}