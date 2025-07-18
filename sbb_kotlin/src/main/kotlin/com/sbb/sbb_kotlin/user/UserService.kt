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

    fun getUser(username: String): UserInfo {
        val siteUser = this.userRepo.findByUsername(username)
        if (siteUser.isPresent) {
            val user = siteUser.get()
            return UserInfo(user.id!!, user.username)
        }
        else {
            throw DataNotFoundException("User is not found")
        }
    }
}