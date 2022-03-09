package com.github.radiantai.BlogPostFetcher.datasources.postsdatasource;

import com.github.radiantai.BlogPostFetcher.datasources.cache.Cache;
import com.github.radiantai.BlogPostFetcher.entities.Post;
import com.github.radiantai.BlogPostFetcher.requests.PostsRequest;
import com.github.radiantai.BlogPostFetcher.responses.PostsResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class PostsDatasource<Posts> {
    private final RestTemplate restTemplate;
    private final Cache<List<Post>> postsCache;

    @Autowired
    public PostsDatasource(RestTemplate restTemplate, Cache<List<Post>> postsCache) {
        this.restTemplate = restTemplate;
        this.postsCache = postsCache;
    }

    public List<Post> getPosts(PostsRequest postsRequest) throws ExecutionException, InterruptedException {
        String[] tagList = getTagList(postsRequest);
        List<Post> posts = getPostsByTags(tagList);
        sortPostList(posts, Post.getComparator(postsRequest.getSortBy(), postsRequest.getDirection()));
        return posts;
    }

    private String[] getTagList(PostsRequest postsRequest) {
        return postsRequest.getTags().split(",");
    }

    private @NotNull List<Post> getPostsByTag(String tag) {
        return postsCache.getFromCache(tag, () -> {
            PostsResponse postsResponse = restTemplate.getForObject("/assessment/blog/posts?tag={tag}", PostsResponse.class, tag);
            if (postsResponse == null || postsResponse.getPosts() == null) {
                return new ArrayList<>();
            }
            return postsResponse.getPosts();
        });
    }

    private @NotNull List<Post> getPostsByTags(String[] tags) throws ExecutionException, InterruptedException {
        Set<Post> uniquePosts = ConcurrentHashMap.newKeySet();
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (String tag : tags) {
            completableFutures.add(CompletableFuture.runAsync(() -> {
                List<Post> posts = getPostsByTag(tag);
                uniquePosts.addAll(posts);
            }));
        }
        CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new)).get();
        return new ArrayList<>(uniquePosts);
    }

    private void sortPostList(List<Post> posts, Comparator<Post> postComparator) {
        posts.sort(postComparator);
    }
}
