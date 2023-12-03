package com.springboot.blog.blogrestapi.repository;

import com.springboot.blog.blogrestapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategoryId(Long categoryId);
}
