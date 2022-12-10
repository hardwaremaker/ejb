package com.lp.server.stueckliste.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { @NamedQuery(name = "ArbeitsgangartfindAll", query = "SELECT OBJECT(o) FROM Arbeitsgangart o") })
@Entity
@Table(name = "STK_AGART")
public class Arbeitsgangart implements Serializable {

	private static final long serialVersionUID = -6340083563397832289L;

	@Id
	@Column(name = "C_NR")
	private String cNr;

	public Arbeitsgangart() {
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}
}
