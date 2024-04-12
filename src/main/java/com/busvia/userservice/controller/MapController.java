package com.busvia.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    @GetMapping("/showMap")
    @CrossOrigin(origins = "http://localhost:4200")
    public String index() {
        return "index";
    }
}