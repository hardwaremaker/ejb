package com.lp.server.system.ejbfac;

import java.io.Serializable;

import com.lp.server.util.MwstsatzId;
import com.lp.server.util.ReversechargeartId;

public class SteuercodeInfo implements Serializable {
	private static final long serialVersionUID = 6167047129129809385L;

	private MwstsatzId mwstsatzId;
	private ReversechargeartId reversechargeartId;
	private String code;
	
	public SteuercodeInfo() {
	}

	public MwstsatzId getMwstsatzId() {
		return mwstsatzId;
	}
	public void setMwstsatzId(MwstsatzId mwstsatzId) {
		this.mwstsatzId = mwstsatzId;
	}
	public Integer getMwstsatzIId() {
		return mwstsatzId == null ? null : mwstsatzId.id();
	}
	public void setMwstsatzIId(Integer mwstsatzIId) {
		mwstsatzId = new MwstsatzId(mwstsatzIId);
	}
	
	public ReversechargeartId getReversechargeartId() {
		return reversechargeartId;
	}
	public void setReversechargeartId(ReversechargeartId reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}
	public Integer getReversechargeartIId() {
		return reversechargeartId == null ? null : reversechargeartId.id();
	}
	public void setReversechargeartIId(Integer reversechargeartIId) {
		reversechargeartId = new ReversechargeartId(reversechargeartIId);
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
