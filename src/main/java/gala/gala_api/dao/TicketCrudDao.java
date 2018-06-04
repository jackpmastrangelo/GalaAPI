package gala.gala_api.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import gala.gala_api.entity.Ticket;

/**
 * This class conducts database actions for Ticket entities in the database.
 */
@Component
@Repository
public class TicketCrudDao extends GenericCrudDao<Ticket> {
    public TicketCrudDao() {
        super(Ticket.class);
    }
}