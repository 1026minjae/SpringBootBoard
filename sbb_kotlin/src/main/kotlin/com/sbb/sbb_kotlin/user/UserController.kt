package com.sbb.sbb_kotlin.user

import com.sbb.sbb_kotlin.DataNotFoundException
import com.sbb.sbb_kotlin.MailService
import jakarta.validation.Valid
import jakarta.servlet.http.HttpServletRequest
import java.sql.SQLIntegrityConstraintViolationException
import java.security.Principal
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/user")
@Controller
class UserController (
    private val userService: UserService,
    private val mailService: MailService
) {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/change-password")
    fun changePassword(changePasswordForm: ChangePasswordForm): String {
        return "change_password_form"
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    fun changePassword(
        @Valid changePasswordForm: ChangePasswordForm,
        bindingResult: BindingResult,
        principal: Principal,
        request: HttpServletRequest
    ): String {
        if (bindingResult.hasErrors()) {
            return "change_password_form"
        }

        if (!changePasswordForm.password1.equals(changePasswordForm.password2)) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "Two passwords are not equal.")
            return "change_password_form"
        }

        userService.changePassword(principal.getName(), changePasswordForm.password1!!)

        request.logout()

        return "redirect:/user/login"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login_form"
    }

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

    @GetMapping("/send-temp-password")
    fun sendTempPassword(emailForm: EmailForm): String {
        return "temp_password"
    }

    @PostMapping("/send-temp-password")
    fun sendTempPassword(
        @Valid emailForm: EmailForm,
        bindingResult: BindingResult
    ): String {
        if (bindingResult.hasErrors()) {
            return "temp_password"
        }

        try {
            val tempPassword = userService.registerTempPassword(emailForm.email!!)
            
            mailService.sendTempPassword(emailForm.email, tempPassword)
        }
        catch (e: Exception) {
            e.printStackTrace()
            bindingResult.reject(
                "userNotFound",
                when (e) {
                    is DataNotFoundException -> {
                        "Given e-mail is not registered"
                    }
                    else -> {
                        e.message ?: ""
                    }
                }
            )
            return "signup_form"
        }

        return "redirect:/user/login"
    }
}
