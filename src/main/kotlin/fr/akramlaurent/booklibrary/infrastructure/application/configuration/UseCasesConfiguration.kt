package fr.akramlaurent.booklibrary.infrastructure.application.configuration

import fr.akramlaurent.booklibrary.domain.usecase.BookUseCase
import fr.akramlaurent.booklibrary.infrastructure.driven.dao.BookDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(BookDAO::class)
class UseCasesConfiguration {

    @Bean
    fun bookUseCases(bookDAO: BookDAO): BookUseCase = BookUseCase(bookDAO)
}