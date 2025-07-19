package com.sbb.sbb_kotlin.answer

import com.sbb.sbb_kotlin.answer.AnswerDetail
import com.sbb.sbb_kotlin.user.UserInfo
import java.time.LocalDateTime
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/* For reading */
@Repository
class AnswerJdbcRepository(
    private val jdbc: NamedParameterJdbcTemplate
) {

    fun findAnswerDetailById(id: Long): AnswerDetail? {
        val sql = """
            SELECT 
                A.id AS id,
                A.question_id AS question_id,
                A.content AS content,
                A.created_time AS created_time,
                A.updated_at AS updated_at,
                U.id AS author_id,
                U.username AS author_name,
                COUNT(V.voter_id) AS num_of_voter
            FROM ANSWERS A
                INNER JOIN USERS U ON A.author_id = U.id
                LEFT JOIN ANSWER_VOTERS V ON A.id = V.answer_id
            WHERE A.id = :id
            GROUP BY A.id, A.question_id, A.content, A.created_time, A.updated_at, U.id, U.username
            """.trimIndent()

        val params = MapSqlParameterSource().addValue("id", id)

        return jdbc.queryForObject(sql, params) { rs, _ ->
            AnswerDetail(
                id = rs.getLong("id"),
                questionId = rs.getLong("question_id"), 
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                author = UserInfo(rs.getLong("author_id"), rs.getString("author_name")),
                numOfVoter = rs.getLong("num_of_voter")
            )
        }
    }

    fun findAnswerListByQuestionId(questionId: Long): List<AnswerDetail> {
        val sql = """
            SELECT 
                A.id AS id,
                A.question_id AS question_id,
                A.content AS content,
                A.created_time AS created_time,
                A.updated_at AS updated_at,
                U.id AS author_id,
                U.username AS author_name,
                COUNT(V.voter_id) AS num_of_voter
            FROM ANSWERS A
                INNER JOIN USERS U ON A.author_id = U.id
                LEFT JOIN ANSWER_VOTERS V ON A.id = V.answer_id
            WHERE A.question_id = :qid
            GROUP BY A.id, A.question_id, A.content, A.created_time, A.updated_at, U.id, U.username
            """.trimIndent()

        val params = MapSqlParameterSource().addValue("qid", questionId)

        return jdbc.query(sql, params) { rs, _ ->
            AnswerDetail(
                id = rs.getLong("id"),
                questionId = rs.getLong("question_id"), 
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                author = UserInfo(rs.getLong("author_id"), rs.getString("author_name")),
                numOfVoter = rs.getLong("num_of_voter")
            )
        }
    }

    fun findQuestionIdById(id: Long): Long? {
        val sql = "SELECT question_id FROM ANSWERS WHERE id = :id"

        val params = MapSqlParameterSource().addValue("id", id)

        return jdbc.queryForObject(sql, params) { rs, _ -> rs.getLong("question_id") }
    }

}