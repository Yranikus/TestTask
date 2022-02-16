package com.example.test.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean userExist(String username){
        try {
            jdbcTemplate.queryForObject("SELECT username FROM USERS WHERE username=?", new Object[]{username}, String.class);
            return true;
        }
        catch (EmptyResultDataAccessException e){
        }
        return false;
    }

    public void addNewUser(String username){
        jdbcTemplate.update("INSERT INTO users(username, clicknumber, date) VALUES (?, ?, ?)", username, 0, new Date());
    }

    public void updateClicks(int clicks, String username){
        jdbcTemplate.update("UPDATE users SET clicknumber=?, date=? WHERE username=?", clicks, new Date(), username);
    }

    public int getCkicks(String username){
        return jdbcTemplate.queryForObject("SELECT clicknumber FROM users WHERE username=?",new Object[]{username}, Integer.class);
    }


    public void reset(String username){
        jdbcTemplate.update("UPDATE users SET clicknumber=0, date=? WHERE username=?", new Date(), username);
    }

    public void deleteUser(String username){
        jdbcTemplate.update("DELETE FROM users WHERE username=?", username);
    }


    public void deleteNoActiveUsers(Date date){
        jdbcTemplate.update("DELETE FROM users WHERE ABS(date_part('day', age(date, ?))) >= 1", date);
    }

}
