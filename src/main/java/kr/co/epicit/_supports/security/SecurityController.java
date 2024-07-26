package kr.co.epicit._supports.security;

import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.epicit.util.App;

@Controller
public class SecurityController {
	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

	@Autowired
	private ApplicationContext applicationContext;

	@SuppressWarnings("unused")
	@Autowired
	private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

	@RequestMapping(value={ "", "/", "/index" })
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session, @AuthenticationPrincipal SecurityUserDetails userDetails, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
//		logger.info( "\nHttpSession:"+ App.toJsonPretty(session) );
//
//		SecurityUserDetails a_principal = App.getPrincipal(request);
//		if (a_principal != null && a_principal.getUsername() != null) {
//			model.addObject("principal", a_principal);
//		}
//
//		App.prefixLoginForm(model);
//		model.setViewName("indexPc");
//		return model;

		return indexpath(request, response, session, userDetails, null, requestParam, model);
	}

	@RequestMapping(value="/{device}")
	public ModelAndView indexpath(HttpServletRequest request, HttpServletResponse response, HttpSession session, @AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable("device") String device, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.info( "\nHttpSession:"+ App.toJsonPretty(session) );
		if (device == null) { device="pc"; }
		device = device.toLowerCase();
		device = device.substring(0, 1).toUpperCase() + device.substring(1);

		SecurityUserDetails a_principal = App.getPrincipal(request);
		if (a_principal != null && a_principal.getUsername() != null) {
			model.addObject("principal", a_principal);
		}

		App.prefixLoginForm(model);
		String targetDomain = "mobile".equalsIgnoreCase( device ) ? "m.vmembersmall.com" : "vmembersmall.com";
		model.addObject("targetDomain",             targetDomain  );
		model.addObject("targetHost"  , "https://"+ targetDomain  );
		model.addObject("device"      , device                    );

		model.addObject("logout"      , requestParam.get("logout"));

		model.setViewName("index");
		return model;
	}

	@RequestMapping(value="/welcome")
	public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response, HttpSession session, @AuthenticationPrincipal SecurityUserDetails userDetails, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {

		model.setViewName("welcome");

		return model;
	}

	@RequestMapping(value="/login")
	public ModelAndView login(
			HttpServletRequest  request,
			HttpServletResponse response,
			@RequestParam Map<String, String> requestParam,
			@RequestParam(value="error" , required=false) String error,
			@RequestParam(value="logout", required=false) String logout,
			ModelAndView model) throws Exception {

		if (error  != null) { model.addObject("error", "Invalid username or password."         ); }
		if (logout != null) { model.addObject("msg"  , "You have been logged out successfully."); }

//		model.addObject("param", new HashMap<String, String>(){{
//			if (error  != null) { put("error", "Invalid username or password."         ); }
//			if (logout != null) { put("msg"  , "You have been logged out successfully."); }
//		}});

////		usernamePasswordAuthenticationFilter.getUsernameParameter();
////		usernamePasswordAuthenticationFilter.getEnvironment()
//
//		model.addObject(usernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, usernamePasswordAuthenticationFilter.getUsernameParameter());
//		model.addObject(usernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY, usernamePasswordAuthenticationFilter.getPasswordParameter());
//		model.addObject("formurl" , "/loginprocessing");
//
//		//usernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY
//		// usernamePasswordAuthenticationFilter.getFilterProcessesUrl
//		//usernamePasswordAuthenticationFilter.setFilterProcessesUrl(logout)
//
//		//usernamePasswordAuthenticationFilter
//		//usernamePasswordAuthenticationFilter.requiresAuthenticationRequestMatcher.get
//
//		//;.AntPathRequestMatcher

		App.prefixLoginForm(requestParam, model);
		model.setViewName("security/login");

		return model;
	}

//	@RequestMapping(value="/loginprocessing")
//	public ModelAndView loginprocessing(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
//		String redirect_uri = !Objects.isNull(requestParam) ? requestParam.get("redirect_uri") : null;
//
//		model.setViewName(getRedirectUri(redirect_uri));
//		return model;
//	}

	@RequestMapping(value="/logout")  // ), method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, HttpSession session, @AuthenticationPrincipal SecurityUserDetails userDetails, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) { new SecurityContextLogoutHandler().logout(request, response, auth); }

		String return_uri = !Objects.isNull(requestParam) ? requestParam.get("return_uri") : null;
		if (Objects.isNull(return_uri)) { return_uri = "/"; }  // login?logout"; }

		model.setViewName(getReturnUri(return_uri));
		return model;  // "redirect: "+ redirect_uri;
	}


	private String getReturnUri(String return_uri) {
		if (Objects.isNull(return_uri)) { return_uri = "/"; }  // login?logout"; }
		return "redirect: "+ return_uri;
	}




//	@Autowired
//	private FormLoginConfigurer formLoginConfigurer;
//	private FormLoginConfigurer getFormLoginConfigurer() {
//		return formLoginConfigurer;
//	}

	@PostConstruct
	public void init() {
//		// FilterChainProxy를 가져와서 필터 체인 리스트를 조회
//		FilterChainProxy filterChainProxy = applicationContext.getBean(FilterChainProxy.class);
//		List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();
//
//		// 필터 체인에서 HttpSecurity 설정을 추출
//		for (SecurityFilterChain chain : filterChains) {
//			//RequestMatcher matcher = chain.getRequestMatcher();
//			//if (matcher != null) {
//				try {
//					// HttpSecurity 설정에 접근
//					HttpSecurity httpSecurity = (HttpSecurity) chain;
//					httpSecurity.getConfigurerList().forEach(configurer -> {
//						if (configurer instanceof FormLoginConfigurer) {
//
//							FormLoginConfigurer formLoginConfigurer = mFormLoginConfigurer = (FormLoginConfigurer) configurer;
//							loginPage = formLoginConfigurer.getLoginPage();
//							defaultTargetUrl = formLoginConfigurer.getDefaultSuccessUrl();
//							authenticationFailureUrl = formLoginConfigurer.getFailureUrl();
//							usernameParameter = formLoginConfigurer.getUsernameParameter();
//							passwordParameter = formLoginConfigurer.getPasswordParameter();
//						}
//					});
//					HttpSecurity httpSecurity;
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			//}
//		}
	}





	@RequestMapping(value="/spring/beans")
	public @ResponseBody String[] beans() throws Exception {
		return applicationContext.getBeanDefinitionNames();
	}



}
