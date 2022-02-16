package com.example.test;

import com.example.test.DAO.UserDAO;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

//Тесты для рест контроллеров
@RunWith(SpringRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TaskApplicationTests extends Assert {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserDAO userDAO;

	private String user = "default";


	//Добавляем тестируемого пользователя. Внутри бд лежат пользователи с именами , которые представляют из себя случайный набор букв и цифр.
	//Это гарантириует что пользователя с ником default там не будет, если только вручную не изменить значение куки в браузере
	@BeforeAll
	void init(){
		userDAO.addNewUser(user);
	}

	//Проверяем что пользователь был добавлен
	@Test
	public void userExistTest(){
		assertEquals(true, userDAO.userExist(user));
	}


	//Проверяем конроллер отвечающий за обновление кликов в бд.
	@Test
	public void updateClicks(){
		//Сначала проверяем работу класса DAO
		userDAO.updateClicks(10,user);
		assertEquals(10, userDAO.getCkicks(user));
		//Теперь сам контроллер. Добавляем в куки юзернейм и задаем в юрл колличество кликов.
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Cookie","username=" + user);
		restTemplate.exchange("http://localhost:8081/update?counter=50", HttpMethod.PUT,
				new HttpEntity<String>(httpHeaders), byte[].class);
		assertEquals(50, userDAO.getCkicks(user));
	}

	//Проверяем конроллер отвечающий за сброс кликов в бд.
	@Test
	public void resetTest(){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Cookie","username=" + user );
		restTemplate.exchange("http://localhost:8081/reset", HttpMethod.DELETE,
				new HttpEntity<String>(httpHeaders), byte[].class);
		assertEquals(0,userDAO.getCkicks(user));
	}

	//Проверяем конроллер отвечающий за получение кликов из бд.
	@Test
	public void getClicks(){
		HttpHeaders httpHeaders = new HttpHeaders();
		//Добавляем в куки юзернейм и текущие кол-во кликов
		httpHeaders.add("Cookie","username=" + user );
		httpHeaders.add("Cookie","counter=" + 100 );
		ResponseEntity<Integer> responseEntity = restTemplate.exchange("http://localhost:8081/get_number_of_clicks", HttpMethod.GET,
				new HttpEntity<String>(httpHeaders), Integer.class);
		//Суть теста в том , что если на сервер пришло больше кликов , чем сейчас у юзера записано в бд то в ответе на фронт вернется то же число.
		//Если в бд на данного юзера кликов больше (например кука счетчик умерла в браузере) , то вернктся число из бд
		assertEquals(100, responseEntity.getBody().intValue());
	}


	//Удаление юзера после тестов.
	@AfterAll
	public void deleteUser(){
		userDAO.deleteUser(user);
	}



}
