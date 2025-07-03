package com.sbb.sbb_kotlin.question

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

class QuestionForm(
    @NotEmpty(message="Title is necessary")    
    @Size(max=200)
    val title: String?,
    
    @NotEmpty(message="Content is necessary")
    val content: String?
)