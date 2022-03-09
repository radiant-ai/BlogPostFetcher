package com.github.radiantai.BlogPostFetcher.controllers.api;

import com.github.radiantai.BlogPostFetcher.responses.PingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ping")
public class PingController {
    @GetMapping
    public PingResponse ping() {
        return new PingResponse(true);
    }
}
