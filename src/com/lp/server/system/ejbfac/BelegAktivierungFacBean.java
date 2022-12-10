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
 *******************************************************************************/
package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.finanz.service.UstWarnungDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.service.BelegDto;
import com.lp.service.BelegVerkaufDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BelegAktivierungFacBean extends Facade implements BelegAktivierungFac {
	/**
	 * Belegarten f&uuml;r die &uuml;berpr&uuml;ft werden soll, ob ein
	 * ustpflichtiger Kunde Nicht-USt-Positionen hat, bzw. ein nicht-ustpflichtiger
	 * Kunde USt-Positionen im Beleg hat
	 */
	private static String[] ustVerkaufBelegarten = { LocaleFac.BELEGART_ANGEBOT, LocaleFac.BELEGART_AUFTRAG,
			LocaleFac.BELEGART_LIEFERSCHEIN, LocaleFac.BELEGART_RECHNUNG, LocaleFac.BELEGART_GUTSCHRIFT };

	private static String[] zeropricePosBelegarten = { LocaleFac.BELEGART_RECHNUNG };

	@Override
	public BelegPruefungDto aktiviereBelegControlled(IAktivierbar facBean, Integer iid, Timestamp t,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return aktiviereBelegControlledImpl(facBean, iid, t, true, theClientDto);
	}

	private BelegPruefungDto aktiviereBelegControlledImpl(IAktivierbar facBean, Integer iid, Timestamp t,
			boolean needsVerify, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Validator.notNull(iid, "iid");
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notNull(t, "t");

		facBean.pruefeAktivierbar(iid, theClientDto);
		facBean.pruefeAktivierbarRecht(iid, theClientDto);
		List<Timestamp> timestamps = facBean.getAenderungsZeitpunkte(iid);
		timestamps.removeAll(Collections.singleton(null));
		Timestamp tAendern = Collections.max(timestamps);

		if (tAendern.after(t)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_AKTIVIEREN_BELEG_WURDE_GEAENDERT,
					"Berechnungszeitpunkt: " + t.toString());
		}

		BelegPruefungDto pruefDto = facBean.aktiviereBeleg(iid, theClientDto);
		return needsVerify ? verify(pruefDto, facBean, iid, theClientDto) : pruefDto;
	}

	@Override
	public BelegPruefungDto berechneBelegControlled(IAktivierbar facBean, Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.notNull(iid, "iid");
		Validator.notNull(theClientDto, "theClientDto");
		facBean.pruefeAktivierbar(iid, theClientDto);
		Timestamp t = facBean.berechneBeleg(iid, theClientDto);
		t = roundUpLikeDB(t);

		BelegPruefungDto pruefungDto = verify(facBean, iid, theClientDto);
		pruefungDto.setBerechnungsZeitpunkt(t);
		return pruefungDto;
	}

	@Override
	public BelegPruefungDto berechneAktiviereControlled(IAktivierbar facBean, Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BelegPruefungDto pruefungDto = berechneBelegControlled(facBean, iid, theClientDto);
		BelegPruefungDto aktivierungDto = aktiviereBelegControlledImpl(
				facBean, iid, pruefungDto.getBerechnungsZeitpunkt(), false, theClientDto);
		if (aktivierungDto != null) {
			for (UstWarnungDto	ustWarnung : aktivierungDto.getUstWarnungDtos()) {
				pruefungDto.addUstWarnung(ustWarnung);
			}			
		}
		return pruefungDto;
	}

	protected BelegPruefungDto verify(IAktivierbar facBean, Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return verify(new BelegPruefungDto(iid), facBean, iid, theClientDto);
	}

	protected BelegPruefungDto verify(BelegPruefungDto pruefDto, IAktivierbar facBean, Integer iid,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BelegDto belegDto = facBean.getBelegDto(iid, theClientDto);
		if (pruefDto == null) {
			pruefDto = new BelegPruefungDto(belegDto.getIId());
		}
		pruefDto.setBelegId(belegDto.getIId());
//		BelegPruefungDto pruefungDto = new BelegPruefungDto(belegDto.getIId());

		pruefDto = verifyUst(belegDto, pruefDto, facBean, theClientDto);
		if (getParameterFac().getRechnungPositionWert0Pruefen(theClientDto.getMandant())) {
			pruefDto = verifyZeroPriceRePos(belegDto, pruefDto, facBean, theClientDto);
		}

		return pruefDto;
	}

	private BelegPruefungDto verifyUst(BelegDto belegDto, BelegPruefungDto pruefungDto, IAktivierbar facBean,
			TheClientDto theClientDto) throws RemoteException {
		if (!Helper.isOneOf(belegDto.getBelegartCNr(), ustVerkaufBelegarten)) {
			return pruefungDto;
		}

		Integer kundeId = facBean.getKundeIdDesBelegs(belegDto, theClientDto);
		pruefungDto.setKundeId(kundeId);
		if (kundeId == null)
			return pruefungDto;

		BelegpositionDto[] positionDtos = facBean.getBelegPositionDtos(belegDto.getIId(), theClientDto);

		// Wir wissen, dass wir nur(!) VerkaufBeleg pruefen (siehe ustVerkaufBelegarten)
		boolean b = getSystemFac().enthaeltEineVKPositionKeineMwstObwohlKundeSteuerpflichtig(kundeId,
				(BelegpositionVerkaufDto[]) positionDtos, (BelegVerkaufDto) belegDto, theClientDto);
		pruefungDto.setKundeHatUstAberNichtUstPositionen(b);
		b = getSystemFac().enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(kundeId,
				(BelegpositionVerkaufDto[]) positionDtos, (BelegVerkaufDto) belegDto, theClientDto);
		pruefungDto.setKundeHatKeineUstAberUstPositionen(b);

		return pruefungDto;
	}

	private BelegPruefungDto verifyZeroPriceRePos(BelegDto belegDto, BelegPruefungDto pruefungDto, IAktivierbar facBean,
			TheClientDto theClientDto) throws RemoteException {
		if (!Helper.isOneOf(belegDto.getBelegartCNr(), zeropricePosBelegarten)) {
			return pruefungDto;
		}

		Integer kundeId = facBean.getKundeIdDesBelegs(belegDto, theClientDto);
		pruefungDto.setKundeId(kundeId);
		if (kundeId == null)
			return pruefungDto;

		BelegpositionDto[] positionDtos = facBean.getBelegPositionDtos(belegDto.getIId(), theClientDto);
		for (int i = 0; i < positionDtos.length; i++) {
			RechnungPositionDto posDto = (RechnungPositionDto) positionDtos[i];
			if (posDto.isLieferschein()) {
				LieferscheinDto lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(posDto.getLieferscheinIId());
				BelegPruefungDto.RePosInfo reposInfo = pruefungDto.new RePosInfo(posDto.getIId(), lsDto.getCNr());
				LieferscheinpositionDto[] lsposDtos = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(posDto.getLieferscheinIId());
				for (int j = 0; j < lsposDtos.length; j++) {
					if (hasZeroPrice(lsposDtos[j], theClientDto)) {
						reposInfo.addLsposId(lsposDtos[j].getIId(),
								getLieferscheinpositionFac().getLSPositionNummer(lsposDtos[j].getIId()));
					}
				}
				if (reposInfo.getLsposIds().size() > 0) {
					pruefungDto.addZeroPriceRePosInfo(reposInfo);
				}
			} else {
				if (hasZeroPrice(posDto, theClientDto)) {
					Integer posNr = getRechnungFac().getPositionNummer(posDto.getIId());
					BelegPruefungDto.RePosInfo reposInfo = pruefungDto.new RePosInfo(posDto.getIId(), posNr);
					pruefungDto.addZeroPriceRePosInfo(reposInfo);
				}
			}
		}

		return pruefungDto;
	}

	private boolean hasZeroPrice(BelegpositionVerkaufDto posDto, TheClientDto theClientDto) {
		if (posDto.isMengenbehaftet() && posDto.getNMenge() != null && posDto.getNMenge().signum() != 0) {

			// SP6944
			if (posDto.getArtikelIId() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(posDto.getArtikelIId(), theClientDto);
				if (!Helper.short2Boolean(aDto.getBVkpreispflichtig())) {
					return false;
				}
			}

			if (Helper.short2boolean(posDto.getBNettopreisuebersteuert())) {
				return posDto.getNNettoeinzelpreis() == null || posDto.getNNettoeinzelpreis().signum() == 0;
			} else {
				return (posDto.getNEinzelpreis() == null) || (posDto.getNEinzelpreis().signum() == 0);
			}
		}
		return false;
	}

	/**
	 * Rundet die Millisekunden des <code>Timestamp t</code> <b>AUF</b>, wie es der
	 * Typ <code>datetime</code> der MSSQL DB macht.<br>
	 * <b>Es wird immer nur aufgerundet, nicht ab!</b><br>
	 * 2 wird aufgerundet auf 3<br>
	 * 5 -> 7<br>
	 * 6 -> 7<br>
	 * 9 ->10<br>
	 * 
	 * Alle anderen Werte werden nicht ver&auml;ndert.
	 * 
	 * @param t
	 * @return den aufgerundeten Timestamp
	 */
	protected Timestamp roundUpLikeDB(Timestamp t) {
		int ms = (int) (t.getTime() % 10);
		int msRounded = 0;

		if (ms == 2)
			msRounded = 3;
		else if (ms == 5 || ms == 6)
			msRounded = 7;
		else if (ms == 9)
			msRounded = 10;
		else
			msRounded = ms;

		return new Timestamp(t.getTime() - ms + msRounded);
	}
}
