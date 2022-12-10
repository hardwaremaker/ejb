package com.lp.server.rechnung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.ejb.Local;

import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.rechnung.service.CoinRoundingResult;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.BelegVerkaufDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;

@Local
public interface CoinRoundingServiceFac {
	CoinRoundingInfo createInfo(RechnungDto rechnungDto, TheClientDto theClientDto);
	CoinRoundingInfo createInfo(EingangsrechnungDto erDto, TheClientDto theClientDto);
	
	boolean isRoundingNeeded(RechnungDto rechnungDto, TheClientDto theClientDto);
	boolean isRoundingNeeded(EingangsrechnungDto erDto, TheClientDto theClientDto);
	
	/**
	 * Rundung eines Bruttobetrags anhand einer Eingangsrechnung durchf&uuml;hren</br>
	 * <p>Wenn im Land M&uuml;nzrundung aktiviert ist, wird gepr&uuml;ft, ob der 
	 * in erDto.getNBetrag() &uuml;bermittelte Betrag tats&auml;chlich rund ist. Ist
	 * er es nicht, wird keine weitere Rundung durchgef&uuml;hrt.</p>
	 * <p>Bei aktiver Rundung in diesem Land wird bei Bedarf auch der Steuerbetrag
	 * gerundet. Dies bedeutet in diesem Falle, dass der Nettobetrag entsprechend 
	 * angepasst wird.</p>
	 * <p>Eine Rundung findet nur statt, wenn die Belegw&auml;hrung der Mandantenw&auml;hrung
	 * entspricht (und im Land M&uuml;nzrundung aktiviert ist.</p>
	 * @param erDto
	 * @param theClientDto
	 * @return enth&auml;t den ermittelten Steuerbetrag (result.getTaxAmount()) und ein
	 *   Kennzeichen, ob der Bruttobetrag den Eingabebedingungen des Landes entspricht. Bei
	 *   M&uuml;nzrundung muss der Bruttobetrag bereits gerundet vorliegen
	 */
	CoinRoundingResult calcMwstBetragFromBrutto(EingangsrechnungDto erDto, TheClientDto theClientDto);
	
	/**
	 * Rundung eines Nettobetrags anhand einer Eingangsrechnung durchf&uuml;hren</br>
	 * <p>Ist die Rundung aktiv, wird der sich ergebende Bruttobetrag gerundet. Dabei wird
	 * erDto.getNBetrag als Nettobetrag angesehen.</p>
	 * <p>Der Steuerbetrag wird ebenfalls gerundet, sofern &uuml;ber die Landeseinstellungen
	 * gefordert</p>
	 * <p>Eine Rundung findet nur statt, wenn die Belegw&auml;hrung der Mandantenw&auml;hrung
	 * entspricht (und im Land M&uuml;nzrundung aktiviert ist.</p>
	 * @param erDto
	 * @param theClientDto
	 * @return enth&auml;lt den (gerundeten) Steuerbetrag (result.getTaxAmount()) und den 
	 *  ermittelten (gerundeten) BruttoBetrag (result.getBruttoAmount())
	 */
	CoinRoundingResult calcMwstBetragFromNetto(EingangsrechnungDto erDto, TheClientDto theClientDto);
	
	/**
	 * Eine Rechnung runden - falls notwendig</br>
	 * <p> Per Definition werden Anzahlungsrechnungen nicht gerundet.
	 * </p>
	 * @param rechnungDto
	 * @param posDtos
	 * @param theClientDto
	 * @throws EJBExceptionLP
	 */
	void round(RechnungDto rechnungDto, BelegpositionVerkaufDto[] posDtos, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;
	
	 void createRoundingPositionForAuftrag(AuftragDto auftragDto, BigDecimal diff, 
				TheClientDto theClientDto) throws RemoteException;
	
	/**
	 * Eine etwaige Rundungsposition aus der Rechnung entfernen</br>
	 * <p>Es wird nur die erste Rundungsposition entfernt. Das heisst, wenn der 
	 * Anwender selbst eine Position mit dem Rundungsartikel angelegt hat und somit
	 * mehr als eine Rundungsposition vorhanden ist, wird nur die erste (ISort) entfernt.
	 * </p>
	 * @param dtos
	 * @param theClientDto
	 * @return
	 * @throws RemoteException
	 */
	boolean removeRoundingPosition(BelegpositionVerkaufDto[] dtos, TheClientDto theClientDto) throws RemoteException;
	
	BigDecimal roundUst(BelegVerkaufDto belegDto, BigDecimal ust, TheClientDto theClientDto);

}
