package com.sbb.sbb_kotlin.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.relational.core.mapping.Column

import java.time.LocalDateTime

@Table("ANSWERS")
data class Answer(
    @Id
    val id: Long? = null,
    
    @Column("question_id")
    val questionId: Long,
    @Column("content")
    var content: String,
    @Column("created_time")
    val createdTime: LocalDateTime,
    @Column("updated_at")
    var updatedAt: LocalDateTime

)