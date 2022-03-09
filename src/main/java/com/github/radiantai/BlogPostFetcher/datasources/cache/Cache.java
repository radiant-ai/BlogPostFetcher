package com.github.radiantai.BlogPostFetcher.datasources.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Cache<T> {
    private final ConcurrentHashMap<String, CacheEntry<T>> cacheMap;
    private final long ttl;

    public Cache(@Value("${cache.ttl}") long ttl) {
        this.cacheMap = new ConcurrentHashMap<>();
        this.ttl = ttl;
    }

    public T getFromCache(String key, Callable<T> cacheMissFunc) {
        CacheEntry<T> result = cacheMap.get(key);
        if (result == null || result.getRetrievalTime() + ttl < System.currentTimeMillis()) {
            try {
                T fetched = cacheMissFunc.call();
                cacheMap.put(key, new CacheEntry<T>(fetched, System.currentTimeMillis()));
                return fetched;
            }
            catch (Exception exception) {
                return null;
            }
        }
        return result.getObject();
    }
}
