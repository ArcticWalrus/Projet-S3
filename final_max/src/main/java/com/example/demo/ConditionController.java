package com.example.demo;


import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.json.*;
import maxmamort.gel.persistence.*;

@RestController
public class ConditionController {
    @RequestMapping({"/Condition", "/Condition/list"})
    public String listCondition() {
        persistantLayer pl = new persistantLayer();
        JSONArray json = pl.getConditions();
        return json.toString();
    }

    //TODO create condition
    //TODO delete condition
}
