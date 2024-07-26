package kr.co.epicit.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import kr.co.epicit._supports.security.SecurityUserDetails;

@Component
public class AppRepository {

	private static BCryptPasswordEncoder pwencode = new BCryptPasswordEncoder();

	@SuppressWarnings("serial")
	public static List<SecurityUserDetails> listUserDetails = new ArrayList<SecurityUserDetails>() {{
		add(new SecurityUserDetails("test00", "테스트00", pwencode.encode("1q2w3e4r%T")/*"$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2"*/, "email00@domain.admin", "010-2558-6118", true, "ROLE_ADMIN", "adm001025586118"));
		add(new SecurityUserDetails("test01", "테스트01", pwencode.encode("1q2w3e4r%T")/*"$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2"*/, "email01@domain.user" , "010-2558-6118", true, "ROLE_USER" , "usr011025586118"));
		add(new SecurityUserDetails("test02", "테스트02", pwencode.encode("1q2w3e4r%T")/*"$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2"*/, "email02@domain.user" , "010-2558-6118", true, "ROLE_USER" , "usr021025586118"));
		add(new SecurityUserDetails("test03", "테스트03", pwencode.encode("1q2w3e4r%T")/*"$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2"*/, "email03@domain.user" , "010-2558-6118", true, "ROLE_USER" , "usr031025586118"));
		add(new SecurityUserDetails("test04", "테스트04", pwencode.encode("1q2w3e4r%T")/*"$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2"*/, "email04@domain.user" , "010-2558-6118", true, "ROLE_USER" , "usr041025586118"));
		add(new SecurityUserDetails("test05", "테스트05", pwencode.encode("1q2w3e4r%T")/*"$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2"*/, "email05@domain.user" , "010-2558-6118", true, "ROLE_USER" , "usr051025586118"));
	}};

}
