package gala.gala_api.service;

import gala.gala_api.dao.EventCrudDao;
import gala.gala_api.entity.Account;
import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;

import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class EventService {

  private EventCrudDao eventCrudDao;

  public Event createEvent(Account account, String name, String place, Date eventTime, int capacity) {
    Event event = new Event();
    event.setAccount(account);
    event.setName(name);
    event.setPlace(place);
    event.setEventTime(eventTime);
    event.setCapacity(capacity);

    eventCrudDao.save(event);

    return event;
  }

  public Optional<Event> findEvent(String eventId) {
    return eventCrudDao.findById(eventId);
  }

  public List<Event> retrieveEventsByAccount(Account account) {
    return eventCrudDao.findAllByAccount(account);
  }

  @Autowired
  public void setEventCrudDao(EventCrudDao eventCrudDao) {
    this.eventCrudDao = eventCrudDao;
  }
}
