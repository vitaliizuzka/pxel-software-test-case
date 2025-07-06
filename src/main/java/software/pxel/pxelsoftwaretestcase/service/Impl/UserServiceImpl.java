package software.pxel.pxelsoftwaretestcase.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import software.pxel.pxelsoftwaretestcase.model.dto.UserReadDto;
import software.pxel.pxelsoftwaretestcase.model.entity.User;
import software.pxel.pxelsoftwaretestcase.model.filter.UserFilter;
import software.pxel.pxelsoftwaretestcase.model.mapper.UserMapper;
import software.pxel.pxelsoftwaretestcase.model.specification.UserSpecification;
import software.pxel.pxelsoftwaretestcase.repository.UserRepository;
import software.pxel.pxelsoftwaretestcase.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Page<UserReadDto> findAllWithFilter(UserFilter userFilter, Pageable pageable) {
        Specification<User> specification = UserSpecification.filterBy(
                userFilter.getDateOfBirth(),
                userFilter.getName(),
                userFilter.getPhone(),
                userFilter.getEmail()
        );
        Page<User> users = userRepository.findAll(specification, pageable);
        return users.map(userMapper::toDto);
    }
}
