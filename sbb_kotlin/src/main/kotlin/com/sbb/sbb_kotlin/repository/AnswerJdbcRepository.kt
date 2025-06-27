package com.sbb.sbb_kotlin.repository

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Repository

import java.time.LocalDateTime

import com.sbb.sbb_kotlin.entity.Answer

/* This class is for complicated sql actions */
@Repository
class AnswerJdbcRepository(
    private val jdbc: NamedParameterJdbcTemplate
) {
    fun addNewAnswer(questionId: Long, content: String) {
        val sql = "INSERT INTO ANSWERS (question_id, content, created_time, updated_at) VALUES (:qid, :content, :createdTime, :updatedAt)"
        val params = mapOf("qid" to questionId, "content" to content, "createdTime" to LocalDateTime.now(), "updatedAt" to LocalDateTime.now())

        jdbc.update(sql, params)
    }

    fun findAnswerById(id: Long): Answer? {
        val sql = "SELECT * FROM ANSWERS WHERE id = :id"
        val params = MapSqlParameterSource().addValue("id", id)

        return jdbc.queryForObject(sql, params) { rs, _ ->
            Answer(
                id = rs.getLong("id"),
                questionId = rs.getLong("question_id"), 
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime()
            )
        }
    }

    fun findAnswersByQuestionId(questionId: Long): List<Answer> {
        val sql = "SELECT * FROM ANSWERS WHERE question_id = :qid"
        val params = MapSqlParameterSource().addValue("qid", questionId)

        return jdbc.query(sql, params) { rs, _ ->
            Answer(
                id = rs.getLong("id"),
                questionId = rs.getLong("question_id"), 
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime()
            )
        }
    }
}