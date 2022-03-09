package com.github.radiantai.BlogPostFetcher;

import com.github.radiantai.BlogPostFetcher.datasources.cache.Cache;
import com.github.radiantai.BlogPostFetcher.entities.Post;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class BlogPostFetcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogPostFetcherApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.rootUri("https://api.hatchways.io")
				.build();
	}
}
