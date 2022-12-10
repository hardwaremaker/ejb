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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.ejb.Anfrage;
import com.lp.server.anfrage.ejb.Anfrageerledigungsgrund;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.ejb.BestellungQuery;
import com.lp.server.bestellung.ejb.Wareneingang;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellung;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungDtoAssembler;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungtextDto;
import com.lp.server.bestellung.service.ImportMonatsbestellungDto;
import com.lp.server.bestellung.service.OpenTransXmlReportResult;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.partner.ejb.Lieferant;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.ejbfac.BelegAktivierungFac;
import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.ejbfac.Versionizer;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.service.BelegDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBExceptionLPwoRollback;
import com.lp.util.Helper;

@Stateless
public class BestellungFacBean extends Facade implements BestellungFac, IAktivierbar, IImportHead {
	private static final long serialVersionUID = 8252413945719616123L;

	@PersistenceContext
	private EntityManager em;

	@EJB
	private transient BelegAktivierungFac belegAktivierungFac;

	/**
	 * Ueberpruefung der BestellungDto; darf nicht null sein
	 * 
	 * @param bestellungDtoI BestellungDto
	 * @param theClientDto   String
	 */
	private void checkBestellungDto(BestellungDto bestellungDtoI, TheClientDto theClientDto) {
		if (bestellungDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception("bestellungDto == null"));
		}
	}

	public BigDecimal berechneOffenenWertEinerBestellung(String bestellungCNr, TheClientDto theClientDto) {

		BigDecimal bdOffenerWert = BigDecimal.ZERO;

		BestellungDto bsDto = bestellungFindByCNrMandantCNr(bestellungCNr, theClientDto.getMandant());
		if (bsDto != null) {
			try {
				BestellpositionDto[] dtos = getBestellpositionFac().bestellpositionFindByBestellung(bsDto.getIId(),
						theClientDto);

				for (int i = 0; i < dtos.length; i++) {
					BestellpositionDto bsposDto = dtos[i];
					if (bsposDto.getNMenge() != null) {
						BigDecimal noffeneMenge = bsposDto.getNMenge();
						BigDecimal ngeliferteMenge = new BigDecimal(0);
						if (bsDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
							noffeneMenge = bsposDto.getNOffeneMenge();
						}

						BigDecimal bdPreisinmandantenwaehrung = bsposDto.getNNettogesamtPreisminusRabatte();
						BigDecimal wechselkursmandantwaehrungzuauftragswaehrung = null;
						if (!bsDto.getWaehrungCNr().equals(theClientDto.getSMandantenwaehrung())) {
							wechselkursmandantwaehrungzuauftragswaehrung = new BigDecimal(
									bsDto.getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue());
							bdPreisinmandantenwaehrung = getBetragMalWechselkurs(bdPreisinmandantenwaehrung,
									Helper.getKehrwert(wechselkursmandantwaehrungzuauftragswaehrung));
						}

						WareneingangspositionDto[] wepDtos = getWareneingangFac()
								.wareneingangspositionFindByBestellpositionIId(bsposDto.getIId());
						for (int k = 0; k < wepDtos.length; k++) {
							WareneingangspositionDto wepDto = wepDtos[k];

							noffeneMenge = noffeneMenge.subtract(wepDto.getNGeliefertemenge());
						}

						bdOffenerWert = bdOffenerWert.add(noffeneMenge.multiply(bdPreisinmandantenwaehrung));

					}
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		} else {
			return new BigDecimal(-1);
		}
		return bdOffenerWert;
	}

	public Integer createBestellung(BestellungDto bestellungDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkBestellungDto(bestellungDtoI, theClientDto);
		if (bestellungDtoI.getIId() != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("bestellungDto.getIId() != null"));
		}

		Integer pk = null;
		try {
			if (bestellungDtoI.getDBelegdatum() == null) {
				bestellungDtoI.setDBelegdatum(getDate());
			}

			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(bestellungDtoI.getMandantCNr());
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(bestellungDtoI.getMandantCNr(),
					bestellungDtoI.getDBelegdatum());
			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(iGeschaeftsjahr, PKConst.PK_BESTELLUNG,
					bestellungDtoI.getMandantCNr(), theClientDto);
			pk = bnr.getPrimaryKey();
			String bestellungsnummer = f.format(bnr);

			bestellungDtoI.setIId(pk);

			// not null-Felder setzen
			if (bestellungDtoI.getDLiefertermin() == null) {
				Timestamp t = Helper.cutTimestamp(getTimestamp());
				bestellungDtoI.setDLiefertermin(t);
			}
			if (bestellungDtoI.getBelegartCNr() == null) {
				bestellungDtoI.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
			}
			if (bestellungDtoI.getBPoenale() == null) {
				bestellungDtoI.setBPoenale(Helper.boolean2Short(false));
			}
			if (bestellungDtoI.getStatusCNr() == null) {
				// eine neue Bestellung ist "angelegt"
				bestellungDtoI.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
			}
			// den Lieferantenrabatt uebernehmen, falls er noch nicht gesetzt
			// wurde.
			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(bestellungDtoI.getLieferantIIdBestelladresse(), theClientDto);
			if (bestellungDtoI.getFAllgemeinerRabattsatz() == null && lieferantDto.getNRabatt() != null) {
				bestellungDtoI.setFAllgemeinerRabattsatz(lieferantDto.getNRabatt().doubleValue());
			}

			if (bestellungDtoI.getFAllgemeinerRabattsatz() == null) {
				bestellungDtoI.setFAllgemeinerRabattsatz(0D);
			}

			if (bestellungDtoI.getPersonalIIdAnlegen() == null) {
				bestellungDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			}
			if (bestellungDtoI.getPersonalIIdAendern() == null) {
				bestellungDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			}
			// anlegen
			Bestellung bestellung = new Bestellung(bestellungDtoI.getIId(), bestellungsnummer,
					bestellungDtoI.getMandantCNr(), bestellungDtoI.getBelegartCNr(), bestellungDtoI.getDBelegdatum(),
					bestellungDtoI.getDLiefertermin(), bestellungDtoI.getBTeillieferungMoeglich(),
					bestellungDtoI.getPersonalIIdAnlegen(), bestellungDtoI.getPersonalIIdAnlegen(),
					bestellungDtoI.getLieferartIId(), bestellungDtoI.getZahlungszielIId(),
					bestellungDtoI.getSpediteurIId(), bestellungDtoI.getBPoenale());
			em.persist(bestellung);
			em.flush();
			setBestellungFromBestellungDto(bestellung, bestellungDtoI);

			HvDtoLogger<BestellungDto> hvLogger = new HvDtoLogger<BestellungDto>(em, bestellungDtoI.getIId(),
					theClientDto);
			hvLogger.logInsert(bestellungDtoI);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return pk;
	}

	public void setzeVersandzeitpunktAufJetzt(Integer iBestellungIId, String sDrucktyp) {
		if (iBestellungIId != null) {
			Bestellung bestellung = em.find(Bestellung.class, iBestellungIId);
			bestellung.setTVersandzeitpunkt(new Timestamp(System.currentTimeMillis()));
			bestellung.setCVersandtype(sDrucktyp);
			em.merge(bestellung);
			em.flush();
		}
	}

	public void updateBestellungMahnstufe(Integer bestellungIId, Integer mahnstufeNeu, TheClientDto theClientDto) {
		BestellungDto bestellung = bestellungFindByPrimaryKey(bestellungIId);
		if (bestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		bestellung.setIMahnstufeIId(mahnstufeNeu);
		updateBestellung(bestellung, theClientDto);
	}

	public void updateBestellungRechnungsadresse(Integer bestellungIId, Integer lieferantIIdRechnungsadresseNeu,
			TheClientDto theClientDto) {

		try {
			WareneingangDto[] weDtos = getWareneingangFac().wareneingangFindByBestellungIId(bestellungIId);
			for (int i = 0; i < weDtos.length; i++) {

				WareneingangspositionDto[] weposDtos = getWareneingangFac()
						.wareneingangspositionFindByWareneingangIId(weDtos[i].getIId());
				for (int j = 0; j < weposDtos.length; j++) {
					if (weposDtos[j].getBPreiseErfasst() == true) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_LIEFERADRESSE_NUR_AENDERBAR_WENN_KEINE_PREISE_ERFASST, "");
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		Bestellung oBestellung = em.find(Bestellung.class, bestellungIId);
		oBestellung.setLieferantIIdRechnungsadresse(lieferantIIdRechnungsadresseNeu);
		em.merge(oBestellung);
		em.flush();

	}

	public void updateBestellung(BestellungDto bestellungDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		updateBestellung(bestellungDtoI, theClientDto, true);
	}

	public void updateBestellung(BestellungDto bestellungDtoI, TheClientDto theClientDto, boolean pruefeKorrekterStatus)
			throws EJBExceptionLP {
		checkBestellungDto(bestellungDtoI, theClientDto);

		// merken fuer Liefertermin setzen (siehe ende von updateBestellung)
		BestellungDto bsDtoOld = bestellungFindByPrimaryKey(bestellungDtoI.getIId());
		try {
			// Informationen des aktuellen Benutzers setzen
			bestellungDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			bestellungDtoI.setTAendern(getTimestamp());

			// SP8281
			if (bestellungDtoI.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ANGELEGT)) {
				bestellungDtoI.setNBestellwert(null);
			}

			Bestellung bestellung = em.find(Bestellung.class, bestellungDtoI.getIId());
			if (bestellung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			Integer iGJAlt = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant(),
					bestellung.getTBelegdatum());
			Integer iGJNeu = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant(),
					bestellungDtoI.getDBelegdatum());
			if (!iGJNeu.equals(iGJAlt)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN,
						new Exception("Es wurde versucht, die BS " + bestellungDtoI.getCNr() + " auf "
								+ bestellungDtoI.getDBelegdatum() + " (GJ " + iGJNeu + ") umzudatieren"));
			}

			// PJ19376 Wenn der Rabattbetrag geaendert wird, dann Korrekturbetrag
			// auf NULL setzen
			if (bestellungDtoI.getFAllgemeinerRabattsatz() != null
					&& bestellungDtoI.getFAllgemeinerRabattsatz().doubleValue() != 0) {
				bestellung.setNKorrekturbetrag(null);
			}

			BestellungDto bestellungDto_vorher = bestellungFindByPrimaryKey(bestellungDtoI.getIId());
			HvDtoLogger<BestellungDto> logger = new HvDtoLogger<BestellungDto>(em, bestellungDtoI.getIId(),
					theClientDto);
			logger.log(bestellungDto_vorher, bestellungDtoI);

			setBestellungFromBestellungDto(bestellung, bestellungDtoI);

			BestellungDto bsDtoNew = bestellungFindByPrimaryKey(bestellungDtoI.getIId());
			// updaten des Datums der bestellpositionen
			if (!Helper.cutTimestamp(bsDtoOld.getDLiefertermin())
					.equals(Helper.cutTimestamp(bsDtoNew.getDLiefertermin()))) {
				BestellpositionDto aBSPOSDto[] = getBestellpositionFac()
						.bestellpositionFindByBestellung(bestellungDtoI.getIId(), theClientDto);

				for (int i = 0; i < aBSPOSDto.length; i++) {
					// Bestelltliste aktualsisiert
					getBestellpositionFac().updateBestellpositionBeiLieferterminAenderung(aBSPOSDto[i].getIId(),
							theClientDto);
					getArtikelbestelltFac().aktualisiereBestelltListe(aBSPOSDto[i].getIId(), theClientDto);
				}
			}

			if (!pruefeKorrekterStatus)
				return;
			String sKorrekterStatus = getRichtigenBestellStatus(bestellungDtoI.getIId(), false, theClientDto);
			if (!bsDtoNew.getStatusCNr().equals(sKorrekterStatus)) {
				// Der neue Status stimmt nicht mit dem errechneten ueberein
				String sAlterStatus = bestellungDtoI.getStatusCNr();
				bestellungDtoI.setStatusCNr(sKorrekterStatus);
				bestellung = em.find(Bestellung.class, bestellungDtoI.getIId());
				setBestellungFromBestellungDto(bestellung, bestellungDtoI);
				ArrayList<Object> al = new ArrayList<Object>();
				al.add(sAlterStatus.trim());
				al.add(sKorrekterStatus.trim());
				al.add(bestellungDtoI.getCNr());
				EJBExceptionLPwoRollback exception = new EJBExceptionLPwoRollback(
						EJBExceptionLP.FEHLER_BESTELLUNG_FALSCHER_STATUS, "");
				exception.setAlInfoForTheClient(al);
				throw exception;
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void setAuftragIIdInBestellung(BestellungDto bestellungDto, TheClientDto theClientDto) {
		if (bestellungDto.getIId() != null) {
			BestellungDto bestellung = bestellungFindByPrimaryKey(bestellungDto.getIId());
			bestellung.setAuftragIId(bestellungDto.getAuftragIId());
			updateBestellung(bestellungDto, theClientDto, false);
		}
	}

	public BestellungDto bestellungFindByPrimaryKey(Integer iIdBSI) throws EJBExceptionLP {

		// Preconditions.
		if (iIdBSI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iId == null"));
		}

		BestellungDto bestellungDto = null;
		// try {
		Bestellung bestellung = em.find(Bestellung.class, iIdBSI);
		if (bestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		bestellungDto = assembleBestellungDto(bestellung);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return bestellungDto;
	}

	public BestellungDto bestellungFindByCNrMandantCNr(String cNr, String mandantCNr) {
		Query query = em.createNamedQuery("BestellungfindByCNrMandantCNr");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCNr);
		try {
			Bestellung bestellung = (Bestellung) query.getSingleResult();
			return assembleBestellungDto(bestellung);
		} catch (NoResultException ex) {
			return null;
		}
	}

	/**
	 * Diese Methode holt alle Bestellungen, bei denen die Lieferadresse der
	 * &uuml;bergebeben PartnerIId zugeordnet ist
	 * 
	 * @param partnerIId  Integer
	 * @param cNrMandantI String
	 * @return BestellungDto[]
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public BestellungDto[] bestellungFindByLieferadressePartnerIIdMandantCNr(Integer partnerIId, String cNrMandantI)
			throws EJBExceptionLP, RemoteException {
		BestellungDto[] bestellungDtos = null;
		// try {
		Query query = em.createNamedQuery("BestellungfindByLieferadressepartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		bestellungDtos = assembleBestellungDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return bestellungDtos;
	}

	public BestellungDto[] bestellungFindByLieferadressePartnerIIdMandantCNrOhneExc(Integer partnerIId,
			String cNrMandantI) throws RemoteException {
		BestellungDto[] bestellungDtos = null;
		try {
			Query query = em.createNamedQuery("BestellungfindByLieferadressepartnerIIdMandantCNr");
			query.setParameter(1, partnerIId);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			// if(cl.isEmpty()){
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
			// }
			bestellungDtos = assembleBestellungDtos(cl);

			// catch (ObjectNotFoundException ex) {
			// myLogger.warn("partnerIID: " + partnerIId, ex);
			// }
			// catch (FinderException ex) {

			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		} catch (Throwable t) {
			myLogger.warn("bestellungFindByLieferadressePartnerIIdMandantCNrOhneExc", t);
		}
		return bestellungDtos;
	}

	public BestellungDto[] bestellungFindByAnsprechpartnerIIdMandantCNr(Integer iAnsprechpartnerIId, String cNrMandantI)
			throws EJBExceptionLP {
		BestellungDto[] bestellungDtos = null;
		// try {
		Query query = em.createNamedQuery("BestellungfindByAnsprechpartnerIIdMandantCNr");
		query.setParameter(1, iAnsprechpartnerIId);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		bestellungDtos = assembleBestellungDtos(cl);

		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return bestellungDtos;
	}

	public BestellungDto[] bestellungFindByAnsprechpartnerIIdMandantCNrOhneExc(Integer iAnsprechpartnerIId,
			String cNrMandantI) {
		try {
			Query query = em.createNamedQuery("BestellungfindByAnsprechpartnerIIdMandantCNr");
			query.setParameter(1, iAnsprechpartnerIId);
			query.setParameter(2, cNrMandantI);
			return assembleBestellungDtos(query.getResultList());
		} catch (Throwable th) {
			return null;
		}
	}

	public BestellungDto[] bestellungFindByLieferantIIdBestelladresseMandantCNr(Integer lieferantIId,
			String cNrMandantI) throws EJBExceptionLP, RemoteException {
		BestellungDto[] bestellungDtos = null;
		// try {
		Query query = em.createNamedQuery("BestellungfindByLieferantIIdBestelladresseMandantCNr");
		query.setParameter(1, lieferantIId);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		bestellungDtos = assembleBestellungDtos(cl);

		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return bestellungDtos;
	}

	public BestellungDto[] bestellungFindByLieferantIIdBestelladresseMandantCNrOhneExc(Integer lieferantIId,
			String cNrMandantI) {
		BestellungDto[] bestellungDtos = null;
		try {
			Query query = em.createNamedQuery("BestellungfindByLieferantIIdBestelladresseMandantCNr");
			query.setParameter(1, lieferantIId);
			query.setParameter(2, cNrMandantI);
			bestellungDtos = assembleBestellungDtos(query.getResultList());
		} catch (Throwable th) {
			return null;
		}
		return bestellungDtos;
	}

	public BestellungDto[] bestellungFindByLieferantIIdRechnungsadresseMandantCNr(Integer lieferantIId,
			String cNrMandantI) throws EJBExceptionLP, RemoteException {
		BestellungDto[] bestellungDtos = null;
		// try {
		Query query = em.createNamedQuery("BestellungfindByLieferantIIdRechnungsadresseMandantCNr");
		query.setParameter(1, lieferantIId);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		bestellungDtos = assembleBestellungDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return bestellungDtos;
	}

	public BestellungDto[] bestellungFindByLieferantIIdRechnungsadresseMandantCNrOhneExc(Integer lieferantIId,
			String cNrMandantI) {
		BestellungDto[] bestellungDtos = null;
		try {
			Query query = em.createNamedQuery("BestellungfindByLieferantIIdRechnungsadresseMandantCNr");
			query.setParameter(1, lieferantIId);
			query.setParameter(2, cNrMandantI);
			bestellungDtos = assembleBestellungDtos(query.getResultList());
		} catch (Throwable th) {
			return null;
		}
		return bestellungDtos;
	}

	private void setBestellungFromBestellungDto(Bestellung bestellung, BestellungDto bestellungDto) {
		bestellung.setMandantCNr(bestellungDto.getMandantCNr());
		bestellung.setBestellungartCNr(bestellungDto.getBestellungartCNr());
		bestellung.setBestellungstatusCNr(bestellungDto.getStatusCNr());
		bestellung.setBelegartCNr(bestellungDto.getBelegartCNr());
		bestellung.setTBelegdatum(bestellungDto.getDBelegdatum());
		bestellung.setLieferantIIdBestelladresse(bestellungDto.getLieferantIIdBestelladresse());
		bestellung.setAnsprechpartnerIId(bestellungDto.getAnsprechpartnerIId());
		bestellung.setPersonalIIdAnforderer(bestellungDto.getPersonalIIdAnforderer());
		bestellung.setLieferantIIdRechnungsadresse(bestellungDto.getLieferantIIdRechnungsadresse());
		bestellung.setCBezprojektbezeichnung(bestellungDto.getCBez());
		bestellung.setWaehrungCNrBestellungswaehrung(bestellungDto.getWaehrungCNr());
		bestellung.setFWechselkursmandantwaehrungbestellungswaehrung(
				bestellungDto.getFWechselkursmandantwaehrungzubelegwaehrung());
		bestellung.setTLiefertermin(bestellungDto.getDLiefertermin());
		bestellung.setKostenstelleIId(bestellungDto.getKostenstelleIId());
		bestellung.setBTeillieferungmoeglich(bestellungDto.getBTeillieferungMoeglich());
		bestellung.setILeihtage(bestellungDto.getILeihtage());
		bestellung.setFAllgemeinerrabattsatz(bestellungDto.getFAllgemeinerRabattsatz());
		bestellung.setLieferartIId(bestellungDto.getLieferartIId());
		bestellung.setZahlungszielIId(bestellungDto.getZahlungszielIId());
		bestellung.setSpediteurIId(bestellungDto.getSpediteurIId());
		bestellung.setNBestellwert(bestellungDto.getNBestellwert());
		bestellung.setAnfrageIId(bestellungDto.getAnfrageIId());
		bestellung.setBestellungtextIIdKopftext(bestellungDto.getBestelltextIIdKopftext());
		bestellung.setCKopftextuebersteuert(bestellungDto.getCKopftextUebersteuert());
		bestellung.setBestellungtextIIdFusstext(bestellungDto.getBestelltextIIdFusstext());
		bestellung.setCFusstextuebersteuert(bestellungDto.getCFusstextUebersteuert());
		bestellung.setTGedruckt(bestellungDto.getTGedruckt());
		bestellung.setPersonalIIdStorniert(bestellungDto.getPersonalIIdStorniert());
		bestellung.setTStorniert(bestellungDto.getTStorniert());
		bestellung.setBestellungIIdRahmenbestellung(bestellungDto.getIBestellungIIdRahmenbestellung());
		bestellung.setXInternerkommentar(bestellungDto.getXInternerKommentar());
		bestellung.setXExternerkommentar(bestellungDto.getXExternerKommentar());
		bestellung.setPartnerIIdLieferadresse(bestellungDto.getPartnerIIdLieferadresse());
		bestellung.setTMahnsperrebis(bestellungDto.getTMahnsperreBis());
		bestellung.setTManuellgeliefert(bestellungDto.getTManuellGeliefert());
		bestellung.setAuftragIId(bestellungDto.getAuftragIId());
		bestellung.setMahnstufeIId(bestellungDto.getIMahnstufeIId());
		bestellung.setTAenderungsbestellung(bestellungDto.getTAenderungsbestellung());
		bestellung.setTVersandzeitpunkt(bestellungDto.getTVersandzeitpunkt());
		// Wenn Versandzeitpunkt zurueckgesetzt wird, dann auch den
		// Versandstatus zuruechsetzen
		if (bestellungDto.getTVersandzeitpunkt() == null) {
			bestellung.setCVersandtype(null);
		}

		bestellung.setAnsprechpartnerIIdLieferadresse(bestellungDto.getAnsprechpartnerIIdLieferadresse());
		bestellung.setPartnerIIdAbholadresse(bestellungDto.getPartnerIIdAbholadresse());
		bestellung.setAnsprechpartnerIIdAbholadresse(bestellungDto.getAnsprechpartnerIIdAbholadresse());
		bestellung.setBPoenale(bestellungDto.getBPoenale());
		bestellung.setCLieferantenangebot(bestellungDto.getCLieferantenangebot());
		bestellung.setCLieferartort(bestellungDto.getCLieferartort());
		bestellung.setProjektIId(bestellungDto.getProjektIId());
		bestellung.setNKorrekturbetrag(bestellungDto.getNKorrekturbetrag());

		bestellung.setTKommissionierungDurchgefuehrt(bestellungDto.getTKommissionierungDurchgefuehrt());
		bestellung.setTKommissionierungGeplant(bestellungDto.getTKommissionierungGeplant());
		bestellung.setTUebergabeTechnik(bestellungDto.getTUebergabeTechnik());
		bestellung.setTTVollstaendigGeliefert(bestellungDto.getTVollstaendigGeliefert());
		bestellung.setIVersion(bestellungDto.getIVersion());
		bestellung.setNTransportkosten(bestellungDto.getNTransportkosten());
		bestellung.setPersonalIIdInterneranforderer(bestellungDto.getPersonalIIdInterneranforderer());

		em.merge(bestellung);
		em.flush();
	}

	private BestellungDto assembleBestellungDto(Bestellung bestellung) {
		return BestellungDtoAssembler.createDto(bestellung);
	}

	private BestellungDto[] assembleBestellungDtos(Collection<?> bestellungs) {
		List<BestellungDto> list = new ArrayList<BestellungDto>();
		if (bestellungs != null) {
			Iterator<?> iterator = bestellungs.iterator();
			while (iterator.hasNext()) {
				Bestellung bestellung = (Bestellung) iterator.next();
				list.add(assembleBestellungDto(bestellung));
			}
		}
		BestellungDto[] returnArray = new BestellungDto[list.size()];
		return list.toArray(returnArray);
	}

	/**
	 * @deprecated MB.
	 * 
	 *             Betellungen duerfen nur in bestimmten Stati geaendert werden.
	 *             <br>
	 *             Nachdem eine Bestellung geaendert wurde, befindet sie sich im
	 *             Status ANGELEGT.
	 * 
	 * @param iIdBestellungI PK der Bestellung
	 * @param theClientDto   String
	 * @throws EJBExceptionLP
	 */
	public void pruefeUndSetzeBestellungstatusBeiAenderung(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Bestellung bestellung = null;

		// try {
		bestellung = em.find(Bestellung.class, iIdBestellungI);
		if (bestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		String sStatus = bestellung.getBestellungstatusCNr();
		if (sStatus.equals(BestellungFac.BESTELLSTATUS_OFFEN)) {
			bestellung.setBestellungstatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
			bestellung.setNBestellwert(null);
		}
		bestellung.setTAendern(getTimestamp());
		bestellung.setPersonalIIdAendern(theClientDto.getIDPersonal());
	}

	public void aktiviereBestellung(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.notNull(iIdBestellungI, "iIdBestellungI");
		pruefeAktivierbar(iIdBestellungI, theClientDto);
		// Wert berechnen
		berechneBeleg(iIdBestellungI, theClientDto);
		// und Status aendern
		aktiviereBeleg(iIdBestellungI, theClientDto);
	}

	@Override
	public void pruefeAktivierbar(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		BestellungDto bestellung = bestellungFindByPrimaryKey(iid);
		if (bestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// Ohne Positionen darf der Beleg nicht aktiviert werden.
		if (getBestellpositionFac().getAnzahlBestellpositionen(iid) == 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN, "");
		}

		BestellpositionDto[] aPositionDtos = getBestellpositionFac().bestellpositionFindByBestellung(iid, theClientDto);
		boolean bHatMengenbehaftetePositionen = false;
		boolean bHatMengenbehaftetePositionenMitNegativerMenge = false;
		for (int i = 0; i < aPositionDtos.length; i++) {
			if (aPositionDtos[i].getNMenge() != null) {
				bHatMengenbehaftetePositionen = true;

				if (aPositionDtos[i].getNMenge().doubleValue() < 0) {
					bHatMengenbehaftetePositionenMitNegativerMenge = true;
				}

			}

			if (aPositionDtos[i].getArtikelIId() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(aPositionDtos[i].getArtikelIId(),
						theClientDto);
				if (Helper.short2boolean(aDto.getBKeineLagerzubuchung())) {
					ArrayList al = new ArrayList();
					al.add(aDto.getCNr());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BESTELLUNG_HAT_ARTIKEL_KEINE_LAGERZUBUCHUNG, al,
							new Exception("FEHLER_BESTELLUNG_HAT_ARTIKEL_KEINE_LAGERZUBUCHUNG"));
				}
			}

		}
		if (bHatMengenbehaftetePositionen == false) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BESTELLUNG_HAT_KEINE_MENGENPOSITIONEN,
					new Exception("FEHLER_BESTELLUNG_HAT_KEINE_MENGENPOSITIONEN"));
		}
		if (bHatMengenbehaftetePositionenMitNegativerMenge == true) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BESTELLUNG_HAT_NEGATIVE_MENGENPOSITIONEN,
					new Exception("FEHLER_BESTELLUNG_HAT_NEGATIVE_MENGENPOSITIONEN"));
		}

	}

	@Override
	public BelegPruefungDto aktiviereBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		Bestellung bestellung = em.find(Bestellung.class, iid);
		if (BestellungFac.BESTELLSTATUS_ANGELEGT.equals(bestellung.getBestellungstatusCNr())) {

			bestellung.setTGedruckt(getTimestamp());
			em.merge(bestellung);
			em.flush();

			BestellungDto bestellungDto = bestellungFindByPrimaryKey(iid);

			// SP2415 Es muss der berechnete Bestellstatus kommen, da es
			// eventuell schon Wareneingaenge usw. geben kann
			bestellungDto.setStatusCNr(getRichtigenBestellStatus(iid, false, theClientDto));

			updateBestellung(bestellungDto, theClientDto, false);

			// PJ19485
			// Wenn Zentraler Artikelstamm und der Lieferant ein anderer Mandant
			// ist
			if (bestellungDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR)
					&& getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {

				if (bestellungDto.getLieferantIIdBestelladresse() != null) {
					Integer partnerIId_Lieferant = getLieferantFac()
							.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto)
							.getPartnerIId();
					MandantDto[] dtos = getMandantFac().mandantFindByPartnerIIdOhneExc(partnerIId_Lieferant,
							theClientDto);

					if (dtos != null && dtos.length > 0) {

						for (int i = 0; i < dtos.length; i++) {

							String mandantCNr = dtos[i].getCNr();

							if (!mandantCNr.equals(theClientDto.getMandant())) {
								// SP4569 auch der Ziel-Mandant muss
								// -ZENTRALER_ARTIKELSTAMM- haben
								if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
										MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, mandantCNr)) {

									getAuftragFac().erzeugeAuftragAusBestellungeinesAnderenMandanten(
											bestellungDto.getIId(), mandantCNr, theClientDto);
									break;
								}

							}

						}

					}

				}
			}
		}

		return null;
	}

	public void bestellungAufPauschalbetragSetzten(Integer bestellungIId, BigDecimal bdPauschalbetrag,
			TheClientDto theClientDto) {

		if (bdPauschalbetrag != null) {
			Bestellung bes = em.find(Bestellung.class, bestellungIId);
			bes.setNKorrekturbetrag(null);
			em.merge(bes);
			em.flush();
			if (bes.getFAllgemeinerrabattsatz() != null && bes.getFAllgemeinerrabattsatz() != 0) {
				// Geht nicht
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BESTELLUNG_PAUSCHALKORREKTUR_ALLGEMEINER_RABATT_MUSS_0_SEIN,
						new Exception(
								"FEHLER_BESTELLUNG_PAUSCHALKORREKTUR_ALLGEMEINER_RABATT_MUSS_0_SEIN" + bes.getCNr()));

			}

			try {
				if (getWareneingangFac().wareneingangFindByBestellungIId(bestellungIId).length > 0) {
					// Geht nicht
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BESTELLUNG_PAUSCHALKORREKTUR_NUR_BIS_WARENEINGANG_MOEGLICH,
							new Exception("FEHLER_BESTELLUNG_PAUSCHALKORREKTUR_NUR_BIS_WARENEINGANG_MOEGLICH "
									+ bes.getCNr()));

				}
			} catch (RemoteException e1) {
				throwEJBExceptionLPRespectOld(e1);
			}

			try {

				int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisVK(theClientDto.getMandant());

				BestellpositionDto[] besPosDtos = getBestellpositionFac().bestellpositionFindByBestellung(bestellungIId,
						theClientDto);

				// Nach Nettopreis sortieren
				TreeMap tmPreibehaftetePositionen = new TreeMap();

				BigDecimal bdGesamtsummeOhneMaterialzuschlag = BigDecimal.ZERO;

				BigDecimal bdGesamtsummeMaterialzuschlag = BigDecimal.ZERO;

				for (int i = 0; i < besPosDtos.length; i++) {
					if (besPosDtos[i].getNNettogesamtPreisminusRabatte() != null) {

						BigDecimal bdZeilensummeOhneMaterialzuschlag = null;

						if (besPosDtos[i].getNMaterialzuschlag() != null) {
							bdZeilensummeOhneMaterialzuschlag = besPosDtos[i].getNNettogesamtPreisminusRabatte()
									.subtract(besPosDtos[i].getNMaterialzuschlag()).multiply(besPosDtos[i].getNMenge());

							bdGesamtsummeMaterialzuschlag = bdGesamtsummeMaterialzuschlag
									.add(besPosDtos[i].getNMaterialzuschlag().multiply(besPosDtos[i].getNMenge()));

						} else {
							bdZeilensummeOhneMaterialzuschlag = besPosDtos[i].getNNettogesamtPreisminusRabatte()
									.multiply(besPosDtos[i].getNMenge());
						}

						bdGesamtsummeOhneMaterialzuschlag = bdGesamtsummeOhneMaterialzuschlag
								.add(bdZeilensummeOhneMaterialzuschlag);

						besPosDtos[i].setBdZeilensummeOhneMaterialzuschlag_NOT_IN_DB(bdZeilensummeOhneMaterialzuschlag);

						tmPreibehaftetePositionen.put(
								Helper.fitString2LengthAlignRight(bdZeilensummeOhneMaterialzuschlag + "", 20, '0')
										+ Helper.fitString2LengthAlignRight(besPosDtos[i].getISort() + "", 10, '0'),
								besPosDtos[i]);
					}

				}

				BigDecimal bdPauschalbetragOhneMaterialZuschlag = bdPauschalbetrag
						.subtract(bdGesamtsummeMaterialzuschlag);

				// Faktor Pauschalbetrag
				BigDecimal bdFaktor = bdPauschalbetragOhneMaterialZuschlag.divide(bdGesamtsummeOhneMaterialzuschlag, 10,
						BigDecimal.ROUND_HALF_EVEN);

				BigDecimal bdRestbetragOhneMaterialzuschlag = bdPauschalbetragOhneMaterialZuschlag;

				Iterator it = tmPreibehaftetePositionen.keySet().iterator();
				while (it.hasNext()) {

					String key = (String) it.next();

					BestellpositionDto besPos = (BestellpositionDto) tmPreibehaftetePositionen.get(key);

					// Zeilensumme hat immer 2 Stellen

					BigDecimal bdZeilensummeNeuOhneMaterialzuschlag = null;
					// Den Rest auf die teuerste Position verteilen
					if (it.hasNext()) {
						bdZeilensummeNeuOhneMaterialzuschlag = Helper.rundeKaufmaennisch(
								besPos.getBdZeilensummeOhneMaterialzuschlag_NOT_IN_DB().multiply(bdFaktor), 2);
					} else {

						bdZeilensummeNeuOhneMaterialzuschlag = bdRestbetragOhneMaterialzuschlag;

					}

					BigDecimal bdNettopreisNeu = bdZeilensummeNeuOhneMaterialzuschlag.divide(besPos.getNMenge(),
							iNachkommastellenPreis, BigDecimal.ROUND_HALF_EVEN);

					// Rabatt neu berechnen
					BigDecimal bdRabattsumme = besPos.getNNettoeinzelpreis().subtract(bdNettopreisNeu);

					// Rabattsatz berechnen
					Double satz = (bdRabattsumme.doubleValue() / (besPos.getNNettoeinzelpreis().doubleValue())) * 100;

					// Updaten
					besPos.setNRabattbetrag(bdRabattsumme);
					besPos.setDRabattsatz(satz);

					BigDecimal bdNettopreisNeuMitMaterialzuschlag = bdNettopreisNeu;

					if (besPos.getNMaterialzuschlag() != null) {
						bdNettopreisNeuMitMaterialzuschlag = bdNettopreisNeuMitMaterialzuschlag
								.add(besPos.getNMaterialzuschlag());
					}

					besPos.setNNettogesamtpreis(bdNettopreisNeuMitMaterialzuschlag);
					besPos.setBNettopreisuebersteuert(Helper.boolean2Short(true));
					besPos.setBRabattsatzUebersteuert(Helper.boolean2Short(false));

					bdRestbetragOhneMaterialzuschlag = bdRestbetragOhneMaterialzuschlag
							.subtract(bdZeilensummeNeuOhneMaterialzuschlag);

					getBestellpositionFac().updateBestellposition(besPos, theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);

					getBestellpositionFac().befuelleZusaetzlichePreisfelder(besPos.getIId());

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			BigDecimal bdNettowert = berechneNettowertGesamt(bes.getIId(), theClientDto);

			BigDecimal bdKorrekturbetrag = bdPauschalbetrag.subtract(bdNettowert);

			bes = em.find(Bestellung.class, bestellungIId);
			bes.setNKorrekturbetrag(bdKorrekturbetrag);
			if (bes.getNBestellwert() != null) {

				bes.setNBestellwert(berechneNettowertGesamt(bes.getIId(), theClientDto));

			}
		}

	}

	@Override
	public Timestamp berechneBeleg(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		Bestellung oBestellung = em.find(Bestellung.class, iid);
		if (!BestellungFac.BESTELLSTATUS_ANGELEGT.equals(oBestellung.getBestellungstatusCNr()))
			return getTimestamp();

		oBestellung.setNBestellwert(berechneNettowertGesamt(oBestellung.getIId(), theClientDto)); // Berechnung in
		// Bestellwaehrung
		// nach den Berechnungen!
		ParametermandantDto paramDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_LIEFERANTEN_AUTOMATISCH_SORTIEREN);
