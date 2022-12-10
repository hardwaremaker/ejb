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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import com.lp.server.artikel.ejb.Artikelkommentar;
import com.lp.server.artikel.ejb.Artikelkommentarart;
import com.lp.server.artikel.ejb.Artikelkommentarartspr;
import com.lp.server.artikel.ejb.ArtikelkommentarartsprPK;
import com.lp.server.artikel.ejb.Artikelkommentardruck;
import com.lp.server.artikel.ejb.ArtikelkommentardruckQuery;
import com.lp.server.artikel.ejb.Artikelkommentarspr;
import com.lp.server.artikel.ejb.ArtikelkommentarsprPK;
import com.lp.server.artikel.ejb.Dateiverweis;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarDtoAssembler;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikelkommentarartDtoAssembler;
import com.lp.server.artikel.service.ArtikelkommentarartsprDto;
import com.lp.server.artikel.service.ArtikelkommentarartsprDtoAssembler;
import com.lp.server.artikel.service.ArtikelkommentardruckDto;
import com.lp.server.artikel.service.ArtikelkommentardruckDtoAssembler;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ArtikelkommentarFacBean extends Facade implements ArtikelkommentarFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createArtikelkommentarart(ArtikelkommentarartDto artikelkommentarartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelkommentarartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentarartDto == null"));
		}
		if (artikelkommentarartDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelkommentarartDto.getCNr() == null"));
		}
		try {
			Query query = em.createNamedQuery("ArtikelkommentarartfindByCNr");
			query.setParameter(1, artikelkommentarartDto.getCNr());
			Artikelkommentarart doppelt = (Artikelkommentarart) query.getSingleResult();
			// if (doppelt != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTIKELKOMMENTAR.CNR"));
		} catch (NoResultException ex) {
			//
		}

		// Es werden nun mehr als eine Artikelkommentarart im Webshop
		// unterstuetzt
		// try {
		// if (Helper.short2boolean(artikelkommentarartDto.getBWebshop())) {
		// Query query1 = em
		// .createNamedQuery("ArtikelkommentarartfindByBWebshop");
		//
		// Artikelkommentarart doppelt = (Artikelkommentarart) query1
		// .getSingleResult();
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
		// "WW_ARTIKELKOMMENTAR.B_WEBSHOP"));
		// }
		//
		// } catch (NoResultException ex) {
		// // nothing here
		// }

		if (artikelkommentarartDto.getBTooltip() == null) {
			artikelkommentarartDto.setBTooltip(Helper.boolean2Short(false));
		}

		if (artikelkommentarartDto.getBDetail() == null) {
			artikelkommentarartDto.setBDetail(Helper.boolean2Short(false));
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELKOMMENTARART);
			artikelkommentarartDto.setIId(pk);

			Artikelkommentarart artikelkommentarart = new Artikelkommentarart(artikelkommentarartDto.getIId(),
					artikelkommentarartDto.getCNr(), artikelkommentarartDto.getBWebshop(),
					artikelkommentarartDto.getBTooltip(), artikelkommentarartDto.getBDetail());
			em.persist(artikelkommentarart);
			em.flush();
			setArtikelkommentarartFromArtikelkommentarartDto(artikelkommentarart, artikelkommentarartDto);
			if (artikelkommentarartDto.getArtikelkommentartartsprDto() != null) {
				Artikelkommentarartspr artikelkommentarartspr = new Artikelkommentarartspr(
						artikelkommentarartDto.getIId(), theClientDto.getLocUiAsString());
				em.persist(artikelkommentarartspr);
				em.flush();
				setArtikelkommentarartsprFromArtikelkommentarartsprDto(artikelkommentarartspr,
						artikelkommentarartDto.getArtikelkommentartartsprDto());
			}
			return artikelkommentarartDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeArtikelkommentarart(ArtikelkommentarartDto artikelkommentarartDto) throws EJBExceptionLP {
		myLogger.entry();
		if (artikelkommentarartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentarartDto == null"));
		}
		if (artikelkommentarartDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelkommentarartDto.getIId() == null"));
		}
		// try {
		try {
			Query query = em.createNamedQuery("ArtikelkommentarartsprfindByArtikelkommentarartIId");
			query.setParameter(1, artikelkommentarartDto.getIId());
			Collection<?> allArtklaspr = query.getResultList();
			Iterator<?> iter = allArtklaspr.iterator();
			while (iter.hasNext()) {
				Artikelkommentarartspr artklasprTemp = (Artikelkommentarartspr) iter.next();
				em.remove(artklasprTemp);
			}
			Artikelkommentarart artikelkommentarart = em.find(Artikelkommentarart.class,
					artikelkommentarartDto.getIId());
			if (artikelkommentarart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"FEhler bei removeArtikelkommentarart. Es gibt keine iid " + artikelkommentarartDto.getIId()
								+ "\ndto.toString(): " + artikelkommentarartDto.toString());
			}
			em.remove(artikelkommentarart);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }
	}

	public void updateArtikelkommentarart(ArtikelkommentarartDto artikelkommentarartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelkommentarartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentarartDto == null"));
		}
		if (artikelkommentarartDto.getIId() == null || artikelkommentarartDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"artikelkommentarartDto.getIId() == null || artikelkommentarartDto.getCNr() == null"));
		}
		Integer iId = artikelkommentarartDto.getIId();

		Artikelkommentarart artikelkommentarart = null;
		// try {
		artikelkommentarart = em.find(Artikelkommentarart.class, iId);
		if (artikelkommentarart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikelkommentarart. Es gibt keine Kommentarart mit der iid " + iId
							+ "\ndto.toString: " + artikelkommentarartDto.toString());

		}
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }
		try {
			Query query = em.createNamedQuery("ArtikelkommentarartfindByCNr");
			query.setParameter(1, artikelkommentarartDto.getCNr());
			Integer iIdVorhanden = ((Artikelkommentarart) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_ARTIKELKOMMENTARART.UC"));
			}
		} catch (NoResultException ex) {
			//
		}

		try {

			if (Helper.short2boolean(artikelkommentarartDto.getBWebshop())) {
				Query query1 = em.createNamedQuery("ArtikelkommentarartfindByBWebshop");

				Artikelkommentarart doppelt = (Artikelkommentarart) query1.getSingleResult();
				if (iId.equals(doppelt.getIId()) == false) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_ARTIKELKOMMENTAR.B_WEBSHOP"));
				}
			}

		} catch (NoResultException ex) {
			// nothing here
		}

		try {
			setArtikelkommentarartFromArtikelkommentarartDto(artikelkommentarart, artikelkommentarartDto);

			if (artikelkommentarartDto.getArtikelkommentartartsprDto() != null) {
				// try {
				Artikelkommentarartspr artklaspr = em.find(Artikelkommentarartspr.class,
						new ArtikelkommentarartsprPK(iId, theClientDto.getLocUiAsString()));
				if (artklaspr == null) {
					artklaspr = new Artikelkommentarartspr(iId, theClientDto.getLocUiAsString());
					em.persist(artklaspr);
					em.flush();
					setArtikelkommentarartsprFromArtikelkommentarartsprDto(artklaspr,
							artikelkommentarartDto.getArtikelkommentartartsprDto());
				}
				setArtikelkommentarartsprFromArtikelkommentarartsprDto(artklaspr,
						artikelkommentarartDto.getArtikelkommentartartsprDto());
				// }
				// catch (FinderException ex) {
				// Artikelkommentarartspr artklaspr = new
				// Artikelkommentarartspr(
				// iId,getTheClient(cNrUserI).getLocUiAsString());
				// em.persist(artikelkommentarartspr);
				// setArtikelkommentarartsprFromArtikelkommentarartsprDto(
				// artklaspr,
				// artikelkommentarartDto.getArtikelkommentartartsprDto());
				// }
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public String getArtikelkommentarFuerDetail(Integer artikelIId, TheClientDto theClientDto) {

		org.hibernate.Session sessionVkPreisBasis = FLRSessionFactory.getFactory().openSession();
		String kommentare = "SELECT ko.x_kommentar, ko.artikelkommentar.flrartikelkommentarart.c_nr FROM FLRArtikelkommentarspr AS ko WHERE ko.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' AND ko.x_kommentar IS NOT NULL AND ko.artikelkommentar.datenformat_c_nr <> '"
				+ MediaFac.DATENFORMAT_MIMETYPE_APP_PDF + "' AND ko.artikelkommentar.artikel_i_id=" + artikelIId
				+ " AND ko.artikelkommentar.flrartikelkommentarart.b_detail=1 ORDER BY ko.artikelkommentar.i_sort ";

		org.hibernate.Query queryKommentare = sessionVkPreisBasis.createQuery(kommentare);

		List resultListKommentare = queryKommentare.list();

		Iterator resultListIteratorKommentare = resultListKommentare.iterator();

		String kommentarVorhanden = null;

		while (resultListIteratorKommentare.hasNext()) {
			Object[] o = (Object[]) resultListIteratorKommentare.next();

			if (o[1] != null && ((String) o[0]).length() > 0) {
				String kommentar = "<style isBold=\"true\" isUnderline=\"true\"  >" + (String) o[1] + ":</style>\n"
						+ (String) o[0];

				if (kommentarVorhanden == null) {
					kommentarVorhanden = kommentar;
				} else {
					kommentarVorhanden = kommentarVorhanden + "\n\n" + kommentar;
				}

			}

		}

		return kommentarVorhanden;
	}

	public ArtikelkommentarartDto artikelkommentarartFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Artikelkommentarart artikelkommentarart = em.find(Artikelkommentarart.class, iId);
		if (artikelkommentarart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelkommentarartFindByPrimaryKey. Es gibt keine iid " + iId);
		}

		ArtikelkommentarartDto artikelkommentarartDto = assembleArtikelkommentarartDto(artikelkommentarart);
		ArtikelkommentarartsprDto artikelkommentarartsprDto = null;
		// try {
		Artikelkommentarartspr artikelkommentarartspr = em.find(Artikelkommentarartspr.class,
				new ArtikelkommentarartsprPK(iId, theClientDto.getLocUiAsString()));
		if (artikelkommentarartspr == null) {
			// nothing here
		} else {
			artikelkommentarartsprDto = assembleArtikelkommentarartsprDto(artikelkommentarartspr);
		}
		// }
		// catch (FinderException ex) {
		// nothing here
		// }
		if (artikelkommentarartsprDto == null) {
			// try {
			Artikelkommentarartspr temp = em.find(Artikelkommentarartspr.class,
					new ArtikelkommentarartsprPK(iId, theClientDto.getLocKonzernAsString()));
			if (temp == null) {
				// nothing here
			} else {
				artikelkommentarartsprDto = assembleArtikelkommentarartsprDto(temp);
			}
			// }
			// catch (FinderException ex) {
			// nothing here
			// }
		}
		artikelkommentarartDto.setArtikelkommentartartsprDto(artikelkommentarartsprDto);
		return artikelkommentarartDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setArtikelkommentarartFromArtikelkommentarartDto(Artikelkommentarart artikelkommentarart,
			ArtikelkommentarartDto artikelkommentarartDto) {
		artikelkommentarart.setCNr(artikelkommentarartDto.getCNr());
		artikelkommentarart.setBrancheId(artikelkommentarartDto.getBrancheId());
		artikelkommentarart.setBWebshop(artikelkommentarartDto.getBWebshop());
		artikelkommentarart.setBTooltip(artikelkommentarartDto.getBTooltip());
		artikelkommentarart.setBDetail(artikelkommentarartDto.getBDetail());
		em.merge(artikelkommentarart);
		em.flush();
	}

	private ArtikelkommentarartDto assembleArtikelkommentarartDto(Artikelkommentarart artikelkommentarart) {
		return ArtikelkommentarartDtoAssembler.createDto(artikelkommentarart);
	}

	private ArtikelkommentarartDto[] assembleArtikelkommentarartDtos(Collection<?> artikelkommentararts) {
		List<ArtikelkommentarartDto> list = new ArrayList<ArtikelkommentarartDto>();
		if (artikelkommentararts != null) {
			Iterator<?> iterator = artikelkommentararts.iterator();
			while (iterator.hasNext()) {
				Artikelkommentarart artikelkommentarart = (Artikelkommentarart) iterator.next();
				list.add(assembleArtikelkommentarartDto(artikelkommentarart));
			}
		}
		ArtikelkommentarartDto[] returnArray = new ArtikelkommentarartDto[list.size()];
		return (ArtikelkommentarartDto[]) list.toArray(returnArray);
	}

	private void setArtikelkommentarartsprFromArtikelkommentarartsprDto(Artikelkommentarartspr artikelkommentarartspr,
			ArtikelkommentarartsprDto artikelkommentarartsprDto) {
		artikelkommentarartspr.setCBez(artikelkommentarartsprDto.getCBez());
		em.merge(artikelkommentarartspr);
		em.flush();
	}

	private ArtikelkommentarartsprDto assembleArtikelkommentarartsprDto(Artikelkommentarartspr artikelkommentarartspr) {
		return ArtikelkommentarartsprDtoAssembler.createDto(artikelkommentarartspr);
	}

	private ArtikelkommentarartsprDto[] assembleArtikelkommentarartsprDtos(Collection<?> artikelkommentarartsprs) {
		List<ArtikelkommentarartsprDto> list = new ArrayList<ArtikelkommentarartsprDto>();
		if (artikelkommentarartsprs != null) {
			Iterator<?> iterator = artikelkommentarartsprs.iterator();
			while (iterator.hasNext()) {
				Artikelkommentarartspr artikelkommentarartspr = (Artikelkommentarartspr) iterator.next();
				list.add(assembleArtikelkommentarartsprDto(artikelkommentarartspr));
			}
		}
		ArtikelkommentarartsprDto[] returnArray = new ArtikelkommentarartsprDto[list.size()];
		return (ArtikelkommentarartsprDto[]) list.toArray(returnArray);
	}

	public byte[] getArtikelkommentarBild(String artikelCNr, String artikelkommentarartCNr, String localeCNr,
			String mandantCNr) {
		try {

			ArtikelDto aDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(artikelCNr, mandantCNr);

			if (aDto != null && aDto.getIId() != null) {

				try {
					Query query = em.createNamedQuery("ArtikelkommentarartfindByCNr");
					query.setParameter(1, artikelkommentarartCNr);
					Artikelkommentarart art = (Artikelkommentarart) query.getSingleResult();

					Query query2 = em.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIId");
					query2.setParameter(1, aDto.getIId());
					query2.setParameter(2, art.getIId());

					Collection c = query2.getResultList();

					Iterator it = c.iterator();
					while (it.hasNext()) {

						Artikelkommentar artikelkommentar = (Artikelkommentar) it.next();

						if (artikelkommentar.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
								|| artikelkommentar.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
								|| artikelkommentar.getDatenformatCNr()
										.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {

							try {

								Query query3 = em
										.createNamedQuery("ArtikelkommentarsprfindByArtikelkommentarIIdLocaleCNr");
								query3.setParameter(1, artikelkommentar.getIId());
								query3.setParameter(2, localeCNr);
								Artikelkommentarspr artikelspr = (Artikelkommentarspr) query3.getSingleResult();
								return artikelspr.getOMedia();

							} catch (NoResultException ex) {
								// nicht gefunden
							}
						}

					}

				} catch (NoResultException ex) {
					//
				}

			}
		} catch (NoResultException ex) {
			//
		}
		return null;
	}

	public byte[] getPDFArtikelkommentar(String artikelCNr, String artikelkommentarartCNr, String localeCNr,
			String mandantCNr) {
		try {

			ArtikelDto aDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(artikelCNr, mandantCNr);

			if (aDto != null && aDto.getIId() != null) {

				try {
					Query query = em.createNamedQuery("ArtikelkommentarartfindByCNr");
					query.setParameter(1, artikelkommentarartCNr);
					Artikelkommentarart art = (Artikelkommentarart) query.getSingleResult();

					Query query2 = em
							.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
					query2.setParameter(1, aDto.getIId());
					query2.setParameter(2, art.getIId());
					query2.setParameter(3, MediaFac.DATENFORMAT_MIMETYPE_APP_PDF);
					Artikelkommentar artikelkommentar = (Artikelkommentar) query2.getSingleResult();

					try {

						Query query3 = em.createNamedQuery("ArtikelkommentarsprfindByArtikelkommentarIIdLocaleCNr");
						query3.setParameter(1, artikelkommentar.getIId());
						query3.setParameter(2, localeCNr);
						Artikelkommentarspr artikelspr = (Artikelkommentarspr) query3.getSingleResult();

						if (Helper.short2boolean(artikelkommentar.getBDateiverweis())) {
							String dateiname = artikelspr.getCDateiname();
							dateiname = dateiverweisLaufwerkDurchUNCErsetzen(dateiname, mandantCNr);
							if (dateiname != null) {
								File f = new File(dateiname);
								if (f.exists()) {
									try {
										byte[] pdf = Helper.getBytesFromFile(f);
										return pdf;

									} catch (IOException e) {
										myLogger.error("IO-Fehler Dateiverweis Artikelkommentar " + f.getAbsolutePath(),
												e);
									}
								} else {
									myLogger.logKritisch("Dateiverweis des Artikelkommentares " + f.getAbsolutePath()
											+ " nicht gefunden");
								}
							}
						} else {
							return artikelspr.getOMedia();
						}

					} catch (NoResultException ex) {
						// nicht gefunden
					}

				} catch (NoResultException ex) {
					//
				}

			}
		} catch (NoResultException ex) {
			//
		}
		return null;
	}

	public void textAusPdfInXKommentarAktualisieren(Integer artikelkommentarIId, String locUI) {
		Artikelkommentarspr artikelkommentarspr = em.find(Artikelkommentarspr.class,
				new ArtikelkommentarsprPK(artikelkommentarIId, locUI));
		if (artikelkommentarspr != null && artikelkommentarspr.getOMedia() != null) {
			PDDocument document = null;
			try {
				document = PDDocument.load(artikelkommentarspr.getOMedia());

				if (!document.isEncrypted()) {

					PDFTextStripper tStripper = new PDFTextStripper();
					String pdfFileInText = tStripper.getText(document);

					String s = pdfFileInText.replaceAll("\\r?\\n", " ");
					if (s != null && s.length() > 3000) {
						s = s.substring(0, 3000);
					}
					artikelkommentarspr.setXKommentar(s);

				}
			} catch (IOException e) {
				System.err.println("Exception while trying to read pdf document - " + e);
			} finally {
				if (document != null) {

					try {
						document.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

					}
				}

			}
		}
	}

	public Integer createArtikelkommentar(ArtikelkommentarDto artikelkommentarDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelkommentarDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikelkommentarDto == null"));
		}
		if (artikelkommentarDto.getArtikelIId() == null || artikelkommentarDto.getArtikelkommentarartIId() == null
				|| artikelkommentarDto.getBDefaultbild() == null || artikelkommentarDto.getDatenformatCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"artikelkommentarDto.getArtikelIId() == null || artikelkommentarDto.getArtikelkommentarartIId() == null || artikelkommentarDto.getBDefaultbild() == null || artikelkommentarDto.getDatenformatCNr() == null"));
		}

		// SP6694
		if (artikelkommentarDto.getBDateiverweis() == null) {
			artikelkommentarDto.setBDateiverweis(Helper.getShortFalse());
		}

		if (artikelkommentarDto.getDatenformatCNr()
				.equals(com.lp.server.system.service.MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			artikelkommentarDto.setBDefaultbild(Helper.boolean2Short(false));
		}

		if (Helper.short2boolean(artikelkommentarDto.getBDefaultbild()) == true) {
			// try {
			Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIId");
			query.setParameter(1, artikelkommentarDto.getArtikelIId());
			Collection<?> cl = query.getResultList();

			ArtikelkommentarDto[] dtos = assembleArtikelkommentarDtos(cl);
			if (dtos != null && dtos.length > 0) {
				for (int i = 0; i < dtos.length; i++) {
					Artikelkommentar artikelkommentar = em.find(Artikelkommentar.class, dtos[0].getIId());
					if (artikelkommentar != null) {
						artikelkommentar.setBDefaultbild(new Short((short) 0));
					}
				}
			}
			// }
			// catch (FinderException ex1) {
			//
			// }
		}

		try {
			Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
			query.setParameter(1, artikelkommentarDto.getArtikelIId());
			query.setParameter(2, artikelkommentarDto.getArtikelkommentarartIId());
			query.setParameter(3, artikelkommentarDto.getDatenformatCNr());
			Artikelkommentar doppelt = (Artikelkommentar) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTIKELKOMMENTAR.UC"));
		} catch (NoResultException ex) {
			//
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELKOMMENTAR);
			artikelkommentarDto.setIId(pk);

			Query queryNext = em.createNamedQuery("ArtikelkommentarejbSelectNextReihung");
			queryNext.setParameter(1, artikelkommentarDto.getArtikelIId());

			Integer iSort = (Integer) queryNext.getSingleResult();
			if (iSort == null) {
				iSort = 0;
			}

			iSort = new Integer(iSort + 1);

			artikelkommentarDto.setISort(iSort);

			Artikelkommentar artikelkommentar = new Artikelkommentar(artikelkommentarDto.getIId(),
					artikelkommentarDto.getArtikelIId(), artikelkommentarDto.getArtikelkommentarartIId(),
					artikelkommentarDto.getDatenformatCNr(), artikelkommentarDto.getBDefaultbild(),
					artikelkommentarDto.getIArt(), artikelkommentarDto.getISort(),
					artikelkommentarDto.getBDateiverweis());
			em.persist(artikelkommentar);
			em.flush();
			setArtikelkommentarFromArtikelkommentarDto(artikelkommentar, artikelkommentarDto);
			if (artikelkommentarDto.getArtikelkommentarsprDto() != null) {

				artikelkommentarDto.getArtikelkommentarsprDto().setPersonalIIdAendern(theClientDto.getIDPersonal());
				artikelkommentarDto.getArtikelkommentarsprDto().setTAendern(new Timestamp(System.currentTimeMillis()));

				Artikelkommentarspr artikelkommentarspr = new Artikelkommentarspr(artikelkommentarDto.getIId(),
						theClientDto.getLocUiAsString(),
						artikelkommentarDto.getArtikelkommentarsprDto().getPersonalIIdAendern(),
						artikelkommentarDto.getArtikelkommentarsprDto().getTAendern());
				em.persist(artikelkommentarspr);
				em.flush();
				setArtikelkommentarsprFromArtikelkommentarsprDto(artikelkommentarspr,
						artikelkommentarDto.getArtikelkommentarsprDto());
			}

			if (artikelkommentarDto.getArtikelkommentardruckDto() != null) {
				for (int i = 0; i < artikelkommentarDto.getArtikelkommentardruckDto().length; i++) {
					ArtikelkommentardruckDto dto = artikelkommentarDto.getArtikelkommentardruckDto()[i];
					dto.setArtikelkommentarIId(artikelkommentarDto.getIId());
					dto.setArtikelIId(artikelkommentarDto.getArtikelIId());
					createArtikelkommentardruck(dto);
				}
			}

			if (artikelkommentarDto.getDatenformatCNr()
					.equals(com.lp.server.system.service.MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
				textAusPdfInXKommentarAktualisieren(artikelkommentarDto.getIId(), theClientDto.getLocUiAsString());
			}

			return artikelkommentarDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void vertauscheArtikelkommentar(Integer iiD1, Integer iId2) {
		Artikelkommentar iArtikelkommentar1 = em.find(Artikelkommentar.class, iiD1);

		Artikelkommentar iArtikelkommentar2 = em.find(Artikelkommentar.class, iId2);

		Integer iSort1 = iArtikelkommentar1.getISort();
		Integer iSort2 = iArtikelkommentar2.getISort();
		// iSort der zweiten Preisliste auf ungueltig setzen, damit UK
		// constraint nicht verletzt wird
		iArtikelkommentar2.setISort(new Integer(-1));
		iArtikelkommentar1.setISort(iSort2);
		iArtikelkommentar2.setISort(iSort1);

	}

	public void removeArtikelkommentar(ArtikelkommentarDto artikelkommentarDto) throws EJBExceptionLP {
		myLogger.entry();
		if (artikelkommentarDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikelkommentarDto == null"));
		}
		if (artikelkommentarDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelkommentarDto.getIId() == null"));
		}
		// try {
		try {
			Query query = em.createNamedQuery("ArtikelkommentarsprfindByArtikelkommentarIId");
			query.setParameter(1, artikelkommentarDto.getIId());
			Collection<?> cl = query.getResultList();
			Iterator<?> iter = cl.iterator();
			while (iter.hasNext()) {
				Artikelkommentarspr artikelkommentarspr = (Artikelkommentarspr) iter.next();
				em.remove(artikelkommentarspr);
			}

			query = em.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIId");
			query.setParameter(1, artikelkommentarDto.getArtikelIId());
			query.setParameter(2, artikelkommentarDto.getIId());
			Collection<?> allDruck = query.getResultList();
			Iterator<?> iterAllDruck = allDruck.iterator();
			while (iterAllDruck.hasNext()) {
				Artikelkommentardruck artklasprTemp = (Artikelkommentardruck) iterAllDruck.next();
				em.remove(artklasprTemp);
			}

			Artikelkommentar artikelkommentar = em.find(Artikelkommentar.class, artikelkommentarDto.getIId());
			if (artikelkommentar == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeArtikelkommentar. Es gibt keine iid " + artikelkommentarDto.getIId()
								+ "\ndto.toString(): " + artikelkommentarDto.toString());
			}
			em.remove(artikelkommentar);
			em.flush();

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }

	}

	public void updateArtikelkommentar(ArtikelkommentarDto artikelkommentarDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelkommentarDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikelkommentarDto == null"));
		}
		if (artikelkommentarDto.getIId() == null || artikelkommentarDto.getArtikelIId() == null
				|| artikelkommentarDto.getArtikelkommentarartIId() == null
				|| artikelkommentarDto.getBDefaultbild() == null || artikelkommentarDto.getDatenformatCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"artikelkommentarDto.getIId() == null || artikelkommentarDto.getArtikelIId() == null || artikelkommentarDto.getArtikelkommentarartIId() == null || artikelkommentarDto.getBDefaultbild() == null"));
		}
		if (artikelkommentarDto.getDatenformatCNr()
				.equals(com.lp.server.system.service.MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			artikelkommentarDto.setBDefaultbild(Helper.boolean2Short(false));
		}

		if (Helper.short2boolean(artikelkommentarDto.getBDefaultbild()) == true) {
			// try {
			Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIId");
			query.setParameter(1, artikelkommentarDto.getArtikelIId());
			ArtikelkommentarDto[] dtos = assembleArtikelkommentarDtos(query.getResultList());
			if (dtos != null && dtos.length > 0) {
				for (int i = 0; i < dtos.length; i++) {
					Artikelkommentar artikelkommentar = em.find(Artikelkommentar.class, dtos[i].getIId());
					if (artikelkommentar != null) {
						artikelkommentar.setBDefaultbild(new Short((short) 0));
					}
				}
			}
			// }
			// catch (FinderException ex1) {
			//
			// }
		}

		Integer iId = artikelkommentarDto.getIId();

		Artikelkommentar artikelkommentar = null;
		// try {
		artikelkommentar = em.find(Artikelkommentar.class, iId);
		if (artikelkommentar == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikelkommentar. Es gibt keine iid + " + iId + "\ndto.toString(): "
							+ artikelkommentarDto.toString());

		}

		Query query = em.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIId");
		query.setParameter(1, artikelkommentarDto.getArtikelIId());
		query.setParameter(2, artikelkommentarDto.getIId());
		Collection<?> allDruck = query.getResultList();
		Iterator<?> iterAllDruck = allDruck.iterator();
		try {
			while (iterAllDruck.hasNext()) {
				Artikelkommentardruck artklasprTemp = (Artikelkommentardruck) iterAllDruck.next();
				em.remove(artklasprTemp);
			}
			em.flush();
		} catch (EntityExistsException ex2) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex2);
		}
		if (artikelkommentarDto.getArtikelkommentardruckDto() != null) {
			for (int i = 0; i < artikelkommentarDto.getArtikelkommentardruckDto().length; i++) {
				ArtikelkommentardruckDto dto = artikelkommentarDto.getArtikelkommentardruckDto()[i];
				dto.setArtikelkommentarIId(artikelkommentarDto.getIId());
				dto.setArtikelIId(artikelkommentarDto.getArtikelIId());
				createArtikelkommentardruck(dto);
			}
		}

		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }
		try {
			query = em.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
			query.setParameter(1, artikelkommentarDto.getArtikelIId());
			query.setParameter(2, artikelkommentarDto.getArtikelkommentarartIId());
			query.setParameter(3, artikelkommentarDto.getDatenformatCNr());
			Integer iIdVorhanden = ((Artikelkommentar) query.getSingleResult()).getIId();

			if (iId.equals(iIdVorhanden) == false) {

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_ARTIKELKOMMENTAR.UC"));
			}
		} catch (NoResultException ex) {
			//
		}
		try {
			setArtikelkommentarFromArtikelkommentarDto(artikelkommentar, artikelkommentarDto);

			if (artikelkommentarDto.getArtikelkommentarsprDto() != null) {
				artikelkommentarDto.getArtikelkommentarsprDto().setPersonalIIdAendern(theClientDto.getIDPersonal());
				artikelkommentarDto.getArtikelkommentarsprDto().setTAendern(new Timestamp(System.currentTimeMillis()));

				// PJ19566
				String locale = artikelkommentarDto.getArtikelkommentarsprDto().getLocaleCNr();
				if (locale == null) {
					locale = theClientDto.getLocUiAsString();
				}

				Artikelkommentarspr artklaspr = em.find(Artikelkommentarspr.class,
						new ArtikelkommentarsprPK(iId, locale));
				if (artklaspr == null) {
					artklaspr = new Artikelkommentarspr(iId, locale,
							artikelkommentarDto.getArtikelkommentarsprDto().getPersonalIIdAendern(),
							artikelkommentarDto.getArtikelkommentarsprDto().getTAendern());
					em.persist(artklaspr);
					em.flush();
					setArtikelkommentarsprFromArtikelkommentarsprDto(artklaspr,
							artikelkommentarDto.getArtikelkommentarsprDto());
				}
				setArtikelkommentarsprFromArtikelkommentarsprDto(artklaspr,
						artikelkommentarDto.getArtikelkommentarsprDto());

				if (artikelkommentarDto.getDatenformatCNr()
						.equals(com.lp.server.system.service.MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
					textAusPdfInXKommentarAktualisieren(iId, locale);
				}

			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public byte[] getArtikeldefaultBild(Integer artikelIId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("artikelIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIIdBDefaultbild");
		query.setParameter(1, artikelIId);
		query.setParameter(2, new Short((short) 1));
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }

		ArtikelkommentarDto[] dtos = assembleArtikelkommentarDtos(cl);

		if (dtos != null && dtos.length > 0) {

			ArtikelkommentarDto dto = artikelkommentarFindByPrimaryKey(dtos[0].getIId(), theClientDto);

			if (dto.getArtikelkommentarsprDto() != null) {
				return dto.getArtikelkommentarsprDto().getOMedia();
			} else {
				return null;
			}
		} else {
			return null;
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArrayList<byte[]> getArtikelBilder(Integer artikelIId, TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("artikelIId == null"));
		}

		ArrayList<byte[]> al = new ArrayList<byte[]>();

		// try {
		Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIIdBDefaultbild");
		query.setParameter(1, artikelIId);
		query.setParameter(2, new Short((short) 1));
		Collection<?> cl = query.getResultList();

		ArtikelkommentarDto[] dtos = assembleArtikelkommentarDtos(cl);

		if (dtos != null && dtos.length > 0) {

			for (int i = 0; i < dtos.length; i++) {

				Artikelkommentarart artikelkommentarart = em.find(Artikelkommentarart.class,
						dtos[i].getArtikelkommentarartIId());

				// SP8421
				if (Helper.short2boolean(artikelkommentarart.getBDetail())) {

					ArtikelkommentarDto dto = artikelkommentarFindByPrimaryKey(dtos[i].getIId(), theClientDto);

					if (dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
							|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
							|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
							|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
						if (dto.getArtikelkommentarsprDto() != null
								&& dto.getArtikelkommentarsprDto().getOMedia() != null) {
							al.add(dto.getArtikelkommentarsprDto().getOMedia());
						}
					}
				}
			}

		}
		query = em.createNamedQuery("ArtikelkommentarfindByArtikelIIdBDefaultbild");
		query.setParameter(1, artikelIId);
		query.setParameter(2, new Short((short) 0));
		cl = query.getResultList();

		boolean bPdfAnzeigen = false;
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_PDF_IN_ARTIKELDETAIL_ANZEIGEN);

			bPdfAnzeigen = (Boolean) parameter.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		dtos = assembleArtikelkommentarDtos(cl);

		if (dtos != null && dtos.length > 0) {

			for (int i = 0; i < dtos.length; i++) {

				Artikelkommentarart artikelkommentarart = em.find(Artikelkommentarart.class,
						dtos[i].getArtikelkommentarartIId());

				// SP8421
				if (Helper.short2boolean(artikelkommentarart.getBDetail())) {

					ArtikelkommentarDto dto = artikelkommentarFindByPrimaryKey(dtos[i].getIId(), theClientDto);

					if (dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
							|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
							|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
							|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
						if (dto.getArtikelkommentarsprDto() != null
								&& dto.getArtikelkommentarsprDto().getOMedia() != null) {
							al.add(dto.getArtikelkommentarsprDto().getOMedia());
						}
					}

					if (bPdfAnzeigen) {
						if (dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {

							byte[] pdf = null;

							if (Helper.short2boolean(dto.getBDateiverweis())) {
								if (dto.getArtikelkommentarsprDto() != null) {
									String dateiname = dto.getArtikelkommentarsprDto().getCDateiname();
									dateiname = dateiverweisLaufwerkDurchUNCErsetzen(dateiname, theClientDto);
									if (dateiname != null) {
										File f = new File(dateiname);
										if (f.exists()) {
											try {
												pdf = Helper.getBytesFromFile(f);

											} catch (IOException e) {
												myLogger.error("IO-Fehler Dateiverweis Artikelkommentar "
														+ f.getAbsolutePath(), e);
											}
										} else {
											myLogger.logKritisch("Dateiverweis des Artikelkommentares "
													+ f.getAbsolutePath() + " nicht gefunden");
										}
									}
								}

							} else {
								if (dto.getArtikelkommentarsprDto() != null
										&& dto.getArtikelkommentarsprDto().getOMedia() != null) {
									pdf = dto.getArtikelkommentarsprDto().getOMedia();
								}
							}

							if (pdf != null) {
								PDDocument document = null;

								try {

									InputStream myInputStream = new ByteArrayInputStream(pdf);

									document = PDDocument.load(myInputStream);
									int numPages = document.getNumberOfPages();
									PDFRenderer renderer = new PDFRenderer(document);

									for (int p = 0; p < numPages; p++) {

										BufferedImage image = renderer.renderImageWithDPI(p, 150); // Windows
										al.add(Helper.imageToByteArray(image));

									}
								} catch (IOException e) {
									e.printStackTrace();
									throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

								} finally {
									if (document != null) {

										try {
											document.close();
										} catch (IOException e) {
											e.printStackTrace();
											throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

										}
									}

								}

							}
						}
					}
				}
			}

		}
		return al;

	}

	public boolean gibtEsKommentareInAnderenSprachen(Integer artikelkommentarIId, TheClientDto theClientDto) {

		Query query2 = em.createNamedQuery("ArtikelkommentarsprfindByArtikelkommentarIId");
		query2.setParameter(1, artikelkommentarIId);
		Collection<?> cl2 = query2.getResultList();
		ArtikelkommentarsprDto[] artikelkommentarsprDtos = assembleArtikelkommentarsprDtos(cl2);

		for (int i = 0; i < artikelkommentarsprDtos.length; i++) {
			ArtikelkommentarsprDto artikelkommentarsprDto = artikelkommentarsprDtos[i];
			if (!artikelkommentarsprDto.getLocaleCNr().equals(theClientDto.getLocUiAsString())) {
				return true;
			}
		}
		return false;
	}

	public ArtikelkommentarDto artikelkommentarFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Artikelkommentar artikelkommentar = em.find(Artikelkommentar.class, iId);
		if (artikelkommentar == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelkommentarFindByPrimaryKey. Es gibt keine iid " + iId);
		}
		ArtikelkommentarDto artikelkommentarDto = assembleArtikelkommentarDto(artikelkommentar);

		Query query = em.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIId");
		query.setParameter(1, artikelkommentar.getArtikelIId());
		query.setParameter(2, artikelkommentar.getIId());
		Collection<?> cl = query.getResultList();
		artikelkommentarDto.setArtikelkommentardruckDto(assembleArtikelkommentardruckDtos(cl));

		ArtikelkommentarsprDto artikelkommentarsprDto = null;

		// try {
		Artikelkommentarspr artikelkommentarspr = em.find(Artikelkommentarspr.class,
				new ArtikelkommentarsprPK(iId, theClientDto.getLocUiAsString()));
		if (artikelkommentarspr == null) {
			// nothing here
		} else {
			artikelkommentarsprDto = assembleArtikelkommentarsprDto(artikelkommentarspr);
		}
		// }
		// catch (FinderException ex) {
		// nothing here
		// }
		if (artikelkommentarsprDto == null) {
			// try {
			Artikelkommentarspr temp = em.find(Artikelkommentarspr.class,
					new ArtikelkommentarsprPK(iId, theClientDto.getLocKonzernAsString()));
			if (temp == null) {
				// nothing here
			} else {
				artikelkommentarsprDto = assembleArtikelkommentarsprDto(temp);
			}

			// }
			// catch (FinderException ex) {
			// nothing here
			// }
		}
		artikelkommentarDto.setArtikelkommentarsprDto(artikelkommentarsprDto);
		return artikelkommentarDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public boolean kopiereArtikelkommentar(Integer artikelIId_alt, Integer artikelIId_neu, boolean bNurClientLocale,
			TheClientDto theClientDto) {

		boolean bEsWurdeMehrereSprachenKopiert = false;

		Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIId");
		query.setParameter(1, artikelIId_alt);
		Collection<?> cl = query.getResultList();

		ArtikelkommentarDto[] artikelkommentarDtos = assembleArtikelkommentarDtos(cl);

		for (int i = 0; i < artikelkommentarDtos.length; i++) {

			// Nur wenn Kommentarart noch nicht vorhanden
			Query queryUK = em.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
			queryUK.setParameter(1, artikelIId_neu);
			queryUK.setParameter(2, artikelkommentarDtos[i].getArtikelkommentarartIId());
			queryUK.setParameter(3, artikelkommentarDtos[i].getDatenformatCNr());
			Collection<?> clUK = queryUK.getResultList();

			if (clUK.size() == 0) {

				Integer artikelkommentarIId_Alt = artikelkommentarDtos[i].getIId();

				ArtikelkommentarDto dto = artikelkommentarDtos[i];
				dto.setIId(null);
				dto.setArtikelIId(artikelIId_neu);

				PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
				Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELKOMMENTAR);

				Artikelkommentar artikelkommentar = new Artikelkommentar(pk, artikelIId_neu,
						dto.getArtikelkommentarartIId(), dto.getDatenformatCNr(), dto.getBDefaultbild(), dto.getIArt(),
						dto.getISort(), dto.getBDateiverweis());
				em.persist(artikelkommentar);
				em.flush();

				Query query2 = em.createNamedQuery("ArtikelkommentarsprfindByArtikelkommentarIId");
				query2.setParameter(1, artikelkommentarIId_Alt);
				Collection<?> cl2 = query2.getResultList();
				ArtikelkommentarsprDto[] artikelkommentarsprDtos = assembleArtikelkommentarsprDtos(cl2);

				for (int z = 0; z < artikelkommentarsprDtos.length; z++) {

					if (bNurClientLocale == true
							&& !artikelkommentarsprDtos[z].getLocaleCNr().equals(theClientDto.getLocUiAsString())) {
						// auslassen
					} else {

						Artikelkommentarspr artikelkommentarspr = new Artikelkommentarspr(pk,
								artikelkommentarsprDtos[z].getLocaleCNr(),
								artikelkommentarsprDtos[z].getPersonalIIdAendern(),
								artikelkommentarsprDtos[z].getTAendern());

						em.persist(artikelkommentarspr);
						em.flush();
						setArtikelkommentarsprFromArtikelkommentarsprDto(artikelkommentarspr,
								artikelkommentarsprDtos[z]);

						if (!artikelkommentarsprDtos[z].getLocaleCNr().equals(theClientDto.getLocUiAsString())) {
							bEsWurdeMehrereSprachenKopiert = true;
						}
					}

				}

				// druck

				Query querydruck = em.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIId");
				querydruck.setParameter(1, artikelIId_alt);
				querydruck.setParameter(2, artikelkommentarIId_Alt);
				Collection<?> cldruck = querydruck.getResultList();
				ArtikelkommentardruckDto[] artikelkommentardruckDtos = assembleArtikelkommentardruckDtos(cldruck);

				for (int z = 0; z < artikelkommentardruckDtos.length; z++) {
					ArtikelkommentardruckDto artikelkommentardruckDto = artikelkommentardruckDtos[z];
					artikelkommentardruckDto.setIId(null);
					artikelkommentardruckDto.setArtikelIId(artikelIId_neu);
					artikelkommentardruckDto.setArtikelkommentarIId(pk);
					createArtikelkommentardruck(artikelkommentardruckDto);
				}
			}
		}

		return bEsWurdeMehrereSprachenKopiert;

	}

	public ArtikelkommentarDto[] artikelkommentarFindByArtikelIId(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Validator.notNull(artikelIId, "artikelIId");

		// Query query =
		// em.createNamedQuery("ArtikelkommentarfindByArtikelIId");
		Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIIdSorted");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		ArtikelkommentarDto[] artikelkommentarDtos = assembleArtikelkommentarDtos(cl);

		for (int i = 0; i < artikelkommentarDtos.length; i++) {
			ArtikelkommentarDto artikelkommentarDto = artikelkommentarDtos[i];
			// @ToDo Ungueltiger Prozeduraufruf oder ungueltiges Argument
			// artikelkommentarDto
			// .setArtikelkommentardruckDto(assembleArtikelkommentardruckDtos
			// (artikelkommentardruckHome
			// .findByArtikelIIdArtikelkommentarIId(artikelkommentarDto
			// .getArtikelIId(), artikelkommentarDto.getIId())));
			artikelkommentarDto.setArtikelkommentarsprDto(
					artikelkommentarsprFindByPrimaryKey(artikelkommentarDto.getIId(), theClientDto.getLocUiAsString()));

		}
		return artikelkommentarDtos;
	}

	public ArtikelkommentarsprDto artikelkommentarsprFindByPrimaryKey(Integer artikelkommentarIId, String localeCNr)
			throws EJBExceptionLP {
		if (artikelkommentarIId == null || localeCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelkommentarIId == null || localeCNr == null"));
		}

		// try {
		Artikelkommentarspr artikelkommentarspr = (Artikelkommentarspr) em.find(Artikelkommentarspr.class,
				new ArtikelkommentarsprPK(artikelkommentarIId, localeCNr));
		if (artikelkommentarspr == null) {
			return null;
		}
		return assembleArtikelkommentarsprDto(artikelkommentarspr);
		// }
		// catch (FinderException ex) {
		// return null;
		// }

	}

	public ArtikelkommentarsprDto artikelkommentarsprFindByPrimaryKeyOhneExc(Integer artikelkommentarIId,
			String localeCNr, TheClientDto theClientDto) {

		ArtikelkommentarsprDto artikelkommentarsprDto = null;
		try {

			Query query = em.createNamedQuery("ArtikelkommentarsprfindByArtikelkommentarIIdLocaleCNr");
			query.setParameter(1, artikelkommentarIId);
			query.setParameter(2, localeCNr);
			Artikelkommentarspr artikelspr = (Artikelkommentarspr) query.getSingleResult();
			artikelkommentarsprDto = assembleArtikelkommentarsprDto(artikelspr);
		} catch (NoResultException ex) {
			// nicht gefunden
		}

		return artikelkommentarsprDto;
	}

	public ArtikelkommentarDto artikelkommentarFindByPrimaryKeyUndLocale(Integer iId, String localeCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Artikelkommentar artikelkommentar = em.find(Artikelkommentar.class, iId);
		if (artikelkommentar == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelkommentarFindByPrimaryKeyUndLocale. Es gibt keine iid " + iId);
		}
		ArtikelkommentarDto artikelkommentarDto = assembleArtikelkommentarDto(artikelkommentar);

		ArtikelkommentarsprDto artikelkommentarsprDto = null;
		Artikelkommentarspr artikelkommentarspr = em.find(Artikelkommentarspr.class,
				new ArtikelkommentarsprPK(iId, localeCNr));
		if (artikelkommentarspr != null) {
			artikelkommentarsprDto = assembleArtikelkommentarsprDto(artikelkommentarspr);
		}

		if (artikelkommentarsprDto == null) {
			Artikelkommentarspr temp = em.find(Artikelkommentarspr.class,
					new ArtikelkommentarsprPK(iId, theClientDto.getLocKonzernAsString()));
			if (temp != null) {
				artikelkommentarsprDto = assembleArtikelkommentarsprDto(temp);
			} else {
				artikelkommentarsprDto = new ArtikelkommentarsprDto();
				artikelkommentarsprDto.setArtikelkommentarIId(iId);
				artikelkommentarsprDto.setXKommentar(getTextRespectUISpr("artikel.keinkommentarinsprvorhanden",
						theClientDto.getMandant(), theClientDto.getLocUi(), localeCNr.trim()));
			}
		}
		artikelkommentarDto.setArtikelkommentarsprDto(artikelkommentarsprDto);
		return artikelkommentarDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setArtikelkommentarFromArtikelkommentarDto(Artikelkommentar artikelkommentar,
			ArtikelkommentarDto artikelkommentarDto) {
		artikelkommentar.setArtikelIId(artikelkommentarDto.getArtikelIId());
		artikelkommentar.setArtikelkommentarartIId(artikelkommentarDto.getArtikelkommentarartIId());
		artikelkommentar.setBDefaultbild(artikelkommentarDto.getBDefaultbild());
		artikelkommentar.setIArt(artikelkommentarDto.getIArt());
		artikelkommentar.setDatenformatCNr(artikelkommentarDto.getDatenformatCNr());
		artikelkommentar.setISort(artikelkommentarDto.getISort());
		artikelkommentar.setBDateiverweis(artikelkommentarDto.getBDateiverweis());
		em.merge(artikelkommentar);
		em.flush();
	}

	private ArtikelkommentarDto assembleArtikelkommentarDto(Artikelkommentar artikelkommentar) {
		return ArtikelkommentarDtoAssembler.createDto(artikelkommentar);
	}

	private ArtikelkommentarDto[] assembleArtikelkommentarDtos(Collection<?> artikelkommentars) {
		List<ArtikelkommentarDto> list = new ArrayList<ArtikelkommentarDto>();
		if (artikelkommentars != null) {
			Iterator<?> iterator = artikelkommentars.iterator();
			while (iterator.hasNext()) {
				Artikelkommentar artikelkommentar = (Artikelkommentar) iterator.next();
				list.add(assembleArtikelkommentarDto(artikelkommentar));
			}
		}
		ArtikelkommentarDto[] returnArray = new ArtikelkommentarDto[list.size()];
		return (ArtikelkommentarDto[]) list.toArray(returnArray);
	}

	private void setArtikelkommentarsprFromArtikelkommentarsprDto(Artikelkommentarspr artikelkommentarspr,
			ArtikelkommentarsprDto artikelkommentarsprDto) {
		artikelkommentarspr.setXKommentar(artikelkommentarsprDto.getXKommentar());
		artikelkommentarspr.setOMedia(artikelkommentarsprDto.getOMedia());
		artikelkommentarspr.setCDateiname(artikelkommentarsprDto.getCDateiname());
		artikelkommentarspr.setTFiledatum(artikelkommentarsprDto.getTFiledatum());
		artikelkommentarspr.setTAendern(artikelkommentarsprDto.getTAendern());
		artikelkommentarspr.setPersonalIIdAendern(artikelkommentarsprDto.getPersonalIIdAendern());
		em.merge(artikelkommentarspr);
		em.flush();
	}

	private ArtikelkommentarsprDto assembleArtikelkommentarsprDto(Artikelkommentarspr artikelkommentarspr) {
		return ArtikelkommentarsprDtoAssembler.createDto(artikelkommentarspr);
	}

	private ArtikelkommentarsprDto[] assembleArtikelkommentarsprDtos(Collection<?> artikelkommentarsprs) {
		List<ArtikelkommentarsprDto> list = new ArrayList<ArtikelkommentarsprDto>();
		if (artikelkommentarsprs != null) {
			Iterator<?> iterator = artikelkommentarsprs.iterator();
			while (iterator.hasNext()) {
				Artikelkommentarspr artikelkommentarspr = (Artikelkommentarspr) iterator.next();
				list.add(assembleArtikelkommentarsprDto(artikelkommentarspr));
			}
		}
		ArtikelkommentarsprDto[] returnArray = new ArtikelkommentarsprDto[list.size()];
		return (ArtikelkommentarsprDto[]) list.toArray(returnArray);
	}

	public Integer createArtikelkommentardruck(ArtikelkommentardruckDto artikelkommentardruckDto)
			throws EJBExceptionLP {

		myLogger.entry();
		if (artikelkommentardruckDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentardruckDto == null"));
		}
		if (artikelkommentardruckDto.getArtikelIId() == null
				|| artikelkommentardruckDto.getArtikelkommentarIId() == null
				|| artikelkommentardruckDto.getBelegartCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"artikelkommentardruckDto.getArtikelIId() == null || artikelkommentardruckDto.getArtikelkommentarIId() == null || artikelkommentardruckDto.getBelegartCNr() == null"));
		}
		try {
			Query query = em.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIIdBelegartCNr");
			query.setParameter(1, artikelkommentardruckDto.getArtikelIId());
			query.setParameter(2, artikelkommentardruckDto.getArtikelkommentarIId());
			query.setParameter(3, artikelkommentardruckDto.getBelegartCNr());
			// @todo getSingleResult oder getResultList ?
			Artikelkommentardruck doppelt = (Artikelkommentardruck) query.getSingleResult();
			// if (doppelt != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELKOMMENTARDRUCK.UC"));
		} catch (NoResultException ex) {
			//
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELKOMMENTARDRUCK);
			artikelkommentardruckDto.setIId(pk);
			Artikelkommentardruck artikelkommentardruck = new Artikelkommentardruck(artikelkommentardruckDto.getIId(),
					artikelkommentardruckDto.getArtikelIId(), artikelkommentardruckDto.getArtikelkommentarIId(),
					artikelkommentardruckDto.getBelegartCNr());

			em.persist(artikelkommentardruck);
			em.flush();
			setArtikelkommentardruckFromArtikelkommentardruckDto(artikelkommentardruck, artikelkommentardruckDto);

			return artikelkommentardruckDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void removeArtikelkommentardruck(ArtikelkommentardruckDto artikelkommentardruckDto) throws EJBExceptionLP {
		myLogger.entry();
		if (artikelkommentardruckDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentardruckDto == null"));
		}
		if (artikelkommentardruckDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelkommentardruckDto.getIId() == null"));
		}
		// try {
		Artikelkommentardruck artikelkommentardruck = em.find(Artikelkommentardruck.class,
				artikelkommentardruckDto.getIId());
		if (artikelkommentardruck == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikelkommentardruck. Es gibt keine iid " + artikelkommentardruckDto.getIId()
							+ "\ndto.toString: " + artikelkommentardruckDto.toString());
		}
		try {
			em.remove(artikelkommentardruck);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }

	}

	public void updateArtikelkommentardruck(ArtikelkommentardruckDto artikelkommentardruckDto) throws EJBExceptionLP {
		myLogger.entry();
		if (artikelkommentardruckDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelkommentardruckDto == null"));
		}
		Integer iId = artikelkommentardruckDto.getIId();
		Artikelkommentardruck artikelkommentardruck = null;
		// try {
		artikelkommentardruck = em.find(Artikelkommentardruck.class, iId);
		if (artikelkommentardruck == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikelkommentardruck. Es gibt keine iid " + iId + "\ndto.toString(): "
							+ artikelkommentardruckDto.toString());
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// try {
		Query query = em.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIIdBelegartCNr");
		query.setParameter(1, artikelkommentardruckDto.getArtikelIId());
		query.setParameter(2, artikelkommentardruckDto.getArtikelkommentarIId());
		query.setParameter(3, artikelkommentardruckDto.getBelegartCNr());
		Integer iIdVorhanden = ((Artikelkommentardruck) query.getSingleResult()).getIId();
		if (iId.equals(iIdVorhanden) == false) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELKOMMENTARDRUCK.UC"));
		}
		// }
		// catch (FinderException ex) {
		// nothing here
		// }
		setArtikelkommentardruckFromArtikelkommentardruckDto(artikelkommentardruck, artikelkommentardruckDto);

	}

	public void updateArtikelkommentardrucks(ArtikelkommentardruckDto[] artikelkommentardruckDtos)
			throws EJBExceptionLP {
		if (artikelkommentardruckDtos != null) {
			for (int i = 0; i < artikelkommentardruckDtos.length; i++) {
				updateArtikelkommentardruck(artikelkommentardruckDtos[i]);
			}
		}
	}

	public ArtikelkommentardruckDto artikelkommentardruckFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		ArtikelkommentardruckDto artikelkommentardruckDto = new ArtikelkommentardruckDto();
		Artikelkommentardruck artikelkommentardruck = em.find(Artikelkommentardruck.class, iId);
		if (artikelkommentardruck == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelkommentardruckFindByPrimaryKey. Es gibt keine iid " + iId);
		}
		artikelkommentardruckDto = assembleArtikelkommentardruckDto(artikelkommentardruck);
		return artikelkommentardruckDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public boolean sindTexteOderPDFsVorhanden(Integer artikelIId, TheClientDto theClientDto) {
		ArrayList<String> al = new ArrayList<String>();
		try {
			Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIId");
			query.setParameter(1, artikelIId);
			ArtikelkommentarDto[] artikelkommentarDtos = assembleArtikelkommentarDtos(query.getResultList());

			for (int i = 0; i < artikelkommentarDtos.length; i++) {

				if (artikelkommentarDtos[i].getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)
						|| artikelkommentarDtos[i].getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {

					return true;

				}

			}
		} catch (NoResultException e) {
			// nothing here
		}
		return false;
	}

	public ArrayList<KeyvalueDto> getArtikelhinweise(Integer artikelIId, String belegartCNr,
			TheClientDto theClientDto) {
		ArrayList<KeyvalueDto> al = new ArrayList<KeyvalueDto>();
		try {
			Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIId");
			query.setParameter(1, artikelIId);
			ArtikelkommentarDto[] artikelkommentarDtos = assembleArtikelkommentarDtos(query.getResultList());

			for (int i = 0; i < artikelkommentarDtos.length; i++) {
				if (artikelkommentarDtos[i].getIArt() == ARTIKELKOMMENTARART_HINWEIS) {

					if (artikelkommentarDtos[i].getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {

						Query query1 = em.createNamedQuery(
								"ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIIdBelegartCNr");
						query1.setParameter(1, artikelIId);
						query1.setParameter(2, artikelkommentarDtos[i].getIId());
						query1.setParameter(3, belegartCNr);
						// @todo getSingleResult oder getResultList ?

						try {

							Artikelkommentardruck artikelkommentardruck = (Artikelkommentardruck) query1
									.getSingleResult();
							if (artikelkommentardruck != null) {
								// try {
								Artikelkommentarspr artikelkommentarspr = em.find(Artikelkommentarspr.class,
										new ArtikelkommentarsprPK(artikelkommentarDtos[i].getIId(),
												theClientDto.getLocUiAsString()));
								if (artikelkommentarspr == null) {
									// nothing here
								} else {
									ArtikelkommentarsprDto artikelkommentarsprDto = assembleArtikelkommentarsprDto(
											artikelkommentarspr);
									if (artikelkommentarsprDto.getXKommentar() != null) {

										ArtikelkommentarartDto artDto = artikelkommentarartFindByPrimaryKey(
												artikelkommentarDtos[i].getArtikelkommentarartIId(), theClientDto);

										KeyvalueDto kvDto = new KeyvalueDto(artDto.getBezeichnung(),
												artikelkommentarsprDto.getXKommentar());

										al.add(kvDto);
									}
								}
								// }
								// catch (FinderException ex) {
								// nothing here
								// }
							}
						} catch (NoResultException e) {
							// nothing here
						}

					}

				}
			}
		} catch (NoResultException e) {
			// nothing here
		}
		return al;
	}

	public ArrayList<byte[]> getArtikelhinweiseBild(Integer artikelIId, String belegartCNr, TheClientDto theClientDto) {
		ArrayList<byte[]> al = new ArrayList<byte[]>();
		try {
			Query query = em.createNamedQuery("ArtikelkommentarfindByArtikelIId");
			query.setParameter(1, artikelIId);
			ArtikelkommentarDto[] artikelkommentarDtos = assembleArtikelkommentarDtos(query.getResultList());

			for (int i = 0; i < artikelkommentarDtos.length; i++) {
				if (artikelkommentarDtos[i].getIArt() == ARTIKELKOMMENTARART_HINWEIS) {

					if (artikelkommentarDtos[i].getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
							|| artikelkommentarDtos[i].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
							|| artikelkommentarDtos[i].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
							|| artikelkommentarDtos[i].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

						Query query1 = em.createNamedQuery(
								"ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIIdBelegartCNr");
						query1.setParameter(1, artikelIId);
						query1.setParameter(2, artikelkommentarDtos[i].getIId());
						query1.setParameter(3, belegartCNr);
						// @todo getSingleResult oder getResultList ?

						try {

							Artikelkommentardruck artikelkommentardruck = (Artikelkommentardruck) query1
									.getSingleResult();
							if (artikelkommentardruck != null) {
								// try {
								Artikelkommentarspr artikelkommentarspr = em.find(Artikelkommentarspr.class,
										new ArtikelkommentarsprPK(artikelkommentarDtos[i].getIId(),
												theClientDto.getLocUiAsString()));
								if (artikelkommentarspr == null) {
									// nothing here
								} else {
									ArtikelkommentarsprDto artikelkommentarsprDto = assembleArtikelkommentarsprDto(
											artikelkommentarspr);
									if (artikelkommentarsprDto.getOMedia() != null) {
										al.add(artikelkommentarsprDto.getOMedia());
									}
								}
								// }
								// catch (FinderException ex) {
								// nothing here
								// }
							}
						} catch (NoResultException e) {
							// nothing here
						}

					}

				}
			}
		} catch (NoResultException e) {
			// nothing here
		}
		return al;
	}

	public ArtikelkommentarDto[] artikelkommentardruckFindByArtikelIIdBelegartCNr(Integer artikelIId,
			String belegartCNr, String localeCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null || belegartCNr == null || localeCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || belegartCNr == null || localeCNr == null"));
		}
		Query query = em.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdBelegartCNr");
		query.setParameter(1, artikelIId);
		query.setParameter(2, belegartCNr);
		Collection<?> cl = query.getResultList();

		ArtikelkommentardruckDto[] artikelkommentardruckDtos = assembleArtikelkommentardruckDtos(cl);
		ArrayList<ArtikelkommentarDto> al = new ArrayList<ArtikelkommentarDto>();

		// long tick0 = System.currentTimeMillis();
		for (int i = 0; i < artikelkommentardruckDtos.length; i++) {
			ArtikelkommentarDto artikelkommentarDto = artikelkommentarFindByPrimaryKeyUndLocale(
					artikelkommentardruckDtos[i].getArtikelkommentarIId(), localeCNr, theClientDto);
			if (artikelkommentarDto.getIArt() == ARTIKELKOMMENTARART_MITDRUCKEN) {

				if (Helper.short2boolean(artikelkommentarDto.getBDateiverweis())) {
					if (artikelkommentarDto.getArtikelkommentarsprDto() != null) {
						String dateiname = artikelkommentarDto.getArtikelkommentarsprDto().getCDateiname();
						dateiname = dateiverweisLaufwerkDurchUNCErsetzen(dateiname, theClientDto);
						if (dateiname != null) {
							File f = new File(dateiname);
							if (f.exists()) {
								try {
									byte[] pdf = Helper.getBytesFromFile(f);

									artikelkommentarDto.getArtikelkommentarsprDto().setOMedia(pdf);
									al.add(artikelkommentarDto);
								} catch (IOException e) {
									myLogger.error("IO-Fehler Dateiverweis Artikelkommentar " + f.getAbsolutePath(), e);
								}
							} else {
								myLogger.logKritisch("Dateiverweis des Artikelkommentares " + f.getAbsolutePath()
										+ " nicht gefunden");
							}
						}
					}
				} else {
					al.add(artikelkommentarDto);
				}

			}
		}
		// myLogger.warn("Building artikelkommentardruck dauert " +
		// (System.currentTimeMillis() - tick0) + "ms.");

		Collections.sort(al, new Comparator<ArtikelkommentarDto>() {
			public int compare(ArtikelkommentarDto o1, ArtikelkommentarDto o2) {
				return o1.getISort().compareTo(o2.getISort());
			};
		});

		// Nun noch nach I_SORT sortieren
		// for (int i = al.size() - 1; i > 0; --i) {
		// for (int j = 0; j < i; ++j) {
		// ArtikelkommentarDto o = al.get(j);
		// ArtikelkommentarDto o1 = al.get(j + 1);
		//
		// int iSort = o.getISort();
		// int iSort1 = o1.getISort();
		//
		// if (iSort > iSort1) {
		// al.set(j, o1);
		// al.set(j + 1, o);
		// }
		// }
		// }

		ArtikelkommentarDto[] dtos = new ArtikelkommentarDto[al.size()];
		return (ArtikelkommentarDto[]) al.toArray(dtos);
	}

	public String dateiverweisLaufwerkDurchUNCErsetzen(String dateiname, TheClientDto theClientDto) {
		return dateiverweisLaufwerkDurchUNCErsetzen(dateiname, theClientDto.getMandant());
	}

	private String dateiverweisLaufwerkDurchUNCErsetzen(String dateiname, String mandantCNr) {
		if (dateiname != null) {
			Query query = em.createNamedQuery("DateiverweisfindByMandantCNr");
			query.setParameter(1, mandantCNr);

			Collection<?> cl = query.getResultList();
			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Dateiverweis dv = (Dateiverweis) it.next();
				if (dateiname.toLowerCase().startsWith(dv.getCLaufwerk().toLowerCase())) {

					dateiname = dateiname.substring(dv.getCLaufwerk().length());
					return dv.getCUnc() + dateiname;
				}

			}

		}

		return dateiname;
	}

	public ArrayList<byte[]> getArtikelbilderFindByArtikelIIdBelegartCNr(Integer artikelIId, String belegartCNr,
			String localeCNr, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdBelegartCNr");
		query.setParameter(1, artikelIId);
		query.setParameter(2, belegartCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }

		ArtikelkommentardruckDto[] artikelkommentardruckDtos = assembleArtikelkommentardruckDtos(cl);
		ArrayList<ArtikelkommentarDto> al = new ArrayList<ArtikelkommentarDto>();

		for (int i = 0; i < artikelkommentardruckDtos.length; i++) {
			ArtikelkommentarDto artikelkommentarDto = artikelkommentarFindByPrimaryKeyUndLocale(
					artikelkommentardruckDtos[i].getArtikelkommentarIId(), localeCNr, theClientDto);
			if (artikelkommentarDto.getIArt() == ARTIKELKOMMENTARART_MITDRUCKEN) {
				al.add(artikelkommentarDto);
			}
		}

		// Nun noch nach I_SORT sortieren
		for (int i = al.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				ArtikelkommentarDto o = al.get(j);
				ArtikelkommentarDto o1 = al.get(j + 1);

				int iSort = o.getISort();
				int iSort1 = o1.getISort();

				if (iSort > iSort1) {
					al.set(j, o1);
					al.set(j + 1, o);
				}
			}
		}

		ArrayList<byte[]> bilder = new ArrayList<byte[]>();

		for (int i = 0; i < al.size(); i++) {

			ArtikelkommentarDto dto = al.get(i);

			if (dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
					|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
					|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {
				bilder.add(dto.getArtikelkommentarsprDto().getOMedia());
			} else if (dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {

				byte[] pdf = dto.getArtikelkommentarsprDto().getOMedia();

				// SP6701
				if (Helper.short2boolean(dto.getBDateiverweis())) {
					if (dto.getArtikelkommentarsprDto() != null) {
						String dateiname = dto.getArtikelkommentarsprDto().getCDateiname();

						dateiname = dateiverweisLaufwerkDurchUNCErsetzen(dateiname, theClientDto);

						if (dateiname != null) {
							File f = new File(dateiname);
							if (f.exists()) {
								try {
									pdf = Helper.getBytesFromFile(f);

								} catch (IOException e) {
									myLogger.error("IO-Fehler Dateiverweis Artikelkommentar " + f.getAbsolutePath(), e);
								}
							} else {
								myLogger.logKritisch("Dateiverweis des Artikelkommentares " + f.getAbsolutePath()
										+ " nicht gefunden");
							}
						}
					}
				}

				PDDocument document = null;

				try {

					InputStream myInputStream = new ByteArrayInputStream(pdf);

					document = PDDocument.load(myInputStream);

					int numPages = document.getNumberOfPages();

					PDFRenderer renderer = new PDFRenderer(document);
					for (int p = 0; p < numPages; p++) {
						BufferedImage image = renderer.renderImageWithDPI(p, 150);
						bilder.add(Helper.imageToByteArray(image));
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

				} finally {
					if (document != null) {

						try {
							document.close();
						} catch (IOException e) {
							e.printStackTrace();
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

						}
					}

				}

			} else if (dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

				BufferedImage[] tiffs = Helper.tiffToImageArray(dto.getArtikelkommentarsprDto().getOMedia());
				if (tiffs != null) {
					for (int k = 0; k < tiffs.length; k++) {
						bilder.add(Helper.imageToByteArray(tiffs[k]));
					}
				}

			}

		}
		return bilder;

	}

	public ArtikelkommentarDto[] artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(Integer artikelIId,
			String belegartCNr, String localeCNr, TheClientDto theClientDto) {
		if (artikelIId == null || belegartCNr == null || localeCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || belegartCNr == null || localeCNr == null"));
		}
		Query query = em.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdBelegartCNr");
		query.setParameter(1, artikelIId);
		query.setParameter(2, belegartCNr);
		Collection<?> cl = query.getResultList();

		ArtikelkommentardruckDto[] artikelkommentardruckDtos = assembleArtikelkommentardruckDtos(cl);
		ArrayList<ArtikelkommentarDto> al = new ArrayList<ArtikelkommentarDto>();

		for (int i = 0; i < artikelkommentardruckDtos.length; i++) {
			ArtikelkommentarDto artikelkommentarDto = artikelkommentarFindByPrimaryKeyUndLocale(
					artikelkommentardruckDtos[i].getArtikelkommentarIId(), localeCNr, theClientDto);
			if (artikelkommentarDto.getIArt() == ARTIKELKOMMENTARART_ANHANG) {

				if (Helper.short2boolean(artikelkommentarDto.getBDateiverweis())) {
					if (artikelkommentarDto.getArtikelkommentarsprDto() != null) {
						String dateiname = artikelkommentarDto.getArtikelkommentarsprDto().getCDateiname();
						dateiname = dateiverweisLaufwerkDurchUNCErsetzen(dateiname, theClientDto);
						if (dateiname != null) {
							File f = new File(dateiname);
							if (f.exists()) {
								try {
									byte[] file = Helper.getBytesFromFile(f);

									artikelkommentarDto.getArtikelkommentarsprDto().setOMedia(file);
									al.add(artikelkommentarDto);
								} catch (IOException e) {
									myLogger.error("IO-Fehler Dateiverweis Artikelkommentar " + f.getAbsolutePath(), e);
								}
							} else {
								myLogger.logKritisch("Dateiverweis des Artikelkommentares " + f.getAbsolutePath()
										+ " nicht gefunden");
							}
						}
					}
				} else {
					al.add(artikelkommentarDto);
				}

			}
		}

		ArtikelkommentarDto[] dtos = new ArtikelkommentarDto[al.size()];
		return (ArtikelkommentarDto[]) al.toArray(dtos);

	}

	private void setArtikelkommentardruckFromArtikelkommentardruckDto(Artikelkommentardruck artikelkommentardruck,
			ArtikelkommentardruckDto artikelkommentardruckDto) {
		artikelkommentardruck.setArtikelIId(artikelkommentardruckDto.getArtikelIId());
		artikelkommentardruck.setArtikelkommentarIId(artikelkommentardruckDto.getArtikelkommentarIId());
		artikelkommentardruck.setBelegartCNr(artikelkommentardruckDto.getBelegartCNr());
		em.merge(artikelkommentardruck);
		em.flush();
	}

	private ArtikelkommentardruckDto assembleArtikelkommentardruckDto(Artikelkommentardruck artikelkommentardruck) {
		return ArtikelkommentardruckDtoAssembler.createDto(artikelkommentardruck);
	}

	private ArtikelkommentardruckDto[] assembleArtikelkommentardruckDtos(Collection<?> artikelkommentardrucks) {
		List<ArtikelkommentardruckDto> list = new ArrayList<ArtikelkommentardruckDto>();
		if (artikelkommentardrucks != null) {
			Iterator<?> iterator = artikelkommentardrucks.iterator();
			while (iterator.hasNext()) {
				Artikelkommentardruck artikelkommentardruck = (Artikelkommentardruck) iterator.next();
				list.add(assembleArtikelkommentardruckDto(artikelkommentardruck));
			}
		}
		ArtikelkommentardruckDto[] returnArray = new ArtikelkommentardruckDto[list.size()];
		return (ArtikelkommentardruckDto[]) list.toArray(returnArray);
	}

	@Override
	public List<ArtikelkommentarDto> artikelkommentarFindByArtikelIIdFull(Integer artikelIId,
			TheClientDto theClientDto) {
		ArtikelkommentarDto[] kommentare = artikelkommentarFindByArtikelIId(artikelIId, theClientDto);
		if (kommentare == null)
			return new ArrayList<ArtikelkommentarDto>();

		for (ArtikelkommentarDto kommentarDto : kommentare) {
			List<Artikelkommentardruck> kommentardrucke = ArtikelkommentardruckQuery
					.listByArtikelIIdArtikelkommentarIId(em, artikelIId, kommentarDto.getIId());
			ArtikelkommentardruckDto[] kommentardruckeDtos = assembleArtikelkommentardruckDtos(kommentardrucke);
			kommentarDto.setArtikelkommentardruckDto(kommentardruckeDtos);
		}

		return Arrays.asList(kommentare);
	}
}
