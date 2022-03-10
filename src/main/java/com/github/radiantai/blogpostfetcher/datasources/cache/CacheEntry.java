package com.github.radiantai.blogpostfetcher.datasources.cache;

public class CacheEntry<T> {
    private final T object;
    private final long retrievalTime;
    protected CacheEntry(T object, long retrievalTime) {
        this.object = object;
        this.retrievalTime = retrievalTime;
    }

    public T getObject() {
        return object;
    }

    public long getRetrievalTime() {
        return retrievalTime;
    }
}
