package com.sbb.sbb_kotlin.question

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

class QuestionForm(
    @field:NotEmpty(message="Title is necessary")    
    @field:Size(max=200)
    var title: String?,
    
    @field:NotEmpty(message="Content is necessary")
    var content: String?
)