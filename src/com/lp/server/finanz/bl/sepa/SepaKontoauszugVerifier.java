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
package com.lp.server.finanz.bl.sepa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lp.server.finanz.service.SepaBetrag;
import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaHabenBetrag;
import com.lp.server.finanz.service.SepaImportTransformResult;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.finanz.service.SepaSaldo;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaImportExceptionLP;
import com.lp.util.Helper;

public abstract class SepaKontoauszugVerifier implements ISepaKontoauszugVerifier {
	
	private Comparator<SepaKontoauszug> elctrncSeqNbComparator;
	private Map<BigDecimal, List<SepaKontoauszug>> groups;
	private List<EJBSepaImportExceptionLP> messages;
	private String empfaengerIban;
	private ILPLogger myLogger;

	public SepaKontoauszugVerifier(String iban) {
		empfaengerIban = iban;
		myLogger = LPLogService.getInstance().getLogger(this.getClass());
		messages = new ArrayList<EJBSepaImportExceptionLP>();
		elctrncSeqNbComparator = new Comparator<SepaKontoauszug>() {
			@Override
			public int compare(SepaKontoauszug auszug1, SepaKontoauszug auszug2) {
				if (auszug1.getElektronischeAuszugsnr() == null || auszug2.getElektronischeAuszugsnr() == null) {
					return 0;
				}
				return auszug1.getElektronischeAuszugsnr().compareTo(auszug2.getElektronischeAuszugsnr());
			}
		};
	}

	@Override
	public SepaImportTransformResult groupAndVerify(SepaImportTransformResult result) {
		List<SepaKontoauszug> ktoauszuege = result.getKtoauszug();
		if (!verifyIban(ktoauszuege)) {
			result.getWarnings().addAll(messages);
			result.setKtoauszug(new ArrayList<SepaKontoauszug>());
			return result;
		}
		
		group(ktoauszuege);
		sort();
		
		if (!validateMandatoryFields()
				|| !verifyIndividual()) {
			result.getWarnings().addAll(messages);
			result.setKtoauszug(new ArrayList<SepaKontoauszug>());
			return result;
		}
		
		result.setKtoauszug(mapToList());
		result.getWarnings().addAll(messages);
		return result;
	}

	protected abstract boolean verifyIndividual();
	protected abstract boolean validateIndividualFields(SepaKontoauszug ktoauszug);

	/**
	 * Fuegt alle Kontoauszuege wieder in eine Liste zusammen.
	 * 
	 * @return Liste aller verifizierten Kontoauszuege
	 */
	private List<SepaKontoauszug> mapToList() {
		List<SepaKontoauszug> list = new ArrayList<SepaKontoauszug>();
		
		for (Map.Entry<BigDecimal, List<SepaKontoauszug>> entry : groups.entrySet()) {
			SepaKontoauszug ktoauszug = merge(entry.getValue());
			if (ktoauszug != null) list.add(ktoauszug);
		}

		return list;
	}

	private SepaKontoauszug merge(List<SepaKontoauszug> list) {
		if (list == null || list.isEmpty()) return null;
		if (list.size() == 1) return list.get(0);
		
		SepaKontoauszug mergedAuszug = list.get(0);
		SepaKontoauszug item = null;
		List<SepaBuchung> buchungen = new ArrayList<SepaBuchung>();
		for (Integer i = 1; i < list.size(); i++) {
			item = list.get(i);
			for (SepaBuchung buchung : item.getBuchungen()) {
				buchungen.add(buchung);
			}
		}
		List<SepaSaldo> salden = mergedAuszug.getSalden();
		salden.add(item.getEndSaldo());
		mergedAuszug.setSalden(salden);
		
		for (SepaBuchung buchung : buchungen) {
			mergedAuszug.addBuchung(buchung);
		}
		
		return mergedAuszug;
	}

