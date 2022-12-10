package com.lp.server.system.jcr.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.jcr.RepositoryException;
import javax.naming.NamingException;

import com.lp.server.artikel.ejbfac.ArtikelFacBean;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.system.jcr.service.JcrDmsConfig;
import com.lp.server.system.jcr.service.JcrDmsWildflyConfig;
import com.lp.server.system.jcr.service.JcrDumpFac;
import com.lp.server.system.jcr.service.JcrDumpResult;
import com.lp.server.system.jcr.service.JcrScanResult;
import com.lp.server.util.FacLookup;
import com.lp.server.util.Facade;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.ISelect;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

@Stateless
public class JcrDumpFacBean extends Facade implements JcrDumpFac {
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(JcrDumpFacBean.class);

	private static final String jndiWildflyLocal = "java:/jcr/local";
	private static final String jndiWildflyDump = "java:/jcr/dump";
	

//	@EJB(name="JcrDmsRepoSourceBean")
	private JcrDmsRepoFac sourceJcr ;
	
//	@EJB(name="JcrDmsRepoTargetBean")
	private JcrDmsRepoFac targetJcr ;

	@org.jboss.ejb3.annotation.TransactionTimeout(45000)
	public JcrDumpResult dump(JcrDmsConfig sourceConfig, JcrDmsConfig targetConfig) throws NamingException, RepositoryException {
		return new JcrDumpResult();

//		try {
//			myLogger.info("Starting dump...");
//			sourceJcr = (JcrDmsRepoFac) getInitialContext().lookup("lpserver/JcrDmsRepoSourceBean/local") ;
//			targetJcr = (JcrDmsRepoFac) getInitialContext().lookup("lpserver/JcrDmsRepoTargetBean/local") ;
//			myLogger.info("loaded sourceJcr " + (sourceJcr != null) + ", targetJcr " + (targetJcr != null) + ".");
//
//			FindRootRepoNode rootNodeVisitor = new RootNodeVisitor(sourceConfig, sourceJcr);
//			myLogger.info("starting iterate...");
//			sourceJcr.iterateRepository(sourceConfig, rootNodeVisitor);
//			List<String> rootPaths = rootNodeVisitor.getRootPaths();
//			myLogger.warn("Repository rootNodes ended with " + rootNodeVisitor.getPathNodeCount());
//		
//			Collection<String> roots = reduce(rootPaths);
//			
//			JcrDumpResult result = new JcrDumpResult() ;
//			myLogger.info("starting iterate...");
//			for (String rootPath : roots) {
//				sourceJcr = (JcrDmsRepoFac) getInitialContext().lookup("lpserver/JcrDmsRepoSourceBean/local") ;
//				targetJcr = (JcrDmsRepoFac) getInitialContext().lookup("lpserver/JcrDmsRepoTargetBean/local") ;
//				DumpRepoNode dumpVisitor = new DumpVisitor(sourceConfig, sourceJcr, targetConfig, targetJcr) ;
//				sourceJcr.iterateRepository(rootPath, sourceConfig, dumpVisitor);
//				result.setDataNodeCount(result.getDataNodeCount() + dumpVisitor.getDataNodeCount());
//				result.setErrorNodeCount(result.getErrorNodeCount() + dumpVisitor.getErrorNodeCount());
//				result.setPathNodeCount(result.getPathNodeCount() + dumpVisitor.getPathNodeCount());
//			}
//			
//			return result;
//		} catch(Throwable t) {
//			myLogger.error("dump Exception", t);
//			return new JcrDumpResult();
//		}
	}

	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(45000)
	public JcrScanResult scan(JcrDmsConfig sourceConfig) throws NamingException, RepositoryException {
		try {
			myLogger.info("Starting scan...");
//			sourceJcr = (JcrDmsRepoFac) getInitialContext().lookup("lpserver/JcrDmsRepoSourceBean/local") ;
			sourceJcr = FacLookup.lookupLocal(getInitialContext(),
					JcrDmsRepoSourceBean.class, JcrDmsRepoSourceBean.class);
			myLogger.info("loaded sourceJcr " + (sourceJcr != null) + ".");

//			JcrDmsScanVisitorFac scanVisitor = 
//					(JcrDmsScanVisitorFac) getInitialContext()
//						.lookup("lpserver/JcrDmsScanVisitorFacBean/local");

			JcrDmsScanVisitorFac scanVisitor = 
					FacLookup.lookupLocal(getInitialContext(),
							JcrDmsScanVisitorFacBean.class, JcrDmsScanVisitorFac.class);
			myLogger.info("starting iterate...");
			sourceJcr.iterateRepository(sourceConfig, scanVisitor);
			List<String> rootPaths = scanVisitor.getRootPaths();
			myLogger.warn("Repository rootNodes ended with " + scanVisitor.getPathNodeCount());
		
//			List<String> filtered = filterDocuments(rootPaths);
			Collection<String> roots = reduce(rootPaths);
			return new JcrScanResult(roots, 
					scanVisitor.getPathNodeCount(), scanVisitor.getDataNodeCount(), 0);
		} catch(Throwable t) {
			myLogger.error("scan Exception", t);
			return new JcrScanResult();
		}		
	}

//	@Override
//	@TransactionTimeout(45000)	
//	public JcrScanResult scan(JcrDmsConfig sourceConfig) throws NamingException, RepositoryException {
//		try {
//			myLogger.info("Starting scan...");
//			sourceJcr = (JcrDmsRepoFac) getInitialContext().lookup("lpserver/JcrDmsRepoSourceBean/local") ;
//			myLogger.info("loaded sourceJcr " + (sourceJcr != null) + ".");
//
//			FindRootRepoNode rootNodeVisitor = new RootNodeVisitor(sourceConfig, sourceJcr);
//			myLogger.info("starting iterate...");
//			sourceJcr.iterateRepository(sourceConfig, rootNodeVisitor);
//			List<String> rootPaths = rootNodeVisitor.getRootPaths();
//			myLogger.warn("Repository rootNodes ended with " + rootNodeVisitor.getPathNodeCount());
//		
//			Collection<String> roots = reduce(rootPaths);
//			return new JcrScanResult(roots, rootNodeVisitor.getPathNodeCount(), 
//					rootNodeVisitor.getDataNodeCount(), rootNodeVisitor.getErrorNodeCount());
//		} catch(Throwable t) {
//			myLogger.error("scan Exception", t);
//			return new JcrScanResult();
//		}		
//	}

