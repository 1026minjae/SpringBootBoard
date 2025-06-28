package com.sbb.sbb_kotlin.question

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.jdbc.repository.query.Query

import com.sbb.sbb_kotlin.question.Question

/* For simple sql actions */
interface QuestionRepository : CrudRepository<Question, Long> {
    @Query("SELECT * FROM QUESTIONS WHERE title LIKE :keyword OR content LIKE :keyword")
    fun findByKeyword(@Param("keyword") keyword: String): List<Question>
}