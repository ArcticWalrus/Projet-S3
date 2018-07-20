package com.example.demo;


import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.json.*;
import maxmamort.gel.persistence.*;

@RestController
public class UserController {
    @RequestMapping("/user/create")
    public String createUser(@RequestParam(value = "cip", defaultValue = "0") String cip) {
        if(cip.equalsIgnoreCase("0")){
            return listUser();
        }
        persistantLayer pl = new persistantLayer();
        pl.createUser(cip);
        return listUser();
    }

    @RequestMapping("/user/delete")
    public String deleteUser(@RequestParam(value = "cip", defaultValue = "0") String cip) {
        if(cip.equalsIgnoreCase("0")){
            return listUser();
        }
        persistantLayer pl = new persistantLayer();
        pl.deleteUser(cip);
        return listUser();
    }

    @RequestMapping({"/user", "/user/list"})
    public String listUser() {
        persistantLayer pl = new persistantLayer();
        JSONArray json = pl.getUsers();
        return json.toString();
    }
}
