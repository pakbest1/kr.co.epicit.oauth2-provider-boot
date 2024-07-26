<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="m_principal" value="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal}" />
<c:set var="protocol" value="${pageContext.request.scheme}" />
<c:set var="domain" value="${pageContext.request.serverName}" />
<c:set var="port" value="${pageContext.request.serverPort}" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!-- 포트가 80 또는 443이 아니면 포함, 아니면 생략 -->
<c:choose>
	<c:when test="${port != 80 && port != 443}">
		<c:set var="this_host" value="${protocol}://${domain}:${port}${contextPath}" />
	</c:when>
	<c:otherwise>
		<c:set var="this_host" value="${protocol}://${domain}${contextPath}" />
	</c:otherwise>
</c:choose>
<c:set var="return_uri" value="${contextPath}${requestScope['javax.servlet.forward.servlet_path']}" />
<!doctype  html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="description" content="" />
<link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/sign-in/" />
<link href="../assets/dist/css/bootstrap.min.css" rel="stylesheet" />
<!-- Bootstrap core CSS -->

<meta http-equiv="X-UA-Compatible" content="IE=edge" /><!-- IE 문서모드 설정 빼기 -->
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"><!-- 모바일용 뷰포트 설정하기, 화면 확대/축소 막기 -->
<meta name="viewport" content="viewport-fit=cover"><!-- 아이폰 노치(안전영역) 가리기 -->
<meta name="format-detection" content="telephone=no"><!-- 모바일 전화번호 링크 방지하기 -->
<link rel="apple-touch-icon-precomposed" href=""><!-- 애플터치 아이콘 사용하기 -->

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

<script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js" charset="utf-8"></script>
<script type="text/javascript" src="${contextPath}/vmembersmall/sso.js?_=" charset="utf-8"></script>

