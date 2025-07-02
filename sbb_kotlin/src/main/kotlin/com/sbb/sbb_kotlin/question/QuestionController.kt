package com.sbb.sbb_kotlin

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

import com.sbb.sbb_kotlin.question.QuestionService

@Controller
class QuestionController (
    private val questionService: QuestionService
) {
    @GetMapping("/question/list")
    fun list(model: Model): String {
        val questionList = questionService.getList()
        model.addAttribute("questionList", questionList)
        return "question_list"
    }    
}
