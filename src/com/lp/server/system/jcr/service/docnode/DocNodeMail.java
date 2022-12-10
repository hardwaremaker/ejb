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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

public class DocNodeMail extends DocNodeBase {

	private static final long serialVersionUID = 5430274228560643057L;
	
	private String literalValue;

	public DocNodeMail(String value) {
		super(MAIL);
		this.literalValue = escapeJCRChars(value.trim());
	}
	
	public DocNodeMail(Node node) throws ValueFormatException, PathNotFoundException, RepositoryException, IOException {
		super(node);
	}
	
	@Override
	public String asVisualPath() {
		return "<html>"+literalValue+"</html>";
	}

	@Override
	public String asPath() {
		return literalValue;
	}
	
	@Override
	protected void loadFromImpl(Node node) throws RepositoryException {
		literalValue = node.getProperty(NODEPROPERTY_LITERALVALUE).getString();
	}

	@Override
	protected void persistToImpl(Node node) throws RepositoryException {
		node.setProperty(NODEPROPERTY_LITERALVALUE, literalValue);
	}

	@Override
	public List<DocNodeBase> getHierarchy() {
		return new ArrayList<DocNodeBase>();
	}
}
