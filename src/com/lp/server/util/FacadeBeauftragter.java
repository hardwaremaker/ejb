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
package com.lp.server.util;

import java.io.Serializable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklpositionFac;
import com.lp.server.artikel.ejbfac.WebshopItemServiceFacLocal;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikelimportFac;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.FehlmengeFac;
import com.lp.server.artikel.service.InventurFac;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.artikel.service.MaterialFac;
import com.lp.server.artikel.service.RahmenbedarfeFac;
import com.lp.server.artikel.service.ReservierungFac;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.auftrag.ejbfac.WebshopOrderServiceFacLocal;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragRahmenAbrufFac;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.auftrag.service.AuftragteilnehmerFac;
import com.lp.server.benutzer.ejbfac.BenutzerServicesFacLocal;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BSMahnwesenFac;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.bestellung.service.BestellungServiceFac;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungServiceFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.FertigungServiceFac;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.finanz.service.BelegbuchungFac;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.MahnwesenFac;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.instandhaltung.service.InstandhaltungFac;
import com.lp.server.kueche.service.KuecheFac;
import com.lp.server.kueche.service.KuecheReportFac;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.media.ejbfac.EmailMediaLocalFac;
import com.lp.server.media.service.EmailMediaFac;
import com.lp.server.partner.ejbfac.WebshopCustomerServiceFacLocal;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.BankFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.KundesachbearbeiterFac;
import com.lp.server.partner.service.KundesokoFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.LieferantServicesFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.personal.service.MaschineFac;
import com.lp.server.personal.service.PersonalApiFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.ejbfac.BatcherFac;
import com.lp.server.system.ejbfac.BatcherSingleTransactionFac;
import com.lp.server.system.fastlanereader.service.FastLaneReader;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.JCRMediaFac;
import com.lp.server.system.pkgenerator.bl.BelegnummerGeneratorObj;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.AutoBestellvorschlagFac;
import com.lp.server.system.service.AutoFehlmengendruckFac;
import com.lp.server.system.service.AutoMahnenFac;
import com.lp.server.system.service.AutoMahnungsversandFac;
import com.lp.server.system.service.AutoPaternosterFac;
import com.lp.server.system.service.AutoRahmendetailbedarfdruckFac;
import com.lp.server.system.service.AutomatikjobFac;
import com.lp.server.system.service.AutomatikjobtypeFac;
import com.lp.server.system.service.AutomatiktimerFac;
import com.lp.server.system.service.BelegpositionkonvertierungFac;
import com.lp.server.system.service.DokumenteFac;
import com.lp.server.system.service.DruckerFac;
import com.lp.server.system.service.IntelligenterStklImportFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.PflegeFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemMultilanguageFac;
import com.lp.server.system.service.SystemReportFac;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientFac;
import com.lp.server.system.service.TheJudgeFac;
import com.lp.server.system.service.VersandFac;
import com.lp.util.EJBExceptionLP;

