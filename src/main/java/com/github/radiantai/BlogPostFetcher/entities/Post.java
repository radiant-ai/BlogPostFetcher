package com.github.radiantai.BlogPostFetcher.entities;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Post {
    private final int id;
    private final String author;
    private final int authodId;
    private final int likes;
    private final double popularity;
    private final int reads;
    private final List<String> tags;

    public Post(int id, String author, int authodId, int likes, double popularity, int reads, List<String> tags) {
        this.id = id;
        this.author = author;
        this.authodId = authodId;
        this.likes = likes;
        this.popularity = popularity;
        this.reads = reads;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public int getAuthodId() {
        return authodId;
    }

    public int getLikes() {
        return likes;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getReads() {
        return reads;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public static @NotNull Comparator<Post> getComparator(String sortBy, String direction) {
        Function<Post, Comparable> sortingMethod = switch (sortBy) {
            case "reads":
                yield Post::getReads;
            case "likes":
                yield Post::getLikes;
            case "popularity":
                yield Post::getPopularity;
            default:
                yield Post::getId;
        };
        return (o1, o2) -> {
            int result = sortingMethod.apply(o1).compareTo(sortingMethod.apply(o2));
            if (direction.equals("desc"))
                result *= -1;
            return result;
        };
    }
}
