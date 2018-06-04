package gala.gala_api.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import gala.gala_api.entity.Event;

/**
 * This class conducts database actions for Event entities in the database.
 */
@Component
@Repository
public class EventCrudDao extends GenericCrudDao<Event> {
    public EventCrudDao() {
        super(Event.class);
    }
}