package software.pxel.pxelsoftwaretestcase.model.mapper;

import org.springframework.stereotype.Component;
import software.pxel.pxelsoftwaretestcase.model.dto.UserReadDto;
import software.pxel.pxelsoftwaretestcase.model.entity.EmailData;
import software.pxel.pxelsoftwaretestcase.model.entity.PhoneData;
import software.pxel.pxelsoftwaretestcase.model.entity.User;

@Component
public class UserMapper {
    public UserReadDto toDto(User user){
        return new UserReadDto(
                user.getId(),
                user.getName(),
                user.getDateOfBirth(),
                user.getEmails().stream().map(EmailData::getEmail).toList(),
                user.getPhones().stream().map(PhoneData::getPhone).toList(),
                user.getAccount());
    }
}
