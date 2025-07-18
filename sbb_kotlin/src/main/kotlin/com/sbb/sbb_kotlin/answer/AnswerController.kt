package com.sbb.sbb_kotlin.answer

import com.sbb.sbb_kotlin.answer.AnswerService
import com.sbb.sbb_kotlin.question.QuestionService
import com.sbb.sbb_kotlin.user.UserService
import jakarta.validation.Valid
import java.security.Principal
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/answer")
@Controller
class AnswerController (
    private val questionService: QuestionService,
    private val answerService: AnswerService,
    private val userService: UserService
) {
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    fun createAnswer(
        model: Model, 
        @PathVariable("id") id: Long, 
        @Valid answerForm: AnswerForm, 
        bindingResult: BindingResult,
        principal: Principal
    ): String {
        val question = this.questionService.getQuestionDetail(id)

        val user = userService.getUser(principal.getName())

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question)
            return "question_detail"
        }
        
        this.answerService.create(question.id, answerForm.content!!, user)

        return "redirect:/question/detail/$id"
    }
}
