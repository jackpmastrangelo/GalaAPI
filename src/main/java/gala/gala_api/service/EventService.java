package gala.gala_api.service;

import gala.gala_api.dao.EventCrudDao;
import gala.gala_api.entity.Account;
import gala.gala_api.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional
public class EventService {

  @Autowired
  private EventCrudDao eventCrudDao;

  public void createEvent(
      Account account,
      String name,
      String place,
      Date eventTime,
      int capacity
  ) {
    Event created = new Event();
    created.setAccount(account);
    created.setName(name);
    created.setPlace(place);
    created.setEventTime(eventTime);
    created.setCapacity(capacity);

    eventCrudDao.save(created);
  }

  public List<Event> getUserEvents(long userId) {
    Iterable<Event> allEvents = eventCrudDao.findAll();

    //Commenting out for now because Iterables do not suppoer stream. Will have to be changed somehow.
    /**
    return allEvents.stream().filter(event -> event.getAccount().getId() == userId)
        .collect(toList());
     */

    return null;
  }

  public Event getEvent(long eventId) {
    return eventCrudDao.findById(eventId)
        .orElseThrow(() ->
            new IllegalStateException(String.format("Event with id %d does not exist", eventId)));
  }
}
