package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.DataNotFoundException
import com.sbb.sbb_kotlin.answer.AnswerRepository
import java.time.LocalDateTime
import java.util.ArrayList
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service

@Service
class QuestionService (
    private val questionRepo: QuestionRepository,
    private val answerRepo: AnswerRepository
) {
    fun getQuestionWithoutAnswerList(id: Long): QuestionDTO {
        val question = questionRepo.findById(id)
        if (question.isPresent) {
            val q = question.get()
            return QuestionDTO(
                q.id!!, 
                q.title, 
                q.content, 
                q.createdTime, 
                q.updatedAt, 
                emptyList()
            )
        }
        else {
            throw DataNotFoundException("question not found")
        }
    }

    fun getQuestion(id: Long): QuestionDTO {
        val question = this.getQuestionWithoutAnswerList(id)
        question.answerList = answerRepo.findByQuestionId(id)
        return question
    }

    fun getList(page_num: Int): Page<QuestionDTO> {
        val sorts = listOf(Sort.Order.desc("createdTime"), Sort.Order.desc("id"))
        val pageable = PageRequest.of(page_num, 10, Sort.by(sorts))
        val page = questionRepo.findAll(pageable)

        val questionIds = page.content.mapNotNull { it.id }
        val answerListByQuestionId = answerRepo.findByQuestionIds(questionIds).groupBy { it.questionId }

        val questionsWithAnswerList = page.content.map { question -> 
            QuestionDTO(
                question.id!!,
                question.title,
                question.content,
                question.createdTime,
                question.updatedAt,
                answerListByQuestionId[question.id] ?: emptyList()
            )
        }

        return PageImpl(questionsWithAnswerList, page.pageable, page.totalElements)
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