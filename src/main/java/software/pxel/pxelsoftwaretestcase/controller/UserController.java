package software.pxel.pxelsoftwaretestcase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.pxel.pxelsoftwaretestcase.model.dto.UserReadDto;
import software.pxel.pxelsoftwaretestcase.model.filter.UserFilter;
import software.pxel.pxelsoftwaretestcase.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserReadDto> findAllWithFilter(UserFilter userFilter,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return userService.findAllWithFilter(userFilter, pageable);
    }

}
