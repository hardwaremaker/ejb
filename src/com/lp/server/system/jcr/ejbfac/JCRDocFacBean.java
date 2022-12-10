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
package com.lp.server.system.jcr.ejbfac;

import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_AGPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_AGSTKLPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_AGSTUECKLISTE;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_ANFPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_ANFRAGE;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_ANGEBOT;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_ARTIKEL;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_AUFPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_AUFTRAG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_BENUTZER;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_BESTELLUNG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_BESTPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_EINGANGSRECHNG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_GUTSCHRIFT;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_GUTSPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_KASSENBUCH;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_KUNDE;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LAGERSTANDSLISTE;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LIEFERANT;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LIEFERSCHEIN;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LIEFERSPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LOS;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LOSABLIEFERUNG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PARTNER;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PERSONAL;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PROFORMAPOS;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PROFORMARECHNG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PROJEKT;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PROJHISTORY;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_RECHNUNG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_RECHPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_REKLAMATION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_SALDENLISTE;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_STKLPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_UVA;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_WARENEINGANG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_WEPOSITION;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jcr.AccessDeniedException;
import javax.jcr.Credentials;
import javax.jcr.ItemNotFoundException;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.Workspace;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.ejb.Anfrage;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.angebot.ejb.Angebot;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.ejb.Agstkl;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.fertigung.ejb.Los;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.GesamtkalkulationDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.PrintKontoblaetterModel;
import com.lp.server.finanz.service.ReportSaldenlisteKriterienDto;
import com.lp.server.finanz.service.ReportUvaKriterienDto;
import com.lp.server.inserat.ejb.Inserat;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerDtoSmall;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.ejb.Projekt;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.reklamation.ejb.Reklamation;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.fastlanereader.ejb.FastLaneReaderBean;
import com.lp.server.system.fastlanereader.service.FastLaneReader;
import com.lp.server.system.jcr.ejb.Dokumentbelegart;
import com.lp.server.system.jcr.ejb.DokumentbelegartPK;
import com.lp.server.system.jcr.ejb.Dokumentgruppierung;
import com.lp.server.system.jcr.ejb.DokumentgruppierungPK;
import com.lp.server.system.jcr.ejb.Dokumentnichtarchiviert;
import com.lp.server.system.jcr.service.DocumentResult;
import com.lp.server.system.jcr.service.DokumentbelegartDto;
import com.lp.server.system.jcr.service.DokumentgruppierungDto;
import com.lp.server.system.jcr.service.DokumentnichtarchiviertDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.JcrSession;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAnfrage;
import com.lp.server.system.jcr.service.docnode.DocNodeAngebot;
import com.lp.server.system.jcr.service.docnode.DocNodeArtikel;
import com.lp.server.system.jcr.service.docnode.DocNodeAuftrag;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeBestellung;
import com.lp.server.system.jcr.service.docnode.DocNodeEingangsrechnung;
import com.lp.server.system.jcr.service.docnode.DocNodeFactory;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeFolder;
import com.lp.server.system.jcr.service.docnode.DocNodeInserat;
import com.lp.server.system.jcr.service.docnode.DocNodeKunde;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferant;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferschein;
import com.lp.server.system.jcr.service.docnode.DocNodeLiteral;
import com.lp.server.system.jcr.service.docnode.DocNodeLos;
import com.lp.server.system.jcr.service.docnode.DocNodePartner;
import com.lp.server.system.jcr.service.docnode.DocNodeProjekt;
import com.lp.server.system.jcr.service.docnode.DocNodeRechnung;
import com.lp.server.system.jcr.service.docnode.DocNodeReklamation;
import com.lp.server.system.jcr.service.docnode.DocNodeSymbolicLink;
import com.lp.server.system.jcr.service.docnode.DocNodeUVAVerprobung;
import com.lp.server.system.jcr.service.docnode.DocNodeVersion;
import com.lp.server.system.jcr.service.docnode.DocNodeWEPosition;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.jcr.service.docnode.HeliumDocPath;
import com.lp.server.system.jcr.service.docnode.visualizer.VisualNodeFile;
import com.lp.server.system.jcr.service.docnode.visualizer.VisualNodeLiteral;
import com.lp.server.system.pkgenerator.bl.BelegnummerGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.BelegartdokumentDto;
import com.lp.server.system.service.DokumentDto;
import com.lp.server.system.service.DokumentschlagwortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.FacLookup;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JRException;

@Stateless
//@Service
//@Singleton
//@Interceptors(MethodLogger.class)
public class JCRDocFacBean extends Facade implements JCRDocFac {
	@PersistenceContext
	private EntityManager em;

	private static final String PATH_LAGERSTANDSLISTE = "HELIUMV/Artikel/Lagerstandsliste/";

	private static final String PATH_ANGEBOT = "HELIUMV/Angebot/Angebot/*/";
	private static final String PATH_AGPOSITION = "HELIUMV/Angebot/Angebot/*/Angebotpositionen/*/";
	private static final String PATH_AGSTUECKLISTE = "HELIUMV/AGStueckliste/AGStueckliste/*/";
	private static final String PATH_AGSTKLPOSITION = "HELIUMV/AGStueckliste/AGStueckliste/*/Angebotsstuecklistenpositionen/*/";

	private static final String PATH_ANFRAGE = "HELIUMV/Anfrage/Anfrage/*/";
	private static final String PATH_ANFPOSITION = "HELIUMV/Anfrage/Anfrage/*/Anfragepositionen/*/";

	private static final String PATH_ARTIKEL = "HELIUMV/Artikel/*/*/";
	private static final String PATH_STKLPOSITION = "HELIUMV/Artikel/*/*/Stuecklistenpositionen/*/";

	private static final String PATH_AUFTRAG = "HELIUMV/Auftrag/Auftrag/*/";
	private static final String PATH_AUFPOSITION = "HELIUMV/Auftrag/Auftrag/*/Auftragpositionen/*/";

	private static final String PATH_PARTNER = "HELIUMV/Partner/Partner/*/";
	private static final String PATH_PERSONAL = "HELIUMV/Personal/Personal/*/";
	private static final String PATH_BENUTZER = "HELIUMV/Benutzer/Benutzer/*/";
	private static final String PATH_KUNDE = "HELIUMV/Kunde/Kunde/*/";
	private static final String PATH_LIEFERANT = "HELIUMV/Lieferant/Lieferant/*/";

	private static final String PATH_BESTELLUNG = "HELIUMV/Bestellung/*/*/";
	private static final String PATH_BESTPOSITION = "HELIUMV/Bestellung/*/*/Bestellpositionen/*/";
	private static final String PATH_WARENEINGANG = "HELIUMV/Bestellung/*/*/Wareneing\u00E4nge/*/";
	private static final String PATH_WEPOSITION = "HELIUMV/Bestellung/*/*/Wareneing\u00E4nge/*/Wareneinganspositionen/*/#";

	private static final String PATH_EINGANGSRECHNG = "HELIUMV/Eingangsrechng/Eingangsrechng/*/";

	private static final String PATH_UVA = "HELIUMV/Finanzbuchhaltg/*/UVA/#";
	private static final String PATH_SALDENLISTE = "HELIUMV/Finanzbuchhaltg/*/Saldenliste/#";
	private static final String PATH_KASSENBUCH = "HELIUMV/Finanzbuchhaltg/*/Kassenbuch/#";

	private static final String PATH_LOS = "HELIUMV/Los/Los/*/";
	private static final String PATH_LOSABLIEFERUNG = "HELIUMV/Los/Los/*/Ablieferungen/*/";

	private static final String PATH_LIEFERSCHEIN = "HELIUMV/Lieferschein/Lieferschein/*/";
	private static final String PATH_LIEFERSPOSITION = "HELIUMV/Lieferschein/Lieferschein/*/Lieferscheinpositionen/*/";

	private static final String PATH_PROJEKT = "HELIUMV/Projekt/*/*/";
	private static final String PATH_PROJHISTORY = "HELIUMV/Projekt/*/*/*/";

	private static final String PATH_RECHNUNG = "HELIUMV/Rechnung/Rechnung/*/";

	private static final String PATH_RECHPOSITION = "HELIUMV/Rechnung/Rechnung/*/Rechnungpositionen/*/";

	private static final String PATH_REKLAMATION = "HELIUMV/Reklamation/Reklamation/*/";

	private static final String IGNORE_FLAG = "ignoreMe";

	private static final String PATH_NOT_IMPLEMENTED = "NOPATHIMPL";
	private static final long timeout = 60000;

	private static final int DOC_LIMIT = 100;

	private static Map<String, String> pathMap;

	private String UUIDFuerUseCase = "JCR";

//	private InitialContext ctx;
	private Session session;
	private Repository repo;
	private Credentials cred;
	private FastLaneReader fastLaneReader = null;

	private DocumentResult result;
	private int jcrsCheckedCount = 0;
	private int jcrsDeletedCount = 0;

	private List<String> clearedPaths;
	private List<String> mandanten;
	private String belegartToRepair;

	private boolean overwriteExisting = false;
	private boolean skipExisting = false;
	private boolean clearFolders = false;
	private boolean deleteFiles = false;
	private boolean readOnly = false;

	private int startIndex = 0;

	private TheClientDto theClientDto;

	private Map<Integer, PersonalDto> cachedPersonalDto = new HashMap<Integer, PersonalDto>();

//	private static final ReentrantLock versandauftragLock = new ReentrantLock();
	private static final ReentrantLock jcrLock = new ReentrantLock();

/*	
	private class JcrSession {
		private Session session;
		private Repository repository;
		private boolean isWildfly;

		public JcrSession() {
		}

		public Session getSession() throws NamingException, RepositoryException {
			if (session == null) {
				myLogger.info("jcrsession::login to repository necessary");

				if (!isOnline()) {
					myLogger.info("repository not available");
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE, "repo == null");
				}

				Credentials creds = isWildfly ? new SimpleCredentials("jcr", "jcr".toCharArray())
						: new SimpleCredentials("Anonymous", "".toCharArray());

				session = repository.login(creds);
				myLogger.info("jcrsession::login to repository done.");
			}

			return session;
		}

		public void closeSession() {
			if (session != null) {
				myLogger.info("jcrSession::logout from repository");
				try {
					if (session.isLive()) {
						session.logout();
						session = null;
					} else {
						myLogger.warn("jcrsession::session was not live?");
					}
				} catch (IllegalStateException e) {
					myLogger.error("jcrSession::closeSession:IllegalState", e);
				}
			} else {
				myLogger.info("jcrSession::no logout necessary?");
			}

			session = null;
			repository = null;
		}

		public void forcedCloseSession() {
			if (null != session) {
				myLogger.info("jcrSession::forced logout from repository");
			}
			session = null;
			repo = null;
		}

		public boolean isOnline() throws NamingException {
			createRepo();
			return repository != null;
		}

		public Node getRootNode() throws NamingException, RepositoryException {
			return getSession().getRootNode();
		}

		public Node getNode(String path) {
			try {
				Node rootNode = null;
				try {
					rootNode = getSession().getRootNode();
				} catch (IllegalStateException e) {
					// session timed out?
					// session = repo.login(cred);
					myLogger.warn("jcr:IllegalState:getNode", e);
					forcedCloseSession();
					rootNode = getSession().getRootNode();
				} catch (RepositoryException e) {
					myLogger.warn("jcr:Repository:getNode", e);
					forcedCloseSession();
					rootNode = getSession().getRootNode();
				}

				Node returnNode = null;
				try {
					returnNode = rootNode.getNode(path);
				} catch (PathNotFoundException ex) {
					// Den Knoten gibt es nicht Null zurueckgeben
				}

				return returnNode;
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
			}
		}

		public Node getNode(DocPath docPath) {
			return getNode(docPath.getPathAsString());
		}

		public void save() throws NamingException, RepositoryException {
			getSession().save();
		}

		private void createRepo() throws NamingException {
			if (repository != null) {
				return;
			}

			InitialContext ctx = new InitialContext();

			try {
				// Wildfly
				repository = (Repository) ctx.lookup("java:/jcr/local");
				isWildfly = true;
				myLogger.info("jcrSession::created Wildfly JCR-Repo");
				return;
			} catch (NameNotFoundException ex) {
				try {
					// JBoss
					repository = (Repository) ctx.lookup("java:jcr/local");
					isWildfly = false;
					myLogger.info("jcrSession::created JBoss JCR-Repo");
					return;
				} catch (NameNotFoundException ex2) {
					// OFFLINE FUER JBOSS
					myLogger.error("Couldn't find jcr-name (/jcr/local)", ex2);
				} catch (java.lang.ClassCastException ex2) {
					myLogger.error("Naming Wildfly", ex2);
				}
			}

			myLogger.error("jcrSession::Couldn't create JCR-Repo!");
		}
	}
*/
	public JCRDocFacBean() throws NamingException, LoginException, RepositoryException {
		super();

		myLogger.warn("Creating new JCRDocFacBean");

		if (pathMap == null) {
			Map<String, String> localpathMap = new HashMap<String, String>();
			localpathMap.put(BELEGART_LAGERSTANDSLISTE, PATH_LAGERSTANDSLISTE);
			localpathMap.put(BELEGART_ANGEBOT, PATH_ANGEBOT);
			localpathMap.put(BELEGART_AGPOSITION, PATH_AGPOSITION);
			localpathMap.put(BELEGART_AGSTUECKLISTE, PATH_AGSTUECKLISTE);
			localpathMap.put(BELEGART_AGSTKLPOSITION, PATH_AGSTKLPOSITION);

			localpathMap.put(BELEGART_ANFRAGE, PATH_ANFRAGE);
			localpathMap.put(BELEGART_ANFPOSITION, PATH_ANFPOSITION);

			localpathMap.put(BELEGART_ARTIKEL, PATH_ARTIKEL);
			localpathMap.put(BELEGART_STKLPOSITION, PATH_STKLPOSITION);

			localpathMap.put(BELEGART_AUFTRAG, PATH_AUFTRAG);
			localpathMap.put(BELEGART_AUFPOSITION, PATH_AUFPOSITION);

			localpathMap.put(BELEGART_PARTNER, PATH_PARTNER);
			localpathMap.put(BELEGART_PERSONAL, PATH_PERSONAL);
			localpathMap.put(BELEGART_BENUTZER, PATH_BENUTZER);
			localpathMap.put(BELEGART_KUNDE, PATH_KUNDE);
			localpathMap.put(BELEGART_LIEFERANT, PATH_LIEFERANT);

			localpathMap.put(BELEGART_BESTELLUNG, PATH_BESTELLUNG);
			localpathMap.put(BELEGART_BESTPOSITION, PATH_BESTPOSITION);
			localpathMap.put(BELEGART_WARENEINGANG, PATH_WARENEINGANG);
			localpathMap.put(BELEGART_WEPOSITION, PATH_WEPOSITION);

			localpathMap.put(BELEGART_EINGANGSRECHNG, PATH_EINGANGSRECHNG);

			localpathMap.put(BELEGART_UVA, PATH_UVA);
			localpathMap.put(BELEGART_SALDENLISTE, PATH_SALDENLISTE);
			localpathMap.put(BELEGART_KASSENBUCH, PATH_KASSENBUCH);

			localpathMap.put(BELEGART_LOS, PATH_LOS);
			localpathMap.put(BELEGART_LOSABLIEFERUNG, PATH_LOSABLIEFERUNG);

			localpathMap.put(BELEGART_LIEFERSCHEIN, PATH_LIEFERSCHEIN);
			localpathMap.put(BELEGART_LIEFERSPOSITION, PATH_LIEFERSPOSITION);

			localpathMap.put(BELEGART_PROJEKT, PATH_PROJEKT);
			localpathMap.put(BELEGART_PROJHISTORY, PATH_PROJHISTORY);

			localpathMap.put(BELEGART_RECHNUNG, PATH_RECHNUNG);
			localpathMap.put(BELEGART_GUTSCHRIFT, PATH_RECHNUNG);
			localpathMap.put(BELEGART_PROFORMARECHNG, PATH_RECHNUNG);

			localpathMap.put(BELEGART_RECHPOSITION, PATH_RECHPOSITION);
			localpathMap.put(BELEGART_GUTSPOSITION, PATH_RECHPOSITION);
			localpathMap.put(BELEGART_PROFORMAPOS, PATH_RECHPOSITION);

			localpathMap.put(BELEGART_REKLAMATION, PATH_REKLAMATION);

			// localpathMap.put(BELEGART_VERSANDAUFTRAG, PATH_VERSANDAUFTRAG);
			// localpathMap.put(BELEGART_VERSANDANHANG, PATH_VERSANDANHANG);

			pathMap = localpathMap;
		}
		/*
		 * Wir haben eine StatelessBean. Deshalb sollte es eigentlich gar keine
		 * klassenweiten privaten Variablen geben, denn diese sind mit hoher
		 * Wahrscheinlichkeit nicht fuer den multithreading context geeignet.
		 * 
		 * Der Container-Manager sorgt zwar dafuer, dass die gleiche Bean zur gleichen
		 * Zeit nicht mehrfach verwendet wird, aber eventuell gesetzte Variablen (und
		 * damit auch die dahinterliegenden Resourcen wie z.B. eine JCR Session(!))
		 * bleiben erhalten.
		 * 
		 * Achtung: Eine JCRSession ist per Definition nicht multithreading-fest (so
		 * dokumentiert bei Jackrabbit
		 * (https://jackrabbit.apache.org/jcr/first-hops.html) unter Hop 1: Logging in
		 * to JackRabbit
		 * 
		 * Hinweis: Es gibt zwar ein "repository.login()", aber keinen entsprechenden
		 * logout(). Das wird durch die vom repository.login() erhaltene session und
		 * session.logout() erreicht.
		 *
		 * myLogger.warn("new if context"); if (ctx == null) { ctx = new
		 * InitialContext(); }
		 * 
		 * myLogger.warn("new if repo"); if (repo == null) { try { repo = (Repository)
		 * ctx.lookup("java:jcr/local");
		 * 
		 * } catch (NameNotFoundException ex) { try { repo = (Repository)
		 * ctx.lookup("java:/jcr/local"); cred = new SimpleCredentials("jcr",
		 * "jcr".toCharArray()); } catch (NameNotFoundException ex2) { // OFFLINE FUER
		 * JBOSS System.out.println("NAME_NOT_FOUND_EXCEPTION"); } catch
		 * (java.lang.ClassCastException ex2) { // WORKAROUND FUER WILDFLY
		 * ex2.printStackTrace(); }
		 * 
		 * System.out.println("FUNKTIONIERT");
		 * 
		 * }
		 * 
		 * } if (cred == null) { cred = new SimpleCredentials("Anonymous",
		 * "".toCharArray()); } // if (session == null) { // session = repo.login(cred);
		 * // }
		 * 
		 * 
		 */
	}

	private void assertClosed(String message) {
		if (repo != null) {
			myLogger.warn(message + ":repo is already set?");
		}
		if (session != null) {
			myLogger.warn(message + ":session is already open?");
		}
	}

	private Repository createRepo() throws NamingException {
		if (repo != null) {
			return repo;
		}

		InitialContext ctx = new InitialContext();

		try {
			// JBoss
			repo = (Repository) ctx.lookup("java:jcr/local");
			cred = new SimpleCredentials("Anonymous", "".toCharArray());
			myLogger.info("created JBoss JCR-Repo");
			return repo;
		} catch (NameNotFoundException ex) {
			// Wildfly
			try {
				repo = (Repository) ctx.lookup("java:/jcr/local");
				cred = new SimpleCredentials("jcr", "jcr".toCharArray());
				myLogger.info("created Wildfly JCR-Repo");
				return repo;
			} catch (NameNotFoundException ex2) {
				// OFFLINE FUER JBOSS
				myLogger.error("Couldn't find jcr-name (/jcr/local)", ex2);
			} catch (java.lang.ClassCastException ex2) {
				myLogger.error("Naming Wildfly", ex2);
			}
		}

		myLogger.error("Couldn't create JCR-Repo!");
		return null;
	}

	private void closeRepo() {
		repo = null;
		myLogger.info("closing repo");
	}


	// Just for testing in beginning of development
	public void printNodeStructure(Node nStartnode, int rek, boolean bPrintSystemNode) {
		try {
			// checkAndDoLogin();
			boolean bRoot = false;
			String sToPrint = "" + rek + " ";
			for (int i = 0; i < rek; i++) {
				sToPrint = sToPrint + "   ";
			}
			Node node;
			if (nStartnode == null) {
				node = getSession().getRootNode();
				bRoot = true;
				String sPath = node.getPath();
				System.out.println(sPath);
			} else {
				node = nStartnode;
			}
			if (bRoot) {
				sToPrint = sToPrint + "ROOT ";
			} else {
				sToPrint = sToPrint + node.getName();

			}
			// System.out.println(sToPrint);
			try {
				VersionHistory hist = node.getVersionHistory();
				VersionIterator vIt = hist.getAllVersions();
				int i = 0;
				while (vIt.hasNext()) {
					Version v = vIt.nextVersion();
					NodeIterator niVersion = v.getNodes("jcr:frozenNode");
					if (niVersion.hasNext()) {
						Node no = niVersion.nextNode();
						String sSpace = "  ";
						for (int y = 0; y < rek; y++) {
							sSpace = sSpace + "   ";
						}
						System.out.println(rek + sSpace + "Version " + i + " of: " + no.getName() + " Created on: "
								+ v.getCreated().getTime().toString());
					}
					i++;
				}
			} catch (UnsupportedRepositoryOperationException ex) {
				// Node ist nicht Versionierbar
			}

			NodeIterator it = node.getNodes();
			while (it.hasNext()) {
				Node nextNode = it.nextNode();
				if (bPrintSystemNode) {
					printNodeStructure(nextNode, rek + 1, false);
				} else {
					if (nextNode.getName().equals("jcr:system")) {
						// do nothing
					} else {
						printNodeStructure(nextNode, rek + 1, false);
					}
				}
			}
			// getSession().logout()
		} catch (Exception e) {
			myLogger.error("uncatched printNodeStructure", e);
			// e.printStackTrace();
		} finally {
			// closeSession();
		}
	}

	public void removeNode(String path) throws ItemNotFoundException, RepositoryException {
		removeNode(getNode(path));
	}

	private void removeNode(Node node) throws ItemNotFoundException, RepositoryException {
		if (node == null)
			throw new ItemNotFoundException("Der Node existiert nicht.");
		Node parent = node.getParent();
		node.remove();
		parent.save();
	}

	public void fuehreDokumenteZusammen(PartnerDto zielDto, PartnerDto quellDto) {
		DocPath zielPath = new DocPath(new DocNodePartner(zielDto));
		DocPath quellPath = new DocPath(new DocNodePartner(quellDto));
		fuehreDokumenteZusammen(zielPath, quellPath, zielDto.getIId(), quellDto.getIId());
	}

	public void fuehreDokumenteZusammen(LieferantDto zielDto, LieferantDto quellDto) {
		DocPath zielPath = new DocPath(
				new DocNodeLieferant(zielDto, getPartnerFac().partnerFindByPrimaryKey(zielDto.getPartnerIId(), null)));
		DocPath quellPath = new DocPath(new DocNodeLieferant(quellDto,
				getPartnerFac().partnerFindByPrimaryKey(quellDto.getPartnerIId(), null)));
		fuehreDokumenteZusammen(zielPath, quellPath, zielDto.getPartnerIId(), quellDto.getPartnerIId());
	}

	public void fuehreDokumenteZusammen(KundeDto zielDto, KundeDto quellDto) {
		DocPath zielPath = new DocPath(
				new DocNodeKunde(zielDto, getPartnerFac().partnerFindByPrimaryKey(zielDto.getPartnerIId(), null)));
		DocPath quellPath = new DocPath(
				new DocNodeKunde(quellDto, getPartnerFac().partnerFindByPrimaryKey(quellDto.getPartnerIId(), null)));
		fuehreDokumenteZusammen(zielPath, quellPath, zielDto.getPartnerIId(), quellDto.getPartnerIId());
	}

