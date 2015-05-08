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
package com.lp.server.finanz.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.jboss.annotation.ejb.TransactionTimeout;

import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.finanz.bl.FibuExportManager;
import com.lp.server.finanz.bl.FibuExportManagerFactory;
import com.lp.server.finanz.ejb.Belegbuchung;
import com.lp.server.finanz.ejb.Buchung;
import com.lp.server.finanz.ejb.Buchungdetail;
import com.lp.server.finanz.ejb.Finanzamt;
import com.lp.server.finanz.ejb.FinanzamtPK;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.ejb.Steuerkategorie;
import com.lp.server.finanz.ejb.Steuerkategoriekonto;
import com.lp.server.finanz.ejb.SteuerkategoriekontoPK;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BelegbuchungDtoAssembler;
import com.lp.server.finanz.service.BelegbuchungFac;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.UvaverprobungDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.Lieferant;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.Rechnungzahlung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BelegbuchungFacBean extends Facade implements BelegbuchungFac {
	@PersistenceContext
	private EntityManager em;

	public BelegbuchungDto createBelegbuchung(BelegbuchungDto belegbuchungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(belegbuchungDto, theClientDto.getIDUser());
		// Primary Key bestimmen
		PKGeneratorObj pkGen = getPKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BELEGBUCHUNG);
		belegbuchungDto.setIId(pk);
		try {
			Belegbuchung belegbuchung = new Belegbuchung(
					belegbuchungDto.getIId(), belegbuchungDto.getBelegartCNr(),
					belegbuchungDto.getIBelegiid(),
					belegbuchungDto.getBuchungIId());
			em.persist(belegbuchung);
			em.flush();
			setBelegbuchungFromBelegbuchungDto(belegbuchung, belegbuchungDto);
			return belegbuchungDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeBelegbuchung(BelegbuchungDto belegbuchungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(belegbuchungDto, theClientDto.getIDUser());
		if (belegbuchungDto != null) {
			Integer iId = belegbuchungDto.getIId();
			Belegbuchung toRemove = em.find(Belegbuchung.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}
	}

	public BelegbuchungDto updateBelegbuchung(BelegbuchungDto belegbuchungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(belegbuchungDto, theClientDto.getIDUser());
		Integer iId = belegbuchungDto.getIId();
		Belegbuchung belegbuchung = em.find(Belegbuchung.class, iId);
		if (belegbuchung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setBelegbuchungFromBelegbuchungDto(belegbuchung, belegbuchungDto);
		return belegbuchungDto;
	}

	public BelegbuchungDto belegbuchungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		Belegbuchung belegbuchung = em.find(Belegbuchung.class, iId);
		if (belegbuchung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBelegbuchungDto(belegbuchung);
	}

	public BelegbuchungDto belegbuchungFindByBelegartCNrBelegiid(
			String belegartCNr, Integer iBelegiid) throws EJBExceptionLP {
		Query query = em
				.createNamedQuery(Belegbuchung.QUERY_BelegbuchungfindByBelegartCNrBelegiid);
		query.setParameter(1, belegartCNr);
		query.setParameter(2, iBelegiid);
		Belegbuchung belegbuchung = null;
		try {
			belegbuchung = (Belegbuchung) query.getSingleResult();
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Belegbuchung nicht gefunden. Belegart: " + belegartCNr
							+ " ID: " + iBelegiid.intValue());
		}
		return assembleBelegbuchungDto(belegbuchung);
	}

	public BelegbuchungDto belegbuchungFindByBelegartCNrBelegiidOhneExc(
			String belegartCNr, Integer iBelegiid) throws EJBExceptionLP {
		HvTypedQuery<Belegbuchung> query = new HvTypedQuery<Belegbuchung>(
				em.createNamedQuery(Belegbuchung.QUERY_BelegbuchungfindByBelegartCNrBelegiid));
		query.setParameter(1, belegartCNr);
		query.setParameter(2, iBelegiid);
		List<Belegbuchung> belegbuchung = query.getResultList();
		return belegbuchung.size() == 1 ? assembleBelegbuchungDto(belegbuchung
				.get(0)) : null;
	}

	public BelegbuchungDto belegbuchungFindByBuchungIId(Integer buchungIId)
			throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery(Belegbuchung.QUERY_BelegbuchungfindByBuchungIId);
			query.setParameter(1, buchungIId);
			Belegbuchung belegbuchung = (Belegbuchung) query.getSingleResult();
			return assembleBelegbuchungDto(belegbuchung);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		}
	}

	public BelegbuchungDto belegbuchungFindByBuchungIIdOhneExc(
			Integer buchungIId) {
			HvTypedQuery<Belegbuchung> query = new HvTypedQuery<Belegbuchung>(
					em.createNamedQuery(Belegbuchung.QUERY_BelegbuchungfindByBuchungIId));
			query.setParameter(1, buchungIId);
			List<Belegbuchung> list = query.getResultList();
			if(list == null || list.size() == 0) return null;
			return assembleBelegbuchungDto(list.get(0));
	}

	private void setBelegbuchungFromBelegbuchungDto(Belegbuchung belegbuchung,
			BelegbuchungDto belegbuchungDto) {
		belegbuchung.setBelegartCNr(belegbuchungDto.getBelegartCNr());
		belegbuchung.setIBelegiid(belegbuchungDto.getIBelegiid());
		belegbuchung.setBuchungIId(belegbuchungDto.getBuchungIId());
		belegbuchung.setBelegartCNr(belegbuchungDto.getBelegartCNr());
		belegbuchung.setBuchungIIdZahlung(belegbuchungDto
				.getBuchungIIdZahlung());
		em.merge(belegbuchung);
		em.flush();
	}

	protected BelegbuchungDto assembleBelegbuchungDto(Belegbuchung belegbuchung) {
		return BelegbuchungDtoAssembler.createDto(belegbuchung);
	}

	private BelegbuchungDto[] assembleBelegbuchungDtos(
			Collection<?> belegbuchungs) {
		List<BelegbuchungDto> list = new ArrayList<BelegbuchungDto>();
		if (belegbuchungs != null) {
			Iterator<?> iterator = belegbuchungs.iterator();
			while (iterator.hasNext()) {
				Belegbuchung belegbuchung = (Belegbuchung) iterator.next();
				list.add(assembleBelegbuchungDto(belegbuchung));
			}
		}
		BelegbuchungDto[] returnArray = new BelegbuchungDto[list.size()];
		return (BelegbuchungDto[]) list.toArray(returnArray);
	}

	/**
	 * Verbuchen einer Rechnung
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public BuchungDto verbucheRechnung(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		long startTime = System.currentTimeMillis();

		BuchungDto buchungDto = null;
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			RechnungDto rechnungDto = null;
			String rechnungtyp = null;
			try {
				rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(
						rechnungIId);
				RechnungartDto rechnungartDto = getRechnungServiceFac()
						.rechnungartFindByPrimaryKey(
								rechnungDto.getRechnungartCNr(), theClientDto);
				rechnungtyp = rechnungartDto.getRechnungtypCNr();
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
			BelegbuchungDto bbDto = null;
			try {
				if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
					bbDto = getBelegbuchungFac(theClientDto.getMandant())
							.belegbuchungFindByBelegartCNrBelegiidOhneExc(
									LocaleFac.BELEGART_RECHNUNG, rechnungIId);
				} else if (rechnungtyp
						.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
					bbDto = getBelegbuchungFac(theClientDto.getMandant())
							.belegbuchungFindByBelegartCNrBelegiidOhneExc(
									LocaleFac.BELEGART_GUTSCHRIFT, rechnungIId);
				} else if (rechnungtyp
						.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
					// ignorieren, wird unten uebersprungen
				} else {
					myLogger.error("Ungueltiger Rechnungstyp, keine Verbuchung moeglich");
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
							"Ungueltiger Rechnungstyp: " + rechnungtyp);
				}

				if (bbDto != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
							"Beleg bereits verbucht: ID=" + rechnungIId);
				}
			} catch (RemoteException ex2) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex2);
			}
			// wenn nicht, passts ja
			boolean bRechnung0Erlaubt = false;
			try {
				if (getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_FINANZ_RECHNUNG_WERT0_ERLAUBT)
						.getCWert().equals("1"))
					bRechnung0Erlaubt = true;
			} catch (RemoteException e) {
				//
			}
			if (!rechnungtyp.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)
					&& !rechnungDto.getStatusCNr().equals(
							RechnungFac.STATUS_STORNIERT)) {
				if (!bRechnung0Erlaubt && rechnungDto.getNWert().signum() == 0)
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RECHNUNG_HAT_KEINEN_WERT, "",
							rechnungDto.getCNr());

				if (rechnungDto.getNWert().signum() != 0) {
					// SP 2013/01327 nur Rechnungen mit Wert <> 0 abhaengig vom
					// Parameter

					// TODO: bei Anzahlungsrechnung wird auf
					// Anzahlungserloeskonto
					// gebucht
					// bei Schlussrechnung werden die Anzahlungen am
					// Anzahlungserloeskonto gegengebucht und der Erloes der
					// Schlussrechnung gebucht
					// (Anzahlung- + Schlussrechnung = gleicher Auftrag)
					// noch Googlen wie das ueblicherweise gebucht wird
					//
					FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
					if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG))
						fibuExportKriterienDto
								.setSBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
					else
						fibuExportKriterienDto
								.setSBelegartCNr(LocaleFac.BELEGART_GUTSCHRIFT);
					fibuExportKriterienDto.setDStichtag(new Date(rechnungDto
							.getTBelegdatum().getTime()));

					FibuExportManager manager = FibuExportManagerFactory
							.getFibuExportManager(
									getExportVariante(theClientDto),
									getExportFormat(theClientDto),
									fibuExportKriterienDto, theClientDto);
					FibuexportDto[] exportDaten;
					if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG))
						exportDaten = manager.getExportdatenRechnung(
								rechnungIId, null);
					else if (rechnungtyp
							.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT))
						exportDaten = manager.getExportdatenGutschrift(
								rechnungIId, null);
					else
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
								"Rechnungstyp fuer Fibu nicht definiert: Typ="
										+ rechnungtyp);

					PartnerDto partnerDto = null;
					Konto debitorKonto = null;
					try {
						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										rechnungDto.getKundeIId(), theClientDto);
						partnerDto = getPartnerFac().partnerFindByPrimaryKey(
								kundeDto.getPartnerIId(), theClientDto);
						if (kundeDto.getIidDebitorenkonto() == null) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_KEIN_DEBITORENKONTO_DEFINIERT,
									"Kein Debitorenkonto: Kunde ID="
											+ kundeDto.getIId());
						}
						debitorKonto = em.find(Konto.class,
								kundeDto.getIidDebitorenkonto());
						if (debitorKonto.getSteuerkategorieIId() == null)
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
									"Keine Steuerkategorie bei Debitorenkonto ",
									debitorKonto.getCNr() + " "
											+ debitorKonto.getCBez());

						// if (kundeDto.getIidErloeseKonto() == null) {
						// throw new
						// EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_DEFINIERT,
						// "Kein Erloeskonto: Kunde ID=" + kundeDto.getIId());
						// }
					} catch (Exception ex3) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex3);
					}
					buchungDto = new BuchungDto();
					buchungDto.setCBelegnummer(rechnungDto.getCNr());
					buchungDto.setCText(partnerDto
							.getCName1nachnamefirmazeile1());
					buchungDto.setDBuchungsdatum(new Date(rechnungDto
							.getTBelegdatum().getTime()));
					buchungDto.setKostenstelleIId(rechnungDto
							.getKostenstelleIId());
					buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BUCHUNG);

					BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
					List<BuchungdetailDto> details = new ArrayList<BuchungdetailDto>();
					Integer steuerkategorieIId = null;
					boolean reverseCharge = Helper.short2boolean(rechnungDto
							.getBReversecharge());
					if (reverseCharge) {
						if (exportDaten[1].getDebitorenKontoIIdUebersteuert() == null) {
							steuerkategorieIId = debitorKonto
									.getSteuerkategorieIIdReverse();
							if(steuerkategorieIId == null)
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_REVERSE_DEFINIERT,
												"Keine Steuerkategorie bei Debitorenkonto "
														+ debitorKonto.getCNr(), debitorKonto
														.getCNr() + " " + debitorKonto.getCBez());
						} else {
							Konto debKontouebersteuert = em
									.find(Konto.class, exportDaten[1]
											.getDebitorenKontoIIdUebersteuert());
							steuerkategorieIId = debKontouebersteuert
									.getSteuerkategorieIIdReverse();
							if(steuerkategorieIId == null)
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_REVERSE_DEFINIERT,
												"Keine Steuerkategorie bei Debitorenkonto "
														+ debKontouebersteuert.getCNr(), debKontouebersteuert
														.getCNr() + " " + debKontouebersteuert.getCBez());
						}
					} else if (exportDaten[1].getDebitorenKontoIIdUebersteuert() == null) {
						steuerkategorieIId = debitorKonto
								.getSteuerkategorieIId();
						if(steuerkategorieIId == null)
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
											"Keine Steuerkategorie bei Debitorenkonto "
													+ debitorKonto.getCNr(), debitorKonto
													.getCNr() + " " + debitorKonto.getCBez());
					} else {
						Konto debKontouebersteuert = em.find(Konto.class,
								exportDaten[1]
										.getDebitorenKontoIIdUebersteuert());
						steuerkategorieIId = debKontouebersteuert
								.getSteuerkategorieIId();
						if(steuerkategorieIId == null)
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
											"Keine Steuerkategorie bei Debitorenkonto "
													+ debKontouebersteuert.getCNr(), debKontouebersteuert
													.getCNr() + " " + debKontouebersteuert.getCBez());
					}
					details.addAll(Arrays.asList(getBuchungdetailsVonExportDtos(exportDaten,
							steuerkategorieIId, true, false, reverseCharge,
							theClientDto)));
					if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
						buchungDto
								.setBelegartCNr(LocaleFac.BELEGART_FIBU_RECHNUNG);
						belegbuchungDto
								.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
					} else if (rechnungtyp
							.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
						buchungDto
								.setBelegartCNr(LocaleFac.BELEGART_GUTSCHRIFT);
						belegbuchungDto
								.setBelegartCNr(LocaleFac.BELEGART_GUTSCHRIFT);
					} else {
						myLogger.error("Ungueltiger Rechnungstyp, keine Verbuchung moeglich");
					}
					// nur buchen, wenn die Rechnung auch verbucht werden kann
					if (details != null) {
						try {
							if (rechnungDto.getRechnungartCNr().equals(
									RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
								// zusaetzlich alle Anzahlungsrechnungen
								// ausbuchen
								details.addAll(Arrays.asList(getDetailsSchlussrechnungAnzahlungen(
										rechnungDto, theClientDto)));
							}
							
							if(reverseCharge) {
								MwstsatzDto mwstSatz = getMandantFac().mwstsatzFindByPrimaryKey(rechnungDto.getMwstsatzIId(), theClientDto);
								BigDecimal steuer = rechnungDto.getNWert().multiply(new BigDecimal(mwstSatz.getFMwstsatz()).divide(new BigDecimal("100")));
								steuer = Helper.rundeKaufmaennisch(steuer, FinanzFac.NACHKOMMASTELLEN);
								
								KontoDto debitor = getFinanzFac().kontoFindByPrimaryKey(details.get(0).getKontoIId());
								Integer ustKontoIId = getFinanzServiceFac().getUstKontoFuerSteuerkategorie(debitor.getSteuerkategorieIIdReverse(), mwstSatz.getIIMwstsatzbezId());
								Integer rcStKontoIId = getFinanzServiceFac().getEUstKontoFuerSteuerkategorie(debitor.getSteuerkategorieIIdReverse(), mwstSatz.getIIMwstsatzbezId());
								
								if(ustKontoIId != null && rcStKontoIId != null) {
									BuchungdetailDto ustHaben = new BuchungdetailDto();
									ustHaben.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
									ustHaben.setNBetrag(steuer);
									ustHaben.setNUst(BigDecimal.ZERO);
									ustHaben.setKontoIId(ustKontoIId);
									
									BuchungdetailDto ustSoll = new BuchungdetailDto();
									ustSoll.setBuchungdetailartCNr(BuchenFac.SollBuchung);
									ustSoll.setNBetrag(steuer);
									ustSoll.setNUst(BigDecimal.ZERO);
									ustSoll.setKontoIId(rcStKontoIId);
									
									details.add(ustHaben);
									details.add(ustSoll);
								}
								
							}
//							HelperServer.printBuchungssatz(details, getFinanzFac(), System.out);
							
							// Das Verbuchen einer Rechnung muss immer den
							// Buchungsregeln entsprechen
							buchungDto = getBuchenFac().buchen(buchungDto,
									details.toArray(new BuchungdetailDto[0]), true, theClientDto);

							// in Tabelle buchungRechnung speichern
							belegbuchungDto.setBuchungIId(buchungDto.getIId());
							belegbuchungDto.setIBelegiid(rechnungDto.getIId());
							createBelegbuchung(belegbuchungDto, theClientDto);
							
							if (rechnungDto.getRechnungartCNr().equals(
									RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
								List<BelegbuchungDto> ausziffernListe = new ArrayList<BelegbuchungDto>();
								ausziffernListe.add(belegbuchungDto);
								
								for(RechnungDto rech : getRechnungFac().rechnungFindByAuftragIId(rechnungDto.getAuftragIId())) {
									if(rech.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {
										ausziffernListe.addAll(getAlleBelegbuchungenInklZahlungenAR(rech.getIId()));
									}
								}
								FinanzamtDto famt = getFinanzFac().finanzamtFindByPrimaryKey(debitorKonto.getFinanzamtIId(), debitorKonto.getMandantCNr(), theClientDto);
								belegbuchungenAusziffernWennNoetig(rechnungDto.isReverseCharge() ?
										famt.getKontoIIdRCAnzahlungErhaltBezahlt() :
										famt.getKontoIIdAnzahlungErhaltBezahlt(), ausziffernListe);
								belegbuchungenAusziffernWennNoetig(rechnungDto.isReverseCharge() ?
										famt.getKontoIIdRCAnzahlungErhaltVerr() :
										famt.getKontoIIdAnzahlungErhaltVerr(), ausziffernListe);
							}

							// Status der Rechnung auf verbucht setzen
							// AD: nicht auf verbucht setzen, macht erst die UVA
							/*
							 * rechnungDto.setStatusCNr(RechnungFac.STATUS_VERBUCHT
							 * ); getRechnungFac().updateRechnung(rechnungDto,
							 * theClientDto);
							 */
						} catch (RemoteException ex) {
							throwEJBExceptionLPRespectOld(ex);
						}
					}

					long dauer = System.currentTimeMillis() - startTime;
					myLogger.logData("Verbuchen der Rechnung dauerte " + dauer
							+ " ms.");
				}
			}
		}
		return buchungDto;
	}

	private BuchungdetailDto[] getDetailsSchlussrechnungAnzahlungen(
			RechnungDto rechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		ArrayList<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		RechnungDto[] anzRechnungen = getRechnungFac()
				.rechnungFindByAuftragIId(rechnungDto.getAuftragIId());
		for (int i = 0; i < anzRechnungen.length; i++) {
			if (!anzRechnungen[i].getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_ANZAHLUNG)
					|| anzRechnungen[i].getNWertfw() == null) continue;
			
			List<BuchungdetailDto> tempList = new ArrayList<BuchungdetailDto>();
			boolean bMitUst = (anzRechnungen[i].getNWertust().signum() != 0);

			BelegbuchungDto belegRech = belegbuchungFindByBelegartCNrBelegiid(
					LocaleFac.BELEGART_RECHNUNG, anzRechnungen[i].getIId());

			BuchungdetailDto[] detailDtos = getBuchenFac()
					.buchungdetailsFindByBuchungIId(
							belegRech.getBuchungIId());

//			System.out.println("Anzahlung: " + anzRechnungen[i].getCNr());
//			HelperServer.printBuchungssatz(Arrays.asList(detailDtos), getFinanzFac(), System.out);

			BuchungdetailDto perDebitor = detailDtos[0];
			perDebitor.setNBetrag(perDebitor.getNBetrag().negate());
			tempList.add(perDebitor);
			if(bMitUst) {
				BuchungdetailDto anUst = detailDtos[2];
				anUst.setNBetrag(anUst.getNBetrag().negate());
				tempList.add(anUst);
			}
			
			if(RechnungFac.STATUS_BEZAHLT.equals(anzRechnungen[i].getStatusCNr())) {
				RechnungzahlungDto[] zahlungDtos = getRechnungFac()
						.zahlungFindByRechnungIId(anzRechnungen[i].getIId());
				Integer kontoAnzBezahlt = null;
				for (int j = 0; j < zahlungDtos.length; j++) {
					if(RechnungFac.ZAHLUNGSART_GUTSCHRIFT.equals(zahlungDtos[j].getZahlungsartCNr()))
						continue;
					BelegbuchungDto bbDto = belegbuchungFindByBelegartCNrBelegiid(
									LocaleFac.BELEGART_REZAHLUNG,
									zahlungDtos[j].getIId());
					detailDtos = getBuchenFac()
							.buchungdetailsFindByBuchungIId(
									bbDto.getBuchungIId());
					boolean skonto = detailDtos.length > 5;
					BuchungdetailDto perAnzBezahlt = detailDtos[detailDtos.length-(skonto?3:2)];
					//dritte von hinten ist Buchung an erhaltene Anzahlungen bezahlt
					perAnzBezahlt.swapSollHaben();
					perAnzBezahlt.setKontoIIdGegenkonto(null);
					tempList.add(perAnzBezahlt);
					
					if(skonto) {
						BuchungdetailDto perAnzVerrech = detailDtos[detailDtos.length-4];
						perAnzVerrech.setNBetrag(detailDtos[2].getNBetrag().negate());
						perAnzVerrech.setKontoIIdGegenkonto(null);
						kontoAnzBezahlt = perAnzVerrech.getKontoIId();
						//vierte von hinten ist Buchung per erhaltene Anzahlungen verrechnet
						tempList.add(perAnzVerrech);
					}
					
				}
				korrigiereRundungsUngenauigkeit(tempList, kontoAnzBezahlt);
			} else {
//				HelperServer.printBuchungssatz(detailDtos, getFinanzFac()) ;
				RechnungzahlungDto zwischenZahlung = new RechnungzahlungDto(); //Die Zahlung gibts noch nicht wirklich,
				zwischenZahlung.setNBetrag(anzRechnungen[i].getNWert());// aber wir brauchen sie um die Buchungsdetails zu generieren
				zwischenZahlung.setNBetragUst(anzRechnungen[i].getNWertust());				
				BuchungdetailDto[] details = new BuchungdetailDto[]{new BuchungdetailDto()};
				details[0].setKontoIIdGegenkonto(perDebitor.getKontoIId());
				details = addAnzahlungZahlungDetails(details, rechnungDto, zwischenZahlung, theClientDto);
//				BuchungdetailDto perAnzBezahlt = details[detailDtos.length-(2)];
//				//dritte von hinten ist Buchung an erhaltene Anzahlungen bezahlt
				BuchungdetailDto perAnzBezahlt = details[2];
				perAnzBezahlt.swapSollHaben();
				perAnzBezahlt.setKontoIIdGegenkonto(null);
				tempList.add(perAnzBezahlt);
			}
			list.addAll(tempList);
		}
		for(BuchungdetailDto dto : list) {
			dto.setIId(null);
			dto.setBuchungIId(null);
			dto.setIAusziffern(null);
			dto.setIAuszug(null);
		}
		
