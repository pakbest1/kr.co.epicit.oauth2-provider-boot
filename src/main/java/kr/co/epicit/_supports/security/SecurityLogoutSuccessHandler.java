package kr.co.epicit._supports.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import kr.co.epicit.util.App;

@Component
public class SecurityLogoutSuccessHandler implements LogoutSuccessHandler {
	private static final Logger logger = LoggerFactory.getLogger(SecurityLogoutSuccessHandler.class);

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		logger.info("SecurityLogoutSuccessHandler.onLogoutSuccess()");

		if (authentication != null) {

		}

		HttpSession session = request.getSession();
		session.removeAttribute(SecurityConsts.LOGIN_ID    );
		session.removeAttribute(SecurityConsts.LOGIN_NAME  );
		session.removeAttribute(SecurityConsts.LOGIN_DETAIL);

		String contextPath = request.getContextPath()+"/";
		String return_uri=request.getParameter("return_uri");
		if (App.isNull(return_uri)) { return_uri = contextPath; }

		response.sendRedirect(return_uri+ (return_uri.indexOf("?")>-1 ? "&" : "?") + "logout=y");
	}

}
