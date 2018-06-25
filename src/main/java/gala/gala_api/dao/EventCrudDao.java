package gala.gala_api.dao;

import gala.gala_api.entity.Account;
import org.springframework.data.repository.CrudRepository;

import gala.gala_api.entity.Event;

import java.util.List;

/**
 * This repository conducts database actions for Event entities in the database.
 */
public interface EventCrudDao extends CrudRepository<Event, String> {
  List<Event> findAllByAccount(Account account); //TODO Inconsistency between find and findAll, see TicketCrudDao
}