package com.github.radiantai.blogpostfetcher.controllers.api;

import com.github.radiantai.blogpostfetcher.responses.PingResponse;
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
