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
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jcr.RepositoryException;
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

import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.artikel.ejb.Artgru;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.ejb.IndexanpassungLog;
import com.lp.server.auftrag.ejb.Lieferstatus;
import com.lp.server.auftrag.ejb.Verrechenbar;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionReport;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragDtoAssembler;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragNachkalkulationDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragauftragdokumentDto;
import com.lp.server.auftrag.service.AuftragdokumentDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionDtoAssembler;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.auftrag.service.IOrderResponse;
import com.lp.server.auftrag.service.IOrderResponseProducer;
import com.lp.server.auftrag.service.ImportAmazonCsvDto;
import com.lp.server.auftrag.service.ImportShopifyCsvDto;
import com.lp.server.auftrag.service.ImportSonCsvDto;
import com.lp.server.auftrag.service.ImportVATXlsxDto;
import com.lp.server.auftrag.service.ImportWooCommerceCsvDto;
import com.lp.server.auftrag.service.LieferstatusDto;
import com.lp.server.auftrag.service.OrderResponseCC;
import com.lp.server.auftrag.service.OrderResponseEdiOrdRsp;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.KundeKennung;
import com.lp.server.partner.ejb.KundeKennungQuery;
import com.lp.server.partner.ejb.Kundesoko;
import com.lp.server.partner.ejb.KundesokoQuery;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.PartnerQuery;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.rechnung.ejb.Mmz;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.ejbfac.CoinRoundingServiceFac;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.MmzDto;
import com.lp.server.rechnung.service.RechnungDto;
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
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.ejb.Landplzort;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejb.Lieferartspr;
import com.lp.server.system.ejb.Ort;
import com.lp.server.system.ejb.VersandwegPartner;
import com.lp.server.system.ejb.VersandwegPartnerQuery;
import com.lp.server.system.ejb.Zahlungsziel;
import com.lp.server.system.ejb.Zahlungszielspr;
import com.lp.server.system.ejbfac.BelegAktivierungFac;
import com.lp.server.system.ejbfac.CleverCureProducer;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.EdifactOrderResponseProducer;
import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.ejbfac.Versionizer;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeAuftrag;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.IVersandwegPartnerDto;
import com.lp.server.system.service.ImportRueckgabeDto;
import com.lp.server.system.service.KennungType;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemFac.VersandwegType;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandwegPartnerCCDto;
import com.lp.server.system.service.VersandwegPartnerOrdRspDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.AuftragPositionNumberAdapter;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperWebshop;
import com.lp.server.util.HvOptional;
import com.lp.server.util.KundeId;
import com.lp.server.util.MwstsatzCache;
import com.lp.server.util.MwstsatzId;
import com.lp.server.util.PositionNumberAdapter;
import com.lp.server.util.Validator;
import com.lp.server.util.ZwsPositionMapper;
import com.lp.server.util.bean.EdifactOrdersImportImplBean;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.BelegDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AuftragFacBean extends Facade implements AuftragFac, IAktivierbar {
	@PersistenceContext
	private EntityManager em;

	@EJB
	private BelegAktivierungFac belegAktivierungFac;

	@EJB
	private CoinRoundingServiceFac coinRoundingService;

	private EdifactOrdersImportImplBean ediImportImplBean = new EdifactOrdersImportImplBean();

	// Referenzen auf CMPs

	// Auftrag
	// -------------------------------------------------------------------

	/**
	 * Anlegen eines neuen Auftragskopfes in der DB.
	 * 
	 * @param oAuftragDtoI die Daten des Auftragkopfes
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @return Integer PK des Auftrags
	 */
	public Integer createAuftrag(AuftragDto oAuftragDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragDto(oAuftragDtoI);
		Integer auftragIId = null;
		String auftragCNr = null;

		try {
			// Generieren von PK + Belegnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(oAuftragDtoI.getMandantCNr());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(oAuftragDtoI.getMandantCNr(),
					oAuftragDtoI.getTBelegdatum());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(iGeschaeftsjahr, PKConst.PK_AUFTRAG,
					oAuftragDtoI.getMandantCNr(), theClientDto);

			auftragIId = bnr.getPrimaryKey();
			auftragCNr = f.formatMitStellenZufall(bnr);

			oAuftragDtoI.setIId(auftragIId);
			oAuftragDtoI.setCNr(auftragCNr);
			oAuftragDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			oAuftragDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());

			// PJ19958 Mit erstem anhand iSort befuellen

			if (oAuftragDtoI.getVerrechenbarIId() == null) {
				Query query = em.createNamedQuery("VerrechenbarFindAll");
				query.setMaxResults(1);
				Collection<?> cl = query.getResultList();
				Verrechenbar v = (Verrechenbar) cl.iterator().next();
				oAuftragDtoI.setVerrechenbarIId(v.getIId());

			}

			// PJ20739
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ABRECHNUNGSVORSCHLAG,
					theClientDto)) {
				if (oAuftragDtoI.getVerrechnungsmodellIId() == null) {
					KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(oAuftragDtoI.getKundeIIdRechnungsadresse(),
							theClientDto);

					if (kdDto.getVerrechnungsmodellIId() == null) {
						ArrayList al = new ArrayList();
						al.add(oAuftragDtoI.getCNr());
						al.add(kdDto.getPartnerDto().formatAnrede());
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_KEIN_VERRECHNUNGSMODELL, al,
								new Exception(""));
					} else {
						oAuftragDtoI.setVerrechnungsmodellIId(kdDto.getVerrechnungsmodellIId());
					}

				}
			}

			if (oAuftragDtoI.getBMindermengenzuschlag() == null) {
				oAuftragDtoI.setBMindermengenzuschlag(Helper.boolean2Short(false));
			}

			Timestamp t = getTimestamp();
			// java.sql.Date d = getDate();

			Auftrag auftrag = new Auftrag(oAuftragDtoI.getIId(), oAuftragDtoI.getCNr(), oAuftragDtoI.getMandantCNr(),
					oAuftragDtoI.getAuftragartCNr(), oAuftragDtoI.getKundeIIdAuftragsadresse(),
					oAuftragDtoI.getKundeIIdLieferadresse(), oAuftragDtoI.getKundeIIdRechnungsadresse(),
					oAuftragDtoI.getCAuftragswaehrung(), t, // dLiefertermin
					t, // dFinaltermin
					oAuftragDtoI.getLieferartIId(), oAuftragDtoI.getZahlungszielIId(), oAuftragDtoI.getSpediteurIId(),
					oAuftragDtoI.getStatusCNr(), oAuftragDtoI.getPersonalIIdAnlegen(),
					oAuftragDtoI.getPersonalIIdAendern(), oAuftragDtoI.getKostIId(),
					oAuftragDtoI.getPersonalIIdVertreter(), LocaleFac.BELEGART_AUFTRAG, // belegart
					oAuftragDtoI.getFWechselkursmandantwaehrungzubelegwaehrung(),
					oAuftragDtoI.getLagerIIdAbbuchungslager(), oAuftragDtoI.getVerrechenbarIId());
			em.persist(auftrag);
			em.flush();

			oAuftragDtoI.setTAnlegen(auftrag.getTAnlegen());
			oAuftragDtoI.setTAendern(auftrag.getTAendern());

			if (oAuftragDtoI.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {

				ParametermandantDto whVerstecktDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_WIEDERHOLENDER_AUFTRAG_VERSTECKT);
				if ((Boolean) whVerstecktDto.getCWertAsObject() == true) {
					oAuftragDtoI.setBVersteckt(Helper.boolean2Short(true));
				} else {
					oAuftragDtoI.setBVersteckt(Helper.boolean2Short(false));
				}
			}

			setAuftragFromAuftragDto(auftrag, oAuftragDtoI);

			// PJ14938
			if (oAuftragDtoI.getKundeIIdRechnungsadresse() != null) {
				getKundeFac().checkAndCreateAutoDebitorennummerZuKunden(
						new KundeId(oAuftragDtoI.getKundeIIdRechnungsadresse()), theClientDto);
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
	 * @param auftragDto AuftragDto
	 * @throws EJBExceptionLP
	 */
	private void checkAuftragDto(AuftragDto auftragDto) throws EJBExceptionLP {
		if (auftragDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("auftragDto == null"));
		}

		myLogger.info("AuftragDto: " + auftragDto);
	}

	private void checkAuftragIId(Integer iIdAuftragI) throws EJBExceptionLP {
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAuftragI == null"));
		}

		myLogger.info("AuftragIId: " + iIdAuftragI);
	}

	/**
	 * Aktualisieren der Kopfdaten eines Auftrags.
	 * 
	 * @param auftragDtoI     die Daten des Auftrags
	 * @param waehrungOriCNrI die urspruengliche Belegwaehrung aenderewaehrung: 0
	 * @param theClientDto    der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	// TODO Grosser Auftrag bei Kunden. Funktion sollte jedoch
	// optimiert werden
	@org.jboss.ejb3.annotation.TransactionTimeout(60000)
	public boolean updateAuftrag(AuftragDto auftragDtoI, String waehrungOriCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAuftragDto(auftragDtoI);
		boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = false;
		AuftragDto auftragOldDto = auftragFindByPrimaryKey(auftragDtoI.getIId());
		try {
			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragDtoI.getIId());

			if (auftragOldDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)
					&& aAuftragpositionDto.length > 0) {

				if (auftragOldDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)
						&& !auftragDtoI.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN,
							"FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN");
				}

				if (!auftragOldDto.getAuftragIIdRahmenauftrag().equals(auftragDtoI.getAuftragIIdRahmenauftrag())) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN,
							"FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN");
				}
			}

			// SK: Fuer Voest eingebaut am 27.1.2009
			if (auftragOldDto.getDFinaltermin() != null && auftragDtoI.getDFinaltermin() != null) {
				if (!auftragOldDto.getDFinaltermin().equals(auftragDtoI.getDFinaltermin())) {
					// Finaltermin hat sich geaendert... Negative Positionen
					// bekommen neuen Positionstermin
					for (int i = 0; i < aAuftragpositionDto.length; i++) {
						if (aAuftragpositionDto[i].getNMenge() != null) {
							if (aAuftragpositionDto[i].getNMenge().compareTo(new BigDecimal(0)) < 0
									&& aAuftragpositionDto[i].getTUebersteuerbarerLiefertermin()
											.equals(auftragOldDto.getDFinaltermin())) {
								aAuftragpositionDto[i].setTUebersteuerbarerLiefertermin(auftragDtoI.getDFinaltermin());
								getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(aAuftragpositionDto[i],
										theClientDto);
								// Artikelreservierung Liefertermin aendern
								if (aAuftragpositionDto[i].isIdent()) {
									ArtikelreservierungDto artikelreservierungDto = getReservierungFac()
											.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
													LocaleFac.BELEGART_AUFTRAG, aAuftragpositionDto[i].getIId());
									if (artikelreservierungDto != null) {
										artikelreservierungDto.setTLiefertermin(
												aAuftragpositionDto[i].getTUebersteuerbarerLiefertermin());
										getReservierungFac().updateArtikelreservierung(artikelreservierungDto);
									}
								}
							}
						}
					}
				}
			}
			// aenderewaehrung: 1 wenn die Waehrung geaendert wurde, muessen die
			// Belegwerte neu berechnet werden
			if (waehrungOriCNrI != null && !waehrungOriCNrI.equals(auftragDtoI.getCAuftragswaehrung())) {

				// aenderewaehrung: 2 die Positionswerte neu berechnen und
				// abspeichern
				BigDecimal ffWechselkurs = getLocaleFac().getWechselkurs2(waehrungOriCNrI,
						auftragDtoI.getCAuftragswaehrung(), theClientDto);

				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					if (aAuftragpositionDto[i].getNMenge() != null
							&& aAuftragpositionDto[i].getNEinzelpreis() != null) {
						BigDecimal nNettoeinzelpreisInNeuerWaehrung = aAuftragpositionDto[i].getNEinzelpreis()
								.multiply(ffWechselkurs);

						VerkaufspreisDto verkaufspreisDto = getVkPreisfindungFac().berechnePreisfelder(
								nNettoeinzelpreisInNeuerWaehrung, aAuftragpositionDto[i].getFRabattsatz(),
								aAuftragpositionDto[i].getFZusatzrabattsatz(), aAuftragpositionDto[i].getMwstsatzIId(),
								4, // @todo Konstante PJ 3778
								theClientDto);

						aAuftragpositionDto[i].setNEinzelpreis(verkaufspreisDto.einzelpreis);
						aAuftragpositionDto[i].setNRabattbetrag(verkaufspreisDto.rabattsumme);
						aAuftragpositionDto[i].setNNettoeinzelpreis(verkaufspreisDto.nettopreis);
						aAuftragpositionDto[i].setNMwstbetrag(verkaufspreisDto.mwstsumme);
						aAuftragpositionDto[i].setNBruttoeinzelpreis(verkaufspreisDto.bruttopreis);
						// alle Preisfelder incl. der zusaetzlichen Preisfelder
						// befuellen
						getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(aAuftragpositionDto[i],
								theClientDto);
					}
				}
			}

			Timestamp auftragOldLT = Helper.cutTimestamp(auftragOldDto.getDLiefertermin());
			if (!auftragOldDto.getDLiefertermin().equals(auftragDtoI.getDLiefertermin())) {
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					Timestamp posLT = Helper.cutTimestamp(aAuftragpositionDto[i].getTUebersteuerbarerLiefertermin());
					if (posLT.equals(auftragOldLT)) {
						aAuftragpositionDto[i].setTUebersteuerbarerLiefertermin(
								new Timestamp(auftragDtoI.getDLiefertermin().getTime()));
						getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(aAuftragpositionDto[i],
								theClientDto);
						// Artikelreservierung Liefertermin aendern
						if (aAuftragpositionDto[i].isIdent()) {
							ArtikelreservierungDto artikelreservierungDto = getReservierungFac()
									.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
											LocaleFac.BELEGART_AUFTRAG, aAuftragpositionDto[i].getIId());
							if (artikelreservierungDto != null) {
								artikelreservierungDto
										.setTLiefertermin(new Timestamp(auftragDtoI.getDLiefertermin().getTime()));
								getReservierungFac().updateArtikelreservierung(artikelreservierungDto);
							}
						}
					}
				}
			}

			// SP8308
			if (!auftragOldDto.getTBelegdatum().equals(auftragDtoI.getTBelegdatum())) {
				checkMwstsaetzeImZeitraum(auftragOldDto.getTBelegdatum(), auftragDtoI.getTBelegdatum(),
						auftragDtoI.getMandantCNr());
			}

			// Auftrag Kunde wurde geaendert ?
			if (!auftragOldDto.getKundeIIdAuftragsadresse().equals(auftragDtoI.getKundeIIdAuftragsadresse())) {
				// mwstsatz
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDtoI.getKundeIIdAuftragsadresse(),
						theClientDto);
				KundeDto kundeDtoVorher = getKundeFac()
						.kundeFindByPrimaryKey(auftragOldDto.getKundeIIdAuftragsadresse(), theClientDto);
				ParametermandantDto parameterPositionskontierung = getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);

				boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung.getCWertAsObject();

				MwstsatzCache mwstsatzCache = new MwstsatzCache(auftragDtoI, theClientDto);
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					if (aAuftragpositionDto[i].isIdent() || aAuftragpositionDto[i].isHandeingabe()) {

						/*
						 * MwstsatzDto mwstsatzDto = getMandantFac()
						 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(),
						 * theClientDto);
						 */
						MwstsatzDto mwstsatzDto = mwstsatzCache.getValueOfKey(kundeDto.getMwstsatzbezIId());
						if (bDefaultMwstsatzAusArtikel && aAuftragpositionDto[i].isIdent()) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(aAuftragpositionDto[i].getArtikelIId(), theClientDto);
							if (artikelDto.getMwstsatzbezIId() != null) {
								/*
								 * mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
								 * artikelDto.getMwstsatzbezIId(), theClientDto);
								 */
								mwstsatzDto = mwstsatzCache.getValueOfKey(artikelDto.getMwstsatzbezIId());
							}

						}
						// SP503
						if (bDefaultMwstsatzAusArtikel && aAuftragpositionDto[i].isHandeingabe()) {

							// Wenn alter und neuer Kunde den gleichen MWST-Satz
							// haben, dann nichts tun
							/*
							 * MwstsatzDto mwstsatzDtoKundeNeu =
							 * getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
							 * kundeDto.getMwstsatzbezIId(), theClientDto);
							 * 
							 * MwstsatzDto mwstsatzDtoKundeVorher = getMandantFac()
							 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDtoVorher.getMwstsatzbezIId(),
							 * theClientDto);
							 */
							MwstsatzDto mwstsatzDtoKundeNeu = mwstsatzCache.getValueOfKey(kundeDto.getMwstsatzbezIId());
							MwstsatzDto mwstsatzDtoKundeVorher = mwstsatzCache
									.getValueOfKey(kundeDtoVorher.getMwstsatzbezIId());
							if (mwstsatzDtoKundeVorher.getFMwstsatz() == 0 && mwstsatzDtoKundeNeu.getFMwstsatz() > 0) {
								bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = true;
							}

							if (mwstsatzDtoKundeNeu.getIId().equals(mwstsatzDtoKundeVorher.getIId())) {
								continue;
							}
						}
						if (!aAuftragpositionDto[i].getMwstsatzIId().equals(mwstsatzDto.getIId())) {
							aAuftragpositionDto[i].setMwstsatzIId(mwstsatzDto.getIId());

							BigDecimal mwstBetrag = aAuftragpositionDto[i]
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte().multiply(
											new BigDecimal(mwstsatzDto.getFMwstsatz().doubleValue()).movePointLeft(2));
							aAuftragpositionDto[i].setNMwstbetrag(mwstBetrag);
							aAuftragpositionDto[i].setNBruttoeinzelpreis(mwstBetrag.add(
									aAuftragpositionDto[i].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
							getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(aAuftragpositionDto[i],
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			if (auftrag.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
				if (!auftragDtoI.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF))
					auftragDtoI.setAuftragIIdRahmenauftrag(null);

			}

			if (!auftragDtoI.getKundeIIdAuftragsadresse().equals(auftrag.getKundeIIdAuftragsadresse())) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDtoI.getKundeIIdAuftragsadresse(),
						theClientDto);

				Integer iGarantie = new Integer(0);
				if (kundeDto.getIGarantieinmonaten() != null) {
					iGarantie = kundeDto.getIGarantieinmonaten();
				}
				auftragDtoI.setIGarantie(iGarantie);
			}

			Integer iGJAlt = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant(), auftrag.getTBelegdatum());
			Integer iGJNeu = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant(),
					auftragDtoI.getTBelegdatum());
			if (!iGJNeu.equals(iGJAlt)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN,
						new Exception("Es wurde versucht, den AB " + auftragDtoI.getCNr() + " auf "
								+ auftragDtoI.getTBelegdatum() + " (GJ " + iGJNeu + ") umzudatieren"));
			}

			setAuftragFromAuftragDto(auftrag, auftragDtoI);
			// aenderewaehrung: 3 der Status des Auftrags wechselt auf Angelegt
			pruefeUndSetzeAuftragstatusBeiAenderung(auftragDtoI.getIId(), theClientDto);

			if (auftragDtoI.getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)) {
				mindermengenzuschlagEntfernen(assembleAuftragDto(auftrag), theClientDto);
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben;
	}

	/**
	 * Die Begr&uuml;ndung des Auftrags auf den neuen Wert setzen
	 * 
	 * @param auftragIId     PK des Auftrags
	 * @param begruendungIId die IId der neuen Auftragbegruendung
	 * @param theClientDto   der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */

	public void updateAuftragBegruendung(Integer auftragIId, Integer begruendungIId, TheClientDto theClientDto) {

		Auftrag auftrag = em.find(Auftrag.class, auftragIId);
		auftrag.setAuftragbegruendungIId(begruendungIId);
		auftrag.setTBegruendung(new Timestamp(System.currentTimeMillis()));
		auftrag.setPersonalIIdBegruendung(theClientDto.getIDPersonal());
		em.merge(auftrag);
		em.flush();
	}

	public void updateAuftragVersteckt(Integer auftragIId, TheClientDto theClientDto) {

		Auftrag auftrag = em.find(Auftrag.class, auftragIId);
		auftrag.setBVersteckt(Helper.boolean2Short(!Helper.short2boolean(auftrag.getBVersteckt())));
		auftrag.setTAendern(getTimestamp());
		auftrag.setPersonalIIdAendern(theClientDto.getIDPersonal());
		em.merge(auftrag);
		em.flush();
	}

	public void updateAuftragKonditionen(Integer iIdAuftragI, TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "updateAuftragKonditionen";
		myLogger.entry();
		try {
			AuftragpositionDto[] aPosition = getAuftragpositionFac().auftragpositionFindByAuftrag(iIdAuftragI);

			// fuer jede Position die Zu- und Abschlaege neu beruecksichtigen
			for (int i = 0; i < aPosition.length; i++) {
				getAuftragpositionFac().befuelleZusaetzlichePreisfelder(aPosition[i].getIId());
			}

			Auftrag oAuftrag = em.find(Auftrag.class, iIdAuftragI);
			if (oAuftrag == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			oAuftrag.setTAendern(getTimestamp());
			oAuftrag.setPersonalIIdAendern(theClientDto.getIDPersonal());
			if (!oAuftrag.getAuftragstatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
				oAuftrag.setNGesamtauftragswertinauftragswaehrung(berechneNettowertGesamt(iIdAuftragI, theClientDto));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Einen bestehenden Auftrag als storniert kennzeichnen. <br>
	 * Artikelreservierungen werden zurueckgenommen.
	 * 
	 * @param iiAuftragI   pk des Auftrags
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void storniereAuftrag(Integer iiAuftragI, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		try {
			AuftragDto auftragDto = this.auftragFindByPrimaryKey(iiAuftragI);
			auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT);
			auftragDto.setPersonalIIdStorniert(theClientDto.getIDPersonal());
			auftragDto.setTStorniert(new Timestamp(System.currentTimeMillis()));

			// nicht das allgemeine update, das aendert den status wieder
			Auftrag auftrag = em.find(Auftrag.class, auftragDto.getIId());
			if (auftrag == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setAuftragFromAuftragDto(auftrag, auftragDto);

			// eventuell vorhandene auftragreseriverungen zuruecknehmen (aber
			// nicht bei Rahmenauftraegen)

			AuftragpositionDto[] posDtos = getAuftragpositionFac().auftragpositionFindByAuftrag(iiAuftragI);
			for (int i = 0; i < posDtos.length; i++) {
				if (!auftrag.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					if (posDtos[i].isIdent()) {
						if (posDtos[i].getNMenge().compareTo(new BigDecimal(0)) != 0) {
							ArtikelreservierungDto artikelreservierungDto = getReservierungFac()
									.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
											LocaleFac.BELEGART_AUFTRAG, posDtos[i].getIId());
							if (artikelreservierungDto != null) {
								getReservierungFac().removeArtikelreservierung(artikelreservierungDto.getIId());
							}
							getAuftragpositionFac().loescheAuftragseriennrnEinesAuftragposition(posDtos[i].getIId(),
									theClientDto);
						}
					}
					// fuer Handartikel gibt es keine Reservierungen!
				}
				posDtos[i].setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_STORNIERT);
				getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(posDtos[i], theClientDto);
				// Wenn Abrufposition muss auch Rahmen upgedatet werden
				if (posDtos[i].getAuftragpositionIIdRahmenposition() != null) {
					AuftragpositionDto rahmenPosDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(posDtos[i].getAuftragpositionIIdRahmenposition());
					getAuftragpositionFac().updateAuftragposition(rahmenPosDto, theClientDto);
				}

			}

			// PJ19485
			if (auftragDto.getBestellungIIdAndererMandant() != null) {
				BestellungDto bsDto = getBestellungFac()
						.bestellungFindByPrimaryKey(auftragDto.getBestellungIIdAndererMandant());
				/*
				 * TheClientDto theClientDto_Zielmandant = getLogonFac().logon(
				 * Helper.getFullUsername(sUser), Helper.getMD5Hash((sUser +
				 * sPassword).toCharArray()), theClientDto.getLocUi(), bsDto.getMandantCNr(),
				 * new Timestamp(System.currentTimeMillis()));
				 */

				TheClientDto theClientDto_Zielmandant = getLogonFac().logonIntern(theClientDto.getLocUi(),
						bsDto.getMandantCNr());

				getBestellungFac().stornieren(auftragDto.getBestellungIIdAndererMandant(), theClientDto);
				getLogonFac().logout(theClientDto_Zielmandant);
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public ArrayList<String> terminVerschieben(ArrayList<Integer> auftragIIds, java.sql.Timestamp tLiefertermin,
			java.sql.Timestamp tFinaltermin, java.sql.Timestamp tWunschtermin, boolean bMehrereAuftraege,
			TheClientDto theClientDto) {

		ArrayList<String> teilerledigteWelcheNichtVerschobenWurden = new ArrayList<String>();

		for (int j = 0; j < auftragIIds.size(); j++) {

			Integer auftragIId = auftragIIds.get(j);

			Auftrag auftrag = em.find(Auftrag.class, auftragIId);
			auftrag.setTAendern(getTimestamp());
			auftrag.setPersonalIIdAendern(theClientDto.getIDPersonal());

			if (bMehrereAuftraege) {
				if (!auftrag.getAuftragstatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
							new Exception(auftrag.getAuftragstatusCNr()));
				}
			} else {
				if (!auftrag.getAuftragstatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
						&& !auftrag.getAuftragstatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
							new Exception(auftrag.getAuftragstatusCNr()));
				}
			}

			int iTageDiff = Helper.ermittleTageEinesZeitraumes(new java.sql.Date(auftrag.getTLiefertermin().getTime()),
					new java.sql.Date(tLiefertermin.getTime()));

			// 4406
			if (auftrag.getTLiefertermin().after(new java.sql.Date(tLiefertermin.getTime()))) {
				iTageDiff = -iTageDiff;
			}

			auftrag.setTLiefertermin(tLiefertermin);
			auftrag.setTFinaltermin(tFinaltermin);
			auftrag.setTWunschtermin(tWunschtermin);
			em.merge(auftrag);
			em.flush();

			try {
				AuftragpositionDto[] posDtos = getAuftragpositionFac().auftragpositionFindByAuftrag(auftragIId);

				for (int i = 0; i < posDtos.length; i++) {
					AuftragpositionDto auftPos = posDtos[i];

					if (auftPos.getAuftragpositionstatusCNr() != null
							&& (auftPos.getAuftragpositionstatusCNr().equals(LocaleFac.STATUS_OFFEN)
									|| auftPos.getAuftragpositionstatusCNr().equals(LocaleFac.STATUS_TEILERLEDIGT)
									|| auftPos.getAuftragpositionstatusCNr().equals(LocaleFac.STATUS_ERLEDIGT))) {

						auftPos.setTUebersteuerbarerLiefertermin(new Timestamp(
								Helper.addiereTageZuDatum(posDtos[i].getTUebersteuerbarerLiefertermin(), iTageDiff)
										.getTime()));

						if (auftPos.getAuftragpositionstatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {
							if (auftrag.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
								// SP4253
								getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(auftPos, theClientDto);

							}
						} else {

							if (auftPos.getAuftragpositionstatusCNr().equals(LocaleFac.STATUS_TEILERLEDIGT)) {

								String artikel = "";
								if (auftPos.getArtikelIId() != null) {
									artikel = getArtikelFac()
											.artikelFindByPrimaryKey(auftPos.getArtikelIId(), theClientDto)
											.formatArtikelbezeichnung();
								}

								teilerledigteWelcheNichtVerschobenWurden
										.add(getTextRespectUISpr("auft.terminverschieben.error.auftragsposition",
												theClientDto.getMandant(), theClientDto.getLocUi()) + " "
												+ getAuftragpositionFac().getPositionNummer(auftPos.getIId()) + " "
												+ artikel);

							} else {

								getAuftragpositionFac().updateAuftragposition(auftPos, theClientDto);
							}
						}

					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (bMehrereAuftraege == false) {

				LosDto[] losDtos = getFertigungFac().losFindByAuftragIId(auftragIId);
				for (int i = 0; i < losDtos.length; i++) {

					if (losDtos[i].getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)
							|| losDtos[i].getStatusCNr().equals(LocaleFac.STATUS_AUSGEGEBEN)
							|| losDtos[i].getStatusCNr().equals(LocaleFac.STATUS_IN_PRODUKTION)
							|| losDtos[i].getStatusCNr().equals(LocaleFac.STATUS_TEILERLEDIGT)) {

						if (losDtos[i].getStatusCNr().equals(LocaleFac.STATUS_TEILERLEDIGT)) {

							teilerledigteWelcheNichtVerschobenWurden.add(
									getTextRespectUISpr("lp.los", theClientDto.getMandant(), theClientDto.getLocUi())
											+ " " + losDtos[i].getCNr());
						} else {

							getFertigungFac().terminVerschieben(losDtos[i].getIId(), new Timestamp(
									Helper.addiereTageZuDatum(losDtos[i].getTProduktionsbeginn(), iTageDiff).getTime()),
									new Timestamp(Helper.addiereTageZuDatum(losDtos[i].getTProduktionsende(), iTageDiff)
											.getTime()),
									theClientDto);
						}

					}

				}
			}
		}
		return teilerledigteWelcheNichtVerschobenWurden;

	}

	public void uebersteuereIntelligenteZwischensumme(Integer auftragpositionIId,
			BigDecimal bdBetragInBelegwaehrungUebersteuert, TheClientDto theClientDto) {

		try {

			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_RUNDUNGSAUSGLEICH_ARTIKEL);
			String artikelCnr = parameterDto.getCWert();
			ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrOhneExc(artikelCnr, theClientDto);
			if (null == artikelDto) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT,
						"Rundungsartikel nicht definiert");
			}

			AuftragpositionDto apDto = getAuftragpositionFac().auftragpositionFindByPrimaryKey(auftragpositionIId);

			AuftragDto abDto = auftragFindByPrimaryKey(apDto.getBelegIId());

			if (apDto.getZwsNettoSumme() == null) {
				berechneBeleg(abDto.getIId(), theClientDto);
				apDto = getAuftragpositionFac().auftragpositionFindByPrimaryKey(auftragpositionIId);
			}

			AuftragpositionDto[] apDtos = getAuftragpositionFac().auftragpositionFindByAuftrag(abDto.getIId());

			int iNachkommastellenPreis = getUINachkommastellenPreisVK(theClientDto.getMandant());

			if (apDto.isIntelligenteZwischensumme() && bdBetragInBelegwaehrungUebersteuert != null
					&& apDto.getZwsNettoSumme() != null && apDto.getZwsNettoSumme().doubleValue() != 0) {

				BigDecimal bdSumme = apDto.getZwsNettoSumme();

				BigDecimal faktor = bdBetragInBelegwaehrungUebersteuert.divide(bdSumme, 10, BigDecimal.ROUND_HALF_EVEN);

				ArrayList<BelegpositionVerkaufDto> alPos = getBelegVerkaufFac().getIntZwsPositions(apDto, apDtos);

				Integer mwstsatzIIdErstePosition = alPos.get(0).getMwstsatzIId();

				MwstsatzDto mwstsatzDtoErstePosition = getMandantFac()
						.mwstsatzFindByPrimaryKey(mwstsatzIIdErstePosition, theClientDto);

				BigDecimal bdNettosummeNeu = BigDecimal.ZERO;

				for (int i = 0; i < alPos.size(); i++) {

					AuftragpositionDto bvDto = (AuftragpositionDto) alPos.get(i);

					if (bvDto.getMwstsatzIId() != null && !artikelDto.getIId().equals(bvDto.getArtikelIId())) {

						MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
								.mwstsatzFindByPrimaryKey(bvDto.getMwstsatzIId(), theClientDto);

						bvDto.setBNettopreisuebersteuert(Helper.boolean2Short(true));
						bvDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));

						BigDecimal bdPreisNeu = Helper.rundeKaufmaennisch(bvDto.getNNettoeinzelpreis().multiply(faktor),
								iNachkommastellenPreis);

						bvDto.setNNettoeinzelpreis(bdPreisNeu);

						bvDto.setFZusatzrabattsatz(0D);

						BigDecimal bdRabattsumme = bvDto.getNEinzelpreis().subtract(bdPreisNeu);

						Double rabbattsatz = new Double(
								Helper.getProzentsatzBD(bvDto.getNEinzelpreis(), bdRabattsumme, 4).doubleValue());
						bvDto.setFRabattsatz(rabbattsatz);
						bvDto.setNRabattbetrag(bdRabattsumme);

						BigDecimal mwstBetrag = bdPreisNeu
								.multiply(new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz()).movePointLeft(2));
						bvDto.setNMwstbetrag(mwstBetrag);
						bvDto.setNBruttoeinzelpreis(bdPreisNeu.add(mwstBetrag));

						getAuftragpositionFac().updateAuftragposition(bvDto, theClientDto);

						bdNettosummeNeu = bdNettosummeNeu.add(bdPreisNeu.multiply(bvDto.getNMenge()));
					}

				}

				// Wenn rundungsdifferenz auftritt, Rundungsartikel einfuegen

				BigDecimal diff = bdBetragInBelegwaehrungUebersteuert.subtract(bdNettosummeNeu);

				AuftragpositionDto positionDtoRundungsartikel = null;
				for (int i = 0; i < alPos.size(); i++) {
					AuftragpositionDto bvDto = (AuftragpositionDto) alPos.get(i);
					if (artikelDto.getIId().equals(bvDto.getArtikelIId())) {
						positionDtoRundungsartikel = bvDto;
					}
				}

				if (diff.doubleValue() != 0) {

					if (positionDtoRundungsartikel == null) {
						positionDtoRundungsartikel = new AuftragpositionDto();

						positionDtoRundungsartikel.setBelegIId(abDto.getIId());
						positionDtoRundungsartikel.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
						positionDtoRundungsartikel.setArtikelIId(artikelDto.getIId());
						positionDtoRundungsartikel.setNMenge(BigDecimal.ONE);
						positionDtoRundungsartikel.setEinheitCNr(artikelDto.getEinheitCNr());

						positionDtoRundungsartikel.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
						positionDtoRundungsartikel.setTUebersteuerbarerLiefertermin(abDto.getDLiefertermin());

						positionDtoRundungsartikel.setArtikelIId(artikelDto.getIId());
						positionDtoRundungsartikel.setBNettopreisuebersteuert(Helper.boolean2Short(true));
						positionDtoRundungsartikel.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
						positionDtoRundungsartikel.setFRabattsatz(0D);
						positionDtoRundungsartikel.setFZusatzrabattsatz(0D);
						positionDtoRundungsartikel.setNRabattbetrag(BigDecimal.ZERO);
					}

					positionDtoRundungsartikel.setNEinzelpreis(diff);

					positionDtoRundungsartikel.setNNettoeinzelpreis(diff);

					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindZuDatum(mwstsatzDtoErstePosition.getIIMwstsatzbezId(), abDto.getTBelegdatum());
					positionDtoRundungsartikel.setMwstsatzIId(mwstsatzDto.getIId());
					BigDecimal mwstBetrag = diff.multiply(new BigDecimal(mwstsatzDto.getFMwstsatz()).movePointLeft(2));
					positionDtoRundungsartikel.setNMwstbetrag(mwstBetrag);
					positionDtoRundungsartikel.setNBruttoeinzelpreis(diff.add(mwstBetrag));

					if (positionDtoRundungsartikel.getIId() == null) {

						Integer iSort = apDto.getISort();

						getAuftragpositionFac().sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(abDto.getIId(),
								iSort);

						positionDtoRundungsartikel.setISort(iSort);

						apDto = getAuftragpositionFac().auftragpositionFindByPrimaryKey(auftragpositionIId);

						Integer agposIIdNeu = getAuftragpositionFac().createAuftragposition(positionDtoRundungsartikel,
								theClientDto);
						apDto.setZwsBisPosition(agposIIdNeu);
					} else {
						getAuftragpositionFac().updateAuftragposition(positionDtoRundungsartikel, theClientDto);
					}

				} else {
					if (positionDtoRundungsartikel != null) {

						Integer angebotspositionIId_LetzterNomalerArtikel = null;

						for (int i = alPos.size(); i > 0; i--) {
							AngebotpositionDto bvDto = (AngebotpositionDto) alPos.get(i - 1);
							if (!artikelDto.getIId().equals(bvDto.getArtikelIId())) {
								angebotspositionIId_LetzterNomalerArtikel = bvDto.getIId();
								break;
							}
						}

						if (angebotspositionIId_LetzterNomalerArtikel != null) {
							getAuftragpositionFac().removeAuftragposition(positionDtoRundungsartikel, theClientDto);
							apDto.setZwsBisPosition(angebotspositionIId_LetzterNomalerArtikel);
						} else {
							positionDtoRundungsartikel.setNEinzelpreis(BigDecimal.ZERO);
							positionDtoRundungsartikel.setNNettoeinzelpreis(BigDecimal.ZERO);
							positionDtoRundungsartikel.setNMwstbetrag(BigDecimal.ZERO);
							positionDtoRundungsartikel.setNBruttoeinzelpreis(BigDecimal.ZERO);

							getAuftragpositionFac().updateAuftragposition(positionDtoRundungsartikel, theClientDto);
						}

					}
				}

				apDto.setZwsNettoSumme(null);
				getAuftragpositionFac().updateAuftragposition(apDto, theClientDto);
			} else if (apDto.isIntelligenteZwischensumme() && bdBetragInBelegwaehrungUebersteuert != null
					&& apDto.getZwsNettoSumme() != null && apDto.getZwsNettoSumme().doubleValue() == 0) {
				throwExceptionZwischensummeNull();
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	/**
	 * Einen Auftrag manuell auf 'Erledigt' setzen.
	 * 
	 * @param iIdAuftragI  PK des Auftrags
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void manuellErledigen(Integer iIdAuftragI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		myLogger.logData(iIdAuftragI);
		try {
			Auftrag auftrag = em.find(Auftrag.class, iIdAuftragI);
			if (auftrag == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (auftrag.getAuftragstatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
					|| auftrag.getAuftragstatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {

				// PJ18288
				// Logging
				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
						theClientDto);
				String sMessage = personalDto.formatFixName1Name2() + " hat den Status von Auftrag " + auftrag.getCNr()
						+ " von Status " + auftrag.getAuftragstatusCNr() + " nach Status "
						+ AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT + " geaendert ";

				myLogger.logKritisch(sMessage);

				auftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
				auftrag.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
				auftrag.setTManuellerledigt(Helper.cutTimestamp(getTimestamp()));
				auftrag.setPersonalIIdErledigt(theClientDto.getIDPersonal());
				auftrag.setTErledigt(Helper.cutTimestamp(getTimestamp()));

				// eventuell vorhandene auftragreseriverungen zuruecknehmen
				// Bei Rahmenauftraegen gibt es keine Reservierungen.
				if (!auftrag.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					AuftragpositionDto[] posDtos = getAuftragpositionFac().auftragpositionFindByAuftrag(iIdAuftragI);
					for (int i = 0; i < posDtos.length; i++) {
						if (posDtos[i].isIdent()) {
							if (posDtos[i].getNMenge().compareTo(new BigDecimal(0)) != 0) {
								if (!posDtos[i].getAuftragpositionstatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {
									ArtikelreservierungDto artikelreservierungDto = getReservierungFac()
											.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
													LocaleFac.BELEGART_AUFTRAG, posDtos[i].getIId());
									if (artikelreservierungDto != null) {
										getReservierungFac().removeArtikelreservierung(artikelreservierungDto.getIId());
									}
								}
							}
						}
						// fuer Handartikel gibt es keine Reservierungen!
					}
				}
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception("Auftrag " + auftrag.getCNr()
						+ " kann nicht manuell erledigt werden, Status : " + auftrag.getAuftragstatusCNr()));
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

	public void toggleAuftragsfreigabe(Integer auftragIId, TheClientDto theClientDto) {
		if (getTheJudgeFac().hatRecht(RechteFac.RECHT_AUFT_DARF_AUFTRAEGE_FREIGEBEN, theClientDto)) {

			Auftrag auftrag = em.find(Auftrag.class, auftragIId);
			if (auftrag.getTAuftragsfreigabe() == null) {
				auftrag.setTAuftragsfreigabe(new Timestamp(System.currentTimeMillis()));
				auftrag.setPersonalIIdAuftragsfreigabe(theClientDto.getIDPersonal());

			} else {
				// Ruecknahme geht nur, wenn Angelegt
				if (auftrag.getAuftragstatusCNr().equals(LocaleFac.STATUS_ANGELEGT)) {
					auftrag.setPersonalIIdAuftragsfreigabe(null);
					auftrag.setTAuftragsfreigabe(null);
				}

			}
			em.merge(auftrag);
			em.flush();
		}
	}

	public void auftragFreigeben(Integer auftragIId, TheClientDto theClientDto) {
		Auftrag auftrag = em.find(Auftrag.class, auftragIId);
		auftrag.setTFreigabe(new Timestamp(System.currentTimeMillis()));
		auftrag.setPersonalIIdFreigabe(theClientDto.getIDPersonal());
		em.merge(auftrag);
		em.flush();
	}

	/**
	 * Den Status eines Auftrags von 'Erledigt' auf 'Erfasst' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param iIdAuftragI  PK des Auftrags
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void erledigungAufheben(Integer iIdAuftragI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		try {
			Auftrag auftrag = em.find(Auftrag.class, iIdAuftragI);
			if (auftrag == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (auftrag.getAuftragstatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				auftrag.setPersonalIIdManuellerledigt(null);
				auftrag.setTManuellerledigt(null);
				auftrag.setPersonalIIdErledigt(null);
				auftrag.setTErledigt(null);

				if (auftrag.getPersonalIIdManuellerledigt() == null && auftrag.getTManuellerledigt() == null) {
					auftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
					myLogger.logKritisch(
							"Status Erledigt wurde aufgehoben, obwohl der Auftrag nicht manuell erledigt wurde, AuftragIId: "
									+ iIdAuftragI);
				}

				auftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);

				AuftragpositionDto[] posDtos = null;
				posDtos = getAuftragpositionFac().auftragpositionFindByAuftrag(iIdAuftragI);
				boolean bOffen = false;
				for (int i = 0; i < posDtos.length; i++) {
					if (posDtos[i].isIdent()) {
						// Keine Reservierungen bei Rahmenauftraegen.
						if (!auftrag.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {

							// SP7604
							if (!posDtos[i].getAuftragpositionstatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {

								if (posDtos[i].getNOffeneMenge().doubleValue() > 0) {
									ArtikelreservierungDto reservierungDto = new ArtikelreservierungDto();
									reservierungDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
									reservierungDto.setIBelegartpositionid(posDtos[i].getIId());
									reservierungDto.setArtikelIId(posDtos[i].getArtikelIId());
									reservierungDto.setTLiefertermin(posDtos[i].getTUebersteuerbarerLiefertermin());
									reservierungDto.setNMenge(posDtos[i].getNOffeneMenge());
									getReservierungFac().createArtikelreservierung(reservierungDto);
								} else if (posDtos[i].getNOffeneMenge().doubleValue() < 0) {

									ArtikelreservierungDto reservierungDto = new ArtikelreservierungDto();
									reservierungDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
									reservierungDto.setIBelegartpositionid(posDtos[i].getIId());
									reservierungDto.setArtikelIId(posDtos[i].getArtikelIId());
									reservierungDto.setTLiefertermin(auftrag.getTFinaltermin());
									reservierungDto.setNMenge(posDtos[i].getNOffeneMenge());
									getReservierungFac().createArtikelreservierung(reservierungDto);

								}
							}

						}
						if (posDtos[i].getNMenge().equals(posDtos[i].getNOffeneMenge())) {
							bOffen = true;

							if (posDtos[i].getNOffeneMenge().doubleValue() > 0) {
								Auftragposition oPos = em.find(Auftragposition.class, posDtos[i].getIId());
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
					PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
					String sMessage = personalDto.formatFixName1Name2() + " hat den Status von Auftrag "
							+ auftrag.getCNr() + " von Status Erledigt nach Status " + auftrag.getAuftragstatusCNr()
							+ " geaendert ";

					myLogger.logKritisch(sMessage);

				}

				// PJ20800
				if (auftrag.getBestellungIIdAndererMandant() != null) {
					BestellungDto bsDto = getBestellungFac()
							.bestellungFindByPrimaryKey(auftrag.getBestellungIIdAndererMandant());
					ArrayList al = new ArrayList();
					al.add(bsDto.getCNr());

					if (bsDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZUGEHOERIGE_BESTELLUNG_IST_ERLEDIGT, al,
								new Exception("Zugehoerige Bestellung " + bsDto.getCNr() + " ist Erledigt "
										+ auftrag.getAuftragstatusCNr()));
					}
				}

			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
						new Exception("Die Erledigung des Auftrags kann nicht aufgehoben werden, Status: "
								+ auftrag.getAuftragstatusCNr()));
			}
		} catch (

		RemoteException ex) {
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
	 * @param iId Integer
	 * @throws EJBExceptionLP
	 * @return AuftragDto
	 */
	public AuftragDto auftragFindByPrimaryKey(Integer iId) {
		AuftragDto auftragDto = null;

		Auftrag auftrag = em.find(Auftrag.class, iId);
		if (auftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, iId.toString());
		}
		auftragDto = assembleAuftragDto(auftrag);

		return auftragDto;
	}

	/**
	 * Einen Auftrag ueber seinen PK holen. <br>
	 * Diese Methode wirft keine Exceptions.
	 * 
	 * @param iIdAuftragI PK des Auftrags
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

	public void mindermengenzuschlagEntfernen(AuftragDto auftragDto, TheClientDto theClientDto) {
		if (Helper.short2boolean(auftragDto.getBMindermengenzuschlag())) {
			// Vorhandene MMZ-Artikel entfernen
			try {
				AuftragpositionDto[] rePosDtos = getAuftragpositionFac()
						.auftragpositionFindByAuftrag(auftragDto.getIId());
				Query query = em.createNamedQuery("MmzByMandantCNr");
				query.setParameter(1, theClientDto.getMandant());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Mmz mmz = (Mmz) it.next();

					for (int i = 0; i < rePosDtos.length; i++) {
						if (rePosDtos[i].getArtikelIId() != null
								&& rePosDtos[i].getArtikelIId().equals(mmz.getArtikelIId())) {
							getAuftragpositionFac().removeAuftragposition(rePosDtos[i], theClientDto);
						}
					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
	}

	public Integer erzeugeAuftragpositionUeberSchnellanlage(Integer auftragIId, ArtikelDto artikelDto,
			PaneldatenDto[] paneldatenDtos, TheClientDto theClientDto) {

		AuftragDto auftragDto = null;

		try {

			auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);

			// Artikelnummer zusammenbauen

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_TRENNZEICHEN_ARTIKELGRUPPE_AUFTRAGSNUMMER);

			String trennzeichen = (String) parameter.getCWertAsObject();

			ArtgruDto agDto = getArtikelFac().artgruFindByPrimaryKey(artikelDto.getArtgruIId(), theClientDto);

			// PJ19371
			// Kostenstelle kommt aus Artikel

			if (agDto.getKostenstelleIId() != null) {
				auftragDto.setKostIId(agDto.getKostenstelleIId());
				updateAuftrag(auftragDto, null, theClientDto);
			}

			String artikelnummer = agDto.getCNr() + trennzeichen + auftragDto.getCNr();

			artikelDto.setCNr(artikelnummer);
			// Dann Artikel anlegen
			Integer artikelIId = getArtikelFac().createArtikel(artikelDto, theClientDto);

			// Dann Artikeleigenschaften anlegen
			for (int i = 0; i < paneldatenDtos.length; i++) {
				paneldatenDtos[i].setCKey(artikelIId + "");
			}
			getPanelFac().createPaneldaten(paneldatenDtos, theClientDto);

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdRechnungsadresse(),
					theClientDto);

			// Dann Auftragsposition anlegen
			AuftragpositionDto auftragpositionDto = new AuftragpositionDto();
			auftragpositionDto.setBelegIId(auftragIId);
			auftragpositionDto.setArtikelIId(artikelIId);
			auftragpositionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
			auftragpositionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
			auftragpositionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
			auftragpositionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
			auftragpositionDto.setNMenge(new BigDecimal(1));
			auftragpositionDto.setNOffeneMenge(new BigDecimal(1));
			auftragpositionDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
			auftragpositionDto.setFRabattsatz(new Double(0));

			auftragpositionDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
			/*
			 * MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
			 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kdDto.getMwstsatzbezIId(),
			 * theClientDto);
			 */
			MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(kdDto.getMwstsatzbezIId(),
					auftragDto.getTBelegdatum(), theClientDto);
			auftragpositionDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
			auftragpositionDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
			auftragpositionDto.setNEinzelpreis(new BigDecimal(0));
			auftragpositionDto.setNRabattbetrag(new BigDecimal(0));
			auftragpositionDto.setNNettoeinzelpreis(new BigDecimal(0));
			auftragpositionDto.setNEinzelpreisplusversteckteraufschlag(new BigDecimal(0));
			auftragpositionDto.setNNettoeinzelpreisplusversteckteraufschlag(new BigDecimal(0));
			auftragpositionDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(new BigDecimal(0));
			auftragpositionDto.setNMwstbetrag(new BigDecimal(0));
			auftragpositionDto.setNBruttoeinzelpreis(new BigDecimal(30));
			auftragpositionDto.setTUebersteuerbarerLiefertermin(auftragDto.getTBelegdatum());
			auftragpositionDto.setBDrucken(Helper.boolean2Short(false));
			auftragpositionDto.setFZusatzrabattsatz(new Double(0));
			auftragpositionDto.setISort(new Integer(1));

			getAuftragpositionFac().createAuftragposition(auftragpositionDto, theClientDto);

			// aufgrund PJ21830 auskommentiert
			// aktiviereAuftrag(auftragIId, theClientDto);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return auftragIId;
	}

	public AuftragDto auftragFindByMandantCNrCNr(String cNrMandantI, String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AuftragDto auftragDto = auftragFindByMandantCNrCNrOhneExc(cNrMandantI, cNrI);
		if (auftragDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception("cNrI=" + cNrI + " cNrMandantI=" + cNrMandantI));
		}

		return auftragDto;
	}

	public AuftragDto auftragFindByMandantCNrCNrOhneExc(String cNrMandantI, String cNrI) {
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
	 * Update eines bestehenden Auftrags mit den Werten aus dem Dto.* @param auftrag
	 * Auftrag* @param auftragDto AuftragDto* @throws EJBExceptionLP
	 */
	private void setAuftragFromAuftragDto(Auftrag auftrag, AuftragDto auftragDto) throws EJBExceptionLP {
		final String METHOD_NAME = "setAuftragFromAuftragDto";
		myLogger.entry();
		if (auftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("auftrag == null"));
		}
		if (auftragDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("auftragDto == null"));
		}

		auftrag.setAuftragIIdRahmenauftrag(auftragDto.getAuftragIIdRahmenauftrag());
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
			auftrag.setKundeIIdAuftragsadresse(auftragDto.getKundeIIdAuftragsadresse());
		}
		// der Ansprechpartner kann null sein
		auftrag.setAnsprechpartnerIIdKunde(auftragDto.getAnsprechparnterIId());

		if (auftragDto.getPersonalIIdVertreter() != null) {
			auftrag.setPersonalIIdVertreter(auftragDto.getPersonalIIdVertreter());
		}
		if (auftragDto.getKundeIIdLieferadresse() != null) {
			auftrag.setKundeIIdLieferadresse(auftragDto.getKundeIIdLieferadresse());
		}
		if (auftragDto.getKundeIIdRechnungsadresse() != null) {
			auftrag.setKundeIIdRechnungsadresse(auftragDto.getKundeIIdRechnungsadresse());
		}
		auftrag.setCBez(auftragDto.getCBezProjektbezeichnung());
		auftrag.setCBestellnummer(auftragDto.getCBestellnummer());
		auftrag.setTBestelldatum(auftragDto.getDBestelldatum());
		if (auftragDto.getCAuftragswaehrung() != null) {
			auftrag.setWaehrungCNrAuftragswaehrung(auftragDto.getCAuftragswaehrung());
		}
		if (auftragDto.getFWechselkursmandantwaehrungzubelegwaehrung() != null) {
			auftrag.setFWechselkursmandantwaehrungzuauftragswaehrung(
					auftragDto.getFWechselkursmandantwaehrungzubelegwaehrung());
		}
		/*
		 * if (auftragDto.getFSonderrabattsatz() != null) {
		 * auftrag.setFSonderrabattsatz(auftragDto.getFSonderrabattsatz()); }
		 */
		if (auftragDto.getDLiefertermin() != null) {
			auftrag.setTLiefertermin(auftragDto.getDLiefertermin());
		}
		if (auftragDto.getBLieferterminUnverbindlich() != null) {
			auftrag.setBLieferterminunverbindlich(auftragDto.getBLieferterminUnverbindlich());
		}
		if (auftragDto.getDFinaltermin() != null) {
			auftrag.setTFinaltermin(auftragDto.getDFinaltermin());
		}
		if (auftragDto.getKostIId() != null) {
			auftrag.setKostenstelleIId(auftragDto.getKostIId());
		}
		if (auftragDto.getBTeillieferungMoeglich() != null) {
			auftrag.setBTeillieferungmoeglich(auftragDto.getBTeillieferungMoeglich());
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
			auftrag.setFVersteckteraufschlag(auftragDto.getFVersteckterAufschlag());
		}
		if (auftragDto.getFAllgemeinerRabattsatz() != null) {
			auftrag.setFAllgemeinerrabattsatz(auftragDto.getFAllgemeinerRabattsatz());
		}
		if (auftragDto.getFProjektierungsrabattsatz() != null) {
			auftrag.setFProjektierungsrabattsatz(auftragDto.getFProjektierungsrabattsatz());
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
		auftrag.setNGesamtauftragswertinauftragswaehrung(auftragDto.getNGesamtauftragswertInAuftragswaehrung());

		auftrag.setNRohdeckunginmandantenwaehrung(auftragDto.getNRohdeckungInMandantenwaehrung());
		auftrag.setNRohdeckungaltinmandantenwaehrung(auftragDto.getNRohdeckungaltInMandantenwaehrung());
		auftrag.setNMaterialwertinmandantenwaehrung(auftragDto.getNMaterialwertInMandantenwaehrung());

		auftrag.setAuftragtextIIdKopftext(auftragDto.getAuftragIIdKopftext());
		auftrag.setXKopftextuebersteuert(auftragDto.getCKopftextUebersteuert());
		auftrag.setAuftragtextIIdFusstext(auftragDto.getAuftragIIdFusstext());
		auftrag.setXFusstextuebersteuert(auftragDto.getCFusstextUebersteuert());

		auftrag.setAnsprechpartnerIIdLieferadresse(auftragDto.getAnsprechpartnerIIdLieferadresse());

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
			auftrag.setPersonalIIdStorniert(auftragDto.getPersonalIIdStorniert());
		}
		if (auftragDto.getTStorniert() != null) {
			auftrag.setTStorniert(auftragDto.getTStorniert());
		}

		auftrag.setPersonalIIdAnlegen(auftragDto.getPersonalIIdAnlegen());
		auftrag.setTAnlegen(auftragDto.getTAnlegen());
		auftrag.setPersonalIIdAendern(auftragDto.getPersonalIIdAendern());
		auftrag.setTAendern(auftragDto.getTAendern());
		auftrag.setPersonalIIdManuellerledigt(auftragDto.getPersonalIIdManuellerledigt());
		if (auftragDto.getTManuellerledigt() != null) {
			auftrag.setTManuellerledigt(Helper.cutTimestamp(auftragDto.getTManuellerledigt()));
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
		auftrag.setAuftragwiederholungsintervallCNr(auftragDto.getWiederholungsintervallCNr());
		auftrag.setFErfuellungsgrad(auftragDto.getFErfuellungsgrad());

		auftrag.setAuftragbegruendungIId(auftragDto.getAuftragbegruendungIId());
		auftrag.setLagerIIdAbbuchungslager(auftragDto.getLagerIIdAbbuchungslager());
		auftrag.setAnsprechpartnerIIdRechnungsadresse(auftragDto.getAnsprechpartnerIIdRechnungsadresse());
		auftrag.setCLieferartort(auftragDto.getCLieferartort());
		auftrag.setProjektIId(auftragDto.getProjektIId());
		auftrag.setNKorrekturbetrag(auftragDto.getNKorrekturbetrag());
		auftrag.setTResponse(auftragDto.getTResponse());
		auftrag.setPersonalIIdResponse(auftragDto.getPersonalIIdResponse());

		auftrag.setTFreigabe(auftragDto.getTFreigabe());
		auftrag.setPersonalIIdFreigabe(auftragDto.getPersonalIIdFreigabe());

		auftrag.setNPraemie(auftragDto.getNPraemie());

		auftrag.setTWunschtermin(auftragDto.getTWunschtermin());
		auftrag.setTAenderungsauftrag(auftragDto.getTAenderungsauftrag());
		auftrag.setTLaufterminBis(auftragDto.getTLaufterminBis());

		auftrag.setBestellungIIdAndererMandant(auftragDto.getBestellungIIdAndererMandant());
		auftrag.setPersonalIIdAuftragsfreigabe(auftragDto.getPersonalIIdAuftragsfreigabe());
		auftrag.setTAuftragsfreigabe(auftragDto.getTAuftragsfreigabe());
		auftrag.setNIndexanpassung(auftragDto.getNIndexanpassung());
		auftrag.setVerrechenbarIId(auftragDto.getVerrechenbarIId());
		auftrag.setIVersion(auftragDto.getIVersion());
		auftrag.setLaenderartCnr(auftragDto.getLaenderartCnr());

		auftrag.setVerrechnungsmodellIId(auftragDto.getVerrechnungsmodellIId());

		auftrag.setBMindermengenzuschlag(auftragDto.getBMindermengenzuschlag());
		auftrag.setPersonalIIdVertreter2(auftragDto.getPersonalIIdVertreter2());

		auftrag.setCKommission(auftragDto.getCKommission());

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
	 * @param iIdAuftragI  pk des Auftrags
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void pruefeUndSetzeAuftragstatusBeiAenderung(Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Auftrag auftrag = null;
		auftrag = em.find(Auftrag.class, iIdAuftragI);
		if (auftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		String sStatus = auftrag.getAuftragstatusCNr();
		if (AuftragServiceFac.AUFTRAGART_RAHMEN.equals(auftrag.getAuftragartCNr())) {
			if (!sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
					&& !sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)
					&& !sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(sStatus));
			}
		} else {
			if (!sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
					&& !sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(sStatus));
			}
		}
		if (sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN) ||
		// MB 06.06.06 damit auch im status "Angelegt" alle Belegwert-Felder
		// null gesetzt werden
				sStatus.equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
			auftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
			auftrag.setNGesamtauftragswertinauftragswaehrung(null);
			auftrag.setNMaterialwertinmandantenwaehrung(null);
			if (getTheJudgeFac().hatRecht(RechteFac.RECHT_AUFT_DARF_AUFTRAEGE_FREIGEBEN, theClientDto)
					&& auftrag.getBestellungIIdAndererMandant() == null) {
				auftrag.setTAuftragsfreigabe(null);
				auftrag.setPersonalIIdAuftragsfreigabe(null);
			}

		}
	}

	/**
	 * AuftragpositionDto auf null pruefen.
	 * 
	 * @param auftragspositionDto AuftragpositionDto
	 * @throws EJBExceptionLP
	 */
	private void checkAuftragpositionDto(AuftragpositionDto auftragspositionDto) throws EJBExceptionLP {
		if (auftragspositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("auftragspositionDto == null"));
		}
	}

	/**
	 * Die Anrede fuer einen Kunden bauen.
	 * 
	 * @param iIdKundeI    PK des Kunden
	 * @param theClientDto der aktuelle Benutzer
	 * @throws Throwable
	 * @return String die Anrede
	 */
	private String baueAnredeKunde(Integer iIdKundeI, TheClientDto theClientDto) throws Throwable {
		String sAnredeKundeO = null;
		if (iIdKundeI != null) {
			KundeDto oKunde = getKundeFac().kundeFindByPrimaryKey(iIdKundeI, theClientDto);
			sAnredeKundeO = oKunde.getPartnerDto().formatAnrede();
		}
		return sAnredeKundeO;
	}

	/**
	 * Die Anschrift fuer einen Kunden bauen. <br>
	 * Es muss keine Anschrift geben.
	 * 
	 * @param iIdKundeI    PK des Kunden
	 * @param theClientDto der aktuelle Benutzer
	 * @throws Throwable
	 * @return String die Anschrift
	 */
	private String baueAnschriftKunde(Integer iIdKundeI, TheClientDto theClientDto) throws Throwable {
		String sAnschriftKunde = null;
		if (iIdKundeI != null) {
			KundeDto oKunde = getKundeFac().kundeFindByPrimaryKey(iIdKundeI, theClientDto);
			if (oKunde.getPartnerDto().getLandplzortDto() != null) {
				sAnschriftKunde = oKunde.getPartnerDto().getLandplzortDto().formatLandPlzOrt();
			}
		}
		return sAnschriftKunde;
	}

	/**
	 * Die Anrede fuer einen Ansprechpartner bauen. <br>
	 * Es muss keinen Ansprechpartner geben.
	 * 
	 * @param iIdAnsprechpartnerI pk des Ansprechpartners
	 * @param theClientDto        der aktuelle Benutzer
	 * @throws Throwable
	 * @return String die Anrede
	 */
	private String baueAnredeAnsprechpartner(Integer iIdAnsprechpartnerI, TheClientDto theClientDto) throws Throwable {
		String sAnredeAnsprechpartner = null;

		if (iIdAnsprechpartnerI != null) {
			AnsprechpartnerDto oAnsprechpartner = getAnsprechpartnerFac()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI, theClientDto);
			sAnredeAnsprechpartner = oAnsprechpartner.getPartnerDto().formatAnrede();
		}

		return sAnredeAnsprechpartner;
	}

	/**
	 * In der Auftrag Nachkalkulation wird eine extra Zeile geschrieben, wenn es auf
	 * einem Lieferschein mit Bezug zum relevanten Auftrag eine mengenbehaftete
	 * Position gibt, die im Auftrag nicht geplant war.
	 * 
	 * @param auftragDto               AuftragDto
	 * @param oLieferscheinpositionDto die Lieferscheinposition
	 * @param sAuftragswaehrungI       die Waehrung des aktuellen Auftrags
	 * @param theClientDto             der aktuelle Benutzer
	 * @return AuftragNachkalkulationDto der Inhalt der extra Zeile
	 * @throws EJBExceptionLP Ausnahme
	 */
	private AuftragNachkalkulationDto buildZeileZusaetzlicheLieferscheinposition(AuftragDto auftragDto,
			LieferscheinpositionDto oLieferscheinpositionDto, String sAuftragswaehrungI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AuftragNachkalkulationDto oNachkalkulationDto = new AuftragNachkalkulationDto(auftragDto);

		/*
		 * try { ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(
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
		 * sbArtikelInfo.append(oLieferscheinpositionDto.getCBezeichnung()); } else { if
		 * (oArtikelDto.getArtikelsprDto() != null &&
		 * oArtikelDto.getArtikelsprDto().getCBez() != null) { sbArtikelInfo.append
		 * ("\n").append(oArtikelDto.getArtikelsprDto().getCBez()); } }
		 * 
		 * if (oArtikelDto.getArtikelsprDto() != null &&
		 * oArtikelDto.getArtikelsprDto().getCZbez() != null) { sbArtikelInfo.append
		 * ("\n").append(oArtikelDto.getArtikelsprDto().getCZbez()); } } else if
		 * (oLieferscheinpositionDto.getLieferscheinpositionartCNr().equals(
		 * LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
		 * sbArtikelInfo.append(oArtikelDto.getArtikelsprDto().getCBez());
		 * 
		 * if (oArtikelDto.getArtikelsprDto().getCZbez() != null) { sbArtikelInfo
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
		 * oNachkalkulationDto.setSArtikelcnrbezeichnung(sbArtikelInfo.toString() );
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
		 * oNachkalkulationDto.setBdMengeist(oLieferscheinpositionDto.getNMenge() );
		 * 
		 * if (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT
		 * )) { oNachkalkulationDto.setBdArbeitpreisist(oLieferscheinpositionDto.
		 * getNNettogesamtpreisPlusVersteckterAufschlagMinusRabatt()); } else { // sonst
		 * Material oNachkalkulationDto.setBdMaterialpreisist(oLieferscheinpositionDto
		 * .getNNettogesamtpreisPlusVersteckterAufschlagMinusRabatt()); }
		 * 
		 * BigDecimal bdEinzelsummeist = oLieferscheinpositionDto.getNMenge().multiply(
		 * oLieferscheinpositionDto
		 * .getNNettogesamtpreisPlusVersteckterAufschlagMinusRabatt());
		 * 
		 * oNachkalkulationDto.setBdSummeist(bdEinzelsummeist);
		 * 
		 * // Gestehungspreise der Lieferscheinposition, es gilt das Lager des
		 * Lieferscheins LieferscheinDto oLieferscheinDto = getLieferscheinFac().
		 * lieferscheinFindByPrimaryKey(oLieferscheinpositionDto .getLieferscheinIId());
		 * 
		 * LagerDto oLagerDto =
		 * getLagerFac().lagerFindByPrimaryKey(oLieferscheinDto.getLagerIId());
		 * 
		 * // Grundlage ist der Gestehungspreis des Artikels am Lager des Lieferscheins
		 * BigDecimal bdGestehungspreisIst = getLagerFac().getGestehungspreis(
		 * oArtikelDto.getIId(), oLagerDto.getIId());
		 * 
		 * // umrechnen in Auftragwaehrung bdGestehungspreisIst =
		 * getLocaleFac().rechneUmInAndereWaehrung( bdGestehungspreisIst, Currency
		 * .getInstance(getTheClient(sUserI).getLocMandant()).getCurrencyCode(),
		 * sAuftragswaehrungI);
		 * 
		 * if (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT
		 * )) { oNachkalkulationDto.setBdGestpreisarbeitist(bdGestehungspreisIst); }
		 * else { // sonst Material
		 * oNachkalkulationDto.setBdGestpreismaterialist(bdGestehungspreisIst); }
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
		 * (oLieferscheinpositionDto.getNMenge())); } catch(Throwable t) { throw new
		 * EJBExceptionLP(EJBExceptionLP.FEHLER, t); }
		 */

		return oNachkalkulationDto;
	}

	public ArrayList<KundeDto> getRechnungsadressenEinerAuftragsadresseSortiertNachHaeufigkeit(
			Integer kundeIIdAuftragsadresse, TheClientDto theClientDto) {

		HashMap<Integer, Integer> anzahlRechnungsadressen = new HashMap<Integer, Integer>();

		Query query = em.createNamedQuery("AuftragfindByKundeIIdAuftragsadresseMandantCNr");
		query.setParameter(1, kundeIIdAuftragsadresse);
		query.setParameter(2, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Auftrag auftrag = (Auftrag) it.next();

			if (anzahlRechnungsadressen.containsKey(auftrag.getKundeIIdRechnungsadresse())) {
				Integer iAnzahl = anzahlRechnungsadressen.get(auftrag.getKundeIIdRechnungsadresse());
				iAnzahl++;
				anzahlRechnungsadressen.put(auftrag.getKundeIIdRechnungsadresse(), iAnzahl);
			} else {
				anzahlRechnungsadressen.put(auftrag.getKundeIIdRechnungsadresse(), new Integer(1));
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
			kunden.add(getKundeFac().kundeFindByPrimaryKey(keyGroessteAnzahl, theClientDto));

		}

		return kunden;
	}

	public ArrayList<KundeDto> getLieferadressenEinerRechnungsadresseSortiertNachHaeufigkeit(
			Integer kundeIIdRechnungsadresse, TheClientDto theClientDto) {

		HashMap<Integer, Integer> anzahlLieferadressen = new HashMap<Integer, Integer>();

		Query query = em.createNamedQuery("AuftragfindByKundeIIdRechnungsadresseMandantCNr");
		query.setParameter(1, kundeIIdRechnungsadresse);
		query.setParameter(2, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Auftrag auftrag = (Auftrag) it.next();

			if (anzahlLieferadressen.containsKey(auftrag.getKundeIIdLieferadresse())) {
				Integer iAnzahl = anzahlLieferadressen.get(auftrag.getKundeIIdLieferadresse());
				iAnzahl++;
				anzahlLieferadressen.put(auftrag.getKundeIIdLieferadresse(), iAnzahl);
			} else {
				anzahlLieferadressen.put(auftrag.getKundeIIdLieferadresse(), new Integer(1));
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
			kunden.add(getKundeFac().kundeFindByPrimaryKey(keyGroessteAnzahl, theClientDto));

		}

		return kunden;
	}

	private String getArtikelbezeichnung(AuftragpositionDto oPositionDtoI, ArtikelDto oArtikelDtoI) throws Throwable {
		StringBuffer sbArtikelInfo = new StringBuffer();

		if (oPositionDtoI.isIdent()) {
			sbArtikelInfo.append(oArtikelDtoI.getCNr());

			if (oPositionDtoI.getCBez() != null) {
				sbArtikelInfo.append(oPositionDtoI.getCBez());
			} else {
				if (oArtikelDtoI.getArtikelsprDto().getCBez() != null) {
					sbArtikelInfo.append("\n").append(oArtikelDtoI.getArtikelsprDto().getCBez());
				}
			}

			if (oArtikelDtoI.getArtikelsprDto().getCZbez() != null) {
				sbArtikelInfo.append("\n").append(oArtikelDtoI.getArtikelsprDto().getCZbez());
			}
		} else if (oPositionDtoI.isHandeingabe()) {
			sbArtikelInfo.append(oArtikelDtoI.getArtikelsprDto().getCBez());

			if (oArtikelDtoI.getArtikelsprDto().getCZbez() != null) {
				sbArtikelInfo.append("\n").append(oArtikelDtoI.getArtikelsprDto().getCZbez());
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
	 * @param iIdAuftragI  pk des Auftrags
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @return BigDecimal der Materialwert in der gewuenschten Waehrung
	 */
	public BigDecimal berechneMaterialwertGesamt(Integer iIdAuftragI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		BigDecimal materialwert = new BigDecimal(0);
		try {
			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);

			AuftragDto aDto = auftragFindByPrimaryKey(iIdAuftragI);

			// das Hauptlager des Mandanten bestimmen
			LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(aDto.getLagerIIdAbbuchungslager());
			for (int i = 0; i < aAuftragpositionDto.length; i++) {
				AuftragpositionDto auftragpositionDto = aAuftragpositionDto[i];

				// alle positiven mengenbehafteten Positionen beruecksichtigen
				if (auftragpositionDto.getNMenge() != null && auftragpositionDto.getNMenge().doubleValue() > 0
						&& !auftragpositionDto.isPosition()) {

					if (auftragpositionDto.isIntelligenteZwischensumme()) {
						continue;
					}

					String typ = getAuftragReportFac().getArtikelsetType(auftragpositionDto);
					if (typ != null && typ.equals(ArtikelFac.SETARTIKEL_TYP_KOPF)) {
						continue;
					}

					ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId(),
							theClientDto);

					// SP4965
					if (!oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {

						BigDecimal menge = auftragpositionDto.getNMenge();

						// Grundlage ist der Gestehungspreis des Artikels am
						// Hauptlager des Mandanten
						BigDecimal gestehungspreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
								auftragpositionDto.getArtikelIId(), lagerDto.getIId(), theClientDto);

						// SP 4965 Wenn Gestpreis 0, dann Lief1Preis verwenden
						ArtikellieferantDto dto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
								auftragpositionDto.getArtikelIId(), new BigDecimal(1),
								theClientDto.getSMandantenwaehrung(), theClientDto);
						if (dto != null) {
							if (dto.getNNettopreis() != null) {
								gestehungspreis = dto.getNNettopreis();
							}
						}

						BigDecimal wertDerPosition = menge.multiply(gestehungspreis);

						materialwert = materialwert.add(wertDerPosition);
					}
				}
			}

			// checknumberformat: 1 die Nachkommastellen kappen
			materialwert = Helper.rundeKaufmaennisch(materialwert, 4);
			// checknumberformat: 2 passt der Wert in die DB?
			checkNumberFormat(materialwert);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(t));
		}

		return materialwert;
	}

	public BigDecimal berechneOffenenWertEinesAuftrags(String auftragCNr, TheClientDto theClientDto) {

		BigDecimal bdOffenerWert = BigDecimal.ZERO;

		AuftragDto aDto = getAuftragFac().auftragFindByMandantCNrCNrOhneExc(theClientDto.getMandant(), auftragCNr);
		if (aDto != null) {
			AuftragpositionDto[] dtos = getAuftragpositionFac()
					.auftragpositionFindByAuftragIIdNMengeNotNull(aDto.getIId(), theClientDto);
			for (int i = 0; i < dtos.length; i++) {

				AuftragpositionDto apDto = dtos[i];

				BigDecimal nPreisInBelegwaehrung = apDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();
				BigDecimal wechselkursmandantwaehrungzuauftragswaehrung = null;
				if (aDto.getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue() != 0) {
					wechselkursmandantwaehrungzuauftragswaehrung = new BigDecimal(
							aDto.getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue());
				} else {
					wechselkursmandantwaehrungzuauftragswaehrung = new BigDecimal(1);
				}
				nPreisInBelegwaehrung = getBetragMalWechselkurs(nPreisInBelegwaehrung,
						Helper.getKehrwert(wechselkursmandantwaehrungzuauftragswaehrung));

				BigDecimal bdOffeneMenge = apDto.getNMenge();

				if (aDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					bdOffeneMenge = apDto.getNOffeneRahmenMenge();
				} else {
					// SP7309 Offene Menge zum Stichtag berechnen

					Session session = FLRSessionFactory.getFactory().openSession();
					Criteria critLSPOS = session.createCriteria(FLRLieferscheinposition.class);
					// nur auftragsbezogene.
					critLSPOS.add(Restrictions
							.isNotNull(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRPOSITIONENSICHTAUFTRAG));
					// und nur die, die sich auf den aktuellen Auftrag beziehen.
					critLSPOS.createCriteria(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRPOSITIONENSICHTAUFTRAG)
							.add(Restrictions.eq("i_id", apDto.getIId()));

					List<?> resultList = critLSPOS.list();

					for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
						FLRLieferscheinposition itemLlpos = (FLRLieferscheinposition) iter.next();
						// Den LS brauchen wir auch, wegen stichtag und der
						// eventuellen waehrungsumrechnung
						FLRLieferschein lieferschein = itemLlpos.getFlrlieferschein();
						// stichtag pruefen

						// mengenbehaftet?
						if (itemLlpos.getN_menge() != null) {

							bdOffeneMenge = bdOffeneMenge.subtract(itemLlpos.getN_menge());

						}

					}
					session = FLRSessionFactory.getFactory().openSession();
					Criteria rePosCcrit = session.createCriteria(FLRRechnungPosition.class);
					// nur auftragsbezogene.
					rePosCcrit.add(Restrictions.isNotNull(RechnungFac.FLR_RECHNUNGPOSITION_FLRPOSITIONENSICHTAUFTRAG));
					// und nur die, die sich auf den aktuellen Auftrag beziehen.
					rePosCcrit.createCriteria(RechnungFac.FLR_RECHNUNGPOSITION_FLRPOSITIONENSICHTAUFTRAG)
							.add(Restrictions.eq("i_id", apDto.getIId()));

					resultList = rePosCcrit.list();

					for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
						FLRRechnungPosition itemRepos = (FLRRechnungPosition) iter.next();
						// preis- und mengenbehaftet?
						if (itemRepos.getN_menge() != null) {
							bdOffeneMenge = bdOffeneMenge.subtract(itemRepos.getN_menge());

						}

					}
				}

				bdOffenerWert = bdOffenerWert.add(bdOffeneMenge.multiply(nPreisInBelegwaehrung));

			}

		} else {
			return new BigDecimal(-1);
		}

		return bdOffenerWert;

	}

	/**
	 * Fuer eine bestimmte Auftragsart fuer einen bestimmten Zeitraum und eine
	 * bestimmte Terminart (Belegdatum, Liefertermin, Finaltermin) den
	 * Nettoauftragswert in Abhaengigkeit vom aktuellen Mandanten bestimmen. <br>
	 * Beruecksichtigt werden koennen entweder alle offenen Auftraege oder alle
	 * eingegangenen Auftraege.
	 * 
	 * @param cNrAuftragartI         die Auftragart (Frei, Rahmen, Abruf)
	 * @param whichKriteriumI        welche Zeitraumart (Belegdatum, Liefertermin,
	 *                               Finaltermin)
	 * @param gcBerechnungsdatumVonI ab diesem Datum
	 * @param gcBerechnungsdatumBisI bis zu diesem Datum
	 * @param offenOderEingegangenI  sollen alle offenen oder alle eingegangengen
	 *                               Auftraegr beruecksichtigt werden
	 * @param theClientDto           der aktuelle Benutzer
	 * @return BigDecimal der Nettoauftragswert, 0 wenn keine offenen Positionen
	 *         gefunden wurden
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BigDecimal berechneSummeAuftragsnettowert(String cNrAuftragartI, String whichKriteriumI,
			GregorianCalendar gcBerechnungsdatumVonI, GregorianCalendar gcBerechnungsdatumBisI,
			String offenOderEingegangenI, TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal nSummeAuftragsnettowert = new BigDecimal(0);
		try {

			// SP3921

			String sQuery = "SELECT ap FROM FLRAuftragpositionReport ap WHERE ap.flrauftrag.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";
			// Einschraenken nach Auftragart
			sQuery += " AND ap.flrauftrag.flrverrechenbar.b_verrechenbar=1 AND ap.flrauftrag.auftragart_c_nr='"
					+ cNrAuftragartI + "'";

			if (whichKriteriumI.equals(AuftragFac.KRIT_BELEGDATUM)) {
				// Belegdatum von bis: flrauftrag.t_belegdatum
				if (gcBerechnungsdatumVonI != null) {
					sQuery += " AND ap.flrauftrag.t_belegdatum>='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(gcBerechnungsdatumVonI.getTimeInMillis()))
							+ "'";
				}

				if (gcBerechnungsdatumBisI != null) {
					sQuery += " AND ap.flrauftrag.t_belegdatum<'"
							+ Helper.formatDateWithSlashes(new java.sql.Date(gcBerechnungsdatumBisI.getTimeInMillis()))
							+ "'";
				}
			} else if (whichKriteriumI.equals(AuftragFac.KRIT_LIEFERTERMIN)) {
				// Liefertermin von bis: flrauftrag.t_liefertermin

				if (gcBerechnungsdatumVonI != null) {
					sQuery += " AND ap.t_uebersteuerterliefertermin>='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(gcBerechnungsdatumVonI.getTimeInMillis()))
							+ "'";
				}

				if (gcBerechnungsdatumBisI != null) {
					sQuery += " AND ap.t_uebersteuerterliefertermin<'"
							+ Helper.formatDateWithSlashes(new java.sql.Date(gcBerechnungsdatumBisI.getTimeInMillis()))
							+ "'";
				}

			} else if (whichKriteriumI.equals(AuftragFac.KRIT_FINALTERMIN)) {
				// Belegdatum von bis: flrauftrag.t_finaltermin

				if (gcBerechnungsdatumVonI != null) {
					sQuery += " AND ap.flrauftrag.t_finaltermin>='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(gcBerechnungsdatumVonI.getTimeInMillis()))
							+ "'";
				}

				if (gcBerechnungsdatumBisI != null) {
					sQuery += " AND ap.flrauftrag.t_finaltermin<'"
							+ Helper.formatDateWithSlashes(new java.sql.Date(gcBerechnungsdatumBisI.getTimeInMillis()))
							+ "'";
				}

			}

			// Einschraenken nach Auftragstatus
			if (offenOderEingegangenI.equals(AuftragFac.AUFT_UMSATZUEBERSICHT_OFFEN)) {

				sQuery += " AND ap.flrauftrag.auftragstatus_c_nr in ('" + AuftragServiceFac.AUFTRAGSTATUS_OFFEN + "','"
						+ AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT + "')";

				sQuery += "AND ap.n_offenemenge IS NOT NULL AND  ap.n_offenemenge >0";

			} else if (offenOderEingegangenI.equals(AuftragFac.AUFT_UMSATZUEBERSICHT_EINGANG)) {
				sQuery += " AND ap.flrauftrag.auftragstatus_c_nr not in ('" + AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT
						+ "','" + AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')";

				sQuery += "AND ap.n_menge IS NOT NULL";

			}
			// sQuery += " ORDER BY
			// ap.flrauftrag.flrkunde.flrpartner.c_name1nachnamefirmazeile1 ASC,
			// ap.flrauftrag.c_nr ASC";
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> listPositionen = query.list();
			Iterator<?> it2 = listPositionen.iterator();

			while (it2.hasNext()) {
				FLRAuftragpositionReport flrauftragposition = (FLRAuftragpositionReport) it2.next();

				if (!flrauftragposition.getAuftragpositionart_c_nr()
						.equals(AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME)) {

					if (offenOderEingegangenI.equals(AuftragFac.AUFT_UMSATZUEBERSICHT_OFFEN)) {

						BigDecimal offeneMenge = flrauftragposition.getN_offenemenge();
						if (cNrAuftragartI.equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
							offeneMenge = flrauftragposition.getN_offenerahmenmenge();
						}

						if (!flrauftragposition.getAuftragpositionstatus_c_nr()
								.equals(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)
								&& offeneMenge.doubleValue() > 0) {
							BigDecimal bdBeitragDieserPosition = flrauftragposition
									.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte().multiply(offeneMenge);

							// Umrechnen des Beitrags in Mandantenwaehrung
							Double ddWechselkursReziprok = flrauftragposition.getFlrauftrag()
									.getF_wechselkursmandantwaehrungzuauftragswaehrung();

							if (ddWechselkursReziprok != null && ddWechselkursReziprok.doubleValue() != 1) {
								ddWechselkursReziprok = new Double(1 / ddWechselkursReziprok.doubleValue());

								bdBeitragDieserPosition = bdBeitragDieserPosition
										.multiply(new BigDecimal(ddWechselkursReziprok.doubleValue()));
							}

							bdBeitragDieserPosition = Helper.rundeKaufmaennisch(bdBeitragDieserPosition, 4);
							checkNumberFormat(bdBeitragDieserPosition);

							nSummeAuftragsnettowert = nSummeAuftragsnettowert.add(bdBeitragDieserPosition);
						}
					} else if (offenOderEingegangenI.equals(AuftragFac.AUFT_UMSATZUEBERSICHT_EINGANG)) {

						BigDecimal bdBeitragDieserPosition = flrauftragposition
								.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
								.multiply(flrauftragposition.getN_menge());

						// Umrechnen des Beitrags in Mandantenwaehrung
						Double ddWechselkursReziprok = flrauftragposition.getFlrauftrag()
								.getF_wechselkursmandantwaehrungzuauftragswaehrung();

						if (ddWechselkursReziprok != null && ddWechselkursReziprok.doubleValue() != 1) {
							ddWechselkursReziprok = new Double(1 / ddWechselkursReziprok.doubleValue());

							bdBeitragDieserPosition = bdBeitragDieserPosition
									.multiply(new BigDecimal(ddWechselkursReziprok.doubleValue()));
						}

						bdBeitragDieserPosition = Helper.rundeKaufmaennisch(bdBeitragDieserPosition, 4);
						checkNumberFormat(bdBeitragDieserPosition);

						nSummeAuftragsnettowert = nSummeAuftragsnettowert.add(bdBeitragDieserPosition);
					}
				}
			}

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
		}

		return nSummeAuftragsnettowert;
	}

	/**
	 * Berechne den Gesamtwert eines bestimmten Auftrags in der Auftragswaehrung.
	 * <br>
	 * Der Gesamtwert berechnet sich aus
	 * <p>
	 * Summe der Nettogesamtpreise der Positionen <br>
	 * + Versteckter Aufschlag <br>
	 * - Allgemeiner Rabatt <br>
	 * - Projektierungsrabatt <br>
	 * Beruecksichtigt werden alle mengenbehafteten Positionen.
	 * 
	 * @param aAuftragpositionDto
	 * @param theClientDto        der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @return BigDecimal der Gesamtwert des Auftrags
	 */

	public AuftragpositionDto[] entferneKalkulatorischeArtikel(AuftragpositionDto[] aAuftragpositionDto,
			TheClientDto theClientDto) {
		ArrayList al = new ArrayList();

		for (int i = 0; i < aAuftragpositionDto.length; i++) {

			if (aAuftragpositionDto[i].getArtikelIId() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(aAuftragpositionDto[i].getArtikelIId(),
						theClientDto);
				if (Helper.short2boolean(aDto.getBKalkulatorisch())) {
					continue;
				}
			}
			al.add(aAuftragpositionDto[i]);
		}
		AuftragpositionDto[] returnArray = new AuftragpositionDto[al.size()];
		return (AuftragpositionDto[]) al.toArray(returnArray);

	}

	public BigDecimal berechneNettowertGesamt(Integer iIdAuftragI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		BigDecimal auftragswert = new BigDecimal(0);
		try {
			AuftragDto auftragDto = auftragFindByPrimaryKey(iIdAuftragI);

			if (auftragDto.getNGesamtauftragswertInAuftragswaehrung() == null
					|| auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
				// Step 1: Wenn der Gesamtauftragswert NULL und der Status
				// ANGELEGT ist, dann den Wert aus den Positionen berechnen
				AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByAuftrag(iIdAuftragI);

				aAuftragpositionDto = entferneKalkulatorischeArtikel(aAuftragpositionDto, theClientDto);

				Set<Integer> modifiedPositions = getBelegVerkaufFac().prepareIntZwsPositions(aAuftragpositionDto,
						auftragDto);

				auftragswert = getBelegVerkaufFac().getGesamtwertinBelegwaehrung(aAuftragpositionDto, auftragDto);
				getBelegVerkaufFac().adaptIntZwsPositions(aAuftragpositionDto);
				getBelegVerkaufFac().saveIntZwsPositions(aAuftragpositionDto, modifiedPositions, Auftragposition.class);

				/*
				 * for (int i = 0; i < aAuftragpositionDto.length; i++) { AuftragpositionDto
				 * auftragpositionDto = aAuftragpositionDto[i];
				 * 
				 * // alle mengenbehafteten Positionen beruecksichtigen if
				 * (auftragpositionDto.getNMenge() != null) {
				 * 
				 * BigDecimal menge = auftragpositionDto.getNMenge();
				 * 
				 * // Grundlage ist der NettogesamtwertPlusVersteckterAufschlagMinusRabatte der
				 * Position in Auftragswaehrung // BigDecimal wertDerPosition =
				 * menge.multiply(auftragpositionDto. //
				 * getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()); BigDecimal
				 * wertDerPosition = menge.multiply(auftragpositionDto.
				 * getNNettoeinzelpreisplusversteckteraufschlag()); auftragswert =
				 * auftragswert.add(wertDerPosition); } } // - Allgemeiner Rabatt BigDecimal
				 * bdAllgemeinerRabattSatz = new
				 * BigDecimal(auftragDto.getFAllgemeinerRabattsatz().
				 * doubleValue()).movePointLeft(2); bdAllgemeinerRabattSatz =
				 * Helper.rundeKaufmaennisch(bdAllgemeinerRabattSatz, 4);
				 * 
				 * BigDecimal bdAllgemeinerRabatt =
				 * auftragswert.multiply(bdAllgemeinerRabattSatz); // auf 4 Stellen runden
				 * bdAllgemeinerRabatt = Helper.rundeKaufmaennisch(bdAllgemeinerRabatt, 4);
				 * 
				 * auftragswert=auftragswert.subtract(bdAllgemeinerRabatt);
				 * 
				 * // - Projektierungsrabatt BigDecimal bdProjektierungsRabatt = new
				 * BigDecimal(auftragDto. getFProjektierungsrabattsatz().doubleValue
				 * ()).movePointLeft(2); bdProjektierungsRabatt =
				 * Helper.rundeKaufmaennisch(bdProjektierungsRabatt, 4);
				 * 
				 * BigDecimal bdNettogesamtpreisProjektierungsrabattSumme =
				 * auftragswert.multiply( bdProjektierungsRabatt); // auf 4 Stellen runden
				 * bdNettogesamtpreisProjektierungsrabattSumme = Helper.rundeKaufmaennisch(
				 * bdNettogesamtpreisProjektierungsrabattSumme, 4); auftragswert =
				 * auftragswert.subtract(bdNettogesamtpreisProjektierungsrabattSumme );
				 */
				for (AuftragpositionDto auftragPositionDto : aAuftragpositionDto) {
					if (auftragPositionDto.isIntelligenteZwischensumme()) {
						auftragPositionDto.setNOffeneMenge(auftragPositionDto.getNMenge());
						getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(auftragPositionDto,
								theClientDto);
					}
				}
			} else if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
				// Step 2: Wenn der status STORNIERT ist, 0 zurueckgeben obwohl
				// der Auftragswert noch in der Tabelle steht
				auftragswert = Helper.getBigDecimalNull();
			} else if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
					|| auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)
					|| auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				// Step 3: Wenn der status OFFEN, TEILERLEDIGT oder ERLEDIGT
				// ist, den Auftragswert aus der Tabelle holen
				if (auftragDto.getNGesamtauftragswertInAuftragswaehrung() != null) {
					auftragswert = auftragDto.getNGesamtauftragswertInAuftragswaehrung();
				}
			}

			// Step 4: Der Wert muss in unsere Datenbank passen, fuer decimal(9)
			// gilt 15,4
			auftragswert = Helper.rundeKaufmaennisch(auftragswert, 4);
			checkNumberFormat(auftragswert);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(t));
		}

		return auftragswert;
	}

	/**
	 * Berechnet den Nettoauftragswert fuer einen Kunden in einem bestimmten
	 * Zeitintervall.
	 * 
	 * @param iIdKundeI  pk des Kunden
	 * @param datVonI    von diesem Datum weg inclusive diesem Datum
	 * @param datBisI    bis zu diesem Datum inclusive diesem Datum
	 * @param cCurrencyI die Zielwaehrung
	 * @throws EJBExceptionLP
	 * @return BigDecimal der Nettoauftragswert ohne Mwst
	 */
	public BigDecimal berechneGesamtwertAuftragProKundeProZeitintervall(Integer iIdKundeI, Date datVonI, Date datBisI,
			String cCurrencyI) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGesamtwertAuftragProKundeProZeitintervall";

		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iiKundeI == null"));
		}
		if (datVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("datVonI == null"));
		}
		if (datVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("datBisI == null"));
		}
		if (cCurrencyI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("cCurrencyI == null"));
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
			BigDecimal bdAuftragswert = auftrag.getNGesamtauftragswertinauftragswaehrung();

			// der auftragswert muss in die zielwaehrung umgerechnet werden
			// bdAuftragswert = getLocaleFac().
			// rechneUmInAndereWaehrung(bdAuftragswert,
			// auftrag.getCAuftragswaehrung(), cCurrencyI);

			bdAuftragswertSumme = bdAuftragswertSumme.add(bdAuftragswert);
		}
		return bdAuftragswertSumme;
	}

	/**
	 * Berechnet den Nettoauftragswert fuer alle Auftraege eines Kunden innerhalb
	 * eines bestimmten Zeitintervalls, die sich in einem bestimmten Status
	 * befinden.
	 * 
	 * @param sStatusI   der gewuenschte Status
	 * @param iIdKundeI  pk des Kunden
	 * @param datVonI    alle Auftraege ab diesem Datum inclusive
	 * @param datBisI    alle Auftraege bis zu diesem Datum inclusive
	 * @param cCurrencyI die Zielwaehrung
	 * @throws EJBExceptionLP
	 * @return BigDecimal der Gesamtwert in Zielwaehrung
	 */
	public BigDecimal berechneGesamtwertAuftragProStatusProKundeProZeitintervall(String sStatusI, Integer iIdKundeI,
			Date datVonI, Date datBisI, String cCurrencyI) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGesamtwertAuftragProStatusProKundeProZeitintervall";

		if (sStatusI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("sStatusI == null"));
		}
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iiKundeI == null"));
		}
		if (datVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("datVonI == null"));
		}
		if (datVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("datBisI == null"));
		}
		if (cCurrencyI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("cCurrencyI == null"));
		}

		BigDecimal bdAuftragswertSumme = Helper.getBigDecimalNull();
		Query query = em.createNamedQuery("AuftragfindByStatusKundeBelegdatumVonBis");
		query.setParameter(1, sStatusI);
		query.setParameter(2, iIdKundeI);
		query.setParameter(3, datVonI);
		query.setParameter(4, datBisI);
		Collection<?> c = query.getResultList();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Auftrag auftrag = ((Auftrag) iter.next());

			// der gesamtwert des auftrags ist in der db hinterlegt
			BigDecimal bdAuftragswert = auftrag.getNGesamtauftragswertinauftragswaehrung();

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
	 * @param pkAuftrag    PK des Auftrags
	 * @param status       der neue Status
	 * @param theClientDto der aktuelle User, null wenn keine Kopfdaten geaendert
	 *                     wurden
	 * @throws EJBExceptionLP
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean aendereAuftragstatus(Integer pkAuftrag, String status, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
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
			if (getTheJudgeFac().hatRecht(RechteFac.RECHT_AUFT_DARF_AUFTRAEGE_FREIGEBEN, theClientDto)) {
				auftragDto.setTAuftragsfreigabe(null);
				auftragDto.setPersonalIIdAuftragsfreigabe(null);
			}

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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setAuftragFromAuftragDto(auftrag, auftragDto);
		if (AuftragServiceFac.AUFTRAGSTATUS_STORNIERT.equals(sStautsVorAenderung)) {
			AuftragpositionDto[] posDto = getAuftragpositionFac().auftragpositionFindByAuftrag(pkAuftrag);
			for (int i = 0; i < posDto.length; i++) {
				posDto[i].setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				getAuftragpositionFac().updateAuftragposition(posDto[i], theClientDto);

				// Wenn Abrufposition muss auch Rahmen upgedatet werden
				if (posDto[i].getAuftragpositionIIdRahmenposition() != null) {
					AuftragpositionDto rahmenPosDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(posDto[i].getAuftragpositionIIdRahmenposition());
					getAuftragpositionFac().updateAuftragposition(rahmenPosDto, theClientDto);
				}

			}
		}

		// SP7381
		if (!status.equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)
				&& AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT.equals(sStautsVorAenderung)) {
			// Reservierungen neu setzen
			AuftragpositionDto[] posDto = getAuftragpositionFac().auftragpositionFindByAuftrag(pkAuftrag);
			for (int i = 0; i < posDto.length; i++) {
				getAuftragpositionFac().updateOffeneMengeAuftragposition(posDto[i].getIId(), false, theClientDto);
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

	private AuftragpositionDto[] entferneKalkulatorischeArtikel(AuftragpositionDto[] aAuftragpositionDto) {

		ArrayList<AuftragpositionDto> alOhneKalkulatorische = new ArrayList<AuftragpositionDto>();

		for (int i = 0; i < aAuftragpositionDto.length; i++) {
			AuftragpositionDto auftragpositionDto = aAuftragpositionDto[i];

			if (auftragpositionDto.getArtikelIId() != null) {
				Artikel a = em.find(Artikel.class, auftragpositionDto.getArtikelIId());
				if (Helper.short2boolean(a.getBKalkulatorisch())) {
					continue;
				}
			}
			alOhneKalkulatorische.add(auftragpositionDto);
		}

		return alOhneKalkulatorische.toArray(new AuftragpositionDto[alOhneKalkulatorische.size()]);
	}

	/**
	 * Der Auftragstatus ergibt sich aus der Summe der Stati der einzelnen
	 * Positionen.
	 * 
	 * @param iIdAuftragI  pk des Auftrag
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void setzeAuftragstatusAufgrundAuftragpositionstati(Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			// bei
			// - AuftragFac.AUFTRAGSTATUS_ANGELEGT
			// - AuftragFac.AUFTRAGSTATUS_STORNIERT
			// muss man die stati der positionen nicht weiter ansehen
			AuftragDto auftragDto = auftragFindByPrimaryKey(iIdAuftragI);

			if (!auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)
					&& !auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
				AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByAuftragIIdNMengeNotNull(iIdAuftragI, theClientDto);

				// SP8635 ohne kalkulatorische Artikel
				aAuftragpositionDto = entferneKalkulatorischeArtikel(aAuftragpositionDto);

				int iAnzahlOffenOhneZWS = zaehleStatiAuftragpositionenOhneIntZWS(aAuftragpositionDto,
						AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);

				int iAnzahlErledigtOhneZWS = zaehleStatiAuftragpositionenOhneIntZWS(aAuftragpositionDto,
						AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);

				int iAnzahlPositionenOhneIntelligenteZWS = 0;

				// PJ19632
				boolean bEsWurdenAlleAuftragspositionenVerrechnet = true;

				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					AuftragpositionDto auftragpositionDto = aAuftragpositionDto[i];

					// es gibt Positionen mit Status null, z.B. Betrifft
					if (auftragpositionDto.getNMenge() != null && !auftragpositionDto.isIntelligenteZwischensumme()) {

						iAnzahlPositionenOhneIntelligenteZWS++;

						BigDecimal bdMengeVerrechnet = BigDecimal.ZERO;

						// SP5794

						Session session3 = FLRSessionFactory.getFactory().openSession();
						String sQuery3 = "SELECT SUM(repos.n_menge) FROM FLRRechnungPosition repos WHERE repos.auftragposition_i_id="
								+ auftragpositionDto.getIId();

						org.hibernate.Query query3 = session3.createQuery(sQuery3);
						query3.setMaxResults(1);

						List<?> results3 = query3.list();
						Iterator<?> resultListIterator3 = results3.iterator();
						if (resultListIterator3.hasNext()) {
							// wenn ja, dann den zurueckliefern
							BigDecimal bdSumme = (BigDecimal) resultListIterator3.next();

							if (bdSumme != null) {
								bdMengeVerrechnet = bdMengeVerrechnet.add(bdSumme);
							}
						}

						session3.close();

						session3 = FLRSessionFactory.getFactory().openSession();
						sQuery3 = "SELECT SUM(lspos.n_menge) FROM FLRLieferscheinposition lspos WHERE lspos.auftragposition_i_id="
								+ auftragpositionDto.getIId()
								+ " AND lspos.flrlieferschein.lieferscheinstatus_status_c_nr='"
								+ LocaleFac.STATUS_VERRECHNET + "'";

						query3 = session3.createQuery(sQuery3);
						query3.setMaxResults(1);

						results3 = query3.list();
						resultListIterator3 = results3.iterator();
						if (resultListIterator3.hasNext()) {
							// wenn ja, dann den zurueckliefern
							BigDecimal bdSumme = (BigDecimal) resultListIterator3.next();

							if (bdSumme != null) {
								bdMengeVerrechnet = bdMengeVerrechnet.add(bdSumme);
							}
						}

						session3.close();

						if (bdMengeVerrechnet.doubleValue() < auftragpositionDto.getNMenge().doubleValue()) {
							bEsWurdenAlleAuftragspositionenVerrechnet = false;
						}

					}

				}

				if (iAnzahlOffenOhneZWS == iAnzahlPositionenOhneIntelligenteZWS) {
					auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
				} else if (iAnzahlErledigtOhneZWS == iAnzahlPositionenOhneIntelligenteZWS) {
					// PJ18288
					ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_AUFTRAG_AUTOMATISCH_VOLLSTAENDIG_ERLEDIGEN);

					int iAutomatischErledigen = (Integer) parameterDto.getCWertAsObject();

					if (iAutomatischErledigen >= 1) {

						if (iAutomatischErledigen == 2) {

							if (bEsWurdenAlleAuftragspositionenVerrechnet) {
								auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
							} else {
								auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
							}

						} else {
							auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
						}

					} else {
						auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
					}
				} else {
					auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
				}

				aendereAuftragstatus(auftragDto.getIId(), auftragDto.getStatusCNr(), theClientDto);
				if (AuftragServiceFac.AUFTRAGART_ABRUF.equals(auftragDto.getAuftragartCNr())) {
					getAuftragRahmenAbrufFac().pruefeUndSetzeRahmenstatus(auftragDto.getAuftragIIdRahmenauftrag(),
							theClientDto);
				}
			}
			// wg. SP2983 auskommentiert, da ansonsten das T_ERLEDIGT wieder auf
			// null gesetzt wird
			// updateAuftragOhneWeitereAktion(auftragDto, theClientDto);

			// } catch (EJBExceptionLP ex) {
			// throw new EJBExceptionLP(
			// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			// } catch (Throwable t) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new
			// Exception(t));
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Anzahl eines bestimmten Status in einer Liste von Auftragpositionen
	 * bestimmen.
	 * 
	 * @param aAuftragpositionDtoI Liste der Auftragpositionen
	 * @param sStatusI             Status
	 * @throws Throwable
	 * @return int die Anzahl der Stati
	 */
	private int zaehleStatiAuftragpositionenOhneIntZWS(AuftragpositionDto[] aAuftragpositionDtoI, String sStatusI) {
		int iAnzahl = 0;

		for (int i = 0; i < aAuftragpositionDtoI.length; i++) {
			AuftragpositionDto auftragpositionDto = aAuftragpositionDtoI[i];

			// es gibt Positionen mit Status null, z.B. Betrifft
			if (auftragpositionDto.getNMenge() != null && !auftragpositionDto.isIntelligenteZwischensumme()) {
				if (auftragpositionDto.getAuftragpositionstatusCNr().equals(sStatusI)) {
					iAnzahl++;
				}
			}
		}

		return iAnzahl;
	}

	/**
	 * Fuer die Nachkalkulation des Auftrags den Soll-Verkaufswert (=
	 * NettoVerkaufspreisPlusAufschlaegeMinusRabatte pro Stueck * geplanter Menge)
	 * bezogen auf eine bestimmte Artikelart berechnen. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Auftragpositionen.
	 * 
	 * @param iIdAuftragI  PK des Auftrags
	 * @param sArtikelartI die gewuenschte Artikelart
	 * @param theClientDto der aktuelle Benutzer
	 * @return BigDecimal der Verkaufswert der Artikelart Soll in Auftragswaherung
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BigDecimal berechneVerkaufswertSoll(Integer iIdAuftragI, String sArtikelartI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneVKWArbeitSoll";
		myLogger.entry();
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAuftragI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("sArtikelartI == null"));
		}

		BigDecimal bdVerkaufswertSollO = Helper.getBigDecimalNull();

		try {
			AuftragpositionDto[] aAuftragpositionDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);

			for (int i = 0; i < aAuftragpositionDtos.length; i++) {

				// SP4149 Die intelligente Zwischensumme selbst darf nicht
				// mitgerechent werden, da die IZWS bereits in
				// getNNettoeinzelpreisplusversteckteraufschlagminusrabatte
				// beruecksichtigt wird

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aAuftragpositionDtos[i].getNMenge() != null && aAuftragpositionDtos[i].getArtikelIId() != null
						&& aAuftragpositionDtos[i].getPositioniIdArtikelset() == null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(aAuftragpositionDtos[i].getArtikelIId(), theClientDto);

					// je nach Artikelart beruecksichtigen
					BigDecimal bdBeitragDieserPosition = aAuftragpositionDtos[i].getNMenge().multiply(
							aAuftragpositionDtos[i].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

					if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						if (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)
								|| Helper.short2boolean(oArtikelDto.getBAzinabnachkalk())) {
							bdVerkaufswertSollO = bdVerkaufswertSollO.add(bdBeitragDieserPosition);
						}
					} else {
						if (!oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)
								&& Helper.short2boolean(oArtikelDto.getBAzinabnachkalk()) == false) {
							bdVerkaufswertSollO = bdVerkaufswertSollO.add(bdBeitragDieserPosition);
						}
					}

				}
			}
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Verkaufswert " + sArtikelartI + " Soll : " + bdVerkaufswertSollO.toString());

		return bdVerkaufswertSollO;
	}

	/**
	 * Fuer die Nachkalkulation des Auftrags den Soll-Gestehungspreis (=
	 * Gestehungspreis am Hauptlager des Mandanten pro Stueck * geplanter Menge)
	 * bezogen auf eine bestimmte Artikelart berechnen. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Auftragpositionen.
	 * 
	 * @param iIdAuftragI  PK des Auftrags
	 * @param sArtikelartI die gewuenschte Artikelart
	 * @param theClientDto der aktuelle Benutzer
	 * @return BigDecimal der Gestehungswert der Artikelart Soll in Auftragswaherung
	 * @throws EJBExceptionLP Ausnahme
	 */

	private BigDecimal getGestehungswertSoll(AuftragpositionDto aAuftragpositionDto, AuftragDto auftragDto,
			TheClientDto theClientDto) {

		BigDecimal bdGestehungspreisSoll = new BigDecimal(0);
		if (aAuftragpositionDto.getPositioniIdArtikelset() == null && aAuftragpositionDto.getArtikelIId() != null) {

			try {
				LagerDto lDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

				Query query = em.createNamedQuery("AuftragpositionfindByPositionIIdArtikelset");
				query.setParameter(1, aAuftragpositionDto.getIId());
				Collection<?> angebotpositionDtos = query.getResultList();
				AuftragpositionDto[] zugehoerigeABPosDtos = AuftragpositionDtoAssembler.createDtos(angebotpositionDtos);

				if (zugehoerigeABPosDtos.length == 0) {

					ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(
							theClientDto.getMandant(), ParameterFac.KATEGORIE_ANGEBOT,
							ParameterFac.PARAMETER_LIEFERANT_ANGEBEN);

					boolean bLieferantAngeben = (Boolean) parametermandantDto.getCWertAsObject();

					if (bLieferantAngeben == true) {
						if (aAuftragpositionDto.getBdEinkaufpreis() != null) {
							// Als erstes zaehlt der EK-reis aus der Position
							return aAuftragpositionDto.getBdEinkaufpreis().multiply(aAuftragpositionDto.getNMenge());
						} else {
							// anosonsten der Lief1Preis
							ArtikellieferantDto alDto = getArtikelFac()
									.getArtikelEinkaufspreisDesBevorzugtenLieferanten(
											aAuftragpositionDto.getArtikelIId(), aAuftragpositionDto.getNMenge(),
											auftragDto.getCAuftragswaehrung(), theClientDto);
							if (alDto != null && alDto.getNNettopreis() != null) {
								return alDto.getNNettopreis().multiply(aAuftragpositionDto.getNMenge());
							}

						}
					}

					bdGestehungspreisSoll = getLagerFac().getGestehungspreisZumZeitpunkt(
							aAuftragpositionDto.getArtikelIId(), lDto.getIId(), auftragDto.getTBelegdatum(),
							theClientDto);
					if (bdGestehungspreisSoll != null) {
						bdGestehungspreisSoll = bdGestehungspreisSoll.multiply(aAuftragpositionDto.getNMenge());
					}

				} else {

					for (int k = 0; k < zugehoerigeABPosDtos.length; k++) {
						AuftragpositionDto apDtoSetartikel = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(zugehoerigeABPosDtos[k].getIId());
						bdGestehungspreisSoll = bdGestehungspreisSoll
								.add(getGestehungswertSoll(apDtoSetartikel, auftragDto, theClientDto));
					}
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		return bdGestehungspreisSoll;

	}

	public BigDecimal berechneGestehungswertSoll(Integer iIdAuftragI, String sArtikelartI,
			boolean bMitEigengefertigtenStuecklisten, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAuftragI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("sArtikelartI == null"));
		}

		BigDecimal bdGestehungswertSollO = Helper.getBigDecimalNull();

		try {

			AuftragDto auftragDto = auftragFindByPrimaryKey(iIdAuftragI);
			AuftragpositionDto[] aAuftragpositionDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);

			for (int i = 0; i < aAuftragpositionDtos.length; i++) {

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aAuftragpositionDtos[i].getNMenge() != null && aAuftragpositionDtos[i].getArtikelIId() != null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(aAuftragpositionDtos[i].getArtikelIId(), theClientDto);

					if (bMitEigengefertigtenStuecklisten == false) {
						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(oArtikelDto.getIId(), theClientDto);
						if (stklDto != null && Helper.short2boolean(stklDto.getBFremdfertigung()) == false) {
							continue;
						}
					}

					// Grundlage ist der Gestehungspreis des Artikels am
					// Hauptlager des Mandanten
					BigDecimal bdGestehungspreisSoll = getGestehungswertSoll(aAuftragpositionDtos[i], auftragDto,
							theClientDto);

					// je nach Artikelart beruecksichtigen

					if (bdGestehungspreisSoll != null) {
						if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							if (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)
									|| Helper.short2boolean(oArtikelDto.getBAzinabnachkalk())) {
								bdGestehungswertSollO = bdGestehungswertSollO.add(bdGestehungspreisSoll);
							}
						} else {
							if (!oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)
									&& Helper.short2boolean(oArtikelDto.getBAzinabnachkalk()) == false) {
								bdGestehungswertSollO = bdGestehungswertSollO.add(bdGestehungspreisSoll);
							}
						}
					}
				}
			}
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Gestehungswert " + sArtikelartI + " Soll : " + bdGestehungswertSollO.toString());

		return bdGestehungswertSollO;
	}

	/**
	 * Die Anzahl der Belege holen, die zu einem bestimmten Auftrag existieren. <br>
	 * Als Beleg gilt auch, wenn Auftragzeiten zu diesem Auftrag erfasst wurden.
	 * 
	 * @param iIdAuftragI  PK des Auftrags
	 * @param theClientDto der aktuelle Benutzer
	 * @return int die Anzahl der Belege zu diesem Auftrag
	 * @throws EJBExceptionLP Ausnahme
	 */
	public int berechneAnzahlBelegeZuAuftrag(Integer iIdAuftragI, TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneAnzahlBelegeZuAuftrag";
		myLogger.entry();
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAuftragI == null"));
		}
		int iAnzahlBelegeO = 0;
		try {
			// Eingangsrechnungen
			EingangsrechnungAuftragszuordnungDto[] aEingangsrechnungDtos = getEingangsrechnungFac()
					.eingangsrechnungAuftragszuordnungFindByAuftragIId(iIdAuftragI);

			iAnzahlBelegeO += aEingangsrechnungDtos.length;

			// Lieferscheine
			LieferscheinDto[] aLieferscheinDtos = getLieferscheinFac().lieferscheinFindByAuftrag(iIdAuftragI,
					theClientDto);

			iAnzahlBelegeO += aLieferscheinDtos.length;

			AuftragpositionDto[] aPositionDtos = getAuftragpositionFac().auftragpositionFindByAuftrag(iIdAuftragI);
			iAnzahlBelegeO += aPositionDtos.length;
			// @todo Rechnungen PJ 3809

			// Auftragzeiten
			AuftragzeitenDto[] aAuftragzeitenDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
					LocaleFac.BELEGART_AUFTRAG, iIdAuftragI, null, null, null, null,
					ZeiterfassungFac.SORTIERUNG_ZEITDATEN_PERSONAL, // order
																	// by
																	// personal
					theClientDto);
			if (aAuftragzeitenDtos != null && aAuftragzeitenDtos.length > 0) {
				iAnzahlBelegeO++;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return iAnzahlBelegeO;
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
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
	public void pruefeAktivierbar(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Auftrag oAuftrag = em.find(Auftrag.class, iid);
		if (oAuftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// Ohne Positionen darf der Beleg nicht aktiviert werden.
		if (getAuftragpositionFac().getAnzahlMengenbehafteteAuftragpositionen(oAuftrag.getIId(), theClientDto) == 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN, "");
		}
		
		pruefeObZusammenfassungErfuellt(Helper.isTrue(oAuftrag.getBMitzusammenfassung()), oAuftrag.getIId(),
				theClientDto);
		
		pruefeZwischensumme(oAuftrag.getIId(), theClientDto);
	}

	private void pruefeZwischensumme(Integer auftragId, TheClientDto theClientDto) throws RemoteException {
		AuftragpositionDto[] allPos = getAuftragpositionFac()
				.auftragpositionFindByAuftrag(auftragId);
		getBelegVerkaufFac().validiereZwsAufGleichenMwstSatzThrow(allPos);
	}
	
	private void pruefeObZusammenfassungErfuellt(boolean mitZusammenfassung, Integer auftragId,
			TheClientDto theClientDto) throws RemoteException {
		if (!mitZusammenfassung)
			return;

		AuftragpositionDto[] allPos = getAuftragpositionFac().auftragpositionFindByAuftrag(auftragId);
		Set<Integer> posIdsNotInZws = getBelegVerkaufFac().calculatePositionsNotInZws(allPos);
		if (posIdsNotInZws.size() != 0) {

			List<Integer> posNrs = new ArrayList<Integer>();
			for (Integer posId : posIdsNotInZws) {
				Integer posNr = getAuftragpositionFac().getPositionNummer(posId);
				posNrs.add(posNr);
			}
			Collections.sort(posNrs, new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});
			throw EJBExcFactory.auftragPositionenOhneZws(posNrs);
		}
	}
	
	private void mindestbestellwertHinzufuegen(Integer iIdAuftragI, TheClientDto theClientDto) {

		try {
			AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(iIdAuftragI);
			int iNachkommastellenPreis = getUINachkommastellenPreisVK(aDto.getMandantCNr());

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(aDto.getKundeIIdRechnungsadresse(), theClientDto);

			BigDecimal bdMindestbestellwertInAuftragswaehrung = null;

			if (kdDto.getNMindestbestellwert() != null) {
				bdMindestbestellwertInAuftragswaehrung = getLocaleFac().rechneUmInAndereWaehrungGerundetZuDatum(
						kdDto.getNMindestbestellwert(), kdDto.getCWaehrung(), aDto.getCAuftragswaehrung(),
						new java.sql.Date(aDto.getTBelegdatum().getTime()), theClientDto);
			}

			if (bdMindestbestellwertInAuftragswaehrung != null
					&& bdMindestbestellwertInAuftragswaehrung.doubleValue() > 0) {

				ArtikelDto aDtoMindestbestellwert = getParameterFac().getMindestbestellwertArtikel(theClientDto);

				if (aDtoMindestbestellwert != null) {

					// Zuerst alle Mindesbestellwertsartikel entfernen
					AuftragpositionDto[] posDtos = getAuftragpositionFac().auftragpositionFindByAuftrag(iIdAuftragI);
					for (int i = 0; i < posDtos.length; i++) {
						if (posDtos[i].getArtikelIId() != null
								&& posDtos[i].getArtikelIId().equals(aDtoMindestbestellwert.getIId())) {
							getAuftragpositionFac().removeAuftragposition(posDtos[i], theClientDto);
						}
					}

					posDtos = getAuftragpositionFac().auftragpositionFindByAuftrag(iIdAuftragI);

					BigDecimal bdGesamtPreisVorRabatten = BigDecimal.ZERO;
					for (int i = 0; i < posDtos.length; i++) {

						if (posDtos[i].getArtikelIId() != null) {
							BelegpositionVerkaufDto bvDto = getBelegVerkaufFac()
									.getBelegpositionVerkaufReport(posDtos[i], aDto, iNachkommastellenPreis);

							if (bvDto != null && bvDto.getNReportGesamtpreis() != null) {

								bdGesamtPreisVorRabatten = bdGesamtPreisVorRabatten.add(bvDto.getNReportGesamtpreis());
							}
						}

					}

					// Mindestbestellwert beim Kunden ist immer in
					// Kundenwaehrung

					// Parameter ist in Mandantenwaehrung

					Auftrag oAuftrag = em.find(Auftrag.class, iIdAuftragI);

					// Wenn der Nettowert < Mindestbestellwert
					if (bdGesamtPreisVorRabatten.compareTo(bdMindestbestellwertInAuftragswaehrung) == -1) {

						BigDecimal diff = bdMindestbestellwertInAuftragswaehrung.subtract(bdGesamtPreisVorRabatten);

						BigDecimal bdDiffOhneVerstecktemAufschlag = diff;

						if (oAuftrag.getFVersteckteraufschlag() != null
								&& oAuftrag.getFVersteckteraufschlag().doubleValue() != 0) {
							bdDiffOhneVerstecktemAufschlag = diff.subtract(Helper.getProzentWert(diff,
									new BigDecimal(oAuftrag.getFVersteckteraufschlag()), 4));
						}
						// + Projektrabatt
						// + Allgemeiner Rabatt

						// Nun neue Position hinzufuegen

						// Dann Auftragsposition anlegen
						AuftragpositionDto auftragpositionDto = new AuftragpositionDto();
						auftragpositionDto.setBelegIId(iIdAuftragI);
						auftragpositionDto.setArtikelIId(aDtoMindestbestellwert.getIId());
						auftragpositionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
						auftragpositionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
						auftragpositionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
						auftragpositionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
						auftragpositionDto.setNMenge(new BigDecimal(1));
						auftragpositionDto.setNOffeneMenge(new BigDecimal(1));
						auftragpositionDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
						auftragpositionDto.setFRabattsatz(new Double(0));

						auftragpositionDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
						/*
						 * MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
						 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kdDto.getMwstsatzbezIId(),
						 * theClientDto);
						 */
						MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(
								kdDto.getMwstsatzbezIId(), oAuftrag.getTBelegdatum(), theClientDto);

						auftragpositionDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
						auftragpositionDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
						auftragpositionDto.setNEinzelpreis(diff);
						auftragpositionDto.setNRabattbetrag(new BigDecimal(0));
						auftragpositionDto.setNNettoeinzelpreis(diff);
						auftragpositionDto.setNBruttoeinzelpreis(diff);
						auftragpositionDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(diff);
						auftragpositionDto.setNMwstbetrag(new BigDecimal(0));

						auftragpositionDto.setNEinzelpreisplusversteckteraufschlag(bdDiffOhneVerstecktemAufschlag);
						auftragpositionDto.setNNettoeinzelpreisplusversteckteraufschlag(bdDiffOhneVerstecktemAufschlag);

						auftragpositionDto.setTUebersteuerbarerLiefertermin(oAuftrag.getTBelegdatum());
						auftragpositionDto.setBDrucken(Helper.boolean2Short(false));
						auftragpositionDto.setFZusatzrabattsatz(new Double(0));

						Integer auftragspositionIId = getAuftragpositionFac().createAuftragposition(auftragpositionDto,
								theClientDto);

						Auftragposition position = em.find(Auftragposition.class, auftragspositionIId);

						position.setNRabattbetrag(new BigDecimal(0));

						position.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(diff);

						position.setNNettogesamtpreis(bdDiffOhneVerstecktemAufschlag);
						position.setNNettoeinzelpreis(bdDiffOhneVerstecktemAufschlag);
						position.setNNettogesamtpreisplusversteckteraufschlag(diff);
						position.setNNettoeinzelpreisplusversteckteraufschlag(diff);

						// MWST
						MwstsatzDto mwstsatzDto = getBelegpositionkonvertierungFac()
								.ermittleMwstSatz(auftragpositionDto, oAuftrag.getTBelegdatum(), theClientDto);
						if (mwstsatzDto != null && mwstsatzDto.getFMwstsatz() != 0.0) {
							BigDecimal bdMwstbetrag = Helper.getProzentWert(position.getNNettoeinzelpreis(),
									new BigDecimal(mwstsatzDto.getFMwstsatz()), iNachkommastellenPreis);
							position.setNMwstbetrag(bdMwstbetrag);
							position.setNMwstbetrag(new BigDecimal(0));
							position.setNBruttogesamtpreis(position.getNNettoeinzelpreis().add(bdMwstbetrag));

						} else {
							position.setNMwstbetrag(new BigDecimal(0));
							position.setNBruttogesamtpreis(bdDiffOhneVerstecktemAufschlag);
						}

						em.merge(position);
						em.flush();

					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	public BelegPruefungDto aktiviereBeleg(Integer iIdAuftragI, TheClientDto theClientDto) {
		Auftrag oAuftrag = em.find(Auftrag.class, iIdAuftragI);
		if (oAuftrag.getAuftragstatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {

			try {
				// SP5608
				ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_AUFTRAG_SERIENNUMMERN);

				if (((Boolean) parametermandantDto.getCWertAsObject()) == true) {
					AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
							.auftragpositionFindByAuftrag(iIdAuftragI);

					aAuftragpositionDto = entferneKalkulatorischeArtikel(aAuftragpositionDto, theClientDto);

					for (AuftragpositionDto auftragPositionDto : aAuftragpositionDto) {

						if (auftragPositionDto.getArtikelIId() != null && auftragPositionDto.getNMenge() != null
								&& auftragPositionDto.getNMenge().doubleValue() > 0) {
							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(auftragPositionDto.getArtikelIId(), theClientDto);
							if (aDto.isSeriennrtragend()) {

								Query query = em.createNamedQuery("AuftragseriennrnfindByAuftragpositionIId");
								query.setParameter(1, auftragPositionDto.getIId());
								Collection<?> seriennummern = query.getResultList();

								if (seriennummern.size() == 0) {
									// KEINE SNRS definiert FEHLER

									ArrayList alInfo = new ArrayList();

									alInfo.add(getAuftragpositionFac().getPositionNummer(auftragPositionDto.getIId()));

									throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_KEINE_SNRS_AUF_POSITION,
											alInfo, new Exception("FEHLER_AUFTRAG_KEINE_SNRS_AUF_POSITION"));
								}

							}
						}

					}
				}

				// PJ20384
				boolean bEsGibtBereitsLieferscheine = false;
				LieferscheinDto[] lsDtos = getLieferscheinFac().lieferscheinFindByAuftrag(iIdAuftragI, theClientDto);

				if (lsDtos != null && lsDtos.length > 0) {
					bEsGibtBereitsLieferscheine = true;
				}
				// PJ19570
				ParametermandantDto parametermandantLieferdatumDto = getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_AUFTRAGSFREIGABE);
				if ((Boolean) parametermandantLieferdatumDto.getCWertAsObject()) {

					if (oAuftrag.getTAuftragsfreigabe() != null && oAuftrag.getPersonalIIdAuftragsfreigabe() != null) {
						oAuftrag.setTGedruckt(getTimestamp());
						if (bEsGibtBereitsLieferscheine) {
							oAuftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
						} else {
							oAuftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
						}
					} else {

						oAuftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
					}

				} else {
					oAuftrag.setTGedruckt(getTimestamp());
					if (bEsGibtBereitsLieferscheine) {
						oAuftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
					} else {
						oAuftrag.setAuftragstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (oAuftrag.getAuftragstatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {
				// PJ 15710
				Kunde kunde = em.find(Kunde.class, oAuftrag.getKundeIIdAuftragsadresse());
				kunde.setBIstinteressent(Helper.boolean2Short(false));
				em.merge(kunde);
				em.flush();

				// PJ19485
				if (oAuftrag.getBestellungIIdAndererMandant() != null) {
					getBestellungFac().bestellungAusAnderemMandantRueckbestaetigen(iIdAuftragI, theClientDto);
				}
			}

			// SP9000
			Kunde kunde = em.find(Kunde.class, oAuftrag.getKundeIIdAuftragsadresse());
			getAnsprechpartnerFac().pruefeAnsprechpartnerZugehoerigkeit(kunde.getPartnerIId(),
					oAuftrag.getAnsprechpartnerIIdKunde(), theClientDto);
			Kunde kundeLieferadresse = em.find(Kunde.class, oAuftrag.getKundeIIdLieferadresse());
			getAnsprechpartnerFac().pruefeAnsprechpartnerZugehoerigkeit(kundeLieferadresse.getPartnerIId(),
					oAuftrag.getAnsprechpartnerIIdLieferadresse(), theClientDto);
			Kunde kundeRechnungsadresse = em.find(Kunde.class, oAuftrag.getKundeIIdRechnungsadresse());
			getAnsprechpartnerFac().pruefeAnsprechpartnerZugehoerigkeit(kundeRechnungsadresse.getPartnerIId(),
					oAuftrag.getAnsprechpartnerIIdRechnungsadresse(), theClientDto);

		}

		return null;
	}

	@Override
	public Timestamp berechneBeleg(Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Auftrag oAuftrag = em.find(Auftrag.class, iIdAuftragI);
		myLogger.entry();

		if (oAuftrag.getAuftragstatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)
				|| oAuftrag.getBestellungIIdAndererMandant() != null) {

			if (oAuftrag.getBestellungIIdAndererMandant() != null) {
				oAuftrag.setNGesamtauftragswertinauftragswaehrung(null);
			}

			// SP4935 Auftragsfreigabe muss erhalten bleiben
			Timestamp tAuftragsfreigabe = oAuftrag.getTAuftragsfreigabe();
			Integer personalIIdAuftragsfreigabe = oAuftrag.getPersonalIIdAuftragsfreigabe();

			// Mindermengenzuschlag PJ21361

			if (Helper.short2boolean(oAuftrag.getBMindermengenzuschlag())) {

				mindermengenzuschlagEntfernen(assembleAuftragDto(oAuftrag), theClientDto);

				BigDecimal nNettowertGesamt = berechneNettowertGesamt(oAuftrag.getIId(), theClientDto);

				BigDecimal bdWertInMandantenwaehrung = nNettowertGesamt.divide(
						new BigDecimal(oAuftrag.getFWechselkursmandantwaehrungzuauftragswaehrung()), 4,
						BigDecimal.ROUND_HALF_EVEN);

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(oAuftrag.getKundeIIdRechnungsadresse(),
						theClientDto);
				Integer landIId = null;
				if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
					landIId = kundeDto.getPartnerDto().getLandplzortDto().getIlandID();
				}
				MmzDto mmzDto = getRechnungServiceFac().getMindermengenzuschlag(bdWertInMandantenwaehrung, landIId,
						theClientDto);
				if (mmzDto != null) {

					ArtikelDto aDtoMMz = getArtikelFac().artikelFindByPrimaryKey(mmzDto.getArtikelIId(), theClientDto);

					if (Helper.short2boolean(aDtoMMz.getBLagerbewirtschaftet())) {

						ArrayList al = new ArrayList();
						al.add(aDtoMMz);
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MMZ_ARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN,
								al, new Exception(
										"FEHLER_MMZ_ARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN: " + aDtoMMz.getCNr()));
					}

					ParametermandantDto parameterPosKont = getParameterFac().getMandantparameter(
							theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
					boolean isPositionskontierung = ((Boolean) parameterPosKont.getCWertAsObject()).booleanValue();

					Integer mwstsatzBezId = aDtoMMz.getMwstsatzbezIId();
					if (!isPositionskontierung) {

						mwstsatzBezId = kundeDto.getMwstsatzbezIId();
					}
					MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(mwstsatzBezId,
							oAuftrag.getTBelegdatum(), theClientDto);
					/*
					 * MwstsatzDto mwstsatzDtoAktuell = null;
					 * 
					 * if (!isPositionskontierung) { // Aktuellen MWST-Satz uebersetzen. KundeDto
					 * kundeDto =
					 * getKundeFac().kundeFindByPrimaryKey(oAuftrag.getKundeIIdRechnungsadresse(),
					 * theClientDto);
					 * 
					 * mwstsatzDtoAktuell = getMandantFac()
					 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(),
					 * theClientDto);
					 * 
					 * } else { mwstsatzDtoAktuell = getMandantFac()
					 * .mwstsatzFindByMwstsatzbezIIdAktuellster(aDtoMMz.getMwstsatzbezIId(),
					 * theClientDto); }
					 */
					AuftragpositionDto posMmzDto = new AuftragpositionDto();
					posMmzDto.setArtikelIId(aDtoMMz.getIId());
					posMmzDto.setPositionsartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
					posMmzDto.setBelegIId(oAuftrag.getIId());
					posMmzDto.setNMenge(new BigDecimal(1));
					posMmzDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
					posMmzDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
					posMmzDto.setTUebersteuerbarerLiefertermin(oAuftrag.getTLiefertermin());

					BigDecimal bdNettoeinzel = mmzDto.getNZuschlag()
							.multiply(new BigDecimal(oAuftrag.getFWechselkursmandantwaehrungzuauftragswaehrung()));// noch
																													// in
					// Belegwaehrung
					// umrechnen
					posMmzDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(bdNettoeinzel);
					posMmzDto.setNNettoeinzelpreis(bdNettoeinzel);
					posMmzDto.setNEinzelpreis(bdNettoeinzel);
					posMmzDto.setFRabattsatz(0D);
					posMmzDto.setNRabattbetrag(BigDecimal.ZERO);
					posMmzDto.setFZusatzrabattsatz(0D);
					posMmzDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
					posMmzDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

					BigDecimal mwstBetrag = posMmzDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
							.multiply(new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz()).movePointLeft(2));
					posMmzDto.setNMwstbetrag(mwstBetrag);
					posMmzDto.setNBruttoeinzelpreis(
							posMmzDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte().add(mwstBetrag));

					getAuftragpositionFac().createAuftragposition(posMmzDto, theClientDto);

				}

			}

			mindestbestellwertHinzufuegen(iIdAuftragI, theClientDto);

			// PJ21885

			AuftragpositionDto[] alle = getAuftragpositionFac()
					.auftragpositionFindByAuftragIIdNMengeNotNull(oAuftrag.getIId(), theClientDto);

			Integer artikelIIdVerpackungskosten = getVerpackunskostenArtikel(theClientDto);
			if (artikelIIdVerpackungskosten != null) {
				for (int i = 0; i < alle.length; i++) {
					if (alle[i].getArtikelIId() != null
							&& alle[i].getArtikelIId().equals(artikelIIdVerpackungskosten)) {
						getAuftragpositionFac().removeAuftragposition(alle[i], theClientDto);

					}
				}

				alle = getAuftragpositionFac().auftragpositionFindByAuftragIIdNMengeNotNull(oAuftrag.getIId(),
						theClientDto);
			}

			AuftragpositionDto belegpositionDto_Verpackungskosten = new AuftragpositionDto();
			belegpositionDto_Verpackungskosten.setTUebersteuerbarerLiefertermin(oAuftrag.getTLiefertermin());
			belegpositionDto_Verpackungskosten = (AuftragpositionDto) getBelegVerkaufFac()
					.erstelleVerpackungskostenpositionAnhandNettowert(oAuftrag.getKundeIIdRechnungsadresse(), alle,
							assembleAuftragDto(oAuftrag), belegpositionDto_Verpackungskosten, theClientDto);

			if (belegpositionDto_Verpackungskosten != null) {
				getAuftragpositionFac().createAuftragposition(belegpositionDto_Verpackungskosten, false, theClientDto);
			}

			oAuftrag.setTAuftragsfreigabe(tAuftragsfreigabe);
			oAuftrag.setPersonalIIdAuftragsfreigabe(personalIIdAuftragsfreigabe);

			BigDecimal bdAuftragswertInAuftragswaehrung = berechneNettowertGesamt(oAuftrag.getIId(), theClientDto); // Berechnung
																													// in
			// Auftragswaehrung

			if (oAuftrag.getNKorrekturbetrag() != null) {
				bdAuftragswertInAuftragswaehrung = bdAuftragswertInAuftragswaehrung.add(oAuftrag.getNKorrekturbetrag());
			}

			BigDecimal bdWechselkurs = Helper.getKehrwert(
					new BigDecimal(oAuftrag.getFWechselkursmandantwaehrungzuauftragswaehrung().doubleValue()));
			BigDecimal bdAuftragswertInMandantenwaehrung = bdAuftragswertInAuftragswaehrung.multiply(bdWechselkurs);
			bdAuftragswertInMandantenwaehrung = Helper.rundeKaufmaennisch(bdAuftragswertInMandantenwaehrung, 4);
			checkNumberFormat(bdAuftragswertInMandantenwaehrung);

			BigDecimal bdMaterialwertInMandantenwaehrung = berechneMaterialwertGesamt(oAuftrag.getIId(), theClientDto); // inMandantenwaehrung

			oAuftrag.setNGesamtauftragswertinauftragswaehrung(bdAuftragswertInAuftragswaehrung);
			oAuftrag.setNMaterialwertinmandantenwaehrung(bdMaterialwertInMandantenwaehrung);

			BigDecimal bdRohdeckungInMandantenwaehrung = bdAuftragswertInMandantenwaehrung
					.subtract(bdMaterialwertInMandantenwaehrung);

			bdRohdeckungInMandantenwaehrung = Helper.rundeKaufmaennisch(bdRohdeckungInMandantenwaehrung, 4);
			checkNumberFormat(bdRohdeckungInMandantenwaehrung);
			if (oAuftrag.getNRohdeckungaltinmandantenwaehrung() == null) {
				oAuftrag.setNRohdeckunginmandantenwaehrung(bdRohdeckungInMandantenwaehrung);
				oAuftrag.setNRohdeckungaltinmandantenwaehrung(bdRohdeckungInMandantenwaehrung);
			}
			em.merge(oAuftrag);
			em.flush();

			// // PJ3687, PJ2276
			// AuftragpositionDto[] abPosDto = getAuftragpositionFac()
			// .auftragpositionFindByAuftrag(iIdAuftragI);
			// Set<Integer> modifiedPositions = getBelegVerkaufFac()
			// .adaptIntZwsPositions(abPosDto);
			// for (Integer index : modifiedPositions) {
			// Auftragposition abposEntity = em.find(Auftragposition.class,
			// abPosDto[index].getIId());
			// abposEntity
			// .setNNettogesamtpreisplusversteckteraufschlagminusrabatte(abPosDto[index]
			// .getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			// em.merge(abposEntity);
			// }
		}

		return getTimestamp();
	}

	public boolean checkPositionFormat(Integer iIdAuftragI, TheClientDto theClientDto) throws EJBExceptionLP {
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
				if (pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_POSITION)) {
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
	 * @param oAuftragpositionDtoI die Auftragposition
	 * @param theClientDto         der aktuelle Benutzer
	 * @return String die vallstaendig Artikelbezeichnung
	 * @throws java.lang.Throwable Ausnahme
	 */
	private String baueArtikelBezeichnung(AuftragpositionDto oAuftragpositionDtoI, TheClientDto theClientDto)
			throws Throwable {
		ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(oAuftragpositionDtoI.getArtikelIId(),
				theClientDto);

		StringBuffer sbBezeichnung = new StringBuffer();

		if (oAuftragpositionDtoI.isIdent()) {
			sbBezeichnung.append(oArtikelDto.getCNr());

			if (oAuftragpositionDtoI.getCBez() != null) {
				sbBezeichnung.append(oAuftragpositionDtoI.getCBez());
			} else {
				if (oArtikelDto.getArtikelsprDto() != null) {
					if (oArtikelDto.getArtikelsprDto().getCBez() != null) {
						sbBezeichnung.append("\n").append(oArtikelDto.getArtikelsprDto().getCBez());
					}
				}
			}

			if (oArtikelDto.getArtikelsprDto() != null) {
				if (oArtikelDto.getArtikelsprDto().getCZbez() != null) {
					sbBezeichnung.append("\n").append(oArtikelDto.getArtikelsprDto().getCZbez());
				}
			}
		} else if (oAuftragpositionDtoI.isHandeingabe()) {
			sbBezeichnung.append(oArtikelDto.getArtikelsprDto().getCBez());

			if (oArtikelDto.getArtikelsprDto().getCZbez() != null) {
				sbBezeichnung.append("\n").append(oArtikelDto.getArtikelsprDto().getCZbez());
			}
		}

		return sbBezeichnung.toString();
	}

	public int importiereSON_CSV(LinkedHashMap<String, ArrayList<ImportSonCsvDto>> hmNachBestellnummerGruppiert,
			TheClientDto theClientDto) {

		Iterator it = hmNachBestellnummerGruppiert.keySet().iterator();

		while (it.hasNext()) {
			String bestellnummer = (String) it.next();

			ArrayList<ImportSonCsvDto> zeilen = hmNachBestellnummerGruppiert.get(bestellnummer);
			if (zeilen.size() > 0) {

				String fehler = "";

				String abladestelleNr = zeilen.get(0).getAbladestelle();

				if (abladestelleNr != null) {
					try {
						List<KundeDto> kundeDtos = getKundeFac().kundeFindByFremdsystemnummerMandantCnrOhneExc(
								abladestelleNr, theClientDto.getMandant());
						if (kundeDtos != null && kundeDtos.size() > 0) {

							KundeDto kundeDto = kundeDtos.get(0);

							String liefertermin = zeilen.get(0).getLiefertermin();

							try {
								DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
								java.util.Date date = formatter.parse(liefertermin);

								// Auftrag erzeugen
								AuftragDto auftragDto = new AuftragDto();
								auftragDto.setMandantCNr(theClientDto.getMandant());
								auftragDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
								auftragDto.setTBelegdatum(Helper.cutTimestamp(getTimestamp()));
								auftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);
								auftragDto.setKundeIIdAuftragsadresse(kundeDto.getIId());
								auftragDto.setKundeIIdLieferadresse(kundeDto.getIId());
								auftragDto.setKundeIIdRechnungsadresse(kundeDto.getIId());
								auftragDto.setKostIId(kundeDto.getKostenstelleIId());
								auftragDto.setDLiefertermin(new java.sql.Timestamp(date.getTime()));
								auftragDto.setCBestellnummer(bestellnummer);
								auftragDto.setCAuftragswaehrung(kundeDto.getCWaehrung());
								auftragDto.setFWechselkursmandantwaehrungzubelegwaehrung(
										new Double(getLocaleFac().getWechselkurs2(theClientDto.getSMandantenwaehrung(),
												kundeDto.getCWaehrung(), theClientDto).doubleValue()));

								auftragDto.setLieferartIId(kundeDto.getLieferartIId());
								auftragDto.setZahlungszielIId(kundeDto.getZahlungszielIId());
								auftragDto.setSpediteurIId(kundeDto.getSpediteurIId());
								auftragDto.setLagerIIdAbbuchungslager(kundeDto.getLagerIIdAbbuchungslager());

								Integer auftragIId = createAuftrag(auftragDto, theClientDto);

								MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(
										kundeDto.getMwstsatzbezIId(), auftragDto.getTBelegdatum(), theClientDto);
								for (int i = 0; i < zeilen.size(); i++) {
									ImportSonCsvDto positionDto = zeilen.get(i);

									String kundenartikelnummer = positionDto.getArtikelnummer();

									String bezeichnung = positionDto.getBezeichnung();

									BigDecimal menge = null;
									try {
										menge = new BigDecimal(positionDto.getMenge());
									} catch (NumberFormatException e) {
										fehler = "Menge aus Spalte 19 '" + positionDto.getMenge()
												+ "' kann nicht in einen Wert konvertiert werden. Zeile: " + (i + 1);
										e.printStackTrace();
										break;
									}

									// PJ21746
									if (positionDto.getBestelltyp() != null
											&& positionDto.getBestelltyp().trim().equals("FPQ")) {
										AuftragpositionDto posDtoText = new AuftragpositionDto();
										posDtoText.setBelegIId(auftragIId);
										posDtoText.setBNettopreisuebersteuert(Helper.boolean2Short(false));
										posDtoText.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
										posDtoText.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE);
										posDtoText.setXTextinhalt("FPQ");
										getAuftragpositionFac().createAuftragposition(posDtoText, theClientDto);
									}

									AuftragpositionDto posDto = new AuftragpositionDto();
									posDto.setBelegIId(auftragIId);
									posDto.setNMenge(menge);
									posDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
									posDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

									java.util.Date datePosition = formatter.parse(positionDto.getLiefertermin());

									posDto.setTUebersteuerbarerLiefertermin(
											new java.sql.Timestamp(datePosition.getTime()));

									/*
									 * TODO: Warum pro Position, wenn Kunde.steuersatz konstant? MwstsatzDto
									 * mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
									 * kundeDto.getMwstsatzbezIId(), theClientDto);
									 */
									posDto.setMwstsatzIId(mwstsatzDto.getIId());

									javax.persistence.Query q = KundesokoQuery.byKundeIIdArtikelnummer(em,
											kundeDto.getIId(), kundenartikelnummer,
											Helper.cutDate(new java.sql.Date(System.currentTimeMillis())));
									Collection c = q.getResultList();

									if (c.size() == 0) {

										// HANDEINGABE
										posDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE);
										posDto.setCBez(bezeichnung);
										posDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
									} else {
										Kundesoko soko = ((Kundesoko) c.iterator().next());

										posDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
										ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(soko.getArtikelIId(),
												theClientDto);
										posDto.setArtikelIId(soko.getArtikelIId());
										posDto.setEinheitCNr(aDto.getEinheitCNr());
									}

									getAuftragpositionFac().createAuftragposition(posDto, theClientDto);

								}

							} catch (ParseException e) {
								// Datum falsch
								fehler = "Datum aus Spalte 18 '" + liefertermin
										+ "' kann nicht konvertiert werden. Format muss 'dd.MM.yyyy' sein.";

								e.printStackTrace();
							}

						} else {
							// Kein Kunde mit Lieferantenr gefunden

							fehler = "Es konnte kein Kunde mit der Fremdsystemnummer '" + abladestelleNr
									+ "' aus Spalte 1 im Kundenstamm gefunden werden.";

						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				} else {
					// Fehler keine Lieferanten
					fehler = "Keine Lieferantennummer in Spalte 1 gefunden.";

				}

				if (fehler.length() > 0) {
					// Menge falsch
					ArrayList al = new ArrayList();
					al.add(fehler);
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_SON_IMPORT, al,
							new Exception("FEHLER_AUFTRAG_SON_IMPORT"));
				}

			}

		}

		return 0;
	}

	public int importiereWooCommerce_CSV(
			LinkedHashMap<String, ArrayList<ImportWooCommerceCsvDto>> hmNachBestellnummerGruppiert,
			TheClientDto theClientDto) {

		
		boolean bPartnerZusammenziehen =false;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_WOOCOMMERCE_PARTNER_ZUSAMMENZIEHEN);

			 bPartnerZusammenziehen = (Boolean) parameterDto.getCWertAsObject();
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		} 
		
		Iterator it = hmNachBestellnummerGruppiert.keySet().iterator();
		String fehler = "";
		while (it.hasNext()) {
			String bestellnummer = (String) it.next();

			ArrayList<ImportWooCommerceCsvDto> alZeilen = hmNachBestellnummerGruppiert.get(bestellnummer);
			if (alZeilen.size() > 0) {

				bestellnummer = "SHOP-" + bestellnummer;

				ImportWooCommerceCsvDto positionDtoFuerAuftrag = alZeilen.get(0);

				Session session3 = FLRSessionFactory.getFactory().openSession();
				String sQuery3 = "FROM FLRAuftrag ab WHERE ab.mandant_c_nr ='" + theClientDto.getMandant()
						+ "' AND ab.c_bestellnummer='" + bestellnummer + "'";
				org.hibernate.Query query3 = session3.createQuery(sQuery3);
				query3.setMaxResults(1);

				List<?> results3 = query3.list();
				Iterator<?> resultListIterator3 = results3.iterator();
				if (resultListIterator3.hasNext()) {

					fehler = "Es ist bereits ein Auftrag mit der Bestellnummer " + bestellnummer + " vorhanden.";
					// Menge falsch
					ArrayList al = new ArrayList();
					al.add(fehler);
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_WOOCOMMERCE_IMPORT, al,
							new Exception("FEHLER_AUFTRAG_WOOCOMMERCE_IMPORT"));
				}

				int defaultLieferzeitAuftrag = 14;

				try {
					ParametermandantDto parametermandantLieferzeitDto = getParameterFac().getMandantparameter(
							theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG);
					defaultLieferzeitAuftrag = ((Integer) parametermandantLieferzeitDto.getCWertAsObject()).intValue();

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				int iNachkommastellenPreis = getUINachkommastellenPreisVK(theClientDto.getMandant());

				Integer kundeIIdRechnungsadresse = erstelleBzwHoleKundeRechnungsadresseAnhandEmail(
						positionDtoFuerAuftrag.anrede, positionDtoFuerAuftrag.email,
						positionDtoFuerAuftrag.rechnungsadresseName1, positionDtoFuerAuftrag.rechnungsadresseName2,
						positionDtoFuerAuftrag.rechnungsadresseFirma, positionDtoFuerAuftrag.rechnungsadresseStrasse,
						positionDtoFuerAuftrag.rechnungsadresseLand, positionDtoFuerAuftrag.rechnungsadressePLZ,
						positionDtoFuerAuftrag.rechnungsadresseOrt, positionDtoFuerAuftrag.rechnungsadresseTelefon,
						true,bPartnerZusammenziehen, theClientDto);

				KundeDto kundeDtoRechnungsadresse = getKundeFac().kundeFindByPrimaryKey(kundeIIdRechnungsadresse,
						theClientDto);

				// SP9495 lt. WH: Wenn Kunde und Lieferadresse lt. CSV gleich (ohne Company),
				// dann
				// ist wird auch in HV die gleiche Adresse verwendetn

				String reName1 = "";
				if (positionDtoFuerAuftrag.rechnungsadresseName1 != null) {
					reName1 = positionDtoFuerAuftrag.rechnungsadresseName1;
				}

				String reName2 = "";
				if (positionDtoFuerAuftrag.rechnungsadresseName2 != null) {
					reName2 = positionDtoFuerAuftrag.rechnungsadresseName2;
				}

				String reNameStrasse = "";
				if (positionDtoFuerAuftrag.rechnungsadresseStrasse != null) {
					reNameStrasse = positionDtoFuerAuftrag.rechnungsadresseStrasse;
				}

				String reNamePLZ = "";
				if (positionDtoFuerAuftrag.rechnungsadressePLZ != null) {
					reNamePLZ = positionDtoFuerAuftrag.rechnungsadressePLZ;
				}

				String reNameORT = "";
				if (positionDtoFuerAuftrag.rechnungsadresseOrt != null) {
					reNameORT = positionDtoFuerAuftrag.rechnungsadresseOrt;
				}

				String reNameLAND = "";
				if (positionDtoFuerAuftrag.rechnungsadresseLand != null) {
					reNameLAND = positionDtoFuerAuftrag.rechnungsadresseLand;
				}

				String lfName1 = "";
				if (positionDtoFuerAuftrag.lieferadresseName1 != null) {
					lfName1 = positionDtoFuerAuftrag.lieferadresseName1;
				}

				String lfName2 = "";
				if (positionDtoFuerAuftrag.lieferadresseName2 != null) {
					lfName2 = positionDtoFuerAuftrag.lieferadresseName2;
				}

				String lfNameStrasse = "";
				if (positionDtoFuerAuftrag.lieferadresseStrasse != null) {
					lfNameStrasse = positionDtoFuerAuftrag.lieferadresseStrasse;
				}

				String lfNamePLZ = "";
				if (positionDtoFuerAuftrag.lieferadressePLZ != null) {
					lfNamePLZ = positionDtoFuerAuftrag.lieferadressePLZ;
				}

				String lfNameORT = "";
				if (positionDtoFuerAuftrag.lieferadresseOrt != null) {
					lfNameORT = positionDtoFuerAuftrag.lieferadresseOrt;
				}

				String lfNameLAND = "";
				if (positionDtoFuerAuftrag.lieferadresseLand != null) {
					lfNameLAND = positionDtoFuerAuftrag.lieferadresseLand;
				}

				Integer kundeIIdLieferadresse = null;
				if (reName1.equals(lfName1) && reName2.equals(lfName2) && reNameStrasse.equals(lfNameStrasse)
						&& reNamePLZ.equals(lfNamePLZ) && reNameORT.equals(lfNameORT)
						&& reNameLAND.equals(lfNameLAND)) {
					kundeIIdLieferadresse = kundeIIdRechnungsadresse;
				} else {
					kundeIIdLieferadresse = erstelleBzwHoleKundeLieferadresseadresseAnhandName(
							positionDtoFuerAuftrag.anrede, positionDtoFuerAuftrag.lieferadresseName1,
							positionDtoFuerAuftrag.lieferadresseName2, positionDtoFuerAuftrag.rechnungsadresseFirma,
							positionDtoFuerAuftrag.lieferadresseStrasse, positionDtoFuerAuftrag.lieferadresseLand,
							positionDtoFuerAuftrag.lieferadressePLZ, positionDtoFuerAuftrag.lieferadresseOrt,bPartnerZusammenziehen,
							theClientDto);
				}

				String belegdatum = positionDtoFuerAuftrag.belegdatum;

				BigDecimal bruttopreisGesamt = null;
				try {
					bruttopreisGesamt = new BigDecimal(positionDtoFuerAuftrag.bruttopreis);
				} catch (NumberFormatException e) {

					fehler = "Bruttopreis aus Spalte Order Total Amount '" + positionDtoFuerAuftrag.bruttopreis
							+ "' kann nicht in einen Wert konvertiert werden";

					e.printStackTrace();
					break;
				}

				BigDecimal versandkosten = null;
				try {
					versandkosten = new BigDecimal(positionDtoFuerAuftrag.versandkosten);
				} catch (NumberFormatException e) {

					fehler = "Versandkosten aus Spalte Order Shipping Amount '" + positionDtoFuerAuftrag.versandkosten
							+ "' kann nicht in einen Wert konvertiert werden";

					e.printStackTrace();
					break;
				}

				try {

					// Lieferart

					Integer artikelIIdVersandkosten = null;

					Query query = em.createNamedQuery("LieferartsprfindByCBezeichnungLocaleCNr");
					query.setParameter(1, positionDtoFuerAuftrag.lieferart);
					query.setParameter(2, theClientDto.getLocUiAsString());
					Collection<Lieferartspr> cl = query.getResultList();

					Lieferart lieferartZuVerwenden = null;

					for (Lieferartspr lieferartspr : cl) {
						Lieferart lieferart = em.find(Lieferart.class, lieferartspr.getPk().getLieferartIId());

						if (lieferart.getMandantCNr().equals(theClientDto.getMandant())) {
							lieferartZuVerwenden = lieferart;
						}

					}

					if (lieferartZuVerwenden == null) {
						fehler = "Lieferart aus Spalte Shipping Method Title '" + positionDtoFuerAuftrag.lieferart
								+ "' kann nicht im System gefunden werden";
						break;
					} else {
						artikelIIdVersandkosten = lieferartZuVerwenden.getArtikelIIdVersand();
					}

					Query queryZZ = em.createNamedQuery("ZahlungszielsprfindByCBezeichnungLocaleCNr");
					queryZZ.setParameter(1, positionDtoFuerAuftrag.zahlungsziel);
					queryZZ.setParameter(2, theClientDto.getLocUiAsString());
					Collection<Zahlungszielspr> clZZ = queryZZ.getResultList();

					Zahlungsziel zahlungszielZuVerwenden = null;

					for (Zahlungszielspr zahlungszielspr : clZZ) {
						Zahlungsziel zahlungsziel = em.find(Zahlungsziel.class,
								zahlungszielspr.getPk().getZahlungszielIId());

						if (zahlungsziel.getMandantCNr().equals(theClientDto.getMandant())) {
							zahlungszielZuVerwenden = zahlungsziel;
						}

					}

					if (zahlungszielZuVerwenden == null) {
						fehler = "Zahlungsziel aus Spalte Payment Method Title '" + positionDtoFuerAuftrag.zahlungsziel
								+ "' kann nicht im System gefunden werden";
						break;
					}

					AuftragDto auftragDto = null;

					MwstsatzDto mwstsatzDto = null;

					try {
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
						java.util.Date date = formatter.parse(belegdatum);

						// Auftrag erzeugen
						auftragDto = new AuftragDto();
						auftragDto.setMandantCNr(theClientDto.getMandant());
						auftragDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
						auftragDto.setTBelegdatum(new java.sql.Timestamp(date.getTime()));
						auftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);
						auftragDto.setKundeIIdAuftragsadresse(kundeDtoRechnungsadresse.getIId());
						auftragDto.setKundeIIdLieferadresse(kundeIIdLieferadresse);
						auftragDto.setKundeIIdRechnungsadresse(kundeDtoRechnungsadresse.getIId());
						auftragDto.setKostIId(kundeDtoRechnungsadresse.getKostenstelleIId());

						auftragDto.setPersonalIIdVertreter(theClientDto.getIDPersonal());

						auftragDto.setXExternerkommentar(positionDtoFuerAuftrag.kommentar);

						GregorianCalendar calendar = new GregorianCalendar();
						calendar.setTimeInMillis(auftragDto.getTBelegdatum().getTime());
						calendar.add(Calendar.DATE, defaultLieferzeitAuftrag);
						Timestamp aktuelleDatumPlusLieferZeit = new Timestamp(calendar.getTimeInMillis());

						auftragDto.setDLiefertermin(aktuelleDatumPlusLieferZeit);
						auftragDto.setBLieferterminUnverbindlich(Helper.boolean2Short(false));
						auftragDto.setCBestellnummer(bestellnummer);
						auftragDto.setCAuftragswaehrung(kundeDtoRechnungsadresse.getCWaehrung());
						auftragDto
								.setFWechselkursmandantwaehrungzubelegwaehrung(
										new Double(getLocaleFac()
												.getWechselkurs2(theClientDto.getSMandantenwaehrung(),
														kundeDtoRechnungsadresse.getCWaehrung(), theClientDto)
												.doubleValue()));

						auftragDto.setLieferartIId(lieferartZuVerwenden.getIId());
						auftragDto.setZahlungszielIId(zahlungszielZuVerwenden.getIId());
						auftragDto.setSpediteurIId(kundeDtoRechnungsadresse.getSpediteurIId());
						auftragDto.setLagerIIdAbbuchungslager(kundeDtoRechnungsadresse.getLagerIIdAbbuchungslager());

						ArtikelDto aDto = getArtikelFac().artikelFindByCNrOhneExc(positionDtoFuerAuftrag.artikelnummer,
								theClientDto);
						if (aDto != null) {
							auftragDto.setCBezProjektbezeichnung(aDto.getCKBezAusSpr());
						}

						Integer auftragIId = createAuftrag(auftragDto, theClientDto);

						auftragDto.setIId(auftragIId);

						mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(
								kundeDtoRechnungsadresse.getMwstsatzbezIId(), auftragDto.getTBelegdatum(),
								theClientDto);

					} catch (ParseException e) {
						// Datum falsch
						fehler = "Datum aus Spalte Order Date '" + belegdatum
								+ "' kann nicht konvertiert werden. Format muss 'yyyy-MM-dd hh:mm' sein.";

						e.printStackTrace();
					}

					for (int i = 0; i < alZeilen.size(); i++) {
						fehler = "";
						ImportWooCommerceCsvDto positionDto = alZeilen.get(i);

						BigDecimal menge = null;
						try {
							menge = new BigDecimal(positionDto.menge);
						} catch (NumberFormatException e) {
							fehler = "Menge aus Spalte Quantity '" + positionDto.menge
									+ "' kann nicht in einen Wert konvertiert werden. Zeile: " + (i + 1);
							e.printStackTrace();
							break;
						}

						BigDecimal artikelpreis = null;
						try {
							artikelpreis = new BigDecimal(positionDto.artikelpreis);
						} catch (NumberFormatException e) {

							fehler = "Artikelpreis aus Spalte Item Cost '" + positionDtoFuerAuftrag.artikelpreis
									+ "' kann nicht in einen Wert konvertiert werden";

							e.printStackTrace();
							break;
						}

						AuftragpositionDto posDto = new AuftragpositionDto();
						posDto.setBelegIId(auftragDto.getIId());
						posDto.setNMenge(menge);
						posDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
						posDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

						posDto.setTUebersteuerbarerLiefertermin(auftragDto.getDLiefertermin());

						posDto.setMwstsatzIId(mwstsatzDto.getIId());

						ArtikelDto aDto = getArtikelFac().artikelFindByCNrOhneExc(positionDto.artikelnummer,
								theClientDto);

						if (aDto == null) {

							// HANDEINGABE
							posDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE);
							posDto.setCBez(positionDto.artikelnummer);
							posDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);

							AuftragpositionDto posDtoText = new AuftragpositionDto();
							posDtoText.setBelegIId(auftragDto.getIId());
							posDtoText.setBNettopreisuebersteuert(Helper.boolean2Short(false));
							posDtoText.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

							posDtoText.setXTextinhalt("ACHTUNG: Es konnte kein Artikel mit der Artikelnummer '"
									+ positionDto.artikelnummer + "' im HeliumV-Artikelstamm gefunden werden");

							posDtoText.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE);

							getAuftragpositionFac().createAuftragposition(posDtoText, theClientDto);

						} else {
							posDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);

							posDto.setArtikelIId(aDto.getIId());
							posDto.setEinheitCNr(aDto.getEinheitCNr());
						}

						// posDto = preiseBerechnenWennBasisNettopreis(mwstsatzDto, artikelpreis,
						// BigDecimal.ZERO,
						// posDto, iNachkommastellenPreis);

						// Preis kommt auch VK-Preisfindung
						Integer auftragpositionIId = getAuftragpositionFac().createAuftragposition(posDto,
								theClientDto);
						posDto = getAuftragpositionFac().auftragpositionFindByPrimaryKey(auftragpositionIId);

						// Wenn mehr als 10ct. Abweichung, dann zusaetzliche Textposition

						BigDecimal dAbweichungZumVKPreis = posDto
								.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte().subtract(artikelpreis)
								.abs();
						if (dAbweichungZumVKPreis.doubleValue() > 0.10) {
							AuftragpositionDto posDtoText = new AuftragpositionDto();
							posDtoText.setBelegIId(auftragDto.getIId());
							posDtoText.setBNettopreisuebersteuert(Helper.boolean2Short(false));
							posDtoText.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

							posDtoText.setXTextinhalt("ACHTUNG: Der VK-Preis fuer Artikel '" + positionDto.artikelnummer
									+ "' aus der Import-Datei ("
									+ Helper.formatZahl(artikelpreis, iNachkommastellenPreis, theClientDto.getLocUi())
									+ ") weicht um mehr als 10 Cent vom HeliumV- VK-Preis ab");

							posDtoText.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE);

							getAuftragpositionFac().createAuftragposition(posDtoText, theClientDto);
						}

					}

					// Versandkosten

					if (versandkosten.doubleValue() != 0) {

						if (artikelIIdVersandkosten == null) {
							fehler = "Die Lieferart '" + positionDtoFuerAuftrag.lieferart
									+ "' hat keinen Versandkostenartikel hinterlegt.";
							break;
						}

						AuftragpositionDto posDtoVersandkosten = new AuftragpositionDto();
						posDtoVersandkosten.setBelegIId(auftragDto.getIId());
						posDtoVersandkosten.setNMenge(BigDecimal.ONE);
						posDtoVersandkosten.setBNettopreisuebersteuert(Helper.boolean2Short(true));
						posDtoVersandkosten.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

						posDtoVersandkosten.setTUebersteuerbarerLiefertermin(auftragDto.getDLiefertermin());

						posDtoVersandkosten.setMwstsatzIId(mwstsatzDto.getIId());

						posDtoVersandkosten.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);

						posDtoVersandkosten.setArtikelIId(artikelIIdVersandkosten);
						posDtoVersandkosten.setEinheitCNr(SystemFac.EINHEIT_STUECK);

						posDtoVersandkosten = preiseBerechnenWennBasisNettopreis(mwstsatzDto, versandkosten,
								BigDecimal.ZERO, posDtoVersandkosten, iNachkommastellenPreis);

						getAuftragpositionFac().createAuftragposition(posDtoVersandkosten, false, theClientDto);
					}

					/// PJ22372

					berechneBeleg(auftragDto.getIId(), theClientDto);

					auftragDto = auftragFindByPrimaryKey(auftragDto.getIId());

					BigDecimal auftragsWertUST = getBelegVerkaufFac().getGesamtwertInMandantwaehrungUST(
							getAuftragpositionFac().auftragpositionFindByAuftrag(auftragDto.getIId()), auftragDto,
							theClientDto);

					BigDecimal bruttoAuftragBerechnet = auftragDto.getNGesamtauftragswertInAuftragswaehrung()
							.add(auftragsWertUST);

					BigDecimal diff = bruttoAuftragBerechnet.subtract(bruttopreisGesamt);

					if (diff.abs().doubleValue() > 1) {

						AuftragpositionDto posDtoText = new AuftragpositionDto();
						posDtoText.setBelegIId(auftragDto.getIId());
						posDtoText.setBNettopreisuebersteuert(Helper.boolean2Short(false));
						posDtoText.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

						posDtoText.setXTextinhalt(
								"ACHTUNG: Der Bruttogesamtpreis 'Order Total Amount' aus der Import-Datei ("
										+ Helper.formatZahl(bruttopreisGesamt, iNachkommastellenPreis,
												theClientDto.getLocUi())
										+ ") weicht um mehr als 1 EUR vom berechneten HeliumV- Auftrags-Bruttowert ("
										+ Helper.formatZahl(bruttoAuftragBerechnet, iNachkommastellenPreis,
												theClientDto.getLocUi())
										+ ") ab");

						posDtoText.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE);

						getAuftragpositionFac().createAuftragposition(posDtoText, theClientDto);

					}

					if (diff.abs().doubleValue() > 0) {
						// Korrekturartikel einfuegen

						coinRoundingService.createRoundingPositionForAuftrag(auftragDto, diff.negate(), theClientDto);

						berechneBeleg(auftragDto.getIId(), theClientDto);

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
		}

		if (fehler.length() > 0) {
			// Menge falsch
			ArrayList al = new ArrayList();
			al.add(fehler);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_WOOCOMMERCE_IMPORT, al,
					new Exception("FEHLER_AUFTRAG_WOOCOMMERCE_IMPORT"));
		}

		return 0;
	}

	private AuftragpositionDto preiseBerechnenWennBasisNettopreis(MwstsatzDto mwstsatzDto, BigDecimal nettopreis,
			BigDecimal artikelRabatt, AuftragpositionDto posDto, int nachkommastellen) {
		// PREISE berechnen

		BigDecimal mwstBetrag = nettopreis.multiply(new BigDecimal(mwstsatzDto.getFMwstsatz()).movePointLeft(2));

		posDto.setNMwstbetrag(mwstBetrag);
		BigDecimal bdNettopreis = nettopreis;
		posDto.setNNettoeinzelpreis(bdNettopreis);
		posDto.setNBruttoeinzelpreis(bdNettopreis.add(mwstBetrag));

		posDto.setNRabattbetrag(BigDecimal.ZERO);

		posDto.setNEinzelpreis(bdNettopreis);
		posDto.setFRabattsatz(0D);
		posDto.setFZusatzrabattsatz(0D);

		return posDto;
	}

	private AuftragpositionDto preiseBerechnen(MwstsatzDto mwstsatzDto, BigDecimal bruttopreis,
			BigDecimal artikelRabatt, AuftragpositionDto posDto, int nachkommastellen) {
		// PREISE berechnen

		BigDecimal bruttoPositionspreis = bruttopreis.subtract(artikelRabatt);

		// MWSTBetrag berechnen
		BigDecimal bdMwstSatz = new BigDecimal(mwstsatzDto.getFMwstsatz()).movePointLeft(2);
		BigDecimal bdBetragBasis = bruttoPositionspreis.divide(BigDecimal.ONE.add(bdMwstSatz), nachkommastellen,
				BigDecimal.ROUND_HALF_EVEN);
		BigDecimal mwstBetrag = bruttoPositionspreis.subtract(bdBetragBasis);

		posDto.setNMwstbetrag(mwstBetrag);

		BigDecimal bdNettopreis = bruttoPositionspreis.subtract(mwstBetrag);

		posDto.setNNettoeinzelpreis(bdNettopreis);
		posDto.setNBruttoeinzelpreis(bdNettopreis.add(mwstBetrag));

		BigDecimal bdRabattBetragBasis = artikelRabatt.divide(BigDecimal.ONE.add(bdMwstSatz), nachkommastellen,
				BigDecimal.ROUND_HALF_EVEN);

		posDto.setNRabattbetrag(bdRabattBetragBasis);

		BigDecimal bEinzelpreis = bdNettopreis.add(bdRabattBetragBasis);

		Double rabbattsatz = new Double(Helper.getProzentsatzBD(bEinzelpreis, bdRabattBetragBasis, 4).doubleValue());

		posDto.setNEinzelpreis(bEinzelpreis);
		posDto.setFRabattsatz(rabbattsatz);
		posDto.setFZusatzrabattsatz(0D);

		return posDto;
	}

	public int importiereVAT_XLSX(Integer kundeIId, Integer ansprechpartnerIId,
			LinkedHashMap<String, ArrayList<ImportVATXlsxDto>> hmNachBestellnummerGruppiert,
			TheClientDto theClientDto) {

		Iterator it = hmNachBestellnummerGruppiert.keySet().iterator();

		while (it.hasNext()) {
			String bestellnummer = (String) it.next();

			ArrayList<ImportVATXlsxDto> zeilen = hmNachBestellnummerGruppiert.get(bestellnummer);
			if (zeilen.size() > 0) {

				String fehler = "";
				int iNachkommastellenPreis = getUINachkommastellenPreisVK(theClientDto.getMandant());
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

				try {

					String waehrung = zeilen.get(0).getWaehrung();
					WaehrungDto whgDto = getLocaleFac().waehrungFindByPrimaryKeyWithNull(waehrung);

					if (whgDto == null) {

						ArrayList al = new ArrayList();
						al.add("Waehrung " + zeilen.get(0).getWaehrung()
								+ " aus Spalte 'waehrCd' konnte nicht gefunden werden. Zeile: "
								+ zeilen.get(0).getZeile());
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VAT_IMPORT, al,
								new Exception("FEHLER_AUFTRAG_VAT_IMPORT"));
					}

					// Auftrag erzeugen
					AuftragDto auftragDto = new AuftragDto();
					auftragDto.setMandantCNr(theClientDto.getMandant());
					auftragDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
					auftragDto.setTBelegdatum(Helper.cutTimestamp(getTimestamp()));
					auftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);
					auftragDto.setKundeIIdAuftragsadresse(kundeDto.getIId());

					// Lieferadresse = Filialadresse
					Integer kundeIIdLieferadresse = kundeDto.getIId();
					if (zeilen.get(0).getLieferadresse() != null) {

						Query query = em.createQuery("SELECT p FROM Partner p WHERE p.cFilialnummer='"
								+ zeilen.get(0).getLieferadresse() + "'");

						Collection<?> cl = query.getResultList();

						if (cl.size() == 1) {
							Partner p = (Partner) cl.iterator().next();

							KundeDto kundeDtoLieferadrese = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
									p.getIId(), theClientDto.getMandant(), theClientDto);
							if (kundeDtoLieferadrese != null) {
								kundeIIdLieferadresse = kundeDtoLieferadrese.getIId();
							} else {
								// FEHLER
								fehler = "Es konnte kein Kunde mit der Filialnummer " + zeilen.get(0).getLieferadresse()
										+ " gefunden werden. Zeile: " + zeilen.get(0).getZeile();
							}
						} else if (cl.size() == 0) {
							fehler = "Es konnte kein Partner mit der Filialnummer " + zeilen.get(0).getLieferadresse()
									+ " gefunden werden. Zeile: " + zeilen.get(0).getZeile();
						} else {
							fehler = "Es wurden mehrere Partner mit der Filialnummer "
									+ zeilen.get(0).getLieferadresse() + " gefunden. Zeile: "
									+ zeilen.get(0).getZeile();
						}

					}

					auftragDto.setKundeIIdLieferadresse(kundeIIdLieferadresse);
					auftragDto.setKundeIIdRechnungsadresse(kundeDto.getIId());
					auftragDto.setKostIId(kundeDto.getKostenstelleIId());
					auftragDto.setDLiefertermin(zeilen.get(0).getLiefertermin());
					auftragDto.setCBestellnummer(zeilen.get(0).getBestellnummer());
					auftragDto.setCAuftragswaehrung(zeilen.get(0).getWaehrung());
					auftragDto.setFWechselkursmandantwaehrungzubelegwaehrung(
							new Double(getLocaleFac().getWechselkurs2(theClientDto.getSMandantenwaehrung(),
									kundeDto.getCWaehrung(), theClientDto).doubleValue()));

					auftragDto.setLieferartIId(kundeDto.getLieferartIId());
					auftragDto.setZahlungszielIId(kundeDto.getZahlungszielIId());
					auftragDto.setSpediteurIId(kundeDto.getSpediteurIId());
					auftragDto.setLagerIIdAbbuchungslager(kundeDto.getLagerIIdAbbuchungslager());

					Integer auftragIId = createAuftrag(auftragDto, theClientDto);

					final MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(
							kundeDto.getMwstsatzbezIId(), auftragDto.getTBelegdatum(), theClientDto);

					for (int i = 0; i < zeilen.size(); i++) {
						ImportVATXlsxDto positionDto = zeilen.get(i);

						AuftragpositionDto posDto = new AuftragpositionDto();
						posDto.setBelegIId(auftragIId);
						posDto.setNMenge(positionDto.getMenge());
						posDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
						posDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
						posDto.setTUebersteuerbarerLiefertermin(auftragDto.getDLiefertermin());
						posDto.setCZusatzbez(positionDto.getZusatzbezeichnung());
						/*
						 * MwstsatzDto mwstsatzDto = getMandantFac()
						 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(),
						 * theClientDto);
						 */
						posDto.setMwstsatzIId(mwstsatzDto.getIId());

						posDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);

						ArtikelDto aDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(
								"VAT" + positionDto.getArtikelnummer(), theClientDto.getMandant());

						String texteingabe = "";

						if (aDto != null) {

							aDto = getArtikelFac().artikelFindByPrimaryKey(aDto.getIId(), theClientDto);

							// INDEXPRUEFUNG
							if (aDto.getCKBezAusSpr() != null && aDto.getCRevision() != null) {

								String indexAusBestText = "";

								if (positionDto.getBest_text() != null) {

									int position = positionDto.getBest_text().indexOf(aDto.getCKBezAusSpr());

									if (position > 0) {

										String text = positionDto.getBest_text().substring(position);

										String nameIndex = "Index ";

										int positionIndex = text.indexOf(nameIndex);
										if (positionIndex > 0) {
											text = text.substring(positionIndex + nameIndex.length());

											int posEnde = text.indexOf("\n");
											if (posEnde > 0) {
												text = text.substring(0, posEnde);
											}

											indexAusBestText = text;

										}

									}

								}

								if (!indexAusBestText.equals(aDto.getCRevision())) {

									// FEHLER
									texteingabe += "Indexpr\u00fcfung f\u00fcr Artikel VAT"
											+ positionDto.getArtikelnummer() + " fehlgeschlagen: Index aus XLSX:'"
											+ indexAusBestText + "' <> Index aus HeliumV-Artikel '"
											+ aDto.getCRevision() + "'\n";
								}

							} else {
								if (aDto.getCKBezAusSpr() == null) {
									texteingabe += "Zeichnungsnummer im HeliumV-Artikel ist leer\n";
								}
								if (aDto.getCRevision() == null) {
									texteingabe += "Znr.Rev. im HeliumV-Artikel ist leer";
								}

							}

							if (positionDto.getPreis() != null) {

								VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(
										aDto.getIId(), kundeDto.getIId(),

										posDto.getNMenge(), new java.sql.Date(auftragDto.getTBelegdatum().getTime()),
										kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), mwstsatzDto.getIId(),
										auftragDto.getCAuftragswaehrung(), theClientDto);

								VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

								if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {

									if (Math.abs(kundenVKPreisDto.nettopreis.doubleValue()
											- positionDto.getPreis().doubleValue()) >= 0.001f) {
										texteingabe += "Preis f\u00fcr Artikel VAT" + positionDto.getArtikelnummer()
												+ " aus VK-Preisfindung "
												+ Helper.rundeKaufmaennisch(kundenVKPreisDto.nettopreis,
														iNachkommastellenPreis)
												+ " weicht von Preis aus Import " + Helper.rundeKaufmaennisch(
														positionDto.getPreis(), iNachkommastellenPreis)
												+ " ab.";
									}

								}

							}

							posDto.setArtikelIId(aDto.getIId());
							posDto.setEinheitCNr(aDto.getEinheitCNr());

							posDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
							posDto.setNEinzelpreis(positionDto.getPreis());
							posDto.setNRabattbetrag(new BigDecimal(0));
							posDto.setFRabattsatz(0D);
							posDto.setFZusatzrabattsatz(0D);
							posDto.setNNettoeinzelpreis(positionDto.getPreis());
							posDto.setNEinzelpreisplusversteckteraufschlag(positionDto.getPreis());
							posDto.setNNettoeinzelpreisplusversteckteraufschlag(positionDto.getPreis());
							posDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(positionDto.getPreis());

							BigDecimal mwstBetrag = Helper.getProzentWert(posDto.getNNettoeinzelpreis(),
									new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);

							posDto.setNMwstbetrag(mwstBetrag);
							posDto.setNBruttoeinzelpreis(positionDto.getPreis().add(mwstBetrag));

							Integer auftragpositionIId = getAuftragpositionFac().createAuftragposition(posDto,
									theClientDto);

							if (texteingabe.length() > 0) {
								AuftragpositionDto posDtoText = new AuftragpositionDto();
								posDtoText.setBelegIId(auftragIId);
								posDtoText.setBNettopreisuebersteuert(Helper.boolean2Short(false));
								posDtoText.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
								posDtoText.setXTextinhalt(texteingabe);

								posDtoText.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE);

								getAuftragpositionFac().createAuftragposition(posDtoText, theClientDto);
							}

						} else {
							// Fehler
							fehler = "Artikel VAT" + positionDto.getArtikelnummer()
									+ " aus Spalte 5 konnte nicht gefunden werden. Zeile: " + positionDto.getZeile();
						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				if (fehler.length() > 0) {
					// Menge falsch
					ArrayList al = new ArrayList();
					al.add(fehler);
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VAT_IMPORT, al,
							new Exception("FEHLER_AUFTRAG_VAT_IMPORT"));
				}

			}

		}

		return 0;
	}

	public AuftragDto istAuftragBeiAnderemMandantenHinterlegt(Integer bestellungIId) {
		Query query = em.createNamedQuery("AuftragfindByBestellungIIdAndererMandant");
		query.setParameter(1, bestellungIId);

		Collection<?> cl = query.getResultList();

		if (cl.size() > 0) {
			return getAuftragFac().auftragFindByPrimaryKey(((Auftrag) cl.iterator().next()).getIId());
		} else {
			return null;
		}

	}

	public void erzeugeAuftragAusBestellungeinesAnderenMandanten(Integer bestellungIId, String mandantCNr_Zielmandant,
			TheClientDto theClientDto_Quelle) {

		try {
			BestellungDto bsDto = getBestellungFac().bestellungFindByPrimaryKey(bestellungIId);

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(mandantCNr_Zielmandant,
					theClientDto_Quelle);
			MandantDto mandantDtoQuelle = getMandantFac().mandantFindByPrimaryKey(theClientDto_Quelle.getMandant(),
					theClientDto_Quelle);

			TheClientDto theClientDto_Zielmandant = null;
			try {
				/*
				 * theClientDto_Zielmandant = getLogonFac().logon(
				 * Helper.getFullUsername(sUser), Helper.getMD5Hash((sUser +
				 * sPassword).toCharArray()), Helper.string2Locale(mandantDto.getPartnerDto()
				 * .getLocaleCNrKommunikation()), mandantCNr_Zielmandant, new
				 * Timestamp(System.currentTimeMillis()));
				 */
				theClientDto_Zielmandant = getLogonFac().logonIntern(
						Helper.string2Locale(mandantDto.getPartnerDto().getLocaleCNrKommunikation()),
						mandantCNr_Zielmandant);
			} catch (EJBExceptionLP e1) {
				e1.printStackTrace();
				// Meldung bringen
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BENUTZER_AUTOMATISCHE_BELEGANLAGE_NICHT_DEFINIERT,
						new Exception("FEHLER_BENUTZER_AUTOMATISCHE_BELEGANLAGE_NICHT_DEFINIERT"));

			}

			int iEinheitWiederbeschaffung = 1;
			int iAutomatikregel = 0;
			int iPositionsschwellwert = 0;
			try {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto_Quelle.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_AUTOMATIKREGEL);
				iAutomatikregel = (Integer) parameter.getCWertAsObject();

				parameter = getParameterFac().getMandantparameter(theClientDto_Quelle.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_AUTOMATIK_POSITIONSSCHWELLWERT);
				iPositionsschwellwert = (Integer) parameter.getCWertAsObject();

				parameter = getParameterFac().getMandantparameter(theClientDto_Zielmandant.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);
				String einheit = (String) parameter.getCWert();
				if (einheit.equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
					iEinheitWiederbeschaffung = 7;
				} else if (parameter.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
					iEinheitWiederbeschaffung = 1;
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			AuftragDto auftragDto = new AuftragDto();
			auftragDto.setBestellungIIdAndererMandant(bestellungIId);
			auftragDto.setMandantCNr(mandantCNr_Zielmandant);
			auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
			// Kunde muss vorhanden sein

			KundeDto kdDto_Ziel = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(mandantDtoQuelle.getPartnerIId(),
					mandantDto.getCNr(), theClientDto_Zielmandant);
			if (kdDto_Ziel == null) {
				// Throw Exception

				PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(mandantDtoQuelle.getPartnerIId(),
						theClientDto_Quelle);

				ArrayList<Object> al = new ArrayList<Object>();
				al.add(pDto.formatAnrede());
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_AUS_BESTELLUNG_KUNDE_NICHT_ANGELEGT, al,
						new Exception(pDto.formatAnrede() + " nicht als Kunde angelegt"));

			}

			auftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);

			auftragDto.setTBelegdatum(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));

			auftragDto.setDFinaltermin(bsDto.getDLiefertermin());
			auftragDto.setDLiefertermin(bsDto.getDLiefertermin());
			auftragDto.setDBestelldatum(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));
			auftragDto.setCBestellnummer(bsDto.getCNr());

			auftragDto.setPersonalIIdVertreter(kdDto_Ziel.getPersonaliIdProvisionsempfaenger());
			auftragDto.setCAuftragswaehrung(kdDto_Ziel.getCWaehrung());
			auftragDto.setFWechselkursmandantwaehrungzubelegwaehrung(
					getLocaleFac().getWechselkurs2(theClientDto_Quelle.getSMandantenwaehrung(),
							kdDto_Ziel.getCWaehrung(), theClientDto_Zielmandant).doubleValue());

			auftragDto.setKundeIIdAuftragsadresse(kdDto_Ziel.getIId());
			auftragDto.setKundeIIdLieferadresse(kdDto_Ziel.getIId());

			if (bsDto.getPartnerIIdLieferadresse() != null) {
				KundeDto kdDtoLieferadresse_Ziel = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
						bsDto.getPartnerIIdLieferadresse(), mandantDto.getCNr(), theClientDto_Zielmandant);

				if (kdDtoLieferadresse_Ziel == null) {
					// Throw Exception

					PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(bsDto.getPartnerIIdLieferadresse(),
							theClientDto_Quelle);

					ArrayList<Object> al = new ArrayList<Object>();
					al.add(pDto.formatAnrede());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_AUS_BESTELLUNG_LIEFERADRESSE_NICHT_ANGELEGT,
							al, new Exception(pDto.formatAnrede() + " nicht als Kunde angelegt"));

				} else {
					auftragDto.setKundeIIdLieferadresse(kdDtoLieferadresse_Ziel.getIId());
				}

			}

			auftragDto.setKundeIIdRechnungsadresse(kdDto_Ziel.getIId());

			// PJ8442
			auftragDto.setAnsprechpartnerIIdLieferadresse(bsDto.getAnsprechpartnerIIdLieferadresse());

			if (bsDto.getPersonalIIdAnforderer() != null) {
				PersonalDto anfordererDto = getPersonalFac().personalFindByPrimaryKey(bsDto.getPersonalIIdAnforderer(),
						theClientDto_Quelle);

				AnsprechpartnerDto[] anspDtos = getAnsprechpartnerFac()
						.ansprechpartnerFindByPartnerIIdAndPartnerIIdAnsprechpartner(kdDto_Ziel.getPartnerIId(),
								anfordererDto.getPartnerIId(), theClientDto_Zielmandant);

				if (anspDtos != null && anspDtos.length > 0) {
					auftragDto.setAnsprechpartnerIId(anspDtos[0].getIId());
					auftragDto.setAnsprechpartnerIIdRechnungsadresse(anspDtos[0].getIId());
				}

			} else {
				// aus Parameter
				ParametermandantDto anspParameterDto = getParameterFac().getMandantparameter(
						theClientDto_Zielmandant.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_AUFTRAG_ANSP_VORBESETZEN);
				if ((Boolean) anspParameterDto.getCWertAsObject() == true) {
					AnsprechpartnerDto anspDto = getAnsprechpartnerFac().ansprechpartnerFindErstenEinesPartnersOhneExc(
							kdDto_Ziel.getPartnerIId(), theClientDto_Zielmandant);
					if (anspDto != null) {
						auftragDto.setAnsprechpartnerIId(anspDto.getIId());
						auftragDto.setAnsprechpartnerIIdRechnungsadresse(anspDto.getIId());
					}
				}
			}

			auftragDto.setFAllgemeinerRabattsatz(0D);
			auftragDto.setFProjektierungsrabattsatz(0D);
			auftragDto.setFVersteckterAufschlag(0D);

			auftragDto.setLieferartIId(kdDto_Ziel.getLieferartIId());
			auftragDto.setSpediteurIId(kdDto_Ziel.getSpediteurIId());
			auftragDto.setZahlungszielIId(kdDto_Ziel.getZahlungszielIId());

			auftragDto.setBLieferterminUnverbindlich(Helper.boolean2Short(false));

			auftragDto.setKostIId(kdDto_Ziel.getKostenstelleIId());
			auftragDto.setBTeillieferungMoeglich(bsDto.getBTeillieferungMoeglich());
			auftragDto.setBPoenale(bsDto.getBPoenale());
			auftragDto.setILeihtage(0);
			auftragDto.setIGarantie(0);
			auftragDto.setLagerIIdAbbuchungslager(kdDto_Ziel.getLagerIIdAbbuchungslager());

			// PJ19570 Freigabe nur, wenn Automatikregel
			if (iAutomatikregel > 0) {

				ParametermandantDto parameterDto = getParameterFac().getMandantparameter(
						theClientDto_Zielmandant.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_AUFTRAGSFREIGABE);

				boolean bAuftragsfreigabe = (Boolean) parameterDto.getCWertAsObject();
				if (bAuftragsfreigabe == true) {
					auftragDto.setTAuftragsfreigabe(new Timestamp(System.currentTimeMillis()));
					auftragDto.setPersonalIIdAuftragsfreigabe(theClientDto_Zielmandant.getIDPersonal());
				}
			}

			HashMap<Integer, BigDecimal> hmLetzteAufrundung = new HashMap<Integer, BigDecimal>();

			Integer auftragIId = createAuftrag(auftragDto, theClientDto_Zielmandant);

			// Positionsarten Ident/Hand und Texteingabe importieren
			BestellpositionDto[] bsposDtos = getBestellpositionFac().bestellpositionFindByBestellung(bestellungIId,
					theClientDto_Quelle);

			boolean bPositionsschwellwertUeberschritten = false;

			for (int i = 0; i < bsposDtos.length; i++) {
				BestellpositionDto bsPosDto = bsposDtos[i];

				AuftragpositionDto auftragpositionDto = new AuftragpositionDto();
				auftragpositionDto = (AuftragpositionDto) getBelegpositionkonvertierungFac()
						.cloneBelegpositionDtoFromBelegpositionDto(auftragpositionDto, bsPosDto,
								theClientDto_Zielmandant);
				auftragpositionDto.setBelegIId(auftragIId);

				auftragpositionDto.setPositionsartCNr(bsPosDto.getPositionsartCNr());

				auftragpositionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

				if (bsPosDto.getTUebersteuerterLiefertermin() != null) {
					auftragpositionDto.setTUebersteuerbarerLiefertermin(bsPosDto.getTUebersteuerterLiefertermin());
				} else {
					auftragpositionDto.setTUebersteuerbarerLiefertermin(auftragDto.getDLiefertermin());
				}

				if (bsPosDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
						|| bsPosDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

					befuellePreisfelderAnhandVKPreisfindung(auftragpositionDto, auftragDto.getTBelegdatum(),
							auftragDto.getKundeIIdAuftragsadresse(), auftragDto.getCAuftragswaehrung(),
							theClientDto_Zielmandant);

					// Schwellwerte berechenen, bzw. Automatikregel anwenden
					if (bsPosDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
						if (iAutomatikregel > 0) {

							if (iAutomatikregel == 1) {
								BigDecimal bdMengeBestellt = auftragpositionDto.getNMenge();

								// SP4649

								if (hmLetzteAufrundung.containsKey(auftragpositionDto.getArtikelIId())) {
									BigDecimal bdLetzteAufrundung = hmLetzteAufrundung
											.get(auftragpositionDto.getArtikelIId());

									// 4673
									if (bdLetzteAufrundung.subtract(bdMengeBestellt).doubleValue() >= 0) {
										hmLetzteAufrundung.put(auftragpositionDto.getArtikelIId(),
												bdLetzteAufrundung.subtract(bdMengeBestellt));

										continue;
									}

									bdMengeBestellt = bdMengeBestellt.subtract(bdLetzteAufrundung);

									hmLetzteAufrundung.remove(auftragpositionDto.getArtikelIId());

								}

								BigDecimal bdLiefermenge = bdMengeBestellt;

								BigDecimal einheitAufzurunden = null;

								StuecklisteDto stklDto = getStuecklisteFac()
										.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
												auftragpositionDto.getArtikelIId(),
												theClientDto_Zielmandant.getMandant());

								ArtikellieferantDto alDto = null;

								if (stklDto != null && !Helper.short2boolean(stklDto.getBFremdfertigung())) {
									// VKPReis und auf Fertigungssatzgroesse
									// aufrunden
									ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(stklDto.getArtikelIId(),
											theClientDto_Zielmandant);

									if (aDto.getFFertigungssatzgroesse() != null) {
										einheitAufzurunden = new BigDecimal(aDto.getFFertigungssatzgroesse());
									}

									// Preis bleibt VK-PReis

									// PJ19780 Set kann nicht bestellt werden

									if (stklDto.getStuecklisteartCNr()
											.equals(StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
										ArrayList<Object> al = new ArrayList<Object>();
										al.add(aDto.getCNr());
										throw new EJBExceptionLP(
												EJBExceptionLP.FEHLER_BESTELLUNG_ANDERER_MANDANT_SET_KANN_NICHT_BESTELLT_WERDEN,
												al, new Exception(
														"FEHLER_BESTELLUNG_ANDERER_MANDANT_SET_KANN_NICHT_BESTELLT_WERDEN"));

									}

								} else {

									ArtikellieferantDto[] dtos = getArtikelFac()
											.artikellieferantfindByArtikelIIdTPreisgueltigab(
													auftragpositionDto.getArtikelIId(),
													new java.sql.Date(auftragDto.getTBelegdatum().getTime()),
													theClientDto_Zielmandant);
									if (dtos.length > 0) {
										alDto = getArtikelFac()
												.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
														auftragpositionDto.getArtikelIId(), dtos[0].getLieferantIId(),
														new java.sql.Date(auftragDto.getTBelegdatum().getTime()), null,
														theClientDto_Zielmandant);

										// Preis
										alDto = getArtikelFac().getArtikelEinkaufspreis(
												auftragpositionDto.getArtikelIId(), null, bdMengeBestellt,
												auftragDto.getCAuftragswaehrung(),
												new java.sql.Date(auftragDto.getTBelegdatum().getTime()),
												theClientDto_Zielmandant);
										if (alDto != null && alDto.getNEinzelpreis() != null) {
											auftragpositionDto.setNEinzelpreis(alDto.getNEinzelpreis());
											auftragpositionDto.setFRabattsatz(alDto.getFRabatt());
											auftragpositionDto.setNNettoeinzelpreis(alDto.getNNettopreis());
											auftragpositionDto.setBRabattsatzuebersteuert(alDto.getBRabattbehalten());

											auftragpositionDto.setNMaterialzuschlag(alDto.getNMaterialzuschlag());

											auftragpositionDto.setNBruttoeinzelpreis(alDto.getNNettopreis());
										}

									}

									if (alDto != null && alDto.getNVerpackungseinheit() != null
											&& alDto.getNVerpackungseinheit().doubleValue() != 0) {
										einheitAufzurunden = alDto.getNVerpackungseinheit();
									}
								}

								// Aufrunden
								if (einheitAufzurunden != null && einheitAufzurunden.doubleValue() != 0) {

									if (bdMengeBestellt.doubleValue() <= einheitAufzurunden.doubleValue()) {

										hmLetzteAufrundung.put(auftragpositionDto.getArtikelIId(),
												einheitAufzurunden.subtract(bdLiefermenge));

										bdLiefermenge = einheitAufzurunden;

									} else {
										double rest = bdLiefermenge.doubleValue() % einheitAufzurunden.doubleValue();

										hmLetzteAufrundung.put(auftragpositionDto.getArtikelIId(),
												einheitAufzurunden.subtract(new BigDecimal(rest)));

										if (rest > 0) {
											bdLiefermenge = new BigDecimal((bdLiefermenge.doubleValue() - rest)
													+ einheitAufzurunden.doubleValue());
										}

									}

								}

								BigDecimal bestellwert = bdMengeBestellt
										.multiply(auftragpositionDto.getNNettoeinzelpreis());

								BigDecimal bestellwertandhandLiefermenge = bdLiefermenge
										.multiply(auftragpositionDto.getNNettoeinzelpreis());

								if (bestellwertandhandLiefermenge.subtract(bestellwert)
										.doubleValue() > iPositionsschwellwert) {
									bPositionsschwellwertUeberschritten = true;
								}

								auftragpositionDto.setNMenge(bdLiefermenge);

							}

						}
					}

					if (bsPosDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
						// Handeingabe: Preis = Preis aus BS-Pos in
						// ZielWaehrung umgerechnet

						auftragpositionDto.setNNettoeinzelpreis(
								getLocaleFac().rechneUmInMandantenWaehrung(bsPosDto.getNNettogesamtpreis(),
										new BigDecimal(auftragDto.getFWechselkursmandantwaehrungzubelegwaehrung())));

						auftragpositionDto.setNEinzelpreis(
								getLocaleFac().rechneUmInMandantenWaehrung(bsPosDto.getNNettoeinzelpreis(),
										new BigDecimal(auftragDto.getFWechselkursmandantwaehrungzubelegwaehrung())));

						auftragpositionDto.setFRabattsatz(bsPosDto.getDRabattsatz());
						auftragpositionDto.setNRabattbetrag(
								getLocaleFac().rechneUmInMandantenWaehrung(bsPosDto.getNRabattbetrag(),
										new BigDecimal(auftragDto.getFWechselkursmandantwaehrungzubelegwaehrung())));
						/*
						 * MwstsatzDto mwstsatzAktuell =
						 * getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
						 * auftragpositionDto.getMwstsatzIId(), theClientDto_Zielmandant);
						 */

						MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(
								auftragpositionDto.getMwstsatzIId(), theClientDto_Zielmandant);

						MwstsatzDto mwstsatzAktuell = getMandantFac().mwstsatzZuDatumValidate(
								mwstsatzDto.getIIMwstsatzbezId(), auftragDto.getTBelegdatum(),
								theClientDto_Zielmandant);
						BigDecimal mwstBetrag = bsPosDto.getNNettoeinzelpreis()
								.multiply(new BigDecimal(mwstsatzAktuell.getFMwstsatz()).movePointLeft(2));

						auftragpositionDto.setNMwstbetrag(mwstBetrag);
						auftragpositionDto.setNBruttoeinzelpreis(bsPosDto.getNNettoeinzelpreis().add(mwstBetrag));

					}

				}

				if (bsPosDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN)) {

					MediastandardDto msDto = getMediaFac()
							.mediastandardFindByPrimaryKey(auftragpositionDto.getMediastandardIId());

					auftragpositionDto.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE);

					auftragpositionDto.setMediastandardIId(null);
					auftragpositionDto.setXTextinhalt(msDto.getOMediaText());

				}

				auftragpositionDto.setBestellpositionIId(bsPosDto.getIId());

				getAuftragpositionFac().createAuftragposition(auftragpositionDto, false, theClientDto_Zielmandant);

			}

			// Wenn nirgends der Positionsschwellwert ueberschritten worden ist,
			// dann automatisch aktivieren
			// SP7203
			if (iAutomatikregel == 2 || (iAutomatikregel > 0 && bPositionsschwellwertUeberschritten == false)) {
				getAuftragFac().aktiviereAuftrag(auftragIId, theClientDto_Zielmandant);
			}

			getLogonFac().logout(theClientDto_Zielmandant);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	@org.jboss.ejb3.annotation.TransactionTimeout(1000)
	public String wiederholendeAuftraegeMitPreisgueltigkeitAnpassen(Timestamp tDatumPreisgueltigkeit,
			double dAbweichung, TheClientDto theClientDto) {

		// Alle offenen und teilerledigten

		Session session3 = FLRSessionFactory.getFactory().openSession();
		String sQuery3 = "FROM FLRAuftrag a WHERE a.auftragart_c_nr='" + AuftragServiceFac.AUFTRAGART_WIEDERHOLEND
				+ "' AND a.mandant_c_nr='" + theClientDto.getMandant() + "' AND a.t_lauftermin<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(System.currentTimeMillis()))
				+ "' AND a.auftragstatus_c_nr IN('" + AuftragServiceFac.AUFTRAGSTATUS_OFFEN + "','"
				+ AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT + "') ORDER BY a.c_nr ASC";

		org.hibernate.Query query3 = session3.createQuery(sQuery3);

		int iNachkommastellenPreis = getUINachkommastellenPreisVK(theClientDto.getMandant());

		List<?> results3 = query3.list();
		Iterator<?> resultListIterator3 = results3.iterator();

		ArrayList<AuftragpositionDto> alErhoeht = new ArrayList<AuftragpositionDto>();
		ArrayList<AuftragpositionDto> alPreisNullOderMaterialzuschlag = new ArrayList<AuftragpositionDto>();
		ArrayList<AuftragpositionDto> alPreisAusserhalbGrenzen = new ArrayList<AuftragpositionDto>();
		ArrayList<AuftragpositionDto> alNichtGeaendert = new ArrayList<AuftragpositionDto>();
		while (resultListIterator3.hasNext()) {

			FLRAuftrag flrAuftrag = (FLRAuftrag) resultListIterator3.next();

			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(flrAuftrag.getI_id());

			try {
				AuftragpositionDto[] apDtos = getAuftragpositionFac()
						.auftragpositionFindByAuftragIIdNMengeNotNull(auftragDto.getIId(), theClientDto);

				for (int j = 0; j < apDtos.length; j++) {
					// Positionen um Faktor erhoehen und Auftragswert
					// neu
					// berechnen
					if (apDtos[j].getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
							&& apDtos[j].getNNettoeinzelpreis() != null) {

						AuftragpositionDto apDto = apDtos[j];

						if (apDto.getNMaterialzuschlag() != null && apDto.getNMaterialzuschlag().doubleValue() != 0) {
							alPreisNullOderMaterialzuschlag.add(apDto);
							continue;
						}

						BigDecimal bdNettoeinzelpreisVorhanden = apDtos[j].getNNettoeinzelpreis();
						if (bdNettoeinzelpreisVorhanden.doubleValue() == 0) {
							alPreisNullOderMaterialzuschlag.add(apDto);
							continue;
						}

						apDto = (AuftragpositionDto) befuellePreisfelderAnhandVKPreisfindung(apDto,
								tDatumPreisgueltigkeit, auftragDto.getKundeIIdRechnungsadresse(),
								auftragDto.getCAuftragswaehrung(), theClientDto);

						if (bdNettoeinzelpreisVorhanden.equals(apDto.getNNettoeinzelpreis())) {
							// Nicht geaendert

							alNichtGeaendert.add(apDto);

						} else {

							// Wenn Abweichung groesser als xx Prozent

							BigDecimal untereGrenze = Helper.getWertPlusProzent(bdNettoeinzelpreisVorhanden,
									new BigDecimal(dAbweichung), iNachkommastellenPreis);
							BigDecimal obereGrenze = Helper.getWertPlusProzent(bdNettoeinzelpreisVorhanden,
									new BigDecimal(-dAbweichung), iNachkommastellenPreis);

							if (apDto.getNNettoeinzelpreis().doubleValue() < untereGrenze.doubleValue()
									|| apDto.getNNettoeinzelpreis().doubleValue() > obereGrenze.doubleValue()) {
								alPreisAusserhalbGrenzen.add(apDto);
							} else {
								alErhoeht.add(apDto);

								getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(apDto, theClientDto);

							}

						}

					}

				}

				auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
				updateAuftrag(auftragDto, null, theClientDto);

				aktiviereAuftrag(auftragDto.getIId(), theClientDto);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		String zeilen = "";

		if (!alPreisNullOderMaterialzuschlag.isEmpty()) {
			zeilen += "Nicht erhoeht, weil Einzelpreis 0 bzw. Materialzuschlag auf Position: <br />";

			for (int i = 0; i < alPreisNullOderMaterialzuschlag.size(); i++) {

				try {
					AuftragpositionDto apDto = alPreisNullOderMaterialzuschlag.get(i);
					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(apDto.getArtikelIId(), theClientDto);
					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(apDto.getBelegIId());
					zeilen += auftragDto.getCNr() + " " + getAuftragpositionFac().getPositionNummer(apDto.getIId())
							+ " " + aDto.getCNr() + "<br />";
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
		}

		if (!alPreisAusserhalbGrenzen.isEmpty()) {
			zeilen += "Nicht angepasst, weil Einzelpreis ausserhalb der Toleranz: <br />";

			for (int i = 0; i < alPreisAusserhalbGrenzen.size(); i++) {

				try {
					AuftragpositionDto apDto = alPreisAusserhalbGrenzen.get(i);
					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(apDto.getArtikelIId(), theClientDto);
					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(apDto.getBelegIId());
					zeilen += auftragDto.getCNr() + " " + getAuftragpositionFac().getPositionNummer(apDto.getIId())
							+ " " + aDto.getCNr() + "<br />";
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
		}

		if (!alErhoeht.isEmpty()) {
			zeilen += "Positionspreis an VK-Preis angepasst: <br />";

			for (int i = 0; i < alErhoeht.size(); i++) {

				try {
					AuftragpositionDto apDto = alErhoeht.get(i);
					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(apDto.getArtikelIId(), theClientDto);
					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(apDto.getBelegIId());
					zeilen += auftragDto.getCNr() + " " + getAuftragpositionFac().getPositionNummer(apDto.getIId())
							+ " " + aDto.getCNr() + "<br />";
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
		}

		if (!alNichtGeaendert.isEmpty()) {
			zeilen += "Preis nicht angepasst, da dieser bereits dem VK-Preis entsprach: <br />";

			for (int i = 0; i < alNichtGeaendert.size(); i++) {

				try {
					AuftragpositionDto apDto = alNichtGeaendert.get(i);
					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(apDto.getArtikelIId(), theClientDto);
					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(apDto.getBelegIId());
					zeilen += auftragDto.getCNr() + " " + getAuftragpositionFac().getPositionNummer(apDto.getIId())
							+ " " + aDto.getCNr() + "<br />";
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
		}

		String s = "<html><body><font face=\"monospace\" size =3 color=\"#000000\">" + zeilen + "</font></body></html>";

		return s;
	}

	public ArrayList<Integer> wiederholendeAuftraegeUmIndexAnpassen(TheClientDto theClientDto) {
		AuftragDto[] auftragDtos = auftragFindByMandantCNrAuftragartCNrStatusCNr(theClientDto.getMandant(),
				AuftragServiceFac.AUFTRAGART_WIEDERHOLEND, AuftragServiceFac.AUFTRAGSTATUS_OFFEN, theClientDto);

		Integer iNachkommastellenPreis = getUINachkommastellenPreisVK(theClientDto.getMandant());
		ArrayList<Integer> alErhoeht = new ArrayList<Integer>();
		for (int i = 0; i < auftragDtos.length; i++) {
			if (auftragDtos[i].getNIndexanpassung() != null && auftragDtos[i].getNIndexanpassung().doubleValue() != 0) {

				if (auftragDtos[i].getTLauftermin() != null
						&& auftragDtos[i].getTLauftermin().getTime() <= System.currentTimeMillis()) {

					try {
						AuftragpositionDto[] apDtos = getAuftragpositionFac()
								.auftragpositionFindByAuftragIIdNMengeNotNull(auftragDtos[i].getIId(), theClientDto);

						for (int j = 0; j < apDtos.length; j++) {
							// Positionen um Faktor erhoehen und Auftragswert
							// neu
							// berechnen
							if (apDtos[j].getNNettoeinzelpreis() != null) {

								AuftragpositionDto apDto = apDtos[j];

								BigDecimal diffEinzelpreis = Helper.getProzentWert(apDto.getNEinzelpreis(),
										auftragDtos[i].getNIndexanpassung(), iNachkommastellenPreis);

								BigDecimal bdEinzelpreisNeu = apDto.getNEinzelpreis().add(diffEinzelpreis);
								apDto.setNEinzelpreis(bdEinzelpreisNeu);

								//
								BigDecimal diffNettoeinzelpreis = Helper.getProzentWert(
										apDtos[j].getNNettoeinzelpreis(), auftragDtos[i].getNIndexanpassung(),
										iNachkommastellenPreis);

								BigDecimal bdNettoeinzelpreisNeu = apDtos[j].getNNettoeinzelpreis()
										.add(diffNettoeinzelpreis);
								apDto.setNNettoeinzelpreis(bdNettoeinzelpreisNeu);

								// Rabatt berechnen
								apDto.setFZusatzrabattsatz(0D);
								BigDecimal rabattbetrag = bdEinzelpreisNeu.subtract(bdNettoeinzelpreisNeu);

								apDto.setNRabattbetrag(rabattbetrag);

								// Rabattsatz berechnen
								Double dRabattsatz = 0D;
								if (bdEinzelpreisNeu.doubleValue() != 0) {
									new Double(dRabattsatz = rabattbetrag.doubleValue() / bdEinzelpreisNeu.doubleValue()
											* 100);
								}

								apDto.setFRabattsatz(dRabattsatz);

								MwstsatzDto mwstsatzDto = getMandantFac()
										.mwstsatzFindByPrimaryKey(apDto.getMwstsatzIId(), theClientDto);
								BigDecimal mwstBetrag = Helper.getProzentWert(bdNettoeinzelpreisNeu,
										new BigDecimal(mwstsatzDto.getFMwstsatz()), iNachkommastellenPreis);
								apDto.setNMwstbetrag(mwstBetrag);

								apDto.setNBruttoeinzelpreis(bdNettoeinzelpreisNeu.add(mwstBetrag));

								getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(apDto, theClientDto);

							}

						}

						auftragDtos[i].setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
						updateAuftrag(auftragDtos[i], null, theClientDto);

						aktiviereAuftrag(auftragDtos[i].getIId(), theClientDto);

						alErhoeht.add(auftragDtos[i].getIId());

						// Loggen

						PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
						Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VORZUG);
						IndexanpassungLog log = new IndexanpassungLog(pk, theClientDto.getIDPersonal(),
								auftragDtos[i].getIId(), auftragDtos[i].getNIndexanpassung(),
								new Timestamp(System.currentTimeMillis()));
						em.merge(log);
						em.flush();

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}
			}
		}
		return alErhoeht;
	}

	@Override
	public Integer erzeugeAuftragAusAuftrag(Integer auftragId, HvOptional<Timestamp> belegDatum,
			Timestamp tLieferterminUebersteuert, TheClientDto theClientDto) {
		checkAuftragIId(auftragId);
		Validator.dtoNotNull(belegDatum, "belegDatum");

		AuftragDto auftragBasisDto = getAuftragFac().auftragFindByPrimaryKey(auftragId);

		boolean bFreienAuftragStattAbrufAnlegen = false;
		if (auftragBasisDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)
				&& auftragBasisDto.getAuftragIIdRahmenauftrag() != null) {
			if (getAuftragFac().auftragFindByPrimaryKey(auftragBasisDto.getAuftragIIdRahmenauftrag()).getStatusCNr()
					.equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				bFreienAuftragStattAbrufAnlegen = true;
			}
		}

		Integer iIdAuftragKopie = null;

		try {
			GregorianCalendar calendar = new GregorianCalendar();
			AuftragDto auftragDto = (AuftragDto) auftragBasisDto.clone();
			if (belegDatum.isPresent()) {
				auftragDto.setTBelegdatum(belegDatum.get());
			}

			if (bFreienAuftragStattAbrufAnlegen == true) {
				auftragDto.setAuftragIIdRahmenauftrag(null);
				auftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);
			}

			// SP6638 Ist Vertreter noch im Unternehmen
			auftragDto.setPersonalIIdVertreter(
					pruefeVertreterUndHoleBeiBedarfAktuellenAusKunde(auftragDto.getPersonalIIdVertreter(),
							auftragDto.getKundeIIdAuftragsadresse(), auftragDto.getTBelegdatum(), theClientDto));

			auftragDto.setFWechselkursmandantwaehrungzubelegwaehrung(
					new Double(getLocaleFac().getWechselkurs2(theClientDto.getSMandantenwaehrung(),
							auftragDto.getCAuftragswaehrung(), theClientDto).doubleValue()));

			ParametermandantDto parametermandantLieferzeitDto = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG);
			int defaultLieferzeitAuftrag = ((Integer) parametermandantLieferzeitDto.getCWertAsObject()).intValue();

			calendar = new GregorianCalendar();
			calendar.setTimeInMillis(auftragDto.getTBelegdatum().getTime());
			calendar.add(Calendar.DATE, defaultLieferzeitAuftrag);
			Timestamp aktuelleDatumPlusLieferZeit = new Timestamp(calendar.getTimeInMillis());

			ParametermandantDto parametermandantLieferdatumDto = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAG_KOPIEREN_LIEFERDATUM_UEBERNEHMEN);
			if ((Boolean) parametermandantLieferdatumDto.getCWertAsObject()) {
				auftragDto.setDLiefertermin(auftragBasisDto.getDLiefertermin());
				auftragDto.setDFinaltermin(auftragBasisDto.getDLiefertermin());
			} else {
				auftragDto.setDLiefertermin(aktuelleDatumPlusLieferZeit);
				auftragDto.setDFinaltermin(aktuelleDatumPlusLieferZeit);
			}
			auftragDto.setDBestelldatum(auftragDto.getTBelegdatum());

			// PJ22399
			if (tLieferterminUebersteuert != null) {
				auftragDto.setDLiefertermin(tLieferterminUebersteuert);
				auftragDto.setDFinaltermin(tLieferterminUebersteuert);
				aktuelleDatumPlusLieferZeit = tLieferterminUebersteuert;
			}

			iIdAuftragKopie = createAuftrag(auftragDto, theClientDto);
			auftragDto = auftragFindByPrimaryKey(iIdAuftragKopie);

			// PJ 15507 Auftragdokumente kopieren
			AuftragauftragdokumentDto[] auftragauftragdokumentDtos = getAuftragServiceFac()
					.auftragauftragdokumentFindByAuftragIId(auftragId);
			if (auftragauftragdokumentDtos != null && auftragauftragdokumentDtos.length > 0) {
				ArrayList<AuftragdokumentDto> al = new ArrayList<AuftragdokumentDto>();

				for (int i = 0; i < auftragauftragdokumentDtos.length; i++) {
					AuftragdokumentDto dto = new AuftragdokumentDto();
					dto.setIId(auftragauftragdokumentDtos[i].getAuftragdokumentIId());

					al.add(dto);
				}

				getAuftragServiceFac().updateAuftragdokumente(iIdAuftragKopie, al);
			}

			// alle Positionen kopieren
			AuftragpositionDto[] aAuftragpositionBasis = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragId);
			Integer positionIId = null;
			Integer positionIIdSet = null;
			for (int i = 0; i < aAuftragpositionBasis.length; i++) {
				AuftragpositionDto auftragpositionDto = (AuftragpositionDto) aAuftragpositionBasis[i].clone();

				if (auftragBasisDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
					auftragpositionDto.setAuftragpositionIIdRahmenposition(
							aAuftragpositionBasis[i].getAuftragpositionIIdRahmenposition());
				}

				// Wenn sich sie MWST seither geaendert hat
				if (auftragpositionDto.getMwstsatzIId() != null) {
					MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzZuDatumEvaluate(
							new MwstsatzId(auftragpositionDto.getMwstsatzIId()), auftragDto.getTBelegdatum(),
							theClientDto);

					BigDecimal mwstBetrag = Helper.getProzentWert(auftragpositionDto.getNNettoeinzelpreis(),
							new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
					auftragpositionDto.setMwstsatzIId(mwstsatzDto.getIId());
					auftragpositionDto.setNMwstbetrag(mwstBetrag);
					auftragpositionDto.setNBruttoeinzelpreis(auftragpositionDto.getNNettoeinzelpreis().add(mwstBetrag));
				}

				if (aAuftragpositionBasis[i].getPositioniIdArtikelset() != null) {
					auftragpositionDto.setPositioniIdArtikelset(positionIIdSet);
				}
				auftragpositionDto.setBelegIId(iIdAuftragKopie);
				if ((Boolean) parametermandantLieferdatumDto.getCWertAsObject()) {
					auftragpositionDto.setTUebersteuerbarerLiefertermin(auftragBasisDto.getDLiefertermin());
				} else {
					auftragpositionDto.setTUebersteuerbarerLiefertermin(aktuelleDatumPlusLieferZeit);
				}

				// PJ22399
				if (tLieferterminUebersteuert != null) {
					auftragpositionDto.setTUebersteuerbarerLiefertermin(tLieferterminUebersteuert);
				}

				if (auftragpositionDto.getTypCNr() != null) {

					if (auftragpositionDto.isPosition()) {
						if (auftragpositionDto.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_BEGINN)) {
							positionIId = getAuftragpositionFac().createAuftragposition(auftragpositionDto, false,
									theClientDto);
						} else if (auftragpositionDto.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {
							getAuftragpositionFac().createAuftragposition(auftragpositionDto, false, theClientDto);
						}
					} else {
						auftragpositionDto.setPositioniId(positionIId);
						getAuftragpositionFac().createAuftragposition(auftragpositionDto, false, theClientDto);
					}
				} else {
					if (aAuftragpositionBasis[i].isIntelligenteZwischensumme()) {
						ZwsPositionMapper mapper = new ZwsPositionMapper(getAuftragpositionFac(),
								getAuftragpositionFac());
						mapper.map(aAuftragpositionBasis[i], auftragpositionDto, iIdAuftragKopie);
					}

					if (auftragpositionDto.getPositioniIdArtikelset() == null) {

						if (auftragBasisDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)
								&& bFreienAuftragStattAbrufAnlegen == false) {
							getAuftragRahmenAbrufFac().createAbrufpositionZuRahmenposition(auftragpositionDto,
									theClientDto);
						} else {
							positionIIdSet = getAuftragpositionFac().createAuftragposition(auftragpositionDto, false,
									theClientDto);
						}

					} else {
						getAuftragpositionFac().createAuftragposition(auftragpositionDto, false, theClientDto);
					}
				}
			}

			// kopieren der Auftrageigenschaften
			PaneldatenDto[] aPaneldatenDtoBasis = getPanelFac()
					.paneldatenFindByPanelCNrCKey(PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN, auftragId.toString());

			PaneldatenDto[] datenKorrigiert = new PaneldatenDto[aPaneldatenDtoBasis.length];

			for (int y = 0; y < aPaneldatenDtoBasis.length; y++) {
				PaneldatenDto paneldatenDto = (PaneldatenDto) aPaneldatenDtoBasis[y].clone();
				paneldatenDto.setCKey(iIdAuftragKopie.toString());
				datenKorrigiert[y] = paneldatenDto;
			}

			if (datenKorrigiert != null) {
				getPanelFac().createPaneldaten(datenKorrigiert, theClientDto);
			}

			mindermengenzuschlagEntfernen(auftragDto, theClientDto);

			myLogger.exit("Der Auftrag wurde mit " + aAuftragpositionBasis.length + " Positionen erzeugt.");
		} catch (RemoteException ex) {
			throw EJBExcFactory.respectOld(ex);
		}

		return iIdAuftragKopie;
	}

	/**
	 * Methode zum Erzeugen eines neues Auftrags als Kopie eines bestehenden
	 * Auftrags.<br>
	 * <p>
	 * Als Belegdatum wird das aktuelle Datum verwendet (kommt indirekt aus dem
	 * AuftragDto.clone()
	 * </p>
	 * <p>
	 * Es werden auch die Positionen kopiert.
	 * </p>
	 * 
	 * @param iIdAuftragI  PK des bestehenden Auftrags
	 * @param theClientDto der aktuelle Benutzer
	 * @return Integer PK des neuen Auftrags
	 * @throws EJBExceptionLP Ausnahme
	 */
	@Override
	public Integer erzeugeAuftragAusAuftrag(Integer iIdAuftragI, TheClientDto theClientDto) throws EJBExceptionLP {
		return erzeugeAuftragAusAuftrag(iIdAuftragI, HvOptional.empty(), null, theClientDto);
	}

	public Integer erzeugeNegativeMengeAusAuftrag(Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			Auftrag auftrag = em.find(Auftrag.class, iIdAuftragI);
			try {
				Query query = em.createNamedQuery("AuftragpositionfindByAuftragNegativeMenge");
				query.setParameter(1, iIdAuftragI);
				Collection<?> c = query.getResultList();
				for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
					Auftragposition pos = ((Auftragposition) iter.next());

					// vorher noch die Set-Positionen entfernen
					Query querySet = em.createNamedQuery("AuftragpositionfindByPositionIIdArtikelsetAll");
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
				AuftragpositionDto auftragpositionDto = (AuftragpositionDto) aAuftragpositionBasis[i].clone();
				auftragpositionDto.setBelegIId(iIdAuftragI);
				if (auftragpositionDto.getNMenge() != null) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId(),
							theClientDto);
					if (artikelDto.getArtgruIId() != null) {
						ArtgruDto artgruDto = getArtikelFac().artgruFindByPrimaryKey(artikelDto.getArtgruIId(),
								theClientDto);
						if (Helper.short2boolean(artgruDto.getBRueckgabe())) {
							auftragpositionDto.setNMenge(auftragpositionDto.getNMenge().negate());
							auftragpositionDto.setNOffeneMenge(auftragpositionDto.getNMenge());
							auftragpositionDto.setTUebersteuerbarerLiefertermin(auftrag.getTFinaltermin());

							auftragpositionDto.setNBruttoeinzelpreis(new BigDecimal(0));
							auftragpositionDto.setNNettoeinzelpreis(new BigDecimal(0));

							auftragpositionDto.setNNettoeinzelpreisplusversteckteraufschlag(new BigDecimal(0));
							auftragpositionDto.setNEinzelpreis(new BigDecimal(0));
							auftragpositionDto.setNEinzelpreisplusversteckteraufschlag(new BigDecimal(0));
							auftragpositionDto.setVerleihIId(null);

							auftragpositionDto
									.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(new BigDecimal(0));

							auftragpositionDto.setISort(null);
							getAuftragpositionFac().createAuftragposition(auftragpositionDto, theClientDto);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdAuftragI;
	}

	public Integer vorhandenenLieferscheinEinesAuftagsHolenBzwNeuAnlegen(Integer auftragIId,
			TheClientDto theClientDto) {
		// PJ18738
		// Nachsehen, ob es zu dem Auftrag bereits einen LS gibt, der 'Angelegt'
		// ist

		Session session3 = FLRSessionFactory.getFactory().openSession();
		String sQuery3 = "FROM FLRLieferschein flrLieferschein WHERE flrLieferschein.auftrag_i_id=" + auftragIId
				+ " AND flrLieferschein.lieferscheinstatus_status_c_nr='" + LocaleFac.STATUS_ANGELEGT
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
			return erzeugeLieferscheinAusAuftrag(auftragIId, null, null, theClientDto);
		}
	}

	/**
	 * Methode zum Erzeugen eines eines Lieferscheins aus einem bestehenden Auftrag.
	 * <br>
	 * Nicht mengenbehaftete Positionen werden ebebfalls kopiert, mengenbehaftete
	 * Positionen muessen vom Benutzer gezielt uebernommen werden.
	 * 
	 * @param iIdAuftragI      PK des bestehenden Auftrags
	 * @param lieferscheinDtoI der Benutzer kann bestimmte Eigenschaften des
	 *                         Auftrags uebersteuern
	 * @param theClientDto     der aktuelle Benutzer
	 * @return Integer PK des neuen Lieferscheins
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer erzeugeLieferscheinAusAuftrag(Integer iIdAuftragI, LieferscheinDto lieferscheinDtoI,
			Double dRabattAusRechnungsadresse, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		AuftragDto auftragBasisDto = getAuftragFac().auftragFindByPrimaryKey(iIdAuftragI);

		Integer iIdLieferschein = null;

		try {
			LieferscheinDto lieferscheinDto = (LieferscheinDto) auftragBasisDto.cloneAsLieferscheinDto();

			// PJ21028

			ParametermandantDto parametermandantKopftextuebernehmenDto = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_KOPFTEXT_UEBERNEHMEN);
			boolean bKopftextUebernehmen = (Boolean) parametermandantKopftextuebernehmenDto.getCWertAsObject();
			if (bKopftextUebernehmen && auftragBasisDto.getCKopftextUebersteuert() != null) {
				lieferscheinDto.setCLieferscheinKopftextUeberschrieben(auftragBasisDto.getCKopftextUebersteuert());
			}

			// PJ18344
			// Aufgrund von PJ19958 wieder auskommentiert, da diese Info nun aus
			// dem Auftrag Kommt
			/*
			 * ParametermandantDto parameterDto = getParameterFac()
			 * .getMandantparameter(theClientDto.getMandant(),
			 * ParameterFac.KATEGORIE_LIEFERSCHEIN,
			 * ParameterFac.PARAMETER_LS_DEFAULT_VERRECHENBAR);
			 * 
			 * lieferscheinDto.setBVerrechenbar(Helper .boolean2Short((Boolean)
			 * parameterDto.getCWertAsObject()));
			 */

			// PJ19958
			lieferscheinDto.setBVerrechenbar(getAuftragServiceFac()
					.verrechenbarFindByPrimaryKey(auftragBasisDto.getVerrechenbarIId()).getBVerrechenbar());

			if (dRabattAusRechnungsadresse != null) {
				lieferscheinDto.setFAllgemeinerRabattsatz(dRabattAusRechnungsadresse);
			}

			lieferscheinDto.setFWechselkursmandantwaehrungzubelegwaehrung(
					new Double(getLocaleFac().getWechselkurs2(theClientDto.getSMandantenwaehrung(),
							lieferscheinDto.getWaehrungCNr(), theClientDto).doubleValue()));

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(lieferscheinDto.getKundeIIdLieferadresse(),
					theClientDto);
			lieferscheinDto.setLagerIId(auftragBasisDto.getLagerIIdAbbuchungslager());
			lieferscheinDto.setZiellagerIId(kundeDto.getPartnerDto().getLagerIIdZiellager());

			/**
			 * Auskommentiert von CK am 22.10.2008, wegen Projekt 08/13491 lieferscheinDto
			 * .setPersonalIIdVertreter(theClientDto.getIDPersonal());
			 */

			// SP6638 Ist Vertreter noch im Unternehmen
			lieferscheinDto.setPersonalIIdVertreter(pruefeVertreterUndHoleBeiBedarfAktuellenAusKunde(
					lieferscheinDto.getPersonalIIdVertreter(), lieferscheinDto.getKundeIIdLieferadresse(),
					lieferscheinDto.getTBelegdatum(), theClientDto));

			// der Benutzer kann bestimmte vorbelegte Eigenschaften uebersteuern
			if (lieferscheinDtoI != null) {
				lieferscheinDto.setTBelegdatum(lieferscheinDtoI.getTBelegdatum());
				lieferscheinDto.setAnsprechpartnerIId(lieferscheinDtoI.getAnsprechpartnerIId());
				lieferscheinDto.setPersonalIIdVertreter(lieferscheinDtoI.getPersonalIIdVertreter());
				lieferscheinDto.setKundeIIdRechnungsadresse(lieferscheinDtoI.getKundeIIdRechnungsadresse());
				lieferscheinDto.setWaehrungCNr(lieferscheinDtoI.getWaehrungCNr());
				lieferscheinDto.setFWechselkursmandantwaehrungzubelegwaehrung(
						lieferscheinDtoI.getFWechselkursmandantwaehrungzubelegwaehrung());
				lieferscheinDto.setLagerIId(lieferscheinDtoI.getLagerIId());

				// SP6094
				if (lieferscheinDtoI.getZiellagerIId() != null) {
					lieferscheinDto.setZiellagerIId(lieferscheinDtoI.getZiellagerIId());
				}

			}

			// rueckgabedatum berechenen fuer leihtage Lieferschein
			if (auftragBasisDto.getILeihtage().intValue() != 0) {
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.add(Calendar.DATE, auftragBasisDto.getILeihtage().intValue());
				Timestamp rueckgabeterminLieferschein = new Timestamp(calendar.getTimeInMillis());
				lieferscheinDto.setTRueckgabetermin(rueckgabeterminLieferschein);
			}
			lieferscheinDto.setAuftragIId(iIdAuftragI);
			iIdLieferschein = getLieferscheinFac().createLieferschein(lieferscheinDto, theClientDto);

			// alle nicht mengenbehafteten Positionen mituebernehmen
			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);

			if (aAuftragpositionDto != null && aAuftragpositionDto.length > 0) {
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					// Kalkulatorische Artikel sofort Erledigen, damit diese im
					// LS nicht aufscheinen
					if (aAuftragpositionDto[i].getArtikelIId() != null) {
						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(aAuftragpositionDto[i].getArtikelIId(), theClientDto);
						if (Helper.short2boolean(aDto.getBKalkulatorisch())) {

							aAuftragpositionDto[i].setAuftragpositionstatusCNr(LocaleFac.STATUS_ERLEDIGT);
							getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(aAuftragpositionDto[i],
									theClientDto);

						}
					}
				}
			}

			// PJ21885
			Integer artikelIIdVerpackungskosten = getVerpackunskostenArtikel(theClientDto);
			if (artikelIIdVerpackungskosten != null) {
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					if (aAuftragpositionDto[i].getArtikelIId() != null
							&& aAuftragpositionDto[i].getArtikelIId().equals(artikelIIdVerpackungskosten)) {
						aAuftragpositionDto[i].setAuftragpositionstatusCNr(LocaleFac.STATUS_ERLEDIGT);
						getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(aAuftragpositionDto[i],
								theClientDto);

					}
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdLieferschein;
	}

	public AuftragDto[] auftragFindByAuftragstatusCNr(String cNrAuftragstatusI, TheClientDto theClientDto)
			throws EJBExceptionLP {
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
	 * @param iIdAngebotI  PK des Angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @return AuftragDto[] die Auftraege
	 * @throws EJBExceptionLP Ausnahme
	 */
	public AuftragDto[] auftragFindByAngebotIId(Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAngebotI == null"));
		}
		AuftragDto[] aAuftragDto = null;
		Query query = em.createNamedQuery("AuftragfindByAngebotIId");
		query.setParameter(1, iIdAngebotI);
		Collection<?> cl = query.getResultList();
		aAuftragDto = assembleAuftragDtos(cl);
		return aAuftragDto;
	}

	public Map getListeDerErfasstenAuftraege(Integer angebotIId, TheClientDto theClientDto) {
		Map m = new TreeMap();

		AuftragDto[] buchungen = auftragFindByAngebotIId(angebotIId, theClientDto);
		for (int i = 0; i < buchungen.length; i++) {

			AuftragDto aDto = buchungen[i];

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(aDto.getKundeIIdAuftragsadresse(), theClientDto);

			m.put(buchungen[i].getIId(),
					Helper.fitString2Length(aDto.getCNr(), 15, ' ')
							+ Helper.fitString2Length(kdDto.getPartnerDto().formatFixName1Name2(), 50, ' ')
							+ aDto.getCBezProjektbezeichnung());

		}

		return m;
	}

	public AuftragDto[] auftragFindByKundeIIdAuftragsadresseMandantCNr(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundeI == null"));
		}
		AuftragDto[] aAuftragDtos = null;
		Query query = em.createNamedQuery("AuftragfindByKundeIIdAuftragsadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAuftragDtos = assembleAuftragDtos(cl);
		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByKundeIIdAuftragsadresseMandantCNrOhneExc(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) throws RemoteException {
		AuftragDto[] aAuftragDtos = null;
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundeI == null"));
		}
		try {
			Query query = em.createNamedQuery("AuftragfindByKundeIIdAuftragsadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAuftragDtos = assembleAuftragDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI" + cNrMandantI, t);
		}

		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByKundeIIdLieferadresseMandantCNr(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundeI == null"));
		}
		AuftragDto[] aAuftragDtos = null;
		Query query = em.createNamedQuery("AuftragfindByKundeIIdLieferadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAuftragDtos = assembleAuftragDtos(cl);
		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByKundeIIdLieferadresseMandantCNrOhneExc(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) throws RemoteException {

		AuftragDto[] aAuftragDtos = null;

		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundeI == null"));
		}

		try {
			Query query = em.createNamedQuery("AuftragfindByKundeIIdLieferadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAuftragDtos = assembleAuftragDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI" + cNrMandantI, t);
		}

		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByKundeIIdRechnungsadresseMandantCNr(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundeI == null"));
		}
		AuftragDto[] aAuftragDtos = null;
		Query query = em.createNamedQuery("AuftragfindByKundeIIdRechnungsadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAuftragDtos = assembleAuftragDtos(cl);
		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByKundeIIdRechnungsadresseMandantCNrOhneExc(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) throws RemoteException {
		AuftragDto[] aAuftragDtos = null;

		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundeI == null"));
		}

		try {
			Query query = em.createNamedQuery("AuftragfindByKundeIIdRechnungsadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAuftragDtos = assembleAuftragDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI" + cNrMandantI, t);
		}

		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByAnsprechpartnerIIdMandantCNr(Integer iIdAnsprechpartnerI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAnsprechpartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAnsprechpartnerI == null"));
		}
		AuftragDto[] aAuftragDtos = null;
		Query query = em.createNamedQuery("AuftragfindByAnsprechpartnerIIdMandantCNr");
		query.setParameter(1, iIdAnsprechpartnerI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAuftragDtos = assembleAuftragDtos(cl);
		return aAuftragDtos;
	}

	public AuftragDto[] auftragFindByAnsprechpartnerIIdMandantCNrOhneExc(Integer iIdAnsprechpartnerI,
			String cNrMandantI, TheClientDto theClientDto) {
		if (iIdAnsprechpartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAnsprechpartnerI == null"));
		}
		AuftragDto[] aAuftragDtos = null;
		try {
			Query query = em.createNamedQuery("AuftragfindByAnsprechpartnerIIdMandantCNr");
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
	 * @param mandantCNrI    String
	 * @param auftragartCNrI String
	 * @param theClientDto   der aktuelle Benutzer
	 * @return AuftragDto[] die Auftraege
	 * @throws EJBExceptionLP Ausnahme
	 */
	public AuftragDto[] auftragFindByMandantCNrAuftragartCNr(String mandantCNrI, String auftragartCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (mandantCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("mandantCNrI == null"));
		}
		if (auftragartCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("auftragartCNrI == null"));
		}
		AuftragDto[] aAuftragDto = null;
		Query query = em.createNamedQuery("AuftragfindByMandantCNrAuftragartCNr");
		query.setParameter(1, mandantCNrI);
		query.setParameter(2, auftragartCNrI);
		Collection<?> cl = query.getResultList();
		aAuftragDto = assembleAuftragDtos(cl);
		return aAuftragDto;
	}

	/**
	 * Alle Auftraege eines Mandanten und einer Auftrags-Art holen holen.
	 * 
	 * @param mandantCNrI    String
	 * @param auftragartCNrI String
	 * @param theClientDto   der aktuelle Benutzer
	 * @return AuftragDto[] die Auftraege
	 * @throws EJBExceptionLP Ausnahme
	 */
	public AuftragDto[] auftragFindByMandantCNrAuftragartCNrStatusCNr(String mandantCNrI, String auftragartCNrI,
			String statusCNrI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (mandantCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("mandantCNrI == null"));
		}
		if (auftragartCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("auftragartCNrI == null"));
		}
		AuftragDto[] aAuftragDto = null;
		Query query = em.createNamedQuery("AuftragfindByMandantCNrAuftragartCNrStatusCNr");
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
	 * @param iIdRahmenauftragI PK des Rahmenauftrags
	 * @param theClientDto      der aktuelle Benutzer
	 * @return AuftragDto[] die Abrufauftraege
	 * @throws EJBExceptionLP Ausnahme
	 */
	public AuftragDto[] abrufauftragFindByAuftragIIdRahmenauftrag(Integer iIdRahmenauftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdRahmenauftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdRahmenauftragI == null"));
		}
		myLogger.logData(iIdRahmenauftragI);
		AuftragDto[] aAuftragDto = null;
		Query query = em.createNamedQuery("AuftragfindByAuftragIIdRahmenauftrag");
		query.setParameter(1, iIdRahmenauftragI);
		Collection<?> cl = query.getResultList();
		aAuftragDto = assembleAuftragDtos(cl);
		return aAuftragDto;
	}

	@Override
	public AuftragDto[] auftragFindByMandantCnrKundeIIdBestellnummerOhneExc(Integer iIdKundeI, String cNrMandantI,
			String cBestellnummerI) throws RemoteException {
		AuftragDto[] aAuftragDtos = null;
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundeI == null"));
		}
		try {
			Query query = em.createNamedQuery("AuftragfindByMandantCNrKundeIIdCBestellnummer");
			query.setParameter(1, cNrMandantI);
			query.setParameter(2, iIdKundeI);
			query.setParameter(3, cBestellnummerI.replace(" ", ""));
			Collection<?> cl = query.getResultList();
			aAuftragDtos = assembleAuftragDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI" + cNrMandantI + " cBestellnummer" + cBestellnummerI,
					t);
		}

		return aAuftragDtos;
	}

	/**
	 * Den Auftrag mit Daten aktualisieren. Der Status bleibt dabei unveraendert.
	 * 
	 * @param auftragDtoI  der Auftrag
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateAuftragOhneWeitereAktion(AuftragDto auftragDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAuftragDto(auftragDtoI);
		Auftrag auftrag = em.find(Auftrag.class, auftragDtoI.getIId());
		if (auftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setAuftragFromAuftragDto(auftrag, auftragDtoI);
		auftrag.setTAendern(getTimestamp());
		auftrag.setPersonalIIdAendern(theClientDto.getIDPersonal());
	}

	public boolean darfWiederholungsTerminAendern(Integer auftragIId, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("RechnungfindByAuftragIIdNotInStatusCNr");
		query.setParameter(1, auftragIId);
		query.setParameter(2, RechnungFac.STATUS_STORNIERT);
		Collection<?> cl = query.getResultList();
		if (cl.size() != 0)
			return false;
		return true;
	}

	@Override
	public void setzeVersandzeitpunktAufJetzt(Integer auftragIId, String druckart) {
		if (auftragIId != null) {
			Auftrag auftrag = em.find(Auftrag.class, auftragIId);
			auftrag.setTVersandzeitpunkt(new Timestamp(System.currentTimeMillis()));
			auftrag.setCVersandtype(druckart);
			em.merge(auftrag);
			em.flush();
		}

	}

	public java.sql.Date getWiederholungsTermin(Integer auftragIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return getRechnungFac().getWiederholungsTermin(auftragIId, theClientDto);

	}

	@Override
	public BigDecimal berechneBestellwertAuftrag(Integer iIdAuftrag) throws EJBExceptionLP, RemoteException {
		// TODO ghp, 20.4.2022 Warum keine SQL-Query? 
		// select sum(n_wert) from bes_bestellung where auftrag_i_id = iIdAuftrag and status_c_nr <> 'Storniert'
		BigDecimal bestellwert = BigDecimal.ZERO;
		BestellungDto[] besDtos = getBestellungFac().bestellungFindByAuftragIId(iIdAuftrag);

		if (besDtos == null)
			return bestellwert;

		for (BestellungDto bestellungDto : besDtos) {
			if (!bestellungDto.isStorniert() && 
					bestellungDto.getNBestellwert() != null) {
				bestellwert = bestellwert.add(bestellungDto.getNBestellwert());
			}
		}

		return bestellwert;
	}

	@Override
	public String[] berechneSummeSplittbetragAuftrag(Integer iIdAuftrag, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		BigDecimal bdSummeFreieEingansgrechnungen = BigDecimal.ZERO;
		BigDecimal bdSummeFreieEingansgrechnungenMitAuftragswertung = BigDecimal.ZERO;
		EingangsrechnungAuftragszuordnungDto[] erauftragszuordnungDtos = getEingangsrechnungFac()
				.eingangsrechnungAuftragszuordnungFindByAuftragIId(iIdAuftrag);

		BigDecimal bdSummeSchlussEingangsgrechnungen = BigDecimal.ZERO;
		BigDecimal bdSummeSchlussEingangsgrechnungenMitAuftragswertung = BigDecimal.ZERO;

		BigDecimal bdSummeAnzahlungsEingangsgrechnungen = BigDecimal.ZERO;
		BigDecimal bdSummeAnzahlungsEingangsgrechnungenMitAuftragswertung = BigDecimal.ZERO;

		BigDecimal bdSummeAnzahlungseingangsrechnungenNochNichtInSchlussrechnungenEnthalten = BigDecimal.ZERO;
		BigDecimal bdSummeAnzahlungseingangsrechnungenNochNichtInSchlussrechnungenEnthaltenMitAuftragswertung = BigDecimal.ZERO;

		for (int i = 0; i < erauftragszuordnungDtos.length; i++) {

			EingangsrechnungDto erDto = getEingangsrechnungFac()
					.eingangsrechnungFindByPrimaryKey(erauftragszuordnungDtos[i].getEingangsrechnungIId());

			if (erDto.getBestellungIId() != null) {

				EingangsrechnungDto[] erDtos = getEingangsrechnungFac()
						.eingangsrechnungFindByBestellungIId(erDto.getBestellungIId());

				boolean bSchlussZahlungvorhanden = false;

				for (EingangsrechnungDto ers : erDtos) {

					if (!ers.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {
						if (ers.getEingangsrechnungartCNr()
								.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
							bSchlussZahlungvorhanden = true;

						}

					}
				}

				if (erDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {

					bdSummeSchlussEingangsgrechnungen = bdSummeSchlussEingangsgrechnungen
							.add(erauftragszuordnungDtos[i].getNBetrag());
					if (Helper.short2boolean(erauftragszuordnungDtos[i].getBKeineAuftragswertung()) == false) {
						bdSummeSchlussEingangsgrechnungenMitAuftragswertung = bdSummeSchlussEingangsgrechnungenMitAuftragswertung
								.add(erauftragszuordnungDtos[i].getNBetrag());
					}

				}

				if (erDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)) {
					bdSummeAnzahlungsEingangsgrechnungen = bdSummeAnzahlungsEingangsgrechnungen
							.add(erauftragszuordnungDtos[i].getNBetrag());
					if (Helper.short2boolean(erauftragszuordnungDtos[i].getBKeineAuftragswertung()) == false) {
						bdSummeAnzahlungsEingangsgrechnungenMitAuftragswertung = bdSummeAnzahlungsEingangsgrechnungenMitAuftragswertung
								.add(erauftragszuordnungDtos[i].getNBetrag());
					}
				}

				if (bSchlussZahlungvorhanden == false) {
					if (erDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)) {
						bdSummeAnzahlungseingangsrechnungenNochNichtInSchlussrechnungenEnthalten = bdSummeAnzahlungseingangsrechnungenNochNichtInSchlussrechnungenEnthalten
								.add(erDto.getNBetrag().subtract(erDto.getNUstBetrag()));
						if (Helper.short2boolean(erauftragszuordnungDtos[i].getBKeineAuftragswertung()) == false) {
							bdSummeAnzahlungseingangsrechnungenNochNichtInSchlussrechnungenEnthaltenMitAuftragswertung = bdSummeAnzahlungseingangsrechnungenNochNichtInSchlussrechnungenEnthaltenMitAuftragswertung
									.add(erDto.getNBetrag().subtract(erDto.getNUstBetrag()));
						}
					}
				}

			} else {

				bdSummeFreieEingansgrechnungen = bdSummeFreieEingansgrechnungen
						.add(erauftragszuordnungDtos[i].getNBetrag());
				if (Helper.short2boolean(erauftragszuordnungDtos[i].getBKeineAuftragswertung()) == false) {
					bdSummeFreieEingansgrechnungenMitAuftragswertung = bdSummeFreieEingansgrechnungenMitAuftragswertung
							.add(erauftragszuordnungDtos[i].getNBetrag());
				}

			}

		}

		String summen = getTextRespectUISpr("auft.er.summe1", theClientDto.getMandant(), theClientDto.getLocUi()) + ": "
				+ Helper.formatBetrag(bdSummeFreieEingansgrechnungen.add(bdSummeSchlussEingangsgrechnungen),
						theClientDto.getLocUi());

		summen += ", " + getTextRespectUISpr("auft.er.summe2", theClientDto.getMandant(), theClientDto.getLocUi())
				+ ": " + Helper.formatBetrag(bdSummeAnzahlungsEingangsgrechnungen, theClientDto.getLocUi());

		summen += ", " + getTextRespectUISpr("auft.er.summe3", theClientDto.getMandant(), theClientDto.getLocUi())
				+ ": "
				+ Helper.formatBetrag(
						bdSummeFreieEingansgrechnungen.add(bdSummeSchlussEingangsgrechnungen)
								.add(bdSummeAnzahlungseingangsrechnungenNochNichtInSchlussrechnungenEnthalten),
						theClientDto.getLocUi());

		String summen2 = getTextRespectUISpr("auft.er.summe1", theClientDto.getMandant(), theClientDto.getLocUi())
				+ ": " + Helper.formatBetrag(bdSummeFreieEingansgrechnungenMitAuftragswertung
						.add(bdSummeSchlussEingangsgrechnungenMitAuftragswertung), theClientDto.getLocUi());

		summen2 += ", " + getTextRespectUISpr("auft.er.summe2", theClientDto.getMandant(), theClientDto.getLocUi())
				+ ": "
				+ Helper.formatBetrag(bdSummeAnzahlungsEingangsgrechnungenMitAuftragswertung, theClientDto.getLocUi());

		summen2 += ", " + getTextRespectUISpr("auft.er.summe3", theClientDto.getMandant(), theClientDto.getLocUi())
				+ ": "
				+ Helper.formatBetrag(bdSummeFreieEingansgrechnungenMitAuftragswertung
						.add(bdSummeSchlussEingangsgrechnungenMitAuftragswertung)
						.add(bdSummeAnzahlungseingangsrechnungenNochNichtInSchlussrechnungenEnthaltenMitAuftragswertung),
						theClientDto.getLocUi());

		summen2 += " (" + getTextRespectUISpr("auft.er.summe.nurauftragsewertung", theClientDto.getMandant(),
				theClientDto.getLocUi()) + ")";

		return new String[] { summen, summen2 };
	}

	@Override
	public IOrderResponse createOrderResponse(AuftragDto auftragDto, TheClientDto theClientDto)
			throws NamingException, RemoteException {
		Validator.notNull(auftragDto, "auftragDto");
		Validator.notEmpty(auftragDto.getMandantCNr(), "auftragDto.mandant");
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notEmpty(theClientDto.getMandant(), "theClient.mandant");
		if (!auftragDto.getMandantCNr().equals(theClientDto.getMandant())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FALSCHER_MANDANT, auftragDto.getIId().toString());
		}

		if (!AuftragServiceFac.AUFTRAGSTATUS_OFFEN.equals(auftragDto.getStatusCNr())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(auftragDto.getStatusCNr()));
		}

		IOrderResponseProducer responseProducer = responseFactory.getProducer(auftragDto, theClientDto);
		if (null == responseProducer) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT, "");
		}

		IOrderResponse orderResponse = responseProducer.createResponse(auftragDto, theClientDto);
		return orderResponse;
	}

	private void updateResponseTimestamp(Integer auftragId, TheClientDto theClientDto) {
		Auftrag auftrag = em.find(Auftrag.class, auftragId);
		auftrag.setTResponse(new Timestamp(System.currentTimeMillis()));
		auftrag.setPersonalIIdResponse(theClientDto.getIDPersonal());
		em.merge(auftrag);
		em.flush();
	}

	@Override
	public String createOrderResponsePost(AuftragDto auftragDto, TheClientDto theClientDto)
			throws RemoteException, NamingException, EJBExceptionLP {
		IOrderResponse response = createOrderResponse(auftragDto, theClientDto);
		if (response == null)
			return null;

		IOrderResponseProducer producer = responseFactory.getProducer(auftragDto, theClientDto);
		producer.postResponse(response, auftragDto, theClientDto);

		// Besser Zeitstempel ist gesetzt, und dafuer ev. nicht archiviert
		updateResponseTimestamp(auftragDto.getIId(), theClientDto);

		return producer.archiveResponse(response, auftragDto, theClientDto);
	}

	@Override
	public String createOrderResponseToString(AuftragDto auftragDto, TheClientDto theClientDto)
			throws RemoteException, NamingException {
		IOrderResponse response = createOrderResponse(auftragDto, theClientDto);
		return response == null ? null : orderresponseToStringImpl(response, auftragDto, theClientDto);
	}

	public LieferstatusDto lieferstatusFindByPrimaryKey(Integer iId) {
		Lieferstatus lieferstatus = em.find(Lieferstatus.class, iId);
		LieferstatusDto lsDto = new LieferstatusDto();

		lsDto.setIId(lieferstatus.getIId());
		lsDto.setAuftragpositionIId(lieferstatus.getAuftragpositionIId());
		lsDto.setLieferscheinpositionIId(lieferstatus.getLieferscheinpositionIId());
		lsDto.setRechnungpositionIId(lieferstatus.getRechnungpositionIId());

		return lsDto;

	}

	private String orderresponseToStringImpl(IOrderResponse response, AuftragDto auftragDto, TheClientDto theClientDto)
			throws RemoteException, NamingException {
		IOrderResponseProducer producer = responseFactory.getProducer(auftragDto, theClientDto);
		return producer.toString(response);
	}

	// PJ19372
	public void aendereRechnungsadresseProjeknummerBestellnummer(AuftragDto auftragDto, TheClientDto theClientDto) {

		// Alle direkten Rechnungen am Auftrag
		try {

			HashMap<Integer, RechnungDto> rechnungen = new HashMap<Integer, RechnungDto>();

			RechnungDto[] reDtos = getRechnungFac().rechnungFindByAuftragIId(auftragDto.getIId());
			for (RechnungDto reDto : reDtos) {
				rechnungen.put(reDto.getIId(), reDto);
			}

			LieferscheinDto[] lsDtos = getLieferscheinFac().lieferscheinFindByAuftrag(auftragDto.getIId(),
					theClientDto);

			for (LieferscheinDto lsDto : lsDtos) {

				RechnungDto[] reDtosLS = getRechnungFac().rechnungFindByLieferscheinIId(lsDto.getIId());
				for (RechnungDto reDto : reDtosLS) {
					rechnungen.put(reDto.getIId(), reDto);
				}

				Query query = em.createNamedQuery("RechnungPositionfindByLieferscheinIId");
				query.setParameter(1, lsDto.getIId());
				Collection c = query.getResultList();
				Iterator it = c.iterator();
				while (it.hasNext()) {

					Rechnungposition rechnungposition = (Rechnungposition) it.next();
					rechnungen.put(rechnungposition.getRechnungIId(),
							getRechnungFac().rechnungFindByPrimaryKey(rechnungposition.getRechnungIId()));

				}

				// RE-Adresse im LS aendern

				Lieferschein ls = em.find(Lieferschein.class, lsDto.getIId());
				ls.setKundeIIdRechnungsadresse(auftragDto.getKundeIIdRechnungsadresse());
				ls.setAnsprechpartnerIIdRechnungsadresse(auftragDto.getAnsprechpartnerIIdRechnungsadresse());
				ls.setCBestellnummer(auftragDto.getCBestellnummer());
				ls.setCBez(auftragDto.getCBezProjektbezeichnung());
				ls.setPersonalIIdVertreter(auftragDto.getPersonalIIdVertreter());

			}

			for (Integer rechnungId : rechnungen.keySet()) {
				RechnungDto reDto = rechnungen.get(rechnungId);
				Rechnung re = em.find(Rechnung.class, reDto.getIId());
				if (reDto.getStatusCNr().equals(RechnungFac.STATUS_ANGELEGT)
						|| re.getKundeIId().equals(auftragDto.getKundeIIdRechnungsadresse())) {

					re.setKundeIId(auftragDto.getKundeIIdRechnungsadresse());
					re.setAnsprechpartnerIId(auftragDto.getAnsprechpartnerIIdRechnungsadresse());
					re.setCBestellnummer(auftragDto.getCBestellnummer());
					re.setCBez(auftragDto.getCBezProjektbezeichnung());
					re.setPersonalIIdVertreter(auftragDto.getPersonalIIdVertreter());

				} else {
					// Exception
					ArrayList al = new ArrayList();
					al.add(reDto.getCNr());
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_READRESSE_PROJEKT_BESTELLNUMMER_AENDERN_RECHNUNG_NICHT_IM_STATUS_ANGELEGT,
							al, new Exception(
									"FEHLER_READRESSE_PROJEKT_BESTELLNUMMER_AENDERN_RECHNUNG_NICHT_IM_STATUS_ANGELEGT"));

				}

			}

			// Und den Auftrag selbst
			Auftrag ab = em.find(Auftrag.class, auftragDto.getIId());
			ab.setKundeIIdRechnungsadresse(auftragDto.getKundeIIdRechnungsadresse());
			ab.setAnsprechpartnerIIdRechnungsadresse(auftragDto.getAnsprechpartnerIIdRechnungsadresse());
			ab.setCBestellnummer(auftragDto.getCBestellnummer());
			ab.setCBez(auftragDto.getCBezProjektbezeichnung());
			ab.setProjektIId(auftragDto.getProjektIId());
			ab.setPersonalIIdVertreter(auftragDto.getPersonalIIdVertreter());
			ab.setVerrechnungsmodellIId(auftragDto.getVerrechnungsmodellIId());
			ab.setTLaufterminBis(auftragDto.getTLaufterminBis());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	private void archiveResponseDocument(AuftragDto auftragDto, String xmlContent, TheClientDto theClientDto) {
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocPath dp = new DocPath(new DocNodeAuftrag(auftragDto))
				.add(new DocNodeFile("Auftragbest\u00e4tigung_osa_Clevercure.xml"));
		jcrDocDto.setDocPath(dp);
		jcrDocDto.setbData(xmlContent.getBytes());
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(partnerDto.getIId());

		Integer kundeId = auftragDto.getKundeIIdLieferadresse();
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

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
		getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, theClientDto);
	}

	private Integer getVersandwegIdFor(AuftragDto auftragDto, String versandwegCnr, TheClientDto theClientDto)
			throws RemoteException {
		return getVersandwegIdFor(auftragDto.getKundeIIdAuftragsadresse(), versandwegCnr, theClientDto);
	}

	private Integer getVersandwegIdFor(Integer kundeId, String versandwegCnr, TheClientDto theClientDto)
			throws RemoteException {
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);
		VersandwegPartner versandwegPartner = VersandwegPartnerQuery.findByPartnerIIdVersandwegCnr(em,
				kundeDto.getPartnerIId(), versandwegCnr, theClientDto.getMandant());

		return versandwegPartner == null ? null : versandwegPartner.getVersandwegId();
	}

	private class AuftragpositionenNurIdentFilter implements Predicate, Serializable {
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

	// private Integer getVersandwegIdFor(AuftragDto auftragDto,
	// TheClientDto theClientDto) throws RemoteException {
	// return getVersandwegIdFor(auftragDto.getKundeIIdLieferadresse(),
	// theClientDto);
	// }
	//
	// private Integer getVersandwegIdFor(Integer kundeId,
	// TheClientDto theClientDto) throws RemoteException {
	// KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId,
	// theClientDto);
	//
	// Integer versandwegId = kundeDto.getPartnerDto().getVersandwegIId();
	// return versandwegId;
	// }

	private OrderresponseFactory responseFactory = new OrderresponseFactory();

	private class OrderresponseFactory {
		private IOrderResponseProducer getProducerCC() {
			// return new OrderResponseProducerCCSelfSender() ;
			return new OrderResponseProducerCC();
		}

		private IOrderResponseProducer getProducerEdiOrdRsp() {
			return new OrderResponseProducerEdiOrdRsp();
		}

		public IOrderResponseProducer getProducer(AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException, NamingException {
			Integer versandwegId = getVersandwegIdFor(auftragDto, VersandwegType.CleverCureVerkauf, theClientDto);
			if (versandwegId != null) {
				return getProducerCC();
			}

			versandwegId = getVersandwegIdFor(auftragDto, VersandwegType.EdiOrderResponse, theClientDto);
			if (versandwegId != null) {
				return getProducerEdiOrdRsp();
			}

			return null;
//			return new OrderResponseProducerDummy();
			//
			// Integer versandwegId = getVersandwegIdFor(auftragDto,
			// theClientDto);
			// if (null == versandwegId)
			// return new OrderResponseProducerDummy();
			//
			// Versandweg versandweg = em.find(Versandweg.class, versandwegId);
			// if (null == versandweg)
			// throw new EJBExceptionLP(
			// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
			// versandwegId.toString());
			//
			// if (SystemFac.VersandwegType.CleverCureVerkauf.equals(versandweg
			// .getCnr().trim()))
			// return getProducerCC();
			// return null;
		}
	}

	public class OrderResponseProducerCCSelfSender extends OrderResponseProducerCC {
		@Override
		protected String getCCEndpunkt(EntityManager em, VersandwegPartnerCCDto ccPartnerDto) {
			return "http://localhost:8280/restapi/services/rest/api/beta/cc?fake=yes";
		}
	}

	public class OrderResponseProducerDummy implements IOrderResponseProducer {
		@Override
		public boolean isDummy() {
			return true;
		}

		@Override
		public IOrderResponse createResponse(AuftragDto auftragDto, TheClientDto theClientDto) throws RemoteException {
			return null;
		}

		@Override
		public void postResponse(IOrderResponse response, AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
		}

		@Override
		public String archiveResponse(IOrderResponse orderResponse, AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			return null;
		}

		@Override
		public String toString(IOrderResponse orderResponse) {
			return null;
		}
	}

	public class OrderResponseProducerCC extends CleverCureProducer implements IOrderResponseProducer {
		public final static String GENERATOR_INFO = "2.4";

		@Override
		public boolean isDummy() {
			return false;
		}

		public IOrderResponse createResponse(AuftragDto auftragDto, TheClientDto theClientDto)
				throws NamingException, RemoteException {
			OrderResponseCC orderResponse = new OrderResponseCC();
			XMLXMLORDERRESPONSE or = createOr(orderResponse, auftragDto, theClientDto);
			orderResponse.setResponse(or);
			return orderResponse;
		}

		@Override
		public void postResponse(IOrderResponse response, AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			if (!(response instanceof OrderResponseCC))
				return;

			try {
				HttpURLConnection urlConnection = buildUrlconnectionPost(response, theClientDto);
				urlConnection.setRequestProperty("Content-Type", "text/xml");
				OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
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

				myLogger.info("postResponse: " + lastResponseCode + " content-type: " + lastContentType + " for: "
						+ theContent);
			} catch (IOException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HTTP_POST_IO, e);
			} catch (KeyManagementException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_MANAGMENT, e);
			} catch (UnrecoverableKeyException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_RECOVER, e);
			} catch (NoSuchAlgorithmException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_ALGORITHMEN, e);
			} catch (CertificateException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_CERTIFICATE, e);
			} catch (KeyStoreException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE, e);
			}
		}

		@Override
		public String archiveResponse(IOrderResponse response, AuftragDto auftragDto, TheClientDto theClientDto)
				throws NamingException, RemoteException {
			String xmlContent = orderresponseToStringImpl(response, auftragDto, theClientDto);
			archiveResponseDocument(auftragDto, xmlContent, theClientDto);
			return xmlContent;
		}

		private HttpURLConnection buildUrlconnectionPost(IOrderResponse response, TheClientDto theClientDto)
				throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException,
				KeyManagementException, UnrecoverableKeyException {
			// IVersandwegPartnerDto versandwegPartnerDto = getSystemFac()
			// .versandwegPartnerFindByPrimaryKey(
			// response.getVersandwegId(), response.getPartnerId());
			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac().versandwegPartnerFindByVersandwegIdPartnerId(
					response.getVersandwegId(), response.getPartnerId(), theClientDto.getMandant());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						response.getPartnerId().toString());
			}
			// VersandwegCCPartnerDto ccPartnerDto = (VersandwegCCPartnerDto)
			// versandwegPartnerDto;
			VersandwegPartnerCCDto ccPartnerDto = (VersandwegPartnerCCDto) versandwegPartnerDto;

			String uri = getCCEndpunkt(em, ccPartnerDto);
			// String uri =
			// "http://localhost:8280/restapi/services/rest/api/beta/cc/?dummy=value"
			// ;
			uri += "&datatype=osd";
			uri += "&companycode=" + ccPartnerDto.getCKundennummer().trim();
			uri += "&password=" + ccPartnerDto.getCKennwort().trim();

			URL requestedUrl = new URL(uri);
			HttpURLConnection urlConnection = (HttpURLConnection) requestedUrl.openConnection();
			if (urlConnection instanceof HttpsURLConnection) {
				KeyStore keystore = getKeystore(ccPartnerDto);
				SSLContext sslContext = getSslContext(ccPartnerDto, keystore);

				((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());
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
			return orderResponse instanceof OrderResponseCC ? fromXml((OrderResponseCC) orderResponse) : null;
		}

		private String fromXml(OrderResponseCC orderresponse) {
			StringWriter writer = new StringWriter();
			try {
				JAXBContext context = JAXBContext
						.newInstance(orderresponse.getResponse().getClass().getPackage().getName());
				Marshaller m = context.createMarshaller();
				m.marshal(orderresponse.getResponse(), writer);
				String s = writer.toString();
				return s;
			} catch (JAXBException e) {
				System.out.println("JAXBException" + e.getMessage());
			}

			return null;
		}

		private XMLXMLORDERRESPONSE createOr(IOrderResponse response, AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			XMLXMLORDERRESPONSE or = new XMLXMLORDERRESPONSE();
			XMLXMLORDERRESPONSEHEADER orHeader = new XMLXMLORDERRESPONSEHEADER();
			XMLXMLCONTROLINFO controlInfo = new XMLXMLCONTROLINFO();
			controlInfo.setGENERATORINFO(GENERATOR_INFO);
			controlInfo.setGENERATIONDATE(formatAsIso8601Timestamp(GregorianCalendar.getInstance().getTime()));
			orHeader.setCONTROLINFO(controlInfo);

			Collection<AuftragpositionDto> positionsDto = getAuftragPositionsDto(auftragDto, theClientDto);

			XMLXMLORDERRESPONSEINFO orInfo = buildOrInfo(response, auftragDto, theClientDto);
			orHeader.setORDERRESPONSEINFO(orInfo);
			or.setORDERRESPONSEHEADER(orHeader);

			XMLXMLORDERRESPONSEITEMLIST orItemList = buildOrItemList(response, auftragDto, positionsDto, theClientDto);
			or.setORDERRESPONSEITEMLIST(orItemList);

			XMLXMLORDERRESPONSESUMMARY orSummary = new XMLXMLORDERRESPONSESUMMARY();
			orSummary.setTOTALITEMNUM(new BigInteger(String.valueOf(orItemList.getORDERRESPONSEITEM().size())));
			orSummary.setTOTALAMOUNT(ccScaled3(((OrderResponseCC) response).getTotalPriceLineAmount()));
			or.setORDERRESPONSESUMMARY(orSummary);

			return or;
		}

		private XMLXMLORDERRESPONSEINFO buildOrInfo(IOrderResponse response, AuftragDto auftragDto,
				TheClientDto theClientDto) throws RemoteException {
			XMLXMLORDERRESPONSEINFO orInfo = new XMLXMLORDERRESPONSEINFO();
			orInfo.setORDERID(auftragDto.getCBestellnummer());
			orInfo.setSUPPLIERORDERID(auftragDto.getCNr());

			Timestamp t = auftragDto.getTGedruckt();
			if (t == null)
				t = new Timestamp(System.currentTimeMillis());
			orInfo.setORDERRESPONSEDATE(formatAsIso8601Timestamp(new Date(t.getTime())));

			if (!HelperWebshop.isEmptyString(auftragDto.getCBezProjektbezeichnung())) {
				XMLXMLREMARK dnRemark = new XMLXMLREMARK();
				dnRemark.setValue(auftragDto.getCBezProjektbezeichnung());
				orInfo.getREMARK().add(dnRemark);
			}

			orInfo.setPRICECURRENCY(getDtCurrency(auftragDto, theClientDto));

			XMLXMLORDERPARTIES xmlParties = buildOrInfoParties(response, orInfo, auftragDto, theClientDto);
			orInfo.setORDERPARTIES(xmlParties);

			return orInfo;
		}

		private XMLXMLORDERPARTIES buildOrInfoParties(IOrderResponse orderResponse, XMLXMLORDERRESPONSEINFO orInfo,
				AuftragDto auftragDto, TheClientDto theClientDto) throws RemoteException {
			XMLXMLORDERPARTIES xmlParties = new XMLXMLORDERPARTIES();
			XMLXMLBUYERPARTY buyerParty = new XMLXMLBUYERPARTY();
			XMLXMLPARTY xmlParty = new XMLXMLPARTY();
			XMLXMLPARTYID xmlPartyId = new XMLXMLPARTYID();
			xmlPartyId.setValue(getLieferantennummer(auftragDto.getKundeIIdAuftragsadresse(), theClientDto));
			xmlParty.setPARTYID(xmlPartyId);
			buyerParty.setPARTY(xmlParty);
			xmlParties.setBUYERPARTY(buyerParty);

			XMLXMLSUPPLIERPARTY supplierParty = new XMLXMLSUPPLIERPARTY();
			XMLXMLPARTY xmlSupplierParty = new XMLXMLPARTY();
			XMLXMLPARTYID xmlSupplierPartyId = new XMLXMLPARTYID();
			xmlSupplierPartyId.setValue(getSupplierPartyId(orderResponse, auftragDto, theClientDto));
			xmlSupplierParty.setPARTYID(xmlSupplierPartyId);
			supplierParty.setPARTY(xmlSupplierParty);
			xmlParties.setSUPPLIERPARTY(supplierParty);

			XMLXMLINVOICEPARTY invoiceParty = new XMLXMLINVOICEPARTY();
			XMLXMLPARTY xmlInvoiceParty = new XMLXMLPARTY();
			XMLXMLPARTYID xmlInvoicePartyId = new XMLXMLPARTYID();
			xmlInvoicePartyId.setValue(getLieferantennummer(auftragDto.getKundeIIdRechnungsadresse(), theClientDto));
			xmlInvoiceParty.setPARTYID(xmlInvoicePartyId);
			invoiceParty.setPARTY(xmlInvoiceParty);
			xmlParties.setINVOICEPARTY(invoiceParty);

			XMLXMLSHIPMENTPARTIES xmlShipmentParties = new XMLXMLSHIPMENTPARTIES();
			XMLXMLDELIVERYPARTY xmlDeliveryParty = new XMLXMLDELIVERYPARTY();
			xmlParty = new XMLXMLPARTY();
			XMLXMLPARTYID xmlDeliveryPartyId = new XMLXMLPARTYID();
			xmlDeliveryPartyId.setValue(getLieferantennummer(auftragDto.getKundeIIdLieferadresse(), theClientDto));
			xmlParty.setPARTYID(xmlDeliveryPartyId);

			KundeDto lieferKundeDto = getKundeFac().kundeFindByPrimaryKeyOhneExc(auftragDto.getKundeIIdLieferadresse(),
					theClientDto);
			if (lieferKundeDto != null) {
				PartnerDto lieferPartnerDto = getPartnerFac()
						.partnerFindByPrimaryKeyOhneExc(lieferKundeDto.getPartnerIId(), theClientDto);
				XMLXMLADDRESS xmlAddress = new XMLXMLADDRESS();
				xmlAddress.setNAME(lieferPartnerDto.getCName1nachnamefirmazeile1());
				xmlAddress.setNAME2(lieferPartnerDto.getCName2vornamefirmazeile2());
				xmlAddress.setNAME3(lieferPartnerDto.getCName3vorname2abteilung());
				xmlAddress.setSTREET(lieferPartnerDto.getCStrasse());
				xmlAddress.setZIP(lieferPartnerDto.getLandplzortDto().getCPlz());
				xmlAddress.setCITY(lieferPartnerDto.getLandplzortDto().getOrtDto().getCName());
				xmlAddress.setCCCOUNTRY(lieferPartnerDto.getLandplzortDto().getLandDto().getCName());
				xmlAddress.setCCCOUNTRYISOCODE(lieferPartnerDto.getLandplzortDto().getLandDto().getCLkz());
				xmlParty.setADDRESS(xmlAddress);
			}

			xmlDeliveryParty.setPARTY(xmlParty);

			xmlShipmentParties.setDELIVERYPARTY(xmlDeliveryParty);
			xmlParties.setSHIPMENTPARTIES(xmlShipmentParties);

			return xmlParties;
		}

		private String getLieferantennummer(Integer kundeIId, TheClientDto theClientDto) {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
			return kundeDto.getCLieferantennr();
		}

		private DtCURRENCIES getDtCurrency(AuftragDto auftragDto, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {
			KundeDto kundeDto = null;
			String currency = auftragDto.getWaehrungCNr();
			if (currency == null) {
				kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
				currency = kundeDto.getCWaehrung();
			}
			if (currency == null) {
				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(kundeDto.getMandantCNr(), theClientDto);
				currency = mandantDto.getWaehrungCNr();
			}

			return DtCURRENCIES.fromValue(currency);
		}

		private String getSupplierPartyId(IOrderResponse response, AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			Integer versandwegId = getVersandwegIdFor(auftragDto, SystemFac.VersandwegType.CleverCureVerkauf,
					theClientDto);
			if (versandwegId == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT, "");
			}

			Integer kundeId = auftragDto.getKundeIIdAuftragsadresse();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

			response.setVersandwegId(versandwegId);
			response.setPartnerId(kundeDto.getPartnerIId());

			// IVersandwegPartnerDto versandwegPartnerDto = getSystemFac()
			// .versandwegPartnerFindByPrimaryKey(versandwegId,
			// response.getPartnerId());
			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac().versandwegPartnerFindByVersandwegIdPartnerId(
					versandwegId, response.getPartnerId(), theClientDto.getMandant());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_PARTNER_KUNDENNUMMER_FEHLT,
						kundeDto.getPartnerIId().toString());
			}
			VersandwegPartnerCCDto partnerDto = (VersandwegPartnerCCDto) versandwegPartnerDto;
			String supplierId = partnerDto.getCKundennummer();
			return supplierId.trim();
		}

		private String buildItemCnr(ArtikelDto itemDto) {
			String cnr = itemDto.getCNr();
			return cnr;
		}

		private XMLXMLORDERRESPONSEITEMLIST buildOrItemList(IOrderResponse response, AuftragDto auftragDto,
				Collection<AuftragpositionDto> ejbPositions, TheClientDto theClientDto) throws RemoteException {
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

				ArtikelDto itemDto = getArtikelFac().artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId(),
						theClientDto);
				orItem.setCCORIGINCOUNTRY(buildUrsprungsland(itemDto));

				XMLXMLARTICLEID itemId = new XMLXMLARTICLEID();
				itemId.setSUPPLIERAID(buildItemCnr(itemDto));
				if (itemDto.getArtikelsprDto() != null) {
					itemId.setDESCRIPTIONSHORT(
							HelperWebshop.isEmptyString(itemDto.getArtikelsprDto().getCKbez()) ? itemDto.getCNr()
									: itemDto.getArtikelsprDto().getCKbez());
				} else {
					itemId.setDESCRIPTIONSHORT(itemDto.getCNr());
				}
				KundesokoDto[] sokos = getKundesokoFac()
						.kundesokoFindByKundeIIdArtikelIId(auftragDto.getKundeIIdLieferadresse(), itemDto.getIId());
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
				BigDecimal einzelpreis = auftragpositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();
				xmlPrice.setPRICEAMOUNT(ccScaled4(einzelpreis));
				BigDecimal lineamount = ccScaled3(einzelpreis.multiply(auftragpositionDto.getNMenge()));
				ccResponse.addTotalPriceLineAmount(lineamount);
				xmlPrice.setPRICELINEAMOUNT(lineamount);
				xmlPrice.setPRICEQUANTITY(BigDecimal.ONE);
				xmlPrice.setCCPRICECONVERSIONRATIO(BigDecimal.ONE);
				xmlPrice.setCCORDERUNIT(auftragpositionDto.getEinheitCNr().trim());
				orItem.setARTICLEPRICE(xmlPrice);

				XMLXMLDELIVERYDATE xmlDeliveryDate = new XMLXMLDELIVERYDATE();
				String deliveryDate = formatAsIso8601Timestamp(
						new Date(auftragpositionDto.getTUebersteuerbarerLiefertermin().getTime()));
				xmlDeliveryDate.setDELIVERYSTARTDATE(deliveryDate);
				xmlDeliveryDate.setDELIVERYENDDATE(deliveryDate);
				orItem.setDELIVERYDATE(xmlDeliveryDate);

				XMLXMLCCCUSTOMSINFO xmlCustomsInfo = new XMLXMLCCCUSTOMSINFO();
				xmlCustomsInfo.setCCORDERUNIT(auftragpositionDto.getEinheitCNr().trim());
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

		/*
		 * public class AuftragpositionenNurIdentFilter implements Predicate,
		 * Serializable { private static final long serialVersionUID =
		 * -5617572280308975044L;
		 * 
		 * @Override public boolean evaluate(Object arg0) { if (arg0 instanceof
		 * AuftragpositionDto) { AuftragpositionDto pos = (AuftragpositionDto) arg0;
		 * return pos.isIdent(); }
		 * 
		 * return false; } }
		 */
		private Collection<AuftragpositionDto> getAuftragPositionsDto(AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			Collection<AuftragpositionDto> ejbPositions = getAuftragpositionFac()
					.auftragpositionFindByAuftragList(auftragDto.getIId());
			CollectionUtils.filter(ejbPositions, new AuftragpositionenNurIdentFilter());
			return ejbPositions;
		}
	}

	public class OrderResponseProducerEdiOrdRsp implements IOrderResponseProducer {
		private DateFormat unbDateFormat = new SimpleDateFormat("yyMMdd");
		private DateFormat unbTimeFormat = new SimpleDateFormat("HHmm");
		private DateFormat controlFormat = new SimpleDateFormat("yyMMddHHmmss");
		private DateFormat refDateFormat = new SimpleDateFormat("yyyyMMdd");
		private EdifactOrderResponseProducer producer = new EdifactOrderResponseProducer();
		private String ediContentCached = null;

		@Override
		public boolean isDummy() {
			return false;
		}

		@Override
		public IOrderResponse createResponse(AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException, NamingException {
			OrderResponseEdiOrdRsp response = new OrderResponseEdiOrdRsp();
			createOr(response, auftragDto, theClientDto);
			createPositions(response, auftragDto, theClientDto);
			return response;
		}

		@Override
		public void postResponse(IOrderResponse orderResponse, AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			if (!(orderResponse instanceof OrderResponseEdiOrdRsp))
				return;
			OrderResponseEdiOrdRsp response = (OrderResponseEdiOrdRsp) orderResponse;

			if (isDummy()) {
				response.setPostStatusCode(200);
				response.setPostResponseContent("<html>no content</html>");
				response.setPostResponseType("text/html");
				return;
			}

			try {
				HttpURLConnection urlConnection = buildUrlconnectionPost(response, theClientDto);
				urlConnection.setRequestProperty("Content-Type", "text/plain");
				OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
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

				response.setPostStatusCode(lastResponseCode);
				response.setPostResponseContent(theContent);
				response.setPostResponseType(lastContentType);

				myLogger.info("postResponse: " + lastResponseCode + " content-type: " + lastContentType + " for: "
						+ theContent);
			} catch (IOException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HTTP_POST_IO, e);
			} catch (KeyManagementException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_MANAGMENT, e);
			} catch (UnrecoverableKeyException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_RECOVER, e);
			} catch (NoSuchAlgorithmException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_ALGORITHMEN, e);
			} catch (CertificateException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE_CERTIFICATE, e);
			} catch (KeyStoreException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEYSTORE, e);
			}
		}

		private HttpURLConnection buildUrlconnectionPost(IOrderResponse response, TheClientDto theClientDto)
				throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException,
				KeyManagementException, UnrecoverableKeyException {
			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac().versandwegPartnerFindByVersandwegIdPartnerId(
					response.getVersandwegId(), response.getPartnerId(), theClientDto.getMandant());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						response.getPartnerId().toString());
			}

			VersandwegPartnerOrdRspDto ediPartnerDto = (VersandwegPartnerOrdRspDto) versandwegPartnerDto;

			String uri = ediPartnerDto.getCEndpunkt();
			if (Helper.isStringEmpty(uri)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_ENDPUNKT_FEHLT,
						response.getPartnerId().toString());
			}

			String user = ediPartnerDto.getCBenutzer();
			if (Helper.isStringEmpty(user)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_BENUTZER_FEHLT,
						response.getPartnerId().toString());
			}

			String pwd = ediPartnerDto.getCKennwort();
			if (Helper.isStringEmpty(pwd)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_KENNWORT_FEHLT,
						response.getPartnerId().toString());
			}

			URL requestedUrl = new URL(uri);
			HttpURLConnection urlConnection = (HttpURLConnection) requestedUrl.openConnection();

			String auth = user + ":" + pwd;
			byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
			String authHeaderValue = "Basic " + new String(encodedAuth);
			urlConnection.setRequestProperty("Authorization", authHeaderValue);
			/*
			 * Keine spezielle Behandlung von https, da oeffentliches Zertifikat
			 * 
			 * if (urlConnection instanceof HttpsURLConnection) { KeyStore keystore =
			 * getKeystore(ccPartnerDto); SSLContext sslContext =
			 * getSslContext(ccPartnerDto, keystore);
			 * 
			 * ((HttpsURLConnection)
			 * urlConnection).setSSLSocketFactory(sslContext.getSocketFactory()); }
			 */
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("User-Agent", "HELIUM V");
			urlConnection.setConnectTimeout(10000);
			urlConnection.setReadTimeout(10500);
			urlConnection.setDoOutput(true);

			return urlConnection;
		}

		@Override
		public String archiveResponse(IOrderResponse orderResponse, AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			if (!(orderResponse instanceof OrderResponseEdiOrdRsp))
				return null;

			OrderResponseEdiOrdRsp response = (OrderResponseEdiOrdRsp) orderResponse;
			String ediContent = response.asEdiContent();

			Integer kundeId = auftragDto.getKundeIIdAuftragsadresse();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

			JCRDocDto jcrDocDto = prepareJcrDto(new JCRDocDto(), auftragDto, kundeDto, theClientDto);
			DocPath dp = new DocPath(new DocNodeAuftrag(auftragDto))
					.add(new DocNodeFile("Auftragbest\u00e4tigung_ORDRSP.txt"));
			jcrDocDto.setDocPath(dp);
			jcrDocDto.setbData(ediContent.getBytes());

			jcrDocDto.setsMIME(".txt");
			jcrDocDto.setsName("Orderresponse Edifact " + auftragDto.getIId());
			String sSchlagworte = "Export Edifact EDI Orderresponse ordrsp";
			jcrDocDto.setsSchlagworte(sSchlagworte);

			List<JCRDocDto> dtos = new ArrayList<JCRDocDto>();
			dtos.add(jcrDocDto);

			if (response.getPostResponseContent() != null) {
				JCRDocDto jcrPostDto = prepareJcrDto(new JCRDocDto(), auftragDto, kundeDto, theClientDto);
				DocPath dpPost = new DocPath(new DocNodeAuftrag(auftragDto))
						.add(new DocNodeFile("Auftragbest\u00e4tigung_ORDRSP_post.html"));
				jcrPostDto.setDocPath(dpPost);
				jcrPostDto.setbData(response.getPostResponseContent().getBytes());
				jcrPostDto.setsMIME(".html");
				jcrPostDto.setsName("Orderresponse Edifact " + auftragDto.getIId() + " post");
				sSchlagworte = "Export Edifact EDI Orderresponse ordrsp post " + response.getPostStatusCode();
				jcrPostDto.setsSchlagworte(sSchlagworte);
				dtos.add(jcrPostDto);
			}

			getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(dtos, theClientDto);
			return ediContent;
		}

		private JCRDocDto prepareJcrDto(JCRDocDto dto, BelegDto belegDto, KundeDto kundeDto,
				TheClientDto theClientDto) {
			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);

			dto.setlAnleger(partnerDto.getIId());
			dto.setbVersteckt(false);
			dto.setlPartner(kundeDto.getPartnerIId());
			dto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
			dto.setlZeitpunkt(System.currentTimeMillis());
			dto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
			dto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
			dto.setsBelegnummer(belegDto.getCNr().replace("/", "."));
			dto.setsFilename(belegDto.getCNr().replace("/", "."));
			dto.setsRow(belegDto.getIId().toString());
			dto.setsTable("AUFTRAG");
			return dto;
		}

		@Override
		public String toString(IOrderResponse orderResponse) {
			if (!(orderResponse instanceof OrderResponseEdiOrdRsp))
				return "";

			return ((OrderResponseEdiOrdRsp) orderResponse).asEdiContent();
		}

		private String getEdiContent(AuftragDto auftragDto, TheClientDto theClientDto)
				throws IOException, RepositoryException {
			if (ediContentCached == null) {
				ediContentCached = loadEdiFileBestellungDto(auftragDto, theClientDto);
			}

			return ediContentCached;
		}

		private OrderResponseEdiOrdRsp createOr(OrderResponseEdiOrdRsp response, AuftragDto auftragDto,
				TheClientDto theClientDto) throws RemoteException {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
					theClientDto);
			Date ref = Helper.asDate(getTimestamp());
			createUnbUnh(response, kundeDto, ref, theClientDto);
			createBgm(response, auftragDto);
			createBelegdatum(response, auftragDto);
			createBestellreferenz(response, auftragDto);
			createLieferantInfo(response, auftragDto, kundeDto, theClientDto);
			createVertreterInfo(response, auftragDto, theClientDto);
			createBuyerPartyInfo(response, auftragDto, kundeDto, theClientDto);
			createDeliveryPartyInfo(response, auftragDto, theClientDto);
			createInvoicePartyInfo(response, auftragDto, theClientDto);
			createWaehrungInfo(response, auftragDto, kundeDto, theClientDto);
			createLieferart(response, auftragDto, theClientDto);
			return response;
		}

		private OrderResponseEdiOrdRsp createPositions(OrderResponseEdiOrdRsp response, AuftragDto auftragDto,
				TheClientDto theClientDto) throws RemoteException {
			Collection<AuftragpositionDto> dtos = getAuftragPositionsDto(auftragDto, theClientDto);
			Collection<Collection<AuftragpositionDto>> groups = splitPositions(dtos);

			PositionNumberAdapter positionAdapter = new AuftragPositionNumberAdapter(em);
			for (Collection<AuftragpositionDto> group : groups) {
				if (group.isEmpty())
					continue;

				AuftragpositionDto posDto = group.iterator().next();
				createLinInfo(response, auftragDto, posDto, positionAdapter, theClientDto);
				if (group.size() > 1) {
					BigDecimal total = BigDecimal.ZERO;
					for (AuftragpositionDto dto : group) {
						total = total.add(dto.getNMenge());
					}

					createOrderQty(response, posDto, total, theClientDto);
					createPriceInfo(response, auftragDto, posDto, theClientDto);
					for (AuftragpositionDto dto : group) {
						createSccQty(response, dto, theClientDto);
					}
				} else {
					createOrderQty(response, posDto, theClientDto);
					createPriceInfo(response, auftragDto, posDto, theClientDto);
				}
			}

			return response;
		}

		private OrderResponseEdiOrdRsp createLinInfo(OrderResponseEdiOrdRsp response, AuftragDto auftragDto,
				AuftragpositionDto posDto, PositionNumberAdapter positionAdapter, TheClientDto theClientDto)
				throws RemoteException {
			Integer posNr = getAuftragpositionFac().getPositionNummer(posDto.getIId(), positionAdapter);
			StringBuffer sb = new StringBuffer(posNr.toString()).append('+').append("4").append('+')
					.append(producer.getOrderItemInfo(posDto));
			response.addSegment("LIN", sb);

			sb = new StringBuffer("1").append('+').append(producer.getOrderItemInfo(posDto));
			response.addSegment("PIA", sb);

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(posDto.getArtikelIId(), theClientDto);
			sb = new StringBuffer("1").append('+').append(artikelDto.getCNr().trim()).append(':').append("VP");
			response.addSegment("PIA", sb);

			sb = new StringBuffer("F").append('+').append('+').append(':').append(':').append(':')
					.append(artikelDto.getCBezAusSpr());
			response.addSegment("IMD", sb);
			return response;
		}

		private OrderResponseEdiOrdRsp createOrderQty(OrderResponseEdiOrdRsp response, AuftragpositionDto posDto,
				TheClientDto theClientDto) {
			return createOrderQty(response, posDto, posDto.getNMenge(), theClientDto);
		}

		// QTY+21:20:PCE'
		// DTM+2:20210216:102'
		private OrderResponseEdiOrdRsp createOrderQty(OrderResponseEdiOrdRsp response, AuftragpositionDto posDto,
				BigDecimal quantity, TheClientDto theClientDto) {
			StringBuffer sb = new StringBuffer("21").append(':').append(asEdiQuantity(quantity)).append(':')
					.append(posDto.getEinheitCNr().trim());
			response.addSegment("QTY", sb);

			Date d = Helper.asDate(posDto.getTUebersteuerbarerLiefertermin());
			sb = new StringBuffer("2").append(':').append(refDateFormat.format(d)).append(':').append("102");
			response.addSegment("DTM", sb);
			return response;
		}

		// PRI+AAA:567.45000:CT::1:PCE'
		// RFF+ON::00010'
		private OrderResponseEdiOrdRsp createPriceInfo(OrderResponseEdiOrdRsp response, AuftragDto auftragDto,
				AuftragpositionDto posDto, TheClientDto theClientDto) {
			BigDecimal price = posDto.getNNettoeinzelpreisplusversteckteraufschlag();
			StringBuffer sb = new StringBuffer("AAA").append(':').append(price.toPlainString()).append(':').append("CT")
					.append(':').append(':').append("1").append(':').append(posDto.getEinheitCNr().trim());
			response.addSegment("PRI", sb);

			HvOptional<String> bestellPos = producer.getLineItemId(posDto);
			if (bestellPos.isPresent()) {
				sb = new StringBuffer("ON").append(':').append(':').append(bestellPos.get());
				response.addSegment("RFF", sb);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_BESTELLPOSREFERENZ_FEHLT,
						auftragDto.getIId().toString(), auftragDto.getCNr());
			}
			return response;
		}

		private String asEdiQuantity(BigDecimal quantity) {
			if (quantity.remainder(BigDecimal.ONE).signum() == 0) {
				return quantity.setScale(0).toPlainString();
			} else {
				return quantity.toPlainString();
			}
		}

		// SCC+1'
		// QTY+21:10:PCE'
		// DTM+2:20210223:102'
		private OrderResponseEdiOrdRsp createSccQty(OrderResponseEdiOrdRsp response, AuftragpositionDto posDto,
				TheClientDto theClientDto) {
			response.addSegment("SCC", "1");

			StringBuffer sb = new StringBuffer("21").append(':').append(asEdiQuantity(posDto.getNMenge())).append(':')
					.append(posDto.getEinheitCNr().trim());
			response.addSegment("QTY", sb);

			Date d = Helper.asDate(posDto.getTUebersteuerbarerLiefertermin());
			sb = new StringBuffer("2").append(':').append(refDateFormat.format(d)).append(':').append("102");
			response.addSegment("DTM", sb);
			return response;
		}

		private Collection<Collection<AuftragpositionDto>> splitPositions(Collection<AuftragpositionDto> dtos) {
			Collection<Collection<AuftragpositionDto>> list = new ArrayList<Collection<AuftragpositionDto>>();
			Collection<AuftragpositionDto> subList = new ArrayList<AuftragpositionDto>();

			String lastLineItemId = "";

			for (AuftragpositionDto entry : dtos) {
				HvOptional<String> lineItem = producer.getLineItemId(entry);
				if (!lineItem.isPresent()) {
					lastLineItemId = "";
					continue;
				}

				if (!lineItem.get().equals(lastLineItemId)) {
					if (subList.size() > 0) {
						list.add(subList);
						subList = new ArrayList<AuftragpositionDto>();
					}
					lastLineItemId = lineItem.get();
				}

				subList.add(entry);
			}

			if (subList.size() > 0) {
				list.add(subList);
			}

			return list;
		}

		// UNB+UNOC:3+<Lieferantennr>+<unbReceiver>+JJMMTT:HHMM+JJMMTTHHMMSS++ORDRSP
		// UNH+JJMMTTHHMMSS+ORDRSP:D:96A:UN
		private void createUnbUnh(OrderResponseEdiOrdRsp response, KundeDto kundeDto, Date ref,
				TheClientDto theClientDto) {
			if (Helper.isStringEmpty(kundeDto.getCLieferantennr())) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_LIEFERANTENNUMMER_FEHLT,
						kundeDto.getIId().toString());
			}

			IVersandwegPartnerDto dto = getSystemFac().versandwegPartnerFindByVersandwegCnrPartnerId(
					SystemFac.VersandwegType.EdiOrderResponse, kundeDto.getPartnerIId(), theClientDto.getMandant());
			if (dto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						kundeDto.getPartnerIId().toString());
			}

			VersandwegPartnerOrdRspDto ordRspDto = (VersandwegPartnerOrdRspDto) dto;
			response.setPartnerId(kundeDto.getPartnerIId());
			response.setVersandwegId(ordRspDto.getVersandwegId());

			StringBuffer sb = new StringBuffer("UNOC:3+").append(kundeDto.getCLieferantennr()).append('+')
					.append(ordRspDto.getCUnbEmpfaenger()).append('+').append(unbDateFormat.format(ref)).append(':')
					.append(unbTimeFormat.format(ref)).append('+').append(controlFormat.format(ref)).append('+')
					.append('+').append("ORDRSP");
			response.addSegment("UNB", sb.toString());

			sb = new StringBuffer(controlFormat.format(ref)).append('+').append("ORDRSP:D:96A:UN");
			response.addSegment("UNH", sb);
		}

		// BGM+231+<AuftragCnr>+(9|5)
		private void createBgm(OrderResponseEdiOrdRsp response, AuftragDto auftragDto) {
			StringBuffer sb = new StringBuffer("231").append('+').append(auftragDto.getCNr()).append('+')
					.append(auftragDto.getTResponse() == null ? '9' : '5');
			response.addSegment("BGM", sb);
		}

		private void createBelegdatum(OrderResponseEdiOrdRsp response, AuftragDto auftragDto) {
			StringBuffer sb = new StringBuffer("137").append(':')
					.append(refDateFormat.format(Helper.asDate(auftragDto.getTBelegdatum()))).append(":102");
			response.addSegment("DTM", sb);
		}

		private void createBestellreferenz(OrderResponseEdiOrdRsp response, AuftragDto auftragDto) {
			StringBuffer sb = new StringBuffer("ON").append(':').append(auftragDto.getCBestellnummer());
			response.addSegment("RFF", sb.toString());

			sb = new StringBuffer("171").append(':')
					.append(refDateFormat.format(Helper.asDate(auftragDto.getDBestelldatum()))).append(":102");
			response.addSegment("DTM", sb);
		}

		private void createLieferantInfo(OrderResponseEdiOrdRsp response, AuftragDto auftragDto, KundeDto kundeDto,
				TheClientDto theClientDto) throws RemoteException {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			PartnerDto partnerDto = mandantDto.getPartnerDto();
			createNadInfo(response, "SU", kundeDto.getCLieferantennr(), partnerDto);
		}

		private void createBuyerPartyInfo(OrderResponseEdiOrdRsp response, AuftragDto auftragDto, KundeDto kundeDto,
				TheClientDto theClientDto) {
			createNadPartyInfo(response, "BY", KennungType.OrdersNadBuyer, auftragDto, kundeDto, theClientDto);
		}

		private void createDeliveryPartyInfo(OrderResponseEdiOrdRsp response, AuftragDto auftragDto,
				TheClientDto theClientDto) {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdLieferadresse(),
					theClientDto);
			createNadPartyInfo(response, "DP", KennungType.OrdersNadDelivery, auftragDto, kundeDto, theClientDto);
		}

		private void createInvoicePartyInfo(OrderResponseEdiOrdRsp response, AuftragDto auftragDto,
				TheClientDto theClientDto) {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdRechnungsadresse(),
					theClientDto);
			createNadPartyInfo(response, "IV", KennungType.OrdersNadInvoice, auftragDto, kundeDto, theClientDto);
		}

		private void createNadPartyInfo(OrderResponseEdiOrdRsp response, String ediKennung, KennungType kennung,
				AuftragDto auftragDto, KundeDto kundeDto, TheClientDto theClientDto) {
			KundeId kundeId = new KundeId(kundeDto.getIId());

			HvOptional<String> ediId = HvOptional.empty();
			try {
				String ediContent = getEdiContent(auftragDto, theClientDto);
				ediId = ediImportImplBean.get().extractNADPartyIdentification(ediContent, ediKennung);
			} catch (IOException | RepositoryException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_KUNDENKENNUNG_BESTELLUNG_FEHLT,
						kundeId.id().toString(), kundeId, ediKennung);
			}

			List<KundeKennung> kks = KundeKennungQuery.findByKundeIdKennung(em, kundeId, kennung);
			KundeKennung kk = null;
			if (ediId.isPresent()) {
				kk = ediImportImplBean.get().filterOursWithEdiId(kks, ediId.get());
			} else {
				if (kks.size() == 1) {
					kk = kks.get(0);
				}
			}

			if (kk == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_KUNDENKENNUNG_FEHLT, kundeId.id().toString(),
						kundeId, kennung.getText());
			}

			createNadInfo(response, ediKennung, ediId.orElse(kk.getCWert()), kundeDto.getPartnerDto());
		}

		private void createNadInfo(OrderResponseEdiOrdRsp response, String ediKennung, String ediReference,
				PartnerDto partnerDto) {
			StringBuffer sb = new StringBuffer(ediKennung).append('+').append(ediReference).append('+').append('+')
					.append(partnerDto.getCName1nachnamefirmazeile1()).append('+').append(partnerDto.getCStrasse())
					.append('+').append(partnerDto.getLandplzortDto().getOrtDto().getCName()).append('+').append('+')
					.append(partnerDto.getLandplzortDto().getCPlz()).append('+')
					.append(partnerDto.getLandplzortDto().getLandDto().getCLkz());
			response.addSegment("NAD", sb);
		}

		private void createVertreterInfo(OrderResponseEdiOrdRsp response, AuftragDto auftragDto,
				TheClientDto theClientDto) throws RemoteException {
			Integer vertreterPersonalId = auftragDto.getPersonalIIdVertreter();
			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(vertreterPersonalId, theClientDto);

			String s = getPartnerFac().formatFixAnredeTitelName2Name1(personalDto.getPartnerDto(),
					theClientDto.getLocMandant(), theClientDto);
			StringBuffer sb = new StringBuffer("OC").append('+').append(':').append(s);
			response.addSegment("CTA", sb);
		}

		// CUX+2:EUR:9
		private void createWaehrungInfo(OrderResponseEdiOrdRsp response, AuftragDto auftragDto, KundeDto kundeDto,
				TheClientDto theClientDto) {
			String currency = auftragDto.getWaehrungCNr();
			if (Helper.isStringEmpty(currency)) {
				currency = kundeDto.getCWaehrung();
			}
			if (Helper.isStringEmpty(currency)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_WAEHRUNG_FEHLT, auftragDto.getIId().toString(),
						kundeDto.getIId().toString());
			}
			StringBuffer sb = new StringBuffer("2").append(':').append(currency).append(':').append("9");
			response.addSegment("CUX", sb);
		}

		private void createLieferart(OrderResponseEdiOrdRsp response, AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			LieferartDto lieferartDto = getLocaleFac().lieferartFindByPrimaryKey(auftragDto.getLieferartIId(),
					theClientDto);
			StringBuffer sb = new StringBuffer("6").append('+').append('+').append(lieferartDto.getCNr());
			response.addSegment("TOD", sb);

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			PartnerDto partnerDto = mandantDto.getPartnerDto();

			String loc = partnerDto.getLandplzortDto().getCPlz() + " " + lieferartDto.getCVersandort();
			sb = new StringBuffer("1").append('+').append(loc);
			response.addSegment("LOC", sb);
		}

		private Collection<AuftragpositionDto> getAuftragPositionsDto(AuftragDto auftragDto, TheClientDto theClientDto)
				throws RemoteException {
			Collection<AuftragpositionDto> ejbPositions = getAuftragpositionFac()
					.auftragpositionFindByAuftragList(auftragDto.getIId());
			CollectionUtils.filter(ejbPositions, new AuftragpositionenNurIdentFilter());
			return ejbPositions;
		}
	}

	public boolean hatAuftragVersandweg(AuftragDto auftragDto, TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(auftragDto, "auftragDto");
		return getKundeFac().hatKundeVersandweg(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
	}

	public boolean hatAuftragVersandweg(Integer auftragIId, TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(auftragIId, "auftragIId");
		AuftragDto auftragDto = auftragFindByPrimaryKey(auftragIId);
		return getKundeFac().hatKundeVersandweg(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
	}

	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	// ruft der Client auf(!)
	public BelegPruefungDto aktiviereBelegControlled(Integer iid, Timestamp t, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.aktiviereBelegControlled(this, iid, t, theClientDto);
	}

	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	// ruft der Client auf(! vorsichtshalber)
	public BelegPruefungDto berechneBelegControlled(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.berechneBelegControlled(this, iid, theClientDto);
	}

	@Override
	public List<Timestamp> getAenderungsZeitpunkte(Integer iid) throws EJBExceptionLP, RemoteException {
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
	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	public BelegPruefungDto berechneAktiviereBelegControlled(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.berechneAktiviereControlled(this, iid, theClientDto);
		// new BelegAktivierungController(this).berechneAktiviereControlled(iid,
		// theClientDto);
	}

	@Override
	public BelegDto getBelegDto(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BelegDto belegDto = auftragFindByPrimaryKey(iid);
		belegDto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
		return belegDto;
	}

	@Override
	public BelegpositionDto[] getBelegPositionDtos(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return getAuftragpositionFac().auftragpositionFindByAuftrag(iid);
	}

	@Override
	public Integer getKundeIdDesBelegs(BelegDto belegDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return ((AuftragDto) belegDto).getKundeIIdRechnungsadresse();
	}

	@Override
	public void pruefeAktivierbarRecht(Integer belegId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		boolean rechtAktivieren = getTheJudgeFac().hatRecht(RechteFac.RECHT_AUFT_AKTIVIEREN, theClientDto);
		boolean rechtCUD = getTheJudgeFac().hatRecht(RechteFac.RECHT_AUFT_AUFTRAG_CUD, theClientDto);
		if (!(rechtAktivieren || rechtCUD)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN,
					"FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN");
		}
		// PJ 15233
		// if (!getTheJudgeFac().hatRecht(
		// RechteFac.RECHT_AUFT_AKTIVIEREN, theClientDto)) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN,
		// "FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN");
		// }
	}

	// SP5524
	@Override
	public void repairAuftragZws5524(Integer auftragId, TheClientDto theClientDto) throws RemoteException {
		AuftragDto auftragDto = auftragFindByPrimaryKey(auftragId);
		if (RechnungFac.STATUS_STORNIERT.equals(auftragDto.getStatusCNr()))
			return;

		AuftragpositionDto[] abPosDto = getAuftragpositionFac().auftragpositionFindByAuftrag(auftragDto.getIId());
		getBelegVerkaufFac().prepareIntZwsPositions(abPosDto, auftragDto);
		Set<Integer> modifiedPositions = getBelegVerkaufFac().adaptIntZwsPositions(abPosDto);
		getBelegVerkaufFac().saveIntZwsPositions(abPosDto, modifiedPositions, Auftragposition.class);
	}

	@Override
	public List<Integer> repairAuftragZws5524GetList(TheClientDto theClientDto) {
		try {
			Session session = FLRSessionFactory.getFactory().openSession();
			String sQuery = "SELECT DISTINCT flrauftrag.i_id "
					+ " FROM FLRAuftragposition AS abpos WHERE abpos.positionart_c_nr = 'IZwischensumme'";

			org.hibernate.Query auftragIdsQuery = session.createQuery(sQuery);

			List<Integer> resultList = new ArrayList<Integer>();
			List<Integer> queryList = (List<Integer>) auftragIdsQuery.list();
			for (Integer auftrag_i_id : queryList) {
				Auftrag auftrag = em.find(Auftrag.class, auftrag_i_id);
				if (theClientDto.getMandant().equals(auftrag.getMandantCNr())) {
					resultList.add(auftrag_i_id);
				}
			}

			session.close();
			return resultList;
		} catch (Exception e) {
			return new ArrayList<Integer>();
		}
	}

	@Override
	public AuftragDto erzeugeAenderungsauftrag(Integer auftragIId, TheClientDto theClientDto) {
		Validator.notNull(auftragIId, "auftragIId");

		synchronized (auftragIId) {
			Auftrag auftrag = em.find(Auftrag.class, auftragIId);
			Versionizer versionizer = new Versionizer(em);
			versionizer.incrementVersion(auftrag);

			AuftragDto auftragDto = auftragFindByPrimaryKey(auftragIId);
			auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
			updateAuftrag(auftragDto, null, theClientDto);

			return auftragDto;
		}
	}

	@Override
	public String formatLieferadresseAuftrag(Integer auftragId, TheClientDto theClientDto) throws RemoteException {
		AuftragDto auftragDto = auftragFindByPrimaryKey(auftragId);
		Integer kundeId = auftragDto.getKundeIIdLieferadresse();
		Integer ansprechpartnerId = auftragDto.getAnsprechpartnerIIdLieferadresse();
		return formatLieferadresse(kundeId, ansprechpartnerId, theClientDto);
	}

	@Override
	public String formatLieferadresse(Integer kundeId, Integer ansprechpartnerId, TheClientDto theClientDto)
			throws RemoteException {
		AnsprechpartnerDto anspDto = null;
		if (ansprechpartnerId != null) {
			anspDto = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(ansprechpartnerId, theClientDto);
		}
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

		return formatAdresseFuerAusdruck(kundeDto.getPartnerDto(), anspDto, mandantDto, theClientDto.getLocUi(), false,
				LocaleFac.BELEGART_AUFTRAG, false);
	}

	@Override
	public DocPath getDocPathEdiImportFile(AuftragDto auftragDto) {
		DocPath dp = new DocPath(new DocNodeAuftrag(auftragDto)).add(new DocNodeFile("Import_Edifact.txt"));
		return dp;
	}

	@Override
	public String loadEdiFileBestellung(Integer auftragId, TheClientDto theClientDto)
			throws IOException, RepositoryException {
		AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragId);
		return loadEdiFileBestellungDto(auftragDto, theClientDto);
	}

	@Override
	public String loadEdiFileBestellungDto(AuftragDto auftragDto, TheClientDto theClientDto)
			throws IOException, RepositoryException {
		DocPath dp = getDocPathEdiImportFile(auftragDto);
		JCRDocDto jcrDto = getJCRDocFac().getJCRDocDtoFromNode(dp);
		if (jcrDto != null) {
			jcrDto = getJCRDocFac().getData(jcrDto);
		}
		return (jcrDto == null) ? null : new String(jcrDto.getbData());
	}
}
