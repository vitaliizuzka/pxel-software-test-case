package software.pxel.pxelsoftwaretestcase.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import software.pxel.pxelsoftwaretestcase.model.dto.UserReadDto;
import software.pxel.pxelsoftwaretestcase.model.entity.User;
import software.pxel.pxelsoftwaretestcase.model.filter.UserFilter;

public interface UserService {
    public Page<UserReadDto> findAllWithFilter(UserFilter userFilter, Pageable pageable);
}
