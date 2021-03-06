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

    @RequestMapping({"Condition/create"})
    public String createCondition(@RequestParam(value = "MAC", defaultValue = "0") String mac,
                                  @RequestParam(value = "pin1", defaultValue = "-1") String pin1,
                                  @RequestParam(value = "pin2", defaultValue = "-1") String pin2,
                                  @RequestParam(value = "pin3", defaultValue = "-1") String pin3,
                                  @RequestParam(value = "operation", defaultValue = "-1") String op){
        persistantLayer pl = new persistantLayer();
        pl.createCondition(mac, Integer.parseInt(pin1), Integer.parseInt(pin2), Integer.parseInt(pin3), Integer.parseInt(op));
        return listCondition();
    }

    @RequestMapping({"Condition/delete"})
    public String deleteCondition(@RequestParam(value = "condition", defaultValue = "0") String condition) {
        System.out.println("Hello");
        if (condition.equalsIgnoreCase("0")) {
            return listCondition();
        }
        persistantLayer pl = new persistantLayer();
        pl.deleteCondition(Integer.parseInt(condition));

        return listCondition();
    }

}
