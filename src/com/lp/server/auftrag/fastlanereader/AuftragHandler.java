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
package com.lp.server.auftrag.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFLRDataDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragHandlerFeature;
import com.lp.server.auftrag.service.AuftragQueryResult;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.IAuftragFLRData;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.AddressContactDto;
import com.lp.server.partner.service.AdresseDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.IAddressContact;
import com.lp.server.partner.service.IAdresse;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAuftrag;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.FlrFirmaAnsprechpartnerFilterBuilder;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer den Auftrag implementiert. Pro UseCase
 * gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-08-14
 * </p>
 * <p>
 * </p>
 * 
 * @author martin werner, uli walch
 * @version 1.0
 */

public class AuftragHandler extends UseCaseHandler {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_AUFTRAG = "flrauftrag.";
	public static final String FLR_AUFTRAG_FROM_CLAUSE = " from FLRAuftrag flrauftrag ";
	public static final String FLR_AUFTRAG_TEXTSUCHE_CLASSNAME = FLRAuftragtextsuche.class.getSimpleName();
	Integer iAnlegerStattVertreterAnzeigen = 0;

	boolean verrechenbarStattRohsAnzeigen = false;
	boolean bAuftragsfreigabe = false;
	boolean bProjekttitelInAG_AB = false;
	boolean bAbbuchungslagerAnzeigen = false;
	boolean bZweiterVertreter = false;
	boolean bAuftragspositionstermin_in_auswahl_anzeigen = false;

	private Feature cachedFeature = null;
	private Integer filterPartnerId = null;

	private Integer filterPartnerIdOderVertreter = null;

	private class Feature {
		private boolean featureAdresse = false;
		private boolean featureAdresseAnschrift = false;
		private boolean featureAdresseKurzanschrift = false;
		private List<IAddressContact> addresses = new ArrayList<IAddressContact>();
		private boolean featureUseLieferadresse = false;
		private int flrRowCount = 0;
		private IAuftragFLRData[] flrData = null;
		private boolean featureKundenIIds = false;

		public Feature() {
			if (getQuery() instanceof QueryParametersFeatures) {
				QueryParametersFeatures qpf = (QueryParametersFeatures) getQuery();
				featureAdresse = qpf.hasFeature(AuftragHandlerFeature.ADRESSE_KOMPLETT);
				featureAdresseAnschrift = qpf.hasFeature(AuftragHandlerFeature.ADRESSE_ANSCHRIFT);
				featureAdresseKurzanschrift = qpf.hasFeature(AuftragHandlerFeature.ADRESSE_KURZANSCHRIFT);
				featureUseLieferadresse = qpf.hasFeature(AuftragHandlerFeature.ADRESSE_IST_LIEFERADRESSE);
				featureKundenIIds = qpf.hasFeature(AuftragHandlerFeature.KUNDEN_IIDS);
			}
		}

		public void setFlrRowCount(int rows) {
			flrRowCount = rows;
			flrData = new AuftragFLRDataDto[flrRowCount];
		}

		public IAuftragFLRData[] getFlrData() {
			return flrData;
		}

		public boolean hasAdresse() {
			return featureAdresse;
		}

		public boolean hasAdresseAnschrift() {
			return featureAdresseAnschrift;
		}

		public boolean hasAdresseKurzanschrift() {
			return featureAdresseKurzanschrift;
		}

		public boolean useLieferadresse() {
			return featureUseLieferadresse;
		}

		public boolean hasKundenIIds() {
			return featureKundenIIds;
		}

		public boolean hasFeature() {
			return hasAdresse() || hasKundenIIds();
		}

		public void buildAddress(int row, FLRAuftrag auftrag) throws RemoteException {
			if (!hasAdresse())
				return;

			FLRPartner flrpartner = useLieferadresse() ? auftrag.getFlrlieferkunde().getFlrpartner()
					: auftrag.getFlrkunde().getFlrpartner();

			Integer contactId = useLieferadresse() ? auftrag.getAnsprechpartner_i_id_lieferadresse()
					: auftrag.getAnsprechpartner_i_id_kunde();

			IAdresse address = new AdresseDto();
			address.setPartnerId(flrpartner.getI_id());

			FLRLandplzort flranschrift = flrpartner.getFlrlandplzort();
			if (flranschrift != null) {
				if (flranschrift.getFlrland() != null) {
					address.setCountryCode(flranschrift.getFlrland().getC_lkz());
				}
				address.setZipcode(flranschrift.getC_plz());
				if (flranschrift.getFlrort() != null) {
					address.setCity(flranschrift.getFlrort().getC_name());
				}
			}

			address.setName1Lastname(flrpartner.getC_name1nachnamefirmazeile1());
			address.setName2Firstname(flrpartner.getC_name2vornamefirmazeile2());
			address.setName3Department(flrpartner.getC_name3vorname2abteilung());
			address.setTitel(flrpartner.getC_titel());
			address.setTitelSuffix(flrpartner.getC_ntitel());
			address.setSalutation(flrpartner.getAnrede_c_nr());
			address.setStreet(flrpartner.getC_strasse());
			address.setEmail(flrpartner.getC_email());
			address.setPhone(flrpartner.getC_telefon());

			IAdresse contactAdresse = null;
			AnsprechpartnerDto anspDto = contactId == null ? null
					: getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(contactId, theClientDto);
			if (anspDto != null) {
				contactAdresse = buildAdresse(anspDto, address);
			}

			if (hasAdresseAnschrift()) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(flrpartner.getI_id(), theClientDto);

				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);
				Locale locale = theClientDto.getLocUi();
				String s = formatAdresseFuerAusdruck(partnerDto, anspDto, mandantDto, locale,
						LocaleFac.BELEGART_AUFTRAG);
				address.setLines(s.split("\n"));
			}

