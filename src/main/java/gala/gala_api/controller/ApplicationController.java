package gala.gala_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController  {

    @GetMapping("/")
    public String index() {
        return "<h1> Obligatory: Hello World! </h1>";
    }

}