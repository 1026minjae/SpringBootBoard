package com.sbb.sbb_kotlin.question

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("QUESTIONS")
data class Category(
    @Id
    val id: Long,
    @Column("label")
    val label: String
)