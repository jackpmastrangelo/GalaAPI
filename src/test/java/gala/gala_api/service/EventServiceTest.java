package gala.gala_api.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import gala.gala_api.dao.EventCrudDao;
import gala.gala_api.entity.Event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {

  private static final Long EVENT_ID = 1L;

  @Test
  public void testGetEvent(){
    EventCrudDao mockEventDao = mock(EventCrudDao.class);
    when(mockEventDao.findById(EVENT_ID)).thenReturn(buildFakeEvent(EVENT_ID));

    EventService service = new EventService(mockEventDao);

    Optional<Event> actualEvent = service.getEvent(EVENT_ID);
    Assert.assertEquals(buildFakeEvent(EVENT_ID), actualEvent);
  }

  private Optional<Event> buildFakeEvent(Long id) {
    Event event = new Event();
    event.setId(id);

    return Optional.of(event);
  }

}