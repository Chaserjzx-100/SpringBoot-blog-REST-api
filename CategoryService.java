package com.springboot.blog.blogrestapi.service;

import com.springboot.blog.blogrestapi.payload.CategoryDto;

import java.util.List;

public interface CategoryService{
    CategoryDto addCategory(CategoryDto categoryDto);
    CategoryDto getCategory(Long categoryId);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
    void deleteCategory(Long categoryId);
}
