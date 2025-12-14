package com.friendly.recommender.api.auth

import com.friendly.recommender.api.food.FoodService
import com.friendly.recommender.api.user.UserService
import com.friendly.recommender.entities.User
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/auth")
final class AuthController(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        val user = userService.findByEmail(request.email)
            ?: return ResponseEntity.status(401).build()

        if (!passwordEncoder.matches(request.password, user.password)) {
            return ResponseEntity.status(401).build()
        }

        val token = jwtService.generateToken(user.email, user.firstname, user.email, listOf())
        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<UserResponse> {
//        if (userService.findByEmail(request.email) != null) {
//            return ResponseEntity.status(409).build()
//        }

        val createdUser = userService.createUser(
            User(
                email = request.email,
                password = request.password,
                firstname = request.firstname,
                lastname = request.lastname
            )
        )

        request.traits.forEach { traitId -> userService.addTrait(createdUser.email, traitId) }
        request.hobbies.forEach { hobbyId -> userService.addHobby(createdUser.email, hobbyId) }
        request.likedMovies.forEach { movieRel ->
            userService.likeMovie(
                createdUser.email,
                movieRel.movieId,
                movieRel.score
            )
        }
        request.dislikedMovies.forEach { movieRel -> userService.dislikeMovie(createdUser.email, movieRel.movieId) }
        request.foods.forEach { foodRel -> userService.likeFood(createdUser.email, foodRel.foodId, foodRel.score) }
        request.dislikeFood.forEach { foodRel -> userService.dislikeFood(createdUser.email, foodRel.foodId) }
        request.likedUsers.forEach { otherId -> userService.likeUser(createdUser.email, otherId) }
        request.dislikedUsers.forEach { otherId -> userService.dislikeUser(createdUser.email, otherId) }

        val response = UserResponse(
            id = createdUser.email,
            firstname = createdUser.firstname,
            lastname = createdUser.lastname,
            email = createdUser.email
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/me")
    fun whoami(request: HttpServletRequest): ResponseEntity<UserResponse> {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build()
        }

        val token = authHeader.substring(7)
        val email = jwtService.extractUsername(token)
        val user = userService.findByEmail(email!!) ?: return ResponseEntity.status(401).build()

        return ResponseEntity.ok(UserResponse(user.email, user.firstname, user.lastname, user.email))
    }

    @GetMapping("/token/validate")
    fun validateToken(request: HttpServletRequest): ResponseEntity<Map<String, Boolean>> {
        val authHeader = request.getHeader("Authorization") ?: return ResponseEntity.ok(mapOf("valid" to false))
        if (!authHeader.startsWith("Bearer ")) return ResponseEntity.ok(mapOf("valid" to false))

        val token = authHeader.substring(7)
        val email = jwtService.extractUsername(token)
        val user = email?.let { userService.findByEmail(it) } ?: return ResponseEntity.ok(mapOf("valid" to false))
        val valid = jwtService.validateToken(token, user.email)
        return ResponseEntity.ok(mapOf("valid" to valid))
    }
}