package com.sbb.sbb_kotlin.question

data class QuestionVoters (
    val questionId: Long,
    val voters: Set<Long>
)