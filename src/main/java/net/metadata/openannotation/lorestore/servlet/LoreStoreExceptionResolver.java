package net.metadata.openannotation.lorestore.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;

import org.openrdf.query.MalformedQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

public class LoreStoreExceptionResolver extends
        AbstractHandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(LoreStoreExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {
        logger.warn("Exception Resolver: " + ex.getMessage());
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        
        if (ex instanceof InvalidQueryParametersException || ex instanceof MalformedQueryException) {
            statusCode = HttpStatus.BAD_REQUEST.value();
        }
        response.setStatus(statusCode);
        ModelAndView mav = new ModelAndView("exception");
        mav.addObject("message", ex.getLocalizedMessage());
        mav.addObject("className", ClassUtils.getShortName(ex.getClass()));
        mav.addObject("stackTrace",ex.getStackTrace());
        return mav;
    }

}
