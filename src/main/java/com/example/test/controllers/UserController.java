package com.example.test.controllers;
import com.example.test.congiguration.CounterOfUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class UserController{

    @Autowired
    private CounterOfUsers counterOfUsers;


    //Основной URL, где сам кликер.
    @GetMapping("/")
    public String index(@CookieValue(name = "username", defaultValue = "0") String username,
                        HttpServletResponse response){
        if (username.equals("0")) {
            counterOfUsers.getNewNameForUser(response);
        }
        return "index";
    }


}
