package com.github.radiantai.BlogPostFetcher.responses;

import com.github.radiantai.BlogPostFetcher.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class PostsResponse extends BaseResponse {
    private final List<Post> posts;

    public PostsResponse() {
        this.posts = new ArrayList<>();
    }

    public PostsResponse(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
