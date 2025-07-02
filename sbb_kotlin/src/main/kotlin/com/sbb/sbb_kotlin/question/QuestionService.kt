package com.sbb.sbb_kotlin.question

import org.springframework.stereotype.Service

@Service
class QuestionService (
    private val questionRepo: QuestionRepository
) {
    fun getList(): Iterable<Question> {
        return questionRepo.findAll()
    }
}