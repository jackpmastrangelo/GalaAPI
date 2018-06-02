package gala.gala_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController  {

    @RequestMapping("/")
    public String index() {
        return "Obligatory: Hello World!";
    }

}