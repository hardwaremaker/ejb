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
package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelkommentarart;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKommentarimport implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private Integer artikelkommentarart_i_id;

    /** nullable persistent field */
    private FLRArtikelkommentarart flrartikelkommentarart;

    /** full constructor */
    public FLRKommentarimport(String belegart_c_nr, Integer artikelkommentarart_i_id, FLRArtikelkommentarart flrartikelkommentarart) {
        this.belegart_c_nr = belegart_c_nr;
        this.artikelkommentarart_i_id = artikelkommentarart_i_id;
        this.flrartikelkommentarart = flrartikelkommentarart;
    }

    /** default constructor */
    public FLRKommentarimport() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getBelegart_c_nr() {
        return this.belegart_c_nr;
    }

    public void setBelegart_c_nr(String belegart_c_nr) {
        this.belegart_c_nr = belegart_c_nr;
    }

    public Integer getArtikelkommentarart_i_id() {
        return this.artikelkommentarart_i_id;
    }

    public void setArtikelkommentarart_i_id(Integer artikelkommentarart_i_id) {
        this.artikelkommentarart_i_id = artikelkommentarart_i_id;
    }

    public FLRArtikelkommentarart getFlrartikelkommentarart() {
        return this.flrartikelkommentarart;
    }

    public void setFlrartikelkommentarart(FLRArtikelkommentarart flrartikelkommentarart) {
        this.flrartikelkommentarart = flrartikelkommentarart;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
