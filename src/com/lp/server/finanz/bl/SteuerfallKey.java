package com.lp.server.finanz.bl;

import java.io.Serializable;

import com.lp.server.util.MwstsatzId;
import com.lp.server.util.ReversechargeartId;

public class SteuerfallKey implements Serializable {
	private static final long serialVersionUID = -162024300611585887L;

	private MwstsatzId mwstsatzId;
	private ReversechargeartId rcartId;
	
	public SteuerfallKey(MwstsatzId mwstsatzId, ReversechargeartId rcartId) {
		this.mwstsatzId = mwstsatzId;
		this.rcartId = rcartId;
	}
	
	public MwstsatzId getMwstsatzId() {
		return mwstsatzId;
	}
	public ReversechargeartId getRcartId() {
		return rcartId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mwstsatzId.hashCode();
		result = prime * result + rcartId.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		if (!obj.getClass().equals(getClass())) return false;
		
		SteuerfallKey other = (SteuerfallKey) obj;
		if (mwstsatzId == null ? other.getMwstsatzId() != null : !mwstsatzId.equals(other.getMwstsatzId()))
			return false;
		
		if (rcartId == null ? other.getRcartId() != null : !rcartId.equals(other.getRcartId()))
			return false;
		
		return true;
	}

}
