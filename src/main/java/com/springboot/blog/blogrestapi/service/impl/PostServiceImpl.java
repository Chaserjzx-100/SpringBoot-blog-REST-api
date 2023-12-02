package com.springboot.blog.blogrestapi.service.impl;

import com.springboot.blog.blogrestapi.entity.Category;
import com.springboot.blog.blogrestapi.entity.Post;
import com.springboot.blog.blogrestapi.exception.ResourceNotFound;
import com.springboot.blog.blogrestapi.payload.PostDTO;
import com.springboot.blog.blogrestapi.payload.PostResponse;
import com.springboot.blog.blogrestapi.repository.CategoryRepository;
import com.springboot.blog.blogrestapi.repository.PostRepository;
import com.springboot.blog.blogrestapi.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

//import java.awt.print.Pageable;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;
    private CategoryRepository categoryRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper,
                           CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO){

    Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFound("Category", "id", postDTO.getCategoryId()));
    Post post = mapToEntity(postDTO);
    Post newPost = postRepository.save(post);
    post.setCategory(category);
    PostDTO postResponse = mapToDTO(newPost);

    return postResponse;
    }

    //converts entity into dto
    private PostDTO mapToDTO(Post post){
        PostDTO postDTO = mapper.map(post, PostDTO.class);

//        PostDTO postDTO = new PostDTO();
//        postDTO.setId(post.getId());
//        postDTO.setTitle(post.getTitle());
//        postDTO.setDescription(post.getDescription());
//        postDTO.setContent(post.getContent());
        return postDTO;
    }
    private Post mapToEntity(PostDTO postDTO){
        Post post = mapper.map(postDTO, Post.class);

//        Post post = new Post();
//        post.setTitle(postDTO.getTitle());
//        post.setDescription(postDTO.getDescription());
//        post.setContent(postDTO.getContent());
        return post;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir){

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        //get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDTO> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDTO getPostById(long id){
    Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post","id", id));
    return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, long id){
        //First retrieve the post from the database.
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post","id", id));

        Category category = categoryRepository.findById(postDTO.getCategoryId())
                        .orElseThrow(()-> new ResourceNotFound("Category", "id", postDTO.getCategoryId()));

        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        post.setCategory(category);

        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);

    }

    @Override
    public String deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post","id", id));
        postRepository.deleteById(id);

        return "Post deleted with id:" + id;
    }

    @Override
    public List<PostDTO> getPostsByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFound("Category", "id", categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream().map((post)-> mapToDTO(post))
                .collect(Collectors.toList());
    }


}
