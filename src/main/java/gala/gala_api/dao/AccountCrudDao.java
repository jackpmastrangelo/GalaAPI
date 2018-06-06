package gala.gala_api.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import gala.gala_api.entity.Account;

/**
 * Spring Data provides the implementation. CAREFUL when altering the method
 * names and signatures.
 */
public interface AccountCrudDao extends CrudRepository<Account, Long> {
  Account findByEmail(String email);
}
