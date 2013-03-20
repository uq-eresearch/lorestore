package net.metadata.openannotation.lorestore.servlet;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.servlet.ModelAndView;

import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;
import net.metadata.openannotation.lorestore.exceptions.NotFoundException;
import au.edu.diasb.chico.mvc.RequestFailureException;

public interface LoreStoreValidationHandler {
    public ModelAndView validate(InputStream inputRDF, String contentType)
            throws RequestFailureException, IOException, LoreStoreException,
            InterruptedException;

}