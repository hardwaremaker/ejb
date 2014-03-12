/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.kueche.ejbfac;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.jboss.annotation.ejb.TransactionTimeout;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Einkaufsean;
import com.lp.server.artikel.ejb.Vkpfartikelpreisliste;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.kueche.ejb.Bedienerlager;
import com.lp.server.kueche.ejb.Kassaartikel;
import com.lp.server.kueche.ejb.Kassaimport;
import com.lp.server.kueche.ejb.Kdc100log;
import com.lp.server.kueche.ejb.Kuecheumrechnung;
import com.lp.server.kueche.ejb.Speiseplan;
import com.lp.server.kueche.ejb.Speiseplanposition;
import com.lp.server.kueche.ejb.Tageslos;
import com.lp.server.kueche.fastlanereader.generated.FLRSpeiseplan;
import com.lp.server.kueche.service.BedienerlagerDto;
import com.lp.server.kueche.service.BedienerlagerDtoAssembler;
import com.lp.server.kueche.service.KassaartikelDto;
import com.lp.server.kueche.service.KassaartikelDtoAssembler;
import com.lp.server.kueche.service.Kdc100logDto;
import com.lp.server.kueche.service.KuecheFac;
import com.lp.server.kueche.service.KuecheumrechnungDto;
import com.lp.server.kueche.service.KuecheumrechnungDtoAssembler;
import com.lp.server.kueche.service.SpeiseplanDto;
import com.lp.server.kueche.service.SpeiseplanDtoAssembler;
import com.lp.server.kueche.service.SpeiseplanpositionDto;
import com.lp.server.kueche.service.SpeiseplanpositionDtoAssembler;
import com.lp.server.kueche.service.TageslosDto;
import com.lp.server.kueche.service.TageslosDtoAssembler;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejb.Mwstsatz;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ProtokollDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.FileCopy;
import com.lp.util.Helper;

@Stateless
public class KuecheFacBean extends Facade implements KuecheFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createKassaartikel(KassaartikelDto kassaartikelDto) {
		if (kassaartikelDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kassaartikelDto == null"));
		}
		if (kassaartikelDto.getCBez() == null
				|| kassaartikelDto.getCArtikelnummerkassa() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"kassaartikelDto.getCBez() == null || kassaartikelDto.getCArtikelnummerkassa() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("KassaartikelfindByCArtikelnummerkassa");
			query.setParameter(1, kassaartikelDto.getCArtikelnummerkassa());
			Kassaartikel doppelt = (Kassaartikel) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"KUE_SPEISEKASSA.C_KASSENNUMMER"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SPEISEKASSA);
			kassaartikelDto.setIId(pk);
			kassaartikelDto.setISort(getNextKassaartikel());

			Kassaartikel kassaartikel = new Kassaartikel(
					kassaartikelDto.getIId(),
					kassaartikelDto.getCArtikelnummerkassa(),
					kassaartikelDto.getCBez(), kassaartikelDto.getISort());
			em.persist(kassaartikel);
			em.flush();
			setKassaartikelFromKassaartikelDto(kassaartikel, kassaartikelDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return kassaartikelDto.getIId();
	}

