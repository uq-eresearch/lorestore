package oreservlet.servlets;

import oreservlet.common.ORETypeFactory;
import au.edu.diasb.annotation.danno.db.RDFDBContainerFactory;

public class OREControllerConfig {

	public RDFDBContainerFactory getContainerFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public UIDGenerator getUIDGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	public ORETypeFactory getTypeFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public OREAccessPolicy getAccessPolicy() {
		return null;
		
	}

	public int getBlankNodeClosureDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
