package com.example.test.controllers;


import com.example.test.DAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/")
public class RestContr {

    @Autowired
    private UserDAO userDAO;


    //Обновление кликов в бд.
    @PutMapping("/update")
    public void updateClicks(@RequestParam(name = "counter") Integer c,
                             @CookieValue(name = "username", defaultValue = "none") String username){
        userDAO.updateClicks(c,username);
    }

    //Получение браузером кол-ва кликов из БД, если кука счетчика умерала
    @GetMapping("/get_number_of_clicks")
    public Integer getNubmerOfClicks(@CookieValue(name = "username", defaultValue = "none") String username,
                                     @CookieValue(name = "counter", defaultValue = "none") Integer c){
        int DBint = userDAO.getCkicks(username);
        if (DBint > c){
            return DBint;
        }
        else return c;
    }

    //Сброс счетчика
    @DeleteMapping("/reset")
    public void resetCounter(@CookieValue(name = "username", defaultValue = "none") String username){
        userDAO.reset(username);
    }

}
