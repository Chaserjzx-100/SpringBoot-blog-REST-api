package com.springboot.blog.blogrestapi.controller;

import com.springboot.blog.blogrestapi.entity.Post;
import com.springboot.blog.blogrestapi.payload.PostDTO;
import com.springboot.blog.blogrestapi.payload.PostResponse;
import com.springboot.blog.blogrestapi.service.PostService;
import com.springboot.blog.blogrestapi.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/posts")
@Tag(
        name = "CRUD REST APIs for Post Resource"
)
public class PostController   {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(
            summary = "Create Post REST API",
            description = "Create Post REST API is used to save post into database."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    //create blog post rest api
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO){
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get All Posts REST API",
            description = "Get All Posts REST API is used to fetch all posts from the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allPosts")
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }

    @Operation(
            summary = "Get Post By Id REST API",
            description = "Get Post By Id REST API is used to get a single post from the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(
            summary = "Update Post By Id REST API",
            description = "Update Post By Id REST API is used to update a specific post in the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable(name = "id") long id){

        PostDTO postResponse = postService.updatePost(postDTO, id);
        return ResponseEntity.ok(postResponse);
    }

    @Operation(
            summary = "Delete Post By Id REST API",
            description = "Delete Post By Id REST API is used to delete a specific post from the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable(name = "id") long id){
        return postService.deletePostById(id);
    }

    @Operation(
            summary = "Get Post By Category Id REST API",
            description = "Get Post By Category Id REST API is used to get a single post from the database by the category id."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    //Build get posts by Category REST API
    //http://localhost:8080i/api/posts/category/3
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable("id") Long categoryId){
        List<PostDTO> postDtos = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(postDtos);
    }
}
