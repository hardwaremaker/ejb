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
package com.lp.server.system.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.finanz.service.UstWarnungDto;

public class BelegPruefungDto implements Serializable {
	private static final long serialVersionUID = 8029498872525899814L;
	
	private Integer belegId ;
	private Integer kundeId ;
	private boolean kundeHatUstAberNichtUstPositionen ;
	private boolean kundeHatKeineUstAberUstPositionen ;
	private Timestamp berechnungsZeitpunkt ;
	private List<RePosInfo> reposInfos;
	private List<UstWarnungDto> ustWarnungDtos;
	
	public BelegPruefungDto(Integer belegId) {
		this(belegId, null); 
	}
	
	public BelegPruefungDto(Integer belegId, Integer kundeId) {
		this.belegId = belegId ;
		this.kundeId = kundeId ;
		this.reposInfos = new ArrayList<RePosInfo>();
		ustWarnungDtos = new ArrayList<UstWarnungDto>();
	}
	
	public Integer getBelegId() {
		return belegId;
	}
	public void setBelegId(Integer belegId) {
		this.belegId = belegId;
	}
	public Integer getKundeId() {
		return kundeId;
	}
	public void setKundeId(Integer kundeId) {
		this.kundeId = kundeId;
	}
	public boolean isKundeHatUstAberNichtUstPositionen() {
		return kundeHatUstAberNichtUstPositionen;
	}
	public void setKundeHatUstAberNichtUstPositionen(
			boolean kundeHatUstAberNichtUstPositionen) {
		this.kundeHatUstAberNichtUstPositionen = kundeHatUstAberNichtUstPositionen;
	}
	public boolean isKundeHatKeineUstAberUstPositionen() {
		return kundeHatKeineUstAberUstPositionen;
	}
	public void setKundeHatKeineUstAberUstPositionen(
			boolean kundeHatKeineUstAberUstPositionen) {
		this.kundeHatKeineUstAberUstPositionen = kundeHatKeineUstAberUstPositionen;
	}
	public Timestamp getBerechnungsZeitpunkt() {
		return berechnungsZeitpunkt;
	}
	public void setBerechnungsZeitpunkt(Timestamp berechnungsZeitpunkt) {
		this.berechnungsZeitpunkt = berechnungsZeitpunkt;
	}
	
	public void addZeroPriceRePosInfo(RePosInfo reposInfo) {
		reposInfos.add(reposInfo);
	}
	 
	public List<RePosInfo> getZeroPriceRePosInfos() {
		return reposInfos;
	}
	
	public void addUstWarnung(UstWarnungDto ustWarnungDto) {
		ustWarnungDtos.add(ustWarnungDto);
	}
	
	public List<UstWarnungDto> getUstWarnungDtos() {
		return ustWarnungDtos;
	}
	
	public class RePosInfo implements Serializable {
		private static final long serialVersionUID = -7280647300318336488L;
		private Integer reposId;
		private Integer reposNr;
		private String  lsCnr;
		private List<Integer> lsposIds;
		private List<Integer> lsposNrs;
		
		public RePosInfo(Integer reposId, Integer reposNr) {
			this.reposId = reposId;
			this.reposNr = reposNr;
			this.lsposIds = new ArrayList<Integer>();
			this.lsposNrs = new ArrayList<Integer>();
		}
		
		public RePosInfo(Integer reposId, String lsCnr) {
			this.reposId = reposId;
			this.lsCnr = lsCnr;
			this.lsposIds = new ArrayList<Integer>();
			this.lsposNrs = new ArrayList<Integer>();
		}
		
		public Integer getReposId() {
			return reposId;
		}
		
		public Integer getReposNr() {
			return reposNr;
		}
		
		public void setLsCnr(String lsCnr) {
			this.lsCnr = lsCnr;
		}
		
		public String getLsCnr() {
			return lsCnr;
		}
		
		public List<Integer> getLsposIds() {
			return lsposIds;
		}
		
		public List<Integer> getLsposNrs() {
			return lsposNrs;
		}
		
		public void addLsposId(Integer lsposId, Integer lsposNr) {
			lsposIds.add(lsposId);
			lsposNrs.add(lsposNr);
		}
	}
}
