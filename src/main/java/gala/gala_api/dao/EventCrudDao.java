package gala.gala_api.dao;

import org.springframework.data.repository.CrudRepository;

import gala.gala_api.entity.Event;

/**
 * This repository conducts database actions for Event entities in the database.
 */
public interface EventCrudDao extends CrudRepository<Event, Long> {

}