package com.springboot.blog.blogrestapi.repository;

import com.springboot.blog.blogrestapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
