package software.pxel.pxelsoftwaretestcase.service;

import java.math.BigDecimal;

public interface AccountService {
    void increaseBalances();
    void transfer(Long userIdFrom, Long userIdTo, BigDecimal amount);

    void increaseBalance(Long id);
}
