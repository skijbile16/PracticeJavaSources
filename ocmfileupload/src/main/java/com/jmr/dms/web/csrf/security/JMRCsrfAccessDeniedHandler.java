package com.jmr.dms.web.csrf.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

public class JMRCsrfAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if (accessDeniedException instanceof MissingCsrfTokenException) {
			String finalUrl = request.getContextPath() + "/login/invalidAccess";
			response.sendRedirect(finalUrl);
		} else if (accessDeniedException instanceof InvalidCsrfTokenException) {
			String finalUrl = request.getContextPath() + "/login/invalidAccess";
			response.sendRedirect(finalUrl);
		}
	}

}
