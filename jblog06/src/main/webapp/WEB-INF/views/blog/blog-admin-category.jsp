<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
			<div id="content" class="full-screen">
				<ul class="admin-menu">
					<li><a href="${pageContext.request.contextPath}/${authUser.id}/admin/basic">기본설정</a></li>
					<li class="selected">카테고리</li>
					<li><a href="${pageContext.request.contextPath}/${authUser.id}/admin/write">글작성</a></li>
				</ul>
		      	<table class="admin-cat">
		      		<tr>
		      			<th>번호</th>
		      			<th>카테고리명</th>
		      			<th>포스트 수</th>
		      			<th>설명</th>
		      			<th>삭제</th>      			
		      		</tr>
					 
					<c:forEach items="${categoryList}" var="vo" varStatus="status">
						<tr>
							<td>${status.count}</td>
							<td>
								<a href="${pageContext.request.contextPath}/${authUser.id}/admin/category/${vo.no}">
									${vo.name}
								</a>
							</td>
							<td>${vo.postCount}</td>
							<td>${vo.description}</td>
							<td>
								<c:if test="${categoryCount > 1 && vo.postCount == 0}">
									<a href="${pageContext.request.contextPath}/${authUser.id}/admin/category/delete/${vo.no}">
										<img src="${pageContext.request.contextPath}/assets/images/delete.jpg">
									</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>	
									  
				</table>
      	
      			<h4 class="n-c">새로운 카테고리 추가</h4>
		      	<form method="post" action="${pageContext.request.contextPath}/${authUser.id}/admin/category/add" >
		      		<input type="hidden" name="blogId" value="${authUser.id}">
		      		<table id="admin-cat-add">
			      		<tr>
			      			<td class="t">카테고리명</td>
			      			<td><input type="text" name="name" required></td>
			      		</tr>
			      		<tr>
			      			<td class="t">설명</td>
			      			<td><input type="text" name="description" required></td>
			      		</tr>
			      		<tr>
			      			<td class="s">&nbsp;</td>
			      			<td><input type="submit" value="카테고리 추가"></td>
			      		</tr>
		      		</table> 
		      	</form>
		      	<table class="admin-cat">
		      		
		      		<c:if test="${fn:length(postList) > 0}">
			      		<tr>
			      			<th>글 제목</th>
			      			<th>삭제</th>      			
			      		</tr>
		      		</c:if>
					<c:forEach items="${postList}" var="vo" varStatus="status">
					<tr>
						<td>
							${vo.title}
						</td>
						<td>				
							<a href="${pageContext.request.contextPath}/${authUser.id}/admin/category/delete/${vo.categoryNo}/${vo.no}">
								<img src="${pageContext.request.contextPath}/assets/images/delete.jpg">
							</a>
						</td>
					</tr>
					</c:forEach>
				</table>
		      
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>	
	</div>
</body>
</html>