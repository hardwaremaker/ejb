package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@NamedQueries({
	@NamedQuery(name = ArtikelTruTopsQuery.ByArtikelIId,
			query = "SELECT OBJECT(o) from ArtikelTruTops o WHERE o.artikelIId = :artikelid")
})
@Entity
@Table(name = ITablenames.WW_ARTIKELTRUTOPS)
public class ArtikelTruTops implements Serializable {
	private static final long serialVersionUID = 5908283385478837736L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;
	
	@Column(name = "T_EXPORT_BEGINN")
	private Timestamp tExportBeginn;
	
	@Column(name = "T_EXPORT_ENDE")
	private Timestamp tExportEnde;
	
	@Column(name = "C_PFAD")
	private String cPfad;
	
	@Column(name = "C_EXPORT_PFAD")
	private String cExportPfad;
	
	@Column(name = "C_FEHLERCODE")
	private String cFehlercode;
	
	@Column(name = "C_FEHLERTEXT")
	private String cFehlertext;
	
	public ArtikelTruTops() {
	}
	
	public ArtikelTruTops(Integer artikelId) {
		setArtikelIId(artikelId);
	}
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	public Integer getArtikelIId() {
		return artikelIId;
	}
	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}
	
	public Timestamp getTExportBeginn() {
		return tExportBeginn;
	}
	public void setTExportBeginn(Timestamp tExportBeginn) {
		this.tExportBeginn = tExportBeginn;
	}
	
	public Timestamp getTExportEnde() {
		return tExportEnde;
	}
	public void setTExportEnde(Timestamp tExportEnde) {
		this.tExportEnde = tExportEnde;
	}
	
	public String getCPfad() {
		return cPfad;
	}
	public void setCPfad(String cPfad) {
		this.cPfad = cPfad;
	}
	
	public String getCExportPfad() {
		return cExportPfad;
	}
	public void setCExportPfad(String cExportPfad) {
		this.cExportPfad = cExportPfad;
	}
	
	public String getCFehlercode() {
		return cFehlercode;
	}
	public void setCFehlercode(String cFehlercode) {
		this.cFehlercode = cFehlercode;
	}
	
	public String getCFehlertext() {
		return cFehlertext;
	}
	public void setCFehlertext(String cFehlertext) {
		this.cFehlertext = cFehlertext;
	}

}
