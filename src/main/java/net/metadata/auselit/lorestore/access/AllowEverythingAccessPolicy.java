package net.metadata.auselit.lorestore.access;

import net.metadata.auselit.lorestore.model.CompoundObject;

import org.ontoware.rdf2go.model.Model;


public class AllowEverythingAccessPolicy implements OREAccessPolicy {

	public void checkRead(Model res) {

	}

	public void checkCreate( Model res) {

	}

	public void checkUpdate( CompoundObject obj) {

	}

	public void checkDelete( CompoundObject obj) {

	}

	public void checkAdmin() {

	}

}
