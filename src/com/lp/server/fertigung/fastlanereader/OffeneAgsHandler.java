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
package com.lp.server.fertigung.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.fastlanereader.generated.FLROffeneags;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.IOffeneAgsFLRData;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.OffeneAgsFLRDataDto;
import com.lp.server.fertigung.service.OffeneAgsHandlerFeature;
import com.lp.server.fertigung.service.OffeneAgsQueryResult;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.pkgenerator.format.LpDefaultBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.QueryFeature;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.IInject;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.DoppelIcon;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer den Losarbeitsplan implementiert.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 21.07.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class OffeneAgsHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String FLR_OFFENEAGS = "flroffeneags.";
	private static final String FLR_LOSMAT_FROM_CLAUSE = " from FLROffeneags flroffeneags "
			+ " LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit.artikelsprset AS aspr_taetigkeit "
			+ " LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste.artikelsprset AS aspr_stueckliste "
			+ " LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial.artikelsprset AS aspr_sollmaterial "
			+ " LEFT OUTER JOIN flroffeneags.flrkunde AS flrkunde "
			+ " LEFT OUTER JOIN flroffeneags.flrmaschine AS flrmaschine "
			+ " LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit AS taetigkeit "
			+ " LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial AS sollmaterial "
			+ " LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste AS stueckliste "
			+ " LEFT OUTER JOIN flroffeneags.flrlossollarbeitsplan AS sollarbeitsplan "
			+ " LEFT OUTER JOIN flroffeneags.flrlos AS flrlos " + " LEFT OUTER JOIN flrlos.flrauftrag AS flrauftrag ";

	private int bLosnummerAuftragsbezogen = 0;
	private Feature cachedFeature = null;

	private class ActualTimeEntry {
		private Integer losId;
		private AuftragzeitenDto[] machineTimes;
		private AuftragzeitenDto[] humanTimes;
		private boolean excessiveEntries;
		private boolean errors;

		public ActualTimeEntry(Integer losId) {
			setLosId(losId);
			setMachineTimes(new AuftragzeitenDto[0]);
			setHumanTimes(new AuftragzeitenDto[0]);
		}

		public Integer getLosId() {
			return losId;
		}

		public void setLosId(Integer losId) {
			this.losId = losId;
		}

		public AuftragzeitenDto[] getMachineTimes() {
			return machineTimes;
		}

		public void setMachineTimes(AuftragzeitenDto[] machineTimes) {
			this.machineTimes = machineTimes;
		}

		public AuftragzeitenDto[] getHumanTimes() {
			return humanTimes;
		}

		public void setHumanTimes(AuftragzeitenDto[] humanTimes) {
			this.humanTimes = humanTimes;
		}

		public boolean hasExcessiveEntries() {
			return excessiveEntries;
		}

		public void setExcessiveEntries(boolean excessiveEntries) {
			this.excessiveEntries = excessiveEntries;
		}

		public boolean hasErrors() {
			return errors;
		}

		public void setErrors(boolean errors) {
			this.errors = errors;
		}
	}

	private class ActualTimeEntryCache extends HvCreatingCachingProvider<Integer, ActualTimeEntry> {
		@Override
		protected ActualTimeEntry provideValue(Integer losId, Integer transformedKey) {
			try {
				ActualTimeEntry timeEntry = new ActualTimeEntry(losId);
				timeEntry.setExcessiveEntries(getZeiterfassungFac()
						.sindZuvieleZeitdatenEinesBelegesVorhanden(LocaleFac.BELEGART_LOS, losId, theClientDto));
				if (!timeEntry.hasExcessiveEntries()) {
					timeEntry.setHumanTimes(
							getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS, losId, null, null,
									null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto));
				}

				timeEntry.setMachineTimes(
						getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(losId, null, null, null, theClientDto));
				return timeEntry;
			} catch (EJBExceptionLP e) {
				myLogger.warn("Los mit der Id '" + losId + "' hat Fehler in den Zeitdaten. Zeiten ignoriert!", e);
				ActualTimeEntry timeEntry = new ActualTimeEntry(losId);
				timeEntry.setErrors(true);
				return timeEntry;
			} catch (RemoteException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
			}
		}
	}

	private class LosMoveableEntry {
		private LossollarbeitsplanDto[] sollarbeitsplanDtos;
		private boolean moveable;

		public LossollarbeitsplanDto[] getSollarbeitsplanDtos() {
			return sollarbeitsplanDtos;
		}

		public void setSollarbeitsplanDtos(LossollarbeitsplanDto[] sollarbeitsplanDtos) {
			this.sollarbeitsplanDtos = sollarbeitsplanDtos;
		}

		/**
		 * Ist das Los prinzipiell verschiebbar?</br>
		 * <p>
		 * Weil noch keiner der AG fertig, und noch kein Fortschritt erfasst. Die
		 * m&ouml;glicherweise erfasste Istzeit wird vorerst ignoriert
		 * </p>
		 * 
		 * @return
		 */
		public boolean isMoveable() {
			return moveable;
		}

		public void setMoveable(boolean moveable) {
			this.moveable = moveable;
		}
	}

	private class LossollarbeitsplanCache extends HvCreatingCachingProvider<Integer, LosMoveableEntry> {
		@Override
		protected LosMoveableEntry provideValue(Integer losId, Integer transformedKey) {
			try {
				LosMoveableEntry entry = new LosMoveableEntry();
				entry.setSollarbeitsplanDtos(getFertigungFac().lossollarbeitsplanFindByLosIId(losId));
				entry.setMoveable(false);
				for (LossollarbeitsplanDto lossollarbeitsplanDto : entry.getSollarbeitsplanDtos()) {
					if (Helper.short2boolean(lossollarbeitsplanDto.getBFertig())) {
						return entry;
					}
					if (lossollarbeitsplanDto.getFFortschritt() != null) {
						return entry;
					}
				}

				entry.setMoveable(true);
				return entry;
			} catch (RemoteException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
			}
		}
	}

	private class Feature extends QueryFeature<IOffeneAgsFLRData> {
		private boolean featureAuftragsdatum;
		private boolean featureProjektnr;
		private boolean featureArtikelKbez;
		private boolean featureIstzeit;
		private boolean featureNaechsterAG;
		private boolean featureAbgelieferteMenge;
		private long now;
		private OffeneAgsFLRDataDto flrDataEntry;
		private ActualTimeEntryCache cachedTimes;
		private LossollarbeitsplanCache cachedSollarbeitsplan;

		public Feature() {
			if (getQuery() instanceof QueryParametersFeatures) {
				initializeFeature((QueryParametersFeatures) getQuery());
			}
		}

		protected void initializeFeature(QueryParametersFeatures query) {
			featureAuftragsdatum = query.hasFeature(OffeneAgsHandlerFeature.AUFTRAGS_DATUM);
			featureProjektnr = query.hasFeature(OffeneAgsHandlerFeature.PROJEKT_NR);
			featureArtikelKbez = query.hasFeature(OffeneAgsHandlerFeature.ARTIKEL_KBEZ);
			featureIstzeit = query.hasFeature(OffeneAgsHandlerFeature.ISTZEIT_ERMITTELN);
			featureNaechsterAG = query.hasFeature(OffeneAgsHandlerFeature.NUR_NAECHSTER_AG);
			featureAbgelieferteMenge = query.hasFeature(OffeneAgsHandlerFeature.ABGELIEFERTE_MENGE);
			now = System.currentTimeMillis();
			cachedTimes = new ActualTimeEntryCache();
			cachedSollarbeitsplan = new LossollarbeitsplanCache();
		}

		@Override
		protected IOffeneAgsFLRData[] createFlrData(int rows) {
			return new OffeneAgsFLRDataDto[rows];
		}

		public boolean hasFeatureAuftragsDatum() {
			return featureAuftragsdatum;
		}

		public boolean hasFeatureProjektNr() {
			return featureProjektnr;
		}

		public boolean hasFeatureArtikelKbez() {
			return featureArtikelKbez;
		}

		public boolean hasFeatureIstzeit() {
			return featureIstzeit;
		}

		public boolean hasFeatureNurNaechsterAg() {
			return featureNaechsterAG;
		}

		public boolean hasFeatureAbgelieferteMenge() {
			return featureAbgelieferteMenge;
		}

		protected void clearFlrDataEntry() {
			flrDataEntry = null;
		}

		protected OffeneAgsFLRDataDto getFlrDataEntry(int row) {
			if (flrDataEntry == null) {
				flrDataEntry = new OffeneAgsFLRDataDto();
				setFlrDataObject(row, flrDataEntry);
			}
			return flrDataEntry;
		}

		public void buildFeatureData(int row, Object o[]) {
			clearFlrDataEntry();
			FLROffeneags loszeit = (FLROffeneags) o[0];
			if (hasFeatureAuftragsDatum()) {
				FLRAuftragReport flrAuftrag = loszeit.getFlrlos().getFlrauftrag();
				long losEnde = loszeit.getFlrlos().getT_produktionsende().getTime();
				if (flrAuftrag != null) {
					Long liefertermin = calculateDeliveryDate(loszeit.getFlrlos());
					flrDataEntry = new OffeneAgsFLRDataDto(flrAuftrag.getI_id(), flrAuftrag.getC_nr(), liefertermin,
							losEnde, now > losEnde);
				} else {
					flrDataEntry = new OffeneAgsFLRDataDto(losEnde, now > losEnde);
				}

				setFlrDataObject(row, flrDataEntry);
			}

			if (hasFeatureProjektNr()) {
				getFlrDataEntry(row).setLosProjekt(loszeit.getFlrlos().getC_projekt());
			}

			if (hasFeatureArtikelKbez()) {
				FLRArtikellistespr artikellistespr_taetigkeit = (FLRArtikellistespr) o[1];
				if (artikellistespr_taetigkeit != null) {
					getFlrDataEntry(row).setTaetigkeitArtikelKBez(artikellistespr_taetigkeit.getC_kbez());
				}
			}

			getFlrDataEntry(row).setActualTime(calculateActualTime(loszeit));

			FLRArtikelliste artikel_stueckliste = loszeit.getFlrartikel_stueckliste();
			if (artikel_stueckliste != null) {
				getFlrDataEntry(row).setPartlistItemCnr(artikel_stueckliste.getC_nr());
			}
			FLRArtikellistespr artikellistespr_stueckliste = (FLRArtikellistespr) o[2];
			if (artikellistespr_stueckliste != null) {
				getFlrDataEntry(row).setPartlistItemDescription(artikellistespr_stueckliste.getC_bez());
				getFlrDataEntry(row).setPartlistItemShortDescription(artikellistespr_stueckliste.getC_kbez());
			} else {
				if (artikel_stueckliste != null) {
					setDescriptionByLocKonzern(getFlrDataEntry(row), artikel_stueckliste.getI_id());
				}
			}

			if (hasFeatureIstzeit()) {
				getFlrDataEntry(row).setStartTimeMoveable(calculateStartMoveable(loszeit));
				getFlrDataEntry(row).setFinishTimeMoveable(calculateFinishMoveable(loszeit));
			}

			if (hasFeatureAbgelieferteMenge()) {
				getFlrDataEntry(row).setOpenQuantity(calculateOpenQuantity(loszeit.getFlrlos()));
			}
		}

		private Long calculateDeliveryDate(FLRLos flrlos) {
			FLRAuftragReport flrauftrag = flrlos.getFlrauftrag();
			java.util.Date liefertermin = flrauftrag.getT_liefertermin_timestamp();
			FLRAuftragposition flrAuftragPos = flrlos.getFlrauftragposition();

			if (flrAuftragPos != null && flrAuftragPos.getT_uebersteuerterliefertermin() != null) {
				liefertermin = flrAuftragPos.getT_uebersteuerterliefertermin();
			}

			Timestamp abliefertermin = getAusliefervorschlagFac().umKundenlieferdauerVersetzen(theClientDto,
					(Date) Helper.cutDate(liefertermin), flrauftrag.getFlrkunde().getI_lieferdauer());
			return abliefertermin.getTime();
		}

		private void setDescriptionByLocKonzern(OffeneAgsFLRDataDto flrDataEntry, Integer artikelIId) {
			try {
				ArtikelsprDto artikelsprDto = getArtikelFac().artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelIId,
						theClientDto.getLocKonzernAsString(), theClientDto);
				if (artikelsprDto != null) {
					flrDataEntry.setPartlistItemDescription(artikelsprDto.getCBez());
					flrDataEntry.setPartlistItemShortDescription(artikelsprDto.getCKbez());
				}
			} catch (RemoteException e) {
			}
		}

		private BigDecimal calculateOpenQuantity(FLRLos los) {
			try {
				LosablieferungDto[] losablieferungen = getFertigungFac().losablieferungFindByLosIId(los.getI_id(),
						false, theClientDto);
				if (losablieferungen == null)
					return los.getN_losgroesse();

				BigDecimal summe = BigDecimal.ZERO;
				for (LosablieferungDto dto : losablieferungen) {
					summe = summe.add(dto.getNMenge());
				}

				return summe.compareTo(los.getN_losgroesse()) >= 0 ? BigDecimal.ZERO
						: los.getN_losgroesse().subtract(summe);
			} catch (RemoteException e) {
				return los.getN_losgroesse();
			}
		}

		private class LoszeitenSum implements IInject<AuftragzeitenDto, BigDecimal> {
			private Integer losId;

			public LoszeitenSum(Integer losId) {
				this.losId = losId;
			}

			@Override
			public BigDecimal inject(BigDecimal sum, AuftragzeitenDto each) {
				return losId.equals(each.getBelegpositionIId())
						? sum.add(new BigDecimal(each.getDdDauer().doubleValue()))
						: sum;
			}
		}

		private BigDecimal calculateActualTime(final FLROffeneags loszeit) {
			if (!hasFeatureIstzeit())
				return BigDecimal.ZERO;

			ActualTimeEntry entry = cachedTimes.getValueOfKey(loszeit.getFlrlos().getI_id());
			if (entry == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new NullPointerException(
						"ActualTimeEntry, losid " + loszeit.getFlrlos().getI_id() + " unbekannt?"));
			}
			if (entry.hasExcessiveEntries() || entry.hasErrors()) {
				return null;
			}

			LoszeitenSum calculator = new LoszeitenSum(loszeit.getI_id());
