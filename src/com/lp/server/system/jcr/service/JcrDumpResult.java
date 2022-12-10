package com.lp.server.system.jcr.service;

import java.io.Serializable;

public class JcrDumpResult implements Serializable {
	private static final long serialVersionUID = 3398654088287515610L;

	private int pathNodes ;
	private int dataNodes ;
	private int errorNodes ;
//	private List<String> defectNodes ;
	private String path;
	private boolean error;
	private long dataByteCount;
	
	public JcrDumpResult() {
	}
	

	public JcrDumpResult(String path) {
		this.path = "" + path;
	}
	
	public JcrDumpResult(int pathNodeCount, int dataNodesCount, int errorNodesCount, String path) {
		this.pathNodes = pathNodeCount ;
		this.dataNodes = dataNodesCount ;
		this.errorNodes = errorNodesCount;
		this.path = path;
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
	
	public long getDataByteCount() {
		return dataByteCount;
	}	
	public void setDataByteCount(long databyteCount) {
		this.dataByteCount = databyteCount;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isError() {
		return error;
	}
	
	public void setError(boolean value) {
		this.error = value;
	}
	
//	public List<String> getDefectNodes() {
//		return defectNodes;
//	}
//
//	public void setDefectNodes(List<String> defectNodes) {
//		this.defectNodes = defectNodes;
//	}
}
