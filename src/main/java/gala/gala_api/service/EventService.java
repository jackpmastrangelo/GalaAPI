package gala.gala_api.service;

import gala.gala_api.dao.EventCrudDao;
import gala.gala_api.entity.Account;
import gala.gala_api.entity.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {

  private EventCrudDao eventCrudDao;

  public Event createEvent(Account account, String name, String place, Date startTime, Date endTime, int capacity,
                           String description) {
    Event event = new Event();
    event.setAccount(account);
    event.setName(name);
    event.setPlace(place);
    event.setStartTime(startTime);
    event.setEndTime(endTime);
    event.setCapacity(capacity);
    event.setDescription(description);

    eventCrudDao.save(event);

    return event;
  }

  public Optional<Event> findEvent(String eventId) {
    return eventCrudDao.findById(eventId);
  }

  public List<Event> retrieveEventsByAccount(Account account) {
    return eventCrudDao.findByAccount(account);
  }

  @Autowired
  public void setEventCrudDao(EventCrudDao eventCrudDao) {
    this.eventCrudDao = eventCrudDao;
  }
}
