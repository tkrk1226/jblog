<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<% pageContext.setAttribute("newline", "\n"); %>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"/>	
		<div id="wrapper">
			<div id="content">
				<div class="blog-content">
					<h4>${postVo.title}</h4>
					<p>
						${fn:replace(postVo.contents, newline, "<br/>") }
					<p>
				</div>
				<c:choose>
					<c:when test="${fn:length(postList) > 0}">
						<ul class="blog-list">
							<c:forEach items="${postList}" var="vo" varStatus="status">
								<li><a href="${pageContext.request.contextPath}/${blogVo.userId}/${vo.categoryNo}/${vo.no}">${vo.title}</a> <span>${vo.regDate}</span>	</li>
							</c:forEach>
						</ul>
					</c:when>
					<c:otherwise>
						<h1>작성된 글이 없습니다...</h1>
					</c:otherwise>
				</c:choose>
				

			</div>
		</div>

		<div id="extra">
			<div class="blog-logo">
				<img src="${pageContext.request.contextPath}${blogVo.logo}">
			</div>
		</div>

		<div id="navigation">
			<h2>카테고리</h2>
			<ul>
				<c:forEach items="${categorylist}" var="vo" varStatus="status">
					<li><a href="${pageContext.request.contextPath}/${blogVo.userId}/${vo.no}">${vo.name}</a></li>
				</c:forEach>
			</ul>
		</div>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>	
	</div>
</body>
</html>