package com.sbb.sbb_kotlin.question

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

class QuestionForm(
    @field:NotEmpty(message="Title is necessary")    
    @field:Size(max=200)
    val title: String?,
    
    @field:NotEmpty(message="Content is necessary")
    val content: String?
)