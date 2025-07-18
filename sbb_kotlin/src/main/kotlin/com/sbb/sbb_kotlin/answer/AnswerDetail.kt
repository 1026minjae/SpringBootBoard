package com.sbb.sbb_kotlin.answer

import com.sbb.sbb_kotlin.user.UserInfo
import java.time.LocalDateTime

data class AnswerDetail(
    val id: Long,
    val questionId: Long,
    val content: String,
    val createdTime: LocalDateTime,
    val author: UserInfo
)