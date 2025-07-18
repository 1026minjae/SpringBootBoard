package com.sbb.sbb_kotlin.question

// import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
// import org.springframework.data.repository.query.Param

/* For writing */
interface QuestionCrudRepository : CrudRepository<Question, Long> {
    
}