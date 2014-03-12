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
package com.lp.server.system.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface PanelFac {

	public static final String TYP_WRAPPERLABEL = "WrapperLabel        ";
	public static final String TYP_WRAPPEREDITOR = "WrapperEditor       ";
	public static final String TYP_WRAPPERCHECKBOX = "WrapperCheckbox     ";
	public static final String TYP_WRAPPERTEXTFIELD = "WrapperTextField    ";
	public static final String TYP_WRAPPERPRINTBUTTON = "WrapperPrintButton  ";
	public static final String TYP_WRAPPEREXECBUTTON = "WrapperExecButton   ";
	public static final String TYP_WRAPPERCOMBOBOX =   "WrapperComboBox     ";

	public static final String FILL_HORIZONTAL = "HORIZONTAL";
	public static final String FILL_VERTICAL = "VERTICAL";
	public static final String FILL_NONE = "NONE";
	public static final String FILL_BOTH = "BOTH";

	public static final String ANCHOR_WEST = "WEST";
	public static final String ANCHOR_SOUTHWEST = "SOUTHWEST";
	public static final String ANCHOR_NORTH = "NORTH";
	public static final String ANCHOR_SOUTH = "SOUTH";
	public static final String ANCHOR_SOUTHEAST = "SOUTHEAST";
	public static final String ANCHOR_EAST = "EAST";
	public static final String ANCHOR_NORTHEAST = "NORTHEAST";
	public static final String ANCHOR_CENTER = "CENTER";
	public static final String ANCHOR_NORTHWEST = "NORTHWEST";

	public static final String FLR_PANELBESCHREIBUNG_C_NAME = "c_name";
	public static final String FLR_PANELBESCHREIBUNG_C_TYP = "c_typ";
	public static final String FLR_PANELBESCHREIBUNG_C_FILL = "c_fill";
	public static final String FLR_PANELBESCHREIBUNG_C_ANCHOR = "c_anchor";
	public static final String FLR_PANELBESCHREIBUNG_PANEL_C_NR = "panel_c_nr";
	public static final String FLR_PANELBESCHREIBUNG_I_GRIDX = "i_gridx";
	public static final String FLR_PANELBESCHREIBUNG_I_GRIDY = "i_gridy";
	public static final String FLR_PANELBESCHREIBUNG_B_MANDATORY = "b_mandatory";
	public static final String FLR_PANELBESCHREIBUNG_ARTGRU_I_ID = "artgru_i_id";
	public static final String FLR_PANELBESCHREIBUNG_ARTGRU_BEZ = "artgru_bez";

	public static final String FLR_PANELDATEN_I_ID = "i_id";
	public static final String FLR_PANELDATEN_C_KEY = "c_key";
	public static final String FLR_PANELDATEN_X_INHALT = "x_inhalt";

	public static final String PANEL_ARTIKELEIGENSCHAFTEN = "ARTIKELEIGENSCHAFTEN     ";
	public static final String PANEL_AUFTRAGSEIGENSCHAFTEN = "AUFTRAGSEIGENSCHAFTEN    ";
	public static final String PANEL_KUNDENEIGENSCHAFTEN = "KUNDENEIGENSCHAFTEN      ";
	public static final String PANEL_LOSEIGENSCHAFTEN =     "LOSEIGENSCHAFTEN         ";
	public static final String PANEL_PROJEKTEIGENSCHAFTEN = "PROJEKTEIGENSCHAFTEN     ";

	public static final String REPORT_MODUL = "panel";

	// Feldlaengen
	public static final int MAX_PANELBESCHREIBUNG_NAME = 120;

	public Integer createPanelbeschreibung(
			PanelbeschreibungDto panelbeschreibungDto, boolean bFuegeNeuePositionVorDerSelektiertenEin) throws RemoteException,
			EJBExceptionLP;

	public void removePanelbeschreibung(Integer iId) throws RemoteException,
			EJBExceptionLP;

	public void removePanelbeschreibung(
			PanelbeschreibungDto panelbeschreibungDto) throws RemoteException,
			EJBExceptionLP;

	public void updatePanelbeschreibung(
			PanelbeschreibungDto panelbeschreibungDto) throws RemoteException,
			EJBExceptionLP;

	public PanelbeschreibungDto panelbeschreibungFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNr(
			String panelCNr, String mandantCNr, Integer artgruIId) throws RemoteException,
			EJBExceptionLP;

	public JasperPrintLP printPanel(String panelCNr, String report,
			String cKey, TheClientDto theClientDto) throws RemoteException;

	public void createPaneldaten(PaneldatenDto[] paneldatenDto)
			throws RemoteException, EJBExceptionLP;

	public void removePaneldaten(Integer iId) throws RemoteException,
			EJBExceptionLP;

	public void removePaneldaten(PaneldatenDto paneldatenDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePaneldaten(PaneldatenDto paneldatenDto)
			throws RemoteException, EJBExceptionLP;

	public PaneldatenDto paneldatenFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public PaneldatenDto findByPanelCNrPanelbeschreibungIIdCKey(
			String panelCNr, Integer panelbeschreibungIId, String cKey)
			throws EJBExceptionLP, RemoteException;

	public PaneldatenDto[] paneldatenFindByPanelCNrCKey(String panelCNr,
			String cKey) throws EJBExceptionLP, RemoteException;

	public void createPanel(PanelDto panelDto) throws RemoteException,
			EJBExceptionLP;

	public void removePanel(String cNr) throws RemoteException, EJBExceptionLP;

	public void removePanel(PanelDto panelDto) throws RemoteException,
			EJBExceptionLP;

	public void updatePanel(PanelDto panelDto) throws RemoteException,
			EJBExceptionLP;

	public PanelDto panelFindByPrimaryKey(String cNr) throws RemoteException,
			EJBExceptionLP;
	public boolean panelbeschreibungVorhanden(String panelCNr, String mandantCNr);
	
	
	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNrPartnerklasseIId(
			String panelCNr, String mandantCNr, Integer partnerklasseIId);
	
}
