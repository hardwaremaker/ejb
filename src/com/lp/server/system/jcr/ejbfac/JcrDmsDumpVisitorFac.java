package com.lp.server.system.jcr.ejbfac;

import com.lp.server.system.jcr.service.JcrDmsConfig;

public interface JcrDmsDumpVisitorFac extends DmsNodeVisitor {
	int getPathNodeCount();
	int getDataNodeCount();
	int getErrorNodeCount();
	long getDataByteCount();

	void setupRepositories(
		JcrDmsRepoFac sourceRepo, JcrDmsConfig sourceConfig,
		JcrDmsRepoFac targetRepo, JcrDmsConfig targetConfig);
}
