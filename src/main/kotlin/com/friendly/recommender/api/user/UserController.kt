package com.friendly.recommender.api.user

import com.friendly.recommender.api.auth.JwtService
import com.friendly.recommender.entities.FoodId
import com.friendly.recommender.entities.HobbyId
import com.friendly.recommender.entities.PersonalityTraitId
import com.friendly.recommender.entities.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val jwtService: JwtService
) {

    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        val created = userService.createUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<User> {
        val user = userService.getUserById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> = ResponseEntity.ok(userService.getAllUsers())

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody user: User): ResponseEntity<User> {
        val updated = userService.updateUser(id, user) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        val deleted = userService.deleteUser(id)
        return if (deleted) ResponseEntity.noContent().build() else ResponseEntity.notFound().build()
    }

    @PostMapping("/{id}/like")
    fun likeUser(
        @PathVariable id: String,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Void> {
        val principalEmail = jwtService.extractEmail(authHeader.substring(7))
        userService.likeUser(
            userId = principalEmail,
            otherId = id
        )
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}/dislike")
    fun dislikeUser(
        @PathVariable id: String,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Void> {
        val principalEmail = jwtService.extractEmail(authHeader.substring(7))
        userService.dislikeUser(
            userId = principalEmail,
            otherId = id
        )
        return ResponseEntity.ok().build()
    }


    @PostMapping("/movie/{id}/like/{score}")
    fun likeMovie(
        @PathVariable id: String,
        @PathVariable score: Int,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Void> {
        jwtService.extractUserId(authHeader.substring(7)).let { userId ->
            userService.likeMovie(
                userId = userId,
                movieId = id,
                score = score
            )
        }
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/movie/{id}/dislike")
    fun dislikeMovie(
        @PathVariable id: String,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Void> {
        val principalEmail = jwtService.extractEmail(authHeader.substring(7))
        userService.dislikeMovie(
            userId = principalEmail,
            movieId = id
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/food/{id}/like/{score}")
    fun likeFood(
        @PathVariable id: String,
        @PathVariable score: Int,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Void> {
        val principalEmail = jwtService.extractEmail(authHeader.substring(7))
        userService.likeFood(
            userId = principalEmail,
            foodId = id,
            score = score
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/food/{id}/dislike")
    fun dislikeFood(
        @PathVariable id: FoodId,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Void> {
        val principalEmail = jwtService.extractEmail(authHeader.substring(7))
        userService.dislikeMovie(
            userId = principalEmail,
            movieId = id
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/hobby/{hobbyId}")
    fun addHobby(
        @PathVariable hobbyId: HobbyId,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Void> {
        val principalEmail = jwtService.extractEmail(authHeader.substring(7))
        userService.addHobby(
            userId = principalEmail,
            hobbyId = hobbyId
        )
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/remove-hobby/{hobbyId}")
    fun removeHobby(
        @PathVariable hobbyId: HobbyId,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Void> {
        val principalEmail = jwtService.extractEmail(authHeader.substring(7))
        userService.removeHobby(
            userId = principalEmail,
            hobbyId = hobbyId
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/trait/{traitId}")
    fun addTrait(
        @PathVariable traitId: PersonalityTraitId,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Void> {
        val principalEmail = jwtService.extractEmail(authHeader.substring(7))
        userService.addTrait(
            userId = principalEmail,
            traitId = traitId
        )
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/remove-trait/{traitId}")
    fun removeTrait(
        @PathVariable traitId: PersonalityTraitId,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Void> {
        val principalEmail = jwtService.extractEmail(authHeader.substring(7))
        userService.removeTrait(
            userId = principalEmail,
            traitId = traitId
        )
        return ResponseEntity.ok().build()
    }
}
