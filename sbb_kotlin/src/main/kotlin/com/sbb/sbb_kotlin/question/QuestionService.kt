package com.sbb.sbb_kotlin.question

import org.springframework.stereotype.Service

import com.sbb.sbb_kotlin.DataNotFoundException

@Service
class QuestionService (
    private val questionRepo: QuestionRepository
) {
    fun getQuestion(id: Long): Question {
        val question = questionRepo.findById(id)
        if (question.isPresent) {
            return question.get()
        }
        else {
            throw DataNotFoundException("question not found")
        }
    }

    fun getList(): Iterable<Question> {
        return questionRepo.findAll()
    }
}