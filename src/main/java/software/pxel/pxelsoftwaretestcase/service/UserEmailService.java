package software.pxel.pxelsoftwaretestcase.service;

import software.pxel.pxelsoftwaretestcase.model.dto.EmailCreateDeleteDto;
import software.pxel.pxelsoftwaretestcase.model.dto.EmailUpdateDto;

public interface UserEmailService {
    void addEmail(Long userId, EmailCreateDeleteDto emailDto);
    void updateEmail(Long userId, EmailUpdateDto emailDto);
    void deleteEmail(Long userId, EmailCreateDeleteDto emailDto);
}
