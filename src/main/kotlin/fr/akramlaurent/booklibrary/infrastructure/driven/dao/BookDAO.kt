package fr.akramlaurent.booklibrary.infrastructure.driven.dao

import fr.akramlaurent.booklibrary.domain.model.Book
import fr.akramlaurent.booklibrary.domain.port.BDDPort
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service

@Service
class BookDAO (private val jdbcTemplate: JdbcTemplate): BDDPort {

    override fun addBook(book: Book) {
        jdbcTemplate.update("INSERT INTO book (title, author) VALUES (?, ?)", book.title, book.author)
    }

    override fun listLibrary(): List<Book> {
        return jdbcTemplate.query("SELECT * FROM book", RowMapper { rs, _ ->
            Book(
                title = rs.getString("title"),
                author = rs.getString("author"),
                reserved = rs.getBoolean("reserved")
            )
        })
    }

    override fun reserveBook(book: Book) {
        jdbcTemplate.update("UPDATE book SET reserved = true WHERE title = ? AND author = ?", book.title, book.author)
    }
}