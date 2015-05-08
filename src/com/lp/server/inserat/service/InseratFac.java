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
package com.lp.server.inserat.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.inserat.fastlanereader.generated.FLRInserat;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.TheClientDto;

@Remote
public interface InseratFac {

	public static final String FLR_INSERAT_STATUS_C_NR = "status_c_nr";
	public static final String FLR_INSERAT_FLRLIEFERANT = "flrlieferant";
	public static final String FLR_INSERAT_FLRARTIKEL_INSERATART = "flrartikel_inseratart";
	public static final String FLR_INSERAT_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_INSERAT_N_MENGE = "n_menge";
	public static final String FLR_INSERAT_N_NETTOEINZELPRESI_VK = "n_nettoeinzelpreis_vk";
	public static final String FLR_INSERAT_T_ERSCHIENEN = "t_erschienen";
	public static final String FLR_INSERAT_T_TERMIN = "t_termin";
	public static final String FLR_INSERAT_C_STICHWORT = "c_stichwort";
	public static final String FLR_INSERAT_FLRBESTELLPOSITION = "flrbestellposition";

	public static final String FLR_INSERATRECHNUNG_FLRKUNDE = "flrkunde";
	public static final String FLR_INSERATRECHNUNG_INSERAT_I_ID = "inserat_i_id";
	public static final String FLR_INSERATRECHNUNG_FLRRECHNUNGPOSITION = "flrrechnungposition";
	public static final String FLR_INSERATER_FLREINGANGSRECHNUNG = "flreingangsrechnung";
	public static final String FLR_INSERATER_EINGANGSRECHNUNG_I_ID = "eingangsrechnung_i_id";
	public static final String FLR_INSERATER_N_BETRAG = "n_betrag";
	public static final String FLR_INSERATER_C_TEXT = "c_text";
	public static final String FLR_INSERATER_FLRINSERAT = "flrinserat";

	public static final String FLR_INSERATARTIKEL_FLRARTIKEL = "flrartikel";
	public static final String FLR_INSERATARTIKEL_N_MENGE = "n_menge";
	public static final String FLR_INSERATARTIKEL_N_NETTOEINZELPREIS_EK = "n_nettoeinzelpreis_ek";
	public static final String FLR_INSERATARTIKEL_N_NETTOEINZELPREIS_VK = "n_nettoeinzelpreis_vk";
	public static final String FLR_INSERATARTIKEL_FLRBESTELLPOSITION = "flrbestellposition";

	public boolean istInseratInEinerRechnungEnthalten(Integer rechnungIId);

	public Integer createInserat(InseratDto oAuftragDtoI,
			TheClientDto theClientDto);

	
	public BigDecimal berechneWerbeabgabeLFEinesInserates(Integer inseratIId,
			TheClientDto theClientDto);
	
	public void storniereInserat(Integer inseratIId, TheClientDto theClientDto);

	public void updateInserat(InseratDto inseratDto, TheClientDto theClientDto);

	public InseratDto inseratFindByPrimaryKey(Integer iId);

	public void updateInseratrechnung(InseratrechnungDto inseratrechnungDto,
			TheClientDto theClientDto);

	public void removeInseratrechnung(Integer inseratrechnungIId);

	public Integer createInseratrechnung(InseratrechnungDto inseratrechnungDto,
			TheClientDto theClientDto);

	public void createInseratrechnungartikel(Integer inseratrechnungIId,
			RechnungPositionDto reposDtoZusatzArtikel);

	public InseratrechnungDto inseratrechnungFindByPrimaryKey(Integer iId);

	public void updateInseratOhneWeiterAktion(InseratDto inseratDto,
			TheClientDto theClientDto);

	public void vertauscheInseratrechnung(Integer inseratrechnungIId1,
			Integer inseratrechnungIId2);

	public void eingangsrechnungZuordnen(Integer inseratIId,
			Integer eingangsrechnungIId, BigDecimal nBetrag,
			TheClientDto theClientDto);

