package com.sbb.sbb_kotlin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired

import com.sbb.sbb_kotlin.entity.Question
import com.sbb.sbb_kotlin.entity.Answer

import com.sbb.sbb_kotlin.repository.QuestionRepository
import com.sbb.sbb_kotlin.repository.AnswerRepository
import com.sbb.sbb_kotlin.repository.QuestionJdbcRepository
import com.sbb.sbb_kotlin.repository.AnswerJdbcRepository


import java.time.LocalDateTime
import java.time.LocalDate

@SpringBootTest
class SbbKotlinApplicationTests {

	@Autowired
    lateinit var questionRepo: QuestionRepository

    @Test
    fun testQuestionRepository() {
        // 1. check if adding new question is successful
        val q0 = Question(
            title = "What is sbb?",
            content = "I want to know sbb",
            createdTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val saved_q0 = this.questionRepo.save(q0)
        assertNotNull(saved_q0.id, "C1: id is null")

        val id = saved_q0.id!!

        // 2. check if searching a question by id is successful
        val foundById = this.questionRepo.findById(id)
        assertTrue(foundById.isPresent, "C2: question cannot be searched by id")

        val q1: Question = foundById.get()    
        assertEquals("What is sbb?", q1.title, "C2: title is wrong")
        assertEquals("I want to know sbb", q1.content, "C2: content is wrong")

        // 3. check if updating a question is successful
        q1.title = "modified title"
        q1.content = "modified content"
        q1.updatedAt = LocalDateTime.now()
        
        val saved_q1 = this.questionRepo.save(q1)
        assertEquals(id, saved_q1.id, "C3: id is wrong")
        assertEquals("modified title", saved_q1.title, "C3: title is wrong")
        assertEquals("modified content", saved_q1.content, "C3: content is wrong")

        // 4. check if searching a question by keyword
        val foundByKeyword: List<Question> = this.questionRepo.findByKeyword("%modified%")
        assertEquals(1, foundByKeyword.size, "C4: list size is wrong")

        val q2 = foundByKeyword.get(0)
        assertEquals(id, q2.id, "C4: id is wrong")
        assertEquals("modified title", q2.title, "C4: title is wrong")
        assertEquals("modified content", q2.content, "C4: content is wrong")

        // 5. check if deleting the question is successful
        this.questionRepo.deleteById(id)
        
        val removed = this.questionRepo.findById(id)
        assertFalse(removed.isPresent)
    }

    @Autowired
    lateinit var answerRepo: AnswerRepository

    @Test
    fun testAnswerRepository() {
        // 1. check if adding new answer is successful
        val q = Question(
            title = "What is sbb?",
            content = "I want to know sbb",
            createdTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val saved_q = this.questionRepo.save(q)
        assertNotNull(saved_q.id, "C1: failed to add a question")

        val qid = saved_q.id!!

        val a0 = Answer(
            questionId = qid,
            content = "Sbb is Springboot board",
            createdTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val saved_a0 = this.answerRepo.save(a0)
        assertNotNull(saved_a0.id, "C1: failed to add an answer")

        val id = saved_a0.id!!

        // 2. check if searching a question by id is successful
        val foundById = this.answerRepo.findById(id)
        assertTrue(foundById.isPresent, "C2: answer cannot be searched by id")

        val a1 = foundById.get()
        assertEquals(qid, a1.questionId, "C2: question id is wrong")
        assertEquals("Sbb is Springboot board", a1.content, "C2: content is wrong")

        // 3. check if updating an answer is successful
        a1.content = "modified content"
        a1.updatedAt = LocalDateTime.now()
        
        val saved_a1 = this.answerRepo.save(a1)
        assertEquals(id, saved_a1.id, "C3: id is wrong")
        assertEquals(qid, saved_a1.questionId, "C3: question id is wrong")
        assertEquals("modified content", saved_a1.content, "C3: content is wrong")

        // 4. check if searching an answer by question id
        val foundByQuestionId = this.answerRepo.findByQuestionId(qid)
        assertEquals(1, foundByQuestionId.size, "C4: list size is wrong")

        val a2 = foundByQuestionId.get(0)
        assertEquals(id, a2.id, "C4: id is wrong")
        assertEquals(qid, a2.questionId, "C4: question id is wrong")
        assertEquals("modified content", a2.content, "C4: content is wrong")

        // 5. check if deleting the answer is successful
        this.answerRepo.deleteById(id)
        
        val removed = this.answerRepo.findById(id)
        assertFalse(removed.isPresent)
    }

}
