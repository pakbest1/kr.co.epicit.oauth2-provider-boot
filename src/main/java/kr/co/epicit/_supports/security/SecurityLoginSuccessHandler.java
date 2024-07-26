package kr.co.epicit._supports.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import kr.co.epicit.util.App;
import kr.co.epicit.util.AuthorizeCodeGenerator;

@Component
public class SecurityLoginSuccessHandler implements AuthenticationSuccessHandler {
	private static final Logger logger = LoggerFactory.getLogger(SecurityLoginSuccessHandler.class);

	@Autowired
	private SecurityUserDetailsRepository userDetailRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		String contextPath = request.getContextPath()+"/";

		String redirect_uri=request.getParameter("redirect_uri");
		if (App.isNull(redirect_uri)) { redirect_uri = contextPath; }

		HttpSession session = request.getSession();
		SecurityUserDetails suser = (SecurityUserDetails) authentication.getPrincipal();

		String return_uri  = request.getParameter("return_uri"     );
		String token       = AuthorizeCodeGenerator.genAccessToken();
		String accessToken = AuthorizeCodeGenerator.genAccessToken();
		suser.setToken      (token      );
		suser.setAccessToken(accessToken);

		userDetailRepository.commit(suser);

		session.setAttribute(SecurityConsts.LOGIN_ID    , suser.getLoginId  ());
		session.setAttribute(SecurityConsts.LOGIN_NAME  , suser.getLoginName());
		session.setAttribute(SecurityConsts.LOGIN_DETAIL, suser);

		String d_return_uri = App.url.decode(return_uri);
		if (d_return_uri != null) {  // 이거 있음 오류발생하니까 변경
			if (d_return_uri.indexOf("&amp;") > -1) { d_return_uri = d_return_uri.replaceAll("&amp;", "&"  ); }
			if (d_return_uri.indexOf("["    ) > -1) { d_return_uri = d_return_uri.replaceAll("\\["  , "%5B"); }
			if (d_return_uri.indexOf("]"    ) > -1) { d_return_uri = d_return_uri.replaceAll("\\]"  , "%5D"); }
		}
		if (!App.isNull(d_return_uri)) { contextPath = d_return_uri; }

		logger.info("\n"+
				"\t[username:"    + suser.getUsername()    +"]\n" +
				"\t[sesionid:"    + session.getId()        +"]\n" +
				"\t[redirect:"    + contextPath            +"]\n" +
				"\t[token:"       + suser.getToken()       +"]\n" +
				"\t[accesstoken:" + suser.getAccessToken() +"]\n" +
				"\t[return_uri:"  + d_return_uri           +"]"
		);

		response.sendRedirect( contextPath );
	}

}
// 출처: https://ttl-blog.tistory.com/103 [Shin._.Mallang:티스토리]
