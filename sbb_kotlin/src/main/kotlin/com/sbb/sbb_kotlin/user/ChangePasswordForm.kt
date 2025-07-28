package com.sbb.sbb_kotlin.user

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

class ChangePasswordForm(
    @field:NotEmpty(message="Password is necessary")
    val password1: String?,

    @field:NotEmpty(message="Password confirm is necessary")
    val password2: String?
)