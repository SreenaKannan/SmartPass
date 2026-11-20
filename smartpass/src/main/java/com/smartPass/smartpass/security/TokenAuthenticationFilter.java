package com.smartPass.smartpass.security;
import com.smartPass.smartpass.entity.User; 
import jakarta.servlet.*; 
import jakarta.servlet.http.*; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.authority.SimpleGrantedAuthority; 
import org.springframework.security.core.context.SecurityContextHolder; 
import org.springframework.stereotype.Component; 
import org.springframework.web.filter.OncePerRequestFilter; 
import java.io.IOException; import java.util.List;
@Component public class TokenAuthenticationFilter extends OncePerRequestFilter {
 private final TokenService tokens; public TokenAuthenticationFilter(TokenService tokens){this.tokens=tokens;}
 protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException { String h=req.getHeader("Authorization"); if(h!=null&&h.startsWith("Bearer ")){User u=tokens.find(h.substring(7)); if(u!=null) SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u,u.getId(),List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole()))));} chain.doFilter(req,res); }
}
