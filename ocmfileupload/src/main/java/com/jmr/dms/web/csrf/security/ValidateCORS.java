package com.jmr.dms.web.csrf.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;

public class ValidateCORS implements Filter {
	private static final Logger logger = Logger.getLogger(ValidateCORS.class);
	private JMRCsrfAccessDeniedHandler accessDeniedHandler = new JMRCsrfAccessDeniedHandler();
	private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

	HashSet<String> sqlKeywords = new HashSet<String>();

	@SuppressWarnings("deprecation")
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();

		// Authorize (allow) all domains to consume the content
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");

		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
		// For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per
		// CORS handshake
		if (request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}

		logger.info("ValidateCORS.doFilter(-,-,-) ************************  START :=> Request URL: " + path);
		logger.info("ValidateCORS.doFilter(-,-,-) context path" + contextPath);
		// TODO Auto-generated method stub

		/*
		 * ignore ArrayList Contain list of URL need not to validate CSRF
		 * Validation
		 * 
		 * 
		 */
		/*if (!response.isCommitted()) {
			response.addHeader("X-XSS-Protection", "1; mode=block");
			response.addHeader("X-Content-Type-Options", "nosniff");
			response.addHeader("X-Frame-Options", "DENY");
			response.addHeader("Access-Control-Allow-Origin", "DENY");
			response.addHeader("Access-Control-Allow-Credentials", "true");
		}*/
		List<String> ignore = new ArrayList<String>();
		ignore.add(contextPath + "/shareholder/user-registration");
		ignore.add(contextPath + "/shareholder/shareholder/user-registration");
		ignore.add(contextPath + "/j_spring_security_check");
		ignore.add(contextPath + "/login/invalidAccess");
		ignore.add(contextPath + "/");
		if (sqlKeywords.size() <= 0) {
			sqlKeywords.add("Create");
			sqlKeywords.add("Alter");
			sqlKeywords.add("Drop");
			sqlKeywords.add("Modify");
			sqlKeywords.add("Truncate");
			sqlKeywords.add("Grant");
			sqlKeywords.add("revoke");
			sqlKeywords.add("privileges");
			sqlKeywords.add("select");
			sqlKeywords.add("from");
			sqlKeywords.add("where");
			sqlKeywords.add("drop");
			sqlKeywords.add("delete");
		}

		if (allowedMethods.matcher(request.getMethod()).matches()) {
			if (isNotValidGetRequest(request, sqlKeywords)) {
				accessDeniedHandler.handle(request, response, new AccessDeniedException("SQL Containt Found"));
				response.setStatus(403, "invalid request.");
				return;
			}
		}
		chain.doFilter(request, response);
		logger.info("ValidateCORS.doFilter(-,-,-) ************************  END :=> Request URL: " + path);
	}

	@Override
	public void destroy() {
	}

	public static HttpSession getSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		session = request.getSession();
		return session;
	}

	@SuppressWarnings("unchecked")
	public static boolean isNotValidGetRequest(HttpServletRequest request, HashSet<String> sqlKeywords) {
		boolean isInvalidRequest = false;
		Map<?, ?> map = request.getParameterMap();
		Set<?> s = map.entrySet();
		Iterator<?> it = s.iterator();
		HashSet<String> uniqueGetRequestParam = new HashSet<>();

		while (it.hasNext()) {
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			uniqueGetRequestParam.add(key);
			String[] vals = (String[]) value;
			for (String val : vals) {
				uniqueGetRequestParam.add(String.valueOf(val));
			}

		}
		for (Object paramKeyOrValue : uniqueGetRequestParam) {
			for (String keyword : sqlKeywords) {

				if (paramKeyOrValue.toString().toLowerCase().indexOf(keyword.toLowerCase()) != -1) {
					logger.info("ValidateCORS.isNotValidGetRequest(-)  Request Data: " + paramKeyOrValue
							+ " *******************  Content Found: " + keyword);
					isInvalidRequest = true;

				}
				if (isInvalidRequest)
					break;
			}
			if (isInvalidRequest)
				break;
		}

		return isInvalidRequest;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
