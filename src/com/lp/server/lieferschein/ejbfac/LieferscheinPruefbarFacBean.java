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
package com.lp.server.lieferschein.ejbfac;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
import javax.xml.datatype.DatatypeConfigurationException;

// import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.WarenzugangsreferenzDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.forecast.ejb.Fclieferadresse;
import com.lp.server.forecast.ejb.Forecast;
import com.lp.server.forecast.ejb.Forecastauftrag;
import com.lp.server.forecast.ejb.Forecastposition;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.server.lieferschein.bl.EasydataStockMovementImporter;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.ejb.LieferscheinQuery;
import com.lp.server.lieferschein.ejb.Lieferscheinart;
import com.lp.server.lieferschein.ejb.Lieferscheinartspr;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.ejb.LieferscheinpositionQuery;
import com.lp.server.lieferschein.ejb.Lieferscheinpositionart;
import com.lp.server.lieferschein.ejb.Lieferscheinstatus;
import com.lp.server.lieferschein.ejb.Lieferscheintext;
import com.lp.server.lieferschein.ejb.Packstueck;
import com.lp.server.lieferschein.ejb.Verkettet;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.BestaetigterLieferscheinDto;
import com.lp.server.lieferschein.service.EasydataImportResult;
import com.lp.server.lieferschein.service.IEasydataBeanServices;
import com.lp.server.lieferschein.service.ILieferscheinAviso;
import com.lp.server.lieferschein.service.ILieferscheinAvisoProducer;
import com.lp.server.lieferschein.service.LieferavisoEdiDesAdv;
import com.lp.server.lieferschein.service.LieferscheinAvisoCC;
import com.lp.server.lieferschein.service.LieferscheinAvisoEdi4All;
import com.lp.server.lieferschein.service.LieferscheinAvisoLinienabruf;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
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
import com.lp.server.lieferschein.service.PaketVersandAntwortDto;
import com.lp.server.lieferschein.service.VerkettetDto;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.KundeKennung;
import com.lp.server.partner.ejb.KundeKennungQuery;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesachbearbeiterDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungFac;
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
import com.lp.server.system.ejb.Land;
import com.lp.server.system.ejb.VersandwegPartner;
import com.lp.server.system.ejb.VersandwegPartnerEdi4All;
import com.lp.server.system.ejb.VersandwegPartnerQuery;
import com.lp.server.system.ejbfac.BelegAktivierungFac;
import com.lp.server.system.ejbfac.CleverCureProducer;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.EdifactOrderResponseProducer;
import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferschein;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.FunktionDto;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.IVersandwegPartnerDto;
import com.lp.server.system.service.KennungType;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemFac.VersandwegType;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.system.service.VersandwegPartnerCCDto;
import com.lp.server.system.service.VersandwegPartnerDesAdvDto;
import com.lp.server.system.service.VersandwegPartnerEdi4AllDto;
import com.lp.server.system.service.VersandwegPartnerLinienabrufDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperWebshop;
import com.lp.server.util.HvOptional;
import com.lp.server.util.IISort;
import com.lp.server.util.KundeId;
import com.lp.server.util.LieferscheinId;
import com.lp.server.util.MwstsatzCache;
import com.lp.server.util.TauscheISort;
import com.lp.server.util.Validator;
import com.lp.server.util.bean.EdifactOrdersImportImplBean;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.service.Artikelset;
import com.lp.service.BelegDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.service.edifact.BelegnummerTransformerAbstract;
import com.lp.service.edifact.BelegnummerTransformerFactory;
import com.lp.service.edifact.BelegnummerTyp;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class LieferscheinPruefbarFacBean extends Facade implements IAktivierbar {
	@PersistenceContext
	private EntityManager em;

	@EJB
	private BelegAktivierungFac belegAktivierungFac;

	// Lieferschein
	// --------------------------------------------------------------

	/**
	 * Anlegen eines neuen Lieferscheinkopfs in der DB. <br>
	 * Ein Lieferschein kann zu mehreren Auftraegen liefern. <br>
	 * Der Bezug zu einem Auftrag wird in einer Kreuztabelle eingetragen.
	 * 
	 * @param lieferscheinDtoI die Daten des Lieferscheins
	 * @param theClientDto     der aktuelle User
	 * @throws EJBExceptionLP
	 * @return Integer PK des neuen Lieferscheins
	 */
	public Integer createLieferschein(LieferscheinDto lieferscheinDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkLieferscheinDto(lieferscheinDtoI);
		Integer lieferscheinIId = null;
		String lieferscheinCNr = null;

		try {

			gehtFreierLieferscheinOhneAuftragsbezugAnAnderenMandanten(lieferscheinDtoI, theClientDto);

			// Generieren von PK + Belegnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(lieferscheinDtoI.getMandantCNr());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(lieferscheinDtoI.getMandantCNr(),
					lieferscheinDtoI.getTBelegdatum());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(iGeschaeftsjahr, PKConst.PK_LIEFERSCHEIN,
					lieferscheinDtoI.getMandantCNr(), theClientDto);

			lieferscheinIId = bnr.getPrimaryKey();
			lieferscheinCNr = f.formatMitStellenZufall(bnr);

			lieferscheinDtoI.setTBelegdatum(Helper.cutTimestamp(lieferscheinDtoI.getTBelegdatum()));

			lieferscheinDtoI.setIId(lieferscheinIId);
			lieferscheinDtoI.setCNr(lieferscheinCNr);
			lieferscheinDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			lieferscheinDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			Lieferschein lieferschein = new Lieferschein(lieferscheinDtoI.getIId(), lieferscheinDtoI.getCNr(),
					lieferscheinDtoI.getLieferscheinartCNr(), lieferscheinDtoI.getTBelegdatum(),
					lieferscheinDtoI.getKundeIIdLieferadresse(), lieferscheinDtoI.getPersonalIIdVertreter(),
					lieferscheinDtoI.getKundeIIdRechnungsadresse(), lieferscheinDtoI.getCBezProjektbezeichnung(),
					lieferscheinDtoI.getCBestellnummer(), lieferscheinDtoI.getKostenstelleIId(),
					lieferscheinDtoI.getTLiefertermin(), lieferscheinDtoI.getTRueckgabetermin(),
					lieferscheinDtoI.getLieferartIId(), lieferscheinDtoI.getPersonalIIdAnlegen(),
					lieferscheinDtoI.getPersonalIIdAendern(), lieferscheinDtoI.getMandantCNr(),
					lieferscheinDtoI.getStatusCNr(), lieferscheinDtoI.getLagerIId(), LocaleFac.BELEGART_LIEFERSCHEIN,
					lieferscheinDtoI.getWaehrungCNr(), lieferscheinDtoI.getFWechselkursmandantwaehrungzubelegwaehrung(),
					lieferscheinDtoI.getZahlungszielIId(), lieferscheinDtoI.getSpediteurIId(),
					lieferscheinDtoI.getLieferscheintextIIdDefaultKopftext(),
					lieferscheinDtoI.getLieferscheintextIIdDefaultFusstext());
			em.persist(lieferschein);
			em.flush();

			lieferscheinDtoI.setTAnlegen(lieferschein.getTAnlegen());
			lieferscheinDtoI.setTAendern(lieferschein.getTAendern());
			setLieferscheinFromLieferscheinDto(lieferschein, lieferscheinDtoI);

			// SP9315
			if (!lieferscheinDtoI.getLieferscheinartCNr().equals(LieferscheinFac.LSART_LIEFERANT)
					&& lieferscheinDtoI.getKundeIIdRechnungsadresse() != null) {
				// PJ14938
				getKundeFac().checkAndCreateAutoDebitorennummerZuKunden(
						new KundeId(lieferscheinDtoI.getKundeIIdRechnungsadresse()), theClientDto);
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		HvDtoLogger<LieferscheinDto> lsLogger = new HvDtoLogger<LieferscheinDto>(em, lieferscheinDtoI.getIId(),
				theClientDto);
		lsLogger.logInsert(lieferscheinDtoI);

		return lieferscheinIId;
	}

	private void gehtFreierLieferscheinOhneAuftragsbezugAnAnderenMandanten(LieferscheinDto lieferscheinDtoI,
			TheClientDto theClientDto) {
		// SP7745
		if (lieferscheinDtoI.getAuftragIId() == null && getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
			Integer partnerIId_Lieferadresse = getKundeFac()
					.kundeFindByPrimaryKey(lieferscheinDtoI.getKundeIIdLieferadresse(), theClientDto).getPartnerIId();

			MandantDto[] dtos = getMandantFac().mandantFindAll(theClientDto);
			for (int i = 0; i < dtos.length; i++) {
				MandantDto dto = dtos[i];
				if (!dto.getCNr().equals(theClientDto.getMandant())) {
					if (dtos[i].getPartnerIId().equals(partnerIId_Lieferadresse)
							|| (dtos[i].getPartnerIIdLieferadresse() != null
									&& dtos[i].getPartnerIIdLieferadresse().equals(partnerIId_Lieferadresse))) {

						boolean b = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, dto.getCNr());

						if (b == true) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FREIER_LIEFERSCHEIN_OHNE_AUFTRAGSBEZUG_AN_ANDEREN_MANDANTEN,
									"EJBExceptionLP.FEHLER_FREIER_LIEFERSCHEIN_OHNE_AUFTRAGSBEZUG_AN_ANDEREN_MANDANTEN");
						}
					}
				}
			}

		}

	}

	/**
	 * Einen bestehenden Lieferschein aktualisieren. <br>
	 * Ein eventuelle Bezug zu Auftraegen bleibt dabei unberuecksichtig.
	 * 
	 * @param lieferscheinDtoI der aktuelle Lieferschein
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateLieferschein(LieferscheinDto lieferscheinDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinDto(lieferscheinDtoI);
		lieferscheinDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
		lieferscheinDtoI.setTAendern(getTimestamp());
		Lieferschein lieferschein = em.find(Lieferschein.class, lieferscheinDtoI.getIId());
		if (lieferschein == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLieferscheinFromLieferscheinDto(lieferschein, lieferscheinDtoI);
		pruefeUndSetzeLieferscheinstatusBeiAenderung(lieferscheinDtoI.getIId());
		myLogger.exit("Der Lieferschein wurde aktualisiert, Status: " + lieferschein.getLieferscheinstatusCNr());
	}

	public void updateTAendern(Integer lieferscheinIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

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
	 * @param lieferscheinDtoI die Daten des Lieferscheins
	 * @param aAuftragIIdI     der Bezug zu 0..n Auftraegen
	 * @param waehrungOriCNrI  die urspruengliche Belegwaehrung
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public boolean updateLieferschein(LieferscheinDto lieferscheinDtoI, Integer[] aAuftragIIdI, String waehrungOriCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinDto(lieferscheinDtoI);

		boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = false;

		try {

			gehtFreierLieferscheinOhneAuftragsbezugAnAnderenMandanten(lieferscheinDtoI, theClientDto);

			// wenn die Waehrung geaendert wurde, muessen die Belegwerte neu
			// berechnet werden
			if (waehrungOriCNrI != null && !waehrungOriCNrI.equals(lieferscheinDtoI.getWaehrungCNr())) {
				LieferscheinpositionDto[] aLieferscheinpositionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(lieferscheinDtoI.getIId());

				// die Positionswerte neu berechnen und abspeichern
				BigDecimal ffWechselkurs = getLocaleFac().getWechselkurs2(waehrungOriCNrI,
						lieferscheinDtoI.getWaehrungCNr(), theClientDto);

				for (int i = 0; i < aLieferscheinpositionDto.length; i++) {
					if (aLieferscheinpositionDto[i].getNMenge() != null
							&& aLieferscheinpositionDto[i].getNEinzelpreis() != null) {
						BigDecimal nNettoeinzelpreisInNeuerWaehrung = aLieferscheinpositionDto[i].getNEinzelpreis()
								.multiply(ffWechselkurs);

						VerkaufspreisDto verkaufspreisDto = getVkPreisfindungFac().berechnePreisfelder(
								nNettoeinzelpreisInNeuerWaehrung, aLieferscheinpositionDto[i].getFRabattsatz(),
								aLieferscheinpositionDto[i].getFZusatzrabattsatz(),
								aLieferscheinpositionDto[i].getMwstsatzIId(), 4, // @todo
								// Konstante
								// PJ
								// 4390
								theClientDto);

						aLieferscheinpositionDto[i].setNEinzelpreis(verkaufspreisDto.einzelpreis);
						aLieferscheinpositionDto[i].setNRabattbetrag(verkaufspreisDto.rabattsumme);
						aLieferscheinpositionDto[i].setNNettoeinzelpreis(verkaufspreisDto.nettopreis);
						aLieferscheinpositionDto[i].setNMwstbetrag(verkaufspreisDto.mwstsumme);
						aLieferscheinpositionDto[i].setNBruttoeinzelpreis(verkaufspreisDto.bruttopreis);

						// alle Preisfelder incl. der zusaetzlichen Preisfelder
						// befuellen
						getLieferscheinpositionFac()
								.updateLieferscheinpositionOhneWeitereAktion(aLieferscheinpositionDto[i], theClientDto);
					}
				}
			}

			lieferscheinDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			lieferscheinDtoI.setTAendern(getTimestamp());

			Lieferschein lieferschein = em.find(Lieferschein.class, lieferscheinDtoI.getIId());
			if (lieferschein == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			LieferscheinDto lsDto_vorher = lieferscheinFindByPrimaryKey(lieferscheinDtoI.getIId());
			HvDtoLogger<LieferscheinDto> erLogger = new HvDtoLogger<LieferscheinDto>(em, lieferscheinDtoI.getIId(),
					theClientDto);
			erLogger.log(lsDto_vorher, lieferscheinDtoI);

			// Wird der kunde geaendert muss man die Konditionen neu holen

			// CK: 2013-06-04 Gilt nicht mehr, da die Konditionen nun
			// vorher am Client bestaetigt werden muessen
			/*
			 * if (!lieferscheinDtoI.getKundeIIdLieferadresse().equals(
			 * lieferschein.getKundeIIdLieferadresse())) { KundeDto kundeDto =
			 * getKundeFac().kundeFindByPrimaryKey(
			 * lieferscheinDtoI.getKundeIIdLieferadresse(), theClientDto); Double
			 * dAllgemeinerrabattsatz = new Double(0); if (kundeDto.getFRabattsatz() !=
			 * null) { dAllgemeinerrabattsatz = kundeDto.getFRabattsatz(); }
			 * lieferscheinDtoI .setFAllgemeinerRabattsatz(dAllgemeinerrabattsatz); if
			 * (kundeDto.getLieferartIId() != null) { lieferscheinDtoI
			 * .setLieferartIId(kundeDto.getLieferartIId()); } if
			 * (kundeDto.getZahlungszielIId() != null) {
			 * lieferscheinDtoI.setZahlungszielIId(kundeDto .getZahlungszielIId()); } if
			 * (kundeDto.getSpediteurIId() != null) { lieferscheinDtoI
			 * .setSpediteurIId(kundeDto.getSpediteurIId()); } }
			 */
			if (!lieferscheinDtoI.getKundeIIdRechnungsadresse().equals(lieferschein.getKundeIIdRechnungsadresse())) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(lieferscheinDtoI.getKundeIIdRechnungsadresse(),
						theClientDto);
				KundeDto kundeDtoVorher = getKundeFac()
						.kundeFindByPrimaryKey(lieferschein.getKundeIIdRechnungsadresse(), theClientDto);

				ParametermandantDto parameterPositionskontierung = getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);

				boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung.getCWertAsObject();

				LieferscheinpositionDto[] aLieferscheinpositionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(lieferscheinDtoI.getIId());
				MwstsatzCache mwstsatzCache = new MwstsatzCache(lieferscheinDtoI, theClientDto);
				for (int i = 0; i < aLieferscheinpositionDto.length; i++) {
					if (aLieferscheinpositionDto[i].isIdent() || aLieferscheinpositionDto[i].isHandeingabe()) {

						MwstsatzDto mwstsatzDto = mwstsatzCache.getValueOfKey(kundeDto.getMwstsatzbezIId());
						if (bDefaultMwstsatzAusArtikel && aLieferscheinpositionDto[i].getPositionsartCNr()
								.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
							ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
									aLieferscheinpositionDto[i].getArtikelIId(), theClientDto);
							if (artikelDto.getMwstsatzbezIId() != null) {
								/*
								 * mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
								 * artikelDto.getMwstsatzbezIId(), theClientDto);
								 */
								mwstsatzDto = mwstsatzCache.getValueOfKey(artikelDto.getMwstsatzbezIId());
							}

						}

						// SP503
						if (bDefaultMwstsatzAusArtikel && aLieferscheinpositionDto[i].getPositionsartCNr()
								.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {

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

						if (!aLieferscheinpositionDto[i].getMwstsatzIId().equals(mwstsatzDto.getIId())) {
							aLieferscheinpositionDto[i].setMwstsatzIId(mwstsatzDto.getIId());

							BigDecimal mwstBetrag = aLieferscheinpositionDto[i]
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte().multiply(
											new BigDecimal(mwstsatzDto.getFMwstsatz().doubleValue()).movePointLeft(2));
							aLieferscheinpositionDto[i].setNMwstbetrag(mwstBetrag);
							aLieferscheinpositionDto[i].setNBruttoeinzelpreis(mwstBetrag.add(aLieferscheinpositionDto[i]
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
							getLieferscheinpositionFac().updateLieferscheinpositionOhneWeitereAktion(
									aLieferscheinpositionDto[i], theClientDto);
						}
					}
				}

				Double dAllgemeinerrabattsatz = new Double(0);
				if (kundeDto.getFRabattsatz() != null) {
					dAllgemeinerrabattsatz = kundeDto.getFRabattsatz();
				}
				lieferscheinDtoI.setFAllgemeinerRabattsatz(dAllgemeinerrabattsatz);
				if (kundeDto.getLieferartIId() != null) {
					lieferscheinDtoI.setLieferartIId(kundeDto.getLieferartIId());
				}
				if (kundeDto.getZahlungszielIId() != null) {
					lieferscheinDtoI.setZahlungszielIId(kundeDto.getZahlungszielIId());
				}
				if (kundeDto.getSpediteurIId() != null) {
					lieferscheinDtoI.setSpediteurIId(kundeDto.getSpediteurIId());
				}
			}

			if (!lieferschein.getTBelegdatum().equals(lieferscheinDtoI.getTBelegdatum())) {

				// SP8308
				if (!lieferschein.getTBelegdatum().equals(lieferscheinDtoI.getTBelegdatum())) {
					checkMwstsaetzeImZeitraum(lieferschein.getTBelegdatum(), lieferscheinDtoI.getTBelegdatum(),
							lieferscheinDtoI.getMandantCNr());
				}

				Integer iGJAlt = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant(),
						lieferschein.getTBelegdatum());
				Integer iGJNeu = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant(),
						lieferscheinDtoI.getTBelegdatum());
				if (!iGJNeu.equals(iGJAlt)) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN,
							new Exception("Es wurde versucht, den LS " + lieferscheinDtoI.getCNr() + " auf "
									+ lieferscheinDtoI.getTBelegdatum() + " (GJ " + iGJNeu + ") umzudatieren"));
				}

				try {
					getLagerFac().updateTBelegdatumEinesBelegesImLager(LocaleFac.BELEGART_LIEFERSCHEIN,
							lieferscheinDtoI.getIId(), lieferscheinDtoI.getTBelegdatum(), theClientDto);

					if (lieferscheinDtoI.getZiellagerIId() != null) {
						getLagerFac().updateTBelegdatumEinesBelegesImLager(LocaleFac.BELEGART_LSZIELLAGER,
								lieferscheinDtoI.getIId(), lieferscheinDtoI.getTBelegdatum(), theClientDto);
					}
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}
			}

			setLieferscheinFromLieferscheinDto(lieferschein, lieferscheinDtoI);

			// die Zuordnung zu den Auftraegen kann geaendert werden, solange es
			// keine
			// mengenbehafteten Positionen gibt
			if (lieferscheinDtoI.getLieferscheinartCNr().equals(LieferscheinFac.LSART_AUFTRAG)) {
				if (aAuftragIIdI == null || aAuftragIIdI.length == 0) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
							new Exception("Lieferscheinart ist Auftrag, aber aAuftragIIdI ist leer."));
				}

			}

			pruefeUndSetzeLieferscheinstatusBeiAenderung(lieferscheinDtoI.getIId());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben;
	}

	/**
	 * @deprecated use lieferscheinFindByPrimaryKey(Integer) instead
	 */
	public LieferscheinDto lieferscheinFindByPrimaryKey(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LieferscheinDto lieferscheinDto = null;
		// Lieferschein holen
		Lieferschein lieferschein = em.find(Lieferschein.class, iIdLieferscheinI);
		if (lieferschein == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lieferscheinDto = assembleLieferscheinDto(lieferschein);
		return lieferscheinDto;
	}

	/**
	 * Den LieferscheinDto aufgrund seiner IId liefern
	 * 
	 * Die urspr&uuml;ngliche Methode war die mit (Integer, TheClientDto). Das
	 * ClientDto braucht nur niemand, es gibt aber so viele Methoden, die bereits
	 * die vorherige Methode benutzen.
	 * 
	 * @param iIdLieferscheinI is die Lieferschein-IId fuer die der dazugehoerige
	 *                         LieferscheinDto ermittelt werden soll
	 */
	public LieferscheinDto lieferscheinFindByPrimaryKey(Integer iIdLieferscheinI) throws EJBExceptionLP {
		LieferscheinDto lieferscheinDto = null;

		lieferscheinDto = lieferscheinFindByPrimaryKeyOhneExc(iIdLieferscheinI);
		if (lieferscheinDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return lieferscheinDto;
	}

	public LieferscheinDto lieferscheinFindByPrimaryKeyOhneExc(Integer iIdLieferscheinI) {

		Lieferschein lieferschein = em.find(Lieferschein.class, iIdLieferscheinI);
		if (lieferschein == null) {
			return null;
		}

		return assembleLieferscheinDto(lieferschein);
	}

	public LieferscheinDto lieferscheinFindByCNrMandantCNr(String cNr, String mandantCNr) {
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
	 * @param iIdAuftragI  Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return LieferscheinDto[]
	 */
	public LieferscheinDto[] lieferscheinFindByAuftrag(Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAuftragI == null"));
		}
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Suchen aller LS-Positionen, die sich auf diesen Auftrag beziehen.
			Criteria c = session.createCriteria(FLRLieferscheinposition.class);
			c.createCriteria(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRPOSITIONENSICHTAUFTRAG)
					.add(Restrictions.eq(AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAG_I_ID, iIdAuftragI));
			// Query ausfuehren
			List<?> list = c.list();
			// Damit das ganze "distinct" wird, geb ich die Auftrags-ID's in
			// eine Hashmap.
			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRLieferscheinposition item = (FLRLieferscheinposition) iter.next();
				hm.put(item.getFlrlieferschein().getI_id(), item.getFlrlieferschein().getI_id());
			}

			// PJ18792
			// Alle Lieferscheine keine auftragsbezogenen Positioen haben, aber
			// in den Kopfdaten einen auftragsbezug haben

			session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Query query = session
					.createQuery("SELECT ls FROM FLRLieferschein ls WHERE ls.auftrag_i_id=" + iIdAuftragI);

			List resultList = query.list();
			Iterator resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {

				FLRLieferschein ls = (FLRLieferschein) resultListIterator.next();

				if (!hm.containsKey(ls.getI_id())) {
					hm.put(ls.getI_id(), ls.getI_id());
				}
			}

			// in der HM stehen jetzt die Auftrags-ID's.
			LieferscheinDto[] aLieferscheinDto = new LieferscheinDto[hm.size()];
			int index = 0;
			for (Iterator<?> iter = hm.keySet().iterator(); iter.hasNext();) {
				Integer item = (Integer) iter.next();
				aLieferscheinDto[index] = lieferscheinFindByPrimaryKey(item, theClientDto);
				index++;
			}
			return aLieferscheinDto;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public LieferscheinDto[] lieferscheinFindByKundeIIdLieferadresseMandantCNr(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheinDto[] aLieferscheinDtos = null;
		Query query = em.createNamedQuery("LieferscheinfindByKundeIIdLieferadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> lieferscheinDtos = query.getResultList();
		aLieferscheinDtos = assembleLieferscheinDtos(lieferscheinDtos);
		return aLieferscheinDtos;
	}

	public LieferscheinDto[] lieferscheinFindByKundeIIdLieferadresseMandantCNrOhneExc(Integer iIdKundeI,
			String cNrMandantI, TheClientDto theClientDto) {
		LieferscheinDto[] aLieferscheinDtos = null;
		try {
			Query query = em.createNamedQuery("LieferscheinfindByKundeIIdLieferadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			aLieferscheinDtos = assembleLieferscheinDtos(query.getResultList());
		} catch (Throwable ex) {
			// nothing
		}
		return aLieferscheinDtos;
	}

	public LieferscheinDto[] lieferscheinFindByKundeIIdRechnungsadresseMandantCNr(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheinDto[] aLieferscheinDtos = null;
		Query query = em.createNamedQuery("LieferscheinfindByKundeIIdRechnungsadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> lieferscheinDtos = query.getResultList();
		aLieferscheinDtos = assembleLieferscheinDtos(lieferscheinDtos);
		return aLieferscheinDtos;
	}

	public LieferscheinDto[] lieferscheinFindByKundeIIdRechnungsadresseMandantCNrOhneExc(Integer iIdKundeI,
			String cNrMandantI, TheClientDto theClientDto) {
		LieferscheinDto[] aLieferscheinDtos = null;
		try {
			Query query = em.createNamedQuery("LieferscheinfindByKundeIIdRechnungsadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			aLieferscheinDtos = assembleLieferscheinDtos(query.getResultList());
		} catch (Throwable ex) {
			// nothing
		}
		return aLieferscheinDtos;
	}

	public LieferscheinDto[] lieferscheinFindByAnsprechpartnerIIdMandantCNr(Integer iIdAnsprechpartnerI,
			String cNrMandantI, TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheinDto[] aLieferscheinDtos = null;
		Query query = em.createNamedQuery("LieferscheinfindByAnsprechpartnerIIdMandantCNr");
		query.setParameter(1, iIdAnsprechpartnerI);
		query.setParameter(2, cNrMandantI);
		Collection<?> lieferscheinDtos = query.getResultList();
		aLieferscheinDtos = assembleLieferscheinDtos(lieferscheinDtos);
		return aLieferscheinDtos;
	}

	public LieferscheinDto[] lieferscheinFindByAnsprechpartnerIIdMandantCNrOhneExc(Integer iIdAnsprechpartnerI,
			String cNrMandantI, TheClientDto theClientDto) {
		LieferscheinDto[] aLieferscheinDtos = null;
		try {
			Query query = em.createNamedQuery("LieferscheinfindByAnsprechpartnerIIdMandantCNr");
			query.setParameter(1, iIdAnsprechpartnerI);
			query.setParameter(2, cNrMandantI);
			aLieferscheinDtos = assembleLieferscheinDtos(query.getResultList());
		} catch (Throwable th) {
			return null;
		}
		return aLieferscheinDtos;
	}

	private void checkLieferscheinDto(LieferscheinDto lieferscheinDtoI) throws EJBExceptionLP {
		if (lieferscheinDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("lieferscheinDto == null"));
		}

		myLogger.info("LieferscheinDto: " + lieferscheinDtoI.toString());
	}

	private void checkLieferscheinIId(Integer iIdLieferscheinI) throws EJBExceptionLP {
		Validator.notNull(iIdLieferscheinI, "iIdLieferscheinI");

		myLogger.info("LieferscheinIId: " + iIdLieferscheinI.toString());
	}

	public void toggleZollexportpapiereErhalten(Integer lieferscheinIId, String cZollexportpapier,
			Integer eingangsrechnungIId_Zollexport, TheClientDto theClientDto) {
		Lieferschein lieferschein = em.find(Lieferschein.class, lieferscheinIId);

		LieferscheinDto lsDto_vorher = lieferscheinFindByPrimaryKey(lieferscheinIId);
		LieferscheinDto lsDto_nachher = lieferscheinFindByPrimaryKey(lieferscheinIId);

		if (lieferschein.getTZollexportpapier() == null) {
			lieferschein.setTZollexportpapier(new Timestamp(System.currentTimeMillis()));
			lieferschein.setPersonalIIdZollexportpapier(theClientDto.getIDPersonal());
			lieferschein.setCZollexportpapier(cZollexportpapier);
			lieferschein.setEingangsrechnungIdZollexport(eingangsrechnungIId_Zollexport);

			lsDto_nachher.setTZollexportpapier(new Timestamp(System.currentTimeMillis()));
			lsDto_nachher.setPersonalIIdZollexportpapier(theClientDto.getIDPersonal());
			lsDto_nachher.setCZollexportpapier(cZollexportpapier);
			lsDto_nachher.setEingangsrechnungIdZollexport(eingangsrechnungIId_Zollexport);

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

		HvDtoLogger<LieferscheinDto> lsLogger = new HvDtoLogger<LieferscheinDto>(em, lsDto_vorher.getIId(),
				theClientDto);
		lsLogger.log(lsDto_vorher, lsDto_nachher);

	}

	private void setLieferscheinFromLieferscheinDto(Lieferschein lieferschein, LieferscheinDto lieferscheinDto) {
		if (lieferscheinDto.getCNr() != null) {
			lieferschein.setCNr(lieferscheinDto.getCNr());
		}
		if (lieferscheinDto.getMandantCNr() != null) {
			lieferschein.setMandantCNr(lieferscheinDto.getMandantCNr());
		}
		if (lieferscheinDto.getLieferscheinartCNr() != null) {
			lieferschein.setLieferscheinartCNr(lieferscheinDto.getLieferscheinartCNr());
		}
		if (lieferscheinDto.getStatusCNr() != null) {
			lieferschein.setLieferscheinstatusCNr(lieferscheinDto.getStatusCNr());
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
			lieferschein.setKundeIIdLieferadresse(lieferscheinDto.getKundeIIdLieferadresse());
		}
		// der Ansprechpartner kann null sein
		lieferschein.setAnsprechpartnerIIdKunde(lieferscheinDto.getAnsprechpartnerIId());

		if (lieferscheinDto.getPersonalIIdVertreter() != null) {
			lieferschein.setPersonalIIdVertreter(lieferscheinDto.getPersonalIIdVertreter());
		}
		if (lieferscheinDto.getKundeIIdRechnungsadresse() != null) {
			lieferschein.setKundeIIdRechnungsadresse(lieferscheinDto.getKundeIIdRechnungsadresse());
		}

		lieferschein.setCBez(lieferscheinDto.getCBezProjektbezeichnung());
		lieferschein.setCBestellnummer(lieferscheinDto.getCBestellnummer());

		if (lieferscheinDto.getLagerIId() != null) {
			lieferschein.setLagerIId(lieferscheinDto.getLagerIId());
		}
		lieferschein.setZiellagerIId(lieferscheinDto.getZiellagerIId());
		if (lieferscheinDto.getKostenstelleIId() != null) {
			lieferschein.setKostenstelleIId(lieferscheinDto.getKostenstelleIId());
		}
		lieferschein.setTLiefertermin(lieferscheinDto.getTLiefertermin());
		lieferschein.setTRueckgabetermin(lieferscheinDto.getTRueckgabetermin());
		if (lieferscheinDto.getFVersteckterAufschlag() != null) {
			lieferschein.setFVersteckteraufschlag(lieferscheinDto.getFVersteckterAufschlag());
		}
		if (lieferscheinDto.getFAllgemeinerRabattsatz() != null) {
			lieferschein.setFAllgemeinerrabatt(lieferscheinDto.getFAllgemeinerRabattsatz());
		}
		if (lieferscheinDto.getLieferartIId() != null) {
			lieferschein.setLieferartIId(lieferscheinDto.getLieferartIId());
		}
		if (lieferscheinDto.getIAnzahlPakete() != null) {
			lieferschein.setIAnzahlpakete(lieferscheinDto.getIAnzahlPakete());
		}
		if (lieferscheinDto.getFGewichtLieferung() != null) {
			lieferschein.setFGewichtlieferung(lieferscheinDto.getFGewichtLieferung());
		}
		if (lieferscheinDto.getCVersandnummer() != null) {
			lieferschein.setCVersandnummer(lieferscheinDto.getCVersandnummer());
		}
		if (lieferscheinDto.getTGedruckt() != null) {
			lieferschein.setTGedruckt(lieferscheinDto.getTGedruckt());
		}
		if (lieferscheinDto.getPersonalIIdManuellerledigt() != null) {
			lieferschein.setPersonalIIdManuellerledigt(lieferscheinDto.getPersonalIIdManuellerledigt());
		}
		if (lieferscheinDto.getTManuellerledigt() != null) {
			lieferschein.setTManuellerledigt(lieferscheinDto.getTManuellerledigt());
		}

		if (lieferscheinDto.getPersonalIIdStorniert() != null) {
			lieferschein.setPersonalIIdStorniert(lieferscheinDto.getPersonalIIdStorniert());
		}
		if (lieferscheinDto.getTStorniert() != null) {
			lieferschein.setTStorniert(lieferscheinDto.getTStorniert());
		}

		lieferschein.setPersonalIIdAnlegen(lieferscheinDto.getPersonalIIdAnlegen());
		lieferschein.setTAnlegen(lieferscheinDto.getTAnlegen());
		lieferschein.setPersonalIIdAendern(lieferscheinDto.getPersonalIIdAendern());
		lieferschein.setTAendern(lieferscheinDto.getTAendern());

		if (lieferscheinDto.getWaehrungCNr() != null) {
			lieferschein.setWaehrungCNrLieferscheinwaehrung(lieferscheinDto.getWaehrungCNr());
		}
		if (lieferscheinDto.getFWechselkursmandantwaehrungzubelegwaehrung() != null) {
			lieferschein.setFWechselkursmandantwaehrungzulieferscheinwaehrung(
					lieferscheinDto.getFWechselkursmandantwaehrungzubelegwaehrung());
		}
		lieferschein.setNGesamtwertinlieferscheinwaehrung(lieferscheinDto.getNGesamtwertInLieferscheinwaehrung());
		lieferschein.setNGestehungswertinmandantenwaehrung(lieferscheinDto.getNGestehungswertInMandantenwaehrung());
		if (lieferscheinDto.getZahlungszielIId() != null) {
			lieferschein.setZahlungszielIId(lieferscheinDto.getZahlungszielIId());
		}
		if (lieferscheinDto.getSpediteurIId() != null) {
			lieferschein.setSpediteurIId(lieferscheinDto.getSpediteurIId());
		}
		lieferschein.setLieferscheintextIIdKopftext(lieferscheinDto.getLieferscheintextIIdDefaultKopftext());
		lieferschein.setLieferscheintextIIdFusstext(lieferscheinDto.getLieferscheintextIIdDefaultFusstext());
		lieferschein.setXKopftextuebersteuert(lieferscheinDto.getCLieferscheinKopftextUeberschrieben());
		lieferschein.setXFusstextuebersteuert(lieferscheinDto.getCLieferscheinFusstextUeberschrieben());
		lieferschein.setRechnungIId(lieferscheinDto.getRechnungIId());
		lieferschein.setAuftragIId(lieferscheinDto.getAuftragIId());
		lieferschein.setCKommission(lieferscheinDto.getCKommission());
		lieferschein.setBegruendungIId(lieferscheinDto.getBegruendungIId());
		lieferschein.setAnsprechpartnerIIdRechnungsadresse(lieferscheinDto.getAnsprechpartnerIIdRechnungsadresse());
		lieferschein.setCLieferartort(lieferscheinDto.getCLieferartort());
		lieferschein.setProjektIId(lieferscheinDto.getProjektIId());
		lieferschein.setTZollexportpapier(lieferscheinDto.getTZollexportpapier());
		lieferschein.setPersonalIIdZollexportpapier(lieferscheinDto.getPersonalIIdZollexportpapier());
		lieferschein.setEingangsrechnungIdZollexport(lieferscheinDto.getEingangsrechnungIdZollexport());
		lieferschein.setCZollexportpapier(lieferscheinDto.getCZollexportpapier());
		lieferschein.setTLieferaviso(lieferscheinDto.getTLieferaviso());
		lieferschein.setPersonalIIdLieferaviso(lieferscheinDto.getPersonalIIdLieferaviso());
		lieferschein.setCVersandnummer2(lieferscheinDto.getCVersandnummer2());
		lieferschein.setIKommissioniertyp(lieferscheinDto.getIKommissioniertyp());
		lieferschein.setLaenderartCnr(lieferscheinDto.getLaenderartCnr());
		lieferschein.setXInternerkommentar(lieferscheinDto.getXInternerkommentar());
		em.merge(lieferschein);
		em.flush();
	}

	private LieferscheinDto assembleLieferscheinDto(Lieferschein lieferschein) {
		return LieferscheinDtoAssembler.createDto(lieferschein);
	}

	private LieferscheinDto[] assembleLieferscheinDtos(Collection<?> lieferscheins) {
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
	 * @param pStatus String
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @throws EJBExceptionLP
	 * @return String
	 */
	public String getLieferscheinstatus(String pStatus, Locale locale1, Locale locale2) throws EJBExceptionLP {
		final String METHOD_NAME = "getLieferscheinstatus";
		myLogger.entry();
		// @todo check parameter PJ 4398
		String uebersetzung = null;
		try {
			Lieferscheinstatus status = this.em.find(Lieferscheinstatus.class, pStatus);
			if (status == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			LieferscheinstatusDto statusDto = this.assembleLieferscheinstatusDto(status);
			// uebersetzung von system holen
			uebersetzung = getSystemMultilanguageFac().uebersetzeStatusOptimal(pStatus, locale1, locale2);
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		return uebersetzung;
	}

	public String getLieferscheinCNr(Object[] keys) throws EJBExceptionLP {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			Lieferschein ls = this.em.find(Lieferschein.class, (Integer) keys[i]);
			if (i == keys.length - 1)
				sb.append(ls.getCNr());
			else {
				sb.append(ls.getCNr());
				sb.append(",");
			}
		}
		return sb.toString();
	}

	private LieferscheinstatusDto assembleLieferscheinstatusDto(Lieferscheinstatus pStatus) {
		return LieferscheinstatusDtoAssembler.createDto(pStatus);
	}

	// Lieferscheinpositionart
	// ---------------------------------------------------

	public Map getLieferscheinpositionart(Locale locale1, Locale locale2) throws EJBExceptionLP {
		final String METHOD_NAME = "getLieferscheinpositionart";
		myLogger.entry();
		// @todo param PJ 4398
		Map<?, ?> map = null;
		try {
			Query query = em.createNamedQuery("LieferscheinpositionartfindAll");
			Collection<?> arten = query.getResultList();
			LieferscheinpositionartDto[] artenDtos = this.assembleLieferscheinpositionartDtos(arten);
			map = getSystemMultilanguageFac().uebersetzePositionsartOptimal(artenDtos, locale1, locale2);
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		return map;
	}

	private LieferscheinpositionartDto assembleLieferscheinpositionartDto(
			Lieferscheinpositionart lieferscheinpositionart) {
		return LieferscheinpositionartDtoAssembler.createDto(lieferscheinpositionart);
	}

	private LieferscheinpositionartDto[] assembleLieferscheinpositionartDtos(Collection<?> lieferscheinpositionarts) {
		List<LieferscheinpositionartDto> list = new ArrayList<LieferscheinpositionartDto>();
		if (lieferscheinpositionarts != null) {
			Iterator<?> iterator = lieferscheinpositionarts.iterator();
			while (iterator.hasNext()) {
				Lieferscheinpositionart lieferscheinpositionart = (Lieferscheinpositionart) iterator.next();
				list.add(assembleLieferscheinpositionartDto(lieferscheinpositionart));
			}
		}
		LieferscheinpositionartDto[] returnArray = new LieferscheinpositionartDto[list.size()];
		return (LieferscheinpositionartDto[]) list.toArray(returnArray);
	}

	// Lieferscheinart
	// -----------------------------------------------------------

	/**
	 * Alle Lieferscheinarten in der bestmoeglichen Uebersetzung holen.
	 * 
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getLieferscheinart(Locale locale1, Locale locale2) throws EJBExceptionLP {
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
	 * Hole die bestmoeglichen Uebersetzungen fuer ein Array von Lieferscheinarten.
	 * 
	 * @param pArray  Stati
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	private Map<String, String> uebersetzeLieferscheinartOptimal(LieferscheinartDto[] pArray, Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		final String METHOD_NAME = "";
		myLogger.entry();

		// @todo check param PJ 4398

		Map<String, String> uebersetzung = new TreeMap<String, String>();

		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = uebersetzeLieferscheinartOptimal(pArray[i].getCNr(), locale1, locale2);
			uebersetzung.put(key, value);
		}

		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Lieferscheinart optimal. 1.Versuch: mit locale1 2.Versuch:
	 * mit locale2 3.Versuch: cNr
	 * 
	 * @param cNr     der Name des Lieferscheins
	 * @param locale1 das bevorzugte Locale
	 * @param locale2 das Ersatzlocale
	 * @throws EJBExceptionLP
	 * @return String die Lieferarten mit ihrer Uebersetzung
	 */
	private String uebersetzeLieferscheinartOptimal(String cNr, Locale locale1, Locale locale2) throws EJBExceptionLP {
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

	private LieferscheinartDto assembleLieferscheinartDto(Lieferscheinart lieferscheinart) {
		return LieferscheinartDtoAssembler.createDto(lieferscheinart);
	}

	private LieferscheinartDto[] assembleLieferscheinartDtos(Collection<?> lieferscheinarts) {
		List<LieferscheinartDto> list = new ArrayList<LieferscheinartDto>();
		if (lieferscheinarts != null) {
			Iterator<?> iterator = lieferscheinarts.iterator();
			while (iterator.hasNext()) {
				Lieferscheinart lieferscheinart = (Lieferscheinart) iterator.next();
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
	 * @param pLocale Locale
	 * @param pArt    String
	 * @throws EJBExceptionLP
	 * @return String
	 */
	private String uebersetzeLieferscheinart(Locale pLocale, String pArt) throws EJBExceptionLP {
		myLogger.entry();
		Lieferscheinartspr spr = null;
		String locale = Helper.locale2String(pLocale);
		Query query = em.createNamedQuery("LieferscheinartsprfindBySpracheAndCNr");
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

	private void checkLieferscheinpositionDto(LieferscheinpositionDto lieferscheinpositionDto) throws EJBExceptionLP {
		if (lieferscheinpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lieferscheinpositionDto == null"));
		}
	}

	/**
	 * Aus einem Lieferschein jene Position heraussuchen, die zu einer bestimmten
	 * Auftragposition gehoert.
	 * 
	 * @param iIdLieferscheinI    pk des Lieferscheins
	 * @param iIdAuftragpositionI pk der Auftragposition
	 * @throws EJBExceptionLP
	 * @return LieferscheinpositionDto die entsprechende Position, null wenn es
	 *         keine gibt
	 */
	public LieferscheinpositionDto getLieferscheinpositionByLieferscheinAuftragposition(Integer iIdLieferscheinI,
			Integer iIdAuftragpositionI) throws EJBExceptionLP {
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
			Query query = em.createNamedQuery("LieferscheinpositionfindByLieferscheinIIdAuftragpositionIId");
			query.setParameter(1, iIdLieferscheinI);
			query.setParameter(2, iIdAuftragpositionI);
			oLieferscheinpositionDtoO = assembleLieferscheinpositionDto((Lieferscheinposition) query.getSingleResult());
		} catch (Throwable t) {
			// es gibt keine Lieferscheinposition mit diesen Eigenschaften
		}

		return oLieferscheinpositionDtoO;
	}

	private LieferscheinpositionDto assembleLieferscheinpositionDto(Lieferscheinposition lieferscheinposition) {

		LieferscheinpositionDto lieferscheinpositionDto = LieferscheinpositionDtoAssembler
				.createDto(lieferscheinposition);
		lieferscheinpositionDto.setSeriennrChargennrMitMenge(
				getLagerFac().getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
						LocaleFac.BELEGART_LIEFERSCHEIN, lieferscheinpositionDto.getIId()));

		return lieferscheinpositionDto;
	}

	private LieferscheinpositionDto[] assembleLieferscheinpositionDtos(Collection<?> lieferscheinpositions) {
		List<LieferscheinpositionDto> list = new ArrayList<LieferscheinpositionDto>();
		if (lieferscheinpositions != null) {
			Iterator<?> iterator = lieferscheinpositions.iterator();
			while (iterator.hasNext()) {
				Lieferscheinposition lieferscheinposition = (Lieferscheinposition) iterator.next();
				list.add(assembleLieferscheinpositionDto(lieferscheinposition));
			}
		}
		LieferscheinpositionDto[] returnArray = new LieferscheinpositionDto[list.size()];
		return (LieferscheinpositionDto[]) list.toArray(returnArray);
	}

	/**
	 * Den Status eines Lieferscheins auf 'Geliefert' setzen und seinen Wert
	 * berechnen.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     String der aktuelle Benutzer
	 * @throws EJBExceptionLP  Ausnahme
	 * @throws RemoteException
	 */
	public void aktiviereLieferschein(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		myLogger.entry();
		Validator.notNull(iIdLieferscheinI, "IdLieferscheinI");
		pruefeAktivierbar(iIdLieferscheinI, theClientDto);
		// Wert berechnen
		berechneBeleg(iIdLieferscheinI, theClientDto);
		// und Status aendern
		aktiviereBeleg(iIdLieferscheinI, theClientDto);
	}

	@Override
	public void pruefeAktivierbar(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Lieferschein oLieferschein = em.find(Lieferschein.class, iid);
		if (oLieferschein == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// Ohne Positionen darf der Beleg nicht aktiviert werden.
		if (getLieferscheinpositionFac().berechneAnzahlMengenbehaftetePositionen(iid, theClientDto) == 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN, "");
		}

		pruefeZwischensumme(iid, theClientDto);
	}
	
	private void pruefeZwischensumme(Integer lieferscheinId, TheClientDto theClientDto) throws RemoteException {
		LieferscheinpositionDto[] allPos = getLieferscheinpositionFac()
				.lieferscheinpositionFindByLieferscheinIId(lieferscheinId);
		getBelegVerkaufFac().validiereZwsAufGleichenMwstSatzThrow(allPos);
	}

	@Override
	public BelegPruefungDto aktiviereBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Lieferschein oLieferschein = em.find(Lieferschein.class, iid);
		if (oLieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
			oLieferschein.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
			// den Druckzeitpunkt vermerken
			oLieferschein.setTGedruckt(getTimestamp());

			// PJ20677
			ParametermandantDto parameterPositionskontierung = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_LIEFERSCHEIN,

					ParameterFac.PARAMETER_AUSLIEFERDATUM_HEUTE);

			boolean bAuslieferdatumHeute = (Boolean) parameterPositionskontierung.getCWertAsObject();
			if (bAuslieferdatumHeute) {
				oLieferschein.setTLiefertermin(Helper.cutTimestamp(getTimestamp()));
			}

			// SP9000
			Kunde kunde = em.find(Kunde.class, oLieferschein.getKundeIIdLieferadresse());
			getAnsprechpartnerFac().pruefeAnsprechpartnerZugehoerigkeit(kunde.getPartnerIId(),
					oLieferschein.getAnsprechpartnerIIdKunde(), theClientDto);
			Kunde kundeLieferadresse = em.find(Kunde.class, oLieferschein.getKundeIIdRechnungsadresse());
			getAnsprechpartnerFac().pruefeAnsprechpartnerZugehoerigkeit(kundeLieferadresse.getPartnerIId(),
					oLieferschein.getAnsprechpartnerIIdRechnungsadresse(), theClientDto);

		}
		return null;
	}

	// PJ19683
	public void lieferscheinGesamtwertaufgrundGeaenderterMaterialkurseImStatusVerrechnetNeuSetzten(
			Integer lieferscheinIId, TheClientDto theClientDto) {
		Lieferschein oLieferschein = em.find(Lieferschein.class, lieferscheinIId);
		if (oLieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_VERRECHNET)) {
			BigDecimal nGesamtWertInlieferscheinwaehrung = berechneGesamtwertLieferscheinAusPositionen(
					oLieferschein.getIId(), theClientDto);
			nGesamtWertInlieferscheinwaehrung = Helper.rundeKaufmaennisch(nGesamtWertInlieferscheinwaehrung, 4);
			oLieferschein.setNGesamtwertinlieferscheinwaehrung(nGesamtWertInlieferscheinwaehrung);
			em.merge(oLieferschein);
			em.flush();
		}
	}

	@Override
	public Timestamp berechneBeleg(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Lieferschein oLieferschein = em.find(Lieferschein.class, iid);
		if (oLieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
			BigDecimal nGesamtWertInlieferscheinwaehrung = berechneGesamtwertLieferscheinAusPositionen(
					oLieferschein.getIId(), theClientDto);
			nGesamtWertInlieferscheinwaehrung = Helper.rundeKaufmaennisch(nGesamtWertInlieferscheinwaehrung, 4);
			oLieferschein.setNGesamtwertinlieferscheinwaehrung(nGesamtWertInlieferscheinwaehrung);
			BigDecimal nGestehungwert = null;
			if (oLieferschein.getZiellagerIId() != null) {
				nGestehungwert = null;
			} else {
				nGestehungwert = berechneGestehungswert(oLieferschein.getIId(), theClientDto);
			}
			oLieferschein.setNGestehungswertinmandantenwaehrung(nGestehungwert);

			// // PJ2276
			// LieferscheinpositionDto[] lsPosDto = getLieferscheinpositionFac()
			// .getLieferscheinPositionenByLieferschein(
			// oLieferschein.getIId(), theClientDto);
			// Set<Integer> modifiedPositions = getBelegVerkaufFac()
			// .adaptIntZwsPositions(lsPosDto);
			// for (Integer index : modifiedPositions) {
			// Lieferscheinposition lsposEntity = em.find(
			// Lieferscheinposition.class, lsPosDto[index].getIId());
			// lsposEntity
			// .setNNettogesamtpreisplusversteckteraufschlagminusrabatte(lsPosDto[index]
			// .getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			// em.merge(lsposEntity);
			// }
		}

		return getTimestamp();
	}

	/**
	 * Der Status eines Lieferscheins kann von aussen gesetzt werden. <br>
	 * Diese Methode wird von der Rechnung verwendet, um einen Lieferschein zu
	 * verrechnen oder den Status auf 'Geliefert' zurueckzusetzen.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param iIdRechnungI     die verrechnende Rechnung, wenn der Lieferschein auf
	 *                         Status Verrechnet gesetzt wird, sonst null
	 * @param sStatusI         der Status
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void setzeStatusLieferschein(Integer iIdLieferscheinI, String sStatusI, Integer iIdRechnungI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.notNull(iIdLieferscheinI, "");
		Validator.notEmpty(sStatusI, "");
		validateAenderungStatusLieferschein(sStatusI, iIdRechnungI);

		setzeStatusLieferscheinImpl(iIdLieferscheinI, sStatusI, iIdRechnungI);
	}

	private void validateAenderungStatusLieferschein(String statusCnr, Integer rechnungIId) {
		if (LieferscheinFac.LSSTATUS_VERRECHNET.equals(statusCnr)) {
			if (rechnungIId == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
						new Exception("sStatusI.equals('Verrechnet') && iIdRechnungI == null"));
			}
		}

		if (LieferscheinFac.LSSTATUS_GELIEFERT.equals(statusCnr)) {
			if (rechnungIId != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NOT_NULL,
						new Exception("sStatusI.equals('Geliefert') && iIdRechnungI != null"));
			}
		}
	}

	private Lieferschein findLieferschein(Integer lieferscheinIId) {
		Lieferschein ls = em.find(Lieferschein.class, lieferscheinIId);
		if (ls == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Lieferschein mit iId=" + String.valueOf(lieferscheinIId) + " nicht gefunden.");
		}
		return ls;
	}

	private void setzeStatusLieferscheinImpl(Integer lieferscheinIId, String statusCnr, Integer rechnungIId) {

		Lieferschein ls = findLieferschein(lieferscheinIId);

		ls.setLieferscheinstatusCNr(statusCnr);

		// SP5133
		if (LieferscheinFac.LSSTATUS_ANGELEGT.equals(statusCnr)) {
			ls.setNGesamtwertinlieferscheinwaehrung(null);
			ls.setNGestehungswertinmandantenwaehrung(null);
		}

		if (LieferscheinFac.LSSTATUS_VERRECHNET.equals(statusCnr)) {
			ls.setRechnungIId(rechnungIId);
		} else {
			// bei Statuswechsel von 'Verrechnet' auf 'Geliefert'
			ls.setRechnungIId(null);
		}
		em.merge(ls);
		em.flush();
	}

	/**
	 * Berechnung des Gestehungswerts eines Lieferscheins. <br>
	 * Der Gestehungswert ist die Summe ueber die Gestehungswerte der enthaltenen
	 * Artikelpositionen. <br>
	 * Der Gestehungswert einer Artikelposition errechnet sich aus Menge x
	 * positionsbezogenem Gestehungspreis des enthaltenen Artikels.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @return BigDecimal der Gestehungswert in der gewuenschten Waehrung
	 */
	public BigDecimal berechneGestehungswert(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGestehungswert";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}

		BigDecimal bdGestehungswert = Helper.getBigDecimalNull();

		// alle Positionen dieses Lieferscheins
		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> c = query.getResultList();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Lieferscheinposition oPosition = ((Lieferscheinposition) iter.next());

			// alle positiven mengenbehafteten Positionen beruecksichtigen
			if (oPosition.getNMenge() != null && oPosition.getNMenge().doubleValue() > 0) {

				// Grundlage ist der positionsbezogene Gestehungspreis des
				// Artikels.

				if (oPosition.getLieferscheinpositionartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

					bdGestehungswert = bdGestehungswert.add(berechneGestehungswertEinerLieferscheinposition(
							assembleLieferscheinpositionDto(oPosition), theClientDto));
				}
			}
		}

		bdGestehungswert = Helper.rundeKaufmaennisch(bdGestehungswert, 4);
		checkNumberFormat(bdGestehungswert);
		return bdGestehungswert;
	}

	/**
	 * Berechnung des Gestehungswerts eines Lieferscheins. <br>
	 * Der Gestehungswert ist die Summe ueber die Gestehungswerte der enthaltenen
	 * Artikelpositionen. <br>
	 * Der Gestehungswert einer Artikelposition errechnet sich aus Menge x
	 * positionsbezogenem Gestehungspreis des enthaltenen Artikels.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @return BigDecimal der Gestehungswert in der gewuenschten Waehrung
	 */
	public BigDecimal berechneGestehungswertZielLager(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGestehungswert";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		BigDecimal bdGestehungswert = Helper.getBigDecimalNull();
		// alle Positionen dieses Lieferscheins
		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> c = query.getResultList();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Lieferscheinposition oPosition = ((Lieferscheinposition) iter.next());
			// alle positiven mengenbehafteten Positionen beruecksichtigen
			if (oPosition.getNMenge() != null && oPosition.getNMenge().doubleValue() > 0) {
				// Grundlage ist der positionsbezogene Gestehungspreis des
				// Artikels.
				BigDecimal bdGestehungspreis = Helper.getBigDecimalNull();
				BigDecimal bdWertderposition = Helper.getBigDecimalNull();
				if (oPosition.getLieferscheinpositionartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
					bdWertderposition = oPosition.getNMenge().multiply(oPosition.getNNettoeinzelpreis());
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
	 * @param lieferscheinpositionDtoI die Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @return BigDecimal der Gestehungswert
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BigDecimal berechneGestehungswertEinerLieferscheinposition(LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		BigDecimal bdGestehungswert = new BigDecimal(0);

		try {
			if (getLieferscheinpositionFac().enthaeltLieferscheinpositionLagerbewirtschaftetenArtikel(
					lieferscheinpositionDtoI.getIId(), theClientDto)) {
				BigDecimal bdGestehungpreisEinerEinheit = new BigDecimal(0);
				BigDecimal bdWertderposition = new BigDecimal(0);

				// CK: Gestehungspreise gibt es nur, wenn es eine zugehoerige
				// Lagerbewegung gibt.
				// Es gibt keine Lagerbewegungen fuer Menge <= 0
				if (lieferscheinpositionDtoI.getNMenge().doubleValue() > 0) {
					// wenn es keine Serien- oder Chargennummer gibt
					if (lieferscheinpositionDtoI.getSeriennrChargennrMitMenge() == null
							|| lieferscheinpositionDtoI.getSeriennrChargennrMitMenge().size() == 0) {
						bdGestehungpreisEinerEinheit = getLagerFac().getGestehungspreisEinerAbgangsposition(
								LocaleFac.BELEGART_LIEFERSCHEIN, lieferscheinpositionDtoI.getIId(), null);

						bdWertderposition = lieferscheinpositionDtoI.getNMenge().multiply(bdGestehungpreisEinerEinheit);

						bdGestehungswert = bdGestehungswert.add(bdWertderposition);
					}

					// getGestehungpreis() greift auf WW_LAGERBEWEGUNG zu. Hier
					// gibt es pro Serien- oder
					// Chargennummer 1 Datensatz
					else {

						for (int i = 0; i < lieferscheinpositionDtoI.getSeriennrChargennrMitMenge().size(); i++) {
							bdGestehungpreisEinerEinheit = getLagerFac()
									.getGestehungspreisEinerAbgangspositionMitTransaktion(
											LocaleFac.BELEGART_LIEFERSCHEIN, lieferscheinpositionDtoI.getIId(),
											lieferscheinpositionDtoI.getSeriennrChargennrMitMenge().get(i)
													.getCSeriennrChargennr());

							bdWertderposition = lieferscheinpositionDtoI.getSeriennrChargennrMitMenge().get(i)
									.getNMenge().multiply(bdGestehungpreisEinerEinheit);

							bdGestehungswert = bdGestehungswert.add(bdWertderposition);
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
	 * @param iIdlieferscheinpositionI die IId der Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @return BigDecimal der Gestehungswert
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BigDecimal berechneGestehungswertEinerLieferscheinposition(Integer iIdlieferscheinpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		LieferscheinpositionDto lieferscheinpositionDtoI = null;
		BigDecimal bdGestehungswert = new BigDecimal(0);
		try {
			lieferscheinpositionDtoI = getLieferscheinpositionFac()
					.lieferscheinpositionFindByPrimaryKey(iIdlieferscheinpositionI, theClientDto);
			if (getLieferscheinpositionFac().enthaeltLieferscheinpositionLagerbewirtschaftetenArtikel(
					lieferscheinpositionDtoI.getIId(), theClientDto)) {
				BigDecimal bdGestehungpreisEinerEinheit = new BigDecimal(0);
				BigDecimal bdWertderposition = new BigDecimal(0);

				// CK: Gestehungspreise gibt es nur, wenn es eine zugehoerige
				// Lagerbewegung gibt.
				// Es gibt keine Lagerbewegungen fuer Menge <= 0
				if (lieferscheinpositionDtoI.getNMenge().doubleValue() > 0) {
					// wenn es keine Serien- oder Chargennummer gibt
					if (lieferscheinpositionDtoI.getSeriennrChargennrMitMenge() == null
							|| lieferscheinpositionDtoI.getSeriennrChargennrMitMenge().size() == 0) {
						bdGestehungpreisEinerEinheit = getLagerFac().getGestehungspreisEinerAbgangsposition(
								LocaleFac.BELEGART_LIEFERSCHEIN, lieferscheinpositionDtoI.getIId(), null);

						bdWertderposition = lieferscheinpositionDtoI.getNMenge().multiply(bdGestehungpreisEinerEinheit);

						bdGestehungswert = bdGestehungswert.add(bdWertderposition);
					}

					// getGestehungpreis() greift auf WW_LAGERBEWEGUNG zu. Hier
					// gibt es pro Serien- oder
					// Chargennummer 1 Datensatz
					else {

						for (int i = 0; i < lieferscheinpositionDtoI.getSeriennrChargennrMitMenge().size(); i++) {
							bdGestehungpreisEinerEinheit = getLagerFac().getGestehungspreisEinerAbgangsposition(
									LocaleFac.BELEGART_LIEFERSCHEIN, lieferscheinpositionDtoI.getIId(),
									lieferscheinpositionDtoI.getSeriennrChargennrMitMenge().get(i)
											.getCSeriennrChargennr());

							bdWertderposition = lieferscheinpositionDtoI.getNMenge()
									.multiply(bdGestehungpreisEinerEinheit);

							bdGestehungswert = bdGestehungswert.add(bdWertderposition);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bdGestehungswert;
	}

	public BigDecimal getEKWertUeberLoseAusKundenlaegern(Integer lieferscheinpositionIId, boolean bMaxSollmenge,
			TheClientDto theClientDto) {

		try {
			LieferscheinpositionDto lsposDto = getLieferscheinpositionFac()
					.lieferscheinpositionFindByPrimaryKey(lieferscheinpositionIId, theClientDto);

			LieferscheinDto lsDto = lieferscheinFindByPrimaryKey(lsposDto.getLieferscheinIId());

			ArrayList<WarenzugangsreferenzDto> weReferanz = getLagerFac().getWareneingangsreferenz(
					LocaleFac.BELEGART_LIEFERSCHEIN, lieferscheinpositionIId, lsposDto.getSeriennrChargennrMitMenge(),
					false, theClientDto);

			BigDecimal bdSummeEKWertAusKundenlaegern = BigDecimal.ZERO;

			for (int k = 0; k < weReferanz.size(); k++) {

				WarenzugangsreferenzDto ref = weReferanz.get(k);
				if (ref.getBelegart().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(ref.getBelegartIId());

					LossollmaterialDto[] sollmatDtos = getFertigungFac().lossollmaterialFindByLosIId(losDto.getIId());
					for (LossollmaterialDto sollmatDto : sollmatDtos) {

						if (!Helper.short2boolean(sollmatDto.getBNachtraeglich())) {

							BigDecimal bdIstmengeGesamtAusKundenkonsignationslager = BigDecimal.ZERO;
							BigDecimal bdEinstandswertGesamtAusKundenkonsignationslager = BigDecimal.ZERO;

							LosistmaterialDto[] istmaterialDtos = getFertigungFac()
									.losistmaterialFindByLossollmaterialIId(sollmatDto.getIId());

							BigDecimal bdSummeIstmaterial = BigDecimal.ZERO;

							for (int x = 0; x < istmaterialDtos.length; x++) {
								LosistmaterialDto item = istmaterialDtos[x];

								if (Helper.short2boolean(item.getBAbgang())) {
									bdSummeIstmaterial = bdSummeIstmaterial.add(item.getNMenge());
								} else {
									bdSummeIstmaterial = bdSummeIstmaterial.subtract(item.getNMenge());
								}

								LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(item.getLagerIId());
								boolean bKundenkonsignationslager = Helper
										.short2boolean(lagerDto.getBKonsignationslager());
								if (bKundenkonsignationslager) {

									ArrayList<WarenzugangsreferenzDto> weReferenz = getLagerFac()
											.getWareneingangsreferenz(LocaleFac.BELEGART_LOS, item.getIId(),
													getLagerFac()
															.getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
																	LocaleFac.BELEGART_LOS, item.getIId()),
													false, theClientDto);

									for (int m = 0; m < weReferenz.size(); m++) {
										WarenzugangsreferenzDto wa = weReferenz.get(m);

										if (wa.getBelegart() != null
												&& wa.getBelegart().equals(LocaleFac.BELEGART_BESTELLUNG.trim())
												&& wa.getMenge().doubleValue() > 0) {
											WareneingangspositionDto weposDto = getWareneingangFac()
													.wareneingangspositionFindByPrimaryKeyOhneExc(
															wa.getBelegartpositionIId());
											if (weposDto != null && weposDto.getNEinstandspreis() != null) {

												BestellungDto bsDto = getBestellungFac()
														.bestellungFindByPrimaryKeyOhneExc(wa.getBelegartIId());
												if (bsDto != null) {

													// Wenn die BS-Waehrung gleich ist, dann m�ssen wir nicht
													// umrechnen
													BigDecimal einstandspreisProStueckInLieferscheinwaehrung = BigDecimal.ZERO;
													if (lsDto.getWaehrungCNr().equals(bsDto.getWaehrungCNr())) {
														einstandspreisProStueckInLieferscheinwaehrung = weposDto
																.getNEinstandspreis();
													} else {
														einstandspreisProStueckInLieferscheinwaehrung = getLocaleFac()
																.rechneUmInAndereWaehrungGerundetZuDatum(
																		einstandspreisProStueckInLieferscheinwaehrung,
																		bsDto.getWaehrungCNr(), lsDto.getWaehrungCNr(),
																		new java.sql.Date(
																				lsDto.getTBelegdatum().getTime()),
																		theClientDto);
													}

													if (einstandspreisProStueckInLieferscheinwaehrung
															.doubleValue() > 0) {

														bdIstmengeGesamtAusKundenkonsignationslager = bdIstmengeGesamtAusKundenkonsignationslager
																.add(wa.getMenge());

														bdEinstandswertGesamtAusKundenkonsignationslager = bdEinstandswertGesamtAusKundenkonsignationslager
																.add(einstandspreisProStueckInLieferscheinwaehrung
																		.multiply(wa.getMenge()));
													}

												}

											}

										}
									}

								}

							}

							if (bdEinstandswertGesamtAusKundenkonsignationslager.doubleValue() > 0) {

								if (bMaxSollmenge) {
									if (bdSummeIstmaterial.doubleValue() > sollmatDto.getNMenge().doubleValue()) {
										bdEinstandswertGesamtAusKundenkonsignationslager = bdEinstandswertGesamtAusKundenkonsignationslager
												.divide(bdSummeIstmaterial, 4, BigDecimal.ROUND_HALF_EVEN)
												.multiply(sollmatDto.getNMenge());
									}
								}

								BigDecimal preisProWEReferenz = bdEinstandswertGesamtAusKundenkonsignationslager
										.divide(losDto.getNLosgroesse(), 4, BigDecimal.ROUND_HALF_EVEN)
										.multiply(ref.getMenge());

								bdSummeEKWertAusKundenlaegern = bdSummeEKWertAusKundenlaegern.add(preisProWEReferenz);

							}

						}

					}

				}
			}

			return bdSummeEKWertAusKundenlaegern;
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}

	}

	/**
	 * Den Gesamtwert eines Lieferscheins berechnen.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @return BigDecimal der Gesamtwert des Lieferscheins
	 */
	public BigDecimal berechneGesamtwertLieferschein(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGesamtwertLieferschein";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		BigDecimal wertDesLieferscheins = Helper.getBigDecimalNull();
		// der aktuelle Lieferschein
		Lieferschein lieferschein = em.find(Lieferschein.class, iIdLieferscheinI);
		if (lieferschein == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// Step 1: Wenn der Lieferscheinwert NULL und Status ANGELEGT, dann den
		// Wert aus den Positionen berechnen
		if (lieferschein.getNGesamtwertinlieferscheinwaehrung() == null
				&& lieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
			wertDesLieferscheins = berechneGesamtwertLieferscheinAusPositionen(iIdLieferscheinI, theClientDto);
		} else

		// Step 2: Wenn der status STORNIERT ist, 0 zurueckgeben obwohl der
		// auftragswert noch in der Tabelle steht
		if (lieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_STORNIERT)) {
			wertDesLieferscheins = Helper.getBigDecimalNull();
		}

		else

		// Step 3: Wenn der status OFFEN ist, den Wert aus der Tabelle holen
		if (lieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_OFFEN)
				|| lieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_GELIEFERT)
				|| lieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_ERLEDIGT)
				|| lieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_VERRECHNET)) {
			if (lieferschein.getNGesamtwertinlieferscheinwaehrung() != null) {
				wertDesLieferscheins = lieferschein.getNGesamtwertinlieferscheinwaehrung();
			}
		}

		// Step 4: Der Wert muss in unsere Datenbank passen, fuer decimal(9)
		// gilt 15,4
		wertDesLieferscheins = Helper.rundeKaufmaennisch(wertDesLieferscheins, 4);
		checkNumberFormat(wertDesLieferscheins);
		return wertDesLieferscheins;
	}

	/**
	 * Den Gesamtwert eines Lieferscheins aus seinen Positionen berechnen.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     der aktuelle Benutzer
	 * @return BigDecimal der Gesamtwert in Lieferscheinwaehrung
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BigDecimal berechneGesamtwertLieferscheinAusPositionen(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.notNull(iIdLieferscheinI, "iIdLieferscheinI");
		try {
			// alle Positionen dieses Lieferscheins
			LieferscheinpositionDto[] aPositionDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);
			LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(iIdLieferscheinI, theClientDto);

			Set<Integer> modifiedPositions = getBelegVerkaufFac().prepareIntZwsPositions(aPositionDtos,
					lieferscheinDto);

			BigDecimal bdGesamtwertO = getBelegVerkaufFac().getGesamtwertinBelegwaehrung(aPositionDtos,
					lieferscheinDto);
			getBelegVerkaufFac().adaptIntZwsPositions(aPositionDtos);
			getBelegVerkaufFac().saveIntZwsPositions(aPositionDtos, modifiedPositions, Lieferscheinposition.class);

			for (LieferscheinpositionDto lsPositionDto : aPositionDtos) {
				if (lsPositionDto.isIntelligenteZwischensumme()) {
					getLieferscheinpositionFac().updateLieferscheinpositionOhneWeitereAktion(lsPositionDto,
							theClientDto);
				}
			}

			return bdGesamtwertO;
			/*
			 * for (int i = 0; i < aPositionDtos.length; i++) { LieferscheinpositionDto
			 * oPositionDto = aPositionDtos[i];
			 * 
			 * // alle mengenbehafteten Positionen beruecksichtigen //IMS 2129 if
			 * (oPositionDto.getNMenge() != null) {
			 * 
			 * BigDecimal bdMenge = oPositionDto.getNMenge();
			 * 
			 * // Grundlage ist der NettogesamtwertPlusVersteckterAufschlagMinusRabatt der
			 * Position in Lieferscheinwaehrung BigDecimal bdWertDerPosition =
			 * bdMenge.multiply(oPositionDto.
			 * getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			 * 
			 * bdGesamtwertO = bdGesamtwertO.add(bdWertDerPosition); } }
			 */
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private Long searchNextPackstuecknummer() {
		Query query = em.createNamedQuery("selectgNextPackstuecknummer");
		Long l = (Long) query.getSingleResult();

		if (l == null) {
			l = 0L;
		}

		return ++l;
	}

	public Long getNextPackstuecknummer(Integer lieferscheinIId, Integer lieferscheinpositionIId, Integer losIId,
			Integer losablieferungIId, TheClientDto theClientDto) {

		Long l = searchNextPackstuecknummer();

		// Speichern
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PACKSTUECK);

		Packstueck p = new Packstueck(pk, l, theClientDto.getIDPersonal(), new Timestamp(System.currentTimeMillis()));

		if (losablieferungIId != null || losIId != null) {
			p.setLosIId(losIId);
			p.setLosablieferungIId(losablieferungIId);
		} else {
			p.setLieferscheinIId(lieferscheinIId);
			p.setLieferscheinpositionIId(lieferscheinpositionIId);
		}

		em.merge(p);
		em.flush();

		return l;
	}

	public Long getNextPackstuecknummerForecast(Integer forecastpositionId, Integer losId, TheClientDto theClientDto) {
		Validator.notNull(forecastpositionId, "forecastpositionId");

		Long l = searchNextPackstuecknummer();

		// Speichern
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PACKSTUECK);

		Packstueck p = new Packstueck(pk, l, theClientDto.getIDPersonal(), new Timestamp(System.currentTimeMillis()));

		p.setForecastpositionIId(forecastpositionId);
		p.setLosIId(losId);
		em.persist(p);
		em.flush();

		return l;
	}

	/**
	 * Auf einen Basiswert saemtliche Zu- und Abschlaege anrechnen, die in den
	 * Konditionen eines Lieferscheins hinterlegt sind.
	 * 
	 * @param lieferscheinIId PK des Lieferscheins
	 * @throws EJBExceptionLP Ausnahme
	 * @return BigDecimal der errechnete Wert
	 */

	public boolean enthaeltLieferscheinNullPreise(Integer lieferscheinIId) {

		String queryString = "SELECT count(ls.n_nettogesamtpreisplusversteckteraufschlagminusrabatt) FROM FLRLieferscheinposition AS ls WHERE ls.n_nettogesamtpreisplusversteckteraufschlagminusrabatt=0 AND ls.flrartikel.b_vkpreispflichtig=1 AND ls.flrartikel.i_id IS NOT NULL AND ls.flrlieferschein.i_id="
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

	private BigDecimal berechneWertMitZuUndAbschlaegenAusKonditionen(BigDecimal bdWertI, Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheinDto oDto = lieferscheinFindByPrimaryKey(iIdLieferscheinI, theClientDto);

		// zuerst den versteckten Aufschlag aufschlagen ...
		if (oDto.getFVersteckterAufschlag().doubleValue() != (double) 0) {
			BigDecimal versteckterAufschlag = bdWertI
					.multiply(new BigDecimal(oDto.getFVersteckterAufschlag().doubleValue()).movePointLeft(2));
			versteckterAufschlag = Helper.rundeKaufmaennisch(versteckterAufschlag, 4);
			bdWertI = bdWertI.add(versteckterAufschlag);
		}

		// dann den rabatt beruecksichtigen ...
		if (oDto.getFAllgemeinerRabattsatz().doubleValue() != (double) 0) {
			BigDecimal allgRabatt = bdWertI
					.multiply(new BigDecimal(oDto.getFAllgemeinerRabattsatz().doubleValue()).movePointLeft(2));
			allgRabatt = Helper.rundeKaufmaennisch(allgRabatt, 4);
			bdWertI = bdWertI.subtract(allgRabatt);
		}

		return bdWertI;
	}

	public void removeLieferscheintext(Integer iId) throws EJBExceptionLP {
		// try {
		Lieferscheintext toRemove = em.find(Lieferscheintext.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void removeLieferscheintext(LieferscheintextDto lieferscheintextDto) throws EJBExceptionLP {
		if (lieferscheintextDto != null) {
			Integer iId = lieferscheintextDto.getIId();
			removeLieferscheintext(iId);
		}
	}

	public void updateLieferscheintext(LieferscheintextDto lieferscheintextDto) throws EJBExceptionLP {
		if (lieferscheintextDto != null) {
			Integer iId = lieferscheintextDto.getIId();
			// try {
			Lieferscheintext lieferscheintext = em.find(Lieferscheintext.class, iId);
			if (lieferscheintext == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setLieferscheintextFromLieferscheintextDto(lieferscheintext, lieferscheintextDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateLieferscheintexts(LieferscheintextDto[] lieferscheintextDtos) throws EJBExceptionLP {
		if (lieferscheintextDtos != null) {
			for (int i = 0; i < lieferscheintextDtos.length; i++) {
				updateLieferscheintext(lieferscheintextDtos[i]);
			}
		}
	}

	public LieferscheintextDto lieferscheintextFindByPrimaryKey(Integer iiIIdI) throws EJBExceptionLP {
		// try {
		Lieferscheintext lieferscheintext = em.find(Lieferscheintext.class, iiIIdI);
		if (lieferscheintext == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLieferscheintextDto(lieferscheintext);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setLieferscheintextFromLieferscheintextDto(Lieferscheintext lieferscheintext,
			LieferscheintextDto lieferscheintextDto) {
		lieferscheintext.setMandantCNr(lieferscheintextDto.getMandantCNr());
		lieferscheintext.setLocaleCNr(lieferscheintextDto.getLocaleCNr());
		lieferscheintext.setMediaartCNr(lieferscheintextDto.getMediaartCNr());
		lieferscheintext.setXTextinhalt(lieferscheintextDto.getCTextinhalt());
		em.merge(lieferscheintext);
		em.flush();
	}

	private LieferscheintextDto assembleLieferscheintextDto(Lieferscheintext lieferscheintext) {
		return LieferscheintextDtoAssembler.createDto(lieferscheintext);
	}

	private LieferscheintextDto[] assembleLieferscheintextDtos(Collection<?> lieferscheintexts) {
		List<LieferscheintextDto> list = new ArrayList<LieferscheintextDto>();
		if (lieferscheintexts != null) {
			Iterator<?> iterator = lieferscheintexts.iterator();
			while (iterator.hasNext()) {
				Lieferscheintext lieferscheintext = (Lieferscheintext) iterator.next();
				list.add(assembleLieferscheintextDto(lieferscheintext));
			}
		}
		LieferscheintextDto[] returnArray = new LieferscheintextDto[list.size()];
		return (LieferscheintextDto[]) list.toArray(returnArray);
	}

	/**
	 * Einen in der db fix hinterlegten Text zum Lieferschein holen.
	 * 
	 * @param sLocaleI     gewuenschtes Locale
	 * @param sCNrI        die Bezeichnung des Textes
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @return LieferscheintextDto
	 */
	public LieferscheintextDto lieferscheintextFindByMandantLocaleCNr(String sLocaleI, String sCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheintextDto text = null;
		Lieferscheintext lieferscheintext = null;
		try {
			Query query = em.createNamedQuery("LieferscheintextfindByMandantLocaleMediaartCNr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, sLocaleI);
			query.setParameter(3, sCNrI);
			lieferscheintext = (Lieferscheintext) query.getSingleResult();
		} catch (NoResultException ex1) {
			try {
				Query query = em.createNamedQuery("LieferscheintextfindByMandantLocaleMediaartCNr");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, theClientDto.getLocUiAsString());
				query.setParameter(3, sCNrI);
				lieferscheintext = (Lieferscheintext) query.getSingleResult();
			} catch (NoResultException ex2) {
				try {
					Query query = em.createNamedQuery("LieferscheintextfindByMandantLocaleMediaartCNr");
					query.setParameter(1, theClientDto.getMandant());
					query.setParameter(2, theClientDto.getLocKonzernAsString());
					query.setParameter(3, sCNrI);
					lieferscheintext = (Lieferscheintext) query.getSingleResult();
				} catch (NoResultException ex3) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
				}
			}

		}
		return assembleLieferscheintextDto(lieferscheintext);
	}

	public BigDecimal berechneOffenenLieferscheinwert(Integer kundeIId, TheClientDto theClientDto) {
		String queryString = "SELECT ls FROM FLRLieferschein AS ls WHERE ls.b_verrechenbar=1 AND ls.lieferscheinstatus_status_c_nr IN ('"
				+ LocaleFac.STATUS_OFFEN + "','" + LocaleFac.STATUS_GELIEFERT + "') ";

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
				wert = wert.add(ls.getN_gesamtwertinlieferscheinwaehrung().divide(
						new BigDecimal(ls.getF_wechselkursmandantwaehrungzulieferscheinwaehrung()), 4,
						BigDecimal.ROUND_HALF_EVEN));
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
	 * Die Kopfdaten und Positionen eines Lieferscheins duerfen geaendert werden,
	 * wenn der Status des Lieferscheins Angelegt, Offen oder Geliefert ist.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void pruefeUndSetzeLieferscheinstatusBeiAenderung(Integer iIdLieferscheinI) throws EJBExceptionLP {
		final String METHOD_NAME = "pruefeUndSetzeLieferscheinstatusBeiAenderung";
		myLogger.entry();

		Lieferschein lieferschein = null;

		// try {
		lieferschein = em.find(Lieferschein.class, iIdLieferscheinI);
		if (lieferschein == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		String sStatus = lieferschein.getLieferscheinstatusCNr();

		if (!sStatus.equals(LieferscheinFac.LSSTATUS_ANGELEGT) && !sStatus.equals(LieferscheinFac.LSSTATUS_OFFEN)
				&& !sStatus.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception("Fehler: Lieferschein: " + lieferschein.getCNr()
							+ "kann nicht ver\u00E4ndert werden, da er sich im Status " + sStatus.trim()
							+ "befindet!"));
		}

		if (sStatus.equals(LieferscheinFac.LSSTATUS_OFFEN) || sStatus.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
			lieferschein.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_ANGELEGT);
			lieferschein.setNGesamtwertinlieferscheinwaehrung(null);
			lieferschein.setNGestehungswertinmandantenwaehrung(null);
		}
	}

	/**
	 * Wenn die Konditionen im Lieferschein geaendert werden, muessen die Werte der
	 * einzelnen Positionen und die Gesamtwerte des Lieferscheins angepasst werden.
	 * 
	 * @param iIdLieferscheinI PK des Lieferschein
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateLieferscheinKonditionen(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.notNull(iIdLieferscheinI, "iIdLieferscheinI");

		try {
			LieferscheinpositionDto[] aDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);

			// fuer jede Position die Zu- und Abschlaege neu beruecksichtigen
			for (int i = 0; i < aDtos.length; i++) {
				aDtos[i] = getLieferscheinpositionFac().befuelleZusaetzlichePreisfelder(aDtos[i].getIId(),
						theClientDto);
			}

			// PJ2276
			Set<Integer> modifiedPositions = getBelegVerkaufFac().adaptIntZwsPositions(aDtos);
			for (Integer index : modifiedPositions) {
				Lieferscheinposition lspos = em.find(Lieferscheinposition.class, aDtos[index].getIId());
				lspos.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
						aDtos[index].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
				em.merge(lspos);
				em.flush();
			}

			for (int i = 0; i < aDtos.length; i++) {
				if (aDtos[i].getLieferscheinpositionartCNr().equals(
						LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT) && aDtos[i].getNMenge() != null) {
					if (aDtos[i].getNMenge().signum() > 0) {
						getLieferscheinpositionFac().bucheAbLager(aDtos[i], theClientDto);

					} else if (aDtos[i].getNMenge().signum() < 0) {
						getLieferscheinpositionFac().bucheZuLager(aDtos[i], theClientDto);

					}
				}
			}

			Lieferschein oLieferschein = em.find(Lieferschein.class, iIdLieferscheinI);
			if (oLieferschein == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (!oLieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
				oLieferschein.setNGesamtwertinlieferscheinwaehrung(
						berechneGesamtwertLieferscheinAusPositionen(iIdLieferscheinI, theClientDto));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void updateLieferscheinBegruendung(Integer lieferscheinIId, Integer begruendungIId,
			TheClientDto theClientDto) {
		Lieferschein oLieferschein = em.find(Lieferschein.class, lieferscheinIId);
		oLieferschein.setBegruendungIId(begruendungIId);
		oLieferschein.setTAendern(new Timestamp(System.currentTimeMillis()));
		oLieferschein.setPersonalIIdAendern(theClientDto.getIDPersonal());
		em.merge(oLieferschein);
		em.flush();
	}

	/**
	 * Die Anzahl der Lieferscheine, die in der Umsatzuebersicht aufscheinen.
	 * 
	 * @param gcVonI       von diesem Zeitpunkt
	 * @param gcBisI       bis zu diesem Zeitpunkt
	 * @param theClientDto der aktuelle Benutzer
	 * @return Integer die Anzahl
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer zaehleLieferscheinFuerUmsatz(GregorianCalendar gcVonI, GregorianCalendar gcBisI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "zaehleLieferscheinFuerUmsatz";
		myLogger.entry();
		if (gcVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("gcVonI == null"));
		}

		if (gcBisI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("gcBisI == null"));
		}

		Integer iiAnzahl = new Integer(0);
		Query query = em.createNamedQuery("LieferscheinfindByMandantVonBisStatus1Status2Status3Status4");
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
	 * @param gcVonI       ab diesem Datum inklusive
	 * @param gcBisI       bis zu diesem Datum inklusive
	 * @param theClientDto der aktuelle Benutzer
	 * @return BigDecimal der Umsatz
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BigDecimal berechneUmsatz(GregorianCalendar gcVonI, GregorianCalendar gcBisI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneUmsatz";
		myLogger.entry();
		if (gcVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("gcVonI == null"));
		}

		if (gcBisI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("gcBisI == null"));
		}

		BigDecimal bdUmsatzO = Helper.getBigDecimalNull();
		// Schritt 1: Alle relevanten Lieferscheine besorgen
		Query query = em.createNamedQuery("LieferscheinfindByMandantVonBisStatus1Status2Status3Status4");
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

			BigDecimal bdBeitragDiesesLieferscheins = oLieferschein.getNGesamtwertinlieferscheinwaehrung();

			if (bdBeitragDiesesLieferscheins != null) {

				// den Beitrag normieren auf Mandantenwaehrung
				BigDecimal bdWechselkurs = Helper.getKehrwert(new BigDecimal(
						oLieferschein.getFWechselkursmandantwaehrungzulieferscheinwaehrung().doubleValue()));
				bdBeitragDiesesLieferscheins = bdBeitragDiesesLieferscheins.multiply(bdWechselkurs);
				bdBeitragDiesesLieferscheins = Helper.rundeKaufmaennisch(bdBeitragDiesesLieferscheins, 4);
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
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void manuellFreigeben(Integer iIdLieferscheinI, TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "manuellFreigeben";
		myLogger.entry();

		Validator.notNull(iIdLieferscheinI, "iIdLieferscheinI");
		// IMS ID 179 : Manuell freigeben bedeutet je nach Parameterwert den
		// Wechsel
		// auf 'Offen' oder 'Geliefert'

		// LP 5.0.5 Wechsel auf 'Geliefert'
		LieferscheinDto lieferschein = lieferscheinFindByPrimaryKey(iIdLieferscheinI);

		if (lieferschein.getStatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
			lieferschein.setStatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
			updateLieferscheinOhneWeitereAktion(lieferschein, theClientDto);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(
					"Lieferschein kann nicht manuell freigegeben werden, Status : " + lieferschein.getStatusCNr()));
		}
	}

	/**
	 * Ein Lieferschein kann manuell erledigt werden. <br>
	 * Der Lieferschein muss sich im Status 'Geliefert' befinden.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void manuellErledigen(Integer iIdLieferscheinI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinIId(iIdLieferscheinI);
		myLogger.logData(iIdLieferscheinI);
		Validator.notNull(iIdLieferscheinI, "iIdLieferscheinI");

		LieferscheinDto lieferschein = lieferscheinFindByPrimaryKey(iIdLieferscheinI);
		if (lieferschein.getStatusCNr().equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
			lieferschein.setStatusCNr(LieferscheinFac.LSSTATUS_ERLEDIGT);
			lieferschein.setTManuellerledigt(getTimestamp());
			lieferschein.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
			updateLieferscheinOhneWeitereAktion(lieferschein, theClientDto);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(
					"Lieferschein kann nicht manuell erledigt werden, Status : " + lieferschein.getStatusCNr()));
		}
	}

	/**
	 * Die Erledigung eines Lieferscheins kann aufgehoben werden. <br>
	 * Der Lieferschein muss sich im Status 'Erledigt' befinden.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void erledigungAufheben(Integer iIdLieferscheinI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinIId(iIdLieferscheinI);
		LieferscheinDto lieferschein = lieferscheinFindByPrimaryKey(iIdLieferscheinI);
		if (lieferschein.getStatusCNr().equals(LieferscheinFac.LSSTATUS_ERLEDIGT)) {
			if (lieferschein.getPersonalIIdManuellerledigt() != null && lieferschein.getTManuellerledigt() != null) {
				lieferschein.setStatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
				lieferschein.setTManuellerledigt(null);
				lieferschein.setPersonalIIdManuellerledigt(null);
			} else {
				// throw new EJBExceptionLP(
				// EJBExceptionLP.FEHLER_BELEG_WURDE_NICHT_MANUELL_ERLEDIGT,
				// new
				// Exception("Dieser Lieferschein wurde nicht manuell erledigt"
				// ));
				lieferschein.setStatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
				myLogger.logKritisch(
						"Status Erledigt wurde aufgehoben, obwohl der Lieferschein nicht manuell erledigt wurde, LieferscheinIId: "
								+ iIdLieferscheinI);
			}
			updateLieferschein(lieferschein, theClientDto);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception("Die Erledigung des Lieferscheins kann nicht aufgehoben werden, Status: "
							+ lieferschein.getStatusCNr()));
		}
	}

	/**
	 * Ein Lieferschein kann durch eine Rechnung verrechnet werden. <br>
	 * Der Lieferschein muss sich im Status 'Geliefert' befinden.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param iIdRechnungI     PK der verrechnenden Rechnung
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void verrechnen(Integer iIdLieferscheinI, Integer iIdRechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "verrechnen";
		myLogger.entry();
		Validator.notNull(iIdLieferscheinI, "iIdLieferscheinI");
		Validator.notNull(iIdRechnungI, "iIdRechnungI");

		LieferscheinDto lieferschein = lieferscheinFindByPrimaryKey(iIdLieferscheinI);

		if (lieferschein.getStatusCNr().equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
			lieferschein.setStatusCNr(LieferscheinFac.LSSTATUS_VERRECHNET);
			lieferschein.setRechnungIId(iIdRechnungI);
			updateLieferscheinOhneWeitereAktion(lieferschein, theClientDto);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(
					"Lieferschein kann nicht verrechnet werden, Status : " + lieferschein.getStatusCNr()));
		}
	}

	/**
	 * Einen Lieferschein stornieren. <br>
	 * Der Lieferschein bekommt den Status Storniert. <br>
	 * Die Lieferscheinposition bleiben mit ihren Werten erhalten, alle Buchungen
	 * werden rueckgaengig gemacht. <br>
	 * Der Storno eines Lieferscheins kann nicht rueckgaengig gemacht werden.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void stornieren(Integer iIdLieferscheinI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinIId(iIdLieferscheinI);

		// PJ18739
		VerkettetDto[] lsDtosVerketet = getLieferscheinServiceFac().verkettetFindByLieferscheinIId(iIdLieferscheinI);

		Query query = em.createNamedQuery("VerkettetfindByLieferscheinIIdVerkettet");
		query.setParameter(1, iIdLieferscheinI);
		Verkettet verkettet = null;
		try {
			verkettet = (Verkettet) query.getSingleResult();
		} catch (NoResultException e) {
			//
		}

		if (lsDtosVerketet.length > 0) {

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_ENTHAELT_VERKETTETE_LIEFERSCHEINE,
					new Exception("Lieferschein enthaelt verkettetet Lieferscheine"));

		}

		if (verkettet != null) {
			LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(verkettet.getLieferscheinIId());

			ArrayList alDaten = new ArrayList();
			alDaten.add(lieferscheinDto.getCNr());
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_IST_VERKETTET, alDaten,
					new Exception("Lieferschein ist verkettet is Lieferschein " + lieferscheinDto.getCNr()));

		}

		try {
			LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(iIdLieferscheinI);
			if (lieferscheinDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_GELIEFERT)
					|| lieferscheinDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
				// die Lieferscheinwerte bleiben beim Stornieren erhalten
				lieferscheinDto.setStatusCNr(LieferscheinFac.LSSTATUS_STORNIERT);

				lieferscheinDto.setTStorniert(getTimestamp());
				lieferscheinDto.setPersonalIIdStorniert(theClientDto.getIDPersonal());

				// SP3020 LS muss vorher auf Storniert gesetzt werden, damit die
				// naechste Methode mit dem gaenternet Status weiterarbeiten
				// kann
				updateLieferscheinOhneWeitereAktion(lieferscheinDto, theClientDto);

				LieferscheinpositionDto[] aLieferscheinpositionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);

				if (aLieferscheinpositionDto != null && aLieferscheinpositionDto.length > 0) {
					for (int i = 0; i < aLieferscheinpositionDto.length; i++) {
						getLieferscheinpositionFac().storniereLieferscheinposition(aLieferscheinpositionDto[i].getIId(),
								theClientDto);
					}
				}

			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(
						"Lieferschein kann nicht storniert werden, Status : " + lieferscheinDto.getStatusCNr()));

			}
		} catch (RemoteException t) {
			throwEJBExceptionLPRespectOld(t);
		}
	}

	public void stornoAufheben(Integer lieferscheinIId, TheClientDto theClientDto) {
		Lieferschein ls = em.find(Lieferschein.class, lieferscheinIId);
		if (ls.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_STORNIERT)) {
			ls.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_ANGELEGT);
			ls.setTStorniert(null);
			ls.setPersonalIIdStorniert(null);
			ls.setPersonalIIdAendern(theClientDto.getIDPersonal());
			ls.setTAendern(getTimestamp());
			ls.setNGesamtwertinlieferscheinwaehrung(null);
			ls.setNGestehungswertinmandantenwaehrung(null);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception("Storno des Lieferscheines kann nicht aufgehoben werden, Status : "
							+ ls.getLieferscheinstatusCNr()));
		}

		try {
			LieferscheinpositionDto[] aLieferscheinpositionDto = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(lieferscheinIId);

			if (aLieferscheinpositionDto != null && aLieferscheinpositionDto.length > 0) {
				for (int i = 0; i < aLieferscheinpositionDto.length; i++) {

					if (aLieferscheinpositionDto[i].getNMenge() != null) {

						Lieferscheinposition lsPos = em.find(Lieferscheinposition.class,
								aLieferscheinpositionDto[i].getIId());
						lsPos.setNMenge(Helper.getBigDecimalNull());
						em.merge(lsPos);

					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		myLogger.exit("Stornierung wurde aufgehoben");
	}

	// /**
	// * Storno eines Lieferscheins aufheben.
	// *
	// * @param iIdLieferscheinI
	// * PK des Lieferscheins
	// * @param theClientDto
	// * der aktuelle Benutzer
	// * @throws EJBExceptionLP
	// * Ausnahme
	// */
	// private void stornoAufheben(Integer iIdLieferscheinI,
	// TheClientDto theClientDto) throws EJBExceptionLP {
	// final String METHOD_NAME = "stornoAufheben";
	// myLogger.entry();
	// if (iIdLieferscheinI == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
	// new Exception("iIdLieferscheinI == null"));
	// }
	// Lieferschein oLieferschein = em.find(Lieferschein.class,
	// iIdLieferscheinI);
	// if (oLieferschein == null) {
	// throw new EJBExceptionLP(
	// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
	// }
	//
	// if (oLieferschein.getLieferscheinstatusCNr().equals(
	// LieferscheinFac.LSSTATUS_STORNIERT)) {
	// oLieferschein
	// .setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_GELIEFERT);
	// oLieferschein.setTStorniert(null);
	// oLieferschein.setPersonalIIdStorniert(null);
	// } else {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
	// new Exception(
	// "Storno des Lieferscheins kann nicht aufgehoben werden, Status : "
	// + oLieferschein.getLieferscheinstatusCNr()));
	// }
	// }

	/**
	 * Fuer die Nachkalkulation des Lieferscheins den Ist-Verkaufswert (=
	 * NettoVerkaufspreisPlusAufschlaegeMinusRabatte pro Stueck * gelieferte Menge)
	 * bezogen auf eine bestimmte Artikelart berechnen. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Lieferscheinpositionen.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param sArtikelartI     die gewuenschte Artikelart
	 * @param theClientDto     der aktuelle Benutzer
	 * @return BigDecimal der Verkaufswert der Artikelart Ist in Mandantenwaehrung
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BigDecimal berechneVerkaufswertIst(Integer iIdLieferscheinI, HashMap lieferscheinpositionIIds,
			String sArtikelartI, TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneVerkaufswertIst";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("sArtikelartI == null"));
		}

		BigDecimal bdVerkaufswertIstO = Helper.getBigDecimalNull();

		try {
			LieferscheinpositionDto[] aLieferscheinpositionDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);

			for (int i = 0; i < aLieferscheinpositionDtos.length; i++) {

				if (lieferscheinpositionIIds == null
						|| lieferscheinpositionIIds.containsKey(aLieferscheinpositionDtos[i].getIId())) {

					// alle mengenbehafteten Positionen beruecksichtigen
					if (aLieferscheinpositionDtos[i].getNMenge() != null
							&& aLieferscheinpositionDtos[i].getArtikelIId() != null) {
						ArtikelDto oArtikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(aLieferscheinpositionDtos[i].getArtikelIId(), theClientDto);

						BigDecimal bdBeitragDieserPosition = aLieferscheinpositionDtos[i].getNMenge()
								.multiply(aLieferscheinpositionDtos[i]
										.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

						// je nach Artikelart beruecksichtigen
						if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							if (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)
									|| Helper.short2boolean(oArtikelDto.getBAzinabnachkalk())) {
								bdVerkaufswertIstO = bdVerkaufswertIstO.add(bdBeitragDieserPosition);
							}
						} else {
							if (!oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)
									&& Helper.short2boolean(oArtikelDto.getBAzinabnachkalk()) == false) {
								bdVerkaufswertIstO = bdVerkaufswertIstO.add(bdBeitragDieserPosition);
							}
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Verkaufswert " + sArtikelartI + " Ist : " + bdVerkaufswertIstO.toString());

		return bdVerkaufswertIstO;
	}

	/**
	 * Fuer die Nachkalkulation des Auftrags den Ist-Gestehungswert (=
	 * Gestehungswert des Artikels auf Lager Lieferschein pro Stueck * gelieferter
	 * Menge) bezogen auf eine bestimmte Artikelart berechnen. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Lieferscheinpositionen.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param sArtikelartI     die gewuenschte Artikelart
	 * @param theClientDto     der aktuelle Benutzer
	 * @return BigDecimal der Ist-Gestehungswert der gewuenschten Artikelart in
	 *         Mandantenwaehrung
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BigDecimal berechneGestehungswertOderEinstandwertIst(Integer iIdLieferscheinI,
			HashMap lieferscheinpositionIIds, String sArtikelartI, boolean bGestehungswert, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGestehungswertIst";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("sArtikelartI == null"));
		}

		BigDecimal bdGestehungswertIstO = Helper.getBigDecimalNull();

		try {
			LieferscheinpositionDto[] aLieferscheinpositionDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);

			for (int i = 0; i < aLieferscheinpositionDtos.length; i++) {

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aLieferscheinpositionDtos[i].getNMenge() != null
						&& aLieferscheinpositionDtos[i].getArtikelIId() != null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(aLieferscheinpositionDtos[i].getArtikelIId(), theClientDto);
					if (lieferscheinpositionIIds == null
							|| lieferscheinpositionIIds.containsKey(aLieferscheinpositionDtos[i].getIId())) {
						// Grundlage ist der positionsbezogene Gestehungspreis
						// des
						// Artikels.
						BigDecimal bdGestehungswertIst = Helper.getBigDecimalNull();

						if (aLieferscheinpositionDtos[i].getLieferscheinpositionartCNr()
								.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
							if (bGestehungswert == true) {

								bdGestehungswertIst = berechneGestehungswertEinerLieferscheinposition(
										aLieferscheinpositionDtos[i], theClientDto);
							} else {
								// PJ19143
								ArrayList<WarenzugangsreferenzDto> zu = getLagerFac().getWareneingangsreferenz(

										LocaleFac.BELEGART_LIEFERSCHEIN, aLieferscheinpositionDtos[i].getIId(),
										aLieferscheinpositionDtos[i].getSeriennrChargennrMitMenge(), false,
										theClientDto);

								for (int k = 0; k < zu.size(); k++) {

									bdGestehungswertIst = bdGestehungswertIst
											.add(zu.get(k).getnEinstandspreis().multiply(zu.get(k).getMenge()));

								}
							}
						}

						// je nach Artikelart beruecksichtigen // @todo PJ 4399
						if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							if (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)
									|| Helper.short2boolean(oArtikelDto.getBAzinabnachkalk())) {
								bdGestehungswertIstO = bdGestehungswertIstO.add(bdGestehungswertIst);
							}
						} else {
							if (!oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)
									&& Helper.short2boolean(oArtikelDto.getBAzinabnachkalk()) == false) {
								bdGestehungswertIstO = bdGestehungswertIstO.add(bdGestehungswertIst);
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

		myLogger.exit("Gestehungswert " + sArtikelartI + " Ist : " + bdGestehungswertIstO.toString());

		return bdGestehungswertIstO;
	}

	private boolean isArtikelSetHead(Integer artikelIId, TheClientDto theClientDto) {
		StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
				theClientDto);
		return stklDto != null && stklDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_SETARTIKEL);
	}

	private BigDecimal getAvailableAmountArtikelset(Integer auftragpositionIId, List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			if (artikelset.getHead().getIId().equals(auftragpositionIId))
				return artikelset.getAvailableAmount();
		}

		return null;
	}

	private BelegpositionVerkaufDto findArtikelsetHead(BelegpositionVerkaufDto anyAuftragpositionDto,
			List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			BelegpositionVerkaufDto headDto = artikelset.findHeadPositionDto(anyAuftragpositionDto);
			if (null != headDto)
				return headDto;
		}

		return null;
	}

	private BigDecimal getSollsatzgroesseArtikelSet(AuftragpositionDto auftragpositionDto,
			List<Artikelset> artikelsets) {
		BelegpositionVerkaufDto headDto = findArtikelsetHead(auftragpositionDto, artikelsets);
		if (null != headDto) {
			BigDecimal headAmount = headDto.getNMenge();
			return auftragpositionDto.getNMenge().divide(headAmount);
		}

		return BigDecimal.ONE;
	}

	private List<SeriennrChargennrMitMengeDto> getAvailableSnrsArtikelset(Integer auftragpositionIId,
			List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			if (artikelset.getHead().getIId().equals(auftragpositionIId))
				return artikelset.getIdentities();
		}

		// TODO: Eigentlich(?) Ist das ein Fehler wenn ich hier kein Artikelset
		// finde!?
		return new ArrayList<SeriennrChargennrMitMengeDto>();
	}

	private Artikelset getAppropriateArtikelset(Integer auftragpositionIId, List<Artikelset> artikelsets) {
		for (Artikelset artikelset : artikelsets) {
			if (artikelset.getHead().getIId().equals(auftragpositionIId))
				return artikelset;
		}

		return null;
	}

	protected Integer uebernimmAuftragsposition(boolean bEsGibtNochPositiveOffene, LieferscheinDto lieferscheinDto,
			AuftragpositionDto auftragpositionDto, List<Artikelset> artikelsets,
			AuftragpositionDto[] auftragpositionDtos, TheClientDto theClientDto) throws RemoteException {

		if (AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT.equals(auftragpositionDto.getAuftragpositionstatusCNr()))
			return null;

		if (auftragpositionDto.getNMenge() == null) {
			LieferscheinpositionDto lieferscheinpositionDto = auftragpositionDto.cloneAsLieferscheinpositionDto();
			lieferscheinpositionDto.setLieferscheinIId(lieferscheinDto.getIId());
			lieferscheinpositionDto.setISort(null);
			getLieferscheinpositionFac().createLieferscheinposition(lieferscheinpositionDto, false, theClientDto);
			return null;
		}

		// IMS 2129
		// wenn es noch positive offene gibt, dann duerfen die
		// negativen noch nicht geliefert werden
		if (auftragpositionDto.getNMenge().signum() < 0 && bEsGibtNochPositiveOffene) {
			return null;
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

		boolean needsArtikelsetISort = false;

		if (auftragpositionDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {

			// PJ17906
			if (auftragpositionDto.getNMenge() != null && auftragpositionDto.getNMenge().doubleValue() == 0) {
				auftragpositionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);
				getAuftragpositionFac().updateOffeneMengeAuftragposition(auftragpositionDto.getIId(), theClientDto);
				return null;
			}

			bLieferscheinpositionErzeugen = true;
			nMengeFuerLieferscheinposition = auftragpositionDto.getNOffeneMenge();
		} else if (auftragpositionDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {

			// PJ17906
			if (auftragpositionDto.getNMenge() != null && auftragpositionDto.getNMenge().doubleValue() == 0) {
				auftragpositionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);
				getAuftragpositionFac().updateOffeneMengeAuftragposition(auftragpositionDto.getIId(), theClientDto);
				return auftragpositionDto.getArtikelIId();
			}

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId(),
					theClientDto);

			// nicht lagerbewirtschaftete Artikel werden mit der
			// vollen offenen Menge uebernommen sofern es nicht ein
			// Artikelset-Kopfartikel ist
			if (!artikelDto.isLagerbewirtschaftet()) {
				bLieferscheinpositionErzeugen = true;

				if (isArtikelSetHead(artikelDto.getIId(), theClientDto)) {
					BigDecimal verfuegbareMenge = getAvailableAmountArtikelset(auftragpositionDto.getIId(),
							artikelsets);
					nMengeFuerLieferscheinposition = auftragpositionDto.getNOffeneMenge().min(verfuegbareMenge);
				} else {
					nMengeFuerLieferscheinposition = auftragpositionDto.getNOffeneMenge();
				}
			} else {
				if (artikelDto.isSeriennrtragend()) {
					// seriennummerbehaftete Artikel werden nur
					// uebernommen wenn sie in einem Artikelset vorhanden sind
					if (auftragpositionDto.getPositioniIdArtikelset() != null) {
						Artikelset artikelset = getAppropriateArtikelset(auftragpositionDto.getPositioniIdArtikelset(),
								artikelsets);
						if (null != artikelset) {
							BigDecimal sollsatzgroesse = auftragpositionDto.getNMenge()
									.divide(artikelset.getHead().getNMenge());
							nMengeFuerLieferscheinposition = sollsatzgroesse.multiply(artikelset.getAvailableAmount());
							bLieferscheinpositionErzeugen = true;

							if (nMengeFuerLieferscheinposition.compareTo(auftragpositionDto.getNOffeneMenge()) > 0) {
								nMengeFuerLieferscheinposition = auftragpositionDto.getNOffeneMenge();
							}
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
					SeriennrChargennrAufLagerDto[] alleChargennummern = getLagerFac().getAllSerienChargennrAufLager(
							artikelDto.getIId(), lieferscheinDto.getLagerIId(), theClientDto, true, false);
					if (alleChargennummern != null && alleChargennummern.length == 1) {
						BigDecimal nLagerstd = alleChargennummern[0].getNMenge();
						cSerienchargennummer = alleChargennummern[0].getCSeriennrChargennr();

						if (nLagerstd.signum() > 0) {
							bLieferscheinpositionErzeugen = true;
							nMengeFuerLieferscheinposition = auftragpositionDto.getNOffeneMenge().min(nLagerstd);
						}
					}
				} else {
					// bei lagerbewirtschafteten Artikeln muss
					// die Menge auf Lager
					// beruecksichtigt werden

					BigDecimal nMengeAufLager = null;
					int bImmerAusreichendVerfuegbar = 0;
					try {
						ParametermandantDto parameterM = getParameterFac().getMandantparameter(
								theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR);
						bImmerAusreichendVerfuegbar = ((Integer) parameterM.getCWertAsObject());

					} catch (RemoteException ex) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
					}
					if (bImmerAusreichendVerfuegbar > 0) {
						nMengeAufLager = new BigDecimal(999999999);
					} else {
						nMengeAufLager = getLagerFac().getMengeAufLager(artikelDto.getIId(),
								lieferscheinDto.getLagerIId(), null, theClientDto);
					}

					if (nMengeAufLager.signum() > 0) {
						bLieferscheinpositionErzeugen = true;
						if (auftragpositionDto.getPositioniIdArtikelset() != null) {

							Artikelset artikelset = getAppropriateArtikelset(
									auftragpositionDto.getPositioniIdArtikelset(), artikelsets);
							if (null != artikelset) {
								BigDecimal sollsatzGroesse = auftragpositionDto.getNMenge()
										.divide(artikelset.getHead().getNMenge());
								nMengeFuerLieferscheinposition = auftragpositionDto.getNOffeneMenge()
										.min(artikelset.getAvailableAmount()).multiply(sollsatzGroesse)
										.min(nMengeAufLager);
							} else {
								nMengeFuerLieferscheinposition = BigDecimal.ONE;
							}
						} else {

							int iOffeneMengeVorschlagen = 0;
							try {
								ParametermandantDto parameterM = getParameterFac().getMandantparameter(
										theClientDto.getMandant(), ParameterFac.KATEGORIE_LIEFERSCHEIN,
										ParameterFac.PARAMETER_OFFENE_MENGE_IN_SICHT_AUFTRAG_VORSCHLAGEN);
								iOffeneMengeVorschlagen = ((Integer) parameterM.getCWertAsObject());

							} catch (RemoteException ex) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
							}
							// PJ21101
							if (iOffeneMengeVorschlagen == 2) {
								nMengeFuerLieferscheinposition = nMengeAufLager;
							} else {
								nMengeFuerLieferscheinposition = auftragpositionDto.getNOffeneMenge()
										.min(nMengeAufLager);
							}

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
		} else if (auftragpositionDto.isIntelligenteZwischensumme()) {
			if (!hatOffeneAuftragpositionenInZws(auftragpositionDto.getZwsVonPosition(),
					auftragpositionDto.getZwsBisPosition(), auftragpositionDtos)) {
				bLieferscheinpositionErzeugen = true;
				nMengeFuerLieferscheinposition = auftragpositionDto.getNOffeneMenge();
			}
		}

		if (bLieferscheinpositionErzeugen && nMengeFuerLieferscheinposition != null) {
			LieferscheinpositionDto lieferscheinpositionBisherDto = getLieferscheinpositionByLieferscheinAuftragposition(
					lieferscheinDto.getIId(), auftragpositionDto.getIId());

			Integer artikelIId = null;

			if (lieferscheinpositionBisherDto == null) {
				LieferscheinpositionDto lieferscheinpositionDto = auftragpositionDto.cloneAsLieferscheinpositionDto();

				lieferscheinpositionDto.setNMenge(auftragpositionDto.getNOffeneMenge());

				// SP5827
				if (lieferscheinpositionDto.getMwstsatzIId() != null
						&& lieferscheinpositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() != null) {

					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindByPrimaryKey(lieferscheinpositionDto.getMwstsatzIId(), theClientDto);
					MwstsatzDto mwstsatzDtoZumBelegdatum = getMandantFac()
							.mwstsatzFindZuDatum(mwstsatzDto.getIIMwstsatzbezId(), lieferscheinDto.getTBelegdatum());

					if (!mwstsatzDtoZumBelegdatum.getIId().equals(lieferscheinpositionDto.getMwstsatzIId())) {

						lieferscheinpositionDto.setMwstsatzIId(mwstsatzDtoZumBelegdatum.getIId());

						BigDecimal mwstBetrag = lieferscheinpositionDto
								.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
								.multiply(new BigDecimal(mwstsatzDtoZumBelegdatum.getFMwstsatz().doubleValue())
										.movePointLeft(2));
						lieferscheinpositionDto.setNMwstbetrag(mwstBetrag);
						lieferscheinpositionDto.setNBruttoeinzelpreis(mwstBetrag.add(
								lieferscheinpositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
					}
				}

				if (auftragpositionDto.isIntelligenteZwischensumme()) {
					Integer von = getAuftragpositionFac().getPositionNummer(auftragpositionDto.getZwsVonPosition());
					if (von != null) {
						lieferscheinpositionDto.setZwsVonPosition(getLieferscheinpositionFac()
								.getLSPositionIIdFromPositionNummer(lieferscheinDto.getIId(), von));
					}
					Integer bis = getAuftragpositionFac().getPositionNummer(auftragpositionDto.getZwsBisPosition());
					if (bis != null) {
						lieferscheinpositionDto.setZwsBisPosition(getLieferscheinpositionFac()
								.getLSPositionIIdFromPositionNummer(lieferscheinDto.getIId(), bis));
					}
					lieferscheinpositionDto
							.setBZwsPositionspreisDrucken(auftragpositionDto.getBZwsPositionspreisZeigen());
				}

				if (auftragpositionDto.getPositioniIdArtikelset() != null) {
					LieferscheinpositionDto[] lPositionDtos = null;
					Query query = em.createNamedQuery("LieferscheinpositionfindByAuftragposition");
					query.setParameter(1, auftragpositionDto.getPositioniIdArtikelset());
					Collection<?> cl = query.getResultList();
					lPositionDtos = assembleLieferscheinpositionDtos(cl);

					for (int j = 0; j < lPositionDtos.length; j++) {
						if (lPositionDtos[j].getLieferscheinIId().equals(lieferscheinDto.getIId())) {
							lieferscheinpositionDto.setPositioniIdArtikelset(lPositionDtos[j].getIId());
							break;
						}
					}

					getBelegVerkaufFac().setupPositionWithIdentities(lieferscheinpositionDto,
							getAvailableSnrsArtikelset(auftragpositionDto.getPositioniIdArtikelset(), artikelsets),
							theClientDto);

					cSerienchargennummer = null;
				}

				lieferscheinpositionDto.setLieferscheinIId(lieferscheinDto.getIId());

				// TODO: Problematisch bei seriennummern-artikeln (artikelset?)
				lieferscheinpositionDto.setNMenge(nMengeFuerLieferscheinposition);

				if (null != cSerienchargennummer) {
					lieferscheinpositionDto.setSeriennrChargennrMitMenge(SeriennrChargennrMitMengeDto
							.erstelleDtoAusEinerChargennummer(cSerienchargennummer, nMengeFuerLieferscheinposition));
				}
				lieferscheinpositionDto.setISort(null);

				artikelIId = lieferscheinpositionDto.getArtikelIId();

				Integer lieferscheinpositionIId = getLieferscheinpositionFac()
						.createLieferscheinposition(lieferscheinpositionDto, false, theClientDto);

				// PJ22082

				if (auftragpositionDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
					KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(lieferscheinDto.getKundeIIdRechnungsadresse(),
							theClientDto);
					if (Helper.short2boolean(kdDto.getBVkpreisAnhandLSDatum())) {
						lieferscheinpositionDto = getLieferscheinpositionFac()
								.lieferscheinpositionFindByPrimaryKey(lieferscheinpositionIId, theClientDto);
						lieferscheinpositionDto = (LieferscheinpositionDto) befuellePreisfelderAnhandVKPreisfindung(
								lieferscheinpositionDto, lieferscheinDto.getTBelegdatum(), kdDto.getIId(),
								lieferscheinDto.getWaehrungCNr(), theClientDto);
						getLieferscheinpositionFac()
								.updateLieferscheinpositionOhneWeitereAktion(lieferscheinpositionDto, theClientDto);
					}
				}

			} else {
				lieferscheinpositionBisherDto.setNMenge(nMengeFuerLieferscheinposition);

				artikelIId = lieferscheinpositionBisherDto.getArtikelIId();

				if (auftragpositionDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId(),
							theClientDto);

					if (artikelDto.isChargennrtragend() || artikelDto.isSeriennrtragend()) {

						if (auftragpositionDto.getPositioniIdArtikelset() != null) {
							List<SeriennrChargennrMitMengeDto> oldEntries = lieferscheinpositionBisherDto
									.getSeriennrChargennrMitMenge();
							lieferscheinpositionBisherDto
									.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
							getBelegVerkaufFac().setupPositionWithIdentities(lieferscheinpositionBisherDto,
									getAvailableSnrsArtikelset(auftragpositionDto.getPositioniIdArtikelset(),
											artikelsets),
									theClientDto);
							oldEntries.addAll(lieferscheinpositionBisherDto.getSeriennrChargennrMitMenge());
							lieferscheinpositionBisherDto.setSeriennrChargennrMitMenge(oldEntries);

							if (lieferscheinpositionBisherDto.getPositioniIdArtikelset() == null) {
								// Wurde wohl direkt in Sicht Auftrag hinzugefuegt, hat (noch) keine SetInfo
								LieferscheinpositionDto lsKopfDto = getLieferscheinpositionFac()
										.lieferscheinpositionKopfFindByLieferscheinIdAuftragpositionId(
												lieferscheinpositionBisherDto.getLieferscheinIId(),
												auftragpositionDto.getIId(), theClientDto);
								if (lsKopfDto != null) {
									lieferscheinpositionBisherDto.setPositioniIdArtikelset(lsKopfDto.getIId());
									needsArtikelsetISort = true;
								}
							}
						} else {
							lieferscheinpositionBisherDto.setSeriennrChargennrMitMenge(
									SeriennrChargennrMitMengeDto.erstelleDtoAusEinerChargennummer(cSerienchargennummer,
											nMengeFuerLieferscheinposition));
						}
					} else {
						lieferscheinpositionBisherDto.setSeriennrChargennrMitMenge(null);
					}

				}

				getLieferscheinpositionFac().updateLieferscheinpositionSichtAuftrag(lieferscheinpositionBisherDto,
						theClientDto);

				if (needsArtikelsetISort) {
					ArtikelsetISortRenumber renum = new ArtikelsetISortRenumber(em, Lieferscheinposition.class);
					renum.renumber(lieferscheinpositionBisherDto.getLieferscheinIId(),
							lieferscheinpositionBisherDto.getPositioniIdArtikelset());
				}
			}

			return artikelIId;
		}

		return null;
	}

	private boolean hatOffeneAuftragpositionenInZws(Integer fromIId, Integer toIId, AuftragpositionDto[] positionDtos)
			throws RemoteException {
		int fromIndex = -1;
		for (int i = 0; fromIndex == -1 && i < positionDtos.length; i++) {
			if (positionDtos[i].getIId().equals(fromIId)) {
				fromIndex = i;
			}
		}
		if (fromIndex == -1)
			return true;

		int toIndex = -1;
		for (int i = fromIndex; toIndex == -1 && i < positionDtos.length; i++) {
			if (positionDtos[i].getIId().equals(toIId)) {
				toIndex = i;
			}
		}
		if (toIndex == -1)
			return true;

		// Erneutes Lesen ist Absicht, da liefern die Menge geaendert hat!
		for (int i = fromIndex; i <= toIndex; i++) {
			AuftragpositionDto positionDto = getAuftragpositionFac()
					.auftragpositionFindByPrimaryKey(positionDtos[i].getIId());
			if (isAuftragPositionStatusErledigt(positionDto))
				continue;

			// Nicht mengenbehaftete Positionen (Texteingabe, ...bausteine, ...)
			// ignorieren
			if (positionDto.getNMenge() == null)
				continue;
		}

		return false;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void verrechneGelieferteLieferscheine(List<Integer> kundeIIdRechnungsadresse,
			java.sql.Date tBisInclBelegdatum, java.sql.Date neuDatum, TheClientDto theClientDto) {

		List<Integer> kundeIIds_Rechnungadresse = new ArrayList();
		if (kundeIIdRechnungsadresse == null) {
			kundeIIds_Rechnungadresse = getRechnungsadressenGelieferterLieferscheine(tBisInclBelegdatum, theClientDto);
		} else {
			kundeIIds_Rechnungadresse.addAll(kundeIIdRechnungsadresse);
		}

		tBisInclBelegdatum = Helper.addiereTageZuDatum(tBisInclBelegdatum, 1);

		Iterator it = kundeIIds_Rechnungadresse.iterator();

		int iMaxRechnungswert = -1;
		try {
			ParametermandantDto parameterM = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_MAXIMALER_NETTO_RECHNUNGSWERT);
			iMaxRechnungswert = ((Integer) parameterM.getCWertAsObject());

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		if (iMaxRechnungswert == -1) {
			iMaxRechnungswert = 999999999;
		}

		while (it.hasNext()) {
			Integer kundeIId_local = (Integer) it.next();

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIId_local, theClientDto);

			if (Helper.short2boolean(kdDto.getBRechnungJeLieferadresse())) {

				Session sessionLieferadressen = FLRSessionFactory.getFactory().openSession();
				String sQueryLieferadressen = "SELECT distinct ls.kunde_i_id_lieferadresse FROM FLRLieferschein AS ls WHERE ls.flrkunderechnungsadresse.b_monatsrechnung=0 AND ls.kunde_i_id_rechnungsadresse="
						+ kundeIId_local + " AND ls.b_verrechenbar=1 AND ls.lieferscheinart_c_nr<>'"
						+ LieferscheinFac.LSART_LIEFERANT + "' AND ls.lieferscheinstatus_status_c_nr='"
						+ LocaleFac.STATUS_GELIEFERT + "' AND ls.mandant_c_nr='" + theClientDto.getMandant()
						+ "' AND ls.d_belegdatum<'" + Helper.formatDateWithSlashes(tBisInclBelegdatum) + "'";

				org.hibernate.Query lieferscheinQueryLieferadressen = sessionLieferadressen
						.createQuery(sQueryLieferadressen);

				List resultListLieferadressen = lieferscheinQueryLieferadressen.list();

				Iterator itLSLieferadressen = resultListLieferadressen.iterator();
				while (itLSLieferadressen.hasNext()) {
					Integer kundeIIdLieferadresse = (Integer) itLSLieferadressen.next();

					verrechnen(tBisInclBelegdatum, neuDatum, theClientDto, iMaxRechnungswert, kundeIId_local,
							kundeIIdLieferadresse, kdDto);

				}

				sessionLieferadressen.close();

			} else {
				verrechnen(tBisInclBelegdatum, neuDatum, theClientDto, iMaxRechnungswert, kundeIId_local, null, kdDto);
			}

		}

	}

	private void verrechnen(java.sql.Date tBisInclBelegdatum, java.sql.Date neuDatum, TheClientDto theClientDto,
			int iMaxRechnungswert, Integer kundeIId_local, Integer kundeIIdLieferadresse, KundeDto kdDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT ls FROM FLRLieferschein AS ls WHERE ls.flrkunderechnungsadresse.b_monatsrechnung=0 AND ls.kunde_i_id_rechnungsadresse="
				+ kundeIId_local;

		if (kundeIIdLieferadresse != null) {
			sQuery += " AND ls.kunde_i_id_lieferadresse=" + kundeIIdLieferadresse + " ";
		}

		sQuery += " AND ls.b_verrechenbar=1 AND ls.lieferscheinart_c_nr<>'" + LieferscheinFac.LSART_LIEFERANT
				+ "' AND ls.lieferscheinstatus_status_c_nr='" + LocaleFac.STATUS_GELIEFERT + "' AND ls.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND ls.d_belegdatum<'"
				+ Helper.formatDateWithSlashes(tBisInclBelegdatum) + "' ORDER BY ls.c_nr ASC";

		org.hibernate.Query lieferscheinQuery = session.createQuery(sQuery);

		List resultList = lieferscheinQuery.list();

		Iterator itLS = resultList.iterator();

		int iMaxRepos = 99999999;
		if (kdDto.getIMaxRepos() != null) {
			iMaxRepos = kdDto.getIMaxRepos();
		}

		BigDecimal bdAktuellerWert = BigDecimal.ZERO;
		int iAktuelleAnzahlPositionen = 0;
		ArrayList<ArrayList<Integer>> alLieferscheine = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> alLieferscheineTemp = new ArrayList<Integer>();

		while (itLS.hasNext()) {
			FLRLieferschein ls = (FLRLieferschein) itLS.next();

			int iAnzahlLsPos = 0;
			Set s = ls.getFlrlieferscheinpositionen();
			Iterator itPos = s.iterator();

			while (itPos.hasNext()) {
				FLRLieferscheinposition pos = (FLRLieferscheinposition) itPos.next();
				if (pos.getN_menge() != null) {
					iAnzahlLsPos++;
				}
			}

			BigDecimal lsWert = BigDecimal.ZERO;
			if (ls.getN_gesamtwertinlieferscheinwaehrung() != null) {
				lsWert = ls.getN_gesamtwertinlieferscheinwaehrung();
			}

			if (iAnzahlLsPos >= iMaxRepos || lsWert.doubleValue() > iMaxRechnungswert) {

				ArrayList<Integer> alEinzelLs = new ArrayList<Integer>();
				alEinzelLs.add(ls.getI_id());
				alLieferscheine.add(alEinzelLs);
			} else {

				if (bdAktuellerWert.add(lsWert).doubleValue() >= iMaxRechnungswert
						|| (iAktuelleAnzahlPositionen + iAnzahlLsPos) > iMaxRepos) {
					alLieferscheine.add(alLieferscheineTemp);
					alLieferscheineTemp = new ArrayList<Integer>();

					if (bdAktuellerWert.add(lsWert).doubleValue() >= iMaxRechnungswert) {
						bdAktuellerWert = BigDecimal.ZERO;
					}

					if ((iAktuelleAnzahlPositionen + iAnzahlLsPos) > iMaxRepos) {
						iAktuelleAnzahlPositionen = 0;
					}

					alLieferscheineTemp.add(ls.getI_id());
				} else {
					// Sammeln
					alLieferscheineTemp.add(ls.getI_id());
				}

				bdAktuellerWert = bdAktuellerWert.add(lsWert);
				iAktuelleAnzahlPositionen = iAktuelleAnzahlPositionen + iAnzahlLsPos;
			}

		}

		if (alLieferscheineTemp.size() > 0) {
			alLieferscheine.add(alLieferscheineTemp);
		}

		if (alLieferscheine.size() > 0) {
			Iterator itTemp = alLieferscheine.iterator();
			while (itTemp.hasNext()) {
				ArrayList<Integer> lieferscheineIIds = (ArrayList<Integer>) itTemp.next();

				try {
					getRechnungFac().createRechnungAusMehrereLieferscheine(lieferscheineIIds.toArray(), null,
							RechnungFac.RECHNUNGTYP_RECHNUNG, neuDatum, theClientDto);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}
	}

	public List<Integer> getRechnungsadressenGelieferterLieferscheine(java.sql.Date tBisInclBelegdatum,
			TheClientDto theClientDto) {

		tBisInclBelegdatum = Helper.addiereTageZuDatum(tBisInclBelegdatum, 1);

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT DISTINCT ls.kunde_i_id_rechnungsadresse "
				+ " FROM FLRLieferschein AS ls WHERE ls.flrkunderechnungsadresse.b_monatsrechnung=0 AND ls.b_verrechenbar=1 AND ls.lieferscheinart_c_nr<>'"
				+ LieferscheinFac.LSART_LIEFERANT + "' AND ls.lieferscheinstatus_status_c_nr='"
				+ LocaleFac.STATUS_GELIEFERT + "' AND ls.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND ls.d_belegdatum<'" + Helper.formatDateWithSlashes(tBisInclBelegdatum) + "'";

		org.hibernate.Query lieferscheinIdsQuery = session.createQuery(sQuery);

		List<Integer> resultList = new ArrayList<Integer>();
		List<Integer> queryList = (List<Integer>) lieferscheinIdsQuery.list();
		for (Integer lieferschein_i_id : queryList) {
			Lieferschein lieferschein = em.find(Lieferschein.class, lieferschein_i_id);
			if (theClientDto.getMandant().equals(lieferschein.getMandantCNr())) {
				resultList.add(lieferschein_i_id);
			}
		}

		session.close();
		return resultList;

	}

	private boolean isAuftragPositionStatusErledigt(AuftragpositionDto auftragpositionDto) {
		return AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
				.equals(auftragpositionDto.getAuftragpositionstatusCNr());
	}

	private boolean gibtEsPositivOffeneAuftragsMengen(AuftragpositionDto[] auftragpositionen) {
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

	public boolean isAuftragpositionenMengeVorhanden(AuftragpositionDto[] auftragpositionenDto, Integer lagerIId,
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

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId(),
						theClientDto);

				if (!artikelDto.isLagerbewirtschaftet())
					continue;

				BigDecimal verfuegbareMenge = getLagerFac().getLagerstand(auftragpositionDto.getArtikelIId(), lagerIId,
						theClientDto);

				if (verfuegbareMenge.compareTo(auftragpositionDto.getNOffeneMenge()) < 0)
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
	 * <li>Artikelpositionen mit Chargennummer werden mit jener Menge uebernommen,
	 * die auf Lager liegt, wenn es genau eine Charge gibt.
	 * </ul>
	 * Die restlichen Positionen koennen nicht automatisch uebernommen werden.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param auftragIIdI      Integer
	 * @param theClientDto     String der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Set<Integer> uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(Integer iIdLieferscheinI,
			Integer auftragIIdI, List<Artikelset> artikelsets, List<Integer> auftragspositionIIds,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinIId(iIdLieferscheinI);

		LinkedHashSet alUebernommeneArtikel = new LinkedHashSet<Integer>();

		try {
			LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(iIdLieferscheinI);
			AuftragpositionDto[] aAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragIIdI);

			boolean bEsGibtNochPositiveOffene = false;
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_VERLEIH,
					theClientDto)) {
				for (int i = 0; i < aAuftragpositionDto.length; i++) {
					if (!AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
							.equals(aAuftragpositionDto[i].getAuftragpositionstatusCNr())) {
						if (aAuftragpositionDto[i].getNMenge() != null
								&& aAuftragpositionDto[i].getNMenge().doubleValue() > 0) {
							bEsGibtNochPositiveOffene = true;
						}
					}
				}
			}

			for (int i = 0; i < aAuftragpositionDto.length; i++) {

				if (auftragspositionIIds != null && auftragspositionIIds.size() > 0) {
					if (!auftragspositionIIds.contains(aAuftragpositionDto[i].getIId())) {
						continue;
					}
				}

				Integer artikelIId = uebernimmAuftragsposition(bEsGibtNochPositiveOffene, lieferscheinDto,
						aAuftragpositionDto[i], artikelsets, aAuftragpositionDto, theClientDto);

				if (artikelIId != null) {
					alUebernommeneArtikel.add(artikelIId);
				}

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return alUebernommeneArtikel;
	}

	/**
	 * Einen bestehenden Lieferschein ohne weitere Aktion aktualisieren. <br>
	 * Ein eventuelle Bezug zu Auftraegen bleibt dabei unberuecksichtig.
	 * 
	 * @param lieferscheinDtoI der aktuelle Lieferschein
	 * @param theClientDto     der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateLieferscheinOhneWeitereAktion(LieferscheinDto lieferscheinDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		lieferscheinDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
		lieferscheinDtoI.setTAendern(getTimestamp());

		// try {
		Lieferschein lieferschein = em.find(Lieferschein.class, lieferscheinDtoI.getIId());
		if (lieferschein == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lieferschein.setTAendern(getTimestamp());
		lieferschein.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// SP5025
		if (!lieferschein.getLieferscheinstatusCNr().equals(lieferscheinDtoI.getStatusCNr())
				&& LieferscheinFac.LSSTATUS_VERRECHNET.equals(lieferschein.getLieferscheinstatusCNr())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, "Lieferschein ist bereits auf 'Verrechnet'");
		}
		setLieferscheinFromLieferscheinDto(lieferschein, lieferscheinDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void updateLieferscheinVersandinfos(LieferscheinDto lieferscheinDtoI, TheClientDto theClientDto) {
		lieferscheinDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
		lieferscheinDtoI.setTAendern(getTimestamp());

		Lieferschein lieferschein = em.find(Lieferschein.class, lieferscheinDtoI.getIId());
		if (lieferschein == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lieferschein.setTAendern(getTimestamp());
		lieferschein.setPersonalIIdAendern(theClientDto.getIDPersonal());

		lieferschein.setCVersandnummer(lieferscheinDtoI.getCVersandnummer());
		lieferschein.setCVersandnummer2(lieferscheinDtoI.getCVersandnummer2());

		lieferschein.setFGewichtlieferung(lieferscheinDtoI.getFGewichtLieferung());
		lieferschein.setIAnzahlpakete(lieferscheinDtoI.getIAnzahlPakete());

		lieferschein.setTLiefertermin(lieferscheinDtoI.getTLiefertermin());

		em.merge(lieferschein);
		em.flush();

	}

	public void setzeVersandzeitpunktAufJetzt(Integer lieferscheinIId, String druckart) {
		if (lieferscheinIId != null) {
			Lieferschein lieferschein = em.find(Lieferschein.class, lieferscheinIId);
			// PJ17915
			if (!lieferschein.getLieferscheinstatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {

				lieferschein.setTVersandzeitpunkt(new Timestamp(System.currentTimeMillis()));
				lieferschein.setCVersandtype(druckart);

				// PJ19078 Erster echter Druck setzt Auslieferdatum
				if (lieferschein.getTLiefertermin() == null) {
					lieferschein.setTLiefertermin(lieferschein.getTVersandzeitpunkt());
				}

				em.merge(lieferschein);
				em.flush();
			}
		}
	}

	public class LieferscheinAvisoProducerCC extends CleverCureProducer implements ILieferscheinAvisoProducer {
		public final static String GENERATOR_INFO = "2.4";

		private Integer versandwegId;
		private Integer receiverKundeId;

		public LieferscheinAvisoProducerCC(Integer versandwegId, Integer receiverKundeId) {
			this.versandwegId = versandwegId;
			this.receiverKundeId = receiverKundeId;
		}

		@Override
		public Integer versandwegId() {
			return versandwegId;
		}

		@Override
		public Integer receiverKundeId() {
			return receiverKundeId;
		}

		@Override
		public boolean isDummy() {
			return false;
		}

		@Override
		public ILieferscheinAviso createAviso(LieferscheinDto lieferscheinDto, TheClientDto theClientDto)
				throws RemoteException, NamingException, EJBExceptionLP {
			LieferscheinAvisoCC aviso = new LieferscheinAvisoCC();
			XMLXMLDISPATCHNOTIFICATION dn = createDn(aviso, lieferscheinDto, theClientDto);
			aviso.setNotification(dn);
			aviso.setLieferscheinDto(lieferscheinDto);
			return aviso;
		}

		@Override
		public String toString(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			return aviso instanceof LieferscheinAvisoCC ? fromXml((LieferscheinAvisoCC) aviso) : null;
		}

		@Override
		public void archiveAviso(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			LieferscheinDto lsDto = aviso.getLieferscheinDto();
			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);

			JCRDocDto jcrDocDto = new JCRDocDto();
			DocPath dp = new DocPath(new DocNodeLieferschein(lsDto)).add(new DocNodeFile("Lieferaviso_Clevercure.xml"));
			jcrDocDto.setDocPath(dp);
//			jcrDocDto.setbData(aviso.toString().getBytes());
			jcrDocDto.setbData(toString(aviso, theClientDto).getBytes());
			jcrDocDto.setbVersteckt(false);
			jcrDocDto.setlAnleger(partnerDto.getIId());

			Integer kundeId = lsDto.getKundeIIdLieferadresse();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

			jcrDocDto.setlPartner(kundeDto.getPartnerIId());
			jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
			jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
			jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
			jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
			jcrDocDto.setsBelegnummer(lsDto.getCNr().replace("/", "."));
			jcrDocDto.setsFilename(lsDto.getCNr().replace("/", "."));
			jcrDocDto.setsMIME(".xml");
			jcrDocDto.setsName("Lieferaviso Clevercure" + lsDto.getIId());
			jcrDocDto.setsRow(lsDto.getIId().toString());
			jcrDocDto.setsTable("LIEFERSCHEIN");
			String sSchlagworte = "Export Clevercure XML Dispatchnotification Aviso dnd";
			jcrDocDto.setsSchlagworte(sSchlagworte);
			getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, theClientDto);
		}

		private HttpURLConnection buildUrlconnectionPost(ILieferscheinAviso aviso, TheClientDto theClientDto)
				throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException,
				KeyManagementException, UnrecoverableKeyException {
			// IVersandwegPartnerDto versandwegPartnerDto = getSystemFac()
			// .versandwegPartnerFindByPrimaryKey(aviso.getVersandwegId(),
			// aviso.getPartnerId());
			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac().versandwegPartnerFindByVersandwegIdPartnerId(
					aviso.getVersandwegId(), aviso.getPartnerId(), theClientDto.getMandant());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						aviso.getPartnerId().toString());
			}
			VersandwegPartnerCCDto ccPartnerDto = (VersandwegPartnerCCDto) versandwegPartnerDto;

			String uri = getCCEndpunkt(em, ccPartnerDto);
			uri += "&datatype=dnd";
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
		public void postAviso(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			if (!(aviso instanceof LieferscheinAvisoCC))
				return;

			try {
				HttpURLConnection urlConnection = buildUrlconnectionPost(aviso, theClientDto);
				urlConnection.setRequestProperty("Content-Type", "text/xml");
				OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
				String xmlDndContent = toString(aviso, theClientDto);
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
				String theMessage = "Lieferschein Aviso post: Status:'" + lastResponseCode + "' content-type: "
						+ lastContentType + " for: '" + theContent + "'.";
				myLogger.info(theMessage);
				// System.out.println("response: " + lastResponseCode
				// + " content-type: " + lastContentType + " for: "
				// + theContent);
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

		private String fromXml(LieferscheinAvisoCC aviso) {
			StringWriter writer = new StringWriter();
			try {
				JAXBContext context = JAXBContext
						.newInstance(aviso.getNotification().getClass().getPackage().getName());
				Marshaller m = context.createMarshaller();
				m.marshal(aviso.getNotification(), writer);
				String s = writer.toString();
				return s;
			} catch (JAXBException e) {
				System.out.println("JAXBException" + e.getMessage());
			}

			return null;
		}

		private XMLXMLDISPATCHNOTIFICATION createDn(ILieferscheinAviso aviso, LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			XMLXMLDISPATCHNOTIFICATION dn = new XMLXMLDISPATCHNOTIFICATION();
			XMLXMLDISPATCHNOTIFICATIONHEADER dnHeader = new XMLXMLDISPATCHNOTIFICATIONHEADER();
			XMLXMLCONTROLINFO controlInfo = new XMLXMLCONTROLINFO();
			controlInfo.setGENERATORINFO(GENERATOR_INFO);
			controlInfo.setGENERATIONDATE(formatAsIso8601Timestamp(GregorianCalendar.getInstance().getTime()));
			dnHeader.setCONTROLINFO(controlInfo);

			Collection<LieferscheinpositionDto> positionDtos = getLSPositionsDto(lieferscheinDto, theClientDto);

			XMLXMLDISPATCHNOTIFICATIONINFO dnInfo = buildDnInfo(aviso, lieferscheinDto, theClientDto);
			dnHeader.setDISPATCHNOTIFICATIONINFO(dnInfo);
			dn.setDISPATCHNOTIFICATIONHEADER(dnHeader);

			XMLXMLDISPATCHNOTIFICATIONITEMLIST dnItemList = buildDnItemList(lieferscheinDto, positionDtos,
					theClientDto);
			dn.setDISPATCHNOTIFICATIONITEMLIST(dnItemList);

			XMLXMLDISPATCHNOTIFICATIONSUMMARY dnSummary = buildDnSummary(lieferscheinDto, positionDtos, theClientDto);
			dn.setDISPATCHNOTIFICATIONSUMMARY(dnSummary);

			return dn;
		}

		private Collection<LieferscheinpositionDto> getLSPositionsDto(LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			Collection<LieferscheinpositionDto> ejbPositions = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(lieferscheinDto.getIId(), theClientDto);
			CollectionUtils.filter(ejbPositions, new LieferscheinpositionenNurIdentFilter());
			return ejbPositions;
		}

		private Timestamp calculateAvisoDate(Integer lieferadresseKundeId, Timestamp lieferDate) {
			Kunde kunde = em.find(Kunde.class, lieferadresseKundeId);
			Integer kundeDauer = kunde.getILieferdauer();
			Integer avisoDauer = getParameterFac().getLieferavisoTageZusaetzlich(kunde.getMandantCNr());
			Integer days = kundeDauer + avisoDauer;
			if (days < 0) {
				myLogger.warn("Lieferavisodauer < 0 (Lieferdauer Kunde:" + kundeDauer + ", paramzusaetzlich:"
						+ avisoDauer + ") auf 0 gesetzt.");
				days = 0;
			}
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(lieferDate.getTime());
			c.add(Calendar.DAY_OF_MONTH, days);
			return new Timestamp(c.getTimeInMillis());
		}

		private XMLXMLDISPATCHNOTIFICATIONINFO buildDnInfo(ILieferscheinAviso aviso, LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			XMLXMLDISPATCHNOTIFICATIONINFO dnInfo = new XMLXMLDISPATCHNOTIFICATIONINFO();
			dnInfo.setCCROWID(
					lieferscheinDto.getIId() + "_" + lieferscheinDto.getCNr() + "_" + lieferscheinDto.getTAendern());

			dnInfo.setDISPATCHNOTIFICATIONID(lieferscheinDto.getCNr());

			Timestamp t = lieferscheinDto.getTLiefertermin();
			if (t == null) {
				t = setzeAuslieferdatumAufJetzt(lieferscheinDto.getIId());
				lieferscheinDto.setTLiefertermin(t);
			}
//			t = calculateAvisoDate(lieferscheinDto.getKundeIIdLieferadresse(), t);

			dnInfo.setDISPATCHNOTIFICATIONDATE(formatAsIso8601Timestamp(new Date(t.getTime())));

			if (!HelperWebshop.isEmptyString(lieferscheinDto.getCBezProjektbezeichnung())) {
				XMLXMLREMARK dnRemark = new XMLXMLREMARK();
				dnRemark.setValue(lieferscheinDto.getCBezProjektbezeichnung());
				dnInfo.getREMARK().add(dnRemark);
			}

			XMLXMLBUYERPARTY buyerParty = new XMLXMLBUYERPARTY();
			XMLXMLPARTY xmlParty = new XMLXMLPARTY();
			XMLXMLPARTYID xmlPartyId = new XMLXMLPARTYID();
			// xmlPartyId.setValue(
			// getLieferantennummer(
			// lieferscheinDto.getKundeIIdRechnungsadresse(),
			// theClientDto));
			xmlPartyId.setValue(getLieferantennummer(lieferscheinDto, theClientDto));
			xmlParty.setPARTYID(xmlPartyId);
			buyerParty.setPARTY(xmlParty);
			dnInfo.setBUYERPARTY(buyerParty);

			XMLXMLSUPPLIERPARTY supplierParty = new XMLXMLSUPPLIERPARTY();
			XMLXMLPARTY xmlSupplierParty = new XMLXMLPARTY();
			XMLXMLPARTYID xmlSupplierPartyId = new XMLXMLPARTYID();
			xmlSupplierPartyId.setValue(getSupplierPartyId(aviso, lieferscheinDto, theClientDto));
			xmlSupplierParty.setPARTYID(xmlSupplierPartyId);
			supplierParty.setPARTY(xmlSupplierParty);
			dnInfo.setSUPPLIERPARTY(supplierParty);

			return dnInfo;
		}

		private String getSupplierPartyId(ILieferscheinAviso aviso, LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			if (versandwegId() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_NICHT_UNTERSTUETZT, "");
			}

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(receiverKundeId(), theClientDto);
			aviso.setVersandwegId(versandwegId());
			aviso.setPartnerId(kundeDto.getPartnerIId());

			// IVersandwegPartnerDto versandwegPartnerDto = getSystemFac()
			// .versandwegPartnerFindByPrimaryKey(versandwegId,
			// aviso.getPartnerId());
			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac().versandwegPartnerFindByVersandwegIdPartnerId(
					versandwegId(), aviso.getPartnerId(), theClientDto.getMandant());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						kundeDto.getPartnerIId().toString());
			}
			VersandwegPartnerCCDto partnerDto = (VersandwegPartnerCCDto) versandwegPartnerDto;
			String supplierId = partnerDto.getCKundennummer();
			return supplierId.trim();
		}

		private XMLXMLDISPATCHNOTIFICATIONITEMLIST buildDnItemList(LieferscheinDto lieferscheinDto,
				Collection<LieferscheinpositionDto> ejbPositions, TheClientDto theClientDto) throws RemoteException {
			XMLXMLDISPATCHNOTIFICATIONITEMLIST itemlist = new XMLXMLDISPATCHNOTIFICATIONITEMLIST();

			Timestamp tAvisoDate = calculateAvisoDate(lieferscheinDto.getKundeIIdLieferadresse(),
					lieferscheinDto.getTLiefertermin());

			Integer lineitemId = 0;
			for (LieferscheinpositionDto lieferscheinpositionDto : ejbPositions) {
				XMLXMLDISPATCHNOTIFICATIONITEM dnItem = new XMLXMLDISPATCHNOTIFICATIONITEM();
				dnItem.setCCROWID(lieferscheinpositionDto.getIId().toString());
				dnItem.setLINEITEMID((++lineitemId).toString());
				dnItem.setCCORDERUNIT(lieferscheinpositionDto.getEinheitCNr().trim());

				ArtikelDto itemDto = getArtikelFac().artikelFindByPrimaryKey(lieferscheinpositionDto.getArtikelIId(),
						theClientDto);
				dnItem.setCCORIGINCOUNTRY(buildUrsprungsland(itemDto));

				XMLXMLARTICLEID itemId = new XMLXMLARTICLEID();
				itemId.setSUPPLIERAID(buildItemCnr(itemDto));
				if (itemDto.getArtikelsprDto() != null) {
					itemId.setDESCRIPTIONSHORT(
							HelperWebshop.isEmptyString(itemDto.getArtikelsprDto().getCKbez()) ? itemDto.getCNr()
									: itemDto.getArtikelsprDto().getCKbez());
				} else {
					itemId.setDESCRIPTIONSHORT(itemDto.getCNr());
				}
				KundesokoDto[] sokos = getKundesokoFac().kundesokoFindByKundeIIdArtikelIId(
						lieferscheinDto.getKundeIIdLieferadresse(), itemDto.getIId());
				if (sokos != null && sokos.length > 0) {
					XMLXMLBUYERAID buyerItemId = new XMLXMLBUYERAID();
					buyerItemId.setValue(sokos[0].getCKundeartikelnummer());
					buyerItemId.setType("buyer_specific");
					List<XMLXMLBUYERAID> buyerItemlist = itemId.getBUYERAID();
					buyerItemlist.add(buyerItemId);
				}

				dnItem.setARTICLEID(itemId);
				dnItem.setQUANTITY(ccScaled3(lieferscheinpositionDto.getNMenge()));

				buildOrderReference(dnItem, lieferscheinpositionDto);
				buildDeliveryInfo(dnItem, lieferscheinDto, lieferscheinpositionDto, tAvisoDate);

				itemlist.getDISPATCHNOTIFICATIONITEM().add(dnItem);
			}

			return itemlist;
		}

		private String getOrderReference(AuftragpositionDto auftragpositionDto) throws RemoteException {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKeyOhneExc(auftragpositionDto.getBelegIId());
			if (auftragDto == null)
				return "";

			return auftragDto.getCBestellnummer();
		}

		private void buildOrderReference(XMLXMLDISPATCHNOTIFICATIONITEM dnItem, LieferscheinpositionDto lsposDto)
				throws RemoteException {
			if (lsposDto.getAuftragpositionIId() != null) {
				XMLXMLORDERREFERENCE orderReference = new XMLXMLORDERREFERENCE();

				AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKeyOhneExc(lsposDto.getAuftragpositionIId());
				if (auftragpositionDto != null) {
					orderReference.setLINEITEMID(extractLineItemIdRef(auftragpositionDto));
					orderReference.setORDERID(getOrderReference(auftragpositionDto));
					dnItem.setORDERREFERENCE(orderReference);
				}
			}
		}

		private void buildDeliveryInfo(XMLXMLDISPATCHNOTIFICATIONITEM dnItem, LieferscheinDto lieferscheinDto,
				LieferscheinpositionDto lsposDto, Timestamp avisoDate) throws RemoteException {
			Calendar c = GregorianCalendar.getInstance();
//			c.setTimeInMillis(lieferscheinDto.getTLiefertermin().getTime());
			c.setTimeInMillis(avisoDate.getTime());
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			String theDate = formatAsIso8601Timestamp(c.getTime());

			XMLXMLDELIVERYDATE deliveryDate = new XMLXMLDELIVERYDATE();
			deliveryDate.setDELIVERYSTARTDATE(theDate);
			deliveryDate.setDELIVERYENDDATE(theDate);
			dnItem.setDELIVERYDATE(deliveryDate);
		}

		private String buildUrsprungsland(ArtikelDto itemDto) {
			if (itemDto.getLandIIdUrsprungsland() == null) {
				return "";
			}
			Integer landId = itemDto.getLandIIdUrsprungsland();
			LandDto landDto = getSystemFac().landFindByPrimaryKey(landId);
			return landDto.getCLkz();
		}

		private String buildItemCnr(ArtikelDto itemDto) {
			String cnr = itemDto.getCNr();
			// if(itemDto.getCRevision() != null &&
			// itemDto.getCRevision().trim().length() > 0) {
			// cnr = cnr + "-" +itemDto.getCRevision().trim() ;
			// }

			return cnr;
		}

		private String getLieferantennummer(LieferscheinDto lsDto, TheClientDto theClientDto) {
			if (lsDto.getAuftragIId() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_AVISO_BEREITS_DURCHGEFUEHRT,
						lsDto.getCNr());
			}
			Auftrag auftrag = em.find(Auftrag.class, lsDto.getAuftragIId());
			if (auftrag == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, lsDto.getAuftragIId().toString());
			}
			Kunde kunde = em.find(Kunde.class, auftrag.getKundeIIdAuftragsadresse());
			if (kunde == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						auftrag.getKundeIIdAuftragsadresse().toString());
			}
			return kunde.getCLieferantennr();
		}

		private String getLieferantennummer(Integer kundeIId, TheClientDto theClientDto) {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
			return kundeDto.getCLieferantennr();
		}

		private XMLXMLDISPATCHNOTIFICATIONSUMMARY buildDnSummary(LieferscheinDto lieferscheinDto,
				Collection<LieferscheinpositionDto> positionDtos, TheClientDto theClientDto) {
			XMLXMLDISPATCHNOTIFICATIONSUMMARY dnSummary = new XMLXMLDISPATCHNOTIFICATIONSUMMARY();
			int itemCount = positionDtos.size();

			dnSummary.setTOTALITEMNUM(new BigInteger(String.valueOf(itemCount)));
			return dnSummary;
		}
	}

	public class LieferscheinAvisoProducerEdi4All implements ILieferscheinAvisoProducer {
		private VersandwegPartnerEdi4All versandwegPartner;
		private Integer versandwegId;
		private Integer receiverKundeId;

		public LieferscheinAvisoProducerEdi4All(Integer versandwegId, Integer receiverKundeId) {
			this.versandwegId = versandwegId;
			this.receiverKundeId = receiverKundeId;
		}

		@Override
		public Integer versandwegId() {
			return versandwegId;
		}

		@Override
		public Integer receiverKundeId() {
			return receiverKundeId;
		}

		@Override
		public boolean isDummy() {
			return false;
		}

		@Override
		public ILieferscheinAviso createAviso(LieferscheinDto lieferscheinDto, TheClientDto theClientDto)
				throws RemoteException, NamingException, EJBExceptionLP {
			LieferscheinAvisoEdi4All aviso = createHeaderInfo(lieferscheinDto, theClientDto);
			Collection<LieferscheinpositionDto> posDtos = getLSPositionsDto(lieferscheinDto, theClientDto);
			createPositionInfo(aviso, posDtos, theClientDto);

			aviso.persist();

			/*
			 * Integer versandwegId = getVersandwegIdFor(aviso.getLieferscheinDto(),
			 * SystemFac.VersandwegType.Edi4AllDesadv, theClientDto);
			 * aviso.setVersandwegId(versandwegId); Kunde kunde = em.find(Kunde.class,
			 * lieferscheinDto.getKundeIIdLieferadresse());
			 * aviso.setPartnerId(kunde.getPartnerIId());
			 */
			return aviso;
		}

		@Override
		public void archiveAviso(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			LieferscheinDto lsDto = aviso.getLieferscheinDto();
			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);

			JCRDocDto jcrDocDto = new JCRDocDto();
			DocPath dp = new DocPath(new DocNodeLieferschein(lsDto)).add(new DocNodeFile("Lieferaviso_Edi4All.txt"));
			jcrDocDto.setDocPath(dp);
			jcrDocDto.setbData(aviso.toString().getBytes());
			jcrDocDto.setbVersteckt(false);
			jcrDocDto.setlAnleger(partnerDto.getIId());

			Integer kundeId = lsDto.getKundeIIdLieferadresse();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

			jcrDocDto.setlPartner(kundeDto.getPartnerIId());
			jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
			jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
			jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
			jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
			jcrDocDto.setsBelegnummer(lsDto.getCNr().replace("/", "."));
			jcrDocDto.setsFilename(lsDto.getCNr().replace("/", "."));
			jcrDocDto.setsMIME(".txt");
			jcrDocDto.setsName("Lieferaviso Edifact" + lsDto.getIId());
			jcrDocDto.setsRow(lsDto.getIId().toString());
			jcrDocDto.setsTable("LIEFERSCHEIN");
			String sSchlagworte = "Export Edifact EDI4ALL EDI DESADV Aviso";
			jcrDocDto.setsSchlagworte(sSchlagworte);
			getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, theClientDto);
		}

		private LieferscheinAvisoEdi4All setupAviso(LieferscheinAvisoEdi4All aviso, LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			aviso.setLieferscheinDto(lieferscheinDto);
			aviso.setVersandwegId(versandwegId());

//			Integer versandwegId = getVersandwegIdForEdi4All(lieferscheinDto, theClientDto);
//			aviso.setVersandwegId(versandwegId);
			return aviso;
		}

		private LieferscheinAvisoEdi4All createHeaderInfo(LieferscheinDto lieferscheinDto, TheClientDto theClientDto)
				throws RemoteException {
			LieferscheinAvisoEdi4All aviso = setupAviso(new LieferscheinAvisoEdi4All(), lieferscheinDto, theClientDto);

			Integer kundeId = receiverKundeId();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);
			aviso.setPartnerId(kundeDto.getPartnerIId());

			versandwegPartner = (VersandwegPartnerEdi4All) getVersandwegPartner(kundeId,
					SystemFac.VersandwegType.Edi4AllDesadv, theClientDto);

			String field00E = (versandwegPartner != null && versandwegPartner.getC00E() != null)
					? versandwegPartner.getC00E()
					: kundeDto.getPartnerDto().getCKbez();
			aviso.addField("00E", field00E);
			// aviso.addField("00E", kundeDto.getPartnerDto().getCKbez());
//			aviso.addField("00P", kundeDto.getPartnerDto().getCKbez());
			aviso.addField("001", "Liefermeldung");

			BelegnummerTransformerAbstract bnTransformer = BelegnummerTransformerFactory.create();
			aviso.addField("002",
					bnTransformer.transform(BelegnummerTyp.Lieferschein, aviso.getLieferscheinDto().getCNr()));
//			aviso.addField("002", createBelegnummer(
//					Belegtyp.Lieferschein, aviso.getLieferscheinDto().getCNr(), null));
			aviso.addField("003", aviso.getLieferscheinDto().getTBelegdatum());
			Date avisoDate = calculateAvisoDate(kundeDto, aviso.getLieferscheinDto());
			aviso.addField("007", avisoDate);

			String rechnungKundeFeld = getRechnungKunde(aviso.getLieferscheinDto(), theClientDto);
			if (rechnungKundeFeld != null) {
				// Der Kaeufer entspricht dem Empfaenger, zumindest fuer EDI4ALL
				aviso.addField("028", rechnungKundeFeld);
			}
			if (aviso.getLieferscheinDto().getKundeIIdRechnungsadresse() != null) {
				String uidKaeufer = createUIDKaeufer(aviso.getLieferscheinDto().getKundeIIdRechnungsadresse(),
						theClientDto);
				if (!Helper.isStringEmpty(uidKaeufer)) {
					aviso.addField("030", uidKaeufer);
				}
			}
			String lieferKundeFeld = getLieferKunde(aviso.getLieferscheinDto(), theClientDto);
			if (lieferKundeFeld != null) {
				aviso.addField("041", lieferKundeFeld);
			}

			String uidLieferant = createUIDLieferant(aviso.getLieferscheinDto().getKundeIIdLieferadresse(),
					theClientDto);
			if (!uidLieferant.isEmpty()) {
				aviso.addField("045", uidLieferant);
			}

			if (aviso.getLieferscheinDto().getSpediteurIId() != null) {
				SpediteurDto spediteurDto = getMandantFac()
						.spediteurFindByPrimaryKey(aviso.getLieferscheinDto().getSpediteurIId());
				String s = createSpediteurInfo(spediteurDto);
				aviso.addField("052", s);
			}

			aviso.addField("060", getLieferInfo(aviso.getLieferscheinDto(), theClientDto));
			aviso.addSeparator();

			return aviso;
		}

		private String getLieferInfo(LieferscheinDto lieferscheinDto, TheClientDto theClientDto)
				throws RemoteException {
			LieferartDto lieferartDto = getLocaleFac().lieferartFindByPrimaryKey(lieferscheinDto.getLieferartIId(),
					theClientDto);
			StringBuffer sb = new StringBuffer();
			sb.append(lieferartDto.getCNr());
			sb.append(";" + lieferartDto.getCNr());
			sb.append(";" + (lieferscheinDto.getIAnzahlPakete() != null && lieferscheinDto.getIAnzahlPakete() > 0
					? lieferscheinDto.getIAnzahlPakete().toString()
					: ""));
			BigDecimal d = lieferscheinDto.getFGewichtLieferung() != null && lieferscheinDto.getFGewichtLieferung() > 0
					? new BigDecimal(lieferscheinDto.getFGewichtLieferung())
					: BigDecimal.ZERO;
			d = d.setScale(2, RoundingMode.HALF_EVEN);
			sb.append(";" + d.toPlainString());
			return sb.toString();
		}

		/**
		 * Spediteurname f&uuml;r Konverter ermitteln</br>
		 * <p>
		 * Hat der Spediteur ein ";" im Namen, wird nur der Teil ab dem ; an den
		 * Konverter &uuml;bergeben.
		 * 
		 * @param spediteurDto
		 * @return Der Name des Spediteurs
		 */
		private String createSpediteurInfo(SpediteurDto spediteurDto) {
			String s = spediteurDto.getCNamedesspediteurs();
			if (s.indexOf(';') > -1) {
				// Wenn "Panalpina;SHIP", dann "SHIP" verwenden
				String tokens[] = s.split(";");
				if (tokens.length > 1) {
					s = tokens[1];
				}
			}

			return s;
		}

		private String createUIDKaeufer(Integer kaeuferKundeId, TheClientDto theClientDto) throws RemoteException {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kaeuferKundeId, theClientDto);
			String s = emptyField(kundeDto.getPartnerDto().getCUid(), "");
			return s;
		}

		private String createUIDLieferant(Integer lieferKundeId, TheClientDto theClientDto) throws RemoteException {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			String s = emptyField(mandantDto.getPartnerDto().getCUid(), "");
			FunktionDto edi4allFunktionDto = getLocaleFac().funktionFindByCnr("EDI4ALL", theClientDto);
			if (edi4allFunktionDto == null)
				return s;

			KundesachbearbeiterDto[] dtos = getKundesachbearbeiterFac()
					.kundesachbearbeiterFindByKundeIId(lieferKundeId);
			for (KundesachbearbeiterDto dto : dtos) {
				if (edi4allFunktionDto.getIId().equals(dto.getFunktionIId())) {
					PersonalDto persDto = getPersonalFac().personalFindByPrimaryKey(dto.getPersonalIId(), theClientDto);
					PartnerDto partnerDto = persDto.getPartnerDto();
					String anrede = formatAdresse(partnerDto, mandantDto, theClientDto.getLocUi());
					s = s + ";" + anrede.trim();
					if (!Helper.isStringEmpty(persDto.getCTelefon())) {
						s = s + ";" + persDto.getCTelefon().trim();
					}
				}
			}

			return s;
		}

		private String formatAdresse(PartnerDto partnerDto, MandantDto mandantDto, Locale locale) {
			String s = formatAdresseFuerAusdruck(partnerDto, null, mandantDto, locale, false, null, true);
			return s.split("\n")[0];
		}

		private void createPositionInfo(LieferscheinAvisoEdi4All aviso, Collection<LieferscheinpositionDto> posDtos,
				TheClientDto theClientDto) throws RemoteException {
			BelegnummerTransformerAbstract bnTransformer = BelegnummerTransformerFactory.create();
			for (LieferscheinpositionDto posDto : posDtos) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(posDto.getArtikelIId(), theClientDto);
				aviso.addField("068", artikelDto.getCNr());
				aviso.addField("070",
						artikelDto.getKbezAusSpr() == null ? artikelDto.getCNr() : artikelDto.getKbezAusSpr(), 35);
				if (!Helper.isStringEmpty(artikelDto.getCWarenverkehrsnummer())) {
					aviso.addField("071", artikelDto.getCWarenverkehrsnummer().trim());
				}
				if (artikelDto.getArtikelsprDto() != null) {
					aviso.addField("072", artikelDto.getArtikelsprDto().getCBez(), 35);
				}
				if (artikelDto.getLandIIdUrsprungsland() != null) {
					Land land = em.find(Land.class, artikelDto.getLandIIdUrsprungsland());
					aviso.addField("083", land.getCLkz());
				}
				aviso.addField("084", posDto.getNMenge());

				addFieldPosBestellnr(aviso, posDto, theClientDto);

				if (posDto.getAuftragpositionIId() != null) {
					AuftragpositionDto abposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(posDto.getAuftragpositionIId());
					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(abposDto.getBelegIId());
					Integer posNr = getAuftragpositionFac().getPositionNummer(abposDto.getIId());
					aviso.addField("097",
							bnTransformer.transform(BelegnummerTyp.Auftrag, "AB" + auftragDto.getCNr(), posNr));
//					aviso.addField("097", createBelegnummer(Belegtyp.Auftrag,
//							"AB" + auftragDto.getCNr(), posNr));
				} else {
					Integer posNr = getLieferscheinpositionFac().getPositionNummer(posDto.getIId());
					aviso.addField("097", bnTransformer.transform(BelegnummerTyp.Lieferschein,
							"LS" + aviso.getLieferscheinDto().getCNr(), posNr));
//					aviso.addField("097", createBelegnummer(Belegtyp.Lieferschein,
//							"LS" + aviso.getLieferscheinDto().getCNr(), posNr));
				}
				aviso.addField("098", aviso.getLieferscheinDto().getTBelegdatum());
				aviso.addSeparator();
			}
		}

		private void addFieldPosBestellnr(LieferscheinAvisoEdi4All aviso, LieferscheinpositionDto posDto,
				TheClientDto theClientDto) throws RemoteException {
			String s = null;
			if (posDto.getAuftragpositionIId() != null) {
				Auftragposition abpos = em.find(Auftragposition.class, posDto.getAuftragpositionIId());
				if (abpos != null) {
					Auftrag auftrag = em.find(Auftrag.class, abpos.getAuftragIId());
					s = auftrag.getCBestellnummer();
				}
			} else {
				if (posDto.getForecastpositionIId() != null) {
					Forecastposition fcpos = em.find(Forecastposition.class, posDto.getForecastpositionIId());
					if (Helper.isStringEmpty(fcpos.getCBestellnummer())) {
						Integer posNr = getLieferscheinpositionFac().getPositionNummer(posDto.getIId());
						throw EJBExcFactory.bestellNummerInForecastpositionFehlt(aviso.getLieferscheinDto(), posNr,
								fcpos.getIId());
					}
					s = fcpos.getCBestellnummer().replace("-", ";");
				}
			}

			if (Helper.isStringEmpty(s)) {
				s = aviso.getLieferscheinDto().getCBestellnummer();
			}

			if (Helper.isStringEmpty(s)) {
				s = versandwegPartner.getC094();
			}

			if (!Helper.isStringEmpty(s)) {
				aviso.addField("094", s);
			}
		}

		private Collection<LieferscheinpositionDto> getLSPositionsDto(LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			Collection<LieferscheinpositionDto> ejbPositions = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(lieferscheinDto.getIId(), theClientDto);
			CollectionUtils.filter(ejbPositions, new LieferscheinpositionenNurIdentFilter());
			return ejbPositions;
		}

		private Date calculateAvisoDate(KundeDto kundeDto, LieferscheinDto lieferscheinDto) {
			Timestamp t = lieferscheinDto.getTLiefertermin();
			if (t == null) {
				t = setzeAuslieferdatumAufJetzt(lieferscheinDto.getIId());
				lieferscheinDto.setTLiefertermin(t);

				if (t == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_MUSS_GELIEFERT_SEIN,
							"LS muss geliefert sein", lieferscheinDto.getCNr());
				}
			}

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(t.getTime());
			return c.getTime();
		}

		private String getRechnungKunde(LieferscheinDto lieferscheinDto, TheClientDto theClientDto) {
			Kunde kunde = em.find(Kunde.class, lieferscheinDto.getKundeIIdRechnungsadresse());
			return createKundeInfo(kunde, theClientDto);
		}

		private String getLieferKunde(LieferscheinDto lieferscheinDto, TheClientDto theClientDto) {
			Kunde kunde = em.find(Kunde.class, lieferscheinDto.getKundeIIdLieferadresse());
			return createKundeInfo(kunde, theClientDto);
		}

		private String getKaeuferKunde(LieferscheinDto lieferscheinDto, Collection<LieferscheinpositionDto> posDtos,
				TheClientDto theClientDto) {
			Kunde kunde = null;
			if (lieferscheinDto.getAuftragIId() != null) {
				Auftrag auftrag = em.find(Auftrag.class, lieferscheinDto.getAuftragIId());
				Integer kundeId = auftrag.getKundeIIdAuftragsadresse();
				kunde = em.find(Kunde.class, kundeId);
			} else {
				for (LieferscheinpositionDto posDto : posDtos) {
					if (posDto.getForecastpositionIId() != null) {
						Forecastposition fcpos = em.find(Forecastposition.class, posDto.getForecastpositionIId());
						Forecastauftrag fcauftrag = em.find(Forecastauftrag.class, fcpos.getForecastauftragIId());
						Fclieferadresse fclieferadresse = em.find(Fclieferadresse.class,
								fcauftrag.getFclieferadresseIId());
						Forecast forecast = em.find(Forecast.class, fclieferadresse.getForecastIId());
						kunde = em.find(Kunde.class, forecast.getKundeIId());
						break;
					}
				}

				if (kunde == null) {
					kunde = em.find(Kunde.class, lieferscheinDto.getKundeIIdLieferadresse());
				}
			}

			return kunde != null ? createKundeInfo(kunde, theClientDto) : null;
		}

		private String createKundeInfo(Kunde kunde, TheClientDto theClientDto) {
			StringBuffer sb = new StringBuffer();
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(kunde.getPartnerIId(), theClientDto);
			sb.append(emptyField(partnerDto.getCIln(), "ILN" + kunde.getIId())).append(";");
			sb.append(partnerDto.getCName1nachnamefirmazeile1()).append(";");
			sb.append(emptyField(partnerDto.getCName2vornamefirmazeile2())).append(";");
			sb.append(emptyField(partnerDto.getCStrasse())).append(";");

			sb.append(partnerDto.getLandplzortDto().getLandDto().getCLkz()).append(";");
			sb.append(emptyField(partnerDto.getLandplzortDto().getCPlz())).append(";");
			sb.append(emptyField(partnerDto.getLandplzortDto().getOrtDto().getCName()));
			return sb.toString();
		}

		private String emptyField(String value) {
			return value == null ? "" : value;
		}

		private String emptyField(String value, String defaultValue) {
			return value == null ? (defaultValue == null ? "" : defaultValue) : value;
		}

		private Date calculateAvisoDate0(KundeDto kundeDto, Timestamp lieferDate) {
			Integer kundeDauer = kundeDto.getILieferdauer();
			Integer avisoDauer = getParameterFac().getLieferavisoTageZusaetzlich(kundeDto.getMandantCNr());
			Integer days = kundeDauer + avisoDauer;
			if (days < 0) {
				myLogger.warn("Lieferavisodauer < 0 (Lieferdauer Kunde:" + kundeDauer + ", paramzusaetzlich:"
						+ avisoDauer + ") auf 0 gesetzt.");
				days = 0;
			}
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(lieferDate.getTime());
			c.add(Calendar.DAY_OF_MONTH, days);
			return c.getTime();
		}

		@Override
		public void postAviso(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			if (!(aviso instanceof LieferscheinAvisoEdi4All)) {
				return;
			}
			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac().versandwegPartnerFindByVersandwegIdPartnerId(
					aviso.getVersandwegId(), aviso.getPartnerId(), theClientDto.getMandant());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						aviso.getPartnerId().toString());
			}

			VersandwegPartnerEdi4AllDto ediPartnerDto = (VersandwegPartnerEdi4AllDto) versandwegPartnerDto;
			createAvisoFile((LieferscheinAvisoEdi4All) aviso, ediPartnerDto, theClientDto);
		}

		private void createAvisoFile(LieferscheinAvisoEdi4All aviso, VersandwegPartnerEdi4AllDto versandwegPartnerDto,
				TheClientDto theClientDto) {

			File f = new File(versandwegPartnerDto.getCExportPfad());
			if (f.isDirectory()) {
				String basefilename = "ls_" + aviso.getLieferscheinDto().getCNr().replaceAll("/", "_");
				String filename = basefilename + ".txt";
				try {
					String path = versandwegPartnerDto.getCExportPfad() + "/" + filename;
					writeFile(path, aviso.toString());
				} catch (IOException e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EDIFACT_EXPORTDATEI_NICHT_ERSTELLBAR, e);
				}

//				try {
//					String batchfile = versandwegPartnerDto.getCExportPfad()
//							+ "/hv_" + basefilename + ".bat";
//					writeFile(batchfile, "@echo off\r\n" + "cd edi4all\r\n"
//							+ "edi4all.exe ../" + filename + "\r\n");
//				} catch (IOException e) {
//					throw new EJBExceptionLP(
//							EJBExceptionLP.FEHLER_EDIFACT_EXPORTDATEI_NICHT_ERSTELLBAR,
//							e);
//				}
			} else {
				String msg = "Edifact Exportpath '" + versandwegPartnerDto.getCExportPfad()
						+ "' existiert nicht, bzw. ist kein Verzeichnis!";
				myLogger.warn(msg);
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EDIFACT_EXPORTVERZEICHNIS_NICHT_VORHANDEN, msg,
						versandwegPartnerDto.getCExportPfad());
			}
		}

		private void writeFile(String filename, String content) throws IOException {
			Writer writer = null;

			try {
//				writer = new BufferedWriter(new OutputStreamWriter(
//						new FileOutputStream(filename), "utf-8"));
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "Cp1252"));
				writer.write(content);
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (Exception ex) {
				}
			}
		}

		@Override
		public String toString(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			if (!(aviso instanceof LieferscheinAvisoEdi4All)) {
				return null;
			}
			return aviso.toString();
		}
	}

	private KundeDto getAuftragsKunde(LieferscheinDto lieferscheinDto, TheClientDto theClientDto) {
		if (lieferscheinDto.getAuftragIId() != null) {
			Auftrag auftrag = em.find(Auftrag.class, lieferscheinDto.getAuftragIId());
			return getKundeFac().kundeFindByPrimaryKey(auftrag.getKundeIIdAuftragsadresse(), theClientDto);
		}

		return getKundeFac().kundeFindByPrimaryKey(lieferscheinDto.getKundeIIdLieferadresse(), theClientDto);
	}

	public class LieferscheinAvisoProducerDesAdv implements ILieferscheinAvisoProducer {
		private DateFormat unbDateFormat = new SimpleDateFormat("yyMMdd");
		private DateFormat unbTimeFormat = new SimpleDateFormat("HHmm");
		private DateFormat controlFormat = new SimpleDateFormat("yyMMddHHmmss");
		private DateFormat refDateFormat = new SimpleDateFormat("yyyyMMdd");
		private EdifactOrdersImportImplBean ediImportImplBean = new EdifactOrdersImportImplBean();
		private EdifactOrderResponseProducer producer = new EdifactOrderResponseProducer();
		private String ediContentCached = null;

		private Integer versandwegId;
		private Integer receiverKundeId;

		public LieferscheinAvisoProducerDesAdv(Integer versandwegId, Integer receiverKundeId) {
			this.versandwegId = versandwegId;
			this.receiverKundeId = receiverKundeId;
		}

		@Override
		public Integer versandwegId() {
			return versandwegId;
		}

		@Override
		public Integer receiverKundeId() {
			return receiverKundeId;
		}

		@Override
		public boolean isDummy() {
			return false;
		}

		@Override
		public ILieferscheinAviso createAviso(LieferscheinDto lieferscheinDto, TheClientDto theClientDto)
				throws RemoteException, NamingException, EJBExceptionLP {
			LieferavisoEdiDesAdv response = new LieferavisoEdiDesAdv(lieferscheinDto);
			createLs(response, theClientDto);
			createPositions(response, theClientDto);
			return response;
		}

		@Override
		public String toString(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			if (!(aviso instanceof LieferavisoEdiDesAdv))
				return "";

			return ((LieferavisoEdiDesAdv) aviso).asEdiContent();
		}

		@Override
		public void archiveAviso(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			if (!(aviso instanceof LieferavisoEdiDesAdv))
				return;

			LieferavisoEdiDesAdv response = (LieferavisoEdiDesAdv) aviso;

			LieferscheinDto lsDto = aviso.getLieferscheinDto();
			Integer kundeId = lsDto.getKundeIIdLieferadresse();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

			JCRDocDto jcrDocDto = prepareJcrDto(new JCRDocDto(), lsDto, kundeDto, theClientDto);
			DocPath dp = new DocPath(new DocNodeLieferschein(lsDto)).add(new DocNodeFile("Lieferaviso_DESADV.txt"));
			jcrDocDto.setDocPath(dp);
			String ediContent = response.asEdiContent();
			jcrDocDto.setbData(ediContent.getBytes());

			jcrDocDto.setsMIME(".txt");
			jcrDocDto.setsName("DespatchAdvice Edifact " + lsDto.getIId());
			String sSchlagworte = "Export Edifact EDI Despatchadvice desadv";
			jcrDocDto.setsSchlagworte(sSchlagworte);

			List<JCRDocDto> dtos = new ArrayList<JCRDocDto>();
			dtos.add(jcrDocDto);

			if (response.getPostResponseContent() != null) {
				JCRDocDto jcrPostDto = prepareJcrDto(new JCRDocDto(), lsDto, kundeDto, theClientDto);
				DocPath dpPost = new DocPath(new DocNodeLieferschein(lsDto))
						.add(new DocNodeFile("Lieferaviso_DESADV_post.html"));

				jcrPostDto.setDocPath(dpPost);
				jcrPostDto.setbData(response.getPostResponseContent().getBytes());
				jcrPostDto.setsMIME(".html");
				jcrPostDto.setsName("DespatchAdvice Edifact " + lsDto.getIId() + " post");
				sSchlagworte = "Export Edifact EDI DespatchAdvice desadv post " + response.getPostStatusCode();
				jcrPostDto.setsSchlagworte(sSchlagworte);
				dtos.add(jcrPostDto);
			}

			getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(dtos, theClientDto);
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
			dto.setsTable("LIEFERSCHEIN");
			return dto;
		}

		@Override
		public void postAviso(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			if (!(aviso instanceof LieferavisoEdiDesAdv))
				return;
			LieferavisoEdiDesAdv response = (LieferavisoEdiDesAdv) aviso;

			try {
				HttpURLConnection urlConnection = buildUrlconnectionPost(response, theClientDto);
				urlConnection.setRequestProperty("Content-Type", "text/plain");
				OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
				String xmlDndContent = toString(response, theClientDto);
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

		private HttpURLConnection buildUrlconnectionPost(ILieferscheinAviso response, TheClientDto theClientDto)
				throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException,
				KeyManagementException, UnrecoverableKeyException {
			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac().versandwegPartnerFindByVersandwegIdPartnerId(
					response.getVersandwegId(), response.getPartnerId(), theClientDto.getMandant());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						response.getPartnerId().toString());
			}

			VersandwegPartnerDesAdvDto ediPartnerDto = (VersandwegPartnerDesAdvDto) versandwegPartnerDto;

			String uri = ediPartnerDto.getCEndpunkt();
			if (Helper.isStringEmpty(uri)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_ENDPUNKT_FEHLT,
						response.getPartnerId().toString());
			}

			String user = ediPartnerDto.getCBenutzer();
			if (Helper.isStringEmpty(user)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_BENUTZER_FEHLT,
						response.getPartnerId().toString());
			}

			String pwd = ediPartnerDto.getCKennwort();
			if (Helper.isStringEmpty(pwd)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_KENNWORT_FEHLT,
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

		private String getEdiContent(Integer auftragId, TheClientDto theClientDto)
				throws IOException, RepositoryException {
			if (ediContentCached == null) {
				ediContentCached = getAuftragFac().loadEdiFileBestellung(auftragId, theClientDto);
			}

			return ediContentCached;
		}

		private LieferavisoEdiDesAdv createLs(LieferavisoEdiDesAdv response, TheClientDto theClientDto)
				throws RemoteException {
			KundeDto kundeDto = getAuftragsKunde(response.getLieferscheinDto(), theClientDto);
			Date ref = Helper.asDate(getTimestamp());
			createUnbUnh(response, kundeDto, ref, theClientDto);
			createBgm(response, kundeDto);
			createBelegdatum(response, theClientDto);
			createBestellreferenz(response);
			createLieferantInfo(response, kundeDto, theClientDto);
			createVertreterInfo(response, theClientDto);
			createBuyerPartyInfo(response, theClientDto);
			createDeliveryPartyInfo(response, theClientDto);
			createTransportPartyInfo(response, theClientDto);
			createPackagingInfo(response, theClientDto);
			return response;
		}

		private LieferavisoEdiDesAdv createPositions(LieferavisoEdiDesAdv response, TheClientDto theClientDto)
				throws RemoteException {
			LieferscheinDto lsDto = response.getLieferscheinDto();
			Collection<LieferscheinpositionDto> dtos = getLieferscheinPositionsDto(lsDto, theClientDto);

			for (LieferscheinpositionDto posDto : dtos) {
				createLinInfo(response, posDto, theClientDto);
			}

			return response;
		}

		private LieferavisoEdiDesAdv createLinInfo(LieferavisoEdiDesAdv response, LieferscheinpositionDto posDto,
				TheClientDto theClientDto) throws RemoteException {
			Integer posNr = getLieferscheinpositionFac().getLSPositionNummer(posDto.getIId());
			StringBuffer sb = new StringBuffer(posNr.toString()).append('+').append("4").append('+')
					.append(producer.getOrderItemInfo(posDto));
			response.addSegment("LIN", sb);

			sb = new StringBuffer("1").append('+').append(producer.getOrderItemInfo(posDto));
			response.addSegment("PIA", sb);

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(posDto.getArtikelIId(), theClientDto);
			sb = new StringBuffer("1").append('+').append(artikelDto.getCNr().trim()).append(':').append("VP");
			response.addSegment("PIA", sb);

			if (!Helper.isStringEmpty(artikelDto.getCWarenverkehrsnummer())) {
				sb = new StringBuffer("1").append('+').append(artikelDto.getCWarenverkehrsnummer().trim()).append(':')
						.append("CV");
				response.addSegment("PIA", sb);
			}

			sb = new StringBuffer("F").append('+').append('+').append(':').append(':').append(':')
					.append(artikelDto.getCBezAusSpr());
			response.addSegment("IMD", sb);

			// Despatch-Quantity
			sb = new StringBuffer("12").append(':').append(asEdiQuantity(posDto.getNMenge())).append(':')
					.append(posDto.getEinheitCNr().trim());
			response.addSegment("QTY", sb);

			// Origin
			if (artikelDto.getLandIIdUrsprungsland() != null) {
				Land land = em.find(Land.class, artikelDto.getLandIIdUrsprungsland());
				sb = new StringBuffer(land.getCLkz());
				response.addSegment("ALI", sb);
			}

			createGinInfo(response, posDto, artikelDto);

			createOrderReference(response, posDto);
			return response;
		}

		private LieferavisoEdiDesAdv createOrderReference(LieferavisoEdiDesAdv response,
				LieferscheinpositionDto posDto) {
			LieferscheinDto lsDto = response.getLieferscheinDto();
			StringBuffer sb = new StringBuffer("ON").append(':').append(lsDto.getCBestellnummer());
			HvOptional<String> lineItemId = producer.getLineItemId(posDto);
			if (lineItemId.isPresent()) {
				sb.append(':').append(lineItemId.get());
			}
			response.addSegment("RFF", sb.toString());
			return response;
		}

		private LieferavisoEdiDesAdv createGinInfo(LieferavisoEdiDesAdv response, LieferscheinpositionDto posDto,
				ArtikelDto artikelDto) {
			if (artikelDto.isSeriennrtragend() && posDto.getSeriennrChargennrMitMenge().size() > 0) {
				int repetitions = 0;
				int subs = 0;
				StringBuffer sb = new StringBuffer();
				for (SeriennrChargennrMitMengeDto entry : posDto.getSeriennrChargennrMitMenge()) {
					if (subs == 0) {
						sb = new StringBuffer("BN");
					}

					sb.append('+').append(entry.getCSeriennrChargennr().trim());
					if (++subs >= 5) {
						response.addSegment("GIN", sb);
						subs = 0;
						if (++repetitions >= 100) {
							myLogger.warn("Max " + repetitions + " Wiederholungen innerhalb Seriennummer erreicht!");
							break;
						}
					}
				}

				if (subs > 0) {
					response.addSegment("GIN", sb);
				}
			}

			return response;
		}

		private Collection<LieferscheinpositionDto> getLieferscheinPositionsDto(LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			Collection<LieferscheinpositionDto> ejbPositions = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferschein(lieferscheinDto.getIId(), theClientDto);
			CollectionUtils.filter(ejbPositions, new LieferscheinpositionenNurIdentFilter());
			return ejbPositions;
		}

		private String asEdiQuantity(BigDecimal quantity) {
			if (quantity.remainder(BigDecimal.ONE).signum() == 0) {
				return quantity.setScale(0).toPlainString();
			} else {
				return quantity.toPlainString();
			}
		}

		// UNB+UNOC:3+<Lieferantennr>+<unbReceiver>+JJMMTT:HHMM+JJMMTTHHMMSS++ORDRSP
		// UNH+JJMMTTHHMMSS+ORDRSP:D:96A:UN
		private void createUnbUnh(LieferavisoEdiDesAdv response, KundeDto kundeDto, Date ref,
				TheClientDto theClientDto) {
			if (Helper.isStringEmpty(kundeDto.getCLieferantennr())) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_LIEFERANTENNUMMER_FEHLT,
						kundeDto.getIId().toString());
			}

			IVersandwegPartnerDto dto = getSystemFac().versandwegPartnerFindByVersandwegCnrPartnerId(
					SystemFac.VersandwegType.EdiDesadv, kundeDto.getPartnerIId(), theClientDto.getMandant());
			if (dto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						kundeDto.getPartnerIId().toString());
			}

			VersandwegPartnerDesAdvDto desAdvDto = (VersandwegPartnerDesAdvDto) dto;
			response.setPartnerId(kundeDto.getPartnerIId());
			response.setVersandwegId(desAdvDto.getVersandwegId());

			StringBuffer sb = new StringBuffer("UNOC:3+").append(kundeDto.getCLieferantennr()).append('+')
					.append(desAdvDto.getCUnbEmpfaenger()).append('+').append(unbDateFormat.format(ref)).append(':')
					.append(unbTimeFormat.format(ref)).append('+').append(controlFormat.format(ref)).append('+')
					.append('+').append("DESADV");
			response.addSegment("UNB", sb.toString());

			sb = new StringBuffer(controlFormat.format(ref)).append('+').append("DESADV:D:96A:UN");
			response.addSegment("UNH", sb);
		}

		// BGM+350+<LieferantenNr><LieferscheinCnr>+(9|5)
		private void createBgm(LieferavisoEdiDesAdv response, KundeDto kundeDto) {
			LieferscheinDto lsDto = response.getLieferscheinDto();
			StringBuffer sb = new StringBuffer("351").append('+').append(kundeDto.getCLieferantennr()).append("LS ")
					.append(lsDto.getCNr()).append('+').append(lsDto.getTLieferaviso() == null ? '9' : '5');
			response.addSegment("BGM", sb);
		}

		private void createBelegdatum(LieferavisoEdiDesAdv response, TheClientDto theClientDto) {
			LieferscheinDto lsDto = response.getLieferscheinDto();
			StringBuffer sb = new StringBuffer("137").append(':')
					.append(refDateFormat.format(Helper.asDate(lsDto.getTBelegdatum()))).append(":102");
			response.addSegment("DTM", sb);

			KundeDto lieferKundeDto = getKundeFac().kundeFindByPrimaryKey(lsDto.getKundeIIdLieferadresse(),
					theClientDto);
			Timestamp tAviso = calculateAvisoDate(lieferKundeDto, lsDto);
			sb = new StringBuffer("132").append(':').append(refDateFormat.format(Helper.asDate(tAviso))).append(':')
					.append("102");
			response.addSegment("DTM", sb);
		}

		private Timestamp calculateAvisoDate(KundeDto kundeDto, LieferscheinDto lieferscheinDto) {
			Timestamp t = lieferscheinDto.getTLiefertermin();
			if (t == null) {
				t = setzeAuslieferdatumAufJetzt(lieferscheinDto.getIId());
				lieferscheinDto.setTLiefertermin(t);

				if (t == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_MUSS_GELIEFERT_SEIN,
							"LS muss geliefert sein", lieferscheinDto.getCNr());
				}
			}

			Timestamp tAviso = addAvisoDurationDate(kundeDto, t);
			return tAviso;
		}

		private Timestamp addAvisoDurationDate(KundeDto lieferKundeDto, Timestamp lieferDate) {
			Integer kundeDauer = lieferKundeDto.getILieferdauer();
			Integer avisoDauer = getParameterFac().getLieferavisoTageZusaetzlich(lieferKundeDto.getMandantCNr());
			Integer days = kundeDauer + avisoDauer;
			if (days < 0) {
				myLogger.warn("Lieferavisodauer < 0 (Lieferdauer Kunde:" + kundeDauer + ", paramzusaetzlich:"
						+ avisoDauer + ") auf 0 gesetzt.");
				days = 0;
			}
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(lieferDate.getTime());
			c.add(Calendar.DAY_OF_MONTH, days);
			return new Timestamp(c.getTimeInMillis());
		}

		private void createBestellreferenz(LieferavisoEdiDesAdv response) {
			LieferscheinDto lsDto = response.getLieferscheinDto();
			StringBuffer sb = new StringBuffer("ON").append(':').append(lsDto.getCBestellnummer());
			response.addSegment("RFF", sb.toString());

			if (lsDto.getAuftragIId() != null) {
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(lsDto.getAuftragIId());
				sb = new StringBuffer("171").append(':')
						.append(refDateFormat.format(Helper.asDate(auftragDto.getDBestelldatum()))).append(":102");
				response.addSegment("DTM", sb);
			}
		}

		private void createLieferantInfo(LieferavisoEdiDesAdv response, KundeDto kundeDto, TheClientDto theClientDto)
				throws RemoteException {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			PartnerDto partnerDto = mandantDto.getPartnerDto();
			createNadInfo(response, "SU", kundeDto.getCLieferantennr(), partnerDto);
		}

		private void createVertreterInfo(LieferavisoEdiDesAdv response, TheClientDto theClientDto)
				throws RemoteException {
			LieferscheinDto lsDto = response.getLieferscheinDto();
			Integer vertreterPersonalId = lsDto.getPersonalIIdVertreter();
			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(vertreterPersonalId, theClientDto);

			String s = getPartnerFac().formatFixAnredeTitelName2Name1(personalDto.getPartnerDto(),
					theClientDto.getLocMandant(), theClientDto);
			StringBuffer sb = new StringBuffer("OC").append('+').append(':').append(s);
			response.addSegment("CTA", sb);
		}

		private void createBuyerPartyInfo(LieferavisoEdiDesAdv response, TheClientDto theClientDto) {
			LieferscheinDto lsDto = response.getLieferscheinDto();
			if (lsDto.getAuftragIId() != null) {
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(lsDto.getAuftragIId());
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);
				createNadPartyInfo(response, "BY", KennungType.OrdersNadBuyer, kundeDto, theClientDto);
			}
		}

		private void createDeliveryPartyInfo(LieferavisoEdiDesAdv response, TheClientDto theClientDto) {
			LieferscheinDto lsDto = response.getLieferscheinDto();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(lsDto.getKundeIIdLieferadresse(), theClientDto);
			createNadPartyInfo(response, "DP", KennungType.OrdersNadDelivery, kundeDto, theClientDto);
		}

		private void createTransportPartyInfo(LieferavisoEdiDesAdv response, TheClientDto theClientDto)
				throws RemoteException {
			LieferscheinDto lsDto = response.getLieferscheinDto();
			if (lsDto.getSpediteurIId() != null) {
				SpediteurDto spediteurDto = getMandantFac().spediteurFindByPrimaryKey(lsDto.getSpediteurIId());
				PartnerDto partnerDto = null;
				if (spediteurDto.getPartnerIId() != null) {
					partnerDto = getPartnerFac().partnerFindByPrimaryKey(spediteurDto.getPartnerIId(), theClientDto);
				}
				createNadInfo(response, "CZ", spediteurDto.getCNamedesspediteurs(), partnerDto);
			}
		}

		private void createPackagingInfo(LieferavisoEdiDesAdv response, TheClientDto theClientDto)
				throws RemoteException {
			LieferscheinDto lsDto = response.getLieferscheinDto();
			StringBuffer sb = new StringBuffer("20");
			if (!Helper.isStringEmpty(lsDto.getCVersandnummer())) {
				sb.append('+').append(lsDto.getCVersandnummer().trim());
			}
			response.addSegment("TDT", sb);

			if (lsDto.getIAnzahlPakete() != null && lsDto.getIAnzahlPakete() > 0) {
				// Packaging level 1, 4 = packaging level undefined
				sb = new StringBuffer("1::4");
				response.addSegment("CPS", sb);

				sb = new StringBuffer().append(lsDto.getIAnzahlPakete());
				response.addSegment("PAC", sb);
			}
		}

		private void createNadPartyInfo(LieferavisoEdiDesAdv response, String ediKennung, KennungType kennung,
				KundeDto kundeDto, TheClientDto theClientDto) {
			KundeId kundeId = new KundeId(kundeDto.getIId());

			HvOptional<String> ediId = HvOptional.empty();
			try {
				String ediContent = getEdiContent(response.getLieferscheinDto().getAuftragIId(), theClientDto);
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

////			if (!ediId.isPresent() && kks.size() > 1) {
////				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_KUNDENKENNUNG_BESTELLUNG_FEHLT, 
////						kundeId.id().toString(), kundeId, ediKennung);					
////			}
//		
//			if (kks.size() != 1) {
			if (kk == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_KUNDENKENNUNG_FEHLT, kundeId.id().toString(),
						kundeId, kennung.getText());
			}

			createNadInfo(response, ediKennung, ediId.orElse(kk.getCWert()), kundeDto.getPartnerDto());
		}

		private void createNadInfo(LieferavisoEdiDesAdv response, String ediKennung, String ediReference,
				PartnerDto partnerDto) {
			StringBuffer sb = new StringBuffer(ediKennung).append('+').append(ediReference);
			if (partnerDto != null) {
				sb.append('+').append('+').append(partnerDto.getCName1nachnamefirmazeile1()).append('+')
						.append(partnerDto.getCStrasse()).append('+')
						.append(partnerDto.getLandplzortDto().getOrtDto().getCName()).append('+').append('+')
						.append(partnerDto.getLandplzortDto().getCPlz()).append('+')
						.append(partnerDto.getLandplzortDto().getLandDto().getCLkz());
			}
			response.addSegment("NAD", sb);
		}
	}

	public class LieferscheinAvisoProducerDummy implements ILieferscheinAvisoProducer {

		public LieferscheinAvisoProducerDummy() {
		}

		@Override
		public Integer versandwegId() {
			return null;
		}

		@Override
		public Integer receiverKundeId() {
			return null;
		}

		@Override
		public boolean isDummy() {
			return true;
		}

		@Override
		public ILieferscheinAviso createAviso(LieferscheinDto lieferscheinDto, TheClientDto theClientDto)
				throws RemoteException, NamingException, EJBExceptionLP {
			return null;
		}

		@Override
		public void archiveAviso(ILieferscheinAviso aviso, TheClientDto theClientDto) {
		}

		@Override
		public String toString(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			return null;
		}

		@Override
		public void postAviso(ILieferscheinAviso aviso, TheClientDto theClientDto) {
		}
	}

	public class LieferscheinAvisoProducerCCSelfSender extends LieferscheinAvisoProducerCC {
		public LieferscheinAvisoProducerCCSelfSender(Integer versandwegId, Integer receiverKundeId) {
			super(versandwegId, receiverKundeId);
		}

		@Override
		protected String getCCEndpunkt(EntityManager em, VersandwegPartnerCCDto partnerCCDto) {
			return "http://localhost:8280/restapi/services/rest/api/beta/cc?fake=yes";
		}
	}

	public class LinienabrufFormatter {
		private Map<String, Object> valueMap;
		private LieferscheinDto lsDto;
		private VersandwegPartnerLinienabrufDto versandwegPartnerDto;
		private LieferscheinAvisoLinienabruf aviso;

		private final static String keyArtikelKBez = "%1";
		private final static String keyArtikelCnr = "%2";
		private final static String keyBestellnummer = "%3";
		private final static String keyDatum = "%4";
		private final static String keyMenge = "%5";
		private final static String keyLieferscheinCnr = "%6";

		private final static String valueEmptyDatum = "      ";

		public LinienabrufFormatter(LieferscheinAvisoLinienabruf aviso,
				VersandwegPartnerLinienabrufDto versandwegPartnerDto, TheClientDto theClientDto) {
			this.aviso = aviso;
			this.lsDto = aviso.getLieferscheinDto();
			this.versandwegPartnerDto = versandwegPartnerDto;
		}

		public void format(LieferscheinpositionDto posDto, TheClientDto theClientDto) {
			valueMap = new HashMap<String, Object>();
//			valueMap.put(keyLieferscheinCnr, lsDto.getCNr().replace("/", "0"));
			// PJ21114
//			valueMap.put(keyLieferscheinCnr, lsDto.getCNr().replace("/", "1"));

			prepareArtikel(posDto.getArtikelIId(), theClientDto);
			preparePosition(posDto, theClientDto);
		}

		protected void prepareArtikel(Integer artikelId, TheClientDto theClientDto) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelId, theClientDto);
			valueMap.put(keyArtikelKBez, emptyField(artikelDto.getKbezAusSpr(), artikelDto.getCNr()));
			valueMap.put(keyArtikelCnr, artikelDto.getCNr());
		}

		protected void preparePosition(LieferscheinpositionDto posDto, TheClientDto theClientDto) {
			if (posDto.getForecastpositionIId() == null) {
				preparePurePosition(posDto, theClientDto);
			} else {
				prepareLinienabrufPosition(posDto, theClientDto);
			}
		}

		private String getBestellnummer(String bestellnummer) {
			return Helper.isStringEmpty(bestellnummer) ? versandwegPartnerDto.getcBestellnummer()
					: bestellnummer.trim();
		}

		protected void preparePurePosition(LieferscheinpositionDto posDto, TheClientDto theClientDto) {
			valueMap.put(keyBestellnummer, emptyField(getBestellnummer(lsDto.getCBestellnummer())));
			valueMap.put(keyMenge, posDto.getNMenge());
			valueMap.put(keyDatum, valueEmptyDatum);
			emit();
		}

		protected void prepareLinienabrufPosition(LieferscheinpositionDto posDto, TheClientDto theClientDto) {
			Forecastposition fcpos = em.find(Forecastposition.class, posDto.getForecastpositionIId());
			List<LinienabrufDto> abrufDtos = getForecastFac().linienabrufFindByForecastpositionId(fcpos.getIId());
			if (abrufDtos.size() == 0) {
				prepareEmptyLinienabrufPosition(fcpos, posDto, theClientDto);
			} else {
				prepareLinienAbrufe(abrufDtos, posDto, theClientDto);
			}
		}

		protected void prepareEmptyLinienabrufPosition(Forecastposition fcpos, LieferscheinpositionDto posDto,
				TheClientDto theClientDto) {
			valueMap.put(keyBestellnummer, emptyField(getBestellnummer(fcpos.getCBestellnummer())));
			valueMap.put(keyMenge, posDto.getNMenge());
			valueMap.put(keyDatum, valueEmptyDatum);
			emit();
		}

		private int findStartIndex(List<LinienabrufDto> abrufDtos, BigDecimal summeGeliefert) {
			BigDecimal summeVorigeAbrufe = BigDecimal.ZERO;
			for (int i = 0; i < abrufDtos.size(); i++) {
				LinienabrufDto abrufDto = abrufDtos.get(i);
				summeVorigeAbrufe = summeVorigeAbrufe.add(abrufDto.getNMenge());
				if (summeGeliefert.compareTo(summeVorigeAbrufe) <= 0) {
					return i;
				}
			}

			// es wurde mehr geliefert als per Linienabruf gefordert
			return abrufDtos.size();
		}

		protected void prepareLinienAbrufe(List<LinienabrufDto> abrufDtos, LieferscheinpositionDto posDto,
				TheClientDto theClientDto) {
			BigDecimal summeGeliefert = getForecastFac().getBereitsGelieferteMenge(posDto.getForecastpositionIId());
			summeGeliefert = summeGeliefert.subtract(posDto.getNMenge());
			int startIndex = findStartIndex(abrufDtos, summeGeliefert);

			BigDecimal summeAbruf = summeGeliefert;
			for (int i = startIndex; i < abrufDtos.size(); i++) {
				LinienabrufDto abrufDto = abrufDtos.get(i);
				BigDecimal menge = abrufDto.getNMenge();
				BigDecimal sum = summeAbruf.add(menge);
				if (sum.compareTo(posDto.getNMenge()) > 0) {
					menge = posDto.getNMenge().subtract(summeAbruf);
				}

				valueMap.put(keyBestellnummer, emptyField(abrufDto.getCBestellnummer()));
				valueMap.put(keyDatum, new Date(abrufDto.getTProduktionstermin().getTime()));
				valueMap.put(keyMenge, menge);
				emit();

				summeAbruf = summeAbruf.add(menge);
				if (summeAbruf.compareTo(posDto.getNMenge()) == 0) {
					break;
				}
			}
		}

		protected void emit() {
			String s = versandwegPartnerDto.getCDatenzeile();
			s = s.replace("|Artikel.KBez|", keyArtikelKBez);
			s = s.replace("|Artikel.CNr|", keyArtikelCnr);
			s = s.replace("|Linienabruf.CBestellnummer|", keyBestellnummer);
			s = s.replace("|Linienabruf.Produktionstermin|", keyDatum);
			s = s.replace("|Linienabruf.NMenge|", keyMenge);
			s = s.replace("|Lieferschein.CNr|", keyLieferscheinCnr);
			s = replaceNullDateFormat(s);
			s = String.format(s, valueMap.get(keyArtikelKBez), valueMap.get(keyArtikelCnr),
					valueMap.get(keyBestellnummer), valueMap.get(keyDatum), valueMap.get(keyMenge),
					valueMap.get(keyLieferscheinCnr));
			s = s.replace("\\n", new String(new byte[] { 10 }));
			s = s.replace("\\r", new String(new byte[] { 13 }));
			aviso.addLine(s);
		}

		private String replaceNullDateFormat(String s) {
			if (valueEmptyDatum.equals(valueMap.get(keyDatum))) {
				s = s.replace("$tm", "$-2.2s");
				s = s.replace("$ty", "$-2.2s");
				s = s.replace("$td", "$-2.2s");
			}

			return s;
		}

		private String emptyField(String value) {
			return value == null ? "" : value;
		}

		private String emptyField(String value, String defaultValue) {
			return value == null ? (defaultValue == null ? "" : defaultValue) : value;
		}
	}

	public class LieferscheinAvisoProducerLinienabruf implements ILieferscheinAvisoProducer {
		private Integer versandwegId;
		private Integer receiverKundeId;

		public LieferscheinAvisoProducerLinienabruf(Integer versandwegId, Integer receiverKundeId) {
			this.versandwegId = versandwegId;
			this.receiverKundeId = receiverKundeId;
		}

		@Override
		public Integer versandwegId() {
			return versandwegId;
		}

		@Override
		public Integer receiverKundeId() {
			return receiverKundeId;
		}

		@Override
		public boolean isDummy() {
			return false;
		}

		@Override
		public ILieferscheinAviso createAviso(LieferscheinDto lieferscheinDto, TheClientDto theClientDto)
				throws RemoteException, NamingException, EJBExceptionLP {
			LieferscheinAvisoLinienabruf aviso = new LieferscheinAvisoLinienabruf();
			aviso.setLieferscheinDto(lieferscheinDto);
			aviso.setVersandwegId(versandwegId());

			Kunde kunde = em.find(Kunde.class, receiverKundeId);
			aviso.setPartnerId(kunde.getPartnerIId());

			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac().versandwegPartnerFindByVersandwegIdPartnerId(
					aviso.getVersandwegId(), aviso.getPartnerId(), theClientDto.getMandant());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						aviso.getPartnerId().toString());
			}

			VersandwegPartnerLinienabrufDto linienabrufPartnerDto = (VersandwegPartnerLinienabrufDto) versandwegPartnerDto;

			Collection<LieferscheinpositionDto> posDtos = getLSPositionsDto(lieferscheinDto, theClientDto);
			createHeaderInfo(aviso, linienabrufPartnerDto, posDtos, theClientDto);
			createPositionInfo(aviso, linienabrufPartnerDto, posDtos, theClientDto);

			aviso.persist();

			return aviso;
		}

		@Override
		public void archiveAviso(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			LieferscheinDto lsDto = aviso.getLieferscheinDto();
			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);

			JCRDocDto jcrDocDto = new JCRDocDto();
			DocPath dp = new DocPath(new DocNodeLieferschein(lsDto))
					.add(new DocNodeFile("Lieferaviso_Linienabruf.txt"));
			jcrDocDto.setDocPath(dp);
			jcrDocDto.setbData(aviso.toString().getBytes());
			jcrDocDto.setbVersteckt(false);
			jcrDocDto.setlAnleger(partnerDto.getIId());

			Integer kundeId = lsDto.getKundeIIdLieferadresse();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

			jcrDocDto.setlPartner(kundeDto.getPartnerIId());
			jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
			jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
			jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
			jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
			jcrDocDto.setsBelegnummer(lsDto.getCNr().replace("/", "."));
			jcrDocDto.setsFilename(lsDto.getCNr().replace("/", "."));
			jcrDocDto.setsMIME(".txt");
			jcrDocDto.setsName("Lieferaviso Linienabruf" + lsDto.getIId());
			jcrDocDto.setsRow(lsDto.getIId().toString());
			jcrDocDto.setsTable("LIEFERSCHEIN");
			String sSchlagworte = "Export Linienabruf Aviso";
			jcrDocDto.setsSchlagworte(sSchlagworte);
			getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, theClientDto);
		}

		private String formatHeader(String headerFormat) {
			String s = headerFormat;
			s = s.replace("\\n", new String(new byte[] { 10 }));
			s = s.replace("\\r", new String(new byte[] { 13 }));
			return s;
		}

		private LieferscheinAvisoLinienabruf createHeaderInfo(LieferscheinAvisoLinienabruf aviso,
				VersandwegPartnerLinienabrufDto versandwegPartnerDto, Collection<LieferscheinpositionDto> posDtos,
				TheClientDto theClientDto) throws RemoteException {

			aviso.addHeader(formatHeader(versandwegPartnerDto.getCKopfzeile()));
			return aviso;
		}

		private void createPositionInfo(LieferscheinAvisoLinienabruf aviso,
				VersandwegPartnerLinienabrufDto versandwegPartnerDto, Collection<LieferscheinpositionDto> posDtos,
				TheClientDto theClientDto) {
			for (LieferscheinpositionDto posDto : posDtos) {
				createAbrufInfo(aviso, versandwegPartnerDto, posDto, theClientDto);
			}
		}

		private void createAbrufInfo(LieferscheinAvisoLinienabruf aviso,
				VersandwegPartnerLinienabrufDto versandwegPartnerDto, LieferscheinpositionDto posDto,
				TheClientDto theClientDto) {
			LinienabrufFormatter formatter = new LinienabrufFormatter(aviso, versandwegPartnerDto, theClientDto);
			formatter.format(posDto, theClientDto);
		}

		private Collection<LieferscheinpositionDto> getLSPositionsDto(LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			Collection<LieferscheinpositionDto> ejbPositions = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(lieferscheinDto.getIId(), theClientDto);
			CollectionUtils.filter(ejbPositions, new LieferscheinpositionenNurIdentFilter());
			return ejbPositions;
		}

		private boolean isMyInstance(ILieferscheinAviso aviso) {
			return aviso instanceof LieferscheinAvisoLinienabruf;
		}

		@Override
		public void postAviso(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			if (!isMyInstance(aviso)) {
				return;
			}

			IVersandwegPartnerDto versandwegPartnerDto = getSystemFac().versandwegPartnerFindByVersandwegIdPartnerId(
					aviso.getVersandwegId(), aviso.getPartnerId(), theClientDto.getMandant());
			if (versandwegPartnerDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
						aviso.getPartnerId().toString());
			}

			VersandwegPartnerLinienabrufDto abrufPartnerDto = (VersandwegPartnerLinienabrufDto) versandwegPartnerDto;
			createAvisoFile((LieferscheinAvisoLinienabruf) aviso, abrufPartnerDto, theClientDto);
		}

		private void createAvisoFile(LieferscheinAvisoLinienabruf aviso,
				VersandwegPartnerLinienabrufDto versandwegPartnerDto, TheClientDto theClientDto) {

			File f = new File(versandwegPartnerDto.getCExportPfad());
			if (f.isDirectory()) {
				String basefilename = "ls_" + aviso.getLieferscheinDto().getCNr().replaceAll("/", "_");
				String filename = basefilename + ".txt";
				try {
					String path = versandwegPartnerDto.getCExportPfad() + "/" + filename;
					writeFile(path, aviso.toString());
				} catch (IOException e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EDIFACT_EXPORTDATEI_NICHT_ERSTELLBAR, e);
				}
			} else {
				String msg = "Edifact Exportpath '" + versandwegPartnerDto.getCExportPfad()
						+ "' existiert nicht, bzw. ist kein Verzeichnis!";
				myLogger.warn(msg);
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EDIFACT_EXPORTVERZEICHNIS_NICHT_VORHANDEN, msg,
						versandwegPartnerDto.getCExportPfad());
			}
		}

		private void writeFile(String filename, String content) throws IOException {
			Writer writer = null;

			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
				writer.write(content);
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (Exception ex) {
				}
			}
		}

		@Override
		public String toString(ILieferscheinAviso aviso, TheClientDto theClientDto) {
			if (!isMyInstance(aviso)) {
				return null;
			}

			return aviso.toString();
		}
	}

	private class LieferscheinAvisoFactory {
		private ILieferscheinAvisoProducer getProducerCC(LieferscheinDto lieferscheinDto, TheClientDto theClientDto)
				throws RemoteException {
			KundeDto kundeDto = getAuftragsKunde(lieferscheinDto, theClientDto);
//			Integer kundeId = null;
//			if (lieferscheinDto.getAuftragIId() != null) {
//				Auftrag auftrag = em.find(Auftrag.class, lieferscheinDto.getAuftragIId());
//				kundeId = auftrag.getKundeIIdAuftragsadresse();
//			} else {
//				kundeId = lieferscheinDto.getKundeIIdLieferadresse();
//			}
			Integer kundeId = kundeDto.getIId();
			Integer versandwegId = getVersandwegIdFor(kundeId, VersandwegType.CleverCureVerkauf, theClientDto);
			return versandwegId == null ? null : new LieferscheinAvisoProducerCC(versandwegId, kundeId);
			// return new LieferscheinAvisoProducerCCSelfSender() ;
		}

		private ILieferscheinAvisoProducer getProducerEdifact(LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			KundeDto kundeDto = getAuftragsKunde(lieferscheinDto, theClientDto);
//			Integer kundeId = null;
//			if (lieferscheinDto.getAuftragIId() != null) {
//				Auftrag auftrag = em.find(Auftrag.class, lieferscheinDto.getAuftragIId());
//				kundeId = auftrag.getKundeIIdAuftragsadresse();
//			} else {
//				kundeId = lieferscheinDto.getKundeIIdLieferadresse();
//			}
//
			Integer kundeId = kundeDto.getIId();
			Integer versandwegId = getVersandwegIdFor(kundeId, VersandwegType.Edi4AllDesadv, theClientDto);
			if (versandwegId != null)
				return new LieferscheinAvisoProducerEdi4All(versandwegId, kundeId);

			versandwegId = getVersandwegIdFor(kundeId, VersandwegType.EdiDesadv, theClientDto);
			return versandwegId == null ? null : new LieferscheinAvisoProducerDesAdv(versandwegId, kundeId);
		}

		private ILieferscheinAvisoProducer getProducerEdi4All(LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			Integer versandwegId = getVersandwegIdFor(lieferscheinDto.getKundeIIdLieferadresse(),
					VersandwegType.Edi4AllDesadv, theClientDto);
			return versandwegId == null ? null
					: new LieferscheinAvisoProducerEdi4All(versandwegId, lieferscheinDto.getKundeIIdLieferadresse());
		}

		private ILieferscheinAvisoProducer getProducerLinienabruf(LieferscheinDto lieferscheinDto,
				TheClientDto theClientDto) throws RemoteException {
			Integer versandwegId = getVersandwegIdFor(lieferscheinDto.getKundeIIdLieferadresse(),
					VersandwegType.Linienabruf, theClientDto);
			return versandwegId == null ? null
					: new LieferscheinAvisoProducerLinienabruf(versandwegId,
							lieferscheinDto.getKundeIIdLieferadresse());
		}

		public ILieferscheinAvisoProducer getProducer(LieferscheinDto lieferscheinDto, TheClientDto theClientDto)
				throws RemoteException, NamingException {
			ILieferscheinAvisoProducer producer = getProducerCC(lieferscheinDto, theClientDto);
			if (producer != null)
				return producer;

			producer = getProducerEdi4All(lieferscheinDto, theClientDto);
			if (producer != null)
				return producer;

			producer = getProducerEdifact(lieferscheinDto, theClientDto);
			if (producer != null)
				return producer;

			producer = getProducerLinienabruf(lieferscheinDto, theClientDto);
			if (producer != null)
				return producer;

//			return new LieferscheinAvisoProducerDummy();
			return null;
		}
	}

	private LieferscheinAvisoFactory avisoFactory = new LieferscheinAvisoFactory();

	private Integer getVersandwegIdFor(Integer kundeId, String versandwegCnr, TheClientDto theClientDto)
			throws RemoteException {
		VersandwegPartner versandwegPartner = getVersandwegPartner(kundeId, versandwegCnr, theClientDto);
		return versandwegPartner == null ? null : versandwegPartner.getVersandwegId();
	}

	private VersandwegPartner getVersandwegPartner(Integer kundeId, String versandwegCnr, TheClientDto theClientDto) {
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);
		VersandwegPartner versandwegPartner = VersandwegPartnerQuery.findByPartnerIIdVersandwegCnr(em,
				kundeDto.getPartnerIId(), versandwegCnr, theClientDto.getMandant());

		return versandwegPartner;
	}

	public ILieferscheinAviso createLieferscheinAviso(Integer lieferscheinIId, TheClientDto theClientDto)
			throws NamingException, RemoteException {
		Validator.notNull(lieferscheinIId, "lieferscheinIId");
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notEmpty(theClientDto.getMandant(), "theClient.mandant");

		LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(lieferscheinIId);
		if (!lieferscheinDto.getMandantCNr().equals(theClientDto.getMandant())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FALSCHER_MANDANT, lieferscheinDto.getIId().toString());
		}

		if (lieferscheinDto.getTLieferaviso() != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_AVISO_BEREITS_DURCHGEFUEHRT,
					lieferscheinDto.getTLieferaviso().toString());
		}

		ILieferscheinAvisoProducer avisoProducer = avisoFactory.getProducer(lieferscheinDto, theClientDto);
		if (null == avisoProducer) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_NICHT_UNTERSTUETZT, "");
		}

		ILieferscheinAviso aviso = avisoProducer.createAviso(lieferscheinDto, theClientDto);
		return aviso;
	}

	private void updateAvisoTimestamp(Integer lieferscheinIId, TheClientDto theClientDto) {
		Lieferschein ls = em.find(Lieferschein.class, lieferscheinIId);
		ls.setTLieferaviso(getTimestamp());
		ls.setPersonalIIdLieferaviso(theClientDto.getIDPersonal());
		em.merge(ls);
		em.flush();
	}

	public void resetLieferscheinAviso(Integer lieferscheinIId, TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(lieferscheinIId, "lieferscheinIId");
		Validator.notNull(theClientDto, "theClientDto");

		Lieferschein ls = em.find(Lieferschein.class, lieferscheinIId);
		if (ls == null)
			return;

		if (!ls.getMandantCNr().equals(theClientDto.getMandant())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FALSCHER_MANDANT, lieferscheinIId.toString());
		}

		ls.setTLieferaviso(null);
		ls.setPersonalIIdLieferaviso(theClientDto.getIDPersonal());
		em.merge(ls);
		em.flush();
	}

	private String lieferscheinAvisoToStringImpl(ILieferscheinAviso aviso, LieferscheinDto lieferscheinDto,
			TheClientDto theClientDto) throws RemoteException, NamingException {
		ILieferscheinAvisoProducer producer = avisoFactory.getProducer(lieferscheinDto, theClientDto);
		return producer.toString(aviso, theClientDto);
	}

	public String lieferscheinAvisoToString(LieferscheinDto lieferscheinDto, ILieferscheinAviso lieferscheinAviso,
			TheClientDto theClientDto) throws RemoteException, NamingException {
		Validator.notNull(lieferscheinDto, "lieferscheinDto");
		Validator.notNull(lieferscheinAviso, "lieferscheinAviso");
		Validator.notNull(theClientDto, "theClientDto");

		return lieferscheinAvisoToStringImpl(lieferscheinAviso, lieferscheinDto, theClientDto);
	}

	public String createLieferscheinAvisoToString(Integer lieferscheinIId, TheClientDto theClientDto)
			throws RemoteException, NamingException {
		ILieferscheinAviso aviso = createLieferscheinAviso(lieferscheinIId, theClientDto);
		if (aviso == null)
			return null;
		LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(lieferscheinIId);
		return aviso == null ? null : lieferscheinAvisoToStringImpl(aviso, lieferscheinDto, theClientDto);
	}

	private void archiveAvisoDocument(LieferscheinDto lieferscheinDto, String xmlContent, TheClientDto theClientDto) {
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocPath dp = new DocPath(new DocNodeLieferschein(lieferscheinDto))
				.add(new DocNodeFile("Lieferaviso_Clevercure.xml"));
		jcrDocDto.setDocPath(dp);
		jcrDocDto.setbData(xmlContent.getBytes());
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(partnerDto.getIId());

		Integer kundeId = lieferscheinDto.getKundeIIdLieferadresse();
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

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

	public String createLieferscheinAvisoPost(Integer lieferscheinIId, TheClientDto theClientDto)
			throws RemoteException, NamingException, EJBExceptionLP {
		ILieferscheinAviso aviso = createLieferscheinAviso(lieferscheinIId, theClientDto);
		if (aviso == null)
			return null;

		LieferscheinDto lieferscheinDto = aviso.getLieferscheinDto();
		ILieferscheinAvisoProducer producer = avisoFactory.getProducer(lieferscheinDto, theClientDto);
		producer.postAviso(aviso, theClientDto);

		// Besser Zeitstempel ist gesetzt, und dafuer ev. nicht archiviert
		updateAvisoTimestamp(lieferscheinIId, theClientDto);

		producer.archiveAviso(aviso, theClientDto);

		return producer.toString(aviso, theClientDto);
	}

	public boolean hatLieferscheinVersandweg(LieferscheinDto lieferscheinDto, TheClientDto theClientDto)
			throws RemoteException {
		Validator.notNull(lieferscheinDto, "lieferscheinDto");
		boolean hasVersandweg = getKundeFac().hatKundeVersandweg(lieferscheinDto.getKundeIIdLieferadresse(),
				LocaleFac.BELEGART_LIEFERSCHEIN, theClientDto);
		if (!hasVersandweg && lieferscheinDto.getAuftragIId() != null) {
			Auftrag auftrag = em.find(Auftrag.class, lieferscheinDto.getAuftragIId());
			if (auftrag != null) {
				hasVersandweg = getKundeFac().hatKundeVersandweg(auftrag.getKundeIIdAuftragsadresse(), theClientDto);
			}
		}
		return hasVersandweg;
	}

	public boolean hatLieferscheinVersandweg(Integer lieferscheinIId, TheClientDto theClientDto)
			throws RemoteException {
		Validator.notNull(lieferscheinIId, "lieferscheinIId");
		LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKeyOhneExc(lieferscheinIId);
		return hatLieferscheinVersandweg(lieferscheinDto, theClientDto);
	}

	public BelegPruefungDto aktiviereBelegControlled(Integer iid, Timestamp t, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BelegPruefungDto pruefungDto = pruefeBeleg(new ILSPruefungFunction() {
			@Override
			public BelegPruefungDto process(Integer iid, Timestamp t, TheClientDto theClientDto)
					throws EJBExceptionLP, RemoteException {
				return belegAktivierungFac.aktiviereBelegControlled(LieferscheinPruefbarFacBean.this, iid, t,
						theClientDto);
			}
		}, iid, t, theClientDto);
		return pruefungDto;

		// BelegPruefungDto pruefungDto =
		// belegAktivierungFac.aktiviereBelegControlled(this, iid, t,
		// theClientDto) ;
		//
		// boolean bUst = pruefungDto.isKundeHatUstAberNichtUstPositionen() ;
		// boolean bNichtUst = pruefungDto.isKundeHatKeineUstAberUstPositionen()
		// ;
		//
		// for (VerkettetDto verkettetDto :
		// getLieferscheinServiceFac().verkettetFindByLieferscheinIId(iid)) {
		// BelegPruefungDto subBelegPruefungDto =
		// belegAktivierungFac.aktiviereBelegControlled(this,
		// verkettetDto.getLieferscheinIIdVerkettet(), t, theClientDto) ;
		//
		// bUst |= subBelegPruefungDto.isKundeHatUstAberNichtUstPositionen() ;
		// bNichtUst |=
		// subBelegPruefungDto.isKundeHatKeineUstAberUstPositionen() ;
		// }
		// pruefungDto.setKundeHatUstAberNichtUstPositionen(bUst);
		// pruefungDto.setKundeHatKeineUstAberUstPositionen(bNichtUst);
		// return pruefungDto ;

		// ArrayList<Integer> alLs = new ArrayList<Integer>();
		// alLs.add(iid);
		//
		// for (VerkettetDto vDto : lsDtosVerketet) {
		// alLs.add(vDto.getLieferscheinIIdVerkettet());
		// }
		//
		// for (Integer lieferscheinIId : alLs) {
		//
		// new BelegAktivierungController(this).aktiviereBelegControlled(
		// lieferscheinIId, t, theClientDto);
		// }
	}

	public BelegPruefungDto berechneBelegControlled(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BelegPruefungDto pruefungDto = pruefeBeleg(new ILSPruefungFunction() {
			@Override
			public BelegPruefungDto process(Integer iid, Timestamp t, TheClientDto theClientDto)
					throws EJBExceptionLP, RemoteException {
				return belegAktivierungFac.berechneBelegControlled(LieferscheinPruefbarFacBean.this, iid, theClientDto);
			}
		}, iid, null, theClientDto);
		return pruefungDto;

		// BelegPruefungDto pruefungDto =
		// belegAktivierungFac.berechneBelegControlled(this, iid, theClientDto)
		// ;
		// boolean bUst = pruefungDto.isKundeHatUstAberNichtUstPositionen() ;
		// boolean bNichtUst = pruefungDto.isKundeHatKeineUstAberUstPositionen()
		// ;
		//
		// for (VerkettetDto verkettetDto :
		// getLieferscheinServiceFac().verkettetFindByLieferscheinIId(iid)) {
		// BelegPruefungDto subBelegPruefungDto =
		// belegAktivierungFac.berechneBelegControlled(this,
		// verkettetDto.getLieferscheinIIdVerkettet(), theClientDto) ;
		//
		// bUst |= subBelegPruefungDto.isKundeHatUstAberNichtUstPositionen() ;
		// bNichtUst |=
		// subBelegPruefungDto.isKundeHatKeineUstAberUstPositionen() ;
		// }
		// pruefungDto.setKundeHatUstAberNichtUstPositionen(bUst);
		// pruefungDto.setKundeHatKeineUstAberUstPositionen(bNichtUst);
		// return pruefungDto ;
		//
		// VerkettetDto[] lsDtosVerketet = getLieferscheinServiceFac()
		// .verkettetFindByLieferscheinIId(iid);
		//
		// ArrayList<Integer> alLs = new ArrayList<Integer>();
		// alLs.add(iid);
		//
		// for (VerkettetDto vDto : lsDtosVerketet) {
		// alLs.add(vDto.getLieferscheinIIdVerkettet());
		// }
		//
		// Timestamp ts = null;
		//
		// for (Integer lieferscheinIId : alLs) {
		//
		// Timestamp tsTemp = new BelegAktivierungController(this)
		// .berechneBelegControlled(lieferscheinIId, theClientDto);
		//
		// if (ts == null) {
		// ts = tsTemp;
		// }
		// }
		//
		// return ts;
	}

	@Override
	public List<Timestamp> getAenderungsZeitpunkte(Integer iid) throws EJBExceptionLP, RemoteException {
		LieferscheinDto l = lieferscheinFindByPrimaryKey(iid);
		List<Timestamp> timestamps = new ArrayList<Timestamp>();
		timestamps.add(l.getTAendern());
		timestamps.add(l.getTStorniert());
		timestamps.add(l.getTManuellerledigt());
		return timestamps;
	}

	// PJ2276
	public void repairLieferscheinZws2276(Integer lieferscheinId, TheClientDto theClientDto) throws RemoteException {
		LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(lieferscheinId);
		if (LieferscheinFac.LSSTATUS_STORNIERT.equals(lieferscheinDto.getStatusCNr()))
			return;

		LieferscheinpositionDto[] lsPosDto = getLieferscheinpositionFac()
				.lieferscheinpositionFindByLieferscheinIId(lieferscheinDto.getIId());
		getBelegVerkaufFac().prepareIntZwsPositions(lsPosDto, lieferscheinDto);
		Set<Integer> modifiedPositions = getBelegVerkaufFac().adaptIntZwsPositions(lsPosDto);
		for (Integer index : modifiedPositions) {
			Lieferscheinposition lieferscheinposition = em.find(Lieferscheinposition.class, lsPosDto[index].getIId());
			lieferscheinposition.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
					lsPosDto[index].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			em.merge(lieferscheinposition);
			em.flush();

			if (LocaleFac.POSITIONSART_IDENT.equals(lieferscheinposition.getLieferscheinpositionartCNr())) {
				LieferscheinpositionDto lieferscheinpositionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByPrimaryKey(lieferscheinposition.getIId(), theClientDto);
				if (lieferscheinpositionDto.getNMenge().signum() > 0) {
					getLieferscheinpositionFac().bucheAbLager(lieferscheinpositionDto, theClientDto);
				} else {
					if (lieferscheinpositionDto.getNMenge().signum() < 0) {
						getLieferscheinpositionFac().bucheZuLager(lieferscheinpositionDto, theClientDto);
					}
				}
			}
		}
	}

	public List<Integer> repairLieferscheinZws2276GetList(TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT DISTINCT lieferschein_i_id "
				+ " FROM FLRLieferscheinposition AS lspos WHERE lspos.positionsart_c_nr = 'IZwischensumme'";

		org.hibernate.Query lieferscheinIdsQuery = session.createQuery(sQuery);

		List<Integer> resultList = new ArrayList<Integer>();
		List<Integer> queryList = (List<Integer>) lieferscheinIdsQuery.list();
		for (Integer lieferschein_i_id : queryList) {
			Lieferschein lieferschein = em.find(Lieferschein.class, lieferschein_i_id);
			if (theClientDto.getMandant().equals(lieferschein.getMandantCNr())) {
				resultList.add(lieferschein_i_id);
			}
		}

		session.close();
		return resultList;
	}

	interface ILSPruefungFunction {
		BelegPruefungDto process(Integer id, Timestamp t, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException;
	}

	private BelegPruefungDto pruefeBeleg(ILSPruefungFunction functor, Integer belegId, Timestamp t,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BelegPruefungDto pruefungDto = functor.process(belegId, t, theClientDto);

		boolean bUst = pruefungDto.isKundeHatUstAberNichtUstPositionen();
		boolean bNichtUst = pruefungDto.isKundeHatKeineUstAberUstPositionen();

		for (VerkettetDto verkettetDto : getLieferscheinServiceFac().verkettetFindByLieferscheinIId(belegId)) {
			BelegPruefungDto subBelegPruefungDto = functor.process(verkettetDto.getLieferscheinIIdVerkettet(), t,
					theClientDto);

			bUst |= subBelegPruefungDto.isKundeHatUstAberNichtUstPositionen();
			bNichtUst |= subBelegPruefungDto.isKundeHatKeineUstAberUstPositionen();
		}
		pruefungDto.setKundeHatUstAberNichtUstPositionen(bUst);
		pruefungDto.setKundeHatKeineUstAberUstPositionen(bNichtUst);
		return pruefungDto;
	}

	public BelegPruefungDto berechneAktiviereBelegControlled(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BelegPruefungDto pruefungDto = pruefeBeleg(new ILSPruefungFunction() {
			@Override
			public BelegPruefungDto process(Integer iid, Timestamp t, TheClientDto theClientDto)
					throws EJBExceptionLP, RemoteException {
				return belegAktivierungFac.berechneAktiviereControlled(LieferscheinPruefbarFacBean.this, iid,
						theClientDto);
			}
		}, iid, null, theClientDto);
		return pruefungDto;
		//
		// BelegPruefungDto pruefungDto =
		// belegAktivierungFac.berechneAktiviereControlled(this, iid,
		// theClientDto) ;
		//
		// boolean bUst = pruefungDto.isKundeHatUstAberNichtUstPositionen() ;
		// boolean bNichtUst = pruefungDto.isKundeHatKeineUstAberUstPositionen()
		// ;
		//
		// for (VerkettetDto verkettetDto :
		// getLieferscheinServiceFac().verkettetFindByLieferscheinIId(iid)) {
		// BelegPruefungDto subBelegPruefungDto =
		// belegAktivierungFac.berechneAktiviereControlled(this,
		// verkettetDto.getLieferscheinIIdVerkettet(), theClientDto) ;
		//
		// bUst |= subBelegPruefungDto.isKundeHatUstAberNichtUstPositionen() ;
		// bNichtUst |=
		// subBelegPruefungDto.isKundeHatKeineUstAberUstPositionen() ;
		// }
		// pruefungDto.setKundeHatUstAberNichtUstPositionen(bUst);
		// pruefungDto.setKundeHatKeineUstAberUstPositionen(bNichtUst);
		// return pruefungDto ;

		// new BelegAktivierungController(this).berechneAktiviereControlled(iid,
		// theClientDto);
	}

	@Override
	public BelegDto getBelegDto(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BelegDto belegDto = lieferscheinFindByPrimaryKey(iid);
		belegDto.setBelegartCNr(LocaleFac.BELEGART_LIEFERSCHEIN);
		return belegDto;
	}

	@Override
	public BelegpositionDto[] getBelegPositionDtos(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return getLieferscheinpositionFac().lieferscheinpositionFindByLieferscheinIId(iid);
	}

	@Override
	public Integer getKundeIdDesBelegs(BelegDto belegDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return ((LieferscheinDto) belegDto).getKundeIIdRechnungsadresse();
	}

	public LieferscheinDto setupDefaultLieferschein(KundeDto kundeDto, TheClientDto theClientDto)
			throws RemoteException {
		LieferscheinDto lsDto = new LieferscheinDto();

		lsDto.setTBelegdatum(Helper.cutTimestamp(getTimestamp()));
		lsDto.setStatusCNr(LieferscheinFac.LSSTATUS_ANGELEGT);
		lsDto.setLieferscheinartCNr(LieferscheinFac.LSART_FREI);
		lsDto.setMandantCNr(kundeDto.getMandantCNr());
		lsDto.setKundeIIdLieferadresse(kundeDto.getIId());
		lsDto.setKundeIIdRechnungsadresse(kundeDto.getIId());
		lsDto.setZahlungszielIId(kundeDto.getZahlungszielIId());
		lsDto.setLieferartIId(kundeDto.getLieferartIId());
		lsDto.setSpediteurIId(kundeDto.getSpediteurIId());

		lsDto.setPersonalIIdVertreter(theClientDto.getIDPersonal());
		// lsDto.setCBezProjektbezeichnung("");
		// lsDto.setCBestellnummer("");
		Integer lagerId = kundeDto.getLagerIIdAbbuchungslager();
		if (lagerId == null) {
			lagerId = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();
		}
		lsDto.setLagerIId(lagerId);

		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		WaehrungDto waehrungDto = getLocaleFac().waehrungFindByPrimaryKey(mandantDto.getWaehrungCNr());
		lsDto.setWaehrungCNr(waehrungDto.getCNr());
		lsDto.setFWechselkursmandantwaehrungzubelegwaehrung(1.00);

		PartnerDto partnerDto = kundeDto.getPartnerDto();
		if (partnerDto == null || partnerDto.getIId() == null) {
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
		}

		String localeCnrKommunikation = partnerDto.getLocaleCNrKommunikation();
		lsDto.setLieferscheintextIIdDefaultKopftext(getLieferscheinServiceFac().lieferscheintextFindByMandantLocaleCNr(
				localeCnrKommunikation, MediaFac.MEDIAART_KOPFTEXT, theClientDto).getIId());
		lsDto.setLieferscheintextIIdDefaultFusstext(getLieferscheinServiceFac().lieferscheintextFindByMandantLocaleCNr(
				localeCnrKommunikation, MediaFac.MEDIAART_FUSSTEXT, theClientDto).getIId());

		lsDto.setZiellagerIId(partnerDto.getLagerIIdZiellager());

		KostenstelleDto kostenstellenDtos[] = getSystemFac().kostenstelleFindByMandant(lsDto.getMandantCNr());
		if (kostenstellenDtos.length > 0) {
			lsDto.setKostenstelleIId(kostenstellenDtos[0].getIId());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception("Keine Kostenstelle definiert"));
		}
		return lsDto;
	}

	public Timestamp setzeAuslieferdatumAufJetzt(Integer lieferscheinIId) {
		Validator.pkFieldNotNull(lieferscheinIId, "lieferscheinIId");
		Lieferschein lieferschein = em.find(Lieferschein.class, lieferscheinIId);
		if (lieferschein.getTVersandzeitpunkt() != null) {
			lieferschein.setTLiefertermin(getTimestamp());
			em.merge(lieferschein);
			em.flush();
		}
		return lieferschein.getTLiefertermin();
		// if (!lieferschein.getLieferscheinstatusCNr().equals(
		// LieferscheinFac.LSSTATUS_ANGELEGT)) {
		// lieferschein.setTLiefertermin(new Timestamp(System
		// .currentTimeMillis()));
		// em.merge(lieferschein);
		// em.flush();
		// }
	}

	@Override
	public void pruefeAktivierbarRecht(Integer belegId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		boolean rechtAktivieren = getTheJudgeFac().hatRecht(RechteFac.RECHT_LS_AKTIVIEREN, theClientDto);
		boolean rechtCUD = getTheJudgeFac().hatRecht(RechteFac.RECHT_LS_LIEFERSCHEIN_CUD, theClientDto);
		if (!(rechtAktivieren || rechtCUD)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN,
					"FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN");
		}
	}

	public List<LieferscheinDto> lieferscheinFindByKundeIIdLieferadresseMandantCNrBelegdatumOhneExc(Integer kundeIId,
			String mandantCnr, java.sql.Date belegdatum, TheClientDto theClientDto) {
		List<Lieferschein> entities = LieferscheinQuery.listByKundeIIdLieferadresseMandantCNrBelegdatum(em, kundeIId,
				mandantCnr, belegdatum);

		return entities != null ? Arrays.asList(assembleLieferscheinDtos(entities)) : new ArrayList<LieferscheinDto>();
	}

	public List<LieferscheinDto> lieferscheinFindByKundeIIdLieferadresseMandantCNrStatusCNrOhneExc(Integer kundeIId,
			String mandantCnr, String statusCnr, TheClientDto theClientDto) {
		List<Lieferschein> entities = LieferscheinQuery.listByKundeIIdLieferadresseMandantCNrStatusCNr(em, kundeIId,
				mandantCnr, statusCnr);

		return entities != null ? Arrays.asList(assembleLieferscheinDtos(entities)) : new ArrayList<LieferscheinDto>();
	}

	public void setzeStatusLieferschein(Integer lieferscheinIId, String statusCnr) {
		Validator.notNull(lieferscheinIId, "lieferscheinIId");
		Validator.notEmpty(statusCnr, "statusCnr");

		Lieferschein lieferschein = findLieferschein(lieferscheinIId);
		if (!lieferschein.getLieferscheinstatusCNr().equals(statusCnr)
				&& LieferscheinFac.LSSTATUS_VERRECHNET.equals(lieferschein.getLieferscheinstatusCNr())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, "Lieferschein '" + lieferschein.getCNr()
					+ "' ist bereits auf '" + LieferscheinFac.LSSTATUS_VERRECHNET + "'");
		}

		setzeStatusLieferscheinImpl(lieferscheinIId, statusCnr, null);
	}

	public EasydataImportResult importXMLEasydataStockMovements(String xmlDaten, boolean checkOnly,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		EasydataBeanHolder beanHolder = new EasydataBeanHolder(getArtikelFac(), getLagerFac(), getPersonalFac(),
				getLieferscheinFac(), getLieferscheinpositionFac(), getAnsprechpartnerFac(), getMandantFac(),
				getKundeFac(), getPartnerFac(), getParameterFac());
		IEasydataBeanServices beanServices = new EasydataBeanServices(beanHolder, theClientDto);

		EasydataStockMovementImporter importer = new EasydataStockMovementImporter(beanServices);
		if (checkOnly) {
			return importer.checkXMLDaten(xmlDaten);
		}
		return importer.importXMLDaten(xmlDaten);
	}

	public List<Integer> repairLieferscheinSP6402GetList(TheClientDto theClientDto) throws RemoteException {
		GeschaeftsjahrMandantDto gjDto = getSystemFac().geschaeftsjahrFindByPrimaryKey(2017, theClientDto.getMandant());
		Timestamp ts = gjDto.getDBeginndatum();
		String gjDate = Helper.formatDateWithSlashes(new java.sql.Date(ts.getTime()));
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT DISTINCT lieferschein_i_id " + " FROM FLRLieferscheinposition AS lspos"
				+ " WHERE lspos.position_i_id_artikelset IS NOT NULL"
				+ " AND (lspos.n_nettogesamtpreisplusversteckteraufschlagminusrabatt IS NULL"
				+ "   OR lspos.n_nettogesamtpreisplusversteckteraufschlagminusrabatt = 0)"
				+ " AND lspos.flrlieferschein.d_belegdatum >= '" + gjDate + "'"
				+ " AND lspos.flrlieferschein.mandant_c_nr = '" + theClientDto.getMandant() + "'";

		org.hibernate.Query lieferscheinIdsQuery = session.createQuery(sQuery);

		List<Integer> resultList = new ArrayList<Integer>();
		List<Integer> queryList = (List<Integer>) lieferscheinIdsQuery.list();
		for (Integer lieferschein_i_id : queryList) {
			Lieferschein lieferschein = em.find(Lieferschein.class, lieferschein_i_id);
			if (theClientDto.getMandant().equals(lieferschein.getMandantCNr())) {
				resultList.add(lieferschein_i_id);
			}
		}

		session.close();
		return resultList;
	}

	public void repairLieferscheinSP6402(Integer lieferscheinId, TheClientDto theClientDto) throws RemoteException {
		Collection<LieferscheinpositionDto> positions = getLieferscheinpositionFac()
				.lieferscheinpositionFindByLieferscheinIId(lieferscheinId, theClientDto);
		for (LieferscheinpositionDto posDto : positions) {
			if (posDto.getArtikelIId() == null)
				continue;
			if (!posDto.isIdent())
				continue;

			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(posDto.getArtikelIId(), theClientDto);
			if (stklDto == null)
				continue;

			if (!StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stklDto.getStuecklisteartCNr()))
				continue;

			getLieferscheinpositionFac().preiseEinesArtikelsetsUpdatenLocal(posDto.getIId(), theClientDto);
		}
	}

	protected class ArtikelsetISortRenumber extends TauscheISort<Lieferscheinposition> {
		private List<Lieferscheinposition> artikelsetPositions;

		public ArtikelsetISortRenumber(EntityManager em, Class<Lieferscheinposition> theClass) {
			super(em, theClass);
		}

		@Override
		protected Lieferscheinposition findNextEntityISort(Lieferscheinposition startEntity) {
			return null;
		}

		@Override
		protected Lieferscheinposition findPreviousEntityISort(Lieferscheinposition startEntity) {
			return null;
		}

		@Override
		protected List<Lieferscheinposition> findAllEntitiesISort() {
			return artikelsetPositions;
		}

		@Override
		protected Lieferscheinposition findLastEntityISort() {
			return null;
		}

		public void renumber(Integer lieferscheinId, Integer lsposKopfId) {
			artikelsetPositions = LieferscheinpositionQuery.listByPositionIIdArtikelsetAll(em, lsposKopfId);
			Lieferscheinposition kopfPos = em.find(Lieferscheinposition.class, lsposKopfId);
			artikelsetPositions.add(0, kopfPos);
			List<Lieferscheinposition> allEntities = findAllEntitiesISort();
			Integer newISort = kopfPos.getISort(); // getMinISort();
			Integer maxISort = getMaxISort();

			for (Lieferscheinposition t : allEntities) {
				IISort entry = (IISort) t;
				entry.setISort(newISort);

				getEm().merge(entry);
				newISort++;
			}

			if (newISort > maxISort) {
				myLogger.warn("LieferscheinId '" + lieferscheinId + "' hat maxISort '" + maxISort
						+ "' ueberschritten! (newISort '" + newISort + "')");
			}
			getEm().flush();
		}

		private Integer getMinISort() {
			Integer isort = Integer.MAX_VALUE;
			for (Lieferscheinposition pos : findAllEntitiesISort()) {
				if (pos.getISort() == null) {
					continue;
				}
				if (pos.getISort() < isort) {
					isort = pos.getISort();
				}
			}

			return isort;
		}

		private Integer getMaxISort() {
			Integer isort = -1;
			for (Lieferscheinposition pos : findAllEntitiesISort()) {
				if (pos.getISort() == null) {
					continue;
				}
				if (pos.getISort() > isort) {
					isort = pos.getISort();
				}
			}

			return isort;
		}
	}

	public void repairLieferscheinSP6999(Integer lieferscheinId, TheClientDto theClientDto) throws RemoteException {
		Collection<LieferscheinpositionDto> positions = getLieferscheinpositionFac()
				.lieferscheinpositionFindByLieferscheinIId(lieferscheinId, theClientDto);
		for (LieferscheinpositionDto posDto : positions) {
			if (posDto.getArtikelIId() == null)
				continue;
			if (!posDto.isIdent())
				continue;

			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(posDto.getArtikelIId(), theClientDto);
			if (stklDto == null)
				continue;

			if (!StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stklDto.getStuecklisteartCNr()))
				continue;

			getLieferscheinpositionFac().updateLieferscheinposition(posDto, theClientDto);
		}
	}

	@EJB
	private IPaketVersandFacLocal plcVersand;

	public boolean isPaketEtikettErzeugbar(Integer lieferscheinId, TheClientDto theClientDto) throws RemoteException {
		if (!getMandantFac().hatZusatzfunktionPostPLCVersand(theClientDto)) {
			return false;
		}

		return plcVersand.isLieferscheinVersendbar(new LieferscheinId(lieferscheinId), theClientDto);
	}

	public PaketVersandAntwortDto erzeugePaketInfo(Integer lieferscheinId, TheClientDto theClientDto)
			throws RemoteException {
		if (!getMandantFac().hatZusatzfunktionPostPLCVersand(theClientDto)) {
			return PackageResponseFactory.zusatzfunktionFehlt();
		}

		try {
			LieferscheinId lsId = new LieferscheinId(lieferscheinId);
			PaketVersandAntwortDto paketDto = plcVersand.sendeLieferschein(lsId, theClientDto);
			if (paketDto.isOk()) {
				updateLSMitPaketDaten(lsId, paketDto, theClientDto);
			}
			return paketDto;
		} catch (DatatypeConfigurationException e) {
			myLogger.error("config", e);
			return new PaketVersandAntwortDto(e);
		}
	}

	private void updateLSMitPaketDaten(LieferscheinId lsId, PaketVersandAntwortDto paketDto,
			TheClientDto theClientDto) {
		List<StringBuffer> sbs = new ArrayList<StringBuffer>();
		StringBuffer sb = new StringBuffer();
		int count = 0;
		for (String trackingNumber : paketDto.getTrackingNumbers()) {
			if (sb.length() + trackingNumber.length() > 80) {
				sbs.add(sb);
				sb = new StringBuffer();
			}

			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(trackingNumber);

			if (sbs.size() <= 2) {
				++count;
			}
		}

		sbs.add(sb);

		LieferscheinDto lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(lsId.id());

		if (sbs.size() > 2) {
			myLogger.error("Lieferschein '" + lsDto.getCNr() + "' hat " + paketDto.getTrackingNumbers().size()
					+ " Sendungsnummern. " + "Es konnten nur " + count + " gespeichert werden.");
			myLogger.error("Lieferschein '" + lsDto.getCNr() + "' Sendungsnummern: "
					+ StringUtils.join(paketDto.getTrackingNumbers().iterator(), ","));
		}

		lsDto.setCVersandnummer(sbs.get(0).toString());
		if (sbs.size() > 1) {
			lsDto.setCVersandnummer2(sbs.get(1).toString());
		}
		updateLieferscheinOhneWeitereAktion(lsDto, theClientDto);

		archiveLSPaketInfo(lsDto, paketDto, theClientDto);
	}

	private void archiveLSPaketInfo(LieferscheinDto lsDto, PaketVersandAntwortDto paketDto, TheClientDto theClientDto) {
		List<JCRDocDto> jcrDtos = new ArrayList<JCRDocDto>();
		jcrDtos.add(buildArchivePaketDaten(lsDto, paketDto, theClientDto));
		jcrDtos.add(buildArchivePaketEtikett(lsDto, paketDto, theClientDto));
		getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDtos, theClientDto);
	}

	private JCRDocDto buildArchivePaketDaten(LieferscheinDto lsDto, PaketVersandAntwortDto paketDto,
			TheClientDto theClientDto) {
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocPath dp = new DocPath(new DocNodeLieferschein(lsDto)).add(new DocNodeFile("ShipmentResponse.xml"));
		jcrDocDto.setDocPath(dp);
		jcrDocDto.setbData(paketDto.getRawContent().getBytes());
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(partnerDto.getIId());

		Integer kundeId = lsDto.getKundeIIdLieferadresse();
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

		jcrDocDto.setlPartner(kundeDto.getPartnerIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(lsDto.getCNr().replace("/", "."));
		jcrDocDto.setsFilename(lsDto.getCNr().replace("/", "."));
		jcrDocDto.setsMIME(".txt");
		jcrDocDto.setsName("ShipmentResponse_" + lsDto.getIId());
		jcrDocDto.setsRow(lsDto.getIId().toString());
		jcrDocDto.setsTable("LIEFERSCHEIN");
		String sSchlagworte = "ShipmentResponse Paketversand Trackingnumber Sendungsnummer";
		jcrDocDto.setsSchlagworte(sSchlagworte);
		return jcrDocDto;
	}

	private JCRDocDto buildArchivePaketEtikett(LieferscheinDto lsDto, PaketVersandAntwortDto paketDto,
			TheClientDto theClientDto) {
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocPath dp = new DocPath(new DocNodeLieferschein(lsDto)).add(new DocNodeFile("Paketetiketten.pdf"));
		jcrDocDto.setDocPath(dp);
//		jcrDocDto.setbData(Base64.decodeBase64(paketDto.getPdfContent().getBytes()));
		jcrDocDto.setbData(Base64.getDecoder().decode(paketDto.getPdfContent().getBytes()));
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(partnerDto.getIId());

		Integer kundeId = lsDto.getKundeIIdLieferadresse();
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

		jcrDocDto.setlPartner(kundeDto.getPartnerIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(lsDto.getCNr().replace("/", "."));
		jcrDocDto.setsFilename(lsDto.getCNr().replace("/", "."));
		jcrDocDto.setsMIME(".pdf");
		jcrDocDto.setsName("Paketetikett_" + lsDto.getIId());
		jcrDocDto.setsRow(lsDto.getIId().toString());
		jcrDocDto.setsTable("LIEFERSCHEIN");

		StringBuffer sb = new StringBuffer();
		for (String trackingNumber : paketDto.getTrackingNumbers()) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(trackingNumber);
		}

		String sSchlagworte = "Paketetikett Paketversand Trackingnumber Sendungsnummer " + sb.toString();
		jcrDocDto.setsSchlagworte(sSchlagworte);
		return jcrDocDto;
	}

	public void archiveSignedResponse(BestaetigterLieferscheinDto bestaetigungDto, TheClientDto theClientDto)
			throws RemoteException {
		Validator.notNull(bestaetigungDto, "bestaetigungDto");

		LieferscheinDto lsDto = getLieferscheinFac()
				.lieferscheinFindByPrimaryKey(bestaetigungDto.getLieferscheinId().id());
		List<JCRDocDto> jcrDtos = new ArrayList<JCRDocDto>();
		jcrDtos.add(buildResponseLieferschein(bestaetigungDto, lsDto, theClientDto));
		jcrDtos.add(buildSignedLieferschein(bestaetigungDto, lsDto, theClientDto));
		getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDtos, theClientDto);

		createBestaetigungEmail(bestaetigungDto, lsDto, theClientDto);
	}

	private JCRDocDto buildResponseLieferschein(BestaetigterLieferscheinDto bestaetigungDto, LieferscheinDto lsDto,
			TheClientDto theClientDto) {
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocPath dp = new DocPath(new DocNodeLieferschein(lsDto))
				.add(new DocNodeFile(LieferscheinReportFac.REPORT_LIEFERSCHEIN_UNTERSCHRIFT));
		jcrDocDto.setDocPath(dp);
		jcrDocDto.setbData(bestaetigungDto.getPdf());
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(partnerDto.getIId());

		Integer kundeId = lsDto.getKundeIIdLieferadresse();
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

		jcrDocDto.setlPartner(kundeDto.getPartnerIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(lsDto.getCNr().replace("/", "."));
		jcrDocDto.setsFilename(lsDto.getCNr().replace("/", "."));
		jcrDocDto.setsMIME(".pdf");
		jcrDocDto.setsName("Unterschriebener_Lieferschein_" + lsDto.getIId());
		jcrDocDto.setsRow(lsDto.getIId().toString());
		jcrDocDto.setsTable("LIEFERSCHEIN");

		String sSchlagworte = StringUtils
				.join(new Object[] { lsDto.getCNr(), "Laufnummer", bestaetigungDto.getSerialNumber().toString() }, " ");
		jcrDocDto.setsSchlagworte(sSchlagworte);
		return jcrDocDto;
	}

	private JCRDocDto buildSignedLieferschein(BestaetigterLieferscheinDto bestaetigungDto, LieferscheinDto lsDto,
			TheClientDto theClientDto) {
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocPath dp = new DocPath(new DocNodeLieferschein(lsDto))
				.add(new DocNodeFile(LieferscheinReportFac.REPORT_LIEFERSCHEIN_SIGNATURE));
		jcrDocDto.setDocPath(dp);
		jcrDocDto.setbData(bestaetigungDto.getUnterschrift());
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(partnerDto.getIId());

		Integer kundeId = lsDto.getKundeIIdLieferadresse();
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto);

		jcrDocDto.setlPartner(kundeDto.getPartnerIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_KOPIE_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(lsDto.getCNr().replace("/", "."));
		jcrDocDto.setsFilename(lsDto.getCNr().replace("/", "."));
		jcrDocDto.setsMIME(".png");
		jcrDocDto.setsName("Unterschrift_Lieferschein_" + lsDto.getIId());
		jcrDocDto.setsRow(lsDto.getIId().toString());
		jcrDocDto.setsTable("LIEFERSCHEIN");

		String sSchlagworte = StringUtils.join(new Object[] { lsDto.getCNr(), "Unterschreiber",
				bestaetigungDto.getUnterschreiber(), "Bemerkung", bestaetigungDto.getBemerkung() }, " ");
		jcrDocDto.setsSchlagworte(sSchlagworte);
		return jcrDocDto;
	}

	private void createBestaetigungEmail(BestaetigterLieferscheinDto bestaetigungDto, LieferscheinDto lsDto,
			TheClientDto theClientDto) throws RemoteException {
		HvOptional<String> receiver = getEmailEmpfaenger(lsDto, theClientDto);
		if (!receiver.isPresent()) {
			myLogger.warn("Lieferscheinbestaetigung fuer Lieferschein " + lsDto.getCNr()
					+ " nicht per E-Mail gesendet (kein E-Mail Empfaenger)");
			return;
		}

		MailtextDto txtDto = new MailtextDto();
		txtDto.setParamMandantCNr(theClientDto.getMandant());

		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(lsDto.getKundeIIdLieferadresse(), theClientDto);
		txtDto.setParamLocale(Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation()));

		txtDto.setMailPartnerIId(kundeDto.getPartnerIId());
		txtDto.setMailAnprechpartnerIId(lsDto.getAnsprechpartnerIId());
		PersonalDto personalDtoVertreter = getPersonalFac().personalFindByPrimaryKey(lsDto.getPersonalIIdVertreter(),
				theClientDto);
		txtDto.setMailVertreter(personalDtoVertreter);
		txtDto.setMailBelegdatum(new java.sql.Date(lsDto.getTBelegdatum().getTime()));
		txtDto.setMailBelegnummer(lsDto.getCNr());
		txtDto.setMailProjekt(lsDto.getCBezProjektbezeichnung());
		txtDto.setKundenbestellnummer(lsDto.getCBestellnummer());
		txtDto.setProjektIId(lsDto.getProjektIId());
		if (lsDto.getAuftragIId() != null) {
			txtDto.setAbnummer(getAuftragFac().auftragFindByPrimaryKey(lsDto.getAuftragIId()).getCNr());
		}
		txtDto.setMailFusstext(lsDto.getCLieferscheinFusstextUeberschrieben());
		txtDto.setParamXslFile(LieferscheinReportFac.REPORT_LIEFERSCHEIN_EMAIL);
		txtDto.setParamModul(LieferscheinReportFac.REPORT_MODUL);
		txtDto.setMailText(null);
		txtDto.setAnwesenheitsLfdnr(bestaetigungDto.getSerialNumber());

		txtDto.setMailBetreff(null);

		VersandauftragDto versandDto = new VersandauftragDto();
		versandDto.setCEmpfaenger(receiver.get());
		versandDto.setCBetreff(getVersandFac().getDefaultBetreffForBelegEmail(txtDto, LocaleFac.BELEGART_LIEFERSCHEIN,
				null, txtDto.getParamLocale(), theClientDto));

		txtDto.setMailText(null);
		versandDto.setCText(getVersandFac().getDefaultTextForBelegEmail(txtDto, theClientDto));
		versandDto.setBelegartCNr(null);

		versandDto.setCAbsenderadresse(getAbsenderEmailThrow(theClientDto));

//		VersandauftragDto resultDto = getVersandFac().createVersandauftrag(versandDto, false, theClientDto);

		VersandanhangDto anhangPdfDto = new VersandanhangDto();
		anhangPdfDto.setCDateiname(lsDto.getCNr() + "_lieferscheinbestaetigung.pdf");
		anhangPdfDto.setOInhalt(bestaetigungDto.getPdf());
//		anhangPdfDto.setVersandauftragIId(resultDto.getIId());

		VersandanhangDto anhangSigDto = new VersandanhangDto();
		anhangSigDto.setCDateiname(lsDto.getCNr() + "_unterschrift.png");
		anhangSigDto.setOInhalt(bestaetigungDto.getUnterschrift());

		List<VersandanhangDto> anhaenge = new ArrayList<VersandanhangDto>();
		anhaenge.add(anhangPdfDto);
		anhaenge.add(anhangSigDto);
		getVersandFac().createVersandauftrag(versandDto, anhaenge, false, theClientDto);
	}

	private String getAbsenderEmailThrow(TheClientDto theClientDto) throws RemoteException {
		HvOptional<String> sender = getPersonalFac().getAbsenderEmail(theClientDto);
		if (sender.isPresent())
			return sender.get();

		throw EJBExcFactory.absenderEMailZeitbestaetigungFehlt(
				getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto));
	}

	private HvOptional<String> getEmailEmpfaenger(LieferscheinDto lsDto, TheClientDto theClientDto)
			throws RemoteException {
		HvOptional<String> receiver = HvOptional.empty();
		if (!receiver.isPresent() && lsDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
					.ansprechpartnerFindByPrimaryKey(lsDto.getAnsprechpartnerIId(), theClientDto);

			if (!Helper.isStringEmpty(anspDto.getCEmail())) {
				receiver = HvOptional.of(anspDto.getCEmail().trim());
			}
		}

		if (!receiver.isPresent() && lsDto.getKundeIIdLieferadresse() != null) {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(lsDto.getKundeIIdLieferadresse(), theClientDto);
			if (!Helper.isStringEmpty(kundeDto.getPartnerDto().getCEmail())) {
				receiver = HvOptional.of(kundeDto.getPartnerDto().getCEmail().trim());
			}
		}

		return receiver;
	}

	public void uebersteuereIntelligenteZwischensumme(Integer lieferscheinpositionIId,
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

			LieferscheinpositionDto lpDto = getLieferscheinpositionFac()
					.lieferscheinpositionFindByPrimaryKey(lieferscheinpositionIId, theClientDto);

			LieferscheinDto lsDto = lieferscheinFindByPrimaryKey(lpDto.getBelegIId());

			if (lpDto.getZwsNettoSumme() == null) {
				berechneBeleg(lsDto.getIId(), theClientDto);
				lpDto = getLieferscheinpositionFac().lieferscheinpositionFindByPrimaryKey(lieferscheinpositionIId,
						theClientDto);
			}

			LieferscheinpositionDto[] asDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(lsDto.getIId());

			int iNachkommastellenPreis = getUINachkommastellenPreisVK(theClientDto.getMandant());

			if (lpDto.isIntelligenteZwischensumme() && bdBetragInBelegwaehrungUebersteuert != null
					&& lpDto.getZwsNettoSumme() != null && lpDto.getZwsNettoSumme().doubleValue() != 0) {

				BigDecimal bdSumme = lpDto.getZwsNettoSumme();

				BigDecimal faktor = bdBetragInBelegwaehrungUebersteuert.divide(bdSumme, 10, BigDecimal.ROUND_HALF_EVEN);

				ArrayList<BelegpositionVerkaufDto> alPos = getBelegVerkaufFac().getIntZwsPositions(lpDto, asDtos);

				Integer mwstsatzIIdErstePosition = alPos.get(0).getMwstsatzIId();
				MwstsatzDto mwstsatzDtoErstePosition = getMandantFac()
						.mwstsatzFindByPrimaryKey(mwstsatzIIdErstePosition, theClientDto);

				BigDecimal bdNettosummeNeu = BigDecimal.ZERO;

				for (int i = 0; i < alPos.size(); i++) {

					LieferscheinpositionDto bvDto = (LieferscheinpositionDto) alPos.get(i);

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

						getLieferscheinpositionFac().updateLieferscheinposition(bvDto, theClientDto);

						bdNettosummeNeu = bdNettosummeNeu.add(bdPreisNeu.multiply(bvDto.getNMenge()));
					}

				}

				// Wenn rundungsdifferenz auftritt, Rundungsartikel einfuegen

				BigDecimal diff = bdBetragInBelegwaehrungUebersteuert.subtract(bdNettosummeNeu);

				LieferscheinpositionDto positionDtoRundungsartikel = null;
				for (int i = 0; i < alPos.size(); i++) {
					LieferscheinpositionDto bvDto = (LieferscheinpositionDto) alPos.get(i);
					if (artikelDto.getIId().equals(bvDto.getArtikelIId())) {
						positionDtoRundungsartikel = bvDto;
					}
				}

				if (diff.doubleValue() != 0) {

					if (positionDtoRundungsartikel == null) {
						positionDtoRundungsartikel = new LieferscheinpositionDto();
						positionDtoRundungsartikel.setBelegIId(lsDto.getIId());
						positionDtoRundungsartikel.setPositionsartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
						positionDtoRundungsartikel.setArtikelIId(artikelDto.getIId());
						positionDtoRundungsartikel.setNMenge(BigDecimal.ONE);
						positionDtoRundungsartikel.setEinheitCNr(artikelDto.getEinheitCNr());

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
							.mwstsatzFindZuDatum(mwstsatzDtoErstePosition.getIIMwstsatzbezId(), lsDto.getTBelegdatum());
					positionDtoRundungsartikel.setMwstsatzIId(mwstsatzDto.getIId());
					BigDecimal mwstBetrag = diff.multiply(new BigDecimal(mwstsatzDto.getFMwstsatz()).movePointLeft(2));
					positionDtoRundungsartikel.setNMwstbetrag(mwstBetrag);
					positionDtoRundungsartikel.setNBruttoeinzelpreis(diff.add(mwstBetrag));

					if (positionDtoRundungsartikel.getIId() == null) {

						Integer iSort = lpDto.getISort();

						getLieferscheinpositionFac()
								.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(lsDto.getIId(), iSort);

						positionDtoRundungsartikel.setISort(iSort);

						lpDto = getLieferscheinpositionFac()
								.lieferscheinpositionFindByPrimaryKey(lieferscheinpositionIId, theClientDto);

						Integer agposIIdNeu = getLieferscheinpositionFac()
								.createLieferscheinposition(positionDtoRundungsartikel, false, theClientDto);
						lpDto.setZwsBisPosition(agposIIdNeu);
					} else {
						getLieferscheinpositionFac().updateLieferscheinposition(positionDtoRundungsartikel,
								theClientDto);
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
							getLieferscheinpositionFac().removeLieferscheinposition(positionDtoRundungsartikel,
									theClientDto);
							lpDto.setZwsBisPosition(angebotspositionIId_LetzterNomalerArtikel);
						} else {
							positionDtoRundungsartikel.setNEinzelpreis(BigDecimal.ZERO);
							positionDtoRundungsartikel.setNNettoeinzelpreis(BigDecimal.ZERO);
							positionDtoRundungsartikel.setNMwstbetrag(BigDecimal.ZERO);
							positionDtoRundungsartikel.setNBruttoeinzelpreis(BigDecimal.ZERO);

							getLieferscheinpositionFac().updateLieferscheinposition(positionDtoRundungsartikel,
									theClientDto);
						}

					}
				}

				lpDto.setZwsNettoSumme(null);
				getLieferscheinpositionFac().updateLieferscheinposition(lpDto, theClientDto);
			} else if (lpDto.isIntelligenteZwischensumme() && bdBetragInBelegwaehrungUebersteuert != null
					&& lpDto.getZwsNettoSumme() != null && lpDto.getZwsNettoSumme().doubleValue() == 0) {
				throwExceptionZwischensummeNull();
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	private class LieferscheinositionenNurIdentFilter implements Predicate, Serializable {
		private static final long serialVersionUID = -5617572280308975044L;

		@Override
		public boolean evaluate(Object arg0) {
			if (arg0 instanceof LieferscheinpositionDto) {
				LieferscheinpositionDto pos = (LieferscheinpositionDto) arg0;
				return pos.isIdent();
			}

			return false;
		}
	}

}
