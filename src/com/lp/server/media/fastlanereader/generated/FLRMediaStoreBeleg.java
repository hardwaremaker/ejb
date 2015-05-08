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
 *******************************************************************************/
package com.lp.server.media.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMediaStoreBeleg implements Serializable {
	private static final long serialVersionUID = -3736214805518234099L;

	/** identifier field */
    private Integer i_id;

    private String mandant_c_nr ;
    
    /** persistent field */
    private Integer media_i_id;

    private String c_belegartnr ;

    private Integer beleg_i_id ;
    
    private Integer belegposition_i_id ;
    
    /** persistent field */
    private Date t_anlegen;
    
    /** persistent field */
    private Integer personal_i_id_anlegen;

    /** persistent field */
    private Date t_aendern;

    /** persistent field */
    private Integer personal_i_id_aendern;

    /** persistent field */
    private Date t_gelesen;
    
    /** nullable persistent field */
    private com.lp.server.media.fastlanereader.generated.FLRMediaEmailMeta flrmedia;

    /** full constructor */
    public FLRMediaStoreBeleg(Integer media_i_id, Integer personal_i_id, String mandant_c_nr, String c_belegartnr, Date t_anlegen, Integer personal_i_id_anlegen, Date t_aendern, Integer personal_i_id_aendern, com.lp.server.media.fastlanereader.generated.FLRMediaEmailMeta flrmedia) {
        this.media_i_id = media_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.c_belegartnr = c_belegartnr;
        this.t_anlegen = t_anlegen;
        this.personal_i_id_anlegen = personal_i_id_anlegen;
        this.t_aendern = t_aendern;
        this.personal_i_id_aendern = personal_i_id_aendern;
        this.flrmedia = flrmedia;
    }

    /** default constructor */
    public FLRMediaStoreBeleg() {
    }

    /** minimal constructor */
    public FLRMediaStoreBeleg(Integer media_i_id, String mandant_c_nr, String c_belegartnr, Date t_anlegen, Integer personal_i_id_anlegen, Date t_aendern, Integer personal_i_id_aendern) {
        this.media_i_id = media_i_id;
        this.mandant_c_nr = mandant_c_nr ;
        this.c_belegartnr = c_belegartnr;
        this.t_anlegen = t_anlegen;
        this.personal_i_id_anlegen = personal_i_id_anlegen;
        this.t_aendern = t_aendern;
        this.personal_i_id_aendern = personal_i_id_aendern;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getMedia_i_id() {
        return this.media_i_id;
    }

    public void setMedia_i_id(Integer media_i_id) {
        this.media_i_id = media_i_id;
    }

  
    public String getMandant_c_nr() {
		return mandant_c_nr;
	}

	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}

	public String getC_belegartnr() {
		return c_belegartnr;
	}

	public void setC_belegartnr(String c_belegartnr) {
		this.c_belegartnr = c_belegartnr;
	}

	public Integer getBeleg_i_id() {
		return beleg_i_id;
	}

	public void setBeleg_i_id(Integer beleg_i_id) {
		this.beleg_i_id = beleg_i_id;
	}

	public Integer getBelegposition_i_id() {
		return belegposition_i_id;
	}

	public void setBelegposition_i_id(Integer belegposition_i_id) {
		this.belegposition_i_id = belegposition_i_id;
	}

	public Date getT_gelesen() {
		return t_gelesen;
	}

	public void setT_gelesen(Date t_gelesen) {
		this.t_gelesen = t_gelesen;
	}

	public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Integer getPersonal_i_id_anlegen() {
        return this.personal_i_id_anlegen;
    }

    public void setPersonal_i_id_anlegen(Integer personal_i_id_anlegen) {
        this.personal_i_id_anlegen = personal_i_id_anlegen;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Integer getPersonal_i_id_aendern() {
        return this.personal_i_id_aendern;
    }

    public void setPersonal_i_id_aendern(Integer personal_i_id_aendern) {
        this.personal_i_id_aendern = personal_i_id_aendern;
    }

    public com.lp.server.media.fastlanereader.generated.FLRMediaEmailMeta getFlrmedia() {
        return this.flrmedia;
    }

    public void setFlrmedia(com.lp.server.media.fastlanereader.generated.FLRMediaEmailMeta flrmedia) {
        this.flrmedia = flrmedia;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }
}
