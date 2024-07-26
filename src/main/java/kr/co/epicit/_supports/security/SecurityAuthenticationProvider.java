package kr.co.epicit._supports.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import kr.co.epicit.util.App;
import kr.co.epicit.util.AuthorizeCodeGenerator;

@Component
public class SecurityAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private SecurityUserDetailsService service;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username=(String) authentication.getPrincipal();
		String password=(String) authentication.getCredentials();

		SecurityUserDetails user = (SecurityUserDetails) service.loadUserByUsername(username);

		if (!matchPassword(password, user.getPassword())) { throw new BadCredentialsException("비밀번호 불일치 !!!!!"); }
		if (!user.isEnabled()) { throw new BadCredentialsException("계정 비활성화 !!!!!"); }

		// user.isAccountNonLocked()
		user.setToken(AuthorizeCodeGenerator.genAccessToken());

		return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		//return true;
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	private boolean matchPassword(String cPasswd, String sPasswd) {
		return App.isNull(cPasswd) || App.isNull(sPasswd) ? false : cPasswd.equals(sPasswd);
	}

}
