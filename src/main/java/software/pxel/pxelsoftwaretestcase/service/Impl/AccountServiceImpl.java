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
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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



}