	public void verschiebeBzwKopiereDokumentInAnderenDocPath(DocPath quellePath, DocPath zielPath) {
		verschiebeBzwKopiereDokumentInAnderenDocPath(quellePath, zielPath, true);
	}

	public void verschiebeBzwKopiereDokumentInAnderenDocPath(DocPath quellePath, DocPath zielPath,
			boolean withSubNodes) {
		Node node = getNode(quellePath);
		if (node == null)
			return;
		List<JCRDocDto> jcrs = getAllJCRDocs(node, withSubNodes);
		boolean failed = false;
		for (JCRDocDto jcr : jcrs) {
			ArrayList<DocNodeVersion> versions = getAllVersions(jcr);
			Collections.reverse(versions);
			DocPath newPath = null;
			newPath = zielPath.getDeepCopy().add(new DocNodeFile(jcr.getsName()));
			int i = 0;
			while (getNode(newPath) != null) {
				newPath = zielPath.getDeepCopy().add(new DocNodeFile(jcr.getsName() + "(" + i++ + ")"));
			}
			JCRDocDto latestVer = null;
			int verNr = 0;
			for (DocNodeVersion ver : versions) {
				JCRDocDto jcrVer = getData(ver.getJCRDocDto());
				jcrVer.setDocPath(newPath);
				if (jcrVer.getlVersion() == -1) {
					jcrVer.setlVersion(verNr++);
				} else {
					verNr = (int) jcrVer.getlVersion();
				}

				addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrVer, null);
				latestVer = jcrVer;
			}
			if (getAllVersions(latestVer).size() == versions.size()) {
				try {
					removeNode(jcr.getsPath());
				} catch (ItemNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				failed = true;
			}
		}
		if (!failed) {
			try {
				removeNode(node);
			} catch (ItemNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void fuehreDokumenteZusammen(DocPath zielPath, DocPath quellPath, int zielPartnerIId, int quellPartnerIId) {
		Node node = getNode(quellPath);
		if (node == null)
			return;
		List<JCRDocDto> jcrs = getAllJCRDocs(node, true);
		boolean failed = false;
		for (JCRDocDto jcr : jcrs) {
			ArrayList<DocNodeVersion> versions = getAllVersions(jcr);
			Collections.reverse(versions);
			DocPath newPath = null;
			newPath = zielPath.getDeepCopy().add(new DocNodeFile(jcr.getsName()));
			int i = 0;
			while (getNode(newPath) != null) {
				newPath = zielPath.getDeepCopy().add(new DocNodeFile(jcr.getsName() + "(" + i++ + ")"));
			}
			JCRDocDto latestVer = null;
			int verNr = 0;
			for (DocNodeVersion ver : versions) {
				JCRDocDto jcrVer = getData(ver.getJCRDocDto());
				jcrVer.setDocPath(newPath);
				if (jcrVer.getlVersion() == -1) {
					jcrVer.setlVersion(verNr++);
				} else {
					verNr = (int) jcrVer.getlVersion();
				}
				if (jcrVer.getlPartner() == quellPartnerIId) {
					jcrVer.setlPartner(zielPartnerIId);
				}
				addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrVer, null);
				latestVer = jcrVer;
			}
			if (getAllVersions(latestVer).size() == versions.size()) {
				try {
					removeNode(jcr.getsPath());
				} catch (ItemNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				failed = true;
			}
		}
		if (!failed) {
			try {
				removeNode(node);
			} catch (ItemNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	private void printNodeTree(Node n, int depth) {
		try {
			for (int i = 0; i < depth; i++) {
				System.out.print(" ");
			}
			if (n == null) {
				System.out.println("-null");
				return;
			}
			System.out.println("+" + n.getName() + " (" + n.getUUID() + ")");
			NodeIterator iter = n.getNodes();
			while (iter.hasNext()) {
				printNodeTree(iter.nextNode(), depth + 1);
			}
			try {
				VersionIterator vIter = n.getVersionHistory().getAllVersions();
				while (iter.hasNext()) {
					printNodeTree(vIter.nextVersion(), depth + 1);
				}
			} catch (Exception e) {

			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	// Wird nicht benutzt
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void setVisibilityOfDocument(String basePath, String versionPath, boolean hidden) {
		try {
			Node base = getNode(basePath);
			Node version = getNode(versionPath);

			new JCRDocDto(version, false); // pruefen ob es auch wirklich ein dokument ist

			if (base.isLocked())
				base.unlock();
			boolean versionable = false;
			for (NodeType mixin : base.getMixinNodeTypes()) {
				if (mixin.getName().equals("mix:versionable"))
					versionable = true;
			}

			if (!versionable)
				base.addMixin("mix:versionable");
			if (!base.isCheckedOut())
				base.checkout();

			PropertyIterator iter = version.getProperties();
			while (iter.hasNext()) {
				Property prop = iter.nextProperty();
				if (prop.getName().startsWith("jcr:"))
					continue;
				try {
					base.setProperty(prop.getName(), prop.getValue());
				} catch (Exception e) {
					// Schreibgeschuetzte Eigenschaft, einfach auslassen.
				}
			}
			base.setProperty(PROPERTY_VERSTECKT, hidden);
			getSession().save();
			base.checkin();
			getSession().save();
			// jetzt gibt es eine neue Version

			closeSession();
			// muss neue session sein, sonst tritt ein Fehler auf
			base = getNode(basePath);
			version = getNode(versionPath);
			String verName = version.getParent().getName();
			base.getVersionHistory().removeVersion(verName);
			getSession().save();

		} catch (Throwable e) {
			e.printStackTrace(System.out);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, "");
		}
	}

	private void lockJcr() {
		lockJcr("");
	}
	
	private void lockJcr(String message) {
		myLogger.info(message + "lockingJcr: " + jcrLock.getQueueLength() + ".");		
//		jcrLock.lock();
		myLogger.info(message + "lockedJcr: " + jcrLock.getQueueLength() + ".");		
	}
	
	private void unlockJcr() {
		unlockJcr("");			
	}
	
	private void unlockJcr(String message) {
		myLogger.info(message + "unlockingJcr: " + jcrLock.getQueueLength() + ".");		
//		jcrLock.unlock();			
		myLogger.info(message + "unlockedJcr: " + jcrLock.getQueueLength() + ".");		
	}
	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void addNewDocumentOrNewVersionOfDocument(JCRDocDto jcrDocDto, TheClientDto theClientDto) {
		assertClosed("addNewDocumentOrNewVersionOfDocument");
		try {
			lockJcr();
			addNewDocumentOrNewVersionOfDocumentImpl(null, jcrDocDto, theClientDto);			
		} finally {
			unlockJcr();			
		}
	}

	public void addNewDocumentOrNewVersionOfDocumentWithinTransaction(JCRDocDto jcrDocDto,
			TheClientDto theClientDto) {
		try {
			lockJcr();
			addNewDocumentOrNewVersionOfDocumentImpl(null, jcrDocDto, theClientDto);
		} finally {
			unlockJcr();
		}
	}

	public void addNewDocumentOrNewVersionOfDocumentWithinTransaction(List<JCRDocDto> dtos,
			TheClientDto theClientDto) {
		JcrSession jcrSession = new JcrSession();
		try {
			lockJcr();
			for (JCRDocDto jcrDocDto : dtos) {
				addNewDocumentOrNewVersionOfDocumentSession(jcrSession, jcrDocDto, theClientDto);
			}
		} finally {
			jcrSession.closeSession();
			unlockJcr();
		}
	}

	public void addNewDocumentOrNewVersionOfDocumentSession(JcrSession jcrSession, JCRDocDto jcrDocDto,
			TheClientDto theClientDto) {
		try {
			lockJcr();
			addNewDocumentOrNewVersionOfDocumentImpl(jcrSession, jcrDocDto, theClientDto);			
		} finally {
			unlockJcr();			
		}
	}

	private void addNewDocumentOrNewVersionOfDocumentImpl(JcrSession jcrSession, JCRDocDto jcrDocDto,
			TheClientDto theClientDto) {
		myLogger.warn("addNewDocumentOrNewVersionOfDocumentImpl beginn, have jcrSession=" + (jcrSession != null));
		JcrSession mySession = jcrSession;
		if (mySession == null) {
			mySession = new JcrSession();
		}

		try {
			if (!mySession.isOnline()) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE, "repo == null");
			}
		} catch (NamingException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE, e);
		}

		try {
			// Version wird immer hier gesetzt damit doppelte vermieden werden
			// long lVersion = getNextVersionNumer(jcrDocDto);
			DocPath docPath = jcrDocDto.getDocPath();
			if (jcrDocDto.getlVersion() == -1)
				jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
			// checkAndDoLogin();
			// No login we already logged in for version check
			Node rootNode = null;
			try {
				rootNode = mySession.getRootNode();
			} catch (IllegalStateException e) {
				// session timed out?
				// session = repo.login(cred);
				myLogger.warn("jcr:IllegalState", e);
				mySession.forcedCloseSession();
				rootNode = mySession.getRootNode();
			} catch (RepositoryException e) {
				myLogger.warn("jcr:RepositoryException", e);
				mySession.forcedCloseSession();
				rootNode = mySession.getRootNode();
			}

			if (theClientDto != null) {
				persistToFilesystem(docPath, jcrDocDto, theClientDto);
			}

			Node nUpdateNode = null;
			try {
				nUpdateNode = rootNode.getNode(docPath.getPathAsString());
				// Neue Version des Knotens
				if (nUpdateNode.getMixinNodeTypes().length == 0)
					nUpdateNode.addMixin("mix:versionable");

				nUpdateNode.checkout();
				nUpdateNode = setProperties(nUpdateNode, jcrDocDto);
// 15.10.2019 ghp
//				nUpdateNode.getParent().save();
				nUpdateNode.save();
				nUpdateNode.checkin();

				if (jcrSession == null) {
					mySession.closeSession();
				}
				// getSession().logout();
			} catch (PathNotFoundException ex) {
				// Der Knoten existiert nicht und muss angelegt werden
				// String[] sFolders = jcrDocDto.getsFullNodePath().split("/");
				List<DocNodeBase> folders = docPath.asDocNodeList();
				String sPath = "";
				for (int i = 0; i < folders.size(); i++) {
					sPath += (i == 0 ? "" : DocPath.SEPERATOR) + folders.get(i).asEncodedPath();

					try {
						nUpdateNode = rootNode.getNode(sPath);
						try {
							nUpdateNode.getProperty(DocNodeBase.NODEPROPERTY_NODETYPE);
						} catch (PathNotFoundException pnfEx) {
							folders.get(i).persistTo(nUpdateNode);
							nUpdateNode.getParent().save();
						}
					} catch (Exception pnfEx) {
						myLogger.info("Creating '" + sPath + "'...");
						// Anlegen
						nUpdateNode = rootNode.addNode(sPath);
						nUpdateNode.getSession().save();
//						jcrSession.save();
						folders.get(i).persistTo(nUpdateNode);
						nUpdateNode.getSession().save();
// 15.10.2019 ghp						
//						nUpdateNode.getParent().save();

						// try {
						// nUpdateNode.checkin();
						// } catch (Exception ex1) {
						// // Nichts tun, ist keine versionabel Node
						// }
						// rootNode.getNode(sPath).checkin();
					}
				}

				myLogger.info("Having created new Path '" + docPath.getPathAsString() + "'.");

				// Jetzt gibt es den Knoten
				nUpdateNode = rootNode.getNode(docPath.getPathAsString());
				nUpdateNode.addMixin("mix:versionable");
// 15.10.2019 ghp				
// 				nUpdateNode = setProperties(nUpdateNode, jcrDocDto);

// 15.10.2019 ghp
//				nUpdateNode.save();

				// Auch gleich die erste Version anlegen
// 15.10.2019 ghp ev. nicht notwendig, weil "already checked-out"				
//				nUpdateNode.checkout();
				nUpdateNode = setProperties(nUpdateNode, jcrDocDto);
				nUpdateNode.getSession().save();
				nUpdateNode.checkin();

// 15.10.2019 ghp
//				mySession.save();

				if (jcrSession == null) {
					mySession.closeSession();
				}
			}
		} catch (Exception e) {
			myLogger.error("addNewDocumentOrNewVersionOfDocumentImpl:", e);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_JCR_KNOTEN_NICHT_GESPEICHERT, e);
		} finally {
			myLogger.info("finally forcedCloseSession");
			if (jcrSession == null) {
				mySession.forcedCloseSession();
			}
		}

		myLogger.warn("addNewDocumentOrNewVersionOfDocumentImpl ende");
	}


	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void updateVersionOfDocument(JCRDocDto jcrDocDto, String jcrVersion, TheClientDto theClientDto) {
		updateVersionOfDocumentImpl(jcrDocDto, jcrVersion, theClientDto);
	}

	public void updateVersionOfDocumentWithinTransaction(JCRDocDto jcrDocDto, String jcrVersion,
			TheClientDto theClientDto) {
		updateVersionOfDocumentImpl(jcrDocDto, jcrVersion, theClientDto);
	}

	private void persistToFilesystem(DocPath docPath, JCRDocDto jcrDocDto, TheClientDto theClientDto)
			throws RemoteException, JRException, IOException {
		ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ZUSAETZLICHER_DOKUMENTENSPEICHERPFAD);
		if (parametermandantDto.getCWert() != null && parametermandantDto.getCWert().trim().length() > 0) {

			String pfadFuerDateisystem = parametermandantDto.getCWert() + docPath.getPathAsString();
			byte[] bData = jcrDocDto.getbData();
			if (pfadFuerDateisystem.endsWith(".jasper") && jcrDocDto.getJasperPrint() != null) {
				// Zuerst PDF daraus machen
				pfadFuerDateisystem = pfadFuerDateisystem.replace(".jasper", ".pdf");
				bData = exportToPDF(jcrDocDto.getJasperPrint());
			} else {
				pfadFuerDateisystem += jcrDocDto.getsMIME();
			}

			if (bData != null) {
				int stelleTrennungVerzeichnisDateiname = pfadFuerDateisystem.lastIndexOf("/");

				String verzeichnis = pfadFuerDateisystem.substring(0, stelleTrennungVerzeichnisDateiname);
				java.io.File f = new File(verzeichnis);
				if (!f.exists()) {
					f.mkdirs();
				}
				f = new File(pfadFuerDateisystem);

				if (!f.exists()) {
					f.createNewFile();
				}
				FileOutputStream fo = new FileOutputStream(pfadFuerDateisystem);
				fo.write(bData);
				fo.flush();
				fo.close();
			}
		}
	}

	private void updateVersionOfDocumentImpl(JCRDocDto jcrDocDto, String jcrVersion, TheClientDto theClientDto) {
		if (!isOnline()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE, "repo == null");
		}

		try {
			// Version wird immer hier gesetzt damit doppelte vermieden werden
			// long lVersion = getNextVersionNumer(jcrDocDto);
			DocPath docPath = jcrDocDto.getDocPath();
			if (jcrDocDto.getlVersion() == -1)
				jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
			// checkAndDoLogin();
			// No login we already logged in for version check
			Node rootNode = null;
			try {
				rootNode = getSession().getRootNode();

			} catch (IllegalStateException e) {
				// session timed out?
				// session = repo.login(cred);
				myLogger.warn("jcr:IllegalState:updateVersionOfDocument()", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			} catch (RepositoryException e) {
				myLogger.warn("jcr:Repository:updateVersionOfDocument()", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			}

			if (theClientDto != null) {
				persistToFilesystem(docPath, jcrDocDto, theClientDto);
			}

			Node nUpdateNode = rootNode.getNode(docPath.getPathAsString());
			// Neue Version des Knotens
			if (nUpdateNode.getMixinNodeTypes().length == 0) {
				nUpdateNode.addMixin("mix:versionable");
			}
			VersionHistory history = nUpdateNode.getVersionHistory();
			VersionIterator i = history.getAllVersions();
			String lastVersion = "";
			String rootedJcrVersion = "/" + jcrVersion;
			while (i.hasNext()) {
				Version v = i.nextVersion();
				if (rootedJcrVersion.startsWith(v.getPath())) {
					lastVersion = v.getName();
					break;
				}
			}

			Long lastAnlegeZeit = nUpdateNode.getProperty(JCRDocFac.PROPERTY_ZEITPUNKT).getLong();
			nUpdateNode.checkout();
			nUpdateNode = setProperties(nUpdateNode, jcrDocDto);
			nUpdateNode.setProperty(JCRDocFac.PROPERTY_ZEITPUNKT, lastAnlegeZeit);
			nUpdateNode.getParent().save();
			nUpdateNode.save();
			getSession().save();
			nUpdateNode.checkin();
			history.removeVersion(lastVersion);

			closeSession();
		} catch (Exception e) {
			String docPathString = jcrDocDto.getDocPath() != null ? jcrDocDto.getDocPath().getPathAsString() : null;
			myLogger.error("Fehler bei Document Update (DocPath = " + docPathString + ")", e);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_JCR_KNOTEN_NICHT_GESPEICHERT, e);
		} finally {
		}
	}

	private Node getNode(DocPath docPath) {
		try {
			// checkAndDoLogin();
			Node rootNode = null;
			try {
				rootNode = getSession().getRootNode();
			} catch (IllegalStateException e) {
				// session timed out?
				// session = repo.login(cred);
				myLogger.warn("jcr:IllegalState:getNode", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			} catch (RepositoryException e) {
				myLogger.warn("jcr:Repository:getNode", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			}
			Node returnNode = null;
			try {
				returnNode = rootNode.getNode(docPath.getPathAsString());
			} catch (PathNotFoundException ex) {
				// Den Knoten gibt es nicht Null zurueckgeben
			}
			// getSession().logout();
			return returnNode;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public Node getNode(String sPath) {
		try {
			// checkAndDoLogin();
			Node rootNode = null;
			try {
				rootNode = getSession().getRootNode();
			} catch (IllegalStateException e) {
				// session timed out?
				// session = repo.login(cred);
				myLogger.warn("jcr:IllegalState:getNode(path)", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			} catch (RepositoryException e) {
				myLogger.warn("jcr:Repository:getNode(path)", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			}
			Node returnNode = null;
			try {
				returnNode = rootNode.getNode(sPath);
			} catch (PathNotFoundException ex) {
				// Den Knoten gibt es nicht Null zurueckgeben
			}
			// getSession().logout();
			return returnNode;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}

	}

	/**
	 * Liefert alle JCRDocDto's die im angegeben Pfad gespeichert sind.<br>
	 * M&ouml;gliche Pfadangaben k&ouml;nnen so aussehen:<br>
	 * <b>HELIUMV/001/Angebot/Angebot/<br>
	 * HELUMV/001/Angebot/Angebot/*&#47Angebotpositionen/<br>
	 * HELUMV/001/Angebot/Angebot/*&#47Angebotpositionen/#</b><br>
	 * Der Operator * steht f&uuml;r alle Ordner die in dieser Ebene vorhanden
	 * sind.<br>
	 * Der Operator # zum Schluss gibt an, dass auch alle Unterordner durchsucht
	 * werden sollten.
	 */
	private ArrayList<JCRDocDto> getAllJCRDocs(JcrSession jcrSession, String path, int limit) {

		ArrayList<JCRDocDto> list = new ArrayList<JCRDocDto>();
		int fixedPathLength = path.indexOf("*");
		boolean withSubNodes = path.endsWith("#");

		if (fixedPathLength != -1) {
			Node node = jcrSession.getNode(path.substring(0, fixedPathLength));
			if (null == node)
				return list;

			NodeIterator it;
			try {
				it = node.getNodes();
			} catch (Exception e) {
				myLogger.warn("Unexpected ex", e);
				return list;
			}

			while (it.hasNext()) {
				Node child = it.nextNode();
				try {
					list.addAll(
							getAllJCRDocs(jcrSession, path.replaceFirst("\\*", child.getName()), limit - list.size()));
				} catch (RepositoryException e) {
					myLogger.warn("Ignored RepositoryException", e);
				}

				if (list.size() >= limit)
					break;

			}
			// list.addAll(getAllJCRDocs(path.substring(0, fixedPathLength)));
		} else {
			if (withSubNodes) {
				path = path.substring(0, path.length() - 1);
			}
			list.addAll(getAllJCRDocs(jcrSession.getNode(path), withSubNodes));
		}
		return list;
	}

	private ArrayList<JCRDocDto> getAllJCRDocs(Node node, boolean withSubNodes) {

		ArrayList<JCRDocDto> list = new ArrayList<JCRDocDto>();

		if (node == null)
			return list;

		NodeIterator iter;
		try {
			iter = node.getNodes();
		} catch (RepositoryException e) {
			return list;
		}
		while (iter.hasNext()) {
			Node child = iter.nextNode();
			try {
				JCRDocDto docDto = new JCRDocDto(child, false);
				list.add(docDto);
			} catch (PathNotFoundException ex) {
			} catch (ValueFormatException e) {
			} catch (RepositoryException e) {
			} catch (IOException e) {
			}

			if (withSubNodes) {
				list.addAll(getAllJCRDocs(child, true));
			}
		}
		return list;
	}

	public ArrayList<DocPath> searchDocNodes(String sToSearch, DocPath startDocPath) {
		try {
			ArrayList<DocPath> al = new ArrayList<DocPath>();
			al = searchInNodeProperty(startDocPath, sToSearch, al);
			al = searchInNodeProperty(new HeliumDocPath().add(new DocNodeLiteral(LocaleFac.BELEGART_ARTIKEL)),
					sToSearch, al);
			al = searchInNodeProperty(new HeliumDocPath().add(new DocNodeLiteral(LocaleFac.BELEGART_PARTNER)),
					sToSearch, al);
			al = searchInNodeProperty(new HeliumDocPath().add(new DocNodeLiteral(LocaleFac.BELEGART_SYSTEM)), sToSearch,
					al);
			return al;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private ArrayList<DocPath> searchInNodeProperty(DocPath startDocPath, String sToSearch,
			ArrayList<DocPath> alreadyFound) {
		try {
			if (alreadyFound == null) {
				alreadyFound = new ArrayList<DocPath>();
			}
			Workspace ws = null;
			try {
				ws = getSession().getWorkspace();
			} catch (IllegalStateException e) {
				// session timed out?
				// session = repo.login(cred);
				myLogger.warn("jcr:IllegalState:searchInNodeProperty", e);
				forcedCloseSession();
				ws = getSession().getWorkspace();
			}
			QueryManager qm = ws.getQueryManager();

			javax.jcr.query.Query q = qm.createQuery("SELECT * FROM nt:base WHERE jcr:path LIKE '/"
					+ startDocPath.getPathAsString() + "/%'" + " AND ( CONTAINS(" + JCRDocFac.PROPERTY_NAME + ", '"
					+ sToSearch.toLowerCase() + "') OR CONTAINS(" + JCRDocFac.PROPERTY_SCHLAGWORTE + ", '"
					+ sToSearch.toLowerCase() + "'))", javax.jcr.query.Query.SQL);
			// "SELECT * FROM nt:note WHERE" //nt:version." + sProperty + " =
			// '*" + sToSearch + "*' AND"
			// + " PATH() = '/jcr:root/'", javax.jcr.query.Query.SQL); // +
			// startDocPath.getPathAsString() + "/'",
			// javax.jcr.query.Query.SQL);
			// javax.jcr.query.Query q = qm.createQuery("SELECT * FROM nt:base",
			// javax.jcr.query.Query.SQL);

			// qm.createQuery("/jcr:root/HELIUMV//element(*)[@NAME='a']",
			// javax.jcr.query.Query.XPATH).execute().getNodes().nextNode();
			// qm.createQuery("//*[jcr:contains(@NAME,'*0*')]",
			// javax.jcr.query.Query.XPATH).execute().getNodes().nextNode();
			QueryResult res = q.execute();
			NodeIterator it = res.getNodes();
			while (it.hasNext()) {
				Node n = it.nextNode();
				try {
					JCRDocDto temp = new JCRDocDto(n, false);
					DocPath path = getDocPathFromJCR(temp);

					if (!alreadyFound.contains(path))
						alreadyFound.add(path);

				} catch (PathNotFoundException ex) {
					// Es handelt sich bei dem Node um kein Dokument
					// al.add(n.getName());
				}
			}
			return alreadyFound;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public JCRDocDto getData(JCRDocDto jcrDocDto) {
		JcrSession jcrSession = new JcrSession();
		try {
			return getData(jcrSession, jcrDocDto);
		} finally {
			myLogger.info("finally closing session getData");
			jcrSession.closeSession();
		}

//		try {
//			Node node = jcrSession.getNode(jcrDocDto.getsPath());
//			return new JCRDocDto(node, true);
//		} catch (Exception e) {
//			myLogger.error("getData", e);
//			return null;
//		} finally {
//			myLogger.info("finally closing session getData");
//			jcrSession.closeSession();
//		}
	}

	private JCRDocDto getData(JcrSession jcrSession, JCRDocDto jcrDocDto) {
		try {
			Node node = jcrSession.getNode(jcrDocDto.getsPath());
			return new JCRDocDto(node, true);
		} catch (Exception e) {
			myLogger.error("getData", e);
			return null;
		}		
	}
	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JCRRepoInfo checkIfNodeExists(DocPath dPath) {
		JCRRepoInfo info = new JCRRepoInfo();
		JcrSession jcrSession = new JcrSession();

		try {
			info.setOnline(jcrSession.isOnline());
			if (info.isOnline()) {
				boolean exists = checkIfNodeExistsWithinTransaction(jcrSession, dPath.getPathAsString());
				info.setExists(exists);
			}
		} catch (NamingException e) {
			myLogger.error("namingException", e);
		} finally {
			myLogger.info("finally closing session checkIfNodeExists");
			jcrSession.closeSession();
		}

		return info;
	}

	private boolean checkIfNodeExistsWithinTransaction(DocPath dPath) {
		return checkIfNodeExistsWithinTransaction(dPath.getPathAsString());
	}

	private boolean checkIfNodeExistsWithinTransaction(String sPath) {
		try {
			if (sPath == null) {
				return false;
			}
			// checkAndDoLogin();
			Node rootNode = null;
			try {
				rootNode = getSession().getRootNode();
			} catch (IllegalStateException e) {
				// session timed out?
				// session = repo.login(cred);
				myLogger.warn("jcr:IllegalState:checkIfNodeExistsWithinTransaction", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			} catch (RepositoryException e) {
				myLogger.warn("jcr:RepositoryException:checkIfNodeExistsWithinTransaction", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			}
			try {
				String propertyName = PROPERTY_VERSION;
//				boolean exists = rootNode.getNode(sPath).hasProperty(propertyName) ;
//				return exists ;
				rootNode.getNode(sPath).getProperty(propertyName);
				return true;
			} catch (PathNotFoundException ex) {
				try {
					DocNodeFactory.createDocNodeByNode(rootNode.getNode(sPath));
					return true;
				} catch (PathNotFoundException e) {
					// Den Knoten gibt es nicht => false
					return false;
				}
			} finally {
				closeSession();
			}
		} catch (Exception e) {
			String s = "JCR-Node-Exists:Path: <" + sPath + ">.";
			myLogger.error(s, e);
			ArrayList<Object> ao = new ArrayList<Object>();
			ao.add(s);

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_JCR_ROOT_EXISTIERT_NICHT, ao, e);
		}
	}

	private boolean checkIfNodeExistsWithinTransaction(JcrSession jcrSession, String sPath) {
		try {
			if (sPath == null) {
				return false;
			}

			// checkAndDoLogin();
			Node rootNode = null;
			try {
				rootNode = jcrSession.getRootNode();
			} catch (IllegalStateException e) {
				// session timed out?
				// session = repo.login(cred);
				myLogger.warn("jcr:IllegalState:checkIfNodeExistsWithinTransaction", e);
				jcrSession.forcedCloseSession();
				rootNode = jcrSession.getRootNode();
			} catch (RepositoryException e) {
				myLogger.warn("jcr:RepositoryException:checkIfNodeExistsWithinTransaction", e);
				jcrSession.forcedCloseSession();
				rootNode = jcrSession.getRootNode();
			}

			try {
				String propertyName = PROPERTY_VERSION;
				rootNode.getNode(sPath).getProperty(propertyName);
				return true;
			} catch (PathNotFoundException ex) {
				try {
					DocNodeFactory.createDocNodeByNode(rootNode.getNode(sPath));
					return true;
				} catch (PathNotFoundException e) {
					// Den Knoten gibt es nicht => false
					return false;
				}
			}
		} catch (Exception e) {
			String s = "JCR-Node-Exists:Path: <" + sPath + ">.";
			myLogger.error(s, e);
			ArrayList<Object> ao = new ArrayList<Object>();
			ao.add(s);

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_JCR_ROOT_EXISTIERT_NICHT, ao, e);
		}
	}

	public DocPath getDocPathFromJCR(JCRDocDto jcrDocDto)
			throws ValueFormatException, PathNotFoundException, RepositoryException, IOException {
		if (jcrDocDto.getDocPath() != null)
			return jcrDocDto.getDocPath();
		ArrayList<DocNodeBase> docNodes = new ArrayList<DocNodeBase>();
		String parts[] = jcrDocDto.getsPath().split("/");
		String tempPath = "";

		for (int i = 0; i < parts.length; i++) {
			tempPath += (i == 0 ? "" : "/") + parts[i];
			DocNodeBase docNode;
			try {
				docNode = getDocNodeFromSPath(tempPath);
			} catch (PathNotFoundException ex) {
				docNode = new VisualNodeLiteral(parts[i], "color:gray");
				docNode.setVersion(1);
			}
			if (docNode != null)
				docNodes.add(docNode);
		}
		jcrDocDto.setDocPath(new DocPath().add(docNodes));
		return jcrDocDto.getDocPath();
	}

	private DocNodeBase getDocNodeFromSPath(String sPath)
			throws ValueFormatException, PathNotFoundException, RepositoryException, IOException {
		return DocNodeFactory.createDocNodeByNode(getNode(sPath));
	}

	private Node setProperties(Node node, JCRDocDto jcrDocDto) throws ValueFormatException, VersionException,
			LockException, ConstraintViolationException, RepositoryException {
		// Das Dokument selbst
		if (jcrDocDto.getbData() != null) {
			InputStream stream = new ByteArrayInputStream(jcrDocDto.getbData());
			node.setProperty(PROPERTY_DATA, stream);
		}
		// Weitere Properties

		node.setProperty(PROPERTY_BELEGART, jcrDocDto.getsBelegart());
		node.setProperty(PROPERTY_ANLEGER, jcrDocDto.getlAnleger());
		node.setProperty(PROPERTY_PARTNER, jcrDocDto.getlPartner());
		node.setProperty(PROPERTY_ZEITPUNKT, jcrDocDto.getlZeitpunkt());
		node.setProperty(PROPERTY_BELEGNUMMER, jcrDocDto.getsBelegnummer());
		node.setProperty(PROPERTY_SCHLAGWORTE, jcrDocDto.getsSchlagworte());
		node.setProperty(PROPERTY_NAME, jcrDocDto.getsName());
		node.setProperty(PROPERTY_FILENAME, jcrDocDto.getsFilename());
		node.setProperty(PROPERTY_TABLE, jcrDocDto.getsTable());
		node.setProperty(PROPERTY_ROW, jcrDocDto.getsRow());
		node.setProperty(PROPERTY_MIME, jcrDocDto.getsMIME());
		node.setProperty(PROPERTY_VERSION, jcrDocDto.getlVersion());
		node.setProperty(PROPERTY_VERSTECKT, jcrDocDto.getbVersteckt());
		node.setProperty(PROPERTY_SICHERHEITSSTUFE, jcrDocDto.getlSicherheitsstufe());
		node.setProperty(PROPERTY_GRUPPIERUNG, jcrDocDto.getsGruppierung());
		return node;
	}

	public JCRDocDto getJCRDocDtoFromNode(DocPath docPath) throws RepositoryException, IOException {
		JcrSession jcrSession = new JcrSession();
		try {
			return getJCRDocDtoFromNode(jcrSession, docPath);
		} finally {
			jcrSession.closeSession();
		}
	}

	private JCRDocDto getJCRDocDtoFromNode(JcrSession jcrSession, DocPath docPath)
			throws RepositoryException, IOException {
		Node node = jcrSession.getNode(docPath);
		if (node == null)
			return null;

		JCRDocDto jcr = null;
		try {
			jcr = new JCRDocDto(node, false);
		} catch (Exception ex) {
			return null;
		}

		try {
			jcr.setDocPath(getDocPathFromJCR(jcr));
		} catch (PathNotFoundException ex) {
		}

		return jcr;
	}

	/**
	 * Gibt die DocNodes aus dem angegeben Pfad sortiert zurueck.
	 *
	 */
	public List<DocNodeBase> getDocNodeChildrenFromNode(DocPath docPath, TheClientDto theClientDto)
			throws RepositoryException, IOException {
		JcrSession jcrSession = new JcrSession();
		try {
			return getDocNodeChildrenFromNode(jcrSession, docPath, theClientDto);
		} finally {
			myLogger.info("jcrSession::finally closing session");
			jcrSession.closeSession();
		}

		/*
		 * DocPath newPath = new DocPath(); for (DocNodeBase docNode :
		 * docPath.asDocNodeList()) { if (docNode.getNodeType() ==
		 * DocNodeBase.SYMBOLIC_LINK) { newPath = ((DocNodeSymbolicLink)
		 * docNode).getLinkedPath(); // Wenn ich einen symbolischen Link habe, will ich
		 * dessen // Unterordner sehen } else { newPath.add(docNode); } } docPath =
		 * newPath;
		 * 
		 * List<DocNodeBase> docNodes = new ArrayList<DocNodeBase>();
		 * 
		 * try { Node node = getNode(docPath); if (node != null) { NodeIterator ni =
		 * node.getNodes(); while (ni.hasNext()) { Node helper = ni.nextNode(); try {
		 * DocNodeBase docNode = DocNodeFactory.createDocNodeByNode(helper); if
		 * (isNodeValidForPath(docNode, docPath, theClientDto)) docNodes.add(docNode); }
		 * catch (PathNotFoundException ex) { try { // Die alten Files anhaengen
		 * JCRDocDto jcr = new JCRDocDto(helper, false); DocNodeBase dn = new
		 * VisualNodeFile(jcr, "color:gray"); dn.setVersion(1); docNodes.add(dn); }
		 * catch (PathNotFoundException pnfEx) { myLogger.warn("PathNotFound", pnfEx);
		 * 
		 * // Die alten Ordner anhaengen DocNodeBase dn = new
		 * VisualNodeLiteral(helper.getName(), "color:gray"); dn.setVersion(1);
		 * docNodes.add(dn); } } } } } finally { closeSession(); }
		 * 
		 * if (theClientDto != null) { docNodes.addAll(getSpezificNodesForPath(docPath,
		 * theClientDto)); docNodes.addAll(getStaticNodesForPath(docPath,
		 * theClientDto.getMandant())); }
		 * 
		 * Collections.sort(docNodes, new DocNodeComparator()); return docNodes;
		 */
	}

	private List<DocNodeBase> getDocNodeChildrenFromNode(JcrSession jcrSession, DocPath docPath,
			TheClientDto theClientDto) throws RepositoryException, IOException {

		DocPath newPath = new DocPath();
		for (DocNodeBase docNode : docPath.asDocNodeList()) {
			if (docNode.getNodeType() == DocNodeBase.SYMBOLIC_LINK) {
				newPath = ((DocNodeSymbolicLink) docNode).getLinkedPath();
				// Wenn ich einen symbolischen Link habe, will ich dessen
				// Unterordner sehen
			} else {
				newPath.add(docNode);
			}
		}
		docPath = newPath;

		List<DocNodeBase> docNodes = new ArrayList<DocNodeBase>();

		Node node = jcrSession.getNode(docPath);
		if (node != null) {
			NodeIterator ni = node.getNodes();
			while (ni.hasNext()) {
				Node helper = ni.nextNode();
				try {
					DocNodeBase docNode = DocNodeFactory.createDocNodeByNode(helper);
					if (isNodeValidForPath(docNode, docPath, theClientDto))
						docNodes.add(docNode);
				} catch (PathNotFoundException ex) {
					try { // Die alten Files anhaengen
						JCRDocDto jcr = new JCRDocDto(helper, false);
						DocNodeBase dn = new VisualNodeFile(jcr, "color:gray");
						dn.setVersion(1);
						docNodes.add(dn);
					} catch (PathNotFoundException pnfEx) {
						myLogger.warn("PathNotFound", pnfEx);

						// Die alten Ordner anhaengen
						DocNodeBase dn = new VisualNodeLiteral(helper.getName(), "color:gray");
						dn.setVersion(1);
						docNodes.add(dn);
					}
				}
			}
		}

		if (theClientDto != null) {
			docNodes.addAll(getSpezificNodesForPath(jcrSession, docPath, theClientDto));
			docNodes.addAll(getStaticNodesForPath(docPath, theClientDto.getMandant()));
		}

		Collections.sort(docNodes, new DocNodeComparator());
		return docNodes;
	}

	/**
	 * Pfadnamen der Nodes welche, wenn sie aus dem CMS kommen, nie angezeigt
	 * werden.<br>
	 * Pfadname = docNode.asPath()
	 */
	private static List<String> NodesFromCMSToFilter = Arrays.asList(DocNodeBase.BELEGART_PARTNER,
			DocNodeBase.BELEGART_LIEFERANT, DocNodeBase.BELEGART_KUNDE);
	/**
	 * Pfadnamen der Nodes welche, wenn sie aus dem CMS kommen, nur angezeigt
	 * werden, wenn man LPAdmin ist.<br>
	 * Pfadname = docNode.asPath()
	 */
	private static List<String> NodesFromCMSToFilterWhenNotAdmin = Arrays.asList(DocNodeBase.BELEGART_SYSTEM);

	/**
	 * Darf der DocNode in diesem Pfad angezeigt werden?
	 *
	 * @param node
	 * @param path
	 * @param theClientDto
	 * @return true wenn der Knoten angezeigt werden darf
	 */
	private boolean isNodeValidForPath(DocNodeBase node, DocPath path, TheClientDto theClientDto) {
		if (NodesFromCMSToFilter.contains(node.asPath())) {
			return false;
		}
		if (!HelperServer.isLPAdmin(theClientDto)) {
			if (NodesFromCMSToFilterWhenNotAdmin.contains(node.asPath())) {
				return false;
			}
			if (path.size() == 1 && theClientDto != null && !node.asPath().equals(theClientDto.getMandant())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gibt die im Pfad dynamisch existierenden Nodes zurueck.<br>
	 * zB. wird im Pfad HELIUMV/Partner/Partner fuer jeden Partner (also auch ohne
	 * hinterlegten Dokumenten) ein DocNodePartner angehaengt.<br>
	 * Die zurueckgegebenen Nodes koennen, aber muessen nicht im CMS existieren.
	 *
	 * @param docPath
	 * @param theClientDto
	 * @return eine (leere) Liste von Knoten im angegebenen Pfad
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	private List<DocNodeBase> getSpezificNodesForPath(JcrSession jcrSession, DocPath docPath, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		List<DocNodeBase> docNodes = new ArrayList<DocNodeBase>();
		if (docPath.size() == 3 && docPath.getLastDocNode().asPath().equals(DocNodeBase.BELEGART_PARTNER)) {
			docNodes.addAll(getAllPartnerNodes());
		}
		if (docPath.size() == 4 && docPath.getDocNodeAt(1).asPath().equals(theClientDto.getMandant())) {
			if (docPath.getLastDocNode().asPath().equals(DocNodeBase.BELEGART_KUNDE)) {
				docNodes.addAll(getAllKundenNodes(theClientDto.getMandant()));
			}
			if (docPath.getLastDocNode().asPath().equals(DocNodeBase.BELEGART_LIEFERANT)) {
				docNodes.addAll(getAllLieferantenNodes(theClientDto.getMandant()));
			}
		}

		if (docPath.getLastDocNode() instanceof DocNodePartner) {
			DocNodePartner partnerNode = (DocNodePartner) docPath.getLastDocNode();
			docNodes.addAll(getAllDocumentsForPartner(jcrSession, partnerNode.getiId(), theClientDto.getMandant()));
		} else if (docPath.getLastDocNode() instanceof DocNodeKunde) {
			DocNodeKunde kundeNode = (DocNodeKunde) docPath.getLastDocNode();
			docNodes.addAll(getAllDocumentsForKunde(jcrSession, kundeNode.getiId(), theClientDto.getMandant()));
		} else if (docPath.getLastDocNode() instanceof DocNodeLieferant) {
			DocNodeLieferant lieferantNode = (DocNodeLieferant) docPath.getLastDocNode();
			docNodes.addAll(getAllDocumentsForLieferant(jcrSession, lieferantNode.getiId(), theClientDto.getMandant()));
		}
		return docNodes;
	}

	/**
	 * Gibt die immer in der View anzuzeigenden Nodes im &uuml;bergebenen Pfad
	 * zur&uuml;ck.</br>
	 * <p>
	 * Wir zeigen beispielsweise immer einen Partner-Knoten an, der physisch im DMS
	 * so gar nicht existiert, hier aber mitgeliefert wird
	 * </p>
	 *
	 * @param docPath
	 * @param mandant
	 * @return eine (leere) Liste von Knoten im angegebenen Pfad
	 */
	private List<DocNodeBase> getStaticNodesForPath(DocPath docPath, String mandant) {
		List<DocNodeBase> docNodes = new ArrayList<DocNodeBase>();

		// TODO rk: Das geht sicher auch schoener
		if (docPath.size() == 1) {
			docNodes.add(new DocNodeFolder(DocNodeBase.BELEGART_PARTNER)); // HELIUMV/Partner
		} else if (docPath.size() == 2) {
			if (docPath.getLastDocNode().asPath().equals(DocNodeBase.BELEGART_PARTNER)) {
				docNodes.add(new DocNodeFolder(DocNodeBase.BELEGART_PARTNER)); // HELIUMV/Partner/Partner/
			} else if (docPath.getLastDocNode().asPath().equals(mandant)) {
				docNodes.add(new DocNodeFolder(DocNodeBase.BELEGART_KUNDE)); // HELIUMV/xxx/Kunde/
				docNodes.add(new DocNodeFolder(DocNodeBase.BELEGART_LIEFERANT)); // HELIUMV/xxx/Lieferant/
			}
		} else if (docPath.size() == 3) {
			if (docPath.getLastDocNode().asPath().equals(DocNodeBase.BELEGART_KUNDE)) {
				docNodes.add(new DocNodeFolder(DocNodeBase.BELEGART_KUNDE)); // HELIUMV/xxx/Kunde/Kunde
			} else if (docPath.getLastDocNode().asPath().equals(DocNodeBase.BELEGART_LIEFERANT)) {
				docNodes.add(new DocNodeFolder(DocNodeBase.BELEGART_LIEFERANT)); // HELIUMV/xxx/Lieferant/Lieferant
			}
		}
		return docNodes;
	}

	/**
	 * Nur fuer Instanzierung von DocNodes verwenden!!!!
	 *
	 * @param flrPartner
	 * @return ein PartnerDto bei dem nur die Adressinfos gefuellt sind
	 */
	private PartnerDto createPartnerDtoFromFLRPartner(FLRPartner flrPartner) {
		PartnerDto dto = new PartnerDto();
		dto.setIId(flrPartner.getI_id());
		dto.setCName1nachnamefirmazeile1(flrPartner.getC_name1nachnamefirmazeile1());
		dto.setCName2vornamefirmazeile2(flrPartner.getC_name2vornamefirmazeile2());
		dto.setCName3vorname2abteilung(flrPartner.getC_name3vorname2abteilung());
		return dto;
	}

	/**
	 * Gibt eine Liste von DocNodePartner ALLER Partner unsortiert zurueck,
	 * unabhaengig davon, ob der Partner wirklich Dokumente hinterlegt hat, oder
	 * nicht
	 *
	 * @return eine (leere) Liste von DocNodePartner
	 */
	private List<DocNodePartner> getAllPartnerNodes() {
		List<DocNodePartner> docNodes = new ArrayList<DocNodePartner>();

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();
		Criteria c = session.createCriteria(FLRPartner.class);
		for (Object o : c.list()) {
			FLRPartner partner = (FLRPartner) o;
			DocNodePartner node = new DocNodePartner(createPartnerDtoFromFLRPartner(partner));
			// DocPath path = new DocPath(node);
			// if (!checkIfNodeExists(path)) {
			docNodes.add(node);
			// }
			// }
		}
		return docNodes;
	}

	/**
	 * Gibt eine Liste von DocNodeLieferant ALLER Lieferanten unsortiert zurueck,
	 * unabhaengig davon, ob der Kunde wirklich Dokumente hinterlegt hat, oder nicht
	 *
	 * @return (leere) Liste aller DocNodeLieferant(en) fuer den Mandanten
	 */
	private List<DocNodeLieferant> getAllLieferantenNodes(String mandant) {
		List<DocNodeLieferant> docNodes = new ArrayList<DocNodeLieferant>();
		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();
		Criteria c = session.createCriteria(FLRLieferant.class);
		c.add(Restrictions.eq("mandant_c_nr", mandant));
		for (Object o : c.list()) {
			FLRLieferant lieferant = (FLRLieferant) o;
			LieferantDto lieferantDto = new LieferantDto();
			lieferantDto.setMandantCNr(mandant);
			lieferantDto.setIId(lieferant.getI_id());
			DocNodeLieferant node = new DocNodeLieferant(lieferantDto,
					createPartnerDtoFromFLRPartner(lieferant.getFlrpartner()));
			// DocPath path = new DocPath(node);
			// if (!checkIfNodeExists(path)) {
			docNodes.add(node);
			// }
			// }
		}
		return docNodes;
	}

	/**
	 * Gibt eine Liste von DocNodeKunde ALLER Kunden unsortiert zurueck, unabhaengig
	 * davon, ob der Kunde wirklich Dokumente hinterlegt hat, oder nicht
	 *
	 * @return (leere) LIste aller DocNodeKunden im angegebenen Mandanten
	 */
	private List<DocNodeKunde> getAllKundenNodes(String mandant) {
		List<DocNodeKunde> docNodes = new ArrayList<DocNodeKunde>();
		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();
		Criteria c = session.createCriteria(FLRKunde.class);
		c.add(Restrictions.eq("mandant_c_nr", mandant));
		for (Object o : c.list()) {
			FLRKunde kunde = (FLRKunde) o;
			KundeDto kundeDto = new KundeDto();
			kundeDto.setIId(kunde.getI_id());
			kundeDto.setMandantCNr(mandant);

			DocNodeKunde node = new DocNodeKunde(kundeDto, createPartnerDtoFromFLRPartner(kunde.getFlrpartner()));
			// DocPath path = new DocPath(node);
			// if (!checkIfNodeExists(path)) {
			docNodes.add(node);
			// }
			// }
		}
		return docNodes;
	}


	public ArrayList<JCRDocDto> holeDokumenteZuUseCase(Integer key, int useCaseId, TheClientDto theClientDto) {

		PrintInfoDto piDto = getPathAndPartnerAndTable(key, useCaseId, theClientDto);
		ArrayList<JCRDocDto> alTemp = new ArrayList<JCRDocDto>();
		if (piDto != null && piDto.getDocPath() != null) {
			JcrSession jcrSession = new JcrSession();
			try {
				for (DocNodeBase docNode : getDocNodeChildrenFromNode(jcrSession, piDto.getDocPath(), theClientDto)) {
					JCRDocDto jcr = getJCRDocDtoFromNode(jcrSession, piDto.getDocPath().getDeepCopy().add(docNode));
					jcr = getData(jcr);
					alTemp.add(jcr);
				}
			} catch (RepositoryException e) {
				myLogger.error("RepositoryException", e);
			} catch (IOException e) {
				myLogger.error("IOException", e);
			} finally {
				myLogger.info("finally closing session holeDokumenteZuUseCase");
				jcrSession.closeSession();
			}
		}

		return alTemp;
	}

	public ArrayList<DocNodeVersion> getAllVersionsSession(JCRDocDto jcrDocDto) {
		JcrSession jcrSession = new JcrSession();
		try {
			return getAllVersions(jcrSession, jcrDocDto);
		} finally {
			myLogger.info("finally closing session (getAllVersionsSession)");
			jcrSession.closeSession();
		}
	}

	@Override
	public DocNodeVersion getLastVersionOfJcrDoc(JCRDocDto jcrDocDto) {
		Validator.dtoNotNull(jcrDocDto, "jcrDocDto");

		JcrSession jcrSession = new JcrSession();
		try {
			List<DocNodeVersion> versions = getAllVersions(jcrSession, jcrDocDto);
			return versions.isEmpty() ? null : versions.get(0);
		} finally {
			myLogger.info("finally closing session (getLastVersionOfJcrDoc)");
			jcrSession.closeSession();
		}
	}

	private ArrayList<DocNodeVersion> getAllVersions(JcrSession jcrSession, JCRDocDto jcrDocDto) {
		ArrayList<DocNodeVersion> retArray = new ArrayList<DocNodeVersion>();
		try {
			Node rootNode = null;
			try {
				rootNode = jcrSession.getRootNode();
			} catch (IllegalStateException e) {
				myLogger.warn("jcr:IllegalState:getAllVersions()", e);
				jcrSession.forcedCloseSession();
				rootNode = jcrSession.getRootNode();
			} catch (RepositoryException e) {
				myLogger.warn("jcr:RepositoryException:getAllVersions()", e);
				jcrSession.forcedCloseSession();
				rootNode = jcrSession.getRootNode();
			}
			Node node = rootNode.getNode(jcrDocDto.getsPath());

			VersionHistory hist = node.getVersionHistory();
			// String p = node.getPath() ;
			// VersionHistory hist =
			// getSession().getWorkspace().getVersionManager().getVersionHistory(p)
			// ;
			VersionIterator vIt = hist.getAllVersions();
			// if (vIt.hasNext()) {
			// List<Node> nodelist = new ArrayList<Node>();
			// while(vIt.hasNext())
			// nodelist.add(vIt.nextVersion());
			//
			// System.out.println(nodelist.size());
			// }
			// vIt = hist.getAllVersions();
			while (vIt.hasNext()) {
				Version v = vIt.nextVersion();
				NodeIterator niVersion = v.getNodes("jcr:frozenNode");
				// NodeIterator niAll = v.getNodes();
				// if (niAll.hasNext()) {
				// List<Node> nodelist = new ArrayList<Node>();
				// while(niAll.hasNext())
				// nodelist.add(niAll.nextNode());
				//
				// System.out.println(nodelist.size());
				// }
				if (niVersion.hasNext()) {
					Node no = niVersion.nextNode();
					try {
						JCRDocDto jcr = new JCRDocDto(no, false);
						retArray.add(0, new DocNodeVersion(jcr));
					} catch (PathNotFoundException ex) {
						// ignorieren... Knoten ist kein Dokument und irrelevant
					}
				}
			}
		} catch (RepositoryException e) {
			myLogger.error("RepositoryException", e);
		} catch (IOException e) {
			myLogger.error("IOException", e);
		} catch (Exception ex) {
			myLogger.error("Ex", ex);
		}

		Collections.sort(retArray, new DocNodeComparator());
		return retArray;
	}

	public ArrayList<DocNodeVersion> getAllVersions(JCRDocDto jcrDocDto) {
		ArrayList<DocNodeVersion> retArray = new ArrayList<DocNodeVersion>();
		try {
			// try{
			// //checkAndDoLogin();
			// }catch(NamingException e){}
			Node rootNode = null;
			try {
				rootNode = getSession().getRootNode();
			} catch (IllegalStateException e) {
				myLogger.warn("jcr:IllegalState:getAllVersions()", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			} catch (RepositoryException e) {
				// session timed out?
				// session = repo.login(cred);
				myLogger.warn("jcr:RepositoryException:getAllVersions()", e);
				forcedCloseSession();
				rootNode = getSession().getRootNode();
			}
			Node node = rootNode.getNode(jcrDocDto.getsPath());

			VersionHistory hist = node.getVersionHistory();
			// String p = node.getPath() ;
			// VersionHistory hist =
			// getSession().getWorkspace().getVersionManager().getVersionHistory(p)
			// ;
			VersionIterator vIt = hist.getAllVersions();
			// if (vIt.hasNext()) {
			// List<Node> nodelist = new ArrayList<Node>();
			// while(vIt.hasNext())
			// nodelist.add(vIt.nextVersion());
			//
			// System.out.println(nodelist.size());
			// }
			// vIt = hist.getAllVersions();
			while (vIt.hasNext()) {
				Version v = vIt.nextVersion();
				NodeIterator niVersion = v.getNodes("jcr:frozenNode");
				// NodeIterator niAll = v.getNodes();
				// if (niAll.hasNext()) {
				// List<Node> nodelist = new ArrayList<Node>();
				// while(niAll.hasNext())
				// nodelist.add(niAll.nextNode());
				//
				// System.out.println(nodelist.size());
				// }
				if (niVersion.hasNext()) {
					Node no = niVersion.nextNode();
					try {
						JCRDocDto jcr = new JCRDocDto(no, false);
						retArray.add(0, new DocNodeVersion(jcr));
					} catch (PathNotFoundException ex) {
						// ignorieren... Knoten ist kein Dokument und irrelevant
					}
				}
			}
			// getSession().logout();
		} catch (RepositoryException e) {
			myLogger.error("RepositoryException", e);
			// e.printStackTrace();
		} catch (IOException e) {
			myLogger.error("IOException", e);

			// Nothing to do maybe no versions
			// e.printStackTrace() ;
		} catch (Exception ex) {
			myLogger.error("Ex", ex);
		}
		Collections.sort(retArray, new DocNodeComparator());
		return retArray;

	}

	public DokumentbelegartDto[] dokumentbelegartfindbyMandant(String mandantCNr) {
		Query query = em.createNamedQuery("DokumentbelegartfindByMandant");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		List<DokumentbelegartDto> list = new ArrayList<DokumentbelegartDto>();
		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {
				Dokumentbelegart dokumentbelegart = (Dokumentbelegart) iterator.next();
				list.add(new DokumentbelegartDto(dokumentbelegart.getMandantCNr(), dokumentbelegart.getCNr()));
			}
		}
		DokumentbelegartDto[] returnArray = new DokumentbelegartDto[list.size()];
		return list.toArray(returnArray);
	}

	public DokumentgruppierungDto[] dokumentgruppierungfindbyMandant(String mandantCNr) {
		Query query = em.createNamedQuery("DokumentgruppierungfindByMandant");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		List<DokumentgruppierungDto> list = new ArrayList<DokumentgruppierungDto>();
		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {
				Dokumentgruppierung dokumentgruppierung = (Dokumentgruppierung) iterator.next();
				list.add(new DokumentgruppierungDto(dokumentgruppierung.getMandantCNr(), dokumentgruppierung.getCNr()));
			}
		}
		DokumentgruppierungDto[] returnArray = new DokumentgruppierungDto[list.size()];
		return list.toArray(returnArray);
	}

	public DokumentnichtarchiviertDto dokumentnichtarchiviertfindbyMandantCReportname(String mandantCNr,
			String CReportname) {
		Query query = em.createNamedQuery("DokumentnichtarchiviertfindByMandantCReportname");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, CReportname);
		Dokumentnichtarchiviert dokumentnichtarchiviert;
		try {
			dokumentnichtarchiviert = (Dokumentnichtarchiviert) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
		if (dokumentnichtarchiviert != null) {
			return new DokumentnichtarchiviertDto(dokumentnichtarchiviert.getMandantCNr(),
					dokumentnichtarchiviert.getCReportname());
		}
		return null;
	}

	public void deaktiviereArchivierung(String mandantCNr, String CReportname) {
		Dokumentnichtarchiviert da = new Dokumentnichtarchiviert(mandantCNr, CReportname);
		em.merge(da);
		em.flush();
	}

	public DokumentbelegartDto dokumentbelegartfindbyPrimaryKey(DokumentbelegartPK dokumentbelegartPK) {
		Dokumentbelegart dokumentbelegart = em.find(Dokumentbelegart.class, dokumentbelegartPK);
		return new DokumentbelegartDto(dokumentbelegart.getMandantCNr(), dokumentbelegart.getCNr());
	}

	public DokumentgruppierungDto dokumentgruppierungfindbyPrimaryKey(DokumentgruppierungPK dokumentgruppierungPK) {
		Dokumentgruppierung dokumentgruppierung = em.find(Dokumentgruppierung.class, dokumentgruppierungPK);
		return new DokumentgruppierungDto(dokumentgruppierung.getMandantCNr(), dokumentgruppierung.getCNr());
	}

	public void createDokumentgruppierung(DokumentgruppierungDto dokumentgruppierungDto) {
		Dokumentgruppierung dokumentgruppierung = new Dokumentgruppierung(dokumentgruppierungDto.getMandantCNr(),
				dokumentgruppierungDto.getCNr());
		try {
			em.persist(dokumentgruppierung);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_PRIMARY_KEY, e);
		}
	}

	public void createDokumentbelegart(DokumentbelegartDto dokumentbelegartDto) {
		Dokumentbelegart dokumentbelegart = new Dokumentbelegart(dokumentbelegartDto.getMandantCNr(),
				dokumentbelegartDto.getCNr());
		try {
			em.persist(dokumentbelegart);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_PRIMARY_KEY, e);
		}
	}

	public void removeDokumentbelegart(DokumentbelegartDto dokumentbelegartDto) {
		Dokumentbelegart dokumentbelegart = em.find(Dokumentbelegart.class,
				dokumentbelegartDto.getDokumentbelegartPK());
		em.remove(dokumentbelegart);
		em.flush();

	}

	public long getNextVersionNumer(JCRDocDto jcrDocDto) {
		return -1;
	}

	public PrintInfoDto getPathAndPartnerAndTable(Object sKey, Integer idUsecase, TheClientDto theClientDto) {
		if (fastLaneReader == null) {
			fastLaneReader = flrLookUp();
			if (fastLaneReader == null)
				return null;
		}
		// DistributedTableDataSourceImpl.getFastLaneReader(idUsecase,
		// queryParameters);
		UseCaseHandler uch = null;
		try {
			uch = fastLaneReader.getUseCaseHandler(UUIDFuerUseCase, idUsecase, theClientDto);
		} catch (Throwable t) {
			// sfsb removed from jboss
			t.printStackTrace();
			fastLaneReader = flrLookUp();
			if (fastLaneReader == null)
				return null;
			else
				try {
					uch = fastLaneReader.getUseCaseHandler(UUIDFuerUseCase, idUsecase, theClientDto);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
		}

		if (uch != null) {
			PrintInfoDto piDto = uch.getSDocPathAndPartner(sKey);
			if (piDto != null) {
				piDto.setTable(uch.getSTable());
				return piDto;
			}

			try {
				fastLaneReader.cleanup(UUIDFuerUseCase, idUsecase, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			return new PrintInfoDto();
		} else {
			return null;
		}

	}

	private FastLaneReader flrLookUp() {
		FastLaneReader flr = null;
		try {
			flr = FacLookup.lookup(new InitialContext(), FastLaneReaderBean.class, FastLaneReader.class);
		} catch (Exception e) {
			return null;
		}
		return flr;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void kopiereAlteDokumenteInJCR(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BelegartdokumentDto[] alteDokumente = getDokumenteFac().belegartdokumentFindAll();
		int count = alteDokumente.length;
		System.out.println("Kopiere " + count + " Dokumente");
		for (int i = 0; i < alteDokumente.length; i++) {
			System.out.println(i + 1 + "/" + count);
			boolean bCopied = false;
			JCRDocDto jcrDocDto = new JCRDocDto();
			DokumentDto dokumentDto = getDokumenteFac().dokumentFindByPrimaryKey(alteDokumente[i].getDokumentIId());
			jcrDocDto.setbData(dokumentDto.getOInhalt());

			String sDateiname = "unbekannt.pdf";

			if (dokumentDto.getCDateiname() != null) {
				sDateiname = dokumentDto.getCDateiname().replace("/", ".").replace("\\", ".");
			}
			jcrDocDto.setsFilename(sDateiname);
			jcrDocDto.setsMIME(Helper.getMime(sDateiname));
			jcrDocDto.setlAnleger(dokumentDto.getPersonalIIdAnlegen());
			jcrDocDto.setlVersion(0);
			jcrDocDto.setsRow(alteDokumente[i].getIBelegartid().toString());
			jcrDocDto.setbVersteckt(false);
			jcrDocDto.setsName(sDateiname.replace("/", "."));
			jcrDocDto.setlZeitpunkt(dokumentDto.getTAnlegen().getTime());
			jcrDocDto.setlSicherheitsstufe(0);
			jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_KOPIE_GRUPPE);
			jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_KOPIE_BELEGART);
			DokumentschlagwortDto[] dokumentschlagwortDto = getDokumenteFac()
					.dokumentschlagwortFindByDokumentIId(dokumentDto.getIId());
			String sSchlagworte = "\u00FCbernommen ";
			for (int y = 0; y < dokumentschlagwortDto.length; y++) {
				sSchlagworte = sSchlagworte + dokumentschlagwortDto[y].getCSchlagwort() + " ";
			}
			jcrDocDto.setsSchlagworte(sSchlagworte);
			if (alteDokumente[i].getBelegartCNr().equals(LocaleFac.BELEGART_ARTIKEL)) {
				// Dokumente aus dem Artikelmodul kopieren
				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);
				jcrDocDto.setlPartner(mandantDto.getPartnerIId());
				ArtikelDto artikelDto = null;
				artikelDto = getArtikelFac().artikelFindByPrimaryKey(alteDokumente[i].getIBelegartid(), theClientDto);
				if (artikelDto != null) {
					jcrDocDto.setDocPath(new DocPath(new DocNodeArtikel(artikelDto)).add(new DocNodeFile(sDateiname)));
					// jcrDocDto.setsFullNodePath(JCRDocFac.HELIUMV_NODE + "/"
					// + LocaleFac.BELEGART_ARTIKEL.trim() + "/"
					// + artikelDto.getArtikelartCNr().trim() + "/"
					// + artikelDto.getCNr().replace("/", ".") + "/"
					// + sDateiname);
				}
				jcrDocDto.setsBelegnummer(artikelDto.getCNr());
				jcrDocDto.setsTable("ARTIKEL");
				bCopied = true;
			}
			if (alteDokumente[i].getBelegartCNr().equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
				EingangsrechnungDto erDto = null;
				PartnerDto partnerDto = null;
				LieferantDto lieferantDto = null;
				erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(alteDokumente[i].getIBelegartid());
				lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(erDto.getLieferantIId(), theClientDto);
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto);
				jcrDocDto.setsBelegnummer(erDto.getCNr());
				jcrDocDto.setlPartner(partnerDto.getIId());
				jcrDocDto.setsTable("EINGANGSRECHNUNG");
				jcrDocDto.setDocPath(new DocPath(new DocNodeEingangsrechnung(erDto)).add(new DocNodeFile(sDateiname)));
				// jcrDocDto.setsFullNodePath(JCRDocFac.HELIUMV_NODE + "/"
				// + LocaleFac.BELEGART_EINGANGSRECHNUNG.trim() + "/"
				// + LocaleFac.BELEGART_EINGANGSRECHNUNG.trim() + "/"
				// + erDto.getCNr().replace("/", ".") + "/" + sDateiname);
				bCopied = true;
			}
			if (alteDokumente[i].getBelegartCNr().equals(LocaleFac.BELEGART_KUNDE)) {
				KundeDto kundeDto = null;
				PartnerDto partnerDto = null;
				kundeDto = getKundeFac().kundeFindByPrimaryKey(alteDokumente[i].getIBelegartid(), theClientDto);
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
				String sKunde = partnerDto.getCName1nachnamefirmazeile1().replace("/", ".");
				if (partnerDto.getCName2vornamefirmazeile2() != null) {
					sKunde = sKunde + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".");
				}
				jcrDocDto.setsBelegnummer(sKunde);
				jcrDocDto.setlPartner(partnerDto.getIId());
				jcrDocDto.setsTable("KUNDE");

				kundeDto.setPartnerDto(partnerDto);
				jcrDocDto.setDocPath(
						new DocPath(new DocNodeKunde(kundeDto, partnerDto)).add(new DocNodeFile(sDateiname)));
				// jcrDocDto.setsFullNodePath(JCRDocFac.HELIUMV_NODE + "/"
				// + LocaleFac.BELEGART_KUNDE.trim() + "/"
				// + LocaleFac.BELEGART_KUNDE.trim() + "/" + sKunde.trim()
				// + "/" + sDateiname);
				bCopied = true;
			}
			if (alteDokumente[i].getBelegartCNr().equals(LocaleFac.BELEGART_LIEFERANT)) {
				LieferantDto lieferantDto = null;
				PartnerDto partnerDto = null;
				lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(alteDokumente[i].getIBelegartid(),
						theClientDto);
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto);
				String sLieferant = partnerDto.getCName1nachnamefirmazeile1().replace("/", ".");
				if (partnerDto.getCName2vornamefirmazeile2() != null) {
					sLieferant = sLieferant + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".");
				}
				lieferantDto.setPartnerDto(partnerDto);
				jcrDocDto.setDocPath(
						new DocPath(new DocNodeLieferant(lieferantDto, partnerDto)).add(new DocNodeFile(sDateiname)));
				// jcrDocDto.setsFullNodePath(JCRDocFac.HELIUMV_NODE + "/"
				// + LocaleFac.BELEGART_LIEFERANT.trim() + "/"
				// + LocaleFac.BELEGART_LIEFERANT.trim() + "/"
				// + sLieferant.trim() + "/" + sDateiname);
				jcrDocDto.setsBelegnummer(sLieferant);
				jcrDocDto.setlPartner(partnerDto.getIId());
				jcrDocDto.setsTable("LIEFERANT");
				bCopied = true;
			}
			// Anfrage
			if (alteDokumente[i].getBelegartCNr().equals(LocaleFac.BELEGART_ANFRAGE)) {
				AnfrageDto anfDto = null;
				PartnerDto partnerDto = null;
				LieferantDto lieferantDto = null;
				anfDto = getAnfrageFac().anfrageFindByPrimaryKey(alteDokumente[i].getIBelegartid(), theClientDto);
				lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(anfDto.getLieferantIIdAnfrageadresse(),
						theClientDto);
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto);
				jcrDocDto.setsBelegnummer(anfDto.getCNr());
				jcrDocDto.setlPartner(partnerDto.getIId());
				jcrDocDto.setsTable("ANFRAGE");

				jcrDocDto.setDocPath(new DocPath(new DocNodeAnfrage(anfDto)).add(new DocNodeFile(sDateiname)));
				// jcrDocDto.setsFullNodePath(JCRDocFac.HELIUMV_NODE + "/"
				// + LocaleFac.BELEGART_ANFRAGE.trim() + "/"
				// + LocaleFac.BELEGART_ANFRAGE.trim() + "/"
				// + anfDto.getCNr().replace("/", ".") + "/" + sDateiname);
				bCopied = true;
			}
			// Angebot
			if (alteDokumente[i].getBelegartCNr().equals(LocaleFac.BELEGART_ANGEBOT)) {
				AngebotDto angDto = null;
				PartnerDto partnerDto = null;
				KundeDto kundeDto = null;
				angDto = getAngebotFac().angebotFindByPrimaryKey(alteDokumente[i].getIBelegartid(), theClientDto);
				kundeDto = getKundeFac().kundeFindByPrimaryKey(angDto.getKundeIIdAngebotsadresse(), theClientDto);
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
				jcrDocDto.setsBelegnummer(angDto.getCNr());
				jcrDocDto.setlPartner(partnerDto.getIId());
				jcrDocDto.setsTable("ANGEBOT");

				jcrDocDto.setDocPath(new DocPath(new DocNodeAngebot(angDto)).add(new DocNodeFile(sDateiname)));

				// jcrDocDto.setsFullNodePath(JCRDocFac.HELIUMV_NODE + "/"
				// + LocaleFac.BELEGART_ANGEBOT.trim() + "/"
				// + LocaleFac.BELEGART_ANGEBOT.trim() + "/"
				// + angDto.getCNr().replace("/", ".") + "/" + sDateiname);
				bCopied = true;
			}
			// Auftrag
			if (alteDokumente[i].getBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {
				AuftragDto aufDto = null;
				PartnerDto partnerDto = null;
				KundeDto kundeDto = null;
				aufDto = getAuftragFac().auftragFindByPrimaryKey(alteDokumente[i].getIBelegartid());
				kundeDto = getKundeFac().kundeFindByPrimaryKey(aufDto.getKundeIIdAuftragsadresse(), theClientDto);
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
				jcrDocDto.setsBelegnummer(aufDto.getCNr());
				jcrDocDto.setlPartner(partnerDto.getIId());
				jcrDocDto.setsTable("AUFTRAG");
				jcrDocDto.setDocPath(new DocPath(new DocNodeAuftrag(aufDto)).add(new DocNodeFile(sDateiname)));
				// jcrDocDto.setsFullNodePath(JCRDocFac.HELIUMV_NODE + "/"
				// + LocaleFac.BELEGART_AUFTRAG.trim() + "/"
				// + LocaleFac.BELEGART_AUFTRAG.trim() + "/"
				// + aufDto.getCNr().replace("/", ".") + "/" + sDateiname);
				bCopied = true;
			}
			// Bestellung
			if (alteDokumente[i].getBelegartCNr().equals(LocaleFac.BELEGART_BESTELLUNG)) {
				BestellungDto besDto = null;
				PartnerDto partnerDto = null;
				LieferantDto lieferantDto = null;
				besDto = getBestellungFac().bestellungFindByPrimaryKey(alteDokumente[i].getIBelegartid());
				lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(besDto.getLieferantIIdBestelladresse(),
						theClientDto);
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto);
				jcrDocDto.setsBelegnummer(besDto.getCNr());
				jcrDocDto.setlPartner(partnerDto.getIId());
				jcrDocDto.setsTable("BESTELLUNG");
				jcrDocDto.setDocPath(new DocPath(new DocNodeBestellung(besDto)).add(new DocNodeFile(sDateiname)));
				// jcrDocDto.setsFullNodePath(JCRDocFac.HELIUMV_NODE + "/"
				// + LocaleFac.BELEGART_BESTELLUNG.trim() + "/"
				// + LocaleFac.BELEGART_BESTELLUNG.trim() + "/"
				// + besDto.getCNr().replace("/", ".") + "/" + sDateiname);
				bCopied = true;
			}
			// Lieferschein
			if (alteDokumente[i].getBelegartCNr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
				LieferscheinDto lschDto = null;
				PartnerDto partnerDto = null;
				KundeDto kundeDto = null;
				lschDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(alteDokumente[i].getIBelegartid(),
						theClientDto);
				kundeDto = getKundeFac().kundeFindByPrimaryKey(lschDto.getKundeIIdLieferadresse(), theClientDto);
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
				jcrDocDto.setsBelegnummer(lschDto.getCNr());
				jcrDocDto.setlPartner(partnerDto.getIId());
				jcrDocDto.setsTable("LIEFERSCHEIN");

				jcrDocDto.setDocPath(new DocPath(new DocNodeLieferschein(lschDto)).add(new DocNodeFile(sDateiname)));
				// jcrDocDto
				// .setsFullNodePath(JCRDocFac.HELIUMV_NODE + "/"
				// + LocaleFac.BELEGART_LIEFERSCHEIN.trim() + "/"
				// + LocaleFac.BELEGART_LIEFERSCHEIN.trim() + "/"
				// + lschDto.getCNr().replace("/", ".") + "/"
				// + sDateiname);
				bCopied = true;
			}
			// Los
			if (alteDokumente[i].getBelegartCNr().equals(LocaleFac.BELEGART_LOS)) {
				LosDto losDto = null;
				PartnerDto partnerDto = null;
				KundeDto kundeDto = null;
				losDto = getFertigungFac().losFindByPrimaryKey(alteDokumente[i].getIBelegartid());
				if (losDto.getAuftragIId() != null) {
					AuftragDto aufDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
					kundeDto = getKundeFac().kundeFindByPrimaryKey(aufDto.getKundeIIdAuftragsadresse(), theClientDto);
					partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
				} else {
					MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
					partnerDto = getPartnerFac().partnerFindByPrimaryKey(mDto.getPartnerIId(), theClientDto);
				}
				jcrDocDto.setsBelegnummer(losDto.getCNr());
				jcrDocDto.setlPartner(partnerDto.getIId());
				jcrDocDto.setsTable("LOS");
				jcrDocDto.setDocPath(new DocPath(new DocNodeLos(losDto)).add(new DocNodeFile(sDateiname)));
				// jcrDocDto.setsFullNodePath(JCRDocFac.HELIUMV_NODE + "/"
				// + LocaleFac.BELEGART_LOS.trim() + "/"
				// + LocaleFac.BELEGART_LOS.trim() + "/"
				// + losDto.getCNr().replace("/", ".") + "/" + sDateiname);
				bCopied = true;
			}

			addNewDocumentOrNewVersionOfDocument(jcrDocDto, theClientDto);
			if (bCopied) {
				for (int y = 0; y < dokumentschlagwortDto.length; y++) {
					getDokumenteFac().removeDokumentschlagwort(dokumentschlagwortDto[y]);
				}
				getDokumenteFac().removeBelegartdokument(alteDokumente[i]);
				getDokumenteFac().removeDokument(dokumentDto);
			}
		}
	}

	public VersandauftragDto getDataForVersandauftragFromJCR(VersandauftragDto versandauftragDto,
			TheClientDto theClientDto) {
		PartnerDto partnerDto = null;
		PersonalDto personalDto = null;
		try {
			personalDto = getPersonalFac().personalFindByPrimaryKey(versandauftragDto.getPersonalIId(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);
			personalDto.setPartnerDto(partnerDto);
		} catch (Exception e) {
		}

		String sPartner = partnerDto.getCName1nachnamefirmazeile1().replace("/", ".").replace("'", " ");
		if (partnerDto.getCName2vornamefirmazeile2() != null) {
			sPartner = sPartner + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".").replace("'", " ");
		}
		String sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/" + "Versandauftrag/" + sPartner.trim()
				+ "/" + versandauftragDto.getIId();
		JCRDocDto jcrDocDto = null;

		try {
			jcrDocDto = new JCRDocDto(getNode(sPath + "/" + "Versandauftrag"), true);
			versandauftragDto.setOInhalt(jcrDocDto.getbData());

			closeSession();
			return versandauftragDto;
		} catch (Exception e) {
		}
		sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/" + "Versandauftrag/"
				+ new DocNodeLiteral(sPartner.trim()).asEncodedPath() + "/" + versandauftragDto.getIId();
		jcrDocDto = null;

		try {
			jcrDocDto = new JCRDocDto(getNode(sPath + "/" + "Versandauftrag"), true);
			versandauftragDto.setOInhalt(jcrDocDto.getbData());

			closeSession();
			return versandauftragDto;
		} catch (Exception e) {
		}
		sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/" + "Versandauftrag/"
				+ sPartner.trim().replace(" ", "_") + "/" + versandauftragDto.getIId();
		try {
			jcrDocDto = new JCRDocDto(getNode(sPath + "/Versandauftrag"), true);
			versandauftragDto.setOInhalt(jcrDocDto.getbData());

			closeSession();
			return versandauftragDto;
		} catch (Exception e1) {
		}
		// Knoten nicht gefunden evtl mit vorhergenhendem Bug kopiert
		// worden
		// => Mit anderem Pfad nochmal suchen
		try {
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getIId(), theClientDto);
			sPartner = partnerDto.getCName1nachnamefirmazeile1().replace("/", ".");
			if (partnerDto.getCName2vornamefirmazeile2() != null) {
				sPartner = sPartner + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".");
			}
			sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/" + "Versandauftrag/" + sPartner.trim()
					+ "/" + versandauftragDto.getIId();
			jcrDocDto = new JCRDocDto(getNode(sPath + "/" + "Versandauftrag"), true);
		} catch (Exception e2) {
			// Den Knoten gibt es wirklich nicht
		}
		if (jcrDocDto != null) {
			versandauftragDto.setOInhalt(jcrDocDto.getbData());
		}
		closeSession();
		return versandauftragDto;
	}

	public VersandanhangDto getDataForVersandanhangFromJCR(VersandanhangDto versandanhangDto,
			TheClientDto theClientDto) {
		VersandauftragDto versandauftragDto = null;
		PartnerDto partnerDto = null;
		PersonalDto personalDto = null;
		try {
			versandauftragDto = getVersandFac().versandauftragFindByPrimaryKey(versandanhangDto.getVersandauftragIId());
			personalDto = getPersonalFac().personalFindByPrimaryKey(versandauftragDto.getPersonalIId(), theClientDto);
//			partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);
//			personalDto.setPartnerDto(partnerDto);
			partnerDto = personalDto.getPartnerDto();

		} catch (Exception e) {
		}
		String sPartner = partnerDto.getCName1nachnamefirmazeile1().replace("/", ".").replace("'", " ");
		if (partnerDto.getCName2vornamefirmazeile2() != null) {
			sPartner = sPartner + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".").replace("'", " ");
		}
		String sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/" + "Versandauftrag/" + sPartner.trim()
				+ "/" + versandauftragDto.getIId();
		JCRDocDto jcrDocDto = null;
		JcrSession jcrSession = new JcrSession();
		try {
			jcrDocDto = new JCRDocDto(jcrSession.getNode(sPath + "/Anhang" + versandanhangDto.getIId()), true);

			versandanhangDto.setOInhalt(jcrDocDto.getbData());
			jcrSession.closeSession();
			return versandanhangDto;
		} catch (Exception e) {
		}

		sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/" + "Versandauftrag/"
				+ new DocNodeLiteral(sPartner.trim()).asEncodedPath() + "/" + versandauftragDto.getIId();
		try {
			jcrDocDto = new JCRDocDto(jcrSession.getNode(sPath + "/Anhang" + versandanhangDto.getIId()), true);

			versandanhangDto.setOInhalt(jcrDocDto.getbData());
			jcrSession.closeSession();
			return versandanhangDto;
		} catch (Exception e) {
		}

		// Knoten nicht gefunden evtl mit vorhergenhendem Bug kopiert worden
		// => Mit anderem Pfad nochmal suchen
		sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/" + "Versandauftrag/"
				+ sPartner.trim().replace(" ", "_") + "/" + versandauftragDto.getIId();
		try {
			jcrDocDto = new JCRDocDto(getNode(sPath + "/Anhang" + versandanhangDto.getIId()), true);
			versandanhangDto.setOInhalt(jcrDocDto.getbData());
			jcrSession.closeSession();
			return versandanhangDto;
		} catch (Exception e1) {
		}

		try {
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getIId(), theClientDto);
			sPartner = partnerDto.getCName1nachnamefirmazeile1().replace("/", ".");
			if (partnerDto.getCName2vornamefirmazeile2() != null) {
				sPartner = sPartner + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".");
			}
			sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/" + "Versandauftrag/" + sPartner.trim()
					+ "/" + versandauftragDto.getIId();
			jcrDocDto = new JCRDocDto(getNode(sPath + "/Anhang" + versandanhangDto.getIId()), true);
			versandanhangDto.setOInhalt(jcrDocDto.getbData());

			jcrSession.closeSession();
			return versandanhangDto;
		} catch (Exception e2) {
		}

		jcrSession.closeSession();
		return versandanhangDto;
	}

	@Override
	// Es ist wichtig, dass mehrere Anhaenge in der gleichen JCR Session behandelt
	// werden
	public void saveVersandanhangsAsDocument(List<VersandanhangDto> dtos, boolean clearContent,
			TheClientDto theClientDto) throws RemoteException {
		myLogger.warn("saveVersandanhangsAsDocument beginn");
		assertClosed("saveVersandanhangsAsDocument");

		JcrSession jcrSession = new JcrSession();
		for (VersandanhangDto dto : dtos) {
			saveVersandanhangAsDocument(jcrSession, dto, clearContent, theClientDto);
		}
		myLogger.warn("saveVersandanhangsAsDocument ende");
	}

	public void saveVersandanhangAsDocument(JcrSession jcrSession, VersandanhangDto versandanhangDto,
			boolean setOInhaltNull, TheClientDto theClientDto) throws RemoteException {
		myLogger.warn("saveVersandanhangAsDocument beginn");
		assertClosed("saveVersandanhangAsDocument");

		VersandauftragDto versandauftrag = getVersandFac()
				.versandauftragFindByPrimaryKey(versandanhangDto.getVersandauftragIId());

		JCRDocDto jcrDocDto = createJCRDocDtoFrom(versandauftrag, versandanhangDto, theClientDto);
		addNewDocumentOrNewVersionOfDocumentSession(jcrSession, jcrDocDto, theClientDto);
		/*
		 * PersonalDto personalDto =
		 * getPersonalFac().personalFindByPrimaryKey(versandauftrag.getPersonalIId(),
		 * theClientDto); PartnerDto partnerDto =
		 * getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(),
		 * theClientDto); String sPartner =
		 * partnerDto.getCName1nachnamefirmazeile1().replace("/", ".").replace("'",
		 * " "); if (partnerDto.getCName2vornamefirmazeile2() != null) { sPartner =
		 * sPartner + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".");
		 * } String sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/"
		 * + "Versandauftrag/" + sPartner.trim() + "/" + versandauftrag.getIId();
		 * JCRDocDto jcrDocDto = new JCRDocDto();
		 * jcrDocDto.setbData(versandanhangDto.getOInhalt());
		 * jcrDocDto.setbVersteckt(false); jcrDocDto.setlAnleger(partnerDto.getIId());
		 * if (versandauftrag.getPartnerIIdEmpfaenger() != null) {
		 * jcrDocDto.setlPartner(versandauftrag.getPartnerIIdEmpfaenger()); } else {
		 * MandantDto mandantDto = null; try { mandantDto =
		 * getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
		 * theClientDto); } catch (Exception e) { }
		 * jcrDocDto.setlPartner(mandantDto.getPartnerIId()); }
		 * jcrDocDto.setlSicherheitsstufe(SECURITY_ARCHIV); jcrDocDto.setlVersion(0);
		 * jcrDocDto.setlZeitpunkt(versandauftrag.getTAnlegen().getTime());
		 * jcrDocDto.setsBelegart(DEFAULT_VERSANDAUFTRAG_BELEGART);
		 * jcrDocDto.setsBelegnummer(versandanhangDto.getCDateiname());
		 * jcrDocDto.setsFilename(versandanhangDto.getCDateiname());
		 * 
		 * // jcrDocDto.setDocPath(new DocPath(new //
		 * DocNodeVersandanhang(versandanhangDto, personalDto)).add(new //
		 * DocNodeFile("Anhang"))); // so wars: 12.07.2013 jcrDocDto.setDocPath(new
		 * DocPath(sPath + // "/Anhang" + versandanhangDto.getIId()));
		 * jcrDocDto.setDocPath(new DocPath(sPath).add(new DocNodeFile("Anhang" +
		 * versandanhangDto.getIId())));
		 * 
		 * jcrDocDto.setsGruppierung(DEFAULT_VERSANDAUFTRAG_GRUPPE);
		 * jcrDocDto.setsMIME(Helper.getMime(versandanhangDto.getCDateiname()));
		 * jcrDocDto.setsName("Anhang" + versandanhangDto.getIId());
		 * jcrDocDto.setsRow(versandanhangDto.getIId().toString());
		 * jcrDocDto.setsTable("VERSANDANHANG"); String sSchlagworte = "Versandanhang ";
		 * jcrDocDto.setsSchlagworte(sSchlagworte);
		 * addNewDocumentOrNewVersionOfDocumentSession(jcrSession, jcrDocDto,
		 * theClientDto);
		 */
		if (setOInhaltNull) {
			versandanhangDto.setOInhalt(null);
			getVersandFac().updateVersandanhang(versandanhangDto);
		}
		myLogger.warn("saveVersandanhangAsDocument ende");
	}

	public void saveVersandanhangAsDocument(VersandanhangDto versandanhangDto, boolean setOInhaltNull,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		saveVersandanhangAsDocument(null, versandanhangDto, setOInhaltNull, theClientDto);
		/*
		 * myLogger.warn("saveVersandanhangAsDocument beginn");
		 * assertClosed("saveVersandanhangAsDocument");
		 * 
		 * VersandauftragDto versandauftrag = getVersandFac()
		 * .versandauftragFindByPrimaryKey(versandanhangDto.getVersandauftragIId());
		 * PersonalDto personalDto =
		 * getPersonalFac().personalFindByPrimaryKey(versandauftrag.getPersonalIId(),
		 * theClientDto); PartnerDto partnerDto =
		 * getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(),
		 * theClientDto); String sPartner =
		 * partnerDto.getCName1nachnamefirmazeile1().replace("/", ".").replace("'",
		 * " "); if (partnerDto.getCName2vornamefirmazeile2() != null) { sPartner =
		 * sPartner + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".");
		 * } String sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/"
		 * + "Versandauftrag/" + sPartner.trim() + "/" + versandauftrag.getIId();
		 * JCRDocDto jcrDocDto = new JCRDocDto();
		 * jcrDocDto.setbData(versandanhangDto.getOInhalt());
		 * jcrDocDto.setbVersteckt(false); jcrDocDto.setlAnleger(partnerDto.getIId());
		 * if (versandauftrag.getPartnerIIdEmpfaenger() != null) {
		 * jcrDocDto.setlPartner(versandauftrag.getPartnerIIdEmpfaenger()); } else {
		 * MandantDto mandantDto = null; try { mandantDto =
		 * getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
		 * theClientDto); } catch (Exception e) { }
		 * jcrDocDto.setlPartner(mandantDto.getPartnerIId()); }
		 * jcrDocDto.setlSicherheitsstufe(SECURITY_ARCHIV); jcrDocDto.setlVersion(0);
		 * jcrDocDto.setlZeitpunkt(versandauftrag.getTAnlegen().getTime());
		 * jcrDocDto.setsBelegart(DEFAULT_VERSANDAUFTRAG_BELEGART);
		 * jcrDocDto.setsBelegnummer(versandanhangDto.getCDateiname());
		 * jcrDocDto.setsFilename(versandanhangDto.getCDateiname());
		 * 
		 * // jcrDocDto.setDocPath(new DocPath(new //
		 * DocNodeVersandanhang(versandanhangDto, personalDto)).add(new //
		 * DocNodeFile("Anhang"))); // so wars: 12.07.2013 jcrDocDto.setDocPath(new
		 * DocPath(sPath + // "/Anhang" + versandanhangDto.getIId()));
		 * jcrDocDto.setDocPath(new DocPath(sPath).add(new DocNodeFile("Anhang" +
		 * versandanhangDto.getIId())));
		 * 
		 * jcrDocDto.setsGruppierung(DEFAULT_VERSANDAUFTRAG_GRUPPE);
		 * jcrDocDto.setsMIME(Helper.getMime(versandanhangDto.getCDateiname()));
		 * jcrDocDto.setsName("Anhang" + versandanhangDto.getIId());
		 * jcrDocDto.setsRow(versandanhangDto.getIId().toString());
		 * jcrDocDto.setsTable("VERSANDANHANG"); String sSchlagworte = "Versandanhang ";
		 * jcrDocDto.setsSchlagworte(sSchlagworte);
		 * addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto,
		 * theClientDto); if (setOInhaltNull) { versandanhangDto.setOInhalt(null);
		 * getVersandFac().updateVersandanhang(versandanhangDto); }
		 * myLogger.warn("saveVersandanhangAsDocument ende");
		 */
	}

	public void saveVersandauftragAsDocument(VersandauftragDto versandauftrag, boolean setOInhaltNull,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		myLogger.warn("saveVersandauftragAsDocument beginn");
		assertClosed("saveVersandauftragAsDocument");

		if (VersandFac.STATUS_ERLEDIGT.equals(versandauftrag.getStatusCNr()) || !setOInhaltNull) {
			saveVersandauftragAsDocumentImpl(versandauftrag, setOInhaltNull, theClientDto);
		}

		myLogger.warn("saveVersandauftragAsDocument ende");
	}

	@Override
	public void saveVersandauftragsAsDocument(List<VersandauftragDto> versandauftraege,
			boolean setOInhaltNull, TheClientDto theClientDto) throws RemoteException {
		myLogger.warn("saveVersandauftragAsDocument beginn");
		assertClosed("saveVersandauftragAsDocument");

		JcrSession jcrSession = new JcrSession();
		for (VersandauftragDto versandauftragDto : versandauftraege) {
			if (VersandFac.STATUS_ERLEDIGT.equals(versandauftragDto.getStatusCNr()) || !setOInhaltNull) {
				saveVersandauftragInSessionAsDocumentImpl(
						jcrSession, versandauftragDto, setOInhaltNull, theClientDto);
			}
		}
		jcrSession.closeSession();
	}
	

	private void saveVersandauftragAsDocumentImpl(VersandauftragDto versandauftrag, boolean setOInhaltNull,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		JcrSession jcrSession = new JcrSession();
		saveVersandauftragInSessionAsDocumentImpl(jcrSession, versandauftrag, setOInhaltNull, theClientDto);
		jcrSession.closeSession();		
	}

	
	private void saveVersandauftragInSessionAsDocumentImpl(JcrSession theirJcrSession, VersandauftragDto versandauftrag,
			boolean setOInhaltNull, TheClientDto theClientDto) throws RemoteException {
		VersandanhangDto[] anhangDtos = getVersandFac()
				.VersandanhangFindByVersandauftragIID(versandauftrag.getIId());
		if (anhangDtos == null) {
			myLogger.info("got null anhangDtos, don't saving anything");
			return;			
		}

		myLogger.info("having anhangDto with " + anhangDtos.length + " length. Saving anhaenge...");

		JcrSession jcrSession = theirJcrSession; 
		if(jcrSession == null) {
			jcrSession = new JcrSession();
		}

		String lockMsg = "VersandauftragId:" + versandauftrag.getIId() + " ";
		try {
			lockJcr(lockMsg);
			
			for (VersandanhangDto versandanhangDto : anhangDtos) {
				saveVersandanhangAsDocument(jcrSession, versandanhangDto, setOInhaltNull, theClientDto);
			}

			myLogger.info("done saving " + anhangDtos.length + " anhaenge");

			JCRDocDto jcrDocDto = createJCRDocDtoFrom(versandauftrag, null, theClientDto);
			addNewDocumentOrNewVersionOfDocumentSession(jcrSession, jcrDocDto, theClientDto);
			if (theirJcrSession == null) {
				jcrSession.closeSession();				
			}
		} finally {
			unlockJcr(lockMsg);
		}

		if (setOInhaltNull) {
			versandauftrag.setOInhalt(null);
			getVersandFac().updateVersandauftrag(versandauftrag, theClientDto);
		}
	}
	
	private String buildPathFor(PartnerDto partnerDto) {
		String sPartner = partnerDto.getCName1nachnamefirmazeile1().replace("/", ".").replace("'", " ");
		if (partnerDto.getCName2vornamefirmazeile2() != null) {
			sPartner = sPartner + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".").replace("'", " ");
		}

		return sPartner.trim();
	}

	private String buildPathFor(VersandauftragDto versandauftragDto, PartnerDto partnerDto) {
		String sPartner = buildPathFor(partnerDto);
		String sPath = HELIUMV_NODE + "/" + LocaleFac.BELEGART_SYSTEM.trim() + "/" + "Versandauftrag/" + sPartner + "/"
				+ versandauftragDto.getIId();
		return sPath;
	}

	private String buildBelegkennung(VersandauftragDto versandauftragDto, TheClientDto theClientDto)
			throws RemoteException {
		if (versandauftragDto.getBelegartCNr() == null)
			return "";
		if (versandauftragDto.getIIdBeleg() == null)
			return "";

		BelegKennungObj bkObj = new BelegKennungObj().create(versandauftragDto.getBelegartCNr(),
				versandauftragDto.getIIdBeleg(), theClientDto);
		return bkObj.kennung();
	}

	private JCRDocDto createJCRDocDtoFrom(VersandauftragDto versandauftragDto, VersandanhangDto anhangDto,
			TheClientDto theClientDto) throws RemoteException {
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(versandauftragDto.getPersonalIId(),
				theClientDto);

		JCRDocDto jcrDocDto = new JCRDocDto();
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(personalDto.getPartnerDto().getIId());
		if (versandauftragDto.getPartnerIIdEmpfaenger() != null) {
			jcrDocDto.setlPartner(versandauftragDto.getPartnerIIdEmpfaenger());
		} else {
			try {
				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);
				jcrDocDto.setlPartner(mandantDto.getPartnerIId());
			} catch (Exception e) {
			}
		}

		jcrDocDto.setlVersion(0);
		jcrDocDto.setlZeitpunkt(versandauftragDto.getTAnlegen().getTime());
		jcrDocDto.setsBelegart(DEFAULT_VERSANDAUFTRAG_BELEGART);
		jcrDocDto.setsGruppierung(DEFAULT_VERSANDAUFTRAG_GRUPPE);

		String sPath = buildPathFor(versandauftragDto, personalDto.getPartnerDto());
		DocPath docPath = new DocPath(sPath);

		if (anhangDto == null) {
			jcrDocDto.setlSicherheitsstufe(SECURITY_ARCHIV);
			jcrDocDto.setDocPath(docPath.add(new DocNodeFile("Versandauftrag")));
			jcrDocDto.setbData(versandauftragDto.getOInhalt());
			jcrDocDto.setsBelegnummer("Beleganhang");
			jcrDocDto.setsFilename("Beleganhang.pdf");
			jcrDocDto.setsMIME(".pdf");
			jcrDocDto.setsName("Versandauftrag");
			jcrDocDto.setsRow(versandauftragDto.getIId().toString());
			jcrDocDto.setsTable("VERSANDAUFTRAG");
			jcrDocDto.setsSchlagworte("Versandauftrag " + buildBelegkennung(versandauftragDto, theClientDto));
		} else {
			jcrDocDto.setlSicherheitsstufe(SECURITY_ARCHIV);
			jcrDocDto.setDocPath(docPath.add(new DocNodeFile("Anhang" + anhangDto.getIId())));
			jcrDocDto.setbData(anhangDto.getOInhalt());
			jcrDocDto.setsBelegnummer(anhangDto.getCDateiname());
			jcrDocDto.setsFilename(anhangDto.getCDateiname());
			jcrDocDto.setsMIME(Helper.getMime(anhangDto.getCDateiname()));
			jcrDocDto.setsName("Anhang" + anhangDto.getIId());
			jcrDocDto.setsRow(anhangDto.getIId().toString());
			jcrDocDto.setsTable("VERSANDANHANG");
			jcrDocDto.setsSchlagworte("Versandanhang " + buildBelegkennung(versandauftragDto, theClientDto));
		}

		return jcrDocDto;
	}


	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void kopiereVersandauftraegeInJCR(TheClientDto theClient) throws EJBExceptionLP, RemoteException {
		VersandauftragDto versandauftrag = getVersandFac().versandauftragFindNextNotInDoc();

		int counter = 0;
		while (versandauftrag != null) {

			counter++;
			saveVersandauftragAsDocument(versandauftrag, true, theClient);
			System.out.println("Saved Versandauftrag number " + counter + " with ID " + versandauftrag.getIId());
			versandauftrag = getVersandFac().versandauftragFindNextNotInDoc();

		}
	}

	public int removeAllEmptyNodes() {
		return removeAllEmptyNodes(HELIUMV_NODE);
	}

	public int removeAllEmptyNodes(String path) {
		Node node = getNode(path);
		if (null != node) {
			return removeEmptyNodes(node);
		}
		return 0;
	}

	public void removeNodeAndAllChildlessParents(String path) {
		removeNodeAndAllChildlessParents(getNode(path));
	}

	private void removeNodeAndAllChildlessParents(Node node) {
		if (node == null)
			return;
		Node myParent;
		try {
			myParent = node.getParent();

			if (node.getNodes().getSize() != 0)
				return;
			myLogger.warn("L\u00F6sche Pfad: " + node.getPath());
			node.remove();
			removeNodeAndAllChildlessParents(myParent);
		} catch (Exception e) {
			myLogger.error("Fehler beim l\u00F6schen leerer Pfade.", e);
		}
	}

	private int removeEmptyNodes(Node node) {
		int count = 0;
		try {
			NodeIterator iter = node.getNodes();
			while (iter.hasNext()) {
				count += removeEmptyNodes(iter.nextNode());
			}
			iter = node.getNodes();
			if (iter.getSize() == 0) {
				try {
					Property p = node.getProperty(JCRDocFac.PROPERTY_DATA);
					if (p == null) {
						myLogger.warn("L\u00F6sche Pfad (Property " + JCRDocFac.PROPERTY_DATA + " nicht gefunden): "
								+ node.getPath());
						node.remove();
						getSession().save();
						count++;
					}
				} catch (PathNotFoundException ex) {
					myLogger.warn("L\u00F6sche Pfad (Property " + JCRDocFac.PROPERTY_DATA + " nicht gefunden): "
							+ node.getPath());
					node.remove();
					getSession().save();
					count++;
				}
			}
		} catch (RepositoryException e) {
			myLogger.warn("RepositoryException", e);
		}
		return count;
	}

	public void removeDokumentgruppierung(DokumentgruppierungDto dokumentgruppierungDto) {
		Dokumentgruppierung dokumentgruppierung = em.find(Dokumentgruppierung.class,
				dokumentgruppierungDto.getDokumentgruppierungPK());
		em.remove(dokumentgruppierung);
		em.flush();
	}

	private List<DocNodeBase> getAllDocumentsForKunde(JcrSession jcrSession, Integer kundeIId, String mandant)
			throws EJBExceptionLP, RemoteException {
		List<DocNodeBase> nodes = new ArrayList<DocNodeBase>();
		for (AngebotDto angebot : getAngebotFac().angebotFindByKundeIIdAngebotsadresseMandantCNr(kundeIId, mandant,
				null)) {
			nodes.add(new DocNodeAngebot(angebot));
		}
		for (AuftragDto auftrag : getAuftragFac().auftragFindByKundeIIdAuftragsadresseMandantCNrOhneExc(kundeIId,
				mandant, null)) {
			nodes.add(new DocNodeAuftrag(auftrag));
// SP7388 Beschluss: Lose waren so nie gewollt (wh:12.04.2019, ghp)			
//			for (LosDto los : getFertigungFac().losFindByAuftragIId(auftrag.getIId())) {
//				nodes.add(new DocNodeLos(los));
//			}
		}
		for (InseratDto inserat : getInseratFac().inseratFindByKundeIId(kundeIId)) {
			nodes.add(new DocNodeInserat(inserat));
		}
		for (LieferscheinDto lieferschein : getLieferscheinFac()
				.lieferscheinFindByKundeIIdLieferadresseMandantCNrOhneExc(kundeIId, mandant, null)) {
			nodes.add(new DocNodeLieferschein(lieferschein));
		}
		for (RechnungDto rechnung : getRechnungFac().rechnungFindByKundeIIdMandantCNrOhneExc(kundeIId, mandant)) {
			nodes.add(new DocNodeRechnung(rechnung));
		}
		for (ReklamationDto reklamation : getReklamationFac().reklamationFindByKundeIIdMandantCNr(kundeIId, mandant)) {
			nodes.add(new DocNodeReklamation(reklamation));
		}

		return createSymbolicLinkForPartnerDocs(jcrSession, nodes);
	}

	private List<DocNodeBase> createSymbolicLinkForPartnerDocs(JcrSession jcrSession, List<DocNodeBase> nodes) {
		List<DocNodeBase> existingNodes = new ArrayList<DocNodeBase>();
		for (DocNodeBase node : nodes) {
			DocPath tempPath = new DocPath(node);
			if (checkIfNodeExistsWithinTransaction(jcrSession, tempPath.getPathAsString())) {
				DocPath viewPath = tempPath.getDeepCopy();
				viewPath.asDocNodeList().remove(0); // HELIUMV und Mandant
				viewPath.asDocNodeList().remove(0); // entfernen
				existingNodes.add(new DocNodeSymbolicLink(tempPath, viewPath));
			}
		}
		return existingNodes;
	}

	private List<DocNodeBase> getAllDocumentsForLieferant(JcrSession jcrSession, Integer lieferantIId, String mandant)
			throws EJBExceptionLP, RemoteException {
		List<DocNodeBase> nodes = new ArrayList<DocNodeBase>();
		for (AnfrageDto anfrage : getAnfrageFac().anfrageFindByLieferantIIdAnfrageadresseMandantCNrOhneExc(lieferantIId,
				mandant, null)) {
			nodes.add(new DocNodeAnfrage(anfrage));
		}
		for (BestellungDto bestellung : getBestellungFac()
				.bestellungFindByLieferantIIdRechnungsadresseMandantCNrOhneExc(lieferantIId, mandant)) {
			nodes.add(new DocNodeBestellung(bestellung));
		}
		for (EingangsrechnungDto eingangsrechnung : getEingangsrechnungFac()
				.eingangsrechnungFindByMandantLieferantIIdOhneExc(mandant, lieferantIId)) {
			nodes.add(new DocNodeEingangsrechnung(eingangsrechnung));
		}
		for (InseratDto inserat : getInseratFac().inseratFindByLieferantIId(lieferantIId)) {
			nodes.add(new DocNodeInserat(inserat));
		}
		for (ReklamationDto reklamation : getReklamationFac().reklamationFindByLieferantIIdMandantCNr(lieferantIId,
				mandant)) {
			nodes.add(new DocNodeReklamation(reklamation));
		}

		return createSymbolicLinkForPartnerDocs(jcrSession, nodes);
	}

	private List<DocNodeBase> getAllDocumentsForPartner(JcrSession jcrSession, Integer partnerIId, String mandant)
			throws RemoteException, EJBExceptionLP {
		List<DocNodeBase> partnerNodes = new ArrayList<DocNodeBase>();
		for (ProjektDto projekt : getProjektFac().projektFindByPartnerIIdMandantCNrOhneExc(partnerIId, mandant)) {
			BereichDto bereich = getProjektServiceFac().bereichFindByPrimaryKey(projekt.getBereichIId());
			partnerNodes.add(new DocNodeProjekt(projekt, bereich));
		}

		List<DocNodeBase> nodes = new ArrayList<DocNodeBase>();

		nodes.addAll(createSymbolicLinkForPartnerDocs(jcrSession, partnerNodes));

		KundeDto kunde = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerIId, mandant, theClientDto);
		if (kunde != null)
			nodes.addAll(getAllDocumentsForKunde(jcrSession, kunde.getIId(), mandant));
		LieferantDto lieferant = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(partnerIId, mandant,
				theClientDto);
		if (lieferant != null)
			nodes.addAll(getAllDocumentsForLieferant(jcrSession, lieferant.getIId(), mandant));
		return nodes;
	}

	@Override
	public ArrayList<JCRDocDto> getJCRDocDtoFromNodeChildren(DocPath docPath) throws RepositoryException, IOException {
		ArrayList<JCRDocDto> jcrDocs = new ArrayList<JCRDocDto>();
		JcrSession jcrSession = new JcrSession();
		try {
			List<DocNodeBase> children = getDocNodeChildrenFromNode(jcrSession, docPath, null);
			if (null == children)
				return jcrDocs;

			for (DocNodeBase child : children) {
				jcrDocs.add(getJCRDocDtoFromNode(jcrSession, docPath.getDeepCopy().add(child)));
			}

			return jcrDocs;
		} finally {
			jcrSession.closeSession();
		}
	}

	public boolean isOnline() {
		try {
			createRepo();
		} catch (NamingException e) {
			myLogger.error("NamingException, repo = null", e);
			repo = null;
		}

		return repo != null;
	}

	private Session getSession() throws LoginException, RepositoryException {
		if (null == session) {
			myLogger.warn("login to repository necessary");

			if (!isOnline()) {
				myLogger.info("repository not available");
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE, "repo == null");
			}
			session = repo.login(cred);
			myLogger.warn("login to repository done.");
		}
		return session;
	}

