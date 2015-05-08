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
package com.lp.server.system.ejbfac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.hibernate.Session;

import com.lp.server.angebotstkl.service.AgstklImportSpezifikation;
import com.lp.server.angebotstkl.service.EinkaufsagstklImportSpezifikation;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.stueckliste.ejbfac.IImportColumnQueryBuilder;
import com.lp.server.stueckliste.ejbfac.ImportQueryBuilder;
import com.lp.server.stueckliste.ejbfac.StklImportSearchHelper;
import com.lp.server.stueckliste.service.CondensedResultList;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.stueckliste.service.FertigungsStklImportSpezifikation;
import com.lp.server.stueckliste.service.StklImportResult;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.IntelligenterStklImportFac;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.BelegpositionDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.Pair;

@Stateless
/**
 * 
 * @author andi
 *
 */
public class IntelligenterStklImportFacBean extends Facade implements
		IntelligenterStklImportFac {

	@Override
	public List<IStklImportResult> searchForImportMatches(
			StklImportSpezifikation spez, List<String> importLines,
			int rowIndex, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		
		List<List<String>> allColumnValues = convertToSeparatedFields(spez,
				importLines, rowIndex);
		allColumnValues = removeBlankLines(allColumnValues);
		if(allColumnValues.isEmpty()) {
			return new ArrayList<IStklImportResult>();
		}
		
		List<Map<String, String>> valueForDefinitionPerRow = new ArrayList<Map<String, String>>();

		NumberFormat format = DecimalFormat.getNumberInstance(theClientDto
				.getLocUi());

		for (List<String> columnValues : allColumnValues) {
			Map<String, String> valueForDefinition = new HashMap<String, String>();
			for (int i = 0; i < columnValues.size()
					&& i < spez.getColumnTypes().size(); i++) {
				String type = spez.getColumnTypes().get(i);
				if (StklImportSpezifikation.MENGE.equals(type)) {
					Number menge;
					try {
						menge = format.parse(columnValues.get(i).trim());
					} catch (ParseException e) {
						menge = BigDecimal.ONE;
						e.printStackTrace();
						// throw new
						// EJBExceptionLP(EJBExceptionLP.FEHLER_FORMAT_NUMBER,
						// "Menge " + columnValues.get(i).trim() + " in Zeile "
						// + i + " ist keine gueltige Zahl");
					}
					valueForDefinition.put(type, menge.toString());
				} else if (!StklImportSpezifikation.UNDEFINED.equals(type)) {
					valueForDefinition.put(type, columnValues.get(i).trim());
				}
			}
			valueForDefinitionPerRow.add(valueForDefinition);
		}

		List<IStklImportResult> results = new ArrayList<IStklImportResult>();
		StklImportSearchHelper searchHelper = getSearchHelper(spez, theClientDto);
		
		for (Map<String, String> valueForDefinition : valueForDefinitionPerRow) {
			searchHelper.setValues(valueForDefinition);
			results.add(searchByColumnValues(searchHelper));
			if (valueForDefinition.get(StklImportSpezifikation.MENGE) == null) {
				valueForDefinition.put(StklImportSpezifikation.MENGE,
						BigDecimal.ONE.toString());
			}
		}

		return results;
	}

	/**
	 * Konvertiert die Rohdaten der St&uuml;ckliste in getrennte Felder.
	 * 
	 * @param spez Spezifikation der ImportResults
	 * @param importLines Zeilen der Importdatei als Rohdaten
	 * @param rowIndex Nummer der Zeile in der Datei welche 
	 * <code>importLines.get(0)</code> entspricht.
	 * @return Liste der in getrennte Felder geteilten Zeilen
	 */
	protected List<List<String>> convertToSeparatedFields(
			StklImportSpezifikation spez, List<String> importLines, int rowIndex) {
		List<List<String>> allColumnValues = new ArrayList<List<String>>();
		int fromRow = spez.getFromRow() > rowIndex ? spez.getFromRow() - 1
				- rowIndex : 0;
		for (int i = fromRow; i < importLines.size(); i++) {
			String line = importLines.get(i);

			List<String> fields = new ArrayList<String>();
			if (spez.isFixedWidth()) {
				int lastCol = 0;
				for (Integer w : spez.getWidths()) {
					fields.add(line.length() > w ? line.substring(lastCol, w)
							: "");
					lastCol = w;
				}
			} else {
				List<String> fieldsWithQuoMark = Arrays.asList(line.split(spez
						.getSeparator() + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
				for (String field : fieldsWithQuoMark) {
					fields.add(field.replaceAll("\"(?!\")", ""));
				}
			}
			allColumnValues.add(fields);
		}
		return allColumnValues;
	}
	
	/**
	 * L&ouml;scht alle leeren Zeilen einer Liste von Zeilen mit String-Feldern.
	 * Felder nur mit Leerzeichen werden als leer erkannt.
	 * 
	 * @param list die von Leerzeilen befreit werden soll
	 * @return bereinigte Liste, kann auch leer sein
	 */
	protected List<List<String>> removeBlankLines(List<List<String>> list) {
		
		for(Iterator<List<String>> iter = list.listIterator(); iter.hasNext(); ) {
			List<String> row = iter.next();
			boolean rowIsEmpty = true;
			
			for(int i = 0; i < row.size(); i++) {
				if(!row.get(i).trim().isEmpty()) {
					rowIsEmpty = false;
					break;
				}
			}
			
			if(rowIsEmpty) {
				iter.remove();
			}
		}
		return list;
	}

	/**
	 * Erstellt die grundlegenden FROM und WHERE Klauseln und startet die Artikelsuche
	 * f&uuml;r die aktuelle Zeile. Alle n&ouml;tigen Variablen sind im SearchHelper
	 * gespeichert.
	 * 
	 * @param searchHelper Hilfsobjekt f&uuml;r die Artikelsuche
	 * @return das Result der Suche des aktuellen Artikels
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	protected IStklImportResult searchByColumnValues(StklImportSearchHelper searchHelper) 
			throws RemoteException, EJBExceptionLP {
		String artikel = IImportColumnQueryBuilder.Artikel;
		String queryFrom = "SELECT " + artikel + ".i_id FROM FLRArtikelliste "
				+ artikel;
		String mandant = searchHelper.getTheClientDto().getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, searchHelper.getTheClientDto())) {
			mandant = getSystemFac().getHauptmandant();
		}

		String queryWhere = artikel + ".mandant_c_nr='"
				// + theClientDto.getMandant() + "' AND " + artikel
				+ mandant + "' AND " + artikel + ".artikelart_c_nr='"
				+ ArtikelFac.ARTIKELART_ARTIKEL + "'";

		searchHelper.setBasicQueryFrom(queryFrom);
		searchHelper.setBasicQueryWhere(queryWhere);
		
		return searchByColumn(searchHelper);
	}

	/**
	 * Liefert den entsprechenden SearchHelper je nach Typ der St&uuml;ckliste und setzt
	 * die individuellen Variablen.
	 * 
	 * @param spez Spezifikation der ImportResults
	 * @param theClientDto der aktuelle Benutzer
	 * @return den nach St&uuml;cklistentyp entsprechenen StklImportSearchHelper
	 * 
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	private StklImportSearchHelper getSearchHelper(StklImportSpezifikation spez, TheClientDto theClientDto) 
			throws RemoteException, EJBExceptionLP {
		Pair<IImportHead, IImportPositionen> importerPair = getImporterBeans(spez);
		Integer kundeIId = importerPair.getKey().getKundeIIdDerStueckliste(spez, theClientDto);

		return new StklImportSearchHelper(spez, theClientDto, kundeIId);
	}

	/**
	 * Sucht f&uuml;r den aktuellen Artikel der St&uuml;ckliste nach passenden
	 * Artikeln in der Datenbank. Die Reihenfolge der Suchanfragen ist im SearchHelper
	 * gespeichert. Die gefundenen Artikeln werden dem StklImportResult hinzugef&uuml;gt
	 * 
	 * @param searchHelper Hilfsobjekt f&uuml;r die Artikelsuche, &uuml;ber das auch die
	 * Queries bezogen werden.
	 * @return das fertige StklImportResult mit den gefundenen Artikeln
	 */
	protected IStklImportResult searchByColumn(StklImportSearchHelper searchHelper) {
		Session session = FLRSessionFactory.getFactory().openSession();

		List<Integer> artikelIIds = new ArrayList<Integer>();
		IStklImportResult result = new StklImportResult();

		for (String type : searchHelper.getColumnPriorityOrder()) {
			String value = searchHelper.getValues().get(type);
			if (value == null || value.isEmpty())
				continue;

			List<String> wheres = searchHelper.getWhereQuery(type);
			if (wheres == null)
				continue;

			List<String> froms = searchHelper.getFromQuery(type);

			@SuppressWarnings("unchecked")
			// geht leider nicht anders
			List<Integer> list = session.createQuery(
					ImportQueryBuilder.buildQuery(froms, wheres)).list();
			if (list.size() == 1) {
				artikelIIds.add(list.get(0));
				result.setTotalMatch(searchHelper.getImportColumns().get(type).isTotalMatch());
				break;
			} else if (list.size() > 1) {
				artikelIIds.addAll(list);
				break;
//				List<String> subTypes = queryBuilder
//						.getDeeperColumnQueryBuilders();
//				if (subTypes != null)
//					result = searchByColumn(subTypes, values, spez, froms,
//							wheres, theClientDto);
//				if (result.getFoundItems() != null
//						&& result.getFoundItems().size() > 0) {
//					return result;
//				} else {
//					artikelIIds.addAll(list);
//					break;
//				}
			}
		}
		
		List<ArtikelDto> artikelliste = new ArrayList<ArtikelDto>();
		if(artikelIIds.size() < MAX_ARTIKEL_FUER_AUSWAHL) {
			for (Integer iId : artikelIIds) {
				artikelliste.add(getArtikelFac().artikelFindByPrimaryKey(iId,
						searchHelper.getTheClientDto()));
			}
		} else {
			result.setFoundTooManyArticles(true);
		}
		result.setFoundItems(artikelliste);
		
		if (artikelIIds.size() == 1 && result.isTotalMatch()) {
			result.setSelectedIndex(0);
		}
		result.setValues(searchHelper.getValues());
		
		return result;
	}

	@Override
	public int importiereImportResultsAlsBelegpositionen(
			StklImportSpezifikation spez, List<IStklImportResult> results,
			Boolean updateArtikel, TheClientDto theClientDto) 
			throws RemoteException, EJBExceptionLP {

		Validator.notNull(spez, "spez");
		Validator.notNull(results, "results");
		Validator.notNull(updateArtikel, "updateArtikel");
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notNull(spez.getStklIId(), "spez.getStklIId()");
		
		boolean bZentralerArtikelstamm = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);
		List<BelegpositionDto> posDtos = new ArrayList<BelegpositionDto>();
		Pair<IImportHead, IImportPositionen> importerPair = getImporterBeans(spez);
		IImportPositionen positionImporter = importerPair.getValue();
		IImportHead headImporter = importerPair.getKey();
		
		Integer kundeIId = headImporter.getKundeIIdDerStueckliste(spez, theClientDto);
		
		if(results instanceof CondensedResultList) {
			results = ((CondensedResultList) results).convertToNormalList();
		}
		
		for(IStklImportResult result : results) {
			BelegpositionDto posDto = positionImporter.getNewPositionDto();

			ArtikelDto artikelDto = result.getSelectedArtikelDto();
			if(artikelDto == null) {
				artikelDto = new ArtikelDto();
				artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);
			}
			
			posDto.setBelegIId(spez.getStklIId());
			String menge = result.getValues().get(StklImportSpezifikation.MENGE);
			posDto.setNMenge(new BigDecimal(menge == null ? "1" : menge));
			if(ArtikelFac.ARTIKELART_HANDARTIKEL.equals(artikelDto.getArtikelartCNr())) {
				posDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
				posDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
				posDto.setCBez(Helper.cutString(getHandartikelCBez(result), 
						StuecklisteFac.FieldLength.STUECKLISTEPOSITION_CBEZ));
			} else {
				posDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
				posDto.setCBez(artikelDto.getArtikelsprDto().getCBez());
				posDto.setArtikelIId(artikelDto.getIId());
				posDto.setEinheitCNr(artikelDto.getEinheitCNr());

				if(bZentralerArtikelstamm) {
					updateArtikel(result, updateArtikel, theClientDto);
				}
				updateKundesoko(result, kundeIId, theClientDto);
			}
			
			posDto = positionImporter.preparePositionDtoAusImportResult(
					posDto, spez, result, theClientDto);
			posDtos.add(posDto);
		}

		positionImporter.createPositions(posDtos, theClientDto);
		return posDtos.size();
	}

	/**
	 * Aktualisiert den Artikel, der im ImportResult ausgew&auml;hlt wurde, im
	 * Artkelstamm, wenn sich die Herstellerbezeichnung oder die 
	 * Herstellerartikelnummer unterscheiden.
	 * 
	 * @param result aktuelles ImportResult
	 * @param updateNeeded true, wenn der Artikel aktualisiert werden soll
	 * @param theClientDto der aktuelle Benutzer
	 * @throws RemoteException 
	 */
	private void updateArtikel(IStklImportResult result,
			Boolean updateNeeded, TheClientDto theClientDto) throws RemoteException {
		if (!updateNeeded ||
				!ArtikelFac.ARTIKELART_ARTIKEL.equals(
						result.getSelectedArtikelDto().getArtikelartCNr()))
			return;

		getArtikelFac().updateArtikelAusImportResult(result, theClientDto);
	}

	/**
	 * Zust&auml;ndig f&uuml;r das Updaten eines Artikels und des KundeSoko-Eintrags
	 * 
	 * Ist das KundeSoko-Update aktiviert und eine Kundenartikelnummer vorhanden, 
	 * wird, bei bestehendem KundeSoko-Eintrag, dieser aktualisiert. Ist noch keiner
	 * vorhanden, wird ein neuer angelegt.
	 * 
	 * @param result aktuelles ImportResult
	 * @param kundeIId IId des Kunden, dem die St&uuml;ckliste zugeordnet ist
	 * @param theClientDto der aktuelle Benutzer
	 * 
	 * @throws RemoteException
	 */
	private void updateKundesoko(IStklImportResult result, Integer kundeIId,
			TheClientDto theClientDto) throws RemoteException {
		if (!ArtikelFac.ARTIKELART_ARTIKEL.equals(result.getSelectedArtikelDto().getArtikelartCNr()))
			return;

		String kundeArtikelCNr = result.getValues().get(
				StklImportSpezifikation.KUNDENARTIKELNUMMER);

		if (result.getSokoUpdate() && kundeArtikelCNr != null 
				&& !kundeArtikelCNr.isEmpty() && kundeIId != null) {
			getKundesokoFac().updateKundesokoOrCreateIfNotExist(kundeIId, result.getSelectedArtikelDto().getIId(), 
					kundeArtikelCNr, theClientDto);
		}
	}

	/**
	 * Liefert die nach der Art der St&uuml;ckliste betreffende Beans als
	 * Importer-Pair f&uuml;r die Kopf- und Positionsdaten
	 * 
	 * @param spez Spezifikation der ImportResult
	 * @return Importer-Pair f&uuml;r Kopf- und Positionsdaten
	 */
	private Pair<IImportHead, IImportPositionen> getImporterBeans(StklImportSpezifikation spez) {
		Pair<IImportHead, IImportPositionen> importerPair = 
				new Pair<IImportHead, IImportPositionen>(null, null);
		
		if(spez instanceof FertigungsStklImportSpezifikation) {
			importerPair = new Pair<IImportHead, IImportPositionen>(getStuecklisteFac().asHeadImporter(), 
					getStuecklisteFac().asPositionImporter()) ;
		} else if(spez instanceof EinkaufsagstklImportSpezifikation) {
			importerPair = new Pair<IImportHead, IImportPositionen>(getAngebotstklFac().asHeadImporter(), 
					getAngebotstklFac().asPositionImporter()) ;
		} else if(spez instanceof AgstklImportSpezifikation) {
			importerPair = new Pair<IImportHead, IImportPositionen>(getAngebotstklpositionFac().asHeadImporter(), 
					getAngebotstklpositionFac().asPositionImporter()) ;
		}
		Validator.notNull(importerPair.getKey(), "positionImporter");
		Validator.notNull(importerPair.getValue(), "headImporter");
		
		return importerPair;
	}

	@Override
	public Map<String, StklImportSpezifikation> stklImportSpezifikationenFindAll(
			int stklTyp, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		KeyvalueDto[] kva = getSystemServicesFac()
				.keyvalueFindyByCGruppe(StklImportSpezifikation.getKeyValueCGruppeByStklTyp(stklTyp));
		Map<String, StklImportSpezifikation> map = new HashMap<String, StklImportSpezifikation>();
		String mandant = theClientDto.getMandant();
		
		for (KeyvalueDto kv : kva) {
			StklImportSpezifikation spez = keyValueDtoToSpez(kv);
			if(mandant.equals(spez.getMandantCnr())) {
				map.put(spez.getName(), spez);
			}
		}
		return map;
	}

	@Override
	public void createStklImportSpezifikation(StklImportSpezifikation spez) 
			throws RemoteException, EJBExceptionLP {
		KeyvalueDto kv = spezToKeyValueDto(spez);
		getSystemServicesFac().createKeyvalue(kv);
	}

	@Override
	public void removeStklImportSpezifikation(StklImportSpezifikation spez) 
			throws RemoteException, EJBExceptionLP {
		getSystemServicesFac().removeKeyvalue(spez.getKeyValueCGruppe(), spez.getKeyValueCKey());
	}

	@Override
	public void updateStklImportSpezifikation(StklImportSpezifikation spez) 
			throws RemoteException, EJBExceptionLP {
		KeyvalueDto kv = spezToKeyValueDto(spez);
		getSystemServicesFac().updateKeyvalue(kv);
	}
	
	/**
	 * Konvertiert eine StklImportSpezifikation in ein KeyValueDto um.
	 * 
	 * @param spez, die zu konvertierende Spezifikation
	 * @return konvertiertes KeyValueDto
	 */
	private KeyvalueDto spezToKeyValueDto(StklImportSpezifikation spez) {
		StringWriter sw = new StringWriter();
		spez.writeString(sw);

		KeyvalueDto kv = new KeyvalueDto();
		kv.setCGruppe(spez.getKeyValueCGruppe());
		kv.setCDatentyp("java.lang.String");
		kv.setCKey(spez.getName() + ", " + spez.getMandantCnr());
		kv.setCValue(sw.toString());
		return kv;
	}
	
	private StklImportSpezifikation keyValueDtoToSpez(KeyvalueDto kv) {
		try {
			StklImportSpezifikation spez = null;
			if(SystemServicesFac.KEYVALUE_STKL_IMPORT_SPEZ.equals(kv.getCGruppe())) {
				spez = new FertigungsStklImportSpezifikation();
				spez.readString(new BufferedReader(new StringReader(kv.getCValue())));
			} else if(SystemServicesFac.KEYVALUE_AGSTKL_IMPORT_SPEZ.equals(kv.getCGruppe())) {
				spez = new AgstklImportSpezifikation();
				spez.readString(new BufferedReader(new StringReader(kv.getCValue())));
			} else if(SystemServicesFac.KEYVALUE_EINKAUFSAGSTKL_IMPORT_SPEZ.equals(kv.getCGruppe())) {
				spez = new EinkaufsagstklImportSpezifikation();
				spez.readString(new BufferedReader(new StringReader(kv.getCValue())));
			}

			Validator.notNull(spez, "spez");
			return spez;
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	private String getHandartikelCBez(IStklImportResult r) {
		Map<String, String> m = r.getValues();
		String[] types = { StklImportSpezifikation.BEZEICHNUNG,
				StklImportSpezifikation.BEZEICHNUNG1,
				StklImportSpezifikation.BEZEICHNUNG2,
				StklImportSpezifikation.BEZEICHNUNG3,
				StklImportSpezifikation.ARTIKELNUMMER,
				StklImportSpezifikation.HERSTELLERARTIKELNUMMER,
				StklImportSpezifikation.KUNDENARTIKELNUMMER,
				StklImportSpezifikation.SI_WERT,
				StklImportSpezifikation.BAUFORM,
				StklImportSpezifikation.ARTIKELNUMMER };
		for (String type : types) {
			String bez = m.get(type);
			if (bez != null && !bez.isEmpty())
				return bez;
		}
		return null;
	}

}