	public Integer getNextKassaartikel() throws EJBExceptionLP {

		try {
			Query querynext = em
					.createNamedQuery("KassaartikelejbSelectNextReihung");

			Integer i = (Integer) querynext.getSingleResult();
			if (i != null) {
				i = i + 1;
			} else {
				i = 1;
			}
			return i;
		} catch (NoResultException ex) {
			return new Integer(1);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

	}

	public BigDecimal getTheoretischerWareneinsatz(Integer speiseplanIId,
			Integer artikelIId, BigDecimal nMenge, TheClientDto theClientDto) {

		BigDecimal theoretischerWareneinsatz = new BigDecimal(0);

		if (speiseplanIId != null) {
			SpeiseplanDto speiseplanDto = speiseplanFindByPrimaryKey(speiseplanIId);

			Query query = em
					.createNamedQuery("SpeiseplanpositionfindBySpeiseplanIId");
			query.setParameter(1, speiseplanIId);
			Collection speiseplanpositions = query.getResultList();

			Iterator<?> iterator = speiseplanpositions.iterator();
			while (iterator.hasNext()) {
				Speiseplanposition speiseplanposition = (Speiseplanposition) iterator
						.next();

				BigDecimal menge = new BigDecimal(0);

				if (speiseplanDto.getNMenge().doubleValue() != 0) {
					menge = speiseplanposition.getNMenge()
							.divide(speiseplanDto.getNMenge()).multiply(nMenge);
				}

				ArtikellieferantDto ekPreis = getArtikelFac()
						.getArtikelEinkaufspreis(
								speiseplanposition.getArtikelIId(), null,
								menge, theClientDto.getSMandantenwaehrung(),
								null, theClientDto);
				if (ekPreis != null && ekPreis.getLief1Preis() != null) {

					theoretischerWareneinsatz = theoretischerWareneinsatz
							.add(ekPreis.getLief1Preis().multiply(menge));
				}
			}

		} else {
			ArtikellieferantDto ekPreis = getArtikelFac()
					.getArtikelEinkaufspreis(artikelIId, null, nMenge,
							theClientDto.getSMandantenwaehrung(), null,
							theClientDto);
			if (ekPreis != null && ekPreis.getNNettopreis() != null) {

				theoretischerWareneinsatz = theoretischerWareneinsatz
						.add(ekPreis.getNNettopreis().multiply(nMenge));
			}
		}

		return theoretischerWareneinsatz;
	}

	public int getAnzahlKassaimportZuSpeiseplan(Integer speiseplanIId) {

		try {
			Query querynext = em
					.createNamedQuery("KassaimportAnzahlSpeiseplanIId");
			querynext.setParameter(1, speiseplanIId);

			Integer i = ((Long) querynext.getSingleResult()).intValue();
			if (i == null) {
				i = 0;
			}
			return i;
		} catch (NoResultException ex) {
			return new Integer(1);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

	}

	public void vertauscheKassaartikel(Integer iId1I, Integer iId2I) {
		// try {
		Kassaartikel o1 = em.find(Kassaartikel.class, iId1I);
		if (o1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Kassaartikel o2 = em.find(Kassaartikel.class, iId2I);
		if (o2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = o1.getISort();
		Integer iSort2 = o2.getISort();

		o2.setISort(new Integer(-1));

		o1.setISort(iSort2);
		o2.setISort(iSort1);

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void importiereAlleKassenfiles(TheClientDto theClientDto) {

		ProtokollDto protokollDto = new ProtokollDto();

		protokollDto.setCArt(SystemFac.PROTOKOLL_ART_FEHLER);
		protokollDto.setCTyp(SystemFac.PROTOKOLL_TYP_KASSA_OSCAR);

		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUECHE,
							ParameterFac.PARAMETER_PFAD_KASSENIMPORTDATEIEN_OSCAR);
			String pfad = parameter.getCWert();

			if (pfad != null && !pfad.endsWith("/")) {
				pfad += "/";
			}

			File f = new File(pfad);

			File[] fileArray = f.listFiles();

			if (fileArray != null) {

				for (int i = 0; i < fileArray.length; i++) {
					if (fileArray[i].isFile() == true) {

						FileReader leser = new FileReader(fileArray[i]);

						StringBuffer sb = new StringBuffer();

						// erzeugen einer Dauerschleife:
						for (;;) {
							int gelesenInt = leser.read();

							// Wenn kein Zeichen mehr zurueckgegeben wird (= -1),
							// dann ist das Dateienende ereicht,
							// daher aufhoeren
							if (gelesenInt == -1) {
								break;
							}

							// ein char wird als integer eingelesen!
							// Daher als char umwandeln
							char gelesenChar = (char) gelesenInt;

							// Jedes Zeichen ausgeben
							sb.append(gelesenChar);
						}
						leser.close();

						ArrayList<String> al = new ArrayList<String>();

						int index = 0;

						while (index >= 0 && index < sb.length()) {
							int cr = sb.indexOf("\r\n", index);

							if (cr > -1) {
								al.add(new String(sb.substring(index, cr + 2)));
								index = cr + 2;
							} else {
								break;
							}

						}

						ArrayList<String> alQueue = null;

						// Essensausgabe/Shop
						alQueue = getKuecheFac().importiereKassenfileOscar(al,
								theClientDto);

						if (alQueue.size() > 0) {

							// Pruefen, ob Verzeichnis QUEUE bereits angelegt
							File verzeichnisQueue = new File(pfad + "queue");
							if (verzeichnisQueue.isDirectory()) {
							} else {
								verzeichnisQueue.mkdir();
							}

							File fQueue = new File(pfad + "queue/queue_"
									+ fileArray[i].getName());
							fQueue.createNewFile();
							FileWriter fw = new FileWriter(fQueue);
							BufferedWriter bw = new BufferedWriter(fw);

							for (int j = 0; j < alQueue.size(); j++) {
								bw.write(alQueue.get(j));
							}
							bw.flush();
							bw.close();
							fw.close();
						}

						// Pruefen, ob Verzeichnis BACKUP bereits angelegt
						File verzeichnisBackup = new File(pfad + "backup");
						if (verzeichnisBackup.isDirectory()) {
						} else {
							verzeichnisBackup.mkdir();
						}

						File ziel = new File(pfad + "backup/"
								+ fileArray[i].getName());

						FileCopy fc = new FileCopy();
						fc.copy(fileArray[i], ziel);

						fileArray[i].delete();

					}
				}
			}

		} catch (Throwable ex2) {
			ex2.printStackTrace();
			protokollDto.setCText("Fehler Import Kassendateien: "
					+ ex2.getMessage());
			erstelleProtokollEintrag(protokollDto, theClientDto);
		}

		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUECHE,
							ParameterFac.PARAMETER_PFAD_KASSENIMPORTDATEIEN_ADS);
			String pfad = parameter.getCWert();

			if (pfad != null && !pfad.endsWith("/")) {
				pfad += "/";
			}

			File f = new File(pfad);

			File[] fileArray = f.listFiles();

			if (fileArray != null) {

				for (int i = 0; i < fileArray.length; i++) {
					if (fileArray[i].isFile() == true) {

						FileReader leser = new FileReader(fileArray[i]);

						StringBuffer sb = new StringBuffer();

						// erzeugen einer Dauerschleife:
						for (;;) {
							int gelesenInt = leser.read();

							// Wenn kein Zeichen mehr zurueckgegeben wird (= -1),
							// dann ist das Dateienende ereicht,
							// daher aufhoeren
							if (gelesenInt == -1) {
								break;
							}

							// ein char wird als integer eingelesen!
							// Daher als char umwandeln
							char gelesenChar = (char) gelesenInt;

							// Jedes Zeichen ausgeben
							sb.append(gelesenChar);
						}
						leser.close();

						ArrayList<String> al = new ArrayList<String>();

						int index = 0;

						while (index >= 0 && index < sb.length()) {
							int cr = sb.indexOf("\r\n", index);

							if (cr > -1) {
								al.add(new String(sb.substring(index, cr + 2)));
								index = cr + 2;
							} else {
								break;
							}

						}

						ArrayList<String> alQueue = null;

						// Gaestehaus
						alQueue = getKuecheFac()
								.importiereKassenfileGaestehaus(al,
										theClientDto);

						if (alQueue.size() > 0) {

							// Pruefen, ob Verzeichnis QUEUE bereits angelegt
							File verzeichnisQueue = new File(pfad + "queue");
							if (verzeichnisQueue.isDirectory()) {
							} else {
								verzeichnisQueue.mkdir();
							}

							File fQueue = new File(pfad + "queue/queue_"
									+ fileArray[i].getName());
							fQueue.createNewFile();
							FileWriter fw = new FileWriter(fQueue);
							BufferedWriter bw = new BufferedWriter(fw);

							for (int j = 0; j < alQueue.size(); j++) {
								bw.write(alQueue.get(j));
							}
							bw.flush();
							fw.close();
						}

						// Pruefen, ob Verzeichnis BACKUP bereits angelegt
						File verzeichnisBackup = new File(pfad + "backup");
						if (verzeichnisBackup.isDirectory()) {
						} else {
							verzeichnisBackup.mkdir();
						}

						File ziel = new File(pfad + "backup/"
								+ fileArray[i].getName());

						FileCopy fc = new FileCopy();
						fc.copy(fileArray[i], ziel);

						fileArray[i].delete();

					}
				}
			}

		} catch (Throwable ex2) {
			ex2.printStackTrace();
			protokollDto.setCText("Fehler Import Kassendateien: "
					+ ex2.getMessage());
			erstelleProtokollEintrag(protokollDto, theClientDto);
		}

	}

	private BigDecimal anzahlArtikelAbbuchbar(Integer artikelIId,
			Integer lagerIId, boolean bLagerbewirtschaftet, BigDecimal nMenge,
			TheClientDto theClientDto) {

		BigDecimal anzahl = nMenge;

		try {
			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
							theClientDto);
			if (stklDto == null
					|| !stklDto.getStuecklisteartCNr().equals(
							StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {

				if (bLagerbewirtschaftet) {
					return getLagerFac().getLagerstand(artikelIId, lagerIId,
							theClientDto);
				} else {
					return new BigDecimal(99999999);
				}
			} else {
				List<?> m = null;
				try {
					m = getStuecklisteFac()
							.getStrukturDatenEinerStueckliste(
									stklDto.getIId(),
									theClientDto,
									StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR,
									0, null, false, false, nMenge, null, true);
				} catch (RemoteException ex4) {
					throwEJBExceptionLPRespectOld(ex4);
				}

				// Zuerst Gesamtwert berechnen
				Iterator<?> it = m.listIterator();

				while (it.hasNext()) {
					StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
							.next();
					StuecklistepositionDto position = struktur
							.getStuecklistepositionDto();

					if (position.getArtikelIId() != null) {

						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										position.getArtikelIId(), theClientDto);

						if (Helper
								.short2boolean(aDto.getBLagerbewirtschaftet())) {

							BigDecimal lagerstand = getLagerFac()
									.getLagerstand(position.getArtikelIId(),
											lagerIId, theClientDto);

							BigDecimal benoetigteMenge = Helper
									.rundeKaufmaennisch(position
											.getNZielmenge().multiply(nMenge),
											4);
							if (benoetigteMenge.doubleValue() > lagerstand
									.doubleValue()) {
								Integer anzahlTemp = lagerstand
										.divide(benoetigteMenge, 4,
												BigDecimal.ROUND_HALF_EVEN)
										.multiply(nMenge).intValue();
								if (anzahlTemp.doubleValue() < anzahl
										.doubleValue()) {
									anzahl = new BigDecimal(
											anzahlTemp.doubleValue());
								}
							}
						}
					}

				}

			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return anzahl;

	}

	public ArrayList<String> importiereKassenfileGaestehaus(
			ArrayList<String> zeilen, TheClientDto theClientDto) {
		ArrayList<String> alQueue = new ArrayList<String>();

		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}
		String letzteRechnungsnummer = "";
		Integer letzterLieferscheinIId = null;

		for (int i = 0; i < zeilen.size(); i++) {
			System.out.println(i);
			ProtokollDto protokollDto = new ProtokollDto();
			String sZeile = "in Zeile " + i;
			protokollDto.setCArt(SystemFac.PROTOKOLL_ART_FEHLER);
			protokollDto.setCTyp(SystemFac.PROTOKOLL_TYP_KASSA_ADS3000);

			String zeile = (String) zeilen.get(i);

			if (zeile == null || zeile.trim().length() < 1) {
				continue;
			}

			String artikelnummerkassa = zeile.substring(20, 25);
			artikelnummerkassa = artikelnummerkassa.replaceFirst("0*", "");

			try {
				Query artikelQuery = em
						.createNamedQuery("ArtikelfindByCNrMandantCNr");
				artikelQuery.setParameter(1, artikelnummerkassa);
				artikelQuery.setParameter(2, theClientDto.getMandant());
				Artikel artikel = (Artikel) artikelQuery.getSingleResult();

				String kundennummer = zeile.substring(0, 8);

				kundennummer = kundennummer.replaceFirst("0*", "");

				Session session2 = FLRSessionFactory.getFactory().openSession();
				String sQuery2 = "FROM FLRKunde flrkunde WHERE flrkunde.flrpartner.c_filialnummer='"
						+ kundennummer + "'";
				org.hibernate.Query query2 = session2.createQuery(sQuery2);

				List<?> results2 = query2.list();
				Iterator<?> resultListIterator2 = results2.iterator();

				if (resultListIterator2.hasNext()) {
					FLRKunde flrKunde = (FLRKunde) resultListIterator2.next();

					String rechnungsnummer = zeile.substring(9, 20);
					String datum = zeile.substring(48, 54);

					SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
					java.sql.Date date = null;
					try {
						date = new java.sql.Date(format.parse(datum).getTime());
						protokollDto.setTQuelle(new java.sql.Timestamp(date
								.getTime()));

					} catch (ParseException e) {
						protokollDto
								.setCText("Datum konnte nicht konvertiert werden: "
										+ datum + "," + sZeile);
						erstelleProtokollEintrag(protokollDto, theClientDto);
						alQueue.add(zeile);
						continue;

					}

					String menge = zeile.substring(54, 62);

					BigDecimal bdMenge = new BigDecimal(menge).setScale(2)
							.divide(new BigDecimal(100));

					String bediener = zeile.substring(75, 77);

					Query queryVkpfartikelpreisliste = em
							.createNamedQuery("VkpfartikelpreislistefindByCFremdsystemnr");
					queryVkpfartikelpreisliste.setParameter(1, bediener);
					Collection c = queryVkpfartikelpreisliste.getResultList();

					// PJ15313
					Integer abbuchungslagerIId = null;
					try {
						Query query = em
								.createNamedQuery("BedienerlagerFindByCBedienernummer");
						query.setParameter(1, bediener);
						Bedienerlager eintrag = (Bedienerlager) query
								.getSingleResult();
						if (eintrag != null) {
							abbuchungslagerIId = eintrag.getLagerIId();
						}
					} catch (NoResultException ex) {
						//
					}

					if (c.size() > 0) {

						Iterator<?> iterator = c.iterator();

						Vkpfartikelpreisliste vkpfPreisliste = (Vkpfartikelpreisliste) iterator
								.next();

						try {
							VkPreisfindungPreislisteDto artikelPreisliste = getVkPreisfindungFac()
									.getAktuellePreislisteByArtikelIIdPreislisteIId(
											artikel.getIId(),
											vkpfPreisliste.getIId(),
											new Date(System.currentTimeMillis()),
											theClientDto
													.getSMandantenwaehrung(),
											theClientDto);

							BigDecimal preis = new BigDecimal(0);

							KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKeyOhneExc(
											flrKunde.getI_id(), theClientDto);

							MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
									.mwstsatzFindByMwstsatzbezIIdAktuellster(
											kundeDto.getMwstsatzbezIId(),
											theClientDto);

							if (abbuchungslagerIId == null) {
								abbuchungslagerIId = flrKunde
										.getLager_i_id_abbuchungslager();
							}

							if (artikelPreisliste != null) {

								VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
										.verkaufspreisfindung(
												artikel.getIId(),
												flrKunde.getI_id(),
												bdMenge,
												new Date(System
														.currentTimeMillis()),
												vkpfPreisliste.getIId(),
												mwstsatzDtoAktuell.getIId(),
												theClientDto
														.getSMandantenwaehrung(),
												theClientDto);

								VerkaufspreisDto kundenVKPreisDto = Helper
										.getVkpreisBerechnet(vkpreisfindungDto);
								preis = kundenVKPreisDto.nettopreis;

							}

							if (!letzteRechnungsnummer.equals(rechnungsnummer)) {

								LieferscheinDto lieferscheinDto = new LieferscheinDto();
								lieferscheinDto
										.setKundeIIdLieferadresse(flrKunde
												.getI_id());
								lieferscheinDto
										.setKundeIIdRechnungsadresse(flrKunde
												.getI_id());
								lieferscheinDto
										.setLieferscheinartCNr(LieferscheinFac.LSART_FREI);
								lieferscheinDto
										.setPersonalIIdVertreter(theClientDto
												.getIDPersonal());

								lieferscheinDto.setLagerIId(abbuchungslagerIId);

								lieferscheinDto
										.setWaehrungCNr(theClientDto
												.getSMandantenwaehrung());
								lieferscheinDto
										.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
												1));

								lieferscheinDto.setCKommission(rechnungsnummer);

								lieferscheinDto.setTBelegdatum(new java.sql.Timestamp(date.getTime()));
								lieferscheinDto.setMandantCNr(theClientDto
										.getMandant());

								if (kundeDto.getKostenstelleIId() != null) {
									lieferscheinDto.setKostenstelleIId(kundeDto
											.getKostenstelleIId());
								} else {
									lieferscheinDto
											.setKostenstelleIId(mandantDto
													.getIIdKostenstelle());
								}

								lieferscheinDto
										.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

								lieferscheinDto.setLieferartIId(mandantDto
										.getLieferartIIdKunde());
								lieferscheinDto.setSpediteurIId(mandantDto
										.getSpediteurIIdKunde());
								lieferscheinDto.setZahlungszielIId(mandantDto
										.getZahlungszielIIdKunde());

								letzterLieferscheinIId = getLieferscheinFac()
										.createLieferschein(lieferscheinDto,
												theClientDto);
							}

							letzteRechnungsnummer = rechnungsnummer;

							// Position anlegen
							LieferscheinpositionDto lieferscheinposDto = new LieferscheinpositionDto();
							lieferscheinposDto
									.setLieferscheinIId(letzterLieferscheinIId);
							lieferscheinposDto
									.setLieferscheinpositionartCNr(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);

							BigDecimal lagerstand = anzahlArtikelAbbuchbar(
									artikel.getIId(), abbuchungslagerIId,
									Helper.short2boolean(artikel
											.getBLagerbewirtschaftet()),
									bdMenge, theClientDto);

							String kunde = flrKunde.getFlrpartner()
									.getC_name1nachnamefirmazeile1()
									+ " "
									+ flrKunde.getFlrpartner()
											.getC_name2vornamefirmazeile2();
							String lager = getLagerFac().lagerFindByPrimaryKey(
									abbuchungslagerIId).getCNr();

							if (lagerstand.doubleValue() <= 0) {
								// FEHLER -> ZEILE IN QUEUE-FILE LEGEN
								protokollDto
										.setCText("'Zuwenig auf Lager' auf Lager '"
												+ lager
												+ "' f\u00FCr  Artikelnummer "
												+ artikel.getCNr()
												+ " und Kunde "
												+ kunde
												+ " Ben\u00F6tigt: "
												+ bdMenge.doubleValue()
												+ " "
												+ sZeile);
								erstelleProtokollEintrag(protokollDto,
										theClientDto);
								alQueue.add(zeile);
								continue;

							} else if (bdMenge.doubleValue() > lagerstand
									.doubleValue()) {
								lieferscheinposDto.setNMenge(lagerstand);

								BigDecimal rest = bdMenge.subtract(lagerstand);
								String teil1 = zeile.substring(0, 55);
								String teil2 = zeile.substring(62);

								String zahl = Helper
										.rundeKaufmaennisch(rest, 2)
										.movePointRight(2).toString();

								zahl = Helper.fitString2LengthAlignRight(zahl,
										7, '0');

								zeile = teil1 + zahl + teil2;

								// FEHLER -> REST IN QUEUE-FILE LEGEN
								alQueue.add(zeile);

							} else {
								lieferscheinposDto.setNMenge(bdMenge);
							}

							lieferscheinposDto.setNNettoeinzelpreis(preis);
							lieferscheinposDto.setNEinzelpreis(preis);

							lieferscheinposDto.setFRabattsatz(0.0);
							lieferscheinposDto.setFZusatzrabattsatz(0.0);
							lieferscheinposDto
									.setBNettopreisuebersteuert(Helper
											.boolean2Short(false));
							lieferscheinposDto.setArtikelIId(artikel.getIId());
							lieferscheinposDto.setEinheitCNr(artikel
									.getEinheitCNr());

							// Ausser der Kunde hat MWST-Satz mit 0%, dann muss
							// dieser
							// verwendet werden

							BigDecimal mwstBetrag = new BigDecimal(0);
							if (mwstsatzDtoAktuell.getFMwstsatz().doubleValue() == 0) {
								lieferscheinposDto
										.setMwstsatzIId(mwstsatzDtoAktuell
												.getIId());
							} else {
								if (artikel.getMwstsatzIId() == null) {
									protokollDto
											.setCText("Es konnte kein MWST- Satz Artikelnummer "
													+ artikel.getCNr()
													+ " gefunden werden. "
													+ sZeile);
									erstelleProtokollEintrag(protokollDto,
											theClientDto);
									alQueue.add(zeile);
									continue;
								} else {
									MwstsatzDto mwstsatzAktuell = getMandantFac()
											.mwstsatzFindByMwstsatzbezIIdAktuellster(
													artikel.getMwstsatzIId(),
													theClientDto);

									mwstBetrag = preis.multiply(new BigDecimal(
											mwstsatzDtoAktuell.getFMwstsatz())
											.movePointLeft(2));

									if (mwstsatzAktuell != null) {
										lieferscheinposDto
												.setMwstsatzIId(mwstsatzAktuell
														.getIId());
									} else {
										// fehler
									}
								}
							}

							lieferscheinposDto.setNBruttoeinzelpreis(preis
									.add(mwstBetrag));
							lieferscheinposDto.setNMwstbetrag(mwstBetrag);

							try {
								getLieferscheinpositionFac()
										.createLieferscheinposition(
												lieferscheinposDto, true,
												theClientDto);
							} catch (EJBExceptionLP e) {
								if (e.getCode() == EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER) {
									protokollDto
											.setCText("'Zuwenig auf Lager' auf Lager '"
													+ lager
													+ "' f\u00FCr  Artikelnummer "
													+ artikel.getCNr()
													+ " und Kunde "
													+ kunde
													+ " Ben\u00F6tigt: "
													+ bdMenge.doubleValue()
													+ " " + sZeile);
									erstelleProtokollEintrag(protokollDto,
											theClientDto);
									alQueue.add(zeile);
									continue;
								}
							}

							protokollDto.setCArt(SystemFac.PROTOKOLL_ART_INFO);
							protokollDto.setCText(lieferscheinposDto
									.getNMenge()
									+ " Einheiten mit der Artikelnummer "
									+ artikel.getCNr()
									+ " mit Lieferschein abgebucht. " + sZeile);
							erstelleProtokollEintrag(protokollDto, theClientDto);

						} catch (Throwable t) {
							// ignore
							t.printStackTrace();
						}

					} else {
						// fehler
						protokollDto.setCText("Fremdsystemnummer " + bediener
								+ " konnte nicht gefunden werden. " + sZeile);
						erstelleProtokollEintrag(protokollDto, theClientDto);

						alQueue.add(zeile);
					}

				} else {
					protokollDto
							.setCText("Es konnte kein Kunde zu Filialnummer "
									+ kundennummer + " gefunden werden. "
									+ sZeile);
					erstelleProtokollEintrag(protokollDto, theClientDto);

					alQueue.add(zeile);
				}
				session2.close();
			} catch (NoResultException e) {
				// Fehler Artikelnummer nicht gefunden
				protokollDto.setCText("Zu Kassenartikelnummer "
						+ artikelnummerkassa
						+ " konnte kein Artikel in HeliumV gefunden werden. "
						+ sZeile);
				erstelleProtokollEintrag(protokollDto, theClientDto);

				alQueue.add(zeile);
			}

			// String preis = zeile.substring(62, 69); Preis kommt aus HeliumV

		}

		return alQueue;
	}

