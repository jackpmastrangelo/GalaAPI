package gala.gala_api.service.security;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import gala.gala_api.entity.Account;
import gala.gala_api.service.AccountService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountLoaderSecurityServiceTest {

  private static final String EMAIL_WITH_ACCOUNT = "valid@example.com";
  private static final String PASSWORD = "password123";

  private static final String EMAIL_WITH_NO_ACCOUNT = "invalid@example.com";

  @Test
  public void testLoadByUsername_EmailWithAccount() {
    Account expectedAccount = new Account();
    expectedAccount.setEmail(EMAIL_WITH_ACCOUNT);
    expectedAccount.setPassword(PASSWORD);

    AccountService mockAccountService = mock(AccountService.class);
    when(mockAccountService.findByEmailIgnoreCase(EMAIL_WITH_ACCOUNT)).thenReturn(Optional.of(expectedAccount));

    AccountLoaderSecurityService service = buildAccountLoaderService(mockAccountService);
    try {
      UserDetails actualUserDetails = service.loadUserByUsername(EMAIL_WITH_ACCOUNT);

      assertEquals(actualUserDetails.getUsername(), EMAIL_WITH_ACCOUNT);
      assertEquals(actualUserDetails.getPassword(), PASSWORD);

      Set<GrantedAuthority> expectedAuthorities = new HashSet<>(AuthorityUtils.createAuthorityList("USER"));
      assertEquals(actualUserDetails.getAuthorities(), expectedAuthorities);
    } catch (UsernameNotFoundException e) {
      fail();
    }
  }

  @Test(expected = UsernameNotFoundException.class)
  public void testLoadByUsername_EmailWithNoAccount() {
    AccountLoaderSecurityService service = buildAccountLoaderService(mock(AccountService.class));
    service.loadUserByUsername(EMAIL_WITH_NO_ACCOUNT);
  }

  private AccountLoaderSecurityService buildAccountLoaderService(AccountService mockAccountService) {
    AccountLoaderSecurityService service = new AccountLoaderSecurityService();
    service.setAccountService(mockAccountService);

    return service;
  }

}