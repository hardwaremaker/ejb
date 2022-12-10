package com.lp.service.edifact.schema;

public class LocInfo {
	private String functionCode;
	private LocInfoIdentification[] identifications;
	private String relationCode;
	
	public LocInfo() {
		identifications = new LocInfoIdentification[3];
	}
	
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	
	public LocInfoIdentification[] getIdentifications() {
		return identifications;
	}
	public void setIdentifications(LocInfoIdentification[] identifications) {
		this.identifications = identifications;
	}
	
	public LocInfoIdentification getIdentification(int index) {
		if(index < 0 || index >= identifications.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + identifications.length);
		}

		if(identifications[index] == null) {
			identifications[index] = new LocInfoIdentification();
		}
		
		return identifications[index];
	}
	
	public void setIdentification(int index, LocInfoIdentification identification) {
		if(index < 0 || index >= identifications.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + identifications.length);
		}

		identifications[index] = identification;
	}
	public String getRelationCode() {
		return relationCode;
	}
	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}
}
