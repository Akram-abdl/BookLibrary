package fr.akramlaurent.booklibrary.driven.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import fr.akramlaurent.booklibrary.domain.model.Book
import fr.akramlaurent.booklibrary.infrastructure.driven.dao.BookDAO
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@ExtendWith(SpringExtension::class)
@SpringBootTest
@Testcontainers
class BookDaoTest {

    @Autowired
    private lateinit var bookDao: BookDAO

    companion object {
        @Container
        private val postgreSQLContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:13-alpine")

        @JvmStatic
        @BeforeAll
        fun setUp() {
            postgreSQLContainer.start()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            postgreSQLContainer.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
        // Empty the book table before each test
        @BeforeEach
        fun cleanUp() {
            performQuery("DELETE FROM book")
        }

        
        @Test
        fun `Book reservation`() {
            val book = Book("Moby Dick", "Herman Melville")
            bookDao.addBook(book)
            
            bookDao.reserveBook(book)
            val books = bookDao.listLibrary()
            
            Assertions.assertTrue(books.any { it.title == book.title && it.author == book.author && it.reserved })
        }
        
        @Test
        fun `Book listing`() {
            val book1 = Book("To Kill a Mockingbird", "Harper Lee")
            val book2 = Book("The Great Gatsby", "F. Scott Fitzgerald")

            bookDao.addBook(book1)
            bookDao.addBook(book2)
            val books = bookDao.listLibrary()

            Assertions.assertTrue(books.any { it.title == book1.title && it.author == book1.author })
            Assertions.assertTrue(books.any { it.title == book2.title && it.author == book2.author })
        }
    }
}