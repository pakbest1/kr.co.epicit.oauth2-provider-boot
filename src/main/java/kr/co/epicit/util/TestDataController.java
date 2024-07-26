package kr.co.epicit.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/data")
public class TestDataController {


	@RequestMapping("/json")
	public @ResponseBody Map<String, Object> testjson() {
		Map<String, Object> r = createTestData();

		return r;
	}

	@RequestMapping("/xml")
	public @ResponseBody Map<String, Object> testxml() {
		Map<String, Object> r = createTestData();

		return r;
	}

	@SuppressWarnings("serial")
	private Map<String, Object> createTestData() {
		Map<String, Object> r = new HashMap<String, Object>(){{
			put("loginId",   "test01");
			put("loginName", "테스트01");
			put("access_token", "INckUV91o1_Jd0ynkt0Wnmvm-vP2hNMt6TEEz05ZXZ4");
		}};


//		/org.springframework.web.servlet.mvc.method.annotation.ResponseBody
		return r;
	}
}
