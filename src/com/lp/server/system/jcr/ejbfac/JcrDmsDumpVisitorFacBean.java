package com.lp.server.system.jcr.ejbfac;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.naming.NamingException;

import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.JcrDmsConfig;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.util.Facade;

@Singleton
public class JcrDmsDumpVisitorFacBean extends Facade implements JcrDmsDumpVisitorFac {
	private JcrDmsRepoFac sourceRepo ;
	private JcrDmsConfig sourceConfig ;
	private JcrDmsRepoFac targetRepo ;
	private JcrDmsConfig targetConfig ;

	private int level ;
	private int pathNodes ;
	private int dataNodes ;
	private int errorNodes ;
	private long dataBytes;
	
	private byte[] brokenNodeData ;

	private SimpleDateFormat timestampFormat;
	private List<String> actualPaths ;
	private List<String> defectNodePaths ;


	@Override
	public void setupRepositories(
			JcrDmsRepoFac sourceRepo, JcrDmsConfig sourceConfig,
			JcrDmsRepoFac targetRepo, JcrDmsConfig targetConfig) {
		this.sourceRepo = sourceRepo;
		this.sourceConfig = sourceConfig;
		this.targetRepo = targetRepo;
		this.targetConfig = targetConfig;
	}
	
	@Override
	public void initializeVisit() {
		dataBytes = pathNodes = dataNodes = errorNodes = level = 0;
		actualPaths = new ArrayList<String>();
		defectNodePaths = new ArrayList<String>();
		timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}

	@Override
	public void doneVisit() {
//		sourceRepo.close();
		targetRepo.close();
	}

	@Override
	public void start() {
		myLogger.warn(getLogPrefix() + "DumpVisitor start...") ;
	}

	@Override
	public void stop() {
		myLogger.warn(getLogPrefix() + "DumpVisitor stop");
	}

	@Override
	public void push(String path) {
		++level ;
		myLogger.warn(getLogPrefix() + "path=" + path + ".") ;
		
		actualPaths.add(path) ;
	}

	@Override
	public void pop() {
		--level;
		actualPaths.remove(actualPaths.size() - 1);
	}

	@Override
	public void startIterate(NodeIterator nodeIterator) {
	}

	@Override
	public void stopIterator() {
	}

	@Override
	public void visitPath(Node n) {
		++pathNodes ;
		try {
			myLogger.warn(getLogPrefix() + "visitpath='" + n.getName() + "'.");
			logNode("S-", n);
		} catch (RepositoryException e) {
			myLogger.error("RepoEx", e);
//		} catch (NamingException e) {
//			myLogger.error("NamingEx", e);
		}
	}

	@Override
	public void visitData(Node n) {
		++dataNodes ;

		try {
			myLogger.warn(getLogPrefix() + "visitdata='" + n.getName() + "', uuid='" + n.getUUID() + "'.") ;						
			dumpNode(n);			
		} catch(IOException e) {
			++errorNodes;
			myLogger.warn(getLogPrefix() + "visitdata, got IOException (node damaged)?", e) ;				
		} catch (NamingException e) {
			myLogger.warn(getLogPrefix() + "visitdata, got NamingException (node damaged)?", e) ;				
		} catch (RepositoryException e) {
			++errorNodes;
			myLogger.error(getLogPrefix() + "RepoEx", e);
		} catch (Throwable t) {
			++errorNodes;
			myLogger.error(getLogPrefix() + "Catchall", t);
		}
	}

