package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.DataNotFoundException
import com.sbb.sbb_kotlin.answer.AnswerJdbcRepository
import com.sbb.sbb_kotlin.user.UserInfo
import java.time.LocalDateTime
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service

@Service
class QuestionService (
    private val questionCrudRepo: QuestionCrudRepository,
    private val questionJdbcRepo: QuestionJdbcRepository,
    private val questionVoterRepo: QuestionVoterRepository,
    private val answerJdbcRepo: AnswerJdbcRepository
) {

    fun create(title: String, content: String, user: UserInfo) {
        val question = Question(
            title = title,
            content = content,
            createdTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            authorId = user.id
        )
        questionCrudRepo.save(question)
    }

    fun delete(id: Long) {
        questionCrudRepo.deleteById(id)
    }

    fun getList(page_num: Int, kw: String): Page<QuestionListEntry> {
        val sorts = listOf(Sort.Order.desc("createdTime"), Sort.Order.desc("id"))
        val pageable = PageRequest.of(page_num, 10, Sort.by(sorts))
        
        return questionJdbcRepo.findQuestionList(kw, pageable)
    }

    fun getQuestion(id: Long): Question {
        val question = questionJdbcRepo.findQuestionById(id)

        if (question != null) {
            return question
        }
        else {
            throw DataNotFoundException("question not found")
        }
    }
    
    fun getQuestionDetail(id: Long): QuestionDetail {
        val question = getQuestionDetailWithoutAnswerList(id)
        
        question.answerList = answerJdbcRepo.findAnswerListByQuestionId(id)
        
        return question
    }

    fun getQuestionDetailWithoutAnswerList(id: Long): QuestionDetail {
        val question = questionJdbcRepo.findQuestionDetailById(id)
        
        if (question != null) {
            return question
        }
        else {
            throw DataNotFoundException("question not found")
        }
    }

    fun getQuestionVoters(id: Long): QuestionVoters {
        return questionVoterRepo.findVotersByQuestionId(id)
    }

    fun modify(question: QuestionDetail, title: String, content: String) {
        val q = Question(
            id = question.id,
            title = title, 
            content = content, 
            createdTime = question.createdTime, 
            updatedAt = LocalDateTime.now(), 
            authorId = question.author.id
        )

        questionCrudRepo.save(q)
    }

    fun vote(voters: QuestionVoters, newVoter: UserInfo) {
        if (!voters.voters.contains(newVoter.id)) {
            questionVoterRepo.addNewVoter(voters.questionId, newVoter.id)
        }
    }

}