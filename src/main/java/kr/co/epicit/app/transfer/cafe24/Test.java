package kr.co.epicit.app.transfer.cafe24;

import java.util.Arrays;
import java.util.List;

public class Test {

	public static void main(String... args) {

		List<String> scopes = Arrays.asList(
				"mall.read_application",
				"mall.write_application",
				"mall.read_category",
				"mall.read_product",
				"mall.read_order",
				"mall.read_customer",
				"mall.write_customer",
				"mall.read_customer",
				"mall.read_promotion",
				"mall.read_mileage",
				"mall.read_shipping",
				"mall.read_privacy",
				""
			);
		scopes.removeAll(Arrays.asList(null, ""));

		String scope = scopes.toString().replaceAll("\\[|\\]| ", "");
		System.out.println(scope);
	}
}
