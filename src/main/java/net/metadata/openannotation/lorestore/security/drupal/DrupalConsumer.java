package net.metadata.openannotation.lorestore.security.drupal;

import javax.servlet.http.HttpServletRequest;

public interface DrupalConsumer {

    public String beginConsumption(HttpServletRequest req, String claimedIdentity, String returnToUrl, String realm)
            throws DrupalConsumerException;

    public DrupalAuthenticationToken endConsumption(HttpServletRequest req)
            throws DrupalConsumerException;

}
