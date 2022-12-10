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

import com.lp.server.system.ejb.EditorBaseBlock;
import com.lp.server.util.EditorBlockIId;
import com.lp.server.util.EditorContentIId;
import com.lp.server.util.HvImageIId;
import com.lp.server.util.HvOptional;
import com.lp.util.EJBExceptionLP;

@Remote
public interface MediaFac {
	public static final int MAX_MEDIASTANDARD_DATEINAME = 260;

	public static final String FLR_MEDIASTANDARD_I_ID = "i_id";
	public static final String FLR_MEDIASTANDARD_C_NR = "c_nr";
	public static final String FLR_MEDIASTANDARD_DATENFORMAT_C_NR = "datenformat_c_nr";
	public static final String FLR_MEDIASTANDARD_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_MEDIASTANDARD_LOCALE_C_NR = "locale_c_nr";
	public static final String FLR_MEDIASTANDARD_B_VERSTECKT = "b_versteckt";
	public static final String DATENFORMAT_MIMETYPE_TEXT_HTML = "text/html";
	public static final String DATENFORMAT_MIMETYPE_IMAGE_JPEG = "image/jpeg";
	public static final String DATENFORMAT_MIMETYPE_IMAGE_GIF = "image/gif";
	public static final String DATENFORMAT_MIMETYPE_IMAGE_PNG = "image/png";
	public static final String DATENFORMAT_MIMETYPE_IMAGE_TIFF = "image/tiff";
	public static final String DATENFORMAT_MIMETYPE_UNBEKANNT = "unbekannt";
	public static final String DATENFORMAT_MIMETYPE_TEXT_PLAIN = "text/plain";
	public static final String DATENFORMAT_MIMETYPE_TEXT_RITCHTEXT = "text/richtext";
	public static final String DATENFORMAT_MIMETYPE_APP_ARCHIVE = "application/java-archive";
	public static final String DATENFORMAT_MIMETYPE_APP_MSWORD = "application/msword";
	public static final String DATENFORMAT_MIMETYPE_APP_PDF = "application/pdf";
	public static final String DATENFORMAT_MIMETYPE_APP_ZIP = "application/zip";
	public static final String DATENFORMAT_MIMETYPE_APP_JASPER = "application/jasper";

	public static final String DATENFORMAT_MIMETYPE_MESSAGE_RFC822 = "message/rfc822";
	public static final String DATENFORMAT_MIMETYPE_APP_MSOUTLOOK = "application/vnp.msoutlook";

	public static final String DATENFORMAT_MIMETYPE_TIKA_MSOFFICE = "application/x-tika-msoffice";

	public static final String DATENFORMAT_MIMETYPEART_IMAGE = "image"; // @todo
	// VF PJ
	// 4459
	public static final String DATENFORMAT_MIMETYPEART_TEXT = "text";
	public static final String DATENFORMAT_MIMETYPEART_APP = "application";

	public static final String MEDIAART_EIGENTUMSVORBEHALT = "Eigentumsvorbehalt";
	public static final String MEDIAART_KOPFTEXT = "Kopftext";
	public static final String MEDIAART_FUSSTEXT = "Fusstext";
	public static final String MEDIAART_FREIERTEXT = "Freier Text";
	public static final String MEDIAART_LIEFERBEDINGUNGEN = "Lieferbedingungen";

	// Fixe Eintraege in der Mediastandard-Tabelle
	public static final String MEDIASTANDARD_EIGENTUMSVORBEHALT = MEDIAART_EIGENTUMSVORBEHALT;
	public static final String MEDIASTANDARD_LIEFERBEDINGUNGEN = MEDIAART_LIEFERBEDINGUNGEN;

	public static final String FLR_MEDIASTANDARD_TEXTSUCHE_C_INHALT_O_MEDIA = "c_inhalt_o_media";

	// fix verdrahtete Texte
	public static final String DEFAULT_LIEFERBEDINGUNGEN = "Unsere Lieferungen und Leistungen erfolgen gem\u00E4\u00DF den allgemeinen Lieferbedingungen (Ausgabe Oktober 1997) und den Softwarebedingungen (Ausgabe Februar 1998) der Elektro- und Elektronikindustrie \u00D6sterreichs.";
	public static final String DEFAULT_EIGENTUMSVORBEHALT = "Alle gelieferten Waren und Dienstleistungen bleiben bis zur vollst\u00E4ndigen Bezahlung unser uneingeschr\u00E4nktes Eigentum.";

