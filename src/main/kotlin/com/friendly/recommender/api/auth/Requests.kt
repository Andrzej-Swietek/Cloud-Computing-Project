package com.friendly.recommender.api.auth

data class AuthRequest(val email: String, val password: String)
data class AuthResponse(val token: String)
data class UserResponse(val id: String?, val firstname: String, val lastname: String, val email: String)
data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val traits: List<Long> = emptyList(),
    val hobbies: List<Long> = emptyList(),
    val likedMovies: List<MovieRelationshipRequest> = emptyList(),
    val dislikedMovies: List<MovieRelationshipRequest> = emptyList(),
    val foods: List<FoodRelationshipRequest> = emptyList(),
    val dislikeFood: List<FoodRelationshipRequest> = emptyList(),
    val likedUsers: List<Long> = emptyList(),
    val dislikedUsers: List<Long> = emptyList()
)
data class MovieRelationshipRequest(val movieId: Long, val score: Int)
data class FoodRelationshipRequest(val foodId: Long, val score: Int)
