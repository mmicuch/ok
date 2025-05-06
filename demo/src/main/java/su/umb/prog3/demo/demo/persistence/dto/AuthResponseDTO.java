package su.umb.prog3.demo.demo.persistence.dto;

public class AuthResponseDTO {
    private String token;
    private String username;

    public AuthResponseDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
