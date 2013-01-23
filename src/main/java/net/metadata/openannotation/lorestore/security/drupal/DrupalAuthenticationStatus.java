package net.metadata.openannotation.lorestore.security.drupal;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class DrupalAuthenticationStatus implements Serializable {
    
    private static final long serialVersionUID = -998877665544332211L;
    private static int nextOrdinal = 0;

    public static final DrupalAuthenticationStatus SUCCESS = new DrupalAuthenticationStatus("success");

    public static final DrupalAuthenticationStatus FAILURE = new DrupalAuthenticationStatus("failure");

    public static final DrupalAuthenticationStatus ERROR = new DrupalAuthenticationStatus("error");

    public static final DrupalAuthenticationStatus SETUP_NEEDED = new DrupalAuthenticationStatus("setup needed");

    public static final DrupalAuthenticationStatus CANCELLED = new DrupalAuthenticationStatus("cancelled");
    private static final DrupalAuthenticationStatus[] PRIVATE_VALUES = {SUCCESS, FAILURE, ERROR, SETUP_NEEDED, CANCELLED};

    private String name;
    private final int ordinal = nextOrdinal++;

    private DrupalAuthenticationStatus(String name) {
        this.name = name;
    }

    private Object readResolve() throws ObjectStreamException {
        return PRIVATE_VALUES[ordinal];
    }

    public String toString() {
        return name;
    }
}
