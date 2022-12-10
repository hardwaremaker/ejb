package com.lp.server.finanz.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.server.system.service.ITablenames;

@NamedQueries({
	@NamedQuery(name = Iso20022Query.ZahlungsauftragSchemaByStandardCNr, query = "SELECT OBJECT (o) FROM Iso20022ZahlungsauftragSchema o JOIN o.standard s WHERE s.cNr = ?1")
})

@Entity
@Table(name = ITablenames.FB_ISO20022ZAHLUNGSAUFTRAGSCHEMA)
@Inheritance(strategy = InheritanceType.JOINED)
public class Iso20022ZahlungsauftragSchema implements Serializable, IIso20022NachrichtEntity {
	private static final long serialVersionUID = 1266603324007962216L;

	@Id
	@Column(name = "I_ID")
	@TableGenerator(name="iso20022zahlungsauftragschema_id", table="LP_PRIMARYKEY",
		pkColumnName = "C_NAME", pkColumnValue="iso20022zahlungsauftragschema", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="iso20022zahlungsauftragschema_id")
	private Integer iId;

	@Column(name = "STANDARD_I_ID")
	private Integer standardIId;
	
	@Column(name = "SCHEMA_I_ID")
	private Integer schemaIId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STANDARD_I_ID" , referencedColumnName= "I_ID", insertable = false, updatable = false)
	private Iso20022Standard standard;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHEMA_I_ID" , referencedColumnName= "I_ID", insertable = false, updatable = false)
	private Iso20022Schema schema;
	
	public Integer getIId() {
		return iId;
	}
	
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	public Integer getStandardIId() {
		return standardIId;
	}
	public void setStandardIId(Integer standardIId) {
		this.standardIId = standardIId;
	}
	
	public Integer getSchemaIId() {
		return schemaIId;
	}
	public void setSchemaIId(Integer schemaIId) {
		this.schemaIId = schemaIId;
	}
	
	public Iso20022Standard getStandardObject() {
		return standard;
	}

	public Iso20022Schema getSchemaObject() {
		return schema;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result	+ ((standardIId == null) ? 0 : standardIId.hashCode());
		result = prime * result	+ ((schemaIId == null) ? 0 : schemaIId.hashCode());
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
		
		Iso20022ZahlungsauftragSchema other = (Iso20022ZahlungsauftragSchema) obj;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		
		if (standardIId == null) {
			if (other.standardIId != null)
				return false;
		} else if (!standardIId.equals(other.standardIId))
			return false;

		if (schemaIId == null) {
			if (other.schemaIId != null)
				return false;
		} else if (!schemaIId.equals(other.schemaIId))
			return false;
		
		return true;
	}
	
}
