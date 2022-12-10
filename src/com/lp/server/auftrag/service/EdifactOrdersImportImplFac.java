package com.lp.server.auftrag.service;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Local;

import com.lp.server.partner.ejb.KundeKennung;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HvOptional;
import com.lp.service.edifact.OrdersRepository;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.EdifactMessage;

@Local
public interface EdifactOrdersImportImplFac {

	EdifactOrdersImportResult importOrdersMsg(
			String content, EdifactMessage msg, OrdersRepository repository,
			TheClientDto theClientDto) throws RemoteException;

	void reportEdifactException(String edifactContent,
			EdifactException ex, TheClientDto theClientDto);

	void reportApplicationException(String edifactContent, 
			Exception ex, TheClientDto theClientDto);

	/**
	 * Aus eine EDI-Inhalt die Kennung der NAD+<Qualifier> auslesen
	 * 
	 * <p>Es wird davon ausgegangen, dass es sich bei diesem ediContent um
	 * einen bereits validierten Inhalt handel (sprich: Wir haben daraus
	 * schon mal einen Auftrag erstellen koennen.</p>
	 * 
	 * <p>Es wird die <b>erste</b> Party-Info ausgelesen</p>
	 *  
	 * @param ediContent
	 * @param nadPartyQualifier zum Beispiel "DP", "IV" oder "BY"
	 * @return die - falls vorhanden - Party id. identification
	 */
	HvOptional<String> extractNADPartyIdentification(String ediContent, String nadPartyQualifier);

	/**
	 * Aus unseren Kennungen jene heraussuchen, die tatsaechlich passt
	 * 
	 * <p>Wir haben fuer einen Kunden mehrere definiert, jenen Kunden suchen, der 
	 * tatsaechlich gemeint war. Unsere Kennung kann kuerzer sein - weil die zusaetzlichen
	 * Informationen wie zum Beispiel welcher Codetype diese Id war - fehlt.
	 * "123456" (unsere) vs. "123456::92" oder auch "123456::". </p>
	 * 
	 * @param kks
	 * @param ediIdString
	 * @return
	 */
	KundeKennung filterOursWithEdiId(List<KundeKennung> kks, String ediIdString);
}
