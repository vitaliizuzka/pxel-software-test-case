package software.pxel.pxelsoftwaretestcase.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.pxelsoftwaretestcase.model.entity.Account;
import software.pxel.pxelsoftwaretestcase.repository.AccountRepository;
import software.pxel.pxelsoftwaretestcase.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private static final BigDecimal MAX_BALANCE_MULTIPLIER_FACTOR = new BigDecimal("2.07");
    private static final BigDecimal INCREASE_BALANCE_MULTIPLIER_FACTOR = new BigDecimal("1.10");

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Transactional
    public void transfer(Long userIdFrom, Long userIdTo, BigDecimal amount) {
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

    }

    @Transactional
    @Scheduled(fixedRateString = "${balances.increase.fixed-rate}", initialDelayString = "${balances.increase.initial-delay}")
    public void increaseBalances() {
        List<Long> accountIds = accountRepository.findAllIds();
        for (Long id : accountIds) {
            try {
                increaseBalance(id);
            } catch (Exception e) {
                //log
            }
        }
    }

    public void increaseBalance(Long accountId) {
        Account account = accountRepository.findByUserIdForUpdate(accountId).orElseThrow(EntityNotFoundException::new);

        BigDecimal maxBalance = account.getInitialBalance().multiply(MAX_BALANCE_MULTIPLIER_FACTOR);
        BigDecimal newBalance = account.getBalance().multiply(INCREASE_BALANCE_MULTIPLIER_FACTOR);

        if (newBalance.compareTo(maxBalance) > 0) {
            newBalance = maxBalance;
        }

        if (newBalance.compareTo(account.getBalance()) > 0) {
            account.setBalance(newBalance);
            accountRepository.save(account);
        }
    }
}
