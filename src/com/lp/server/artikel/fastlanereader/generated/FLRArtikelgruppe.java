package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;


/** @author Hibernate CodeGenerator */
public class FLRArtikelgruppe implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private Integer artgru_i_id;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Short b_fremdfertigung;

    /** nullable persistent field */
    private Short b_bei_erster_zeitbuchung_abbuchen;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe;

    /** persistent field */
    private Set artikelgruppesprset;

    /** persistent field */
    private Set konten;

    /** full constructor */
    public FLRArtikelgruppe(String c_nr, Integer artgru_i_id, String mandant_c_nr, Short b_fremdfertigung, Short b_bei_erster_zeitbuchung_abbuchen, com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe, Set artikelgruppesprset, Set konten) {
        this.c_nr = c_nr;
        this.artgru_i_id = artgru_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.b_fremdfertigung = b_fremdfertigung;
        this.b_bei_erster_zeitbuchung_abbuchen = b_bei_erster_zeitbuchung_abbuchen;
        this.flrartikelgruppe = flrartikelgruppe;
        this.artikelgruppesprset = artikelgruppesprset;
        this.konten = konten;
    }

    public FLRArtikelgruppe(Integer i_id, Integer artgru_i_id) {
		this.i_id = i_id;
		this.artgru_i_id = artgru_i_id;
	}
    
    /** default constructor */
    public FLRArtikelgruppe() {
    }

    /** minimal constructor */
    public FLRArtikelgruppe(String mandant_c_nr, Set artikelgruppesprset, Set konten) {
        this.mandant_c_nr = mandant_c_nr;
        this.artikelgruppesprset = artikelgruppesprset;
        this.konten = konten;
    }

    

    /**
	 * @deprecated PJ19150 use getFlrkonto(boolean bZentralerArtikelstamm,String
	 *             mandantCNrHauptmandant)
	 */
	public FLRFinanzKonto getFlrkonto() {

		if (this.konten != null && this.konten.size() > 0) {
			Iterator it = konten.iterator();
			while (it.hasNext()) {
				FLRArtikelgruppemandant artgrumandant = (FLRArtikelgruppemandant) it
						.next();

				if (artgrumandant.getKonto_i_id() != null) {

					if (artgrumandant.getMandant_c_nr().equals(mandant_c_nr)) {
						return artgrumandant.getFlrkonto();
					}
				}
			}
		}

		return null;

	}

	public FLRFinanzKonto getFlrkonto(String mandantCNr) {

		if (this.konten != null && this.konten.size() > 0) {
			Iterator it = konten.iterator();

			while (it.hasNext()) {
				FLRArtikelgruppemandant artgrumandant = (FLRArtikelgruppemandant) it
						.next();

				if (artgrumandant.getKonto_i_id() != null) {

					if (artgrumandant.getMandant_c_nr().equals(mandantCNr)) {

						return artgrumandant.getFlrkonto();

					}

				}

			}

		}

		return null;

	}
    
    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public Integer getArtgru_i_id() {
        return this.artgru_i_id;
    }

    public void setArtgru_i_id(Integer artgru_i_id) {
        this.artgru_i_id = artgru_i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Short getB_fremdfertigung() {
        return this.b_fremdfertigung;
    }

    public void setB_fremdfertigung(Short b_fremdfertigung) {
        this.b_fremdfertigung = b_fremdfertigung;
    }

    public Short getB_bei_erster_zeitbuchung_abbuchen() {
        return this.b_bei_erster_zeitbuchung_abbuchen;
    }

    public void setB_bei_erster_zeitbuchung_abbuchen(Short b_bei_erster_zeitbuchung_abbuchen) {
        this.b_bei_erster_zeitbuchung_abbuchen = b_bei_erster_zeitbuchung_abbuchen;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe getFlrartikelgruppe() {
        return this.flrartikelgruppe;
    }

    public void setFlrartikelgruppe(com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe) {
        this.flrartikelgruppe = flrartikelgruppe;
    }

    public Set getArtikelgruppesprset() {
        return this.artikelgruppesprset;
    }

    public void setArtikelgruppesprset(Set artikelgruppesprset) {
        this.artikelgruppesprset = artikelgruppesprset;
    }

    public Set getKonten() {
        return this.konten;
    }

    public void setKonten(Set konten) {
        this.konten = konten;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
