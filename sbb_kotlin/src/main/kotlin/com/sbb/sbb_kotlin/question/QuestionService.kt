package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.DataNotFoundException
import java.time.LocalDateTime
import org.springframework.stereotype.Service

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

    fun create(title: String, content: String) {
        val question = Question(
            title = title,
            content = content,
            createdTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        questionRepo.save(question)
    }
}