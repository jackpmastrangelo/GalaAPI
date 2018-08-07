package gala.gala_api.controller;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import gala.gala_api.config.security.JwtTokenProvider;
import gala.gala_api.entity.Account;
import gala.gala_api.service.AccountService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountControllerTest {

  private static final String FIRST_NAME = "bob";
  private static final String LAST_NAME = "eventplanner";
  private static final String PASSWORD = "galaIsAwesome";

  private static final String VALID_EMAIL = "bob@example.com";
  private static final String INVALID_EMAIL = "notanemail";

  private static final String FAKE_JWT_TOKEN = "token";

  @Test
  public void testCreateAccount_ValidEmail() {
    AccountController controller = new AccountController();

    AccountService mockAccountService = mock(AccountService.class);
    //This is actually default behavior but is left in for logical clarity
    when(mockAccountService.findByEmailIgnoreCase(VALID_EMAIL)).thenReturn(Optional.empty());
    controller.setAccountService(mockAccountService);

    JwtTokenProvider mockTokenProvider = mock(JwtTokenProvider.class);
    when(mockTokenProvider.createToken(VALID_EMAIL)).thenReturn(FAKE_JWT_TOKEN);
    controller.setJwtTokenProvider(mockTokenProvider);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    CreateAccountBody requestBody = buildCreateAccountBody(FIRST_NAME, LAST_NAME, VALID_EMAIL, PASSWORD);
    String actualJwtToken = controller.createAccount(requestBody, mockResponse);

    assertEquals(FAKE_JWT_TOKEN, actualJwtToken);
    assertEquals(HttpServletResponse.SC_OK, mockResponse.getStatus());
    verify(mockAccountService).createAccount(FIRST_NAME, LAST_NAME, VALID_EMAIL, PASSWORD);
  }

  @Test
  public void testCreateAccount_InvalidEmail() {
    AccountController controller = new AccountController();
    AccountService mockAccountService = mock(AccountService.class);
    controller.setAccountService(mockAccountService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    controller.createAccount(buildCreateAccountBody(FIRST_NAME, LAST_NAME, INVALID_EMAIL, PASSWORD), mockResponse);

    verify(mockAccountService, never()).createAccount(anyString(), anyString(), anyString(), anyString());
    assertEquals(HttpServletResponse.SC_BAD_REQUEST, mockResponse.getStatus());
  }

  @Test
  public void testCreateAccount_EmailAlreadyInUse() {
    AccountController controller = new AccountController();

    AccountService mockAccountService = mock(AccountService.class);
    when(mockAccountService.findByEmailIgnoreCase(VALID_EMAIL)).thenReturn(Optional.of(new Account()));
    controller.setAccountService(mockAccountService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    controller.createAccount(buildCreateAccountBody(FIRST_NAME, LAST_NAME, VALID_EMAIL, PASSWORD), mockResponse);

    verify(mockAccountService, never()).createAccount(anyString(), anyString(), anyString(), anyString());
    assertEquals(HttpServletResponse.SC_CONFLICT, mockResponse.getStatus());
  }

  @Test
  public void testLogin_ValidCredentials() {
    AccountController controller = new AccountController();

    JwtTokenProvider mockTokenProvider = mock(JwtTokenProvider.class);
    when(mockTokenProvider.isValidAccount(VALID_EMAIL, PASSWORD)).thenReturn(true);
    when(mockTokenProvider.createToken(VALID_EMAIL)).thenReturn(FAKE_JWT_TOKEN);
    controller.setJwtTokenProvider(mockTokenProvider);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    String actualToken = controller.login(buildLoginBody(VALID_EMAIL, PASSWORD), mockResponse);

    assertEquals(FAKE_JWT_TOKEN, actualToken);
  }

  @Test
  public void testLogin_InvalidCredentials() {
    AccountController controller = new AccountController();

    JwtTokenProvider mockTokenProvider = mock(JwtTokenProvider.class);
    when(mockTokenProvider.isValidAccount(INVALID_EMAIL, PASSWORD)).thenReturn(false);
    controller.setJwtTokenProvider(mockTokenProvider);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    String actualToken = controller.login(buildLoginBody(INVALID_EMAIL, PASSWORD), mockResponse);

    assertNull(actualToken);
    assertEquals(HttpServletResponse.SC_FORBIDDEN, mockResponse.getStatus());
  }

  private CreateAccountBody buildCreateAccountBody(String firstName, String lastName, String email, String password) {
    CreateAccountBody body = new CreateAccountBody();
    body.setFirstName(firstName);
    body.setLastName(lastName);
    body.setEmail(email);
    body.setPassword(password);

    return body;
  }

  private LoginBody buildLoginBody(String email, String password) {
    LoginBody body = new LoginBody();
    body.setEmail(email);
    body.setPassword(password);

    return body;
  }
}