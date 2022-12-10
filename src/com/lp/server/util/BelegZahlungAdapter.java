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

import java.math.BigDecimal;
import java.sql.Date;

public abstract class BelegZahlungAdapter {
	
	private BelegAdapter belegAdapter;

	protected BelegZahlungAdapter() {
	}

	public abstract Integer getRechnungIId();
	
	public abstract void setRechnungIId(Integer rechnungIId);

	public abstract Date getDZahldatum();

	public abstract void setDZahldatum(Date dZahldatum);

	public abstract String getZahlungsartCNr();

	public abstract void setZahlungsartCNr(String zahlungsartCNr);

	public abstract Integer getBankkontoIId();

	public abstract void setBankkontoIId(Integer bankkontoIId);

	public abstract Integer getIAuszug();

	public abstract void setIAuszug(Integer iAuszug);

	public abstract BigDecimal getNKurs();

	public abstract void setNKurs(BigDecimal nKurs);

	public abstract BigDecimal getNBetrag();

	public abstract void setNBetrag(BigDecimal nBetrag);

	public abstract BigDecimal getNBetragfw();

	public abstract void setNBetragfw(BigDecimal nBetragfw);

	public abstract BigDecimal getNBetragUst();
	
	public abstract void setNBetragUst(BigDecimal nBetragUst);

	public abstract BigDecimal getNBetragUstfw();

	public abstract void setNBetragUstfw(BigDecimal nBetragUstfw);
	
	public abstract String getKommentar();

	public abstract void setKommentar(String kommentar);

	public abstract Boolean isBErledigt();

	public abstract void setBErledigt(Boolean bErledigt);
	
	public abstract Boolean getBKursuebersteuert();
	
	public abstract void setBKursuebersteuert(Boolean bKursuebersteuert);
	
	public abstract BigDecimal getBruttoBetrag();
	
	public BelegAdapter getBelegAdapter() {
		return belegAdapter;
	}
	
	public void setBelegAdapter(BelegAdapter belegAdapter) {
		this.belegAdapter = belegAdapter;
	}
	
	public abstract Object getRawBelegZahlungDto();
	
	public abstract boolean isEingangsrechnungzahlungAdapter();
	
	public abstract boolean isRechnungzahlungAdapter();
}
