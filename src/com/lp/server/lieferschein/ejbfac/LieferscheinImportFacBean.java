package com.lp.server.lieferschein.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.ImportAmazonCsvDto;
import com.lp.server.auftrag.service.ImportShopifyCsvDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinImportFac;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.PartnerQuery;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.ejb.Landplzort;
import com.lp.server.system.ejb.Ort;
import com.lp.server.system.service.ImportRueckgabeDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.Helper;

@Stateless
public class LieferscheinImportFacBean extends Facade implements LieferscheinImportFac {

	
	public ImportRueckgabeDto importiereShopify_CSV(
			LinkedHashMap<String, ArrayList<ImportShopifyCsvDto>> hmNachBestellnummerGruppiert,
			Integer lagerIIdAbbuchungslager, boolean bImportiereWennKeinFehler, TheClientDto theClientDto) {

		Iterator it = hmNachBestellnummerGruppiert.keySet().iterator();

		ImportRueckgabeDto importRueckgabeDto = new ImportRueckgabeDto();

		HashMap<Integer, BigDecimal> hmLagerstaende = new HashMap<Integer, BigDecimal>();

		Integer artikelIIdVersandkosten = getVersandkostenArtikel(theClientDto);
		if (artikelIIdVersandkosten == null) {
			importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_ERROR,
					"Parameter 'VERSANDKOSTENARTIKEL' nicht definiert", 1);
			return importRueckgabeDto;
		}

		int iNachkommastellenPreis = getUINachkommastellenPreisVK(theClientDto.getMandant());

