package com.sbb.sbb_kotlin.comment

import jakarta.validation.constraints.NotEmpty

class CommentForm(
    @field:NotEmpty(message="Content is necessary")
    var content: String?,

    var questionId: Long?
)