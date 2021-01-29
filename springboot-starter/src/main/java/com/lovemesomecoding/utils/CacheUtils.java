package com.lovemesomecoding.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;

import com.lovemesomecoding.exception.ApiError;
import com.lovemesomecoding.exception.ApiException;

public class CacheUtils {

    @Autowired
    private CacheManager cacheManager;

    private Logger       log = LoggerFactory.getLogger(this.getClass());

    public boolean clearCache(String key) {
        return clearCache(key, false);
    }

    public boolean clearCache(String key, boolean logCacheDetails) {
        boolean cacheCleared = false;

        org.springframework.cache.Cache cache = cacheManager.getCache(key);

        if (cache != null) {
            if (logCacheDetails) {
                // log.info("cache details:{}", ObjectUtils.toJson(cache));
            }

            log.info("Clear cache for key:{}", key);
            log.info("cache details:{}", ObjMapperUtils.toJson(cache));

            cache.clear();

            cacheCleared = true;
        } else {
            log.info("Cache not found for key:{}", key);

            throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, "Cache, " + key + " not found"));
        }

        return cacheCleared;
    }
}
