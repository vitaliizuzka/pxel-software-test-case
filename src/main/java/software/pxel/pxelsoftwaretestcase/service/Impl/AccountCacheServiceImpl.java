package software.pxel.pxelsoftwaretestcase.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import software.pxel.pxelsoftwaretestcase.service.AccountCacheService;

@Service
public class AccountCacheServiceImpl implements AccountCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountCacheServiceImpl.class);
    private final CacheManager cacheManager;

    @Autowired
    public AccountCacheServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @CacheEvict(cacheNames = "account", key = "#userId")
    public void evictAccountCache(Long userId) {
        LOGGER.info("evict {}", userId);

    }
}