package gala.gala_api.controller;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import gala.gala_api.entity.Account;
import gala.gala_api.service.AccountService;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountControllerTest {

  private static final String FIRST_NAME = "bob";
  private static final String LAST_NAME = "eventplanner";
  private static final String PASSWORD = "galaIsAwesome";

  private static final String VALID_EMAIL = "bob@example.com";
  private static final String INVALID_EMAIL = "notanemail";


  @Test
  public void testCreateAccount_ValidEmail() {
    AccountController controller = new AccountController();

    AccountService mockAccountService = mock(AccountService.class);
    //This is actually default behavior but is left in for logical clarity
    when(mockAccountService.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
    controller.setAccountService(mockAccountService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    controller.createAccount(FIRST_NAME, LAST_NAME, VALID_EMAIL, PASSWORD, mockResponse);

    verify(mockAccountService).createAccount(FIRST_NAME, LAST_NAME, VALID_EMAIL, PASSWORD);
    assertEquals(HttpServletResponse.SC_OK, mockResponse.getStatus());
  }

  @Test
  public void testCreateAccount_InvalidEmail() {
    AccountController controller = new AccountController();
    AccountService mockAccountService = mock(AccountService.class);
    controller.setAccountService(mockAccountService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    controller.createAccount(FIRST_NAME, LAST_NAME, INVALID_EMAIL, PASSWORD, mockResponse);

    verify(mockAccountService, never()).createAccount(anyString(), anyString(), anyString(), anyString());
    assertEquals(HttpServletResponse.SC_BAD_REQUEST, mockResponse.getStatus());
  }

  @Test
  public void testCreateAccount_EmailAlreadyInUse() {
    AccountController controller = new AccountController();

    AccountService mockAccountService = mock(AccountService.class);
    when(mockAccountService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(new Account()));
    controller.setAccountService(mockAccountService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    controller.createAccount(FIRST_NAME, LAST_NAME, VALID_EMAIL, PASSWORD, mockResponse);

    verify(mockAccountService, never()).createAccount(anyString(), anyString(), anyString(), anyString());
    assertEquals(HttpServletResponse.SC_CONFLICT, mockResponse.getStatus());
  }
}