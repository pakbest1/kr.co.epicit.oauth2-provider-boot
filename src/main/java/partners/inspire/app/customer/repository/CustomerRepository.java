package partners.inspire.app.customer.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import partners.inspire.app.customer.model.Customer;

@Repository
public class CustomerRepository {
	private static final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);
	
	@SuppressWarnings("serial")
	private List<Customer> repo = new ArrayList<Customer>() {{
		add(new Customer("ausr00", "관리자00", "ausr00@inspire.partners", "010-2558-6118", "a001025586118", "ROLE_ADMIN"));
		add(new Customer("cust01", "사용자01", "cust01@inspire.partners", "010-2558-6118", "c101025586118"));
		add(new Customer("cust02", "사용자02", "cust02@inspire.partners", "010-2558-6118", "c201025586118"));
	}};

	public Customer selectById(String username) {
		logger.debug("selectById("+ username +")");
		Optional<Customer> firstItem = repo.stream()
				.filter(item -> username.equals(item.getId()))
				.findFirst();
		
		return firstItem.get();
	}
	
	public Customer selectByAccesstoken(String accessToken) {
		logger.debug("selectById("+ accessToken +")");
		
		Optional<Customer> firstItem = repo.stream()
				.filter(item -> accessToken.equals(item.getAccesstoken()))
				.findFirst();
		
		return firstItem.get();
	}
}
