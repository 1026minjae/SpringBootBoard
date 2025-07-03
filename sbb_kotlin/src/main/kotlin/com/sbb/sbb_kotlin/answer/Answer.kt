package com.sbb.sbb_kotlin.answer

import java.time.LocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

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