package com.sbb.sbb_kotlin.answer

import com.sbb.sbb_kotlin.answer.AnswerService
import com.sbb.sbb_kotlin.question.QuestionService
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
        val question = questionService.getQuestionDetail(id)

        val user = userService.getUser(principal.getName())

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question)
            return "question_detail"
        }
        
        val answer = answerService.create(question.id, answerForm.content!!, user)

        return "redirect:/question/detail/${id}#answer_${answer.id}"
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    fun answerDelete(principal: Principal, @PathVariable("id") id: Long): String {
        val answer = answerService.getAnswerDetail(id)

        if (!answer.author.username.equals(principal.getName())) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission to delete")
        }

        answerService.delete(answer.id)

        return "redirect:/question/detail/${answer.questionId}"
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    fun answerModify(
        answerForm: AnswerForm, 
        @PathVariable("id") id: Long, 
        principal: Principal
    ): String {
        val answer = answerService.getAnswerDetail(id)

        if (!answer.author.username.equals(principal.getName())) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission to modify")
        }
        
        answerForm.content = answer.content

        return "answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    fun answerModify(
        @Valid answerForm: AnswerForm, 
        bindingResult: BindingResult,
        @PathVariable("id") id: Long, 
        principal: Principal
    ): String {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }

        val answer = this.answerService.getAnswerDetail(id)

        if (!answer.author.username.equals(principal.getName())) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission to modify")
        }

        answerService.modify(answer, answerForm.content!!)

        return "redirect:/question/detail/${answer.questionId}#answer_${id}"
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    fun answerVote(principal: Principal, @PathVariable("id") id: Long): String {
        val voters = answerService.getAnswerVoters(id)
        val newVoter = userService.getUser(principal.getName())

        answerService.vote(voters, newVoter)

        val qid = answerService.getQuestionIdForAnswer(id)

        return "redirect:/question/detail/${qid}#answer_${id}"
    }

}
