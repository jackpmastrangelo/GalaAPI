package gala.gala_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import gala.gala_api.dao.AccountCrudDao;
import gala.gala_api.entity.Account;

import java.util.Optional;

@Component
@Transactional
public class AccountService {

  private AccountCrudDao accountCrudDao;
  private PasswordEncoder passwordEncoder;

  public void createAccount(String firstName, String lastName, String email, String password) {
    Account account = new Account();
    account.setFirstName(firstName);
    account.setLastName(lastName);
    account.setEmail(email);
    account.setPassword(passwordEncoder.encode(password));

    accountCrudDao.save(account);
  }

  public Optional<Account> findById(Long accountId) {
    return accountCrudDao.findById(accountId);
  }

  public Optional<Account> findByEmail(String email) {
    return accountCrudDao.findByEmail(email);
  }

  @Autowired
  public void setAccountCrudDao(AccountCrudDao accountCrudDao) {
    this.accountCrudDao = accountCrudDao;
  }

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }
}
