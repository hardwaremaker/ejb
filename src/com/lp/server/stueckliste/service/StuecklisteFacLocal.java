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
package com.lp.server.stueckliste.service;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.system.service.TheClientDto;

@Local
public interface StuecklisteFacLocal {
	StrukturDatenParamDto getStrukturDatenEinerStuecklisteMitArbeitsplanNew(
			Integer stuecklisteId, StrukturDatenParamDto paramDto,
			TheClientDto theClientDto);
	
	List<FLRStuecklisteposition> calculatePositions(
			Integer stuecklisteId, StrukturDatenParamDto paramDto,
			List<FLRStuecklisteposition> positions, TheClientDto theClientDto);
	
	List<FLRStuecklistearbeitsplan> calculateArbeitsplan(
			Integer stuecklisteId, StrukturDatenParamDto paramDto,
			List<FLRStuecklistearbeitsplan> positions, TheClientDto theClientDto);
	
	StuecklisteAufgeloest getStrukturdatenEinesArtikelsStrukturiert(
			Integer artikelIIdI, boolean bNurZuDruckendeI,
			int iStuecklistenaufloesungTiefeI,
			boolean bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
			BigDecimal nWievieleEinheitenDerUnterpositionInStuecklisteI,
			boolean bFremdfertigungAufloesenI, boolean bUeberAlleMandanten,
			TheClientDto theClientDto);

	StuecklisteAufgeloest getStrukturdatenEinesArtikelsStrukturiert(
			Integer artikelIIdI, boolean bNurZuDruckendeI,
			int iStuecklistenaufloesungTiefeI,
			boolean bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
			BigDecimal nWievieleEinheitenDerUnterpositionInStuecklisteI,
			boolean bFremdfertigungAufloesenI, boolean bUeberAlleMandanten,
			double dWBZWennNichtDefiniertInTagen, TheClientDto theClientDto);

	StuecklisteAufgeloest getStrukturdatenEinesArtikelsStrukturiert(
			Integer artikelIIdI, boolean bNurZuDruckendeI,
			int iStuecklistenaufloesungTiefeI,
			boolean bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
			BigDecimal nWievieleEinheitenDerUnterpositionInStuecklisteI, 
			boolean bFremdfertigungAufloesenI, boolean bUeberAlleMandanten, 
			double dWBZWennNichtDefiniertInTagen, StrukturDatenParamDto paramDto,
			TheClientDto theClientDto);
	
	public int STUECKLISTENAUFLOESUNGSTIEFE_BESTIMMT_LAGERSTANDSDETAIL = -2;
	public int STUECKLISTENAUFLOESUNGSTIEFE_UNBEGRENZT = -1;
	
}
