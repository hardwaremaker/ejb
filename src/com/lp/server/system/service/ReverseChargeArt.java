package com.lp.server.system.service;

import java.io.Serializable;

public class ReverseChargeArt implements Serializable {
	private static final long serialVersionUID = -6020285029313252668L;
	
	private Integer id ;

	public ReverseChargeArt() {
	}
	
	public ReverseChargeArt(Integer id) {
		setId(id) ;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
