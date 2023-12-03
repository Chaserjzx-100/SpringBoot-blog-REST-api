package com.springboot.blog.blogrestapi.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(
        description = "PostDto Model Information"
)
public class PostDTO {
    private long id;
    @Schema(
            description = "Blog Post Title"
    )
    @NotEmpty
    @Size(min = 2, message = "Post title must have at least 2 characters.")
    private String title;

    @Schema(
            description = "Blog Post Description"
    )
    @NotEmpty
    @Size(min = 5, message = "Description must contain at least 5 characters.")
    private String description;

    @Schema(
            description = "Blog Post Content"
    )
    @NotEmpty
    private String content;
    private Set<CommentDTO> comments;

    @Schema(
            description = "Blog Post Category"
    )
    private Long categoryId;

}
