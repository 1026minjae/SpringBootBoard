package com.sbb.sbb_kotlin.comment

import com.sbb.sbb_kotlin.question.QuestionService
import com.sbb.sbb_kotlin.user.UserService
import jakarta.validation.Valid
import java.security.Principal
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.server.ResponseStatusException

@RequestMapping("/comment")
@Controller
class CommentController (
    private val questionService: QuestionService,
    private val commentService: CommentService,
    private val userService: UserService
) {

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    fun createComment(
        model: Model, 
        @RequestParam(value="type") type: Long,
        @RequestParam(value="target") targetId: Long, 
        @RequestParam(value="content") content: String,
        @RequestParam(value="qid") questionId: Long,
        @RequestParam(value="page") page: Int,
        principal: Principal
    ): String {
        val commentType = integerToCommentType(type)

        if (commentType == CommentType.Undefined) {
            val question = questionService.getQuestionDetail(questionId, page)
            model.addAttribute("question", question)
            return "question_detail"
        }

        val user = userService.getUser(principal.getName())

        if (commentType == CommentType.QuestionComment) {
            commentService.createQuestionComment(targetId, content, user)

            return "redirect:/question/detail/${questionId}"
        }
        else { // commentType == CommentType.AnswerComment
            commentService.createAnswerComment(targetId, content, user)

            return "redirect:/question/detail/${questionId}#answer_${targetId}?page=${page}"
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    fun deleteComment(
        @PathVariable("id") id: Long,
        @RequestParam(value="return") questionId: Long,
        principal: Principal
    ): String {
        val comment = commentService.getCommentDetail(id)

        if (!comment.author.username.equals(principal.getName())) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission to delete")
        }

        commentService.delete(id)

        return "redirect:/question/detail/${questionId}"
    }

    @GetMapping("/load")
    @ResponseBody
    fun loadComments(
        @RequestParam("type") type: Long,
        @RequestParam("target") targetId: Long
    ): List<CommentDetail> {
        when(integerToCommentType(type)) {
            CommentType.QuestionComment -> return commentService.getQuestionComments(targetId)
            CommentType.AnswerComment -> return commentService.getAnswerComments(targetId)
            else -> return emptyList() 
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    fun modifyComment(
        commentForm: CommentForm, 
        @PathVariable("id") id: Long,
        @RequestParam("return") questionId: Long,
        principal: Principal
    ): String {
        val comment = commentService.getCommentDetail(id)

        if (!comment.author.username.equals(principal.getName())) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission to modify")
        }
        
        commentForm.content = comment.content
        commentForm.questionId = questionId;

        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    fun modifyComment(
        @Valid commentForm: CommentForm, 
        bindingResult: BindingResult,
        @PathVariable("id") id: Long,
        principal: Principal
    ): String {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        val comment = commentService.getCommentDetail(id)

        if (!comment.author.username.equals(principal.getName())) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission to modify")
        }

        commentService.modify(comment, commentForm.content!!)

        return "redirect:/question/detail/${commentForm.questionId}"
    }

}
