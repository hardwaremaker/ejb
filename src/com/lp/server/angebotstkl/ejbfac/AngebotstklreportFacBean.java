package com.lp.server.angebotstkl.ejbfac;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklarbeitsplan;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklposition;
import com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRPositionlieferant;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklmaterialDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklreportFac;
import com.lp.server.angebotstkl.service.AufschlagDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.PositionlieferantDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.MaschinenStundensatzDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
public class AngebotstklreportFacBean extends LPReport implements AngebotstklreportFac {
	@PersistenceContext
	private EntityManager em;

	@EJB
	private AngebotstklpositionLocalFac angebotstklPositionLocalFac;
	private Object[][] data = null;
	private String sAktuellerReport = null;
	private static int REPORT_ANGEBOTSTKL_NR = 0;
	private static int REPORT_ANGEBOTSTKL_ARTIKELNUMMER = 1;
	private static int REPORT_ANGEBOTSTKL_ARTIKELBEZEICHNUNG = 2;
	private static int REPORT_ANGEBOTSTKL_MENGE = 3;
	private static int REPORT_ANGEBOTSTKL_MENGENEINHEIT = 4;
	private static int REPORT_ANGEBOTSTKL_EINZELPREIS = 5;
	private static int REPORT_ANGEBOTSTKL_NETTOGESAMTPREIS = 6;
	private static int REPORT_ANGEBOTSTKL_AUFSCHLAG = 7;
	private static int REPORT_ANGEBOTSTKL_GESTPREIS = 8;
	private static int REPORT_ANGEBOTSTKL_GESTPREISGESAMT = 9;
	private static int REPORT_ANGEBOTSTKL_POSITIONSART = 10;
	private static int REPORT_ANGEBOTSTKL_POSITIONSOBJEKT = 11;
	private static int REPORT_ANGEBOTSTKL_NETTOPREIS = 12;
	private static int REPORT_ANGEBOTSTKL_RABATTSATZ = 13;
	private static int REPORT_ANGEBOTSTKL_ZUSATZRABATTSATZ = 14;
	private static int REPORT_ANGEBOTSTKL_AUFSCHLAG_PROZENT = 15;
	private static int REPORT_ANGEBOTSTKL_AUFSCHLAG_BETRAG = 16;
	private static int REPORT_ANGEBOTSTKL_NETTOGESAMTPREISMITAUFSCHLAG = 17;
	private static int REPORT_ANGEBOTSTKL_LIEF1PREIS = 18;
	private static int REPORT_ANGEBOTSTKL_LETZTER_GELIEFERT_PREIS = 19;
	private static int REPORT_ANGEBOTSTKL_LETZTES_WE_DATUM = 20;
	private static int REPORT_ANGEBOTSTKL_ARTIKELART = 21;
	private static int REPORT_ANGEBOTSTKL_POSITION = 22;
	private static int REPORT_ANGEBOTSTKL_MIT_PREISEN = 23;
	private static int REPORT_ANGEBOTSTKL_INITIALKOSTEN = 24;
	private static int REPORT_ANGEBOTSTKL_ANZAHL_FELDER = 25;

	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_NR = 0;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELNUMMER = 1;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELBEZEICHNUNG = 2;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGE = 3;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGENEINHEIT = 4;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_EINZELPREIS = 5;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREIS = 6;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG = 7;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREIS = 8;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREISGESAMT = 9;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSART = 10;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSOBJEKT = 11;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOPREIS = 12;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_RABATTSATZ = 13;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ZUSATZRABATTSATZ = 14;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_PROZENT = 15;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_BETRAG = 16;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREISMITAUFSCHLAG = 17;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_LIEF1PREIS = 18;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTER_GELIEFERT_PREIS = 19;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTES_WE_DATUM = 20;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELART = 21;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_VKPREIS_KUNDE = 22;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AG = 23;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINE = 24;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTEN = 25;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AUFSPANNUNG = 26;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AGART = 27;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_RUESTZEIT = 28;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_STUECKZEIT = 29;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT = 30;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER = 31;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTENVK = 32;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_MIT_PREISEN = 33;

	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER_ZUM_VERGLEICHSDATUM = 34;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTEN_ZUM_VERGLEICHSDATUM = 35;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_LIEF1PREIS_ZUM_VERGLEICHSDATUM = 36;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTENVK_ZUM_VERGLEICHSDATUM = 37;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_RUESTMENGE = 38;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_INITIALKOSTEN = 39;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ANZAHL_FELDER = 40;

	private static int REPORT_EINKAUFSANGEBOT_ARTIKELNUMMER = 0;
	private static int REPORT_EINKAUFSANGEBOT_BEZEICHNUNG = 1;
	private static int REPORT_EINKAUFSANGEBOT_KURZBEZEICHNUNG = 2;
	private static int REPORT_EINKAUFSANGEBOT_ZUSATZBEZEICHNUNG = 3;
	private static int REPORT_EINKAUFSANGEBOT_ZUSATZBEZEICHNUNG2 = 4;
	private static int REPORT_EINKAUFSANGEBOT_MENGE = 5;
	private static int REPORT_EINKAUFSANGEBOT_EINHEIT = 6;
	private static int REPORT_EINKAUFSANGEBOT_PREIS1 = 7;
	private static int REPORT_EINKAUFSANGEBOT_PREIS2 = 8;
	private static int REPORT_EINKAUFSANGEBOT_PREIS3 = 9;
	private static int REPORT_EINKAUFSANGEBOT_PREIS4 = 10;
	private static int REPORT_EINKAUFSANGEBOT_PREIS5 = 11;
	private static int REPORT_EINKAUFSANGEBOT_HERSTELLER = 12;
	private static int REPORT_EINKAUFSANGEBOT_HERSTELLERNR = 13;
	private static int REPORT_EINKAUFSANGEBOT_VERPACKUNGSEINHEIT = 14;
	private static int REPORT_EINKAUFSANGEBOT_WIEDERBESCHAFFUNGSZEIT = 15;
	private static int REPORT_EINKAUFSANGEBOT_MINDESTBESTELLMENGE = 16;
	private static int REPORT_EINKAUFSANGEBOT_BEMERKUNG = 17;
	private static int REPORT_EINKAUFSANGEBOT_POSITION = 18;
	private static int REPORT_EINKAUFSANGEBOT_INTERNE_BEMERKUNG = 19;
	private static int REPORT_EINKAUFSANGEBOT_BEMERKUNG_MITDRUCKEN = 20;
	private static int REPORT_EINKAUFSANGEBOT_KOMMENTAR1 = 21;
	private static int REPORT_EINKAUFSANGEBOT_KOMMENTAR2 = 22;
	private static int REPORT_EINKAUFSANGEBOT_LIEFERANT = 23;
	private static int REPORT_EINKAUFSANGEBOT_LETZTEWEBABFRAGE = 24;
	private static int REPORT_EINKAUFSANGEBOT_BUYERURL = 25;
	private static int REPORT_EINKAUFSANGEBOT_HERSTELLERBEZ = 26;
	private static int REPORT_EINKAUFSANGEBOT_LAGERSTAND = 27;
	private static int REPORT_EINKAUFSANGEBOT_FREIE_VERFUEGBARKEIT_ZU_HEUTE = 28;
	private static int REPORT_EINKAUFSANGEBOT_FREIE_VERFUEGBARKEIT_ZUM_PRODUKTIONSDATUM = 29;
	private static int REPORT_EINKAUFSANGEBOT_ANZAHL_SPALTEN = 30;

	private static int REPORT_VERLGLEICH_ARTIKELNUMMER = 0;
	private static int REPORT_VERLGLEICH_BEZEICHNUNG = 1;
	private static int REPORT_VERLGLEICH_MENGE = 2;
	private static int REPORT_VERLGLEICH_EINHEIT = 3;
	private static int REPORT_VERLGLEICH_PREIS1 = 4;
	private static int REPORT_VERLGLEICH_PREIS2 = 5;
	private static int REPORT_VERLGLEICH_PREIS3 = 6;
	private static int REPORT_VERLGLEICH_PREIS4 = 7;
	private static int REPORT_VERLGLEICH_PREIS5 = 8;
	private static int REPORT_VERLGLEICH_HERSTELLER = 9;
	private static int REPORT_VERLGLEICH_HERSTELLERNR = 10;
	private static int REPORT_VERLGLEICH_VERPACKUNGSEINHEIT = 11;
	private static int REPORT_VERLGLEICH_WIEDERBESCHAFFUNGSZEIT = 12;
	private static int REPORT_VERLGLEICH_MINDESTBESTELLMENGE = 13;
	private static int REPORT_VERLGLEICH_BEMERKUNG = 14;
	private static int REPORT_VERLGLEICH_POSITION = 15;
	private static int REPORT_VERLGLEICH_INTERNE_BEMERKUNG = 16;
	private static int REPORT_VERLGLEICH_BEMERKUNG_MITDRUCKEN = 17;
	private static int REPORT_VERLGLEICH_KOMMENTAR1 = 18;
	private static int REPORT_VERLGLEICH_KOMMENTAR2 = 19;
	private static int REPORT_VERLGLEICH_LIEFERANT = 20;
	private static int REPORT_VERLGLEICH_LETZTEWEBABFRAGE = 21;
	private static int REPORT_VERLGLEICH_BUYERURL = 22;
	private static int REPORT_VERLGLEICH_SUBREPORT_LIEFERANTEN = 23;

	private static int REPORT_VERLGLEICH_GUENSTIGSTER_PREIS1 = 24;
	private static int REPORT_VERLGLEICH_GUENSTIGSTER_PREIS2 = 25;
	private static int REPORT_VERLGLEICH_GUENSTIGSTER_PREIS3 = 26;
	private static int REPORT_VERLGLEICH_GUENSTIGSTER_PREIS4 = 27;
	private static int REPORT_VERLGLEICH_GUENSTIGSTER_PREIS5 = 28;
	private static int REPORT_VERLGLEICH_SCHNELLSTE_LIEFERZEIT = 29;

	private static int REPORT_VERLGLEICH_LAGERSTAND = 30;
	private static int REPORT_VERLGLEICH_RESERVIERT = 31;
	private static int REPORT_VERLGLEICH_FEHLMENGE = 32;
	private static int REPORT_VERLGLEICH_INFERTIGUNG = 33;
	private static int REPORT_VERLGLEICH_BESTELLT = 34;
	private static int REPORT_VERLGLEICH_RAHMENRESERVIERT = 35;
	private static int REPORT_VERLGLEICH_RAHMENBESTELLT = 36;

	private static int REPORT_VERLGLEICH_BESTELLT_PREIS1 = 37;
	private static int REPORT_VERLGLEICH_BESTELLT_PREIS2 = 38;
	private static int REPORT_VERLGLEICH_BESTELLT_PREIS3 = 39;
	private static int REPORT_VERLGLEICH_BESTELLT_PREIS4 = 40;
	private static int REPORT_VERLGLEICH_BESTELLT_PREIS5 = 41;

