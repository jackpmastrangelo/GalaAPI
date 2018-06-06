package gala.gala_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import gala.gala_api.dao.AccountCrudDao;
import gala.gala_api.entity.Account;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private AccountCrudDao accountCrudDao;

  //TODO
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = accountCrudDao.findByEmail(username);
    return null;
  }
}
