package gala.gala_api.security;

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

import gala.gala_api.dao.AccountCrudDao;
import gala.gala_api.entity.Account;

@Component
@Qualifier("accountLoaderService")
public class AccountLoaderService implements UserDetailsService {

  private static final String USER_ROLE = "USER";

  private final AccountCrudDao accountCrudDao;

  @Autowired
  public AccountLoaderService(AccountCrudDao accountCrudDao) {
    this.accountCrudDao = accountCrudDao;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = accountCrudDao.findByEmail(username);

    if (account != null) {
      List<GrantedAuthority> rolesForAllUsers = AuthorityUtils.createAuthorityList(USER_ROLE);

      return new User(account.getEmail(), account.getPassword(), rolesForAllUsers);
    } else {
      throw new UsernameNotFoundException("Could not find Account for email: " + username);
    }
  }
}