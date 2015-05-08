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
package com.lp.server.auftrag.ejbfac;

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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.ejb.TransactionTimeout;

import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragFuerUebersicht;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionFuerUebersicht;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragDtoAssembler;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragNachkalkulationDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragauftragdokumentDto;
import com.lp.server.auftrag.service.AuftragdokumentDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionDtoAssembler;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.auftrag.service.IOrderResponse;
import com.lp.server.auftrag.service.IOrderResponseProducer;
import com.lp.server.auftrag.service.OrderResponseCC;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.schema.opentrans.cc.orderresponse.DtCURRENCIES;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLADDRESS;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLARTICLEID;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLARTICLEPRICE;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLBUYERAID;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLBUYERPARTY;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLCCCUSTOMSINFO;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLCONTROLINFO;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLDELIVERYDATE;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLDELIVERYPARTY;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLINVOICEPARTY;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERPARTIES;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERRESPONSE;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERRESPONSEHEADER;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERRESPONSEINFO;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERRESPONSEITEM;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERRESPONSEITEMLIST;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERRESPONSESUMMARY;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLPARTY;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLPARTYID;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLREMARK;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLSHIPMENTPARTIES;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLSUPPLIERPARTY;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.ejb.Versandweg;
import com.lp.server.system.ejbfac.BelegAktivierungFac;
import com.lp.server.system.ejbfac.CleverCureProducer;
import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeAuftrag;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.IVersandwegPartnerDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandwegCCPartnerDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperWebshop;
import com.lp.server.util.Validator;
import com.lp.server.util.ZwsPositionMapper;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.BelegDto;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AuftragFacBean extends Facade implements AuftragFac, IAktivierbar {
	@PersistenceContext
	private EntityManager em;

	@EJB
	private BelegAktivierungFac belegAktivierungFac ;
	
	// Referenzen auf CMPs

	// Auftrag
	// -------------------------------------------------------------------

	/**
	 * Anlegen eines neuen Auftragskopfes in der DB.
	 *
	 * @param oAuftragDtoI
	 *            die Daten des Auftragkopfes
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @return Integer PK des Auftrags
	 */
	public Integer createAuftrag(AuftragDto oAuftragDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragDto(oAuftragDtoI);
		Integer auftragIId = null;
		String auftragCNr = null;

		try {
			// Generieren von PK + Belegnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(oAuftragDtoI.getMandantCNr());

			Integer iGeschaeftsjahr = getParameterFac()
					.getGeschaeftsjahr(oAuftragDtoI.getMandantCNr(),
							oAuftragDtoI.getTBelegdatum());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(
					iGeschaeftsjahr, PKConst.PK_AUFTRAG,
					oAuftragDtoI.getMandantCNr(), theClientDto);

			auftragIId = bnr.getPrimaryKey();
			auftragCNr = f.format(bnr);

			oAuftragDtoI.setIId(auftragIId);
			oAuftragDtoI.setCNr(auftragCNr);
			oAuftragDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			oAuftragDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());

			Timestamp t = getTimestamp();
			// java.sql.Date d = getDate();

			Auftrag auftrag = new Auftrag(
					oAuftragDtoI.getIId(),
					oAuftragDtoI.getCNr(),
					oAuftragDtoI.getMandantCNr(),
					oAuftragDtoI.getAuftragartCNr(),
					oAuftragDtoI.getKundeIIdAuftragsadresse(),
					oAuftragDtoI.getKundeIIdLieferadresse(),
					oAuftragDtoI.getKundeIIdRechnungsadresse(),
					oAuftragDtoI.getCAuftragswaehrung(),
					t, // dLiefertermin
					t, // dFinaltermin
					oAuftragDtoI.getLieferartIId(),
					oAuftragDtoI.getZahlungszielIId(),
					oAuftragDtoI.getSpediteurIId(),
					oAuftragDtoI.getStatusCNr(),
					oAuftragDtoI.getPersonalIIdAnlegen(),
					oAuftragDtoI.getPersonalIIdAendern(),
					oAuftragDtoI.getKostIId(),
					oAuftragDtoI.getPersonalIIdVertreter(),
					LocaleFac.BELEGART_AUFTRAG, // belegart
					oAuftragDtoI
							.getFWechselkursmandantwaehrungzubelegwaehrung(),
					oAuftragDtoI.getLagerIIdAbbuchungslager());
			em.persist(auftrag);
			em.flush();

			oAuftragDtoI.setTAnlegen(auftrag.getTAnlegen());
			oAuftragDtoI.setTAendern(auftrag.getTAendern());

			if (oAuftragDtoI.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {

				ParametermandantDto whVerstecktDto = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_AUFTRAG,
								ParameterFac.PARAMETER_WIEDERHOLENDER_AUFTRAG_VERSTECKT);
				if ((Boolean) whVerstecktDto.getCWertAsObject() == true) {
					oAuftragDtoI.setBVersteckt(Helper.boolean2Short(true));
				} else {
					oAuftragDtoI.setBVersteckt(Helper.boolean2Short(false));
				}
			}

			setAuftragFromAuftragDto(auftrag, oAuftragDtoI);

			// PJ14938
			ParametermandantDto parametermandantautoDebitDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_AUTOMATISCHE_DEBITORENNUMMER);
			if ((Boolean) parametermandantautoDebitDto.getCWertAsObject() == true) {
				if (oAuftragDtoI.getKundeIIdRechnungsadresse() != null) {

					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							oAuftragDtoI.getKundeIIdRechnungsadresse(),
							theClientDto);
					if (kundeDto.getIidDebitorenkonto() == null) {
						KontoDto ktoDto = getKundeFac()
								.createDebitorenkontoZuKundenAutomatisch(
										oAuftragDtoI
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

		return auftragIId;
	}

	/**
	 * Parameter pruefen.
	 *
	 * @param auftragDto
	 *            AuftragDto
	 * @throws EJBExceptionLP
	 */
	private void checkAuftragDto(AuftragDto auftragDto) throws EJBExceptionLP {
		if (auftragDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("auftragDto == null"));
		}

		myLogger.info("AuftragDto: " + auftragDto);
	}

	private void checkAuftragIId(Integer iIdAuftragI) throws EJBExceptionLP {
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}

		myLogger.info("AuftragIId: " + iIdAuftragI);
	}

	/**
	 * Aktualisieren der Kopfdaten eines Auftrags.
	 *
	 * @param auftragDtoI
	 *            die Daten des Auftrags
	 * @param waehrungOriCNrI
	 *            die urspruengliche Belegwaehrung aenderewaehrung: 0
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	// TODO Grosser Auftrag bei Kunden. Funktion sollte jedoch
	// optimiert werden
	@TransactionTimeout(60000)
	public boolean updateAuftrag(AuftragDto auftragDtoI,
			String waehrungOriCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAuftragDto(auftragDtoI);
		boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = false;
		AuftragDto auftragOldDto = auftragFindByPrimaryKey(auftragDtoI.getIId());
		try {
			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragDtoI.getIId());

			if (auftragOldDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_ABRUF)
					&& aAuftragpositionDto.length > 0) {

				if (auftragOldDto.getAuftragartCNr().equals(
						AuftragServiceFac.AUFTRAGART_ABRUF)
						&& !auftragDtoI.getAuftragartCNr().equals(
								AuftragServiceFac.AUFTRAGART_ABRUF)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN,
							"FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN");
				}

				if (!auftragOldDto.getAuftragIIdRahmenauftrag().equals(
						auftragDtoI.getAuftragIIdRahmenauftrag())) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN,
							"FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN");
				}
			}

			// SK: Fuer Voest eingebaut am 27.1.2009
			if (auftragOldDto.getDFinaltermin() != null
					&& auftragDtoI.getDFinaltermin() != null) {
				if (!auftragOldDto.getDFinaltermin().equals(
						auftragDtoI.getDFinaltermin())) {
					// Finaltermin hat sich geaendert... Negative Positionen
					// bekommen neuen Positionstermin
					for (int i = 0; i < aAuftragpositionDto.length; i++) {
						if (aAuftragpositionDto[i].getNMenge() != null) {
							if (aAuftragpositionDto[i].getNMenge().compareTo(
									new BigDecimal(0)) < 0
									&& aAuftragpositionDto[i]
											.getTUebersteuerbarerLiefertermin()
											.equals(auftragOldDto
													.getDFinaltermin())) {
								aAuftragpositionDto[i]
										.setTUebersteuerbarerLiefertermin(auftragDtoI
												.getDFinaltermin());
								getAuftragpositionFac()
										.updateAuftragpositionOhneWeitereAktion(
												aAuftragpositionDto[i],
												theClientDto);
								// Artikelreservierung Liefertermin aendern
								if (aAuftragpositionDto[i].isIdent()) {
									ArtikelreservierungDto artikelreservierungDto = getReservierungFac()
											.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
													LocaleFac.BELEGART_AUFTRAG,
													aAuftragpositionDto[i]
															.getIId());
									if (artikelreservierungDto != null) {
										artikelreservierungDto
												.setTLiefertermin(aAuftragpositionDto[i]
														.getTUebersteuerbarerLiefertermin());
										getReservierungFac()
												.updateArtikelreservierung(
														artikelreservierungDto);
									}
								}
							}
						}
					}
				}
			}
			// aenderewaehrung: 1 wenn die Waehrung geaendert wurde, muessen die
			// Belegwerte neu berechnet werden
			if (waehrungOriCNrI != null
					&& !waehrungOriCNrI.equals(auftragDtoI
							.getCAuftragswaehrung())) {

				// aenderewaehrung: 2 die Positionswerte neu berechnen und
				// abspeichern
				BigDecimal ffWechselkurs = getLocaleFac().getWechselkurs2(
						waehrungOriCNrI, auftragDtoI.getCAuftragswaehrung(),
						theClientDto);

				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					if (aAuftragpositionDto[i].getNMenge() != null
							&& aAuftragpositionDto[i].getNEinzelpreis() != null) {
						BigDecimal nNettoeinzelpreisInNeuerWaehrung = aAuftragpositionDto[i]
								.getNEinzelpreis().multiply(ffWechselkurs);

						VerkaufspreisDto verkaufspreisDto = getVkPreisfindungFac()
								.berechnePreisfelder(
										nNettoeinzelpreisInNeuerWaehrung,
										aAuftragpositionDto[i].getFRabattsatz(),
										aAuftragpositionDto[i]
												.getFZusatzrabattsatz(),
										aAuftragpositionDto[i].getMwstsatzIId(),
										4, // @todo Konstante PJ 3778
										theClientDto);

						aAuftragpositionDto[i]
								.setNEinzelpreis(verkaufspreisDto.einzelpreis);
						aAuftragpositionDto[i]
								.setNRabattbetrag(verkaufspreisDto.rabattsumme);
						aAuftragpositionDto[i]
								.setNNettoeinzelpreis(verkaufspreisDto.nettopreis);
						aAuftragpositionDto[i]
								.setNMwstbetrag(verkaufspreisDto.mwstsumme);
						aAuftragpositionDto[i]
								.setNBruttoeinzelpreis(verkaufspreisDto.bruttopreis);
						// alle Preisfelder incl. der zusaetzlichen Preisfelder
						// befuellen
						getAuftragpositionFac()
								.updateAuftragpositionOhneWeitereAktion(
										aAuftragpositionDto[i], theClientDto);
					}
				}
			}
			Timestamp auftragOldLT = Helper.cutTimestamp(auftragOldDto
					.getDLiefertermin());
			if (!auftragOldDto.getDLiefertermin().equals(
					auftragDtoI.getDLiefertermin())) {
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					Timestamp posLT = Helper
							.cutTimestamp(aAuftragpositionDto[i]
									.getTUebersteuerbarerLiefertermin());
					if (posLT.equals(auftragOldLT)) {
						aAuftragpositionDto[i]
								.setTUebersteuerbarerLiefertermin(new Timestamp(
										auftragDtoI.getDLiefertermin()
												.getTime()));
						getAuftragpositionFac()
								.updateAuftragpositionOhneWeitereAktion(
										aAuftragpositionDto[i], theClientDto);
						// Artikelreservierung Liefertermin aendern
						if (aAuftragpositionDto[i].isIdent()) {
							ArtikelreservierungDto artikelreservierungDto = getReservierungFac()
									.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
											LocaleFac.BELEGART_AUFTRAG,
											aAuftragpositionDto[i].getIId());
							if (artikelreservierungDto != null) {
								artikelreservierungDto
										.setTLiefertermin(new Timestamp(
												auftragDtoI.getDLiefertermin()
														.getTime()));
								getReservierungFac().updateArtikelreservierung(
										artikelreservierungDto);
							}
						}
					}
				}
			}
			// Auftrag Kunde wurde geaendert ?
			if (!auftragOldDto.getKundeIIdAuftragsadresse().equals(
					auftragDtoI.getKundeIIdAuftragsadresse())) {
				// mwstsatz
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDtoI.getKundeIIdAuftragsadresse(), theClientDto);
				KundeDto kundeDtoVorher = getKundeFac().kundeFindByPrimaryKey(
						auftragOldDto.getKundeIIdAuftragsadresse(),
						theClientDto);
				ParametermandantDto parameterPositionskontierung = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_KUNDEN,
								ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);

				boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung
						.getCWertAsObject();

				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					if (aAuftragpositionDto[i].isIdent()
							|| aAuftragpositionDto[i].isHandeingabe()) {

						MwstsatzDto mwstsatzDto = getMandantFac()
								.mwstsatzFindByMwstsatzbezIIdAktuellster(
										kundeDto.getMwstsatzbezIId(),
										theClientDto);
						if (bDefaultMwstsatzAusArtikel
								&& aAuftragpositionDto[i].isIdent()) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											aAuftragpositionDto[i]
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
								&& aAuftragpositionDto[i].isHandeingabe()) {

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
						if (!aAuftragpositionDto[i].getMwstsatzIId().equals(
								mwstsatzDto.getIId())) {
							aAuftragpositionDto[i].setMwstsatzIId(mwstsatzDto
									.getIId());

							BigDecimal mwstBetrag = aAuftragpositionDto[i]
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
									.multiply(
											new BigDecimal(mwstsatzDto
													.getFMwstsatz()
													.doubleValue())
													.movePointLeft(2));
							aAuftragpositionDto[i].setNMwstbetrag(mwstBetrag);
							aAuftragpositionDto[i]
									.setNBruttoeinzelpreis(mwstBetrag.add(aAuftragpositionDto[i]
											.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
							getAuftragpositionFac()
									.updateAuftragpositionOhneWeitereAktion(
											aAuftragpositionDto[i],
											theClientDto);
						}
					}
				}
				// Kopftext und Fusstext zuruecksetzen
				auftragDtoI.setCFusstextUebersteuert(null);
				auftragDtoI.setCKopftextUebersteuert(null);
			}
			auftragDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			auftragDtoI.setTAendern(getTimestamp());

			Auftrag auftrag = em.find(Auftrag.class, auftragDtoI.getIId());

			if (auftrag == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			if (auftrag.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_ABRUF)) {
				if (!auftragDtoI.getAuftragartCNr().equals(
						AuftragServiceFac.AUFTRAGART_ABRUF))
					auftragDtoI.setAuftragIIdRahmenauftrag(null);

			}

			if (!auftragDtoI.getKundeIIdAuftragsadresse().equals(
					auftrag.getKundeIIdAuftragsadresse())) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDtoI.getKundeIIdAuftragsadresse(), theClientDto);

				Integer iGarantie = new Integer(0);
				if (kundeDto.getIGarantieinmonaten() != null) {
					iGarantie = kundeDto.getIGarantieinmonaten();
				}
				auftragDtoI.setIGarantie(iGarantie);
			}

			setAuftragFromAuftragDto(auftrag, auftragDtoI);
			// aenderewaehrung: 3 der Status des Auftrags wechselt auf Angelegt
			pruefeUndSetzeAuftragstatusBeiAenderung(auftragDtoI.getIId(),
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben;
	}

	/**
	 * Die Begr&uuml;ndung des Auftrags auf den neuen Wert setzen
	 *
	 * @param auftragIId
	 *            PK des Auftrags
	 * @param begruendungIId
	 *            die IId der neuen Auftragbegruendung
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */

	public void updateAuftragBegruendung(Integer auftragIId,
			Integer begruendungIId, TheClientDto theClientDto) {

		Auftrag auftrag = em.find(Auftrag.class, auftragIId);
		auftrag.setAuftragbegruendungIId(begruendungIId);
		auftrag.setTBegruendung(new Timestamp(System.currentTimeMillis()));
		auftrag.setPersonalIIdBegruendung(theClientDto.getIDPersonal());
		em.merge(auftrag);
		em.flush();
	}

	public void updateAuftragVersteckt(Integer auftragIId,
			TheClientDto theClientDto) {

		Auftrag auftrag = em.find(Auftrag.class, auftragIId);
		auftrag.setBVersteckt(Helper.boolean2Short(!Helper
				.short2boolean(auftrag.getBVersteckt())));
		auftrag.setTAendern(getTimestamp());
		auftrag.setPersonalIIdAendern(theClientDto.getIDPersonal());
		em.merge(auftrag);
		em.flush();
	}

	public void updateAuftragKonditionen(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "updateAuftragKonditionen";
		myLogger.entry();
		try {
			AuftragpositionDto[] aPosition = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);

			// fuer jede Position die Zu- und Abschlaege neu beruecksichtigen
			for (int i = 0; i < aPosition.length; i++) {
				getAuftragpositionFac().befuelleZusaetzlichePreisfelder(
						aPosition[i].getIId());
			}

			Auftrag oAuftrag = em.find(Auftrag.class, iIdAuftragI);
			if (oAuftrag == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			oAuftrag.setTAendern(getTimestamp());
			oAuftrag.setPersonalIIdAendern(theClientDto.getIDPersonal());
			if (!oAuftrag.getAuftragstatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
				oAuftrag.setNGesamtauftragswertinauftragswaehrung(berechneNettowertGesamt(
						iIdAuftragI, theClientDto));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Einen bestehenden Auftrag als storniert kennzeichnen. <br>
	 * Artikelreservierungen werden zurueckgenommen.
	 *
	 * @param iiAuftragI
	 *            pk des Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void storniereAuftrag(Integer iiAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();

		try {
			AuftragDto auftragDto = this.auftragFindByPrimaryKey(iiAuftragI);
			auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT);
			auftragDto.setPersonalIIdStorniert(theClientDto.getIDPersonal());
			auftragDto.setTStorniert(new Timestamp(System.currentTimeMillis()));

			// nicht das allgemeine update, das aendert den status wieder
			Auftrag auftrag = em.find(Auftrag.class, auftragDto.getIId());
			if (auftrag == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setAuftragFromAuftragDto(auftrag, auftragDto);

			// eventuell vorhandene auftragreseriverungen zuruecknehmen (aber
			// nicht bei Rahmenauftraegen)

			AuftragpositionDto[] posDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iiAuftragI);
			for (int i = 0; i < posDtos.length; i++) {
				if (!auftrag.getAuftragartCNr().equals(
						AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					if (posDtos[i].isIdent()) {
						if (posDtos[i].getNMenge().compareTo(new BigDecimal(0)) != 0) {
							ArtikelreservierungDto artikelreservierungDto = getReservierungFac()
									.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
											LocaleFac.BELEGART_AUFTRAG,
											posDtos[i].getIId());
							if (artikelreservierungDto != null) {
								getReservierungFac().removeArtikelreservierung(
										artikelreservierungDto.getIId());
							}
							getAuftragpositionFac()
									.loescheAuftragseriennrnEinesAuftragposition(
											posDtos[i].getIId(), theClientDto);
						}
					}
					// fuer Handartikel gibt es keine Reservierungen!
				}
				posDtos[i]
						.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_STORNIERT);
				getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(
						posDtos[i], theClientDto);
				// Wenn Abrufposition muss auch Rahmen upgedatet werden
				if (posDtos[i].getAuftragpositionIIdRahmenposition() != null) {
					AuftragpositionDto rahmenPosDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									posDtos[i]
											.getAuftragpositionIIdRahmenposition());
					getAuftragpositionFac().updateAuftragposition(rahmenPosDto,
							theClientDto);
				}

			}
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void terminVerschieben(Integer auftragIId, int iTage,
			TheClientDto theClientDto) {

		Auftrag auftrag = em.find(Auftrag.class, auftragIId);
		auftrag.setTAendern(getTimestamp());
		auftrag.setPersonalIIdAendern(theClientDto.getIDPersonal());

		if (!auftrag.getAuftragstatusCNr().equals(
				AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
				&& !auftrag.getAuftragstatusCNr().equals(
						AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(auftrag.getAuftragstatusCNr()));
		}

		auftrag.setTLiefertermin(new Timestamp(Helper.addiereTageZuDatum(
				auftrag.getTLiefertermin(), iTage).getTime()));
		em.merge(auftrag);
		em.flush();

		try {
			AuftragpositionDto[] posDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragIId);

			for (int i = 0; i < posDtos.length; i++) {
				AuftragpositionDto auftPos = posDtos[i];

				if (auftPos.getAuftragpositionstatusCNr().equals(
						LocaleFac.STATUS_OFFEN)
						|| auftPos.getAuftragpositionstatusCNr().equals(
								LocaleFac.STATUS_TEILERLEDIGT)) {
					auftPos.setTUebersteuerbarerLiefertermin(new Timestamp(
							Helper.addiereTageZuDatum(
									posDtos[i]
											.getTUebersteuerbarerLiefertermin(),
									iTage).getTime()));

					getAuftragpositionFac().updateAuftragposition(auftPos,
							theClientDto);
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		LosDto[] losDtos = getFertigungFac().losFindByAuftragIId(auftragIId);
		for (int i = 0; i < losDtos.length; i++) {

			if (losDtos[i].getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)
					|| losDtos[i].getStatusCNr().equals(
							LocaleFac.STATUS_AUSGEGEBEN)
					|| losDtos[i].getStatusCNr().equals(
							LocaleFac.STATUS_IN_PRODUKTION)
					|| losDtos[i].getStatusCNr().equals(
							LocaleFac.STATUS_TEILERLEDIGT)) {

				getFertigungFac().terminVerschieben(
						losDtos[i].getIId(),
						new Timestamp(Helper.addiereTageZuDatum(
								losDtos[i].getTProduktionsbeginn(), iTage)
								.getTime()),
						new Timestamp(Helper.addiereTageZuDatum(
								losDtos[i].getTProduktionsende(), iTage)
								.getTime()), theClientDto);

			}

		}

	}

	/**
	 * Einen Auftrag manuell auf 'Erledigt' setzen.
	 *
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void manuellErledigen(Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		myLogger.logData(iIdAuftragI);
		try {
			Auftrag auftrag = em.find(Auftrag.class, iIdAuftragI);
			if (auftrag == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (auftrag.getAuftragstatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
					|| auftrag.getAuftragstatusCNr().equals(
							AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {

				// PJ18288
				// Logging
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
								theClientDto);
				String sMessage = personalDto.formatFixName1Name2()
						+ " hat den Status von Auftrag " + auftrag.getCNr()
						+ " von Status " + auftrag.getAuftragstatusCNr()
						+ " nach Status "
						+ AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT
						+ " geaendert ";

				myLogger.logKritisch(sMessage);

				auftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
				auftrag.setPersonalIIdManuellerledigt(theClientDto
						.getIDPersonal());
				auftrag.setTManuellerledigt(Helper.cutTimestamp(getTimestamp()));
				auftrag.setPersonalIIdErledigt(theClientDto.getIDPersonal());
				auftrag.setTErledigt(Helper.cutTimestamp(getTimestamp()));

				// eventuell vorhandene auftragreseriverungen zuruecknehmen
				// Bei Rahmenauftraegen gibt es keine Reservierungen.
				if (!auftrag.getAuftragartCNr().equals(
						AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					AuftragpositionDto[] posDtos = getAuftragpositionFac()
							.auftragpositionFindByAuftrag(iIdAuftragI);
					for (int i = 0; i < posDtos.length; i++) {
						if (posDtos[i].isIdent()) {
							if (posDtos[i].getNMenge().compareTo(
									new BigDecimal(0)) != 0) {
								if (!posDtos[i].getAuftragpositionstatusCNr()
										.equals(LocaleFac.STATUS_ERLEDIGT)) {
									ArtikelreservierungDto artikelreservierungDto = getReservierungFac()
											.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
													LocaleFac.BELEGART_AUFTRAG,
													posDtos[i].getIId());
									if (artikelreservierungDto != null) {
										getReservierungFac()
												.removeArtikelreservierung(
														artikelreservierungDto
																.getIId());
									}
								}
							}
						}
						// fuer Handartikel gibt es keine Reservierungen!
					}
				}
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
						new Exception(
								"Auftrag kann nicht manuell erledigt werden, Status : "
										+ auftrag.getAuftragstatusCNr()));
			}
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}

		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	public void toggleVerrechenbar(Integer auftragIId, TheClientDto theClientDto) {
		Auftrag auftrag = em.find(Auftrag.class, auftragIId);
		if (auftrag.getTVerrechenbar() == null) {
			auftrag.setTVerrechenbar(new Timestamp(System.currentTimeMillis()));
			auftrag.setPersonalIIdVerrechenbar(theClientDto.getIDPersonal());

		} else {
			auftrag.setPersonalIIdVerrechenbar(null);
			auftrag.setTVerrechenbar(null);

		}
		em.merge(auftrag);
		em.flush();
	}

	/**
	 * Den Status eines Auftrags von 'Erledigt' auf 'Erfasst' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 *
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void erledigungAufheben(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		try {
			Auftrag auftrag = em.find(Auftrag.class, iIdAuftragI);
			if (auftrag == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (auftrag.getAuftragstatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				auftrag.setPersonalIIdManuellerledigt(null);
				auftrag.setTManuellerledigt(null);
				auftrag.setPersonalIIdErledigt(null);
				auftrag.setTErledigt(null);
				if (auftrag.getPersonalIIdManuellerledigt() != null
						&& auftrag.getTManuellerledigt() != null) {
					auftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);

					AuftragpositionDto[] posDtos = null;
					posDtos = getAuftragpositionFac()
							.auftragpositionFindByAuftrag(iIdAuftragI);
					boolean bOffen = false;
					for (int i = 0; i < posDtos.length; i++) {
						if (posDtos[i].isIdent()) {
							// Keine Reservierungen bei Rahmenauftraegen.
							if (!auftrag.getAuftragartCNr().equals(
									AuftragServiceFac.AUFTRAGART_RAHMEN)) {

								if (posDtos[i].getNOffeneMenge().signum() != 0) {
									ArtikelreservierungDto reservierungDto = new ArtikelreservierungDto();
									reservierungDto
											.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
									reservierungDto
											.setIBelegartpositionid(posDtos[i]
													.getIId());
									reservierungDto.setArtikelIId(posDtos[i]
											.getArtikelIId());
									reservierungDto
											.setTLiefertermin(posDtos[i]
													.getTUebersteuerbarerLiefertermin());
									reservierungDto.setNMenge(posDtos[i]
											.getNOffeneMenge());
									getReservierungFac()
											.createArtikelreservierung(
													reservierungDto);
								}
							}
							if (posDtos[i].getNMenge().equals(
									posDtos[i].getNOffeneMenge())) {
								bOffen = true;

								if (posDtos[i].getNOffeneMenge().doubleValue() > 0) {
									Auftragposition oPos = em.find(
											Auftragposition.class,
											posDtos[i].getIId());
									if (bOffen) {
										oPos.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
									} else {
										oPos.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
									}
								}

							}
						}
						auftrag.setAuftragstatusCNr(bOffen ? AuftragServiceFac.AUFTRAGSTATUS_OFFEN
								: AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);

						// PJ18288
						// Logging
						PersonalDto personalDto = getPersonalFac()
								.personalFindByPrimaryKey(
										theClientDto.getIDPersonal(),
										theClientDto);
						String sMessage = personalDto.formatFixName1Name2()
								+ " hat den Status von Auftrag "
								+ auftrag.getCNr()
								+ " von Status Erledigt nach Status "
								+ auftrag.getAuftragstatusCNr() + " geaendert ";

						myLogger.logKritisch(sMessage);

					}
				} else {
					auftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
					myLogger.logKritisch("Status Erledigt wurde aufgehoben, obwohl der Auftrag nicht manuell erledigt wurde, AuftragIId: "
							+ iIdAuftragI);
				}
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
						new Exception(
								"Die Erledigung des Auftrags kann nicht aufgehoben werden, Status: "
										+ auftrag.getAuftragstatusCNr()));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Einen Auftrag ueber seinen PK aus der DB holen.
	 *
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return AuftragDto
	 */
	public AuftragDto auftragFindByPrimaryKey(Integer iId) {
		AuftragDto auftragDto = null;

		// try {
		Auftrag auftrag = em.find(Auftrag.class, iId);
		if (auftrag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		auftragDto = assembleAuftragDto(auftrag);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return auftragDto;
	}

	/**
	 * Einen Auftrag ueber seinen PK holen. <br>
	 * Diese Methode wirft keine Exceptions.
	 *
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return AuftragDto der Auftrag
	 */
	public AuftragDto auftragFindByPrimaryKeyOhneExc(Integer iIdAuftragI) {
		AuftragDto auftragDto = null;

		try {
			auftragDto = auftragFindByPrimaryKey(iIdAuftragI);
		} catch (Throwable t) {
			myLogger.warn("iIdAuftragI=" + iIdAuftragI, t);
		}

		return auftragDto;
	}

	public Integer erzeugeAuftragUeberSchnellanlage(AuftragDto auftragDto,
			ArtikelDto artikelDto, PaneldatenDto[] paneldatenDtos,
			TheClientDto theClientDto) {
		Integer auftragIId = null;
		try {
			// Zuerst Auftrag anlegen
			auftragIId = getAuftragFac().createAuftrag(auftragDto, theClientDto);

			auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);

			// Artikelnummer zusammenbauen

			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_TRENNZEICHEN_ARTIKELGRUPPE_AUFTRAGSNUMMER);

			String trennzeichen = (String) parameter.getCWertAsObject();

			ArtgruDto agDto = getArtikelFac().artgruFindByPrimaryKey(
					artikelDto.getArtgruIId(), theClientDto);

			String artikelnummer = agDto.getCNr() + trennzeichen
					+ auftragDto.getCNr();

			artikelDto.setCNr(artikelnummer);
			// Dann Artikel anlegen
			Integer artikelIId = getArtikelFac().createArtikel(artikelDto,
					theClientDto);

			// Dann Artikeleigenschaften anlegen
			for (int i = 0; i < paneldatenDtos.length; i++) {
				paneldatenDtos[i].setCKey(artikelIId + "");
			}
			getPanelFac().createPaneldaten(paneldatenDtos);

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdRechnungsadresse(), theClientDto);

			// Dann Auftragsposition anlegen
			AuftragpositionDto auftragpositionDto = new AuftragpositionDto();
			auftragpositionDto.setBelegIId(auftragIId);
			auftragpositionDto.setArtikelIId(artikelIId);
			auftragpositionDto
					.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
			auftragpositionDto
					.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
			auftragpositionDto.setBArtikelbezeichnunguebersteuert(Helper
					.boolean2Short(false));
			auftragpositionDto.setBNettopreisuebersteuert(Helper
					.boolean2Short(false));
			auftragpositionDto.setNMenge(new BigDecimal(1));
			auftragpositionDto.setNOffeneMenge(new BigDecimal(1));
			auftragpositionDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
			auftragpositionDto.setFRabattsatz(new Double(0));

			auftragpositionDto.setBRabattsatzuebersteuert(Helper
					.boolean2Short(false));

			MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							kdDto.getMwstsatzbezIId(), theClientDto);

			auftragpositionDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
			auftragpositionDto.setBMwstsatzuebersteuert(Helper
					.boolean2Short(false));
			auftragpositionDto.setNEinzelpreis(new BigDecimal(0));
			auftragpositionDto.setNRabattbetrag(new BigDecimal(0));
			auftragpositionDto.setNNettoeinzelpreis(new BigDecimal(0));
			auftragpositionDto
					.setNEinzelpreisplusversteckteraufschlag(new BigDecimal(0));
			auftragpositionDto
					.setNNettoeinzelpreisplusversteckteraufschlag(new BigDecimal(
							0));
			auftragpositionDto
					.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(new BigDecimal(
							0));
			auftragpositionDto.setNMwstbetrag(new BigDecimal(0));
			auftragpositionDto.setNBruttoeinzelpreis(new BigDecimal(30));
			auftragpositionDto.setTUebersteuerbarerLiefertermin(auftragDto
					.getTBelegdatum());
			auftragpositionDto.setBDrucken(Helper.boolean2Short(false));
			auftragpositionDto.setFZusatzrabattsatz(new Double(0));
			auftragpositionDto.setISort(new Integer(1));

			getAuftragpositionFac().createAuftragposition(auftragpositionDto,
					theClientDto);
			aktiviereAuftrag(auftragIId, theClientDto);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return auftragIId;
	}

	public AuftragDto auftragFindByMandantCNrCNr(String cNrMandantI,
			String cNrI, TheClientDto theClientDto) throws EJBExceptionLP {
		AuftragDto auftragDto = auftragFindByMandantCNrCNrOhneExc(cNrMandantI,
				cNrI);
		if (auftragDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"cNrI=" + cNrI + " cNrMandantI=" + cNrMandantI));
		}

		return auftragDto;
	}

	public AuftragDto auftragFindByMandantCNrCNrOhneExc(String cNrMandantI,
			String cNrI) {
		Query query = em.createNamedQuery("AuftragfindByMandantCNrCNr");
		query.setParameter(1, cNrMandantI);
		query.setParameter(2, cNrI);
		Auftrag auftrag;
		try {
			auftrag = (Auftrag) query.getSingleResult();
			return assembleAuftragDto(auftrag);
		} catch (NoResultException ex) {
			return null;
		}
	}

	/***
	 * Update eines bestehenden Auftrags mit den Werten aus dem Dto.* @param
	 * auftrag Auftrag* @param auftragDto AuftragDto* @throws EJBExceptionLP
	 */
	private void setAuftragFromAuftragDto(Auftrag auftrag, AuftragDto auftragDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "setAuftragFromAuftragDto";
		myLogger.entry();
		if (auftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("auftrag == null"));
		}
		if (auftragDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("auftragDto == null"));
		}

		auftrag.setAuftragIIdRahmenauftrag(auftragDto
				.getAuftragIIdRahmenauftrag());
		auftrag.setAngebotIId(auftragDto.getAngebotIId());
		if (auftragDto.getMandantCNr() != null) {
			auftrag.setMandantCNr(auftragDto.getMandantCNr());
		}
		if (auftragDto.getCNr() != null) {
			auftrag.setCNr(auftragDto.getCNr());
		}
		if (auftragDto.getAuftragartCNr() != null) {
			auftrag.setAuftragartCNr(auftragDto.getAuftragartCNr());
		}
		if (auftragDto.getBelegartCNr() != null) {
			auftrag.setBelegartCNr(auftragDto.getBelegartCNr());
		}
		if (auftragDto.getKundeIIdAuftragsadresse() != null) {
			auftrag.setKundeIIdAuftragsadresse(auftragDto
					.getKundeIIdAuftragsadresse());
		}
		// der Ansprechpartner kann null sein
		auftrag.setAnsprechpartnerIIdKunde(auftragDto.getAnsprechparnterIId());

		if (auftragDto.getPersonalIIdVertreter() != null) {
			auftrag.setPersonalIIdVertreter(auftragDto
					.getPersonalIIdVertreter());
		}
		if (auftragDto.getKundeIIdLieferadresse() != null) {
			auftrag.setKundeIIdLieferadresse(auftragDto
					.getKundeIIdLieferadresse());
		}
		if (auftragDto.getKundeIIdRechnungsadresse() != null) {
			auftrag.setKundeIIdRechnungsadresse(auftragDto
					.getKundeIIdRechnungsadresse());
		}
		auftrag.setCBez(auftragDto.getCBezProjektbezeichnung());
		auftrag.setCBestellnummer(auftragDto.getCBestellnummer());
		auftrag.setTBestelldatum(auftragDto.getDBestelldatum());
		if (auftragDto.getCAuftragswaehrung() != null) {
			auftrag.setWaehrungCNrAuftragswaehrung(auftragDto
					.getCAuftragswaehrung());
		}
		if (auftragDto.getFWechselkursmandantwaehrungzubelegwaehrung() != null) {
			auftrag.setFWechselkursmandantwaehrungzuauftragswaehrung(auftragDto
					.getFWechselkursmandantwaehrungzubelegwaehrung());
		}
		/*
		 * if (auftragDto.getFSonderrabattsatz() != null) {
		 * auftrag.setFSonderrabattsatz(auftragDto.getFSonderrabattsatz()); }
		 */
		if (auftragDto.getDLiefertermin() != null) {
			auftrag.setTLiefertermin(auftragDto.getDLiefertermin());
		}
		if (auftragDto.getBLieferterminUnverbindlich() != null) {
			auftrag.setBLieferterminunverbindlich(auftragDto
					.getBLieferterminUnverbindlich());
		}
		if (auftragDto.getDFinaltermin() != null) {
			auftrag.setTFinaltermin(auftragDto.getDFinaltermin());
		}
		if (auftragDto.getKostIId() != null) {
			auftrag.setKostenstelleIId(auftragDto.getKostIId());
		}
		if (auftragDto.getBTeillieferungMoeglich() != null) {
			auftrag.setBTeillieferungmoeglich(auftragDto
					.getBTeillieferungMoeglich());
		}
		if (auftragDto.getBPoenale() != null) {
			auftrag.setBPoenale(auftragDto.getBPoenale());
		}

		if (auftragDto.getBMitzusammenfassung() != null) {
			auftrag.setBMitzusammenfassung(auftragDto.getBMitzusammenfassung());
		}

		if (auftragDto.getILeihtage() != null) {
			auftrag.setILeihtage(auftragDto.getILeihtage());
		}
		if (auftragDto.getFVersteckterAufschlag() != null) {
			auftrag.setFVersteckteraufschlag(auftragDto
					.getFVersteckterAufschlag());
		}
		if (auftragDto.getFAllgemeinerRabattsatz() != null) {
			auftrag.setFAllgemeinerrabattsatz(auftragDto
					.getFAllgemeinerRabattsatz());
		}
		if (auftragDto.getFProjektierungsrabattsatz() != null) {
			auftrag.setFProjektierungsrabattsatz(auftragDto
					.getFProjektierungsrabattsatz());
		}
		if (auftragDto.getLieferartIId() != null) {
			auftrag.setLieferartIId(auftragDto.getLieferartIId());
		}
		if (auftragDto.getZahlungszielIId() != null) {
			auftrag.setZahlungszielIId(auftragDto.getZahlungszielIId());
		}
		if (auftragDto.getSpediteurIId() != null) {
			auftrag.setSpediteurIId(auftragDto.getSpediteurIId());
		}
		if (auftragDto.getIGarantie() != null) {
			auftrag.setIGarantie(auftragDto.getIGarantie());
		}
		auftrag.setNGesamtauftragswertinauftragswaehrung(auftragDto
				.getNGesamtauftragswertInAuftragswaehrung());

		auftrag.setNRohdeckunginmandantenwaehrung(auftragDto
				.getNRohdeckungInMandantenwaehrung());
		auftrag.setNRohdeckungaltinmandantenwaehrung(auftragDto
				.getNRohdeckungaltInMandantenwaehrung());
		auftrag.setNMaterialwertinmandantenwaehrung(auftragDto
				.getNMaterialwertInMandantenwaehrung());

		auftrag.setAuftragtextIIdKopftext(auftragDto.getAuftragIIdKopftext());
		auftrag.setXKopftextuebersteuert(auftragDto.getCKopftextUebersteuert());
		auftrag.setAuftragtextIIdFusstext(auftragDto.getAuftragIIdFusstext());
		auftrag.setXFusstextuebersteuert(auftragDto.getCFusstextUebersteuert());

		auftrag.setAnsprechpartnerIIdLieferadresse(auftragDto
				.getAnsprechpartnerIIdLieferadresse());

		if (auftragDto.getStatusCNr() != null) {
			auftrag.setAuftragstatusCNr(auftragDto.getStatusCNr());
		}
		if (auftragDto.getTBelegdatum() != null) {
			auftrag.setTBelegdatum(auftragDto.getTBelegdatum());
		}
		if (auftragDto.getTGedruckt() != null) {
			auftrag.setTGedruckt(auftragDto.getTGedruckt());
		}
		if (auftragDto.getPersonalIIdStorniert() != null) {
			auftrag.setPersonalIIdStorniert(auftragDto
					.getPersonalIIdStorniert());
		}
		if (auftragDto.getTStorniert() != null) {
			auftrag.setTStorniert(auftragDto.getTStorniert());
		}

		auftrag.setPersonalIIdAnlegen(auftragDto.getPersonalIIdAnlegen());
		auftrag.setTAnlegen(auftragDto.getTAnlegen());
		auftrag.setPersonalIIdAendern(auftragDto.getPersonalIIdAendern());
		auftrag.setTAendern(auftragDto.getTAendern());
		auftrag.setPersonalIIdManuellerledigt(auftragDto
				.getPersonalIIdManuellerledigt());
		if (auftragDto.getTManuellerledigt() != null) {
			auftrag.setTManuellerledigt(Helper.cutTimestamp(auftragDto
					.getTManuellerledigt()));
		} else {
			auftrag.setTManuellerledigt(null);
		}
		auftrag.setXExternerkommentar(auftragDto.getXExternerkommentar());
		auftrag.setXInternerkommentar(auftragDto.getXInternerkommentar());
		auftrag.setPersonalIIdErledigt(auftragDto.getPersonalIIdErledigt());
		if (auftragDto.getTErledigt() != null) {
			auftrag.setTErledigt(auftragDto.getTErledigt());
		} else {
			auftrag.setTErledigt(null);
		}
		if (auftragDto.getBRoHs() != null) {
			auftrag.setBRoHs(auftragDto.getBRoHs());
		}
		if (auftragDto.getBVersteckt() != null) {
			auftrag.setBVersteckt(auftragDto.getBVersteckt());
		}
		auftrag.setTLauftermin(auftragDto.getTLauftermin());
		auftrag.setAuftragwiederholungsintervallCNr(auftragDto
				.getWiederholungsintervallCNr());
		auftrag.setFErfuellungsgrad(auftragDto.getFErfuellungsgrad());

		auftrag.setAuftragbegruendungIId(auftragDto.getAuftragbegruendungIId());
		auftrag.setLagerIIdAbbuchungslager(auftragDto
				.getLagerIIdAbbuchungslager());
		auftrag.setAnsprechpartnerIIdRechnungsadresse(auftragDto
				.getAnsprechpartnerIIdRechnungsadresse());
		auftrag.setCLieferartort(auftragDto.getCLieferartort());
		auftrag.setProjektIId(auftragDto.getProjektIId());
		auftrag.setNKorrekturbetrag(auftragDto.getNKorrekturbetrag());
		auftrag.setTResponse(auftragDto.getTResponse());
		auftrag.setPersonalIIdResponse(auftragDto.getPersonalIIdResponse());
		em.merge(auftrag);
		em.flush();
	}

	private AuftragDto assembleAuftragDto(Auftrag auftrag) {
		return AuftragDtoAssembler.createDto(auftrag);
	}

	private AuftragDto[] assembleAuftragDtos(Collection<?> auftrags) {
		List<AuftragDto> list = new ArrayList<AuftragDto>();
		if (auftrags != null) {
			Iterator<?> iterator = auftrags.iterator();
			while (iterator.hasNext()) {
				Auftrag auftrag = (Auftrag) iterator.next();
				list.add(assembleAuftragDto(auftrag));
			}
		}
		AuftragDto[] returnArray = new AuftragDto[list.size()];
		return (AuftragDto[]) list.toArray(returnArray);
	}

	/**
	 * Auftraege duerfen nur in bestimmten Stati geaendert werden. <br>
	 * Nachdem ein Auftrag geaendert wurde, befindet er sich im Status ANGELEGT.
	 *
	 * @param iIdAuftragI
	 *            pk des Auftrags
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void pruefeUndSetzeAuftragstatusBeiAenderung(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Auftrag auftrag = null;
		auftrag = em.find(Auftrag.class, iIdAuftragI);
		if (auftrag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		String sStatus = auftrag.getAuftragstatusCNr();
		if (AuftragServiceFac.AUFTRAGART_RAHMEN.equals(auftrag
				.getAuftragartCNr())) {
			if (!sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
					&& !sStatus
							.equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)
					&& !sStatus
							.equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
						new Exception(sStatus));
			}
		} else {
			if (!sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
					&& !sStatus
							.equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
						new Exception(sStatus));
			}
		}
		if (sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN) ||
		// MB 06.06.06 damit auch im status "Angelegt" alle Belegwert-Felder
		// null gesetzt werden
				sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
			auftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
			auftrag.setNGesamtauftragswertinauftragswaehrung(null);
			auftrag.setNMaterialwertinmandantenwaehrung(null);
			// auftrag.setNRohdeckunginmandantenwaehrung(null);
		}
	}

	/**
	 * AuftragpositionDto auf null pruefen.
	 *
	 * @param auftragspositionDto
	 *            AuftragpositionDto
	 * @throws EJBExceptionLP
	 */
	private void checkAuftragpositionDto(AuftragpositionDto auftragspositionDto)
			throws EJBExceptionLP {
		if (auftragspositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("auftragspositionDto == null"));
		}
	}

	/**
	 * Die Anrede fuer einen Kunden bauen.
	 *
	 * @param iIdKundeI
	 *            PK des Kunden
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws Throwable
	 * @return String die Anrede
	 */
	private String baueAnredeKunde(Integer iIdKundeI, TheClientDto theClientDto)
			throws Throwable {
		String sAnredeKundeO = null;
		if (iIdKundeI != null) {
			KundeDto oKunde = getKundeFac().kundeFindByPrimaryKey(iIdKundeI,
					theClientDto);
			sAnredeKundeO = oKunde.getPartnerDto().formatAnrede();
		}
		return sAnredeKundeO;
	}

	/**
	 * Die Anschrift fuer einen Kunden bauen. <br>
	 * Es muss keine Anschrift geben.
	 *
	 * @param iIdKundeI
	 *            PK des Kunden
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws Throwable
	 * @return String die Anschrift
	 */
	private String baueAnschriftKunde(Integer iIdKundeI,
			TheClientDto theClientDto) throws Throwable {
		String sAnschriftKunde = null;
		if (iIdKundeI != null) {
			KundeDto oKunde = getKundeFac().kundeFindByPrimaryKey(iIdKundeI,
					theClientDto);
			if (oKunde.getPartnerDto().getLandplzortDto() != null) {
				sAnschriftKunde = oKunde.getPartnerDto().getLandplzortDto()
						.formatLandPlzOrt();
			}
		}
		return sAnschriftKunde;
	}

	/**
	 * Die Anrede fuer einen Ansprechpartner bauen. <br>
	 * Es muss keinen Ansprechpartner geben.
	 *
	 * @param iIdAnsprechpartnerI
	 *            pk des Ansprechpartners
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws Throwable
	 * @return String die Anrede
	 */
	private String baueAnredeAnsprechpartner(Integer iIdAnsprechpartnerI,
			TheClientDto theClientDto) throws Throwable {
		String sAnredeAnsprechpartner = null;

		if (iIdAnsprechpartnerI != null) {
			AnsprechpartnerDto oAnsprechpartner = getAnsprechpartnerFac()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI,
							theClientDto);
			sAnredeAnsprechpartner = oAnsprechpartner.getPartnerDto()
					.formatAnrede();
		}

		return sAnredeAnsprechpartner;
	}

	/**
	 * In der Auftrag Nachkalkulation wird eine extra Zeile geschrieben, wenn es
	 * auf einem Lieferschein mit Bezug zum relevanten Auftrag eine
	 * mengenbehaftete Position gibt, die im Auftrag nicht geplant war.
	 *
	 * @param auftragDto
	 *            AuftragDto
	 * @param oLieferscheinpositionDto
	 *            die Lieferscheinposition
	 * @param sAuftragswaehrungI
	 *            die Waehrung des aktuellen Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return AuftragNachkalkulationDto der Inhalt der extra Zeile
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private AuftragNachkalkulationDto buildZeileZusaetzlicheLieferscheinposition(
			AuftragDto auftragDto,
			LieferscheinpositionDto oLieferscheinpositionDto,
			String sAuftragswaehrungI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AuftragNachkalkulationDto oNachkalkulationDto = new AuftragNachkalkulationDto(
				auftragDto);

		/*
		 * try { ArtikelDto oArtikelDto =
		 * getArtikelFac().artikelFindByPrimaryKey(
		 * oLieferscheinpositionDto.getArtikelIId(), sUserI);
		 *
		 * StringBuffer sbArtikelInfo = new StringBuffer();
		 *
		 * // die Artikelbezeichnung zum Andrucken if
		 * (oLieferscheinpositionDto.getLieferscheinpositionartCNr
		 * ().equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
		 * sbArtikelInfo.append(oArtikelDto.getCNr());
		 *
		 * if (oLieferscheinpositionDto.getCBezeichnung() != null) {
		 * sbArtikelInfo.append(oLieferscheinpositionDto.getCBezeichnung()); }
		 * else { if (oArtikelDto.getArtikelsprDto() != null &&
		 * oArtikelDto.getArtikelsprDto().getCBez() != null) {
		 * sbArtikelInfo.append
		 * ("\n").append(oArtikelDto.getArtikelsprDto().getCBez()); } }
		 *
		 * if (oArtikelDto.getArtikelsprDto() != null &&
		 * oArtikelDto.getArtikelsprDto().getCZbez() != null) {
		 * sbArtikelInfo.append
		 * ("\n").append(oArtikelDto.getArtikelsprDto().getCZbez()); } } else if
		 * (oLieferscheinpositionDto.getLieferscheinpositionartCNr().equals(
		 * LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
		 * sbArtikelInfo.append(oArtikelDto.getArtikelsprDto().getCBez());
		 *
		 * if (oArtikelDto.getArtikelsprDto().getCZbez() != null) {
		 * sbArtikelInfo
		 * .append("\n").append(oArtikelDto.getArtikelsprDto().getCZbez()); } }
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 * oNachkalkulationDto.setSArtikelcnrbezeichnung(sbArtikelInfo.toString()
		 * );
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 * oNachkalkulationDto.setBdMengeist(oLieferscheinpositionDto.getNMenge()
		 * );
		 *
		 * if
		 * (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT
		 * )) {
		 * oNachkalkulationDto.setBdArbeitpreisist(oLieferscheinpositionDto.
		 * getNNettogesamtpreisPlusVersteckterAufschlagMinusRabatt()); } else {
		 * // sonst Material
		 * oNachkalkulationDto.setBdMaterialpreisist(oLieferscheinpositionDto
		 * .getNNettogesamtpreisPlusVersteckterAufschlagMinusRabatt()); }
		 *
		 * BigDecimal bdEinzelsummeist =
		 * oLieferscheinpositionDto.getNMenge().multiply(
		 * oLieferscheinpositionDto
		 * .getNNettogesamtpreisPlusVersteckterAufschlagMinusRabatt());
		 *
		 * oNachkalkulationDto.setBdSummeist(bdEinzelsummeist);
		 *
		 * // Gestehungspreise der Lieferscheinposition, es gilt das Lager des
		 * Lieferscheins LieferscheinDto oLieferscheinDto =
		 * getLieferscheinFac().
		 * lieferscheinFindByPrimaryKey(oLieferscheinpositionDto
		 * .getLieferscheinIId());
		 *
		 * LagerDto oLagerDto =
		 * getLagerFac().lagerFindByPrimaryKey(oLieferscheinDto.getLagerIId());
		 *
		 * // Grundlage ist der Gestehungspreis des Artikels am Lager des
		 * Lieferscheins BigDecimal bdGestehungspreisIst =
		 * getLagerFac().getGestehungspreis( oArtikelDto.getIId(),
		 * oLagerDto.getIId());
		 *
		 * // umrechnen in Auftragwaehrung bdGestehungspreisIst =
		 * getLocaleFac().rechneUmInAndereWaehrung( bdGestehungspreisIst,
		 * Currency
		 * .getInstance(getTheClient(sUserI).getLocMandant()).getCurrencyCode(),
		 * sAuftragswaehrungI);
		 *
		 * if
		 * (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT
		 * )) {
		 * oNachkalkulationDto.setBdGestpreisarbeitist(bdGestehungspreisIst); }
		 * else { // sonst Material
		 * oNachkalkulationDto.setBdGestpreismaterialist(bdGestehungspreisIst);
		 * }
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 * oNachkalkulationDto.setBdGestpreissummeist(bdGestehungspreisIst.multiply
		 * (oLieferscheinpositionDto.getNMenge())); } catch(Throwable t) { throw
		 * new EJBExceptionLP(EJBExceptionLP.FEHLER, t); }
		 */

		return oNachkalkulationDto;
	}

	public ArrayList<KundeDto> getRechnungsadressenEinerAuftragsadresseSortiertNachHaeufigkeit(
			Integer kundeIIdAuftragsadresse, TheClientDto theClientDto) {

		HashMap<Integer, Integer> anzahlRechnungsadressen = new HashMap<Integer, Integer>();

		Query query = em
				.createNamedQuery("AuftragfindByKundeIIdAuftragsadresseMandantCNr");
		query.setParameter(1, kundeIIdAuftragsadresse);
		query.setParameter(2, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Auftrag auftrag = (Auftrag) it.next();

			if (anzahlRechnungsadressen.containsKey(auftrag
					.getKundeIIdRechnungsadresse())) {
				Integer iAnzahl = anzahlRechnungsadressen.get(auftrag
						.getKundeIIdRechnungsadresse());
				iAnzahl++;
				anzahlRechnungsadressen.put(
						auftrag.getKundeIIdRechnungsadresse(), iAnzahl);
			} else {
				anzahlRechnungsadressen.put(
						auftrag.getKundeIIdRechnungsadresse(), new Integer(1));
			}

		}

		ArrayList<KundeDto> kunden = new ArrayList<KundeDto>();

		while (anzahlRechnungsadressen.size() > 0) {

			Iterator itAnzahl = anzahlRechnungsadressen.keySet().iterator();

			Integer iGroessteAnzahl = null;
			Integer keyGroessteAnzahl = null;

			while (itAnzahl.hasNext()) {

				Integer key = (Integer) itAnzahl.next();

				Integer value = anzahlRechnungsadressen.get(key);

				if (iGroessteAnzahl == null) {
					iGroessteAnzahl = value;
				}

				if (keyGroessteAnzahl == null) {
					keyGroessteAnzahl = key;
				}

				if (value >= iGroessteAnzahl) {
					iGroessteAnzahl = value;
					keyGroessteAnzahl = key;
				}
			}
			anzahlRechnungsadressen.remove(keyGroessteAnzahl);
			kunden.add(getKundeFac().kundeFindByPrimaryKey(keyGroessteAnzahl,
					theClientDto));

		}

		return kunden;
	}

	public ArrayList<KundeDto> getLieferadressenEinerRechnungsadresseSortiertNachHaeufigkeit(
			Integer kundeIIdRechnungsadresse, TheClientDto theClientDto) {

		HashMap<Integer, Integer> anzahlLieferadressen = new HashMap<Integer, Integer>();

		Query query = em
				.createNamedQuery("AuftragfindByKundeIIdRechnungsadresseMandantCNr");
		query.setParameter(1, kundeIIdRechnungsadresse);
		query.setParameter(2, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Auftrag auftrag = (Auftrag) it.next();

			if (anzahlLieferadressen.containsKey(auftrag
					.getKundeIIdLieferadresse())) {
				Integer iAnzahl = anzahlLieferadressen.get(auftrag
						.getKundeIIdLieferadresse());
				iAnzahl++;
				anzahlLieferadressen.put(auftrag.getKundeIIdLieferadresse(),
						iAnzahl);
			} else {
				anzahlLieferadressen.put(auftrag.getKundeIIdLieferadresse(),
						new Integer(1));
			}

		}

		ArrayList<KundeDto> kunden = new ArrayList<KundeDto>();

		while (anzahlLieferadressen.size() > 0) {

			Iterator itAnzahl = anzahlLieferadressen.keySet().iterator();

			Integer iGroessteAnzahl = null;
			Integer keyGroessteAnzahl = null;

			while (itAnzahl.hasNext()) {

				Integer key = (Integer) itAnzahl.next();

				Integer value = anzahlLieferadressen.get(key);

				if (iGroessteAnzahl == null) {
					iGroessteAnzahl = value;
				}

				if (keyGroessteAnzahl == null) {
					keyGroessteAnzahl = key;
				}

				if (value >= iGroessteAnzahl) {
					iGroessteAnzahl = value;
					keyGroessteAnzahl = key;
				}
			}
			anzahlLieferadressen.remove(keyGroessteAnzahl);
			kunden.add(getKundeFac().kundeFindByPrimaryKey(keyGroessteAnzahl,
					theClientDto));

		}

		return kunden;
	}

	private String getArtikelbezeichnung(AuftragpositionDto oPositionDtoI,
			ArtikelDto oArtikelDtoI) throws Throwable {
		StringBuffer sbArtikelInfo = new StringBuffer();

		if (oPositionDtoI.isIdent()) {
			sbArtikelInfo.append(oArtikelDtoI.getCNr());

			if (oPositionDtoI.getCBez() != null) {
				sbArtikelInfo.append(oPositionDtoI.getCBez());
			} else {
				if (oArtikelDtoI.getArtikelsprDto().getCBez() != null) {
					sbArtikelInfo.append("\n").append(
							oArtikelDtoI.getArtikelsprDto().getCBez());
				}
			}

			if (oArtikelDtoI.getArtikelsprDto().getCZbez() != null) {
				sbArtikelInfo.append("\n").append(
						oArtikelDtoI.getArtikelsprDto().getCZbez());
			}
		} else if (oPositionDtoI.isHandeingabe()) {
			sbArtikelInfo.append(oArtikelDtoI.getArtikelsprDto().getCBez());

			if (oArtikelDtoI.getArtikelsprDto().getCZbez() != null) {
				sbArtikelInfo.append("\n").append(
						oArtikelDtoI.getArtikelsprDto().getCZbez());
			}
		}

		return sbArtikelInfo.toString();
	}

	/**
	 * Berechnung des Materialwerts eines Auftrags in Mandantenwaehrung. <br>
	 * Der Materialwert ist die Summe ueber die Materialwerte der enthaltenen
	 * Artikelpositionen. <br>
	 * Der Materialwert einer Artikelposition errechnet sich aus Menge x
	 * Gestehungspreis des enthaltenen Artikels. <br>
	 * Der Gestehungspreis eines Artikels ist in Mandantenwaehrung abgelegt.
	 *
	 * @param iIdAuftragI
	 *            pk des Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Materialwert in der gewuenschten Waehrung
	 */
	public BigDecimal berechneMaterialwertGesamt(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		BigDecimal materialwert = new BigDecimal(0);
		try {
			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);

			AuftragDto aDto = auftragFindByPrimaryKey(iIdAuftragI);

			// das Hauptlager des Mandanten bestimmen
			LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(
					aDto.getLagerIIdAbbuchungslager());
			for (int i = 0; i < aAuftragpositionDto.length; i++) {
				AuftragpositionDto auftragpositionDto = aAuftragpositionDto[i];

				// alle positiven mengenbehafteten Positionen beruecksichtigen
				if (auftragpositionDto.getNMenge() != null
						&& auftragpositionDto.getNMenge().doubleValue() > 0
						&& !auftragpositionDto.isPosition()) {

					if (auftragpositionDto.isIntelligenteZwischensumme()) {
						continue;
					}

					BigDecimal menge = auftragpositionDto.getNMenge();

					// Grundlage ist der Gestehungspreis des Artikels am
					// Hauptlager des Mandanten
					BigDecimal gestehungspreis = getLagerFac()
							.getGemittelterGestehungspreisEinesLagers(
									auftragpositionDto.getArtikelIId(),
									lagerDto.getIId(), theClientDto);

					BigDecimal wertDerPosition = menge
							.multiply(gestehungspreis);

					materialwert = materialwert.add(wertDerPosition);
				}
			}

			// checknumberformat: 1 die Nachkommastellen kappen
			materialwert = Helper.rundeKaufmaennisch(materialwert, 4);
			// checknumberformat: 2 passt der Wert in die DB?
			checkNumberFormat(materialwert);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(t));
		}

		return materialwert;
	}

	/**
	 * Fuer eine bestimmte Auftragsart fuer einen bestimmten Zeitraum und eine
	 * bestimmte Terminart (Belegdatum, Liefertermin, Finaltermin) den
	 * Nettoauftragswert in Abhaengigkeit vom aktuellen Mandanten bestimmen. <br>
	 * Beruecksichtigt werden koennen entweder alle offenen Auftraege oder alle
	 * eingegangenen Auftraege.
	 *
	 * @param cNrAuftragartI
	 *            die Auftragart (Frei, Rahmen, Abruf)
	 * @param whichKriteriumI
	 *            welche Zeitraumart (Belegdatum, Liefertermin, Finaltermin)
	 * @param gcBerechnungsdatumVonI
	 *            ab diesem Datum
	 * @param gcBerechnungsdatumBisI
	 *            bis zu diesem Datum
	 * @param offenOderEingegangenI
	 *            sollen alle offenen oder alle eingegangengen Auftraegr
	 *            beruecksichtigt werden
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Nettoauftragswert, 0 wenn keine offenen Positionen
	 *         gefunden wurden
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneSummeAuftragsnettowert(String cNrAuftragartI,
			String whichKriteriumI, GregorianCalendar gcBerechnungsdatumVonI,
			GregorianCalendar gcBerechnungsdatumBisI,
			String offenOderEingegangenI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer();
		buff.append(offenOderEingegangenI)
				.append(", ")
				.append(whichKriteriumI)
				.append(", ")
				.append(cNrAuftragartI)
				.append(", ")
				.append("Von: ")
				.append(Helper.formatDatum(gcBerechnungsdatumVonI.getTime(),
						theClientDto.getLocUi()))
				.append(", Bis: ")
				.append(Helper.formatDatum(gcBerechnungsdatumBisI.getTime(),
						theClientDto.getLocUi()));

		myLogger.info(buff.toString());

		BigDecimal nSummeAuftragsnettowert = new BigDecimal(0);
		Session session = null;

		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			// Criteria duerfen keine Texts oder Blobs enthalten!

			// Criteria anlegen fuer alle referenzierten Objekte anlegen
			Criteria crit = session
					.createCriteria(FLRAuftragFuerUebersicht.class);

			// Einschraenken nach Mandant
			crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			// Einschraenken nach Auftragart
			crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
					cNrAuftragartI));

			if (whichKriteriumI.equals(AuftragFac.KRIT_BELEGDATUM)) {
				// Belegdatum von bis: flrauftrag.t_belegdatum
				if (gcBerechnungsdatumVonI != null) {
					crit.add(Restrictions.ge(
							AuftragFac.FLR_AUFTRAG_D_BELEGDATUM,
							new java.sql.Date(gcBerechnungsdatumVonI
									.getTimeInMillis())));
				}

				if (gcBerechnungsdatumBisI != null) {
					crit.add(Restrictions.lt(
							AuftragFac.FLR_AUFTRAG_D_BELEGDATUM,
							new java.sql.Date(gcBerechnungsdatumBisI
									.getTimeInMillis())));
				}
			} else if (whichKriteriumI.equals(AuftragFac.KRIT_LIEFERTERMIN)) {
				// Liefertermin von bis: flrauftrag.t_liefertermin
				if (gcBerechnungsdatumVonI != null) {
					crit.add(Restrictions.ge(
							AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN,
							new java.sql.Date(gcBerechnungsdatumVonI
									.getTimeInMillis())));
				}

				if (gcBerechnungsdatumBisI != null) {
					crit.add(Restrictions.le(
							AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN,
							new java.sql.Date(gcBerechnungsdatumBisI
									.getTimeInMillis())));
				}
			} else if (whichKriteriumI.equals(AuftragFac.KRIT_FINALTERMIN)) {
				// Belegdatum von bis: flrauftrag.t_finaltermin
				if (gcBerechnungsdatumVonI != null) {
					crit.add(Restrictions.ge(
							AuftragFac.FLR_AUFTRAG_T_FINALTERMIN,
							new java.sql.Date(gcBerechnungsdatumVonI
									.getTimeInMillis())));
				}

				if (gcBerechnungsdatumBisI != null) {
					crit.add(Restrictions.le(
							AuftragFac.FLR_AUFTRAG_T_FINALTERMIN,
							new java.sql.Date(gcBerechnungsdatumBisI
									.getTimeInMillis())));
				}
			}

			// Einschraenken nach Auftragstatus
			if (offenOderEingegangenI
					.equals(AuftragFac.AUFT_UMSATZUEBERSICHT_OFFEN)) {
				crit.add(Expression.or(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
						AuftragServiceFac.AUFTRAGSTATUS_OFFEN), Restrictions
						.eq(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
								AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)));
			} else if (offenOderEingegangenI
					.equals(AuftragFac.AUFT_UMSATZUEBERSICHT_EINGANG)) {
				crit.add(Restrictions.ne(
						AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
						AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT));
				crit.add(Restrictions.ne(
						AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
						AuftragServiceFac.AUFTRAGSTATUS_STORNIERT));
			}

			List<?> list = crit.list();

			if (offenOderEingegangenI
					.equals(AuftragFac.AUFT_UMSATZUEBERSICHT_OFFEN)) {
				nSummeAuftragsnettowert = berechneAuftragsnettowertOffen(list,
						session, theClientDto);
			} else if (offenOderEingegangenI
					.equals(AuftragFac.AUFT_UMSATZUEBERSICHT_EINGANG)) {
				nSummeAuftragsnettowert = berechneAuftragsnettowertEingang(
						list, theClientDto);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
					new Exception(t));
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		return nSummeAuftragsnettowert;
	}

	/**
	 * Hier wird der Auftragsnettowert fuer eine Hibernate Liste von offenen
	 * Auftraegen bestimmt (Status Offen oder Teilerledigt). <br>
	 * Dabei werden alle Auftragswerte in Mandantenwaehrung beruecksichtigt.
	 *
	 * @param listFLRAuftragFuerUebersichtI
	 *            Liste von FLRAuftragFuerUebersicht Objekten
	 * @param sessionI
	 *            die aktive Hibernate Session
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der offene Auftragsnettowert
	 * @throws Throwable
	 *             Ausnahme
	 */
	private BigDecimal berechneAuftragsnettowertOffen(
			List<?> listFLRAuftragFuerUebersichtI, Session sessionI,
			TheClientDto theClientDto) throws Throwable {
		BigDecimal nSummeAuftragsnettowert = new BigDecimal(0);

		if (listFLRAuftragFuerUebersichtI != null) {
			Iterator<?> it = listFLRAuftragFuerUebersichtI.iterator();

			while (it.hasNext()) {
				BigDecimal nBeitragDiesesAuftrags = new BigDecimal(0);

				FLRAuftragFuerUebersicht flrauftrag = (FLRAuftragFuerUebersicht) it
						.next();

				// wir befinden uns innerhalb einer Hibernate Session
				Criteria critAuftragposition = sessionI
						.createCriteria(FLRAuftragpositionFuerUebersicht.class);

				critAuftragposition.add(Restrictions.eq(
						AuftragpositionFac.FLR_AUFTRAGPOSITION_AUFTRAG_I_ID,
						flrauftrag.getI_id()));
				critAuftragposition
						.add(Restrictions
								.isNotNull(AuftragpositionFac.FLR_AUFTRAGPOSITION_N_OFFENEMENGE));
				critAuftragposition.add(Restrictions.gt(
						AuftragpositionFac.FLR_AUFTRAGPOSITION_N_OFFENEMENGE,
						new BigDecimal(0)));

				List<?> listPositionen = critAuftragposition.list();
				Iterator<?> it2 = listPositionen.iterator();

				while (it2.hasNext()) {
					FLRAuftragpositionFuerUebersicht flrauftragposition = (FLRAuftragpositionFuerUebersicht) it2
							.next();

					BigDecimal bdBeitragDieserPosition = flrauftragposition
							.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
							.multiply(flrauftragposition.getN_offenemenge());

					nBeitragDiesesAuftrags = nBeitragDiesesAuftrags
							.add(bdBeitragDieserPosition);
				}

				// Umrechnen des Beitrags in Mandantenwaehrung
				Double ddWechselkursReziprok = flrauftrag
						.getF_wechselkursmandantwaehrungzuauftragswaehrung();

				if (ddWechselkursReziprok != null
						&& ddWechselkursReziprok.doubleValue() != 1) {
					ddWechselkursReziprok = new Double(
							1 / ddWechselkursReziprok.doubleValue());

					nBeitragDiesesAuftrags = nBeitragDiesesAuftrags
							.multiply(new BigDecimal(ddWechselkursReziprok
									.doubleValue()));
				}

				nBeitragDiesesAuftrags = Helper.rundeKaufmaennisch(
						nBeitragDiesesAuftrags, 4);
				checkNumberFormat(nBeitragDiesesAuftrags);

				nSummeAuftragsnettowert = nSummeAuftragsnettowert
						.add(nBeitragDiesesAuftrags);
			}
		}

		return nSummeAuftragsnettowert;
	}

	/**
	 * Hier wird der Auftragsnettowert fuer eine Hibernate Liste von
	 * eingegangenen Auftraegen bestimmt (Status Offen, Teilerledigt, Erledigt). <br>
	 * Dabei werden alle Auftragswerte in Mandantenwaehrung beruecksichtigt.
	 *
	 * @param listFLRAuftragFuerUebersichtI
	 *            Liste von FLRAuftragFuerUebersicht Objekten
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der offene Auftragsnettowert
	 * @throws Throwable
	 *             Ausnahme
	 */
	private BigDecimal berechneAuftragsnettowertEingang(
			List<?> listFLRAuftragFuerUebersichtI, TheClientDto theClientDto)
			throws Throwable {
		BigDecimal nSummeAuftragsnettowert = new BigDecimal(0);

		if (listFLRAuftragFuerUebersichtI != null) {
			Iterator<?> it = listFLRAuftragFuerUebersichtI.iterator();

			while (it.hasNext()) {
				FLRAuftragFuerUebersicht flrauftrag = (FLRAuftragFuerUebersicht) it
						.next();

				// der Gesamtwert wurde bei der Aktivierung in Auftragswaehrung
				// hinterlegt
				BigDecimal nBeitragDiesesAuftrags = flrauftrag
						.getN_gesamtauftragswertinauftragswaehrung();

				// Umrechnen des Beitrags in Mandantenwaehrung
				Double ddWechselkursReziprok = flrauftrag
						.getF_wechselkursmandantwaehrungzuauftragswaehrung();

				if (ddWechselkursReziprok != null
						&& ddWechselkursReziprok.doubleValue() != 1) {
					ddWechselkursReziprok = new Double(
							1 / ddWechselkursReziprok.doubleValue());

					nBeitragDiesesAuftrags = nBeitragDiesesAuftrags
							.multiply(new BigDecimal(ddWechselkursReziprok
									.doubleValue()));
				}

				nBeitragDiesesAuftrags = Helper.rundeKaufmaennisch(
						nBeitragDiesesAuftrags, 4);
				checkNumberFormat(nBeitragDiesesAuftrags);

				nSummeAuftragsnettowert = nSummeAuftragsnettowert
						.add(nBeitragDiesesAuftrags);
			}
		}

		return nSummeAuftragsnettowert;
	}

	/**
	 * Berechne den Gesamtwert eines bestimmten Auftrags in der
	 * Auftragswaehrung. <br>
	 * Der Gesamtwert berechnet sich aus
	 * <p>
	 * Summe der Nettogesamtpreise der Positionen <br>
	 * + Versteckter Aufschlag <br>
	 * - Allgemeiner Rabatt <br>
	 * - Projektierungsrabatt <br>
	 * Beruecksichtigt werden alle mengenbehafteten Positionen.
	 *
	 * @param aAuftragpositionDto
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Gesamtwert des Auftrags
	 */

	public AuftragpositionDto[] entferneKalkulatorischeArtikel(
			AuftragpositionDto[] aAuftragpositionDto, TheClientDto theClientDto) {
		ArrayList al = new ArrayList();

		for (int i = 0; i < aAuftragpositionDto.length; i++) {

			if (aAuftragpositionDto[i].getArtikelIId() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						aAuftragpositionDto[i].getArtikelIId(), theClientDto);
				if (Helper.short2boolean(aDto.getBKalkulatorisch())) {
					continue;
				}
			}
			al.add(aAuftragpositionDto[i]);
		}
		AuftragpositionDto[] returnArray = new AuftragpositionDto[al.size()];
		return (AuftragpositionDto[]) al.toArray(returnArray);

	}

	public BigDecimal berechneNettowertGesamt(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		BigDecimal auftragswert = new BigDecimal(0);
		try {
			AuftragDto auftragDto = auftragFindByPrimaryKey(iIdAuftragI);

			if (auftragDto.getNGesamtauftragswertInAuftragswaehrung() == null
					|| auftragDto.getStatusCNr().equals(
							AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
				// Step 1: Wenn der Gesamtauftragswert NULL und der Status
				// ANGELEGT ist, dann den Wert aus den Positionen berechnen
				AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByAuftrag(iIdAuftragI);

				aAuftragpositionDto = entferneKalkulatorischeArtikel(
						aAuftragpositionDto, theClientDto);

				auftragswert = getBelegVerkaufFac()
						.getGesamtwertinBelegwaehrung(aAuftragpositionDto,
								auftragDto);
				/*
				 * for (int i = 0; i < aAuftragpositionDto.length; i++) {
				 * AuftragpositionDto auftragpositionDto =
				 * aAuftragpositionDto[i];
				 *
				 * // alle mengenbehafteten Positionen beruecksichtigen if
				 * (auftragpositionDto.getNMenge() != null) {
				 *
				 * BigDecimal menge = auftragpositionDto.getNMenge();
				 *
				 * // Grundlage ist der
				 * NettogesamtwertPlusVersteckterAufschlagMinusRabatte der
				 * Position in Auftragswaehrung // BigDecimal wertDerPosition =
				 * menge.multiply(auftragpositionDto. //
				 * getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
				 * BigDecimal wertDerPosition =
				 * menge.multiply(auftragpositionDto.
				 * getNNettoeinzelpreisplusversteckteraufschlag()); auftragswert
				 * = auftragswert.add(wertDerPosition); } } // - Allgemeiner
				 * Rabatt BigDecimal bdAllgemeinerRabattSatz = new
				 * BigDecimal(auftragDto.getFAllgemeinerRabattsatz().
				 * doubleValue()).movePointLeft(2); bdAllgemeinerRabattSatz =
				 * Helper.rundeKaufmaennisch(bdAllgemeinerRabattSatz, 4);
				 *
				 * BigDecimal bdAllgemeinerRabatt =
				 * auftragswert.multiply(bdAllgemeinerRabattSatz); // auf 4
				 * Stellen runden bdAllgemeinerRabatt =
				 * Helper.rundeKaufmaennisch(bdAllgemeinerRabatt, 4);
				 *
				 * auftragswert=auftragswert.subtract(bdAllgemeinerRabatt);
				 *
				 * // - Projektierungsrabatt BigDecimal bdProjektierungsRabatt =
				 * new BigDecimal(auftragDto.
				 * getFProjektierungsrabattsatz().doubleValue
				 * ()).movePointLeft(2); bdProjektierungsRabatt =
				 * Helper.rundeKaufmaennisch(bdProjektierungsRabatt, 4);
				 *
				 * BigDecimal bdNettogesamtpreisProjektierungsrabattSumme =
				 * auftragswert.multiply( bdProjektierungsRabatt); // auf 4
				 * Stellen runden bdNettogesamtpreisProjektierungsrabattSumme =
				 * Helper.rundeKaufmaennisch(
				 * bdNettogesamtpreisProjektierungsrabattSumme, 4); auftragswert
				 * =
				 * auftragswert.subtract(bdNettogesamtpreisProjektierungsrabattSumme
				 * );
				 */
				for (AuftragpositionDto auftragPositionDto : aAuftragpositionDto) {
					if (auftragPositionDto.isIntelligenteZwischensumme()) {
						auftragPositionDto.setNOffeneMenge(auftragPositionDto
								.getNMenge());
						getAuftragpositionFac()
								.updateAuftragpositionOhneWeitereAktion(
										auftragPositionDto, theClientDto);
					}
				}
			} else if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
				// Step 2: Wenn der status STORNIERT ist, 0 zurueckgeben obwohl
				// der Auftragswert noch in der Tabelle steht
				auftragswert = Helper.getBigDecimalNull();
			} else if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
					|| auftragDto.getStatusCNr().equals(
							AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)
					|| auftragDto.getStatusCNr().equals(
							AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				// Step 3: Wenn der status OFFEN, TEILERLEDIGT oder ERLEDIGT
				// ist, den Auftragswert aus der Tabelle holen
				if (auftragDto.getNGesamtauftragswertInAuftragswaehrung() != null) {
					auftragswert = auftragDto
							.getNGesamtauftragswertInAuftragswaehrung();
				}
			}

			// Step 4: Der Wert muss in unsere Datenbank passen, fuer decimal(9)
			// gilt 15,4
			auftragswert = Helper.rundeKaufmaennisch(auftragswert, 4);
			checkNumberFormat(auftragswert);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(t));
		}

		return auftragswert;
	}

	/**
	 * Berechnet den Nettoauftragswert fuer einen Kunden in einem bestimmten
	 * Zeitintervall.
	 *
	 * @param iIdKundeI
	 *            pk des Kunden
	 * @param datVonI
	 *            von diesem Datum weg inclusive diesem Datum
	 * @param datBisI
	 *            bis zu diesem Datum inclusive diesem Datum
	 * @param cCurrencyI
	 *            die Zielwaehrung
	 * @throws EJBExceptionLP
	 * @return BigDecimal der Nettoauftragswert ohne Mwst
	 */
	public BigDecimal berechneGesamtwertAuftragProKundeProZeitintervall(
			Integer iIdKundeI, Date datVonI, Date datBisI, String cCurrencyI)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGesamtwertAuftragProKundeProZeitintervall";

		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iiKundeI == null"));
		}
		if (datVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datVonI == null"));
		}
		if (datVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datBisI == null"));
		}
		if (cCurrencyI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cCurrencyI == null"));
		}

		BigDecimal bdAuftragswertSumme = Helper.getBigDecimalNull();
		Query query = em.createNamedQuery("AuftragfindByKundeBelegdatumVonBis");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, datVonI);
		query.setParameter(3, datBisI);
		Collection<?> c = query.getResultList();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Auftrag auftrag = ((Auftrag) iter.next());

			// der gesamtwert des auftrags ist in der db hinterlegt
			BigDecimal bdAuftragswert = auftrag
					.getNGesamtauftragswertinauftragswaehrung();

			// der auftragswert muss in die zielwaehrung umgerechnet werden
			// bdAuftragswert = getLocaleFac().
			// rechneUmInAndereWaehrung(bdAuftragswert,
			// auftrag.getCAuftragswaehrung(), cCurrencyI);

			bdAuftragswertSumme = bdAuftragswertSumme.add(bdAuftragswert);
		}
		return bdAuftragswertSumme;
	}

	/**
	 * Berechnet den Nettoauftragswert fuer alle Auftraege eines Kunden
	 * innerhalb eines bestimmten Zeitintervalls, die sich in einem bestimmten
	 * Status befinden.
	 *
	 * @param sStatusI
	 *            der gewuenschte Status
	 * @param iIdKundeI
	 *            pk des Kunden
	 * @param datVonI
	 *            alle Auftraege ab diesem Datum inclusive
	 * @param datBisI
	 *            alle Auftraege bis zu diesem Datum inclusive
	 * @param cCurrencyI
	 *            die Zielwaehrung
	 * @throws EJBExceptionLP
	 * @return BigDecimal der Gesamtwert in Zielwaehrung
	 */
	public BigDecimal berechneGesamtwertAuftragProStatusProKundeProZeitintervall(
			String sStatusI, Integer iIdKundeI, Date datVonI, Date datBisI,
			String cCurrencyI) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGesamtwertAuftragProStatusProKundeProZeitintervall";

		if (sStatusI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sStatusI == null"));
		}
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iiKundeI == null"));
		}
		if (datVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datVonI == null"));
		}
		if (datVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datBisI == null"));
		}
		if (cCurrencyI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cCurrencyI == null"));
		}

		BigDecimal bdAuftragswertSumme = Helper.getBigDecimalNull();
		Query query = em
				.createNamedQuery("AuftragfindByStatusKundeBelegdatumVonBis");
		query.setParameter(1, sStatusI);
		query.setParameter(2, iIdKundeI);
		query.setParameter(3, datVonI);
		query.setParameter(4, datBisI);
		Collection<?> c = query.getResultList();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Auftrag auftrag = ((Auftrag) iter.next());

			// der gesamtwert des auftrags ist in der db hinterlegt
			BigDecimal bdAuftragswert = auftrag
					.getNGesamtauftragswertinauftragswaehrung();

			// der auftragswert muss in die zielwaehrung umgerechnet werden
			// bdAuftragswert = getLocaleFac().
			// rechneUmInAndereWaehrung(bdAuftragswert,
			// auftrag.getCAuftragswaehrung(), cCurrencyI);

			bdAuftragswertSumme = bdAuftragswertSumme.add(bdAuftragswert);
		}
		return bdAuftragswertSumme;
	}

	public void updateArtikelnummerInAuftrag() {
	}

	/**
	 * Den Status des Auftrags aendern.
	 *
	 * @param pkAuftrag
	 *            PK des Auftrags
	 * @param status
	 *            der neue Status
	 * @param theClientDto
	 *            der aktuelle User, null wenn keine Kopfdaten geaendert wurden
	 * @throws EJBExceptionLP
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean aendereAuftragstatus(Integer pkAuftrag, String status,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		boolean statusGeaendert = false;
		AuftragDto auftragDto = auftragFindByPrimaryKey(pkAuftrag);
		String sStautsVorAenderung = auftragDto.getStatusCNr();
		auftragDto.setStatusCNr(status);
		// wenn der Status auf angelegt wechselt, die Auftragswerte
		// zuruecksetzen
		if (status.equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
			auftragDto.setNGesamtauftragswertInAuftragswaehrung(null);
			auftragDto.setNMaterialwertInMandantenwaehrung(null);
			auftragDto.setNRohdeckungInMandantenwaehrung(null);
		}
		if (status.equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
			auftragDto.setPersonalIIdErledigt(theClientDto.getIDPersonal());
			auftragDto.setTErledigt(Helper.cutTimestamp(getTimestamp()));
		} else {
			auftragDto.setPersonalIIdErledigt(null);
			auftragDto.setTErledigt(null);
		}
		// Benutzerinformation setzen
		if (theClientDto.getIDUser() != null) {
			auftragDto.setPersonalIIdAendern(theClientDto.getIDPersonal());

			Timestamp t = new Timestamp(System.currentTimeMillis());
			auftragDto.setTAendern(t);
		}
		Auftrag auftrag = em.find(Auftrag.class, pkAuftrag);
		if (auftrag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setAuftragFromAuftragDto(auftrag, auftragDto);
		if (AuftragServiceFac.AUFTRAGSTATUS_STORNIERT
				.equals(sStautsVorAenderung)) {
			AuftragpositionDto[] posDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(pkAuftrag);
			for (int i = 0; i < posDto.length; i++) {
				posDto[i]
						.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				getAuftragpositionFac().updateAuftragposition(posDto[i],
						theClientDto);

				// Wenn Abrufposition muss auch Rahmen upgedatet werden
				if (posDto[i].getAuftragpositionIIdRahmenposition() != null) {
					AuftragpositionDto rahmenPosDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									posDto[i]
											.getAuftragpositionIIdRahmenposition());
					getAuftragpositionFac().updateAuftragposition(rahmenPosDto,
							theClientDto);
				}

			}
		}
		statusGeaendert = true;
		return statusGeaendert;
	}

	public void korrekturbetragZuruecknehmen(Integer auftragIId) {
		Auftrag auftrag = em.find(Auftrag.class, auftragIId);
		auftrag.setNKorrekturbetrag(null);
		em.merge(auftrag);
		em.flush();
	}


	/**
	 * Der Auftragstatus ergibt sich aus der Summe der Stati der einzelnen
	 * Positionen.
	 *
	 * @param iIdAuftragI
	 *            pk des Auftrag
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void setzeAuftragstatusAufgrundAuftragpositionstati(
			Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			// bei
			// - AuftragFac.AUFTRAGSTATUS_ANGELEGT
			// - AuftragFac.AUFTRAGSTATUS_STORNIERT
			// muss man die stati der positionen nicht weiter ansehen
			AuftragDto auftragDto = auftragFindByPrimaryKey(iIdAuftragI);

			if (!auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)
					&& !auftragDto.getStatusCNr().equals(
							AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
				AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByAuftragIIdNMengeNotNull(
								iIdAuftragI, theClientDto);

				int iAnzahlOffen = zaehleStatiAuftragpositionen(
						aAuftragpositionDto,
						AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				// int iAnzahlTeilerledigt = zaehleStatiAuftragpositionen(c,
				// AuftragFac.AUFTRAGPOSITIONSTATUS_TEILERLEDIGT);
				int iAnzahlErledigt = zaehleStatiAuftragpositionen(
						aAuftragpositionDto,
						AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);

				if (iAnzahlOffen == aAuftragpositionDto.length) {
					auftragDto
							.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
				} else if (iAnzahlErledigt == aAuftragpositionDto.length) {
					// PJ18288
					ParametermandantDto parameterDto = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_AUFTRAG,
									ParameterFac.PARAMETER_AUFTRAG_AUTOMATISCH_VOLLSTAENDIG_ERLEDIGEN);
					if ((Boolean) parameterDto.getCWertAsObject() == true) {
						auftragDto
								.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
					} else {
						auftragDto
								.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
					}
				} else {
					auftragDto
							.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
				}

				aendereAuftragstatus(auftragDto.getIId(),
						auftragDto.getStatusCNr(), theClientDto);
				if (AuftragServiceFac.AUFTRAGART_ABRUF.equals(auftragDto
						.getAuftragartCNr())) {
					getAuftragRahmenAbrufFac().pruefeUndSetzeRahmenstatus(
							auftragDto.getAuftragIIdRahmenauftrag(),
							theClientDto);
				}
			}
			//wg. SP2983 auskommentiert, da ansonsten das T_ERLEDIGT wieder auf null gesetzt wird
			//updateAuftragOhneWeitereAktion(auftragDto, theClientDto);
		} catch (EJBExceptionLP ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
	}

	/**
	 * Anzahl eines bestimmten Status in einer Liste von Auftragpositionen
	 * bestimmen.
	 *
	 * @param aAuftragpositionDtoI
	 *            Liste der Auftragpositionen
	 * @param sStatusI
	 *            Status
	 * @throws Throwable
	 * @return int die Anzahl der Stati
	 */
	private int zaehleStatiAuftragpositionen(
			AuftragpositionDto[] aAuftragpositionDtoI, String sStatusI)
			throws Throwable {
		int iAnzahl = 0;

		for (int i = 0; i < aAuftragpositionDtoI.length; i++) {
			AuftragpositionDto auftragpositionDto = aAuftragpositionDtoI[i];

			// es gibt Positionen mit Status null, z.B. Betrifft
			if (auftragpositionDto.getNMenge() != null) {
				if (auftragpositionDto.getAuftragpositionstatusCNr().equals(
						sStatusI)) {
					iAnzahl++;
				}
			}
		}

		return iAnzahl;
	}

	/**
	 * Fuer die Nachkalkulation des Auftrags den Soll-Verkaufswert (=
	 * NettoVerkaufspreisPlusAufschlaegeMinusRabatte pro Stueck * geplanter
	 * Menge) bezogen auf eine bestimmte Artikelart berechnen. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Auftragpositionen.
	 *
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param sArtikelartI
	 *            die gewuenschte Artikelart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Verkaufswert der Artikelart Soll in
	 *         Auftragswaherung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneVerkaufswertSoll(Integer iIdAuftragI,
			String sArtikelartI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneVKWArbeitSoll";
		myLogger.entry();
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sArtikelartI == null"));
		}

		BigDecimal bdVerkaufswertSollO = Helper.getBigDecimalNull();

		try {
			AuftragpositionDto[] aAuftragpositionDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);

			for (int i = 0; i < aAuftragpositionDtos.length; i++) {

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aAuftragpositionDtos[i].getNMenge() != null
						&& aAuftragpositionDtos[i].getArtikelIId() != null
						&& aAuftragpositionDtos[i].getPositioniIdArtikelset() == null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aAuftragpositionDtos[i].getArtikelIId(),
									theClientDto);

					// je nach Artikelart beruecksichtigen
					BigDecimal bdBeitragDieserPosition = aAuftragpositionDtos[i]
							.getNMenge()
							.multiply(
									aAuftragpositionDtos[i]
											.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

					if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						if (oArtikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdVerkaufswertSollO = bdVerkaufswertSollO
									.add(bdBeitragDieserPosition);
						}
					} else {
						if (!oArtikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdVerkaufswertSollO = bdVerkaufswertSollO
									.add(bdBeitragDieserPosition);
						}
					}
				}

				if (aAuftragpositionDtos[i].isIntelligenteZwischensumme()
						&& aAuftragpositionDtos[i]
								.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() != null) {
					if (!sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						bdVerkaufswertSollO = bdVerkaufswertSollO
								.add(aAuftragpositionDtos[i]
										.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
					}

				}
			}
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Verkaufswert " + sArtikelartI + " Soll : "
				+ bdVerkaufswertSollO.toString());

		return bdVerkaufswertSollO;
	}

	/**
	 * Fuer die Nachkalkulation des Auftrags den Soll-Gestehungspreis (=
	 * Gestehungspreis am Hauptlager des Mandanten pro Stueck * geplanter Menge)
	 * bezogen auf eine bestimmte Artikelart berechnen. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Auftragpositionen.
	 *
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param sArtikelartI
	 *            die gewuenschte Artikelart
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der Gestehungswert der Artikelart Soll in
	 *         Auftragswaherung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */

	private BigDecimal getGestehungswertSoll(
			AuftragpositionDto aAuftragpositionDto, AuftragDto auftragDto,
			TheClientDto theClientDto) {

		BigDecimal bdGestehungspreisSoll = new BigDecimal(0);
		if (aAuftragpositionDto.getPositioniIdArtikelset() == null
				&& aAuftragpositionDto.getArtikelIId() != null) {

			try {
				LagerDto lDto = getLagerFac().getHauptlagerDesMandanten(
						theClientDto);

				Query query = em
						.createNamedQuery("AuftragpositionfindByPositionIIdArtikelset");
				query.setParameter(1, aAuftragpositionDto.getIId());
				Collection<?> angebotpositionDtos = query.getResultList();
				AuftragpositionDto[] zugehoerigeABPosDtos = AuftragpositionDtoAssembler
						.createDtos(angebotpositionDtos);

				if (zugehoerigeABPosDtos.length == 0) {

					ParametermandantDto parametermandantDto = getParameterFac()
							.getMandantparameter(theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ANGEBOT,
									ParameterFac.PARAMETER_LIEFERANT_ANGEBEN);

					boolean bLieferantAngeben = (Boolean) parametermandantDto
							.getCWertAsObject();

					if (bLieferantAngeben == true) {
						if (aAuftragpositionDto.getBdEinkaufpreis() != null) {
							// Als erstes zaehlt der EK-reis aus der Position
							return aAuftragpositionDto.getBdEinkaufpreis()
									.multiply(aAuftragpositionDto.getNMenge());
						} else {
							// anosonsten der Lief1Preis
							ArtikellieferantDto alDto = getArtikelFac()
									.getArtikelEinkaufspreis(
											aAuftragpositionDto.getArtikelIId(),
											aAuftragpositionDto.getNMenge(),
											auftragDto.getCAuftragswaehrung(),
											theClientDto);
							if (alDto != null && alDto.getNNettopreis() != null) {
								return alDto.getNNettopreis().multiply(
										aAuftragpositionDto.getNMenge());
							}

						}
					}

					bdGestehungspreisSoll = getLagerFac()
							.getGestehungspreisZumZeitpunkt(
									aAuftragpositionDto.getArtikelIId(),
									lDto.getIId(), auftragDto.getTBelegdatum(),
									theClientDto);
					if (bdGestehungspreisSoll != null) {
						bdGestehungspreisSoll = bdGestehungspreisSoll
								.multiply(aAuftragpositionDto.getNMenge());
					}

				} else {

					for (int k = 0; k < zugehoerigeABPosDtos.length; k++) {
						AuftragpositionDto apDtoSetartikel = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(
										zugehoerigeABPosDtos[k].getIId());
						bdGestehungspreisSoll = bdGestehungspreisSoll
								.add(getGestehungswertSoll(apDtoSetartikel,
										auftragDto, theClientDto));
					}
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		return bdGestehungspreisSoll;

	}

	public BigDecimal berechneGestehungswertSoll(Integer iIdAuftragI,
			String sArtikelartI, boolean bMitEigengefertigtenStuecklisten,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sArtikelartI == null"));
		}

		BigDecimal bdGestehungswertSollO = Helper.getBigDecimalNull();

		try {

			AuftragDto auftragDto = auftragFindByPrimaryKey(iIdAuftragI);
			AuftragpositionDto[] aAuftragpositionDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);

			for (int i = 0; i < aAuftragpositionDtos.length; i++) {

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aAuftragpositionDtos[i].getNMenge() != null
						&& aAuftragpositionDtos[i].getArtikelIId() != null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aAuftragpositionDtos[i].getArtikelIId(),
									theClientDto);

					if (bMitEigengefertigtenStuecklisten == false) {
						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
										oArtikelDto.getIId(), theClientDto);
						if (stklDto != null
								&& Helper.short2boolean(stklDto
										.getBFremdfertigung()) == false) {
							continue;
						}
					}

					// Grundlage ist der Gestehungspreis des Artikels am
					// Hauptlager des Mandanten
					BigDecimal bdGestehungspreisSoll = getGestehungswertSoll(
							aAuftragpositionDtos[i], auftragDto, theClientDto);

					// je nach Artikelart beruecksichtigen

					if (bdGestehungspreisSoll != null) {
						if (sArtikelartI
								.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							if (oArtikelDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bdGestehungswertSollO = bdGestehungswertSollO
										.add(bdGestehungspreisSoll);
							}
						} else {
							if (!oArtikelDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bdGestehungswertSollO = bdGestehungswertSollO
										.add(bdGestehungspreisSoll);
							}
						}
					}
				}
			}
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Gestehungswert " + sArtikelartI + " Soll : "
				+ bdGestehungswertSollO.toString());

		return bdGestehungswertSollO;
	}

	/**
	 * Die Anzahl der Belege holen, die zu einem bestimmten Auftrag existieren. <br>
	 * Als Beleg gilt auch, wenn Auftragzeiten zu diesem Auftrag erfasst wurden.
	 *
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return int die Anzahl der Belege zu diesem Auftrag
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public int berechneAnzahlBelegeZuAuftrag(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneAnzahlBelegeZuAuftrag";
		myLogger.entry();
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}
		int iAnzahlBelegeO = 0;
		try {
			// Eingangsrechnungen
			EingangsrechnungAuftragszuordnungDto[] aEingangsrechnungDtos = getEingangsrechnungFac()
					.eingangsrechnungAuftragszuordnungFindByAuftragIId(
							iIdAuftragI);

			iAnzahlBelegeO += aEingangsrechnungDtos.length;

			// Lieferscheine
			LieferscheinDto[] aLieferscheinDtos = getLieferscheinFac()
					.lieferscheinFindByAuftrag(iIdAuftragI, theClientDto);

			iAnzahlBelegeO += aLieferscheinDtos.length;

			AuftragpositionDto[] aPositionDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);
			iAnzahlBelegeO += aPositionDtos.length;
			// @todo Rechnungen PJ 3809

			// Auftragzeiten
			AuftragzeitenDto[] aAuftragzeitenDtos = getZeiterfassungFac()
					.getAllZeitenEinesBeleges(LocaleFac.BELEGART_AUFTRAG,
							iIdAuftragI, null, null, null, null, false, // order
							// by
							// artikelcnr
							true, // order by personal
							theClientDto);
			if (aAuftragzeitenDtos != null && aAuftragzeitenDtos.length > 0) {
				iAnzahlBelegeO++;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return iAnzahlBelegeO;
	}

	public void aktiviereAuftrag(Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.notNull(iIdAuftragI, "iIdAuftragI");
		pruefeAktivierbar(iIdAuftragI, theClientDto);
		// Wert berechnen
		berechneBeleg(iIdAuftragI, theClientDto);
		// und Status aendern
		aktiviereBeleg(iIdAuftragI, theClientDto);
	}

	@Override
	public void pruefeAktivierbar(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Auftrag oAuftrag = em.find(Auftrag.class, iid);
		if (oAuftrag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// Ohne Positionen darf der Beleg nicht aktiviert werden.
		if (getAuftragpositionFac().getAnzahlMengenbehafteteAuftragpositionen(
				oAuftrag.getIId(), theClientDto) == 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN, "");
		}

		// PJ 15233
		if (!getTheJudgeFac().hatRecht(RechteFac.RECHT_AUFT_AKTIVIEREN,
				theClientDto)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN,
					"FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN");
		}
	}

	@Override
	public void aktiviereBeleg(Integer iIdAuftragI, TheClientDto theClientDto) {
		Auftrag oAuftrag = em.find(Auftrag.class, iIdAuftragI);
		if (oAuftrag.getAuftragstatusCNr().equals(
				AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {

			oAuftrag.setTGedruckt(getTimestamp());
			oAuftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);

			// PJ 15710
			Kunde kunde = em.find(Kunde.class,
					oAuftrag.getKundeIIdAuftragsadresse());
			kunde.setBIstinteressent(Helper.boolean2Short(false));
			em.merge(kunde);
			em.flush();
		}
	};

	@Override
	public Timestamp berechneBeleg(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Auftrag oAuftrag = em.find(Auftrag.class, iIdAuftragI);
		myLogger.entry();

		if (oAuftrag.getAuftragstatusCNr().equals(
				AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {

			BigDecimal bdAuftragswertInAuftragswaehrung = berechneNettowertGesamt(
					oAuftrag.getIId(), theClientDto); // Berechnung in
			// Auftragswaehrung

			if (oAuftrag.getNKorrekturbetrag() != null) {
				bdAuftragswertInAuftragswaehrung = bdAuftragswertInAuftragswaehrung
						.add(oAuftrag.getNKorrekturbetrag());
			}

			BigDecimal bdWechselkurs = Helper.getKehrwert(new BigDecimal(
					oAuftrag.getFWechselkursmandantwaehrungzuauftragswaehrung()
							.doubleValue()));
			BigDecimal bdAuftragswertInMandantenwaehrung = bdAuftragswertInAuftragswaehrung
					.multiply(bdWechselkurs);
			bdAuftragswertInMandantenwaehrung = Helper.rundeKaufmaennisch(
					bdAuftragswertInMandantenwaehrung, 4);
			checkNumberFormat(bdAuftragswertInMandantenwaehrung);

			BigDecimal bdMaterialwertInMandantenwaehrung = berechneMaterialwertGesamt(
					oAuftrag.getIId(), theClientDto); // in
			// Mandantenwaehrung

			oAuftrag.setNGesamtauftragswertinauftragswaehrung(bdAuftragswertInAuftragswaehrung);
			oAuftrag.setNMaterialwertinmandantenwaehrung(bdMaterialwertInMandantenwaehrung);

			BigDecimal bdRohdeckungInMandantenwaehrung = bdAuftragswertInMandantenwaehrung
					.subtract(bdMaterialwertInMandantenwaehrung);

			bdRohdeckungInMandantenwaehrung = Helper.rundeKaufmaennisch(
					bdRohdeckungInMandantenwaehrung, 4);
			checkNumberFormat(bdRohdeckungInMandantenwaehrung);
			if (oAuftrag.getNRohdeckungaltinmandantenwaehrung() == null) {
				oAuftrag.setNRohdeckunginmandantenwaehrung(bdRohdeckungInMandantenwaehrung);
				oAuftrag.setNRohdeckungaltinmandantenwaehrung(bdRohdeckungInMandantenwaehrung);
			}
			em.merge(oAuftrag);
			em.flush();

		}
		return getTimestamp();
	}

	public boolean checkPositionFormat(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		int countBeginn = 0;
		int countEnde = 0;
		boolean ok = true;
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRAuftragposition.class);
			crit.add(Restrictions.eq("flrauftrag.i_id", iIdAuftragI));
			List<?> l = crit.list();
			Iterator<?> iter = l.iterator();
			while (iter.hasNext()) {
				FLRAuftragposition pos = (FLRAuftragposition) iter.next();
				if (pos.getPositionart_c_nr().equals(
						LocaleFac.POSITIONSART_POSITION)) {
					if (pos.getC_zbez().equals(LocaleFac.POSITIONBEZ_BEGINN))
						countBeginn++;
					if (pos.getC_zbez().equals(LocaleFac.POSITIONBEZ_ENDE))
						countEnde++;
				}
			}
			if (countBeginn != countEnde)
				ok = false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return ok;
	}

	/**
	 * Die vollstaendig Artikelbezeichnung einer Auftragposition zum Andrucken
	 * zusammenbauen.
	 *
	 * @param oAuftragpositionDtoI
	 *            die Auftragposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die vallstaendig Artikelbezeichnung
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private String baueArtikelBezeichnung(
			AuftragpositionDto oAuftragpositionDtoI, TheClientDto theClientDto)
			throws Throwable {
		ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(
				oAuftragpositionDtoI.getArtikelIId(), theClientDto);

		StringBuffer sbBezeichnung = new StringBuffer();

		if (oAuftragpositionDtoI.isIdent()) {
			sbBezeichnung.append(oArtikelDto.getCNr());

			if (oAuftragpositionDtoI.getCBez() != null) {
				sbBezeichnung.append(oAuftragpositionDtoI.getCBez());
			} else {
				if (oArtikelDto.getArtikelsprDto() != null) {
					if (oArtikelDto.getArtikelsprDto().getCBez() != null) {
						sbBezeichnung.append("\n").append(
								oArtikelDto.getArtikelsprDto().getCBez());
					}
				}
			}

			if (oArtikelDto.getArtikelsprDto() != null) {
				if (oArtikelDto.getArtikelsprDto().getCZbez() != null) {
					sbBezeichnung.append("\n").append(
							oArtikelDto.getArtikelsprDto().getCZbez());
				}
			}
		} else if (oAuftragpositionDtoI.isHandeingabe()) {
			sbBezeichnung.append(oArtikelDto.getArtikelsprDto().getCBez());

			if (oArtikelDto.getArtikelsprDto().getCZbez() != null) {
				sbBezeichnung.append("\n").append(
						oArtikelDto.getArtikelsprDto().getCZbez());
			}
		}

		return sbBezeichnung.toString();
	}

	/**
	 * Methode zum Erzeugen eines neues Auftrags als Kopie eines bestehenden
	 * Auftrags. <br>
	 * Es werden auch die Positionen kopiert.
	 *
	 * @param iIdAuftragI
	 *            PK des bestehenden Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK des neuen Auftrags
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer erzeugeAuftragAusAuftrag(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		AuftragDto auftragBasisDto = getAuftragFac().auftragFindByPrimaryKey(
				iIdAuftragI);

		Integer iIdAuftragKopie = null;

		try {
			GregorianCalendar calendar = new GregorianCalendar();
			AuftragDto auftragDto = (AuftragDto) auftragBasisDto.clone();

			auftragDto
					.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
							getLocaleFac().getWechselkurs2(
									theClientDto.getSMandantenwaehrung(),
									auftragDto.getCAuftragswaehrung(),
									theClientDto).doubleValue()));

			ParametermandantDto parametermandantLieferzeitDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG);
			int defaultLieferzeitAuftrag = ((Integer) parametermandantLieferzeitDto
					.getCWertAsObject()).intValue();

			calendar = new GregorianCalendar();
			calendar.add(Calendar.DATE, defaultLieferzeitAuftrag);
			Timestamp aktuelleDatumPlusLieferZeit = new Timestamp(
					calendar.getTimeInMillis());

			ParametermandantDto parametermandantLieferdatumDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_AUFTRAG_KOPIEREN_LIEFERDATUM_UEBERNEHMEN);
			if ((Boolean) parametermandantLieferdatumDto.getCWertAsObject()) {
				auftragDto.setDLiefertermin(auftragBasisDto.getDLiefertermin());
				auftragDto.setDFinaltermin(auftragBasisDto.getDLiefertermin());
			} else {
				auftragDto.setDLiefertermin(aktuelleDatumPlusLieferZeit);
				auftragDto.setDFinaltermin(aktuelleDatumPlusLieferZeit);
			}
			auftragDto.setDBestelldatum(new java.sql.Timestamp(System
					.currentTimeMillis()));

			iIdAuftragKopie = createAuftrag(auftragDto, theClientDto);

			// PJ 15507 Auftragdokumente kopieren
			AuftragauftragdokumentDto[] auftragauftragdokumentDtos = getAuftragServiceFac()
					.auftragauftragdokumentFindByAuftragIId(iIdAuftragI);
			if (auftragauftragdokumentDtos != null
					&& auftragauftragdokumentDtos.length > 0) {
				ArrayList<AuftragdokumentDto> al = new ArrayList<AuftragdokumentDto>();

				for (int i = 0; i < auftragauftragdokumentDtos.length; i++) {
					AuftragdokumentDto dto = new AuftragdokumentDto();
					dto.setIId(auftragauftragdokumentDtos[i]
							.getAuftragdokumentIId());

					al.add(dto);
				}

				getAuftragServiceFac().updateAuftragdokumente(iIdAuftragKopie,
						al);

			}

			// alle Positionen kopieren
			AuftragpositionDto[] aAuftragpositionBasis = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);
			Integer positionIId = null;
			Integer positionIIdSet = null;
			for (int i = 0; i < aAuftragpositionBasis.length; i++) {
				AuftragpositionDto auftragpositionDto = (AuftragpositionDto) aAuftragpositionBasis[i]
						.clone();

				if (auftragBasisDto.getAuftragartCNr().equals(
						AuftragServiceFac.AUFTRAGART_ABRUF)) {
					auftragpositionDto
							.setAuftragpositionIIdRahmenposition(aAuftragpositionBasis[i]
									.getAuftragpositionIIdRahmenposition());
				}

				// Wenn sich sie MWST seither geaendert hat
				if (auftragpositionDto.getMwstsatzIId() != null) {
					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindByPrimaryKey(
									auftragpositionDto.getMwstsatzIId(),
									theClientDto);

					mwstsatzDto = getMandantFac()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									mwstsatzDto.getIIMwstsatzbezId(),
									theClientDto);
					BigDecimal mwstBetrag = Helper.getProzentWert(
							auftragpositionDto.getNNettoeinzelpreis(),
							new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
					auftragpositionDto.setMwstsatzIId(mwstsatzDto.getIId());
					auftragpositionDto.setNMwstbetrag(mwstBetrag);
					auftragpositionDto.setNBruttoeinzelpreis(auftragpositionDto
							.getNNettoeinzelpreis().add(mwstBetrag));
				}

				if (aAuftragpositionBasis[i].getPositioniIdArtikelset() != null) {
					auftragpositionDto.setPositioniIdArtikelset(positionIIdSet);
				}
				auftragpositionDto.setBelegIId(iIdAuftragKopie);
				if ((Boolean) parametermandantLieferdatumDto.getCWertAsObject()) {
					auftragpositionDto
							.setTUebersteuerbarerLiefertermin(auftragBasisDto
									.getDLiefertermin());
				} else {
					auftragpositionDto
							.setTUebersteuerbarerLiefertermin(aktuelleDatumPlusLieferZeit);
				}
				if (auftragpositionDto.getTypCNr() != null) {

					if (auftragpositionDto.isPosition()) {
						if (auftragpositionDto.getCZusatzbez().equals(
								LocaleFac.POSITIONBEZ_BEGINN)) {
							positionIId = getAuftragpositionFac()
									.createAuftragposition(auftragpositionDto,
											false, theClientDto);
						} else if (auftragpositionDto.getCZusatzbez().equals(
								LocaleFac.POSITIONBEZ_ENDE)) {
							getAuftragpositionFac().createAuftragposition(
									auftragpositionDto, false, theClientDto);
						}
					} else {
						auftragpositionDto.setPositioniId(positionIId);
						getAuftragpositionFac().createAuftragposition(
								auftragpositionDto, false, theClientDto);
					}
				} else {
					if (aAuftragpositionBasis[i].isIntelligenteZwischensumme()) {
						ZwsPositionMapper mapper = new ZwsPositionMapper(
								getAuftragpositionFac(),
								getAuftragpositionFac());
						mapper.map(aAuftragpositionBasis[i],
								auftragpositionDto, iIdAuftragKopie);
						//
						// auftragpositionDto.setBZwsPositionspreisDrucken(
						// aAuftragpositionBasis[i].getBZwsPositionspreisZeigen());
						// Integer von = getAuftragpositionFac()
						// .getPositionNummer(
						// aAuftragpositionBasis[i]
						// .getZwsVonPosition());
						// auftragpositionDto
						// .setZwsVonPosition(getAuftragpositionFac()
						// .getPositionIIdFromPositionNummer(
						// iIdAuftragKopie, von));
						// Integer bis = getAuftragpositionFac()
						// .getPositionNummer(
						// aAuftragpositionBasis[i]
						// .getZwsBisPosition());
						// auftragpositionDto
						// .setZwsBisPosition(getAuftragpositionFac()
						// .getPositionIIdFromPositionNummer(
						// iIdAuftragKopie, bis));
					}

					if (auftragpositionDto.getPositioniIdArtikelset() == null) {
						positionIIdSet = getAuftragpositionFac()
								.createAuftragposition(auftragpositionDto,
										false, theClientDto);
					} else {
						getAuftragpositionFac().createAuftragposition(
								auftragpositionDto, false, theClientDto);
					}
				}
			}
			// kopieren der Auftrageigenschaften
			PaneldatenDto[] aPaneldatenDtoBasis = getPanelFac()
					.paneldatenFindByPanelCNrCKey(
							PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN,
							iIdAuftragI.toString());

			PaneldatenDto[] datenKorrigiert = new PaneldatenDto[aPaneldatenDtoBasis.length];

			for (int y = 0; y < aPaneldatenDtoBasis.length; y++) {
				PaneldatenDto paneldatenDto = (PaneldatenDto) aPaneldatenDtoBasis[y]
						.clone();
				paneldatenDto.setCKey(iIdAuftragKopie.toString());
				datenKorrigiert[y] = paneldatenDto;

			}

			if (datenKorrigiert != null) {
				getPanelFac().createPaneldaten(datenKorrigiert);
			}

			myLogger.exit("Der Auftrag wurde mit "
					+ aAuftragpositionBasis.length + " Positionen erzeugt.");
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return iIdAuftragKopie;
	}

	public Integer erzeugeNegativeMengeAusAuftrag(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Auftrag auftrag = em.find(Auftrag.class, iIdAuftragI);
			try {
				Query query = em
						.createNamedQuery("AuftragpositionfindByAuftragNegativeMenge");
				query.setParameter(1, iIdAuftragI);
				Collection<?> c = query.getResultList();
				for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
					Auftragposition pos = ((Auftragposition) iter.next());

					// vorher noch die Set-Positionen entfernen
					Query querySet = em
							.createNamedQuery("AuftragpositionfindByPositionIIdArtikelsetAll");
					querySet.setParameter(1, pos.getIId());
					Collection<?> cl = querySet.getResultList();
					Iterator it = cl.iterator();
					while (it.hasNext()) {
						Auftragposition posSet = (Auftragposition) it.next();

						em.remove(posSet);
						em.flush();

					}

					em.remove(pos);
					em.flush();

				}
			} catch (NoResultException ex) {

			}
			// alle Positionen kopieren
			AuftragpositionDto[] aAuftragpositionBasis = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);
			for (int i = 0; i < aAuftragpositionBasis.length; i++) {
				AuftragpositionDto auftragpositionDto = (AuftragpositionDto) aAuftragpositionBasis[i]
						.clone();
				auftragpositionDto.setBelegIId(iIdAuftragI);
				if (auftragpositionDto.getNMenge() != null) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									auftragpositionDto.getArtikelIId(),
									theClientDto);
					if (artikelDto.getArtgruIId() != null) {
						ArtgruDto artgruDto = getArtikelFac()
								.artgruFindByPrimaryKey(
										artikelDto.getArtgruIId(), theClientDto);
						if (Helper.short2boolean(artgruDto.getBRueckgabe())) {
							auftragpositionDto.setNMenge(auftragpositionDto
									.getNMenge().negate());
							auftragpositionDto
									.setNOffeneMenge(auftragpositionDto
											.getNMenge());
							auftragpositionDto
									.setTUebersteuerbarerLiefertermin(auftrag
											.getTFinaltermin());

							auftragpositionDto
									.setNBruttoeinzelpreis(new BigDecimal(0));
							auftragpositionDto
									.setNNettoeinzelpreis(new BigDecimal(0));

							auftragpositionDto
									.setNNettoeinzelpreisplusversteckteraufschlag(new BigDecimal(
											0));
							auftragpositionDto
									.setNEinzelpreis(new BigDecimal(0));
							auftragpositionDto
									.setNEinzelpreisplusversteckteraufschlag(new BigDecimal(
											0));
							auftragpositionDto.setVerleihIId(null);

							auftragpositionDto
									.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(new BigDecimal(
											0));

							auftragpositionDto.setISort(null);
							getAuftragpositionFac().createAuftragposition(
									auftragpositionDto, theClientDto);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdAuftragI;
	}

	public Integer vorhandenenLieferscheinEinesAuftagsHolenBzwNeuAnlegen(
			Integer auftragIId, TheClientDto theClientDto) {
		// PJ18738
		// Nachsehen, ob es zu dem Auftrag bereits einen LS gibt, der 'Angelegt'
		// ist

		Session session3 = FLRSessionFactory.getFactory().openSession();
		String sQuery3 = "FROM FLRLieferschein flrLieferschein WHERE flrLieferschein.auftrag_i_id="
				+ auftragIId
				+ " AND flrLieferschein.lieferscheinstatus_status_c_nr='"
				+ LocaleFac.STATUS_ANGELEGT
				+ "' ORDER BY flrLieferschein.c_nr ASC";

		org.hibernate.Query query3 = session3.createQuery(sQuery3);
		query3.setMaxResults(1);

		List<?> results3 = query3.list();
		Iterator<?> resultListIterator3 = results3.iterator();
		if (resultListIterator3.hasNext()) {
			// wenn ja, dann den zurueckliefern
			FLRLieferschein ls = (FLRLieferschein) resultListIterator3.next();
			return ls.getI_id();
		} else {
			// wenn nein, dann einen LS aus dem Auftrag anlegen
			return erzeugeLieferscheinAusAuftrag(auftragIId, null, null,
					theClientDto);
		}
	}

	/**
	 * Methode zum Erzeugen eines eines Lieferscheins aus einem bestehenden
	 * Auftrag. <br>
	 * Nicht mengenbehaftete Positionen werden ebebfalls kopiert,
	 * mengenbehaftete Positionen muessen vom Benutzer gezielt uebernommen
	 * werden.
	 *
	 * @param iIdAuftragI
	 *            PK des bestehenden Auftrags
	 * @param lieferscheinDtoI
	 *            der Benutzer kann bestimmte Eigenschaften des Auftrags
	 *            uebersteuern
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK des neuen Lieferscheins
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer erzeugeLieferscheinAusAuftrag(Integer iIdAuftragI,
			LieferscheinDto lieferscheinDtoI,
			Double dRabattAusRechnungsadresse, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		AuftragDto auftragBasisDto = getAuftragFac().auftragFindByPrimaryKey(
				iIdAuftragI);

		Integer iIdLieferschein = null;

		try {
			LieferscheinDto lieferscheinDto = (LieferscheinDto) auftragBasisDto
					.cloneAsLieferscheinDto();

			// PJ18344

			ParametermandantDto parameterDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_LIEFERSCHEIN,
							ParameterFac.PARAMETER_LS_DEFAULT_VERRECHENBAR);

			lieferscheinDto.setBVerrechenbar(Helper
					.boolean2Short((Boolean) parameterDto.getCWertAsObject()));

			if (dRabattAusRechnungsadresse != null) {
				lieferscheinDto
						.setFAllgemeinerRabattsatz(dRabattAusRechnungsadresse);
			}

			lieferscheinDto
					.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
							getLocaleFac().getWechselkurs2(
									theClientDto.getSMandantenwaehrung(),
									lieferscheinDto.getWaehrungCNr(),
									theClientDto).doubleValue()));

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					lieferscheinDto.getKundeIIdLieferadresse(), theClientDto);
			lieferscheinDto.setLagerIId(auftragBasisDto
					.getLagerIIdAbbuchungslager());
			lieferscheinDto.setZiellagerIId(kundeDto.getPartnerDto()
					.getLagerIIdZiellager());

			/**
			 * Auskommentiert von CK am 22.10.2008, wegen Projekt 08/13491
			 * lieferscheinDto
			 * .setPersonalIIdVertreter(theClientDto.getIDPersonal());
			 */

			// der Benutzer kann bestimmte vorbelegte Eigenschaften uebersteuern
			if (lieferscheinDtoI != null) {
				lieferscheinDto.setTBelegdatum(lieferscheinDtoI
						.getTBelegdatum());
				lieferscheinDto.setAnsprechpartnerIId(lieferscheinDtoI
						.getAnsprechpartnerIId());
				lieferscheinDto.setPersonalIIdVertreter(lieferscheinDtoI
						.getPersonalIIdVertreter());
				lieferscheinDto.setKundeIIdRechnungsadresse(lieferscheinDtoI
						.getKundeIIdRechnungsadresse());
				lieferscheinDto.setWaehrungCNr(lieferscheinDtoI
						.getWaehrungCNr());
				lieferscheinDto
						.setFWechselkursmandantwaehrungzubelegwaehrung(lieferscheinDtoI
								.getFWechselkursmandantwaehrungzubelegwaehrung());
				lieferscheinDto.setLagerIId(lieferscheinDtoI.getLagerIId());
			}

			// rueckgabedatum berechenen fuer leihtage Lieferschein
			if (auftragBasisDto.getILeihtage().intValue() != 0) {
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.add(Calendar.DATE, auftragBasisDto.getILeihtage()
						.intValue());
				Timestamp rueckgabeterminLieferschein = new Timestamp(
						calendar.getTimeInMillis());
				lieferscheinDto
						.setTRueckgabetermin(rueckgabeterminLieferschein);
			}
			lieferscheinDto.setAuftragIId(iIdAuftragI);
			iIdLieferschein = getLieferscheinFac().createLieferschein(
					lieferscheinDto, theClientDto);

			// alle nicht mengenbehafteten Positionen mituebernehmen
			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);

			if (aAuftragpositionDto != null && aAuftragpositionDto.length > 0) {
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					// Kalkulatorische Artikel sofort Erledigen, damit diese im
					// LS nicht aufscheinen
					if (aAuftragpositionDto[i].getArtikelIId() != null) {
						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										aAuftragpositionDto[i].getArtikelIId(),
										theClientDto);
						if (Helper.short2boolean(aDto.getBKalkulatorisch())) {

							aAuftragpositionDto[i]
									.setAuftragpositionstatusCNr(LocaleFac.STATUS_ERLEDIGT);
							getAuftragpositionFac()
									.updateAuftragpositionOhneWeitereAktion(
											aAuftragpositionDto[i],
											theClientDto);

						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdLieferschein;
	}

	public AuftragDto[] auftragFindByAuftragstatusCNr(String cNrAuftragstatusI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (cNrAuftragstatusI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAuftragstatusI == null"));
		}
		myLogger.logData(cNrAuftragstatusI);
		AuftragDto[] aAuftragDto = null;
		Query query = em.createNamedQuery("AuftragfindByMandantAndStatus");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, cNrAuftragstatusI);
		Collection<?> cl = query.getResultList();
		aAuftragDto = assembleAuftragDtos(cl);
		myLogger.exit("Anzahl: " + aAuftragDto.length);
		return aAuftragDto;
	}

	/**
	 * Alle Auftraege holen, die zu einem bestimmten Angebot erfasst wurden.
	 *
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return AuftragDto[] die Auftraege
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public AuftragDto[] auftragFindByAngebotIId(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}
		AuftragDto[] aAuftragDto = null;
		Query query = em.createNamedQuery("AuftragfindByAngebotIId");
		query.setParameter(1, iIdAngebotI);
		Collection<?> cl = query.getResultList();
		aAuftragDto = assembleAuftragDtos(cl);
		return aAuftragDto;
	}

	public AuftragDto[] auftragFindByKundeIIdAuftragsadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdKundeI == null"));
		}
		AuftragDto[] aAuftragDtos = null;
		Query query = em
				.createNamedQuery("AuftragfindByKundeIIdAuftragsadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAuftragDtos = assembleAuftragDtos(cl);
		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByKundeIIdAuftragsadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException {
		AuftragDto[] aAuftragDtos = null;
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdKundeI == null"));
		}
		try {
			Query query = em
					.createNamedQuery("AuftragfindByKundeIIdAuftragsadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAuftragDtos = assembleAuftragDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI"
					+ cNrMandantI, t);
		}

		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByKundeIIdLieferadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdKundeI == null"));
		}
		AuftragDto[] aAuftragDtos = null;
		Query query = em
				.createNamedQuery("AuftragfindByKundeIIdLieferadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAuftragDtos = assembleAuftragDtos(cl);
		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByKundeIIdLieferadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException {

		AuftragDto[] aAuftragDtos = null;

		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdKundeI == null"));
		}

		try {
			Query query = em
					.createNamedQuery("AuftragfindByKundeIIdLieferadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAuftragDtos = assembleAuftragDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI"
					+ cNrMandantI, t);
		}

		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByKundeIIdRechnungsadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdKundeI == null"));
		}
		AuftragDto[] aAuftragDtos = null;
		Query query = em
				.createNamedQuery("AuftragfindByKundeIIdRechnungsadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAuftragDtos = assembleAuftragDtos(cl);
		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByKundeIIdRechnungsadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException {
		AuftragDto[] aAuftragDtos = null;

		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdKundeI == null"));
		}

		try {
			Query query = em
					.createNamedQuery("AuftragfindByKundeIIdRechnungsadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAuftragDtos = assembleAuftragDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI"
					+ cNrMandantI, t);
		}

		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByAnsprechpartnerIIdMandantCNr(
			Integer iIdAnsprechpartnerI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAnsprechpartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAnsprechpartnerI == null"));
		}
		AuftragDto[] aAuftragDtos = null;
		Query query = em
				.createNamedQuery("AuftragfindByAnsprechpartnerIIdMandantCNr");
		query.setParameter(1, iIdAnsprechpartnerI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAuftragDtos = assembleAuftragDtos(cl);
		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByAnsprechpartnerIIdMandantCNrOhneExc(
			Integer iIdAnsprechpartnerI, String cNrMandantI,
			TheClientDto theClientDto) {
		if (iIdAnsprechpartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAnsprechpartnerI == null"));
		}
		AuftragDto[] aAuftragDtos = null;
		try {
			Query query = em
					.createNamedQuery("AuftragfindByAnsprechpartnerIIdMandantCNr");
			query.setParameter(1, iIdAnsprechpartnerI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAuftragDtos = assembleAuftragDtos(cl);
		} catch (Throwable th) {
			return null;
		}
		return aAuftragDtos;
	}

	/**
	 * Alle Auftraege eines Mandanten und einer Auftrags-Art holen holen.
	 *
	 * @param mandantCNrI
	 *            String
	 * @param auftragartCNrI
	 *            String
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return AuftragDto[] die Auftraege
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public AuftragDto[] auftragFindByMandantCNrAuftragartCNr(
			String mandantCNrI, String auftragartCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (mandantCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("mandantCNrI == null"));
		}
		if (auftragartCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("auftragartCNrI == null"));
		}
		AuftragDto[] aAuftragDto = null;
		Query query = em
				.createNamedQuery("AuftragfindByMandantCNrAuftragartCNr");
		query.setParameter(1, mandantCNrI);
		query.setParameter(2, auftragartCNrI);
		Collection<?> cl = query.getResultList();
		aAuftragDto = assembleAuftragDtos(cl);
		return aAuftragDto;
	}

	/**
	 * Alle Auftraege eines Mandanten und einer Auftrags-Art holen holen.
	 *
	 * @param mandantCNrI
	 *            String
	 * @param auftragartCNrI
	 *            String
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return AuftragDto[] die Auftraege
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public AuftragDto[] auftragFindByMandantCNrAuftragartCNrStatusCNr(
			String mandantCNrI, String auftragartCNrI, String statusCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (mandantCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("mandantCNrI == null"));
		}
		if (auftragartCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("auftragartCNrI == null"));
		}
		AuftragDto[] aAuftragDto = null;
		Query query = em
				.createNamedQuery("AuftragfindByMandantCNrAuftragartCNrStatusCNr");
		query.setParameter(1, mandantCNrI);
		query.setParameter(2, auftragartCNrI);
		query.setParameter(3, AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
		query.setParameter(4, AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
		Collection<?> cl = query.getResultList();
		aAuftragDto = assembleAuftragDtos(cl);
		return aAuftragDto;
	}

	/**
	 * Liefert alle Abrufeauftraege zu einem bestimmten Rahmenauftrag.
	 *
	 * @param iIdRahmenauftragI
	 *            PK des Rahmenauftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return AuftragDto[] die Abrufauftraege
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public AuftragDto[] abrufauftragFindByAuftragIIdRahmenauftrag(
			Integer iIdRahmenauftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdRahmenauftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdRahmenauftragI == null"));
		}
		myLogger.logData(iIdRahmenauftragI);
		AuftragDto[] aAuftragDto = null;
		Query query = em
				.createNamedQuery("AuftragfindByAuftragIIdRahmenauftrag");
		query.setParameter(1, iIdRahmenauftragI);
		Collection<?> cl = query.getResultList();
		aAuftragDto = assembleAuftragDtos(cl);
		return aAuftragDto;
	}

	public AuftragDto[] auftragFindByMandantCnrKundeIIdBestellnummerOhneExc(
			Integer iIdKundeI, String cNrMandantI, String cBestellnummerI,
			TheClientDto theClientDto) throws RemoteException {
		AuftragDto[] aAuftragDtos = null;
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdKundeI == null"));
		}
		try {
			Query query = em
					.createNamedQuery("AuftragfindByMandantCNrKundeIIdCBestellnummer");
			query.setParameter(1, cNrMandantI);
			query.setParameter(2, iIdKundeI);
			query.setParameter(3, cBestellnummerI.replace(" ", ""));
			Collection<?> cl = query.getResultList();
			aAuftragDtos = assembleAuftragDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI"
					+ cNrMandantI + " cBestellnummer" + cBestellnummerI, t);
		}

		return aAuftragDtos;
	}

	/**
	 * Den Auftrag mit Daten aktualisieren. Der Status bleibt dabei
	 * unveraendert.
	 *
	 * @param auftragDtoI
	 *            der Auftrag
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAuftragOhneWeitereAktion(AuftragDto auftragDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragDto(auftragDtoI);
		Auftrag auftrag = em.find(Auftrag.class, auftragDtoI.getIId());
		if (auftrag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setAuftragFromAuftragDto(auftrag, auftragDtoI);
		auftrag.setTAendern(getTimestamp());
		auftrag.setPersonalIIdAendern(theClientDto.getIDPersonal());
	}

	public boolean darfWiederholungsTerminAendern(Integer auftragIId,
			TheClientDto theClientDto) {
		Query query = em
				.createNamedQuery("RechnungfindByAuftragIIdNotInStatusCNr");
		query.setParameter(1, auftragIId);
		query.setParameter(2, RechnungFac.STATUS_STORNIERT);
		Collection<?> cl = query.getResultList();
		if (cl.size() != 0)
			return false;
		return true;
	}

	@Override
	public void setzeVersandzeitpunktAufJetzt(Integer auftragIId,
			String druckart) {
		if (auftragIId != null) {
			Auftrag auftrag = em.find(Auftrag.class, auftragIId);
			auftrag.setTVersandzeitpunkt(new Timestamp(System
					.currentTimeMillis()));
			auftrag.setCVersandtype(druckart);
			em.merge(auftrag);
			em.flush();
		}

	}

	public java.sql.Date getWiederholungsTermin(Integer auftragIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return getRechnungFac()
				.getWiederholungsTermin(auftragIId, theClientDto);

	}

	@Override
	public BigDecimal berechneBestellwertAuftrag(Integer iIdAuftrag)
			throws EJBExceptionLP, RemoteException {

		BigDecimal bestellwert = new BigDecimal(0);
		BestellungDto[] besDtos = getBestellungFac()
				.bestellungFindByAuftragIId(iIdAuftrag);

		if (besDtos == null)
			return bestellwert;

		for (BestellungDto bestellungDto : besDtos) {
			if (bestellungDto.getNBestellwert() != null)
				bestellwert = bestellwert.add(bestellungDto.getNBestellwert());
		}

		return bestellwert;
	}

	@Override
	public BigDecimal berechneSummeSplittbetragAuftrag(Integer iIdAuftrag)
			throws EJBExceptionLP, RemoteException {
		EingangsrechnungAuftragszuordnungDto[] azDtos = getEingangsrechnungFac()
				.eingangsrechnungAuftragszuordnungFindByAuftragIId(iIdAuftrag);

		BigDecimal summe = new BigDecimal(0);

		if (azDtos == null)
			return summe;

		for (EingangsrechnungAuftragszuordnungDto az : azDtos) {
			if (az.getNBetrag() != null)
				summe = summe.add(az.getNBetrag());
		}

		return summe;
	}

	@Override
	public IOrderResponse createOrderResponse(AuftragDto auftragDto,
			TheClientDto theClientDto) throws NamingException, RemoteException {
		Validator.notNull(auftragDto, "auftragDto");
		Validator.notEmpty(auftragDto.getMandantCNr(), "auftragDto.mandant");
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notEmpty(theClientDto.getMandant(), "theClient.mandant");
		if (!auftragDto.getMandantCNr().equals(theClientDto.getMandant())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FALSCHER_MANDANT,
					auftragDto.getIId().toString());
		}

		if (!AuftragServiceFac.AUFTRAGSTATUS_OFFEN.equals(auftragDto
				.getStatusCNr())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(auftragDto.getStatusCNr()));
		}

		IOrderResponseProducer responseProducer = responseFactory.getProducer(
				auftragDto, theClientDto);
		if (null == responseProducer) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_NICHT_UNTERSTUETZT,
					"");
		}

		IOrderResponse orderResponse = responseProducer.createResponse(
				auftragDto, theClientDto);
		// if(orderResponse != null) {
		// Auftrag auftrag = em.find(Auftrag.class, auftragDto.getIId()) ;
		// auftrag.setTResponse(new Timestamp(System.currentTimeMillis()));
		// auftrag.setPersonalIIdResponse(theClientDto.getIDPersonal()) ;
		// em.merge(auftrag) ;
		// em.flush() ;
		// }

		return orderResponse;
	}

	private void updateResponseTimestamp(Integer auftragId,
			TheClientDto theClientDto) {
		Auftrag auftrag = em.find(Auftrag.class, auftragId);
		auftrag.setTResponse(new Timestamp(System.currentTimeMillis()));
		auftrag.setPersonalIIdResponse(theClientDto.getIDPersonal());
		em.merge(auftrag);
		em.flush();
	}

	@Override
	public String createOrderResponsePost(AuftragDto auftragDto,
			TheClientDto theClientDto) throws RemoteException, NamingException,
			EJBExceptionLP {
		IOrderResponse response = createOrderResponse(auftragDto, theClientDto);
		if (response == null)
			return null;

		IOrderResponseProducer producer = responseFactory.getProducer(
				auftragDto, theClientDto);
		producer.postResponse(response, auftragDto, theClientDto);

		// Besser Zeitstempel ist gesetzt, und dafuer ev. nicht archiviert
		updateResponseTimestamp(auftragDto.getIId(), theClientDto);

		String xmlContent = orderresponseToStringImpl(response, auftragDto,
				theClientDto);
		archiveResponseDocument(auftragDto, xmlContent, theClientDto);

		return xmlContent;
	}

	@Override
	public String createOrderResponseToString(AuftragDto auftragDto,
			TheClientDto theClientDto) throws RemoteException, NamingException {
		IOrderResponse response = createOrderResponse(auftragDto, theClientDto);
		return response == null ? null : orderresponseToStringImpl(response,
				auftragDto, theClientDto);
	}

	private String orderresponseToStringImpl(IOrderResponse response,
			AuftragDto auftragDto, TheClientDto theClientDto)
			throws RemoteException, NamingException {
		IOrderResponseProducer producer = responseFactory.getProducer(
				auftragDto, theClientDto);
		return producer.toString(response);
	}

	private void archiveResponseDocument(AuftragDto auftragDto,
			String xmlContent, TheClientDto theClientDto) {
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
				theClientDto.getIDPersonal(), theClientDto);

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocPath dp = new DocPath(new DocNodeAuftrag(auftragDto))
				.add(new DocNodeFile(
						"Auftragbest\u00e4tigung_osa_Clevercure.xml"));
		jcrDocDto.setDocPath(dp);
		jcrDocDto.setbData(xmlContent.getBytes());
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(partnerDto.getIId());

		Integer kundeId = auftragDto.getKundeIIdLieferadresse();
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId,
				theClientDto);

		jcrDocDto.setlPartner(kundeDto.getPartnerIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(auftragDto.getCNr().replace("/", "."));
		jcrDocDto.setsFilename(auftragDto.getCNr().replace("/", "."));
		jcrDocDto.setsMIME(".xml");
		jcrDocDto.setsName("Orderresponse Clevercure" + auftragDto.getIId());
		jcrDocDto.setsRow(auftragDto.getIId().toString());
		jcrDocDto.setsTable("AUFTRAG");
		String sSchlagworte = "Export Clevercure XML Orderresponse osd";
		jcrDocDto.setsSchlagworte(sSchlagworte);
		getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(
				jcrDocDto, theClientDto);
	}

	private Integer getVersandwegIdFor(AuftragDto auftragDto,
			TheClientDto theClientDto) throws RemoteException {
		return getVersandwegIdFor(auftragDto.getKundeIIdLieferadresse(),
				theClientDto);
	}

	private Integer getVersandwegIdFor(Integer kundeId,
			TheClientDto theClientDto) throws RemoteException {
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId,
				theClientDto);

		Integer versandwegId = kundeDto.getPartnerDto().getVersandwegIId();
		return versandwegId;
	}

	private OrderresponseFactory responseFactory = new OrderresponseFactory();

	private class OrderresponseFactory {
		private IOrderResponseProducer getProducerCC() {
			// return new OrderResponseProducerCCSelfSender() ;
			return new OrderResponseProducerCC();
		}

		public IOrderResponseProducer getProducer(AuftragDto auftragDto,
				TheClientDto theClientDto) throws RemoteException,
				NamingException {
			Integer versandwegId = getVersandwegIdFor(auftragDto, theClientDto);
			if (null == versandwegId)
				return new OrderResponseProducerDummy();

			Versandweg versandweg = em.find(Versandweg.class, versandwegId);
			if (null == versandweg)
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						versandwegId.toString());

			if (SystemFac.VersandwegType.CleverCureVerkauf.equals(versandweg
					.getCnr().trim()))
				return getProducerCC();
			return null;
		}
	}

	public class OrderResponseProducerCCSelfSender extends
			OrderResponseProducerCC {
		@Override
		protected String getCCEndpunkt(EntityManager em,
				VersandwegCCPartnerDto ccPartnerDto) {
			return "http://localhost:8280/restapi/services/rest/api/beta/cc?fake=yes";
		}
	}

	public class OrderResponseProducerDummy implements IOrderResponseProducer {
		@Override
		public boolean isDummy() {
			return true;
		}

		@Override
		public IOrderResponse createResponse(AuftragDto auftragDto,
				TheClientDto theClientDto) throws RemoteException {
			return null;
		}

		@Override
		public void postResponse(IOrderResponse response,
				AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
		}

		@Override
		public String toString(IOrderResponse orderResponse) {
			return null;
		}
	}

	public class OrderResponseProducerCC extends CleverCureProducer implements
			IOrderResponseProducer {
		public final static String GENERATOR_INFO = "2.4";

		@Override
		public boolean isDummy() {
			return false;
		}

		public IOrderResponse createResponse(AuftragDto auftragDto,
				TheClientDto theClientDto) throws NamingException,
				RemoteException {
			OrderResponseCC orderResponse = new OrderResponseCC();
			XMLXMLORDERRESPONSE or = createOr(orderResponse, auftragDto,
					theClientDto);
			orderResponse.setResponse(or);
			return orderResponse;
		}

		@Override
		public void postResponse(IOrderResponse response,
				AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			if (!(response instanceof OrderResponseCC))
				return;

			try {
				HttpURLConnection urlConnection = buildUrlconnectionPost(response);
				urlConnection.setRequestProperty("Content-Type", "text/xml");
				OutputStreamWriter out = new OutputStreamWriter(
						urlConnection.getOutputStream());
				String xmlDndContent = toString(response);
				out.write(xmlDndContent);
				out.flush();
				out.close();

				int lastResponseCode = urlConnection.getResponseCode();

				InputStream s = urlConnection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(s));
				String theContent = "";
				String line = "";
				while ((line = br.readLine()) != null) {
					theContent += line + "\n";
				}
				String lastContentType = urlConnection.getContentType();

				System.out.println("response: " + lastResponseCode
						+ " content-type: " + lastContentType + " for: "
						+ theContent);
			} catch (IOException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HTTP_POST_IO, e);
			} catch (KeyManagementException e) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_KEYSTORE_MANAGMENT, e);
			} catch (UnrecoverableKeyException e) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_KEYSTORE_RECOVER, e);
			} catch (NoSuchAlgorithmException e) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_KEYSTORE_ALGORITHMEN, e);
			} catch (CertificateException e) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_KEYSTORE_CERTIFICATE, e);
			} catch (KeyStoreException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE, e);
			}
		}

		private HttpURLConnection buildUrlconnectionPost(IOrderResponse response)
				throws IOException, NoSuchAlgorithmException,
				CertificateException, KeyStoreException,
				KeyManagementException, UnrecoverableKeyException {
			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac()
					.versandwegPartnerFindByPrimaryKey(
							response.getVersandwegId(), response.getPartnerId());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_PARTNER_KUNDENNUMMER_FEHLT,
						response.getPartnerId().toString());
			}
			VersandwegCCPartnerDto ccPartnerDto = (VersandwegCCPartnerDto) versandwegPartnerDto;

			String uri = getCCEndpunkt(em, ccPartnerDto);
			// String uri =
			// "http://localhost:8280/restapi/services/rest/api/beta/cc/?dummy=value"
			// ;
			uri += "&datatype=osd";
			uri += "&companycode=" + ccPartnerDto.getCKundennummer().trim();
			uri += "&password=" + ccPartnerDto.getCKennwort().trim();

			URL requestedUrl = new URL(uri);
			HttpURLConnection urlConnection = (HttpURLConnection) requestedUrl
					.openConnection();
			if (urlConnection instanceof HttpsURLConnection) {
				KeyStore keystore = getKeystore(ccPartnerDto);
				SSLContext sslContext = getSslContext(ccPartnerDto, keystore);

				((HttpsURLConnection) urlConnection)
						.setSSLSocketFactory(sslContext.getSocketFactory());
			}
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("User-Agent", "HELIUM V");
			urlConnection.setConnectTimeout(10000);
			urlConnection.setReadTimeout(10500);
			urlConnection.setDoOutput(true);

			return urlConnection;
		}

		@Override
		public String toString(IOrderResponse orderResponse) {
			return orderResponse instanceof OrderResponseCC ? fromXml((OrderResponseCC) orderResponse)
					: null;
		}

		private String fromXml(OrderResponseCC orderresponse) {
			StringWriter writer = new StringWriter();
			try {
				JAXBContext context = JAXBContext.newInstance(orderresponse
						.getResponse().getClass().getPackage().getName());
				Marshaller m = context.createMarshaller();
				m.marshal(orderresponse.getResponse(), writer);
				String s = writer.toString();
				return s;
			} catch (JAXBException e) {
				System.out.println("JAXBException" + e.getMessage());
			}

			return null;
		}

		private XMLXMLORDERRESPONSE createOr(IOrderResponse response,
				AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			XMLXMLORDERRESPONSE or = new XMLXMLORDERRESPONSE();
			XMLXMLORDERRESPONSEHEADER orHeader = new XMLXMLORDERRESPONSEHEADER();
			XMLXMLCONTROLINFO controlInfo = new XMLXMLCONTROLINFO();
			controlInfo.setGENERATORINFO(GENERATOR_INFO);
			controlInfo
					.setGENERATIONDATE(formatAsIso8601Timestamp(GregorianCalendar
							.getInstance().getTime()));
			orHeader.setCONTROLINFO(controlInfo);

			Collection<AuftragpositionDto> positionsDto = getAuftragPositionsDto(
					auftragDto, theClientDto);

			XMLXMLORDERRESPONSEINFO orInfo = buildOrInfo(response, auftragDto,
					theClientDto);
			orHeader.setORDERRESPONSEINFO(orInfo);
			or.setORDERRESPONSEHEADER(orHeader);

			XMLXMLORDERRESPONSEITEMLIST orItemList = buildOrItemList(response,
					auftragDto, positionsDto, theClientDto);
			or.setORDERRESPONSEITEMLIST(orItemList);

			XMLXMLORDERRESPONSESUMMARY orSummary = new XMLXMLORDERRESPONSESUMMARY();
			orSummary.setTOTALITEMNUM(new BigInteger(String.valueOf(orItemList
					.getORDERRESPONSEITEM().size())));
			orSummary.setTOTALAMOUNT(ccScaled3(((OrderResponseCC) response)
					.getTotalPriceLineAmount()));
			or.setORDERRESPONSESUMMARY(orSummary);

			return or;
		}

		private XMLXMLORDERRESPONSEINFO buildOrInfo(IOrderResponse response,
				AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			XMLXMLORDERRESPONSEINFO orInfo = new XMLXMLORDERRESPONSEINFO();
			orInfo.setORDERID(auftragDto.getCBestellnummer());
			orInfo.setSUPPLIERORDERID(auftragDto.getCNr());

			Timestamp t = auftragDto.getTGedruckt();
			if (t == null)
				t = new Timestamp(System.currentTimeMillis());
			orInfo.setORDERRESPONSEDATE(formatAsIso8601Timestamp(new Date(t
					.getTime())));

			if (!HelperWebshop.isEmptyString(auftragDto
					.getCBezProjektbezeichnung())) {
				XMLXMLREMARK dnRemark = new XMLXMLREMARK();
				dnRemark.setValue(auftragDto.getCBezProjektbezeichnung());
				orInfo.getREMARK().add(dnRemark);
			}

			orInfo.setPRICECURRENCY(getDtCurrency(auftragDto, theClientDto));

			XMLXMLORDERPARTIES xmlParties = buildOrInfoParties(response,
					orInfo, auftragDto, theClientDto);
			orInfo.setORDERPARTIES(xmlParties);

			return orInfo;
		}

		private XMLXMLORDERPARTIES buildOrInfoParties(
				IOrderResponse orderResponse, XMLXMLORDERRESPONSEINFO orInfo,
				AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			XMLXMLORDERPARTIES xmlParties = new XMLXMLORDERPARTIES();
			XMLXMLBUYERPARTY buyerParty = new XMLXMLBUYERPARTY();
			XMLXMLPARTY xmlParty = new XMLXMLPARTY();
			XMLXMLPARTYID xmlPartyId = new XMLXMLPARTYID();
			xmlPartyId.setValue(getLieferantennummer(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto));
			xmlParty.setPARTYID(xmlPartyId);
			buyerParty.setPARTY(xmlParty);
			xmlParties.setBUYERPARTY(buyerParty);

			XMLXMLSUPPLIERPARTY supplierParty = new XMLXMLSUPPLIERPARTY();
			XMLXMLPARTY xmlSupplierParty = new XMLXMLPARTY();
			XMLXMLPARTYID xmlSupplierPartyId = new XMLXMLPARTYID();
			xmlSupplierPartyId.setValue(getSupplierPartyId(orderResponse,
					auftragDto, theClientDto));
			xmlSupplierParty.setPARTYID(xmlSupplierPartyId);
			supplierParty.setPARTY(xmlSupplierParty);
			xmlParties.setSUPPLIERPARTY(supplierParty);

			XMLXMLINVOICEPARTY invoiceParty = new XMLXMLINVOICEPARTY();
			XMLXMLPARTY xmlInvoiceParty = new XMLXMLPARTY();
			XMLXMLPARTYID xmlInvoicePartyId = new XMLXMLPARTYID();
			xmlInvoicePartyId.setValue(getLieferantennummer(
					auftragDto.getKundeIIdRechnungsadresse(), theClientDto));
			xmlInvoiceParty.setPARTYID(xmlInvoicePartyId);
			invoiceParty.setPARTY(xmlInvoiceParty);
			xmlParties.setINVOICEPARTY(invoiceParty);

			XMLXMLSHIPMENTPARTIES xmlShipmentParties = new XMLXMLSHIPMENTPARTIES();
			XMLXMLDELIVERYPARTY xmlDeliveryParty = new XMLXMLDELIVERYPARTY();
			xmlParty = new XMLXMLPARTY();
			XMLXMLPARTYID xmlDeliveryPartyId = new XMLXMLPARTYID();
			xmlDeliveryPartyId.setValue(getLieferantennummer(
					auftragDto.getKundeIIdLieferadresse(), theClientDto));
			xmlParty.setPARTYID(xmlDeliveryPartyId);

			KundeDto lieferKundeDto = getKundeFac()
					.kundeFindByPrimaryKeyOhneExc(
							auftragDto.getKundeIIdLieferadresse(), theClientDto);
			if (lieferKundeDto != null) {
				PartnerDto lieferPartnerDto = getPartnerFac()
						.partnerFindByPrimaryKeyOhneExc(
								lieferKundeDto.getPartnerIId(), theClientDto);
				XMLXMLADDRESS xmlAddress = new XMLXMLADDRESS();
				xmlAddress.setNAME(lieferPartnerDto
						.getCName1nachnamefirmazeile1());
				xmlAddress.setNAME2(lieferPartnerDto
						.getCName2vornamefirmazeile2());
				xmlAddress.setNAME3(lieferPartnerDto
						.getCName3vorname2abteilung());
				xmlAddress.setSTREET(lieferPartnerDto.getCStrasse());
				xmlAddress
						.setZIP(lieferPartnerDto.getLandplzortDto().getCPlz());
				xmlAddress.setCITY(lieferPartnerDto.getLandplzortDto()
						.getOrtDto().getCName());
				xmlAddress.setCCCOUNTRY(lieferPartnerDto.getLandplzortDto()
						.getLandDto().getCName());
				xmlAddress.setCCCOUNTRYISOCODE(lieferPartnerDto
						.getLandplzortDto().getLandDto().getCLkz());
				xmlParty.setADDRESS(xmlAddress);
			}

			xmlDeliveryParty.setPARTY(xmlParty);

			xmlShipmentParties.setDELIVERYPARTY(xmlDeliveryParty);
			xmlParties.setSHIPMENTPARTIES(xmlShipmentParties);

			return xmlParties;
		}

		private String getLieferantennummer(Integer kundeIId,
				TheClientDto theClientDto) {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
					theClientDto);
			return kundeDto.getCLieferantennr();
		}

		private DtCURRENCIES getDtCurrency(AuftragDto auftragDto,
				TheClientDto theClientDto) throws EJBExceptionLP,
				RemoteException {
			KundeDto kundeDto = null;
			String currency = auftragDto.getWaehrungCNr();
			if (currency == null) {
				kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
				currency = kundeDto.getCWaehrung();
			}
			if (currency == null) {
				MandantDto mandantDto = getMandantFac()
						.mandantFindByPrimaryKey(kundeDto.getMandantCNr(),
								theClientDto);
				currency = mandantDto.getWaehrungCNr();
			}

			return DtCURRENCIES.fromValue(currency);
		}

		private String getSupplierPartyId(IOrderResponse response,
				AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			Integer versandwegId = getVersandwegIdFor(auftragDto, theClientDto);
			if (versandwegId == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						"");
			}

			Integer kundeId = auftragDto.getKundeIIdLieferadresse();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId,
					theClientDto);

			response.setVersandwegId(versandwegId);
			response.setPartnerId(kundeDto.getPartnerIId());

			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac()
					.versandwegPartnerFindByPrimaryKey(versandwegId,
							response.getPartnerId());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_PARTNER_KUNDENNUMMER_FEHLT,
						kundeDto.getPartnerIId().toString());
			}
			VersandwegCCPartnerDto partnerDto = (VersandwegCCPartnerDto) versandwegPartnerDto;
			String supplierId = partnerDto.getCKundennummer();
			return supplierId.trim();
		}

		private String buildItemCnr(ArtikelDto itemDto) {
			String cnr = itemDto.getCNr();
			return cnr;
		}

		private XMLXMLORDERRESPONSEITEMLIST buildOrItemList(
				IOrderResponse response, AuftragDto auftragDto,
				Collection<AuftragpositionDto> ejbPositions,
				TheClientDto theClientDto) throws RemoteException {
			XMLXMLORDERRESPONSEITEMLIST itemlist = new XMLXMLORDERRESPONSEITEMLIST();
			OrderResponseCC ccResponse = (OrderResponseCC) response;

			for (AuftragpositionDto auftragpositionDto : ejbPositions) {
				XMLXMLORDERRESPONSEITEM orItem = new XMLXMLORDERRESPONSEITEM();
				// orItem.setCCDRAWINGNR(value);
				// orItem.setCCINDEXNR(value);
				orItem.setCCORDERUNIT(auftragpositionDto.getEinheitCNr());
				String refLineItemId = extractLineItemIdRef(auftragpositionDto);
				orItem.setLINEITEMID(refLineItemId);
				orItem.setCCORDERUNIT(auftragpositionDto.getEinheitCNr().trim());

				ArtikelDto itemDto = getArtikelFac().artikelFindByPrimaryKey(
						auftragpositionDto.getArtikelIId(), theClientDto);
				orItem.setCCORIGINCOUNTRY(buildUrsprungsland(itemDto));

				XMLXMLARTICLEID itemId = new XMLXMLARTICLEID();
				itemId.setSUPPLIERAID(buildItemCnr(itemDto));
				if (itemDto.getArtikelsprDto() != null) {
					itemId.setDESCRIPTIONSHORT(HelperWebshop
							.isEmptyString(itemDto.getArtikelsprDto()
									.getCKbez()) ? itemDto.getCNr() : itemDto
							.getArtikelsprDto().getCKbez());
				} else {
					itemId.setDESCRIPTIONSHORT(itemDto.getCNr());
				}
				KundesokoDto[] sokos = getKundesokoFac()
						.kundesokoFindByKundeIIdArtikelIId(
								auftragDto.getKundeIIdLieferadresse(),
								itemDto.getIId());
				if (sokos != null && sokos.length > 0) {
					XMLXMLBUYERAID buyerItemId = new XMLXMLBUYERAID();
					buyerItemId.setValue(sokos[0].getCKundeartikelnummer());
					buyerItemId.setType("buyer_specific");
					List<XMLXMLBUYERAID> buyerItemlist = itemId.getBUYERAID();
					buyerItemlist.add(buyerItemId);
				}

				orItem.setARTICLEID(itemId);
				orItem.setQUANTITY(ccScaled3(auftragpositionDto.getNMenge()));

				XMLXMLARTICLEPRICE xmlPrice = new XMLXMLARTICLEPRICE();
				xmlPrice.setType("net_customer");
				BigDecimal einzelpreis = auftragpositionDto
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();
				xmlPrice.setPRICEAMOUNT(ccScaled4(einzelpreis));
				BigDecimal lineamount = ccScaled3(einzelpreis
						.multiply(auftragpositionDto.getNMenge()));
				ccResponse.addTotalPriceLineAmount(lineamount);
				xmlPrice.setPRICELINEAMOUNT(lineamount);
				xmlPrice.setPRICEQUANTITY(BigDecimal.ONE);
				xmlPrice.setCCPRICECONVERSIONRATIO(BigDecimal.ONE);
				xmlPrice.setCCORDERUNIT(auftragpositionDto.getEinheitCNr()
						.trim());
				orItem.setARTICLEPRICE(xmlPrice);

				XMLXMLDELIVERYDATE xmlDeliveryDate = new XMLXMLDELIVERYDATE();
				String deliveryDate = formatAsIso8601Timestamp(new Date(
						auftragpositionDto.getTUebersteuerbarerLiefertermin()
								.getTime()));
				xmlDeliveryDate.setDELIVERYSTARTDATE(deliveryDate);
				xmlDeliveryDate.setDELIVERYENDDATE(deliveryDate);
				orItem.setDELIVERYDATE(xmlDeliveryDate);

				XMLXMLCCCUSTOMSINFO xmlCustomsInfo = new XMLXMLCCCUSTOMSINFO();
				xmlCustomsInfo.setCCORDERUNIT(auftragpositionDto
						.getEinheitCNr().trim());
				orItem.setCCCUSTOMSINFO(xmlCustomsInfo);

				itemlist.getORDERRESPONSEITEM().add(orItem);
			}

			return itemlist;
		}

		private String buildUrsprungsland(ArtikelDto itemDto) {
			if (itemDto.getLandIIdUrsprungsland() == null) {
				return "";
			}
			Integer landId = itemDto.getLandIIdUrsprungsland();
			LandDto landDto = getSystemFac().landFindByPrimaryKey(landId);
			return landDto.getCLkz();
		}

		public class AuftragpositionenNurIdentFilter implements Predicate,
				Serializable {
			private static final long serialVersionUID = -5617572280308975044L;

			@Override
			public boolean evaluate(Object arg0) {
				if (arg0 instanceof AuftragpositionDto) {
					AuftragpositionDto pos = (AuftragpositionDto) arg0;
					return pos.isIdent();
				}

				return false;
			}
		}

		private Collection<AuftragpositionDto> getAuftragPositionsDto(
				AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			Collection<AuftragpositionDto> ejbPositions = getAuftragpositionFac()
					.auftragpositionFindByAuftragList(auftragDto.getIId());
			CollectionUtils.filter(ejbPositions,
					new AuftragpositionenNurIdentFilter());
			return ejbPositions;
		}
	}

	public boolean hatAuftragVersandweg(AuftragDto auftragDto,
			TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(auftragDto, "auftragDto");
		return getKundeFac().hatKundeVersandweg(
				auftragDto.getKundeIIdLieferadresse(), theClientDto);
	}

	public boolean hatAuftragVersandweg(Integer auftragIId,
			TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(auftragIId, "auftragIId");
		AuftragDto auftragDto = auftragFindByPrimaryKey(auftragIId);
		return getKundeFac().hatKundeVersandweg(
				auftragDto.getKundeIIdLieferadresse(), theClientDto);
	}

	@Override
	public BelegPruefungDto aktiviereBelegControlled(Integer iid, Timestamp t,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.aktiviereBelegControlled(this, iid, t, theClientDto) ;
//		new BelegAktivierungController(this).aktiviereBelegControlled(iid, t,
//				theClientDto);
	}

	@Override
	public BelegPruefungDto berechneBelegControlled(Integer iid,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.berechneBelegControlled(this, iid, theClientDto) ;
//		return new BelegAktivierungController(this).berechneBelegControlled(
//				iid, theClientDto);
	}

	@Override
	public List<Timestamp> getAenderungsZeitpunkte(Integer iid)
			throws EJBExceptionLP, RemoteException {
		AuftragDto a = auftragFindByPrimaryKey(iid);
		List<Timestamp> timestamps = new ArrayList<Timestamp>();
		timestamps.add(a.getTAendern());
		timestamps.add(a.getTBegruendung());
		timestamps.add(a.getTErledigt());
		timestamps.add(a.getTManuellerledigt());
		timestamps.add(a.getTStorniert());
		return timestamps;
	}

	@Override
	public BelegPruefungDto berechneAktiviereBelegControlled(Integer iid,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.berechneAktiviereControlled(this, iid, theClientDto) ;
//		new BelegAktivierungController(this).berechneAktiviereControlled(iid, theClientDto);
	}
	
	@Override
	public BelegDto getBelegDto(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BelegDto belegDto = auftragFindByPrimaryKey(iid) ;
		belegDto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG); 
		return belegDto ;
	}
	
	@Override
	public BelegpositionDto[] getBelegPositionDtos(Integer iid,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return getAuftragpositionFac().auftragpositionFindByAuftrag(iid);
	}
	
	@Override
	public Integer getKundeIdDesBelegs(BelegDto belegDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return ((AuftragDto)belegDto).getKundeIIdRechnungsadresse();
	}
}
