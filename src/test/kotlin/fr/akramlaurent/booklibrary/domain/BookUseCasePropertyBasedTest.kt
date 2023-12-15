package fr.akramlaurent.booklibrary.domain

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isTrue
import fr.akramlaurent.booklibrary.domain.model.Book
import fr.akramlaurent.booklibrary.domain.port.BDDPort
import fr.akramlaurent.booklibrary.domain.usecase.BookUseCase
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import net.jqwik.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ExtendWith(MockKExtension::class)
class BookUseCasePropertyBasedTest {
    private val mock: BDDPort = mockk()
    private val bookUseCases = BookUseCase(mock)
    private val books = mutableListOf<Book>()

    @Provide
    fun book(): Arbitrary<Book> {
        return Arbitraries.strings().alpha().ofLength(10)
                .flatMap { title ->
                    Arbitraries.strings().alpha().ofLength(10)
                            .map { author ->
                                Book(title = title, author = author)
                            }
                }
    }

    @Provide
    fun books(): Arbitrary<List<Book>> {
        return book().list()
                .ofSize(10)
                .uniqueElements { b -> b.title }
    }

    @Property
    fun bookWasAddedProperly(
            @ForAll("book") arbitraryBook: Book
    ) {
        every { mock.addBook(any()) } answers { books.add(firstArg()) }
        every { mock.listLibrary() } answers { books.sortedBy { it.title } }
        bookUseCases.addBook(arbitraryBook)

        val result = bookUseCases.listLibrary()

        assertAll {
            assertThat(result).contains(arbitraryBook)
        }
    }

    @Property
    fun allBooksAreListedProperly(
            @ForAll("books") arbitraryBooks: List<Book>
    ) {
        books.clear()
        every { mock.addBook(any()) } answers { books.add(firstArg()) }
        every { mock.listLibrary() } answers { books.sortedBy { it.title } }
        arbitraryBooks.forEach { bookUseCases.addBook(it) }

        val result = bookUseCases.listLibrary()

        assertAll {
            assertThat(result.containsAll(arbitraryBooks)).isTrue()
        }
    }
}