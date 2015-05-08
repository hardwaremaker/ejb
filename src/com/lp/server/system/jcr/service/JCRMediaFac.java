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
package com.lp.server.system.jcr.service;

import java.io.IOException;

import javax.ejb.Remote;
import javax.jcr.RepositoryException;
import javax.mail.MessagingException;

import com.lp.server.system.service.TheClientDto;

@Remote
public interface JCRMediaFac {
	final static String PREFIX    = "hv" ;

	public class NNEmail {
		public final static String PREFIX_EMAIL = PREFIX + "Email" ;
		public final static String FROM      = PREFIX_EMAIL + "From" ;
		public final static String TO        = PREFIX_EMAIL + "To" ;
		public final static String CC        = PREFIX_EMAIL + "Cc" ;
		public final static String BCC       = PREFIX_EMAIL + "Bcc" ;
		public final static String SUBJECT   = PREFIX_EMAIL + "Subject" ;
		public final static String MESSAGEID = PREFIX_EMAIL + "MessageId" ;
		public final static String EMAILDATE = PREFIX_EMAIL + "EmailDate" ;
		public final static String ISHTML    = PREFIX_EMAIL + "IsHtml" ;
		public final static String DATA      = PREFIX_EMAIL + "DATA" ;	
		public final static String INREPLYTOID = PREFIX_EMAIL + "InReplyTo" ;
	} ;
	
	public class NNAttachment {
		public final static String PREFIX_ATTACH = PREFIX + "Attach" ;
		public final static String PARENTUUID  = PREFIX_ATTACH + "ParentUuid" ;
		public final static String NAME        = PREFIX_ATTACH + "Name" ;
		public final static String DESCRIPTION = PREFIX_ATTACH + "Description" ;
		public final static String TYPE        = PREFIX_ATTACH + "Type" ;
		public final static String DATA        = PREFIX_ATTACH + "Data" ;
	}
	
	JCRMediaUuidHelper addEMail(JCRMediaDto mediaDto, 
			TheClientDto theClientDto)  throws Exception ;

	JCRMediaUuidHelper addEMailCloseSession(JCRMediaDto mediaDto, TheClientDto theClientDto) 
			throws Exception ;

	JCRMediaUuidHelper addEMailWithinTransaction(JCRMediaDto mediaDto, 
			TheClientDto theClientDto) throws Exception ;
	
	JCRMediaDto findEmail(String messageId, 
			TheClientDto theClientDto) throws IOException, MessagingException ;
	
	JCRMediaDto findEmailByUUID(String uuid, 
			TheClientDto theClientDto) throws IOException, RepositoryException;
	
	JCRMediaDto findEmail(String messageId, boolean withContent,
			TheClientDto theClientDto) throws IOException, MessagingException ;
	
	JCRMediaDto findEmailByUUID(String uuid, boolean withContent,
			TheClientDto theClientDto) throws IOException, RepositoryException;

	public JCRMediaDto findEmailEx(String messageId, boolean withContent, 
			TheClientDto theClientDto) throws IOException, RepositoryException, MessagingException ;
}