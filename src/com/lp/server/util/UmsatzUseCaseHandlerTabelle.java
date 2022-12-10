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
package com.lp.server.util;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

public abstract class UmsatzUseCaseHandlerTabelle extends UseCaseHandlerTabelle {

	private static final long serialVersionUID = 1L;
	protected final int iAnzahlZeilen = 17;
	protected final int iAnzahlSpalten = 9;

	protected int iJahr = 0;
	protected int iVorjahr = 0;
	protected int iCurrentMonat = 0;
	protected int iCurrentJahr = 0;

	protected int iIndexBeginnMonat = 0;
	protected String[] aMonatsnamen = null;

	protected Locale locUI = null;
	protected String mandantCNr = null;

	protected final static int IDX_SUMMEN_VORJAHR = 0;
	protected final static int IDX_SUMMEN_GESAMT = 16;

	public UmsatzUseCaseHandlerTabelle() {
		super();
		try {
			setAnzahlZeilen(iAnzahlZeilen);
			setAnzahlSpalten(iAnzahlSpalten);
		} catch (Throwable t) {

		}
	}

	public void init(FilterKriterium fk) {
		mandantCNr = theClientDto.getMandant();
		locUI = theClientDto.getLocUi();
		iJahr = new Integer(fk.value).intValue();
		iVorjahr = iJahr - 1;
		if (fk.kritName.equals(RechnungFac.KRIT_JAHR_KALENDERJAHR)) {
			iIndexBeginnMonat = 0;
		}
		if (fk.kritName.equals(RechnungFac.KRIT_JAHR_GESCHAEFTSJAHR)) {
			// die Monate im GregorianCalendar beginnen mit 0
			ParametermandantDto pmBeginnMonat;
			try {
				pmBeginnMonat = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT);
				iIndexBeginnMonat = ((Integer) pmBeginnMonat.getCWertAsObject()).intValue() - 1;
			} catch (RemoteException e) {
			}
		}
		// mit diesen Variablen werden die laufenden Monate in der
		// Darstellung gehandelt
		iCurrentMonat = iIndexBeginnMonat;
		iCurrentJahr = iJahr;

		// das sind alle Monatsnamen, die in dieser Tabelle in dieser
		// Reihenfolge vorkommen koennen.
		DateFormatSymbols symbols = new DateFormatSymbols(locUI);
		aMonatsnamen = symbols.getMonths();
	}

	protected GregorianCalendar berechneNaechstesZeitintervall(int iLetztesMonatI, int iJahrI) {
		GregorianCalendar gcNextO = null;

		if (iLetztesMonatI == GregorianCalendar.DECEMBER) {
			gcNextO = new GregorianCalendar(iJahrI + 1, GregorianCalendar.JANUARY, 1);
		} else {
			gcNextO = new GregorianCalendar(iJahrI, iLetztesMonatI + 1, 1);
		}

		return gcNextO;
	}

	

	

	protected GregorianCalendar[] getGesamtJahr(Integer iJahr, Integer plusJahre, boolean bGeschaeftsjahr) {

		if (bGeschaeftsjahr == true) {

			java.sql.Timestamp[] tVonBis = getBuchenFac().getDatumVonBisGeschaeftsjahr(iJahr, theClientDto);

			tVonBis[0] = addiereJahre(tVonBis[0], plusJahre);
			tVonBis[1] = addiereJahre(tVonBis[1], plusJahre);

			GregorianCalendar gcBis = new GregorianCalendar();
			gcBis.setTimeInMillis(tVonBis[1].getTime());
			gcBis.add(GregorianCalendar.DAY_OF_MONTH, 1);
			gcBis.getTime();

			return new GregorianCalendar[] { new GregorianCalendar(1900, GregorianCalendar.JANUARY, 1), gcBis };

		} else {

			return new GregorianCalendar[] { new GregorianCalendar(1900, GregorianCalendar.JANUARY, 1),
					new GregorianCalendar(iJahr + 1 + plusJahre, GregorianCalendar.JANUARY, 1) };

		}

	}

	

	protected ArrayList<GregorianCalendar[]> getMonateAktuellesJahr(Integer iJahr, Integer plusJahre,
			boolean bGeschaeftsjahr) {

		ArrayList<GregorianCalendar[]> alMonate = new ArrayList<GregorianCalendar[]>();

		if (bGeschaeftsjahr == true) {
			java.sql.Timestamp[] tVonBis = getBuchenFac().getDatumVonBisGeschaeftsjahr(iJahr, theClientDto);

			tVonBis[0] = addiereJahre(tVonBis[0], plusJahre);
			tVonBis[1] = addiereJahre(tVonBis[1], plusJahre);

			GregorianCalendar cAktuell = new GregorianCalendar();
			cAktuell.setTime(tVonBis[0]);
			while (cAktuell.getTime().before(tVonBis[1])) {

				GregorianCalendar gcEnde = new GregorianCalendar();
				gcEnde.setTime(cAktuell.getTime());
				gcEnde.add(GregorianCalendar.MONTH, 1);

				GregorianCalendar gcTemp = new GregorianCalendar();
				gcTemp.setTimeInMillis(cAktuell.getTimeInMillis());

				alMonate.add(new GregorianCalendar[] { gcTemp, gcEnde });

				cAktuell.add(Calendar.MONTH, 1);

			}

		} else {

			GregorianCalendar gcBeginn = new GregorianCalendar(iJahr, GregorianCalendar.JANUARY, 1);
			gcBeginn.add(GregorianCalendar.YEAR, plusJahre);

			for (int i = 0; i <= 11; i++) {
				GregorianCalendar gcEnde = new GregorianCalendar(iJahr, gcBeginn.get(GregorianCalendar.MONTH), 1);
				gcEnde.add(GregorianCalendar.MONTH, 1);
				gcEnde.getTime();

				GregorianCalendar gcTemp = new GregorianCalendar();
				gcTemp.setTimeInMillis(gcBeginn.getTimeInMillis());
				alMonate.add(new GregorianCalendar[] { gcTemp, gcEnde });

				gcBeginn.add(GregorianCalendar.MONTH, 1);

			}

		}

		return alMonate;

	}

}
