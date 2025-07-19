package com.sbb.sbb_kotlin.question

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class QuestionVoterRepository(
    private val jdbc: NamedParameterJdbcTemplate
) {

    fun addNewVoter(questionId: Long, voterId: Long) {
        val sql = "INSERT INTO QUESTION_VOTERS (question_id, voter_id) VALUES (:qid, :vid)"

        val params = mapOf("qid" to questionId, "vid" to voterId)

        jdbc.update(sql, params)
    }

    fun findVotersByQuestionId(questionId: Long): QuestionVoters {
        val sql = "SELECT voter_id FROM QUESTION_VOTERS WHERE question_id = :qid"
        
        val params = MapSqlParameterSource().addValue("qid", questionId)

        val voterList = jdbc.query(sql, params) { rs, _ -> rs.getLong("voter_id") }

        return QuestionVoters(questionId, voterList.toSet())
    }

}