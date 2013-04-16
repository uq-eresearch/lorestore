package net.metadata.openannotation.lorestore.model;

import net.metadata.openannotation.lorestore.exceptions.LoreStoreException;

public interface OpenAnnotation extends NamedGraph {

	
	void setUserWithName(String uri, String username) throws LoreStoreException;
}
