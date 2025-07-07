package software.pxel.pxelsoftwaretestcase.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.pxelsoftwaretestcase.model.dto.EmailCreateDeleteDto;
import software.pxel.pxelsoftwaretestcase.model.dto.EmailUpdateDto;
import software.pxel.pxelsoftwaretestcase.model.entity.EmailData;
import software.pxel.pxelsoftwaretestcase.model.entity.User;
import software.pxel.pxelsoftwaretestcase.repository.EmailDataRepository;
import software.pxel.pxelsoftwaretestcase.repository.UserRepository;
import software.pxel.pxelsoftwaretestcase.service.UserEmailService;

@Service
public class UserEmailServiceImpl implements UserEmailService {

    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;

    @Autowired
    public UserEmailServiceImpl(UserRepository userRepository, EmailDataRepository emailDataRepository) {
        this.userRepository = userRepository;
        this.emailDataRepository = emailDataRepository;
    }

    @Override
    @Transactional
    public void addEmail(Long userId, EmailCreateDeleteDto emailDto) {
        if (emailDataRepository.existsByEmail(emailDto.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        EmailData emailData = new EmailData();
        emailData.setEmail(emailDto.email());
        user.addEmail(emailData);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateEmail(Long userId, EmailUpdateDto emailDto) {
        if (emailDataRepository.existsByEmail(emailDto.newEmail())) {
            throw new IllegalArgumentException("New email already in use");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user not found"));

        EmailData emailData = user.getEmails()
                .stream()
                .filter(userEmail -> userEmail.getEmail().equals(emailDto.oldEmail()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with id: " + userId + " don't have email: " + emailDto.oldEmail()));
        emailData.setEmail(emailDto.newEmail());
        emailDataRepository.save(emailData);
    }

    @Override
    @Transactional
    public void deleteEmail(Long userId, EmailCreateDeleteDto emailDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        EmailData emailData = user.getEmails()
                .stream()
                .filter(userEmail -> userEmail.getEmail().equals(emailDto.email()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with id: " + userId + " don't have email: " + emailDto.email()));
        if (user.getPhones().size() == 1) throw new IllegalArgumentException("It's the last user email");
        user.removeEmail(emailData);
        emailDataRepository.delete(emailData);
    }
}
