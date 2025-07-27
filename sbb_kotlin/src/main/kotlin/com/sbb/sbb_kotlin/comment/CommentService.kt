package com.sbb.sbb_kotlin.comment

import com.sbb.sbb_kotlin.DataNotFoundException
import com.sbb.sbb_kotlin.user.UserInfo
import com.sbb.sbb_kotlin.comment.integerToCommentType
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class CommentService (
    private val commentCrudRepo: CommentCrudRepository,
    private val commentJdbcRepo: CommentJdbcRepository
) {
    fun createQuestionComment(questionId: Long, content: String, author: UserInfo): Comment {
        val comment = Comment(
            type = commentTypeToInteger(CommentType.QuestionComment),
            targetId = questionId,
            content = content,
            createdTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            authorId = author.id
        )

        val saved_comment = commentCrudRepo.save(comment)

        return saved_comment
    }

    fun createAnswerComment(answerId: Long, content: String, author: UserInfo): Comment {
        val comment = Comment(
            type = commentTypeToInteger(CommentType.AnswerComment),
            targetId = answerId,
            content = content,
            createdTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            authorId = author.id
        )

        val saved_comment = commentCrudRepo.save(comment)

        return saved_comment
    }

    fun delete(id: Long) {
        commentCrudRepo.deleteById(id)
    }

    fun getComment(id: Long): Comment {
        val comment = commentJdbcRepo.findCommentById(id)

        if (comment != null) {
            return comment
        } else {
            throw DataNotFoundException("comment not found")
        }
    }

    fun getCommentDetail(id: Long): CommentDetail {
        val comment = commentJdbcRepo.findCommentDetailById(id)

        if (comment != null) {
            return comment
        } else {
            throw DataNotFoundException("comment not found")
        }
    }

    fun getAnswerComments(answerId: Long): List<CommentDetail> {
        return commentJdbcRepo.findCommentsByAnswerId(answerId)
    }

    fun getQuestionComments(questionId: Long): List<CommentDetail> {
        return commentJdbcRepo.findCommentsByQuestionId(questionId)
    }

    fun modify(comment: CommentDetail, content: String) {
        val c = Comment(
            id = comment.id,
            type = comment.type,
            targetId = comment.targetId, 
            content = content, 
            createdTime = comment.createdTime, 
            updatedAt = LocalDateTime.now(), 
            authorId = comment.author.id
        )

        commentCrudRepo.save(c);
    }

}