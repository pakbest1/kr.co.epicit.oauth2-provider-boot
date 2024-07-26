package kr.co.epicit._supports.mvc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * HTMLTagFilter.java
 *
 * @author 실행환경 개발팀 함철
 * @since 2009.06.01
 * @version 1.0
 * <pre>
 * 개정이력(Modification Information)
 *
 * 수정일		수정자				수정내용
 * ----------------------------------------------
 * 2009.05.30	함철				최초 생성
 * </pre>
 */
public class HTMLTagFilter implements Filter {

	@SuppressWarnings("unused")
	private FilterConfig config;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new HTMLTagFilterRequestWrapper((HttpServletRequest) request), response);
	}

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	public void destroy() {
	}

}
