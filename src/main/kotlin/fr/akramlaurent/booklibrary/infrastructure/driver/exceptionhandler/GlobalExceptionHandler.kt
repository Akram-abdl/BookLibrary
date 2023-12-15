package fr.akramlaurent.booklibrary.infrastructure.driver.exceptionhandler

import fr.akramlaurent.booklibrary.domain.exceptions.BookAlreadyReservedException
import fr.akramlaurent.booklibrary.domain.exceptions.BookNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handleGlobalException(e: Exception): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(value = [BookAlreadyReservedException::class])
    fun handleGlobalException(e: BookAlreadyReservedException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(value = [BookNotFoundException::class])
    fun handleGlobalException(e: BookNotFoundException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }
}