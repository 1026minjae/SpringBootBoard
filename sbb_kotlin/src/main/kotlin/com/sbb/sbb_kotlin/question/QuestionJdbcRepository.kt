package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.question.Question
import java.time.LocalDateTime
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/* This class is for complicated sql actions */
@Repository
class QuestionJdbcRepository(
    private val jdbc: NamedParameterJdbcTemplate
) {
    fun addNewQuestion(title: String, content: String) {
        val sql = "INSERT INTO QUESTIONS (title, content, created_time, updated_at) VALUES (:title, :content, :createdTime, :updatedAt)"
        val params = mapOf("title" to title, "content" to content, "createdTime" to LocalDateTime.now(), "updatedAt" to LocalDateTime.now())

        jdbc.update(sql, params)
    }

    fun findQuestionById(id: Long): Question? {
        val sql = "SELECT * FROM QUESTIONS WHERE id = :id"
        val params = MapSqlParameterSource().addValue("id", id)

        return jdbc.queryForObject(sql, params) { rs, _ ->
            Question(
                id = rs.getLong("id"),
                title = rs.getString("title"), 
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime()
            )
        }
    }
}