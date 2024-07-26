package kr.co.epicit.util;

import java.security.SecureRandom;
import java.util.Base64;

public class AuthorizeCodeGenerator {

	// 인증 코드 생성 메소드
	public static String generateAuthCode() {
		return generateAuthCode(32);
	}
	public static String generateAuthCode(int length) {
		// SecureRandom 인스턴스 생성
		SecureRandom secureRandom = new SecureRandom();
		// 랜덤 바이트 배열 생성
		byte[] randomBytes = new byte[length];
		secureRandom.nextBytes(randomBytes);
		// Base64 인코딩 (URL-safe)
		String authCode = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
		return authCode;
	}

	public static String genAccessToken() {
		return generateAuthCode(32);
	}

	public static void main(String[] args) {
		// 32바이트 길이의 인증 코드 생성
		String authCode = generateAuthCode(32);
		System.out.println("Generated Auth Code: " + authCode);
	}

}