	public ArrayList<String> importiereKassenfileOscar(
			ArrayList<String> zeilen, TheClientDto theClientDto) {

		ArrayList<String> alQueue = new ArrayList<String>();

		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}
		// PJ16437
		HashMap betroffeneLS = new HashMap();
		for (int i = 0; i < zeilen.size(); i++) {
			String zeile = (String) zeilen.get(i);

			String sZeile = "in Zeile " + i;

			ProtokollDto protokollDto = new ProtokollDto();

			protokollDto.setCArt(SystemFac.PROTOKOLL_ART_FEHLER);
			protokollDto.setCTyp(SystemFac.PROTOKOLL_TYP_KASSA_OSCAR);

			String datum = zeile.substring(1, 9);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			java.sql.Date date = null;
			try {
				date = new java.sql.Date(format.parse(datum).getTime());
				protokollDto.setTQuelle(new java.sql.Timestamp(date.getTime()));

			} catch (ParseException e) {

				protokollDto.setCText("Datum konnte nicht konvertiert werden: "
						+ datum + "," + sZeile);
				erstelleProtokollEintrag(protokollDto, theClientDto);
				alQueue.add(zeile);
				continue;

			}

			String kassennummer = zeile.substring(22, 36);

			kassennummer = kassennummer.replaceFirst("0*", "");

			Integer speiseplanIId = null;

			Integer artikelIId = null;

			Session session = FLRSessionFactory.getFactory().openSession();

			String sQuery = "FROM FLRSpeiseplan flrspeiseplan WHERE flrspeiseplan.flrkassaartikel.c_artikelnummerkassa='"
					+ kassennummer
					+ "'"
					+ " AND flrspeiseplan.t_datum='"
					+ Helper.formatDateWithSlashes(date) + "'";
			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			// Zuerst in Speiseplan nachsehen, dann in Umrechung, dann in
			// Artikel

			if (resultListIterator.hasNext()) {
				FLRSpeiseplan speisplan = (FLRSpeiseplan) resultListIterator
						.next();
				speiseplanIId = speisplan.getI_id();
				artikelIId = speisplan.getFlrstueckliste().getFlrartikel()
						.getI_id();
			} else {

				try {
					Query queryUmrechnung = em
							.createNamedQuery("KuecheumrechnungfindByCArtikelnummerkassa");
					queryUmrechnung.setParameter(1, kassennummer);
					Kuecheumrechnung kuecheumrechnung = (Kuecheumrechnung) queryUmrechnung
							.getSingleResult();
					artikelIId = kuecheumrechnung.getArtikelIId();
				} catch (NoResultException ex) {

					try {
						Query queryArtikel = em
								.createNamedQuery("ArtikelfindByCNrMandantCNr");
						queryArtikel.setParameter(1, kassennummer);
						queryArtikel.setParameter(2, theClientDto.getMandant());

						Artikel artikel = (Artikel) queryArtikel
								.getSingleResult();
						artikelIId = artikel.getIId();
					} catch (NoResultException ex1) {
						//
					}

				}

			}

			if (artikelIId != null) {

				String kundennummer = zeile.substring(18, 22);

				kundennummer = kundennummer.replaceFirst("0*", "");

				Session session2 = FLRSessionFactory.getFactory().openSession();
				String sQuery2 = "FROM FLRKunde flrkunde WHERE flrkunde.flrpartner.c_filialnummer='"
						+ kundennummer + "'";
				org.hibernate.Query query2 = session2.createQuery(sQuery2);

				List<?> results2 = query2.list();
				Iterator<?> resultListIterator2 = results2.iterator();

				if (resultListIterator2.hasNext()) {
					FLRKunde flrKunde = (FLRKunde) resultListIterator2.next();

					String menge = zeile.substring(49, 61); // 3 Nachkomma
					String preis = zeile.substring(36, 49); // 2 Nachkomma

					BigDecimal bdMenge = new BigDecimal(menge).setScale(3)
							.divide(new BigDecimal(1000));

					BigDecimal bdPreisGesamt = new BigDecimal(preis)
							.setScale(2).divide(new BigDecimal(100));

					if (bdMenge.doubleValue() == 0) {
						protokollDto
								.setCText("Menge 0 aus Import-File f\u00FCr Artikelnummer-Kassa "
										+ kassennummer + " " + sZeile);
						erstelleProtokollEintrag(protokollDto, theClientDto);
						alQueue.add(zeile);
						continue;
					}

					// Mit voller Genauigkeit dividieren
					BigDecimal einzelpreisBrutto = bdPreisGesamt.divide(
							bdMenge, 6, BigDecimal.ROUND_HALF_EVEN);
					BigDecimal einzelpreisNetto = einzelpreisBrutto;

					// Umrechnung
					BigDecimal bdUmrechnungsfaktor = new BigDecimal(1);
					boolean bKuecheUmrechnungVorhanden = false;

					try {
						Query queryUmrechnung = em
								.createNamedQuery("KuecheumrechnungfindByCArtikelnummerkassa");
						queryUmrechnung.setParameter(1, kassennummer);
						Kuecheumrechnung kuecheumrechnung = (Kuecheumrechnung) queryUmrechnung
								.getSingleResult();
						if (kuecheumrechnung.getNFaktor().doubleValue() != 0) {
							bdUmrechnungsfaktor = kuecheumrechnung.getNFaktor();
						}
						artikelIId = kuecheumrechnung.getArtikelIId();
						bKuecheUmrechnungVorhanden = true;
					} catch (NoResultException ex) {
						//
					}

					bdMenge = bdMenge.divide(bdUmrechnungsfaktor, 4,
							BigDecimal.ROUND_HALF_EVEN);

					// Noch die MWST abziehen

					try {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(artikelIId,
										theClientDto);

						if (artikelDto.getMwstsatzbezIId() != null) {

							Query ejbquery = em
									.createNamedQuery("MwstsatzfindByTGueltigabMwstsatzbezIId");
							ejbquery.setParameter(1, date);
							ejbquery.setParameter(2,
									artikelDto.getMwstsatzbezIId());
							ejbquery.setMaxResults(1); // ist absteigend
														// sortiert!
							Collection c = ejbquery.getResultList();

							if (c.size() > 0) {

								Mwstsatz mwstsatz = (Mwstsatz) c.iterator()
										.next();

								if (mwstsatz.getFMwstsatz() > 0) {

									BigDecimal bdMwstSatz = new BigDecimal(
											mwstsatz.getFMwstsatz())
											.movePointLeft(2);

									einzelpreisNetto = einzelpreisBrutto
											.divide(new BigDecimal(1)
													.add(bdMwstSatz),
													BigDecimal.ROUND_HALF_EVEN);

								}
							}
						}

						// PJ14318
						// Wenn Partnerklasse und deren Import = Shop, dann LS
						// schreiben
						if ((flrKunde.getFlrpartner().getFlrpartnerklasse() != null
								&& flrKunde.getFlrpartner()
										.getFlrpartnerklasse().getC_importart() != null && flrKunde
								.getFlrpartner().getFlrpartnerklasse()
								.getC_importart()
								.equals(PartnerFac.IMPORTART_SHOP))
								|| bKuecheUmrechnungVorhanden == true) {

							// Zuerst nachsehen ob es schon einen angelegten
							// Lieferschein zum Datum gibt
							Session session3 = FLRSessionFactory.getFactory()
									.openSession();
							String sQuery3 = "FROM FLRLieferschein flrLieferschein WHERE flrLieferschein.kunde_i_id_rechnungsadresse="
									+ flrKunde.getI_id()
									+ " AND flrLieferschein.d_belegdatum='"
									+ Helper.formatDateWithSlashes(date)
									+ "' AND flrLieferschein.lieferscheinstatus_status_c_nr='"
									+ LocaleFac.STATUS_ANGELEGT
									+ "' ORDER BY flrLieferschein.c_nr DESC";

							org.hibernate.Query query3 = session3
									.createQuery(sQuery3);

							List<?> results3 = query3.list();
							Iterator<?> resultListIterator3 = results3
									.iterator();

							Integer lieferscheinIId = null;

							KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKeyOhneExc(
											flrKunde.getI_id(), theClientDto);

							// PJ 974 Zuerst Lagerstand pruefen, dan ansonsten
							// ein Lieferschein ohne Positionen nicht aktiviert
							// werden kann

							BigDecimal lagerstand = anzahlArtikelAbbuchbar(
									artikelDto.getIId(),
									flrKunde.getLager_i_id_abbuchungslager(),
									Helper.short2boolean(artikelDto
											.getBLagerbewirtschaftet()),
									bdMenge, theClientDto);
							if (lagerstand.doubleValue() <= 0) {
								// FEHLER -> ZEILE IN QUEUE-FILE LEGEN
								protokollDto
										.setCText("'Zuwenig auf Lager' f\u00FCr  Artikelnummer "
												+ artikelDto.getCNr()
												+ " Ben\u00F6tigt: "
												+ bdMenge.doubleValue()
												+ " "
												+ sZeile);
								erstelleProtokollEintrag(protokollDto,
										theClientDto);
								alQueue.add(zeile);
								continue;

							}

							if (resultListIterator3.hasNext()) {
								FLRLieferschein flrLieferschein = (FLRLieferschein) resultListIterator3
										.next();

								lieferscheinIId = flrLieferschein.getI_id();

							} else {
								// Wenn nein, dann anlegen

								LieferscheinDto lieferscheinDto = new LieferscheinDto();
								lieferscheinDto
										.setKundeIIdLieferadresse(flrKunde
												.getI_id());
								lieferscheinDto
										.setKundeIIdRechnungsadresse(flrKunde
												.getI_id());
								lieferscheinDto
										.setLieferscheinartCNr(LieferscheinFac.LSART_FREI);
								lieferscheinDto
										.setPersonalIIdVertreter(theClientDto
												.getIDPersonal());
								lieferscheinDto.setLagerIId(flrKunde
										.getLager_i_id_abbuchungslager());
								lieferscheinDto
										.setWaehrungCNr(theClientDto
												.getSMandantenwaehrung());
								lieferscheinDto
										.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
												1));
								lieferscheinDto.setTBelegdatum(new java.sql.Timestamp(date.getTime()));
								lieferscheinDto.setMandantCNr(theClientDto
										.getMandant());

								if (kundeDto.getKostenstelleIId() != null) {
									lieferscheinDto.setKostenstelleIId(kundeDto
											.getKostenstelleIId());
								} else {
									lieferscheinDto
											.setKostenstelleIId(mandantDto
													.getIIdKostenstelle());
								}

								lieferscheinDto
										.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

								lieferscheinDto.setLieferartIId(mandantDto
										.getLieferartIIdKunde());
								lieferscheinDto.setSpediteurIId(mandantDto
										.getSpediteurIIdKunde());
								lieferscheinDto.setZahlungszielIId(mandantDto
										.getZahlungszielIIdKunde());

								lieferscheinIId = getLieferscheinFac()
										.createLieferschein(lieferscheinDto,
												theClientDto);

							}

							if (!betroffeneLS.containsKey(lieferscheinIId)) {
								betroffeneLS.put(lieferscheinIId,
										lieferscheinIId);
							}

							// Position anlegen
							LieferscheinpositionDto lieferscheinposDto = new LieferscheinpositionDto();
							lieferscheinposDto
									.setLieferscheinIId(lieferscheinIId);
							lieferscheinposDto
									.setLieferscheinpositionartCNr(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);

							if (bdMenge.doubleValue() > lagerstand
									.doubleValue()) {
								lieferscheinposDto.setNMenge(lagerstand);

								BigDecimal rest = bdMenge.subtract(lagerstand)
										.multiply(bdUmrechnungsfaktor);
								String teil1 = zeile.substring(0, 49);
								String teil2 = zeile.substring(61);

								String zahl = Helper
										.rundeKaufmaennisch(rest, 3)
										.movePointRight(3).toString();

								zahl = "+"
										+ Helper.fitString2LengthAlignRight(
												zahl, 11, '0');

								zeile = teil1 + zahl + teil2;

								// FEHLER -> REST IN QUEUE-FILE LEGEN
								alQueue.add(zeile);

							} else {
								lieferscheinposDto.setNMenge(bdMenge);
							}

							lieferscheinposDto
									.setNNettoeinzelpreis(einzelpreisNetto);
							lieferscheinposDto
									.setNEinzelpreis(einzelpreisNetto);
							lieferscheinposDto
									.setNBruttoeinzelpreis(einzelpreisBrutto);

							lieferscheinposDto.setNMwstbetrag(einzelpreisBrutto
									.subtract(einzelpreisNetto));

							lieferscheinposDto.setFRabattsatz(0.0);
							lieferscheinposDto.setFZusatzrabattsatz(0.0);
							lieferscheinposDto
									.setBNettopreisuebersteuert(Helper
											.boolean2Short(false));
							lieferscheinposDto.setArtikelIId(artikelIId);
							lieferscheinposDto.setEinheitCNr(artikelDto
									.getEinheitCNr());
							session3.close();

							// Ausser der Kunde hat MWST-Satz mit 0%, dann muss
							// dieser
							// verwendet werden
							MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
									.mwstsatzFindByMwstsatzbezIIdAktuellster(
											kundeDto.getMwstsatzbezIId(),
											theClientDto);

							if (mwstsatzDtoAktuell.getFMwstsatz().doubleValue() == 0) {
								lieferscheinposDto
										.setMwstsatzIId(mwstsatzDtoAktuell
												.getIId());
							} else {
								if (artikelDto.getMwstsatzbezIId() == null) {
									protokollDto
											.setCText("Es konnte kein MWST- Satz Artikelnummer "
													+ artikelDto.getCNr()
													+ " gefunden werden. "
													+ sZeile);
									erstelleProtokollEintrag(protokollDto,
											theClientDto);
									alQueue.add(zeile);
									continue;
								} else {
									MwstsatzDto mwstsatzAktuell = getMandantFac()
											.mwstsatzFindByMwstsatzbezIIdAktuellster(
													artikelDto
															.getMwstsatzbezIId(),
													theClientDto);
									if (mwstsatzAktuell != null) {
										lieferscheinposDto
												.setMwstsatzIId(mwstsatzAktuell
														.getIId());
									} else {
										// fehler
									}
								}
							}

							try {
								getLieferscheinpositionFac()
										.createLieferscheinposition(
												lieferscheinposDto, true,
												theClientDto);
							} catch (EJBExceptionLP e) {
								if (e.getCode() == EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER) {
									protokollDto
											.setCText("'Zuwenig auf Lager' f\u00FCr  Artikelnummer "
													+ artikelDto.getCNr()
													+ " Ben\u00F6tigt: "
													+ bdMenge.doubleValue()
													+ " " + sZeile);
									erstelleProtokollEintrag(protokollDto,
											theClientDto);
									alQueue.add(zeile);
									continue;
								}
							}
							protokollDto.setCArt(SystemFac.PROTOKOLL_ART_INFO);
							protokollDto.setCText(lieferscheinposDto
									.getNMenge()
									+ " Einheiten mit der Artikelnummer "
									+ artikelDto.getCNr()
									+ " mit Lieferschein abgebucht. " + sZeile);
							erstelleProtokollEintrag(protokollDto, theClientDto);

						} else {

							if (flrKunde.getFlrpartner().getFlrpartnerklasse() != null
									&& flrKunde.getFlrpartner()
											.getFlrpartnerklasse()
											.getC_importart() != null
									&& flrKunde
											.getFlrpartner()
											.getFlrpartnerklasse()
											.getC_importart()
											.equals(PartnerFac.IMPORTART_ESEENSAUSGABE)) {
								// generieren von primary key
								PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
								Integer pk = pkGen
										.getNextPrimaryKey(PKConst.PK_KASSAIMPORT);

								Kassaimport kassaimport = new Kassaimport(pk,
										new java.sql.Timestamp(System
												.currentTimeMillis()),
										new java.sql.Timestamp(date.getTime()),
										bdMenge, Helper.rundeKaufmaennisch(
												einzelpreisNetto, 4),
										flrKunde.getI_id(), artikelIId,
										speiseplanIId);

								em.persist(kassaimport);
								em.flush();

								protokollDto
										.setCArt(SystemFac.PROTOKOLL_ART_INFO);
								protokollDto.setCText("Artikelnummer "
										+ artikelDto.getCNr() + " mit Menge "
										+ bdMenge + " importiert. " + sZeile);
								erstelleProtokollEintrag(protokollDto,
										theClientDto);
							} else {
								protokollDto
										.setCArt(SystemFac.PROTOKOLL_ART_INFO);
								protokollDto
										.setCText("Kunde mit Filialnummer "
												+ kundennummer
												+ " hat keine Partnerklasse mit Importart Shop/Essensausgabe definiert."
												+ sZeile);
								erstelleProtokollEintrag(protokollDto,
										theClientDto);
								continue;
							}

						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				} else {
					protokollDto
							.setCText("Es konnte kein Kunde zu Filialnummer "
									+ kundennummer + " gefunden werden. "
									+ sZeile);
					erstelleProtokollEintrag(protokollDto, theClientDto);
					session2.close();
					alQueue.add(zeile);
					continue;
				}
				session2.close();

			} else {
				protokollDto
						.setCText("Zu Kassennummer "
								+ kassennummer
								+ " und Datum "
								+ datum
								+ " konnte kein Speiseplan/Artikel/Umrechnung gefunden werden. "
								+ sZeile);
				erstelleProtokollEintrag(protokollDto, theClientDto);
				session.close();
				alQueue.add(zeile);
				continue;

			}

			session.close();

		}

