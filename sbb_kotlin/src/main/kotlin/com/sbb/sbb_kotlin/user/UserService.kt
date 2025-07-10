package com.sbb.sbb_kotlin.user

import com.sbb.sbb_kotlin.DataNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun create(username: String, email: String, password: String) {
        val user = SiteUser(
            username = username,
            password = this.passwordEncoder.encode(password),
            email = email
        )

        this.userRepo.save(user)
    }
}