<style>
.div-login-form {
	width: 460px;
}
</style>
</head>
<body>
	<h2>[${device}] U.kr Provider Mall!</h2>

	<hr />
	<h4> 로그인 <a class="logout" href="/logout?return_uri=${return_uri}">[ 로그아웃 ]</a> </h4>
	<%@include file="./parts/login.form.jsp"%>

	<hr />
	<h3>v-맴버스 Link</h3>
	<ul>
		<li><a target="winParent" href="${targetHost}/member/login.html?sso=sso&returnUrl=blank">v-맴버스 > blank</a>
		<li><a target="winParent" href="${targetHost}/member/login.html?sso=sso&returnUrl=%2F">v-맴버스 > 홈</a> <!-- #${targetHost}/index.html -->
		<li><a target="winParent" href="${targetHost}/member/login.html?sso=sso&returnUrl=%2Fproduct%2Ftime_deal.html%3Fcate_no%3D53">v-맴버스 > TIME DEAL</a>
		<li><a target="winParent" href="${targetHost}/member/login.html?sso=sso&returnUrl=%2Fboard%2Fevent%2Fmimage.html">v-맴버스 > 🌴휴가지원🌴</a>
		<li><a target="winParent" href="${targetHost}/member/login.html?sso=sso&returnUrl=%2Fproduct%2Fevent_product.html%3Fcate_no%3D1818.html">v-맴버스 > 6월 썸머스페셜 모음.zip</a>
		<li><a target="winParent" href="${targetHost}/member/login.html?sso=sso&returnUrl=%2Fproduct%2Fevent_product.html%3Fcate_no%3D1863%26ifdotrk_campaign%3D07sale%26ifdotrk_slot%3Dmain%26ifdotrk_material%3Dgnb%26ifdotrk_rest%3D240701">v-맴버스 > 24-07 이달의브랜드 네오플램</a> <!-- #${targetHost}/board/gallery/list.html?board_no=8 -->
		<li><a target="winParent" href="${targetHost}/member/login.html?sso=sso&returnUrl=%2Fproduct%2Flg%25EC%25A0%2584%25EC%259E%2590-%25ED%2594%2584%25EB%25A6%25AC%25EB%25AF%25B8%25EC%2597%2584-%25EB%2594%2594%25EC%2598%25A4%25EC%258A%25A4-%25ED%258A%25B8%25EB%25A3%25A8-%25EC%258B%259D%25EA%25B8%25B0%25EC%2584%25B8%25EC%25B2%2599%25EA%25B8%25B0-35%25EC%259D%25B8-%25EA%25B0%2580%25EA%25B5%25AC%25EC%259A%25A914%25EC%259D%25B8%25EC%259A%25A9-%25EC%2586%2594%25EB%25A6%25AC%25EB%2593%259C-%25EA%25B7%25B8%25EB%25A6%25B0%25EA%25B8%25B0%25EB%25B3%25B8%25EC%2584%25A4%25EC%25B9%2598%25EB%25B9%2584-%25ED%258F%25AC%25ED%2595%25A8-due6glh-%25EC%258B%25A0%25EC%2584%25B8%25EA%25B3%2584-5%25EB%25A7%258C%25EC%259B%2590-%25EC%2582%25AC%25EC%259D%2580%2F38242%2Fcategory%2F53%2Fdisplay%2F1%2F">[LG전자] 프리미엄 디오스 트루 식기세척기 3~5인 가구용(14인용) 솔리드 그린_기본설치비 포함 / DUE6GLH [❣신세계 5만원 사은]</a>
	</ul>

	<hr />
	<h4>v-맴버스 SSO <c:if test="!empty ${principal}">[ ${r_principal.loginId} ]</c:if></h4>
	<ul class="vmbrslink">
		<!--
		<li><a target="_blank0" href="${targetHost}/api/member/Oauth2ClientLogin/sso/?mapping_doing=%2Fmember%2Flogin.html%3FreturnUrl%3D'">
		0. vmembersmall.com SSO Login - ${targetHost}/api/member/Oauth2ClientLogin/sso/?mapping_doing=%2Fmember%2Flogin.html%3FreturnUrl%3D'
		</a>
		</li>
		-->

		<li><a target="_blank1" href="${this_host}/oauth2/authorize?response_type=code&client_id=u.kr-client-id&state=MTIzNDU2Nzg=&redirect_uri=https%3A%2F%2F${targetDomain}%2FApi%2FMember%2FOauth2ClientCallback%2Fsso%2F">
			1.[Callback] OAuth2 - authorize >
			<!-- ${this_host}/oauth2/authorize?response_type=code&client_id=u.kr-client-id&state=MTIzNDU2Nzg=&redirect_uri=https%3A%2F%2Fvmembersmall.com%2FApi%2FMember%2FOauth2ClientCallback%2Fsso%2F -->
		</a></li>
		<li><a target="_blank2" href="${this_host}/oauth2/token?grant_type=authorization_code&code=${principal.token}">
			2.[Restful] OAuth2 - token >
			<!-- ${this_host}/oauth2/token?grant_type=authorization_code&code=${principal.token} -->
		</a></li>
		<li><a target="_blank3" href="${this_host}/resource/user/profile?access_token=${principal.accessToken}">
			3.[Restful] OAuth2 - userinfo >
			<!-- ${this_host}/resource/user/profile?access_token=${principal.accessToken} -->
		</a></li>
		<!--
		<li><a target="_blank4" href="${this_host}/resource/user/group?sso=sso&group_no=60&&member_id=${principal.loginId}">
			4.[Restful] ${this_host}/resource/user/group?sso=sso&group_no=60&&member_id=${principal.loginId}
		</a>
		</li>
		<li><a target="_blank5" href="${this_host}/resource/user/update">
			5.[Restful] ${this_host}/resource/user/update
		</a>
		</li>
		<li><a target="_blank6" href="${this_host}/resource/user/revoke">
			6.[Restful] ${this_host}/resource/user/revoke
		</a>
		</li>
		-->
	</ul>

	<hr />
	<h4>Cafe24 AdmPI Worker</h4>
	<ul class="cafe24transferlink">
		<li><a target="cafe24transfer" href="/transfer/cafe24/show">/transfer/cafe24/show</a>
		<li><a target="cafe24transfer" href="/transfer/cafe24/boot">/transfer/cafe24/boot</a>
	</ul>

	<!-- views -->
	<!--
	<a href="/">
		<img src="/web/upload/contents/20210908/logo-kor-gifv4.gif" alt="V-Members Mall" height="44" style="float: left;">
		<div class="sso" style="font-size: 16px !important;float: left;margin-left: 3px;"> x 우리가게패키지 </div>
	</a>
	-->
	<script>
	// Promise를 반환하여 지연을 구현하는 함수
	function delay(ms) {
		return new Promise(resolve => setTimeout(resolve, ms));
	}
	var fnLogout=(e)=>{
		var frm=null/*window.open('', 'winParent')*/, t=l.protocol+'//${targetDomain}/exec/front/Member/logout/';
		if (frm) {
			frm.location.href=t;
		} else {
			frm=d.createElement('iframe')
			frm.src=t;
			frm.name='winParent';
			frm.style='display:none';
			b.appendChild(frm);
			//b.removeChild(ifrm);
		}
		delay(800);
	};
	var d=document, b=d.body, l=location, lnkLogout = document.querySelectorAll('.logout');
	lnkLogout.forEach((el)=>{
		el.addEventListener('click', (e)=>{ fnLogout(); });
	});
	</script>
</body>
</html>
