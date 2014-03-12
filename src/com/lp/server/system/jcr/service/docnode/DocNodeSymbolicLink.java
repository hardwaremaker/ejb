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
package com.lp.server.system.jcr.service.docnode;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import com.lp.server.util.Validator;


/**
 * Dieser Knoten existiert ausschliesslich in der View, niemals im CMS!
 * @author robert
 *
 */
public class DocNodeSymbolicLink extends DocNodeBase {
	
	private static final long serialVersionUID = -6047264995290865662L;

	private DocPath link;
	private DocPath viewPath;
	
	/**
	 * Verweist auf einen DocPath und haengt dessen Inhalt an.<br>
	 * Beispiel: Man hat den Pfad <code>HELIUMV/Partner/Partner</code> und haengt
	 * dort einen <code>DocNodeSymbolicLink</code> mit link = <code>HELIUMV/001/Auftrag/Auftrag/13.0012</code>
	 * und showingPath = <code>Auftrag/Auftrag/13.0012<code> an, erhaelt man einen sichtbaren Pfad
	 * <code>HELIUMV/Partner/Partner/Auftrag/Auftrag/13.0012</code>, wobei alle nachfolgenden Ordner und Dokumente
	 * aus <code>HELIUMV/001/Auftrag/Auftrag/13.0012</code> kommen.
	 * @param link Der Pfad auf den gezeigt wird
	 * @param viewPath
	 */
	public DocNodeSymbolicLink(DocPath link, DocPath viewPath) {
		super(SYMBOLIC_LINK);
		Validator.notNull(link, "link");
		Validator.notNull(viewPath, "viewPath") ;
		
		this.link = link.getDeepCopy();
		this.viewPath = viewPath.getDeepCopy();
	}
	
	public DocPath getViewPath() {
		return viewPath;
	}
	
	public DocPath getLinkedPath() {
		return link;
	}

	@Override
	public String asPath() {
		return viewPath.getLastDocNode().asPath();
	}

	@Override
	public String asVisualPath() {
		return viewPath.getLastDocNode().asVisualPath();
	}

	@Override
	public List<DocNodeBase> getHierarchy() {
		return link.asDocNodeList();
	}

	@Override
	protected void applyPropertiesSub(Node node) throws RepositoryException {
		//Keine Properties, dieser DocNode Typ wird nicht ins CMS gespeichert
	}

	@Override
	protected void persistSub(Node node) throws RepositoryException {
		//Keine Properties, dieser DocNode Typ wird nicht ins CMS gespeichert
	}

}
