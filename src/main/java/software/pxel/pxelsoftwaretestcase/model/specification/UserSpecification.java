package software.pxel.pxelsoftwaretestcase.model.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import software.pxel.pxelsoftwaretestcase.model.entity.EmailData;
import software.pxel.pxelsoftwaretestcase.model.entity.PhoneData;
import software.pxel.pxelsoftwaretestcase.model.entity.User;

import java.time.LocalDate;

public class UserSpecification {

    public static Specification<User> filterBy(
            LocalDate dateOfBirth,
            String name,
            String phone,
            String email) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (dateOfBirth != null) {
                predicate = builder.and(predicate, builder.greaterThan(root.get("dateOfBirth"), dateOfBirth));
            }
            if (name != null && !name.isEmpty()) {
                predicate = builder.and(predicate, builder.like(root.get("name"), name + "%"));
            }
            if (email != null && !email.isBlank()) {
                Join<User, EmailData> emailJoin = root.join("emails", JoinType.LEFT);
                predicate = builder.and(predicate, builder.equal(emailJoin.get("email"), email));
            }
            if (phone != null && !phone.isEmpty()) {
                Join<User, PhoneData> phoneJoin = root.join("phones", JoinType.LEFT);
                predicate = builder.and(predicate, builder.equal(phoneJoin.get("phone"), phone));
            }
            return predicate;
        };
    }
}
