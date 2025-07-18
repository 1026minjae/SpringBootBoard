package com.sbb.sbb_kotlin.answer

import com.sbb.sbb_kotlin.DataNotFoundException
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

    fun delete(id: Long) {
        answerCrudRepo.deleteById(id)
    }

    fun getAnswerDetail(id: Long): AnswerDetail {
        val answer = answerJdbcRepo.findAnswerDetailById(id)

        if (answer != null) {
            return answer
        } else {
            throw DataNotFoundException("answer not found")
        }
    }

    fun modify(answer: AnswerDetail, content: String) {
        val a = Answer(
            id = answer.id,
            questionId = answer.questionId, 
            content = content, 
            createdTime = answer.createdTime, 
            updatedAt = LocalDateTime.now(), 
            authorId = answer.author.id
        )

        answerCrudRepo.save(a);
    }

    fun getList(qid: Long): List<AnswerDetail> {
        return answerJdbcRepo.findAnswerListByQuestionId(qid)
    }
}