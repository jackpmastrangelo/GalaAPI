package gala.gala_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gala.gala_api.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

  private AccountService accountService;

  @PostMapping
  @ResponseStatus(code = HttpStatus.OK)
  public void createAccount(String email, String firstName, String lastName, String password) {
    accountService.createAccount(email, firstName, lastName, password);
  }

  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }
}
