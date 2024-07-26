package kr.co.epicit._supports.mvc.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HTMLTagFilterRequestWrapper extends HttpServletRequestWrapper {

	public HTMLTagFilterRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				values[i] = getSafeParamData(values[i]);
			} else {
				values[i] = null;
			}
		}
		return values;
	}

	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		value = getSafeParamData(value);
		return value;
	}

	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> valueMap = super.getParameterMap();
		String[] values;
		for (String key : valueMap.keySet()) {
			values = valueMap.get(key);
			for (int i = 0; i < values.length; i++) {
				if (values[i] != null) {
					values[i] = getSafeParamData(values[i]);
				} else {
					values[i] = null;
				}
			}
		}
		return valueMap;
	}

	public String getSafeParamData(String value) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
				case '<':
					stringBuilder.append("&lt;");
					break;
				case '>':
					stringBuilder.append("&gt;");
					break;
				case '&':
					stringBuilder.append("&amp;");
					break;
				case '"':
					stringBuilder.append("&quot;");
					break;
				case '\'':
					stringBuilder.append("&apos;");
					break;
				default:
					stringBuilder.append(c);
					break;
			}
		}
		value = stringBuilder.toString();
		return value;
	}

}
