package com.sbb.sbb_kotlin.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

class EmailForm(
    @field:Email
    @field:NotEmpty(message="Email is necessary")
    val email: String?
)