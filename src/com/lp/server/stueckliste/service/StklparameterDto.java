package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.util.EJBExceptionLP;

public class StklparameterDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cNr;
	private String cTyp;
	private BigDecimal nMin;
	private BigDecimal nMax;
	private Integer iSort;
	private Integer stuecklisteIId;

	private String cWert;

	public String getCWert() {
		return this.cWert;
	}

	public void setCWert(String cWert) {
		this.cWert = cWert;
	}

	
	public Object getCWertAsObject() {

		Object ret = null;
		if (this.getCTyp().equals(StuecklisteFac.STKLPARAMETER_TYP_STRING)) {
			ret = getCWert();
		} else if (this.getCTyp().equals(StuecklisteFac.STKLPARAMETER_TYP_INTEGER)) {
			ret = new Integer(cWert);
		} else if (this.getCTyp().equals(StuecklisteFac.STKLPARAMETER_TYP_BOOLEAN)) {
			ret = new Boolean(Integer.parseInt(getCWert()) != 0);
		} else if (this.getCTyp().equals(StuecklisteFac.STKLPARAMETER_TYP_BIGDECIMAL)) {
			ret = new BigDecimal(getCWert());
		} else if (this.getCTyp().equals(StuecklisteFac.STKLPARAMETER_TYP_DOUBLE)) {
			ret = new Double(getCWert());
		} else if (this.getCTyp().equals(StuecklisteFac.STKLPARAMETER_TYP_ITEM_ID)) {
			ret = getCWert();
		} else if (this.getCTyp().equals(StuecklisteFac.STKLPARAMETER_TYP_KUNDE_ID)) {
			ret = getCWert();
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
					new Exception());
		}
		return ret;
	}
	
	
	private String cBereich;

	public String getCBereich() {
		return cBereich;
	}

	public void setCBereich(String cBereich) {
		this.cBereich = cBereich;
	}
	
	private Short bCombobox;
	public Short getBCombobox() {
		return bCombobox;
	}

	public void setBCombobox(Short bCombobox) {
		this.bCombobox = bCombobox;
	}
	
	private StklparametersprDto stklparametersprDto;

	public StklparametersprDto getStklparametersprDto() {
		return stklparametersprDto;
	}

	public void setStklparametersprDto(StklparametersprDto stklparametersprDto) {
		this.stklparametersprDto = stklparametersprDto;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNR) {
		this.cNr = cNR;
	}

	public String getCTyp() {
		return cTyp;
	}

	public void setCTyp(String cTyp) {
		this.cTyp = cTyp;
	}

	public BigDecimal getNMin() {
		return nMin;
	}

	public void setNMin(BigDecimal nMin) {
		this.nMin = nMin;
	}

	public BigDecimal getNMax() {
		return nMax;
	}

	public void setNMax(BigDecimal nMax) {
		this.nMax = nMax;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getStuecklisteIId() {
		return stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}
	
    private Short bMandatory;
	
	
	public Short getBMandatory() {
		return bMandatory;
	}

	public void setBMandatory(Short bMandatory) {
		this.bMandatory = bMandatory;
	}
	

}
