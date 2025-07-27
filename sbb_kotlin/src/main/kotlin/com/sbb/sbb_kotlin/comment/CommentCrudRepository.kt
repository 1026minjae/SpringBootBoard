package com.sbb.sbb_kotlin.comment

// import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
// import org.springframework.data.repository.query.Param

/* For writing */
interface CommentCrudRepository : CrudRepository<Comment, Long> {

}