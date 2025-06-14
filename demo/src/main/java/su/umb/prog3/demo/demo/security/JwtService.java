package su.umb.prog3.demo.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    
    public JwtService() {
        System.out.println("JwtService initialized without database dependencies");
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    public String generateToken(String username) {
        System.out.println("Generating token for username: " + username);
        return generateToken(Map.of(), username);
    }
    
    public String generateToken(Map<String, Object> extraClaims, String username) {
        System.out.println("Generating token with claims for username: " + username);
        String token = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("Generated token: " + token.substring(0, Math.min(token.length(), 50)) + "...");
        return token;
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public UserDetails extractUserDetails(String token) {
        System.out.println("Extracting user details from token");
        
        try {
            String username = extractUsername(token);
            System.out.println("Extracted username: " + username);
            
            // Return a dummy user since database is disabled
            return User.builder()
                    .username(username)
                    .password("$2a$10$dummyPasswordHashForTesting")
                    .authorities("ROLE_ADMIN")
                    .build();
        } catch (Exception e) {
            System.err.println("Error extracting user details: " + e.getMessage());
            // Fallback to dummy user
            return User.builder()
                    .username("admin")
                    .password("$2a$10$dummyPasswordHashForTesting")
                    .authorities("ROLE_ADMIN")
                    .build();
        }
    }
}
