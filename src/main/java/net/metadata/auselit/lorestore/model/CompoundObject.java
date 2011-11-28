package net.metadata.auselit.lorestore.model;

public interface CompoundObject extends NamedGraph {
    
	
	/**
	 * Is this compound object locked for editing.
	 * 
	 * A locked compound object can no longer be edited (including being unlocked),
	 * except by an administrator.
	 * 
	 * @return
	 */
	public boolean isLocked();
}
