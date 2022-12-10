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
package com.lp.server.system.ejbfac;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.lp.server.system.service.BelegDatumClient;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * Server-Klasse zur Berechnung von Daten fuer Belegdatumsfelder
 * 
 * @author andi
 *
 */
public class BelegDatumServer implements Serializable {

	private static final long serialVersionUID = 24431565318498222L;

	private BelegDatumClient bdClient;
	private Boolean hatFibuModul;
	private Boolean istChefbuchhalter;
	private int parameterTage;
	private int aktuellesGJ;

	private SystemFac systemBean;
	private ParameterFac parameterBean;

	public BelegDatumServer(BelegDatumClient bdClient, Boolean hatFibuModul,
			Boolean istChefbuchhalter, SystemFac systemBean,
			ParameterFac parameterBean) throws RemoteException {
		super();
		this.bdClient = bdClient;
		this.hatFibuModul = hatFibuModul;
		this.istChefbuchhalter = istChefbuchhalter;
		this.systemBean = systemBean;
		setParameterBean(parameterBean);
	}

	public void setChefbuchhalter(Boolean bVal) {
		istChefbuchhalter = bVal;
	}
	
	public void setHatFibuModul(Boolean bVal) {
		hatFibuModul = bVal;
	}
	
	public void setSystemBean(SystemFac bean) {
		systemBean = bean;
	}
	
	public void setParameterBean(ParameterFac bean) throws RemoteException {
		parameterBean = bean;
		
		ParametermandantDto parametermandantDto = parameterBean.getMandantparameter(bdClient.getMandant(), 
				ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BEWEGUNGSMODULE_ANLEGEN_BIS_ZUM);
		parameterTage = (Integer)parametermandantDto.getCWertAsObject();
		aktuellesGJ = parameterBean.getGeschaeftsjahr(bdClient.getMandant());
	}
	
	/**
	 * Ermittelt das n&auml;chste g&uuml;ltige (nicht gesperrte) Gesch&auml;ftsjahr.
	 * Als erstes wird das &uuml;bergebenen Jahr gepr&uuml;ft, anschließend
	 * aufsteigend. Sobald keine Sperre vorhanden ist wird das gerade getestete Jahr
	 * zur&uuml;ckgeliefert.
	 * 
	 * @param jahr mit diesem Jahr soll die Pr&uuml;fung starten
	 * @return das n&auml;chste g&uuml;ltige Gesch&auml;ftsjahr
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	protected Integer getGueltigesGeschaeftsjahr(int jahr) throws EJBExceptionLP, RemoteException {
		if(hatFibuModul) {
			for(; jahr < aktuellesGJ; jahr++) {
				try {
					systemBean.pruefeGeschaeftsjahrSperre(new Integer(jahr), bdClient.getMandant());
					break;
				} catch (Throwable t) {
					continue;
				}
			}
		}
		
		return new Integer(jahr);
	}
	
	/**
	 * Ermittelt das minimale Belegdatum, das vom Benutzer ausgew&auml;hlt werden
	 * darf, unter Ber&uuml;cksichtigung des Parameters
	 * {@link ParameterFac#ZEITBUCHUNGEN_NACHTRAEGLICH_BUCHEN_BIS} und Infos
	 * des Clients
	 * 
	 * @return das Minimum-Datum eines Belegs
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public Date getMinimumDate() throws EJBExceptionLP, RemoteException {
		if(istChefbuchhalter && bdClient.getMitChefbuchalter()) {
			return null;
		}
		
		if (parameterTage == -1) {
			//Ganzes vergangenes GJ ist verf&uuml;gbar
			Integer gueltigesGJ = getGueltigesGeschaeftsjahr(aktuellesGJ - 1);
			GeschaeftsjahrMandantDto gjMandantDto = 
					systemBean.geschaeftsjahrFindByPrimaryKey(gueltigesGJ, bdClient.getMandant());
			return gjMandantDto.getDBeginndatum();
			
		} 
		
		GregorianCalendar minDatum = bdClient.getAktuellesDatum();
			
		if (parameterTage < 0) {
			//Tage werden ab heute zur&uuml;ckgerechnet
			minDatum.add(Calendar.DATE, parameterTage);
			Integer gjNachAbzugVonTagen = parameterBean.getGeschaeftsjahr(
					bdClient.getMandant(), minDatum.getTime());
			
			if(aktuellesGJ == gjNachAbzugVonTagen.intValue()) {
				//GJ stimmt mit aktuellem noch ueberein
				return Helper.cutDate(minDatum.getTime());
			}
			
			Integer gueltigesGJ = getGueltigesGeschaeftsjahr(gjNachAbzugVonTagen);
				
			if(gueltigesGJ.equals(gjNachAbzugVonTagen)) {
				//GJ ist gueltig
				return Helper.cutDate(minDatum.getTime());
			}
			
			//GJ ist gesperrt, hole Beginndatum des naechsten gueltigen als Minimum
			GeschaeftsjahrMandantDto gjMandantDto = 
					systemBean.geschaeftsjahrFindByPrimaryKey(gueltigesGJ, bdClient.getMandant());
			return gjMandantDto.getDBeginndatum();
		}
			
		
		//Tag des vergangenen Monats wird gesetzt
		minDatum.add(Calendar.MONTH, -1);
		minDatum.set(Calendar.DATE, parameterTage);
		return minDatum.getTime();
	}
}
