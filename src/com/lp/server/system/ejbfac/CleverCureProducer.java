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
package com.lp.server.system.ejbfac;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.persistence.EntityManager;

import com.lp.server.system.ejb.VersandwegCC;
import com.lp.server.system.service.VersandwegCCPartnerDto;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;

public class CleverCureProducer {
	private DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") ;
	
	protected KeyStore getKeystore(VersandwegCCPartnerDto versandwegPartnerDto)
			throws KeyStoreException, FileNotFoundException, CertificateException, NoSuchAlgorithmException, IOException {
		KeyStore keystore = KeyStore.getInstance("jks") ;
		String certfileName = SystemServicesFacBean.getPathFromSSLDir(versandwegPartnerDto.getCKeystore()) ;
		FileInputStream fis = new FileInputStream(certfileName);
		keystore.load(fis, versandwegPartnerDto.getCKeystoreKennwort().toCharArray()) ;
		return keystore ;
	}
	
	protected SSLContext getSslContext(VersandwegCCPartnerDto versandwegPartnerDo, KeyStore keystore)
		throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(keystore, versandwegPartnerDo.getCKeystoreKennwort().toCharArray());
		KeyManager[] keyManagers = kmf.getKeyManagers();
		 
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagers, null, null);
		return sslContext ;
	}
	
	protected String getCCEndpunkt(EntityManager em, VersandwegCCPartnerDto ccPartnerDto) {
		VersandwegCC versandweg = em.find(VersandwegCC.class, ccPartnerDto.getVersandwegId()) ;
		if(null == versandweg) throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ccPartnerDto.getVersandwegId().toString()) ;
		
		return versandweg.getCEndpunkt() ;
	}
	
	
	private BigDecimal ccScaled(int scale, BigDecimal value) {
		return value.setScale(scale, RoundingMode.HALF_EVEN) ;
	}

	/**
	 * Einen BigDecimal mit 3 Nachkommastellen ausgegeben</br>
	 * <p>Es wird HALF_EVEN gerundet sofern notwendig</p>
	 * @param value
	 * @return ein BigDecimal mit 3 Nachkommastellen
	 */
	protected BigDecimal ccScaled3(BigDecimal value) {
		return ccScaled(3, value) ;
	}
	
	/**
	 * Einen BigDecimal mit 4 Nachkommastellen ausgegeben</br>
	 * <p>Es wird HALF_EVEN gerundet sofern notwendig</p>
	 * @param value
	 * @return ein BigDecimal mit 4 Nachkommastellen
	 */
	protected BigDecimal ccScaled4(BigDecimal value) {
		return ccScaled(4, value) ;
	}

	/**
	 * Den Bezug zur Bestellposition ermitteln</br>
	 * <p>Beim Erstellen der Belegposition wird ein JSON String mit der reference
	 * manuell in das X_TEXTINHALT Feld gestellt. Dieses wird hier nun extrahiert</p>
	 * 
	 * @param belegpositionDto
	 * @return die LineitemIdRef 
	 */
	public String extractLineItemIdRef(BelegpositionDto belegpositionDto) {
		String xtext = belegpositionDto.getXTextinhalt() ;
		if(xtext == null) return "" ;
		
		xtext = xtext.replaceAll("&quot;", "\"") ;
		String lineitemidToken = "{\"lineitemid\" : \"" ;
		int startIndex = xtext.indexOf(lineitemidToken);
		if(startIndex == -1) return "" ;
		
		int endIndex = xtext.indexOf("\"", startIndex + lineitemidToken.length()) ;
		if(endIndex == -1) return "" ;
		
		return xtext.substring(startIndex + lineitemidToken.length(), endIndex) ;
	}
	
	public void setLineItemIdRef(BelegpositionDto positionDto, String reference) {
		positionDto.setXTextinhalt("{\"lineitemid\" : \"" + reference + "\"}") ;		
	}
	
	public String formatAsIso8601Timestamp(Date date) {
		// Clevercure unterstuetzt keine Zeitzonen!
		return  isoFormat.format(date) ;
	}		
}
