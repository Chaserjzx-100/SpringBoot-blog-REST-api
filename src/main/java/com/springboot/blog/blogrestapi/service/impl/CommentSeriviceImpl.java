package com.springboot.blog.blogrestapi.service.impl;

import com.springboot.blog.blogrestapi.entity.Comment;
import com.springboot.blog.blogrestapi.entity.Post;
import com.springboot.blog.blogrestapi.exception.BlogAPIException;
import com.springboot.blog.blogrestapi.exception.ResourceNotFound;
import com.springboot.blog.blogrestapi.payload.CommentDTO;
import com.springboot.blog.blogrestapi.repository.CommentRepository;
import com.springboot.blog.blogrestapi.repository.PostRepository;
import com.springboot.blog.blogrestapi.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentSeriviceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    @Autowired
    public CommentSeriviceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    private CommentDTO mapToDto(Comment comment){
        CommentDTO commentDTO = mapper.map(comment, CommentDTO.class);
//        CommentDTO commentDTO = new CommentDTO();
//        commentDTO.setId(comment.getId());
//        commentDTO.setName(comment.getName());
//        commentDTO.setEmail(comment.getEmail());
//        commentDTO.setBody(comment.getBody());
        return commentDTO;
    }
    private Comment mapToEntity(CommentDTO commentDTO){
        Comment comment = mapper.map(commentDTO, Comment.class);
//        Comment comment = new Comment();
//        comment.setName(commentDTO.getName());
//        comment.setEmail(commentDTO.getEmail());
//        comment.setBody(commentDTO.getBody());
        return comment;
    }

    @Override
    public CommentDTO createComment(long postId,CommentDTO commentDTO) {

        Comment comment = mapToEntity(commentDTO);

        Post post = postRepository.findById(postId).orElseThrow(
                    ()-> new ResourceNotFound("Post", "id", postId));

        comment.setPost(post);


          Comment newComment = commentRepository.save(comment);
          CommentDTO commentResponse = mapToDto(newComment);
          return commentResponse;
}

    @Override
    public List<CommentDTO> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findPostById(postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFound("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFound("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        return mapToDto(comment);

    }

    @Override
    public CommentDTO updateComment(long postId, long commentId, CommentDTO commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFound("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()->

                new ResourceNotFound("Comment", "id", commentId));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFound("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()->

                new ResourceNotFound("Comment", "id", commentId));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        commentRepository.delete(comment);
    }
}