		while (it.hasNext()) {
			String bestellnummer = (String) it.next();

			ArrayList<ImportShopifyCsvDto> zeilen = hmNachBestellnummerGruppiert.get(bestellnummer);
			if (zeilen.size() > 0) {

				// Wenn es bereits einen Lieferschein mit der Bestellnummer gibt, dann
				// bestellnummer auslassen

				Session session3 = FLRSessionFactory.getFactory().openSession();
				String sQuery3 = "FROM FLRLieferschein ls WHERE ls.mandant_c_nr ='" + theClientDto.getMandant()
						+ "' AND ls.c_bestellnummer='" + bestellnummer + "'";
				org.hibernate.Query query3 = session3.createQuery(sQuery3);

				List<?> results3 = query3.list();
				Iterator<?> resultListIterator3 = results3.iterator();
				if (resultListIterator3.hasNext()) {
					FLRLieferschein ls = (FLRLieferschein) resultListIterator3.next();
					importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_INFO,
							"Zeile " + zeilen.get(0).zeile + " wurde ausgelassen, da es bereits Lieferschein '"
									+ ls.getC_nr() + "' zu Bestellnummmer '" + bestellnummer + "' gibt",
							zeilen.get(0).zeile);
					continue;
				}

				try {
					BigDecimal versandkosten = null;
					Integer lieferscheinIId = null;
					MwstsatzDto mwstsatzDto = null;
					for (int i = 0; i < zeilen.size(); i++) {

						ImportShopifyCsvDto positionDto = zeilen.get(i);

						java.util.Date dBelegdatum = null;
						String belegdatum = positionDto.belegdatum;
						try {

							DateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
							dBelegdatum = formatter.parse(belegdatum.substring(0, 10));
						} catch (ParseException e) {
							// Datum falsch
							importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_ERROR,
									"Datum aus Spalte 18 '" + belegdatum
											+ "' kann nicht konvertiert werden. Format muss 'yyyy-dd-MM' sein.",
									positionDto.zeile);

							continue;
						}

						
						if (i == 0 && bImportiereWennKeinFehler) {

							if (zeilen.get(0).lieferadresseLand != null) {
								LandDto landDto = getSystemFac().landFindByLkz(zeilen.get(0).lieferadresseLand);
								if (landDto == null) {
									importRueckgabeDto
											.addZeile(ImportRueckgabeDto.ART_ERROR,
													"Land '" + zeilen.get(0).lieferadresseLand
															+ "' konnte im System nicht gefunden werden",
													zeilen.get(0).zeile);
									continue;
								}
							}

							if (zeilen.get(0).waehrung != null) {
								WaehrungDto waehrungDto = getLocaleFac()
										.waehrungFindByPrimaryKeyWithNull(zeilen.get(0).waehrung);
								if (waehrungDto == null) {
									importRueckgabeDto
											.addZeile(ImportRueckgabeDto.ART_ERROR,
													"Waehrung '" + zeilen.get(0).waehrung
															+ "' konnte im System nicht gefunden werden",
													zeilen.get(0).zeile);
									continue;
								}
							}

							try {
								versandkosten = new BigDecimal(zeilen.get(0).versandkosten);
							} catch (NumberFormatException e) {
								importRueckgabeDto
										.addZeile(ImportRueckgabeDto.ART_ERROR,
												"Versandkosten aus Spalte Shipping '" + zeilen.get(0).versandkosten
														+ "' kann nicht in einen Wert konvertiert werden",
												zeilen.get(0).zeile);
								e.printStackTrace();
								break;
							}

							BigDecimal rabatt = BigDecimal.ZERO;

							try {
								rabatt = new BigDecimal(zeilen.get(0).rabatt);
							} catch (NumberFormatException e) {
								importRueckgabeDto
										.addZeile(ImportRueckgabeDto.ART_ERROR,
												"Rabatt aus Spalte Discount Amount '" + zeilen.get(0).rabatt
														+ "' kann nicht in einen Wert konvertiert werden",
												zeilen.get(0).zeile);
								e.printStackTrace();
								break;
							}

							BigDecimal gesamterBelegBetrag = BigDecimal.ZERO;

							try {
								rabatt = new BigDecimal(zeilen.get(0).gesamterbelegbetrag).negate();
							} catch (NumberFormatException e) {
								importRueckgabeDto
										.addZeile(ImportRueckgabeDto.ART_ERROR,
												"Rabatt aus Spalte Total '" + zeilen.get(0).gesamterbelegbetrag
														+ "' kann nicht in einen Wert konvertiert werden",
												zeilen.get(0).zeile);
								e.printStackTrace();
								break;
							}

							// Allg. Rabatt
							Double fAllgRabatt = 0D;
							if (rabatt.doubleValue() != 0 && gesamterBelegBetrag.doubleValue() != 0) {
								BigDecimal bdBetragInclRabatt = gesamterBelegBetrag.add(rabatt);

								fAllgRabatt = Helper.rundeKaufmaennisch(
										rabatt.divide(bdBetragInclRabatt, BigDecimal.ROUND_HALF_UP, 4)
												.multiply(new BigDecimal(100D)),
										2).doubleValue();

							}

							Integer kundeIIdRechnungsadresse = erstelleBzwHoleKundeRechnungsadresseAnhandEmail(null,
									positionDto.email, positionDto.rechnungsadresseName, null, null,
									positionDto.rechnungsadresseStrasse, positionDto.lieferadresseLand,
									positionDto.lieferadressePLZ, positionDto.lieferadresseOrt,null, false,false, theClientDto);

							KundeDto kundeDtoRechnungsadresse = getKundeFac()
									.kundeFindByPrimaryKey(kundeIIdRechnungsadresse, theClientDto);

							Integer kundeIIdLieferadresse = erstelleBzwHoleKundeLieferadresseadresseAnhandName(null,
									positionDto.lieferadresseName, null, null, positionDto.lieferadresseStrasse,
									positionDto.lieferadresseLand, positionDto.lieferadressePLZ,
									positionDto.lieferadresseOrt,false, theClientDto);

							// Lieferschein erzeugen
							LieferscheinDto lieferscheinDto = new LieferscheinDto();
							lieferscheinDto.setMandantCNr(theClientDto.getMandant());
							lieferscheinDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
							lieferscheinDto.setTBelegdatum(Helper.cutTimestamp(getTimestamp()));
							lieferscheinDto.setLieferscheinartCNr(LieferscheinFac.LSART_FREI);
							lieferscheinDto.setKundeIIdLieferadresse(kundeIIdLieferadresse);
							lieferscheinDto.setKundeIIdRechnungsadresse(kundeDtoRechnungsadresse.getIId());
							lieferscheinDto.setKostenstelleIId(kundeDtoRechnungsadresse.getKostenstelleIId());
							lieferscheinDto.setTLiefertermin(new java.sql.Timestamp(dBelegdatum.getTime()));
							lieferscheinDto.setCBestellnummer(bestellnummer);
							lieferscheinDto.setWaehrungCNr(kundeDtoRechnungsadresse.getCWaehrung());
							lieferscheinDto
									.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(getLocaleFac()
											.getWechselkurs2(theClientDto.getSMandantenwaehrung(),
													kundeDtoRechnungsadresse.getCWaehrung(), theClientDto)
											.doubleValue()));

							lieferscheinDto.setLieferartIId(kundeDtoRechnungsadresse.getLieferartIId());
							lieferscheinDto.setZahlungszielIId(kundeDtoRechnungsadresse.getZahlungszielIId());
							lieferscheinDto.setSpediteurIId(kundeDtoRechnungsadresse.getSpediteurIId());
							lieferscheinDto.setLagerIId(lagerIIdAbbuchungslager);
							lieferscheinDto.setPersonalIIdVertreter(theClientDto.getIDPersonal());
							lieferscheinDto.setFAllgemeinerRabattsatz(fAllgRabatt);

							lieferscheinIId = getLieferscheinFac().createLieferschein(lieferscheinDto, theClientDto);

							mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(
									kundeDtoRechnungsadresse.getMwstsatzbezIId(), lieferscheinDto.getTBelegdatum(),
									theClientDto);

						}

						String artikelnummer = positionDto.artikelnummer;

						ArtikelDto aDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(artikelnummer,
								theClientDto.getMandant());
						if (aDto == null) {
							importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_ERROR,
									"Artikel '" + artikelnummer
											+ "' aus Spalte Lineitem sku konnte nicht gefunden werden",
									positionDto.zeile);
							continue;
						}

