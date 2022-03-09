package com.github.radiantai.BlogPostFetcher.requests;

import com.github.radiantai.BlogPostFetcher.validation.constraints.CommaSeparatedListOfStringsConstraint;
import com.github.radiantai.BlogPostFetcher.validation.constraints.DirectionSpecifierConstraint;
import com.github.radiantai.BlogPostFetcher.validation.constraints.SortBySpecifierConstraint;

import javax.validation.constraints.NotBlank;

public class PostsRequest {
    @NotBlank(message = "Tags parameter is required")
    @CommaSeparatedListOfStringsConstraint
    private final String tags;
    @SortBySpecifierConstraint
    private final String sortBy;
    @DirectionSpecifierConstraint
    private final String direction;

    public PostsRequest(String tags, String sortBy, String direction) {
        this.tags = tags;
        this.sortBy = sortBy == null ? "id" : sortBy;
        this.direction = direction == null ? "asc" : direction;
    }

    public String getTags() {
        return tags;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getDirection() {
        return direction;
    }
}
