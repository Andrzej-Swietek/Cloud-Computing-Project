package com.friendly.recommender.api.user

import com.friendly.recommender.entities.User
import com.friendly.recommender.entities.respositories.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


interface UserService {
    fun createUser(user: User): User
    fun getUserById(id: String): User?
    fun getAllUsers(): List<User>
    fun updateUser(id: String, updatedUser: User): User?
    fun deleteUser(id: String): Boolean
    fun findByEmail(username: String): User?

    // --- Movie ---
    fun likeMovie(userId: String, movieId: Long, score: Int): User
    fun dislikeMovie(userId: String, movieId: Long): User
    fun removeLikedMovie(userId: String, movieId: Long)
    fun removeDislikedMovie(userId: String, movieId: Long)

    // --- Food ---
    fun likeFood(userId: String, foodId: Long, score: Int): User
    fun dislikeFood(userId: String, foodId: Long): User
    fun removeLikedFood(userId: String, foodId: Long)
    fun removeDislikedFood(userId: String, foodId: Long)


    // --- Hobby ---
    fun addHobby(userId: String, hobbyId: Long): User
    fun removeHobby(userId: String, hobbyId: Long)

    // --- Trait ---
    fun addTrait(userId: String, traitId: Long): User
    fun removeTrait(userId: String, traitId: Long)

    // --- User relationships ---
    fun likeUser(userId: String, otherId: Long): User
    fun dislikeUser(userId: String, otherId: Long): User
    fun removeLikedUser(userId: String, otherId: Long): User
    fun findTopLikedUsers(userId: String): List<User>

    // --- Recommendations ---
    fun findTop10SimilarUsers(userId: String): List<User>
    fun findTop10SimilarUsersByRelationship(userId: String, relationshipType: String): List<User>

}


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : UserService {

    override fun createUser(user: User): User {
        user.password = passwordEncoder.encode(user.password).toString()
        return userRepository.save(user)
    }

    override fun getUserById(id: String): User? = userRepository.findById(id).orElse(null)

    override fun getAllUsers(): List<User> = userRepository.findAll()

    override fun updateUser(id: String, updatedUser: User): User? {
        val existing = getUserById(id) ?: return null
        existing.email = updatedUser.email
        updatedUser.password
            .takeIf { it.isNotBlank() }
            ?.let { newPassword ->
                existing.password = passwordEncoder.encode(newPassword).toString()
            }

        existing.traits = updatedUser.traits
        existing.likedMovies = updatedUser.likedMovies
        existing.dislikedMovies = updatedUser.dislikedMovies
        existing.hobbies = updatedUser.hobbies
        existing.foods = updatedUser.foods
        existing.dislikeFood = updatedUser.dislikeFood
        return userRepository.save(existing)
    }

    override fun deleteUser(id: String): Boolean {
        val exists = userRepository.existsById(id)
        if (exists) userRepository.deleteById(id)
        return exists
    }

    override fun findByEmail(username: String): User? = userRepository.findByEmail(username)

    // --- Movie ---
    override fun likeMovie(userId: String, movieId: Long, score: Int): User =
        userRepository.addLikedMovie(userId, movieId, score)

    override fun dislikeMovie(userId: String, movieId: Long): User =
        userRepository.addDislikedMovie(userId, movieId)

    override fun removeLikedMovie(userId: String, movieId: Long) =
        userRepository.removeLikedMovie(userId, movieId)

    override fun removeDislikedMovie(userId: String, movieId: Long) =
        userRepository.removeDislikedMovie(userId, movieId)

    // --- Food ---

    override fun likeFood(
        userId: String,
        foodId: Long,
        score: Int
    ): User =
        userRepository.addLikedFood(userId, foodId, score)

    override fun dislikeFood(userId: String, foodId: Long): User =
        userRepository.addDislikedFood(userId, foodId)

    override fun removeLikedFood(userId: String, foodId: Long) =
        userRepository.removeLikedFood(userId, foodId)

    override fun removeDislikedFood(userId: String, foodId: Long) =
        userRepository.removeDislikedFood(userId, foodId)


    // --- Hobby ---
    override fun addHobby(userId: String, hobbyId: Long): User =
        userRepository.addHobby(userId, hobbyId)

    override fun removeHobby(userId: String, hobbyId: Long) =
        userRepository.removeHobby(userId, hobbyId)

    // --- Trait ---
    override fun addTrait(userId: String, traitId: Long): User =
        userRepository.addTrait(userId, traitId)

    override fun removeTrait(userId: String, traitId: Long) =
        userRepository.removeTrait(userId, traitId)

    // --- User relationships ---
    override fun likeUser(userId: String, otherId: Long): User =
        userRepository.addLikedUser(userId, otherId)

    override fun dislikeUser(userId: String, otherId: Long): User =
        userRepository.addDislikedUser(userId, otherId)

    override fun removeLikedUser(userId: String, otherId: Long): User =
        userRepository.removeLikedUser(userId, otherId)

    override fun findTopLikedUsers(userId: String): List<User> =
        userRepository.findTopLikedUsers(userId)

    // --- Recommendations ---
    override fun findTop10SimilarUsers(userId: String): List<User> =
        userRepository.findTop10SimilarUsers(userId)

    override fun findTop10SimilarUsersByRelationship(userId: String, relationshipType: String): List<User> =
        userRepository.findTop10SimilarUsersByRelationship(userId, relationshipType)
}