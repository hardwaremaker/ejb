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
package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLohnartstundenfaktor implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Double f_faktor;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRLohnart flrlohnart;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRLohnstundenart flrlohnstundenart;

    /** full constructor */
    public FLRLohnartstundenfaktor(Double f_faktor, com.lp.server.personal.fastlanereader.generated.FLRLohnart flrlohnart, com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart, com.lp.server.personal.fastlanereader.generated.FLRLohnstundenart flrlohnstundenart) {
        this.f_faktor = f_faktor;
        this.flrlohnart = flrlohnart;
        this.flrtagesart = flrtagesart;
        this.flrlohnstundenart = flrlohnstundenart;
    }

    /** default constructor */
    public FLRLohnartstundenfaktor() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Double getF_faktor() {
        return this.f_faktor;
    }

    public void setF_faktor(Double f_faktor) {
        this.f_faktor = f_faktor;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRLohnart getFlrlohnart() {
        return this.flrlohnart;
    }

    public void setFlrlohnart(com.lp.server.personal.fastlanereader.generated.FLRLohnart flrlohnart) {
        this.flrlohnart = flrlohnart;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTagesart getFlrtagesart() {
        return this.flrtagesart;
    }

    public void setFlrtagesart(com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart) {
        this.flrtagesart = flrtagesart;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRLohnstundenart getFlrlohnstundenart() {
        return this.flrlohnstundenart;
    }

    public void setFlrlohnstundenart(com.lp.server.personal.fastlanereader.generated.FLRLohnstundenart flrlohnstundenart) {
        this.flrlohnstundenart = flrlohnstundenart;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
