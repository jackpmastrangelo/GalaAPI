package gala.gala_api.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {
  private JwtTokenProvider jwtTokenProvider;

  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
          throws IOException, ServletException {

    String token = extractToken((HttpServletRequest) req);
    if (token != null) {
      Authentication auth = jwtTokenProvider.createAuthentication(token);
      if (auth != null) {
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }

    filterChain.doFilter(req, res);
  }

  private String extractToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }

    return null;
  }
}
