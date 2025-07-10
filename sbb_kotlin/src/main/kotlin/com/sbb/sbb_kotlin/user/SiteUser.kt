package com.sbb.sbb_kotlin.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("USERS")
data class SiteUser (
    @Id 
    val id: Long? = null, 
    
    @Column("username")
    var username: String,
    @Column("pw")
    var password: String,
    @Column("email")
    var email: String
)