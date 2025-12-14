package com.friendly.recommender.api.user

import com.friendly.recommender.entities.FoodId
import com.friendly.recommender.entities.HobbyId
import com.friendly.recommender.entities.MovieId
import com.friendly.recommender.entities.PersonalityTraitId
import com.friendly.recommender.entities.User
import com.friendly.recommender.entities.UserId
import com.friendly.recommender.entities.respositories.ManualUserRepository
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
    fun likeMovie(userId: UserId, movieId: MovieId, score: Int): User
    fun dislikeMovie(userId: UserId, movieId: MovieId): User
    fun removeLikedMovie(userId: UserId, movieId: MovieId)
    fun removeDislikedMovie(userId: UserId, movieId: MovieId)

    // --- Food ---
    fun likeFood(userId: UserId, foodId: FoodId, score: Int): User
    fun dislikeFood(userId: UserId, foodId: FoodId): User
    fun removeLikedFood(userId: UserId, foodId: FoodId)
    fun removeDislikedFood(userId: UserId, foodId: FoodId)


    // --- Hobby ---
    fun addHobby(userId: UserId, hobbyId: HobbyId): User
    fun removeHobby(userId: UserId, hobbyId: HobbyId)

    // --- Trait ---
    fun addTrait(userId: UserId, traitId: PersonalityTraitId): User
    fun removeTrait(userId: UserId, traitId: PersonalityTraitId)

    // --- User relationships ---
    fun likeUser(userId: UserId, otherId: UserId): User
    fun dislikeUser(userId: UserId, otherId: UserId): User
    fun removeLikedUser(userId: UserId, otherId: UserId): User
    fun findTopLikedUsers(userId: UserId): List<User>

    // --- Recommendations ---
    fun findTop10SimilarUsers(userId: UserId): List<User>
    fun findTop10SimilarUsersByRelationship(userId: UserId, relationshipType: String): List<User>

}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val manualUserRepository: ManualUserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : UserService {

    override fun createUser(user: User): User {
        user.password = passwordEncoder.encode(user.password).toString()
        return manualUserRepository.createUser(user)
    }

    override fun getUserById(id: String): User? = manualUserRepository.findByIdWithRelations(id)?.toUser()

    override fun getAllUsers(): List<User> = manualUserRepository.findAllUsersWithRelations().map { it.toUser() }

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

    override fun findByEmail(username: String): User? = manualUserRepository.findByEmail(username)

    // --- Movie ---
    override fun likeMovie(userId: UserId, movieId: MovieId, score: Int): User =
        manualUserRepository.addLikedMovie(userId, movieId, score)

    override fun dislikeMovie(userId: UserId, movieId: MovieId): User =
        manualUserRepository.addDislikedMovie(userId, movieId)

    override fun removeLikedMovie(userId: UserId, movieId: MovieId) {
        manualUserRepository.removeLikedMovie(userId, movieId)
    }

    override fun removeDislikedMovie(userId: UserId, movieId: MovieId) {
        manualUserRepository.removeDislikedMovie(userId, movieId)
    }

    // --- Food ---

    override fun likeFood(userId: UserId, foodId: FoodId, score: Int): User =
        manualUserRepository.addLikedFood(userId, foodId, score)

    override fun dislikeFood(userId: UserId, foodId: FoodId): User =
        manualUserRepository.addDislikedFood(userId, foodId)

    override fun removeLikedFood(userId: UserId, foodId: FoodId) {
        manualUserRepository.removeLikedFood(userId, foodId)
    }

    override fun removeDislikedFood(userId: UserId, foodId: FoodId) {
        manualUserRepository.removeDislikedFood(userId, foodId)
    }


    // --- Hobby ---
    override fun addHobby(userId: UserId, hobbyId: HobbyId): User =
        manualUserRepository.addHobby(userId, hobbyId)

    override fun removeHobby(userId: UserId, hobbyId: HobbyId) {
        manualUserRepository.removeHobby(userId, hobbyId)
    }

    // --- Trait ---
    override fun addTrait(userId: UserId, traitId: PersonalityTraitId): User =
        manualUserRepository.addTrait(userId, traitId)

    override fun removeTrait(userId: UserId, traitId: PersonalityTraitId) {
        manualUserRepository.removeTrait(userId, traitId)
    }

    // --- User relationships ---
    override fun likeUser(userId: UserId, otherId: UserId): User =
        manualUserRepository.addLikedUser(userId, otherId)

    override fun dislikeUser(userId: UserId, otherId: UserId): User =
        manualUserRepository.addDislikedUser(userId, otherId)

    override fun removeLikedUser(userId: UserId, otherId: UserId): User =
        manualUserRepository.removeLikedUser(userId, otherId)

    override fun findTopLikedUsers(userId: UserId): List<User> =
        manualUserRepository.findTopLikedUsers(userId)

    // --- Recommendations ---
    override fun findTop10SimilarUsers(userId: UserId): List<User> =
        manualUserRepository.findTop10SimilarUsers(userId)

    override fun findTop10SimilarUsersByRelationship(userId: UserId, relationshipType: String): List<User> =
        manualUserRepository.findTop10SimilarUsersByRelationship(userId, relationshipType)
}