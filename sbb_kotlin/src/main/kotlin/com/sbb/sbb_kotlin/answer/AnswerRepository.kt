package com.sbb.sbb_kotlin.answer

import com.sbb.sbb_kotlin.answer.Answer
import java.util.Optional
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/* For simple sql actions */
interface AnswerRepository : CrudRepository<Answer, Long> {
    fun findByQuestionId(question_id: Long): List<Answer>

    @Query("SELECT * FROM ANSWERS WHERE question_id IN (:questionIds)")
    fun findByQuestionIds(@Param("questionIds") questionIds: List<Long>): List<Answer>
}