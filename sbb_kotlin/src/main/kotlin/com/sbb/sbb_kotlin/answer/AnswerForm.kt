package com.sbb.sbb_kotlin.answer

import jakarta.validation.constraints.NotEmpty

class AnswerForm(
    @NotEmpty(message="Content is necessary")
    val content: String?
)