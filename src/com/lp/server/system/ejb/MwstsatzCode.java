package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;
import com.lp.server.util.IIId;

@NamedQueries( {
	@NamedQuery(name = MwstsatzCodeQuery.ByMwstsatzIIdReversechargeartIId, query = "SELECT OBJECT (o) FROM MwstsatzCode o WHERE o.mwstsatzIId=:mwstsatzId AND o.reversechargeartIId=:rcartId"), 
	@NamedQuery(name = MwstsatzCodeQuery.IIdsByMwstsatzIId, query = "SELECT o.iId FROM MwstsatzCode o WHERE o.mwstsatzIId=:mwstsatzId") 
})

@Entity
@Table(name = ITablenames.LP_MWSTSATZCODE)
public class MwstsatzCode implements Serializable, IIId {
	private static final long serialVersionUID = -5239005263796095069L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MWSTSATZ_I_ID")
	private Integer mwstsatzIId;
	
	@Column(name = "REVERSECHARGEART_I_ID")
	private Integer reversechargeartIId;
	
	@Column(name = "C_STEUERCODE_AR")
	private String cSteuercodeAr;
	
	@Column(name = "C_STEUERCODE_ER")
	private String cSteuercodeEr;
	
	public MwstsatzCode() {
	}
	
	public MwstsatzCode(Integer iId, Integer mwstsatzIId, Integer reversechargeartIId, String cSteuercodeAr, String steuercodeEr) {
		setIId(iId);
		setMwstsatzIId(mwstsatzIId);
		setReversechargeartIId(reversechargeartIId);
		setCSteuercodeAr(cSteuercodeAr);
		setCSteuercodeEr(steuercodeEr);
	}

	@Override
	public Integer getIId() {
		return iId;
	}

	@Override
	public void setIId(Integer newIId) {
		this.iId = newIId;
	}
	
	public Integer getMwstsatzIId() {
		return mwstsatzIId;
	}
	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}
	
	public Integer getReversechargeartIId() {
		return reversechargeartIId;
	}
	public void setReversechargeartIId(Integer reversechargeartIId) {
		this.reversechargeartIId = reversechargeartIId;
	}
	
	public String getCSteuercodeAr() {
		return cSteuercodeAr;
	}
	
	public void setCSteuercodeAr(String cSteuercodeAr) {
		this.cSteuercodeAr = cSteuercodeAr;
	}
	
	public String getCSteuercodeEr() {
		return cSteuercodeEr;
	}
	
	public void setCSteuercodeEr(String cSteuercodeEr) {
		this.cSteuercodeEr = cSteuercodeEr;
	}
}
