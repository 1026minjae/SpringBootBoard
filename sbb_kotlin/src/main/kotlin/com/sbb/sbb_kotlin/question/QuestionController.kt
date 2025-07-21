package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.answer.AnswerForm
import com.sbb.sbb_kotlin.answer.AnswerService
import com.sbb.sbb_kotlin.user.UserService
import jakarta.validation.Valid
import java.security.Principal
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException

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
        val user = userService.getUser(principal.getName())
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
    fun list(
        model: Model, 
        @RequestParam(value="page", defaultValue="0") page: Int,
        @RequestParam(value="kw", defaultValue="") kw: String
    ): String {
        val paging = questionService.getList(page, kw)
        model.addAttribute("paging", paging)
        model.addAttribute("kw", kw)
        return "question_list"
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    fun questionDelete(principal: Principal, @PathVariable("id") id: Long): String {
        val question = questionService.getQuestionInfo(id)

        if (!question.authorName.equals(principal.getName())) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission to delete")
        }

        questionService.delete(id);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    fun questionModify(questionForm: QuestionForm, @PathVariable("id") id: Long, principal: Principal): String {
        val question = questionService.getQuestionInfo(id)

        if(!question.authorName.equals(principal.getName())) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission to modify")
        }
        
        questionForm.title = question.title
        questionForm.content = question.content
        
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    fun questionModify(
        @Valid questionForm: QuestionForm, 
        bindingResult: BindingResult, 
        principal: Principal, 
        @PathVariable("id") id: Long
    ): String {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        val question = questionService.getQuestionInfo(id)

        if (!question.authorName.equals(principal.getName())) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission to modify")
        }

        questionService.modify(question.id, questionForm.title!!, questionForm.content!!)
        
        return "redirect:/question/detail/$id"
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    fun questionVote(principal: Principal, @PathVariable("id") id: Long): String {
        val voters = questionService.getQuestionVoters(id)
        val newVoter = userService.getUser(principal.getName())
        
        questionService.vote(voters, newVoter)
        
        return "redirect:/question/detail/$id"
    }

}
