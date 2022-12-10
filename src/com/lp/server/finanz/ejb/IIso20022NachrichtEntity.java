package com.lp.server.finanz.ejb;

public interface IIso20022NachrichtEntity {

	Integer getIId();
	void setIId(Integer iId);
	
	Integer getStandardIId();
	void setStandardIId(Integer iId);
	
	Integer getSchemaIId();
	void setSchemaIId(Integer schemaIId);
	
	Iso20022Standard getStandardObject();
	Iso20022Schema getSchemaObject();
}
