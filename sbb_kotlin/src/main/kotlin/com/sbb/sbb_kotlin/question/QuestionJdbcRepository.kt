package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.user.UserInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageImpl
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/* For reading */
@Repository
class QuestionJdbcRepository(
    private val jdbc: NamedParameterJdbcTemplate
) {

    fun checkIfGivenQuestionExists(id: Long): Boolean {
        val sql = "SELECT COUNT(*) FROM QUESTIONS WHERE id = :id"
        val params = MapSqlParameterSource().addValue("id", id)
        val result = jdbc.queryForObject(sql, params, Long::class.java) ?: 0

        return (result > 0)
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
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                authorId = rs.getLong("author_id")
            )
        }
    }

    fun findQuestionDetailById(id: Long): QuestionDetail? {
        val sql = """
            SELECT
                Q.id AS id,
                Q.title AS title,
                Q.content AS content,
                Q.created_time AS created_time,
                Q.updated_at AS updated_at,
                U.id AS author_id,
                U.username AS author_name,
                COUNT(V.voter_id) AS num_of_voter
            FROM QUESTIONS Q 
                INNER JOIN USERS U ON Q.author_id = U.id
                LEFT JOIN QUESTION_VOTERS V ON Q.id = V.question_id
            WHERE Q.id = :id
            GROUP BY Q.id, Q.title, Q.content, Q.created_time, Q.updated_at, U.id, U.username
            """.trimIndent()
        
        val params = MapSqlParameterSource().addValue("id", id)

        return jdbc.queryForObject(sql, params) { rs, _ ->
            QuestionDetail(
                id = rs.getLong("id"),
                title = rs.getString("title"), 
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                answerList = emptyList(),
                author = UserInfo(rs.getLong("author_id"), rs.getString("author_name")),
                numOfVoter = rs.getLong("num_of_voter")
            )
        }
    }

    fun findQuestionList(pageable: Pageable): Page<QuestionListEntry> {
        val limit = pageable.pageSize
        val offset = pageable.pageNumber * pageable.pageSize

        val countQuery = "SELECT COUNT(*) FROM QUESTIONS INNER JOIN USERS"
        val dataQuery = """
            SELECT 
                Q.id AS id, 
                Q.title AS title,
                Q.created_time AS created_time,
                COUNT(A.id) AS num_of_answer,
                U.username AS author_name
            FROM QUESTIONS Q
                INNER JOIN USERS U ON Q.author_id = U.id
                LEFT JOIN ANSWERS A ON Q.id = A.question_id
            GROUP BY Q.id, Q.title, Q.created_time, U.username
            ORDER BY Q.created_time DESC LIMIT :limit OFFSET :offset
            """.trimIndent()

        val params = mapOf("limit" to limit, "offset" to offset)

        val total = jdbc.queryForObject(countQuery, emptyMap<String, Any>(), Long::class.java) ?: 0
        val content = jdbc.query(dataQuery, params) { rs, _ ->
            QuestionListEntry(
                id = rs.getLong("id"),
                title = rs.getString("title"),
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(),
                numOfAnswer = rs.getLong("num_of_answer"),
                authorName = rs.getString("author_name")
            )
        }

        return PageImpl(content, pageable, total)
    }

}