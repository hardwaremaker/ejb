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
package com.lp.server.personal.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.PartnerDtoSmall;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.personal.ejb.Angehoerigenart;
import com.lp.server.personal.ejb.Angehoerigenartspr;
import com.lp.server.personal.ejb.AngehoerigenartsprPK;
import com.lp.server.personal.ejb.Artikelzulage;
import com.lp.server.personal.ejb.Bereitschaftart;
import com.lp.server.personal.ejb.Bereitschafttag;
import com.lp.server.personal.ejb.Beruf;
import com.lp.server.personal.ejb.Betriebskalender;
import com.lp.server.personal.ejb.Eintrittaustritt;
import com.lp.server.personal.ejb.Fahrzeug;
import com.lp.server.personal.ejb.Fahrzeugkosten;
import com.lp.server.personal.ejb.Familienstand;
import com.lp.server.personal.ejb.Familienstandspr;
import com.lp.server.personal.ejb.FamilienstandsprPK;
import com.lp.server.personal.ejb.Feiertag;
import com.lp.server.personal.ejb.Gleitzeitsaldo;
import com.lp.server.personal.ejb.Kollektiv;
import com.lp.server.personal.ejb.Kollektivuestd;
import com.lp.server.personal.ejb.Kollektivuestd50;
import com.lp.server.personal.ejb.Lohnart;
import com.lp.server.personal.ejb.Lohnartstundenfaktor;
import com.lp.server.personal.ejb.Lohngruppe;
import com.lp.server.personal.ejb.Lohnstundenart;
import com.lp.server.personal.ejb.Pendlerpauschale;
import com.lp.server.personal.ejb.Personal;
import com.lp.server.personal.ejb.PersonalQuery;
import com.lp.server.personal.ejb.Personalangehoerige;
import com.lp.server.personal.ejb.Personalart;
import com.lp.server.personal.ejb.Personalartspr;
import com.lp.server.personal.ejb.PersonalartsprPK;
import com.lp.server.personal.ejb.Personalfunktion;
import com.lp.server.personal.ejb.Personalfunktionspr;
import com.lp.server.personal.ejb.PersonalfunktionsprPK;
import com.lp.server.personal.ejb.Personalgehalt;
import com.lp.server.personal.ejb.Personalgruppe;
import com.lp.server.personal.ejb.Personalgruppekosten;
import com.lp.server.personal.ejb.Personalverfuegbarkeit;
import com.lp.server.personal.ejb.Personalzeiten;
import com.lp.server.personal.ejb.Personalzeitmodell;
import com.lp.server.personal.ejb.Religion;
import com.lp.server.personal.ejb.Religionspr;
import com.lp.server.personal.ejb.ReligionsprPK;
import com.lp.server.personal.ejb.Schichtzeitmodell;
import com.lp.server.personal.ejb.Signatur;
import com.lp.server.personal.ejb.Stundenabrechnung;
import com.lp.server.personal.ejb.Urlaubsanspruch;
import com.lp.server.personal.ejb.Zeitabschluss;
import com.lp.server.personal.ejb.Zulage;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.personal.fastlanereader.generated.FLRPersonalverfuegbarkeit;
import com.lp.server.personal.service.AngehoerigenartDto;
import com.lp.server.personal.service.AngehoerigenartDtoAssembler;
import com.lp.server.personal.service.AngehoerigenartsprDto;
import com.lp.server.personal.service.AngehoerigenartsprDtoAssembler;
import com.lp.server.personal.service.ArtikelzulageDto;
import com.lp.server.personal.service.ArtikelzulageDtoAssembler;
import com.lp.server.personal.service.BereitschaftartDto;
import com.lp.server.personal.service.BereitschaftartDtoAssembler;
import com.lp.server.personal.service.BereitschafttagDto;
import com.lp.server.personal.service.BereitschafttagDtoAssembler;
import com.lp.server.personal.service.BerufDto;
import com.lp.server.personal.service.BerufDtoAssembler;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.BetriebskalenderDtoAssembler;
import com.lp.server.personal.service.EintrittaustrittDto;
import com.lp.server.personal.service.EintrittaustrittDtoAssembler;
import com.lp.server.personal.service.FahrzeugDto;
import com.lp.server.personal.service.FahrzeugDtoAssembler;
import com.lp.server.personal.service.FahrzeugkostenDto;
import com.lp.server.personal.service.FahrzeugkostenDtoAssembler;
import com.lp.server.personal.service.FamilienstandDto;
import com.lp.server.personal.service.FamilienstandDtoAssembler;
import com.lp.server.personal.service.FamilienstandsprDto;
import com.lp.server.personal.service.FamilienstandsprDtoAssembler;
import com.lp.server.personal.service.FeiertagDto;
import com.lp.server.personal.service.FeiertagDtoAssembler;
import com.lp.server.personal.service.GleitzeitsaldoDto;
import com.lp.server.personal.service.GleitzeitsaldoDtoAssembler;
import com.lp.server.personal.service.KollektivDto;
import com.lp.server.personal.service.KollektivDtoAssembler;
import com.lp.server.personal.service.Kollektivuestd50Dto;
import com.lp.server.personal.service.Kollektivuestd50DtoAssembler;
import com.lp.server.personal.service.KollektivuestdDto;
import com.lp.server.personal.service.KollektivuestdDtoAssembler;
import com.lp.server.personal.service.LohnartDto;
import com.lp.server.personal.service.LohnartDtoAssembler;
import com.lp.server.personal.service.LohnartstundenfaktorDto;
import com.lp.server.personal.service.LohnartstundenfaktorDtoAssembler;
import com.lp.server.personal.service.LohngruppeDto;
import com.lp.server.personal.service.LohngruppeDtoAssembler;
import com.lp.server.personal.service.LohnstundenartDto;
import com.lp.server.personal.service.LohnstundenartDtoAssembler;
import com.lp.server.personal.service.PendlerpauschaleDto;
import com.lp.server.personal.service.PendlerpauschaleDtoAssembler;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalDtoAssembler;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.PersonalangehoerigeDto;
import com.lp.server.personal.service.PersonalangehoerigeDtoAssembler;
import com.lp.server.personal.service.PersonalartDto;
import com.lp.server.personal.service.PersonalartDtoAssembler;
import com.lp.server.personal.service.PersonalartsprDto;
import com.lp.server.personal.service.PersonalartsprDtoAssembler;
import com.lp.server.personal.service.PersonalfunktionDto;
import com.lp.server.personal.service.PersonalfunktionDtoAssembler;
import com.lp.server.personal.service.PersonalfunktionsprDto;
import com.lp.server.personal.service.PersonalfunktionsprDtoAssembler;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.PersonalgehaltDtoAssembler;
import com.lp.server.personal.service.PersonalgruppeDto;
import com.lp.server.personal.service.PersonalgruppeDtoAssembler;
import com.lp.server.personal.service.PersonalgruppekostenDto;
import com.lp.server.personal.service.PersonalgruppekostenDtoAssembler;
import com.lp.server.personal.service.PersonalverfuegbarkeitDto;
import com.lp.server.personal.service.PersonalverfuegbarkeitDtoAssembler;
import com.lp.server.personal.service.PersonalzeitenDto;
import com.lp.server.personal.service.PersonalzeitenDtoAssembler;
import com.lp.server.personal.service.PersonalzeitmodellDto;
import com.lp.server.personal.service.PersonalzeitmodellDtoAssembler;
import com.lp.server.personal.service.PersonalzutrittsklasseDto;
import com.lp.server.personal.service.ReligionDto;
import com.lp.server.personal.service.ReligionDtoAssembler;
import com.lp.server.personal.service.ReligionsprDto;
import com.lp.server.personal.service.ReligionsprDtoAssembler;
import com.lp.server.personal.service.SchichtzeitmodellDto;
import com.lp.server.personal.service.SchichtzeitmodellDtoAssembler;
import com.lp.server.personal.service.StundenabrechnungDto;
import com.lp.server.personal.service.StundenabrechnungDtoAssembler;
import com.lp.server.personal.service.UrlaubsanspruchDto;
import com.lp.server.personal.service.UrlaubsanspruchDtoAssembler;
import com.lp.server.personal.service.ZeitabschlussDto;
import com.lp.server.personal.service.ZeitabschlussDtoAssembler;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitstiftDto;
import com.lp.server.personal.service.ZulageDto;
import com.lp.server.personal.service.ZulageDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBExceptionLPwoRollback;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;
import com.lp.util.report.PersonRpt;

@Stateless
public class PersonalFacBean extends LPReport implements PersonalFac {
	@PersistenceContext
	private EntityManager em;
	private Personalart personalart;

	private Personalfunktion personalfunktion;

	private Object[][] data = null;

	private static int REPORT_PERSONALLISTE_PERSONALNUMMER = 0;
	private static int REPORT_PERSONALLISTE_AUSWEISNUMMER = 1;
	private static int REPORT_PERSONALLISTE_NAME = 2;
	private static int REPORT_PERSONALLISTE_VORNAME = 3;
	private static int REPORT_PERSONALLISTE_ART = 4;
	private static int REPORT_PERSONALLISTE_KOSTENSTELLE = 5;
	private static int REPORT_PERSONALLISTE_ZUTRITTSKLASSE = 6;
	private static int REPORT_PERSONALLISTE_BILD = 7;
	private static int REPORT_PERSONALLISTE_GEBURTSTAG = 8;
	private static int REPORT_PERSONALLISTE_ALTER = 9;

	private static int REPORT_PERSONALLISTE_SUBREPORT_BANKVERBINDUNG = 10;
	private static int REPORT_PERSONALLISTE_EINTRITTSDATUM_ZUM_STICHTAG = 11;
	private static int REPORT_PERSONALLISTE_ZEITMODELL_ZUM_STICHTAG = 12;
	private static int REPORT_PERSONALLISTE_FAMILIENASTAND = 13;
	private static int REPORT_PERSONALLISTE_GEBURTSORT = 14;
	private static int REPORT_PERSONALLISTE_SOZIALVERSNR = 15;
	private static int REPORT_PERSONALLISTE_SOZIALVERSICHERER = 16;
	private static int REPORT_PERSONALLISTE_STAATSANGEHOERIGKEIT = 17;
	private static int REPORT_PERSONALLISTE_RELIGION = 18;
	private static int REPORT_PERSONALLISTE_KOLLEKTIV = 19;
	private static int REPORT_PERSONALLISTE_BERUF = 20;
	private static int REPORT_PERSONALLISTE_PENDLERPAUSCHALE = 21;
	private static int REPORT_PERSONALLISTE_LOHNGRUPPE = 22;
	private static int REPORT_PERSONALLISTE_FIRMENZUGEHOERIGKEIT = 23;
	private static int REPORT_PERSONALLISTE_UNTERSCHRIFTSFUNKTION = 24;
	private static int REPORT_PERSONALLISTE_UNTERSCHRIFTSTEXT = 25;
	private static int REPORT_PERSONALLISTE_KEINE_UEBERSTUNDENAUSZAHLUNG = 26;
	private static int REPORT_PERSONALLISTE_ABSENDER_DURCHWAHL = 27;
	private static int REPORT_PERSONALLISTE_ABSENDER_EMAIL = 28;
	private static int REPORT_PERSONALLISTE_ABSENDER_FAXDW = 29;
	private static int REPORT_PERSONALLISTE_ABSENDER_DIREKTFAX = 30;
	private static int REPORT_PERSONALLISTE_ABSENDER_HANDY = 31;
	private static int REPORT_PERSONALLISTE_KURZZEICHEN = 32;
	private static int REPORT_PERSONALLISTE_KURZBEZEICHNUNG = 33;
	private static int REPORT_PERSONALLISTE_TITEL = 34;
	private static int REPORT_PERSONALLISTE_VORNAME2 = 35;
	private static int REPORT_PERSONALLISTE_GESCHLECHT = 36;
	private static int REPORT_PERSONALLISTE_STRASSE = 37;
	private static int REPORT_PERSONALLISTE_PERSONALGRUPPE = 38;
	private static int REPORT_PERSONALLISTE_ORT = 39;
	private static int REPORT_PERSONALLISTE_PERSONALFUNKTION = 40;
	private static int REPORT_PERSONALLISTE_HEIMATKOSTENSTELLE = 41;
	private static int REPORT_PERSONALLISTE_TELEFON = 42;
	private static int REPORT_PERSONALLISTE_FAX = 43;
	private static int REPORT_PERSONALLISTE_EMAIL = 44;
	private static int REPORT_PERSONALLISTE_HOMEPAGE = 45;
	private static int REPORT_PERSONALLISTE_HANDY = 46;
	private static int REPORT_PERSONALLISTE_SUBREPORT_ANGEHOERIGE = 47;

	private static int REPORT_PERSONALLISTE_ANZAHL_SPALTEN = 48;

