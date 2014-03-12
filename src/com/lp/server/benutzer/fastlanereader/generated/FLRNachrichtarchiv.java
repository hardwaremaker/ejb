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
package com.lp.server.benutzer.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRNachrichtarchiv implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nachricht;

    /** nullable persistent field */
    private String c_erledigungsgrund;

    /** nullable persistent field */
    private Date t_zeit;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private Date t_bearbeitung;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_anlegen;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_bearbeiter;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_erledigt;

    /** nullable persistent field */
    private com.lp.server.benutzer.fastlanereader.generated.FLRNachrichtart flrnachrichtart;

    /** full constructor */
    public FLRNachrichtarchiv(String c_nachricht, String c_erledigungsgrund, Date t_zeit, Date t_erledigt, Date t_bearbeitung, FLRPersonal flrpersonal_anlegen, FLRPersonal flrpersonal_bearbeiter, FLRPersonal flrpersonal_erledigt, com.lp.server.benutzer.fastlanereader.generated.FLRNachrichtart flrnachrichtart) {
        this.c_nachricht = c_nachricht;
        this.c_erledigungsgrund = c_erledigungsgrund;
        this.t_zeit = t_zeit;
        this.t_erledigt = t_erledigt;
        this.t_bearbeitung = t_bearbeitung;
        this.flrpersonal_anlegen = flrpersonal_anlegen;
        this.flrpersonal_bearbeiter = flrpersonal_bearbeiter;
        this.flrpersonal_erledigt = flrpersonal_erledigt;
        this.flrnachrichtart = flrnachrichtart;
    }

    /** default constructor */
    public FLRNachrichtarchiv() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nachricht() {
        return this.c_nachricht;
    }

    public void setC_nachricht(String c_nachricht) {
        this.c_nachricht = c_nachricht;
    }

    public String getC_erledigungsgrund() {
        return this.c_erledigungsgrund;
    }

    public void setC_erledigungsgrund(String c_erledigungsgrund) {
        this.c_erledigungsgrund = c_erledigungsgrund;
    }

    public Date getT_zeit() {
        return this.t_zeit;
    }

    public void setT_zeit(Date t_zeit) {
        this.t_zeit = t_zeit;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public Date getT_bearbeitung() {
        return this.t_bearbeitung;
    }

    public void setT_bearbeitung(Date t_bearbeitung) {
        this.t_bearbeitung = t_bearbeitung;
    }

    public FLRPersonal getFlrpersonal_anlegen() {
        return this.flrpersonal_anlegen;
    }

    public void setFlrpersonal_anlegen(FLRPersonal flrpersonal_anlegen) {
        this.flrpersonal_anlegen = flrpersonal_anlegen;
    }

    public FLRPersonal getFlrpersonal_bearbeiter() {
        return this.flrpersonal_bearbeiter;
    }

    public void setFlrpersonal_bearbeiter(FLRPersonal flrpersonal_bearbeiter) {
        this.flrpersonal_bearbeiter = flrpersonal_bearbeiter;
    }

    public FLRPersonal getFlrpersonal_erledigt() {
        return this.flrpersonal_erledigt;
    }

    public void setFlrpersonal_erledigt(FLRPersonal flrpersonal_erledigt) {
        this.flrpersonal_erledigt = flrpersonal_erledigt;
    }

    public com.lp.server.benutzer.fastlanereader.generated.FLRNachrichtart getFlrnachrichtart() {
        return this.flrnachrichtart;
    }

    public void setFlrnachrichtart(com.lp.server.benutzer.fastlanereader.generated.FLRNachrichtart flrnachrichtart) {
        this.flrnachrichtart = flrnachrichtart;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
