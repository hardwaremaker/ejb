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
package com.lp.server.system.jcr.service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.ejb.Remote;
import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.jcr.ejb.DokumentbelegartPK;
import com.lp.server.system.jcr.ejb.DokumentgruppierungPK;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeVersion;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface JCRDocFac {

	public final static String HELIUMV_NODE = "HELIUMV";

	// Konstanten definieren fuer Propertynamen
	public final static String PROPERTY_BELEGART = "BELEGART";
	public final static String PROPERTY_ANLEGER = "ANLEGER";
	public final static String PROPERTY_PARTNER = "PARTNER";
	public final static String PROPERTY_ZEITPUNKT = "ZEITPUNKT";
	public final static String PROPERTY_BELEGNUMMER = "BELEGNUMMER";
	public final static String PROPERTY_SCHLAGWORTE = "SCHLAGWORTE";
	public final static String PROPERTY_NAME = "NAME";
	public final static String PROPERTY_FILENAME = "FILENAME";
	public final static String PROPERTY_TABLE = "TABLE";
	public final static String PROPERTY_ROW = "ROW";
	public final static String PROPERTY_MIME = "MIME";
	public final static String PROPERTY_VERSION = "VERSION";
	public final static String PROPERTY_VERSTECKT = "VERSTECKT";
	public final static String PROPERTY_SICHERHEITSSTUFE = "SICHERHEITSSTUFE";
	public final static String PROPERTY_GRUPPIERUNG = "GRUPPIERUNG";
	public final static String PROPERTY_DATA = "DATA";

	// Sicherheitsstufen
	public final static long SECURITY_NONE = 0;
	public final static long SECURITY_LOW = 1;
	public final static long SECURITY_MEDIUM = 2;
	public final static long SECURITY_HIGH = 3;
	public final static long SECURITY_ARCHIV = 99;

	public final static String DEFAULT_ARCHIV_GRUPPE = "Archiv";
	public final static String DEFAULT_ARCHIV_BELEGART = "archivierter Druck";
	public final static String DEFAULT_KOPIE_GRUPPE = "Dokumente";
	public final static String DEFAULT_KOPIE_BELEGART = "kopiertes Dokument";
	public final static String DEFAULT_VERSANDAUFTRAG_BELEGART = "Versandauftrag";
	public final static String DEFAULT_VERSANDAUFTRAG_GRUPPE = "Versandauftrag";
	public final static String DEFAULT_EMAIL_BELEGART = "E-Mail";

	/**
	 * Argument n&uuml;tzlich f&uuml;r <code>runPflege(TheClientDto, String...args)</code><br>
	 * Bei &UUml;bergabe werden bereits existierende Dokumente &uuml;berschrieben.
	 */
	public static final String ARG_OVERWRITEEXISTING = "OVERRIDEEXISTING";
	/**
	 * Argument n&uuml;tzlich f&uuml;r <code>runPflege(TheClientDto, String...args)</code><br>
	 * Bei &UUml;bergabe werden bereits existierende Dokumente &uuml;bersprungen.
	 */
	public static final String ARG_SKIPEXISTING = "SKIPEXISTING";

	/**
	 * Argument n&uuml;tzlich f&uuml;r <code>runPflege(TheClientDto, String...args)</code><br>
	 * Bei &UUml;bergabe werden die Dokumente nur durchsucht,
	 * aber nicht in den neuen Pfad geschrieben!
	 */
	public static final String ARG_READONLY = "READONLY";
	
	/**
	 * Argument n&uuml;tzlich f&uuml;r <code>runPflege(TheClientDto, String...args)</code><br>
	 * Bei &UUml;bergabe werden zuerst alle Dateien gel&ouml;scht, die sich breits im neuen Ordner des
	 * Beleges befinden.<br>Also <b>ALLES</b> was sich zBsp. in folgendem Pfad befindet:<br>
	 * HELIUMV<b>/</b>001<b>/</b>Angebot<b>/</b>Angebot<b>/</b><br><br>
	 * Praktisch falls bei einem vorherigem Durchlauf falsche Dateinamen verwendet wurden.
	 */
	public static final String ARG_CLEARNEWFOLDERS = "CLEARFOLDERS";
	
	/**
	 * Argument n&uuml;tzlich f&uuml;r <code>runPflege(TheClientDto, String...args)</code><br>
	 * Durch die &UUml;bergabe werden die alten Dokumente gel&ouml;scht.<br>
	 * Sollte verwendet werden! Sonst verdoppelt sich der Speicherverbrauch!
	 */
	public static final String ARG_DELETEOLDFILES = "DELETEFILES";
	
	
	public static final String ARG_STARTINDEX = "STARTINDEX=";
	
	public static final String SAVE_AS_ORPHAN = "saveAsOrphan";
	
	public DocumentResult runPflege(TheClientDto theClientDto, String[] args);

	public List<?> getDtoMatches(String path, String belegart, String searchKey);

	public int removeEmptyNodes();

	public DocumentResult applyDtoTo(String path, List<String> documents, String belegart, Object dto, TheClientDto theClientDto);
	
	public DocumentResult getAllDocuments();

	public void fuehreDokumenteZusammen(KundeDto zielDto, KundeDto quellDto);
	public void fuehreDokumenteZusammen(LieferantDto zielDto, LieferantDto quellDto);
	public void fuehreDokumenteZusammen(PartnerDto zielDto, PartnerDto quellDto);
	
	public void setVisibilityOfDocument(String basePath, String versionPath, boolean hidden);

	void addNewDocumentOrNewVersionOfDocument(
			JCRDocDto jcrDocDto, TheClientDto theClientDto);
	void addNewDocumentOrNewVersionOfDocumentWithinTransaction(
			JCRDocDto jcrDocDto, TheClientDto theClientDto) ;
	void addNewDocumentOrNewVersionOfDocumentWithinTransaction(
			List<JCRDocDto> dtos, TheClientDto theClientDto);
	
	public Node getNode(String sFullPath);

	public void removeNode(String path) throws ItemNotFoundException,
	AccessDeniedException, RepositoryException ;
	
	/**
	 * Liefert alle JCRDocDto's die im angegeben Pfad gespeichert sind.<br>
	 * M&ouml;gliche Pfadangaben k&ouml;nnen so aussehen:
	 * <b>HELIUMV/001/Angebot/Angebot/<br>
	 * HELUMV/001/Angebot/Angebot/*&#47Angebotpositionen/</b><br>
	 * HELUMV/001/Angebot/Angebot/*&#47Angebotpositionen/#</b><br>
	 * Der Operator * steht f&uuml;r alle Ordner die in dieser Ebene vorhanden sind.<br>
	 * Der Operator # zum Schluss gibt an, dass auch alle Unterordner durchsucht werden sollten.
	 */
//	public ArrayList<JCRDocDto> getAllJCRDocs(String path, int limit);

	public ArrayList<DocNodeVersion> getAllVersions(JCRDocDto jcrDocDto);

	public ArrayList<DocNodeVersion> getAllVersionsSession(JCRDocDto jcrDocDto);

	public DokumentgruppierungDto[] dokumentgruppierungfindbyMandant(
			String mandantCNr);

	public DokumentbelegartDto[] dokumentbelegartfindbyMandant(String mandantCNr);

//	public boolean checkIfNodeExists(String sFullPath);
	
//	public boolean checkIfNodeExists(DocPath docPath);
	public JCRRepoInfo checkIfNodeExists(DocPath dPath) ;

	public boolean isOnline();
	
	public DokumentgruppierungDto dokumentgruppierungfindbyPrimaryKey(
			DokumentgruppierungPK dokumentgruppierungPK);

	public DokumentbelegartDto dokumentbelegartfindbyPrimaryKey(
			DokumentbelegartPK dokumentbelegartPK);

	public void createDokumentgruppierung(
			DokumentgruppierungDto dokumentgruppierungDto);

	public void createDokumentbelegart(DokumentbelegartDto dokumentbelegartDto);

	public long getNextVersionNumer(JCRDocDto jcrDocDto);

	public DokumentnichtarchiviertDto dokumentnichtarchiviertfindbyMandantCReportname(
			String mandantCNr, String CReportname);

	public void kopiereAlteDokumenteInJCR(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public JCRDocDto getJCRDocDtoFromNode(DocPath docPath) throws RepositoryException, IOException;
	
	public ArrayList<JCRDocDto> getJCRDocDtoFromNodeChildren(DocPath docPath) throws RepositoryException, IOException;
	
	public List<DocNodeBase> getDocNodeChildrenFromNode(DocPath docPath, TheClientDto theClientDto) throws RepositoryException, IOException;
	
	public PrintInfoDto getPathAndPartnerAndTable(Object sKey, Integer idUsecase,
			TheClientDto theClientDto);
	
	public JCRDocDto getData(JCRDocDto jcrDocDto);

	public void kopiereVersandauftraegeInJCR(TheClientDto theClient)
			throws EJBExceptionLP, RemoteException;

	public void removeDokumentbelegart(DokumentbelegartDto dokumentbelegartDto);


	public int removeAllEmptyNodes();
	
	public int removeAllEmptyNodes(String path);
	
	public void removeNodeAndAllChildlessParents(String path);
	
	public void removeDokumentgruppierung(
			DokumentgruppierungDto dokumentgruppierungDto);

	public VersandanhangDto getDataForVersandanhangFromJCR(
			VersandanhangDto versandanhangDto, TheClientDto theClientDto);

	public VersandauftragDto getDataForVersandauftragFromJCR(
			VersandauftragDto versandauftragDto, TheClientDto theClientDto);

	public void saveVersandauftragAsDocument(VersandauftragDto versandauftrag,
			boolean setOInhaltNull, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void saveVersandanhangAsDocument(VersandanhangDto versandanhangDto,
			boolean setOInhaltNull, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<DocPath> searchDocNodes(String sToSearch, DocPath startDocPath);
	
	public DocPath getDocPathFromJCR(JCRDocDto jcrDocDto) throws ValueFormatException, PathNotFoundException, RepositoryException, IOException;

	public ArrayList<JCRDocDto> holeDokumenteZuUseCase(Integer key,
			int useCaseId, TheClientDto theClientDto);
	
	public void deaktiviereArchivierung(
			String mandantCNr, String CReportname);
	
	public void verschiebeBzwKopiereDokumentInAnderenDocPath(DocPath quellePath, DocPath zielPath);
	
	void updateVersionOfDocument(
			JCRDocDto jcrDocDto, String jcrVersion, TheClientDto theClientDto);
	
	void updateVersionOfDocumentWithinTransaction(
			JCRDocDto jcrDocDto, String jcrVersion, TheClientDto theClientDto);

	DocNodeVersion getLastVersionOfJcrDoc(JCRDocDto jcrDocDto);

	DokumentgruppierungDto dokumentgruppierungfindbyPrimaryKeyOhneExc(
			DokumentgruppierungPK dokumentgruppierungPK);

	DokumentbelegartDto dokumentbelegartfindbyPrimaryKeyOhneExc(
			DokumentbelegartPK dokumentbelegartPK);

	void saveVersandanhangsAsDocument(List<VersandanhangDto> dtos, 
			boolean clearContent, TheClientDto theClientDto)
			throws RemoteException;
	
	public void verschiebeBzwKopiereDokumentInAnderenDocPath(DocPath quellePath, DocPath zielPath, boolean withSubNodes);

	void saveVersandauftragsAsDocument(List<VersandauftragDto> versandauftraege, boolean setOInhaltNull,
			TheClientDto theClientDto) throws RemoteException;

	TreeMap<String, List<JCRDocDto>> getLosablieferungsDokumente(Integer losId, String filterDokuBelegart,
			String filterGruppierung, boolean alleVersionen, TheClientDto theClientDto) throws RemoteException;
}
