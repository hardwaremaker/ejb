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
package com.lp.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.rechnung.service.RechnungPositionDto;

/**
 * Zur Verwaltung eines Artikelsets
 * 
 * Ein Artikelset (Setartikel) besteht aus dem Kopfartikel der nicht lagergef&uuml;hrt ist. 
 * Es muss ein Ident-Artikel sein (headItem)
 * Und aus den Positionen. Diese k&ouml;nnen alle Positionsarten annehmen die HeliumV 
 * unterst&uuml;tzt.
 * 
 * @author Gerold
 */
public class Artikelset implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4505276869758042897L;

	private BelegpositionVerkaufDto headItem ;
	
	private List<BelegpositionVerkaufDto> positions = new ArrayList<BelegpositionVerkaufDto>() ;
	
	private List<SeriennrChargennrMitMengeDto> identities = new ArrayList<SeriennrChargennrMitMengeDto>() ;
	
	private BigDecimal availableAmount ;
	private BigDecimal slipAmount ;
	private BigDecimal previousSlipAmount ;
	
	public Artikelset(AuftragpositionDto headItem, AuftragpositionDto[] positions) {
		this.headItem = headItem ;
		for (AuftragpositionDto belegpositionVerkaufDto : positions) {
			this.positions.add(belegpositionVerkaufDto) ;
			setupIdentitiesFrom(belegpositionVerkaufDto) ;
		}
		slipAmount = headItem.getNOffeneMenge() ;
	}

	public Artikelset(AuftragpositionDto headItem, List<AuftragpositionDto> positions) {
		this.headItem = headItem ;
		for (AuftragpositionDto belegpositionVerkaufDto : positions) {
			this.positions.add(belegpositionVerkaufDto) ;
			setupIdentitiesFrom(belegpositionVerkaufDto) ;
		}
		slipAmount = headItem.getNOffeneMenge() ;
	}

	public Artikelset(AuftragpositionDto headItem) {
		this.headItem = headItem ;
		slipAmount = headItem.getNOffeneMenge() ;
	}

	public Artikelset(List<BelegpositionVerkaufDto> positions) {
		if(positions.size() < 1) throw new IllegalArgumentException("positions.size < 1") ;
		this.headItem = positions.get(0) ;
		for(int i = 1; i < positions.size(); i++) {
			BelegpositionVerkaufDto belegpositionDto = positions.get(i) ;
			this.positions.add(belegpositionDto) ;
			setupIdentitiesFrom(belegpositionDto) ;
		}
	}
	
	public Artikelset(BelegpositionVerkaufDto headItem, RechnungPositionDto[] allPositions) {
		this.headItem = headItem ;
		for (RechnungPositionDto belegpositionVerkaufDto : allPositions) {
			this.positions.add(belegpositionVerkaufDto) ;
			setupIdentitiesFrom(belegpositionVerkaufDto) ;
		}
	}
	
	public void addPosition(BelegpositionVerkaufDto position) {
		positions.add(position) ;
	}
	
	public BelegpositionVerkaufDto getHead() {
		return headItem ;
	}
	
	public List<BelegpositionVerkaufDto> getPositions() {
		return positions ;
	}
	
	public BelegpositionVerkaufDto[] getAllPositions() {
		BelegpositionVerkaufDto[] allPositions = new BelegpositionVerkaufDto[1 + positions.size()] ;
		allPositions[0] = headItem ;
		int startIndex = 0 ;
		for (BelegpositionVerkaufDto belegVerkaufDto : positions) {
			allPositions[++startIndex] = belegVerkaufDto ;
		}
		return allPositions ;
	}

	public List<SeriennrChargennrMitMengeDto> getIdentities() {
		return identities;
	}

	public void setIdentities(List<SeriennrChargennrMitMengeDto> identities) {
		this.identities = identities;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount == null ? getSlipAmount() : availableAmount ;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}
	
	
	public BigDecimal getSlipAmount() {
		return slipAmount ;
	}
	
	public void setSlipAmount(BigDecimal slipAmount) {
		this.slipAmount = slipAmount ;
	}
	
	
	public BigDecimal getPreviousSlipAmount() {
		return previousSlipAmount ;
	}
	
	public void setPreviousSlipAmount(BigDecimal slipAmount) {
		this.previousSlipAmount = slipAmount ;
	}
	
	
	/**
	 * Findet die Kopfposition mittels beliebiger Auftragposition
	 * 
	 * @param anyPosition ist die Auftragposition fuer die die Kopfposition gesucht werden soll
	 * @return null wenn anyPosition nicht Teil dieses Artikelsets ist, ansonsten die Kopfposition
	 */
	public BelegpositionVerkaufDto findHeadPositionDto(BelegpositionVerkaufDto anyPosition) {
		if(anyPosition.getPositioniIdArtikelset() == null) {
			if(headItem.getIId().equals(anyPosition.getPositioniIdArtikelset())) return headItem ;
		} else {
			for (BelegpositionVerkaufDto position : getPositions()) {
				if(position.getIId().equals(anyPosition.getIId())) return headItem ;
			}
		}
		
		return null ;
	}
	
	private void setupIdentitiesFrom(BelegpositionVerkaufDto belegpositionDto) {
		if(null != belegpositionDto.getSeriennrChargennrMitMenge()) {
			for (SeriennrChargennrMitMengeDto snrDto : belegpositionDto.getSeriennrChargennrMitMenge()) {
				if((snrDto.getCSeriennrChargennr() != null) || 
					(snrDto.getAlGeraetesnr() != null && (snrDto.getAlGeraetesnr().size() > 0))) {
					identities.add(snrDto) ;
				}
			}
//			identities.addAll(belegpositionDto.getSeriennrChargennrMitMenge()) ;
		}
		
	}
}

