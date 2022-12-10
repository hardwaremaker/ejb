package com.lp.service.edifact.schema;

public class PiaInfo {
	private String identifierCode ;
	private PiaInfoItem[] itemInfo;
	
	public PiaInfo() {
		itemInfo = new PiaInfoItem[5];
	}
	
	public String getIdentifierCode() {
		return identifierCode;
	}
	public void setIdentifierCode(String identifierCode) {
		this.identifierCode = identifierCode;
	}
	public PiaInfoItem[] getItemInfos() {
		return itemInfo;
	}
	public void setItemInfos(PiaInfoItem[] itemInfo) {
		this.itemInfo = itemInfo;
	}
	
	public PiaInfoItem getItemInfo(int index) {
		if(index < 0 || index >= itemInfo.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + itemInfo.length);
		}
		if(itemInfo[index] == null) {
			itemInfo[index] = new PiaInfoItem();
		}
		return itemInfo[index];
	}

	public void setItemInfo(int index, PiaInfoItem itemInfo) {
		if(index < 0 || index >= this.itemInfo.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + this.itemInfo.length);
		}
		this.itemInfo[index] = itemInfo;
	}
}
