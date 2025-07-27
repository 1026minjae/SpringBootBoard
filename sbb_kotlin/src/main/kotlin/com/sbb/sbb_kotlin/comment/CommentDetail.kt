package com.sbb.sbb_kotlin.comment

import com.sbb.sbb_kotlin.user.UserInfo
import java.time.LocalDateTime

data class CommentDetail(
    val id: Long,
    val type: Long,
    val targetId: Long,
    val content: String,
    val createdTime: LocalDateTime,
    val updatedAt: LocalDateTime,
    var author: UserInfo
)