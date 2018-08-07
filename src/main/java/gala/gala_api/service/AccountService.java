package gala.gala_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import gala.gala_api.dao.AccountCrudDao;
import gala.gala_api.entity.Account;

import java.util.Optional;

@Service
@Transactional
public class AccountService {

  private AccountCrudDao accountCrudDao;
  private PasswordEncoder passwordEncoder;
  private AuthenticationManager authenticationManager;

  public void createAccount(String firstName, String lastName, String email, String password) {
    Account account = new Account();
    account.setFirstName(firstName);
    account.setLastName(lastName);
    account.setEmail(email);
    account.setPassword(passwordEncoder.encode(password));

    accountCrudDao.save(account);
  }

  public boolean validAccount(String email, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
      return true;
    } catch (Exception e) {
      throw e;
    }
  }

  public Optional<Account> findById(String accountId) {
    return accountCrudDao.findById(accountId);
  }

  public Optional<Account> findByEmailIgnoreCase(String email) {
    return accountCrudDao.findByEmailIgnoreCase(email);
  }

  @Autowired
  public void setAccountCrudDao(AccountCrudDao accountCrudDao) {
    this.accountCrudDao = accountCrudDao;
  }

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }
}
