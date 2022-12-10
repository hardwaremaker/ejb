/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.finanz.fastlanereader.generated;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;


/** @author Hibernate CodeGenerator */
public class FLRFinanzBuchung implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1140871459295986253L;

	/** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_text;

    /** nullable persistent field */
    private String c_belegnummer;

    /** nullable persistent field */
    private Date d_buchungsdatum;

    /** nullable persistent field */
    private String buchungsart_c_nr;

    /** nullable persistent field */
    private Timestamp t_anlegen;

    /** nullable persistent field */
    private Date t_storniert;

    /** nullable persistent field */
    private Integer geschaeftsjahr_i_geschaeftsjahr;

    /** nullable persistent field */
    private Integer personal_i_id_anlegen;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFbbelegart flrfbbelegart;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRBuchungsart flrbuchungsart;

    private Short b_autombuchung ;
    
    private Short b_autombuchungeb ;
    
    private Integer uvaverprobung_i_id ;

    /** full constructor */
    public FLRFinanzBuchung(String c_text, String c_belegnummer, Date d_buchungsdatum, String buchungsart_c_nr, Timestamp t_anlegen, Date t_storniert, Integer geschaeftsjahr_i_geschaeftsjahr, Integer personal_i_id_anlegen, Integer uvaverprobung_i_id, FLRKostenstelle flrkostenstelle, com.lp.server.finanz.fastlanereader.generated.FLRFbbelegart flrfbbelegart, com.lp.server.finanz.fastlanereader.generated.FLRBuchungsart flrbuchungsart) {
        this.c_text = c_text;
        this.c_belegnummer = c_belegnummer;
        this.d_buchungsdatum = d_buchungsdatum;
        this.buchungsart_c_nr = buchungsart_c_nr;
        this.t_anlegen = t_anlegen;
        this.t_storniert = t_storniert;
        this.geschaeftsjahr_i_geschaeftsjahr = geschaeftsjahr_i_geschaeftsjahr;
        this.personal_i_id_anlegen = personal_i_id_anlegen;
        this.flrkostenstelle = flrkostenstelle;
        this.flrfbbelegart = flrfbbelegart;
        this.flrbuchungsart = flrbuchungsart;
    }

    /** default constructor */
    public FLRFinanzBuchung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_text() {
        return this.c_text;
    }

    public void setC_text(String c_text) {
        this.c_text = c_text;
    }

    public String getC_belegnummer() {
        return this.c_belegnummer;
    }

    public void setC_belegnummer(String c_belegnummer) {
        this.c_belegnummer = c_belegnummer;
    }

    public Date getD_buchungsdatum() {
        return this.d_buchungsdatum;
    }

    public void setD_buchungsdatum(Date d_buchungsdatum) {
        this.d_buchungsdatum = d_buchungsdatum;
    }

    public String getBuchungsart_c_nr() {
        return this.buchungsart_c_nr;
    }

    public void setBuchungsart_c_nr(String buchungsart_c_nr) {
        this.buchungsart_c_nr = buchungsart_c_nr;
    }

    public Timestamp getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Timestamp t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Date getT_storniert() {
        return this.t_storniert;
    }

    public void setT_storniert(Date t_storniert) {
        this.t_storniert = t_storniert;
    }

    public Integer getGeschaeftsjahr_i_geschaeftsjahr() {
        return this.geschaeftsjahr_i_geschaeftsjahr;
    }

    public void setGeschaeftsjahr_i_geschaeftsjahr(Integer geschaeftsjahr_i_geschaeftsjahr) {
        this.geschaeftsjahr_i_geschaeftsjahr = geschaeftsjahr_i_geschaeftsjahr;
    }

    public Integer getPersonal_i_id_anlegen() {
        return this.personal_i_id_anlegen;
    }

    public void setPersonal_i_id_anlegen(Integer personal_i_id_anlegen) {
        this.personal_i_id_anlegen = personal_i_id_anlegen;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFbbelegart getFlrfbbelegart() {
        return this.flrfbbelegart;
    }

    public void setFlrfbbelegart(com.lp.server.finanz.fastlanereader.generated.FLRFbbelegart flrfbbelegart) {
        this.flrfbbelegart = flrfbbelegart;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRBuchungsart getFlrbuchungsart() {
        return this.flrbuchungsart;
    }

    public void setFlrbuchungsart(com.lp.server.finanz.fastlanereader.generated.FLRBuchungsart flrbuchungsart) {
        this.flrbuchungsart = flrbuchungsart;
    }

	public Short getB_autombuchung() {
		return b_autombuchung;
	}

	public void setB_autombuchung(Short b_autombuchung) {
		this.b_autombuchung = b_autombuchung;
	}

    public Short getB_autombuchungeb() {
		return b_autombuchungeb;
	}

	public void setB_autombuchungeb(Short b_autombuchungeb) {
		this.b_autombuchungeb = b_autombuchungeb;
	}

    public Integer getUvaverprobung_i_id() {
		return uvaverprobung_i_id ;
	}

	public void setUvaverprobung_i_id(Integer uvaverprobungIId) {
		this.uvaverprobung_i_id = uvaverprobungIId;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }
}
