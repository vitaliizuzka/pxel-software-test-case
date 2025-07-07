package software.pxel.pxelsoftwaretestcase.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import software.pxel.pxelsoftwaretestcase.model.entity.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account> findByUserIdForUpdate(Long id);
    Optional<Account> findByUserId(Long userId);

    @Query("select a.id from Account a")
    List<Long> findAllIds();
}
