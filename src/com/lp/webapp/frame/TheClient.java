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
package com.lp.webapp.frame;

import java.net.URL;

import com.lp.server.system.service.TheClientDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2011/07/06 06:17:28 $
 */
public class TheClient {
	private String userName = null;
	private String sMsg = null;
	private String idSession = null;
	private String sContextPath = null;
	private boolean bResponseIsReady = false;
	private String sServletPath = null;
	private URL uRLClient = null;
	private Object data = null;
	private TheClientDto theClientDto=null;

	public TheClientDto getTheClientDto() {
		return theClientDto;
	}

	public void setTheClientDto(TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
	}

	public TheClient() {
		// nothing here
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setSMsg(String sMsg) {
		this.sMsg = sMsg;
	}

	public void setIdSession(String idSession) {
		this.idSession = idSession;
	}

	public void setSContextPath(String sContextPath) {
		this.sContextPath = sContextPath;
	}

	public void setSServletPath(String sServletPath) {
		this.sServletPath = sServletPath;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setBResponseIsReady(boolean bResponseIsReady) {
		this.bResponseIsReady = bResponseIsReady;
	}

	public void setSURLClient(URL sURLClient) {
		this.uRLClient = sURLClient;
	}

	public String getUserName() {
		return userName;
	}

	public String getSMsg() {
		return sMsg;
	}

	public String getIdSession() {
		return idSession;
	}

	public String getSContextPath() {
		return sContextPath;
	}

	public String getSContextServletPath() {
		return sContextPath + sServletPath;
	}

	public String getSServletPath() {
		return sServletPath;
	}

	public Object getData() {
		return data;
	}

	public boolean isBResponseIsReady() {
		return bResponseIsReady;
	}

	public URL getSURLClient() {
		return uRLClient;
	}

}
