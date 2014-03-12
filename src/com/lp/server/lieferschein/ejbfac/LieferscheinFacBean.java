/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.lieferschein.ejbfac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.ejb.Lieferscheinart;
import com.lp.server.lieferschein.ejb.Lieferscheinartspr;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.ejb.Lieferscheinpositionart;
import com.lp.server.lieferschein.ejb.Lieferscheinstatus;
import com.lp.server.lieferschein.ejb.Lieferscheintext;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.ILieferscheinAviso;
import com.lp.server.lieferschein.service.ILieferscheinAvisoProducer;
import com.lp.server.lieferschein.service.LieferscheinAvisoCC;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinartDto;
import com.lp.server.lieferschein.service.LieferscheinartDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.lieferschein.service.LieferscheinpositionartDto;
import com.lp.server.lieferschein.service.LieferscheinpositionartDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinstatusDto;
import com.lp.server.lieferschein.service.LieferscheinstatusDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheintextDto;
import com.lp.server.lieferschein.service.LieferscheintextDtoAssembler;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLARTICLEID;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLBUYERAID;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLBUYERPARTY;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLCONTROLINFO;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLDELIVERYDATE;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLDISPATCHNOTIFICATION;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLDISPATCHNOTIFICATIONHEADER;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLDISPATCHNOTIFICATIONINFO;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLDISPATCHNOTIFICATIONITEM;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLDISPATCHNOTIFICATIONITEMLIST;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLDISPATCHNOTIFICATIONSUMMARY;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLORDERREFERENCE;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLPARTY;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLPARTYID;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLREMARK;
import com.lp.server.schema.opentrans.cc.dispatchnotification.XMLXMLSUPPLIERPARTY;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.ejb.Versandweg;
import com.lp.server.system.ejbfac.BelegAktivierungController;
import com.lp.server.system.ejbfac.CleverCureProducer;
import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferschein;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.IVersandwegPartnerDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandwegCCPartnerDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperWebshop;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.service.Artikelset;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class LieferscheinFacBean extends Facade implements LieferscheinFac, IAktivierbar {
	@PersistenceContext
	private EntityManager em;

	// Lieferschein
	// --------------------------------------------------------------

	/**
	 * Anlegen eines neuen Lieferscheinkopfs in der DB. <br>
	 * Ein Lieferschein kann zu mehreren Auftraegen liefern. <br>
	 * Der Bezug zu einem Auftrag wird in einer Kreuztabelle eingetragen.
	 * 
	 * @param lieferscheinDtoI
	 *            die Daten des Lieferscheins
	 * @param theClientDto
	 *            der aktuelle User
	 * @throws EJBExceptionLP
	 * @return Integer PK des neuen Lieferscheins
	 */
	public Integer createLieferschein(LieferscheinDto lieferscheinDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinDto(lieferscheinDtoI);
		Integer lieferscheinIId = null;
		String lieferscheinCNr = null;

		try {
			// Generieren von PK + Belegnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(lieferscheinDtoI.getMandantCNr());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					lieferscheinDtoI.getMandantCNr(),
					lieferscheinDtoI.getTBelegdatum());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(
					iGeschaeftsjahr, PKConst.PK_LIEFERSCHEIN,
					lieferscheinDtoI.getMandantCNr(), theClientDto);

			lieferscheinIId = bnr.getPrimaryKey();
			lieferscheinCNr = f.format(bnr);

			lieferscheinDtoI.setIId(lieferscheinIId);
			lieferscheinDtoI.setCNr(lieferscheinCNr);
			lieferscheinDtoI
					.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			lieferscheinDtoI
					.setPersonalIIdAendern(theClientDto.getIDPersonal());
			Lieferschein lieferschein = new Lieferschein(
					lieferscheinDtoI.getIId(), lieferscheinDtoI.getCNr(),
					lieferscheinDtoI.getLieferscheinartCNr(),
					lieferscheinDtoI.getTBelegdatum(),
					lieferscheinDtoI.getKundeIIdLieferadresse(),
					lieferscheinDtoI.getPersonalIIdVertreter(),
					lieferscheinDtoI.getKundeIIdRechnungsadresse(),
					lieferscheinDtoI.getCBezProjektbezeichnung(),
					lieferscheinDtoI.getCBestellnummer(),
					lieferscheinDtoI.getKostenstelleIId(),
					lieferscheinDtoI.getTLiefertermin(),
					lieferscheinDtoI.getTRueckgabetermin(),
					lieferscheinDtoI.getLieferartIId(),
					lieferscheinDtoI.getPersonalIIdAnlegen(),
					lieferscheinDtoI.getPersonalIIdAendern(),
					lieferscheinDtoI.getMandantCNr(),
					lieferscheinDtoI.getStatusCNr(),
					lieferscheinDtoI.getLagerIId(),
					LocaleFac.BELEGART_LIEFERSCHEIN,
					lieferscheinDtoI.getWaehrungCNr(),
					lieferscheinDtoI
							.getFWechselkursmandantwaehrungzubelegwaehrung(),
					lieferscheinDtoI.getZahlungszielIId(),
					lieferscheinDtoI.getSpediteurIId(),
					lieferscheinDtoI.getLieferscheintextIIdDefaultKopftext(),
					lieferscheinDtoI.getLieferscheintextIIdDefaultFusstext());
			em.persist(lieferschein);
			em.flush();

			lieferscheinDtoI.setTAnlegen(lieferschein.getTAnlegen());
			lieferscheinDtoI.setTAendern(lieferschein.getTAendern());
			setLieferscheinFromLieferscheinDto(lieferschein, lieferscheinDtoI);

			// PJ14938
			ParametermandantDto parametermandantautoDebitDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_AUTOMATISCHE_DEBITORENNUMMER);
			if ((Boolean) parametermandantautoDebitDto.getCWertAsObject() == true) {
				if (lieferscheinDtoI.getKundeIIdRechnungsadresse() != null) {

					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							lieferscheinDtoI.getKundeIIdRechnungsadresse(),
							theClientDto);
					if (kundeDto.getIidDebitorenkonto() == null) {
						KontoDto ktoDto = getKundeFac()
								.createDebitorenkontoZuKundenAutomatisch(
										lieferscheinDtoI
												.getKundeIIdRechnungsadresse(),
										false, null, theClientDto);

						kundeDto.setIDebitorenkontoAsIntegerNotiId(new Integer(
								ktoDto.getCNr()));
						getKundeFac().updateKunde(kundeDto, theClientDto);

					}
				}
			}

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		HvDtoLogger<LieferscheinDto> lsLogger = new HvDtoLogger<LieferscheinDto>(
				em, lieferscheinDtoI.getIId(),  theClientDto);
		lsLogger.logInsert(lieferscheinDtoI);
		
		return lieferscheinIId;
	}

	/**
	 * Einen bestehenden Lieferschein aktualisieren. <br>
	 * Ein eventuelle Bezug zu Auftraegen bleibt dabei unberuecksichtig.
	 * 
	 * @param lieferscheinDtoI
	 *            der aktuelle Lieferschein
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateLieferschein(LieferscheinDto lieferscheinDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinDto(lieferscheinDtoI);
		lieferscheinDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
		lieferscheinDtoI.setTAendern(getTimestamp());
		Lieferschein lieferschein = em.find(Lieferschein.class,
				lieferscheinDtoI.getIId());
		if (lieferschein == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLieferscheinFromLieferscheinDto(lieferschein, lieferscheinDtoI);
		pruefeUndSetzeLieferscheinstatusBeiAenderung(lieferscheinDtoI.getIId());
		myLogger.exit("Der Lieferschein wurde aktualisiert, Status: "
				+ lieferschein.getLieferscheinstatusCNr());
	}
	
	public void updateTAendern(Integer lieferscheinIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		Lieferschein lieferschein = em.find(Lieferschein.class, lieferscheinIId);
		lieferschein.setTAendern(getTimestamp());
		lieferschein.setPersonalIIdAendern(theClientDto.getIDPersonal());
		em.merge(lieferschein);
		em.flush();
	}
	/**
	 * Einen bestehenden Lieferschein aktualisieren. <br>
	 * Ein Lieferschein kann zu mehreren Auftraegen liefern. <br>
	 * Der Bezug zu einem Auftrag wird in einer Kreuztabelle eingetragen.
	 * 
	 * @param lieferscheinDtoI
	 *            die Daten des Lieferscheins
	 * @param aAuftragIIdI
	 *            der Bezug zu 0..n Auftraegen
	 * @param waehrungOriCNrI
	 *            die urspruengliche Belegwaehrung
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public boolean updateLieferschein(LieferscheinDto lieferscheinDtoI,
			Integer[] aAuftragIIdI, String waehrungOriCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinDto(lieferscheinDtoI);

		boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = false;

		try {
			// wenn die Waehrung geaendert wurde, muessen die Belegwerte neu
			// berechnet werden
			if (waehrungOriCNrI != null
					&& !waehrungOriCNrI.equals(lieferscheinDtoI
							.getWaehrungCNr())) {
				LieferscheinpositionDto[] aLieferscheinpositionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(
								lieferscheinDtoI.getIId());

				// die Positionswerte neu berechnen und abspeichern
				BigDecimal ffWechselkurs = getLocaleFac().getWechselkurs2(
						waehrungOriCNrI,
						lieferscheinDtoI.getWaehrungCNr(),
						theClientDto);

				for (int i = 0; i < aLieferscheinpositionDto.length; i++) {
					if (aLieferscheinpositionDto[i].getNMenge() != null
							&& aLieferscheinpositionDto[i].getNEinzelpreis() != null) {
						BigDecimal nNettoeinzelpreisInNeuerWaehrung = aLieferscheinpositionDto[i]
								.getNEinzelpreis().multiply(ffWechselkurs);

						VerkaufspreisDto verkaufspreisDto = getVkPreisfindungFac()
								.berechnePreisfelder(
										nNettoeinzelpreisInNeuerWaehrung,
										aLieferscheinpositionDto[i]
												.getFRabattsatz(),
										aLieferscheinpositionDto[i]
												.getFZusatzrabattsatz(),
										aLieferscheinpositionDto[i]
												.getMwstsatzIId(), 4, // @todo
										// Konstante
										// PJ
										// 4390
										theClientDto);

						aLieferscheinpositionDto[i]
								.setNEinzelpreis(verkaufspreisDto.einzelpreis);
						aLieferscheinpositionDto[i]
								.setNRabattbetrag(verkaufspreisDto.rabattsumme);
						aLieferscheinpositionDto[i]
								.setNNettoeinzelpreis(verkaufspreisDto.nettopreis);
						aLieferscheinpositionDto[i]
								.setNMwstbetrag(verkaufspreisDto.mwstsumme);
						aLieferscheinpositionDto[i]
								.setNBruttoeinzelpreis(verkaufspreisDto.bruttopreis);

						// alle Preisfelder incl. der zusaetzlichen Preisfelder
						// befuellen
						getLieferscheinpositionFac()
								.updateLieferscheinpositionOhneWeitereAktion(
										aLieferscheinpositionDto[i],
										theClientDto);
					}
				}
			}

			lieferscheinDtoI
					.setPersonalIIdAendern(theClientDto.getIDPersonal());
			lieferscheinDtoI.setTAendern(getTimestamp());

			Lieferschein lieferschein = em.find(Lieferschein.class,
					lieferscheinDtoI.getIId());
			if (lieferschein == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			
			LieferscheinDto lsDto_vorher = lieferscheinFindByPrimaryKey(
					lieferscheinDtoI.getIId());
			HvDtoLogger<LieferscheinDto> erLogger = new HvDtoLogger<LieferscheinDto>(
					em, lieferscheinDtoI.getIId(), theClientDto);
			erLogger.log(lsDto_vorher, lieferscheinDtoI);
			
			// Wird der kunde geaendert muss man die Konditionen neu holen

			// CK: 2013-06-04 Gilt nicht mehr, da die Konditionen nun
			// vorher am Client bestaetigt werden muessen
			/*
			 * if (!lieferscheinDtoI.getKundeIIdLieferadresse().equals(
			 * lieferschein.getKundeIIdLieferadresse())) { KundeDto kundeDto =
			 * getKundeFac().kundeFindByPrimaryKey(
			 * lieferscheinDtoI.getKundeIIdLieferadresse(), theClientDto);
			 * Double dAllgemeinerrabattsatz = new Double(0); if
			 * (kundeDto.getFRabattsatz() != null) { dAllgemeinerrabattsatz =
			 * kundeDto.getFRabattsatz(); } lieferscheinDtoI
			 * .setFAllgemeinerRabattsatz(dAllgemeinerrabattsatz); if
			 * (kundeDto.getLieferartIId() != null) { lieferscheinDtoI
			 * .setLieferartIId(kundeDto.getLieferartIId()); } if
			 * (kundeDto.getZahlungszielIId() != null) {
			 * lieferscheinDtoI.setZahlungszielIId(kundeDto
			 * .getZahlungszielIId()); } if (kundeDto.getSpediteurIId() != null)
			 * { lieferscheinDtoI .setSpediteurIId(kundeDto.getSpediteurIId());
			 * } }
			 */
			if (!lieferscheinDtoI.getKundeIIdRechnungsadresse().equals(
					lieferschein.getKundeIIdRechnungsadresse())) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						lieferscheinDtoI.getKundeIIdRechnungsadresse(),
						theClientDto);
				KundeDto kundeDtoVorher = getKundeFac().kundeFindByPrimaryKey(
						lieferschein.getKundeIIdRechnungsadresse(),
						theClientDto);

				ParametermandantDto parameterPositionskontierung = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_KUNDEN,
								ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);

				boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung
						.getCWertAsObject();

				LieferscheinpositionDto[] aLieferscheinpositionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(
								lieferscheinDtoI.getIId());
				for (int i = 0; i < aLieferscheinpositionDto.length; i++) {
					if (aLieferscheinpositionDto[i]
							.getPositionsartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)
							|| aLieferscheinpositionDto[i]
									.getPositionsartCNr()
									.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {

						MwstsatzDto mwstsatzDto = getMandantFac()
								.mwstsatzFindByMwstsatzbezIIdAktuellster(
										kundeDto.getMwstsatzbezIId(),
										theClientDto);
						if (bDefaultMwstsatzAusArtikel
								&& aLieferscheinpositionDto[i]
										.getPositionsartCNr()
										.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											aLieferscheinpositionDto[i]
													.getArtikelIId(),
											theClientDto);
							if (artikelDto.getMwstsatzbezIId() != null) {
								mwstsatzDto = getMandantFac()
										.mwstsatzFindByMwstsatzbezIIdAktuellster(
												artikelDto.getMwstsatzbezIId(),
												theClientDto);
							}

						}

						// SP503
						if (bDefaultMwstsatzAusArtikel
								&& aLieferscheinpositionDto[i]
										.getPositionsartCNr()
										.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {

							// Wenn alter und neuer Kunde den gleichen MWST-Satz
							// haben, dann nichts tun
							MwstsatzDto mwstsatzDtoKundeNeu = getMandantFac()
									.mwstsatzFindByMwstsatzbezIIdAktuellster(
											kundeDto.getMwstsatzbezIId(),
											theClientDto);

							MwstsatzDto mwstsatzDtoKundeVorher = getMandantFac()
									.mwstsatzFindByMwstsatzbezIIdAktuellster(
											kundeDtoVorher.getMwstsatzbezIId(),
											theClientDto);

							if (mwstsatzDtoKundeVorher.getFMwstsatz() == 0
									&& mwstsatzDtoKundeNeu.getFMwstsatz() > 0) {

								bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = true;
							}

							if (mwstsatzDtoKundeNeu.getIId().equals(
									mwstsatzDtoKundeVorher.getIId())) {
								continue;
							}
						}

						if (!aLieferscheinpositionDto[i].getMwstsatzIId()
								.equals(mwstsatzDto.getIId())) {
							aLieferscheinpositionDto[i]
									.setMwstsatzIId(mwstsatzDto.getIId());

							BigDecimal mwstBetrag = aLieferscheinpositionDto[i]
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
									.multiply(
											new BigDecimal(mwstsatzDto
													.getFMwstsatz()
													.doubleValue())
													.movePointLeft(2));
							aLieferscheinpositionDto[i]
									.setNMwstbetrag(mwstBetrag);
							aLieferscheinpositionDto[i]
									.setNBruttoeinzelpreis(mwstBetrag.add(aLieferscheinpositionDto[i]
											.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
							getLieferscheinpositionFac()
									.updateLieferscheinpositionOhneWeitereAktion(
											aLieferscheinpositionDto[i],
											theClientDto);
						}
					}
				}

				Double dAllgemeinerrabattsatz = new Double(0);
				if (kundeDto.getFRabattsatz() != null) {
					dAllgemeinerrabattsatz = kundeDto.getFRabattsatz();
				}
				lieferscheinDtoI
						.setFAllgemeinerRabattsatz(dAllgemeinerrabattsatz);
				if (kundeDto.getLieferartIId() != null) {
					lieferscheinDtoI
							.setLieferartIId(kundeDto.getLieferartIId());
				}
				if (kundeDto.getZahlungszielIId() != null) {
					lieferscheinDtoI.setZahlungszielIId(kundeDto
							.getZahlungszielIId());
				}
				if (kundeDto.getSpediteurIId() != null) {
					lieferscheinDtoI
							.setSpediteurIId(kundeDto.getSpediteurIId());
				}
			}

			if (!lieferschein.getTBelegdatum().equals(
					lieferscheinDtoI.getTBelegdatum())) {
				try {
					getLagerFac().updateTBelegdatumEinesBelegesImLager(
							LocaleFac.BELEGART_LIEFERSCHEIN,
							lieferscheinDtoI.getIId(),lieferscheinDtoI.getTBelegdatum(), theClientDto);
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}
			}

			setLieferscheinFromLieferscheinDto(lieferschein, lieferscheinDtoI);

			// die Zuordnung zu den Auftraegen kann geaendert werden, solange es
			// keine
			// mengenbehafteten Positionen gibt
			if (lieferscheinDtoI.getLieferscheinartCNr().equals(
					LieferscheinFac.LSART_AUFTRAG)) {
				if (aAuftragIIdI == null || aAuftragIIdI.length == 0) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
							new Exception(
									"Lieferscheinart ist Auftrag, aber aAuftragIIdI ist leer."));
				}

			}

			pruefeUndSetzeLieferscheinstatusBeiAenderung(lieferscheinDtoI
					.getIId());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben;
	}

	/**
	 * @deprecated use lieferscheinFindByPrimaryKey(Integer) instead
	 */
	public LieferscheinDto lieferscheinFindByPrimaryKey(
			Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LieferscheinDto lieferscheinDto = null;
		// Lieferschein holen
		Lieferschein lieferschein = em.find(Lieferschein.class,
				iIdLieferscheinI);
		if (lieferschein == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lieferscheinDto = assembleLieferscheinDto(lieferschein);
		return lieferscheinDto;
	}

	/**
	 * Den LieferscheinDto aufgrund seiner IId liefern
	 * 
	 * Die urspr&uuml;ngliche Methode war die mit (Integer, TheClientDto). Das
	 * ClientDto braucht nur niemand, es gibt aber so viele Methoden, die
	 * bereits die vorherige Methode benutzen.
	 * 
	 * @param iIdLieferscheinI
	 *            is die Lieferschein-IId fuer die der dazugehoerige
	 *            LieferscheinDto ermittelt werden soll
	 */
	public LieferscheinDto lieferscheinFindByPrimaryKey(Integer iIdLieferscheinI)
			throws EJBExceptionLP {
		LieferscheinDto lieferscheinDto = null;

		lieferscheinDto = lieferscheinFindByPrimaryKeyOhneExc(iIdLieferscheinI);
		if (lieferscheinDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return lieferscheinDto;
	}

	public LieferscheinDto lieferscheinFindByPrimaryKeyOhneExc(
			Integer iIdLieferscheinI) {

		Lieferschein lieferschein = em.find(Lieferschein.class,
				iIdLieferscheinI);
		if (lieferschein == null) {
			return null;
		}

		return assembleLieferscheinDto(lieferschein);
	}

	public LieferscheinDto lieferscheinFindByCNrMandantCNr(String cNr,
			String mandantCNr) {
		Query query = em.createNamedQuery("LieferscheinfindByCNrMandantCNr");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCNr);
		try {
			Lieferschein lieferschein = (Lieferschein) query.getSingleResult();
			return assembleLieferscheinDto(lieferschein);
		} catch (NoResultException ex) {
			return null;
		}
	}

	/**
	 * Alle Lieferscheine zu einem bestimmten Auftrag finden.
	 * 
	 * @param iIdAuftragI
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return LieferscheinDto[]
	 */
	public LieferscheinDto[] lieferscheinFindByAuftrag(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Suchen aller LS-Positionen, die sich auf diesen Auftrag beziehen.
			Criteria c = session.createCriteria(FLRLieferscheinposition.class);
			c.createCriteria(
					LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRPOSITIONENSICHTAUFTRAG)
					.add(Restrictions
							.eq(AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAG_I_ID,
									iIdAuftragI));
			// Query ausfuehren
			List<?> list = c.list();
			// Damit das ganze "distinct" wird, geb ich die Auftrags-ID's in
			// eine Hashmap.
			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRLieferscheinposition item = (FLRLieferscheinposition) iter
						.next();
				hm.put(item.getFlrlieferschein().getI_id(), item
						.getFlrlieferschein().getI_id());
			}
			// in der HM stehen jetzt die Auftrags-ID's.
			LieferscheinDto[] aLieferscheinDto = new LieferscheinDto[hm.size()];
			int index = 0;
			for (Iterator<?> iter = hm.keySet().iterator(); iter.hasNext();) {
				Integer item = (Integer) iter.next();
				aLieferscheinDto[index] = lieferscheinFindByPrimaryKey(item,
						theClientDto);
				index++;
			}
			return aLieferscheinDto;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public LieferscheinDto[] lieferscheinFindByKundeIIdLieferadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LieferscheinDto[] aLieferscheinDtos = null;
		Query query = em
				.createNamedQuery("LieferscheinfindByKundeIIdLieferadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> lieferscheinDtos = query.getResultList();
		aLieferscheinDtos = assembleLieferscheinDtos(lieferscheinDtos);
		return aLieferscheinDtos;
	}

	public LieferscheinDto[] lieferscheinFindByKundeIIdLieferadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto) {
		LieferscheinDto[] aLieferscheinDtos = null;
		try {
			Query query = em
					.createNamedQuery("LieferscheinfindByKundeIIdLieferadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			aLieferscheinDtos = assembleLieferscheinDtos(query.getResultList());
		} catch (Throwable ex) {
			// nothing
		}
		return aLieferscheinDtos;
	}

	public LieferscheinDto[] lieferscheinFindByKundeIIdRechnungsadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LieferscheinDto[] aLieferscheinDtos = null;
		Query query = em
				.createNamedQuery("LieferscheinfindByKundeIIdRechnungsadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> lieferscheinDtos = query.getResultList();
		aLieferscheinDtos = assembleLieferscheinDtos(lieferscheinDtos);
		return aLieferscheinDtos;
	}

	public LieferscheinDto[] lieferscheinFindByKundeIIdRechnungsadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto) {
		LieferscheinDto[] aLieferscheinDtos = null;
		try {
			Query query = em
					.createNamedQuery("LieferscheinfindByKundeIIdRechnungsadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			aLieferscheinDtos = assembleLieferscheinDtos(query.getResultList());
		} catch (Throwable ex) {
			// nothing
		}
		return aLieferscheinDtos;
	}

	public LieferscheinDto[] lieferscheinFindByAnsprechpartnerIIdMandantCNr(
			Integer iIdAnsprechpartnerI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheinDto[] aLieferscheinDtos = null;
		Query query = em
				.createNamedQuery("LieferscheinfindByAnsprechpartnerIIdMandantCNr");
		query.setParameter(1, iIdAnsprechpartnerI);
		query.setParameter(2, cNrMandantI);
		Collection<?> lieferscheinDtos = query.getResultList();
		aLieferscheinDtos = assembleLieferscheinDtos(lieferscheinDtos);
		return aLieferscheinDtos;
	}

	public LieferscheinDto[] lieferscheinFindByAnsprechpartnerIIdMandantCNrOhneExc(
			Integer iIdAnsprechpartnerI, String cNrMandantI,
			TheClientDto theClientDto) {
		LieferscheinDto[] aLieferscheinDtos = null;
		try {
			Query query = em
					.createNamedQuery("LieferscheinfindByAnsprechpartnerIIdMandantCNr");
			query.setParameter(1, iIdAnsprechpartnerI);
			query.setParameter(2, cNrMandantI);
			aLieferscheinDtos = assembleLieferscheinDtos(query.getResultList());
		} catch (Throwable th) {
			return null;
		}
		return aLieferscheinDtos;
	}

	private void checkLieferscheinDto(LieferscheinDto lieferscheinDtoI)
			throws EJBExceptionLP {
		if (lieferscheinDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lieferscheinDto == null"));
		}

		myLogger.info("LieferscheinDto: " + lieferscheinDtoI.toString());
	}

	private void checkLieferscheinIId(Integer iIdLieferscheinI)
			throws EJBExceptionLP {
		Validator.notNull(iIdLieferscheinI, "iIdLieferscheinI");

		myLogger.info("LieferscheinIId: " + iIdLieferscheinI.toString());
	}

	public void toggleZollexportpapiereErhalten(Integer lieferscheinIId,
			String cZollexportpapier, Integer eingangsrechnungIId_Zollexport,
			TheClientDto theClientDto) {
		Lieferschein lieferschein = em.find(Lieferschein.class,
				lieferscheinIId);

		
		LieferscheinDto lsDto_vorher = lieferscheinFindByPrimaryKey(lieferscheinIId);
		LieferscheinDto lsDto_nachher = lieferscheinFindByPrimaryKey(lieferscheinIId);
		
		if (lieferschein.getTZollexportpapier() == null) {
			lieferschein.setTZollexportpapier(new Timestamp(System
					.currentTimeMillis()));
			lieferschein.setPersonalIIdZollexportpapier(theClientDto
					.getIDPersonal());
			lieferschein.setCZollexportpapier(cZollexportpapier);
			lieferschein
					.setEingangsrechnungIdZollexport(eingangsrechnungIId_Zollexport);
			
			
			
			lsDto_nachher.setTZollexportpapier(new Timestamp(System
					.currentTimeMillis()));
			lsDto_nachher.setPersonalIIdZollexportpapier(theClientDto
					.getIDPersonal());
			lsDto_nachher.setCZollexportpapier(cZollexportpapier);
			lsDto_nachher
					.setEingangsrechnungIdZollexport(eingangsrechnungIId_Zollexport);
			
		} else {
			lieferschein.setTZollexportpapier(null);
			lieferschein.setPersonalIIdZollexportpapier(null);
			lieferschein.setCZollexportpapier(null);
			lieferschein.setEingangsrechnungIdZollexport(null);
			
			lsDto_nachher.setTZollexportpapier(null);
			lsDto_nachher.setPersonalIIdZollexportpapier(null);
			lsDto_nachher.setCZollexportpapier(null);
			lsDto_nachher.setEingangsrechnungIdZollexport(null);
			
			
			
			
		}
		
		HvDtoLogger<LieferscheinDto> lsLogger = new HvDtoLogger<LieferscheinDto>(
				em, lsDto_vorher.getIId(), theClientDto);
		lsLogger.log(lsDto_vorher, lsDto_nachher);
		
	}
	
	private void setLieferscheinFromLieferscheinDto(Lieferschein lieferschein,
			LieferscheinDto lieferscheinDto) {
		if (lieferscheinDto.getCNr() != null) {
			lieferschein.setCNr(lieferscheinDto.getCNr());
		}
		if (lieferscheinDto.getMandantCNr() != null) {
			lieferschein.setMandantCNr(lieferscheinDto.getMandantCNr());
		}
		if (lieferscheinDto.getLieferscheinartCNr() != null) {
			lieferschein.setLieferscheinartCNr(lieferscheinDto
					.getLieferscheinartCNr());
		}
		if (lieferscheinDto.getStatusCNr() != null) {
			lieferschein.setLieferscheinstatusCNr(lieferscheinDto
					.getStatusCNr());
		}
		if (lieferscheinDto.getBelegartCNr() != null) {
			lieferschein.setBelegartCNr(lieferscheinDto.getBelegartCNr());
		}
		if (lieferscheinDto.getBVerrechenbar() != null) {
			lieferschein.setBVerrechenbar(lieferscheinDto.getBVerrechenbar());
		}
		if (lieferscheinDto.getTBelegdatum() != null) {
			lieferschein.setTBelegdatum(lieferscheinDto.getTBelegdatum());
		}
		if (lieferscheinDto.getKundeIIdLieferadresse() != null) {
			lieferschein.setKundeIIdLieferadresse(lieferscheinDto
					.getKundeIIdLieferadresse());
		}
		// der Ansprechpartner kann null sein
		lieferschein.setAnsprechpartnerIIdKunde(lieferscheinDto
				.getAnsprechpartnerIId());

		if (lieferscheinDto.getPersonalIIdVertreter() != null) {
			lieferschein.setPersonalIIdVertreter(lieferscheinDto
					.getPersonalIIdVertreter());
		}
		if (lieferscheinDto.getKundeIIdRechnungsadresse() != null) {
			lieferschein.setKundeIIdRechnungsadresse(lieferscheinDto
					.getKundeIIdRechnungsadresse());
		}

		lieferschein.setCBez(lieferscheinDto.getCBezProjektbezeichnung());
		lieferschein.setCBestellnummer(lieferscheinDto.getCBestellnummer());

		if (lieferscheinDto.getLagerIId() != null) {
			lieferschein.setLagerIId(lieferscheinDto.getLagerIId());
		}
		lieferschein.setZiellagerIId(lieferscheinDto.getZiellagerIId());
		if (lieferscheinDto.getKostenstelleIId() != null) {
			lieferschein.setKostenstelleIId(lieferscheinDto
					.getKostenstelleIId());
		}
		lieferschein.setTLiefertermin(lieferscheinDto.getTLiefertermin());
		lieferschein.setTRueckgabetermin(lieferscheinDto.getTRueckgabetermin());
		if (lieferscheinDto.getFVersteckterAufschlag() != null) {
			lieferschein.setFVersteckteraufschlag(lieferscheinDto
					.getFVersteckterAufschlag());
		}
		if (lieferscheinDto.getFAllgemeinerRabattsatz() != null) {
			lieferschein.setFAllgemeinerrabatt(lieferscheinDto
					.getFAllgemeinerRabattsatz());
		}
		if (lieferscheinDto.getLieferartIId() != null) {
			lieferschein.setLieferartIId(lieferscheinDto.getLieferartIId());
		}
		if (lieferscheinDto.getBMindermengenzuschlag() != null) {
			lieferschein.setBMindermengenzuschlag(lieferscheinDto
					.getBMindermengenzuschlag());
		}
		if (lieferscheinDto.getIAnzahlPakete() != null) {
			lieferschein.setIAnzahlpakete(lieferscheinDto.getIAnzahlPakete());
		}
		if (lieferscheinDto.getFGewichtLieferung() != null) {
			lieferschein.setFGewichtlieferung(lieferscheinDto
					.getFGewichtLieferung());
		}
		if (lieferscheinDto.getCVersandnummer() != null) {
			lieferschein.setCVersandnummer(lieferscheinDto.getCVersandnummer());
		}
		if (lieferscheinDto.getTGedruckt() != null) {
			lieferschein.setTGedruckt(lieferscheinDto.getTGedruckt());
		}
		if (lieferscheinDto.getPersonalIIdManuellErledigt() != null) {
			lieferschein.setPersonalIIdManuellerledigt(lieferscheinDto
					.getPersonalIIdManuellErledigt());
		}
		if (lieferscheinDto.getTManuellErledigt() != null) {
			lieferschein.setTManuellerledigt(lieferscheinDto
					.getTManuellErledigt());
		}

		if (lieferscheinDto.getPersonalIIdStorniert() != null) {
			lieferschein.setPersonalIIdStorniert(lieferscheinDto
					.getPersonalIIdStorniert());
		}
		if (lieferscheinDto.getTStorniert() != null) {
			lieferschein.setTStorniert(lieferscheinDto.getTStorniert());
		}

		lieferschein.setPersonalIIdAnlegen(lieferscheinDto
				.getPersonalIIdAnlegen());
		lieferschein.setTAnlegen(lieferscheinDto.getTAnlegen());
		lieferschein.setPersonalIIdAendern(lieferscheinDto
				.getPersonalIIdAendern());
		lieferschein.setTAendern(lieferscheinDto.getTAendern());

		if (lieferscheinDto.getWaehrungCNr() != null) {
			lieferschein.setWaehrungCNrLieferscheinwaehrung(lieferscheinDto
					.getWaehrungCNr());
		}
		if (lieferscheinDto.getFWechselkursmandantwaehrungzubelegwaehrung() != null) {
			lieferschein
					.setFWechselkursmandantwaehrungzulieferscheinwaehrung(lieferscheinDto
							.getFWechselkursmandantwaehrungzubelegwaehrung());
		}
		lieferschein.setNGesamtwertinlieferscheinwaehrung(lieferscheinDto
				.getNGesamtwertInLieferscheinwaehrung());
		lieferschein.setNGestehungswertinmandantenwaehrung(lieferscheinDto
				.getNGestehungswertInMandantenwaehrung());
		if (lieferscheinDto.getZahlungszielIId() != null) {
			lieferschein.setZahlungszielIId(lieferscheinDto
					.getZahlungszielIId());
		}
		if (lieferscheinDto.getSpediteurIId() != null) {
			lieferschein.setSpediteurIId(lieferscheinDto.getSpediteurIId());
		}
		lieferschein.setLieferscheintextIIdKopftext(lieferscheinDto
				.getLieferscheintextIIdDefaultKopftext());
		lieferschein.setLieferscheintextIIdFusstext(lieferscheinDto
				.getLieferscheintextIIdDefaultFusstext());
		lieferschein.setXKopftextuebersteuert(lieferscheinDto
				.getCLieferscheinKopftextUeberschrieben());
		lieferschein.setXFusstextuebersteuert(lieferscheinDto
				.getCLieferscheinFusstextUeberschrieben());
		lieferschein.setRechnungIId(lieferscheinDto.getRechnungIId());
		lieferschein.setAuftragIId(lieferscheinDto.getAuftragIId());
		lieferschein.setCKommission(lieferscheinDto.getCKommission());
		lieferschein.setBegruendungIId(lieferscheinDto.getBegruendungIId());
		lieferschein.setAnsprechpartnerIIdRechnungsadresse(lieferscheinDto
				.getAnsprechpartnerIIdRechnungsadresse());
		lieferschein.setCLieferartort(lieferscheinDto.getCLieferartort());
		lieferschein.setProjektIId(lieferscheinDto.getProjektIId());
		lieferschein.setTZollexportpapier(lieferscheinDto
				.getTZollexportpapier());
		lieferschein.setPersonalIIdZollexportpapier(lieferscheinDto
				.getPersonalIIdZollexportpapier());
		lieferschein.setEingangsrechnungIdZollexport(lieferscheinDto
				.getEingangsrechnungIdZollexport());
		lieferschein.setCZollexportpapier(lieferscheinDto
				.getCZollexportpapier());
		lieferschein.setTLieferaviso(lieferscheinDto.getTLieferaviso());
		lieferschein.setPersonalIIdLieferaviso(lieferscheinDto.getPersonalIIdLieferaviso());
		em.merge(lieferschein);
		em.flush();
	}

	private LieferscheinDto assembleLieferscheinDto(Lieferschein lieferschein) {
		return LieferscheinDtoAssembler.createDto(lieferschein);
	}

	private LieferscheinDto[] assembleLieferscheinDtos(
			Collection<?> lieferscheins) {
		List<LieferscheinDto> list = new ArrayList<LieferscheinDto>();
		if (lieferscheins != null) {
			Iterator<?> iterator = lieferscheins.iterator();
			while (iterator.hasNext()) {
				Lieferschein lieferschein = (Lieferschein) iterator.next();
				list.add(assembleLieferscheinDto(lieferschein));
			}
		}
		LieferscheinDto[] returnArray = new LieferscheinDto[list.size()];
		return (LieferscheinDto[]) list.toArray(returnArray);
	}

	// Lieferscheinstatus
	// --------------------------------------------------------

	/**
	 * Einen Lieferscheinstatus in seiner Uebersetzung holen.
	 * 
	 * @param pStatus
	 *            String
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return String
	 */
	public String getLieferscheinstatus(String pStatus, Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		final String METHOD_NAME = "getLieferscheinstatus";
		myLogger.entry();
		// @todo check parameter PJ 4398
		String uebersetzung = null;
		try {
			Lieferscheinstatus status = this.em.find(Lieferscheinstatus.class,
					pStatus);
			if (status == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			LieferscheinstatusDto statusDto = this
					.assembleLieferscheinstatusDto(status);
			// uebersetzung von system holen
			uebersetzung = getSystemMultilanguageFac().uebersetzeStatusOptimal(
					pStatus, locale1, locale2);
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		return uebersetzung;
	}

	public String getLieferscheinCNr(Object[] keys) throws EJBExceptionLP {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			Lieferschein ls = this.em.find(Lieferschein.class,
					(Integer) keys[i]);
			if (i == keys.length - 1)
				sb.append(ls.getCNr());
			else {
				sb.append(ls.getCNr());
				sb.append(",");
			}
		}
		return sb.toString();
	}

	private LieferscheinstatusDto assembleLieferscheinstatusDto(
			Lieferscheinstatus pStatus) {
		return LieferscheinstatusDtoAssembler.createDto(pStatus);
	}

	// Lieferscheinpositionart
	// ---------------------------------------------------

	public Map getLieferscheinpositionart(Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		final String METHOD_NAME = "getLieferscheinpositionart";
		myLogger.entry();
		// @todo param PJ 4398
		Map<?, ?> map = null;
		try {
			Query query = em.createNamedQuery("LieferscheinpositionartfindAll");
			Collection<?> arten = query.getResultList();
			LieferscheinpositionartDto[] artenDtos = this
					.assembleLieferscheinpositionartDtos(arten);
			map = getSystemMultilanguageFac().uebersetzePositionsartOptimal(
					artenDtos, locale1, locale2);
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		return map;
	}

	private LieferscheinpositionartDto assembleLieferscheinpositionartDto(
			Lieferscheinpositionart lieferscheinpositionart) {
		return LieferscheinpositionartDtoAssembler
				.createDto(lieferscheinpositionart);
	}

	private LieferscheinpositionartDto[] assembleLieferscheinpositionartDtos(
			Collection<?> lieferscheinpositionarts) {
		List<LieferscheinpositionartDto> list = new ArrayList<LieferscheinpositionartDto>();
		if (lieferscheinpositionarts != null) {
			Iterator<?> iterator = lieferscheinpositionarts.iterator();
			while (iterator.hasNext()) {
				Lieferscheinpositionart lieferscheinpositionart = (Lieferscheinpositionart) iterator
						.next();
				list.add(assembleLieferscheinpositionartDto(lieferscheinpositionart));
			}
		}
		LieferscheinpositionartDto[] returnArray = new LieferscheinpositionartDto[list
				.size()];
		return (LieferscheinpositionartDto[]) list.toArray(returnArray);
	}

	// Lieferscheinart
	// -----------------------------------------------------------

	/**
	 * Alle Lieferscheinarten in der bestmoeglichen Uebersetzung holen.
	 * 
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getLieferscheinart(Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		final String METHOD_NAME = "getLieferscheinart";
		myLogger.entry();
		Map<String, String> map = null;
		Query query = em.createNamedQuery("LieferscheinartfindAll");
		Collection<?> arten = query.getResultList();
		LieferscheinartDto[] artDtos = this.assembleLieferscheinartDtos(arten);
		map = this.uebersetzeLieferscheinartOptimal(artDtos, locale1, locale2);
		return map;
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen fuer ein Array von
	 * Lieferscheinarten.
	 * 
	 * @param pArray
	 *            Stati
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	private Map<String, String> uebersetzeLieferscheinartOptimal(
			LieferscheinartDto[] pArray, Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		final String METHOD_NAME = "";
		myLogger.entry();

		// @todo check param PJ 4398

		Map<String, String> uebersetzung = new TreeMap<String, String>();

		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = uebersetzeLieferscheinartOptimal(pArray[i].getCNr(),
					locale1, locale2);
			uebersetzung.put(key, value);
		}

		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Lieferscheinart optimal. 1.Versuch: mit locale1
	 * 2.Versuch: mit locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            der Name des Lieferscheins
	 * @param locale1
	 *            das bevorzugte Locale
	 * @param locale2
	 *            das Ersatzlocale
	 * @throws EJBExceptionLP
	 * @return String die Lieferarten mit ihrer Uebersetzung
	 */
	private String uebersetzeLieferscheinartOptimal(String cNr, Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		myLogger.entry();

		String uebersetzung = "";

		try {
			uebersetzung = uebersetzeLieferscheinart(locale1, cNr);
		} catch (Throwable t1) {
			try {
				uebersetzung = uebersetzeLieferscheinart(locale2, cNr);
			} catch (Throwable t2) {
				uebersetzung = cNr;
			}
		}

		return uebersetzung;
	}

	private LieferscheinartDto assembleLieferscheinartDto(
			Lieferscheinart lieferscheinart) {
		return LieferscheinartDtoAssembler.createDto(lieferscheinart);
	}

	private LieferscheinartDto[] assembleLieferscheinartDtos(
			Collection<?> lieferscheinarts) {
		List<LieferscheinartDto> list = new ArrayList<LieferscheinartDto>();
		if (lieferscheinarts != null) {
			Iterator<?> iterator = lieferscheinarts.iterator();
			while (iterator.hasNext()) {
				Lieferscheinart lieferscheinart = (Lieferscheinart) iterator
						.next();
				list.add(assembleLieferscheinartDto(lieferscheinart));
			}
		}
		LieferscheinartDto[] returnArray = new LieferscheinartDto[list.size()];
		return (LieferscheinartDto[]) list.toArray(returnArray);
	}

	// Lieferscheinartspr
	// --------------------------------------------------------

	/**
	 * Eine Lieferscheinart in eine bestimmte Sprache uebersetzen.
	 * 
	 * @param pLocale
	 *            Locale
	 * @param pArt
	 *            String
	 * @throws EJBExceptionLP
	 * @return String
	 */
	private String uebersetzeLieferscheinart(Locale pLocale, String pArt)
			throws EJBExceptionLP {
		myLogger.entry();
		Lieferscheinartspr spr = null;
		String locale = Helper.locale2String(pLocale);
		Query query = em
				.createNamedQuery("LieferscheinartsprfindBySpracheAndCNr");
		query.setParameter(1, locale);
		query.setParameter(2, pArt);
		spr = (Lieferscheinartspr) query.getSingleResult();
		if (spr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return spr.getCBez();
	}

	// Lieferscheinposition
	// ------------------------------------------------------

	private void checkLieferscheinpositionDto(
			LieferscheinpositionDto lieferscheinpositionDto)
			throws EJBExceptionLP {
		if (lieferscheinpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lieferscheinpositionDto == null"));
		}
	}

	/**
	 * Aus einem Lieferschein jene Position heraussuchen, die zu einer
	 * bestimmten Auftragposition gehoert.
	 * 
	 * @param iIdLieferscheinI
	 *            pk des Lieferscheins
	 * @param iIdAuftragpositionI
	 *            pk der Auftragposition
	 * @throws EJBExceptionLP
	 * @return LieferscheinpositionDto die entsprechende Position, null wenn es
	 *         keine gibt
	 */
	public LieferscheinpositionDto getLieferscheinpositionByLieferscheinAuftragposition(
			Integer iIdLieferscheinI, Integer iIdAuftragpositionI)
			throws EJBExceptionLP {
		myLogger.entry();

		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}

		if (iIdAuftragpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragpositionI == null"));
		}

		LieferscheinpositionDto oLieferscheinpositionDtoO = null;

		try {
			Query query = em
					.createNamedQuery("LieferscheinpositionfindByLieferscheinIIdAuftragpositionIId");
			query.setParameter(1, iIdLieferscheinI);
			query.setParameter(2, iIdAuftragpositionI);
			oLieferscheinpositionDtoO = assembleLieferscheinpositionDto((Lieferscheinposition) query
					.getSingleResult());
		} catch (Throwable t) {
			// es gibt keine Lieferscheinposition mit diesen Eigenschaften
		}

		return oLieferscheinpositionDtoO;
	}

	private LieferscheinpositionDto assembleLieferscheinpositionDto(
			Lieferscheinposition lieferscheinposition) {

		LieferscheinpositionDto lieferscheinpositionDto = LieferscheinpositionDtoAssembler
				.createDto(lieferscheinposition);
		lieferscheinpositionDto.setSeriennrChargennrMitMenge(getLagerFac()
				.getAllSeriennrchargennrEinerBelegartposition(
						LocaleFac.BELEGART_LIEFERSCHEIN,
						lieferscheinpositionDto.getIId()));

		return lieferscheinpositionDto;
	}

	private LieferscheinpositionDto[] assembleLieferscheinpositionDtos(
			Collection<?> lieferscheinpositions) {
		List<LieferscheinpositionDto> list = new ArrayList<LieferscheinpositionDto>();
		if (lieferscheinpositions != null) {
			Iterator<?> iterator = lieferscheinpositions.iterator();
			while (iterator.hasNext()) {
				Lieferscheinposition lieferscheinposition = (Lieferscheinposition) iterator
						.next();
				list.add(assembleLieferscheinpositionDto(lieferscheinposition));
			}
		}
		LieferscheinpositionDto[] returnArray = new LieferscheinpositionDto[list
				.size()];
		return (LieferscheinpositionDto[]) list.toArray(returnArray);
	}

	/**
	 * Den Status eines Lieferscheins auf 'Geliefert' setzen und seinen Wert
	 * berechnen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            String der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @throws RemoteException 
	 */
	@Override
	public void aktiviereLieferschein(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		myLogger.entry();
		Validator.notNull(iIdLieferscheinI, "IdLieferscheinI");
		pruefeAktivierbar(iIdLieferscheinI, theClientDto);
		//Wert berechnen
		berechneBeleg(iIdLieferscheinI, theClientDto);
		//und Status aendern
		aktiviereBeleg(iIdLieferscheinI, theClientDto);
	}
	
	@Override
	public void pruefeAktivierbar(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Lieferschein oLieferschein = em.find(Lieferschein.class, iid);
		if (oLieferschein == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// Ohne Positionen darf der Beleg nicht aktiviert werden.
		if (getLieferscheinpositionFac()
				.berechneAnzahlMengenbehaftetePositionen(iid,
						theClientDto) == 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN, "");
		}
	}
	
	@Override
	public void aktiviereBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Lieferschein oLieferschein = em.find(Lieferschein.class, iid);
		if (oLieferschein.getLieferscheinstatusCNr().equals(
				LieferscheinFac.LSSTATUS_ANGELEGT)) {
			oLieferschein.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
			// den Druckzeitpunkt vermerken
			oLieferschein.setTGedruckt(getTimestamp());
		}
	}
	
	@Override
	public void berechneBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Lieferschein oLieferschein = em.find(Lieferschein.class, iid);
		if (oLieferschein.getLieferscheinstatusCNr().equals(
				LieferscheinFac.LSSTATUS_ANGELEGT)) {
			BigDecimal nGesamtWertInlieferscheinwaehrung = berechneGesamtwertLieferscheinAusPositionen(
					oLieferschein.getIId(), theClientDto);
			nGesamtWertInlieferscheinwaehrung = Helper.rundeKaufmaennisch(
					nGesamtWertInlieferscheinwaehrung, 4);
			oLieferschein
					.setNGesamtwertinlieferscheinwaehrung(nGesamtWertInlieferscheinwaehrung);
			BigDecimal nGestehungwert = null;
			if (oLieferschein.getZiellagerIId() != null) {
				nGestehungwert = null;
			} else {
				nGestehungwert = berechneGestehungswert(oLieferschein.getIId(),
						theClientDto);
			}
			oLieferschein
					.setNGestehungswertinmandantenwaehrung(nGestehungwert);
		}
	}

	/**
	 * Der Status eines Lieferscheins kann von aussen gesetzt werden. <br>
	 * Diese Methode wird von der Rechnung verwendet, um einen Lieferschein zu
	 * verrechnen oder den Status auf 'Geliefert' zurueckzusetzen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param iIdRechnungI
	 *            die verrechnende Rechnung, wenn der Lieferschein auf Status
	 *            Verrechnet gesetzt wird, sonst null
	 * @param sStatusI
	 *            der Status
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void setzeStatusLieferschein(Integer iIdLieferscheinI,
			String sStatusI, Integer iIdRechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}

		if (sStatusI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sStatusI == null"));
		}
		if (sStatusI.equals(LieferscheinFac.LSSTATUS_VERRECHNET)) {
			if (iIdRechnungI == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
						new Exception(
								"sStatusI.equals('Verrechnet') && iIdRechnungI == null"));
			}
		}

		if (sStatusI.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
			if (iIdRechnungI != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_PARAMETER_IS_NOT_NULL,
						new Exception(
								"sStatusI.equals('Geliefert') && iIdRechnungI != null"));
			}
		}

		Lieferschein ls = em.find(Lieferschein.class, iIdLieferscheinI);
		if (ls == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		ls.setLieferscheinstatusCNr(sStatusI);

		if (sStatusI.equals(LieferscheinFac.LSSTATUS_VERRECHNET)) {
			ls.setRechnungIId(iIdRechnungI);
		}
		// bei Statuswechsel von 'Verrechnet' auf 'Geliefert'
		else {
			ls.setRechnungIId(null);
		}
	}

	/**
	 * Berechnung des Gestehungswerts eines Lieferscheins. <br>
	 * Der Gestehungswert ist die Summe ueber die Gestehungswerte der
	 * enthaltenen Artikelpositionen. <br>
	 * Der Gestehungswert einer Artikelposition errechnet sich aus Menge x
	 * positionsbezogenem Gestehungspreis des enthaltenen Artikels.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Gestehungswert in der gewuenschten Waehrung
	 */
	public BigDecimal berechneGestehungswert(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGestehungswert";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}

		BigDecimal bdGestehungswert = Helper.getBigDecimalNull();

		// alle Positionen dieses Lieferscheins
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> c = query.getResultList();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Lieferscheinposition oPosition = ((Lieferscheinposition) iter
					.next());

			// alle positiven mengenbehafteten Positionen beruecksichtigen
			if (oPosition.getNMenge() != null
					&& oPosition.getNMenge().doubleValue() > 0) {

				// Grundlage ist der positionsbezogene Gestehungspreis des
				// Artikels.

				if (oPosition.getLieferscheinpositionartCNr().equals(
						LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

					bdGestehungswert = bdGestehungswert
							.add(berechneGestehungswertEinerLieferscheinposition(
									assembleLieferscheinpositionDto(oPosition),
									theClientDto));
				}
			}
		}

		bdGestehungswert = Helper.rundeKaufmaennisch(bdGestehungswert, 4);
		checkNumberFormat(bdGestehungswert);
		return bdGestehungswert;
	}

	/**
	 * Berechnung des Gestehungswerts eines Lieferscheins. <br>
	 * Der Gestehungswert ist die Summe ueber die Gestehungswerte der
	 * enthaltenen Artikelpositionen. <br>
	 * Der Gestehungswert einer Artikelposition errechnet sich aus Menge x
	 * positionsbezogenem Gestehungspreis des enthaltenen Artikels.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Gestehungswert in der gewuenschten Waehrung
	 */
	public BigDecimal berechneGestehungswertZielLager(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGestehungswert";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		BigDecimal bdGestehungswert = Helper.getBigDecimalNull();
		// alle Positionen dieses Lieferscheins
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> c = query.getResultList();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Lieferscheinposition oPosition = ((Lieferscheinposition) iter
					.next());
			// alle positiven mengenbehafteten Positionen beruecksichtigen
			if (oPosition.getNMenge() != null
					&& oPosition.getNMenge().doubleValue() > 0) {
				// Grundlage ist der positionsbezogene Gestehungspreis des
				// Artikels.
				BigDecimal bdGestehungspreis = Helper.getBigDecimalNull();
				BigDecimal bdWertderposition = Helper.getBigDecimalNull();
				if (oPosition.getLieferscheinpositionartCNr().equals(
						LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
					bdWertderposition = oPosition.getNMenge().multiply(
							oPosition.getNNettoeinzelpreis());
					bdGestehungswert = bdGestehungswert.add(bdWertderposition);
				}
			}
		}
		bdGestehungswert = Helper.rundeKaufmaennisch(bdGestehungswert, 4);
		checkNumberFormat(bdGestehungswert);
		return bdGestehungswert;
	}

	/**
	 * Den Gestehungswert einer Lieferscheinposition berechnen. <br>
	 * Ein Gestehungswert existiert nur fuer lagerbewirtschaftete Artikel.
	 * 
	 * @param lieferscheinpositionDtoI
	 *            die Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Gestehungswert
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneGestehungswertEinerLieferscheinposition(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		BigDecimal bdGestehungswert = new BigDecimal(0);

		try {
			if (getLieferscheinpositionFac()
					.enthaeltLieferscheinpositionLagerbewirtschaftetenArtikel(
							lieferscheinpositionDtoI.getIId(), theClientDto)) {
				BigDecimal bdGestehungpreisEinerEinheit = new BigDecimal(0);
				BigDecimal bdWertderposition = new BigDecimal(0);

				// CK: Gestehungspreise gibt es nur, wenn es eine zugehoerige
				// Lagerbewegung gibt.
				// Es gibt keine Lagerbewegungen fuer Menge <= 0
				if (lieferscheinpositionDtoI.getNMenge().doubleValue() > 0) {
					// wenn es keine Serien- oder Chargennummer gibt
					if (lieferscheinpositionDtoI.getSeriennrChargennrMitMenge() == null
							|| lieferscheinpositionDtoI
									.getSeriennrChargennrMitMenge().size() == 0) {
						bdGestehungpreisEinerEinheit = getLagerFac()
								.getGestehungspreisEinerAbgangsposition(
										LocaleFac.BELEGART_LIEFERSCHEIN,
										lieferscheinpositionDtoI.getIId(), null);

						bdWertderposition = lieferscheinpositionDtoI
								.getNMenge().multiply(
										bdGestehungpreisEinerEinheit);

						bdGestehungswert = bdGestehungswert
								.add(bdWertderposition);
					}

					// getGestehungpreis() greift auf WW_LAGERBEWEGUNG zu. Hier
					// gibt es pro Serien- oder
					// Chargennummer 1 Datensatz
					else {

						for (int i = 0; i < lieferscheinpositionDtoI
								.getSeriennrChargennrMitMenge().size(); i++) {
							bdGestehungpreisEinerEinheit = getLagerFac()
									.getGestehungspreisEinerAbgangspositionMitTransaktion(
											LocaleFac.BELEGART_LIEFERSCHEIN,
											lieferscheinpositionDtoI.getIId(),
											lieferscheinpositionDtoI
													.getSeriennrChargennrMitMenge()
													.get(i)
													.getCSeriennrChargennr());

							bdWertderposition = lieferscheinpositionDtoI
									.getSeriennrChargennrMitMenge().get(i)
									.getNMenge()
									.multiply(bdGestehungpreisEinerEinheit);

							bdGestehungswert = bdGestehungswert
									.add(bdWertderposition);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bdGestehungswert;
	}

	/**
	 * Den Gestehungswert einer Lieferscheinposition berechnen. <br>
	 * Ein Gestehungswert existiert nur fuer lagerbewirtschaftete Artikel.
	 * 
	 * @param iIdlieferscheinpositionI
	 *            die IId der Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Gestehungswert
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneGestehungswertEinerLieferscheinposition(
			Integer iIdlieferscheinpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		LieferscheinpositionDto lieferscheinpositionDtoI = null;
		BigDecimal bdGestehungswert = new BigDecimal(0);
		try {
			lieferscheinpositionDtoI = getLieferscheinpositionFac()
					.lieferscheinpositionFindByPrimaryKey(
							iIdlieferscheinpositionI, theClientDto);
			if (getLieferscheinpositionFac()
					.enthaeltLieferscheinpositionLagerbewirtschaftetenArtikel(
							lieferscheinpositionDtoI.getIId(), theClientDto)) {
				BigDecimal bdGestehungpreisEinerEinheit = new BigDecimal(0);
				BigDecimal bdWertderposition = new BigDecimal(0);

				// CK: Gestehungspreise gibt es nur, wenn es eine zugehoerige
				// Lagerbewegung gibt.
				// Es gibt keine Lagerbewegungen fuer Menge <= 0
				if (lieferscheinpositionDtoI.getNMenge().doubleValue() > 0) {
					// wenn es keine Serien- oder Chargennummer gibt
					if (lieferscheinpositionDtoI.getSeriennrChargennrMitMenge() == null
							|| lieferscheinpositionDtoI
									.getSeriennrChargennrMitMenge().size() == 0) {
						bdGestehungpreisEinerEinheit = getLagerFac()
								.getGestehungspreisEinerAbgangsposition(
										LocaleFac.BELEGART_LIEFERSCHEIN,
										lieferscheinpositionDtoI.getIId(), null);

						bdWertderposition = lieferscheinpositionDtoI
								.getNMenge().multiply(
										bdGestehungpreisEinerEinheit);

						bdGestehungswert = bdGestehungswert
								.add(bdWertderposition);
					}

					// getGestehungpreis() greift auf WW_LAGERBEWEGUNG zu. Hier
					// gibt es pro Serien- oder
					// Chargennummer 1 Datensatz
					else {

						for (int i = 0; i < lieferscheinpositionDtoI
								.getSeriennrChargennrMitMenge().size(); i++) {
							bdGestehungpreisEinerEinheit = getLagerFac()
									.getGestehungspreisEinerAbgangsposition(
											LocaleFac.BELEGART_LIEFERSCHEIN,
											lieferscheinpositionDtoI.getIId(),
											lieferscheinpositionDtoI
													.getSeriennrChargennrMitMenge()
													.get(i)
													.getCSeriennrChargennr());

							bdWertderposition = lieferscheinpositionDtoI
									.getNMenge().multiply(
											bdGestehungpreisEinerEinheit);

							bdGestehungswert = bdGestehungswert
									.add(bdWertderposition);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bdGestehungswert;
	}

	/**
	 * Den Gesamtwert eines Lieferscheins berechnen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Gesamtwert des Lieferscheins
	 */
	public BigDecimal berechneGesamtwertLieferschein(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGesamtwertLieferschein";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		BigDecimal wertDesLieferscheins = Helper.getBigDecimalNull();
		// der aktuelle Lieferschein
		Lieferschein lieferschein = em.find(Lieferschein.class,
				iIdLieferscheinI);
		if (lieferschein == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// Step 1: Wenn der Lieferscheinwert NULL und Status ANGELEGT, dann den
		// Wert aus den Positionen berechnen
		if (lieferschein.getNGesamtwertinlieferscheinwaehrung() == null
				&& lieferschein.getLieferscheinstatusCNr().equals(
						LieferscheinFac.LSSTATUS_ANGELEGT)) {
			wertDesLieferscheins = berechneGesamtwertLieferscheinAusPositionen(
					iIdLieferscheinI, theClientDto);
		} else

		// Step 2: Wenn der status STORNIERT ist, 0 zurueckgeben obwohl der
		// auftragswert noch in der Tabelle steht
		if (lieferschein.getLieferscheinstatusCNr().equals(
				LieferscheinFac.LSSTATUS_STORNIERT)) {
			wertDesLieferscheins = Helper.getBigDecimalNull();
		}

		else

		// Step 3: Wenn der status OFFEN ist, den Wert aus der Tabelle holen
		if (lieferschein.getLieferscheinstatusCNr().equals(
				LieferscheinFac.LSSTATUS_OFFEN)
				|| lieferschein.getLieferscheinstatusCNr().equals(
						LieferscheinFac.LSSTATUS_GELIEFERT)
				|| lieferschein.getLieferscheinstatusCNr().equals(
						LieferscheinFac.LSSTATUS_ERLEDIGT)
				|| lieferschein.getLieferscheinstatusCNr().equals(
						LieferscheinFac.LSSTATUS_VERRECHNET)) {
			if (lieferschein.getNGesamtwertinlieferscheinwaehrung() != null) {
				wertDesLieferscheins = lieferschein
						.getNGesamtwertinlieferscheinwaehrung();
			}
		}

		// Step 4: Der Wert muss in unsere Datenbank passen, fuer decimal(9)
		// gilt 15,4
		wertDesLieferscheins = Helper.rundeKaufmaennisch(wertDesLieferscheins,
				4);
		checkNumberFormat(wertDesLieferscheins);
		return wertDesLieferscheins;
	}

	/**
	 * Den Gesamtwert eines Lieferscheins aus seinen Positionen berechnen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Gesamtwert in Lieferscheinwaehrung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneGesamtwertLieferscheinAusPositionen(
			Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGesamtwertLieferscheinAusPositionen";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		BigDecimal bdGesamtwertO = Helper.getBigDecimalNull();
		try {
			// alle Positionen dieses Lieferscheins
			LieferscheinpositionDto[] aPositionDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);
			LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(
					iIdLieferscheinI, theClientDto);
			bdGesamtwertO = getBelegVerkaufFac().getGesamtwertinBelegwaehrung(
					aPositionDtos, lieferscheinDto);

			for (LieferscheinpositionDto lsPositionDto : aPositionDtos) {
				if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_INTELLIGENTE_ZWISCHENSUMME
						.equals(lsPositionDto.getPositionsartCNr())) {
					getLieferscheinpositionFac()
							.updateLieferscheinpositionOhneWeitereAktion(
									lsPositionDto, theClientDto);
				}
			}

			/*
			 * for (int i = 0; i < aPositionDtos.length; i++) {
			 * LieferscheinpositionDto oPositionDto = aPositionDtos[i];
			 * 
			 * // alle mengenbehafteten Positionen beruecksichtigen //IMS 2129
			 * if (oPositionDto.getNMenge() != null) {
			 * 
			 * BigDecimal bdMenge = oPositionDto.getNMenge();
			 * 
			 * // Grundlage ist der
			 * NettogesamtwertPlusVersteckterAufschlagMinusRabatt der Position
			 * in Lieferscheinwaehrung BigDecimal bdWertDerPosition =
			 * bdMenge.multiply(oPositionDto.
			 * getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			 * 
			 * bdGesamtwertO = bdGesamtwertO.add(bdWertDerPosition); } }
			 */
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return bdGesamtwertO;
	}

	/**
	 * Auf einen Basiswert saemtliche Zu- und Abschlaege anrechnen, die in den
	 * Konditionen eines Lieferscheins hinterlegt sind.
	 * 
	 * @param lieferscheinIId
	 *            PK des Lieferscheins
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der errechnete Wert
	 */

	public boolean enthaeltLieferscheinNullPreise(Integer lieferscheinIId) {

		String queryString = "SELECT count(ls.n_nettogesamtpreisplusversteckteraufschlagminusrabatt) FROM FLRLieferscheinposition AS ls WHERE ls.n_nettogesamtpreisplusversteckteraufschlagminusrabatt=0 AND ls.flrartikel.i_id IS NOT NULL AND ls.flrlieferschein.i_id="
				+ lieferscheinIId;

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		if (results.size() > 0) {
			long anzahl = (Long) resultListIterator.next();
			if (anzahl > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	private BigDecimal berechneWertMitZuUndAbschlaegenAusKonditionen(
			BigDecimal bdWertI, Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheinDto oDto = lieferscheinFindByPrimaryKey(iIdLieferscheinI,
				theClientDto);

		// zuerst den versteckten Aufschlag aufschlagen ...
		if (oDto.getFVersteckterAufschlag().doubleValue() != (double) 0) {
			BigDecimal versteckterAufschlag = bdWertI.multiply(new BigDecimal(
					oDto.getFVersteckterAufschlag().doubleValue())
					.movePointLeft(2));
			versteckterAufschlag = Helper.rundeKaufmaennisch(
					versteckterAufschlag, 4);
			bdWertI = bdWertI.add(versteckterAufschlag);
		}

		// dann den rabatt beruecksichtigen ...
		if (oDto.getFAllgemeinerRabattsatz().doubleValue() != (double) 0) {
			BigDecimal allgRabatt = bdWertI.multiply(new BigDecimal(oDto
					.getFAllgemeinerRabattsatz().doubleValue())
					.movePointLeft(2));
			allgRabatt = Helper.rundeKaufmaennisch(allgRabatt, 4);
			bdWertI = bdWertI.subtract(allgRabatt);
		}

		return bdWertI;
	}

	public void removeLieferscheintext(Integer iId) throws EJBExceptionLP {
		// try {
		Lieferscheintext toRemove = em.find(Lieferscheintext.class, iId);
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new
		// Exception(e));
		// }
	}

	public void removeLieferscheintext(LieferscheintextDto lieferscheintextDto)
			throws EJBExceptionLP {
		if (lieferscheintextDto != null) {
			Integer iId = lieferscheintextDto.getIId();
			removeLieferscheintext(iId);
		}
	}

	public void updateLieferscheintext(LieferscheintextDto lieferscheintextDto)
			throws EJBExceptionLP {
		if (lieferscheintextDto != null) {
			Integer iId = lieferscheintextDto.getIId();
			// try {
			Lieferscheintext lieferscheintext = em.find(Lieferscheintext.class,
					iId);
			if (lieferscheintext == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setLieferscheintextFromLieferscheintextDto(lieferscheintext,
					lieferscheintextDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateLieferscheintexts(
			LieferscheintextDto[] lieferscheintextDtos) throws EJBExceptionLP {
		if (lieferscheintextDtos != null) {
			for (int i = 0; i < lieferscheintextDtos.length; i++) {
				updateLieferscheintext(lieferscheintextDtos[i]);
			}
		}
	}

	public LieferscheintextDto lieferscheintextFindByPrimaryKey(Integer iiIIdI)
			throws EJBExceptionLP {
		// try {
		Lieferscheintext lieferscheintext = em.find(Lieferscheintext.class,
				iiIIdI);
		if (lieferscheintext == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLieferscheintextDto(lieferscheintext);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setLieferscheintextFromLieferscheintextDto(
			Lieferscheintext lieferscheintext,
			LieferscheintextDto lieferscheintextDto) {
		lieferscheintext.setMandantCNr(lieferscheintextDto.getMandantCNr());
		lieferscheintext.setLocaleCNr(lieferscheintextDto.getLocaleCNr());
		lieferscheintext.setMediaartCNr(lieferscheintextDto.getMediaartCNr());
		lieferscheintext.setXTextinhalt(lieferscheintextDto.getCTextinhalt());
		em.merge(lieferscheintext);
		em.flush();
	}

	private LieferscheintextDto assembleLieferscheintextDto(
			Lieferscheintext lieferscheintext) {
		return LieferscheintextDtoAssembler.createDto(lieferscheintext);
	}

	private LieferscheintextDto[] assembleLieferscheintextDtos(
			Collection<?> lieferscheintexts) {
		List<LieferscheintextDto> list = new ArrayList<LieferscheintextDto>();
		if (lieferscheintexts != null) {
			Iterator<?> iterator = lieferscheintexts.iterator();
			while (iterator.hasNext()) {
				Lieferscheintext lieferscheintext = (Lieferscheintext) iterator
						.next();
				list.add(assembleLieferscheintextDto(lieferscheintext));
			}
		}
		LieferscheintextDto[] returnArray = new LieferscheintextDto[list.size()];
		return (LieferscheintextDto[]) list.toArray(returnArray);
	}

	/**
	 * Einen in der db fix hinterlegten Text zum Lieferschein holen.
	 * 
	 * @param sLocaleI
	 *            gewuenschtes Locale
	 * @param sCNrI
	 *            die Bezeichnung des Textes
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @return LieferscheintextDto
	 */
	public LieferscheintextDto lieferscheintextFindByMandantLocaleCNr(
			String sLocaleI, String sCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LieferscheintextDto text = null;
		Lieferscheintext lieferscheintext = null;
		try {
			Query query = em
					.createNamedQuery("LieferscheintextfindByMandantLocaleMediaartCNr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, sLocaleI);
			query.setParameter(3, sCNrI);
			lieferscheintext = (Lieferscheintext) query.getSingleResult();
		} catch (NoResultException ex1) {
			try {
				Query query = em
						.createNamedQuery("LieferscheintextfindByMandantLocaleMediaartCNr");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, theClientDto.getLocUiAsString());
				query.setParameter(3, sCNrI);
				lieferscheintext = (Lieferscheintext) query.getSingleResult();
			} catch (NoResultException ex2) {
				try {
					Query query = em
							.createNamedQuery("LieferscheintextfindByMandantLocaleMediaartCNr");
					query.setParameter(1, theClientDto.getMandant());
					query.setParameter(2, theClientDto.getLocKonzernAsString());
					query.setParameter(3, sCNrI);
					lieferscheintext = (Lieferscheintext) query
							.getSingleResult();
				} catch (NoResultException ex3) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
				}
			}

		}
		return assembleLieferscheintextDto(lieferscheintext);
	}

	public BigDecimal berechneOffenenLieferscheinwert(Integer kundeIId,
			TheClientDto theClientDto) {
		String queryString = "SELECT ls FROM FLRLieferschein AS ls WHERE ls.b_verrechenbar=1 AND ls.lieferscheinstatus_status_c_nr IN ('"
				+ LocaleFac.STATUS_OFFEN
				+ "','"
				+ LocaleFac.STATUS_GELIEFERT
				+ "') ";

		if (kundeIId != null) {
			queryString += " AND ls.kunde_i_id_rechnungsadresse=" + kundeIId;
		}

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		BigDecimal wert = new BigDecimal(0);
		while (resultListIterator.hasNext()) {
			FLRLieferschein ls = (FLRLieferschein) resultListIterator.next();

			if (ls.getN_gesamtwertinlieferscheinwaehrung() != null) {
				wert = wert
						.add(ls.getN_gesamtwertinlieferscheinwaehrung()
								.divide(new BigDecimal(
										ls.getF_wechselkursmandantwaehrungzulieferscheinwaehrung()),
										4, BigDecimal.ROUND_HALF_EVEN));
			}

		}
		return wert;

	}

	/**
	 * Die Anzahl der Lieferscheine mit Status Offen oder Geliefert.
	 * 
	 * @return int die Anzahl der offenen Lieferscheine
	 * @throws EJBExceptionLP
	 */
	public int berechneAnzahlDerOffenenLieferscheine() throws EJBExceptionLP {
		int iAnzahlO = 0;
		Query query = em.createNamedQuery("LieferscheinfindByStatus1Status2");
		query.setParameter(1, LieferscheinFac.LSSTATUS_OFFEN);
		query.setParameter(2, LieferscheinFac.LSSTATUS_GELIEFERT);
		Collection<?> c = query.getResultList();
		Iterator<?> it = c.iterator();
		while (it.hasNext()) {
			it.next();

			iAnzahlO++;
		}
		return iAnzahlO;
	}

	/**
	 * Die Kopfdaten und Positionen eines Lieferscheins duerfen geaendert
	 * werden, wenn der Status des Lieferscheins Angelegt, Offen oder Geliefert
	 * ist.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void pruefeUndSetzeLieferscheinstatusBeiAenderung(
			Integer iIdLieferscheinI) throws EJBExceptionLP {
		final String METHOD_NAME = "pruefeUndSetzeLieferscheinstatusBeiAenderung";
		myLogger.entry();

		Lieferschein lieferschein = null;

		// try {
		lieferschein = em.find(Lieferschein.class, iIdLieferscheinI);
		if (lieferschein == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		String sStatus = lieferschein.getLieferscheinstatusCNr();

		if (!sStatus.equals(LieferscheinFac.LSSTATUS_ANGELEGT)
				&& !sStatus.equals(LieferscheinFac.LSSTATUS_OFFEN)
				&& !sStatus.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Fehler: Lieferschein: "
									+ lieferschein.getCNr()
									+ "kann nicht ver\u00E4ndert werden, da er sich im Status "
									+ sStatus.trim() + "befindet!"));
		}

		if (sStatus.equals(LieferscheinFac.LSSTATUS_OFFEN)
				|| sStatus.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
			lieferschein
					.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_ANGELEGT);
			lieferschein.setNGesamtwertinlieferscheinwaehrung(null);
			lieferschein.setNGestehungswertinmandantenwaehrung(null);
		}
	}

	/**
	 * Wenn die Konditionen im Lieferschein geaendert werden, muessen die Werte
	 * der einzelnen Positionen und die Gesamtwerte des Lieferscheins angepasst
	 * werden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferschein
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateLieferscheinKonditionen(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "updateLieferscheinKonditionen";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		try {
			LieferscheinpositionDto[] aDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);

			// fuer jede Position die Zu- und Abschlaege neu beruecksichtigen
			for (int i = 0; i < aDtos.length; i++) {
				aDtos[i] = getLieferscheinpositionFac()
						.befuelleZusaetzlichePreisfelder(aDtos[i].getIId(),
								theClientDto);
				if (aDtos[i].getLieferscheinpositionartCNr().equals(
						LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
					if (aDtos[i].getNMenge() != null
							&& aDtos[i].getNMenge().doubleValue() > 0) {

						getLieferscheinpositionFac().bucheAbLager(aDtos[i],
								theClientDto);

					} else if (aDtos[i].getNMenge() != null
							&& aDtos[i].getNMenge().doubleValue() < 0) {

						getLieferscheinpositionFac().bucheZuLager(aDtos[i],
								theClientDto);

					}
				}

			}

			Lieferschein oLieferschein = em.find(Lieferschein.class,
					iIdLieferscheinI);
			if (oLieferschein == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (!oLieferschein.getLieferscheinstatusCNr().equals(
					LieferscheinFac.LSSTATUS_ANGELEGT)) {
				oLieferschein
						.setNGesamtwertinlieferscheinwaehrung(berechneGesamtwertLieferscheinAusPositionen(
								iIdLieferscheinI, theClientDto));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void updateLieferscheinBegruendung(Integer lieferscheinIId,
			Integer begruendungIId, TheClientDto theClientDto) {
		Lieferschein oLieferschein = em.find(Lieferschein.class,
				lieferscheinIId);
		oLieferschein.setBegruendungIId(begruendungIId);
		oLieferschein.setTAendern(new Timestamp(System.currentTimeMillis()));
		oLieferschein.setPersonalIIdAendern(theClientDto.getIDPersonal());
		em.merge(oLieferschein);
		em.flush();
	}

	/**
	 * Die Anzahl der Lieferscheine, die in der Umsatzuebersicht aufscheinen.
	 * 
	 * @param gcVonI
	 *            von diesem Zeitpunkt
	 * @param gcBisI
	 *            bis zu diesem Zeitpunkt
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer die Anzahl
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer zaehleLieferscheinFuerUmsatz(GregorianCalendar gcVonI,
			GregorianCalendar gcBisI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "zaehleLieferscheinFuerUmsatz";
		myLogger.entry();
		if (gcVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("gcVonI == null"));
		}

		if (gcBisI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("gcBisI == null"));
		}

		Integer iiAnzahl = new Integer(0);
		Query query = em
				.createNamedQuery("LieferscheinfindByMandantVonBisStatus1Status2Status3Status4");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, new java.sql.Date(gcVonI.getTimeInMillis()));
		query.setParameter(3, new java.sql.Date(gcBisI.getTimeInMillis()));
		query.setParameter(4, LieferscheinFac.LSSTATUS_OFFEN);
		query.setParameter(5, LieferscheinFac.LSSTATUS_GELIEFERT);
		query.setParameter(6, LieferscheinFac.LSSTATUS_VERRECHNET);
		query.setParameter(7, LieferscheinFac.LSSTATUS_ERLEDIGT);
		Collection<?> cl = query.getResultList();
		iiAnzahl = new Integer(cl.size());
		return iiAnzahl;
	}

	/**
	 * Den Umsatz der Lieferscheine in einem gewissen Zeitraum berechnen. <br>
	 * Der Umsatz wird in Mandantenwaehrung angezeigt.
	 * 
	 * @param gcVonI
	 *            ab diesem Datum inklusive
	 * @param gcBisI
	 *            bis zu diesem Datum inklusive
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Umsatz
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneUmsatz(GregorianCalendar gcVonI,
			GregorianCalendar gcBisI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneUmsatz";
		myLogger.entry();
		if (gcVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("gcVonI == null"));
		}

		if (gcBisI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("gcBisI == null"));
		}

		BigDecimal bdUmsatzO = Helper.getBigDecimalNull();
		// Schritt 1: Alle relevanten Lieferscheine besorgen
		Query query = em
				.createNamedQuery("LieferscheinfindByMandantVonBisStatus1Status2Status3Status4");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, new java.sql.Date(gcVonI.getTimeInMillis()));
		query.setParameter(3, new java.sql.Date(gcBisI.getTimeInMillis()));
		query.setParameter(4, LieferscheinFac.LSSTATUS_OFFEN);
		query.setParameter(5, LieferscheinFac.LSSTATUS_GELIEFERT);
		query.setParameter(6, LieferscheinFac.LSSTATUS_VERRECHNET);
		query.setParameter(7, LieferscheinFac.LSSTATUS_ERLEDIGT);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {

			// Schritt 2: Jeden einzelnen relevanten Lieferschein
			// beruecksichtigen
			Lieferschein oLieferschein = (Lieferschein) it.next();

			// fuer jeden Lieferschein ist sein Umsatz fuer jeden dieser Stati
			// in
			// Lieferscheinwaehrung in der DB abgelegt

			BigDecimal bdBeitragDiesesLieferscheins = oLieferschein
					.getNGesamtwertinlieferscheinwaehrung();

			if (bdBeitragDiesesLieferscheins != null) {

				// den Beitrag normieren auf Mandantenwaehrung
				BigDecimal bdWechselkurs = Helper
						.getKehrwert(new BigDecimal(
								oLieferschein
										.getFWechselkursmandantwaehrungzulieferscheinwaehrung()
										.doubleValue()));
				bdBeitragDiesesLieferscheins = bdBeitragDiesesLieferscheins
						.multiply(bdWechselkurs);
				bdBeitragDiesesLieferscheins = Helper.rundeKaufmaennisch(
						bdBeitragDiesesLieferscheins, 4);
				checkNumberFormat(bdBeitragDiesesLieferscheins);

				bdUmsatzO = bdUmsatzO.add(bdBeitragDiesesLieferscheins);
			}
		}
		return bdUmsatzO;
	}

	/**
	 * Ein Lieferschein kann manuell freigegeben werden. <br>
	 * Der Lieferschein muss sich im Status 'Angelegt' befinden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void manuellFreigeben(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "manuellFreigeben";
		myLogger.entry();
		
		Validator.notNull(iIdLieferscheinI, "iIdLieferscheinI");
		// IMS ID 179 : Manuell freigeben bedeutet je nach Parameterwert den
		// Wechsel
		// auf 'Offen' oder 'Geliefert'

		// LP 5.0.5 Wechsel auf 'Geliefert'
		LieferscheinDto lieferschein = lieferscheinFindByPrimaryKey(iIdLieferscheinI);

		if (lieferschein.getStatusCNr().equals(
				LieferscheinFac.LSSTATUS_ANGELEGT)) {
			lieferschein.setStatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
			updateLieferscheinOhneWeitereAktion(lieferschein, theClientDto);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Lieferschein kann nicht manuell freigegeben werden, Status : "
									+ lieferschein.getStatusCNr()));
		}
	}

	/**
	 * Ein Lieferschein kann manuell erledigt werden. <br>
	 * Der Lieferschein muss sich im Status 'Geliefert' befinden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void manuellErledigen(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinIId(iIdLieferscheinI);
		myLogger.logData(iIdLieferscheinI);
		Validator.notNull(iIdLieferscheinI, "iIdLieferscheinI");
		
		LieferscheinDto lieferschein = lieferscheinFindByPrimaryKey(iIdLieferscheinI);
		if (lieferschein.getStatusCNr().equals(
				LieferscheinFac.LSSTATUS_GELIEFERT)) {
			lieferschein
					.setStatusCNr(LieferscheinFac.LSSTATUS_ERLEDIGT);
			lieferschein.setTManuellerledigt(getTimestamp());
			lieferschein.setPersonalIIdManuellerledigt(theClientDto
					.getIDPersonal());
			updateLieferscheinOhneWeitereAktion(lieferschein, theClientDto);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Lieferschein kann nicht manuell erledigt werden, Status : "
									+ lieferschein.getStatusCNr()));
		}
	}

	/**
	 * Die Erledigung eines Lieferscheins kann aufgehoben werden. <br>
	 * Der Lieferschein muss sich im Status 'Erledigt' befinden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void erledigungAufheben(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinIId(iIdLieferscheinI);
		LieferscheinDto lieferschein = lieferscheinFindByPrimaryKey(iIdLieferscheinI);
		if (lieferschein.getStatusCNr().equals(
				LieferscheinFac.LSSTATUS_ERLEDIGT)) {
			if (lieferschein.getPersonalIIdManuellerledigt() != null
					&& lieferschein.getTManuellerledigt() != null) {
				lieferschein
						.setStatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
				lieferschein.setTManuellerledigt(null);
				lieferschein.setPersonalIIdManuellerledigt(null);
			} else {
				// throw new EJBExceptionLP(
				// EJBExceptionLP.FEHLER_BELEG_WURDE_NICHT_MANUELL_ERLEDIGT,
				// new
				// Exception("Dieser Lieferschein wurde nicht manuell erledigt"
				// ));
				lieferschein
						.setStatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
				myLogger.logKritisch("Status Erledigt wurde aufgehoben, obwohl der Lieferschein nicht manuell erledigt wurde, LieferscheinIId: "
						+ iIdLieferscheinI);
			}
			updateLieferschein(lieferschein, theClientDto);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Die Erledigung des Lieferscheins kann nicht aufgehoben werden, Status: "
									+ lieferschein.getStatusCNr()));
		}
	}

	/**
	 * Ein Lieferschein kann durch eine Rechnung verrechnet werden. <br>
	 * Der Lieferschein muss sich im Status 'Geliefert' befinden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param iIdRechnungI
	 *            PK der verrechnenden Rechnung
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void verrechnen(Integer iIdLieferscheinI, Integer iIdRechnungI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "verrechnen";
		myLogger.entry();
		Validator.notNull(iIdLieferscheinI, "iIdLieferscheinI");
		Validator.notNull(iIdRechnungI, "iIdRechnungI");
		
		LieferscheinDto lieferschein = lieferscheinFindByPrimaryKey(iIdLieferscheinI);

		if (lieferschein.getStatusCNr().equals(
				LieferscheinFac.LSSTATUS_GELIEFERT)) {
			lieferschein.setStatusCNr(LieferscheinFac.LSSTATUS_VERRECHNET);
			lieferschein.setRechnungIId(iIdRechnungI);
			updateLieferscheinOhneWeitereAktion(lieferschein, theClientDto);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Lieferschein kann nicht verrechnet werden, Status : "
									+ lieferschein.getStatusCNr()));
		}
	}

	/**
	 * Einen Lieferschein stornieren. <br>
	 * Der Lieferschein bekommt den Status Storniert. <br>
	 * Die Lieferscheinposition bleiben mit ihren Werten erhalten, alle
	 * Buchungen werden rueckgaengig gemacht. <br>
	 * Der Storno eines Lieferscheins kann nicht rueckgaengig gemacht werden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void stornieren(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkLieferscheinIId(iIdLieferscheinI);
		try {
			LieferscheinDto lieferschein = lieferscheinFindByPrimaryKey(iIdLieferscheinI);
			if (lieferschein.getStatusCNr().equals(
					LieferscheinFac.LSSTATUS_GELIEFERT)) {
				// die Lieferscheinwerte bleiben beim Stornieren erhalten
				lieferschein
						.setStatusCNr(LieferscheinFac.LSSTATUS_STORNIERT);
				lieferschein.setTStorniert(getTimestamp());
				lieferschein.setPersonalIIdStorniert(theClientDto
						.getIDPersonal());

				LieferscheinpositionDto[] aLieferscheinpositionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(
								iIdLieferscheinI);

				if (aLieferscheinpositionDto != null
						&& aLieferscheinpositionDto.length > 0) {
					for (int i = 0; i < aLieferscheinpositionDto.length; i++) {
						getLieferscheinpositionFac()
								.storniereLieferscheinposition(
										aLieferscheinpositionDto[i].getIId(),
										theClientDto);
					}
				}
				updateLieferscheinOhneWeitereAktion(lieferschein, theClientDto);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
						new Exception(
								"Lieferschein kann nicht storniert werden, Status : "
										+ lieferschein
												.getStatusCNr()));

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_STORNIEREN,
					new Exception(t));
		}
	}

//	/**
//	 * Storno eines Lieferscheins aufheben.
//	 * 
//	 * @param iIdLieferscheinI
//	 *            PK des Lieferscheins
//	 * @param theClientDto
//	 *            der aktuelle Benutzer
//	 * @throws EJBExceptionLP
//	 *             Ausnahme
//	 */
//	private void stornoAufheben(Integer iIdLieferscheinI,
//			TheClientDto theClientDto) throws EJBExceptionLP {
//		final String METHOD_NAME = "stornoAufheben";
//		myLogger.entry();
//		if (iIdLieferscheinI == null) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
//					new Exception("iIdLieferscheinI == null"));
//		}
//		Lieferschein oLieferschein = em.find(Lieferschein.class,
//				iIdLieferscheinI);
//		if (oLieferschein == null) {
//			throw new EJBExceptionLP(
//					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
//		}
//
//		if (oLieferschein.getLieferscheinstatusCNr().equals(
//				LieferscheinFac.LSSTATUS_STORNIERT)) {
//			oLieferschein
//					.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
//			oLieferschein.setTStorniert(null);
//			oLieferschein.setPersonalIIdStorniert(null);
//		} else {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
//					new Exception(
//							"Storno des Lieferscheins kann nicht aufgehoben werden, Status : "
//									+ oLieferschein.getLieferscheinstatusCNr()));
//		}
//	}

	/**
	 * Fuer die Nachkalkulation des Lieferscheins den Ist-Verkaufswert (=
	 * NettoVerkaufspreisPlusAufschlaegeMinusRabatte pro Stueck * gelieferte
	 * Menge) bezogen auf eine bestimmte Artikelart berechnen. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Lieferscheinpositionen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param sArtikelartI
	 *            die gewuenschte Artikelart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Verkaufswert der Artikelart Ist in
	 *         Mandantenwaehrung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneVerkaufswertIst(Integer iIdLieferscheinI,
			HashMap lieferscheinpositionIIds, String sArtikelartI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneVerkaufswertIst";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sArtikelartI == null"));
		}

		BigDecimal bdVerkaufswertIstO = Helper.getBigDecimalNull();

		try {
			LieferscheinpositionDto[] aLieferscheinpositionDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);

			for (int i = 0; i < aLieferscheinpositionDtos.length; i++) {

				if (lieferscheinpositionIIds == null
						|| lieferscheinpositionIIds
								.containsKey(aLieferscheinpositionDtos[i]
										.getIId())) {

					// alle mengenbehafteten Positionen beruecksichtigen
					if (aLieferscheinpositionDtos[i].getNMenge() != null && aLieferscheinpositionDtos[i].getArtikelIId() != null) {
						ArtikelDto oArtikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										aLieferscheinpositionDtos[i]
												.getArtikelIId(),
										theClientDto);

						BigDecimal bdBeitragDieserPosition = aLieferscheinpositionDtos[i]
								.getNMenge()
								.multiply(
										aLieferscheinpositionDtos[i]
												.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

						// je nach Artikelart beruecksichtigen
						if (sArtikelartI
								.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							if (oArtikelDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bdVerkaufswertIstO = bdVerkaufswertIstO
										.add(bdBeitragDieserPosition);
							}
						} else {
							if (!oArtikelDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bdVerkaufswertIstO = bdVerkaufswertIstO
										.add(bdBeitragDieserPosition);
							}
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Verkaufswert " + sArtikelartI + " Ist : "
				+ bdVerkaufswertIstO.toString());

		return bdVerkaufswertIstO;
	}

	/**
	 * Fuer die Nachkalkulation des Auftrags den Ist-Gestehungswert (=
	 * Gestehungswert des Artikels auf Lager Lieferschein pro Stueck *
	 * gelieferter Menge) bezogen auf eine bestimmte Artikelart berechnen. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Lieferscheinpositionen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param sArtikelartI
	 *            die gewuenschte Artikelart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Ist-Gestehungswert der gewuenschten Artikelart in
	 *         Mandantenwaehrung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneGestehungswertIst(Integer iIdLieferscheinI,
			HashMap lieferscheinpositionIIds, String sArtikelartI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGestehungswertIst";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sArtikelartI == null"));
		}

		BigDecimal bdGestehungswertIstO = Helper.getBigDecimalNull();

		try {
			LieferscheinpositionDto[] aLieferscheinpositionDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);

			for (int i = 0; i < aLieferscheinpositionDtos.length; i++) {

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aLieferscheinpositionDtos[i].getNMenge() != null && aLieferscheinpositionDtos[i].getArtikelIId() != null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aLieferscheinpositionDtos[i]
											.getArtikelIId(),
									theClientDto);
					if (lieferscheinpositionIIds == null
							|| lieferscheinpositionIIds
									.containsKey(aLieferscheinpositionDtos[i]
											.getIId())) {
						// Grundlage ist der positionsbezogene Gestehungspreis
						// des
						// Artikels.
						BigDecimal bdGestehungswertIst = Helper
								.getBigDecimalNull();

						if (aLieferscheinpositionDtos[i]
								.getLieferscheinpositionartCNr()
								.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
							bdGestehungswertIst = berechneGestehungswertEinerLieferscheinposition(
									aLieferscheinpositionDtos[i], theClientDto);
						}

						// je nach Artikelart beruecksichtigen // @todo PJ 4399
						if (sArtikelartI
								.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							if (oArtikelDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bdGestehungswertIstO = bdGestehungswertIstO
										.add(bdGestehungswertIst);
							}
						} else {
							if (!oArtikelDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bdGestehungswertIstO = bdGestehungswertIstO
										.add(bdGestehungswertIst);
							}
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		myLogger.exit("Gestehungswert " + sArtikelartI + " Ist : "
				+ bdGestehungswertIstO.toString());

		return bdGestehungswertIstO;
	}

	/**
	 * Bei einem auftragbezogenen Lieferschein ist es moeglich, all jene offenen
	 * oder teilerledigten Auftragpositionen innerhalb einer Transaktion zu
	 * uebernehmen, die keine Benutzerinteraktion benoetigen. <br>
	 * Es gilt:
	 * <ul>
	 * <li>Handeingabepositionen werden uebernommen.
	 * <li>Nicht Serien- oder Chargennummertragende Artikelpositionen werden mit
	 * jener Menge uebernommen, die auf Lager liegt.
	 * <li>Artikelpositionen mit Seriennummer werden nicht uebernommen.
	 * <li>Artikelpositionen mit Chargennummer werden mit jener Menge
	 * uebernommen, die auf Lager liegt, wenn es genau eine Charge gibt.
	 * </ul>
	 * Die restlichen Positionen koennen nicht automatisch uebernommen werden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param auftragIIdI
	 *            Integer
	 * @param theClientDto
	 *            String der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion(
			Integer iIdLieferscheinI, Integer auftragIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinIId(iIdLieferscheinI);
		try {
			LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(
					iIdLieferscheinI, theClientDto);
			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragIIdI);

			boolean bEsGibtNochPositiveOffene = false;
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_VERLEIH, theClientDto)) {
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					if (!AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
							.equals(aAuftragpositionDto[i]
									.getAuftragpositionstatusCNr())) {
						if (aAuftragpositionDto[i].getNMenge() != null
								&& aAuftragpositionDto[i].getNMenge()
										.doubleValue() > 0) {
							bEsGibtNochPositiveOffene = true;
						}
					}
				}
			}

			for (int i = 0; i < aAuftragpositionDto.length; i++) {
				if (!AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
						.equals(aAuftragpositionDto[i]
								.getAuftragpositionstatusCNr())) {
					// IMS 2129
					if (aAuftragpositionDto[i].getNMenge() != null) {

						// wenn es noch positive offene gibt, dann duerfen dei
						// negativen noch nicht geliert werden

						if (aAuftragpositionDto[i].getNMenge().doubleValue() < 0
								&& bEsGibtNochPositiveOffene) {
							continue;
						}

						// dieses Flag legt fest, ob eine Lieferscheinposition
						// fuer die aktuelle
						// Auftragposition angleget oder aktualisiert werden
						// soll
						boolean bLieferscheinpositionErzeugen = false;

						// die Menge, mit der eine neue Lieferscheinposition
						// angelegt oder eine
						// bestehende Lieferscheinposition aktualisiert werden
						// soll
						BigDecimal nMengeFuerLieferscheinposition = null;

						// die Serien- oder Chargennummer, die bei der Abbuchung
						// verwendet werden soll
						String cSerienchargennummer = null;

						if (aAuftragpositionDto[i]
								.getPositionsartCNr()
								.equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
							bLieferscheinpositionErzeugen = true;
							nMengeFuerLieferscheinposition = aAuftragpositionDto[i]
									.getNOffeneMenge();
						} else if (aAuftragpositionDto[i]
								.getPositionsartCNr()
								.equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(
											aAuftragpositionDto[i]
													.getArtikelIId(),
											theClientDto);

							// nicht lagerbewirtschaftete Artikel werden mit der
							// vollen offenen Menge uebernommen
							if (!Helper.short2boolean(artikelDto
									.getBLagerbewirtschaftet())) {
								bLieferscheinpositionErzeugen = true;
								nMengeFuerLieferscheinposition = aAuftragpositionDto[i]
										.getNOffeneMenge();
							} else {
								if (Helper.short2boolean(artikelDto
										.getBSeriennrtragend())) {
									// seriennummerbehaftete Artikel koennen
									// nicht automatisch uebernommen werden
								} else if (Helper.short2boolean(artikelDto
										.getBChargennrtragend())) {
									// chargennummernbehaftete Artikel koennen
									// nur uebernommen werden, wenn
									// es nur eine Charge gibt und mit der
									// Menge, die in dieser Charge
									// vorhanden ist
									SeriennrChargennrAufLagerDto[] alleChargennummern = getLagerFac()
											.getAllSerienChargennrAufLager(
													artikelDto.getIId(),
													lieferscheinDto
															.getLagerIId(),
													theClientDto, true, false);
									if (alleChargennummern != null
											&& alleChargennummern.length == 1) {
										BigDecimal nLagerstd = alleChargennummern[0]
												.getNMenge();
										cSerienchargennummer = alleChargennummern[0]
												.getCSeriennrChargennr();
										// ist ausreichend auf Lager?
										if (nLagerstd
												.compareTo(aAuftragpositionDto[i]
														.getNOffeneMenge()) >= 0) {
											bLieferscheinpositionErzeugen = true;
											nMengeFuerLieferscheinposition = aAuftragpositionDto[i]
													.getNOffeneMenge();
										}
										// nicht genug auf Lager, aber es kann
										// zumindest ein Teil abgebucht werden.
										else if (nLagerstd
												.compareTo(new BigDecimal(0)) > 0) {
											bLieferscheinpositionErzeugen = true;
											nMengeFuerLieferscheinposition = nLagerstd;
										}
									}
								} else {
									// bei lagerbewirtschafteten Artikeln muss
									// die Menge auf Lager
									// beruecksichtigt werden
									BigDecimal nMengeAufLager = getLagerFac()
											.getMengeAufLager(
													artikelDto.getIId(),
													lieferscheinDto
															.getLagerIId(),
													null, theClientDto);

									if (nMengeAufLager.doubleValue() >= aAuftragpositionDto[i]
											.getNOffeneMenge().doubleValue()) {
										bLieferscheinpositionErzeugen = true;
										nMengeFuerLieferscheinposition = aAuftragpositionDto[i]
												.getNOffeneMenge();
									} else if (nMengeAufLager.doubleValue() > 0
											&& nMengeAufLager.doubleValue() < aAuftragpositionDto[i]
													.getNOffeneMenge()
													.doubleValue()) {
										bLieferscheinpositionErzeugen = true;
										nMengeFuerLieferscheinposition = nMengeAufLager;
									}
								}
							}
						}

						if (bLieferscheinpositionErzeugen
								&& nMengeFuerLieferscheinposition != null) {
							LieferscheinpositionDto lieferscheinpositionBisherDto = getLieferscheinpositionByLieferscheinAuftragposition(
									iIdLieferscheinI,
									aAuftragpositionDto[i].getIId());

							if (lieferscheinpositionBisherDto == null) {
								LieferscheinpositionDto lieferscheinpositionDto = aAuftragpositionDto[i]
										.cloneAsLieferscheinpositionDto();

								if (aAuftragpositionDto[i]
										.getPositioniIdArtikelset() != null) {

									LieferscheinpositionDto[] lPositionDtos = null;
									Query query = em
											.createNamedQuery("LieferscheinpositionfindByAuftragposition");
									query.setParameter(1,
											aAuftragpositionDto[i]
													.getPositioniIdArtikelset());
									Collection<?> cl = query.getResultList();
									lPositionDtos = assembleLieferscheinpositionDtos(cl);

									for (int j = 0; j < lPositionDtos.length; j++) {
										if (lPositionDtos[j]
												.getLieferscheinIId().equals(
														iIdLieferscheinI)) {
											lieferscheinpositionDto
													.setPositioniIdArtikelset(lPositionDtos[j]
															.getIId());
											break;
										}
									}

								}

								lieferscheinpositionDto
										.setLieferscheinIId(iIdLieferscheinI);
								lieferscheinpositionDto
										.setNMenge(nMengeFuerLieferscheinposition);
								lieferscheinpositionDto
										.setSeriennrChargennrMitMenge(SeriennrChargennrMitMengeDto
												.erstelleDtoAusEinerChargennummer(
														cSerienchargennummer,
														nMengeFuerLieferscheinposition));
								lieferscheinpositionDto.setISort(null);
								getLieferscheinpositionFac()
										.createLieferscheinposition(
												lieferscheinpositionDto, false,
												theClientDto);
							} else {
								lieferscheinpositionBisherDto
										.setNMenge(nMengeFuerLieferscheinposition);

								if (aAuftragpositionDto[i]
										.getPositionsartCNr()
										.equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
									ArtikelDto artikelDto = getArtikelFac()
											.artikelFindByPrimaryKey(
													aAuftragpositionDto[i]
															.getArtikelIId(),
													theClientDto);

									if (Helper.short2boolean(artikelDto
											.getBSeriennrtragend())
											|| Helper.short2boolean(artikelDto
													.getBChargennrtragend())) {
										lieferscheinpositionBisherDto
												.setSeriennrChargennrMitMenge(SeriennrChargennrMitMengeDto
														.erstelleDtoAusEinerChargennummer(
																cSerienchargennummer,
																nMengeFuerLieferscheinposition));
									} else {
										lieferscheinpositionBisherDto
												.setSeriennrChargennrMitMenge(null);
									}

								}

								getLieferscheinpositionFac()
										.updateLieferscheinpositionSichtAuftrag(
												lieferscheinpositionBisherDto,
												theClientDto);
							}
						}
					} else {
						LieferscheinpositionDto lieferscheinpositionDto = aAuftragpositionDto[i]
								.cloneAsLieferscheinpositionDto();
						lieferscheinpositionDto
								.setLieferscheinIId(iIdLieferscheinI);
						lieferscheinpositionDto.setISort(null);
						getLieferscheinpositionFac()
								.createLieferscheinposition(
										lieferscheinpositionDto, false,
										theClientDto);
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private boolean isArtikelSetHead(Integer artikelIId,
			TheClientDto theClientDto) {
		StuecklisteDto stklDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
						theClientDto);
		return stklDto != null
				&& stklDto.getStuecklisteartCNr().equals(
						StuecklisteFac.STUECKLISTEART_SETARTIKEL);
	}

	private BigDecimal getAvailableAmountArtikelset(Integer auftragpositionIId,
			List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			if (artikelset.getHead().getIId().equals(auftragpositionIId))
				return artikelset.getAvailableAmount();
		}

		return null;
	}

	private BelegpositionVerkaufDto findArtikelsetHead(
			BelegpositionVerkaufDto anyAuftragpositionDto,
			List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			BelegpositionVerkaufDto headDto = artikelset
					.findHeadPositionDto(anyAuftragpositionDto);
			if (null != headDto)
				return headDto;
		}

		return null;
	}

	private BigDecimal getSollsatzgroesseArtikelSet(
			AuftragpositionDto auftragpositionDto, List<Artikelset> artikelsets) {
		BelegpositionVerkaufDto headDto = findArtikelsetHead(
				auftragpositionDto, artikelsets);
		if (null != headDto) {
			BigDecimal headAmount = headDto.getNMenge();
			return auftragpositionDto.getNMenge().divide(headAmount);
		}

		return BigDecimal.ONE;
	}

	private List<SeriennrChargennrMitMengeDto> getAvailableSnrsArtikelset(
			Integer auftragpositionIId, List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			if (artikelset.getHead().getIId().equals(auftragpositionIId))
				return artikelset.getIdentities();
		}

		// TODO: Eigentlich(?) Ist das ein Fehler wenn ich hier kein Artikelset
		// finde!?
		return new ArrayList<SeriennrChargennrMitMengeDto>();
	}

	private Artikelset getAppropriateArtikelset(Integer auftragpositionIId,
			List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			if (artikelset.getHead().getIId().equals(auftragpositionIId))
				return artikelset;
		}

		return null;
	}

	protected void uebernimmAuftragsposition(boolean bEsGibtNochPositiveOffene,
			LieferscheinDto lieferscheinDto,
			AuftragpositionDto auftragpositionDto,
			List<Artikelset> artikelsets, TheClientDto theClientDto)
			throws RemoteException {

		if (AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
				.equals(auftragpositionDto.getAuftragpositionstatusCNr()))
			return;

		if (auftragpositionDto.getNMenge() == null) {
			LieferscheinpositionDto lieferscheinpositionDto = auftragpositionDto
					.cloneAsLieferscheinpositionDto();
			lieferscheinpositionDto
					.setLieferscheinIId(lieferscheinDto.getIId());
			lieferscheinpositionDto.setISort(null);
			getLieferscheinpositionFac().createLieferscheinposition(
					lieferscheinpositionDto, false, theClientDto);
			return;
		}

		// IMS 2129
		// wenn es noch positive offene gibt, dann duerfen die
		// negativen noch nicht geliefert werden
		if (auftragpositionDto.getNMenge().signum() < 0
				&& bEsGibtNochPositiveOffene) {
			return;
		}

		// dieses Flag legt fest, ob eine Lieferscheinposition
		// fuer die aktuelle
		// Auftragposition angleget oder aktualisiert werden
		// soll
		boolean bLieferscheinpositionErzeugen = false;

		// die Menge, mit der eine neue Lieferscheinposition
		// angelegt oder eine
		// bestehende Lieferscheinposition aktualisiert werden
		// soll
		BigDecimal nMengeFuerLieferscheinposition = null;

		// die Serien- oder Chargennummer, die bei der Abbuchung
		// verwendet werden soll
		String cSerienchargennummer = null;

		if (auftragpositionDto.getPositionsartCNr().equals(
				AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {

			// PJ17906
			if (auftragpositionDto.getNMenge() != null
					&& auftragpositionDto.getNMenge().doubleValue() == 0) {
				auftragpositionDto
						.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);
				getAuftragpositionFac().updateOffeneMengeAuftragposition(
						auftragpositionDto.getIId(), theClientDto);
				return;
			}

			bLieferscheinpositionErzeugen = true;
			nMengeFuerLieferscheinposition = auftragpositionDto
					.getNOffeneMenge();
		} else if (auftragpositionDto.getPositionsartCNr().equals(
				AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {

			// PJ17906
			if (auftragpositionDto.getNMenge() != null
					&& auftragpositionDto.getNMenge().doubleValue() == 0) {
				auftragpositionDto
						.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);
				getAuftragpositionFac().updateOffeneMengeAuftragposition(
						auftragpositionDto.getIId(), theClientDto);
				return;
			}

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					auftragpositionDto.getArtikelIId(), theClientDto);

			// nicht lagerbewirtschaftete Artikel werden mit der
			// vollen offenen Menge uebernommen sofern es nicht ein
			// Artikelset-Kopfartikel ist
			if (!artikelDto.isLagerbewirtschaftet()) {
				bLieferscheinpositionErzeugen = true;

				if (isArtikelSetHead(artikelDto.getIId(), theClientDto)) {
					BigDecimal verfuegbareMenge = getAvailableAmountArtikelset(
							auftragpositionDto.getIId(), artikelsets);
					nMengeFuerLieferscheinposition = auftragpositionDto
							.getNOffeneMenge().min(verfuegbareMenge);
				} else {
					nMengeFuerLieferscheinposition = auftragpositionDto
							.getNOffeneMenge();
				}
			} else {
				if (artikelDto.isSeriennrtragend()) {
					// seriennummerbehaftete Artikel werden nur
					// uebernommen wenn sie in einem Artikelset vorhanden sind
					if (auftragpositionDto.getPositioniIdArtikelset() != null) {
						Artikelset artikelset = getAppropriateArtikelset(
								auftragpositionDto.getPositioniIdArtikelset(),
								artikelsets);
						if (null != artikelset) {
							BigDecimal sollsatzgroesse = auftragpositionDto
									.getNMenge().divide(
											artikelset.getHead().getNMenge());
							nMengeFuerLieferscheinposition = sollsatzgroesse
									.multiply(artikelset.getAvailableAmount());
							bLieferscheinpositionErzeugen = true;
						}
						// BigDecimal verfuegbareMenge =
						// getAvailableAmountArtikelset(auftragpositionDto.
						// getPositioniIdArtikelset(),
						// artikelsets) ;
						// bLieferscheinpositionErzeugen = true ;
						// if(verfuegbareMenge != null) {
						// nMengeFuerLieferscheinposition =
						// auftragpositionDto.getNOffeneMenge() ;
						// }
					}
				} else if (artikelDto.isChargennrtragend()) {
					// chargennummernbehaftete Artikel koennen
					// nur uebernommen werden, wenn
					// es nur eine Charge gibt und mit der
					// Menge, die in dieser Charge
					// vorhanden ist
					SeriennrChargennrAufLagerDto[] alleChargennummern = getLagerFac()
							.getAllSerienChargennrAufLager(artikelDto.getIId(),
									lieferscheinDto.getLagerIId(),
									theClientDto, true, false);
					if (alleChargennummern != null
							&& alleChargennummern.length == 1) {
						BigDecimal nLagerstd = alleChargennummern[0]
								.getNMenge();
						cSerienchargennummer = alleChargennummern[0]
								.getCSeriennrChargennr();

						if (nLagerstd.signum() > 0) {
							bLieferscheinpositionErzeugen = true;
							nMengeFuerLieferscheinposition = auftragpositionDto
									.getNOffeneMenge().min(nLagerstd);
						}
					}
				} else {
					// bei lagerbewirtschafteten Artikeln muss
					// die Menge auf Lager
					// beruecksichtigt werden
					BigDecimal nMengeAufLager = getLagerFac().getMengeAufLager(
							artikelDto.getIId(), lieferscheinDto.getLagerIId(),
							null, theClientDto);

					if (nMengeAufLager.signum() > 0) {
						bLieferscheinpositionErzeugen = true;
						if (auftragpositionDto.getPositioniIdArtikelset() != null) {

							Artikelset artikelset = getAppropriateArtikelset(
									auftragpositionDto
											.getPositioniIdArtikelset(),
									artikelsets);
							if (null != artikelset) {
								BigDecimal sollsatzGroesse = auftragpositionDto
										.getNMenge().divide(
												artikelset.getHead()
														.getNMenge());
								nMengeFuerLieferscheinposition = auftragpositionDto
										.getNOffeneMenge()
										.min(artikelset.getAvailableAmount())
										.multiply(sollsatzGroesse)
										.min(nMengeAufLager);
							} else {
								nMengeFuerLieferscheinposition = BigDecimal.ONE;
							}
						} else {
							nMengeFuerLieferscheinposition = auftragpositionDto
									.getNOffeneMenge().min(nMengeAufLager);
						}
					}
				}
			}
			// TODO: Vorerst mal eine Zwischensummenposition auf "OFFEN"
			// belassen. ghp, 07.09.2012
			// } else
			// if(auftragpositionDto.getAuftragpositionartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME))
			// {
			// auftragpositionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)
			// ;
			// getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(auftragpositionDto,
			// theClientDto) ;
		}

		if (bLieferscheinpositionErzeugen
				&& nMengeFuerLieferscheinposition != null) {
			LieferscheinpositionDto lieferscheinpositionBisherDto = getLieferscheinpositionByLieferscheinAuftragposition(
					lieferscheinDto.getIId(), auftragpositionDto.getIId());

			if (lieferscheinpositionBisherDto == null) {
				LieferscheinpositionDto lieferscheinpositionDto = auftragpositionDto
						.cloneAsLieferscheinpositionDto();

				if (auftragpositionDto.getPositioniIdArtikelset() != null) {
					LieferscheinpositionDto[] lPositionDtos = null;
					Query query = em
							.createNamedQuery("LieferscheinpositionfindByAuftragposition");
					query.setParameter(1,
							auftragpositionDto.getPositioniIdArtikelset());
					Collection<?> cl = query.getResultList();
					lPositionDtos = assembleLieferscheinpositionDtos(cl);

					for (int j = 0; j < lPositionDtos.length; j++) {
						if (lPositionDtos[j].getLieferscheinIId().equals(
								lieferscheinDto.getIId())) {
							lieferscheinpositionDto
									.setPositioniIdArtikelset(lPositionDtos[j]
											.getIId());
							break;
						}
					}

					getBelegVerkaufFac().setupPositionWithIdentities(
							lieferscheinpositionDto,
							getAvailableSnrsArtikelset(auftragpositionDto
									.getPositioniIdArtikelset(), artikelsets),
							theClientDto);

					cSerienchargennummer = null;
				}

				lieferscheinpositionDto.setLieferscheinIId(lieferscheinDto
						.getIId());

				// TODO: Problematisch bei seriennummern-artikeln (artikelset?)
				lieferscheinpositionDto
						.setNMenge(nMengeFuerLieferscheinposition);

				if (null != cSerienchargennummer) {
					lieferscheinpositionDto
							.setSeriennrChargennrMitMenge(SeriennrChargennrMitMengeDto
									.erstelleDtoAusEinerChargennummer(
											cSerienchargennummer,
											nMengeFuerLieferscheinposition));
				}
				lieferscheinpositionDto.setISort(null);
				getLieferscheinpositionFac().createLieferscheinposition(
						lieferscheinpositionDto, false, theClientDto);
			} else {
				lieferscheinpositionBisherDto
						.setNMenge(nMengeFuerLieferscheinposition);

				if (auftragpositionDto.getPositionsartCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									auftragpositionDto.getArtikelIId(),
									theClientDto);

					if (artikelDto.isChargennrtragend()
							|| artikelDto.isSeriennrtragend()) {
						lieferscheinpositionBisherDto
								.setSeriennrChargennrMitMenge(SeriennrChargennrMitMengeDto
										.erstelleDtoAusEinerChargennummer(
												cSerienchargennummer,
												nMengeFuerLieferscheinposition));
					} else {
						lieferscheinpositionBisherDto
								.setSeriennrChargennrMitMenge(null);
					}

				}

				getLieferscheinpositionFac()
						.updateLieferscheinpositionSichtAuftrag(
								lieferscheinpositionBisherDto, theClientDto);
			}
		}
	}

	private boolean isAuftragPositionStatusErledigt(
			AuftragpositionDto auftragpositionDto) {
		return AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
				.equals(auftragpositionDto.getAuftragpositionstatusCNr());
	}

	private boolean gibtEsPositivOffeneAuftragsMengen(
			AuftragpositionDto[] auftragpositionen) {
		for (AuftragpositionDto auftragpositionDto : auftragpositionen) {
			if (isAuftragPositionStatusErledigt(auftragpositionDto))
				continue;
			if (auftragpositionDto.getNMenge() == null)
				continue;
			if (auftragpositionDto.getNMenge().signum() > 0)
				return true;
		}

		return false;
	}

	public boolean isAuftragpositionenMengeVorhanden(
			AuftragpositionDto[] auftragpositionenDto, Integer lagerIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			// boolean bEsGibtNochPositiveOffene = false;
			// if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
			// MandantFac.ZUSATZFUNKTION_VERLEIH, theClientDto)) {
			// bEsGibtNochPositiveOffene =
			// gibtEsPositivOffeneAuftragsMengen(auftragpositionenDto) ;
			// }

			for (AuftragpositionDto auftragpositionDto : auftragpositionenDto) {
				if (isAuftragPositionStatusErledigt(auftragpositionDto))
					continue;
				if (null == auftragpositionDto.getNMenge())
					continue;

				// TODO: Negative (und 0 Menge) vorerst nicht beruecksichtigen
				if (auftragpositionDto.getNMenge().signum() <= 0)
					continue;

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								auftragpositionDto.getArtikelIId(),
								theClientDto);

				if (!artikelDto.isLagerbewirtschaftet())
					continue;

				BigDecimal verfuegbareMenge = getLagerFac().getLagerstand(
						auftragpositionDto.getArtikelIId(), lagerIId,
						theClientDto);

				if (verfuegbareMenge.compareTo(auftragpositionDto
						.getNOffeneMenge()) < 0)
					return false;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return true;
	}

	/**
	 * Bei einem auftragbezogenen Lieferschein ist es moeglich, all jene offenen
	 * oder teilerledigten Auftragpositionen innerhalb einer Transaktion zu
	 * uebernehmen, die keine Benutzerinteraktion benoetigen. <br>
	 * Es gilt:
	 * <ul>
	 * <li>Handeingabepositionen werden uebernommen.
	 * <li>Nicht Serien- oder Chargennummertragende Artikelpositionen werden mit
	 * jener Menge uebernommen, die auf Lager liegt.
	 * <li>Artikelpositionen mit Seriennummer werden nicht uebernommen.
	 * <li>Artikelpositionen mit Chargennummer werden mit jener Menge
	 * uebernommen, die auf Lager liegt, wenn es genau eine Charge gibt.
	 * </ul>
	 * Die restlichen Positionen koennen nicht automatisch uebernommen werden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param auftragIIdI
	 *            Integer
	 * @param theClientDto
	 *            String der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
			Integer iIdLieferscheinI, Integer auftragIIdI,
			List<Artikelset> artikelsets, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkLieferscheinIId(iIdLieferscheinI);
		try {
			LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(iIdLieferscheinI);
			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragIIdI);

			boolean bEsGibtNochPositiveOffene = false;
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_VERLEIH, theClientDto)) {
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					if (!AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
							.equals(aAuftragpositionDto[i]
									.getAuftragpositionstatusCNr())) {
						if (aAuftragpositionDto[i].getNMenge() != null
								&& aAuftragpositionDto[i].getNMenge()
										.doubleValue() > 0) {
							bEsGibtNochPositiveOffene = true;
						}
					}
				}
			}

			for (int i = 0; i < aAuftragpositionDto.length; i++) {
				uebernimmAuftragsposition(bEsGibtNochPositiveOffene,
						lieferscheinDto, aAuftragpositionDto[i], artikelsets,
						theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Einen bestehenden Lieferschein ohne weitere Aktion aktualisieren. <br>
	 * Ein eventuelle Bezug zu Auftraegen bleibt dabei unberuecksichtig.
	 * 
	 * @param lieferscheinDtoI
	 *            der aktuelle Lieferschein
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateLieferscheinOhneWeitereAktion(
			LieferscheinDto lieferscheinDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		lieferscheinDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
		lieferscheinDtoI.setTAendern(getTimestamp());

		// try {
		Lieferschein lieferschein = em.find(Lieferschein.class,
				lieferscheinDtoI.getIId());
		if (lieferschein == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lieferschein.setTAendern(getTimestamp());
		lieferschein.setPersonalIIdAendern(theClientDto.getIDPersonal());
		setLieferscheinFromLieferscheinDto(lieferschein, lieferscheinDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	@Override
	public void setzeVersandzeitpunktAufJetzt(Integer lieferscheinIId,
			String druckart) {
		if (lieferscheinIId != null) {
			Lieferschein lieferschein = em.find(Lieferschein.class,
					lieferscheinIId);
			// PJ17915
			if (!lieferschein.getLieferscheinstatusCNr().equals(
					LieferscheinFac.LSSTATUS_ANGELEGT)) {

				lieferschein.setTVersandzeitpunkt(new Timestamp(System
						.currentTimeMillis()));
				lieferschein.setCVersandtype(druckart);
				em.merge(lieferschein);
				em.flush();
			}
		}

	}
	
	
	public class LieferscheinAvisoProducerCC extends CleverCureProducer implements ILieferscheinAvisoProducer {
		public final static String GENERATOR_INFO = "2.4" ;
		
		@Override
		public boolean isDummy() {
			return false;
		}

		@Override
		public ILieferscheinAviso createAviso(LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException,
				NamingException, EJBExceptionLP {
			LieferscheinAvisoCC aviso = new LieferscheinAvisoCC() ;
			XMLXMLDISPATCHNOTIFICATION dn = createDn(aviso, lieferscheinDto, theClientDto) ; 
			aviso.setNotification(dn) ;
			return aviso ;
		}

		@Override
		public String toString(ILieferscheinAviso aviso) {
			return aviso instanceof LieferscheinAvisoCC ? fromXml((LieferscheinAvisoCC) aviso) : null ;	
		}
		
		private HttpURLConnection buildUrlconnectionPost(ILieferscheinAviso aviso) 
				throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, KeyManagementException, UnrecoverableKeyException {
			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac()
					.versandwegPartnerFindByPrimaryKey(aviso.getVersandwegId(), aviso.getPartnerId()) ;
			if(versandwegPartnerDto == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_PARTNER_KUNDENNUMMER_FEHLT, 
						aviso.getPartnerId().toString()) ;
			}
			VersandwegCCPartnerDto ccPartnerDto = (VersandwegCCPartnerDto) versandwegPartnerDto ;
			
			String uri = getCCEndpunkt(em, ccPartnerDto)  ;
			uri += "&datatype=dnd" ;
			uri += "&companycode=" + ccPartnerDto.getCKundennummer().trim() ;
			uri += "&password=" + ccPartnerDto.getCKennwort().trim() ;
			
			URL requestedUrl = new URL(uri) ;
		    HttpURLConnection urlConnection = (HttpURLConnection) requestedUrl.openConnection();
		    if(urlConnection instanceof HttpsURLConnection) {
				KeyStore keystore = getKeystore(ccPartnerDto) ;
				SSLContext sslContext = getSslContext(ccPartnerDto, keystore) ;
				
		        ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());
		    }
		    urlConnection.setRequestMethod("POST");
		    urlConnection.setRequestProperty("User-Agent", "HELIUM V");
		    urlConnection.setConnectTimeout(10000);
		    urlConnection.setReadTimeout(10500);
		    urlConnection.setDoOutput(true);

		    return urlConnection ;
		}
		
		@Override
		public void postAviso(ILieferscheinAviso aviso) {
			if(!(aviso instanceof LieferscheinAvisoCC)) return ;
			
			try {
				HttpURLConnection urlConnection = buildUrlconnectionPost(aviso) ;
			    urlConnection.setRequestProperty("Content-Type", "text/xml") ;
				OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream()) ;
				String xmlDndContent = toString(aviso) ;
				out.write(xmlDndContent) ;
				out.flush() ;
				out.close() ;

			    int lastResponseCode = urlConnection.getResponseCode();

			    InputStream s = urlConnection.getInputStream() ;
				BufferedReader br = new BufferedReader(new InputStreamReader(s)) ;
				String theContent = "" ;
				String line = ""; 
				while((line = br.readLine()) != null) {
					theContent += line + "\n" ;
				}				    
			    String lastContentType = urlConnection.getContentType();

			    System.out.println("response: " + lastResponseCode + " content-type: " + lastContentType + " for: " + theContent) ;				
			} catch(IOException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HTTP_POST_IO, e) ;
			} catch (KeyManagementException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_MANAGMENT, e) ;
			} catch (UnrecoverableKeyException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_RECOVER, e) ;
			} catch (NoSuchAlgorithmException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_ALGORITHMEN, e) ;
			} catch (CertificateException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_CERTIFICATE, e) ;
			} catch (KeyStoreException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE, e) ;
			}
		}
		
		private String fromXml(LieferscheinAvisoCC aviso) {
			StringWriter writer = new StringWriter() ;
			try {
				JAXBContext context = JAXBContext.newInstance(aviso.getNotification().getClass().getPackage().getName()) ;
				Marshaller m = context.createMarshaller() ;
				m.marshal(aviso.getNotification(), writer) ;
				String s = writer.toString() ;
				return s ;
			} catch(JAXBException e) {
				System.out.println("JAXBException" + e.getMessage()) ;
			}
			
			return null ;			
		}
		
		private XMLXMLDISPATCHNOTIFICATION createDn(ILieferscheinAviso aviso, LieferscheinDto lieferscheinDto, TheClientDto theClientDto) throws RemoteException {
			XMLXMLDISPATCHNOTIFICATION dn = new XMLXMLDISPATCHNOTIFICATION() ;
			XMLXMLDISPATCHNOTIFICATIONHEADER dnHeader = new XMLXMLDISPATCHNOTIFICATIONHEADER() ;
			XMLXMLCONTROLINFO controlInfo = new XMLXMLCONTROLINFO() ;
			controlInfo.setGENERATORINFO(GENERATOR_INFO);		
			controlInfo.setGENERATIONDATE(
					formatAsIso8601Timestamp(GregorianCalendar.getInstance().getTime()));
			dnHeader.setCONTROLINFO(controlInfo) ;
			
			Collection<LieferscheinpositionDto> positionDtos = getLSPositionsDto(lieferscheinDto, theClientDto) ;
			
			XMLXMLDISPATCHNOTIFICATIONINFO dnInfo = buildDnInfo(aviso, lieferscheinDto, theClientDto) ;
			dnHeader.setDISPATCHNOTIFICATIONINFO(dnInfo);
			dn.setDISPATCHNOTIFICATIONHEADER(dnHeader) ;
			
			XMLXMLDISPATCHNOTIFICATIONITEMLIST dnItemList= buildDnItemList(lieferscheinDto, positionDtos, theClientDto) ;
			dn.setDISPATCHNOTIFICATIONITEMLIST(dnItemList);
			
			XMLXMLDISPATCHNOTIFICATIONSUMMARY dnSummary = buildDnSummary(lieferscheinDto, positionDtos, theClientDto) ;
			dn.setDISPATCHNOTIFICATIONSUMMARY(dnSummary);

			return dn ;
		}

		public class LieferscheinpositionenNurIdentFilter implements Predicate, Serializable {
			private static final long serialVersionUID = -5617572280308975044L;

			@Override
			public boolean evaluate(Object arg0) {
				if(arg0 instanceof LieferscheinpositionDto) {
					LieferscheinpositionDto pos = (LieferscheinpositionDto) arg0 ;
					return LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT.equals(
							pos.getLieferscheinpositionartCNr()) ; 
				}

				return false;
			}		
		}
		
		private Collection<LieferscheinpositionDto> getLSPositionsDto(
				LieferscheinDto lieferscheinDto, TheClientDto theClientDto) throws RemoteException {
			Collection<LieferscheinpositionDto> ejbPositions = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(lieferscheinDto.getIId(), theClientDto) ;
			CollectionUtils.filter(ejbPositions, new LieferscheinpositionenNurIdentFilter());
			return ejbPositions ;
		}
		
		private XMLXMLDISPATCHNOTIFICATIONINFO buildDnInfo(
				ILieferscheinAviso aviso, LieferscheinDto lieferscheinDto, TheClientDto theClientDto) throws RemoteException {
			XMLXMLDISPATCHNOTIFICATIONINFO dnInfo = new XMLXMLDISPATCHNOTIFICATIONINFO() ;
			dnInfo.setCCROWID(
					lieferscheinDto.getIId() + "_" + lieferscheinDto.getCNr() + "_" + lieferscheinDto.getTAendern());

			dnInfo.setDISPATCHNOTIFICATIONID(lieferscheinDto.getCNr());

			Timestamp t = lieferscheinDto.getTLiefertermin() ;
			if(t == null) t = lieferscheinDto.getTBelegdatum() ;
			if(t == null) t = new Timestamp(System.currentTimeMillis());
			dnInfo.setDISPATCHNOTIFICATIONDATE(formatAsIso8601Timestamp(new Date(t.getTime())));

			if(!HelperWebshop.isEmptyString(lieferscheinDto.getCBezProjektbezeichnung())) {
				XMLXMLREMARK dnRemark = new XMLXMLREMARK() ;
				dnRemark.setValue(lieferscheinDto.getCBezProjektbezeichnung());
				dnInfo.getREMARK().add(dnRemark) ;
			}
			
			XMLXMLBUYERPARTY buyerParty = new XMLXMLBUYERPARTY() ;
			XMLXMLPARTY xmlParty = new XMLXMLPARTY() ;
			XMLXMLPARTYID xmlPartyId = new XMLXMLPARTYID() ;		
			xmlPartyId.setValue(getLieferantennummer(lieferscheinDto.getKundeIIdRechnungsadresse(), theClientDto)) ;
			xmlParty.setPARTYID(xmlPartyId);
			buyerParty.setPARTY(xmlParty);
			dnInfo.setBUYERPARTY(buyerParty) ;

			XMLXMLSUPPLIERPARTY supplierParty = new XMLXMLSUPPLIERPARTY() ;
			XMLXMLPARTY xmlSupplierParty = new XMLXMLPARTY() ;
			XMLXMLPARTYID xmlSupplierPartyId = new XMLXMLPARTYID() ;		
			xmlSupplierPartyId.setValue(getSupplierPartyId(aviso, lieferscheinDto, theClientDto)) ;
			xmlSupplierParty.setPARTYID(xmlSupplierPartyId);
			supplierParty.setPARTY(xmlSupplierParty);
			dnInfo.setSUPPLIERPARTY(supplierParty);
			
			return dnInfo ;
		}

	
		private String getSupplierPartyId(ILieferscheinAviso aviso, LieferscheinDto lieferscheinDto, TheClientDto theClientDto) throws RemoteException {
			Integer versandwegId = getVersandwegIdFor(lieferscheinDto, theClientDto) ;
			if(versandwegId == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT, "") ;
			}

			Integer kundeId = lieferscheinDto.getKundeIIdLieferadresse() ;
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto) ;

			aviso.setVersandwegId(versandwegId) ;
			aviso.setPartnerId(kundeDto.getPartnerIId());
			
			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac()
					.versandwegPartnerFindByPrimaryKey(versandwegId, aviso.getPartnerId()) ;
			if(versandwegPartnerDto == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_PARTNER_KUNDENNUMMER_FEHLT, 
						kundeDto.getPartnerIId().toString()) ;
			}
			VersandwegCCPartnerDto partnerDto = (VersandwegCCPartnerDto) versandwegPartnerDto ;
			String supplierId = partnerDto.getCKundennummer() ;
			return supplierId.trim();
		}
		
		private XMLXMLDISPATCHNOTIFICATIONITEMLIST buildDnItemList(LieferscheinDto lieferscheinDto,
				Collection<LieferscheinpositionDto> ejbPositions, TheClientDto theClientDto) throws RemoteException {
			XMLXMLDISPATCHNOTIFICATIONITEMLIST itemlist = new XMLXMLDISPATCHNOTIFICATIONITEMLIST();
			
			Integer lineitemId = 0 ;
			for (LieferscheinpositionDto lieferscheinpositionDto : ejbPositions) {
				XMLXMLDISPATCHNOTIFICATIONITEM dnItem = new XMLXMLDISPATCHNOTIFICATIONITEM() ;
				dnItem.setCCROWID(lieferscheinpositionDto.getIId().toString()) ;
				dnItem.setLINEITEMID((++lineitemId).toString());
				dnItem.setCCORDERUNIT(lieferscheinpositionDto.getEinheitCNr().trim()) ;

				ArtikelDto itemDto = getArtikelFac()
						.artikelFindByPrimaryKey(lieferscheinpositionDto.getArtikelIId(), theClientDto) ;
				dnItem.setCCORIGINCOUNTRY(buildUrsprungsland(itemDto));

				XMLXMLARTICLEID itemId = new XMLXMLARTICLEID() ;
				itemId.setSUPPLIERAID(buildItemCnr(itemDto));
				if(itemDto.getArtikelsprDto() != null) {
					itemId.setDESCRIPTIONSHORT(
							HelperWebshop.isEmptyString(itemDto.getArtikelsprDto().getCKbez()) 
								? itemDto.getCNr() : itemDto.getArtikelsprDto().getCKbez());
				} else {
					itemId.setDESCRIPTIONSHORT(itemDto.getCNr());
				}
				KundesokoDto[] sokos = getKundesokoFac().kundesokoFindByKundeIIdArtikelIId(
						lieferscheinDto.getKundeIIdLieferadresse(), itemDto.getIId()) ;
				if(sokos != null && sokos.length > 0) {
					XMLXMLBUYERAID buyerItemId = new XMLXMLBUYERAID() ;
					buyerItemId.setValue(sokos[0].getCKundeartikelnummer()) ;
					buyerItemId.setType("buyer_specific");
					List<XMLXMLBUYERAID> buyerItemlist = itemId.getBUYERAID() ;					
					buyerItemlist.add(buyerItemId) ;					
				}

				dnItem.setARTICLEID(itemId);
				dnItem.setQUANTITY(ccScaled3(lieferscheinpositionDto.getNMenge())); 

				buildOrderReference(dnItem, lieferscheinpositionDto);
				buildDeliveryInfo(dnItem, lieferscheinDto, lieferscheinpositionDto) ;
				
				itemlist.getDISPATCHNOTIFICATIONITEM().add(dnItem) ;
			}

			return itemlist;
		}
		

		
		private String getOrderReference(AuftragpositionDto auftragpositionDto) throws RemoteException {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKeyOhneExc(auftragpositionDto.getBelegIId()) ;
			if(auftragDto == null) return "" ;
			
			return auftragDto.getCBestellnummer() ;
 		}
		
		private void buildOrderReference(XMLXMLDISPATCHNOTIFICATIONITEM dnItem, LieferscheinpositionDto lsposDto) throws RemoteException {
			if(lsposDto.getAuftragpositionIId() != null) {
				XMLXMLORDERREFERENCE orderReference = new XMLXMLORDERREFERENCE() ;
				
				AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKeyOhneExc(lsposDto.getAuftragpositionIId()) ;
				if(auftragpositionDto != null) {
					orderReference.setLINEITEMID(extractLineItemIdRef(auftragpositionDto));
					orderReference.setORDERID(getOrderReference(auftragpositionDto));
					dnItem.setORDERREFERENCE(orderReference) ;						
				}
			}		
		}
		
		private void buildDeliveryInfo(XMLXMLDISPATCHNOTIFICATIONITEM dnItem, LieferscheinDto lieferscheinDto, LieferscheinpositionDto lsposDto) throws RemoteException {			
			Calendar c = GregorianCalendar.getInstance() ;
			c.setTimeInMillis(lieferscheinDto.getTLiefertermin().getTime());
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0) ;
			String theDate = formatAsIso8601Timestamp(c.getTime()) ;
			
			XMLXMLDELIVERYDATE deliveryDate = new XMLXMLDELIVERYDATE() ;
			deliveryDate.setDELIVERYSTARTDATE(theDate);
			deliveryDate.setDELIVERYENDDATE(theDate);
			dnItem.setDELIVERYDATE(deliveryDate);
		}
		
		private String buildUrsprungsland(ArtikelDto itemDto) {
			if(itemDto.getLandIIdUrsprungsland() == null) {
				return "" ;
			}
			Integer landId = itemDto.getLandIIdUrsprungsland() ;
			LandDto landDto = getSystemFac().landFindByPrimaryKey(landId) ;
			return landDto.getCLkz() ;
		}
		
		private String buildItemCnr(ArtikelDto itemDto) {
			String cnr = itemDto.getCNr() ;
//			if(itemDto.getCRevision() != null && itemDto.getCRevision().trim().length() > 0) {
//				cnr = cnr + "-" +itemDto.getCRevision().trim() ;
//			}
			
			return cnr ;
		}
		
		private String getLieferantennummer(Integer kundeIId, TheClientDto theClientDto) {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto) ;
			return kundeDto.getCLieferantennr() ;
		}
		
		private XMLXMLDISPATCHNOTIFICATIONSUMMARY buildDnSummary(LieferscheinDto lieferscheinDto,
				Collection<LieferscheinpositionDto> positionDtos, TheClientDto theClientDto) {
			XMLXMLDISPATCHNOTIFICATIONSUMMARY dnSummary = new XMLXMLDISPATCHNOTIFICATIONSUMMARY() ;
			int itemCount = positionDtos.size() ;
			
			dnSummary.setTOTALITEMNUM(new BigInteger(String.valueOf(itemCount))) ;
			return dnSummary ;
		}		
	}
	
	public class LieferscheinAvisoProducerDummy implements ILieferscheinAvisoProducer {

		@Override
		public boolean isDummy() {
			return true;
		}

		@Override
		public ILieferscheinAviso createAviso(LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException,
				NamingException, EJBExceptionLP {
			return null;
		}

		@Override
		public String toString(ILieferscheinAviso aviso) {
			return null;
		}	
		
		@Override
		public void postAviso(ILieferscheinAviso aviso) {
		}
	}
	
	public class LieferscheinAvisoProducerCCSelfSender extends LieferscheinAvisoProducerCC {
		@Override
		protected String getCCEndpunkt(EntityManager em,
				VersandwegCCPartnerDto ccPartnerDto) {
			return "http://localhost:8280/restapi/services/rest/api/beta/cc?fake=yes";
		}
	}
	
	private class LieferscheinAvisoFactory {
		private ILieferscheinAvisoProducer getProducerCC() {
//			return new LieferscheinAvisoProducerCCSelfSender() ;
			return new LieferscheinAvisoProducerCC() ;
		}
		
		public ILieferscheinAvisoProducer getProducer(
				LieferscheinDto lieferscheinDto, TheClientDto theClientDto) throws RemoteException, NamingException {
			Integer versandwegId = getVersandwegIdFor(lieferscheinDto, theClientDto) ;
			if(null == versandwegId ) return new LieferscheinAvisoProducerDummy() ;
			
			Versandweg versandweg = em.find(Versandweg.class, versandwegId) ;
			if(null == versandweg) throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, versandwegId.toString()) ;

			if(SystemFac.VersandwegType.CleverCureVerkauf.equals(versandweg.getCnr().trim())) return getProducerCC() ;
			return null ;
		}
	}
	
	private LieferscheinAvisoFactory avisoFactory = new LieferscheinAvisoFactory() ;

	private Integer getVersandwegIdFor(LieferscheinDto lieferscheinDto, TheClientDto theClientDto) throws RemoteException {
		return getVersandwegIdFor(lieferscheinDto.getKundeIIdLieferadresse(), theClientDto) ;
	}
	
	private Integer getVersandwegIdFor(Integer kundeId, TheClientDto theClientDto) throws RemoteException {
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto) ;
		
		Integer versandwegId = kundeDto.getPartnerDto().getVersandwegIId() ;
		return versandwegId ;
	}
	
	public ILieferscheinAviso createLieferscheinAviso(Integer lieferscheinIId, TheClientDto theClientDto) throws NamingException, RemoteException {
		Validator.notNull(lieferscheinIId, "lieferscheinIId") ;
		Validator.notNull(theClientDto, "theClientDto") ;
		Validator.notEmpty(theClientDto.getMandant(), "theClient.mandant");
		
		LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(lieferscheinIId) ;
		if(!lieferscheinDto.getMandantCNr().equals(theClientDto.getMandant())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FALSCHER_MANDANT, lieferscheinDto.getIId().toString());
		}

		if(lieferscheinDto.getTLieferaviso() != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_AVISO_BEREITS_DURCHGEFUEHRT,
					lieferscheinDto.getTLieferaviso().toString()) ;
		}
		
		ILieferscheinAvisoProducer avisoProducer = avisoFactory.getProducer(lieferscheinDto, theClientDto) ;
		if(null == avisoProducer) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_NICHT_UNTERSTUETZT, "") ;
		}
		
		ILieferscheinAviso aviso = avisoProducer.createAviso(lieferscheinDto, theClientDto) ;
//		if(aviso != null) {
//			Lieferschein ls = em.find(Lieferschein.class, lieferscheinDto.getIId())  ;		
//			ls.setTLieferaviso(new Timestamp(System.currentTimeMillis()));
//			ls.setPersonalIIdLieferaviso(theClientDto.getIDPersonal());
//			em.merge(ls) ;
//			em.flush() ;
//			
//		}
		
		return aviso ;
	}
	
	private void updateAvisoTimestamp(Integer lieferscheinIId, TheClientDto theClientDto) {
		Lieferschein ls = em.find(Lieferschein.class, lieferscheinIId)  ;		
		ls.setTLieferaviso(new Timestamp(System.currentTimeMillis()));
		ls.setPersonalIIdLieferaviso(theClientDto.getIDPersonal());
		em.merge(ls) ;
		em.flush() ;		
	}
	
	@Override
	public void resetLieferscheinAviso(Integer lieferscheinIId, TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(lieferscheinIId, "lieferscheinIId") ;
		Validator.notNull(theClientDto, "theClientDto") ;		
		
		Lieferschein ls = em.find(Lieferschein.class, lieferscheinIId)  ;
		if(ls == null) return ;
		
		if(!ls.getMandantCNr().equals(theClientDto.getMandant())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FALSCHER_MANDANT, lieferscheinIId.toString());
		}
		
		ls.setTLieferaviso(null);
		ls.setPersonalIIdLieferaviso(theClientDto.getIDPersonal());
		em.merge(ls) ;
		em.flush() ;
	}
	
	private String lieferscheinAvisoToStringImpl(
			ILieferscheinAviso aviso, LieferscheinDto lieferscheinDto, 
			TheClientDto theClientDto) throws RemoteException, NamingException {
		ILieferscheinAvisoProducer producer = avisoFactory.getProducer(lieferscheinDto, theClientDto) ;
		return producer.toString(aviso) ;
	}
	
	@Override
	public String lieferscheinAvisoToString(
			LieferscheinDto lieferscheinDto, ILieferscheinAviso lieferscheinAviso, 
			TheClientDto theClientDto) throws RemoteException, NamingException {
		Validator.notNull(lieferscheinDto, "lieferscheinDto") ;
		Validator.notNull(lieferscheinAviso, "lieferscheinAviso");
		Validator.notNull(theClientDto, "theClientDto") ;		

		return lieferscheinAvisoToStringImpl(lieferscheinAviso, lieferscheinDto, theClientDto) ;
	}
	
	@Override
	public String createLieferscheinAvisoToString(
			Integer lieferscheinIId, TheClientDto theClientDto) throws RemoteException, NamingException {
		ILieferscheinAviso aviso = createLieferscheinAviso(lieferscheinIId, theClientDto) ;
		if(aviso == null) return null ;
		LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(lieferscheinIId) ;
		return aviso == null ? null : lieferscheinAvisoToStringImpl(aviso, lieferscheinDto, theClientDto) ;
	}
	
	private void archiveAvisoDocument(LieferscheinDto lieferscheinDto, String xmlContent, TheClientDto theClientDto) {
		
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
				theClientDto.getIDPersonal(), theClientDto);

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocPath dp = new DocPath(new DocNodeLieferschein(lieferscheinDto)).add(new DocNodeFile("Lieferaviso_Clevercure.xml")) ;
		jcrDocDto.setDocPath(dp) ;
		jcrDocDto.setbData(xmlContent.getBytes());
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(partnerDto.getIId());
		
		Integer kundeId = lieferscheinDto.getKundeIIdLieferadresse() ;
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto) ;
		
		jcrDocDto.setlPartner(kundeDto.getPartnerIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(lieferscheinDto.getCNr().replace("/", "."));
		jcrDocDto.setsFilename(lieferscheinDto.getCNr().replace("/", "."));
		jcrDocDto.setsMIME(".xml");
		jcrDocDto.setsName("Lieferaviso Clevercure" + lieferscheinDto.getIId());
		jcrDocDto.setsRow(lieferscheinDto.getIId().toString());
		jcrDocDto.setsTable("LIEFERSCHEIN");
		String sSchlagworte = "Export Clevercure XML Dispatchnotification Aviso dnd";
		jcrDocDto.setsSchlagworte(sSchlagworte);
		getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, theClientDto);
	}
		
	@Override
	public String createLieferscheinAvisoPost(
			Integer lieferscheinIId, TheClientDto theClientDto) throws RemoteException, NamingException, EJBExceptionLP {
		ILieferscheinAviso aviso = createLieferscheinAviso(lieferscheinIId, theClientDto) ;
		if(aviso == null) return null ;
	
		LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(lieferscheinIId) ;
		ILieferscheinAvisoProducer producer = avisoFactory.getProducer(lieferscheinDto, theClientDto) ;
		producer.postAviso(aviso) ;

		// Besser Zeitstempel ist gesetzt, und dafuer ev. nicht archiviert
		updateAvisoTimestamp(lieferscheinIId, theClientDto) ;

		String xmlContent = lieferscheinAvisoToStringImpl(aviso, lieferscheinDto, theClientDto) ;
		archiveAvisoDocument(lieferscheinDto, xmlContent, theClientDto);
		
		return xmlContent ;
	}

	@Override
	public boolean hatLieferscheinVersandweg(LieferscheinDto lieferscheinDto, TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(lieferscheinDto, "lieferscheinDto") ;
		return getKundeFac().hatKundeVersandweg(lieferscheinDto.getKundeIIdLieferadresse(), theClientDto) ;
	}

	@Override
	public boolean hatLieferscheinVersandweg(Integer lieferscheinIId, TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(lieferscheinIId, "lieferscheinIId") ;
		LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKeyOhneExc(lieferscheinIId) ;
		return getKundeFac().hatKundeVersandweg(lieferscheinDto.getKundeIIdLieferadresse(), theClientDto) ;
	}

	@Override
	public void aktiviereBelegControlled(Integer iid, Timestamp t,
			TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		new BelegAktivierungController(this).aktiviereBelegControlled(iid, t, theClientDto);
	}
	
	@Override
	public Timestamp berechneBelegControlled(Integer iid,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return new BelegAktivierungController(this).berechneBelegControlled(iid, theClientDto);
	}

	@Override
	public boolean hatAenderungenNach(Integer iid, Timestamp t)
			throws EJBExceptionLP, RemoteException {
		LieferscheinDto l = lieferscheinFindByPrimaryKey(iid);
		if(l.getTAendern() != null && l.getTAendern().after(t))
			return true;
		if(l.getTStorniert() != null && l.getTStorniert().after(t))
			return true;
		if(l.getTManuellErledigt() != null && l.getTManuellErledigt().after(t))
			return true;
		return false;
	}

}