	/**
	 * Legt einen neuen Personal-Datensatz an
	 * 
	 * @param personalDto
	 *            PersonalDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return Integer
	 */
	public Integer createPersonal(PersonalDto personalDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (personalDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalDto == null"));
		}

		if (personalDto.getCPersonalnr() == null
				|| personalDto.getKostenstelleIIdStamm() == null
				|| personalDto.getPersonalartCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"personalDto.getIPersonalnr() == null  || personalDto.getKostenstelleIIdStamm() == null || personalDto.getPersonalartCNr() == null"));
		}

		if (personalDto.getMandantCNr() == null) {
			personalDto.setMandantCNr(theClientDto.getMandant());
		}

		try {
			Query query = em
					.createNamedQuery("PersonalfindByCPersonalnrMandantCNr");
			query.setParameter(1, personalDto.getCPersonalnr());
			query.setParameter(2, personalDto.getMandantCNr());
			Personal doppelt = (Personal) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_PERSONAL.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		pruefePersonalnummer(personalDto.getCPersonalnr(), theClientDto);

		if (personalDto.getCAusweis() != null) {
			// try {
			Query query1 = em.createNamedQuery("PersonalfindByCAusweis");
			query1.setParameter(1, personalDto.getCAusweis());
			Collection<?> cl = query1.getResultList();
			// if (! cl.isEmpty()) {
			PersonalDto[] dtos = assemblePersonalDtos(cl, true);
			if (dtos != null && dtos.length > 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_PERSONAL_DUPLICATE_AUSWEIS,
						new Exception("PERS_PERSONAL.C_AUSWEIS"));
				// }
				// }
				// catch (FinderException ex1) {
				// nothing here
				// }
			}
		}

		if (personalDto.getPartnerIId() == null) {
			if (personalDto.getPartnerDto() != null) {
				try {
					personalDto.getPartnerDto().setPartnerartCNr(
							PartnerFac.PARTNERART_PERSON);
					try {
						personalDto.getPartnerDto().setLocaleCNrKommunikation(
								theClientDto.getLocMandantAsString());
					} catch (Exception ex) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
					}
					personalDto.setPartnerIId(getPartnerFac().createPartner(
							personalDto.getPartnerDto(), theClientDto));
				} catch (RemoteException ex) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
				}
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("FEHLER_FELD_DARF_NICHT_NULL_SEIN"));
			}

		} else {
			try {
				if (personalDto.getPartnerDto() != null) {
					getPartnerFac().updatePartner(personalDto.getPartnerDto(),
							theClientDto);
				}
			} catch (RemoteException ex1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
			}

		}

		try {
			Query query2 = em
					.createNamedQuery("PersonalfindByPartnerIIdMandantCNr");
			query2.setParameter(1, personalDto.getPartnerIId());
			query2.setParameter(2, personalDto.getMandantCNr());
			Personal doppelt1 = (Personal) query2.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_PERSONAL.UK"));
		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PERSONAL);
		personalDto.setIId(pk);

		try {
			Personal personal = new Personal(personalDto.getIId(),
					personalDto.getPartnerIId(), personalDto.getMandantCNr(),
					personalDto.getCPersonalnr(),
					personalDto.getPersonalartCNr(),
					personalDto.getKostenstelleIIdStamm());
			em.persist(personal);
			em.flush();

			if (personalDto.getBAnwesenheitsliste() == null) {
				personalDto.setBAnwesenheitsliste(personal
						.getBAnwesenheitsliste());
			}
			if (personalDto.getBMaennlich() == null) {
				personalDto.setBMaennlich(personal.getBMaennlich());
			}
			if (personalDto.getBUeberstundenausbezahlt() == null) {
				personalDto.setBUeberstundenausbezahlt(personal
						.getBUeberstundenausbezahlt());
			}
			if (personalDto.getBVersteckt() == null) {
				personalDto.setBVersteckt(personal.getBVersteckt());
			}
			if (personalDto.getBAnwesenheitTerminal() == null) {
				personalDto.setBAnwesenheitTerminal(personal
						.getBAnwesenheitTerminal());
			}
			if (personalDto.getBAnwesenheitalleterminal() == null) {
				personalDto.setBAnwesenheitalleterminal(personal
						.getBAnwesenheitalleterminal());
			}
			if (personalDto.getBTelefonzeitstarten() == null) {
				personalDto.setBTelefonzeitstarten(personal
						.getBTelefonzeitstarten());
			}

			setPersonalFromPersonalDto(personal, personalDto);

			personal.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			personal.setPersonalIIdAendern(theClientDto.getIDPersonal());

		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return personalDto.getIId();
	}

	private void pruefePersonalnummer(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("cNr == null"));
		}

		String gueltigeZeichen = "";
		try {

			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_PERSONALNUMMER_ZEICHENSATZ);

			gueltigeZeichen = parameter.getCWert();

			for (int i = 0; i < cNr.length(); i++) {
				boolean bErlaubt = false;
				char c = cNr.charAt(i);

				for (int j = 0; j < gueltigeZeichen.length(); j++) {
					if (c == gueltigeZeichen.charAt(j)) {
						bErlaubt = true;
						break;
					}
				}

				if (bErlaubt == false) {
					ArrayList<Object> l = new ArrayList<Object>();
					l.add(new Character(c));
					l.add(cNr);
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_PERSONAL_ZEICHEN_IN_PERSONALNUMMER_NICHT_ERLAUBT,
							l,
							new Exception(
									"FEHLER_PERSONAL_ZEICHEN_IN_PERSONALNUMMER_NICHT_ERLAUBT"));
				}

			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public void removePersonal(PersonalDto personalDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (personalDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalDto == null"));
		}
		if (personalDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalDto.getIId() == null"));
		}
		try {

			Personal personal = em.find(Personal.class, personalDto.getIId());
			if (personal == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			em.remove(personal);
			em.flush();
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	/**
	 * Schreibt &Auml;nderungen im Personaldatensatz zur&uuml;ck
	 * 
	 * @param personalDto
	 *            PersonalDto
	 * @param theClientDto
	 *            User-ID
	 * @throws EJBExceptionLP
	 */
	public void updatePersonal(PersonalDto personalDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (personalDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalDto == null"));
		}
		if (personalDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalDto.getIId() == null"));
		}
		if (personalDto.getBAnwesenheitsliste() == null
				|| personalDto.getBMaennlich() == null
				|| personalDto.getBUeberstundenausbezahlt() == null
				|| personalDto.getBVersteckt() == null
				|| personalDto.getPartnerIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"personalDto.getBAnwesenheitsliste() == null || personalDto.getBMaennlich() == null || personalDto.getBUeberstundenausbezahlt() == null || personalDto.getBVersteckt() == null || personalDto.getPartnerIId() == null "));
		}
		Integer iId = personalDto.getIId();
		// try {
		Personal personal = em.find(Personal.class, iId);
		if (personal == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			getPartnerFac().updatePartner(personalDto.getPartnerDto(),
					theClientDto);
		} catch (RemoteException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
		}
		try {
			Query query = em
					.createNamedQuery("PersonalfindByCPersonalnrMandantCNr");
			query.setParameter(1, personalDto.getCPersonalnr());
			query.setParameter(2, theClientDto.getMandant());
			Integer iIdVorhanden = ((Personal) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PERSONAL.UK"));
			}

		} catch (javax.persistence.NoResultException ex) {
			//
		}

		pruefePersonalnummer(personalDto.getCPersonalnr(), theClientDto);

		if (personalDto.getCAusweis() != null) {
			// try {
			Query query1 = em.createNamedQuery("PersonalfindByCAusweis");
			query1.setParameter(1, personalDto.getCAusweis());
			Collection<?> cl = query1.getResultList();
			// if (! cl.isEmpty()) {
			PersonalDto[] dtos = assemblePersonalDtos(cl, true);
			if (dtos != null && dtos.length > 0) {

				if (dtos[0].getIId().equals(iId) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_PERSONAL_DUPLICATE_AUSWEIS,
							new Exception("PERS_PERSONAL.C_AUSWEIS"));
				}
			}
			// }
			// catch (FinderException ex1) {
			// nothing here
			// }
		}

		try {
			Query query2 = em
					.createNamedQuery("PersonalfindByPartnerIIdMandantCNr");
			query2.setParameter(1, personalDto.getPartnerIId());
			query2.setParameter(2, theClientDto.getMandant());
			Personal doppelt = (Personal) query2.getSingleResult();
			if (doppelt != null) {
				Integer iIdVorhanden = doppelt.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PERS_PERSONAL.UK"));
				}

			}
		} catch (javax.persistence.NoResultException ex) {
			//
		}

		// Wenn Person einem Zeitstift zugeordnet ist und der Ausweis ungleich
		// 3stellig ist, dann Fehler
		ZeitstiftDto[] zeitstiftDtos = null;
		try {
			zeitstiftDtos = getZeiterfassungFac().zeitstiftFindByPersonalIId(
					iId);
		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}
		if (zeitstiftDtos != null && zeitstiftDtos.length > 0) {
			for (int i = 0; i < zeitstiftDtos.length; i++) {
				if (personalDto.getCAusweis() == null
						|| personalDto.getCAusweis().length() != 3) {
					if (zeitstiftDtos[i].getCTyp().equals(
							ZeiterfassungFac.ZEITSTIFT_TYP_F630)) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_AUSWEISNUMMER_ZESTIFT,
								new Exception("FEHLER_AUSWEISNUMMER_ZESTIFT"));
					}
				}
			}

		}

		setPersonalFromPersonalDto(personal, personalDto);
		personal.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		personal.setPersonalIIdAendern(theClientDto.getIDPersonal());

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	/**
	 * Datet nur X_KOMMENTAR up
	 * 
	 * @param personalDto
	 *            personalDto
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void updatePersonalKommentar(PersonalDto personalDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (personalDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalDto == null"));
		}
		if (personalDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalDto.getIId() == null"));
		}
		// try {
		Personal personal = em.find(Personal.class, personalDto.getIId());
		if (personal == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		personal.setXKommentar(personalDto.getXKommentar());
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public PartnerDtoSmall partnerDtoSmallFindByPersonalIId(
			Integer personalIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// check2(cNrUserI);
		if (personalIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalIId == null"));
		}
		PartnerDtoSmall partnerDtoSmall = null;
		try {
			Personal personal = em.find(Personal.class, personalIId);
			if (personal == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

			}
			partnerDtoSmall = getPartnerFac().partnerFindByPrimaryKeySmall(
					personal.getPartnerIId(), theClientDto);
			partnerDtoSmall.setCPersonalnummer(personal.getCPersonalnr());
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEI_FINDBYPRIMARYKEY,
			// ex);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return partnerDtoSmall;
	}

	/**
	 * Holt einen Personaldatensatz und den dazugeh&ouml;rigen Partner-
	 * Datensatz
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return PersonalDto
	 */
	public PersonalDto personalFindByPrimaryKeySmall(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		PersonalDto personalDto = personalFindByPrimaryKeySmallOhneExc(iId);
		if (personalDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return personalDto;
	}

	public PersonalDto personalFindByPrimaryKeySmallOhneExc(Integer iId) {
		if (iId == null) {
			return null;
		}
		Personal personal = em.find(Personal.class, iId);
		if (personal == null) {
			return null;
		}
		return assemblePersonalDto(personal);
	}

	/**
	 * Holt einen Personaldatensatz und den dazugeh&ouml;rigen Partner-
	 * Datensatz
	 * 
	 * @param iId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return PersonalDto
	 * @throws EJBExceptionLP
	 */
	public PersonalDto personalFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// check2(idUser);
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Personal personal = em.find(Personal.class, iId);
		if (personal == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		PersonalDto personalDto = assemblePersonalDto(personal);
		if (personalDto.getBerufIId() != null) {
			Beruf beruf = em.find(Beruf.class, personalDto.getBerufIId());
			if (beruf == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			personalDto.setBerufDto(assembleBerufDto(beruf));
		}
		if (personalDto.getKollektivIId() != null) {
			Kollektiv kollektiv = em.find(Kollektiv.class,
					personalDto.getKollektivIId());
			if (kollektiv == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			personalDto.setKollektivDto(assembleKollektivDto(kollektiv));
		}
		if (personalDto.getLohngruppeIId() != null) {
			Lohngruppe lohngruppe = em.find(Lohngruppe.class,
					personalDto.getLohngruppeIId());
			if (lohngruppe == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			personalDto.setLohngruppeDto(assembleLohngruppeDto(lohngruppe));
		}

		if (personalDto.getPendlerpauschaleIId() != null) {
			personalDto.setPendlerpauschaleDto(assemblePendlerpauschaleDto(em
					.find(Pendlerpauschale.class,
							personalDto.getPendlerpauschaleIId())));
		}
		if (personalDto.getReligionIId() != null) {
			Religion religion = em.find(Religion.class,
					personalDto.getReligionIId());
			if (religion == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			personalDto.setReligionDto(assembleReligionDto(religion));
		}
		try {
			if (personalDto.getPartnerIIdSozialversicherer() != null) {
				personalDto.setPartnerDto_Sozialversicherer(getPartnerFac()
						.partnerFindByPrimaryKey(
								personalDto.getPartnerIIdSozialversicherer(),
								theClientDto));

			}
			if (personalDto.getPartnerIIdFirma() != null) {
				personalDto
						.setPartnerDto_Firma(getPartnerFac()
								.partnerFindByPrimaryKey(
										personalDto.getPartnerIIdFirma(),
										theClientDto));

			}
			if (personalDto.getLandplzortIIdGeburt() != null) {
				personalDto.setLandplzortDto_Geburtsort(getSystemFac()
						.landplzortFindByPrimaryKey(
								personalDto.getLandplzortIIdGeburt()));

			}
			if (personalDto.getLandIIdStaatsangehoerigkeit() != null) {
				personalDto.setLandDto(getSystemFac().landFindByPrimaryKey(
						personalDto.getLandIIdStaatsangehoerigkeit()));

			}
			if (personalDto.getKostenstelleIIdAbteilung() != null) {
				personalDto.setKostenstelleDto_Abteilung(getSystemFac()
						.kostenstelleFindByPrimaryKey(
								personalDto.getKostenstelleIIdAbteilung()));

			}
			if (personalDto.getKostenstelleIIdStamm() != null) {
				personalDto.setKostenstelleDto_Stamm(getSystemFac()
						.kostenstelleFindByPrimaryKey(
								personalDto.getKostenstelleIIdStamm()));

			}

			personalDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
					personalDto.getPartnerIId(), theClientDto));
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return personalDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setPersonalFromPersonalDto(Personal personal,
			PersonalDto personalDto) {
		personal.setPartnerIId(personalDto.getPartnerIId());
		personal.setMandantCNr(personalDto.getMandantCNr());
		personal.setCPersonalnr(personalDto.getCPersonalnr());
		personal.setPersonalartCNr(personalDto.getPersonalartCNr());
		personal.setCAusweis(personalDto.getCAusweis());
		personal.setBMaennlich(personalDto.getBMaennlich());
		personal.setFamilienstandCNr(personalDto.getFamilienstandCNr());
		personal.setKollektivIId(personalDto.getKollektivIId());
		personal.setBerufIId(personalDto.getBerufIId());
		personal.setLohngruppeIId(personalDto.getLohngruppeIId());
		personal.setLandIIdStaatsangehoerigkeit(personalDto
				.getLandIIdStaatsangehoerigkeit());
		personal.setBUeberstundenausbezahlt(personalDto
				.getBUeberstundenausbezahlt());
		personal.setReligionIId(personalDto.getReligionIId());
		personal.setLandplzortIIdGeburt(personalDto.getLandplzortIIdGeburt());
		personal.setTGeburtsdatum(personalDto.getTGeburtsdatum());
		personal.setCSozialversnr(personalDto.getCSozialversnr());
		personal.setPartnerIIdSozialversicherer(personalDto
				.getPartnerIIdSozialversicherer());
		personal.setPartnerIIdFirma(personalDto.getPartnerIIdFirma());
		personal.setKostenstelleIIdAbteilung(personalDto
				.getKostenstelleIIdAbteilung());
		personal.setKostenstelleIIdStamm(personalDto.getKostenstelleIIdStamm());
		personal.setBAnwesenheitsliste(personalDto.getBAnwesenheitsliste());
		personal.setBAnwesenheitTerminal(personalDto.getBAnwesenheitTerminal());
		personal.setBAnwesenheitalleterminal(personalDto
				.getBAnwesenheitalleterminal());
		personal.setCKurzzeichen(personalDto.getCKurzzeichen());
		personal.setPendlerpauschaleIId(personalDto.getPendlerpauschaleIId());
		personal.setCUnterschriftsfunktion(personalDto
				.getCUnterschriftsfunktion());
		personal.setCUnterschriftstext(personalDto.getCUnterschriftstext());

		personal.setXKommentar(personalDto.getXKommentar());
		personal.setBVersteckt(personalDto.getBVersteckt());
		personal.setPersonalfunktionCNr(personalDto.getPersonalfunktionCNr());
		personal.setPersonalIIdAnlegen(personalDto.getPersonalIIdAnlegen());
		personal.setPersonalIIdAendern(personalDto.getPersonalIIdAendern());

		personal.setCDirektfax(personalDto.getCDirektfax());
		personal.setCEmail(personalDto.getCEmail());
		personal.setCFax(personalDto.getCFax());
		personal.setCHandy(personalDto.getCHandy());
		personal.setCTelefon(personalDto.getCTelefon());

		personal.setPersonalgruppeIId(personalDto.getPersonalgruppeIId());

		personal.setCImapbenutzer(personalDto.getCImapbenutzer());
		personal.setCImapkennwort(personalDto.getCImapkennwort());
		personal.setBTelefonzeitstarten(personalDto.getBTelefonzeitstarten());

		personal.setCImapInboxFolder(personalDto.getCImapInboxFolder());
		em.merge(personal);
		em.flush();
	}

	protected PersonalDto assemblePersonalDto(Personal personal) {
		return PersonalDtoAssembler.createDto(personal);
	}

	private PersonalDto[] assemblePersonalDtos(Collection<?> personals,
			boolean bPlusVersteckte) {
		List<PersonalDto> list = new ArrayList<PersonalDto>();
		if (personals != null) {
			Iterator<?> iterator = personals.iterator();
			while (iterator.hasNext()) {
				Personal personal = (Personal) iterator.next();

				if (bPlusVersteckte == true) {
					list.add(assemblePersonalDto(personal));
				} else {
					if (Helper.short2boolean(personal.getBVersteckt()) == false) {
						list.add(assemblePersonalDto(personal));
					}
				}

			}
		}
		PersonalDto[] returnArray = new PersonalDto[list.size()];
		return (PersonalDto[]) list.toArray(returnArray);
	}

	/**
	 * Legt einen neue Lohngruppe an
	 * 
	 * @param lohngruppeDto
	 *            LohngruppeDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return Integer
	 */
	public Integer createLohngruppe(LohngruppeDto lohngruppeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (lohngruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lohngruppeDto == null"));
		}
		if (lohngruppeDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lohngruppeDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("LohngruppefindByCBez");
			query.setParameter(1, lohngruppeDto.getCBez());
			Lohngruppe doppelt = (Lohngruppe) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_LOHNGRUPPE.C_BEZ"));
		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LOHNGRUPPE);
		lohngruppeDto.setIId(pk);

		try {
			Lohngruppe lohngruppe = new Lohngruppe(lohngruppeDto.getIId(),
					lohngruppeDto.getCBez());
			em.persist(lohngruppe);
			em.flush();
			setLohngruppeFromLohngruppeDto(lohngruppe, lohngruppeDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return lohngruppeDto.getIId();
	}

	/**
	 * Entfernt eine vorhandene Lohngruppe
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 */
	public void removeLohngruppe(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			Lohngruppe lohngruppe = em.find(Lohngruppe.class, iId);
			if (lohngruppe == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(lohngruppe);
			em.flush();
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	/**
	 * Speichert &auml;nderungen einer Lohngruppe
	 * 
	 * @param lohngruppeDto
	 *            LohngruppeDto
	 * @throws EJBExceptionLP
	 */
	public void updateLohngruppe(LohngruppeDto lohngruppeDto)
			throws EJBExceptionLP {
		if (lohngruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lohngruppeDto == null"));
		}
		if (lohngruppeDto.getIId() == null || lohngruppeDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"lohngruppeDto.getIId() == null || lohngruppeDto.getCBez() == null"));
		}

		Integer iId = lohngruppeDto.getIId();
		// try {
		Lohngruppe lohngruppe = em.find(Lohngruppe.class, iId);
		if (lohngruppe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("LohngruppefindByCBez");
			query.setParameter(1, lohngruppeDto.getCBez());
			Integer iIdVorhanden = ((Lohngruppe) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_LOHNGRUPPE.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		}
		setLohngruppeFromLohngruppeDto(lohngruppe, lohngruppeDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	/**
	 * Holt eine bestimmte Lohngruppe
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return LohngruppeDto
	 */
	public LohngruppeDto lohngruppeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		try {
			Lohngruppe lohngruppe = em.find(Lohngruppe.class, iId);
			if (lohngruppe == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleLohngruppeDto(lohngruppe);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setLohngruppeFromLohngruppeDto(Lohngruppe lohngruppe,
			LohngruppeDto lohngruppeDto) {
		lohngruppe.setCBez(lohngruppeDto.getCBez());
		em.merge(lohngruppe);
		em.flush();
	}

	private LohngruppeDto assembleLohngruppeDto(Lohngruppe lohngruppe) {
		return LohngruppeDtoAssembler.createDto(lohngruppe);
	}

	private LohngruppeDto[] assembleLohngruppeDtos(Collection<?> lohngruppes) {
		List<LohngruppeDto> list = new ArrayList<LohngruppeDto>();
		if (lohngruppes != null) {
			Iterator<?> iterator = lohngruppes.iterator();
			while (iterator.hasNext()) {
				Lohngruppe lohngruppe = (Lohngruppe) iterator.next();
				list.add(assembleLohngruppeDto(lohngruppe));
			}
		}
		LohngruppeDto[] returnArray = new LohngruppeDto[list.size()];
		return (LohngruppeDto[]) list.toArray(returnArray);
	}

	public Integer createReligion(ReligionDto religionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (religionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("religionDto == null"));
		}
		if (religionDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("religionDto.getCNr() == null"));
		}
		try {
			Query query = em.createNamedQuery("ReligionfindByCNr");
			query.setParameter(1, religionDto.getCNr());
			Religion doppelt = (Religion) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_RELIGION.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_RELIGION);
			religionDto.setIId(pk);

			Religion religion = new Religion(religionDto.getIId(),
					religionDto.getCNr());
			em.persist(religion);
			em.flush();
			setReligionFromReligionDto(religion, religionDto);

			// flrsort: 4 wenn jeder haupteintrag einen spreintrag hat - null
			// gilt auch -
			// dann kommen beim sortieren der bezeichnung auch alle!
			// if (religionDto.getReligionsprDto() != null) {
			String sprache = theClientDto.getLocUiAsString();
			Religionspr religionspr = new Religionspr(sprache,
					religionDto.getIId());
			em.persist(religionspr);
			em.flush();
			setReligionsprFromReligionsprDto(religionspr,
					religionDto.getReligionsprDto());
			// }
			return religionDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeReligion(ReligionDto religionDto) throws EJBExceptionLP {
		if (religionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("religionDto == null"));
		}
		if (religionDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("religionDto.getIId() == null"));
		}
		try {
			// try {
			Query query = em.createNamedQuery("ReligionsprfindByReligionIId");
			query.setParameter(1, religionDto.getIId());
			Collection<?> allReligionspr = query.getResultList();
			Iterator<?> iter = allReligionspr.iterator();
			while (iter.hasNext()) {
				Religionspr religionsprTemp = (Religionspr) iter.next();
				em.remove(religionsprTemp);
			}
			// }
			// catch (RemoveException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEIM_LOESCHEN, ex);
			// }
			Religion religion = em.find(Religion.class, religionDto.getIId());
			if (religion == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(religion);
			em.flush();
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateReligion(ReligionDto religionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (religionDto != null) {
			Integer iId = religionDto.getIId();

			Religion religion = null;
			// try {
			religion = em.find(Religion.class, iId);
			if (religion == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

			}
			// }
			// catch (FinderException ex1) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEI_FINDBYPRIMARYKEY, ex1);

			// }

			try {
				Query query = em.createNamedQuery("ReligionfindByCNr");
				query.setParameter(1, religionDto.getCNr());
				Integer iIdVorhanden = ((Religion) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PERS_RELIGION.CNR"));
				}

			} catch (NoResultException ex) {
				//
			}

			setReligionFromReligionDto(religion, religionDto);

			if (religionDto.getReligionsprDto() != null) {
				// try {
				Religionspr religionspr = em
						.find(Religionspr.class,
								new ReligionsprPK(theClientDto
										.getLocUiAsString(), iId));
				if (religionspr == null) {
					religionspr = null;
					try {
						religionspr = new Religionspr(
								theClientDto.getLocUiAsString(), iId);
						em.persist(religionspr);
						em.flush();
					} catch (EntityExistsException ex3) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex3);
					}
					setReligionsprFromReligionsprDto(religionspr,
							religionDto.getReligionsprDto());
				}
				setReligionsprFromReligionsprDto(religionspr,
						religionDto.getReligionsprDto());
				// }
				// catch (FinderException ex) {
				// Religionspr religionspr = null;
				// try {
				// religionspr = new
				// Religionspr(getTheClient(idUser).getLocUiAsString(), iId);
				// em.persist(religionspr);
				// }
				// catch (CreateException ex3) {
				// throw new EJBExceptionLP(EJBExceptionLP.
				// FEHLER_BEIM_ANLEGEN, ex3);
				// }
				// setReligionsprFromReligionsprDto(religionspr,
				// religionDto.getReligionsprDto());
				// }
			}

		}
	}

	public ReligionDto religionFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Religion religion = em.find(Religion.class, iId);
		if (religion == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		ReligionDto religionDto = assembleReligionDto(religion);
		ReligionsprDto religionsprDto = null;
		// try {
		String sprache = theClientDto.getLocUiAsString();
		Religionspr religionspr = em.find(Religionspr.class, new ReligionsprPK(
				sprache, iId));
		if (religionspr != null) {
			religionsprDto = assembleReligionsprDto(religionspr);
		}
		// }
		// catch (FinderException ex) {
		// }
		if (religionsprDto == null) {
			// try {
			String konzernsprache = theClientDto.getLocKonzernAsString();
			Religionspr temp = em.find(Religionspr.class, new ReligionsprPK(
					konzernsprache, iId));
			if (temp != null) {
				religionsprDto = assembleReligionsprDto(temp);
			}

			// }
			// catch (FinderException ex) {
			// }
		}
		religionDto.setReligionsprDto(religionsprDto);
		return religionDto;
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setReligionFromReligionDto(Religion religion,
			ReligionDto religionDto) {
		religion.setCNr(religionDto.getCNr());
		em.merge(religion);
		em.flush();
	}

	private ReligionDto assembleReligionDto(Religion religion) {
		return ReligionDtoAssembler.createDto(religion);
	}

	private ReligionDto[] assembleReligionDtos(Collection<?> religions) {
		List<ReligionDto> list = new ArrayList<ReligionDto>();
		if (religions != null) {
			Iterator<?> iterator = religions.iterator();
			while (iterator.hasNext()) {
				Religion religion = (Religion) iterator.next();
				list.add(assembleReligionDto(religion));
			}
		}
		ReligionDto[] returnArray = new ReligionDto[list.size()];
		return (ReligionDto[]) list.toArray(returnArray);
	}

	private void setReligionsprFromReligionsprDto(Religionspr religionspr,
			ReligionsprDto religionsprDto) {
		religionspr.setCBez(religionsprDto.getCBez());
		em.merge(religionspr);
		em.flush();
	}

	private ReligionsprDto assembleReligionsprDto(Religionspr religionspr) {
		return ReligionsprDtoAssembler.createDto(religionspr);
	}

	private ReligionsprDto[] assembleReligionsprDtos(Collection<?> religionsprs) {
		List<ReligionsprDto> list = new ArrayList<ReligionsprDto>();
		if (religionsprs != null) {
			Iterator<?> iterator = religionsprs.iterator();
			while (iterator.hasNext()) {
				Religionspr religionspr = (Religionspr) iterator.next();
				list.add(assembleReligionsprDto(religionspr));
			}
		}
		ReligionsprDto[] returnArray = new ReligionsprDto[list.size()];
		return (ReligionsprDto[]) list.toArray(returnArray);
	}

	private void setPersonalartsprFromPersonalartsprDto(
			Personalartspr personalartspr, PersonalartsprDto personalartsprDto) {
		personalartspr.setCBez(personalartsprDto.getCBez());
		em.merge(personalartspr);
		em.flush();
	}

	private PersonalartsprDto assemblePersonalartsprDto(
			Personalartspr personalartspr) {
		return PersonalartsprDtoAssembler.createDto(personalartspr);
	}

	private PersonalartsprDto[] assemblePersonalartsprDtos(
			Collection<?> personalartsprs) {
		List<PersonalartsprDto> list = new ArrayList<PersonalartsprDto>();
		if (personalartsprs != null) {
			Iterator<?> iterator = personalartsprs.iterator();
			while (iterator.hasNext()) {
				Personalartspr personalartspr = (Personalartspr) iterator
						.next();
				list.add(assemblePersonalartsprDto(personalartspr));
			}
		}
		PersonalartsprDto[] returnArray = new PersonalartsprDto[list.size()];
		return (PersonalartsprDto[]) list.toArray(returnArray);
	}

	private void setPersonalartFromPersonalartDto(Personalart personalart,
			PersonalartDto personalartDto) {
		em.merge(personalart);
		em.flush();
	}

	private PersonalartDto assemblePersonalartDto(Personalart personalart) {
		return PersonalartDtoAssembler.createDto(personalart);
	}

	private PersonalartDto[] assemblePersonalartDtos(Collection<?> personalarts) {
		List<PersonalartDto> list = new ArrayList<PersonalartDto>();
		if (personalarts != null) {
			Iterator<?> iterator = personalarts.iterator();
			while (iterator.hasNext()) {
				Personalart personalart = (Personalart) iterator.next();
				list.add(assemblePersonalartDto(personalart));
			}
		}
		PersonalartDto[] returnArray = new PersonalartDto[list.size()];
		return (PersonalartDto[]) list.toArray(returnArray);
	}

	private void setAngehoerigenartFromAngehoerigenartDto(
			Angehoerigenart angehoerigenart,
			AngehoerigenartDto angehoerigenartDto) {
		angehoerigenart.setISort(angehoerigenartDto.getISort());
		em.merge(angehoerigenart);
		em.flush();
	}

	private AngehoerigenartDto assembleAngehoerigenartDto(
			Angehoerigenart angehoerigenart) {
		return AngehoerigenartDtoAssembler.createDto(angehoerigenart);
	}

	private AngehoerigenartDto[] assembleAngehoerigenartDtos(
			Collection<?> angehoerigenarts) {
		List<AngehoerigenartDto> list = new ArrayList<AngehoerigenartDto>();
		if (angehoerigenarts != null) {
			Iterator<?> iterator = angehoerigenarts.iterator();
			while (iterator.hasNext()) {
				Angehoerigenart angehoerigenart = (Angehoerigenart) iterator
						.next();
				list.add(assembleAngehoerigenartDto(angehoerigenart));
			}
		}
		AngehoerigenartDto[] returnArray = new AngehoerigenartDto[list.size()];
		return (AngehoerigenartDto[]) list.toArray(returnArray);
	}

	private void setFamilienstandFromFamilienstandDto(
			Familienstand familienstand, FamilienstandDto familienstandDto) {
		familienstand.setISort(familienstandDto.getISort());
		em.merge(familienstand);
		em.flush();
	}

	private FamilienstandDto assembleFamilienstandDto(
			Familienstand familienstand) {
		return FamilienstandDtoAssembler.createDto(familienstand);
	}

	private FamilienstandDto[] assembleFamilienstandDtos(
			Collection<?> familienstands) {
		List<FamilienstandDto> list = new ArrayList<FamilienstandDto>();
		if (familienstands != null) {
			Iterator<?> iterator = familienstands.iterator();
			while (iterator.hasNext()) {
				Familienstand familienstand = (Familienstand) iterator.next();
				list.add(assembleFamilienstandDto(familienstand));
			}
		}
		FamilienstandDto[] returnArray = new FamilienstandDto[list.size()];
		return (FamilienstandDto[]) list.toArray(returnArray);
	}

	public Integer createPersonalangehoerige(
			PersonalangehoerigeDto personalangehoerigeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (personalangehoerigeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("partnerkommunikationDto == null"));
		}
		if (personalangehoerigeDto.getPersonalIId() == null
				|| personalangehoerigeDto.getAngehoerigenartCNr() == null
				|| personalangehoerigeDto.getCVorname() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"personalangehoerigeDto.getPersonalPartnerIId() == null || personalangehoerigeDto.getAngehoerigenartCNr() == null || personalangehoerigeDto.getCVorname() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("PersonalangehoerigefindByPersonalIIdAngegoerigenartCNrCVorname");
			query.setParameter(1, personalangehoerigeDto.getPersonalIId());
			query.setParameter(2,
					personalangehoerigeDto.getAngehoerigenartCNr());
			query.setParameter(3, personalangehoerigeDto.getCVorname());
			Personalangehoerige doppelt = (Personalangehoerige) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_PERSONALANGEHOERIGE.UK"));
		} catch (NoResultException ex1) {
		}
		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj();

			Integer pk = pkGen
					.getNextPrimaryKey(PKConst.PK_PERSONALANGEHOERIGE);
			personalangehoerigeDto.setIId(pk);

			Personalangehoerige personalangehoerige = new Personalangehoerige(
					personalangehoerigeDto.getIId(),
					personalangehoerigeDto.getPersonalIId(),
					personalangehoerigeDto.getAngehoerigenartCNr(),
					personalangehoerigeDto.getCVorname());
			em.persist(personalangehoerige);
			em.flush();
			setPersonalangehoerigeFromPersonalangehoerigeDto(
					personalangehoerige, personalangehoerigeDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return personalangehoerigeDto.getIId();
	}

	public void removePersonalangehoerige(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			Personalangehoerige personalangehoerige = em.find(
					Personalangehoerige.class, iId);
			if (personalangehoerige == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(personalangehoerige);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updatePersonalangehoerige(
			PersonalangehoerigeDto personalangehoerigeDto)
			throws EJBExceptionLP {
		if (personalangehoerigeDto != null) {
			Integer iId = personalangehoerigeDto.getIId();
			// try {
			Personalangehoerige personalangehoerige = em.find(
					Personalangehoerige.class, iId);
			if (personalangehoerige == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPersonalangehoerigeFromPersonalangehoerigeDto(
					personalangehoerige, personalangehoerigeDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public PersonalangehoerigeDto personalangehoerigeFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Personalangehoerige personalangehoerige = em.find(
				Personalangehoerige.class, iId);
		if (personalangehoerige == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		PersonalangehoerigeDto personalangehoerigeDto = assemblePersonalangehoerigeDto(personalangehoerige);

		return personalangehoerigeDto;
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setPersonalangehoerigeFromPersonalangehoerigeDto(
			Personalangehoerige personalangehoerige,
			PersonalangehoerigeDto personalangehoerigeDto) {
		personalangehoerige.setPersonalIId(personalangehoerigeDto
				.getPersonalIId());
		personalangehoerige.setAngehoerigenartCNr(personalangehoerigeDto
				.getAngehoerigenartCNr());
		personalangehoerige.setCVorname(personalangehoerigeDto.getCVorname());
		personalangehoerige.setCName(personalangehoerigeDto.getCName());
		personalangehoerige.setTGeburtsdatum(personalangehoerigeDto
				.getTGeburtsdatum());
		personalangehoerige.setCSozialversnr(personalangehoerigeDto
				.getCSozialversnr());
		em.merge(personalangehoerige);
		em.flush();
	}

	private PersonalangehoerigeDto assemblePersonalangehoerigeDto(
			Personalangehoerige personalangehoerige) {
		return PersonalangehoerigeDtoAssembler.createDto(personalangehoerige);
	}

	private PersonalangehoerigeDto[] assemblePersonalangehoerigeDtos(
			Collection<?> personalangehoeriges) {
		List<PersonalangehoerigeDto> list = new ArrayList<PersonalangehoerigeDto>();
		if (personalangehoeriges != null) {
			Iterator<?> iterator = personalangehoeriges.iterator();
			while (iterator.hasNext()) {
				Personalangehoerige personalangehoerige = (Personalangehoerige) iterator
						.next();
				list.add(assemblePersonalangehoerigeDto(personalangehoerige));
			}
		}
		PersonalangehoerigeDto[] returnArray = new PersonalangehoerigeDto[list
				.size()];
		return (PersonalangehoerigeDto[]) list.toArray(returnArray);
	}

	public Integer createKollektiv(KollektivDto kollektivDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (kollektivDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kollektivDto == null"));
		}
		if (kollektivDto.getCBez() == null
				|| kollektivDto.getBUestdverteilen() == null
				|| kollektivDto.getBVerbraucheuestd() == null
				|| kollektivDto.getBUestdabsollstderbracht() == null
				|| kollektivDto.getNFaktoruestd100() == null
				|| kollektivDto.getNFaktoruestd50() == null
				|| kollektivDto.getNFaktormehrstd() == null
				|| kollektivDto.getNFaktoruestd200() == null
				|| kollektivDto.getN200prozentigeab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"kollektivDto.getCBez() == null || kollektivDto.getBUestdverteilen() == null || kollektivDto.getBUestdabsollstderbracht() == null || kollektivDto.getBVerbraucheuestd() == null || kollektivDto.getNFaktoruestd100() == null || kollektivDto.getNFaktoruestd50() == null || kollektivDto.getNFaktormehrstd() == null || kollektivDto.getNFaktoruestd200() == null || kollektivDto.getN200prozentigeab() == null"));
		}
		try {
			Query query = em.createNamedQuery("KollektivfindByCBez");
			query.setParameter(1, kollektivDto.getCBez());
			Kollektiv doppelt = (Kollektiv) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_KOLLEKTIV.C_BEZ"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KOLLEKTIV);
		kollektivDto.setIId(pk);

		try {
			Kollektiv kollektiv = new Kollektiv(kollektivDto.getIId(),
					kollektivDto.getCBez(), kollektivDto.getBVerbraucheuestd(),
					kollektivDto.getNFaktoruestd50(),
					kollektivDto.getNFaktoruestd100(),
					kollektivDto.getNFaktormehrstd(),
					kollektivDto.getBUestdabsollstderbracht(),
					kollektivDto.getBUestdverteilen(),
					kollektivDto.getNFaktoruestd200(),
					kollektivDto.getN200prozentigeab());
			em.persist(kollektiv);
			em.flush();
			setKollektivFromKollektivDto(kollektiv, kollektivDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return kollektivDto.getIId();
	}

	public void removeKollektiv(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			Kollektiv kollektiv = em.find(Kollektiv.class, iId);
			if (kollektiv == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(kollektiv);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeLohnart(Integer iId) {
		try {
			Lohnart lohnart = em.find(Lohnart.class, iId);
			if (lohnart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(lohnart);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeLohnartstundenfaktor(Integer iId) {
		try {
			Lohnartstundenfaktor lohnartstundenfaktor = em.find(
					Lohnartstundenfaktor.class, iId);
			if (lohnartstundenfaktor == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(lohnartstundenfaktor);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeLohnstundenart(String cNr) {
		try {
			Lohnart lohnart = em.find(Lohnart.class, cNr);
			if (lohnart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(lohnart);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateKollektiv(KollektivDto kollektivDto)
			throws EJBExceptionLP {
		if (kollektivDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kollektivDto == null"));
		}
		if (kollektivDto.getIId() == null || kollektivDto.getCBez() == null
				|| kollektivDto.getNFaktoruestd100() == null
				|| kollektivDto.getBUestdabsollstderbracht() == null
				|| kollektivDto.getBUestdverteilen() == null
				|| kollektivDto.getNFaktoruestd50() == null
				|| kollektivDto.getNFaktormehrstd() == null
				|| kollektivDto.getNFaktoruestd200() == null
				|| kollektivDto.getN200prozentigeab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"kollektivDto.getIId() == null || kollektivDto.getBUestdverteilen() == null || kollektivDto.getBUestdabsollstderbracht() == null || kollektivDto.getCBez() == null || kollektivDto.getNFaktoruestd100() == null || kollektivDto.getNFaktoruestd50() == null || kollektivDto.getNFaktormehrstd() == null || kollektivDto.getNFaktoruestd200() == null || kollektivDto.getN200prozentigeab() == null"));
		}

		Integer iId = kollektivDto.getIId();
		// try {
		Kollektiv kollektiv = em.find(Kollektiv.class, iId);
		if (kollektiv == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("KollektivfindByCBez");
			query.setParameter(1, kollektivDto.getCBez());
			Integer iIdVorhanden = ((Kollektiv) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_KOLLEKTIV.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		}
		setKollektivFromKollektivDto(kollektiv, kollektivDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public void updateLohnart(LohnartDto lohnartDto) {

		Integer iId = lohnartDto.getIId();
		// try {
		Lohnart kollektiv = em.find(Lohnart.class, iId);
		if (kollektiv == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("LohnartfindByILohnart");
			query.setParameter(1, lohnartDto.getILohnart());
			Integer iIdVorhanden = ((Lohnart) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_KOLLEKTIV.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		}
		setLohnartFromLohnartDto(kollektiv, lohnartDto);

	}

	public void updateLohnartstundenfaktor(
			LohnartstundenfaktorDto lohnartstundenfaktorDto) {

		Integer iId = lohnartstundenfaktorDto.getIId();
		// try {
		Lohnartstundenfaktor lohnartstundenfaktor = em.find(
				Lohnartstundenfaktor.class, iId);
		if (lohnartstundenfaktor == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em
					.createNamedQuery("LohnartstundenfaktorfindByLohnartIIdLohnstundenartCNrTagesartIId");
			query.setParameter(1, lohnartstundenfaktor.getLohnartIId());
			query.setParameter(2, lohnartstundenfaktor.getLohnstundenartCNr());
			query.setParameter(3, lohnartstundenfaktor.getTagesartIId());
			Integer iIdVorhanden = ((Lohnartstundenfaktor) query
					.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_LOHNARTSTUNDENFAKTOR.UK"));
			}

		} catch (NoResultException ex) {
			//
		}
		setLohnartstundenfaktorFromLohnartstundenfaktorDto(
				lohnartstundenfaktor, lohnartstundenfaktorDto);

	}

	public void updateLohnstundenart(LohnstundenartDto lohnstundenartDto) {

		String cNr = lohnstundenartDto.getCNr();
		// try {
		Lohnstundenart lohnstundenart = em.find(Lohnstundenart.class, cNr);
		if (lohnstundenart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		setLohnstundenartFromLohnstundenartDto(lohnstundenart,
				lohnstundenartDto);

	}

	public Map getAllSprFamilienstaende(String cNrSpracheI)
			throws EJBExceptionLP {
		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();
		// try {
		Query query = em.createNamedQuery("FamilienstandfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Familienstand familienstandTemp = (Familienstand) itArten.next();
			Object key = familienstandTemp.getCNr();
			Object value = null;
			// try {
			Familienstandspr familienstandspr = em.find(
					Familienstandspr.class,
					new FamilienstandsprPK(cNrSpracheI, familienstandTemp
							.getCNr()));
			if (familienstandspr == null) {
				// fuer locale und C_NR keine Bezeichnu g vorhanden ...
				value = familienstandTemp.getCNr();
			} else {
				value = familienstandspr.getCBez();
			}
			// }
			// catch (FinderException ex1) {
			// fuer locale und C_NR keine Bezeichnu g vorhanden ...
			// value = familienstandTemp.getCNr();
			// }
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

		return tmArten;

	}

	public HashMap getAllKurzzeichen() throws EJBExceptionLP {

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRPersonal.class);
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		HashMap<Integer, String> h = new HashMap<Integer, String>();

		while (resultListIterator.hasNext()) {
			FLRPersonal fehlmenge = (FLRPersonal) resultListIterator.next();
			h.put(fehlmenge.getI_id(), fehlmenge.getC_kurzzeichen());
		}
		session.close();
		return h;
	}

	public Map getAllSprangehoerigenarten(String cNrSpracheI)
			throws EJBExceptionLP {
		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();
		// try {
		Query query = em.createNamedQuery("AngehoerigenartfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		Iterator<?> itArten = clArten.iterator();

		while (itArten.hasNext()) {
			Angehoerigenart angehoerigenartTemp = (Angehoerigenart) itArten
					.next();
			Object key = angehoerigenartTemp.getCNr();
			Object value = null;
			// try {
			Angehoerigenartspr angehoerigenartspr = em.find(
					Angehoerigenartspr.class, new AngehoerigenartsprPK(
							cNrSpracheI, angehoerigenartTemp.getCNr()));
			if (angehoerigenartspr == null) {
				// fuer locale und C_NR keine Bezeichnug vorhanden ...
				value = angehoerigenartTemp.getCNr();
			} else {
				value = angehoerigenartspr.getCBez();
			}
			// }
			// catch (FinderException ex1) {
			// fuer locale und C_NR keine Bezeichnug vorhanden ...
			// value = angehoerigenartTemp.getCNr();
			// }
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }
		return tmArten;
	}

	/**
	 * uselocalspr: 0 Hole alle Personalarten nach Spr.
	 * 
	 * @param cNrSpracheI
	 *            String
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllSprPersonalarten(String cNrSpracheI) throws EJBExceptionLP {
		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();
		// try {
		Query query = em.createNamedQuery("PersonalartfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty() ) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Personalart personalartTemp = (Personalart) itArten.next();
			Object key = personalartTemp.getCNr();
			Object value = null;
			// try {
			Personalartspr personalartspr = em
					.find(Personalartspr.class, new PersonalartsprPK(
							cNrSpracheI, personalartTemp.getCNr()));
			if (personalartspr == null) {
				// fuer locale und C_NR keine Bezeichnu g vorhanden ...
				value = personalartTemp.getCNr();
			} else {
				value = personalartspr.getCBez();
			}
			// }
			// catch (FinderException ex1) {
			// fuer locale und C_NR keine Bezeichnu g vorhanden ...
			// value = personalartTemp.getCNr();
			// }
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

		return tmArten;
	}

	public PersonalDto[] getAllPersonenOhneEintragInEintrittAustritt(
			TheClientDto theClientDto, Boolean bPlusVersteckte, Integer iOption)
			throws EJBExceptionLP {
		PersonalDto[] personalDtos = null;

		if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
			personalDtos = personalFindByMandantCNr(theClientDto.getMandant(),
					bPlusVersteckte);
		} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
			personalDtos = personalFindAllArbeiterEinesMandanten(
					theClientDto.getMandant(), bPlusVersteckte);
		} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
			personalDtos = personalFindAllAngestellteEinesMandanten(
					theClientDto.getMandant(), bPlusVersteckte);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"OPTION NICHT VERFUEGBAR"));
		}

		for (int i = 0; i < personalDtos.length; i++) {

		}
		List<PersonalDto> list = new ArrayList<PersonalDto>();

		for (int i = 0; i < personalDtos.length; i++) {
			// try {
			Query query = em
					.createNamedQuery("EintrittaustrittfindByPersonalIId");
			query.setParameter(1, personalDtos[i].getIId());
			Collection<?> cl = query.getResultList();
			// if (! cl.isEmpty()) {
			if (assembleEintrittaustrittDtos(cl).length < 1) {
				list.add(personalFindByPrimaryKey(personalDtos[i].getIId(),
						theClientDto));
			}
			// }
			// catch (FinderException ex) {
			// nix da
			// }
		}

		PersonalDto[] returnArray = new PersonalDto[list.size()];
		return (PersonalDto[]) list.toArray(returnArray);
	}

	/**
	 * uselocalspr: 0 Hole alle Personalfunktionen nach Spr.
	 * 
	 * @param cNrSpracheI
	 *            String
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllSprPersonalfunktionen(String cNrSpracheI)
			throws EJBExceptionLP {
		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();
		// try {
		Query query = em.createNamedQuery("PersonalfunktionfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Personalfunktion personalfunktionTemp = (Personalfunktion) itArten
					.next();
			Object key = personalfunktionTemp.getCNr();
			Object value = null;
			// try {
			Personalfunktionspr personalfunktionspr = em.find(
					Personalfunktionspr.class, new PersonalfunktionsprPK(
							cNrSpracheI, personalfunktionTemp.getCNr()));
			if (personalfunktionspr == null) {
				// fuer locale und C_NR keine Bezeichnug vorhanden ...
				value = personalfunktionTemp.getCNr();
			} else {
				value = personalfunktionspr.getCBez();
			}
			// }
			// catch (FinderException ex1) {
			// fuer locale und C_NR keine Bezeichnug vorhanden ...
			// value = personalfunktionTemp.getCNr();
			// }
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

		return tmArten;
	}

	public KollektivDto kollektivFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Kollektiv kollektiv = em.find(Kollektiv.class, iId);
		if (kollektiv == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKollektivDto(kollektiv);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setKollektivFromKollektivDto(Kollektiv kollektiv,
			KollektivDto kollektivDto) {
		kollektiv.setCBez(kollektivDto.getCBez());
		kollektiv.setBVerbraucheuestdt(kollektivDto.getBVerbraucheuestd());
		kollektiv.setBUestdabsollstderbracht(kollektivDto
				.getBUestdabsollstderbracht());
		kollektiv.setNNormalstunden(kollektivDto.getNNormalstunden());
		kollektiv.setNFaktoruestd100(kollektivDto.getNFaktoruestd100());
		kollektiv.setNFaktoruestd50(kollektivDto.getNFaktoruestd50());
		kollektiv.setNFaktormehrstd(kollektivDto.getNFaktormehrstd());
		kollektiv.setUBlockzeitab(kollektivDto.getUBlockzeitab());
		kollektiv.setUBlockzeitbis(kollektivDto.getUBlockzeitbis());
		kollektiv.setBUestdverteilen(kollektivDto.getBUestdverteilen());
		kollektiv.setNFaktoruestd200(kollektivDto.getNFaktoruestd200());
		kollektiv.setN200prozentigeab(kollektivDto.getN200prozentigeab());

		em.merge(kollektiv);
		em.flush();
	}

	private KollektivDto assembleKollektivDto(Kollektiv kollektiv) {
		return KollektivDtoAssembler.createDto(kollektiv);
	}

	private KollektivDto[] assembleKollektivDtos(Collection<?> kollektivs) {
		List<KollektivDto> list = new ArrayList<KollektivDto>();
		if (kollektivs != null) {
			Iterator<?> iterator = kollektivs.iterator();
			while (iterator.hasNext()) {
				Kollektiv kollektiv = (Kollektiv) iterator.next();
				list.add(assembleKollektivDto(kollektiv));
			}
		}
		KollektivDto[] returnArray = new KollektivDto[list.size()];
		return (KollektivDto[]) list.toArray(returnArray);
	}

	public Integer createPendlerpauschale(
			PendlerpauschaleDto pendlerpauschaleDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (pendlerpauschaleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("pendlerpauschaleDto == null"));
		}
		if (pendlerpauschaleDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("pendlerpauschaleDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("PendlerpauschalefindByCBez");
			query.setParameter(1, pendlerpauschaleDto.getCBez());
			Pendlerpauschale doppelt = (Pendlerpauschale) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_PENDLERPAUSCHALE.C_BEZ"));
		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PENDLERPAUSCHALE);
		pendlerpauschaleDto.setIId(pk);

		try {
			Pendlerpauschale pendlerpauschale = new Pendlerpauschale(
					pendlerpauschaleDto.getIId(), pendlerpauschaleDto.getCBez());
			em.persist(pendlerpauschale);
			em.flush();
			setPendlerpauschaleFromPendlerpauschaleDto(pendlerpauschale,
					pendlerpauschaleDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return pendlerpauschaleDto.getIId();
	}

	public void removePendlerpauschale(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			Pendlerpauschale pendlerpauschale = em.find(Pendlerpauschale.class,
					iId);
			if (pendlerpauschale == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(pendlerpauschale);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updatePendlerpauschale(PendlerpauschaleDto pendlerpauschaleDto)
			throws EJBExceptionLP {
		if (pendlerpauschaleDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("pendlerpauschaleDto == null"));
		}
		if (pendlerpauschaleDto.getIId() == null
				|| pendlerpauschaleDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"pendlerpauschaleDto.getIId() == null || pendlerpauschaleDto.getCBez() == null"));
		}

		Integer iId = pendlerpauschaleDto.getIId();
		// try {
		Pendlerpauschale pendlerpauschale = em
				.find(Pendlerpauschale.class, iId);
		if (pendlerpauschale == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		try {
			Query query = em.createNamedQuery("PendlerpauschalefindByCBez");
			query.setParameter(1, pendlerpauschaleDto.getCBez());
			Integer iIdVorhanden = ((Pendlerpauschale) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PENDLERPAUSCHALE.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		}
		setPendlerpauschaleFromPendlerpauschaleDto(pendlerpauschale,
				pendlerpauschaleDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }

	}

	public PendlerpauschaleDto pendlerpauschaleFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Pendlerpauschale pendlerpauschale = em
				.find(Pendlerpauschale.class, iId);
		if (pendlerpauschale == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePendlerpauschaleDto(pendlerpauschale);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	private void setPendlerpauschaleFromPendlerpauschaleDto(
			Pendlerpauschale pendlerpauschale,
			PendlerpauschaleDto pendlerpauschaleDto) {
		pendlerpauschale.setCBez(pendlerpauschaleDto.getCBez());
		em.merge(pendlerpauschale);
		em.flush();
	}

	private PendlerpauschaleDto assemblePendlerpauschaleDto(
			Pendlerpauschale pendlerpauschale) {
		return PendlerpauschaleDtoAssembler.createDto(pendlerpauschale);
	}

	private PendlerpauschaleDto[] assemblePendlerpauschaleDtos(
			Collection<?> pendlerpauschales) {
		List<PendlerpauschaleDto> list = new ArrayList<PendlerpauschaleDto>();
		if (pendlerpauschales != null) {
			Iterator<?> iterator = pendlerpauschales.iterator();
			while (iterator.hasNext()) {
				Pendlerpauschale pendlerpauschale = (Pendlerpauschale) iterator
						.next();
				list.add(assemblePendlerpauschaleDto(pendlerpauschale));
			}
		}
		PendlerpauschaleDto[] returnArray = new PendlerpauschaleDto[list.size()];
		return (PendlerpauschaleDto[]) list.toArray(returnArray);
	}

	private void setFamilienstandsprFromFamilienstandsprDto(
			Familienstandspr familienstandspr,
			FamilienstandsprDto familienstandsprDto) {
		familienstandspr.setCBez(familienstandsprDto.getCBez());
		em.merge(familienstandspr);
		em.flush();
	}

	private FamilienstandsprDto assembleFamilienstandsprDto(
			Familienstandspr familienstandspr) {
		return FamilienstandsprDtoAssembler.createDto(familienstandspr);
	}

	private FamilienstandsprDto[] assembleFamilienstandsprDtos(
			Collection<?> familienstandsprs) {
		List<FamilienstandsprDto> list = new ArrayList<FamilienstandsprDto>();
		if (familienstandsprs != null) {
			Iterator<?> iterator = familienstandsprs.iterator();
			while (iterator.hasNext()) {
				Familienstandspr familienstandspr = (Familienstandspr) iterator
						.next();
				list.add(assembleFamilienstandsprDto(familienstandspr));
			}
		}
		FamilienstandsprDto[] returnArray = new FamilienstandsprDto[list.size()];
		return (FamilienstandsprDto[]) list.toArray(returnArray);
	}

	public Integer createBeruf(BerufDto berufDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (berufDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("berufDto == null"));
		}
		if (berufDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("berufDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("BeruffindByCBez");
			query.setParameter(1, berufDto.getCBez());
			Beruf doppelt = (Beruf) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_BERUF.C_BEZ"));
		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BERUF);
		berufDto.setIId(pk);

		try {
			Beruf beruf = new Beruf(berufDto.getIId(), berufDto.getCBez());
			em.persist(beruf);
			em.flush();
			setBerufFromBerufDto(beruf, berufDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return berufDto.getIId();
	}

	public Map getAllLohnstundenarten() {
		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();
		// try {
		Query query = em.createNamedQuery("LohnstundenartfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Lohnstundenart artikelartTemp = (Lohnstundenart) itArten.next();
			Object key = artikelartTemp.getCNr();
			Object value = null;
			if (artikelartTemp.getCBez() == null) {
				value = artikelartTemp.getCNr();
			} else {
				value = artikelartTemp.getCBez();
			}

			// }
			// catch (FinderException ex1) {
			// fuer locale und C_NR keine Bezeichnu g vorhanden ...
			// value = artikelartTemp.getCNr();
			// }
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }
		return tmArten;
	}

	public Integer createLohnart(LohnartDto lohnartDto) {

		try {
			Query query = em.createNamedQuery("LohnartfindByILohnart");
			query.setParameter(1, lohnartDto.getILohnart());
			Lohnart doppelt = (Lohnart) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_LOHNART.I_LOHNART"));
		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LOHNART);
		lohnartDto.setIId(pk);

		try {
			Lohnart lohnart = new Lohnart(lohnartDto.getIId(),
					lohnartDto.getILohnart(), lohnartDto.getCBez(),
					lohnartDto.getCTyp(), lohnartDto.getTaetigkeitIIdNl());
			em.persist(lohnart);
			em.flush();
			setLohnartFromLohnartDto(lohnart, lohnartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return lohnartDto.getIId();
	}

	public Integer createLohnartstundenfaktor(
			LohnartstundenfaktorDto lohnartstundenfaktorDto) {

		try {
			Query query = em
					.createNamedQuery("LohnartstundenfaktorfindByLohnartIIdLohnstundenartCNrTagesartIId");
			query.setParameter(1, lohnartstundenfaktorDto.getLohnartIId());
			query.setParameter(2,
					lohnartstundenfaktorDto.getLohnstundenartCNr());
			query.setParameter(3, lohnartstundenfaktorDto.getTagesartIId());
			Lohnartstundenfaktor doppelt = (Lohnartstundenfaktor) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_LOHNARTSTUNDENFAKTOR.UK"));
		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LOHNARTSTUNDENFAKTOR);
		lohnartstundenfaktorDto.setIId(pk);

		try {
			Lohnartstundenfaktor lohnart = new Lohnartstundenfaktor(
					lohnartstundenfaktorDto.getIId(),
					lohnartstundenfaktorDto.getLohnartIId(),
					lohnartstundenfaktorDto.getLohnstundenartCNr(),
					lohnartstundenfaktorDto.getFFaktor());
			em.persist(lohnart);
			em.flush();
			setLohnartstundenfaktorFromLohnartstundenfaktorDto(lohnart,
					lohnartstundenfaktorDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return lohnartstundenfaktorDto.getIId();
	}

	public void createLohnstundenart(LohnstundenartDto lohnstundenartDto) {

		try {
			Lohnstundenart ls = em.find(Lohnstundenart.class,
					lohnstundenartDto.getCNr());
			if (ls != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_LOHNSTUNDENART.UK"));
			}
		} catch (NoResultException ex1) {
		}

		try {
			Lohnstundenart lohnstundenart = new Lohnstundenart(
					lohnstundenartDto.getCNr(), lohnstundenartDto.getCBez());
			em.persist(lohnstundenart);
			em.flush();
			setLohnstundenartFromLohnstundenartDto(lohnstundenart,
					lohnstundenartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}

	}

	public void removeBeruf(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			Beruf beruf = em.find(Beruf.class, iId);
			if (beruf == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(beruf);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateBeruf(BerufDto berufDto) throws EJBExceptionLP {
		if (berufDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("berufDto == null"));
		}
		if (berufDto.getIId() == null || berufDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"berufDto.getIId() == null || berufDto.getCBez() == null"));
		}

		Integer iId = berufDto.getIId();
		// try {
		Beruf beruf = em.find(Beruf.class, iId);
		if (beruf == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("BeruffindByCBez");
			query.setParameter(1, berufDto.getCBez());
			Integer iIdVorhanden = ((Beruf) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_BERUF.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		}
		setBerufFromBerufDto(beruf, berufDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public BerufDto berufFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Beruf beruf = em.find(Beruf.class, iId);
		if (beruf == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBerufDto(beruf);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public LohnartDto lohnartFindByPrimaryKey(Integer iId) {

		Lohnart lohnart = em.find(Lohnart.class, iId);
		if (lohnart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLohnartDto(lohnart);
	}

	public LohnartstundenfaktorDto lohnartstundenfaktorFindByPrimaryKey(
			Integer iId) {

		Lohnartstundenfaktor lohnartstundenfaktor = em.find(
				Lohnartstundenfaktor.class, iId);
		if (lohnartstundenfaktor == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLohnartstundenfaktorDto(lohnartstundenfaktor);
	}

	public LohnstundenartDto lohnstundenartFindByPrimaryKey(Integer iId) {

		Lohnstundenart lohnstundenart = em.find(Lohnstundenart.class, iId);
		if (lohnstundenart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLohnstundenartDto(lohnstundenart);
	}

	private void setBerufFromBerufDto(Beruf beruf, BerufDto berufDto) {
		beruf.setCBez(berufDto.getCBez());
		em.merge(beruf);
		em.flush();
	}

	private void setLohnartFromLohnartDto(Lohnart lohnart, LohnartDto lohnartDto) {
		lohnart.setCBez(lohnartDto.getCBez());
		lohnart.setCFormel(lohnartDto.getCBez());
		lohnart.setCKommentar(lohnartDto.getcKommentar());
		lohnart.setCTyp(lohnartDto.getCTyp());
		lohnart.setILohnart(lohnartDto.getILohnart());
		lohnart.setPersonalartCNr(lohnartDto.getPersonalartCNr());
		lohnart.setTaetigkeitIIdNl(lohnartDto.getTaetigkeitIIdNl());
		em.merge(lohnart);
		em.flush();
	}

	private void setLohnartstundenfaktorFromLohnartstundenfaktorDto(
			Lohnartstundenfaktor lohnartstundenfaktor,
			LohnartstundenfaktorDto lohnartstundenfaktorDto) {
		lohnartstundenfaktor.setFFaktor(lohnartstundenfaktorDto.getFFaktor());
		lohnartstundenfaktor.setLohnartIId(lohnartstundenfaktorDto
				.getLohnartIId());
		lohnartstundenfaktor.setLohnstundenartCNr(lohnartstundenfaktorDto
				.getLohnstundenartCNr());
		lohnartstundenfaktor.setTagesartIId(lohnartstundenfaktorDto
				.getTagesartIId());
		em.merge(lohnartstundenfaktor);
		em.flush();
	}

	private void setLohnstundenartFromLohnstundenartDto(
			Lohnstundenart lohnstundenart, LohnstundenartDto lohnstundenartDto) {
		lohnstundenart.setCBez(lohnstundenartDto.getCBez());
		lohnstundenart.setCNr(lohnstundenartDto.getCNr());

		em.merge(lohnstundenart);
		em.flush();
	}

	private BerufDto assembleBerufDto(Beruf beruf) {
		return BerufDtoAssembler.createDto(beruf);
	}

	private LohnstundenartDto assembleLohnstundenartDto(
			Lohnstundenart lohnstundenart) {
		return LohnstundenartDtoAssembler.createDto(lohnstundenart);
	}

	private LohnartDto assembleLohnartDto(Lohnart lohnart) {
		return LohnartDtoAssembler.createDto(lohnart);
	}

	private LohnartstundenfaktorDto assembleLohnartstundenfaktorDto(
			Lohnartstundenfaktor lohnartstundenfaktor) {
		return LohnartstundenfaktorDtoAssembler.createDto(lohnartstundenfaktor);
	}

	private BerufDto[] assembleBerufDtos(Collection<?> berufs) {
		List<BerufDto> list = new ArrayList<BerufDto>();
		if (berufs != null) {
			Iterator<?> iterator = berufs.iterator();
			while (iterator.hasNext()) {
				Beruf beruf = (Beruf) iterator.next();
				list.add(assembleBerufDto(beruf));
			}
		}
		BerufDto[] returnArray = new BerufDto[list.size()];
		return (BerufDto[]) list.toArray(returnArray);
	}

	public Integer createEintrittaustritt(
			EintrittaustrittDto eintrittaustrittDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (eintrittaustrittDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("eintrittaustrittDto == null"));
		}
		if (eintrittaustrittDto.getPersonalIId() == null
				|| eintrittaustrittDto.getTEintritt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"eintrittaustrittDto.getPersonalIId() == null || eintrittaustrittDto.getDEintritt() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("EintrittaustrittfindByPersonalIIdTEintritt");
			query.setParameter(1, eintrittaustrittDto.getPersonalIId());
			query.setParameter(2, eintrittaustrittDto.getTEintritt());
			Eintrittaustritt doppelt = (Eintrittaustritt) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_EINTRITTAUSTRITT.UK"));
		} catch (NoResultException ex1) {
		}
		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_EINTRITTAUSTRITT);
			eintrittaustrittDto.setIId(pk);

			Eintrittaustritt eintrittaustritt = new Eintrittaustritt(
					eintrittaustrittDto.getIId(),
					eintrittaustrittDto.getPersonalIId(),
					eintrittaustrittDto.getTEintritt());
			em.persist(eintrittaustritt);
			em.flush();
			setEintrittaustrittFromEintrittaustrittDto(eintrittaustritt,
					eintrittaustrittDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return eintrittaustrittDto.getIId();
	}

	public void removeEintrittaustritt(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			Eintrittaustritt eintrittaustritt = em.find(Eintrittaustritt.class,
					iId);
			if (eintrittaustritt == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(eintrittaustritt);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateEintrittaustritt(EintrittaustrittDto eintrittaustrittDto)
			throws EJBExceptionLP {
		if (eintrittaustrittDto != null) {
			Integer iId = eintrittaustrittDto.getIId();
			// try {
			Eintrittaustritt eintrittaustritt = em.find(Eintrittaustritt.class,
					iId);
			if (eintrittaustritt == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setEintrittaustrittFromEintrittaustrittDto(eintrittaustritt,
					eintrittaustrittDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public PersonRpt getPersonRpt(Integer personalIId, TheClientDto theClientDto) {

		PersonalDto personalDto = personalFindByPrimaryKey(personalIId,
				theClientDto);

		Integer partnerIId_Mandant = null;
		;
		try {
			partnerIId_Mandant = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto).getPartnerIId();
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		PersonRpt personRpt = new PersonRpt();
		personRpt.setPersonalIId(personalIId);
		personRpt.setSTitel(personalDto.getPartnerDto().getCTitel());
		personRpt.setSNtitel(personalDto.getPartnerDto().getCNtitel());
		personRpt.setSVorname(personalDto.getPartnerDto()
				.getCName2vornamefirmazeile2());
		personRpt.setSNachname(personalDto.getPartnerDto()
				.getCName1nachnamefirmazeile1());

		personRpt.setSEmail(personalDto.getCEmail());

		personRpt.setSDirektfax(personalDto.getCDirektfax());

		if (personalDto.getCTelefon() != null) {
			personRpt.setSTelefonDWFirma(personalDto.getCTelefon());

			try {
				String kommDto = getPartnerFac().enrichNumber(
						partnerIId_Mandant,
						PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto,
						null, true);
				String sNummerMitDurchwahl = kommDto + " - "
						+ personalDto.getCTelefon();
				personRpt
						.setSTelefonFirmaMitDurchwahlBearbeiter(sNummerMitDurchwahl);
			} catch (Exception e) {
				// Gibt halt keine Nummer
			}
		}

		if (personalDto.getCFax() != null) {
			personRpt.setSFaxDWFirma(personalDto.getCFax());

			try {
				String kommDto = getPartnerFac().enrichNumber(
						partnerIId_Mandant, PartnerFac.KOMMUNIKATIONSART_FAX,
						theClientDto, null, true);
				String sNummerMitDurchwahl = kommDto + " - "
						+ personalDto.getCFax();
				personRpt
						.setSFaxFirmaMitDurchwahlBearbeiter(sNummerMitDurchwahl);
			} catch (Exception e) {
				// Gibt halt keine Nummer
			}
		}

		personRpt.setSMobil(personalDto.getCHandy());

		// Firmentelefon = Telefonnnummer des Mandanten
		try {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			personRpt
					.setSTelefonFirma(mandantDto.getPartnerDto().getCTelefon());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return personRpt;
	}

	public EintrittaustrittDto eintrittaustrittFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Eintrittaustritt eintrittaustritt = em
				.find(Eintrittaustritt.class, iId);
		if (eintrittaustritt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		EintrittaustrittDto eintrittaustrittDto = assembleEintrittaustrittDto(eintrittaustritt);

		return eintrittaustrittDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public Boolean istPersonalAusgetreten(Integer personalIId,
			java.sql.Timestamp tsZeitpunkt, TheClientDto theClientDto) {
		if (personalIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalIId == null"));
		}
		boolean bAusgetreten = true;
		// try {
		Query query = em
				.createNamedQuery("EintrittaustrittfindLetztenEintrittBisDatum");
		query.setParameter(1, personalIId);
		query.setParameter(2, tsZeitpunkt);

		EintrittaustrittDto[] eintrittaustrittDtos = assembleEintrittaustrittDtos(query
				.getResultList());

		if (eintrittaustrittDtos.length > 0) {
			EintrittaustrittDto eintrittaustrittDto = eintrittaustrittDtos[0];
			if (eintrittaustrittDto.getTAustritt() != null) {

				if (eintrittaustrittDto.getTAustritt().after(tsZeitpunkt) == true) {
					bAusgetreten = false;
				}
			} else {
				bAusgetreten = false;
			}

		}

		// }
		// catch (FinderException ex) {
		//
		// }
		return new Boolean(bAusgetreten);
	}

	public EintrittaustrittDto eintrittaustrittFindLetztenEintrittBisDatum(
			Integer personalIId, java.sql.Timestamp dSucheBis)
			throws EJBExceptionLP {
		if (personalIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalIId == null"));
		}
		EintrittaustrittDto letzterEintritt = null;
		// try {
		Query query = em
				.createNamedQuery("EintrittaustrittfindLetztenEintrittBisDatum");
		query.setParameter(1, personalIId);
		query.setParameter(2, dSucheBis);

		EintrittaustrittDto[] eintrittaustrittDto = assembleEintrittaustrittDtos(query
				.getResultList());

		if (eintrittaustrittDto.length > 0) {
			letzterEintritt = eintrittaustrittDto[0];
		} else {
			ArrayList<Object> al = new ArrayList<Object>();
			al.add(personalIId);
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM,
					al, new Exception(
							"FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM"
									+ " personalIId=" + personalIId));
		}

		return letzterEintritt;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(e);
		// }
	}

	public EintrittaustrittDto eintrittaustrittFindByPersonalIIdDEintritt(
			Integer personalIId, java.sql.Timestamp dEintritt)
			throws EJBExceptionLP {
		if (personalIId == null || dEintritt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalIId == null || dEintritt == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("EintrittaustrittfindByPersonalIIdTEintritt");
		query.setParameter(1, personalIId);
		query.setParameter(2, dEintritt);
		Eintrittaustritt eintrittaustritt = (Eintrittaustritt) query
				.getSingleResult();
		if (eintrittaustritt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		EintrittaustrittDto eintrittaustrittDto = assembleEintrittaustrittDto(eintrittaustritt);

		return eintrittaustrittDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setEintrittaustrittFromEintrittaustrittDto(
			Eintrittaustritt eintrittaustritt,
			EintrittaustrittDto eintrittaustrittDto) {
		eintrittaustritt.setPersonalIId(eintrittaustrittDto.getPersonalIId());
		eintrittaustritt.setTEintritt(eintrittaustrittDto.getTEintritt());
		eintrittaustritt.setTAustritt(eintrittaustrittDto.getTAustritt());
		eintrittaustritt.setCAustrittsgrund(eintrittaustrittDto
				.getCAustrittsgrund());
		em.merge(eintrittaustritt);
		em.flush();
	}

	private EintrittaustrittDto assembleEintrittaustrittDto(
			Eintrittaustritt eintrittaustritt) {
		return EintrittaustrittDtoAssembler.createDto(eintrittaustritt);
	}

	private EintrittaustrittDto[] assembleEintrittaustrittDtos(
			Collection<?> eintrittaustritts) {
		List<EintrittaustrittDto> list = new ArrayList<EintrittaustrittDto>();
		if (eintrittaustritts != null) {
			Iterator<?> iterator = eintrittaustritts.iterator();
			while (iterator.hasNext()) {
				Eintrittaustritt eintrittaustritt = (Eintrittaustritt) iterator
						.next();
				list.add(assembleEintrittaustrittDto(eintrittaustritt));
			}
		}
		EintrittaustrittDto[] returnArray = new EintrittaustrittDto[list.size()];
		return (EintrittaustrittDto[]) list.toArray(returnArray);
	}

	private void setAngehoerigenartsprFromAngehoerigenartsprDto(
			Angehoerigenartspr angehoerigenartspr,
			AngehoerigenartsprDto angehoerigenartsprDto) {
		angehoerigenartspr.setCBez(angehoerigenartsprDto.getCBez());
		em.merge(angehoerigenartspr);
		em.flush();
	}

	private AngehoerigenartsprDto assembleAngehoerigenartsprDto(
			Angehoerigenartspr angehoerigenartspr) {
		return AngehoerigenartsprDtoAssembler.createDto(angehoerigenartspr);
	}

	private AngehoerigenartsprDto[] assembleAngehoerigenartsprDtos(
			Collection<?> angehoerigenartsprs) {
		List<AngehoerigenartsprDto> list = new ArrayList<AngehoerigenartsprDto>();
		if (angehoerigenartsprs != null) {
			Iterator<?> iterator = angehoerigenartsprs.iterator();
			while (iterator.hasNext()) {
				Angehoerigenartspr angehoerigenartspr = (Angehoerigenartspr) iterator
						.next();
				list.add(assembleAngehoerigenartsprDto(angehoerigenartspr));
			}
		}
		AngehoerigenartsprDto[] returnArray = new AngehoerigenartsprDto[list
				.size()];
		return (AngehoerigenartsprDto[]) list.toArray(returnArray);
	}

	/**
	 * Holt einen Personaldatensatz und den dazugeh&ouml;rigen Partner-
	 * Datensatz anhand der Personalnummer
	 * 
	 * @param cPersonalnr
	 *            die Personalnummer
	 * @param mandantCNr
	 *            Mandant
	 * @throws EJBExceptionLP
	 * @return PersonalDto
	 */
	public PersonalDto personalFindByCPersonalnrMandantCNr(String cPersonalnr,
			String mandantCNr) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("PersonalfindByCPersonalnrMandantCNr");
			query.setParameter(1, cPersonalnr);
			query.setParameter(2, mandantCNr);
			Personal personal = (Personal) query.getSingleResult();
			if (personal == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assemblePersonalDto(personal);
		} catch (NoResultException ex) {
			return null;
		}
	}

	/**
	 * Holt einen Personaldatensatz und den dazugeh&ouml;rigen Partner-
	 * Datensatz anhand der Personalnummer
	 * 
	 * @param cPersonalnr
	 *            die Personalnummer
	 * @param mandantCNr
	 *            Mandant
	 * @throws EJBExceptionLP
	 * @return PersonalDto
	 */
	public PersonalDto personalFindByCPersonalnrMandantCNrOhneExc(
			String cPersonalnr, String mandantCNr) throws EJBExceptionLP {
		PersonalDto personalDto = null;
		try {
			Query query = em
					.createNamedQuery("PersonalfindByCPersonalnrMandantCNr");
			query.setParameter(1, cPersonalnr);
			query.setParameter(2, mandantCNr);
			Personal personal = (Personal) query.getSingleResult();
			personalDto = assemblePersonalDto(personal);
		} catch (NoResultException ex) {
			// nothing here.
		}
		return personalDto;
	}

	/**
	 * Holt Alle Personen die eine Ausweisnummer definiert haben, sortiert nach
	 * Personalnr
	 * 
	 * @throws EJBExceptionLP
	 * @return PersonalDto[]
	 */
	public PersonalDto[] personalFindByCAusweisSortiertNachPersonalnr()
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("PersonalfindByCAusweisSortiertNachIPersonalnr");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }

		return assemblePersonalDtos(cl, true);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	/**
	 * Suche nach Ausweisnummer
	 * 
	 * @throws EJBExceptionLP
	 * @return PersonalDto[]
	 * @param cAusweis
	 *            String
	 */
	public PersonalDto personalFindByCAusweis(String cAusweis)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("PersonalfindByCAusweis");
		query.setParameter(1, cAusweis);
		Collection<?> cl = query.getResultList();
		PersonalDto[] personalDtos = assemblePersonalDtos(cl, true);
		if (personalDtos.length > 0) {
			return personalDtos[0];
		} else {
			return null;
		}
	}

	/**
	 * Holt Alle Personen die eine Ausweisnummer definiert haben, sortiert nach
	 * Ausweisnummer
	 * 
	 * @throws EJBExceptionLP
	 * @return PersonalDto[]
	 */
	public PersonalDto[] personalFindByCAusweisSortiertNachCAusweis()
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("PersonalfindByCAusweisSortiertNachCAusweis");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assemblePersonalDtos(cl, true);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	/**
	 * Holt einen Personaldatensatz und den dazugeh&ouml;rigen Partner-
	 * Datensatz anhand des Mandanten und der PartnerIId
	 * 
	 * @param partnerIId
	 *            Integer
	 * @param mandantCNr
	 *            String
	 * @throws EJBExceptionLP
	 * @return PersonalDto
	 */
	public PersonalDto personalFindByPartnerIIdMandantCNr(Integer partnerIId,
			String mandantCNr) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("PersonalfindByPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		Personal personal = (Personal) query.getSingleResult();
		if (personal == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return assemblePersonalDto(personal);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public PersonalDto[] personalFindByPersonalgruppeIdMandantCNr(
			Integer personalgruppe, String mandantCNr, boolean bPlusVersteckte) {
		// try {
		Query query = em
				.createNamedQuery("PersonalfindByPersonalgruppeIIdMandantCNr");
		query.setParameter(1, personalgruppe);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();

		return assemblePersonalDtos(cl, bPlusVersteckte);

	}

	public PersonalDto personalFindByPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr) {
		try {
			Query query = em
					.createNamedQuery("PersonalfindByPartnerIIdMandantCNr");
			query.setParameter(1, partnerIId);
			query.setParameter(2, mandantCNr);
			Personal personal = (Personal) query.getSingleResult();
			return assemblePersonalDto(personal);
		} catch (NoResultException ex1) {
			return null;
		}

	}

	/**
	 * Holt alle Personaldatens&auml;tze, die als Sozialversicherer den
	 * &uuml;bergebenen Partner haben
	 * 
	 * @param partnerIId
	 *            Integer - Sozialversicherer
	 * @param mandantCNr
	 *            String
	 * @return PersonalDto
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	public PersonalDto[] personalFindBySozialversichererPartnerIIdMandantCNr(
			Integer partnerIId, String mandantCNr) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("PersonalfindBySozialversichererPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assemblePersonalDtos(cl, true);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public PersonalDto[] personalFindBySozialversichererPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr) throws EJBExceptionLP {
		PersonalDto[] aPersonalDtos = null;
		try {
			Query query = em
					.createNamedQuery("PersonalfindBySozialversichererPartnerIIdMandantCNr");
			query.setParameter(1, partnerIId);
			query.setParameter(2, mandantCNr);
			Collection<?> cl = query.getResultList();
			// if (! cl.isEmpty()) {
			aPersonalDtos = assemblePersonalDtos(cl, true);
			// }
		} catch (Throwable ex) {
			myLogger.warn(
					"PersonalFindBySozialversichererPartnerIIdMandantCNrOhneExc",
					ex);
		}
		return aPersonalDtos;
	}

	/**
	 * Holt alle Personaldatens&auml;tze, die bei Firma die &uuml;bergebe
	 * PartnerId eingetragen haben
	 * 
	 * @param partnerIId
	 *            Integer
	 * @param mandantCNr
	 *            String
	 * @return PersonalDto[]
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	public PersonalDto[] personalFindByFirmaPartnerIIdMandantCNr(
			Integer partnerIId, String mandantCNr) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("PersonalfindByFirmaPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assemblePersonalDtos(cl, true);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public PersonalDto[] personalFindByFirmaPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr) throws EJBExceptionLP {
		PersonalDto[] aPersonalDtos = null;
		try {
			Query query = em
					.createNamedQuery("PersonalfindByFirmaPartnerIIdMandantCNr");
			query.setParameter(1, partnerIId);
			query.setParameter(2, mandantCNr);
			Collection<?> cl = query.getResultList();
			// if (! cl.isEmpty()) {
			aPersonalDtos = assemblePersonalDtos(cl, true);
			// }
		}
		// catch (javax.ejb.ObjectNotFoundException ex) {
		// // nothing
		// }
		catch (Throwable ex) {
			myLogger.warn("personalFindByFirmaPartnerIIdMandantCNrOhneExc", ex);
		}
		return aPersonalDtos;
	}

	/**
	 * Holt alle Personen eines Mandanten einer gewissen Personalfunktion des
	 * Mandanten und der PartnerIId
	 * 
	 * @param mandantCNr
	 *            String
	 * @param personalfunktionCNr
	 *            "Geschesftsfuehrer"
	 * @throws EJBExceptionLP
	 * @return PersonalDto[]
	 */
	public PersonalDto[] personalFindByMandantCNrPersonalfunktionCNr(
			String mandantCNr, String personalfunktionCNr)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("PersonalfindByMandantCNrPersonalfunktionCNr");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, personalfunktionCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assemblePersonalDtos(cl, true);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

	}

	/**
	 * Holt alle Personen eines Mandanten des Mandanten und der PartnerIId
	 * 
	 * @param mandantCNr
	 *            String
	 * @param bPlusVersteckte
	 *            Boolean
	 * @throws EJBExceptionLP
	 * @return PersonalDto[]
	 */
	public PersonalDto[] personalFindByMandantCNr(String mandantCNr,
			Boolean bPlusVersteckte) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("PersonalfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		return assemblePersonalDtos(cl, bPlusVersteckte.booleanValue());
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);

		// }
	}

	/**
	 * Holt alle Angestellte eines Mandanten des Mandanten und der PartnerIId
	 * 
	 * @param mandantCNr
	 *            String
	 * @param bPlusVersteckte
	 *            Boolean
	 * @throws EJBExceptionLP
	 * @return PersonalDto[]
	 */
	public PersonalDto[] personalFindAllAngestellteEinesMandanten(
			String mandantCNr, Boolean bPlusVersteckte) throws EJBExceptionLP {
		// try {

		Query query = em
				.createNamedQuery("PersonalfindByMandantCNrPersonalartCNr");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, PersonalFac.PERSONALART_ANGESTELLTER);
		Collection<?> personals = query.getResultList();
		// if (personals.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		List<PersonalDto> list = new ArrayList<PersonalDto>();
		if (personals != null) {
			Iterator<?> iterator = personals.iterator();
			while (iterator.hasNext()) {
				Personal personal = (Personal) iterator.next();
				if (bPlusVersteckte.booleanValue() == true) {
					list.add(assemblePersonalDto(personal));
				} else {
					if (Helper.short2boolean(personal.getBVersteckt()) == false) {
						list.add(assemblePersonalDto(personal));
					}
				}

			}
		}

		query = em.createNamedQuery("PersonalfindByMandantCNrPersonalartCNr");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, PersonalFac.PERSONALART_LEHRLING_ANGESTELLTER);
		personals = query.getResultList();

		// if (! personals.isEmpty()) {
		Iterator<?> iterator = personals.iterator();
		while (iterator.hasNext()) {
			Personal personal = (Personal) iterator.next();
			if (bPlusVersteckte.booleanValue() == true) {
				list.add(assemblePersonalDto(personal));
			} else {
				if (Helper.short2boolean(personal.getBVersteckt()) == false) {
					list.add(assemblePersonalDto(personal));
				}
			}

		}
		// }

		PersonalDto[] returnArray = new PersonalDto[list.size()];
		return (PersonalDto[]) list.toArray(returnArray);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);

		// }
	}

	public PersonalDto[] personalFindAllPersonenMeinerAbteilung(
			Integer kostenstelleIIdAbteilung, String mandantCNr,
			Boolean bPlusVersteckte) {
		Query query = em
				.createNamedQuery("PersonalfindByMandantCNrKostenstelleIIdAbteilung");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, kostenstelleIIdAbteilung);
		Collection<?> personals = query.getResultList();

		List<PersonalDto> list = new ArrayList<PersonalDto>();
		if (personals != null) {
			Iterator<?> iterator = personals.iterator();
			while (iterator.hasNext()) {
				Personal personal = (Personal) iterator.next();
				if (bPlusVersteckte.booleanValue() == true) {
					list.add(assemblePersonalDto(personal));
				} else {
					if (Helper.short2boolean(personal.getBVersteckt()) == false) {
						list.add(assemblePersonalDto(personal));
					}
				}

			}
		}

		PersonalDto[] returnArray = new PersonalDto[list.size()];
		return (PersonalDto[]) list.toArray(returnArray);
	}

	/**
	 * Holt alle Angestellte eines Mandanten des Mandanten und der PartnerIId
	 * 
	 * @param mandantCNr
	 *            String
	 * @param bPlusVersteckte
	 *            Boolean
	 * @throws EJBExceptionLP
	 * @return PersonalDto[]
	 */
	public PersonalDto[] personalFindAllArbeiterEinesMandanten(
			String mandantCNr, Boolean bPlusVersteckte) throws EJBExceptionLP {
		// try {

		Query query = em
				.createNamedQuery("PersonalfindByMandantCNrPersonalartCNr");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, PersonalFac.PERSONALART_ARBEITER);
		Collection<?> personals = query.getResultList();
		List<PersonalDto> list = new ArrayList<PersonalDto>();
		if (personals != null) {
			Iterator<?> iterator = personals.iterator();
			while (iterator.hasNext()) {
				Personal personal = (Personal) iterator.next();
				if (bPlusVersteckte.booleanValue() == true) {
					list.add(assemblePersonalDto(personal));
				} else {
					if (Helper.short2boolean(personal.getBVersteckt()) == false) {
						list.add(assemblePersonalDto(personal));
					}
				}

			}
		}

		query = em.createNamedQuery("PersonalfindByMandantCNrPersonalartCNr");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, PersonalFac.PERSONALART_LEHRLING_ARBEITER);
		personals = query.getResultList();

		if (personals != null) {
			Iterator<?> iterator = personals.iterator();
			while (iterator.hasNext()) {
				Personal personal = (Personal) iterator.next();
				if (bPlusVersteckte.booleanValue() == true) {
					list.add(assemblePersonalDto(personal));
				} else {
					if (Helper.short2boolean(personal.getBVersteckt()) == false) {
						list.add(assemblePersonalDto(personal));
					}
				}

			}
		}

		PersonalDto[] returnArray = new PersonalDto[list.size()];
		return (PersonalDto[]) list.toArray(returnArray);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);

		// }
	}

	public void createPersonalart(PersonalartDto personalartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (personalartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalartDto == null"));
		}
		if (personalartDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalartDto.getCNr() == null"));
		}
		try {
			Personalart partnerart = new Personalart(personalartDto.getCNr());
			em.persist(personalart);
			em.flush();
			setPersonalartFromPersonalartDto(partnerart, personalartDto);
			if (personalartDto.getPersonalartsprDto() != null) {
				Personalartspr personalartspr = new Personalartspr(
						theClientDto.getLocMandantAsString(),
						personalartDto.getCNr());
				em.persist(personalartspr);
				em.flush();
				setPersonalartsprFromPersonalartsprDto(personalartspr,
						personalartDto.getPersonalartsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void createPersonalfunktion(PersonalfunktionDto personalfunktionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (personalfunktionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalfunktionDto == null"));
		}
		if (personalfunktionDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalfunktionDto.getCNr() == null"));
		}
		try {
			Personalfunktion partnerart = new Personalfunktion(
					personalfunktionDto.getCNr());
			em.persist(personalfunktion);
			em.flush();
			setPersonalfunktionFromPersonalfunktionDto(partnerart,
					personalfunktionDto);
			if (personalfunktionDto.getPersonalfunktionsprDto() != null) {
				Personalfunktionspr personalfunktionspr = new Personalfunktionspr(
						theClientDto.getLocMandantAsString(),
						personalfunktionDto.getCNr());
				em.persist(personalfunktionspr);
				em.flush();
				setPersonalfunktionsprFromPersonalfunktionsprDto(
						personalfunktionspr,
						personalfunktionDto.getPersonalfunktionsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void createFamilienstand(FamilienstandDto familienstandDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (familienstandDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalartDto == null"));
		}
		if (familienstandDto.getCNr() == null
				|| familienstandDto.getISort() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"personalartDto.getCNr() == null || familienstandDto.getISort() == null"));
		}
		try {
			Familienstand familienstand = new Familienstand(
					familienstandDto.getCNr(), familienstandDto.getISort());
			em.persist(familienstand);
			em.flush();
			setFamilienstandFromFamilienstandDto(familienstand,
					familienstandDto);
			if (familienstandDto.getFamilienstandsprDto() != null) {
				Familienstandspr familienstandspr = new Familienstandspr(
						theClientDto.getLocMandantAsString(),
						familienstandDto.getCNr());
				em.persist(familienstandspr);
				em.flush();
				setFamilienstandsprFromFamilienstandsprDto(familienstandspr,
						familienstandDto.getFamilienstandsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void createAngehoerigenart(AngehoerigenartDto angehoerigenartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (angehoerigenartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("angehoerigenartDto == null"));
		}
		if (angehoerigenartDto.getCNr() == null
				|| angehoerigenartDto.getISort() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"angehoerigenartDto.getCNr() == null || angehoerigenartDto.getISort() == null"));
		}
		try {
			Angehoerigenart angehoerigenart = new Angehoerigenart(
					angehoerigenartDto.getCNr(), angehoerigenartDto.getISort());
			em.persist(angehoerigenart);
			em.flush();
			setAngehoerigenartFromAngehoerigenartDto(angehoerigenart,
					angehoerigenartDto);
			if (angehoerigenartDto.getAngehoerigenartsprDto() != null) {
				Angehoerigenartspr angehoerigenartspr = new Angehoerigenartspr(
						theClientDto.getLocMandantAsString(),
						angehoerigenartDto.getCNr());
				em.persist(angehoerigenartspr);
				em.flush();
				setAngehoerigenartsprFromAngehoerigenartsprDto(
						angehoerigenartspr,
						angehoerigenartDto.getAngehoerigenartsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removePersonalart(String cNr) throws EJBExceptionLP {
		// try {
		Personalart toRemove = em.find(Personalart.class, cNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }

	}

	public void removePersonalart(PersonalartDto personalartDto)
			throws EJBExceptionLP {
		if (personalartDto != null) {
			String cNr = personalartDto.getCNr();
			removePersonalart(cNr);
		}
	}

	public void updatePersonalart(PersonalartDto personalartDto)
			throws EJBExceptionLP {
		if (personalartDto != null) {
			String cNr = personalartDto.getCNr();
			// try {
			Personalart personalart = em.find(Personalart.class, cNr);
			if (personalart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPersonalartFromPersonalartDto(personalart, personalartDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public void updatePersonalarts(PersonalartDto[] personalartDtos)
			throws EJBExceptionLP {
		if (personalartDtos != null) {
			for (int i = 0; i < personalartDtos.length; i++) {
				updatePersonalart(personalartDtos[i]);
			}
		}
	}

	public PersonalartDto personalartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto) {
		// try {
		Personalart personalart = em.find(Personalart.class, cNr);
		if (personalart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		PersonalartDto personalartDto = assemblePersonalartDto(personalart);

		PersonalartsprDto personalartsprDto = null;
		// try {
		Personalartspr personalartspr = em.find(Personalartspr.class,
				new PersonalartsprPK(theClientDto.getLocUiAsString(), cNr));
		if (personalartspr != null) {
			personalartsprDto = assemblePersonalartsprDto(personalartspr);
		}
		if (personalartsprDto == null) {
		}
		// }
		// catch (FinderException ex) {
		// }
		if (personalartsprDto == null) {
			// try {
			Personalartspr temp = em.find(Personalartspr.class,
					new PersonalartsprPK(theClientDto.getLocKonzernAsString(),
							cNr));
			if (temp != null) {
				personalartsprDto = assemblePersonalartsprDto(temp);
			}
			if (personalartsprDto == null) {
			}
			// }
			// catch (FinderException ex) {
			// }
		}
		personalartDto.setPersonalartsprDto(personalartsprDto);

		return personalartDto;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	private void setPersonalfunktionsprFromPersonalfunktionsprDto(
			Personalfunktionspr personalfunktionspr,
			PersonalfunktionsprDto personalfunktionsprDto) {
		personalfunktionspr.setCBez(personalfunktionsprDto.getCBez());
		em.merge(personalfunktionspr);
		em.flush();
	}

	private PersonalfunktionsprDto assemblePersonalfunktionsprDto(
			Personalfunktionspr personalfunktionspr) {
		return PersonalfunktionsprDtoAssembler.createDto(personalfunktionspr);
	}

	private PersonalfunktionsprDto[] assemblePersonalfunktionsprDtos(
			Collection<?> personalfunktionsprs) {
		List<PersonalfunktionsprDto> list = new ArrayList<PersonalfunktionsprDto>();
		if (personalfunktionsprs != null) {
			Iterator<?> iterator = personalfunktionsprs.iterator();
			while (iterator.hasNext()) {
				Personalfunktionspr personalfunktionspr = (Personalfunktionspr) iterator
						.next();
				list.add(assemblePersonalfunktionsprDto(personalfunktionspr));
			}
		}
		PersonalfunktionsprDto[] returnArray = new PersonalfunktionsprDto[list
				.size()];
		return (PersonalfunktionsprDto[]) list.toArray(returnArray);
	}

	public void createPersonalfunktion(PersonalfunktionDto personalfunktionDto)
			throws EJBExceptionLP {
		if (personalfunktionDto == null) {
			return;
		}
		try {
			Personalfunktion personalfunktion = new Personalfunktion(
					personalfunktionDto.getCNr());
			em.persist(personalfunktion);
			em.flush();
			setPersonalfunktionFromPersonalfunktionDto(personalfunktion,
					personalfunktionDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void removePersonalfunktion(String cNr) throws EJBExceptionLP {
		// try {
		Personalfunktion toRemove = em.find(Personalfunktion.class, cNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }

	}

	public void removePersonalfunktion(PersonalfunktionDto personalfunktionDto)
			throws EJBExceptionLP {
		if (personalfunktionDto != null) {
			String cNr = personalfunktionDto.getCNr();
			removePersonalfunktion(cNr);
		}
	}

	public void updatePersonalfunktion(PersonalfunktionDto personalfunktionDto)
			throws EJBExceptionLP {
		if (personalfunktionDto != null) {
			String cNr = personalfunktionDto.getCNr();
			// try {
			Personalfunktion personalfunktion = em.find(Personalfunktion.class,
					cNr);
			if (personalfunktion == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPersonalfunktionFromPersonalfunktionDto(personalfunktion,
					personalfunktionDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public void updatePersonalfunktions(
			PersonalfunktionDto[] personalfunktionDtos) throws EJBExceptionLP {
		if (personalfunktionDtos != null) {
			for (int i = 0; i < personalfunktionDtos.length; i++) {
				updatePersonalfunktion(personalfunktionDtos[i]);
			}
		}
	}

	public PersonalfunktionDto personalfunktionFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		// try {
		Personalfunktion personalfunktion = em
				.find(Personalfunktion.class, cNr);
		if (personalfunktion == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePersonalfunktionDto(personalfunktion);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public PersonalfunktionDto[] personalfunktionFindAll()
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("PersonalfunktionfindAll");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,"");
		// }
		return assemblePersonalfunktionDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }

	}

	private void setPersonalfunktionFromPersonalfunktionDto(
			Personalfunktion personalfunktion,
			PersonalfunktionDto personalfunktionDto) {
		em.merge(personalfunktion);
		em.flush();
	}

	private PersonalfunktionDto assemblePersonalfunktionDto(
			Personalfunktion personalfunktion) {
		return PersonalfunktionDtoAssembler.createDto(personalfunktion);
	}

	private PersonalfunktionDto[] assemblePersonalfunktionDtos(
			Collection<?> personalfunktions) {
		List<PersonalfunktionDto> list = new ArrayList<PersonalfunktionDto>();
		if (personalfunktions != null) {
			Iterator<?> iterator = personalfunktions.iterator();
			while (iterator.hasNext()) {
				Personalfunktion personalfunktion = (Personalfunktion) iterator
						.next();
				list.add(assemblePersonalfunktionDto(personalfunktion));
			}
		}
		PersonalfunktionDto[] returnArray = new PersonalfunktionDto[list.size()];
		return (PersonalfunktionDto[]) list.toArray(returnArray);
	}

	public String getNextPersonalnummer(TheClientDto theClientDto)
			throws EJBExceptionLP {
		return generiereNeuePersonalnummer(theClientDto);
	}

	private Comparator<FLRPersonal> getHvPersonalnummerComparator() {
		return new Comparator<FLRPersonal>() {
			@Override
			public int compare(FLRPersonal first, FLRPersonal second) {
				String p1 = first.getC_personalnummer();
				String p2 = second.getC_personalnummer();
				if (p1.length() < p2.length())
					return -1;
				if (p1.length() > p2.length())
					return 1;

				return p1.compareTo(p2);
			}
		};
	}

	private FLRPersonal getLetztesFLRPersonal(List<FLRPersonal> allePersonen) {
		if (allePersonen == null || allePersonen.size() == 0)
			return null;

		Collections.sort(allePersonen, getHvPersonalnummerComparator());
		return allePersonen.get(allePersonen.size() - 1);
	}

	private String generiereNeuePersonalnummer(TheClientDto theClientDto) {

		String beginnArtikelnummer = "";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRPersonal.class);
		crit.add(Restrictions.like("c_personalnummer", beginnArtikelnummer,
				MatchMode.START));
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		crit.addOrder(Order.desc("c_personalnummer"));
		// crit.setMaxResults(1);
		// List<?> results = crit.list();
		// Iterator<?> resultListIterator = results.iterator();

		// if (results.size() > 0) {
		// FLRPersonal flrArtikel = (FLRPersonal) resultListIterator.next();

		FLRPersonal flrArtikel = getLetztesFLRPersonal(crit.list());
		if (flrArtikel != null) {
			String letzteArtikelnummer = flrArtikel.getC_personalnummer();
			int iStartZahl = -1;
			int iEndeZahl = -1;
			boolean bEndeFound = false;
			int i = beginnArtikelnummer.length();
			while (i < letzteArtikelnummer.length()) {

				char c = letzteArtikelnummer.charAt(i);
				// wenn 0-9
				if (c > 47 && c < 58) {
					iStartZahl = i;
					iEndeZahl = iStartZahl;
					for (int j = i; j < letzteArtikelnummer.length(); j++) {
						char d = letzteArtikelnummer.charAt(j);
						if (d > 47 && d < 58) {
							iEndeZahl = j;
							if (j == letzteArtikelnummer.length() - 1) {
								bEndeFound = true;
							}
						} else {
							bEndeFound = true;
							break;
						}
					}
				}
				i++;
				if (bEndeFound) {
					break;
				}
			}
			if (iStartZahl >= 0 && iEndeZahl >= 0) {
				String zahlenteil = letzteArtikelnummer.substring(iStartZahl,
						iEndeZahl + 1);
				long zahl = new Long(zahlenteil);
				zahl++;
				// Neue Artikelnummer zusammenbauen
				String neueArtNr = letzteArtikelnummer.substring(0, iStartZahl)
						+ zahl + letzteArtikelnummer.substring(iEndeZahl + 1);

				return neueArtNr;
			}
		}
		session.close();

		return beginnArtikelnummer;
	}

	public Integer getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
			Integer personaIId) throws EJBExceptionLP {
		Session session = FLRSessionFactory.getFactory().openSession();
		Integer artikelIId = null;
		org.hibernate.Criteria crit = session
				.createCriteria(FLRPersonalverfuegbarkeit.class);
		crit.add(Restrictions.eq(
				PersonalFac.FLR_PERSONALVERFUEGBARKEIT_PERSONAL_I_ID,
				personaIId));
		crit.addOrder(Order
				.desc(PersonalFac.FLR_PERSONALVERFUEGBARKEIT_F_ANTEILPROZENT));
		crit.setMaxResults(1);

		List<?> resultList = crit.list();
		if (resultList.size() > 0) {
			Iterator<?> resultListIterator = resultList.iterator();
			FLRPersonalverfuegbarkeit personal = (FLRPersonalverfuegbarkeit) resultListIterator
					.next();
			artikelIId = personal.getArtikel_i_id();
		}

		return artikelIId;
	}

	public Integer createPersonalzeiten(PersonalzeitenDto personalzeitenDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (personalzeitenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalzeitenDto == null"));
		}

		if (personalzeitenDto.getPersonalIId() == null
				|| personalzeitenDto.getTVon() == null
				|| personalzeitenDto.getTBis() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"personalzeitenDto.getPersonalIId() == null || personalzeitenDto.getDVon() == null ||  personalzeitenDto.getDBis() == null"));
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PERSONALZEITEN);
		personalzeitenDto.setIId(pk);
		try {
			Personalzeiten personalzeiten = new Personalzeiten(
					personalzeitenDto.getIId(),
					personalzeitenDto.getPersonalIId(),
					personalzeitenDto.getTVon(), personalzeitenDto.getTBis());
			em.persist(personalzeiten);
			em.flush();
			setPersonalzeitenFromPersonalzeitenDto(personalzeiten,
					personalzeitenDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return personalzeitenDto.getIId();
	}

	public void removePersonalzeiten(Integer iId) throws EJBExceptionLP {
		// try {
		Personalzeiten toRemove = em.find(Personalzeiten.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }

	}

	public void removePersonalzeiten(PersonalzeitenDto personalzeitenDto)
			throws EJBExceptionLP {
		if (personalzeitenDto != null) {
			Integer iId = personalzeitenDto.getIId();
			removePersonalzeiten(iId);
		}
	}

	public void updatePersonalzeiten(PersonalzeitenDto personalzeitenDto)
			throws EJBExceptionLP {
		if (personalzeitenDto != null) {
			Integer iId = personalzeitenDto.getIId();
			// try {
			Personalzeiten personalzeiten = em.find(Personalzeiten.class, iId);
			if (personalzeiten == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPersonalzeitenFromPersonalzeitenDto(personalzeiten,
					personalzeitenDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public void updatePersonalzeitens(PersonalzeitenDto[] personalzeitenDtos)
			throws EJBExceptionLP {
		if (personalzeitenDtos != null) {
			for (int i = 0; i < personalzeitenDtos.length; i++) {
				updatePersonalzeiten(personalzeitenDtos[i]);
			}
		}
	}

	public PersonalzeitenDto personalzeitenFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Personalzeiten personalzeiten = em.find(Personalzeiten.class, iId);
		if (personalzeiten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePersonalzeitenDto(personalzeiten);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setPersonalzeitenFromPersonalzeitenDto(
			Personalzeiten personalzeiten, PersonalzeitenDto personalzeitenDto) {
		personalzeiten.setPersonalIId(personalzeitenDto.getPersonalIId());
		personalzeiten.setTVon(personalzeitenDto.getTVon());
		personalzeiten.setTBis(personalzeitenDto.getTBis());
		personalzeiten.setCBemerkung(personalzeitenDto.getCBemerkung());
		em.merge(personalzeiten);
		em.flush();
	}

	private PersonalzeitenDto assemblePersonalzeitenDto(
			Personalzeiten personalzeiten) {
		return PersonalzeitenDtoAssembler.createDto(personalzeiten);
	}

	private PersonalzeitenDto[] assemblePersonalzeitenDtos(
			Collection<?> personalzeitens) {
		List<PersonalzeitenDto> list = new ArrayList<PersonalzeitenDto>();
		if (personalzeitens != null) {
			Iterator<?> iterator = personalzeitens.iterator();
			while (iterator.hasNext()) {
				Personalzeiten personalzeiten = (Personalzeiten) iterator
						.next();
				list.add(assemblePersonalzeitenDto(personalzeiten));
			}
		}
		PersonalzeitenDto[] returnArray = new PersonalzeitenDto[list.size()];
		return (PersonalzeitenDto[]) list.toArray(returnArray);
	}

	public Integer createPersonalgehalt(PersonalgehaltDto personalgehaltDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (personalgehaltDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalgehaltDto == null"));
		}

		if (personalgehaltDto.getPersonalIId() == null
				|| personalgehaltDto.getIJahr() == null
				|| personalgehaltDto.getIMonat() == null
				|| personalgehaltDto.getBUestdauszahlen() == null
				|| personalgehaltDto.getNUestdpuffer() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"personalgehaltDto.getPersonalIId() == null || personalgehaltDto.getIJahr() == null || personalgehaltDto.getIMonat() == null || personalgehaltDto.getBUestdauszahlen() == null || personalgehaltDto.getNUestdpuffer() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("PersonalgehaltfindByPersonalIIdIJahrIMonat");
			query.setParameter(1, personalgehaltDto.getPersonalIId());
			query.setParameter(2, personalgehaltDto.getIJahr());
			query.setParameter(3, personalgehaltDto.getIMonat());
			Personalgehalt doppelt = (Personalgehalt) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PERSONALGEHALT.UK"));
			}
		} catch (NoResultException ex1) {
			// nothing here
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PERSONALGEHALT);
			personalgehaltDto.setIId(pk);

			personalgehaltDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			personalgehaltDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));

			Personalgehalt personalgehalt = new Personalgehalt(
					personalgehaltDto.getIId(),
					personalgehaltDto.getPersonalIId(),
					personalgehaltDto.getNGehalt(),
					personalgehaltDto.getFUestpauschale(),
					personalgehaltDto.getNStundensatz(),
					personalgehaltDto.getPersonalIIdAendern(),
					personalgehaltDto.getIJahr(),
					personalgehaltDto.getIMonat(),
					personalgehaltDto.getBUestdauszahlen(),
					personalgehaltDto.getNUestdpuffer());
			em.persist(personalgehalt);
			em.flush();

			if (personalgehaltDto.getBAlleinerzieher() == null) {
				personalgehaltDto.setBAlleinerzieher(personalgehalt
						.getBAlleinerzieher());
			}
			if (personalgehaltDto.getBAlleinverdiener() == null) {
				personalgehaltDto.setBAlleinverdiener(personalgehalt
						.getBAlleinverdiener());
			}
			if (personalgehaltDto.getBKksgebbefreit() == null) {
				personalgehaltDto.setBKksgebbefreit(personalgehalt
						.getBKksgebbefreit());
			}
			if (personalgehaltDto.getBStundensatzFixiert() == null) {
				personalgehaltDto.setBStundensatzFixiert(personalgehalt
						.getBStundensatzFixiert());
			}
			if (personalgehaltDto.getNKmgeld1() == null) {
				personalgehaltDto.setNKmgeld1(personalgehalt.getNKmgeld1());
			}
			if (personalgehaltDto.getFBiskilometer() == null) {
				personalgehaltDto.setFBiskilometer(personalgehalt
						.getFBiskilometer());
			}
			if (personalgehaltDto.getNKmgeld2() == null) {
				personalgehaltDto.setNKmgeld2(personalgehalt.getNKmgeld2());
			}
			if (personalgehaltDto.getNKmgeld2() == null) {
				personalgehaltDto.setNKmgeld2(personalgehalt.getNKmgeld2());
			}
			if (personalgehaltDto.getFVerfuegbarkeit() == null) {
				personalgehaltDto.setFVerfuegbarkeit(personalgehalt
						.getFVerfuegbarkeit());
			}
			if (personalgehaltDto.getNGehalt() == null) {
				personalgehaltDto.setNGehalt(personalgehalt.getNGehalt());
			}
			if (personalgehaltDto.getFUestpauschale() == null) {
				personalgehaltDto.setFUestpauschale(personalgehalt
						.getFUestpauschale());
			}
			if (personalgehaltDto.getNStundensatz() == null) {
				personalgehaltDto.setNStundensatz(personalgehalt
						.getNStundensatz());
			}

			setPersonalgehaltFromPersonalgehaltDto(personalgehalt,
					personalgehaltDto);

			return personalgehaltDto.getIId();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void removePersonalgehalt(PersonalgehaltDto personalgehaltDto)
			throws EJBExceptionLP {
		if (personalgehaltDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalgehaltDto == null"));
		}
		if (personalgehaltDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("personalgehaltDto.getIId() == nulll"));
		}
		try {
			Personalgehalt personalgehalt = em.find(Personalgehalt.class,
					personalgehaltDto.getIId());
			if (personalgehalt == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(personalgehalt);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updatePersonalgehalt(PersonalgehaltDto personalgehaltDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (personalgehaltDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalgehaltDto == null"));
		}

		if (personalgehaltDto.getIId() == null
				|| personalgehaltDto.getPersonalIId() == null
				|| personalgehaltDto.getIJahr() == null
				|| personalgehaltDto.getIMonat() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"personalgehaltDto.getIId() == null || personalgehaltDto.getPersonalIId() == null || personalgehaltDto.getIJahr() == null || personalgehaltDto.getIMonat() == null"));
		}
		if (personalgehaltDto.getFBiskilometer() == null
				|| personalgehaltDto.getFUestpauschale() == null
				|| personalgehaltDto.getFVerfuegbarkeit() == null
				|| personalgehaltDto.getNGehalt() == null
				|| personalgehaltDto.getNKmgeld1() == null
				|| personalgehaltDto.getNKmgeld2() == null
				|| personalgehaltDto.getNStundensatz() == null
				|| personalgehaltDto.getBAlleinerzieher() == null
				|| personalgehaltDto.getBAlleinverdiener() == null
				|| personalgehaltDto.getBKksgebbefreit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"personalgehaltDto.getFBiskilometer() == null || personalgehaltDto.getFUestpauschale() == null || personalgehaltDto.getFVerfuegbarkeit() == null || personalgehaltDto.getNGehalt() == null || personalgehaltDto.getNKmgeld1() == null || personalgehaltDto.getNKmgeld2() == null || personalgehaltDto.getNStundensatz() == null || personalgehaltDto.getBAlleinerzieher() == null || personalgehaltDto.getBAlleinverdiener() == null || personalgehaltDto.getBKksgebbefreit() == null"));
		}
		Integer iId = personalgehaltDto.getIId();
		// try {
		Personalgehalt personalgehalt = em.find(Personalgehalt.class, iId);
		if (personalgehalt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em
					.createNamedQuery("PersonalgehaltfindByPersonalIIdIJahrIMonat");
			query.setParameter(1, personalgehaltDto.getPersonalIId());
			query.setParameter(2, personalgehaltDto.getIJahr());
			query.setParameter(3, personalgehaltDto.getIMonat());
			Integer iIdVorhanden = ((Personalgehalt) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PERSONALGEHALT.UK"));
			}

		} catch (NoResultException ex) {
			//
		}

		personalgehaltDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		personalgehaltDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		setPersonalgehaltFromPersonalgehaltDto(personalgehalt,
				personalgehaltDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public PersonalgehaltDto personalgehaltFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Personalgehalt personalgehalt = em.find(Personalgehalt.class, iId);
		if (personalgehalt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		PersonalgehaltDto personalgehaltDto = assemblePersonalgehaltDto(personalgehalt);
		return personalgehaltDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setPersonalgehaltFromPersonalgehaltDto(
			Personalgehalt personalgehalt, PersonalgehaltDto personalgehaltDto) {
		personalgehalt.setPersonalIId(personalgehaltDto.getPersonalIId());
		personalgehalt.setIJahr(personalgehaltDto.getIJahr());
		personalgehalt.setIMonat(personalgehaltDto.getIMonat());
		personalgehalt.setNGehalt(personalgehaltDto.getNGehalt());
		personalgehalt.setFUestpauschale(personalgehaltDto.getFUestpauschale());
		personalgehalt.setNStundensatz(personalgehaltDto.getNStundensatz());
		personalgehalt.setFVerfuegbarkeit(personalgehaltDto
				.getFVerfuegbarkeit());
		personalgehalt.setNKmgeld1(personalgehaltDto.getNKmgeld1());
		personalgehalt.setNUestdpuffer(personalgehaltDto.getNUestdpuffer());
		personalgehalt.setFBiskilometer(personalgehaltDto.getFBiskilometer());
		personalgehalt.setNKmgeld2(personalgehaltDto.getNKmgeld2());
		personalgehalt.setBKksgebbefreit(personalgehaltDto.getBKksgebbefreit());
		personalgehalt.setBUestdauszahlen(personalgehaltDto
				.getBUestdauszahlen());
		personalgehalt.setCGrundkksgebbefreit(personalgehaltDto
				.getCGrundkksgebbefreit());
		personalgehalt.setBAlleinverdiener(personalgehaltDto
				.getBAlleinverdiener());
		personalgehalt.setBAlleinerzieher(personalgehaltDto
				.getBAlleinerzieher());
		personalgehalt.setPersonalIIdAendern(personalgehaltDto
				.getPersonalIIdAendern());
		personalgehalt.setTAendern(personalgehaltDto.getTAendern());
		personalgehalt.setFLeistungswert(personalgehaltDto.getFLeistungswert());

		personalgehalt.setBStundensatzFixiert(personalgehaltDto
				.getBStundensatzFixiert());
		personalgehalt.setNGehaltBruttobrutto(personalgehaltDto
				.getNGehaltBruttobrutto());
		personalgehalt.setNGehaltNetto(personalgehaltDto.getNGehaltNetto());
		personalgehalt.setNPraemieBruttobrutto(personalgehaltDto
				.getNPraemieBruttobrutto());
		personalgehalt.setNLohnmittelstundensatz(personalgehaltDto
				.getNLohnmittelstundensatz());
		personalgehalt.setFFaktorLohnmittelstundensatz(personalgehaltDto
				.getFFaktorLohnmittelstundensatz());
		personalgehalt.setFKalkIstJahresstunden(personalgehaltDto
				.getFKalkIstJahresstunden());
		personalgehalt.setNAufschlagLohnmittelstundensatz(personalgehaltDto
				.getNAufschlagLohnmittelstundensatz());

		em.merge(personalgehalt);
		em.flush();
	}

	private PersonalgehaltDto assemblePersonalgehaltDto(
			Personalgehalt personalgehalt) {
		return PersonalgehaltDtoAssembler.createDto(personalgehalt);
	}

	private PersonalgehaltDto[] assemblePersonalgehaltDtos(
			Collection<?> personalgehalts) {
		List<PersonalgehaltDto> list = new ArrayList<PersonalgehaltDto>();
		if (personalgehalts != null) {
			Iterator<?> iterator = personalgehalts.iterator();
			while (iterator.hasNext()) {
				Personalgehalt personalgehalt = (Personalgehalt) iterator
						.next();
				list.add(assemblePersonalgehaltDto(personalgehalt));
			}
		}
		PersonalgehaltDto[] returnArray = new PersonalgehaltDto[list.size()];
		return (PersonalgehaltDto[]) list.toArray(returnArray);
	}

	public Integer createPersonalzeitmodell(
			PersonalzeitmodellDto personalzeitmodellDto) throws EJBExceptionLP {
		if (personalzeitmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalzeitmodellDto == null"));
		}
		if (personalzeitmodellDto.getPersonalIId() == null
				|| personalzeitmodellDto.getZeitmodellIId() == null
				|| personalzeitmodellDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"personalzeitmodellDto.getPersonalIId() == null || personalzeitmodellDto.getZeitmodellIId() == null || personalzeitmodellDto.getDGueltigab() == null"));
		}

		personalzeitmodellDto.setTGueltigab(Helper
				.cutTimestamp(personalzeitmodellDto.getTGueltigab()));

		try {
			Query query = em
					.createNamedQuery("PersonalzeitmodellfindByPersonalIIdTDatum");
			query.setParameter(1, personalzeitmodellDto.getPersonalIId());
			query.setParameter(2, personalzeitmodellDto.getTGueltigab());
			Personalzeitmodell doppelt = (Personalzeitmodell) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PERSONALZEITMODELL.UK"));
			}
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PERSONALZEITMODELL);
		personalzeitmodellDto.setIId(pk);

		try {

			Personalzeitmodell personalzeitmodell = new Personalzeitmodell(
					personalzeitmodellDto.getIId(),
					personalzeitmodellDto.getPersonalIId(),
					personalzeitmodellDto.getZeitmodellIId(),
					personalzeitmodellDto.getTGueltigab());
			em.persist(personalzeitmodell);
			em.flush();
			setPersonalzeitmodellFromPersonalzeitmodellDto(personalzeitmodell,
					personalzeitmodellDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return personalzeitmodellDto.getIId();
	}

	public Integer createSchichtzeitmodell(
			SchichtzeitmodellDto schichtzeitmodellDto) {
		if (schichtzeitmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("schichtzeitmodellDto == null"));
		}
		if (schichtzeitmodellDto.getPersonalIId() == null
				|| schichtzeitmodellDto.getZeitmodellIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"schichtzeitmodellDto.getPersonalIId() == null || schichtzeitmodellDto.getZeitmodellIId()"));
		}

		try {
			Query query = em
					.createNamedQuery("SchichtzeitmodellfindByPersonalIIdZeitmodellIId");
			query.setParameter(1, schichtzeitmodellDto.getPersonalIId());
			query.setParameter(2, schichtzeitmodellDto.getZeitmodellIId());
			Personalzeitmodell doppelt = (Personalzeitmodell) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_SCHICHTZEITMODELL.UK"));
			}
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SCHICHTZEITMODELL);
		schichtzeitmodellDto.setIId(pk);

		try {

			Schichtzeitmodell personalzeitmodell = new Schichtzeitmodell(
					schichtzeitmodellDto.getIId(),
					schichtzeitmodellDto.getPersonalIId(),
					schichtzeitmodellDto.getZeitmodellIId());
			em.persist(personalzeitmodell);
			em.flush();
			setSchichtzeitmodellFromSchichtzeitmodellDto(personalzeitmodell,
					schichtzeitmodellDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return schichtzeitmodellDto.getIId();
	}

	public void removePersonalzeitmodell(
			PersonalzeitmodellDto personalzeitmodellDto) throws EJBExceptionLP {
		if (personalzeitmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalzeitmodellDto == null"));
		}
		if (personalzeitmodellDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalzeitmodellDto.getIId() == null"));
		}
		// try {

		Personalzeitmodell personalzeitmodell = null;
		try {
			personalzeitmodell = em.find(Personalzeitmodell.class,
					personalzeitmodellDto.getIId());
			if (personalzeitmodell == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
			em.remove(personalzeitmodell);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeSchichtzeitmodell(
			SchichtzeitmodellDto schichtzeitmodellDto) {

		Schichtzeitmodell schichtzeitmodell = null;
		try {
			schichtzeitmodell = em.find(Schichtzeitmodell.class,
					schichtzeitmodellDto.getIId());

			em.remove(schichtzeitmodell);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updatePersonalzeitmodell(
			PersonalzeitmodellDto personalzeitmodellDto) throws EJBExceptionLP {
		if (personalzeitmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalzeitmodellDto == null"));
		}
		if (personalzeitmodellDto.getIId() == null
				|| personalzeitmodellDto.getPersonalIId() == null
				|| personalzeitmodellDto.getZeitmodellIId() == null
				|| personalzeitmodellDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"personalzeitmodellDto.getIId() == null || personalzeitmodellDto.getPersonalIId() == null || personalzeitmodellDto.getZeitmodellIId() == null || personalzeitmodellDto.getDGueltigab() == null"));
		}
		personalzeitmodellDto.setTGueltigab(Helper
				.cutTimestamp(personalzeitmodellDto.getTGueltigab()));

		Integer iId = personalzeitmodellDto.getIId();
		// try {
		Personalzeitmodell personalzeitmodell = em.find(
				Personalzeitmodell.class, iId);
		if (personalzeitmodell == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em
					.createNamedQuery("PersonalzeitmodellfindByPersonalIIdTDatum");
			query.setParameter(1, personalzeitmodellDto.getPersonalIId());
			query.setParameter(2, personalzeitmodellDto.getTGueltigab());
			Integer iIdVorhanden = ((Personalzeitmodell) query
					.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PERSONALZEITMODELL.UK"));
			}
		} catch (NoResultException ex) {
			//
		}

		setPersonalzeitmodellFromPersonalzeitmodellDto(personalzeitmodell,
				personalzeitmodellDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public PersonalDto getPersonalDto_Vorgesetzter(Integer personalIId,
			TheClientDto theClientDto) {
		Personal personal = em.find(Personal.class, personalIId);

		if (personal.getPersonalfunktionCNr() != null
				&& personal.getPersonalfunktionCNr().equals(
						PersonalFac.PERSONALFUNKTION_ABTEILUNGSLEITER)) {
			// Vorgesetztet ist Geschaeftsfuehrer

			Query query = em
					.createNamedQuery("PersonalfindByMandantCNrPersonalfunktionCNrBVersteckt");
			query.setParameter(1, personal.getMandantCNr());
			query.setParameter(2,
					PersonalFac.PERSONALFUNKTION_GESCHAEFTSFUEHRER);
			Collection c = query.getResultList();

			if (c.size() > 0) {
				Personal pGF = (Personal) c.iterator().next();
				return getPersonalFac().personalFindByPrimaryKey(pGF.getIId(),
						theClientDto);
			}

		} else {
			if (personal.getKostenstelleIIdAbteilung() != null) {
				Query query = em
						.createNamedQuery("PersonalfindByMandantCNrKostenstelleIIdAbteilungPersonalfunktionCNr");
				query.setParameter(1, personal.getMandantCNr());
				query.setParameter(2, personal.getKostenstelleIIdAbteilung());
				query.setParameter(3,
						PersonalFac.PERSONALFUNKTION_ABTEILUNGSLEITER);
				Collection c = query.getResultList();

				if (c.size() > 0) {
					Personal pVorgesetzter = (Personal) c.iterator().next();

					return getPersonalFac().personalFindByPrimaryKey(
							pVorgesetzter.getIId(), theClientDto);

				}

			}
		}

		return null;

	}

	public void updateSchichtzeitmodell(
			SchichtzeitmodellDto schichtzeitmodellDto) {
		if (schichtzeitmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("schichtzeitmodellDto == null"));
		}
		if (schichtzeitmodellDto.getIId() == null
				|| schichtzeitmodellDto.getPersonalIId() == null
				|| schichtzeitmodellDto.getZeitmodellIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"schichtzeitmodellDto.getIId() == null || schichtzeitmodellDto.getPersonalIId() == null || schichtzeitmodellDto.getZeitmodellIId() == null"));
		}

		Integer iId = schichtzeitmodellDto.getIId();
		// try {
		Schichtzeitmodell schichtzeitmodell = em.find(Schichtzeitmodell.class,
				iId);
		try {
			Query query = em
					.createNamedQuery("SchichtzeitmodellfindByPersonalIIdZeitmodellIId");
			query.setParameter(1, schichtzeitmodellDto.getPersonalIId());
			query.setParameter(2, schichtzeitmodellDto.getZeitmodellIId());
			Integer iIdVorhanden = ((Schichtzeitmodell) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_SCHICHTZEITMODELL.UK"));
			}
		} catch (NoResultException ex) {
			//
		}

		setSchichtzeitmodellFromSchichtzeitmodellDto(schichtzeitmodell,
				schichtzeitmodellDto);

	}

	public void updatePersonalzeitmodells(
			PersonalzeitmodellDto[] personalzeitmodellDtos)
			throws EJBExceptionLP {
		if (personalzeitmodellDtos != null) {
			for (int i = 0; i < personalzeitmodellDtos.length; i++) {
				updatePersonalzeitmodell(personalzeitmodellDtos[i]);
			}
		}
	}

	public PersonalzeitmodellDto personalzeitmodellFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Personalzeitmodell personalzeitmodell = em.find(
				Personalzeitmodell.class, iId);
		if (personalzeitmodell == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		PersonalzeitmodellDto personalzeitmodellDto = assemblePersonalzeitmodellDto(personalzeitmodell);
		try {
			personalzeitmodellDto.setZeitmodellDto(getZeiterfassungFac()
					.zeitmodellFindByPrimaryKey(
							personalzeitmodellDto.getZeitmodellIId(),
							theClientDto));
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return personalzeitmodellDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public SchichtzeitmodellDto schichtzeitmodellFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Schichtzeitmodell schichtzeitmodell = em.find(Schichtzeitmodell.class,
				iId);
		if (schichtzeitmodell == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		SchichtzeitmodellDto personalzeitmodellDto = assembleSchichtzeitmodellDto(schichtzeitmodell);

		return personalzeitmodellDto;
	}

	public PersonalzeitmodellDto personalzeitmodellFindByPersonalIIdTDatumOhneExc(
			Integer personalIId, Timestamp tDatum) throws EJBExceptionLP {
		PersonalzeitmodellDto personalzeitmodellDto = null;
		try {
			personalzeitmodellDto = personalzeitmodellFindByPersonalIIdTDatum(
					personalIId, tDatum);
		} catch (Throwable t) {
			myLogger.warn(
					"personalzeitmodellFindByPersonalIIdZeitmodellIIdTDatumOhneExc",
					t);
		}
		return personalzeitmodellDto;
	}

	public int getAnzahlDerZeitmodelleEinerPerson(Integer personalIId) {
		Query query = em
				.createNamedQuery("PersonalzeitmodellAnzahlfindByPersonalIId");
		query.setParameter(1, personalIId);

		Long i = (Long) query.getSingleResult();
		return i.intValue();
	}

	public PersonalzeitmodellDto personalzeitmodellFindByPersonalIIdTDatum(
			Integer personalIId, Timestamp tDatum) throws EJBExceptionLP {
		if (personalIId == null || tDatum == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"personalIId == null || zeitmodellIId  == null || tDatum == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("PersonalzeitmodellfindByPersonalIIdTDatum");
		query.setParameter(1, personalIId);
		query.setParameter(2, tDatum);
		Personalzeitmodell personalzeitmodell = (Personalzeitmodell) query
				.getSingleResult();
		if (personalzeitmodell == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		PersonalzeitmodellDto personalzeitmodellDto = assemblePersonalzeitmodellDto(personalzeitmodell);
		return personalzeitmodellDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }
	}

	public PersonalzeitmodellDto personalzeitmodellFindZeitmodellZuDatum(
			Integer personalIId, java.sql.Timestamp dDatum,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (personalIId == null || dDatum == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalIId == null || dDatum == null"));
		}

		String sQuery = "select pzm.i_id FROM FLRPersonalzeitmodell pzm WHERE pzm.personal_i_id="
				+ personalIId
				+ " AND pzm.t_gueltigab<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(dDatum
						.getTime())) + "' ORDER BY pzm.t_gueltigab DESC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query gueltigesZm = session.createQuery(sQuery);
		gueltigesZm.setMaxResults(1);

		List<?> resultList = gueltigesZm.list();

		Iterator<?> resultListIterator = resultList.iterator();

		PersonalzeitmodellDto personalzeitmodellDto = null;

		if (resultListIterator.hasNext()) {
			Integer personalzeitmodellIId = (Integer) resultListIterator.next();
			personalzeitmodellDto = personalzeitmodellFindByPrimaryKey(
					personalzeitmodellIId, theClientDto);
		}

		if (personalzeitmodellDto != null) {
			try {
				personalzeitmodellDto.setZeitmodellDto(getZeiterfassungFac()
						.zeitmodellFindByPrimaryKey(
								personalzeitmodellDto.getZeitmodellIId(),
								theClientDto));
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}
		}
		return personalzeitmodellDto;

	}

	private void setPersonalzeitmodellFromPersonalzeitmodellDto(
			Personalzeitmodell personalzeitmodell,
			PersonalzeitmodellDto personalzeitmodellDto) {
		personalzeitmodell.setPersonalIId(personalzeitmodellDto
				.getPersonalIId());
		personalzeitmodell.setZeitmodellIId(personalzeitmodellDto
				.getZeitmodellIId());
		personalzeitmodell.setTGueltigab(personalzeitmodellDto.getTGueltigab());
		em.merge(personalzeitmodell);
		em.flush();
	}

	private void setSchichtzeitmodellFromSchichtzeitmodellDto(
			Schichtzeitmodell schichtzeitmodell,
			SchichtzeitmodellDto schichtzeitmodellDto) {
		schichtzeitmodell.setPersonalIId(schichtzeitmodellDto.getPersonalIId());
		schichtzeitmodell.setZeitmodellIId(schichtzeitmodellDto
				.getZeitmodellIId());
		em.merge(schichtzeitmodell);
		em.flush();
	}

	private PersonalzeitmodellDto assemblePersonalzeitmodellDto(
			Personalzeitmodell personalzeitmodell) {
		return PersonalzeitmodellDtoAssembler.createDto(personalzeitmodell);
	}

	private SchichtzeitmodellDto assembleSchichtzeitmodellDto(
			Schichtzeitmodell personalzeitmodell) {
		return SchichtzeitmodellDtoAssembler.createDto(personalzeitmodell);
	}

	private PersonalzeitmodellDto[] assemblePersonalzeitmodellDtos(
			Collection<?> personalzeitmodells) {
		List<PersonalzeitmodellDto> list = new ArrayList<PersonalzeitmodellDto>();
		if (personalzeitmodells != null) {
			Iterator<?> iterator = personalzeitmodells.iterator();
			while (iterator.hasNext()) {
				Personalzeitmodell personalzeitmodell = (Personalzeitmodell) iterator
						.next();
				list.add(assemblePersonalzeitmodellDto(personalzeitmodell));
			}
		}
		PersonalzeitmodellDto[] returnArray = new PersonalzeitmodellDto[list
				.size()];
		return (PersonalzeitmodellDto[]) list.toArray(returnArray);
	}

	public Integer createBetriebskalender(
			BetriebskalenderDto betriebskalenderDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (betriebskalenderDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("betriebskalenderDto == null"));
		}

		betriebskalenderDto.setTDatum(Helper.cutTimestamp(betriebskalenderDto
				.getTDatum()));

		if (betriebskalenderDto.getTDatum() == null
				|| betriebskalenderDto.getTagesartIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"betriebskalenderDto.getDDatum() == null || betriebskalenderDto.getTagesartIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("BetriebskalenderfindByMandantCNrTDatum");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, betriebskalenderDto.getTDatum());
			Betriebskalender doppelt = (Betriebskalender) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_BETRIEBSKALENDER.UK"));
		} catch (NoResultException ex) {

		}
		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BETRIEBSKALENDER);
			betriebskalenderDto.setIId(pk);

			betriebskalenderDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));
			betriebskalenderDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			betriebskalenderDto.setMandantCNr(theClientDto.getMandant());

			Betriebskalender betriebskalender = new Betriebskalender(
					betriebskalenderDto.getIId(),
					betriebskalenderDto.getMandantCNr(),
					betriebskalenderDto.getTDatum(),
					betriebskalenderDto.getTagesartIId(),
					betriebskalenderDto.getPersonalIIdAendern());
			em.persist(betriebskalender);
			em.flush();
			setBetriebskalenderFromBetriebskalenderDto(betriebskalender,
					betriebskalenderDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return betriebskalenderDto.getIId();
	}

	public Integer createFeiertag(FeiertagDto feiertagDto,
			TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("FeiertagfindByMandantCNr");
			query.setParameter(1, theClientDto.getMandant());

			Collection cl = query.getResultList();
			/*
			 * throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
			 * new Exception("PERS_BETRIEBSKALENDER.UK"));
			 */
		} catch (NoResultException ex) {

		}
		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FEIERTAG);
			feiertagDto.setIId(pk);

			feiertagDto.setMandantCNr(theClientDto.getMandant());

			Feiertag feiertag = new Feiertag(feiertagDto.getIId(),
					feiertagDto.getMandantCNr(), feiertagDto.getCBez(),
					feiertagDto.getTagesartIId());
			em.persist(feiertag);
			em.flush();
			setFeiertagFromFeiertagDto(feiertag, feiertagDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return feiertagDto.getIId();
	}

	public Integer createZeitabschluss(ZeitabschlussDto dto,
			TheClientDto theClientDto) {

		try {
			Query query = em
					.createNamedQuery("ZeitabschlussfindByPersonalIIdTAbgeschlossenBis");
			query.setParameter(1, dto.getPersonalIId());
			query.setParameter(2, dto.getTAbgeschlossenBis());

			Zeitabschluss zeitabschluss = (Zeitabschluss) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_ZEITABSCHLUSS.UK"));

		} catch (NoResultException ex) {

		}
		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZEITABSCHLUSS);
			dto.setIId(pk);

			Zeitabschluss zeitabschluss = new Zeitabschluss(dto.getIId(),
					dto.getPersonalIId(), dto.getTAbgeschlossenBis());
			em.persist(zeitabschluss);
			em.flush();
			setZeitabschlussFromZeitabschlussDto(zeitabschluss, dto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		HvDtoLogger<ZeitabschlussDto> zeitdatenLogger = new HvDtoLogger<ZeitabschlussDto>(
				em, dto.getPersonalIId(), theClientDto);
		zeitdatenLogger.logInsert(dto);

		return dto.getIId();
	}

	public void removeBetriebskalender(BetriebskalenderDto betriebskalenderDto)
			throws EJBExceptionLP {
		if (betriebskalenderDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("betriebskalenderDto == null"));
		}
		if (betriebskalenderDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("betriebskalenderDto.getIId() == null"));
		}
		try {
			Betriebskalender betriebskalender = em.find(Betriebskalender.class,
					betriebskalenderDto.getIId());
			if (betriebskalender == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(betriebskalender);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}

	}

	public void removeFeiertag(FeiertagDto feiertagDto) {
		Feiertag feiertag = em.find(Feiertag.class, feiertagDto.getIId());
		em.remove(feiertag);
		em.flush();

	}

	public void removeZeitabschluss(ZeitabschlussDto zeitabschlussDto,
			TheClientDto theClientDto) {
		Zeitabschluss zeitabschluss = em.find(Zeitabschluss.class,
				zeitabschlussDto.getIId());
		em.remove(zeitabschluss);
		em.flush();

		HvDtoLogger<ZeitabschlussDto> zeitdatenLogger = new HvDtoLogger<ZeitabschlussDto>(
				em, zeitabschlussDto.getPersonalIId(), theClientDto);
		zeitdatenLogger.logDelete(zeitabschlussDto);

	}

	public void updateBetriebskalender(BetriebskalenderDto betriebskalenderDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (betriebskalenderDto != null) {
			Integer iId = betriebskalenderDto.getIId();

			betriebskalenderDto.setTDatum(Helper
					.cutTimestamp(betriebskalenderDto.getTDatum()));

			Betriebskalender betriebskalender = em.find(Betriebskalender.class,
					iId);
			if (betriebskalender == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			try {
				Query query = em
						.createNamedQuery("BetriebskalenderfindByMandantCNrTDatumReligionIId");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, betriebskalenderDto.getTDatum());
				query.setParameter(3, betriebskalenderDto.getReligionIId());
				Integer iIdVorhanden = ((Betriebskalender) query
						.getSingleResult()).getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PERS_BETRIEBSKALENDER.UK"));
				}

			} catch (NoResultException ex) {

			}

			betriebskalenderDto.setMandantCNr(theClientDto.getMandant());
			betriebskalenderDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));
			betriebskalenderDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());

			setBetriebskalenderFromBetriebskalenderDto(betriebskalender,
					betriebskalenderDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public void feiertageAusVorlageFuerJahrEintragen(Integer iJahr,
			TheClientDto theClientDto) {
		Query query = em.createNamedQuery("FeiertagfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());

		Collection cl = query.getResultList();

		java.sql.Timestamp tOstersonntag = Helper.berechneOstersonntag(iJahr);

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Feiertag ftg = (Feiertag) it.next();

			java.sql.Timestamp datum = null;

			if (ftg.getIOffsetOstersonntag() != null) {

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(tOstersonntag.getTime());
				c.add(Calendar.DATE, ftg.getIOffsetOstersonntag());
				datum = Helper.cutTimestamp(new Timestamp(c.getTimeInMillis()));
			} else {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, iJahr);
				c.set(Calendar.MONTH, ftg.getiMonat());
				c.set(Calendar.DAY_OF_MONTH, ftg.getiTag());

				datum = Helper.cutTimestamp(new Timestamp(c.getTimeInMillis()));
			}

			Query queryUK = em
					.createNamedQuery("BetriebskalenderfindByMandantCNrTDatum");
			queryUK.setParameter(1, theClientDto.getMandant());
			queryUK.setParameter(2, datum);
			Collection cl2 = queryUK.getResultList();

			if (cl2.size() == 0) {
				BetriebskalenderDto bkDto = new BetriebskalenderDto();
				bkDto.setCBez(ftg.getCBez());
				bkDto.setTDatum(datum);
				bkDto.setTagesartIId(ftg.getTagesartIId());
				bkDto.setReligionIId(ftg.getReligionIId());
				createBetriebskalender(bkDto, theClientDto);

			}

		}

	}

	public void updateFeiertag(FeiertagDto feiertagDto,
			TheClientDto theClientDto) {

		Integer iId = feiertagDto.getIId();

		Feiertag feiertag = em.find(Feiertag.class, iId);

		try {
			Query query = em.createNamedQuery("FeiertagfindByMandantCNr");
			query.setParameter(1, theClientDto.getMandant());

			Collection cl = query.getResultList();

			/*
			 * if (iId.equals(iIdVorhanden) == false) { throw new
			 * EJBExceptionLP( EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new
			 * Exception("PERS_BETRIEBSKALENDER.UK")); }
			 */

		} catch (NoResultException ex) {

		}

		feiertagDto.setMandantCNr(theClientDto.getMandant());

		setFeiertagFromFeiertagDto(feiertag, feiertagDto);

	}

	public void updateZeitabschluss(ZeitabschlussDto zeitabschlussDto,
			TheClientDto theClientDto) {

		Integer iId = zeitabschlussDto.getIId();

		Zeitabschluss zeitabschluss = em.find(Zeitabschluss.class, iId);

		vergleicheZeitabschlussDtoVorherNachherUndLoggeAenderungen(
				zeitabschlussDto, theClientDto);
		try {
			Query query = em
					.createNamedQuery("ZeitabschlussfindByPersonalIIdTAbgeschlossenBis");
			query.setParameter(1, zeitabschlussDto.getPersonalIId());
			query.setParameter(2, zeitabschlussDto.getTAbgeschlossenBis());
			Integer iIdVorhanden = ((Zeitabschluss) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZEITABSCHLUSS.UK"));
			}

		} catch (NoResultException ex) {
			//
		}

		setZeitabschlussFromZeitabschlussDto(zeitabschluss, zeitabschlussDto);

	}

	private void vergleicheZeitabschlussDtoVorherNachherUndLoggeAenderungen(
			ZeitabschlussDto dto, TheClientDto theClientDto) {
		ZeitabschlussDto dto_vorher = zeitabschlussFindByPrimaryKey(
				dto.getIId(), theClientDto);

		HvDtoLogger<ZeitabschlussDto> zeitdatenLogger = new HvDtoLogger<ZeitabschlussDto>(
				em, dto_vorher.getPersonalIId(), theClientDto);
		zeitdatenLogger.log(dto_vorher, dto);
	}

	public BetriebskalenderDto betriebskalenderFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Betriebskalender betriebskalender = em
				.find(Betriebskalender.class, iId);
		if (betriebskalender == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		BetriebskalenderDto dto = assembleBetriebskalenderDto(betriebskalender);
		if (dto.getReligionIId() != null) {
			dto.setReligionDto(religionFindByPrimaryKey(dto.getReligionIId(),
					theClientDto));
		}
		return dto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public FeiertagDto feiertagFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {

		Feiertag feiertag = em.find(Feiertag.class, iId);

		FeiertagDto dto = FeiertagDtoAssembler.createDto(feiertag);

		return dto;

	}

	public ZeitabschlussDto zeitabschlussFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {

		Zeitabschluss zeitabschluss = em.find(Zeitabschluss.class, iId);

		ZeitabschlussDto dto = ZeitabschlussDtoAssembler
				.createDto(zeitabschluss);

		return dto;

	}

	public BetriebskalenderDto betriebskalenderFindByMandantCNrDDatum(
			java.sql.Timestamp dDatum, String mandantCNr,
			TheClientDto theClientDto) {
		try {
			Query query = em
					.createNamedQuery("BetriebskalenderfindByMandantCNrTDatum");
			query.setParameter(1, mandantCNr);
			query.setParameter(2, dDatum);
			// @todo getSingleResult oder getResultList ?
			Betriebskalender betriebskalender = (Betriebskalender) query
					.getSingleResult();
			BetriebskalenderDto dto = assembleBetriebskalenderDto(betriebskalender);
			return dto;
		} catch (NoResultException e) {
			return null;
		}
	}

	public BetriebskalenderDto[] betriebskalenderFindByMandantCNrTagesartCNr(
			String tagesartCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Integer tagesartIId = getZeiterfassungFac().tagesartFindByCNr(
				tagesartCNr, theClientDto).getIId();
		Query query = em
				.createNamedQuery("BetriebskalenderfindByMandantCNrTagesartCNr");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, tagesartIId);
		Collection<?> cl = query.getResultList();

		BetriebskalenderDto[] dtos = assembleBetriebskalenderDtos(cl);
		return dtos;

	}

	public BetriebskalenderDto betriebskalenderFindByMandantCNrDDatumReligionIId(
			java.sql.Timestamp dDatum, String mandantCNr, Integer religionIId)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("BetriebskalenderfindByMandantCNrTDatumReligionIId");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, dDatum);
		query.setParameter(3, religionIId);
		Betriebskalender betriebskalender;
		try {
			betriebskalender = (Betriebskalender) query.getSingleResult();
			if (betriebskalender == null) {
				return null;
			}
		} catch (NoResultException e) {
			return null;
		}

		BetriebskalenderDto dto = assembleBetriebskalenderDto(betriebskalender);
		return dto;
		// }
		// catch (FinderException e) {
		// return null;
		// }
	}

	private void setBetriebskalenderFromBetriebskalenderDto(
			Betriebskalender betriebskalender,
			BetriebskalenderDto betriebskalenderDto) {
		betriebskalender.setMandantCNr(betriebskalenderDto.getMandantCNr());
		betriebskalender.setCBez(betriebskalenderDto.getCBez());
		betriebskalender.setTDatum(betriebskalenderDto.getTDatum());
		betriebskalender.setReligionIId(betriebskalenderDto.getReligionIId());
		betriebskalender.setTagesartIId(betriebskalenderDto.getTagesartIId());
		betriebskalender.setPersonalIIdAendern(betriebskalenderDto
				.getPersonalIIdAendern());
		betriebskalender.setTAendern(betriebskalenderDto.getTAendern());
		em.merge(betriebskalender);
		em.flush();
	}

	private void setFeiertagFromFeiertagDto(Feiertag feiertag,
			FeiertagDto feiertagDto) {
		feiertag.setMandantCNr(feiertagDto.getMandantCNr());
		feiertag.setCBez(feiertagDto.getCBez());
		feiertag.setiTag(feiertagDto.getiTag());
		feiertag.setiMonat(feiertagDto.getiMonat());
		feiertag.setReligionIId(feiertagDto.getReligionIId());
		feiertag.setTagesartIId(feiertagDto.getTagesartIId());
		feiertag.setIOffsetOstersonntag(feiertagDto.getIOffsetOstersonntag());

		em.merge(feiertag);
		em.flush();
	}

	private void setZeitabschlussFromZeitabschlussDto(
			Zeitabschluss zeitabschluss, ZeitabschlussDto zeitabschlussDto) {
		zeitabschluss.setPersonalIId(zeitabschlussDto.getPersonalIId());
		zeitabschluss.setTAbgeschlossenBis(zeitabschlussDto
				.getTAbgeschlossenBis());

		em.merge(zeitabschluss);
		em.flush();
	}

	private BetriebskalenderDto assembleBetriebskalenderDto(
			Betriebskalender betriebskalender) {
		return BetriebskalenderDtoAssembler.createDto(betriebskalender);
	}

	private BetriebskalenderDto[] assembleBetriebskalenderDtos(
			Collection<?> betriebskalenders) {
		List<BetriebskalenderDto> list = new ArrayList<BetriebskalenderDto>();
		if (betriebskalenders != null) {
			Iterator<?> iterator = betriebskalenders.iterator();
			while (iterator.hasNext()) {
				Betriebskalender betriebskalender = (Betriebskalender) iterator
						.next();
				list.add(assembleBetriebskalenderDto(betriebskalender));
			}
		}
		BetriebskalenderDto[] returnArray = new BetriebskalenderDto[list.size()];
		return (BetriebskalenderDto[]) list.toArray(returnArray);
	}

	public Integer createUrlaubsanspruch(UrlaubsanspruchDto urlaubsanspruchDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (urlaubsanspruchDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("urlaubsanspruchDto == null"));
		}
		if (urlaubsanspruchDto.getIJahr() == null
				|| urlaubsanspruchDto.getFTage() == null
				|| urlaubsanspruchDto.getFStunden() == null
				|| urlaubsanspruchDto.getPersonalIId() == null
				|| urlaubsanspruchDto.getBGesperrt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"urlaubsanspruchDto.getIJahr() == null || urlaubsanspruchDto.getFTage() == null || urlaubsanspruchDto.getFStunden() == null || urlaubsanspruchDto.getPersonalIId() == null || urlaubsanspruchDto.getBGesperrt() == null"));
		}
		if (urlaubsanspruchDto.getFTagezusaetzlich() == null
				|| urlaubsanspruchDto.getFStundenzusaetzlich() == null
				|| urlaubsanspruchDto.getFJahresurlaubinwochen() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"urlaubsanspruchDto.getFTagezusaetzlich() == null || urlaubsanspruchDto.getFStundenzusaetzlich() == null || urlaubsanspruchDto.getFJahresurlaubinwochen() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("UrlaubsanspruchfindByPersonalIIdIJahr");
			query.setParameter(1, urlaubsanspruchDto.getPersonalIId());
			query.setParameter(2, urlaubsanspruchDto.getIJahr());
			Urlaubsanspruch doppelt = (Urlaubsanspruch) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_URLAUBSANSPRUCH.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_URLAUBSANSPRUCH);
			urlaubsanspruchDto.setIId(pk);
			urlaubsanspruchDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			urlaubsanspruchDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));

			Urlaubsanspruch urlaubsanspruch = new Urlaubsanspruch(
					urlaubsanspruchDto.getIId(),
					urlaubsanspruchDto.getPersonalIId(),
					urlaubsanspruchDto.getIJahr(),
					urlaubsanspruchDto.getFTage(),
					urlaubsanspruchDto.getFStunden(),
					urlaubsanspruchDto.getPersonaIIdAendern(),
					urlaubsanspruchDto.getFStundenzusaetzlich(),
					urlaubsanspruchDto.getFTagezusaetzlich(),
					urlaubsanspruchDto.getBGesperrt(),
					urlaubsanspruchDto.getFJahresurlaubinwochen());
			em.persist(urlaubsanspruch);
			em.flush();
			setUrlaubsanspruchFromUrlaubsanspruchDto(urlaubsanspruch,
					urlaubsanspruchDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
		return urlaubsanspruchDto.getIId();
	}

	public void removeUrlaubsanspruch(UrlaubsanspruchDto urlaubsanspruchDto)
			throws EJBExceptionLP {
		if (urlaubsanspruchDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("urlaubsanspruchDto == null"));
		}
		if (urlaubsanspruchDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("urlaubsanspruchDto.getIId() == nulll"));
		}
		try {
			Urlaubsanspruch urlaubsanspruch = em.find(Urlaubsanspruch.class,
					urlaubsanspruchDto.getIId());
			if (urlaubsanspruch == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(urlaubsanspruch);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateUrlaubsanspruch(UrlaubsanspruchDto urlaubsanspruchDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (urlaubsanspruchDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("urlaubsanspruchDto == null"));
		}
		if (urlaubsanspruchDto.getIId() == null
				|| urlaubsanspruchDto.getIJahr() == null
				|| urlaubsanspruchDto.getFTage() == null
				|| urlaubsanspruchDto.getFStunden() == null
				|| urlaubsanspruchDto.getPersonalIId() == null
				|| urlaubsanspruchDto.getBGesperrt() == null
				|| urlaubsanspruchDto.getFJahresurlaubinwochen() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"urlaubsanspruchDto.getIId() == null || urlaubsanspruchDto.getIJahr() == null || urlaubsanspruchDto.getFTage() == null || urlaubsanspruchDto.getFStunden() == null || urlaubsanspruchDto.getPersonalIId() == null || urlaubsanspruchDto.getBGesperrt() == null || urlaubsanspruchDto.getFJahresurlaubinwochen() == null"));
		}
		if (urlaubsanspruchDto.getFTagezusaetzlich() == null
				|| urlaubsanspruchDto.getFStundenzusaetzlich() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"urlaubsanspruchDto.getFTagezusaetzlich() == null || urlaubsanspruchDto.getFStundenzusaetzlich() == null"));
		}

		Integer iId = urlaubsanspruchDto.getIId();
		// try {
		Urlaubsanspruch urlaubsanspruch = em.find(Urlaubsanspruch.class, iId);
		if (urlaubsanspruch == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em
					.createNamedQuery("UrlaubsanspruchfindByPersonalIIdIJahr");
			query.setParameter(1, urlaubsanspruchDto.getPersonalIId());
			query.setParameter(2, urlaubsanspruchDto.getIJahr());
			Integer iIdVorhanden = ((Urlaubsanspruch) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_URLAUBSANSPRUCH.UK"));
			}

		} catch (NoResultException ex) {
			//
		}

		urlaubsanspruchDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		urlaubsanspruchDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		setUrlaubsanspruchFromUrlaubsanspruchDto(urlaubsanspruch,
				urlaubsanspruchDto);

	}

	public UrlaubsanspruchDto urlaubsanspruchFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Urlaubsanspruch urlaubsanspruch = (Urlaubsanspruch) em.find(
				Urlaubsanspruch.class, iId);
		if (urlaubsanspruch == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleUrlaubsanspruchDto(urlaubsanspruch);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public UrlaubsanspruchDto[] urlaubsanspruchFindLetztenUrlaubsanspruch(
			Integer personalIId, Integer iJahr) throws EJBExceptionLP {
		if (personalIId == null || iJahr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalIId == null || iJahr == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("UrlaubsanspruchfindLetztenUrlaubsanspruch");
		query.setParameter(1, personalIId);
		query.setParameter(2, iJahr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleUrlaubsanspruchDtos(query.getResultList());
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public UrlaubsanspruchDto urlaubsanspruchFindByPersonalIIdIJahr(
			Integer personalIId, Integer iJahr) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("UrlaubsanspruchfindByPersonalIIdIJahr");
			query.setParameter(1, personalIId);
			query.setParameter(2, iJahr);
			Urlaubsanspruch urlaubsanspruch = (Urlaubsanspruch) query
					.getSingleResult();
			return assembleUrlaubsanspruchDto(urlaubsanspruch);
		} catch (NoResultException e) {
			return null;
		}
	}

	public UrlaubsanspruchDto[] urlaubsanspruchFindByPersonalIIdIJahrGroesser(
			Integer personalIId, Integer iJahr) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("UrlaubsanspruchfindByPersonalIIdIJahrGroesser");
		query.setParameter(1, personalIId);
		query.setParameter(2, iJahr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		return assembleUrlaubsanspruchDtos(cl);
		// }
		// catch (FinderException e) {
		// return null;
		// }
	}

	public UrlaubsanspruchDto[] urlaubsanspruchFindByPersonalIIdIJahrKleiner(
			Integer personalIId, Integer iJahr) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("UrlaubsanspruchfindByPersonalIIdIJahrKleiner");
		query.setParameter(1, personalIId);
		query.setParameter(2, iJahr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		return assembleUrlaubsanspruchDtos(cl);
		// }
		// catch (FinderException e) {
		// return null;
		// }
	}

	private void setUrlaubsanspruchFromUrlaubsanspruchDto(
			Urlaubsanspruch urlaubsanspruch,
			UrlaubsanspruchDto urlaubsanspruchDto) {
		urlaubsanspruch.setPersonalIId(urlaubsanspruchDto.getPersonalIId());
		urlaubsanspruch.setIJahr(urlaubsanspruchDto.getIJahr());
		urlaubsanspruch.setFTage(urlaubsanspruchDto.getFTage());
		urlaubsanspruch.setFStunden(urlaubsanspruchDto.getFStunden());
		urlaubsanspruch.setFTagezusaetzlich(urlaubsanspruchDto
				.getFTagezusaetzlich());
		urlaubsanspruch.setFStundenzusaetzlich(urlaubsanspruchDto
				.getFStundenzusaetzlich());
		urlaubsanspruch.setPersonalIIdAendern(urlaubsanspruchDto
				.getPersonaIIdAendern());
		urlaubsanspruch.setTAendern(urlaubsanspruchDto.getTAendern());
		urlaubsanspruch.setFResturlaubjahresendestunden(urlaubsanspruchDto
				.getFResturlaubjahresendestunden());
		urlaubsanspruch.setFResturlaubjahresendetage(urlaubsanspruchDto
				.getFResturlaubjahresendetage());
		urlaubsanspruch.setBGesperrt(urlaubsanspruchDto.getBGesperrt());
		urlaubsanspruch.setFJahresurlaubinwochen(urlaubsanspruchDto
				.getFJahresurlaubinwochen());
		em.merge(urlaubsanspruch);
		em.flush();
	}

	private UrlaubsanspruchDto assembleUrlaubsanspruchDto(
			Urlaubsanspruch urlaubsanspruch) {
		return UrlaubsanspruchDtoAssembler.createDto(urlaubsanspruch);
	}

	private UrlaubsanspruchDto[] assembleUrlaubsanspruchDtos(
			Collection<?> urlaubsanspruchs) {
		List<UrlaubsanspruchDto> list = new ArrayList<UrlaubsanspruchDto>();
		if (urlaubsanspruchs != null) {
			Iterator<?> iterator = urlaubsanspruchs.iterator();
			while (iterator.hasNext()) {
				Urlaubsanspruch urlaubsanspruch = (Urlaubsanspruch) iterator
						.next();
				list.add(assembleUrlaubsanspruchDto(urlaubsanspruch));
			}
		}
		UrlaubsanspruchDto[] returnArray = new UrlaubsanspruchDto[list.size()];
		return (UrlaubsanspruchDto[]) list.toArray(returnArray);
	}

	public Integer createGleitzeitsaldo(GleitzeitsaldoDto gleitzeitsaldoDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (gleitzeitsaldoDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("gleitzeitsaldoDto == null"));
		}
		if (gleitzeitsaldoDto.getIJahr() == null
				|| gleitzeitsaldoDto.getIMonat() == null
				|| gleitzeitsaldoDto.getPersonalIId() == null
				|| gleitzeitsaldoDto.getNSaldo() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"gleitzeitsaldoDto.getIJahr() == null || gleitzeitsaldoDto.getIMonat() == null || gleitzeitsaldoDto.getPersonalIId() == null || gleitzeitsaldoDto.getNSaldo() == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("GleitzeitsaldofindByPersonalIIdIJahrIMonat");
		query.setParameter(1, gleitzeitsaldoDto.getPersonalIId());
		query.setParameter(2, gleitzeitsaldoDto.getIJahr());
		query.setParameter(3, gleitzeitsaldoDto.getIMonat());
		try {
			Gleitzeitsaldo doppelt = (Gleitzeitsaldo) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_GLEITZEITSALDO.UK"));
		} catch (NoResultException e) {
			// nix
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_GLEITZEITSALDO);
			gleitzeitsaldoDto.setIId(pk);

			gleitzeitsaldoDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			gleitzeitsaldoDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));

			Gleitzeitsaldo gleitzeitsaldo = new Gleitzeitsaldo(
					gleitzeitsaldoDto.getIId(),
					gleitzeitsaldoDto.getPersonalIId(),
					gleitzeitsaldoDto.getIJahr(),
					gleitzeitsaldoDto.getIMonat(),
					gleitzeitsaldoDto.getPersonalIIdAendern(),
					gleitzeitsaldoDto.getNSaldo());
			em.persist(gleitzeitsaldo);
			em.flush();

			if (gleitzeitsaldoDto.getBGesperrt() == null) {
				gleitzeitsaldoDto.setBGesperrt(gleitzeitsaldo.getBGesperrt());
			}
			if (gleitzeitsaldoDto.getNSaldomehrstunden() == null) {
				gleitzeitsaldoDto.setNSaldomehrstunden(gleitzeitsaldo
						.getNSaldomehrstunden());
			}
			if (gleitzeitsaldoDto.getNSaldouestfrei100() == null) {
				gleitzeitsaldoDto.setNSaldouestfrei100(gleitzeitsaldo
						.getNSaldouestfrei100());
			}
			if (gleitzeitsaldoDto.getNSaldouestfrei50() == null) {
				gleitzeitsaldoDto.setNSaldouestfrei50(gleitzeitsaldo
						.getNSaldouestfrei50());
			}
			if (gleitzeitsaldoDto.getNSaldouestpflichtig100() == null) {
				gleitzeitsaldoDto.setNSaldouestpflichtig100(gleitzeitsaldo
						.getNSaldouestpflichtig100());
			}
			if (gleitzeitsaldoDto.getNSaldouestpflichtig50() == null) {
				gleitzeitsaldoDto.setNSaldouestpflichtig50(gleitzeitsaldo
						.getNSaldouestpflichtig50());
			}
			if (gleitzeitsaldoDto.getNSaldouest200() == null) {
				gleitzeitsaldoDto.setNSaldouest200(gleitzeitsaldo
						.getNSaldouest200());
			}
			setGleitzeitsaldoFromGleitzeitsaldoDto(gleitzeitsaldo,
					gleitzeitsaldoDto);

			return gleitzeitsaldoDto.getIId();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void removeGleitzeitsaldo(GleitzeitsaldoDto gleitzeitsaldoDto)
			throws EJBExceptionLP {
		if (gleitzeitsaldoDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("gleitzeitsaldoDto == null"));
		}
		if (gleitzeitsaldoDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("gleitzeitsaldoDto.getIId() == nulll"));
		}
		try {
			Gleitzeitsaldo gleitzeitsaldo = em.find(Gleitzeitsaldo.class,
					gleitzeitsaldoDto.getIId());
			if (gleitzeitsaldo == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(gleitzeitsaldo);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateGleitzeitsaldo(GleitzeitsaldoDto gleitzeitsaldoDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (gleitzeitsaldoDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("gleitzeitsaldoDto == null"));
		}
		if (gleitzeitsaldoDto.getIId() == null
				|| gleitzeitsaldoDto.getIJahr() == null
				|| gleitzeitsaldoDto.getIMonat() == null
				|| gleitzeitsaldoDto.getPersonalIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"gleitzeitsaldoDto.getIId() == null || gleitzeitsaldoDto.getIJahr() == null || gleitzeitsaldoDto.getIMonat() == null || gleitzeitsaldoDto.getPersonalIId() == null"));
		}
		if (gleitzeitsaldoDto.getNSaldomehrstunden() == null
				||
				// gleitzeitsaldoDto.getNSaldouestdpauschale() == null ||
				gleitzeitsaldoDto.getNSaldouestfrei100() == null
				|| gleitzeitsaldoDto.getNSaldouestfrei50() == null
				|| gleitzeitsaldoDto.getNSaldouestpflichtig100() == null
				|| gleitzeitsaldoDto.getNSaldouestpflichtig50() == null
				|| gleitzeitsaldoDto.getNSaldo() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"gleitzeitsaldoDto.getNSaldomehrst() == null || gleitzeitsaldoDto.getNSaldouestdpauschale() == null || gleitzeitsaldoDto.getNSaldouestfrei100() == null || gleitzeitsaldoDto.getNSaldouestfrei50() == null || gleitzeitsaldoDto.getNSaldouestpflichtig100() == null || gleitzeitsaldoDto.getNSaldouestpflichtig50() == null"));
		}

		Integer iId = gleitzeitsaldoDto.getIId();
		// try {
		Gleitzeitsaldo gleitzeitsaldo = em.find(Gleitzeitsaldo.class, iId);
		if (gleitzeitsaldo == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em
					.createNamedQuery("GleitzeitsaldofindByPersonalIIdIJahrIMonat");
			query.setParameter(1, gleitzeitsaldoDto.getPersonalIId());
			query.setParameter(2, gleitzeitsaldoDto.getIJahr());
			query.setParameter(3, gleitzeitsaldoDto.getIMonat());
			Integer iIdVorhanden = ((Gleitzeitsaldo) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_GLEITZEITSALDO.UK"));
			}

		} catch (NoResultException ex) {
			//
		}

		gleitzeitsaldoDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		gleitzeitsaldoDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		setGleitzeitsaldoFromGleitzeitsaldoDto(gleitzeitsaldo,
				gleitzeitsaldoDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public GleitzeitsaldoDto gleitzeitsaldoFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Gleitzeitsaldo gleitzeitsaldo = em.find(Gleitzeitsaldo.class, iId);
		if (gleitzeitsaldo == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		return assembleGleitzeitsaldoDto(gleitzeitsaldo);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public GleitzeitsaldoDto gleitzeitsaldoFindByPersonalIIdIJahrIMonat(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("GleitzeitsaldofindByPersonalIIdIJahrIMonat");
			query.setParameter(1, personalIId);
			query.setParameter(2, iJahr);
			query.setParameter(3, iMonat);
			Gleitzeitsaldo gleitzeitsaldo = (Gleitzeitsaldo) query
					.getSingleResult();
			// if (gleitzeitsaldo==null) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEI_FIND,
			// null);
			// }
			return assembleGleitzeitsaldoDto(gleitzeitsaldo);
		} catch (NoResultException e) {
			throw new EJBExceptionLPwoRollback(EJBExceptionLP.FEHLER_BEI_FIND,
					e);

		}
	}

	public GleitzeitsaldoDto gleitzeitsaldoFindLetztenGleitzeitsaldo(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("GleitzeitsaldofindLetztenGleitzeitsaldo");
		query.setParameter(1, personalIId);
		query.setParameter(2, iJahr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		GleitzeitsaldoDto[] dtos = assembleGleitzeitsaldoDtos(cl);
		GleitzeitsaldoDto dto = null;

		for (int i = 0; i < dtos.length; i++) {
			if (dtos[i].getIJahr().intValue() == iJahr.intValue()) {
				if (dtos[i].getIMonat().intValue() < iMonat.intValue()) {
					dto = dtos[i];
					break;
				}
			} else {
				dto = dtos[0];
				break;
			}

		}

		return dto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);

		// }
	}

	private void setGleitzeitsaldoFromGleitzeitsaldoDto(
			Gleitzeitsaldo gleitzeitsaldo, GleitzeitsaldoDto gleitzeitsaldoDto) {
		gleitzeitsaldo.setPersonalIId(gleitzeitsaldoDto.getPersonalIId());
		gleitzeitsaldo.setIJahr(gleitzeitsaldoDto.getIJahr());
		gleitzeitsaldo.setIMonat(gleitzeitsaldoDto.getIMonat());
		gleitzeitsaldo.setNSaldomehrstunden(Helper.rundeKaufmaennisch(
				gleitzeitsaldoDto.getNSaldomehrstunden(), 4));
		gleitzeitsaldo.setNSaldouestfrei50(Helper.rundeKaufmaennisch(
				gleitzeitsaldoDto.getNSaldouestfrei50(), 4));
		gleitzeitsaldo.setNSaldouestpflichtig50(Helper.rundeKaufmaennisch(
				gleitzeitsaldoDto.getNSaldouestpflichtig50(), 4));
		gleitzeitsaldo.setNSaldouestfrei100(Helper.rundeKaufmaennisch(
				gleitzeitsaldoDto.getNSaldouestfrei100(), 4));
		gleitzeitsaldo.setNSaldouest200(Helper.rundeKaufmaennisch(
				gleitzeitsaldoDto.getNSaldouest200(), 4));
		gleitzeitsaldo.setNSaldouestpflichtig100(Helper.rundeKaufmaennisch(
				gleitzeitsaldoDto.getNSaldouestpflichtig100(), 4));
		gleitzeitsaldo.setNSaldo(Helper.rundeKaufmaennisch(
				gleitzeitsaldoDto.getNSaldo(), 4));
		gleitzeitsaldo.setBGesperrt(gleitzeitsaldoDto.getBGesperrt());
		gleitzeitsaldo.setPersonalIIdAendern(gleitzeitsaldoDto
				.getPersonalIIdAendern());
		gleitzeitsaldo.setTAendern(gleitzeitsaldoDto.getTAendern());
		gleitzeitsaldo.setTAbrechnungsstichtag(gleitzeitsaldoDto
				.getDAbrechnungstichtag());
		em.merge(gleitzeitsaldo);
		em.flush();
	}

	private GleitzeitsaldoDto assembleGleitzeitsaldoDto(
			Gleitzeitsaldo gleitzeitsaldo) {
		return GleitzeitsaldoDtoAssembler.createDto(gleitzeitsaldo);
	}

	private GleitzeitsaldoDto[] assembleGleitzeitsaldoDtos(
			Collection<?> gleitzeitsaldos) {
		List<GleitzeitsaldoDto> list = new ArrayList<GleitzeitsaldoDto>();
		if (gleitzeitsaldos != null) {
			Iterator<?> iterator = gleitzeitsaldos.iterator();
			while (iterator.hasNext()) {
				Gleitzeitsaldo gleitzeitsaldo = (Gleitzeitsaldo) iterator
						.next();
				list.add(assembleGleitzeitsaldoDto(gleitzeitsaldo));
			}
		}
		GleitzeitsaldoDto[] returnArray = new GleitzeitsaldoDto[list.size()];
		return (GleitzeitsaldoDto[]) list.toArray(returnArray);
	}

	public Integer createFahrzeug(FahrzeugDto dto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("FahrzeugfindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			Fahrzeug doppelt = (Fahrzeug) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_FAHRUEZG.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FAHRZEUG);
			dto.setIId(pk);

			Fahrzeug bean = new Fahrzeug(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez(), dto.getCKennzeichen());
			em.persist(bean);
			em.flush();
			setFahrzeugFromFahrzeugDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void setFahrzeugFromFahrzeugDto(Fahrzeug bean, FahrzeugDto dto) {
		bean.setCBez(dto.getCBez());
		bean.setCKennzeichen(dto.getCKennzeichen());
		bean.setMandantCNr(dto.getMandantCNr());
		em.merge(bean);
		em.flush();
	}

	public void updateFahrzeug(FahrzeugDto dto, TheClientDto theClientDto) {
		Fahrzeug fahrzeug = em.find(Fahrzeug.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("FahrzeugfindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Fahrzeug) query.getSingleResult())
					.getIId();
			if (fahrzeug.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_FAHRZEUG.UK"));
			}
		} catch (NoResultException ex) {

		}

		setFahrzeugFromFahrzeugDto(fahrzeug, dto);
	}

	public FahrzeugDto fahrzeugFindByPrimaryKey(Integer iId) {
		Fahrzeug ialle = em.find(Fahrzeug.class, iId);
		if (ialle == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return FahrzeugDtoAssembler.createDto(ialle);
	}

	public void removeFahrzeug(FahrzeugDto dto) {
		Fahrzeug toRemove = em.find(Fahrzeug.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public Integer createFahrzeugkosten(FahrzeugkostenDto dto) {
		dto.setTGueltigab(Helper.cutTimestamp(dto.getTGueltigab()));
		try {
			Query query = em
					.createNamedQuery("FahrzeugkostenfindByFahrzeugIIdTGueltigab");
			query.setParameter(1, dto.getFahrzeugIId());
			query.setParameter(2, dto.getTGueltigab());
			Fahrzeugkosten doppelt = (Fahrzeugkosten) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_FAHRUEZGKOSTEN.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FAHRZEUGKOSTEN);
			dto.setIId(pk);

			Fahrzeugkosten bean = new Fahrzeugkosten(dto.getIId(),
					dto.getFahrzeugIId(), dto.getTGueltigab(),
					dto.getNKmkosten());
			em.persist(bean);
			em.flush();
			setFahrzeugkostenFromFahrzeugkostenDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void setFahrzeugkostenFromFahrzeugkostenDto(Fahrzeugkosten bean,
			FahrzeugkostenDto dto) {
		bean.setFahrzeugIId(dto.getFahrzeugIId());
		bean.setNKmkosten(dto.getNKmkosten());
		bean.setTGueltigab(dto.getTGueltigab());
		em.merge(bean);
		em.flush();
	}

	public void updateFahrzeugkosten(FahrzeugkostenDto dto) {
		Fahrzeugkosten fahrzeugkosten = em.find(Fahrzeugkosten.class,
				dto.getIId());
		dto.setTGueltigab(Helper.cutTimestamp(dto.getTGueltigab()));
		try {
			Query query = em
					.createNamedQuery("FahrzeugkostenfindByFahrzeugIIdTGueltigab");
			query.setParameter(1, dto.getFahrzeugIId());
			query.setParameter(2, dto.getTGueltigab());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Fahrzeugkosten) query.getSingleResult())
					.getIId();
			if (fahrzeugkosten.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_FAHRZEUGKOSTEN.UK"));
			}
		} catch (NoResultException ex) {

		}

		setFahrzeugkostenFromFahrzeugkostenDto(fahrzeugkosten, dto);
	}

	public FahrzeugkostenDto fahrzeugkostenFindByPrimaryKey(Integer iId) {
		Fahrzeugkosten ialle = em.find(Fahrzeugkosten.class, iId);
		if (ialle == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return FahrzeugkostenDtoAssembler.createDto(ialle);
	}

	public void removeFahrzeugkosten(FahrzeugkostenDto dto) {
		Fahrzeugkosten toRemove = em.find(Fahrzeugkosten.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public Integer createStundenabrechnung(
			StundenabrechnungDto stundenabrechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (stundenabrechnungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stundenabrechnungDto == null"));
		}
		if (stundenabrechnungDto.getPersonalIId() == null
				|| stundenabrechnungDto.getTDatum() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"stundenabrechnungDto.getPersonalIId() == null || stundenabrechnungDto.getDDatum() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("StundenabrechnungfindByPersonalIIdTDatum");
			query.setParameter(1, stundenabrechnungDto.getPersonalIId());
			query.setParameter(2, stundenabrechnungDto.getTDatum());
			Stundenabrechnung doppelt = (Stundenabrechnung) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_STUNDENABRECHNUNG.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUNDENABRECHNUNG);
			stundenabrechnungDto.setIId(pk);

			stundenabrechnungDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			stundenabrechnungDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));

			Stundenabrechnung stundenabrechnung = new Stundenabrechnung(
					stundenabrechnungDto.getIId(),
					stundenabrechnungDto.getPersonalIId(),
					stundenabrechnungDto.getTDatum(),
					stundenabrechnungDto.getPersonalIIdAendern());
			em.persist(stundenabrechnung);
			em.flush();

			if (stundenabrechnungDto.getNMehrstunden() == null) {
				stundenabrechnungDto.setNMehrstunden(stundenabrechnung
						.getNMehrstunden());
			}
			if (stundenabrechnungDto.getNQualifikationspraemie() == null) {
				stundenabrechnungDto
						.setNQualifikationspraemie(stundenabrechnung
								.getNQualifikationspraemie());
			}
			if (stundenabrechnungDto.getNUestfrei100() == null) {
				stundenabrechnungDto.setNUestfrei100(stundenabrechnung
						.getNUestfrei100());
			}
			if (stundenabrechnungDto.getNUestfrei50() == null) {
				stundenabrechnungDto.setNUestfrei50(stundenabrechnung
						.getNUestfrei50());
			}
			if (stundenabrechnungDto.getNGutstunden() == null) {
				stundenabrechnungDto.setNGutstunden(stundenabrechnung
						.getNGutstunden());
			}
			if (stundenabrechnungDto.getNUestpflichtig100() == null) {
				stundenabrechnungDto.setNUestpflichtig100(stundenabrechnung
						.getNUestpflichtig100());
			}
			if (stundenabrechnungDto.getNUestpflichtig50() == null) {
				stundenabrechnungDto.setNUestpflichtig50(stundenabrechnung
						.getNUestpflichtig50());
			}
			if (stundenabrechnungDto.getNNormalstunden() == null) {
				stundenabrechnungDto.setNNormalstunden(stundenabrechnung
						.getNNormalstunden());
			}
			if (stundenabrechnungDto.getNUest200() == null) {
				stundenabrechnungDto.setNUest200(stundenabrechnung
						.getNUest200());
			}
			setStundenabrechnungFromStundenabrechnungDto(stundenabrechnung,
					stundenabrechnungDto);
			return stundenabrechnungDto.getIId();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void removeStundenabrechnung(
			StundenabrechnungDto stundenabrechnungDto) throws EJBExceptionLP {
		if (stundenabrechnungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stundenabrechnungDto == null"));
		}
		if (stundenabrechnungDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stundenabrechnungDto.getIId() == nulll"));
		}
		try {
			Stundenabrechnung stundenabrechnung = em.find(
					Stundenabrechnung.class, stundenabrechnungDto.getIId());
			if (stundenabrechnung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(stundenabrechnung);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateStundenabrechnung(
			StundenabrechnungDto stundenabrechnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (stundenabrechnungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stundenabrechnungDto == null"));
		}
		if (stundenabrechnungDto.getPersonalIId() == null
				|| stundenabrechnungDto.getTDatum() == null
				|| stundenabrechnungDto.getNNormalstunden() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"stundenabrechnungDto.getPersonalIId() == null || stundenabrechnungDto.getDDatum() == null || stundenabrechnungDto.getNNormalstunden() == null"));
		}
		if (stundenabrechnungDto.getNMehrstunden() == null) {
			stundenabrechnungDto.setNMehrstunden(new BigDecimal(0));
		}
		if (stundenabrechnungDto.getNQualifikationspraemie() == null) {
			stundenabrechnungDto.setNQualifikationspraemie(new BigDecimal(0));
		}
		if (stundenabrechnungDto.getNUestfrei100() == null) {
			stundenabrechnungDto.setNUestfrei100(new BigDecimal(0));
		}
		if (stundenabrechnungDto.getNUestfrei50() == null) {
			stundenabrechnungDto.setNUestfrei50(new BigDecimal(0));
		}
		if (stundenabrechnungDto.getNGutstunden() == null) {
			stundenabrechnungDto.setNGutstunden(new BigDecimal(0));
		}
		if (stundenabrechnungDto.getNUestpflichtig100() == null) {
			stundenabrechnungDto.setNUestpflichtig100(new BigDecimal(0));
		}
		if (stundenabrechnungDto.getNUestpflichtig50() == null) {
			stundenabrechnungDto.setNUestpflichtig50(new BigDecimal(0));
		}
		if (stundenabrechnungDto.getNUest200() == null) {
			stundenabrechnungDto.setNUest200(new BigDecimal(0));
		}

		Integer iId = stundenabrechnungDto.getIId();
		// try {
		Stundenabrechnung stundenabrechnung = em.find(Stundenabrechnung.class,
				iId);
		if (stundenabrechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em
					.createNamedQuery("StundenabrechnungfindByPersonalIIdTDatum");
			query.setParameter(1, stundenabrechnungDto.getPersonalIId());
			query.setParameter(2, stundenabrechnungDto.getTDatum());
			Integer iIdVorhanden = ((Stundenabrechnung) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_STUNDENABRECHNUNG.UK"));
			}

		} catch (NoResultException ex) {
			//
		}

		stundenabrechnungDto
				.setPersonalIIdAendern(theClientDto.getIDPersonal());
		stundenabrechnungDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		setStundenabrechnungFromStundenabrechnungDto(stundenabrechnung,
				stundenabrechnungDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public StundenabrechnungDto stundenabrechnungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Stundenabrechnung stundenabrechnung = em.find(Stundenabrechnung.class,
				iId);
		if (stundenabrechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleStundenabrechnungDto(stundenabrechnung);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public StundenabrechnungDto stundenabrechnungFindByPersonalIIdDDatum(
			Integer personalIId, Timestamp dDatum) throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("StundenabrechnungfindByPersonalIIdTDatum");
		query.setParameter(1, personalIId);
		query.setParameter(2, dDatum);
		Collection<?> cl = query.getResultList();
		if (cl.size() == 0)
			return null;
		else {
			Iterator<?> iterator = cl.iterator();
			return assembleStundenabrechnungDto((Stundenabrechnung) iterator
					.next());
		}
	}

	public StundenabrechnungDto[] stundenabrechnungFindByPersonalIIdIJahrIMonat(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws EJBExceptionLP {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, iJahr.intValue());
		c.set(Calendar.MONTH, iMonat.intValue());
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		Timestamp tVon = new Timestamp(c.getTime().getTime());

		c.set(Calendar.DAY_OF_MONTH,
				Helper.ermittleAnzahlTageEinesMonats(iJahr, iMonat));

		Timestamp tBis = new Timestamp(c.getTime().getTime());

		Query query = em
				.createNamedQuery("StundenabrechnungfindByPersonalIIdTDatumVonTDatumBis");
		query.setParameter(1, personalIId);
		query.setParameter(2, tVon);
		query.setParameter(3, tBis);
		Collection<?> cl = query.getResultList();
		return assembleStundenabrechnungDtos(cl);
	}

	private void setStundenabrechnungFromStundenabrechnungDto(
			Stundenabrechnung stundenabrechnung,
			StundenabrechnungDto stundenabrechnungDto) {
		stundenabrechnung.setPersonalIId(stundenabrechnungDto.getPersonalIId());
		stundenabrechnung.setTDatum(stundenabrechnungDto.getTDatum());
		stundenabrechnung.setNMehrstunden(stundenabrechnungDto
				.getNMehrstunden());
		stundenabrechnung.setNUestfrei50(stundenabrechnungDto.getNUestfrei50());
		stundenabrechnung.setNUestpflichtig50(stundenabrechnungDto
				.getNUestpflichtig50());
		stundenabrechnung.setNUestfrei100(stundenabrechnungDto
				.getNUestfrei100());
		stundenabrechnung.setNUest200(stundenabrechnungDto.getNUest200());
		stundenabrechnung.setNUestpflichtig100(stundenabrechnungDto
				.getNUestpflichtig100());
		stundenabrechnung.setNGutstunden(stundenabrechnungDto.getNGutstunden());
		stundenabrechnung.setNNormalstunden(stundenabrechnungDto
				.getNNormalstunden());
		stundenabrechnung.setNQualifikationspraemie(stundenabrechnungDto
				.getNQualifikationspraemie());
		stundenabrechnung.setCKommentar(stundenabrechnungDto.getCKommentar());
		stundenabrechnung.setTAendern(stundenabrechnungDto.getTAendern());
		stundenabrechnung.setPersonalIIdAendern(stundenabrechnungDto
				.getPersonalIIdAendern());
		stundenabrechnung.setNQualifikationsfaktor(stundenabrechnungDto
				.getNQualifikationsfaktor());
		em.merge(stundenabrechnung);
		em.flush();
	}

	private StundenabrechnungDto assembleStundenabrechnungDto(
			Stundenabrechnung stundenabrechnung) {
		return StundenabrechnungDtoAssembler.createDto(stundenabrechnung);
	}

	private StundenabrechnungDto[] assembleStundenabrechnungDtos(
			Collection<?> stundenabrechnungs) {
		List<StundenabrechnungDto> list = new ArrayList<StundenabrechnungDto>();
		if (stundenabrechnungs != null) {
			Iterator<?> iterator = stundenabrechnungs.iterator();
			while (iterator.hasNext()) {
				Stundenabrechnung stundenabrechnung = (Stundenabrechnung) iterator
						.next();
				list.add(assembleStundenabrechnungDto(stundenabrechnung));
			}
		}
		StundenabrechnungDto[] returnArray = new StundenabrechnungDto[list
				.size()];
		return (StundenabrechnungDto[]) list.toArray(returnArray);
	}

	public PersonalgehaltDto personalgehaltFindByPersonalIIdDGueltigab(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("PersonalgehaltfindByPersonalIIdIJahrIMonat");
		query.setParameter(1, personalIId);
		query.setParameter(2, iJahr);
		query.setParameter(3, iMonat);
		Personalgehalt personalgehalt = (Personalgehalt) query
				.getSingleResult();
		if (personalgehalt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return assemblePersonalgehaltDto(personalgehalt);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, e);
		// }
	}

	public PersonalgehaltDto personalgehaltFindLetztePersonalgehalt(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws EJBExceptionLP {
		// try {

		Query query2 = em
				.createNamedQuery("PersonalgehaltfindLetztePersonalgehalt");
		query2.setParameter(1, personalIId);
		query2.setParameter(2, new Integer(iJahr.intValue() - 1));
		query2.setParameter(3, new Integer(Calendar.DECEMBER));

		PersonalgehaltDto[] dtos = assemblePersonalgehaltDtos(query2
				.getResultList());
		Query query = em
				.createNamedQuery("PersonalgehaltfindByPersonalIIdIJahrMonat");
		query.setParameter(1, personalIId);
		query.setParameter(2, iJahr);
		query.setParameter(3, iMonat);
		// @todo getSingleResult oder getResultList ?
		PersonalgehaltDto[] dtos2 = assemblePersonalgehaltDtos(query
				.getResultList());
		if (dtos2.length > 0) {
			return dtos2[0];
		}

		if (dtos.length > 0) {
			return dtos[0];
		} else {
			return null;
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, e);
		// }
	}

	public JasperPrintLP printPersonalliste(java.sql.Timestamp tsStichtag,
			boolean bMitBarcodes, boolean bMitVersteckten,
			int iOptionSortierung, TheClientDto theClientDto) {

		if (tsStichtag != null) {
			tsStichtag = Helper.cutTimestamp(tsStichtag);
		}
		Session session = null;
		String sSortierung = "";
		try {

			session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Criteria crit = session
					.createCriteria(FLRPersonal.class)
					.createAlias(PersonalFac.FLR_PERSONAL_FLRPARTNER, "p")
					.add(Restrictions.eq("mandant_c_nr",
							theClientDto.getMandant()));
			if (bMitVersteckten == false) {
				crit.add(Restrictions.eq(PersonalFac.FLR_PERSONAL_B_VERSTECKT,
						Helper.boolean2Short(false)));
			}
			if (iOptionSortierung == PersonalFac.REPORT_PERSONALLISTE_OPTION_SORTIERUNG_AUSWEIS) {
				crit.addOrder(Order.asc(PersonalFac.FLR_PERSONAL_C_AUSWEIS));
				sSortierung = getTextRespectUISpr("lp.ausweis",
						theClientDto.getMandant(), theClientDto.getLocUi());
			} else if (iOptionSortierung == PersonalFac.REPORT_PERSONALLISTE_OPTION_SORTIERUNG_NAME) {
				crit.addOrder(Order.asc("p."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
				sSortierung = getTextRespectUISpr("lp.name",
						theClientDto.getMandant(), theClientDto.getLocUi());
			} else if (iOptionSortierung == PersonalFac.REPORT_PERSONALLISTE_OPTION_SORTIERUNG_PERSONALNUMMER) {
				crit.addOrder(Order
						.asc(PersonalFac.FLR_PERSONAL_C_PERSONALNUMMER));
				sSortierung = getTextRespectUISpr("lp.personalnr",
						theClientDto.getMandant(), theClientDto.getLocUi());
			}

			List<?> resultList = crit.list();

			Iterator<?> resultListIterator = resultList.iterator();

			ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

			while (resultListIterator.hasNext()) {
				FLRPersonal personal = (FLRPersonal) resultListIterator.next();

				if (tsStichtag == null
						|| istPersonalAusgetreten(personal.getI_id(),
								tsStichtag, theClientDto).booleanValue() == false) {

					Object[] dataHelp = new Object[REPORT_PERSONALLISTE_ANZAHL_SPALTEN];

					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(personal.getI_id(),
									theClientDto);
					dataHelp[REPORT_PERSONALLISTE_PERSONALNUMMER] = personal
							.getC_personalnummer();
					dataHelp[REPORT_PERSONALLISTE_NAME] = personal
							.getFlrpartner().getC_name1nachnamefirmazeile1();
					dataHelp[REPORT_PERSONALLISTE_VORNAME] = personal
							.getFlrpartner().getC_name2vornamefirmazeile2();
					dataHelp[REPORT_PERSONALLISTE_GEBURTSTAG] = personal
							.getT_geburtsdatum();

					try {
						if (tsStichtag == null) {
							dataHelp[REPORT_PERSONALLISTE_EINTRITTSDATUM_ZUM_STICHTAG] = eintrittaustrittFindLetztenEintrittBisDatum(
									personal.getI_id(),
									new Timestamp(System.currentTimeMillis()))
									.getTEintritt();

						} else {
							dataHelp[REPORT_PERSONALLISTE_EINTRITTSDATUM_ZUM_STICHTAG] = eintrittaustrittFindLetztenEintrittBisDatum(
									personal.getI_id(), tsStichtag)
									.getTEintritt();
						}

					} catch (Exception e) {
						// Kein Eintrittsdatum
					}
					PersonalzeitmodellDto pzDto = null;
					if (tsStichtag == null) {
						pzDto = personalzeitmodellFindZeitmodellZuDatum(
								personal.getI_id(),
								new Timestamp(System.currentTimeMillis()),
								theClientDto);
					} else {
						pzDto = personalzeitmodellFindZeitmodellZuDatum(
								personal.getI_id(), tsStichtag, theClientDto);
					}

					if (pzDto != null && pzDto.getZeitmodellDto() != null) {
						dataHelp[REPORT_PERSONALLISTE_ZEITMODELL_ZUM_STICHTAG] = pzDto
								.getZeitmodellDto().getBezeichnung();
					}

					if (personalDto.getBerufDto() != null) {
						dataHelp[REPORT_PERSONALLISTE_BERUF] = personalDto
								.getBerufDto().getCBez();
					}

					if (personalDto.getReligionDto() != null) {
						dataHelp[REPORT_PERSONALLISTE_RELIGION] = personalDto
								.getReligionDto().getCNr();
					}
					if (personalDto.getKollektivDto() != null) {
						dataHelp[REPORT_PERSONALLISTE_KOLLEKTIV] = personalDto
								.getKollektivDto().getCBez();
					}
					dataHelp[REPORT_PERSONALLISTE_FAMILIENASTAND] = personalDto
							.getFamilienstandCNr();

					if (personalDto.getLohngruppeDto() != null) {
						dataHelp[REPORT_PERSONALLISTE_LOHNGRUPPE] = personalDto
								.getLohngruppeDto().getCBez();
					}
					if (personalDto.getLandplzortDto_Geburtsort() != null) {
						dataHelp[REPORT_PERSONALLISTE_GEBURTSORT] = personalDto
								.getLandplzortDto_Geburtsort()
								.formatLandPlzOrt();
					}

					if (personalDto.getPendlerpauschaleDto() != null) {
						dataHelp[REPORT_PERSONALLISTE_PENDLERPAUSCHALE] = personalDto
								.getPendlerpauschaleDto().getCBez();
					}

					if (personalDto.getPartnerDto_Sozialversicherer() != null) {
						dataHelp[REPORT_PERSONALLISTE_SOZIALVERSICHERER] = personalDto
								.getPartnerDto_Sozialversicherer()
								.formatAnrede();
					}
					if (personalDto.getPartnerDto_Firma() != null) {
						dataHelp[REPORT_PERSONALLISTE_FIRMENZUGEHOERIGKEIT] = personalDto
								.getPartnerDto_Firma().formatAnrede();
					}
					if (personalDto.getLandDto() != null) {
						dataHelp[REPORT_PERSONALLISTE_STAATSANGEHOERIGKEIT] = personalDto
								.getLandDto().getCName();
					}
					if (personalDto.getKostenstelleDto_Stamm() != null) {
						dataHelp[REPORT_PERSONALLISTE_HEIMATKOSTENSTELLE] = personalDto
								.getKostenstelleDto_Stamm().getCBez();
					}

					if (Helper.short2boolean(personalDto.getBMaennlich())) {
						dataHelp[REPORT_PERSONALLISTE_GESCHLECHT] = "M";
					} else {
						dataHelp[REPORT_PERSONALLISTE_GESCHLECHT] = "W";
					}

					dataHelp[REPORT_PERSONALLISTE_SOZIALVERSNR] = personalDto
							.getCSozialversnr();
					dataHelp[REPORT_PERSONALLISTE_UNTERSCHRIFTSFUNKTION] = personalDto
							.getCUnterschriftsfunktion();
					dataHelp[REPORT_PERSONALLISTE_UNTERSCHRIFTSTEXT] = personalDto
							.getCUnterschriftstext();
					dataHelp[REPORT_PERSONALLISTE_KEINE_UEBERSTUNDENAUSZAHLUNG] = Helper
							.short2Boolean(personalDto
									.getBUeberstundenausbezahlt());
					dataHelp[REPORT_PERSONALLISTE_VORNAME2] = personalDto
							.getPartnerDto().getCName3vorname2abteilung();
					dataHelp[REPORT_PERSONALLISTE_TITEL] = personalDto
							.getPartnerDto().getCTitel();
					dataHelp[REPORT_PERSONALLISTE_STRASSE] = personalDto
							.getPartnerDto().getCStrasse();
					dataHelp[REPORT_PERSONALLISTE_KURZBEZEICHNUNG] = personalDto
							.getPartnerDto().getCKbez();
					dataHelp[REPORT_PERSONALLISTE_KURZZEICHEN] = personalDto
							.getCKurzzeichen();

					dataHelp[REPORT_PERSONALLISTE_ABSENDER_DURCHWAHL] = personalDto
							.getCTelefon();

					dataHelp[REPORT_PERSONALLISTE_ABSENDER_EMAIL] = personalDto
							.getCEmail();

					dataHelp[REPORT_PERSONALLISTE_ABSENDER_HANDY] = personalDto
							.getCHandy();

					dataHelp[REPORT_PERSONALLISTE_ABSENDER_FAXDW] = personalDto
							.getCFax();

					dataHelp[REPORT_PERSONALLISTE_ABSENDER_DIREKTFAX] = personalDto
							.getCDirektfax();

					dataHelp[REPORT_PERSONALLISTE_PERSONALFUNKTION] = personalDto
							.getPersonalfunktionCNr();

					if (personalDto.getPersonalgruppeIId() != null) {

						dataHelp[REPORT_PERSONALLISTE_PERSONALGRUPPE] = personalgruppeFindByPrimaryKey(
								personalDto.getPersonalgruppeIId()).getCBez();
					}

					dataHelp[REPORT_PERSONALLISTE_TELEFON] = personalDto
							.getPartnerDto().getCTelefon();

					dataHelp[REPORT_PERSONALLISTE_EMAIL] = personalDto
							.getPartnerDto().getCEmail();

					dataHelp[REPORT_PERSONALLISTE_HANDY] = personalDto
							.getPartnerDto().getCHandy();

					dataHelp[REPORT_PERSONALLISTE_FAX] = personalDto
							.getPartnerDto().getCFax();

					dataHelp[REPORT_PERSONALLISTE_HOMEPAGE] = personalDto
							.getPartnerDto().getCHomepage();

					if (personalDto.getPartnerDto().getLandplzortDto() != null) {

						dataHelp[REPORT_PERSONALLISTE_ORT] = personalDto
								.getPartnerDto().getLandplzortDto()
								.formatLandPlzOrt();
					}

					if (personal.getT_geburtsdatum() != null) {

						Calendar cGeb = Calendar.getInstance();
						cGeb.setTimeInMillis(personal.getT_geburtsdatum()
								.getTime());
						dataHelp[REPORT_PERSONALLISTE_ALTER] = Calendar
								.getInstance().get(Calendar.YEAR)
								- cGeb.get(Calendar.YEAR);
					}

					dataHelp[REPORT_PERSONALLISTE_BILD] = Helper
							.byteArrayToImage(personalDto.getPartnerDto()
									.getOBild());

					dataHelp[REPORT_PERSONALLISTE_AUSWEISNUMMER] = personal
							.getC_ausweis();
					dataHelp[REPORT_PERSONALLISTE_ART] = personal
							.getPersonalart_c_nr();
					dataHelp[REPORT_PERSONALLISTE_KOSTENSTELLE] = personal
							.getFlrkostenstelleabteilung() == null ? null
							: personal.getFlrkostenstelleabteilung().getC_nr();
					PersonalzutrittsklasseDto dto = null;
					if (tsStichtag == null) {

						java.sql.Timestamp tHeute = Helper
								.cutTimestamp(new java.sql.Timestamp(System
										.currentTimeMillis()));

						dto = getZutrittscontrollerFac()
								.personalzutrittsklasseFindZutrittsklasseZuDatum(
										personal.getI_id(), tHeute,
										theClientDto);
					} else {
						dto = getZutrittscontrollerFac()
								.personalzutrittsklasseFindZutrittsklasseZuDatum(
										personal.getI_id(), tsStichtag,
										theClientDto);

					}

					if (dto != null) {
						dataHelp[REPORT_PERSONALLISTE_ZUTRITTSKLASSE] = dto
								.getZutrittsklasseDto().getCNr();
					}

					Query query = em
							.createNamedQuery("PersonalangehoerigefindByPersonalIId");
					query.setParameter(1, personal.getI_id());
					Collection<?> cl = query.getResultList();

					PersonalangehoerigeDto[] personalangehoerigeDtos = assemblePersonalangehoerigeDtos(cl);

					if (personalangehoerigeDtos.length > 0) {

						String[] fieldnames = new String[] { "Name", "Vorname",
								"Geburtsdatum", "Art", "Sozialversicherungsnr" };

						Object[][] dataSub = new Object[personalangehoerigeDtos.length][fieldnames.length];
						for (int i = 0; i < personalangehoerigeDtos.length; i++) {
							dataSub[i][0] = personalangehoerigeDtos[i]
									.getCName();
							dataSub[i][1] = personalangehoerigeDtos[i]
									.getCVorname();
							dataSub[i][2] = personalangehoerigeDtos[i]
									.getTGeburtsdatum();
							dataSub[i][3] = personalangehoerigeDtos[i]
									.getAngehoerigenartCNr();
							dataSub[i][4] = personalangehoerigeDtos[i]
									.getCSozialversnr();
						}

						dataHelp[REPORT_PERSONALLISTE_SUBREPORT_ANGEHOERIGE] = new LPDatenSubreport(
								dataSub, fieldnames);
					}

					PartnerbankDto[] bankverbindungDtos = getBankFac()
							.partnerbankFindByPartnerIId(
									personal.getFlrpartner().getI_id(),
									theClientDto);
					if (bankverbindungDtos.length > 0) {

						String[] fieldnames = new String[] { "Iban",
								"Kontonummer", "Blz", "Bic", "Bank" };

						Object[][] dataSub = new Object[bankverbindungDtos.length][fieldnames.length];
						for (int i = 0; i < bankverbindungDtos.length; i++) {

							BankDto bankDto = getBankFac()
									.bankFindByPrimaryKey(
											bankverbindungDtos[i]
													.getBankPartnerIId(),
											theClientDto);

							dataSub[i][0] = bankverbindungDtos[i].getCIban();
							dataSub[i][1] = bankverbindungDtos[i].getCKtonr();
							dataSub[i][2] = bankDto.getCBlz();
							dataSub[i][3] = bankDto.getCBic();
							dataSub[i][4] = bankDto.getPartnerDto()
									.formatAnrede();
						}

						dataHelp[REPORT_PERSONALLISTE_SUBREPORT_BANKVERBINDUNG] = new LPDatenSubreport(
								dataSub, fieldnames);
					}

					alDaten.add(dataHelp);
				}
			}

			if (iOptionSortierung == PersonalFac.REPORT_PERSONALLISTE_OPTION_SORTIERUNG_GEBURTSTAG) {

				for (int i = alDaten.size() - 1; i > 0; --i) {
					for (int j = 0; j < i; ++j) {
						java.util.Date d = (java.util.Date) alDaten.get(j)[REPORT_PERSONALLISTE_GEBURTSTAG];
						java.util.Date d1 = (java.util.Date) alDaten.get(j + 1)[REPORT_PERSONALLISTE_GEBURTSTAG];

						int dayOfYear = 0;
						if (d != null) {
							Calendar c = Calendar.getInstance();
							c.setTimeInMillis(d.getTime());
							dayOfYear = c.get(Calendar.DAY_OF_YEAR);
						}

						int dayOfYear1 = 0;

						if (d1 != null) {
							Calendar c = Calendar.getInstance();
							c.setTimeInMillis(d1.getTime());
							dayOfYear1 = c.get(Calendar.DAY_OF_YEAR);
						}

						if (dayOfYear > dayOfYear1) {
							Object[] h = alDaten.get(j);
							alDaten.set(j, alDaten.get(j + 1));
							alDaten.set(j + 1, h);
						}

					}
				}

				sSortierung = getTextRespectUISpr("pers.geburtstag",
						theClientDto.getMandant(), theClientDto.getLocUi());
			}

			session.close();
			data = new Object[alDaten.size()][REPORT_PERSONALLISTE_ANZAHL_SPALTEN];
			for (int i = 0; i < alDaten.size(); i++) {
				data[i] = alDaten.get(i);
			}

		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		}

		// Erstellung des Reports
		JasperPrintLP print = null;
		index = -1;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_SORTIERUNG", sSortierung);

		parameter.put("P_STICHTAG", tsStichtag);
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));

		if (bMitBarcodes == true) {
			initJRDS(parameter, PersonalFac.REPORT_MODUL,
					PersonalFac.REPORT_BARCODELISTE, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

		} else {
			initJRDS(parameter, PersonalFac.REPORT_MODUL,
					PersonalFac.REPORT_PERSONALLISTE,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
		}

		print = getReportPrint();
		return print;

	}

	/**
	 * Methode fuer JRDataSource
	 * 
	 * @return boolean
	 * @throws JRException
	 */
	public boolean next() throws JRException {

		index++;

		return (index < data.length);

	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;

		String fieldName = field.getName();
		if ("Personalnummer".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_PERSONALNUMMER];
		} else if ("Ausweisnummer".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_AUSWEISNUMMER];
		} else if ("Name".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_NAME];
		} else if ("Vorname".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_VORNAME];
		} else if ("Bild".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_BILD];
		} else if ("Art".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_ART];
		} else if ("Kostenstelle".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_KOSTENSTELLE];
		} else if ("Zutrittsklasse".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_ZUTRITTSKLASSE];
		} else if ("Geburtsdatum".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_GEBURTSTAG];
		} else if ("Alter".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_ALTER];
		}

		else if ("SubreportBankverbindung".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_SUBREPORT_BANKVERBINDUNG];
		} else if ("EintrittsdatumZumStichtag".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_EINTRITTSDATUM_ZUM_STICHTAG];
		} else if ("ZeitmodellZumStichtag".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_ZEITMODELL_ZUM_STICHTAG];
		} else if ("Familienstand".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_FAMILIENASTAND];
		} else if ("Geburtsort".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_GEBURTSORT];
		} else if ("Sozialversnr".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_SOZIALVERSNR];
		} else if ("Sozialversicherer".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_SOZIALVERSICHERER];
		} else if ("Staatsangehoerigkeit".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_STAATSANGEHOERIGKEIT];
		} else if ("Religion".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_RELIGION];
		} else if ("Kollektiv".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_KOLLEKTIV];
		} else if ("Beruf".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_BERUF];
		} else if ("Pendlerpauschale".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_PENDLERPAUSCHALE];
		} else if ("Lohngruppe".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_LOHNGRUPPE];
		} else if ("Firmenzugehoerigkeit".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_FIRMENZUGEHOERIGKEIT];
		} else if ("Unterschriftsfunktion".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_UNTERSCHRIFTSFUNKTION];
		} else if ("Unterschriftstext".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_UNTERSCHRIFTSTEXT];
		} else if ("Ueberstundenauszahlung".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_KEINE_UEBERSTUNDENAUSZAHLUNG];
		} else if ("AbsenderDurchwahl".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_ABSENDER_DURCHWAHL];
		} else if ("AbsenderEmail".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_ABSENDER_EMAIL];
		} else if ("AbsenderFaxDW".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_ABSENDER_FAXDW];
		} else if ("AbsenderDirektfax".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_ABSENDER_DIREKTFAX];
		} else if ("AbsenderHandy".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_ABSENDER_HANDY];
		} else if ("Kurzzeichen".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_KURZZEICHEN];
		} else if ("Kurzbezeichnung".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_KURZBEZEICHNUNG];
		} else if ("Titel".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_TITEL];
		} else if ("Vorname2".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_VORNAME2];
		} else if ("Geschlecht".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_GESCHLECHT];
		} else if ("Strasse".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_STRASSE];
		} else if ("Personalgruppe".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_PERSONALGRUPPE];
		} else if ("Ort".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_ORT];
		} else if ("Personalfunktion".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_PERSONALFUNKTION];
		} else if ("Heimatkostenstelle".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_HEIMATKOSTENSTELLE];
		} else if ("Telefon".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_TELEFON];
		} else if ("Fax".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_FAX];
		} else if ("Email".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_EMAIL];
		} else if ("Homepage".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_HOMEPAGE];
		} else if ("Handy".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_HANDY];
		} else if ("SubreportAngehoerige".equals(fieldName)) {
			value = data[index][REPORT_PERSONALLISTE_SUBREPORT_ANGEHOERIGE];
		}
		return value;

	}

	public Integer createZulage(ZulageDto zulageDto) throws EJBExceptionLP {
		if (zulageDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zulageDto == null"));
		}
		if (zulageDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("zulageDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("ZulagefindByCBez");
			query.setParameter(1, zulageDto.getCBez());
			Zulage doppelt = (Zulage) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_ZULAGE.C_BEZ"));
		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZULAGE);
		zulageDto.setIId(pk);

		try {
			Zulage zulage = new Zulage(zulageDto.getIId(), zulageDto.getCBez());
			em.persist(zulage);
			em.flush();
			setZulageFromZulageDto(zulage, zulageDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return zulageDto.getIId();
	}

	public void removeZulage(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			Zulage zulage = em.find(Zulage.class, iId);
			if (zulage == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(zulage);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}

	}

	public void removeZulage(ZulageDto zulageDto) throws EJBExceptionLP {
		if (zulageDto != null) {
			Integer iId = zulageDto.getIId();
			removeZulage(iId);
		}
	}

	public void updateZulage(ZulageDto zulageDto) throws EJBExceptionLP {
		if (zulageDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zulageDto == null"));
		}
		if (zulageDto.getIId() == null || zulageDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"zulageDto.getIId() == null || zulageDto.getCBez() == null"));
		}

		Integer iId = zulageDto.getIId();
		// try {
		Zulage zulage = em.find(Zulage.class, iId);
		if (zulage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("ZulagefindByCBez");
			query.setParameter(1, zulageDto.getCBez());
			Integer iIdVorhanden = ((Zulage) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZULAGE.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		}
		setZulageFromZulageDto(zulage, zulageDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ZulageDto zulageFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Zulage zulage = em.find(Zulage.class, iId);
		if (zulage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZulageDto(zulage);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setZulageFromZulageDto(Zulage zulage, ZulageDto zulageDto) {
		zulage.setCBez(zulageDto.getCBez());
		em.merge(zulage);
		em.flush();
	}

	private ZulageDto assembleZulageDto(Zulage zulage) {
		return ZulageDtoAssembler.createDto(zulage);
	}

	private ZulageDto[] assembleZulageDtos(Collection<?> zulages) {
		List<ZulageDto> list = new ArrayList<ZulageDto>();
		if (zulages != null) {
			Iterator<?> iterator = zulages.iterator();
			while (iterator.hasNext()) {
				Zulage zulage = (Zulage) iterator.next();
				list.add(assembleZulageDto(zulage));
			}
		}
		ZulageDto[] returnArray = new ZulageDto[list.size()];
		return (ZulageDto[]) list.toArray(returnArray);
	}

	public Integer createArtikelzulage(ArtikelzulageDto artikelzulageDto)
			throws EJBExceptionLP {
		if (artikelzulageDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelzulageDto == null"));
		}
		if (artikelzulageDto.getArtikelIId() == null
				|| artikelzulageDto.getZulageIId() == null
				|| artikelzulageDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikelzulageDto.getArtikelIId() == null || artikelzulageDto.getZulageIId() == null || artikelzulageDto.getTGueltigab() == null"));
		}

		Query query = em
				.createNamedQuery("ArtikelzulagefindByArtikelIIdZulageIIdTGueltigab");
		query.setParameter(1, artikelzulageDto.getArtikelIId());
		query.setParameter(2, artikelzulageDto.getZulageIId());
		query.setParameter(3, artikelzulageDto.getTGueltigab());
		try {
			Artikelzulage doppelt = (Artikelzulage) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ARTIKELZULAGE.UC"));
			}
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELZULAGE);
		artikelzulageDto.setIId(pk);

		try {
			Artikelzulage artikelzulage = new Artikelzulage(
					artikelzulageDto.getIId(),
					artikelzulageDto.getArtikelIId(),
					artikelzulageDto.getZulageIId(),
					artikelzulageDto.getTGueltigab());
			em.persist(artikelzulage);
			em.flush();
			setArtikelzulageFromArtikelzulageDto(artikelzulage,
					artikelzulageDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return artikelzulageDto.getIId();
	}

	public void removeArtikelzulage(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			Artikelzulage artikelzulage = em.find(Artikelzulage.class, iId);
			if (artikelzulage == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(artikelzulage);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeArtikelzulage(ArtikelzulageDto artikelzulageDto)
			throws EJBExceptionLP {
		if (artikelzulageDto != null) {
			Integer iId = artikelzulageDto.getIId();
			removeArtikelzulage(iId);
		}
	}

	public void schichtzeitenVorausplanen(Integer personalIId,
			Integer zeitmodellIId1, Integer zeitmodellIId2,
			Integer zeitmodellIId3, Integer zeitmodellIId4, Integer iKwVon,
			Integer iKwBis, Integer iJahrVon, Integer iJahrBis,
			String tagesartCNrSchichtwechsel) {
		if (personalIId == null || zeitmodellIId1 == null
				|| zeitmodellIId2 == null || iKwVon == null || iKwBis == null
				|| iJahrVon == null || iJahrBis == null
				|| tagesartCNrSchichtwechsel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"personalIId == null || zeitmodellIId1 == null || zeitmodellIId2 == null || tVon == null || tBis == null || iJahrVon == null || iJahrBis == null || tagesartCNrSchichtwechsel == null"));
		}
		Calendar cBis = Calendar.getInstance();
		cBis.set(Calendar.YEAR, iJahrBis);
		cBis.set(Calendar.WEEK_OF_YEAR, iKwBis);

		Calendar cAktuelleWoche = Calendar.getInstance();
		cAktuelleWoche.set(Calendar.YEAR, iJahrVon);
		cAktuelleWoche.set(Calendar.WEEK_OF_YEAR, iKwVon);

		if (tagesartCNrSchichtwechsel.equals(ZeiterfassungFac.TAGESART_MONTAG)) {
			cAktuelleWoche.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		} else if (tagesartCNrSchichtwechsel
				.equals(ZeiterfassungFac.TAGESART_DIENSTAG)) {
			cAktuelleWoche.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		} else if (tagesartCNrSchichtwechsel
				.equals(ZeiterfassungFac.TAGESART_MITTWOCH)) {
			cAktuelleWoche.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		} else if (tagesartCNrSchichtwechsel
				.equals(ZeiterfassungFac.TAGESART_DONNERSTAG)) {
			cAktuelleWoche.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		} else if (tagesartCNrSchichtwechsel
				.equals(ZeiterfassungFac.TAGESART_FREITAG)) {
			cAktuelleWoche.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		} else if (tagesartCNrSchichtwechsel
				.equals(ZeiterfassungFac.TAGESART_SAMSTAG)) {
			cAktuelleWoche.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		} else if (tagesartCNrSchichtwechsel
				.equals(ZeiterfassungFac.TAGESART_SONNTAG)) {
			cAktuelleWoche.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		}

		int iAnzahlSchichten = 0;

		if (zeitmodellIId1 != null && zeitmodellIId2 != null
				&& zeitmodellIId3 != null && zeitmodellIId4 != null) {
			iAnzahlSchichten = 4;
		} else if (zeitmodellIId1 != null && zeitmodellIId2 != null
				&& zeitmodellIId3 != null) {
			iAnzahlSchichten = 3;
		} else if (zeitmodellIId1 != null && zeitmodellIId2 != null) {
			iAnzahlSchichten = 2;
		}

		int iAktuelleSchicht = 1;

		while (cBis.getTime().after(cAktuelleWoche.getTime())) {
			// Eintragen fuer aktuelle Wochen
			Timestamp tAktuell = Helper.cutTimestamp(new Timestamp(
					cAktuelleWoche.getTimeInMillis()));

			try {
				Query query = em
						.createNamedQuery("PersonalzeitmodellfindByPersonalIIdTDatum");
				query.setParameter(1, personalIId);
				query.setParameter(2, tAktuell);
				Personalzeitmodell personalzeitmodell = (Personalzeitmodell) query
						.getSingleResult();
				// Wenns schon ein Zeitmodell gibt an diesem Tag, dann
				// auslassen
			} catch (NoResultException ex) {
				PersonalzeitmodellDto personalzeitmodellDto = new PersonalzeitmodellDto();
				personalzeitmodellDto.setPersonalIId(personalIId);
				personalzeitmodellDto.setTGueltigab(tAktuell);

				if (iAktuelleSchicht == 1) {
					personalzeitmodellDto.setZeitmodellIId(zeitmodellIId1);
				} else if (iAktuelleSchicht == 2) {
					personalzeitmodellDto.setZeitmodellIId(zeitmodellIId2);
				} else if (iAktuelleSchicht == 3) {
					personalzeitmodellDto.setZeitmodellIId(zeitmodellIId3);
				} else if (iAktuelleSchicht == 4) {
					personalzeitmodellDto.setZeitmodellIId(zeitmodellIId4);
				}
				personalzeitmodellDto.setPersonalIId(personalIId);
				createPersonalzeitmodell(personalzeitmodellDto);
			}
			cAktuelleWoche.add(Calendar.WEEK_OF_YEAR, 1);
			//
			// cAktuelleWoche.set(Calendar.WEEK_OF_YEAR,
			// cAktuelleWoche.get(Calendar.WEEK_OF_YEAR) + 1);
			iAktuelleSchicht++;
			if (iAktuelleSchicht > iAnzahlSchichten) {
				iAktuelleSchicht = 1;
			}
		}

	}

	public void updateArtikelzulage(ArtikelzulageDto artikelzulageDto)
			throws EJBExceptionLP {
		if (artikelzulageDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelzulageDto == null"));
		}
		if (artikelzulageDto.getIId() == null
				|| artikelzulageDto.getArtikelIId() == null
				|| artikelzulageDto.getZulageIId() == null
				|| artikelzulageDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikelzulageDto.getIId() == null || artikelzulageDto.getArtikelIId() == null || artikelzulageDto.getZulageIId() == null || artikelzulageDto.getTGueltigab() == null"));
		}

		Integer iId = artikelzulageDto.getIId();
		// try {
		Artikelzulage artikelzulage = em.find(Artikelzulage.class, iId);
		if (artikelzulage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em
					.createNamedQuery("ArtikelzulagefindByArtikelIIdZulageIIdTGueltigab");
			query.setParameter(1, artikelzulageDto.getArtikelIId());
			query.setParameter(2, artikelzulageDto.getZulageIId());
			query.setParameter(3, artikelzulageDto.getTGueltigab());
			Integer iIdVorhanden = ((Artikelzulage) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZULAGE.C_BEZ"));
			}

		} catch (NoResultException ex) {
			// nix
		}
		setArtikelzulageFromArtikelzulageDto(artikelzulage, artikelzulageDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ArtikelzulageDto artikelzulageFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Artikelzulage artikelzulage = em.find(Artikelzulage.class, iId);
		if (artikelzulage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleArtikelzulageDto(artikelzulage);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setArtikelzulageFromArtikelzulageDto(
			Artikelzulage artikelzulage, ArtikelzulageDto artikelzulageDto) {
		artikelzulage.setArtikelIId(artikelzulageDto.getArtikelIId());
		artikelzulage.setZulageIId(artikelzulageDto.getZulageIId());
		artikelzulage.setTGueltigab(artikelzulageDto.getTGueltigab());
		em.merge(artikelzulage);
		em.flush();
	}

	private ArtikelzulageDto assembleArtikelzulageDto(
			Artikelzulage artikelzulage) {
		return ArtikelzulageDtoAssembler.createDto(artikelzulage);
	}

	private ArtikelzulageDto[] assembleArtikelzulageDtos(
			Collection<?> artikelzulages) {
		List<ArtikelzulageDto> list = new ArrayList<ArtikelzulageDto>();
		if (artikelzulages != null) {
			Iterator<?> iterator = artikelzulages.iterator();
			while (iterator.hasNext()) {
				Artikelzulage artikelzulage = (Artikelzulage) iterator.next();
				list.add(assembleArtikelzulageDto(artikelzulage));
			}
		}
		ArtikelzulageDto[] returnArray = new ArtikelzulageDto[list.size()];
		return (ArtikelzulageDto[]) list.toArray(returnArray);
	}

	public Integer createPersonalverfuegbarkeit(
			PersonalverfuegbarkeitDto personalverfuegbarkeitDto)
			throws EJBExceptionLP {
		if (personalverfuegbarkeitDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalverfuegbarkeitDto == null"));
		}

		if (personalverfuegbarkeitDto.getPersonalIId() == null
				|| personalverfuegbarkeitDto.getArtikelIId() == null
				|| personalverfuegbarkeitDto.getFAnteilprozent() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"personalverfuegbarkeitDto.getPersonalIId() == null || personalverfuegbarkeitDto.getArtikelIId() == null || personalverfuegbarkeitDto.getFAnteilprozent() == null"));
		}

		try {
			Query query = em
					.createNamedQuery("PersonalverfuegbarkeitfindByPersonalIIdArtikelIId");
			query.setParameter(1, personalverfuegbarkeitDto.getPersonalIId());
			query.setParameter(2, personalverfuegbarkeitDto.getArtikelIId());
			// @todo getSingleResult oder getResultList ?
			Personalverfuegbarkeit doppelt = (Personalverfuegbarkeit) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_PERSONALVERFUEGBARKEIT.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PERSONALVERFUEGBARKEIT);
		personalverfuegbarkeitDto.setIId(pk);
		try {
			Personalverfuegbarkeit personalverfuegbarkeit = new Personalverfuegbarkeit(
					personalverfuegbarkeitDto.getIId(),
					personalverfuegbarkeitDto.getPersonalIId(),
					personalverfuegbarkeitDto.getArtikelIId(),
					personalverfuegbarkeitDto.getFAnteilprozent());
			em.persist(personalverfuegbarkeit);
			em.flush();
			setPersonalverfuegbarkeitFromPersonalverfuegbarkeitDto(
					personalverfuegbarkeit, personalverfuegbarkeitDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return personalverfuegbarkeitDto.getIId();
	}

	public Double getAuslastungEinerPerson(Integer personalIId)
			throws EJBExceptionLP {
		double wert = 0;
		// try {
		Query query = em
				.createNamedQuery("PersonalverfuegbarkeitfindByPersonalIId");
		query.setParameter(1, personalIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		PersonalverfuegbarkeitDto[] dtos = assemblePersonalverfuegbarkeitDtos(cl);
		for (int i = 0; i < dtos.length; i++) {
			wert += dtos[i].getFAnteilprozent();
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// ex);
		// }
		return wert;
	}

	public void removePersonalverfuegbarkeit(Integer iId) throws EJBExceptionLP {
		// try {
		Personalverfuegbarkeit toRemove = em.find(Personalverfuegbarkeit.class,
				iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public void removePersonalverfuegbarkeit(
			PersonalverfuegbarkeitDto personalverfuegbarkeitDto)
			throws EJBExceptionLP {
		if (personalverfuegbarkeitDto != null) {
			Integer iId = personalverfuegbarkeitDto.getIId();
			removePersonalverfuegbarkeit(iId);
		}
	}

	public void updatePersonalverfuegbarkeit(
			PersonalverfuegbarkeitDto personalverfuegbarkeitDto)
			throws EJBExceptionLP {
		if (personalverfuegbarkeitDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalverfuegbarkeitDto == null"));
		}

		if (personalverfuegbarkeitDto.getIId() == null
				|| personalverfuegbarkeitDto.getPersonalIId() == null
				|| personalverfuegbarkeitDto.getArtikelIId() == null
				|| personalverfuegbarkeitDto.getFAnteilprozent() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"personalverfuegbarkeitDto.getIId() == null || personalverfuegbarkeitDto.getPersonalIId() == null || personalverfuegbarkeitDto.getArtikelIId() == null || personalverfuegbarkeitDto.getFAnteilprozent() == null"));
		}

		Integer iId = personalverfuegbarkeitDto.getIId();
		// try {

		try {
			Query query = em
					.createNamedQuery("PersonalverfuegbarkeitfindByPersonalIIdArtikelIId");
			query.setParameter(1, personalverfuegbarkeitDto.getPersonalIId());
			query.setParameter(2, personalverfuegbarkeitDto.getArtikelIId());
			Integer iIdVorhanden = ((Personalverfuegbarkeit) query
					.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PERSONALVERFUEGBARKEIT.UK"));
			}

		} catch (NoResultException ex) {
			//
		}

		Personalverfuegbarkeit personalverfuegbarkeit = em.find(
				Personalverfuegbarkeit.class, iId);
		if (personalverfuegbarkeit == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setPersonalverfuegbarkeitFromPersonalverfuegbarkeitDto(
				personalverfuegbarkeit, personalverfuegbarkeitDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public PersonalverfuegbarkeitDto personalverfuegbarkeitFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Personalverfuegbarkeit personalverfuegbarkeit = em.find(
				Personalverfuegbarkeit.class, iId);
		if (personalverfuegbarkeit == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePersonalverfuegbarkeitDto(personalverfuegbarkeit);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public PersonalverfuegbarkeitDto[] personalverfuegbarkeitFindByPersonalIId(
			Integer personalIId) throws EJBExceptionLP {
		if (personalIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalIId == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("PersonalverfuegbarkeitfindByPersonalIId");
		query.setParameter(1, personalIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assemblePersonalverfuegbarkeitDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setPersonalverfuegbarkeitFromPersonalverfuegbarkeitDto(
			Personalverfuegbarkeit personalverfuegbarkeit,
			PersonalverfuegbarkeitDto personalverfuegbarkeitDto) {
		personalverfuegbarkeit.setPersonalIId(personalverfuegbarkeitDto
				.getPersonalIId());
		personalverfuegbarkeit.setArtikelIId(personalverfuegbarkeitDto
				.getArtikelIId());
		personalverfuegbarkeit.setFAnteilprozent(personalverfuegbarkeitDto
				.getFAnteilprozent());
		em.merge(personalverfuegbarkeit);
		em.flush();
	}

	private PersonalverfuegbarkeitDto assemblePersonalverfuegbarkeitDto(
			Personalverfuegbarkeit personalverfuegbarkeit) {
		return PersonalverfuegbarkeitDtoAssembler
				.createDto(personalverfuegbarkeit);
	}

	private PersonalverfuegbarkeitDto[] assemblePersonalverfuegbarkeitDtos(
			Collection<?> personalverfuegbarkeits) {
		List<PersonalverfuegbarkeitDto> list = new ArrayList<PersonalverfuegbarkeitDto>();
		if (personalverfuegbarkeits != null) {
			Iterator<?> iterator = personalverfuegbarkeits.iterator();
			while (iterator.hasNext()) {
				Personalverfuegbarkeit personalverfuegbarkeit = (Personalverfuegbarkeit) iterator
						.next();
				list.add(assemblePersonalverfuegbarkeitDto(personalverfuegbarkeit));
			}
		}
		PersonalverfuegbarkeitDto[] returnArray = new PersonalverfuegbarkeitDto[list
				.size()];
		return (PersonalverfuegbarkeitDto[]) list.toArray(returnArray);
	}

	public Integer createKollektivuestd(KollektivuestdDto kollektivuestdDto)
			throws EJBExceptionLP {
		if (kollektivuestdDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kollektivuestdDto == null"));
		}
		if (kollektivuestdDto.getTagesartIId() == null
				|| kollektivuestdDto.getKollektivIId() == null
				|| kollektivuestdDto.getBRestdestages() == null
				|| kollektivuestdDto.getUAb() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"kollektivuestdDto.getTagesartIId() == null || kollektivuestdDto.getKollektivIId() == null || kollektivuestdDto.getBRestdestages() == null || kollektivuestdDto.getUAb() == null"));
		}
		if (Helper.short2Boolean(kollektivuestdDto.getBRestdestages()) == true) {
			kollektivuestdDto.setUBis(null);
		} else {
			if (kollektivuestdDto.getUBis() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
						new Exception("kollektivuestdDto.getUBis()"));

			}
		}

		try {
			Query query = em
					.createNamedQuery("KollektivuestdfindByKollektivIIdTagesartIId");
			query.setParameter(1, kollektivuestdDto.getKollektivIId());
			query.setParameter(2, kollektivuestdDto.getTagesartIId());
			// @todo getSingleResult oder getResultList ?
			Kollektivuestd doppelt = (Kollektivuestd) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_KOLLEKTIVUESTD.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KOLLEKTIVUESTD);
		kollektivuestdDto.setIId(pk);

		try {
			Kollektivuestd kollektivuestd = new Kollektivuestd(
					kollektivuestdDto.getIId(),
					kollektivuestdDto.getKollektivIId(),
					kollektivuestdDto.getBRestdestages(),
					kollektivuestdDto.getTagesartIId(),
					kollektivuestdDto.getUAb(),
					kollektivuestdDto.getBUnterignorieren());
			em.persist(kollektivuestd);
			em.flush();
			setKollektivuestdFromKollektivuestdDto(kollektivuestd,
					kollektivuestdDto);
			return kollektivuestdDto.getIId();

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeKollektivuestd(KollektivuestdDto kollektivuestdDto)
			throws EJBExceptionLP {
		if (kollektivuestdDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kollektivuestdDto == null"));
		}
		if (kollektivuestdDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kollektivuestdDto.getIId() == null"));
		}
		try {
			Kollektivuestd kollektivuestd = em.find(Kollektivuestd.class,
					kollektivuestdDto.getIId());
			if (kollektivuestd == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(kollektivuestd);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateKollektivuestd(KollektivuestdDto kollektivuestdDto)
			throws EJBExceptionLP {
		if (kollektivuestdDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kollektivuestdDto == null"));
		}
		if (kollektivuestdDto.getIId() == null
				|| kollektivuestdDto.getTagesartIId() == null
				|| kollektivuestdDto.getKollektivIId() == null
				|| kollektivuestdDto.getBRestdestages() == null
				|| kollektivuestdDto.getUAb() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"kollektivuestdDto.getIId() == null || kollektivuestdDto.getTagesartIId() == null || kollektivuestdDto.getKollektivIId() == null || kollektivuestdDto.getBRestdestages() == null || kollektivuestdDto.getUAb() == null"));
		}

		if (Helper.short2Boolean(kollektivuestdDto.getBRestdestages()) == true) {
			kollektivuestdDto.setUBis(null);
		} else {
			if (kollektivuestdDto.getUBis() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
						new Exception("kollektivuestdDto.getUBis()"));

			}
		}

		Integer iId = kollektivuestdDto.getIId();
		// try {
		try {
			Query query = em
					.createNamedQuery("KollektivuestdfindByKollektivIIdTagesartIId");
			query.setParameter(1, kollektivuestdDto.getKollektivIId());
			query.setParameter(2, kollektivuestdDto.getTagesartIId());
			Integer iIdVorhanden = ((Kollektivuestd) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_KOLLEKTIVUESTD.UK"));
			}

		} catch (NoResultException ex) {

		}

		Kollektivuestd kollektivuestd = em.find(Kollektivuestd.class, iId);
		if (kollektivuestd == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		setKollektivuestdFromKollektivuestdDto(kollektivuestd,
				kollektivuestdDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }

	}

	public KollektivuestdDto kollektivuestdFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Kollektivuestd kollektivuestd = em.find(Kollektivuestd.class, iId);
		if (kollektivuestd == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKollektivuestdDto(kollektivuestd);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public KollektivuestdDto[] kollektivuestdFindByKollektivIId(
			Integer kollektivId) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("KollektivuestdfindByKollektivIId");
		query.setParameter(1, kollektivId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		return assembleKollektivuestdDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);

		// }
	}

	private void setKollektivuestdFromKollektivuestdDto(
			Kollektivuestd kollektivuestd, KollektivuestdDto kollektivuestdDto) {
		kollektivuestd.setKollektivIId(kollektivuestdDto.getKollektivIId());
		kollektivuestd.setUBis(kollektivuestdDto.getUBis());
		kollektivuestd.setUAb(kollektivuestdDto.getUAb());
		kollektivuestd.setBRestdestages(kollektivuestdDto.getBRestdestages());
		kollektivuestd.setTagesartIId(kollektivuestdDto.getTagesartIId());
		kollektivuestd.setBUnterignorieren(kollektivuestdDto
				.getBUnterignorieren());
		em.merge(kollektivuestd);
		em.flush();
	}

	private KollektivuestdDto assembleKollektivuestdDto(
			Kollektivuestd kollektivuestd) {
		return KollektivuestdDtoAssembler.createDto(kollektivuestd);
	}

	private KollektivuestdDto[] assembleKollektivuestdDtos(
			Collection<?> kollektivuestds) {
		List<KollektivuestdDto> list = new ArrayList<KollektivuestdDto>();
		if (kollektivuestds != null) {
			Iterator<?> iterator = kollektivuestds.iterator();
			while (iterator.hasNext()) {
				Kollektivuestd kollektivuestd = (Kollektivuestd) iterator
						.next();
				list.add(assembleKollektivuestdDto(kollektivuestd));
			}
		}
		KollektivuestdDto[] returnArray = new KollektivuestdDto[list.size()];
		return (KollektivuestdDto[]) list.toArray(returnArray);
	}

	public Integer createKollektivuestd50(
			Kollektivuestd50Dto kollektivuestd50Dto) throws EJBExceptionLP {
		if (kollektivuestd50Dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kollektivuestd50Dto == null"));
		}
		if (kollektivuestd50Dto.getTagesartIId() == null
				|| kollektivuestd50Dto.getKollektivIId() == null
				|| kollektivuestd50Dto.getBRestdestages() == null
				|| kollektivuestd50Dto.getUBis() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"kollektivuestd50Dto.getTagesartIId() == null || kollektivuestd50Dto.getKollektivIId() == null ||  kollektivuestd50Dto.getBRestdestages() == null || kollektivuestd50Dto.getUBis() == null"));
		}
		if (Helper.short2Boolean(kollektivuestd50Dto.getBRestdestages()) == true) {
			kollektivuestd50Dto.setUBis(null);
		} else {
			if (kollektivuestd50Dto.getUBis() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
						new Exception("kollektivuestd50Dto.getUBis()"));

			}
		}

		try {
			Query query = em
					.createNamedQuery("Kollektivuestd50findByKollektivIIdTagesartIId");
			query.setParameter(1, kollektivuestd50Dto.getKollektivIId());
			query.setParameter(2, kollektivuestd50Dto.getTagesartIId());
			Kollektivuestd50 doppelt = (Kollektivuestd50) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_KOLLEKTIVUESTD50.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KOLLEKTIVUESTD50);
		kollektivuestd50Dto.setIId(pk);

		try {
			Kollektivuestd50 kollektivuestd50 = new Kollektivuestd50(
					kollektivuestd50Dto.getIId(),
					kollektivuestd50Dto.getKollektivIId(),
					kollektivuestd50Dto.getBRestdestages(),
					kollektivuestd50Dto.getTagesartIId(),
					kollektivuestd50Dto.getUVon(),
					kollektivuestd50Dto.getBUnterignorieren());
			em.persist(kollektivuestd50);
			em.flush();
			setKollektivuestd50FromKollektivuestd50Dto(kollektivuestd50,
					kollektivuestd50Dto);
			return kollektivuestd50Dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeKollektivuestd50(Kollektivuestd50Dto kollektivuestd50Dto)
			throws EJBExceptionLP {
		if (kollektivuestd50Dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kollektivuestd50Dto == null"));
		}
		if (kollektivuestd50Dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kollektivuestd50Dto.getIId() == null"));
		}
		try {
			Kollektivuestd50 kollektivuestd50 = em.find(Kollektivuestd50.class,
					kollektivuestd50Dto.getIId());
			em.remove(kollektivuestd50);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}

	}

	public void updateKollektivuestd50(Kollektivuestd50Dto kollektivuestd50Dto)
			throws EJBExceptionLP {
		if (kollektivuestd50Dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kollektivuestd50Dto == null"));
		}
		if (kollektivuestd50Dto.getIId() == null
				|| kollektivuestd50Dto.getTagesartIId() == null
				|| kollektivuestd50Dto.getKollektivIId() == null
				|| kollektivuestd50Dto.getBRestdestages() == null
				|| kollektivuestd50Dto.getUBis() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"kollektivuestdDto.getIId() == null || kollektivuestdDto.getTagesartIId() == null || kollektivuestdDto.getKollektivIId() == null || kollektivuestdDto.getBRestdestages() == null || kollektivuestdDto.getUBis() == null"));
		}

		if (Helper.short2Boolean(kollektivuestd50Dto.getBRestdestages()) == true) {
			kollektivuestd50Dto.setUBis(null);
		} else {
			if (kollektivuestd50Dto.getUBis() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
						new Exception("kollektivuestd50Dto.getUBis()"));

			}
		}

		Integer iId = kollektivuestd50Dto.getIId();

		try {
			Query query = em
					.createNamedQuery("Kollektivuestd50findByKollektivIIdTagesartIId");
			query.setParameter(1, kollektivuestd50Dto.getKollektivIId());
			query.setParameter(2, kollektivuestd50Dto.getTagesartIId());
			Integer iIdVorhanden = ((Kollektivuestd50) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_KOLLEKTIVUESTD50.UK"));
			}

			// }
		} catch (NoResultException ex) {

		}

		// try {
		Kollektivuestd50 kollektivuestd50 = em
				.find(Kollektivuestd50.class, iId);
		if (kollektivuestd50 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		setKollektivuestd50FromKollektivuestd50Dto(kollektivuestd50,
				kollektivuestd50Dto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }

	}

	public Kollektivuestd50Dto kollektivuestd50FindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Kollektivuestd50 kollektivuestd50 = em
				.find(Kollektivuestd50.class, iId);
		if (kollektivuestd50 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKollektivuestd50Dto(kollektivuestd50);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public Kollektivuestd50Dto kollektivuestd50FindByKollektivIIdTagesartIId(
			Integer kollektivIId, Integer tagesartIId) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("Kollektivuestd50findByKollektivIIdTagesartIId");
			query.setParameter(1, kollektivIId);
			query.setParameter(2, tagesartIId);
			Kollektivuestd50 kollektivuestd50 = (Kollektivuestd50) query
					.getSingleResult();
			return assembleKollektivuestd50Dto(kollektivuestd50);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);

		}
	}

	public Kollektivuestd50Dto[] kollektivuestd50FindByKollektivIId(
			Integer kollektivIId) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("Kollektivuestd50findByKollektivIId");
		query.setParameter(1, kollektivIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		return assembleKollektivuestd50Dtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);

		// }
	}

	private void setKollektivuestd50FromKollektivuestd50Dto(
			Kollektivuestd50 kollektivuestd50,
			Kollektivuestd50Dto kollektivuestd50Dto) {
		kollektivuestd50.setKollektivIId(kollektivuestd50Dto.getKollektivIId());
		kollektivuestd50.setUVon(kollektivuestd50Dto.getUVon());
		kollektivuestd50.setUBis(kollektivuestd50Dto.getUBis());
		kollektivuestd50.setBRestdestages(kollektivuestd50Dto
				.getBRestdestages());
		kollektivuestd50.setTagesartIId(kollektivuestd50Dto.getTagesartIId());
		kollektivuestd50.setBUnterignorieren(kollektivuestd50Dto
				.getBUnterignorieren());
		em.merge(kollektivuestd50);
		em.flush();
	}

	private Kollektivuestd50Dto assembleKollektivuestd50Dto(
			Kollektivuestd50 kollektivuestd50) {
		return Kollektivuestd50DtoAssembler.createDto(kollektivuestd50);
	}

	private Kollektivuestd50Dto[] assembleKollektivuestd50Dtos(
			Collection<?> kollektivuestd50s) {
		List<Kollektivuestd50Dto> list = new ArrayList<Kollektivuestd50Dto>();
		if (kollektivuestd50s != null) {
			Iterator<?> iterator = kollektivuestd50s.iterator();
			while (iterator.hasNext()) {
				Kollektivuestd50 kollektivuestd50 = (Kollektivuestd50) iterator
						.next();
				list.add(assembleKollektivuestd50Dto(kollektivuestd50));
			}
		}
		Kollektivuestd50Dto[] returnArray = new Kollektivuestd50Dto[list.size()];
		return (Kollektivuestd50Dto[]) list.toArray(returnArray);
	}

	public Integer createPersonalgruppekosten(
			PersonalgruppekostenDto personalgruppekostenDto) {
		if (personalgruppekostenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalgruppekostenDto == null"));
		}
		if (personalgruppekostenDto.getPersonalgruppeIId() == null
				|| personalgruppekostenDto.getNStundensatz() == null
				|| personalgruppekostenDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"personalgruppekostenDto.getPersonalgruppeIId() == null || personalgruppekostenDto.getNStundensatz() == null || personalgruppekostenDto.getTGueltigab() == null"));
		}

		personalgruppekostenDto.setTGueltigab(Helper
				.cutTimestamp(personalgruppekostenDto.getTGueltigab()));

		try {
			Query query = em
					.createNamedQuery("PersonalgruppekostenfindByPersonalgruppeIIdTGueltigab");
			query.setParameter(1,
					personalgruppekostenDto.getPersonalgruppeIId());
			query.setParameter(2, personalgruppekostenDto.getTGueltigab());
			Personalgruppekosten doppelt = (Personalgruppekosten) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_PERSONALGRUPPEKOSTEN.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen
					.getNextPrimaryKey(PKConst.PK_PERSONALGRUPPEKOSTEN);
			personalgruppekostenDto.setIId(pk);

			Personalgruppekosten personalgruppekosten = new Personalgruppekosten(
					personalgruppekostenDto.getIId(),
					personalgruppekostenDto.getPersonalgruppeIId(),
					personalgruppekostenDto.getTGueltigab(),
					personalgruppekostenDto.getNStundensatz());
			em.persist(personalgruppekosten);
			em.flush();
			setPersonalgruppekostenFromPersonalgruppekostenDto(
					personalgruppekosten, personalgruppekostenDto);
			return personalgruppekostenDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removePersonalgruppekosten(
			PersonalgruppekostenDto personalgruppekostenDto) {
		if (personalgruppekostenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalgruppekostenDto == null"));
		}
		if (personalgruppekostenDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalgruppekostenDto.getIId() == null"));
		}

		Personalgruppekosten toRemove = em.find(Personalgruppekosten.class,
				personalgruppekostenDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public String getSignatur(Integer personalIId, String locale) {
		try {
			Query query = em
					.createNamedQuery("SignaturfindByPersonalIIdLocaleCNr");
			query.setParameter(1, personalIId);
			query.setParameter(2, locale);
			Signatur signatur = (Signatur) query.getSingleResult();

			return signatur.getXSignatur();

		} catch (NoResultException ex) {
			return null;
		}
	}

	public void updateSignatur(Integer personalIId, String xSignatur,
			TheClientDto theClientDto) {

		try {
			Query query = em
					.createNamedQuery("SignaturfindByPersonalIIdLocaleCNr");
			query.setParameter(1, personalIId);
			query.setParameter(2, theClientDto.getLocUiAsString());
			Signatur signatur = (Signatur) query.getSingleResult();

			if (xSignatur == null) {
				em.remove(signatur);
			} else {
				signatur.setXSignatur(xSignatur);
				em.merge(signatur);
				em.flush();
			}

		} catch (NoResultException ex) {
			if (xSignatur != null) {
				PKGeneratorObj pkGen = new PKGeneratorObj();
				Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SIGNATUR);

				Signatur signatur = new Signatur(pk,
						theClientDto.getLocUiAsString(), personalIId, xSignatur);
				em.merge(signatur);
				em.flush();
			}

		}

	}

	public void updatePersonalgruppekosten(
			PersonalgruppekostenDto personalgruppekostenDto) {
		if (personalgruppekostenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalgruppekostenDto == null"));
		}
		if (personalgruppekostenDto.getIId() == null
				|| personalgruppekostenDto.getPersonalgruppeIId() == null
				|| personalgruppekostenDto.getNStundensatz() == null
				|| personalgruppekostenDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"personalgruppekostenDto.getIId() == null || personalgruppekostenDto.getPersonalgruppeIId() == null || personalgruppekostenDto.getNStundensatz() == null || personalgruppekostenDto.getTGueltigab() == null"));
		}

		personalgruppekostenDto.setTGueltigab(Helper
				.cutTimestamp(personalgruppekostenDto.getTGueltigab()));

		Integer iId = personalgruppekostenDto.getIId();
		try {
			Query query = em
					.createNamedQuery("PersonalgruppekostenfindByPersonalgruppeIIdTGueltigab");
			query.setParameter(1,
					personalgruppekostenDto.getPersonalgruppeIId());
			query.setParameter(2, personalgruppekostenDto.getTGueltigab());
			Integer iIdVorhanden = ((Personalgruppekosten) query
					.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PERSONALGRUPPEKOSTEN.UK"));
			}
		} catch (NoResultException ex) {
			//
		}
		try {
			Personalgruppekosten personalgruppekosten = em.find(
					Personalgruppekosten.class, iId);
			setPersonalgruppekostenFromPersonalgruppekostenDto(
					personalgruppekosten, personalgruppekostenDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}

	}

	public PersonalgruppekostenDto personalgruppekostenFindByPrimaryKey(
			Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Personalgruppekosten personalgruppekosten = em.find(
				Personalgruppekosten.class, iId);
		if (personalgruppekosten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePersonalgruppekostenDto(personalgruppekosten);

	}

	public BigDecimal getPersonalgruppeKostenZumZeitpunkt(
			Integer personalgruppeIId, java.sql.Timestamp tDatum) {
		// try {
		Query query = em
				.createNamedQuery("PersonalgruppekostenfindLetzeKostenByPersonalgruppeIIdTGueltigab");
		query.setParameter(1, personalgruppeIId);
		query.setParameter(2, tDatum);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return new BigDecimal(0);
		// } else {
		PersonalgruppekostenDto[] personalgruppekostenDtos = assemblePersonalgruppekostenDtos(cl);

		if (personalgruppekostenDtos != null
				&& personalgruppekostenDtos.length > 0) {
			return personalgruppekostenDtos[0].getNStundensatz();
		} else {
			return new BigDecimal(0);
		}
		// }
		// catch (NoResultException ex) {
		// return new BigDecimal(0);
		// }
	}

	public PersonalgruppekostenDto personalgruppekostenFindByPersonalgruppeIIdTGueltigab(
			Integer personalgruppeIId, Timestamp tGueltigab) {
		try {
			Query query = em
					.createNamedQuery("PersonalgruppekostenfindByPersonalgruppeIIdTGueltigab");
			query.setParameter(1, personalgruppeIId);
			query.setParameter(2, tGueltigab);
			return assemblePersonalgruppekostenDto((Personalgruppekosten) query
					.getSingleResult());
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	private void setPersonalgruppekostenFromPersonalgruppekostenDto(
			Personalgruppekosten personalgruppekosten,
			PersonalgruppekostenDto personalgruppekostenDto) {
		personalgruppekosten.setPersonalgruppeIId(personalgruppekostenDto
				.getPersonalgruppeIId());
		personalgruppekosten.setTGueltigab(personalgruppekostenDto
				.getTGueltigab());
		personalgruppekosten.setNStundensatz(personalgruppekostenDto
				.getNStundensatz());
		em.merge(personalgruppekosten);
		em.flush();
	}

	private PersonalgruppekostenDto assemblePersonalgruppekostenDto(
			Personalgruppekosten personalgruppekosten) {
		return PersonalgruppekostenDtoAssembler.createDto(personalgruppekosten);
	}

	private PersonalgruppekostenDto[] assemblePersonalgruppekostenDtos(
			Collection<?> personalgruppekostens) {
		List<PersonalgruppekostenDto> list = new ArrayList<PersonalgruppekostenDto>();
		if (personalgruppekostens != null) {
			Iterator<?> iterator = personalgruppekostens.iterator();
			while (iterator.hasNext()) {
				Personalgruppekosten personalgruppekosten = (Personalgruppekosten) iterator
						.next();
				list.add(assemblePersonalgruppekostenDto(personalgruppekosten));
			}
		}
		PersonalgruppekostenDto[] returnArray = new PersonalgruppekostenDto[list
				.size()];
		return (PersonalgruppekostenDto[]) list.toArray(returnArray);
	}

	public Integer createPersonalgruppe(PersonalgruppeDto personalgruppeDto) {
		if (personalgruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalgruppeDto == null"));
		}
		if (personalgruppeDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("personalgruppeDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("PersonalgruppefindByCBez");
			query.setParameter(1, personalgruppeDto.getCBez());
			Personalgruppe doppelt = (Personalgruppe) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_PERSONALGRUPPE.C_BEZ"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PERSONALGRUPPE);
			personalgruppeDto.setIId(pk);

			Personalgruppe personalgruppe = new Personalgruppe(
					personalgruppeDto.getIId(), personalgruppeDto.getCBez());
			em.persist(personalgruppe);
			em.flush();
			setPersonalgruppeFromPersonalgruppeDto(personalgruppe,
					personalgruppeDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return personalgruppeDto.getIId();
	}

	public void removePersonalgruppe(PersonalgruppeDto personalgruppeDto) {
		if (personalgruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalgruppeDto == null"));
		}
		if (personalgruppeDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalgruppeDto.getIId() == null"));
		}

		Personalgruppe toRemove = em.find(Personalgruppe.class,
				personalgruppeDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updatePersonalgruppe(PersonalgruppeDto personalgruppeDto) {
		if (personalgruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("personalgruppeDto == null"));
		}
		if (personalgruppeDto.getIId() == null
				|| personalgruppeDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"personalgruppeDto.getIId() == null || personalgruppeDto.getCBez() == null"));
		}
		Integer iId = personalgruppeDto.getIId();

		Personalgruppe personalgruppe = em.find(Personalgruppe.class, iId);
		if (personalgruppe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("PersonalgruppefindByCBez");
			query.setParameter(1, personalgruppeDto.getCBez());
			Integer iIdVorhanden = ((Personalgruppe) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_PERSONALGRUPPE.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		}

		setPersonalgruppeFromPersonalgruppeDto(personalgruppe,
				personalgruppeDto);

	}

	public PersonalgruppeDto personalgruppeFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Personalgruppe personalgruppe = em.find(Personalgruppe.class, iId);
		if (personalgruppe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePersonalgruppeDto(personalgruppe);

	}

	private void setPersonalgruppeFromPersonalgruppeDto(
			Personalgruppe personalgruppe, PersonalgruppeDto personalgruppeDto) {
		personalgruppe.setCBez(personalgruppeDto.getCBez());
		em.merge(personalgruppe);
		em.flush();
	}

	private PersonalgruppeDto assemblePersonalgruppeDto(
			Personalgruppe personalgruppe) {
		return PersonalgruppeDtoAssembler.createDto(personalgruppe);
	}

	private PersonalgruppeDto[] assemblePersonalgruppeDtos(
			Collection<?> personalgruppes) {
		List<PersonalgruppeDto> list = new ArrayList<PersonalgruppeDto>();
		if (personalgruppes != null) {
			Iterator<?> iterator = personalgruppes.iterator();
			while (iterator.hasNext()) {
				Personalgruppe personalgruppe = (Personalgruppe) iterator
						.next();
				list.add(assemblePersonalgruppeDto(personalgruppe));
			}
		}
		PersonalgruppeDto[] returnArray = new PersonalgruppeDto[list.size()];
		return (PersonalgruppeDto[]) list.toArray(returnArray);
	}

	public Integer createBereitschaftart(BereitschaftartDto dto) {

		try {
			Query query = em
					.createNamedQuery(Bereitschaftart.QueryFindByMandantCNrCBez);
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			Bereitschaftart doppelt = (Bereitschaftart) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_BEREITSCHAFTART.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BEREITSCHAFTART);
			dto.setIId(pk);

			Bereitschaftart bean = new Bereitschaftart(dto.getIId(),
					dto.getMandantCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setBereitschaftartFromIsBereitschaftartDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public BereitschaftartDto bereitschaftartFindByPrimaryKey(Integer iId) {
		Bereitschaftart ialle = em.find(Bereitschaftart.class, iId);
		return BereitschaftartDtoAssembler.createDto(ialle);
	}

	public void updateBereitschaftart(BereitschaftartDto dto) {
		Bereitschaftart ialle = em.find(Bereitschaftart.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery(Bereitschaftart.QueryFindByMandantCNrCBez);
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Bereitschaftart) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_WEBSHOP.UK"));
			}
		} catch (NoResultException ex) {

		}

		setBereitschaftartFromIsBereitschaftartDto(ialle, dto);
	}

	public void removeBereitschaftart(BereitschaftartDto dto) {
		Bereitschaftart toRemove = em.find(Bereitschaftart.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	private void setBereitschaftartFromIsBereitschaftartDto(
			Bereitschaftart bean, BereitschaftartDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	public Integer createBereitschafttag(BereitschafttagDto dto) {

		try {
			Query query = em
					.createNamedQuery("BereitschafttagfindByBereitschaftartIIdTagesartIIdUBeginn");
			query.setParameter(1, dto.getBereitschaftartIId());
			query.setParameter(2, dto.getTagesartIId());
			query.setParameter(3, dto.getUBeginn());
			Bereitschafttag doppelt = (Bereitschafttag) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_BEREITSCHAFTTAG.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		if (Helper.short2Boolean(dto.getBEndedestages()) == true) {
			dto.setUEnde(null);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BEREITSCHAFTTAG);
			dto.setIId(pk);

			Bereitschafttag bean = new Bereitschafttag(dto.getIId(),
					dto.getBereitschaftartIId(), dto.getTagesartIId(),
					dto.getUBeginn(), dto.getBEndedestages());
			em.persist(bean);
			em.flush();
			setBereitschafttagFromIsBereitschafttagDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public BereitschafttagDto bereitschafttagFindByPrimaryKey(Integer iId) {
		Bereitschafttag ialle = em.find(Bereitschafttag.class, iId);
		return BereitschafttagDtoAssembler.createDto(ialle);
	}

	public void updateBereitschafttag(BereitschafttagDto dto) {
		Bereitschafttag ialle = em.find(Bereitschafttag.class, dto.getIId());
		if (Helper.short2Boolean(dto.getBEndedestages()) == true) {
			dto.setUEnde(null);
		}
		try {
			Query query = em
					.createNamedQuery("BereitschafttagfindByBereitschaftartIIdTagesartIIdUBeginn");
			query.setParameter(1, dto.getBereitschaftartIId());
			query.setParameter(2, dto.getTagesartIId());
			query.setParameter(3, dto.getUBeginn());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Bereitschafttag) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_WEBSHOP.UK"));
			}
		} catch (NoResultException ex) {

		}

		setBereitschafttagFromIsBereitschafttagDto(ialle, dto);
	}

	public void removeBereitschafttag(BereitschafttagDto dto) {
		Bereitschafttag toRemove = em.find(Bereitschafttag.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	private void setBereitschafttagFromIsBereitschafttagDto(
			Bereitschafttag bean, BereitschafttagDto dto) {
		bean.setBereitschaftartIId(dto.getBereitschaftartIId());
		bean.setTagesartIId(dto.getTagesartIId());
		bean.setUBeginn(dto.getUBeginn());
		bean.setUEnde(dto.getUEnde());
		bean.setBEndedestages(dto.getBEndedestages());
		em.merge(bean);
		em.flush();
	}

	public BigDecimal getKMKostenInZielwaehrung(Integer fahrzeugIId,
			java.util.Date datGueltigkeitsdatumI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("FahrzeugkostenfindAktuelleKosten");
		query.setParameter(1, fahrzeugIId);
		query.setParameter(2,
				new java.sql.Date(datGueltigkeitsdatumI.getTime()));
		query.setMaxResults(1);
		Collection<?> cl = query.getResultList();

		FahrzeugkostenDto[] dtos = FahrzeugkostenDtoAssembler.createDtos(cl);
		if (dtos.length > 0) {

			if (!waehrungCNrZielwaehrung.equals(theClientDto
					.getSMandantenwaehrung())) {
				try {
					BigDecimal kosten = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									dtos[0].getNKmkosten(),
									theClientDto.getSMandantenwaehrung(),
									waehrungCNrZielwaehrung,
									new Date(System.currentTimeMillis()),
									theClientDto);

					dtos[0].setNKmkosten(kosten);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);

				}
			}
			return dtos[0].getNKmkosten();
		} else {
			return null;
		}

	}

	public PersonalDto[] personalFindByMandantCNrWithEmail(String mandantCNr,
			boolean bPlusVersteckte) throws EJBExceptionLP {
		List<Personal> personals = PersonalQuery.listByMandantCnrWithEmail(em,
				mandantCNr);
		return assemblePersonalDtos(personals, bPlusVersteckte);
	}

}
