package com.lp.server.system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.util.EJBExceptionLP;

public class CreatePaneldatenResult implements Serializable{
	private static final long serialVersionUID = 5533483683072595541L;

	private Integer paneldatenIId;
	private List<EJBExceptionLP> ejbExceptions;
	
	public CreatePaneldatenResult() {
	}
	
	public CreatePaneldatenResult(Integer paneldatenIId) {
		this();
		setPaneldatenIId(paneldatenIId);
	}

	public Integer getPaneldatenIId() {
		return paneldatenIId;
	}
	public void setPaneldatenIId(Integer paneldatenIId) {
		this.paneldatenIId = paneldatenIId;
	}
	
	public List<EJBExceptionLP> getEjbExceptions() {
		if (ejbExceptions == null) {
			ejbExceptions = new ArrayList<EJBExceptionLP>();
		}
		return ejbExceptions;
	}
	public void setEjbExceptions(List<EJBExceptionLP> ejbExceptions) {
		this.ejbExceptions = ejbExceptions;
	}
	
	public boolean hasEjbExceptions() {
		return !getEjbExceptions().isEmpty();
	}
}
