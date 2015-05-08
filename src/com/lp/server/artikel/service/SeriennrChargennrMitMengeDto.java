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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.system.service.PaneldatenDto;

public class SeriennrChargennrMitMengeDto implements Serializable {
	/**
	 * 
	 */
	public static List<SeriennrChargennrMitMengeDto> erstelleDtoAusEinerSeriennummer(
			String seriennummer) {
		return erstelleSrnDtoArrayAusStringArray(new String[] { seriennummer });
	}

	public static List<SeriennrChargennrMitMengeDto> erstelleDtoAusEinerChargennummer(
			String chargennummer, BigDecimal menge) {
		return erstelleChnrDtoArrayAusStringArrayUndMengen(
				new String[] { chargennummer }, new BigDecimal[] { menge });
	}

	public SeriennrChargennrMitMengeDto() {

	}

	public SeriennrChargennrMitMengeDto(String cSeriennrChargennr,
			BigDecimal menge) {
		nMenge = menge;
		this.cSeriennrChargennr = cSeriennrChargennr;
	}

	public SeriennrChargennrMitMengeDto(String cSeriennrChargennr,
			String cVersion, BigDecimal menge) {
		nMenge = menge;
		this.cSeriennrChargennr = cSeriennrChargennr;
		this.cVersion = cVersion;
	}

	public static List erstelleSrnDtoArrayAusStringArray(String[] snrs) {
		ArrayList alSnrs = new ArrayList();

		if (snrs != null) {
			for (int i = 0; i < snrs.length; i++) {
				SeriennrChargennrMitMengeDto dto = new SeriennrChargennrMitMengeDto();
				dto.setCSeriennrChargennr(snrs[i]);
				dto.setNMenge(new BigDecimal(1));
				alSnrs.add(dto);
			}
		} else {
			SeriennrChargennrMitMengeDto dto = new SeriennrChargennrMitMengeDto();
			dto.setCSeriennrChargennr(null);
			dto.setNMenge(null);
			alSnrs.add(dto);
		}

		return alSnrs;
	}

	public static boolean sind2ListenGleich(
			List<SeriennrChargennrMitMengeDto> liste1,
			List<SeriennrChargennrMitMengeDto> liste2) {

		if (liste1 == null && liste2 == null) {
			return true;
		}

		if (liste1 == null && liste2 != null) {

			if (liste2.size() == 1
					&& liste2.get(0).getCSeriennrChargennr() == null) {
				return true;
			} else {
				return false;
			}

		}

		if (liste2 == null && liste1 != null) {

			if (liste1.size() == 1
					&& liste1.get(0).getCSeriennrChargennr() == null) {
				return true;
			} else {
				return false;
			}

		}

		if (liste1.size() == liste2.size()) {

			for (int i = 0; i < liste1.size(); i++) {

				SeriennrChargennrMitMengeDto eintrag1 = liste1.get(i);
				SeriennrChargennrMitMengeDto eintrag2 = liste2.get(i);

				String cSNR1 = "";
				if (eintrag1.getCSeriennrChargennr() != null) {
					cSNR1 = eintrag1.getCSeriennrChargennr();
				}

				String cSNR2 = "";
				if (eintrag2.getCSeriennrChargennr() != null) {
					cSNR2 = eintrag2.getCSeriennrChargennr();
				}

				if (eintrag1.getNMenge().equals(eintrag2.getNMenge())
						&& cSNR1.equals(cSNR2)) {

				} else {
					return false;
				}

			}

			return true;

		} else {
			return false;
		}
	}

	public static List add2SnrChnrDtos(
			List<SeriennrChargennrMitMengeDto> vorhandeneListe,
			List<SeriennrChargennrMitMengeDto> toAdd) {
		ArrayList alSnrs = new ArrayList();

		if (vorhandeneListe != null) {
			if (toAdd != null) {
				for (int i = 0; i < toAdd.size(); i++) {
					vorhandeneListe.add(toAdd.get(i));
				}
			}

		} else {
			vorhandeneListe = toAdd;
		}

		return alSnrs;
	}