	private static int REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT1 = 42;
	private static int REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT2 = 43;
	private static int REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT3 = 44;
	private static int REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT4 = 45;
	private static int REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT5 = 46;

	private static int REPORT_VERLGLEICH_ANZAHL_SPALTEN = 47;

	public void alteVersionInDokumentenablageKopierenUndAgstklAufGeaendertStellen(Integer agstklIId,
			TheClientDto theClientDto) {

		try {
			JasperPrintLP print = printAngebotstklmenenstaffel(agstklIId, new Timestamp(System.currentTimeMillis()),
					theClientDto);

			String pathSeparator = getSystemFac().getServerPathSeperator();
			String sName = print.getSReportName().substring(print.getSReportName().lastIndexOf(pathSeparator) + 1);
			if (!sName.equals("")) {
				PrintInfoDto oInfo = getJCRDocFac().getPathAndPartnerAndTable(agstklIId, QueryParameters.UC_ID_AGSTKL,
						theClientDto);
				DocPath docPath = oInfo != null ? oInfo.getDocPath() : null;
				if (docPath != null) {
					// Nur archivieren wenn nicht in dieser Tabelle vorhanden
					JCRDocDto jcrDocDto = new JCRDocDto();
					String sBelegnummer = docPath.getLastDocNode().asVisualPath();
					if (sBelegnummer == null) {
						sBelegnummer = "0000000";
					}
					String sRow = docPath.getLastDocNode().asPath();
					sRow = sRow == null ? " " : sRow;
					Integer iPartnerIId = null;
					if (oInfo.getiId() != null) {
						iPartnerIId = oInfo.getiId();
					} else {
						// if (getOptions().getPartnerDtoEmpfaenger() != null) {
						// iPartnerIId = getOptions()
						// .getPartnerDtoEmpfaenger().getIId();
						// } else {
						// // Wenn kein Partner uebergeben dann Default
						// MandantDto mandantDto = DelegateFactory
						// .getInstance()
						// .getMandantDelegate()
						// .mandantFindByPrimaryKey(
						// theClientDto.getMandant());
						// iPartnerIId = mandantDto.getPartnerIId();
						// }
					}
					Helper.setJcrDocBinaryData(jcrDocDto, print.getPrint());

					// AD JR JRPrintSaveContributor saver = new
					// JRPrintSaveContributor(getLocale(),));
					// AD JR saver.save(print.getPrint(), file);
					// jcrDocDto.setbData(Helper.getBytesFromFile(file));
					jcrDocDto.setJasperPrint(print.getPrint());
					// file.delete();
					jcrDocDto.setDocPath(docPath.add(new DocNodeFile(sName)));
					jcrDocDto.setlPartner(iPartnerIId);
					jcrDocDto.setsBelegnummer(sBelegnummer);
					jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
					jcrDocDto.setlAnleger(theClientDto.getIDPersonal());
					jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
					jcrDocDto.setsSchlagworte(" ");
					jcrDocDto.setsName(sName);
					jcrDocDto.setsFilename(sName + ".jrprint");
					if (oInfo.getTable() != null) {
						jcrDocDto.setsTable(oInfo.getTable());
					} else {
						jcrDocDto.setsTable(" ");
					}
					jcrDocDto.setsRow(sRow);
					jcrDocDto.setsMIME(".jrprint");
					jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
					jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
					jcrDocDto.setbVersteckt(false);
					jcrDocDto.setlVersion(getJCRDocFac().getNextVersionNumer(jcrDocDto));
					getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, theClientDto);
				}
			}

			AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(agstklIId);
			agstklDto.setBDatengeaendert(Helper.boolean2Short(true));
			getAngebotstklFac().updateAgstkl(agstklDto, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public JasperPrintLP printAngebotstkl(Integer iIdAngebotstkl, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		index = -1;
		sAktuellerReport = REPORT_ANGEBOTSTUECKLISTE;

		AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(iIdAngebotstkl);
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(agstklDto.getKundeIId(), theClientDto);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRAgstklposition.class);
		crit.add(Expression.eq(AngebotstklFac.FLR_AGSTKLPOSITION_AGSTKL_I_ID, iIdAngebotstkl));
		crit.addOrder(Order.asc("i_sort"));
		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;
		data = new Object[resultList.size()][REPORT_ANGEBOTSTKL_ANZAHL_FELDER];

		BigDecimal bdArbeitswert = new BigDecimal(0);
		BigDecimal bdMaterialwert = new BigDecimal(0);

		while (resultListIterator.hasNext()) {
			FLRAgstklposition flrAgstklposition = (FLRAgstklposition) resultListIterator.next();

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(flrAgstklposition.getFlrartikel().getI_id(), theClientDto);

			if (artikelDto.getArtikelartCNr() != null
					&& artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
				bdArbeitswert = bdArbeitswert
						.add(flrAgstklposition.getN_menge().multiply(flrAgstklposition.getN_gestehungspreis()));
			} else {
				bdMaterialwert = bdMaterialwert
						.add(flrAgstklposition.getN_menge().multiply(flrAgstklposition.getN_gestehungspreis()));

			}
			data[row][REPORT_ANGEBOTSTKL_POSITIONSOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_AGSTUECKLISTE, flrAgstklposition.getI_id(), theClientDto);
			data[row][REPORT_ANGEBOTSTKL_NR] = new Integer(row + 1);
			if (artikelDto.getArtikelartCNr() != null
					&& !artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				data[row][REPORT_ANGEBOTSTKL_ARTIKELNUMMER] = artikelDto.getCNr();
			}
			data[row][REPORT_ANGEBOTSTKL_ARTIKELART] = artikelDto.getArtikelartCNr();
			data[row][REPORT_ANGEBOTSTKL_ARTIKELBEZEICHNUNG] = artikelDto.formatBezeichnung();
			data[row][REPORT_ANGEBOTSTKL_MENGE] = flrAgstklposition.getN_menge();
			data[row][REPORT_ANGEBOTSTKL_MENGENEINHEIT] = flrAgstklposition.getEinheit_c_nr();
			data[row][REPORT_ANGEBOTSTKL_EINZELPREIS] = flrAgstklposition.getN_nettoeinzelpreis();
			data[row][REPORT_ANGEBOTSTKL_POSITIONSART] = flrAgstklposition.getAgstklpositionsart_c_nr();

			data[row][REPORT_ANGEBOTSTKL_NETTOPREIS] = flrAgstklposition.getN_nettogesamtpreis();

			AgstklpositionDto agstklposDto = getAngebotstklpositionFac()
					.agstklpositionFindByPrimaryKey(flrAgstklposition.getI_id(), theClientDto);

			data[row][REPORT_ANGEBOTSTKL_POSITION] = agstklposDto.getCPosition();

			data[row][REPORT_ANGEBOTSTKL_MIT_PREISEN] = Helper.short2Boolean(agstklposDto.getBMitPreisen());
			data[row][REPORT_ANGEBOTSTKL_INITIALKOSTEN] = Helper.short2Boolean(agstklposDto.getBInitial());

			data[row][REPORT_ANGEBOTSTKL_AUFSCHLAG_PROZENT] = agstklposDto.getFAufschlag();
			data[row][REPORT_ANGEBOTSTKL_AUFSCHLAG_BETRAG] = agstklposDto.getNAufschlag();
			data[row][REPORT_ANGEBOTSTKL_NETTOGESAMTPREISMITAUFSCHLAG] = agstklposDto.getNNettogesamtmitaufschlag();

			if (flrAgstklposition.getAgstklpositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {

				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(
						flrAgstklposition.getFlrartikel().getI_id(), null, flrAgstklposition.getN_menge(),
						flrAgstklposition.getFlragstkl().getWaehrung_c_nr(),
						new java.sql.Date(flrAgstklposition.getFlragstkl().getT_belegdatum().getTime()), theClientDto);
				if (alDto != null) {
					data[row][REPORT_ANGEBOTSTKL_LIEF1PREIS] = alDto.getNNettopreis();
				}

				Integer wepIId = getLagerFac().getLetzteWEP_IID(flrAgstklposition.getFlrartikel().getI_id());
				if (wepIId != null) {
					WareneingangspositionDto wepDto = getWareneingangFac()
							.wareneingangspositionFindByPrimaryKeyOhneExc(wepIId);

					if (wepDto != null) {

						WareneingangDto weDto = getWareneingangFac()
								.wareneingangFindByPrimaryKey(wepDto.getWareneingangIId());

						data[row][REPORT_ANGEBOTSTKL_LETZTER_GELIEFERT_PREIS] = wepDto.getNGelieferterpreis();
						data[row][REPORT_ANGEBOTSTKL_LETZTES_WE_DATUM] = weDto.getTWareneingangsdatum();

					}
				}

			}

			data[row][REPORT_ANGEBOTSTKL_RABATTSATZ] = agstklposDto.getFRabattsatz();
			data[row][REPORT_ANGEBOTSTKL_ZUSATZRABATTSATZ] = agstklposDto.getFZusatzrabattsatz();

			data[row][REPORT_ANGEBOTSTKL_NETTOGESAMTPREIS] = flrAgstklposition.getN_menge()
					.multiply(flrAgstklposition.getN_nettogesamtpreis());

			if (flrAgstklposition.getN_gestehungspreis().doubleValue() != 0) {

				data[row][REPORT_ANGEBOTSTKL_AUFSCHLAG] = Helper.getAufschlagProzent(
						flrAgstklposition.getN_nettogesamtpreis(), flrAgstklposition.getN_gestehungspreis(), 4);
			} else {
				data[row][REPORT_ANGEBOTSTKL_AUFSCHLAG] = new BigDecimal(0);
			}

			data[row][REPORT_ANGEBOTSTKL_GESTPREIS] = flrAgstklposition.getN_gestehungspreis();
			if (flrAgstklposition.getN_menge() != null) {
				data[row][REPORT_ANGEBOTSTKL_GESTPREISGESAMT] = flrAgstklposition.getN_gestehungspreis()
						.multiply(flrAgstklposition.getN_menge());
			}
			row++;
		}
		session.close();
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_ANGEBOTSTKL", agstklDto.getCNr());

		parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatTitelAnrede());

		parameter.put("P_PROJEKT", agstklDto.getCBez());

		parameter.put("P_ZEICHNUNGSNUMMER", agstklDto.getCZeichnungsnummer());

		parameter.put("P_WAEHRUNG", agstklDto.getWaehrungCNr());

		parameter.put("P_ARBEITSWERT", bdArbeitswert);

		parameter.put("P_MATERIALWERT", bdMaterialwert);

		// PJ20553
		if (agstklDto.getDatenformatCNr() != null && agstklDto.getOMedia() != null) {
			if (agstklDto.getDatenformatCNr().indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
				BufferedImage image = Helper.byteArrayToImage(agstklDto.getOMedia());
				parameter.put("P_IMAGE", image);
			} else if (agstklDto.getDatenformatCNr().indexOf(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF) != -1) {

				byte[] pdf = agstklDto.getOMedia();

				PDDocument document = null;

				try {

					InputStream myInputStream = new ByteArrayInputStream(pdf);

					document = PDDocument.load(myInputStream);
					PDFRenderer renderer = new PDFRenderer(document);
					int numPages = document.getNumberOfPages();

					if (numPages > 0) {

						BufferedImage image = renderer.renderImageWithDPI(0, 150);
						parameter.put("P_IMAGE", image);

					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

				} finally {
					if (document != null) {

						try {
							document.close();
						} catch (IOException e) {
							e.printStackTrace();
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

						}
					}

				}

			}
		}

		parameter.put("P_SUBREORT_MATERIAL", getSubreportMaterial(theClientDto, null, agstklDto));

		ParametermandantDto parameterMand = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_KALKULATIONSART);
		parameter.put("P_KALKULATIONSART", (Integer) parameterMand.getCWertAsObject());

		// PJ18040
		ArrayList alDaten = new ArrayList();
		String[] fieldnamesAufschlag = new String[] { "Bezeichnung", "AufschlagProzent", "AufschlagWert", "Typ" };

		BigDecimal[] nWerte = getAngebotstklFac().berechneAgstklMaterialwertUndArbeitszeitwert(iIdAngebotstkl,
				theClientDto);

		BigDecimal bdStartwertMaterial = nWerte[0];

		BigDecimal bdAufschlaegeMaterial = new BigDecimal(0);
		Object[] zeile = new Object[fieldnamesAufschlag.length];

		zeile[0] = "Nettosumme Material";
		zeile[1] = null;
		zeile[2] = nWerte[0];
		zeile[3] = "1";
		alDaten.add(zeile);
		AufschlagDto[] aufschlagDtos = getAngebotstklFac().aufschlagFindByBMaterial(iIdAngebotstkl,
				Helper.boolean2Short(true), theClientDto);

		for (int i = 0; i < aufschlagDtos.length; i++) {

			zeile = new Object[fieldnamesAufschlag.length];

			BigDecimal aufschlag = Helper.getProzentWert(bdStartwertMaterial,
					new BigDecimal(aufschlagDtos[i].getAgstklaufschlagDto().getFAufschlag()), 2);

			bdAufschlaegeMaterial = bdAufschlaegeMaterial.add(aufschlag);

			zeile[0] = aufschlagDtos[i].getCBez();
			zeile[1] = aufschlagDtos[i].getAgstklaufschlagDto().getFAufschlag();
			zeile[2] = aufschlag;
			zeile[3] = "1";

			alDaten.add(zeile);

		}

		BigDecimal gesamtwertMaterial = bdStartwertMaterial.add(bdAufschlaegeMaterial);

		zeile = new Object[fieldnamesAufschlag.length];
		zeile[0] = "Materialverkaufspreis";
		zeile[1] = null;
		zeile[2] = gesamtwertMaterial;
		zeile[3] = "2";
		alDaten.add(zeile);

		zeile = new Object[fieldnamesAufschlag.length];
		zeile[0] = "Arbeitszeit";
		zeile[1] = null;
		zeile[2] = nWerte[1];
		zeile[3] = "2";
		alDaten.add(zeile);

		BigDecimal bdStartwertAZ = nWerte[1];
		BigDecimal bdAufschlaegeAZ = new BigDecimal(0);

		aufschlagDtos = getAngebotstklFac().aufschlagFindByBMaterial(iIdAngebotstkl, Helper.boolean2Short(false),
				theClientDto);

		for (int i = 0; i < aufschlagDtos.length; i++) {

			zeile = new Object[fieldnamesAufschlag.length];

			BigDecimal aufschlag = Helper.getProzentWert(bdStartwertAZ,
					new BigDecimal(aufschlagDtos[i].getAgstklaufschlagDto().getFAufschlag()), 2);

			bdAufschlaegeAZ = bdAufschlaegeAZ.add(aufschlag);

			zeile[0] = aufschlagDtos[i].getCBez();
			zeile[1] = aufschlagDtos[i].getAgstklaufschlagDto().getFAufschlag();
			zeile[2] = aufschlag;
			zeile[3] = "2";

			alDaten.add(zeile);

		}

		zeile = new Object[fieldnamesAufschlag.length];
		zeile[0] = "Verkaufspreis Gesamt";
		zeile[1] = null;
		zeile[2] = gesamtwertMaterial.add(bdStartwertAZ.add(bdAufschlaegeAZ));
		zeile[3] = "3";
		alDaten.add(zeile);

		Object[][] dataSubAufschlag = new Object[alDaten.size()][fieldnamesAufschlag.length];
		dataSubAufschlag = (Object[][]) alDaten.toArray(dataSubAufschlag);
		parameter.put("P_SUBREPORT_AUFSCHLAEGE", new LPDatenSubreport(dataSubAufschlag, fieldnamesAufschlag));

		initJRDS(parameter, REPORT_MODUL, REPORT_ANGEBOTSTUECKLISTE, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	public BigDecimal getSummeMaterial(AgstklDto agstklDto, TheClientDto theClientDto) throws RemoteException {

		BigDecimal bdSummeMaterial = BigDecimal.ZERO;
		ArrayList<AgstklmaterialDto> agstklmaterialDto = getAngebotstklpositionFac()
				.agstklmaterialFindByAgstklIId(agstklDto.getIId(), theClientDto);
		ArrayList alDatenSubreport = new ArrayList();
		for (int i = 0; i < agstklmaterialDto.size(); i++) {
			AgstklmaterialDto matDto = agstklmaterialDto.get(i);
			if (matDto.getNGewichtpreis() != null) {
				bdSummeMaterial = bdSummeMaterial.add(matDto.getNGewichtpreis());
			}
		}

		return bdSummeMaterial;
	}

	public LPDatenSubreport getSubreportMaterial(TheClientDto theClientDto, Timestamp tVergleichsdatum,
			AgstklDto agstklDto) throws RemoteException {
		String[] fieldnamesMaterial = new String[] { "Material", "SpezGewicht", "PreisProKG", "Dimension1",
				"Dimension2", "Dimension3", "Materialtyp", "GewichtPreis", "Bezeichnung", "PreisProKGVergleichsdatum" };

		ArrayList<AgstklmaterialDto> agstklmaterialDto = getAngebotstklpositionFac()
				.agstklmaterialFindByAgstklIId(agstklDto.getIId(), theClientDto);
		ArrayList alDatenSubreport = new ArrayList();
		for (int i = 0; i < agstklmaterialDto.size(); i++) {
			AgstklmaterialDto matDto = agstklmaterialDto.get(i);
			Object[] zeileSub = new Object[fieldnamesMaterial.length];

			MaterialDto materialDto = getMaterialFac().materialFindByPrimaryKey(matDto.getMaterialIId(), theClientDto);

			zeileSub[0] = materialDto.getBezeichnung();
			zeileSub[1] = materialDto.getNGewichtInKG();

			BigDecimal materialpreisProKG = getMaterialFac().getMaterialpreisInZielwaehrung(matDto.getMaterialIId(),
					agstklDto.getTBelegdatum(), theClientDto.getSMandantenwaehrung(), theClientDto);

			zeileSub[2] = materialpreisProKG;
			zeileSub[3] = matDto.getNDimension1();
			zeileSub[4] = matDto.getNDimension2();
			zeileSub[5] = matDto.getNDimension3();
			zeileSub[6] = matDto.getCMaterialtyp();
			zeileSub[7] = matDto.getNGewichtpreis();
			zeileSub[8] = matDto.getCBez();

			if (tVergleichsdatum != null) {
				zeileSub[9] = getMaterialFac().getMaterialpreisInZielwaehrung(matDto.getMaterialIId(), tVergleichsdatum,
						theClientDto.getSMandantenwaehrung(), theClientDto);
			}

			alDatenSubreport.add(zeileSub);

		}

		Object[][] dataSubMaterial = new Object[alDatenSubreport.size()][fieldnamesMaterial.length];
		dataSubMaterial = (Object[][]) alDatenSubreport.toArray(dataSubMaterial);

		LPDatenSubreport lpds = new LPDatenSubreport(dataSubMaterial, fieldnamesMaterial);
		return lpds;
	}

	public JasperPrintLP printAngebotstklmenenstaffel(Integer iIdAngebotstkl, Timestamp tVergleichsdatum,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		sAktuellerReport = REPORT_ANGEBOTSTUECKLISTEMENGENSTAFFEL;

		AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(iIdAngebotstkl);
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(agstklDto.getKundeIId(), theClientDto);
		MwstsatzDto mwstsatzKunde = kundeDto.getMwstsatzbezIId() != null
				? getMandantFac().mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(), agstklDto.getTBelegdatum(),
						theClientDto)
				: null;
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRAgstklposition.class);
		crit.add(Expression.eq(AngebotstklFac.FLR_AGSTKLPOSITION_AGSTKL_I_ID, iIdAngebotstkl));
		crit.addOrder(Order.asc("i_sort"));
		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		int row = 0;

		while (resultListIterator.hasNext()) {
			FLRAgstklposition flrAgstklposition = (FLRAgstklposition) resultListIterator.next();

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(flrAgstklposition.getFlrartikel().getI_id(), theClientDto);

			Object[] oZeile = new Object[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ANZAHL_FELDER];

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_AGSTUECKLISTE, flrAgstklposition.getI_id(), theClientDto);
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_NR] = new Integer(row + 1);
			if (artikelDto.getArtikelartCNr() != null
					&& !artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELNUMMER] = artikelDto.getCNr();
			}
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELART] = artikelDto.getArtikelartCNr();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELBEZEICHNUNG] = artikelDto.formatBezeichnung();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGE] = flrAgstklposition.getN_menge();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGENEINHEIT] = flrAgstklposition.getEinheit_c_nr();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_EINZELPREIS] = flrAgstklposition.getN_nettoeinzelpreis();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSART] = flrAgstklposition.getAgstklpositionsart_c_nr();

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOPREIS] = flrAgstklposition.getN_nettogesamtpreis();

			AgstklpositionDto agstklposDto = getAngebotstklpositionFac()
					.agstklpositionFindByPrimaryKey(flrAgstklposition.getI_id(), theClientDto);

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_MIT_PREISEN] = Helper.short2Boolean(agstklposDto.getBMitPreisen());
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_INITIALKOSTEN] = Helper.short2Boolean(agstklposDto.getBInitial());
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_RUESTMENGE] = Helper.short2Boolean(agstklposDto.getBRuestmenge());

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_PROZENT] = agstklposDto.getFAufschlag();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_BETRAG] = agstklposDto.getNAufschlag();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREISMITAUFSCHLAG] = agstklposDto
					.getNNettogesamtmitaufschlag();

			if (flrAgstklposition.getAgstklpositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {

				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(
						flrAgstklposition.getFlrartikel().getI_id(), null, flrAgstklposition.getN_menge(),
						flrAgstklposition.getFlragstkl().getWaehrung_c_nr(),
						new java.sql.Date(flrAgstklposition.getFlragstkl().getT_belegdatum().getTime()), theClientDto);
				if (alDto != null) {
					oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_LIEF1PREIS] = alDto.getNNettopreis();
				}

				alDto = getArtikelFac().getArtikelEinkaufspreis(flrAgstklposition.getFlrartikel().getI_id(), null,
						flrAgstklposition.getN_menge(), flrAgstklposition.getFlragstkl().getWaehrung_c_nr(),
						new java.sql.Date(tVergleichsdatum.getTime()), theClientDto);
				if (alDto != null) {
					oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_LIEF1PREIS_ZUM_VERGLEICHSDATUM] = alDto.getNNettopreis();
				}

				Integer wepIId = getLagerFac().getLetzteWEP_IID(flrAgstklposition.getFlrartikel().getI_id());
				if (wepIId != null) {
					WareneingangspositionDto wepDto = getWareneingangFac()
							.wareneingangspositionFindByPrimaryKeyOhneExc(wepIId);

					if (wepDto != null) {

						WareneingangDto weDto = getWareneingangFac()
								.wareneingangFindByPrimaryKey(wepDto.getWareneingangIId());

						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTER_GELIEFERT_PREIS] = wepDto.getNGelieferterpreis();
						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTES_WE_DATUM] = weDto.getTWareneingangsdatum();

					}
				}

				// VK-Preis
				if (kundeDto.getMwstsatzbezIId() != null) {
					VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(
							flrAgstklposition.getFlrartikel().getI_id(), agstklDto.getKundeIId(),
							flrAgstklposition.getN_menge(),
//									new java.sql.Date(flrAgstklposition.getFlragstkl().getT_belegdatum().getTime()),
							new java.sql.Date(agstklDto.getTBelegdatum().getTime()),
							kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//									getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
//											kundeDto.getMwstsatzbezIId(), theClientDto).getIId(),
							mwstsatzKunde.getIId(), agstklDto.getWaehrungCNr(), theClientDto);

					VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

					if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {

						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_VKPREIS_KUNDE] = kundenVKPreisDto.nettopreis;

					}
				}

			} else {
				// SP5252
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_VKPREIS_KUNDE] = oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREISMITAUFSCHLAG];
			}

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_RABATTSATZ] = agstklposDto.getFRabattsatz();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ZUSATZRABATTSATZ] = agstklposDto.getFZusatzrabattsatz();

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREIS] = flrAgstklposition.getN_menge()
					.multiply(flrAgstklposition.getN_nettogesamtpreis());

			if (flrAgstklposition.getN_gestehungspreis().doubleValue() != 0) {

				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG] = Helper.getAufschlagProzent(
						flrAgstklposition.getN_nettogesamtpreis(), flrAgstklposition.getN_gestehungspreis(), 4);
			} else {
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG] = new BigDecimal(0);
			}

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREIS] = flrAgstklposition.getN_gestehungspreis();
			if (flrAgstklposition.getN_menge() != null) {
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREISGESAMT] = flrAgstklposition.getN_gestehungspreis()
						.multiply(flrAgstklposition.getN_menge());
			}

			alDaten.add(oZeile);
			row++;
		}
		session.close();

		// Arbeitsplan

		ParametermandantDto parameterDto = null;
		try {
			parameterDto = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		String sEinheit = parameterDto.getCWert().trim();

		session = FLRSessionFactory.getFactory().openSession();
		crit = session.createCriteria(FLRAgstklarbeitsplan.class);
		crit.add(Expression.eq(AngebotstklFac.FLR_AGSTKLARBEITSPLAN_AGSTKL_I_ID, iIdAngebotstkl));
		crit.addOrder(Order.asc(AngebotstklFac.FLR_AGSTKLARBEITSPLAN_I_ARBEITSGANG));
		crit.addOrder(Order.asc(AngebotstklFac.FLR_AGSTKLARBEITSPLAN_I_UNTERARBEITSGANG));
		resultList = crit.list();

		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRAgstklarbeitsplan flrAgstklposition = (FLRAgstklarbeitsplan) resultListIterator.next();

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(flrAgstklposition.getFlrartikel().getI_id(), theClientDto);

			Object[] oZeile = new Object[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ANZAHL_FELDER];

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELNUMMER] = artikelDto.getCNr();

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELART] = artikelDto.getArtikelartCNr();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELBEZEICHNUNG] = artikelDto.formatBezeichnung();

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AG] = flrAgstklposition.getI_arbeitsgang();

			
			
			AgstklarbeitsplanDto apDto = getAngebotstklFac()
					.agstklarbeitsplanFindByPrimaryKey(flrAgstklposition.getI_id(), theClientDto);

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_INITIALKOSTEN] = Helper.short2Boolean(apDto.getBInitial());
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AGART] = apDto.getAgartCNr();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AUFSPANNUNG] = flrAgstklposition.getI_aufspannung();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT] = flrAgstklposition.getI_arbeitsgang();

			if (flrAgstklposition.getFlrmaschine() != null) {

				String maschine = "";
				if (flrAgstklposition.getFlrmaschine().getC_identifikationsnr() != null) {
					maschine += flrAgstklposition.getFlrmaschine().getC_identifikationsnr() + " ";
				}
				if (flrAgstklposition.getFlrmaschine().getC_bez() != null) {
					maschine += flrAgstklposition.getFlrmaschine().getC_bez() + " ";
				}
				if (flrAgstklposition.getFlrmaschine().getC_inventarnummer() != null) {
					maschine += flrAgstklposition.getFlrmaschine().getC_inventarnummer();
				}

				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINE] = maschine;
				
				
				MaschinenStundensatzDto maschinenStundensatzDto=	getZeiterfassungFac()
				.getMaschinenKostenZumZeitpunkt(flrAgstklposition.getMaschine_i_id(),
						new Timestamp(flrAgstklposition.getFlragstkl().getT_belegdatum().getTime()),LocaleFac.BELEGART_AGSTUECKLISTE,flrAgstklposition.getI_id());
				
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTEN] =maschinenStundensatzDto.getBdStundensatz();
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTENVK] = maschinenStundensatzDto.getBdStundensatzVK();

				MaschinenStundensatzDto	maschinenStundensatzDtoVergleichsdatum=	getZeiterfassungFac()
						.getMaschinenKostenZumZeitpunkt(flrAgstklposition.getMaschine_i_id(),
								tVergleichsdatum,LocaleFac.BELEGART_AGSTUECKLISTE,flrAgstklposition.getI_id());
				
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTEN_ZUM_VERGLEICHSDATUM] = maschinenStundensatzDtoVergleichsdatum.getBdStundensatz();
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTENVK_ZUM_VERGLEICHSDATUM] = maschinenStundensatzDtoVergleichsdatum.getBdStundensatzVK();

				
				if(apDto.getNStundensatzMaschine()!=null) {
					oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTEN] = apDto.getNStundensatzMaschine();
				}

				
				
			}
			double lStueckzeit = flrAgstklposition.getL_stueckzeit().longValue();
			double lRuestzeit = flrAgstklposition.getL_ruestzeit().longValue();

			double dRuestzeit = 0;
			double dStueckzeit = 0;

			if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
				dStueckzeit = lStueckzeit / 3600000;
				dRuestzeit = lRuestzeit / 3600000;
			} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
				dStueckzeit = lStueckzeit / 60000;
				dRuestzeit = lRuestzeit / 60000;
			} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
				dStueckzeit = lStueckzeit / 100;
				dRuestzeit = lRuestzeit / 100;
			}

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_RUESTZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(dRuestzeit), 5);
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_STUECKZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(dStueckzeit), 5);
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER] = new BigDecimal(0);
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER_ZUM_VERGLEICHSDATUM] = new BigDecimal(
					0);

			BigDecimal dGesamt = new BigDecimal(dStueckzeit).add(new BigDecimal(dRuestzeit));

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT] = Helper.rundeKaufmaennisch(dGesamt, 5);

			if (Helper.short2boolean(flrAgstklposition.getB_nurmaschinenzeit()) == false) {

				try {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreisDesBevorzugtenLieferanten(
									flrAgstklposition.getFlrartikel().getI_id(),
									(BigDecimal) oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT],
									agstklDto.getWaehrungCNr(), theClientDto);

					if (artikellieferantDto != null) {
						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER] = artikellieferantDto
								.getLief1Preis();
					}

					artikellieferantDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferantenZuDatum(
							flrAgstklposition.getFlrartikel().getI_id(),
							(BigDecimal) oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT],
							agstklDto.getWaehrungCNr(), new java.sql.Date(tVergleichsdatum.getTime()), theClientDto);

					if (artikellieferantDto != null) {
						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER_ZUM_VERGLEICHSDATUM] = artikellieferantDto
								.getLief1Preis();
					}

					
					if(apDto.getNStundensatzMann()!=null) {
						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER] = apDto.getNStundensatzMann();
					}
					
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

