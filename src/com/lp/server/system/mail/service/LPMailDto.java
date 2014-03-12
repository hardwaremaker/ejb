/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.system.mail.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class LPMailDto implements Serializable {

	private static final long serialVersionUID = 4175064614790804470L;
	
	private Integer versandauftragIId;
	private String smtpServer;
	private String smtpBenutzer;
	private String smtpKennwort;
	private String imapServer;
	private String imapAdmin;
	private String imapAdminKennwort;
	private Timestamp tSendezeitpunktwunsch;
	private String imapBenutzer;
	private String imapBenutzerKennwort;
	private String sentFolder = sentFolderDefault;
	private String faxDomain;
	private boolean isFax = false;
	private String mailAdmin;
	private int faxAnbindung = 0;
	private String xpirioKennwort;
	
	private final static String sentFolderDefault = "Gesendete Objekte";
	
	public void setVersandauftragIId(Integer versandauftragIId) {
		this.versandauftragIId = versandauftragIId;
	}
	public Integer getVersandauftragIId() {
		return versandauftragIId;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpBenutzer(String smtpBenutzer) {
		this.smtpBenutzer = smtpBenutzer;
	}
	public String getSmtpBenutzer() {
		return smtpBenutzer;
	}
	public void setSmtpKennwort(String smtpKennwort) {
		this.smtpKennwort = smtpKennwort;
	}
	public String getSmtpKennwort() {
		return smtpKennwort;
	}
	public void setImapServer(String imapServer) {
		this.imapServer = imapServer;
	}
	public String getImapServer() {
		return imapServer;
	}
	public void setImapAdmin(String imapAdmin) {
		this.imapAdmin = imapAdmin;
	}
	public String getImapAdmin() {
		return imapAdmin;
	}
	public void setImapAdminKennwort(String imapAdminKennwort) {
		this.imapAdminKennwort = imapAdminKennwort;
	}
	public String getImapAdminKennwort() {
		return imapAdminKennwort;
	}
	public void setTSendezeitpunktwunsch(Timestamp tSendezeitpunktwunsch) {
		this.tSendezeitpunktwunsch = tSendezeitpunktwunsch;
	}
	public Timestamp getTSendezeitpunktwunsch() {
		return tSendezeitpunktwunsch;
	}
	public void setImapBenutzer(String imapBenutzer) {
		this.imapBenutzer = imapBenutzer;
	}
	public String getImapBenutzer() {
		return imapBenutzer;
	}
	public void setImapBenutzerKennwort(String imapBenutzerKennwort) {
		this.imapBenutzerKennwort = imapBenutzerKennwort;
	}
	public String getImapBenutzerKennwort() {
		return imapBenutzerKennwort;
	}
	public void setSentFolder(String sentFolder) {
		this.sentFolder = sentFolder;
	}
	public String getSentFolder() {
		return sentFolder;
	}
	public void setFaxDomain(String faxDomain) {
		this.faxDomain = faxDomain;
	}
	public String getFaxDomain() {
		return faxDomain;
	}
	public void setFax(boolean isFax) {
		this.isFax = isFax;
	}
	public boolean isFax() {
		return isFax;
	}
	public void setMailAdmin(String mailAdmin) {
		this.mailAdmin = mailAdmin;
	}
	public String getMailAdmin() {
		return mailAdmin;
	}
	
	public String toLogInfo() {
		String info = "ID:" + versandauftragIId + " "
			+ "TIME:" + tSendezeitpunktwunsch.toString();
		return info;
	}
	public void setFaxAnbindung(int faxAnbindung) {
		this.faxAnbindung = faxAnbindung;
	}
	public int getFaxAnbindung() {
		return faxAnbindung;
	}
	public void setXpirioKennwort(String xpirioKennwort) {
		this.xpirioKennwort = xpirioKennwort;
	}
	public String getXpirioKennwort() {
		return xpirioKennwort;
	}

}
