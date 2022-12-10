package com.lp.util;

import java.io.Serializable;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.rechnung.service.RechnungDto;

public class SteuerkategorieFehltException extends BelegdataBase implements Serializable {
	private static final long serialVersionUID = -3064981077628614529L;

	private Integer reversechargeartId ;
	private Integer finanzamtId ;
	private String steuerkategorieCnr ;

	public SteuerkategorieFehltException(Integer finanzamtId, Integer reversechargeartId, String steuerkategorieCnr) {
		setFinanzamtId(finanzamtId);
		setReversechargeartId(reversechargeartId);
		setSteuerkategorieCnr(steuerkategorieCnr);
	}
	
	public SteuerkategorieFehltException(EingangsrechnungDto erDto, Integer finanzamtId, Integer reversechargeartId, String steuerkategorieCnr) {
		super(erDto) ;
		setFinanzamtId(finanzamtId);
		setReversechargeartId(reversechargeartId);
		setSteuerkategorieCnr(steuerkategorieCnr);
	}

	public SteuerkategorieFehltException(RechnungDto rechnungDto, Integer finanzamtId, Integer reversechargeartId, String steuerkategorieCnr) {
		super(rechnungDto) ;
		setFinanzamtId(finanzamtId);
		setReversechargeartId(reversechargeartId);
		setSteuerkategorieCnr(steuerkategorieCnr);
	}
	
//	@Override
//	public Object[] asArray() {
//		return new Object[]{getFinanzamtId(), getReversechargeartId(), getSteuerkategorieCnr()} ;
//	}


	public Integer getReversechargeartId() {
		return reversechargeartId;
	}

	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}

	public Integer getFinanzamtId() {
		return finanzamtId;
	}

	public void setFinanzamtId(Integer finanzamtId) {
		this.finanzamtId = finanzamtId;
	}

	public String getSteuerkategorieCnr() {
		return steuerkategorieCnr;
	}

	public void setSteuerkategorieCnr(String steuerkategorieCnr) {
		this.steuerkategorieCnr = steuerkategorieCnr;
	}

}
