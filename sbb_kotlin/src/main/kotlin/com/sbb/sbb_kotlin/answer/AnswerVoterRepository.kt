package com.sbb.sbb_kotlin.answer

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AnswerVoterRepository(
    private val jdbc: NamedParameterJdbcTemplate
) {

    fun addNewVoter(answerId: Long, voterId: Long) {
        val sql = "INSERT INTO ANSWER_VOTERS (answer_id, voter_id) VALUES (:aid, :vid)"

        val params = mapOf("aid" to answerId, "vid" to voterId)

        jdbc.update(sql, params)
    }

    fun findVotersByAnswerId(answerId: Long): AnswerVoters {
        val sql = "SELECT voter_id FROM ANSWER_VOTERS WHERE answer_id = :aid"
        
        val params = MapSqlParameterSource().addValue("aid", answerId)

        val voterList = jdbc.query(sql, params) { rs, _ -> rs.getLong("voter_id") }

        return AnswerVoters(answerId, voterList.toSet())
    }

}