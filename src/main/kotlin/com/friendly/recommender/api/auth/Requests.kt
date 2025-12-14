package com.friendly.recommender.api.auth

import com.friendly.recommender.entities.FoodId
import com.friendly.recommender.entities.HobbyId
import com.friendly.recommender.entities.MovieId
import com.friendly.recommender.entities.PersonalityTraitId
import com.friendly.recommender.entities.UserId

data class AuthRequest(val email: String, val password: String)
data class AuthResponse(val token: String)
data class UserResponse(val id: String?, val firstname: String, val lastname: String, val email: String)
data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val traits: List<PersonalityTraitId> = emptyList(),
    val hobbies: List<HobbyId> = emptyList(),
    val likedMovies: List<MovieRelationshipRequest> = emptyList(),
    val dislikedMovies: List<MovieRelationshipRequest> = emptyList(),
    val foods: List<FoodRelationshipRequest> = emptyList(),
    val dislikeFood: List<FoodRelationshipRequest> = emptyList(),
    val likedUsers: List<UserId> = emptyList(),
    val dislikedUsers: List<UserId> = emptyList()
)
data class MovieRelationshipRequest(val movieId: MovieId, val score: Int)
data class FoodRelationshipRequest(val foodId: FoodId, val score: Int)
