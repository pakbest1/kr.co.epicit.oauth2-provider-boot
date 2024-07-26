<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!doctype html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Responsive Login Form Using Bootstrap 5</title>
<!-- Bootstrap 5 CDN Link -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS Link -->
<link rel="stylesheet" href="style.css">
</head>
<body>

<section class="wrapper">
	<div class="container">
		<div class="col-sm-8 offset-sm-2 col-lg-6 offset-lg-3 col-xl-4 offset-xl-4 text-center">
			<div class="logo">
				<img decoding="async" src="images/logo.png" class="img-fluid" alt="Logo">
			</div>
			<form class="rounded bg-white shadow py-5 px-4">
				<h3 class="text-dark fw-bolder fs-4 mb-2">Sign In to HILLTOP</h3>
				<div class="fw-normal text-muted mb-4"> New Here?
					<a href="#" class="text-primary fw-bold text-decoration-none">Create an Account</a>
				</div>
				<div class="form-floating mb-3">
					<input type="email" class="form-control" id="floatingInput" placeholder="name@example.com">
					<label for="floatingInput">Email address</label>
				</div>
				<div class="form-floating">
					<input type="password" class="form-control" id="floatingPassword" placeholder="Password">
					<label for="floatingPassword">Password</label>
				</div>
				<div class="mt-2 text-end">
					<a href="#" class="text-primary fw-bold text-decoration-none">Forget Password?</a>
				</div>
				<button type="submit" class="btn btn-primary submit_btn w-100 my-4">Continue</button>
				<div class="text-center text-muted text-uppercase mb-3">or</div>
				<a href="#" class="btn btn-light login_with w-100 mb-3">
					<img decoding="async" alt="Logo" src="images/google-icon.svg" class="img-fluid me-3">Continue with Google
				</a>
				<a href="#" class="btn btn-light login_with w-100 mb-3">
					<img decoding="async" alt="Logo" src="images/facebook-icon.svg" class="img-fluid me-3">Continue with Facebook
				</a>
				<a href="#" class="btn btn-light login_with w-100 mb-3">
					<img decoding="async" alt="Logo" src="images/linkedin-icon.svg" class="img-fluid me-3">Continue with Linkedin
				</a>
			</form>
		</div>
	</div>
</section>

</body>
</html>

