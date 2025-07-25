package com.sbb.sbb_kotlin.question

import java.time.LocalDateTime

data class QuestionInfo(
    val id: Long,
    val title: String,
    val content: String,
    val createdTime: LocalDateTime,
    val updatedAt: LocalDateTime,
    val authorId: Long,
    val authorName: String,
    val viewCnt: Long,
    val categoryId: Long
)