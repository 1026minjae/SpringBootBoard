package com.sbb.sbb_kotlin.answer

import com.sbb.sbb_kotlin.question.Question
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class AnswerService (
    private val answerRepo: AnswerRepository
) {
    fun create(question: Question, content: String) {
        val answer = Answer(
            questionId = question.id!!,
            content = content,
            createdTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        answerRepo.save(answer)
    }

    fun getList(qid: Long): List<Answer> {
        return answerRepo.findByQuestionId(qid)
    }
}