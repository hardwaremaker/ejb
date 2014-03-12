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
package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "PanelbeschreibungfindByPanelCNrMandantCNr", query = "SELECT OBJECT (o) FROM Panelbeschreibung o WHERE o.panelCNr=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "PanelbeschreibungfindByPanelCNrMandantCNrCName", query = "SELECT OBJECT (o) FROM Panelbeschreibung o WHERE o.panelCNr=?1 AND o.mandantCNr=?2 AND  o.cName=?3"),
		@NamedQuery(name = "PanelbeschreibungfindByPanelCNrMandantCNrIGridy", query = "SELECT OBJECT (o) FROM Panelbeschreibung o WHERE o.panelCNr=?1 AND o.mandantCNr=?2 AND  o.iGridy>=?3") })
@Entity
@Table(name = "LP_PANELBESCHREIBUNG")
public class Panelbeschreibung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NAME")
	private String cName;

	@Column(name = "C_TYP")
	private String cTyp;

	@Column(name = "C_TOKENINRESOURCEBUNDLE")
	private String cTokeninresourcebundle;

	@Column(name = "I_GRIDX")
	private Integer iGridx;

	@Column(name = "I_GRIDY")
	private Integer iGridy;

	@Column(name = "I_GRIDWIDTH")
	private Integer iGridwidth;

	@Column(name = "I_GRIDHEIGTH")
	private Integer iGridheigth;

	@Column(name = "C_FILL")
	private String cFill;

	@Column(name = "C_ANCHOR")
	private String cAnchor;

	@Column(name = "I_INSETSLEFT")
	private Integer iInsetsleft;

	@Column(name = "I_INSETSRIGHT")
	private Integer iInsetsright;
	
	@Column(name = "ARTGRU_I_ID")
	private Integer artgruIId;

	@Column(name = "I_INSETSTOP")
	private Integer iInsetstop;

	@Column(name = "I_INSETSBOTTOM")
	private Integer iInsetsbottom;

	@Column(name = "I_IPADX")
	private Integer iIpadx;

	@Column(name = "I_IPADY")
	private Integer iIpady;

	@Column(name = "B_MANDATORY")
	private Short bMandatory;

	@Column(name = "F_WEIGHTX")
	private Double fWeightx;

	@Column(name = "F_WEIGHTY")
	private Double fWeighty;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PANEL_C_NR")
	private String panelCNr;
	
	@Column(name = "C_DRUCKNAME")
	private String cDruckname;
	
	@Column(name = "PARTNERKLASSE_I_ID")
	private Integer partnerklasseIId;

	public Integer getPartnerklasseIId() {
		return partnerklasseIId;
	}

	public void setPartnerklasseIId(Integer partnerklasseIId) {
		this.partnerklasseIId = partnerklasseIId;
	}

	private static final long serialVersionUID = 1L;

	public Panelbeschreibung() {
		super();
	}

	public Panelbeschreibung(Integer id, String panelCNr,String mandantCNr, String name,
			String typ, Integer gridx, Integer gridy, Integer gridwidth,
			Integer gridheigth, String fill, String anchor, Integer insetsleft,
			Integer insetsright, Integer insetstop, Integer insetsbottom,
			Integer ipadx, Integer ipady,Short mandatory, Double weighty, Double weightx , String Druckname, Integer artgruIId) {
		setIId(id);
		setCName(name);
		setPanelCNr(panelCNr);
		setMandantCNr(mandantCNr);
		setCTyp(typ);
		setIGridx(gridx);
		setIGridy(gridy);
		setIGridwidth(gridwidth);
		setIGridheigth(gridheigth);
		setCFill(fill);
		setCAnchor(anchor);
		setIInsetsleft(insetsleft);
		setIInsetsright(insetsright);
		setIInsetstop(insetstop);
		setIInsetsbottom(insetsbottom);
		setIIpadx(ipadx);
		setIIpady(ipady);
		setBMandatory(mandatory);
		setFWeightx(weighty);
		setFWeighty(weightx);
		setCDruckname(Druckname);
		setArtgruIId(artgruIId);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCName() {
		return this.cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
	}

	public String getCTyp() {
		return this.cTyp;
	}

	public void setCTyp(String cTyp) {
		this.cTyp = cTyp;
	}

	public String getCTokeninresourcebundle() {
		return this.cTokeninresourcebundle;
	}

	public void setCTokeninresourcebundle(String cTokeninresourcebundle) {
		this.cTokeninresourcebundle = cTokeninresourcebundle;
	}

	public Integer getIGridx() {
		return this.iGridx;
	}

	public void setIGridx(Integer iGridx) {
		this.iGridx = iGridx;
	}

	public Integer getIGridy() {
		return this.iGridy;
	}

	public void setIGridy(Integer iGridy) {
		this.iGridy = iGridy;
	}

	public Integer getIGridwidth() {
		return this.iGridwidth;
	}

	public void setIGridwidth(Integer iGridwidth) {
		this.iGridwidth = iGridwidth;
	}
	
	public Integer getArtgruIId() {
		return artgruIId;
	}

	public void setArtgruIId(Integer artgruIId) {
		this.artgruIId = artgruIId;
	}

	public Integer getIGridheigth() {
		return this.iGridheigth;
	}

	public void setIGridheigth(Integer iGridheigth) {
		this.iGridheigth = iGridheigth;
	}

	public String getCFill() {
		return this.cFill;
	}

	public void setCFill(String cFill) {
		this.cFill = cFill;
	}

	public String getCAnchor() {
		return this.cAnchor;
	}

	public void setCAnchor(String cAnchor) {
		this.cAnchor = cAnchor;
	}

	public String getCDruckname() {
		return this.cDruckname;
	}
	
	public void setCDruckname(String cDruckname) {
		this.cDruckname = cDruckname;
	}
	
	public Integer getIInsetsleft() {
		return this.iInsetsleft;
	}

	public void setIInsetsleft(Integer iInsetsleft) {
		this.iInsetsleft = iInsetsleft;
	}

	public Integer getIInsetsright() {
		return this.iInsetsright;
	}

	public void setIInsetsright(Integer iInsetsright) {
		this.iInsetsright = iInsetsright;
	}

	public Integer getIInsetstop() {
		return this.iInsetstop;
	}

	public void setIInsetstop(Integer iInsetstop) {
		this.iInsetstop = iInsetstop;
	}

	public Integer getIInsetsbottom() {
		return this.iInsetsbottom;
	}

	public void setIInsetsbottom(Integer iInsetsbottom) {
		this.iInsetsbottom = iInsetsbottom;
	}

	public Integer getIIpadx() {
		return this.iIpadx;
	}

	public void setIIpadx(Integer iIpadx) {
		this.iIpadx = iIpadx;
	}

	public Integer getIIpady() {
		return this.iIpady;
	}

	public void setIIpady(Integer iIpady) {
		this.iIpady = iIpady;
	}

	public Short getBMandatory() {
		return this.bMandatory;
	}

	public void setBMandatory(Short bMandatory) {
		this.bMandatory = bMandatory;
	}

	public Double getFWeightx() {
		return this.fWeightx;
	}

	public void setFWeightx(Double fWeightx) {
		this.fWeightx = fWeightx;
	}

	public Double getFWeighty() {
		return this.fWeighty;
	}

	public void setFWeighty(Double fWeighty) {
		this.fWeighty = fWeighty;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
	}

	public String getPanelCNr() {
		return this.panelCNr;
	}

	public void setPanelCNr(String panel) {
		this.panelCNr = panel;
	}

}
