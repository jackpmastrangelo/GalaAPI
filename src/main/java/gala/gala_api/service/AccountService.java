package gala.gala_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import gala.gala_api.dao.AccountCrudDao;
import gala.gala_api.entity.Account;

@Component
@Transactional
public class AccountService {

  private final AccountCrudDao accountCrudDao;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AccountService(AccountCrudDao accountCrudDao, PasswordEncoder passwordEncoder) {
    this.accountCrudDao = accountCrudDao;
    this.passwordEncoder = passwordEncoder;
  }

  public void createAccount(String email, String firstName, String lastName, String password) {
    Account account = new Account();
    account.setEmail(email);
    account.setFirstName(firstName);
    account.setLastName(lastName);
    account.setPassword(passwordEncoder.encode(password));

    accountCrudDao.save(account);
  }
}