	/**
	 * Teilt die Liste der Kontoauszuege in Gruppen mit der gleichen
	 * Auszug-ID auf
	 * 
	 * @param ktoauszuege
	 */
	private void group(List<SepaKontoauszug> ktoauszuege) {
		GroupManager groupManager = getGroupManager(empfaengerIban.substring(0, 2));
		groups = groupManager.group(ktoauszuege);
	}
	
	/**
	 * Sortiert die Kontoauszuege nach ihren Seitennummern.
	 */
	private void sort() {
		GroupManager groupManager = getGroupManager(empfaengerIban.substring(0, 2));
		groupManager.sort(groups);
	}

	/**
	 * Verifiziert die Seitenangaben der Kontoauszuege. Seitennummern muessen direkt
	 * aufeinanderfolgen. Die letzte Seite muss als solche (Flag) gekennzeichnet sein.
	 * Werden diese Bedingungen nicht erfuellt, kann dieser Kontoauszug nicht
	 * weiterverarbeitet werden.
	 */
	protected boolean verifyPageNumbers() {
		
		for (Map.Entry<BigDecimal, List<SepaKontoauszug>> entry : groups.entrySet()) {
			if (entry.getValue().size() == 1) {
				continue;
			}
			
			SepaKontoauszug auszug1, auszug2;
			Iterator<SepaKontoauszug> iterator = entry.getValue().iterator();
			auszug1 = iterator.next();
			while (iterator.hasNext()) {
				auszug2 = iterator.next();
				int auszug1Seitennummer = auszug1.getSeitennummer().intValue();
				int auszug2Seitennummer = auszug2.getSeitennummer().intValue();
				if ((auszug1Seitennummer + 1) == auszug2Seitennummer) {
					auszug1 = auszug2;
					if (!iterator.hasNext() && !auszug1.getbLetzteSeite()) {
						messages.add(new EJBSepaImportExceptionLP(
								EJBExceptionLP.FEHLER_SEPAIMPORT_ENDE_DES_KONTOAUSZUGS_NICHT_ERKANNT, 
								EJBSepaImportExceptionLP.SEVERITY_ERROR, entry.getKey()));
						myLogger.error("Letzte Seite des Kontoauszugs mit Auszugsnummer \"" 
								+ entry.getKey() + "\" ist nicht als solche gekennzeichnet.");
						return false;
					} else if (iterator.hasNext() && auszug1.getbLetzteSeite()) {
						messages.add(new EJBSepaImportExceptionLP(
								EJBExceptionLP.FEHLER_SEPAIMPORT_ENDE_DES_KONTOAUSZUGS_NICHT_ERKANNT, 
								EJBSepaImportExceptionLP.SEVERITY_ERROR, entry.getKey()));
						myLogger.error("Die letzte Seite in Kontoauszug mit Auszugsnummer \"" 
								+ entry.getKey() + "\" ist real nicht die letzte Seite.");
						return false;
					}
				} else {
					messages.add(new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_SEITENNUMMERN_INKONSISTENT, 
							EJBSepaImportExceptionLP.SEVERITY_ERROR, entry.getKey()));
					myLogger.error("Seitennummern des Kontoauszugs mit Message-ID \"" 
							+ entry.getKey() + "\" inkonsistent.");
					return false;
				}
			}
			
		}
		return true;
	}

	private boolean verifyIban(List<SepaKontoauszug> ktoauszuege) {
		
		for (SepaKontoauszug ktoauszug : ktoauszuege) {
			if (ktoauszug.getKontoInfo() != null && ktoauszug.getKontoInfo().getIban() != null) {
				if (!ktoauszug.getKontoInfo().getIban().trim().equals(empfaengerIban)) {
					myLogger.error("IBAN des Kontoauszugs mit Auszugs-ID \"" + ktoauszug.getAuszugsId() 
							+ "\" stimmt nicht mit der gewählten Bankverbindung überein.");
					messages.add(new EJBSepaImportExceptionLP(
							EJBExceptionLP.FEHLER_SEPAIMPORT_KTOINFO_NICHT_ALS_BANKVERBINDUNG_BEKANNT,
							EJBSepaImportExceptionLP.SEVERITY_ERROR, ktoauszug.getKontoInfo().getIban(), ktoauszug.getMessageId()));
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean validateMandatoryFields() {
		boolean isValidationOk = true;
		for (Map.Entry<BigDecimal, List<SepaKontoauszug>> entry : groups.entrySet()) {
			for (SepaKontoauszug ktoauszug : entry.getValue()) {
				if (!validateAuszugsnummer(ktoauszug)) isValidationOk = false;
				if (!validateIban(ktoauszug)) isValidationOk = false;
				if (!validateBuchungsdatum(ktoauszug)) isValidationOk = false;
				if (!validateBetraege(ktoauszug)) isValidationOk = false;
				if (!validateIndividualFields(ktoauszug)) isValidationOk = false;
			}
		}
		
		return isValidationOk;
	}
	
	private boolean validateAuszugsnummer(SepaKontoauszug ktoauszug) {
//		if (ktoauszug.getAuszugsnr() == null) {
//			myLogger.error("Kontoauszug mit Message-ID \"" + ktoauszug.getMessageId() + "\" hat keine Auszugsnummer.");
//			messages.add(new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_KTOAUSZUG_HAT_KEINE_AUSZUGSNUMMER, 
//					EJBSepaImportExceptionLP.SEVERITY_ERROR, ktoauszug.getMessageId()));
//		}
		return true;
	}
	
	private boolean validateIban(SepaKontoauszug ktoauszug) throws EJBSepaImportExceptionLP {
		return true;
	}

	protected boolean validateSalden(SepaKontoauszug ktoauszug) throws EJBSepaImportExceptionLP {
		if (!ktoauszug.hatSalden()) {
			myLogger.error("Kontoauszug hat keine Salden");
			messages.add(new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_KTOAUSZUG_HAT_KEINE_SALDEN, 
					EJBSepaImportExceptionLP.SEVERITY_ERROR, new Object[]{ktoauszug.getTypVersion()}));
			return false;
		}
		//Betraege mit Salden vergleichen
		SepaBetrag betragBuchungen = new SepaHabenBetrag(BigDecimal.ZERO);
		for (SepaBuchung buchung : ktoauszug.getBuchungen()) {
			betragBuchungen = betragBuchungen.add(buchung.getBetrag());
		}
		SepaBetrag betragErwarteterEndsaldo = ktoauszug.getStartSaldo().getBetrag().add(betragBuchungen);
		if (!betragErwarteterEndsaldo.equals(ktoauszug.getEndSaldo().getBetrag())) {
			messages.add(new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_SALDENBETRAEGE_FEHLERHAFT, 
					EJBSepaImportExceptionLP.SEVERITY_ERROR, new Object[]{betragErwarteterEndsaldo.getPlusMinusWert(), 
					ktoauszug.getEndSaldo().getBetrag().getPlusMinusWert()}));
			return false;
		}
		
		return true;
	}

	private boolean validateBuchungsdatum(SepaKontoauszug ktoauszug) throws EJBSepaImportExceptionLP {
		return true;
	}

	private boolean validateBetraege(SepaKontoauszug ktoauszug) {
		for (SepaBuchung buchung : ktoauszug.getBuchungen()) {
			BigDecimal summe = new BigDecimal(0);
			for (SepaZahlung zahlung : buchung.getZahlungen()) {
				if (zahlung.getBetrag() == null) {
					if (buchung.getZahlungen().size() == 1) {
						zahlung.setBetrag(buchung.getBetrag());
					} else {
						continue;
					}
				}
				summe = summe.add(zahlung.getBetrag().getPlusMinusWert());
			}
			
			if (summe.compareTo(buchung.getBetrag().getPlusMinusWert()) != 0) {
				myLogger.error("Buchungsbetrag \"" + buchung.getBetrag().getPlusMinusWert() + "\" stimmt nicht mit der"
						+ " Summe der Zahlungsbeträge der Buchung \"" + summe + "\" zusammen.");
				messages.add(new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_BUCHUNGSBETRAEGE_FEHLERHAFT, 
						EJBSepaImportExceptionLP.SEVERITY_ERROR, buchung.getBetrag().getPlusMinusWert(), summe));
				return false;
			}
		}
		return true;
	}
	
	private GroupManager getGroupManager(String lkz) {
		if ("AT".equals(lkz)) {
			return new GroupManagerAT();
		} else if ("DE".equals(lkz)) {
			return new GroupManagerDE();
		} else if (Helper.isOneOf(lkz, "CH", "LI")) {
			return new GroupManagerCH();
		}
		
		return null;
	}

	
	protected abstract class GroupManager {
		protected abstract BigDecimal getGroupKey(SepaKontoauszug ktoauszug);
		
		public Map<BigDecimal, List<SepaKontoauszug>> group(List<SepaKontoauszug> ktoauszuege) {
			Map<BigDecimal, List<SepaKontoauszug>> map = new HashMap<BigDecimal, List<SepaKontoauszug>>();
			
			for (SepaKontoauszug auszug : ktoauszuege) {
				BigDecimal key = getGroupKey(auszug);
				
				if (map.containsKey(key)) {
					map.get(key).add(auszug);
					
				} else {
					List<SepaKontoauszug> newGroup = new ArrayList<SepaKontoauszug>();
					newGroup.add(auszug);
					map.put(key, newGroup);
				}
			}
			
			return map;
		}

		public abstract Map<BigDecimal, List<SepaKontoauszug>> sort(Map<BigDecimal, List<SepaKontoauszug>> group);

		protected boolean verifyConsecutiveElectronicNumbering(List<SepaKontoauszug> list) {
			BigDecimal number = null;
			for (SepaKontoauszug ktoauszug : list) {
				if (number != null) {
					if (number.add(BigDecimal.ONE).compareTo(ktoauszug.getElektronischeAuszugsnr()) != 0) {
						myLogger.error("Elektronische Auszugsnummer ist innerhalb der Auszugsnummer \'" + ktoauszug.getAuszugsnr() + "\' nicht fortlaufend!");
						messages.add(new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_KEINE_FORTLAUFENDE_ELEKTRONISCHE_AUSZUGSNUMMER, 
								EJBSepaImportExceptionLP.SEVERITY_ERROR, ktoauszug.getAuszugsnr().toString(), number.add(BigDecimal.ONE), ktoauszug.getElektronischeAuszugsnr()));
						return false;
					}
				}
				number = ktoauszug.getElektronischeAuszugsnr();
			}
			
			return true;
		}
	}
	
	protected class GroupManagerAT extends GroupManager {
		@Override
		public BigDecimal getGroupKey(SepaKontoauszug ktoauszug) {
			return ktoauszug.getAuszugsnr();
		}

		@Override
		public Map<BigDecimal, List<SepaKontoauszug>> sort(Map<BigDecimal, List<SepaKontoauszug>> groups) {
			for (Map.Entry<BigDecimal, List<SepaKontoauszug>> entry : groups.entrySet()) {
				if (entry.getValue().size() == 1) {
					continue;
				}
				Collections.sort(entry.getValue(), elctrncSeqNbComparator);
				verifyConsecutiveElectronicNumbering(entry.getValue());
			}
			return groups;
		}
	}

	protected class GroupManagerDE extends GroupManager {
		@Override
		public BigDecimal getGroupKey(SepaKontoauszug ktoauszug) {
			return ktoauszug.getElektronischeAuszugsnr();
		}

		@Override
		public Map<BigDecimal, List<SepaKontoauszug>> sort(Map<BigDecimal, List<SepaKontoauszug>> group) {
			
			return groups;
		}
	}

	protected class GroupManagerCH extends GroupManager {
		@Override
		public BigDecimal getGroupKey(SepaKontoauszug ktoauszug) {
			return ktoauszug.getElektronischeAuszugsnr();
		}
		
		@Override
		public Map<BigDecimal, List<SepaKontoauszug>> sort(Map<BigDecimal, List<SepaKontoauszug>> groups) {
			return groups;
		}
	}
}
