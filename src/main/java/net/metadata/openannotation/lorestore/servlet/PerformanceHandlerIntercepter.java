package net.metadata.openannotation.lorestore.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PerformanceHandlerIntercepter extends HandlerInterceptorAdapter {

	private int logSlowerThan = 100;
	
	private final Logger LOG = Logger
			.getLogger(PerformanceHandlerIntercepter.class);

	/**
	 * Adds a start time attribute for performance logging. This implementation
	 * always returns <code>true</code>.
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		request.setAttribute("stopWatch", (Long) System.currentTimeMillis());
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		long startTime = (Long) request.getAttribute("stopWatch");
		long elapsedTime = System.currentTimeMillis() - startTime;
		if (elapsedTime >= logSlowerThan) {
			LOG.info("Slow Query URL: " + getUrl(request) + " Elapsed time: "
					+ elapsedTime + "ms");
		}
	}
	public String getUrl(HttpServletRequest req) {
	    String reqUrl = req.getRequestURL().toString();
	    String queryString = req.getQueryString();   // d=789
	    if (queryString != null) {
	        reqUrl += "?"+queryString;
	    }
	    return reqUrl;
	}

	public void setLogSlowerThan(int logSlowerThan) {
		this.logSlowerThan = logSlowerThan;
	}

	public int getLogSlowerThan() {
		return logSlowerThan;
	}
}
