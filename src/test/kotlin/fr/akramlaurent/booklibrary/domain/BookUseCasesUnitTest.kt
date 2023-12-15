package fr.akramlaurent.booklibrary.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import fr.akramlaurent.booklibrary.domain.exceptions.BookAlreadyReservedException
import fr.akramlaurent.booklibrary.domain.exceptions.BookNotFoundException
import fr.akramlaurent.booklibrary.domain.model.Book
import fr.akramlaurent.booklibrary.domain.port.BDDPort
import fr.akramlaurent.booklibrary.domain.usecase.BookUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BookUseCaseUnitTest {
    private val mock: BDDPort = mockk(relaxed = true)
    private val bookUseCases = BookUseCase(mock)
    private val books = mutableListOf<Book>()

    @BeforeEach
    fun setup() {
        books.clear()
        every { mock.addBook(any()) } answers { books.add(firstArg()) }
        every { mock.listLibrary() } answers { books.sortedBy { it.title } }
        every { mock.reserveBook(any()) } answers {
            books.find { it.title == firstArg<String>() && it.author == firstArg<String>() }?.reserved = true
        }
    }
    @Test
    fun titleAndAuthorEmpty() {
      
        assertThrows<Exception> {
            bookUseCases.addBook("", "")
        }
    }

    
    
    
    @Test
    fun listBook() {
        
        bookUseCases.addBook("Harry Potter", "JK Rowling")
        bookUseCases.addBook("Incroyable", "Hulk")
        bookUseCases.addBook("Salutation", "Mon Ami")
        
        
        val returnedBooks = bookUseCases.listLibrary()
        
        
        assertThat(returnedBooks[0].title).isEqualTo("Incroyable")
        assertThat(returnedBooks[1].title).isEqualTo("Harry Potter")
        assertThat(returnedBooks[2].title).isEqualTo("Salutation")
    }
    @Test
    fun `addBook adds a book at the end`() {
        

        
        bookUseCases.addBook("Harry Potter", "JK Rowling")

        
        val lastBook = books.last()
        assertThat(lastBook.title).isEqualTo("Harry Potter")
        assertThat(lastBook.author).isEqualTo("JK Rowlng")
    }

    @Test
    fun reserveBookReservesABook() {

        bookUseCases.addBook("Harry Potter", "JK Rowling")
        bookUseCases.reserveBook("Harry Potter", "JK Rowling")
        val lastBook = books.last()
        assertThat(lastBook.reserved).isEqualTo(true)
    }


    @Test
    fun bookDoesNotExist() {
        
        assertThrows<BookNotFoundException> {
            bookUseCases.reserveBook("Harry Potter", "JK Rowling")
        }
    }

    @Test
    fun bookAlreadyReserved() {

        bookUseCases.addBook("Harry Potter", "JK Rowling")
        bookUseCases.reserveBook("Harry Potter", "JK Rowling")
        assertThrows<BookAlreadyReservedException> {
            bookUseCases.reserveBook("Harry Potter", "JK Rowling")
        }
    }
}