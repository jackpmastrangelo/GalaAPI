package gala.gala_api.service.security;

import gala.gala_api.data_model.AccountUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import gala.gala_api.entity.Account;
import gala.gala_api.service.AccountService;

@Service
public class AccountLoaderSecurityService implements UserDetailsService {

  private static final List<GrantedAuthority> ROLES_FOR_ALL_USERS =
          AuthorityUtils.createAuthorityList("USER");

  private AccountService accountService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Account> maybeAccount = accountService.findByEmailIgnoreCase(username);

    if (maybeAccount.isPresent()) {
      Account account = maybeAccount.get();
      return new AccountUserDetails(account, ROLES_FOR_ALL_USERS);
    } else {
      throw new UsernameNotFoundException("Could not find Account for email: " + username);
    }
  }

  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }
}