package com.lp.server.rechnung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.eingangsrechnung.ejb.Auftragszuordnungverrechnet;
import com.lp.server.eingangsrechnung.ejb.EingangsrechnungAuftragszuordnung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungAuftragszuordnung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.ejb.Maschinenzeitdaten;
import com.lp.server.personal.ejb.Maschinenzeitdatenverrechnet;
import com.lp.server.personal.ejb.Reise;
import com.lp.server.personal.ejb.Reiseverrechnet;
import com.lp.server.personal.ejb.Tagesart;
import com.lp.server.personal.ejb.Telefonzeiten;
import com.lp.server.personal.ejb.Telefonzeitenverrechnet;
import com.lp.server.personal.ejb.Zeitdaten;
import com.lp.server.personal.ejb.Zeitdatenverrechnet;
import com.lp.server.personal.ejb.Zeitdatenverrechnetzeitraum;
import com.lp.server.personal.ejb.Zeitmodelltagpause;
import com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRReise;
import com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitenmanuellerledigt;
import com.lp.server.personal.service.AbschnittEinerReiseDto;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.ReiseKomplettDto;
import com.lp.server.personal.service.ReisezeitenEinerPersonDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeitdatenDtoAssembler;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.ejb.Abrechnungsvorschlag;
import com.lp.server.rechnung.ejb.Verrechnungsmodell;
import com.lp.server.rechnung.ejb.Verrechnungsmodelltag;
import com.lp.server.rechnung.fastlanereader.generated.FLRAbrechnungsvorschlag;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRVerrechnungsmodelltag;
import com.lp.server.rechnung.service.AbrechnungsvorschlagDto;
import com.lp.server.rechnung.service.AbrechnungsvorschlagDtoAssembler;
import com.lp.server.rechnung.service.AbrechnungsvorschlagFac;
import com.lp.server.rechnung.service.AbrechnungsvorschlagUeberleitenDto;
import com.lp.server.rechnung.service.DauerUndZeitraumDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.VerrechnungsmodellDto;
import com.lp.server.rechnung.service.VerrechnungsmodellDtoAssembler;
import com.lp.server.rechnung.service.VerrechnungsmodelltagDto;
import com.lp.server.rechnung.service.VerrechnungsmodelltagDtoAssembler;
import com.lp.server.rechnung.service.ZeileFuerAbrechnungsvorschlagUeberleitungDto;
import com.lp.server.rechnung.service.ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto;
import com.lp.server.rechnung.service.ZeitVonBisUndDauer;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
public class AbrechnungsvorschlagFacBean extends LPReport implements AbrechnungsvorschlagFac {

	private Object[][] data = null;
	private String sAktuellerReport = null;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;

