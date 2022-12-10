package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@NamedQueries({
	@NamedQuery(name = ArtikelTruTopsMetadatenQuery.ByArtikelTruTopsIId,
			query = "SELECT OBJECT(o) from ArtikelTruTopsMetadaten o WHERE o.artikelTruTopsIId = :artikelTruTopsId"),
	@NamedQuery(name = ArtikelTruTopsMetadatenQuery.ByArtikelIId,
		query = "SELECT OBJECT(o) from ArtikelTruTopsMetadaten o LEFT JOIN o.artikelTruTops a WHERE a.artikelIId = :artikelid")
})
@Entity
@Table(name = ITablenames.WW_ARTIKELTRUTOPSMETADATEN)
public class ArtikelTruTopsMetadaten implements Serializable {
	private static final long serialVersionUID = -5530350649006668145L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "ARTIKELTRUTOPS_I_ID")
	private Integer artikelTruTopsIId;
	
	@Column(name = "C_FILENAME")
	private String cFilename;
	
	@Column(name = "I_SIZE")
	private Integer iSize;
	
	@Column(name = "T_CREATION")
	private Timestamp tCreation;
	
	@Column(name = "T_MODIFICATION")
	private Timestamp tModification;
	
	@Column(name = "C_HASH")
	private String cHash;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTIKELTRUTOPS_I_ID", referencedColumnName = "I_ID", insertable = false, updatable = false)
	private ArtikelTruTops artikelTruTops;
	
	public ArtikelTruTopsMetadaten() {
	}


	public Integer getIId() {
		return iId;
	}


	public void setIId(Integer iId) {
		this.iId = iId;
	}


	public Integer getArtikelTruTopsIId() {
		return artikelTruTopsIId;
	}


	public void setArtikelTruTopsIId(Integer artikelTruTopsIId) {
		this.artikelTruTopsIId = artikelTruTopsIId;
	}


	public String getCFilename() {
		return cFilename;
	}


	public void setCFilename(String cFilename) {
		this.cFilename = cFilename;
	}


	public Integer getISize() {
		return iSize;
	}


	public void setISize(Integer iSize) {
		this.iSize = iSize;
	}


	public Timestamp getTCreation() {
		return tCreation;
	}


	public void setTCreation(Timestamp tCreation) {
		this.tCreation = tCreation;
	}


	public Timestamp getTModification() {
		return tModification;
	}


	public void setTModification(Timestamp tModification) {
		this.tModification = tModification;
	}


	public String getCHash() {
		return cHash;
	}


	public void setCHash(String cHash) {
		this.cHash = cHash;
	}

}
