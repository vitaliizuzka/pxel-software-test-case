package software.pxel.pxelsoftwaretestcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import software.pxel.pxelsoftwaretestcase.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailsEmail(String email);
    Optional<User> findByPhonesPhone(String phone);
}
