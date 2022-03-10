package com.github.radiantai.blogpostfetcher.responses;

import com.github.radiantai.blogpostfetcher.entities.Post;

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
