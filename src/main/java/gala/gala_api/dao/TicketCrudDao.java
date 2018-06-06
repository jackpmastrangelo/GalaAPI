package gala.gala_api.dao;

import org.springframework.data.repository.CrudRepository;

import gala.gala_api.entity.Ticket;

/**
 * This class conducts database actions for Ticket entities in the database.
*/
public interface TicketCrudDao extends CrudRepository<Ticket, Long> {
}