package gala.gala_api.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import gala.gala_api.dao.AccountCrudDao;
import gala.gala_api.entity.Account;

@Component
@Qualifier("accountLoaderService")
public class AccountLoaderSecurityService implements UserDetailsService {

  private static final List<GrantedAuthority> ROLES_FOR_ALL_USERS =
          AuthorityUtils.createAuthorityList("USER");

  private final AccountCrudDao accountCrudDao;

  @Autowired
  public AccountLoaderSecurityService(AccountCrudDao accountCrudDao) {
    this.accountCrudDao = accountCrudDao;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Account> maybeAccount = accountCrudDao.findByEmail(username);

    if (maybeAccount.isPresent()) {
      Account account = maybeAccount.get();
      return new User(account.getEmail(), account.getPassword(), ROLES_FOR_ALL_USERS);
    } else {
      throw new UsernameNotFoundException("Could not find Account for email: " + username);
    }
  }
}