		String fieldName = jRField.getName();
		if (sAktuellerReport.equals(REPORT_ABRECHNUNGSVORSCHLAG)) {

			if ("Art".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_ART];
			} else if ("Verrechnet".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_B_VERRECHNET];
			} else if ("Person".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_PERSON];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_KUNDE];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_PROJEKT];
			} else if ("ProjektStatus".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_PROJEKT_STATUS];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_AUFTRAG];
			} else if ("AuftragStatus".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_AUFTRAG_STATUS];
			} else if ("Los".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_LOS];
			} else if ("LosStatus".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_LOS_STATUS];
			} else if ("Von".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_VON];
			} else if ("Feiertag".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_FEIERTAG];
			} else if ("UnterschriebenAm".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_UNTERSCHRIEBEN_AM];
			} else if ("Bis".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_BIS];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_ARTIKELNUMMER];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_ARTIKELBEZEICHNUNG];
			} else if ("Verrechenbar".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_VERRECHENBAR];
			} else if ("StundenGesamt".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_STUNDEN_GESAMT];
			} else if ("StundenVerrechenbar".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_STUNDEN_VERRECHENBAR];
			} else if ("StundenOffen".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_STUNDEN_OFFEN];
			} else if ("BetragGesamt".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_BETRAG_GESAMT];
			} else if ("BetragVerrechenbar".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_BETRAG_VERRECHENBAR];
			} else if ("BetragOffen".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_BETRAG_OFFEN];
			} else if ("ABRECHNUNGSVORSCHLAG_I_ID".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_ABRECHNUNGSVORSCHLAG_I_ID];
			} else if ("KilometerGesamt".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_KILOMETER_GESAMT];
			} else if ("KilometerVerrechenbar".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_KILOMETER_VERRECHENBAR];
			} else if ("KilometerOffen".equals(fieldName)) {
				value = data[index][REPORT_ABRECHNUNGSVORSCHLAG_KILOMETER_OFFEN];
			}
		} else if (sAktuellerReport.equals(REPORT_VERRECHNUNGSMODELL)) {

			if ("Tagesart".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_TAGESART];
			} else if ("RestDesTages".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_REST_DES_TAGES];
			} else if ("ArtikelnummerGebucht".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ARTIKELNUMMER_GEBUCHT];
			} else if ("ArtikelbezeichnungGebucht".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ARTIKELBEZEICHNUNG_GEBUCHT];
			} else if ("ArtikelzusatzbezeichnungGebucht".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG_GEBUCHT];
			} else if ("Artikelzusatzbezeichnung2Gebucht".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG2_GEBUCHT];
			} else if ("ArtikelnummerZuverrechnen".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ARTIKELNUMMER_ZUVERRECHNEN];
			} else if ("ArtikelZuverrechnenKalkulatorisch".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ARTIKEL_ZUVERRECHNEN_KALKULATORISCH];
			} else if ("ArtikelbezeichnungZuverrechnen".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ARTIKELBEZEICHNUNG_ZUVERRECHNEN];
			} else if ("ArtikelzusatzbezeichnungZuverrechnen".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG_ZUVERRECHNEN];
			} else if ("Artikelzusatzbezeichnung2Zuverrechnen".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG2_ZUVERRECHNEN];
			} else if ("ZeitVon".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ZEIT_VON];
			} else if ("ZeitBis".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_ZEIT_BIS];
			} else if ("DauerAb".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_DAUER_AB];
			} else if ("Faktor".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_FAKTOR];
			} else if ("PreisbasisGebucht".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_PREISBASIS_GEBUCHT];
			} else if ("PreisbasisZuVerrechnen".equals(fieldName)) {
				value = data[index][REPORT_VERRECHNUNGSMODELL_PREISBASIS_ZU_VERRECHNEN];
			}
		} else if (sAktuellerReport.equals(REPORT_MANUELLERLEDIGTEZEITEN)) {

			if ("Von".equals(fieldName)) {
				value = data[index][REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_VON];
			} else if ("Bis".equals(fieldName)) {
				value = data[index][REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_BIS];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_AUFTRAG];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_KUNDE];
			} else if ("Maschine".equals(fieldName)) {
				value = data[index][REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_MASCHINE];
			} else if ("Personal".equals(fieldName)) {
				value = data[index][REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_PERSONAL];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_PROJEKT];
			} else if ("Art".equals(fieldName)) {
				value = data[index][REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_ART];
			}
		}
		return value;

	}

	private static int REPORT_ABRECHNUNGSVORSCHLAG_ART = 0;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_PERSON = 1;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_KUNDE = 2;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_PROJEKT = 3;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_PROJEKT_STATUS = 4;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_AUFTRAG = 5;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_AUFTRAG_STATUS = 6;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_LOS = 7;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_LOS_STATUS = 8;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_VON = 9;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_BIS = 10;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_ARTIKELNUMMER = 11;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_ARTIKELBEZEICHNUNG = 12;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_VERRECHENBAR = 13;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_STUNDEN_GESAMT = 14;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_STUNDEN_VERRECHENBAR = 15;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_STUNDEN_OFFEN = 16;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_BETRAG_GESAMT = 17;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_BETRAG_VERRECHENBAR = 18;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_BETRAG_OFFEN = 19;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_ABRECHNUNGSVORSCHLAG_I_ID = 20;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_B_VERRECHNET = 21;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_FEIERTAG = 22;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_KILOMETER_GESAMT = 23;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_KILOMETER_VERRECHENBAR = 24;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_KILOMETER_OFFEN = 25;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_UNTERSCHRIEBEN_AM = 26;
	private static int REPORT_ABRECHNUNGSVORSCHLAG_ANZAHL_SPALTEN = 27;

	private static int REPORT_VERRECHNUNGSMODELL_TAGESART = 0;
	private static int REPORT_VERRECHNUNGSMODELL_ARTIKELNUMMER_GEBUCHT = 1;
	private static int REPORT_VERRECHNUNGSMODELL_ARTIKELBEZEICHNUNG_GEBUCHT = 2;
	private static int REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG_GEBUCHT = 3;
	private static int REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG2_GEBUCHT = 4;
	private static int REPORT_VERRECHNUNGSMODELL_ARTIKELNUMMER_ZUVERRECHNEN = 5;
	private static int REPORT_VERRECHNUNGSMODELL_ARTIKELBEZEICHNUNG_ZUVERRECHNEN = 6;
	private static int REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG_ZUVERRECHNEN = 7;
	private static int REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG2_ZUVERRECHNEN = 8;
	private static int REPORT_VERRECHNUNGSMODELL_REST_DES_TAGES = 9;
	private static int REPORT_VERRECHNUNGSMODELL_DAUER_AB = 10;
	private static int REPORT_VERRECHNUNGSMODELL_ZEIT_VON = 11;
	private static int REPORT_VERRECHNUNGSMODELL_ZEIT_BIS = 12;
	private static int REPORT_VERRECHNUNGSMODELL_PREISBASIS_GEBUCHT = 13;
	private static int REPORT_VERRECHNUNGSMODELL_PREISBASIS_ZU_VERRECHNEN = 14;
	private static int REPORT_VERRECHNUNGSMODELL_FAKTOR = 15;
	private static int REPORT_VERRECHNUNGSMODELL_ARTIKEL_ZUVERRECHNEN_KALKULATORISCH = 16;
	private static int REPORT_VERRECHNUNGSMODELL_ANZAHL_SPALTEN = 17;

	private static int REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_VON = 0;
	private static int REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_BIS = 1;
	private static int REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_PERSONAL = 2;
	private static int REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_AUFTRAG = 3;
	private static int REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_KUNDE = 4;
	private static int REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_MASCHINE = 5;
	private static int REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_PROJEKT = 6;
	private static int REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_ART = 7;
	private static int REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_ANZAHL_SPALTEN = 8;

	@PersistenceContext
	private EntityManager em;

	private ZeileFuerAbrechnungsvorschlagUeberleitungDto anhandVerrechnungsmodellAufsplitten(java.util.Date tVon,
			java.util.Date tBis, Integer artikelIIdGebucht, AbrechnungsvorschlagDto av, Double dDauerGesamt,
			boolean bZeitraeume, boolean bDauer, TheClientDto theClientDto) {

		ZeileFuerAbrechnungsvorschlagUeberleitungDto zeile = new ZeileFuerAbrechnungsvorschlagUeberleitungDto(
				abrechnungsvorschlagFindByPrimaryKey(av.getIId(), theClientDto));

		Integer verrechnungsmodellIId = null;

		if (av.getAuftragIId() != null) {

			AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(av.getAuftragIId());
			if (aDto.getVerrechnungsmodellIId() != null) {
				verrechnungsmodellIId = getAuftragFac().auftragFindByPrimaryKey(av.getAuftragIId())
						.getVerrechnungsmodellIId();
			} else {

				ArrayList al = new ArrayList();
				al.add(aDto.getCNr());
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZEITDATEN_UEBERLEITUNG_AUFTRAG_KEIN_VERRECHNUNGSMODELL,
						al, new Exception("FEHLER_ZEITDATEN_UEBERLEITUNG_AUFTRAG_KEIN_VERRECHNUNGSMODELL"));

			}
		} else {
			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(av.getKundeIId(), theClientDto);
			if (kdDto.getVerrechnungsmodellIId() != null) {
				verrechnungsmodellIId = kdDto.getVerrechnungsmodellIId();
			} else {
				ArrayList al = new ArrayList();
				al.add(kdDto.getPartnerDto().formatAnrede());

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZEITDATEN_UEBERLEITUNG_KUNDE_KEIN_VERRECHNUNGSMODELL, al,
						new Exception("FEHLER_ZEITDATEN_UEBERLEITUNG_KUNDE_KEIN_VERRECHNUNGSMODELL"));
			}
		}

		boolean bPreiseAusAuftrag = false;

		VerrechnungsmodellDto vmDto = verrechnungsmodellFindByPrimaryKey(verrechnungsmodellIId, theClientDto);
		bPreiseAusAuftrag = Helper.short2boolean(vmDto.getBPreiseAusAuftrag());

		if (artikelIIdGebucht == null && av.getTelefonzeitenIId() != null) {

			artikelIIdGebucht = vmDto.getArtikelIIdTelefon();

		}

		// PJ21646
		if (artikelIIdGebucht != null && av.getAuftragpositionIId() != null) {
			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIIdGebucht, theClientDto);

			if (Helper.short2boolean(aDto.getBKalkulatorisch())) {
				AuftragpositionDto apDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKeyOhneExc(av.getAuftragpositionIId());
				if (apDto != null && apDto.getPositionIIdZugehoerig() != null && apDto.getNMenge() != null) {
					try {

						AuftragpositionDto apDtoZugehoerig = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(apDto.getPositionIIdZugehoerig());
						if (apDtoZugehoerig != null && apDtoZugehoerig.getNMenge() != null
								&& apDtoZugehoerig.getArtikelIId() != null) {
							BigDecimal bdFaktor = apDto.getNMenge().divide(apDtoZugehoerig.getNMenge(), 6,
									BigDecimal.ROUND_HALF_EVEN);

							artikelIIdGebucht = apDtoZugehoerig.getArtikelIId();
							dDauerGesamt = new BigDecimal(dDauerGesamt).divide(bdFaktor, 3, BigDecimal.ROUND_HALF_EVEN)
									.doubleValue();
						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}
			}
		}

		Integer tagesartIId_Feiertag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

		Integer taetigkeitIId_Kommt = getZeiterfassungFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
		Integer taetigkeitIId_Geht = getZeiterfassungFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();
		Integer taetigkeitIId_Unter = getZeiterfassungFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_UNTER, theClientDto).getIId();

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Helper.cutDate(tVon).getTime());

		java.util.Date tVonOri = tVon;
		java.util.Date tBisOri = tBis;

		while (cal.getTimeInMillis() <= tBis.getTime()) {

			tVon = tVonOri;
			tBis = tBisOri;

			Timestamp tVonMin = new Timestamp(Helper.cutDate(cal.getTime()).getTime());

			cal.add(Calendar.DATE, 1);
			Timestamp tBisMax = new Timestamp(Helper.cutDate(cal.getTime()).getTime());

			if (tVon.before(tVonMin)) {
				tVon = tVonMin;
			}
			if (tBis.after(tBisMax)) {
				tBis = tBisMax;
			}

			Integer tagesartIId = getTagesart(new Timestamp(tVon.getTime()), tagesartIId_Feiertag, tagesartIId_Halbtag,
					theClientDto);

			if (tagesartIId != null) {
				BigDecimal bdNochOffen = new BigDecimal(dDauerGesamt).movePointLeft(2)
						.multiply(new BigDecimal(av.getFVerrechenbar()));

				if (bZeitraeume) {

					// Zuerst Zeitraume abziehen
					Query query = em
							.createNamedQuery("VerrechnungsmodelltagfindByVerrechnungsmodellIIdTagesartIIdZeitraum");
					query.setParameter(1, verrechnungsmodellIId);
					query.setParameter(2, tagesartIId);
					query.setParameter(3, artikelIIdGebucht);
					Collection c = query.getResultList();

					Iterator it = c.iterator();

					while (it.hasNext()) {
						Verrechnungsmodelltag tag = (Verrechnungsmodelltag) it.next();

						java.sql.Time tVonAusVerrechnungsmodell = tag.getUZeitraumVon();

						Timestamp tVonVerschoben = new Timestamp(tVon.getTime());

						Calendar tZeit = Calendar.getInstance();
						tZeit.setTimeInMillis(tVon.getTime());
						Calendar tKommt = Calendar.getInstance();
						tKommt.setTimeInMillis(tVonAusVerrechnungsmodell.getTime());
						tZeit.set(Calendar.HOUR_OF_DAY, tKommt.get(Calendar.HOUR_OF_DAY));
						tZeit.set(Calendar.MINUTE, tKommt.get(Calendar.MINUTE));
						tZeit.set(Calendar.SECOND, 0);
						tZeit.set(Calendar.MILLISECOND, 0);
						java.sql.Timestamp tNeueZeitKommt = new java.sql.Timestamp(tZeit.getTimeInMillis());
						if (tVon.before(tNeueZeitKommt)) {
							tVonVerschoben = tNeueZeitKommt;

						}

						java.sql.Time tBisAusVerrechnungsmodell = tag.getUZeitraumBis();

						Timestamp tBisVerschoben = new Timestamp(tBis.getTime());

						Calendar tGeht = Calendar.getInstance();

						if (Helper.short2boolean(tag.getBEndedestages())) {
							tZeit.add(Calendar.DATE, 1);
							tZeit.set(Calendar.HOUR_OF_DAY, 0);
							tZeit.set(Calendar.MINUTE, 0);
							tZeit.set(Calendar.SECOND, 0);
							tZeit.set(Calendar.MILLISECOND, 0);

						} else {

							tGeht.setTimeInMillis(tBisAusVerrechnungsmodell.getTime());
							tZeit.set(Calendar.HOUR_OF_DAY, tGeht.get(Calendar.HOUR_OF_DAY));
							tZeit.set(Calendar.MINUTE, tGeht.get(Calendar.MINUTE));
							tZeit.set(Calendar.SECOND, 0);
							tZeit.set(Calendar.MILLISECOND, 0);
						}

						java.sql.Timestamp tNeueZeitGht = new java.sql.Timestamp(tZeit.getTimeInMillis());

						if (tBis.after(tNeueZeitGht)) {
							tBisVerschoben = tNeueZeitGht;
							// SP8769
							if (av.getTVon().before(tBisVerschoben)) {
								av.setTVon(tBisVerschoben);
							}

						}

						long lZeitraum = tBisVerschoben.getTime() - tVonVerschoben.getTime();

						if (lZeitraum > 0) {
							double dDauerZeitraum = Helper.timeInMillis2Double(lZeitraum);

							zeile.add2Verrechnen(tag.getArtikelIIdZuverrechnen(), new BigDecimal(dDauerZeitraum),
									bPreiseAusAuftrag ? av.getAuftragpositionIId() : null, tVonVerschoben,
									tBisVerschoben, zeile.getAvDto().getZeitdatenIId(),
									Helper.short2boolean(vmDto.getBNachArtikelVerdichten()));

							// SP8769 Eventuell hier DTO statt FLRKlasse verwenden und T_VON nur in DTO
							// updaten, damit dies das
							// fuer die naechste Runde bei der Dauer-Berechnung zur verfuegung steht
							// av.setT_von(t_von);

							bdNochOffen = bdNochOffen.subtract(new BigDecimal(dDauerZeitraum));

						}

					}
				}
				// Dann Dauer
				if (bDauer == true) {
					if (bdNochOffen.doubleValue() > 0) {
						Query queryDauer = em
								.createNamedQuery("VerrechnungsmodelltagfindByVerrechnungsmodellIIdTagesartIIdDauer");
						queryDauer.setParameter(1, verrechnungsmodellIId);
						queryDauer.setParameter(2, tagesartIId);
						queryDauer.setParameter(3, artikelIIdGebucht);
						Collection cDauer = queryDauer.getResultList();

						Iterator itDauer = cDauer.iterator();

						while (itDauer.hasNext()) {
							Verrechnungsmodelltag tag = (Verrechnungsmodelltag) itDauer.next();

							double dDauerAb = Helper.time2Double(tag.getUDauerAb());

							// PJ21725 Das DauerAb Gilt nun ab dem Kommt des Tages

							if (av.getZeitdatenIId() != null) {

								// letztes Kommt suchen

								Query query = em.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
								query.setParameter(1, av.getPersonalIId());
								query.setParameter(2, Helper.cutTimestamp(new Timestamp(av.getTBis().getTime())));
								query.setParameter(3, new Timestamp(av.getTVon().getTime()));
								Collection<?> cl = query.getResultList();
								// if (! cl.isEmpty()) {
								ZeitdatenDto[] zeitdatenDtos = ZeitdatenDtoAssembler.createDtos(query.getResultList());
								// }
								// catch (NoResultException ex) {
								// zeitdatenDtos = null;
								// }
								// Letztes KOMMT suchen
								Timestamp tLetztesKommt = null;
								ArrayList<ZeitdatenDto> alZeitdaten = new ArrayList<ZeitdatenDto>();
								for (int i = zeitdatenDtos.length - 1; i >= 0; i--) {
									ZeitdatenDto zeitdatenDto = zeitdatenDtos[i];
									alZeitdaten.add(zeitdatenDto);
									if (zeitdatenDto.getTaetigkeitIId() != null) {
										if (zeitdatenDto.getTaetigkeitIId().equals(taetigkeitIId_Geht)) {
											break;
										}
										if (zeitdatenDto.getTaetigkeitIId().equals(taetigkeitIId_Kommt)) {
											tLetztesKommt = zeitdatenDto.getTZeit();
											// kommt wieder aus liste entfernen
											alZeitdaten.remove(alZeitdaten.size() - 1);

											break;
										}
									}
								}

								if (tLetztesKommt != null) {

									Timestamp tDauerBeginn = new Timestamp(
											tLetztesKommt.getTime() + (long) (dDauerAb * 3600000));

									double dUnterImTZeitraum;
									try {
										dUnterImTZeitraum = getZeiterfassungFac()
												.berechneDauerPaarweiserSondertaetigkeitenEinerPersonUndEinesZeitraumes(
														av.getPersonalIId(), tLetztesKommt, tDauerBeginn,
														taetigkeitIId_Unter);
										tDauerBeginn = new Timestamp(
												tDauerBeginn.getTime() + (long) (dUnterImTZeitraum * 3600000));
									} catch (RemoteException e) {
										throwEJBExceptionLPRespectOld(e);
									}

									if (tDauerBeginn.before(av.getTVon())) {
										tDauerBeginn = new Timestamp(av.getTVon().getTime());
									}

									// SP8543

									Timestamp tMaxBis = new Timestamp(av.getTBis().getTime());

									Time uMaxBis = new Time(tMaxBis.getTime());

									Query queryZRM = em.createNamedQuery(
											"VerrechnungsmodelltagfindByVerrechnungsmodellIIdTagesartIIdZeitraumTVon");
									queryZRM.setParameter(1, verrechnungsmodellIId);
									queryZRM.setParameter(2, tagesartIId);
									queryZRM.setParameter(3, artikelIIdGebucht);
									queryZRM.setParameter(4, uMaxBis);
									Collection c = queryZRM.getResultList();

									Iterator it = c.iterator();

									if (it.hasNext()) {
										Verrechnungsmodelltag vmTemp = (Verrechnungsmodelltag) it.next();

										Calendar tZeitVon = Calendar.getInstance();
										tZeitVon.setTimeInMillis(tVon.getTime());
										Calendar tKommt = Calendar.getInstance();
										tKommt.setTimeInMillis(vmTemp.getUZeitraumVon().getTime());
										tZeitVon.set(Calendar.HOUR_OF_DAY, tKommt.get(Calendar.HOUR_OF_DAY));
										tZeitVon.set(Calendar.MINUTE, tKommt.get(Calendar.MINUTE));
										tZeitVon.set(Calendar.SECOND, 0);
										tZeitVon.set(Calendar.MILLISECOND, 0);
										java.sql.Timestamp tNeueZeitAb = new java.sql.Timestamp(
												tZeitVon.getTimeInMillis());
										if (tMaxBis.after(tNeueZeitAb)) {

											// Das Bis muss auch nach dem Von sein

											Calendar tZeit = Calendar.getInstance();
											tZeit.setTimeInMillis(tMaxBis.getTime());

											Calendar tBisAusVM = Calendar.getInstance();
											tBisAusVM.setTimeInMillis(vmTemp.getUZeitraumVon().getTime());

											if (Helper.short2boolean(vmTemp.getBEndedestages())) {
												tZeit.add(Calendar.DATE, 1);
												tZeit.set(Calendar.HOUR_OF_DAY, 0);
												tZeit.set(Calendar.MINUTE, 0);
												tZeit.set(Calendar.SECOND, 0);
												tZeit.set(Calendar.MILLISECOND, 0);

											} else {

												tZeit.set(Calendar.HOUR_OF_DAY, tBisAusVM.get(Calendar.HOUR_OF_DAY));
												tZeit.set(Calendar.MINUTE, tBisAusVM.get(Calendar.MINUTE));
												tZeit.set(Calendar.SECOND, 0);
												tZeit.set(Calendar.MILLISECOND, 0);
											}

											if (tZeit.getTime().after(tMaxBis)) {
												tMaxBis = tNeueZeitAb;
											}

										}
									}

									long lDauer = tMaxBis.getTime() - tDauerBeginn.getTime();
									if (lDauer > 0) {
										BigDecimal bdStunden = new BigDecimal((double) lDauer / 3600000);
										zeile.add2Verrechnen(tag.getArtikelIIdZuverrechnen(), bdStunden,
												bPreiseAusAuftrag ? av.getAuftragpositionIId() : null, tDauerBeginn,
												new Timestamp(tMaxBis.getTime()), av.getZeitdatenIId(),
												Helper.short2boolean(vmDto.getBNachArtikelVerdichten()));
										bdNochOffen = bdNochOffen.subtract(bdStunden);

									}

									// Alles was dann noch bleibt geht auf den gebuchten Artikel
									if (bdNochOffen.doubleValue() > 0) {
										Timestamp tDauerEnde = tDauerBeginn;

										if (tDauerEnde.after(tMaxBis)) {
											tDauerEnde = new Timestamp(tMaxBis.getTime());
										}

										zeile.add2Verrechnen(artikelIIdGebucht, bdNochOffen,
												bPreiseAusAuftrag ? av.getAuftragpositionIId() : null,
												new Timestamp(av.getTVon().getTime()), tDauerEnde, av.getZeitdatenIId(),
												Helper.short2boolean(vmDto.getBNachArtikelVerdichten()));
										bdNochOffen = BigDecimal.ZERO;
									}

								}

							}

						}
					}

					// Alles was dann noch bleibt geht auf den gebuchten Artikel
					if (bdNochOffen.doubleValue() > 0) {
						zeile.add2Verrechnen(artikelIIdGebucht, bdNochOffen,
								bPreiseAusAuftrag ? av.getAuftragpositionIId() : null,
								new Timestamp(av.getTVon().getTime()), new Timestamp(av.getTBis().getTime()),
								av.getZeitdatenIId(), Helper.short2boolean(vmDto.getBNachArtikelVerdichten()));
					}
				}

			} else {

				// DIE GANZ GEBUCHTE ZEIT geht auf den gebuchten Artikel
				zeile.add2Verrechnen(artikelIIdGebucht, new BigDecimal(dDauerGesamt),
						bPreiseAusAuftrag ? av.getAuftragpositionIId() : null, new Timestamp(av.getTVon().getTime()),
						new Timestamp(av.getTBis().getTime()), av.getZeitdatenIId(),
						Helper.short2boolean(vmDto.getBNachArtikelVerdichten()));
			}

		}

		return zeile;
	}

	private Integer getTagesart(Timestamp d_datum, Integer tagesartIId_Feiertag, Integer tagesartIId_Halbtag,
			TheClientDto theClientDto) {
		d_datum = Helper.cutTimestamp(d_datum);

		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		Integer tagesartIId = null;
		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1, Helper.holeTagbezeichnungLang(c.get(Calendar.DAY_OF_WEEK)));
			Tagesart tagesart = (Tagesart) query.getSingleResult();
			tagesartIId = tagesart.getIId();

			BetriebskalenderDto dto = getPersonalFac().betriebskalenderFindByMandantCNrDDatum(d_datum,
					theClientDto.getMandant(), theClientDto);
			if (dto != null) {
				// NUR RELIGIONSLOSE FEIERTAGE
				if (dto.getReligionIId() == null) {
					if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
							|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
						tagesartIId = dto.getTagesartIId();
					} else {
						tagesartIId = dto.getTagesartIId();
					}
				}
			}

		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
		return tagesartIId;
	}

	private void pruefeAufKalkulatorischeArtikel(ZeileFuerAbrechnungsvorschlagUeberleitungDto zavDto, String art,
			FLRAbrechnungsvorschlag flr, MaschineDto mDto, TheClientDto theClientDto) {

		Map<Integer, Map<Integer, DauerUndZeitraumDto>> mGetrenntNachArtikel = zavDto
				.getGetrenntNachArtikelnAusVerrechnungsmodell();

		Iterator itArtikel = mGetrenntNachArtikel.keySet().iterator();

		while (itArtikel.hasNext()) {

			Integer artikelIId = (Integer) itArtikel.next();

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);

			if (Helper.short2boolean(aDto.getBKalkulatorisch())) {
				ArrayList al = new ArrayList();

				String sZeile = art;

				if (art.equals(AbrechnungsvorschlagFac.ART_MASCHINE) && mDto != null) {
					sZeile += " " + mDto.getBezeichnung();
				} else {
					if (flr.getFlrpersonal() != null && flr.getFlrpersonal().getC_kurzzeichen() != null) {
						sZeile += " " + flr.getFlrpersonal().getC_kurzzeichen();
					} else {
						sZeile += " " + HelperServer.formatPersonAusFLRPErsonal(flr.getFlrpersonal());
					}

				}

				sZeile += " (" + Helper.formatDatumZeit(flr.getT_von(), theClientDto.getLocUi()) + ")";

				al.add(sZeile);
				al.add(aDto.formatArtikelbezeichnung());

				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_ZU_VERRECHNENDER_ARTIKEL_KALKULATORISCH, al,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_ZU_VERRECHNENDER_ARTIKEL_KALKULATORISCH"));

			}
		}

	}

	public AbrechnungsvorschlagUeberleitenDto erzeugeUeberleitungsvorschlag(String art,
			ArrayList<Integer> selektierteIIds, TheClientDto theClientDto) {

		AbrechnungsvorschlagUeberleitenDto ueberleitenDto = new AbrechnungsvorschlagUeberleitenDto();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRAbrechnungsvorschlag.class);

		crit.add(Restrictions.in("i_id", selektierteIIds));

		if (art.equals(AbrechnungsvorschlagFac.ART_MASCHINE)) {
			crit.add(Restrictions.isNotNull("maschinenzeitdaten_i_id"));
		} else if (art.equals(AbrechnungsvorschlagFac.ART_ZEITDATEN)) {
			crit.add(Restrictions.isNotNull("zeitdaten_i_id"));
		} else if (art.equals(AbrechnungsvorschlagFac.ART_TELEFON)) {
			crit.add(Restrictions.isNotNull("telefonzeiten_i_id"));
		}

		// SP9093
		crit.createAlias("flrauftragposition", "ap", JoinType.LEFT_OUTER_JOIN);
		crit.addOrder(Order.asc("ap.i_sort"));

		crit.addOrder(Order.asc("t_von"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRAbrechnungsvorschlag flr = (FLRAbrechnungsvorschlag) resultListIterator.next();

			AbrechnungsvorschlagDto avDto = abrechnungsvorschlagFindByPrimaryKey(flr.getI_id(), theClientDto);

			if (art.equals(AbrechnungsvorschlagFac.ART_MASCHINE)) {

				MaschineDto mDto = getZeiterfassungFac()
						.maschineFindByPrimaryKey(flr.getFlrmaschinenzeitdaten().getFlrmaschine().getI_id());

				if (mDto.getArtikelIIdVerrechnen() != null) {

					ZeileFuerAbrechnungsvorschlagUeberleitungDto zavDto = anhandVerrechnungsmodellAufsplitten(
							flr.getFlrmaschinenzeitdaten().getT_von(), flr.getFlrmaschinenzeitdaten().getT_bis(),
							mDto.getArtikelIIdVerrechnen(), avDto,
							Helper.timeInMillis2Double(flr.getFlrmaschinenzeitdaten().getT_bis().getTime()
									- flr.getFlrmaschinenzeitdaten().getT_von().getTime()),
							true, true, theClientDto);

					pruefeAufKalkulatorischeArtikel(zavDto, AbrechnungsvorschlagFac.ART_MASCHINE, flr, mDto,
							theClientDto);

					ueberleitenDto.addZeile(zavDto);

				} else {
					ArrayList al = new ArrayList();
					al.add(mDto.getBezeichnung());
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ZEITDATEN_UEBERLEITUNG_MASCHINE_KEIN_VERRECHNUNGSARTIKEL,
							new Exception("FEHLER_ZEITDATEN_UEBERLEITUNG_MASCHINE_KEIN_VERRECHNUNGSARTIKEL"));
				}

			} else if (art.equals(AbrechnungsvorschlagFac.ART_TELEFON)) {
				ZeileFuerAbrechnungsvorschlagUeberleitungDto zavDto = anhandVerrechnungsmodellAufsplitten(
						flr.getFlrtelefonzeiten().getT_von(), flr.getFlrtelefonzeiten().getT_bis(), null, avDto,
						Helper.timeInMillis2Double(flr.getFlrtelefonzeiten().getT_bis().getTime()
								- flr.getFlrtelefonzeiten().getT_von().getTime()),
						true, true, theClientDto);

				pruefeAufKalkulatorischeArtikel(zavDto, AbrechnungsvorschlagFac.ART_TELEFON, flr, null, theClientDto);

				ueberleitenDto.addZeile(zavDto);

			} else {

				AuftragzeitenDto[] zeitenDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
						flr.getFlrzeitdaten().getC_belegartnr(), flr.getFlrzeitdaten().getI_belegartid(), null, null,
						null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ZEITPUNKT_PERSONAL, false,
						flr.getFlrzeitdaten().getI_id(), null, ZeiterfassungFac.BELEGZEITEN_NUR_PERSONALZEITEN,
						theClientDto);

				BigDecimal bdOffen = flr.getN_stunden_offen();

				for (AuftragzeitenDto zDto : zeitenDtos) {

					ZeileFuerAbrechnungsvorschlagUeberleitungDto zeile = anhandVerrechnungsmodellAufsplitten(
							zDto.getTsBeginn(), zDto.getTsEnde(), zDto.getArtikelIId(), avDto, zDto.getDdDauer(), true,
							false, theClientDto);

					bdOffen = bdOffen.subtract(zeile.getGesamtsumme());

					pruefeAufKalkulatorischeArtikel(zeile, AbrechnungsvorschlagFac.ART_ZEITDATEN, flr, null,
							theClientDto);

					ueberleitenDto.addZeile(zeile);

				}

				if (bdOffen.doubleValue() > 0.01) {
					ZeileFuerAbrechnungsvorschlagUeberleitungDto zeile = anhandVerrechnungsmodellAufsplitten(
							flr.getT_von(), flr.getT_bis(), flr.getFlrzeitdaten().getArtikel_i_id(), avDto,
							bdOffen.doubleValue(), false, true, theClientDto);

					pruefeAufKalkulatorischeArtikel(zeile, AbrechnungsvorschlagFac.ART_ZEITDATEN, flr, null,
							theClientDto);

					ueberleitenDto.addZeile(zeile);
				}

			}

			if (flr.getZeitdaten_i_id() != null) {
				Query query = em.createNamedQuery("ZeitdatenverrechnetFindByZeitdatenIId");
				query.setParameter(1, flr.getZeitdaten_i_id());
				Collection<?> cl = query.getResultList();
				Iterator itBereitsAbgerechnet = cl.iterator();
				while (itBereitsAbgerechnet.hasNext()) {
					Zeitdatenverrechnet zv = (Zeitdatenverrechnet) itBereitsAbgerechnet.next();

					ueberleitenDto.bereitsAbgerechneteStundenAbziehen(zv.getArtikelIId(), flr.getAuftragposition_i_id(),
							zv.getNStunden());

				}
			}
			if (flr.getMaschinenzeitdaten_i_id() != null) {
				Query query = em.createNamedQuery("MaschinenzeitdatenverrechnetFindByMaschinenzeitdatenIId");
				query.setParameter(1, flr.getMaschinenzeitdaten_i_id());

				Collection<?> cl = query.getResultList();
				Iterator itBereitsAbgerechnet = cl.iterator();
				while (itBereitsAbgerechnet.hasNext()) {
					Maschinenzeitdatenverrechnet zv = (Maschinenzeitdatenverrechnet) itBereitsAbgerechnet.next();

					ueberleitenDto.bereitsAbgerechneteStundenAbziehen(zv.getArtikelIId(), flr.getAuftragposition_i_id(),
							zv.getNStunden());

				}
			}
			if (flr.getTelefonzeiten_i_id() != null) {
				Query query = em.createNamedQuery("TelefonzeitenverrechnetFindByTelefonzeitenIId");
				query.setParameter(1, flr.getTelefonzeiten_i_id());

				Collection<?> cl = query.getResultList();
				Iterator itBereitsAbgerechnet = cl.iterator();
				while (itBereitsAbgerechnet.hasNext()) {
					Telefonzeitenverrechnet zv = (Telefonzeitenverrechnet) itBereitsAbgerechnet.next();

					ueberleitenDto.bereitsAbgerechneteStundenAbziehen(zv.getArtikelIId(), flr.getAuftragposition_i_id(),
							zv.getNStunden());

				}

			}
		}
		return ueberleitenDto;
	}

	public void abrechnungsvorschlagUeberleitenZeitdaten(
			Map<String, ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto> zeilen,
			boolean bErledigt, java.sql.Date tBelegdatum, TheClientDto theClientDto) {

		Iterator it = zeilen.keySet().iterator();

		it = zeilen.keySet().iterator();
		while (it.hasNext()) {

			String sKey = (String) it.next();

			ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto zeile = zeilen
					.get(sKey);

			Integer kundeIId = zeile.getAlBetroffeneAV().get(0).getKundeIId();
			String waehrungCNr = zeile.getAlBetroffeneAV().get(0).getWaehrungCNrRechnung();

			HashMap<Integer, AbrechnungsvorschlagDto> alleBetroffeneZeilen = new HashMap<Integer, AbrechnungsvorschlagDto>();

			for (int i = 0; i < zeile.getAlBetroffeneAV().size(); i++) {
				alleBetroffeneZeilen.put(zeile.getAlBetroffeneAV().get(i).getIId(), zeile.getAlBetroffeneAV().get(i));
			}

			// Rechnung + Position anlegen
			Integer rechnungIId = rechnungFindenOderAnlegen(kundeIId, tBelegdatum, waehrungCNr, theClientDto);

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(zeile.getArtikelIId(), theClientDto);

			RechnungPositionDto reposDto = new RechnungPositionDto();
			reposDto.setArtikelIId(zeile.getArtikelIId());
			reposDto.setBelegIId(rechnungIId);
			reposDto.setBDrucken(Helper.boolean2Short(true));
			reposDto.setEinheitCNr(aDto.getEinheitCNr());
			reposDto.setXTextinhalt(zeile.getKommentarVomClient());
			reposDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
			reposDto.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_IDENT);
			reposDto.setAuftragpositionIId(zeile.getAuftragspositionIId());

			//
			if (zeile.getAuftragspositionIId() != null) {
				AuftragpositionDto apDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKeyOhneExc(zeile.getAuftragspositionIId());
				if (apDto.getCBez() != null) {
					reposDto.setCBez(apDto.getCBez());
				}
				if (apDto.getCZusatzbez() != null) {
					reposDto.setCZusatzbez(apDto.getCZusatzbez());
				}
			}

			try {
				RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);

				reposDto.setNMaterialzuschlag(BigDecimal.ZERO);
				reposDto.setBDrucken(Helper.boolean2Short(true));

				reposDto.setNMenge(zeile.getBdZuverrechnenVomClient());

				reposDto = (RechnungPositionDto) befuellePreisfelderAnhandVKPreisfindung(reposDto,
						reDto.getTBelegdatum(), kundeDto.getIId(), reDto.getWaehrungCNr(), theClientDto);

				// PJ21150
				if (zeile.getBdPreisVomClient() != null) {
					reposDto.setNNettoeinzelpreis(zeile.getBdPreisVomClient());
				}

				if (reposDto.getNNettoeinzelpreis() != null) {

					reposDto.setNEinzelpreis(reposDto.getNNettoeinzelpreis());
					reposDto.setFRabattsatz(0D);
					reposDto.setFZusatzrabattsatz(0D);
					reposDto.setNMaterialzuschlag(null);
					reposDto.setNNettoeinzelpreis(reposDto.getNNettoeinzelpreis());

					reposDto = (RechnungPositionDto) getBelegVerkaufFac().berechneBelegpositionVerkauf(reposDto, reDto);

					reposDto = (RechnungPositionDto) mwstsatzBestimmenUndNeuBerechnen(reposDto, kundeDto.getIId(),
							reDto.getTBelegdatum(), theClientDto);

				}

				reposDto = getRechnungFac().createRechnungPosition(reposDto, kundeDto.getLagerIIdAbbuchungslager(),
						theClientDto);

				if (zeile.getDauerUndZeitraumDto() != null && zeile.getDauerUndZeitraumDto().getMZeitraeume() != null) {

					Iterator itZeitr = zeile.getDauerUndZeitraumDto().getMZeitraeume().keySet().iterator();
					while (itZeitr.hasNext()) {
						Integer zeitdatenIId = (Integer) itZeitr.next();
						ArrayList<ZeitVonBisUndDauer> zeitraeume = zeile.getDauerUndZeitraumDto().getMZeitraeume()
								.get(zeitdatenIId);

						for (int f = 0; f < zeitraeume.size(); f++) {

							ZeitVonBisUndDauer zeitraum = zeitraeume.get(f);

							PKGeneratorObj pkGen = new PKGeneratorObj();
							Integer pk2 = pkGen.getNextPrimaryKey(PKConst.PK_ZEITDATENVERRECHNETZEITRAUM);
							Zeitdatenverrechnetzeitraum zr = new Zeitdatenverrechnetzeitraum(pk2, reposDto.getIId(),
									zeitdatenIId, zeitraum.tVon, zeitraum.tBis);
							zr.setNStunden(zeitraum.bdStunden);
							em.merge(zr);
							em.flush();
							if (bErledigt) {
								Zeitdaten zd = em.find(Zeitdaten.class, zeitdatenIId);
								zd.setTErledigt(new Timestamp(System.currentTimeMillis()));
								zd.setPersonalIIdErledigt(theClientDto.getIDPersonal());
								em.merge(zd);
								em.flush();
							}
						}
					}

				}

				BigDecimal bdlfdWert = zeile.getBdZuverrechnenVomClient();

				Iterator itBetroffeneZeilen = alleBetroffeneZeilen.keySet().iterator();

				while (itBetroffeneZeilen.hasNext()) {

					Integer key = (Integer) itBetroffeneZeilen.next();
					AbrechnungsvorschlagDto abrechnungsvorschlagDtoBetroffen = alleBetroffeneZeilen.get(key);

					Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class,
							abrechnungsvorschlagDtoBetroffen.getIId());

					BigDecimal bdAnteil = abrechnungsvorschlag.getNStundenOffen();
					if (bdlfdWert.doubleValue() < abrechnungsvorschlag.getNStundenOffen().doubleValue()) {
						bdAnteil = bdlfdWert;
					}

					if (bdAnteil.doubleValue() != 0) {
						if (abrechnungsvorschlag.getZeitdatenIId() != null) {

							PKGeneratorObj pkGen = new PKGeneratorObj();
							Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZEITDATENVERRECHNET);
							Zeitdatenverrechnet zv = new Zeitdatenverrechnet(pk, reposDto.getIId(),
									abrechnungsvorschlag.getZeitdatenIId(), bdAnteil, zeile.getArtikelIId());
							em.merge(zv);
							em.flush();
							if (bErledigt) {
								Zeitdaten zd = em.find(Zeitdaten.class, abrechnungsvorschlag.getZeitdatenIId());
								zd.setTErledigt(new Timestamp(System.currentTimeMillis()));
								zd.setPersonalIIdErledigt(theClientDto.getIDPersonal());
								em.merge(zd);
								em.flush();
							}

						} else if (abrechnungsvorschlag.getTelefonzeitenIId() != null) {

							PKGeneratorObj pkGen = new PKGeneratorObj();
							// PKGEN
							Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_TELEFONZEITENVERRECHNET);
							Telefonzeitenverrechnet zv = new Telefonzeitenverrechnet(pk, reposDto.getIId(),
									abrechnungsvorschlag.getTelefonzeitenIId(), bdAnteil, zeile.getArtikelIId());
							em.merge(zv);
							em.flush();
							if (bErledigt) {
								Telefonzeiten zd = em.find(Telefonzeiten.class,
										abrechnungsvorschlag.getTelefonzeitenIId());
								zd.setTErledigt(new Timestamp(System.currentTimeMillis()));
								zd.setPersonalIIdErledigt(theClientDto.getIDPersonal());
								em.merge(zd);
								em.flush();
							}

						} else if (abrechnungsvorschlag.getMaschinenzeitdatenIId() != null) {

							PKGeneratorObj pkGen = new PKGeneratorObj();
							// PKGEN
							Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MASCHINENZEITENVERRECHNET);
							Maschinenzeitdatenverrechnet zv = new Maschinenzeitdatenverrechnet(pk, reposDto.getIId(),
									abrechnungsvorschlag.getMaschinenzeitdatenIId(), bdAnteil, zeile.getArtikelIId());
							em.merge(zv);
							em.flush();
							if (bErledigt) {
								Maschinenzeitdaten zd = em.find(Maschinenzeitdaten.class,
										abrechnungsvorschlag.getMaschinenzeitdatenIId());
								zd.setTErledigt(new Timestamp(System.currentTimeMillis()));
								zd.setPersonalIIdErledigt(theClientDto.getIDPersonal());
								em.merge(zd);
								em.flush();
							}

						}

						if (abrechnungsvorschlag.getNStundenOffen().subtract(bdAnteil).doubleValue() < 0) {
							abrechnungsvorschlag.setNStundenOffen(BigDecimal.ZERO);
						} else {
							abrechnungsvorschlag
									.setNStundenOffen(abrechnungsvorschlag.getNStundenOffen().subtract(bdAnteil));
						}
					}
					bdlfdWert = bdlfdWert.subtract(bdAnteil);

					if (bErledigt) {
						abrechnungsvorschlag.setBVerrechnet(Helper.boolean2Short(true));
					}

					em.merge(abrechnungsvorschlag);
					em.flush();

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

	}

	public void abrechnungsvorschlagUeberleitenReise(ArrayList<Integer> selektierteIIds, Integer artikelIId,
			BigDecimal bdBetragInRechnungswaehrung, String kommentar, Integer artikelIIdKilometer,
			BigDecimal bdKilometer, String kommentarKilometer, BigDecimal bdSpesenInRechnungswaehrung,
			String kommentarSpesen, Integer artikelIIdSpesen, boolean bErledigt, java.sql.Date tBelegdatum,
			TheClientDto theClientDto) {

		HashSet hsAnzahlKunden = new HashSet();

		ArrayList<Integer> ids_MitBetrag = new ArrayList<Integer>();

		Integer kundeIId = null;
		String waehrung = null;
		for (int i = 0; i < selektierteIIds.size(); i++) {
			Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, selektierteIIds.get(i));

			waehrung = abrechnungsvorschlag.getWaehrungCNrRechnung();

			if (abrechnungsvorschlag.getKundeIId() == null) {
				// Fehler -> Kunde wird benoetigt
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE"));
			} else {
				kundeIId = abrechnungsvorschlag.getKundeIId();
				hsAnzahlKunden.add(abrechnungsvorschlag.getKundeIId());
			}

			if (hsAnzahlKunden.size() > 1) {
				// Fehler -> Kann nur einen Kunden abrechnen
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN"));
			}

			if (abrechnungsvorschlag.getNBetragOffen() != null) {
				ids_MitBetrag.add(selektierteIIds.get(i));
			}

		}

		BigDecimal bdSelektiertBetrag = BigDecimal.ZERO;
		BigDecimal bdSelektierteKilometer = BigDecimal.ZERO;
		BigDecimal bdSelektierteSpesen = BigDecimal.ZERO;
		for (int i = 0; i < ids_MitBetrag.size(); i++) {
			Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, ids_MitBetrag.get(i));
			abrechnungsvorschlag.setBVerrechnet(Helper.boolean2Short(true));

			bdSelektiertBetrag = bdSelektiertBetrag.add(abrechnungsvorschlag.getNBetragOffen());
			bdSelektierteKilometer = bdSelektierteKilometer.add(abrechnungsvorschlag.getNKilometerOffen());
			if (abrechnungsvorschlag.getNSpesenOffen() != null) {
				bdSelektierteSpesen = bdSelektierteSpesen.add(abrechnungsvorschlag.getNSpesenOffen());
			}

			em.flush();

		}

		// Faktor berechnen, wenn nur Teilverrechnet

		BigDecimal bdFaktor = BigDecimal.ONE;
		BigDecimal bdFaktorKilometer = BigDecimal.ONE;
		BigDecimal bdFaktorSpesen = BigDecimal.ONE;

		if (bdBetragInRechnungswaehrung != null && bdSelektiertBetrag.doubleValue() != 0) {
			bdFaktor = bdBetragInRechnungswaehrung.divide(bdSelektiertBetrag, 10, BigDecimal.ROUND_HALF_EVEN);
		}

		if (bdKilometer != null && bdSelektierteKilometer.doubleValue() != 0) {
			bdFaktorKilometer = bdKilometer.divide(bdSelektierteKilometer, 10, BigDecimal.ROUND_HALF_EVEN);
		}

		if (bdSpesenInRechnungswaehrung != null && bdSelektierteSpesen.doubleValue() != 0) {
			bdFaktorSpesen = bdSpesenInRechnungswaehrung.divide(bdSelektierteSpesen, 10, BigDecimal.ROUND_HALF_EVEN);
		}

		// Rechnung + Position anlegen
		Integer rechnungIId = rechnungFindenOderAnlegen(kundeIId, tBelegdatum, waehrung, theClientDto);

		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

		ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		try {

			// POSITION FUER BETRAG

			RechnungPositionDto reposDtoBetrag = new RechnungPositionDto();
			reposDtoBetrag.setArtikelIId(artikelIId);
			reposDtoBetrag.setBelegIId(rechnungIId);
			reposDtoBetrag.setBDrucken(Helper.boolean2Short(true));
			reposDtoBetrag.setEinheitCNr(aDto.getEinheitCNr());
			reposDtoBetrag.setXTextinhalt(kommentar);
			reposDtoBetrag.setBNettopreisuebersteuert(Helper.boolean2Short(false));
			reposDtoBetrag.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_IDENT);

			RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);

			reposDtoBetrag.setNMaterialzuschlag(BigDecimal.ZERO);
			reposDtoBetrag.setBDrucken(Helper.boolean2Short(true));

			reposDtoBetrag.setNMenge(BigDecimal.ONE);

			reposDtoBetrag = (RechnungPositionDto) befuellePreisfelderAnhandVKPreisfindung(reposDtoBetrag,
					reDto.getTBelegdatum(), kundeDto.getIId(), reDto.getWaehrungCNr(), theClientDto);

			if (bdBetragInRechnungswaehrung != null) {

				reposDtoBetrag.setNEinzelpreis(bdBetragInRechnungswaehrung);
				reposDtoBetrag.setFRabattsatz(0D);
				reposDtoBetrag.setFZusatzrabattsatz(0D);
				reposDtoBetrag.setNMaterialzuschlag(null);
				reposDtoBetrag.setNNettoeinzelpreis(bdBetragInRechnungswaehrung);

				reposDtoBetrag = (RechnungPositionDto) getBelegVerkaufFac().berechneBelegpositionVerkauf(reposDtoBetrag,
						reDto);

				reposDtoBetrag = (RechnungPositionDto) mwstsatzBestimmenUndNeuBerechnen(reposDtoBetrag,
						kundeDto.getIId(), reDto.getTBelegdatum(), theClientDto);

			}

			reposDtoBetrag = getRechnungFac().createRechnungPosition(reposDtoBetrag,
					kundeDto.getLagerIIdAbbuchungslager(), theClientDto);

			// Kilometer

			// PJ21335
			RechnungPositionDto reposDtoKilometer = null;
			if (bdKilometer != null && bdKilometer.doubleValue() != 0) {
				reposDtoKilometer = new RechnungPositionDto();
				reposDtoKilometer.setArtikelIId(artikelIIdKilometer);
				reposDtoKilometer.setBelegIId(rechnungIId);
				reposDtoKilometer.setBDrucken(Helper.boolean2Short(true));
				reposDtoKilometer.setEinheitCNr(aDto.getEinheitCNr());
				reposDtoKilometer.setXTextinhalt(kommentarKilometer);
				reposDtoKilometer.setBNettopreisuebersteuert(Helper.boolean2Short(false));
				reposDtoKilometer.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_IDENT);

				reposDtoKilometer.setNMaterialzuschlag(BigDecimal.ZERO);
				reposDtoKilometer.setBDrucken(Helper.boolean2Short(true));

				reposDtoKilometer.setNMenge(bdKilometer);

				reposDtoKilometer = (RechnungPositionDto) befuellePreisfelderAnhandVKPreisfindung(reposDtoKilometer,
						reDto.getTBelegdatum(), kundeDto.getIId(), reDto.getWaehrungCNr(), theClientDto);

				if (reposDtoKilometer.getNNettoeinzelpreis() != null) {

					reposDtoKilometer.setNEinzelpreis(reposDtoKilometer.getNNettoeinzelpreis());
					reposDtoKilometer.setFRabattsatz(0D);
					reposDtoKilometer.setFZusatzrabattsatz(0D);
					reposDtoKilometer.setNMaterialzuschlag(null);
					reposDtoKilometer.setNNettoeinzelpreis(bdBetragInRechnungswaehrung);

					reposDtoKilometer = (RechnungPositionDto) getBelegVerkaufFac()
							.berechneBelegpositionVerkauf(reposDtoKilometer, reDto);

					reposDtoKilometer = (RechnungPositionDto) mwstsatzBestimmenUndNeuBerechnen(reposDtoKilometer,
							kundeDto.getIId(), reDto.getTBelegdatum(), theClientDto);
				}

				reposDtoKilometer = getRechnungFac().createRechnungPosition(reposDtoKilometer,
						kundeDto.getLagerIIdAbbuchungslager(), theClientDto);
			}

			// POSITION FUER SPESEN

			RechnungPositionDto reposDtoSpesen = null;

			// SP8279
			if (bdSpesenInRechnungswaehrung != null && bdSpesenInRechnungswaehrung.doubleValue() >= 0.01) {
				reposDtoSpesen = new RechnungPositionDto();
				reposDtoSpesen.setArtikelIId(artikelIIdSpesen);
				reposDtoSpesen.setBelegIId(rechnungIId);
				reposDtoSpesen.setBDrucken(Helper.boolean2Short(true));
				reposDtoSpesen.setEinheitCNr(aDto.getEinheitCNr());
				reposDtoSpesen.setXTextinhalt(kommentarSpesen);
				reposDtoSpesen.setBNettopreisuebersteuert(Helper.boolean2Short(false));
				reposDtoSpesen.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_IDENT);
				reposDtoSpesen.setNMaterialzuschlag(BigDecimal.ZERO);
				reposDtoSpesen.setBDrucken(Helper.boolean2Short(true));
				reposDtoSpesen.setNMenge(BigDecimal.ONE);

				reposDtoSpesen = (RechnungPositionDto) befuellePreisfelderAnhandVKPreisfindung(reposDtoSpesen,
						reDto.getTBelegdatum(), kundeDto.getIId(), reDto.getWaehrungCNr(), theClientDto);

				if (bdSpesenInRechnungswaehrung != null) {

					reposDtoSpesen.setNEinzelpreis(bdSpesenInRechnungswaehrung);
					reposDtoSpesen.setFRabattsatz(0D);
					reposDtoSpesen.setFZusatzrabattsatz(0D);
					reposDtoSpesen.setNMaterialzuschlag(null);
					reposDtoSpesen.setNNettoeinzelpreis(bdSpesenInRechnungswaehrung);

					reposDtoSpesen = (RechnungPositionDto) getBelegVerkaufFac()
							.berechneBelegpositionVerkauf(reposDtoSpesen, reDto);

					reposDtoSpesen = (RechnungPositionDto) mwstsatzBestimmenUndNeuBerechnen(reposDtoSpesen,
							kundeDto.getIId(), reDto.getTBelegdatum(), theClientDto);

				}

				reposDtoSpesen = getRechnungFac().createRechnungPosition(reposDtoSpesen,
						kundeDto.getLagerIIdAbbuchungslager(), theClientDto);
			}

			for (int i = 0; i < selektierteIIds.size(); i++) {
				Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, selektierteIIds.get(i));

				// ZUERST BETRAG

				BigDecimal bdAbgerechnetInMandantenwaehrung = Helper
						.rundeGeldbetrag(abrechnungsvorschlag.getNBetragOffen().multiply(bdFaktor));

				if (abrechnungsvorschlag.getReiseIId() != null) {

					PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
					Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_REISEVERRECHNET);
					Reiseverrechnet zv = new Reiseverrechnet(pk, reposDtoBetrag.getIId(),
							abrechnungsvorschlag.getReiseIId(), bdAbgerechnetInMandantenwaehrung, BigDecimal.ZERO,
							BigDecimal.ZERO);
					em.merge(zv);
					em.flush();
					if (bErledigt) {
						Reise zd = em.find(Reise.class, abrechnungsvorschlag.getReiseIId());
						zd.setTErledigt(new Timestamp(System.currentTimeMillis()));
						zd.setPersonalIIdErledigt(theClientDto.getIDPersonal());
						em.merge(zd);
						em.flush();
					}

				}

				if (abrechnungsvorschlag.getNBetragOffen().subtract(bdAbgerechnetInMandantenwaehrung)
						.doubleValue() < 0) {
					abrechnungsvorschlag.setNBetragOffen(BigDecimal.ZERO);
				} else {
					abrechnungsvorschlag.setNBetragOffen(
							abrechnungsvorschlag.getNBetragOffen().subtract(bdAbgerechnetInMandantenwaehrung));
				}

				em.merge(abrechnungsvorschlag);
				em.flush();

				// DANN KILOMETER
				BigDecimal bdAbgerechneteKilometer = Helper
						.rundeGeldbetrag(abrechnungsvorschlag.getNKilometerOffen().multiply(bdFaktorKilometer));

				if (abrechnungsvorschlag.getReiseIId() != null) {
					if (reposDtoKilometer != null) {
						PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
						Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_REISEVERRECHNET);
						Reiseverrechnet zv = new Reiseverrechnet(pk, reposDtoKilometer.getIId(),
								abrechnungsvorschlag.getReiseIId(), BigDecimal.ZERO, bdAbgerechneteKilometer,
								BigDecimal.ZERO);
						em.merge(zv);
						em.flush();
					}
					if (bErledigt) {
						Reise zd = em.find(Reise.class, abrechnungsvorschlag.getReiseIId());
						zd.setTErledigt(new Timestamp(System.currentTimeMillis()));
						zd.setPersonalIIdErledigt(theClientDto.getIDPersonal());
						em.merge(zd);
						em.flush();
					}

				}

				if (abrechnungsvorschlag.getNKilometerOffen().subtract(bdAbgerechneteKilometer).doubleValue() < 0) {
					abrechnungsvorschlag.setNKilometerOffen(BigDecimal.ZERO);
				} else {
					abrechnungsvorschlag.setNKilometerOffen(
							abrechnungsvorschlag.getNKilometerOffen().subtract(bdAbgerechneteKilometer));
				}

				// UndSpesen
				// DANN KILOMETER
				BigDecimal bdAbgerechneteSpesen = Helper
						.rundeGeldbetrag(abrechnungsvorschlag.getNSpesenOffen().multiply(bdFaktorSpesen));

				if (abrechnungsvorschlag.getReiseIId() != null) {
					if (reposDtoSpesen != null) {
						PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
						Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_REISEVERRECHNET);
						Reiseverrechnet zv = new Reiseverrechnet(pk, reposDtoSpesen.getIId(),
								abrechnungsvorschlag.getReiseIId(), BigDecimal.ZERO, BigDecimal.ZERO,
								bdAbgerechneteSpesen);
						em.merge(zv);
						em.flush();
					}
					if (bErledigt) {
						Reise zd = em.find(Reise.class, abrechnungsvorschlag.getReiseIId());
						zd.setTErledigt(new Timestamp(System.currentTimeMillis()));
						zd.setPersonalIIdErledigt(theClientDto.getIDPersonal());
						em.merge(zd);
						em.flush();
					}

				}

				if (abrechnungsvorschlag.getNSpesenOffen().subtract(bdAbgerechneteSpesen).doubleValue() < 0) {
					abrechnungsvorschlag.setNSpesenOffen(BigDecimal.ZERO);
				} else {
					abrechnungsvorschlag
							.setNSpesenOffen(abrechnungsvorschlag.getNSpesenOffen().subtract(bdAbgerechneteSpesen));
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void abrechnungsvorschlagUeberleitenER(ArrayList<Integer> selektierteIIds, Integer artikelIId,
			BigDecimal bdBetragInRechnungswaehrung, BigDecimal bdBetragInRechnungswaehrungInclAufschlag,
			String kommentar, boolean bErledigt, java.sql.Date tBelegdatum, TheClientDto theClientDto) {

		HashSet hsAnzahlKunden = new HashSet();

		ArrayList<Integer> ids_MitBetrag = new ArrayList<Integer>();

		Integer kundeIId = null;
		String waehrung = null;
		for (int i = 0; i < selektierteIIds.size(); i++) {
			Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, selektierteIIds.get(i));

			waehrung = abrechnungsvorschlag.getWaehrungCNrRechnung();

			if (abrechnungsvorschlag.getKundeIId() == null) {
				// Fehler -> Kunde wird benoetigt
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE"));
			} else {
				kundeIId = abrechnungsvorschlag.getKundeIId();
				hsAnzahlKunden.add(abrechnungsvorschlag.getKundeIId());
			}

			if (hsAnzahlKunden.size() > 1) {
				// Fehler -> Kann nur einen Kunden abrechnen
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN"));
			}

			if (abrechnungsvorschlag.getNBetragOffen() != null) {
				ids_MitBetrag.add(selektierteIIds.get(i));
			}

		}

		BigDecimal bdSelektiertBetrag = BigDecimal.ZERO;
		for (int i = 0; i < ids_MitBetrag.size(); i++) {
			Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, ids_MitBetrag.get(i));
			abrechnungsvorschlag.setBVerrechnet(Helper.boolean2Short(true));

			bdSelektiertBetrag = bdSelektiertBetrag.add(abrechnungsvorschlag.getNBetragOffen());

			em.flush();

		}

		// Faktor berechnen, wenn nur Teilverrechnet

		BigDecimal bdFaktor = BigDecimal.ONE;

		if (bdBetragInRechnungswaehrung != null && bdSelektiertBetrag.doubleValue() != 0) {
			bdFaktor = bdBetragInRechnungswaehrung.divide(bdSelektiertBetrag, 10, BigDecimal.ROUND_HALF_EVEN);
		}

		// Rechnung + Position anlegen
		Integer rechnungIId = rechnungFindenOderAnlegen(kundeIId, tBelegdatum, waehrung, theClientDto);

		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

		ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);

		RechnungPositionDto reposDto = new RechnungPositionDto();
		reposDto.setArtikelIId(artikelIId);
		reposDto.setBelegIId(rechnungIId);
		reposDto.setBDrucken(Helper.boolean2Short(true));
		reposDto.setEinheitCNr(aDto.getEinheitCNr());
		reposDto.setXTextinhalt(kommentar);
		reposDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
		reposDto.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_IDENT);

		try {
			RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);

			reposDto.setNMaterialzuschlag(BigDecimal.ZERO);
			reposDto.setBDrucken(Helper.boolean2Short(true));

			reposDto.setNMenge(BigDecimal.ONE);

			reposDto = (RechnungPositionDto) befuellePreisfelderAnhandVKPreisfindung(reposDto, reDto.getTBelegdatum(),
					kundeDto.getIId(), reDto.getWaehrungCNr(), theClientDto);

			if (bdBetragInRechnungswaehrungInclAufschlag != null) {

				reposDto.setNEinzelpreis(bdBetragInRechnungswaehrungInclAufschlag);
				reposDto.setFRabattsatz(0D);
				reposDto.setFZusatzrabattsatz(0D);
				reposDto.setNMaterialzuschlag(null);
				reposDto.setNNettoeinzelpreis(bdBetragInRechnungswaehrungInclAufschlag);

				reposDto = (RechnungPositionDto) getBelegVerkaufFac().berechneBelegpositionVerkauf(reposDto, reDto);

				reposDto = (RechnungPositionDto) mwstsatzBestimmenUndNeuBerechnen(reposDto, kundeDto.getIId(),
						reDto.getTBelegdatum(), theClientDto);

			}

			reposDto = getRechnungFac().createRechnungPosition(reposDto, kundeDto.getLagerIIdAbbuchungslager(),
					theClientDto);

			for (int i = 0; i < selektierteIIds.size(); i++) {
				Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, selektierteIIds.get(i));

				if (bdBetragInRechnungswaehrung != null) {
					BigDecimal bdAbgerechnetInRechnungswaehrung = Helper
							.rundeGeldbetrag(abrechnungsvorschlag.getNBetragOffen().multiply(bdFaktor));

					if (abrechnungsvorschlag.getAuftragszuordnungIId() != null) {
						EingangsrechnungAuftragszuordnung zd = em.find(EingangsrechnungAuftragszuordnung.class,
								abrechnungsvorschlag.getAuftragszuordnungIId());

						PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
						Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AUFTRAGSZUORDNUNGVERRECHNET);
						Auftragszuordnungverrechnet zv = new Auftragszuordnungverrechnet(pk, reposDto.getIId(),
								abrechnungsvorschlag.getAuftragszuordnungIId(), bdAbgerechnetInRechnungswaehrung);
						em.merge(zv);
						em.flush();
						if (bErledigt) {

							zd.setTErledigt(new Timestamp(System.currentTimeMillis()));
							zd.setPersonalIIdErledigt(theClientDto.getIDPersonal());
							em.merge(zd);
							em.flush();
						}

					}

					if (abrechnungsvorschlag.getNBetragOffen().subtract(bdAbgerechnetInRechnungswaehrung)
							.doubleValue() < 0) {
						abrechnungsvorschlag.setNBetragOffen(BigDecimal.ZERO);
					} else {
						abrechnungsvorschlag.setNBetragOffen(
								abrechnungsvorschlag.getNBetragOffen().subtract(bdAbgerechnetInRechnungswaehrung));
					}

				}

				em.merge(abrechnungsvorschlag);
				em.flush();

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	private Integer rechnungFindenOderAnlegen(Integer kundeIId, java.sql.Date tBelegdatum, String waehrungCNr,
			TheClientDto theClientDto) {
		String sQuery2 = "SELECT r" + " FROM FLRRechnung AS r WHERE r.flrkunde.i_id=" + kundeIId
				+ " AND r.status_c_nr='" + LocaleFac.STATUS_ANGELEGT + "' AND r.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND r.flrrechnungart.c_nr='" + RechnungFac.RECHNUNGART_RECHNUNG
				+ "' AND r.waehrung_c_nr='" + waehrungCNr + "' ORDER BY r.c_nr DESC";
		Session session2 = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query rechnungen = session2.createQuery(sQuery2);
		rechnungen.setMaxResults(1);
		List<?> resultListR = rechnungen.list();

		Integer letzteRechnungIId = null;

		Integer lagerIId = null;

		if (resultListR.iterator().hasNext()) {
			// Bestehende verwenden
			FLRRechnung r = (FLRRechnung) resultListR.iterator().next();
			return r.getI_id();
		} else {
			// neue anlegen

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

			RechnungDto reDto = new RechnungDto();
			reDto.setKundeIId(kundeIId);
			reDto.setKundeIIdStatistikadresse(kundeIId);

			reDto.setRechnungartCNr(RechnungFac.RECHNUNGART_RECHNUNG);

			reDto.setPersonalIIdVertreter(theClientDto.getIDPersonal());

			reDto.setLagerIId(kdDto.getLagerIIdAbbuchungslager());

			reDto.setWaehrungCNr(waehrungCNr);

			if (tBelegdatum != null) {
				reDto.setTBelegdatum(Helper.cutTimestamp(new Timestamp(tBelegdatum.getTime())));
			} else {
				reDto.setTBelegdatum(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));
			}

			reDto.setMandantCNr(theClientDto.getMandant());

			reDto.setKostenstelleIId(kdDto.getKostenstelleIId());

			reDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

			reDto.setLieferartIId(kdDto.getLieferartIId());
			reDto.setSpediteurIId(kdDto.getSpediteurIId());
			reDto.setZahlungszielIId(kdDto.getZahlungszielIId());
			try {

				WechselkursDto kursDto = getLocaleFac().getKursZuDatum(theClientDto.getSMandantenwaehrung(),
						waehrungCNr, new java.sql.Date(reDto.getTBelegdatum().getTime()), theClientDto);

				BigDecimal bdKurs = kursDto.getNKurs().setScale(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
						BigDecimal.ROUND_HALF_EVEN);
				reDto.setNKurs(bdKurs);

				return getRechnungFac().createRechnung(reDto, theClientDto).getIId();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
				return null;
			}

		}
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(1000)
	public void selektiertePositionenManuellErledigen(ArrayList<Integer> iids, TheClientDto theClientDto) {

		while (iids.size() > 0) {

			int iBis = 1000;
			if (iids.size() < 1000) {
				iBis = iids.size();
			}
			List<Integer> alSub = iids.subList(0, iBis);

			Session session = FLRSessionFactory.getFactory().openSession();
			Criteria crit = session.createCriteria(FLRAbrechnungsvorschlag.class);
			crit.add(Restrictions.in("i_id", alSub));

			@SuppressWarnings("unchecked")
			List<FLRAbrechnungsvorschlag> list = new ArrayList<FLRAbrechnungsvorschlag>(crit.list());

			int iAnzahlVerrechnet = 0;

			for (FLRAbrechnungsvorschlag bd : list) {
				if (Helper.short2Boolean(bd.getB_verrechnet())) {
					iAnzahlVerrechnet++;
				}
			}

			if (iAnzahlVerrechnet == 0) {

				for (FLRAbrechnungsvorschlag bd : list) {

					if (bd.getZeitdaten_i_id() != null) {
						Zeitdaten bean = em.find(Zeitdaten.class, bd.getZeitdaten_i_id());
						bean.setTErledigt(getTimestamp());
						bean.setPersonalIIdErledigt(theClientDto.getIDPersonal());

					} else if (bd.getReise_i_id() != null) {
						Reise bean = em.find(Reise.class, bd.getReise_i_id());
						bean.setTErledigt(getTimestamp());
						bean.setPersonalIIdErledigt(theClientDto.getIDPersonal());
					} else if (bd.getMaschinenzeitdaten_i_id() != null) {
						Maschinenzeitdaten bean = em.find(Maschinenzeitdaten.class, bd.getMaschinenzeitdaten_i_id());
						bean.setTErledigt(getTimestamp());
						bean.setPersonalIIdErledigt(theClientDto.getIDPersonal());
					} else if (bd.getTelefonzeiten_i_id() != null) {
						Telefonzeiten bean = em.find(Telefonzeiten.class, bd.getTelefonzeiten_i_id());
						bean.setTErledigt(getTimestamp());
						bean.setPersonalIIdErledigt(theClientDto.getIDPersonal());
					} else if (bd.getAuftragszuordnung_i_id() != null) {
						EingangsrechnungAuftragszuordnung bean = em.find(EingangsrechnungAuftragszuordnung.class,
								bd.getAuftragszuordnung_i_id());
						bean.setTErledigt(getTimestamp());
						bean.setPersonalIIdErledigt(theClientDto.getIDPersonal());
					}

					Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, bd.getI_id());

					abrechnungsvorschlag.setBVerrechnet(Helper.boolean2Short(true));
					em.merge(abrechnungsvorschlag);
					em.flush();

				}
			} else if (iAnzahlVerrechnet == alSub.size()) {
				for (FLRAbrechnungsvorschlag bd : list) {

					if (bd.getZeitdaten_i_id() != null) {
						Zeitdaten bean = em.find(Zeitdaten.class, bd.getZeitdaten_i_id());
						bean.setTErledigt(null);
						bean.setPersonalIIdErledigt(null);

					} else if (bd.getReise_i_id() != null) {
						Reise bean = em.find(Reise.class, bd.getReise_i_id());
						bean.setTErledigt(null);
						bean.setPersonalIIdErledigt(null);

					} else if (bd.getMaschinenzeitdaten_i_id() != null) {
						Maschinenzeitdaten bean = em.find(Maschinenzeitdaten.class, bd.getMaschinenzeitdaten_i_id());
						bean.setTErledigt(null);
						bean.setPersonalIIdErledigt(null);

					} else if (bd.getTelefonzeiten_i_id() != null) {
						Telefonzeiten bean = em.find(Telefonzeiten.class, bd.getTelefonzeiten_i_id());
						bean.setTErledigt(null);
						bean.setPersonalIIdErledigt(null);

					} else if (bd.getAuftragszuordnung_i_id() != null) {
						EingangsrechnungAuftragszuordnung bean = em.find(EingangsrechnungAuftragszuordnung.class,
								bd.getAuftragszuordnung_i_id());
						bean.setTErledigt(null);
						bean.setPersonalIIdErledigt(null);

					}

					Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, bd.getI_id());

					abrechnungsvorschlag.setBVerrechnet(Helper.boolean2Short(false));
					em.merge(abrechnungsvorschlag);
					em.flush();

				}
			} else {

				// FEHLER GEMISCHT

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_MANUELL_ERLEDIGEN,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_MANUELL_ERLEDIGEN"));
			}
			iids.removeAll(alSub);
		}

	}

	public void selektiertenPositionenKundeZuordnen(ArrayList<Integer> iids, Integer kundeIId,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria crit = session.createCriteria(FLRAbrechnungsvorschlag.class);
		crit.add(Restrictions.in("i_id", iids.toArray()));

		KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

		@SuppressWarnings("unchecked")
		List<FLRAbrechnungsvorschlag> list = new ArrayList<FLRAbrechnungsvorschlag>(crit.list());

		for (FLRAbrechnungsvorschlag bd : list) {

			Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, bd.getI_id());
			abrechnungsvorschlag.setKundeIId(kundeIId);
			em.merge(abrechnungsvorschlag);
			em.flush();

			if (abrechnungsvorschlag.getTelefonzeitenIId() != null) {
				Telefonzeiten tz = em.find(Telefonzeiten.class, abrechnungsvorschlag.getTelefonzeitenIId());
				tz.setPartnerIId(kdDto.getPartnerIId());
				tz.setAnsprechpartnerIId(null);
				em.merge(tz);
				em.flush();
			}

		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void erstelleAbrechnungsvorschlag(java.sql.Date dStichtag, boolean bAltenLoeschen, boolean bMitZeitdaten,
			boolean bMitTelefonzeiten, boolean bMitReisezeiten, boolean bMitEingangsrechnungen,
			boolean bMitMaschinenzeiten, TheClientDto theClientDto) {

		long lStart = System.currentTimeMillis();

		// Einstellungen speichern
		ArrayList<KeyvalueDto> alDtos = new ArrayList<KeyvalueDto>();

		alDtos.add(new KeyvalueDto("ErzeugtAm",
				Helper.formatTimestamp(new java.sql.Timestamp(System.currentTimeMillis()), theClientDto.getLocUi())));
		alDtos.add(new KeyvalueDto("ErzeugtVon", getPersonalFac()
				.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto).getCKurzzeichen()));

		// 7720
		getSystemServicesFac().replaceKeyvaluesEinerGruppe(
				SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTER_ABRECHUNGSVORSCHLAG + theClientDto.getMandant(),
				alDtos);

		try {
			// Zuerst den alten loeschen
			if (bAltenLoeschen) {
				loescheAbrechnungsvorschlagEinesMandaten(theClientDto);
			}

			if (dStichtag != null) {

				dStichtag = Helper.addiereTageZuDatum(dStichtag, 1);
			}

			if (bMitZeitdaten) {
				erstelleAbrechnungsvorschlagZeitdaten(dStichtag, null, theClientDto);
			}

			if (bMitEingangsrechnungen) {
				erstelleAbrechnungsvorschlagER(dStichtag, null, theClientDto);
			}
			// Reise
			if (bMitReisezeiten) {
				erstelleAbrechnungsvorschlagReise(dStichtag, null, theClientDto);
			}
			if (bMitTelefonzeiten) {
				// Telefonzeiten

				erstelleAbrechnungsvorschlagTelefonzeiten(dStichtag, null, theClientDto);
			}
			if (bMitMaschinenzeiten) {
				erstelleAbrechnungsvorschlagMaschinenzeitdaten(dStichtag, null, theClientDto);
			}

			alDtos.add(new KeyvalueDto("Stichtag", Helper.formatDatum(dStichtag, theClientDto.getLocUi())));
			alDtos.add(new KeyvalueDto("AltenLoeschen", bAltenLoeschen + ""));
			alDtos.add(new KeyvalueDto("MitZeitdaten", bMitZeitdaten + ""));
			alDtos.add(new KeyvalueDto("MitTelefonzeiten", bMitTelefonzeiten + ""));
			alDtos.add(new KeyvalueDto("MitReisezeiten", bMitReisezeiten + ""));
			alDtos.add(new KeyvalueDto("MitEingangsrechnungen", bMitEingangsrechnungen + ""));
			alDtos.add(new KeyvalueDto("MitMaschinenzeiten", bMitMaschinenzeiten + ""));
			alDtos.add(new KeyvalueDto("DauerInSekunden", new Long((System.currentTimeMillis() - lStart) / 1000) + ""));
			getSystemServicesFac().replaceKeyvaluesEinerGruppe(
					SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTER_ABRECHUNGSVORSCHLAG + theClientDto.getMandant(),
					alDtos);

		} catch (EJBExceptionLP e) {

			alDtos.add(new KeyvalueDto("FehlerBeiAbbruch", e.getCause().getMessage()));
			getSystemServicesFac().replaceKeyvaluesEinerGruppe(
					SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTER_ABRECHUNGSVORSCHLAG + theClientDto.getMandant(),
					alDtos);
			throw e;

		}

	}

	public void erstelleAbrechnungsvorschlagReise(java.sql.Date dStichtag, Integer abrechnungsvorschlagIIdVorhanden,
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT  r.personal_i_id, min(r.t_zeit), max(r.t_zeit) FROM FLRReise r WHERE r.b_beginn = 1 AND r.t_erledigt IS NULL ";

		if (dStichtag != null) {
			sQuery += " AND r.t_zeit <='" + Helper.formatDateWithSlashes(dStichtag) + "' ";
		}
		sQuery += " AND flrpersonal.mandant_c_nr='" + theClientDto.getMandant() + "' GROUP BY r.personal_i_id";

		org.hibernate.Query queryFirst = session.createQuery(sQuery);

		List<?> resultList = queryFirst.list();
		Iterator<?> resultListIterator = resultList.iterator();

		Integer landIIdHeimat = null;
		try {
			Integer partnerMandant = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto)
					.getPartnerIId();
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerMandant, theClientDto);

			landIIdHeimat = null;
			if (partnerDto.getLandplzortDto() != null) {
				landIIdHeimat = partnerDto.getLandplzortDto().getIlandID();
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		ArrayList<AbschnittEinerReiseDto> abschnitteAllerPersonen = new ArrayList<AbschnittEinerReiseDto>();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			Integer personalIId = (Integer) o[0];
			java.sql.Timestamp tVon = (java.sql.Timestamp) o[1];
			java.sql.Timestamp tBis = (java.sql.Timestamp) o[2];

			try {
				Query query = em.createNamedQuery("ReisefindByPersonalIIdTZeit");
				query.setParameter(1, personalIId);
				query.setParameter(2, tBis);
				Reise reise = (Reise) query.getSingleResult();

				if (reise != null && Helper.short2boolean(reise.getBBeginn())) {
					// naechstes Ende suchen

					String sQuerySub = "SELECT  r FROM FLRReise r WHERE r.personal_i_id=" + personalIId
							+ " AND r.b_beginn = 0 ";

					sQuerySub += " AND r.t_zeit >'" + Helper.formatTimestampWithSlashes(tBis) + "' ORDER BY r.t_zeit";
					Session session2 = FLRSessionFactory.getFactory().openSession();
					org.hibernate.Query queryNaechstesEnde = session.createQuery(sQuerySub);
					queryNaechstesEnde.setMaxResults(1);

					List<?> resultListNaechstesEnde = queryNaechstesEnde.list();
					if (resultListNaechstesEnde.iterator().hasNext()) {
						tBis = new Timestamp(
								((FLRReise) resultListNaechstesEnde.iterator().next()).getT_zeit().getTime());
					}
					session2.close();
				}

			} catch (NoResultException ex1) {
				// nothing here
			}

			ArrayList<AbschnittEinerReiseDto> abschnitteEinerPerson = getReisekostenFac()
					.erstelleEinzelneReiseeintraege(tVon, tBis, theClientDto, landIIdHeimat, personalIId, false);
			ReisezeitenEinerPersonDto rzDto = getReisekostenFac()
					.getSummenDerEinzelnenTageUndVerteileKostenAufAbschnitte(abschnitteEinerPerson, theClientDto);
			abschnitteEinerPerson = rzDto.getAlAbschnitteEinerReise();

			for (int k = 0; k < abschnitteEinerPerson.size(); k++) {
				AbschnittEinerReiseDto abschnittEinerReiseDto = (AbschnittEinerReiseDto) abschnitteEinerPerson.get(k);

				if (abschnittEinerReiseDto.getTErledigt() == null) {
					abschnitteAllerPersonen.add(abschnittEinerReiseDto);
				}
			}

		}

		AbrechnungsvorschlagDto avDtoVorher = null;
		if (abrechnungsvorschlagIIdVorhanden != null) {
			avDtoVorher = abrechnungsvorschlagFindByPrimaryKey(abrechnungsvorschlagIIdVorhanden, theClientDto);
			Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, avDtoVorher.getIId());
			em.remove(abrechnungsvorschlag);
		}

		Iterator it = abschnitteAllerPersonen.iterator();
		while (it.hasNext()) {
			AbschnittEinerReiseDto abschnittEinerReiseDto = (AbschnittEinerReiseDto) it.next();

			AbrechnungsvorschlagDto dto = new AbrechnungsvorschlagDto();
			if (avDtoVorher != null) {
				dto.setTAnlegen(avDtoVorher.getTAnlegen());
			}

			dto.setMandantCNr(theClientDto.getMandant());
			dto.setReiseIId(abschnittEinerReiseDto.getReiseIId());
			dto.setTVon(abschnittEinerReiseDto.gettBeginn());
			dto.setPersonalIId(abschnittEinerReiseDto.getPersonalIId());

			dto.setFVerrechenbar(abschnittEinerReiseDto.getFVerrechenbar());

			if (abschnittEinerReiseDto.getBelegartCNr() != null && abschnittEinerReiseDto.getBelegartIId() != null) {

				if (abschnittEinerReiseDto.getBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {
					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByPrimaryKeyOhneExc(abschnittEinerReiseDto.getBelegartIId());
					if (auftragDto != null) {
						dto.setAuftragIId(auftragDto.getIId());
						dto.setKundeIId(auftragDto.getKundeIIdRechnungsadresse());
						dto.setWaehrungCNrRechnung(auftragDto.getCAuftragswaehrung());
					}
				} else if (abschnittEinerReiseDto.getBelegartCNr().equals(LocaleFac.BELEGART_PROJEKT)) {
					ProjektDto pjDto = getProjektFac()
							.projektFindByPrimaryKeyOhneExc(abschnittEinerReiseDto.getBelegartIId());
					if (pjDto != null) {
						dto.setProjektIId(abschnittEinerReiseDto.getBelegartIId());

						try {
							KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(pjDto.getPartnerIId(),
									theClientDto.getMandant(), theClientDto);
							if (kdDto != null) {
								dto.setKundeIId(kdDto.getIId());
								dto.setWaehrungCNrRechnung(kdDto.getCWaehrung());
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

					}
				}
			}

			if (dto.getKundeIId() == null && abschnittEinerReiseDto.getPartnerIId() != null) {
				try {
					KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
							abschnittEinerReiseDto.getPartnerIId(), theClientDto.getMandant(), theClientDto);
					if (kdDto != null) {
						dto.setKundeIId(kdDto.getIId());
						dto.setWaehrungCNrRechnung(kdDto.getCWaehrung());
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			Timestamp tBis = abschnittEinerReiseDto.gettEnde();

			dto.setTBis(tBis);

			// Spesen
			BigDecimal spesen = BigDecimal.ZERO;

			if (abschnittEinerReiseDto.getSpesen() != null) {
				spesen = spesen.add(abschnittEinerReiseDto.getSpesen());

			}

			// Umrechnen
			if (dto.getWaehrungCNrRechnung() != null) {
				try {
					spesen = getLocaleFac().rechneUmInAndereWaehrungZuDatum(spesen,
							theClientDto.getSMandantenwaehrung(), dto.getWaehrungCNrRechnung(),
							new java.sql.Date(tBis.getTime()), theClientDto);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			dto.setNSpesenGesamt(spesen);

			// Dieaeten
			BigDecimal bdDiaeten = abschnittEinerReiseDto.getBdAnteiligeKostenAusTageweisenDiaeten();

			// Umrechnen
			if (dto.getWaehrungCNrRechnung() != null) {
				try {
					spesen = getLocaleFac().rechneUmInAndereWaehrungZuDatum(bdDiaeten,
							theClientDto.getSMandantenwaehrung(), dto.getWaehrungCNrRechnung(),
							new java.sql.Date(tBis.getTime()), theClientDto);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			dto.setNBetragGesamt(bdDiaeten);

			if (abschnittEinerReiseDto.getEntfernungInKm() != null) {
				dto.setNKilometerGesamt(new BigDecimal(abschnittEinerReiseDto.getEntfernungInKm()));
			} else {
				dto.setNKilometerGesamt(new BigDecimal(0));
			}

			dto = berechneVerrechnenbar(dto);

			// bereits abgerechneten Betrag beruecksichtigen

			BigDecimal bdBetragOffen = dto.getNBetragVerrechenbar();
			BigDecimal bdKilometerOffen = dto.getNKilometerVerrechenbar();
			BigDecimal bdSpesenOffen = dto.getNSpesenVerrechenbar();

			Query query = em.createNamedQuery("ReiseverrechnetFindByReiseIId");
			query.setParameter(1, abschnittEinerReiseDto.getReiseIId());
			Collection<?> cl = query.getResultList();
			Iterator it2 = cl.iterator();
			while (it2.hasNext()) {
				Reiseverrechnet zv = (Reiseverrechnet) it2.next();
				bdBetragOffen = bdBetragOffen.subtract(zv.getNBetrag());
				bdKilometerOffen = bdKilometerOffen.subtract(zv.getNKilometer());
				bdSpesenOffen = bdSpesenOffen.subtract(zv.getNSpesen());
			}

			if (bdBetragOffen.doubleValue() < 0) {
				bdBetragOffen = BigDecimal.ZERO;
			}

			if (bdKilometerOffen.doubleValue() < 0) {
				bdKilometerOffen = BigDecimal.ZERO;
			}

			if (bdSpesenOffen.doubleValue() < 0) {
				bdSpesenOffen = BigDecimal.ZERO;
			}

			dto.setNBetragOffen(bdBetragOffen);
			dto.setNKilometerOffen(bdKilometerOffen);
			dto.setNSpesenOffen(bdSpesenOffen);

			getAbrechnungsvorschlagFac().createAbrechnungsvorschlag(dto, theClientDto);
		}

	}

	public void erstelleAbrechnungsvorschlagMaschinenzeitdaten(java.sql.Date dStichtag,
			Integer abrechnungsvorschlagIIdVorhanden, TheClientDto theClientDto) {
		// Maschinenzeiten

		String sQueryAuftragzeiten = "SELECT t FROM FLRMaschinenzeitdaten t WHERE t.flrmaschine.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND t.t_bis IS NOT NULL AND t.t_erledigt IS NULL";

		AbrechnungsvorschlagDto avDtoVorher = null;
		if (abrechnungsvorschlagIIdVorhanden != null) {
			avDtoVorher = abrechnungsvorschlagFindByPrimaryKey(abrechnungsvorschlagIIdVorhanden, theClientDto);
			sQueryAuftragzeiten += " AND t.i_id=" + avDtoVorher.getMaschinenzeitdatenIId();

			Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, avDtoVorher.getIId());
			em.remove(abrechnungsvorschlag);
		} else {

			if (dStichtag != null) {
				sQueryAuftragzeiten += " AND t.t_bis < '" + Helper.formatDateWithSlashes(dStichtag) + "'";
			}
		}

		sQueryAuftragzeiten += " ORDER BY t.t_von ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query zeiten = session.createQuery(sQueryAuftragzeiten);

		List<?> resultList = zeiten.list();

		Iterator<?> resultListAuftraege = resultList.iterator();

		while (resultListAuftraege.hasNext()) {
			FLRMaschinenzeitdaten flrMaschinenzeitdaten = (FLRMaschinenzeitdaten) resultListAuftraege.next();

			AbrechnungsvorschlagDto dto = new AbrechnungsvorschlagDto();
			if (avDtoVorher != null) {
				dto.setTAnlegen(avDtoVorher.getTAnlegen());
			}
			dto.setMandantCNr(theClientDto.getMandant());
			dto.setMaschinenzeitdatenIId(flrMaschinenzeitdaten.getI_id());
			dto.setPersonalIId(flrMaschinenzeitdaten.getPersonal_i_id_gestartet());
			dto.setFVerrechenbar(flrMaschinenzeitdaten.getF_verrechenbar());
			dto.setTVon(new Timestamp(flrMaschinenzeitdaten.getT_von().getTime()));
			dto.setTBis(new Timestamp(flrMaschinenzeitdaten.getT_bis().getTime()));

			long lDauer = flrMaschinenzeitdaten.getT_bis().getTime() - flrMaschinenzeitdaten.getT_von().getTime();

			dto.setNStundenGesamt(new BigDecimal(Helper.timeInMillis2Double(lDauer)));

			dto = berechneVerrechnenbar(dto);

			if (flrMaschinenzeitdaten.getFlrlossollarbeitsplan() != null) {

				dto.setLosIId(flrMaschinenzeitdaten.getFlrlossollarbeitsplan().getFlrlos().getI_id());

				if (flrMaschinenzeitdaten.getFlrlossollarbeitsplan().getFlrlos().getFlrauftrag() != null) {

					dto.setAuftragIId(
							flrMaschinenzeitdaten.getFlrlossollarbeitsplan().getFlrlos().getFlrauftrag().getI_id());
					dto.setKundeIId(flrMaschinenzeitdaten.getFlrlossollarbeitsplan().getFlrlos().getFlrauftrag()
							.getKunde_i_id_rechnungsadresse());
					dto.setWaehrungCNrRechnung(flrMaschinenzeitdaten.getFlrlossollarbeitsplan().getFlrlos()
							.getFlrauftrag().getWaehrung_c_nr_auftragswaehrung());

				} else if (flrMaschinenzeitdaten.getFlrlossollarbeitsplan().getFlrlos().getFlrkunde() != null) {
					dto.setKundeIId(
							flrMaschinenzeitdaten.getFlrlossollarbeitsplan().getFlrlos().getFlrkunde().getI_id());
					dto.setWaehrungCNrRechnung(flrMaschinenzeitdaten.getFlrlossollarbeitsplan().getFlrlos()
							.getFlrkunde().getWaehrung_c_nr());
				}

				// bereits abgerechnete Stunden beruecksichtigen

				BigDecimal bdStundenOffen = dto.getNStundenVerrechenbar();

				Query query = em.createNamedQuery("MaschinenzeitdatenverrechnetFindByMaschinenzeitdatenIId");
				query.setParameter(1, flrMaschinenzeitdaten.getI_id());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Maschinenzeitdatenverrechnet zv = (Maschinenzeitdatenverrechnet) it.next();
					bdStundenOffen = bdStundenOffen.subtract(zv.getNStunden());
				}

				if (bdStundenOffen.doubleValue() < 0) {
					bdStundenOffen = BigDecimal.ZERO;
				}
				dto.setNStundenOffen(bdStundenOffen);

				getAbrechnungsvorschlagFac().createAbrechnungsvorschlag(dto, theClientDto);

			}
		}
	}

	public void erstelleAbrechnungsvorschlagZeitdaten(java.sql.Date dStichtag, Integer abrechnungsvorschlagIIdVorhanden,
			TheClientDto theClientDto) {
		// Zeitdaten
		String sQueryAuftragzeiten = "SELECT z FROM FLRZeitdaten z WHERE z.c_belegartnr IN ('"
				+ LocaleFac.BELEGART_AUFTRAG + "','" + LocaleFac.BELEGART_LOS + "','" + LocaleFac.BELEGART_PROJEKT
				+ "') AND z.flrpersonal.mandant_c_nr='" + theClientDto.getMandant() + "' AND z.t_erledigt IS NULL ";

		AbrechnungsvorschlagDto avDtoVorher = null;
		if (abrechnungsvorschlagIIdVorhanden != null) {
			avDtoVorher = abrechnungsvorschlagFindByPrimaryKey(abrechnungsvorschlagIIdVorhanden, theClientDto);
			sQueryAuftragzeiten += " AND z.i_id=" + avDtoVorher.getZeitdatenIId();

			Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, avDtoVorher.getIId());
			em.remove(abrechnungsvorschlag);
		} else {

			if (dStichtag != null) {
				sQueryAuftragzeiten += " AND z.t_zeit < '" + Helper.formatDateWithSlashes(dStichtag) + "'";
			}
		}
		sQueryAuftragzeiten += " ORDER BY z.t_zeit ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query zeiten = session.createQuery(sQueryAuftragzeiten);

		List<?> resultList = zeiten.list();

		Iterator<?> resultListAuftraege = resultList.iterator();

		while (resultListAuftraege.hasNext()) {
			FLRZeitdaten flrzeitdaten = (FLRZeitdaten) resultListAuftraege.next();

			AuftragzeitenDto[] zeitenDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
					flrzeitdaten.getC_belegartnr(), flrzeitdaten.getI_belegartid(), null, null, null, null,
					ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ZEITPUNKT_PERSONAL, false, flrzeitdaten.getI_id(), null,
					ZeiterfassungFac.BELEGZEITEN_NUR_PERSONALZEITEN, theClientDto);

			if (zeitenDtos.length > 0) {
				BigDecimal bdStunden = BigDecimal.ZERO;

				AbrechnungsvorschlagDto dto = new AbrechnungsvorschlagDto();
				if (avDtoVorher != null) {
					dto.setTAnlegen(avDtoVorher.getTAnlegen());
				}

				dto.setMandantCNr(theClientDto.getMandant());
				dto.setZeitdatenIId(flrzeitdaten.getI_id());
				dto.setPersonalIId(flrzeitdaten.getPersonal_i_id());
				dto.setTVon(zeitenDtos[zeitenDtos.length - 1].getTsBeginn());
				dto.setTBis(zeitenDtos[0].getTsEnde());
				dto.setFVerrechenbar(flrzeitdaten.getF_verrechenbar());

				for (int i = 0; i < zeitenDtos.length; i++) {
					bdStunden = bdStunden.add(new BigDecimal(zeitenDtos[i].getDdDauer()));

				}

				// PJ21749
				if (flrzeitdaten.getF_dauer_uebersteuert() != null) {
					bdStunden = new BigDecimal(flrzeitdaten.getF_dauer_uebersteuert());
				}

				dto.setNStundenGesamt(Helper.rundeKaufmaennisch(bdStunden, 3));

				dto = berechneVerrechnenbar(dto);

				if (flrzeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByPrimaryKeyOhneExc(flrzeitdaten.getI_belegartid());

					if (auftragDto != null) {

						if (flrzeitdaten.getI_belegartpositionid() != null) {
							AuftragpositionDto apDto = getAuftragpositionFac()
									.auftragpositionFindByPrimaryKeyOhneExc(flrzeitdaten.getI_belegartpositionid());
							if (apDto != null && Helper.short2boolean(apDto.getBPauschal())) {
								continue;
							}

						}

						dto.setAuftragIId(flrzeitdaten.getI_belegartid());
						dto.setKundeIId(auftragDto.getKundeIIdRechnungsadresse());
						dto.setWaehrungCNrRechnung(auftragDto.getCAuftragswaehrung());

						dto.setProjektIId(auftragDto.getProjektIId());

						if (dto.getProjektIId() == null && auftragDto.getAngebotIId() != null) {
							try {
								AngebotDto angebotDto = getAngebotFac()
										.angebotFindByPrimaryKey(auftragDto.getAngebotIId(), theClientDto);
								dto.setProjektIId(angebotDto.getProjektIId());
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}
						}

					}
				} else if (flrzeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
					LosDto losDto = getFertigungFac().losFindByPrimaryKeyOhneExc(flrzeitdaten.getI_belegartid());
					if (losDto != null) {
						dto.setLosIId(flrzeitdaten.getI_belegartid());

						if (losDto.getKundeIId() != null) {
							dto.setKundeIId(losDto.getKundeIId());
							dto.setProjektIId(losDto.getProjektIId());

							KundeDto kdDto = getKundeFac().kundeFindByPrimaryKeySmall(losDto.getKundeIId());
							dto.setWaehrungCNrRechnung(kdDto.getCWaehrung());

						} else if (losDto.getAuftragIId() != null) {
							AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());

							if (losDto.getAuftragpositionIId() != null) {
								AuftragpositionDto apDto = getAuftragpositionFac()
										.auftragpositionFindByPrimaryKeyOhneExc(losDto.getAuftragpositionIId());
								if (apDto != null && Helper.short2boolean(apDto.getBPauschal())) {
									continue;
								}
							}

							dto.setAuftragIId(auftragDto.getIId());
							dto.setWaehrungCNrRechnung(auftragDto.getCAuftragswaehrung());

							dto.setProjektIId(auftragDto.getProjektIId());
							dto.setKundeIId(auftragDto.getKundeIIdRechnungsadresse());

							if (dto.getProjektIId() == null && auftragDto.getAngebotIId() != null) {
								try {
									AngebotDto angebotDto = getAngebotFac()
											.angebotFindByPrimaryKey(auftragDto.getAngebotIId(), theClientDto);
									dto.setProjektIId(angebotDto.getProjektIId());
								} catch (RemoteException e) {
									throwEJBExceptionLPRespectOld(e);
								}
							}
						}

					}
				} else {
					ProjektDto pjDto = getProjektFac().projektFindByPrimaryKeyOhneExc(flrzeitdaten.getI_belegartid());
					if (pjDto != null) {
						dto.setProjektIId(flrzeitdaten.getI_belegartid());

						try {
							KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(pjDto.getPartnerIId(),
									theClientDto.getMandant(), theClientDto);
							if (kdDto != null) {
								dto.setKundeIId(kdDto.getIId());
								dto.setWaehrungCNrRechnung(kdDto.getCWaehrung());
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

					}
				}

				// bereits abgerechnete Stunden beruecksichtigen

				BigDecimal bdStundenOffen = dto.getNStundenVerrechenbar();

				Query query = em.createNamedQuery("ZeitdatenverrechnetFindByZeitdatenIId");
				query.setParameter(1, flrzeitdaten.getI_id());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Zeitdatenverrechnet zv = (Zeitdatenverrechnet) it.next();
					bdStundenOffen = bdStundenOffen.subtract(zv.getNStunden());
				}

				if (bdStundenOffen.doubleValue() < 0) {
					bdStundenOffen = BigDecimal.ZERO;
				}
				dto.setNStundenOffen(bdStundenOffen);

				dto.setAuftragpositionIId(getAuftragsposition(dto.getAuftragIId(), flrzeitdaten.getArtikel_i_id(),
						flrzeitdaten.getI_belegartpositionid(), theClientDto));

				getAbrechnungsvorschlagFac().createAbrechnungsvorschlag(dto, theClientDto);
			}

		}
	}

	public void erstelleAbrechnungsvorschlagER(java.sql.Date dStichtag, Integer abrechnungsvorschlagIIdVorhanden,
			TheClientDto theClientDto) {
		// ER-Auftragszuordnung
		String sQueryAuftragzeiten = "SELECT z FROM FLREingangsrechnungAuftragszuordnung z WHERE z.flreingangsrechnung.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND z.t_erledigt IS NULL";

		AbrechnungsvorschlagDto avDtoVorher = null;
		if (abrechnungsvorschlagIIdVorhanden != null) {
			avDtoVorher = abrechnungsvorschlagFindByPrimaryKey(abrechnungsvorschlagIIdVorhanden, theClientDto);
			sQueryAuftragzeiten += " AND z.i_id=" + avDtoVorher.getAuftragszuordnungIId();

			Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, avDtoVorher.getIId());
			em.remove(abrechnungsvorschlag);
		} else {

			if (dStichtag != null) {
				sQueryAuftragzeiten += " AND z.flreingangsrechnung.t_belegdatum < '"
						+ Helper.formatDateWithSlashes(dStichtag) + "'";
			}
		}

		sQueryAuftragzeiten += " ORDER BY z.flrauftrag.c_nr ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query zeiten = session.createQuery(sQueryAuftragzeiten);

		List<?> resultList = zeiten.list();

		Iterator<?> resultListAuftraege = resultList.iterator();

		while (resultListAuftraege.hasNext()) {
			FLREingangsrechnungAuftragszuordnung eraz = (FLREingangsrechnungAuftragszuordnung) resultListAuftraege
					.next();

			AbrechnungsvorschlagDto dto = null;
			try {
				EingangsrechnungAuftragszuordnungDto erazDto = getEingangsrechnungFac()
						.eingangsrechnungAuftragszuordnungFindByPrimaryKey(eraz.getI_id());

				dto = new AbrechnungsvorschlagDto();
				if (avDtoVorher != null) {
					dto.setTAnlegen(avDtoVorher.getTAnlegen());
				}
				dto.setMandantCNr(theClientDto.getMandant());
				dto.setAuftragszuordnungIId(eraz.getI_id());
				dto.setAuftragIId(eraz.getFlrauftrag().getI_id());
				dto.setKundeIId(eraz.getFlrauftrag().getKunde_i_id_rechnungsadresse());
				dto.setWaehrungCNrRechnung(eraz.getFlrauftrag().getWaehrung_c_nr_auftragswaehrung());

				// Waehrung umrechnen
				BigDecimal bdBetragInRechungsWaehrung = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
						eraz.getN_betrag(), eraz.getFlreingangsrechnung().getWaehrung_c_nr(),
						eraz.getFlrauftrag().getWaehrung_c_nr_auftragswaehrung(),
						new java.sql.Date(eraz.getFlreingangsrechnung().getT_belegdatum().getTime()), theClientDto);

				dto.setNBetragGesamt(bdBetragInRechungsWaehrung);
				dto.setPersonalIId(erazDto.getPersonalIIdAendern());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			dto.setTVon(new Timestamp(eraz.getFlreingangsrechnung().getT_belegdatum().getTime()));
			dto.setFVerrechenbar(eraz.getF_verrechenbar());

			// bereits abgerechneten Betrag beruecksichtigen

			dto = berechneVerrechnenbar(dto);

			BigDecimal bdBetragOffen = dto.getNBetragVerrechenbar();

			Query query = em.createNamedQuery("AuftragszuordnungverrechnetFindByAuftragszuordnungIId");
			query.setParameter(1, eraz.getI_id());
			Collection<?> cl = query.getResultList();
			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Auftragszuordnungverrechnet zv = (Auftragszuordnungverrechnet) it.next();

				try {

					BigDecimal bdAbgerechnetInREWaehrung = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							zv.getNBetrag(), eraz.getFlreingangsrechnung().getWaehrung_c_nr(),
							eraz.getFlrauftrag().getWaehrung_c_nr_auftragswaehrung(),
							new java.sql.Date(eraz.getFlreingangsrechnung().getT_belegdatum().getTime()), theClientDto);

					bdBetragOffen = bdBetragOffen.subtract(bdAbgerechnetInREWaehrung);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			if (bdBetragOffen.doubleValue() < 0) {
				bdBetragOffen = BigDecimal.ZERO;
			}
			dto.setNBetragOffen(bdBetragOffen);

			getAbrechnungsvorschlagFac().createAbrechnungsvorschlag(dto, theClientDto);

		}
	}

	public void erstelleAbrechnungsvorschlagTelefonzeiten(java.sql.Date dStichtag,
			Integer abrechnungsvorschlagIIdVorhanden, TheClientDto theClientDto) {
		String sQueryAuftragzeiten = "SELECT t FROM FLRTelefonzeiten t WHERE t.flrpersonal.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND t.t_erledigt IS NULL";

		AbrechnungsvorschlagDto avDtoVorher = null;
		if (abrechnungsvorschlagIIdVorhanden != null) {
			avDtoVorher = abrechnungsvorschlagFindByPrimaryKey(abrechnungsvorschlagIIdVorhanden, theClientDto);
			sQueryAuftragzeiten += " AND t.i_id=" + avDtoVorher.getTelefonzeitenIId();

			Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, avDtoVorher.getIId());
			em.remove(abrechnungsvorschlag);
		} else {
			if (dStichtag != null) {
				sQueryAuftragzeiten += " AND t.t_bis < '" + Helper.formatDateWithSlashes(dStichtag) + "'";
			}
		}

		sQueryAuftragzeiten += " ORDER BY t.t_von ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query zeiten = session.createQuery(sQueryAuftragzeiten);

		List<?> resultList = zeiten.list();

		Iterator<?> resultListAuftraege = resultList.iterator();

		while (resultListAuftraege.hasNext()) {
			FLRTelefonzeiten flrTelefonzeiten = (FLRTelefonzeiten) resultListAuftraege.next();

			AbrechnungsvorschlagDto dto = new AbrechnungsvorschlagDto();
			if (avDtoVorher != null) {
				dto.setTAnlegen(avDtoVorher.getTAnlegen());
			}
			dto.setMandantCNr(theClientDto.getMandant());
			dto.setTelefonzeitenIId(flrTelefonzeiten.getI_id());
			dto.setPersonalIId(flrTelefonzeiten.getPersonal_i_id());

			dto.setFVerrechenbar(flrTelefonzeiten.getF_verrechenbar());

			dto.setTVon(new Timestamp(flrTelefonzeiten.getT_von().getTime()));
			dto.setTBis(new Timestamp(flrTelefonzeiten.getT_bis().getTime()));

			long lDauer = flrTelefonzeiten.getT_bis().getTime() - flrTelefonzeiten.getT_von().getTime();

			dto.setNStundenGesamt(new BigDecimal(Helper.timeInMillis2Double(lDauer)));

			dto = berechneVerrechnenbar(dto);

			if (flrTelefonzeiten.getProjekt_i_id() != null) {
				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKeyOhneExc(flrTelefonzeiten.getProjekt_i_id());
				dto.setProjektIId(flrTelefonzeiten.getProjekt_i_id());

				try {
					KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(pjDto.getPartnerIId(),
							theClientDto.getMandant(), theClientDto);
					if (kdDto != null) {
						dto.setKundeIId(kdDto.getIId());
						dto.setWaehrungCNrRechnung(kdDto.getCWaehrung());
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			} else if (flrTelefonzeiten.getAuftrag_i_id() != null) {
				dto.setAuftragIId(flrTelefonzeiten.getAuftrag_i_id());
				dto.setKundeIId(flrTelefonzeiten.getFlrauftrag().getFlrkunde().getI_id());
				dto.setProjektIId(flrTelefonzeiten.getFlrauftrag().getProjekt_i_id());
				dto.setWaehrungCNrRechnung(flrTelefonzeiten.getFlrauftrag().getWaehrung_c_nr_auftragswaehrung());
			} else if (flrTelefonzeiten.getAngebot_i_id() != null) {

				dto.setKundeIId(flrTelefonzeiten.getFlrangebot().getFlrkunde().getI_id());
				dto.setWaehrungCNrRechnung(flrTelefonzeiten.getFlrangebot().getFlrkunde().getWaehrung_c_nr());

				dto.setProjektIId(flrTelefonzeiten.getFlrangebot().getProjekt_i_id());

			} else if (flrTelefonzeiten.getFlrpartner() != null) {
				try {
					KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
							flrTelefonzeiten.getFlrpartner().getI_id(), theClientDto.getMandant(), theClientDto);
					if (kdDto != null) {
						dto.setKundeIId(kdDto.getIId());
						dto.setWaehrungCNrRechnung(kdDto.getCWaehrung());
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			// bereits abgerechnete Stunden beruecksichtigen

			BigDecimal bdStundenOffen = dto.getNStundenVerrechenbar();

			Query query = em.createNamedQuery("TelefonzeitenverrechnetFindByTelefonzeitenIId");
			query.setParameter(1, flrTelefonzeiten.getI_id());
			Collection<?> cl = query.getResultList();
			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Telefonzeitenverrechnet zv = (Telefonzeitenverrechnet) it.next();
				bdStundenOffen = bdStundenOffen.subtract(zv.getNStunden());
			}

			if (bdStundenOffen.doubleValue() < 0) {
				bdStundenOffen = BigDecimal.ZERO;
			}
			dto.setNStundenOffen(bdStundenOffen);

			getAbrechnungsvorschlagFac().createAbrechnungsvorschlag(dto, theClientDto);

		}
	}

	public AbrechnungsvorschlagDto berechneVerrechnenbar(AbrechnungsvorschlagDto dto) {

		if (dto.getFVerrechenbar() == null) {
			dto.setFVerrechenbar(100D);
		}

		if (dto.getNBetragGesamt() != null) {
			dto.setNBetragVerrechenbar(Helper.rundeKaufmaennisch(dto.getNBetragGesamt().movePointLeft(2)
					.multiply(new BigDecimal(dto.getFVerrechenbar().doubleValue())), 2));
		}

		if (dto.getNStundenGesamt() != null) {
			dto.setNStundenVerrechenbar(Helper.rundeKaufmaennisch(dto.getNStundenGesamt().movePointLeft(2)
					.multiply(new BigDecimal(dto.getFVerrechenbar().doubleValue())), 3));
		}

		if (dto.getNKilometerGesamt() != null) {
			dto.setNKilometerVerrechenbar(Helper.rundeKaufmaennisch(dto.getNKilometerGesamt().movePointLeft(2)
					.multiply(new BigDecimal(dto.getFVerrechenbar().doubleValue())), 2));
		}
		if (dto.getNSpesenGesamt() != null) {
			dto.setNSpesenVerrechenbar(Helper.rundeKaufmaennisch(dto.getNSpesenGesamt().movePointLeft(2)
					.multiply(new BigDecimal(dto.getFVerrechenbar().doubleValue())), 2));
		}

		return dto;

	}

	public Integer createAbrechnungsvorschlag(AbrechnungsvorschlagDto dto, TheClientDto theClientDto) {

		// generieren von primary key

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ABRECHNUNGSVORSCHLAG);
		dto.setIId(pk);

		dto.setMandantCNr(theClientDto.getMandant());
		if (dto.getTAnlegen() == null) {
			dto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
		}

		if (dto.getFVerrechenbar() == null) {
			dto.setFVerrechenbar(100D);
		}

		if (dto.getBVerrechnet() == null) {
			dto.setBVerrechnet(Helper.boolean2Short(false));
		}

		if (dto.getWaehrungCNrRechnung() == null) {
			dto.setWaehrungCNrRechnung(theClientDto.getSMandantenwaehrung());
		}

		try {
			Abrechnungsvorschlag abrechnungsvorschlag = new Abrechnungsvorschlag(dto.getIId(), dto.getMandantCNr(),
					dto.getTVon(), dto.getTAnlegen(), dto.getBVerrechnet(), dto.getFVerrechenbar(),
					dto.getWaehrungCNrRechnung());
			em.persist(abrechnungsvorschlag);
			em.flush();

			setAbrechnungsvorschlagFromAbrechnungsvorschlagDto(abrechnungsvorschlag, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public BigDecimal getSelektierteStunden(ArrayList<Integer> iids) {

		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria crit = session.createCriteria(FLRAbrechnungsvorschlag.class);
		crit.add(Restrictions.in("i_id", iids.toArray()));
		crit.add(Restrictions.eq("b_verrechnet", (short) 0));

		@SuppressWarnings("unchecked")
		List<FLRAbrechnungsvorschlag> list = new ArrayList<FLRAbrechnungsvorschlag>(crit.list());

		HashSet hsAnzahlKunden = new HashSet();

		BigDecimal stundenOffen = BigDecimal.ZERO;
		for (FLRAbrechnungsvorschlag bd : list) {

			if (bd.getFlrkunde() == null) {
				// Fehler -> Kunde wird benoetigt
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE"));
			} else {
				hsAnzahlKunden.add(bd.getFlrkunde().getI_id());
			}

			if (hsAnzahlKunden.size() > 1) {
				// Fehler -> Kann nur einen Kunden abrechnen
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN"));
			}

			if (bd.getN_stunden_offen() != null) {
				stundenOffen = stundenOffen.add(bd.getN_stunden_offen());
			}

		}

		return stundenOffen;
	}

	private Integer getAuftragsposition(Integer auftragIId, Integer artikelIId, Integer auftragpositionIIdAusZeitdaten,
			TheClientDto theClientDto) {

		if (auftragIId != null && artikelIId != null) {

			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);
			ArtikelDto aDtoKalk = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
			if (auftragDto.getVerrechnungsmodellIId() != null) {
				VerrechnungsmodellDto vmDto = verrechnungsmodellFindByPrimaryKey(auftragDto.getVerrechnungsmodellIId(),
						theClientDto);

				if (Helper.short2boolean(vmDto.getBPreiseAusAuftrag())) {

					Query queryPOS = em.createNamedQuery("AuftragpositionfindByAuftragIIdArtikelIId");
					queryPOS.setParameter(1, auftragIId);
					queryPOS.setParameter(2, artikelIId);
					Collection c = queryPOS.getResultList();

					Iterator<Auftragposition> it = c.iterator();
					while (it.hasNext()) {
						Auftragposition ap = it.next();

						if (Helper.short2boolean(aDtoKalk.getBKalkulatorisch()) && ap.getPositionIIdZugehoerig() != null
								&& ap.getNMenge() != null) {
							AuftragpositionDto apDtoZugehoerig = getAuftragpositionFac()
									.auftragpositionFindByPrimaryKeyOhneExc(ap.getPositionIIdZugehoerig());
							if (apDtoZugehoerig.getIId().equals(auftragpositionIIdAusZeitdaten)) {
								return ap.getIId();
							}
						} else {
							if (ap.getIId().equals(auftragpositionIIdAusZeitdaten)) {
								return ap.getIId();
							}
						}
					}

					if (c.size() > 0) {
						Auftragposition p = (Auftragposition) c.iterator().next();
						return p.getIId();
					}
				}
			}

		}
		return null;
	}

	public BigDecimal getSelektierteKilometer(ArrayList<Integer> iids) {

		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria crit = session.createCriteria(FLRAbrechnungsvorschlag.class);
		crit.add(Restrictions.in("i_id", iids.toArray()));
		crit.add(Restrictions.eq("b_verrechnet", (short) 0));

		@SuppressWarnings("unchecked")
		List<FLRAbrechnungsvorschlag> list = new ArrayList<FLRAbrechnungsvorschlag>(crit.list());

		HashSet hsAnzahlKunden = new HashSet();

		BigDecimal kilometerOffen = BigDecimal.ZERO;
		for (FLRAbrechnungsvorschlag bd : list) {

			if (bd.getFlrkunde() == null) {
				// Fehler -> Kunde wird benoetigt
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE"));
			} else {
				hsAnzahlKunden.add(bd.getFlrkunde().getI_id());
			}

			if (hsAnzahlKunden.size() > 1) {
				// Fehler -> Kann nur einen Kunden abrechnen
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN"));
			}

			if (bd.getN_kilometer_offen() != null) {
				kilometerOffen = kilometerOffen.add(bd.getN_kilometer_offen());
			}

		}

		return kilometerOffen;
	}

	public BigDecimal getSummeZeitdatenVerrechnet(Integer zeitdatenIId) {
		Query querynext = em.createNamedQuery("ZeitdatenverrechnetSumZeitdatenIId");
		querynext.setParameter(1, zeitdatenIId);
		BigDecimal bdSum = (BigDecimal) querynext.getSingleResult();
		if (bdSum != null) {
			return bdSum;
		} else {
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal getSummeTelefonzeitenVerrechnet(Integer telefonzeitenIId) {
		Query querynext = em.createNamedQuery("TelefonzeitenverrechnetSumTelefonzeitenIId");
		querynext.setParameter(1, telefonzeitenIId);
		BigDecimal bdSum = (BigDecimal) querynext.getSingleResult();
		if (bdSum != null) {
			return bdSum;
		} else {
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal getSelektiertenBetrag(ArrayList<Integer> iids) {

		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria crit = session.createCriteria(FLRAbrechnungsvorschlag.class);
		crit.add(Restrictions.in("i_id", iids.toArray()));
		crit.add(Restrictions.eq("b_verrechnet", (short) 0));

		@SuppressWarnings("unchecked")
		List<FLRAbrechnungsvorschlag> list = new ArrayList<FLRAbrechnungsvorschlag>(crit.list());

		HashSet hsAnzahlKunden = new HashSet();

		BigDecimal betragOffen = BigDecimal.ZERO;

		for (FLRAbrechnungsvorschlag bd : list) {

			if (bd.getFlrkunde() == null) {
				// Fehler -> Kunde wird benoetigt
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE"));
			} else {
				hsAnzahlKunden.add(bd.getFlrkunde().getI_id());
			}

			if (hsAnzahlKunden.size() > 1) {
				// Fehler -> Kann nur einen Kunden abrechnen
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN"));
			}

			if (bd.getN_betrag_offen() != null) {
				betragOffen = betragOffen.add(bd.getN_betrag_offen());
			}

		}

		return betragOffen;
	}

	public BigDecimal getSelektierteSpesen(ArrayList<Integer> iids) {

		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria crit = session.createCriteria(FLRAbrechnungsvorschlag.class);
		crit.add(Restrictions.in("i_id", iids.toArray()));
		crit.add(Restrictions.eq("b_verrechnet", (short) 0));

		@SuppressWarnings("unchecked")
		List<FLRAbrechnungsvorschlag> list = new ArrayList<FLRAbrechnungsvorschlag>(crit.list());

		HashSet hsAnzahlKunden = new HashSet();

		BigDecimal spesenOffen = BigDecimal.ZERO;

		for (FLRAbrechnungsvorschlag bd : list) {

			if (bd.getFlrkunde() == null) {
				// Fehler -> Kunde wird benoetigt
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE"));
			} else {
				hsAnzahlKunden.add(bd.getFlrkunde().getI_id());
			}

			if (hsAnzahlKunden.size() > 1) {
				// Fehler -> Kann nur einen Kunden abrechnen
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN"));
			}

			if (bd.getN_spesen_offen() != null) {
				spesenOffen = spesenOffen.add(bd.getN_spesen_offen());
			}

		}

		return spesenOffen;
	}

	public Integer kopiereVerrechnungsmodelltag(Integer verrechnungsmodellmodelltagIId, TheClientDto theClientDto) {

		VerrechnungsmodelltagDto zmTag = verrechnungsmodelltagFindByPrimaryKey(verrechnungsmodellmodelltagIId,
				theClientDto);

		Query query = em.createNamedQuery("TagesartfindAll");
		Collection<?> clArten = query.getResultList();
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Tagesart tagesartTemp = (Tagesart) itArten.next();

			if (tagesartTemp.getIId().equals(zmTag.getTagesartIId())) {

				while (itArten.hasNext()) {

					Tagesart tagesartTempNaechster = (Tagesart) itArten.next();

					Query query2 = em.createNamedQuery("VerrechnungsmodelltagfindByVerrechnungsmodellIIdTagesartIId");
					query2.setParameter(1, zmTag.getVerrechnungsmodellIId());
					query2.setParameter(2, tagesartTempNaechster.getIId());
					Collection c = query2.getResultList();

					if (c.size() == 0) {

						Integer zmTagIIdNeu = null;
						Query query3 = em
								.createNamedQuery("VerrechnungsmodelltagfindByVerrechnungsmodellIIdTagesartIId");
						query3.setParameter(1, zmTag.getVerrechnungsmodellIId());
						query3.setParameter(2, zmTag.getTagesartIId());
						Collection c3 = query3.getResultList();
						Iterator<?> it3 = c3.iterator();
						while (it3.hasNext()) {
							Verrechnungsmodelltag vmTag = (Verrechnungsmodelltag) it3.next();

							VerrechnungsmodelltagDto vmTagDtoNeu = VerrechnungsmodelltagDtoAssembler.createDto(vmTag);

							vmTagDtoNeu.setIId(null);
							vmTagDtoNeu.setTagesartIId(tagesartTempNaechster.getIId());
							zmTagIIdNeu = createVerrechnungsmodelltag(vmTagDtoNeu);
						}

						return zmTagIIdNeu;

					}

				}

			}
		}

		return null;

	}

	public Integer createVerrechnungsmodelltag(VerrechnungsmodelltagDto dto) {

		try {

			pruefeObArtikelKalkulatorisch(dto.getArtikelIIdZuverrechnen());

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VERRECHNUNGSMODELLTAG);
			dto.setIId(pk);

			Verrechnungsmodelltag bean = new Verrechnungsmodelltag(dto.getIId(), dto.getTagesartIId(),
					dto.getArtikelIIdGebucht(), dto.getArtikelIIdZuverrechnen(), dto.getVerrechnungsmodellIId(),
					dto.getBEndedestages());
			em.persist(bean);
			em.flush();
			setVerrechnungsmodelltagFromVerrechnungsmodelltagDto(bean, dto);

			pruefeObVerrechnungsmodelltagSichUeberschneidet(dto.getVerrechnungsmodellIId(), dto.getIId());

			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createVerrechnungsmodell(VerrechnungsmodellDto dto) {

		try {
			Query query = em.createNamedQuery("VerrechnungsmodellFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Verrechnungsmodell doppelt = (Verrechnungsmodell) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("RECH_VERRECHNUNGSMODELL.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		pruefeObArtikelKalkulatorisch(dto.getArtikelIIdEr());
		pruefeObArtikelKalkulatorisch(dto.getArtikelIIdReisekilometer());
		pruefeObArtikelKalkulatorisch(dto.getArtikelIIdReisespesen());
		pruefeObArtikelKalkulatorisch(dto.getArtikelIIdTelefon());

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VERRECHNUNGSMODELL);
			dto.setIId(pk);

			if (dto.getBPreiseAusAuftrag() == null) {
				dto.setBPreiseAusAuftrag(Helper.boolean2Short(false));
			}

			if (dto.getBNachArtikelVerdichten() == null) {
				dto.setBNachArtikelVerdichten(Helper.boolean2Short(true));
			}

			Verrechnungsmodell bean = new Verrechnungsmodell(dto.getIId(), dto.getMandantCNr(), dto.getCBez(),
					dto.getArtikelIIdReisespesen(), dto.getArtikelIIdReisekilometer(), dto.getArtikelIIdTelefon(),
					dto.getArtikelIIdEr(), dto.getFAufschlagEr(), dto.getBVersteckt(), dto.getBPreiseAusAuftrag(),
					dto.getBNachArtikelVerdichten());
			em.persist(bean);
			em.flush();
			setVerrechnungsmodellFromVerrechnungsmodellDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void aendereVerrechenbarkeit(ArrayList<Integer> abrechnugnsvorschlagIIds, Double fProzent,
			TheClientDto theClientDto) {
		for (int i = 0; i < abrechnugnsvorschlagIIds.size(); i++) {

			Integer abrechnugnsvorschlagIId = abrechnugnsvorschlagIIds.get(i);
			AbrechnungsvorschlagDto avDto = abrechnungsvorschlagFindByPrimaryKey(abrechnugnsvorschlagIId, theClientDto);
			avDto.setFVerrechenbar(fProzent);

			BigDecimal bdStundenGesamt = avDto.getNStundenVerrechenbar();

			BigDecimal bdStundenVerrechenbarNeu = avDto.getNStundenGesamt().movePointLeft(2)
					.multiply(new BigDecimal(avDto.getFVerrechenbar().doubleValue()));

			BigDecimal diff = bdStundenGesamt.subtract(bdStundenVerrechenbarNeu);

			Abrechnungsvorschlag av = em.find(Abrechnungsvorschlag.class, abrechnugnsvorschlagIId);

			av.setNStundenVerrechenbar(bdStundenVerrechenbarNeu);

			av.setFVerrechenbar(fProzent);

			// Offen
			if (av.getNStundenOffen().subtract(diff).doubleValue() < 0) {
				av.setNStundenOffen(BigDecimal.ZERO);
			} else {
				av.setNStundenOffen(av.getNStundenOffen().subtract(diff));
			}

			em.merge(av);
			em.flush();
		}

	}

	private void pruefeObArtikelKalkulatorisch(Integer artikelIId) {

		if (artikelIId != null) {
			Artikel artikel = em.find(Artikel.class, artikelIId);
			if (Helper.short2boolean(artikel.getBKalkulatorisch())) {

				ArrayList al = new ArrayList();
				al.add(artikel.getCNr());

				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_ARTIKEL_IN_VERRECHNUNGSMODELL_KALKULATORISCH, al,
						new Exception("FEHLER_ABRECHUNGSVORSCHLAG_ARTIKEL_IN_VERRECHNUNGSMODELL_KALKULATORISCH "
								+ artikel.getCNr()));
			}
		}
	}

	public void updateVerrechnungsmodell(VerrechnungsmodellDto dto) {
		Verrechnungsmodell ialle = em.find(Verrechnungsmodell.class, dto.getIId());

		pruefeObArtikelKalkulatorisch(dto.getArtikelIIdEr());
		pruefeObArtikelKalkulatorisch(dto.getArtikelIIdReisekilometer());
		pruefeObArtikelKalkulatorisch(dto.getArtikelIIdReisespesen());
		pruefeObArtikelKalkulatorisch(dto.getArtikelIIdTelefon());

		try {
			Query query = em.createNamedQuery("VerrechnungsmodellFindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Verrechnungsmodell) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("RECH_VERRECHNUNGSMODELL.UK"));
			}
		} catch (NoResultException ex) {

		}

		setVerrechnungsmodellFromVerrechnungsmodellDto(ialle, dto);
	}

	public void updateVerrechnungsmodelltag(VerrechnungsmodelltagDto dto) {

		pruefeObArtikelKalkulatorisch(dto.getArtikelIIdZuverrechnen());

		Verrechnungsmodelltag ialle = em.find(Verrechnungsmodelltag.class, dto.getIId());
		setVerrechnungsmodelltagFromVerrechnungsmodelltagDto(ialle, dto);

		pruefeObVerrechnungsmodelltagSichUeberschneidet(dto.getVerrechnungsmodellIId(), dto.getIId());

	}

	private void pruefeObVerrechnungsmodelltagSichUeberschneidet(Integer verrechnungsmodellIId,
			Integer verrechnungsmodelltagIId) {

		Verrechnungsmodelltag verrechnungsmodelltagAktuell = em.find(Verrechnungsmodelltag.class,
				verrechnungsmodelltagIId);

		Query query = em.createNamedQuery("VerrechnungsmodelltagfindByVerrechnungsmodellIIdTagesartIId");
		query.setParameter(1, verrechnungsmodellIId);
		query.setParameter(2, verrechnungsmodelltagAktuell.getTagesartIId());
		Collection<?> cl = query.getResultList();
		Iterator it = cl.iterator();

		while (it.hasNext()) {
			Verrechnungsmodelltag zmtVorhanden = (Verrechnungsmodelltag) it.next();

			if (verrechnungsmodelltagAktuell.getUDauerAb() == null && zmtVorhanden.getUDauerAb() == null) {

			}

			if (!zmtVorhanden.getIId().equals(verrechnungsmodelltagIId)) {

				if (zmtVorhanden.getArtikelIIdGebucht().equals(verrechnungsmodelltagAktuell.getArtikelIIdGebucht())) {
					boolean bFehler = false;

					if (zmtVorhanden.getUDauerAb() != null && verrechnungsmodelltagAktuell.getUDauerAb() != null) {
						if (zmtVorhanden.getUDauerAb().equals(verrechnungsmodelltagAktuell.getUDauerAb())) {
							bFehler = true;
						}

					} else if (zmtVorhanden.getUZeitraumVon() != null
							&& verrechnungsmodelltagAktuell.getUZeitraumVon() != null) {
						java.sql.Time uVonVorhanden = zmtVorhanden.getUZeitraumVon();
						java.sql.Time uVonAktuell = verrechnungsmodelltagAktuell.getUZeitraumVon();

						Calendar cal24Uhr = Calendar.getInstance();
						cal24Uhr.setTimeInMillis(0);
						cal24Uhr.set(Calendar.MILLISECOND, 0);
						cal24Uhr.set(Calendar.MINUTE, 0);
						cal24Uhr.set(Calendar.SECOND, 0);
						cal24Uhr.set(Calendar.HOUR_OF_DAY, 24);

						java.sql.Time uBisVorhanden = null;
						if (zmtVorhanden.getUZeitraumBis() != null
								&& !Helper.short2boolean(zmtVorhanden.getBEndedestages())) {
							uBisVorhanden = zmtVorhanden.getUZeitraumBis();
						} else {

							uBisVorhanden = new java.sql.Time(cal24Uhr.getTimeInMillis());

						}

						java.sql.Time uBisAktuell = null;
						if (verrechnungsmodelltagAktuell.getUZeitraumBis() != null
								&& !Helper.short2boolean(verrechnungsmodelltagAktuell.getBEndedestages())) {
							uBisAktuell = verrechnungsmodelltagAktuell.getUZeitraumBis();
						} else {
							uBisAktuell = new java.sql.Time(cal24Uhr.getTimeInMillis());
						}

						if (uVonVorhanden.getTime() >= uVonAktuell.getTime()
								&& uBisVorhanden.getTime() <= uBisAktuell.getTime()) {
							bFehler = true;
						}

						if (uVonVorhanden.getTime() >= uVonAktuell.getTime()
								&& uVonVorhanden.getTime() < uBisAktuell.getTime()) {
							bFehler = true;
						}
						if (uVonVorhanden.getTime() <= uVonAktuell.getTime()
								&& uBisVorhanden.getTime() >= uBisAktuell.getTime()) {
							bFehler = true;
						}
						if (uVonVorhanden.getTime() <= uVonAktuell.getTime()
								&& uBisVorhanden.getTime() >= uBisAktuell.getTime()) {
							bFehler = true;
						}
						if (uVonAktuell.getTime() < uBisVorhanden.getTime()
								&& uBisAktuell.getTime() >= uBisVorhanden.getTime()) {
							bFehler = true;
						}

					}

					if (bFehler == true) {

						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VERRECHNUNGSMODELLTAG_UEBERSCHNEIDEN_SICH,
								new Exception("FEHLER_VERRECHNUNGSMODELLTAG_UEBERSCHNEIDEN_SICH"));

					}
				}

			}

		}

	}

	private void setVerrechnungsmodellFromVerrechnungsmodellDto(Verrechnungsmodell bean, VerrechnungsmodellDto dto) {
		bean.setCBez(dto.getCBez());
		bean.setArtikelIIdEr(dto.getArtikelIIdEr());
		bean.setArtikelIIdReisekilometer(dto.getArtikelIIdReisekilometer());
		bean.setArtikelIIdReisespesen(dto.getArtikelIIdReisespesen());
		bean.setArtikelIIdTelefon(dto.getArtikelIIdTelefon());
		bean.setFAufschlagEr(dto.getFAufschlagEr());
		bean.setBVersteckt(dto.getBVersteckt());
		bean.setBNachArtikelVerdichten(dto.getBNachArtikelVerdichten());
		bean.setBPreiseAusAuftrag(dto.getBPreiseAusAuftrag());
		em.merge(bean);
		em.flush();
	}

	private void setVerrechnungsmodelltagFromVerrechnungsmodelltagDto(Verrechnungsmodelltag bean,
			VerrechnungsmodelltagDto dto) {
		bean.setArtikelIIdGebucht(dto.getArtikelIIdGebucht());
		bean.setArtikelIIdZuverrechnen(dto.getArtikelIIdZuverrechnen());
		bean.setTagesartIId(dto.getTagesartIId());
		bean.setUZeitraumVon(dto.getUZeitraumVon());
		bean.setUZeitraumBis(dto.getUZeitraumBis());
		bean.setUDauerAb(dto.getUDauerAb());
		bean.setVerrechnungsmodellIId(dto.getVerrechnungsmodellIId());
		bean.setBEndedestages(dto.getBEndedestages());
		em.merge(bean);
		em.flush();
	}

	public AbrechnungsvorschlagDto abrechnungsvorschlagFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		Abrechnungsvorschlag abrechnungsvorschlag = em.find(Abrechnungsvorschlag.class, iId);
		return AbrechnungsvorschlagDtoAssembler.createDto(abrechnungsvorschlag);
	}

	public VerrechnungsmodellDto verrechnungsmodellFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		Verrechnungsmodell bean = em.find(Verrechnungsmodell.class, iId);
		return VerrechnungsmodellDtoAssembler.createDto(bean);
	}

	public VerrechnungsmodelltagDto verrechnungsmodelltagFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		Verrechnungsmodelltag abrechnungsvorschlag = em.find(Verrechnungsmodelltag.class, iId);
		return VerrechnungsmodelltagDtoAssembler.createDto(abrechnungsvorschlag);
	}

	public void removeVerrechnungsmodell(VerrechnungsmodellDto dto) {

		Verrechnungsmodell toRemove = em.find(Verrechnungsmodell.class, dto.getIId());

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeVerrechnungsmodelltag(VerrechnungsmodelltagDto dto) {

		Verrechnungsmodelltag toRemove = em.find(Verrechnungsmodelltag.class, dto.getIId());

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	private void setAbrechnungsvorschlagFromAbrechnungsvorschlagDto(Abrechnungsvorschlag z,
			AbrechnungsvorschlagDto dto) {
		z.setAuftragIId(dto.getAuftragIId());
		z.setAuftragszuordnungIId(dto.getAuftragszuordnungIId());
		z.setLosIId(dto.getLosIId());
		z.setReiseIId(dto.getReiseIId());
		z.setZeitdatenIId(dto.getZeitdatenIId());
		z.setNBetragOffen(dto.getNBetragOffen());
		z.setNStundenOffen(dto.getNStundenOffen());
		z.setZeitdatenIId(dto.getZeitdatenIId());
		z.setProjektIId(dto.getProjektIId());
		z.setKundeIId(dto.getKundeIId());
		z.setTelefonzeitenIId(dto.getTelefonzeitenIId());
		z.setTVon(dto.getTVon());
		z.setTBis(dto.getTBis());
		z.setPersonalIId(dto.getPersonalIId());
		z.setBVerrechnet(dto.getBVerrechnet());
		z.setMaschinenzeitdatenIId(dto.getMaschinenzeitdatenIId());
		z.setNBetragGesamt(dto.getNBetragGesamt());
		z.setNStundenGesamt(dto.getNStundenGesamt());
		z.setFVerrechenbar(dto.getFVerrechenbar());
		z.setNBetragVerrechenbar(dto.getNBetragVerrechenbar());
		z.setNStundenVerrechenbar(dto.getNStundenVerrechenbar());
		z.setNKilometerGesamt(dto.getNKilometerGesamt());
		z.setNKilometerOffen(dto.getNKilometerOffen());
		z.setNKilometerVerrechenbar(dto.getNKilometerVerrechenbar());
		z.setAuftragpositionIId(dto.getAuftragpositionIId());
		z.setNSpesenGesamt(dto.getNSpesenGesamt());
		z.setNSpesenOffen(dto.getNSpesenOffen());
		z.setNSpesenVerrechenbar(dto.getNSpesenVerrechenbar());
		z.setWaehrungCNrRechnung(dto.getWaehrungCNrRechnung());
		em.merge(z);
		em.flush();
	}

	private void loescheAbrechnungsvorschlagEinesMandaten(TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();
		try {
			String hqlDelete = "delete FLRAbrechnungsvorschlag WHERE mandant_c_nr='" + theClientDto.getMandant() + "'";

			session.createQuery(hqlDelete).executeUpdate();
		} finally {
			closeSession(session);
		}
	}

	public VerrechnungsmodellDto holeVerrechnungsmodellFuerERUeberleitungAnhandSelektierterZeilen(
			ArrayList<Integer> selektierteIIds, TheClientDto theClientDto) {
		VerrechnungsmodellDto vmDto = null;

		HashSet<Integer> hmVerrechnungsartikel = new HashSet();
		HashSet<Double> hmAufschlaege = new HashSet();

		if (selektierteIIds != null && selektierteIIds.size() > 0) {
			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Criteria crit = session.createCriteria(FLRAbrechnungsvorschlag.class);

			crit.add(Restrictions.in("i_id", selektierteIIds));

			crit.add(Restrictions.isNotNull("auftragszuordnung_i_id"));

			crit.addOrder(Order.asc("t_von"));
			List<?> results = crit.list();
			Iterator<?> resultListIterator = results.iterator();

			ArrayList alDaten = new ArrayList();

			while (resultListIterator.hasNext()) {
				FLRAbrechnungsvorschlag av = (FLRAbrechnungsvorschlag) resultListIterator.next();

				Integer verrechnungsmodellIId = null;

				if (av.getFlrauftrag() != null) {

					AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(av.getFlrauftrag().getI_id());
					if (aDto.getVerrechnungsmodellIId() != null) {
						verrechnungsmodellIId = getAuftragFac().auftragFindByPrimaryKey(av.getFlrauftrag().getI_id())
								.getVerrechnungsmodellIId();
					} else {

						ArrayList al = new ArrayList();
						al.add(aDto.getCNr());
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ZEITDATEN_UEBERLEITUNG_AUFTRAG_KEIN_VERRECHNUNGSMODELL, al,
								new Exception("FEHLER_ZEITDATEN_UEBERLEITUNG_AUFTRAG_KEIN_VERRECHNUNGSMODELL"));

					}
				} else {
					KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(av.getFlrkunde().getI_id(), theClientDto);
					if (kdDto.getVerrechnungsmodellIId() != null) {
						verrechnungsmodellIId = kdDto.getVerrechnungsmodellIId();
					} else {
						ArrayList al = new ArrayList();
						al.add(kdDto.getPartnerDto().formatAnrede());

						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ZEITDATEN_UEBERLEITUNG_KUNDE_KEIN_VERRECHNUNGSMODELL, al,
								new Exception("FEHLER_ZEITDATEN_UEBERLEITUNG_KUNDE_KEIN_VERRECHNUNGSMODELL"));
					}
				}

				vmDto = verrechnungsmodellFindByPrimaryKey(verrechnungsmodellIId, theClientDto);

				hmVerrechnungsartikel.add(vmDto.getArtikelIIdEr());
				hmAufschlaege.add(vmDto.getFAufschlagEr());

			}

		}

		if (hmVerrechnungsartikel.size() > 1 || hmAufschlaege.size() > 1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ER_UEBERLEITUNG_MEHRERE_ARTIKEL_ODER_AUFSCHLAEGE,
					new Exception("FEHLER_ER_UEBERLEITUNG_MEHRERE_ARTIKEL_ODER_AUFSCHLAEGE"));
		}

		return vmDto;

	}

	public VerrechnungsmodellDto holeVerrechnungsmodellFuerReiseUeberleitungAnhandSelektierterZeilen(
			ArrayList<Integer> selektierteIIds, TheClientDto theClientDto) {
		VerrechnungsmodellDto vmDto = null;

		HashSet<Integer> hmVerrechnungsartikelSpesen = new HashSet();
		HashSet<Integer> hmVerrechnungsartikelKilometer = new HashSet();

		if (selektierteIIds != null && selektierteIIds.size() > 0) {
			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Criteria crit = session.createCriteria(FLRAbrechnungsvorschlag.class);

			crit.add(Restrictions.in("i_id", selektierteIIds));

			crit.add(Restrictions.isNotNull("reise_i_id"));

			crit.addOrder(Order.asc("t_von"));
			List<?> results = crit.list();
			Iterator<?> resultListIterator = results.iterator();

			ArrayList alDaten = new ArrayList();

			while (resultListIterator.hasNext()) {
				FLRAbrechnungsvorschlag av = (FLRAbrechnungsvorschlag) resultListIterator.next();

				Integer verrechnungsmodellIId = null;

				if (av.getFlrauftrag() != null) {

					AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(av.getFlrauftrag().getI_id());
					if (aDto.getVerrechnungsmodellIId() != null) {
						verrechnungsmodellIId = getAuftragFac().auftragFindByPrimaryKey(av.getFlrauftrag().getI_id())
								.getVerrechnungsmodellIId();
					} else {

						ArrayList al = new ArrayList();
						al.add(aDto.getCNr());
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ZEITDATEN_UEBERLEITUNG_AUFTRAG_KEIN_VERRECHNUNGSMODELL, al,
								new Exception("FEHLER_ZEITDATEN_UEBERLEITUNG_AUFTRAG_KEIN_VERRECHNUNGSMODELL"));

					}
				} else {
					KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(av.getFlrkunde().getI_id(), theClientDto);
					if (kdDto.getVerrechnungsmodellIId() != null) {
						verrechnungsmodellIId = kdDto.getVerrechnungsmodellIId();
					} else {
						ArrayList al = new ArrayList();
						al.add(kdDto.getPartnerDto().formatAnrede());

						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ZEITDATEN_UEBERLEITUNG_KUNDE_KEIN_VERRECHNUNGSMODELL, al,
								new Exception("FEHLER_ZEITDATEN_UEBERLEITUNG_KUNDE_KEIN_VERRECHNUNGSMODELL"));
					}
				}

				vmDto = verrechnungsmodellFindByPrimaryKey(verrechnungsmodellIId, theClientDto);

				hmVerrechnungsartikelSpesen.add(vmDto.getArtikelIIdReisespesen());
				hmVerrechnungsartikelKilometer.add(vmDto.getArtikelIIdReisekilometer());

			}

		}

		if (hmVerrechnungsartikelSpesen.size() > 1 || hmVerrechnungsartikelKilometer.size() > 1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_REISE_UEBERLEITUNG_MEHRERE_ARTIKEL_FUER_KILOMETER_ODER_SPESEN,
					new Exception("FEHLER_REISE_UEBERLEITUNG_MEHRERE_ARTIKEL_FUER_KILOMETER_ODER_SPESEN"));
		}

		return vmDto;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAbrechnungsvorschlag(TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = REPORT_ABRECHNUNGSVORSCHLAG;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_SUBREPORT_LETZTE_EINSTELLUNGEN", getSystemServicesFac().getSubreportLetzteEinstellungen(
				SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTER_ABRECHUNGSVORSCHLAG + theClientDto.getMandant()));

		String sQuery = "SELECT av,  (SELECT MAX(ab.t_unterschrift) FROM FLRAnwesenheitsbestaetigung ab WHERE ab.t_unterschrift <=av.flrzeitdaten.t_aendern AND ab.personal_i_id=av.personal_i_id AND ab.auftrag_i_id=av.auftrag_i_id GROUP BY ab.auftrag_i_id) FROM FLRAbrechnungsvorschlag av LEFT OUTER JOIN av.flrkunde as flrkunde   LEFT JOIN av.flrkunde.flrpartner as flrpartner  LEFT JOIN av.flrauftrag as flrauftrag LEFT JOIN av.flrzeitdaten as flrzeitdaten WHERE av.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' ORDER BY flrpartner.c_name1nachnamefirmazeile1 ASC, flrauftrag.c_nr ASC, av.t_von ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query querylagerbewegungen = session.createQuery(sQuery);
		List<?> results = querylagerbewegungen.list();

		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			FLRAbrechnungsvorschlag av = (FLRAbrechnungsvorschlag) o[0];

			java.util.Date zuletztUnterschrienen = (java.util.Date) o[1];

			Object[] oZeile = new Object[REPORT_ABRECHNUNGSVORSCHLAG_ANZAHL_SPALTEN];

			String art = AbrechnungsvorschlagFac.ART_ZEITDATEN;
			if (av.getAuftragszuordnung_i_id() != null) {
				art = AbrechnungsvorschlagFac.ART_ER;
			} else if (av.getTelefonzeiten_i_id() != null) {
				art = AbrechnungsvorschlagFac.ART_TELEFON;
			}
			if (av.getReise_i_id() != null) {
				art = AbrechnungsvorschlagFac.ART_REISE;
			}
			if (av.getMaschinenzeitdaten_i_id() != null) {
				art = AbrechnungsvorschlagFac.ART_MASCHINE;
			}

			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_ART] = art;
			if (av.getFlrpersonal() != null) {
				oZeile[REPORT_ABRECHNUNGSVORSCHLAG_PERSON] = av.getFlrpersonal().getC_kurzzeichen();
			}
			if (av.getFlrkunde() != null) {
				oZeile[REPORT_ABRECHNUNGSVORSCHLAG_KUNDE] = HelperServer
						.formatNameAusFLRPartner(av.getFlrkunde().getFlrpartner());
			}
			if (av.getFlrauftrag() != null) {
				oZeile[REPORT_ABRECHNUNGSVORSCHLAG_AUFTRAG] = av.getFlrauftrag().getC_nr();
				oZeile[REPORT_ABRECHNUNGSVORSCHLAG_AUFTRAG_STATUS] = av.getFlrauftrag().getAuftragstatus_c_nr();
			}
			if (av.getFlrlos() != null) {
				oZeile[REPORT_ABRECHNUNGSVORSCHLAG_LOS] = av.getFlrlos().getC_nr();
				oZeile[REPORT_ABRECHNUNGSVORSCHLAG_LOS_STATUS] = av.getFlrlos().getStatus_c_nr();

			}

			if (av.getFlrprojekt() != null) {
				oZeile[REPORT_ABRECHNUNGSVORSCHLAG_PROJEKT] = av.getFlrprojekt().getC_nr();
				oZeile[REPORT_ABRECHNUNGSVORSCHLAG_PROJEKT_STATUS] = av.getFlrprojekt().getStatus_c_nr();

			}

			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_B_VERRECHNET] = Helper.short2Boolean(av.getB_verrechnet());
			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_UNTERSCHRIEBEN_AM] = zuletztUnterschrienen;

			if (av.getFlrzeitdaten() != null) {
				if (av.getFlrzeitdaten().getFlrartikel() != null) {
					oZeile[REPORT_ABRECHNUNGSVORSCHLAG_ARTIKELNUMMER] = av.getFlrzeitdaten().getFlrartikel().getC_nr();
					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKey(av.getFlrzeitdaten().getFlrartikel().getI_id(), theClientDto);
					oZeile[REPORT_ABRECHNUNGSVORSCHLAG_ARTIKELBEZEICHNUNG] = aDto.getCBezAusSpr();

				}

			}

			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_VON] = av.getT_von();

			BetriebskalenderDto dto = getPersonalFac().betriebskalenderFindByMandantCNrDDatum(
					Helper.cutTimestamp(new Timestamp(av.getT_von().getTime())), theClientDto.getMandant(),
					theClientDto);
			if (dto != null) {
				oZeile[REPORT_ABRECHNUNGSVORSCHLAG_FEIERTAG] = dto.getCBez();
			}

			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_BIS] = av.getT_bis();

			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_VERRECHENBAR] = av.getF_verrechenbar();

			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_STUNDEN_OFFEN] = av.getN_stunden_offen();
			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_STUNDEN_VERRECHENBAR] = av.getN_stunden_verrechenbar();
			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_STUNDEN_GESAMT] = av.getN_stunden_gesamt();

			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_BETRAG_OFFEN] = av.getN_betrag_offen();
			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_BETRAG_GESAMT] = av.getN_betrag_gesamt();
			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_BETRAG_VERRECHENBAR] = av.getN_betrag_verrechenbar();

			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_KILOMETER_OFFEN] = av.getN_kilometer_offen();
			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_KILOMETER_GESAMT] = av.getN_kilometer_gesamt();
			oZeile[REPORT_ABRECHNUNGSVORSCHLAG_KILOMETER_VERRECHENBAR] = av.getN_kilometer_verrechenbar();

			alDaten.add(oZeile);

		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_ABRECHNUNGSVORSCHLAG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, RechnungReportFac.REPORT_MODUL, REPORT_ABRECHNUNGSVORSCHLAG, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public void enterledigen(HashMap<String, ArrayList<Integer>> hmEnterledigen) {

		Iterator keySet = hmEnterledigen.keySet().iterator();
		while (keySet.hasNext()) {
			String art = (String) keySet.next();

			ArrayList<Integer> alKeys = hmEnterledigen.get(art);

			for (int i = 0; i < alKeys.size(); i++) {

				Integer key = alKeys.get(i);

				if (art.equals(AbrechnungsvorschlagFac.ART_ZEITDATEN)) {
					Zeitdaten entitiy = em.find(Zeitdaten.class, key);
					entitiy.setTErledigt(null);
					entitiy.setPersonalIIdErledigt(null);
					em.merge(entitiy);
				} else if (art.equals(AbrechnungsvorschlagFac.ART_MASCHINE)) {
					Maschinenzeitdaten entitiy = em.find(Maschinenzeitdaten.class, key);
					entitiy.setTErledigt(null);
					entitiy.setPersonalIIdErledigt(null);
					em.merge(entitiy);
				} else if (art.equals(AbrechnungsvorschlagFac.ART_REISE)) {
					Reise entitiy = em.find(Reise.class, key);
					entitiy.setTErledigt(null);
					entitiy.setPersonalIIdErledigt(null);
					em.merge(entitiy);
				} else if (art.equals(AbrechnungsvorschlagFac.ART_TELEFON)) {
					Telefonzeiten entitiy = em.find(Telefonzeiten.class, key);
					entitiy.setTErledigt(null);
					entitiy.setPersonalIIdErledigt(null);
					em.merge(entitiy);
				}
				em.flush();

			}

		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printManuellErledigteEnterledigen(Integer personalIId, Integer kundeIId_I,
			Integer auftragIId_I, DatumsfilterVonBis dVonBis, TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = REPORT_MANUELLERLEDIGTEZEITEN;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_VON", dVonBis.getTimestampVon());
		parameter.put("P_BIS", dVonBis.getTimestampBisUnveraendert());

		// Zuerst personalzeiten

		String sQuery = "SELECT z FROM FLRZeitenmanuellerledigt z LEFT OUTER JOIN z.flrzeitdaten As flrzeitdaten LEFT OUTER JOIN z.flrmaschinenzeitdaten As flrmaschinenzeitdaten LEFT OUTER JOIN z.flrtelefonzeiten As flrtelefonzeiten LEFT OUTER JOIN z.flrreise As flrreise WHERE 1=1  ";

		if (personalIId != null) {

			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
			parameter.put("P_PERSONAL", personalDto.formatFixName1Name2());

			sQuery += " AND ( flrzeitdaten.personal_i_id=" + personalIId + "  OR flrtelefonzeiten.personal_i_id="
					+ personalIId + " OR flrreise.personal_i_id=" + personalIId + ")";

		}

		if (kundeIId_I != null) {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId_I, theClientDto);
			parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatFixName1Name2());
		}

		if (auftragIId_I != null) {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId_I);
			parameter.put("P_AUFTRAG", auftragDto.getCNr());
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query querylagerbewegungen = session.createQuery(sQuery);
		List<?> results = querylagerbewegungen.list();

		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		HashMap<String, ArrayList<Integer>> hmEnterledigen = new HashMap<String, ArrayList<Integer>>();

		while (resultListIterator.hasNext()) {
			FLRZeitenmanuellerledigt av = (FLRZeitenmanuellerledigt) resultListIterator.next();

			Object[] oZeile = new Object[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_ANZAHL_SPALTEN];

			String art = null;
			Integer iId = null;

			Integer auftragIId = null;
			Integer kundeIId = null;
			Integer projektIId = null;
			java.util.Date dVon = null;

			if (av.getFlrzeitdaten() != null) {
				iId = av.getZeitdaten_i_id();
				art = AbrechnungsvorschlagFac.ART_ZEITDATEN;

				dVon = av.getFlrzeitdaten().getT_zeit();

				oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_VON] = av.getFlrzeitdaten().getT_zeit();
				oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_PERSONAL] = HelperServer
						.formatPersonAusFLRPErsonal(av.getFlrzeitdaten().getFlrpersonal());

				if (av.getFlrzeitdaten().getC_belegartnr() != null) {

					if (av.getFlrzeitdaten().getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKeyOhneExc(av.getFlrzeitdaten().getI_belegartid());
						if (auftragDto != null) {
							kundeIId = auftragDto.getKundeIIdRechnungsadresse();
							auftragIId = auftragDto.getIId();
							projektIId = auftragDto.getProjektIId();
						}
					} else if (av.getFlrzeitdaten().getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
						LosDto losDto = getFertigungFac()
								.losFindByPrimaryKeyOhneExc(av.getFlrzeitdaten().getI_belegartid());
						if (losDto != null) {

							if (losDto.getKundeIId() != null) {
								kundeIId = losDto.getKundeIId();
								projektIId = losDto.getProjektIId();
							}

							auftragIId = losDto.getAuftragIId();

						}
					} else if (av.getFlrzeitdaten().getC_belegartnr().equals(LocaleFac.BELEGART_PROJEKT)) {
						ProjektDto pjDto = getProjektFac()
								.projektFindByPrimaryKeyOhneExc(av.getFlrzeitdaten().getI_belegartid());
						if (pjDto != null) {
							projektIId = av.getFlrzeitdaten().getI_belegartid();

							try {
								KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
										pjDto.getPartnerIId(), theClientDto.getMandant(), theClientDto);
								if (kdDto != null) {
									kundeIId = kdDto.getIId();
								}
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

						}
					}
				}
			} else if (av.getFlrmaschinenzeitdaten() != null) {
				iId = av.getMaschinenzeitdaten_i_id();
				art = AbrechnungsvorschlagFac.ART_MASCHINE;
				dVon = av.getFlrmaschinenzeitdaten().getT_von();
				oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_BIS] = av.getFlrmaschinenzeitdaten().getT_bis();

				oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_MASCHINE] = av.getFlrmaschinenzeitdaten().getFlrmaschine()
						.getC_identifikationsnr();

				if (av.getFlrmaschinenzeitdaten().getFlrlossollarbeitsplan() != null) {

					if (av.getFlrmaschinenzeitdaten().getFlrlossollarbeitsplan().getFlrlos().getFlrauftrag() != null) {

						auftragIId = av.getFlrmaschinenzeitdaten().getFlrlossollarbeitsplan().getFlrlos()
								.getFlrauftrag().getI_id();
						kundeIId = av.getFlrmaschinenzeitdaten().getFlrlossollarbeitsplan().getFlrlos().getFlrauftrag()
								.getKunde_i_id_rechnungsadresse();
						if (av.getFlrmaschinenzeitdaten().getFlrlossollarbeitsplan().getFlrlos().getFlrauftrag()
								.getFlrprojekt() != null) {
							projektIId = av.getFlrmaschinenzeitdaten().getFlrlossollarbeitsplan().getFlrlos()
									.getFlrauftrag().getFlrprojekt().getI_id();
						} else if (av.getFlrmaschinenzeitdaten().getFlrlossollarbeitsplan().getFlrlos()
								.getFlrkunde() != null) {
							kundeIId = av.getFlrmaschinenzeitdaten().getFlrlossollarbeitsplan().getFlrlos()
									.getFlrkunde().getI_id();
						}
					}
				}
			} else if (av.getFlrreise() != null) {
				iId = av.getReise_i_id();
				art = AbrechnungsvorschlagFac.ART_REISE;
				dVon = av.getFlrreise().getT_zeit();

				oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_PERSONAL] = HelperServer
						.formatPersonAusFLRPErsonal(av.getFlrreise().getFlrpersonal());

				if (av.getFlrreise().getBelegart_c_nr() != null) {
					if (av.getFlrreise().getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {
						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKeyOhneExc(av.getFlrreise().getI_belegartid());
						if (auftragDto != null) {
							auftragIId = auftragDto.getIId();
							kundeIId = auftragDto.getKundeIIdRechnungsadresse();
						}
					} else if (av.getFlrreise().getBelegart_c_nr().equals(LocaleFac.BELEGART_PROJEKT)) {
						ProjektDto pjDto = getProjektFac()
								.projektFindByPrimaryKeyOhneExc(av.getFlrreise().getI_belegartid());
						if (pjDto != null) {
							projektIId = pjDto.getIId();

							try {
								KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
										pjDto.getPartnerIId(), theClientDto.getMandant(), theClientDto);
								if (kdDto != null) {
									kundeIId = kdDto.getIId();
								}
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

						}
					}
				}

			} else if (av.getFlrtelefonzeiten() != null) {
				iId = av.getTelefonzeiten_i_id();
				art = AbrechnungsvorschlagFac.ART_TELEFON;
				dVon = av.getFlrtelefonzeiten().getT_von();
				oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_BIS] = av.getFlrtelefonzeiten().getT_bis();
				oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_PERSONAL] = HelperServer
						.formatPersonAusFLRPErsonal(av.getFlrtelefonzeiten().getFlrpersonal());

				if (av.getFlrtelefonzeiten().getProjekt_i_id() != null) {
					ProjektDto pjDto = getProjektFac()
							.projektFindByPrimaryKeyOhneExc(av.getFlrtelefonzeiten().getProjekt_i_id());
					projektIId = av.getFlrtelefonzeiten().getProjekt_i_id();

					try {
						KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(pjDto.getPartnerIId(),
								theClientDto.getMandant(), theClientDto);
						if (kdDto != null) {
							kundeIId = kdDto.getIId();
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				} else if (av.getFlrtelefonzeiten().getFlrpartner() != null) {
					try {
						KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
								av.getFlrtelefonzeiten().getFlrpartner().getI_id(), theClientDto.getMandant(),
								theClientDto);
						if (kdDto != null) {
							kundeIId = kdDto.getIId();
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

			}

			oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_VON] = dVon;

			if (dVon.before(dVonBis.getTimestampVon()) || dVon.after(dVonBis.getTimestampBis())) {
				continue;
			}

			if (kundeIId != null) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
				oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_KUNDE] = kundeDto.getPartnerDto().formatFixName1Name2();
			}

			if (auftragIId != null) {
				oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_AUFTRAG] = getAuftragFac()
						.auftragFindByPrimaryKey(auftragIId).getCNr();
			}

			if (projektIId != null) {
				try {
					oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_PROJEKT] = getProjektFac()
							.projektFindByPrimaryKey(projektIId).getCNr();
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			if (kundeIId_I != null && !kundeIId_I.equals(kundeIId)) {
				continue;
			}

			if (auftragIId_I != null && !auftragIId_I.equals(auftragIId)) {
				continue;
			}

			oZeile[REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_ART] = art;

			ArrayList<Integer> keys = new ArrayList<Integer>();

			if (hmEnterledigen.containsKey(art)) {
				keys = hmEnterledigen.get(art);
			}

			keys.add(iId);

			hmEnterledigen.put(art, keys);

			alDaten.add(oZeile);

		}

		parameter.put("P_HM_ENTERLEDIGEN", hmEnterledigen);

		Object[][] returnArray = new Object[alDaten.size()][REPORT_MANUELL_ERLEDIGTE_ENTERLEDIGEN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, RechnungReportFac.REPORT_MODUL, REPORT_MANUELLERLEDIGTEZEITEN, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVerrechnungsmodell(Integer verrechnungsmodellIId, TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = REPORT_VERRECHNUNGSMODELL;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		String sQuery = "SELECT vm FROM FLRVerrechnungsmodelltag vm WHERE vm.verrechnungsmodell_i_id="
				+ verrechnungsmodellIId
				+ " ORDER BY vm.flrtagesart.i_sort ASC, vm.flrartikel_gebucht.c_nr ASC, vm.u_zeitraum_von ASC, vm.u_dauer_ab ASC";

		VerrechnungsmodellDto vmDto = verrechnungsmodellFindByPrimaryKey(verrechnungsmodellIId, theClientDto);
		ArtikelDto aDtoER = getArtikelFac().artikelFindByPrimaryKey(vmDto.getArtikelIIdEr(), theClientDto);
		ArtikelDto aDtoReiseKilometer = getArtikelFac().artikelFindByPrimaryKey(vmDto.getArtikelIIdReisekilometer(),
				theClientDto);
		ArtikelDto aDtoReisespesen = getArtikelFac().artikelFindByPrimaryKey(vmDto.getArtikelIIdReisespesen(),
				theClientDto);
		ArtikelDto aDtoTelefon = getArtikelFac().artikelFindByPrimaryKey(vmDto.getArtikelIIdTelefon(), theClientDto);

		parameter.put("P_BEZEICHNUNG", vmDto.getCBez());

		parameter.put("P_VERSTECKT", Helper.short2Boolean(vmDto.getBVersteckt()));
		parameter.put("P_PREISE_AUS_AUFTRAG", Helper.short2Boolean(vmDto.getBPreiseAusAuftrag()));

		parameter.put("P_AUFSCHLAG_EINGANGSRECHNUNG", vmDto.getFAufschlagEr());

		parameter.put("P_ARTIKELNUMMER_EINGANGSRECHNUNG", aDtoER.getCNr());
		parameter.put("P_ARTIKELNUMMER_REISEKILOMETER", aDtoReiseKilometer.getCNr());
		parameter.put("P_ARTIKELNUMMER_REISESPESEN", aDtoReisespesen.getCNr());
		parameter.put("P_ARTIKELNUMMER_TELEFON", aDtoTelefon.getCNr());

		parameter.put("P_ARTIKELBEZEICHNUNG_EINGANGSRECHNUNG", aDtoER.getCBezAusSpr());
		parameter.put("P_ARTIKELBEZEICHNUNG_REISEKILOMETER", aDtoReiseKilometer.getCBezAusSpr());
		parameter.put("P_ARTIKELBEZEICHNUNG_REISESPESEN", aDtoReisespesen.getCBezAusSpr());
		parameter.put("P_ARTIKELBEZEICHNUNG_TELEFON", aDtoTelefon.getCBezAusSpr());

		parameter.put("P_ARTIKELZUSATZBEZEICHNUNG_EINGANGSRECHNUNG", aDtoER.getCZBezAusSpr());
		parameter.put("P_ARTIKELZUSATZBEZEICHNUNG_REISEKILOMETER", aDtoReiseKilometer.getCZBezAusSpr());
		parameter.put("P_ARTIKELZUSATZBEZEICHNUNG_REISESPESEN", aDtoReisespesen.getCZBezAusSpr());
		parameter.put("P_ARTIKELZUSATZBEZEICHNUNG_TELEFON", aDtoTelefon.getCZBezAusSpr());

		parameter.put("P_ARTIKELZUSATZBEZEICHNUNG2_EINGANGSRECHNUNG", aDtoER.getCZBez2AusSpr());
		parameter.put("P_ARTIKELZUSATZBEZEICHNUNG2_REISEKILOMETER", aDtoReiseKilometer.getCZBez2AusSpr());
		parameter.put("P_ARTIKELZUSATZBEZEICHNUNG2_REISESPESEN", aDtoReisespesen.getCZBez2AusSpr());
		parameter.put("P_ARTIKELZUSATZBEZEICHNUNG2_TELEFON", aDtoTelefon.getCZBez2AusSpr());

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query querylagerbewegungen = session.createQuery(sQuery);
		List<?> results = querylagerbewegungen.list();

		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRVerrechnungsmodelltag av = (FLRVerrechnungsmodelltag) resultListIterator.next();

			Object[] oZeile = new Object[REPORT_VERRECHNUNGSMODELL_ANZAHL_SPALTEN];

			oZeile[REPORT_VERRECHNUNGSMODELL_TAGESART] = av.getFlrtagesart().getC_nr();
			oZeile[REPORT_VERRECHNUNGSMODELL_ARTIKELNUMMER_GEBUCHT] = av.getFlrartikel_gebucht().getC_nr();

			ArtikelDto aDtoGebucht = getArtikelFac().artikelFindByPrimaryKey(av.getFlrartikel_gebucht().getI_id(),
					theClientDto);
			oZeile[REPORT_VERRECHNUNGSMODELL_ARTIKELBEZEICHNUNG_GEBUCHT] = aDtoGebucht.getCBezAusSpr();
			oZeile[REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG_GEBUCHT] = aDtoGebucht.getCZBezAusSpr();
			oZeile[REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG2_GEBUCHT] = aDtoGebucht.getCZBez2AusSpr();

			oZeile[REPORT_VERRECHNUNGSMODELL_ARTIKELNUMMER_ZUVERRECHNEN] = av.getFlrartikel_zuverrechnen().getC_nr();

			oZeile[REPORT_VERRECHNUNGSMODELL_ARTIKEL_ZUVERRECHNEN_KALKULATORISCH] = Helper
					.short2Boolean(av.getFlrartikel_zuverrechnen().getB_kalkulatorisch());

			ArtikelDto aDtoZuVerrechnen = getArtikelFac()
					.artikelFindByPrimaryKey(av.getFlrartikel_zuverrechnen().getI_id(), theClientDto);
			oZeile[REPORT_VERRECHNUNGSMODELL_ARTIKELBEZEICHNUNG_ZUVERRECHNEN] = aDtoZuVerrechnen.getCBezAusSpr();
			oZeile[REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG_ZUVERRECHNEN] = aDtoZuVerrechnen.getCZBezAusSpr();
			oZeile[REPORT_VERRECHNUNGSMODELL_ARTIKELZUSATZBEZEICHNUNG2_ZUVERRECHNEN] = aDtoZuVerrechnen
					.getCZBez2AusSpr();

			oZeile[REPORT_VERRECHNUNGSMODELL_ZEIT_VON] = av.getU_zeitraum_von();

			oZeile[REPORT_VERRECHNUNGSMODELL_DAUER_AB] = av.getU_dauer_ab();
			oZeile[REPORT_VERRECHNUNGSMODELL_REST_DES_TAGES] = Helper.short2boolean(av.getB_endedestages());
			if (Helper.short2boolean(av.getB_endedestages())) {

			} else {
				oZeile[REPORT_VERRECHNUNGSMODELL_ZEIT_BIS] = av.getU_zeitraum_bis();
			}
			try {
				BigDecimal bdPreisbasisGebucht = getVkPreisfindungFac().ermittlePreisbasis(aDtoGebucht.getIId(),
						new java.sql.Date(System.currentTimeMillis()), null, theClientDto.getSMandantenwaehrung(),
						theClientDto);

				BigDecimal bdPreisbasisZuVerrechnen = getVkPreisfindungFac().ermittlePreisbasis(
						aDtoZuVerrechnen.getIId(), new java.sql.Date(System.currentTimeMillis()), null,
						theClientDto.getSMandantenwaehrung(), theClientDto);

				oZeile[REPORT_VERRECHNUNGSMODELL_PREISBASIS_GEBUCHT] = bdPreisbasisGebucht;
				oZeile[REPORT_VERRECHNUNGSMODELL_PREISBASIS_ZU_VERRECHNEN] = bdPreisbasisZuVerrechnen;

				if (bdPreisbasisGebucht != null && bdPreisbasisZuVerrechnen != null) {

					if (bdPreisbasisGebucht.doubleValue() != 0) {
						BigDecimal bdFaktor = bdPreisbasisZuVerrechnen.divide(bdPreisbasisGebucht, 2,
								BigDecimal.ROUND_HALF_EVEN);

						oZeile[REPORT_VERRECHNUNGSMODELL_FAKTOR] = bdFaktor;

					}

				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			alDaten.add(oZeile);

		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_ABRECHNUNGSVORSCHLAG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, RechnungReportFac.REPORT_MODUL, REPORT_VERRECHNUNGSMODELL, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public Integer sindEintraegeInAbrechnungsvorschlag(Integer zeitdatenIId, Integer maschinenzeitdatenIId,
			Integer telefonzeitenIId, Integer auftragszuordnungIId, Integer reiseIId, TheClientDto theClientDto) {
		// PJ20872

		String sQuery = "select av FROM FLRAbrechnungsvorschlag av WHERE 1=1 AND ";

		if (zeitdatenIId != null) {
			sQuery += "av.zeitdaten_i_id=" + zeitdatenIId;
		} else if (maschinenzeitdatenIId != null) {
			sQuery += "av.maschinenzeitdaten_i_id=" + maschinenzeitdatenIId;
		} else if (telefonzeitenIId != null) {
			sQuery += "av.telefonzeiten_i_id=" + telefonzeitenIId;
		} else if (auftragszuordnungIId != null) {
			sQuery += "av.auftragszuordnung_i_id=" + auftragszuordnungIId;
		} else if (reiseIId != null) {
			sQuery += "av.reise_i_id=" + reiseIId;
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createQuery(sQuery);
		query.setMaxResults(1);

		List<?> resultList = query.list();

		if (resultList.size() > 0) {

			FLRAbrechnungsvorschlag av = (FLRAbrechnungsvorschlag) resultList.iterator().next();

			// PJ20872

			// Darf nur von der Person geaendert werden, die aktuell den
			// Abrechnungsvorschlag sperrt

			LockMeDto lockMeDto = new LockMeDto();
			lockMeDto.setCUsernr(theClientDto.getIDUser());

			String mandant = theClientDto.getMandant();

			try {
				lockMeDto.setCWas(mandant);
				lockMeDto.setCWer(LOCKME_ABRECHNUNGSVORSCHLAG_TP);
				LockMeDto[] lockMeDtoLock = getTheJudgeFac().findByWerWasOhneExc(LOCKME_ABRECHNUNGSVORSCHLAG_TP,
						mandant);
				if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
					if (lockMeDtoLock[0].getPersonalIIdLocker().equals(theClientDto.getIDPersonal())) {
						// Wenn ich ihn Sperre, dann darf geaendert werden
						return av.getI_id();
					}
				}

				// ansonsten
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_EINTRAG_IN_ABRECHNUNGSVORSCHLAG_UND_KANN_NICHT_GEAENDERT_WERDEN,
						new Exception("FEHLER_EINTRAG_IN_ABRECHNUNGSVORSCHLAG_UND_KANN_NICHT_GEAENDERT_WERDEN"));

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

		}

		return null;

	}

	public void pruefeBearbeitenDesAbrechungsvorschlagesErlaubt(TheClientDto theClientDto) {
		try {
			// lock-objekt zusammenstellen
			LockMeDto lockMeDto = new LockMeDto();
			lockMeDto.setCUsernr(theClientDto.getIDUser());

			String mandant = theClientDto.getMandant();

			lockMeDto.setCWas(mandant);
			lockMeDto.setCWer(LOCKME_ABRECHNUNGSVORSCHLAG_TP);
			LockMeDto[] lockMeDtoLock = getTheJudgeFac().findByWerWasOhneExc(LOCKME_ABRECHNUNGSVORSCHLAG_TP, mandant);
			if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
				if (lockMeDtoLock[0].getPersonalIIdLocker().equals(theClientDto.getIDPersonal())
						&& lockMeDtoLock[0].getCUsernr().trim().equals(theClientDto.getIDUser().trim())) {
					// dann ist er eh durch diesen benutzer auf diesem client
					// gelockt
					return;
				} else {
					ArrayList<Object> al = new ArrayList<Object>();
					al.add(lockMeDtoLock[0].getCUsernr());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABRECHNUNGSVORSCHLAG_IST_GESPERRT, al,
							new Exception("Abrechungsvorschlag auf Mandant " + mandant + " gesperrt durch Personal Id "
									+ lockMeDtoLock[0].getPersonalIIdLocker()));
				}
			} else {
				// dann sperren
				lockMeDto.setPersonalIIdLocker(theClientDto.getIDPersonal());
				getTheJudgeFac().addLockedObject(lockMeDto, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void removeLockDesAbrechungsvorschlagesWennIchIhnSperre(TheClientDto theClientDto) {
		try {

			String mandant = theClientDto.getMandant();

			LockMeDto[] lockMeDtoLock = getTheJudgeFac().findByWerWasOhneExc(LOCKME_ABRECHNUNGSVORSCHLAG_TP, mandant);
			if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
				if (lockMeDtoLock[0].getPersonalIIdLocker().equals(theClientDto.getIDPersonal())
						&& lockMeDtoLock[0].getCUsernr().trim().equals(theClientDto.getIDUser().trim())) {
					getTheJudgeFac().removeLockedObject(lockMeDtoLock[0]);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

}
