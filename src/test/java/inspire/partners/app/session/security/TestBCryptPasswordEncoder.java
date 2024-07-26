package inspire.partners.app.session.security;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCryptPasswordEncoder {

	@Test
	public void testcase001() {
		BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

		System.out.println( encode.encode("1q2w3e4r%T") );
	}
}
