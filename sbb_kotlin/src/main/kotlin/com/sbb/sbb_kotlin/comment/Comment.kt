package com.sbb.sbb_kotlin.comment

import java.time.LocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("COMMENTS")
data class Comment(
    @Id 
    val id: Long? = null,

    @Column("target_type")
    var type: Long,
    @Column("target_id")
    var targetId: Long,
    @Column("author_id")
    var authorId: Long,
    @Column("content")
    var content: String,
    @Column("created_time")
    val createdTime: LocalDateTime,
    @Column("updated_at")
    var updatedAt: LocalDateTime
)