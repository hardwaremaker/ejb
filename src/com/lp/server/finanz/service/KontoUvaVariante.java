package com.lp.server.finanz.service;

import java.io.Serializable;

public class KontoUvaVariante implements Serializable {
	private static final long serialVersionUID = 1275515524633765741L;

	private final int variante;
	
	public KontoUvaVariante() {
		this(0);
	}
	
	public KontoUvaVariante(int variante) {
		this.variante = variante;
	}
	
	
	public int getVariante() {
		return this.variante;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + variante;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KontoUvaVariante other = (KontoUvaVariante) obj;
		if (variante != other.variante)
			return false;
		return true;
	}
}
