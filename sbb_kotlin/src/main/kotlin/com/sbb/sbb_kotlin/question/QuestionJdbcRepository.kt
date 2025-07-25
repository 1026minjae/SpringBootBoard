package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.user.UserInfo
import java.sql.Timestamp
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageImpl
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class QuestionJdbcRepository(
    private val jdbc: NamedParameterJdbcTemplate
) {
    fun findQuestionDetailById(id: Long): QuestionDetail? {
        val readSql = """
            SELECT
                Q.id AS id,
                Q.title AS title,
                Q.content AS content,
                Q.created_time AS created_time,
                Q.updated_at AS updated_at,
                U.id AS author_id,
                U.username AS author_name,
                COUNT(V.voter_id) AS num_of_voter,
                Q.view_cnt AS view_cnt,
                C.id AS category_id,
                C.label AS category_label
            FROM QUESTIONS Q 
                INNER JOIN USERS U ON Q.author_id = U.id
                LEFT JOIN QUESTION_VOTERS V ON Q.id = V.question_id
                INNER JOIN CATEGORIES C ON Q.category_id = C.id
            WHERE Q.id = :id
            GROUP BY Q.id, Q.title, Q.content, Q.created_time, Q.updated_at, U.id, U.username, Q.view_cnt, C.id, C.label
            """.trimIndent()

        val viewCntUpdateSql = "UPDATE QUESTIONS SET view_cnt = view_cnt + 1 WHERE id = :id"
        
        val params = MapSqlParameterSource().addValue("id", id)

        jdbc.update(viewCntUpdateSql, params)

        return jdbc.queryForObject(readSql, params) { rs, _ ->
            QuestionDetail(
                id = rs.getLong("id"),
                title = rs.getString("title"), 
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                answerList = emptyList(),
                author = UserInfo(rs.getLong("author_id"), rs.getString("author_name")),
                numOfVoter = rs.getLong("num_of_voter"),
                viewCnt = rs.getLong("view_cnt"),
                category = Category(rs.getLong("category_id"), rs.getString("category_label"))
            )
        }
    }

    fun findQuestionInfoById(id: Long): QuestionInfo? {
        val sql = """
            SELECT
                Q.id AS id,
                Q.title AS title,
                Q.content AS content,
                Q.created_time AS created_time,
                Q.updated_at AS updated_at,
                U.id AS author_id,
                U.username AS author_name,
                Q.view_cnt AS view_cnt,
                C.id AS category_id
            FROM QUESTIONS Q 
                INNER JOIN USERS U ON Q.author_id = U.id
                INNER JOIN CATEGORIES C ON Q.category_id = C.id
            WHERE Q.id = :id
            """.trimIndent()
        
        val params = MapSqlParameterSource().addValue("id", id)

        return jdbc.queryForObject(sql, params) { rs, _ ->
            QuestionInfo(
                id = rs.getLong("id"),
                title = rs.getString("title"), 
                content = rs.getString("content"), 
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(), 
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                authorId = rs.getLong("author_id"), 
                authorName = rs.getString("author_name"),
                viewCnt = rs.getLong("view_cnt"),
                categoryId = rs.getLong("category_id")
            )
        }
    }

    fun findQuestionList(keyword: String, pageable: Pageable, category: Int): Page<QuestionListEntry> {
        val limit = pageable.pageSize
        val offset = pageable.pageNumber * pageable.pageSize

        val countQuery = """
            SELECT 
                COUNT(DISTINCT Q.id) 
            FROM QUESTIONS Q
                INNER JOIN USERS U1 ON Q.author_id = U1.id
                LEFT JOIN ANSWERS A ON Q.id = A.question_id
                LEFT JOIN USERS U2 ON A.author_id = U2.id
            WHERE Q.category_id = :categoryId
                AND (Q.title LIKE '%${keyword}%'
                OR Q.content LIKE '%${keyword}%'
                OR U1.username LIKE '%${keyword}%'
                OR A.content LIKE '%${keyword}%'
                OR U2.username LIKE '%${keyword}%')
            """.trimIndent()
        
        val dataQuery = """
            SELECT 
                Q.id AS id, 
                Q.title AS title,
                Q.created_time AS created_time,
                COUNT(A.id) AS num_of_answer,
                U1.username AS author_name,
                Q.view_cnt AS view_cnt
            FROM QUESTIONS Q
                INNER JOIN USERS U1 ON Q.author_id = U1.id
                LEFT JOIN ANSWERS A ON Q.id = A.question_id
                LEFT JOIN USERS U2 ON A.author_id = U2.id
            WHERE Q.category_id = :categoryId
                AND (Q.title LIKE '%${keyword}%'
                OR Q.content LIKE '%${keyword}%'
                OR U1.username LIKE '%${keyword}%'
                OR A.content LIKE '%${keyword}%'
                OR U2.username LIKE '%${keyword}%')
            GROUP BY Q.id, Q.title, Q.created_time, U1.username, Q.view_cnt
            ORDER BY Q.created_time DESC LIMIT :limit OFFSET :offset
            """.trimIndent()

        val params = mapOf("limit" to limit, "offset" to offset, "categoryId" to category)

        val total = jdbc.queryForObject(countQuery, mapOf("categoryId" to category), Long::class.java) ?: 0
        val content = jdbc.query(dataQuery, params) { rs, _ ->
            QuestionListEntry(
                id = rs.getLong("id"),
                title = rs.getString("title"),
                createdTime = rs.getTimestamp("created_time").toLocalDateTime(),
                numOfAnswer = rs.getLong("num_of_answer"),
                authorName = rs.getString("author_name"),
                viewCnt = rs.getLong("view_cnt")
            )
        }

        return PageImpl(content, pageable, total)
    }

    fun modifyTitleContentUpdatedAtCategoryId(id: Long, title: String, content: String, updatedAt: Timestamp, categoryId: Long) {
        val sql = "UPDATE QUESTIONS SET title = :title, content = :content, updated_at = :updatedAt, category_id = :categoryId WHERE id = :id"

        val params = mapOf("id" to id, "title" to title, "content" to content, "updatedAt" to updatedAt, "categoryId" to categoryId)

        jdbc.update(sql, params)
    }

}