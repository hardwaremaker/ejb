package com.lp.server.system.jcr.ejbfac;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.JcrDmsConfig;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

public class FindRootRepoNode implements NodeVisitor {
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(
			this.getClass());

	private int level ;
	private int pathNodes ;
	private int dataNodes ;
	private int errorNodes ;
	
	private JcrDmsRepoFac sourceRepo ;
	private JcrDmsConfig sourceConfig ;
	private List<String> rootPaths ;
	private List<String> defectNodePaths ;
	
	private SimpleDateFormat timestampFormat;
	
	public FindRootRepoNode(JcrDmsConfig sourceConfig, JcrDmsRepoFac sourceRepo) {
		this.sourceConfig = sourceConfig ;
		this.sourceRepo = sourceRepo ;
			
		rootPaths = new ArrayList<String>();
		defectNodePaths = new ArrayList<String>();
		timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}
	
	public int getPathNodeCount() {
		return pathNodes ;
	}
	
	public int getDataNodeCount() {
		return dataNodes ;
	}
	
	public int getErrorNodeCount() {
		return errorNodes ;
	}

	public List<String> getDefectNodePaths() {
		return defectNodePaths ;
	}

	@Override
	public void initializeVisit() {
//		sourceRepo.close();
	}
	
	
	@Override
	public void doneVisit() { 
//		sourceRepo.close();
	}
	
	@Override
	public void push(String path) {
		++level ;
		myLogger.info(getLogPrefix() + "path=" + path + ".") ;

		if(level <= 3) {
			rootPaths.add(path) ;			
		}
	}

	@Override
	public void pop() {
		--level;
	}

	
	@Override
	public void start() {
		myLogger.info(getLogPrefix() + "start...") ;
	}
	
	@Override
	public void stop() {
		myLogger.info(getLogPrefix() + "stop") ;
	}
	
	@Override
	public void startIterate(NodeIterator nodeIterator) {
	}

	@Override
	public void stopIterator() {
	}

	private String getLogPrefix() {
		return  "|" + level + "| " ;
	}
	
	@Override
	public void visitPath(Node n) {
		++pathNodes ;
		try {
			myLogger.warn(getLogPrefix() + "visitpath='" + n.getName() + "'.");
			logNode(n);
		} catch (RepositoryException e) {
			myLogger.error("RepoEx", e);
		}
	}

	@Override
	public void visitData(Node n) {
		++dataNodes;
	}

	
	private String asTimestamp(long timestamp) {
		return timestampFormat.format(new Date(timestamp));
	}

	private void logNode(Node n) throws RepositoryException {
		myLogger.warn(getLogPrefix() + " Nodeinfo: [" 
				+ "type=" + (n.hasProperty(DocNodeBase.NODEPROPERTY_NODETYPE) ? n.getProperty(DocNodeBase.NODEPROPERTY_NODETYPE).getLong() : -1)
				+ ", sPath=" + n.getPath() 
				+ ", timestamp=" + (n.hasProperty(JCRDocFac.PROPERTY_ZEITPUNKT) ? asTimestamp(n.getProperty(JCRDocFac.PROPERTY_ZEITPUNKT).getLong()) : -1) 
				+ ", filename=" + (n.hasProperty(JCRDocFac.PROPERTY_FILENAME) ? n.getProperty(JCRDocFac.PROPERTY_FILENAME).getString() : "(nofilename)")
				+ ", mime=" + (n.hasProperty(JCRDocFac.PROPERTY_MIME) ? n.getProperty(JCRDocFac.PROPERTY_MIME).getString() : "(nomime)")
				+ ", data=" + n.hasProperty(JCRDocFac.PROPERTY_DATA)
				+ "].");
	}
	
	public List<String> getRootPaths() {
		return rootPaths;
	}
}