						BigDecimal menge = null;
						try {
							menge = new BigDecimal(positionDto.menge);
						} catch (NumberFormatException e) {
							importRueckgabeDto.addZeile(
									ImportRueckgabeDto.ART_ERROR, "Menge aus Spalte Lineitem quantitiy '"
											+ positionDto.menge + "' kann nicht in einen Wert konvertiert werden",
									positionDto.zeile);
							e.printStackTrace();
							break;
						}

						BigDecimal bruttopreis = null;
						try {
							bruttopreis = new BigDecimal(positionDto.bruttopreis);
						} catch (NumberFormatException e) {
							importRueckgabeDto.addZeile(
									ImportRueckgabeDto.ART_ERROR, "Bruttopreis aus Spalte Lineitem price '"
											+ positionDto.bruttopreis + "' kann nicht in einen Wert konvertiert werden",
									positionDto.zeile);
							e.printStackTrace();
							break;
						}

						BigDecimal versandkostenRabatt = BigDecimal.ZERO;

						// Lagerstaende pruefen
						BigDecimal lagerstand = null;
						if (hmLagerstaende.containsKey(aDto.getIId())) {
							lagerstand = hmLagerstaende.get(aDto.getIId());
						} else {
							lagerstand = getLagerFac().getLagerstand(aDto.getIId(), lagerIIdAbbuchungslager,
									theClientDto);
						}

						lagerstand = lagerstand.subtract(menge);

