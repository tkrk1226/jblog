package com.poscoict.jblog.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//import com.poscoict.jblog.security.Auth;
import com.poscoict.jblog.vo.UserVo;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1. handler 종류 확인
		if (handler instanceof HandlerMethod == false) {
			return true; // css, ... 등은 검사할 필요 없이 그냥 지나가게
		}

		// 2. casting (default servlet handler가 아닌 method handler로 올 수 있게)
		// 물론 assets에 들어가지 않게 해놔서 default servlet handler가 들어오지는 않지만
		// 어떤 상황이 생길지 모름

		HandlerMethod handlerMethod = (HandlerMethod) handler;

		// 3. Handler Method의 @Auth 받아오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

		// 5. type과 method에 @Auth 가 적용이 안되어 있는 경우
		if (auth == null) {
			return true;
		}

		// 5. @Auth가 적용이 되어 있기 때문에 인증(Authentication) 여부 확인
		HttpSession session = request.getSession();
		if (session == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}

		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}

		return true;
	}
}
