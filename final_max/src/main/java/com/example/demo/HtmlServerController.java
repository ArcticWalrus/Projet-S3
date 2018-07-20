package com.example.demo;


import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.json.*;
import maxmamort.gel.persistence.*;

@RestController
public class HtmlServerController {

    @RequestMapping("/")
    public String homePage() {
        return "/home.html";
    }

}
