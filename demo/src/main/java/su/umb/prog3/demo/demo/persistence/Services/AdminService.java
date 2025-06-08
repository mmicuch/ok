package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.dto.AuthResponseDTO;
import su.umb.prog3.demo.demo.persistence.dto.LoginRequestDTO;
import su.umb.prog3.demo.demo.persistence.entity.AdminEntity;
import su.umb.prog3.demo.demo.persistence.entity.OsobaEntity;
import su.umb.prog3.demo.demo.persistence.repos.AdminRepository;
import su.umb.prog3.demo.demo.persistence.repos.OsobaRepository;
import su.umb.prog3.demo.demo.security.JwtService;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final OsobaRepository osobaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminService(AdminRepository adminRepository, OsobaRepository osobaRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.osobaRepository = osobaRepository;
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
        String token = jwtService.generateToken(admin.getUsername());
        return new AuthResponseDTO(token, admin.getUsername());
    }

    // Metóda pre vytvorenie admin používateľa (ak neexistuje)
    public AdminEntity createAdminIfNotExists(String username, String rawPassword) {
        return adminRepository.findByUsername(username)
                .orElseGet(() -> {
                    AdminEntity admin = new AdminEntity();
                    admin.setUsername(username);
                    admin.setPassword(passwordEncoder.encode(rawPassword));
                    return adminRepository.save(admin);
                });
    }

    // Pridajte túto metódu
    public OsobaEntity createOsoba(OsobaEntity osoba) {
        return osobaRepository.save(osoba);
    }
}
