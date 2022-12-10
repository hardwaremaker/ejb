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
package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import com.lp.server.finanz.bl.FibuExportManager;
import com.lp.server.util.BelegAdapter;

public class RechnungAdapter extends BelegAdapter implements Serializable {

	private static final long serialVersionUID = 1L;

	private RechnungDto reDto;
	private BigDecimal offenerBetrag;
	private String partnerKurzbezeichnung;
	
	public RechnungAdapter(RechnungDto rechnungDto) {
		this(rechnungDto, "");
	}
	
	public RechnungAdapter(RechnungDto rechnungDto, String partnerKurzBez) {
		reDto = rechnungDto;
		partnerKurzbezeichnung = partnerKurzBez;
	}
	@Override
	public Integer getIId() {
		return reDto.getIId();
	}

	@Override
	public String getMandantCNr() {
		return reDto.getMandantCNr();
	}

	@Override
	public String getCNr() {
		return reDto.getCNr();
	}

	@Override
	public String getRechnungsartCNr() {
		return reDto.getRechnungartCNr();
	}

	@Override
	public Date getDBelegdatum() {
		return new Date(reDto.getTBelegdatum().getTime());
	}

	@Override
	public Integer getPartnerIId() {
		return reDto.getKundeIId();
	}

	@Override
	public BigDecimal getBruttoBetrag() {
		return getNettoBetrag().add(getUstBetrag());
	}

	@Override
	public BigDecimal getBruttoBetragfw() {
		return getNettoBetragfw().add(getUstBetragfw());
	}

	@Override
	public BigDecimal getNettoBetrag() {
		return reDto.getNWert();
	}

	@Override
	public BigDecimal getNettoBetragfw() {
		return reDto.getNWertfw();
	}

	@Override
	public BigDecimal getUstBetrag() {
		return reDto.getNWertust();
	}

	@Override
	public BigDecimal getUstBetragfw() {
		return reDto.getNWertustfw();
	}

	@Override
	public String getWaehrungCNr() {
		return reDto.getWaehrungCNr();
	}

	@Override
	public String getStatusCNr() {
		return reDto.getStatusCNr();
	}

	@Override
	public BigDecimal getKurs() {
		return reDto.getNKurs();
	}

	@Override
	public String getArtAndCNr() {
		if (RechnungFac.RECHNUNGART_RECHNUNG.compareTo(reDto.getRechnungartCNr()) == 0) {
			return FibuExportManager.BELEGART_AR + " " + reDto.getCNr();
		}
		if (RechnungFac.RECHNUNGART_GUTSCHRIFT.compareTo(reDto.getRechnungartCNr()) == 0) {
			return FibuExportManager.BELEGART_GS + " " + reDto.getCNr();
		}
		return reDto.getCNr();
	}

	@Override
	public Integer getMwstsatzIId() {
		return reDto.getMwstsatzIId();
	}

	@Override
	public void setOffenerBetrag(BigDecimal offenerBetrag) {
		this.offenerBetrag = offenerBetrag;
	}

	@Override
	public BigDecimal getOffenerBetrag() {
		return offenerBetrag;
	}

	@Override
	public Integer getZahlungszielIId() {
		return reDto.getZahlungszielIId();
	}

	@Override
	public String getPartnerKurzbezeichnung() {
		return partnerKurzbezeichnung;
	}

	@Override
	public Object getRawBelegDto() {
		return reDto;
	}

}
