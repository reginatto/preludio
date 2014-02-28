package com.preludio.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.preludio.services.Context;
import com.preludio.services.infra.auth.Authtoken;

public class AuthFilter implements Filter {

	private static final String LOGIN = "/login";

	private FilterConfig config;

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		Context ctx = (Context) request.getSession().getAttribute(Authtoken.TOKEN);
		if (ctx != null) {
			// user logged in, continue
			chain.doFilter(req, resp);
		} else {
			// no user logged in
			String completeBlock = this.config.getInitParameter("COMPLETE_BLOCK");
			if (completeBlock != null) {
				if (completeBlock.equals("1")) {
					// block resource since user is not logged in
					response.sendRedirect(LOGIN);
				} else if (completeBlock.equals("0")) {
					if (request.getMethod().toUpperCase().equals("GET")) {
						// allow read-only access even with user not logged in
						chain.doFilter(req, resp);
					} else {
						// block resource since user is not logged in
						response.sendRedirect(LOGIN);
					}
				}
			} else {
				// resource not configured, continue
				chain.doFilter(req, resp);
			}
		}
	}

	@Override
	public void destroy() {
	}

	/*
	private void forward(String destination, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dispatcher = req.getRequestDispatcher(destination);
		dispatcher.forward(req, resp);
	}
	*/
}
