package com.sbb.sbb_kotlin.question

import java.time.LocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("QUESTIONS")
data class Question(
    @Id 
    val id: Long? = null,

    @Column("title")
    var title: String,
    @Column("content")
    var content: String,
    @Column("created_time")
    val createdTime: LocalDateTime,
    @Column("updated_at")
    var updatedAt: LocalDateTime,
    @Column("author_id")
    var authorId: Long,
    @Column("view_cnt")
    var viewCnt: Long
)