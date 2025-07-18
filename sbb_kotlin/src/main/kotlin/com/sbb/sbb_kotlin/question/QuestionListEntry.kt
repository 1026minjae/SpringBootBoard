package com.sbb.sbb_kotlin.question

import java.time.LocalDateTime

data class QuestionListEntry(
    val id: Long,
    val title: String,
    val createdTime: LocalDateTime,
    var numOfAnswer: Long,
    var authorName: String
)