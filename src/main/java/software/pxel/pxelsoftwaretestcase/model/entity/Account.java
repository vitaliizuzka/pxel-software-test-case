package software.pxel.pxelsoftwaretestcase.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    private Long id;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "initial_balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal initialBalance;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

}
