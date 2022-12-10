package com.lp.service.edifact.schema;

import java.util.ArrayList;
import java.util.List;

public class EdifactDelforMessage extends EdifactMessage {
	private List<FtxInfo> headerTexts;
	
	public EdifactDelforMessage() {
		super("DELFOR");
		
		headerTexts = new ArrayList<FtxInfo>();
	}

	public List<FtxInfo> getHeaderTexts() {
		return headerTexts;
	}

	public void setHeaderTexts(List<FtxInfo> headerTexts) {
		this.headerTexts = headerTexts;
	}
}
