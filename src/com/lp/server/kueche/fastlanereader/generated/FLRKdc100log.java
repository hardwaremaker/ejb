/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.kueche.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKdc100log implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_seriennummer;

    /** nullable persistent field */
    private String c_barcode;

    /** nullable persistent field */
    private String c_art;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private Date t_buchungszeit;

    /** nullable persistent field */
    private Date t_stiftzeit;

    /** full constructor */
    public FLRKdc100log(String c_seriennummer, String c_barcode, String c_art, String c_kommentar, Date t_buchungszeit, Date t_stiftzeit) {
        this.c_seriennummer = c_seriennummer;
        this.c_barcode = c_barcode;
        this.c_art = c_art;
        this.c_kommentar = c_kommentar;
        this.t_buchungszeit = t_buchungszeit;
        this.t_stiftzeit = t_stiftzeit;
    }

    /** default constructor */
    public FLRKdc100log() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_seriennummer() {
        return this.c_seriennummer;
    }

    public void setC_seriennummer(String c_seriennummer) {
        this.c_seriennummer = c_seriennummer;
    }

    public String getC_barcode() {
        return this.c_barcode;
    }

    public void setC_barcode(String c_barcode) {
        this.c_barcode = c_barcode;
    }

    public String getC_art() {
        return this.c_art;
    }

    public void setC_art(String c_art) {
        this.c_art = c_art;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public Date getT_buchungszeit() {
        return this.t_buchungszeit;
    }

    public void setT_buchungszeit(Date t_buchungszeit) {
        this.t_buchungszeit = t_buchungszeit;
    }

    public Date getT_stiftzeit() {
        return this.t_stiftzeit;
    }

    public void setT_stiftzeit(Date t_stiftzeit) {
        this.t_stiftzeit = t_stiftzeit;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
