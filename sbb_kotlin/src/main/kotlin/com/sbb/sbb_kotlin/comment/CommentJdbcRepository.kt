package com.sbb.sbb_kotlin.comment

import com.sbb.sbb_kotlin.user.UserInfo
import com.sbb.sbb_kotlin.comment.integerToCommentType
import com.sbb.sbb_kotlin.comment.commentTypeToInteger
import java.time.LocalDateTime
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/* For reading */
@Repository
class CommentJdbcRepository(
    private val jdbc: NamedParameterJdbcTemplate
) {

    fun findCommentById(id: Long): Comment? {
        val sql = "SELECT * FROM COMMENTS WHERE id = :id"

        val params = MapSqlParameterSource().addValue("id", id)

        return jdbc.queryForObject(sql, params) { rs, _ ->
            Comment(
                id = rs.getLong("id"),
                type = rs.getLong("type"), 
                targetId = rs.getLong("target_id"),
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                authorId = rs.getLong("author_id")
            )
        }
    }

    fun findCommentDetailById(id: Long): CommentDetail? {
        val sql = """
            SELECT 
                C.id AS id,
                C.target_type AS type,
                C.target_id AS target_id,
                C.content AS content,
                C.created_time AS created_time,
                C.updated_at AS updated_at,
                U.id AS author_id,
                U.username AS author_name
            FROM COMMENTS C
                LEFT JOIN USERS U ON C.author_id = U.id
            WHERE C.id = :id
            """.trimIndent()

        val params = MapSqlParameterSource().addValue("id", id)

        return jdbc.queryForObject(sql, params) { rs, _ ->
            CommentDetail(
                id = rs.getLong("id"),
                type = rs.getLong("type"),
                targetId = rs.getLong("target_id"),
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                author = UserInfo(rs.getLong("author_id"), rs.getString("author_name"))
            )
        }
    }

    fun findCommentsByQuestionId(questionId: Long): List<CommentDetail> {        
        val sql = """
            SELECT 
                C.id AS id,
                C.target_id AS target_id,
                C.content AS content,
                C.created_time AS created_time,
                C.updated_at AS updated_at,
                U.id AS author_id,
                U.username AS author_name
            FROM COMMENTS C
                LEFT JOIN USERS U ON C.author_id = U.id
            WHERE C.target_type = 0 AND C.target_id = :qid
            """.trimIndent()

        val params = mapOf("qid" to questionId)

        return jdbc.query(sql, params) { rs, _ ->
            CommentDetail(
                id = rs.getLong("id"),
                type = commentTypeToInteger(CommentType.QuestionComment),
                targetId = rs.getLong("target_id"),
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                author = UserInfo(rs.getLong("author_id"), rs.getString("author_name"))
            )
        }
    }

    fun findCommentsByAnswerId(answerId: Long): List<CommentDetail> {        
        val sql = """
            SELECT 
                C.id AS id,
                C.target_id AS target_id,
                C.content AS content,
                C.created_time AS created_time,
                C.updated_at AS updated_at,
                U.id AS author_id,
                U.username AS author_name
            FROM COMMENTS C
                LEFT JOIN USERS U ON C.author_id = U.id
            WHERE C.target_type = 1 AND C.target_id = :aid
            """.trimIndent()

        val params = mapOf("aid" to answerId)

        return jdbc.query(sql, params) { rs, _ ->
            CommentDetail(
                id = rs.getLong("id"),
                type = commentTypeToInteger(CommentType.AnswerComment),
                targetId = rs.getLong("target_id"),
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                author = UserInfo(rs.getLong("author_id"), rs.getString("author_name"))
            )
        }
    }

}