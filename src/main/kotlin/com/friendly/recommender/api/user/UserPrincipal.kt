package com.friendly.recommender.api.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserPrincipal(
    val userId: String,
    val email: String,
    private val usernameInternal: String,
    private val authoritiesInternal: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authoritiesInternal
    override fun getPassword(): String? = null
    override fun getUsername(): String = usernameInternal
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}