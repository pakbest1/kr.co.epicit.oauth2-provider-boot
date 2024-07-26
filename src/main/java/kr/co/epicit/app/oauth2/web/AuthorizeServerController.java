package kr.co.epicit.app.oauth2.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.epicit._supports.security.SecurityUserDetails;
import kr.co.epicit._supports.security.SecurityUserDetailsRepository;
import kr.co.epicit.util.App;

/*
 * http://uplus.co.kr/ssovmbrs/authorize.redirect.url
 * http://uplus.co.kr/ssovmbrs/access.token
 * http://uplus.co.kr/ssovmbrs/user.profile
 */
@Controller
@RequestMapping("/oauth2")
public class AuthorizeServerController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${sso.vmbrs.mall.id}"               ) private String sso_vmbrs_mall_id               ;
	@Value("${sso.vmbrs.client.id}"             ) private String sso_vmbrs_client_id             ;
	@Value("${sso.vmbrs.client.secret}"         ) private String sso_vmbrs_client_secret         ;
	@Value("${sso.vmbrs.service.key}"           ) private String sso_vmbrs_service_key           ;
	@Value("${sso.vmbrs.webhook.key}"           ) private String sso_vmbrs_webhook_key           ;
	@Value("${sso.vmbrs.authorize.redirect.url}") private String sso_vmbrs_authorize_redirect_url;
//	@Value("${sso.vmbrs.authorize.callback.url:}") private String sso_vmbrs_authorize_callback_url;

	@RequestMapping("/authorize/show/{accesskey}")
	public @ResponseBody Map<String, String> authorizeshow(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelAndView model, @RequestParam Map<String, String> requestParam, @PathVariable String accesskey) throws Exception {
		Map<String, String> r = new HashMap<String, String>();

		if ("1025586118".equals(accesskey)) {
			r.put("sso.vmbrs.client.id"             , sso_vmbrs_client_id               );
			r.put("sso.vmbrs.client.secret"         , sso_vmbrs_client_secret           );
			r.put("sso.vmbrs.service.key"           , sso_vmbrs_service_key             );
			r.put("sso.vmbrs.webhook.key"           , sso_vmbrs_webhook_key             );
			//r.put("sso.vmbrs.authorize.redirect.url", sso_vmbrs_authorize_redirect_url  );
			r.put("sso.vmbrs.authorize.redirect.url",
					sso_vmbrs_authorize_redirect_url
						.replaceAll("\\{mall_id\\}"            , sso_vmbrs_mall_id       )
					+
					"?response_type=code&client_id={client_id}&state={encode_csrf_token}&redirect_uri={encode_redirect_uri}&scope={scope}"
						.replaceAll("\\{client_id\\}"          , sso_vmbrs_client_id    )
						.replaceAll("\\{client_secret\\}"      , sso_vmbrs_client_secret)
						.replaceAll("\\{service_key\\}"        , sso_vmbrs_service_key  )
						.replaceAll("\\{webhook_key\\}"        , sso_vmbrs_webhook_key  )

						.replaceAll("\\{encode_csrf_token\\}"  , session.getId         ())

						//.replaceAll("\\{encode_redirect_uri\\}", sso_vmbrs_authorize_redirect_url  )
						//.replaceAll("\\{encode_redirect_uri\\}", URLEncoder.encode(sso_vmbrs_authorize_callback_url, "UTF-8"))

						.replaceAll("\\{scope\\}"              , "mall.read_customer_identifier")

						.replaceAll("\\{[a-zA-Z0-9_.]+\\}", "")
			);

			logger.info(App.toJsonPretty( r ));
		}

		return r;
	}


	/**
	 * 1. Authorization Code 발급 요청 (생략: 로그인 페이지)
	 *    [AUTHORIZE REDIRECT URL]?response_type=code&client_id=[CLIENT_ID]&state= [STATE] &redirect_uri= [REDIRECT_URI]
	 *    ?response_type=code&client_id=63TxKJEKuczELEdPzQju&state=178a00758.....4bd9dc96c7&redirect_uri=https%3A%2F%2F[도메인]%2FApi%2FMember%2FOauth2ClientCallback%2Fsso%2F
	 *
	 * 2. 연동서비스 미로그인 상태
	 *    연동서비스 미로그인 상태일 경우 연동서비스의 로그인 페이지를 제공해야하고, 쇼핑몰 회원은 계정 정보를 입력하여 로그인해요.
	 *
	 * 3.Authorization Code 반환
	 *   회원 로그인이 되면, Access Token 발급을 위해 Authorization Code를 카페24로 반환해주세요.
	 *   [REDIRECT_URI] ?code= [CODE] &state= [STATE]
	 *   https%3A%2F%2F[도메인]%2FApi%2FMember%2FOauth2ClientCallback%2Fsso?code=[CODE]&state=178a00758.....4bd9dc96c7
	 * +================+=======================================================================+
	 * | Parameter      | 변수                                                                  |
	 * +----------------+-----------------------------------------------------------------------+
	 * | response_type  | code                                                                  |
	 * | client_id      | [CLIENT_ID]                                                           |
	 * | state          | [STATE]                                                               |
	 * | redirect_uri   | [REDIRECT_URI]                                                        |
	 * | scope          | [CODE] - mall.read_application                                        |
	 * +================+=======================================================================+
	 */