						if (lagerstand.doubleValue() < 0) {
							importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_ERROR,
									"Lagerstand fuer Artikel '" + aDto.getCNr() + "' nicht ausreichend",
									positionDto.zeile);
							continue;
						}

						hmLagerstaende.put(aDto.getIId(), lagerstand);

						if (bImportiereWennKeinFehler) {
							LieferscheinpositionDto posDto = new LieferscheinpositionDto();
							posDto.setBelegIId(lieferscheinIId);
							posDto.setNMenge(menge);

							ArrayList<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
							if (aDto.istArtikelSnrOderchargentragend()) {

								SeriennrChargennrAufLagerDto[] snrchnrDtos = getLagerFac()
										.getAllSerienChargennrAufLagerInfoDtos(aDto.getIId(), lagerIIdAbbuchungslager,
												false, null, theClientDto);

								BigDecimal bdVerbleibendeMenge = menge;

								for (SeriennrChargennrAufLagerDto snrchnrDto : snrchnrDtos) {

									SeriennrChargennrMitMengeDto zeile = new SeriennrChargennrMitMengeDto();
									zeile.setCSeriennrChargennr(snrchnrDto.getCSeriennrChargennr());

									if (bdVerbleibendeMenge.doubleValue() > snrchnrDto.getNMenge().doubleValue()) {
										zeile.setNMenge(snrchnrDto.getNMenge());
									} else {
										zeile.setNMenge(bdVerbleibendeMenge);
									}
									alSeriennrChargennrMitMenge.add(zeile);

									bdVerbleibendeMenge = bdVerbleibendeMenge.subtract(zeile.getNMenge());

									if (bdVerbleibendeMenge.doubleValue() <= 0) {
										break;
									}
								}

							}
							posDto.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);
							posDto.setBNettopreisuebersteuert(Helper.boolean2Short(true));
							posDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

							posDto.setMwstsatzIId(mwstsatzDto.getIId());

							posDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);

							posDto.setArtikelIId(aDto.getIId());
							posDto.setEinheitCNr(aDto.getEinheitCNr());

							posDto = preiseBerechnen(mwstsatzDto, bruttopreis, BigDecimal.ZERO, posDto,
									iNachkommastellenPreis);

							getLieferscheinpositionFac().createLieferscheinposition(posDto, false, theClientDto);

							// Versandkosten
							if (i == zeilen.size() - 1) {
								if (versandkosten.subtract(versandkostenRabatt).doubleValue() > 0) {

									LieferscheinpositionDto posDtoVersandkosten = new LieferscheinpositionDto();
									posDtoVersandkosten.setBelegIId(lieferscheinIId);
									posDtoVersandkosten.setNMenge(BigDecimal.ONE);
									posDtoVersandkosten.setBNettopreisuebersteuert(Helper.boolean2Short(true));
									posDtoVersandkosten.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

									posDtoVersandkosten.setMwstsatzIId(mwstsatzDto.getIId());

									posDtoVersandkosten.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);

									posDtoVersandkosten.setArtikelIId(artikelIIdVersandkosten);
									posDtoVersandkosten.setEinheitCNr(aDto.getEinheitCNr());

									posDtoVersandkosten = preiseBerechnen(mwstsatzDto, versandkosten, BigDecimal.ZERO,
											posDtoVersandkosten, iNachkommastellenPreis);

									getLieferscheinpositionFac().createLieferscheinposition(posDtoVersandkosten, false,
											theClientDto);
								}
							}

						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
		}
		return importRueckgabeDto;
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(1000)
	public ImportRueckgabeDto importiereAmazon_CSV(
			LinkedHashMap<String, ArrayList<ImportAmazonCsvDto>> hmNachBestellnummerGruppiert,
			Integer lagerIIdAbbuchungslager, boolean bImportiereWennKeinFehler, TheClientDto theClientDto) {

		Iterator it = hmNachBestellnummerGruppiert.keySet().iterator();

		ImportRueckgabeDto importRueckgabeDto = new ImportRueckgabeDto();

		HashMap<Integer, BigDecimal> hmLagerstaende = new HashMap<Integer, BigDecimal>();

		Integer artikelIIdVersandkosten = getVersandkostenArtikel(theClientDto);
		if (artikelIIdVersandkosten == null) {
			importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_ERROR,
					"Parameter 'VERSANDKOSTENARTIKEL' nicht definiert", 1);
			return importRueckgabeDto;
		}

		int iNachkommastellenPreis = getUINachkommastellenPreisVK(theClientDto.getMandant());

		while (it.hasNext()) {
			String bestellnummer = (String) it.next();

			ArrayList<ImportAmazonCsvDto> zeilen = hmNachBestellnummerGruppiert.get(bestellnummer);
			if (zeilen.size() > 0) {

				try {

					for (int i = 0; i < zeilen.size(); i++) {

						ImportAmazonCsvDto positionDto = zeilen.get(i);

						java.util.Date dBelegdatum = null;
						String belegdatum = positionDto.belegdatum;
						try {

							DateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
							dBelegdatum = formatter.parse(belegdatum.substring(0, 10));
						} catch (ParseException e) {
							// Datum falsch
							importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_ERROR,
									"Datum aus Spalte 18 '" + belegdatum
											+ "' kann nicht konvertiert werden. Format muss 'yyyy-dd-MM' sein.",
									positionDto.zeile);

							continue;
						}

						if (positionDto.trackingNumber == null) {
							importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_INFO,
									"Zeile " + positionDto.zeile + " wurde ausgelassen, da keine 'tracking-number'",
									positionDto.zeile);
							continue;
						}

						// Wenn es bereits einen Lieferschein mit der Bestellnummer gibt, dann
						// bestellnummer auslassen

						Session session3 = FLRSessionFactory.getFactory().openSession();
						String sQuery3 = "FROM FLRLieferschein ls WHERE ls.mandant_c_nr ='" + theClientDto.getMandant()
								+ "' AND ls.c_bestellnummer='" + bestellnummer + "'";
						org.hibernate.Query query3 = session3.createQuery(sQuery3);

						List<?> results3 = query3.list();
						Iterator<?> resultListIterator3 = results3.iterator();
						if (resultListIterator3.hasNext()) {
							FLRLieferschein ls = (FLRLieferschein) resultListIterator3.next();
							importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_INFO,
									"Zeile " + positionDto.zeile + " wurde ausgelassen, da es bereits Lieferschein '"
											+ ls.getC_nr() + "' zu Bestellnummmer '" + bestellnummer + "' gibt",
									positionDto.zeile);
							continue;
						}

						if (positionDto.lieferadresseLand != null) {
							LandDto landDto = getSystemFac().landFindByLkz(positionDto.lieferadresseLand);
							if (landDto == null) {
								importRueckgabeDto
										.addZeile(ImportRueckgabeDto.ART_ERROR,
												"Land '" + positionDto.lieferadresseLand
														+ "' konnte im System nicht gefunden werden",
												positionDto.zeile);
								continue;
							}
						}

						if (positionDto.waehrung != null) {
							WaehrungDto waehrungDto = getLocaleFac()
									.waehrungFindByPrimaryKeyWithNull(positionDto.waehrung);
							if (waehrungDto == null) {
								importRueckgabeDto
										.addZeile(ImportRueckgabeDto.ART_ERROR,
												"Waehrung '" + positionDto.waehrung
														+ "' konnte im System nicht gefunden werden",
												positionDto.zeile);
								continue;
							}
						}

						Integer lieferscheinIId = null;
						MwstsatzDto mwstsatzDto = null;
						if (i == 0 && bImportiereWennKeinFehler) {

							String rechnungsadresse_cName2 = null;
							String rechnungsadresse_cStrasse = null;
							if (positionDto.rechnungsadresseAdresse2 == null) {
								rechnungsadresse_cStrasse = positionDto.rechnungsadresseAdresse1;
							} else {
								rechnungsadresse_cName2 = positionDto.rechnungsadresseAdresse1;
								rechnungsadresse_cStrasse = positionDto.rechnungsadresseAdresse2;
							}

							Integer kundeIIdRechnungsadresse = erstelleBzwHoleKundeRechnungsadresseAnhandEmail(null,
									positionDto.email, positionDto.recipientName, positionDto.rechnungsadresseName,
									rechnungsadresse_cName2, rechnungsadresse_cStrasse,
									positionDto.rechnungsadresseLand, positionDto.rechnungsadressePLZ,
									positionDto.rechnungsadresseOrt,null, false,false, theClientDto);

							KundeDto kundeDtoRechnungsadresse = getKundeFac()
									.kundeFindByPrimaryKey(kundeIIdRechnungsadresse, theClientDto);

							String lieferadresse_cName2 = null;
							String lieferadresse_cStrasse = null;
							if (positionDto.lieferadresseAdresse2 == null) {
								lieferadresse_cStrasse = positionDto.lieferadresseAdresse1;
							} else {
								lieferadresse_cName2 = positionDto.lieferadresseAdresse1;
								lieferadresse_cStrasse = positionDto.lieferadresseAdresse2;
							}

							Integer kundeIIdLieferadresse = erstelleBzwHoleKundeLieferadresseadresseAnhandName(null,
									positionDto.recipientName, positionDto.lieferadresseName, lieferadresse_cName2,
									lieferadresse_cStrasse, positionDto.lieferadresseLand, positionDto.lieferadressePLZ,
									positionDto.lieferadresseOrt,false, theClientDto);

							// Lieferschein erzeugen
							LieferscheinDto lieferscheinDto = new LieferscheinDto();
							lieferscheinDto.setMandantCNr(theClientDto.getMandant());
							lieferscheinDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
							lieferscheinDto.setTBelegdatum(Helper.cutTimestamp(getTimestamp()));
							lieferscheinDto.setLieferscheinartCNr(LieferscheinFac.LSART_FREI);
							lieferscheinDto.setKundeIIdLieferadresse(kundeIIdLieferadresse);
							lieferscheinDto.setKundeIIdRechnungsadresse(kundeDtoRechnungsadresse.getIId());
							lieferscheinDto.setKostenstelleIId(kundeDtoRechnungsadresse.getKostenstelleIId());
							lieferscheinDto.setTLiefertermin(new java.sql.Timestamp(dBelegdatum.getTime()));
							lieferscheinDto.setCBestellnummer(bestellnummer);
							lieferscheinDto.setWaehrungCNr(kundeDtoRechnungsadresse.getCWaehrung());
							lieferscheinDto
									.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(getLocaleFac()
											.getWechselkurs2(theClientDto.getSMandantenwaehrung(),
													kundeDtoRechnungsadresse.getCWaehrung(), theClientDto)
											.doubleValue()));

							lieferscheinDto.setLieferartIId(kundeDtoRechnungsadresse.getLieferartIId());
							lieferscheinDto.setZahlungszielIId(kundeDtoRechnungsadresse.getZahlungszielIId());
							lieferscheinDto.setSpediteurIId(kundeDtoRechnungsadresse.getSpediteurIId());
							lieferscheinDto.setLagerIId(lagerIIdAbbuchungslager);
							lieferscheinDto.setPersonalIIdVertreter(theClientDto.getIDPersonal());
							lieferscheinDto.setCVersandnummer(positionDto.trackingNumber);

							lieferscheinIId = getLieferscheinFac().createLieferschein(lieferscheinDto, theClientDto);

							mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(
									kundeDtoRechnungsadresse.getMwstsatzbezIId(), lieferscheinDto.getTBelegdatum(),
									theClientDto);

						}

						String artikelnummer = positionDto.artikelnummer;

						ArtikelDto aDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(artikelnummer,
								theClientDto.getMandant());
						if (aDto == null) {
							importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_ERROR,
									"Artikel '" + artikelnummer + "' konnte nicht gefunden werden", positionDto.zeile);
							continue;
						}

						BigDecimal menge = null;
						try {
							menge = new BigDecimal(positionDto.menge);
						} catch (NumberFormatException e) {
							importRueckgabeDto
									.addZeile(ImportRueckgabeDto.ART_ERROR,
											"Menge aus Spalte 19 '" + positionDto.menge
													+ "' kann nicht in einen Wert konvertiert werden",
											positionDto.zeile);
							e.printStackTrace();
							break;
						}

						BigDecimal bruttopreis = null;
						try {
							bruttopreis = new BigDecimal(positionDto.bruttopreis);
						} catch (NumberFormatException e) {
							importRueckgabeDto.addZeile(
									ImportRueckgabeDto.ART_ERROR, "Bruttopreis aus Spalte item-price '"
											+ positionDto.bruttopreis + "' kann nicht in einen Wert konvertiert werden",
									positionDto.zeile);
							e.printStackTrace();
							break;
						}

						BigDecimal artikelRabatt = null;
						try {
							artikelRabatt = new BigDecimal(positionDto.rabatt).negate();
						} catch (NumberFormatException e) {
							importRueckgabeDto.addZeile(
									ImportRueckgabeDto.ART_ERROR, "Rabatt aus Spalte item-promotion-discount '"
											+ positionDto.rabatt + "' kann nicht in einen Wert konvertiert werden",
									positionDto.zeile);
							e.printStackTrace();
							break;
						}

						BigDecimal versandkosten = null;
						try {
							versandkosten = new BigDecimal(positionDto.versandkosten);
						} catch (NumberFormatException e) {
							importRueckgabeDto
									.addZeile(ImportRueckgabeDto.ART_ERROR,
											"Versandkosten aus Spalte shipping-price '" + positionDto.versandkosten
													+ "' kann nicht in einen Wert konvertiert werden",
											positionDto.zeile);
							e.printStackTrace();
							break;
						}

						BigDecimal versandkostenRabatt = null;
						try {
							versandkostenRabatt = new BigDecimal(positionDto.versandrabatt).negate();
						} catch (NumberFormatException e) {
							importRueckgabeDto
									.addZeile(ImportRueckgabeDto.ART_ERROR,
											"Rabatt aus Spalte ship-promotion-discount '" + positionDto.versandrabatt
													+ "' kann nicht in einen Wert konvertiert werden",
											positionDto.zeile);
							e.printStackTrace();
							break;
						}

						// Lagerstaende pruefen
						BigDecimal lagerstand = null;
						if (hmLagerstaende.containsKey(aDto.getIId())) {
							lagerstand = hmLagerstaende.get(aDto.getIId());
						} else {
							lagerstand = getLagerFac().getLagerstand(aDto.getIId(), lagerIIdAbbuchungslager,
									theClientDto);
						}

						lagerstand = lagerstand.subtract(menge);

						if (lagerstand.doubleValue() < 0) {
							importRueckgabeDto.addZeile(ImportRueckgabeDto.ART_ERROR,
									"Lagerstand fuer Artikel '" + aDto.getCNr() + "' nicht ausreichend",
									positionDto.zeile);
							continue;
						}

						hmLagerstaende.put(aDto.getIId(), lagerstand);

						if (bImportiereWennKeinFehler) {
							LieferscheinpositionDto posDto = new LieferscheinpositionDto();
							posDto.setBelegIId(lieferscheinIId);
							posDto.setNMenge(menge);

							ArrayList<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
							if (aDto.istArtikelSnrOderchargentragend()) {

								SeriennrChargennrAufLagerDto[] snrchnrDtos = getLagerFac()
										.getAllSerienChargennrAufLagerInfoDtos(aDto.getIId(), lagerIIdAbbuchungslager,
												false, null, theClientDto);

								BigDecimal bdVerbleibendeMenge = menge;

								for (SeriennrChargennrAufLagerDto snrchnrDto : snrchnrDtos) {

									SeriennrChargennrMitMengeDto zeile = new SeriennrChargennrMitMengeDto();
									zeile.setCSeriennrChargennr(snrchnrDto.getCSeriennrChargennr());

									if (bdVerbleibendeMenge.doubleValue() > snrchnrDto.getNMenge().doubleValue()) {
										zeile.setNMenge(snrchnrDto.getNMenge());
									} else {
										zeile.setNMenge(bdVerbleibendeMenge);
									}
									alSeriennrChargennrMitMenge.add(zeile);

									bdVerbleibendeMenge = bdVerbleibendeMenge.subtract(zeile.getNMenge());

									if (bdVerbleibendeMenge.doubleValue() <= 0) {
										break;
									}
								}

							}
							posDto.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);
							posDto.setBNettopreisuebersteuert(Helper.boolean2Short(true));
							posDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

							posDto.setMwstsatzIId(mwstsatzDto.getIId());

							posDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);

							posDto.setArtikelIId(aDto.getIId());
							posDto.setEinheitCNr(aDto.getEinheitCNr());

							posDto = preiseBerechnen(mwstsatzDto, bruttopreis, artikelRabatt, posDto,
									iNachkommastellenPreis);

							getLieferscheinpositionFac().createLieferscheinposition(posDto, false, theClientDto);

							// Versandkosten
							if (versandkosten.subtract(versandkostenRabatt).doubleValue() > 0) {

								LieferscheinpositionDto posDtoVersandkosten = new LieferscheinpositionDto();
								posDtoVersandkosten.setBelegIId(lieferscheinIId);
								posDtoVersandkosten.setNMenge(BigDecimal.ONE);
								posDtoVersandkosten.setBNettopreisuebersteuert(Helper.boolean2Short(true));
								posDtoVersandkosten.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

								posDtoVersandkosten.setMwstsatzIId(mwstsatzDto.getIId());

								posDtoVersandkosten.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);

								posDtoVersandkosten.setArtikelIId(artikelIIdVersandkosten);
								posDtoVersandkosten.setEinheitCNr(aDto.getEinheitCNr());

								posDtoVersandkosten = preiseBerechnen(mwstsatzDto, versandkosten, versandkostenRabatt,
										posDtoVersandkosten, iNachkommastellenPreis);

								getLieferscheinpositionFac().createLieferscheinposition(posDtoVersandkosten, false,
										theClientDto);
							}

						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
		}
		return importRueckgabeDto;
	}

	private LieferscheinpositionDto preiseBerechnen(MwstsatzDto mwstsatzDto, BigDecimal bruttopreis,
			BigDecimal artikelRabatt, LieferscheinpositionDto posDto, int nachkommastellen) {
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
}
