package com.sbb.sbb_kotlin.user

import jakarta.validation.Valid
import java.sql.SQLIntegrityConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/user")
@Controller
class UserController (
    private val userService: UserService
) {
    @GetMapping("/signup")
    fun signup(userCreateForm: UserCreateForm): String {
        return "signup_form"
    }

    @PostMapping("/signup")
    fun signup(@Valid userCreateForm: UserCreateForm, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return "signup_form"
        }
        
        if (!userCreateForm.password1.equals(userCreateForm.password2)) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "Two passwords are not equal.")
            return "signup_form"
        }

        try {
            userService.create(userCreateForm.username!!, userCreateForm.email!!, userCreateForm.password1!!)
        }
        catch (e: Exception) {
            e.printStackTrace()
            bindingResult.reject(
                "signupFailed",
                when (e) {
                    is DataIntegrityViolationException, is DbActionExecutionException, is SQLIntegrityConstraintViolationException -> {
                        "Already signed-up user"
                    }
                    else -> {
                        e.message ?: ""
                    }
                }
            )
            return "signup_form"
        }

        return "redirect:/"
    }
}
