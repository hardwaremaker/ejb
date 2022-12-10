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
package com.lp.server.bestellung.ejbfac;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.ejb.BestellpositionQuery;
import com.lp.server.bestellung.ejb.Wareneingang;
import com.lp.server.bestellung.ejb.WareneingangQuery;
import com.lp.server.bestellung.ejb.Wareneingangsposition;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.fastlanereader.generated.FLRWarezwischenmandanten;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.EinstandspreiseEinesWareneingangsDto;
import com.lp.server.bestellung.service.RueckgabeWEPMitReelIDDto;
import com.lp.server.bestellung.service.WEPBuchenReturnDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangDtoAssembler;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.bestellung.service.WareneingangspositionDtoAssembler;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.ejb.Personal;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.Facade;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.IDetect;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class WareneingangFacBean extends Facade implements WareneingangFac {

	@PersistenceContext
	private EntityManager em;

	private WareneingangspositionDto assembleWareneingangspositionDto(Wareneingangsposition wareneingangsposition) {

		WareneingangspositionDto pos = WareneingangspositionDtoAssembler.createDto(wareneingangsposition);

		pos.setSeriennrChargennrMitMenge(getLagerFac().getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
				LocaleFac.BELEGART_BESTELLUNG, pos.getIId()));
		LagerbewegungDto lagerbewegungDto = null;
		if (pos.getSeriennrChargennrMitMenge() != null && pos.getSeriennrChargennrMitMenge().size() > 0) {
			lagerbewegungDto = getLagerFac().getLetzteintrag(LocaleFac.BELEGART_BESTELLUNG, pos.getIId(),
					pos.getSeriennrChargennrMitMenge().get(0).getCSeriennrChargennr());
		} else {
			lagerbewegungDto = getLagerFac().getLetzteintrag(LocaleFac.BELEGART_BESTELLUNG, pos.getIId(), null);
		}

		if (lagerbewegungDto != null) {
			pos.setHerstellerIId(lagerbewegungDto.getHerstellerIId());
			pos.setLandIId(lagerbewegungDto.getLandIId());
		}
		return pos;
	}

	private WareneingangspositionDto[] assembleWareneingangspositionDtos(Collection<?> wareneingangspositions) {
		List<WareneingangspositionDto> list = new ArrayList<WareneingangspositionDto>();
		if (wareneingangspositions != null) {
			Iterator<?> iterator = wareneingangspositions.iterator();
			while (iterator.hasNext()) {
				Wareneingangsposition wareneingangsposition = (Wareneingangsposition) iterator.next();
				list.add(assembleWareneingangspositionDto(wareneingangsposition));
			}
		}
		WareneingangspositionDto[] returnArray = new WareneingangspositionDto[list.size()];
		return (WareneingangspositionDto[]) list.toArray(returnArray);
	}

	private WareneingangDto assembleWareneingangDto(Wareneingang wareneingang) {
		return WareneingangDtoAssembler.createDto(wareneingang);
	}

	private WareneingangDto[] assembleWareneingangDtos(Collection<?> wareneingangs) {
		List<WareneingangDto> list = new ArrayList<WareneingangDto>();
		if (wareneingangs != null) {
			Iterator<?> iterator = wareneingangs.iterator();
			while (iterator.hasNext()) {
				Wareneingang wareneingang = (Wareneingang) iterator.next();
				list.add(assembleWareneingangDto(wareneingang));
			}
		}
		WareneingangDto[] returnArray = new WareneingangDto[list.size()];
		return (WareneingangDto[]) list.toArray(returnArray);
	}

	/**
	 * Das maximale iSort bei den Wareneingaengen fuer einen bestimmten Mandanten
	 * bestimmen.
	 * 
	 * @param iIdBestellungI PK der Bestellung
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP Ausnahme
	 */
	private Integer getMaxISort(Integer iIdBestellungI) {
		Integer iiMaxISortO = null;
		try {
			Query query = em.createNamedQuery("WareneingangejbSelectMaxISort");
			query.setParameter(1, iIdBestellungI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT, e);
		}
		return iiMaxISortO;
	}

	public Integer getAnzahlWEImZeitraum(Integer artikelIId, Timestamp tVon, Timestamp tBis,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", Helper.locale2String(theClientDto.getLocUi()));

		String queryString = "SELECT wep FROM FLRWareneingangspositionen wep WHERE wep.flrbestellposition.flrbestellung.bestellungstatus_c_nr<>'"
				+ BestellungFac.BESTELLSTATUS_STORNIERT + "' AND wep.flrbestellposition.flrartikel.i_id=" + artikelIId;

		if (tVon != null) {
			queryString += " AND wep.flrwareneingang.t_wareneingansdatum >='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "'";
		}

		if (tBis != null) {
			queryString += " AND wep.flrwareneingang.t_wareneingansdatum <='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "'";
		}

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();

		int iAnzahl = resultList.size();

		session.close();
		return iAnzahl;

	}

	/**
	 * Wareneingangsposition anlegen.
	 * 
	 * @param weposDtoI    WareneingangspositionDto
	 * @param theClientDto String
	 * @return Integer
	 * @throws EJBExceptionLP
	 */

	public Integer createWareneingangsposition(WareneingangspositionDto weposDtoI, TheClientDto theClientDto) {
		return createWareneingangsposition(weposDtoI, true, true, theClientDto);
	}

	public Integer createWareneingangsposition(WareneingangspositionDto weposDto, boolean setartikelAufloesen,
			TheClientDto theClientDto) {
		return createWareneingangsposition(weposDto, setartikelAufloesen, true, theClientDto);
	}

	public RueckgabeWEPMitReelIDDto wareneingangspositionMitReelIDBuchen(Integer wareneingangIId,
			Integer bestellpositionIId, BigDecimal nMenge, String datecode, String batch1, TheClientDto theClientDto) {

		Integer weposIId = null;
		String chargennummmer_reelid = null;
		try {
			WareneingangspositionDto weposDtoVorhanden = null;
			BestellpositionDto bsposDto = getBestellpositionFac().bestellpositionFindByPrimaryKey(bestellpositionIId);

			if (bsposDto.getArtikelIId() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(bsposDto.getArtikelIId(), theClientDto);
				if (aDto.isChargennrtragend() == false) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_REELID_NICH_MOEGLICH, "");
				} else {
					ParametermandantDto p = getParameterFac().parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_AUTOMATISCHE_CHARGENNUMMER_BEI_WEP,
							ParameterFac.KATEGORIE_BESTELLUNG, theClientDto.getMandant());
					Integer i = (Integer) p.getCWertAsObject();

					if (i != 3) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_REELID_NICH_MOEGLICH, "");
					}
				}
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_REELID_NICH_MOEGLICH, "");
			}

			BestellungDto bsDto = getBestellungFac().bestellungFindByPrimaryKey(bsposDto.getBestellungIId());

			WareneingangDto weDto = wareneingangFindByPrimaryKey(wareneingangIId);

			WareneingangspositionDto[] aWEPOSDto = getWareneingangFac()
					.wareneingangspositionFindByWareneingangIId(wareneingangIId);

			if (aWEPOSDto.length >= 1) {
				for (int i = 0; i < aWEPOSDto.length; i++) {
					if (aWEPOSDto[i].getBestellpositionIId().equals(bestellpositionIId)) {
						weposDtoVorhanden = aWEPOSDto[i];
					}
				}
			}

			Integer iRn = 1;

			if (weposDtoVorhanden != null && weposDtoVorhanden.getIRn() != null) {
				iRn = weposDtoVorhanden.getIRn() + 1;
			}

			chargennummmer_reelid = bsDto.getCNr() + Helper.fitString2LengthAlignRight(weDto.getISort() + "", 2, '0')
					+ Helper.fitString2LengthAlignRight(
							getBestellpositionFac().getPositionNummer(bsposDto.getIId(), theClientDto) + "", 3, '0')
					+ Helper.fitString2LengthAlignRight(iRn + "", 2, '0');

			if (datecode == null && batch1 != null) {
				chargennummmer_reelid = chargennummmer_reelid + "||" + batch1;
			} else if (datecode != null && batch1 == null) {
				chargennummmer_reelid = chargennummmer_reelid + "|" + datecode;
			} else if (datecode != null && batch1 != null) {
				chargennummmer_reelid = chargennummmer_reelid + "|" + datecode + "|" + batch1;
			}

			if (weposDtoVorhanden == null) {

				WareneingangspositionDto weposDto = new WareneingangspositionDto();
				weposDto.setBestellpositionIId(bestellpositionIId);
				weposDto.setNGeliefertemenge(nMenge);

				weposDto.setSeriennrChargennrMitMenge(
						SeriennrChargennrMitMengeDto.erstelleDtoAusEinerChargennummer(chargennummmer_reelid, nMenge));

				weposDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
				weposDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
				weposDto.setTAendern(new Timestamp(System.currentTimeMillis()));
				weposDto.setTAendern(new Timestamp(System.currentTimeMillis()));
				weposDto.setWareneingangIId(wareneingangIId);
				weposDto.setIRn(iRn);
				BigDecimal nGeliefertpreis = bsposDto.getNNettogesamtpreis();
				weposDto.setNGelieferterpreis(nGeliefertpreis);
				weposIId = createWareneingangsposition(weposDto, false, theClientDto);
			} else {

				weposDtoVorhanden.setIRn(iRn);
				weposDtoVorhanden.getSeriennrChargennrMitMenge()
						.add(new SeriennrChargennrMitMengeDto(chargennummmer_reelid, nMenge));
				weposDtoVorhanden.setNGeliefertemenge(weposDtoVorhanden.getNGeliefertemenge().add(nMenge));

				updateWareneingangsposition(weposDtoVorhanden, false, theClientDto);
				weposIId = weposDtoVorhanden.getIId();
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return new RueckgabeWEPMitReelIDDto(chargennummmer_reelid, weposIId);
	}

	private Integer createWareneingangsposition(WareneingangspositionDto weposDtoI, boolean bSetartikelAufloesen,
			boolean bPreiseAllerWareneingaengeNeuBerechnen, TheClientDto theClientDto) throws EJBExceptionLP {
		// Preconditions.
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("theClientDto == null"));
		}
		if (weposDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("wareneingangspositionDtoI == null"));
		}

		// Wer legt an setzen.
		weposDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
		weposDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		// PK fuer WEP generieren.
		PKGeneratorObj pkGen = new PKGeneratorObj();
		weposDtoI.setIId(pkGen.getNextPrimaryKey(PKConst.PK_WARENEINGANGSPOSITIONEN));
		//
		if (weposDtoI.getBPreiseErfasst() == null) {
			weposDtoI.setBPreiseErfasst(false);
		}
		if (weposDtoI.getBVerraeumt() == null) {
			weposDtoI.setBVerraeumt(Helper.boolean2Short(false));
		}

		// Pruefen, ob der Wareneingang auch richtig verkettet wird
		try {
			WareneingangDto weDto = wareneingangFindByPrimaryKey(weposDtoI.getWareneingangIId());
			// PJ18259 Neuanlage der Bestellposition aus WEP
			if (weposDtoI.getBestellpositionIId() == null) {
				BestellpositionDto oBestellpositionDtoI = new BestellpositionDto();
				oBestellpositionDtoI.setArtikelIId(weposDtoI.getArtikelIIdFuerNeuAnlageAusWEP());
				oBestellpositionDtoI.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
				oBestellpositionDtoI.setNMenge(weposDtoI.getNGeliefertemenge());
				oBestellpositionDtoI.setBelegIId(weDto.getBestellungIId());

				ArtikelDto aDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(weposDtoI.getArtikelIIdFuerNeuAnlageAusWEP(), theClientDto);

				oBestellpositionDtoI.setEinheitCNr(aDto.getEinheitCNr());
				oBestellpositionDtoI.setBNettopreisuebersteuert(Helper.boolean2Short(true));
				oBestellpositionDtoI.setBRabattsatzUebersteuert(Helper.boolean2Short(false));
				oBestellpositionDtoI.setNNettoeinzelpreis(weposDtoI.getNGelieferterpreis());
				oBestellpositionDtoI.setNNettogesamtpreis(weposDtoI.getNGelieferterpreis());

				BestellungDto besDto = getBestellungFac().bestellungFindByPrimaryKey(weDto.getBestellungIId());
				BigDecimal nZuschlag = getMaterialFac().getMaterialzuschlagEKInZielwaehrung(
						weposDtoI.getArtikelIIdFuerNeuAnlageAusWEP(), besDto.getLieferantIIdBestelladresse(),
						besDto.getDBelegdatum(), besDto.getWaehrungCNr(), theClientDto);

				if (nZuschlag != null) {
					BigDecimal preisOhneAktuellemMaterialzuschlag = weposDtoI.getNGelieferterpreis()
							.subtract(nZuschlag);

					oBestellpositionDtoI.setNMaterialzuschlag(nZuschlag);

					oBestellpositionDtoI.setNNettoeinzelpreis(preisOhneAktuellemMaterialzuschlag);

				} else {

					oBestellpositionDtoI.setNMaterialzuschlag(BigDecimal.ZERO);
				}

				oBestellpositionDtoI.setDRabattsatz(0D);
				oBestellpositionDtoI.setNRabattbetrag(BigDecimal.ZERO);

				weposDtoI.setBestellpositionIId(getBestellpositionFac().createBestellposition(oBestellpositionDtoI,
						theClientDto, BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null));

				// Den Status von vorher setzen

				String sKorrekterStatus = getBestellungFac().getRichtigenBestellStatus(besDto.getIId(), false,
						theClientDto);
				besDto.setStatusCNr(sKorrekterStatus);
				getBestellungFac().updateBestellung(besDto, theClientDto, false);

			}

			BestellpositionDto bestPosDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(weposDtoI.getBestellpositionIId());

			if (bSetartikelAufloesen == true) {
				if (bestPosDto.getPositioniIdArtikelset() != null) {
					// Es koennen nur Kopfartikel zugebucht werden
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BESTELLUNG_NUR_KOPFARTIKEL_ZUBUCHBAR,
							new Exception("FEHLER_BESTELLUNG_NUR_KOPFARTIKEL_ZUBUCHBAR"));
				}
			}

			if (!weDto.getBestellungIId().equals(bestPosDto.getBestellungIId())) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"FATAL_FALSCH_ZUORDNUNG_WARENEINGANG: BESTELLUNG_I_ID:" + bestPosDto.getBestellungIId()));
			}

			weposDtoI.setNEinstandspreis(new BigDecimal(0)); // der Preis wird
			// spaeter
			// berechnet.

			Wareneingangsposition wareneingangsposition = new Wareneingangsposition(weposDtoI.getIId(),
					weposDtoI.getNGeliefertemenge(), weposDtoI.getNGelieferterpreis(), weposDtoI.getNEinstandspreis(),
					weposDtoI.getBestellpositionIId(), weposDtoI.getPersonalIIdAendern(),
					weposDtoI.getWareneingangIId(), weposDtoI.getPersonalIIdAnlegen());
			em.persist(wareneingangsposition);
			em.flush();

			// tAendern und tAnlegen werden im Bean generiert;
			// jetzt holen und setzen wegen update.
			weposDtoI.setTAendern(wareneingangsposition.getTAendern());
			weposDtoI.setTAnlegen(wareneingangsposition.getTAnlegen());
			setWareneingangspositionFromWareneingangspositionDto(wareneingangsposition, weposDtoI);
			// Lagerbuchung
			bucheWareneingangspositionAmLager(weposDtoI, false, theClientDto);
			// Gelieferte Fixkosten in die Bestellposition uebertragen.
			setGelieferteFixkosten2Bestellposition(weposDtoI.getBestellpositionIId(),
					weposDtoI.getNFixkosten4Bestellposition(), theClientDto);
			// Einstandspreise aufrollen.

			if (bPreiseAllerWareneingaengeNeuBerechnen == true) {

				aktualisiereEinstandspreise(weDto.getBestellungIId(),
						verteileFixkostenaufWareneingangspositionen(weposDtoI.getBestellpositionIId(), theClientDto),
						theClientDto);
			}
			// PJ20372
			fremdarbeitsmaterialInsLosBuchen(weposDtoI.getNGeliefertemenge(), weposDtoI, theClientDto, weDto,
					bestPosDto);

			// Positionsstatus neu bestimmen
			getBestellpositionFac().versucheBestellpositionAufErledigtZuSetzen(weposDtoI.getBestellpositionIId(),
					theClientDto);

			// Zuegeorige Set-Positionen mitbuchen
			if (bSetartikelAufloesen == true && bestPosDto.getPositioniIdArtikelset() == null) {
				Query query = em.createNamedQuery("BestellpositionfindByPositionIIdArtikelset");
				query.setParameter(1, weposDtoI.getBestellpositionIId());
				Collection<?> bestellpositions = query.getResultList();
				Iterator it = bestellpositions.iterator();

				// SP3553 hoehere Genauigkeit verwenden
				BigDecimal faktor = weposDtoI.getNGeliefertemenge().divide(bestPosDto.getNMenge(), 10,
						BigDecimal.ROUND_HALF_EVEN);

				while (it.hasNext()) {
					Bestellposition bspos = (Bestellposition) it.next();
					// Neuer Wareneingang fuer
					WareneingangspositionDto wePosNeu = new WareneingangspositionDto();
					wePosNeu.setBestellpositionIId(bspos.getIId());
					wePosNeu.setBPreiseErfasst(weposDtoI.getBPreiseErfasst());
					wePosNeu.setWareneingangIId(weposDtoI.getWareneingangIId());

					// Menge berechnen
					BestellpositionDto bestPosDtoSet = getBestellpositionFac()
							.bestellpositionFindByPrimaryKey(bspos.getIId());

					wePosNeu.setNGeliefertemenge(Helper.rundeKaufmaennisch(bestPosDtoSet.getNMenge().multiply(faktor),
							getMandantFac().getNachkommastellenMenge(theClientDto.getMandant())));

					// Preis?
					wePosNeu.setNGelieferterpreis(new BigDecimal(0));
					wePosNeu.setNEinstandspreis(new BigDecimal(0));

					// Preis ueber Wert berechnen

					createWareneingangsposition(wePosNeu, false, true, theClientDto);
				}

				preiseEinesArtikelsetsUpdaten(weposDtoI, theClientDto);

			}

			emailAnAnfordererSenden(theClientDto, weDto, weposDtoI.getNGeliefertemenge(), bestPosDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return weposDtoI.getIId();
	}

	private void emailAnAnfordererSenden(TheClientDto theClientDto, WareneingangDto weDto, BigDecimal menge,
			BestellpositionDto bestPosDto) throws RemoteException {
		// PJ20699 E-Mail versenden
		if (bestPosDto.getArtikelIId() != null) {
			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(bestPosDto.getArtikelIId(), theClientDto);

			if (Helper.short2boolean(aDto.getBWepinfoAnAnforderer())
					|| Helper.short2boolean(bestPosDto.getBWepinfoAnAnforderer())) {
				BestellungDto besDto = getBestellungFac().bestellungFindByPrimaryKey(weDto.getBestellungIId());

				ArrayList<String> alEmpaenger = new ArrayList<String>();

				PersonalDto persDtoAbsender = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
						theClientDto);

				if (besDto.getPersonalIIdAnforderer() != null) {
					PersonalDto persDto = getPersonalFac().personalFindByPrimaryKey(besDto.getPersonalIIdAnforderer(),
							theClientDto);
					if (persDto.getCEmail() != null) {
						alEmpaenger.add(persDto.getCEmail());
					}
				}

				if (besDto.getPersonalIIdInterneranforderer() != null) {
					PersonalDto persDtoInternerAnforderer = getPersonalFac()
							.personalFindByPrimaryKey(besDto.getPersonalIIdInterneranforderer(), theClientDto);
					if (persDtoInternerAnforderer.getCEmail() != null) {
						alEmpaenger.add(persDtoInternerAnforderer.getCEmail());
					}
				}

				Query query = em.createNamedQuery("PersonalfindByBWepInfoMandantCNr");
				query.setParameter(1, (short) 1);
				query.setParameter(2, theClientDto.getMandant());
				Collection<?> clPersonal = query.getResultList();
				Iterator itPersonal = clPersonal.iterator();

				while (itPersonal.hasNext()) {
					Personal personal = (Personal) itPersonal.next();
					if (personal.getCEmail() != null && !alEmpaenger.contains(personal.getCEmail())) {
						alEmpaenger.add(personal.getCEmail());
					}

				}

				if (persDtoAbsender.getCEmail() != null && alEmpaenger.size() > 0) {

					VersandauftragDto versandauftragDto = new VersandauftragDto();
					versandauftragDto.setOInhalt(null);
					versandauftragDto.setBEmpfangsbestaetigung(new Short("0"));

					String internerKommentar = "";

					if (besDto.getXInternerKommentar() != null) {
						internerKommentar = Helper.strippHTML(besDto.getXInternerKommentar());
					}

					if (bestPosDto.getCBez() != null || bestPosDto.getCZusatzbez() != null) {
						String besBosBez = "";
						if (bestPosDto.getCBez() != null) {
							besBosBez += bestPosDto.getCBez() + " ";
						}

						if (bestPosDto.getCZusatzbez() != null) {
							besBosBez += bestPosDto.getCZusatzbez();
						}

						versandauftragDto.setCText(getTextRespectUISpr("bes.wepinfo.ananforderer.text2",
								theClientDto.getMandant(), theClientDto.getLocUi(),
								Helper.formatZahl(menge, theClientDto.getLocUi()) + "  " + aDto.getEinheitCNr().trim(),
								aDto.formatArtikelbezeichnungMitZusatzbezeichnung(), besDto.getCNr(),
								Helper.formatDatum(weDto.getTWareneingangsdatum(), theClientDto.getLocUi()), besBosBez,
								internerKommentar));

					} else {
						versandauftragDto.setCText(getTextRespectUISpr("bes.wepinfo.ananforderer.text",
								theClientDto.getMandant(), theClientDto.getLocUi(),
								Helper.formatZahl(menge, theClientDto.getLocUi()) + "  " + aDto.getEinheitCNr().trim(),
								aDto.formatArtikelbezeichnungMitZusatzbezeichnung(), besDto.getCNr(),
								Helper.formatDatum(weDto.getTWareneingangsdatum(), theClientDto.getLocUi()),
								internerKommentar));
					}

					versandauftragDto.setCAbsenderadresse(persDtoAbsender.getCEmail());

					String empfaenger = "";
					Iterator it = alEmpaenger.iterator();
					while (it.hasNext()) {
						String email = (String) it.next();

						empfaenger += email;
						if (it.hasNext()) {
							empfaenger += ";";
						}

					}

					versandauftragDto.setCEmpfaenger(empfaenger);

					versandauftragDto.setCBetreff(getTextRespectUISpr("bes.wepinfo.ananforderer.betreff",
							theClientDto.getMandant(), theClientDto.getLocUi(), aDto.getCNr()));
					getVersandFac().createVersandauftrag(versandauftragDto, false, theClientDto);
				} else {
					myLogger.logKritisch("Bestellung " + besDto.getCNr()
							+ ": WEP-Info an Anforderer nicht moeglich, da kein Absender/Empfaenger vorhanden");
				}

			}
		}
	}

	private void fremdarbeitsmaterialInsLosBuchen(BigDecimal bdMenge, WareneingangspositionDto weposDtoI,
			TheClientDto theClientDto, WareneingangDto weDto, BestellpositionDto bestPosDto) throws RemoteException {
		// PJ20372
		if (bestPosDto.getLossollmaterialIId() != null) {
			LossollmaterialDto sollmatDto = getFertigungFac()
					.lossollmaterialFindByPrimaryKey(bestPosDto.getLossollmaterialIId());

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(sollmatDto.getLosIId());

			LossollarbeitsplanDto[] sollapDtos = getFertigungFac()
					.lossollarbeitsplanFindByLossollmaterialIId(sollmatDto.getIId(), theClientDto);

			if (sollapDtos != null && sollapDtos.length > 0) {
				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
						|| losDto.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
						|| losDto.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {

					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(bestPosDto.getArtikelIId(),
							theClientDto);

					if (aDto.isLagerbewirtschaftet()) {
						BigDecimal bdLagerstand = getLagerFac().getLagerstand(aDto.getIId(), weDto.getLagerIId(),
								theClientDto);

						if (Helper
								.rundeKaufmaennisch(bdLagerstand,
										getMandantFac().getNachkommastellenMenge(theClientDto.getMandant()))
								.equals(Helper.rundeKaufmaennisch(bdMenge,
										getMandantFac().getNachkommastellenMenge(theClientDto.getMandant())))) {

							if (sollmatDto.getArtikelIId().equals(bestPosDto.getArtikelIId())) {

								LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
								losistmaterialDto.setLossollmaterialIId(sollmatDto.getIId());
								losistmaterialDto.setLagerIId(weDto.getLagerIId());

								losistmaterialDto.setNMenge(bdMenge);

								losistmaterialDto.setBAbgang(Helper.boolean2Short(true));

								getFertigungFac().gebeMaterialNachtraeglichAus(sollmatDto, losistmaterialDto,
										weposDtoI.getSeriennrChargennrMitMenge(), true, theClientDto);
							}

						}
					}
				}
			}
		}
	}

	private void setGelieferteFixkosten2Bestellposition(Integer bestellpositionIId, BigDecimal bdFixkostenGeliefert,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			BestellpositionDto besposDto = getBestellpositionFac().bestellpositionFindByPrimaryKey(bestellpositionIId);
			besposDto.setNFixkostengeliefert(bdFixkostenGeliefert);
			getBestellpositionFac().updateBestellpositionOhneWeitereAktion(besposDto, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void bucheWareneingangspositionAmLager(WareneingangspositionDto weposDtoI, boolean bLoescheBuchung,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (weposDtoI == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("weposDtoI == null"));
			}
			WareneingangDto weDto = wareneingangFindByPrimaryKey(weposDtoI.getWareneingangIId());
			BestellpositionDto besposDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(weposDtoI.getBestellpositionIId());
			// Einstandspreis in Mandantenwaehrung umrechnen
			BigDecimal bdEinstandspreis = Helper.rundeKaufmaennisch(
					getLocaleFac().rechneUmInMandantenWaehrung(weposDtoI.getNEinstandspreis(), weDto.getNWechselkurs()),
					WareneingangFac.I_NACHKOMMASTELLEN_EINSTANDSPREISE);

			BigDecimal bdMenge = bLoescheBuchung ? new BigDecimal(0) : weposDtoI.getNGeliefertemenge();
			getLagerFac().bucheZu(LocaleFac.BELEGART_BESTELLUNG, weDto.getBestellungIId(), weposDtoI.getIId(),
					besposDto.getArtikelIId(), bdMenge, // Menge
					bdEinstandspreis, weDto.getLagerIId(), weposDtoI.getSeriennrChargennrMitMenge(),
					weDto.getTWareneingangsdatum(), theClientDto);

			// Hersteller/Ursprungsland

			if (weposDtoI.getSeriennrChargennrMitMenge() != null
					&& weposDtoI.getSeriennrChargennrMitMenge().size() > 0) {
				getLagerFac().setzeHerstellerUrsprungsland(weposDtoI.getLandIId(), weposDtoI.getHerstellerIId(),
						LocaleFac.BELEGART_BESTELLUNG, weposDtoI.getIId(),
						weposDtoI.getSeriennrChargennrMitMenge().get(0).getCSeriennrChargennr(), theClientDto);
			} else {
				getLagerFac().setzeHerstellerUrsprungsland(weposDtoI.getLandIId(), weposDtoI.getHerstellerIId(),
						LocaleFac.BELEGART_BESTELLUNG, weposDtoI.getIId(), null, theClientDto);
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void removeWareneingangsposition(WareneingangspositionDto wareneingangspositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		removeWareneingangsposition(wareneingangspositionDtoI, true, theClientDto);
	}

	public void removeWareneingangsposition(WareneingangspositionDto wareneingangspositionDtoI,
			boolean bSetartikelAufloesen, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (wareneingangspositionDtoI == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
						new Exception("wareneingangspositionDtoI == null"));
			}
			// Lagerbuchungen loeschen.
			bucheWareneingangspositionAmLager(wareneingangspositionDtoI, true, theClientDto);
			// WE - Position loeschen.
			Wareneingangsposition toRemove = em.find(Wareneingangsposition.class, wareneingangspositionDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}

			// Einstandspreise aufrollen.

			aktualisiereEinstandspreise(wareneingangspositionDtoI.getBestellpositionIId(),
					verteileFixkostenaufWareneingangspositionen(wareneingangspositionDtoI.getBestellpositionIId(),
							theClientDto),
					theClientDto);

			getBestellpositionFac().versucheBestellpositionAufErledigtZuSetzen(
					wareneingangspositionDtoI.getBestellpositionIId(), theClientDto);

			BestellpositionDto bestPosDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(wareneingangspositionDtoI.getBestellpositionIId());

			if (bSetartikelAufloesen == true) {
				if (bestPosDto.getPositioniIdArtikelset() != null) {
					// Es koennen nur Kopfartikel zugebucht werden
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BESTELLUNG_NUR_KOPFARTIKEL_ZUBUCHBAR,
							new Exception("FEHLER_BESTELLUNG_NUR_KOPFARTIKEL_ZUBUCHBAR"));
				}
			}

			if (bSetartikelAufloesen == true && bestPosDto.getPositioniIdArtikelset() == null) {

				Query query = em.createNamedQuery("BestellpositionfindByPositionIIdArtikelset");
				query.setParameter(1, wareneingangspositionDtoI.getBestellpositionIId());
				Collection<?> bestellpositions = query.getResultList();
				Iterator it = bestellpositions.iterator();

				while (it.hasNext()) {
					Bestellposition bsSet = (Bestellposition) it.next();
					// 1.ten Wareneingang loeschen
					WareneingangspositionDto[] weDtos = wareneingangspositionFindByBestellpositionIId(bsSet.getIId());

					for (int i = 0; i < weDtos.length; i++) {
						WareneingangspositionDto weDto = weDtos[i];

						if (weDto.getWareneingangIId().equals(wareneingangspositionDtoI.getWareneingangIId())) {
							removeWareneingangsposition(weDtos[i], false, theClientDto);
						}

					}

				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public Integer createWareneingang(WareneingangDto wareneingangDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// Preconditions.
		if (wareneingangDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("wareneingangDtoI == null"));
		}
		if (wareneingangDtoI.getCLieferscheinnr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("wareneingangDtoI.getCLieferscheinnr() == null"));
		}
		if (wareneingangDtoI.getTLieferscheindatum() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("wareneingangDtoI.getTLieferscheindatum() == null"));
		}
		if (wareneingangDtoI.getTWareneingangsdatum() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("wareneingangDto.getTWareneingangdatum() == null"));
		}

		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer iIdWareneingangNew = pkGen.getNextPrimaryKey(PKConst.PK_WARENEINGANG);
		wareneingangDtoI.setIId(iIdWareneingangNew);

		// Wechselkurs sicherheitshalber runden.
		wareneingangDtoI.setNWechselkurs(Helper.rundeKaufmaennisch(wareneingangDtoI.getNWechselkurs(),
				LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS));

		if (wareneingangDtoI.getISort() == null) {
			// keine Sortierung vorgegeben -> hinten dranhaengen.
			wareneingangDtoI.setISort(getMaxISort(wareneingangDtoI.getBestellungIId()) + 1);
		} else {
			// Einfuegen -> Sortierung anpassen.
			sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(wareneingangDtoI.getBestellungIId(),
					wareneingangDtoI.getISort());
		}

		try {
			Wareneingang wareneingang = new Wareneingang(wareneingangDtoI.getIId(), wareneingangDtoI.getISort(),
					wareneingangDtoI.getCLieferscheinnr(), wareneingangDtoI.getTLieferscheindatum(),
					wareneingangDtoI.getNTransportkosten(), wareneingangDtoI.getDGemeinkostenfaktor(),
					wareneingangDtoI.getTWareneingangsdatum(), wareneingangDtoI.getBestellungIId(),
					wareneingangDtoI.getLagerIId(), wareneingangDtoI.getNWechselkurs());
			em.persist(wareneingang);
			em.flush();
			setWareneingangFromWareneingangDto(wareneingang, wareneingangDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return wareneingangDtoI.getIId();
	}

	/**
	 * Wareneingang updaten.
	 * 
	 * @param wareneingangDtoI WareneingangDto
	 * @param theClientDto     String
	 * @throws EJBExceptionLP
	 */
	public void updateWareneingang(WareneingangDto wareneingangDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		// Preconditions.
		if (wareneingangDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("wareneingangDtoI == null"));
		}
		Wareneingang wareneingang = em.find(Wareneingang.class, wareneingangDtoI.getIId());
		if (wareneingang == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}

		if (!wareneingang.getTWareneingangsdatum().equals(wareneingangDtoI.getTWareneingangsdatum())) {
			try {
				getLagerFac().updateTBelegdatumEinesBelegesImLager(LocaleFac.BELEGART_BESTELLUNG,
						wareneingangDtoI.getIId(), new Timestamp(wareneingangDtoI.getTWareneingangsdatum().getTime()),
						theClientDto);
			} catch (EJBExceptionLP ex1) {
			} catch (RemoteException ex1) {
			}
		}

		WareneingangspositionDto[] wePosDtos = wareneingangspositionFindByWareneingangIId(wareneingangDtoI.getIId());
		// Wenn es schon Wareneingangspositionen gibt, darf das Lager nicht mehr
		// geaendert werden.
		if (wePosDtos.length > 0 && !wareneingang.getLagerIId().equals(wareneingangDtoI.getLagerIId())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_WARENEINGANG_DARF_LAGER_NICHT_AENDERN, new Exception());
		}
		// speichern.
		setWareneingangFromWareneingangDto(wareneingang, wareneingangDtoI);
		// Einstandspreise aufrollen.

		Set<Integer> betroffeneWEs = new HashSet<Integer>();

		for (int i = 0; i < wePosDtos.length; i++) {
			Set<Integer> setEinerWepos = verteileFixkostenaufWareneingangspositionen(
					wePosDtos[i].getBestellpositionIId(), theClientDto);
			betroffeneWEs.addAll(setEinerWepos);
		}

		aktualisiereEinstandspreise(wareneingang.getBestellungIId(), betroffeneWEs, theClientDto);

	}

	/**
	 * Berechnung des Einstandspreises aller Wareneingangspositionen einer
	 * Bestellposition. Diese kann sich durch die Fixkosten auch auf mehrere
	 * Wareneingaenge der Bestellung auswirken. Die berechneten Einstandspreise
	 * werden als Gestehungspreise ins Lager uebertragen.
	 * 
	 * @param bestellpositionIId Integer
	 * @param theClientDto       String
	 * @throws EJBExceptionLP
	 */
	private void aktualisiereEinstandspreise(Integer bestellungIId, Set<Integer> wareneingangIIds,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// WENN HIER ETWAS GEAENDERT WIRD, MUSS AUCH IN
		// getBerechnetenEinstandspreisEinerWareneingangsposition() geaendert
		// werden

		// SP5530 Bewusste Abweichung dazu
		// getBerechnetenEinstandspreis beruecksichtigt bewusst nicht die
		// Setpositionen
		// um keinen doppelten Wert zu ermitteln. Hier wird der Kopfartikel mit
		// einem
		// Einstandspreis von 0 gesetzt und die Positionen enthalten die
		// aufgeteilten
		// Einstandspreise.

		// ---------------------------------------------------------------------
		// Schritt 1: als erstes muessen die Fixkosten verteilt werden.
		// Dabei wird auch ermittelt, welche Wareneingaenge betroffen sind.
		// ---------------------------------------------------------------------

		// PJ21137

		boolean bVorsteuer = false;
		BestellungDto bsDto = null;
		LieferantDto ldfDto = null;

		WareneingangDto[] weDtos = null;

		if (wareneingangIIds != null && wareneingangIIds.size() > 0) {
			weDtos = new WareneingangDto[wareneingangIIds.size()];
			Iterator it = wareneingangIIds.iterator();
			int i = 0;
			while (it.hasNext()) {
				Integer wareneingangIId = (Integer) it.next();

				weDtos[i] = wareneingangFindByPrimaryKey(wareneingangIId);
				i++;
			}

		} else {
			weDtos = wareneingangFindByBestellungIId(bestellungIId);
		}

		for (int j = 0; j < weDtos.length; j++) {
			WareneingangDto wareneingangDtoI = weDtos[j];
			// ------------------------------------------------------------------
			// -
			// nun die Einstandspreise der Wareneingaenge aufrollen.
			// ------------------------------------------------------------------
			// -
			WareneingangspositionDto[] aWEPOSDto = wareneingangspositionFindByWareneingangIId(
					wareneingangDtoI.getIId());
			// Preise in Arrays zwischenspeichern, da spaeter eine
			// nach Preis und Menge gewichtete Verteilung der Transportkosten
			// erfolgt.
			BigDecimal[] bdEinstandspreiseTemp = new BigDecimal[aWEPOSDto.length];
			BigDecimal[] bdGelieferteMenge = new BigDecimal[aWEPOSDto.length];

			if (bsDto == null) {
				try {
					bsDto = getBestellungFac().bestellungFindByPrimaryKey(wareneingangDtoI.getBestellungIId());

					if (bsDto.getLieferantIIdRechnungsadresse() != null) {
						ldfDto = getLieferantFac().lieferantFindByPrimaryKey(bsDto.getLieferantIIdRechnungsadresse(),
								theClientDto);
					} else {
						ldfDto = getLieferantFac().lieferantFindByPrimaryKey(bsDto.getLieferantIIdBestelladresse(),
								theClientDto);
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				ParametermandantDto p = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_VORSTEUER_BEI_WE_EINSTANDSPREIS,
						new Timestamp(bsDto.getDBelegdatum().getTime()));
				bVorsteuer = (Boolean) p.getCWertAsObject();
			}

			for (int i = 0; i < aWEPOSDto.length; i++) {

				Bestellposition bespos = em.find(Bestellposition.class, aWEPOSDto[i].getBestellpositionIId());

				boolean istKopfartikel = false;
				if (bespos.getArtikelIId() != null && bespos.getPositionIIdArtikelset() == null) {
					istKopfartikel = getStuecklisteFac().istArtikelArtikelset(bespos.getArtikelIId(),
							theClientDto.getMandant());
				}

				bdEinstandspreiseTemp[i] = BigDecimal.ZERO;
				// --------------------------------------------------------------
				// ---
				// Schritt 2: (Gelieferter Preis + antlg. Fixkosten) abzgl.
				// Rabatt
				// --------------------------------------------------------------
				// ---
				// Gelieferter Preis (sofern definiert)
				if (!istKopfartikel && aWEPOSDto[i].getNGelieferterpreis() != null) {
					bdEinstandspreiseTemp[i] = bdEinstandspreiseTemp[i].add(aWEPOSDto[i].getNGelieferterpreis());
				}
				// Gelieferter Preis (sofern definiert)
				if (!istKopfartikel && aWEPOSDto[i].getNAnteiligefixkosten() != null) {
					bdEinstandspreiseTemp[i] = bdEinstandspreiseTemp[i].add(aWEPOSDto[i].getNAnteiligefixkosten());
				}
				// Rabatt berechnen.
				BigDecimal bdRabatt = new BigDecimal(0);
				if (!istKopfartikel && wareneingangDtoI.getFRabattsatz() != null) {
					bdRabatt = Helper.getProzentWert(bdEinstandspreiseTemp[i],
							new BigDecimal(wareneingangDtoI.getFRabattsatz()),
							WareneingangFac.I_NACHKOMMASTELLEN_EINSTANDSPREISE);
					bdEinstandspreiseTemp[i] = bdEinstandspreiseTemp[i].subtract(bdRabatt);
				}
				aWEPOSDto[i].setNRabattwert(bdRabatt);
				// Die Menge
				bdGelieferteMenge[i] = aWEPOSDto[i].getNGeliefertemenge();
			}

			// -----------------------------------------------------------------
			// Schritt 3: gewichtete Transportkosten ermitteln.
			// -----------------------------------------------------------------

			BigDecimal[] bdAnteiligeTransportkosten = Helper.teileBetragAnteiligAuf(bdEinstandspreiseTemp,
					bdGelieferteMenge, wareneingangDtoI.getSummeTransportkostenUndSpesen(),
					WareneingangFac.I_NACHKOMMASTELLEN_EINSTANDSPREISE);
			for (int i = 0; i < aWEPOSDto.length; i++) {
				BigDecimal bdEinstandspreis = bdEinstandspreiseTemp[i].add(bdAnteiligeTransportkosten[i]);
				// --------------------------------------------------------------
				// ---
				// Schritt 3: gewichtete Transportkosten ermitteln.
				// --------------------------------------------------------------
				// ---

				if (wareneingangDtoI.getDGemeinkostenfaktor() != null) {
					BigDecimal fGKFaktor = new BigDecimal(wareneingangDtoI.getDGemeinkostenfaktor().floatValue());
					BigDecimal bdGK = Helper.getProzentWert(bdEinstandspreis, fGKFaktor,
							WareneingangFac.I_NACHKOMMASTELLEN_EINSTANDSPREISE);
					bdEinstandspreis = bdEinstandspreis.add(bdGK);
				}

				if (bVorsteuer) {

					if (ldfDto.getMwstsatzbezIId() != null) {
						MwstsatzDto mwstsatzDtoZumBelegdatum = getMandantFac().mwstsatzFindZuDatum(
								ldfDto.getMwstsatzbezIId(), new Timestamp(bsDto.getDBelegdatum().getTime()));
						if (mwstsatzDtoZumBelegdatum != null && mwstsatzDtoZumBelegdatum.getFMwstsatz() != 0) {
							BigDecimal mwstBetrag = bdEinstandspreis
									.multiply(new BigDecimal(mwstsatzDtoZumBelegdatum.getFMwstsatz().doubleValue())
											.movePointLeft(2));
							bdEinstandspreis = bdEinstandspreis.add(mwstBetrag);
						}
					}
				}

				aWEPOSDto[i].setNEinstandspreis(bdEinstandspreis);
				aWEPOSDto[i].setNAnteiligetransportkosten(bdAnteiligeTransportkosten[i]);
			}

			// Eintraege speichern.
			for (int i = 0; i < aWEPOSDto.length; i++) {
				updateWareneingangsposition(aWEPOSDto[i], false, false, theClientDto);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void geliefertPreiseAllerWEPRueckpflegen(java.sql.Date dVon, java.sql.Date dBis, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", Helper.locale2String(theClientDto.getLocUi()));

		String queryString = "SELECT wep FROM FLRWareneingangspositionen wep WHERE wep.flrbestellposition.flrbestellung.bestellungstatus_c_nr<>'"
				+ BestellungFac.BESTELLSTATUS_STORNIERT + "' ";

		if (dVon != null) {
			queryString += " AND wep.flrwareneingang.t_wareneingansdatum >='" + Helper.formatDateWithSlashes(dVon)
					+ "'";
		}

		if (dBis != null) {
			queryString += " AND wep.flrwareneingang.t_wareneingansdatum <='" + Helper.formatDateWithSlashes(dBis)
					+ "'";
		}

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator it = resultList.iterator();
		while (it.hasNext()) {
			FLRWareneingangspositionen wep = (FLRWareneingangspositionen) it.next();

			if (wep.getN_gelieferterpreis() != null) {
				BestellpositionDto besPosDto = null;
				try {
					besPosDto = getBestellpositionFac().bestellpositionFindByPrimaryKey(wep.getBestellposition_i_id());
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				besPosDto.setNNettogesamtpreis(wep.getN_gelieferterpreis());

				// Rabatt berechnen

				// Rabatt berechnen
				BigDecimal rabattsatz = null;
				if (besPosDto.getNNettoeinzelpreis().doubleValue() != 0) {

					rabattsatz = new BigDecimal(1).subtract(wep.getN_gelieferterpreis()
							.divide(besPosDto.getNNettoeinzelpreis(), 4, BigDecimal.ROUND_HALF_EVEN));
					rabattsatz = rabattsatz.multiply(new BigDecimal(100));

				} else {
					rabattsatz = new BigDecimal(100);
				}

				besPosDto.setDRabattsatz(rabattsatz.doubleValue());

				getBestellpositionFac().preispflege(besPosDto,
						BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_EINZELPREIS_RUECKPFLEGEN, null, false,
						theClientDto);

			}
		}

	}

	public EinstandspreiseEinesWareneingangsDto getBerechnetenEinstandspreisEinerWareneingangsposition(
			Integer wareneingangIId, TheClientDto theClientDto) {

		// WENN HIER ETWAS GEAENDERT WIRD, MUSS AUCH IN
		// aktualisiereEinstandspreise() geaendert werden

		// ---------------------------------------------------------------------
		// Schritt 1: als erstes muessen die Fixkosten verteilt werden.
		// Dabei wird auch ermittelt, welche Wareneingaenge betroffen sind.
		// ---------------------------------------------------------------------

		EinstandspreiseEinesWareneingangsDto preiseDtos = new EinstandspreiseEinesWareneingangsDto();

		WareneingangDto wareneingangDtoI = wareneingangFindByPrimaryKey(wareneingangIId);
		// ------------------------------------------------------------------
		// -
		// nun die Einstandspreise der Wareneingaenge aufrollen.
		// ------------------------------------------------------------------
		// -
		WareneingangspositionDto[] aWEPOSDto = wareneingangspositionFindByWareneingangIId(wareneingangIId);
		// Preise in Arrays zwischenspeichern, da spaeter eine
		// nach Preis und Menge gewichtete Verteilung der Transportkosten
		// erfolgt.
		BigDecimal[] bdEinstandspreiseTemp = new BigDecimal[aWEPOSDto.length];
		BigDecimal[] bdGelieferteMenge = new BigDecimal[aWEPOSDto.length];
		for (int i = 0; i < aWEPOSDto.length; i++) {
			bdEinstandspreiseTemp[i] = new BigDecimal(0);
			// --------------------------------------------------------------
			// ---
			// Schritt 2: (Gelieferter Preis + antlg. Fixkosten) abzgl.
			// Rabatt
			// --------------------------------------------------------------
			// ---

			Bestellposition bespos = em.find(Bestellposition.class, aWEPOSDto[i].getBestellpositionIId());
			boolean usePosition = true;
			if (bespos.getArtikelIId() != null && bespos.getPositionIIdArtikelset() != null) {
				usePosition = false;
			}

			// Gelieferter Preis (sofern definiert)
			if (usePosition && aWEPOSDto[i].getNGelieferterpreis() != null) {
				bdEinstandspreiseTemp[i] = bdEinstandspreiseTemp[i].add(aWEPOSDto[i].getNGelieferterpreis());
			}
			// Gelieferter Preis (sofern definiert)
			if (usePosition && aWEPOSDto[i].getNAnteiligefixkosten() != null) {
				bdEinstandspreiseTemp[i] = bdEinstandspreiseTemp[i].add(aWEPOSDto[i].getNAnteiligefixkosten());
			}
			// Rabatt berechnen.
			BigDecimal bdRabatt = new BigDecimal(0);
			if (usePosition && wareneingangDtoI.getFRabattsatz() != null) {
				bdRabatt = Helper.getProzentWert(bdEinstandspreiseTemp[i],
						new BigDecimal(wareneingangDtoI.getFRabattsatz()), 10);
				bdEinstandspreiseTemp[i] = bdEinstandspreiseTemp[i].subtract(bdRabatt);
			}
			aWEPOSDto[i].setNRabattwert(bdRabatt);
			// Die Menge
			bdGelieferteMenge[i] = aWEPOSDto[i].getNGeliefertemenge();
		}
		// -----------------------------------------------------------------
		// Schritt 3: gewichtete Transportkosten ermitteln.
		// -----------------------------------------------------------------

		BigDecimal[] bdAnteiligeTransportkosten = Helper.teileBetragAnteiligAuf(bdEinstandspreiseTemp,
				bdGelieferteMenge, wareneingangDtoI.getSummeTransportkostenUndSpesen(), 10);
		for (int i = 0; i < aWEPOSDto.length; i++) {
			BigDecimal bdEinstandspreis = bdEinstandspreiseTemp[i].add(bdAnteiligeTransportkosten[i]);
			// --------------------------------------------------------------
			// ---
			// Schritt 3: gewichtete Transportkosten ermitteln.
			// --------------------------------------------------------------
			// ---

			// wg. SP2783 auskommentiert
			/*
			 * if (wareneingangDtoI.getDGemeinkostenfaktor() != null) { BigDecimal fGKFaktor
			 * = new BigDecimal(wareneingangDtoI .getDGemeinkostenfaktor().floatValue());
			 * BigDecimal bdGK = Helper.getProzentWert(bdEinstandspreis, fGKFaktor, 10);
			 * bdEinstandspreis = bdEinstandspreis.add(bdGK); }
			 */

			preiseDtos.getHmEinstandpreiseAllerPositionen().put(aWEPOSDto[i].getIId(), bdEinstandspreis);

		}

		return preiseDtos;
	}

	/**
	 * verteileFixkostenaufWareneingangspositionen.
	 * 
	 * @param bestellpositionIIdI Integer
	 * @param theClientDto        String
	 * @throws EJBExceptionLP
	 * @return Set I_ID's der betroffenen Wareneingaenge. diese muessen neu
	 *         aufgerollt werden.
	 */
	private Set<Integer> verteileFixkostenaufWareneingangspositionen(Integer bestellpositionIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Set<Integer> sBetroffeneWareneingaenge = new HashSet<Integer>();
		try {
			BestellpositionDto bestPosDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(bestellpositionIIdI);
			BigDecimal bdFixkosten = bestPosDto.getNFixkostengeliefert();
			if (bdFixkosten == null) {
				// es wurden keine angegeben -> 0.
				bdFixkosten = new BigDecimal(0);
			}
			WareneingangspositionDto[] wePos = wareneingangspositionFindByBestellpositionIId(bestellpositionIIdI);
			if (wePos.length > 0) {
				// Gesamtmenge bestimmen.
				BigDecimal bdMenge = new BigDecimal(0);
				for (int i = 0; i < wePos.length; i++) {
					bdMenge = bdMenge.add(wePos[i].getNGeliefertemenge());
					sBetroffeneWareneingaenge.add(wePos[i].getWareneingangIId());
				}
				// Nun die Fixkosten je Einheit berechnen.
				BigDecimal bdFixkostenJeEinheit = BigDecimal.ZERO;
				if (bdMenge.signum() != 0) {
					bdFixkostenJeEinheit = bdFixkosten.divide(bdMenge,
							WareneingangFac.I_NACHKOMMASTELLEN_EINSTANDSPREISE, RoundingMode.HALF_EVEN);
				}
				// nun bei den einzelnen WE-Pos. speichern.
				for (int i = 0; i < wePos.length; i++) {
					wePos[i].setNAnteiligefixkosten(bdFixkostenJeEinheit);
					updateWareneingangsposition(wePos[i], false, false, theClientDto);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sBetroffeneWareneingaenge;
	}

	public WareneingangspositionDto wareneingangspositionFindByPrimaryKey(Integer iIdWareneingangpositionI)
			throws EJBExceptionLP {

		if (iIdWareneingangpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iIdWareneingangpositionI == null"));
		}

		WareneingangspositionDto wareneingangspositionDto = wareneingangspositionFindByPrimaryKeyOhneExc(
				iIdWareneingangpositionI);
		if (wareneingangspositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return wareneingangspositionDto;

	}

	public WareneingangspositionDto wareneingangspositionFindByPrimaryKeyOhneExc(Integer iIdWareneingangpositionI) {
		Wareneingangsposition wareneingangsposition = em.find(Wareneingangsposition.class, iIdWareneingangpositionI);
		return wareneingangsposition == null ? null : assembleWareneingangspositionDto(wareneingangsposition);
	}

	private void setWareneingangspositionFromWareneingangspositionDto(Wareneingangsposition wepos,
			WareneingangspositionDto weposDto) {
		wepos.setWareneingangIId(weposDto.getWareneingangIId());
		wepos.setNGeliefertemenge(weposDto.getNGeliefertemenge());
		wepos.setNGelieferterpreis(weposDto.getNGelieferterpreis());
		wepos.setNRabattwert(weposDto.getNRabattwert());
		wepos.setNAnteiligetransportkosten(weposDto.getNAnteiligetransportkosten());
		wepos.setNEinstandspreis(weposDto.getNEinstandspreis());
		wepos.setBestellpositionIId(weposDto.getBestellpositionIId());
		wepos.setTAnlegen(weposDto.getTAnlegen());
		wepos.setTAendern(weposDto.getTAendern());
		wepos.setPersonalIIdAnlegen(weposDto.getPersonalIIdAnlegen());
		wepos.setPersonalIIdAendern(weposDto.getPersonalIIdAendern());
		wepos.setXInternerkommentar(weposDto.getXInternerKommentar());
		wepos.setTManuellerledigt(weposDto.getTManuellErledigt());
		wepos.setNAnteiligefixkosten(weposDto.getNAnteiligefixkosten());
		wepos.setBPreiseerfasst(Helper.boolean2Short(weposDto.getBPreiseErfasst()));
		wepos.setNBreiteInMm(weposDto.getNBreiteInMm());
		wepos.setNTiefeInMm(weposDto.getNTiefeInMm());
		wepos.setIRn(weposDto.getIRn());
		em.merge(wepos);
		em.flush();
	}

	/**
	 * Die Anzahl der WE-Positionen eines Wareneingangs bestimmen.
	 * 
	 * @param iIdWareneingangI Integer
	 * @return Integer
	 * @throws EJBExceptionLP
	 */
	public Integer getAnzahlWEP(Integer iIdWareneingangI) throws EJBExceptionLP {
		if (iIdWareneingangI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdBestellungI == null"));
		}
		return wareneingangspositionFindByWareneingangIId(iIdWareneingangI).length;
	}

	public Integer getLieferscheinIIdAusWareUnterwegs(String wareunterwegsCNr, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT w FROM FLRWarezwischenmandanten w WHERE w.c_nr='" + wareunterwegsCNr + "'";

		Integer lieferscheinIId = null;

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator it = resultList.iterator();
		if (it.hasNext()) {
			FLRWarezwischenmandanten wep = (FLRWarezwischenmandanten) it.next();
			if (wep.getFlrlieferscheinposition() != null) {
				lieferscheinIId = wep.getFlrlieferscheinposition().getLieferschein_i_id();
			}

		}

		session.close();
		return lieferscheinIId;
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	// ruft der Client auf(!)
	public void wareUnterwegsZubuchen(Integer lieferscheinIId, ArrayList<String> wareunterwegsCNr,
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT w FROM FLRWarezwischenmandanten w WHERE w.flrlieferscheinposition.wareneingangsposition_i_id_anderermandant IS NULL";

		if (lieferscheinIId != null) {
			queryString += " AND w.flrlieferscheinposition.lieferschein_i_id=" + lieferscheinIId;
		} else if (wareunterwegsCNr != null) {

			Iterator it = wareunterwegsCNr.iterator();
			String in = "";
			while (it.hasNext()) {
				in += "'" + (String) it.next() + "'";

				if (it.hasNext()) {
					in += ",";
				}

			}

			queryString += " AND w.c_nr IN(" + in + ")";
		}

		HashMap<Integer, Integer> hmBestellungenWareneingaenge = new HashMap<Integer, Integer>();

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator it = resultList.iterator();
		while (it.hasNext()) {
			FLRWarezwischenmandanten w = (FLRWarezwischenmandanten) it.next();

			Integer bestellungIId = w.getFlrbestellposition().getFlrbestellung().getI_id();

			if (theClientDto.getMandant().equals(w.getFlrbestellposition().getFlrbestellung().getMandant_c_nr())) {

				if (w.getFlrlieferscheinposition().getN_menge() != null
						&& w.getFlrlieferscheinposition().getWareneingangsposition_i_id_anderermandant() == null) {
					try {
						LieferscheinpositionDto lsPosDto = getLieferscheinpositionFac()
								.lieferscheinpositionFindByPrimaryKey(w.getFlrlieferscheinposition().getI_id(),
										theClientDto);

						Integer wareneingangIId = null;

						if (hmBestellungenWareneingaenge.containsKey(bestellungIId)) {
							wareneingangIId = hmBestellungenWareneingaenge.get(bestellungIId);
						} else {
							// Wareneingang anlegen
							WareneingangDto weDto = new WareneingangDto();
							weDto.setBestellungIId(bestellungIId);
							weDto.setCLieferscheinnr(w.getFlrlieferscheinposition().getFlrlieferschein().getC_nr());
							weDto.setTLieferscheindatum(new Timestamp(
									w.getFlrlieferscheinposition().getFlrlieferschein().getD_belegdatum().getTime()));
							weDto.setFRabattsatz(0D);
							weDto.setNWechselkurs(BigDecimal.ONE);

							weDto.setTWareneingangsdatum(
									Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));
							LieferantDto lfDto = getLieferantFac().lieferantFindByPrimaryKey(
									w.getFlrbestellposition().getFlrbestellung().getFlrlieferant().getI_id(),
									theClientDto);
							weDto.setLagerIId(lfDto.getLagerIIdZubuchungslager());

							wareneingangIId = createWareneingang(weDto, theClientDto);
							hmBestellungenWareneingaenge.put(bestellungIId, wareneingangIId);

						}

						// Wareneingangsposition anlegen

						WareneingangspositionDto weposDto = new WareneingangspositionDto();
						weposDto.setNGeliefertemenge(w.getFlrlieferscheinposition().getN_menge());

						weposDto.setWareneingangIId(wareneingangIId);
						weposDto.setBestellpositionIId(w.getFlrbestellposition().getI_id());

						weposDto.setSeriennrChargennrMitMenge(lsPosDto.getSeriennrChargennrMitMenge());

						BigDecimal materialzuschlag = w.getFlrlieferscheinposition().getN_materialzuschlag();

						if (materialzuschlag != null) {
							weposDto.setNGelieferterpreis(w.getFlrlieferscheinposition()
									.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt().add(materialzuschlag));

						} else {
							weposDto.setNGelieferterpreis(w.getFlrlieferscheinposition()
									.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt());

						}

						// PJ20146
						Query queryUK = em
								.createNamedQuery("WareneingangspositionfindByWareneingangIIdAndBestellpositionIId");
						queryUK.setParameter(1, wareneingangIId);
						queryUK.setParameter(2, w.getFlrbestellposition().getI_id());
						Collection<?> cUK = queryUK.getResultList();
						if (cUK.size() > 0) {
							ArrayList alInfos = new ArrayList();

							// Bestellnummer
							alInfos.add(w.getFlrbestellposition().getFlrbestellung().getC_nr());
							// BS-Posnummer
							alInfos.add(getBestellpositionFac().getPositionNummer(w.getFlrbestellposition().getI_id(),
									theClientDto));

							// Artikel
							alInfos.add(w.getFlrbestellposition().getFlrartikel().getC_nr());

							// Lieferscheinnummer
							alInfos.add(w.getFlrlieferscheinposition().getFlrlieferschein().getC_nr());
							// Lieferscheinpositionsnummer
							alInfos.add(
									getLieferscheinpositionFac().getPositionNummer(w.getLieferscheinposition_i_id()));

							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_AUSLIEFERVORSCHLAG_BSPOS_ZU_WE_BEREITS_VORHANDEN, alInfos,
									new Exception("FEHLER_AUSLIEFERVORSCHLAG_BSPOS_ZU_WE_BEREITS_VORHANDEN"));

						}

						Integer weposIId = createWareneingangsposition(weposDto, false, true, theClientDto);

						Lieferscheinposition lspos = em.find(Lieferscheinposition.class,
								w.getFlrlieferscheinposition().getI_id());

						lspos.setWareneingangspositionIIdAndererMandant(weposIId);
						em.merge(lspos);
						em.flush();

					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}
				}
			}

		}

		session.close();

	}

	/**
	 * Nur den internen Kommentar einer Wareneingangsposition updaten.
	 * 
	 * @param wepDto       WareneingangspositionDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void updateWareneingangspositionInternenKommentar(WareneingangspositionDto wepDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Wareneingangsposition wePos = em.find(Wareneingangsposition.class, wepDto.getIId());
		if (wePos == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		wePos.setXInternerkommentar(wepDto.getXInternerKommentar());
		// wer, wann.
		wePos.setTAendern(getTimestamp());
		wePos.setPersonalIIdAendern(theClientDto.getIDPersonal());
	}

	/**
	 * Alle WE-Positionen zu einer Bestellposition finden.
	 * 
	 * @param iIdBestellpositionI Integer
	 * @return WareneingangspositionDto[]
	 * @throws EJBExceptionLP
	 */
	public WareneingangspositionDto[] wareneingangspositionFindByBestellpositionIId(Integer iIdBestellpositionI)
			throws EJBExceptionLP {
		if (iIdBestellpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iIdBestellpositionI == null"));
		}
		WareneingangspositionDto[] wepDtos = null;
		try {
			Query query = em.createNamedQuery("WareneingangspositionfindByBestellpositionIId");
			query.setParameter(1, iIdBestellpositionI);
			Collection<?> c = query.getResultList();
			wepDtos = assembleWareneingangspositionDtos(c);
		} catch (NoResultException ex) {
			return null;
		}
		return wepDtos;
	}

	/**
	 * Die Anzahl der Wareneingaenge zu einer Bestellung ermitteln.
	 * 
	 * @param iIdBestellungI Integer
	 * @return Integer
	 * @throws EJBExceptionLP
	 */
	public Integer getAnzahlWE(Integer iIdBestellungI) throws EJBExceptionLP {
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdBestellungI == null"));
		}
		// try {
		Query query = em.createNamedQuery("WareneingangfindByBestellungIId");
		query.setParameter(1, iIdBestellungI);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return new Integer(c.size());
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public String generiereAutomatischeChargennummerAnhandBestellnummerWEPNrPosnr(Integer bsposIId,
			Integer wareneingangIId) {
		try {
			WareneingangDto weDto = wareneingangFindByPrimaryKey(wareneingangIId);
			BestellungDto bsDto = getBestellungFac().bestellungFindByPrimaryKey(weDto.getBestellungIId());

			// PJ20059
			String bestellnummer = bsDto.getCNr();

			String wenr = Helper.fitString2LengthAlignRight(weDto.getISort() + "", 2, '0');

			String posnr = Helper.fitString2LengthAlignRight(
					getBestellpositionFac().getPositionNummerReadOnly(bsposIId) + "", 3, '0');
			return bestellnummer + "-" + wenr + "-" + posnr;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	public void removeWareneingang(WareneingangDto wareneingangDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		if (wareneingangDto != null) {
			Wareneingang toRemove = em.find(Wareneingang.class, wareneingangDto.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
			// die Sortierung muss angepasst werden
			sortierungAnpassenBeiLoeschenEinerPosition(wareneingangDto.getBestellungIId(),
					wareneingangDto.getISort().intValue());
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public WareneingangDto wareneingangFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		WareneingangDto wareneingangDto = wareneingangFindByPrimaryKeyOhneExc(iId);
		if (wareneingangDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return wareneingangDto;
	}

	public WareneingangDto wareneingangFindByPrimaryKeyOhneExc(Integer iId) {
		Wareneingang wareneingang = em.find(Wareneingang.class, iId);
		return wareneingang == null ? null : assembleWareneingangDto(wareneingang);
	}

	private void setWareneingangFromWareneingangDto(Wareneingang wareneingang, WareneingangDto wareneingangDto) {
		wareneingang.setISort(wareneingangDto.getISort());
		wareneingang.setCLieferscheinnr(wareneingangDto.getCLieferscheinnr());
		wareneingang.setTLieferscheindatum(wareneingangDto.getTLieferscheindatum());
		wareneingang.setNTransportkosten(wareneingangDto.getNTransportkosten());
		wareneingang.setNBankspesen(wareneingangDto.getNBankspesen());
		wareneingang.setNZollkosten(wareneingangDto.getNZollkosten());
		wareneingang.setNSonstigespesen(wareneingangDto.getNSonstigespesen());
		wareneingang.setFGemeinkostenfaktor(wareneingangDto.getDGemeinkostenfaktor());
		wareneingang.setFRabattsatz(wareneingangDto.getFRabattsatz());
		wareneingang.setTWareneingangsdatum(wareneingangDto.getTWareneingangsdatum());
		wareneingang.setBestellungIId(wareneingangDto.getBestellungIId());
		wareneingang.setLagerIId(wareneingangDto.getLagerIId());
		wareneingang.setNWechselkurs(wareneingangDto.getNWechselkurs());
		wareneingang.setEingangsrechnungIId(wareneingangDto.getEingangsrechnungIId());
		em.merge(wareneingang);
		em.flush();
	}

	/**
	 * Zwei bestehende Wareneingaenge in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdPosition1I PK der ersten Position
	 * @param iIdPosition2I PK der zweiten Position
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void vertauscheWareneingang(Integer iIdPosition1I, Integer iIdPosition2I) throws EJBExceptionLP {
		// try {
		Wareneingang oPosition1 = em.find(Wareneingang.class, iIdPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Wareneingang oPosition2 = em.find(Wareneingang.class, iIdPosition2I);

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		// das zweite iSort auf ungueltig setzen, damit UK constraint nicht
		// verletzt wird
		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iIdBestellungI           PK der Bestellung
	 * @param iSortierungNeuePositionI die Stelle, an der eingefuegt werden soll
	 * @throws EJBExceptionLP Ausnahme
	 */
	private void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer iIdBestellungI,
			int iSortierungNeuePositionI) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("WareneingangfindByWareneingang");
		query.setParameter(1, iIdBestellungI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Wareneingang oPosition = (Wareneingang) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Wenn fuer eine Bestellung eine Position geloescht wurden, dann muss die
	 * Sortierung der Positionen angepasst werden, damit keine Luecken entstehen.
	 * <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server aufgerufen.
	 * 
	 * @param iIdBestellungI                 PK der Bestellung
	 * @param iSortierungGeloeschtePositionI die Position der geloschten Position
	 * @throws EJBExceptionLP Ausnahme
	 */
	private void sortierungAnpassenBeiLoeschenEinerPosition(Integer iIdBestellungI, int iSortierungGeloeschtePositionI)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("WareneingangfindByWareneingang");
		query.setParameter(1, iIdBestellungI);
		Collection<?> clPositionen = query.getResultList();
		// if (clPositionen.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		Iterator<?> it = clPositionen.iterator();

		while (it.hasNext()) {
			Wareneingang oPosition = (Wareneingang) it.next();

			if (oPosition.getISort().intValue() > iSortierungGeloeschtePositionI) {
				oPosition.setISort(new Integer(iSortierungGeloeschtePositionI));
				iSortierungGeloeschtePositionI++;
			}
		}
	}

	public WareneingangDto[] wareneingangFindByBestellungIId(Integer iIdBestellungI) throws EJBExceptionLP {
		Query query = em.createNamedQuery("WareneingangfindByWareneingang");
		query.setParameter(1, iIdBestellungI);
		Collection<?> cl = query.getResultList();
		return assembleWareneingangDtos(cl);
	}

	public WareneingangDto[] wareneingangFindByEingangsrechnungIId(Integer eingangsrechnungIId) {
		Query query = em.createNamedQuery("WareneingangfindByEingangsrechnungIId");
		query.setParameter(1, eingangsrechnungIId);
		Collection<?> cl = query.getResultList();
		return assembleWareneingangDtos(cl);
	}

	public WareneingangspositionDto[] wareneingangspositionFindByWareneingangIId(Integer iIdWareneingangI)
			throws EJBExceptionLP {
		if (iIdWareneingangI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdWareneingangI == null"));
		}
		WareneingangspositionDto[] wepDtos = null;
		// try {
		Query query = em.createNamedQuery("WareneingangspositionfindByWareneingangIId");
		query.setParameter(1, iIdWareneingangI);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		wepDtos = assembleWareneingangspositionDtos(c);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return wepDtos;
	}

	/**
	 * Wareneingangsposition updaten.
	 * 
	 * @param weposDtoI    WareneingangspositionDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */

	public void updateWareneingangspositionOhnePreisberechnung(WareneingangspositionDto weposDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		updateWareneinganspositionImpl(weposDtoI, false, true, theClientDto);
		// try {
		// // Gelieferte Fixkosten in die Bestellposition uebertragen.
		// setGelieferteFixkosten2Bestellposition(
		// weposDtoI.getBestellpositionIId(),
		// weposDtoI.getNFixkosten4Bestellposition(), theClientDto);
		// updateWareneingangsposition(weposDtoI, false, true, theClientDto);
		// // Positionsstatus neu bestimmen
		// getBestellpositionFac().versucheBestellpositionAufErledigtZuSetzen(
		// weposDtoI.getBestellpositionIId(), theClientDto);
		// } catch (RemoteException ex) {
		// throwEJBExceptionLPRespectOld(ex);
		// }
	}

	public void updateWareneingangsposition(WareneingangspositionDto weposDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		updateWareneinganspositionImpl(weposDtoI, true, true, theClientDto);
		// try {
		// // Gelieferte Fixkosten in die Bestellposition uebertragen.
		// setGelieferteFixkosten2Bestellposition(
		// weposDtoI.getBestellpositionIId(),
		// weposDtoI.getNFixkosten4Bestellposition(), theClientDto);
		// updateWareneingangsposition(weposDtoI, true, true, theClientDto);
		// // Positionsstatus neu bestimmen
		// getBestellpositionFac().versucheBestellpositionAufErledigtZuSetzen(
		// weposDtoI.getBestellpositionIId(), theClientDto);
		// } catch (RemoteException ex) {
		// throwEJBExceptionLPRespectOld(ex);
		// }
	}

	public void updateWareneingangsposition(WareneingangspositionDto weposDtoI, boolean setartikelAufloesen,
			TheClientDto theClientDto) throws EJBExceptionLP {
		updateWareneinganspositionImpl(weposDtoI, true, setartikelAufloesen, theClientDto);
		// try {
		// // Gelieferte Fixkosten in die Bestellposition uebertragen.
		// setGelieferteFixkosten2Bestellposition(
		// weposDtoI.getBestellpositionIId(),
		// weposDtoI.getNFixkosten4Bestellposition(), theClientDto);
		// updateWareneingangsposition(weposDtoI, true, setartikelAufloesen,
		// theClientDto);
		// getBestellpositionFac().versucheBestellpositionAufErledigtZuSetzen(
		// weposDtoI.getBestellpositionIId(), theClientDto);
		// } catch (RemoteException ex) {
		// throwEJBExceptionLPRespectOld(ex);
		// }
	}

	private void updateWareneinganspositionImpl(WareneingangspositionDto posDto, boolean updateEinstandspreis,
			boolean setartikelAufloesen, TheClientDto theClientDto) {
		try {
			// Gelieferte Fixkosten in die Bestellposition uebertragen.
			setGelieferteFixkosten2Bestellposition(posDto.getBestellpositionIId(),
					posDto.getNFixkosten4Bestellposition(), theClientDto);
			updateWareneingangsposition(posDto, updateEinstandspreis, setartikelAufloesen, theClientDto);
			getBestellpositionFac().versucheBestellpositionAufErledigtZuSetzen(posDto.getBestellpositionIId(),
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Wareneingangsposition updaten.
	 * 
	 * @param weposDtoI              WareneingangspositionDto
	 * @param bUpdateEinstandspreise boolean
	 * @param theClientDto           String
	 * @throws EJBExceptionLP
	 */
	private void updateWareneingangsposition(final WareneingangspositionDto weposDtoI, boolean bUpdateEinstandspreise,
			boolean bArtikelsetaufloesen, TheClientDto theClientDto) throws EJBExceptionLP {
		// Preconditions.
		if (weposDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("weposDtoI == null"));
		}
		// wer, wann
		weposDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
		weposDtoI.setTAendern(getTimestamp());
		// speichern.
		Wareneingangsposition wepos = em.find(Wareneingangsposition.class, weposDtoI.getIId());
		if (wepos == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}

		BigDecimal bdDelta = weposDtoI.getNGeliefertemenge().subtract(wepos.getNGeliefertemenge());

		// Lagerbuchung.
		bucheWareneingangspositionAmLager(weposDtoI, false, theClientDto);

		setWareneingangspositionFromWareneingangspositionDto(wepos, weposDtoI);

		// Einstandspreise aufrollen.
		if (bUpdateEinstandspreise) {

			aktualisiereEinstandspreise(weposDtoI.getBestellpositionIId(),
					verteileFixkostenaufWareneingangspositionen(weposDtoI.getBestellpositionIId(), theClientDto),
					theClientDto);

		}

		try {
			BestellpositionDto bestPosDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(weposDtoI.getBestellpositionIId());

			// PJ20372
			if (bdDelta.doubleValue() > 0) {
				WareneingangDto weDto = wareneingangFindByPrimaryKey(weposDtoI.getWareneingangIId());
				fremdarbeitsmaterialInsLosBuchen(bdDelta,

						weposDtoI, theClientDto, weDto, bestPosDto);

				emailAnAnfordererSenden(theClientDto, weDto, bdDelta, bestPosDto);

			}

			if (bArtikelsetaufloesen == false && bestPosDto.getPositioniIdArtikelset() == null) {
				preiseEinesArtikelsetsUpdaten(weposDtoI, theClientDto);
			}

			if (bArtikelsetaufloesen == true && bestPosDto.getPositioniIdArtikelset() == null) {

				// BigDecimal faktor = weposDtoI.getNGeliefertemenge().divide(
				// bestPosDto.getNMenge(), BigDecimal.ROUND_HALF_EVEN);
				//
				BigDecimal headAmount = bestPosDto.getNMenge();
				BigDecimal newAmount = weposDtoI.getNGeliefertemenge();
				int scale = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());

				List<Bestellposition> bestellpositions = BestellpositionQuery.listByPositionIIdArtikelset(em,
						weposDtoI.getBestellpositionIId());
				for (Bestellposition bsSet : bestellpositions) {
					WareneingangspositionDto[] weDtos = wareneingangspositionFindByBestellpositionIId(bsSet.getIId());
					WareneingangspositionDto weDto = CollectionTools.detect(weDtos,
							new IDetect<WareneingangspositionDto>() {
								public boolean accept(WareneingangspositionDto posDto) {
									return posDto.getWareneingangIId().equals(weposDtoI.getWareneingangIId());
								};
							});
					if (weDto != null) {
						weDto.setNGeliefertemenge(newAmount.multiply(bsSet.getNMenge()).divide(headAmount, scale,
								RoundingMode.HALF_EVEN));
						weDto.setBPreiseErfasst(weposDtoI.getBPreiseErfasst());
						updateWareneingangsposition(weDto, bUpdateEinstandspreise, false, theClientDto);
					}

					// for (int i = 0; i < weDtos.length; i++) {
					// WareneingangspositionDto weDto = weDtos[i];
					//
					// if (weDto.getWareneingangIId().equals(
					// weposDtoI.getWareneingangIId())) {
					//
					// weDto.setNGeliefertemenge(newAmount.multiply(bsSet.getNMenge())
					// .divide(headAmount, scale, RoundingMode.HALF_EVEN));
					// // weDto.setNGeliefertemenge(Helper
					// // .rundeKaufmaennisch(bsSet.getNMenge()
					// // .multiply(faktor), 3));
					//
					// weDto.setBPreiseErfasst(weposDtoI
					// .getBPreiseErfasst());
					// updateWareneingangsposition(weDto,
					// bUpdateEinstandspreise, false, theClientDto);
					// }
					// }
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	public BigDecimal berechneWertDesWareneingangsInBestellungswaehrung(Integer wareneingangIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		WareneingangDto weDto = wareneingangFindByPrimaryKey(wareneingangIIdI);

		BigDecimal wert = weDto.getSummeTransportkostenUndSpesen();

		BestellungDto bsDto = null;
		try {
			bsDto = getBestellungFac().bestellungFindByPrimaryKey(weDto.getBestellungIId());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		BigDecimal bdAllgemeinerRabatt = new BigDecimal(bsDto.getFAllgemeinerRabattsatz().doubleValue())
				.movePointLeft(2);
		bdAllgemeinerRabatt = Helper.rundeKaufmaennisch(bdAllgemeinerRabatt, 4);

		WareneingangspositionDto[] weposDtos = wareneingangspositionFindByWareneingangIId(wareneingangIIdI);
		for (int i = 0; i < weposDtos.length; i++) {

			// Preis
			if (weposDtos[i].getNGelieferterpreis() != null && weposDtos[i].getNGeliefertemenge() != null) {

				BigDecimal bdNettogesamtpreisAllgemeinerRabattSumme = weposDtos[i].getNGelieferterpreis()
						.multiply(bdAllgemeinerRabatt);

				BigDecimal bdPosWert = weposDtos[i].getNGelieferterpreis()
						.subtract(bdNettogesamtpreisAllgemeinerRabattSumme)
						.multiply(weposDtos[i].getNGeliefertemenge());

				wert = wert.add(bdPosWert);
			}

		}

		return wert;
	}

	/**
	 * @todo den restlichen source checken
	 * 
	 * 
	 *       Eine Bestellung wird von geliefert auf offen gesetzt wenn alle
	 *       WEPositionen geloescht sind
	 * @param iIdBestellungI Integer
	 * @param theClientDto   String
	 */
	public void geliefertAufOffenOderBestaetigt(Integer iIdBestellungI, TheClientDto theClientDto) {

		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
		}
		try {
			BestellungDto oBestellung = getBestellungFac().bestellungFindByPrimaryKey(iIdBestellungI);
			// status geliefert
			if (oBestellung.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_GELIEFERT)) {
				BestellpositionDto besposDto[] = null;
				besposDto = getBestellpositionFac().bestellpositionFindByBestellung(oBestellung.getIId(), theClientDto);
				if (besposDto != null) {
					// wenn jede Position eine ABTermin hat dann wird die
					// Bestellung
					// auf bestatigt zurueckgesetzt
					int countABTermin = 0;
					for (int i = 0; i < besposDto.length; i++) {
						if (besposDto[i].getTAuftragsbestaetigungstermin() != null) {
							countABTermin++;
						}
					}
					if (countABTermin == besposDto.length) {
						oBestellung.setStatusCNr(BestellungFac.BESTELLSTATUS_BESTAETIGT);
						getBestellungFac().updateBestellung(oBestellung, theClientDto);
					}
					// sonst auf offen
					else {
						oBestellung.setStatusCNr(BestellungFac.BESTELLSTATUS_OFFEN);
						getBestellungFac().updateBestellung(oBestellung, theClientDto);
					}
				}
				myLogger.logKritisch("Status Geliefert wurde aufgehoben, obwohl die Bestellung"
						+ "nicht manuell erledigt wurde, BestellungIId: " + iIdBestellungI);

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private void preiseEinesArtikelsetsUpdaten(WareneingangspositionDto weposDtoI, TheClientDto theClientDto) {

		try {

			BestellpositionDto positionDtoKopfartikel = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(weposDtoI.getBestellpositionIId());

			Query query = em.createNamedQuery("BestellpositionfindByPositionIIdArtikelset");
			query.setParameter(1, positionDtoKopfartikel.getIId());
			Collection<?> lieferscheinpositionDtos = query.getResultList();

			BestellungDto bestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(positionDtoKopfartikel.getBestellungIId());

			// Zuerst Gesamtwert berechnen
			BigDecimal bdMenge = weposDtoI.getNGeliefertemenge();

			BigDecimal bdNettoeinzelpreis = weposDtoI.getNGelieferterpreis();

			BigDecimal bdGesamtwertposition = bdMenge.multiply(bdNettoeinzelpreis);

			BigDecimal bdGesamtVKwert = new BigDecimal(0);

			Iterator<?> it = lieferscheinpositionDtos.iterator();

			while (it.hasNext()) {
				Bestellposition struktur = (Bestellposition) it.next();

				ArtikellieferantDto artLief = getArtikelFac()
						.artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(struktur.getArtikelIId(),
								bestellungDto.getLieferantIIdBestelladresse(), bestellungDto.getWaehrungCNr(),
								theClientDto);

				if (artLief != null && artLief.getNNettopreis() != null) {
					bdGesamtVKwert = bdGesamtVKwert.add(artLief.getNNettopreis().multiply(struktur.getNMenge()));
				}

			}

			bdGesamtVKwert = Helper.rundeKaufmaennisch(bdGesamtVKwert, 4);

			it = lieferscheinpositionDtos.iterator();

			while (it.hasNext()) {
				Bestellposition struktur = (Bestellposition) it.next();

				WareneingangspositionDto[] weDtos = wareneingangspositionFindByBestellpositionIId(struktur.getIId());

				for (int i = 0; i < weDtos.length; i++) {
					WareneingangspositionDto weDto = weDtos[i];

					if (weDto.getWareneingangIId().equals(weposDtoI.getWareneingangIId())) {

						Wareneingangsposition wareneingangsposition = em.find(Wareneingangsposition.class,
								weDto.getIId());

						ArtikellieferantDto artLief = getArtikelFac()
								.artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(struktur.getArtikelIId(),
										bestellungDto.getLieferantIIdBestelladresse(), bestellungDto.getWaehrungCNr(),
										theClientDto);

						if (artLief != null && artLief.getNNettopreis() != null && bdGesamtVKwert.doubleValue() != 0) {
							// Preis berechnen
							BigDecimal bdAnteilVKWert = artLief.getNNettopreis()
									.multiply(struktur.getNMenge().multiply(bdMenge))
									.divide(bdGesamtVKwert, 4, BigDecimal.ROUND_HALF_EVEN);

							wareneingangsposition.setNEinstandspreis(bdGesamtwertposition.multiply(bdAnteilVKWert)
									.divide(struktur.getNMenge().multiply(bdMenge), 4, BigDecimal.ROUND_HALF_EVEN));

							wareneingangsposition.setNGelieferterpreis(bdGesamtwertposition.multiply(bdAnteilVKWert)
									.divide(struktur.getNMenge().multiply(bdMenge), 4, BigDecimal.ROUND_HALF_EVEN));

						}
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);

		}
	}

	public WEPBuchenReturnDto uebernimmAlleWepsOhneBenutzerinteraktion(Integer iIdWareneingangI, Integer iIdBestellungI,
			ArrayList<Integer> bestellpositionIIds_selektiert, TheClientDto theClientDto) {
		// BestellpositionDto besposDto[] = null;
		// WareneingangspositionDto weposDtos[] = null;
		// WareneingangspositionDto weposDto = null;
		// WareneingangDto wareneingangDto = null;
		// BigDecimal bdOffeneMenge = null;
		// BestellungDto besDto = null;
		// ArtikelDto artikelDto = null;
		ArrayList<Object[]> alData = null;

		TreeMap<String, Integer> tmArtikelIIdsMitKommentar = new TreeMap<String, Integer>();

		try {
			BestellpositionDto[] besposDto = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI,
					theClientDto);
			BestellungDto besDto = getBestellungFac().bestellungFindByPrimaryKey(iIdBestellungI);
			WareneingangDto wareneingangDto = getWareneingangFac().wareneingangFindByPrimaryKey(iIdWareneingangI);
			Integer artikelsetHeadId = null;
			boolean setAufloesen = true;
			for (int i = 0; i < besposDto.length; i++) {

				if (bestellpositionIIds_selektiert != null && bestellpositionIIds_selektiert.size() > 1) {
					if (!bestellpositionIIds_selektiert.contains(besposDto[i].getIId())) {
						continue;
					}
				}

				if (besposDto[i].getNMenge() != null) {
					// Wareneingangsposition wepo = null;
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(besposDto[i].getArtikelIId(),
							theClientDto);

					// PJ22451
					ArrayList<KeyvalueDto> hinweise = getArtikelkommentarFac().getArtikelhinweise(
							besposDto[i].getArtikelIId(), LocaleFac.BELEGART_WARENEINGANG, theClientDto);

					if (hinweise != null && hinweise.size() > 0) {
						tmArtikelIIdsMitKommentar.put(artikelDto.getCNr(), besposDto[i].getArtikelIId());
						continue;
					} else {

						ArrayList<byte[]> bilder = getArtikelkommentarFac().getArtikelhinweiseBild(
								besposDto[i].getArtikelIId(), LocaleFac.BELEGART_WARENEINGANG, theClientDto);

						if (bilder != null && bilder.size() > 0) {
							tmArtikelIIdsMitKommentar.put(artikelDto.getCNr(), besposDto[i].getArtikelIId());
							continue;
						}
					}

					if (besposDto[i].getPositioniIdArtikelset() == null) {
						artikelsetHeadId = null;
						setAufloesen = true;

						// moegliches Artikelset
						StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(
								artikelDto.getIId(), theClientDto.getMandant());
						if (stklDto != null
								&& StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stklDto.getStuecklisteartCNr())) {
							List<Integer> artikelIIds = getStuecklisteFac()
									.getSeriennrChargennrArtikelIIdsFromStueckliste(stklDto.getIId(), BigDecimal.ONE,
											theClientDto);
							if (!artikelIIds.isEmpty()) {
								artikelsetHeadId = besposDto[i].getIId();
								setAufloesen = false;
							}
						}

						// Der Kopfartikel eines nicht aufzuloesenden
						// Artikelsets wird nicht weiter beachtet
						if (!setAufloesen)
							continue;
					} else {
						if (setAufloesen)
							continue;
					}
					try {
						Query query = em
								.createNamedQuery("WareneingangspositionfindByWareneingangIIdAndBestellpositionIId");
						query.setParameter(1, iIdWareneingangI);
						query.setParameter(2, besposDto[i].getIId());
						Wareneingangsposition wepo = (Wareneingangsposition) query.getSingleResult();
						if (!artikelDto.istArtikelSnrOderchargentragend()) {
							WareneingangspositionDto weposDto = assembleWareneingangspositionDto(wepo);
							BigDecimal bdMengeVorher = weposDto.getNGeliefertemenge();
							if (new BigDecimal(0).compareTo(besposDto[i].getNOffeneMenge()) < 0) {
								// Nur wenn noch nicht komplett geliefert
								if (alData == null) {
									alData = new ArrayList<Object[]>();
								}
								weposDto.setNGeliefertemenge(bdMengeVorher.add(besposDto[i].getNOffeneMenge()));
								updateWareneingangspositionOhnePreisberechnung(weposDto, theClientDto);
								Object[] oData = new Object[4];
								oData[0] = artikelDto.getIId();
								oData[1] = wareneingangDto.getLagerIId();
								oData[2] = besposDto[i].getNOffeneMenge();
								oData[3] = besposDto[i].getLossollmaterialIId();
								alData.add(oData);
							}
						}
					} catch (NoResultException ex) {
						WareneingangspositionDto weposDto = new WareneingangspositionDto();
						BigDecimal bdOffeneMenge = getBestellpositionFac().berechneOffeneMenge(besposDto[i].getIId());
						// if (bdOffeneMenge.compareTo(new BigDecimal(0)) != 0)
						// {
						if (bdOffeneMenge.signum() != 0) {
							if (!artikelDto.istArtikelSnrOderchargentragend()) {
								weposDto.setNGeliefertemenge(
										bdOffeneMenge.signum() <= 0 ? BigDecimal.ZERO : bdOffeneMenge);
								// 17967

								weposDto.setNGelieferterpreis(besposDto[i].getNNettogesamtpreis());

								if (besposDto[i].getNMaterialzuschlag() != null
										&& besposDto[i].getNNettogesamtpreis() != null) {
									BigDecimal preisOhneAktuellemMaterialzuschlag = besposDto[i].getNNettogesamtpreis()
											.subtract(besposDto[i].getNMaterialzuschlag());

									BigDecimal zuschlag = getMaterialFac().getMaterialzuschlagEKInZielwaehrung(
											besposDto[i].getArtikelIId(), besDto.getLieferantIIdBestelladresse(),
											new java.sql.Date(wareneingangDto.getTLieferscheindatum().getTime()),
											besDto.getWaehrungCNr(), theClientDto);
									if (zuschlag != null) {
										weposDto.setNGelieferterpreis(preisOhneAktuellemMaterialzuschlag.add(zuschlag));
									}

								}

								weposDto.setBestellpositionIId(besposDto[i].getIId());
								weposDto.setWareneingangIId(iIdWareneingangI);
								createWareneingangsposition(weposDto, setAufloesen, false, theClientDto);
								// Daten fuer Fehlmengenaufloesung zusammenbauen
								if (alData == null) {
									alData = new ArrayList<Object[]>();
								}
								Object[] oData = new Object[4];
								oData[0] = artikelDto.getIId();
								oData[1] = wareneingangDto.getLagerIId();
								oData[2] = bdOffeneMenge;
								oData[3] = besposDto[i].getLossollmaterialIId();
								alData.add(oData);
							}
						}
					}
				}
			}

			aktualisiereEinstandspreise(iIdBestellungI, null, theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return new WEPBuchenReturnDto(alData, tmArtikelIIdsMitKommentar);
	}

	public void wareneingangspositionAlsVerraeumtKennzeichnen(Integer wareneingangspositionIId) {
		Wareneingangsposition wareneingangsposition = em.find(Wareneingangsposition.class, wareneingangspositionIId);
		wareneingangsposition.setBVerraeumt(Helper.boolean2Short(true));
	}

	public Integer[] erfasseAllePreiseOhneBenutzerinteraktion(Integer iIdWareneingangI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		WareneingangspositionDto[] wepoDto = wareneingangspositionFindByWareneingangIId(iIdWareneingangI);
		ArrayList<Integer> alArtikelIId = new ArrayList<Integer>();
		Integer bestellungIId = null;
		for (int i = 0; i < wepoDto.length; i++) {
			if (!wepoDto[i].getBPreiseErfasst()) {
				wepoDto[i].setBPreiseErfasst(true);

				BestellpositionDto besPosDto = getBestellpositionFac()
						.bestellpositionFindByPrimaryKey(wepoDto[i].getBestellpositionIId());
				verteileFixkostenaufWareneingangspositionen(besPosDto.getIId(), theClientDto);
				if (besPosDto.getArtikelIId() != null) {
					alArtikelIId.add(besPosDto.getArtikelIId());
				}
				// if (besPosDto.getPositioniIdArtikelset() == null) {
				Wareneingangsposition wareneingangsposition = em.find(Wareneingangsposition.class, wepoDto[i].getIId());
				wareneingangsposition.setBPreiseerfasst(Helper.boolean2Short(true));
				em.merge(wareneingangsposition);
				em.flush();
				// }
				getBestellpositionFac().versucheBestellpositionAufErledigtZuSetzen(wepoDto[i].getBestellpositionIId(),
						theClientDto);
			}
		}

		Set s = new HashSet();
		s.add(iIdWareneingangI);

		aktualisiereEinstandspreise(bestellungIId, s, theClientDto);

		Integer[] toReturn = new Integer[alArtikelIId.size()];
		toReturn = alArtikelIId.toArray(toReturn);
		return toReturn;
	}

	public BigDecimal getWareneingangWertsumme(WareneingangDto wareneingangDto, TheClientDto theClientDto) {
		BigDecimal bdReturn = new BigDecimal(0);
		if (wareneingangDto != null && wareneingangDto.getIId() != null) {
			try {
				Integer iWareneingangIId = wareneingangDto.getIId();
				WareneingangspositionDto[] wePos = wareneingangspositionFindByWareneingangIId(iWareneingangIId);
				if (wePos.length > 0) {

					EinstandspreiseEinesWareneingangsDto preiseDtos = getBerechnetenEinstandspreisEinerWareneingangsposition(
							iWareneingangIId, theClientDto);

					for (int i = 0; i < wePos.length; i++) {

						if (preiseDtos.getHmEinstandpreiseAllerPositionen().containsKey(wePos[i].getIId())) {
							BigDecimal bdEinstandspreisUngerundet = preiseDtos.getHmEinstandpreiseAllerPositionen()
									.get(wePos[i].getIId());

							if (bdEinstandspreisUngerundet != null) {
								if (wePos[i].getNGeliefertemenge() != null) {
									BigDecimal bdPosSumme = wePos[i].getNGeliefertemenge()
											.multiply(bdEinstandspreisUngerundet);
									bdReturn = bdReturn.add(bdPosSumme);
								}
							}
						}

					}
				}

			} catch (Throwable e) {
				// Nothing we just return null
			}
		}
		try {
			if (bdReturn != null) {
				bdReturn = bdReturn.setScale(getMandantFac().getNachkommastellenPreisWE(theClientDto.getMandant()),
						BigDecimal.ROUND_HALF_EVEN);
			}

		} catch (Throwable e) {
			// Nothing we just return null
		}

		return bdReturn;
	}

	public boolean allePreiseFuerWareneingangErfasst(Integer iWareneingangIId) {
		boolean bAlleErfasst = false;
		WareneingangspositionDto[] wePosDto = wareneingangspositionFindByWareneingangIId(iWareneingangIId);
		if (wePosDto.length > 0) {
			bAlleErfasst = true;
			for (int i = 0; i < wePosDto.length; i++) {
				if (!wePosDto[i].getBPreiseErfasst()) {
					bAlleErfasst = false;
				}
			}
		}
		return bAlleErfasst;
	}

	public List<WareneingangDto> wareneingangFindByLieferscheinnummer(Integer bestellungId, String lieferscheinnummer) {
		List<Wareneingang> wes = WareneingangQuery.listByLieferscheinnummer(em, bestellungId, lieferscheinnummer);
		List<WareneingangDto> weDtos = new ArrayList<WareneingangDto>();
		for (Wareneingang wareneingang : wes) {
			weDtos.add(WareneingangDtoAssembler.createDto(wareneingang));
		}
		return weDtos;
	}
}