//	private String authorize_code = "ed7f1ce05efdd438d2e06117cd2380985925b9643f46c45f5e024f10907d6cb9bb6a22863e11e7719cf2d6508f43a5e5ab97696e2ac98307b129a280cf768";

	@SuppressWarnings("serial")
	@RequestMapping("/authorize")
	public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.info("/authorize > @RequestParam : "+ App.toJsonPretty( requestParam ));

		SecurityUserDetails suser = App.getPrincipal();
		if (App.isNull(suser) || App.isNull(suser.getUsername())) {
			String return_uri = App.url.encode(request, requestParam);
			String login_url = "/login?return_uri="+ return_uri;

			model.setViewName("redirect:"+ login_url);
			return model;
		}

		// Check - response_type="code"
		// Check - sso_vmbrs_client_id
		String code         = suser.getToken();  // AuthorizeCodeGenerator.generateAuthCode(); // authorize_code;
		String state        = requestParam.get("state");
		String redirect_uri = requestParam.get("redirect_uri");

		String redirect = redirect_uri + "?code="+ code +"&state="+ state;
		model.addObject("code"        , code );
		model.addObject("state"       , state);
		model.addObject("redirect_uri", redirect);

		logger.info("/authorize Return : "+ App.toJsonPretty(
			new HashMap<String,String>(){{
				put("redirect_uri", redirect);
				put("code"        , code    );
				put("state"       , state   );
			}}
		));

		model.setViewName("redirect:"+  redirect);
		return model;  // model;
	}


	/**
	 * 4.Authorization Code로 Access Token 교환 요청
	 *   카페24가 Access Token 발급하기 위해 연동서비스로 Authorization code를 가지고 요청해요.
	 * Request)
	 * +================+=======================================================================+
	 * | Parameter      | 변수                                                                  |
	 * +----------------+-----------------------------------------------------------------------+
	 * | grant_type     | authorization_code                                                    |
	 * | client_id      | [CLIENT_ID]                                                           |
	 * | client_secret  | [CLIENT_SECRET]                                                       |
	 * | redirect_uri   | [REDIRECT_URI]                                                        |
	 * | code           | [CODE]                                                                |
	 * | refresh_token  | [REFRESH_TOKEN]                                                       |
	 * +================+=======================================================================+
	 * +================+=======================================================================+
	 * | Parameter	    | 변수                                                                  |
	 * +----------------+-----------------------------------------------------------------------+
	 * | grant_type	    | authorization_code                                                    |
	 * | client_id	    | 63TxKJEKuczELEdPzQju                                                  |
	 * | client_secret  | jCHLeke2iS                                                            |
	 * | redirect_uri 	| https%3A%2F%2F[도메인]%2FApi%2FMember%2FOauth2ClientCallback%2Fsso%2F |
	 * | code          	| 63TxKJEKuczELEdPzQju                                                  |
	 * | refresh_token	| 63TxKJEKuczELEdPzQju                                                  |
	 * +================+=======================================================================+
	 *
	 * 5.Access token 반환
	 *   연동서비스에서 Authorization Code로 인증이 완료되면 Access Token을 JSON 포맷으로 응답해주세요.
	 * Response)
	 * +================+=======================================================================+
	 * | Response Type  | Value                                                                 |
	 * +----------------+-----------------------------------------------------------------------+
	 * | json           | { "access_token": [ACCESS_TOKEN] }                                    |
	 * +================+=======================================================================+
	 */
	@Autowired
	private SecurityUserDetailsRepository userDetailRepository;

	@RequestMapping("/token")
	public @ResponseBody Map<String, String> token(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.info("/token > @RequestParam : "+ App.toJsonPretty( requestParam ));
		Map<String, String> map = new HashMap<String, String>();

		String grant_type = requestParam.get("grant_type"); // authorization_code
		String code       = requestParam.get("code"      ); // code

		if (grant_type == null || code == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, App.errorResponseJson("Unauthorized Access", "Unauthorized Access"));
			return null;
		} else
		if ("authorization_code".equals(grant_type) && code != null) {

			SecurityUserDetails principal = userDetailRepository.loadUserByToken(code);
			if (principal == null) {
				//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Access");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, App.errorResponseJson("Unauthorized Access", "Unauthorized Access"));
				return null;
			}

			map.put("access_token", principal.getAccessToken());
			logger.info("/token @ResponseBody : "+ App.toJsonPretty(
				map
			));
		}
		//org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor

		return map;
	}


//	/**
//	 * 6.Access Token으로 사용자 정보 요청
//	 *   카페24는 쇼핑몰에서 활용할 사용자 정보를 획득하기 위해 연동서비스로 사용자 정보를 요청해요.
//	 * Request)
//	 * +================+=======================================================================+
//	 * | Parameter	    | 변수                                                                  |
//	 * +----------------+-----------------------------------------------------------------------+
//	 * | access_token   | [ACCESS_TOKEN]                                                        |
//	 * +================+=======================================================================+
//	 *
//	 * 7.사용자 정보 반환
//	 *   연동서비스는 사용자 정보를 JSON 포맷으로 응답해주세요.
//	 * Response)
//	 * +================+=======================================================================+
//	 * | Response Type  | Value                                                                 |
//	 * +----------------+-----------------------------------------------------------------------+
//	 * | json           | { "id": [ID], "name": [NAME], "email": [EMAIL], "MOBILE": [MOBILE] }  |
//	 * +================+=======================================================================+
//	 */
//	@RequestMapping("/user/profile")
//	public @ResponseBody Map<String, String> userprofile(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
//		logger.info("@RequestParam : "+ App.toJsonPretty( requestParam ));
//
//		Map<String, String> userprofile = null;
//
//
//
//		return userprofile;
//	}

//	@ExceptionHandler(UnauthorizedException.class)
//	public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
//		ResponseEntity
//		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
//		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//	}
}