		Iterator it = betroffeneLS.keySet().iterator();
		while (it.hasNext()) {
			try {
				Integer lieferscheinIId = (Integer) it.next();
				getLieferscheinFac().aktiviereLieferschein(lieferscheinIId,
						theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		return alQueue;
	}

	public void removeKassaartikel(KassaartikelDto kassaartikelDto) {
		if (kassaartikelDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kassaartikelDto == null"));
		}
		if (kassaartikelDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kassaartikelDto.getIId() == null"));
		}
		// try {
		Integer iId = kassaartikelDto.getIId();
		Kassaartikel toRemove = em.find(Kassaartikel.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateKassaartikel(KassaartikelDto kassaartikelDto) {
		if (kassaartikelDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kassaartikelDto == null"));
		}
		if (kassaartikelDto.getCBez() == null
				|| kassaartikelDto.getCArtikelnummerkassa() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"kassaartikelDto.getCBez() == null || kassaartikelDto.getCKassennummer() == null"));
		}

		Integer iId = kassaartikelDto.getIId();
		// try {
		Kassaartikel speisekassa = em.find(Kassaartikel.class, iId);
		if (speisekassa == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em
					.createNamedQuery("KassaartikelfindByCArtikelnummerkassa");
			query.setParameter(1, kassaartikelDto.getCArtikelnummerkassa());
			Integer iIdVorhanden = ((Kassaartikel) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"KUE_SPEISEKASSA.C_ARTIKELNUMMERKASSA"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setKassaartikelFromKassaartikelDto(speisekassa, kassaartikelDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public void updateBedienerlager(BedienerlagerDto bedienerlagerDto) {
		if (bedienerlagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("bedienerlagerDto == null"));
		}
		if (bedienerlagerDto.getIId() == null
				|| bedienerlagerDto.getCBedienernummer() == null
				|| bedienerlagerDto.getLagerIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"bedienerlagerDto.getIId() == null || bedienerlagerDto.getCBedienernummer() == null || bedienerlagerDto.getLagerIId() == null"));
		}
		Integer iId = bedienerlagerDto.getIId();
		// try {
		Bedienerlager bedienerlager = em.find(Bedienerlager.class, iId);
		if (bedienerlager == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em
					.createNamedQuery("BedienerlagerFindByCBedienernummer");
			query.setParameter(1, bedienerlagerDto.getCBedienernummer());
			Integer iIdVorhanden = ((Bedienerlager) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"KUE_BEDIENERLAGER.UK"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setBedienerlagerFromBedienerlagerDto(bedienerlager, bedienerlagerDto);

	}

	public KassaartikelDto kassaartikelFindByPrimaryKey(Integer iId) {
		// try {
		Kassaartikel speisekassa = em.find(Kassaartikel.class, iId);
		if (speisekassa == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleSpeisekassaDto(speisekassa);

	}

	public BedienerlagerDto bedienerlagerFindByPrimaryKey(Integer iId) {
		// try {
		Bedienerlager speisekassa = em.find(Bedienerlager.class, iId);
		if (speisekassa == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBedienerlagerDto(speisekassa);

	}

	private void setKassaartikelFromKassaartikelDto(Kassaartikel speisekassa,
			KassaartikelDto speisekassaDto) {
		speisekassa.setCBez(speisekassaDto.getCBez());
		speisekassa.setCArtikelnummerkassa(speisekassaDto
				.getCArtikelnummerkassa());
		em.merge(speisekassa);
		em.flush();
	}

	private KassaartikelDto assembleSpeisekassaDto(Kassaartikel speisekassa) {
		return KassaartikelDtoAssembler.createDto(speisekassa);
	}

	private BedienerlagerDto assembleBedienerlagerDto(
			Bedienerlager bedienerlager) {
		return BedienerlagerDtoAssembler.createDto(bedienerlager);
	}

	private KassaartikelDto[] assembleSpeisekassaDtos(Collection<?> speisekassas) {
		List<KassaartikelDto> list = new ArrayList<KassaartikelDto>();
		if (speisekassas != null) {
			Iterator<?> iterator = speisekassas.iterator();
			while (iterator.hasNext()) {
				Kassaartikel speisekassa = (Kassaartikel) iterator.next();
				list.add(assembleSpeisekassaDto(speisekassa));
			}
		}
		KassaartikelDto[] returnArray = new KassaartikelDto[list.size()];
		return (KassaartikelDto[]) list.toArray(returnArray);
	}

	public Integer createSpeiseplan(SpeiseplanDto speiseplanDto,
			TheClientDto theClientDto) {
		if (speiseplanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("speiseplanDto == null"));
		}
		if (speiseplanDto.getStuecklisteIId() == null
				|| speiseplanDto.getNMenge() == null
				|| speiseplanDto.getKassaartikelIId() == null
				|| speiseplanDto.getTDatum() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"speiseplanDto.getArtikelIId() == null || speiseplanDto.getNMenge() == null || speiseplanDto.getSpeisekassaIId() == null || speiseplanDto.getTDatum() == null"));
		}

		speiseplanDto.setMandantCNr(theClientDto.getMandant());

		try {
			Query query = em
					.createNamedQuery("SpeiseplanfindByMandantCNrKassaartikelIIdTDatum");
			query.setParameter(1, speiseplanDto.getMandantCNr());
			query.setParameter(2, speiseplanDto.getKassaartikelIId());
			query.setParameter(3, speiseplanDto.getTDatum());
			Speiseplan doppelt = (Speiseplan) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"KUE_SPEISEPLAN.UK"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SPEISEPLAN);
			speiseplanDto.setIId(pk);

			Speiseplan speiseplan = new Speiseplan(
					speiseplanDto.getStuecklisteIId(),
					speiseplanDto.getMandantCNr(), speiseplanDto.getTDatum(),
					speiseplanDto.getNMenge(), speiseplanDto.getIId(),
					speiseplanDto.getKassaartikelIId(),
					speiseplanDto.getFertigungsgruppeIId());
			em.persist(speiseplan);
			em.flush();
			setSpeiseplanFromSpeiseplanDto(speiseplan, speiseplanDto);

			aktualisiereUndReserviereAlleSpeiseplanpositionen(speiseplanDto,
					theClientDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return speiseplanDto.getIId();
	}

	public void removeSpeiseplan(SpeiseplanDto speiseplanDto) {
		if (speiseplanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("speiseplanDto == null"));
		}
		if (speiseplanDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("speiseplanDto.getIId() == null"));
		}

		Query query = em
				.createNamedQuery("SpeiseplanpositionfindBySpeiseplanIId");
		query.setParameter(1, speiseplanDto.getIId());
		Collection speiseplanpositions = query.getResultList();
		try {
			Iterator<?> iterator = speiseplanpositions.iterator();
			while (iterator.hasNext()) {
				Speiseplanposition speiseplanposition = (Speiseplanposition) iterator
						.next();
				// Zuerst alle Reservierungen loeschen

				ArtikelreservierungDto artikelreservierungDelDto = getReservierungFac()
						.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
								LocaleFac.BELEGART_KUECHE,
								speiseplanposition.getIId());
				if (artikelreservierungDelDto != null) {
					getReservierungFac().removeArtikelreservierung(
							artikelreservierungDelDto.getIId());
				}
				// Dann die Position loeschen
				em.remove(speiseplanposition);
			}
		} catch (RemoteException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

		Integer iId = speiseplanDto.getIId();
		Speiseplan toRemove = em.find(Speiseplan.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void updateSpeiseplan(SpeiseplanDto speiseplanDto,
			TheClientDto theClientDto) {
		if (speiseplanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fehlerDto == null"));
		}
		if (speiseplanDto.getStuecklisteIId() == null
				|| speiseplanDto.getNMenge() == null
				|| speiseplanDto.getKassaartikelIId() == null
				|| speiseplanDto.getTDatum() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"speiseplanDto.getArtikelIId() == null || speiseplanDto.getNMenge() == null || speiseplanDto.getSpeisekassaIId() == null || speiseplanDto.getTDatum() == null"));
		}

		Integer iId = speiseplanDto.getIId();
		// try {
		Speiseplan speiseplan = em.find(Speiseplan.class, iId);

		aktualisiereUndReserviereAlleSpeiseplanpositionen(speiseplanDto,
				theClientDto);
		if (speiseplan == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em
					.createNamedQuery("SpeiseplanfindByMandantCNrKassaartikelIIdTDatum");
			query.setParameter(1, speiseplanDto.getMandantCNr());
			query.setParameter(2, speiseplanDto.getKassaartikelIId());
			query.setParameter(3, speiseplanDto.getTDatum());
			Integer iIdVorhanden = ((Speiseplan) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_FEHLER.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setSpeiseplanFromSpeiseplanDto(speiseplan, speiseplanDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public SpeiseplanDto speiseplanFindByPrimaryKey(Integer iId) {
		// try {
		Speiseplan speiseplan = em.find(Speiseplan.class, iId);
		if (speiseplan == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleSpeiseplanDto(speiseplan);

	}

	public SpeiseplanpositionDto speiseplanpositionFindByPrimaryKey(Integer iId) {
		// try {
		Speiseplanposition speiseplanposition = em.find(
				Speiseplanposition.class, iId);
		if (speiseplanposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleSpeiseplanpositionDto(speiseplanposition);

	}

	private void aktualisiereUndReserviereAlleSpeiseplanpositionen(
			SpeiseplanDto speiseplanDto, TheClientDto theClientDto) {

		Query query = em
				.createNamedQuery("SpeiseplanpositionfindBySpeiseplanIId");
		query.setParameter(1, speiseplanDto.getIId());
		Collection speiseplanpositions = query.getResultList();
		try {
			Iterator<?> iterator = speiseplanpositions.iterator();
			while (iterator.hasNext()) {
				Speiseplanposition speiseplanposition = (Speiseplanposition) iterator
						.next();
				// Zuerst alle Reservierungen loeschen

				ArtikelreservierungDto artikelreservierungDelDto = getReservierungFac()
						.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
								LocaleFac.BELEGART_KUECHE,
								speiseplanposition.getIId());
				if (artikelreservierungDelDto != null) {
					getReservierungFac().removeArtikelreservierung(
							artikelreservierungDelDto.getIId());
				}
				// Dann die Position loeschen
				em.remove(speiseplanposition);
			}

			// Stueckliste aufloesen
			ArrayList<?> stuecklisteAufegloest = getStuecklisteFac()
					.getStrukturDatenEinerStueckliste(
							speiseplanDto.getStuecklisteIId(),
							theClientDto,
							StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR,
							0, null, true, true, speiseplanDto.getNMenge(),
							null, true);
			PKGeneratorObj pkGen = new PKGeneratorObj();
			for (int j = 0; j < stuecklisteAufegloest.size(); j++) {
				StuecklisteMitStrukturDto strukt = (StuecklisteMitStrukturDto) stuecklisteAufegloest
						.get(j);
				if (strukt.getStuecklistepositionDto() != null) {
					if (strukt.getStuecklistepositionDto().getArtikelIId() != null
							&& strukt.getStuecklistepositionDto()
									.getNZielmenge() != null) {

						BigDecimal menge = strukt.getStuecklistepositionDto()
								.getNZielmenge()
								.multiply(speiseplanDto.getNMenge());

						if (menge.doubleValue() > 0) {

							Integer pk = pkGen
									.getNextPrimaryKey(PKConst.PK_SPEISEPLANPOSITION);

							Speiseplanposition speisekassa = new Speiseplanposition(
									pk, speiseplanDto.getIId(), strukt
											.getStuecklistepositionDto()
											.getArtikelIId(), menge);
							em.persist(speisekassa);
							em.flush();

							ArtikelreservierungDto artikelreservierungDto = new ArtikelreservierungDto();
							artikelreservierungDto.setArtikelIId(strukt
									.getStuecklistepositionDto()
									.getArtikelIId());
							artikelreservierungDto
									.setCBelegartnr(LocaleFac.BELEGART_KUECHE);
							artikelreservierungDto.setNMenge(menge);
							artikelreservierungDto.setIBelegartpositionid(pk);
							artikelreservierungDto
									.setTLiefertermin(speiseplanDto.getTDatum());
							getReservierungFac().createArtikelreservierung(
									artikelreservierungDto);
						}
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	private void setSpeiseplanFromSpeiseplanDto(Speiseplan speiseplan,
			SpeiseplanDto speiseplanDto) {
		speiseplan.setStuecklisteIId(speiseplanDto.getStuecklisteIId());
		speiseplan.setMandantCNr(speiseplanDto.getMandantCNr());
		speiseplan.setNMenge(speiseplanDto.getNMenge());
		speiseplan.setKassaartikelIId(speiseplanDto.getKassaartikelIId());
		speiseplan.setTDatum(speiseplanDto.getTDatum());
		speiseplan.setFertigungsgruppeIId(speiseplanDto
				.getFertigungsgruppeIId());
		em.merge(speiseplan);
		em.flush();
	}

	private SpeiseplanDto assembleSpeiseplanDto(Speiseplan speiseplan) {
		return SpeiseplanDtoAssembler.createDto(speiseplan);
	}

	private SpeiseplanpositionDto assembleSpeiseplanpositionDto(
			Speiseplanposition speiseplanposition) {
		return SpeiseplanpositionDtoAssembler.createDto(speiseplanposition);
	}

	private SpeiseplanDto[] assembleSpeiseplanDtos(Collection<?> speiseplans) {
		List<SpeiseplanDto> list = new ArrayList<SpeiseplanDto>();
		if (speiseplans != null) {
			Iterator<?> iterator = speiseplans.iterator();
			while (iterator.hasNext()) {
				Speiseplan speiseplan = (Speiseplan) iterator.next();
				list.add(assembleSpeiseplanDto(speiseplan));
			}
		}
		SpeiseplanDto[] returnArray = new SpeiseplanDto[list.size()];
		return (SpeiseplanDto[]) list.toArray(returnArray);
	}

	@TransactionTimeout(1000)
	public int stiftdatenImportieren(Kdc100logDto[] kdc100logDtoI,
			Integer lagerIId_Ziel, TheClientDto theClientDto) {

		int iFehler = 0;

		int iStandarddurchlaufzeit = 0;
		MontageartDto montageartDto = null;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
			iStandarddurchlaufzeit = ((Integer) parameter.getCWertAsObject())
					.intValue();

			montageartDto = getStuecklisteFac().montageartFindByMandantCNr(
					theClientDto)[0];

		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		ArrayList alBereinigt = new ArrayList();

		for (int i = 0; i < kdc100logDtoI.length; i++) {
			// EinkaufsEAN suchen
			if (kdc100logDtoI[i].getCBarcode().equals("$STORNO")) {
				if (alBereinigt.size() > 0) {
					alBereinigt.remove(alBereinigt.size() - 1);
				}
				continue;
			}

			alBereinigt.add(kdc100logDtoI[i]);
		}

		for (int i = 0; i < alBereinigt.size(); i++) {

			String sMeldung = "";

			Kdc100logDto kdc100logDto = (Kdc100logDto) alBereinigt.get(i);
			kdc100logDto.setCArt(SystemFac.PROTOKOLL_ART_INFO);

			try {
				// EinkaufsEAN suchen
				Query query = em.createNamedQuery("EinkaufseanfindByCEan");
				query.setParameter(1, kdc100logDto.getCBarcode());

				Einkaufsean einkaufsean = (Einkaufsean) query.getSingleResult();

				// Artikel holen
				try {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									einkaufsean.getArtikelIId(), theClientDto);

					if (Helper.short2boolean(artikelDto.getBChargennrtragend())
							|| Helper.short2boolean(artikelDto
									.getBSeriennrtragend())) {
						sMeldung += "Artikel "
								+ artikelDto.getCNr()
								+ " ist SNR/CHNR behaftet und kann daher nicht gebucht werden, ";
						kdc100logDto.setCArt(SystemFac.PROTOKOLL_ART_FEHLER);
						iFehler++;
					} else {

						// Lagerstand holen
						BigDecimal ls = getLagerFac().getLagerstand(
								einkaufsean.getArtikelIId(),
								kdc100logDto.getLagerIId(), theClientDto);

						BigDecimal bdMengeAbzubuchen = null;

						if (ls.doubleValue() < einkaufsean.getNMenge()
								.doubleValue()) {
							bdMengeAbzubuchen = ls;
							sMeldung += "Es konnten nur  "
									+ bdMengeAbzubuchen.doubleValue()
									+ " anstatt "
									+ einkaufsean.getNMenge().doubleValue()
									+ " abgebucht werde, da nicht mehr auf Lager war";
							kdc100logDto
									.setCArt(SystemFac.PROTOKOLL_ART_FEHLER);
							iFehler++;
						} else {
							bdMengeAbzubuchen = einkaufsean.getNMenge();
						}

						if (lagerIId_Ziel == null) {
							// Material aufs Tageslos buchen
							Integer losIId = getFertigungFac()
									.holeTageslos(kdc100logDto.getLagerIId(),
											artikelDto.getMandantCNr(),
											iStandarddurchlaufzeit, false,
											theClientDto);

							if (losIId != null) {

								LossollmaterialDto lossollmaterialDto = new LossollmaterialDto();
								lossollmaterialDto.setLosIId(losIId);
								lossollmaterialDto
										.setNSollpreis(getLagerFac()
												.getGemittelterGestehungspreisEinesLagers(
														artikelDto.getIId(),
														kdc100logDto
																.getLagerIId(),
														theClientDto));
								lossollmaterialDto.setNMenge(new BigDecimal(0));
								lossollmaterialDto
										.setMontageartIId(montageartDto
												.getIId());
								lossollmaterialDto.setEinheitCNr(artikelDto
										.getEinheitCNr());
								lossollmaterialDto.setArtikelIId(artikelDto
										.getIId());

								LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
								losistmaterialDto.setLagerIId(kdc100logDto
										.getLagerIId());
								losistmaterialDto.setBAbgang(Helper
										.boolean2Short(true));

								losistmaterialDto.setNMenge(bdMengeAbzubuchen);

								getFertigungFac().gebeMaterialNachtraeglichAus(
										lossollmaterialDto, losistmaterialDto,
										null, true, theClientDto);

								sMeldung += artikelDto.getCNr() + " mit Menge "
										+ bdMengeAbzubuchen + " abgebucht";

							} else {
								sMeldung += "Es konnte keine Tageslosdefinition zu Lager-ID "
										+ kdc100logDto.getLagerIId()
										+ " gefunden werden";
								kdc100logDto
										.setCArt(SystemFac.PROTOKOLL_ART_FEHLER);
								iFehler++;
							}
						} else {
							getLagerFac()
									.bucheUm(
											artikelDto.getIId(),
											kdc100logDto.getLagerIId(),
											artikelDto.getIId(),
											lagerIId_Ziel,
											bdMengeAbzubuchen,
											null,
											"Umbuchung KDC100",
											getLagerFac()
													.getGemittelterGestehungspreisEinesLagers(
															artikelDto.getIId(),
															kdc100logDto
																	.getLagerIId(),
															theClientDto),
											theClientDto);
						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			} catch (javax.persistence.NoResultException e1) {
				// Wenn kein Eintrag gefunden wurde, Eintrag ins Log
				sMeldung += "EAN-Code '" + kdc100logDto.getCBarcode()
						+ "' nicht gefunden, ";
				kdc100logDto.setCArt(SystemFac.PROTOKOLL_ART_FEHLER);
				iFehler++;
			}

			// Log-Eintrag erstellen

			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KDC100LOG);
			kdc100logDto.setIId(pk);

			if (sMeldung != null && sMeldung.length() > 300) {
				sMeldung = sMeldung.substring(0, 299);
			}

			Kdc100log kdc100log = new Kdc100log(kdc100logDto.getIId(),
					kdc100logDto.getCSeriennummer(), kdc100logDto.getCArt(),
					kdc100logDto.getCBarcode(), sMeldung,
					new java.sql.Timestamp(System.currentTimeMillis()),
					kdc100logDto.getTStiftzeit());
			em.persist(kdc100log);
			em.flush();

		}
		return iFehler;
	}

	public Integer createTageslos(TageslosDto tageslosDto) {
		if (tageslosDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("tageslosDto == null"));
		}
		if (tageslosDto.getBSofortverbrauch() == null
				|| tageslosDto.getCBez() == null
				|| tageslosDto.getKostenstelleIId() == null
				|| tageslosDto.getLagerIIdAbbuchung() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"tageslosDto.getBSofortverbrauch() == null || tageslosDto.getCBez() == null || tageslosDto.getKostenstelleIId() == null || tageslosDto.getLagerIIdAbbuchung() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("TageslosfindByLagerIIdBSofortverbrauch");
			query.setParameter(1, tageslosDto.getLagerIIdAbbuchung());
			query.setParameter(2, tageslosDto.getBSofortverbrauch());
			Tageslos doppelt = (Tageslos) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"KUE_TAGESLOS.UK"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_TAGESLOS);
			tageslosDto.setIId(pk);

			Tageslos schwere = new Tageslos(tageslosDto.getLagerIIdAbbuchung(),
					tageslosDto.getCBez(), tageslosDto.getIId(),
					tageslosDto.getBSofortverbrauch(),
					tageslosDto.getKostenstelleIId());
			em.persist(schwere);
			em.flush();
			setTageslosFromTageslosDto(schwere, tageslosDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return tageslosDto.getIId();

	}

	public void removeTageslos(TageslosDto tageslosDto) {
		if (tageslosDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("tageslosDto == null"));
		}
		if (tageslosDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("tageslosDto.getIId() == null"));
		}

		Integer iId = tageslosDto.getIId();
		Tageslos toRemove = em.find(Tageslos.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateTageslos(TageslosDto tageslosDto) {
		if (tageslosDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("tageslosDto == null"));
		}
		if (tageslosDto.getBSofortverbrauch() == null
				|| tageslosDto.getCBez() == null
				|| tageslosDto.getKostenstelleIId() == null
				|| tageslosDto.getLagerIIdAbbuchung() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"tageslosDto.getBSofortverbrauch() == null || tageslosDto.getCBez() == null || tageslosDto.getKostenstelleIId() == null || tageslosDto.getLagerIIdAbbuchung() == null"));
		}

		Integer iId = tageslosDto.getIId();
		// try {
		Tageslos tageslos = em.find(Tageslos.class, iId);
		if (tageslos == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em
					.createNamedQuery("TageslosfindByLagerIIdBSofortverbrauch");
			query.setParameter(1, tageslosDto.getLagerIIdAbbuchung());
			query.setParameter(2, tageslosDto.getBSofortverbrauch());
			Integer iIdVorhanden = ((Tageslos) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"KUE_TAGESLOS.UK"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setTageslosFromTageslosDto(tageslos, tageslosDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public TageslosDto tageslosFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Tageslos tageslos = em.find(Tageslos.class, iId);
		if (tageslos == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleTageslosDto(tageslos);

	}

	public TageslosDto tageslosFindByLagerIIdBSofortverbrauch(
			Integer lagerIIdAbbuchung, Short bSofortverbrauch) {

		Query query = em
				.createNamedQuery("TageslosfindByLagerIIdBSofortverbrauch");
		query.setParameter(1, lagerIIdAbbuchung);
		query.setParameter(2, bSofortverbrauch);
		Tageslos tageslos;
		try {
			tageslos = (Tageslos) query.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
		return assembleTageslosDto(tageslos);

	}

	private void setTageslosFromTageslosDto(Tageslos tageslos,
			TageslosDto tageslosDto) {
		tageslos.setBSofortverbrauch(tageslosDto.getBSofortverbrauch());
		tageslos.setCBez(tageslosDto.getCBez());
		tageslos.setKostenstelleIId(tageslosDto.getKostenstelleIId());
		tageslos.setLagerIIdAbbuchung(tageslosDto.getLagerIIdAbbuchung());
		em.merge(tageslos);
		em.flush();
	}

	private TageslosDto assembleTageslosDto(Tageslos tageslos) {
		return TageslosDtoAssembler.createDto(tageslos);
	}

	private TageslosDto[] assembleTageslosDtos(Collection<?> tagesloss) {
		List<TageslosDto> list = new ArrayList<TageslosDto>();
		if (tagesloss != null) {
			Iterator<?> iterator = tagesloss.iterator();
			while (iterator.hasNext()) {
				Tageslos tageslos = (Tageslos) iterator.next();
				list.add(assembleTageslosDto(tageslos));
			}
		}
		TageslosDto[] returnArray = new TageslosDto[list.size()];
		return (TageslosDto[]) list.toArray(returnArray);
	}

	public Integer createKuecheumrechnung(
			KuecheumrechnungDto kuecheumrechnungDto) {
		if (kuecheumrechnungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kuecheumrechnungDto == null"));
		}
		if (kuecheumrechnungDto.getArtikelIId() == null
				|| kuecheumrechnungDto.getCArtikelnummerkassa() == null
				|| kuecheumrechnungDto.getNFaktor() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"kuecheumrechnungDto.getArtikelIId() == null || kuecheumrechnungDto.getCKassaartikel() == null || kuecheumrechnungDto.getNFaktor() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("KuecheumrechnungfindByCArtikelnummerkassa");
			query.setParameter(1, kuecheumrechnungDto.getCArtikelnummerkassa());
			Kuecheumrechnung doppelt = (Kuecheumrechnung) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"KUE_KUECHUMRECHNUNG.C_KASSAARTIKEL"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KUECHEUMRECHNUNG);
			kuecheumrechnungDto.setIId(pk);

			Kuecheumrechnung kuecheumrechnung = new Kuecheumrechnung(
					kuecheumrechnungDto.getIId(),
					kuecheumrechnungDto.getArtikelIId(),
					kuecheumrechnungDto.getCArtikelnummerkassa(),
					kuecheumrechnungDto.getNFaktor());
			em.persist(kuecheumrechnung);
			em.flush();
			setKuecheumrechnungFromKuecheumrechnungDto(kuecheumrechnung,
					kuecheumrechnungDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return kuecheumrechnungDto.getIId();

	}

	public Integer createBedienerlager(BedienerlagerDto bedienerlagerDto) {
		if (bedienerlagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("bedienerlagerDto == null"));
		}
		if (bedienerlagerDto.getCBedienernummer() == null
				|| bedienerlagerDto.getLagerIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"bedienerlagerDto.getCBedienernummer() == null || bedienerlagerDto.getLagerIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("BedienerlagerFindByCBedienernummer");
			query.setParameter(1, bedienerlagerDto.getCBedienernummer());
			Bedienerlager doppelt = (Bedienerlager) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"KUE_BEDIENERLAGER.UK"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BEDIENERLAGER);
			bedienerlagerDto.setIId(pk);

			Bedienerlager kuecheumrechnung = new Bedienerlager(
					bedienerlagerDto.getIId(),
					bedienerlagerDto.getCBedienernummer(),
					bedienerlagerDto.getLagerIId());
			em.persist(kuecheumrechnung);
			em.flush();
			setBedienerlagerFromBedienerlagerDto(kuecheumrechnung,
					bedienerlagerDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return bedienerlagerDto.getIId();

	}

	public void removeBedienerlager(BedienerlagerDto bedienerlagerDto) {
		if (bedienerlagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("bedienerlagerDto == null"));
		}
		if (bedienerlagerDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("bedienerlagerDto.getIId() == null"));
		}

		Integer iId = bedienerlagerDto.getIId();
		Bedienerlager toRemove = em.find(Bedienerlager.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeKuecheumrechnung(KuecheumrechnungDto kuecheumrechnungDto) {
		if (kuecheumrechnungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kuecheumrechnungDto == null"));
		}
		if (kuecheumrechnungDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("kuecheumrechnungDto.getIId() == null"));
		}

		Integer iId = kuecheumrechnungDto.getIId();
		Kuecheumrechnung toRemove = em.find(Kuecheumrechnung.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateKuecheumrechnung(KuecheumrechnungDto kuecheumrechnungDto) {
		if (kuecheumrechnungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("kuecheumrechnungDto == null"));
		}
		if (kuecheumrechnungDto.getArtikelIId() == null
				|| kuecheumrechnungDto.getCArtikelnummerkassa() == null
				|| kuecheumrechnungDto.getIId() == null
				|| kuecheumrechnungDto.getNFaktor() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"kuecheumrechnungDto.getArtikelIId() == null || kuecheumrechnungDto.getCArtikelnummerkassa() == null || kuecheumrechnungDto.getIId() == null || kuecheumrechnungDto.getNFaktor() == null"));
		}

		Integer iId = kuecheumrechnungDto.getIId();
		// try {
		Kuecheumrechnung kuecheumrechnung = em
				.find(Kuecheumrechnung.class, iId);
		if (kuecheumrechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em
					.createNamedQuery("KuecheumrechnungfindByCArtikelnummerkassa");
			query.setParameter(1, kuecheumrechnungDto.getCArtikelnummerkassa());
			Integer iIdVorhanden = ((Kuecheumrechnung) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"KUE_KUECHUMRECHNUNG.C_KASSAARTIKEL"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setKuecheumrechnungFromKuecheumrechnungDto(kuecheumrechnung,
				kuecheumrechnungDto);

	}

	public KuecheumrechnungDto kuecheumrechnungFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Kuecheumrechnung kuecheumrechnung = em
				.find(Kuecheumrechnung.class, iId);
		if (kuecheumrechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKuecheumrechnungDto(kuecheumrechnung);

	}

	private void setKuecheumrechnungFromKuecheumrechnungDto(
			Kuecheumrechnung kuecheumrechnung,
			KuecheumrechnungDto kuecheumrechnungDto) {
		kuecheumrechnung.setArtikelIId(kuecheumrechnungDto.getArtikelIId());
		kuecheumrechnung.setCArtikelnummerkassa(kuecheumrechnungDto
				.getCArtikelnummerkassa());
		kuecheumrechnung.setNFaktor(kuecheumrechnungDto.getNFaktor());
		em.merge(kuecheumrechnung);
		em.flush();
	}

	private void setBedienerlagerFromBedienerlagerDto(
			Bedienerlager kuecheumrechnung, BedienerlagerDto kuecheumrechnungDto) {
		kuecheumrechnung.setLagerIId(kuecheumrechnungDto.getLagerIId());
		kuecheumrechnung.setCBedienernummer(kuecheumrechnungDto
				.getCBedienernummer());
		em.merge(kuecheumrechnung);
		em.flush();
	}

	private KuecheumrechnungDto assembleKuecheumrechnungDto(
			Kuecheumrechnung kuecheumrechnung) {
		return KuecheumrechnungDtoAssembler.createDto(kuecheumrechnung);
	}

	private KuecheumrechnungDto[] assembleKuecheumrechnungDtos(
			Collection<?> kuecheumrechnungdtos) {
		List<KuecheumrechnungDto> list = new ArrayList<KuecheumrechnungDto>();
		if (kuecheumrechnungdtos != null) {
			Iterator<?> iterator = kuecheumrechnungdtos.iterator();
			while (iterator.hasNext()) {
				Kuecheumrechnung kuecheumrechnung = (Kuecheumrechnung) iterator
						.next();
				list.add(assembleKuecheumrechnungDto(kuecheumrechnung));
			}
		}
		KuecheumrechnungDto[] returnArray = new KuecheumrechnungDto[list.size()];
		return (KuecheumrechnungDto[]) list.toArray(returnArray);
	}

}
