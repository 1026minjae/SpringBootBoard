package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.answer.Answer
import java.time.LocalDateTime

data class QuestionDTO(
    val id: Long,
    var title: String,
    var content: String,
    val createdTime: LocalDateTime,
    var updatedAt: LocalDateTime,
    var answerList: List<Answer>
)