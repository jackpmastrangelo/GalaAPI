package gala.gala_api.controller;

import gala.gala_api.config.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import gala.gala_api.entity.Account;
import gala.gala_api.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  private AccountService accountService;

  private JwtTokenProvider jwtTokenProvider;

  @PostMapping
  @ResponseBody
  public String createAccount(@RequestBody CreateAccountBody body,
                            HttpServletResponse response) {
    if (isValidEmailRegex(body.getEmail())) {
      Optional<Account> existingAccountWithEmail = accountService.findByEmail(body.getEmail());

      if (existingAccountWithEmail.isPresent()) {
        GalaApiSpec.sendError(response, HttpServletResponse.SC_CONFLICT,
                "That email is already in use.");
      } else {
        accountService.createAccount(body.getFirstName(), body.getLastName(), body.getEmail(), body.getPassword());
        return jwtTokenProvider.createToken(body.getEmail());
      }
    } else {
      GalaApiSpec.sendError(response, HttpServletResponse.SC_BAD_REQUEST,
              "The given email parameter is not a valid email.");
    }

    return null;
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public String login(@RequestBody LoginBody body, HttpServletResponse response) {
    if (accountService.validAccount(body.getEmail(), body.getPassword())) {
      return jwtTokenProvider.createToken(body.getEmail());
    } else {
      GalaApiSpec.sendError(response, HttpStatus.FORBIDDEN.value(), "Account credentials were invalid.");
    }

    return null;
  }

  private boolean isValidEmailRegex(String email) {
    String validEmailRegex = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
    Pattern pattern = Pattern.compile(validEmailRegex);
    Matcher matcher = pattern.matcher(email);

    return matcher.matches();
  }

  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  @Autowired
  public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }
}

//TODO Where do these go?
class CreateAccountBody {
  private String firstName;
  private String lastName;
  private String email;
  private String password;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

class LoginBody {
  private String email;
  private String password;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
