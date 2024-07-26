/* mobile - /layout/new/js/sso.js */
"use strict";
var   _lgc_ = 0
	, _lgj_ = 0
	, _lgp_ = 0
	, _pathname = location.pathname;
this.$sso = ({
	isDebug: true,
	version: '1.0.1a',
	log: (s)=>{
		if ($sso.isDebug || false) {
			console.log(s);
		}
	}
	,

	// 로그인체크
	isLogin: ()=>{
		return document.cookie.match(/(?:^| |;)iscache=F/) ? true : false;
	}
	,

	// URL QueryString to Object
	getQueryObject: (url)=>{
		// S1. 필요데이터 체크 - sso, returnUrl
		url = url || location;
		const qs = url instanceof Location ? url.search : url.split('?')[1]
		, qp = new URLSearchParams(qs)
		, qa = {};
		for (const [key,value] of qp.entries()) {
			qa[key] = value;
		}
		return qa;
	}
	,

	// sessionStorage Functions
	removeSessionStorage: (name)=>{
		return window.sessionStorage.removeItem(name);
	}
	,
	setSessionStorage: (name,value)=>{
		return window.sessionStorage.setItem(name, value);
	}
	,
	getSessionStorage: (name)=>{
		return window.sessionStorage.getItem(name);
	}
	,

	// Cookie Functions - 초기화 : setCookie(~~~~,~~~~,-1);
	resetCookie(cName) {
		var expireDate = new Date();
		expireDate.setDate(expireDate.getDate() - 1);
		document.cookie = cName + "= " + "; expires=" + expireDate.toGMTString() + "; path=/";
	},
	setCookie(name, value, exp) {
		var date = new Date();
		date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);
		document.cookie = name + '=' + escape(value) + ';expires=' + date.toUTCString() + ';path=/';
	},
	getCookie: (name)=>{
		var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
		return value ? unescape(value[2]) : null;
	}
	,

	// 플러스앱접근인지 확인
	isPlusAppAccess: ()=>{
		if (navigator.userAgent.indexOf('Cafe24Plus') !== -1) {
			return true;
		}
		return false;
	}
	,

	// Sns Login Custom
	login: (sSnsName,sReturnUrl,sMappingDoing)=>{
		$sso.log('> run $sso.login();');

		window.name = 'winParent';
		// 플러스앱전용
		if (isPlusAppAccess() === true) {
			if (sSnsName == 'googleplus' || sSnsName == 'facebook') {
				var sLoginUrl = location.protocol + '//' + location.hostname;
				sLoginUrl += EC_ROUTE.getPrefixUrl('/Api/Member/Oauth2CustomtabLogin/' + sSnsName) + '/?resolver=custom_tab&returnUrl=' + sReturnUrl + '&mapping_doing=' + sMappingDoing;
				location.href = 'login://oauth_web?provider_code=' + sSnsName + '$&login_url=' + sLoginUrl;
			} else {
				location.href = EC_ROUTE.getPrefixUrl('/api/member/Oauth2ClientLogin/' + sSnsName) + '/?returnUrl=' + sReturnUrl + '&mapping_doing=' + sMappingDoing;
			}
		} else {
			window.open(EC_ROUTE.getPrefixUrl('/api/member/Oauth2ClientLogin/' + sSnsName) + '/?mapping_doing=' + sMappingDoing, 'winSnsLogin', 'toolbar=no location=no scrollbars=yes resizable=yes width=472 height=539');
		}
		return false;

	}
	,

	// 초기화
	init: ()=>{
		$sso.log('> run $sso.init();');

		// 로그인체크
		if (!this.isLogin) {
			this.isLogin = $sso.isLogin();
		}

		// URL QueryString to Object
		if (!this.getQueryObject) {
			this.getQueryObject = $sso.getQueryObject;
		}

		// 플러스앱접근인지 확인
		if (!this.isPlusAppAccess) {
			this.isPlusAppAccess = $sso.isPlusAppAccess;
		}

		// 로그인 웹페이지 처리
		if (!window['MemberAction']) {
			window['MemberAction'] = {};
		}

		// SNS 로그인 웹페이지에서 분기 처리
		if (!window['MemberAction'].snsLogin) {
			window['MemberAction'].snsLogin = $sso.login;
		}
	}
	,

	// 로고처리
	ssologo: ()=>{
		if (_lgc_ > 10) {
			return;
		}
		// !$sso.isLogin() ||
		var hlogo = document.querySelector('.header__logo');

		// Process - Logo > Query String
		var q = $sso.getQueryObject()
		, sso = q.sso
		, ssol = document.querySelector('.header__logo .logo_' + sso);
		if (q && q.sso) {
			if (!ssol) {
				setTimeout(()=>{
					_lgc_++;
					$sso.ssologo();
				}
				, 100);
				return;
			}

			if (hlogo) {
				hlogo.style = CAFE24.MOBILE ? '' : 'width:495px;'
			}
			if (ssol) {
				ssol.style = '';
			}

			var la = document.querySelector('.header__logo a');
			if (la) {
				la.innerHTML = la.innerHTML.replace(/\&nbsp;/g, '').trim();
			}

			$sso.log('> run $sso.ssologo(); #01');
			return;
		}

		// Process - Logo > Login Session
		if (!window['CAFE24API'] || !window['CAFE24API'].getLoginProvider || !window['CAPP_ASYNC_METHODS']) {
			setTimeout(()=>{
				_lgc_++;
				$sso.ssologo();
			}
			, 100);
			return;
		}
		CAFE24API.getLoginProvider((r)=>{
			if (!r || !r.login || !r.login.provider) {
				setTimeout(()=>{
					_lgc_++;
					$sso.ssologo();
				}
				, 100);
				return;
			}

			hlogo = document.querySelector('.header__logo');
			sso = r.login.provider;
			ssol = document.querySelector('.header__logo .logo_' + sso);

			if (hlogo) {
				hlogo.style = CAFE24.MOBILE ? '' : 'width:495px;'
			}
			if (ssol) {
				ssol.style = '';
			}

			$sso.log('> run $sso.ssologo(); #02');
		}
		);
	}
	,

	// My Shop > 회원정보 수정
	ssoprofile: ()=>{
		var _opf_ = document.querySelector('.myshopMenu .profile')
		, sso = null;
		if (_lgp_ > 10) {
			_opf_.style = '';
			$sso.log('> run $sso.ssoprofile(); #03');
			return;
		}
		if (!window['CAFE24API'] || !window['CAFE24API'].getLoginProvider || !window['CAPP_ASYNC_METHODS']) {
			setTimeout(()=>{
				_lgp_++;
				$sso.ssoprofile();
			}
			, 100);
			$sso.log('timer #01');
			return;
		}
		CAFE24API.getLoginProvider((r)=>{
			if (!r || !r.login || !r.login.provider) {
				setTimeout(()=>{
					_lgp_++;
					$sso.ssoprofile();
				}
				, 100);
				return;
			}

			sso = r.login.provider;
			_opf_ = document.querySelector('.myshopMenu .profile');
			_opf_.style = sso ? 'display:none' : '';

			$sso.log('> run $sso.ssoprofile(); #02');
		}
		);
	}
	,

	// 로그인 페이지 > SNS Login Button
	ssolink: ()=>{
		var sttl = document.querySelector('.titleArea h2');
		// 페이지 타이틀
		if (!sttl) {
			setTimeout(()=>{
				$sso.ssolink();
			}
			, 100);
			return;
		}

		var st = document.querySelector('#titleArea h2');
		// 페이지 타이틀
		if (st) {
			st.style = $sso && $sso.isLogin() ? 'display:none;' : '';
		}

		var q = $sso.getQueryObject();
		if (!q || !q.sso) {
			return;
		}

		var sso = q.sso;
		var lnav = document.querySelector('.path ol');
		// 페이지 네비게이션
		var ssot = (CAFE24 && CAFE24.MOBILE) ? '.snsLogin a.' : '.snsArea li.';
		var ssol = document.querySelector(ssot + sso);
		// SSO 로그인 버튼 표시
		if (lnav) {
			lnav.style = sso && $sso.isLogin() ? 'display:none;' : '';
		}
		// 페이지 네비게이션
		if (ssol) {
			ssol.style = !sso ? 'display:none' : '';
		}
		// SSO 로그인 버튼

		$sso.log('> run $sso.ssolink(); #01');
	}
	,

	// 로그인 페이지 > SNS Login Popup
	ssologin_onload: (qs)=>{
		$sso.log('  > run $sso.ssologin_onload();');

		// S2. 로그인체크 - CAFE24API.getCustomerIDInfo((e,r)=>{ console.log(JSON.stringify(r, null, 2)); }); => {"id":{"member_id":null,"guest_id":"2e3f681362859518172b1bc6d2d150d7"}}
		let q = $sso.getQueryObject(qs || location);
		$sso.log('ssologin_onload(' + JSON.stringify(q, null, 2) + ');');
		if (!q.sso || !q.returnUrl) {
			return;
		}

		if (!$sso.isLogin() && q.sso && q.returnUrl && q.returnUrl != 'blank') {
			if (!window['MemberAction'] || !window['MemberAction'].snsLogin) {
				$sso.init();
			}
			q.returnUrl = encodeURIComponent(q.returnUrl);
			var ifs = document.querySelector('iframe#ifrm_sns');
			if (ifs && ifs.src) {
				var ifsSrc = ifs.src
				, ifsParam = $sso.getQueryObject(ifsSrc);
				ifsParam.returnUrl = q.returnUrl;

				ifs.addEventListener('load', (e)=>{
					var ifsW = ifs.contentWindow
					, ifsD = ifsW.document
					, ifsJF = ifsD.querySelector('form[name=joinForm]');
					if (ifsW && ifsD && ifsJF && ifsJF.returnUrl) {
						ifsJF.returnUrl.value = q.returnUrl;
					}
				}
				);
				//
				ifs.src = ifsSrc.split('?')[0] + '?' + new URLSearchParams(ifsParam).toString();
			}

			MemberAction.snsLogin(q.sso, q.returnUrl);
			$sso.log('  > ssologin_onload MemberAction.snsLogin("' + q.sso + '","' + q.returnUrl + '")');
			return;
		}

		if (q.returnUrl && q.returnUrl != 'blank') {
			$sso.log('> location: ' + q.returnUrl);
			location.href = q.returnUrl;
		}
	}
	,

	// SSO 회원가입 결과 > SNS Member Join Result
	ssojoinresult: ()=>{
		if (_lgj_ > 10) {
			return;
		}
		if (!CAFE24API || !CAFE24API.getLoginProvider) {
			_lgj_++;
			setTimeout(()=>{
				$sso.ssojoinresult();
			}
			, 100);
			return;
		}
		CAFE24API.getLoginProvider((r)=>{
			$sso.log('r:' + JSON.stringify(r, null, 2) + '\n'/*+'r:'+JSON.stringify(r, null, 2)+'\n'*/
			);
			if (!r || !r.login || !r.login.provider) {
				_lgj_++;
				setTimeout(()=>{
					$sso.ssojoinresult();
				}
				, 100);
				return;
			}
			var ssons = {
				sso: '유플러스SOHO'
			}
			, ssonm = ssons[r.login.provider]
			, mg = document.querySelector('.welcome p:nth-child(2) strong:nth-child(2)');
			if (mg && ssonm) {
				mg.innerHTML = '[' + ssonm + ']';
			}
		}
		);
	}
	,

	load: ()=>{
		// Process
		$sso.log('##### $sns Start #####');
		_lgc_ = 0;
		_lgj_ = 0;
		_lgp_ = 0;
		_pathname = location.pathname;
		$sso.log('location[pathname:' + _pathname + ']');

		// Process > All Page Logo > QueryString + LoginSession
		$sso.ssologo();

		// Process > Login Page(/member/login.html)
		if (_pathname && ['/member/login.html'].includes(_pathname)) {
			$sso.ssolink();
			window.addEventListener('load', (e)=>{
				$sso.ssologin_onload();
			}
			);
		}

		// Process > Member Join Result(/member/join_result.html)
		if (_pathname && ['/member/join_result.html'].includes(_pathname)) {
			$sso.ssojoinresult();
		}

		// Process > My Shop(/myshop/index.html)
		if (_pathname && ['/myshop/', '/myshop/index.html'].includes(_pathname)) {
			$sso.ssoprofile();
		}

		$sso.log(JSON.stringify($sso, null, 2));
		$sso.log('##### $sns Finish #####');
	}
	,
});

// Process
//$sso.load();