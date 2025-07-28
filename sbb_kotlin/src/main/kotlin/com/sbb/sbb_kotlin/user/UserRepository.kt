package com.sbb.sbb_kotlin.user

import java.util.Optional
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/* For simple sql actions */
interface UserRepository : CrudRepository<SiteUser, Long> {
    fun findByEmail(email: String): Optional<SiteUser>
    fun findByUsername(username: String): Optional<SiteUser>
}