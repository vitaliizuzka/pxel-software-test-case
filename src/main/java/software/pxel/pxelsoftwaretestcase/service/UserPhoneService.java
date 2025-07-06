package software.pxel.pxelsoftwaretestcase.service;

import software.pxel.pxelsoftwaretestcase.model.dto.PhoneCreateDeleteDto;
import software.pxel.pxelsoftwaretestcase.model.dto.PhoneUpdateDto;

public interface UserPhoneService {
    void addPhone(Long userId, PhoneCreateDeleteDto phoneDto);
    void updatePhone(Long userId, PhoneUpdateDto phoneDto);
    void deletePhone(Long userId, PhoneCreateDeleteDto phoneDto);
}
