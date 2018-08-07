package gala.gala_api.config.security;

import gala.gala_api.service.security.AccountLoaderSecurityService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

  @Value("${security.jwt.token.secret-key}")
  private String secretKey;

  @Value("${security.jwt.token.expire-length}")
  private long validityInMilliseconds = 3600000; // 1h

  private AccountLoaderSecurityService accountLoaderSecurityService;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(String email) {
    Claims claims = Jwts.claims().setSubject(email);
    claims.put("auth", "User");

    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
  }

  public Authentication createAuthentication(String token) {
    try {
      Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      String email = jws.getBody().getSubject();

      UserDetails userDetails = accountLoaderSecurityService.loadUserByUsername(email);
      return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    } catch (JwtException | IllegalArgumentException e) {
      return null;
    }
  }

  @Autowired
  public void setAccountLoaderSecurityService(AccountLoaderSecurityService accountLoaderSecurityService) {
    this.accountLoaderSecurityService = accountLoaderSecurityService;
  }
}