package software.pxel.pxelsoftwaretestcase.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.pxelsoftwaretestcase.model.entity.Account;
import software.pxel.pxelsoftwaretestcase.repository.AccountRepository;
import software.pxel.pxelsoftwaretestcase.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private static final BigDecimal MAX_BALANCE_MULTIPLIER_FACTOR = new BigDecimal("2.07");
    private static final BigDecimal INCREASE_BALANCE_MULTIPLIER_FACTOR = new BigDecimal("1.10");

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, @Lazy AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
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
            Account accountFrom = accountRepository.findByUserIdForUpdate(userIdFrom)
                    .orElseThrow(() -> new EntityNotFoundException("sender not found"));
            Account accountTo = accountRepository.findByUserIdForUpdate(userIdTo)
                    .orElseThrow(() -> new EntityNotFoundException("recipient not found"));
            if (accountFrom.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Not enough money");
            }
            accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
            accountTo.setBalance(accountTo.getBalance().add(amount));
            accountRepository.save(accountFrom);
            accountRepository.save(accountTo);
        } catch (IllegalArgumentException | EntityNotFoundException | IllegalStateException e) {
            LOGGER.info("transfer balance from id: {} to id: {} failed: {}", userIdFrom, userIdTo, e.getMessage());
            throw e;
        }
        LOGGER.info("transfer balance from id: {} to id: {} successful", userIdFrom, userIdTo);
    }


    @Transactional
    @Scheduled(fixedRateString = "${balances.increase.fixed-rate}", initialDelayString = "${balances.increase.initial-delay}")
    @Override
    public void increaseBalances() {
        LOGGER.info("start scheduled increase balances");
        List<Long> accountIds = accountRepository.findAllIds();
        for (Long id : accountIds) {
            try {
                accountService.increaseBalance(id);
            } catch (Exception e) {
                LOGGER.error("scheduled increase balances failed " + e.getMessage());
            }
        }
        LOGGER.info("scheduled increase balances successful");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void increaseBalance(Long accountId) {
        LOGGER.info("start to increase account balance with id: {}", accountId);
        try {
            Account account = accountRepository.findByUserIdForUpdate(accountId)
                    .orElseThrow(() -> new EntityNotFoundException("account " + accountId + " not found"));
            BigDecimal maxBalance = account.getInitialBalance().multiply(MAX_BALANCE_MULTIPLIER_FACTOR);
            BigDecimal newBalance = account.getBalance().multiply(INCREASE_BALANCE_MULTIPLIER_FACTOR);
            if (newBalance.compareTo(maxBalance) > 0) {
                newBalance = maxBalance;
                LOGGER.info("account balance with id: {} is max" , accountId);
            }
            if (newBalance.compareTo(account.getBalance()) > 0) {
                account.setBalance(newBalance);
                accountRepository.save(account);
                LOGGER.info("increase account balance with id: {} successful" , accountId);
            }
        } catch (EntityNotFoundException e) {
            LOGGER.error("increase account balance with id: {} failed: " + e.getMessage(), accountId);
        }

    }
}
