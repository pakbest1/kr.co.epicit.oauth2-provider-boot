package kr.co.epicit.app.transfer.cafe24.worker;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import kr.co.epicit.util.App;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Component
public class Cafe24AdminApiWorker {
	private static final Logger logger = LoggerFactory.getLogger(Cafe24AdminApiWorker.class);

	private String d_mall_id       = "serveonemall";
	private String d_shop_no       = "1";
	private String d_api_version   = "2024-06-01";
	private String d_client_id     = "YqvwfHu1SaddFefhbt8W3G";
	private String d_client_secret = "lSnTFXKdIVKtOVcp0hdphD";
	private String d_service_key   = "gW9dLP04vv/8G4ngm3+KnnO914v5/CvYe2ur41OYriE=";
	private String d_version       = "2024-06-01";

	@Value("${sso.vmbrs.mall.id}")
	private String pedirect_uri;
	private String redirect_uri  = 	App.url.encode("https://inspire23.iptime.org:8443/transfer/cafe24/authorize/callback");  // "https://inspire23.iptime.org:8443/transfer/cafe24/authorize/callback"
	private String state         =  "";
	private List<String> scope   = 	Arrays.asList(
										"mall.read_application,mall.write_application",  // 앱 : 읽기+쓰기권한
										"mall.read_category",                            // 상품분류 : 읽기권한
										"mall.read_product",                             // 상품 : 읽기권한
										//"mall.read_collection",                          // (신규) 판매분류 : 읽기권한
										//"mall.read_supply",                              // (신규) 공급사 정보 : 읽기권한
										//"mall.read_personal",                            // (신규) 개인화정보 : 읽기권한
										"mall.read_order,mall.write_order",              // 주문 : 읽기+쓰기권한
										//"mall.read_community",                         // (신규) 게시판 : 읽기권한
										"mall.read_customer",                            // 회원 : 읽기권한
										//"mall.write_customer",                         // (신규) 회원 : 쓰기권한
										//"mall.read_notification",                        // (신규) 알림 : 읽기권한
										//"mall.read_store",                               // (신규) 상점 : 읽기권한
										"mall.read_promotion",                           // 프로모션 : 읽기권한
										//"mall.read_design",                              // (신규) 디자인 : 읽기권한
										//"mall.read_salesreport",                         // (신규) 매출통계 : 읽기권한
										//"mall.read_privacy",                             // (신규) 개인화정보 : 읽기권한
										"mall.read_mileage",                             // 적립금 : 읽기권한
										//"mall.read_translation",                         // (신규) 번역 : 읽기권한
										//"mall.read_analytics"                            // (신규) 접속통계 : 읽기권한
										"mall.read_shipping"                             // 배송 : 읽기권한
										//""
									);

	private String code          = "";
	private String token         = "";
	private String access_token  = "";
	private String refresh_token = "";

	@SuppressWarnings("serial")
	private Map<String, String> uris = new HashMap<String, String>(){{
		put("authorize"   , "https://{mall_id}.cafe24api.com/api/v2/oauth/authorize?response_type=code&client_id={client_id}&state={state}&redirect_uri={redirect_uri}&scope={scope}");
		put("token"       , "https://{mall_id}.cafe24api.com/api/v2/oauth/token");
		//put("refreshtoken", "https://{mall_id}.cafe24api.com/api/v2/oauth/token");
	}};

	@SuppressWarnings("serial")
	private Map<String, String> config = new HashMap<String, String>(){{
		//scope.removeAll(Arrays.asList(null, ""));

		put("mall_id"      , d_mall_id      );
		put("shop_no"      , d_shop_no      );
		put("api_version"  , d_api_version  );
		put("client_id"    , d_client_id    );
		put("client_secret", d_client_secret);
		put("service_key"  , d_service_key  );
		put("version"      , d_version      );

		put("redirect_uri" ,   redirect_uri );
		put("scope"        ,   scope        .toString().replaceAll("\\[|\\]| ", ""));
		put("state"        ,   state        );

		put("code"         ,   code         );
		put("token"        ,   token        );
		put("access_token" ,   access_token );
		put("refresh_token",   refresh_token);
	}};

	public Map<String, String> getConfig() {
		return config;
	}


	private Map<String, Object> accessToken = null;
	public Map<String, Object> getAccessToken() {
		return accessToken;
	}


	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		return new ThreadPoolTaskScheduler();
	}