/**
 * 
 * <p>
 * <I>In dieser Klasse stehen alle unsere Facades zur Verfuegung.</I>
 * </p>
 * <br>
 * Jeder, der auf die Facades zugreifen moechte, muss diese Klasse
 * instantiieren. <br>
 * beauftragt: 0
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>17. 01. 2005</I>
 * </p>
 * 
 * verantwortlich: Martin Bluehweis
 * 
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */
public class FacadeBeauftragter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PKGeneratorObj pkGeneratorObj = null;
	private BelegnummerGeneratorObj bnGeneratorObj = null;
	private TheJudgeFac theJudgeFac = null;
	private TheClientFac theClientFac = null;
	private AnsprechpartnerFac ansprechpartnerFac = null;
	private AuftragFac auftragFac = null;
	private AuftragpositionFac auftragpositionFac = null;
	private AuftragReportFac auftragReportFac = null;
	private AuftragServiceFac auftragServiceFac = null;
	private AnfrageFac anfrageFac = null;
	private AnfragepositionFac anfragepositionFac = null;
	private AnfrageServiceFac anfrageServiceFac = null;
	private BestellungFac bestellungFac = null;
	private BestellungServiceFac bestellungServiceFac = null;
	private BestellpositionFac bestellpositionFac = null;
	private KundeFac kundeFac = null;
	private KundeReportFac kundeReportFac = null;
	private KundesachbearbeiterFac kundesachbearbeiterFac = null;
	private LagerFac lagerFac = null;
	private LagerReportFac lagerReportFac = null;
	private WareneingangFac wareneingangFac = null;
	private BestellvorschlagFac bestellvorschlagFac = null;
	private LieferscheinFac lieferscheinFac = null;
	private LieferscheinpositionFac lieferscheinpositionFac = null;
	private LieferscheinReportFac lieferscheinReportFac = null;
	private LieferscheinServiceFac lieferscheinServiceFac = null;
	private LocaleFac localeFac = null;
	private MandantFac mandantFac = null;
	private PartnerFac partnerFac = null;
	private PanelFac panelFac = null;
	private PflegeFac pflegeFac = null;
	private PartnerServicesFac partnerServicesFac = null;
	private PersonalFac personalFac = null;
	private ReklamationFac reklamationFac = null;
	private ReservierungFac reservierungFac = null;
	private RechnungFac rechnungFac = null;
	private BuchenFac buchenFac = null;
	private FinanzFac finanzFac = null;
	private FinanzServiceFac finanzServiceFac = null;
	private FinanzReportFac finanzReportFac = null;
	private MahnwesenFac mahnwesenFac = null;
	private SystemMultilanguageFac systemMultilanguageFac = null;
	private SystemFac systemFac = null;
	private SystemServicesFac systemServicesFac = null;
	private VkPreisfindungFac vkPreisfindungFac = null;
	private ArtikelFac artikelFac = null;
	private ArtikelReportFac artikelReportFac = null;
	private ParameterFac parameterFac = null;
	private LieferantFac lieferantFac = null;
	private LieferantServicesFac lieferantServicesFac = null;
	private RechnungReportFac rechnungReportFac = null;
	private RechnungServiceFac rechnungServiceFac = null;
	private EingangsrechnungFac eingangsrechnungFac = null;
	private EingangsrechnungReportFac eingangsrechnungReportFac = null;
	private EingangsrechnungServiceFac eingangsrechnungServiceFac = null;
	private ZeiterfassungFac zeiterfassungFac = null;
	private ZeiterfassungReportFac zeiterfassungReportFac = null;
	private VersandFac versandFac = null;
	private BankFac bankFac = null;
	private BenutzerFac benutzerFac = null;
	private MaterialFac materialFac = null;
	private MediaFac mediaFac = null;
	private ArtikelbestelltFac artikelbestelltFac = null;
	private RechteFac rechteFac = null;
	private AngebotFac angebotFac = null;
	private AngebotpositionFac angebotpositionFac = null;
	private AngebotReportFac angebotReportFac = null;
	private AngebotServiceFac angebotServiceFac = null;
	private FertigungFac fertigungFac = null;
	private FertigungReportFac fertigungReportFac = null;
	private FertigungServiceFac fertigungServiceFac = null;
	private StuecklisteFac stuecklisteFac = null;
	private InventurFac inventurFac = null;
	private FehlmengeFac fehlmengeFac = null;
	private BSMahnwesenFac bsmahnwesenFac = null;
	private ArtikelkommentarFac artikelkommentarFac = null;
	private PartnerReportFac partnerReportFac = null;
	private InternebestellungFac internebestellungFac = null;
	private AngebotstklFac angebotstklFac = null;
	private AngebotstklpositionFac angebotstklpositionFac = null;
	private FibuExportFac fibuExportFac = null;
	private DruckerFac druckerFac = null;
	private KundesokoFac kundesokoFac = null;
	private BelegbuchungFac belegbuchungFac = null;
	private BelegbuchungFac belegbuchungIstversteurerFac = null;
	private BelegbuchungFac belegbuchungMischversteurerFac = null;
	private DokumenteFac dokumenteFac = null;
	private ZutrittscontrollerFac zutrittscontrollerFac = null;
	private ProjektFac projektFac = null;
	private ProjektServiceFac projektServiceFac = null;
	private ZahlungsvorschlagFac zahlungsvorschlagFac = null;
	private BelegpositionkonvertierungFac belegpositionkonvertierungFac = null;
	private RahmenbedarfeFac rahmenbedarfeFac = null;
	private SystemReportFac systemReportFac = null;
	private AuftragteilnehmerFac auftragteilnehmerFac = null;
	private StuecklisteReportFac stuecklisteReportFac = null;
	private AutoFehlmengendruckFac autoFehlmengendruckFac = null;
	private AutoMahnungsversandFac autoMahnungsversandFac = null;
	private AutoMahnenFac autoMahnenFac = null;
	private BestellungReportFac bestellungReportFac = null;
	private AutoBestellvorschlagFac autoBestellvorschlagFac = null;
	private AutoPaternosterFac autoPaternosterFac = null;
	private InstandhaltungFac instandhaltungFac = null;
	private AutomatikjobFac automatikjobFac = null;
	private AutomatikjobtypeFac automatikjobtypeFac = null;
	private AutomatiktimerFac automatiktimerFac = null;
	private LogonFac logonFac=null;
	private JCRDocFac jcrDocFac=null;
	private AutoRahmendetailbedarfdruckFac autoRahmendetailbedarfdruckFac=null;
	private KuecheFac kuecheFac=null;
	private BelegVerkaufFac belegVerkaufFac= null;
	private KuecheReportFac kuecheReportFac=null;
	private AuftragRahmenAbrufFac auftragRahmenAbrufFac=null;
	private BenutzerServicesFacLocal benutzerServicesFac=null;
	private FastLaneReader fastLaneReader=null;
	private WebshopItemServiceFacLocal webshopItemServicesFac = null ;
	private	WebshopOrderServiceFacLocal webshopOrderServicesFac = null ;
	private	WebshopOrderServiceFacLocal webshopCustomerOrderServicesFac = null ;
	private	InseratFac inseratFac = null ;
	private WebshopCustomerServiceFacLocal webshopCustomerServicesFac = null ;
	private JCRMediaFac jcrMediaFac = null ;
	private EmailMediaFac emailMediaFac = null ;
	private EmailMediaLocalFac emailMediaLocalFac = null ;
	private BatcherFac batcherFac = null ;
	private BatcherSingleTransactionFac batcherSingleTransactionFac = null ;
	private MaschineFac maschineFac = null ;
	private ArtikelimportFac artikelimportFac = null ;
	private PersonalApiFac personalApiFac = null ;
	private IntelligenterStklImportFac intellStklimportFac = null;
	
	@PersistenceContext
	private EntityManager em;

	public Context context;

	public FacadeBeauftragter() {
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public Context getInitialContext() {
		return context;
	}

	/**
	 * Hole die Clientinformationen des Users.
	 * 
	 * @param idUser
	 *            String
	 * @return TheClientDto
	 * @throws EJBExceptionLP
	 */
	public final TheClientDto getTheClient(String idUser) throws EJBExceptionLP {
		try {
			return getTheClientFac().theClientFindByPrimaryKey(idUser);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
	}

	/**
	 * SessionFacade fuer TheClient holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public TheClientFac getTheClientFac() throws EJBExceptionLP {
		try {
			if (theClientFac == null) {
				theClientFac = (TheClientFac) context.lookup("lpserver/TheClientFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return theClientFac;
	}

	/**
	 * SessionFacade fuer Ansprechpartner holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AnsprechpartnerFac
	 */
	public AnsprechpartnerFac getAnsprechpartnerFac() throws EJBExceptionLP {
		try {
			if (ansprechpartnerFac == null) {
				ansprechpartnerFac = (AnsprechpartnerFac) context
						.lookup("lpserver/AnsprechpartnerFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return ansprechpartnerFac;
	}

	/**
	 * SessionFacade fuer Auftrag holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragFac
	 */
	public AuftragFac getAuftragFac() throws EJBExceptionLP {
		try {
			if (auftragFac == null) {
				auftragFac = (AuftragFac) context.lookup("lpserver/AuftragFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return auftragFac;
	}

	/**
	 * SessionFacade fuer Auftragteilnehmer holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragteilnehmerFac
	 */
	public AuftragteilnehmerFac getAuftragteilnehmerFac() throws EJBExceptionLP {
		try {
			if (auftragteilnehmerFac == null) {
				auftragteilnehmerFac = (AuftragteilnehmerFac) context
						.lookup("lpserver/AuftragteilnehmerFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return auftragteilnehmerFac;
	}

	public AnfrageFac getAnfrageFac() throws EJBExceptionLP {
		try {
			if (anfrageFac == null) {
				anfrageFac = (AnfrageFac) context.lookup("lpserver/AnfrageFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return anfrageFac;
	}

	public AnfragepositionFac getAnfragepositionFac() throws EJBExceptionLP {
		try {
			if (anfragepositionFac == null) {
				anfragepositionFac = (AnfragepositionFac) context
						.lookup("lpserver/AnfragepositionFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return anfragepositionFac;
	}

	public AnfrageServiceFac getAnfrageServiceFac() throws EJBExceptionLP {
		try {
			if (anfrageServiceFac == null) {
				anfrageServiceFac = (AnfrageServiceFac) context
						.lookup("lpserver/AnfrageServiceFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return anfrageServiceFac;
	}

	/**
	 * SessionFacade fuer Bestellung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return BestellungFac
	 */
	public BestellungFac getBestellungFac() throws EJBExceptionLP {
		try {
			if (bestellungFac == null) {
				bestellungFac = (BestellungFac) context.lookup("lpserver/BestellungFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bestellungFac;
	}

	public BestellungServiceFac getBestellungServiceFac() throws EJBExceptionLP {
		try {
			if (bestellungServiceFac == null) {
				bestellungServiceFac = (BestellungServiceFac) context
						.lookup("lpserver/BestellungServiceFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return bestellungServiceFac;
	}

	/**
	 * SessionFacade fuer ArtikelFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelFac
	 */
	public ArtikelFac getArtikelFac() throws EJBExceptionLP {
		try {
			if (artikelFac == null) {
				artikelFac = (ArtikelFac) context.lookup("lpserver/ArtikelFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelFac;
	}

	public ArtikelimportFac getArtikelimportFac() throws EJBExceptionLP {
		try {
			if (artikelimportFac == null) {
				artikelimportFac = (ArtikelimportFac) context.lookup("lpserver/ArtikelimportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelimportFac;
	}

	/**
	 * SessionFacade fuer ArtikelReportFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelReportFac
	 */
	public ArtikelReportFac getArtikelReportFac() throws EJBExceptionLP {
		try {
			if (artikelReportFac == null) {
				artikelReportFac = (ArtikelReportFac) context
						.lookup("lpserver/ArtikelReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelReportFac;
	}

	/**
	 * SessionFacade fuer ArtikelkommentarFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelkommentarFac
	 */
	public ArtikelkommentarFac getArtikelkommentarFac() throws EJBExceptionLP {
		try {
			if (artikelkommentarFac == null) {
				artikelkommentarFac = (ArtikelkommentarFac) context
						.lookup("lpserver/ArtikelkommentarFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelkommentarFac;
	}

	/**
	 * SessionFacade fuer InventurFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelFac
	 */
	public InventurFac getInventurFac() throws EJBExceptionLP {
		try {
			if (inventurFac == null) {
				inventurFac = (InventurFac) context.lookup("lpserver/InventurFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return inventurFac;
	}

	/**
	 * SessionFacade fuer Benutzer holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return BenutzerFac
	 */
	public BenutzerFac getBenutzerFac() throws EJBExceptionLP {
		try {
			if (benutzerFac == null) {
				benutzerFac = (BenutzerFac) context.lookup("lpserver/BenutzerFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return benutzerFac;
	}

	/**
	 * SessionFacade fuer Auftragposition holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragpositionFac
	 */
	public AuftragpositionFac getAuftragpositionFac() throws EJBExceptionLP {
		try {
			if (auftragpositionFac == null) {
				auftragpositionFac = (AuftragpositionFac) context
						.lookup("lpserver/AuftragpositionFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return auftragpositionFac;
	}

	public AuftragReportFac getAuftragReportFac() throws EJBExceptionLP {
		try {
			if (auftragReportFac == null) {
				auftragReportFac = (AuftragReportFac) context
						.lookup("lpserver/AuftragReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return auftragReportFac;
	}

	/**
	 * SessionFacade fuer AuftragServices holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragServiceFac
	 */
	public AuftragServiceFac getAuftragServiceFac() throws EJBExceptionLP {
		try {
			if (auftragServiceFac == null) {
				auftragServiceFac = (AuftragServiceFac) context
						.lookup("lpserver/AuftragServiceFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return auftragServiceFac;
	}

	/**
	 * SessionFacade fuer Bestellposition holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return BestellpositionFac
	 */
	public BestellpositionFac getBestellpositionFac() throws EJBExceptionLP {
		try {
			if (bestellpositionFac == null) {
				bestellpositionFac = (BestellpositionFac) context
						.lookup("lpserver/BestellpositionFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bestellpositionFac;
	}

	/**
	 * SessionFacade fuer Kunde holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return KundeFac
	 */
	public KundeFac getKundeFac() throws EJBExceptionLP {
		try {
			if (kundeFac == null) {
				kundeFac = (KundeFac) context.lookup("lpserver/KundeFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kundeFac;
	}

	/**
	 * SessionFacade fuer Kundesachbearbeiter holen.
	 * 
	 * @return KundesachbearbeiterFac
	 * @throws EJBExceptionLP
	 */
	public KundesachbearbeiterFac getKundesachbearbeiterFac()
			throws EJBExceptionLP {
		try {
			if (kundesachbearbeiterFac == null) {
				kundesachbearbeiterFac = (KundesachbearbeiterFac) context
						.lookup("lpserver/KundesachbearbeiterFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kundesachbearbeiterFac;
	}

	/**
	 * SessionFacade fuer KundeReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return KundeFac
	 */
	public KundeReportFac getKundeReportFac() throws EJBExceptionLP {
		try {
			if (kundeReportFac == null) {
				kundeReportFac = (KundeReportFac) context
						.lookup("lpserver/KundeReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kundeReportFac;
	}

	/**
	 * SessionFacade fuer Lager holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LagerFac
	 */
	public LagerFac getLagerFac() throws EJBExceptionLP {
		try {
			if (lagerFac == null) {
				lagerFac = (LagerFac) context.lookup("lpserver/LagerFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lagerFac;
	}
	public InseratFac getInseratFac() throws EJBExceptionLP {
		try {
			if (inseratFac == null) {
				inseratFac = (InseratFac) context.lookup("lpserver/InseratFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return inseratFac;
	}
	public LagerReportFac getLagerReportFac() throws EJBExceptionLP {
		try {
			if (lagerReportFac == null) {
				lagerReportFac = (LagerReportFac) context.lookup("lpserver/LagerReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lagerReportFac;
	}

	/**
	 * SessionFacade fuer Wareneingang holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return WareneingangFac
	 */
	public WareneingangFac getWareneingangFac() throws EJBExceptionLP {
		try {
			if (wareneingangFac == null) {
				wareneingangFac = (WareneingangFac) context
						.lookup("lpserver/WareneingangFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return wareneingangFac;
	}

	/**
	 * SessionFacade fuer Bestellvorschlag holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return BestellvorschlagFac
	 */
	public BestellvorschlagFac getBestellvorschlagFac() throws EJBExceptionLP {
		try {
			if (bestellvorschlagFac == null) {
				bestellvorschlagFac = (BestellvorschlagFac) context
						.lookup("lpserver/BestellvorschlagFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bestellvorschlagFac;
	}

	/**
	 * SessionFacade fuer Lieferschein holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LieferscheinFac
	 */
	public LieferscheinFac getLieferscheinFac() throws EJBExceptionLP {
		try {
			if (lieferscheinFac == null) {
				lieferscheinFac = (LieferscheinFac) context
						.lookup("lpserver/LieferscheinFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lieferscheinFac;
	}

	/**
	 * SessionFacade fuer Reklamation holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReklamationFac
	 */
	public ReklamationFac getReklamationFac() throws EJBExceptionLP {
		try {
			if (reklamationFac == null) {
				reklamationFac = (ReklamationFac) context
						.lookup("lpserver/ReklamationFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return reklamationFac;
	}

	/**
	 * SessionFacade fuer Lieferscheinposition holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LieferscheinpositionFac
	 */
	public LieferscheinpositionFac getLieferscheinpositionFac()
			throws EJBExceptionLP {
		try {
			if (lieferscheinpositionFac == null) {
				lieferscheinpositionFac = (LieferscheinpositionFac) context
						.lookup("lpserver/LieferscheinpositionFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return lieferscheinpositionFac;
	}

	public LieferscheinReportFac getLieferscheinReportFac()
			throws EJBExceptionLP {
		try {
			if (lieferscheinReportFac == null) {
				lieferscheinReportFac = (LieferscheinReportFac) context
						.lookup("lpserver/LieferscheinReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return lieferscheinReportFac;
	}

	public LieferscheinServiceFac getLieferscheinServiceFac()
			throws EJBExceptionLP {
		try {
			if (lieferscheinServiceFac == null) {
				lieferscheinServiceFac = (LieferscheinServiceFac) context
						.lookup("lpserver/LieferscheinServiceFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return lieferscheinServiceFac;
	}

	/**
	 * SessionFacade fuer Lieferant holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LieferantFac
	 */
	public LieferantFac getLieferantFac() throws EJBExceptionLP {
		try {
			if (lieferantFac == null) {
				lieferantFac = (LieferantFac) context.lookup("lpserver/LieferantFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lieferantFac;
	}

	public LieferantServicesFac getLieferantServicesFac() throws EJBExceptionLP {
		try {
			if (lieferantServicesFac == null) {
				lieferantServicesFac = (LieferantServicesFac) context
						.lookup("lpserver/LieferantServicesFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lieferantServicesFac;
	}

	/**
	 * SessionFacade fuer System Locale holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LocaleFac
	 */
	public LocaleFac getLocaleFac() throws EJBExceptionLP {
		try {
			if (localeFac == null) {
				localeFac = (LocaleFac) context.lookup("lpserver/LocaleFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return localeFac;
	}

	/**
	 * SessionFacade fuer System Mandant holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return MandantFac
	 */
	public MandantFac getMandantFac() throws EJBExceptionLP {
		try {
			if (mandantFac == null) {
				mandantFac = (MandantFac) context.lookup("lpserver/MandantFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return mandantFac;
	}

	/**
	 * SessionFacade fuer Partner holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return PartnerFac
	 */
	public PartnerFac getPartnerFac() throws EJBExceptionLP {
		try {
			if (partnerFac == null) {
				partnerFac = (PartnerFac) context.lookup("lpserver/PartnerFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return partnerFac;
	}

	public PanelFac getPanelFac() throws EJBExceptionLP {
		try {
			if (panelFac == null) {
				panelFac = (PanelFac) context.lookup("lpserver/PanelFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return panelFac;
	}
	public PflegeFac getPflegeFac() throws EJBExceptionLP {
		try {
			if (pflegeFac == null) {
				pflegeFac = (PflegeFac) context.lookup("lpserver/PflegeFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return pflegeFac;
	}

	public PartnerServicesFac getPartnerServicesFac() throws EJBExceptionLP {
		try {
			if (partnerServicesFac == null) {
				partnerServicesFac = (PartnerServicesFac) context
						.lookup("lpserver/PartnerServicesFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return partnerServicesFac;
	}

	public PartnerReportFac getPartnerReportFac() throws EJBExceptionLP {
		try {
			if (partnerReportFac == null) {
				partnerReportFac = (PartnerReportFac) context
						.lookup("lpserver/PartnerReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return partnerReportFac;
	}

	/**
	 * SessionFacade fuer Personal holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return PersonalFac
	 */
	public PersonalFac getPersonalFac() throws EJBExceptionLP {
		try {
			if (personalFac == null) {
				personalFac = (PersonalFac) context.lookup("lpserver/PersonalFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return personalFac;
	}

	/**
	 * SessionFacade fuer Reservierung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public ReservierungFac getReservierungFac() throws EJBExceptionLP {
		try {
			if (reservierungFac == null) {
				reservierungFac = (ReservierungFac) context
						.lookup("lpserver/ReservierungFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return reservierungFac;
	}

	public ArtikelbestelltFac getArtikelbestelltFac() throws EJBExceptionLP {
		try {
			if (artikelbestelltFac == null) {
				artikelbestelltFac = (ArtikelbestelltFac) context
						.lookup("lpserver/ArtikelbestelltFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelbestelltFac;
	}

	public RechteFac getRechteFac() throws EJBExceptionLP {
		try {
			if (rechteFac == null) {
				rechteFac = (RechteFac) context.lookup("lpserver/RechteFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return rechteFac;
	}

	/**
	 * SessionFacade fuer Rechnung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechnungFac
	 */
	public RechnungFac getRechnungFac() throws EJBExceptionLP {
		try {
			if (rechnungFac == null) {
				rechnungFac = (RechnungFac) context.lookup("lpserver/RechnungFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return rechnungFac;
	}

	/**
	 * SessionFacade fuer RechnungReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechnungFac
	 */
	public RechnungReportFac getRechnungReportFac() throws EJBExceptionLP {
		try {
			if (rechnungReportFac == null) {
				rechnungReportFac = (RechnungReportFac) context
						.lookup("lpserver/RechnungReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return rechnungReportFac;
	}

	/**
	 * SessionFacade fuer RechnungService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechnungFac
	 */
	public RechnungServiceFac getRechnungServiceFac() throws EJBExceptionLP {
		try {
			if (rechnungServiceFac == null) {
				rechnungServiceFac = (RechnungServiceFac) context
						.lookup("lpserver/RechnungServiceFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return rechnungServiceFac;
	}

	/**
	 * SessionFacade fuer Buchen holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public BuchenFac getBuchenFac() throws EJBExceptionLP {
		try {
			if (buchenFac == null) {
				buchenFac = (BuchenFac) context.lookup("lpserver/BuchenFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return buchenFac;
	}
	
	public InstandhaltungFac getInstandhaltungFac() throws EJBExceptionLP {
		try {
			if (instandhaltungFac == null) {
				instandhaltungFac = (InstandhaltungFac) context.lookup("lpserver/InstandhaltungFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return instandhaltungFac;
	}
	

	/**
	 * SessionFacade fuer Finanz holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public FinanzFac getFinanzFac() throws EJBExceptionLP {
		try {
			if (finanzFac == null) {
				finanzFac = (FinanzFac) context.lookup("lpserver/FinanzFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return finanzFac;
	}

	/**
	 * SessionFacade fuer Stueckliste holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public StuecklisteFac getStuecklisteFac() throws EJBExceptionLP {
		try {
			if (stuecklisteFac == null) {
				stuecklisteFac = (StuecklisteFac) context
						.lookup("lpserver/StuecklisteFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return stuecklisteFac;
	}
	
	public IntelligenterStklImportFac getIntelligenterStklImportFac() throws EJBExceptionLP {
		try {
			if (intellStklimportFac == null) {
				intellStklimportFac = (IntelligenterStklImportFac) context
						.lookup("lpserver/IntelligenterStklImportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return intellStklimportFac;
	}

	/**
	 * SessionFacade fuer FinanzReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return FinanzReportFac
	 */
	public FinanzReportFac getFinanzReportFac() throws EJBExceptionLP {
		try {
			if (finanzReportFac == null) {
				finanzReportFac = (FinanzReportFac) context
						.lookup("lpserver/FinanzReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return finanzReportFac;
	}

	/**
	 * SessionFacade fuer FinanzService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return FinanzServiceFac
	 */
	public FinanzServiceFac getFinanzServiceFac() throws EJBExceptionLP {
		try {
			if (finanzServiceFac == null) {
				finanzServiceFac = (FinanzServiceFac) context
						.lookup("lpserver/FinanzServiceFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return finanzServiceFac;
	}

	/**
	 * SessionFacade fuer Mahnwesen holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public MahnwesenFac getMahnwesenFac() throws EJBExceptionLP {
		try {
			if (mahnwesenFac == null) {
				mahnwesenFac = (MahnwesenFac) context.lookup("lpserver/MahnwesenFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return mahnwesenFac;
	}

	/**
	 * SessionFacade fuer Mahnwesen holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public BSMahnwesenFac getBSMahnwesenFac() throws EJBExceptionLP {
		try {
			if (bsmahnwesenFac == null) {
				bsmahnwesenFac = (BSMahnwesenFac) context
						.lookup("lpserver/BSMahnwesenFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bsmahnwesenFac;
	}

	public StuecklisteReportFac getStuecklisteReportFac() throws EJBExceptionLP {
		try {
			if (stuecklisteReportFac == null) {
				stuecklisteReportFac = (StuecklisteReportFac) context
						.lookup("lpserver/StuecklisteReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return stuecklisteReportFac;
	}

	/**
	 * SessionFacade fuer SystemMultilanguage holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public SystemMultilanguageFac getSystemMultilanguageFac()
			throws EJBExceptionLP {
		try {
			if (systemMultilanguageFac == null) {
				systemMultilanguageFac = (SystemMultilanguageFac) context
						.lookup("lpserver/SystemMultilanguageFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return systemMultilanguageFac;
	}

	/**
	 * SessionFacade fuer System holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public SystemFac getSystemFac() throws EJBExceptionLP {
		try {
			if (systemFac == null) {
				systemFac = (SystemFac) context.lookup("lpserver/SystemFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return systemFac;
	}

	public SystemReportFac getSystemReportFac() throws EJBExceptionLP {

		if (systemReportFac == null) {
			try {
				systemReportFac = (SystemReportFac) context
						.lookup("lpserver/SystemReportFacBean/remote");
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
						new Exception(t));
			}
		}
		return systemReportFac;
	}

	public SystemServicesFac getSystemServicesFac() throws EJBExceptionLP {
		if (systemServicesFac == null) {
			try {
				systemServicesFac = (SystemServicesFac) context
						.lookup("lpserver/SystemServicesFacBean/remote");
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
						new Exception(t));
			}

		}
		return systemServicesFac;
	}

	/**
	 * SessionFacade fuer VkPreisfindungFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VkPreisfindungFac
	 */
	public VkPreisfindungFac getVkPreisfindungFac() throws EJBExceptionLP {
		try {
			if (vkPreisfindungFac == null) {
				vkPreisfindungFac = (VkPreisfindungFac) context
						.lookup("lpserver/VkPreisfindungFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return vkPreisfindungFac;
	}

	/**
	 * SessionFacade fuer VkPreisfindungFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VkPreisfindungFac
	 */
	public ZeiterfassungFac getZeiterfassungFac() throws EJBExceptionLP {
		try {
			if (zeiterfassungFac == null) {
				zeiterfassungFac = (ZeiterfassungFac) context
						.lookup("lpserver/ZeiterfassungFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return zeiterfassungFac;
	}
	public MaschineFac getMaschineFac() throws EJBExceptionLP {
		try {
			if (maschineFac == null) {
				maschineFac = (MaschineFac) context
						.lookup("lpserver/MaschineFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return maschineFac;
	}
	public ZeiterfassungReportFac getZeiterfassungReportFac() throws EJBExceptionLP {
		try {
			if (zeiterfassungReportFac == null) {
				zeiterfassungReportFac = (ZeiterfassungReportFac) context
						.lookup("lpserver/ZeiterfassungReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return zeiterfassungReportFac;
	}

	public ZutrittscontrollerFac getZutrittscontrollerFac()
			throws EJBExceptionLP {
		try {
			if (zutrittscontrollerFac == null) {
				zutrittscontrollerFac = (ZutrittscontrollerFac) context
						.lookup("lpserver/ZutrittscontrollerFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return zutrittscontrollerFac;
	}

	/**
	 * SessionFacade fuer ParameterFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VkPreisfindungFac
	 */
	public ParameterFac getParameterFac() throws EJBExceptionLP {
		try {
			if (parameterFac == null) {
				parameterFac = (ParameterFac) context.lookup("lpserver/ParameterFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return parameterFac;
	}

	/**
	 * SessionFacade fuer Eingangsrechnung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungFac
	 */
	public EingangsrechnungFac getEingangsrechnungFac() throws EJBExceptionLP {
		try {
			if (eingangsrechnungFac == null) {
				eingangsrechnungFac = (EingangsrechnungFac) context
						.lookup("lpserver/EingangsrechnungFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return eingangsrechnungFac;
	}

	/**
	 * SessionFacade fuer EingangsrechnungReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungReportFac
	 */
	public EingangsrechnungReportFac getEingangsrechnungReportFac()
			throws EJBExceptionLP {
		try {
			if (eingangsrechnungReportFac == null) {
				eingangsrechnungReportFac = (EingangsrechnungReportFac) context
						.lookup("lpserver/EingangsrechnungReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return eingangsrechnungReportFac;
	}

	/**
	 * SessionFacade fuer EingangsrechnungService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungServiceFac
	 */
	public EingangsrechnungServiceFac getEingangsrechnungServiceFac()
			throws EJBExceptionLP {
		try {
			if (eingangsrechnungServiceFac == null) {
				eingangsrechnungServiceFac = (EingangsrechnungServiceFac) context
						.lookup("lpserver/EingangsrechnungServiceFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return eingangsrechnungServiceFac;
	}

	/**
	 * SessionFacade fuer Versand holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VersandFac
	 */
	public VersandFac getVersandFac() throws EJBExceptionLP {
		try {
			if (versandFac == null) {
				versandFac = (VersandFac) context.lookup("lpserver/VersandFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return versandFac;
	}

	/**
	 * SessionFacade fuer Bank holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VersandFac
	 */
	public BankFac getBankFac() throws EJBExceptionLP {
		try {
			if (bankFac == null) {
				bankFac = (BankFac) context.lookup("lpserver/BankFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bankFac;
	}

	/**
	 * SessionFacade fuer Material holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VersandFac
	 */
	public MaterialFac getMaterialFac() throws EJBExceptionLP {
		try {
			if (materialFac == null) {
				materialFac = (MaterialFac) context.lookup("lpserver/MaterialFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return materialFac;
	}

	public MediaFac getMediaFac() throws EJBExceptionLP {
		try {
			if (mediaFac == null) {
				mediaFac = (MediaFac) context.lookup("lpserver/MediaFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return mediaFac;
	}

	public AngebotFac getAngebotFac() throws EJBExceptionLP {
		try {
			if (angebotFac == null) {
				angebotFac = (AngebotFac) context.lookup("lpserver/AngebotFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotFac;
	}

	public AngebotpositionFac getAngebotpositionFac() throws EJBExceptionLP {
		try {
			if (angebotpositionFac == null) {
				angebotpositionFac = (AngebotpositionFac) context
						.lookup("lpserver/AngebotpositionFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotpositionFac;
	}

	public AngebotReportFac getAngebotReportFac() throws EJBExceptionLP {
		try {
			if (angebotReportFac == null) {
				angebotReportFac = (AngebotReportFac) context
						.lookup("lpserver/AngebotReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotReportFac;
	}

	public AngebotServiceFac getAngebotServiceFac() throws EJBExceptionLP {
		try {
			if (angebotServiceFac == null) {
				angebotServiceFac = (AngebotServiceFac) context
						.lookup("lpserver/AngebotServiceFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotServiceFac;
	}

	public FertigungFac getFertigungFac() throws EJBExceptionLP {
		try {
			if (fertigungFac == null) {
				fertigungFac = (FertigungFac) context.lookup("lpserver/FertigungFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fertigungFac;
	}

	public FertigungReportFac getFertigungReportFac() throws EJBExceptionLP {
		try {
			if (fertigungReportFac == null) {
				fertigungReportFac = (FertigungReportFac) context
						.lookup("lpserver/FertigungReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fertigungReportFac;
	}

	public FertigungServiceFac getFertigungServiceFac() throws EJBExceptionLP {
		try {
			if (fertigungServiceFac == null) {
				fertigungServiceFac = (FertigungServiceFac) context
						.lookup("lpserver/FertigungServiceFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fertigungServiceFac;
	}

	public FehlmengeFac getFehlmengeFac() throws EJBExceptionLP {
		try {
			if (fehlmengeFac == null) {
				fehlmengeFac = (FehlmengeFac) context.lookup("lpserver/FehlmengeFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fehlmengeFac;
	}

	public InternebestellungFac getInternebestellungFac() throws EJBExceptionLP {
		try {
			if (internebestellungFac == null) {
				internebestellungFac = (InternebestellungFac) context
						.lookup("lpserver/InternebestellungFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return internebestellungFac;
	}

	public BenutzerServicesFacLocal getBenutzerServicesFac() throws EJBExceptionLP {
		try {
			if (benutzerServicesFac == null) {
				benutzerServicesFac = (BenutzerServicesFacLocal) context
						.lookup("lpserver/BenutzerServicesFacBean/local");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return benutzerServicesFac;
	}
	public FastLaneReader getFastLaneReader() throws EJBExceptionLP {
		try {
			if (fastLaneReader == null) {
				fastLaneReader = (FastLaneReader) context
						.lookup("lpserver/FastLaneReaderBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fastLaneReader;
	}

	public AngebotstklFac getAngebotstklFac() throws EJBExceptionLP {
		try {
			if (angebotstklFac == null) {
				angebotstklFac = (AngebotstklFac) context
						.lookup("lpserver/AngebotstklFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotstklFac;
	}

	public AngebotstklpositionFac getAngebotstklpositionFac()
			throws EJBExceptionLP {
		try {
			if (angebotstklpositionFac == null) {
				angebotstklpositionFac = (AngebotstklpositionFac) context
						.lookup("lpserver/AngebotstklpositionFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotstklpositionFac;
	}

	public FibuExportFac getFibuExportFac() throws EJBExceptionLP {
		try {
			if (fibuExportFac == null) {
				fibuExportFac = (FibuExportFac) context.lookup("lpserver/FibuExportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fibuExportFac;
	}

	public DruckerFac getDruckerFac() throws EJBExceptionLP {
		try {
			if (druckerFac == null) {
				druckerFac = (DruckerFac) context.lookup("lpserver/DruckerFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return druckerFac;
	}

	public DokumenteFac getDokumenteFac() throws EJBExceptionLP {
		try {
			if (dokumenteFac == null) {
				dokumenteFac = (DokumenteFac) context.lookup("lpserver/DokumenteFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return dokumenteFac;
	}

	public KundesokoFac getKundesokoFac() throws EJBExceptionLP {
		try {
			if (kundesokoFac == null) {
				kundesokoFac = (KundesokoFac) context.lookup("lpserver/KundesokoFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kundesokoFac;
	}

	public BelegbuchungFac getBelegbuchungFac(String mandantCNr) throws EJBExceptionLP {
		boolean istVersteurer = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ISTVERSTEURER, mandantCNr);
		boolean mischVersteurer = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MISCHVERSTEURER, mandantCNr);
		if (!istVersteurer && !mischVersteurer) {
			try {
				if (belegbuchungFac == null) {
					belegbuchungFac = (BelegbuchungFac) context
							.lookup("lpserver/BelegbuchungFacBean/remote");
				}
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}
			return belegbuchungFac;
		} else if (mischVersteurer) {
			try {
				if (belegbuchungMischversteurerFac == null) {
					belegbuchungMischversteurerFac = (BelegbuchungFac) context
							.lookup("lpserver/BelegbuchungMischversteurerFacBean/remote");
				}
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}
			return belegbuchungMischversteurerFac;
		} else {
			try {
				if (belegbuchungIstversteurerFac == null) {
					belegbuchungIstversteurerFac = (BelegbuchungFac) context
							.lookup("lpserver/BelegbuchungIstversteurerFacBean/remote");
				}
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}
			return belegbuchungIstversteurerFac;
		}
	}

	/**
	 * SessionFacade fuer TheJudge holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final TheJudgeFac getTheJudgeFac() throws EJBExceptionLP {
		try {
			if (theJudgeFac == null) {
				theJudgeFac = (TheJudgeFac) context.lookup("lpserver/TheJudgeFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return theJudgeFac;
	}

	/**
	 * SessionFacade fuer Projekt holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ProjektFac
	 */

	public ProjektFac getProjektFac() throws EJBExceptionLP {
		try {
			if (projektFac == null) {
				projektFac = (ProjektFac) context.lookup("lpserver/ProjektFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return projektFac;
	}

	/**
	 * SessionFacade fuer ProjektService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ProjektFac
	 */

	public ProjektServiceFac getProjektServiceFac() throws EJBExceptionLP {
		try {
			if (projektServiceFac == null) {
				projektServiceFac = (ProjektServiceFac) context.lookup("lpserver/ProjektServiceFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return projektServiceFac;
	}

	/**
	 * SessionFacade fuer Zahlungsvorschlag holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ZahlungsvorschlagFac
	 */

	public ZahlungsvorschlagFac getZahlungsvorschlagFac() throws EJBExceptionLP {
		try {
			if (zahlungsvorschlagFac == null) {
				zahlungsvorschlagFac = (ZahlungsvorschlagFac) context
						.lookup("lpserver/ZahlungsvorschlagFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return zahlungsvorschlagFac;
	}

	public AutoFehlmengendruckFac getAutoFehlmengendruckFac()
			throws EJBExceptionLP {
		try {
			if (autoFehlmengendruckFac == null) {
				autoFehlmengendruckFac = (AutoFehlmengendruckFac) context
						.lookup("lpserver/AutoFehlmengendruckFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoFehlmengendruckFac;
	}

	public AutoMahnungsversandFac getAutoMahnungsversandFac()
			throws EJBExceptionLP {
		try {
			if (autoMahnungsversandFac == null) {
				autoMahnungsversandFac = (AutoMahnungsversandFac) context
						.lookup("lpserver/AutoMahnungsversandFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoMahnungsversandFac;
	}
	
	public AutoMahnenFac getAutoMahnenFac(){
		try{
			if(autoMahnenFac == null){
				autoMahnenFac = (AutoMahnenFac) context.lookup("lpserver/AutoMahnenFacBean/remote");
			}
		} catch(Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoMahnenFac;
	}
	public KuecheFac getKuecheFac(){
		try{
			if(kuecheFac == null){
				kuecheFac = (KuecheFac) context.lookup("lpserver/KuecheFacBean/remote");
			}
		} catch(Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kuecheFac;
	}

	public KuecheReportFac getKuecheReportFac(){
		try{
			if(kuecheReportFac == null){
				kuecheReportFac = (KuecheReportFac) context.lookup("lpserver/KuecheReportFacBean/remote");
			}
		} catch(Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kuecheReportFac;
	}

	public BestellungReportFac getBestellungReportFac() throws EJBExceptionLP {
		try {
			if (bestellungReportFac == null) {
				bestellungReportFac = (BestellungReportFac) context
						.lookup("lpserver/BestellungReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bestellungReportFac;
	}

	public AutoBestellvorschlagFac getAutoBestellvorschlagFac()
			throws EJBExceptionLP {
		try {
			if (autoBestellvorschlagFac == null) {
				autoBestellvorschlagFac = (AutoBestellvorschlagFac) context
						.lookup("lpserver/AutoBestellvorschlagFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoBestellvorschlagFac;
	}

	public AutoPaternosterFac getAutoPaternosterFac()
			throws EJBExceptionLP {
		try {
			if (autoPaternosterFac == null) {
				autoPaternosterFac = (AutoPaternosterFac) context
					.lookup("lpserver/AutoPaternosterFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));

		}
		return autoPaternosterFac;
}

	public BelegpositionkonvertierungFac getBelegpositionkonvertierungFac()
			throws EJBExceptionLP {
		try {
			if (belegpositionkonvertierungFac == null) {
				belegpositionkonvertierungFac = (BelegpositionkonvertierungFac) context
						.lookup("lpserver/BelegpositionkonvertierungFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return belegpositionkonvertierungFac;
	}

	public RahmenbedarfeFac getRahmenbedarfeFac() throws EJBExceptionLP {
		try {
			if (rahmenbedarfeFac == null) {
				rahmenbedarfeFac = (RahmenbedarfeFac) context
						.lookup("lpserver/RahmenbedarfeFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return rahmenbedarfeFac;
	}
	
	
	public AutomatikjobFac getAutomatikjobFac() throws EJBExceptionLP {
		try{
			if (automatikjobFac == null){
				automatikjobFac = (AutomatikjobFac) context.lookup("lpserver/AutomatikjobFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return automatikjobFac;
	}
	
	public AutomatikjobtypeFac getAutomatikjobtypeFac() throws EJBExceptionLP{
		try{
			if(automatikjobtypeFac ==null){
				automatikjobtypeFac = (AutomatikjobtypeFac) context.lookup("lpserver/AutomatikjobtypeFacBean/remote");
			}
		} catch(Throwable t){
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return automatikjobtypeFac;
	}

	public AutomatiktimerFac getAutomatiktimerFac() throws EJBExceptionLP{
		try{
			if(automatiktimerFac ==null){
				automatiktimerFac = (AutomatiktimerFac) context.lookup("lpserver/AutomatiktimerFacBean/remote");
			}
		} catch(Throwable t){
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return automatiktimerFac;
	}

	
	public AutoRahmendetailbedarfdruckFac getAutoRahmendetailbedarfdruckFac(){
		try{
			if(autoRahmendetailbedarfdruckFac==null){
				autoRahmendetailbedarfdruckFac = (AutoRahmendetailbedarfdruckFac) context.lookup("lpserver/AutoRahmendetailbedarfdruckFacBean/remote");
			}
		} catch(Throwable t){
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoRahmendetailbedarfdruckFac;
	}
	
	public LogonFac getLogonFac() throws EJBExceptionLP{
		try{
			if(logonFac==null){
				logonFac = (LogonFac) context.lookup("lpserver/LogonFacBean/remote");
			}
			
		} catch (Throwable t){
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return logonFac;
	}
	
	public JCRDocFac getJCRDocFac(){
		try{
			if(jcrDocFac==null){
				jcrDocFac = (JCRDocFac) context.lookup("lpserver/JCRDocFacBean/remote");
			}
		} catch (Throwable t){
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return jcrDocFac;
	}
	
	public AuftragRahmenAbrufFac getAuftragRahmenAbrufFac(){
		try{
			if(auftragRahmenAbrufFac==null){
				auftragRahmenAbrufFac = (AuftragRahmenAbrufFac) context.lookup("lpserver/AuftragRahmenAbrufFacBean/remote");
			}
		} catch (Throwable t){
			
		}
		return auftragRahmenAbrufFac;
	}
	

	/**
	 * Primary Key Generator holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public final PKGeneratorObj getPKGeneratorObj() {
		if (pkGeneratorObj == null) {
			pkGeneratorObj = new PKGeneratorObj();
		}
		return pkGeneratorObj;
	}

	/**
	 * Belegnummern Generator holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public final BelegnummerGeneratorObj getBelegnummerGeneratorObj() {
		if (bnGeneratorObj == null) {
			bnGeneratorObj = new BelegnummerGeneratorObj();
		}
		return bnGeneratorObj;
	}
	
	public BelegVerkaufFac getBelegVerkaufFac() throws EJBExceptionLP{
		try{
			if(belegVerkaufFac ==null){
				belegVerkaufFac = (BelegVerkaufFac) context.lookup("lpserver/BelegVerkaufFacBean/local");
			}
		} catch(Throwable t){
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return belegVerkaufFac;
	}
	
	public WebshopItemServiceFacLocal getWebshopItemServiceFac() throws EJBExceptionLP {
		try {
			if(null == webshopItemServicesFac) {
				webshopItemServicesFac = (WebshopItemServiceFacLocal) context.lookup("lpserver/WebshopItemServiceEjb/local") ;
			}
		} catch(Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));			
		}
		return webshopItemServicesFac ;
	}
	
	
	public WebshopOrderServiceFacLocal getWebshopOrderServiceFac() throws EJBExceptionLP {
		try {
			if(null == webshopOrderServicesFac) {
				webshopOrderServicesFac = (WebshopOrderServiceFacLocal) context.lookup("lpserver/WebshopOrderServiceEjb/local") ;				
			} 
		} catch(Throwable t) {			
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));			
		}

		return webshopOrderServicesFac ;
	}

	public WebshopOrderServiceFacLocal getWebshopCustomerOrderServiceFac() throws EJBExceptionLP {
		try {
			if(null == webshopCustomerOrderServicesFac) {
				webshopCustomerOrderServicesFac = (WebshopOrderServiceFacLocal) context.lookup("lpserver/WebshopCustomerOrderServiceEjb/local") ;				
			} 
		} catch(Throwable t) {			
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));			
		}

		return webshopCustomerOrderServicesFac ;
	}
	
	public BatcherFac getBatcherFac() throws EJBExceptionLP {
		try {
			if(null == batcherFac) {
				batcherFac = (BatcherFac) context.lookup("lpserver/BatcherBean/local") ;
			}
		} catch(Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));						
		}
		return batcherFac ;
	}

	public WebshopCustomerServiceFacLocal getWebshopCustomerServiceFac() throws EJBExceptionLP {
		try {
			if(null == webshopCustomerServicesFac) {
				webshopCustomerServicesFac = (WebshopCustomerServiceFacLocal)
						context.lookup("lpserver/WebshopCustomerServiceEjb/local") ;				
			} 
		} catch(Throwable t) {			
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));			
		}

		return webshopCustomerServicesFac ;
	}
	
	public JCRMediaFac getJCRMediaFac() throws EJBExceptionLP {
		try {
			if (jcrMediaFac == null) {
				jcrMediaFac = (JCRMediaFac) context.lookup("lpserver/JCRMediaFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return jcrMediaFac;		
	}

	public EmailMediaFac getEmailMediaFac() throws EJBExceptionLP {
		try {
			if (emailMediaFac == null) {
				emailMediaFac = (EmailMediaFac) context.lookup("lpserver/EmailMediaFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return emailMediaFac;		
	}
	
	public EmailMediaLocalFac getEmailMediaLocalFac()  throws EJBExceptionLP {
		try {
			if (emailMediaLocalFac == null) {
				emailMediaLocalFac = (EmailMediaLocalFac) context.lookup("lpserver/EmailMediaLocalFacBean/local");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		
		return emailMediaLocalFac;		
	}
	
	public BatcherSingleTransactionFac getBatcherSingleTransactionFac() throws EJBExceptionLP {
		try {
			if(null == batcherSingleTransactionFac) {
				batcherSingleTransactionFac = (BatcherSingleTransactionFac)
						context.lookup("lpserver/BatcherSingleTransactionBean/local") ;
			}
		} catch(Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));						
		}
		return batcherSingleTransactionFac ;
	}
	
	
	public PersonalApiFac getPersonalApiFac()  throws EJBExceptionLP {
		try {
			if (personalApiFac == null) {
				personalApiFac = (PersonalApiFac) context.lookup("lpserver/PersonalApiFacBean/remote");
			}
			
		} catch(Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));			
		}
		
		return personalApiFac ;
	}
}
