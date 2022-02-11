package com.poscoict.jblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JblogApplication {
	public static void main(String[] args) {
//		try - with - resource 는 Console Application 에서만 써야한다.
//		tomcat, container, application 생성, 소멸에 관한 개념을 찾아서 쓰지 말아야하는 이유에 대해 정리할 것
		
		SpringApplication.run(JblogApplication.class, args);
	}
}
