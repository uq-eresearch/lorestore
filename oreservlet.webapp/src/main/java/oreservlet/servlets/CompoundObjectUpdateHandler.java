package oreservlet.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class CompoundObjectUpdateHandler {

	 
	
	public void create(HttpServletRequest request) throws IOException {
		request.getInputStream();
	}
	
	public void delete(HttpServletRequest request) {
		String pathInfo = request.getPathInfo();
	}
}