//		System.out.println("Alle: ");
//		HelperServer.printBuchungssatz(list, getFinanzFac(), System.out);
		return list.toArray(new BuchungdetailDto[list.size()]);
	}
	
	private boolean korrigiereRundungsUngenauigkeit(List<BuchungdetailDto> list, Integer kontoIId) {
		BigDecimal saldo = getBuchungSaldo(list);
		if(saldo.signum() == 0) return true;
		if(saldo.compareTo(new BigDecimal("0.01"))>0) return false;
		
		for(BuchungdetailDto dto : list) {
			if(!dto.getKontoIId().equals(kontoIId)) continue;
			if(dto.getBuchungdetailartCNr().equals(
						BuchenFac.HabenBuchung)) {
				saldo = saldo.negate();
			}
			dto.setNBetrag(dto.getNBetrag().add(saldo));
			return true;
		}
		return false;
	}
	
	private BigDecimal getBuchungSaldo(List<BuchungdetailDto> list) {
		BigDecimal saldo = BigDecimal.ZERO;
		for(BuchungdetailDto dto : list) {
			if(dto.getBuchungdetailartCNr().equals(
					BuchenFac.HabenBuchung)) {
				saldo = saldo.add(dto.getNBetrag());
			} else {
				saldo = saldo.subtract(dto.getNBetrag());
			}
		}
		return saldo;
	}

	private BuchungdetailDto getSteuerBuchungAnzahlungsrechnung(
			RechnungDto rechnungDto, BuchungdetailDto[] zahlungdetailDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// Anzahlungsrechnung hat nur EINE preistragende Position, daher ist die
		// Position fix
		BelegbuchungDto bbDto = getBelegbuchungFac(theClientDto.getMandant())
				.belegbuchungFindByBelegartCNrBelegiid(
						LocaleFac.BELEGART_RECHNUNG, rechnungDto.getIId());
		BuchungdetailDto[] detailDtos = getBuchenFac()
				.buchungdetailsFindByBuchungIId(bbDto.getBuchungIId());

		// Steuerbuchung ist die vorletzte Buchung
		BuchungdetailDto steuerbuchungDto = detailDtos[detailDtos.length - 2];
		Integer steuerkontoIId = steuerbuchungDto.getKontoIId();

		// Steuerbetrag um anteilige Steuer aus Skonti korrigieren, falls welche
		// vorhanden
		for (int i = 0; i < zahlungdetailDtos.length; i++)
			if (zahlungdetailDtos[i].getKontoIId().equals(steuerkontoIId))
				if (zahlungdetailDtos[i].isHabenBuchung())
					steuerbuchungDto.setNBetrag(steuerbuchungDto.getNBetrag()
							.add(zahlungdetailDtos[i].getNBetrag()));

		return steuerbuchungDto;
	}

	private void verbucheRechnungenPeriode(Integer geschaeftsjahr, int periode,
			boolean alleNeu, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		RechnungDto[] rechnungen = getRechnungFac()
				.rechnungFindByBelegdatumVonBis(theClientDto.getMandant(),
						dBeginn, dEnd);
		for (int i = 0; i < rechnungen.length; i++) {
			if (!rechnungen[i].getStatusCNr().equals(
					RechnungFac.STATUS_ANGELEGT)
					&& !rechnungen[i].getStatusCNr().equals(
							RechnungFac.STATUS_STORNIERT)) {
				BelegbuchungDto bbDto = null;
				if (rechnungen[i].getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_RECHNUNG)) {
					bbDto = getBelegbuchungFac(theClientDto.getMandant())
							.belegbuchungFindByBelegartCNrBelegiidOhneExc(
									LocaleFac.BELEGART_RECHNUNG,
									rechnungen[i].getIId());
				} else if (rechnungen[i].getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
					bbDto = getBelegbuchungFac(theClientDto.getMandant())
							.belegbuchungFindByBelegartCNrBelegiidOhneExc(
									LocaleFac.BELEGART_GUTSCHRIFT,
									rechnungen[i].getIId());
				} else {
					return;
				}
				if ((bbDto != null) && alleNeu) {
					verbucheRechnungGutschriftRueckgaengig(
							rechnungen[i].getIId(),
							rechnungen[i].getRechnungartCNr(), theClientDto);
					verbucheRechnung(rechnungen[i].getIId(), theClientDto);
				} else if (bbDto == null) {
					// dann verbuchen
					verbucheRechnung(rechnungen[i].getIId(), theClientDto);
				}
			}
		}
	}

	private void verbucheEingangsrechnungenPeriode(Integer geschaeftsjahr,
			int periode, boolean alleNeu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		EingangsrechnungDto[] rechnungen = getEingangsrechnungFac()
				.eingangsrechnungFindByMandantCNrDatumVonBis(
						theClientDto.getMandant(), dBeginn, dEnd);
		for (int i = 0; i < rechnungen.length; i++) {
			if (!rechnungen[i].getStatusCNr().equals(
					EingangsrechnungFac.STATUS_STORNIERT)) {
				BelegbuchungDto bbDto = getBelegbuchungFac(
						theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_EINGANGSRECHNUNG,
								rechnungen[i].getIId());
				if ((bbDto != null) && alleNeu) {
					verbucheEingangsrechnungRueckgaengig(
							rechnungen[i].getIId(), theClientDto);
					verbucheEingangsrechnung(rechnungen[i].getIId(),
							theClientDto);
				} else if (bbDto == null) {
					// dann verbuchen
					verbucheEingangsrechnung(rechnungen[i].getIId(),
							theClientDto);
				}
			}
		}
	}

	private void verbucheZahlungenPeriode(Integer geschaeftsjahr, int periode,
			boolean alleNeu, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date dVon = new Date(tVonBis[0].getTime());
		Date dBis = new Date(tVonBis[1].getTime());

		List<Integer> zahlungenIds = getRechnungFac()
				.rechnungzahlungIdsByMandantZahldatumVonBis(
						theClientDto.getMandant(), dVon, dBis);
		Iterator<Integer> it = zahlungenIds.iterator();
		while (it.hasNext()) {
			Integer iId = it.next();
			BelegbuchungDto bbDto = getBelegbuchungFac(
					theClientDto.getMandant())
					.belegbuchungFindByBelegartCNrBelegiidOhneExc(
							LocaleFac.BELEGART_REZAHLUNG, iId);
			if ((bbDto != null) && alleNeu) {
				verbucheZahlungRueckgaengig(iId, theClientDto);
				verbucheZahlung(iId, theClientDto);
			} else if (bbDto == null) {
				// dann verbuchen
				verbucheZahlung(iId, theClientDto);
			}
		}
	}

	private void verbucheZahlungenErPeriode(Integer geschaeftsjahr,
			int periode, boolean alleNeu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date dVon = new Date(tVonBis[0].getTime());
		Date dBis = new Date(tVonBis[1].getTime());

		List<Integer> zahlungenIds = getEingangsrechnungFac()
				.eingangsrechnungzahlungIdsByMandantZahldatumVonBis(
						theClientDto.getMandant(), dVon, dBis);
		Iterator<Integer> it = zahlungenIds.iterator();
		while (it.hasNext()) {
			Integer iId = it.next();
			BelegbuchungDto bbDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_ERZAHLUNG, iId);
			if ((bbDto != null) && alleNeu) {
				verbucheZahlungErRueckgaengig(iId, theClientDto);
				bbDto = null; // damit neu verbucht wird!
			}
			if (bbDto == null) {
				Eingangsrechnungzahlung ez = em.find(
						Eingangsrechnungzahlung.class, iId);
				if (!ez.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
					Eingangsrechnung er = em.find(Eingangsrechnung.class,
							ez.getEingangsrechnungIId());
					boolean verbuchen = false;
					// nur buchen wenn Rechnung vollstaendig kontiert
					if (er.getKontoIId() != null) {
						verbuchen = true;
					} else {
						try {
							BigDecimal bdOffen = getEingangsrechnungFac()
									.getWertNochNichtKontiert(
											ez.getEingangsrechnungIId());
							if (bdOffen.doubleValue() == 0)
								verbuchen = true;
						} catch (EJBExceptionLP e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
					if (verbuchen)
						verbucheZahlungEr(iId, theClientDto);
				}
			}
		}
	}

	@TransactionTimeout(value = 120)
	public void verbucheBelegePeriode(Integer geschaeftsjahr, int periode,
			boolean alleNeu, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		verbucheRechnungenPeriode(geschaeftsjahr, periode, alleNeu,
				theClientDto);
		verbucheZahlungenPeriode(geschaeftsjahr, periode, alleNeu, theClientDto);
		verbucheEingangsrechnungenPeriode(geschaeftsjahr, periode, alleNeu,
				theClientDto);
		verbucheZahlungenErPeriode(geschaeftsjahr, periode, alleNeu,
				theClientDto);
	}

	protected BuchungdetailDto[] getBuchungdetailsVonExportDtos(
			FibuexportDto[] exportDaten, Integer steuerkategorieIID,
			boolean bVerkauf, boolean bIgErwerb, boolean reverseCharge,
			TheClientDto theClientDto) {

		ArrayList<BuchungdetailDto> al = new ArrayList<BuchungdetailDto>();

		for (int i = 0; i < exportDaten.length; i++) {
			BuchungdetailDto detail = new BuchungdetailDto();
			detail.setKontoIId(exportDaten[i].getKontoDto().getIId());
			if (exportDaten[i].getSollbetragBD() != null) {
				detail.setBuchungdetailartCNr(BuchenFac.SollBuchung);
				detail.setNBetrag(exportDaten[i].getSollbetragBD());
			} else {
				detail.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
				detail.setNBetrag(exportDaten[i].getHabenbetragBD());
			}
			if (exportDaten[i].getSteuerBD() != null) {
				detail.setNUst(exportDaten[i].getSteuerBD());
			} else {
				detail.setNUst(new BigDecimal(0));
			}

			if (exportDaten[i].getGegenkontoDto() != null)
				detail.setKontoIIdGegenkonto(exportDaten[i].getGegenkontoDto()
						.getIId());
			al.add(detail);
			if (exportDaten[i].getSteuerBD() != null) {
				if (exportDaten[i].getSteuerBD().doubleValue() != 0) {
					detail = new BuchungdetailDto();
					Integer mwstsatzbezIId = null;
					if (exportDaten[i].getMwstsatz() == null) {
						// Handsteuer
						MwstsatzbezDto[] mwstDtos = getMandantFac()
								.mwstsatzbezFindAllByMandantAsDto(
										theClientDto.getMandant(), theClientDto);
						for (int m = 0; m < mwstDtos.length; m++) {
							if (mwstDtos[m].getBHandeingabe()) {
								mwstsatzbezIId = mwstDtos[m].getIId();
								break;
							}
						}
					} else {
						mwstsatzbezIId = exportDaten[i].getMwstsatz()
								.getIIMwstsatzbezId();
					}

					Steuerkategoriekonto stkk = getSteuerkategoriekonto(
							steuerkategorieIID, mwstsatzbezIId);
					if (stkk != null) {
						if (bVerkauf)
							detail.setKontoIId(stkk.getKontoIIdVk());
						else
							detail.setKontoIId(stkk.getKontoIIdEk());
					} else {
						// alte Variante nutzen
						detail.setKontoIId(exportDaten[i].getKontoDto()
								.getKontoIIdWeiterfuehrendUst());
					}
					if (detail.getKontoIId() == null) {
						Steuerkategorie steuerkategorie = em.find(
								Steuerkategorie.class, steuerkategorieIID);
						Mwstsatzbez mwstsatzbez = em.find(Mwstsatzbez.class,
								mwstsatzbezIId);
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_DEFINIERT,
								"Kein Erl\u00F6skonto definiert. "
										+ exportDaten[i].getBelegart() + ": "
										+ exportDaten[i].getBelegnummer()
										+ ", " + "Steuerkategorie: "
										+ steuerkategorie.getCBez() + ", "
										+ "Steuersatz: "
										+ mwstsatzbez.getCBezeichnung());
					}

					if (exportDaten[i].getSollbetragBD() != null) {
						detail.setBuchungdetailartCNr(BuchenFac.SollBuchung);
					} else {
						detail.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					}
					detail.setNBetrag(exportDaten[i].getSteuerBD());
					detail.setNUst(new BigDecimal(0));
					detail.setKontoIIdGegenkonto(exportDaten[i]
							.getGegenkontoDto().getIId());
					if (bIgErwerb || reverseCharge) {
						// EinfuhrUst buchen
						BuchungdetailDto detail1 = new BuchungdetailDto();
						detail1.setKontoIId(stkk.getKontoIIdEinfuhrUst());
						if (detail1.getKontoIId() == null) {
							Steuerkategorie steuerkategorie = em.find(
									Steuerkategorie.class, steuerkategorieIID);
							Mwstsatzbez mwstsatzbez = em.find(
									Mwstsatzbez.class, mwstsatzbezIId);
							String error = exportDaten[i].getBelegart() + ": "
									+ exportDaten[i].getBelegnummer() + ", "
									+ "Steuerkategorie: "
									+ steuerkategorie.getCBez() + ", "
									+ "Steuersatz: "
									+ mwstsatzbez.getCBezeichnung();
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_KEIN_EINFUHRUMSATZSTEUERKONTO_DEFINIERT,
									error, error);
						}
						detail1.setNBetrag(detail.getNBetrag());
						detail1.setNUst(detail.getNUst());
						detail1.setKontoIIdGegenkonto(detail.getKontoIId());
						if (detail.getBuchungdetailartCNr().equals(
								BuchenFac.HabenBuchung)) {
							detail1.setBuchungdetailartCNr(BuchenFac.SollBuchung);
						} else {
							detail1.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
						}
						detail.setKontoIIdGegenkonto(detail1.getKontoIId());
						al.add(detail);
						al.add(detail1);
					} else {
						al.add(detail);
					}
				}
			}
		}
		BuchungdetailDto[] details = new BuchungdetailDto[al.size()];
		for (int i = 0; i < al.size(); i++) {
			details[i] = al.get(i);
		}
		/*
		 * // Buchung am Kundenkonto details[0] = new BuchungdetailDto();
		 * details[0].setKontoIId(debitorenKontoIId);
		 * details[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		 * details[0].setNBetrag
		 * (rechnungDto.getNWert().add(rechnungDto.getNWertust()));
		 * details[0].setNUst(rechnungDto.getNWertust());
		 * details[0].setKontoIIdGegenkonto(erloesKontoIId); // Buchung am
		 * Erloeskonto details[1] = new BuchungdetailDto();
		 * details[1].setKontoIId(erloesKontoIId);
		 * details[1].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		 * details[1].setNBetrag(rechnungDto.getNWert());
		 * details[1].setNUst(zero);
		 * details[1].setKontoIIdGegenkonto(debitorenKontoIId); // Buchung am
		 * UST-Konto details[2] = new BuchungdetailDto();
		 * details[2].setKontoIId(ustKontoIId);
		 * details[2].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		 * details[2].setNBetrag(rechnungDto.getNWertust());
		 * details[2].setNUst(zero);
		 * details[2].setKontoIIdGegenkonto(debitorenKontoIId);
		 */
		return details;
	}

	// TODO: gemeinsame Methode!! FibuExportFacBean
	protected String getExportVariante(TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_EXPORT_VARIANTE);
			return parameter.getCWert();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	// TODO: gemeinsame Methode!! FibuExportFacBean
	protected String getExportFormat(TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_EXPORT_ZIELPROGRAMM);
			return parameter.getCWert();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Rechnung
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void verbucheRechnungRueckgaengig(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		verbucheRechnungGutschriftRueckgaengig(rechnungIId,
				LocaleFac.BELEGART_RECHNUNG, theClientDto);
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Gutschrift und Wertgutschrift
	 * Anmerkung: Wertgutschrift ist in der Fibu die fb_belegart Gutschrift
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void verbucheGutschriftRueckgaengig(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		verbucheRechnungGutschriftRueckgaengig(rechnungIId,
				LocaleFac.BELEGART_GUTSCHRIFT, theClientDto);
	}

	private void verbucheRechnungGutschriftRueckgaengig(Integer rechnungIId,
			String belegartCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			BelegbuchungDto belegbuchungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					belegartCNr, rechnungIId);
			if (belegbuchungDto != null) {
				getSystemFac().pruefeGeschaeftsjahrSperre(belegbuchungDto,
						theClientDto.getMandant());

				Integer buchungIId = belegbuchungDto.getBuchungIId();
				removeBelegbuchung(belegbuchungDto, theClientDto);
				getBuchenFac().storniereBuchung(buchungIId, theClientDto);
			}
		}
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Zahlung
	 * 
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            TheClientDto
	 * @throws EJBExceptionLP
	 */
	private void verbucheZahlungRueckgaengig(Integer zahlungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		RechnungzahlungDto rezDto;
		try {
			rezDto = getRechnungFac().rechnungzahlungFindByPrimaryKey(
					zahlungIId);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
		verbucheZahlungRueckgaengig(rezDto, theClientDto);
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Zahlung eventuell vorhandene
	 * Vorauszahlung wieder aktivieren
	 * 
	 * @param zahlungDto
	 *            RechnungzahlungDto
	 * @param theClientDto
	 *            TheClientDto
	 * @throws EJBExceptionLP
	 */
	public void verbucheZahlungRueckgaengig(RechnungzahlungDto zahlungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			myLogger.logData(zahlungDto.getIId(), theClientDto.getIDUser());
			BelegbuchungDto buchungZahlungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_REZAHLUNG, zahlungDto.getIId());
			if (buchungZahlungDto != null) {
				getSystemFac().pruefeGeschaeftsjahrSperre(buchungZahlungDto,
						theClientDto.getMandant());

				Integer buchungIId = buchungZahlungDto.getBuchungIId();
				removeBelegbuchung(buchungZahlungDto, theClientDto);
				getBuchenFac().storniereBuchung(buchungIId, theClientDto);
				if (zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
					Integer bdIId = zahlungDto.getBuchungdetailIId();
					try {
						BuchungDto bDto = bucheVorauszahlungBetrag(
								bdIId,
								zahlungDto.getNBetrag().add(
										zahlungDto.getNBetragUst()),
								theClientDto);
						Rechnungzahlung zahlung = em.find(
								Rechnungzahlung.class, zahlungDto.getIId());
						if (zahlung != null) {
							// es wurde nur eine Zahlung neu in der Fibu
							// verbucht und diese ist nicht geloescht
							// dann das neue Buchungsdetail an die Zahlung
							// haengen
							BuchungdetailDto[] bdtos = getBuchenFac()
									.buchungdetailsFindByBuchungIId(
											bDto.getIId());
							zahlung.setBuchungdetailIId(bdtos[0].getIId());
							em.merge(zahlung);
							em.flush();
						}
					} catch (RemoteException e) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_BUCHUNGAKTIVIEREN_NICHT_MOEGLICH,
								"Vorauszahlung kann nicht aktiviert werden. "
										+ e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Ermitteln der Buchungsdetails zum Verbuchen einer Rechnung.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return BuchungdetailDto[]
	 * @throws EJBExceptionLP
	 */
	private BuchungdetailDto[] getBuchungdetailsVonRechnung(
			Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		RechnungDto rechnungDto = null;
		KundeDto kundeDto = null;
		try {
			rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		Integer debitorenKontoIId = kundeDto.getIidDebitorenkonto();
		Integer erloesKontoIId = kundeDto.getIidErloeseKonto();
		KontoDto erloesKontoDto = null;
		try {
			erloesKontoDto = getFinanzFac().kontoFindByPrimaryKey(
					erloesKontoIId);
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}
		Integer ustKontoIId = erloesKontoDto.getKontoIIdWeiterfuehrendUst();
		// es muessen alle Konten definiert sein
		if (debitorenKontoIId == null || erloesKontoIId == null
				|| ustKontoIId == null) {
			// ist eines der konten nicht definiert, so braucht die rechnung
			// nicht verbucht werden
			return null;
		}
		// jetzt die einzelbuchungen bauen

		BuchungdetailDto[] details = new BuchungdetailDto[3];
		BigDecimal zero = new BigDecimal(0);
		// Buchung am Kundenkonto
		details[0] = new BuchungdetailDto();
		details[0].setKontoIId(debitorenKontoIId);
		details[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		details[0].setNBetrag(rechnungDto.getNWert().add(
				rechnungDto.getNWertust()));
		details[0].setNUst(rechnungDto.getNWertust());
		details[0].setKontoIIdGegenkonto(erloesKontoIId);
		// Buchung am Erloeskonto
		details[1] = new BuchungdetailDto();
		details[1].setKontoIId(erloesKontoIId);
		details[1].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		details[1].setNBetrag(rechnungDto.getNWert());
		details[1].setNUst(zero);
		details[1].setKontoIIdGegenkonto(debitorenKontoIId);
		// Buchung am UST-Konto
		details[2] = new BuchungdetailDto();
		details[2].setKontoIId(ustKontoIId);
		details[2].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		details[2].setNBetrag(rechnungDto.getNWertust());
		details[2].setNUst(zero);
		details[2].setKontoIIdGegenkonto(debitorenKontoIId);
		return details;
	}

	/**
	 * Ermitteln der Buchungsdetails zum Verbuchen einer Zahlung.
	 * 
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return BuchungdetailDto[]
	 * @throws EJBExceptionLP
	 */
	private BuchungdetailDto[] getBuchungdetailsVonZahlung(
			RechnungzahlungDto zahlungDto, RechnungDto rechnungDto,
			boolean bBucheMitSkonto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer sollKontoIId = null;
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
				rechnungDto.getKundeIId(), theClientDto);
		if (zahlungDto.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BANK)) {
			BankverbindungDto bankverbindungDto = null;
			try {
				if (zahlungDto.getBankkontoIId() != null) {
					bankverbindungDto = getFinanzFac()
							.bankverbindungFindByPrimaryKey(
									zahlungDto.getBankkontoIId());
					sollKontoIId = bankverbindungDto.getKontoIId();
				}
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_BAR)) {
			KassenbuchDto kassenbuchDto = null;
			try {
				if (zahlungDto.getKassenbuchIId() != null) {
					kassenbuchDto = getFinanzFac().kassenbuchFindByPrimaryKey(
							zahlungDto.getKassenbuchIId(), theClientDto);
					sollKontoIId = kassenbuchDto.getKontoIId();
				}
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
			Lieferant lieferant = null;
			try {
				Eingangsrechnung eingangsrechnung = em.find(
						Eingangsrechnung.class,
						zahlungDto.getEingangsrechnungIId());
				lieferant = em.find(Lieferant.class,
						eingangsrechnung.getLieferantIId());
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
			sollKontoIId = lieferant.getKontoIIdKreditorenkonto();
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
			if (zahlungDto.getBuchungdetailIId() == null)
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"BuchungdetailDto=null in Vorauszahlung");
			try {
				Buchungdetail bd = em.find(Buchungdetail.class,
						zahlungDto.getBuchungdetailIId());
				sollKontoIId = bd.getKontoIId();
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
			sollKontoIId = kundeDto.getIidDebitorenkonto();
			//hier das Debitorenkonto verwenden, wird von der aufrufenden Methode sowieso
			//verworfen. Anders waere der Aufwand einfach zu grosz 
			// TODO: Alles was Zahlungen betrifft refactoren
		}
		KontoDto debitorenKontoDto = null;
		KontoDto sollKontoDto = null;
		Integer debitorenKontoIId = kundeDto.getIidDebitorenkonto();
		try {
			debitorenKontoDto = getFinanzFac().kontoFindByPrimaryKey(
					debitorenKontoIId);
			sollKontoDto = getFinanzFac().kontoFindByPrimaryKey(sollKontoIId);
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}
		// es muessen alle Konten definiert sein
		if (debitorenKontoIId == null || sollKontoIId == null) {
			// ist eines der konten nicht definiert, so braucht die Zahlung
			// nicht verbucht werden
			return null;
		}
		// jetzt die einzelbuchungen bauen

		// pruefen ob Skonto zu buchen
		boolean bMitSkonto = false;
		boolean bMehrfachSteuersatz = false;
		boolean reverseCharge = Helper.short2boolean(rechnungDto.getBReversecharge());
		int mehrfachSteuerBuchungen = 0;
		Integer ustKontoIId[] = new Integer[1];
		Integer skontoKontoIId[] = new Integer[1];
		BigDecimal skontoBetrag = new BigDecimal(0);
		BigDecimal skontoUstBetrag = new BigDecimal(0);
		BigDecimal skontoProzent = new BigDecimal(0);
		BigDecimal[] skontoBetragTeil = null;
		BigDecimal[] skontoUstBetragTeil = null;
		BigDecimal kursDifferenzBetrag = new BigDecimal(0);
		Integer steuerkategorieIId = null;
		if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_BEZAHLT)) {
			try {
				BigDecimal betrag = rechnungDto.getNWert().add(
						rechnungDto.getNWertust());
				BigDecimal betragBezahlt = getRechnungFac()
						.getBereitsBezahltWertVonRechnung(rechnungDto.getIId(),
								null).add(
								getRechnungFac()
										.getBereitsBezahltWertVonRechnungUst(
												rechnungDto.getIId(), null));
				if (rechnungDto.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
					// anzahlungen beruecksichtigen
					betragBezahlt = betragBezahlt.add(getRechnungFac()
							.getAnzahlungenZuSchlussrechnungBrutto(
									rechnungDto.getIId()));
				}
				BigDecimal kursDifferenz = rechnungDto.getNKurs().subtract(
						zahlungDto.getNKurs());
				if (kursDifferenz.doubleValue() != 0) {
					kursDifferenzBetrag = zahlungDto
							.getNBetragfw()
							.add(zahlungDto.getNBetragUstfw())
							.divide(rechnungDto.getNKurs(),
									FinanzFac.NACHKOMMASTELLEN,
									BigDecimal.ROUND_HALF_EVEN);
					kursDifferenzBetrag = kursDifferenzBetrag.subtract(
							zahlungDto.getNBetrag().add(
									zahlungDto.getNBetragUst())).negate();
				}
				BigDecimal kursDifferenzGesamt = getRechnungFac()
						.getBezahltKursdifferenzBetrag(rechnungDto.getIId(),
								rechnungDto.getNKurs());

				if (bBucheMitSkonto) {
					// Achtung: hier nochmals runden, da es aus frueheren Fehler
					// noch 4 stellige Betraege in der DB gibt!
					skontoBetrag = Helper.rundeKaufmaennisch(
							betrag.subtract(betragBezahlt).subtract(
									kursDifferenzGesamt),
							FinanzFac.NACHKOMMASTELLEN);
					skontoProzent = skontoBetrag.divide(betrag, 4,
							BigDecimal.ROUND_HALF_EVEN);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			if (skontoBetrag.doubleValue() != 0) {
				bMitSkonto = true;

				FibuexportDto[] exportDaten = getExportDatenRechnung(
						rechnungDto, theClientDto);

				steuerkategorieIId = reverseCharge ?
						exportDaten[0].getKontoDto().getSteuerkategorieIIdReverse() :
						exportDaten[0].getKontoDto().getSteuerkategorieIId();

				if (exportDaten.length > 2) {
					bMehrfachSteuersatz = true;
					// aufteilen
					skontoBetragTeil = new BigDecimal[exportDaten.length - 1];
					skontoUstBetragTeil = new BigDecimal[exportDaten.length - 1];
					ustKontoIId = new Integer[exportDaten.length - 1];
					skontoKontoIId = new Integer[exportDaten.length - 1];
					int iMax = 0;
					BigDecimal skontobetragmax = new BigDecimal(0);
					BigDecimal skontobetragsumme = new BigDecimal(0);
					BigDecimal skontoustsumme = new BigDecimal(0);
					for (int i = 1; i < exportDaten.length; i++) {
						skontoBetragTeil[i - 1] = Helper.rundeKaufmaennisch(
								exportDaten[i].getHabenbetragBD().multiply(
										skontoProzent),
								FinanzFac.NACHKOMMASTELLEN);
						BigDecimal skontoUst = Helper.rundeKaufmaennisch(
								exportDaten[i].getSteuerBD().multiply(
										skontoProzent),
								FinanzFac.NACHKOMMASTELLEN);
						if(reverseCharge) {
							skontoBetragTeil[i - 1] = skontoBetragTeil[i - 1].add(skontoUst);
							skontoUstBetragTeil[i - 1] = BigDecimal.ZERO;
						} else {
							skontoUstBetragTeil[i - 1] = skontoUst;
						}
						if ((skontoBetragTeil[i - 1].subtract(skontobetragmax))
								.doubleValue() > 0) {
							skontobetragmax = skontoBetragTeil[i - 1];
							iMax = i - 1;
						}
						skontobetragsumme = skontobetragsumme
								.add(skontoBetragTeil[i - 1]);
						skontoustsumme = skontoustsumme
								.add(skontoUstBetragTeil[i - 1]);
						if (exportDaten[i].getMwstsatz().getIIMwstsatzbezId() != null) {
							Integer mwstSatzbezIId = exportDaten[i]
									.getMwstsatz().getIIMwstsatzbezId();
							Steuerkategoriekonto stkk = getSteuerkategoriekonto(
									steuerkategorieIId, mwstSatzbezIId);
							if (stkk != null) {
								ustKontoIId[i - 1] = stkk.getKontoIIdVk();
								skontoKontoIId[i - 1] = stkk
										.getKontoIIdSkontoVk();
							} else {
								// wenn nicht ueber Steuerkategorie definiert,
								// dann alte Methode anwenden
								ustKontoIId[i - 1] = exportDaten[i]
										.getKontoDto()
										.getKontoIIdWeiterfuehrendUst();
								skontoKontoIId[i - 1] = exportDaten[i]
										.getKontoDto()
										.getKontoIIdWeiterfuehrendSkonto();
								if (skontoKontoIId[i - 1] == null)
									skontoKontoIId[i - 1] = getSkontoKonto(
											debitorenKontoDto
													.getSteuerkategorieIId(),
											ustKontoIId[i - 1]);
							}
						}
						// Pruefung wirft entspechende Exception
						pruefeSteuerSkontoKonten(rechnungDto.getBelegartCNr(),
								rechnungDto.getCNr(), ustKontoIId[i - 1],
								skontoKontoIId[i - 1], steuerkategorieIId,
								exportDaten[i]);

						if (skontoUstBetragTeil[i - 1].doubleValue() != 0)
							mehrfachSteuerBuchungen++;
					}
					BigDecimal diff = skontoBetrag.subtract(skontobetragsumme)
							.subtract(skontoustsumme);
					if (diff.doubleValue() != 0) {
						// hoechsten korrigieren
						skontoBetragTeil[iMax] = skontoBetragTeil[iMax]
								.add(diff);
						// ? muss die Steuer auch korrigiert werden?
					}
				} else {
					Integer mwstSatzbezIId = exportDaten[1].getMwstsatz()
							.getIIMwstsatzbezId();
					Steuerkategoriekonto stkk = getSteuerkategoriekonto(
							steuerkategorieIId, mwstSatzbezIId);
					if (stkk != null) {
						ustKontoIId[0] = stkk.getKontoIIdVk();
						skontoKontoIId[0] = stkk.getKontoIIdSkontoVk();
					} else {
						ustKontoIId[0] = exportDaten[1].getKontoDto()
								.getKontoIIdWeiterfuehrendUst();
						skontoKontoIId[0] = exportDaten[1].getKontoDto()
								.getKontoIIdWeiterfuehrendSkonto();
						if (skontoKontoIId[0] == null)
							skontoKontoIId[0] = getSkontoKonto(
									debitorenKontoDto.getSteuerkategorieIId(),
									ustKontoIId[0]);
					}
					// Pruefung wirft entspechende Exception
					pruefeSteuerSkontoKonten(rechnungDto.getRechnungartCNr(),
							rechnungDto.getCNr(), ustKontoIId[0],
							skontoKontoIId[0], steuerkategorieIId,
							exportDaten[1]);

					if(!reverseCharge) {
						BigDecimal mwst = new BigDecimal(exportDaten[1]
								.getMwstsatz().getFMwstsatz());
						mwst = mwst.movePointLeft(2);
						skontoUstBetrag = skontoBetrag.multiply(mwst).divide(
								new BigDecimal(1).add(mwst),
								FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN);
					}
				}
			}
		}
		BuchungdetailDto[] details;
		if (bMitSkonto) {
			if (bMehrfachSteuersatz) {
				details = new BuchungdetailDto[2 + 2 * skontoBetragTeil.length
						+ mehrfachSteuerBuchungen];
				// SOLL SKONTO Buchung am Skonto Konto
				int j = 2 + skontoBetragTeil.length;
				for (int i = 0; i < skontoBetragTeil.length; i++) {
					details[j] = new BuchungdetailDto();
					details[j].setKontoIId(skontoKontoIId[i]);
					// !! entgegen der Buchungsregel soll lt. wh hier eine HABEN
					// Buchung mit - gemacht werden !!
					// details[j].setBuchungdetailartCNr(BuchenFac.SollBuchung);
					// details[j].setNBetrag(skontoBetragTeil[i]);
					details[j].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					details[j].setNBetrag(skontoBetragTeil[i].negate());
					details[j].setNUst(skontoUstBetragTeil[i].negate());
					details[j].setKontoIIdGegenkonto(debitorenKontoIId);
					j++;
					if (skontoUstBetragTeil[i].doubleValue() != 0) {
						// Ust Korrekturbuchung
						details[j] = new BuchungdetailDto();
						details[j].setKontoIId(ustKontoIId[i]);
						// !! entgegen der Buchungsregel soll lt. wh hier eine
						// HABEN Buchung mit - gemacht werden !!
						// details[j].setBuchungdetailartCNr(BuchenFac.SollBuchung);
						// details[j].setNBetrag(skontoUstBetragTeil[i]);
						details[j]
								.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
						details[j].setNBetrag(skontoUstBetragTeil[i].negate());
						details[j].setNUst(new BigDecimal(0));
						details[j].setKontoIIdGegenkonto(debitorenKontoIId);
						j++;
					}
				}
			} else {
				if (skontoUstBetrag.doubleValue() != 0)
					details = new BuchungdetailDto[5];
				else
					details = new BuchungdetailDto[4];
				// SOLL SKONTO Buchung am Skonto Konto
				details[2] = new BuchungdetailDto();
				details[2].setKontoIId(skontoKontoIId[0]);
				// !! entgegen der Buchungsregel soll lt. wh hier eine HABEN
				// Buchung mit - gemacht werden !!
				// details[2].setBuchungdetailartCNr(BuchenFac.SollBuchung);
				// details[2].setNBetrag(skontoBetrag.subtract(skontoUstBetrag));
				details[2].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
				details[2].setNBetrag(skontoBetrag.subtract(skontoUstBetrag)
						.negate());
				details[2].setNUst(skontoUstBetrag);
				details[2].setKontoIIdGegenkonto(debitorenKontoIId);
				if (skontoUstBetrag.doubleValue() != 0) {
					// Ust Korrekturbuchung
					details[4] = new BuchungdetailDto();
					details[4].setKontoIId(ustKontoIId[0]);
					// !! entgegen der Buchungsregel soll lt. wh hier eine HABEN
					// Buchung mit - gemacht werden !!
					// details[3].setBuchungdetailartCNr(BuchenFac.SollBuchung);
					// details[3].setNBetrag(skontoUstBetrag);
					details[4].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					details[4].setNBetrag(skontoUstBetrag.negate());
					details[4].setNUst(new BigDecimal(0));
					details[4].setKontoIIdGegenkonto(debitorenKontoIId);
				}
			}
		} else {
			details = new BuchungdetailDto[2];
		}
		// SOLL Buchung Bank
		details[0] = new BuchungdetailDto();
		details[0].setKontoIId(sollKontoIId);
		details[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		details[0].setNBetrag(zahlungDto.getNBetrag().add(
				zahlungDto.getNBetragUst()));
		details[0].setNUst(zahlungDto.getNBetragUst());
		details[0].setKontoIIdGegenkonto(debitorenKontoIId);
		// HABEN Buchung am Forderungen Debitor
		if (bMitSkonto) {
			// mit Skonto Buchung aufsplitten
			details[1] = new BuchungdetailDto();
			details[1].setKontoIId(debitorenKontoIId);
			details[1].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			if (bMehrfachSteuersatz) {
				for (int i = 0; i < skontoBetragTeil.length; i++) {
					details[i + 2] = new BuchungdetailDto();
					details[i + 2].setKontoIId(debitorenKontoIId);
					details[i + 2]
							.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					details[i + 2].setKontoIIdGegenkonto(skontoKontoIId[0]);
					details[i + 2].setNBetrag(skontoBetragTeil[i]
							.add(skontoUstBetragTeil[i]));
					details[i + 2].setNUst(skontoUstBetragTeil[i]);
				}
			} else {
				details[3] = new BuchungdetailDto();
				details[3].setKontoIId(debitorenKontoIId);
				details[3].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
				details[3].setKontoIIdGegenkonto(skontoKontoIId[0]);
				details[3].setNBetrag(skontoBetrag);
				details[3].setNUst(skontoUstBetrag);
			}
		} else {
			details[1] = new BuchungdetailDto();
			details[1].setKontoIId(debitorenKontoIId);
			details[1].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		}
		details[1].setNBetrag(zahlungDto.getNBetrag().add(
				zahlungDto.getNBetragUst()));
		// Zahlung ohne UST
		details[1].setNUst(new BigDecimal(0)); // zahlungDto.getNBetragUst());
		details[1].setKontoIIdGegenkonto(sollKontoIId);

		if (kursDifferenzBetrag.doubleValue() != 0) {
			if (steuerkategorieIId == null) {
				// dann aus Debitor
				steuerkategorieIId = debitorenKontoDto.getSteuerkategorieIId();
			}
			Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class,
					steuerkategorieIId);
			Integer kontoKursIId = null;
			boolean bVerlust = false;
			if (kursDifferenzBetrag.doubleValue() < 0) {
				kontoKursIId = steuerkategorie.getKontoIIdKursverlust();
				if (kontoKursIId == null)
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO,
							"Kein Konto fuer Kursverlust."
									+ rechnungDto.getRechnungartCNr() + ": "
									+ rechnungDto.getCNr() + ", "
									+ "Steuerkategorie: "
									+ steuerkategorie.getCNr().trim());
				bVerlust = true;
			} else {
				kontoKursIId = steuerkategorie.getKontoIIdKursgewinn();
				if (kontoKursIId == null)
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO,
							"Kein Konto fuer Kursgewinn."
									+ rechnungDto.getRechnungartCNr() + ": "
									+ rechnungDto.getCNr() + ", "
									+ "Steuerkategorie: "
									+ steuerkategorie.getCNr().trim());
			}
			details = Arrays.copyOf(details, details.length + 2);
			BuchungdetailDto detailDiff = new BuchungdetailDto();
			detailDiff.setKontoIId(kontoKursIId);
			detailDiff.setKontoIIdGegenkonto(debitorenKontoIId);
			detailDiff.setNBetrag(kursDifferenzBetrag.abs());
			detailDiff.setNUst(new BigDecimal(0));
			if (bVerlust)
				detailDiff.setBuchungdetailartCNr(BuchenFac.SollBuchung);
			else
				detailDiff.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			details[details.length - 2] = detailDiff;
			BuchungdetailDto detailDiffGegen = new BuchungdetailDto();
			detailDiffGegen.setKontoIId(debitorenKontoIId);
			detailDiffGegen.setKontoIIdGegenkonto(kontoKursIId);
			detailDiffGegen.setNBetrag(kursDifferenzBetrag.abs());
			detailDiffGegen.setNUst(new BigDecimal(0));
			details[details.length - 1] = detailDiffGegen;
			if (bVerlust)
				detailDiffGegen.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			else
				detailDiffGegen.setBuchungdetailartCNr(BuchenFac.SollBuchung);
		}

		for (int i = 0; i < details.length; i++)
			details[i].setIAuszug(zahlungDto.getIAuszug());
		return details;
	}

	private FibuexportDto[] getExportDatenRechnung(RechnungDto rechnungDto,
			TheClientDto theClientDto) {
		// ermitteln der beteiligten Steuersaetze ueber den Exportmanager
		FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
		fibuExportKriterienDto.setSBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
		fibuExportKriterienDto.setDStichtag(new Date(rechnungDto
				.getTBelegdatum().getTime()));

		FibuExportManager manager = FibuExportManagerFactory
				.getFibuExportManager(getExportVariante(theClientDto),
						getExportFormat(theClientDto), fibuExportKriterienDto,
						theClientDto);
		FibuexportDto[] exportDaten = manager.getExportdatenRechnung(
				rechnungDto.getIId(), null);
		return exportDaten;
	}

	private void pruefeSteuerSkontoKonten(String beleg, String belegCNr,
			Integer ustKontoIId, Integer skontoKontoIId,
			Integer steuerkategorieIId, FibuexportDto exportDaten) {
		String mwstbezeichung = null;

		if (ustKontoIId == null
				&& exportDaten.getSteuerBD().compareTo(new BigDecimal(0)) != 0) {
			Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class,
					steuerkategorieIId);
			mwstbezeichung = getMwstBezeichnung(exportDaten);
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_KEIN_STEUERKONTO,
					"Kein Steuerkonto definiert. " + beleg + ": " + belegCNr
							+ ", " + "Steuerkategorie: "
							+ steuerkategorie.getCBez() + ", " + "Steuersatz: "
							+ mwstbezeichung);
		}

		if (skontoKontoIId == null) {
			Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class,
					steuerkategorieIId);
			mwstbezeichung = getMwstBezeichnung(exportDaten);
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_KEIN_SKONTOKONTO,
					"Kein Skontokonto definiert. " + beleg + ": " + belegCNr
							+ ", " + "Steuerkategorie: "
							+ steuerkategorie.getCBez() + ", " + "Steuersatz: "
							+ mwstbezeichung);
		}
	}

	private String getMwstBezeichnung(FibuexportDto exportDaten) {
		String mwstbezeichung;
		if (exportDaten.getMwstsatz().getMwstsatzbezDto() == null) {
			Mwstsatzbez mwstbez = em.find(Mwstsatzbez.class, exportDaten
					.getMwstsatz().getIIMwstsatzbezId());
			if (mwstbez != null)
				mwstbezeichung = mwstbez.getCBezeichnung();
			else
				mwstbezeichung = "?";
		} else
			mwstbezeichung = exportDaten.getMwstsatz().getMwstsatzbezDto()
					.getCBezeichnung();
		return mwstbezeichung;
	}

	/**
	 * Ermitteln der Buchungsdetails zum Verbuchen einer Zahlung.
	 * 
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return BuchungdetailDto[]
	 * @throws EJBExceptionLP
	 */
	private BuchungdetailDto[] getBuchungdetailsVonZahlungEr(
			EingangsrechnungzahlungDto zahlungDto,
			EingangsrechnungDto rechnungDto, boolean bBucheMitSkonto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer habenKontoIId = null;

		LieferantDto lieferantDto = getLieferantFac()
				.lieferantFindByPrimaryKey(rechnungDto.getLieferantIId(),
						theClientDto);
		if (zahlungDto.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BANK)) {
			BankverbindungDto bankverbindungDto = null;
			try {
				if (zahlungDto.getBankverbindungIId() != null) {
					bankverbindungDto = getFinanzFac()
							.bankverbindungFindByPrimaryKey(
									zahlungDto.getBankverbindungIId());
					habenKontoIId = bankverbindungDto.getKontoIId();
				}
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_BAR)) {
			KassenbuchDto kassenbuchDto = null;
			try {
				if (zahlungDto.getKassenbuchIId() != null) {
					kassenbuchDto = getFinanzFac().kassenbuchFindByPrimaryKey(
							zahlungDto.getKassenbuchIId(), theClientDto);
					habenKontoIId = kassenbuchDto.getKontoIId();
				}
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
			Kunde kunde = null;
			try {
				Rechnungzahlung rechnungzahlung = em.find(
						Rechnungzahlung.class,
						zahlungDto.getRechnungzahlungIId());
				Rechnung rechnung = em.find(Rechnung.class,
						rechnungzahlung.getRechnungIId());
				kunde = em.find(Kunde.class, rechnung.getKundeIId());
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
			habenKontoIId = kunde.getKontoIIdDebitorenkonto();
		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
			if (zahlungDto.getBuchungdetailIId() == null)
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"BuchungdetailDto=null in Vorauszahlung");
			try {
				Buchungdetail bd = em.find(Buchungdetail.class,
						zahlungDto.getBuchungdetailIId());
				habenKontoIId = bd.getKontoIId();
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		}

		KontoDto habenKontoDto = null;
		try {
			habenKontoDto = getFinanzFac().kontoFindByPrimaryKey(habenKontoIId);
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}
		Integer kreditorenKontoIId = lieferantDto.getKontoIIdKreditorenkonto();
		KontoDto kreditorenKontoDto = null;
		try {
			kreditorenKontoDto = getFinanzFac().kontoFindByPrimaryKey(
					kreditorenKontoIId);
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}
		// es muessen alle Konten definiert sein
		if (kreditorenKontoIId == null || habenKontoIId == null) {
			// ist eines der konten nicht definiert, so braucht die Zahlung
			// nicht verbucht werden
			return null;
		}
		// jetzt die einzelbuchungen bauen

		// pruefen ob Skonto zu buchen
		boolean bMitSkonto = false;
		boolean bMehrfachSteuersatz = false;
		boolean reverseCharge = Helper.short2boolean(rechnungDto.getBReversecharge());
		int mehrfachSteuerBuchungen = 0;
		Integer vstKontoIId[] = new Integer[1];
		Integer skontoKontoIId[] = new Integer[1];
		BigDecimal skontoBetrag = new BigDecimal(0);
		BigDecimal skontoVstBetrag = new BigDecimal(0);
		BigDecimal skontoProzent = new BigDecimal(0);
		BigDecimal[] skontoBetragTeil = null;
		BigDecimal[] skontoVstBetragTeil = null;
		BigDecimal kursDifferenzBetrag = new BigDecimal(0);
		Integer steuerkategorieIId = null;
		if (rechnungDto.getStatusCNr().equals(
				EingangsrechnungFac.STATUS_ERLEDIGT)) {
			try {
				BigDecimal betrag = rechnungDto.getNBetrag();
				BigDecimal betragBezahlt = getEingangsrechnungFac()
						.getBezahltBetrag(rechnungDto.getIId(), null);
				BigDecimal kursDifferenz = new BigDecimal(1).divide(
						rechnungDto.getNKurs(), 6, BigDecimal.ROUND_HALF_EVEN);
				kursDifferenz = kursDifferenz.subtract(zahlungDto.getNKurs());
				if (kursDifferenz.doubleValue() != 0) {
					kursDifferenzBetrag = Helper.rundeKaufmaennisch(zahlungDto
							.getNBetragfw().multiply(rechnungDto.getNKurs()),
							FinanzFac.NACHKOMMASTELLEN);
					kursDifferenzBetrag = kursDifferenzBetrag
							.subtract(zahlungDto.getNBetrag());
				}
				BigDecimal kursDifferenzGesamt = getEingangsrechnungFac()
						.getBezahltKursdifferenzBetrag(rechnungDto.getIId(),
								rechnungDto.getNKurs());

				if (bBucheMitSkonto) {
					// Achtung: hier nochmals runden, da es aus frueheren Fehler
					// noch 4 stellige Betraege in der DB gibt!
					skontoBetrag = Helper.rundeKaufmaennisch(
							betrag.subtract(betragBezahlt).subtract(
									kursDifferenzGesamt),
							FinanzFac.NACHKOMMASTELLEN);
					if (rechnungDto
							.getEingangsrechnungartCNr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG))
						skontoBetrag = skontoBetrag
								.subtract(getEingangsrechnungFac()
										.getAnzahlungenGestelltZuSchlussrechnung(
												rechnungDto.getIId()));
					skontoProzent = skontoBetrag.divide(betrag, 4,
							BigDecimal.ROUND_HALF_EVEN);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			FibuexportDto[] exportDaten = getExportDatenRechnung(rechnungDto,
					theClientDto);

			steuerkategorieIId = reverseCharge ?
					exportDaten[0].getKontoDto().getSteuerkategorieIIdReverse() :
					exportDaten[0].getKontoDto().getSteuerkategorieIId();
			if (skontoBetrag.doubleValue() != 0) {
				bMitSkonto = true;
				if (exportDaten.length > 2) {
					bMehrfachSteuersatz = true;
					// aufteilen
					skontoBetragTeil = new BigDecimal[exportDaten.length - 1];
					skontoVstBetragTeil = new BigDecimal[exportDaten.length - 1];
					vstKontoIId = new Integer[exportDaten.length - 1];
					skontoKontoIId = new Integer[exportDaten.length - 1];
					int iMax = 0;
					BigDecimal skontobetragmax = new BigDecimal(0);
					BigDecimal skontobetragsumme = new BigDecimal(0);
					BigDecimal skontovstsumme = new BigDecimal(0);
					for (int i = 1; i < exportDaten.length; i++) {
						skontoBetragTeil[i - 1] = Helper.rundeKaufmaennisch(
								exportDaten[i].getSollbetragBD().multiply(
										skontoProzent),
								FinanzFac.NACHKOMMASTELLEN);
						BigDecimal skontoVst = Helper.rundeKaufmaennisch(
								exportDaten[i].getSteuerBD().multiply(
										skontoProzent),
								FinanzFac.NACHKOMMASTELLEN);
						if(reverseCharge) {
							skontoBetragTeil[i - 1]= skontoBetragTeil[i - 1].add(skontoVst);
						} else  {
							skontoVstBetragTeil[i - 1] = skontoVst;
						}
						if ((skontoBetragTeil[i - 1].subtract(skontobetragmax))
								.doubleValue() > 0) {
							skontobetragmax = skontoBetragTeil[i - 1];
							iMax = i - 1;
						}
						skontobetragsumme = skontobetragsumme
								.add(skontoBetragTeil[i - 1]);
						skontovstsumme = skontovstsumme
								.add(skontoVstBetragTeil[i - 1]);
						if (exportDaten[i].getMwstsatz().getIIMwstsatzbezId() != null) {
							Integer mwstSatzbezIId = exportDaten[i]
									.getMwstsatz().getIIMwstsatzbezId();
							Steuerkategoriekonto stkk = getSteuerkategoriekonto(
									steuerkategorieIId, mwstSatzbezIId);
							if (stkk != null) {
								vstKontoIId[i - 1] = stkk.getKontoIIdEk();
								skontoKontoIId[i - 1] = stkk
										.getKontoIIdSkontoEk();
							} else {
								// wenn nicht ueber Steuerkategorie definiert,
								// dann alte Methode anwenden
								vstKontoIId[i - 1] = exportDaten[i]
										.getKontoDto()
										.getKontoIIdWeiterfuehrendUst();
								skontoKontoIId[i - 1] = exportDaten[i]
										.getKontoDto()
										.getKontoIIdWeiterfuehrendSkonto();
								if (skontoKontoIId[i - 1] == null)
									skontoKontoIId[i - 1] = getSkontoKontoVst(
											kreditorenKontoDto
													.getSteuerkategorieIId(),
											vstKontoIId[i - 1]);
							}
							// Pruefung wirft entspechende Exception
							pruefeSteuerSkontoKonten(
									rechnungDto.getEingangsrechnungartCNr(),
									rechnungDto.getCNr(), vstKontoIId[i - 1],
									skontoKontoIId[i - 1], steuerkategorieIId,
									exportDaten[i]);
						}
						if (skontoVstBetragTeil[i - 1].doubleValue() != 0)
							mehrfachSteuerBuchungen++;
					}
					BigDecimal diff = skontoBetrag.subtract(skontobetragsumme)
							.subtract(skontovstsumme);
					if (diff.doubleValue() != 0) {
						// hoechsten korrigieren
						skontoBetragTeil[iMax] = skontoBetragTeil[iMax]
								.add(diff);
						// ? muss die Steuer auch korrigiert werden?
					}
				} else {
					Integer mwstSatzbezIId = exportDaten[1].getMwstsatz()
							.getIIMwstsatzbezId();
					Steuerkategoriekonto stkk = getSteuerkategoriekonto(
							steuerkategorieIId, mwstSatzbezIId);
					if (stkk != null) {
						vstKontoIId[0] = stkk.getKontoIIdEk();
						skontoKontoIId[0] = stkk.getKontoIIdSkontoEk();
					} else {
						vstKontoIId[0] = exportDaten[1].getKontoDto()
								.getKontoIIdWeiterfuehrendUst();
						skontoKontoIId[0] = exportDaten[1].getKontoDto()
								.getKontoIIdWeiterfuehrendSkonto();
						if (skontoKontoIId[0] == null)
							skontoKontoIId[0] = getSkontoKontoVst(
									kreditorenKontoDto.getSteuerkategorieIId(),
									vstKontoIId[0]);
					}
					// Pruefung wirft entspechende Exception
					pruefeSteuerSkontoKonten(
							rechnungDto.getEingangsrechnungartCNr(),
							rechnungDto.getCNr(), vstKontoIId[0],
							skontoKontoIId[0], steuerkategorieIId,
							exportDaten[1]);

					if (!reverseCharge) {
						BigDecimal mwst = new BigDecimal(exportDaten[1]
								.getMwstsatz().getFMwstsatz());
						mwst = mwst.movePointLeft(2);
						skontoVstBetrag = skontoBetrag.multiply(mwst).divide(
								new BigDecimal(1).add(mwst),
								FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN);
					}
				}
			}
		}
		BuchungdetailDto[] details;
		if (bMitSkonto) {
			if (bMehrfachSteuersatz) {
				details = new BuchungdetailDto[2 + 2 * skontoBetragTeil.length
						+ mehrfachSteuerBuchungen];
				// SOLL SKONTO Buchung am Skonto Konto
				int j = 2 + skontoBetragTeil.length;
				for (int i = 0; i < skontoBetragTeil.length; i++) {
					details[j] = new BuchungdetailDto();
					details[j].setKontoIId(skontoKontoIId[i]);
					// !! entgegen der Buchungsregel soll lt. wh hier eine
					// SOLL
					// Buchung mit - gemacht werden !!
					// details[j].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					// details[j].setNBetrag(skontoBetragTeil[i]);
					details[j].setBuchungdetailartCNr(BuchenFac.SollBuchung);
					details[j].setNBetrag(skontoBetragTeil[i].negate());
					details[j].setNUst(skontoVstBetragTeil[i].negate());
					details[j].setKontoIIdGegenkonto(kreditorenKontoIId);
					j++;
					if (skontoVstBetragTeil[i].doubleValue() != 0) {
						// Ust Korrekturbuchung
						details[j] = new BuchungdetailDto();
						details[j].setKontoIId(vstKontoIId[i]);
						// !! entgegen der Buchungsregel soll lt. wh hier
						// eine
						// SOLL Buchung mit - gemacht werden !!
						// details[j].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
						// details[j].setNBetrag(skontoUstBetragTeil[i]);
						details[j]
								.setBuchungdetailartCNr(BuchenFac.SollBuchung);
						details[j].setNBetrag(skontoVstBetragTeil[i].negate());
						details[j].setNUst(new BigDecimal(0));
						details[j].setKontoIIdGegenkonto(kreditorenKontoIId);
						j++;
					}
				}
			} else {
				if (skontoVstBetrag.doubleValue() != 0)
					details = new BuchungdetailDto[5];
				else
					details = new BuchungdetailDto[4];
				// SOLL SKONTO Buchung am Skonto Konto
				details[2] = new BuchungdetailDto();
				details[2].setKontoIId(skontoKontoIId[0]);
				// !! entgegen der Buchungsregel soll lt. wh hier eine SOLL
				// Buchung mit - gemacht werden !!
				// details[2].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
				// details[2].setNBetrag(skontoBetrag.subtract(skontoUstBetrag));
				details[2].setBuchungdetailartCNr(BuchenFac.SollBuchung);
				details[2].setNBetrag(skontoBetrag.subtract(skontoVstBetrag)
						.negate());
				details[2].setNUst(skontoVstBetrag);
				details[2].setKontoIIdGegenkonto(kreditorenKontoIId);
				if (skontoVstBetrag.doubleValue() != 0) {
					// Ust Korrekturbuchung
					details[3] = new BuchungdetailDto();
					details[3].setKontoIId(vstKontoIId[0]);
					// !! entgegen der Buchungsregel soll lt. wh hier eine
					// SOLL
					// Buchung mit - gemacht werden !!
					// details[3].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
					// details[3].setNBetrag(skontoUstBetrag);
					details[3].setBuchungdetailartCNr(BuchenFac.SollBuchung);
					details[3].setNBetrag(skontoVstBetrag.negate());
					details[3].setNUst(new BigDecimal(0));
					details[3].setKontoIIdGegenkonto(kreditorenKontoIId);
				}

			}
		} else {
			details = new BuchungdetailDto[2];
		}
		// HABEN Buchung Bank
		details[0] = new BuchungdetailDto();
		details[0].setKontoIId(habenKontoIId);
		details[0].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		details[0].setNBetrag(zahlungDto.getNBetrag());
		details[0].setNUst(zahlungDto.getNBetragust());
		details[0].setKontoIIdGegenkonto(kreditorenKontoIId);
		// SOLL Buchung am Verbindlichkeiten Kreditor
		if (bMitSkonto) {
			// mit Skonto Buchung aufsplitten
			details[1] = new BuchungdetailDto();
			details[1].setKontoIId(kreditorenKontoIId);
			details[1].setBuchungdetailartCNr(BuchenFac.SollBuchung);
			if (bMehrfachSteuersatz) {
				for (int i = 0; i < skontoBetragTeil.length; i++) {
					details[i + 2] = new BuchungdetailDto();
					details[i + 2].setKontoIId(kreditorenKontoIId);
					details[i + 2]
							.setBuchungdetailartCNr(BuchenFac.SollBuchung);
					details[i + 2].setKontoIIdGegenkonto(skontoKontoIId[0]);
					details[i + 2].setNBetrag(skontoBetragTeil[i]
							.add(skontoVstBetragTeil[i]));
					details[i + 2].setNUst(skontoVstBetragTeil[i]);
				}
			} else {
				if (skontoVstBetrag.doubleValue() != 0) {
					details[4] = new BuchungdetailDto();
					details[4].setKontoIId(kreditorenKontoIId);
					details[4].setBuchungdetailartCNr(BuchenFac.SollBuchung);
					details[4].setKontoIIdGegenkonto(skontoKontoIId[0]);
					details[4].setNBetrag(skontoBetrag);
					details[4].setNUst(skontoVstBetrag);
				} else {
					details[3] = new BuchungdetailDto();
					details[3].setKontoIId(kreditorenKontoIId);
					details[3].setBuchungdetailartCNr(BuchenFac.SollBuchung);
					details[3].setKontoIIdGegenkonto(skontoKontoIId[0]);
					details[3].setNBetrag(skontoBetrag);
					details[3].setNUst(skontoVstBetrag);
				}
			}
		} else {
			details[1] = new BuchungdetailDto();
			details[1].setKontoIId(kreditorenKontoIId);
			details[1].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		}
		details[1].setNBetrag(zahlungDto.getNBetrag());
		// Zahlung ohne UST
		details[1].setNUst(new BigDecimal(0)); // zahlungDto.getNBetragUst());
		details[1].setKontoIIdGegenkonto(habenKontoIId);
		if (kursDifferenzBetrag.doubleValue() != 0) {
			Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class,
					steuerkategorieIId);
			Integer kontoKursIId = null;
			boolean bVerlust = false;
			if (kursDifferenzBetrag.doubleValue() < 0) {
				kontoKursIId = steuerkategorie.getKontoIIdKursverlust();
				if (kontoKursIId == null)
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO,
							"Kein Konto fuer Kursverlust."
									+ rechnungDto.getEingangsrechnungartCNr()
									+ ": " + rechnungDto.getCNr() + ", "
									+ "Steuerkategorie: "
									+ steuerkategorie.getCNr().trim());
				bVerlust = true;
			} else {
				kontoKursIId = steuerkategorie.getKontoIIdKursgewinn();
				if (kontoKursIId == null)
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO,
							"Kein Konto fuer Kursgewinn."
									+ rechnungDto.getEingangsrechnungartCNr()
									+ ": " + rechnungDto.getCNr() + ", "
									+ "Steuerkategorie: "
									+ steuerkategorie.getCNr().trim());
			}
			details = Arrays.copyOf(details, details.length + 2);
			BuchungdetailDto detailDiff = new BuchungdetailDto();
			detailDiff.setKontoIId(kontoKursIId);
			detailDiff.setKontoIIdGegenkonto(kreditorenKontoIId);
			detailDiff.setNBetrag(kursDifferenzBetrag.abs());
			detailDiff.setNUst(new BigDecimal(0));
			if (bVerlust)
				detailDiff.setBuchungdetailartCNr(BuchenFac.SollBuchung);
			else
				detailDiff.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			details[details.length - 2] = detailDiff;
			BuchungdetailDto detailDiffGegen = new BuchungdetailDto();
			detailDiffGegen.setKontoIId(kreditorenKontoIId);
			detailDiffGegen.setKontoIIdGegenkonto(kontoKursIId);
			detailDiffGegen.setNBetrag(kursDifferenzBetrag.abs());
			detailDiffGegen.setNUst(new BigDecimal(0));
			details[details.length - 1] = detailDiffGegen;
			if (bVerlust)
				detailDiffGegen.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			else
				detailDiffGegen.setBuchungdetailartCNr(BuchenFac.SollBuchung);
		}
		for (int i = 0; i < details.length; i++)
			details[i].setIAuszug(zahlungDto.getIAuszug());
		return details;
	}

	protected Steuerkategoriekonto getSteuerkategoriekonto(Integer stkIId,
			Integer mwstSatzbezIId) {
		SteuerkategoriekontoPK pk = new SteuerkategoriekontoPK();
		pk.setSteuerkategorieiid(stkIId);
		pk.setMwstsatzbeziid(mwstSatzbezIId);
		Steuerkategoriekonto stkk = em.find(Steuerkategoriekonto.class, pk);
		return stkk;
	}

	@SuppressWarnings("unchecked")
	private Integer getSkontoKonto(Integer steuerkategorieIId,
			Integer ustKontoIId) {
		Query query = em
				.createNamedQuery("SteuerkategoriekontoBySteuerkategorieIId");
		query.setParameter(1, steuerkategorieIId);
		List<Steuerkategoriekonto> list = query.getResultList();
		Iterator<Steuerkategoriekonto> iter = list.iterator();
		while (iter.hasNext()) {
			Steuerkategoriekonto sk = iter.next();
			if (sk.getKontoIIdVk() != null)
				if (sk.getKontoIIdVk().intValue() == ustKontoIId.intValue())
					return sk.getKontoIIdSkontoVk();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Integer getSkontoKontoVst(Integer steuerkategorieIId,
			Integer vstKontoIId) {
		Query query = em
				.createNamedQuery("SteuerkategoriekontoBySteuerkategorieIId");
		query.setParameter(1, steuerkategorieIId);
		List<Steuerkategoriekonto> list = query.getResultList();
		Iterator<Steuerkategoriekonto> iter = list.iterator();
		while (iter.hasNext()) {
			Steuerkategoriekonto sk = iter.next();
			if (sk.getKontoIIdVk() != null)
				if (sk.getKontoIIdEk().intValue() == vstKontoIId.intValue())
					return sk.getKontoIIdSkontoEk();
		}
		return null;
	}

	/**
	 * Ermitteln der Buchungsdetails zum Verbuchen einer Gutschrift.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return BuchungdetailDto[]
	 * @throws EJBExceptionLP
	 */
	private BuchungdetailDto[] getBuchungdetailsVonGutschrift(
			Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		RechnungDto rechnungDto = null;
		KundeDto kundeDto = null;
		try {
			rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		Integer debitorenKontoIId = kundeDto.getIidDebitorenkonto();
		Integer erloesberichtigungenKontoIId = kundeDto.getIidErloeseKonto();
		KontoDto erloesberichtigungenKontoDto = null;
		try {
			erloesberichtigungenKontoDto = getFinanzFac()
					.kontoFindByPrimaryKey(erloesberichtigungenKontoIId);
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}
		Integer ustKontoIId = erloesberichtigungenKontoDto
				.getKontoIIdWeiterfuehrendUst();
		// es muessen alle Konten definiert sein
		if (debitorenKontoIId == null || erloesberichtigungenKontoIId == null
				|| ustKontoIId == null) {
			// sind nicht alle Konten definiert, so braucht die gutschrift nicht
			// verbucht werden
			return null;
		}
		// jetzt die einzelbuchungen bauen

		BuchungdetailDto[] details = new BuchungdetailDto[3];
		BigDecimal zero = new BigDecimal(0);
		// Buchung am Erloesberichtigungskonto
		details[0] = new BuchungdetailDto();
		details[0].setKontoIId(erloesberichtigungenKontoIId);
		details[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		details[0].setNBetrag(rechnungDto.getNWert());
		details[0].setNUst(zero);
		details[0].setKontoIIdGegenkonto(debitorenKontoIId);
		// Buchung am UST-Konto
		details[1] = new BuchungdetailDto();
		details[1].setKontoIId(ustKontoIId);
		details[1].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		details[1].setNBetrag(rechnungDto.getNWertust());
		details[1].setNUst(zero);
		details[1].setKontoIIdGegenkonto(debitorenKontoIId);
		// Buchung am Kundenkonto
		details[2] = new BuchungdetailDto();
		details[2].setKontoIId(debitorenKontoIId);
		details[2].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		details[2].setNBetrag(rechnungDto.getNWert().add(
				rechnungDto.getNWertust()));
		details[2].setNUst(rechnungDto.getNWertust());
		details[2].setKontoIIdGegenkonto(erloesberichtigungenKontoIId);
		return details;
	}

	/**
	 * Verbuchen einer Zahlung einer Rechnung bzw. Gutschrift
	 * 
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 * 
	 *             Anmerkung: Belegbuchung ist in beiden Faellen REZAHLUNG!
	 */
	public BuchungDto verbucheZahlung(Integer zahlungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BuchungDto buchungDto = null;
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			long startTime = System.currentTimeMillis();
			// Zahlung schon verbucht?
			BelegbuchungDto bbDto = null;
			bbDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_REZAHLUNG, zahlungIId);
			if (bbDto != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
						"Zahlung bereits verbucht. ID=" + zahlungIId);
			}

			RechnungzahlungDto zahlungDto = getRechnungzahlung(zahlungIId);
			RechnungDto rechnungDto = getRechnungDto(zahlungDto
					.getRechnungIId());

			boolean bBezahlt = rechnungDto.getStatusCNr().equals(
					RechnungFac.STATUS_BEZAHLT);

			if (bBezahlt) {
				// alle Zahlungen aufrollen und bei einer den Skonto buchen
				RechnungzahlungDto[] zahlungDtos = getRechnungFac()
						.zahlungFindByRechnungIIdAbsteigendSortiert(
								rechnungDto.getIId());
				//alle Zahlungen die:
				// -- Skontobuchungen haben
				// -- spaeteres Zahldatum als die zu buchende Zahlung
				// -- die Zahlung ist die ich buchen will
				List<RechnungzahlungDto> alZahlungen = new ArrayList<RechnungzahlungDto>();
				for (RechnungzahlungDto za : zahlungDtos) {
					if (hatZahlungSkonto(za.getIId(), LocaleFac.BELEGART_REZAHLUNG)
							|| !za.getDZahldatum().before(zahlungDto.getDZahldatum())
							|| za.getIId().equals(zahlungIId)) {
						alZahlungen.add(za);
					}
				}
				// dann wieder verbuchen mit Ruecksicht auf Skonto
				boolean bBucheMitSkonto = true;
				for (RechnungzahlungDto zDto : alZahlungen) {

					if(!zDto.getIId().equals(zahlungIId))
						verbucheZahlungRueckgaengig(zDto.getIId(), theClientDto);
					buchungDto = verbucheZahlungSkonto(bBucheMitSkonto, zDto,
							rechnungDto, theClientDto);
					bBucheMitSkonto = buchungDto == null && bBucheMitSkonto;
						// Skontobuchung ist jetzt
						// drin, restliche ohne
				}
			} else {
				// nur diese Zahlung verbuchen
				buchungDto = verbucheZahlungSkonto(false, zahlungDto,
						rechnungDto, theClientDto);
			}
			long dauer = System.currentTimeMillis() - startTime;
			myLogger.logData("Verbuchen der Zahlung dauerte " + dauer + " ms.");
		}
		return buchungDto;
	}
	
	/**
	 * 
	 * @param zahlungIId
	 * @param belegart LocaleFac.BELEGART_REZAHLUNG oder LocaleFac.BELEGART_ERZAHLUNG
	 * @return true wenn es mindestens eine Skontobuchung f&uuml;r die Zahlung gibt
	 */
	private boolean hatZahlungSkonto(Integer zahlungIId, String belegart) {
		Validator.notNull(zahlungIId, "zahlungIId");
		Validator.notNull(belegart, "belegart");
		
		String kontofeld = belegart.equals(LocaleFac.BELEGART_REZAHLUNG)?"kontoiidskontovk":"kontoiidskontoek";
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query q = session.createQuery("SELECT COUNT(*) " +
				"FROM FLRFinanzBuchungDetail bdetail, FLRFinanzBelegbuchung belegb " +
				"WHERE belegb.flrbuchung.i_id = bdetail.flrbuchung.i_id " +
				"AND belegb.i_belegiid = " + zahlungIId + " " +
				"AND belegb.belegart_c_nr LIKE '" + belegart + "' " +
				"AND bdetail.konto_i_id IN(SELECT stkk." + kontofeld + " FROM FLRSteuerkategoriekonto stkk)");
		Object res = q.uniqueResult();
		return ((Long)res)>0;
		
	}

	protected RechnungDto getRechnungDto(Integer rechnungIId) {
		RechnungDto rechnungDto = null;
		try {
			rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		return rechnungDto;
	}

	private PartnerDto getPartnerDtoMitDebKonto(Integer kundeIId,
			TheClientDto theClientDto) {
		PartnerDto partnerDto = null;
		try {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
					theClientDto);
			if (kundeDto.getIidDebitorenkonto() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEIN_DEBITORENKONTO_DEFINIERT,
						"Kein Debitorenkonto definiert. KundeID="
								+ kundeDto.getIId());
			}
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception ex3) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex3);
		}
		return partnerDto;
	}

	protected RechnungzahlungDto getRechnungzahlung(Integer zahlungIId) {
		RechnungzahlungDto zahlungDto = null;
		try {
			zahlungDto = getRechnungFac().zahlungFindByPrimaryKey(zahlungIId);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		return zahlungDto;
	}

	/**
	 * Verbuchen einer Rechnungs- oder Gutschriftszahlung mit Skonto
	 * 
	 * @param bBucheMitSkonto
	 * @param zahlungDto
	 * @param rechnungDto
	 * @param theClientDto
	 * @return
	 * 
	 *         Anmerkung: Gutschriftszahlung wird nur anhand der Rechnungsart
	 *         erkannt, Betr&auml;ge muessen negiert werden Anmerkung: bei
	 *         Anzahlungsrechnungen werden zus&auml;tzlich die Buchungen auf
	 *         Verrechnungskonto Anzahlung und Erhaltene Anzahlung hinzugefuegt
	 * @throws RemoteException 
	 * @throws EJBExceptionLP 
	 * 
	 */
	protected BuchungDto verbucheZahlungSkonto(boolean bBucheMitSkonto,
			RechnungzahlungDto zahlungDto, RechnungDto rechnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		PartnerDto partnerDto = getPartnerDtoMitDebKonto(
				rechnungDto.getKundeIId(), theClientDto);
		BuchungDto buchungDto = null;
		if (zahlungDto.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BANK)
				|| zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_BAR)
				|| zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {

			buchungDto = new BuchungDto();
			boolean bIstGutschrift = false;
			if (rechnungDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_GUTSCHRIFT)
					|| rechnungDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
				buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_GUTSCHRIFT);
				bIstGutschrift = true;
			} else {
				buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_RECHNUNG);
			}
			if (zahlungDto.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_BANK)
					|| zahlungDto.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG))
				buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG);
			else
				buchungDto
						.setBuchungsartCNr(FinanzFac.BUCHUNGSART_KASSENBUCHUNG);
			buchungDto.setCBelegnummer(rechnungDto.getCNr());
			buchungDto.setCText(partnerDto.getCName1nachnamefirmazeile1());
			buchungDto.setDBuchungsdatum(new Date(zahlungDto.getDZahldatum()
					.getTime()));
			buchungDto.setKostenstelleIId(rechnungDto.getKostenstelleIId());

			BuchungdetailDto[] details = getBuchungdetailsVonZahlung(
					zahlungDto, rechnungDto, bBucheMitSkonto, theClientDto);

			if (bIstGutschrift) {
				// Betraege negieren
				for (int i = 0; i < details.length; i++) {
					details[i].setNBetrag(details[i].getNBetrag().negate());
					details[i].setNUst(details[i].getNUst().negate());
				}
			}
			// nur buchen, wenn die Zahlung auch verbucht werden kann
			if (details != null) {
				try {
					if (rechnungDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_ANZAHLUNG))
						// zusaetzliche Buchungen anhaengen
						details = addAnzahlungZahlungDetails(details,
								rechnungDto, zahlungDto, theClientDto);

					if (zahlungDto.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
						// buchungdetails kommen aus bestehender Umbuchung mit
						// Ausnahme der evt.
						// vorhandenen Waehrungsverlust und Skontobuchungen
						Buchungdetail bd = em.find(Buchungdetail.class,
								zahlungDto.getBuchungdetailIId());
						BuchungdetailDto[] temp = getBuchungDetailsAsNew(bd);
						if (details.length != temp.length) {
							// dies ist eine spezielle Buchung, z.B. Skonto auf Rechnung und keine Bankbuchung
							if (bd.getNBetrag().compareTo(zahlungDto.getNBetrag().add(
									zahlungDto.getNBetragUst())) == 0)
								// nur zulassen wenn der Zahlbetrag gleich dem Buchungsbetrag
								details = temp;
							else
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGER_BETRAG_ZAHLUNG_VORAUSZAHLUNG,"Ungueltiger Betrag bei Zahlung aus Vorauszahlung");
						} else {
							for (int i = 0; i < temp.length; i++) {
								details[i] = temp[i];
								details[i].setNBetrag(zahlungDto.getNBetrag().add(
										zahlungDto.getNBetragUst()));
							}
						}
						// zusaetzlich Vorauszahlung stornieren
						getBuchenFac().storniereBuchung(bd.getBuchungIId(),
								theClientDto);
						// zusaetzlich Rest als Vorauszahlung einbuchen
						bucheDifferenzVorauszahlung(bd.getIId(), zahlungDto
								.getNBetrag().add(zahlungDto.getNBetragUst()),
								theClientDto);
					}
					// Das Verbuchen einer Rechnung muss immer den
					// Buchungsregeln entsprechen
					buchungDto = getBuchenFac().buchen(buchungDto, details,
							true, theClientDto);

					// in Tabelle buchungRechnung speichern
					BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
					belegbuchungDto
							.setBelegartCNr(LocaleFac.BELEGART_REZAHLUNG);
					belegbuchungDto.setBuchungIId(buchungDto.getIId());
					belegbuchungDto.setIBelegiid(zahlungDto.getIId());
					createBelegbuchung(belegbuchungDto, theClientDto);
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}

		} else if (zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
			BuchungdetailDto[] details = getBuchungdetailsVonZahlung(
					zahlungDto, rechnungDto, bBucheMitSkonto, theClientDto);
			buchungDto = new BuchungDto();
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_RECHNUNG);
			buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG);
			buchungDto.setCBelegnummer(rechnungDto.getCNr());
			buchungDto.setCText(partnerDto.getCName1nachnamefirmazeile1());
			buchungDto.setDBuchungsdatum(new Date(zahlungDto.getDZahldatum()
					.getTime()));
			buchungDto.setKostenstelleIId(rechnungDto.getKostenstelleIId());
			if (details != null) {
				try {
					// Das Verbuchen einer Rechnung muss immer den
					// Buchungsregeln entsprechen
					buchungDto = getBuchenFac().buchen(buchungDto, details,
							true, theClientDto);
					// in Tabelle buchungRechnung speichern
					BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
					belegbuchungDto
							.setBelegartCNr(LocaleFac.BELEGART_REZAHLUNG);
					belegbuchungDto.setBuchungIId(buchungDto.getIId());
					belegbuchungDto.setIBelegiid(zahlungDto.getIId());
					createBelegbuchung(belegbuchungDto, theClientDto);
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}
		} else if(zahlungDto.getZahlungsartCNr().equals(
				RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
			if(!bBucheMitSkonto) return null;
			if(!rechnungDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_GUTSCHRIFT)) return null;

			buchungDto = new BuchungDto();
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_GUTSCHRIFT);
//			buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BUCHUNG);
			buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG); 
			buchungDto.setCBelegnummer(rechnungDto.getCNr());
			buchungDto.setCText(partnerDto.getCName1nachnamefirmazeile1());
			buchungDto.setDBuchungsdatum(new Date(zahlungDto.getDZahldatum()
					.getTime()));
			buchungDto.setKostenstelleIId(rechnungDto.getKostenstelleIId());
			
			BuchungdetailDto[] details = getBuchungdetailsVonZahlung(
					zahlungDto, rechnungDto, true, theClientDto);
			List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>(Arrays.asList(details));
			
			if(list.size() > 2) {
				//die ersten zwei sind egal, nur die skontobuchungen sind wichtig
				list.remove(0);
				list.remove(0);
				for(BuchungdetailDto detail : list) {
					detail.setNBetrag(detail.getNBetrag().negate());
				}
			}
			
			buchungDto = getBuchenFac().buchen(buchungDto, list.toArray(new BuchungdetailDto[0]),
					true, theClientDto);
			BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
			belegbuchungDto
					.setBelegartCNr(LocaleFac.BELEGART_REZAHLUNG);
			belegbuchungDto.setBuchungIId(buchungDto.getIId());
			belegbuchungDto.setIBelegiid(zahlungDto.getIId());
			createBelegbuchung(belegbuchungDto, theClientDto);
		}
		return buchungDto;
	}

	private BuchungdetailDto[] addAnzahlungZahlungDetails(
			BuchungdetailDto[] details, RechnungDto rechnungDto,
			RechnungzahlungDto zahlungDto, TheClientDto theClientDto) {
		// folgende Buchunge anhaengen
		// V. Anz an Erh. Anz
		// an Ust
		BuchungdetailDto[] temp;
		boolean bMitUst = false;
		// Achtung: die jetzige Implementierung bucht hier keine Steuer, da laut
		// Werner unsere Anzahlung immer mit Leistung ist,
		// daher schon bei Rechnungsaktivierung die Steuerbuchung erfolgt
		//
		// if (zahlungDto.getNBetragUst().signum()==0)
		temp = new BuchungdetailDto[details.length + 2];
		// else {
		// temp = new BuchungdetailDto[details.length + 3];
		// bMitUst = true;
		// }
		System.arraycopy(details, 0, temp, 0, details.length);
		Konto debitorenkonto = em.find(Konto.class,
				details[0].getKontoIIdGegenkonto());
		FinanzamtPK fpk = new FinanzamtPK(debitorenkonto.getFinanzamtIId(),
				debitorenkonto.getMandantCNr());
		Finanzamt finanzamt = em.find(Finanzamt.class, fpk);
		int i = details.length;
		temp[i] = new BuchungdetailDto();
		temp[i].setBuchungIId(details[0].getBuchungIId());
		temp[i].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		boolean reverseCharge = rechnungDto.isReverseCharge();

		Integer kontoIIdAnzahlungErhaltenVerr = reverseCharge ? finanzamt
				.getKontoIIdRCAnzahlungErhaltenVerr() : finanzamt
				.getKontoIIdAnzahlungErhaltenVerr();
		Integer kontoIIdAnzahlungErhaltenBezahlt = reverseCharge ? finanzamt
				.getKontoIIdRCAnzahlungErhaltenBezahlt() : finanzamt
				.getKontoIIdAnzahlungErhaltenBezahlt();

		temp[i].setKontoIId(kontoIIdAnzahlungErhaltenVerr);
		temp[i].setKontoIIdGegenkonto(kontoIIdAnzahlungErhaltenBezahlt);
		if (bMitUst || reverseCharge) {
			temp[i].setNBetrag(zahlungDto.getNBetrag().add(
					zahlungDto.getNBetragUst()));
			temp[i].setNUst(zahlungDto.getNBetragUst());
		} else {
			temp[i].setNBetrag(zahlungDto.getNBetrag());
			temp[i].setNUst(new BigDecimal(0));
		}
		i++;
		temp[i] = new BuchungdetailDto();
		temp[i].setBuchungIId(details[0].getBuchungIId());
		temp[i].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		temp[i].setKontoIId(kontoIIdAnzahlungErhaltenBezahlt);
		temp[i].setKontoIIdGegenkonto(kontoIIdAnzahlungErhaltenVerr);
		temp[i].setNBetrag(zahlungDto.getNBetrag());
		temp[i].setNUst(zahlungDto.getNBetragUst());
		if (bMitUst) {
			i++;
			FibuexportDto[] exportDaten = getExportDatenRechnung(rechnungDto,
					theClientDto);
			Steuerkategorie stk = em.find(Steuerkategorie.class, exportDaten[0]
					.getKontoDto().getSteuerkategorieIId());
			Integer mwstSatzbezIId = exportDaten[1].getMwstsatz()
					.getIIMwstsatzbezId();
			Steuerkategoriekonto stkk = getSteuerkategoriekonto(stk.getIId(),
					mwstSatzbezIId);

			temp[i] = new BuchungdetailDto();
			temp[i].setBuchungIId(details[0].getBuchungIId());
			temp[i].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			temp[i].setKontoIId(stkk.getKontoIIdVk());
			temp[i].setKontoIIdGegenkonto(kontoIIdAnzahlungErhaltenVerr);
			temp[i].setNBetrag(zahlungDto.getNBetragUst());
			temp[i].setNUst(new BigDecimal(0));
		}
		return temp;
	}

	/**
	 * Automatisches Uebernehmen einer ER in die FiBu
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public BuchungDto verbucheEingangsrechnung(Integer eingangsrechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		long startTime = System.currentTimeMillis();

		BuchungDto buchungDto = null;
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			BelegbuchungDto bbDto = null;
			try {
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_EINGANGSRECHNUNG,
								eingangsrechnungIId);
				if (bbDto != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
							"Beleg bereits verbucht: ID=" + eingangsrechnungIId);
				}
			} catch (RemoteException ex2) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex2);
			}

			EingangsrechnungDto eingangsrechnungDto = null;
			try {
				eingangsrechnungDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}

			FibuexportDto[] exportDaten = getExportDatenRechnung(
					eingangsrechnungDto, theClientDto);

			PartnerDto partnerDto = null;
			Konto kreditorKonto = null;
			try {
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(
								eingangsrechnungDto.getLieferantIId(),
								theClientDto);
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(
						lieferantDto.getPartnerIId(), theClientDto);
				if (lieferantDto.getKontoIIdKreditorenkonto() == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_KEIN_KREDITORENKONTO_DEFINIERT,
							"Kein Kreditorenkonto: Lieferant ID="
									+ lieferantDto.getIId());
				}
				kreditorKonto = em.find(Konto.class,
						lieferantDto.getKontoIIdKreditorenkonto());
				// if (lieferantDto.getKontoIIdWarenkonto() == null) {
				// throw new
				// EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEIN_WARENKONTO_DEFINIERT,
				// "Kein Warenkonto: Lieferant ID=" + lieferantDto.getIId());
				// }
			} catch (Exception ex3) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex3);
			}
			buchungDto = new BuchungDto();
			buchungDto.setCBelegnummer(eingangsrechnungDto.getCNr());
			Boolean lfReNrAlsText = getParameterFac().getMandantparameter(
					eingangsrechnungDto.getMandantCNr(),
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_LF_RE_NR_BUCHUNGSTEXT)
					.asBoolean();
			buchungDto.setCText((lfReNrAlsText ? eingangsrechnungDto
					.getCLieferantenrechnungsnummer() + " " : "")
					+ (eingangsrechnungDto.getCText() == null ? partnerDto
							.getCName1nachnamefirmazeile1()
							: eingangsrechnungDto.getCText()));

			buchungDto.setDBuchungsdatum(eingangsrechnungDto.getDBelegdatum());
			buchungDto.setKostenstelleIId(eingangsrechnungDto
					.getKostenstelleIId());
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_EINGANGSRECHNUNG);
			buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BUCHUNG);

			BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
			BuchungdetailDto[] details = null;
			boolean reverseCharge = Helper.short2boolean(eingangsrechnungDto
					.getBReversecharge());

			Integer steuerkategorieIId = reverseCharge ? kreditorKonto
					.getSteuerkategorieIIdReverse() : kreditorKonto
					.getSteuerkategorieIId();
			if (steuerkategorieIId == null) {
				throw new EJBExceptionLP(
						reverseCharge ? EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_REVERSE_DEFINIERT
								: EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
						"Keine Steuerkategorie bei Kreditorenkonto "
								+ kreditorKonto.getCNr(), kreditorKonto
								.getCNr() + " " + kreditorKonto.getCBez());
			}
			details = getBuchungdetailsVonExportDtos(exportDaten,
					steuerkategorieIId, false,
					Helper.short2boolean(eingangsrechnungDto.getBIgErwerb()),
					reverseCharge, theClientDto);

			if (eingangsrechnungDto.getEingangsrechnungartCNr().equals(
					EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG)
					|| eingangsrechnungDto
							.getEingangsrechnungartCNr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {
				belegbuchungDto
						.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
			} else if (eingangsrechnungDto.getEingangsrechnungartCNr().equals(
					EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)) {
				// TODO: Belegart ER Gutschrift
				belegbuchungDto
						.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
			} else if (eingangsrechnungDto.getEingangsrechnungartCNr().equals(
					EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)) {
				belegbuchungDto
						.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
			} else if (eingangsrechnungDto.getEingangsrechnungartCNr().equals(
					EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
				belegbuchungDto
						.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
			} else {
				myLogger.error("Ungueltiger Eingangsrechnungstyp, keine Verbuchung moeglich");
			}
			// nur buchen, wenn die Eingangsrechnung auch verbucht werden kann
			if (details != null) {
				try {

					BuchungdetailDto[] detailsAll = null;
					if (eingangsrechnungDto
							.getEingangsrechnungartCNr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
						// zusaetzlich alle Anzahlungsrechnungen ausbuchen
						BuchungdetailDto[] anzahlungdetailDtos = getDetailsSchlussrechnungAnzahlungen(
								eingangsrechnungDto, theClientDto);
						if (anzahlungdetailDtos.length > 0) {
							detailsAll = new BuchungdetailDto[details.length
									+ anzahlungdetailDtos.length];
							System.arraycopy(details, 0, detailsAll, 0,
									details.length);
							System.arraycopy(anzahlungdetailDtos, 0,
									detailsAll, details.length,
									anzahlungdetailDtos.length);
						}
					}

					// Das Verbuchen einer Eingangsrechnung muss immer den
					// Buchungsregeln entsprechen
					//
					// Kostenstelle aus erster Buchung wenn Splittbuchung
					if (buchungDto.getKostenstelleIId() == null) {
						for (int i = 0; i < exportDaten.length; i++) {
							if (exportDaten[i].getKostDto() != null) {
								if (exportDaten[i].getKostDto().getIId() != null) {
									buchungDto
											.setKostenstelleIId(exportDaten[i]
													.getKostDto().getIId());
									break;
								}
							}
						}
					}
					if (detailsAll != null)
						buchungDto = getBuchenFac().buchen(buchungDto,
								detailsAll, true, theClientDto);
					else
						buchungDto = getBuchenFac().buchen(buchungDto, details,
								true, theClientDto);
					// in Tabelle buchungRechnung speichern
					belegbuchungDto.setBuchungIId(buchungDto.getIId());
					belegbuchungDto.setIBelegiid(eingangsrechnungDto.getIId());
					createBelegbuchung(belegbuchungDto, theClientDto);
					// Status der Rechnung auf verbucht setzen
					// AD: nicht auf verbucht setzen, macht erst die UVA
					// rechnungDto.setStatusCNr(RechnungFac.STATUS_VERBUCHT);
					// getEingangsrechnungFac().updateEingangsrechnung(eingangsrechnungDto,
					// theClientDto);
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}
			long dauer = System.currentTimeMillis() - startTime;
			myLogger.logData("Verbuchen der Eingangsrechnung dauerte " + dauer
					+ " ms.");
		}

		return buchungDto;
	}

	public List<BelegbuchungDto> getAlleBelegbuchungenInklZahlungenER(
			Integer erIId) throws EJBExceptionLP, RemoteException {
		List<BelegbuchungDto> belegbuchungen = new ArrayList<BelegbuchungDto>();
		for (EingangsrechnungzahlungDto erz : getEingangsrechnungFac()
				.eingangsrechnungzahlungFindByEingangsrechnungIId(erIId)) {
			BelegbuchungDto buchung;
			if (erz.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
				buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
						LocaleFac.BELEGART_REZAHLUNG,
						erz.getRechnungzahlungIId());
			} else if (erz.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
				buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
						LocaleFac.BELEGART_EINGANGSRECHNUNG,
						erz.getEingangsrechnungIIdGutschrift());
			} else {
				buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
						LocaleFac.BELEGART_ERZAHLUNG, erz.getIId());
			}
			if (buchung != null)
				belegbuchungen.add(buchung);
		}
		BelegbuchungDto buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
				LocaleFac.BELEGART_EINGANGSRECHNUNG, erIId);
		if (buchung != null)
			belegbuchungen.add(buchung);
		return belegbuchungen;
	}

	public List<BelegbuchungDto> getAlleBelegbuchungenInklZahlungenAR(
			Integer arIId) throws EJBExceptionLP, RemoteException {
		List<BelegbuchungDto> belegbuchungen = new ArrayList<BelegbuchungDto>();
		for (RechnungzahlungDto rez : getRechnungFac()
				.zahlungFindByRechnungIId(arIId)) {
			// if(rez.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG))
			// {
			// buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
			// LocaleFac.BELEGART_REZAHLUNG, rez.getEingangsrechnungIId());
			// } else
			if (rez.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
				BelegbuchungDto buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
						LocaleFac.BELEGART_RECHNUNG,
						rez.getRechnungIIdGutschrift());
				if(buchung != null)
					belegbuchungen.add(buchung);
			}
			BelegbuchungDto buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
						LocaleFac.BELEGART_REZAHLUNG, rez.getIId());
			if (buchung != null)
				belegbuchungen.add(buchung);
		}
		BelegbuchungDto buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
				LocaleFac.BELEGART_RECHNUNG, arIId);
		if (buchung == null) { // dann ist es eine Gutschrift
			buchung = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_GUTSCHRIFT, arIId);
		}
		if (buchung != null)
			belegbuchungen.add(buchung);
		return belegbuchungen;
	}

	public void belegbuchungenAusziffernWennNoetig(Integer kontoIId,
			List<BelegbuchungDto> belegbuchungen) throws EJBExceptionLP,
			RemoteException {
		belegbuchungenAusziffernWennNoetig(kontoIId,
				belegbuchungen.toArray(new BelegbuchungDto[0]));
	}

	public void belegbuchungenAusziffernWennNoetig(Integer kontoIId,
			BelegbuchungDto... belegbuchungen) throws EJBExceptionLP,
			RemoteException {
		String belegnummer = null;
		boolean ausziffernNoetig = false;

		List<BuchungdetailDto> buchungdetails = new ArrayList<BuchungdetailDto>();

		for (BelegbuchungDto belegbuchung : belegbuchungen) {
			BuchungDto buchung = getBuchenFac().buchungFindByPrimaryKey(
					belegbuchung.getBuchungIId());

			buchungdetails.addAll(getBuchenFac()
					.buchungdetailsFindByKontoIIdBuchungIId(kontoIId,
							buchung.getIId()));
			if (belegnummer == null) {
				belegnummer = buchung.getCBelegnummer().trim() ;
			} else {
				if (!belegnummer.equals(buchung.getCBelegnummer().trim())) {
					ausziffernNoetig = true;
				}
			}
		}

		List<Integer> iidsOhneAZK = new ArrayList<Integer>();
		Integer vorhandenesAZK = null;
		for (BuchungdetailDto buchungdetailDto : buchungdetails) {
			Integer azk = buchungdetailDto.getIAusziffern();
			if (azk != null) {
				if (vorhandenesAZK == null) {
					vorhandenesAZK = azk;
				} else if (!azk.equals(vorhandenesAZK)) {
					EJBExceptionLP exce = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BUCHUNGDETAIL_VERSCHIEDENE_AZK_VORHANDEN,
							"");
					exce.setAlInfoForTheClient(new ArrayList<Object>(Arrays
							.asList(vorhandenesAZK, buchungdetailDto)));
				}
			} else {
				iidsOhneAZK.add(buchungdetailDto.getIId());
			}
		}

		if (vorhandenesAZK == null && ausziffernNoetig) {
			getFinanzServiceFac().createAuszifferung(
					iidsOhneAZK.toArray(new Integer[0]), null);
		} else {
			getFinanzServiceFac().updateAuszifferung(vorhandenesAZK,
					iidsOhneAZK.toArray(new Integer[0]), null);
		}
	}

	private BuchungdetailDto[] getDetailsSchlussrechnungAnzahlungen(
			EingangsrechnungDto eingangsrechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		ArrayList<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		EingangsrechnungDto[] anzRechnungen = getEingangsrechnungFac()
				.eingangsrechnungFindByBestellungIId(
						eingangsrechnungDto.getBestellungIId());
		for (int i = 0; i < anzRechnungen.length; i++) {
			if (anzRechnungen[i].getEingangsrechnungartCNr().equals(
					EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)
					&& anzRechnungen[i].getNBetragfw() != null) {
				boolean reverseCharge = anzRechnungen[i].isReversecharge();
				boolean bMitUst = (anzRechnungen[i].getNUstBetrag().signum() != 0) && !reverseCharge;

				BelegbuchungDto belegRech = belegbuchungFindByBelegartCNrBelegiid(
						LocaleFac.BELEGART_EINGANGSRECHNUNG,
						anzRechnungen[i].getIId());
				
				BuchungdetailDto[] detailDtos = getBuchenFac()
						.buchungdetailsFindByBuchungIId(
								belegRech.getBuchungIId());
				
				BuchungdetailDto anKreditor = detailDtos[0];
				anKreditor.setNBetrag(anKreditor.getNBetrag().negate());
				list.add(anKreditor);
				if(bMitUst) {
					BuchungdetailDto perVst = detailDtos[2];
					perVst.setNBetrag(perVst.getNBetrag().negate());
					list.add(perVst);
				}
				
				if(EingangsrechnungFac.STATUS_ERLEDIGT.equals(anzRechnungen[i].getStatusCNr())) {
					EingangsrechnungzahlungDto[] zahlungDtos = getEingangsrechnungFac()
							.eingangsrechnungzahlungFindByEingangsrechnungIId(
									anzRechnungen[i].getIId());
					for (int j = 0; j < zahlungDtos.length; j++) {
						BelegbuchungDto bbDto = getBelegbuchungFac(
								theClientDto.getMandant())
								.belegbuchungFindByBelegartCNrBelegiid(
										LocaleFac.BELEGART_ERZAHLUNG,
										zahlungDtos[j].getIId());
						detailDtos = getBuchenFac()
								.buchungdetailsFindByBuchungIId(
										bbDto.getBuchungIId());
						boolean skonto = detailDtos.length > 5;
						BuchungdetailDto anAnzBez = detailDtos[detailDtos.length-(skonto?4:3)];
						anAnzBez.swapSollHaben();
						anAnzBez.setKontoIIdGegenkonto(null);
						list.add(anAnzBez);
						
						if(skonto) {
							BuchungdetailDto anAnzVerr = detailDtos[detailDtos.length-3];
							anAnzVerr.setNBetrag(detailDtos[2].getNBetrag().negate());
							anAnzVerr.setKontoIIdGegenkonto(null);
							list.add(anAnzVerr);
						}
	//					BuchungdetailDto[] temp;
	//					if (bMitUst)
	//						temp = new BuchungdetailDto[3];
	//					else
	//						temp = new BuchungdetailDto[2];
	//					temp[0] = (BuchungdetailDto) detailDtos[1].clone();
	//					temp[0].swapSollHaben();
	//					temp[0].setNBetrag(temp[0].getNBetrag().negate());
	//					temp[0].setNUst(temp[0].getNUst().negate());
	//					temp[1] = (BuchungdetailDto) detailDtos[detailDtos.length - 3]
	//							.clone();
	//					temp[0].setKontoIIdGegenkonto(temp[1].getKontoIId());
	//					temp[1].swapSollHaben();
	//					temp[1].setKontoIIdGegenkonto(temp[0].getKontoIId());
	//					if (bMitUst) {
	//						temp[2] = getSteuerBuchungAnzahlungsrechnung(
	//								anzRechnungen[i], theClientDto);
	//						temp[2].swapSollHaben();
	//						temp[2].setKontoIIdGegenkonto(temp[0].getKontoIId());
	//					}
	//					for (int k = 0; k < temp.length; k++) {
	//						temp[k].setIId(null);
	//						temp[k].setBuchungIId(null);
	//						list.add(temp[k]);
	//					}
					}
				} else {
					EingangsrechnungzahlungDto fakeZahlung = new EingangsrechnungzahlungDto();
					fakeZahlung.setNBetrag(anzRechnungen[i].getNBetrag()); //Die Zahlung gibts noch nicht wirklich,
					fakeZahlung.setNBetragust(anzRechnungen[i].getNUstBetrag()); // aber wir brauchen sie, um die Buchungsdetails zu generieren
					BuchungdetailDto[] details = new BuchungdetailDto[]{new BuchungdetailDto()};
					details[0].setKontoIId(anKreditor.getKontoIIdGegenkonto());
					details = addAnzahlungZahlungDetails(details, anzRechnungen[i], fakeZahlung, theClientDto);
					
//					BuchungdetailDto anAnzBez = detailDtos[detailDtos.length-(3)];
					BuchungdetailDto anAnzBez = details[2] ;
					anAnzBez.swapSollHaben();
					anAnzBez.setKontoIIdGegenkonto(null);
					list.add(anAnzBez);					
				}
			}
		}
		for(BuchungdetailDto dto : list) {
			dto.setIId(null);
			dto.setBuchungIId(null);
			dto.setIAusziffern(null);
			dto.setIAuszug(null);
		}
//		HelperServer.printBuchungssatz(list, getFinanzFac(), System.out);
		return list.toArray(new BuchungdetailDto[list.size()]);
	}

	private BuchungdetailDto getSteuerBuchungAnzahlungsrechnung(
			EingangsrechnungDto eingangsrechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// Anzahlungsrechnung hat KEINE mehrfach Kontierung, daher ist die
		// Position fix
		BelegbuchungDto bbDto = getBelegbuchungFac(theClientDto.getMandant())
				.belegbuchungFindByBelegartCNrBelegiid(
						LocaleFac.BELEGART_EINGANGSRECHNUNG,
						eingangsrechnungDto.getIId());
		BuchungdetailDto[] detailDtos = getBuchenFac()
				.buchungdetailsFindByBuchungIId(bbDto.getBuchungIId());
		return detailDtos[detailDtos.length - 2];
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Rechnung
	 * 
	 * @param eingangsrechnungIId die IId der Eingangsrechnung 
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void verbucheEingangsrechnungRueckgaengig(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			BelegbuchungDto buchungRechnungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_EINGANGSRECHNUNG, eingangsrechnungIId);
			if (buchungRechnungDto != null) {
				getSystemFac().pruefeGeschaeftsjahrSperre(buchungRechnungDto,
						theClientDto.getMandant());

				Integer buchungIId = buchungRechnungDto.getBuchungIId();
				removeBelegbuchung(buchungRechnungDto, theClientDto);
				getBuchenFac().storniereBuchung(buchungIId, theClientDto);
			}
		}
	}

	/**
	 * Verbuchen der Zahlung einer Eingangsrechnung
	 * 
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public BuchungDto verbucheZahlungEr(Integer zahlungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		BuchungDto buchungDto = null;
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			long startTime = System.currentTimeMillis();
			// Zahlung schon verbucht?
			BelegbuchungDto bbDto = null;
			bbDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_ERZAHLUNG, zahlungIId);
			if (bbDto != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
						"Zahlung bereits verbucht. ID=" + zahlungIId);
			}
			EingangsrechnungzahlungDto zahlungDto = getEingangsrechnungzahlung(zahlungIId);
			EingangsrechnungDto rechnungDto = getEingangsrechnungDto(zahlungDto
					.getEingangsrechnungIId());

			boolean bBezahlt = rechnungDto.getStatusCNr().equals(
					EingangsrechnungFac.STATUS_ERLEDIGT);

			if (bBezahlt) {
				// alle Zahlungen aufrollen und bei einer den Skonto buchen
				EingangsrechnungzahlungDto[] zahlungDtos = getEingangsrechnungFac()
						.eingangsrechnungzahlungFindByEingangsrechnungIId(
								rechnungDto.getIId());
				//alle Zahlungen die:
				// -- Skontobuchungen haben
				// -- spaeteres Zahldatum als die zu buchende Zahlung
				// -- die Zahlung ist die ich buchen will
				List<EingangsrechnungzahlungDto> alZahlungen = new ArrayList<EingangsrechnungzahlungDto>();
				for (EingangsrechnungzahlungDto za : zahlungDtos) {
					if(hatZahlungSkonto(za.getIId(), LocaleFac.BELEGART_ERZAHLUNG)
							|| !za.getTZahldatum().before(zahlungDto.getTZahldatum())
							|| za.getIId().equals(zahlungIId)) {
						alZahlungen.add(za);
					}
				}
				// dann wieder verbuchen mit Ruecksicht auf Skonto
				boolean bBucheMitSkonto = true;
				for (EingangsrechnungzahlungDto zDto : alZahlungen) {
					if(!zDto.getIId().equals(zahlungIId))
						verbucheZahlungErRueckgaengig(zDto.getIId(), theClientDto);
					buchungDto = verbucheZahlungErSkonto(bBucheMitSkonto, zDto,
							rechnungDto, theClientDto);
					bBucheMitSkonto = buchungDto == null && bBucheMitSkonto;
						// Skontobuchung ist jetzt
						// drin, restliche ohne
				}
			} else {
				// nur diese Zahlung verbuchen
				buchungDto = verbucheZahlungErSkonto(false, zahlungDto,
						rechnungDto, theClientDto);
			}
			long dauer = System.currentTimeMillis() - startTime;
			myLogger.logData("Verbuchen der Zahlung dauerte " + dauer + " ms.");
		}
		return buchungDto;
	}

	protected BuchungDto verbucheZahlungErSkonto(boolean bBucheMitSkonto,
			EingangsrechnungzahlungDto zahlungDto,
			EingangsrechnungDto rechnungDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		PartnerDto partnerDto = getPartnerDtoMitKredKonto(
				rechnungDto.getLieferantIId(), theClientDto);
		BuchungDto buchungDto = null;
		if (zahlungDto.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BANK)
				|| zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_BAR)
				|| zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)
				|| zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
			BuchungdetailDto[] details = getBuchungdetailsVonZahlungEr(
					zahlungDto, rechnungDto, bBucheMitSkonto, theClientDto);
			buchungDto = new BuchungDto();
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_EINGANGSRECHNUNG);
			if (zahlungDto.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_BANK)
					|| zahlungDto.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)
					|| zahlungDto.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG))
				buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG);
			else
				buchungDto
						.setBuchungsartCNr(FinanzFac.BUCHUNGSART_KASSENBUCHUNG);
			buchungDto.setCBelegnummer(rechnungDto.getCNr());
			Boolean lfReNrAlsText = getParameterFac().getMandantparameter(
					rechnungDto.getMandantCNr(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_LF_RE_NR_BUCHUNGSTEXT)
					.asBoolean();
			buchungDto.setCText((lfReNrAlsText ? rechnungDto
					.getCLieferantenrechnungsnummer() + " " : "")
					+ (rechnungDto.getCText() == null ? partnerDto
							.getCName1nachnamefirmazeile1() : rechnungDto
							.getCText()));
			buchungDto.setDBuchungsdatum(new Date(zahlungDto.getTZahldatum()
					.getTime()));
			if (rechnungDto.getKostenstelleIId() != null) {
				buchungDto.setKostenstelleIId(rechnungDto.getKostenstelleIId());
			} else {
				// Kostenstelle aus Buchung der Rechnung holen Splittbuchung
				BelegbuchungDto bbRechnungDto;
				try {
					bbRechnungDto = belegbuchungFindByBelegartCNrBelegiid(
							LocaleFac.BELEGART_EINGANGSRECHNUNG,
							zahlungDto.getEingangsrechnungIId());
					Buchung buchung = em.find(Buchung.class,
							bbRechnungDto.getBuchungIId());
					buchungDto.setKostenstelleIId(buchung.getKostenstelleIId());
				} catch (Exception e1) {
					// Rechnung nicht gebucht da evt. in Vorjahr oder
					// anderer Periode
					// Kostenstelle aus Kontierung holen
					EingangsrechnungKontierungDto[] erkontierungDtos = null;
					try {
						erkontierungDtos = getEingangsrechnungFac()
								.eingangsrechnungKontierungFindByEingangsrechnungIId(
										zahlungDto.getEingangsrechnungIId());
						for (int i = 0; i < erkontierungDtos.length; i++) {
							if (erkontierungDtos[i].getKostenstelleIId() != null) {
								buchungDto
										.setKostenstelleIId(erkontierungDtos[i]
												.getKostenstelleIId());
								break;
							}
						}
					} catch (RemoteException e) {
						//
					}
				}
			}

			// nur buchen, wenn die Zahlung auch verbucht werden kann
			if (details != null) {
				try {
					if (rechnungDto.getEingangsrechnungartCNr().equals(
							EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG))
						// zusaetzliche Buchungen anhaengen
						details = addAnzahlungZahlungDetails(details,
								rechnungDto, zahlungDto, theClientDto);

					if (zahlungDto.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
						// buchungdetails kommen aus bestehender Umbuchung mit
						// Ausnahme der evt.
						// vorhandenen Waehrungsverlust und Skontobuchungen
						Buchungdetail bd = em.find(Buchungdetail.class,
								zahlungDto.getBuchungdetailIId());
						BuchungdetailDto[] temp = getBuchungDetailsAsNew(bd);
						for (int i = 0; i < temp.length; i++) {
							details[i] = temp[i];
							details[i].setNBetrag(zahlungDto.getNBetrag());
						}
						// zusaetzlich Vorauszahlung stornieren
						getBuchenFac().storniereBuchung(bd.getBuchungIId(),
								theClientDto);
						// zusaetzlich Rest als Vorauszahlung einbuchen
						bucheDifferenzVorauszahlung(bd.getIId(),
								zahlungDto.getNBetrag(), theClientDto);
					}

					// Das Verbuchen einer Rechnung muss immer den
					// Buchungsregeln entsprechen
					buchungDto = getBuchenFac().buchen(buchungDto, details,
							true, theClientDto);
					// in Tabelle buchungRechnung speichern
					BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
					belegbuchungDto
							.setBelegartCNr(LocaleFac.BELEGART_ERZAHLUNG);
					belegbuchungDto.setBuchungIId(buchungDto.getIId());
					belegbuchungDto.setIBelegiid(zahlungDto.getIId());
					createBelegbuchung(belegbuchungDto, theClientDto);
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}
		}
		return buchungDto;
	}

	private BuchungdetailDto[] getBuchungDetailsAsNew(Buchungdetail bd) {
		BuchungdetailDto[] details;
		BuchungdetailDto[] temp = getBuchenFac()
				.buchungdetailsFindByBuchungIId(bd.getBuchungIId());
		details = new BuchungdetailDto[temp.length - 1];
		System.arraycopy(temp, 0, details, 0, temp.length - 1);
		for (int i = 0; i < details.length; i++) {
			details[i].setBuchungIId(null);
			details[i].setIId(null);
		}
		return details;
	}

	private BuchungdetailDto[] addVorauszahlungDetails(
			BuchungdetailDto[] details, Integer buchungdetailIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// die bestehende Buchung mit -Betrag dazuhaengen
		BuchungdetailDto bdDto = getBuchenFac().buchungdetailFindByPrimaryKey(
				buchungdetailIId);
		BuchungdetailDto[] temp;
		temp = new BuchungdetailDto[details.length + 1];
		System.arraycopy(details, 0, temp, 0, details.length);
		int i = details.length;
		temp[i] = new BuchungdetailDto();
		temp[i].setBuchungdetailartCNr(bdDto.getBuchungdetailartCNr());
		temp[i].setKontoIId(bdDto.getKontoIId());
		temp[i].setKontoIIdGegenkonto(bdDto.getKontoIIdGegenkonto());
		temp[i].setNBetrag(bdDto.getNBetrag().negate());
		if (bdDto.getNUst() != null)
			temp[i].setNUst(bdDto.getNUst().negate());
		return temp;
	}

	private BuchungdetailDto[] addAnzahlungZahlungDetails(
			BuchungdetailDto[] details, EingangsrechnungDto rechnungDto,
			EingangsrechnungzahlungDto zahlungDto, TheClientDto theClientDto) {
		// folgende Buchunge anhaengen
		// Gel. Anz an Verr. Gel. Anz
		// Vst
		BuchungdetailDto[] temp;
		boolean bMitVst = false;
		// Achtung: die jetzige Implementierung bucht hier keine Steuer, da laut
		// Werner unsere Anzahlung immer mit Leistung ist,
		// da schon bei Eingangsrechnung die Steuerbuchung erfolgte
		//
		// if (zahlungDto.getNBetragUst().signum()==0)
		temp = new BuchungdetailDto[details.length + 2];
		// else {
		// temp = new BuchungdetailDto[details.length + 3];
		// bMitUst = true;
		// }
		System.arraycopy(details, 0, temp, 0, details.length);
		Konto kreditorenkonto = em.find(Konto.class, details[0].getKontoIId());
		FinanzamtPK fpk = new FinanzamtPK(kreditorenkonto.getFinanzamtIId(),
				kreditorenkonto.getMandantCNr());
		Finanzamt finanzamt = em.find(Finanzamt.class, fpk);
		int i = details.length;
		temp[i] = new BuchungdetailDto();
		temp[i].setBuchungIId(details[0].getBuchungIId());
		temp[i].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		boolean reverseCharge = rechnungDto.isReversecharge();
		Integer kontoIIdAnzahlungGegebenBezahlt = reverseCharge ? finanzamt
				.getKontoIIdRCAnzahlungGegebenBezahlt() : finanzamt
				.getKontoIIdAnzahlungGegebenBezahlt();
		Integer kontoIIdAnzahlungGegebenVerr = reverseCharge ? finanzamt
				.getKontoIIdRCAnzahlungGegebenVerr() : finanzamt
				.getKontoIIdAnzahlungGegebenVerr();
		temp[i].setKontoIId(kontoIIdAnzahlungGegebenBezahlt);
		temp[i].setKontoIIdGegenkonto(kontoIIdAnzahlungGegebenVerr);
		if (bMitVst || reverseCharge) {
			// auch bei Reverse Charge Brutto buchen
			temp[i].setNBetrag(zahlungDto.getNBetrag());
			temp[i].setNUst(zahlungDto.getNBetragust());
		} else {
			temp[i].setNBetrag(zahlungDto.getNBetrag().subtract(
					zahlungDto.getNBetragust()));
			temp[i].setNUst(new BigDecimal(0));
		}
		i++;
		temp[i] = new BuchungdetailDto();
		temp[i].setBuchungIId(details[0].getBuchungIId());
		temp[i].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		temp[i].setKontoIId(kontoIIdAnzahlungGegebenVerr);
		temp[i].setKontoIIdGegenkonto(kontoIIdAnzahlungGegebenBezahlt);
		temp[i].setNBetrag(temp[i - 1].getNBetrag());
		temp[i].setNUst(temp[i - 1].getNUst());
		if (bMitVst) {
			i++;
			FibuexportDto[] exportDaten = getExportDatenRechnung(rechnungDto,
					theClientDto);
			Steuerkategorie stk = em.find(Steuerkategorie.class, exportDaten[0]
					.getKontoDto().getSteuerkategorieIId());
			Integer mwstSatzbezIId = exportDaten[1].getMwstsatz()
					.getIIMwstsatzbezId();
			Steuerkategoriekonto stkk = getSteuerkategoriekonto(stk.getIId(),
					mwstSatzbezIId);

			temp[i] = new BuchungdetailDto();
			temp[i].setBuchungIId(details[0].getBuchungIId());
			temp[i].setBuchungdetailartCNr(BuchenFac.SollBuchung);
			temp[i].setKontoIId(kontoIIdAnzahlungGegebenBezahlt);
			temp[i].setKontoIIdGegenkonto(stkk.getKontoIIdEk());
			temp[i].setNBetrag(zahlungDto.getNBetragust());
			temp[i].setNUst(new BigDecimal(0));
		}
		return temp;
	}

	protected FibuexportDto[] getExportDatenRechnung(
			EingangsrechnungDto rechnungDto, TheClientDto theClientDto) {
		// ermitteln der beteiligten Steuersaetze ueber den Exportmanager
		FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
		fibuExportKriterienDto
				.setSBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
		fibuExportKriterienDto.setDStichtag(new Date(rechnungDto
				.getDBelegdatum().getTime()));

		FibuExportManager manager = FibuExportManagerFactory
				.getFibuExportManager(getExportVariante(theClientDto),
						getExportFormat(theClientDto), fibuExportKriterienDto,
						theClientDto);
		FibuexportDto[] exportDaten = manager.getExportdatenEingangsrechnung(
				rechnungDto.getIId(), rechnungDto.getDBelegdatum());
		return exportDaten;
	}

	private PartnerDto getPartnerDtoMitKredKonto(Integer lieferantIId,
			TheClientDto theClientDto) {
		PartnerDto partnerDto = null;
		try {
			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(lieferantIId, theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					lieferantDto.getPartnerIId(), theClientDto);
			if (lieferantDto.getKontoIIdKreditorenkonto() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEIN_KREDITORENKONTO_DEFINIERT,
						"Kein Kreditorenkonto definiert. "
								+ (partnerDto == null ? "LieferantID="
										+ lieferantDto.getIId() : "Lieferant="
										+ partnerDto.formatName()));
			}
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		return partnerDto;
	}

	protected EingangsrechnungzahlungDto getEingangsrechnungzahlung(
			Integer zahlungIId) {
		EingangsrechnungzahlungDto zahlungDto = null;
		try {
			zahlungDto = getEingangsrechnungFac()
					.eingangsrechnungzahlungFindByPrimaryKey(zahlungIId);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		return zahlungDto;
	}

	protected EingangsrechnungDto getEingangsrechnungDto(
			Integer eingangsrechnungIId) {
		EingangsrechnungDto rechnungDto = null;
		try {
			rechnungDto = getEingangsrechnungFac()
					.eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
		return rechnungDto;
	}

	public void verbucheZahlungErRueckgaengig(Integer zahlungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		EingangsrechnungzahlungDto erzDto;
		try {
			erzDto = getEingangsrechnungFac()
					.eingangsrechnungzahlungFindByPrimaryKey(zahlungIId);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
		verbucheZahlungErRueckgaengig(erzDto, theClientDto);
	}

	public void verbucheZahlungErRueckgaengig(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto,
			TheClientDto theClientDto) {

		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			myLogger.logData(eingangsrechnungzahlungDto.getIId(),
					theClientDto.getIDUser());
			BelegbuchungDto buchungZahlungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_ERZAHLUNG,
					eingangsrechnungzahlungDto.getIId());
			if (buchungZahlungDto != null) {
				getSystemFac().pruefeGeschaeftsjahrSperre(buchungZahlungDto,
						theClientDto.getMandant());

				Integer buchungIId = buchungZahlungDto.getBuchungIId();
				removeBelegbuchung(buchungZahlungDto, theClientDto);
				getBuchenFac().storniereBuchung(buchungIId, theClientDto);
				if (eingangsrechnungzahlungDto.getZahlungsartCNr().equals(
						RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
					Integer bdIId = eingangsrechnungzahlungDto
							.getBuchungdetailIId();
					try {
						BuchungDto bDto = bucheVorauszahlungBetrag(bdIId,
								eingangsrechnungzahlungDto.getNBetrag(),
								theClientDto);

						Eingangsrechnungzahlung zahlung = em.find(
								Eingangsrechnungzahlung.class,
								eingangsrechnungzahlungDto.getIId());
						if (zahlung != null) {
							// es wurde nur eine Zahlung neu in der Fibu
							// verbucht und diese ist nicht geloescht
							// dann das neue Buchungsdetail an die Zahlung
							// haengen
							BuchungdetailDto[] bdtos = getBuchenFac()
									.buchungdetailsFindByBuchungIId(
											bDto.getIId());
							zahlung.setBuchungdetailIId(bdtos[0].getIId());
							em.merge(zahlung);
							em.flush();
						}
					} catch (RemoteException e) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_BUCHUNGAKTIVIEREN_NICHT_MOEGLICH,
								"Vorauszahlung kann nicht aktiviert werden. "
										+ e.getMessage());
					}
				}
			}
		}
	}

	protected BuchungDto bucheVorauszahlungBetrag(Integer bdIId,
			BigDecimal betrag, TheClientDto theClientDto)
			throws RemoteException {
		Buchungdetail bd = em.find(Buchungdetail.class, bdIId);
		return bucheVorauszahlungBetrag(bd, betrag, theClientDto);
	}

	protected void bucheDifferenzVorauszahlung(Integer bdIId,
			BigDecimal zahlBetrag, TheClientDto theClientDto)
			throws RemoteException {
		Buchungdetail bd = em.find(Buchungdetail.class, bdIId);
		BigDecimal rest = bd.getNBetrag().subtract(zahlBetrag);
		if (rest.signum() > 0) {
			// Rest als neue Buchung hinzufuegen
			bucheVorauszahlungBetrag(bd, rest, theClientDto);
		}
	}

	private BuchungDto bucheVorauszahlungBetrag(Buchungdetail bd,
			BigDecimal betrag, TheClientDto theClientDto)
			throws RemoteException {
		BuchungdetailDto[] bdDtos = getBuchungDetailsAsNew(bd);
		BuchungDto bDto = getBuchenFac().buchungFindByPrimaryKey(
				bd.getBuchungIId());
		bDto.setIId(null);
		bDto.setTAendern(null);
		bDto.setTAnlegen(null);
		bDto.setTStorniert(null);
		bDto.setPersonalIIdStorniert(null);
		if (bd.getNBetrag().compareTo(betrag) != 0)
			for (int i = 0; i < bdDtos.length; i++)
				bdDtos[i].setNBetrag(betrag);
		return getBuchenFac().buchen(bDto, bdDtos, true, theClientDto);
	}

	public void pruefeUvaVerprobung(String belegartCNr, Integer iBelegiid,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			// nur bei Fibumodul
			BelegbuchungDto belegbuchungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					belegartCNr, iBelegiid);
			if (belegbuchungDto != null) {
				Buchung buchung = em.find(Buchung.class,
						belegbuchungDto.getBuchungIId());
				if (buchung.getUvaverprobungIId() != null)
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
							new Exception(belegartCNr + " " + iBelegiid
									+ " ist bereits UVA verprobt"));
			}
		}
	}

	public void pruefeUvaVerprobung(Date buchungsdatum, Integer kontoIId,
			TheClientDto theClientDto) {
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			// nur bei Fibumodul
			Konto konto = em.find(Konto.class, kontoIId);
			UvaverprobungDto uvapDto = getFinanzServiceFac()
					.getLetzteVerprobung(konto.getFinanzamtIId(), theClientDto);
			if (isInVerprobung(uvapDto, buchungsdatum, theClientDto))
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_UVAVERPROBUNG,
						new Exception("Buchung zum " + buchungsdatum
								+ " ist aufgrund UVA-Verprobung nicht erlaubt"));
		}
	}

	private boolean isInVerprobung(UvaverprobungDto uvapDto,
			Date buchungsdatum, TheClientDto theClientDto) {
		if (null == uvapDto)
			return false;

		Integer periode = null;
		if (uvapDto.getIAbrechnungszeitraum() == UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_MONAT) {
			periode = uvapDto.getIMonat();
		} else if (uvapDto.getIAbrechnungszeitraum() == UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_QUARTAL) {
			Integer[] perioden = HelperServer.getMonateFuerQuartal(uvapDto
					.getIMonat());
			periode = perioden[perioden.length - 1];
		} else if (uvapDto.getIAbrechnungszeitraum() == UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_JAHR) {
			periode = 12;
		}
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				uvapDto.getIGeschaeftsjahr(), periode, theClientDto);
		return buchungsdatum.before(tVonBis[1]);
	}

	public BigDecimal getBuchungsbetragInWahrung(Integer buchungDetailIId,
			String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		Buchungdetail buchungdetail = em.find(Buchungdetail.class,
				buchungDetailIId);
		BelegbuchungDto bbDto = belegbuchungFindByBuchungIIdOhneExc(buchungdetail
				.getBuchungIId());

		if (bbDto == null) {
			// keine Belegbuchung, umrechnen ueber Kurs zu Datum
			Buchung buchung = em.find(Buchung.class,
					buchungdetail.getBuchungIId());
			BigDecimal betrag = getLocaleFac()
					.rechneUmInAndereWaehrungGerundetZuDatum(
							buchungdetail.getNBetrag(),
							theClientDto.getSMandantenwaehrung(), waehrungCNr,
							buchung.getTBuchungsdatum(), theClientDto);
			return betrag;
		} else {
			// aus dem Beleg den Kurs ermitteln falls gleiche Waehrung, sonst
			// umrechnen
			BigDecimal belegkurs = null;
			String belegwaehrung = null;
			java.sql.Date belegdatum = null;
			if (bbDto.getBelegartCNr().equals(LocaleFac.BELEGART_RECHNUNG)
					|| bbDto.getBelegartCNr().equals(
							LocaleFac.BELEGART_GUTSCHRIFT)) {
				Rechnung rechnung = em.find(Rechnung.class,
						bbDto.getIBelegiid());
				if (rechnung != null) {
					belegkurs = rechnung.getNKurs();
					belegwaehrung = rechnung.getWaehrungCNr();
					belegdatum = new java.sql.Date(rechnung.getTBelegdatum()
							.getTime());
				}
			} else if (bbDto.getBelegartCNr().equals(
					LocaleFac.BELEGART_REZAHLUNG)) {
				Rechnungzahlung zahlung = em.find(Rechnungzahlung.class,
						bbDto.getIBelegiid());
				if (zahlung != null) {
					belegkurs = zahlung.getNKurs();
					Rechnung rechnung = em.find(Rechnung.class,
							zahlung.getRechnungIId());
					if (rechnung != null)
						belegwaehrung = rechnung.getWaehrungCNr();
					belegdatum = zahlung.getTZahldatum();
				}
			} else if (bbDto.getBelegartCNr().equals(
					LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
				Eingangsrechnung rechnung = em.find(Eingangsrechnung.class,
						bbDto.getIBelegiid());
				if (rechnung != null) {
					belegkurs = Helper.rundeKaufmaennisch(Helper
							.getKehrwert(rechnung.getNKurs()), rechnung
							.getNKurs().scale());
					belegwaehrung = rechnung.getWaehrungCNr();
					belegdatum = new java.sql.Date(rechnung.getTBelegdatum()
							.getTime());
				}
			} else if (bbDto.getBelegartCNr().equals(
					LocaleFac.BELEGART_ERZAHLUNG)) {
				Eingangsrechnungzahlung zahlung = em.find(
						Eingangsrechnungzahlung.class, bbDto.getIBelegiid());
				if (zahlung != null) {
					belegkurs = zahlung.getNKurs();
					Eingangsrechnung rechnung = em.find(Eingangsrechnung.class,
							zahlung.getEingangsrechnungIId());
					if (rechnung != null)
						belegwaehrung = rechnung.getWaehrungCNr();
					belegdatum = zahlung.getTZahldatum();
				}
			}
			BigDecimal betrag = null;
			if (belegwaehrung.equals(waehrungCNr)) {
				if (belegkurs != null)
					if (belegkurs.compareTo(new BigDecimal(1)) == 0)
						betrag = buchungdetail.getNBetrag();
					else
						betrag = Helper.rundeKaufmaennisch(buchungdetail
								.getNBetrag().multiply(belegkurs),
								FinanzFac.NACHKOMMASTELLEN);
			} else {
				betrag = getLocaleFac()
						.rechneUmInAndereWaehrungGerundetZuDatum(
								buchungdetail.getNBetrag(),
								theClientDto.getSMandantenwaehrung(),
								waehrungCNr, belegdatum, theClientDto);
			}
			return betrag;
		}
	}

}
