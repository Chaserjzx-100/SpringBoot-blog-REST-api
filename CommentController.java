package com.springboot.blog.blogrestapi.controller;

import com.springboot.blog.blogrestapi.payload.CommentDTO;
import com.springboot.blog.blogrestapi.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "postId") long postId,
                                                   @Valid @RequestBody CommentDTO commentDTO){

        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable(value = "postId") long postId){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value = "postId") long postId,
                                                     @PathVariable(value = "id") long commentId){
        CommentDTO commentDTO = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }
    /*Still needs testing in Postman*/
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "postId") long postId,
                                                    @PathVariable(value = "id") long commentId,
                                                   @Valid @RequestBody CommentDTO commentDTO){
        CommentDTO updatedComment = commentService.updateComment(postId, commentId, commentDTO);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") long postId,
                                                    @PathVariable(value = "id") long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
