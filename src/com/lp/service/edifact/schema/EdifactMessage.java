package com.lp.service.edifact.schema;

import java.util.List;

public class EdifactMessage {
	private String message;
	
	private List<UnbInfo> unbInfos;
	private List<UnhInfo> unhInfos;
	private List<BgmInfo> bgmInfos;
	private List<DtmInfo> dtmInfos;
	private List<UntInfo> untInfos;
	private List<UnzInfo> unzInfos;
	
	protected EdifactMessage(String message) {
		setMessage(message);
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	protected void setUnhInfos(List<UnhInfo> unhInfos) {
		this.unhInfos = unhInfos;
	}
	
	public UnhInfo getUnh() {
		return unhInfos.size() > 0 ? unhInfos.get(0) : null;
	}

	public List<UnhInfo> getUnhs() {
		return unhInfos;
	}

	protected void setBgmInfos(List<BgmInfo> bgmInfos) {
		this.bgmInfos = bgmInfos;
	}
	
	public BgmInfo getBgm() {
		return bgmInfos.size() > 0 ? bgmInfos.get(0) : null;
	}

	public List<BgmInfo> getBgms() {
		return bgmInfos;
	}
	
	protected void setDtmInfos(List<DtmInfo> dtmInfos) {
		this.dtmInfos = dtmInfos;
	}
	
	public List<DtmInfo> getDtms() {
		return dtmInfos;
	}
	
	protected void setUntInfos(List<UntInfo> untInfos) {
		this.untInfos = untInfos;
	}

	public List<UntInfo> getUnts() {
		return untInfos;
	}
	
	public UntInfo getUnt() {
		return untInfos.size() > 0 ? untInfos.get(0) : null;
	}	
	
	protected void setUnzInfos(List<UnzInfo> unzInfos) {
		this.unzInfos = unzInfos;
	}
	
	public List<UnzInfo> getUnzs() {
		return unzInfos;
	}
	
	public UnzInfo getUnz() {
		return unzInfos.size() > 0 ? unzInfos.get(0) : null;
	}
	
	protected void setUnbInfos(List<UnbInfo> unbInfos) {
		this.unbInfos = unbInfos;
	}
	
	public List<UnbInfo> getUnbs() {
		return unbInfos;
	}
	
	public UnbInfo getUnb() {
		return unbInfos.size() > 0 ? unbInfos.get(0) : null;
	}
}
