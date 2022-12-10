package com.lp.service.edifact.errors;

import com.lp.service.edifact.schema.UnhInfo;

public class NoProgramFoundException extends EdifactException {
	private static final long serialVersionUID = 5827221021060165633L;
	private UnhInfo unhInfo;
	
	public NoProgramFoundException(UnhInfo unhInfo) {
		super("No Program found for UNH " + unhInfo.getType() + 
				" Version " + unhInfo.getVersion() + 
				" release " + unhInfo.getRelease()+ ".");
		this.setUnhInfo(unhInfo);
	}

	public UnhInfo getUnhInfo() {
		return unhInfo;
	}

	public void setUnhInfo(UnhInfo unhInfo) {
		this.unhInfo = unhInfo;
	}
}