	public ArrayList<Integer> bestellungenAusloesen(Integer lieferantIId,
			Integer kundeIId, TheClientDto theClientDto);

	public int rechnungenAusloesen(Integer kundeIId, Integer inseratIId,java.sql.Date neuDatum,
			TheClientDto theClientDto);

	public InseraterDto[] inseraterFindByInseratIId(
			Integer inseratIId);
	
	public ArrayList<Integer> getAllLieferantIIdsAusInseratenOhneBestellung(
			Integer kundeIId, TheClientDto theClientDto);

	public ArrayList<Integer> getAllLieferantIIdsAusOffenenInseraten(
			TheClientDto theClientDto);

	public ArrayList<Integer> getAllKundeIIdsAusInseratenOhneBestellung(
			TheClientDto theClientDto);

	public void toggleErschienen(Integer inseratIId, TheClientDto theClientDto);

	public void toggleGestoppt(Integer inseratIId, String cBegruendung,
			TheClientDto theClientDto);

	public ArrayList eingangsrechnungsIIdsEinesInserates(Integer inseratIId);

	public void toggleVerrechenbar(Integer inseratIId, TheClientDto theClientDto);

	public void updateInseratartikel(InseratartikelDto inseratartikelDto,
			TheClientDto theClientDto);

	public InseratartikelDto inseratartikelFindByPrimaryKey(Integer iId);

	public Integer createInseratartikel(InseratartikelDto inseratartikelDto,
			TheClientDto theClientDto);

	public void removeInseratartikel(Integer inseratartikelIId);

	public InseratartikelDto[] inseratartikelFindByInseratIId(Integer inseratIId);

	public List<InseratDto> inseratFindByLieferantIId(Integer lieferantIId);
	
	public List<InseratDto> inseratFindByKundeIId(Integer kundeIId);

	public void beziehungZuRechnungspositionAufloesenUndRechnungspositionenLoeschen(
			Integer rechnungspositionIId, TheClientDto theClientDto);

	public InseratDto istIseratAufRechnungspositionVorhanden(
			Integer rechnungspositionIId);

	public void updateInserater(InseraterDto inseraterDto,
			TheClientDto theClientDto);

	public InseraterDto inseraterFindByPrimaryKey(Integer iId);

	public Integer createInserater(InseraterDto inseraterDto,
			TheClientDto theClientDto);

	public InseraterDto[] inseraterFindByEingangsrechnungIId(
			Integer eingangsrechnungIId);

	public void removeInserater(Integer inseraterIId, TheClientDto theClientDto);

	public InseratDto istInseratAufBestellpositionVorhanden(
			Integer bestellpositionIId);

	public void beziehungZuBestellpositionAufloesenUndBestellpositionenLoeschen(
			Integer bestellpositionIId, TheClientDto theClientDto);

	public ArrayList<RechnungDto> gibtEsNochWeitereRechnungenFuerdiesesInserat(
			Integer inseratIId, TheClientDto theClientDto);

	public void toggleManuellerledigt(Integer inseratIId,
			TheClientDto theClientDto);

	public int rechnungenAusloesenInserateMitMehrerenKunden(
			LinkedHashMap<Integer, FLRInserat> hmInserateMitMehrerenKunden,java.sql.Date neuDatum,
			TheClientDto theClientDto);

	public int rechnungenAusloesenInserateMitEinemKunden(
			LinkedHashMap<Integer, LinkedHashMap<Integer, FLRInserat>> hmInserateMitEinemKundenVerdichtet,java.sql.Date neuDatum,
			TheClientDto theClientDto);

	public BigDecimal getZuEingangsrechnungenZugeordnetenWert(
			Integer inseratIId, TheClientDto theClientDto);

	public void storniertAufheben(Integer inseratIId, TheClientDto theClientDto);
	public void inseratVertreterAendern(Integer inseratIId, Integer personalId_Vertreter,TheClientDto theClientDto);

}
