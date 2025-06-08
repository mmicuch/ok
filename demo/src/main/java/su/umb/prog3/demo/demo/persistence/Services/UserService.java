package su.umb.prog3.demo.demo.persistence.Services;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.entity.UserEntity;
import su.umb.prog3.demo.demo.persistence.repos.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createDefaultUsers() {
        System.out.println("=== CREATING DEFAULT USERS ===");
        
        // Create admin user
        if (!userRepository.existsByUsername("admin")) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole("ROLE_ADMIN");
            admin.setEmail("admin@example.com");
            userRepository.save(admin);
            System.out.println("✅ Default admin user created with role: " + admin.getRole());
        } else {
            System.out.println("ℹ️ Admin user already exists");
        }

        // Create regular user
        if (!userRepository.existsByUsername("user")) {
            UserEntity user = new UserEntity();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("ROLE_USER");
            user.setEmail("user@example.com");
            userRepository.save(user);
            System.out.println("✅ Default user created with role: " + user.getRole());
        } else {
            System.out.println("ℹ️ Regular user already exists");
        }
        
        System.out.println("=== USER CREATION COMPLETE ===");
    }

    // Other service methods...
}