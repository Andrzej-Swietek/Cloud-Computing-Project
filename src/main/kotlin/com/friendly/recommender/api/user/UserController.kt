package com.friendly.recommender.api.user

import com.friendly.recommender.entities.FoodId
import com.friendly.recommender.entities.HobbyId
import com.friendly.recommender.entities.PersonalityTraitId
import com.friendly.recommender.entities.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
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
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        userService.likeUser(
            userId = principal.userId,
            otherId = id
        )
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}/dislike")
    fun dislikeUser(
        @PathVariable id: String,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        userService.dislikeUser(
            userId = principal.userId,
            otherId = id
        )
        return ResponseEntity.ok().build()
    }


    @PostMapping("/movie/{id}/like/{score}")
    fun likeMovie(
        @PathVariable id: String,
        @PathVariable score: Int,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        userService.likeMovie(
            userId = principal.userId,
            movieId = id,
            score = score
        )
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/movie/{id}/dislike")
    fun dislikeMovie(
        @PathVariable id: String,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        userService.dislikeMovie(
            userId = principal.userId,
            movieId = id
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/food/{id}/like/{score}")
    fun likeFood(
        @PathVariable id: String,
        @PathVariable score: Int,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        userService.likeFood(
            userId = principal.userId,
            foodId = id,
            score = score
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/food/{id}/dislike")
    fun dislikeFood(
        @PathVariable id: FoodId,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        userService.dislikeMovie(
            userId = principal.userId,
            movieId = id
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/hobby/{hobbyId}")
    fun addHobby(
        @PathVariable hobbyId: HobbyId,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        userService.addHobby(
            userId = principal.userId,
            hobbyId = hobbyId
        )
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/remove-hobby/{hobbyId}")
    fun removeHobby(
        @PathVariable hobbyId: HobbyId,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        userService.removeHobby(
            userId = principal.userId,
            hobbyId = hobbyId
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/trait/{traitId}")
    fun addTrait(
        @PathVariable traitId: PersonalityTraitId,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        userService.addTrait(
            userId = principal.userId,
            traitId = traitId
        )
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/remove-trait/{traitId}")
    fun removeTrait(
        @PathVariable traitId: PersonalityTraitId,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        userService.removeTrait(
            userId = principal.userId,
            traitId = traitId
        )
        return ResponseEntity.ok().build()
    }
}
