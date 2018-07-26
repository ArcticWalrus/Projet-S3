package com.example.demo;


import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.json.*;
import maxmamort.gel.persistence.*;

@RestController
public class DeviceController {
    @RequestMapping({"/device", "/device/list"})
    public String listDevice() {
        persistantLayer pl = new persistantLayer();
        JSONArray json = pl.getDevices();
        return json.toString();
    }

    @RequestMapping("/device/create")
    public String createDevice(@RequestParam(value = "cip", defaultValue = "0") String cip,
                               @RequestParam(value = "mac", defaultValue = "0") String mac,
                               @RequestParam(value = "name", defaultValue = "0") String name) {
        if (cip.equalsIgnoreCase("0") || mac.equalsIgnoreCase("0") || name.equalsIgnoreCase("0"))
            return listDevice();

        persistantLayer pl = new persistantLayer();
        pl.createDevice(mac, cip, name);
        return listDevice();
    }

    @RequestMapping("/device/delete")
    public String deleteDevice(@RequestParam(value = "mac", defaultValue = "0") String mac) {
        if (mac.equalsIgnoreCase("0"))
            return listDevice();

        persistantLayer pl = new persistantLayer();
        pl.deleteDevice(mac);
        return listDevice();
    }
}
