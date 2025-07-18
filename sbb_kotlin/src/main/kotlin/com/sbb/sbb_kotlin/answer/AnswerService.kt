package com.sbb.sbb_kotlin.answer

import com.sbb.sbb_kotlin.user.UserInfo
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class AnswerService (
    private val answerCrudRepo: AnswerCrudRepository,
    private val answerJdbcRepo: AnswerJdbcRepository
) {
    fun create(questionId: Long, content: String, author: UserInfo) {
        val answer = Answer(
            questionId = questionId,
            content = content,
            createdTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            authorId = author.id
        )
        answerCrudRepo.save(answer)
    }

    fun getList(qid: Long): List<AnswerDetail> {
        return answerJdbcRepo.findAnswerListByQuestionId(qid)
    }
}