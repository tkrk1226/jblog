<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<ul class="menu">
	<c:choose>
		<c:when test="${empty authUser}">
			<li><a href="${pageContext.request.contextPath}/user/login">로그인</a></li>
			<li><a href="${pageContext.request.contextPath}/user/join">회원가입</a></li>		
		</c:when>
		<c:otherwise>
			<li><a href="${pageContext.request.contextPath}/user/logout">로그아웃</a></li>
			<li><a href="${pageContext.request.contextPath}/${authUser.id}">내블로그</a></li>	
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test="${empty authUser}">
			<li>Guest님 안녕하세요. ^^</li>
		</c:when>
		<c:otherwise>
			<li>${authUser.name}님 안녕하세요. ^^</li>
		</c:otherwise>
	</c:choose>
</ul>