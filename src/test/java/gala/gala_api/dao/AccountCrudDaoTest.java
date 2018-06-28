package gala.gala_api.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import gala.gala_api.entity.Account;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountCrudDaoTest {

  @Autowired
  TestEntityManager testEntityManager;

  @Autowired
  AccountCrudDao accountCrudDao;

  @Test
  public void testFindByEmail() {
    String email = "gala@example.com";
    Account account = new Account();
    account.setFirstName("firstname");
    account.setLastName("lastname");
    account.setEmail(email);
    account.setPassword("password");
    testEntityManager.persist(account);

    assertEquals(Optional.of(account), accountCrudDao.findByEmail(email));
  }
}