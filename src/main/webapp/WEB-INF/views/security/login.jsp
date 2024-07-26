<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!doctype html>
<html lang="ko">
<head>
<meta charset="utf-8">

<meta http-equiv="X-UA-Compatible" content="IE=edge" /> <!-- IE 문서모드 설정 빼기 -->
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"> <!-- 모바일용 뷰포트 설정하기, 화면 확대/축소 막기 -->
<meta name="viewport" content="viewport-fit=cover"> <!-- 아이폰 노치(안전영역) 가리기 -->
<meta name="format-detection" content="telephone=no"> <!-- 모바일 전화번호 링크 방지하기 -->
<link rel="apple-touch-icon-precomposed" href=""> <!-- 애플터치 아이콘 사용하기 -->
<Style>
/* 모든 태그 박스사이징 속성 설정하기 */
*, *:after, *:before {
	box-sizing: border-box;
}

/* 모바일에선 body에 overflow: hidden; 사용시 창 크기 잡아주기 */
body.on {
	overflow: hidden;
	position: fixed;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
}

/* min-width값 320px로 잡기 */
#warp {
	min-width: 320px;
}

/* 모바일 탭하이라이트 컬러 변경하기 */
a {
	-webkit-tap-highlight-color: rgba(0, 0, 0, .1);
}
</Style>

<link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/sign-in/">
<!-- <link href="../assets/dist/css/bootstrap.min.css" rel="stylesheet"> -->
<!-- Bootstrap core CSS -->

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js" integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF" crossorigin="anonymous"></script>
<script type="module">
import { Toast } from 'bootstrap.esm.min.js';
Array.from(document.querySelectorAll('.toast')).forEach(toastNode => new Toast(toastNode));
</script>

<style>
.bd-placeholder-img {
	font-size: 2.125rem;
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
<link href="${contextPath}/static/signin.css" rel="stylesheet">

<title>Login Page</title>
</head>
<body class="text-center">

	<main class="form-signin">
		<form method="post" action="${contextPath}${formurl}">
			<input type="hidden" name="return_uri" value="${return_uri}" />
			<img class="mb-4" src="../assets/brand/bootstrap-logo.svg" alt="" width="72" height="57">
			<h1 class="h3 mb-3 fw-normal">Please sign in</h1>

			<div class="form-floating">
				<input type="text" class="form-control" id="floatingInput" placeholder="User ID" name="${username}" />
				<label for="floatingInput">User ID</label>
			</div>
			<div class="form-floating">
				<input type="password" class="form-control" id="floatingPassword" placeholder="Password" name="${password}" />
				<label for="floatingPassword">Password</label>
			</div>
			<!--
			<div class="checkbox mb-3">
				<label>
					<input type="checkbox" value="remember-me"> Remember me
				</label>
			</div>
			-->
			<button class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>
<!-- 			<p class="mt-5 mb-3 text-muted">&copy; 2017–2021</p> -->
		</form>
	</main>

	<!--
<h2>Login</h2>
<form method="post" action="${contextPath}${formurl}">
<input type="hidden" name="return_uri" value="${return_uri}" />

<div>
<label for="${username}">Username:</label>
<input type="text" id="${username}" name="${username}"/>
</div>
<div>
<label for="${password}">Password:</label>
<input type="password" id="${password}" name="${password}"/>
</div>
<div>
<button type="submit">Login</button>
</div>


<c:if test="${not empty error }"><div class="error"  style="color:red;"  >${error }</div></c:if>
<c:if test="${not empty msg   }"><div class="msg"    style="color:green;">${msg   }</div></c:if>
<c:if test="${not empty logout}"><div class="logout" style="color:green;">${logout}</div></c:if>

</form>
-->

<!-- views -->
<script>
getQueryObject = (url)=>{
	// S1. 필요데이터 체크 - sso, returnUrl
	url = url || location;
	const qs = url instanceof Location ? url.search : url.split('?')[1]
		, qp = new URLSearchParams(qs)
		, qa = {};
	for (const [key,value] of qp.entries()) {
		qa[key] = value;
	}
	return qa;
};

var qo=getQueryObject(), ir = document.querySelector('form input[name=return_uri]');
if (qo && qo.return_uri && ir) {
	ir.value = encodeURIComponent(qo.return_uri);
	console.log(ir.value);
}
</script>
</body>
</html>