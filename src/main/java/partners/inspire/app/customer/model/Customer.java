package partners.inspire.app.customer.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
public class Customer implements Serializable {

	public Customer(String id, String name, String email, String mobile, String accesstoken) {
		this(id, name, email, mobile, accesstoken, "ROLE_USER", true);
	}
	public Customer(String id, String name, String email, String mobile, String accesstoken, String authority) {
		this(id, name, email, mobile, accesstoken, authority, true);
	}
	public Customer(String id, String name, String email, String mobile, String accesstoken, String authority, boolean enabled) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.accesstoken = accesstoken;
		this.authority = authority;
		this.enabled = enabled;
	}
	
	private String  id;
	private String  name;
	private String  email;
	private String  mobile;
	private String  authority;
	private String  accesstoken;
	private boolean enabled;
	
}
