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
package com.lp.server.system.jcr.service.medianode;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFolder;
import com.lp.server.system.jcr.service.docnode.DocNodeLiteral;
import com.lp.server.system.jcr.service.docnode.HeliumDocPath;
import com.lp.server.util.Validator;


public abstract class MediaNodeJCR extends DocNodeBase {
	private static final long serialVersionUID = -3056586705567467065L;

	private String belegart;
	private String mandantCNr;
	private String showingValue;
	private String artCNr;
	private String id ;
	
	protected MediaNodeJCR(Node node) {
		super(node);
	}
	
	protected MediaNodeJCR(String belegart) {
		super(JCRDOC);
		this.belegart = belegart;
	}

	protected MediaNodeJCR(String belegart, String iId, String showingValue,
			String artCNr, String mandantCNr) {
		this(belegart);
		this.id = iId;
		this.showingValue = showingValue;
		this.artCNr = artCNr;
		this.mandantCNr = mandantCNr;
	}
	
	
	public String getBelegart() {
		return belegart;
	}

	protected void setBelegart(String belegart) {
		this.belegart = belegart;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	protected void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	protected String getShowingValue() {
		return showingValue;
	}

	protected void setShowingValue(String showingValue) {
		this.showingValue = showingValue;
	}

	public String getArtCNr() {
		return artCNr;
	}

	protected void setArtCNr(String artCNr) {
		this.artCNr = artCNr;
	}
		
	public String getId() {
		return id;
	}

	protected void setId(String id) {
		Validator.notEmpty(id, "id") ;
		this.id = id;
	}

	@Override
	public String asPath() {
		return id ;
	}

	@Override
	public String asVisualPath() {
		return showingValue;
	}
	
	protected String getFolder() {
		return getBelegart();
	}

	/**
	 * Laedt die Felder iId, showingValue, artCNr, mandantCNr aus der Node in
	 * das DocNodeJCR Override um andere Felder zu laden.
	 */
	protected void applyPropertiesJCRSub(Node node) throws RepositoryException {
		id = node.getProperty(NODEPROPERTY_IID).getString();
		showingValue = node.getProperty(NODEPROPERTY_SHOWINGVALUE).getString();
		artCNr = node.getProperty(NODEPROPERTY_ARTCNR).getString();
		mandantCNr = node.getProperty(NODEPROPERTY_MANDANTCNR).getString();
	}

	/**
	 * Speichert die Felder iId, showingValue, artCNr, mandantCNr in die Node.<br>
	 * Override um andere Felder zu speichern.
	 */
	protected void persistJCRSub(Node node) throws RepositoryException {
		node.setProperty(NODEPROPERTY_IID, id);
		node.setProperty(NODEPROPERTY_SHOWINGVALUE, showingValue);
		node.setProperty(NODEPROPERTY_ARTCNR, artCNr);
		node.setProperty(NODEPROPERTY_MANDANTCNR, mandantCNr);
	}

	@Override
	protected final void loadFromImpl(Node node)
			throws RepositoryException {
		belegart = node.getProperty(NODEPROPERTY_BELEGART).getString();
		applyPropertiesJCRSub(node);
	}

	@Override
	protected final void persistToImpl(Node node) throws RepositoryException {
		node.setProperty(NODEPROPERTY_BELEGART, belegart);
		persistJCRSub(node);
	}

	@Override
	public List<DocNodeBase> getHierarchy() {
		return new HeliumDocPath().add(new DocNodeLiteral(getMandantCNr()))
				.add(new DocNodeFolder(getFolder()))
				.add(new DocNodeFolder(getArtCNr())).add(this).asDocNodeList();
	}	
}
