package com.sbb.sbb_kotlin.question

import com.sbb.sbb_kotlin.DataNotFoundException
import com.sbb.sbb_kotlin.answer.AnswerJdbcRepository
import com.sbb.sbb_kotlin.user.UserInfo
import java.sql.Timestamp
import java.time.LocalDateTime
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QuestionService (
    private val questionCrudRepo: QuestionCrudRepository,
    private val questionJdbcRepo: QuestionJdbcRepository,
    private val questionVoterRepo: QuestionVoterRepository,
    private val answerJdbcRepo: AnswerJdbcRepository
) {

    @Transactional
    fun create(title: String, content: String, user: UserInfo, categoryId: Long) {
        val question = Question(
            title = title,
            content = content,
            createdTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            authorId = user.id,
            viewCnt = 0,
            categoryId = categoryId
        )
        questionCrudRepo.save(question)
    }

    @Transactional
    fun delete(id: Long) {
        questionCrudRepo.deleteById(id)
    }

    fun getList(page_num: Int, kw: String, category: Int): Page<QuestionListEntry> {
        val sorts = listOf(Sort.Order.desc("createdTime"), Sort.Order.desc("id"))
        val pageable = PageRequest.of(page_num, 10, Sort.by(sorts))
        
        return questionJdbcRepo.findQuestionList(kw, pageable, category)
    }

    @Transactional
    fun getQuestionDetail(id: Long): QuestionDetail {
        val question = questionJdbcRepo.findQuestionDetailById(id)
        
        if (question != null) {
            question.answerList = answerJdbcRepo.findAnswerListByQuestionId(id)
            return question
        }
        else {
            throw DataNotFoundException("question not found")
        }
    }

    fun getQuestionInfo(id: Long): QuestionInfo {
        val question = questionJdbcRepo.findQuestionInfoById(id)

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

    @Transactional
    fun modify(id: Long, title: String, content: String, categoryId: Long) {
        questionJdbcRepo.modifyTitleContentUpdatedAtCategoryId(
            id = id,
            title = title, 
            content = content, 
            updatedAt = Timestamp.valueOf(LocalDateTime.now()),
            categoryId = categoryId
        )
    }

    @Transactional
    fun vote(voters: QuestionVoters, newVoter: UserInfo) {
        if (!voters.voters.contains(newVoter.id)) {
            questionVoterRepo.addNewVoter(voters.questionId, newVoter.id)
        }
    }

}