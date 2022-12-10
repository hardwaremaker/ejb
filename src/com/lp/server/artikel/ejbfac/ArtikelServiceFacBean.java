package com.lp.server.artikel.ejbfac;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Artikelkommentar;
import com.lp.server.artikel.ejb.Artikellager;
import com.lp.server.artikel.ejb.ArtikellagerPK;
import com.lp.server.artikel.ejb.Artikellieferant;
import com.lp.server.artikel.ejb.Artikelspr;
import com.lp.server.artikel.ejb.Dateiverweis;
import com.lp.server.artikel.ejb.Geometrie;
import com.lp.server.artikel.ejb.Hersteller;
import com.lp.server.artikel.ejb.Inventurliste;
import com.lp.server.artikel.ejb.Inventurstand;
import com.lp.server.artikel.ejb.Montage;
import com.lp.server.artikel.ejb.Sollverkauf;
import com.lp.server.artikel.ejb.Verpackung;
import com.lp.server.artikel.ejb.Waffenausfuehrung;
import com.lp.server.artikel.ejb.Waffenkaliber;
import com.lp.server.artikel.ejb.Waffenkategorie;
import com.lp.server.artikel.ejb.Waffentyp;
import com.lp.server.artikel.ejb.WaffentypFein;
import com.lp.server.artikel.ejb.Waffenzusatz;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelServiceFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.DateiverweisDto;
import com.lp.server.artikel.service.DateiverweisDtoAssembler;
import com.lp.server.artikel.service.LumiQuoteArtikelDto;
import com.lp.server.artikel.service.WaffenausfuehrungDto;
import com.lp.server.artikel.service.WaffenausfuehrungDtoAssembler;
import com.lp.server.artikel.service.WaffenkaliberDto;
import com.lp.server.artikel.service.WaffenkaliberDtoAssembler;
import com.lp.server.artikel.service.WaffenkategorieDto;
import com.lp.server.artikel.service.WaffenkategorieDtoAssembler;
import com.lp.server.artikel.service.WaffentypDto;
import com.lp.server.artikel.service.WaffentypDtoAssembler;
import com.lp.server.artikel.service.WaffentypFeinDto;
import com.lp.server.artikel.service.WaffentypFeinDtoAssembler;
import com.lp.server.artikel.service.WaffenzusatzDto;
import com.lp.server.artikel.service.WaffenzusatzDtoAssembler;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragseriennrn;
import com.lp.server.auftrag.service.AuftragseriennrnDto;
import com.lp.server.partner.ejb.Kundesoko;
import com.lp.server.stueckliste.ejb.Stueckliste;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableWorkbook;

@Stateless
public class ArtikelServiceFacBean extends Facade implements ArtikelServiceFac {

	public void zusammenfuehrenHersteller(Integer herstellerIId_Quelle, Integer herstellerIId_Ziel,
			TheClientDto theClientDto) {
		Hersteller herstellerQuelle = em.find(Hersteller.class, herstellerIId_Quelle);
		Hersteller herstellerZiel = em.find(Hersteller.class, herstellerIId_Ziel);

		Query query = em.createQuery("UPDATE Geraet bean SET bean.herstellerIId = " + herstellerIId_Ziel
				+ " WHERE bean.herstellerIId = " + herstellerIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Artikel bean SET bean.herstellerIId = " + herstellerIId_Ziel
				+ " WHERE bean.herstellerIId = " + herstellerIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Lagerbewegung bean SET bean.herstellerIId = " + herstellerIId_Ziel
				+ " WHERE bean.herstellerIId = " + herstellerIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Einkaufsangebotposition bean SET bean.herstellerIId = " + herstellerIId_Ziel
				+ " WHERE bean.herstellerIId = " + herstellerIId_Quelle);
		query.executeUpdate();

		em.remove(herstellerQuelle);

	}

