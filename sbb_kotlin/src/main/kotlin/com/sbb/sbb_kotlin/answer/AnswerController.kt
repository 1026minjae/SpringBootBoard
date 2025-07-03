package com.sbb.sbb_kotlin.answer

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

import com.sbb.sbb_kotlin.question.QuestionService

@RequestMapping("/answer")
@Controller
class AnswerController (
    private val questionService: QuestionService,
    private val AnswerService: AnswerService
) {
    @PostMapping("/create/{id}")
    fun createAnswer(model: Model, @PathVariable("id") id: Long, @RequestParam(value="content") content: String): String {
        val question = questionService.getQuestion(id)
        AnswerService.create(question, content)
        return "redirect:/question/detail/$id"
    }
}
