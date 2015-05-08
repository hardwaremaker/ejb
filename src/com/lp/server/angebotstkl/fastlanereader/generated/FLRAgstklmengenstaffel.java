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
package com.lp.server.angebotstkl.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAgstklmengenstaffel implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_materialeinsatz_lief1;

    /** nullable persistent field */
    private BigDecimal n_azeinsatz_lief1;

    /** nullable persistent field */
    private BigDecimal n_vkpreis;

    /** nullable persistent field */
    private BigDecimal n_vkpreis_gewaehlt;

    /** nullable persistent field */
    private Integer agstkl_i_id;

    /** nullable persistent field */
    private com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl;

    /** full constructor */
    public FLRAgstklmengenstaffel(BigDecimal n_menge, BigDecimal n_materialeinsatz_lief1, BigDecimal n_azeinsatz_lief1, BigDecimal n_vkpreis, BigDecimal n_vkpreis_gewaehlt, Integer agstkl_i_id, com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl) {
        this.n_menge = n_menge;
        this.n_materialeinsatz_lief1 = n_materialeinsatz_lief1;
        this.n_azeinsatz_lief1 = n_azeinsatz_lief1;
        this.n_vkpreis = n_vkpreis;
        this.n_vkpreis_gewaehlt = n_vkpreis_gewaehlt;
        this.agstkl_i_id = agstkl_i_id;
        this.flragstkl = flragstkl;
    }

    /** default constructor */
    public FLRAgstklmengenstaffel() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_materialeinsatz_lief1() {
        return this.n_materialeinsatz_lief1;
    }

    public void setN_materialeinsatz_lief1(BigDecimal n_materialeinsatz_lief1) {
        this.n_materialeinsatz_lief1 = n_materialeinsatz_lief1;
    }

    public BigDecimal getN_azeinsatz_lief1() {
        return this.n_azeinsatz_lief1;
    }

    public void setN_azeinsatz_lief1(BigDecimal n_azeinsatz_lief1) {
        this.n_azeinsatz_lief1 = n_azeinsatz_lief1;
    }

    public BigDecimal getN_vkpreis() {
        return this.n_vkpreis;
    }

    public void setN_vkpreis(BigDecimal n_vkpreis) {
        this.n_vkpreis = n_vkpreis;
    }

    public BigDecimal getN_vkpreis_gewaehlt() {
        return this.n_vkpreis_gewaehlt;
    }

    public void setN_vkpreis_gewaehlt(BigDecimal n_vkpreis_gewaehlt) {
        this.n_vkpreis_gewaehlt = n_vkpreis_gewaehlt;
    }

    public Integer getAgstkl_i_id() {
        return this.agstkl_i_id;
    }

    public void setAgstkl_i_id(Integer agstkl_i_id) {
        this.agstkl_i_id = agstkl_i_id;
    }

    public com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl getFlragstkl() {
        return this.flragstkl;
    }

    public void setFlragstkl(com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl) {
        this.flragstkl = flragstkl;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
