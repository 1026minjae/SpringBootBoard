package com.sbb.sbb_kotlin.answer

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.jdbc.repository.query.Query

import java.util.Optional

import com.sbb.sbb_kotlin.answer.Answer

/* For simple sql actions */
interface AnswerRepository : CrudRepository<Answer, Long> {
    fun findByQuestionId(question_id: Long): List<Answer>
}