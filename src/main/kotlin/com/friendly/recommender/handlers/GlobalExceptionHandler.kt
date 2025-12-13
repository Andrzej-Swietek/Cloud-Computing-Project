package com.friendly.recommender.handlers

import com.friendly.recommender.exceptions.ErrorResponse
import com.friendly.recommender.exceptions.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exp: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = exp.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid") }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(errors))
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationFailedException(exception: ValidationException): ResponseEntity<ErrorResponse> {
        val errorMessage = exception.errors?.joinToString("\n") ?: "no errors"
        val error = mapOf(
            "message" to (exception.message ?: "Validation failed"),
            "errors" to errorMessage
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(error))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(exp: Exception): ResponseEntity<ErrorResponse> {
        val error = mapOf("error" to (exp.message ?: "Internal server error"))
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse(error))
    }
}
