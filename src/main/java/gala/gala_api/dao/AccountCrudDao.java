package gala.gala_api.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import gala.gala_api.entity.Account;

@Component
@Repository
public class AccountCrudDao extends GenericCrudDao<Account> {
  public AccountCrudDao() {
    super(Account.class);
  }
}
