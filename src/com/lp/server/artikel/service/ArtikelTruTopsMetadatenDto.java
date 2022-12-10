package com.lp.server.artikel.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.util.IIId;

public class ArtikelTruTopsMetadatenDto implements Serializable, IIId {
	private static final long serialVersionUID = 8865404923368336000L;

	private Integer iId;
	private Integer artikelTruTopsIId;
	private String cFilename;
	private Integer iSize;
	private Timestamp tCreation;
	private Timestamp tModification;
	private String cHash;

	public ArtikelTruTopsMetadatenDto() {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((artikelTruTopsIId == null) ? 0 : artikelTruTopsIId.hashCode());
		result = prime * result + ((tCreation == null) ? 0 : tCreation.hashCode());
		result = prime * result + ((tModification == null) ? 0 : tModification.hashCode());
		result = prime * result + ((cFilename == null) ? 0 : cFilename.hashCode());
		result = prime * result + ((iSize == null) ? 0 : iSize.hashCode());
		result = prime * result + ((cHash == null) ? 0 : cHash.hashCode());
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
		
		ArtikelTruTopsMetadatenDto other = (ArtikelTruTopsMetadatenDto) obj;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		
		if (artikelTruTopsIId == null) {
			if (other.artikelTruTopsIId != null)
				return false;
		} else if (!artikelTruTopsIId.equals(other.artikelTruTopsIId))
			return false;

		return equalsFileAttributes(other);
	}
	
	public boolean equalsFileAttributes(ArtikelTruTopsMetadatenDto other) {
		if (tCreation == null) {
			if (other.tCreation != null)
				return false;
		} else if (!tCreation.equals(other.tCreation))
			return false;

		if (tModification == null) {
			if (other.tModification != null)
				return false;
		} else if (!tModification.equals(other.tModification))
			return false;

		if (cFilename == null) {
			if (other.cFilename != null)
				return false;
		} else if (!cFilename.equals(other.cFilename))
			return false;

		if (iSize == null) {
			if (other.iSize != null)
				return false;
		} else if (!iSize.equals(other.iSize))
			return false;

		if (cHash == null) {
			if (other.cHash != null)
				return false;
		} else if (!cHash.equals(other.cHash))
			return false;
		
		return true;
	}
}
