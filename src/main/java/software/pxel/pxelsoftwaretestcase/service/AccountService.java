package software.pxel.pxelsoftwaretestcase.service;

import java.math.BigDecimal;

public interface AccountService {

    void transfer(Long userIdFrom, Long userIdTo, BigDecimal amount);

}
