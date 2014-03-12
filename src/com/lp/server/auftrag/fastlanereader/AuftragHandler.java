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
package com.lp.server.auftrag.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
	Integer iAnlegerStattVertreterAnzeigen = 0;

	boolean verrechenbarStattRohsAnzeigen = false;

	private Feature cachedFeature = null ;
	
	private class Feature {
		private boolean featureAdresse = false ;
		private boolean featureAdresseAnschrift = false ;
		private boolean featureAdresseKurzanschrift = false ;
		private List<IAddressContact> addresses = new ArrayList<IAddressContact>() ;
		private boolean featureUseLieferadresse = false ;
		private int flrRowCount = 0 ;
		private IAuftragFLRData[] flrData = null ;
		
		public Feature() {
			if(getQuery() instanceof QueryParametersFeatures) {
				QueryParametersFeatures qpf = (QueryParametersFeatures) getQuery() ;
				featureAdresse = qpf.hasFeature(AuftragHandlerFeature.ADRESSE_KOMPLETT) ;
				featureAdresseAnschrift = qpf.hasFeature(AuftragHandlerFeature.ADRESSE_ANSCHRIFT) ;
				featureAdresseKurzanschrift = qpf.hasFeature(AuftragHandlerFeature.ADRESSE_KURZANSCHRIFT) ;
				featureUseLieferadresse = qpf.hasFeature(AuftragHandlerFeature.ADRESSE_IST_LIEFERADRESSE) ;
			}
		}
		
		public void setFlrRowCount(int rows) {
			flrRowCount = rows ;
			flrData = new AuftragFLRDataDto[flrRowCount] ;
		}
		
		public IAuftragFLRData[] getFlrData() {
			return flrData ;
		}
		
		public boolean hasAdresse() {
			return featureAdresse ;
		}

		public boolean hasAdresseAnschrift() {
			return featureAdresseAnschrift ;
		}
		
		public boolean hasAdresseKurzanschrift() {
			return featureAdresseKurzanschrift ;
		}
		
		public boolean useLieferadresse() {
			return featureUseLieferadresse ;
		}
		
		public void buildAddress(int row, FLRAuftrag auftrag) throws RemoteException {
			if(! hasAdresse()) return ;
			
			FLRPartner flrpartner = useLieferadresse() 
					? auftrag.getFlrlieferkunde().getFlrpartner() 
					: auftrag.getFlrkunde().getFlrpartner() ;
			FLRLandplzort flranschrift = flrpartner.getFlrlandplzort();
			if (flranschrift == null) return ;

			Integer contactId = useLieferadresse() ?
					auftrag.getAnsprechpartner_i_id_lieferadresse() : auftrag.getAnsprechpartner_i_id_kunde();

			IAdresse address = new AdresseDto();
			address.setPartnerId(flrpartner.getI_id());

			address.setCountryCode(flranschrift.getFlrland().getC_lkz()) ;
			address.setZipcode(flranschrift.getC_plz());
			address.setCity(flranschrift.getFlrort().getC_name());
			
			address.setName1Lastname(flrpartner.getC_name1nachnamefirmazeile1());
			address.setName2Firstname(flrpartner.getC_name2vornamefirmazeile2());
			address.setName3Department(flrpartner.getC_name3vorname2abteilung());
			address.setTitel(flrpartner.getC_titel());
			address.setTitelSuffix(flrpartner.getC_ntitel());
			address.setSalutation(flrpartner.getAnrede_c_nr());
			address.setStreet(flrpartner.getC_strasse());
			address.setEmail(flrpartner.getC_email());
			address.setPhone(flrpartner.getC_telefon()); 
			
		
			IAdresse contactAdresse = null ;
			AnsprechpartnerDto anspDto = contactId == null ? null : getAnsprechpartnerFac()
					.ansprechpartnerFindByPrimaryKey(contactId, theClientDto) ;
			if(anspDto != null) {
				contactAdresse = buildAdresse(anspDto, address) ;
			}
			
			if(hasAdresseAnschrift()) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(flrpartner.getI_id(), theClientDto) ;
				
				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto) ;
				Locale locale = theClientDto.getLocUi() ;		
				String s = formatAdresseFuerAusdruck(partnerDto, anspDto, mandantDto, locale, LocaleFac.BELEGART_AUFTRAG) ;
				address.setLines(s.split("\n"));
			}
			
			if(hasAdresseKurzanschrift()) {			
			}
			
			IAddressContact addressContact = new AddressContactDto(address, contactAdresse) ;
			setAddressContact(row, addressContact);
