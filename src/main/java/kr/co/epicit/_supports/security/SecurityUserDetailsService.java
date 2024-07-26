package kr.co.epicit._supports.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import kr.co.epicit.util.App;

@Component
public class SecurityUserDetailsService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(SecurityUserDetailsService.class);

	@SuppressWarnings("unused")
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SecurityUserDetailsRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.debug("SecurityUserDetailsService.loadUserByUsername()");
		if ( App.isNull(username) ) { throw new UsernameNotFoundException(username); }

		SecurityUserDetails loadUser = null;
		loadUser = repo.loadUserByUsername(username);

		// logger.debug(App.toJsonPretty(loadUser));
		// loadUser.isAccountNonLocked()

		// return UserPrincipal.create(loadUser);
		return loadUser;  // loadUser.deepcopy();
	}

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// 사용자 로드 로직 (예: 데이터베이스 조회)
//		// UserDetails 반환
//		return new org.springframework.security.core.userdetails.User(
//				username,
//				"{noop}password", // {noop}는 암호화 없이 저장된 암호를 의미
//				new ArrayList<>()
//		);
//	}

}