	private void closeSession() {
		if (null != session) {
			myLogger.info("logout from repository");
			try {
				if (session.isLive()) {
					session.logout();
					session = null;
				} else {
					myLogger.warn("session was not live?");
				}
			} catch (IllegalStateException e) {
				myLogger.error("jcr:closeSession:IllegalState", e);
			}
		}
		session = null;

		closeRepo();
	}

	private void forcedCloseSession() {
		if (null != session) {
			myLogger.info("forced logout from repository");
			//
			// session.logout() ;
			// session = null ;
		}
		session = null;
		repo = null;
	}

	private String getCorrectBelegNr(String cnr, String mandantCNr) throws EJBExceptionLP, RemoteException {
		Matcher m = Pattern.compile("([0-9]*)(?:\\.[^0-9]*)([0-9]*)").matcher(cnr);
		if (!m.find())
			return cnr;
		Integer geschaeftsJahr = Integer.parseInt(m.group(1));
		Integer belegNummer = Integer.parseInt(m.group(2));

		if (geschaeftsJahr < 100 && geschaeftsJahr > 50) { // belege von 1950
															// bis 1999
			geschaeftsJahr += 1900;
		} else if (geschaeftsJahr < 51) { // belege von 2000 bis 2050
			geschaeftsJahr += 2000;
		}

		ParametermandantDto pm = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG);
		String mk = pm.getCWert().trim();
		BelegnummerGeneratorObj generator = new BelegnummerGeneratorObj();
		LpBelegnummerFormat bnFormat = generator.getBelegnummernFormat(mandantCNr);
		LpBelegnummer belegnummer = new LpBelegnummer(geschaeftsJahr, mk, belegNummer);
		return bnFormat.format(belegnummer);
	}

	@Override
	public List<?> getDtoMatches(String path, String belegart, String searchKey) {

		int matchLimit = 20;

		String mandantCNr;
		try {
			// mandantCNr =
			// getMandantFromJCR(getJCRDocFac().getJCRDocDtoFromNode(new
			// DocPath(path)));
			mandantCNr = getMandantFromJCR(getJCRDocDtoFromNode(new DocPath(path)));
			if (mandantCNr == null) {
				mandantCNr = getSystemFac().getHauptmandant();
			}
		} catch (Exception e1) {
			return null;
		}
		if (belegart.equals(BELEGART_PARTNER) || belegart.equals(BELEGART_KUNDE) || belegart.equals(BELEGART_LIEFERANT)
				|| belegart.equals(BELEGART_UVA)) {
			ArrayList<String> values = getStringsFromPath(path, belegart.equals(BELEGART_UVA) ? 4 : 3);
			PartnerDto[] partner = null;
			if (searchKey.length() == 0)
				partner = getClosestPartners(values.get(0));

			List<PartnerDto> searchMatches = new ArrayList<PartnerDto>();
			if (partner != null && partner.length > 0) {

				int matchCount = 0;
				for (PartnerDto partnerDto : partner) {
					String sPersonal = partnerDto.getCName1nachnamefirmazeile1().replace("/", ".");
					if (partnerDto.getCName2vornamefirmazeile2() != null) {
						sPersonal = sPersonal + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".");
					}
					if (sPersonal.toLowerCase().contains(searchKey.toLowerCase())) {
						try {
							if (belegart.equals(BELEGART_KUNDE)
									&& null != getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerDto.getIId(),
											mandantCNr, null)
									|| belegart.equals(BELEGART_LIEFERANT)
											&& null != getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(
													partnerDto.getIId(), mandantCNr, null)
									|| belegart.equals(BELEGART_UVA) && null != getFinanzFac()
											.finanzamtFindByPartnerIIdMandantCNrOhneExcWithNull(partnerDto.getIId(),
													mandantCNr)
									|| belegart.equals(BELEGART_PARTNER)) {
								searchMatches.add(getPartnerFac().partnerFindByPrimaryKeyOhneExc(partnerDto.getIId(),
										theClientDto));
								if (++matchCount > matchLimit)
									break;
							}
						} catch (Exception e) {
							return null;
						}
					}
				}
			}
			if (searchMatches.size() == 0) {
				partner = getPartnerFac().partnerFindByName1Lower("%" + searchKey + "%");

				int matchCount = 0;
				for (PartnerDto partnerDto : partner) {
					try {
						if (belegart.equals(BELEGART_KUNDE)
								&& null != getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerDto.getIId(),
										mandantCNr, null)
								|| belegart.equals(BELEGART_LIEFERANT)
										&& null != getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(
												partnerDto.getIId(), mandantCNr, null)
								|| belegart.equals(BELEGART_UVA)
										&& null != getFinanzFac().finanzamtFindByPartnerIIdMandantCNrOhneExcWithNull(
												partnerDto.getIId(), mandantCNr)
								|| belegart.equals(BELEGART_PARTNER)) {

							searchMatches.add(
									getPartnerFac().partnerFindByPrimaryKeyOhneExc(partnerDto.getIId(), theClientDto));
							if (++matchCount > matchLimit) {
								PartnerDto p = new PartnerDto();
								p.setCName1nachnamefirmazeile1(
										"<span style=\"color:blue\">Suche auf " + matchLimit + " eingeschr\u00E4nkt.");
								p.setCName2vornamefirmazeile2(
										"Es gibt weitere " + (partner.length - matchLimit) + " Eintr\u00E4ge!</span>");
								searchMatches.add(p);
								break;
							}
						}
					} catch (Exception e) {
						return null;
					}
				}
			}

			return searchMatches;
		}

		if (belegart.equals(BELEGART_PERSONAL)) {
			List<PersonalDto> searchMatches = new ArrayList<PersonalDto>();
			PersonalDto[] pers;
			try {
				pers = getPersonalFac().personalFindByMandantCNr(mandantCNr, true);
			} catch (Exception e) {
				return searchMatches;
			}
			for (PersonalDto personalDto : pers) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKeyOhneExc(personalDto.getPartnerIId(),
						null);
				String sPersonal = partnerDto.getCName1nachnamefirmazeile1().replace("/", ".");
				if (partnerDto.getCName2vornamefirmazeile2() != null) {
					sPersonal = sPersonal + " " + partnerDto.getCName2vornamefirmazeile2().replace("/", ".");
				}
				if (sPersonal.contains(searchKey)) {
					searchMatches.add(personalDto);
				}
			}

			return searchMatches;
		}
		if (belegart.equals(BELEGART_BENUTZER)) {
			BenutzermandantsystemrolleDto[] benutzermandantsystemrolleDtos = getBenutzerFac()
					.benutzermandantsystemrolleFindByMandantCNrOhneExc(mandantCNr);
			List<BenutzerDto> searchMatches = new ArrayList<BenutzerDto>();
			for (BenutzermandantsystemrolleDto b : benutzermandantsystemrolleDtos) {
				BenutzerDto benutzerDto = getBenutzerFac().benutzerFindByPrimaryKeyOhneExc(b.getBenutzerIId());
				if (benutzerDto.getCBenutzerkennung().contains(searchKey)) {
					searchMatches.add(benutzerDto);
				}
			}
			return searchMatches;
		}

		return null;
	}

	// Dokumentenpflege
	@Override
	public DocumentResult applyDtoTo(String path, List<String> documents, String belegart, Object dto,
			TheClientDto theClientDto) {
		result = new DocumentResult(belegart);
		if (documents.size() == 0 || belegart == null)
			return null;
		for (String filename : documents) {
			JCRDocDto baseJCR = null;
			String oldPath = null;
			try {
				// baseJCR = getJCRDocFac().getJCRDocDtoFromNode(new
				// DocPath(path).add(new DocNodeFile(filename)));
				baseJCR = getJCRDocDtoFromNode(new DocPath(path).add(new DocNodeFile(filename)));
				if (baseJCR == null)
					return null;
				oldPath = baseJCR.getsPath();
			} catch (RepositoryException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
			if (dto.equals(SAVE_AS_ORPHAN)) {
				// ArrayList<DocNodeVersion> versionNodes =
				// getJCRDocFac().getAllVersions(baseJCR);
				ArrayList<DocNodeVersion> versionNodes = getAllVersions(baseJCR);
				Collections.reverse(versionNodes);
				for (DocNodeVersion node : versionNodes) {
					JCRDocDto version = node.getJCRDocDto();
					String mandant = getMandantFromJCR(version);
					DocPath newPath = new DocPath(path);
					if (!belegart.equals(BELEGART_PARTNER))
						newPath.asDocNodeList().add(1, new DocNodeLiteral(mandant));
					newPath.add(new DocNodeFile(baseJCR.getsName()));
					// version = getJCRDocFac().getData(version);
					version = getData(version);
					version.setDocPath(newPath);

					// getJCRDocFac().addNewDocumentOrNewVersionOfDocument(version,
					// theClientDto);
					addNewDocumentOrNewVersionOfDocument(version, theClientDto);
					try {
						// newPath =
						// getJCRDocFac().getJCRDocDtoFromNode(newPath).getDocPath();
						newPath = getJCRDocDtoFromNode(newPath).getDocPath();
					} catch (RepositoryException e) {
						return null;
					} catch (IOException e) {
						return null;
					}
					// delteJCRVersion(versionPath, baseJCR.getsPath());
					newPath.asDocNodeList().remove(newPath.getLastDocNode());
					if (!result.contains(newPath.getPathAsString()))
						result.addFoundFile(newPath.getPathAsString(), newPath.getVisualPathAsString(), filename);
				}
				try {
					// getJCRDocFac().removeNode(oldPath);
					removeNode(oldPath);
				} catch (ItemNotFoundException e) {
					e.printStackTrace();
				} catch (AccessDeniedException e) {
					e.printStackTrace();
				} catch (RepositoryException e) {
					e.printStackTrace();
				}

			} else if (belegart.equals(BELEGART_PARTNER) || belegart.equals(BELEGART_KUNDE)
					|| belegart.equals(BELEGART_LIEFERANT) || belegart.equals(BELEGART_UVA)) {
				PartnerDto partner = (PartnerDto) dto;
				baseJCR.setsRow(partner.getIId().toString());
				String mandant = getMandantFromJCR(baseJCR);
				DocPath newPath = null;
				if (belegart.equals(BELEGART_PARTNER)) {
					newPath = new DocPath(new DocNodePartner(partner));
				} else if (belegart.equals(BELEGART_KUNDE)) {
					KundeDto kunde;
					try {
						kunde = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partner.getIId(), mandant,
								theClientDto);
					} catch (Exception e) {
						return null;
					}
					newPath = new DocPath(new DocNodeKunde(kunde, partner));
				} else if (belegart.equals(BELEGART_LIEFERANT)) {
					LieferantDto lieferant;
					try {
						lieferant = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(partner.getIId(),
								mandant, theClientDto);
					} catch (Exception e) {
						return null;
					}
					newPath = new DocPath(new DocNodeLieferant(lieferant, partner));
				} else if (belegart.equals(BELEGART_UVA)) {
					ArrayList<String> values = getStringsFromPath(baseJCR.getsPath(), 2, 5);

					Iterator<String> iter = values.iterator();
					int jahr = Integer.parseInt(iter.next());
					int periode = Integer.parseInt(iter.next());

					ReportUvaKriterienDto uva = new ReportUvaKriterienDto();
					FinanzamtDto fAmt = getFinanzFac()
							.finanzamtFindByPartnerIIdMandantCNrOhneExcWithNull(partner.getIId(), mandant);
					if (fAmt == null)
						return null;
					fAmt.setMandantCNr(mandant);
					fAmt.setPartnerDto(partner);
					uva.setIGeschaeftsjahr(jahr);
					uva.setIPeriode(periode);
					uva.setSPeriode(String.valueOf(periode));
					uva.setFinanzamtIId(partner.getIId());
					newPath = new DocPath(new DocNodeUVAVerprobung(uva, fAmt));
				}

				if (newPath == null)
					return null;

				// baseJCR = getJCRDocFac().getData(baseJCR);
				baseJCR = getData(baseJCR);
				baseJCR.setDocPath(newPath.add(new DocNodeFile(filename)));
				// getJCRDocFac().addNewDocumentOrNewVersionOfDocument(baseJCR,
				// theClientDto);
				addNewDocumentOrNewVersionOfDocument(baseJCR, theClientDto);

				try {
					// getJCRDocFac().removeNode(oldPath);
					removeNode(oldPath);
				} catch (ItemNotFoundException e) {
					return null;
				} catch (AccessDeniedException e) {
					return null;
				} catch (RepositoryException e) {
					return null;
				}
				newPath.asDocNodeList().remove(newPath.getLastDocNode());
				result.addFoundFile(newPath.getPathAsString(), newPath.getVisualPathAsString(), filename);
			}

		}
		removeEmptyNodes();
		return result;
	}

	@Override
	public DocumentResult runPflege(TheClientDto theClientDto, String[] args) {

		try {
			myLogger.warn("Starte Pflege! Anzahl Argumente: " + args.length);
			this.theClientDto = theClientDto;
			init(Arrays.asList(args));
			myLogger.warn("Belegart: " + belegartToRepair);

			run();

			// removeEmptyNodes();

			result.setCheckedFilesCount(jcrsCheckedCount);
			result.setDeletedFilesCount(jcrsDeletedCount);

			myLogger.warn("R\u00FCckgabe an Client: " + belegartToRepair.trim() + " Dokumente: " + "Gepr\u00FCft: "
					+ jcrsCheckedCount + " Gel\u00F6scht: " + jcrsDeletedCount + " Neu: " + result.getNewFileCount()
					+ ".");
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
			return result;
		} finally {
			forcedCloseSession();
		}
	}

	@Override
	public int removeEmptyNodes() {
		return removeAllEmptyNodes();
	}

	private void run() {
		try {
			repairBelegart(belegartToRepair);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void init(List<String> args) {
		clearedPaths = new ArrayList<String>();

		skipExisting = false;
		overwriteExisting = false;
		clearFolders = false;
		readOnly = false;
		startIndex = 0;

		jcrsCheckedCount = 0;
		jcrsDeletedCount = 0;

		mandanten = new ArrayList<String>();
		for (MandantDto m : getMandantFac().mandantFindAll(null)) {
			mandanten.add(m.getCNr());
		}

		for (String string : args) {
			if (string.equals(ARG_OVERWRITEEXISTING))
				overwriteExisting = true;
			else if (string.equals(ARG_SKIPEXISTING))
				skipExisting = true;
			else if (string.equals(ARG_CLEARNEWFOLDERS))
				clearFolders = true;
			else if (string.equals(ARG_DELETEOLDFILES))
				deleteFiles = true;
			else if (string.equals(ARG_READONLY))
				readOnly = true;
			else if (string.startsWith(ARG_STARTINDEX)) {
				try {
					startIndex = Integer.parseInt(string.substring(string.lastIndexOf("=") + 1));
				} catch (NumberFormatException ex) {
					throw new IllegalArgumentException("value of ARG_STARTINDEX must be a number");
				}
			} else
				belegartToRepair = string;
		}
		result = new DocumentResult(belegartToRepair);
	}

	private void clearFolderIfNotDoneBefore(DocPath docPath) {

		myLogger.warn("L\u00F6sche neuen Pfad: " + docPath.getVisualPathAsString());
		DocPath deleteDocPath = docPath.getDeepCopy();
		deleteDocPath.asDocNodeList().remove(deleteDocPath.getLastDocNode());
		String deletePath = deleteDocPath.getPathAsString();
		if (clearedPaths.contains(deletePath))
			return;
		try {
			// getJCRDocFac().removeNode(deletePath);
			removeNode(deletePath);
		} catch (ItemNotFoundException e) {
		} catch (AccessDeniedException e) {
		} catch (RepositoryException e) {
		}
		clearedPaths.add(deletePath);
	}

	private void repairBelegart(String belegart) {
		ArrayList<JCRDocDto> jcrDocs = null;
		String path = pathMap.get(belegart);
		if (path == null) {
			myLogger.error("Belegart " + belegart + " ist noch nicht implementiert!");
			return;
		}

		long startTime = System.currentTimeMillis();

		myLogger.warn("Suche Dokumente in " + path);
		JcrSession jcrSession = new JcrSession();
		// jcrDocs = getJCRDocFac().getAllJCRDocs(path, DOC_LIMIT + startIndex);
		jcrDocs = getAllJCRDocs(jcrSession, path, 100 + startIndex);
		myLogger.warn(jcrDocs.size() + " gefundene Dokumente.");
		myLogger.warn("\u00DCberspringe " + startIndex + " Dokumente.");
		int i = 0;
		for (JCRDocDto jcrDocDto : jcrDocs) {
			if (i < startIndex) {
				i++;
				continue;
			}
			try {
				path = jcrDocDto.getsPath();
				repairDocPath(jcrDocDto, belegart);

				if (getJCRDocDtoFromNode(new DocPath(path)) == null) {
					jcrsDeletedCount++;
					removeNodeAndAllChildlessParents(path.substring(0, path.lastIndexOf("/")));
				}

				jcrSession.closeSession();
			} catch (RepositoryException e) {
				myLogger.error("Pr\u00FCfung ob alte Dokumente erfolgreich gel\u00F6scht ist gescheitert!", e);
			} catch (IOException e) {
				myLogger.error("Pr\u00FCfung ob alte Dokumente erfolgreich gel\u00F6scht ist gescheitert!", e);
			}
			jcrsCheckedCount++;

			if (System.currentTimeMillis() > startTime + timeout) {
				myLogger.warn("Transaction-Dauer von " + timeout / 1000 + " s \u00FCberschritten."
						+ "\nTransaction zu Ende.");
				break;
			}
		}
		jcrSession.closeSession();
	}

	private boolean acceptBeleg(String belegart, String reportName) {
		if (BELEGART_RECHNUNG.equals(belegart)) {
			return !Helper.isOneOf(reportName,
					new String[] { RechnungReportFac.REPORT_GUTSCHRIFT, RechnungReportFac.REPORT_PROFORMARECHNUNG });
		}
		if (BELEGART_GUTSCHRIFT.equals(belegart)) {
			return RechnungReportFac.REPORT_GUTSCHRIFT.equals(reportName);
		}
		if (BELEGART_PROFORMARECHNG.equals(belegart)) {
			return RechnungReportFac.REPORT_PROFORMARECHNUNG.equals(reportName);
		}
		return true;
	}

	private Object[] getDtos(JCRDocDto jcr, String basePath, String belegart, String mandantCNr)
			throws EJBExceptionLP, RemoteException {

		if (!acceptBeleg(belegart, jcr.getsName()))
			return new Object[] { IGNORE_FLAG };

		if (belegart.equals(BELEGART_LAGERSTANDSLISTE))
			return new Object[] { mandantCNr };
		Integer iId = null;
		String cNr = jcr.getsBelegnummer() == null ? null : getCorrectBelegNr(jcr.getsBelegnummer(), mandantCNr);
		try {
			iId = Integer.parseInt(jcr.getsRow());
		} catch (NumberFormatException exce) {
		}

		Object theDtos[] = null;

		if (iId != null) {
			theDtos = getDtosByIId(iId, belegart);
		}

		if (theDtos != null && theDtos.length > 0) {
			return theDtos;
		}

		if (cNr != null && mandantCNr != null) {
			theDtos = getDtosByCNrMandantCNr(cNr, mandantCNr, belegart);
		}

		if (theDtos != null && theDtos.length > 0) {
			return theDtos;
		}

		theDtos = getDtosByJCR(jcr, basePath, belegart);

		return theDtos;
	}

	// private int getIIdFromDto(Object dto, String belegart) {
	// for(Method m : dto.getClass().getMethods()) {
	// if(m.getName().equals("getIId"))
	// try {
	// return Integer.parseInt(m.invoke(dto).toString());
	// } catch (NumberFormatException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// }
	// }
	// return -1;
	// }

	// private String getBelegartWennRechnung(Object theDtos[], String belegart)
	// {
	// if(belegart.equals(BELEGART_RECHNUNG)) {
	// RechnungDto rechnung = (RechnungDto)theDtos[0];
	// if(rechnung == null || rechnung.getRechnungartCNr() == null)
	// return belegart;
	// String s = rechnung.getRechnungartCNr().toLowerCase();
	// if(s.contains("gutschrift"))
	// return BELEGART_GUTSCHRIFT;
	// if(s.contains("proforma"))
	// return BELEGART_PROFORMARECHNG;
	// } else if(belegart.equals(BELEGART_RECHPOSITION)) {
	// RechnungDto rechnung = (RechnungDto)theDtos[1];
	// String s = rechnung.getBelegartCNr().toLowerCase();
	// if(s.contains("gutschrift"))
	// return BELEGART_GUTSPOSITION;
	// if(s.contains("proforma"))
	// return BELEGART_PROFORMAPOS;
	// }
	// return belegart;
	// }

	private void repairDocPath(JCRDocDto jcr, String belegart)
			throws ValueFormatException, PathNotFoundException, RepositoryException, IOException {

		if (getDocPathFromJCR(jcr).getLastDocNode().getVersion() > 1) {
			myLogger.warn("Neuer Doc-Node " + jcr.getsPath() + " wird bei der Migration ignoriert.");
			return;
		}
		List<List<JCRDocDto>> versionsByMandant = getJCRVersionsSortedByMandant(jcr, belegart);
		// fuer jeden Mandanten jetzt Dtos holen, pruefen ob gefunden und
		// speichern

		boolean removeNode = true;
		for (List<JCRDocDto> versionsInThisMandant : versionsByMandant) {
			String mandant = null;
			JCRDocDto latestValidJCR = null;
			if (versionsInThisMandant.size() == 0)
				continue;
			for (JCRDocDto jcrDocDto : versionsInThisMandant) {
				if (mandant == null && jcrDocDto.getlAnleger() != 11) {
					latestValidJCR = jcrDocDto;
					mandant = getMandantFromJCR(latestValidJCR);
				}
				if (mandant != null) {
					break;
				}
			}
			if (latestValidJCR == null)
				latestValidJCR = versionsInThisMandant.get(0);
			mandant = getMandantFromJCR(latestValidJCR);

			myLogger.warn("Suche DTOs in Mandant " + mandant + "...");

			Object theDtos[] = null;

			if (jcr != null && jcr.getsPath() != null)
				theDtos = getDtos(jcr, jcr.getsPath(), belegart, mandant);

			if (theDtos != null && theDtos.length != 0 && theDtos[0] != null) {
				if (theDtos[0].equals(IGNORE_FLAG)) {
					myLogger.warn("Beleg wird ignoriert.");
					return;
				}
			} else {
				myLogger.error("DTOs konnten nicht gefunden werden.");

				addNotFoundToResult(jcr.getsPath());
				continue;
			}

			DocPath docPath = new DocPath(DocNodeFactory.createDocNodeFromDtoBelegart(theDtos, belegart));
			docPath.add(new DocNodeFile(latestValidJCR.getsName()));
			myLogger.warn("Neuer Pfad: " + docPath.getPathAsString());
			myLogger.warn("Sichtbarer Pfad: " + docPath.getVisualPathAsString());
			if (clearFolders)
				clearFolderIfNotDoneBefore(docPath);

			int copiedDocs = 0;
			if (!readOnly) {
				copiedDocs = repairAllVersions(versionsInThisMandant, docPath);
			}

			if (!(deleteFiles && !readOnly && copiedDocs == versionsInThisMandant.size())) {
				removeNode = false;
			}
			if (copiedDocs > 0)
				addFoundToResult(docPath);
			if (copiedDocs < versionsInThisMandant.size())
				addNotFoundToResult(jcr.getsPath());
		}
		if (removeNode == true) {
			removeNode(jcr.getsPath());
			myLogger.warn("L\u00F6sche node!");
		} else {
			myLogger.warn("L\u00F6sche node nicht!");
		}
	}

	private List<List<JCRDocDto>> getJCRVersionsSortedByMandant(JCRDocDto jcr, String belegart) {
		// ArrayList<DocNodeVersion> jcrVersions =
		// getJCRDocFac().getAllVersions(jcr);
		ArrayList<DocNodeVersion> jcrVersions = getAllVersions(jcr);

		// jcr.setsPath("HELIUMV/Bestellung/Bestellung/09.0000005/bes_bestellung.jasper");
		// jcrVersions = getJCRDocFac().getAllVersions(jcr);
		myLogger.warn("Pr\u00FCfe Dokument " + jcr.getsPath());
		myLogger.warn("Anzahl Versionen: " + jcrVersions.size());
		// Einzelne versionen in Mandanten sortieren
		List<List<JCRDocDto>> versionsByMandant = new ArrayList<List<JCRDocDto>>();
		if (belegart.equals(BELEGART_PARTNER)) {
			List<JCRDocDto> versionsInThisMandant = new ArrayList<JCRDocDto>();
			for (DocNodeVersion version : jcrVersions) {
				JCRDocDto jcrVersion = version.getJCRDocDto();
				versionsInThisMandant.add(jcrVersion);
			}
			versionsByMandant.add(versionsInThisMandant);
		} else {

			for (String mandant : mandanten) {
				List<JCRDocDto> versionsInThisMandant = new ArrayList<JCRDocDto>();
				for (DocNodeVersion version : jcrVersions) {
					JCRDocDto jcrVersion = version.getJCRDocDto();
					if (mandant.equals(getMandantFromJCR(jcrVersion))) {
						// jcrVersion.setsPath();
						versionsInThisMandant.add(jcrVersion);
					}
				}
				if (versionsInThisMandant.size() > 0)
					versionsByMandant.add(versionsInThisMandant);
			}

			// Wenn versionen auf 2 mandanten verteilt,
			// ueberpruefen ob mandantenabweichende Dateien von LPAdmin stammen
			// Wenn ja, auf richtigen mandanten Kopieren
			List<JCRDocDto> listToRemove = null;
			if (versionsByMandant.size() == 2) {
				int i = 1;
				for (List<JCRDocDto> list : versionsByMandant) {
					boolean onlyLPAdminInThisMandant = true;
					for (JCRDocDto jcrDocDto : list) {
						if (jcrDocDto.getlAnleger() != 11) {
							onlyLPAdminInThisMandant = false;
							break;
						}
					}
					if (onlyLPAdminInThisMandant) {
						for (JCRDocDto jcrDocDto : list) {
							versionsByMandant.get(i).add(jcrDocDto);
						}
						listToRemove = list;
						break;
					}
					i--;
				}
				if (listToRemove != null)
					versionsByMandant.remove(listToRemove);
			}
			// wenn jetzt immernoch mehrere mandanten existieren sind lpadmin
			// dateien ungueltig
			if (versionsByMandant.size() > 1) {
				for (List<JCRDocDto> list : versionsByMandant) {
					for (int i = 0; i < list.size();) {
						JCRDocDto jcrDocDto = list.get(i);
						if (jcrDocDto.getlAnleger() == 11) {
							// addNotFoundToResult(jcr.getsPath());
							list.remove(i);
						} else {
							i++;
						}
					}
				}
			}
		}
		if (versionsByMandant.size() == 0 && jcr.getlVersion() == 0) {
			List<JCRDocDto> list = new ArrayList<JCRDocDto>();
			list.add(jcr);
			versionsByMandant.add(list);
		}
		return versionsByMandant;
	}

	private int repairAllVersions(List<JCRDocDto> list, DocPath docPath) {
		Collections.reverse(list);
		// boolean nodeExists = getJCRDocFac().checkIfNodeExists(docPath);
		boolean nodeExists = checkIfNodeExistsWithinTransaction(docPath);
		if (overwriteExisting && nodeExists) {
			try {
				String path = docPath.getPathAsString();
				// getJCRDocFac().removeNode(path);
				removeNode(path);
			} catch (Exception ex) {
				return -1;
			}
		} else if (skipExisting && nodeExists) {
			return list.size();
		}
		try {
			int copiedDocs = 0;
			for (JCRDocDto jcr : list) {
				// JCRDocDto version = getJCRDocFac().getData(jcr);
				JCRDocDto version = getData(jcr);

				if (existsVersion(docPath, version)) {
					myLogger.error("Die Version " + jcr.getlVersion() + " existiert schon im Pfad "
							+ docPath.getVisualPathAsString());
					copiedDocs++;
					continue;
				}
				version.setDocPath(docPath);
				myLogger.warn("Speichere Version: " + version.getlVersion());
				// getJCRDocFac().addNewDocumentOrNewVersionOfDocument(version,
				// theClientDto);
				addNewDocumentOrNewVersionOfDocument(version, theClientDto);
				copiedDocs++;
			}
			return copiedDocs;
		} catch (Exception ex) {
			myLogger.error("Fehler: ", ex);
			return -1;
		}
	}

	private boolean existsVersion(DocPath targetLocation, JCRDocDto jcr) {
		boolean versionAlreadyExists = false;

		JCRDocDto targetJCR = null;
		try {
			// targetJCR = getJCRDocFac().getJCRDocDtoFromNode(targetLocation);
			targetJCR = getJCRDocDtoFromNode(targetLocation);
		} catch (RepositoryException e) {
		} catch (IOException e) {
		}
		if (targetJCR != null) {
			// for(DocNodeVersion
			// dnVersion:getJCRDocFac().getAllVersions(targetJCR)) {
			for (DocNodeVersion dnVersion : getAllVersions(targetJCR)) {
				if (dnVersion.getJCRDocDto().getlVersion() == jcr.getlVersion()) {
					versionAlreadyExists = true;
					break;
				}
			}
		}
		return versionAlreadyExists;
	}

	private String getMandantFromJCR(JCRDocDto jcr) {
		Integer key = new Integer((int) jcr.getlAnleger());
		PersonalDto cachedPers = cachedPersonalDto.get(key);
		if (null == cachedPers) {
			cachedPers = getPersonalFac().personalFindByPrimaryKeySmallOhneExc((int) jcr.getlAnleger());
			if (null == cachedPers)
				return null;
			cachedPersonalDto.put(key, cachedPers);
		}
		return cachedPers.getMandantCNr();

		// PersonalDto pers = getPersonalFac()
		// .personalFindByPrimaryKeySmallOhneExc((int) jcr.getlAnleger());
		// if(pers==null) return null;
		// return pers.getMandantCNr();
	}

	private Object[] getDtosByIId(int iId, String belegart) {
		if (belegart.equals(BELEGART_ANGEBOT)) {
			return new Object[] { getAngebotFac().angebotFindByPrimaryKeyOhneExec(iId) };
		}
		if (belegart.equals(BELEGART_AGPOSITION)) {
			AngebotpositionDto agpos = getAngebotpositionFac().angebotpositionFindByPrimaryKeyOhneExc(iId);
			if (agpos == null)
				return null;
			Object angebot = getDtosByIId(agpos.getBelegIId(), BELEGART_ANGEBOT)[0];
			return new Object[] { agpos, angebot };
		}
		if (belegart.equals(BELEGART_AGSTUECKLISTE)) {
			return new Object[] { getAngebotstklFac().agstklFindByPrimaryKeyOhneExc(iId) };
		}
		if (belegart.equals(BELEGART_AGSTKLPOSITION)) {
			AgstklpositionDto aspos = getAngebotstklpositionFac().agstklpositionFindByPrimaryKeyOhneExc(iId);
			if (aspos == null)
				return null;
			Object agstkl = getDtosByIId(aspos.getBelegIId(), BELEGART_AGSTUECKLISTE)[0];
			return new Object[] { aspos, agstkl };
		}
		if (belegart.equals(BELEGART_ANFRAGE)) {
			return new Object[] { getAnfrageFac().anfrageFindByPrimaryKeyOhneExc(iId) };
		}
		if (belegart.equals(BELEGART_ANFPOSITION)) {
			AnfragepositionDto anfpos = getAnfragepositionFac().anfragepositionFindByPrimaryKeyOhneExc(iId);
			if (anfpos == null)
				return null;
			Object anfrage = getDtosByIId(anfpos.getBelegIId(), BELEGART_ANFRAGE)[0];
			return new Object[] { anfpos, anfrage };
		}
		if (belegart.equals(BELEGART_ARTIKEL)) {
			ArtikelDto artikel = getArtikelFac().artikelFindByPrimaryKeySmallOhneExc(iId, theClientDto);
			return new Object[] { artikel };
		}
		if (belegart.equals(BELEGART_STKLPOSITION)) {
			StuecklistepositionDto stklpos = getStuecklisteFac().stuecklistepositionFindByPrimaryKeyOhneExc(iId,
					theClientDto);
			if (stklpos == null)
				return null;
			Object stkl = getDtosByIId(stklpos.getArtikelIId(), BELEGART_ARTIKEL)[0];
			return new Object[] { stklpos, stkl };
		}
		if (belegart.equals(BELEGART_AUFTRAG)) {
			return new Object[] { getAuftragFac().auftragFindByPrimaryKeyOhneExc(iId) };
		}
		if (belegart.equals(BELEGART_AUFPOSITION)) {
			AuftragpositionDto auftpos = getAuftragpositionFac().auftragpositionFindByPrimaryKeyOhneExc(iId);
			if (auftpos == null)
				return null;
			Object auftrag = getDtosByIId(auftpos.getBelegIId(), BELEGART_AUFTRAG)[0];
			return new Object[] { auftpos, auftrag };
		}
		if (belegart.equals(BELEGART_PARTNER)) {
			PartnerDtoSmall small = getPartnerFac().partnerFindByPrimaryKeySmallOhneExc(iId);
			if (small == null)
				return null;
			PartnerDto part = new PartnerDto();
			part.setAnredeCNr(small.getAnredeCNr());
			part.setCName1nachnamefirmazeile1(small.getCName1nachnamefirmazeile1());
			part.setCName2vornamefirmazeile2(small.getCName2vornamefirmazeile2());
			part.setCTitel(small.getCTitel());
			part.setIId(small.getIId());
			return new Object[] { part };
		}
		if (belegart.equals(BELEGART_PERSONAL)) {
			PersonalDto personal = getPersonalFac().personalFindByPrimaryKeySmallOhneExc(iId);
			if (personal == null)
				return null;
			PartnerDto partner = (PartnerDto) getDtosByIId(personal.getPartnerIId(), BELEGART_PARTNER)[0];
			return new Object[] { personal, partner };
		}
		if (belegart.equals(BELEGART_BENUTZER)) {
			BenutzerDto benutzer = getBenutzerFac().benutzerFindByPrimaryKeyOhneExc(iId);
			if (benutzer == null)
				return null;
			String mandantCNr = benutzer.getMandantCNrDefault();
			BenutzermandantsystemrolleDto bmr = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIIdMandantCNrOhneExc(iId, mandantCNr);

			return getDtosByIId(bmr.getPersonalIIdZugeordnet(), BELEGART_PERSONAL);
		}
		if (belegart.equals(BELEGART_KUNDE)) {
			KundeDto kunde = getKundeFac().kundeFindByPrimaryKeyOhneExc(iId, theClientDto);
			if (kunde == null)
				return null;
			PartnerDto partner = (PartnerDto) getDtosByIId(kunde.getPartnerIId(), BELEGART_PARTNER)[0];
			return new Object[] { kunde, partner };
		}
		if (belegart.equals(BELEGART_LIEFERANT)) {
			LieferantDto lieferant = getLieferantFac().lieferantFindByPrimaryKeySmallWithNull(iId);
			if (lieferant == null)
				return null;
			PartnerDto partner = (PartnerDto) getDtosByIId(lieferant.getPartnerIId(), BELEGART_PARTNER)[0];
			return new Object[] { lieferant, partner };
		}
		if (belegart.equals(BELEGART_BESTELLUNG)) {
			return new Object[] { (BestellungDto) getBestellungFac().bestellungFindByPrimaryKeyWithNull(iId) };
		}
		if (belegart.equals(BELEGART_BESTPOSITION)) {
			BestellpositionDto bestpos = getBestellpositionFac().bestellpositionFindByPrimaryKeyOhneExc(iId);
			if (bestpos == null)
				return null;
			BestellungDto bestellung = (BestellungDto) getDtosByIId(bestpos.getBelegIId(), BELEGART_BESTELLUNG)[0];
			return new Object[] { bestpos, bestellung };
		}
		if (belegart.equals(BELEGART_WARENEINGANG)) {
			WareneingangDto wEingang = getWareneingangFac().wareneingangFindByPrimaryKeyOhneExc(iId);
			if (wEingang == null)
				return null;
			Object bestellung = getDtosByIId(wEingang.getBestellungIId(), BELEGART_BESTELLUNG)[0];
			return new Object[] { wEingang, bestellung };
		}
		if (belegart.equals(BELEGART_WEPOSITION)) {
			WareneingangspositionDto wePos = getWareneingangFac().wareneingangspositionFindByPrimaryKeyOhneExc(iId);
			if (wePos == null)
				return null;
			Object dtos[] = getDtosByIId(wePos.getWareneingangIId(), BELEGART_WARENEINGANG);
			return new Object[] { wePos, dtos[0], dtos[1] };
		}
		if (belegart.equals(BELEGART_EINGANGSRECHNG)) {
			return new Object[] { getEingangsrechnungFac().eingangsrechnungFindByPrimaryKeyWithNull(iId) };
		}
		if (belegart.equals(BELEGART_LOS)) {
			return new Object[] { getFertigungFac().losFindByPrimaryKeyOhneExc(iId) };
		}
		if (belegart.equals(BELEGART_LOSABLIEFERUNG)) {
			LosablieferungDto lablieferung = getFertigungFac().losablieferungFindByPrimaryKeyOhneExc(iId);

			if (lablieferung == null)
				return null;
			Object los = getDtosByIId(lablieferung.getLosIId(), BELEGART_LOS)[0];
			return new Object[] { lablieferung, los };
		}
		if (belegart.equals(BELEGART_LIEFERSCHEIN)) {
			return new Object[] { getLieferscheinFac().lieferscheinFindByPrimaryKeyOhneExc(iId) };
		}
		if (belegart.equals(BELEGART_LIEFERSPOSITION)) {
			LieferscheinpositionDto liefpos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByPrimaryKeyOhneExc(iId, null);

			if (liefpos == null)
				return null;
			Object lieferschein = getDtosByIId(liefpos.getLieferscheinIId(), BELEGART_LIEFERSCHEIN)[0];
			return new Object[] { liefpos, lieferschein };
		}
		if (belegart.equals(BELEGART_PROJEKT)) {
			ProjektDto proj = getProjektFac().projektFindByPrimaryKeyOhneExc(iId);

			if (proj == null)
				return null;
			BereichDto bereich = getProjektServiceFac().bereichFindByPrimaryKey(proj.getBereichIId());
			return new Object[] { proj, bereich };
		}
		if (belegart.equals(BELEGART_PROJHISTORY)) {
			HistoryDto hist = getProjektFac().historyFindByPrimaryKeyOhneExc(iId);

			if (hist == null)
				return null;
			Object proj[] = getDtosByIId(hist.getProjektIId(), BELEGART_PROJEKT);
			return new Object[] { hist, proj[0], proj[1] };
		}
		if (belegart.equals(BELEGART_RECHNUNG)) {
			RechnungDto rech = getRechnungFac().rechnungFindByPrimaryKeyOhneExc(iId);
			if (rech == null)
				return null;
			if (!(rech.getRechnungartCNr().toLowerCase().contains("gutschrift")
					|| rech.getRechnungartCNr().equals(BELEGART_PROFORMARECHNG)))
				return new Object[] { rech };
			return new Object[] { IGNORE_FLAG };
		}
		if (belegart.equals(BELEGART_GUTSCHRIFT)) {
			RechnungDto rech = getRechnungFac().rechnungFindByPrimaryKeyOhneExc(iId);
			if (rech == null)
				return null;
			if (rech.getRechnungartCNr().toLowerCase().contains("gutschrift"))
				return new Object[] { rech };
			return new Object[] { IGNORE_FLAG };
		}
		if (belegart.equals(BELEGART_PROFORMARECHNG)) {
			RechnungDto rech = getRechnungFac().rechnungFindByPrimaryKeyOhneExc(iId);
			if (rech == null)
				return null;
			if (rech.getRechnungartCNr().equals(BELEGART_PROFORMARECHNG))
				return new Object[] { rech };
			return new Object[] { IGNORE_FLAG };
		}
		if (belegart.equals(BELEGART_RECHPOSITION)) {
			RechnungPositionDto rechpos = getRechnungFac().rechnungPositionFindByPrimaryKeyOhneExc(iId);
			if (rechpos == null)
				return null;
			return new Object[] { rechpos, getDtosByIId(rechpos.getRechnungIId(), BELEGART_RECHNUNG)[0] };
		}
		if (belegart.equals(BELEGART_GUTSPOSITION)) {
			RechnungPositionDto rechpos = getRechnungFac().rechnungPositionFindByPrimaryKeyOhneExc(iId);
			if (rechpos == null)
				return null;
			return new Object[] { rechpos, getDtosByIId(rechpos.getRechnungIId(), BELEGART_GUTSCHRIFT)[0] };
		}
		if (belegart.equals(BELEGART_PROFORMAPOS)) {
			RechnungPositionDto rechpos = getRechnungFac().rechnungPositionFindByPrimaryKeyOhneExc(iId);
			if (rechpos == null)
				return null;
			return new Object[] { rechpos, getDtosByIId(rechpos.getRechnungIId(), BELEGART_PROFORMARECHNG)[0] };
		}
		if (belegart.equals(BELEGART_REKLAMATION)) {
			ReklamationDto rekl = getReklamationFac().reklamationFindByPrimaryKeyOhneExc(iId);
			return new Object[] { rekl };
		}
		return null;
	}

	private Object[] getDtosByJCR(JCRDocDto jcr, String basePath, String belegart) {
		String mandantCNr = getMandantFromJCR(jcr);
		if (belegart.equals(BELEGART_UVA)) {

			ArrayList<String> values = getStringsFromPath(basePath, 2, 5, 4);

			Iterator<String> iter = values.iterator();
			int jahr = Integer.parseInt(iter.next());
			int periode = Integer.parseInt(iter.next());
			String name = iter.next();

			PartnerDto partner = getPartner(name);
			if (partner == null)
				return null;

			ReportUvaKriterienDto uva = new ReportUvaKriterienDto();
			FinanzamtDto fAmt = getFinanzFac().finanzamtFindByPartnerIIdMandantCNrOhneExcWithNull(partner.getIId(),
					mandantCNr);
			if (fAmt == null)
				return null;
			fAmt.setMandantCNr(mandantCNr);
			fAmt.setPartnerDto(partner);
			uva.setIGeschaeftsjahr(jahr);
			uva.setIPeriode(periode);
			uva.setSPeriode(String.valueOf(periode));
			uva.setFinanzamtIId(partner.getIId());

			return new Object[] { uva, fAmt };
		}
		if (belegart.equals(BELEGART_SALDENLISTE)) {

			ArrayList<String> values = getStringsFromPath(basePath, 2, 5, 4);

			Iterator<String> iter = values.iterator();

			ReportSaldenlisteKriterienDto krit = new ReportSaldenlisteKriterienDto();
			krit.setIGeschaeftsjahr(Integer.parseInt(iter.next()));
			krit.setIPeriode(Integer.parseInt(iter.next()));
			krit.setKontotypCNr(iter.next());

			return new Object[] { krit, mandantCNr };
		}
		if (belegart.equals(BELEGART_KASSENBUCH)) {
			ArrayList<String> values = getStringsFromPath(basePath, 2, 4);

			Iterator<String> iter = values.iterator();
			String mandant = getMandantFromJCR(jcr);
			PrintKontoblaetterModel kbModel = new PrintKontoblaetterModel();
			kbModel.setGeschaeftsjahrString(iter.next());
			kbModel.setPeriodeImGJ(Integer.parseInt(iter.next()));
			return new Object[] { kbModel, mandant };
		}
		if (belegart.equals(BELEGART_LAGERSTANDSLISTE)) {
			return new Object[] { getMandantFromJCR(jcr) };
		}
		if (belegart.equals(BELEGART_PARTNER)) {
			ArrayList<String> values = getStringsFromPath(basePath, 3);
			PartnerDto partner = getPartner(values.get(0));

			return new Object[] { partner };
		}
		if (belegart.equals(BELEGART_PERSONAL)) {

			ArrayList<String> values = getStringsFromPath(basePath, 3);

			BenutzerDto b = getBenutzerFac().benutzerFindByCBenutzerkennungOhneExc(values.get(0));
			BenutzermandantsystemrolleDto[] bmr = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIIdOhneExc(b.getIId());
			if (bmr == null)
				return null;
			for (BenutzermandantsystemrolleDto benutzermandantsystemrolleDto : bmr) {
				if (benutzermandantsystemrolleDto.getMandantCNr().equals(mandantCNr)) {
					PersonalDto pers = getPersonalFac().personalFindByPrimaryKeySmallOhneExc(
							benutzermandantsystemrolleDto.getPersonalIIdZugeordnet());
					return new Object[] { pers,
							getPartnerFac().partnerFindByPrimaryKeySmallOhneExc(pers.getPartnerIId()) };
				}
			}
		}
		if (belegart.equals(BELEGART_BENUTZER)) {
			return new Object[] { getBenutzerFac().benutzerFindByCBenutzerkennungOhneExc(jcr.getsBelegnummer()) };
		}
		if (belegart.equals(BELEGART_KUNDE)) {
			ArrayList<String> values = getStringsFromPath(basePath, 3);
			PartnerDto partner = getPartner(values.get(0));
			if (partner == null)
				return null;
			return new Object[] { getKundeFac().kundeFindByPrimaryKeySmallWithNull(partner.getIId()), partner };
		}
		if (belegart.equals(BELEGART_LIEFERANT)) {
			ArrayList<String> values = getStringsFromPath(basePath, 3);
			PartnerDto partner = getPartner(values.get(0));
			if (partner == null)
				return null;
			return new Object[] { getLieferantFac().lieferantFindByPrimaryKeySmallWithNull(partner.getIId()), partner };
		}
		if (belegart.equals(BELEGART_PROJEKT)) {
			Iterator<String> iter = getStringsFromPath(basePath, 2, 3).iterator();
			String bereichBez = iter.next();
			String projektCNr = iter.next().replace(".", "/");

			ProjektDto[] projektDtos = getProjektFac().projektFindByCNrMandantCNr(projektCNr, mandantCNr);
			if (projektDtos == null || projektDtos.length != 1) {
				return null;
			}
			BereichDto bereich = getProjektServiceFac().bereichFindByPrimaryKey(projektDtos[0].getBereichIId());
			if (bereichBez.equals(bereich.getCBez())) {
				return new Object[] { projektDtos[0], bereich };
			}
			return null;
		}
		if (belegart.equals(BELEGART_PROJHISTORY)) {
			try {
				return getDtosByIId(Integer.parseInt(jcr.getsBelegnummer()), belegart);
			} catch (NumberFormatException ex) {
				return null;
			}
		}

		return null;
	}

	private PartnerDto[] getClosestPartners(String name) {
		PartnerDto[] partners;
		if (name.equals("Finanzamt DE")) {
			name.getClass();
		}
		while (true) {
			partners = getPartnerFac().partnerFindByName1Lower(name + "%");
			if (partners != null && partners.length > 0) {
				return partners;
			}
			if (name.lastIndexOf(" ") == -1) {
				return null;
			}
			name = name.substring(0, name.lastIndexOf(" "));
		}
	}

	private PartnerDto getPartner(String name) {
		ArrayList<PartnerDto> partner = new ArrayList<PartnerDto>();
		PartnerDto[] closestPartners = getClosestPartners(name);
		if (closestPartners == null)
			return null;
		for (PartnerDto p : closestPartners) {
			String sKunde = p.getCName1nachnamefirmazeile1().replace("/", ".");
			if (p.getCName2vornamefirmazeile2() != null) {
				sKunde = sKunde + " " + p.getCName2vornamefirmazeile2().replace("/", ".");
			}
			if (sKunde.equals(name))
				partner.add(p);
		}
		if (partner.size() != 1)
			return null;
		return partner.get(0);
	}

	private ArrayList<String> getStringsFromPath(String path, int... index) {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		String[] parts = path.split("/");
		ArrayList<String> values = new ArrayList<String>();

		for (int i : index) {
			values.add(parts[i].trim());
		}
		return values;
	}

	private Object[] getDtosByCNrMandantCNr(String cNr, String mandantCNr, String belegart) {
		if (belegart.equals(BELEGART_ANGEBOT)) {
			return new Object[] { getAngebotFac().angebotFindByCNrMandantCNrOhneEx(cNr, mandantCNr) };
		}
		if (belegart.equals(BELEGART_AGSTUECKLISTE)) {
			return new Object[] { getAngebotstklFac().agstklFindByCNrMandantCNrOhneExc(cNr, mandantCNr) };
		}
		if (belegart.equals(BELEGART_ANFRAGE)) {
			return new Object[] { getAnfrageFac().anfrageFindByCNrMandantCNrOhneExc(cNr, mandantCNr) };
		}
		// if(belegart.equals(BELEGART_ARTIKEL)) {
		// return new
		// Object[]{getArtikelFac().artikelFindByCNrMandantCNrOhneExc(cNr,
		// mandantCNr)};
		// }
		if (belegart.equals(BELEGART_AUFTRAG)) {
			return new Object[] { getAuftragFac().auftragFindByMandantCNrCNrOhneExc(mandantCNr, cNr) };
		}
		if (belegart.equals(BELEGART_BESTELLUNG)) {
			return new Object[] { getBestellungFac().bestellungFindByCNrMandantCNr(cNr, mandantCNr) };
		}
		if (belegart.equals(BELEGART_EINGANGSRECHNG)) {
			return new Object[] { getEingangsrechnungFac().eingangsrechnungFindByCNrMandantCNr(cNr, mandantCNr,false) };
		}
		if (belegart.equals(BELEGART_LOS)) {

			return new Object[] { getFertigungFac().losFindByCNrMandantCNrOhneExc(cNr, mandantCNr) };
		}
		if (belegart.equals(BELEGART_LIEFERSCHEIN)) {
			return new Object[] { getLieferscheinFac().lieferscheinFindByCNrMandantCNr(cNr, mandantCNr) };
		}
		if (belegart.equals(BELEGART_RECHNUNG)) {
			RechnungDto[] rechnungen = getRechnungFac().rechnungFindByCNrMandantCNrOhneExc(cNr, mandantCNr);
			for (RechnungDto rechnung : rechnungen) {
				if (!(rechnung.getRechnungartCNr().toLowerCase().contains("gutschrift")
						|| rechnung.getRechnungartCNr().equals(BELEGART_PROFORMARECHNG)))
					return new Object[] { rechnung };
			}
			return new Object[] { IGNORE_FLAG };
		}
		if (belegart.equals(BELEGART_GUTSCHRIFT)) {
			RechnungDto[] rechnungen = getRechnungFac().rechnungFindByCNrMandantCNrOhneExc(cNr, mandantCNr);
			for (RechnungDto rechnung : rechnungen) {
				if (rechnung.getRechnungartCNr().toLowerCase().contains("gutschrift")) {
					return new Object[] { rechnung };
				}
			}
			return new Object[] { IGNORE_FLAG };
		}
		if (belegart.equals(BELEGART_PROFORMARECHNG)) {
			RechnungDto[] rechnungen = getRechnungFac().rechnungFindByCNrMandantCNrOhneExc(cNr, mandantCNr);
			for (RechnungDto rechnung : rechnungen) {
				if (rechnung.getRechnungartCNr().equals(BELEGART_PROFORMARECHNG))
					;
				return new Object[] { rechnung };
			}
			return new Object[] { IGNORE_FLAG };
		}
		if (belegart.equals(BELEGART_REKLAMATION)) {
			return new Object[] { getReklamationFac().reklamationFindByCNrMandantCNrOhneExc(cNr, mandantCNr) };
		}
		return null;
	}

	private void addNotFoundToResult(String filePath) {

		String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
		String path = filePath.substring(0, filePath.lastIndexOf("/") + 1);

		result.addNotFoundFile(path, filename);
	}

	private void addFoundToResult(DocPath docPath) {
		String filename = docPath.getLastDocNode().asPath();
		String newPath = docPath.getPathAsString();
		String newVisualPath = docPath.getVisualPathAsString();

		newPath = newPath.substring(0, newPath.lastIndexOf("/") + 1);
		newVisualPath = newVisualPath.substring(0, newVisualPath.lastIndexOf("/"));
		newVisualPath = "<html>" + newVisualPath.replaceAll("/", "<b>/</b>").replaceAll("&#47", "/") + "</html>";
		result.addFoundFile(newPath, newVisualPath, filename);
	}

	@Override
//	@TransactionTimeout(2000)	
	public DocumentResult getAllDocuments() {
		JcrSession jcrSession = new JcrSession();
		try {
			List<JCRDocDto> jcrDocs = null;
			try {
				jcrDocs = getAllJCRDocs(jcrSession, JCRDocFac.HELIUMV_NODE + "/#", DOC_LIMIT);
			} catch (Throwable t) {
				t.printStackTrace();
				forcedCloseSession();
				jcrDocs = getAllJCRDocs(jcrSession, JCRDocFac.HELIUMV_NODE + "/#", DOC_LIMIT);
			}
			DocumentResult returnValue = new DocumentResult(PATH_NOT_IMPLEMENTED);
			for (JCRDocDto jcr : jcrDocs) {
				try {
					DocPath dPath = getDocPathFromJCR(jcr);
					if (dPath.getLastDocNode().getVersion() == 1
							&& !dPath.getDocNodeAt(1).asPath().trim().equals("System"))
						returnValue.addNotFoundFile(jcr.getsPath().substring(0, jcr.getsPath().lastIndexOf("/") + 1),
								jcr.getsName());
				} catch (Exception e) {
				}
			}
			return returnValue;
		} catch (Throwable t) {
			myLogger.error("getAllDocuments", t);
		} finally {
			jcrSession.closeSession();
		}
		return null;
	}

	@Override
	public DokumentgruppierungDto dokumentgruppierungfindbyPrimaryKeyOhneExc(
			DokumentgruppierungPK dokumentgruppierungPK) {
		Dokumentgruppierung dokumentgruppierung = em.find(Dokumentgruppierung.class, dokumentgruppierungPK);
		if (dokumentgruppierung == null)
			return null;

		return new DokumentgruppierungDto(dokumentgruppierung.getMandantCNr(), dokumentgruppierung.getCNr());
	}

	@Override
	public DokumentbelegartDto dokumentbelegartfindbyPrimaryKeyOhneExc(DokumentbelegartPK dokumentbelegartPK) {
		Dokumentbelegart dokumentbelegart = em.find(Dokumentbelegart.class, dokumentbelegartPK);
		if (dokumentbelegart == null)
			return null;

		return new DokumentbelegartDto(dokumentbelegart.getMandantCNr(), dokumentbelegart.getCNr());
	}

	@Override
	public TreeMap<String, List<JCRDocDto>> getLosablieferungsDokumente(
			Integer losId, String filterDokuBelegart, String filterGruppierung, 
			boolean alleVersionen, TheClientDto theClientDto) throws RemoteException {
		LosDto losDto = getFertigungFac().losFindByPrimaryKey(losId);

		GesamtkalkulationDto gkDto = getFertigungReportFac().getDatenGesamtkalkulation(losDto.getIId(),
				null, 99, theClientDto);

		HashSet<Integer> knownWEPs = new HashSet<Integer>();
		
		TreeMap<String, List<JCRDocDto>> alDokumente = new TreeMap<String, List<JCRDocDto>>();
		JcrSession jcrSession = new JcrSession();
		try {
			for (Object[] zeileGK : (ArrayList<Object[]>) gkDto.getAlDaten()) {
				String belegartZugang = (String) zeileGK[FertigungReportFac.GESAMTKALKULATION_BELEGART_ZUGANG];
				Integer belegartpositionIIdZugang = (Integer) zeileGK[FertigungReportFac.GESAMTKALKULATION_BELEGARTPOSITION_I_ID_ZUGANG];
	
				String artikelnummer = (String) zeileGK[FertigungReportFac.GESAMTKALKULATION_ARTIKELNUMMER];
	
				if (belegartZugang != null && belegartZugang.equals(LocaleFac.BELEGART_BESTELLUNG.trim())
						&& belegartpositionIIdZugang != null) {
	
					WareneingangspositionDto wepDto = getWareneingangFac()
							.wareneingangspositionFindByPrimaryKeyOhneExc(belegartpositionIIdZugang);
					if (wepDto != null) {
						if (!knownWEPs.contains(wepDto.getIId())) {
							WareneingangDto weDto = getWareneingangFac()
									.wareneingangFindByPrimaryKey(wepDto.getWareneingangIId());
	
							BestellungDto bsDto = getBestellungFac()
									.bestellungFindByPrimaryKey(weDto.getBestellungIId());
	
							myLogger.info("Artikel:" + artikelnummer + ", Bestellung: " + bsDto.getCNr() + ", wepId=" + wepDto.getIId());
							knownWEPs.add(wepDto.getIId());
	
							DocPath docPath = new DocPath(new DocNodeWEPosition(wepDto, weDto, bsDto));
							try {
								List<DocNodeBase> docs = getDocNodeChildrenFromNode(
										jcrSession, docPath, theClientDto);
								myLogger.info("  docs-size: " + docs.size());
								for (DocNodeBase base : docs) {								
									if (base.getNodeType() == DocNodeBase.FILE) {
	
										JCRDocDto jcrDocDto = ((DocNodeFile) base).getJcrDocDto();
	
										if (filterDokuBelegart != null) {
											if (jcrDocDto.getsBelegart() == null || !jcrDocDto.getsBelegart()
													.equals(filterDokuBelegart)) {
												continue;
											}
										}
	
										if (filterGruppierung != null) {
											if (jcrDocDto.getsGruppierung() == null
													|| !jcrDocDto.getsGruppierung().equals(filterGruppierung)) {
												continue;
											}
										}
	
										List<JCRDocDto> alDokumenteEinesArtikels = null;
	
										if (alDokumente.containsKey(artikelnummer)) {
											alDokumenteEinesArtikels = alDokumente.get(artikelnummer);
										} else {
											alDokumenteEinesArtikels = new ArrayList<JCRDocDto>();
										}
	
										if (alleVersionen) {
											ArrayList<DocNodeVersion> versions = getAllVersions(jcrSession, jcrDocDto);
	
											for (int j = 0; j < versions.size(); j++) {
												JCRDocDto jcrDocDtoVersion = versions.get(j).getJCRDocDto();
												JCRDocDto jcrData = getData(jcrSession, jcrDocDtoVersion);
												alDokumenteEinesArtikels.add(jcrData);
												
												myLogger.info("Node Version: " + jcrDocDtoVersion.getlVersion() + 
														"(" + (j+1) + "/" + versions.size() + "), " + jcrDocDtoVersion.getsPath() + ".");
											}
										} else {
											DocNodeVersion version = getLastVersionOfJcrDoc(jcrDocDto);
											JCRDocDto jcrData = getData(jcrSession, version.getJCRDocDto());
											alDokumenteEinesArtikels.add(jcrData);
										}
	
										alDokumente.put(artikelnummer, alDokumenteEinesArtikels);
									}
								}
							} catch (Throwable t) {
								myLogger.error("Ignored throwable", t);
							}
						}
					}
				}
			}
		} finally {
			jcrSession.closeSession();
		}
		
		return alDokumente;
	}
	
	class BelegKennungObj {
		private String belegartCnr;
		private Integer belegId;
		private String kurzbezeichnung;
		private String belegCnr;

		private BelegKennungObj() {
		}

		public String belegartCnr() {
			return belegartCnr;
		}

		public String cnr() {
			return belegCnr;
		}

		public Integer id() {
			return belegId;
		}

		public String kurzbezeichnung() {
			return kurzbezeichnung;
		}

		public BelegKennungObj create(String belegartCnr, Integer belegId, TheClientDto theClientDto)
				throws RemoteException {
			BelegartDto belegartDto = getLocaleFac().belegartFindByPrimaryKey(belegartCnr, theClientDto);

			this.belegartCnr = belegartDto.getCNr();
			this.kurzbezeichnung = belegartDto.getCKurzbezeichnung();
			this.belegId = belegId;
			this.belegCnr = buildCnr();
			return this;
		}

		public String kennung() {
			return this.kurzbezeichnung + " " + this.belegCnr;
		}

		private String buildCnr() throws RemoteException {
			if (LocaleFac.BELEGART_ANFRAGE.equals(belegartCnr)) {
				Anfrage anfrage = em.find(Anfrage.class, belegId);
				return anfrage.getCNr();
			}
			if (LocaleFac.BELEGART_AGSTUECKLISTE.equals(belegartCnr)) {
				Agstkl agstkl = em.find(Agstkl.class, belegId);
				return agstkl.getCNr();
			}
			if (LocaleFac.BELEGART_ANGEBOT.equals(belegartCnr)) {
				Angebot angebot = em.find(Angebot.class, belegId);
				return angebot.getCNr();
			}
			if (LocaleFac.BELEGART_AUFTRAG.equals(belegartCnr)) {
				Auftrag auftrag = em.find(Auftrag.class, belegId);
				return auftrag.getCNr();
			}
			if (LocaleFac.BELEGART_BESTELLUNG.equals(belegartCnr)) {
				Bestellung bestellung = em.find(Bestellung.class, belegId);
				return bestellung.getCNr();
			}
			if (LocaleFac.BELEGART_GUTSCHRIFT.equals(belegartCnr)) {
				Rechnung rechnung = em.find(Rechnung.class, belegId);
				return rechnung.getCNr();
			}
			if (LocaleFac.BELEGART_INSERAT.equals(belegartCnr)) {
				Inserat inserat = em.find(Inserat.class, belegId);
				return inserat.getCNr();
			}
			if (LocaleFac.BELEGART_LIEFERSCHEIN.equals(belegartCnr)) {
				Lieferschein lieferschein = em.find(Lieferschein.class, belegId);
				return lieferschein.getCNr();
			}
			if (LocaleFac.BELEGART_LOS.equals(belegartCnr)) {
				Los los = em.find(Los.class, belegId);
				return los.getCNr();
			}
			if (LocaleFac.BELEGART_PROFORMARECHNUNG.equals(belegartCnr)) {
				Rechnung rechnung = em.find(Rechnung.class, belegId);
				return rechnung.getCNr();
			}
			if (LocaleFac.BELEGART_PROJEKT.equals(belegartCnr)) {
				Projekt projekt = em.find(Projekt.class, belegId);
				return projekt.getCNr();
			}
			if (LocaleFac.BELEGART_RECHNUNG.equals(belegartCnr)) {
				Rechnung rechnung = em.find(Rechnung.class, belegId);
				return rechnung.getCNr();
			}
			if (LocaleFac.BELEGART_REKLAMATION.equals(belegartCnr)) {
				Reklamation reklamation = em.find(Reklamation.class, belegId);
				return reklamation.getCNr();
			}

			return belegId.toString();
		}
	}
}
