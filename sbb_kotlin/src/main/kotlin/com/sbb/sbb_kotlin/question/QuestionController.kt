package com.sbb.sbb_kotlin.question

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import com.sbb.sbb_kotlin.answer.AnswerService

@RequestMapping("/question")
@Controller
class QuestionController (
    private val questionService: QuestionService,
    private val answerService: AnswerService
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
        val answerList = answerService.getList(id)
        model.addAttribute("question", question)
        model.addAttribute("answerList", answerList)
        return "question_detail"
    }
}
