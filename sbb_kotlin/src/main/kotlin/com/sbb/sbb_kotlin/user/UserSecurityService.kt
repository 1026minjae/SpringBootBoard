package com.sbb.sbb_kotlin.user;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserSecurityService (
    private val userRepo: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val _siteUser = userRepo.findByUsername(username);
        if (_siteUser.isEmpty()) {
            throw UsernameNotFoundException("Cannot find the user")
        }

        val siteUser = _siteUser.get();
        val authorities = mutableListOf<GrantedAuthority>()

        if ("admin".equals(username)) {
            authorities.add(SimpleGrantedAuthority(UserRole.ADMIN.value));
        } else {
            authorities.add(SimpleGrantedAuthority(UserRole.USER.value));
        }
        return User(siteUser.username, siteUser.password, authorities);
    }
}