	@org.jboss.ejb3.annotation.TransactionTimeout(45000)
	@Override
	public JcrDumpResult dumpPath(String rootPath, JcrDmsConfig sourceConfig, JcrDmsConfig targetConfig) throws NamingException, RepositoryException {
		JcrDumpResult result = new JcrDumpResult(rootPath);
		JcrDmsDumpVisitorFac dumpVisitor = null;
		try {
			myLogger.info("Starting dumppath...");
//			sourceJcr = (JcrDmsRepoFac) getInitialContext().lookup("lpserver/JcrDmsRepoSourceBean/local") ;
//			targetJcr = (JcrDmsRepoFac) getInitialContext().lookup("lpserver/JcrDmsRepoTargetBean/local") ;
			sourceJcr = FacLookup.lookupLocal(getInitialContext(), JcrDmsRepoSourceBean.class, JcrDmsRepoSourceBean.class);
			targetJcr = FacLookup.lookupLocal(getInitialContext(), JcrDmsRepoTargetBean.class, JcrDmsRepoTargetBean.class);
			
			myLogger.info("loaded sourceJcr " + (sourceJcr != null) + ", targetJcr " + (targetJcr != null) + ".");

//			dumpVisitor = (JcrDmsDumpVisitorFac) getInitialContext().lookup("lpserver/JcrDmsDumpVisitorFacBean/local");
			dumpVisitor = FacLookup.lookupLocal(
					getInitialContext(), JcrDmsDumpVisitorFacBean.class, JcrDmsDumpVisitorFac.class);
			dumpVisitor.setupRepositories(sourceJcr, sourceConfig, targetJcr, targetConfig);
			sourceJcr.iterateRepository(rootPath, sourceConfig, dumpVisitor);
		} catch(Throwable t) {
			myLogger.error("dump Exception", t);
			result.setError(true);
		}

		if(dumpVisitor != null) {
			result.setDataNodeCount(dumpVisitor.getDataNodeCount());
			result.setErrorNodeCount(dumpVisitor.getErrorNodeCount());
			result.setPathNodeCount(dumpVisitor.getPathNodeCount());
			result.setDataByteCount(dumpVisitor.getDataByteCount());
		}
		
		return result;
	}
	
