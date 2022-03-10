package com.github.radiantai.blogpostfetcher.controllers.api;

import com.github.radiantai.blogpostfetcher.datasources.postsdatasource.PostsDatasource;
import com.github.radiantai.blogpostfetcher.requests.PostsRequest;
import com.github.radiantai.blogpostfetcher.responses.ErrorResponse;
import com.github.radiantai.blogpostfetcher.responses.PostsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
    @Autowired
    private PostsDatasource postsDatasource;
    @GetMapping
    public ResponseEntity getPosts(@Valid PostsRequest postsRequest, BindingResult bindingResult) throws ExecutionException, InterruptedException {

        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(bindingResult.getFieldError().getDefaultMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new PostsResponse(postsDatasource.getPosts(postsRequest)));
    }
}
