<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<style>
.bd-placeholder-img {
	font-size: 1.125rem;
	text-anchor: middle;
	-webkit-user-select: none;
	-moz-user-select: none;
	user-select: none;
}

@media ( min-width : 768px) {
	.bd-placeholder-img-lg {
		font-size: 3.5rem;
	}
}
</style>

<div class="div-center text-center">
	<c:if test="${empty principal.loginId }">
		<div class="div-login-form">
			<main class="form-signin">
				<form method="post" action="${contextPath}${formurl}">
					<input type="hidden" name="return_uri" value="${return_uri}" />

					<!-- 			<img class="mb-4" src="../assets/brand/bootstrap-logo.svg" alt="" width="72" height="57"> -->
					<h1 class="h3 mb-3 fw-normal">Please sign in</h1>

					<div class="form-floating">
						<input type="text" class="form-control" id="floatingInput"
							placeholder="User ID" name="${username}" /> <label
							for="floatingInput">User ID</label>
					</div>
					<div class="form-floating">
						<input type="password" class="form-control" id="floatingPassword"
							placeholder="Password" name="${password}" /> <label
							for="floatingPassword">Password</label>
					</div>
					<!--
					<div class="checkbox mb-3">
						<label>
						<input type="checkbox" value="remember-me"> Remember me
						</label>
					</div>
					-->
					<button class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>
					<div>
						<c:if test="${not empty error }">
							<div class="error" style="color: red;">${error }</div>
						</c:if>
						<c:if test="${not empty msg   }">
							<div class="msg" style="color: green;">${msg   }</div>
						</c:if>
						<c:if test="${not empty logout}">
							<div class="logout" style="color: green;">${logout}</div>
						</c:if>
					</div>
<!-- 					<p class="mt-5 mb-3 text-muted">&copy; 2017~2021</p> -->
				</form>
			</main>
		</div>
	</c:if>
	<c:if test="${!empty logout}">
	<!-- <iframe name="sso-logout" src="https://${targetDomain}/exec/front/Member/logout/" allow="cross-origin" style="display:none;"></iframe> -->
	</c:if>

	<c:if test="${!empty principal.loginId }">
		<div id="div-login-info">
			[ s_principal : ${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.loginId} ]
			[ a_principal : ${principal.loginId} ]

			<ul>
				<li><a class="logout" href="/logout?return_uri=${return_uri}"> 로그아웃 </a>
				<li><a class="logout" href="/logout?return_uri=/login">로그아웃 리다이렉트</a>
			</ul>
		</div>
	</c:if>
</div>
<script>

</script>