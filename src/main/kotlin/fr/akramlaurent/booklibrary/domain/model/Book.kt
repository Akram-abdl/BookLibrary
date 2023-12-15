package fr.akramlaurent.booklibrary.domain.model

class Book(var title: String, var author: String, var reserved: Boolean = false) {
    init {
        require(title.isNotEmpty() && author.isNotEmpty()) { "Title and author must not be empty" }
    }
}