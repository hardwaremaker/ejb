package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.util.EJBExceptionLP;
import com.lp.util.Pair;

public class AblieferungByAuftragResultDto implements Serializable {
	private static final long serialVersionUID = 7883777443592233302L;

	private List<Pair<Integer, String>> abgelieferteLose;
	private List<EJBExceptionLP> loserledigungReturnedExc;
	
	public AblieferungByAuftragResultDto() {
	}

	public List<Pair<Integer, String>> getAbgelieferteLose() {
		if (abgelieferteLose == null) 
			abgelieferteLose = new ArrayList<Pair<Integer,String>>();
		
		return abgelieferteLose;
	}
	
	public void setAbgelieferteLose(List<Pair<Integer, String>> losInfos) {
		this.abgelieferteLose = losInfos;
	}
	
	public List<EJBExceptionLP> getLoserledigungReturnedExc() {
		if (loserledigungReturnedExc == null)
			loserledigungReturnedExc = new ArrayList<EJBExceptionLP>();
		
		return loserledigungReturnedExc;
	}
	
	public void setLoserledigungReturnedExc(
			List<EJBExceptionLP> loserledigungReturnedExc) {
		this.loserledigungReturnedExc = loserledigungReturnedExc;
	}
}