// TODO SP8308 Warum wird hier das aktuelle Datum verwendet? (ghp, 12.06.2020) 
// Es ist doch immer noch die gleiche Angebotstueckliste mit Belegdatum?!
				// VK-Preis
				if (kundeDto.getMwstsatzbezIId() != null) {
					VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(
							flrAgstklposition.getFlrartikel().getI_id(), agstklDto.getKundeIId(), dGesamt,
//							new Date(System.currentTimeMillis()), 
							new Date(agstklDto.getTBelegdatum().getTime()),
							kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//							getMandantFac()
//									.mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(), theClientDto)
//									.getIId(),
							mwstsatzKunde.getIId(), agstklDto.getWaehrungCNr(), theClientDto);

					VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

					if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {

						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_VKPREIS_KUNDE] = kundenVKPreisDto.nettopreis;

					}
				}

			}
			// SP5252
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREISMITAUFSCHLAG] = oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_VKPREIS_KUNDE];
			alDaten.add(oZeile);
		}
		session.close();

		data = new Object[alDaten.size()][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(data);

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_ANGEBOTSTKL", agstklDto.getCNr());

		parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatTitelAnrede());

		parameter.put("P_PROJEKT", agstklDto.getCBez());

		parameter.put("P_ZEICHNUNGSNUMMER", agstklDto.getCZeichnungsnummer());
		
		parameter.put("P_DATEN_NACHTRAEGLICH_GEAENDERT", Helper.short2Boolean(agstklDto.getBDatengeaendert()));

		parameter.put("P_WAEHRUNG", agstklDto.getWaehrungCNr());

		parameter.put("P_ARBEITSPLAN_ZEITEINHEIT", sEinheit);

		parameter.put("P_EK_PREISBASIS", agstklDto.getIEkpreisbasis());

		// PJ20553
		if (agstklDto.getDatenformatCNr() != null && agstklDto.getOMedia() != null) {
			if (agstklDto.getDatenformatCNr().indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
				BufferedImage image = Helper.byteArrayToImage(agstklDto.getOMedia());
				parameter.put("P_IMAGE", image);
			} else if (agstklDto.getDatenformatCNr().indexOf(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF) != -1) {

				byte[] pdf = agstklDto.getOMedia();

				PDDocument document = null;

				try {

					InputStream myInputStream = new ByteArrayInputStream(pdf);

					document = PDDocument.load(myInputStream);
					PDFRenderer renderer = new PDFRenderer(document);
					int numPages = document.getNumberOfPages();

					if (numPages > 0) {

						BufferedImage image = renderer.renderImageWithDPI(0, 150);
						parameter.put("P_IMAGE", image);

					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

				} finally {
					if (document != null) {

						try {
							document.close();
						} catch (IOException e) {
							e.printStackTrace();
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

						}
					}

				}

			}
		}

		parameter.put("P_SUBREPORT_MATERIAL", getSubreportMaterial(theClientDto, tVergleichsdatum, agstklDto));
		parameter.put("P_SUMME_MATERIAL", getSummeMaterial(agstklDto, theClientDto));

		ParametermandantDto parameterMand = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_KALKULATIONSART);
		parameter.put("P_KALKULATIONSART", (Integer) parameterMand.getCWertAsObject());

		parameter.put("P_DATUM_VERGLEICHSPREIS", tVergleichsdatum);

		parameter.put("P_BELEGDATUM", agstklDto.getTBelegdatum());

		parameter.put("P_SUBREPORT_MENGENSTAFFEL",
				getAngebotstklFac().getSubreportAgstklMengenstaffel(iIdAngebotstkl, theClientDto));
		
		
		parameter.put("P_ZUSATZFUNKTION_SCHNELLERFASSUNG",getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MV_AG_SCHNELLERFASSUNG,
				theClientDto));
		
		parameter.put("P_IN_ANGEBOTEN_VERWENDET",getAngebotstklFac().getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(iIdAngebotstkl, theClientDto));
		
		parameter.put("P_SUBREPORT_MENGENSTAFFEL_SCHNELLERFASSUNG",
				getAngebotstklFac().getSubreportAgstklMengenstaffelSchnellerfassung(iIdAngebotstkl, theClientDto));
		

		initJRDS(parameter, REPORT_MODUL, REPORT_ANGEBOTSTUECKLISTEMENGENSTAFFEL, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printEinkaufsangebot(Integer einkaufsangebotIId, int iSortierung,
			Timestamp tGeplanterFertigungstermin, TheClientDto theClientDto) throws EJBExceptionLP {
		index = -1;
		sAktuellerReport = REPORT_EINKAUFSANGEBOT;

		HashMap parameter = new HashMap<Object, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT e FROM FLREinkaufsangebotposition e LEFT OUTER JOIN e.flrartikel AS a WHERE e.einkaufsangebot_i_id="
				+ einkaufsangebotIId + " ORDER BY ";
		String sort = "";
		if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_ARTIKELNR) {
			sQuery += " a.c_nr ASC ,e.i_sort ASC ";
			sort = getTextRespectUISpr("ekag.druck.sort.artikelnummer", theClientDto.getMandant(),
					theClientDto.getLocUi());
		} else if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_POSITION) {
			sQuery += " e.c_position ASC, a.c_nr ASC, e.i_sort ASC  ";
			sort = getTextRespectUISpr("ekag.druck.sort.position", theClientDto.getMandant(), theClientDto.getLocUi());
		} else if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_BEMERKUNG) {
			sQuery += " e.c_bemerkung, a.c_nr ASC, e.i_sort ASC ";
			sort = getTextRespectUISpr("ekag.druck.sort.bemerkung", theClientDto.getMandant(), theClientDto.getLocUi());
		} else if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_SORT) {
			sQuery += " e.i_sort ASC ";
			sort = getTextRespectUISpr("ekag.druck.sort.sortierung", theClientDto.getMandant(),
					theClientDto.getLocUi());
		}

		parameter.put("P_SORTIERUNG", sort);

		parameter.put("P_GEPLANTER_FERTIGUNGSTERMIN", tGeplanterFertigungstermin);

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;
		data = new Object[resultList.size()][REPORT_EINKAUFSANGEBOT_ANZAHL_SPALTEN];

		while (resultListIterator.hasNext()) {

			FLREinkaufsangebotposition epos = (FLREinkaufsangebotposition) resultListIterator.next();

			EinkaufsangebotpositionDto dto = getAngebotstklFac()
					.einkaufsangebotpositionFindByPrimaryKey(epos.getI_id());
			data[row][REPORT_EINKAUFSANGEBOT_EINHEIT] = dto.getEinheitCNr();
			data[row][REPORT_EINKAUFSANGEBOT_MENGE] = dto.getNMenge();
			data[row][REPORT_EINKAUFSANGEBOT_PREIS1] = dto.getNPreis1();
			data[row][REPORT_EINKAUFSANGEBOT_PREIS2] = dto.getNPreis2();
			data[row][REPORT_EINKAUFSANGEBOT_PREIS3] = dto.getNPreis3();
			data[row][REPORT_EINKAUFSANGEBOT_PREIS4] = dto.getNPreis4();
			data[row][REPORT_EINKAUFSANGEBOT_PREIS5] = dto.getNPreis5();
			data[row][REPORT_EINKAUFSANGEBOT_KOMMENTAR1] = dto.getCKommentar1();
			data[row][REPORT_EINKAUFSANGEBOT_KOMMENTAR2] = dto.getCKommentar2();
			data[row][REPORT_EINKAUFSANGEBOT_MINDESTBESTELLMENGE] = dto.getFMindestbestellmenge();
			data[row][REPORT_EINKAUFSANGEBOT_VERPACKUNGSEINHEIT] = dto.getIVerpackungseinheit();
			data[row][REPORT_EINKAUFSANGEBOT_WIEDERBESCHAFFUNGSZEIT] = dto.getIWiederbeschaffungszeit();

			data[row][REPORT_EINKAUFSANGEBOT_BEMERKUNG] = dto.getCBemerkung();
			data[row][REPORT_EINKAUFSANGEBOT_INTERNE_BEMERKUNG] = dto.getCInternebemerkung();
			data[row][REPORT_EINKAUFSANGEBOT_BEMERKUNG_MITDRUCKEN] = Helper.short2Boolean(dto.getBMitdrucken());
			data[row][REPORT_EINKAUFSANGEBOT_POSITION] = dto.getCPosition();

			if (dto.getHerstellerIId() != null) {
				HerstellerDto herstellerDto = getArtikelFac().herstellerFindByPrimaryKey(dto.getHerstellerIId(),
						theClientDto);
				data[row][REPORT_EINKAUFSANGEBOT_HERSTELLER] = herstellerDto.getPartnerDto().formatFixTitelName1Name2();
			}
			data[row][REPORT_EINKAUFSANGEBOT_HERSTELLERNR] = dto.getCArtikelnrhersteller();
			data[row][REPORT_EINKAUFSANGEBOT_HERSTELLERBEZ] = dto.getCArtikelbezhersteller();
			if (dto.getLieferantIId() != null) {
				LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(dto.getLieferantIId(),
						theClientDto);
				data[row][REPORT_EINKAUFSANGEBOT_LIEFERANT] = lieferantDto.getPartnerDto().formatFixTitelName1Name2();
			}
			data[row][REPORT_EINKAUFSANGEBOT_LETZTEWEBABFRAGE] = dto.getTLetztewebabfrage();
			data[row][REPORT_EINKAUFSANGEBOT_BUYERURL] = dto.getCBuyerurl();

			if (dto.getPositionsartCNr().equals(AngebotstklFac.POSITIONSART_AGSTKL_IDENT)) {

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(dto.getArtikelIId(), theClientDto);

				data[row][REPORT_EINKAUFSANGEBOT_ARTIKELNUMMER] = artikelDto.getCNr();
				data[row][REPORT_EINKAUFSANGEBOT_BEZEICHNUNG] = artikelDto.getCBezAusSpr();
				data[row][REPORT_EINKAUFSANGEBOT_KURZBEZEICHNUNG] = artikelDto.getCKBezAusSpr();
				data[row][REPORT_EINKAUFSANGEBOT_ZUSATZBEZEICHNUNG] = artikelDto.getCZBezAusSpr();
				data[row][REPORT_EINKAUFSANGEBOT_ZUSATZBEZEICHNUNG2] = artikelDto.getCZBez2AusSpr();

				// PJ22227

				if (tGeplanterFertigungstermin != null) {
					data[row][REPORT_EINKAUFSANGEBOT_FREIE_VERFUEGBARKEIT_ZUM_PRODUKTIONSDATUM] = getInternebestellungFac()
							.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto, tGeplanterFertigungstermin,
									null);
				}

				data[row][REPORT_EINKAUFSANGEBOT_FREIE_VERFUEGBARKEIT_ZU_HEUTE] = getInternebestellungFac()
						.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto, getTimestamp(), null);
				try {
					data[row][REPORT_EINKAUFSANGEBOT_LAGERSTAND] = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			} else {
				data[row][REPORT_EINKAUFSANGEBOT_BEZEICHNUNG] = dto.getCBez();
				data[row][REPORT_EINKAUFSANGEBOT_ZUSATZBEZEICHNUNG] = dto.getCZusatzbez();
			}

			row++;
		}

		EinkaufsangebotDto einkaufsangebotDto = getAngebotstklFac().einkaufsangebotFindByPrimaryKey(einkaufsangebotIId);

		parameter.put("P_BELEGNUMMER", einkaufsangebotDto.getCNr());
		parameter.put("P_PROJEKT", einkaufsangebotDto.getCProjekt());

		parameter.put("P_STAFFEL1", einkaufsangebotDto.getNMenge1());
		parameter.put("P_STAFFEL2", einkaufsangebotDto.getNMenge2());
		parameter.put("P_STAFFEL3", einkaufsangebotDto.getNMenge3());
		parameter.put("P_STAFFEL4", einkaufsangebotDto.getNMenge4());
		parameter.put("P_STAFFEL5", einkaufsangebotDto.getNMenge5());

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE,
				theClientDto.getMandant())) {
			parameter.put("P_LIEFERTERMIN_GEPLANT", einkaufsangebotDto.getTLiefertermin());
			parameter.put("P_ROHS", Helper.short2Boolean(einkaufsangebotDto.getBRoHs()));
			parameter.put("P_SUMME_WEBABFRAGEN", einkaufsangebotDto.getIAnzahlwebabfragen());
		}

		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(einkaufsangebotDto.getKundeIId(), theClientDto);

		parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatFixTitelName1Name2());

		initJRDS(parameter, REPORT_MODUL, REPORT_EINKAUFSANGEBOT, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printVergleich(Integer einkaufsangebotIId, int iSortierung, TheClientDto theClientDto)
			throws EJBExceptionLP {
		index = -1;
		sAktuellerReport = REPORT_VERGLEICH;

		HashMap parameter = new HashMap<Object, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT e FROM FLREinkaufsangebotposition e LEFT OUTER JOIN e.flrartikel AS a WHERE e.einkaufsangebot_i_id="
				+ einkaufsangebotIId + " ORDER BY ";

		String sort = "";

		if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_ARTIKELNR) {
			sQuery += " a.c_nr ASC ,e.i_sort ASC ";
			sort = getTextRespectUISpr("ekag.druck.sort.artikelnummer", theClientDto.getMandant(),
					theClientDto.getLocUi());
		} else if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_POSITION) {
			sQuery += " e.c_position ASC, a.c_nr ASC ,e.i_sort ASC ";
			sort = getTextRespectUISpr("ekag.druck.sort.position", theClientDto.getMandant(), theClientDto.getLocUi());
		} else if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_BEMERKUNG) {
			sQuery += " e.c_bemerkung, a.c_nr ASC ,e.i_sort ASC ";
			sort = getTextRespectUISpr("ekag.druck.sort.bemerkung", theClientDto.getMandant(), theClientDto.getLocUi());
		} else if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_SORT) {
			sQuery += " e.i_sort ASC ";
			sort = getTextRespectUISpr("ekag.druck.sort.sortierung", theClientDto.getMandant(),
					theClientDto.getLocUi());
		}

		parameter.put("P_SORTIERUNG", sort);

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;
		data = new Object[resultList.size()][REPORT_VERLGLEICH_ANZAHL_SPALTEN];

		LPDatenSubreport lpDatenSubreportForParameters = null;

		int iBestelltMaxLieferzeit1 = 0;
		int iBestelltMaxLieferzeit2 = 0;
		int iBestelltMaxLieferzeit3 = 0;
		int iBestelltMaxLieferzeit4 = 0;
		int iBestelltMaxLieferzeit5 = 0;

		Integer lieferzeitWennLagerstandAusreichend = 2;
		try {
			ParametermandantDto parameterMand = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_LF_LAGER_LIEFERZEIT_IN_KW);
			lieferzeitWennLagerstandAusreichend = (Integer) parameterMand.getCWertAsObject();
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		int iBestelltMaxLieferzeit1_Lagernd = 0;
		int iBestelltMaxLieferzeit2_Lagernd = 0;
		int iBestelltMaxLieferzeit3_Lagernd = 0;
		int iBestelltMaxLieferzeit4_Lagernd = 0;
		int iBestelltMaxLieferzeit5_Lagernd = 0;

		while (resultListIterator.hasNext()) {

			FLREinkaufsangebotposition epos = (FLREinkaufsangebotposition) resultListIterator.next();

			EinkaufsangebotpositionDto dto = getAngebotstklFac()
					.einkaufsangebotpositionFindByPrimaryKey(epos.getI_id());
			data[row][REPORT_VERLGLEICH_EINHEIT] = dto.getEinheitCNr();
			data[row][REPORT_VERLGLEICH_MENGE] = dto.getNMenge();
			data[row][REPORT_VERLGLEICH_PREIS1] = dto.getNPreis1();
			data[row][REPORT_VERLGLEICH_PREIS2] = dto.getNPreis2();
			data[row][REPORT_VERLGLEICH_PREIS3] = dto.getNPreis3();
			data[row][REPORT_VERLGLEICH_PREIS4] = dto.getNPreis4();
			data[row][REPORT_VERLGLEICH_PREIS5] = dto.getNPreis5();
			data[row][REPORT_VERLGLEICH_KOMMENTAR1] = dto.getCKommentar1();
			data[row][REPORT_VERLGLEICH_KOMMENTAR2] = dto.getCKommentar2();
			data[row][REPORT_VERLGLEICH_MINDESTBESTELLMENGE] = dto.getFMindestbestellmenge();
			data[row][REPORT_VERLGLEICH_VERPACKUNGSEINHEIT] = dto.getIVerpackungseinheit();
			data[row][REPORT_VERLGLEICH_WIEDERBESCHAFFUNGSZEIT] = dto.getIWiederbeschaffungszeit();

			data[row][REPORT_VERLGLEICH_BEMERKUNG] = dto.getCBemerkung();
			data[row][REPORT_VERLGLEICH_INTERNE_BEMERKUNG] = dto.getCInternebemerkung();
			data[row][REPORT_VERLGLEICH_BEMERKUNG_MITDRUCKEN] = Helper.short2Boolean(dto.getBMitdrucken());
			data[row][REPORT_VERLGLEICH_POSITION] = dto.getCPosition();

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE,
					theClientDto.getMandant())) {
				if (dto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac().herstellerFindByPrimaryKey(dto.getHerstellerIId(),
							theClientDto);
					data[row][REPORT_VERLGLEICH_HERSTELLER] = herstellerDto.getPartnerDto().formatFixTitelName1Name2();
				}
				data[row][REPORT_VERLGLEICH_HERSTELLERNR] = dto.getCArtikelnrhersteller();
				if (dto.getLieferantIId() != null) {
					LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(dto.getLieferantIId(),
							theClientDto);
					data[row][REPORT_VERLGLEICH_LIEFERANT] = lieferantDto.getPartnerDto().formatFixTitelName1Name2();
				}
				data[row][REPORT_VERLGLEICH_LETZTEWEBABFRAGE] = dto.getTLetztewebabfrage();
				data[row][REPORT_VERLGLEICH_BUYERURL] = dto.getCBuyerurl();
			}

			if (dto.getPositionsartCNr().equals(AngebotstklFac.POSITIONSART_AGSTKL_IDENT)) {

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(dto.getArtikelIId(), theClientDto);

				data[row][REPORT_VERLGLEICH_ARTIKELNUMMER] = artikelDto.getCNr();
				data[row][REPORT_VERLGLEICH_BEZEICHNUNG] = artikelDto.formatBezeichnung();

				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac()
							.herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto);
					data[row][REPORT_VERLGLEICH_HERSTELLERNR] = herstellerDto.getCNr();
					data[row][REPORT_VERLGLEICH_HERSTELLER] = herstellerDto.getPartnerDto().formatFixTitelName1Name2();
				}

				try {
					data[row][REPORT_VERLGLEICH_LAGERSTAND] = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), theClientDto);
					data[row][REPORT_VERLGLEICH_RESERVIERT] = getReservierungFac()
							.getAnzahlReservierungen(artikelDto.getIId(), theClientDto);
					data[row][REPORT_VERLGLEICH_FEHLMENGE] = getFehlmengeFac()
							.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(), theClientDto);
					data[row][REPORT_VERLGLEICH_INFERTIGUNG] = getFertigungFac()
							.getAnzahlInFertigung(artikelDto.getIId(), theClientDto);
					data[row][REPORT_VERLGLEICH_BESTELLT] = getArtikelbestelltFac()
							.getAnzahlBestellt(artikelDto.getIId());
					data[row][REPORT_VERLGLEICH_RAHMENRESERVIERT] = getReservierungFac()
							.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);
					Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac()
							.getAnzahlRahmenbestellt(artikelDto.getIId(), theClientDto);
					if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
						BigDecimal rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
								.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
						data[row][REPORT_VERLGLEICH_RAHMENBESTELLT] = rahmenbestellt;
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			} else {
				String sBez = dto.getCBez();
				if (dto.getCZusatzbez() != null) {
					sBez += "\n" + dto.getCZusatzbez();
				}
				data[row][REPORT_VERLGLEICH_BEZEICHNUNG] = sBez;
			}

			// Subreport

			String[] fieldnames = new String[] { "POSITIONLIEFERANT_I_ID", "Lieferant", "ArtikelnummerLF",
					"LagerstandLF", "LieferzeitInKW", "Verpackungseinheit", "Mindestbestellmenge", "Transportkosten",
					"PreisMenge1", "PreisMenge2", "PreisMenge3", "PreisMenge4", "PreisMenge5", "Menge1Bestellen",
					"Menge2Bestellen", "Menge3Bestellen", "Menge4Bestellen", "Menge5Bestellen", "Bemerkung",
					"BemerkungIntern", "BemerkungVerkauf" };

			Session sessionLF = FLRSessionFactory.getFactory().openSession();
			String queryStringLF = "SELECT poslief FROM FLRPositionlieferant AS poslief WHERE poslief.einkaufsangebotposition_i_id="
					+ epos.getI_id()
					+ " ORDER BY poslief.flrekaglieferant.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 ASC";

			org.hibernate.Query queryLF = sessionLF.createQuery(queryStringLF);
			List<?> resultListLF = queryLF.list();

			Iterator<?> resultListIteratorLF = resultListLF.iterator();

			ArrayList alDatenSubreport = new ArrayList();

			BigDecimal bdPreisBestellt1 = null;
			BigDecimal bdPreisBestellt2 = null;
			BigDecimal bdPreisBestellt3 = null;
			BigDecimal bdPreisBestellt4 = null;
			BigDecimal bdPreisBestellt5 = null;

			Integer iLZBestellt1 = null;
			Integer iLZBestellt2 = null;
			Integer iLZBestellt3 = null;
			Integer iLZBestellt4 = null;
			Integer iLZBestellt5 = null;

			while (resultListIteratorLF.hasNext()) {
				FLRPositionlieferant poslief = (FLRPositionlieferant) resultListIteratorLF.next();

				PositionlieferantDto positionlieferantDto = getAngebotstklFac()
						.positionlieferantFindByPrimaryKeyInZielWaehrung(poslief.getI_id(),
								theClientDto.getSMandantenwaehrung(), theClientDto);

				Object[] zeile = new Object[fieldnames.length];
				zeile[0] = poslief.getI_id();
				zeile[1] = HelperServer
						.formatNameAusFLRPartner(poslief.getFlrekaglieferant().getFlrlieferant().getFlrpartner());
				zeile[2] = positionlieferantDto.getCArtikelnrlieferant();
				zeile[3] = positionlieferantDto.getNLagerstand();
				zeile[4] = positionlieferantDto.getILieferzeitinkw();
				zeile[5] = positionlieferantDto.getNVerpackungseinheit();
				zeile[6] = positionlieferantDto.getNMindestbestellmenge();
				zeile[7] = positionlieferantDto.getNTransportkosten();
				zeile[8] = positionlieferantDto.getNPreisMenge1();
				zeile[9] = positionlieferantDto.getNPreisMenge2();
				zeile[10] = positionlieferantDto.getNPreisMenge3();
				zeile[11] = positionlieferantDto.getNPreisMenge4();
				zeile[12] = positionlieferantDto.getNPreisMenge5();

				Boolean b1 = Helper.short2Boolean(positionlieferantDto.getBMenge1Bestellen());
				Boolean b2 = Helper.short2Boolean(positionlieferantDto.getBMenge2Bestellen());
				Boolean b3 = Helper.short2Boolean(positionlieferantDto.getBMenge3Bestellen());
				Boolean b4 = Helper.short2Boolean(positionlieferantDto.getBMenge4Bestellen());
				Boolean b5 = Helper.short2Boolean(positionlieferantDto.getBMenge5Bestellen());

				if (b1 == true) {
					bdPreisBestellt1 = positionlieferantDto.getNPreisMenge1();
					iLZBestellt1 = positionlieferantDto.getILieferzeitinkw();

					if (iLZBestellt1 != null && iLZBestellt1 > iBestelltMaxLieferzeit1) {
						iBestelltMaxLieferzeit1 = iLZBestellt1;
					}

					if (iLZBestellt1 != null
							&& poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge1() != null
							&& poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge1()
									.doubleValue() > 0) {

						BigDecimal bdMenge1 = poslief.getFlreinkaufsangebotposition().getN_menge().multiply(
								poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge1());

						iLZBestellt1 = positionlieferantDto.getILieferzeitinkw();
						if (positionlieferantDto.getNLagerstand() != null
								&& positionlieferantDto.getNLagerstand().doubleValue() >= bdMenge1.doubleValue()) {
							iLZBestellt1 = lieferzeitWennLagerstandAusreichend;
						}

						if (iLZBestellt1 > iBestelltMaxLieferzeit1_Lagernd) {
							iBestelltMaxLieferzeit1_Lagernd = iLZBestellt1;
						}

					}

				}
				if (b2 == true) {
					bdPreisBestellt2 = positionlieferantDto.getNPreisMenge2();
					iLZBestellt2 = positionlieferantDto.getILieferzeitinkw();

					if (iLZBestellt2 != null && iLZBestellt2 > iBestelltMaxLieferzeit2) {
						iBestelltMaxLieferzeit2 = iLZBestellt2;
					}

					if (iLZBestellt2 != null
							&& poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge2() != null
							&& poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge2()
									.doubleValue() > 0) {

						BigDecimal bdMenge2 = poslief.getFlreinkaufsangebotposition().getN_menge().multiply(
								poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge2());

						iLZBestellt2 = positionlieferantDto.getILieferzeitinkw();
						if (positionlieferantDto.getNLagerstand() != null
								&& positionlieferantDto.getNLagerstand().doubleValue() >= bdMenge2.doubleValue()) {
							iLZBestellt2 = lieferzeitWennLagerstandAusreichend;
						}

						if (iLZBestellt2 > iBestelltMaxLieferzeit2_Lagernd) {
							iBestelltMaxLieferzeit2_Lagernd = iLZBestellt2;
						}

					}

				}
				if (b3 == true) {
					bdPreisBestellt3 = positionlieferantDto.getNPreisMenge3();
					iLZBestellt3 = positionlieferantDto.getILieferzeitinkw();

					if (iLZBestellt3 != null && iLZBestellt3 > iBestelltMaxLieferzeit3) {
						iBestelltMaxLieferzeit3 = iLZBestellt3;
					}

					if (iLZBestellt3 != null
							&& poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge3() != null
							&& poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge3()
									.doubleValue() > 0) {

						BigDecimal bdMenge3 = poslief.getFlreinkaufsangebotposition().getN_menge().multiply(
								poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge3());

						iLZBestellt3 = positionlieferantDto.getILieferzeitinkw();
						if (positionlieferantDto.getNLagerstand() != null
								&& positionlieferantDto.getNLagerstand().doubleValue() >= bdMenge3.doubleValue()) {
							iLZBestellt3 = lieferzeitWennLagerstandAusreichend;
						}

						if (iLZBestellt3 > iBestelltMaxLieferzeit3_Lagernd) {
							iBestelltMaxLieferzeit3_Lagernd = iLZBestellt3;
						}

					}

				}
				if (b4 == true) {
					bdPreisBestellt4 = positionlieferantDto.getNPreisMenge4();
					iLZBestellt4 = positionlieferantDto.getILieferzeitinkw();

					if (iLZBestellt4 != null && iLZBestellt4 > iBestelltMaxLieferzeit4) {
						iBestelltMaxLieferzeit4 = iLZBestellt4;
					}

					if (iLZBestellt4 != null
							&& poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge4() != null
							&& poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge4()
									.doubleValue() > 0) {

						BigDecimal bdMenge4 = poslief.getFlreinkaufsangebotposition().getN_menge().multiply(
								poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge4());

						iLZBestellt4 = positionlieferantDto.getILieferzeitinkw();
						if (positionlieferantDto.getNLagerstand() != null
								&& positionlieferantDto.getNLagerstand().doubleValue() >= bdMenge4.doubleValue()) {
							iLZBestellt4 = lieferzeitWennLagerstandAusreichend;
						}

						if (iLZBestellt4 > iBestelltMaxLieferzeit4_Lagernd) {
							iBestelltMaxLieferzeit4_Lagernd = iLZBestellt4;
						}

					}

				}
				if (b5 == true) {
					bdPreisBestellt5 = positionlieferantDto.getNPreisMenge5();
					iLZBestellt5 = positionlieferantDto.getILieferzeitinkw();

					if (iLZBestellt5 != null && iLZBestellt5 > iBestelltMaxLieferzeit5) {
						iBestelltMaxLieferzeit5 = iLZBestellt5;
					}

					if (iLZBestellt5 != null
							&& poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge5() != null
							&& poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge5()
									.doubleValue() > 0) {

						BigDecimal bdMenge5 = poslief.getFlreinkaufsangebotposition().getN_menge().multiply(
								poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge5());

						iLZBestellt5 = positionlieferantDto.getILieferzeitinkw();
						if (positionlieferantDto.getNLagerstand() != null
								&& positionlieferantDto.getNLagerstand().doubleValue() >= bdMenge5.doubleValue()) {
							iLZBestellt5 = lieferzeitWennLagerstandAusreichend;
						}

						if (iLZBestellt5 > iBestelltMaxLieferzeit5_Lagernd) {
							iBestelltMaxLieferzeit5_Lagernd = iLZBestellt5;
						}

					}

				}

				zeile[13] = b1;
				zeile[14] = b2;
				zeile[15] = b3;
				zeile[16] = b4;
				zeile[17] = b5;

				zeile[18] = positionlieferantDto.getCBemerkung();
				zeile[19] = positionlieferantDto.getCBemerkungIntern();
				zeile[20] = positionlieferantDto.getCBemerkungVerkauf();

				alDatenSubreport.add(zeile);
			}

			Object[][] dataSub = new Object[alDatenSubreport.size()][fieldnames.length];
			dataSub = (Object[][]) alDatenSubreport.toArray(dataSub);
			data[row][REPORT_VERLGLEICH_SUBREPORT_LIEFERANTEN] = new LPDatenSubreport(dataSub, fieldnames);

			if (lpDatenSubreportForParameters == null) {
				lpDatenSubreportForParameters = new LPDatenSubreport(dataSub, fieldnames);
			}

			PositionlieferantDto posSchnellster = getAngebotstklFac().getSchnellstenLieferant(epos.getI_id(),
					theClientDto);
			if (posSchnellster != null) {
				data[row][REPORT_VERLGLEICH_SCHNELLSTE_LIEFERZEIT] = posSchnellster.getILieferzeitinkw();
			}

			PositionlieferantDto posGuenstigsterMenge1 = getAngebotstklFac().getGuenstigstenLieferant(epos.getI_id(),
					AngebotstklFac.MENGE_1, theClientDto);
			if (posGuenstigsterMenge1 != null) {
				data[row][REPORT_VERLGLEICH_GUENSTIGSTER_PREIS1] = posGuenstigsterMenge1.getNPreisMenge1();
			}

			PositionlieferantDto posGuenstigsterMenge2 = getAngebotstklFac().getGuenstigstenLieferant(epos.getI_id(),
					AngebotstklFac.MENGE_2, theClientDto);
			if (posGuenstigsterMenge2 != null) {
				data[row][REPORT_VERLGLEICH_GUENSTIGSTER_PREIS2] = posGuenstigsterMenge2.getNPreisMenge2();
			}

			PositionlieferantDto posGuenstigsterMenge3 = getAngebotstklFac().getGuenstigstenLieferant(epos.getI_id(),
					AngebotstklFac.MENGE_3, theClientDto);
			if (posGuenstigsterMenge3 != null) {
				data[row][REPORT_VERLGLEICH_GUENSTIGSTER_PREIS3] = posGuenstigsterMenge3.getNPreisMenge3();
			}

			PositionlieferantDto posGuenstigsterMenge4 = getAngebotstklFac().getGuenstigstenLieferant(epos.getI_id(),
					AngebotstklFac.MENGE_4, theClientDto);
			if (posGuenstigsterMenge4 != null) {
				data[row][REPORT_VERLGLEICH_GUENSTIGSTER_PREIS4] = posGuenstigsterMenge4.getNPreisMenge4();
			}

			PositionlieferantDto posGuenstigsterMenge5 = getAngebotstklFac().getGuenstigstenLieferant(epos.getI_id(),
					AngebotstklFac.MENGE_5, theClientDto);
			if (posGuenstigsterMenge5 != null) {
				data[row][REPORT_VERLGLEICH_GUENSTIGSTER_PREIS5] = posGuenstigsterMenge5.getNPreisMenge5();
			}

			data[row][REPORT_VERLGLEICH_BESTELLT_PREIS1] = bdPreisBestellt1;
			data[row][REPORT_VERLGLEICH_BESTELLT_PREIS2] = bdPreisBestellt2;
			data[row][REPORT_VERLGLEICH_BESTELLT_PREIS3] = bdPreisBestellt3;
			data[row][REPORT_VERLGLEICH_BESTELLT_PREIS4] = bdPreisBestellt4;
			data[row][REPORT_VERLGLEICH_BESTELLT_PREIS5] = bdPreisBestellt5;

			data[row][REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT1] = iLZBestellt1;
			data[row][REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT2] = iLZBestellt2;
			data[row][REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT3] = iLZBestellt3;
			data[row][REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT4] = iLZBestellt4;
			data[row][REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT5] = iLZBestellt5;

			row++;
		}

		EinkaufsangebotDto einkaufsangebotDto = getAngebotstklFac().einkaufsangebotFindByPrimaryKey(einkaufsangebotIId);

		parameter.put("P_SUBREPORT_LIEFERANTEN", lpDatenSubreportForParameters);

		parameter.put("P_BELEGNUMMER", einkaufsangebotDto.getCNr());
		parameter.put("P_PROJEKT", einkaufsangebotDto.getCProjekt());

		parameter.put("P_STAFFEL1", einkaufsangebotDto.getNMenge1());
		parameter.put("P_STAFFEL2", einkaufsangebotDto.getNMenge2());
		parameter.put("P_STAFFEL3", einkaufsangebotDto.getNMenge3());
		parameter.put("P_STAFFEL4", einkaufsangebotDto.getNMenge4());
		parameter.put("P_STAFFEL5", einkaufsangebotDto.getNMenge5());

		parameter.put("P_KOMMENTAR", einkaufsangebotDto.getCKommentar());

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE,
				theClientDto.getMandant())) {
			parameter.put("P_LIEFERTERMIN_GEPLANT", einkaufsangebotDto.getTLiefertermin());
			parameter.put("P_ROHS", Helper.short2Boolean(einkaufsangebotDto.getBRoHs()));
			parameter.put("P_SUMME_WEBABFRAGEN", einkaufsangebotDto.getIAnzahlwebabfragen());
		}

		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(einkaufsangebotDto.getKundeIId(), theClientDto);

		parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatFixTitelName1Name2());

		parameter.put("P_BESTELLT_MAX_LIEFERZEIT_1", iBestelltMaxLieferzeit1);
		parameter.put("P_BESTELLT_MAX_LIEFERZEIT_2", iBestelltMaxLieferzeit2);
		parameter.put("P_BESTELLT_MAX_LIEFERZEIT_3", iBestelltMaxLieferzeit3);
		parameter.put("P_BESTELLT_MAX_LIEFERZEIT_4", iBestelltMaxLieferzeit4);
		parameter.put("P_BESTELLT_MAX_LIEFERZEIT_5", iBestelltMaxLieferzeit5);

		parameter.put("P_BESTELLT_MAX_LIEFERZEIT_1_LAGERND", iBestelltMaxLieferzeit1_Lagernd);
		parameter.put("P_BESTELLT_MAX_LIEFERZEIT_2_LAGERND", iBestelltMaxLieferzeit2_Lagernd);
		parameter.put("P_BESTELLT_MAX_LIEFERZEIT_3_LAGERND", iBestelltMaxLieferzeit3_Lagernd);
		parameter.put("P_BESTELLT_MAX_LIEFERZEIT_4_LAGERND", iBestelltMaxLieferzeit4_Lagernd);
		parameter.put("P_BESTELLT_MAX_LIEFERZEIT_5_LAGERND", iBestelltMaxLieferzeit5_Lagernd);

		initJRDS(parameter, REPORT_MODUL, REPORT_VERGLEICH, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;

		String fieldName = field.getName();

		if (sAktuellerReport.equals(REPORT_ANGEBOTSTUECKLISTE)) {

			if ("Nummer".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_NR];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_ARTIKELNUMMER];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_POSITION];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_ARTIKELART];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_ARTIKELBEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_MENGE];
			} else if ("Mengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_MENGENEINHEIT];
			} else if ("Einzelpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_EINZELPREIS];
			} else if ("Nettogesamtpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_NETTOGESAMTPREIS];
			} else if ("Nettopreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_NETTOPREIS];
			} else if ("MitPreisen".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_MIT_PREISEN];
			} else if ("Rabattsatz".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_RABATTSATZ];
			} else if ("Zusatzrabattsatz".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_ZUSATZRABATTSATZ];
			} else if ("Aufschlag".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_AUFSCHLAG];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_GESTPREIS];
			} else if ("Gestpreisgesamt".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_GESTPREISGESAMT];
			} else if ("F_POSITIONSART".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_POSITIONSART];
			} else if ("F_POSITIONSOBJEKT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_POSITIONSOBJEKT];
			} else if ("F_AUFSCHLAG_PROZENT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_AUFSCHLAG_PROZENT];
			} else if ("F_AUFSCHLAG_BETRAG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_AUFSCHLAG_BETRAG];
			} else if ("F_NETTOGESAMTPREISMITAUFSCHLAG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_NETTOGESAMTPREISMITAUFSCHLAG];
			} else if ("F_LIEF1PREIS".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_LIEF1PREIS];
			} else if ("F_LETZTER_GELIEFERT_PREIS".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_LETZTER_GELIEFERT_PREIS];
			} else if ("F_LETZTES_WE_DATUM".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_LETZTES_WE_DATUM];
			} else if ("Initialkosten".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_INITIALKOSTEN];
			} 

		} else if (sAktuellerReport.equals(REPORT_ANGEBOTSTUECKLISTEMENGENSTAFFEL)) {

			if ("Nummer".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_NR];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELNUMMER];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELART];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELBEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGE];
			} else if ("Mengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGENEINHEIT];
			} else if ("Einzelpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_EINZELPREIS];
			} else if ("Nettogesamtpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREIS];
			} else if ("MitPreisen".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_MIT_PREISEN];
			} else if ("Initialkosten".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_INITIALKOSTEN];
			} else if ("Nettopreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOPREIS];
			} else if ("Ruestmenge".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_RUESTMENGE];
			} else if ("Rabattsatz".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_RABATTSATZ];
			} else if ("Zusatzrabattsatz".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ZUSATZRABATTSATZ];
			} else if ("Aufschlag".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREIS];
			} else if ("Gestpreisgesamt".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREISGESAMT];
			} else if ("F_POSITIONSART".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSART];
			} else if ("F_POSITIONSOBJEKT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSOBJEKT];
			} else if ("F_AUFSCHLAG_PROZENT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_PROZENT];
			} else if ("F_AUFSCHLAG_BETRAG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_BETRAG];
			} else if ("F_NETTOGESAMTPREISMITAUFSCHLAG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREISMITAUFSCHLAG];
			} else if ("F_LIEF1PREIS".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_LIEF1PREIS];
			} else if ("F_LETZTER_GELIEFERT_PREIS".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTER_GELIEFERT_PREIS];
			} else if ("F_LETZTES_WE_DATUM".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTES_WE_DATUM];
			} else if ("F_VKPREIS_KUNDE".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_VKPREIS_KUNDE];
			}

			else if ("F_ARBEITSPLAN_AG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AG];
			} else if ("F_ARBEITSPLAN_MASCHINE".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINE];
			} else if ("F_ARBEITSPLAN_MASCHINENKOSTEN".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTEN];
			} else if ("F_ARBEITSPLAN_MASCHINENKOSTEN_VK".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTENVK];
			} else if ("F_ARBEITSPLAN_AUFSPANNUNG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AUFSPANNUNG];
			} else if ("F_ARBEITSPLAN_AGART".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AGART];
			} else if ("F_ARBEITSPLAN_RUESTZEIT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_RUESTZEIT];
			} else if ("F_ARBEITSPLAN_STUECKZEIT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_STUECKZEIT];
			} else if ("F_ARBEITSPLAN_GESAMTZEIT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT];
			} else if ("F_ARBEITSPLAN_GESPREIS_MITARBEITER".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER];
			}

			else if ("F_ARBEITSPLAN_GESPREIS_MITARBEITER_ZUM_VERGLEICHSDATUM".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER_ZUM_VERGLEICHSDATUM];
			} else if ("F_ARBEITSPLAN_MASCHINENKOSTEN_ZUM_VERGLEICHSDATUM".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTEN_ZUM_VERGLEICHSDATUM];
			} else if ("F_ARBEITSPLAN_MASCHINENKOSTENVK_ZUM_VERGLEICHSDATUM".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTENVK_ZUM_VERGLEICHSDATUM];
			} else if ("F_LIEF1PREIS_ZUM_VERGLEICHSDATUM".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_LIEF1PREIS_ZUM_VERGLEICHSDATUM];
			}

		} else if (sAktuellerReport.equals(REPORT_EINKAUFSANGEBOT)) {

			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_BEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_EINHEIT];
			} else if ("Hersteller".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_HERSTELLER];
			} else if ("Herstellernr".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_HERSTELLERNR];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_MENGE];
			} else if ("Mindestbestellmenge".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_MINDESTBESTELLMENGE];
			} else if ("Preis1".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_PREIS1];
			} else if ("Preis2".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_PREIS2];
			} else if ("Preis3".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_PREIS3];
			} else if ("Preis4".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_PREIS4];
			} else if ("Preis5".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_PREIS5];
			} else if ("Verpackungseinheit".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_VERPACKUNGSEINHEIT];
			} else if ("Wiederbeschaffungszeit".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_WIEDERBESCHAFFUNGSZEIT];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_POSITION];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_BEMERKUNG];
			} else if ("InterneBemerkung".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_INTERNE_BEMERKUNG];
			} else if ("InterneBemerkungMitdrucken".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_BEMERKUNG_MITDRUCKEN];
			} else if ("Kommentar1".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_KOMMENTAR1];
			} else if ("Kommentar2".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_KOMMENTAR2];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_LIEFERANT];
			} else if ("F_LETZTE_WEBABFRAGE".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_LETZTEWEBABFRAGE];
			} else if ("F_BUYER_URL".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_BUYERURL];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_ZUSATZBEZEICHNUNG2];
			} else if ("Herstellerbez".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_HERSTELLERBEZ];
			} else if ("FreieVerfuegbarkeitHeute".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_FREIE_VERFUEGBARKEIT_ZU_HEUTE];
			} else if ("FreieVerfuegbarkeitZumProduktionsdatum".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_FREIE_VERFUEGBARKEIT_ZUM_PRODUKTIONSDATUM];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_LAGERSTAND];
			}

		} else if (sAktuellerReport.equals(REPORT_VERGLEICH)) {

			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_EINHEIT];
			} else if ("Hersteller".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_HERSTELLER];
			} else if ("Herstellernr".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_HERSTELLERNR];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_MENGE];
			} else if ("Mindestbestellmenge".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_MINDESTBESTELLMENGE];
			} else if ("Preis1".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_PREIS1];
			} else if ("Preis2".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_PREIS2];
			} else if ("Preis3".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_PREIS3];
			} else if ("Preis4".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_PREIS4];
			} else if ("Preis5".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_PREIS5];
			} else if ("Verpackungseinheit".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_VERPACKUNGSEINHEIT];
			} else if ("Wiederbeschaffungszeit".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_WIEDERBESCHAFFUNGSZEIT];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_POSITION];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BEMERKUNG];
			} else if ("InterneBemerkung".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_INTERNE_BEMERKUNG];
			} else if ("InterneBemerkungMitdrucken".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BEMERKUNG_MITDRUCKEN];
			} else if ("Kommentar1".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_KOMMENTAR1];
			} else if ("Kommentar2".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_KOMMENTAR2];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_LIEFERANT];
			} else if ("F_LETZTE_WEBABFRAGE".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_LETZTEWEBABFRAGE];
			} else if ("F_BUYER_URL".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BUYERURL];
			} else if ("F_SUBREPORT_LIEFERANTEN".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_SUBREPORT_LIEFERANTEN];
			}

			else if ("GuenstigsterPreis1".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_GUENSTIGSTER_PREIS1];
			} else if ("GuenstigsterPreis2".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_GUENSTIGSTER_PREIS2];
			} else if ("GuenstigsterPreis3".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_GUENSTIGSTER_PREIS3];
			} else if ("GuenstigsterPreis4".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_GUENSTIGSTER_PREIS4];
			} else if ("GuenstigsterPreis5".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_GUENSTIGSTER_PREIS5];
			} else if ("SchnellsteLieferzeit".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_SCHNELLSTE_LIEFERZEIT];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_LAGERSTAND];
			} else if ("Reserviert".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_RESERVIERT];
			} else if ("Fehlmenge".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_FEHLMENGE];
			} else if ("Infertigung".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_INFERTIGUNG];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT];
			} else if ("Rahmenreserviert".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_RAHMENRESERVIERT];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_RAHMENBESTELLT];
			}

			else if ("BestelltPreis1".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT_PREIS1];
			} else if ("BestelltPreis2".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT_PREIS2];
			} else if ("BestelltPreis3".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT_PREIS3];
			} else if ("BestelltPreis4".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT_PREIS4];
			} else if ("BestelltPreis5".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT_PREIS5];
			}

			else if ("BestelltLZ1".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT1];
			} else if ("BestelltLZ2".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT2];
			} else if ("BestelltLZ3".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT3];
			} else if ("BestelltLZ4".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT4];
			} else if ("BestelltLZ5".equals(fieldName)) {
				value = data[index][REPORT_VERLGLEICH_BESTELLT_LIEFERZEIT5];
			}

		}
		return value;
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
}
