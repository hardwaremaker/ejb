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
package com.lp.webapp.frame;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemMultilanguageFac;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientFac;
import com.lp.server.util.ServiceLocatorException;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2011/12/14 12:05:32 $
 */
public class Command implements ICommand {

	private String sJSPNext = null;
	protected static ILPLogger myLogger = null;
	private LogonFac logonFac = null;
	private ZeiterfassungFac zeiterfassungFac = null;
	private ZutrittscontrollerFac zutrittscontrollerFac = null;
	private PersonalFac personalFac = null;

	private ArtikelFac artikelFac = null;
	private StuecklisteFac stuecklisteFac = null;
	private AuftragFac auftragFac = null;
	private AngebotFac angebotFac = null;
	private ProjektFac projektFac = null;
	private AuftragpositionFac auftragpositionFac = null;
	private FertigungFac fertigungFac = null;
	private PartnerFac partnerFac = null;
	private BenutzerFac benutzerFac = null;
	private MandantFac mandantFac = null;

	private TheClientFac theClientFac = null;
	private ParameterFac parameterFac = null;

	private SystemServicesFac systemServicesFac = null;
	private SystemMultilanguageFac systemMultilanguageFac = null;

	protected String command = null;
	private Context context = null;

	public Command(String sJSPI) {
		sJSPNext = sJSPI;
		myLogger = LPLogService.getInstance().getLogger(this.getClass());
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * siehe Commandpattern.
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param resp
	 *            HttpServletResponse
	 * @return java.lang.String
	 * @throws Exception
	 * @todo Implement this com.lp.webapp.frame.ICommand method PJ 4008
	 */
	public synchronized String execute(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		command = getCommand(req);

		return null;
	}

	/**
	 * getCommand
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	protected String getCommand(HttpServletRequest request) {
		String command = null;
		command = request.getParameter("cmd");
		// if (command == null) {
		// command = TheApp.CMD_WAP_SHOWMENUE;
		// }
		return command;

	}

	public void setSJSPNext(String sJSPNext) {
		this.sJSPNext = sJSPNext;
	}

	public String getSJSPNext() {
		return sJSPNext;
	}

	protected LogonFac getLogonFac() throws RemoteException,
			ServiceLocatorException {
		if (logonFac == null) {
			try {
				logonFac = (LogonFac) context
						.lookup("lpserver/LogonFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return logonFac;
	}

	protected ZeiterfassungFac getZeiterfassungsFac()
			throws ServiceLocatorException, RemoteException {
		if (zeiterfassungFac == null) {
			try {

				zeiterfassungFac = (ZeiterfassungFac) context
						.lookup("lpserver/ZeiterfassungFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return zeiterfassungFac;
	}

	protected ZutrittscontrollerFac getZutrittscontrollerFac()
			throws ServiceLocatorException, RemoteException {
		if (zutrittscontrollerFac == null) {
			try {

				zutrittscontrollerFac = (ZutrittscontrollerFac) context
						.lookup("lpserver/ZutrittscontrollerFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return zutrittscontrollerFac;
	}

	protected PersonalFac getPersonalFac() throws ServiceLocatorException,
			RemoteException {

		if (personalFac == null) {
			try {
				personalFac = (PersonalFac) context
						.lookup("lpserver/PersonalFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return personalFac;
	}

	protected BenutzerFac getBenutzerFac() throws ServiceLocatorException,
			RemoteException {

		if (benutzerFac == null) {
			try {
				benutzerFac = (BenutzerFac) context
						.lookup("lpserver/BenutzerFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return benutzerFac;
	}

	protected PartnerFac getPartnerFac() throws ServiceLocatorException,
			RemoteException {

		if (partnerFac == null) {
			try {
				partnerFac = (PartnerFac) context
						.lookup("lpserver/PartnerFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return partnerFac;
	}

	protected ArtikelFac getArtikelFac() throws ServiceLocatorException,
			RemoteException {

		if (artikelFac == null) {
			try {
				artikelFac = (ArtikelFac) context
						.lookup("lpserver/ArtikelFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return artikelFac;
	}

	protected StuecklisteFac getStuecklisteFac()
			throws ServiceLocatorException, RemoteException {

		if (stuecklisteFac == null) {
			try {
				stuecklisteFac = (StuecklisteFac) context
						.lookup("lpserver/StuecklisteFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return stuecklisteFac;
	}

	protected TheClientFac getTheClientFac() throws ServiceLocatorException,
			RemoteException {

		if (theClientFac == null) {
			try {
				theClientFac = (TheClientFac) context
						.lookup("lpserver/TheClientFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return theClientFac;
	}

	protected ParameterFac getParameterFac() throws ServiceLocatorException,
			RemoteException {

		if (parameterFac == null) {
			try {
				parameterFac = (ParameterFac) context
						.lookup("lpserver/ParameterFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return parameterFac;
	}

	protected MandantFac getMandantFac() throws ServiceLocatorException,
			RemoteException {
		if (mandantFac == null) {
			try {
				mandantFac = (MandantFac) context
						.lookup("lpserver/MandantFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return mandantFac;
	}

	protected AuftragFac getAuftragFac() throws ServiceLocatorException,
			RemoteException {
		if (auftragFac == null) {
			try {
				auftragFac = (AuftragFac) context
						.lookup("lpserver/AuftragFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return auftragFac;
	}

	protected AngebotFac getAngebotFac() throws ServiceLocatorException,
			RemoteException {
		if (angebotFac == null) {
			try {
				angebotFac = (AngebotFac) context
						.lookup("lpserver/AngebotFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return angebotFac;
	}

	protected ProjektFac getProjektFac() throws ServiceLocatorException,
			RemoteException {
		if (projektFac == null) {
			try {
				projektFac = (ProjektFac) context
						.lookup("lpserver/ProjektFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return projektFac;
	}

	protected AuftragpositionFac getAuftragpositionFac()
			throws ServiceLocatorException, RemoteException {

		if (auftragpositionFac == null) {
			try {
				auftragpositionFac = (AuftragpositionFac) context
						.lookup("lpserver/AuftragpositionFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return auftragpositionFac;
	}

	protected FertigungFac getFertigungFac() throws ServiceLocatorException,
			RemoteException {

		if (fertigungFac == null) {
			try {
				fertigungFac = (FertigungFac) context
						.lookup("lpserver/FertigungFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return fertigungFac;
	}

	protected SystemServicesFac getSystemServicesFac()
			throws ServiceLocatorException, RemoteException {

		if (systemServicesFac == null) {
			try {
				systemServicesFac = (SystemServicesFac) context
						.lookup("lpserver/SystemServicesFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return systemServicesFac;
	}

	protected SystemMultilanguageFac getSystemMultilanguageFac()
			throws ServiceLocatorException, RemoteException {

		if (systemMultilanguageFac == null) {
			try {
				systemMultilanguageFac = (SystemMultilanguageFac) context
						.lookup("lpserver/SystemMultilanguageFacBean/remote");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return systemMultilanguageFac;
	}

	public TheClient getTheClient(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		TheClient client = null;
		HttpSession session = request.getSession(true);

		try {
			client = (TheClient) session.getAttribute(session.getId());
		} catch (Exception e) {
			client = null;
		}
		System.out.println("client geholt: " + client);
		return client;
	}

}
