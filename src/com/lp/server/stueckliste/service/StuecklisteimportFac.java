
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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Remote;

import com.lp.server.system.service.ImportErroInfo;
import com.lp.server.system.service.TheClientDto;

@Remote
public interface StuecklisteimportFac {

	public String pruefeUndImportiereArbeitsplanXLS(byte[] xlsDatei, String einheitStueckRuestZeit,
			boolean bImportierenWennKeinFehler, boolean bVorhandenePositionenLoeschen, TheClientDto theClientDto);

	public ImportErroInfo pruefeUndImportiereMaterialXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			boolean bVorhandenePositionenLoeschen, String einheitStueckRuestZeit, TheClientDto theClientDto);

	public String pruefeUndImportierePruefkombinationXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			TheClientDto theClientDto);

	public String pruefeUndImportierePruefplanXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			boolean bVorhandenePositionenLoeschen, TheClientDto theClientDto);

	public String pruefeUndImportiereProFirst(Integer stuecklisteIId, String kundeKbez, TheClientDto theClientDto);

	public String pruefeUndImportiereProFirst(Integer stuecklisteIId, String kundeKbez, boolean bNurVKPreisUpdaten,
			TheClientDto theClientDto);

	public void proFirstImportZeilenweise(StuecklisteDto stklDtoVorhanden, TheClientDto theClientDto,
			Integer montageartIId, String stklFremdsystemnummer, String stklArtikelnummer, String kundeKbez,
			String kundeFremdsystemnummer, String artikelnummerRohmaterial, String kommentardatei,
			String bezRohmaterial, String farbe, String material, Double dicke, Double gewicht, String schneidlaenge,
			Float breite, Float tiefe, String artikelnummerAZArtikel, String maschineNr, Double stueckzeit,
			String parameterKommentarPfad, Integer artikelkommentarart, String verpackungsart,
			String stklZusatzbezeichnung, String zusatzbezeichnung2, String bauform, String nettovkpreis,
			String referenznummer, String kundenartikelnummer, String anarbeitungsmenge, byte[] image,
			Integer artgruIId, String stklArtikelnummerOriginal, boolean bNurVKPreisUpdaten) throws RemoteException;

	public Integer kommentarartProFirstAnlegen(TheClientDto theClientDto);

	public void ignoriereKundeBeiProfirstImport(String kbez);

	public ArrayList<String> getAllProFirstIgnore();

	public boolean wirdKundeVonProFirstIgnoriert(String kbez);

	public void removeProFirstIgnore(String kbez);
	
	public ImportErroInfo pruefeUndImportiereCreoXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			boolean bVorhandenePositionenLoeschen, TheClientDto theClientDto);

}
