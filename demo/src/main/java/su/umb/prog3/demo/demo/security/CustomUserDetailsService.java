package su.umb.prog3.demo.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.entity.UserEntity;
import su.umb.prog3.demo.demo.persistence.repos.UserRepository;

@Service
@Primary
@ConditionalOnBean(UserRepository.class)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(@Autowired(required = false) UserRepository userRepository, 
                                  @Autowired(required = false) PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository == null) {
            throw new UsernameNotFoundException("UserRepository not available");
        }
        
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        System.out.println("Loading user: " + username + " with role: " + user.getRole());
        
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole()) // ROLE_ADMIN alebo ROLE_USER
                .build();
    }
}