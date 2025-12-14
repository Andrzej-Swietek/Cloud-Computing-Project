package com.friendly.recommender.api.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*

interface JwtService {
    fun generateToken(userId: String, username: String, email: String, roles: List<String>): String
    fun extractUsername(token: String): String?
    fun extractUserId(token: String): String
    fun extractEmail(token: String): String
    fun extractRoles(token: String): List<String>
    fun validateToken(token: String, username: String): Boolean
}

@Service
class JwtServiceImpl(
    @Value("\${jwt.secret}") private val secret: String
) : JwtService {

    private val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    override fun generateToken(userId: String, username: String, email: String, roles: List<String>): String =
        Jwts.builder()
            .setSubject(username)
            .claim("userId", userId)
            .claim("email", email)
            .claim("roles", roles)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

    override fun extractUsername(token: String): String? =
        extractClaims(token).subject

    override fun extractUserId(token: String): String =
        extractClaims(token)["userId"]?.toString()
            ?: throw IllegalArgumentException("userId not found in token")

    override fun extractEmail(token: String): String =
        extractClaims(token)["email"]?.toString() ?: ""

    override fun extractRoles(token: String): List<String> {
        return when (val roles = extractClaims(token)["roles"]) {
            is List<*> -> roles.map { it.toString() }
            is String -> listOf(roles)
            else -> emptyList()
        }
    }

    override fun validateToken(token: String, username: String): Boolean =
        extractUsername(token) == username && !isTokenExpired(token)

    private fun isTokenExpired(token: String): Boolean =
        extractClaims(token).expiration.before(Date())

    private fun extractClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
}