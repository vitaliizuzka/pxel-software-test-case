package software.pxel.pxelsoftwaretestcase.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.pxelsoftwaretestcase.model.entity.Account;
import software.pxel.pxelsoftwaretestcase.repository.AccountRepository;
import software.pxel.pxelsoftwaretestcase.service.AccountBalanceAutoInc;
import software.pxel.pxelsoftwaretestcase.service.AccountCacheService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountBalanceAutoIncImpl implements AccountBalanceAutoInc {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountBalanceAutoIncImpl.class);

    private static final BigDecimal MAX_BALANCE_MULTIPLIER_FACTOR = new BigDecimal("2.07");
    private static final BigDecimal INCREASE_BALANCE_MULTIPLIER_FACTOR = new BigDecimal("1.10");

    private final AccountRepository accountRepository;
    private final AccountBalanceAutoInc accountBalanceAutoInc;
    private final AccountCacheService accountCacheService;

    public AccountBalanceAutoIncImpl(AccountRepository accountRepository,
                                     @Lazy AccountBalanceAutoInc accountBalanceAutoInc,
                                     AccountCacheService accountCacheService) {
        this.accountRepository = accountRepository;
        this.accountBalanceAutoInc = accountBalanceAutoInc;
        this.accountCacheService = accountCacheService;
    }


    @Transactional
    @Cacheable(cacheNames = "account", key = "#userId")
    public Account getAccountById(Long userId) {
        return accountRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new EntityNotFoundException("account " + userId + " not found"));
    }

    @Transactional
    @Cacheable(cacheNames = "accountIds")
    public List<Long> getAllAccountIds() {
        return accountRepository.findAllIds();
    }

    @Transactional
    @Scheduled(fixedRateString = "${balances.increase.fixed-rate}", initialDelayString = "${balances.increase.initial-delay}")
    @Override
    public void increaseBalances() {
        LOGGER.info("start scheduled increase balances");
        List<Long> accountIds = accountBalanceAutoInc.getAllAccountIds();

        accountIds.parallelStream().forEach(id -> {
            try {
                accountBalanceAutoInc.increaseBalance(id);
            } catch (Exception e) {
                LOGGER.error("scheduled increase balances failed " + e.getMessage());
            }
        });
        LOGGER.info("scheduled increase balances successful");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void increaseBalance(Long accountId) {
        LOGGER.info("start to increase account balance with id: {}", accountId);
        try {
            Account account = accountBalanceAutoInc.getAccountById(accountId);
            BigDecimal maxBalance = account.getInitialBalance().multiply(MAX_BALANCE_MULTIPLIER_FACTOR);
            BigDecimal newBalance = account.getBalance().multiply(INCREASE_BALANCE_MULTIPLIER_FACTOR);
            if (newBalance.compareTo(maxBalance) > 0) {
                newBalance = maxBalance;
                LOGGER.info("account balance with id: {} is max", accountId);
            }
            if (newBalance.compareTo(account.getBalance()) > 0) {
                account.setBalance(newBalance);
                accountRepository.save(account);
                LOGGER.info("increase account balance with id: {} successful", accountId);
                accountBalanceAutoInc.evictCache(accountId);
            }
        } catch (EntityNotFoundException e) {
            LOGGER.error("increase account balance with id: {} failed: " + e.getMessage(), accountId);
        }
    }

    @CacheEvict(cacheNames = "account", key = "#userId")
    public void evictCache(Long userId) {
        LOGGER.info("evict {}", userId);
    }
}
