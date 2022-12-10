package com.lp.server.system.jcr.ejbfac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jcr.Credentials;
import javax.jcr.InvalidItemStateException;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.JcrDmsConfig;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFactory;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.jcr.service.docnode.visualizer.VisualNodeLiteral;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;



public class JcrDmsRepoBaseBean extends Facade implements JcrDmsRepoFac {
	private InitialContext ctx;
	private Repository repo;
	private Credentials cred;

	private Session session;

	private JcrDmsConfig dmsConfig ;
	
	public JcrDmsRepoBaseBean() throws NamingException {
		super() ;

		ctx = new InitialContext();
		cred = null ;
		session = null ;		
	}
	
	protected Repository connectRepository() throws NamingException {
		if(repo == null) {
			repo = (Repository) ctx.lookup(dmsConfig.getJniRepoName());			
		}

		return repo ;
	}
	
	protected Credentials getCredentials() {
		if(cred == null) {
			cred = new SimpleCredentials(dmsConfig.getUser(), dmsConfig.getPassword().toCharArray());			
		}
		return cred ;
	}
	
	protected boolean isOnline() throws NamingException {
		connectRepository() ;
		return repo != null;
	}

	
	private Session getSession() throws LoginException, RepositoryException, NamingException {
		if (session == null) {
			myLogger.warn("login to repository necessary");

			connectRepository() ;			
			if(!isOnline()) {
				myLogger.warn("repository not available");
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE, "repo == null");
			}
			
			session = repo.login(getCredentials());
		}

//		myLogger.warn("returning session (" + session + ")");
		return session;
	}

	private void closeSession() {
		if (session != null) {
			myLogger.warn("logout from repository (" + session + ")");
			try {
				if (session.isLive()) {
					session.logout();
					session = null;
				}
			} catch(IllegalStateException e) {
				myLogger.warn("logout from repository, Illegal State", e);
			}
		}
		session = null;
	}

	private void forcedCloseSession() {
		if (session != null) {
			 myLogger.warn("forced logout from repository (" + session + ")") ;
			
			 session.logout() ;
			 session = null ;
		}
		session = null;
	}
	

	private Node getRootNode() throws RepositoryException, NamingException {
		Node rootNode = null;
		try {
			rootNode = getSession().getRootNode();
		} catch (IllegalStateException e) {
			myLogger.warn("IllegalStateException (getRootNode) - calling forcedCloseSession", e);
			forcedCloseSession();
			rootNode = getSession().getRootNode();
		} catch (RepositoryException e) {
			myLogger.warn("RepositoryException (getRootNode) - calling forcedCloseSession", e);
			forcedCloseSession();
			rootNode = getSession().getRootNode();
		}
		
		return rootNode ;
	}	
	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	@Override		
	public JCRRepoInfo existsNode(JcrDmsConfig config, DocPath dPath) throws NamingException {
		dmsConfig = config ;
		
		JCRRepoInfo info = new JCRRepoInfo() ;
		
		connectRepository() ;
		info.setOnline(isOnline());
		
		if(info.isOnline()) {
			try {
				boolean exists = existsNodeWithinTransaction(dmsConfig, dPath) ;
				info.setExists(exists);
			} finally {
				closeSession();
			}
		}

		return info ;
	}
	
	@Override		
	public boolean existsNodeWithinTransaction(JcrDmsConfig config, DocPath dPath) throws NamingException {
		return existsNodeWithinTransaction(config, dPath.getPathAsString());
	}
	
	@Override		
	public boolean existsNodeWithinTransaction(JcrDmsConfig config, String path) throws NamingException {
		dmsConfig = config ;		
		connectRepository() ;
		return existsNodeWithinTransactionImpl(path) ;
	}
	
	@Override
	public void close() {
		myLogger.warn("closing possible open session");
		try {
			forcedCloseSession();			
		} catch(Exception e) {
			myLogger.warn("Exception (ignored)", e);
		} catch(Throwable e) {
			myLogger.warn("forcedCloseSession throwed (ignored)", e);
		}
//		session = null;
//		repo = null;
	}
	
	protected boolean existsNodeWithinTransactionImpl(String path) {
		Validator.notNull(dmsConfig, "dmsConfig");
		Validator.notEmpty(path, "path");

		try {			
			Node rootNode = getRootNode() ;
			try {
				String propertyName = HvProperty.VERSION ;
				getProperty(getRootNode().getNode(path), propertyName);
				return true;
			} catch (PathNotFoundException ex) {
				try {
					DocNodeFactory.createDocNodeByNode(rootNode.getNode(path));
					return true;
				} catch (PathNotFoundException e) {
					// Den Beleg gibt es nicht => false
					return false;
				}
			}
		} catch (Exception e) {
			String s = "JCR-Node-Exists:Path: <" + path + ">." ;
			myLogger.error(s, e);
			ArrayList<Object> ao = new ArrayList<Object>() ;
			ao.add(s) ;

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_JCR_ROOT_EXISTIERT_NICHT, ao, e);
		}		
	}
	
	
	protected Property getProperty(Node n, String propertyName) throws PathNotFoundException, RepositoryException {
		return n.getProperty(propertyName) ;
	}
	
	
	@Override	
	public void iterateRepository(JcrDmsConfig config, DmsNodeVisitor nodeVisitorBean) throws NamingException, RepositoryException {
		iterateRepository("HELIUMV/", config, nodeVisitorBean);
	}

	
	@Override
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void iterateRepository(String basePath, JcrDmsConfig config, DmsNodeVisitor nodeVisitorBean) throws NamingException, RepositoryException {
		dmsConfig = config ;
//		connectRepository() ;
		
		nodeVisitorBean.initializeVisit();
		nodeVisitorBean.start();
		if(session != null) {
			try {
				boolean live = session.isLive();
				boolean changes = session.hasPendingChanges();
				myLogger.warn("Session is already open! Pending chances: (" + live + "," + changes + ").");
				if(! (live && changes)) {
					forcedCloseSession();
				} else {
					// Session offen lassen, eine neue beziehen
					session = null;
				}				
			} catch(IllegalStateException e) {
				myLogger.warn("Session " + session + " is open, but invalid.");
				session = null;
			}
		}
		
		iterateNode(basePath, nodeVisitorBean) ;
		nodeVisitorBean.stop();
		nodeVisitorBean.doneVisit();
		close();
	}
	
	protected void iterateNode(String path, DmsNodeVisitor nodeVisitor) throws RepositoryException, NamingException {
		nodeVisitor.push(path) ; 

		Node rootNode = getSession().getRootNode() ;
		Node childNode = rootNode.getNode(path) ;
		if(childNode != null) {
			
			nodeVisitor.visitPath(childNode);
			
			NodeIterator it = childNode.getNodes() ;
			if(it == null) {
				myLogger.error("Kann den Root-Node-Iterator nicht ermitteln!");
				
				nodeVisitor.pop() ;
				return;				
			}

			if(it.getSize() == 0) {
				myLogger.warn("Iterator has no size for  " + childNode.getName() + ", -> visit-data direct.");
				if(childNode.isNodeType("mix:referenceable")) {
					nodeVisitor.visitData(childNode);
				} else { 
					myLogger.warn("Detected Data-Node is not referenceable, ignored");
				}			
			}

			nodeVisitor.startIterate(it) ;
//			myLogger.warn("|" + level + "| Node-Count: " + it.getSize() + " for '" + path + "'.");
			while(it.hasNext()) {
//				myLogger.warn("Iterator Pos: " + it.getPosition() + ".");
				Node n = it.nextNode() ;
				
//				myLogger.warn("Node name='" + n.getName() + "', isNode=" + n.isNode() + ", hasNodes=" + n.hasNodes() + ".") ;

// TODO ghp 12.12.2018 Warum ist "System" auskommentiert? Das ist doch ein HELIUMV/System Knoten?
//				if("System".equals(n.getName())) continue ;

				boolean isFolder = false;
				NodeIterator itn = n.getNodes();
				if(itn != null) {
					isFolder = itn.hasNext();
				}
				myLogger.warn("Childnode-Detector: Node " + n.getName() + " isFolder=" + isFolder);
				
				if(isFolder) {
					nodeVisitor.visitPath(n);					
					iterateNode(path + n.getName() + "/", nodeVisitor);
				} else {
					if(n.isNodeType("mix:referenceable")) {
						nodeVisitor.visitData(n);
					} else { 
						myLogger.warn("Detected Data-Node is not referenceable, ignored");
					}
				}
				
//				iterateNode(path + n.getName() + "/", nodeVisitor);
			}
			
			nodeVisitor.stop();
		}

		nodeVisitor.pop(); 
	}
	
	@Override
	public void createPath(JcrDmsConfig config, String path) throws NamingException,
			RepositoryException {
		dmsConfig = config ;
		connectRepository() ;
	}
	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	@Override	
	public Node addNewDocumentOrNewVersionOfDocument(JcrDmsConfig config, JCRDocDto jcrDocDto) throws NamingException {
		return addNewDocumentOrNewVersionOfDocumentImpl(config, jcrDocDto);
	}

	@Override	
	public Node addNewDocumentOrNewVersionOfDocumentWithinTransaction(
			JcrDmsConfig config, JCRDocDto jcrDocDto) throws NamingException {
		return addNewDocumentOrNewVersionOfDocumentImpl(config, jcrDocDto);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override	
	public Node addNewDocumentOrNewVersionOfDocumentNew(JcrDmsConfig config, JCRDocDto jcrDocDto) throws NamingException {
		session = null;
		return addNewDocumentOrNewVersionOfDocumentImpl(config, jcrDocDto);
	}

	private Node addNewDocumentOrNewVersionOfDocumentImpl(JcrDmsConfig config, JCRDocDto jcrDocDto) throws NamingException {	
		dmsConfig = config ;
		connectRepository() ;
		
		if(!isOnline()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE, "repo == null");
		}
		
		try {
			DocPath docPath = jcrDocDto.getDocPath();
			if (jcrDocDto.getlVersion() == -1) {
				jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
			}

			Node rootNode = getRootNode() ;
			Node updateNode = null;
			try {
				updateNode = rootNode.getNode(docPath.getPathAsString());
				// Neue Version des Knotens
				if (updateNode.getMixinNodeTypes().length == 0)
					updateNode.addMixin("mix:versionable");

				updateNode.checkout();
				updateNode = this.setProperties(updateNode, jcrDocDto);
				updateNode.save();
				updateNode.checkin();
				
				return updateNode;
			} catch (PathNotFoundException ex) {
				// Der Knoten existiert nicht und muss angelegt werden
				// String[] sFolders = jcrDocDto.getsFullNodePath().split("/");
				List<DocNodeBase> folders = docPath.asDocNodeList();
				String sPath = "";
				for (int i = 0; i < folders.size(); i++) {
					sPath += (i == 0 ? "" : DocPath.SEPERATOR)
							+ folders.get(i).asEncodedPath();

					try {

						updateNode = rootNode.getNode(sPath);
						try {
							updateNode
									.getProperty(DocNodeBase.NODEPROPERTY_NODETYPE);
						} catch (PathNotFoundException pnfEx) {
							folders.get(i).persistTo(updateNode);
							updateNode.getParent().save();
						}
					} catch (Exception pnfEx) {
						myLogger.warn("Path didn't exist already, creating new '" + sPath + "'");
						// Anlegen
						updateNode = rootNode.addNode(sPath);
						updateNode.getSession().save();
						folders.get(i).persistTo(updateNode);
						updateNode.getSession().save();
						// try {
						// nUpdateNode.checkin();
						// } catch (Exception ex1) {
						// // Nichts tun, ist keine versionabel Node
						// }
						// rootNode.getNode(sPath).checkin();
					}
				}
				// Jetzt gibt es den Knoten
				updateNode = rootNode.getNode(docPath.getPathAsString());
				updateNode.addMixin("mix:versionable");
				updateNode = setProperties(updateNode, jcrDocDto);
				updateNode.getSession().save();
				// Auch gleich die erste Version anlegen
//				updateNode.checkout();
				updateNode = setProperties(updateNode, jcrDocDto);
				updateNode.getSession().save();
				updateNode.checkin();
				return updateNode;
			}
		} catch (VersionException e) {
			myLogger.warn("VersionException " + e.getMessage(), e);
			return null;
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_JCR_KNOTEN_NICHT_GESPEICHERT, e);
		} finally {
			// closeSession() ;
		}
	}
	
	
	private Node getNode(String sPath) {
		try {
			Node rootNode = getRootNode() ;
			Node returnNode = null;
			
			try {
				returnNode = rootNode.getNode(sPath);
			} catch (PathNotFoundException ex) {
				// Den Knoten gibt es nicht Null zurueckgeben
				myLogger.warn("Couldn't find path '" + sPath + "'.", ex) ;
			}

			return returnNode;
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}
	
	
	public DocPath getDocPathFromJCR(JcrDmsConfig config, JCRDocDto jcrDocDto)
			throws ValueFormatException, PathNotFoundException, RepositoryException, IOException, NamingException {
		if (jcrDocDto.getDocPath() != null)
			return jcrDocDto.getDocPath();
		
		dmsConfig = config ;
		connectRepository() ;
		
		ArrayList<DocNodeBase> docNodes = new ArrayList<DocNodeBase>();
		String parts[] = jcrDocDto.getsPath().split("/");
		String tempPath = "";

		for (int i = 0; i < parts.length; i++) {
			tempPath += (i == 0 ? "" : "/") + parts[i];
			DocNodeBase docNode;
			try {
				docNode = getDocNodeFromSPath(tempPath);
			} catch (PathNotFoundException ex) {
				docNode = new VisualNodeLiteral(parts[i], "color:gray");
				docNode.setVersion(1);
			}
			if (docNode != null)
				docNodes.add(docNode);
		}
		
		jcrDocDto.setDocPath(new DocPath().add(docNodes));
		return jcrDocDto.getDocPath();
	}

	@Override
	public void commitNodes(List<Node> commitNodes) throws NamingException, VersionException,
		UnsupportedRepositoryOperationException, InvalidItemStateException, LockException, RepositoryException {
		if(!isOnline()) return;
		
		for (Node node : commitNodes) {
			node.checkin();
		}	
		
		myLogger.info("Committed " + commitNodes.size() + " nodes.");
	}
	
	private DocNodeBase getDocNodeFromSPath(String sPath)
			throws ValueFormatException, PathNotFoundException,
			RepositoryException, IOException {
		return DocNodeFactory.createDocNodeByNode(getNode(sPath));
	}	
	
	private Node setProperties(Node node, JCRDocDto jcrDocDto)
			throws ValueFormatException, VersionException, LockException,
			ConstraintViolationException, RepositoryException {
		// Das Dokument selbst
		if (jcrDocDto.getbData() != null) {
			InputStream stream = new ByteArrayInputStream(jcrDocDto.getbData());
			node.setProperty(HvProperty.DATA, stream);
		}

		node.setProperty(HvProperty.BELEGART, jcrDocDto.getsBelegart());
		node.setProperty(HvProperty.ANLEGER, jcrDocDto.getlAnleger());
		node.setProperty(HvProperty.PARTNER, jcrDocDto.getlPartner());
		node.setProperty(HvProperty.ZEITPUNKT, jcrDocDto.getlZeitpunkt());
		node.setProperty(HvProperty.BELEGNUMMER, jcrDocDto.getsBelegnummer());
		node.setProperty(HvProperty.SCHLAGWORTE, jcrDocDto.getsSchlagworte());
		node.setProperty(HvProperty.NAME, jcrDocDto.getsName());
		node.setProperty(HvProperty.FILENAME, jcrDocDto.getsFilename());
		node.setProperty(HvProperty.TABLE, jcrDocDto.getsTable());
		node.setProperty(HvProperty.ROW, jcrDocDto.getsRow());
		node.setProperty(HvProperty.MIME, jcrDocDto.getsMIME());
		node.setProperty(HvProperty.VERSION, jcrDocDto.getlVersion());
		node.setProperty(HvProperty.VERSTECKT, jcrDocDto.getbVersteckt());
		node.setProperty(HvProperty.SICHERHEITSSTUFE,
				jcrDocDto.getlSicherheitsstufe());
		node.setProperty(HvProperty.GRUPPIERUNG, jcrDocDto.getsGruppierung());
		return node;
	}
}