	public void createDatenformat(DatenformatDto datenformatDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeDatenformat(DatenformatDto datenformatDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateDatenformat(DatenformatDto datenformatDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public DatenformatDto datenformatFindByPrimaryKey(String cNr) throws EJBExceptionLP, RemoteException;

	public MediastandardDto createMediastandard(MediastandardDto mediastandardDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeMediastandard(MediastandardDto mediastandardDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MediastandardDto updateMediastandard(MediastandardDto mediastandardDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	/**
	 * @deprecated MB: use
	 * @param cNrMediaartI String
	 * @param theClientDto
	 * @return MediastandardDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public MediastandardDto createDefaultMediastandard(String cNrMediaartI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MediastandardDto createDefaultMediastandard(String cNrMediaartI, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MediastandardDto mediastandardFindByPrimaryKey(Integer mediastandardIId)
			throws EJBExceptionLP, RemoteException;

	public DatenformatDto[] eingangsrechnungartFindAll() throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllDatenformat(String cNrSpracheI) throws EJBExceptionLP, RemoteException;

	public String createMediaart(MediaartDto oMediaartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeMediaart(MediaartDto mediaartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateMediaart(MediaartDto mediaartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MediaartDto mediaartFindByPrimaryKey(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String createMediaartspr(MediaartsprDto mediaartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeMediaartspr(MediaartsprDto mediaartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateMediaartspr(MediaartsprDto mediaartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MediaartsprDto mediaartsprFindByPrimaryKey(String mediaartCNr, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public MediaartsprDto getMediaartspr(String cNrI, String sLocUiI, TheClientDto theClientDto) throws RemoteException;

	public MediastandardDto mediastandardFindByCNrDatenformatCNrMandantCNr(String cNrI, String datenformatCNrI,
			String mandantCNrI, String localeCNrI, TheClientDto theClientDto) throws RemoteException;

	public MediastandardDto[] mediastandardFindByDatenformatCNrMandantCNr(String datenformatCNrI, String mandantCNrI,
			TheClientDto theClientDto);

	/**
	 * Textbaustein ermitteln, nach cnr, mandant</br>
	 * 
	 * @param cnr          die Kennung des Textbausteins
	 * @param mandantCnr   der Mandant
	 * @param locale       falls nicht angegeben, wird die Locale des Clients
	 *                     verwendet
	 * @param theClientDto
	 * @return der Textbaustein, oder {link #EJBExceptionLP.FEHLER_BEI_FIND}
	 */
	MediastandardDto mediastandardFindByCnrMandantCNr(String cnr, String mandantCnr, HvOptional<String> locale,
			TheClientDto theClientDto) throws EJBExceptionLP;

	/**
	 * Textbaustein ermitteln, nach cnr, mandant</br>
	 * 
	 * @param cnr          die Kennung des Textbausteins
	 * @param mandantCnr   der Mandant
	 * @param locale       falls nicht angegeben, wird die Locale des Clients
	 *                     verwendet
	 * @param theClientDto
	 * @return der (optionale) Textbaustein
	 */
	HvOptional<MediastandardDto> mediastandardOptFindByCnrMandantCnr(String cnr, String mandantCnr,
			HvOptional<String> locale, TheClientDto theClientDto);

	HvOptional<MediastandardDto> mediastandardOptFindByPrimaryKey(Integer mediastandardIId);

	public String mediastandardTextHtmlFindByCNrMandantCNrLocale(String cNrI, String mandantCNrI, Locale locale);

	HvOptional<EditorContentDto> editorContentFindByPrimaryKey(EditorContentIId id, TheClientDto theClientDto);

	EditorContentDto createEditorContent(EditorContentDto dto, TheClientDto theClientDto);

	EditorContentDto updateEditorContent(EditorContentDto dot, TheClientDto theClientDto);

	/**
	 * Hole einen EditorBlock mit ID. Das ist die einzige Methode, die direkt auf
	 * Block Ebene arbeitet, f&uuml;r update/delete sollte updateEditorContent
	 * verwendet werden, die betroffenen Bl&ouml;cke werden dort richtig bearbeitet
	 * 
	 * @param id
	 * @param theClientDto
	 * @return
	 */
	HvOptional<EditorBaseBlockDto> editorBlockFindByPrimaryKey(EditorBlockIId id, TheClientDto theClientDto);

	void deleteEditorContent(EditorContentIId id, TheClientDto theClientDto);

	void deleteEditorBlock(EditorBlockIId id, TheClientDto theClientDto);

	HvImageDto createHvImage(HvImageDto dto, TheClientDto theClientDto);

	void updateHvImage(HvImageDto dto, TheClientDto theClientDto);

	void deleteHvImage(HvImageIId imageIId, TheClientDto theClientDto);

	HvOptional<HvImageDto> hvImageFindByPrimaryKey(HvImageIId id);
}
