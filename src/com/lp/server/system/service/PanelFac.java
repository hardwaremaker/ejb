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
import java.util.ArrayList;

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
	public static final String TYP_WRAPPERCOMBOBOX = "WrapperComboBox     ";
	public static final String TYP_WRAPPERTEXTAREA = "WrapperTextArea     ";

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
	public static final String FLR_PANELBESCHREIBUNG_KOSTENSTELLE_I_ID = "kostenstelle_i_id";
	public static final String FLR_PANELBESCHREIBUNG_PARTNERKLASSE_I_ID = "partnerklasse_i_id";
	public static final String FLR_PANELBESCHREIBUNG_BEREICH_I_ID = "bereich_i_id";
	public static final String FLR_PANELBESCHREIBUNG_PROJEKTTYP_C_NR = "projekttyp_c_nr";

	public static final String FLR_PANELDATEN_I_ID = "i_id";
	public static final String FLR_PANELDATEN_C_KEY = "c_key";
	public static final String FLR_PANELDATEN_X_INHALT = "x_inhalt";

	public static final String PANEL_ARTIKELEIGENSCHAFTEN = "ARTIKELEIGENSCHAFTEN     ";
	public static final String PANEL_AUFTRAGSEIGENSCHAFTEN = "AUFTRAGSEIGENSCHAFTEN    ";
	public static final String PANEL_KUNDENEIGENSCHAFTEN = "KUNDENEIGENSCHAFTEN      ";
	public static final String PANEL_LOSEIGENSCHAFTEN = "LOSEIGENSCHAFTEN         ";
	public static final String PANEL_PROJEKTEIGENSCHAFTEN = "PROJEKTEIGENSCHAFTEN     ";
	public static final String PANEL_PROJEKTHISTORY =       "PROJEKTHISTORY           ";
	public static final String PANEL_CHARGENEIGENSCHAFTEN = "CHARGENEIGENSCHAFTEN     ";
	public static final String PANEL_PERSONALEIGENSCHAFTEN = "PERSONALEIGENSCHAFTEN    ";
	public static final String PANEL_LIEFERANTENEIGENSCHAFTEN = "LIEFERANTENEIGENSCHAFTEN ";
	public static final String PANEL_ARTIKELTECHNIK = "ARTIKELTECHNIK           ";
	public static final String PANEL_PARTNEREIGENSCHAFTEN = "PARTNEREIGENSCHAFTEN     ";
	public static final String PANEL_ANGEBOTSEIGENSCHAFTEN ="ANGEBOTSEIGENSCHAFTEN    ";

	public static final String REPORT_MODUL = "panel";

	// Feldlaengen
	public static final int MAX_PANELBESCHREIBUNG_NAME = 120;

	public Integer createPanelbeschreibung(PanelbeschreibungDto panelbeschreibungDto,
			boolean bFuegeNeuePositionVorDerSelektiertenEin) throws RemoteException, EJBExceptionLP;

	public void removePanelbeschreibung(Integer iId) throws RemoteException, EJBExceptionLP;

	public void removePanelbeschreibung(PanelbeschreibungDto panelbeschreibungDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePanelbeschreibung(PanelbeschreibungDto panelbeschreibungDto)
			throws RemoteException, EJBExceptionLP;

	public PanelbeschreibungDto panelbeschreibungFindByPrimaryKey(Integer iId) throws RemoteException, EJBExceptionLP;

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNr(String panelCNr, String mandantCNr,
			Integer artgruIId);

	public JasperPrintLP printPanel(String panelCNr, String report, String cKey, TheClientDto theClientDto)
			throws RemoteException;

	public void createPaneldaten(PaneldatenDto[] paneldatenDto, TheClientDto theClientDto);

	public void removePaneldaten(Integer iId) throws RemoteException, EJBExceptionLP;

	public void removePaneldaten(PaneldatenDto paneldatenDto) throws RemoteException, EJBExceptionLP;

	public void updatePaneldaten(PaneldatenDto paneldatenDto) throws RemoteException, EJBExceptionLP;

	public PaneldatenDto paneldatenFindByPrimaryKey(Integer iId) throws RemoteException, EJBExceptionLP;

	public PaneldatenDto findByPanelCNrPanelbeschreibungIIdCKey(String panelCNr, Integer panelbeschreibungIId,
			String cKey) throws EJBExceptionLP, RemoteException;

	public PaneldatenDto[] paneldatenFindByPanelCNrCKey(String panelCNr, String cKey)
			throws EJBExceptionLP, RemoteException;

	public void createPanel(PanelDto panelDto) throws RemoteException, EJBExceptionLP;

	public void removePanel(String cNr) throws RemoteException, EJBExceptionLP;

	public void removePanel(PanelDto panelDto) throws RemoteException, EJBExceptionLP;

	public void updatePanel(PanelDto panelDto) throws RemoteException, EJBExceptionLP;

	public PanelDto panelFindByPrimaryKey(String cNr) throws RemoteException, EJBExceptionLP;

	public boolean panelbeschreibungVorhanden(String panelCNr, String mandantCNr);

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNrPartnerklasseIId(String panelCNr,
			String mandantCNr, Integer partnerklasseIId);

	public boolean panelbeschreibungVorhanden(String panelCNr, String mandantCNr, Integer artgruIId);

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNrKostenstelleIId(String panelCNr,
			String mandantCNr, Integer kostenstelleIId, String projekttypCNr);

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNrBereichIId(String panelCNr,
			String mandantCNr, Integer bereichIId, String projekttypCNr);

	public PanelbeschreibungDto panelbeschreibungFindByPanelCNrMandantCNrCNameOhneExc(String panelCNr,
			String mandantCNr, String cName);

	public PanelbeschreibungDto panelbeschreibungFindByPanelCNrMandantCNrCDrucknameOhneExc(String panelCNr,
			String mandantCNr, String cName);

	public String pruefUndImportierePaneldaten(String panelCNr, byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			TheClientDto theClientDto);

	PaneldatenDto paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(String panelCNr, Integer panelbeschreibungIId,
			String cKey);

	PaneldatenDto paneldatenFindByPrimaryKeyOhneExc(Integer paneldatenIId);

	PanelbeschreibungDto panelbeschreibungFindByPrimaryKeyOhneExc(Integer panelbeschreibungIId);

	/**
	 * Erzeugt neue Paneldaten. Sind diese Paneldaten (&uuml;ber ihre
	 * Schl&uuml;sseldaten) bereits vorhanden wird die entsprechende EJBExceptionLP
	 * im Result-Objekt retourniert und nicht geworfen.</br>
	 * Ausnahme bilden Paneldaten, die mit ihrem Defaultwert (aus
	 * {@link PanelbeschreibungDto}) angelegt sind. Diese werden dann aktualisiert.
	 * 
	 * @param paneldatenDto
	 * @return enth&auml;lt die IId der neu angelegten (oder aktualisierten)
	 *         Paneldaten oder Liste von aufgetretenen EJBExceptionLP
	 */
	CreatePaneldatenResult createPaneldaten(PaneldatenDto paneldatenDto);

	PaneldatenDto setupDefaultPaneldaten(String panelCnr, String cKey, PanelbeschreibungDto panelbeschreibung);

	public Integer createPanelsperren(PanelsperrenDto panelsperrenDto);

	public PanelsperrenDto panelsperrenFindByPrimaryKey(Integer iId);

	public void updatePanelsperren(PanelsperrenDto panelsperrenDto);

	public void removePanelsperren(Integer iId);
	
	public ArrayList<PanelsperrenDto> panelsperrenFindByBelegartCNrMandantCNr(String belegartCNr, String mandantCNr);

	void createPaneldaten(String panelCnr, PaneldatenDto[] panelDtos, TheClientDto theClientDto);
	void updatePaneldaten(String panelCnr, PaneldatenDto[] panelDtos, TheClientDto theClientDto);
	
}
