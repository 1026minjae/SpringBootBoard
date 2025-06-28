package com.sbb.sbb_kotlin

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

import com.sbb.sbb_kotlin.question.QuestionRepository

@Controller
class QuestionController (
    private val questionRepo: QuestionRepository
) {
    @GetMapping("/question/list")
    fun list(model: Model): String {
        val questionList = questionRepo.findAll()
        model.addAttribute("questionList", questionList)
        return "question_list"
    }    
}
