package software.pxel.pxelsoftwaretestcase.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import software.pxel.pxelsoftwaretestcase.model.entity.EmailData;
import software.pxel.pxelsoftwaretestcase.model.entity.PhoneData;
import software.pxel.pxelsoftwaretestcase.model.entity.User;
import software.pxel.pxelsoftwaretestcase.repository.EmailDataRepository;
import software.pxel.pxelsoftwaretestcase.repository.PhoneDataRepository;
import software.pxel.pxelsoftwaretestcase.repository.UserRepository;
import software.pxel.pxelsoftwaretestcase.security.AppUserDetails;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    @Autowired
    public AppUserDetailsService(UserRepository userRepository, EmailDataRepository emailDataRepository, PhoneDataRepository phoneDataRepository) {
        this.userRepository = userRepository;
        this.emailDataRepository = emailDataRepository;
        this.phoneDataRepository = phoneDataRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        if(username.contains("@")){
            EmailData emailData = emailDataRepository
                    .findByEmail(username)
                    .orElseThrow(()->new UsernameNotFoundException("Email not found " + username));
            user =emailData.getUser();
        }else {
            PhoneData phoneData = phoneDataRepository
                    .findByPhone(username)
                    .orElseThrow(()-> new UsernameNotFoundException("Phone not found " + username));
            user = phoneData.getUser();
        }
        return new AppUserDetails(user);
    }

    public UserDetails loadUserByUsernameById(Long id){
        User user = userRepository
                .findById(id)
                .orElseThrow(()->new UsernameNotFoundException("User not found with id " + id));
        return new AppUserDetails(user);
    }

}
