package com.example.test.congiguration;

import com.example.test.DAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@Component
public class CounterOfUsers {


    @Autowired
    private UserDAO userDAO;

    public CounterOfUsers(){
    }


    //Генератор юзернеймов
    private String getNewNumber(){
        int leftLimit = 48; //  '0'
        int rightLimit = 122; //  'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    //Создание новго юзера
    public boolean getNewNameForUser(HttpServletResponse response){
        String username = getNewNumber();
        //Если такой уже есть , что маловероятно , новый добавлен не будет.
        if( userDAO.userExist(username) ) return false;
        userDAO.addNewUser(username);
        //Присвоение юзернейма юзеру при помощи куки
        Cookie cookie = new Cookie("username", username);
        //Время жизни в браузере юзернейма.
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);
        return true;
    }

}
