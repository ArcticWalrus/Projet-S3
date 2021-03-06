package com.example.demo;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/secured")
public class SecuredPageController {
    @GetMapping
    public String index(ModelMap modelMap) {
        System.out.println("Got in secured controller");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.isAuthenticated()){
            System.out.println("Authentified");
        }else{
            System.out.println("Not authentified");
        }
        if( auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserDetails) {
            modelMap.put("username", ((UserDetails) auth.getPrincipal()).getUsername());
            System.out.println("Auth is not null " +  ((UserDetails) auth.getPrincipal()).getUsername());
        }
        //return "/home";
        return "secure/index.html";
    }
}
