package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.dto.AuthResponseDTO;
import su.umb.prog3.demo.demo.persistence.dto.LoginRequestDTO;
import su.umb.prog3.demo.demo.persistence.entity.AdminEntity;
import su.umb.prog3.demo.demo.persistence.repos.AdminRepository;
import su.umb.prog3.demo.demo.security.JwtService;

import java.util.ArrayList;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO authenticate(LoginRequestDTO loginRequest) {
        // Nájdeme admin používateľa podľa username
        AdminEntity admin = adminRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        // Overíme heslo
        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Vytvoríme a vrátime JWT token
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .authorities(new ArrayList<>())
                .build();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(token, admin.getUsername());
    }

    // Metóda pre vytvorenie admin používateľa (ak neexistuje)
    public AdminEntity createAdminIfNotExists(String username, String rawPassword) {
        boolean adminExists = adminRepository.findByUsername(username).isPresent();

        if (!adminExists) {
            AdminEntity admin = new AdminEntity();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(rawPassword));
            return adminRepository.save(admin);
        }

        return adminRepository.findByUsername(username).get();
    }
}
