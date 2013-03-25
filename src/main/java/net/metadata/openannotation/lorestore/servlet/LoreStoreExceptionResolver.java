package net.metadata.openannotation.lorestore.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.exceptions.NotFoundException;

import org.openrdf.query.MalformedQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import au.edu.diasb.chico.mvc.RequestFailureException;

public class LoreStoreExceptionResolver extends
        AbstractHandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(LoreStoreExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {
        logger.warn("Exception Resolver: " + ex.getMessage());
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (ex instanceof LoreStoreException || ex instanceof InvalidQueryParametersException || ex instanceof MalformedQueryException || ex instanceof RequestFailureException) {
            statusCode = HttpStatus.BAD_REQUEST.value();
        } else if (ex instanceof AccessDeniedException){
            statusCode = HttpStatus.FORBIDDEN.value();
        } else if (ex instanceof NotFoundException) {
            statusCode = HttpStatus.NOT_FOUND.value();
        }

        ModelAndView mav = new ModelAndView("exception");
        mav.addObject("statusCode",statusCode);
        mav.addObject("message", ex.getLocalizedMessage());
        mav.addObject("className", ClassUtils.getShortName(ex.getClass()));
        mav.addObject("stackTrace",ex.getStackTrace());
        return mav;
    }

}
