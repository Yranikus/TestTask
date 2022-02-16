package com.example.test.congiguration;


import com.example.test.DAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    @Autowired
    public UserDAO userDAO;


    //Удаление юзеров , которые не клилкали дольше 1 дня. ПРоверка раз в день.
    @Scheduled(cron = "@daily")
    public void dfh(){
        userDAO.deleteNoActiveUsers(new Date());
    }



}
