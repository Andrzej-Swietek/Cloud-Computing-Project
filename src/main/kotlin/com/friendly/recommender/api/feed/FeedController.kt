package com.friendly.recommender.api.feed

import com.friendly.recommender.api.user.UserService
import com.friendly.recommender.entities.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feed")
class FeedController(
    private val userService: UserService,
) {

    @GetMapping("/{email}")
    fun getFeed(
        @PathVariable(value = "email") email: String,
        @RequestParam(value = "limit") limit: Int = 10,
    ): ResponseEntity<List<User>> =
        ResponseEntity.ok(userService.getFeedForUser(email, limit))
}