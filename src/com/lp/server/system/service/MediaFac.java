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
package com.lp.server.system.service;

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.util.EJBExceptionLP;

@Remote
public interface MediaFac {
	public final static int MAX_MEDIASTANDARD_DATEINAME = 260;

	public static final String FLR_MEDIASTANDARD_I_ID = "i_id";
	public static final String FLR_MEDIASTANDARD_C_NR = "c_nr";
	public static final String FLR_MEDIASTANDARD_DATENFORMAT_C_NR = "datenformat_c_nr";
	public static final String FLR_MEDIASTANDARD_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_MEDIASTANDARD_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_MEDIASTANDARD_B_VERSTECKT = "b_versteckt";
	public final static String DATENFORMAT_MIMETYPE_TEXT_HTML = "text/html      ";
	public final static String DATENFORMAT_MIMETYPE_IMAGE_JPEG = "image/jpeg     ";
	public final static String DATENFORMAT_MIMETYPE_IMAGE_GIF = "image/gif      ";
	public final static String DATENFORMAT_MIMETYPE_IMAGE_PNG = "image/png      ";
	public final static String DATENFORMAT_MIMETYPE_IMAGE_TIFF = "image/tiff     ";
	public final static String DATENFORMAT_MIMETYPE_UNBEKANNT = "unbekannt      ";
	public final static String DATENFORMAT_MIMETYPE_TEXT_PLAIN = "text/plain";
	public final static String DATENFORMAT_MIMETYPE_TEXT_RITCHTEXT = "text/richtext";
	public final static String DATENFORMAT_MIMETYPE_APP_ARCHIVE = "application/java-archive";
	public final static String DATENFORMAT_MIMETYPE_APP_MSWORD = "application/msword";
	public final static String DATENFORMAT_MIMETYPE_APP_PDF = "application/pdf";
	public final static String DATENFORMAT_MIMETYPE_APP_ZIP = "application/zip";
	public final static String DATENFORMAT_MIMETYPE_APP_JASPER = "application/jasper";

	public final static String DATENFORMAT_MIMETYPEART_IMAGE = "image"; // @todo
	// VF PJ
	// 4459
	public final static String DATENFORMAT_MIMETYPEART_TEXT = "text";
	public final static String DATENFORMAT_MIMETYPEART_APP = "application";

	public final static String MEDIAART_EIGENTUMSVORBEHALT = "Eigentumsvorbehalt";
	public final static String MEDIAART_KOPFTEXT = "Kopftext";
	public final static String MEDIAART_FUSSTEXT = "Fusstext";
	public final static String MEDIAART_FREIERTEXT = "Freier Text";
	public final static String MEDIAART_LIEFERBEDINGUNGEN = "Lieferbedingungen";

	// Fixe Eintraege in der Mediastandard-Tabelle
	public final static String MEDIASTANDARD_EIGENTUMSVORBEHALT = MEDIAART_EIGENTUMSVORBEHALT;
	public final static String MEDIASTANDARD_LIEFERBEDINGUNGEN = MEDIAART_LIEFERBEDINGUNGEN;

	// fix verdrahtete Texte
	public final static String DEFAULT_LIEFERBEDINGUNGEN = "Unsere Lieferungen und Leistungen erfolgen gem\u00E4\u00DF den allgemeinen Lieferbedingungen (Ausgabe Oktober 1997) und den Softwarebedingungen (Ausgabe Februar 1998) der Elektro- und Elektronikindustrie \u00D6sterreichs.";
	public final static String DEFAULT_EIGENTUMSVORBEHALT = "Alle gelieferten Waren und Dienstleistungen bleiben bis zur vollst\u00E4ndigen Bezahlung unser uneingeschr\u00E4nktes Eigentum.";

	public void createDatenformat(DatenformatDto datenformatDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeDatenformat(DatenformatDto datenformatDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateDatenformat(DatenformatDto datenformatDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public DatenformatDto datenformatFindByPrimaryKey(String cNr)
			throws EJBExceptionLP, RemoteException;

	public MediastandardDto createMediastandard(
			MediastandardDto mediastandardDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeMediastandard(MediastandardDto mediastandardDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MediastandardDto updateMediastandard(
			MediastandardDto mediastandardDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	/**
	 * @deprecated MB: use
	 * @param cNrMediaartI
	 *            String
	 * @param theClientDto
	 * @return MediastandardDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public MediastandardDto createDefaultMediastandard(String cNrMediaartI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MediastandardDto createDefaultMediastandard(String cNrMediaartI,
			String localeCNr, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public MediastandardDto mediastandardFindByPrimaryKey(
			Integer mediastandardIId) throws EJBExceptionLP, RemoteException;

	public DatenformatDto[] eingangsrechnungartFindAll() throws EJBExceptionLP,
			RemoteException;

	public Map<?, ?> getAllDatenformat(String cNrSpracheI) throws EJBExceptionLP,
			RemoteException;

	public String createMediaart(MediaartDto oMediaartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeMediaart(MediaartDto mediaartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateMediaart(MediaartDto mediaartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MediaartDto mediaartFindByPrimaryKey(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String createMediaartspr(MediaartsprDto mediaartsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeMediaartspr(MediaartsprDto mediaartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateMediaartspr(MediaartsprDto mediaartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MediaartsprDto mediaartsprFindByPrimaryKey(String mediaartCNr,
			String localeCNr, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public MediaartsprDto getMediaartspr(String cNrI, String sLocUiI,
			TheClientDto theClientDto) throws RemoteException;

	public MediastandardDto mediastandardFindByCNrDatenformatCNrMandantCNr(
			String cNrI, String datenformatCNrI, String mandantCNrI,
			String localeCNrI, TheClientDto theClientDto) throws RemoteException;
	public MediastandardDto[] mediastandardFindByDatenformatCNrMandantCNr(
			String datenformatCNrI, String mandantCNrI,
			TheClientDto theClientDto);
	public String mediastandardTextHtmlFindByCNrMandantCNrLocale(
			String cNrI,  String mandantCNrI,
			Locale locale);
	
}
