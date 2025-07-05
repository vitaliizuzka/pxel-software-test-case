package software.pxel.pxelsoftwaretestcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import software.pxel.pxelsoftwaretestcase.model.entity.EmailData;

import java.util.Optional;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    boolean existsByEmail(String email);
    Optional<EmailData> findByEmail(String email);
}
