package software.pxel.pxelsoftwaretestcase.service;

import software.pxel.pxelsoftwaretestcase.model.entity.Account;

import java.util.List;

public interface AccountBalanceAutoInc {
    void increaseBalances();
    void increaseBalance(Long id);
    List<Long> getAllAccountIds();
    Account getAccountById(Long userId);
    void evictCache(Long userId);
}
