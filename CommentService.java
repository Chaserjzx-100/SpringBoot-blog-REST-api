package com.springboot.blog.blogrestapi.service;

import com.springboot.blog.blogrestapi.entity.Comment;
import com.springboot.blog.blogrestapi.payload.CommentDTO;

import java.util.List;

public interface CommentService {

    CommentDTO createComment(long postDTO, CommentDTO commentDTO);
    List<CommentDTO> getCommentsByPostId(long postId);
    CommentDTO getCommentById(long postId, long commentId);
    CommentDTO updateComment(long postId, long commentId, CommentDTO commentRequest);
    void deleteComment(long postId, long commentId);
}
