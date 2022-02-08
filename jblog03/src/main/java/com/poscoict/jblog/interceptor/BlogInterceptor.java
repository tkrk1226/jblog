package com.poscoict.jblog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.vo.BlogVo;

public class BlogInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private BlogService blogService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uri = request.getRequestURI();
		String blogId = uri.split("/")[2];
		BlogVo blogVo = blogService.getBlog(blogId);
		if (blogVo == null) {
			response.sendRedirect(request.getContextPath());
			return false;
		}		
		request.setAttribute("blogVo", blogVo);
		return true;
	}
}