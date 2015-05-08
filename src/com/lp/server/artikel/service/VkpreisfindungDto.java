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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * <p>
 * Dieses Dto transportiert die Informationen aus Stufe 1, 2 und 3 der
 * VK-Preisfindung und kennzeichnet den berechneten und den nidriegsten
 * VK-Preis. Beispiel: Aufgrund der VK-Preisfindung wird der Preis aus Stufe 2
 * berechnet. Wuerde Stufe 3 aber ein niedrigeres Ergebnis liefern, so ist auch
 * diese Information fuer den Benutzer interessant.
 * <p>
 * Copyright Logistik Pur GmbH (c) 2006
 * </p>
 * <p>
 * Erstellungsdatum 11.07.2006
 * </p>
 * <p>
 * </p>
 * 
 * @author uli walch
 * @version 1.0
 */
public class VkpreisfindungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String VKPFPREISBASIS = "VK Preisbasis";
	public static String VKPFSTUFE1 = "VKPF Stufe 1";
	public static String VKPFSTUFE2 = "VKPF Stufe 2";
	public static String VKPFSTUFE3 = "VKPF Stufe 3";

	private Locale locale = null;


	private String vkpreisberechnetStufe = null;
	private String vkpreisminimalStufe = null;

	private VerkaufspreisDto vkpPreisbasis = null;
	private VerkaufspreisDto vkpStufe1 = null;
	private VerkaufspreisDto vkpStufe2 = null;
	private VerkaufspreisDto vkpStufe3 = null;

	private BigDecimal nMaterialzuschlag=new BigDecimal(0);
	

	public BigDecimal getNMaterialzuschlag() {
		return nMaterialzuschlag;
	}

	public void setNMaterialzuschlag(BigDecimal nMaterialzuschlag) {
		this.nMaterialzuschlag = nMaterialzuschlag;
	}

	public VkpreisfindungDto(Locale localeI) {
		locale = localeI;
	}

	/*
	 * public void initialize() { VKPFPREISBASIS =
	 * "VK Preisbasis";//HelperServer.getTextRespectUISpr("vkpf.preisbasis",
	 * locale); VKPFSTUFE1 =
	 * "VKPF Stufe 1";//HelperServer.getTextRespectUISpr("vkpf.stufe1", locale);
	 * VKPFSTUFE2 =
	 * "VKPF Stufe 2";//HelperServer.getTextRespectUISpr("vkpf.stufe2", locale);
	 * VKPFSTUFE3 =
	 * "VKPF Stufe 3";//HelperServer.getTextRespectUISpr("vkpf.stufe3", locale);
	 * }
	 */

	public String getVkpreisberechnetStufe() {
		return vkpreisberechnetStufe;
	}

	public void setVkpreisberechnetStufe(String vkpreisberechnetStufeI) {
		vkpreisberechnetStufe = vkpreisberechnetStufeI;
	}

	public String getVkpreisminimalStufe() {
		return vkpreisminimalStufe;
	}

	public void setVkpreisminimalStufe(String vkpreisminimalStufeI) {
		vkpreisminimalStufe = vkpreisminimalStufeI;
	}

	public VerkaufspreisDto getVkpPreisbasis() {
		return vkpPreisbasis;
	}

	public void setVkpPreisbasis(VerkaufspreisDto vkpPreisbasisI) {
		vkpPreisbasis = vkpPreisbasisI;
	}

	public VerkaufspreisDto getVkpStufe1() {
		return vkpStufe1;
	}

	public void setVkpStufe1(VerkaufspreisDto vkpStufe1I) {
		vkpStufe1 = vkpStufe1I;
	}

	public VerkaufspreisDto getVkpStufe2() {
		return vkpStufe2;
	}

	public void setVkpStufe2(VerkaufspreisDto vkpStufe2I) {
		vkpStufe2 = vkpStufe2I;
	}

	public VerkaufspreisDto getVkpStufe3() {
		return vkpStufe3;
	}

	public void setVkpStufe3(VerkaufspreisDto vkpStufe3I) {
		vkpStufe3 = vkpStufe3I;
	}
}
