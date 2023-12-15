package fr.akramlaurent.booklibrary.driver.controller

import com.ninjasquad.springmockk.MockkBean
import fr.akramlaurent.booklibrary.domain.exceptions.BookAlreadyReservedException
import fr.akramlaurent.booklibrary.domain.exceptions.BookNotFoundException
import fr.akramlaurent.booklibrary.domain.model.Book
import fr.akramlaurent.booklibrary.domain.usecase.BookUseCase
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@ExtendWith(SpringExtension::class)
@WebMvcTest
class BookControllerTest {
    @MockkBean
    private lateinit var bookUseCases: BookUseCase

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun validGetBooks() {
        every { bookUseCases.listLibrary() } returns listOf(Book("Harry Potter", "JK Rowling"))

        mockMvc.get("/books") {
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON)  }
            content { json("""[{"title":"Harry Potter","author":"JK Rowling"}]""") }
        }
    }

    @Test
    fun validPostBooks() {
        every { bookUseCases.addBook(any(), any()) } returns Unit

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                        {
                            "title": "Harry Potter",
                            "author": "JK Rowling"
                        }
                    """.trimIndent()
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun postError() {
        every { bookUseCases.addBook(any(), any()) } returns Unit

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                        {
                            "titl": "Harry Potter",
                            "autho": "JK Rowling"
                        }
                    """.trimIndent()
        }.andExpect {
            status { isInternalServerError() }
        }
    }

    @Test
    fun postException() {
        every { bookUseCases.addBook(any(), any()) } throws RuntimeException()

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                        {
                            "title": "Harry Potter",
                            "author": "JK Rowling"
                        }
                    """.trimIndent()
        }.andExpect {
            status { isInternalServerError() }
        }
    }

    @Test
    fun postReserveValid() {
        every { bookUseCases.reserveBook(any(), any()) } returns Unit

        mockMvc.post("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                        {
                            "title": "Harry Potter",
                            "author": "JK Rowling"
                        }
                    """.trimIndent()
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun postReserveInvalid() {
        every { bookUseCases.reserveBook(any(), any()) } returns Unit

        mockMvc.post("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                        {
                            "blabla": "Harry Potter",
                            "blabla": "JK Rowling"
                        }
                    """.trimIndent()
        }.andExpect {
            status { isInternalServerError() }
        }
    }
    @Test
    fun postBookNotFoundError() {
        every { bookUseCases.reserveBook(any(), any()) } throws BookNotFoundException()

        mockMvc.post("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                        {
                            "title": "Harry Potter",
                            "author": "JK Rowling"
                        }
                    """.trimIndent()
        }.andExpect {
            status { isNotFound() }
            content { string("Book not found") }
        }
    }

    @Test
    fun postBookAlreadyReservedError() {
        every { bookUseCases.reserveBook(any(), any()) } throws BookAlreadyReservedException()

        mockMvc.post("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                        {
                            "title": "Harry Potter",
                            "author": "JK Rowling"
                        }
                    """.trimIndent()
        }.andExpect {
            status { isConflict() }
            content { string("Book already reserved") }
        }
    }

    
}