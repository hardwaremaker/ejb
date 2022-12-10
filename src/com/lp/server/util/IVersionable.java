package com.lp.server.util;

import java.sql.Timestamp;

public interface IVersionable {

	boolean hasVersion();
	
	Integer getIVersion();
	void setIVersion(Integer version);
	
	Timestamp getTVersion();
	void setTVersion(Timestamp tVersion);
}