//			addresses.add(addressContact) ;
		}
		
		private IAdresse buildAdresse(AnsprechpartnerDto anspDto, IAdresse companyAddress) {
			IAdresse address = new AdresseDto();
			PartnerDto partnerDto = anspDto.getPartnerDto() ;
			address.setPartnerId(partnerDto.getIId());

			address.setCountryCode(companyAddress.getCountryCode()) ;
			address.setZipcode(companyAddress.getZipcode());
			address.setCity(companyAddress.getCity()) ;
			address.setStreet(companyAddress.getStreet()) ;
			
			address.setName1Lastname(partnerDto.getCName1nachnamefirmazeile1());
			address.setName2Firstname(partnerDto.getCName2vornamefirmazeile2());
			address.setName3Department(partnerDto.getCName3vorname2abteilung());
			address.setTitel(partnerDto.getCTitel());
			address.setTitelSuffix(partnerDto.getCNtitel());
			address.setSalutation(partnerDto.getAnredeCNr());
			address.setEmail(anspDto.getCEmail());
			address.setPhone(anspDto.getCTelefon()); 

			return address ;
		}
		
		public List<IAddressContact> getAddresses() {
			return addresses ;
		}
		
		private IAuftragFLRData getFlrDataObject(int row) {
			if(flrData[row] == null) {
				flrData[row] = new AuftragFLRDataDto() ;
			}
			return flrData[row] ;
		}
		
		public void setInternerKommentar(int row, Boolean value) {
			getFlrDataObject(row).setInternerKommentar(value);
		}
		public Boolean getInternerKommentar(int row) {
			return getFlrDataObject(row).hasInternerKommentar() ;
		}
		public void setExternerKommentar(int row, Boolean value) {
			getFlrDataObject(row).setExternerKommentar(value);
		}
		public Boolean getExternerKommentar(int row) {
			return getFlrDataObject(row).hasExternerKommentar() ;
		}
		
		public void setAddressContact(int row, IAddressContact addressContact) {
			getFlrDataObject(row).setAddressContact(addressContact);
		}
		
		public IAddressContact getAddressContact(int row) {
			return getFlrDataObject(row).getAddressContact() ;
		}
	}
	
	private Feature getFeature() {
		if(cachedFeature == null) {
			cachedFeature = new Feature() ;
		}
		return cachedFeature ;
	}
	
	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex
	 *            diese Zeile soll selektiert sein
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;
			
			session = factory.openSession();
			String queryString = getFromClause() + buildWhereClause()
					+ buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

			getFeature().setFlrRowCount(rows.length);
			
			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();
				FLRAuftrag auftrag = (FLRAuftrag) o[0];

				boolean hasERs = false ;
				long iAnzahlER = 0;
				if (o[1] != null) {
					iAnzahlER = (Long) o[1];
					hasERs = (Long)o[1] > 0 ;
				}
				rows[row][col++] = auftrag.getI_id();

				// Kuerzel fuer die Auftragart
				String auftragart = null;

				if (auftrag.getAuftragart_c_nr().equals(
						AuftragServiceFac.AUFTRAGART_ABRUF)) {
					auftragart = AuftragServiceFac.AUFTRAGART_ABRUF_SHORT;
				} else if (auftrag.getAuftragart_c_nr().equals(
						AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					auftragart = AuftragServiceFac.AUFTRAGART_RAHMEN_SHORT;
				} else if (auftrag.getAuftragart_c_nr().equals(
						AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
					auftragart = AuftragServiceFac.AUFTRAGART_WIEDERHOLEND_SHORT;
				}

				rows[row][col++] = auftragart;
				rows[row][col++] = auftrag.getC_nr();
				rows[row][col++] = auftrag.getFlrkunde() == null ? null
						: auftrag.getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

				// IMS 1757 die Anschrift des Kunden anzeigen
				String cAnschrift = null; // eine Liefergruppenanfrage hat
				// keinen Lieferanten

				if (auftrag.getFlrkunde() != null) {
					FLRLandplzort flranschrift = auftrag.getFlrkunde()
							.getFlrpartner().getFlrlandplzort();

					if (flranschrift != null) {
						cAnschrift = flranschrift.getFlrland().getC_lkz() + "-"
								+ flranschrift.getC_plz() + " "
								+ flranschrift.getFlrort().getC_name();
					}
					
					getFeature().buildAddress(row, auftrag) ;
				}

				rows[row][col++] = cAnschrift;
				String proj_bestellnummer = "";
				if (auftrag.getC_bez() != null) {
					proj_bestellnummer = auftrag.getC_bez();
				}

				if (auftrag.getC_bestellnummer() != null) {
					proj_bestellnummer += " | " + auftrag.getC_bestellnummer();
				}

				rows[row][col++] = proj_bestellnummer;
				rows[row][col++] = auftrag.getT_liefertermin();
				rows[row][col++] = auftrag.getT_belegdatum();

				if (iAnlegerStattVertreterAnzeigen == 1) {
					if (auftrag.getFlrpersonalanleger() != null) {
						rows[row][col++] = auftrag.getFlrpersonalanleger()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				} else if (iAnlegerStattVertreterAnzeigen == 2) {
					if (auftrag.getFlrpersonalaenderer() != null) {
						rows[row][col++] = auftrag.getFlrpersonalaenderer()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				} else {
					if (auftrag.getFlrvertreter() != null) {
						rows[row][col++] = auftrag.getFlrvertreter()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				}

				String sStatus = auftrag.getAuftragstatus_c_nr();
				rows[row][col++] = getStatusMitUebersetzung(sStatus,
						auftrag.getT_versandzeitpunkt(),
						auftrag.getC_versandtype());

				BigDecimal nGesamtwertAuftragInAuftragswaehrung = new BigDecimal(
						0);

				if (auftrag.getN_gesamtauftragswertinauftragswaehrung() != null
						&& !auftrag.getAuftragstatus_c_nr().equals(
								AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
					nGesamtwertAuftragInAuftragswaehrung = auftrag
							.getN_gesamtauftragswertinauftragswaehrung();
				}

				if (bDarfPreiseSehen) {
					rows[row][col++] = nGesamtwertAuftragInAuftragswaehrung;
				} else {
					rows[row][col++] = null;
				}
				rows[row][col++] = auftrag.getWaehrung_c_nr_auftragswaehrung();

				if (verrechenbarStattRohsAnzeigen) {
					if (auftrag.getT_verrechenbar() != null) {
						rows[row][col++] = Boolean.TRUE;
					} else {
						rows[row][col++] = Boolean.FALSE;
					}
				} else {
					rows[row][col++] = Helper
							.short2Boolean(auftrag.getB_rohs());
				}
				
				rows[row][col++] = Helper.short2Boolean(auftrag
						.getB_lieferterminunverbindlich());

				if (auftrag.getB_poenale().equals(new Short((short) 1))) {
//					rows[row][col++] = iAnzahlER == 0 ? Color.RED : Color.MAGENTA ;
					rows[row][col++] = hasERs ? Color.MAGENTA : Color.RED ;
				} else {
//					rows[row][col++] = iAnzahlER == 0 ? null : Color.BLUE ;
					rows[row][col++] = hasERs ? Color.BLUE : null ;
				}

				getFeature().setInternerKommentar(row, auftrag.getX_internerkommentar() != null);
				getFeature().setExternerKommentar(row, auftrag.getX_externerkommentar() != null) ;			

				++row ;
				col = 0;
			}
			
			if(getFeature().hasAdresse()) {
				AuftragQueryResult auftragResult = new AuftragQueryResult(rows, this.getRowCount(), startIndex,
						endIndex, 0);
//				auftragResult.setAddresses(getFeature().getAddresses()) ;
				auftragResult.setFlrData(getFeature().getFlrData());
				result = auftragResult ;				
			} else {
				result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0) ;
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
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
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) from FLRAuftrag  as flrauftrag "
					+ this.buildWhereClause();

			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE,
							he);
				}
			}
		}
		return rowCount;
	}

	private boolean isAdresseKunde(FilterKriterium filterKriterium) {
		return filterKriterium.kritName
				.equals(AuftragFac.FLR_AUFTRAG_FLRKUNDE
				+ "."
				+ LieferantFac.FLR_PARTNER
				+ "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1) ;	
	}

	private boolean isAdresseLieferadresse(FilterKriterium filterKriterium) {
		return filterKriterium.kritName
				.equals(AuftragFac.FLR_AUFTRAG_FLRKUNDELIEFERADRESSE
				+ "."
				+ LieferantFac.FLR_PARTNER
				+ "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1) ;	
	}
	
	private String buildAdresseFilterImpl(String adresseTyp, FilterKriterium filterKriterium) {
		StringBuffer where = new StringBuffer() ;
		
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		
		if (parameter.asBoolean()) {
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" ( upper(" + FLR_AUFTRAG
						+ filterKriterium.kritName + ")");
				where.append(" " + filterKriterium.operator);
				where.append(" "
						+ filterKriterium.value
								.toUpperCase());
				where.append(" OR upper(" + FLR_AUFTRAG
						+ adresseTyp +".flrpartner.c_kbez" + ")");
				where.append(" " + filterKriterium.operator);
				where.append(" "
						+ filterKriterium.value
								.toUpperCase() + ") ");
			} else {
				where.append(" " + FLR_AUFTRAG
						+ filterKriterium.kritName);
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value);
				where.append("OR " + FLR_AUFTRAG
						+ adresseTyp + ".flrpartner.c_kbez");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value);
			}
		} else {
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" upper(" + FLR_AUFTRAG
						+ filterKriterium.kritName + ")");
			} else {
				where.append(" " + FLR_AUFTRAG
						+ filterKriterium.kritName);
			}

			where.append(" " + filterKriterium.operator);

			if (filterKriterium.isBIgnoreCase()) {
				where.append(" "
						+ filterKriterium.value
								.toUpperCase());
			} else {
				where.append(" " + filterKriterium.value);
			}
		}
		return where.toString() ;
	}
	
	private String buildAdresseFilterKunde(FilterKriterium filterKriterium) {
		return buildAdresseFilterImpl(AuftragFac.FLR_AUFTRAG_FLRKUNDE, filterKriterium) ;
	}
	
	private String buildAdresseFilterLieferadresse(FilterKriterium filterKriterium) {
		return buildAdresseFilterImpl(AuftragFac.FLR_AUFTRAG_FLRKUNDELIEFERADRESSE, filterKriterium) ;		
	}
	
	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement
	 * using the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery()
					.getFilterBlock().filterKrit;
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
							String sValue = super.buildWhereBelegnummer(
									filterKriterien[i], false);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRAuftrag", sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" " + FLR_AUFTRAG
									+ filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}
					} else if(isAdresseKunde(filterKriterien[i])) {
						where.append(buildAdresseFilterKunde(filterKriterien[i])) ;
					} else if(isAdresseLieferadresse(filterKriterien[i])) {
						where.append(buildAdresseFilterLieferadresse(filterKriterien[i])) ;						
					} else if (filterKriterien[i].kritName
							.equals(AuftragFac.FLR_AUFTRAG_FLRKUNDE
									+ "."
									+ LieferantFac.FLR_PARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						ParametermandantDto parameter = null;
						try {
							parameter = getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_ALLGEMEIN,
											ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
						} catch (RemoteException ex) {
							throwEJBExceptionLPRespectOld(ex);
						}
						Boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter
								.getCWertAsObject();
						if (bSuchenInklusiveKbez) {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" ( upper(" + FLR_AUFTRAG
										+ filterKriterien[i].kritName + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase());
								where.append(" OR upper(" + FLR_AUFTRAG
										+ "flrkunde.flrpartner.c_kbez" + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase() + ") ");
							} else {
								where.append(" " + FLR_AUFTRAG
										+ filterKriterien[i].kritName);
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
								where.append("OR " + FLR_AUFTRAG
										+ "flrkunde.flrpartner.c_kbez");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
							}
						} else {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" upper(" + FLR_AUFTRAG
										+ filterKriterien[i].kritName + ")");
							} else {
								where.append(" " + FLR_AUFTRAG
										+ filterKriterien[i].kritName);
							}

							where.append(" " + filterKriterien[i].operator);

							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase());
							} else {
								where.append(" " + filterKriterien[i].value);
							}
						}
					} else if (filterKriterien[i].kritName.equals("c_suche")) {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_AUFTRAG
									+ "flrauftragtextsuche."
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_AUFTRAG
									+ "flrauftragtextsuche."
									+ filterKriterien[i].kritName);
						}

						where.append(" " + filterKriterien[i].operator);

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
						} else {
							where.append(" " + filterKriterien[i].value);
						}

					}

					else if (filterKriterien[i].kritName.equals("c_bez")) {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" (lower(flrauftrag."
									+ filterKriterien[i].kritName + ")");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
							where.append(" OR lower(flrauftrag.c_bestellnummer)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase()
									+ ")");
						}
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_AUFTRAG
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_AUFTRAG
									+ filterKriterien[i].kritName);
						}

						where.append(" " + filterKriterien[i].operator);

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
							if (filterKriterien[i].kritName
									.equals("flrpaneldatenckey.x_inhalt")) {
								// Wenn wir nach paneldaten filtern zusaetzlich
								// nur nach auftragpanels suchen und dort nur in
								// Textfeldern
								where.append(" AND flrpaneldatenckey.panel_c_nr = '"
										+ PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN
										+ "'"
										+ " AND flrpaneldatenckey.flrpanelbeschreibung.c_typ = '"
										+ PanelFac.TYP_WRAPPERTEXTFIELD + "'");
							}
						} else {
							where.append(" " + filterKriterien[i].value);
						}
					}
				}
			}
			if (filterAdded) {
				where.insert(0, " WHERE");
			}
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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(FLR_AUFTRAG + kriterien[i].kritName);
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
				orderBy.append(FLR_AUFTRAG).append(AuftragFac.FLR_AUFTRAG_C_NR)
						.append(" DESC ");
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
				orderBy.append(" ").append(FLR_AUFTRAG)
						.append(AuftragFac.FLR_AUFTRAG_I_ID).append(" DESC ");
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
		return "SELECT flrauftrag, (SELECT count(*) FROM FLREingangsrechnungAuftragszuordnung er WHERE er.flrauftrag.i_id=flrauftrag.i_id ) as er from FLRAuftrag  as flrauftrag ";
		/*
		 * +
		 * " left join flrauftrag.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
		 * +
		 * " left join flrauftrag.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
		 * +
		 * " left join flrauftrag.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
		 * ;
		 */
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			int rowNumber = 0;

			getQuery().setSortKrit(sortierKriterien);

			if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = null;

				try {
					session = factory.openSession();
					String queryString = "select " + FLR_AUFTRAG
							+ AuftragFac.FLR_AUFTRAG_I_ID
							+ FLR_AUFTRAG_FROM_CLAUSE + this.buildWhereClause()
							+ this.buildOrderByClause();
					Query query = session.createQuery(queryString);
					ScrollableResults scrollableResult = query.scroll();
					if (scrollableResult != null) {
						scrollableResult.beforeFirst();
						while (scrollableResult.next()) {
							Integer id = (Integer) scrollableResult
									.getInteger(0);
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
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_HIBERNATE, he);
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

	public TableInfo getTableInfo() {

		if (super.getTableInfo() == null) {
			String sortierungNachKunde = Facade.NICHT_SORTIERBAR;
			String sortierungNachOrt = Facade.NICHT_SORTIERBAR;

			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_ANZEIGE_ANLEGER_STATT_VERTRETER);
				iAnlegerStattVertreterAnzeigen = (Integer) parameter
						.getCWertAsObject();

				parameter = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_ZUSATZSTATUS_VERRECHENBAR);
				verrechenbarStattRohsAnzeigen = (Boolean) parameter
						.getCWertAsObject();

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			if (SORTIERUNG_UI_PARTNER_ORT) {
				sortierungNachKunde = AuftragFac.FLR_AUFTRAG_FLRKUNDE + "."
						+ KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1;

				sortierungNachOrt = // Sortierung fuers erste mal nach LKZ
				AuftragFac.FLR_AUFTRAG_FLRKUNDE + "." + KundeFac.FLR_PARTNER
						+ "." + PartnerFac.FLR_PARTNER_FLRLANDPLZORT
						+ "."
						+ SystemFac.FLR_LP_FLRLAND
						+ "."
						+ SystemFac.FLR_LP_LANDLKZ
						+ ", "
						+
						// und dann nach plz
						AuftragHandler.FLR_AUFTRAG
						+ AuftragFac.FLR_AUFTRAG_FLRKUNDE + "."
						+ KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
						+ SystemFac.FLR_LP_LANDPLZORTPLZ;
			}
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, String.class, String.class,
							java.util.Date.class,
							java.util.Date.class,
							String.class,
							Icon.class,
							// flrimage: 0 fuer die
							// Bestimmung des
							// Spaltentyps in JTable
							BigDecimal.class, String.class, Boolean.class,
							Boolean.class, Color.class },
					new String[] {
							"i_id",
							" ",
							getTextRespectUISpr("auft.auftragsnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.kunde",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.ort",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("auft.projektbestellnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.termin",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.datum",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.vertreter",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.status",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.wert",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							" ",
							verrechenbarStattRohsAnzeigen ? getTextRespectUISpr(
									"auft.verrechenbar",
									theClientDto.getMandant(),
									theClientDto.getLocUi())
									: getTextRespectUISpr("detail.label.rohs",
											theClientDto.getMandant(),
											theClientDto.getLocUi()),
							getTextRespectUISpr("detail.label.unverbindlich",
									theClientDto.getMandant(),
									theClientDto.getLocUi()), " ", },
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
							// Spalte
							// wird
							// ausgeblendet
							QueryParameters.FLR_BREITE_XXS, // Kuerzel
							// Auftragart
							QueryParameters.FLR_BREITE_M, // flrspalte: 1 Breite
							// der Auftragnummer
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // flrspalte
							// : 2
							// Breite
							// des
							// Kunden
							// ist
							// variabel
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_XS, // XS, //
							// flrimage: 0
							// Breite fuer
							// das Icon im
							// TableHeader
							// urspr. PREIS
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_WAEHRUNG, 3, 3, 1 },
					new String[] {
							AuftragFac.FLR_AUFTRAG_I_ID,
							Facade.NICHT_SORTIERBAR, // AuftragFac.
							// FLR_AUFTRAGART_C_NR
							AuftragFac.FLR_AUFTRAG_C_NR,
							sortierungNachKunde,
							sortierungNachOrt,
							AuftragFac.FLR_AUFTRAG_C_BEZ_PROJEKTBEZEICHNUNG,
							AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN,
							AuftragFac.FLR_AUFTRAG_D_BELEGDATUM,
							AuftragFac.FLR_AUFTRAG_VERTRETER_I_ID,
							AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, // flrimage
							// : 1
							// Sortierung
							// funktioniert
							// wie
							// gewohnt
							AuftragFac.FLR_AUFTRAG_N_GESAMTAUFTRAGSWERT_IN_AUFTRAGSWAEHRUNG,
							AuftragFac.FLR_AUFTRAG_WAEHRUNG_C_NR_AUFTRAGSWAEHRUNG,
							verrechenbarStattRohsAnzeigen ? AuftragFac.FLR_AUFTRAG_T_VERRECHENBAR
									: AuftragFac.FLR_AUFTRAG_B_ROHS,
							AuftragFac.FLR_AUFTRAG_B_LIEFERTERMINUNVERBINDLICH,
							Facade.NICHT_SORTIERBAR }));
		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		AuftragDto auftragDto = null;
		// KundeDto kundeDto = null;
		Integer partnerIId = null;
		try {
			auftragDto = getAuftragFac().auftragFindByPrimaryKey((Integer) key);
			partnerIId = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
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
