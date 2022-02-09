package com.poscoict.config.web;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

// Bean만 등록하면 되니까 Web을 안함
// But 개발자의 필요성에 따라 쓸 것

@Configuration
public class MessageConfig {

	// Message Source
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("com/poscoict/mysite/config/web/messages/messages_ko");
		messageSource.setDefaultEncoding("utf-8");
		
		return messageSource;
	} 
	
}
