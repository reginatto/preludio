package com.preludio.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public abstract class BaseServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		if (req.getParameter(HTTP.FORCE_METHOD_FIELD) != null) {
			if (req.getParameter(HTTP.FORCE_METHOD_FIELD).equals(HTTP.PUT_METHOD)) {
				// workaround to allow PUT method from HTML forms
				this.__doPut(req, resp);
			} else if (req.getParameter(HTTP.FORCE_METHOD_FIELD).equals(HTTP.DELETE_METHOD)) {
				// workaround to allow DELETE method from HTML forms
				this.__doDelete(req, resp);
			} else {
				this.__doPost(req, resp);
			}
		} else {
			this.__doPost(req, resp);
		}
	}

	protected abstract void __doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		this.__doGet(req, resp);
	}

	protected abstract void __doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;

	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		this.__doPut(req, resp);
	}

	protected abstract void __doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException;

	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		this.__doDelete(req, resp);
	}

	protected abstract void __doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException;

	protected void redirect(String destination, HttpServletResponse resp) throws IOException {
		resp.sendRedirect(resp.encodeRedirectURL(destination));
	}

	protected void forward(String destination, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dispatcher = req.getRequestDispatcher(destination);
		dispatcher.forward(req, resp);
	}

}
