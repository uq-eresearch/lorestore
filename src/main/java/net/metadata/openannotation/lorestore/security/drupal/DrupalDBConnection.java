package net.metadata.openannotation.lorestore.security.drupal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class DrupalDBConnection{

    private String database;
	private String hostname;
    private String username;
    private String password;
    
    private static final Logger logger = 
            Logger.getLogger(DrupalAuthenticationFilter.class);
    
    public String[] getUserDetails(String sid) {
        String[] results = new String[3];
        
        Connection conn;
		try {
			conn = DriverManager.getConnection(
			     "jdbc:mysql://" + hostname + "/" + database + "?autoReconnect=true",
			     username, password );
			try {
				Statement stmt = conn.createStatement();
				try {					
				    ResultSet rs = stmt.executeQuery( 
				    		"SELECT users.uid, users.name, users.mail " +
				    		"FROM sessions, users " +
				    		"WHERE sid='" + sid + "' " +
				    		"AND sessions.uid = users.uid" );
				    try {
				        while ( rs.next() ) {
				        	results[0] = rs.getString(1);
				        	results[1] = rs.getString(2);
				        	results[2] = rs.getString(3);
				        }
				    } finally {
				        try { rs.close(); } catch (Exception e) {
					    	logger.debug(e);
					    }
				    }
				} finally {
				    try { stmt.close(); } catch (Exception e) {
				    	logger.debug(e);
				    }
				}
			} finally {
			    try { conn.close(); } catch (Exception e) {
			    	logger.debug(e);
			    }
			}
		} catch (Exception e) {
			logger.debug(e);
		}
        
        return results;
    }
    
    public boolean sharedGroupMembershipWithOwner(ArrayList<String> ownerIDs, ArrayList<String> authIDs) {        	
    	if (ownerIDs.size() == 0 || authIDs.size() == 0) {
    		return false;
    	}
    	
    	String queryStr = "SELECT * " +
			"FROM og_membership o1, og_membership o2 " +
			"WHERE o1.gid = o2.gid ";
    	if (ownerIDs.size() == 1) {
    		queryStr += "AND o1.etid = '" + ownerIDs.get(0) + "' ";
    	} else {
    		queryStr += "AND (o1.etid = '" + ownerIDs.get(0) + "'";
    		for (int i = 1; i < ownerIDs.size(); i++) {
    			queryStr += " OR o1.etid = '" + ownerIDs.get(0) + "'";
    		}
    		queryStr += ") ";
    	}
    	if (authIDs.size() == 1) {
    		queryStr += "AND o2.etid = '" + authIDs.get(0) + "' ";
    	} else {
    		queryStr += "AND (o2.etid = '" + authIDs.get(0) + "'";
    		for (int i = 1; i < authIDs.size(); i++) {
    			queryStr += " OR o2.etid = '" + authIDs.get(0) + "'";
    		}
    		queryStr += ") ";
    	}    	
    	    	
    	Connection conn;
		try {
			conn = DriverManager.getConnection(
			     "jdbc:mysql://" + hostname + "/" + database + "?autoReconnect=true",
			     username, password );
			try {
				Statement stmt = conn.createStatement();
				try {					
				    ResultSet rs = stmt.executeQuery(queryStr);
				    try {
				        while ( rs.next() ) {
				        	return true;
				        }
				    } finally {
				        try { rs.close(); } catch (Exception e) {
					    	logger.debug(e);
					    }
				    }
				} finally {
				    try { stmt.close(); } catch (Exception e) {
				    	logger.debug(e);
				    }
				}
			} finally {
			    try { conn.close(); } catch (Exception e) {
			    	logger.debug(e);
			    }
			}
		} catch (Exception e) {
			logger.debug(e);
		}
    	
    	return false;
    }
    

    public void setDatabase(String database) {
		this.database = database;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
}