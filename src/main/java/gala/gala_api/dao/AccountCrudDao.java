package gala.gala_api.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gala.gala_api.entity.Account;

/**
 * Spring Data provides the implementation.
 * CAREFUL when altering the method names and signatures as these determine the implementation.
 */
public interface AccountCrudDao extends CrudRepository<Account, String> {
  Optional<Account> findByEmailIgnoreCase(String email);
}
