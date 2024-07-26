package partners.inspire.transfer.cafe24.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.epicit.util.App;
import partners.inspire.transfer.cafe24.worker.Cafe24AdminApiWorker;

@Controller
@RequestMapping("/transfer/cafe24")
public class Cafe24ApiController {
	private static final Logger logger = LoggerFactory.getLogger(Cafe24ApiController.class);

	@Autowired
	private Cafe24AdminApiWorker worker;

	@SuppressWarnings("serial")
	@RequestMapping("/show")
	private @ResponseBody Map<?, ?> show(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.debug(" >> Cafe24ApiController > show(); ");

		Map<?, ?> m = new HashMap<String, Object>(){{
			put("config"     , worker.getConfig     ());
			put("accessToken", worker.getAccessToken());
		}};

		return m;
	}
	private void redirectShow(HttpServletResponse response) throws Exception {
		response.sendRedirect("/transfer/cafe24/show");
	}
	private void redirectToken(HttpServletResponse response) throws Exception {
		response.sendRedirect("/transfer/cafe24/token");
	}

	@SuppressWarnings("unused")
	@RequestMapping({ "/boot", "/reboot" })
	private ModelAndView boot(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.debug(" >> Cafe24ApiController > boot(); > requestParam : \n"+ App.toJsonPretty( requestParam ));

		String uri = worker.getAuthenticationUri();
		String refresh_token = worker.getConfig().get("refresh_token");

//		if (!App.isNull(refresh_token)) {
//			redirectShow(response);
//			return null;
//		}

		model.setViewName("redirect:"+ uri);
		return model;
	}

	@RequestMapping({ "/refresh" })
	private @ResponseBody Map<?, ?> refresh(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		Map<?, ?> m = worker.reboot();

		logger.debug(" >> Cafe24ApiController > refresh(); "+ App.toJsonPretty( m ));
		return m;
	}


	@RequestMapping("/authorize")
	private @ResponseBody Map<?, ?> authorize(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.debug(" >> Cafe24ApiController > authorize(); > requestParam : \n"+ App.toJsonPretty( requestParam ));

		Map<?, ?> m = null;



		return m;
	}

	@RequestMapping("/authorize/callback")
	private @ResponseBody Map<?, ?> authorize_callback(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		logger.debug(" >> Cafe24ApiController > authorize_callback(); > requestParam : \n"+ App.toJsonPretty( requestParam ));

		worker.requestAuthenticationCodeCallback(requestParam);  // getAuthrizeCallback
		// return token(request, response, session, requestParam, model);

		redirectToken(response);
		return null;

		// return m;
	}
	@SuppressWarnings({ "unused", "serial" })
	@RequestMapping("/token")
	private @ResponseBody Map<?, ?> token(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {

		worker.requestAccessToken();                             // getAccessToken

		Map<?, ?> m = new HashMap<String, Object>(){{
			put("config"     , worker.getConfig     ());
			put("accessToken", worker.getAccessToken());
		}};

		redirectShow(response);
		return null;

		// return m;
	}

	@SuppressWarnings("unused")
	@RequestMapping("/refresh/token")
	private @ResponseBody Map<?, ?> refresk_token(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> requestParam, ModelAndView model) throws Exception {
		String s = null;
		Map<?, ?> m = null;

		worker.requestRefreshAccessToken();

		redirectShow(response);
		return null;

		// return m;
	}
}