//	@Bean
//	public ThreadPoolTaskExecutor taskExecutor(){
//		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
//		pool.setCorePoolSize(1);
//		pool.setMaxPoolSize(10);
//		pool.setWaitForTasksToCompleteOnShutdown(true);
//		return pool;
//	}


	public Map<String, String> reboot() {
		return boot();
	}
	public Map<String, String> boot() {
		return config;
	}

	public String getAuthenticationUri() {
		String authuri = null;
		authuri = mappingPlaceholder(uris.get("authorize"));
		return authuri;
	}

	public String requestAuthenticationCode() {
		String authuri = null;

		try {
			OkHttpClient client = new OkHttpClient();
			authuri = mappingPlaceholder(uris.get("authorize"));  logger.debug("getAuthenticationCode() > Authentication Url : "+ authuri);

			/*
			 * GET 'https://{mallid}.cafe24api.com/api/v2/oauth/authorize?response_type=code&client_id={client_id}&state={state}&redirect_uri={redirect_uri}&scope={scope}'
			 */
			Request.Builder requestBuilder = new Request.Builder().url(authuri).get();
			Request request = requestBuilder.build();

			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				ResponseBody body = response.body();
				if (body != null) {
					String sResponseBody = body.string();
					logger.debug("getAuthenticationCode() > Response:" + sResponseBody);
				}
			} else {
				logger.debug("getAuthenticationCode() > Error Occurred");
			}
			response.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return authuri;
	}
	public void requestAuthenticationCodeCallback(Map<String, String> map) {
		String code = map.get("code");
		if (!App.isNull(code)) {
			config.put("code" , code);
			config.put("token", code);
		}
	}

	/*
	 * curl -X POST \
	 *  'https://{mallid}.cafe24api.com/api/v2/oauth/token' \
	 *  -H 'Authorization: Basic {base64_encode({client_id}:{client_secret})}' \
	 *  -H 'Content-Type: application/x-www-form-urlencoded' \
	 *  -d 'grant_type=authorization_code&code={code}&redirect_uri={redirect_uri}'
	 */
	public void requestAccessToken() { requestAccessToken(false); }
	public void requestAccessToken(boolean isRefreshToken) {
		try {
			String tokenuri = mappingPlaceholder(uris.get("token"));
			String params   = !isRefreshToken ? "grant_type=authorization_code&code={code}&redirect_uri={redirect_uri}" : "grant_type=refresh_token&refresh_token={refresh_token}";

			String requestParam = mappingPlaceholder(params);  logger.debug("getAccessToken > requestParam: "+ requestParam);
			RequestBody requestBody = RequestBody.create(requestParam, MediaType.parse("application/x-www-form-urlencoded"));

			String authorization0 = encodeCredentials0(config.get("client_id"), config.get("client_secret"));  logger.debug("getAccessToken > authorization0: "+ authorization0);
//			String authorization1 = encodeCredentials1(config.get("client_id"), config.get("client_secret"));  logger.debug("getAccessToken > authorization1: "+ authorization1);

			Request request = new Request.Builder().url(tokenuri)
				.addHeader("Content-Type" , "application/x-www-form-urlencoded; charset=utf-8")
				.addHeader("Authorization", "Basic "+ authorization0)
				.post(requestBody)
				.build()
			;
			logger.debug("getAccessToken > request:\n"+ App.toJsonPretty( request ));

			OkHttpClient client = new OkHttpClient();
			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				ResponseBody body = response.body();
				if (body != null) {
					String sResponseBody = body.string();
					logger.debug("requestAccessToken() > ResponseBody:" + sResponseBody);

					requestAccessTokenCallback(sResponseBody);
				}
			} else {
				System.err.println("Error Occurred : "+ response.message());
			}
			response.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private String encodeCredentials0(String id, String secret) {
		String s = id + ":" + secret;
		return Base64.getEncoder().encodeToString(s.getBytes());
	}
//	private String encodeCredentials1(String id, String secret) {
//		String s = id + ":" + secret;
//
//		byte[] encodedBytes = org.apache.tomcat.util.codec.binary.Base64.encodeBase64(s.getBytes());
//		String encodedValue = new String(encodedBytes);
//
//		return encodedValue;
//	}
	@SuppressWarnings("unchecked")
	private void requestAccessTokenCallback(String s) {
		logger.debug(s);
		accessToken = App.fromJson(s, Map.class);

		config.put("access_token"            , (String) accessToken.get("access_token"            ));
		config.put("refresh_token"           , (String) accessToken.get("refresh_token"           ));
		config.put("refresh_token_expires_at", (String) accessToken.get("refresh_token_expires_at"));

		//conf.putAll(accessToken);
		// refresh_token_expires_at 스케쥴러 설정해야함.

	}

	/*
	 * curl -X POST \
	 *  'https://{mallid}.cafe24api.com/api/v2/oauth/token' \
	 *  -H 'Authorization: Basic {base64_encode({client_id}:{client_secret})}' \
	 *  -H 'Content-Type: application/x-www-form-urlencoded' \
	 *  -d 'grant_type=refresh_token&refresh_token={refresh_token}'
	 */
	public void requestRefreshAccessToken() {
		requestAccessToken(true);
	}





	public String mappingPlaceholder(String si) {
		return mappingPlaceholder(si, null, false);
	}
	public String mappingPlaceholder(String si, Map<String, ?> map) {
		return mappingPlaceholder(si, map, false);
	}
	public String mappingPlaceholder(String si, Map<String, ?> map, boolean isDebug) {
		if (si == null) { return si; }
		if (map == null || map.size() < 1) { map = config; }
		if (map == null || map.size() < 1) { return si; }

		String so = si;

		for (Map.Entry<String, ?> entry : map.entrySet()) {
			Object ov = entry.getValue();
			String k = entry.getKey();
			String v = ov != null && ov instanceof String ? ov.toString() : App.toJson(ov);
			if (isDebug) { System.out.println("Key: " + k + ", Value: " + v); }

			so = so.replaceAll("\\{"+k+"\\}", v);
		}
		so = so.replaceAll("\\{(\\w+)\\}", "");

		System.out.println("before: "+ si); 	// logger.debug("before: "+ si);
		System.out.println("after : "+ so); 	// logger.debug("after : "+ so);

		return so;
	}

	public Map<?, ?> callApi4PostJSON(String url, Map<String, Object> data) {
		return callApi4PostJSON(url, data, null);
	}
	public Map<?, ?> callApi4PostJSON(String url, Map<String, Object> data, Map<String, String> header) {
		Map<?, ?> m = null;
		if (url == null || data == null) { return m; }

		String requestUrl = mappingPlaceholder(url, data);

		if (header == null) { header = new HashMap<String, String>(); }
		header.put("Content-Type"        , "application/json");
		header.put("Authorization"       , "Bearer "+ config.get("access_token"));  //header.put("Authorization"       , "Basic "+ encodeCredentials0(config.get("client_id"), config.get("client_secret")));
		header.put("X-Cafe24-Api-Version", config.get("version"));

		RequestBody requestBody = buildRequestBodyJson(data);

		Request.Builder requestBuilder = new Request.Builder().url(requestUrl);
		buildRequestHeader(requestBuilder, header);

		Request request = requestBuilder.post(requestBody).build();

		try {
			OkHttpClient client = new OkHttpClient();
			Response response = client.newCall(request).execute();
			if (!response.isSuccessful() || response.body() == null) { return m; }
			ResponseBody body = response.body();
			if (body != null) {
				String responseBody = body.string();
				m = App.fromJson(responseBody, Map.class);
			}
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return m;
	}

	private Request.Builder buildRequestHeader(Request.Builder requestBuilder, Map<String, String> header) {
		if (requestBuilder == null) { requestBuilder = new Request.Builder(); }

		for (Map.Entry<String, String> entry : header.entrySet()) {
			requestBuilder.addHeader(entry.getKey(), entry.getValue());
		}

		return requestBuilder;
	}

	private RequestBody buildRequestBodyJson(Map<String, ?> data) {
		return RequestBody.create(App.toJson(data), MediaType.parse("application/json; charset=utf-8"));
	}


	// Map 데이터를 FormBody로 변환하는 메서드
	@SuppressWarnings("unused")
	private RequestBody buildRequestBodyForm(Map<String, String> formData) {
		FormBody.Builder builder = new FormBody.Builder();
		for (Map.Entry<String, String> entry : formData.entrySet()) {
			builder.add(entry.getKey(), entry.getValue());
		}
		return builder.build();
	}
	@SuppressWarnings("unused")
	private RequestBody buildRequestBodyMultipart(Map<String, String> formData) {
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

		for (Map.Entry<String, String> entry : formData.entrySet()) {
			builder.addFormDataPart(entry.getKey(), entry.getValue());
		}

		return builder.build();
	}

	@SuppressWarnings("unused")
	public static void main(String... args) {
		Cafe24AdminApiWorker worker = new Cafe24AdminApiWorker();
		Map<String, String> conf = worker.config;
		String s = worker.uris.get("authorize");

		worker.requestAuthenticationCode();



	}
}
