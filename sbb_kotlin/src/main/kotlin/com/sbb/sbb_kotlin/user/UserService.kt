package com.sbb.sbb_kotlin.user

import com.sbb.sbb_kotlin.DataNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.sbb.sbb_kotlin.PasswordGenerator

@Service
class UserService (
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val passwordGenerator: PasswordGenerator
) {

    @Transactional
    fun changePassword(username: String, newPassword: String) {
        val siteUser = userRepo.findByUsername(username)
        if (siteUser.isPresent) {
            val user = siteUser.get()
            
            user.password = passwordEncoder.encode(newPassword)

            userRepo.save(user)
        }
        else {
            throw DataNotFoundException("User is not found")
        }
    }

    @Transactional
    fun create(username: String, email: String, password: String) {
        val user = SiteUser(
            username = username,
            password = passwordEncoder.encode(password),
            email = email
        )

        userRepo.save(user)
    }
  
    fun getUser(username: String): UserInfo {
        val siteUser = userRepo.findByUsername(username)
        if (siteUser.isPresent) {
            val user = siteUser.get()
            return UserInfo(user.id!!, user.username)
        }
        else {
            throw DataNotFoundException("User is not found")
        }
    }

    fun registerTempPassword(email: String): String {
        val siteUser = userRepo.findByEmail(email)
        if (siteUser.isPresent) {
            val user = siteUser.get()
            val tempPassword = passwordGenerator.generate()

            user.password = passwordEncoder.encode(tempPassword)

            userRepo.save(user)

            return tempPassword
        }
        else {
            throw DataNotFoundException("User is not found")
        }
    }
}