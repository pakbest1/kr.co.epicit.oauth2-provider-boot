<%@page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Login</title>
</head>
<body>
	<h2>Login Page</h2>
	<form:form
		action="${pageContext.request.contextPath}/j_spring_security_check"
		method="POST">
		<form:label path="username">Username:</form:label>
		<form:input path="j_username" />
		<br />
		<form:label path="password">Password:</form:label>
		<form:password path="j_password" />
		<br />
		<input type="submit" value="Login" />
		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>
	</form:form>
</body>
</html>