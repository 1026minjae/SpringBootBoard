package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.answer.AnswerForm
import com.sbb.sbb_kotlin.answer.AnswerService
import com.sbb.sbb_kotlin.user.UserService
import jakarta.validation.Valid
import java.security.Principal
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/question")
@Controller
class QuestionController (
    private val questionService: QuestionService,
    private val answerService: AnswerService,
    private val userService: UserService
) {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    fun create(questionForm: QuestionForm): String {
        return "question_form"
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    fun create(
        @Valid questionForm: QuestionForm, 
        bindingResult: BindingResult,
        principal: Principal
    ): String {
        val user = this.userService.getUser(principal.getName())
        if (bindingResult.hasErrors()) {
            return "question_form"
        }
        questionService.create(questionForm.title!!, questionForm.content!!, user)
        return "redirect:/question/list"
    }

    @GetMapping("/detail/{id}")
    fun detail(model: Model, @PathVariable("id") id: Long, answerForm: AnswerForm): String {
        val question = questionService.getQuestionDetail(id)
        model.addAttribute("question", question)
        return "question_detail"
    }

    @GetMapping("/list")
    fun list(model: Model, @RequestParam(value="page", defaultValue="0") page: Int): String {
        val paging = questionService.getList(page)
        model.addAttribute("paging", paging)
        return "question_list"
    }
}
