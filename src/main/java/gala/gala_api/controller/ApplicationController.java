package gala.gala_api.controller;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import gala.gala_api.dao.EventCrudDao;
import gala.gala_api.entity.Event;

@RestController
@Transactional
public class ApplicationController  {

    @Autowired
    private EventCrudDao eventCrudDao;

    @GetMapping("/")
    public String index() {
        Event newEvent = new Event();
        newEvent.setPlace("NoWhere");
        newEvent.setEventTime(new Date());
        newEvent.setCapacity(500);

        this.eventCrudDao.save(newEvent);

        return "Worked???";
    }

}