	private List<String> filterDocuments(List<String> possibleRoots) {
		List<String> docs = new ArrayList<String>();
		for (String string : possibleRoots) {
			String[] s = string.split("/");
			int lastIndex = s.length;
			if(s[lastIndex-1].contains(".")) {
				--lastIndex;
			}
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < lastIndex; i++) {
				sb.append(s[i]);
				if(i != lastIndex) {
					sb.append("/");
				}
			}
			docs.add(sb.toString());
		}
		return docs;
	}
	
	private Collection<String> reduce(List<String> possibleRoots) {
		final Set<String> cached = new HashSet<String>();
		Collection<String> result = CollectionTools.select(possibleRoots, new ISelect<String>() {
			String lastElement = null;
			@Override
			public boolean select(String element) {
//				if(element.startsWith("HELIUMV/001")) {
//					System.out.println("halt");
//				}
				int minPathElements = 3;
				String[] paths = element.split("/");
				if(paths.length > 3) {
//					if(paths[1].length() == 3 && paths[1].startsWith("0")) {
//						minPathElements = 4;
//					}
					if(paths[1].length() == 3) {
						// Wenn 2tes Element der Mandant (immer 3stellig)
						minPathElements = 4;
					}
					if(element.startsWith("HELIUMV/Artikel/Artikel")) {
						minPathElements = 3; // eigentlich ueberbestimmt
					}
				}
//				if(paths.length > minPathElements || element.startsWith("HELIUMV/Artikel/Artikel")) {
				if(paths.length > minPathElements) {
					String s = joined(paths);
					if(lastElement != null) {
						if(s.startsWith(lastElement)) {
							return false;
						}
					}
					if(cached.add(s)) {
						lastElement = s;
						return true;
					}
					return false;
				}
				
				return false;
			}
			
			private String joined(String[] paths) {
				int maxPaths = Math.min(5, paths.length);
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < maxPaths; i++) {
					sb.append(paths[i]);
					sb.append("/");
				}
				return sb.toString();
			}
		});
		return result;
/*		
		List<String> tempRoots = new ArrayList<String>();
		tempRoots.addAll(possibleRoots);
		Collections.reverse(tempRoots);
		Collection<String> result = CollectionTools.select(tempRoots, new ISelect<String>() {
			String lastElement = null;
			public boolean select(String element) {
				if(lastElement == null) {
					lastElement = element;
					return true;
				}
				if(lastElement.startsWith(element)) {
					lastElement = element;
					return false;
				}
				lastElement = element;
				return true;
			}
		});
		List<String> tempResult = new ArrayList<String>();
		tempResult.addAll(result);
		Collections.reverse(tempResult);
		return tempResult;
*/
	}

	
	@org.jboss.ejb3.annotation.TransactionTimeout(45000)
//	@TransactionAttribute(TransactionAttributeType.NEVER)
	@Override
	public JcrDumpResult dump() throws NamingException, RepositoryException {
		JcrDmsConfig sourceDms =  new JcrDmsWildflyConfig(jndiWildflyLocal);
		JcrDmsConfig targetDms =  new JcrDmsWildflyConfig(jndiWildflyDump);
		return dump(sourceDms, targetDms);			
	}
	
	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(45000)
	public JcrScanResult scan() throws NamingException, RepositoryException {
		
		JcrDmsConfig sourceDms =  new JcrDmsWildflyConfig(jndiWildflyLocal);
		return scan(sourceDms);
	}
	
	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(45000)
//	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JcrDumpResult dumpPath(String rootPath) throws NamingException, RepositoryException {
		JcrDmsConfig sourceDms =  new JcrDmsWildflyConfig(jndiWildflyLocal) ;
		JcrDmsConfig targetDms =  new JcrDmsWildflyConfig(jndiWildflyDump);
		return dumpPath(rootPath, sourceDms, targetDms);
	}
	
	class RootNodeVisitor extends FindRootRepoNode {
		public RootNodeVisitor(JcrDmsConfig sourceConfig, JcrDmsRepoFac sourceRepo) {
			super(sourceConfig, sourceRepo);
		}
	}
	
	class DumpVisitor extends DumpRepoNode {
		public DumpVisitor(JcrDmsConfig sourceConfig, JcrDmsRepoFac sourceRepo, JcrDmsConfig targetConfig, JcrDmsRepoFac targetRepo) {
			super(sourceConfig, sourceRepo, targetConfig, targetRepo);
		}
	}
}
