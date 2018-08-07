package gala.gala_api.config;

import gala.gala_api.config.security.JwtTokenFilterConfigurer;
import gala.gala_api.config.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private JwtTokenFilterConfigurer jwtTokenFilterConfigurer;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //All OPTIONS calls are permitted for browser's preflight requests.
    http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll();

    // No session will be created or used by spring security
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    //Activate CORS
    http.cors();

    //Disable Cross-Site Request Forgery
    http.csrf().disable();

    // Apply JWT
    http.apply(jwtTokenFilterConfigurer);

    //Route matching to set security.
    http.authorizeRequests()
            .antMatchers("/events/users", "/tickets/validate")
            .authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8080")
                .allowCredentials(true);
      }
    };
  }

  @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Autowired
  public void setJwtTokenFilterConfigurer(JwtTokenFilterConfigurer jwtTokenFilterConfigurer) {
    this.jwtTokenFilterConfigurer = jwtTokenFilterConfigurer;
  }
}
