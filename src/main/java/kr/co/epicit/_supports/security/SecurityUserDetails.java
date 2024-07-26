package kr.co.epicit._supports.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

// @Entity
@Data
@SuppressWarnings("serial")
public class SecurityUserDetails implements UserDetails, Serializable {
//	//@Id
//	private String username;
//	private String password;
//	private boolean enabled;

	private String  username;
	private String  password;
	private String  name;
	private boolean enabled;
	private String  authority;

	public SecurityUserDetails() {}
	public SecurityUserDetails(String username, String loginName, String password) {
		this(username, loginName, password, null, null, true, null);
	}
	public SecurityUserDetails(String username, String loginName, String password, String email, String mobile, boolean enabled) {
		this(username, loginName, password, email, mobile, enabled, null);
	}
	public SecurityUserDetails(String username, String loginName, String password, String email, String mobile, boolean enabled, String authority) {
		this(username, loginName, password, email, mobile, enabled, authority, null);
	}
	public SecurityUserDetails(String username, String loginName, String password, String email, String mobile, boolean enabled, String authority, String accessToken) {
		this.loginId     = username   ;
		this.loginName   = loginName  ;
		this.email       = email      ;
		this.mobile      = mobile     ;
		this.accessToken = accessToken;

		this.username    = username ;
		this.name        = loginName;
		this.password    = password ;
		this.enabled     = enabled  ;
		this.authority   = authority;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();

		auth.add(new SimpleGrantedAuthority(authority));

		return auth;
	}

	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public String getName() {
		return name;
	}



	@Override
	public boolean isAccountNonExpired() {
		return enabled;
	}
	@Override
	public boolean isAccountNonLocked() {
		return enabled;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return enabled;
	}


	private String loginId  ;
	private String loginName;
	private String email    ;
	private String mobile   ;


	private String token;
	private String accessToken;


	public SecurityUserDetails deepcopy() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this);

			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bis);

			return (SecurityUserDetails) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