	public static List add2SnrChnrDtos(
			List<SeriennrChargennrMitMengeDto> vorhandeneListe,
			SeriennrChargennrAufLagerDto toAdd) {
		ArrayList alSnrs = new ArrayList();

		if (toAdd != null) {
			SeriennrChargennrMitMengeDto a = new SeriennrChargennrMitMengeDto(
					toAdd.getCSeriennrChargennr(), toAdd.getCVersion(),
					toAdd.getNMenge());
			vorhandeneListe.add(a);

		}

		return alSnrs;
	}

	public static List erstelleChnrDtoArrayAusStringArrayUndMengen(
			String[] snrs, BigDecimal mengen[]) {
		ArrayList alSnrs = new ArrayList();

		if (snrs != null) {
			for (int i = 0; i < snrs.length; i++) {
				SeriennrChargennrMitMengeDto dto = new SeriennrChargennrMitMengeDto();
				dto.setCSeriennrChargennr(snrs[i]);
				dto.setNMenge(mengen[i]);
				alSnrs.add(dto);
			}
		}

		return alSnrs;
	}

	public static String erstelleStringAusMehrerenSeriennummern(
			List<SeriennrChargennrMitMengeDto> snrs) {
		String s = null;

		if (snrs != null && snrs.size() > 0) {
			s = "";
			for (int i = 0; i < snrs.size(); i++) {
				if (snrs.get(i).getCSeriennrChargennr() != null) {
					s += snrs.get(i).getCSeriennrChargennr();

					if (!(i == snrs.size() - 1)) {
						s += ",";

					}
				}
			}
		}
		return s;

	}

	public static String erstelleStringAusMehrerenVersionen(
			List<SeriennrChargennrMitMengeDto> snrs) {
		String s = null;

		if (snrs != null && snrs.size() > 0) {
			s = "";
			for (int i = 0; i < snrs.size(); i++) {
				if (snrs.get(i).getCVersion() != null) {
					s += snrs.get(i).getCVersion();

					if (!(i == snrs.size() - 1)) {
						s += ",";

					}
				}
			}
		}
		return s;

	}

	public static String[] erstelleStringArrayAusMehrerenSeriennummern(
			List<SeriennrChargennrMitMengeDto> snrs) {
		String[] s = null;

		if (snrs != null && snrs.size() > 0) {
			s = new String[snrs.size()];
			for (int i = 0; i < snrs.size(); i++) {
				s[i] = snrs.get(i).getCSeriennrChargennr();

			}
		}
		return s;

	}

	private static final long serialVersionUID = 1L;
	private BigDecimal nMenge;
	private String cSeriennrChargennr;
	public Integer lossollmaterialIId = null;
	private String cVersion;

	public String getCVersion() {
		return cVersion;
	}

	public void setCVersion(String cVersion) {
		this.cVersion = cVersion;
	}

	ArrayList<GeraetesnrDto> alGeraetesnr = null;

	PaneldatenDto[] paneldatenDtos = null;

	public PaneldatenDto[] getPaneldatenDtos() {
		return paneldatenDtos;
	}

	public void setPaneldatenDtos(PaneldatenDto[] paneldatenDtos) {
		this.paneldatenDtos = paneldatenDtos;
	}

	public ArrayList<GeraetesnrDto> getAlGeraetesnr() {
		return alGeraetesnr;
	}

	public void setAlGeraetesnr(ArrayList<GeraetesnrDto> alGeraetesnr) {
		this.alGeraetesnr = alGeraetesnr;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public String getCSeriennrChargennr() {
		return cSeriennrChargennr;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public void setCSeriennrChargennr(String cSeriennrChargennr) {
		this.cSeriennrChargennr = cSeriennrChargennr;
	}
}
