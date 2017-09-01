package com.example.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping(value = "/announcements/query", method = RequestMethod.GET)
    @ResponseBody
    public String queryAnnouncement() {
        return getAdminAnnouncement() + getUserAnnouncement();
    }

    @PreAuthorize("hasRole('ADMIN')")
    String getAdminAnnouncement() {
        return "admin get announcement";
    }

    @PreAuthorize("hasRole('USER')")
    String getUserAnnouncement() {
        return "user get announcement";
    }




    @RequestMapping(value = "/announcements/add", method = RequestMethod.GET)
    @ResponseBody
    public String addAnnouncement() {
        return "add and add and add";
    }



    @RequestMapping("/login")
    @ResponseBody
    public String login() {
        return "login";
    }

    @RequestMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "index";
    }

}
