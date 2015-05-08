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
package com.lp.server.artikel.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Locale;

import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Handlagerbewegungen implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-11-25
 * </p>
 * <p>
 * </p>
 * 
 * @author Christian Kollmann
 * @version 1.0
 */

public class ChargenAufLagerHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			FilterKriterium[] filterKriterien = this.getQuery()
					.getFilterBlock().filterKrit;
			Integer artikelIId = null;
			Integer lagerIId = null;
			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].kritName
						.equals(LagerFac.FLR_LAGERBEWEGUNG_FLRARTIKEL + ".i_id")) {
					artikelIId = new Integer(filterKriterien[i].value);
				} else if (filterKriterien[i].kritName
						.equals(LagerFac.FLR_LAGERBEWEGUNG_FLRLAGER + ".i_id")) {
					lagerIId = new Integer(filterKriterien[i].value);
				}
			}
			SeriennrChargennrAufLagerDto[] chnr = getLagerFac()
					.getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId, false, null,
							theClientDto);
			Object[][] rows = new Object[chnr.length][colCount];
			int row = 0;
			for (int i = 0; i < chnr.length; i++) {
				int col = 0;
				SeriennrChargennrAufLagerDto lagerbewegung = chnr[i];
				rows[row][col++] = lagerbewegung.getCSeriennrChargennr();
				rows[row][col++] = lagerbewegung.getCSeriennrChargennr();
				rows[row][col++] = Helper.formatTimestamp(lagerbewegung
						.getTBuchungszeit(), theClientDto.getLocUi());
				rows[row++][col++] = lagerbewegung.getNMenge();
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return result;
	}

	protected long getRowCountFromDataBase() {
		FilterKriterium[] filterKriterien = this.getQuery().getFilterBlock().filterKrit;
		Integer artikelIId = null;
		Integer lagerIId = null;
		for (int i = 0; i < filterKriterien.length; i++) {
			if (filterKriterien[i].kritName
					.equals(LagerFac.FLR_LAGERBEWEGUNG_FLRARTIKEL + ".i_id")) {
				artikelIId = new Integer(filterKriterien[i].value);
			} else if (filterKriterien[i].kritName
					.equals(LagerFac.FLR_LAGERBEWEGUNG_FLRLAGER + ".i_id")) {
				lagerIId = new Integer(filterKriterien[i].value);
			}
		}
		SeriennrChargennrAufLagerDto[] chnr = null;
		try {
			chnr = getLagerFac().getAllSerienChargennrAufLagerInfoDtos(
					artikelIId, lagerIId, false, null, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return chnr.length;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {

		QueryResult result = null;

		result = this.getPageAt(0);

		return result;
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Object.class, String.class, String.class,
							BigDecimal.class },
					new String[] {
							"PK",
							getTextRespectUISpr("lp.chargennummer", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.buchungszeit", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.menge", mandantCNr, locUI) },
					new String[] { "PK",
							LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR,
							LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT,
							LagerFac.FLR_LAGERBEWEGUNG_N_MENGE }));

		}
		return super.getTableInfo();
	}
}
