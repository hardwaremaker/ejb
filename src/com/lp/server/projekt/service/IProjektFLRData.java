package com.lp.server.projekt.service;


public interface IProjektFLRData {

	String getAddress() ;
	void setAddress(String address) ;

	Integer getPartnerId() ;
	void setPartnerId(Integer partnerId) ;
	
	String getCategory() ;
	void setCategory(String category) ;

	Integer getPriority() ;
	void setPriority(Integer priority) ;
	
	String getTitle() ;
	void setTitle(String title) ;
	
	String getInternalComment() ;
	void setInternalComment(String comment) ;
	
	String getExternalComment() ;
	void setExternalComment(String comment) ;
	
	void setDeadlineMs(long deadline) ;
	long getDeadlineMs() ;
	
	Boolean isInternalDone() ;
	void setInternalDone(Boolean done);
	
	String getStatusCnr() ;
	void setStatusCnr(String statusCnr) ;
}
