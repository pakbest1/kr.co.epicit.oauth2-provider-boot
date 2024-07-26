/* mobile - /layout/new/js/sso.js */
"use strict";
var   _lgc_ = 0
	, _lgj_ = 0
	, _lgp_ = 0
	, _pathname = location.pathname;
this.$site = ({
	isDebug: true,
	version: '1.0.1a',
	log: (s)=>{
		if ($site.isDebug || false) {
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
		$site.log('> run $site.login();');

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
		$site.log('> run $site.init();');

		// 로그인체크
		if (!this.isLogin) {
			this.isLogin = $site.isLogin();
		}

		// URL QueryString to Object
		if (!this.getQueryObject) {
			this.getQueryObject = $site.getQueryObject;
		}

		// 플러스앱접근인지 확인
		if (!this.isPlusAppAccess) {
			this.isPlusAppAccess = $site.isPlusAppAccess;
		}

		// 로그인 웹페이지 처리
		if (!window['MemberAction']) {
			window['MemberAction'] = {};
		}

		// SNS 로그인 웹페이지에서 분기 처리
		if (!window['MemberAction'].snsLogin) {
			window['MemberAction'].snsLogin = $site.login;
		}
	}
	,

	// 로고처리
	ssologo: ()=>{
		if (_lgc_ > 10) {
			return;
		}
		// !$site.isLogin() ||
		var hlogo = document.querySelector('.header__logo');

		// Process - Logo > Query String
		var q = $site.getQueryObject()
		, sso = q.sso
		, ssol = document.querySelector('.header__logo .logo_' + sso);
		if (q && q.sso) {
			if (!ssol) {
				setTimeout(()=>{
					_lgc_++;
					$site.ssologo();
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

			$site.log('> run $site.ssologo(); #01');
			return;
		}

		// Process - Logo > Login Session
		if (!window['CAFE24API'] || !window['CAFE24API'].getLoginProvider || !window['CAPP_ASYNC_METHODS']) {
			setTimeout(()=>{
				_lgc_++;
				$site.ssologo();
			}
			, 100);
			return;
		}
		CAFE24API.getLoginProvider((r)=>{
			if (!r || !r.login || !r.login.provider) {
				setTimeout(()=>{
					_lgc_++;
					$site.ssologo();
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

			$site.log('> run $site.ssologo(); #02');
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
			$site.log('> run $site.ssoprofile(); #03');
			return;
		}
		if (!window['CAFE24API'] || !window['CAFE24API'].getLoginProvider || !window['CAPP_ASYNC_METHODS']) {
			setTimeout(()=>{
				_lgp_++;
				$site.ssoprofile();
			}
			, 100);
			$site.log('timer #01');
			return;
		}
		CAFE24API.getLoginProvider((r)=>{
			if (!r || !r.login || !r.login.provider) {
				setTimeout(()=>{
					_lgp_++;
					$site.ssoprofile();
				}
				, 100);
				return;
			}

			sso = r.login.provider;
			_opf_ = document.querySelector('.myshopMenu .profile');
			_opf_.style = sso ? 'display:none' : '';

			$site.log('> run $site.ssoprofile(); #02');
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
				$site.ssolink();
			}
			, 100);
			return;
		}

		var st = document.querySelector('#titleArea h2');
		// 페이지 타이틀
		if (st) {
			st.style = $site && $site.isLogin() ? 'display:none;' : '';
		}

		var q = $site.getQueryObject();
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
			lnav.style = sso && $site.isLogin() ? 'display:none;' : '';
		}
		// 페이지 네비게이션
		if (ssol) {
			ssol.style = !sso ? 'display:none' : '';
		}
		// SSO 로그인 버튼

		$site.log('> run $site.ssolink(); #01');
	}
	,

	// 로그인 페이지 > SNS Login Popup
	ssologin_onload: (qs)=>{
		$site.log('  > run $site.ssologin_onload();');

		// S2. 로그인체크 - CAFE24API.getCustomerIDInfo((e,r)=>{ console.log(JSON.stringify(r, null, 2)); }); => {"id":{"member_id":null,"guest_id":"2e3f681362859518172b1bc6d2d150d7"}}
		let q = $site.getQueryObject(qs || location);
		$site.log('ssologin_onload(' + JSON.stringify(q, null, 2) + ');');
		if (!q.sso || !q.returnUrl) {
			return;
		}

		if (!$site.isLogin() && q.sso && q.returnUrl && q.returnUrl != 'blank') {
			if (!window['MemberAction'] || !window['MemberAction'].snsLogin) {
				$site.init();
			}
			q.returnUrl = encodeURIComponent(q.returnUrl);
			var ifs = document.querySelector('iframe#ifrm_sns');
			if (ifs && ifs.src) {
				var ifsSrc = ifs.src
				, ifsParam = $site.getQueryObject(ifsSrc);
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
			$site.log('  > ssologin_onload MemberAction.snsLogin("' + q.sso + '","' + q.returnUrl + '")');
			return;
		}

		if (q.returnUrl && q.returnUrl != 'blank') {
			$site.log('> location: ' + q.returnUrl);
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
				$site.ssojoinresult();
			}
			, 100);
			return;
		}
		CAFE24API.getLoginProvider((r)=>{
			$site.log('r:' + JSON.stringify(r, null, 2) + '\n'/*+'r:'+JSON.stringify(r, null, 2)+'\n'*/
			);
			if (!r || !r.login || !r.login.provider) {
				_lgj_++;
				setTimeout(()=>{
					$site.ssojoinresult();
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
		$site.log('##### $sns Start #####');
		_lgc_ = 0;
		_lgj_ = 0;
		_lgp_ = 0;
		_pathname = location.pathname;
		$site.log('location[pathname:' + _pathname + ']');

		// Process > All Page Logo > QueryString + LoginSession
		$site.ssologo();

		// Process > Login Page(/member/login.html)
		if (_pathname && ['/member/login.html'].includes(_pathname)) {
			$site.ssolink();
			window.addEventListener('load', (e)=>{
				$site.ssologin_onload();
			}
			);
		}

		// Process > Member Join Result(/member/join_result.html)
		if (_pathname && ['/member/join_result.html'].includes(_pathname)) {
			$site.ssojoinresult();
		}

		// Process > My Shop(/myshop/index.html)
		if (_pathname && ['/myshop/', '/myshop/index.html'].includes(_pathname)) {
			$site.ssoprofile();
		}

		$site.log(JSON.stringify($site, null, 2));
		$site.log('##### $sns Finish #####');
	}
	,
});

// Process
//$site.load();