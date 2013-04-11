package net.metadata.openannotation.security.drupal;

import static org.junit.Assert.*;

import javax.servlet.ServletException;

import net.metadata.openannotation.lorestore.security.drupal.DrupalSessionAuthenticationFilter;

import org.junit.Test;

public class DrupalSessionAuthenticationFilterTest {

	@Test
	public void testInitFilterBean() throws ServletException {
		DrupalSessionAuthenticationFilter filter = new DrupalSessionAuthenticationFilter();
		filter.setDrupalHostname("localhost");
		filter.afterPropertiesSet();
		
		assertEquals("SESS49960de5880e8c687434170f6476605b", filter.getDrupalCookieName());
	}

}
