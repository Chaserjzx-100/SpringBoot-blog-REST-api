package com.springboot.blog.blogrestapi.service;

import com.springboot.blog.blogrestapi.payload.PostDTO;
import com.springboot.blog.blogrestapi.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTO postDTO);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDTO getPostById(long id);
    PostDTO updatePost(PostDTO postDTO, long id);
    String deletePostById(long id);
    List<PostDTO> getPostsByCategory(Long categoryId);

}