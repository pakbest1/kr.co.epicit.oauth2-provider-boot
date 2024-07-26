<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form method="post"
	action="${pageContext.request.contextPath}${formurl}">
	<input type="hidden" name="return_uri" value="${return_uri}" />

	<div>
		<label for="${username}">Username:</label> <input type="text"
			id="${username}" name="${username}" />
	</div>
	<div>
		<label for="${password}">Password:</label> <input type="password"
			id="${password}" name="${password}" />
	</div>
	<div>
		<button type="submit">Login</button>
	</div>


	<c:if test="${not empty error }">
		<div class="error" style="color: red;">${error }</div>
	</c:if>
	<c:if test="${not empty msg   }">
		<div class="msg" style="color: green;">${msg   }</div>
	</c:if>
	<c:if test="${not empty logout}">
		<div class="logout" style="color: green;">${logout}</div>
	</c:if>

</form>