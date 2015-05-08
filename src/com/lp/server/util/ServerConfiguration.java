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
package com.lp.server.util;

import java.util.ResourceBundle;

public class ServerConfiguration {
	private static ResourceBundle cfgBundle = null;
	private static final String CONFIG_BUNDLE_LP = "com.lp.server.res.lpconfig";

	private static ResourceBundle versionBundle = null ;
	private static final String VERSION_BUNDLE_LP = "com.lp.server.res.lp";
	
	private static ResourceBundle config() {
		if (cfgBundle == null) {
			cfgBundle = ResourceBundle.getBundle(CONFIG_BUNDLE_LP);
		}
		return cfgBundle ;
	}

	private static ResourceBundle version() {
		if (versionBundle == null) {
			versionBundle = ResourceBundle.getBundle(VERSION_BUNDLE_LP);
		}
		return versionBundle ;
	}
	
	/**
	 * Die Build-Nummer des Servers
	 * @return die Build-Nummer des Servers
	 */
	public static Integer getBuildNumber() {
		return Integer.parseInt(version().getString("lp.version.server.build")) ;
	}
	
	/**
	 * Die komplette Version des Servers
	 * @return die komplette Version des Servers "Helium V 5.12.022"
	 */
	public static String getVersion() {
		return version().getString("lp.version.server") ;
	}
	
	
	public static String getPdfDir() {
		return config().getString("drucken.pdf.output.dir") ;
	}

	/**
	 * Das Basis-Verzeichnis aller Reports
	 * 
	 * @return das Basis-Verzeichnis aller Reports
	 */
	public static String getReportDir() {
		return config().getString("drucken.root.formular.dir");
	}
	

	/**
	 * Das Basis-Verzeichnis in dem die SSL Zertifikate liegen
	 * 
	 * @return das Basis-Verzeichnis der SSL-Zertifikate
	 */
	public static String getSSLCertificateDir() {
		return config().getString("ssl.root.dir") ;
	}
	
	/**
	 * Das Basis-Verzeichnis der Ruby Script-Dateien
	 * @return das Basis-Verzeichnis der Script-Dateien
	 */
	public static String getScriptDir() {
		return config().getString("script.root.dir");
	}	
	
	/**
	 * Der Benutzername des Admin-Benutzers
	 * @return der Name (Account-Name) des Admin-Users
	 */
	public static String getAdminUsername() {
		return config().getString("user.admin") ;
	}
	
	/**
	 * Webapp spezifische Settings ermitteln
	 * 
	 * @param appType ist die App
	 * @param token der Key f&uuml;r den der Wert ermittelt werden soll
	 * @return den Wert f&uuml;r das Token der jeweiligen App
	 */
	public static String getAppTypeProperty(int appType, String token) {
		return config().getString("extapp." + appType + "." + token) ;
	}
	
	/**
	 * Die gecacheten Werte l&ouml;schen</br>
	 * <p>Soll im Falle eines Falles erm&ouml;glichen, dass neue Werte
	 * eingestellt werden k&ouml;nnen, ohne den Server deswegen neu
	 * starten zu m&uuml;ssen</p>
	 */
	public static void clearCache() {
		ResourceBundle.clearCache(); 
		cfgBundle = null ;
		versionBundle = null ;
	}
}
