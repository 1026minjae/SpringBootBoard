package com.sbb.sbb_kotlin

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

import com.sbb.sbb_kotlin.question.QuestionRepository
import com.sbb.sbb_kotlin.question.QuestionService

@RequestMapping("/question")
@Controller
class QuestionController (
    private val questionRepo: QuestionRepository,
    private val questionService: QuestionService
) {
    @GetMapping("/list")
    fun list(model: Model): String {
        val questionList = questionService.getList()
        model.addAttribute("questionList", questionList)
        return "question_list"
    }

    @GetMapping("/detail/{id}")
    fun detail(model: Model, @PathVariable("id") id: Long): String {
        val question = questionService.getQuestion(id)
        model.addAttribute("question", question)
        return "question_detail"
    }
}
