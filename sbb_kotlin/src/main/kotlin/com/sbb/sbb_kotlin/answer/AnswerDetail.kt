package com.sbb.sbb_kotlin.answer

import com.sbb.sbb_kotlin.user.UserInfo
import java.time.LocalDateTime

data class AnswerDetail(
    val id: Long,
    val questionId: Long,
    var content: String,
    val createdTime: LocalDateTime,
    val updatedAt: LocalDateTime,
    val author: UserInfo,
    val numOfVoter: Long
)