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
package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.artikel.ejb.Artikelreservierung;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelreservierung;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.ArtikelreservierungDtoAssembler;
import com.lp.server.artikel.service.ReportRahmenreservierungDto;
import com.lp.server.artikel.service.ReservierungFac;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.kueche.fastlanereader.generated.FLRSpeiseplanposition;
import com.lp.server.kueche.service.KassaartikelDto;
import com.lp.server.kueche.service.SpeiseplanDto;
import com.lp.server.kueche.service.SpeiseplanpositionDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ReservierungFacBean extends LPReport implements ReservierungFac,
		JRDataSource {
	@PersistenceContext
	private EntityManager em;

	private Object[][] data = null;
	private String sAktuellerReport = null;

	private static int REPORT_RESERVIERUNGSLISTE_AUFTRAG = 0;
	private static int REPORT_RESERVIERUNGSLISTE_KUNDENNAME = 1;
	private static int REPORT_RESERVIERUNGSLISTE_PROJEKTNAME = 2;
	private static int REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN = 3;
	private static int REPORT_RESERVIERUNGSLISTE_MENGE = 4;
	private static int REPORT_RESERVIERUNGSLISTE_ZWANGSSERIENNUMMER = 5;
	private static int REPORT_RESERVIERUNGSLISTE_STUECKLISTE = 6;

	/**
	 * Reservierte Menge eines Artikels beim Mandanten ermitteln.
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */

	public BigDecimal getAnzahlReservierungen(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return getAnzahlReservierungen(artikelIId, null,
				theClientDto.getMandant());
	}

	public BigDecimal getAnzahlReservierungen(Integer artikelIId,
			java.sql.Timestamp tStichtag, String mandantCNr)
			throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelIId == null"));
		}
		BigDecimal bdReserviert = new BigDecimal(0);
		try {
			// Alle Reservierungen dieses Artikels.
			Query query = em
					.createNamedQuery("ArtikelreservierungfindByArtikelIId");
			query.setParameter(1, artikelIId);
			Collection<?> allReservierungen = query.getResultList();
			// if (allReservierungen.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
			// }
			Iterator<?> iter = allReservierungen.iterator();
			while (iter.hasNext()) {
				Artikelreservierung artikelreservierungenTemp = (Artikelreservierung) iter
						.next();
				
				
				if(tStichtag!=null){
					if( tStichtag.getTime()<=artikelreservierungenTemp.getTLiefertermin().getTime()){
						continue;
					}
				}
				// pruefen, ob sich die Reservierung auf "meinen" Mandanten
				// bezieht.
				if (artikelreservierungenTemp.getCBelegartnr().equals(
						LocaleFac.BELEGART_AUFTRAG)) {
					// Auftragsreservierungen
					AuftragpositionDto abPosDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKeyOhneExc(
									artikelreservierungenTemp
											.getIBelegartpositionid());
					// Sicherheitshalber
					if (abPosDto != null) {
						Integer auftragIId = abPosDto.getBelegIId();
						if (getAuftragFac().auftragFindByPrimaryKey(auftragIId)
								.getMandantCNr().equals(mandantCNr)) {
							// Menge addieren
							if (artikelreservierungenTemp.getNMenge() != null) {
								bdReserviert = bdReserviert
										.add(artikelreservierungenTemp
												.getNMenge());
							}
						}
					}
				} else if (artikelreservierungenTemp.getCBelegartnr().equals(
						LocaleFac.BELEGART_LOS)) {
					// Losreservierungen
					LossollmaterialDto losPosDto = getFertigungFac()
							.lossollmaterialFindByPrimaryKeyOhneExc(
									artikelreservierungenTemp
											.getIBelegartpositionid());
					// Sicherheitshalber
					if (losPosDto != null) {
						Integer losIId = losPosDto.getLosIId();
						if (getFertigungFac().losFindByPrimaryKey(losIId)
								.getMandantCNr().equals(mandantCNr)) {
							// Menge addieren
							if (artikelreservierungenTemp.getNMenge() != null) {
								bdReserviert = bdReserviert
										.add(artikelreservierungenTemp
												.getNMenge());
							}
						}
					}
				} else if (artikelreservierungenTemp.getCBelegartnr().equals(
						LocaleFac.BELEGART_KUECHE)) {
					// Menge addieren
					if (artikelreservierungenTemp.getNMenge() != null) {
						bdReserviert = bdReserviert
								.add(artikelreservierungenTemp.getNMenge());
					}
				}
			}
		}
		// catch (javax.ejb.ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bdReserviert;
	}

	public void createArtikelreservierung(
			ArtikelreservierungDto artikelreservierungDto)
			throws EJBExceptionLP {
		if (artikelreservierungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("auftragreservierungDto == null"));
		}
		if (artikelreservierungDto.getNMenge() == null
				|| artikelreservierungDto.getCBelegartnr() == null
				|| artikelreservierungDto.getIBelegartpositionid() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"artikelreservierungDto.getNMenge() == null || artikelreservierungDto.getBelegartCNr() == null || artikelreservierungDto.getBelegartpositionIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ArtikelreservierungfindByBelegartCNrIBelegartpositionid");
			query.setParameter(1, artikelreservierungDto.getCBelegartnr());
			query.setParameter(2,
					artikelreservierungDto.getIBelegartpositionid());
			// @todo getSingleResult oder getResultList ?
			Artikelreservierung doppelt = (Artikelreservierung) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ARTIKELRESERVIERUNG.UK"));
			}
		} catch (NoResultException ex) {

		}
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen
					.getNextPrimaryKey(PKConst.PK_ARTIKELRESERVIERUNG);
			artikelreservierungDto.setIId(pk);

			Artikelreservierung artikelreservierung = new Artikelreservierung(
					artikelreservierungDto.getIBelegartpositionid(),
					artikelreservierungDto.getArtikelIId(),
					artikelreservierungDto.getTLiefertermin(),
					artikelreservierungDto.getCBelegartnr(),
					artikelreservierungDto.getIId());
			em.persist(artikelreservierung);
			em.flush();
			artikelreservierung.setNMenge(artikelreservierungDto.getNMenge());
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeArtikelreservierung(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		try {
			Artikelreservierung artikelreservierung = null;
			// try {
			artikelreservierung = em.find(Artikelreservierung.class, iId);
			if (artikelreservierung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEI_FINDBYPRIMARYKEY,
			// ex);
			em.remove(artikelreservierung);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}

	}

	public void removeArtikelreservierung(String belegartCNr,
			Integer belegartpositionIId) throws EJBExceptionLP {
		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"belegartCNr == null || belegartpositionIId == null"));
		}

		try {
			Query query = em
					.createNamedQuery("ArtikelreservierungfindByBelegartCNrIBelegartpositionid");
			query.setParameter(1, belegartCNr);
			query.setParameter(2, belegartpositionIId);
			Artikelreservierung artikelreservierung = (Artikelreservierung) query
					.getSingleResult();

			em.remove(artikelreservierung);
			em.flush();
		} catch (NoResultException ex) {
			// Wenn es keine Reservierung gibt - Beispielsweise 0 Menge - dann
			// ists ja gut
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateArtikelreservierung(
			ArtikelreservierungDto artikelreservierungDto)
			throws EJBExceptionLP {
		if (artikelreservierungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("auftragreservierungDto == null"));

		}
		if (artikelreservierungDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("auftragreservierungDto.getIId() == null"));

		}
		if (artikelreservierungDto.getIBelegartpositionid() == null
				|| artikelreservierungDto.getCBelegartnr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelreservierungDto.getBelegartpositionIId() == null || artikelreservierungDto.getBelegartCNr() == null"));

		}
		if (artikelreservierungDto.getNMenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("auftragreservierungDto.getNMenge() == null"));
		}
		// try {
		Artikelreservierung auftragreservierung = em.find(
				Artikelreservierung.class, artikelreservierungDto.getIId());
		if (auftragreservierung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		auftragreservierung.setNMenge(artikelreservierungDto.getNMenge());
		auftragreservierung.setArtikelIId(artikelreservierungDto
				.getArtikelIId());
		auftragreservierung.setTLiefertermin(artikelreservierungDto
				.getTLiefertermin());
		auftragreservierung.setCBelegartnr(artikelreservierungDto
				.getCBelegartnr());
		auftragreservierung.setIBelegartpositionid(artikelreservierungDto
				.getIBelegartpositionid());
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public void updateArtikelreservierungRelativ(
			ArtikelreservierungDto artikelreservierungDto)
			throws EJBExceptionLP {
		if (artikelreservierungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelreservierungDto == null"));

		}
		if (artikelreservierungDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelreservierungDto.getIId() == null"));

		}
		if (artikelreservierungDto.getIBelegartpositionid() == null
				|| artikelreservierungDto.getCBelegartnr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelreservierungDto.getBelegartpositionIId() == null || artikelreservierungDto.getBelegartCNr() == null"));

		}
		if (artikelreservierungDto.getTLiefertermin() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"auftragreservierungDto.getDLiefertermin() == null"));

		}
		if (artikelreservierungDto.getArtikelIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"auftragreservierungDto.getArtikelIId() == null"));

		}
		if (artikelreservierungDto.getNMenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("auftragreservierungDto.getNMenge() == null"));
		}

		// try {
		Artikelreservierung artikelreservierung = em.find(
				Artikelreservierung.class, artikelreservierungDto.getIId());
		if (artikelreservierung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		/** Alter Wert + neuer Relativer Wert = neuer Wert */
		BigDecimal valueNeu = artikelreservierung.getNMenge().add(
				artikelreservierungDto.getNMenge());

		artikelreservierung.setNMenge(valueNeu);
		artikelreservierung.setArtikelIId(artikelreservierungDto
				.getArtikelIId());
		artikelreservierung.setTLiefertermin(artikelreservierungDto
				.getTLiefertermin());
		artikelreservierung.setCBelegartnr(artikelreservierungDto
				.getCBelegartnr());
		artikelreservierung.setIBelegartpositionid(artikelreservierungDto
				.getIBelegartpositionid());
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public ArtikelreservierungDto artikelreservierungFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Artikelreservierung artikelreservierung = em.find(
				Artikelreservierung.class, iId);
		if (artikelreservierung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		return assembleArtikelreservierungDto(artikelreservierung);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArtikelreservierungDto artikelreservierungFindByBelegartCNrBelegartPositionIId(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP {
		if (belegartpositionIId == null || belegartCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartpositionIId == null || belegartCNr == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ArtikelreservierungfindByBelegartCNrIBelegartpositionid");
			query.setParameter(1, belegartCNr);
			query.setParameter(2, belegartpositionIId);
			Artikelreservierung artikelreservierung = (Artikelreservierung) query
					.getSingleResult();
			return assembleArtikelreservierungDto(artikelreservierung);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public ArtikelreservierungDto artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP {
		if (belegartpositionIId == null || belegartCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartpositionIId == null || belegartCNr == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ArtikelreservierungfindByBelegartCNrIBelegartpositionid");
			query.setParameter(1, belegartCNr);
			query.setParameter(2, belegartpositionIId);
			Artikelreservierung artikelreservierung = (Artikelreservierung) query
					.getSingleResult();
			return assembleArtikelreservierungDto(artikelreservierung);
		} catch (NoResultException e) {
			myLogger.warn("belegartCNr=" + belegartCNr
					+ ", belegartpositionIId=" + belegartpositionIId);
			return null;
		}
	}

	private void setArtikelreservierungFromArtikelreservierungDto(
			Artikelreservierung auftragreservierung,
			ArtikelreservierungDto auftragreservierungDto) {
		auftragreservierung.setNMenge(auftragreservierungDto.getNMenge());
		em.merge(auftragreservierung);
		em.flush();
	}

	private ArtikelreservierungDto assembleArtikelreservierungDto(
			Artikelreservierung artikelreservierung) {
		return ArtikelreservierungDtoAssembler.createDto(artikelreservierung);
	}

	private ArtikelreservierungDto[] assembleArtikelreservierungDtos(
			Collection<?> auftragreservierungs) {
		List<ArtikelreservierungDto> list = new ArrayList<ArtikelreservierungDto>();
		if (auftragreservierungs != null) {
			Iterator<?> iterator = auftragreservierungs.iterator();
			while (iterator.hasNext()) {
				Artikelreservierung artikelreservierung = (Artikelreservierung) iterator
						.next();
				list.add(assembleArtikelreservierungDto(artikelreservierung));
			}
		}
		ArtikelreservierungDto[] returnArray = new ArtikelreservierungDto[list
				.size()];
		return (ArtikelreservierungDto[]) list.toArray(returnArray);
	}

	/**
	 * servertransact: Wenn innerhalb einer Transaktion geprueft werden soll, ob
	 * es eine Auftragreservierung gibt, dann darf durch diese Pruefung keine
	 * Exception ausgeloest werden, das fuehrt zu einem Rollback auch wenn die
	 * Exception abgefangen wird.
	 * 
	 * @param iIdBelegartpositionI
	 *            pk der Position
	 * @param belegartCNr
	 *            Belegart
	 * @throws EJBExceptionLP
	 * @return AuftragreservierungDto die Position, wenn es keine Reservierung
	 *         gibt null
	 */
	public ArtikelreservierungDto getArtikelreservierung(String belegartCNr,
			Integer iIdBelegartpositionI) throws EJBExceptionLP {
		myLogger.entry();

		if (iIdBelegartpositionI == null || belegartCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"iIdBelegartpositionI == null || belegartCNr == null"));
		}

		ArtikelreservierungDto oReservierungDto = null;
		try {
			oReservierungDto = artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
					belegartCNr, iIdBelegartpositionI);
		} catch (Throwable t) {
			// es gibt keine Reservierung, es wird null zurueckgegeben
		}
		return oReservierungDto;
	}

	/**
	 * Reservierungen pruefen. 1. Bestehende Reservierungseintraege aus
	 * Auftrag/Los 2. Lose pruefen, ob die Reservierungen richtig eingetragen
	 * sind. 3. Auftraege pruefen, ob die Reservierungen richtig eingetragen
	 * sind.
	 * 
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void pruefeReservierungen(TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = FLRSessionFactory.getFactory().openSession();

		String hqlDelete = "delete FROM FLRArtikelreservierung";
		session.createQuery(hqlDelete).executeUpdate();

		session.close();

		// Lose
		session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria lossollmaterial = session
				.createCriteria(FLRLossollmaterial.class);
		lossollmaterial.createCriteria(FertigungFac.FLR_LOSSOLLMATERIAL_FLRLOS)
				.add(Restrictions.eq(FertigungFac.FLR_LOS_STATUS_C_NR,
						FertigungFac.STATUS_ANGELEGT));
		// Query ausfuehren
		List<?> lossollList = lossollmaterial.list();
		Iterator<?> lossollListIterator = lossollList.iterator();
		while (lossollListIterator.hasNext()) {
			FLRLossollmaterial lossollmat = (FLRLossollmaterial) lossollListIterator
					.next();
			// Fuer angelegte Lose MUSS es einen Reservierungseintrag geben.
			// nur fuer Artikel
			if (lossollmat.getFlrartikel() != null) {
				ArtikelreservierungDto resDto = new ArtikelreservierungDto();
				resDto.setArtikelIId(lossollmat.getFlrartikel().getI_id());
				resDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
				resDto.setIBelegartpositionid(lossollmat.getI_id());
				resDto.setNMenge(lossollmat.getN_menge());

				// PJ17994
				resDto.setTLiefertermin(Helper.addiereTageZuTimestamp(
						new java.sql.Timestamp(lossollmat.getFlrlos()
								.getT_produktionsbeginn().getTime()),
						lossollmat.getI_beginnterminoffset()));

				// anlegen
				createArtikelreservierung(resDto);
				myLogger.warn(theClientDto.getIDUser(),
						"Reservierung nachgetragen: " + resDto);
			}

		}

		session.close();
		// Auftraege
		session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria abPosCrit = session
				.createCriteria(FLRAuftragposition.class);
		// nur Artikel-Positionen
		abPosCrit.add(Restrictions
				.isNotNull(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRARTIKEL));

		org.hibernate.Criteria abCrit = abPosCrit
				.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG);
		// Rahmenauftraege werden ignoriert, da diese keine Reservierungen
		// ausloesen.
		abCrit.add(Restrictions.ne(AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
				AuftragServiceFac.AUFTRAGART_RAHMEN));

		// Nur Sataus Offen und Teilerledigt
		String[] stati = new String[3];
		stati[0] = AuftragServiceFac.AUFTRAGSTATUS_OFFEN;
		stati[1] = AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT;
		stati[2] = AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT;
		abCrit.add(Restrictions.in(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
				stati));

		// Query ausfuehren
		List<?> abposList = abPosCrit.list();
		Iterator<?> abPosListIterator = abposList.iterator();
		while (abPosListIterator.hasNext()) {
			FLRAuftragposition abPos = (FLRAuftragposition) abPosListIterator
					.next();
			// Fuer offene Auftraege MUSS es einen Reservierungseintrag geben.
			// (ausser positionsstatus = erledigt)

			// nur fuer noch nicht erledigte Auftragspositionen mit offener
			// Menge != 0

			if (abPos.getN_offenemenge().compareTo(new BigDecimal(0)) != 0
					&& !abPos.getAuftragpositionstatus_c_nr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
				ArtikelreservierungDto resDto = new ArtikelreservierungDto();
				resDto.setArtikelIId(abPos.getFlrartikel().getI_id());
				resDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
				resDto.setIBelegartpositionid(abPos.getI_id());
				resDto.setNMenge(abPos.getN_offenemenge());
				java.sql.Timestamp tLiefertermin;
				if (abPos.getN_offenemenge().compareTo(new BigDecimal(0)) < 0) {
					// Negative Menge -> Finaltermin
					tLiefertermin = new java.sql.Timestamp(abPos
							.getFlrauftrag().getT_finaltermin().getTime());
				} else {
					// Positive Menge -> Liefertermin

					AuftragpositionDto abPosDto = null;
					try {
						abPosDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(
										abPos.getI_id());
					} catch (RemoteException ex2) {
						throwEJBExceptionLPRespectOld(ex2);
					}
					tLiefertermin = abPosDto.getTUebersteuerbarerLiefertermin();
				}
				resDto.setTLiefertermin(tLiefertermin);
				// anlegen
				createArtikelreservierung(resDto);
				myLogger.warn(theClientDto.getIDUser(),
						"Reservierung nachgetragen: " + resDto);
			}

		}
		session.close();

		// Auftraege
		session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria speiseplan = session
				.createCriteria(FLRSpeiseplanposition.class);

		speiseplan.createCriteria("flrspeiseplan").add(
				Restrictions.ge("t_datum", Helper.cutTimestamp(new Timestamp(
						System.currentTimeMillis()))));

		// Query ausfuehren
		List<?> speiseplanList = speiseplan.list();
		Iterator<?> speiseplanListIterator = speiseplanList.iterator();
		while (speiseplanListIterator.hasNext()) {
			FLRSpeiseplanposition flrSpeiseplanposition = (FLRSpeiseplanposition) speiseplanListIterator
					.next();
			if (flrSpeiseplanposition.getN_menge().doubleValue() > 0) {
				ArtikelreservierungDto resDto = new ArtikelreservierungDto();
				resDto.setArtikelIId(flrSpeiseplanposition.getArtikel_i_id());
				resDto.setCBelegartnr(LocaleFac.BELEGART_KUECHE);
				resDto.setIBelegartpositionid(flrSpeiseplanposition.getI_id());
				resDto.setNMenge(flrSpeiseplanposition.getN_menge());
				resDto.setTLiefertermin(new Timestamp(flrSpeiseplanposition
						.getFlrspeiseplan().getT_datum().getTime()));
				createArtikelreservierung(resDto);
				myLogger.warn(theClientDto.getIDUser(),
						"Reservierung nachgetragen: " + resDto);
			}
		}

		session.close();

		myLogger.exit("Reservierungspr\u00FCfung abgeschlossen");

	}

	/**
	 * Artikelreservierungen drucken.
	 * 
	 * @todo in eine ReportFac verschieben.
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param dVon
	 *            Date
	 * @param dBis
	 *            Date
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrintLP
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP printArtikelreservierungen(Integer artikelIId,
			java.sql.Date dVon, java.sql.Date dBis, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		// Erstellung des Reports
		JasperPrintLP print = null;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ReservierungFac.REPORT_ARTIKELRESERVIERUNG;

		String eingeloggterMandant = theClientDto.getMandant();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria reservierungen = session
				.createCriteria(FLRArtikelreservierung.class);
		reservierungen.createAlias(
				ReservierungFac.FLR_ARTIKELRESERVIERUNG_FLRARTIKEL, "a").add(
				Restrictions.eq("a.i_id", artikelIId));
		if (dVon != null) {
			reservierungen.add(Restrictions.ge(
					ReservierungFac.FLR_ARTIKELRESERVIERUNG_D_LIEFERTERMIN,
					dVon));
		}
		if (dBis != null) {
			reservierungen.add(Restrictions.lt(
					ReservierungFac.FLR_ARTIKELRESERVIERUNG_D_LIEFERTERMIN,
					dBis));
		}

		reservierungen.addOrder(Order
				.asc(ReservierungFac.FLR_ARTIKELRESERVIERUNG_D_LIEFERTERMIN));

		List<?> resultList = reservierungen.list();
		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;

		Object[][] dataHelp = new Object[resultList.size()][7];

		while (resultListIterator.hasNext()) {
			FLRArtikelreservierung artikelreservierung = (FLRArtikelreservierung) resultListIterator
					.next();

			String sBelegnummer = null;
			String sStueckliste = null;
			String sZwangsSNR = null;
			String sPartner = null;
			String sProjektbezeichnung = null;

			if (artikelreservierung.getC_belegartnr().equals(
					LocaleFac.BELEGART_AUFTRAG)) {
				AuftragpositionDto auftragpositionDto = null;
				try {
					auftragpositionDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									artikelreservierung
											.getI_belegartpositionid());
					sZwangsSNR = auftragpositionDto.getCSeriennrchargennr();
				} catch (RemoteException ex1) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
				}
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(
								auftragpositionDto.getBelegIId());
				sBelegnummer = "A" + auftragDto.getCNr();

				sProjektbezeichnung = auftragDto.getCBezProjektbezeichnung();

				sPartner = getKundeFac()
						.kundeFindByPrimaryKey(
								auftragDto.getKundeIIdAuftragsadresse(),
								theClientDto).getPartnerDto()
						.formatTitelAnrede();

			} else if (artikelreservierung.getC_belegartnr().equals(
					LocaleFac.BELEGART_LOS)) {
				com.lp.server.fertigung.service.LossollmaterialDto auftragpositionDto = null;
				try {
					auftragpositionDto = getFertigungFac()
							.lossollmaterialFindByPrimaryKey(
									artikelreservierung
											.getI_belegartpositionid());
				} catch (RemoteException ex1) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
				}
				LosDto losDto = null;
				try {
					losDto = getFertigungFac().losFindByPrimaryKey(
							auftragpositionDto.getLosIId());
					sBelegnummer = "L" + losDto.getCNr();
					if (losDto.getStuecklisteIId() != null) {
						StuecklisteDto stuecklisteDto = getStuecklisteFac()
								.stuecklisteFindByPrimaryKey(
										losDto.getStuecklisteIId(),
										theClientDto);
						ArtikelDto dto = getArtikelFac()
								.artikelFindByPrimaryKey(
										stuecklisteDto.getArtikelIId(),
										theClientDto);
						sStueckliste = dto.getCNr();
					}
					sProjektbezeichnung = losDto.getCProjekt();
					if (losDto.getAuftragIId() != null) {
						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKey(losDto.getAuftragIId());
						sPartner = getKundeFac()
								.kundeFindByPrimaryKey(
										auftragDto.getKundeIIdAuftragsadresse(),
										theClientDto).getPartnerDto()
								.formatTitelAnrede();
					} else {
						sPartner = "";
					}
				} catch (RemoteException ex3) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
				}
			} else if (artikelreservierung.getC_belegartnr().equals(
					LocaleFac.BELEGART_KUECHE)) {

				SpeiseplanpositionDto speiseplanpositionDto = null;
				try {
					speiseplanpositionDto = getKuecheFac()
							.speiseplanpositionFindByPrimaryKey(
									artikelreservierung
											.getI_belegartpositionid());
				} catch (RemoteException ex1) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
				}
				SpeiseplanDto speiseplanDto = null;
				try {
					speiseplanDto = getKuecheFac().speiseplanFindByPrimaryKey(
							speiseplanpositionDto.getSpeiseplanIId());
					sBelegnummer = "K";

					// Projekt= Speisekassa

					KassaartikelDto speisekassaDto = getKuecheFac()
							.kassaartikelFindByPrimaryKey(
									speiseplanDto.getKassaartikelIId());

					sProjektbezeichnung = speisekassaDto.getCBez();

					// Kunde = Stuecklistebezeichnung
					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(
									speiseplanDto.getStuecklisteIId(),
									theClientDto);

					sPartner = stuecklisteDto.getArtikelDto()
							.formatBezeichnung();

				} catch (RemoteException ex3) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
				}
			}

			dataHelp[row][REPORT_RESERVIERUNGSLISTE_AUFTRAG] = sBelegnummer;
			dataHelp[row][REPORT_RESERVIERUNGSLISTE_STUECKLISTE] = sStueckliste;
			dataHelp[row][REPORT_RESERVIERUNGSLISTE_KUNDENNAME] = sPartner;
			dataHelp[row][REPORT_RESERVIERUNGSLISTE_PROJEKTNAME] = sProjektbezeichnung;
			dataHelp[row][REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN] = artikelreservierung
					.getT_liefertermin();
			dataHelp[row][REPORT_RESERVIERUNGSLISTE_MENGE] = artikelreservierung
					.getN_menge();
			dataHelp[row][REPORT_RESERVIERUNGSLISTE_ZWANGSSERIENNUMMER] = sZwangsSNR;
			row++;

		}
		session.close();

		data = new Object[row][7];
		for (int i = 0; i < row; i++) {
			data[i] = dataHelp[i];
		}

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		initJRDS(parameter, ReservierungFac.REPORT_MODUL,
				ReservierungFac.REPORT_ARTIKELRESERVIERUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		print = getReportPrint();
		return print;
	}

	/**
	 * Methode fuer JRDataSource
	 * 
	 * @return boolean
	 * @throws JRException
	 */
	public boolean next() throws JRException {

		index++;

		return (index < data.length);

	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;

		String fieldName = jRField.getName();

		if (sAktuellerReport.equals(ReservierungFac.REPORT_ARTIKELRESERVIERUNG)) {

			if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_AUFTRAG];
			} else if ("Kundenname".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_KUNDENNAME];
			} else if ("Projektname".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_PROJEKTNAME];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_LIEFERTERMIN];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_MENGE];
			} else if ("F_ZWANGSSERIENNUMMER".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_ZWANGSSERIENNUMMER];
			} else if ("F_STUECKLISTE".equals(fieldName)) {
				value = data[index][REPORT_RESERVIERUNGSLISTE_STUECKLISTE];
				if (value == null) {
					value = "";
				}
			}

		}
		return value;

	}

	public BigDecimal getAnzahlRahmenreservierungen(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal bdReserviert = new BigDecimal(0);
		try {
			ReportAnfragestatistikKriterienDto kritDtoI = new ReportAnfragestatistikKriterienDto();
			kritDtoI.setArtikelIId(artikelIId); // keine Datumseinschraenkung.
			ReportRahmenreservierungDto[] aResult = getArtikelReportFac()
					.getReportRahmenreservierung(kritDtoI, theClientDto);
			for (int i = 0; i < aResult.length; i++) {
				// negative Rahmenreservierungen bleiben unberuecksichtigt.
				if (aResult[i].getNOffeneMenge() != null
						&& aResult[i].getNOffeneMenge().doubleValue() > 0) {
					bdReserviert = bdReserviert.add(aResult[i]
							.getNOffeneMenge());
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bdReserviert;
	}
}