//		ParametermandantDto paramDto = getParameterFac().parametermandantFindByPrimaryKey(
//				ParameterFac.PARAMETER_LIEFERANTEN_AUTOMATISCH_SORTIEREN, ParameterFac.KATEGORIE_BESTELLUNG,
//				theClientDto.getMandant());
		Boolean bLieferantSortieren = false;
		if (paramDto != null) {
			if ((Boolean) paramDto.getCWertAsObject()) {
				bLieferantSortieren = true;
			}
		}
		if (bLieferantSortieren) {
			// Jetzt den Lieferanten als ersten der Artikel setzen
			BestellpositionDto[] besPos = getBestellpositionFac().bestellpositionFindByBestellung(oBestellung.getIId(),
					theClientDto);
			for (int i = 0; i < besPos.length; i++) {
				if (besPos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
					ArtikellieferantDto[] artLief = getArtikelFac()
							.artikellieferantFindByArtikelIId(besPos[i].getArtikelIId(), theClientDto);
					if (artLief.length > 0) {
						if (!artLief[0].getLieferantIId().equals(oBestellung.getLieferantIIdBestelladresse())) {
							Integer artLiefBestIId = null;
							for (int y = 1; y < artLief.length; y++) {
								if (artLief[y].getLieferantIId().equals(oBestellung.getLieferantIIdBestelladresse())) {
									artLiefBestIId = artLief[y].getIId();
								}
							}
							if (artLiefBestIId != null) {
								getArtikelFac().vertauscheArtikellieferanten(artLiefBestIId, artLief[0].getIId(),
										theClientDto);
							}
						}
					}
				}
			}
		}
		return getTimestamp();
	}

	public BigDecimal berechneBestellwertGesamt(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BigDecimal bestellwert = new BigDecimal(0);
		// Bestellung holen
		BestellungDto besDto = bestellungFindByPrimaryKey(iIdBestellungI);
		// alle Positionen dieser Bestellung
		BestellpositionDto[] aBesPosDto = null;
		try {
			aBesPosDto = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI, theClientDto);
			for (int xaBesPosDto = 0; xaBesPosDto < aBesPosDto.length; xaBesPosDto++) {
				BestellpositionDto pos = aBesPosDto[xaBesPosDto];
				// alle positiven mengenbehafteten Positionen beruecksichtigen
				if (pos.getNMenge() != null && pos.getNMenge().doubleValue() > 0
						&& pos.getPositioniIdArtikelset() == null) {
					BigDecimal menge = pos.getNMenge();
					// Grundlage ist der NettogesamtwertMinusRabatt der Position
					// in Bestellwaehrung
					BigDecimal wertDerPosition = menge.multiply(pos.getNNettogesamtpreis());
					bestellwert = bestellwert.add(wertDerPosition);
				}

				// PJ19376
				if (pos.getNFixkosten() != null) {
					bestellwert = bestellwert.add(pos.getNFixkosten());

				}

			}
			// jetzt den allgemeinen rabatt wegrechnen
			BigDecimal bdAllgemeinerRabattsatzFaktor;
			if (besDto.getFAllgemeinerRabattsatz() != null) {
				bdAllgemeinerRabattsatzFaktor = new BigDecimal(100)
						.subtract(new BigDecimal(besDto.getFAllgemeinerRabattsatz())).movePointLeft(2);
			} else {
				bdAllgemeinerRabattsatzFaktor = new BigDecimal(1);
			}
			bestellwert = bestellwert.multiply(bdAllgemeinerRabattsatzFaktor);

			// PJ19376
			if (besDto.getNKorrekturbetrag() != null) {
				bestellwert = bestellwert.add(besDto.getNKorrekturbetrag());

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		// PJ21035
		if (besDto.getNTransportkosten() != null) {
			bestellwert = bestellwert.add(besDto.getNTransportkosten());
		}

		return bestellwert;
	}

	public BigDecimal berechneEinstandswertEinerBestellung(Integer iIdBestellungI, boolean bNurHandeingaben,
			TheClientDto theClientDto) {

		BigDecimal bdEinstandswert = Helper.getBigDecimalNull();

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		String sQuery = "SELECT wp FROM FLRWareneingangspositionen wp WHERE wp.flrbestellposition.flrbestellung.i_id="
				+ iIdBestellungI;

		if (bNurHandeingaben) {
			sQuery += " AND wp.flrbestellposition.flrartikel.artikelart_c_nr='" + ArtikelFac.ARTIKELART_HANDARTIKEL
					+ "'";
		}

		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> list = query.list();
		Iterator<?> iter = list.iterator();

		while (iter.hasNext()) {
			FLRWareneingangspositionen wp = (FLRWareneingangspositionen) iter.next();

			BigDecimal einstandspreisZeile = wp.getN_einstandspreis();
			if (wp.getFlrwareneingang().getN_wechselkurs() != null
					&& wp.getFlrwareneingang().getN_wechselkurs().doubleValue() != 0) {
				einstandspreisZeile = einstandspreisZeile.divide(wp.getFlrwareneingang().getN_wechselkurs(), 4,
						BigDecimal.ROUND_HALF_EVEN);

			}

			bdEinstandswert = bdEinstandswert.add(einstandspreisZeile.multiply(wp.getN_geliefertemenge()));
		}

		return bdEinstandswert;
	}

	public BigDecimal berechneEinkaufswertIst(Integer iIdBestellungI, String sArtikelartI, TheClientDto theClientDto) {

		BigDecimal bdEKwertIstO = Helper.getBigDecimalNull();
		BestellpositionDto[] aBestellpositionDtos = null;
		try {
			aBestellpositionDtos = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI,
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		for (int i = 0; i < aBestellpositionDtos.length; i++) {

			// alle mengenbehafteten Positionen beruecksichtigen
			if (aBestellpositionDtos[i].getNMenge() != null && aBestellpositionDtos[i].getArtikelIId() != null) {
				// PJ 15859
				if (aBestellpositionDtos[i].getPositioniIdArtikelset() == null) {

					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(aBestellpositionDtos[i].getArtikelIId(), theClientDto);

					BigDecimal bdBeitragDieserPosition = aBestellpositionDtos[i].getNMenge()
							.multiply(aBestellpositionDtos[i].getNNettogesamtPreisminusRabatte());

					// je nach Artikelart beruecksichtigen
					if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						if (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdEKwertIstO = bdEKwertIstO.add(bdBeitragDieserPosition);
						}
					} else {
						if (!oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdEKwertIstO = bdEKwertIstO.add(bdBeitragDieserPosition);
						}
					}
				}
			}
		}

		return bdEKwertIstO;
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	public void bestellungAusAnderemMandantRueckbestaetigen(Integer auftragIId, TheClientDto theClientDto_Quelle) {
		AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);

		if (aDto.getBestellungIIdAndererMandant() != null) {
			try {
				BestellungDto bsDto = getBestellungFac()
						.bestellungFindByPrimaryKey(aDto.getBestellungIIdAndererMandant());

				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(bsDto.getMandantCNr(),
						theClientDto_Quelle);
				MandantDto mandantDtoQuelle = getMandantFac().mandantFindByPrimaryKey(theClientDto_Quelle.getMandant(),
						theClientDto_Quelle);
				TheClientDto theClientDto_Zielmandant = null;

				try {
					/*
					 * theClientDto_Zielmandant = getLogonFac()
					 * .logon(Helper.getFullUsername(sUser), Helper.getMD5Hash((sUser + sPassword)
					 * .toCharArray()), Helper.string2Locale(mandantDto .getPartnerDto()
					 * .getLocaleCNrKommunikation()), bsDto.getMandantCNr(), new
					 * Timestamp(System.currentTimeMillis()));
					 */
					theClientDto_Zielmandant = getLogonFac().logonIntern(
							Helper.string2Locale(mandantDto.getPartnerDto().getLocaleCNrKommunikation()),
							bsDto.getMandantCNr());

				} catch (EJBExceptionLP e1) {
					// Meldung bringen
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BENUTZER_AUTOMATISCHE_BELEGANLAGE_NICHT_DEFINIERT,
							new Exception("FEHLER_BENUTZER_AUTOMATISCHE_BELEGANLAGE_NICHT_DEFINIERT"));

				}

				BigDecimal bdWechselkurs = getLocaleFac().getWechselkurs2(theClientDto_Quelle.getSMandantenwaehrung(),
						bsDto.getWaehrungCNr(), theClientDto_Zielmandant);

				// Zuerst verknuefung mit der Bestellposition entfernen
				AuftragpositionDto[] aufposDtos = getAuftragpositionFac().auftragpositionFindByAuftrag(auftragIId);

				for (int i = 0; i < aufposDtos.length; i++) {
					Auftragposition ap = em.find(Auftragposition.class, aufposDtos[i].getIId());
					ap.setBestellpositionIId(null);
					em.merge(ap);
					em.flush();
				}
				// Zuerst alle loeschen
				BestellpositionDto[] bsPosDtos = getBestellpositionFac().bestellpositionFindByBestellung(bsDto.getIId(),
						theClientDto_Zielmandant);
				for (int i = 0; i < bsPosDtos.length; i++) {
					getBestellpositionFac().removeBestellposition(bsPosDtos[i], theClientDto_Zielmandant);
				}

				for (int i = 0; i < aufposDtos.length; i++) {

					AuftragpositionDto aufposDto = aufposDtos[i];

					if (aufposDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
							|| aufposDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)
							|| aufposDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_BETRIFFT)
							|| aufposDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_SEITENUMBRUCH)
							|| aufposDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_LEERZEILE)
							|| aufposDto.getPositionsartCNr()
									.equals(AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE)) {

						BestellpositionDto bestellpositionDto = new BestellpositionDto();

						bestellpositionDto = (BestellpositionDto) getBelegpositionkonvertierungFac()
								.cloneBelegpositionDtoFromBelegpositionDto(bestellpositionDto, aufposDto,
										theClientDto_Zielmandant);

						bestellpositionDto.setBelegIId(bsDto.getIId());

						bestellpositionDto.setTUebersteuerterLiefertermin(aufposDto.getTUebersteuerbarerLiefertermin());
						bestellpositionDto.setTAuftragsbestaetigungstermin(
								new java.sql.Date(aufposDto.getTUebersteuerbarerLiefertermin().getTime()));
						bestellpositionDto.setCABNummer(aDto.getCNr());
						bestellpositionDto.setBNettopreisuebersteuert(aufposDto.getBNettopreisuebersteuert());

						// Preise

						if (aufposDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
								|| aufposDto.getPositionsartCNr()
										.equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {

							bestellpositionDto.setNNettoeinzelpreis(getLocaleFac().rechneUmInMandantenWaehrung(
									aufposDto.getNEinzelpreisplusversteckteraufschlag(), bdWechselkurs));

							BigDecimal bdRabattbetrag = aufposDto.getNRabattbetrag();

							bestellpositionDto.setDRabattsatz(aufposDto.getFRabattsatz());

							if (aufposDto.getFZusatzrabattsatz() != null
									&& aufposDto.getFZusatzrabattsatz().doubleValue() != 0) {
								BigDecimal bdZusatzrabattbetrag = Helper.getProzentWert(
										aufposDto.getNEinzelpreisplusversteckteraufschlag().subtract(bdRabattbetrag),
										new BigDecimal(aufposDto.getFZusatzrabattsatz()), 4);

								bdRabattbetrag = bdRabattbetrag.add(bdZusatzrabattbetrag);

								bestellpositionDto.setDRabattsatz(Helper.berechneRabattsatzMehrererRabatte(
										aufposDto.getFRabattsatz(), aufposDto.getFZusatzrabattsatz(), null));

							}

							bdRabattbetrag = getLocaleFac().rechneUmInMandantenWaehrung(bdRabattbetrag, bdWechselkurs);
							bestellpositionDto.setNRabattbetrag(bdRabattbetrag);

							BigDecimal nettogesamtpreis = getLocaleFac().rechneUmInMandantenWaehrung(
									aufposDto.getNEinzelpreisplusversteckteraufschlag().subtract(bdRabattbetrag),
									bdWechselkurs);

							if (aufposDto.getNMaterialzuschlag() != null) {

								BigDecimal materialzuschlag = getLocaleFac()
										.rechneUmInMandantenWaehrung(aufposDto.getNMaterialzuschlag(), bdWechselkurs);

								bestellpositionDto.setNMaterialzuschlag(materialzuschlag);

								nettogesamtpreis = nettogesamtpreis.add(materialzuschlag);
							}

							bestellpositionDto.setNNettogesamtpreis(nettogesamtpreis);

							bestellpositionDto
									.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT);

						}

						Integer bsPosIId = getBestellpositionFac().createBestellposition(bestellpositionDto,
								theClientDto_Zielmandant,
								BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);

						// Nun Verknuepfung wiederherstellen
						Auftragposition ap = em.find(Auftragposition.class, aufposDtos[i].getIId());
						ap.setBestellpositionIId(bsPosIId);
						em.merge(ap);
						em.flush();

					}

				}

				bsDto.setStatusCNr(BestellungFac.BESTELLSTATUS_BESTAETIGT);
				getBestellungFac().updateBestellung(bsDto, theClientDto_Zielmandant);

				getLogonFac().logout(theClientDto_Zielmandant);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

	}

	public BigDecimal berechneNettowertGesamt(Integer iIdBestellungI, TheClientDto theClientDto) {
		BigDecimal bestellwert = new BigDecimal(0);
		// die aktuelle Bestellung
		BestellungDto besDto = bestellungFindByPrimaryKey(iIdBestellungI);
		String sBesStatus = besDto.getStatusCNr();
		if (sBesStatus.equals(BestellungFac.BESTELLSTATUS_ANGELEGT)) {
			// Step 1: Wenn der Gesamtauftragswert NULL und der Status ANGELEGT
			// ist,
			// dann den Wert aus den Positionen berechnen
			bestellwert = berechneBestellwertGesamt(iIdBestellungI, theClientDto);
		}

		else if (sBesStatus.equals(BestellungFac.BESTELLSTATUS_STORNIERT)) {
			// Step 2: Wenn der status STORNIERT ist, 0 zurueckgeben obwohl der
			// Bestellwert noch in der Tabelle steht
			bestellwert = new BigDecimal(0);
		}

		else if (sBesStatus.equals(BestellungFac.BESTELLSTATUS_OFFEN)
				|| sBesStatus.equals(BestellungFac.BESTELLSTATUS_BESTAETIGT)
				|| sBesStatus.equals(BestellungFac.BESTELLSTATUS_GELIEFERT)
				|| sBesStatus.equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
			// Step 3: Wenn der status OFFEN, BESTAETIGT oder GELIEFERT ist,
			// den Bestellwert aus der Tabelle holen
			if (besDto.getNBestellwert() != null) {
				bestellwert = berechneBestellwertGesamt(iIdBestellungI, theClientDto);
			}
		}

		// Step 4: Der Wert muss in unsere Datenbank passen, fuer decimal(9)
		// gilt 15,4
		bestellwert = Helper.rundeKaufmaennisch(bestellwert, 4);

		checkNumberFormat(bestellwert);
		return bestellwert;
	}

	/**
	 * @deprecated MB.
	 * 
	 *             Wenn der allgemeine Rabatt in den Konditionen geaendert wurde,
	 *             dann werden im Anschluss die davon abhaengigen Werte neu
	 *             berechnet.
	 * @param iIdBestellungI PK der Bestellung
	 * @param theClientDto   der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateBestellungKonditionen(Integer iIdBestellungI, TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			BestellpositionDto[] aPosition = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI,
					theClientDto);

			// fuer jede Position den allg Rabatt neu beruecksichtigen
			for (int i = 0; i < aPosition.length; i++) {
				getBestellpositionFac().befuelleZusaetzlichePreisfelder(aPosition[i].getIId());
			}

			Bestellung oBestellung = em.find(Bestellung.class, iIdBestellungI);
			if (oBestellung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (!oBestellung.getBestellungstatusCNr().equals(BestellungFac.BESTELLSTATUS_ANGELEGT)) {
				oBestellung.setNBestellwert(berechneNettowertGesamt(iIdBestellungI, theClientDto));
			}
			oBestellung.setTAendern(getTimestamp());
			oBestellung.setPersonalIIdAendern(theClientDto.getIDPersonal());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public Integer getWiederbeschaffungsmoralEinesArtikels(Integer artikelIId, TheClientDto theClientDto) {
		return getWiederbeschaffungsmoralEinesArtikels(artikelIId, null, theClientDto);
	}
	
	public Integer getWiederbeschaffungsmoralEinesArtikels(Integer artikelIId,Integer lieferantIId,  TheClientDto theClientDto) {
		Session session = null;

		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();

		java.sql.Date dDatum = null;

		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_WIEDERBSCHAFFUNGSMORAL_MONATE);
			Integer iAnzalMonate = (Integer) parameter.getCWertAsObject();

			Calendar cal = Calendar.getInstance();

			cal.add(Calendar.MONTH, -iAnzalMonate.intValue());
			dDatum = new java.sql.Date(cal.getTimeInMillis());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		String sQuery = "SELECT wp FROM FLRWareneingangspositionen wp WHERE wp.flrbestellposition.flrartikel.i_id="
				+ artikelIId + " AND wp.flrbestellposition.flrbestellung.bestellungstatus_c_nr NOT IN('"
				+ BestellungFac.BESTELLSTATUS_STORNIERT + "') AND wp.flrbestellposition.flrbestellung.t_belegdatum>='"
				+ Helper.formatDateWithSlashes(dDatum) + "'";
		
		if(lieferantIId!=null) {
			sQuery+=" AND wp.flrbestellposition.flrbestellung.lieferant_i_id_bestelladresse="+lieferantIId;
		}
		
		org.hibernate.Query query = session.createQuery(sQuery);

		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_WIEDERBSCHAFFUNGSMORAL_ANZAHL_BESTELLUNGEN);
			Integer iAnzalRechnungen = (Integer) parameter.getCWertAsObject();
			query.setMaxResults(iAnzalRechnungen);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		List<?> list = query.list();

		if (list.size() == 0) {
			return null;
		}
		Iterator<?> iter = list.iterator();
		int iZaehler = 0;
		int iGesamtTage = 0;

		while (iter.hasNext()) {
			FLRWareneingangspositionen wp = (FLRWareneingangspositionen) iter.next();

			wp.getFlrwareneingang().getT_wareneingansdatum();

			iGesamtTage += Helper.ermittleTageEinesZeitraumes(
					new java.sql.Date(wp.getFlrbestellposition().getFlrbestellung().getT_belegdatum().getTime()),
					new java.sql.Date(wp.getFlrwareneingang().getT_wareneingansdatum().getTime()));

			iZaehler++;

		}

		if (iZaehler != 0) {
			return iGesamtTage / iZaehler;
		} else {
			return 0;
		}

	}

	/**
	 * Eine Bestellung kann manuell freigegeben werden. <br>
	 * Bestellung muss sich im Status 'Angelegt' befinden.
	 * 
	 * @param iIdBestellungI PK der Bestellung
	 * @param theClientDto   der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void manuellFreigeben(Integer iIdBestellungI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
		}

		Bestellung oBestellung = em.find(Bestellung.class, iIdBestellungI);
		if (oBestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (oBestellung.getBestellungstatusCNr().equals(BestellungFac.BESTELLSTATUS_ANGELEGT)) {
			oBestellung.setBestellungstatusCNr(BestellungFac.BESTELLSTATUS_OFFEN);
			oBestellung.setTAendern(getTimestamp());
			oBestellung.setPersonalIIdAendern(theClientDto.getIDPersonal());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception("Bestellung kann nicht manuell freigegeben werden, Status : "
							+ oBestellung.getBestellungstatusCNr()));
		}
	}

	/**
	 * Eine Bestellung kann manuell erledigt werden. <br>
	 * Bestellung muss sich im Status 'Geliefert' befinden.
	 * 
	 * @param iIdBestellungI PK der Bestellung
	 * @param theClientDto   der aktuelle Benutzer
	 * @throws EJBExceptionLP  Ausnahme
	 * @throws RemoteException
	 */
	public void manuellErledigen(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
		}
		BestellungDto bestellung = bestellungFindByPrimaryKey(iIdBestellungI);
		if (bestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (Helper.isOneOf(bestellung.getStatusCNr(), BestellungFac.BESTELLSTATUS_OFFEN,
				BestellungFac.BESTELLSTATUS_BESTAETIGT, BestellungFac.BESTELLSTATUS_GELIEFERT)) {
			bestellung.setIPersonalIIdManuellGeliefert(theClientDto.getIDPersonal());
			bestellung.setTManuellGeliefert(getTimestamp());
			bestellung.setStatusCNr(BestellungFac.BESTELLSTATUS_ERLEDIGT);

			BestellpositionDto[] positionen = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI,
					theClientDto);
			for (BestellpositionDto pos : positionen) {
				if (Helper.isOneOf(pos.getBestellpositionstatusCNr(), BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN,
						BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT)
						|| bestellung.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_GELIEFERT)) {
					try {
						getBestellpositionFac().manuellAufVollstaendigGeliefertSetzen(pos.getIId(), theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				if (Helper.isOneOf(pos.getBestellpositionstatusCNr(),
						BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT,
						BestellpositionFac.BESTELLPOSITIONSTATUS_TEILGELIEFERT)) {
					try {
						WareneingangspositionDto[] weposDtos = getWareneingangFac()
								.wareneingangspositionFindByBestellpositionIId(pos.getIId());
						for (int i = 0; i < weposDtos.length; i++) {
							WareneingangspositionDto weposDto = weposDtos[i];
							if (weposDto.getBPreiseErfasst().booleanValue() == false) {
								ArrayList<Object> al = new ArrayList<Object>();
								al.add(getWareneingangFac().wareneingangFindByPrimaryKey(weposDto.getWareneingangIId())
										.getCLieferscheinnr());
								al.add(getBestellpositionFac().getPositionNummer(pos.getIId(), theClientDto) + "");
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_BESTELLUNG_WEPOS_PREIS_NOCH_NICHT_ERFASST, al,
										new Exception("FEHLER_BESTELLUNG_WEPOS_PREIS_NOCH_NICHT_ERFASST"));
							}
						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

			}
			updateBestellung(bestellung, theClientDto, false);

			// PJ20800
			zugehoerigenMandantenuebergreifendenAuftragErledigen(iIdBestellungI, theClientDto);

		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(
					"Bestellung kann nicht manuell erledigt werden, Status : " + bestellung.getStatusCNr()));
		}
	}

	private void zugehoerigenMandantenuebergreifendenAuftragErledigen(Integer iIdBestellungI, TheClientDto theClientDto)
			throws RemoteException {
		// PJ20800
		AuftragDto aDto = getAuftragFac().istAuftragBeiAnderemMandantenHinterlegt(iIdBestellungI);
		if (aDto != null && !aDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {

			TheClientDto theClientDto_Zielmandant = null;
			try {
				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(aDto.getMandantCNr(), theClientDto);
				theClientDto_Zielmandant = getLogonFac().logonIntern(
						Helper.string2Locale(mandantDto.getPartnerDto().getLocaleCNrKommunikation()),
						aDto.getMandantCNr());
			} catch (EJBExceptionLP e1) {
				// Meldung bringen
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BENUTZER_AUTOMATISCHE_BELEGANLAGE_NICHT_DEFINIERT,
						new Exception("FEHLER_BENUTZER_AUTOMATISCHE_BELEGANLAGE_NICHT_DEFINIERT"));

			}

			getAuftragFac().manuellErledigen(aDto.getIId(), theClientDto_Zielmandant);

		}
	}

	/**
	 * Storno einer Bestellung aufheben.
	 * 
	 * @param iIdBestellungI PK der Bestellung
	 * @param theClientDto   der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void stornoAufheben(Integer iIdBestellungI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
		}

		BestellungDto bestellung = bestellungFindByPrimaryKey(iIdBestellungI);
		if (bestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (bestellung.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_STORNIERT)) {
			bestellung.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
			bestellung.setTStorniert(null);
			bestellung.setPersonalIIdStorniert(null);
			updateBestellung(bestellung, theClientDto, false);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(
					"Storno der Bestellung kann nicht aufgehoben werden, Status : " + bestellung.getStatusCNr()));
		}

		// SP6408
		try {
			BestellpositionDto[] positionen = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI,
					theClientDto);
			for (BestellpositionDto pos : positionen) {

				if (bestellung.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {

					if (pos.getIBestellpositionIIdRahmenposition() != null) {
						BigDecimal nAbrufmenge = pos.getNMenge();
						Bestellposition rahmenposition = em.find(Bestellposition.class,
								pos.getIBestellpositionIIdRahmenposition());
						if (rahmenposition != null && nAbrufmenge != null && rahmenposition.getNOffenemenge() != null) {
							rahmenposition.setNOffenemenge(rahmenposition.getNOffenemenge().subtract(nAbrufmenge));
						}
					}
				}

				getArtikelbestelltFac().aktualisiereBestelltListe(pos.getIId(), theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	/**
	 * Eine Bestellung stornieren.
	 * 
	 * @param iIdBestellungI PK der Bestellung
	 * @param theClientDto   der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void stornieren(Integer iIdBestellungI, TheClientDto theClientDto) throws EJBExceptionLP {

		Validator.notNull(iIdBestellungI, "iIdBestellungI");

		try {
			BestellungDto bestellung = bestellungFindByPrimaryKey(iIdBestellungI);

			// wg. PJ19485 Auch offene Bestellungen muessen storniert werden
			// koennen
			if (bestellung.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ANGELEGT)
					|| bestellung.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_OFFEN)) {
				bestellung.setStatusCNr(BestellungFac.BESTELLSTATUS_STORNIERT);
				bestellung.setTStorniert(getTimestamp());
				bestellung.setPersonalIIdStorniert(theClientDto.getIDPersonal());

				updateBestellung(bestellung, theClientDto, false);
				// die Bestellwerte und Positionen bleiben beim Stornieren
				// erhalten
				BestellpositionDto[] oBestellpositionen = getBestellpositionFac()
						.bestellpositionFindByBestellung(iIdBestellungI, theClientDto);
				for (int i = 0; i < oBestellpositionen.length; i++) {
					// PJ 14905 Rahmenmengen korrigieren

					if (bestellung.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {

						if (oBestellpositionen[i].getIBestellpositionIIdRahmenposition() != null) {
							BigDecimal nAbrufmenge = oBestellpositionen[i].getNMenge();
							Bestellposition rahmenposition = em.find(Bestellposition.class,
									oBestellpositionen[i].getIBestellpositionIIdRahmenposition());
							if (rahmenposition != null && nAbrufmenge != null
									&& rahmenposition.getNOffenemenge() != null) {
								rahmenposition.setNOffenemenge(rahmenposition.getNOffenemenge().add(nAbrufmenge));
							}
						}
					}

					getArtikelbestelltFac().aktualisiereBestelltListe(oBestellpositionen[i].getIId(), theClientDto);
				}
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
						new Exception("Bestellung kann nicht storniert werden, Status : " + bestellung.getStatusCNr()));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private void importiereBestellung(ArrayList<ImportMonatsbestellungDto> importBestellung,
			TheClientDto theClientDto) {

		// PJ17902
		// Belegdatum und Lieferant kommen aus der 1.Zeile und gelten fuer den
		// Rest des Import-Files
		// Es wird immer eine neue Bestellung pro Datei angelegt

		BestellungDto bsdto = createBestellungDto(importBestellung.get(0).getLieferantIId(), theClientDto.getMandant(),
				theClientDto.getIDPersonal());

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(importBestellung.get(0).getWeDatum().getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		bsdto.setDBelegdatum(new java.sql.Date(cal.getTime().getTime()));
		bsdto.setDLiefertermin(new Timestamp(cal.getTimeInMillis()));

		bsdto.setCBez("Bestellungsimport "
				+ Helper.formatDatumZeit(new java.sql.Date(System.currentTimeMillis()), theClientDto.getLocUi()) + " "
				+ getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto)
						.getCKurzzeichen());

		Integer bestellungIId = null;
		try {
			bestellungIId = getBestellungFac().createBestellung(bsdto, theClientDto);
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}

		for (int i = 0; i < importBestellung.size(); i++) {
			// Position anlegen

			// SP1768
			if (importBestellung.get(i).getMenge() == null || importBestellung.get(i).getMenge().doubleValue() == 0) {
				continue;
			}

			try {
				BestellpositionDto bspos = createBestellPositionDto(bestellungIId,
						importBestellung.get(i).getLieferantIId(), importBestellung.get(i).getArtikelIId(),
						importBestellung.get(i).getMenge(), theClientDto);
				getBestellpositionFac().createBestellposition(bspos, theClientDto, null, null);
			} catch (EJBExceptionLP e) {
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}

		}

	}

	public void importiereMonatsbestellung(ArrayList<ImportMonatsbestellungDto> importMonatbestellung,
			TheClientDto theClientDto) {

		// letzte Bestellung des Lieferanten suchen und nachsehen, ob diese in
		// diesem Monat bereits vorhanden ist.

		// PJ 17876
		Integer iMonatsbestellungsart = 0;
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_MONATSBESTELLUNGSART);
			iMonatsbestellungsart = (Integer) parameter.getCWertAsObject();
		} catch (RemoteException e2) {
			throwEJBExceptionLPRespectOld(e2);
		}

		if (iMonatsbestellungsart == 2) {
			importiereBestellung(importMonatbestellung, theClientDto);
			return;
		}

		for (int i = 0; i < importMonatbestellung.size(); i++) {

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();
			Criteria c = session.createCriteria(FLRBestellung.class);
			c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
					importMonatbestellung.get(i).getLieferantIId()));

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(importMonatbestellung.get(i).getWeDatum().getTime());

			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);

			c.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM, cal.getTime()));

			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			SimpleDateFormat dateformat = new SimpleDateFormat("MMMM", theClientDto.getLocUi());
			dateformat.format(cal.getTime());

			String projektTextMonatsbestellung = "Monatsbestellung " + dateformat.format(cal.getTime()) + " "
					+ cal.get(Calendar.YEAR);

			c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG, projektTextMonatsbestellung));

			c.add(Restrictions.ge(BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM, cal.getTime()));

			//
			String[] s = new String[2];
			s[0] = BestellungFac.BESTELLSTATUS_ERLEDIGT;
			s[1] = BestellungFac.BESTELLSTATUS_STORNIERT;
			c.add(Restrictions.not(Restrictions.in(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, s)));

			c.addOrder(Order.desc(BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM));
			c.setMaxResults(1);

			List<?> list = c.list();

			Integer bestellungIId = null;

			if (list.size() > 0) {
				FLRBestellung b = (FLRBestellung) list.iterator().next();
				bestellungIId = b.getI_id();
			}

			Lieferant lieferant = em.find(Lieferant.class, importMonatbestellung.get(i).getLieferantIId());
			Partner partner = em.find(Partner.class, lieferant.getPartnerIId());

			if (bestellungIId == null) {

				BestellungDto bsdto = createBestellungDto(importMonatbestellung.get(i).getLieferantIId(),
						theClientDto.getMandant(), theClientDto.getIDPersonal());

				try {
					BestellungtextDto bsText = getBestellungServiceFac().bestellungtextFindByMandantLocaleCNr(
							theClientDto.getMandant(), partner.getLocaleCNrKommunikation(), MediaFac.MEDIAART_KOPFTEXT,
							theClientDto);
					if (bsText != null) {
						bsdto.setBestelltextIIdKopftext(bsText.getIId());
					}
					bsText = getBestellungServiceFac().bestellungtextFindByMandantLocaleCNr(theClientDto.getMandant(),
							partner.getLocaleCNrKommunikation(), MediaFac.MEDIAART_FUSSTEXT, theClientDto);
					if (bsText != null) {
						bsdto.setBestelltextIIdFusstext(bsText.getIId());
					}
				} catch (RemoteException e1) {
					throwEJBExceptionLPRespectOld(e1);
				}

				bsdto.setDBelegdatum(new Date(cal.getTimeInMillis()));//
				bsdto.setDLiefertermin(new Timestamp(cal.getTimeInMillis()));

				bsdto.setCBez(projektTextMonatsbestellung);

				try {
					bestellungIId = getBestellungFac().createBestellung(bsdto, theClientDto);
				} catch (EJBExceptionLP e) {
					throw new EJBExceptionLP(e);
				} catch (RemoteException e) {
					throw new EJBExceptionLP(e);
				}

			}

			// Position anlegen
			try {
				BestellpositionDto bspos = createBestellPositionDto(bestellungIId,
						importMonatbestellung.get(i).getLieferantIId(), importMonatbestellung.get(i).getArtikelIId(),
						importMonatbestellung.get(i).getMenge(), theClientDto);
				Integer bsPosIId = getBestellpositionFac().createBestellposition(bspos, theClientDto, null, null);

				// Wareneingang zu Verladedatum anlegen, wenn noch nicht
				// vorhanden

				if (iMonatsbestellungsart == 0) {
					Timestamp tVerladedatum = new java.sql.Timestamp(
							importMonatbestellung.get(i).getWeDatum().getTime());

					Query query = em.createNamedQuery("WareneingangfindByBestellungIIdTWareneingangsdatum");
					query.setParameter(1, bestellungIId);
					query.setParameter(2, tVerladedatum);
					Collection<?> col = query.getResultList();

					Integer wareneingangId = null;

					if (col.size() > 0) {
						Wareneingang we = (Wareneingang) col.iterator().next();
						wareneingangId = we.getIId();
					} else {
						WareneingangDto wedto = new WareneingangDto();
						wedto.setBestellungIId(bestellungIId);
						wedto.setCLieferscheinnr(importMonatbestellung.get(i).getLieferscheinnr());
						wedto.setTLieferscheindatum(tVerladedatum);
						wedto.setTWareneingangsdatum(tVerladedatum);
						wedto.setLagerIId(lieferant.getLagerIIdZubuchungslager());
						wedto.setNWechselkurs(new BigDecimal(1));
						try {
							wareneingangId = getWareneingangFac().createWareneingang(wedto, theClientDto);
						} catch (RemoteException e) {
							throw new EJBExceptionLP(e);
						}
					}

					try {
						WareneingangspositionDto weposDto = new WareneingangspositionDto();
						weposDto.setBestellpositionIId(bsPosIId);
						weposDto.setNGeliefertemenge(importMonatbestellung.get(i).getMenge());
						weposDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
						weposDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
						weposDto.setTAendern(new Timestamp(System.currentTimeMillis()));
						weposDto.setTAnlegen(weposDto.getTAendern());
						weposDto.setWareneingangIId(wareneingangId);
						BigDecimal nGeliefertpreis = bspos.getNNettogesamtpreis();
						weposDto.setNGelieferterpreis(nGeliefertpreis);
						getWareneingangFac().createWareneingangsposition(weposDto, theClientDto);

						getBestellungFac().aktiviereBestellung(bestellungIId, theClientDto);

						getBestellpositionFac().manuellAufVollstaendigGeliefertSetzen(bsPosIId, theClientDto);

					} catch (EJBExceptionLP e) {
						throw new EJBExceptionLP(e);
					} catch (RemoteException e) {
						throw new EJBExceptionLP(e);
					}
				}

			} catch (EJBExceptionLP e) {
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
			session.close();
		}

	}

	// private Integer getIntegerParameter(String mandantCnr, String kategorie,
	// String parameter) {
	// ParametermandantDto p;
	// try {
	// p = getParameterFac().getMandantparameter(mandantCnr, kategorie,
	// parameter);
	// } catch (Exception e) {
	// return null;
	// }
	// return new Integer(p.getCWert());
	// }

	public BestellungDto createBestellungDto(Integer lieferantIId, String mandantCNr, Integer personalId) {
		BestellungDto bsdto = new BestellungDto();
		Lieferant lieferant = em.find(Lieferant.class, lieferantIId);
		bsdto.setBestellungartCNr(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);
		bsdto.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
		bsdto.setBTeillieferungMoeglich(Helper.boolean2Short(false));

		// TODO: Wechselkurs fuer Bestellung
		bsdto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(1));
		bsdto.setFAllgemeinerRabattsatz(new Double(0));
		bsdto.setLieferantIIdBestelladresse(lieferantIId);
		bsdto.setLieferantIIdRechnungsadresse(lieferantIId);
		bsdto.setLieferartIId(lieferant.getLieferartIId());
		bsdto.setMandantCNr(mandantCNr);
		bsdto.setPersonalIIdAendern(personalId);
		bsdto.setPersonalIIdAnlegen(personalId);
		bsdto.setPersonalIIdAnforderer(personalId);
		bsdto.setSpediteurIId(lieferant.getSpediteurIId());
		bsdto.setTAendern(getTimestamp());
		bsdto.setTAnlegen(getTimestamp());
		bsdto.setZahlungszielIId(lieferant.getZahlungszielIId());
		bsdto.setKostenstelleIId(lieferant.getKostenstelleIId());
		bsdto.setWaehrungCNr(lieferant.getWaehrungCNr());
		bsdto.setLieferartIId(lieferant.getLieferartIId());
		bsdto.setZahlungszielIId(lieferant.getZahlungszielIId());
		bsdto.setSpediteurIId(lieferant.getSpediteurIId());
		bsdto.setLieferartIId(lieferant.getLieferartIId());

		return bsdto;
	}

	public BestellpositionDto createBestellPositionDto(Integer bsId, Integer lieferantId, Integer artikelIId,
			BigDecimal menge, TheClientDto theClientDto) {
		BestellpositionDto bspos = new BestellpositionDto();
		bspos.setArtikelIId(artikelIId);
		Artikel artikel = em.find(Artikel.class, artikelIId);
		bspos.setBestellungIId(bsId);
		bspos.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
		bspos.setNMenge(menge);
		bspos.setEinheitCNr(artikel.getEinheitCNr());
		ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(artikel.getIId(), lieferantId,
				bspos.getNMenge(), theClientDto.getSMandantenwaehrung(), null, theClientDto);
		if (artikellieferantDto == null) {
			bspos.setNNettoeinzelpreis(new BigDecimal(0));
			bspos.setNNettogesamtpreis(new BigDecimal(0));
			bspos.setDRabattsatz(new Double(0));
		} else {
			bspos.setNNettoeinzelpreis(artikellieferantDto.getNEinzelpreis());
			bspos.setNNettogesamtpreis(artikellieferantDto.getNNettopreis());
			bspos.setDRabattsatz(artikellieferantDto.getFRabatt());
		}
		bspos.setNRabattbetrag(
				Helper.getProzentWert(bspos.getNNettogesamtpreis(), new BigDecimal(bspos.getDRabattsatz()), 2));
		bspos.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
		bspos.setBNettopreisuebersteuert(Helper.boolean2Short(false));
		bspos.setBRabattsatzUebersteuert(Helper.boolean2Short(false));
		bspos.setNMaterialzuschlag(new BigDecimal(0));
		return bspos;

	}

	public Integer erzeugeBestellungAusAnfrage(Integer iIdAnfrageI, TheClientDto theClientDto) throws EJBExceptionLP {
		AnfrageFac anfrageFac = getAnfrageFac();
		AnfragepositionFac anfrageposFac = getAnfragepositionFac();
		BestellungFac bestellungFac = getBestellungFac();
		BestellpositionFac bestellposFac = getBestellpositionFac();
		AnfrageDto oAnfrageBasis = null;
		Integer iIdBestellung = null;

		try {

			try {
				// PJ18151
				Query query = em.createNamedQuery("AnfrageerledigungsgrundfindByMandantCnrCBez");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, AnfrageServiceFac.ANFRAGEERLEDIGUNGSGRUND_BESTELLT);
				Anfrageerledigungsgrund anfrageerledigungsgrund = (Anfrageerledigungsgrund) query.getSingleResult();

				// Anfrageerledigungsgrund auf Bestellt setzen
				Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
				anfrage.setAnfrageerledigungsgrundIId(anfrageerledigungsgrund.getIId());

			} catch (NoResultException ex1) {
			}

			oAnfrageBasis = anfrageFac.anfrageFindByPrimaryKey(iIdAnfrageI, theClientDto);

			BestellungDto bestellungDto = new BestellungDto();

			// befuellen des Dto
			bestellungDto.setMandantCNr(theClientDto.getMandant());
			bestellungDto.setPersonalIIdAnforderer(theClientDto.getIDPersonal());
			bestellungDto.setBestellungartCNr(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);
			bestellungDto.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
			bestellungDto.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
			// heutiges Datum

			bestellungDto.setDBelegdatum(Helper.cutDate(getDate()));
			bestellungDto.setLieferantIIdBestelladresse(oAnfrageBasis.getLieferantIIdAnfrageadresse());
			bestellungDto.setAnsprechpartnerIId(oAnfrageBasis.getAnsprechpartnerIIdLieferant());

			if (oAnfrageBasis.getPartnerIIdLieferadresse() == null) {
				// SP6562
				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);
				bestellungDto.setPartnerIIdLieferadresse(mandantDto.getPartnerIIdLieferadresse());
			} else {
				// SP4387
				bestellungDto.setPartnerIIdLieferadresse(oAnfrageBasis.getPartnerIIdLieferadresse());
				bestellungDto.setAnsprechpartnerIIdLieferadresse(oAnfrageBasis.getAnsprechpartnerIIdLieferadresse());
			}
			bestellungDto.setDLiefertermin(oAnfrageBasis.getTAnliefertermin());

			// SP3532
			bestellungDto.setCLieferantenangebot(oAnfrageBasis.getCAngebotnummer());

			// SP1759
			LieferantDto lfDto = getLieferantFac()
					.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto);
			if (lfDto.getPartnerIIdRechnungsadresse() != null) {
				LieferantDto lfDtoRA = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(
						lfDto.getPartnerIIdRechnungsadresse(), theClientDto.getMandant(), theClientDto);
				if (lfDtoRA != null) {
					bestellungDto.setLieferantIIdRechnungsadresse(lfDtoRA.getIId());
				}
			}

			if (bestellungDto.getLieferantIIdRechnungsadresse() == null) {
				bestellungDto.setLieferantIIdRechnungsadresse(lfDto.getIId());
			}

			bestellungDto.setCBez(oAnfrageBasis.getCBez());
			bestellungDto.setProjektIId(oAnfrageBasis.getProjektIId());
			bestellungDto.setWaehrungCNr(oAnfrageBasis.getWaehrungCNr());
			bestellungDto.setFWechselkursmandantwaehrungzubelegwaehrung(
					oAnfrageBasis.getFWechselkursmandantwaehrungzubelegwaehrung());
			bestellungDto.setKostenstelleIId(oAnfrageBasis.getKostenstelleIId());
			bestellungDto.setBTeillieferungMoeglich(new Short((short) 0));

			bestellungDto.setFAllgemeinerRabattsatz(oAnfrageBasis.getFAllgemeinerRabattsatz().doubleValue());
			bestellungDto.setLieferartIId(oAnfrageBasis.getLieferartIId());
			bestellungDto.setZahlungszielIId(oAnfrageBasis.getZahlungszielIId());
			bestellungDto.setSpediteurIId(oAnfrageBasis.getSpediteurIId());
			bestellungDto.setAnfrageIId(oAnfrageBasis.getIId());
			bestellungDto.setNTransportkosten(oAnfrageBasis.getNTransportkosteninanfragewaehrung());

			// diese neue Bestellung abspeichern
			iIdBestellung = bestellungFac.createBestellung(bestellungDto, theClientDto);

			bestellungDto.setIId(iIdBestellung);

			// positionen anlegen
			AnfragepositionDto[] anfragepos = anfrageposFac.anfragepositionFindByAnfrage(iIdAnfrageI, theClientDto);

			for (int i = 0; i < anfragepos.length; i++) {
				AnfragepositionDto anfragepos2 = anfragepos[i];

				BestellpositionDto besposDto = new BestellpositionDto();
				besposDto.setBestellungIId(iIdBestellung);
				besposDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

				Integer bestellpositionIIdNeu = null;
				if (anfragepos2.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)
						|| anfragepos2.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
					// Lieferdaten holen, wenn Lieferdaten vorhanden
					AnfragepositionlieferdatenDto anfposlieferdatenDto = anfrageposFac
							.anfragepositionlieferdatenFindByAnfragepositionIIdOhneExc(anfragepos2.getIId());

					besposDto.setPositionsartCNr(anfragepos2.getPositionsartCNr());

					besposDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
					besposDto.setArtikelIId(anfragepos2.getArtikelIId());

					besposDto.setLossollmaterialIId(anfragepos2.getLossollmaterialIId());

					if (anfragepos2.getArtikelIId() != null
							&& Helper.short2boolean(anfragepos2.getBArtikelbezeichnunguebersteuert()) == false) {

						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(anfragepos2.getArtikelIId(),
								theClientDto);
						besposDto.setCBez(artikelDto.getArtikelsprDto().getCBez());
					} else {
						besposDto.setCBez(anfragepos2.getCBez());
						besposDto.setCZusatzbez(anfragepos2.getCZusatzbez());
					}
					besposDto.setBArtikelbezeichnunguebersteuert(anfragepos2.getBArtikelbezeichnunguebersteuert());
					besposDto.setXTextinhalt(anfragepos2.getXTextinhalt());
					besposDto.setMediastandardIId(anfragepos2.getMediastandardIId());
					besposDto.setEinheitCNr(anfragepos2.getEinheitCNr());
					besposDto.setDRabattsatz(new Double(0));
					besposDto.setNRabattbetrag(new BigDecimal(0));
					besposDto.setNMaterialzuschlag(new BigDecimal(0));

					if (anfposlieferdatenDto != null
							&& Helper.short2boolean(anfposlieferdatenDto.getBErfasst()) == true) {
						besposDto.setNMenge(anfposlieferdatenDto.getNAnliefermenge());
						besposDto.setNNettoeinzelpreis(anfposlieferdatenDto.getNNettogesamtpreisminusrabatt());
						besposDto.setNNettogesamtpreis(anfposlieferdatenDto.getNNettogesamtpreisminusrabatt());
					} else {
						besposDto.setNMenge(anfragepos2.getNMenge());
						besposDto.setNNettoeinzelpreis(anfragepos2.getNRichtpreis());
						besposDto.setNNettogesamtpreis(anfragepos2.getNRichtpreis());
					}

					bestellpositionIIdNeu = bestellposFac.createBestellposition(besposDto, theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_EINZELPREIS_RUECKPFLEGEN, null);

					// den allgemeinen Rabatt fuer alle Positionen
					// beruecksichtigen und das
					// zusaetzliche Preisfeld befuellen
					updateBestellungKonditionen(iIdBestellung, theClientDto);

				} else if (anfragepos2.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_BETRIFFT)) {
					besposDto.setCBez(anfragepos2.getCBez());
					besposDto.setPositionsartCNr(anfragepos2.getPositionsartCNr());
					besposDto.setXTextinhalt(anfragepos2.getXTextinhalt());
					besposDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
					besposDto.setMediastandardIId(anfragepos2.getMediastandardIId());
					bestellpositionIIdNeu = bestellposFac.createBestellposition(besposDto, theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);

				} else if (anfragepos2.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_LEERZEILE)) {
					besposDto.setPositionsartCNr(anfragepos2.getPositionsartCNr());
					besposDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
					besposDto.setMediastandardIId(anfragepos2.getMediastandardIId());
					bestellpositionIIdNeu = bestellposFac.createBestellposition(besposDto, theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);

				} else if (anfragepos2.getPositionsartCNr()
						.equals(BestellpositionFac.BESTELLPOSITIONART_SEITENUMBRUCH)) {
					besposDto.setPositionsartCNr(anfragepos2.getPositionsartCNr());
					besposDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
					besposDto.setMediastandardIId(anfragepos2.getMediastandardIId());
					bestellpositionIIdNeu = bestellposFac.createBestellposition(besposDto, theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);

				} else if (anfragepos2.getPositionsartCNr()
						.equals(BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN)) {
					besposDto.setPositionsartCNr(anfragepos2.getPositionsartCNr());
					besposDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
					besposDto.setMediastandardIId(anfragepos2.getMediastandardIId());
					bestellpositionIIdNeu = bestellposFac.createBestellposition(besposDto, theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);

				} else if (anfragepos2.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE)) {
					besposDto.setPositionsartCNr(anfragepos2.getPositionsartCNr());
					besposDto.setXTextinhalt(anfragepos2.getXTextinhalt());
					besposDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
					besposDto.setMediastandardIId(anfragepos2.getMediastandardIId());
					bestellpositionIIdNeu = bestellposFac.createBestellposition(besposDto, theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);

				}

				if (bestellpositionIIdNeu != null) {
					getBelegartmediaFac().kopiereBelegartmedia(QueryParameters.UC_ID_ANFRAGEPOSITION,
							anfragepos[i].getIId(), QueryParameters.UC_ID_BESTELLPOSITION, bestellpositionIIdNeu,
							theClientDto);
				}

			}

			// setzen des Anfragestatus von offen auf erledigt
			getAnfrageFac().setzeAnfragestatus(AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT, iIdAnfrageI, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdBestellung;
	}

	/**
	 * Eine Anfrage kann in 0..n Bestellungen aufscheinen. Hole alle Bestellungen zu
	 * einer Anfrage.
	 * 
	 * @param iIdAnfrageI  PK der Anfrage
	 * @param theClientDto der aktuelle Benutzer
	 * @return BestellungDto[] die Bestellungen
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BestellungDto[] bestellungFindByAnfrage(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdAnfrageI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAnfrageI == null"));
		}

		BestellungDto[] aBestellungDtos = null;

		// try {
		Query query = em.createNamedQuery("BestellungfindByAnfrage");
		query.setParameter(1, iIdAnfrageI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }

		aBestellungDtos = assembleBestellungDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return aBestellungDtos;
	}

	/**
	 * Den Status einer Bestellung von 'geliefert' auf 'offen' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'geliefert' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param iIdBestellungI Integer
	 * @param theClientDto   String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void erledigenAufheben(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		myLogger.logData(iIdBestellungI);
		BestellungDto bestellung = bestellungFindByPrimaryKey(iIdBestellungI);
		if (bestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		BestellpositionDto[] positionen = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI,
				theClientDto);

		bestellung.setIPersonalIIdManuellGeliefert(null);
		bestellung.setTManuellGeliefert(null);

		updateBestellung(bestellung, theClientDto, false);
		// SP9579
		// SP9712
		String errechneterStatus = getRichtigenBestellStatus(iIdBestellungI, false, theClientDto);
		if (errechneterStatus.equals(BESTELLSTATUS_ERLEDIGT)) {
			bestellung.setStatusCNr(BESTELLSTATUS_GELIEFERT);
		} else {
			bestellung.setStatusCNr(errechneterStatus);
		}

		updateBestellung(bestellung, theClientDto, false);

	}

	/**
	 * liefert zu einer RahmenbestellungId alle Abrufbestellungen zur&uuml;ck
	 * 
	 * @param iIdRahmenBestellungI Integer
	 * @param theClientDto         String
	 * @return BestellungDto[]
	 */
	public BestellungDto[] abrufBestellungenfindByRahmenbestellung(Integer iIdRahmenBestellungI,
			TheClientDto theClientDto) {
		if (iIdRahmenBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdRahmenBestellungI == null"));
		}

		BestellungDto[] aBestellungDtos = null;

		// try {
		Query query = em.createNamedQuery("BestellungfindByRahmenbestellung");
		query.setParameter(1, iIdRahmenBestellungI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }

		aBestellungDtos = assembleBestellungDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return aBestellungDtos;
	}

	/**
	 * 
	 * @param iBestellungId Integer
	 * @return BestellungDto
	 */
	public BestellungDto bestellungFindByPrimaryKeyOhneExc(Integer iBestellungId) {
		BestellungDto bestellungDto = null;
		// try {
		Bestellung bestellung = em.find(Bestellung.class, iBestellungId);
		if (bestellung == null) {
			myLogger.warn("iBestellungId=" + iBestellungId);
		}
		bestellungDto = assembleBestellungDto(bestellung);

		return bestellungDto;
	}

	public BestellungDto bestellungFindByPrimaryKeyWithNull(Integer iBestellungId) {
		Bestellung bestellung = em.find(Bestellung.class, iBestellungId);
		return bestellung == null ? null : assembleBestellungDto(bestellung);
	}

	// /**
	// *
	// * @param bestellungDtoI BestellungDto
	// * @param theClientDto String
	// * @throws EJBExceptionLP
	// */
	// public void updateRahmenbestellung(BestellungDto bestellungDtoI,
	// TheClientDto theClientDto)
	// throws EJBExceptionLP {
	//
	// //merken fuer liefertermin setzen (siehe ende von updateBestellung)
	// BestellungDto besDto = new BestellungDto();
	// besDto = bestellungFindByPrimaryKey(bestellungDtoI.getIId());
	// checkBestellungDto(bestellungDtoI, theClientDto);
	//
	// try {
	// // Informationen des aktuellen Benutzers setzen
	// if (theClientDto != null) {
	// bestellungDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
	//
	// Timestamp t = new Timestamp(System.currentTimeMillis());
	// bestellungDtoI.setTAendern(t);
	// }
	//
	// Bestellung bestellung = em.find(Bestellung.class,
	// bestellungDtoI.getIId());
	// if (bestellung == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
	// null);
	// }
	// //zwischenspeichern der vorigen Bestelldaten
	// String sStatus = bestellung.getBestellungstatusCNr();
	//
	// setBestellungFromBestellungDto(bestellung, bestellungDtoI);
	//
	// //updaten des Datums der bestellpositionen
	// if (besDto.getDLiefertermin() !=
	// bestellungFindByPrimaryKey(bestellungDtoI.getIId()).
	// getDLiefertermin()) {
	// BestellpositionDto besposDto[] = getBestellpositionFac().
	// bestellpositionFindByBestellung(bestellungDtoI.getIId(), theClientDto);
	//
	// for (int i = 0; i < besposDto.length; i++) {
	// // Bestelltliste aktualsisiert
	// getBestellpositionFac().updateBestellpositionBeiLieferterminAenderung(
	// besposDto[
	// i].getIId(), theClientDto);
	// getArtikelbestelltFac().aktualisiereBestelltListe(bestellungDtoI.getIId(),
	// theClientDto);
	// }
	// }
	// }
	// catch (RemoteException ex) {
	// throwEJBExceptionLPRespectOld(ex);
	// }
	// // catch (FinderException ex) {
	// // throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
	// ex);
	// // }
	//
	// }

	// /**
	// * @deprecated MB.
	// *
	// * updated eine Bestellung ohne weitere Aktionen zb. Statusaenderung
	// * erfolgt hier nicht
	// * @param bestellungDto BestellungDto
	// * @param theClientDto String
	// * @throws EJBExceptionLP
	// */
	// public void updateBestellungOhneWeitereAktionen(BestellungDto
	// bestellungDto,
	// TheClientDto theClientDto)
	// throws EJBExceptionLP {
	// // try {
	// Bestellung bestellung = em.find(Bestellung.class,
	// bestellungDto.getIId());
	// if (bestellung == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
	// null);
	// }
	//
	// setBestellungFromBestellungDto(bestellung, bestellungDto);
	//
	// myLogger.exit("Die Bestellung wurde aktualisiert, Status: " +
	// bestellung.getBestellungstatusCNr());
	// // }
	// // catch (FinderException ex) {
	// // throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
	// ex);
	// // }
	//
	// }

	/**
	 * findet alle bestellungen eines mandanten und speziellen status
	 * 
	 * @param theClientDto String
	 * @param bsStatus     String
	 * @return BestellungDto[]
	 * @throws EJBExceptionLP
	 */
	public BestellungDto[] findBestellungByMandantCNrAndBestellstatus(TheClientDto theClientDto, String bsStatus)
			throws EJBExceptionLP {

		BestellungDto bestellungDto[] = null;

		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("theClientDto == null"));
		}
		if (bsStatus == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("bsStatus == null"));
		}

		// try {
		Query query = em.createNamedQuery("BestellungfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, bsStatus);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		bestellungDto = assembleBestellungDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return bestellungDto;
	}

	public void setzeBSMahnsperre(Integer bestellungIId, java.sql.Date tmahnsperre, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BestellungDto bestellung = bestellungFindByPrimaryKey(bestellungIId);
		if (bestellung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		bestellung.setTMahnsperreBis(tmahnsperre);
		updateBestellung(bestellung, theClientDto);
	}

	public Boolean isBSErledigt(Integer iIdBestellungI, TheClientDto theClientDto) {

		// muss geliefert sein
		boolean bErledigt = isBSGeliefert(iIdBestellungI, theClientDto).booleanValue();

		if (bErledigt) {
			BestellpositionDto[] aBSPOS = null;
			try {
				aBSPOS = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI, theClientDto);
				for (int xBSPOS = 0; xBSPOS < aBSPOS.length; xBSPOS++) {
					if (aBSPOS[xBSPOS].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
							|| aBSPOS[xBSPOS].getPositionsartCNr()
									.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
						// --Kandidat fuer Erledigtstatusbestimmung

						WareneingangspositionDto[] wepos = getWareneingangFac()
								.wareneingangspositionFindByBestellpositionIId(aBSPOS[xBSPOS].getIId());

						int xWEPOS = 0;
						int nWEPOS = wepos.length;
						bErledigt = true;
						while (xWEPOS < nWEPOS && bErledigt) {
							if (wepos[xWEPOS].getNGelieferterpreis() == null) {
								bErledigt = false;
								break;
							}
							xWEPOS++;
						}
					}
					if (!bErledigt) {
						break;
					}
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}
		return new Boolean(bErledigt);
	}

	public Boolean isBSGeliefert(Integer iIdBestellungI, TheClientDto theClientDto) {

		boolean bIsGeliefert = true;
		try {
			BestellpositionDto[] aBSPOS = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI,
					theClientDto);

			BigDecimal bdNull = new BigDecimal(0);
			for (int xBSPOS = 0; xBSPOS < aBSPOS.length; xBSPOS++) {
				if (aBSPOS[xBSPOS].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
						|| aBSPOS[xBSPOS].getPositionsartCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

					if (getBestellpositionFac().berechneOffeneMenge(aBSPOS[xBSPOS]).compareTo(bdNull) != 0) {
						if (!aBSPOS[xBSPOS].getBestellpositionstatusCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {
							// --BSPOS hat offene Menge
							bIsGeliefert = false;
						}
					} else {
						aBSPOS[xBSPOS].setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT);
						getBestellpositionFac().updateBestellpositionOhneWeitereAktion(aBSPOS[xBSPOS], theClientDto);
					}
					if ((getBestellpositionFac().berechneOffeneMenge(aBSPOS[xBSPOS]).compareTo(bdNull) < 0)) {
						bIsGeliefert = true;
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return new Boolean(bIsGeliefert);
	}

	public Integer erzeugeBestellungAusBestellung(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception("iIdBestellungI == null"));
		}

		BestellungDto bestellungBasisDto = null;
		boolean bFreieBestellungStattAbrufAnlegen = false;
		try {
			bestellungBasisDto = getBestellungFac().bestellungFindByPrimaryKey(iIdBestellungI);

			if (bestellungBasisDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)
					&& bestellungBasisDto.getIBestellungIIdRahmenbestellung() != null) {
				if (getBestellungFac()
						.bestellungFindByPrimaryKey(bestellungBasisDto.getIBestellungIIdRahmenbestellung())
						.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
					bFreieBestellungStattAbrufAnlegen = true;
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		Integer iIdBestellungKopie = null;

		try {
			BestellungDto bestellungDto = (BestellungDto) bestellungBasisDto.clone();

			if (bFreieBestellungStattAbrufAnlegen) {
				bestellungDto.setBestellungartCNr(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);
				bestellungDto.setIBestellungIIdRahmenbestellung(null);
			}

			bestellungDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(getLocaleFac().getWechselkurs2(
					getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto).getWaehrungCNr(),
					bestellungDto.getWaehrungCNr(), theClientDto).doubleValue()));
			// SP 1833
			bestellungDto.setTAenderungsbestellung(null);
			// +14 tage
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_OFFSET_LIEFERZEIT_IN_TAGEN);
			Integer iOffsetLFZeit = new Integer(parameter.getCWert());

			long lLFTermin = System.currentTimeMillis() + (iOffsetLFZeit.intValue() * 24 * 60 * 60 * 1000);
			bestellungDto.setDBelegdatum(new java.sql.Date(System.currentTimeMillis()));
			bestellungDto.setDLiefertermin(new java.sql.Timestamp(lLFTermin));

			bestellungDto.setPersonalIIdAnforderer(theClientDto.getIDPersonal());

			iIdBestellungKopie = createBestellung(bestellungDto, theClientDto);

			// alle Positionen kopieren
			BestellpositionDto[] aBSpositionBasis = getBestellpositionFac()
					.bestellpositionFindByBestellung(iIdBestellungI, theClientDto);

			for (int i = 0; i < aBSpositionBasis.length; i++) {
				BestellpositionDto bstellpositionDto = (BestellpositionDto) aBSpositionBasis[i].clone();
				bstellpositionDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
				bstellpositionDto.setNMenge(aBSpositionBasis[i].getNMenge());
				bstellpositionDto.setTUebersteuerterLiefertermin(null);

				// SP1733
				if (bFreieBestellungStattAbrufAnlegen) {
					bstellpositionDto.setIBestellpositionIIdRahmenposition(null);
				} else {
					bstellpositionDto.setIBestellpositionIIdRahmenposition(
							aBSpositionBasis[i].getIBestellpositionIIdRahmenposition());

				}

				bstellpositionDto.setTAuftragsbestaetigungstermin(null);
				bstellpositionDto.setCABNummer(null);
				bstellpositionDto.setCABKommentar(null);
				bstellpositionDto.setTAbursprungstermin(null);

				bstellpositionDto.setBestellungIId(iIdBestellungKopie);
				if (bestellungDto.getBestellungartCNr().equals(BESTELLUNGART_RAHMENBESTELLUNG_C_NR))
					bstellpositionDto.setNOffeneMenge(bstellpositionDto.getNMenge());

				Integer bsposIIdNeu = null;

				if (bstellpositionDto.getIBestellpositionIIdRahmenposition() != null) {
					bsposIIdNeu = getBestellpositionFac().createAbrufbestellposition(bstellpositionDto,

							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null, theClientDto);
				} else {
					bsposIIdNeu = getBestellpositionFac().createBestellposition(bstellpositionDto, theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);
				}

				getBelegartmediaFac().kopiereBelegartmedia(QueryParameters.UC_ID_BESTELLPOSITION,
						aBSpositionBasis[i].getIId(), QueryParameters.UC_ID_BESTELLPOSITION, bsposIIdNeu, theClientDto);

				if (aBSpositionBasis[i].getPositionsartCNr()
						.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)
						&& aBSpositionBasis[i].getArtikelIId() != null) {

					BestellpositionDto oBestellpositionDtoNeu = getBestellpositionFac()
							.bestellpositionFindByPrimaryKey(bsposIIdNeu);

					ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(
							aBSpositionBasis[i].getArtikelIId(), bestellungDto.getLieferantIIdBestelladresse(),
							BigDecimal.ONE, bestellungDto.getWaehrungCNr(), bestellungDto.getDBelegdatum(),
							theClientDto);

					// SP4229
					if (alDto != null) {

						// Wurde schon im Zuge der BS-Pos ein Artikellisteferant
						// angelegt?
						ArtikellieferantDto alDtoNeu = getArtikelFac().getArtikelEinkaufspreis(
								oBestellpositionDtoNeu.getArtikelIId(), bestellungDto.getLieferantIIdBestelladresse(),
								BigDecimal.ONE, bestellungDto.getWaehrungCNr(), bestellungDto.getDBelegdatum(),
								theClientDto);

						if (alDtoNeu == null) {
							alDtoNeu = getArtikelFac()
									.artikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabOhneExc(
											oBestellpositionDtoNeu.getArtikelIId(),
											bestellungDto.getLieferantIIdBestelladresse(),
											Helper.cutTimestamp(
													new Timestamp(bestellungDto.getDBelegdatum().getTime())),
											theClientDto);
						}

						if (alDtoNeu == null) {
							alDto.setArtikelIId(oBestellpositionDtoNeu.getArtikelIId());
							alDto.setIId(null);
							alDto.setTPreisgueltigab(new Timestamp(bestellungDto.getDBelegdatum().getTime()));
							getArtikelFac().createArtikellieferant(alDto, theClientDto);
						} else {
							alDtoNeu.setCArtikelnrlieferant(alDto.getCArtikelnrlieferant());
							alDtoNeu.setCBezbeilieferant(alDto.getCBezbeilieferant());
							alDtoNeu.setCAngebotnummer(alDto.getCAngebotnummer());

							getArtikelFac().updateArtikellieferant(alDtoNeu, theClientDto);

						}

					}
				}

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdBestellungKopie;
	}

	public void versucheBestellungAufErledigtZuSetzen(Integer bestellungIIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		int iOffenePositionen = 0;
		int iGeliefertePositionen = 0;
		int iBestaetigtePositionen = 0;
		try {
			BestellungDto bestellung = bestellungFindByPrimaryKey(bestellungIIdI);
			if (bestellung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			if (Helper.isOneOf(bestellung.getStatusCNr(), BestellungFac.BESTELLSTATUS_OFFEN,
					BestellungFac.BESTELLSTATUS_GELIEFERT, BestellungFac.BESTELLSTATUS_BESTAETIGT,
					BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
				// Stati der Positionen bestimmen
				BestellpositionDto[] besPos = getBestellpositionFac().bestellpositionFindByBestellung(bestellungIIdI,
						theClientDto);
				for (int i = 0; i < besPos.length; i++) {
					if (besPos[i].getNMenge() != null) {
						if (besPos[i].getBestellpositionstatusCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {
							// ok
						} else if (besPos[i].getBestellpositionstatusCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT)) {
							iGeliefertePositionen++;
						} else if (besPos[i].getBestellpositionstatusCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT)) {
							iBestaetigtePositionen++;
						} else {
							iOffenePositionen++;
						}
					}
				}
				// Daraus kann der Status bestimmt werden
				if (iBestaetigtePositionen == 0 && iOffenePositionen == 0 && iGeliefertePositionen == 0) {

					// PJ20800
					if (!bestellung.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
						zugehoerigenMandantenuebergreifendenAuftragErledigen(bestellung.getIId(), theClientDto);
					}

					bestellung.setStatusCNr(BestellungFac.BESTELLSTATUS_ERLEDIGT);
				} else if (iOffenePositionen == 0 && iBestaetigtePositionen == 0) {

					if (!bestellung.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_GELIEFERT)) {
						bestellung.setTTVollstaendigGeliefert(getTimestamp());
					}

					bestellung.setStatusCNr(BestellungFac.BESTELLSTATUS_GELIEFERT);

				} else if (iBestaetigtePositionen != 0) {
					bestellung.setStatusCNr(BestellungFac.BESTELLSTATUS_BESTAETIGT);
					bestellung.setTTVollstaendigGeliefert(null);
				} else {
					bestellung.setStatusCNr(BestellungFac.BESTELLSTATUS_OFFEN);
					bestellung.setTTVollstaendigGeliefert(null);
				}
				updateBestellung(bestellung, theClientDto, false);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public ArrayList<Integer> getAngelegtenBestellungen(Integer lieferantIId, TheClientDto theClientDto) {
		Session session = null;

		ArrayList<Integer> a = new ArrayList<Integer>();
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRBestellung.class);
			// Filter auf Mandant
			c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));
			if (lieferantIId != null) {
				c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE, lieferantIId));
			}

			// Filter nach Status: nur angelegte
			c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR,
					BestellungFac.BESTELLSTATUS_ANGELEGT));
			// nach RE-Nummer sortieren
			c.addOrder(Order.asc("c_nr"));
			// Query ausfuehren
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRBestellung item = (FLRBestellung) iter.next();
				a.add(item.getI_id());
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return a;
	}

	public Integer erzeugeEingangsrechnungAusBestellung(Integer iIdBestellungI,
			EingangsrechnungDto eingangsrechnungDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BestellungDto bestellungBasisDto = null;
		Calendar cal = Calendar.getInstance();
		try {
			bestellungBasisDto = getBestellungFac().bestellungFindByPrimaryKey(iIdBestellungI);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		Integer iIdEingangsrechnung = null;
		// Holen der Daten aus der Bestellung
		EingangsrechnungDto eingangsrechnungDto = bestellungBasisDto.cloneAsEingangsrechnungDto();
		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(eingangsrechnungDto.getLieferantIId(),
				theClientDto);
		MwstsatzDto mwst = getMandantFac().mwstsatzFindByPrimaryKey(lieferantDto.getMwstsatzbezIId(), theClientDto);
		eingangsrechnungDto.setNUstBetrag(
				Helper.getMehrwertsteuerBetrag(eingangsrechnungDto.getNBetrag(), mwst.getFMwstsatz().doubleValue()));
		eingangsrechnungDto
				.setNUstBetragfw(eingangsrechnungDto.getNUstBetrag().multiply(eingangsrechnungDto.getNKurs()));
		eingangsrechnungDto.setEingangsrechnungartCNr(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
		eingangsrechnungDto.setDBelegdatum(Helper.cutDate(new Date(cal.getTimeInMillis())));
		eingangsrechnungDto.setDFreigabedatum(Helper.cutDate(new Date(cal.getTimeInMillis())));
		// Vom Benutzer uebersteuerte Werte eintragen
		if (eingangsrechnungDtoI != null) {
			if (eingangsrechnungDtoI.getEingangsrechnungartCNr() != null) {
				eingangsrechnungDto.setEingangsrechnungartCNr(eingangsrechnungDtoI.getEingangsrechnungartCNr());
			}
			if (eingangsrechnungDtoI.getDBelegdatum() != null) {
				eingangsrechnungDto.setDBelegdatum(eingangsrechnungDtoI.getDBelegdatum());
			}
			if (eingangsrechnungDtoI.getDFreigabedatum() != null) {
				eingangsrechnungDto.setDFreigabedatum(eingangsrechnungDtoI.getDFreigabedatum());
			}
			if (eingangsrechnungDtoI.getNUstBetrag() != null) {
				eingangsrechnungDto.setNUstBetrag(eingangsrechnungDtoI.getNUstBetrag());
			}
			if (eingangsrechnungDtoI.getNUstBetragfw() != null) {
				eingangsrechnungDto.setNUstBetragfw(eingangsrechnungDtoI.getNUstBetragfw());
			}
			if (eingangsrechnungDtoI.getNBetrag() != null) {
				eingangsrechnungDto.setNBetrag(eingangsrechnungDtoI.getNBetrag());
			}
			if (eingangsrechnungDtoI.getNBetragfw() != null) {
				eingangsrechnungDto.setNBetragfw(eingangsrechnungDtoI.getNBetragfw());
			}
		}
		iIdEingangsrechnung = getEingangsrechnungFac().createEingangsrechnung(eingangsrechnungDto, theClientDto)
				.getIId();

		return iIdEingangsrechnung;
	}

	public BestellungDto[] bestellungenFindAll() {
		Query query = em.createNamedQuery("BestellungfindAll");
		Collection<?> cl = query.getResultList();
		BestellungDto[] besDto = assembleBestellungDtos(cl);
		return besDto;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String checkBestellStati(TheClientDto theClientDto) {
		BestellungDto[] besDto = getBestellungFac().bestellungenFindAll();
		StringBuffer sErrors = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < besDto.length; i++) {
			if (besDto[i].getCNr().equals("2013/000147")) {

				String sRichtigerStatus = null;
				try {
					sRichtigerStatus = getRichtigenBestellStatus(besDto[i].getIId(), true, theClientDto);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!besDto[i].getStatusCNr().equals(sRichtigerStatus)) {
					// merken und Ausgeben
					counter++;
					String actError = getTimestamp().toString() + " " + counter + " Bestellung " + besDto[i].getCNr()
							+ " in Status " + besDto[i].getStatusCNr().trim() + " statt " + sRichtigerStatus.trim()
							+ "\n";
					sErrors.append(actError);
					System.out.println(actError);
					besDto[i].setStatusCNr(sRichtigerStatus);
					try {
						getBestellungFac().updateBestellung(besDto[i], theClientDto);
					} catch (Exception e) {
						String sError = "\n" + "Fehler beim Update der vorherigen Bestellung " + e.getLocalizedMessage()
								+ "\n";
						sErrors.append(sError);
					}
				}
			}

		}
		return sErrors.toString();
	}

	public String getRichtigenBestellStatus(Integer bestellungIId, boolean bRekursiv, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// Long lStart = System.currentTimeMillis();
		// Zuerst schaun ob manuell erledigt
		BestellungDto bestellungDto = bestellungFindByPrimaryKey(bestellungIId);
		if (bestellungDto.getTManuellGeliefert() != null) {
			return BESTELLSTATUS_ERLEDIGT;
		}
		// Pruefen ob storniert
		if (bestellungDto.getTStorniert() != null) {
			return BESTELLSTATUS_STORNIERT;
		}
		// Pruefen ob Angelegt
		if (bestellungDto.getTGedruckt() == null) {
			return BESTELLSTATUS_ANGELEGT;
		}
		// Fuer die naechsten Pruefungen brauchen wir die Positionen der
		// Bestellung
		BestellpositionDto[] besPosDto;
		try {
			besPosDto = getBestellpositionFac().bestellpositionFindByBestellung(bestellungDto.getIId(), theClientDto);
		} catch (Exception e) {
			besPosDto = null;
		}
		boolean bIsBestaetigt = true;
		boolean bIsGeliefert = true;
		boolean bIsAbgerufen = true;
		for (int i = 0; i < besPosDto.length; i++) {
			if (besPosDto[i].getTAuftragsbestaetigungstermin() == null) {
				// Nur Ident und Handeingaben werden beruecksichtigt
				if (besPosDto[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
						|| besPosDto[i].getPositionsartCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
					bIsBestaetigt = false;
				}
			}
			BigDecimal bdOffeneMenge = besPosDto[i].getNOffeneMenge();
			if (bdOffeneMenge == null) {
				// Wenn offene Menge NULL dann noch komplett offen
				bdOffeneMenge = besPosDto[i].getNMenge();
			}
			if (bdOffeneMenge != null) {
				if (new BigDecimal(0).compareTo(bdOffeneMenge) < 0
						&& besPosDto[i].getTManuellvollstaendiggeliefert() == null) {
					bIsGeliefert = false;
				}
			}
		}
		if (bestellungDto.getBestellungartCNr().equals(BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
			BestellungDto[] abrufDto = abrufBestellungenfindByRahmenbestellung(bestellungDto.getIId(), theClientDto);
			if (abrufDto.length == 0) {
				bIsAbgerufen = false;
			} else {
				for (int i = 0; i < abrufDto.length; i++) {
					String sStatus = "";
					if (bRekursiv) {
						// Rekursion wird maximal 1x aufgerufen, da eine
						// Abrufbestellung keine Rahmenbestellung sein kann
						// Dadurch bRekursiv bei diesem Aufruf egal
						sStatus = getRichtigenBestellStatus(abrufDto[i].getIId(), bRekursiv, theClientDto);
					} else {
						sStatus = abrufDto[i].getStatusCNr();
					}
					if (!sStatus.equals(BESTELLSTATUS_GELIEFERT) && !sStatus.equals(BESTELLSTATUS_ERLEDIGT)
							&& !sStatus.equals(BESTELLSTATUS_STORNIERT)) {
						bIsAbgerufen = false;
					}
				}
				// Wenn alle Abrufe Erledigt oder Geliefert sind ueberpruefen ob
				// die nicht erledigten Positionen des Rahmens komplett
				// abgerufen wurden
				if (bIsAbgerufen) {
					for (int i = 0; i < besPosDto.length; i++) {
						// Wenn Bestellposition nicht erledigt alle
						// Abrufpositionen holen
						BigDecimal bdOffeneMenge = besPosDto[i].getNOffeneMenge();
						if (bdOffeneMenge == null) {
							// Wenn offene Menge NULL dann noch komplett offen
							bdOffeneMenge = besPosDto[i].getNMenge();
						}
						if (bdOffeneMenge != null) {
							if (new BigDecimal(0).compareTo(bdOffeneMenge) < 0
									&& besPosDto[i].getTManuellvollstaendiggeliefert() == null) {
								BestellpositionDto[] abrufePosDto = getBestellpositionFac()
										.bestellpositionFindByBestellpositionIIdRahmenposition(besPosDto[i].getIId(),
												theClientDto);
								BigDecimal bdPosMenge = besPosDto[i].getNMenge();
								if (bdPosMenge != null) {
									for (int y = 0; y < abrufePosDto.length; y++) {
										if (abrufePosDto[y].getNMenge() != null) {
											if (!BESTELLSTATUS_STORNIERT.equals(getRichtigenBestellStatus(
													abrufePosDto[y].getBestellungIId(), bRekursiv, theClientDto))) {
												bdPosMenge = bdPosMenge.subtract(abrufePosDto[y].getNMenge());
											}
										}
									}
									// Wenn die Menge aller Abrufe kleiner als
									// Rahmenmenge ist dann ist nicht abgerufen
									if (new BigDecimal(0).compareTo(bdPosMenge) < 0) {
										bIsAbgerufen = false;
									}
								}
							}
						}
					}
				}
			}
		} else {
			// Eine nicht Rahmenbestellung kann nicht abgerufen sein
			bIsAbgerufen = false;
		}
		// Wenn nicht geliefert abgerufen oder bestaetigt dann Offen
		if (!bIsBestaetigt && !bIsGeliefert && !bIsAbgerufen) {
			return BESTELLSTATUS_OFFEN;
		}
		if (!bIsGeliefert) {
			// Wenn die Bestellung jetzt noch nicht geliefert ist kann sie nur
			// noch im Status bestaetigt oder abgerufen sein sein
			if (bIsAbgerufen) {
				// Wenn sie abgerufen ist kann sie nicht mehr bestaetigt sein
				return BESTELLSTATUS_ABGERUFEN;
			} else {
				// Die Bestellung kann hier nur noch bestaetigt sein
				return BESTELLSTATUS_BESTAETIGT;
			}
		} else {
			// Jetzt pruefen ob erledigt, wenn nicht ist sie geliefert
			// erledigt kann eine Bestellung hier nur sein wenn alle Preise
			// erfasst sind
			boolean bPreiseErfasst = true;
			WareneingangDto[] weDto = getWareneingangFac().wareneingangFindByBestellungIId(bestellungDto.getIId());
			// Es muss Wareneingaenge geben da es sonst vorher schon zu einem
			// return gekommen waere
			for (int i = 0; i < weDto.length; i++) {
				WareneingangspositionDto[] wePosDto = getWareneingangFac()
						.wareneingangspositionFindByWareneingangIId(weDto[i].getIId());
				if (wePosDto != null) {
					for (int y = 0; y < wePosDto.length; y++) {
						if (!wePosDto[y].getBPreiseErfasst()) {
							bPreiseErfasst = false;
						}
					}
				}
			}
			if (bPreiseErfasst) {
				// Long lEnd = System.currentTimeMillis();
				// System.out.println(lEnd - lStart);
				return BESTELLSTATUS_ERLEDIGT;
			} else {
				return BESTELLSTATUS_GELIEFERT;
			}
		}
	}

	// public String getRichtigenBestellStatus(Integer bestellungIId,boolean
	// bRekursiv, TheClientDto theClientDto) throws EJBExceptionLP,
	// RemoteException{
	// // Long lStart = System.currentTimeMillis();
	// //Zuerst schaun ob manuell erledigt
	// BestellungDto bestellungDto = bestellungFindByPrimaryKey(bestellungIId);
	// if(bestellungDto.getTManuellGeliefert()!=null){
	// return BESTELLSTATUS_ERLEDIGT;
	// }
	// //Pruefen ob storniert
	// if(bestellungDto.getTStorniert()!=null){
	// return BESTELLSTATUS_STORNIERT;
	// }
	// //Pruefen ob Angelegt
	// if(bestellungDto.getTGedruckt()==null){
	// return BESTELLSTATUS_ANGELEGT;
	// }
	// //Fuer die naechsten Pruefungen brauchen wir die Positionen der
	// Bestellung
	// BestellpositionDto[] besPosDto;
	// try {
	// besPosDto =
	// getBestellpositionFac().bestellpositionFindByBestellung(bestellungDto.
	// getIId(), theClientDto);
	// } catch (Exception e) {
	// besPosDto = null;
	// }
	// boolean bIsBestaetigt = true;
	// boolean bIsGeliefert = true;
	// boolean bIsAbgerufen = true;
	// for(int i=0;i<besPosDto.length;i++){
	// if(besPosDto[i].getTAuftragsbestaetigungstermin()==null){
	// bIsBestaetigt = false;
	// }
	// if(!bestellungDto.getBelegartCNr().equals(
	// BESTELLUNGART_RAHMENBESTELLUNG_C_NR)){
	// //Bei Rahmenbestellungen trifft geliefert wenn erst durch die abrufe zu
	// BigDecimal bdOffeneMenge = besPosDto[i].getNOffeneMenge();
	// if(bdOffeneMenge==null){
	// //Wenn offene Menge NULL dann noch komplett offen
	// bdOffeneMenge = besPosDto[i].getNMenge();
	// }
	// if(bdOffeneMenge!=null){
	// if(new BigDecimal(0).compareTo(bdOffeneMenge) < 0 &&
	// besPosDto[i].getTManuellvollstaendiggeliefert()==null){
	// bIsGeliefert = false;
	// }
	// }
	// }
	// }
	// if(bestellungDto.getBestellungartCNr().equals(
	// BESTELLUNGART_RAHMENBESTELLUNG_C_NR)){
	// BestellungDto[] abrufDto =
	// abrufBestellungenfindByRahmenbestellung(bestellungDto.getIId(),
	// theClientDto);
	// if(abrufDto.length == 0){
	// bIsAbgerufen = false;
	// } else {
	// for(int i=0;i<abrufDto.length;i++){
	// String sStatus ="";
	// if(bRekursiv){
	// //Rekursion wird maximal 1x aufgerufen, da eine Abrufbestellung keine
	// Rahmenbestellung sein kann
	// //Dadurch bRekursiv bei diesem Aufruf egal
	// sStatus =
	// getRichtigenBestellStatus(abrufDto[i].getIId(),bRekursiv,theClientDto);
	// } else {
	// sStatus = abrufDto[i].getBestellungsstatusCNr();
	// }
	// if(!sStatus.equals(BESTELLSTATUS_GELIEFERT) &&
	// !sStatus.equals(BESTELLSTATUS_ERLEDIGT) &&
	// !sStatus.equals(BESTELLSTATUS_STORNIERT) &&
	// !sStatus.equals(BESTELLSTATUS_ANGELEGT)){
	// bIsAbgerufen = false;
	// }
	// }
	// //Wenn alle Abrufe Erledigt oder Geliefert sind ueberpruefen ob die nicht
	// erledigten Positionen des Rahmens komplett abgerufen wurden
	// if(bIsAbgerufen){
	// for(int i=0;i<besPosDto.length;i++){
	// //Wenn Bestellposition nicht erledigt alle Abrufpositionen holen
	// BigDecimal bdOffeneMenge = besPosDto[i].getNOffeneMenge();
	// if(bdOffeneMenge==null){
	// //Wenn offene Menge NULL dann noch komplett offen
	// bdOffeneMenge = besPosDto[i].getNMenge();
	// }
	// if(bdOffeneMenge!=null){
	// if(/*new BigDecimal(0).compareTo(bdOffeneMenge) < 0 && */
	// besPosDto[i].getTManuellvollstaendiggeliefert()==null){
	// BestellpositionDto[] abrufePosDto =
	// getBestellpositionFac().
	// bestellpositionFindByBestellpositionIIdRahmenposition
	// (besPosDto[i].getIId(),theClientDto);
	// BigDecimal bdPosMenge = besPosDto[i].getNMenge();
	// if(bdPosMenge!=null){
	// for(int y=0;y<abrufePosDto.length;y++){
	// String sAbrufStatus =
	// getRichtigenBestellStatus(abrufePosDto[y].getBestellungIId(), true,
	// theClientDto);
	// //Die Mengen von angelegten und stornierten abrufen werden nicht
	// beruecksichtigt
	// if(!sAbrufStatus.equals(BESTELLSTATUS_STORNIERT) &&
	// !sAbrufStatus.equals(BESTELLSTATUS_ANGELEGT)){
	// if(abrufePosDto[y].getNMenge()!=null){
	// bdPosMenge = bdPosMenge.subtract(abrufePosDto[y].getNMenge());
	// }
	// }
	// }
	// //Wenn die Menge aller Abrufe kleiner als Rahmenmenge ist dann ist nicht
	// abgerufen
	// if(new BigDecimal(0).compareTo(bdPosMenge) < 0){
	// bIsAbgerufen = false;
	// //Nicht abgerufen kann auch nicht geliefert sein
	// bIsGeliefert = false;
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// } else {
	// //Eine nicht Rahmenbestellung kann nicht abgerufen sein
	// bIsAbgerufen = false;
	// }
	// //Wenn nicht geliefert abgerufen oder bestaetigt dann Offen
	// if(!bIsBestaetigt && !bIsGeliefert && !bIsAbgerufen){
	// return BESTELLSTATUS_OFFEN;
	// }
	// if(!bIsGeliefert){
	// //Wenn die Bestellung jetzt noch nicht geliefert ist kann sie nur noch im
	// Status bestaetigt oder abgerufen sein sein
	// if(bIsAbgerufen){
	// //Wenn sie abgerufen ist kann sie nicht mehr bestaetigt sein
	// return BESTELLSTATUS_ABGERUFEN;
	// } else {
	// //Die Bestellung kann hier nur noch bestaetigt sein
	// return BESTELLSTATUS_BESTAETIGT;
	// }
	// } else {
	// //Jetzt pruefen ob erledigt, wenn nicht ist sie geliefert
	// //erledigt kann eine Bestellung hier nur sein wenn alle Preise erfasst
	// sind
	// boolean bPreiseErfasst = true;
	// WareneingangDto[] weDto =
	// getWareneingangFac().wareneingangFindByBestellungIId(bestellungDto.getIId(
	// ));
	// //Es muss Wareneingaenge geben da es sonst vorher schon zu einem return
	// gekommen waere
	// for(int i=0;i<weDto.length;i++){
	// WareneingangspositionDto[] wePosDto =
	// getWareneingangFac().wareneingangspositionFindByWareneingangIId(weDto[i].
	// getIId());
	// if(wePosDto!=null){
	// for(int y=0;y<wePosDto.length;y++){
	// if(!wePosDto[y].getBPreiseErfasst()){
	// bPreiseErfasst = false;
	// }
	// }
	// }
	// }
	// if(bPreiseErfasst){
	// // Long lEnd = System.currentTimeMillis();
	// // System.out.println(lEnd - lStart);
	// return BESTELLSTATUS_ERLEDIGT;
	// } else {
	// return BESTELLSTATUS_GELIEFERT;
	// }
	// }
	// }

	@Override
	public BestellungDto[] bestellungFindByAuftragIId(Integer auftragIId) {

		if (auftragIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iId == null"));
		}

		Query query = em.createNamedQuery(Bestellung.BestellungFindByAuftragIId);
		query.setParameter(1, auftragIId);
		Collection<?> list = query.getResultList();

		return list == null ? null : assembleBestellungDtos(list);
	}

	@Override
	public BelegPruefungDto aktiviereBelegControlled(Integer iid, Timestamp t, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.aktiviereBelegControlled(this, iid, t, theClientDto);
		// new BelegAktivierungController(this).aktiviereBelegControlled(iid, t,
		// theClientDto);
	}

	@Override
	public BelegPruefungDto berechneBelegControlled(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.berechneBelegControlled(this, iid, theClientDto);
		// return new BelegAktivierungController(this).berechneBelegControlled(
		// iid, theClientDto);
	}

	@Override
	public List<Timestamp> getAenderungsZeitpunkte(Integer iid) throws EJBExceptionLP, RemoteException {
		BestellungDto b = bestellungFindByPrimaryKey(iid);
		List<Timestamp> timestamps = new ArrayList<Timestamp>();
		timestamps.add(b.getTAendern());
		timestamps.add(b.getTManuellerledigt());
		timestamps.add(b.getTManuellGeliefert());
		timestamps.add(b.getTStorniert());
		BestellpositionDto[] pos = getBestellpositionFac().bestellpositionFindByBestellung(iid, null);
		for (BestellpositionDto p : pos) {
			timestamps.add(p.getTAbterminAendern());
			timestamps.add(p.getTManuellvollstaendiggeliefert());
		}
		return timestamps;
	}

	@Override
	public BelegPruefungDto berechneAktiviereBelegControlled(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.berechneAktiviereControlled(this, iid, theClientDto);
		// new BelegAktivierungController(this).berechneAktiviereControlled(iid,
		// theClientDto);
	}

	@Override
	public BelegDto getBelegDto(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BelegDto belegDto = bestellungFindByPrimaryKey(iid);
		belegDto.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
		return belegDto;
	}

	@Override
	public BelegpositionDto[] getBelegPositionDtos(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return getBestellpositionFac().bestellpositionFindByBestellung(iid, theClientDto);
	}

	@Override
	public Integer getKundeIdDesBelegs(BelegDto belegDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return null; // es gibt keinen Kunden
	}

	@Override
	public Integer getBezugsobjektIIdDerStueckliste(StklImportSpezifikation spez, TheClientDto theClientDto)
			throws RemoteException {
		Validator.notNull(spez, "spez");
		Validator.notNull(spez.getStklIId(), "spez.getStklIId()");

		BestellungDto bestellungDto = bestellungFindByPrimaryKey(spez.getStklIId());
		return bestellungDto == null ? null : bestellungDto.getLieferantIIdBestelladresse();
	}

	@Override
	public IImportHead asHeadImporter() {
		return this;
	}

	@Override
	public void pruefeAktivierbarRecht(Integer belegId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		boolean rechtCUD = getTheJudgeFac().hatRecht(RechteFac.RECHT_BES_BESTELLUNG_CUD, theClientDto);
		if (!(rechtCUD)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN,
					"FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN");
		}
	}

	public List<BestellungDto> bestellungFindByLieferantIIdBestelladresseMandantCNrFilter(Integer lieferantId,
			String mandantCnr, String[] allowedStati) throws EJBExceptionLP, RemoteException {
		if (allowedStati == null || allowedStati.length == 0) {
			return Arrays.asList(bestellungFindByLieferantIIdBestelladresseMandantCNr(lieferantId, mandantCnr));
		} else {
			List<Bestellung> result = BestellungQuery.listByLieferantIdBestelladresseFilter(em, mandantCnr, lieferantId,
					allowedStati);
			return BestellungDtoAssembler.createList(result);
		}
	}

	@Override
	public BestellungDto erzeugeAenderungsbestellung(Integer bestellungIId, TheClientDto theClientDto) {
		Validator.notNull(bestellungIId, "bestellungIId");

		synchronized (bestellungIId) {
			Bestellung bestellung = em.find(Bestellung.class, bestellungIId);
			Versionizer versionizer = new Versionizer(em);
			versionizer.incrementVersion(bestellung);

			BestellungDto dto = bestellungFindByPrimaryKey(bestellungIId);
			dto.setTGedruckt(null); // wg. Bestellstatusueberpruefung
			dto.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
			updateBestellung(dto, theClientDto);

			return dto;
		}
	}

	@Override
	public void archiviereOpenTransResult(OpenTransXmlReportResult otResult, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.notNull(otResult, "openTransResult");
		Validator.notNull(otResult.getPrints(), "openTransResult.prints");

		JasperPrintLP print = otResult.getPrints()[0];
		if (print == null || print.getOInfoForArchive() == null || print.getOInfoForArchive().getDocPath() == null) {
			return;
		}

		Date dAnlegen = new Date(getTimestamp().getTime());
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
		PartnerDto anlegerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
		String sDate = dateFormat.format(dAnlegen);

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocPath besDocPath = print.getOInfoForArchive().getDocPath();
		String sBelegnummer = besDocPath.getLastDocNode().asVisualPath();
		sBelegnummer = sBelegnummer != null ? sBelegnummer : "000000";
		String sRow = besDocPath.getLastDocNode().asPath();
		sRow = sRow == null ? " " : sRow;
		Integer partnerId = print.getOInfoForArchive().getiId();
		if (partnerId == null) {
			partnerId = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto)
					.getPartnerIId();
		}

		jcrDocDto = new JCRDocDto();

		jcrDocDto.setDocPath(besDocPath.add(new DocNodeFile(otResult.getFilename())));
		try {
			jcrDocDto.setbData(otResult.getXmlOtOrder().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new EJBExceptionLP(e);
		}
		jcrDocDto.setsFilename(otResult.getFilename());
		jcrDocDto.setsName(otResult.getFilename());
		jcrDocDto.setsMIME(".xml");
		jcrDocDto.setsSchlagworte("Zugferd PDF");
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(anlegerDto.getIId());
		jcrDocDto.setlPartner(partnerId);
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(dAnlegen.getTime());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(sDate);
		jcrDocDto.setsRow(sRow);
		jcrDocDto.setsTable("");
		jcrDocDto.setsSchlagworte("openTRANS XML Bestellung");

		getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, theClientDto);
	}
}
