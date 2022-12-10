package com.lp.server.finanz.ejbfac;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.util.KontoId;
import com.lp.server.util.SachkontoId;

public class BuchungdetailFactory {

	/**
	 * Zu einem Array von BuchungdetailDtos neue aus einer Liste hinzugeben</br>
	 * <p>Dabei ein neues Array erstellen, dass genug Platz hat um die
	 * Summe der Elemente aufnehmen zu k&ouml;nnen.</br>
	 * 
	 * @param details des "originals"
	 * @param others die hinzuzuf&uuml;genden Elemente
	 * @return ein neues Array mit allen Elementen, ausser others enth&auml;lt
	 * keine Elemente, dann bleibt detail unver&auml;ndert
	 */
	public static BuchungdetailDto[] add(
			BuchungdetailDto[] details, List<BuchungdetailDto> others) {
		if(others.size() == 0) return details;
		
		BuchungdetailDto[] result = Arrays.copyOf(
				details, details.length + others.size());
		for(int i = 0; i < others.size(); i++) {
			result[i + details.length] = others.get(i);
		}
		
		return result;
	}
	
	/**
	 * Buchungssatz: Die f&uuml;r eine Anzahlung get&auml;tigte Zahlung
	 * enth&auml;lt Skonto.</br>
	 * <p>Wir buchen das so, dass das Skonto ein negatives HABEN ist.</p>
	 * <p>Hier wird die Buchung bei EU-Ausland und Drittland abgebildet.
	 * Es gibt keine Steuer. Nun ja, eigentlich schon, n&auml;lich dann,
	 * wenn es sich um einen Endkunden (B2C) handelt. Diesen Fall
	 * lassen wir aber vorerst weg.</p>
	 * <p>Eine Anzahlungszahlung wird als "Ist-Versteuerer" behandelt.
	 * Daher kann es keine USt.Reduktion wegen des Skontos geben. Es 
	 * wurde ja nur der Betrag bezahlt, der auch bezahlt wurde.</p>
	 * <p>Das ist im Gegensatz zu einer Soll-Versteuerungsbuchung, wo
	 * beim Verbuchen bereits die Steuerbuchung durchgef&uuml;hrt wurde
	 * und jetzt beim Skontobuchen tats&auml;chlich eine Reduktion der
	 * Steuerlast eintritt</p>
	 * 
	 * @param skontoBetrag der Bruttobetrag des Skontos, enth&auml;lt
	 *   also den skontoBetragUst
	 * @param skontoBetragUst der USt-Betrag des Skontos
	 * @param skontoKonto
	 * @param gegenKonto
	 * @return die Liste der Buchungsdetails um das Skonto-Konto und das
	 *   Gegenkonto (Anzahlungbezahlt) zu bef&uuml;llen
	 */
	public static List<BuchungdetailDto> skontoZahlungAnzahlung(
			BigDecimal skontoBetrag,
			SachkontoId skontoKonto, KontoId gegenKonto) {
		BuchungdetailDto b0 = BuchungdetailDto.haben(
				skontoKonto.id(), gegenKonto.id(),
				skontoBetrag.negate(),
				BigDecimal.ZERO);
		BuchungdetailDto b1 = BuchungdetailDto.haben(
				gegenKonto.id(), skontoKonto.id(),
				skontoBetrag, BigDecimal.ZERO);
		return Arrays.asList(b0, b1);
	}
}
