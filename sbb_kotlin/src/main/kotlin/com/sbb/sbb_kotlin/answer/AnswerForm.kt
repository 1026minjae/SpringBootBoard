package com.sbb.sbb_kotlin.answer

import jakarta.validation.constraints.NotEmpty

class AnswerForm(
    @field:NotEmpty(message="Content is necessary")
    var content: String?
)