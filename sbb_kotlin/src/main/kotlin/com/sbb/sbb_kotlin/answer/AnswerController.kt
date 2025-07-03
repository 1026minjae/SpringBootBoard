package com.sbb.sbb_kotlin.answer

import com.sbb.sbb_kotlin.answer.AnswerService
import com.sbb.sbb_kotlin.question.QuestionService
import jakarta.validation.Valid
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
    private val answerService: AnswerService
) {
    @PostMapping("/create/{id}")
    fun createAnswer(model: Model, @PathVariable("id") id: Long, @Valid answerForm: AnswerForm, bindingResult: BindingResult): String {
        val question = questionService.getQuestion(id)

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question)
            return "question_detail"
        }
        
        answerService.create(question, answerForm.content!!)

        return "redirect:/question/detail/$id"
    }
}