//			BigDecimal sumHuman = CollectionTools.inject(BigDecimal.ZERO,
//					entry.getHumanTimes(), calculator);
			BigDecimal sumMachine = CollectionTools.inject(BigDecimal.ZERO, entry.getMachineTimes(), calculator);

			return sumMachine;// sumHuman.add(sumMachine);
		}

		/**
		 * Ermitteln, ob der aktuelle Arbeitsgang eine Verschiebung des Los-Beginns
		 * zul&auml;sst</br>
		 * <p>
		 * Der Losbeginn ist verschiebbar wenn es sich um den ersten Arbeitsgang
		 * handelt, und noch keine Arbeitszeit f&uuml;r alle AG angefallen ist, und noch
		 * kein AG als "fertig" markiert worden ist
		 * </p>
		 * 
		 * @param loszeit
		 * @return true wenn der Los-Beginn verschiebbar ist, ansonsten false
		 */
		private boolean calculateStartMoveable(final FLROffeneags loszeit) {
			LosMoveableEntry entry = cachedSollarbeitsplan.getValueOfKey(loszeit.getFlrlos().getI_id());
			if (entry == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new NullPointerException(
						"Sollarbeitsplan, losid " + loszeit.getFlrlos().getI_id() + " unbekannt?"));
			}

			boolean moveable = entry.isMoveable();
			if (moveable) {
				LossollarbeitsplanDto[] dtos = entry.getSollarbeitsplanDtos();
				moveable = loszeit.getI_arbeitsgangsnummer().equals(dtos[0].getIArbeitsgangnummer());
			}

			if (moveable) {
				myLogger.info("Los '" + loszeit.getFlrlos().getC_nr() + ", AG " + loszeit.getI_arbeitsgangsnummer()
						+ "' Beginn ist verschiebbar");
			}
			return moveable;
		}

		/**
		 * Ermitteln, ob der aktuelle Arbeitsgang eine Verschiebung des Los-Beginns
		 * zul&auml;sst</br>
		 * <p>
		 * Der Losbeginn ist verschiebbar wenn es sich um den ersten Arbeitsgang
		 * handelt, und noch keine Arbeitszeit f&uuml;r alle AG angefallen ist, und noch
		 * kein AG als "fertig" markiert worden ist
		 * </p>
		 * 
		 * @param loszeit
		 * @return true wenn der Los-Beginn verschiebbar ist, ansonsten false
		 */
		private boolean calculateFinishMoveable(final FLROffeneags loszeit) {
			LosMoveableEntry entry = cachedSollarbeitsplan.getValueOfKey(loszeit.getFlrlos().getI_id());
			if (entry == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new NullPointerException(
						"Sollarbeitsplan, losid " + loszeit.getFlrlos().getI_id() + " unbekannt?"));
			}

			boolean moveable = entry.isMoveable();
			if (moveable) {
				LossollarbeitsplanDto[] dtos = entry.getSollarbeitsplanDtos();
				int lastIndex = dtos.length - 1;
				moveable = loszeit.getI_arbeitsgangsnummer().equals(dtos[lastIndex].getIArbeitsgangnummer());
			}

			if (moveable) {
				myLogger.info("Los '" + loszeit.getFlrlos().getC_nr() + ", AG " + loszeit.getI_arbeitsgangsnummer()
						+ "' Ende ist verschiebbar");
			}
			return moveable;
		}
	}

	private Feature getFeature() {
		if (cachedFeature == null) {
			cachedFeature = new Feature();
		}
		return cachedFeature;
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		cachedFeature = null;

		int colCount = getTableInfo().getColumnClasses().length;
		int pageSize = getLimit();
		int startIndex = getStartIndex(rowIndex, pageSize);
		int endIndex = startIndex + pageSize - 1;

		session = factory.openSession();
		session = setFilter(session);
		String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
		Query query = session.createQuery(queryString);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		Object[][] rows = new Object[resultList.size()][colCount + 1];
		int row = 0;
		int col = 0;

		Map<Integer, String> m = getFertigungFac().getAllZusatzstatus(theClientDto);

		getFeature().setFlrRowCount(rows.length);

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			FLROffeneags loszeit = (FLROffeneags) o[0];

			FLRArtikellistespr artikellistespr_taetigkeit = (FLRArtikellistespr) o[1];
			FLRArtikellistespr artikellistespr_stueckliste = (FLRArtikellistespr) o[2];
			FLRArtikellistespr artikellistespr_sollmaterial = (FLRArtikellistespr) o[3];

			rows[row][col++] = loszeit.getI_id();

			if (loszeit.getFlrkunde() != null) {
				rows[row][col++] = loszeit.getFlrkunde().getFlrpartner().getC_kbez();
				rows[row][col++] = loszeit.getFlrkunde().getC_abc();
			} else {
				rows[row][col++] = null;
				rows[row][col++] = null;
			}

			rows[row][col++] = loszeit.getFlrlos().getC_nr();
			rows[row][col++] = loszeit.getFlrlos().getN_losgroesse();

			if (loszeit.getFlrartikel_stueckliste() != null) {
				rows[row][col++] = loszeit.getFlrartikel_stueckliste().getC_nr();

				if (artikellistespr_stueckliste != null) {
					rows[row][col++] = artikellistespr_stueckliste.getC_bez();
				} else {
					rows[row][col++] = null;
				}

			} else {
				rows[row][col++] = null;
				rows[row][col++] = null;
			}

			rows[row][col++] = loszeit.getI_arbeitsgangsnummer();
			rows[row][col++] = loszeit.getFlrartikel_taetigkeit().getC_nr();
			if (artikellistespr_taetigkeit != null) {
				rows[row][col++] = artikellistespr_taetigkeit.getC_bez();
			} else {
				rows[row][col++] = null;
			}

			if (Helper.short2boolean(loszeit.getFlrlossollarbeitsplan().getB_nurmaschinenzeit()) == false) {

				rows[row][col++] = loszeit.getN_gesamtzeit();
			} else {
				rows[row][col++] = null;
			}
			try {
				if (m.size() > 0) {

					DoppelIcon ia = new DoppelIcon();

					Object[] oIcon1 = getStatusMitUebersetzung(loszeit.getFlrlos().getStatus_c_nr());

					if (oIcon1 != null) {

						if (oIcon1.length > 0) {
							ia.setIcon1((String) oIcon1[0]);
						}
						if (oIcon1.length > 1) {
							ia.setTooltip1((String) oIcon1[1]);
						}

					}
					// Juengsten Zusatzstatus holen

					Integer zusatzstatusIId = getFertigungFac()
							.getJuengstenZusatzstatuseinesLoses(loszeit.getFlrlos().getI_id());

					if (zusatzstatusIId != null) {
						m.get(zusatzstatusIId);

						String zusatzstatus = Helper.fitString2Length(m.get(zusatzstatusIId), 15, ' ');

						ia.setIcon2(zusatzstatus);
						ia.setTooltip2(zusatzstatus);
					}

					rows[row][col++] = ia;
				} else {

					rows[row][col++] = getStatusMitUebersetzung(loszeit.getFlrlos().getStatus_c_nr());

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			rows[row][col++] = loszeit.getT_agbeginn();
			rows[row][col++] = loszeit.getI_maschinenversatz_ms();

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(loszeit.getT_agbeginn().getTime());

			rows[row][col++] = c.get(Calendar.WEEK_OF_YEAR) + "/" + Helper.berechneJahrDerKW(c);

			if (loszeit.getFlrmaschine() != null) {
				rows[row][col++] = loszeit.getFlrmaschine().getC_identifikationsnr();
				rows[row][col++] = loszeit.getFlrmaschine().getC_bez();
				rows[row][col++] = loszeit.getN_gesamtzeit();
			} else {
				rows[row][col++] = null;
				rows[row][col++] = null;
				rows[row][col++] = null;
			}

			if (loszeit.getFlrartikel_sollmaterial() != null) {
				rows[row][col++] = loszeit.getFlrartikel_sollmaterial().getC_nr();

				if (artikellistespr_sollmaterial != null) {
					rows[row][col++] = artikellistespr_sollmaterial.getC_bez();
				} else {
					rows[row][col++] = null;
				}

			} else {
				rows[row][col++] = null;
				rows[row][col++] = null;
			}
			rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getC_kommentar();

			rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getF_fortschritt();

			rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getI_reihung();

			if (loszeit.getI_anzahlzeitdaten() != null && loszeit.getI_anzahlzeitdaten() > 0) {
				rows[row][col++] = new Color(0, 153, 51);// GRUEN
			} else {
				rows[row][col++] = null;
			}

			rows[row][col++] = loszeit.getFlrmaschine() != null ? loszeit.getFlrmaschine().getI_id() : null;

			getFeature().buildFeatureData(row, o);
			if (getFeature().hasFeatureIstzeit()) {
				BigDecimal d = getFeature().getFlrDataEntry(row).getActualTime();
				if (d != null && d.signum() != 0) {
					rows[row][17] = ((BigDecimal) loszeit.getN_gesamtzeit()).subtract(d);
				} else {
					rows[row][17] = loszeit.getN_gesamtzeit();
				}
			}
			row++;
			col = 0;
		}

		if (getFeature().hasFeatureAuftragsDatum()) {
			OffeneAgsQueryResult offeneAgsResult = new OffeneAgsQueryResult(rows, getRowCount(), startIndex, endIndex,
					0);
			offeneAgsResult.setFlrData(getFeature().getFlrData());
			result = offeneAgsResult;
		} else {
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		}

		return result;
	}

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);
			String queryString = "select count(*) " + this.getFromClause() + this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} finally {
			closeSession(session);
		}
		return rowCount;
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

					if (filterKriterien[i].kritName.equals("MASCHINE_GRUPPE")) {

						if (filterKriterien[i].value.startsWith("M")) {

							where.append(" flrmaschine.i_id=" + filterKriterien[i].value.substring(1));

						} else if (filterKriterien[i].value.startsWith("G")) {
							where.append(" flrmaschine.maschinengruppe_i_id=" + filterKriterien[i].value.substring(1));
						}

					} else

					if (filterKriterien[i].kritName.equals("flrmaschine.c_identifikationsnr")) {
						where.append(" (lower(flrmaschine.c_identifikationsnr)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(flrmaschine.c_bez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase() + ")");
					} else if (filterKriterien[i].kritName.equals("flroffeneags.flrlos." + FertigungFac.FLR_LOS_C_NR)) {
						try {

							FilterKriterium fkLocal = new FilterKriterium(
									filterKriterien[i].kritName.replace("flroffeneags.", ""), filterKriterien[i].isKrit,
									filterKriterien[i].value, filterKriterien[i].operator,
									filterKriterien[i].isBIgnoreCase());

							LpBelegnummerFormat f = getBelegnummerGeneratorObj()
									.getBelegnummernFormat(theClientDto.getMandant());
							LpDefaultBelegnummerFormat defaultFormat = (LpDefaultBelegnummerFormat) f;

							String postfix = "-___";
							boolean bPostfixVorhanden = false;
							if (fkLocal.value != null && fkLocal.value.indexOf("-") > -1) {
								postfix = fkLocal.value.substring(fkLocal.value.indexOf("-"));
								fkLocal.value = fkLocal.value.substring(0, fkLocal.value.indexOf("-")) + "'";
								bPostfixVorhanden = true;
							}

							String sValue = super.buildWhereBelegnummer(fkLocal, false);

							if (bLosnummerAuftragsbezogen >= 1) {
								if (sValue != null) {
									sValue = sValue.substring(0, defaultFormat.getStellenGeschaeftsjahr() + 2) + "_"
											+ sValue.substring(defaultFormat.getStellenGeschaeftsjahr() + 3);
								}
							}

							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRLos", sValue)) {
								sValue = super.buildWhereBelegnummer(fkLocal, true);

								if (bLosnummerAuftragsbezogen >= 1) {
									if (sValue != null) {
										sValue = sValue.substring(0, defaultFormat.getStellenGeschaeftsjahr() + 2) + "_"
												+ sValue.substring(defaultFormat.getStellenGeschaeftsjahr() + 3);
									}
								}

							}

							if (bLosnummerAuftragsbezogen == 0) {
								where.append(" " + fkLocal.kritName);
								where.append(" " + fkLocal.operator);
								where.append(" " + sValue + "");
							} else {

								int iStellenGesamtAuftragsbezogen = defaultFormat.getStellenGeschaeftsjahr()
										+ defaultFormat.getStellenLfdNummer() + 5;

								// Losnummer kann maximal 12-stellig sein
								if (defaultFormat.getStellenGeschaeftsjahr() == 2) {
									if (iStellenGesamtAuftragsbezogen > 12) {

										iStellenGesamtAuftragsbezogen = 12;

									}
								}
								// SP2765
								if (defaultFormat.getStellenGeschaeftsjahr() == 4) {
									if (iStellenGesamtAuftragsbezogen > 13) {

										iStellenGesamtAuftragsbezogen = 13;

									}
								}

								String losnummer = super.buildWhereBelegnummer(fkLocal, false);
								String valueBelegnummernfilterAuftragsbezogen = losnummer.substring(0,
										defaultFormat.getStellenGeschaeftsjahr() + 2);
								postfix = postfix.replaceAll("'", "");
								String krit = null;
								if (fkLocal.value.indexOf(",") > 0) {
									krit = fkLocal.value.substring(0, fkLocal.value.indexOf(",")).replaceAll("%", "")
											.replaceAll("'", "") + postfix;
								} else {
									krit = fkLocal.value.replaceAll("%", "").replaceAll("'", "") + postfix;
								}

								valueBelegnummernfilterAuftragsbezogen = Helper.fitString2Length(
										valueBelegnummernfilterAuftragsbezogen,
										iStellenGesamtAuftragsbezogen - krit.length() + 1, '0') + krit + "'";

								if (!istBelegnummernInJahr("FLRLos", valueBelegnummernfilterAuftragsbezogen)
										&& !istBelegnummernInJahr("FLRLos",
												valueBelegnummernfilterAuftragsbezogen.replaceFirst("0", "").substring(
														0, valueBelegnummernfilterAuftragsbezogen.length() - 2)
														+ "_'")) {
									losnummer = super.buildWhereBelegnummer(fkLocal, true);
									valueBelegnummernfilterAuftragsbezogen = losnummer.substring(0,
											defaultFormat.getStellenGeschaeftsjahr() + 2);

									if (fkLocal.value.indexOf(",") > 0) {
										krit = fkLocal.value.substring(0, fkLocal.value.indexOf(","))
												.replaceAll("%", "").replaceAll("'", "") + postfix;
									} else {
										krit = fkLocal.value.replaceAll("%", "").replaceAll("'", "") + postfix;
									}

									valueBelegnummernfilterAuftragsbezogen = Helper.fitString2Length(
											valueBelegnummernfilterAuftragsbezogen,
											iStellenGesamtAuftragsbezogen - krit.length() + 1, '0') + krit + "'";

								}

								// Wenn '-' vorhanden, dann nur nach
								// Auftragsbezogenen Nummer suchen
								if (bPostfixVorhanden) {
									where.append(" " + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" " + valueBelegnummernfilterAuftragsbezogen + "");
								} else {
									where.append("(");
									where.append(" (" + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" " + sValue + " OR ");
									where.append(" " + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" " + valueBelegnummernfilterAuftragsbezogen + " OR ");
									where.append(" " + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" " + valueBelegnummernfilterAuftragsbezogen.replaceFirst("0", "")
											.substring(0, valueBelegnummernfilterAuftragsbezogen.length() - 2) + "_'"
											+ ") ");

									where.append(" OR ");
									fkLocal.kritName = FertigungFac.FLR_LOSREPORT_FLRAUFTRAG + ".c_nr";
									whereAuftrag(where, fkLocal);
									where.append(")");
								}

							}

						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else {

						if (filterKriterien[i].isBIgnoreCase()) {

							where.append(" lower(" + filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + filterKriterien[i].kritName);
						}

						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toLowerCase());
						} else {
							where.append(" " + filterKriterien[i].value);
						}
					}

				}
			}

			if (getFeature().hasFeatureNurNaechsterAg()) {
				if (filterAdded)
					where.append(" AND ");
				where.append(
						" flroffeneags.i_arbeitsgangsnummer = ( SELECT min(innerflroffeneags.i_arbeitsgangsnummer) "
								+ " FROM FLROffeneags innerflroffeneags"
								+ " WHERE flroffeneags.los_i_id=innerflroffeneags.los_i_id" + " )) ");
				filterAdded = true;
			}

			if (filterAdded) {
				where.insert(0, " WHERE");
			}
		}

		return where.toString();
	}

	private void whereAuftrag(StringBuffer where, FilterKriterium filterKriterium) {
		try {

			String sValue = super.buildWhereBelegnummer(filterKriterium, false);

			// Belegnummernsuche auch in "altem" Jahr, wenn im
			// neuen noch keines vorhanden ist
			if (!istBelegnummernInJahr("FLRAuftrag", sValue)) {
				sValue = super.buildWhereBelegnummer(filterKriterium, true);
			}
			where.append(" " + filterKriterium.kritName);
			where.append(" " + filterKriterium.operator);
			where.append(" " + sValue);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
		}
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
				orderBy.append(FLR_OFFENEAGS).append("t_agbeginn ASC, flroffeneags.i_maschinenversatz_ms ASC");
				sortAdded = true;
			}
			if (orderBy.indexOf("flroffeneags.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" flroffeneags.i_id ");
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
		return FLR_LOSMAT_FROM_CLAUSE;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				session = setFilter(session);
				String queryString = "select " + FLR_OFFENEAGS + FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ID
						+ FLR_LOSMAT_FROM_CLAUSE + this.buildWhereClause() + this.buildOrderByClause();
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
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
			} finally {
				closeSession(session);
			}
		}

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = this.getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();

			try {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
				bLosnummerAuftragsbezogen = (Integer) parameter.getCWertAsObject();
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class, String.class, BigDecimal.class,
							String.class, String.class, Integer.class, String.class, String.class, BigDecimal.class,
							Icon.class, java.util.Date.class, Integer.class, String.class, String.class, String.class,
							BigDecimal.class, String.class, String.class, String.class, Double.class, Integer.class,
							Color.class },
					new String[] { "i_id", getTextRespectUISpr("lp.kunde", mandantCNr, locUI), "ABC",
							getTextRespectUISpr("lp.losnr", mandantCNr, locUI),
							getTextRespectUISpr("fert.losgroesse", mandantCNr, locUI),
							getTextRespectUISpr("fert.offeneags.artikelnummerstkl", mandantCNr, locUI),
							getTextRespectUISpr("fert.offeneags.bezeichnungstkl", mandantCNr, locUI),
							getTextRespectUISpr("fert.ag", mandantCNr, locUI),
							getTextRespectUISpr("fert.offeneags.artikelnummertaetigkeit", mandantCNr, locUI),
							getTextRespectUISpr("fert.offeneags.bezeichnungtaetigkeit", mandantCNr, locUI)
									+ "Taetigkeit",
							getTextRespectUISpr("lp.dauer", mandantCNr, locUI),
							getTextRespectUISpr("lp.status", mandantCNr, locUI),
							getTextRespectUISpr("fert.agbeginn", mandantCNr, locUI),
							getTextRespectUISpr("fert.offeneags.versatzms", mandantCNr, locUI),
							getTextRespectUISpr("lp.kw", mandantCNr, locUI),

							getTextRespectUISpr("is.maschine", mandantCNr, locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
							getTextRespectUISpr("lp.dauer", mandantCNr, locUI),
							getTextRespectUISpr("fert.offeneags.artikelnummermaterial", mandantCNr, locUI),
							getTextRespectUISpr("fert.offeneags.bezeichnungmaterial", mandantCNr, locUI),
							getTextRespectUISpr("lp.kommentar", mandantCNr, locUI),
							getTextRespectUISpr("fert.offeneags.fortschritt", mandantCNr, locUI),
							getTextRespectUISpr("fert.reihung", mandantCNr, locUI) },

					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, 0 },
					new String[] { FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ID, "flrkunde.flrpartner.c_kbez",
							"flrkunde.c_abc", "flroffeneags.flrlos.c_nr", "flroffeneags.flrlos.n_losgroesse",
							"stueckliste.c_nr", "aspr_stueckliste.c_bez",
							"flroffeneags." + FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER,
							"taetigkeit.c_nr", "aspr_taetigkeit.c_bez",
							"flroffeneags." + FertigungFac.FLR_LOSSOLLARBEITSPLAN_N_GESAMTZEIT,
							"flroffeneags.flrlos.status_c_nr", "flroffeneags.t_agbeginn",
							"flroffeneags.i_maschinenversatz_ms", Facade.NICHT_SORTIERBAR,

							"flrmaschine.c_identifikationsnr", "flrmaschine.c_bez",
							"flroffeneags." + FertigungFac.FLR_LOSSOLLARBEITSPLAN_N_GESAMTZEIT, "sollmaterial.c_nr",
							"aspr_sollmaterial.c_bez", "sollarbeitsplan.c_kommentar", Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR, "" }));

		}

		return super.getTableInfo();
	}
}
