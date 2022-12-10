package com.lp.server.system.jcr.ejbfac;

import java.io.IOException;
import java.util.List;

import javax.ejb.Local;
import javax.jcr.InvalidItemStateException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.version.VersionException;
import javax.naming.NamingException;

import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.JcrDmsConfig;
import com.lp.server.system.jcr.service.docnode.DocPath;

@Local
public interface JcrDmsRepoFac {
	class HvProperty {
		// Konstanten definieren fuer Propertynamen
		public final static String BELEGART = "BELEGART";
		public final static String ANLEGER = "ANLEGER";
		public final static String PARTNER = "PARTNER";
		public final static String ZEITPUNKT = "ZEITPUNKT";
		public final static String BELEGNUMMER = "BELEGNUMMER";
		public final static String SCHLAGWORTE = "SCHLAGWORTE";
		public final static String NAME = "NAME";
		public final static String FILENAME = "FILENAME";
		public final static String TABLE = "TABLE";
		public final static String ROW = "ROW";
		public final static String MIME = "MIME";
		public final static String VERSION = "VERSION";
		public final static String VERSTECKT = "VERSTECKT";
		public final static String SICHERHEITSSTUFE = "SICHERHEITSSTUFE";
		public final static String GRUPPIERUNG = "GRUPPIERUNG";
		public final static String DATA = "DATA";		
	}

	
	JCRRepoInfo existsNode(JcrDmsConfig config, DocPath dPath) throws NamingException ;	
	boolean existsNodeWithinTransaction(JcrDmsConfig config, DocPath dPath) throws NamingException ; 
	
	boolean existsNodeWithinTransaction(JcrDmsConfig config, String path) throws NamingException ;	

	void iterateRepository(JcrDmsConfig config, DmsNodeVisitor nodeFunctor) throws NamingException, RepositoryException ;
	void iterateRepository(String basePath, JcrDmsConfig config, DmsNodeVisitor nodeFunctor)
			throws NamingException, RepositoryException;
	
	void createPath(JcrDmsConfig config, String path) throws NamingException, RepositoryException ;
	
	Node addNewDocumentOrNewVersionOfDocument(JcrDmsConfig config, JCRDocDto jcrDocDto) throws NamingException ;

	Node addNewDocumentOrNewVersionOfDocumentWithinTransaction(
			JcrDmsConfig config, JCRDocDto jcrDocDto) throws NamingException ;	
	
	Node addNewDocumentOrNewVersionOfDocumentNew(
			JcrDmsConfig config, JCRDocDto jcrDocDto) throws NamingException;
	
	DocPath getDocPathFromJCR(JcrDmsConfig config, JCRDocDto jcrDocDto)
			throws ValueFormatException, PathNotFoundException, RepositoryException, IOException, NamingException ;
	void commitNodes(List<Node> commitNodes)  throws NamingException, VersionException,
	UnsupportedRepositoryOperationException, InvalidItemStateException, LockException, RepositoryException;
	void close();
}
