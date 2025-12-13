package com.friendly.recommender.api.auth


import com.friendly.recommender.api.user.UserPrincipal
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class JwtToUserPrincipalConverter(
    private val jwtService: JwtService
) : Converter<Jwt, AbstractAuthenticationToken> {

    override fun convert(jwt: Jwt): AbstractAuthenticationToken? {
        val userId = jwtService.extractUserId(jwt.tokenValue)
        val username = jwtService.extractUsername(jwt.tokenValue)
        val email = jwtService.extractEmail(jwt.tokenValue)
        val roles = jwtService.extractRoles(jwt.tokenValue)

        val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }

        val principal = UserPrincipal(
            userId = userId,
            email = email,
            usernameInternal = username ?: "",
            authoritiesInternal = authorities
        )

        return UsernamePasswordAuthenticationToken(principal, jwt.tokenValue, authorities)
    }
}