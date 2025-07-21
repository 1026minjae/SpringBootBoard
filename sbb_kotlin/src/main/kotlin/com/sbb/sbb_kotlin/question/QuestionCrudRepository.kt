package com.sbb.sbb_kotlin.question

import java.sql.Timestamp
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/* For writing */
interface QuestionCrudRepository : CrudRepository<Question, Long> {
    @Query("UPDATE QUESTIONS SET title = :title, content = :content, updated_at = :updatedAt WHERE id = :id")
    fun modifyTitleAndContentAndUpdatedAt(
        @Param("id") id: Long, 
        @Param("title") title: String,
        @Param("content") content: String,
        @Param("updatedAt") updatedAt: Timestamp
    )
}