	public List<AuftragseriennrnDto> zusammenfuehrenArtikelPruefeAuftragsnnr(Integer artikelIId_Quelle,
			Integer artikelIId_Ziel, TheClientDto theClientDto) {

		List<AuftragseriennrnDto> l = new ArrayList<AuftragseriennrnDto>();

		String queryString = "SELECT l.c_seriennr FROM FLRAuftragseriennrn l WHERE  l.artikel_i_id IN("
				+ artikelIId_Quelle + "," + artikelIId_Ziel
				+ ") GROUP BY l.c_seriennr, l.version_nr having count( l.c_seriennr)>1";

		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query queryFLR = session.createQuery(queryString);
		List<?> resultList = queryFLR.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {

			String cSeriennr = (String) resultListIterator.next();

			String sQuery2 = "SELECT l FROM FLRAuftragseriennrn l WHERE  l.artikel_i_id IN(" + artikelIId_Quelle + ","
					+ artikelIId_Ziel + ") AND l.c_seriennr='" + cSeriennr + "'";

			org.hibernate.Session session2 = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query query2 = session2.createQuery(sQuery2);
			List<?> resultList2 = query2.list();
			Iterator<?> resultListIterator2 = resultList2.iterator();

			while (resultListIterator2.hasNext()) {
				FLRAuftragseriennrn as = (FLRAuftragseriennrn) resultListIterator2.next();

				try {
					AuftragseriennrnDto asDto = getAuftragpositionFac().auftragseriennrnFindByPrimaryKey(as.getI_id(),
							theClientDto);
					asDto.setAuftragCNr_NOT_IN_DB(as.getFlrauftragposition().getFlrauftrag().getC_nr());

					l.add(asDto);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return l;

	}

	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	public void pflegeWiederbeschaffungszeiten(Integer artikelgruppeIId, Integer artikelklasseIId, String artiklenrVon,
			String artiklenrBis, TheClientDto theClientDto) {

		int iEinheit = 1;
		try {
			ParametermandantDto parameterDto = (ParametermandantDto) getParameterFac().parametermandantFindByPrimaryKey(
					ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT, ParameterFac.KATEGORIE_ARTIKEL,
					theClientDto.getMandant());

			if (parameterDto.getCWert() != null) {
				if (parameterDto.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
					iEinheit = 7;
				}

			}

			Session session = FLRSessionFactory.getFactory().openSession();

			String queryString = "  SELECT distinct al.artikel_i_id,al.lieferant_i_id,al.gebinde_i_id FROM FLRArtikellieferant AS al WHERE al.flrartikel.mandant_c_nr='"
					+ theClientDto.getMandant() + "' ";

			if (artiklenrVon != null) {
				queryString += " AND al.flrartikel.c_nr >='" + artiklenrVon + "'";
			}
			if (artiklenrBis != null) {
				queryString += " AND al.flrartikel.c_nr <='" + Helper.fitString2Length(artiklenrBis, 25, '_') + "'";
			}
			if (artikelgruppeIId != null) {
				queryString += " AND al.flrartikel.flrartikelgruppe.i_id=" + artikelgruppeIId + " ";
			}
			if (artikelklasseIId != null) {
				queryString += " AND al.flrartikel.flrartikelklasse.i_id=" + artikelklasseIId + " ";
			}

			// queryString += " ORDER BY al.flrartikel.c_nr ASC";

			org.hibernate.Query query = session.createQuery(queryString);
			List results = query.list();
			Iterator resultListIterator = results.iterator();

			while (resultListIterator.hasNext()) {

				Object[] oZeile = (Object[]) resultListIterator.next();

				Integer artikelIId = (Integer) oZeile[0];

				Integer lieferantIId = (Integer) oZeile[1];
				Integer gebindeIId = (Integer) oZeile[2];

				Integer iMoral = getBestellungFac().getWiederbeschaffungsmoralEinesArtikels(artikelIId, lieferantIId,
						theClientDto);
				if (iMoral != null) {
					int wbzBerechnet = iMoral / iEinheit;

					ArtikellieferantDto dto = getArtikelFac()
							.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(artikelIId,
									lieferantIId, getDate(), gebindeIId, theClientDto);

					if (dto != null) {
						Artikellieferant artikellieferant = em.find(Artikellieferant.class, dto.getIId());
						artikellieferant.setIWiederbeschaffungszeit(wbzBerechnet);
						em.merge(artikellieferant);
						em.flush();
					}

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(1000)
	public void zusammenfuehrenArtikel(Integer artikelIId_Quelle, Integer artikelIId_Ziel, TheClientDto theClientDto) {

		Artikel artikelQuelle = em.find(Artikel.class, artikelIId_Quelle);
		Artikel artikelZiel = em.find(Artikel.class, artikelIId_Ziel);
		int iNachkommastellenPReisEK = 2;

		try {
			iNachkommastellenPReisEK = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Hier alle, welche ausgetauscht werden

		// VK
		Query query = em.createQuery("UPDATE Auftragposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Rechnungposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Lieferscheinposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Angebotposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Artikelreservierung pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		// EK

		query = em.createQuery("UPDATE Anfrageposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Bestellposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		// Agstkl

		query = em.createQuery("UPDATE Agstklarbeitsplan pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Agstklposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Einkaufsangebotposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		// PERS

		query = em.createQuery("UPDATE Personalverfuegbarkeit pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Zeitdaten pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Zeitdatenpruefen pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Zeitverteilung pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		Query queryartikelLF = em.createNamedQuery("ArtikellieferantfindByArtikelIId");

		queryartikelLF.setParameter(1, artikelIId_Quelle);
		Collection<?> allArtikelLF = queryartikelLF.getResultList();
		Iterator<?> iterArtikelLF = allArtikelLF.iterator();
		while (iterArtikelLF.hasNext()) {
			Artikellieferant lf = (Artikellieferant) iterArtikelLF.next();
			query = em.createQuery("DELETE Artikellieferantstaffel x WHERE x.artikellieferantIId = " + lf.getIId());
			query.executeUpdate();
			em.remove(lf);

		}

		query = em.createQuery("UPDATE Artikellog pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		// INVENTUR

		Query queryinventurliste = em.createNamedQuery("InventurlistefindByArtikelIId");
		queryinventurliste.setParameter(1, artikelIId_Quelle);
		Collection<?> allInvnturliste = queryinventurliste.getResultList();
		Iterator<?> iterallInvnturliste = allInvnturliste.iterator();
		while (iterallInvnturliste.hasNext()) {
			Inventurliste inventurliste = (Inventurliste) iterallInvnturliste.next();

			// Wenn nicht SNR/CHNR-Tragend dann

			// nur ersetzen, wenn kein anderer Eintrag, ansonsten addieren

			if (inventurliste.getCSeriennrchargennr() == null) {
				Query qtemp = em.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIId");
				qtemp.setParameter(1, inventurliste.getInventurIId());
				qtemp.setParameter(2, inventurliste.getLagerIId());
				qtemp.setParameter(3, artikelIId_Ziel);

				Collection c = qtemp.getResultList();

				if (c.size() > 0) {
					Inventurliste inventurlisteZielartikel = (Inventurliste) c.iterator().next();

					inventurlisteZielartikel.setNInventurmenge(
							inventurlisteZielartikel.getNInventurmenge().add(inventurliste.getNInventurmenge()));
					em.merge(inventurlisteZielartikel);

					// Protokoll updaten
					query = em.createQuery("UPDATE Inventurprotokoll pos SET pos.inventurlisteIId = "
							+ inventurlisteZielartikel.getIId() + " WHERE pos.inventurlisteIId = "
							+ inventurliste.getIId());
					query.executeUpdate();

					em.remove(inventurliste);
				} else {
					inventurliste.setArtikelIId(artikelIId_Ziel);
					em.merge(inventurliste);
				}

			} else {

				Query qtemp = em.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr");
				qtemp.setParameter(1, inventurliste.getInventurIId());
				qtemp.setParameter(2, inventurliste.getLagerIId());
				qtemp.setParameter(3, artikelIId_Ziel);
				qtemp.setParameter(4, inventurliste.getCSeriennrchargennr());

				Collection c = qtemp.getResultList();

				if (c.size() > 0) {
					Inventurliste inventurlisteZielartikel = (Inventurliste) c.iterator().next();

					inventurlisteZielartikel.setNInventurmenge(
							inventurlisteZielartikel.getNInventurmenge().add(inventurliste.getNInventurmenge()));
					em.merge(inventurlisteZielartikel);

					// Protokoll updaten
					query = em.createQuery("UPDATE Inventurprotokoll pos SET pos.inventurlisteIId = "
							+ inventurlisteZielartikel.getIId() + " WHERE pos.inventurlisteIId = "
							+ inventurliste.getIId());
					query.executeUpdate();

					em.remove(inventurliste);
				} else {
					inventurliste.setArtikelIId(artikelIId_Ziel);
					em.merge(inventurliste);
				}

			}

		}

		// Inventurstand
		Query queryInventurstand = em.createNamedQuery("InventurstandfindByArtikelIId");
		queryInventurstand.setParameter(1, artikelIId_Quelle);
		Collection<?> allInventurstand = queryInventurstand.getResultList();
		Iterator<?> iterInventurstand = allInventurstand.iterator();
		while (iterInventurstand.hasNext()) {
			Inventurstand inventurstand = (Inventurstand) iterInventurstand.next();

			BigDecimal bdWertQuellartikel = inventurstand.getNInventurmenge()
					.multiply(inventurstand.getNInventurpreis());

			// Gibts schon einen Eintrag beim Zielartikel dann addieren
			Query queryInventurstandZielartikel = em
					.createNamedQuery("InventurstandfindByInventurIIdArtikelIIdLagerIId");
			queryInventurstandZielartikel.setParameter(1, inventurstand.getInventurIId());
			queryInventurstandZielartikel.setParameter(2, artikelIId_Ziel);
			queryInventurstandZielartikel.setParameter(3, inventurstand.getLagerIId());
			Collection<?> allInventurstandZielartikel = queryInventurstandZielartikel.getResultList();
			Iterator<?> iterInventurstandZielartikel = allInventurstandZielartikel.iterator();
			if (iterInventurstandZielartikel.hasNext()) {
				Inventurstand inventurstandZielartikel = (Inventurstand) iterInventurstandZielartikel.next();

				BigDecimal bdWertZielartikel = inventurstandZielartikel.getNInventurmenge()
						.multiply(inventurstandZielartikel.getNInventurpreis());

				BigDecimal bzNeueZielmenge = inventurstand.getNInventurmenge()
						.add(inventurstandZielartikel.getNInventurmenge());

				inventurstandZielartikel.setNInventurmenge(bzNeueZielmenge);

				if (bzNeueZielmenge.doubleValue() != 0) {

					BigDecimal bdNeuerPreis = bdWertQuellartikel.add(bdWertZielartikel).divide(bzNeueZielmenge,
							iNachkommastellenPReisEK, BigDecimal.ROUND_HALF_EVEN);

					inventurstandZielartikel.setNInventurpreis(bdNeuerPreis);
				}

				em.remove(inventurstand);

			} else {
				inventurstand.setArtikelIId(artikelIId_Ziel);
				em.merge(inventurstand);
			}

		}

		query = em.createQuery("UPDATE Handlagerbewegung pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		Query queryartikellager = em.createNamedQuery("ArtikellagerfindByArtikelIId");
		queryartikellager.setParameter(1, artikelIId_Quelle);
		Collection<?> allArtikellager = queryartikellager.getResultList();
		Iterator<?> iterArtikellager = allArtikellager.iterator();
		while (iterArtikellager.hasNext()) {
			Artikellager artikellagerQuellartikel = (Artikellager) iterArtikellager.next();

			ArtikellagerPK artikellagerPK = new ArtikellagerPK();
			artikellagerPK.setArtikelIId(artikelIId_Ziel);
			artikellagerPK.setLagerIId(artikellagerQuellartikel.getPk().getLagerIId());
			Artikellager artikellagerZielartikel = em.find(Artikellager.class, artikellagerPK);
			if (artikellagerZielartikel == null) {
				artikellagerZielartikel = new Artikellager(artikelIId_Ziel,
						artikellagerQuellartikel.getPk().getLagerIId(), artikellagerQuellartikel.getMandantCNr());
				artikellagerZielartikel.setNLagerstand(artikellagerQuellartikel.getNLagerstand());
				artikellagerZielartikel.setNGestehungspreis(artikellagerQuellartikel.getNGestehungspreis());
				em.persist(artikellagerZielartikel);
				em.flush();
			} else {
				artikellagerZielartikel.setNLagerstand(
						artikellagerZielartikel.getNLagerstand().add(artikellagerQuellartikel.getNLagerstand()));
			}

			// LAGER
			query = em.createQuery("UPDATE Lagerbewegung pos SET pos.artikelIId = " + artikelIId_Ziel
					+ " WHERE pos.artikelIId = " + artikelIId_Quelle + " AND pos.lagerIId="
					+ artikellagerQuellartikel.getPk().getLagerIId());
			query.executeUpdate();

			em.remove(artikellagerQuellartikel);
			em.flush();
		}

		getLagerFac().gestehungspreisEinesArtikelNeuBerechnen(artikelIId_Ziel, theClientDto);

		// Pruefen, ob SNRS nun doppelt auf Lager waeren

		if (Helper.short2boolean(artikelQuelle.getBSeriennrtragend())) {

			org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

			String queryString = "SELECT distinct l.c_seriennrchargennr, l.lager_i_id FROM FLRLagerbewegung l WHERE l.artikel_i_id="
					+ artikelIId_Ziel;

			org.hibernate.Query queryFLR = session.createQuery(queryString);
			List<?> resultList = queryFLR.list();
			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();

				String snr = (String) o[0];
				Integer lagerIId = (Integer) o[1];

				// Wenn Artikel SNR-Tragend und die Seriennummer schon einmal
				// gebucht worden ist, dann Fehler

				try {
					BigDecimal d = getLagerFac().getMengeAufLager(artikelIId_Ziel, lagerIId, snr, theClientDto);
					if (d.doubleValue() > 1) {
						ArrayList al = new ArrayList();
						al.add(snr);
						throw new EJBExceptionLP(EJBExceptionLP.LAGER_SERIENNUMMER_SCHON_VORHANDEN, al,
								new Exception("LAGER_SERIENNUMMER_SCHON_VORHANDEN"));
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		}

		// FERTIGUNG

		query = em.createQuery("UPDATE Lossollmaterial pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Lossollarbeitsplan pos SET pos.artikelIIdTaetigkeit = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdTaetigkeit = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Stuecklisteposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Stuecklistearbeitsplan pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Fasessioneintrag pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Fasessioneintrag pos SET pos.artikelIIdOffenerag = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdOffenerag = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Verrechnungsmodell pos SET pos.artikelIIdReisespesen = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdReisespesen = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Verrechnungsmodell pos SET pos.artikelIIdTelefon = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdTelefon = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Verrechnungsmodell pos SET pos.artikelIIdReisekilometer = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdReisekilometer = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Verrechnungsmodell pos SET pos.artikelIIdEr = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdEr = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Verrechnungsmodelltag pos SET pos.artikelIIdGebucht = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdGebucht = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Verrechnungsmodelltag pos SET pos.artikelIIdZuverrechnen = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdZuverrechnen = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Auftragseriennrn pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Bestellvorschlag pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE EingangsrechnungKontierung pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Forecastposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Bedarfsuebernahme pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Wartungsschritte pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Wartungsliste pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Inserat pos SET pos.artikelIIdInseratart = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdInseratart = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Inseratartikel pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Kassaimport pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Kuecheumrechnung pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Speiseplanposition pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Ausliefervorschlag pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Artikelzulage pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Artikelzuschlag pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Liefermengen pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Maschine pos SET pos.artikelIIdVerrechnen = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdVerrechnen = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Maschinenzeitdatenverrechnet pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Zeitdatenverrechnet pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Telefonzeitenverrechnet pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Projekt pos SET pos.artikelIId = " + artikelIId_Ziel + " WHERE pos.artikelIId = "
				+ artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Projekttaetigkeit pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Mmz pos SET pos.artikelIId = " + artikelIId_Ziel + " WHERE pos.artikelIId = "
				+ artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Reklamation pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Montageart pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Pruefkombination pos SET pos.artikelIIdLitze = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdLitze = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Pruefkombination pos SET pos.artikelIIdLitze2 = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdLitze2 = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Pruefkombination pos SET pos.artikelIIdKontakt = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdKontakt = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Posersatz pos SET pos.artikelIIdErsatz = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdErsatz = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Artikel pos SET pos.artikelIIdErsatz = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdErsatz = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Artikel pos SET pos.artikelIIdZugehoerig = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdZugehoerig = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Artikelbestellt pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Artikelfehlmenge pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Trumphtopslog pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("UPDATE Trumphtopslog pos SET pos.artikelIIdMaterial = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdMaterial = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Shopgruppe pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE WebshopArtikel pos SET pos.artikelId = " + artikelIId_Ziel
				+ " WHERE pos.artikelId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Rahmenbedarfe pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Geraetesnr pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Ersatztypen pos SET pos.artikelIIdErsatz = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdErsatz = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Zugehoerige pos SET pos.artikelIIdZugehoerig = " + artikelIId_Ziel
				+ " WHERE pos.artikelIIdZugehoerig = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("UPDATE Artikelsnrchnr pos SET pos.artikelIId = " + artikelIId_Ziel
				+ " WHERE pos.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		// Ab hier, welche die geloescht werden

		// KOMMENTARE
		query = em.createQuery("DELETE Artikelkommentardruck x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		Query queryartikelkommentar = em.createNamedQuery("ArtikelkommentarfindByArtikelIId");
		queryartikelkommentar.setParameter(1, artikelIId_Quelle);
		Collection<?> allArtikelkommentar = queryartikelkommentar.getResultList();
		Iterator<?> iterArtikelkommentar = allArtikelkommentar.iterator();
		while (iterArtikelkommentar.hasNext()) {
			Artikelkommentar ak = (Artikelkommentar) iterArtikelkommentar.next();
			query = em.createQuery("DELETE Artikelkommentarspr x WHERE x.pk.artikelkommentarIId = " + ak.getIId());
			query.executeUpdate();
			em.remove(ak);

		}

		// ENTFERNEN
		query = em.createQuery("DELETE Artikellagerplaetze x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		// VK-PREISE
		query = em.createQuery("DELETE Vkpfmengenstaffel x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("DELETE VkPreisfindungPreisliste x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();
		query = em.createQuery("DELETE VkPreisfindungEinzelverkaufspreis x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		// SP8745
		Query queryartikelKundesoko = em.createNamedQuery("KundesokofindByArtikelIId");
		queryartikelKundesoko.setParameter(1, artikelIId_Quelle);
		Collection<?> allArtikelKundesoko = queryartikelKundesoko.getResultList();
		Iterator<?> iterArtikelKundesoko = allArtikelKundesoko.iterator();
		while (iterArtikelKundesoko.hasNext()) {
			Kundesoko ks = (Kundesoko) iterArtikelKundesoko.next();
			query = em.createQuery("DELETE Kundesokomengenstaffel x WHERE x.kundesokoIId = " + ks.getIId());
			query.executeUpdate();
			em.remove(ks);

		}

		query = em.createQuery("DELETE Artikelshopgruppe x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("DELETE Artikelalergen x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("DELETE Artikelsperren x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("DELETE Einkaufsean x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("DELETE Katalog x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("DELETE Ersatztypen x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		query = em.createQuery("DELETE Zugehoerige x WHERE x.artikelIId = " + artikelIId_Quelle);
		query.executeUpdate();

		// Nun versuchen Quelle zu loeschen

		Geometrie geometrie = em.find(Geometrie.class, artikelIId_Quelle);
		if (geometrie != null) {
			em.remove(geometrie);
		}

		Sollverkauf sollverkauf = em.find(Sollverkauf.class, artikelIId_Quelle);
		if (sollverkauf != null) {
			em.remove(sollverkauf);
		}

		Montage montage = em.find(Montage.class, artikelIId_Quelle);
		if (montage != null) {
			em.remove(montage);
		}

		Verpackung verpackung = em.find(Verpackung.class, artikelIId_Quelle);
		if (verpackung != null) {
			em.remove(verpackung);
		}

		Query queryspr = em.createNamedQuery("ArtikelsprfindByArtikelIId");
		queryspr.setParameter(1, artikelIId_Quelle);
		Collection<?> allArtikelspr = queryspr.getResultList();
		Iterator<?> iter = allArtikelspr.iterator();
		while (iter.hasNext()) {
			Artikelspr artikelsprTemp = (Artikelspr) iter.next();
			em.remove(artikelsprTemp);
		}

		// STUECKLISTE zusammenfuehren

		StuecklisteDto[] stklDtos = getStuecklisteFac().stuecklisteFindByArtikelIId(artikelIId_Quelle);
		for (int i = 0; i < stklDtos.length; i++) {
			StuecklisteDto stklDtoQuelle = stklDtos[i];

			Stueckliste stuecklisteQuelle = em.find(Stueckliste.class, stklDtoQuelle.getIId());

			query = em.createQuery("DELETE Stklagerentnahme x WHERE x.stuecklisteIId = " + stklDtoQuelle.getIId());
			query.executeUpdate();

			StuecklisteDto[] stklDtosZiel = getStuecklisteFac().stuecklisteFindByArtikelIId(artikelIId_Ziel);

			if (stklDtosZiel.length > 0) {
				for (int s = 0; s < stklDtosZiel.length; s++) {
					StuecklisteDto stklDtoZiel = stklDtosZiel[s];

					query = em.createQuery("UPDATE Los pos SET pos.stuecklisteIId = " + stklDtoZiel.getIId()
							+ " WHERE pos.stuecklisteIId = " + stklDtoQuelle.getIId());
					query.executeUpdate();

					query = em.createQuery("UPDATE Stuecklistearbeitsplan pos SET pos.stuecklisteIId = "
							+ stklDtoZiel.getIId() + " WHERE pos.stuecklisteIId = " + stklDtoQuelle.getIId());
					query.executeUpdate();

					query = em.createQuery("UPDATE Speiseplan pos SET pos.stuecklisteIId = " + stklDtoZiel.getIId()
							+ " WHERE pos.stuecklisteIId = " + stklDtoQuelle.getIId());
					query.executeUpdate();

				}
			}

			em.remove(stuecklisteQuelle);
			em.flush();
		}

		// LOG EINTRAG erzeugen

		getArtikelFac().artikelAenderungLoggen(artikelIId_Ziel, ArtikelFac.ARTIKEL_LOG_ZUSAMMENGEFUEHRT,
				artikelQuelle.getCNr(), artikelZiel.getCNr(), theClientDto);

		em.remove(artikelQuelle);

	}

	public Integer createWaffenkaliber(WaffenkaliberDto dto) {

		try {
			Query query = em.createNamedQuery("WaffenkaliberFindByCNr");
			query.setParameter(1, dto.getCNr());

			Waffenkaliber doppelt = (Waffenkaliber) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WAFFENKALIBER.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WAFFENKALIBER);
			dto.setIId(pk);

			Waffenkaliber bean = new Waffenkaliber(dto.getIId(), dto.getCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setWaffenkaliberFromWaffenkaliberDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateWaffenkaliber(WaffenkaliberDto dto) {
		Waffenkaliber bean = em.find(Waffenkaliber.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("WaffenkaliberFindByCNr");
			query.setParameter(1, dto.getCNr());

			Integer iIdVorhanden = ((Waffenkaliber) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_WAFFENKALIBER.C_NR"));
			}
		} catch (NoResultException ex) {

		}

		setWaffenkaliberFromWaffenkaliberDto(bean, dto);
	}

	public void removeWaffenkaliber(WaffenkaliberDto dto) {
		Waffenkaliber toRemove = em.find(Waffenkaliber.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public WaffenkaliberDto waffenkaliberFindByPrimaryKey(Integer iId) {
		Waffenkaliber bean = em.find(Waffenkaliber.class, iId);
		return WaffenkaliberDtoAssembler.createDto(bean);
	}

	private void setWaffenkaliberFromWaffenkaliberDto(Waffenkaliber bean, WaffenkaliberDto dto) {
		bean.setCNr(dto.getCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	// ---
	public Integer createWaffenausfuehrung(WaffenausfuehrungDto dto) {

		try {
			Query query = em.createNamedQuery("WaffenausfuehrungFindByCNr");
			query.setParameter(1, dto.getCNr());

			Waffenausfuehrung doppelt = (Waffenausfuehrung) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_WAFFENAUSFUEHRUNG.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WAFFENAUSFUEHRUNG);
			dto.setIId(pk);

			Waffenausfuehrung bean = new Waffenausfuehrung(dto.getIId(), dto.getCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setWaffenausfuehrungFromWaffenausfuehrungDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateWaffenausfuehrung(WaffenausfuehrungDto dto) {
		Waffenausfuehrung bean = em.find(Waffenausfuehrung.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("WaffenausfuehrungFindByCNr");
			query.setParameter(1, dto.getCNr());

			Integer iIdVorhanden = ((Waffenausfuehrung) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_WAFFENAUSFUEHRUNG.C_NR"));
			}
		} catch (NoResultException ex) {

		}

		setWaffenausfuehrungFromWaffenausfuehrungDto(bean, dto);
	}

	public void removeWaffenausfuehrung(WaffenausfuehrungDto dto) {
		Waffenausfuehrung toRemove = em.find(Waffenausfuehrung.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public WaffenausfuehrungDto waffenausfuehrungFindByPrimaryKey(Integer iId) {
		Waffenausfuehrung bean = em.find(Waffenausfuehrung.class, iId);
		return WaffenausfuehrungDtoAssembler.createDto(bean);
	}

	private void setWaffenausfuehrungFromWaffenausfuehrungDto(Waffenausfuehrung bean, WaffenausfuehrungDto dto) {
		bean.setCNr(dto.getCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	// ---
	public Integer createWaffentyp(WaffentypDto dto) {

		try {
			Query query = em.createNamedQuery("WaffentypFindByCNr");
			query.setParameter(1, dto.getCNr());

			Waffentyp doppelt = (Waffentyp) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WAFFENTYP.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WAFFENTYP);
			dto.setIId(pk);

			Waffentyp bean = new Waffentyp(dto.getIId(), dto.getCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setWaffentypFromWaffentypDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateWaffentyp(WaffentypDto dto) {
		Waffentyp bean = em.find(Waffentyp.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("WaffentypFindByCNr");
			query.setParameter(1, dto.getCNr());

			Integer iIdVorhanden = ((Waffentyp) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WAFFENTYP.C_NR"));
			}
		} catch (NoResultException ex) {

		}

		setWaffentypFromWaffentypDto(bean, dto);
	}

	public void removeWaffentyp(WaffentypDto dto) {
		Waffentyp toRemove = em.find(Waffentyp.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public WaffentypDto waffentypFindByPrimaryKey(Integer iId) {
		Waffentyp bean = em.find(Waffentyp.class, iId);
		return WaffentypDtoAssembler.createDto(bean);
	}

	private void setWaffentypFromWaffentypDto(Waffentyp bean, WaffentypDto dto) {
		bean.setCNr(dto.getCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	// ---
	public Integer createWaffentypFein(WaffentypFeinDto dto) {

		try {
			Query query = em.createNamedQuery("WaffentypFeinFindByCNr");
			query.setParameter(1, dto.getCNr());

			WaffentypFein doppelt = (WaffentypFein) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WAFFENTYPFEIN.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WAFFENTYPFEIN);
			dto.setIId(pk);

			WaffentypFein bean = new WaffentypFein(dto.getIId(), dto.getCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setWaffentypFeinFromWaffentypFeinDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateWaffentypFein(WaffentypFeinDto dto) {
		WaffentypFein bean = em.find(WaffentypFein.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("WaffentypFeinFindByCNr");
			query.setParameter(1, dto.getCNr());

			Integer iIdVorhanden = ((WaffentypFein) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_WAFFENTYPFEIN.C_NR"));
			}
		} catch (NoResultException ex) {

		}

		setWaffentypFeinFromWaffentypFeinDto(bean, dto);
	}

	public void removeWaffentypFein(WaffentypFeinDto dto) {
		WaffentypFein toRemove = em.find(WaffentypFein.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public WaffentypFeinDto waffentypFeinFindByPrimaryKey(Integer iId) {
		WaffentypFein bean = em.find(WaffentypFein.class, iId);
		return WaffentypFeinDtoAssembler.createDto(bean);
	}

	private void setWaffentypFeinFromWaffentypFeinDto(WaffentypFein bean, WaffentypFeinDto dto) {
		bean.setCNr(dto.getCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	// --
	public Integer createWaffenkategorie(WaffenkategorieDto dto) {

		try {
			Query query = em.createNamedQuery("WaffenkategorieFindByCNr");
			query.setParameter(1, dto.getCNr());

			Waffenkategorie doppelt = (Waffenkategorie) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WAFFENKATEGORIE.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WAFFENKATEGORIE);
			dto.setIId(pk);

			Waffenkategorie bean = new Waffenkategorie(dto.getIId(), dto.getCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setWaffenkategorieFromWaffenkategorieDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateWaffenkategorie(WaffenkategorieDto dto) {
		Waffenkategorie bean = em.find(Waffenkategorie.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("WaffenkategorieFindByCNr");
			query.setParameter(1, dto.getCNr());

			Integer iIdVorhanden = ((Waffenkategorie) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_WAFFENKATEGORIE.C_NR"));
			}
		} catch (NoResultException ex) {

		}

		setWaffenkategorieFromWaffenkategorieDto(bean, dto);
	}

	public void removeWaffenkategorie(WaffenkategorieDto dto) {
		Waffenkategorie toRemove = em.find(Waffenkategorie.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public WaffenkategorieDto waffenkategorieFindByPrimaryKey(Integer iId) {
		Waffenkategorie bean = em.find(Waffenkategorie.class, iId);
		return WaffenkategorieDtoAssembler.createDto(bean);
	}
	// --

	public Integer createWaffenzusatz(WaffenzusatzDto dto) {

		try {
			Query query = em.createNamedQuery("WaffenzusatzFindByCNr");
			query.setParameter(1, dto.getCNr());

			Waffenzusatz doppelt = (Waffenzusatz) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WAFFENZUSATZ.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WAFFENZUSATZ);
			dto.setIId(pk);

			Waffenzusatz bean = new Waffenzusatz(dto.getIId(), dto.getCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setWaffenzusatzFromWaffenzusatzDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateWaffenzusatz(WaffenzusatzDto dto) {
		Waffenzusatz bean = em.find(Waffenzusatz.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("WaffenzusatzFindByCNr");
			query.setParameter(1, dto.getCNr());

			Integer iIdVorhanden = ((Waffenzusatz) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WAFFENZUSATZ.C_NR"));
			}
		} catch (NoResultException ex) {

		}

		setWaffenzusatzFromWaffenzusatzDto(bean, dto);
	}

	public void removeWaffenzusatz(WaffenzusatzDto dto) {
		Waffenzusatz toRemove = em.find(Waffenzusatz.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<LumiQuoteArtikelDto> getArtikelDatenForLumiquote(String artikelFilter,TheClientDto theClientDto) {

		ArrayList<LumiQuoteArtikelDto> alDaten = new ArrayList<LumiQuoteArtikelDto>();

		String filter="";
		
		if(artikelFilter!=null) {
			filter="LIKE UPPER('"+artikelFilter.toUpperCase()+"%')";
		}
		
		
		Session session = FLRSessionFactory.getFactory().openSession();
		String s = "SELECT A.C_NR AS IPN, COALESCE(A.C_ARTIKELNRHERSTELLER,'') AS MPN, COALESCE(HST.C_NR,'') AS Hersteller,  COALESCE(SPR.C_BEZ,'') AS Beschreibung, COALESCE(SPR.C_ZBEZ,'') AS Beschreibung2,  COALESCE(SPR.C_ZBEZ2,'') AS Beschreibung3,   COALESCE(V.C_BAUFORM,'') AS Bauform "
				+ "		FROM WW_ARTIKEL A LEFT OUTER JOIN WW_ARTIKELSPR SPR ON SPR.ARTIKEL_I_ID=A.I_ID "
				+ "		LEFT OUTER JOIN WW_HERSTELLER HST ON HST.I_ID=A.HERSTELLER_I_ID "
				+ "		LEFT OUTER JOIN WW_VERPACKUNG V ON V.ARTIKEL_I_ID=A.I_ID  "
				+ "		WHERE A.ARTIKELART_C_NR='Artikel' AND A.C_NR "+filter+" AND A.B_VERSTECKT = 0 AND SPR.LOCALE_C_NR='deAT'  ORDER BY A.C_NR";
		org.hibernate.Query query = session.createSQLQuery(s);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			Object[] oZeile = (Object[]) resultListIterator.next();
			LumiQuoteArtikelDto lqZeile = new LumiQuoteArtikelDto();
			lqZeile.setIpn((String) oZeile[0]);

			lqZeile.setHersteller((String) oZeile[2]);

			// Beschreibung
			String cbez = (String) oZeile[3];
			String cbez2 = (String) oZeile[4];
			String cbez3 = (String) oZeile[5];

			String beschreibung = cbez;
			if (cbez2 != null) {
				beschreibung = beschreibung + " " + cbez2;
			} else if (cbez3 != null) {
				beschreibung = beschreibung + " " + cbez3;
			}

			lqZeile.setBeschreibung(beschreibung);

			lqZeile.setBauform((String) oZeile[6]);

			String mpn = (String) oZeile[1];

			if (mpn != null && mpn.indexOf('|') > 0) {
				String[] mpns = mpn.split("\\|", 2);

				for (int i = 0; i < mpns.length; i++) {

					String hstnr = mpns[i];

					if (hstnr != null) {
						hstnr = hstnr.trim();
					}

					LumiQuoteArtikelDto lqZeileZus = LumiQuoteArtikelDto.clone(lqZeile);
					lqZeileZus.setMpn(hstnr);

					alDaten.add(lqZeileZus);
				}
			} else {

				lqZeile.setMpn((String) oZeile[1]);

				alDaten.add(lqZeile);
			}

		}

		return alDaten;

	}

	public WaffenzusatzDto waffenzusatzFindByPrimaryKey(Integer iId) {
		Waffenzusatz bean = em.find(Waffenzusatz.class, iId);
		return WaffenzusatzDtoAssembler.createDto(bean);
	}

	private void setWaffenzusatzFromWaffenzusatzDto(Waffenzusatz bean, WaffenzusatzDto dto) {
		bean.setCNr(dto.getCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}
	// --

	public Integer createDateiverweis(DateiverweisDto dto) {

		try {
			Query query = em.createNamedQuery("DateiverweisfindByMandantCNrCLaufwerk");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCLaufwerk());
			Dateiverweis doppelt = (Dateiverweis) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_DATEIVERWEIS.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_DATEIVERWEIS);
			dto.setIId(pk);

			Dateiverweis bean = new Dateiverweis(dto.getIId(), dto.getMandantCNr(), dto.getCLaufwerk(), dto.getCUnc());
			em.persist(bean);
			em.flush();
			setDateiverweisFromDateiverweisDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateDateiverweis(DateiverweisDto dto) {
		Dateiverweis bean = em.find(Dateiverweis.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("DateiverweisfindByMandantCNrCLaufwerk");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCLaufwerk());
			Integer iIdVorhanden = ((Dateiverweis) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_DATEIVERWEIS.UK"));
			}
		} catch (NoResultException ex) {

		}

		setDateiverweisFromDateiverweisDto(bean, dto);
	}

	private void setDateiverweisFromDateiverweisDto(Dateiverweis bean, DateiverweisDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCLaufwerk(dto.getCLaufwerk());
		bean.setCUnc(dto.getCUnc());
		em.merge(bean);
		em.flush();
	}

	private void setWaffenkategorieFromWaffenkategorieDto(Waffenkategorie bean, WaffenkategorieDto dto) {
		bean.setCNr(dto.getCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	public void removeDateiverweis(DateiverweisDto dto) {
		Dateiverweis toRemove = em.find(Dateiverweis.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public DateiverweisDto dateiverweisFindByPrimaryKey(Integer iId) {
		Dateiverweis bean = em.find(Dateiverweis.class, iId);
		return DateiverweisDtoAssembler.createDto(bean);
	}

	public Map getAllWaffenkaliber(TheClientDto theClientDto) {
		TreeMap<Object, Object> tm = new TreeMap<Object, Object>();

		Query query = em.createNamedQuery("WaffenkaliberFindAll");
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Waffenkaliber w = (Waffenkaliber) itArten.next();
			tm.put(w.getIId(), w.getCBez());
		}

		return tm;
	}

	public Map getAllWaffenausfuehrung(TheClientDto theClientDto) {
		TreeMap<Object, Object> tm = new TreeMap<Object, Object>();

		Query query = em.createNamedQuery("WaffenausfuehrungFindAll");
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Waffenausfuehrung w = (Waffenausfuehrung) itArten.next();
			tm.put(w.getIId(), w.getCBez());
		}

		return tm;
	}

	public Map getAllWaffentyp(TheClientDto theClientDto) {
		TreeMap<Object, Object> tm = new TreeMap<Object, Object>();

		Query query = em.createNamedQuery("WaffentypFindAll");
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Waffentyp w = (Waffentyp) itArten.next();
			tm.put(w.getIId(), w.getCBez());
		}

		return tm;
	}

	public Map getAllWaffentypFein(TheClientDto theClientDto) {
		TreeMap<Object, Object> tm = new TreeMap<Object, Object>();

		Query query = em.createNamedQuery("WaffentypFeinFindAll");
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			WaffentypFein w = (WaffentypFein) itArten.next();
			tm.put(w.getIId(), w.getCBez());
		}

		return tm;
	}

	public Map getAllWaffenkategorie(TheClientDto theClientDto) {
		TreeMap<Object, Object> tm = new TreeMap<Object, Object>();

		Query query = em.createNamedQuery("WaffenkategorieFindAll");
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Waffenkategorie w = (Waffenkategorie) itArten.next();
			tm.put(w.getIId(), w.getCBez());
		}

		return tm;
	}

	public Map getAllWaffenzusatz(TheClientDto theClientDto) {
		TreeMap<Object, Object> tm = new TreeMap<Object, Object>();

		Query query = em.createNamedQuery("WaffenzusatzFindAll");
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Waffenzusatz w = (Waffenzusatz) itArten.next();
			tm.put(w.getIId(), w.getCBez());
		}

		return tm;
	}

}
