package oreservlet.servlets;

import javax.servlet.http.HttpServletRequest;

import org.ontoware.rdf2go.model.Model;

import au.edu.diasb.annotation.danno.model.AnnoteaObject;
import au.edu.diasb.chico.mvc.RequestFailureException;

public class DefaultOREAccessPolicy implements OREAccessPolicy {

	public void checkRead(HttpServletRequest request, Model res)
			throws RequestFailureException {
		// TODO Auto-generated method stub

	}

	public void checkCreate(HttpServletRequest request, Model res)
			throws RequestFailureException {
		// TODO Auto-generated method stub

	}

	public void checkUpdate(HttpServletRequest request, AnnoteaObject obj)
			throws RequestFailureException {
		// TODO Auto-generated method stub

	}

	public void checkDelete(HttpServletRequest request, AnnoteaObject obj)
			throws RequestFailureException {
		// TODO Auto-generated method stub

	}

	public void checkAdmin(HttpServletRequest request)
			throws RequestFailureException {
		// TODO Auto-generated method stub

	}

}
