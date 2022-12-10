package com.lp.service.edifact.schema;

import java.util.ArrayList;

public class EdifactHeaderMessage extends EdifactMessage {
	public EdifactHeaderMessage() {
		super("");
		
		setUnbInfos(new ArrayList<UnbInfo>());
		setUnhInfos(new ArrayList<UnhInfo>());
		setBgmInfos(new ArrayList<BgmInfo>());
		setDtmInfos(new ArrayList<DtmInfo>());
		setUntInfos(new ArrayList<UntInfo>());
		setUnzInfos(new ArrayList<UnzInfo>());
	}
}
