package com.lp.server.system.service;

import java.io.Serializable;

import com.lp.server.util.IIId;
import com.lp.server.util.MwstsatzId;
import com.lp.server.util.ReversechargeartId;

public class MwstsatzCodeDto implements Serializable, IIId {
	private static final long serialVersionUID = 721321831875642303L;
	
	private Integer iId;
	private MwstsatzId mwstsatzId;
	private ReversechargeartId reversechargeartId;
	private String cSteuercodeAr;
	private String cSteuercodeEr;
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer IId) {
		this.iId = IId;
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
	
	public String getCSteuercodeAr() {
		return cSteuercodeAr;
	}
	public void setCSteuercodeAr(String cSteuercodeAr) {
		this.cSteuercodeAr = cSteuercodeAr;
	}
	
	public String getCSteuercodeEr() {
		return cSteuercodeEr;
	}
	public void setCSteuercodeEr(String cSteuercodeEr) {
		this.cSteuercodeEr = cSteuercodeEr;
	}
}
