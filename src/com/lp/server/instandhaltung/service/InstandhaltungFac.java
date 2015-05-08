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
package com.lp.server.instandhaltung.service;

import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;

@Remote
public interface InstandhaltungFac {
	public InstandhaltungDto instandhaltungFindByPrimaryKey(Integer iId);

	public Integer createWartungsschritte(WartungsschritteDto dto,
			TheClientDto theClientDto);

	public WartungsschritteDto wartungsschritteFindByPrimaryKey(Integer iId);

	public void removeInstandhaltung(InstandhaltungDto dto);

	public void updateInstandhaltung(InstandhaltungDto dto);

	public Integer createInstandhaltung(InstandhaltungDto dto);

	public void removeHalle(HalleDto dto);

	public void updateHalle(HalleDto dto);

	public Integer createHalle(HalleDto dto);

	public HalleDto halleFindByPrimaryKey(Integer iId);

	public void removeStandort(StandortDto dto);

	public StandortDto standortFindByPrimaryKey(Integer iId);

	public void updateStandort(StandortDto dto);

	public Integer createStandort(StandortDto dto);

	public Integer createAnlage(AnlageDto dto);

	public void updateAnlage(AnlageDto dto);

	public void removeAnlage(AnlageDto dto);

	public AnlageDto anlageFindByPrimaryKey(Integer iId);

	public Integer createGeraetetyp(GeraetetypDto dto);

	public GeraetetypDto geraetetypFindByPrimaryKey(Integer iId);

	public void updateGeraetetyp(GeraetetypDto dto);

	public void removeGeraetetyp(GeraetetypDto dto);

	public void updateIsmaschine(IsmaschineDto dto);

	public Integer createIsmaschine(IsmaschineDto dto);

	public IsmaschineDto ismaschineFindByPrimaryKey(Integer iId);

	public IskategorieDto iskategorieFindByPrimaryKey(Integer iId);

	public void removeIsmaschine(IsmaschineDto dto);

	public void updateGeraet(GeraetDto dto);

	public GeraetDto geraetFindByPrimaryKey(Integer iId);

	public Integer createGeraet(GeraetDto dto);

	public void removeGeraet(GeraetDto dto);

	public void updateWartungsliste(WartungslisteDto dto,
			TheClientDto theClientDto);

	public WartungslisteDto[] wartungslisteFindByGeraetIId(Integer geratIId);

	public WartungslisteDto wartungslisteFindByPrimaryKey(Integer iId);

	public Integer createWartungsliste(WartungslisteDto dto,
			TheClientDto theClientDto);

	public void removeWartungsliste(WartungslisteDto dto);

	public void removeIskategorie(IskategorieDto dto);

	public IskategorieDto geraeteloseFindByPrimaryKey(Integer iId);

	public Integer createIskategorie(IskategorieDto dto);

	public void updateIskategorie(IskategorieDto dto);

	public void vertauscheWartungsliste(Integer iIdPosition1I,
			Integer iIdPosition2I);

	public void sortierungWartungslisteAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer geraetIId, int iSortierungNeuePositionI);

	public void sortierungWartungsschritteAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer geraetIId, int iSortierungNeuePositionI);

	public void vertauscheWartungsschritte(Integer iIdPosition1I,
			Integer iIdPosition2I);

	public void updateWartungsschritte(WartungsschritteDto dto,
			TheClientDto theClientDto);

	public void removeWartungsschritte(WartungsschritteDto dto);

	public Map getAllKategorieren(TheClientDto theClientDto);

	public Integer createGeraetehistorie(GeraetehistorieDto dto, TheClientDto theClientDto);

	public void updateGeraetehistorie(GeraetehistorieDto dto, TheClientDto theClientDto);

	public GeraetehistorieDto geraetehistorieFindByPrimaryKey(Integer iId);

	public void removeGeraetehistorie(GeraetehistorieDto dto);
	public GeraetehistorieDto[] geraetehistorieFindByGeraetIIdPersonalIIdTechnikerTWartung(
			Integer geraetIId, Integer personalIIdTechniker, java.sql.Timestamp tWartung);
	public void updateGewerk(GewerkDto dto);
	public Integer createGewerk(GewerkDto dto);
	public GewerkDto gewerkFindByPrimaryKey(Integer iId);
	public void removeGewerk(GewerkDto dto);
	
	public StandorttechnikerDto standorttechnikerFindByPrimaryKey(Integer iId);
	public void updateStandorttechniker(StandorttechnikerDto dto);
	public void removeStandorttechniker(StandorttechnikerDto dto);
	public Integer createStandorttechniker(StandorttechnikerDto dto,
			TheClientDto theClientDto);
}
