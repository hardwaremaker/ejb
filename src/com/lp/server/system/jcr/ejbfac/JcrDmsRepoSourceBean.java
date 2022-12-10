package com.lp.server.system.jcr.ejbfac;

import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.JcrDmsConfig;
import com.lp.server.system.jcr.service.docnode.DocPath;

@Singleton
@Local
public class JcrDmsRepoSourceBean extends JcrDmsRepoBaseBean {
	@PersistenceContext
	private EntityManager em;

	public JcrDmsRepoSourceBean() throws NamingException {
		super();
	}
	
	
	// Wenn es klappt, dann dient das nur dazu, dass der EJB Injector diese Bean als gueltige anerkennt.
	// Bitte drinnen lassen
	@Override
	public JCRRepoInfo existsNode(JcrDmsConfig config, DocPath dPath)
			throws NamingException {
		return super.existsNode(config, dPath);
	}
}
