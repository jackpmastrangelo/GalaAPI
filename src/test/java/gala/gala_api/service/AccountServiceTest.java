package gala.gala_api.service;

import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import gala.gala_api.dao.AccountCrudDao;
import gala.gala_api.entity.Account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

  private static final Long ACCOUNT_ID = 1L;

  private static final String FIRST_NAME = "alice";
  private static final String LAST_NAME = "eventGoer";
  private static final String EMAIL = "alice@example.com";
  private static final String PASSWORD = "password";
  private static final String FAKE_ENCODED_PASSWORD = "fake encoded password";


  @Test
  public void testCreateAccount() {
    AccountService service = new AccountService();

    AccountCrudDao mockAccountCrudDao = mock(AccountCrudDao.class);
    service.setAccountCrudDao(mockAccountCrudDao);

    PasswordEncoder mockPasswordEncoder = mock(PasswordEncoder.class);
    when(mockPasswordEncoder.encode(PASSWORD)).thenReturn(FAKE_ENCODED_PASSWORD);
    service.setPasswordEncoder(mockPasswordEncoder);

    service.createAccount(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);

    Account expectedSavedAccount = new Account();
    expectedSavedAccount.setFirstName(FIRST_NAME);
    expectedSavedAccount.setLastName(LAST_NAME);
    expectedSavedAccount.setEmail(EMAIL);
    expectedSavedAccount.setPassword(FAKE_ENCODED_PASSWORD);

    verify(mockAccountCrudDao).save(expectedSavedAccount);
  }

  @Test
  public void testFindById() {
    AccountService service = new AccountService();

    Account expectedAccount = new Account();
    expectedAccount.setId(ACCOUNT_ID);

    AccountCrudDao mockAccountCrudDao = mock(AccountCrudDao.class);
    when(mockAccountCrudDao.findById(ACCOUNT_ID)).thenReturn(Optional.of(expectedAccount));
    service.setAccountCrudDao(mockAccountCrudDao);

    Optional<Account> actualAccount = service.findById(ACCOUNT_ID);
    assertEquals(Optional.of(expectedAccount), actualAccount);
  }

  @Test
  public void testFindByEmail() {
    AccountService service = new AccountService();

    Account expectedAccount = new Account();
    expectedAccount.setEmail(EMAIL);

    AccountCrudDao mockAccountCrudDao = mock(AccountCrudDao.class);
    when(mockAccountCrudDao.findByEmail(EMAIL)).thenReturn(Optional.of(expectedAccount));
    service.setAccountCrudDao(mockAccountCrudDao);

    Optional<Account> actualAccount = service.findByEmail(EMAIL);
    assertEquals(Optional.of(expectedAccount), actualAccount);
  }
}