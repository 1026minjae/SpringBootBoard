package com.sbb.sbb_kotlin.answer

data class AnswerVoters(
    val answerId: Long,
    val voters: Set<Long>
)