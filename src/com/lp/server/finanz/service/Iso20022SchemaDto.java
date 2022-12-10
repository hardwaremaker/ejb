package com.lp.server.finanz.service;

import java.io.Serializable;

public class Iso20022SchemaDto implements Serializable {
	private static final long serialVersionUID = -6204796288556542674L;

	private Integer iId;
	private Iso20022SchemaEnum schema;
	private String cBez;
	
	public Iso20022SchemaDto() {
	}
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	public Iso20022SchemaEnum getSchema() {
		return schema;
	}
	public void setSchema(Iso20022SchemaEnum schema) {
		this.schema = schema;
	}
	
	public String getCBez() {
		return cBez;
	}
	public void setCBez(String cBez) {
		this.cBez = cBez;
	}
}
