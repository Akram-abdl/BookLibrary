package fr.akramlaurent.booklibrary.infrastructure.driver.dto

data class BookDTO(var title: String, var author: String) {
    init {
        require(title.isNotEmpty() && author.isNotEmpty()) { "Error you must provide and author and title name" }
    }
}