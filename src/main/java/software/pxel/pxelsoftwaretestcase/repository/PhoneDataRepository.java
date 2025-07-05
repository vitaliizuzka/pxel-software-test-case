package software.pxel.pxelsoftwaretestcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import software.pxel.pxelsoftwaretestcase.model.entity.PhoneData;

import java.util.Optional;

@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
    boolean existsByPhone(String phone);
    Optional<PhoneData> findByPhone(String phone);
}
