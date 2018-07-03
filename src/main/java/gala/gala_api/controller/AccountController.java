package gala.gala_api.controller;

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

  @PostMapping
  @ResponseBody
  public void createAccount(@RequestParam String firstName, @RequestParam String lastName,
                            @RequestParam String email, @RequestParam String password,
                            HttpServletResponse response) {
    if (isValidEmailRegex(email)) {
      Optional<Account> existingAccountWithEmail = accountService.findByEmail(email);

      if (existingAccountWithEmail.isPresent()) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
      } else {
        accountService.createAccount(firstName, lastName, email, password);
      }
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public void login() {}

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
}
