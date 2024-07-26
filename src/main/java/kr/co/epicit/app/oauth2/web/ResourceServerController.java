package kr.co.epicit.app.oauth2.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.epicit._supports.security.SecurityUserDetails;
import kr.co.epicit._supports.security.SecurityUserDetailsRepository;
import kr.co.epicit.app.transfer.cafe24.worker.Cafe24AdminApiWorker;
import kr.co.epicit.util.App;

/*
 * http://uplus.co.kr/ssovmbrs/authorize.redirect.url
 * http://uplus.co.kr/ssovmbrs/access.token
 * http://uplus.co.kr/ssovmbrs/user.profile
 */
@Controller
@RequestMapping("/resource")
public class ResourceServerController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${sso.vmbrs.mall.id}"               ) private String sso_vmbrs_mall_id               ;
	@Value("${sso.vmbrs.client.id}"             ) private String sso_vmbrs_client_id             ;
	@Value("${sso.vmbrs.client.secret}"         ) private String sso_vmbrs_client_secret         ;
	@Value("${sso.vmbrs.service.key}"           ) private String sso_vmbrs_service_key           ;
	@Value("${sso.vmbrs.webhook.key}"           ) private String sso_vmbrs_webhook_key           ;
	@Value("${sso.vmbrs.authorize.redirect.url}") private String sso_vmbrs_authorize_redirect_url;
	//@Value("${sso.vmbrs.authorize.callback.url}") private String sso_vmbrs_authorize_callback_url;


	@Autowired
	private SecurityUserDetailsRepository userDetailRepository;

	/**
	 * 6.Access Token으로 사용자 정보 요청
	 *   카페24는 쇼핑몰에서 활용할 사용자 정보를 획득하기 위해 연동서비스로 사용자 정보를 요청해요.
	 * Request)
	 * +================+=======================================================================+
	 * | Parameter	    | 변수                                                                  |
	 * +----------------+-----------------------------------------------------------------------+
	 * | access_token   | [ACCESS_TOKEN]                                                        |
	 * +================+=======================================================================+
	 *
	 * 7.사용자 정보 반환
	 *   연동서비스는 사용자 정보를 JSON 포맷으로 응답해주세요.
	 * Response)
	 * +================+=======================================================================+
	 * | Response Type  | Value                                                                 |
	 * +----------------+-----------------------------------------------------------------------+
	 * | json           | { "id": [ID], "name": [NAME], "email": [EMAIL], "mobile": [MOBILE] }  |
	 * +================+=======================================================================+
	 */
	@SuppressWarnings("serial")
	@RequestMapping({"/user/profile", "/user/me"})
	public @ResponseBody Map<String, String> user_profile(HttpServletRequest request, HttpServletResponse response, HttpSession session, @AuthenticationPrincipal SecurityUserDetails userDetails, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.info("/user/profile > @RequestParam : "+ App.toJsonPretty( requestParam ));

		Map<String, String> m = null;
		String access_token = requestParam.get("access_token");
		if (access_token == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, App.errorResponseJson("Enter access_token", "액세스 토큰 값을 누락하여 요청한 경우"));
			return null;
		}

		SecurityUserDetails userDetail = userDetailRepository.loadUserByAccessToken(access_token);
		if (userDetail == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, App.errorResponseJson("Enter access_token", "잘못되거나 만료된 token으로 요청한 경우"));
			return null;
		}

		m = new HashMap<String, String>(){{
			put("id"    , userDetail.getLoginId  ());
			put("name"  , userDetail.getLoginName());
			put("email" , userDetail.getEmail    ());
			put("mobile", userDetail.getMobile   ());
			//put("MOBILE", userDetail.getMobile   ());  // [2024-07-03] 이거아님.
		}};
		logger.info("/user/profile > @ResponseBody : "+ App.toJsonPretty(
			m
		));

		return m;
	}

	@Autowired
	private Cafe24AdminApiWorker adminApiworker;

	@SuppressWarnings("serial")
	@RequestMapping("/user/group")
	public @ResponseBody Map<?, ?> user_group(HttpServletRequest request, HttpServletResponse response, HttpSession session, @AuthenticationPrincipal SecurityUserDetails userDetails, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.info("/user/group > @RequestParam : "+ App.toJsonPretty( requestParam ));
		Map<?, ?> m = null;

		// Data > requests
		Map<String, String> item = new HashMap<String, String>(){{
			put("member_id"  , requestParam.get("member_id"));
			put("group_no"   , requestParam.get("group_no" ));
			put("fixed_group", "T"                          );
		}};
		List<Map<String, String>> requests = new ArrayList<Map<String, String>>();
		requests.add(item);

		Map<String, String> config = adminApiworker.getConfig();
		Map<String, String> placeholder = new HashMap<String, String>(){{
			put("mall_id" , config      .get("mall_id" ));
			put("group_no", requestParam.get("group_no"));
		}};

		String url = adminApiworker.mappingPlaceholder("https://{mall_id}.cafe24api.com/api/v2/admin/customergroups/{group_no}/customers", placeholder);
		Map<String, Object> requestData = new HashMap<String, Object>(){{
			put("shop_no" , config.get("shop_no"));
			put("requests", requests             );
		}};

		m = adminApiworker.callApi4PostJSON(url, requestData);

		return m;
	}

	@RequestMapping("/user/update")
	public @ResponseBody Map<String, String> user_update(HttpServletRequest request, HttpServletResponse response, HttpSession session, @AuthenticationPrincipal SecurityUserDetails userDetails, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.info("/user/update > @RequestParam : "+ App.toJsonPretty( requestParam ));
		Map<String, String> m = null;



		return m;
	}

	@RequestMapping("/user/unjoin")
	public @ResponseBody Map<String, String> user_unjoin(HttpServletRequest request, HttpServletResponse response, HttpSession session, @AuthenticationPrincipal SecurityUserDetails userDetails, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.info("/user/unjoin > @RequestParam : "+ App.toJsonPretty( requestParam ));
		Map<String, String> m = null;



		return m;
	}
}
