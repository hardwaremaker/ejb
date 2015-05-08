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
 *******************************************************************************/
package com.lp.server.media.service;

import java.io.IOException;

import javax.ejb.Remote;
import javax.jcr.RepositoryException;
import javax.jms.JMSException;
import javax.mail.MessagingException;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface EmailMediaFac {	
	/**
	 * Queuename mit der der HV Server Fortschrittsinformation an den Client &uuml;bermittelt</br>
	 * <p>Achtung: Eine &Auml;nderung des Queuenamens muss auch in der Server-Config durchgef&uuml;hrt werden</p>
	 */
	public final static String PROGRESS_QUEUENAME = "queue/EmailMediaProgressQueue" ;
	
	/**
	 * Queuename mit der der HV Client dem Server mitteilen kann, das die laufende Aktion abgebrochen werden soll
	 * <p>Achtung: Eine &Auml;nderung des Queuenamens muss auch in der Server-Config durchgef&uuml;hrt werden</p>
	 */
	public final static String CANCEL_QUEUENAME   = "queue/EmailMediaCancelQueue" ;
	
	public final class CancelMethod {
		public final static String RETRIEVE_EMAIL = "retrieveEmail" ;
	} ;
	
	public final static String FLR_MEDIAINBOX = "flrmediainbox" ;
	public final static String FLR_EMAILMETA  = "flrmediaemailmeta" ;
	public final static String EMAILMETA      = "flrmedia" ;
	public final static String FLR_MEDIASTOREBELEG = "flrmediastorebeleg" ;
	
	public final static String FLR_MEDIAINBOX_EMAILMETA_DATEFROM = FLR_MEDIAINBOX + "." + EMAILMETA + "." + "t_emaildate" ;
	public final static String FLR_MEDIAINBOX_EMAILMETA_FROM = FLR_MEDIAINBOX + "." + EMAILMETA + "." + "c_from";
	public final static String FLR_MEDIAINBOX_EMAILMETA_SUBJECT = FLR_MEDIAINBOX + "." + EMAILMETA + "." + "c_subject" ;
	public final static String FLR_MEDIAINBOX_PERSONALID = FLR_MEDIAINBOX + "." + "personal_i_id" ;
	public final static String FLR_MEDIAINBOX_STATUS = FLR_MEDIAINBOX + "." + "status_c_nr" ; 
	public final static String FLR_MEDIAINBOX_MEDIA_ID =  FLR_MEDIAINBOX + "." + "media_i_id" ; 
	public final static String FLR_MEDIAINBOX_VERSTECKT =  FLR_MEDIAINBOX + "." + "b_versteckt" ; 
	
	public final static String FLR_MEDIASTOREBELEG_EMAILMETA_DATEFROM = FLR_MEDIASTOREBELEG + "." + EMAILMETA + "." + "t_emaildate" ;
	public final static String FLR_MEDIASTOREBELEG_EMAILMETA_FROM = FLR_MEDIASTOREBELEG + "." + EMAILMETA + "." + "c_from" ;
	public final static String FLR_MEDIASTOREBELEG_EMAILMETA_SUBJECT = FLR_MEDIASTOREBELEG + "." + EMAILMETA + "." + "c_subject" ;
	public final static String FLR_MEDIASTOREBELEG_CBELEGARTNR = FLR_MEDIASTOREBELEG + "." + "c_belegartnr" ;
	public final static String FLR_MEDIASTOREBELEG_MANDANTCNR  = FLR_MEDIASTOREBELEG + "." + "mandant_c_nr" ;
	public final static String FLR_MEDIASTOREBELEG_DATENEW = FLR_MEDIASTOREBELEG + "." + "t_anlegen" ;
	public final static String FLR_MEDIASTOREBELEG_BELEGID = FLR_MEDIASTOREBELEG + "." + "beleg_i_id" ;
	public final static String FLR_MEDIASTOREBELEG_BELEGPOSITIONID = FLR_MEDIASTOREBELEG + "." + "belegposition_i_id" ;
	public final static String FLR_MEDIASTOREBELEG_MEDIA_ID = FLR_MEDIASTOREBELEG + "." + "media_i_id" ;
	public final static String FLR_MEDIASTOREBELEG_PERSONALIDANLEGEN = FLR_MEDIASTOREBELEG + "." + "personal_i_id_anlegen" ;
	
	/**
	 * Die Email-Metadaten zu einem Inbox-Satz laden
	 * 
	 * @param inboxId die Id des Inbox-Satzes
	 * @return die EMail-Metadaten des Inbox-Satzes sofern vorhanden, ansonsten Exception
	 * @throws EJBExceptionLP
	 */
	MediaEmailMetaDto emailMetaFindByInboxId(Integer inboxId) throws EJBExceptionLP ;
	
	/**
	 * Einen Inbox-Satz explizit l&ouml;schen
	 * 
	 * @param inboxId die Id des Satzes der geloescht werden soll
	 * 
	 * @throws EJBExceptionLP
	 */
	void removeInboxEntry(Integer inboxId) throws EJBExceptionLP ;

	/**
	 * Einen Inbox-Satz auf versteckt setzen
	 * 
	 * @param inboxId
	 * @throws EJBExceptionLP
	 */
	void versteckeInboxEntry(Integer inboxId) throws EJBExceptionLP;
	
	/**
	 * Den bestehenden Inbox-Satz einer anderen PersonalId zuordnen
	 * 
	 * @param inboxId der zu verschiebende Datensatz
	 * @param personalId die den Satz erhalten soll
	 * @param theClientDto wer verschiebt gerade
	 * @throws EJBExceptionLP
	 */
	void moveInboxEntryTo(Integer inboxId, Integer personalId, 
			TheClientDto theClientDto) throws EJBExceptionLP ;
	
	void retrieveEmails(TheClientDto theClientDto) throws IOException,
		MessagingException, RepositoryException, JMSException ;

	void removeEmail(String uuid, TheClientDto theClientDto) throws IOException, MessagingException ;
	
	/**
	 * Einen Kurzbrief aus einer E-Mail anlegen
	 * 
	 * @param partnerId des Partners f&uuml;r den der Kurzbrief gilt
	 * @param belegartCnr LocaleFac.BELEGART_PARTNER, _LIEFERANT, _KUNDE
	 * @param emailMetaDto die E-Mail die zum Kurzbrief werden soll
	 * @param theClientDto der angemeldete Benutzer
	 * @throws Throwable
	 * @return Id des neu erzeugten Kurzbriefs
	 */
	Integer createKurzbriefFromEmail(Integer partnerId, String belegartCnr, MediaEmailMetaDto emailMetaDto, TheClientDto theClientDto) throws Throwable ;

	/**
	 * Ist der Kurzbrief durch eine Email erzeugt worden?
	 * 
	 * @param kurzbriefId
	 * @param partnerId
	 * @param theClientDto
	 * @return true wenn der Kurzbrief aus einer E-Mail entstanden ist, ansonsten false
	 * @throws EJBExceptionLP
	 */
	boolean hasKurzbriefEmailReferenz(Integer kurzbriefId, Integer partnerId, TheClientDto theClientDto) throws EJBExceptionLP ;

	Integer createHistoryFromEmail(Integer projektId, MediaEmailMetaDto emailMetaDto, TheClientDto theClientDto) throws Throwable ;
	
	boolean hasHistoryEmailReferenz(Integer projektId, Integer historyId, TheClientDto theClientDto) throws EJBExceptionLP ;

	void createBelegReferenz(Integer mediaId, String belegart, Integer belegId, TheClientDto theClientDto) ;	
	void createBelegReferenz(Integer mediaId, String belegart, 
			Integer belegId, Integer belegpositionId, TheClientDto theClientDto) ;	
	void createReferenzPartnerKurzbrief(Integer mediaId, Integer partnerId, TheClientDto theClientDto) ;

	void createReferenzKundeKurzbrief(Integer mediaId, Integer partnerId, TheClientDto theClientDto) ;
	
	/**
	 * Die Belegreferenz &uuml;ber die Belegreferenz-Id laden</br>
	 * <p>Die Email-Metadaten werden <b>nicht</b> geladen</p>
	 * 
	 * @param referenceId
	 * @param theClientDto
	 * @return Das Dto zum Beleg. Gibt es keine Referenz. Wird die Id nicht gefunden, wird eine EJBException geworfen
	 */
	MediaStoreBelegDto belegReferenzFindByReferenceId(Integer referenceId, TheClientDto theClientDto) ;
	
	/**
	 * Die Belegreferenz &uuml;ber die Belegreferenz-Id laden</br>
	 * <p>Die Email-Metadaten werden <b>nicht</b> geladen</p>
	 * 
	 * @param referenceId die Belegreferenz-Id
	 * @param loadEmailMeta true wenn auch die EmailMeta-Daten geladen werden sollen
	 * @param theClientDto
	 * @return Das Dto zum Beleg. Gibt es keine Referenz. Wird die Id nicht gefunden, wird eine EJBException geworfen
	 */
	MediaStoreBelegDto belegReferenzFindByReferenceId(Integer referenceId, boolean loadEmailMeta, TheClientDto theClientDto) ;
}