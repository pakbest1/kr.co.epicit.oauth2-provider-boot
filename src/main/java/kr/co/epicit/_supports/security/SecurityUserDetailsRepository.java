package kr.co.epicit._supports.security;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import kr.co.epicit.util.AppRepository;

@Component
public class SecurityUserDetailsRepository {

	private List<SecurityUserDetails> list = AppRepository.listUserDetails;
//	new ArrayList<SecurityUserDetails>() {{
//		add(new SecurityUserDetails("test00", "테스트00", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "email00@domain.admin", "010-1234-5678", true, "ROLE_ADMIN", "adm001025586118"));
//		add(new SecurityUserDetails("test01", "테스트01", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "email01@domain.user" , "010-1234-5678", true, "ROLE_USER" , "usr011025586118"));
//		add(new SecurityUserDetails("test02", "테스트02", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "email02@domain.user" , "010-1234-5678", true, "ROLE_USER" , "usr021025586118"));
//		add(new SecurityUserDetails("test03", "테스트03", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "email03@domain.user" , "010-1234-5678", true, "ROLE_USER" , "usr031025586118"));
//		add(new SecurityUserDetails("test04", "테스트04", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "email04@domain.user" , "010-1234-5678", true, "ROLE_USER" , "usr041025586118"));
//		add(new SecurityUserDetails("test05", "테스트05", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "email05@domain.user" , "010-1234-5678", true, "ROLE_USER" , "usr051025586118"));
//	}};

//	@PostConstruct
//	public void postConstruct() {
//		list = new ArrayList<UserDetails>() {{
//			add(new SecurityUserDetails("test01", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "테스트01"));
//			add(new SecurityUserDetails("test02", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "테스트02"));
//			add(new SecurityUserDetails("test03", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "테스트03"));
//			add(new SecurityUserDetails("test04", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "테스트04"));
//			add(new SecurityUserDetails("test05", "$2a$10$uwpiMcK6Y/cdCw1NQLMR3.6Uia11DfynbAzshxC4DbttVuB9T7Og2", "테스트05"));
//		}};
//
//	}
	public boolean commit(SecurityUserDetails pSecurityUserDetails) {
		return commit(Arrays.asList( pSecurityUserDetails )) ;
	}

	public boolean commit(List<SecurityUserDetails> lpSecurityUserDetails) {
		boolean r = false;
		if (lpSecurityUserDetails == null || lpSecurityUserDetails.size() < 0 || list == null || list.size() < 1) { return r; }

		for (SecurityUserDetails pSecurityUserDetails : lpSecurityUserDetails) {
			if (pSecurityUserDetails == null || pSecurityUserDetails.getUsername() == null) { continue; }
			String pUsername = pSecurityUserDetails.getUsername();

			for (int i=0; i < list.size(); i++) {  //for (SecurityUserDetails lSecurityUserDetails : list) {
				SecurityUserDetails lsSecurityUserDetails = list.get(i);
				if (lsSecurityUserDetails == null || lsSecurityUserDetails.getUsername() == null) { continue; }
				String lUsername = lsSecurityUserDetails.getUsername();
				if (!pUsername.equals(lUsername)) { continue; }

				list.set(i, pSecurityUserDetails);
				r = true;
			}

		}

		return r;
	}


	public SecurityUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// username이 'aa'인 첫 번째 항목 필터링
		Optional<SecurityUserDetails> firstItem = list.stream()
				.filter(item -> username.equals(item.getUsername()))
				.findFirst();

		return firstItem.isPresent() ? firstItem.get() : null;
	}

	public SecurityUserDetails loadUserByToken(String token) throws UsernameNotFoundException {
		// username이 'aa'인 첫 번째 항목 필터링
		Optional<SecurityUserDetails> firstItem = list.stream()
				.filter(item -> token.equals(item.getToken()))
				.findFirst();
		// Optional.empty
		return firstItem.isPresent() ? firstItem.get() : null;
	}

	public SecurityUserDetails loadUserByAccessToken(String accessToken) throws UsernameNotFoundException {
		// username이 'aa'인 첫 번째 항목 필터링
		Optional<SecurityUserDetails> firstItem = list.stream()
				.filter(item -> accessToken.equals(item.getAccessToken()))
				.findFirst();
		// Optional.empty
		return firstItem.isPresent() ? firstItem.get() : null;
	}

}
