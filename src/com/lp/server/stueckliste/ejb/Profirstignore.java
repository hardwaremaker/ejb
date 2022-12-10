package com.lp.server.stueckliste.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries( { @NamedQuery(name = "ProfirstignorefindAll", query = "SELECT OBJECT(o) FROM Profirstignore o"),
	@NamedQuery(name = "ProfirstignorefindByCKbez", query = "SELECT OBJECT(o) FROM Profirstignore o WHERE o.cKbez = ?1")})
@Entity
@Table(name = "STK_PROFIRSTIGNORE")
public class Profirstignore {
	@Id
	@Column(name = "C_KBEZ")
	private String cKbez;
	


	public String getCKbez() {
		return cKbez;
	}

	public void setCKbez(String cKbez) {
		this.cKbez = cKbez;
	}

	public Profirstignore() {
		super();
	}

	public Profirstignore( String cKbez) {
		setCKbez(cKbez);
	}

	
}
