package gala.gala_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/**/create")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/tickets/validate")
            .fullyAuthenticated()
    .and().httpBasic()
    .and().csrf().disable();
  }
}
