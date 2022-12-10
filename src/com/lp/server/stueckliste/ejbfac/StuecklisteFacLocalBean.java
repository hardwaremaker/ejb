package com.lp.server.stueckliste.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.stueckliste.ejb.Stueckliste;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StklparameterDto;
import com.lp.server.stueckliste.service.StrukturDatenParamDto;
import com.lp.server.stueckliste.service.StuecklisteAufgeloest;
import com.lp.server.stueckliste.service.StrukturDatenParamDto.Sort;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteDtoAssembler;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteFacLocal;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class StuecklisteFacLocalBean extends Facade implements StuecklisteFacLocal {

	@PersistenceContext
	private EntityManager em;

	private StuecklisteDto assembleStuecklisteDto(Stueckliste stueckliste) {
		return StuecklisteDtoAssembler.createDto(stueckliste);
	}

	public List<FLRStuecklisteposition> calculatePositions(Integer stuecklisteId, StrukturDatenParamDto paramDto,
			List<FLRStuecklisteposition> positions, TheClientDto theClientDto) {

		// Keine Formeln wenn keine Positionen, oder Stueckliste keine Formeln
		// will
		if (positions == null || positions.size() == 0)
			return positions;
		if (positions.get(0).getFlrstueckliste().getB_mitFormeln() == 0)
			return positions;

		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteId, theClientDto);
		myLogger.info("Running Positions classId '" + stuecklisteId + "' (" + dto.getArtikelDto().getCNr() + ")...");

		StklparameterDto[] stklparamDtos = getStuecklisteFac().stklparameterFindByStuecklisteIId(stuecklisteId,
				theClientDto);
		ScriptCompiler scc = new ScriptCompiler(stuecklisteId, stklparamDtos);
		ScriptRunnerFLRStuecklisteposition scr = new ScriptRunnerFLRStuecklisteposition(scc);
		scr.compileRun(positions, paramDto);
		return positions;
	}

	public List<FLRStuecklistearbeitsplan> calculateArbeitsplan(Integer stuecklisteId, StrukturDatenParamDto paramDto,
			List<FLRStuecklistearbeitsplan> positions, TheClientDto theClientDto) {

		// Keine Formeln wenn keine Positionen, oder Stueckliste keine Formeln
		// will
		if (positions == null || positions.size() == 0)
			return positions;
		if (positions.get(0).getFlrstueckliste().getB_mitFormeln() == 0)
			return positions;

		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteId, theClientDto);
		myLogger.info("Running Arbeitsplan classId '" + stuecklisteId + "' (" + dto.getArtikelDto().getCNr() + ")...");

		StklparameterDto[] stklparamDtos = getStuecklisteFac().stklparameterFindByStuecklisteIId(stuecklisteId,
				theClientDto);
		ScriptCompiler scc = new ScriptCompiler(stuecklisteId, stklparamDtos);
		ScriptRunnerFLRStuecklistearbeitsplan scr = new ScriptRunnerFLRStuecklistearbeitsplan(scc);
		scr.compileRun(positions, paramDto);
		return positions;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public StrukturDatenParamDto getStrukturDatenEinerStuecklisteMitArbeitsplanNew(Integer stuecklisteId,
			StrukturDatenParamDto paramDto, TheClientDto theClientDto) {
		Validator.notNull(stuecklisteId, "stuecklisteId");
		Validator.notNull(paramDto, "paramDto");

		if (paramDto.getEbene() > 50) {
			throw new EJBExceptionLP(new Exception("STUECKLISTEN DEADLOCK " + stuecklisteId.toString()));
		}

		MandantDto mandantDtoFuerMandantFeld = null;

		try {
			if (paramDto.getMandantCnrStueckliste() == null) {
				mandantDtoFuerMandantFeld = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);
			} else {
				mandantDtoFuerMandantFeld = getMandantFac().mandantFindByPrimaryKey(paramDto.getMandantCnrStueckliste(),
						theClientDto);
			}
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		// Alle Positionen einer Stueckliste holen, sortiert nach Artikelart und
		// nach der angegebenen Option
		FLRStuecklisteposition flrStuecklisteposition = new FLRStuecklisteposition();
		flrStuecklisteposition.setStueckliste_i_id(stuecklisteId);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRStuecklisteposition.class)
				.add(Example.create(flrStuecklisteposition))
				.createAlias(StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL, "a");
		// SP6537 Sortierung nach Artikelart entfaellt
		// crit.addOrder(Order.asc("a." +
		// ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));
		if (paramDto.getSortierung().equals(Sort.Artikelnummer)) {
			crit.addOrder(Order.asc("a." + ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));
			crit.addOrder(Order.asc("a.c_nr"));
		} else if (paramDto.getSortierung().equals(Sort.Position)) {
			crit.addOrder(Order.asc("a." + ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));
			crit.addOrder(Order.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_C_POSITION));
		} else if (paramDto.getSortierung().equals(Sort.Ohne)) {
			crit.addOrder(Order.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_I_SORT));
		}

		List<?> results = crit.list();

		FLRStuecklisteposition[] returnArray = new FLRStuecklisteposition[results.size()];

		results = calculatePositions(stuecklisteId, paramDto, (List<FLRStuecklisteposition>) results, theClientDto);

		returnArray = (FLRStuecklisteposition[]) results.toArray(returnArray);

		// SP3218 Zuerst Zielmengen berechnen

		for (int i = 0; i < returnArray.length; i++) {

			try {

				// Menge nach Zieleinheit umrechnen
				BigDecimal bdMenge = returnArray[i].getN_menge().multiply(paramDto.getSatzgroesse());

				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(returnArray[i].getEinheit_c_nr(),
						theClientDto);

				// Positionsmenge nach Zielenge umrechnen
				int dimension = einheitDto.getIDimension().intValue();

				if (dimension == 1) {
					if (returnArray[i].getF_dimension1() != null) {
						bdMenge = bdMenge.multiply(new BigDecimal(returnArray[i].getF_dimension1().doubleValue()));
					}
				} else if (dimension == 2) {
					if (returnArray[i].getF_dimension1() != null && returnArray[i].getF_dimension2() != null) {
						bdMenge = bdMenge.multiply(new BigDecimal(returnArray[i].getF_dimension1().doubleValue()))
								.multiply(new BigDecimal(returnArray[i].getF_dimension2().doubleValue()));
					}
				} else if (dimension == 3) {
					if (returnArray[i].getF_dimension1() != null && returnArray[i].getF_dimension2() != null
							&& returnArray[i].getF_dimension3() != null) {
						bdMenge = bdMenge.multiply(new BigDecimal(returnArray[i].getF_dimension1().doubleValue()))
								.multiply(new BigDecimal(returnArray[i].getF_dimension2().doubleValue()))
								.multiply(new BigDecimal(returnArray[i].getF_dimension3().doubleValue()));
					}
				}

				BigDecimal faktor = null;
				String artikelnummerStueckliste = returnArray[i].getFlrstueckliste().getFlrartikel().getC_nr();
				try {
					faktor = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(1),
							returnArray[i].getFlrartikel().getEinheit_c_nr(), returnArray[i].getEinheit_c_nr(),
							returnArray[i].getI_id(), theClientDto);
				} catch (EJBExceptionLP ex2) {
					// Projekte 3509/10048/9918
					String positionsArtikel = returnArray[i].getFlrartikel().getC_nr();
					String position = "";
					if (returnArray[i].getI_sort() != null) {
						position = returnArray[i].getI_sort() + "";
					}
					List<Object> info = ex2.getAlInfoForTheClient();
					// Nachricht zusammenbauen
					String meldung = (String) info.get(0) + "\r\n";
					meldung += "Zu finden in Ebene " + (paramDto.getEbene() + 1) + ", St\u00FCckliste: "
							+ artikelnummerStueckliste + "\r\n";
					meldung += "Position " + position + "-> Artikelnummer: " + positionsArtikel;
					info.set(0, meldung);
					ex2.setAlInfoForTheClient(info);
					throwEJBExceptionLPRespectOld(new RemoteException("", ex2));
				}
				if (faktor.doubleValue() != 0) {
					bdMenge = bdMenge.divide(faktor, 12, BigDecimal.ROUND_HALF_EVEN);

					bdMenge = Helper.berechneMengeInklusiveVerschnitt(bdMenge,
							returnArray[i].getFlrartikel().getF_verschnittfaktor(),
							returnArray[i].getFlrartikel().getF_verschnittbasis(), paramDto.getLosgroesse(),
							returnArray[i].getFlrartikel().getF_fertigungs_vpe());
					// PJ 14352
					if (Helper.short2boolean(returnArray[i].getB_ruestmenge()) == false) {
						// SP4930 Nur Durch Erfassungsfaktor dividieren, wenn
						// keine Ruestmenge
						bdMenge = bdMenge.divide(returnArray[i].getFlrstueckliste().getN_erfassungsfaktor(), 12,
								BigDecimal.ROUND_HALF_EVEN);
					}

					returnArray[i].bdZielmenge = bdMenge;

				} else {
					ArrayList<Object> al = new ArrayList<Object>();

					EinheitDto von = getSystemFac().einheitFindByPrimaryKey(returnArray[i].getEinheit_c_nr(),
							theClientDto);
					EinheitDto zu = getSystemFac()
							.einheitFindByPrimaryKey(returnArray[i].getFlrartikel().getEinheit_c_nr(), theClientDto);

					al.add(von.formatBez() + " <-> " + zu.formatBez());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT, al,
							new Exception("FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT"));
				}

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}

		// NEU Projekt 9730
		ArrayList<FLRStuecklisteposition> alKomp = new ArrayList();
		if (paramDto.isGleichePositionenZusammenfassen()) {
			// Positionen zusammenfassen, wenn der Artikel und die
			// Positionseinheit gleich sind
			// Nur wenns kein Arbeitsplan ist und in derselben Ebene
			for (int i = 0; i < returnArray.length; i++) {
				boolean bGefunden = false;

				for (int j = 0; j < alKomp.size(); j++) {
					FLRStuecklisteposition temp = (FLRStuecklisteposition) alKomp.get(j);

					if (temp.getFlrartikel().getI_id().equals(returnArray[i].getFlrartikel().getI_id())
							&& temp.getEinheit_c_nr().equals(returnArray[i].getEinheit_c_nr())
							&& temp.getB_ruestmenge().equals(returnArray[i].getB_ruestmenge())) {
						temp.setN_menge(temp.getN_menge().add(returnArray[i].getN_menge()));
						temp.bdZielmenge = temp.bdZielmenge.add(returnArray[i].bdZielmenge);
						alKomp.set(j, temp);

						bGefunden = true;
					}

				}
				if (!bGefunden) {
					alKomp.add(returnArray[i]);
				}

			}
			returnArray = new FLRStuecklisteposition[alKomp.size()];
			returnArray = (FLRStuecklisteposition[]) alKomp.toArray(returnArray);
		}

		// Arbeitsplan
		FLRStuecklistearbeitsplan flrStuecklistearbeitsplan = new FLRStuecklistearbeitsplan();
		flrStuecklistearbeitsplan.setStueckliste_i_id(stuecklisteId);

		Session session2 = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit2 = session2.createCriteria(FLRStuecklistearbeitsplan.class)
				.add(Example.create(flrStuecklistearbeitsplan))
				.createAlias(StuecklisteFac.FLR_STUECKLISTEARBEITSPLAN_FLRARTIKEL, "a");
		crit2.addOrder(Order.asc("a." + ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));

		crit2.addOrder(Order.asc("i_arbeitsgang"));
		crit2.addOrder(Order.asc("i_unterarbeitsgang"));

		List<?> results2 = crit2.list();

		List<FLRStuecklistearbeitsplan> aps = calculateArbeitsplan(stuecklisteId, paramDto,
				(List<FLRStuecklistearbeitsplan>) results2, theClientDto);

		FLRStuecklistearbeitsplan[] arbeitsplan = new FLRStuecklistearbeitsplan[aps.size()];
		arbeitsplan = (FLRStuecklistearbeitsplan[]) aps.toArray(arbeitsplan);

		int iBasisKalkulation = 0;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_BASIS_GESAMTKALKULATION);
			iBasisKalkulation = (Integer) parameterDto.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		for (int i = 0; i < arbeitsplan.length; i++) {

			Long stueckzeit = new Long(
					(long) (arbeitsplan[i].getL_stueckzeit().longValue() * paramDto.getSatzgroesse().doubleValue()));

			if (arbeitsplan[i].getFlrstueckliste().getN_erfassungsfaktor().doubleValue() != 0) {
				stueckzeit = (long) (stueckzeit
						/ arbeitsplan[i].getFlrstueckliste().getN_erfassungsfaktor().doubleValue());
			}

			Long ruestzeit = arbeitsplan[i].getL_ruestzeit();

			double posmenge = paramDto.getSatzgroesse().multiply(paramDto.getLosgroesse()).doubleValue();
			// SP6514
			if (posmenge == 0) {
				ruestzeit = 0L;
			}

			if (iBasisKalkulation == 1
					&& arbeitsplan[i].getFlrstueckliste().getFlrartikel().getF_fertigungssatzgroesse() != null
					&& arbeitsplan[i].getFlrstueckliste().getFlrartikel().getF_fertigungssatzgroesse().doubleValue() > 0
					&& paramDto.getEbene() > 0) {

				double fsgmenge = arbeitsplan[i].getFlrstueckliste().getFlrartikel().getF_fertigungssatzgroesse()
						.doubleValue();

				while (fsgmenge < posmenge) {

					fsgmenge += arbeitsplan[i].getFlrstueckliste().getFlrartikel().getF_fertigungssatzgroesse()
							.doubleValue();
				}

				ruestzeit = (long) ((ruestzeit / fsgmenge) * paramDto.getSatzgroesse().doubleValue()
						* paramDto.getLosgroesse().doubleValue());

			}

			if (arbeitsplan[i].getFlrmaschine() == null
					|| Helper.short2boolean(arbeitsplan[i].getB_nurmaschinenzeit()) == false) {

				StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
				stuecklistearbeitsplanDto.setIId(arbeitsplan[i].getI_id());
				stuecklistearbeitsplanDto.setArtikelIId(arbeitsplan[i].getFlrartikel().getI_id());
				stuecklistearbeitsplanDto.setCKommentar(arbeitsplan[i].getC_kommentar());
				stuecklistearbeitsplanDto.setLRuestzeit(ruestzeit);
				stuecklistearbeitsplanDto.setBNurmaschinenzeit(arbeitsplan[i].getB_nurmaschinenzeit());
				stuecklistearbeitsplanDto.setIAufspannung(arbeitsplan[i].getI_aufspannung());
				stuecklistearbeitsplanDto.setXFormel(arbeitsplan[i].getX_formel());

				stuecklistearbeitsplanDto.setLStueckzeit(stueckzeit);
				StuecklisteMitStrukturDto stuecklisteMitStrukturDto = new StuecklisteMitStrukturDto(paramDto.getEbene(),
						null);
				stuecklisteMitStrukturDto.setStuecklistearbeitsplanDto(stuecklistearbeitsplanDto);
				stuecklisteMitStrukturDto.setBArbeitszeit(true);

				stuecklistearbeitsplanDto.setIArbeitsgang(arbeitsplan[i].getI_arbeitsgang());
				stuecklistearbeitsplanDto.setIUnterarbeitsgang(arbeitsplan[i].getI_unterarbeitsgang());
				stuecklistearbeitsplanDto.setCKommentar(arbeitsplan[i].getC_kommentar());

				stuecklisteMitStrukturDto.setTFreigabe(arbeitsplan[i].getFlrstueckliste().getT_freigabe());

				stuecklisteMitStrukturDto.setMandantCNr(mandantDtoFuerMandantFeld.getCNr());
				stuecklisteMitStrukturDto.setMandantCKbez(mandantDtoFuerMandantFeld.getCKbez());

				if (arbeitsplan[i].getFlrstueckliste().getFlrpersonal_freigabe() != null) {
					stuecklisteMitStrukturDto.setCKurzzeichenPersonFreigabe(
							arbeitsplan[i].getFlrstueckliste().getFlrpersonal_freigabe().getC_kurzzeichen());
				}

				if (arbeitsplan[i].reportLogging != null) {
					stuecklisteMitStrukturDto.setReportDebug(arbeitsplan[i].reportLogging.getDebug());
					stuecklisteMitStrukturDto.setReportInfo(arbeitsplan[i].reportLogging.getInfo());
					stuecklisteMitStrukturDto.setReportWarn(arbeitsplan[i].reportLogging.getWarn());
					stuecklisteMitStrukturDto.setReportError(arbeitsplan[i].reportLogging.getError());
				}
				paramDto.addStruktur(stuecklisteMitStrukturDto);
			}

			// Wenn Maschine, dann zusaetzlichen Eintrag erstellen:
			if (arbeitsplan[i].getFlrmaschine() != null) {

				StuecklistearbeitsplanDto stuecklistearbeitsplanMaschineDto = new StuecklistearbeitsplanDto();
				stuecklistearbeitsplanMaschineDto.setIId(arbeitsplan[i].getI_id());
				stuecklistearbeitsplanMaschineDto.setArtikelIId(arbeitsplan[i].getFlrartikel().getI_id());
				stuecklistearbeitsplanMaschineDto.setCKommentar(arbeitsplan[i].getC_kommentar());
				stuecklistearbeitsplanMaschineDto.setLRuestzeit(ruestzeit);
				stuecklistearbeitsplanMaschineDto.setLStueckzeit(stueckzeit);
				stuecklistearbeitsplanMaschineDto.setMaschineIId(arbeitsplan[i].getMaschine_i_id());
				stuecklistearbeitsplanMaschineDto.setBNurmaschinenzeit(arbeitsplan[i].getB_nurmaschinenzeit());
				stuecklistearbeitsplanMaschineDto.setIAufspannung(arbeitsplan[i].getI_aufspannung());

				stuecklistearbeitsplanMaschineDto.setIArbeitsgang(arbeitsplan[i].getI_arbeitsgang());
				stuecklistearbeitsplanMaschineDto.setIUnterarbeitsgang(arbeitsplan[i].getI_unterarbeitsgang());
				stuecklistearbeitsplanMaschineDto.setCKommentar(arbeitsplan[i].getC_kommentar());
				stuecklistearbeitsplanMaschineDto.setXFormel(arbeitsplan[i].getX_formel());

				StuecklisteMitStrukturDto stuecklisteMitStrukturMaschineDto = new StuecklisteMitStrukturDto(
						paramDto.getEbene(), null);
				stuecklisteMitStrukturMaschineDto.setStuecklistearbeitsplanDto(stuecklistearbeitsplanMaschineDto);
				stuecklisteMitStrukturMaschineDto.setBArbeitszeit(true);
				stuecklisteMitStrukturMaschineDto.setBMaschinenzeit(true);

				stuecklisteMitStrukturMaschineDto.setMandantCNr(mandantDtoFuerMandantFeld.getCNr());
				stuecklisteMitStrukturMaschineDto.setMandantCKbez(mandantDtoFuerMandantFeld.getCKbez());

				stuecklisteMitStrukturMaschineDto.setTFreigabe(arbeitsplan[i].getFlrstueckliste().getT_freigabe());
				if (arbeitsplan[i].getFlrstueckliste().getFlrpersonal_freigabe() != null) {
					stuecklisteMitStrukturMaschineDto.setCKurzzeichenPersonFreigabe(
							arbeitsplan[i].getFlrstueckliste().getFlrpersonal_freigabe().getC_kurzzeichen());
				}

				if (arbeitsplan[i].reportLogging != null) {
					stuecklisteMitStrukturMaschineDto.setReportDebug(arbeitsplan[i].reportLogging.getDebug());
					stuecklisteMitStrukturMaschineDto.setReportInfo(arbeitsplan[i].reportLogging.getInfo());
					stuecklisteMitStrukturMaschineDto.setReportWarn(arbeitsplan[i].reportLogging.getWarn());
					stuecklisteMitStrukturMaschineDto.setReportError(arbeitsplan[i].reportLogging.getError());
				}
				paramDto.addStruktur(stuecklisteMitStrukturMaschineDto);
			}
		}

		for (int i = 0; i < returnArray.length; i++) {
			StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
			stuecklistepositionDto.setArtikelIId(returnArray[i].getFlrartikel().getI_id());

			
			
			// PJ20862
			stuecklistepositionDto.setFormelArtikelCNr_NOT_IN_DB(returnArray[i].formelArtikel);
			stuecklistepositionDto
					.setFormelUebergeordneteArtikelCNr_NOT_IN_DB(returnArray[i].formelUebergeordneterArtikel);

			stuecklistepositionDto
					.setBdVerschnittmengeDesArtikels(returnArray[i].getFlrartikel().getN_verschnittmenge());

			stuecklistepositionDto.setCKommentar(returnArray[i].getC_kommentar());
			stuecklistepositionDto.setCPosition(returnArray[i].getC_position());
			stuecklistepositionDto.setEinheitCNr(returnArray[i].getEinheit_c_nr());
			stuecklistepositionDto.setNMenge(returnArray[i].getN_menge());

			stuecklistepositionDto.setNZielmenge(returnArray[i].bdZielmenge);

			stuecklistepositionDto.setNKalkpreis(returnArray[i].getN_kalkpreis());

			stuecklistepositionDto.setXFormel(returnArray[i].getX_formel());

			stuecklistepositionDto.setILfdnummer(returnArray[i].getI_lfdnummer());
			stuecklistepositionDto.setIId(returnArray[i].getI_id());
			stuecklistepositionDto.setCKommentar(returnArray[i].getC_kommentar());
			stuecklistepositionDto.setStuecklisteIId(returnArray[i].getStueckliste_i_id());

			stuecklistepositionDto.setBRuestmenge(returnArray[i].getB_ruestmenge());
			stuecklistepositionDto.setfLagermindeststandAusKopfartikel(
					returnArray[i].getFlrstueckliste().getFlrartikel().getF_lagermindest());

			MontageartDto ma = new MontageartDto();
			ma.setIId(returnArray[i].getFlrmontageart().getI_id());
			ma.setCBez(returnArray[i].getFlrmontageart().getC_bez());
			stuecklistepositionDto.setMontageartDto(ma);
			if (returnArray[i].getF_dimension1() != null) {
				stuecklistepositionDto.setFDimension1(new Float(returnArray[i].getF_dimension1().doubleValue()));
			}
			if (returnArray[i].getF_dimension2() != null) {
				stuecklistepositionDto.setFDimension2(new Float(returnArray[i].getF_dimension2().doubleValue()));
			}
			if (returnArray[i].getF_dimension3() != null) {
				stuecklistepositionDto.setFDimension3(new Float(returnArray[i].getF_dimension3().doubleValue()));
			}

			StuecklisteMitStrukturDto zeile = new StuecklisteMitStrukturDto(paramDto.getEbene(),
					stuecklistepositionDto);

			stuecklistepositionDto.setHierarchiemenge_NOT_IN_DB(paramDto.getSatzgroesse());

			if (returnArray[i].reportLogging != null) {
				zeile.setReportDebug(returnArray[i].reportLogging.getDebug());
				zeile.setReportInfo(returnArray[i].reportLogging.getInfo());
				zeile.setReportWarn(returnArray[i].reportLogging.getWarn());
				zeile.setReportError(returnArray[i].reportLogging.getError());
			}

			zeile.setTFreigabe(returnArray[i].getFlrstueckliste().getT_freigabe());

			if (returnArray[i].getFlrstueckliste().getFlrpersonal_freigabe() != null) {
				zeile.setCKurzzeichenPersonFreigabe(
						returnArray[i].getFlrstueckliste().getFlrpersonal_freigabe().getC_kurzzeichen());
			}

			if (paramDto.isMitUnterstuecklisten()) {
				MandantDto[] mandantenDtos = null;

				boolean bIstBeiKeinemMandantenEineStueckliste = true;
				try {
					if (paramDto.isUeberAlleMandanten()) {
						mandantenDtos = getMandantFac().mandantFindAll(theClientDto);
					} else {
						mandantenDtos = new MandantDto[] {
								getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto) };
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				for (int k = 0; k < mandantenDtos.length; k++) {
					zeile.setMandantCNr(mandantenDtos[k].getCNr());
					zeile.setMandantCKbez(mandantenDtos[k].getCKbez());

					try {

						// PJ20762
						if (stuecklistepositionDto.getNMenge().doubleValue() == 0) {
							throw new NoResultException();
						}

						Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
						query.setParameter(1, returnArray[i].getFlrartikel().getI_id());
						query.setParameter(2, mandantenDtos[k].getCNr());
						Stueckliste stkl = (Stueckliste) query.getSingleResult();
						if (stkl != null) {
							bIstBeiKeinemMandantenEineStueckliste = false;
							paramDto.addStruktur(zeile.clone());
							// wenn Fremdfertigungsstueckliste, dann keine
							// Aufloesung
							if (Helper.short2boolean(stkl.getBFremdfertigung()) == false) {

								if (stuecklistepositionDto.getNZielmenge().doubleValue() == 0) {
									boolean bZielmengeDarf0Sein = false;
									try {
										ParametermandantDto parameterDto = getParameterFac().getMandantparameter(
												theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
												ParameterFac.PARAMETER_ZIELMENGE_GLEICH_0_ZULAESSIG);
										bZielmengeDarf0Sein = (Boolean) parameterDto.getCWertAsObject();
									} catch (RemoteException e) {
										throwEJBExceptionLPRespectOld(e);
									}

									if (bZielmengeDarf0Sein == false) {
										ArrayList al = new ArrayList();
										al.add(returnArray[i].getFlrartikel().getC_nr());

										al.add(returnArray[i].getFlrstueckliste().getFlrartikel().getC_nr());
										throw new EJBExceptionLP(
												EJBExceptionLP.FEHLER_STUECKLISTE_EIGENGEFERTIGTE_UNTERSTUECKLISTE_MIT_ZIELMENGE_GLEICH_NULL,
												al, new Exception(
														"FEHLER_STUECKLISTE_EIGENGEFERTIGTE_UNTERSTUECKLISTE_MIT_ZIELMENGE_GLEICH_NULL"));
									}
								}

								query = em.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIId");
								query.setParameter(1, stkl.getIId());
								int iAnzahlPositionenArbeitsplan = query.getResultList().size();
								// Wenn keine Positionen und kein Arbeitsplan,
								// dann
								// auch keinen Aufloesung

								// Aufgrund von PJ20715 auskommentiert

								// if (iAnzahlPositionen
								// + iAnzahlPositionenArbeitsplan > 0) {
								StuecklisteMitStrukturDto strukturTemp = paramDto.getLast();
								strukturTemp.setBStueckliste(true);
								strukturTemp.setIAnzahlArbeitsschritte(iAnzahlPositionenArbeitsplan);
								strukturTemp.setStuecklisteDto(assembleStuecklisteDto(stkl));
								strukturTemp.setDurchlaufzeit(stkl.getNDefaultdurchlaufzeit());

								StrukturDatenParamDto pDto = paramDto.createHierachy();
								pDto.setSatzgroesse(stuecklistepositionDto.getNZielmenge());
								pDto.setMandantCnrStueckliste(stkl.getMandantCNr());
								getStrukturDatenEinerStuecklisteMitArbeitsplanNew(stkl.getIId(), pDto, theClientDto);
								// }
							}
							break;
						}
					} catch (NoResultException ex) {
						// Dann keine Stueckliste

						if (paramDto.isUeberAlleMandanten()) {
							if (paramDto.getEbene() > 0) {
								if (paramDto.getMandantCnrStueckliste() == null
										|| paramDto.getMandantCnrStueckliste().equals(mandantenDtos[k].getCNr())) {
									paramDto.addStruktur(zeile.clone());
								}
							}
						} else {
							paramDto.addStruktur(zeile.clone());
						}

					} catch (NonUniqueResultException ex1) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
					}
				}

				// SP4523
				if (paramDto.isUeberAlleMandanten() && paramDto.getEbene() == 0
						&& bIstBeiKeinemMandantenEineStueckliste == true) {
					zeile.setMandantCNr(mandantDtoFuerMandantFeld.getCNr());
					zeile.setMandantCKbez(mandantDtoFuerMandantFeld.getCKbez());
					paramDto.addStruktur(zeile.clone());
				}
			} else {
				paramDto.addStruktur(zeile);
			}
		}

		session.close();

		return paramDto;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public StuecklisteAufgeloest getStrukturdatenEinesArtikelsStrukturiert(Integer artikelIIdI,
			boolean bNurZuDruckendeI, int iStuecklistenaufloesungTiefeI,
			boolean bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
			BigDecimal nWievieleEinheitenDerUnterpositionInStuecklisteI, boolean bFremdfertigungAufloesenI,
			boolean bUeberAlleMandanten, TheClientDto theClientDto) {
		return getStrukturdatenEinesArtikelsStrukturiertImpl(artikelIIdI, bNurZuDruckendeI,
				iStuecklistenaufloesungTiefeI, bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
				nWievieleEinheitenDerUnterpositionInStuecklisteI, bFremdfertigungAufloesenI, bUeberAlleMandanten, null,
				null, 0, null, theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public StuecklisteAufgeloest getStrukturdatenEinesArtikelsStrukturiert(Integer artikelIIdI,
			boolean bNurZuDruckendeI, int iStuecklistenaufloesungTiefeI,
			boolean bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
			BigDecimal nWievieleEinheitenDerUnterpositionInStuecklisteI, boolean bFremdfertigungAufloesenI,
			boolean bUeberAlleMandanten, double dWBZWennNichtDefiniertInTagen, TheClientDto theClientDto) {
		return getStrukturdatenEinesArtikelsStrukturiertImpl(artikelIIdI, bNurZuDruckendeI,
				iStuecklistenaufloesungTiefeI, bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
				nWievieleEinheitenDerUnterpositionInStuecklisteI, bFremdfertigungAufloesenI, bUeberAlleMandanten, null,
				null, dWBZWennNichtDefiniertInTagen, null, theClientDto);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public StuecklisteAufgeloest getStrukturdatenEinesArtikelsStrukturiert(Integer artikelIIdI,
			boolean bNurZuDruckendeI, int iStuecklistenaufloesungTiefeI,
			boolean bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
			BigDecimal nWievieleEinheitenDerUnterpositionInStuecklisteI, boolean bFremdfertigungAufloesenI,
			boolean bUeberAlleMandanten, double dWBZWennNichtDefiniertInTagen, StrukturDatenParamDto paramDto,
			TheClientDto theClientDto) {
		return getStrukturdatenEinesArtikelsStrukturiertImpl(artikelIIdI, bNurZuDruckendeI,
				iStuecklistenaufloesungTiefeI, bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
				nWievieleEinheitenDerUnterpositionInStuecklisteI, bFremdfertigungAufloesenI, bUeberAlleMandanten, null,
				null, dWBZWennNichtDefiniertInTagen, paramDto, theClientDto);
	}

	private StuecklisteAufgeloest getStrukturdatenEinesArtikelsStrukturiertImpl(Integer artikelIIdI,
			boolean bNurZuDruckendeI, int iStuecklistenaufloesungTiefeI,
			boolean bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
			BigDecimal nWievieleEinheitenDerUnterpositionInStuecklisteI, boolean bFremdfertigungAufloesenI,
			boolean bUeberAlleMandanten, StuecklisteAufgeloest vorgaenger,
			StuecklistepositionDto stuecklistepositionDtoI, double dWBZWennNichtDefiniertInTagen,
			StrukturDatenParamDto paramDto, TheClientDto theClientDto) {

		StuecklisteAufgeloest stuecklisteMitStrukturDto = new StuecklisteAufgeloest(stuecklistepositionDtoI);
		int iFaktor = 1;
		try {

			ParametermandantDto parameterDtoWBZ = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);

			if (parameterDtoWBZ.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				iFaktor = 7;

			} else if (parameterDtoWBZ.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				iFaktor = 1;
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT, new Exception(
						ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT + " ist nicht richtig definiert"));
			}

			stuecklisteMitStrukturDto.setWbzInTagenAusBevoruzgtemArtikellieferant(dWBZWennNichtDefiniertInTagen);

			ArtikellieferantDto[] artLief = getArtikelFac().artikellieferantFindByArtikelIId(artikelIIdI, theClientDto);

			stuecklisteMitStrukturDto.setLagerstand(
					getLagerFac().getLagerstandAllerLagerEinesMandanten(artikelIIdI, false, theClientDto));

			stuecklisteMitStrukturDto.setLagerstandSperrlaeger(
					getLagerFac().getLagerstandAllerSperrlaegerEinesMandanten(artikelIIdI, theClientDto));

			stuecklisteMitStrukturDto.setInFertigung(getFertigungFac().getAnzahlInFertigung(artikelIIdI, theClientDto));

			if (bUeberAlleMandanten == true) {

				if (vorgaenger != null && vorgaenger.getStuecklisteDto() != null) {

					stuecklisteMitStrukturDto.setLagerstand(getLagerFac().getLagerstandAllerLagerEinesMandanten(
							artikelIIdI, false, vorgaenger.getStuecklisteDto().getMandantCNr()));

					boolean bLieferantDesselbenMandantesVerfuegbar = false;

					ArtikellieferantDto artLiefGueltig = null;
					String mandantDesLieferantenWennKeinLieferantDesMandantenVerfuegbar = null;
					for (int i = 0; i < artLief.length; i++) {
						String mandantDesLieferanten = getLieferantFac()
								.lieferantFindByPrimaryKeySmall(artLief[i].getLieferantIId()).getMandantCNr();

						if (mandantDesLieferanten.equals(vorgaenger.getStuecklisteDto().getMandantCNr())) {
							bLieferantDesselbenMandantesVerfuegbar = true;
							mandantDesLieferantenWennKeinLieferantDesMandantenVerfuegbar = null;
							artLiefGueltig = artLief[i];
							break;
						} else {
							mandantDesLieferantenWennKeinLieferantDesMandantenVerfuegbar = mandantDesLieferanten;
							artLiefGueltig = artLief[i];

						}
					}

					stuecklisteMitStrukturDto.setArtikellieferantDto(artLiefGueltig);

					if (bLieferantDesselbenMandantesVerfuegbar == false
							&& mandantDesLieferantenWennKeinLieferantDesMandantenVerfuegbar != null) {

						if (artLiefGueltig != null && artLiefGueltig.getIWiederbeschaffungszeit() != null) {
							stuecklisteMitStrukturDto.setWbzInTagenAusBevoruzgtemArtikellieferant(
									new BigDecimal(artLiefGueltig.getIWiederbeschaffungszeit() * iFaktor)
											.doubleValue());
						}

						try {
							int i = (Integer) getParameterFac().getAnwenderparameter(ParameterFac.KATEGORIE_ALLGEMEIN,
									ParameterFac.ANWENDERPARAMETER_MANDANTEN_TRANSFERDAUER).getCWertAsObject();

							stuecklisteMitStrukturDto.setWbzInTagenAusBevoruzgtemArtikellieferant(
									stuecklisteMitStrukturDto.getWbzInTagenAusBevoruzgtemArtikellieferant()
											+ (i / iFaktor));

						} catch (Exception e) {
							throw new EJBExceptionLP(e);
						}

					} else {

						if (stuecklisteMitStrukturDto.getArtikellieferantDto() != null && stuecklisteMitStrukturDto
								.getArtikellieferantDto().getIWiederbeschaffungszeit() != null) {

							stuecklisteMitStrukturDto.setWbzInTagenAusBevoruzgtemArtikellieferant(new BigDecimal(
									stuecklisteMitStrukturDto.getArtikellieferantDto().getIWiederbeschaffungszeit()
											* iFaktor).doubleValue());

						}
					}

				}

			} else {
				if (artLief.length > 0 && artLief[0].getIWiederbeschaffungszeit() != null) {

					stuecklisteMitStrukturDto.setWbzInTagenAusBevoruzgtemArtikellieferant(
							new BigDecimal(artLief[0].getIWiederbeschaffungszeit() * iFaktor).doubleValue());

				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		stuecklisteMitStrukturDto.setVorgaenger(vorgaenger);

		if (artikelIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIIdI == null"));
		}

		// UW 23.03.06 Die naechste Ebene der Positionen wird nicht gelesen,
		// wenn...
		// ...die gewuenschte Tiefe der Stuecklistenaufloesung == 0 ist
		// ...die gewuenschte Tiefe der Stuecklistenaufloesung bereits
		// erreicht ist, wobei gilt:
		// iEbeneI 0 => gewuenschte Tiefe 1, iEbeneI 1 => gewuenschte Tiefe
		// 2 usw.

		int iAktuelleEbene = stuecklisteMitStrukturDto.getIEbene() + 1;

		if ((iStuecklistenaufloesungTiefeI != 0 && iAktuelleEbene < iStuecklistenaufloesungTiefeI)
				|| iStuecklistenaufloesungTiefeI == STUECKLISTENAUFLOESUNGSTIEFE_UNBEGRENZT
				|| iStuecklistenaufloesungTiefeI == STUECKLISTENAUFLOESUNGSTIEFE_BESTIMMT_LAGERSTANDSDETAIL) {

			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIIdI, theClientDto);

			if (stuecklisteDto == null && bUeberAlleMandanten == true) {
				StuecklisteDto[] stklDtos = getStuecklisteFac().stuecklisteFindByArtikelIId(artikelIIdI);

				if (stklDtos != null && stklDtos.length > 0) {
					stuecklisteDto = stklDtos[0];
				}
			}

			// wenn die aktuelle Artikelposition eine Stueckliste ist
			if (stuecklisteDto != null) {

				// PJ21577
				if (iStuecklistenaufloesungTiefeI == STUECKLISTENAUFLOESUNGSTIEFE_BESTIMMT_LAGERSTANDSDETAIL
						&& Helper.short2boolean(stuecklisteDto.getBDruckeinlagerstandsdetail()) == false) {
					// Dann nicht weiter aufloesen
				} else {

					try {
						if (stuecklisteDto.getNDefaultdurchlaufzeit() == null) {
							ParametermandantDto parameterDtoDDlz = getParameterFac().getMandantparameter(
									stuecklisteDto.getMandantCNr(), ParameterFac.KATEGORIE_FERTIGUNG,
									ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
							stuecklisteDto.setNDefaultdurchlaufzeit(
									new BigDecimal((Integer) parameterDtoDDlz.getCWertAsObject()));

						}

					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}

					// SP4347
					if ((vorgaenger != null && vorgaenger.getStuecklisteDto() != null
							&& !vorgaenger.getStuecklisteDto().getMandantCNr().equals(stuecklisteDto.getMandantCNr()))
							|| (vorgaenger == null
									&& !theClientDto.getMandant().equals(stuecklisteDto.getMandantCNr()))) {

						try {
							int i = (Integer) getParameterFac().getAnwenderparameter(ParameterFac.KATEGORIE_ALLGEMEIN,
									ParameterFac.ANWENDERPARAMETER_MANDANTEN_TRANSFERDAUER).getCWertAsObject();

							stuecklisteDto.setNDefaultdurchlaufzeit(new BigDecimal(
									stuecklisteDto.getNDefaultdurchlaufzeit().doubleValue() + (i / iFaktor)));

						} catch (Exception e) {
							throw new EJBExceptionLP(e);
						}

					}

					stuecklisteMitStrukturDto.setStuecklisteDto(stuecklisteDto);

					// UW 29.03.06 Die naechste Ebene der Positionen wird
					// gelesen, wenn
					// ...es sicht um eine Fremdfertigung handelt, diese aber
					// trotzdem aufgeloest werden soll
					// ...es sich um keine Fremdfertiung handelt
					// es sich um eine Fremdfertigung handelt und die
					// Fremdfertigung nicht aufgeloest werden soll
					if ((bFremdfertigungAufloesenI && Helper.short2boolean(stuecklisteDto.getBFremdfertigung()))
							|| !Helper.short2boolean(stuecklisteDto.getBFremdfertigung())) {
						Session session = null;

						try {
							SessionFactory factory = FLRSessionFactory.getFactory();
							session = factory.openSession();

							// Hiberante Criteria fuer alle Tabellen ausgehend
							// von meiner Haupttabelle anlegen,
							// nach denen ich filtern und sortieren kann
							Criteria crit = session.createCriteria(FLRStuecklisteposition.class);

							crit.add(Restrictions.eq(StuecklisteFac.FLR_STUECKLISTEPOSITION_STUECKLISTE_I_ID,
									stuecklisteDto.getIId()));

							// alle Positionen der Stueckliste, die mitgedruckt
							// werden sollen
							if (bNurZuDruckendeI) {
								crit.add(Restrictions.eq(StuecklisteFac.FLR_STUECKLISTEPOSITION_B_MITDRUCKEN,
										Helper.boolean2Short(bNurZuDruckendeI)));
							}

							crit.addOrder(Order.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_I_SORT));

							List<FLRStuecklisteposition> list = crit.list();

							if (Helper.isTrue(stuecklisteDto.getBMitFormeln()) && paramDto != null) {
								list = calculatePositions(stuecklisteDto.getIId(), paramDto, list, theClientDto);
							}

							ArrayList<StuecklisteAufgeloest> alPositionen = new ArrayList<StuecklisteAufgeloest>();

							Iterator<?> it = list.iterator();
							while (it.hasNext()) {
								FLRStuecklisteposition flrstuecklisteposition = (FLRStuecklisteposition) it.next();

								ArtikelDto artikelDtoSmall = new ArtikelDto();
								artikelDtoSmall.setCNr(flrstuecklisteposition.getFlrartikel().getC_nr());

								StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
								stuecklistepositionDto.setArtikelIId(flrstuecklisteposition.getFlrartikel().getI_id());

								stuecklistepositionDto
										.setBdVerschnittmengeDesArtikels(artikelDtoSmall.getNVerschnittmenge());

								stuecklistepositionDto.setArtikelDto(artikelDtoSmall);

								stuecklistepositionDto
										.setFormelArtikelCNr_NOT_IN_DB(flrstuecklisteposition.formelArtikel);
								stuecklistepositionDto.setFormelUebergeordneteArtikelCNr_NOT_IN_DB(
										flrstuecklisteposition.formelUebergeordneterArtikel);

								stuecklistepositionDto.setIId(flrstuecklisteposition.getI_id());
								stuecklistepositionDto.setEinheitCNr(flrstuecklisteposition.getEinheit_c_nr());
								stuecklistepositionDto.setBRuestmenge(flrstuecklisteposition.getB_ruestmenge());
								if (flrstuecklisteposition.getF_dimension1() != null) {
									stuecklistepositionDto
											.setFDimension1(flrstuecklisteposition.getF_dimension1().floatValue());
								}
								if (flrstuecklisteposition.getF_dimension2() != null) {
									stuecklistepositionDto
											.setFDimension2(flrstuecklisteposition.getF_dimension2().floatValue());
								}
								if (flrstuecklisteposition.getF_dimension3() != null) {
									stuecklistepositionDto
											.setFDimension3(flrstuecklisteposition.getF_dimension3().floatValue());
								}

								if (flrstuecklisteposition.getFlrartikel().getI_id() == 31) {
									int u = 0;
								}
								if (flrstuecklisteposition.getFlrartikel().getArtikelart_c_nr()
										.equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
									stuecklistepositionDto.setSHandeingabe("");
								}
								BigDecimal nMengeUnterposition = flrstuecklisteposition.getN_menge();

								stuecklistepositionDto.setNMenge(nMengeUnterposition);

								// Menge nach Zieleinheit umrechnen

								EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(
										flrstuecklisteposition.getEinheit_c_nr(), theClientDto);

								// Positionsmenge nach Zielenge umrechnen
								int dimension = einheitDto.getIDimension().intValue();

								if (dimension == 1) {
									if (flrstuecklisteposition.getF_dimension1() != null) {
										nMengeUnterposition = nMengeUnterposition.multiply(
												new BigDecimal(flrstuecklisteposition.getF_dimension1().doubleValue()));
									}
								} else if (dimension == 2) {
									if (flrstuecklisteposition.getF_dimension1() != null
											&& flrstuecklisteposition.getF_dimension2() != null) {
										nMengeUnterposition = nMengeUnterposition
												.multiply(new BigDecimal(
														flrstuecklisteposition.getF_dimension1().doubleValue()))
												.multiply(new BigDecimal(
														flrstuecklisteposition.getF_dimension2().doubleValue()));
									}
								} else if (dimension == 3) {
									if (flrstuecklisteposition.getF_dimension1() != null
											&& flrstuecklisteposition.getF_dimension2() != null
											&& flrstuecklisteposition.getF_dimension3() != null) {
										nMengeUnterposition = nMengeUnterposition
												.multiply(new BigDecimal(
														flrstuecklisteposition.getF_dimension1().doubleValue()))
												.multiply(new BigDecimal(
														flrstuecklisteposition.getF_dimension2().doubleValue()))
												.multiply(new BigDecimal(
														flrstuecklisteposition.getF_dimension3().doubleValue()));
									}
								}
								BigDecimal faktor = null;
								if (flrstuecklisteposition.getFlrartikel().getEinheit_c_nr()
										.equals(flrstuecklisteposition.getEinheit_c_nr())) {
									faktor = new BigDecimal(1);
								} else {
									faktor = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(1),
											flrstuecklisteposition.getFlrartikel().getEinheit_c_nr(),
											flrstuecklisteposition.getEinheit_c_nr(), flrstuecklisteposition.getI_id(),
											theClientDto);
								}
								if (faktor.doubleValue() != 0) {
									nMengeUnterposition = nMengeUnterposition.divide(faktor,
											BigDecimal.ROUND_HALF_EVEN);

									nMengeUnterposition = Helper.berechneMengeInklusiveVerschnitt(nMengeUnterposition,
											flrstuecklisteposition.getFlrartikel().getF_verschnittfaktor(),
											flrstuecklisteposition.getFlrartikel().getF_verschnittbasis(),
											nWievieleEinheitenDerUnterpositionInStuecklisteI,
											flrstuecklisteposition.getFlrartikel().getF_fertigungs_vpe());

									// PJ 14352

									if (Helper.short2boolean(flrstuecklisteposition.getB_ruestmenge()) == false) {
										// SP4930 Nur Durch Erfassungsfaktor
										// dividieren, wenn
										// keine Ruestmenge
										nMengeUnterposition = nMengeUnterposition.divide(
												new BigDecimal(flrstuecklisteposition.getFlrstueckliste()
														.getN_erfassungsfaktor().doubleValue()),
												12, BigDecimal.ROUND_HALF_EVEN);
									}

									stuecklistepositionDto.setNZielmenge(nMengeUnterposition);
								} else {
									ArrayList<Object> al = new ArrayList<Object>();

									EinheitDto von = getSystemFac().einheitFindByPrimaryKey(
											flrstuecklisteposition.getEinheit_c_nr(), theClientDto);
									EinheitDto zu = getSystemFac().einheitFindByPrimaryKey(
											flrstuecklisteposition.getFlrartikel().getEinheit_c_nr(), theClientDto);
									al.add(von.formatBez() + " <-> " + zu.formatBez());
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT, al,
											new Exception("FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT"));
								}

								if (bBerechneGesamtmengeDerUnterpositionInStuecklisteI) {
									// die Menge der Unterposition muss man
									// multiplizieren mit der
									// Menge der uebergeordneten Position, die
									// in einer Einheit der Stuckliste enthalten
									// ist

									// Ruestmenge zaehlt nur einmal pro Los
									if (Helper.short2boolean(stuecklistepositionDto.getBRuestmenge()) == false) {
										nMengeUnterposition = nMengeUnterposition
												.multiply(nWievieleEinheitenDerUnterpositionInStuecklisteI);
										// SP7467
										stuecklistepositionDto.setNMenge(nMengeUnterposition);
									}

								}

								if (flrstuecklisteposition.getN_menge().signum() != 0) {
									alPositionen.add(getStrukturdatenEinesArtikelsStrukturiertImpl(
											stuecklistepositionDto.getArtikelIId(), bNurZuDruckendeI,
											iStuecklistenaufloesungTiefeI,
											bBerechneGesamtmengeDerUnterpositionInStuecklisteI, nMengeUnterposition,
											bFremdfertigungAufloesenI, bUeberAlleMandanten, stuecklisteMitStrukturDto,
											stuecklistepositionDto, dWBZWennNichtDefiniertInTagen, paramDto, theClientDto));
									
								}
							}

							stuecklisteMitStrukturDto.setPositionen(alPositionen);

							// Arbeitsplan
							ArrayList<StuecklistearbeitsplanDto> alArbeitsplan = new ArrayList<StuecklistearbeitsplanDto>();

							FLRStuecklistearbeitsplan flrStuecklistearbeitsplan = new FLRStuecklistearbeitsplan();
							flrStuecklistearbeitsplan.setStueckliste_i_id(stuecklisteDto.getIId());

							Session session2 = FLRSessionFactory.getFactory().openSession();
							org.hibernate.Criteria crit2 = session2.createCriteria(FLRStuecklistearbeitsplan.class)
									.add(Example.create(flrStuecklistearbeitsplan))
									.createAlias(StuecklisteFac.FLR_STUECKLISTEARBEITSPLAN_FLRARTIKEL, "a");
							crit2.addOrder(Order.asc("a." + ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));

							crit2.addOrder(Order.asc("i_arbeitsgang"));
							crit2.addOrder(Order.asc("i_unterarbeitsgang"));

							List<FLRStuecklistearbeitsplan> results2 = crit2.list();

							if (Helper.isTrue(stuecklisteDto.getBMitFormeln()) && paramDto != null) {
								calculateArbeitsplan(stuecklisteDto.getIId(), paramDto, results2, theClientDto);
							}

							FLRStuecklistearbeitsplan[] arbeitsplan = new FLRStuecklistearbeitsplan[results2.size()];
							arbeitsplan = (FLRStuecklistearbeitsplan[]) results2.toArray(arbeitsplan);

							BigDecimal nSatzgroesse = BigDecimal.ONE;

							for (int i = 0; i < arbeitsplan.length; i++) {

								Long stueckzeit = new Long((long) (arbeitsplan[i].getL_stueckzeit().longValue()
										* nSatzgroesse.doubleValue()));

								if (arbeitsplan[i].getFlrstueckliste().getN_erfassungsfaktor().doubleValue() != 0) {
									stueckzeit = (long) (stueckzeit

											/ arbeitsplan[i].getFlrstueckliste().getN_erfassungsfaktor().doubleValue());
								}

								int iBasisKalkulation = 0;
								try {
									ParametermandantDto parameterDto = getParameterFac().getMandantparameter(
											theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
											ParameterFac.PARAMETER_BASIS_GESAMTKALKULATION);
									iBasisKalkulation = (Integer) parameterDto.getCWertAsObject();
								} catch (RemoteException e) {
									throwEJBExceptionLPRespectOld(e);
								}

								Long ruestzeit = arbeitsplan[i].getL_ruestzeit();

								double posmenge = nSatzgroesse
										.multiply(nWievieleEinheitenDerUnterpositionInStuecklisteI).doubleValue();
								// SP6514
								if (posmenge == 0) {
									ruestzeit = 0L;
								}

								if (iBasisKalkulation == 1
										&& arbeitsplan[i].getFlrstueckliste().getFlrartikel()
												.getF_fertigungssatzgroesse() != null
										&& arbeitsplan[i].getFlrstueckliste().getFlrartikel()
												.getF_fertigungssatzgroesse().doubleValue() > 0) {

									double fsgmenge = arbeitsplan[i].getFlrstueckliste().getFlrartikel()
											.getF_fertigungssatzgroesse().doubleValue();

									while (fsgmenge < posmenge) {

										fsgmenge += arbeitsplan[i].getFlrstueckliste().getFlrartikel()
												.getF_fertigungssatzgroesse().doubleValue();
									}

									ruestzeit = (long) ((ruestzeit / fsgmenge) * nSatzgroesse.doubleValue()
											* nWievieleEinheitenDerUnterpositionInStuecklisteI.doubleValue());

								}

								StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
								stuecklistearbeitsplanDto.setArtikelIId(arbeitsplan[i].getFlrartikel().getI_id());
								stuecklistearbeitsplanDto.setCKommentar(arbeitsplan[i].getC_kommentar());
								stuecklistearbeitsplanDto.setLRuestzeit(ruestzeit);
								stuecklistearbeitsplanDto.setBNurmaschinenzeit(arbeitsplan[i].getB_nurmaschinenzeit());
								stuecklistearbeitsplanDto.setIAufspannung(arbeitsplan[i].getI_aufspannung());
								stuecklistearbeitsplanDto.setXFormel(arbeitsplan[i].getX_formel());
								stuecklistearbeitsplanDto.setMaschineIId(arbeitsplan[i].getMaschine_i_id());
								stuecklistearbeitsplanDto.setLStueckzeit(stueckzeit);

								stuecklistearbeitsplanDto.setIArbeitsgang(arbeitsplan[i].getI_arbeitsgang());
								stuecklistearbeitsplanDto.setIUnterarbeitsgang(arbeitsplan[i].getI_unterarbeitsgang());
								stuecklistearbeitsplanDto.setCKommentar(arbeitsplan[i].getC_kommentar());

								alArbeitsplan.add(stuecklistearbeitsplanDto);

							}
							stuecklisteMitStrukturDto.setArbeitsplanPositionen(alArbeitsplan);

						} catch (EJBExceptionLP ex) {
							throw ex;
						} catch (RemoteException ex) {
							throwEJBExceptionLPRespectOld(ex);
						} catch (Throwable t) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
						} finally {
							try {
								session.close();
							} catch (HibernateException he) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
							}
						}
					}
				}
			}
		}

		return stuecklisteMitStrukturDto;
	}

}
