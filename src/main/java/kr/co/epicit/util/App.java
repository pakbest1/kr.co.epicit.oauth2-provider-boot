package kr.co.epicit.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import kr.co.epicit._supports.security.SecurityConsts;
import kr.co.epicit._supports.security.SecurityUserDetails;

@Component
public class App {
	//private static final Logger logger = LoggerFactory.getLogger(App.class);
	private static final boolean isDebug = true;

	private App() {}

	@Autowired
	private        UsernamePasswordAuthenticationFilter _usernamePasswordAuthenticationFilter;
	private static UsernamePasswordAuthenticationFilter  usernamePasswordAuthenticationFilter;

	@Value("${login.form.processing.url:/loginprocessing}")
	private String        _login_form_processing_url;
	private static String  login_form_processing_url;

	@PostConstruct
	private void postConstruct() {
		usernamePasswordAuthenticationFilter = _usernamePasswordAuthenticationFilter;
		login_form_processing_url = _login_form_processing_url;
	}

	public static final ModelAndView prefixLoginForm(ModelAndView model) {
		return prefixLoginForm(null, model);
	}
	public static final ModelAndView prefixLoginForm(Map<String, String> requestParam, ModelAndView model) {
		if (model == null) { return model; }

		model.addObject("username", usernamePasswordAuthenticationFilter.getUsernameParameter());
		model.addObject("password", usernamePasswordAuthenticationFilter.getPasswordParameter());
		model.addObject("formurl" , login_form_processing_url);  // "/loginprocessing"

		if (!App.isNull(requestParam)) {
			String return_uri = requestParam.get("return_uri");
			if (!App.isNull(return_uri)) { model.addObject("return_uri" , url.decode(return_uri)); }
		}

		return model;
	}

	public static final String getRequestUrl(HttpServletRequest request) {
		return getRequestUrl(request, null);
	}
	public static final String getRequestUrl(HttpServletRequest request, Map<String, String> requestParam) {
		if (request == null) { return null; }

		String scheme      = request.getScheme     ();  // http 또는 https
		String serverName  = request.getServerName ();  // 호스트 이름
		int    serverPort  = request.getServerPort ();  // 포트 번호
		String contextPath = request.getContextPath();  // 웹 애플리케이션의 루트 경로
		String servletPath = request.getServletPath();  // 서블릿에 매핑된 경로
		String pathInfo    = request.getPathInfo   ();  // 추가 경로 정보 (있을 경우)
		String queryString = request.getQueryString();  // 쿼리 매개변수 (있을 경우)

		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);

		if (serverPort != 80 && serverPort != 443) {
			url.append(":").append(serverPort);
		}

		url.append(contextPath).append(servletPath);

		if (pathInfo != null) {
			url.append(pathInfo);
		}

		if (queryString != null) {
			url.append("?").append(queryString);
		}

		return url.toString();
	}

	// URL encode/decode
	public static class url {

		public static final String encode(HttpServletRequest request) {
			return encode(request, null);
		}
		public static final String encode(HttpServletRequest request, Map<String, String> requestParam) {
			if (request == null) { return null; }
			String s = getRequestUrl(request, requestParam);
			return encode(s);
		}
		public static final String encode(String url) {
			return encode(url, null);
		}
		public static final String encode(String s, String c) {
			if (isNull(s)) { return null; }
			if (isNull(c)) { c = "UTF-8"; }
			String so = null;
			try {
				so = URLEncoder.encode(s, c);
			} catch (Exception e) {}

			return so;
		}

		public static final String decode(String url) {
			return decode(url, null);
		}
		public static final String decode(String s, String c) {
			if (isNull(s)) { return null; }
			if (isNull(c)) { c = "UTF-8"; }
			String so = null;
			try {
				so = URLDecoder.decode(s, c);
			} catch (Exception e) {}

			return so;
		}
	}


//	public static final String encodeRequestUrl(HttpServletRequest request) {
//		if (request == null) { return null; }
//		String s = getRequestUrl(request);
//		return encodeUrl(s);
//	}
//
//	public static final String encodeUrl(String s) {
//		return url.encode(s);
//	}
//	public static final String urlencode(String s) {
//		return url.encode(s, null);
//	}
//	public static final String urlencode(String s, String charset) {
//		if (isNull(s      )) { return null; }
//		if (isNull(charset)) { charset = "UTF-8"; }
//		String so = null;
//		try {
//			so = URLEncoder.encode(s, charset);
//		} catch (Exception e) {}
//
//		return so;
//	}
//
//	public static final String decodeUrl(String s) {
//		if (isNull(s)) { return null; }
//		String so = null;
//		try {
//			so = URLDecoder.decode(s, "UTF-8");
//		} catch (Exception e) {}
//
//		return s;
//	}



	public static final boolean isNull(Object o) {
		boolean isNull = false;
		try {
			if (o instanceof List) {
				List<?> l = (List<?>) o;
				isNull = l == null || l.isEmpty() || l.size() < 1;
			} else
			if (o instanceof Map) {
				Map<?, ?> m = (Map<?, ?>) o;
				isNull = m == null || m.isEmpty() || m.size() < 1;
			} else {
				isNull = Objects.isNull(o) || "".equals(o==null?"":o.toString().trim());
			}
		} catch (Exception e) {
			isNull = true;
		}

		return isNull;
	}

	private static final ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	}

	public static String toJson(Object o) {
		String s = null;

		try {
			s = objectMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			//logger.error(e.getMessage(), e);  // e.printStackTrace();
			if (isDebug) { e.printStackTrace(); }
		}

		return s;
	}

	public static String toJsonPretty(Object o) {
		String s = null;

		try {
			s = "\n"+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
		} catch (JsonProcessingException e) {
			//logger.error(e.getMessage(), e);  // e.printStackTrace();
			if (isDebug) { e.printStackTrace(); }
		}

		return s;
	}

	public static <T> T fromJson(String s, Class<T> clzt) {
		T t;
		try {
			t = (T) objectMapper.readValue(s, clzt);
		} catch (Exception e) {
			//logger.error(e.getMessage(), e);  // e.printStackTrace();
			if (isDebug) { e.printStackTrace(); }
			t = null;
		}

		return t;
	}


	public static String toJsonPretty(HttpSession session) {
		Map<String, Object> m = new HashMap<String, Object>();

		if (session != null) {

			Enumeration<?> e = session.getAttributeNames();
			while (e.hasMoreElements()) {
				String sAttrKey = (String) e.nextElement();
				Object oAattrVal = session.getAttribute(sAttrKey);
				m.put(sAttrKey, oAattrVal);
			}

		}
		return toJsonPretty(m);
	}

	@SuppressWarnings("serial")
	public static final Map<String, String> errorResponse(String error, String description) {
		return new HashMap<String, String>() {{
			put("error"            , error      );
			put("error_description", description);
		}};
	}

	public static final String errorResponseJson(String error, String description) {
		return toJsonPretty( errorResponse(error, description) );
	}

	public static final SecurityUserDetails getPrincipal() {
		return getPrincipal(null);
	}

	public static final SecurityUserDetails getPrincipal(HttpServletRequest request) {
		SecurityUserDetails principal = null;

		try {
			principal = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			principal = null;
			if (request != null) {
				HttpSession session = request.getSession();
				principal = (SecurityUserDetails) session.getAttribute(SecurityConsts.LOGIN_DETAIL);
			}
		}

		return principal;
	}
}
