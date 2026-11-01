package tko.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tko.database.entity.user.UsersEntity;
import tko.database.repository.user.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter{
    public final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;

    public JwtFilter(JwtUtil jwtUtil, UsersRepository usersRepository) {
        this.jwtUtil = jwtUtil;
        this.usersRepository = usersRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if(token != null) {
            String login = jwtUtil.extractLogin(token);
            UsersEntity usersEntity = usersRepository.findByLogin(login);

            if(usersEntity != null && jwtUtil.validateToken(token, usersEntity)) {

                String role = jwtUtil.extractRole(token);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        usersEntity.getLogin(),
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(role))
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
