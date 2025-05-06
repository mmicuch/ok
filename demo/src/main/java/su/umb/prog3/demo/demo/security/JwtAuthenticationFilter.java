package su.umb.prog3.demo.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Ak auth header neexistuje alebo nemá správny formát, pokračujeme na ďalší filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrahujeme JWT token z auth headeru
        jwt = authHeader.substring(7);

        try {
            // Extrahujeme username z tokenu
            username = jwtService.extractUsername(jwt);

            // Ak username existuje a používateľ ešte nie je autentifikovaný
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Načítame user details z databázy
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Validujeme token
                if (jwtService.validateToken(jwt, userDetails)) {
                    // Vytvoríme autentifikačný token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // Nastavíme detaily
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Nastavíme autentifikáciu v security contexte
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // V prípade chyby (neplatný token, neexistujúci používateľ, atď.) pokračujeme bez nastavenia autentifikácie
        }

        // Pokračujeme na ďalší filter
        filterChain.doFilter(request, response);
    }
}
