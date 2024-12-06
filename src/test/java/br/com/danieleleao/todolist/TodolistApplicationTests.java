package br.com.danieleleao.todolist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TodolistApplicationTests {

	@Test
	void contextLoads() {

		String fistName = "Kleber ";
		String lastName = "0 bom.";

		System.out.println("Olá Mundo, Bem vindo ao curso: " + fistName + " Martins " + lastName);

		System.out.println("Fim.");
	}

}
