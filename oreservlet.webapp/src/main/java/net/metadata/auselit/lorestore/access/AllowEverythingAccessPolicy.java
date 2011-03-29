package net.metadata.auselit.lorestore.access;

import net.metadata.auselit.lorestore.model.CompoundObject;

import org.ontoware.rdf2go.model.Model;

import au.edu.diasb.chico.mvc.RequestFailureException;

public class AllowEverythingAccessPolicy implements OREAccessPolicy {

	public void checkRead(Model res)
			throws RequestFailureException {

	}

	public void checkCreate( Model res)
			throws RequestFailureException {

	}

	public void checkUpdate( CompoundObject obj)
			throws RequestFailureException {

	}

	public void checkDelete( CompoundObject obj)
			throws RequestFailureException {

	}

	public void checkAdmin()
			throws RequestFailureException {

	}

}