			if (hasAdresseKurzanschrift()) {
			}

			IAddressContact addressContact = new AddressContactDto(address, contactAdresse);
			setAddressContact(row, addressContact);
			// addresses.add(addressContact) ;
		}

		private IAdresse buildAdresse(AnsprechpartnerDto anspDto, IAdresse companyAddress) {
			IAdresse address = new AdresseDto();
			PartnerDto partnerDto = anspDto.getPartnerDto();
			address.setPartnerId(partnerDto.getIId());

			address.setCountryCode(companyAddress.getCountryCode());
			address.setZipcode(companyAddress.getZipcode());
			address.setCity(companyAddress.getCity());
			address.setStreet(companyAddress.getStreet());

			address.setName1Lastname(partnerDto.getCName1nachnamefirmazeile1());
			address.setName2Firstname(partnerDto.getCName2vornamefirmazeile2());
			address.setName3Department(partnerDto.getCName3vorname2abteilung());
			address.setTitel(partnerDto.getCTitel());
			address.setTitelSuffix(partnerDto.getCNtitel());
			address.setSalutation(partnerDto.getAnredeCNr());
			address.setEmail(anspDto.getCEmail());
			address.setPhone(anspDto.getCTelefon());

			return address;
		}

		public List<IAddressContact> getAddresses() {
			return addresses;
		}

		private IAuftragFLRData getFlrDataObject(int row) {
			if (flrData[row] == null) {
				flrData[row] = new AuftragFLRDataDto();
			}
			return flrData[row];
		}

		public void setInternerKommentar(int row, String kommentar) {
			getFlrDataObject(row).setInternerKommentar(kommentar != null);
			getFlrDataObject(row).setInternerKommentarText(kommentar);
		}

		public Boolean getInternerKommentar(int row) {
			return getFlrDataObject(row).hasInternerKommentar();
		}

		public void setExternerKommentar(int row, String kommentar) {
			getFlrDataObject(row).setExternerKommentar(kommentar != null);
			getFlrDataObject(row).setExternerKommentarText(kommentar);
		}

		public Boolean getExternerKommentar(int row) {
			return getFlrDataObject(row).hasExternerKommentar();
		}

		public void setAddressContact(int row, IAddressContact addressContact) {
			getFlrDataObject(row).setAddressContact(addressContact);
		}

		public IAddressContact getAddressContact(int row) {
			return getFlrDataObject(row).getAddressContact();
		}

		public void setKundenIIds(int row, FLRAuftrag auftrag) {
			if (!hasKundenIIds()) {
				return;
			}
			getFlrDataObject(row).setKundeIIdAuftragsadresse(auftrag.getKunde_i_id_auftragsadresse());
			getFlrDataObject(row).setKundeIIdLieferadresse(auftrag.getKunde_i_id_lieferadresse());
			getFlrDataObject(row).setKundeIIdRechnungsadresse(auftrag.getKunde_i_id_rechnungsadresse());
		}

		public void setVertreter(int row, FLRAuftrag auftrag) {
			if (auftrag.getFlrvertreter() == null) {
				return;
			}

			getFlrDataObject(row).setVertreterKurzzeichen(auftrag.getFlrvertreter().getC_kurzzeichen());
		}
		
		public void setStatusCnr(int row, FLRAuftrag auftrag) {
			getFlrDataObject(row).setStatusCnr(auftrag.getAuftragstatus_c_nr());
		}
	}

	private Feature getFeature() {
		if (cachedFeature == null) {
			cachedFeature = new Feature();
		}
		return cachedFeature;
	}

	private class AuftragKundeAnsprechpartnerFilterBuilder extends FlrFirmaAnsprechpartnerFilterBuilder {

		public AuftragKundeAnsprechpartnerFilterBuilder(boolean bSuchenInklusiveKBez) {
			super(bSuchenInklusiveKBez);
		}

		@Override
		public String getFlrPartner() {
			return FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_FLRKUNDE + "." + LieferantFac.FLR_PARTNER;
		}

		@Override
		public String getFlrPropertyAnsprechpartnerIId() {
			return FLR_AUFTRAG + "ansprechpartner_i_id_kunde";
		}

	}

	/**
	 * gets the data page for the specified row using the current query. The row at
	 * rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex diese Zeile soll selektiert sein
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP Ausnahme
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = getFromClause() + buildWhereClause() + buildOrderByClause();

			if (filterPartnerId != null) {
				session.enableFilter("filterPartner").setParameter("paramPartnerId", filterPartnerId);
			}
			if (filterPartnerIdOderVertreter != null) {
				session.enableFilter("filterPartner").setParameter("paramPartnerId", filterPartnerIdOderVertreter);
			}

			// logQuery(queryString);
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
					theClientDto);

			getFeature().setFlrRowCount(rows.length);

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();
				FLRAuftrag auftrag = (FLRAuftrag) o[0];

				java.util.Date dNaechsterTerminAusPosition = (java.util.Date) o[2];

				long iAnzahlER = o[1] != null ? (Long) o[1] : 0l;
				boolean hasERs = iAnzahlER > 0;

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = auftrag.getI_id();

				rows[row][col++] = auftrag.getI_id();

				// Kuerzel fuer die Auftragart
				String auftragart = null;

				if (auftrag.getAuftragart_c_nr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
					auftragart = AuftragServiceFac.AUFTRAGART_ABRUF_SHORT;
				} else if (auftrag.getAuftragart_c_nr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					auftragart = AuftragServiceFac.AUFTRAGART_RAHMEN_SHORT;
				} else if (auftrag.getAuftragart_c_nr().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
					auftragart = AuftragServiceFac.AUFTRAGART_WIEDERHOLEND_SHORT;
				} else if (auftrag.getBestellung_i_id_anderermandant() != null) {
					auftragart = "M";
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("art")] = auftragart;

				rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.auftragsnummer")] = auftrag.getC_nr();

				

				//PJ22392
				//if (auftrag.getFlrkunde() != null) {
				//	rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kunde")] = getPartnerName(
				//			auftrag.getFlrkunde().getFlrpartner());
				//}
				
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kunde")] = auftrag.getFlrkunde() == null
						? null
						: auftrag.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();
				
				
				// IMS 1757 die Anschrift des Kunden anzeigen
				String cAnschrift = null; // eine Liefergruppenanfrage hat
				// keinen Lieferanten

				if (auftrag.getFlrkunde() != null) {
					FLRLandplzort flranschrift = auftrag.getFlrkunde().getFlrpartner().getFlrlandplzort();

					if (flranschrift != null) {
						cAnschrift = flranschrift.getFlrland().getC_lkz() + "-" + flranschrift.getC_plz() + " "
								+ flranschrift.getFlrort().getC_name();
					}

					getFeature().buildAddress(row, auftrag);
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ort")] = cAnschrift;
				String proj_bestellnummer = "";

				if (bProjekttitelInAG_AB) {

					if (auftrag.getFlrprojekt() != null) {
						proj_bestellnummer = auftrag.getFlrprojekt().getC_titel() + " | ";
					} else if (auftrag.getFlrangebot() != null && auftrag.getFlrangebot().getFlrprojekt() != null) {
						proj_bestellnummer = auftrag.getFlrangebot().getFlrprojekt().getC_titel() + " | ";
					}

				}

				if (auftrag.getC_bez() != null) {
					proj_bestellnummer += auftrag.getC_bez();
				}

				if (auftrag.getC_bestellnummer() != null) {
					proj_bestellnummer += " | " + auftrag.getC_bestellnummer();
				}

				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("auft.projektbestellnummer")] = proj_bestellnummer;

				if (bAuftragspositionstermin_in_auswahl_anzeigen) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("lp.termin")] = dNaechsterTerminAusPosition;
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.termin")] = auftrag
							.getT_liefertermin();
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.datum")] = auftrag.getT_belegdatum();

				if (iAnlegerStattVertreterAnzeigen == 1) {
					if (auftrag.getFlrpersonalanleger() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter")] = auftrag
								.getFlrpersonalanleger().getC_kurzzeichen();
					}
				} else if (iAnlegerStattVertreterAnzeigen == 2) {
					if (auftrag.getFlrpersonalaenderer() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter")] = auftrag
								.getFlrpersonalaenderer().getC_kurzzeichen();
					}
				} else {
					if (auftrag.getFlrvertreter() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter")] = auftrag
								.getFlrvertreter().getC_kurzzeichen();
					}
				}

				if (bZweiterVertreter) {
					if (auftrag.getFlrvertreter2() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter2")] = auftrag
								.getFlrvertreter2().getC_kurzzeichen();
					}
				}

				String sStatus = auftrag.getAuftragstatus_c_nr();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = getStatusMitUebersetzung(
						sStatus, auftrag.getT_versandzeitpunkt(), auftrag.getC_versandtype());

				BigDecimal nGesamtwertAuftragInAuftragswaehrung = new BigDecimal(0);

				if (auftrag.getN_gesamtauftragswertinauftragswaehrung() != null
						&& !auftrag.getAuftragstatus_c_nr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
					nGesamtwertAuftragInAuftragswaehrung = auftrag.getN_gesamtauftragswertinauftragswaehrung();
				}

				if (bDarfPreiseSehen) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("lp.wert")] = nGesamtwertAuftragInAuftragswaehrung;
				}
				rowToAddCandidate[getTableColumnInformation().getViewIndex("waehrung")] = auftrag
						.getWaehrung_c_nr_auftragswaehrung();

				if (verrechenbarStattRohsAnzeigen) {
					if (auftrag.getT_verrechenbar() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.verrechenbar")] = Boolean.TRUE;
					} else {
						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("auft.verrechenbar")] = Boolean.FALSE;
					}

				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("detail.label.rohs")] = Helper
							.short2Boolean(auftrag.getB_rohs());
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("detail.label.unverbindlich")] = Helper
						.short2Boolean(auftrag.getB_lieferterminunverbindlich());

				if (bAuftragsfreigabe) {
					if (auftrag.getT_auftragsfreigabe() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.freigabe")] = Boolean.TRUE;
					}
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kommentar")] = getKommentarart(
						auftrag.getX_internerkommentar(), auftrag.getX_externerkommentar());

				if (bAbbuchungslagerAnzeigen) {
					if (auftrag.getFlrlager() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.abbuchungslager")] = auftrag
								.getFlrlager().getC_nr();
					}
				}

				if (auftrag.getB_poenale().equals(new Short((short) 1))) {
					// rows[row][col++] = iAnzahlER == 0 ? Color.RED :
					// Color.MAGENTA ;
					rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = hasERs ? Color.MAGENTA
							: Color.RED;
				} else {
					// rows[row][col++] = iAnzahlER == 0 ? null : Color.BLUE ;
					rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = hasERs ? Color.BLUE : null;
				}

				// PJ19958
				if (rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] == null) {
					if (Helper.short2boolean(auftrag.getFlrverrechenbar().getB_verrechenbar()) == false) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.LIGHT_GRAY;
					}
				}

				rows[row] = rowToAddCandidate;
				getFeature().setInternerKommentar(row, auftrag.getX_internerkommentar());
				getFeature().setExternerKommentar(row, auftrag.getX_externerkommentar());
				getFeature().setKundenIIds(row, auftrag);
				getFeature().setVertreter(row, auftrag);
				getFeature().setStatusCnr(row, auftrag);

				++row;
				col = 0;
			}

			if (getFeature().hasFeature()) {
				AuftragQueryResult auftragResult = new AuftragQueryResult(rows, this.getRowCount(), startIndex,
						endIndex, 0);
				// auftragResult.setAddresses(getFeature().getAddresses()) ;
				auftragResult.setFlrData(getFeature().getFlrData());
				result = auftragResult;
			} else {
				result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return result;
	}

	/**
	 * gets the total number of rows represented by the current query.
	 * 
	 * @return int die Anzehl der Zeilen im Ergebnis
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 */
	protected long getRowCountFromDataBase() {
		String queryString = "SELECT  COUNT(*) from FLRAuftrag  as flrauftrag "
				+ " left join flrauftrag.flrprojekt as flrprojekt "
				+ "left join flrauftrag.flrangebot.flrprojekt as ag_flrprojekt " + buildWhereClause();

		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);

			if (filterPartnerIdOderVertreter != null) {
				session.enableFilter("filterPartner").setParameter("paramPartnerId", filterPartnerIdOderVertreter);
			}

			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			sessionClose(session);
		}

		return rowCount;

	}

	private boolean isAdresseKunde(FilterKriterium filterKriterium) {
		return filterKriterium.kritName.equals(AuftragFac.FLR_AUFTRAG_FLRKUNDE + "." + LieferantFac.FLR_PARTNER + "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
	}

	private boolean isAdresseLieferadresse(FilterKriterium filterKriterium) {
		return filterKriterium.kritName.equals(AuftragFac.FLR_AUFTRAG_FLRKUNDELIEFERADRESSE + "."
				+ LieferantFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
	}

	private String buildAdresseFilterImpl(String adresseTyp, FilterKriterium filterKriterium) {
		StringBuffer where = new StringBuffer();
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		if (parameter.asBoolean()) {
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" ( upper(" + FLR_AUFTRAG + filterKriterium.kritName + ")");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value.toUpperCase());
				where.append(" OR upper(" + FLR_AUFTRAG + adresseTyp + ".flrpartner.c_kbez" + ")");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value.toUpperCase() + ") ");
			} else {
				where.append(" " + FLR_AUFTRAG + filterKriterium.kritName);
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value);
				where.append("OR " + FLR_AUFTRAG + adresseTyp + ".flrpartner.c_kbez");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value);
			}
		} else {
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" upper(" + FLR_AUFTRAG + filterKriterium.kritName + ")");
			} else {
				where.append(" " + FLR_AUFTRAG + filterKriterium.kritName);
			}

			where.append(" " + filterKriterium.operator);

			if (filterKriterium.isBIgnoreCase()) {
				where.append(" " + filterKriterium.value.toUpperCase());
			} else {
				where.append(" " + filterKriterium.value);
			}
		}
		return where.toString();
	}

	private String buildAdresseFilterKunde(FilterKriterium filterKriterium) {
		AuftragKundeAnsprechpartnerFilterBuilder filterBuilder = new AuftragKundeAnsprechpartnerFilterBuilder(
				getParameterFac().getSuchenInklusiveKBez(theClientDto.getMandant()));
		return filterBuilder.buildFirmaAnsprechpartnerFilter(filterKriterium);
		// return buildAdresseFilterImpl(AuftragFac.FLR_AUFTRAG_FLRKUNDE,
		// filterKriterium) ;
	}

	private String buildAdresseFilterLieferadresse(FilterKriterium filterKriterium) {
		return buildAdresseFilterImpl(AuftragFac.FLR_AUFTRAG_FLRKUNDELIEFERADRESSE, filterKriterium);
	}

	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement using
	 * the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery().getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;
			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					// wenn nach der c_nr gefilter wird, wird das kriterium
					// veraendert
					if (filterKriterien[i].kritName.equals("c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false,
									ParameterFac.KATEGORIE_AUFTRAG,
									ParameterFac.PARAMETER_AUFTRAG_BELEGNUMMERSTARTWERT);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRAuftrag", sValue)) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true);
							}
							where.append(" " + FLR_AUFTRAG + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else if (isAdresseKunde(filterKriterien[i])) {
						where.append(buildAdresseFilterKunde(filterKriterien[i]));
					} else if (isAdresseLieferadresse(filterKriterien[i])) {
						where.append(buildAdresseFilterLieferadresse(filterKriterien[i]));
					} else if (filterKriterien[i].kritName.equals(AuftragFac.FLR_AUFTRAG_FLRKUNDE + "."
							+ LieferantFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						ParametermandantDto parameter = null;
						try {
							parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
						} catch (RemoteException ex) {
							throwEJBExceptionLPRespectOld(ex);
						}
						Boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter.getCWertAsObject();
						if (bSuchenInklusiveKbez) {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" ( upper(" + FLR_AUFTRAG + filterKriterien[i].kritName + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value.toUpperCase());
								where.append(" OR upper(" + FLR_AUFTRAG + "flrkunde.flrpartner.c_kbez" + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value.toUpperCase() + ") ");
							} else {
								where.append(" " + FLR_AUFTRAG + filterKriterien[i].kritName);
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
								where.append("OR " + FLR_AUFTRAG + "flrkunde.flrpartner.c_kbez");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
							}
						} else {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" upper(" + FLR_AUFTRAG + filterKriterien[i].kritName + ")");
							} else {
								where.append(" " + FLR_AUFTRAG + filterKriterien[i].kritName);
							}

							where.append(" " + filterKriterien[i].operator);

							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" " + filterKriterien[i].value.toUpperCase());
							} else {
								where.append(" " + filterKriterien[i].value);
							}
						}
					} else if (filterKriterien[i].kritName.equals("c_suche")) {

						where.append(buildWhereClauseExtendedSearchWithoutDuplicates(FLR_AUFTRAG_TEXTSUCHE_CLASSNAME,
								FLR_AUFTRAG, filterKriterien[i]));
					}

					else if (filterKriterien[i].kritName.equals("c_bez")) {
						where.append(" (");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								FLR_AUFTRAG + filterKriterien[i].kritName, filterKriterien[i].isBIgnoreCase()));
						where.append(" OR ");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								FLR_AUFTRAG + "c_bestellnummer", filterKriterien[i].isBIgnoreCase()));

						// 19915
						if (bProjekttitelInAG_AB) {
							where.append(" OR ");
							where.append(
									buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
											"flrprojekt.c_titel", filterKriterien[i].isBIgnoreCase()));
							where.append(" OR ");
							where.append(
									buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
											"ag_flrprojekt.c_titel", filterKriterien[i].isBIgnoreCase()));
						}

						where.append(") ");
					} else if (filterKriterien[i].kritName.equals(AuftragFac.FLR_AUFTRAG_FLRTEILNEHER_PARTNER_ID)) {
						where.append(" flrteilnehmer.partner_i_id_auftragteilnehmer = ")
								.append(filterKriterien[i].value);
						filterPartnerId = Integer.valueOf(filterKriterien[i].value);
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_AUFTRAG + filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_AUFTRAG + filterKriterien[i].kritName);
						}

						where.append(" " + filterKriterien[i].operator);

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toLowerCase());
							if (filterKriterien[i].kritName.equals("flrpaneldatenckey.x_inhalt")) {
								// Wenn wir nach paneldaten filtern zusaetzlich
								// nur nach auftragpanels suchen und dort nur in
								// Textfeldern
								where.append(" AND flrauftrag.flrpaneldatenckey.panel_c_nr = '"
										+ PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN + "'"
										+ " AND flrauftrag.flrpaneldatenckey.flrpanelbeschreibung.c_typ = '"
										+ PanelFac.TYP_WRAPPERTEXTFIELD + "'");
							}
						} else {
							where.append(" " + filterKriterien[i].value);
						}
					}
				}
			}

			if (!getBenutzerServicesFac().hatRecht(RechteFac.RECHT_AUFT_DARF_ALLE_SEHEN, theClientDto)) {
				if (filterAdded) {
					where.append(" " + booleanOperator);
				}

				filterAdded = true;

				Integer partnerIId = getPersonalFac().personalFindByPrimaryKeySmall(theClientDto.getIDPersonal())
						.getPartnerIId();

				where.append(" ( flrteilnehmer2.partner_i_id_auftragteilnehmer =" + partnerIId
						+ " OR flrauftrag.flrvertreter.flrpartner.i_id=" + partnerIId + ")");
				filterPartnerIdOderVertreter = Integer.valueOf(partnerIId);

			}

			if (filterAdded) {
				where.insert(0, " WHERE");
			}
		}

		// PJ19406 Nur wenn explizit nach einem(!) Teilnehmer gesucht wird,
		// wird auch der notwendige Join gemacht. Join ist notwendig, weil
		// in einer "Collection" (flrauftrag.flrauftragteilnehmer) nicht
		// noch weiter eingeschraenkt werden kann. ACHTUNG: Wenn z.B. ein
		// DirektFilter nach dem Teilnehmer hinzugefuegt wird, dann muss
		// eine andere Such/Filter-Logik implementiert werden
		// (DISTINCT(AUFTRAG.I_ID))
		if (filterPartnerId != null) {
			where.insert(0, " INNER JOIN " + FLR_AUFTRAG + "flrauftragteilnehmer as flrteilnehmer");
		}

		if (filterPartnerIdOderVertreter != null) {
			where.insert(0, " LEFT OUTER JOIN " + FLR_AUFTRAG + "flrauftragteilnehmer as flrteilnehmer2");
		}

		return where.toString();
	}

	/**
	 * builds the HQL (Hibernate Query Language) order by clause using the sort
	 * criterias contained in the current query.
	 * 
	 * @return the HQL order by clause.
	 */
	private String buildOrderByClause() {
		StringBuffer orderBy = new StringBuffer("");
		if (this.getQuery() != null) {
			SortierKriterium[] kriterien = this.getQuery().getSortKrit();
			boolean sortAdded = false;
			if (kriterien != null && kriterien.length > 0) {
				for (int i = 0; i < kriterien.length; i++) {
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(kriterien[i].kritName);
							orderBy.append(" ");
							orderBy.append(kriterien[i].value);
						}
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(FLR_AUFTRAG).append(AuftragFac.FLR_AUFTRAG_C_NR).append(" DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_I_ID) < 0) {
				// Martin Werner original: "unique sort required because
				// otherwise rowNumber of selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt()."
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_AUFTRAG).append(AuftragFac.FLR_AUFTRAG_I_ID).append(" DESC ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {

		String naechster_termin = " flrauftrag.t_liefertermin ";

		if (bAuftragspositionstermin_in_auswahl_anzeigen) {
			naechster_termin = " (SELECT min(ap.t_uebersteuerterliefertermin) FROM FLRAuftragposition ap WHERE ap.flrartikel.b_kalkulatorisch=0 AND ap.auftragpositionstatus_c_nr NOT IN ('"
					+ AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT + "','"
					+ AuftragServiceFac.AUFTRAGPOSITIONSTATUS_STORNIERT
					+ "') AND ap.flrauftrag.i_id=flrauftrag.i_id ) as naechster_termin ";
		}

		return "SELECT flrauftrag, "
				+ "(SELECT count(*) FROM FLREingangsrechnungAuftragszuordnung er WHERE er.flrauftrag.i_id=flrauftrag.i_id ) as er, "
				+ naechster_termin + " from FLRAuftrag  as flrauftrag "
				+ " left join flrauftrag.flrprojekt as flrprojekt " + " left join flrauftrag.flrlager as flrlager "
				+ " left join flrauftrag.flrvertreter2 as flrvertreter2 "
				+ "left join flrauftrag.flrangebot.flrprojekt as ag_flrprojekt ";

		/*
		 * + " left join flrauftrag.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
		 * + " left join flrauftrag.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
		 * +
		 * " left join flrauftrag.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
		 * ;
		 */
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			int rowNumber = 0;

			getQuery().setSortKrit(sortierKriterien);

			if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = null;

				try {

					String naechsterTermin = "";

					if (bAuftragspositionstermin_in_auswahl_anzeigen) {
						naechsterTermin = ",(SELECT count(*) FROM FLREingangsrechnungAuftragszuordnung er WHERE er.flrauftrag.i_id=flrauftrag.i_id ) as er "
								+ ",(SELECT min(ap.t_uebersteuerterliefertermin) FROM FLRAuftragposition ap WHERE ap.flrartikel.b_kalkulatorisch=0 AND  ap.auftragpositionstatus_c_nr NOT IN ('"
								+ AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT + "','"
								+ AuftragServiceFac.AUFTRAGPOSITIONSTATUS_STORNIERT
								+ "') AND ap.flrauftrag.i_id=flrauftrag.i_id ) as naechster_termin  ";
					}

					session = factory.openSession();
					String queryString = "select " + FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_I_ID + naechsterTermin
							+ FLR_AUFTRAG_FROM_CLAUSE

							+ " left join flrauftrag.flrprojekt as flrprojekt "
							+ " left join flrauftrag.flrangebot.flrprojekt as ag_flrprojekt " + this.buildWhereClause()
							+ this.buildOrderByClause();
					// logQuery(queryString);
					Query query = session.createQuery(queryString);
					ScrollableResults scrollableResult = query.scroll();
					if (scrollableResult != null) {
						scrollableResult.beforeFirst();
						while (scrollableResult.next()) {
							Integer id = (Integer) scrollableResult.getInteger(0);
							if (selectedId.equals(id)) {
								rowNumber = scrollableResult.getRowNumber();
								break;
							}
						}
					}
				} finally {
					try {
						session.close();
					} catch (HibernateException he) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
					}
				}
			}

			if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
				rowNumber = 0;
			}

			result = this.getPageAt(new Integer(rowNumber));
			result.setIndexOfSelectedRow(rowNumber);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return result;
	}

	private void setupParameters() {
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ANZEIGE_ANLEGER_STATT_VERTRETER);
			iAnlegerStattVertreterAnzeigen = (Integer) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_ZUSATZSTATUS_VERRECHENBAR);
			verrechenbarStattRohsAnzeigen = (Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAGSFREIGABE);
			bAuftragsfreigabe = ((Boolean) parameter.getCWertAsObject());
			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PROJEKT_TITEL_IN_AG_AB_PROJEKT);
			bProjekttitelInAG_AB = ((Boolean) parameter.getCWertAsObject());

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_ABBUCHUNGSLAGER_IN_AUSWAHLLISTE);
			bAbbuchungslagerAnzeigen = ((Boolean) parameter.getCWertAsObject());

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_ZWEITER_VERTRETER);
			bZweiterVertreter = ((Boolean) parameter.getCWertAsObject());

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAGSPOSITIONSTERMIN_IN_AUSWAHL_ANZEIGEN);
			bAuftragspositionstermin_in_auswahl_anzeigen = ((Boolean) parameter.getCWertAsObject());

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {

		String sortierungNachKunde = Facade.NICHT_SORTIERBAR;
		String sortierungNachOrt = Facade.NICHT_SORTIERBAR;

		if (SORTIERUNG_UI_PARTNER_ORT) {
			sortierungNachKunde = FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
					+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1;

			sortierungNachOrt = // Sortierung fuers erste mal nach LKZ
					FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "." + SystemFac.FLR_LP_FLRLAND + "."
							+ SystemFac.FLR_LP_LANDLKZ + ", " +
							// und dann nach plz
							AuftragHandler.FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_FLRKUNDE + "." + KundeFac.FLR_PARTNER
							+ "." + PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "." + SystemFac.FLR_LP_LANDPLZORTPLZ;
		}

		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_I_ID);
		columns.add("art", String.class, " ", QueryParameters.FLR_BREITE_XXS, Facade.NICHT_SORTIERBAR);
		columns.add("auft.auftragsnummer", String.class, getTextRespectUISpr("auft.auftragsnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_M, FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_C_NR);
		columns.add("lp.kunde", String.class, getTextRespectUISpr("lp.kunde", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, sortierungNachKunde);
		columns.add("lp.ort", String.class, getTextRespectUISpr("lp.ort", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, sortierungNachOrt);
		columns.add("auft.projektbestellnummer", String.class,
				getTextRespectUISpr("auft.projektbestellnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_C_BEZ_PROJEKTBEZEICHNUNG);

		String sortTermin = FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN;

		if (bAuftragspositionstermin_in_auswahl_anzeigen) {
			sortTermin = "col_2_0_";
			/*
			 * sortTermin =
			 * "(SELECT min(ap.t_uebersteuerterliefertermin) FROM FLRAuftragposition ap WHERE ap.auftragpositionstatus_c_nr NOT IN ('"
			 * + AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT + "','" +
			 * AuftragServiceFac.AUFTRAGPOSITIONSTATUS_STORNIERT +
			 * "') AND ap.flrauftrag.i_id=flrauftrag.i_id )";
			 */
		}

		columns.add("lp.termin", java.util.Date.class, getTextRespectUISpr("lp.termin", mandant, locUi),
				QueryParameters.FLR_BREITE_M, sortTermin);
		columns.add("lp.datum", java.util.Date.class, getTextRespectUISpr("lp.datum", mandant, locUi),
				QueryParameters.FLR_BREITE_M, FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_D_BELEGDATUM);

		String orderVertreter = "flrvertreter.c_kurzzeichen";
		if (iAnlegerStattVertreterAnzeigen == 1) {
			orderVertreter = "flrpersonalanleger.c_kurzzeichen";
		} else if (iAnlegerStattVertreterAnzeigen == 2) {
			orderVertreter = "flrpersonalaenderer.c_kurzzeichen";

		}

		columns.add("lp.vertreter", String.class, getTextRespectUISpr("lp.vertreter", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, FLR_AUFTRAG + orderVertreter);

		if (bZweiterVertreter) {
			columns.add("lp.vertreter2", String.class, getTextRespectUISpr("lp.vertreter", mandant, locUi) + " 2",
					QueryParameters.FLR_BREITE_XS, FLR_AUFTRAG + "flrvertreter2.c_kurzzeichen");
		}

		columns.add("lp.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR);
		columns.add("lp.wert", BigDecimal.class, getTextRespectUISpr("lp.wert", mandant, locUi),
				QueryParameters.FLR_BREITE_PREIS,
				FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_N_GESAMTAUFTRAGSWERT_IN_AUFTRAGSWAEHRUNG);
		columns.add("waehrung", String.class, " ", QueryParameters.FLR_BREITE_WAEHRUNG,
				FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_WAEHRUNG_C_NR_AUFTRAGSWAEHRUNG);
		if (verrechenbarStattRohsAnzeigen) {
			columns.add("auft.verrechenbar", Boolean.class, getTextRespectUISpr("auft.verrechnen", mandant, locUi), 3,
					FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_T_VERRECHENBAR,
					getTextRespectUISpr("auft.kannabgerechnetwerden", mandant, locUi));
		} else {
			columns.add("detail.label.rohs", Boolean.class, getTextRespectUISpr("detail.label.rohs", mandant, locUi), 3,
					FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_B_ROHS);
		}

		columns.add("detail.label.unverbindlich", Boolean.class,
				getTextRespectUISpr("auftrag.unverbindlich.short", mandant, locUi), 1,
				FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_B_LIEFERTERMINUNVERBINDLICH,
				getTextRespectUISpr("auftrag.unverbindlich.tooltip", mandant, locUi));

		if (bAuftragsfreigabe) {
			columns.add("auft.freigabe", Boolean.class, getTextRespectUISpr("auft.freigabe", mandant, locUi), 3,
					FLR_AUFTRAG + AuftragFac.FLR_AUFTRAG_T_AUFTRAGSFREIGABE);
		}

		// SP5836 Kommentar
		columns.add("lp.kommentar", String.class, getTextRespectUISpr("lp.kommentar", mandant, locUi), 1,
				Facade.NICHT_SORTIERBAR);

		if (bAbbuchungslagerAnzeigen) {
			columns.add("auft.abbuchungslager", String.class,
					getTextRespectUISpr("auft.abbuchungslager", mandant, locUi), QueryParameters.FLR_BREITE_XS,
					FLR_AUFTRAG + "flrlager.c_nr");
		}

		columns.add("Color", Color.class, "", 1, Facade.NICHT_SORTIERBAR);

		return columns;
	}

	public TableInfo getTableInfo() {

		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setupParameters();
		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;

	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		AuftragDto auftragDto = null;
		// KundeDto kundeDto = null;
		Integer partnerIId = null;
		try {
			auftragDto = getAuftragFac().auftragFindByPrimaryKey((Integer) key);
			partnerIId = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
					.getPartnerIId();
		} catch (EJBExceptionLP e) {

		}

		if (auftragDto != null) {
			// String sPath = new JcrPathResolver(theClientDto.getMandant())
			// .add(LocaleFac.BELEGART_AUFTRAG)
			// .add(LocaleFac.BELEGART_AUFTRAG)
			// .add(auftragDto.getCNr())
			// .asPath();
			DocPath docPath = new DocPath(new DocNodeAuftrag(auftragDto));

			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_AUFTRAG.trim() + "/"
			// + LocaleFac.BELEGART_AUFTRAG.trim() + "/"
			// + auftragDto.getCNr().replace("/", ".");

			return new PrintInfoDto(docPath, partnerIId, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "AUFTRAG";
	}
}
