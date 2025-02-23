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
package com.lp.server.partner.ejbfac;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.modelmapper.ModelMapper;

import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.auftrag.service.AuftragteilnehmerDto;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.fertigung.ejb.Los;
import com.lp.server.fertigung.ejb.Wiederholendelose;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.WiederholendeloseDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.ejb.Anrede;
import com.lp.server.partner.ejb.Anredespr;
import com.lp.server.partner.ejb.AnredesprPK;
import com.lp.server.partner.ejb.Ansprechpartner;
import com.lp.server.partner.ejb.Ansprechpartneradressbuch;
import com.lp.server.partner.ejb.Geodaten;
import com.lp.server.partner.ejb.GeodatenQuery;
import com.lp.server.partner.ejb.Kontakt;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.Kurzbrief;
import com.lp.server.partner.ejb.Lieferant;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.PartnerQuery;
import com.lp.server.partner.ejb.Partneradressbuch;
import com.lp.server.partner.ejb.Partnerart;
import com.lp.server.partner.ejb.Partnerartspr;
import com.lp.server.partner.ejb.PartnerartsprPK;
import com.lp.server.partner.ejb.Partnerklasse;
import com.lp.server.partner.ejb.Partnerklassespr;
import com.lp.server.partner.ejb.PartnerklassesprPK;
import com.lp.server.partner.ejb.Partnerkommentar;
import com.lp.server.partner.ejb.Partnerkommunikation;
import com.lp.server.partner.ejb.Paselektion;
import com.lp.server.partner.ejb.Telefonnummer;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.fastlanereader.generated.FLRPersoenlichesadressbuch;
import com.lp.server.partner.service.AdressbuchExportDto;
import com.lp.server.partner.service.AnredeDto;
import com.lp.server.partner.service.AnredeDtoAssembler;
import com.lp.server.partner.service.AnredesprDto;
import com.lp.server.partner.service.AnredesprDtoAssembler;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.GeodatenDto;
import com.lp.server.partner.service.GeodatenDtoAssembler;
import com.lp.server.partner.service.KontaktDto;
import com.lp.server.partner.service.KontaktDtoAssembler;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KurzbriefDto;
import com.lp.server.partner.service.KurzbriefDtoAssembler;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.PASelektionDtoAssembler;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerDtoAssembler;
import com.lp.server.partner.service.PartnerDtoSmall;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerartDto;
import com.lp.server.partner.service.PartnerartDtoAssembler;
import com.lp.server.partner.service.PartnerartsprDto;
import com.lp.server.partner.service.PartnerartsprDtoAssembler;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.partner.service.PartnerklasseDtoAssembler;
import com.lp.server.partner.service.PartnerklassesprDto;
import com.lp.server.partner.service.PartnerklassesprDtoAssembler;
import com.lp.server.partner.service.PartnerkommentarartDto;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.partner.service.PartnerkommunikationDtoAssembler;
import com.lp.server.partner.service.SerienbriefEmpfaengerDto;
import com.lp.server.partner.service.TelefonSuchergebnisDto;
import com.lp.server.personal.ejb.Personal;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.ReiselogDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.projekt.ejb.Projekt;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.stueckliste.ejb.Stueckliste;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class PartnerFacBean extends Facade implements PartnerFac {

	@PersistenceContext
	private EntityManager em;

	@Transient
	private transient ModelMapper mapper = null;

	/**
	 * Lege einen neuen Partner an.
	 * 
	 * @param partnerDto   PartnerDto
	 * @param theClientDto aktueller Benutzer
	 * @throws EJBExceptionLP
	 * @return Integer
	 */
	public Integer createPartner(PartnerDto partnerDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// Preconditions.
		if (partnerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("partnerDto == null"));
		}

		if (partnerDto.getCKbez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getCKbez() == null"));
		}

		if (partnerDto.getCName1nachnamefirmazeile1() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getCName1nachnamefirmazeile1() == null"));
		}

		if (partnerDto.getPartnerartCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getPartnerartCNr() == null"));
		}
		if (partnerDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getBVersteckt() == null"));
		}

		if (partnerDto.getLocaleCNrKommunikation() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getLocaleCNrKommunikation() == null"));
		}

		// Wer legt an setzen; Ausnahme wegen Junittest
		partnerDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		partnerDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());

		// PK fuer Partner generieren.
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer iIdPartnerNew = pkGen.getNextPrimaryKey(PKConst.PK_PARTNER);
		partnerDto.setIId(iIdPartnerNew);

		try {
			Partner partner = new Partner(partnerDto.getIId(), partnerDto.getLocaleCNrKommunikation(),
					partnerDto.getPartnerartCNr(), partnerDto.getCKbez(), partnerDto.getBVersteckt(),
					partnerDto.getCName1nachnamefirmazeile1(), partnerDto.getPersonalIIdAnlegen(),
					partnerDto.getPersonalIIdAendern());
			em.persist(partner);
			em.flush();

			// tAendern und tAnlegen werden im Bean generiert;
			// jetzt holen und setzen wegen update.
			partnerDto.setTAendern(partner.getTAendern());
			partnerDto.setTAnlegen(partner.getTAnlegen());

			// Partnerkommunikationen createn.
			partnerDto.setIId(iIdPartnerNew);

			// Partnerfelder setzen.
			setPartnerFromPartnerDto(partner, partnerDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (PersistenceException ep) {
			throwEJBExceptionLPforPersistence(ep);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}

		HvDtoLogger<PartnerDto> partnerLogger = new HvDtoLogger<PartnerDto>(em, theClientDto);
		partnerLogger.logInsert(partnerDto);

		getPartnerFac().telefonnummerFuerTapiSynchronisieren(partnerDto.getIId(), null, theClientDto);

		return partnerDto.getIId();
	}

	public void kontoFuerPartnerImportAnlegen(String sKontonummer, String kontotypCNr, Integer kundeIId,
			Integer lieferantIId, TheClientDto theClientDto) {
		try {
			KontoDto kontoDto = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(sKontonummer, kontotypCNr,
					theClientDto.getMandant(), theClientDto);

			Integer kontoIId = null;

			if (kontoDto == null) {
				kontoDto = new KontoDto();
				kontoDto.setCNr(sKontonummer);

				if (lieferantIId != null) {
					kontoDto.setCBez(getLieferantFac().lieferantFindByPrimaryKey(lieferantIId, theClientDto)
							.getPartnerDto().getCName1nachnamefirmazeile1());
				} else {
					kontoDto.setCBez(getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto).getPartnerDto()
							.getCName1nachnamefirmazeile1());
				}

				// Pflichtfelder befuellen
				GeschaeftsjahrMandantDto gjDto = getSystemFac().geschaeftsjahrFindByPrimaryKey(
						getParameterFac().getGeschaeftsjahr(theClientDto.getMandant()), theClientDto.getMandant());
				kontoDto.setDGueltigvon(new Date(gjDto.getDBeginndatum().getTime()));
				kontoDto.setBAllgemeinsichtbar(Helper.boolean2Short(true));
				kontoDto.setBAutomeroeffnungsbuchung(Helper.boolean2Short(true));
				kontoDto.setBManuellbebuchbar(Helper.boolean2Short(true));
				kontoDto.setMandantCNr(theClientDto.getMandant());
				kontoDto.setKontotypCNr(kontotypCNr);
				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);
				kontoDto.setFinanzamtIId(mandantDto.getPartnerIIdFinanzamt());
				kontoDto.setBOhneUst(Helper.boolean2Short(false));

				kontoDto = getFinanzFac().createKonto(kontoDto, theClientDto);
				kontoIId = kontoDto.getIId();
			} else {
				kontoIId = kontoDto.getIId();
			}

			if (lieferantIId != null) {
				Lieferant lieferant = em.find(Lieferant.class, lieferantIId);
				lieferant.setKontoIIdKreditorenkonto(kontoIId);
			} else if (kundeIId != null) {
				Kunde kunde = em.find(Kunde.class, kundeIId);
				kunde.setKontoIIdDebitorenkonto(kontoIId);
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	/**
	 * Loesche Partner und all seine "Satelliten".
	 * 
	 * @param partnerDtoI  PartnerDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void removePartner(PartnerDto partnerDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (partnerDtoI.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("partnerDtoI.getIId()"));
		}

		try {

			Partner partner = em.find(Partner.class, partnerDtoI.getIId());

			Query query = em.createQuery("DELETE Telefonnummer x WHERE x.partnerIId = " + partnerDtoI.getIId());
			query.executeUpdate();

			PartnerkommunikationDto[] partkommdto = getPartnerFac().partnerkommFindByPartnerIId(partner.getIId());
			for (int i = 0; i < partkommdto.length; i++) {
				getPartnerFac().removePartnerkommunikation(partkommdto[i].getIId(), theClientDto);
			}

			GeodatenDto geodatenDto = geodatenFindByPartnerIIdOhneExc(partner.getIId());
			if (geodatenDto != null) {
				getPartnerFac().removeGeodaten(geodatenDto.getIId());
			}

			HvDtoLogger<PartnerDto> partnerLogger = new HvDtoLogger<PartnerDto>(em, partnerDtoI.getIId(), theClientDto);
			partnerLogger.logDelete(partnerDtoI);

			// Partner loeschen.
			em.remove(partner);
			em.flush();
		} catch (EJBExceptionLP ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	/**
	 * Schreibe Partner zurueck.
	 * 
	 * @param partnerDto   PartnerDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void updatePartner(PartnerDto partnerDto, TheClientDto theClientDto) throws EJBExceptionLP {

		if (partnerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("partnerDto == null"));
		}

		if (partnerDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("partnerDto.getIId() == null"));
		}

		if (partnerDto.getPartnerartCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getPartnerartCNr() == null"));
		}

		if (partnerDto.getLocaleCNrKommunikation() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getLocaleCNrKommunikation() == null"));
		}

		Integer iId = partnerDto.getIId();
		try {

			// Suche Partner.
			Partner partner = em.find(Partner.class, iId);

			PartnerDto dtoVorher = assemblePartnerDto(partner);

			if (partner == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			// PJ15739 Wenn LKZ geaendert wird und Kunde bereits in LS oder RE
			// verwendet wird, dann Fehler
			if (partner.getLandplzortIId() != null) {

				if (partnerDto.getLandplzortIId() == null
						|| !partnerDto.getLandplzortIId().equals(partner.getLandplzortIId())) {

					boolean bLandGeaendert = false;

					if (partnerDto.getLandplzortIId() != null
							&& !partnerDto.getLandplzortIId().equals(partner.getLandplzortIId())) {

						LandplzortDto landplzortDto = getSystemFac()
								.landplzortFindByPrimaryKey(partner.getLandplzortIId());
						LandplzortDto landplzortDtoNeu = getSystemFac()
								.landplzortFindByPrimaryKey(partnerDto.getLandplzortIId());

						if (!landplzortDto.getIlandID().equals(landplzortDtoNeu.getIlandID())) {
							bLandGeaendert = true;
						}

					}

					if (bLandGeaendert == true || partnerDto.getLandplzortIId() == null) {

						KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(iId,
								theClientDto.getMandant(), theClientDto);

						if (kundeDto != null) {

							Session session = FLRSessionFactory.getFactory().openSession();
							// ----------------------RECHNUNG-------------------
							String queryString = "SELECT rech" + " FROM FLRRechnung AS rech WHERE rech.flrkunde.i_id="
									+ kundeDto.getIId();
							org.hibernate.Query query = session.createQuery(queryString);
							query.setMaxResults(1);
							List<?> resultList = query.list();

							boolean bVerwendet = false;

							if (resultList.size() > 0) {
								bVerwendet = true;
							}

							session.close();

							if (bVerwendet == false) {

								session = FLRSessionFactory.getFactory().openSession();
								// ----------------------LIEFERSCHEIN------------

								queryString = "SELECT lsch"
										+ " FROM FLRLieferschein AS lsch WHERE lsch.kunde_i_id_lieferadresse="
										+ kundeDto.getIId() + " OR lsch.kunde_i_id_rechnungsadresse="
										+ kundeDto.getIId();
								query = session.createQuery(queryString);
								query.setMaxResults(1);
								resultList = query.list();

								if (resultList.size() > 0) {
									bVerwendet = true;
								}
								session.close();
							}

							if (bVerwendet == true) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_LKZ_AENDERUNG_NICHT_MOEGLICH,
										"");
							}

						}

					}

				}

			}

			// Partner Felder setzen.
			setPartnerFromPartnerDto(partner, partnerDto);

			// Partner Felder setzen.
			partner.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
			partner.setPersonalIIdAendern(theClientDto.getIDPersonal());

			setPartnerFromPartnerDto(partner, partnerDto);

			HvDtoLogger<PartnerDto> artikelLogger = new HvDtoLogger<PartnerDto>(em, partner.getIId(), theClientDto);
			artikelLogger.log(dtoVorher, partnerDto);

			getPartnerFac().telefonnummerFuerTapiSynchronisieren(partnerDto.getIId(), null, theClientDto);

			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}

	}

	/**
	 * Hole einen Partner.
	 * 
	 * @param iIdPartnerI  Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return PartnerDto
	 */
	public PartnerDto partnerFindByPrimaryKey(Integer iIdPartnerI, TheClientDto theClientDto) throws EJBExceptionLP {

		// begin JMS testing
		// try {
		// jmstest();
		// }
		// catch (Exception ex1) {
		// }
		// end JMS testing

		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_IN, new Exception("iIdPartnerI == null"));
		}

		PartnerDto partnerDto = null;
		try {
			// Partner lesen.
			Partner partner = em.find(Partner.class, iIdPartnerI);
			if (partner == null) {
				myLogger.error("Partner with primarykey '" + iIdPartnerI + "' not found!");
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, iIdPartnerI.toString());
			}
			partnerDto = assemblePartnerDto(partner);

			if (partnerDto.getLandplzortIId() != null) {
				// Land lesen.
				LandplzortDto landplzortDto = getSystemFac().landplzortFindByPrimaryKey(partnerDto.getLandplzortIId());
				partnerDto.setLandplzortDto(landplzortDto);
			}

			if (partnerDto.getLandplzortIIdPostfach() != null) {
				// Land lesen.
				LandplzortDto landplzortDto = getSystemFac()
						.landplzortFindByPrimaryKey(partnerDto.getLandplzortIIdPostfach());
				partnerDto.setLandplzortDto_Postfach(landplzortDto);
			}

			partnerDto.setGeodatenDto(geodatenFindByPartnerIIdOhneExc(iIdPartnerI));
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(ex);
		} catch (RemoteException ex) {
			// exccatch: hier ist die Catchkaskade
			throwEJBExceptionLPRespectOld(ex);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
		return partnerDto;
	}

	public PartnerDto partnerFindByPrimaryKeyOhneExc(Integer iIdPartnerI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		PartnerDto partnerDto = null;
		try {
			partnerDto = partnerFindByPrimaryKey(iIdPartnerI, theClientDto);
		} catch (EJBExceptionLP ex) {
			myLogger.warn("partnerIId=" + iIdPartnerI);
			return null;
		}
		return partnerDto;
	}

	private void setPartnerFromPartnerDto(Partner partner, PartnerDto partnerDto) {

		partner.setLocaleCNrKommunikation(partnerDto.getLocaleCNrKommunikation());
		partner.setPartnerartCNr(partnerDto.getPartnerartCNr());
		partner.setCKbez(partnerDto.getCKbez());
		partner.setCIln(partnerDto.getCIln());
		partner.setCFilialnummer(partnerDto.getCFilialnummer());
		partner.setAnredeCNr(partnerDto.getAnredeCNr());
		partner.setCName1nachnamefirmazeile1(partnerDto.getCName1nachnamefirmazeile1());
		partner.setCName2vornamefirmazeile2(partnerDto.getCName2vornamefirmazeile2());
		partner.setCName3vorname2abteilung(partnerDto.getCName3vorname2abteilung());
		partner.setCStrasse(partnerDto.getCStrasse());
		partner.setLandplzortIId(partnerDto.getLandplzortIId());
		partner.setLandplzortIIdPostfach(partnerDto.getLandplzortIIdPostfach());
		partner.setCPostfach(partnerDto.getCPostfach());
		partner.setBrancheIId(partnerDto.getBrancheIId());
		partner.setPartnerklasseIId(partnerDto.getPartnerklasseIId());
		partner.setPartnerIIdVater(partnerDto.getPartnerIIdVater());
		partner.setCUid(partnerDto.getCUid());
		partner.setLagerIIdZiellager(partnerDto.getLagerIIdZiellager());
		partner.setXBemerkung(partnerDto.getXBemerkung());
		partner.setTGeburtsdatumansprechpartner(partnerDto.getDGeburtsdatumansprechpartner());
		partner.setRechtsformIId(partnerDto.getRechtsformIId());
		partner.setPartnerIIdEigentuemer(partnerDto.getPartnerIIdEigentuemer());
		partner.setCFirmenbuchnr(partnerDto.getCFirmenbuchnr());
		partner.setCTitel(partnerDto.getCTitel());
		partner.setCNtitel(partnerDto.getCNtitel());
		partner.setCAdressart(partnerDto.getCAdressart());
		partner.setCGerichtsstand(partnerDto.getCGerichtsstand());
		partner.setBVersteckt(partnerDto.getBVersteckt());
		partner.setOBild(partnerDto.getOBild());
		partner.setCDirektfax(partnerDto.getCDirektfax());
		partner.setCEmail(partnerDto.getCEmail());
		partner.setCFax(partnerDto.getCFax());
		partner.setCHandy(partnerDto.getCHandy());
		partner.setCTelefon(partnerDto.getCTelefon());
		partner.setCHomepage(partnerDto.getCHomepage());
		partner.setLandIIdAbweichendesustland(partnerDto.getLandIIdAbweichendesustland());
		partner.setFGmtversatz(partnerDto.getFGmtversatz());
		partner.setCEori(partnerDto.getCEori());
		partner.setVersandwegIId(partnerDto.getVersandwegIId());
		partner.setNewslettergrundIId(partnerDto.getNewslettergrundIId());
		em.merge(partner);
		em.flush();
	}

	/**
	 * Referenz auf System2FacHome erzeugen.
	 * 
	 * @param partner Partner
	 * @return PartnerDto
	 */
	private PartnerDto assemblePartnerDto(Partner partner) {
		return PartnerDtoAssembler.createDto(partner);
	}

	private PartnerDtoSmall assemblePartnerDtoSmall(Partner partner) {
		return PartnerDtoAssembler.createDtoSmall(partner);
	}

	private PartnerDto[] assemblePartnerDtos(Collection<?> partners) {
		List<PartnerDto> list = new ArrayList<PartnerDto>();
		if (partners != null) {
			Iterator<?> iterator = partners.iterator();
			while (iterator.hasNext()) {
				Partner partner = (Partner) iterator.next();
				list.add(assemblePartnerDto(partner));
			}
		}
		PartnerDto[] returnArray = new PartnerDto[list.size()];
		return (PartnerDto[]) list.toArray(returnArray);
	}

	public Integer createPartnerkommunikation(PartnerkommunikationDto partnerkommunikationDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// Precondition.
		if (partnerkommunikationDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("partnerkommunikationDto == null"));
		}
		try {

			Query query = em.createNamedQuery("PartnerkommunikationfindByPartnerIIdKommunikationsartCNrCBez");
			query.setParameter(1, partnerkommunikationDto.getPartnerIId());
			query.setParameter(2, partnerkommunikationDto.getKommunikationsartCNr());
			query.setParameter(3, partnerkommunikationDto.getCBez());
			query.setParameter(4, partnerkommunikationDto.getCInhalt());
			// @todo getSingleResult oder getResultList ?
			Partnerkommunikation partnerkommunikation = (Partnerkommunikation) query.getSingleResult();
			if (partnerkommunikation != null) {
				// CK: Projekt 10510: Wenn gefunden, dann updaten
				setPartnerkommunikationFromPartnerkommunikationDto(partnerkommunikation, partnerkommunikationDto);
				return partnerkommunikation.getIId();
			}

		} catch (NoResultException ex) {
			// nothing here
		}

		try {
			// Generiere neuen Partnerkommunikation PK.
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer iIIdPartnerkommunikationNew = pkGen.getNextPrimaryKey(PKConst.PK_PARTNEKOMMUNIKATION);
			partnerkommunikationDto.setIId(iIIdPartnerkommunikationNew);

			Partnerkommunikation partnerkommunikation = new Partnerkommunikation(partnerkommunikationDto.getIId(),
					partnerkommunikationDto.getPartnerIId(), partnerkommunikationDto.getKommunikationsartCNr(),
					partnerkommunikationDto.getCBez(), partnerkommunikationDto.getCInhalt());
			em.persist(partnerkommunikation);
			em.flush();

			setPartnerkommunikationFromPartnerkommunikationDto(partnerkommunikation, partnerkommunikationDto);
			partnerkommunikationDto.setIId(iIIdPartnerkommunikationNew);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return partnerkommunikationDto.getIId();
	}

	public void removePartnerkommunikation(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		try {
			Partnerkommunikation partnerkommunikation = em.find(Partnerkommunikation.class, iId);
			if (partnerkommunikation == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			// MB 18.05.06 IMS 1661 es duerfen nur Kommunikationsdaten des
			// eigenen mandanten
			// oder private geloescht werden
			if (partnerkommunikation.getMandantCNr() != null) {
				if (!partnerkommunikation.getMandantCNr().equals(theClientDto.getMandant())) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_KOMM_AENDERN_NUR_EIGENER_MANDANT,
							new Exception("FEHLER_PARTNER_KOMM_AENDERN_NUR_EIGENER_MANDANT"));
				}
			}

			em.remove(partnerkommunikation);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		} catch (EJBExceptionLP ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updatePartnerkommunikation(PartnerkommunikationDto partnerkommunikationDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (partnerkommunikationDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("partnerkommunikationDto == null"));
		}
		if (partnerkommunikationDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("partnerkommunikationDto.getIId() == null"));
		}
		if (partnerkommunikationDto.getKommunikationsartCNr() == null || partnerkommunikationDto.getCBez() == null
				|| partnerkommunikationDto.getPartnerIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"partnerkommunikationDto.getKommunikationsartCNr() == null || partnerkommunikationDto.getCBez() == null || partnerkommunikationDto.getPartnerIId() == null"));
		}
		// MB 18.05.06 IMS 1661 es duerfen nur Kommunikationsdaten des eigenen
		// mandanten
		// oder private geaendert werden

		// CK: Auskommentiert, das lt. WH die Telefonnummern und E-Mail Adressen
		// eines Partners
		// usw. eigentlich Systemweit immer gleich sind

		/*
		 * if (partnerkommunikationDto.getCNrMandant() != null) { if
		 * (!partnerkommunikationDto.getCNrMandant().equals( theClientDto.getMandant()))
		 * { throw new EJBExceptionLP(
		 * EJBExceptionLP.FEHLER_PARTNER_KOMM_AENDERN_NUR_EIGENER_MANDANT, new
		 * Exception( "FEHLER_PARTNER_KOMM_AENDERN_NUR_EIGENER_MANDANT")); } }
		 */
		Integer iId = partnerkommunikationDto.getIId();

		Partnerkommunikation partnerkommunikation = em.find(Partnerkommunikation.class, iId);
		if (partnerkommunikation == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setPartnerkommunikationFromPartnerkommunikationDto(partnerkommunikation, partnerkommunikationDto);
	}

	public PartnerkommunikationDto partnerkommunikationFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Partnerkommunikation partnerkommunikation = em.find(Partnerkommunikation.class, iId);
		if (partnerkommunikation == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePartnerkommunikationDto(partnerkommunikation);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public PartnerkommunikationDto partnerkommunikationFindByPrimaryKeyOhneExc(Integer iId) throws EJBExceptionLP {
		Partnerkommunikation partnerkommunikation = em.find(Partnerkommunikation.class, iId);
		if (partnerkommunikation == null) { // @ToDo null Pruefung?
			return null;
		}
		return assemblePartnerkommunikationDto(partnerkommunikation);

		// }
		// catch (ObjectNotFoundException ex) {
		// return null;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setPartnerkommunikationFromPartnerkommunikationDto(Partnerkommunikation partnerkommunikation,
			PartnerkommunikationDto partnerkommunikationDto) {

		partnerkommunikation.setPartnerIId(partnerkommunikationDto.getPartnerIId());
		partnerkommunikation.setKommunikationsartCNr(partnerkommunikationDto.getKommunikationsartCNr());
		partnerkommunikation.setCBez(partnerkommunikationDto.getCBez());
		partnerkommunikation.setCInhalt(partnerkommunikationDto.getCInhalt());
		partnerkommunikation.setMandantCNr(partnerkommunikationDto.getCNrMandant());
		em.merge(partnerkommunikation);
		em.flush();
	}

	private PartnerkommunikationDto assemblePartnerkommunikationDto(Partnerkommunikation partnerkommunikation) {
		return PartnerkommunikationDtoAssembler.createDto(partnerkommunikation);
	}

	// private PartnerkommunikationDto[] assemblePartnerkommunikationDtos(
	// Collection<?> partnerkommunikation) {
	//
	// List list = new ArrayList();
	// if (partnerkommunikation != null) {
	// Iterator<?> iterator = partnerkommunikation.iterator();
	// while (iterator.hasNext()) {
	// Partnerkommunikation partnerkommu = (Partnerkommunikation)
	// iterator.next();
	// list.add(assemblePartnerkommunikationDto(partnerkommu));
	// }
	// }
	// PartnerkommunikationDto[] returnArray = new
	// PartnerkommunikationDto[list.size()];
	// return (PartnerkommunikationDto[]) list.toArray(returnArray);
	// }

	/**
	 * Lesen aller in der DB vorhandenen Partnerarten.
	 * 
	 * @param cNrLocaleI   Sprache
	 * @param theClientDto User
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllPartnerArten(String cNrLocaleI, TheClientDto theClientDto) throws EJBExceptionLP {

		Map<String, String> map = null;

		// try {
		Query query = em.createNamedQuery("PartnerartfindAll");
		Collection<?> allArten = query.getResultList();
		map = new TreeMap<String, String>();

		Iterator<?> iter = allArten.iterator();
		int i = 0;
		while (iter.hasNext()) {
			Partnerart partnerartTemp = (Partnerart) iter.next();
			String key = partnerartTemp.getCNr();
			// Nur diese kommen zur Auswahl! WH: 07.04.05

			String value;
			// try {
			Partnerartspr partnerartspr = em.find(Partnerartspr.class,
					new PartnerartsprPK(cNrLocaleI, partnerartTemp.getCNr()));
			if (partnerartspr == null) {
				value = partnerartTemp.getCNr();
			} else {
				value = partnerartspr.getCBez();
			}
			// }
			// catch (FinderException ex1) {
			// value = partnerartTemp.getCNr();
			// }
			map.put(key, value);
			i++;

		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return map;
	}

	/**
	 * Lesen aller in der DB vorhandenen Anreden.
	 * 
	 * @param locAnredeI Locale
	 * @return TreeMap
	 * @throws EJBExceptionLP
	 */
	public TreeMap getAllAnreden(Locale locAnredeI) throws EJBExceptionLP {

		String locAnredeAsString = null;

		if (locAnredeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("locAnrede == null"));
		}

		locAnredeAsString = Helper.locale2String(locAnredeI);

		TreeMap<String, String> tmAnreden = new TreeMap<String, String>();
		// try {
		Query query = em.createNamedQuery("AnredefindAll");
		Collection<?> allAnreden = query.getResultList();

		Iterator<?> iter = allAnreden.iterator();
		while (iter.hasNext()) {
			Anrede anredeTemp = (Anrede) iter.next();

			String key = anredeTemp.getCNr();
			String value = null;
			// try {
			Anredespr anredespr = em.find(Anredespr.class, new AnredesprPK(anredeTemp.getCNr(), locAnredeAsString));
			if (anredespr == null) {
				value = anredeTemp.getCNr();
			} else {
				value = anredespr.getCBez();
			}
			// }
			// catch (FinderException ex1) {
			// value = anredeTemp.getCNr();
			// }
			tmAnreden.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return tmAnreden;
	}

	public String createAnrede(AnredeDto anredeDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (anredeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("anredeDto == null"));
		}
		if (anredeDtoI.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("anredeDto.getCNr() == null"));
		}

		String ret = null;
		Anrede anrede = null;
		try {
			anrede = new Anrede(anredeDtoI.getCNr());
			em.persist(anrede);
			em.flush();
			Anrede helper = em.find(Anrede.class, anrede.getCNr());
			if (helper == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			ret = helper.getCNr(); // wegen Blanks!

			if (anredeDtoI.getAnredesprDto() != null) {
				String sprache = theClientDto.getLocUiAsString();
				Anredespr anredespr = new Anredespr(anredeDtoI.getCNr(), sprache);
				em.persist(anredespr);
				em.flush();
				setAnredesprFromAnredesprDto(anredespr, anredeDtoI.getAnredesprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return ret; // wegen Blanks!
	}

	public void removeAnrede(String cNrI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cNr == null"));
		}

		try {
			Query query = em.createNamedQuery("AnredesprfindByAnredeCNr");
			query.setParameter(1, cNrI);
			Collection<?> allSpr = query.getResultList();
			Iterator<?> iter = allSpr.iterator();
			while (iter.hasNext()) {
				Anredespr sprTemp = (Anredespr) iter.next();
				em.remove(sprTemp);
			}
			Anrede anrede = em.find(Anrede.class, cNrI);
			if (anrede == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(anrede);
			em.flush();
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateAnrede(AnredeDto anredeDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (anredeDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("anredeDto == null"));
		}
		if (anredeDtoI.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("anredeDto.getCNr() == null"));
		}

		String cNr = anredeDtoI.getCNr();
		// try {
		Anrede anrede = em.find(Anrede.class, cNr);
		if (anrede == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (anredeDtoI.getAnredesprDto() != null) {
			// -- upd oder create
			String sprache = theClientDto.getLocUiAsString();

			if (anredeDtoI.getAnredesprDto().getAnredeCNr() == null) {
				// create
				// Key(teil) setzen.
				anredeDtoI.getAnredesprDto().setAnredeCNr(anredeDtoI.getCNr());
				Anredespr anredespr = new Anredespr(cNr, sprache);
				em.persist(anredespr);
				em.flush();
				setAnredesprFromAnredesprDto(anredespr, anredeDtoI.getAnredesprDto());
			} else {
				// upd
				Anredespr anredespr = em.find(Anredespr.class, new AnredesprPK(cNr, sprache));
				setAnredesprFromAnredesprDto(anredespr, anredeDtoI.getAnredesprDto());
			}
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (CreateException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		// }
	}

	public AnredeDto anredeFindByPrimaryKey(String cNrI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cNr == null"));
		}

		AnredeDto anredeDto = null;
		// try {
		Anrede anrede = em.find(Anrede.class, cNrI);
		if (anrede == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		anredeDto = assembleAnredeDto(anrede);

		AnredesprDto anredesprDto = null;
		// try {
		String sprache = theClientDto.getLocUiAsString();
		Anredespr anredespr = em.find(Anredespr.class, new AnredesprPK(cNrI, sprache));
		if (anredespr != null) {
			anredesprDto = assembleAnredesprDto(anredespr);
		}
		// }
		// catch (FinderException ex) {
		// nothing here
		// }

		anredeDto.setAnredesprDto(anredesprDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return anredeDto;
	}

	private AnredeDto assembleAnredeDto(Anrede anrede) {
		return AnredeDtoAssembler.createDto(anrede);
	}

	public Integer createPartnerklasse(PartnerklasseDto partnerklasseDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (partnerklasseDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("partnerklasseDtoI == null"));
		}
		if (partnerklasseDtoI.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("partnerklasseDtoI.getCNr() == null"));
		}

		try {
			Query query = em.createNamedQuery("PartnerklassefindByCNr");
			query.setParameter(1, partnerklasseDtoI.getCNr());
			// @todo getSingleResult oder getResultList ?
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_PARTNERKLASSE.CNR"));
		}

		catch (NoResultException ex) {

		}

		Integer iId = null;
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		iId = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERKLASSE);
		partnerklasseDtoI.setIId(iId);

		Partnerklasse partnerklasse = new Partnerklasse(partnerklasseDtoI.getIId(), partnerklasseDtoI.getCNr());
		partnerklasse.setCImportart(partnerklasseDtoI.getCImportart());
		em.persist(partnerklasse);
		em.flush();

		if (partnerklasseDtoI.getPartnerklassesprDto() != null) {
			Partnerklassespr partnerklassespr = new Partnerklassespr(partnerklasseDtoI.getIId(),
					theClientDto.getLocUiAsString());
			em.persist(partnerklassespr);
			em.flush();

			setPartnerklassesprFromPartnerklassesprDto(partnerklassespr, partnerklasseDtoI.getPartnerklassesprDto());
		}
		return iId;
	}

	public void removePartnerklasse(Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdI == null"));
		}

		try {
			Query query = em.createNamedQuery("PartnerklassesprfindByPartnerklasseIId");
			query.setParameter(1, iIdI);
			Collection<?> allSpr = query.getResultList();
			Iterator<?> iter = allSpr.iterator();
			while (iter.hasNext()) {
				Partnerklassespr sprTemp = (Partnerklassespr) iter.next();
				em.remove(sprTemp);
			}
			Partnerklasse partnerklasse = em.find(Partnerklasse.class, iIdI);
			if (partnerklasse == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			em.remove(partnerklasse);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void updatePartnerklasse(PartnerklasseDto partnerklasseDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// Precondition.
		if (partnerklasseDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("partnerklasseDto == null"));
		}
		if (partnerklasseDtoI.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("partnerklasseDtoI.getCNr() == null"));
		}

		Integer iId = partnerklasseDtoI.getIId();
		try {
			Query query = em.createNamedQuery("PartnerklassefindByCNr");
			query.setParameter(1, partnerklasseDtoI.getCNr());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Partnerklasse) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_PARTNERKLASSE.CNR"));
			}
		} catch (NoResultException ex) {
			//
		}

		try {
			Partnerklasse partnerklasse = em.find(Partnerklasse.class, iId);
			if (partnerklasse == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			partnerklasse.setCNr(partnerklasseDtoI.getCNr());
			partnerklasse.setCImportart(partnerklasseDtoI.getCImportart());
			if (partnerklasseDtoI.getPartnerklassesprDto() != null) {
				// -- upd oder create
				if (partnerklasseDtoI.getPartnerklassesprDto().getPartnerklasseIId() == null) {
					// create
					// Key(teil) setzen.
					partnerklasseDtoI.getPartnerklassesprDto().setPartnerklasseIId(partnerklasseDtoI.getIId());

					Partnerklassespr partnerklassespr = new Partnerklassespr(iId, theClientDto.getLocUiAsString());
					em.persist(partnerklassespr);
					em.flush();
					setPartnerklassesprFromPartnerklassesprDto(partnerklassespr,
							partnerklasseDtoI.getPartnerklassesprDto());
				} else {
					// upd
					Partnerklassespr partnerklassespr = em.find(Partnerklassespr.class,
							new PartnerklassesprPK(iId, theClientDto.getLocUiAsString()));
					setPartnerklassesprFromPartnerklassesprDto(partnerklassespr,
							partnerklasseDtoI.getPartnerklassesprDto());
				}
			}
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		}
	}

	public PartnerklasseDto partnerklasseFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto) {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdI == null"));
		}

		// try {
		Partnerklasse partnerklasse = em.find(Partnerklasse.class, iIdI);
		if (partnerklasse == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		PartnerklasseDto partnerklasseDto = assemblePartnerklasseDto(partnerklasse);

		try {
			Partnerklassespr partnerklassespr = em.find(Partnerklassespr.class,
					new PartnerklassesprPK(iIdI, theClientDto.getLocUiAsString()));
			partnerklasseDto.setPartnerklassesprDto(assemblePartnerklassesprDto(partnerklassespr));
		} catch (Throwable t) {
			// nothing here.
		}

		return partnerklasseDto;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private PartnerklasseDto assemblePartnerklasseDto(Partnerklasse partnerklasse) {
		return PartnerklasseDtoAssembler.createDto(partnerklasse);
	}

	private PartnerartDto assemblePartnerartDto(Partnerart partnerart) {
		return PartnerartDtoAssembler.createDto(partnerart);
	}

	private PartnerartDto[] assemblePartnerartDtos(Collection<?> partnerarts) {
		List<PartnerartDto> list = new ArrayList<PartnerartDto>();
		if (partnerarts != null) {
			Iterator<?> iterator = partnerarts.iterator();
			while (iterator.hasNext()) {
				Partnerart partnerart = (Partnerart) iterator.next();
				list.add(assemblePartnerartDto(partnerart));
			}
		}
		PartnerartDto[] returnArray = new PartnerartDto[list.size()];
		return (PartnerartDto[]) list.toArray(returnArray);
	}

	private void setPartnerartsprFromPartnerartsprDto(Partnerartspr partnerartspr, PartnerartsprDto partnerartsprDto) {
		partnerartspr.setCBez(partnerartsprDto.getCBez());
		em.merge(partnerartspr);
		em.flush();
	}

	private PartnerartsprDto assemblePartnerartsprDto(Partnerartspr partnerartspr) {
		return PartnerartsprDtoAssembler.createDto(partnerartspr);
	}

	private void setAnredesprFromAnredesprDto(Anredespr anredespr, AnredesprDto anredesprDto) {
		anredespr.setCBez(anredesprDto.getCBez());
		em.merge(anredespr);
		em.flush();
	}

	private AnredesprDto assembleAnredesprDto(Anredespr anredespr) {
		return AnredesprDtoAssembler.createDto(anredespr);
	}

	private void setPartnerklassesprFromPartnerklassesprDto(Partnerklassespr partnerklassespr,
			PartnerklassesprDto partnerklassesprDto) {
		partnerklassespr.setCBez(partnerklassesprDto.getCBez());
		em.merge(partnerklassespr);
		em.flush();
	}

	private PartnerklassesprDto assemblePartnerklassesprDto(Partnerklassespr partnerklassespr) {
		return PartnerklassesprDtoAssembler.createDto(partnerklassespr);
	}

	public Map getAllPersonenWiedervorlage(TheClientDto theClientDto) {

		TreeMap<Integer, String> m = new TreeMap<Integer, String>();

		Session session = FLRSessionFactory.getFactory().openSession();

		// String sQuery =
		// "SELECT distinct(kontakt.flrpersonal) FROM FLRKontakt AS kontakt WHERE
		// kontakt.t_erledigt IS NULL ORDER BY kontakt.flrpersonal.c_kurzzeichen ASC";
		String sQuery = "SELECT flrpersonal FROM FLRPersonal AS flrpersonal WHERE flrpersonal.i_id IN (SELECT distinct(kontakt.flrpersonal.i_id) FROM FLRKontakt AS kontakt WHERE kontakt.t_erledigt IS NULL) ORDER BY flrpersonal.c_kurzzeichen ASC";

		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();

		boolean bIchBinSelbstSchonDabei = false;

		while (resultListIterator.hasNext()) {
			FLRPersonal p = (FLRPersonal) resultListIterator.next();

			if (theClientDto.getIDPersonal().equals(p.getI_id())) {
				bIchBinSelbstSchonDabei = true;
			}

			String sValue = "";
			if (p.getC_kurzzeichen() != null) {
				sValue = p.getC_kurzzeichen() + " ";
			}

			if (p.getFlrpartner().getC_name2vornamefirmazeile2() != null) {
				sValue = p.getFlrpartner().getC_name2vornamefirmazeile2() + " ";
			}

			if (p.getFlrpartner().getC_name1nachnamefirmazeile1() != null) {
				sValue = p.getFlrpartner().getC_name1nachnamefirmazeile1();
			}

			m.put(p.getI_id(), sValue);

		}

		session.close();

		if (bIchBinSelbstSchonDabei == false) {

			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);

			m.put(personalDto.getIId(), personalDto.getPartnerDto().formatFixName2Name1());

		}

		// Nun noch ALLE hinzufuegen

		m.put(new Integer(-1), getTextRespectUISpr("lp.alle", theClientDto.getMandant(), theClientDto.getLocUi()));

		return m;
	}

	public Integer createKontakt(KontaktDto kontaktDto, TheClientDto theClientDto) {
		if (kontaktDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kontaktDto == null"));
		}
		if (kontaktDto.getCTitel() == null || kontaktDto.getPartnerIId() == null
				|| kontaktDto.getKontaktartIId() == null || kontaktDto.getPersonalIIdZugewiesener() == null
				|| kontaktDto.getTKontakt() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"kontaktDto.getCTitel() == null || kontaktDto.getPartnerIId() == null || kontaktDto.getKontaktartIId() == null || kontaktDto.getPersonalIIdZugewiesener() == null || kontaktDto.getTKontakt() == null"));
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KONTAKT);
			kontaktDto.setIId(pk);

			kontaktDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			kontaktDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));

			Kontakt kontakt = new Kontakt(kontaktDto.getIId(), kontaktDto.getCTitel(), kontaktDto.getPartnerIId(),
					kontaktDto.getPersonalIIdZugewiesener(), kontaktDto.getKontaktartIId(), kontaktDto.getTKontakt(),
					kontaktDto.getPersonalIIdAnlegen(), kontaktDto.getTAnlegen());
			em.persist(kontakt);
			em.flush();
			setKontaktFromKontaktDto(kontakt, kontaktDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return kontaktDto.getIId();
	}

	public void removeKontakt(KontaktDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}

		Integer iId = dto.getIId();
		Kontakt toRemove = em.find(Kontakt.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	@Override
	public List<TelefonSuchergebnisDto> findeTelefonnummer(String telefonnummer, boolean modified,
			TheClientDto theClientDto) {
		if (Helper.isStringEmpty(telefonnummer)) {
			return new ArrayList<TelefonSuchergebnisDto>();
		}

		if (modified) {
			return sucheTelefonnummer(telefonnummer, theClientDto);
		}

		if (telefonnummer.startsWith("+")) {
			return sucheTelefonnummer(telefonnummer.substring(1), theClientDto);
		}

		String telefonnummerOri = telefonnummer;
		MandantDto[] mDtos = getMandantFac().mandantFindAll(theClientDto);

		for (MandantDto mDto : mDtos) {
			telefonnummer = telefonnummerOri;

			try {
				ParametermandantDto pAuslandsvorwahl = getParameterFac().getMandantparameter(mDto.getCNr(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_AUSLANDSVORWAHL);
				String sAuslandsvorwahl = pAuslandsvorwahl.getCWert().trim();

				ParametermandantDto pMaxLangeInterneNummer = getParameterFac().getMandantparameter(mDto.getCNr(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_MAXIMALLAENGE_INTERNE_TELEFONNUMMER);
				Integer maxLaengeInterneNummer = (Integer) pMaxLangeInterneNummer.getCWertAsObject();

				if (telefonnummer.startsWith(sAuslandsvorwahl)) {
					telefonnummer = telefonnummer.substring(sAuslandsvorwahl.length());
				} else {
					if (telefonnummer.length() > maxLaengeInterneNummer) {

						PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(mDto.getPartnerIId(),
								theClientDto);

						if (partnerDto.getLandplzortDto() != null
								&& partnerDto.getLandplzortDto().getLandDto().getCTelvorwahl() != null) {

							String vorwahl = partnerDto.getLandplzortDto().getLandDto().getCTelvorwahl();
							if (vorwahl.startsWith("+")) {
								vorwahl = vorwahl.substring(1);
							}

							else if (vorwahl.startsWith(sAuslandsvorwahl)) {
								vorwahl = vorwahl.substring(sAuslandsvorwahl.length());
							}

							telefonnummer = vorwahl + telefonnummer;
						}
					}
				}

				List<TelefonSuchergebnisDto> ergebnis = sucheTelefonnummer(telefonnummer, theClientDto);

				if (ergebnis.size() > 0) {
					return ergebnis;
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		return sucheTelefonnummer(telefonnummerOri, theClientDto);
	}

	private List<TelefonSuchergebnisDto> sucheTelefonnummer(String telefonnummer, TheClientDto theClientDto) {

		List<TelefonSuchergebnisDto> ergebnis = new ArrayList<TelefonSuchergebnisDto>();

		Query query = em.createQuery(
				"SELECT t FROM  Telefonnummer t WHERE t.cNummer = '" + telefonnummer + "' ORDER BY t.bVersteckt ASC");
		List l = query.getResultList();
		Iterator it = l.iterator();
		while (it.hasNext()) {
			Telefonnummer t = (Telefonnummer) it.next();

			PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(t.getPartnerIId(), theClientDto);

			TelefonSuchergebnisDto telefonSuchergebnisDto = new TelefonSuchergebnisDto(t.getPartnerIId(),
					t.getAnsprechpartnerIId(), pDto.formatAnrede(), null, Helper.short2boolean(t.getBVersteckt()));

			if (t.getAnsprechpartnerIId() != null) {
				try {
					AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(t.getAnsprechpartnerIId(), theClientDto);

					telefonSuchergebnisDto
							.setAnsprechpartner(anspDto.getPartnerDto().formatFixTitelVornameNachnameNTitel());

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			ergebnis.add(telefonSuchergebnisDto);
		}
		return ergebnis;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void testAdressdatenexport(TheClientDto theClientDto) {
		testAdressdatenexport(null, theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void testAdressdatenexport(String cEmailAdresse, TheClientDto theClientDto) {

		System.out.println("BEGIN:" + new Timestamp(System.currentTimeMillis()));

		int iAnzahl = 0;
		int iGesamt = 0;
		do {

			ArrayList<AdressbuchExportDto> alDaten;

			if (cEmailAdresse == null) {
				alDaten = getPartnerFac().getDatenFuerAdressbuchExport(1000, theClientDto);
			} else {
				alDaten = getPartnerFac().getDatenFuerAdressbuchExport(1000, cEmailAdresse, theClientDto);
			}

			iAnzahl = alDaten.size();

			for (int i = 0; i < alDaten.size(); i++) {
				AdressbuchExportDto dto = alDaten.get(i);

				System.out.println(dto.toString());

				getPartnerFac().adressdatensatzEinesPartnersAlsExportiertMarkieren(dto.getPARTNER_I_ID(),
						dto.getANSPRECHPARTNER_I_ID(), cEmailAdresse, "EX_ID" + iGesamt);
				iGesamt++;
				System.out.println("GESAMT:" + iGesamt);
			}
		} while (iAnzahl > 0);

		System.out.println("ENDE:" + new Timestamp(System.currentTimeMillis()));
	}

	public void adressdatensatzEinesPartnersAlsExportiertMarkieren(Integer partnerIId, Integer ansprechpartnerIId,
			String cEmailAdresse, String exchangeId) {

		if (cEmailAdresse == null) {

			if (ansprechpartnerIId != null) {
				Ansprechpartner ansp = em.find(Ansprechpartner.class, ansprechpartnerIId);

				ansp.setTZuletztExportiert(new Timestamp(System.currentTimeMillis()));
				ansp.setCExchangeid(exchangeId);
				em.merge(ansp);
				em.flush();
			} else {
				Partner partner = em.find(Partner.class, partnerIId);

				partner.setTZuletztExportiert(new Timestamp(System.currentTimeMillis()));
				partner.setCExchangeid(exchangeId);
				em.merge(partner);
				em.flush();
			}
		} else {

			cEmailAdresse = cEmailAdresse.toLowerCase();

			Timestamp tJetzt = new Timestamp(System.currentTimeMillis());

			if (ansprechpartnerIId != null) {
				try {

					Query query = em
							.createNamedQuery(Ansprechpartneradressbuch.QueryFindByCEmailadresseAnsprechpartnerIId);
					query.setParameter(1, cEmailAdresse);
					query.setParameter(2, ansprechpartnerIId);
					Ansprechpartneradressbuch ansprechpartneradressbuch = (Ansprechpartneradressbuch) query
							.getSingleResult();

					ansprechpartneradressbuch.setCExchangeid(exchangeId);
					ansprechpartneradressbuch.setTZuletztExportiert(tJetzt);
					em.merge(ansprechpartneradressbuch);
					em.flush();
				}

				catch (NoResultException ex) {

					PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
					Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ANSPRECHPARTNERADRESSBUCH);

					Ansprechpartneradressbuch ansprechpartneradressbuch = new Ansprechpartneradressbuch(pk,
							cEmailAdresse, ansprechpartnerIId, exchangeId, tJetzt);
					em.merge(ansprechpartneradressbuch);
					em.flush();
				}
			} else {

				try {

					Query query = em.createNamedQuery(Partneradressbuch.QueryFindByCEmailadresseAnsprechpartnerIId);
					query.setParameter(1, cEmailAdresse);
					query.setParameter(2, partnerIId);
					Partneradressbuch partneradressbuch = (Partneradressbuch) query.getSingleResult();

					partneradressbuch.setCExchangeid(exchangeId);
					partneradressbuch.setTZuletztExportiert(tJetzt);
					em.merge(partneradressbuch);
					em.flush();
				}

				catch (NoResultException ex) {

					PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
					Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERADRESSBUCH);

					Partneradressbuch partneradressbuch = new Partneradressbuch(pk, cEmailAdresse, partnerIId,
							exchangeId, tJetzt);
					em.merge(partneradressbuch);
					em.flush();
				}
			}

		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<AdressbuchExportDto> getDatenFuerAdressbuchExport(int iMaximaleAanzahl,
			TheClientDto theClientDto) {
		ArrayList<AdressbuchExportDto> alExport = new ArrayList<AdressbuchExportDto>();

		String sQuery = "SELECT flrpartner,ansprechpartnerset FROM FLRPartner AS flrpartner LEFT OUTER JOIN flrpartner.ansprechpartner AS ansprechpartnerset WHERE flrpartner.b_versteckt=0 AND (ansprechpartnerset.b_versteckt IS NULL or ansprechpartnerset.b_versteckt=0 ) AND ((flrpartner.t_zuletzt_exportiert IS NULL OR flrpartner.t_zuletzt_exportiert<flrpartner.t_aendern )  AND (ansprechpartnerset.t_zuletzt_exportiert IS NULL OR ansprechpartnerset.t_zuletzt_exportiert<ansprechpartnerset.t_aendern )) ORDER BY flrpartner.c_name1nachnamefirmazeile1 ASC";
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(sQuery);
		query.setMaxResults(iMaximaleAanzahl);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();

		HashSet<Integer> hmPartnerBereitsHinzugefuegt = new HashSet<Integer>();

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			FLRPartner p = (FLRPartner) o[0];
			FLRAnsprechpartner ansp = (FLRAnsprechpartner) o[1];

			if (!hmPartnerBereitsHinzugefuegt.contains(p.getI_id())) {

				if (p.getT_zuletzt_exportiert() == null
						|| p.getT_zuletzt_exportiert().getTime() < p.getT_aendern().getTime()) {

					AdressbuchExportDto aExpDto = new AdressbuchExportDto();

					aExpDto.setExportId(p.getC_exchangeid());

					// Man ist eine Firma, wenn die Anrede Firma oder NULL ist
					if (p.getAnrede_c_nr() == null || p.getAnrede_c_nr().equals(PartnerFac.PARTNER_ANREDE_FIRMA)) {
						aExpDto.setCompany(HelperServer.formatNameAusFLRPartner(p));
					}
					aExpDto.setDisplayName(HelperServer.formatNameAusFLRPartner(p));

					aExpDto.setEmail(p.getC_email());

					aExpDto.setFirstName(p.getC_name1nachnamefirmazeile1());
					aExpDto.setLastName(p.getC_name2vornamefirmazeile2());

					aExpDto.setNotes(p.getX_bemerkung());
					aExpDto.setPARTNER_I_ID(p.getI_id());
					aExpDto.setPhone(p.getC_telefon());

					aExpDto.setStressAdress(p.getC_strasse());

					if (p.getFlrlandplzort() != null) {
						aExpDto.setCity(p.getFlrlandplzort().getFlrort().getC_name());
						aExpDto.setCountry(p.getFlrlandplzort().getFlrland().getC_name());
						aExpDto.setPostalCode(p.getFlrlandplzort().getC_plz());
					}

					alExport.add(aExpDto);
				}

				hmPartnerBereitsHinzugefuegt.add(p.getI_id());

			}

			// und deren Ansprechpartner

			if (ansp != null) {

				if (ansp.getT_zuletzt_exportiert() == null
						|| ansp.getT_zuletzt_exportiert().getTime() < ansp.getT_aendern().getTime()) {

					AdressbuchExportDto aExpDtoAnsp = new AdressbuchExportDto();
					aExpDtoAnsp.setANSPRECHPARTNER_I_ID(ansp.getI_id());
					aExpDtoAnsp.setExportId(ansp.getFlrpartneransprechpartner().getC_exchangeid());
					// Man ist eine Firma, wenn die Anrede Firma oder NULL ist
					if (p.getAnrede_c_nr() == null || p.getAnrede_c_nr().equals(PartnerFac.PARTNER_ANREDE_FIRMA)) {
						aExpDtoAnsp.setCompany(HelperServer.formatNameAusFLRPartner(p));
					}

					aExpDtoAnsp
							.setDisplayName(HelperServer.formatPersonAusFLRPartner(ansp.getFlrpartneransprechpartner()));

					aExpDtoAnsp.setEmail(ansp.getC_email());

					aExpDtoAnsp.setFirstName(ansp.getFlrpartneransprechpartner().getC_name2vornamefirmazeile2());
					aExpDtoAnsp.setLastName(ansp.getFlrpartneransprechpartner().getC_name1nachnamefirmazeile1());
					
					aExpDtoAnsp.setNotes(ansp.getFlrpartneransprechpartner().getX_bemerkung());
					aExpDtoAnsp.setPARTNER_I_ID(ansp.getFlrpartneransprechpartner().getI_id());

					if (Helper.short2boolean(ansp.getB_durchwahl()) && ansp.getC_telefon() != null) {
						String tel = enrichNumber(p.getI_id(), PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto,
								ansp.getC_telefon(), false);
						aExpDtoAnsp.setPhone(tel);

					} else {
						aExpDtoAnsp.setPhone(ansp.getC_telefon());
					}

					aExpDtoAnsp.setStressAdress(p.getC_strasse());

					if (p.getFlrlandplzort() != null) {
						aExpDtoAnsp.setCity(p.getFlrlandplzort().getFlrort().getC_name());
						aExpDtoAnsp.setCountry(p.getFlrlandplzort().getFlrland().getC_name());
						aExpDtoAnsp.setPostalCode(p.getFlrlandplzort().getC_plz());
					}

					aExpDtoAnsp.setMobilePhone(ansp.getC_handy());
					alExport.add(aExpDtoAnsp);

				}
			}

		}

		return alExport;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<AdressbuchExportDto> getDatenFuerAdressbuchExport(int iMaximaleAanzahl, String cEmailAdresse,
			TheClientDto theClientDto) {

		ArrayList<AdressbuchExportDto> alExport = new ArrayList<AdressbuchExportDto>();

		if (cEmailAdresse != null) {

			cEmailAdresse = cEmailAdresse.toLowerCase();

			java.sql.Date dVorZweiJAhren = Helper.addiereTageZuDatum(new java.util.Date(), -730);

			boolean syncall = false;

			String sQueryPersonal = "SELECT flrpersonal FROM FLRPersonal flrpersonal WHERE lower(flrpersonal.c_email)='"
					+ cEmailAdresse.toLowerCase() + "'";

			Session sessionPersonal = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Query queryPersonal = sessionPersonal.createQuery(sQueryPersonal);
			List<?> resultListPersonal = queryPersonal.list();
			Iterator<?> resultListIteratorPersonal = resultListPersonal.iterator();

			HashSet<Integer> personalIIds = new HashSet<Integer>();

			while (resultListIteratorPersonal.hasNext()) {
				FLRPersonal flrPersonal = (FLRPersonal) resultListIteratorPersonal.next();

				personalIIds.add(flrPersonal.getI_id());
				if (Helper.short2boolean(flrPersonal.getB_synch_alle_kontakte())) {
					syncall = true;
				}
			}

			String inPersonal = "(";
			Iterator itPersonal = personalIIds.iterator();
			while (itPersonal.hasNext()) {
				Integer personalIId = (Integer) itPersonal.next();

				inPersonal += personalIId;

				if (itPersonal.hasNext()) {
					inPersonal += ",";
				}

			}
			inPersonal += ")";

			if (personalIIds.size() == 0) {
				inPersonal = "(-999)";
			}

			String sQuery = null;
			HashSet<Integer> hmPartnerIIDs = new HashSet<Integer>();
			HashSet<Integer> hmAnsprechpartnerIIDs = new HashSet<Integer>();

			if (!syncall) {
				// PROJEKT
				String sQueryPartnerIN = "SELECT distinct flrprojekt.partner_i_id, flrprojekt.ansprechpartner_i_id FROM FLRProjekt flrprojekt LEFT OUTER JOIN flrprojekt.historyset AS hs WHERE flrprojekt.t_anlegen > '"
						+ Helper.formatDateWithSlashes(dVorZweiJAhren)
						+ "' AND  flrprojekt.personal_i_id_zugewiesener IN " + inPersonal
						+ " OR hs.flrpersonal_wirddurchgefuehrtvon.i_id IN" + inPersonal;

				Session sessionIN = FLRSessionFactory.getFactory().openSession();
				org.hibernate.Query queryIN = sessionIN.createQuery(sQueryPartnerIN);
				List<?> resultListIN = queryIN.list();
				Iterator<?> resultListIteratorIN = resultListIN.iterator();

				while (resultListIteratorIN.hasNext()) {
					Object[] o = (Object[]) resultListIteratorIN.next();
					Integer partnerIId = (Integer) o[0];
					Integer ansprechpartnerIId = (Integer) o[1];

					if (partnerIId == 68780) {
						int z = 0;
					}

					hmPartnerIIDs.add(partnerIId);
					if (ansprechpartnerIId != null) {
						hmAnsprechpartnerIIDs.add(ansprechpartnerIId);
					}

				}

				// ANGEBOT
				sQueryPartnerIN = "SELECT distinct flrangebot.flrkunde.flrpartner.i_id, flrangebot.ansprechpartner_i_id_kunde FROM FLRAngebot flrangebot WHERE flrangebot.t_belegdatum > '"
						+ Helper.formatDateWithSlashes(dVorZweiJAhren) + "' AND flrangebot.flrvertreter.i_id IN "
						+ inPersonal;

				sessionIN = FLRSessionFactory.getFactory().openSession();
				queryIN = sessionIN.createQuery(sQueryPartnerIN);
				resultListIN = queryIN.list();
				resultListIteratorIN = resultListIN.iterator();

				while (resultListIteratorIN.hasNext()) {
					Object[] o = (Object[]) resultListIteratorIN.next();
					Integer partnerIId = (Integer) o[0];
					Integer ansprechpartnerIId = (Integer) o[1];

					hmPartnerIIDs.add(partnerIId);
					if (ansprechpartnerIId != null) {
						hmAnsprechpartnerIIDs.add(ansprechpartnerIId);
					}

				}

				// AUFTRAG
				sQueryPartnerIN = "SELECT distinct flrauftrag.flrkunde.flrpartner.i_id, flrauftrag.ansprechpartner_i_id_kunde FROM FLRAuftrag flrauftrag WHERE flrauftrag.t_belegdatum > '"
						+ Helper.formatDateWithSlashes(dVorZweiJAhren) + "' AND flrauftrag.flrvertreter.i_id IN"
						+ inPersonal;

				sessionIN = FLRSessionFactory.getFactory().openSession();
				queryIN = sessionIN.createQuery(sQueryPartnerIN);
				resultListIN = queryIN.list();
				resultListIteratorIN = resultListIN.iterator();

				while (resultListIteratorIN.hasNext()) {
					Object[] o = (Object[]) resultListIteratorIN.next();
					Integer partnerIId = (Integer) o[0];
					Integer ansprechpartnerIId = (Integer) o[1];

					hmPartnerIIDs.add(partnerIId);
					if (ansprechpartnerIId != null) {
						hmAnsprechpartnerIIDs.add(ansprechpartnerIId);
					}

				}

				// AUFTRAEGE bei denen ich Teilnehemer bin
				sQueryPartnerIN = "SELECT distinct flrAuftragteilnehmer.flrauftrag.flrkunde.flrpartner.i_id, flrAuftragteilnehmer.flrauftrag.ansprechpartner_i_id_kunde FROM FLRAuftragteilnehmer flrAuftragteilnehmer WHERE flrAuftragteilnehmer.flrauftrag.t_belegdatum > '"
						+ Helper.formatDateWithSlashes(dVorZweiJAhren)
						+ "' AND flrAuftragteilnehmer.flrpersonal.i_id IN " + inPersonal;

				sessionIN = FLRSessionFactory.getFactory().openSession();
				queryIN = sessionIN.createQuery(sQueryPartnerIN);
				resultListIN = queryIN.list();
				resultListIteratorIN = resultListIN.iterator();

				while (resultListIteratorIN.hasNext()) {
					Object[] o = (Object[]) resultListIteratorIN.next();
					Integer partnerIId = (Integer) o[0];
					Integer ansprechpartnerIId = (Integer) o[1];

					hmPartnerIIDs.add(partnerIId);
					if (ansprechpartnerIId != null) {
						hmAnsprechpartnerIIDs.add(ansprechpartnerIId);
					}

				}

				// RECHNUNG
				sQueryPartnerIN = "SELECT distinct flrrechnung.flrkunde.flrpartner.i_id, flrrechnung.ansprechpartner_i_id FROM FLRRechnung flrrechnung WHERE flrrechnung.d_belegdatum > '"
						+ Helper.formatDateWithSlashes(dVorZweiJAhren) + "' AND flrrechnung.flrvertreter.i_id IN "
						+ inPersonal;

				sessionIN = FLRSessionFactory.getFactory().openSession();
				queryIN = sessionIN.createQuery(sQueryPartnerIN);
				resultListIN = queryIN.list();
				resultListIteratorIN = resultListIN.iterator();

				while (resultListIteratorIN.hasNext()) {
					Object[] o = (Object[]) resultListIteratorIN.next();
					Integer partnerIId = (Integer) o[0];
					Integer ansprechpartnerIId = (Integer) o[1];

					hmPartnerIIDs.add(partnerIId);
					if (ansprechpartnerIId != null) {
						hmAnsprechpartnerIIDs.add(ansprechpartnerIId);
					}

				}

				// Kunde, bei denen ich Provisionsempfaenger bin
				sQueryPartnerIN = "SELECT distinct flrkunde.flrpartner.i_id FROM FLRKunde flrkunde WHERE  flrkunde.personal_i_id_bekommeprovision IN "
						+ inPersonal;

				sessionIN = FLRSessionFactory.getFactory().openSession();
				queryIN = sessionIN.createQuery(sQueryPartnerIN);
				resultListIN = queryIN.list();
				resultListIteratorIN = resultListIN.iterator();

				while (resultListIteratorIN.hasNext()) {
					Integer partnerIId = (Integer) resultListIteratorIN.next();
					hmPartnerIIDs.add(partnerIId);
				}

				// Kunde, bei denen ich Sachbearbeiter bin
				sQueryPartnerIN = "SELECT distinct flrsachbearbeiter.flrkunde.flrpartner.i_id FROM FLRSachbearbeiter flrsachbearbeiter WHERE flrsachbearbeiter.flrpersonal.i_id IN"
						+ inPersonal;

				sessionIN = FLRSessionFactory.getFactory().openSession();
				queryIN = sessionIN.createQuery(sQueryPartnerIN);
				resultListIN = queryIN.list();
				resultListIteratorIN = resultListIN.iterator();

				while (resultListIteratorIN.hasNext()) {
					Integer partnerIId = (Integer) resultListIteratorIN.next();
					hmPartnerIIDs.add(partnerIId);
				}

			}

			String inPArtner = "(";
			Iterator itPartner = hmPartnerIIDs.iterator();
			while (itPartner.hasNext()) {
				Integer partnerIId = (Integer) itPartner.next();

				inPArtner += partnerIId;

				if (itPartner.hasNext()) {
					inPArtner += ",";
				}

			}
			inPArtner += ")";

			if (hmPartnerIIDs.size() == 0) {
				inPArtner = "(-999)";
			}

			String inAnsprechpArtner = "(";
			Iterator itAnsprechpartner = hmAnsprechpartnerIIDs.iterator();
			while (itAnsprechpartner.hasNext()) {
				Integer ansprechpartnerIId = (Integer) itAnsprechpartner.next();

				inAnsprechpArtner += ansprechpartnerIId;

				if (itAnsprechpartner.hasNext()) {
					inAnsprechpArtner += ",";
				}

			}
			inAnsprechpArtner += ")";

			if (hmAnsprechpartnerIIDs.size() == 0) {
				inAnsprechpArtner = "(-999)";
			}

			if (syncall) {
				sQuery = "SELECT p, (SELECT padr.c_exchangeid FROM FLRPartneradressbuch padr WHERE padr.c_emailadresse = '"
						+ cEmailAdresse
						+ "' AND padr.partner_i_id = p.i_id ) AS exchange_id FROM FLRPartner p WHERE p.b_versteckt = 0 AND (p.c_telefon IS NOT NULL OR p.c_email IS NOT NULL) AND p.i_id NOT IN (SELECT padr.partner_i_id FROM FLRPartneradressbuch padr WHERE padr.c_emailadresse='"
						+ cEmailAdresse + "' AND padr.t_zuletzt_exportiert >p.t_aendern )";

			} else {
				sQuery = "SELECT p, (SELECT padr.c_exchangeid FROM FLRPartneradressbuch padr WHERE padr.c_emailadresse='"
						+ cEmailAdresse
						+ "' AND padr.partner_i_id = p.i_id ) AS exchange_id FROM FLRPartner p WHERE p.b_versteckt = 0 AND p.i_id IN "
						+ inPArtner
						+ " AND p.i_id NOT IN (SELECT padr.partner_i_id FROM FLRPartneradressbuch padr WHERE padr.c_emailadresse='"
						+ cEmailAdresse + "' AND padr.t_zuletzt_exportiert >p.t_aendern )";

			}

			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query query = session.createQuery(sQuery);
			query.setMaxResults(iMaximaleAanzahl);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();

			HashSet<Integer> hmPartnerBereitsHinzugefuegt = new HashSet<Integer>();

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();

				FLRPartner p = (FLRPartner) o[0];

				String exchangeid = (String) o[1];

				if (p.getI_id() == 68780) {
					int z = 0;
				}

				AdressbuchExportDto aExpDto = new AdressbuchExportDto();

				aExpDto.setExportId(exchangeid);

				// Man ist eine Firma, wenn die Anrede Firma oder NULL ist
				if (p.getAnrede_c_nr() == null || p.getAnrede_c_nr().equals(PartnerFac.PARTNER_ANREDE_FIRMA)) {
					aExpDto.setCompany(HelperServer.formatNameAusFLRPartner(p));
				}
				aExpDto.setDisplayName(HelperServer.formatNameAusFLRPartner(p));

				aExpDto.setEmail(p.getC_email());

				aExpDto.setFirstName(p.getC_name1nachnamefirmazeile1());
				aExpDto.setLastName(p.getC_name2vornamefirmazeile2());

				aExpDto.setNotes(p.getX_bemerkung());
				aExpDto.setPARTNER_I_ID(p.getI_id());
				aExpDto.setPhone(p.getC_telefon());

				aExpDto.setStressAdress(p.getC_strasse());

				if (p.getFlrlandplzort() != null) {
					aExpDto.setCity(p.getFlrlandplzort().getFlrort().getC_name());
					aExpDto.setCountry(p.getFlrlandplzort().getFlrland().getC_name());
					aExpDto.setPostalCode(p.getFlrlandplzort().getC_plz());
				}

				alExport.add(aExpDto);

				hmPartnerBereitsHinzugefuegt.add(p.getI_id());

			}

			// und deren Ansprechpartner

			if (syncall) {
				sQuery = "SELECT ansp, (SELECT padr.c_exchangeid FROM FLRAnsprechpartneradressbuch padr WHERE padr.c_emailadresse='"
						+ cEmailAdresse
						+ "' AND padr.ansprechpartner_i_id = ansp.i_id ) AS exchange_id FROM FLRAnsprechpartner ansp WHERE ansp.b_versteckt = 0 AND (ansp.c_email IS NOT NULL OR ansp.c_telefon IS NOT NULL OR ansp.c_handy IS NOT NULL ) AND ansp.i_id NOT IN (SELECT padr.ansprechpartner_i_id FROM FLRAnsprechpartneradressbuch padr WHERE padr.c_emailadresse='"
						+ cEmailAdresse
						+ "' AND padr.t_zuletzt_exportiert > ansp.t_aendern AND padr.t_zuletzt_exportiert > ansp.flrpartner.t_aendern  )";

			} else {

				sQuery = "SELECT ansp, (SELECT padr.c_exchangeid FROM FLRAnsprechpartneradressbuch padr WHERE padr.c_emailadresse='"
						+ cEmailAdresse
						+ "' AND padr.ansprechpartner_i_id = ansp.i_id ) AS exchange_id FROM FLRAnsprechpartner ansp WHERE ansp.b_versteckt = 0 AND ansp.i_id IN "
						+ inAnsprechpArtner
						+ " AND ansp.i_id NOT IN (SELECT padr.ansprechpartner_i_id FROM FLRAnsprechpartneradressbuch padr WHERE padr.c_emailadresse='"
						+ cEmailAdresse
						+ "' AND padr.t_zuletzt_exportiert > ansp.t_aendern AND padr.t_zuletzt_exportiert > ansp.flrpartner.t_aendern  )";
			}

			query = session.createQuery(sQuery);
			query.setMaxResults(iMaximaleAanzahl);
			resultList = query.list();
			resultListIterator = resultList.iterator();

			hmPartnerBereitsHinzugefuegt = new HashSet<Integer>();

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();

				FLRAnsprechpartner ansp = (FLRAnsprechpartner) o[0];

				String exchangeid = (String) o[1];

				AdressbuchExportDto aExpDtoAnsp = new AdressbuchExportDto();
				aExpDtoAnsp.setANSPRECHPARTNER_I_ID(ansp.getI_id());
				aExpDtoAnsp.setExportId(exchangeid);
				// Man ist eine Firma, wenn die Anrede Firma oder NULL ist
				if (ansp.getFlrpartner().getAnrede_c_nr() == null
						|| ansp.getFlrpartner().getAnrede_c_nr().equals(PartnerFac.PARTNER_ANREDE_FIRMA)) {
					aExpDtoAnsp.setCompany(HelperServer.formatNameAusFLRPartner(ansp.getFlrpartner()));
				}

				aExpDtoAnsp.setDisplayName(HelperServer.formatPersonAusFLRPartner(ansp.getFlrpartneransprechpartner()));

				aExpDtoAnsp.setEmail(ansp.getC_email());

				aExpDtoAnsp.setFirstName(ansp.getFlrpartneransprechpartner().getC_name2vornamefirmazeile2());
				aExpDtoAnsp.setLastName(ansp.getFlrpartneransprechpartner().getC_name1nachnamefirmazeile1());

				aExpDtoAnsp.setNotes(ansp.getFlrpartneransprechpartner().getX_bemerkung());
				aExpDtoAnsp.setPARTNER_I_ID(ansp.getFlrpartneransprechpartner().getI_id());

				if (Helper.short2boolean(ansp.getB_durchwahl()) && ansp.getC_telefon() != null) {
					String tel = enrichNumber(ansp.getFlrpartner().getI_id(), PartnerFac.KOMMUNIKATIONSART_TELEFON,
							theClientDto, ansp.getC_telefon(), false);
					aExpDtoAnsp.setPhone(tel);

				} else {
					aExpDtoAnsp.setPhone(ansp.getC_telefon());
				}

				aExpDtoAnsp.setStressAdress(ansp.getFlrpartner().getC_strasse());

				if (ansp.getFlrpartner().getFlrlandplzort() != null) {
					aExpDtoAnsp.setCity(ansp.getFlrpartner().getFlrlandplzort().getFlrort().getC_name());
					aExpDtoAnsp.setCountry(ansp.getFlrpartner().getFlrlandplzort().getFlrland().getC_name());
					aExpDtoAnsp.setPostalCode(ansp.getFlrpartner().getFlrlandplzort().getC_plz());
				}

				aExpDtoAnsp.setMobilePhone(ansp.getC_handy());
				alExport.add(aExpDtoAnsp);

			}
		}
		return alExport;

	}

	public void telefonnummerFuerTapiSynchronisieren(Integer partnerIId, Integer ansprechpartnerIId,
			TheClientDto theClientDto) {

		// Zuerst Partner OHNE Asprechpartner

		if (ansprechpartnerIId != null) {
			Query query = em.createQuery("DELETE Telefonnummer x WHERE  x.ansprechpartnerIId = " + ansprechpartnerIId);
			query.executeUpdate();
		} else {
			Query query = em.createQuery("DELETE Telefonnummer x WHERE x.partnerIId = " + partnerIId);
			query.executeUpdate();
		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN

		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

		if (ansprechpartnerIId == null) {
			if (partnerDto.getCTelefon() != null) {
				String tel = passeInlandsAuslandsVorwahlAn(partnerIId, theClientDto.getMandant(),
						partnerDto.getCTelefon(), true, theClientDto);
				tel = Helper.befreieNummerVonSonderzeichenInklisiveLeerzeichen(tel);
				Telefonnummer telpartner = new Telefonnummer(pkGen.getNextPrimaryKey(PKConst.PK_TELEFONNUMMER),
						partnerIId, null, tel, partnerDto.getBVersteckt());
				em.merge(telpartner);

			}

			// PJ21597
			if (partnerDto.getCHandy() != null) {

				String tel = passeInlandsAuslandsVorwahlAn(partnerIId, theClientDto.getMandant(),
						partnerDto.getCHandy(), true, theClientDto);

				tel = Helper.befreieNummerVonSonderzeichenInklisiveLeerzeichen(tel);
				Telefonnummer telpartner = new Telefonnummer(pkGen.getNextPrimaryKey(PKConst.PK_TELEFONNUMMER),
						partnerIId, null, tel, partnerDto.getBVersteckt());
				em.merge(telpartner);

			}

			// Wenn Personal -> Interne Nummer eintragen
			Query query = em.createQuery(
					"SELECT c FROM Personal c WHERE c.cTelefon IS NOT NULL AND c.partnerIId =" + partnerIId);
			Collection c = query.getResultList();
			Iterator it = c.iterator();
			while (it.hasNext()) {
				Personal p = (Personal) it.next();
				Telefonnummer telpartner = new Telefonnummer(pkGen.getNextPrimaryKey(PKConst.PK_TELEFONNUMMER),
						partnerIId, null, p.getCTelefon(), p.getBVersteckt());
				em.merge(telpartner);

			}

		}

		try {

			AnsprechpartnerDto[] anspDtos = getAnsprechpartnerFac().ansprechpartnerFindByPartnerIId(partnerIId,
					theClientDto);

			for (AnsprechpartnerDto anspDto : anspDtos) {
				if (ansprechpartnerIId != null && !anspDto.getIId().equals(ansprechpartnerIId)) {
					continue;
				}

				boolean bVersteckt = Helper.short2boolean(partnerDto.getBVersteckt());
				if (bVersteckt == false && Helper.short2boolean(anspDto.getBVersteckt())) {
					bVersteckt = true;
				}

				if (anspDto.getCHandy() != null) {

					String tel = passeInlandsAuslandsVorwahlAn(partnerIId, theClientDto.getMandant(),
							anspDto.getCHandy(), true, theClientDto);

					tel = Helper.befreieNummerVonSonderzeichenInklisiveLeerzeichen(tel);
					Telefonnummer telpartner = new Telefonnummer(pkGen.getNextPrimaryKey(PKConst.PK_TELEFONNUMMER),
							partnerIId, anspDto.getIId(), tel, Helper.boolean2Short(bVersteckt));
					em.merge(telpartner);

				}

				if (anspDto.getCTelefon() != null) {
					String tel = "";
					if (Helper.short2boolean(anspDto.getBDurchwahl())) {
						tel = enrichNumber(partnerIId, PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto,
								anspDto.getCTelefon(), false);
						tel = passeInlandsAuslandsVorwahlAn(partnerIId, theClientDto.getMandant(), tel, true,
								theClientDto);
					} else {

						tel = passeInlandsAuslandsVorwahlAn(partnerIId, theClientDto.getMandant(),
								anspDto.getCTelefon(), true, theClientDto);

					}

					tel = Helper.befreieNummerVonSonderzeichenInklisiveLeerzeichen(tel);
					Telefonnummer telpartner = new Telefonnummer(pkGen.getNextPrimaryKey(PKConst.PK_TELEFONNUMMER),
							partnerIId, anspDto.getIId(), tel, Helper.boolean2Short(bVersteckt));
					em.merge(telpartner);

				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void updateKontakt(KontaktDto kontaktDto) {
		if (kontaktDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kontaktDto == null"));
		}
		if (kontaktDto.getIId() == null || kontaktDto.getCTitel() == null || kontaktDto.getPartnerIId() == null
				|| kontaktDto.getKontaktartIId() == null || kontaktDto.getPersonalIIdZugewiesener() == null
				|| kontaktDto.getTKontakt() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"kontaktDto.getIId() == null || kontaktDto.getCTitel() == null || kontaktDto.getPartnerIId() == null || kontaktDto.getKontaktartIId() == null || kontaktDto.getPersonalIIdZugewiesener() == null || kontaktDto.getTKontakt() == null"));
		}

		Integer iId = kontaktDto.getIId();
		// try {
		Kontakt kontakt = em.find(Kontakt.class, iId);
		if (kontakt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		setKontaktFromKontaktDto(kontakt, kontaktDto);

	}

	public KontaktDto kontaktFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Kontakt kontakt = em.find(Kontakt.class, iId);
		if (kontakt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKontaktDto(kontakt);

	}

	private void setKontaktFromKontaktDto(Kontakt kontakt, KontaktDto kontaktDto) {
		kontakt.setAnsprechpartnerIId(kontaktDto.getAnsprechpartnerIId());
		kontakt.setCTitel(kontaktDto.getCTitel());
		kontakt.setKontaktartIId(kontaktDto.getKontaktartIId());
		kontakt.setPartnerIId(kontaktDto.getPartnerIId());
		kontakt.setPersonalIIdAnlegen(kontaktDto.getPersonalIIdAnlegen());
		kontakt.setPersonalIIdZugewiesener(kontaktDto.getPersonalIIdZugewiesener());
		kontakt.setTAnlegen(kontaktDto.getTAnlegen());
		kontakt.setTErledigt(kontaktDto.getTErledigt());
		kontakt.setTKontakt(kontaktDto.getTKontakt());
		kontakt.setTKontaktbis(kontaktDto.getTKontaktbis());
		kontakt.setTWiedervorlage(kontaktDto.getTWiedervorlage());
		kontakt.setXKommentar(kontaktDto.getXKommentar());
		em.merge(kontakt);
		em.flush();
	}

	public void empfaengerlisteAlsKontakteBeiPartnernEintragen(String titel, SerienbriefEmpfaengerDto[] empfaenger,
			Timestamp tKontakt, Timestamp tWiedervorlage, Integer kontaktartIId, Integer personalIIdZugewiesener,
			TheClientDto theClientDto) {

		for (int i = 0; i < empfaenger.length; i++) {
			KontaktDto kontaktDto = new KontaktDto();

			kontaktDto.setPartnerIId(empfaenger[i].getPartnerDto().getIId());
			if (empfaenger[i].getAnsprechpartnerDto() != null) {
				kontaktDto.setAnsprechpartnerIId(empfaenger[i].getAnsprechpartnerDto().getIId());
			}

			kontaktDto.setCTitel(titel);
			kontaktDto.setTKontakt(tKontakt);
			kontaktDto.setTWiedervorlage(tWiedervorlage);
			kontaktDto.setKontaktartIId(kontaktartIId);
			kontaktDto.setCTitel(titel);
			kontaktDto.setPersonalIIdZugewiesener(personalIIdZugewiesener);

			createKontakt(kontaktDto, theClientDto);

		}

	}

	private KontaktDto assembleKontaktDto(Kontakt kontakt) {
		return KontaktDtoAssembler.createDto(kontakt);
	}

	private KontaktDto[] assembleKontaktDtos(Collection<?> kontakts) {
		List<KontaktDto> list = new ArrayList<KontaktDto>();
		if (kontakts != null) {
			Iterator<?> iterator = kontakts.iterator();
			while (iterator.hasNext()) {
				Kontakt kotnakt = (Kontakt) iterator.next();
				list.add(assembleKontaktDto(kotnakt));
			}
		}
		KontaktDto[] returnArray = new KontaktDto[list.size()];
		return (KontaktDto[]) list.toArray(returnArray);
	}

	public String createPartnerart(PartnerartDto partnerartDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (partnerartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("partnerartDto == null"));
		}
		if (partnerartDtoI.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("partnerartDto.getCNr() == null"));
		}

		try {
			Query query = em.createNamedQuery("PartnerartfindByCNr");
			query.setParameter(1, partnerartDtoI.getCNr());
			Partnerart partnerart = (Partnerart) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_PARTNERART.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		Partnerart partnerart = null;

		try {
			partnerart = new Partnerart(partnerartDtoI.getCNr());
			em.persist(partnerart);
			em.flush();

			if (partnerartDtoI.getPartnerartsprDto() != null) {
				Partnerartspr partnerartspr = new Partnerartspr(partnerartDtoI.getCNr(),
						theClientDto.getLocMandantAsString());
				em.persist(partnerartspr);
				em.flush();
				setPartnerartsprFromPartnerartsprDto(partnerartspr, partnerartDtoI.getPartnerartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return partnerart.getCNr();
	}

	public void removePartnerart(String cNrI, TheClientDto theClientDto) throws EJBExceptionLP {

		Query query = em.createNamedQuery("PartnerartsprfindByPartnerartCNr");
		query.setParameter(1, cNrI);
		Collection<?> c = query.getResultList();

		if (cNrI.equals(PartnerFac.PARTNERART_ADRESSE) || cNrI.equals(PartnerFac.PARTNERART_ANSPRECHPARTNER)
				|| cNrI.equals(PartnerFac.PARTNERART_FIRMA) || cNrI.equals(PartnerFac.PARTNERART_PERSON)
				|| cNrI.equals(PartnerFac.PARTNERART_SONSTIGES) || cNrI.equals(PartnerFac.PARTNERART_VEREIN)) {
			ArrayList al = new ArrayList();
			al.add(cNrI);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNERART_DARF_NICHT_GELOESCHT_GEAENDERT_WERDEN, al,
					new Exception("FEHLER_PARTNERART_DARF_NICHT_GELOESCHT_GEAENDERT_WERDEN"));

		}

		try {
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Partnerartspr item = (Partnerartspr) iter.next();
				em.remove(item);
			}
			Partnerart toRemove = em.find(Partnerart.class, cNrI);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
	}

	public void removePartnerart(PartnerartDto partnerartDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (partnerartDtoI != null) {
			String cNr = partnerartDtoI.getCNr();
			removePartnerart(cNr, theClientDto);
		}
	}

	/**
	 * &Uuml;berpr&uuml;ft, ob Ziel- und Quellpartner vollst&auml;ndige Partner sind
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 */
	private void checkInputParamsZielQuellPartnerDtos(PartnerDto partnerZielDto, PartnerDto partnerQuellDto)
			throws EJBExceptionLP {

		if (partnerZielDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("partnerDto == null (Ziel)"));
		}

		if (partnerZielDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("partnerDto.getIId() == null (Ziel)"));
		}

		if (partnerZielDto.getPartnerartCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getPartnerartCNr() == null (Ziel)"));
		}

		if (partnerQuellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("partnerDto == null (Quell)"));
		}

		if (partnerQuellDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("partnerDto.getIId() == null (Quell)"));
		}

		if (partnerQuellDto.getPartnerartCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getPartnerartCNr() == null (Quell)"));
		}
	}

	/**
	 * Vor dem Zusammenf&uuml;ren von Quell- und Zielpartner werden die
	 * Kommunikationsdaten angepasst, da ansonsten der Quellpartner wegen
	 * eventueller Abh&auml;ngigkeiten nicht gel&ouml;scht werden kann.
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void reassignPartnerkommunikationBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		// Suchen aller Kommunikationen der Quelle und diese setzen auf das Ziel

		PartnerkommunikationDto[] partkommDto = getPartnerFac().partnerkommFindByPartnerIId(partnerQuellDto.getIId());

		PartnerkommunikationDto[] partkommZielDto = getPartnerFac()
				.partnerkommFindByPartnerIId(partnerZielDto.getIId());
		for (int i = 0; i < partkommDto.length; i++) {
			partkommDto[i].setPartnerIId(partnerZielDto.getIId());
			boolean bVorhanden = false;
			for (int y = 0; y < partkommZielDto.length; y++) {
				if (partkommDto[i].getCInhalt().equals(partkommZielDto[y].getCInhalt())) {
					bVorhanden = true;
				}
			}
			if (!bVorhanden) {
				updatePartnerkommunikation(partkommDto[i], theClientDto);
			} else {
				removePartnerkommunikation(partkommDto[i].getIId(), theClientDto);
			}
		}

		updatePartner(partnerQuellDto, theClientDto);
		updatePartner(partnerZielDto, theClientDto);

	}

	/**
	 * H&auml;ngt den Empf&auml;nger und den Sender (wenn vorhanden) vom
	 * Quellpartner auf den Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignVersandauftragBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// lp_versandauftrag - partner_i_id_empfaenger, partner_i_id_sender
		VersandauftragDto[] aVersandauftragDtos = null;
		aVersandauftragDtos = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			aVersandauftragDtos = getVersandFac()
					.versandauftragFindByEmpfaengerPartnerIIdOhneExc(partnerQuellDto.getIId());
			if (aVersandauftragDtos != null) {
				for (int j = 0; j < aVersandauftragDtos.length; j++) {
					aVersandauftragDtos[j].setPartnerIIdEmpfaenger(partnerZielDto.getIId());
					getVersandFac().updateVersandauftrag(aVersandauftragDtos[j], theClientDto);
				}
			}
			aVersandauftragDtos = null;
			aVersandauftragDtos = getVersandFac().versandauftragFindBySenderPartnerIIdOhneExc(partnerQuellDto.getIId());
			if (aVersandauftragDtos != null) {
				for (int j = 0; j < aVersandauftragDtos.length; j++) {
					aVersandauftragDtos[j].setPartnerIIdSender(partnerZielDto.getIId());
					getVersandFac().updateVersandauftrag(aVersandauftragDtos[j], theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt den Artikelhersteller-Partner vom Quellpartner auf den Zielpartner
	 * um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignHerstellerBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// ww_hersteller - artikel - grunddaten - hersteller
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		HerstellerDto[] aHerstellerDtos = null;
		try {
			aHerstellerDtos = getArtikelFac().herstellerFindByPartnerIId(partnerQuellDto.getIId(), theClientDto);
			if (aHerstellerDtos != null) {
				for (int j = 0; j < aHerstellerDtos.length; j++) {
					aHerstellerDtos[j].setPartnerDto(partnerZielDto);
					aHerstellerDtos[j].setPartnerIId(partnerZielDto.getIId());
					getArtikelFac().updateHersteller(aHerstellerDtos[j]);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	public void reassignKontakteBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Query query = em.createNamedQuery("KontaktfindByPartnerIId");
		query.setParameter(1, partnerQuellDto.getIId());
		Collection<Kontakt> cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Kontakt k = (Kontakt) it.next();

			k.setPartnerIId(partnerZielDto.getIId());
			k.setAnsprechpartnerIId(null);

			em.merge(k);
			em.flush();
		}

	}

	/**
	 * H&auml;ngt die Partnerbank (wenn vorhanden) vom Quellpartner auf den
	 * Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignPartnerbankBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// part_partnerbank - partner_i_id - pro Partner mehrere
		// Bankverbindungen
		PartnerbankDto[] partnerbankDtos = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			partnerbankDtos = getBankFac().partnerbankFindByPartnerIIdOhneExc(partnerQuellDto.getIId(), theClientDto);
			for (int j = 0; j < partnerbankDtos.length; j++) {
				partnerbankDtos[j].setPartnerIId(partnerZielDto.getIId());
				getBankFac().updatePartnerbank(partnerbankDtos[j]);
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt die Bank und die zugeh&ouml;rigen Partnerbankeintrag (wenn
	 * vorhanden) vom Quellpartner auf den Zielpartner um
	 * 
	 * @param partnerZielDto     PartnerDto
	 * @param partnerQuellDto    PartnerDto
	 * @param theClientDto       String
	 * @param bBankMitverdichten boolean
	 * @throws EJBExceptionLP
	 */
	public void reassignBankBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto, boolean bBankMitverdichten) throws EJBExceptionLP {
		// part_bank - partner_i_id

		try {
			BankDto bankQuellDto = getBankFac().bankFindByPrimaryKeyOhneExc(partnerQuellDto.getIId(), theClientDto);
			BankDto bankZielDto = partnerZielDto.getBankDto();
			if (bankZielDto != null) {

				// Gibts die Zielbank schon?
				BankDto bankZielDtoSchonVorhanden = getBankFac().bankFindByPrimaryKeyOhneExc(partnerZielDto.getIId(),
						theClientDto);

				getBankFac().updateBank(bankZielDto, theClientDto);

				PartnerbankDto[] partnerbankDtos = null;
				if (bankQuellDto != null && bankZielDtoSchonVorhanden == null) {

					Integer bankToRemove = null;
					// ZielBank auf Quellbank mit neuer ID setzen
					bankZielDto = bankQuellDto;
					bankZielDto.setPartnerDto(partnerZielDto);
					bankZielDto.setPartnerIIdNeuAus(partnerZielDto.getIId());
					bankToRemove = partnerQuellDto.getIId();
					Integer bankIId = getBankFac().createBank(bankZielDto, theClientDto);

					bankZielDto = getBankFac().bankFindByPrimaryKey(bankIId, theClientDto);
				}

				if (bankQuellDto != null) {

					// SP1382 Wenn in FIBU Bankverbindungen vorhanden sind, dann
					// ist ein zusammenfuehren nicht moeglich

					Query query = em.createNamedQuery("BankverbindungfindByBankIId");
					query.setParameter(1, bankQuellDto.getPartnerIId());
					Collection<?> bvs = query.getResultList();

					if (bvs.size() > 0) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_BANKVERBINDUNG_IN_FIBU_VORHANDEN,
								new Exception(
										"FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_BANKVERBINDUNG_IN_FIBU_VORHANDEN"));
					}

					// part_partnerbank - partnerbank_i_id - ist partnerid der
					// part_bank
					partnerbankDtos = null;
					partnerbankDtos = getBankFac().partnerbankFindByPartnerBankIIdOhneExc(partnerQuellDto.getIId(),
							theClientDto);
					for (int j = 0; j < partnerbankDtos.length; j++) {
						partnerbankDtos[j].setBankPartnerIId(partnerZielDto.getIId());
						getBankFac().updatePartnerbank(partnerbankDtos[j]);
					}

					if (bankQuellDto != null) {
						getBankFac().removeBank(bankQuellDto.getPartnerIId(), theClientDto);
					}
				}

			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt den Lieferanten (wenn vorhanden) vom Quellpartner auf den
	 * Zielpartner um
	 * 
	 * @param partnerZielDto          PartnerDto
	 * @param partnerQuellDto         PartnerDto
	 * @param mandantCNr              String
	 * @param bLieferantMitverdichten boolean
	 * @param theClientDto            String
	 * @throws EJBExceptionLP
	 */
	public void reassignLieferantBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, boolean bLieferantMitverdichten, TheClientDto theClientDto) throws EJBExceptionLP {
		// part_lieferant - pro Mandant darf ein Lieferant auf dem
		// mandantenunabhaengigen Partner haengen
		// partner_i_id & partner_i_id_rechnungsadresse
		/*
		 * abfangen, wenn bereits ein lieferant auf diesem mandanten auf dem zielpartner
		 * vorhanden aber quellpartner auch einen lieferanten auf dem mandanten hat -
		 * mitverdichten oder meldung
		 */
		LieferantDto lieferantQuellDto = null;
		LieferantDto lieferantZielDto = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);
		try {
			lieferantQuellDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(partnerQuellDto.getIId(),
					mandantCNr, theClientDto);
			if (lieferantQuellDto != null) {

				// SP4656
				lieferantQuellDto = getLieferantFac().lieferantFindByPrimaryKey(lieferantQuellDto.getIId(),
						theClientDto);

				lieferantZielDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(partnerZielDto.getIId(),
						mandantCNr, theClientDto);
				if (lieferantZielDto != null) {
					if (bLieferantMitverdichten) {
						// hier lieferanten automatisch zusammenfuehren -
						// ziellieferant bleibt, quelllieferant zu loeschen
						getLieferantFac().zusammenfuehrenLieferant(lieferantZielDto, lieferantQuellDto.getIId(), null,
								theClientDto);
					} else {
						ArrayList<Object> allInfoForTheClient = new ArrayList<Object>();
						allInfoForTheClient.add("lp.lieferant");
						allInfoForTheClient.add(lieferantQuellDto.getIId());
						allInfoForTheClient.add(lieferantZielDto.getIId());
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH,
								allInfoForTheClient, new Exception(
										"Lieferant bereits auf dem Zielmandanten vorhanden - vorher Lieferanten zusammenfuehren noetig"));
					}
				} else {
					lieferantQuellDto.setPartnerIId(partnerZielDto.getIId());
					lieferantQuellDto.setPartnerDto(partnerZielDto);
					getLieferantFac().updateLieferant(lieferantQuellDto, theClientDto);
				}
			}

			// part_lieferant - partner_i_id_rechnungsadresse
			LieferantDto[] aLieferantQuellDtos = null;
			aLieferantQuellDtos = getLieferantFac().lieferantFindByRechnungsadresseiIdPartnercNrMandantOhneExc(
					partnerQuellDto.getIId(), mandantCNr, theClientDto);
			if (aLieferantQuellDtos != null) {
				for (int j = 0; j < aLieferantQuellDtos.length; j++) {
					if (aLieferantQuellDtos[j] != null) {

						aLieferantQuellDtos[j].setPartnerDto(
								partnerFindByPrimaryKey(aLieferantQuellDtos[j].getPartnerIId(), theClientDto));

						aLieferantQuellDtos[j].setPartnerIIdRechnungsadresse(partnerZielDto.getIId());
						aLieferantQuellDtos[j].setPartnerRechnungsadresseDto(partnerZielDto);
						getLieferantFac().updateLieferant(aLieferantQuellDtos[j], theClientDto);
					}
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt die Rechnungsadresse vom Quellpartner auf den Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param mandantCNr      String
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignRechnungBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		// try {
		// // rech_rechnung - partner_i_id_rechnungsadresse
		// // RechnungDto[] aRechnungQuellDtos = null;
		// // aRechnungQuellDtos = getRechnungFac()
		// // .rechnungFindByPartnerIIdRechnungsadresseMandantCNrOhneExc(
		// // partnerQuellDto.getIId(), mandantCNr);
		// // if (aRechnungQuellDtos != null) {
		// // for (int j = 0; j < aRechnungQuellDtos.length; j++) {
		// // if (aRechnungQuellDtos[j] != null) {
		// // aRechnungQuellDtos[j]
		// // .setPartnerIIdRechnungsadresse(partnerZielDto
		// // .getIId());
		// // getRechnungFac()
		// // .updateRechnungPartnerIIdRechnungsadresse(
		// // aRechnungQuellDtos[j], theClientDto);
		// // }
		// // }
		// // }
		// } catch (RemoteException ex1) {
		// throwEJBExceptionLPRespectOld(ex1);
		// }
	}

	/**
	 * H&auml;ngt bei der St&uuml;ckliste die PartnerIId des Kunden vom Quellpartner
	 * auf den Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param mandantCNr      String
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignStuecklisteBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			// stk_stueckliste - partner_i_id
			StuecklisteDto[] aStuecklisteDto = null;
			aStuecklisteDto = getStuecklisteFac().stuecklisteFindByPartnerIIdMandantCNrOhneExc(partnerQuellDto.getIId(),
					mandantCNr, theClientDto);
			if (aStuecklisteDto != null) {
				for (int j = 0; j < aStuecklisteDto.length; j++) {
					if (aStuecklisteDto[j] != null) {
						Stueckliste zeile = em.find(Stueckliste.class, aStuecklisteDto[j].getIId());
						zeile.setPartnerIId(partnerZielDto.getIId());
						em.merge(zeile);
						em.flush();

					}
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt den Kunden (wenn vorhanden) vom Quellpartner auf den Zielpartner
	 * um
	 * 
	 * @param partnerZielDto      PartnerDto
	 * @param partnerQuellDto     PartnerDto
	 * @param mandantCNr          String
	 * @param bKundeMitverdichten boolean
	 * @param theClientDto        String
	 * @throws EJBExceptionLP
	 */
	public void reassignKundeBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, boolean bKundeMitverdichten, TheClientDto theClientDto) throws EJBExceptionLP {
		KundeDto kundeQuellDto = null;
		KundeDto kundeZielDto = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			// part_kunde - pro Mandant darf ein Kunde auf dem
			// mandantenunabhaengigen Partner haengen

			kundeQuellDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerQuellDto.getIId(), mandantCNr,
					theClientDto);
			if (kundeQuellDto != null) {

				kundeQuellDto = getKundeFac().kundeFindByPrimaryKey(kundeQuellDto.getIId(), theClientDto);

				kundeZielDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerZielDto.getIId(), mandantCNr,
						theClientDto);
				if (kundeZielDto != null) {
					if (bKundeMitverdichten) {
						// hier kunden automatisch zusammenfuehren - zielkunde
						// bleibt, quellkunde zu loeschen
						getKundeFac().zusammenfuehrenKunde(kundeZielDto, kundeQuellDto.getIId(), null, theClientDto);
					} else {
						ArrayList<Object> allInfoForTheClient = new ArrayList<Object>();
						allInfoForTheClient.add("lp.kunde");
						allInfoForTheClient.add(kundeQuellDto.getIId());
						allInfoForTheClient.add(kundeZielDto.getIId());
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH,
								allInfoForTheClient, new Exception(
										"Kunde bereits auf dem Zielmandanten vorhanden - vorher Kunden zusammenfuehren noetig"));
					}
				} else {
					kundeQuellDto.setPartnerIId(partnerZielDto.getIId());
					kundeQuellDto.setPartnerDto(partnerZielDto);
					getKundeFac().updateKunde(kundeQuellDto, theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt den Partner beim Finanzamt vom Quell- auf den Zielpartner um -
	 * aber nur Meldung an den Client
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param mandantCNr      String
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignFinanzamtBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		FinanzamtDto finanzamtQuellDto = null;
		FinanzamtDto finanzamtZielDto = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			// lp_finanzamt - pro Mandant darf ein Finanzamt auf dem
			// mandantenunabhaengigen Partner haengen
			/*
			 * ABER: wenn beim zusammenfuehren einer der beiden Partner ein Finanzamt ist,
			 * dann Meldung an den Client, dass dies nicht erlaubt ist
			 */

			finanzamtQuellDto = getFinanzFac().finanzamtFindByPartnerIIdMandantCNrOhneExc(partnerQuellDto.getIId(),
					mandantCNr, theClientDto);
			finanzamtZielDto = getFinanzFac().finanzamtFindByPartnerIIdMandantCNrOhneExc(partnerZielDto.getIId(),
					mandantCNr, theClientDto);
			if (finanzamtQuellDto != null || finanzamtZielDto != null) {
				if (finanzamtQuellDto != null && finanzamtZielDto != null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH,
							new Exception("Das Zusammenfuehren zweier Finanzaemter ist nicht moeglich!"));
				} else {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH,
							new Exception(
									"Das Zusammenfuehren eines Finanzamtes mit einem Partner ist nicht moeglich!"));
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt den Partner beim Mandanten vom Quellpartner auf den Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignMandantBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// lp_mandant - partner_i_id
		/*
		 * wenn man partner mit einem mandanten kombinieren moechte, gibt es eine
		 * meldung, dass es nicht moeglich ist
		 */
		MandantDto[] aMandantQuellDtos = null;
		MandantDto[] aMandantZielDtos = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			// lp_mandant
			/*
			 * ABER: wenn beim zusammenfuehren einer der beiden Partner ein Mandant ist,
			 * dann Meldung an den Client, dass dies nicht erlaubt ist
			 */

			aMandantQuellDtos = getMandantFac().mandantFindByPartnerIIdOhneExc(partnerQuellDto.getIId(), theClientDto);
			aMandantZielDtos = getMandantFac().mandantFindByPartnerIIdOhneExc(partnerZielDto.getIId(), theClientDto);
			if (aMandantQuellDtos.length > 0 || aMandantZielDtos.length > 0) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH,
						new Exception("Das Zusammenfuehren eines Mandanten mit einem Partner ist nicht gestattet!"));
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt den Auftragteilnehmer (wenn vorhanden) vom Quellpartner auf den
	 * Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignAuftragteilnehmerBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// auft_auftragteilnehmer - partner_i_id
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			AuftragteilnehmerDto[] aAuftragteilnehmerDtos = null;
			aAuftragteilnehmerDtos = getAuftragteilnehmerFac()
					.auftragteilnehmerFindByPartnerIIdAuftragteilnehmer(partnerQuellDto.getIId());
			for (int j = 0; j < aAuftragteilnehmerDtos.length; j++) {
				if (aAuftragteilnehmerDtos[j] != null) {
					aAuftragteilnehmerDtos[j].setPartnerIIdAuftragteilnehmer(partnerZielDto.getIId());
					getAuftragteilnehmerFac().updateAuftragteilnehmer(aAuftragteilnehmerDtos[j]);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt den Partner, den Sozialversicherer und die Partnerfirma (wenn
	 * vorhanden) vom Quellpartner auf den Zielpartner um
	 * 
	 * @param partnerZielDto         PartnerDto
	 * @param partnerQuellDto        PartnerDto
	 * @param mandantCNr             String
	 * @param bPersonalMitverdichten boolean
	 * @param theClientDto           String
	 * @throws EJBExceptionLP
	 */
	public void reassignPersonalBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, boolean bPersonalMitverdichten, TheClientDto theClientDto) throws EJBExceptionLP {
		PersonalDto personalQuellDto = null;
		PersonalDto personalZielDto = null;
		PersonalDto[] aPersonalDtos = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			// pers_personal - partner_i_id
			personalQuellDto = getPersonalFac().personalFindByPartnerIIdMandantCNrOhneExc(partnerQuellDto.getIId(),
					mandantCNr);
			if (personalQuellDto != null) {
				personalZielDto = getPersonalFac().personalFindByPartnerIIdMandantCNrOhneExc(partnerZielDto.getIId(),
						mandantCNr);
				if (personalZielDto != null) {
					if (bPersonalMitverdichten) {
						// ToDo: hier Personal automatisch zusammenfuehren
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH,
								new Exception("Personal automatisch zusammenfuehren ist noch nicht implementiert!"));
					} else {
						ArrayList<Object> allInfoForTheClient = new ArrayList<Object>();
						allInfoForTheClient.add("button.personal.tooltip");
						allInfoForTheClient.add(personalQuellDto.getIId());
						allInfoForTheClient.add(personalZielDto.getIId());
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH,
								allInfoForTheClient, new Exception(
										"Personal bereits auf dem Zielmandanten vorhanden - vorher Personal zusammenfuehren noetig"));
					}
				} else { // partnerDto und partnerIId von der quelle auf
					// zielpartner setzen
					personalQuellDto.setPartnerIId(partnerZielDto.getIId());
					personalQuellDto.setPartnerDto(partnerZielDto);
					getPersonalFac().updatePersonal(personalQuellDto, theClientDto);
				}
			}

			// pers_personal - partner_i_id_sozialversicherer
			aPersonalDtos = getPersonalFac()
					.personalFindBySozialversichererPartnerIIdMandantCNrOhneExc(partnerQuellDto.getIId(), mandantCNr);
			if (aPersonalDtos != null) {
				for (int j = 0; j < aPersonalDtos.length; j++) {
					if (aPersonalDtos[j].getPartnerDto() == null && aPersonalDtos[j].getPartnerIId() != null) {
						aPersonalDtos[j].setPartnerDto(
								this.partnerFindByPrimaryKey(aPersonalDtos[j].getPartnerIId(), theClientDto));
					}
					aPersonalDtos[j].setPartnerDto_Sozialversicherer(partnerZielDto);
					aPersonalDtos[j].setPartnerIIdSozialversicherer(partnerZielDto.getIId());
					getPersonalFac().updatePersonal(aPersonalDtos[j], theClientDto);
				}
			}

			// pers_personal - partner_i_id_firma
			aPersonalDtos = null;
			aPersonalDtos = getPersonalFac().personalFindByFirmaPartnerIIdMandantCNrOhneExc(partnerQuellDto.getIId(),
					mandantCNr);
			if (aPersonalDtos != null) {
				for (int j = 0; j < aPersonalDtos.length; j++) {
					if (aPersonalDtos[j].getPartnerDto() == null && aPersonalDtos[j].getPartnerIId() != null) {
						aPersonalDtos[j].setPartnerDto(
								this.partnerFindByPrimaryKey(aPersonalDtos[j].getPartnerIId(), theClientDto));
					}
					aPersonalDtos[j].setPartnerDto_Firma(partnerZielDto);
					aPersonalDtos[j].setPartnerIIdFirma(partnerZielDto.getIId());
					getPersonalFac().updatePersonal(aPersonalDtos[j], theClientDto);
				}
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt die Bestellung (falls vorhanden) vom Quellpartner auf den
	 * Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param mandantCNr      String
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignBestellungBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		BestellungDto[] aBestellungDtos = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			// bes_bestellung - partner_i_id_lieferadresse
			aBestellungDtos = getBestellungFac()
					.bestellungFindByLieferadressePartnerIIdMandantCNrOhneExc(partnerQuellDto.getIId(), mandantCNr);
			if (aBestellungDtos != null) {
				for (int j = 0; j < aBestellungDtos.length; j++) {
					Bestellung bestellung = em.find(Bestellung.class, aBestellungDtos[j].getIId());
					bestellung.setPartnerIIdLieferadresse(partnerZielDto.getIId());
					bestellung.setAnsprechpartnerIIdLieferadresse(null);
					em.merge(bestellung);
					em.flush();
				}
			}

			Query query = em.createNamedQuery("BestellungfindByAbholadressepartnerIIdMandantCNr");
			query.setParameter(1, partnerQuellDto.getIId());
			query.setParameter(2, mandantCNr);
			Collection cl = query.getResultList();

			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Bestellung b = (Bestellung) it.next();
				b.setPartnerIIdAbholadresse(partnerZielDto.getIId());
				b.setAnsprechpartnerIIdAbholadresse(null);
				em.merge(b);
				em.flush();
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt beim Los (falls vorhanden) den Quellpartner auf den Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param mandantCNr      String
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignLosBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LosDto[] aLosDtos = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			// fert_los - partner_i_id_fertigungsort
			aLosDtos = getFertigungFac().losFindByFertigungsortPartnerIIdMandantCNrOhneExc(partnerQuellDto.getIId(),
					mandantCNr);
			if (aLosDtos != null) {
				for (int j = 0; j < aLosDtos.length; j++) {

					Los los = em.find(Los.class, aLosDtos[j].getIId());
					los.setPartnerIIdFertigungsort(partnerZielDto.getIId());
					em.merge(los);
					em.flush();
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt beim wiederholenden Los (falls vorhanden) den Quellpartner auf den
	 * Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param mandantCNr      String
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignWiederholendesLosBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {

		WiederholendeloseDto[] aLosDtos = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			// fert_wiederholendelose - partner_i_id_fertigungsort -
			// normalerweise nur ein mandant, und mandZusammenfuehren darf man
			// nicht
			aLosDtos = getFertigungFac().wiederholendeloseFindByPartnerIIdMandantCNrOhneExc(partnerQuellDto.getIId(),
					mandantCNr);
			if (aLosDtos != null) {
				for (int j = 0; j < aLosDtos.length; j++) {

					Wiederholendelose los = em.find(Wiederholendelose.class, aLosDtos[j].getIId());
					los.setPartnerIIdFertigungsort(partnerZielDto.getIId());
					em.merge(los);
					em.flush();

				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt beim Projekt (falls vorhanden) den Quellpartner auf den
	 * Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param mandantCNr      String
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignProjektBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		ProjektDto[] aProjektDtos = null;
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		try {
			// proj_projekt - partner_i_id
			aProjektDtos = getProjektFac().projektFindByPartnerIIdMandantCNrOhneExc(partnerQuellDto.getIId(),
					mandantCNr);
			if (aProjektDtos != null) {
				for (int j = 0; j < aProjektDtos.length; j++) {

					Projekt zeile = em.find(Projekt.class, aProjektDtos[j].getIId());
					zeile.setPartnerIId(partnerZielDto.getIId());
					zeile.setAnsprechpartnerIId(null);
					em.merge(zeile);
					em.flush();

				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	public void reassignPartnerkommentarBeimZusammenfuehren(Integer partnerIId_Ziel, Integer partnerIId_Quelle,
			boolean bKunde, TheClientDto theClientDto) {
		ProjektDto[] aProjektDtos = null;

		Query query = em.createNamedQuery("PartnerkommentarfindByPartnerIIdBKunde");
		query.setParameter(1, partnerIId_Quelle);
		query.setParameter(2, Helper.boolean2Short(bKunde));

		Collection c = query.getResultList();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			Partnerkommentar partnerkommentarQuelle = (Partnerkommentar) it.next();

			// Wenn beim Ziel noch keine gleiche Partnerkommentarart gibt

			Query queryZiel = em.createNamedQuery("PartnerkommentarfindByPartnerIIdPartnerkommentarartIIdBKunde");
			queryZiel.setParameter(1, partnerIId_Ziel);
			queryZiel.setParameter(2, partnerkommentarQuelle.getPartnerkommentarartIId());
			queryZiel.setParameter(3, Helper.boolean2Short(bKunde));
			if (queryZiel.getResultList().size() == 0) {

				partnerkommentarQuelle.setPartnerIId(partnerIId_Ziel);
				em.merge(partnerkommentarQuelle);
				em.flush();

			} else {
				// FEHLER

				PartnerkommentarartDto pkDto = getPartnerServicesFac().partnerkommentarartFindByPrimaryKey(
						partnerkommentarQuelle.getPartnerkommentarartIId(), theClientDto);
				ArrayList al = new ArrayList();
				al.add(pkDto.getCBez());
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_PARTNERKOMMENTAR,
						al, new Exception(pkDto.getCBez()));

			}

		}

	}

	/**
	 * H&auml;ngt beim Ansprechpartner die PartnerIId und AnsprechpartnerIId vom
	 * Quellpartner auf den Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 */
	public void reassignAnsprechpartnerBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		// part_ansprechpartner - partner_i_id UND/ODER
		// partner_i_id_ansprechpartner
		try {
			AnsprechpartnerDto[] aAnsprechpartnerDtos = null;
			// pruefen, ob beide partner vielleicht den selben ansprechpartner
			// haben
			aAnsprechpartnerDtos = getAnsprechpartnerFac().ansprechpartnerFindByPartnerIId(partnerQuellDto.getIId(),
					theClientDto);
			AnsprechpartnerDto[] aAnsprechpartnerDtosVomZielpartner = getAnsprechpartnerFac()
					.ansprechpartnerFindByPartnerIIdOrPartnerIIdAnsprechpartnerOhneExc(partnerZielDto.getIId(),
							theClientDto);
			for (int j = 0; j < aAnsprechpartnerDtos.length; j++) {
				for (int k = 0; k < aAnsprechpartnerDtosVomZielpartner.length; k++) {
					if (aAnsprechpartnerDtos[j].getPartnerIIdAnsprechpartner()
							.intValue() == aAnsprechpartnerDtosVomZielpartner[k].getPartnerIIdAnsprechpartner()
									.intValue()
							&& aAnsprechpartnerDtos[j].getAnsprechpartnerfunktionIId()
									.intValue() == aAnsprechpartnerDtosVomZielpartner[k].getAnsprechpartnerfunktionIId()
											.intValue()) {
						// dh: der zielpartner haette dann den selben
						// ansprechpartner mit der selben
						// ansprechpartnerfunktion 2x
						// Zusammenfuehren
						getAnsprechpartnerFac().zusammenfuehrenAnsprechpartner(aAnsprechpartnerDtosVomZielpartner[k],
								aAnsprechpartnerDtos[j].getIId(), partnerQuellDto, theClientDto);
					}
				}
			}
			aAnsprechpartnerDtos = getAnsprechpartnerFac().ansprechpartnerFindByPartnerIId(partnerQuellDto.getIId(),
					theClientDto);
			for (int j = 0; j < aAnsprechpartnerDtos.length; j++) {
				// SK ist ja in beiden if das gleiche deshalb weg
				if (aAnsprechpartnerDtos[j].getPartnerIId().intValue() == partnerQuellDto.getIId().intValue()) {
					aAnsprechpartnerDtos[j].setPartnerIId(partnerZielDto.getIId());
					aAnsprechpartnerDtos[j].setPartnerDto(partnerZielDto);

				}
				if (aAnsprechpartnerDtos[j].getPartnerIIdAnsprechpartner().intValue() == partnerQuellDto.getIId()
						.intValue()) {
					aAnsprechpartnerDtos[j].setPartnerIIdAnsprechpartner(partnerZielDto.getIId());

				}
				getAnsprechpartnerFac().updateAnsprechpartner(aAnsprechpartnerDtos[j], theClientDto);
			}

			AnsprechpartnerDto[] dtos = getAnsprechpartnerFac()
					.ansprechpartnerFindByPartnerIIdAnsprechpartner(partnerQuellDto.getIId(), theClientDto);
			for (int j = 0; j < dtos.length; j++) {
				dtos[j].setPartnerIIdAnsprechpartner(partnerZielDto.getIId());

				try {
					Query query = em.createNamedQuery("AnsprechpartnerfindByPartnerFunktionGueltigAb");
					query.setParameter(1, dtos[j].getPartnerIId());
					query.setParameter(2, dtos[j].getPartnerIIdAnsprechpartner());
					query.setParameter(3, dtos[j].getAnsprechpartnerfunktionIId());
					query.setParameter(4, dtos[j].getDGueltigab());
					// @todo getSingleResult oder getResultList ?
					Ansprechpartner ansprechpartner = (Ansprechpartner) query.getSingleResult();
					if (ansprechpartner.getIId().intValue() != dtos[j].getIId().intValue()) {

						getAnsprechpartnerFac()
								.zusammenfuehrenAnsprechpartner(
										getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
												ansprechpartner.getIId(), theClientDto),
										dtos[j].getIId(), partnerQuellDto, theClientDto);

					} else {

						getAnsprechpartnerFac().updateAnsprechpartner(dtos[j], theClientDto);
					}

				} catch (NoResultException ex) {
					getAnsprechpartnerFac().updateAnsprechpartner(dtos[j], theClientDto);
				}

			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	/**
	 * H&auml;ngt beim Kurzbrief die PartnerIId vom Quellpartner auf den Zielpartner
	 * um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 */
	public void reassignPartnerkurzbriefBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		KurzbriefDto[] aKurzbriefDtos = null;
		try {
			// part_kurzbrief - partner_i_id
			aKurzbriefDtos = getPartnerFac().kurzbriefFindByPartnerIIdOhneExc(partnerQuellDto.getIId(), theClientDto);
			if (aKurzbriefDtos != null) {
				for (int j = 0; j < aKurzbriefDtos.length; j++) {
					aKurzbriefDtos[j].setPartnerIId(partnerZielDto.getIId());
					getPartnerFac().updateKurzbrief(aKurzbriefDtos[j], theClientDto);
				}
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt bei Reisen die PartnerIId vom Quellpartner auf den Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 */
	public void reassignReiseBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		ReiseDto[] aReiseDtos = null;
		ReiselogDto[] aReiselogDtos = null;
		try {
			// pers_reise
			aReiseDtos = getZeiterfassungFac().reiseFindByPartnerIIdOhneExc(partnerQuellDto.getIId(), theClientDto);
			if (aReiseDtos != null) {
				for (int j = 0; j < aReiseDtos.length; j++) {
					aReiseDtos[j].setPartnerIId(partnerZielDto.getIId());
					getZeiterfassungFac().updateReise(aReiseDtos[j], theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		try {
			// pers_reiselog - partner_i_id
			aReiselogDtos = getZeiterfassungFac().reiselogFindByPartnerIIdOhneExc(partnerQuellDto.getIId(),
					theClientDto);
			if (aReiselogDtos != null) {
				for (int j = 0; j < aReiselogDtos.length; j++) {
					aReiselogDtos[j].setPartnerIId(partnerZielDto.getIId());
					getZeiterfassungFac().updateReiselog(aReiselogDtos[j], theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt bei den Telefonzeiten die PartnerIId vom Quellpartner auf den
	 * Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignTelefonzeitBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		TelefonzeitenDto[] aTelefonzeitenDtos = null;
		try {
			// pers_telefonzeiten - partner_i_id
			aTelefonzeitenDtos = getZeiterfassungFac().telefonzeitenFindByPartnerIIdOhneExc(partnerQuellDto.getIId());
			if (aTelefonzeitenDtos != null) {
				for (int j = 0; j < aTelefonzeitenDtos.length; j++) {
					aTelefonzeitenDtos[j].setPartnerIId(partnerZielDto.getIId());
					aTelefonzeitenDtos[j].setAnsprechpartnerIId(null);
					getZeiterfassungFac().updateTelefonzeiten(aTelefonzeitenDtos[j], theClientDto);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * H&auml;ngt bei den Selektionen die PartnerIId vom Quellpartner auf den
	 * Zielpartner um
	 * 
	 * @param partnerZielDto  PartnerDto
	 * @param partnerQuellDto PartnerDto
	 * @param theClientDto    String
	 * @throws EJBExceptionLP
	 */
	public void reassignPaSelektionBeimZusammenfuehren(PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkInputParamsZielQuellPartnerDtos(partnerZielDto, partnerQuellDto);

		PASelektionDto[] aPaselektionDtos = null;
		try {
			// part_paselektion - partner_i_id
			/*
			 * beim partner darf eine bestimmte selektion nur einmal eingetragen sein, sonst
			 * fehler: class com.lp.client.frame.ExceptionLP
			 * javax.ejb.DuplicateKeyException: Entity with primary key
			 * com.lp.server.partner.service.PASelektionPK@69b3 already exists
			 */
			aPaselektionDtos = getPartnerFac().pASelektionFindByPartnerIIdOhneExc(partnerQuellDto.getIId());
			if (aPaselektionDtos != null) {
				for (int j = 0; j < aPaselektionDtos.length; j++) {

					try {
						Query query = em.createNamedQuery("PASelektionfindByPartnerIIdSelektionIId");
						query.setParameter(1, partnerZielDto.getIId());
						query.setParameter(2, aPaselektionDtos[j].getSelektionIId());

						Integer iIdVorhanden = ((Paselektion) query.getSingleResult()).getIId();
						// Wenn vorhanden, dann loeschen
						removePASelektion(aPaselektionDtos[j], theClientDto);

					} catch (NoResultException ex) {
						aPaselektionDtos[j].setPartnerIId(partnerZielDto.getIId());
						// Wenn nicht vorhanden, dann neu eintragen
						getPartnerFac().updatePASelektion(aPaselektionDtos[j], theClientDto);

					}

				}
			}
			aPaselektionDtos = getPartnerFac().pASelektionFindByPartnerIIdOhneExc(partnerQuellDto.getIId());
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	/**
	 * Fuehrt zwei Partner zusammen. Schreibt den Partner partnerZielDto mit evtl.
	 * neuen Daten in die Datenbank zur&uuml;ck, setzt die Partnerverbindungen von
	 * dem Partner mit der IId von partnerQuellDtoIId auf den Zielpartner um und
	 * l&ouml;scht zum Schluss den Partner, der zur partnerQuellDtoIId geh&ouml;rt,
	 * aus der Datenbank
	 * 
	 * @param partnerZielDto         PartnerDto
	 * @param partnerQuellDtoIid     int
	 * @param kundeMitverdichten     boolean - verkn&uuml;pfte Kunden werden
	 *                               automatisch zusammengef&uuml;hrt
	 * @param lieferantMitverdichten boolean - verkn&uuml;pfte Lieferanten werden
	 *                               automatisch zusammengef&uuml;hrt
	 * @param bankMitverdichten      boolean - verkn&uuml;pfte Banken werden
	 *                               automatisch zusammengef&uuml;hrt
	 * @param theClientDto           String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void zusammenfuehrenPartner(PartnerDto partnerZielDto, int partnerQuellDtoIid, boolean kundeMitverdichten,
			boolean lieferantMitverdichten, boolean bankMitverdichten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		PartnerDto partnerQuellDto = null;
		/* diese Werte setzen, sobald sie vom Client zurueckgegeben werden */
		boolean bKundeMitverdichten = kundeMitverdichten;
		boolean bLieferantMitverdichten = lieferantMitverdichten;
		boolean bPersonalMitverdichten = false;
		boolean bBankMitverdichten = bankMitverdichten;

		if (partnerZielDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("partnerDto == null (Ziel oder Quell)"));
		}

		if (partnerZielDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("partnerZielDto.getIId() == null (Ziel)"));
		}

		if (partnerZielDto.getPartnerartCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getPartnerartCNr() == null (Ziel oder Quell)"));
		}

		if (partnerZielDto.getLocaleCNrKommunikation() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto.getLocaleCNrKommunikation() == null"));
		}

		myLogger.info("Zielpartner: " + partnerZielDto.toString());

		Partner partnerQuelle = em.find(Partner.class, partnerQuellDtoIid);

		Partner partnerZiel = em.find(Partner.class, partnerZielDto.getIId());

		// ABW_USTLAND muss gleich sein
		String abwQuelle = "";
		String abwZiel = "";
		if (partnerQuelle.getLandIIdAbweichendesustland() != null) {
			LandDto landDto = getSystemFac().landFindByPrimaryKey(partnerQuelle.getLandIIdAbweichendesustland());
			abwQuelle = landDto.getCLkz();
		}
		if (partnerZiel.getLandIIdAbweichendesustland() != null) {
			LandDto landDto = getSystemFac().landFindByPrimaryKey(partnerZiel.getLandIIdAbweichendesustland());
			abwZiel = landDto.getCLkz();
		}

		if (!abwQuelle.equals(abwZiel)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_ABW_UST,
					new Exception("FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_ABW_UST"));
		}

		// UID muss gleich sein
		String uidQuelle = "";
		String uidZiel = "";
		if (partnerQuelle.getCUid() != null) {
			uidQuelle = partnerQuelle.getCUid();
		}
		if (partnerZiel.getCUid() != null) {
			uidZiel = partnerZiel.getCUid();
		}

		if (!uidQuelle.equals(uidZiel) && !uidQuelle.equals("")) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_UID,
					new Exception("FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_UID"));
		}

		// LKZ muss gleich sein
		String lkzQuelle = "";
		String lkzZiel = "";
		if (partnerQuelle.getLandplzortIId() != null) {
			LandplzortDto plzort = getSystemFac().landplzortFindByPrimaryKey(partnerQuelle.getLandplzortIId());
			lkzQuelle = plzort.getLandDto().getCLkz();
		}
		if (partnerZiel.getLandplzortIId() != null) {
			LandplzortDto plzort = getSystemFac().landplzortFindByPrimaryKey(partnerZiel.getLandplzortIId());
			lkzZiel = plzort.getLandDto().getCLkz();
		}

		if (!lkzQuelle.equals(lkzZiel)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_LKZ,
					new Exception("FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_LKZ"));
		}

		partnerQuellDto = partnerFindByPrimaryKey(partnerQuellDtoIid, theClientDto);

		// Partnerkommunikation umhaengen, wenn noetig
		if (partnerQuellDto != null) {
			myLogger.info("Quellpartner: " + partnerQuellDto.toString());

			// neue Ziel-Dto-Daten in die DB zurueckschreiben
			updatePartner(partnerZielDto, theClientDto);

			// alle zugehoerigen Quellpartner-Anhaengsel auf den Zielpartner
			// umhaengen
			MandantDto[] aMandantDtos = null;

			try {
				// mandantenunabhaengig
				getDokumenteFac().vertauscheBelegartIdBeiBelegartdokumenten(LocaleFac.BELEGART_PARTNER,
						partnerQuellDto.getIId(), partnerZielDto.getIId(), theClientDto);
				getJCRDocFac().fuehreDokumenteZusammen(partnerZielDto, partnerQuellDto);
				reassignVersandauftragBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);
				reassignPartnerbankBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);
				reassignBankBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto, bBankMitverdichten);
				reassignAuftragteilnehmerBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);

				reassignPartnerkurzbriefBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);
				reassignReiseBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);
				reassignTelefonzeitBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);
				reassignPaSelektionBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);
				reassignHerstellerBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);
				reassignMandantBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);
				reassignKontakteBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);

				// PJ 14649 Rechnungsadresse im Kunden ersetzen
				Session session = FLRSessionFactory.getFactory().openSession();
				String queryString = "FROM FLRKunde AS kunde WHERE kunde.flrpartnerRechnungsAdresse="
						+ partnerQuellDto.getIId();
				org.hibernate.Query query = session.createQuery(queryString);
				List<?> resultList = query.list();
				Iterator<?> resultListIterator = resultList.iterator();
				while (resultListIterator.hasNext()) {
					FLRKunde o = (FLRKunde) resultListIterator.next();

					Kunde kunde = em.find(Kunde.class, o.getI_id());
					kunde.setPartnerIIdRechnungsadresse(partnerZielDto.getIId());
					em.merge(kunde);
					em.flush();
				}

				// mandantenabhaengig
				aMandantDtos = getMandantFac().mandantFindAll(theClientDto);

				int i = 0;
				while (i < aMandantDtos.length) {
					reassignKundeBeimZusammenfuehren(partnerZielDto, partnerQuellDto, aMandantDtos[i].getCNr(),
							bKundeMitverdichten, theClientDto);
					reassignLieferantBeimZusammenfuehren(partnerZielDto, partnerQuellDto, aMandantDtos[i].getCNr(),
							bLieferantMitverdichten, theClientDto);
					reassignPersonalBeimZusammenfuehren(partnerZielDto, partnerQuellDto, aMandantDtos[i].getCNr(),
							bPersonalMitverdichten, theClientDto);

					reassignBestellungBeimZusammenfuehren(partnerZielDto, partnerQuellDto, aMandantDtos[i].getCNr(),
							theClientDto);
					reassignLosBeimZusammenfuehren(partnerZielDto, partnerQuellDto, aMandantDtos[i].getCNr(),
							theClientDto);
					reassignWiederholendesLosBeimZusammenfuehren(partnerZielDto, partnerQuellDto,
							aMandantDtos[i].getCNr(), theClientDto);
					reassignProjektBeimZusammenfuehren(partnerZielDto, partnerQuellDto, aMandantDtos[i].getCNr(),
							theClientDto);
					reassignRechnungBeimZusammenfuehren(partnerZielDto, partnerQuellDto, aMandantDtos[i].getCNr(),
							theClientDto);
					reassignStuecklisteBeimZusammenfuehren(partnerZielDto, partnerQuellDto, aMandantDtos[i].getCNr(),
							theClientDto);
					reassignFinanzamtBeimZusammenfuehren(partnerZielDto, partnerQuellDto, aMandantDtos[i].getCNr(),
							theClientDto);

					i++;
				}

				reassignAnsprechpartnerBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);
				reassignPartnerkommunikationBeimZusammenfuehren(partnerZielDto, partnerQuellDto, theClientDto);

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			if (partnerQuellDto != null) {
				removePartner(partnerQuellDto, theClientDto);
			}

		}
	}

	public void updatePartnerart(PartnerartDto partnerartDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		String cNr = partnerartDtoI.getCNr();
		Partnerart partnerart = em.find(Partnerart.class, cNr);
		if (partnerart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("PartnerartfindByCNr");
			query.setParameter(1, cNr);
			String cNrVorhanden = ((Partnerart) query.getSingleResult()).getCNr();
			if (partnerart.getCNr().equals(cNrVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_PARTNERART.C_NR"));
			}
		} catch (NoResultException ex) {

		}

		if (partnerartDtoI.getPartnerartsprDto() != null) {
			// -- upd oder create

			try {
				Partnerartspr partnerartspr = em.find(Partnerartspr.class,
						new PartnerartsprPK(theClientDto.getLocUiAsString(), partnerartDtoI.getCNr()));

				if (partnerartspr == null) {
					partnerartspr = new Partnerartspr(partnerartDtoI.getCNr(), theClientDto.getLocUiAsString());
				}

				partnerartspr.setCBez(partnerartDtoI.getPartnerartsprDto().getCBez());
				em.persist(partnerartspr);
				em.flush();
			} catch (NoResultException e) {
				partnerartDtoI.getPartnerartsprDto().setPartnerartCNr(partnerartDtoI.getCNr());
				partnerartDtoI.getPartnerartsprDto().setLocaleCNr(theClientDto.getLocUiAsString());
				createPartnerartspr(partnerartDtoI.getPartnerartsprDto(), theClientDto);
			}

		}

	}

	/** @todo jmsqueue: x serverseitig on demand PJ 4488 */
	// private void jmstest()
	// throws Exception {
	//
	// String destinationName = "/queue/LPQueue";
	// InitialContext ic = null;
	// ConnectionFactory cf = null;
	// Connection connection = null;
	// try {
	// ic = new InitialContext();
	// cf = (ConnectionFactory) ic.lookup("/ConnectionFactory");
	// Queue queue = (Queue) ic.lookup(destinationName);
	// myLogger.logData("Queue " + destinationName + " exists");
	//
	// connection = cf.createConnection();
	// Session session = connection.createSession(false,
	// Session.AUTO_ACKNOWLEDGE);
	// MessageProducer sender = session.createProducer(queue);
	//
	// Queue temporaryQueue = session.createTemporaryQueue();
	// MessageConsumer consumer = session.createConsumer(temporaryQueue);
	//
	// TextMessage message = session.createTextMessage("Hello!");
	// message.setJMSReplyTo(temporaryQueue);
	//
	// sender.send(message);
	// myLogger.logData("The message was successfully sent to the " +
	// queue.getQueueName() +
	// " queue");
	//
	// connection.start();
	//
	// message = (TextMessage) consumer.receive(5000);
	// if (message == null) {
	// myLogger.logData("Have not received any reply. The example failed!");
	// throw new Exception("Have not received any reply. The example failed!");
	// }
	// myLogger.logData("Received message: " + message.getText());
	// myLogger.logData(connection.getMetaData());
	// }
	// finally {
	// if (ic != null) {
	// try {
	// ic.close();
	// }
	// catch (Exception e) {
	// throw e;
	// }
	// }
	// //ALWAYS close your connection in a finally block to avoid leaks
	// //Closing connection also takes care of closing its related objects e.g.
	// sessions
	// jmscloseConnection(connection);
	// }
	// }
	//
	//
	// private void jmscloseConnection(Connection con)
	// throws JMSException {
	//
	// try {
	// con.close();
	// }
	// catch (JMSException jmse) {
	// myLogger.logData("Could not close connection " + con + " exception was "
	// + jmse);
	// throw jmse;
	// }
	// }
	/**
	 * 
	 * @param cNrI         String
	 * @param theClientDto String
	 * @return PartnerartDto
	 * @throws EJBExceptionLP
	 */
	public PartnerartDto partnerartFindByPrimaryKey(String cNrI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("cNrI == null"));
		}

		PartnerartDto partnerartDto = null;
		// try {
		Partnerart partnerart = em.find(Partnerart.class, cNrI);
		if (partnerart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		partnerartDto = assemblePartnerartDto(partnerart);

		try {
			Partnerartspr partnerartspr = em.find(Partnerartspr.class,
					new PartnerartsprPK(theClientDto.getLocUiAsString(), partnerartDto.getCNr()));
			partnerartDto.setPartnerartsprDto(assemblePartnerartsprDto(partnerartspr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return partnerartDto;
	}

	public PartnerartDto[] partnerartFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("PartnerartfindAll");
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		return assemblePartnerartDtos(cl);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }
	}

	public PartnerartsprPK createPartnerartspr(PartnerartsprDto partnerartsprDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Partnerartspr partnerartspr = null;

		try {
			partnerartspr = new Partnerartspr(partnerartsprDtoI.getPartnerartCNr(), partnerartsprDtoI.getLocaleCNr());
			em.persist(partnerartspr);
			em.flush();
			setPartnerartsprFromPartnerartsprDto(partnerartspr, partnerartsprDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return new PartnerartsprPK(partnerartspr.getPk().getPartnerartCNr(), partnerartspr.getPk().getLocaleCNr());
	}

	public void removePartnerartspr(String partnerartCNrI, String cNrLocaleI) throws EJBExceptionLP {

		PartnerartsprPK partnerartsprPK = new PartnerartsprPK();
		partnerartsprPK.setPartnerartCNr(partnerartCNrI);
		partnerartsprPK.setLocaleCNr(cNrLocaleI);

		Partnerartspr toRemove = em.find(Partnerartspr.class, partnerartsprPK);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removePartnerartspr(PartnerartsprDto partnerartsprDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (partnerartsprDtoI != null) {
			String partnerartCNr = partnerartsprDtoI.getPartnerartCNr();
			String spracheCNr = partnerartsprDtoI.getLocaleCNr();
			removePartnerartspr(partnerartCNr, spracheCNr);
		}
	}

	public void updatePartnerartspr(PartnerartsprDto partnerartsprDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (partnerartsprDtoI != null) {
			PartnerartsprPK partnerartsprPK = new PartnerartsprPK();
			partnerartsprPK.setPartnerartCNr(partnerartsprDtoI.getPartnerartCNr());
			partnerartsprPK.setLocaleCNr(partnerartsprDtoI.getLocaleCNr());
			// try {
			Partnerartspr partnerartspr = em.find(Partnerartspr.class, partnerartsprPK);
			if (partnerartspr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPartnerartsprFromPartnerartsprDto(partnerartspr, partnerartsprDtoI);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public PartnerartsprDto partnerartsprFindByPrimaryKey(String partnerartCNr, String cNrLocaleI)
			throws EJBExceptionLP {

		// try {
		PartnerartsprPK partnerartsprPK = new PartnerartsprPK();
		partnerartsprPK.setPartnerartCNr(partnerartCNr);
		partnerartsprPK.setLocaleCNr(cNrLocaleI);
		Partnerartspr partnerartspr = em.find(Partnerartspr.class, partnerartsprPK);
		if (partnerartspr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePartnerartsprDto(partnerartspr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public PartnerDto[] partnerFindByName1(String sName1I, TheClientDto theClientDto) {
		Collection<?> c = null;
		// try {
		Query query = em.createNamedQuery("PartnerfindByCName1");
		query.setParameter(1, sName1I);
		c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return assemblePartnerDtos(c);
	}

	public PartnerDtoSmall partnerFindByPrimaryKeySmall(Integer iIdPartnerI, TheClientDto theClientDto) {

		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_IN, new Exception("iIdPartnerI == null"));
		}

		PartnerDtoSmall partnerDtoSmall = partnerFindByPrimaryKeySmallOhneExc(iIdPartnerI);
		if (partnerDtoSmall == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return partnerDtoSmall;
	}

	public PartnerDtoSmall partnerFindByPrimaryKeySmallOhneExc(Integer iIdPartnerI) {

		Partner partner = em.find(Partner.class, iIdPartnerI);
		if (partner == null) {
			return null;
		}
		return assemblePartnerDtoSmall(partner);
	}

	public String formatBriefAnrede(PartnerDto partnerDto, Locale loI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (partnerDto == null || loI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("partnerDto == null || loI==null"));
		}

		StringBuffer buff = new StringBuffer();

		// Anrede aus lp.text ohne Herr Frau
		String sAnredeCNr = partnerDto.getAnredeCNr();
		String sParameter;
		if (sAnredeCNr == null || sAnredeCNr.equals("") || sAnredeCNr.equals(PartnerFac.PARTNER_ANREDE_FAMILIE)) {
			sParameter = "lp.anrede.null";
		} else {
			sParameter = "lp.anrede." + sAnredeCNr.toLowerCase().trim();
		}
		buff.append(getTextRespectUISpr(sParameter, theClientDto.getMandant(), loI));
		buff.append(" ");
		buff.append(formatFixAnredeTitelName2Name1(partnerDto, loI, theClientDto));
		buff.append(",");

		return buff.toString();
	}

	public Integer createPASelektion(PASelektionDto pASelektionDto, TheClientDto theClientDto) throws EJBExceptionLP {

		Integer iId = null;
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		iId = pkGen.getNextPrimaryKey(PKConst.PK_PASELEKTION);
		pASelektionDto.setIId(iId);

		try {
			Query query = em.createNamedQuery("PASelektionfindByPartnerIIdSelektionIId");
			query.setParameter(1, pASelektionDto.getPartnerIId());
			query.setParameter(2, pASelektionDto.getSelektionIId());
			Paselektion paselektion = (Paselektion) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_PASELEKTION.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		Paselektion pASelektion = null;
		try {
			pASelektion = new Paselektion(pASelektionDto.getIId(), pASelektionDto.getPartnerIId(),
					pASelektionDto.getSelektionIId());
			em.persist(pASelektion);
			em.flush();
			setPASelektionFromPASelektionDto(pASelektion, pASelektionDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return pASelektionDto.getIId();
	}

	public void removePASelektion(PASelektionDto pASelektionDto, TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			Paselektion pASelektion = em.find(Paselektion.class, pASelektionDto.getIId());
			if (pASelektion == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(pASelektion);
			em.flush();
		} catch (EJBExceptionLP ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updatePASelektion(PASelektionDto pASelektionDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (pASelektionDto != null) {

			Integer iId = pASelektionDto.getIId();
			try {
				Query query = em.createNamedQuery("PASelektionfindByPartnerIIdSelektionIId");
				query.setParameter(1, pASelektionDto.getPartnerIId());
				query.setParameter(2, pASelektionDto.getSelektionIId());

				Integer iIdVorhanden = ((Paselektion) query.getSingleResult()).getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PART_PASELEKTION.UK"));
				}
			} catch (NoResultException ex) {
				//
			}

			Paselektion pASelektion = null;
			pASelektion = em.find(Paselektion.class, pASelektionDto.getIId());
			if (pASelektion == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			setPASelektionFromPASelektionDto(pASelektion, pASelektionDto);
		}
	}

	public PASelektionDto pASelektionFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Paselektion paselektion = em.find(Paselektion.class, iId);
			if (paselektion == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assemblePASelektionDto(paselektion);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}

	public PASelektionDto[] pASelektionFindByPartnerIId(Integer partnerIId) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("PASelektionfindByPartnerIId");
			query.setParameter(1, partnerIId);
			Collection<?> cl = query.getResultList();
			// if(cl.isEmpty()){
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
			// }
			return assemblePASelektionDtos(cl);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}

	public PASelektionDto[] pASelektionFindByPartnerIIdOhneExc(Integer partnerIId) throws EJBExceptionLP {
		Query query = em.createNamedQuery("PASelektionfindByPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		return assemblePASelektionDtos(cl);
	}

	private void setPASelektionFromPASelektionDto(Paselektion paselektion, PASelektionDto pASelektionDto) {
		paselektion.setCBemerkung(pASelektionDto.getCBemerkung());
		paselektion.setPartnerIId(pASelektionDto.getPartnerIId());
		paselektion.setSelektionIId(pASelektionDto.getSelektionIId());
		em.merge(paselektion);
		em.flush();
	}

	private PASelektionDto assemblePASelektionDto(Paselektion pASelektion) {
		return PASelektionDtoAssembler.createDto(pASelektion);
	}

	private PASelektionDto[] assemblePASelektionDtos(Collection<?> paselektions) {
		List<PASelektionDto> list = new ArrayList<PASelektionDto>();
		if (paselektions != null) {
			Iterator<?> iterator = paselektions.iterator();
			while (iterator.hasNext()) {
				Paselektion paselektion = (Paselektion) iterator.next();
				list.add(assemblePASelektionDto(paselektion));
			}
		}
		PASelektionDto[] returnArray = new PASelektionDto[list.size()];
		return (PASelektionDto[]) list.toArray(returnArray);
	}

	public Integer createKurzbrief(KurzbriefDto kurzbriefDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (kurzbriefDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kurzbriefDtoI == null"));
		}

		// PK fuer Kurzbrief generieren.
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer iIdKurzbriefNew = pkGen.getNextPrimaryKey(PKConst.PK_PARTNER);
		kurzbriefDtoI.setIId(iIdKurzbriefNew);

		Kurzbrief kurzbrief = null;
		try {
			// Wer legt an setzen.
			kurzbriefDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			kurzbriefDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			Timestamp d = new Timestamp(System.currentTimeMillis());
			kurzbriefDtoI.setTAnlegen(d);
			if (kurzbriefDtoI.getBHtml() == null) {
				kurzbriefDtoI.setBHtml(Helper.boolean2Short(false));
			}

			kurzbrief = new Kurzbrief(kurzbriefDtoI.getIId(), kurzbriefDtoI.getPartnerIId(),
					kurzbriefDtoI.getAnsprechpartnerIId(), kurzbriefDtoI.getPersonalIIdAnlegen(),
					kurzbriefDtoI.getPersonalIIdAendern(), kurzbriefDtoI.getBelegartCNr(), kurzbriefDtoI.getBHtml(),
					kurzbriefDtoI.getMandantCNr());
			em.persist(kurzbrief);
			em.flush();
			setKurzbriefFromKurzbriefDto(kurzbrief, kurzbriefDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return kurzbrief.getIId();
	}

	public void removeKurzbrief(Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdI"));
		}

		try {
			Kurzbrief toRemove = em.find(Kurzbrief.class, iIdI);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
		} catch (EJBExceptionLP ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateKurzbrief(KurzbriefDto kurzbriefDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (kurzbriefDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kurzbriefDtoI == null"));
		}

		Integer iId = kurzbriefDtoI.getIId();
		try {
			Kurzbrief kurzbrief = em.find(Kurzbrief.class, iId);
			setKurzbriefFromKurzbriefDto(kurzbrief, kurzbriefDtoI);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}

	public KurzbriefDto kurzbriefFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdI == null"));
		}

		try {
			Kurzbrief kurzbrief = em.find(Kurzbrief.class, iIdI);
			if (kurzbrief == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleKurzbriefDto(kurzbrief);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}

	public KurzbriefDto kurzbriefFindByPrimaryKeyOhneExc(Integer iIdI, TheClientDto theClientDto) {

		// precondition
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdI == null"));
		}

		try {
			Kurzbrief kurzbrief = em.find(Kurzbrief.class, iIdI);
			if (kurzbrief == null) {
				return null;
			}
			return assembleKurzbriefDto(kurzbrief);
		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * Findet alle Kurzbriefe eines Partners
	 * 
	 * @param iPartnerIdI  Integer
	 * @param theClientDto String
	 * @return KurzbriefDto[]
	 * @throws EJBExceptionLP
	 */
	public KurzbriefDto[] kurzbriefFindByPartnerIId(Integer iPartnerIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// precondition
		if (iPartnerIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iPartnerIdI == null"));
		}

		Query query = em.createNamedQuery("KurzbrieffindByPartnerIId");
		query.setParameter(1, iPartnerIdI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleKurzbriefDtos(cl);

	}

	/**
	 * Findet alle Kurzbriefe eines Partners
	 * 
	 * @param iPartnerIdI  Integer
	 * @param theClientDto String
	 * @return KurzbriefDto[]
	 * @throws EJBExceptionLP
	 */
	public KurzbriefDto[] kurzbriefFindByPartnerIIdOhneExc(Integer iPartnerIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// precondition
		if (iPartnerIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iPartnerIdI == null"));
		}

		Query query = em.createNamedQuery("KurzbrieffindByPartnerIId");
		query.setParameter(1, iPartnerIdI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		return assembleKurzbriefDtos(cl);

	}

	public KurzbriefDto[] kurzbriefFindByAnsprechpartnerIId(Integer iAnsprechartnerIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// precondition
		if (iAnsprechartnerIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iAnsprechartnerIdI == null"));
		}

		Query query = em.createNamedQuery("KurzbrieffindByAnsprechpartnerIId");
		query.setParameter(1, iAnsprechartnerIdI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleKurzbriefDtos(cl);

	}

	public KurzbriefDto[] kurzbriefFindByAnsprechpartnerIIdOhneExc(Integer iAnsprechartnerIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// precondition
		if (iAnsprechartnerIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iAnsprechartnerIdI == null"));
		}

		Query query = em.createNamedQuery("KurzbrieffindByAnsprechpartnerIId");
		query.setParameter(1, iAnsprechartnerIdI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		return assembleKurzbriefDtos(cl);

	}

	private void setKurzbriefFromKurzbriefDto(Kurzbrief kurzbrief, KurzbriefDto kurzbriefDto) {
		kurzbrief.setPartnerIId(kurzbriefDto.getPartnerIId());
		kurzbrief.setXText(kurzbriefDto.getXText());
		kurzbrief.setCBetreff(kurzbriefDto.getCBetreff());
		kurzbrief.setBelegartCNr(kurzbriefDto.getBelegartCNr());
		kurzbrief.setAnsprechpartnerIId(kurzbriefDto.getAnsprechpartnerIId());
		kurzbrief.setTAendern(kurzbriefDto.getTAendern());
		kurzbrief.setPersonalIIdAendern(kurzbriefDto.getPersonalIIdAendern());
		kurzbrief.setBHtml(kurzbriefDto.getBHtml());
		kurzbrief.setMandantCNr(kurzbriefDto.getMandantCNr());
		em.merge(kurzbrief);
		em.flush();
	}

	private KurzbriefDto assembleKurzbriefDto(Kurzbrief kurzbrief) {
		return KurzbriefDtoAssembler.createDto(kurzbrief);
	}

	private KurzbriefDto[] assembleKurzbriefDtos(Collection<?> kurzbriefe) {
		List<KurzbriefDto> list = new ArrayList<KurzbriefDto>();
		if (kurzbriefe != null) {
			Iterator<?> iterator = kurzbriefe.iterator();
			while (iterator.hasNext()) {
				Kurzbrief kurzbrief = (Kurzbrief) iterator.next();
				list.add(assembleKurzbriefDto(kurzbrief));
			}
		}
		KurzbriefDto[] returnArray = new KurzbriefDto[list.size()];
		return (KurzbriefDto[]) list.toArray(returnArray);
	}

	/**
	 * Hole die Komm. (E-Mail, Fax, Telefon, ...) einer cNrKommunikationsartI von
	 * einem iIdPartnerAnsprechpartnerI bei einem iIdPartnerI oder <BR/>
	 * wenn iIdAnsprechpartnerI == null die des iIdPartnerI.
	 * 
	 * @param iIdPartnerI           Integer
	 * @param ansprechpartnerIId    Integer
	 * @param cNrKommunikationsartI String
	 * @param cNrMandantI           String
	 * @param theClientDto          String
	 * @return PartnerkommunikationDto
	 */
	public String partnerkommFindOhneExec(Integer iIdPartnerI, Integer ansprechpartnerIId, String cNrKommunikationsartI,
			String cNrMandantI, TheClientDto theClientDto) {

		String partnerkommunikationDto = null;
		partnerkommunikationDto = partnerkommFindOhneAnpassungOhneExec(iIdPartnerI, ansprechpartnerIId,
				cNrKommunikationsartI, cNrMandantI, theClientDto);
		// Nur bei Telefonnummern
		if (cNrKommunikationsartI != null && (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_DIREKTFAX)
				|| cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_FAX)
				|| cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_HANDY)
				|| cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_TELEFON))) {
			partnerkommunikationDto = passeInlandsAuslandsVorwahlAn(iIdPartnerI, cNrMandantI, partnerkommunikationDto,
					theClientDto);
		}
		return partnerkommunikationDto;
	}

	/**
	 * Wie
	 * {@link PartnerFacBean#partnerkommFindOhneExec(Integer, Integer, String, String, TheClientDto)},
	 * normiert aber TelNr nicht. Verwenden fuer Report wo TelNr so sein soll wie im
	 * Textfeld eingegeben
	 * 
	 * @param iIdPartnerI
	 * @param ansprechpartnerIId
	 * @param cNrKommunikationsartI
	 * @param cNrMandantI
	 * @param theClientDto
	 * @param partnerkommunikationDto
	 * @return
	 */
	public String partnerkommFindOhneAnpassungOhneExec(Integer iIdPartnerI, Integer ansprechpartnerIId,
			String cNrKommunikationsartI, String cNrMandantI, TheClientDto theClientDto) {
		String partnerkommunikationDto = null;
		try {
			if (ansprechpartnerIId != null) {
				// 1. hole komm. zu ansppartner bei partner
				AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(ansprechpartnerIId, theClientDto);

				if (ansprechpartnerDto != null) {
					// da gibt's eine komm
					if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_DIREKTFAX)
							&& ansprechpartnerDto.getCDirektfax() != null) {
						// direktfax
						partnerkommunikationDto = ansprechpartnerDto.getCDirektfax();
					} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_EMAIL)
							&& ansprechpartnerDto.getCEmail() != null) {
						// e-mail
						partnerkommunikationDto = ansprechpartnerDto.getCEmail();
					} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_FAX)
							&& ansprechpartnerDto.getCFax() != null) {
						// fax
						partnerkommunikationDto = ansprechpartnerDto.getCFax();
						partnerkommunikationDto = enrichNumber(iIdPartnerI, cNrKommunikationsartI, theClientDto,
								partnerkommunikationDto, false);
					} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_HANDY)
							&& ansprechpartnerDto.getCHandy() != null) {
						// handy
						partnerkommunikationDto = ansprechpartnerDto.getCHandy();
					} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_TELEFON)
							&& ansprechpartnerDto.getCTelefon() != null) {
						// telefon
						partnerkommunikationDto = ansprechpartnerDto.getCTelefon();

						if (Helper.short2boolean(ansprechpartnerDto.getBDurchwahl())) {
							partnerkommunikationDto = enrichNumber(iIdPartnerI, cNrKommunikationsartI, theClientDto,
									partnerkommunikationDto, false);
						}
					}

				}
			}

			// nix gefunden oder kein Ansprechpartner definiert -> nimm die des
			// Partners.
			if (partnerkommunikationDto == null) {

				PartnerDto p = getPartnerFac().partnerFindByPrimaryKey(iIdPartnerI, theClientDto);

				if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_DIREKTFAX)) {
					// direktfax
					return p.getCDirektfax();
				} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_EMAIL)) {
					// e-mail
					return p.getCEmail();
				} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_FAX)) {
					// fax
					partnerkommunikationDto = p.getCFax();
				} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_HANDY)) {
					// handy
					partnerkommunikationDto = p.getCHandy();
				} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_HOMEPAGE)) {
					// homepage
					partnerkommunikationDto = p.getCHomepage();
				} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_TELEFON)) {
					// telefon
					partnerkommunikationDto = p.getCTelefon();
				}

			}
			return partnerkommunikationDto;
		} catch (RemoteException e) {
			myLogger.logKritisch("Exception in partnerkommFind", e);
			return null;
		} catch (EJBExceptionLP e) {
			myLogger.logKritisch("Exception in partnerkommFind", e);
			return null;
		}
	}

	public String passeInlandsAuslandsVorwahlAn(Integer iIdPartnerI, String cNrMandantI, String cTelefonnummer,
			TheClientDto theClientDto) {
		return passeInlandsAuslandsVorwahlAn(iIdPartnerI, cNrMandantI, cTelefonnummer, false, theClientDto);
	}

	/**
	 * Vorwahlen fuer HVTE und Anrufe entsprechend anpassen <br>
	 * - fuer ausgehende Anrufe wird ein + am Anfang der Nummer mit der
	 * Auslandsvorwahl ausgetauscht <br>
	 * - fuer HVTE werden + bzw. die Auslandsvorwahl entfernt, wenn diese nicht
	 * vorhanden sind wird die Landesvorwahl des Landes vom Partner als Vorwahl
	 * angenommen
	 *
	 * @param iIdPartnerI
	 * @param cNrMandantI
	 * @param cTelefonnummer
	 * @param fuerHVTE
	 * @param theClientDto
	 * @return String cTelefonnummerAngepasst
	 */
	public String passeInlandsAuslandsVorwahlAn(Integer iIdPartnerI, String cNrMandantI, String cTelefonnummer,
			boolean fuerHVTE, TheClientDto theClientDto) {
		try {
			if (cTelefonnummer == null) {
				return null;
			}
			// SP1957
			cTelefonnummer = cTelefonnummer.trim().replaceAll(" ", "");

			ParametermandantDto pAuslandsvorwahl = getParameterFac().getMandantparameter(cNrMandantI,
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_AUSLANDSVORWAHL);
			String sAuslandsvorwahl = pAuslandsvorwahl.getCWert().trim();

			ParametermandantDto pInlandsvorwahl = getParameterFac().getMandantparameter(cNrMandantI,
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_INLANDSVORWAHL);
			String sInlandsvorwahl = pInlandsvorwahl.getCWert().trim();

			if (fuerHVTE) {
				if (cTelefonnummer.startsWith(sAuslandsvorwahl)) {
					cTelefonnummer = cTelefonnummer.substring(sAuslandsvorwahl.length());

					// Wenn keine Landesvorwahl vorhanden, dann Landesvorwahl des Partners verwenden
				} else if (!cTelefonnummer.startsWith("+")) {
					PartnerDto partnerDto = partnerFindByPrimaryKey(iIdPartnerI, theClientDto);
					// Wenn partner kein Land eingetragen hat, eigenes Land verwenden
					if (partnerDto.getLandplzortDto() == null) {
						partnerDto = getMandantFac().mandantFindByPrimaryKey(cNrMandantI, theClientDto).getPartnerDto();
					}
					String sTelefonvorwahl = partnerDto.getLandplzortDto().getLandDto().getCTelvorwahl();

					if (sTelefonvorwahl == null) {

						sTelefonvorwahl = "";
						// SP8378 Beim Partnerimport wissen wir die Vorwahl leider nicht, daher wurde
						// diese Fehler wieder auskommentiert
						// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LAND_HAT_KEINE_VORWAHL,
						// "Das Land hat keine Vorwahl",
						// partnerDto.getLandplzortDto().getLandDto().getCLkz());
					}
					// Wenn Inlandsvorwahl, dann entfernen und Laendervorwahl anhaengen
					if (cTelefonnummer.startsWith(sInlandsvorwahl)) {
						cTelefonnummer = cTelefonnummer.substring(sInlandsvorwahl.length());
					}

					// Wenn die Laendervorwahl die Auslandsvorwahl davor hat, dann die entfernen
					if (sTelefonvorwahl.startsWith("+")) {
						sTelefonvorwahl = sTelefonvorwahl.substring(1);
					} else if (sTelefonvorwahl.startsWith(sAuslandsvorwahl)) {
						sTelefonvorwahl = sTelefonvorwahl.substring(sAuslandsvorwahl.length());
					}

					cTelefonnummer = sTelefonvorwahl.trim() + cTelefonnummer;

				}

				if (cTelefonnummer.startsWith("+")) {
					cTelefonnummer = cTelefonnummer.substring(1);
				}
			} else {
				if (cTelefonnummer.startsWith("+")) {
					cTelefonnummer = sAuslandsvorwahl + cTelefonnummer.substring(1);
				}

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return cTelefonnummer;
	}

	public Integer getDefaultMWSTSatzIIdAnhandLand(LandDto landDto, TheClientDto theClientDto) {
		if (landDto == null)
			return null;

		try {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			String sLKZMandant = mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz();
			if (landDto.getCLkz().equals(sLKZMandant)) {
				return mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz();
			}

			return getSystemFac().isEUMitglied(landDto) ? mandantDto.getMwstsatzbezIIdStandardauslandmwstsatz()
					: mandantDto.getMwstsatzbezIIdStandarddrittlandmwstsatz();
		} catch (Throwable e) {
			// Sollte nicht vorkommen... und wenn doch dann gibts keinen MWST
			// vorbesetzt
			return null;
		}
	}

	/**
	 * Durchwahl gegen persoenliche Durchwahl austauschen.
	 * 
	 * @param iIdPartnerI           Integer
	 * @param cNrKommunikationsartI String
	 * @param theClientDto          aktueller Benutzer
	 * @param cTelefon              die Telefonnummer
	 * @param bNurNummerAbschneiden
	 * @return PartnerkommunikationDto
	 */
	public String enrichNumber(Integer iIdPartnerI, String cNrKommunikationsartI, TheClientDto theClientDto,
			String cTelefon, boolean bNurNummerAbschneiden) {
		String partnerKomm = getKommFromPartner(iIdPartnerI, cNrKommunikationsartI, theClientDto);

		int iAnzahlStellenDurchwahl = 3;
		try {
			ParametermandantDto pInlandsvorwahl = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_MAXIMALELAENGE_DURCHWAHL_ZENTRALE);

			iAnzahlStellenDurchwahl = new Integer(pInlandsvorwahl.getCWert());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		iAnzahlStellenDurchwahl++;

		StringBuffer sb = null;
		if (partnerKomm != null) {
			// Die allgemeine Durchwahl entfernen.
			sb = new StringBuffer(partnerKomm);
			// zb. 07754 8912-0
			int i = sb.lastIndexOf("-");
			if (i > -1 && (i >= (partnerKomm.length() - iAnzahlStellenDurchwahl)
			/*
			 * nur wenn maximal 'iAnzahlStellenDurchwahl' stellen nach dem letzen - gilt es
			 * als Durchwahl
			 */)) {
				// 07754 8912
				sb = new StringBuffer(sb.substring(0, i));
			}
		}
		if (partnerKomm != null && bNurNummerAbschneiden == true) {

			partnerKomm = sb.toString();
			return partnerKomm;
		} else {
			if (partnerKomm != null && cTelefon != null && sb != null && !cTelefon.equals(partnerKomm)) {
				// Die andere Durchwahl anhaengen.
				// 07754 8912 24
				sb.append("-").append(cTelefon);
				cTelefon = sb.toString();
			}
			return cTelefon;
		}

	}

	private String getKommFromPartner(Integer iIdPartnerI, String cNrKommunikationsartI, TheClientDto theClientDto) {

		// precondition
		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdPartnerI"));
		}
		if (cNrKommunikationsartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cNrKommunikationsartI"));
		}

		String partKomm = null;
		PartnerDto partnerDto = null;

		partnerDto = getPartnerFac().partnerFindByPrimaryKey(iIdPartnerI, theClientDto);
		if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_DIREKTFAX)) {
			partKomm = partnerDto.getCDirektfax();
		} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_EMAIL)) {
			partKomm = partnerDto.getCEmail();
		} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_FAX)) {
			partKomm = partnerDto.getCFax();
		} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_HANDY)) {
			partKomm = partnerDto.getCHandy();
		} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_HOMEPAGE)) {
			partKomm = partnerDto.getCHomepage();
		} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_TELEFON)) {
			partKomm = partnerDto.getCTelefon();
		}

		return partKomm;
	}

	/**
	 * Hole fuer den iIdAnsprechpartnerI die Kommunikation als String (E-Mail, Fax,
	 * Telefon, ...) ist eine Kombination der Daten von Partner (= normalerweise
	 * Kunde bzw. Lieferant) mit "seinem" Ansprechpartner. Ist der Ansprechpartner
	 * null, zieht nur der iIdPartnerI. <br>
	 * Seit SP8637 wird hier die TelNr nicht mehr angepasst, sondern wie eingegeben
	 * verwendet. Diese Methode sollte also nur f&uuml;r Reports verwendet werden
	 * 
	 * @param iIdPartnerAnsprechpartnerI Integer
	 * @param partnerDtoI                Integer
	 * @param cNrKommunikationsartI      String
	 * @param cNrMandantI                String
	 * @param theClientDto               String
	 * @return PartnerkommunikationDto
	 * @throws EJBExceptionLP
	 */
	public String partnerkommFindRespectPartnerAsStringOhneExec(Integer ansprechpartnerIId, PartnerDto partnerDtoI,
			String cNrKommunikationsartI, String cNrMandantI, TheClientDto theClientDto) throws EJBExceptionLP {

		String komm = partnerkommFindOhneAnpassungOhneExec(partnerDtoI.getIId(), ansprechpartnerIId,
				cNrKommunikationsartI, cNrMandantI, theClientDto);

		StringBuffer sKommunikation = new StringBuffer();

		if (komm != null) {
			// Praefix ist die Kommunikationsart
			String sPraefix;
			if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_EMAIL)) {
				sPraefix = getTextRespectUISpr("lp.email", theClientDto.getMandant(),
						Helper.string2Locale(partnerDtoI.getLocaleCNrKommunikation())) + ":";
			} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_TELEFON)) {
				sPraefix = getTextRespectUISpr("lp.tel", theClientDto.getMandant(),
						Helper.string2Locale(partnerDtoI.getLocaleCNrKommunikation()));
			} else if (cNrKommunikationsartI.equals(PartnerFac.KOMMUNIKATIONSART_FAX)) {
				sPraefix = getTextRespectUISpr("lp.fax", theClientDto.getMandant(),
						Helper.string2Locale(partnerDtoI.getLocaleCNrKommunikation())) + ":";
			} else {
				sPraefix = "";
			}
			// Praefix anhaengen
			sKommunikation.append(sPraefix).append(" ");
			sKommunikation.append(komm);
		}
		return sKommunikation.toString();
	}

	public PartnerkommunikationDto[] partnerkommFindByPartnerIIdKommunikationsartPAiIdKommArtMandant(
			Integer iIdPartnerI, String cNrKommunikationsartI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		PartnerkommunikationDto[] dto = null;
		// try {
		Query query = em.createNamedQuery("PartnerkommunikationfindByPartnerIIdKommunikationsartPAiIdKommArtMandant");
		query.setParameter(1, iIdPartnerI);
		query.setParameter(2, cNrKommunikationsartI);
		query.setParameter(3, cNrMandantI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		dto = assemblePartnerkommunikationDtos(cl);
		return dto;
	}

	public PartnerkommunikationDto[] partnerkommFindByPartnerIId(Integer iIdPartnerI) throws EJBExceptionLP {
		PartnerkommunikationDto[] aPartkommDto = null;
		Query query = em.createNamedQuery("PartnerkommunikationfindByPartnerIId");
		query.setParameter(1, iIdPartnerI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		aPartkommDto = assemblePartnerkommunikationDtos(cl);
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return aPartkommDto;
	}

	public PartnerkommunikationDto[] partnerkommFindByPartnerIIdMandantCNr(Integer iIdPartnerI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		PartnerkommunikationDto[] aPartkommDto = null;
		Query query = em.createNamedQuery("PartnerkommunikationfindByPartnerIIdMandantCNr");
		query.setParameter(1, iIdPartnerI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		aPartkommDto = assemblePartnerkommunikationDtos(cl);
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return aPartkommDto;
	}

	public PartnerkommunikationDto[] partnerkommFindByPartnerIIdMandantCNrOhneExc(Integer iIdPartnerI,
			String cNrMandantI, TheClientDto theClientDto) throws EJBExceptionLP {
		PartnerkommunikationDto[] aPartkommDto = null;
		Query query = em.createNamedQuery("PartnerkommunikationfindByPartnerIIdMandantCNr");
		query.setParameter(1, iIdPartnerI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		aPartkommDto = assemblePartnerkommunikationDtos(cl);
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return aPartkommDto;
	}

	private PartnerkommunikationDto[] assemblePartnerkommunikationDtos(Collection<?> partners) {
		List<PartnerkommunikationDto> list = new ArrayList<PartnerkommunikationDto>();
		if (partners != null) {
			Iterator<?> iterator = partners.iterator();
			while (iterator.hasNext()) {
				Partnerkommunikation partner = (Partnerkommunikation) iterator.next();
				list.add(assemblePartnerkommunikationDto(partner));
			}
		}
		PartnerkommunikationDto[] returnArray = new PartnerkommunikationDto[list.size()];
		return (PartnerkommunikationDto[]) list.toArray(returnArray);
	}

	public String formatFixAnredeTitelName2Name1(PartnerDto partnerDto, Locale loc, TheClientDto theClientDto)
			throws EJBExceptionLP {
		String ret = "";

		int iBriefanredeMitTitel = 0;

		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BRIEFANREDE_MIT_TITEL);
			iBriefanredeMitTitel = ((Integer) parameter.getCWertAsObject()).intValue();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		boolean bFamilie = false;
		if (partnerDto.getAnredeCNr() != null) {

			if (partnerDto.getAnredeCNr().equals(PartnerFac.PARTNER_ANREDE_FAMILIE)) {
				bFamilie = true;
			}

			AnredesprDto anredesprDto = null;
			if (loc != null) {
				anredesprDto = anredesprFindByAnredeCNrLocaleCNrOhneExc(partnerDto.getAnredeCNr(),
						Helper.locale2String(loc));
				if (anredesprDto != null) {
					ret += anredesprDto.getCBez().trim();
				}
			}
			// Keine Uebersetzung definiert -> AnredeCNr angeben.
			if (anredesprDto == null) {
				ret += partnerDto.getAnredeCNr().trim();
			}
		}

		if (partnerDto.getCTitel() != null) {

			if (iBriefanredeMitTitel == 0 || iBriefanredeMitTitel == 2) {
				ret += " " + partnerDto.getCTitel().trim();
			}
		}

		if (bFamilie == false) {

			try {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ANREDE_MIT_VORNAME);
				boolean bMitVorname = ((Boolean) parameter.getCWertAsObject()).booleanValue();

				if (bMitVorname == true && partnerDto.getCName2vornamefirmazeile2() != null) {
					ret += " " + partnerDto.getCName2vornamefirmazeile2().trim();
				}

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}
		}
		if (partnerDto.getCName1nachnamefirmazeile1() != null) {
			ret += " " + partnerDto.getCName1nachnamefirmazeile1().trim();
		}

		// SP4286 Kein NTtitel bei der Briefanrede
		// if (partnerDto.getCNtitel() != null) {
		// ret += " " + partnerDto.getCNtitel().trim();
		// }

		// SP4809 Titel anhand Parameter
		if (partnerDto.getCNtitel() != null) {
			if (iBriefanredeMitTitel == 2 || iBriefanredeMitTitel == 3) {
				ret += ", " + partnerDto.getCNtitel().trim();
			}
		}

		return ret.trim();
	}

	public String formatFixAnredeTitelName2Name1FuerAdresskopf(PartnerDto partnerDto, Locale loc,
			TheClientDto theClientDto) throws EJBExceptionLP {
		String ret = "";
		if (partnerDto.getAnredeCNr() != null) {
			AnredesprDto anredesprDto = null;
			if (loc != null) {
				anredesprDto = anredesprFindByAnredeCNrLocaleCNrOhneExc(partnerDto.getAnredeCNr(),
						Helper.locale2String(loc));
				if (anredesprDto != null) {
					ret += anredesprDto.getCBez().trim();
				}
			}
			// Keine Uebersetzung definiert -> AnredeCNr angeben.
			if (anredesprDto == null) {
				ret += partnerDto.getAnredeCNr().trim();
			}
		}

		if (partnerDto.getCTitel() != null) {
			ret += " " + partnerDto.getCTitel().trim();
		}
		if (partnerDto.getCName2vornamefirmazeile2() != null) {
			ret += " " + partnerDto.getCName2vornamefirmazeile2().trim();
		}
		if (partnerDto.getCName1nachnamefirmazeile1() != null) {
			ret += " " + partnerDto.getCName1nachnamefirmazeile1().trim();
		}
		if (partnerDto.getCNtitel() != null) {
			// SP4286 "," vor nachgestelletem Titel in Adresskopf
			ret += ", " + partnerDto.getCNtitel().trim();
		}
		return ret.trim();
	}

	public AnredesprDto anredesprFindByAnredeCNrLocaleCNr(String anredeCNr, String localeCNr) throws EJBExceptionLP {
		AnredesprDto anredesprDto = null;
		try {
			Query query = em.createNamedQuery("AnredesprfindByAnredeCNrLocaleCNr");
			query.setParameter(1, anredeCNr);
			query.setParameter(2, localeCNr);
			Anredespr anredespr = (com.lp.server.partner.ejb.Anredespr) query.getSingleResult();
			anredesprDto = assembleAnredesprDto(anredespr);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		return anredesprDto;
	}

	public AnredesprDto anredesprFindByAnredeCNrLocaleCNrOhneExc(String anredeCNr, String localeCNr) {
		AnredesprDto anredesprDto = null;
		try {
			Query query = em.createNamedQuery("AnredesprfindByAnredeCNrLocaleCNr");
			query.setParameter(1, anredeCNr);
			query.setParameter(2, localeCNr);
			Anredespr anredespr = (com.lp.server.partner.ejb.Anredespr) query.getSingleResult();
			anredesprDto = assembleAnredesprDto(anredespr);
		} catch (NoResultException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			return null;
		}
		return anredesprDto;
	}

	public PartnerDto partnerFindByUIDNull(String uidNummer) {
		if (Helper.isStringEmpty(uidNummer))
			return null;

		PartnerDto partnerDto = null;
		try {
			// Query query = createNamedQuery(
			// em, Partner.QueryByUID, new QueryParam[] {new QueryParam("uid",
			// UidNumber.trim())}) ;
			Partner partner = (Partner) PartnerQuery.byUID(em, uidNummer).getSingleResult();
			partnerDto = assemblePartnerDto(partner);
		} catch (NoResultException e) {
		} catch (NonUniqueResultException e) {
		}

		return partnerDto;
	}

	public PartnerDto[] partnerFindByUID(String uidNummer) {
		if (Helper.isStringEmpty(uidNummer)) {
			return new PartnerDto[] {};
		}

		List<Partner> partners = PartnerQuery.listByUid(em, uidNummer);
		return assemblePartnerDtos(partners);
	}

	public PartnerDto[] partnerFindByName1Lower(String sName1I) throws EJBExceptionLP {
		Query query = PartnerQuery.byLowerCName1(em, sName1I);
		return assemblePartnerDtos(query.getResultList());
	}

	/**
	 * Hat der Partner einen externen Versandweg - wie beispielsweise Clevercure /
	 * EDI - definiert?</br>
	 * 
	 * @param partnerIId
	 * @return true wenn fuer den Partner ein Versandweg definiert ist
	 * @throws RemoteException
	 */
	public boolean hatPartnerVersandweg(Integer partnerIId) throws RemoteException {
		Partner partner = em.find(Partner.class, partnerIId);
		if (partner == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return partner.getVersandwegIId() != null;
	}

	/**
	 * Hat der Partner einen externen Versandweg - wie beispielsweise Clevercure /
	 * EDI - definiert?</br>
	 * 
	 * @param partnerDto
	 * @return true wenn fuer den Partner ein Versandweg definiert ist
	 * @throws RemoteException
	 */
	public boolean hatPartnerVersandweg(PartnerDto partnerDto) throws RemoteException {
		return partnerDto.getVersandwegIId() != null;
	}

	public PartnerDto[] partnerFindByEmail(String email) throws EJBExceptionLP {
		Query query = PartnerQuery.byEmail(em, email);
		return assemblePartnerDtos(query.getResultList());
	}

	public Integer partnerIdFindByAnsprechpartnerId(Integer ansprechpartnerId) {
		Validator.notNull(ansprechpartnerId, "ansprechpartnerId");

		Ansprechpartner ansprechpartner = em.find(Ansprechpartner.class, ansprechpartnerId);
		return ansprechpartner == null ? null : ansprechpartner.getPartnerIId();
	}

	public PartnerDto partnerFindByAnsprechpartnerId(Integer ansprechpartnerId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.notNull(ansprechpartnerId, "ansprechpartnerId");

		Ansprechpartner ansprechpartner = em.find(Ansprechpartner.class, ansprechpartnerId);
		if (ansprechpartner == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ansprechpartnerId.toString());
		}

		return partnerFindByPrimaryKey(ansprechpartner.getPartnerIId(), theClientDto);
	}

	protected ModelMapper getMapper() {
		if (mapper == null) {
			mapper = new ModelMapper();
		}
		return mapper;
	}

	@Override
	public Integer createGeodaten(GeodatenDto geodatenDto) {
		Validator.dtoNotNull(geodatenDto, "geodatenDto");
		Validator.notNull(geodatenDto.getPartnerIId(), "geodatenDto.getPartnerIId()");
		Validator.notNull(geodatenDto.getLaengengrad(), "geodatenDto.getLaengengrad()");
		Validator.notNull(geodatenDto.getBreitengrad(), "geodatenDto.getBreitengrad()");

		Geodaten entity = GeodatenQuery.resultByPartnerIIdNoEx(geodatenDto.getPartnerIId(), em);
		if (entity != null)
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("UK_PART_GEODATEN"));

		entity = getMapper().map(geodatenDto, Geodaten.class);
		em.persist(entity);
		em.flush();

		return entity.getIId();
	}

	@Override
	public void removeGeodaten(Integer geodatenIId) {
		Validator.pkFieldNotNull(geodatenIId, "geodatenIId");

		Geodaten toRemove = em.find(Geodaten.class, geodatenIId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, geodatenIId.toString());
		}

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	@Override
	public void updateGeodaten(GeodatenDto geodatenDto) {
		Validator.dtoNotNull(geodatenDto, "geodatenDto");
		Validator.pkFieldNotNull(geodatenDto.getIId(), "geodatenDto.getIId()");

		Geodaten entity = em.find(Geodaten.class, geodatenDto.getIId());
		if (entity == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, geodatenDto.getIId().toString());
		}

		entity = getMapper().map(geodatenDto, Geodaten.class);
		em.merge(entity);
		em.flush();
	}

	@Override
	public GeodatenDto geodatenFindByPartnerIIdOhneExc(Integer partnerIId) {
		Validator.notNull(partnerIId, "geodatenDto.getPartnerIId()");

		Geodaten entity = GeodatenQuery.resultByPartnerIIdNoEx(partnerIId, em);
		return entity != null ? GeodatenDtoAssembler.createDto(entity) : null;
	}

	@Override
	public void createGeodaten(List<GeodatenDto> createList) {
		Validator.notNull(createList, "updateList");
		for (GeodatenDto geodatenDto : createList) {
			createGeodaten(geodatenDto);
		}
	}

}
