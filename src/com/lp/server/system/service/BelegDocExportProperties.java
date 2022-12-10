package com.lp.server.system.service;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class BelegDocExportProperties implements Serializable {
	private static final long serialVersionUID = 2983224183786345883L;

	private Date startdate;
	private Date enddate;
	private List<Integer> ids;

	public BelegDocExportProperties() {
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getStartdate() {
		return startdate;
	}
	
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	public Date getEnddate() {
		return enddate;
	}
	
	public List<Integer> getIds() {
		if (ids == null) {
			ids = new ArrayList<Integer>();
		}
		return ids;
	}
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
}
