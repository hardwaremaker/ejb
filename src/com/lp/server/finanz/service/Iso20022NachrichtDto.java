package com.lp.server.finanz.service;

import java.io.Serializable;

public abstract class Iso20022NachrichtDto implements Serializable {
	private static final long serialVersionUID = 6166974115032497022L;

	public enum Type {
		ZAHLUNGSAUFTRAG,
		LASTSCHRIFT;
	}
	
	private Type type;
	private Integer iId;
	private Integer standardIId;
	private Integer schemaIId;
	private Iso20022SchemaDto schemaDto;
	
	public Iso20022NachrichtDto(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	public Integer getIId() {
		return iId;
	}
	
	public Integer getStandardIId() {
		return standardIId;
	}
	public void setStandardIId(Integer standardIId) {
		this.standardIId = standardIId;
	}
	
	public Integer getSchemaIId() {
		return schemaIId;
	}
	public void setSchemaIId(Integer schemaIId) {
		this.schemaIId = schemaIId;
	}
	
	public Iso20022SchemaDto getSchemaDto() {
		return schemaDto;
	}
	public void setSchemaDto(Iso20022SchemaDto schemaDto) {
		this.schemaDto = schemaDto;
	}
}
