package net.metadata.auselit.lorestore.access;

import net.metadata.auselit.lorestore.model.NamedGraph;

import org.ontoware.rdf2go.model.Model;


public class AllowEverythingAccessPolicy implements LoreStoreAccessPolicy {

	public void checkRead(NamedGraph res) {

	}

	public void checkCreate( Model res) {

	}

	public void checkUpdate( NamedGraph obj) {

	}

	public void checkDelete( NamedGraph obj) {

	}

	public void checkAdmin() {

	}

}
