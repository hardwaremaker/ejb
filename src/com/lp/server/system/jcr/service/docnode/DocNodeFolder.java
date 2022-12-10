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
package com.lp.server.system.jcr.service.docnode;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class DocNodeFolder extends DocNodeBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5724411088759290887L;
	private String belegart;

	public DocNodeFolder(String belegart) {
		super(FOLDER);
		if (null == belegart || belegart.trim().length() == 0)
			throw new IllegalArgumentException("belegart empty");
		this.belegart = belegart;
	}

	public DocNodeFolder(Node node) {
		super(node);
	}

	@Override
	public String asVisualPath() {
		return belegart;
	}

	@Override
	public String asPath() {
		return belegart;
	}

	@Override
	public List<DocNodeBase> getHierarchy() {
		return new ArrayList<DocNodeBase>();
	}

	@Override
	protected void persistToImpl(Node node) throws RepositoryException {
		node.setProperty(NODEPROPERTY_NODETYPE, getNodeType());
		node.setProperty(NODEPROPERTY_BELEGART, belegart);
	}

	@Override
	protected void loadFromImpl(Node node) throws RepositoryException {
		belegart = node.getProperty(NODEPROPERTY_BELEGART).getString();
	}

}
