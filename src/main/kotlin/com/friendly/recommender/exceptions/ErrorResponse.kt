package com.friendly.recommender.exceptions

data class ErrorResponse(
    val errors: Map<String, String>
)