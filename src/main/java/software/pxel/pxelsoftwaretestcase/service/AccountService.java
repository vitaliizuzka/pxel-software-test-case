package software.pxel.pxelsoftwaretestcase.service;

import software.pxel.pxelsoftwaretestcase.model.entity.Account;

import java.math.BigDecimal;

public interface AccountService {

    void transfer(Long userIdFrom, Long userIdTo, BigDecimal amount);
    Account getAccountById(Long userId);

}
