package com.poscoict.jblog.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

// AOP
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Log LOGGER = LogFactory.getLog(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public String ExceptionHandler(Model model, Exception e) {
		// 1. 로깅
		// 나중에 String으로 뽑아서 파일로 저장해야한다.
		// 혹은 서비스 론칭 전에 개발용으로 할때는 
		// Error 내용을 뿌리는 것도 나쁘지 않음
		
//		System.out.println("=================================================");
//		System.out.println("error type : " + e.getClass().getSimpleName());
//		if(e instanceof NoHandlerFoundException) 
//			System.out.println("true");
//		System.out.println("=================================================");
		
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		// 추후 로깅을 위함
		LOGGER.error(errors.toString()); 
		
		// 화면에 뿌리던 내용 다 가진 상태
		model.addAttribute("exception", errors.toString());
		
		// 2. 사과 페이지 (HTML 응답, 정상 종료) 
		return "error/exception";
	}
	
//	@ExceptionHandler(NoHandlerFoundException.class)
//	   public String Exception404() {
//	      return "error/404";
//	   }
}





