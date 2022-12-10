package com.lp.server.util;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Id;

public class BaseIntegerKey implements Serializable {
	private static final long serialVersionUID = -1234875041580643897L;

	private Integer key;
	
	// JaxB Kompatitbilitaet, bitte nicht programmatisch benutzen
	protected BaseIntegerKey() {
	}
	
	public BaseIntegerKey(Integer value) {
		this.key = value;
	}
	
	public Integer id() {
		return key;
	}
	

	public void id(Integer key) {
		this.key = key;
	}

	public boolean isValid() {
		return id() != null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		BaseIntegerKey other = (BaseIntegerKey) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
	public String toString() {
		return super.toString() + "(" + id() + ")";		
	}
}
