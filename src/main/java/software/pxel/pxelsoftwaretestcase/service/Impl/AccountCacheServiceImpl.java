package software.pxel.pxelsoftwaretestcase.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import software.pxel.pxelsoftwaretestcase.service.AccountCacheService;

@Service
public class AccountCacheServiceImpl implements AccountCacheService {

    private final CacheManager cacheManager;

    @Autowired
    public AccountCacheServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void evictAccountCache(Long userId) {
        Cache cache = cacheManager.getCache("accounts");
        if (cache != null) {
            cache.evict(userId);
        }
    }
}