	private void dumpNode(Node n) throws ValueFormatException, PathNotFoundException, RepositoryException, IOException, NamingException {
		if(n.isNodeType("mix:versionable")) {	
			JCRDocDto jcrNodeDto = new JCRDocDto(n, false) ;
			DocPath sourceDocPath = sourceRepo.getDocPathFromJCR(sourceConfig,  jcrNodeDto) ;

			logNode("S-", n);
			
			VersionHistory hist = n.getVersionHistory();
			VersionIterator vIt = hist.getAllVersions();
			
			long lastVersion = -1l ;
			while (vIt.hasNext()) {
				Version v = vIt.nextVersion();
				NodeIterator niVersion = v.getNodes("jcr:frozenNode");
				if (niVersion.hasNext()) {
					Node no = niVersion.nextNode();
					
					JCRDocDto jcrDto = null ;
					try {
						boolean hasData = no.hasProperty(JCRDocFac.PROPERTY_DATA);
						if(!hasData) {
							myLogger.warn("Version info node ignored" + 
									", name=" + no.getName() +
									", nodepath=" + no.getPath() +
									", hasData=" + hasData + ".");
							continue;
						}
						jcrDto = new JCRDocDto(no, hasData);
						int contentLength = jcrDto.getbData().length;
						dataBytes += contentLength;								
						myLogger.warn(getLogPrefix() + "Version info node, version=" 
								+ jcrDto.getlVersion() 
								+ ", timestamp=" + asTimestamp(jcrDto.getlZeitpunkt()) 
								+ ", nodepath=" + no.getPath() 
								+ ", hasData=" + hasData + " (" + contentLength + ").") ;						
					} catch(PathNotFoundException e) {
						myLogger.warn(getLogPrefix() + "Version info node, pathnotfoundex-msg: " + e.getMessage()) ;						
						continue ;
					} catch(IOException e) {
						++errorNodes ;
						
						String s = sourceDocPath.getPathAsString() ;
						defectNodePaths.add(s) ;						
						myLogger.warn(getLogPrefix() + 
								"version info node due to IO-Ex -> dummy document for '" + s + "'.", e) ;	
						
						jcrDto = new JCRDocDto(n, false) ;
						prepareDefectNode(jcrDto) ;
					}
					
					jcrDto.setDocPath(sourceDocPath) ;
					
					// Hat noch keine "Version" und wuerde daher den lZeitpunkt auf "jetzt" setzen,
					// wir haben aber schon einen Zeitpunkt -> deshalb auf 0te Version setzen.
					if(jcrDto.getlVersion() == -1) {
						jcrDto.setlVersion(++lastVersion);
					}
					targetRepo
							.addNewDocumentOrNewVersionOfDocumentWithinTransaction(targetConfig, jcrDto);
					myLogger.warn(getLogPrefix() + "Target Node saved.");
//					myLogger.warn(getLogPrefix() + "Target Version info"
//							+ ", nodepath=" + targetN.getPath() + ".") ;						
				}
			}
		} else {
			logNode("S-", n);
			
			myLogger.warn("not versionable, adding?");
			JCRDocDto jcrDto = new JCRDocDto(n, true) ;
			jcrDto.setDocPath(sourceRepo.getDocPathFromJCR(sourceConfig, jcrDto)) ;
			targetRepo
					.addNewDocumentOrNewVersionOfDocumentNew(targetConfig, jcrDto);
		}		
	}

	@Override
	public int getPathNodeCount() {
		return pathNodes;
	}

	@Override
	public int getDataNodeCount() {
		return dataNodes;
	}

	@Override
	public int getErrorNodeCount() {
		return errorNodes;
	}
	
	@Override
	public long getDataByteCount() {
		return dataBytes;
	}
	
	private String getLogPrefix() {
		return  "|" + level + "| " ;
	}

	private byte[] getBrokenNodeData() {
		if(brokenNodeData == null) {
			try {
				InputStream in = DumpRepoNode.class.getResourceAsStream("/com/lp/server/res/Datenfehler.png") ;
				int size = in.available();
				brokenNodeData = new byte[size];
				in.read(brokenNodeData);
				in.close();
			} catch(IOException e) {
				myLogger.error("Kann Datei 'Datenfehler.png' nicht laden/oeffnen", e);
			}
		}
		
		return brokenNodeData ;
	}

	private void prepareDefectNode(JCRDocDto jcrDto) {
		jcrDto.setbData(getBrokenNodeData());
		jcrDto.setsMIME(".PNG");
		jcrDto.setsFilename("" + jcrDto.getsFilename() + ".png"); // den alten Dateinamen erhalten
		jcrDto.setsSchlagworte("" + jcrDto.getsSchlagworte() + " Datenfehler");		
	}
	
	private String asTimestamp(long timestamp) {
		return timestampFormat.format(new Date(timestamp));
	}

	private void logNode(String prefix, Node n) throws RepositoryException {
		myLogger.warn(getLogPrefix() + " " + prefix + "Nodeinfo: [" 
				+ "type=" + (n.hasProperty(DocNodeBase.NODEPROPERTY_NODETYPE) ? n.getProperty(DocNodeBase.NODEPROPERTY_NODETYPE).getLong() : -1)
				+ ", sPath=" + n.getPath() 
				+ ", timestamp=" + (n.hasProperty(JCRDocFac.PROPERTY_ZEITPUNKT) ? asTimestamp(n.getProperty(JCRDocFac.PROPERTY_ZEITPUNKT).getLong()) : -1) 
				+ ", filename=" + (n.hasProperty(JCRDocFac.PROPERTY_FILENAME) ? n.getProperty(JCRDocFac.PROPERTY_FILENAME).getString() : "(nofilename)")
				+ ", mime=" + (n.hasProperty(JCRDocFac.PROPERTY_MIME) ? n.getProperty(JCRDocFac.PROPERTY_MIME).getString() : "(nomime)")
				+ ", data=" + n.hasProperty(JCRDocFac.PROPERTY_DATA)
				+ "].");
	}
	
}
