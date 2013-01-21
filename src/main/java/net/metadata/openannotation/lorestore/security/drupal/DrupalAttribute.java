package net.metadata.openannotation.lorestore.security.drupal;

import java.io.Serializable;

public class DrupalAttribute implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final String name;
    private final String value;

    public DrupalAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }

    public String getValue() {
    	return value;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("[");
        result.append(name);
        if (value != null) {
            result.append(":");
            result.append(value);
        }
        result.append("]");
        return result.toString();
    }
}
