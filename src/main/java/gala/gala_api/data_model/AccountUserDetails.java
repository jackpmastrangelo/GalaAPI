package gala.gala_api.data_model;

import gala.gala_api.entity.Account;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

//TODO Discuss this.
public class AccountUserDetails extends User {

  private Account account;

  public AccountUserDetails(Account account, List<GrantedAuthority> authorities) {
    super(account.getEmail(), account.getPassword(), authorities);
    this.account = account;
  }

  public Account getAccount() {
    return account;
  }
}
