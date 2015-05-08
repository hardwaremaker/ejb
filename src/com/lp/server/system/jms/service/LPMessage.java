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
package com.lp.server.system.jms.service;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>
 * jmsqueue: Diese Klasse ist die Oberklasse jeder LP-Message.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; 25.06.06
 * </p>
 * 
 * <p>
 * @author $Author: adi $
 * </p>
 * 
 * @version not attributable Date $Date: 2010/06/21 13:26:36 $
 */
public class LPMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// System 1000-1999
	static final public int IIDELETE_THE_CLIENT_LOGGED_IN = 1000;

	private String sSender2stelligesModulKuerzel = null;
	private String sUser = null;
	private int iiWhatIWant = 0;
	private Boolean bbJobPositivProcessed = null;
	private HashMap<Object, Object> keyValuePairs = null;

	public LPMessage() {
		keyValuePairs = new HashMap<Object, Object>();
	}

	public LPMessage(String sSender2stelligesModulKuerzelI, String sUserI,
			int iiWhatIWantI) {

		this();

		sSender2stelligesModulKuerzel = sSender2stelligesModulKuerzelI;
		sUser = sUserI;
		iiWhatIWant = iiWhatIWantI;
	}

	public void setSSender2stelligesModulKuerzel(
			String sSender2stelligesModulKuerzelI) {
		this.sSender2stelligesModulKuerzel = sSender2stelligesModulKuerzelI;
	}

	public void setSUser(String sUser) {
		this.sUser = sUser;
	}

	public void setValue(Object sKeyI, Object oValueI) {
		keyValuePairs.put(sKeyI, oValueI);
	}

	public Object getValue(Object sKeyI) {
		return keyValuePairs.get(sKeyI);
	}

	public Object getKeySet() {
		return keyValuePairs.keySet();
	}

	public void setIiWhatIWant(int iiWhatIWant) {
		this.iiWhatIWant = iiWhatIWant;
	}

	public void setBbJobPositivProcessed(Boolean bbJobPositivProcessed) {
		this.bbJobPositivProcessed = bbJobPositivProcessed;
	}

	public String getSSender2stelligesModulKuerzel() {
		return sSender2stelligesModulKuerzel;
	}

	public String getSUser() {
		return sUser;
	}

	public int getIiWhatIWant() {
		return iiWhatIWant;
	}

	public Boolean getBbJobPositivProcessed() {
		return bbJobPositivProcessed;
	}
}
