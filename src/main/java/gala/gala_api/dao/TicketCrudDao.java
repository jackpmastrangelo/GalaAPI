package gala.gala_api.dao;

import gala.gala_api.entity.Event;
import org.springframework.data.repository.CrudRepository;

import gala.gala_api.entity.Ticket;

import java.util.List;

/**
 * This class conducts database actions for Ticket entities in the database.
*/
public interface TicketCrudDao extends CrudRepository<Ticket, Long> {
  List<Ticket> findByEvent(Event event);
}