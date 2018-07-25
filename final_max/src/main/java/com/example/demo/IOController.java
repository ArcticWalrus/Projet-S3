package com.example.demo;


import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.json.*;
import maxmamort.gel.persistence.*;

@RestController
public class IOController {
    @RequestMapping({"/IO", "/IO/list"})
    public String listIO() {
        persistantLayer pl = new persistantLayer();
        JSONArray json = pl.getIo();
        return json.toString();
    }
    //TODO add IO

    @RequestMapping("/io/delete")
    public String deleteIO(@RequestParam(value = "mac", defaultValue = "0") String mac, @RequestParam(value = "pinid", defaultValue = "0") String pinid) {
        if (mac.equalsIgnoreCase("0") || pinid.equalsIgnoreCase("0")) {
            return listIO();
        }
        persistantLayer pl = new persistantLayer();
        pl.deleteIO(mac, Integer.parseInt(pinid));
        return listIO();
    }

    @RequestMapping({"io/create"})
    public String createIO(@RequestParam(value = "IoName", defaultValue = "0") String IOname,
                           @RequestParam(value = "DeviceId", defaultValue = "-1") String DeviceID,
                           @RequestParam(value = "physicalPinMapping", defaultValue = "-1") String physicalPin,
                           @RequestParam(value = "configurationBit", defaultValue = "-1") String configurationbit){
        if (IOname.equalsIgnoreCase("0") || DeviceID.equalsIgnoreCase("-1") || physicalPin.equalsIgnoreCase("-1") || configurationbit.equalsIgnoreCase("-1"))
            return listIO();
        persistantLayer pl = new persistantLayer();
        pl.createIO(IOname,DeviceID, Integer.parseInt(physicalPin), Integer.parseInt(configurationbit));

        return listIO();
    }
}
