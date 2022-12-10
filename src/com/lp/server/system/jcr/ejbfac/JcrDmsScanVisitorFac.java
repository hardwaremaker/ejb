package com.lp.server.system.jcr.ejbfac;

import java.util.List;

import javax.ejb.Local;

@Local
public interface JcrDmsScanVisitorFac extends DmsNodeVisitor {
	int getPathNodeCount();
	int getDataNodeCount();
	List<String> getRootPaths();
}
