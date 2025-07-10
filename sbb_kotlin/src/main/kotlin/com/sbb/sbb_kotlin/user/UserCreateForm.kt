package com.sbb.sbb_kotlin.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

class UserCreateForm(
    @field:NotEmpty(message="Username is necessary")    
    @field:Size(min=3, max=25)
    val username: String?,
    
    @field:NotEmpty(message="Password is necessary")
    val password1: String?,

    @field:NotEmpty(message="Password confirm is necessary")
    val password2: String?,

    @field:Email
    @field:NotEmpty(message="Email is necessary")
    val email: String?
)