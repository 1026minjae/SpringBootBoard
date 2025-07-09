package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.DataNotFoundException
import java.time.LocalDateTime
import java.util.ArrayList
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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

    fun getList(page: Int): Page<Question> {
        val sorts = listOf(Sort.Order.desc("createdTime"))
        val pageable = PageRequest.of(page, 10, Sort.by(sorts))
        return questionRepo.findAll(pageable)
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