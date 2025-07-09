package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.question.Question
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/* For simple sql actions */
interface QuestionRepository : CrudRepository<Question, Long> {
    @Query("SELECT * FROM QUESTIONS WHERE title LIKE :keyword OR content LIKE :keyword")
    fun findByKeyword(@Param("keyword") keyword: String): List<Question>

    fun findAll(pageable: Pageable): Page<Question>
}