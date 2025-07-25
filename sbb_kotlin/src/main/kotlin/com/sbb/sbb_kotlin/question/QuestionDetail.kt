package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.answer.AnswerDetail
import com.sbb.sbb_kotlin.user.UserInfo
import java.time.LocalDateTime
import org.springframework.data.domain.Page

data class QuestionDetail(
    val id: Long,
    val title: String,
    val content: String,
    val createdTime: LocalDateTime,
    val updatedAt: LocalDateTime,
    var answers: Page<AnswerDetail>?,
    var author: UserInfo,
    val numOfVoter: Long,
    val viewCnt: Long,
    val category: Category
)