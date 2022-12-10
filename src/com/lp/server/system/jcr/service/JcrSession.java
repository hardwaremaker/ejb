package com.lp.server.system.jcr.service;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;

public class JcrSession {
	private static final ILPLogger myLogger = LPLogService.getInstance().getLogger(JcrSession.class);

	private Session session;
	private Repository repository;
	private boolean isWildfly;

	public JcrSession() {
	}

	public Session getSession() throws NamingException, RepositoryException {
		if (session == null) {
			myLogger.info("jcrsession::login to repository necessary");

			if (!isOnline()) {
				myLogger.info("repository not available");
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE, "repo == null");
			}

			Credentials creds = isWildfly ? new SimpleCredentials("jcr", "jcr".toCharArray())
					: new SimpleCredentials("Anonymous", "".toCharArray());

			session = repository.login(creds);
			myLogger.info("jcrsession::login to repository done.");
		}

		return session;
	}

	public void closeSession() {
		if (session != null) {
			myLogger.info("jcrSession::logout from repository");
			try {
				if (session.isLive()) {
					session.logout();
					session = null;
				} else {
					myLogger.warn("jcrsession::session was not live?");
				}
			} catch (IllegalStateException e) {
				myLogger.error("jcrSession::closeSession:IllegalState", e);
			}
		} else {
			myLogger.info("jcrSession::no logout necessary?");
		}

		session = null;
		repository = null;
	}

	public void forcedCloseSession() {
		if (null != session) {
			myLogger.info("jcrSession::forced logout from repository");
		}
		session = null;
		repository = null;
	}

	public boolean isOnline() throws NamingException {
		createRepo();
		return repository != null;
	}

	public Node getRootNode() throws NamingException, RepositoryException {
		return getSession().getRootNode();
	}

	public Node getNode(String path) {
		try {
			Node rootNode = null;
			try {
				rootNode = getSession().getRootNode();
			} catch (IllegalStateException e) {
				// session timed out?
				// session = repo.login(cred);
				myLogger.warn("jcr:IllegalState:getNode", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			} catch (RepositoryException e) {
				myLogger.warn("jcr:Repository:getNode", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			}

			Node returnNode = null;
			try {
				returnNode = rootNode.getNode(path);
			} catch (PathNotFoundException ex) {
				// Den Knoten gibt es nicht Null zurueckgeben
			}

			return returnNode;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public Node getNode(DocPath docPath) {
		return getNode(docPath.getPathAsString());
	}

	public void save() throws NamingException, RepositoryException {
		getSession().save();
	}

	private void createRepo() throws NamingException {
		if (repository != null) {
			return;
		}

		InitialContext ctx = new InitialContext();

		try {
			// Wildfly
			repository = (Repository) ctx.lookup("java:/jcr/local");
			isWildfly = true;
			myLogger.info("jcrSession::created Wildfly JCR-Repo");
			return;
		} catch (NameNotFoundException ex) {
			try {
				// JBoss
				repository = (Repository) ctx.lookup("java:jcr/local");
				isWildfly = false;
				myLogger.info("jcrSession::created JBoss JCR-Repo");
				return;
			} catch (NameNotFoundException ex2) {
				// OFFLINE FUER JBOSS
				myLogger.error("Couldn't find jcr-name (/jcr/local)", ex2);
			} catch (java.lang.ClassCastException ex2) {
				myLogger.error("Naming Wildfly", ex2);
			}
		}

		myLogger.error("jcrSession::Couldn't create JCR-Repo!");
	}
}