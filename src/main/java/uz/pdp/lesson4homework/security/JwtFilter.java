package uz.pdp.lesson4homework.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.lesson4homework.service.MyAuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    MyAuthService myAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        // REQUESTDAN TOKENNI OLISH
        String token = httpServletRequest.getHeader("Authorization");
        // TOKENNI BORLIGINI VA BEARER BN BOSHLANISHINI TEKSHIRISH
        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
            // TOKENNI VALIDATSIYADAN O'TKAZDIK
            boolean validateToken = jwtProvider.validateToken(token);
            if (validateToken) {
                // TOKENNI ICHIDAN USERNAME OLDIK
                String username = jwtProvider.getUserNameFromToken(token);
                // USERNAME ORQALI USERDETAILS NI OLDIK
                UserDetails userDetails = myAuthService.loadUserByUsername(username);
                // USERDETAILS ORQALI AUTHENTIFICATION YARATIB OLDIK
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // SISTEMAGA KIM KIRGANLIGINI O'RNATDIK
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
