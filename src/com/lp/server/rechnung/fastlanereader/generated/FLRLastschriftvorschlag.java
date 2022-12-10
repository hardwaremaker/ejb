package com.lp.server.rechnung.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzMahnlauf;

public class FLRLastschriftvorschlag implements Serializable {

    private static final long serialVersionUID = -3439324372877752196L;

	private Integer i_id;
	private Integer mahnlauf_i_id;
	private Integer rechnung_i_id;
	private Date t_faellig;
	private BigDecimal n_rechnungsbetrag_brutto;
	private BigDecimal n_bereits_bezahlt;
	private BigDecimal n_zahlbetrag;
	private Date t_gespeichert;
	private String c_auftraggeberreferenz;
    private FLRRechnungReport flrrechnungreport;
    private FLRFinanzMahnlauf flrmahnlauf;
    
    public FLRLastschriftvorschlag() {
    	
    }
    
	public FLRLastschriftvorschlag(Integer mahnlauf_i_id,
			Integer rechnung_i_id, Date t_faellig, BigDecimal n_rechnungsbetrag_brutto,
			BigDecimal n_bereits_bezahlt, BigDecimal n_zahlbetrag,
			Date t_gespeichert, FLRRechnungReport flrrechnungreport,
			FLRFinanzMahnlauf flrmahnlauf) {
		super();
		this.mahnlauf_i_id = mahnlauf_i_id;
		this.rechnung_i_id = rechnung_i_id;
		this.t_faellig = t_faellig;
		this.n_rechnungsbetrag_brutto = n_rechnungsbetrag_brutto;
		this.n_bereits_bezahlt = n_bereits_bezahlt;
		this.n_zahlbetrag = n_zahlbetrag;
		this.t_gespeichert = t_gespeichert;
		this.flrrechnungreport = flrrechnungreport;
		this.flrmahnlauf = flrmahnlauf;
	}
	
	public Integer getI_id() {
		return i_id;
	}
	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}
	public Integer getMahnlauf_i_id() {
		return mahnlauf_i_id;
	}
	public void setMahnlauf_i_id(Integer mahnlauf_i_id) {
		this.mahnlauf_i_id = mahnlauf_i_id;
	}
	public Integer getRechnung_i_id() {
		return rechnung_i_id;
	}
	public void setRechnung_i_id(Integer rechnung_i_id) {
		this.rechnung_i_id = rechnung_i_id;
	}
	public Date getT_faellig() {
		return t_faellig;
	}
	public void setT_faellig(Date t_faellig) {
		this.t_faellig = t_faellig;
	}
	public BigDecimal getN_rechnungsbetrag_brutto() {
		return n_rechnungsbetrag_brutto;
	}
	public void setN_rechnungsbetrag_brutto(BigDecimal n_rechnungsbetrag_brutto) {
		this.n_rechnungsbetrag_brutto = n_rechnungsbetrag_brutto;
	}
	public BigDecimal getN_bereits_bezahlt() {
		return n_bereits_bezahlt;
	}
	public void setN_bereits_bezahlt(BigDecimal n_bereits_bezahlt) {
		this.n_bereits_bezahlt = n_bereits_bezahlt;
	}
	public BigDecimal getN_zahlbetrag() {
		return n_zahlbetrag;
	}
	public void setN_zahlbetrag(BigDecimal n_zahlbetrag) {
		this.n_zahlbetrag = n_zahlbetrag;
	}
	public Date getT_gespeichert() {
		return t_gespeichert;
	}
	public void setT_gespeichert(Date t_gespeichert) {
		this.t_gespeichert = t_gespeichert;
	}
	public FLRRechnungReport getFlrrechnungreport() {
		return flrrechnungreport;
	}
	public void setFlrrechnungreport(FLRRechnungReport flrrechnungreport) {
		this.flrrechnungreport = flrrechnungreport;
	}
	public FLRFinanzMahnlauf getFlrmahnlauf() {
		return flrmahnlauf;
	}
	public void setFlrmahnlauf(FLRFinanzMahnlauf flrmahnlauf) {
		this.flrmahnlauf = flrmahnlauf;
	}

	public String getC_auftraggeberreferenz() {
		return c_auftraggeberreferenz;
	}

	public void setC_auftraggeberreferenz(String c_auftraggeberreferenz) {
		this.c_auftraggeberreferenz = c_auftraggeberreferenz;
	}

}
