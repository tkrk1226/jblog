package com.poscoict.jblog.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.poscoict.jblog.interceptor.BlogInterceptor;
import com.poscoict.jblog.security.AuthInterceptor;
import com.poscoict.jblog.security.AuthUserHandlerMethodArgumentResolver;
import com.poscoict.jblog.security.LoginInterceptor;
import com.poscoict.jblog.security.LogoutInterceptor;

@SpringBootConfiguration
@PropertySource("classpath:com/poscoict/jblog/config/WebConfig.properties")
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;
	
	// Argument Resolver
	@Bean
	public HandlerMethodArgumentResolver handlerMethodArgumentResolver() {
		return new AuthUserHandlerMethodArgumentResolver();
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(handlerMethodArgumentResolver());
	}
	
	// Interceptors
	@Bean
	public HandlerInterceptor loginInterceptor() {	
		return new LoginInterceptor();
	}
	
	@Bean
	public HandlerInterceptor logoutInterceptor() {		
		return new LogoutInterceptor();
	}
	
	@Bean
	public HandlerInterceptor authInterceptor() {		
		return new AuthInterceptor();
	}

	@Bean
	public HandlerInterceptor blogInterceptor() {
		return new BlogInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry
		.addInterceptor(loginInterceptor())
		.addPathPatterns("/user/auth");
	
		registry
			.addInterceptor(logoutInterceptor())
			.addPathPatterns("/user/logout");
	
		registry
			.addInterceptor(authInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/user/auth")
			.excludePathPatterns("/user/logout")
			.excludePathPatterns("/assets/**");
		
		registry
		.addInterceptor(blogInterceptor())
		.addPathPatterns("/**")
		.excludePathPatterns("/")
		.excludePathPatterns("/images/**")
		.excludePathPatterns("/assets/**")
		.excludePathPatterns("/user/**")
		.excludePathPatterns("/main/**");
	}
	
	// resource Mapping
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(env.getProperty("fileupload.resourceMapping"))
		.addResourceLocations("file:" + env.getProperty("fileupload.uploadLocation"));		

		registry.addResourceHandler(env.getProperty("assets.resourceMapping"))
		.addResourceLocations("classpath:" + env.getProperty("assets.resourceLocation")); 

	}

	// DefaultServlet Handler
//	@Override
//	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//		configurer.enable();
//	}
	
}
