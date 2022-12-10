package com.lp.server.system.jcr.service;

import java.io.Serializable;
import java.util.Collection;

public class JcrScanResult implements Serializable {
	private static final long serialVersionUID = 2117672954280722599L;
	private int pathNodes ;
	private int dataNodes ;
	private int errorNodes ;
	private Collection<String> rootPaths ;
	
	public JcrScanResult() {
	}
	
	public JcrScanResult(Collection<String> paths, int pathNodeCount, int dataNodesCount, int errorNodesCount) {
		this.rootPaths = paths;
		this.pathNodes = pathNodeCount ;
		this.dataNodes = dataNodesCount ;
		this.errorNodes = errorNodesCount ;
	}
	
	public Collection<String> getRootPaths() {
		return rootPaths;
	}
	
	public int getPathNodeCount() {
		return pathNodes;
	}
	public void setPathNodeCount(int pathNodes) {
		this.pathNodes = pathNodes;
	}
	public int getDataNodeCount() {
		return dataNodes;
	}
	public void setDataNodeCount(int dataNodes) {
		this.dataNodes = dataNodes;
	}
	public int getErrorNodeCount() {
		return errorNodes;
	}
	public void setErrorNodeCount(int errorNodes) {
		this.errorNodes = errorNodes;
	}
}
