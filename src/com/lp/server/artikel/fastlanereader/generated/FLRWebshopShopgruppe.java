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
package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Made handcrafted and with love */
public class FLRWebshopShopgruppe implements Serializable {
 	private static final long serialVersionUID = -862916102901689972L;

	/** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer shopgruppe_i_id;
    
    /** nullable persistent field */
    private Integer webshop_i_id;

    private FLRShopgruppe flrshopgruppe;
    
    private FLRWebshop flrwebshop;
    
   
    /** full constructor */
    public FLRWebshopShopgruppe(Integer i_id, Integer shopgruppe_i_id, Integer webshop_i_id) {
        this.i_id = i_id;
        setShopgruppe_i_id(shopgruppe_i_id);
        setWebshop_i_id(webshop_i_id);
    }

    /** default constructor */
    public FLRWebshopShopgruppe() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getShopgruppe_i_id() {
        return this.shopgruppe_i_id;
    }

    public void setShopgruppe_i_id(Integer shopgruppe_i_id) {
        this.shopgruppe_i_id = shopgruppe_i_id;
    }
    
    public Integer getWebshop_i_id() {
        return this.webshop_i_id;
    }

    public void setWebshop_i_id(Integer webshop_i_id) {
        this.webshop_i_id = webshop_i_id;
    }
 
    public void setFlrshopgruppe(FLRShopgruppe flrshopgruppe) {
    	this.flrshopgruppe = flrshopgruppe;
    }
    
    public FLRShopgruppe getFlrshopgruppe() {
    	return flrshopgruppe;
    }
    
    public void setFlrwebshop(FLRWebshop flrwebshop) {
    	this.flrwebshop = flrwebshop;
    }
    
    public FLRWebshop getFlrwebshop() {
    	return flrwebshop;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }
}
