package software.pxel.pxelsoftwaretestcase.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.pxelsoftwaretestcase.model.dto.PhoneCreateDeleteDto;
import software.pxel.pxelsoftwaretestcase.model.dto.PhoneUpdateDto;
import software.pxel.pxelsoftwaretestcase.model.entity.PhoneData;
import software.pxel.pxelsoftwaretestcase.model.entity.User;
import software.pxel.pxelsoftwaretestcase.repository.PhoneDataRepository;
import software.pxel.pxelsoftwaretestcase.repository.UserRepository;
import software.pxel.pxelsoftwaretestcase.service.UserPhoneService;

@Service
public class UserPhoneServiceImpl implements UserPhoneService {

    private final UserRepository userRepository;
    private final PhoneDataRepository phoneDataRepository;

    @Autowired
    public UserPhoneServiceImpl(UserRepository userRepository, PhoneDataRepository phoneDataRepository) {
        this.userRepository = userRepository;
        this.phoneDataRepository = phoneDataRepository;
    }

    @Transactional
    @Override
    public void addPhone(Long userId, PhoneCreateDeleteDto phoneDto) {
        if (phoneDataRepository.existsByPhone(phoneDto.phone())) {
            throw new IllegalArgumentException("Phone already in use");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(phoneDto.phone());
        user.addPhone(phoneData);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePhone(Long userId, PhoneUpdateDto phoneDto) {
        if (phoneDataRepository.existsByPhone(phoneDto.newPhone())) {
            throw new IllegalArgumentException("New phone already in use");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user not found"));

        PhoneData phoneData = user.getPhones()
                .stream()
                .filter(userPhone -> userPhone.getPhone().equals(phoneDto.oldPhone()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with id: " + userId + " don't have phone: " + phoneDto.oldPhone()));
        phoneData.setPhone(phoneDto.newPhone());
        phoneDataRepository.save(phoneData);
    }

    @Override
    @Transactional
    public void deletePhone(Long userId, PhoneCreateDeleteDto phoneDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        PhoneData phoneData = user.getPhones()
                .stream()
                .filter(userPhone -> userPhone.getPhone().equals(phoneDto.phone()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with id: " + userId + " don't have phone: " + phoneDto.phone()));
        if (user.getPhones().size() == 1) throw new IllegalArgumentException("It's the last user phone");
        user.removePhone(phoneData);
        phoneDataRepository.delete(phoneData);
    }
}
