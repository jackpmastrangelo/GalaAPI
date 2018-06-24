package gala.gala_api.service;


import gala.gala_api.dao.EventCrudDao;
import gala.gala_api.entity.Account;
import gala.gala_api.entity.Event;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventServiceTest {

  @Test
  public void testCreateEvent() {
    EventService eventService = new EventService();
    EventCrudDao eventCrudDao = mock(EventCrudDao.class);

    Account account = new Account();

    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.JUNE, 17);

    eventService.setEventCrudDao(eventCrudDao);
    Event event = eventService.createEvent(account, "AIDACA", "Acadia", calendar.getTime(), 16 );

    assertEquals("AIDACA", event.getName());
    assertEquals("Acadia", event.getPlace());
    assertEquals(event.getEventTime().getTime(), calendar.getTime().getTime());
    assertEquals( 16, (int) event.getCapacity());
  }

  @Test
  public void testFindEvent() {
    EventService eventService = new EventService();
    EventCrudDao eventCrudDao = mock(EventCrudDao.class);

    Event event = new Event();

    when(eventCrudDao.findById("ID")).thenReturn(Optional.of(event));
    eventService.setEventCrudDao(eventCrudDao);

    assertEquals(event, eventService.findEvent("ID").get());
  }

  @Test
  public void testRetriveEventsByAccount() {
    EventService eventService = new EventService();
    EventCrudDao eventCrudDao = mock(EventCrudDao.class);

    Account account = new Account();
    Event event1 = new Event();
    Event event2 = new Event();

    when(eventCrudDao.findAllByAccount(account)).thenReturn(Arrays.asList(event1, event2));
    eventService.setEventCrudDao(eventCrudDao);

    List<Event> eventList = eventService.retrieveEventsByAccount(account);
    assertEquals(event1, eventList.get(0)); //TODO event1.equals(event2) is true
    assertEquals(event2, eventList.get(1));
    assertEquals(2, eventList.size());
  }
}
