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
package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import com.lp.server.finanz.bl.FibuExportManager;
import com.lp.server.util.BelegAdapter;

public class EingangsrechnungAdapter extends BelegAdapter implements Serializable {

	private static final long serialVersionUID = 1L;

	private EingangsrechnungDto erDto;
	private BigDecimal offenerBetrag;
	private String partnerKurzbezeichnung;
	
	public EingangsrechnungAdapter(EingangsrechnungDto eingangsrechnungDto) {
		this(eingangsrechnungDto, "");
	}
	
	public EingangsrechnungAdapter(EingangsrechnungDto eingangsrechnungDto, String partnerKurzBez) {
		erDto = eingangsrechnungDto;
		partnerKurzbezeichnung = partnerKurzBez;
	}
	
	@Override
	public Integer getIId() {
		return erDto.getIId();
	}

	@Override
	public String getMandantCNr() {
		return erDto.getMandantCNr();
	}

	@Override
	public String getCNr() {
		return erDto.getCNr();
	}

	@Override
	public String getRechnungsartCNr() {
		return erDto.getEingangsrechnungartCNr();
	}

	@Override
	public Date getDBelegdatum() {
		return erDto.getDBelegdatum();
	}

	@Override
	public Integer getPartnerIId() {
		return erDto.getLieferantIId();
	}

	@Override
	public BigDecimal getBruttoBetrag() {
		return erDto.getNBetrag();
	}

	@Override
	public BigDecimal getBruttoBetragfw() {
		return erDto.getNBetragfw();
	}

	@Override
	public BigDecimal getNettoBetrag() {
		return getBruttoBetrag().subtract(getUstBetrag());
	}

	@Override
	public BigDecimal getNettoBetragfw() {
		return getBruttoBetragfw().subtract(getUstBetragfw());
	}

	@Override
	public BigDecimal getUstBetrag() {
		return erDto.getNUstBetrag();
	}

	@Override
	public BigDecimal getUstBetragfw() {
		return erDto.getNUstBetragfw();
	}

	@Override
	public String getWaehrungCNr() {
		return erDto.getWaehrungCNr();
	}

	@Override
	public String getStatusCNr() {
		return erDto.getStatusCNr();
	}

	@Override
	public BigDecimal getKurs() {
		return erDto.getNKurs();
	}

	@Override
	public String getArtAndCNr() {
		if (EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG.compareTo(erDto.getEingangsrechnungartCNr()) == 0) {
			return FibuExportManager.BELEGART_ER + " " + erDto.getCNr();
		}
		if (EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT.compareTo(erDto.getEingangsrechnungartCNr()) == 0) {
			return FibuExportManager.BELEGART_GS + " " + erDto.getCNr();
		}
		return erDto.getCNr();
	}

	@Override
	public Integer getMwstsatzIId() {
		return erDto.getMwstsatzIId();
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
		return erDto.getZahlungszielIId();
	}

	@Override
	public String getPartnerKurzbezeichnung() {
		return partnerKurzbezeichnung;
	}

	@Override
	public Object getRawBelegDto() {
		return erDto;
	}

}
