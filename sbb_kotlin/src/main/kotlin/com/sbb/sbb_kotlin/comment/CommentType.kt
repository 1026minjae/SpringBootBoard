package com.sbb.sbb_kotlin.comment

enum class CommentType {
    QuestionComment,
    AnswerComment,
    Undefined
}

fun integerToCommentType(i: Long): CommentType = when (i) {
    0L -> CommentType.QuestionComment
    1L -> CommentType.AnswerComment
    else -> CommentType.Undefined
}

fun commentTypeToInteger(c: CommentType): Long = when (c) {
    CommentType.QuestionComment -> 0L
    CommentType.AnswerComment -> 1L
    else -> 2L
}