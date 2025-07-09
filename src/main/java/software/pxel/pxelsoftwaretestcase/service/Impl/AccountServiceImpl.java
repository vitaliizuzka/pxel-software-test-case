package software.pxel.pxelsoftwaretestcase.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.pxelsoftwaretestcase.model.entity.Account;
import software.pxel.pxelsoftwaretestcase.repository.AccountRepository;
import software.pxel.pxelsoftwaretestcase.service.AccountCacheService;
import software.pxel.pxelsoftwaretestcase.service.AccountService;

import java.math.BigDecimal;


@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AccountCacheService accountCacheService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              @Lazy AccountService accountService,
                              AccountCacheService accountCacheService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.accountCacheService = accountCacheService;
    }

    @Transactional
    @Override
    public void transfer(Long userIdFrom, Long userIdTo, BigDecimal amount) {
        LOGGER.info("start transfer balance from id: {} to id: {}", userIdFrom, userIdTo);
        try {
            if (userIdFrom.equals(userIdTo)) {
                throw new IllegalArgumentException("you can't transfer money to yourself");
            }
            if ((amount.compareTo(BigDecimal.ZERO) <= 0)) {
                throw new IllegalArgumentException("the transfer amount must be positive");
            }

            Long firstLockId = userIdFrom < userIdTo ? userIdFrom : userIdTo;
            Long secondLockId = userIdFrom < userIdTo ? userIdTo : userIdFrom;

            Account firstAccount = accountRepository.findByUserIdForUpdate(firstLockId)
                    .orElseThrow(() -> new EntityNotFoundException("account " + firstLockId + " not found"));
            Account secondAccount = accountRepository.findByUserIdForUpdate(secondLockId)
                    .orElseThrow(() -> new EntityNotFoundException("account " + secondLockId + " not found"));

            Account senderAccount = null;
            Account receiverAccount = null;

            if (userIdFrom.equals(firstLockId)) {
                senderAccount = firstAccount;
                receiverAccount = secondAccount;
            } else {
                senderAccount = secondAccount;
                receiverAccount = firstAccount;
            }

            if (senderAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Not enough money");
            }

            secondAccount.setBalance(firstAccount.getBalance().subtract(amount));
            receiverAccount.setBalance(secondAccount.getBalance().add(amount));
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);

            accountCacheService.evictAccountCache(senderAccount.getId());
            accountCacheService.evictAccountCache(receiverAccount.getId());

        } catch (IllegalArgumentException | EntityNotFoundException | IllegalStateException e) {
            LOGGER.info("transfer balance from id: {} to id: {} failed: {}", userIdFrom, userIdTo, e.getMessage());
            throw e;
        }
        LOGGER.info("transfer balance from id: {} to id: {} successful", userIdFrom, userIdTo);
    }
}
