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
package com.lp.server.system.service;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ServerLocaleInfo implements Externalizable {

	private String country ;	
	private String language ;
	private String timezone ;
	private String timezoneID ;
	private Integer dstSavings ;
	
	public ServerLocaleInfo() {
		setCountry("<undefined>") ;
		setLanguage("<undefined>") ;
		setTimezone("<undefined>") ;
		setTimezoneID("<undefined>") ;
		setDSTSavings(null) ;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(country) ;
		out.writeUTF(language) ;
		out.writeUTF(timezone) ;
		out.writeUTF(timezoneID);
		out.writeInt(dstSavings);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		country = in.readUTF() ;
		language = in.readUTF() ;
		timezone = in.readUTF() ;
		timezoneID = in.readUTF();
		dstSavings = in.readInt();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	public void setDSTSavings(Integer dayLightSavings) {
		this.dstSavings = dayLightSavings;
	}
	
	public Integer getDSTSavings() {
		return dstSavings;
	}
	
	public void setTimezoneID(String timezoneID) {
		this.timezoneID = timezoneID;
	}
	
	public String getTimezoneID() {
		return timezoneID;
	}
}
