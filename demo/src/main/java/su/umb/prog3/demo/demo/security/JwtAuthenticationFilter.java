package su.umb.prog3.demo.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(@Autowired(required = false) JwtService jwtService, @Autowired(required = false) UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        
        System.out.println("=== JWT FILTER DEBUG ===");
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Auth Header: " + authHeader);
        
        if (authHeader != null && authHeader.startsWith("Bearer ") && userDetailsService != null && jwtService != null) {
            token = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(token);
                System.out.println("Extracted username: " + username);
            } catch (Exception e) {
                System.out.println("Error extracting username: " + e.getMessage());
            }
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && userDetailsService != null && jwtService != null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("User authorities: " + userDetails.getAuthorities());
                
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication set successfully");
                } else {
                    System.out.println("Token is not valid");
                }
            } catch (Exception e) {
                System.out.println("Error in JWT authentication: " + e.getMessage());
            }
        }
        
        System.out.println("=== END JWT FILTER DEBUG ===");
        filterChain.doFilter(request, response);
    }
}
