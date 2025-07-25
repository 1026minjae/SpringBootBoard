package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.answer.AnswerDetail
import com.sbb.sbb_kotlin.user.UserInfo
import java.time.LocalDateTime

data class QuestionDetail(
    val id: Long,
    val title: String,
    val content: String,
    val createdTime: LocalDateTime,
    val updatedAt: LocalDateTime,
    var answerList: List<AnswerDetail>,
    var author: UserInfo,
    val numOfVoter: Long,
    val viewCnt: Long,
    val category: Category
)