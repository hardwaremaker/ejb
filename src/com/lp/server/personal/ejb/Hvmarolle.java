
package com.lp.server.personal.ejb;



import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "HvmarollefindBySystemrolleIIdHvmarechtIId", query = "SELECT OBJECT(o) FROM Hvmarolle o WHERE o.systemrolleIId=?1 AND o.hvmarechtIId=?2")})

@Entity
@Table(name = "PERS_HVMAROLLE")
public class Hvmarolle implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "SYSTEMROLLE_I_ID")
	private Integer systemrolleIId;

	@Column(name = "HVMARECHT_I_ID")
	private Integer hvmarechtIId;
	

	private static final long serialVersionUID = 1L;

	public Hvmarolle() {
		super();
	}

	public Hvmarolle(Integer id, Integer systemrolleIId, Integer hvmarechtIId) {
		setSystemrolleIId(systemrolleIId);
		setIId(id);
		setHvmarechtIId(hvmarechtIId);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getSystemrolleIId() {
		return systemrolleIId;
	}

	public void setSystemrolleIId(Integer systemrolleIId) {
		this.systemrolleIId = systemrolleIId;
	}

	public Integer getHvmarechtIId() {
		return hvmarechtIId;
	}

	public void setHvmarechtIId(Integer hvmarechtIId) {
		this.hvmarechtIId = hvmarechtIId;
	}




	

}
