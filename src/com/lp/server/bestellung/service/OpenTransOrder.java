package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OpenTransOrder implements Serializable {
	private static final long serialVersionUID = -5991106944923410378L;

	private OpenTransOrderHead head;
	private List<OpenTransOrderPosition> positions;
	
	public OpenTransOrderHead getHead() {
		return head;
	}
	
	public void setHead(OpenTransOrderHead head) {
		this.head = head;
	}
	
	public List<OpenTransOrderPosition> getPositions() {
		if (positions == null) {
			positions = new ArrayList<OpenTransOrderPosition>();
		}
		return positions;
	}
	
	public void setPositions(List<OpenTransOrderPosition> positions) {
		this.positions = positions;
	}
	
	public Integer getNumberOfPositions() {
		return getPositions().size();
	}
}
