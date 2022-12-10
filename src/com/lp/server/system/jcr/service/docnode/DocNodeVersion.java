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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import com.lp.server.system.jcr.service.JCRDocDto;

public class DocNodeVersion extends DocNodeBase {

	private static final long serialVersionUID = -5774394568857580210L;
	
	private String docName;
	private JCRDocDto jcrDocDto;
	private long versionNr;
	private String dateTimeString;

	public DocNodeVersion(JCRDocDto jcr) {
		super(VERSION);
		this.jcrDocDto = jcr;
		this.docName = jcr.getsName();
		this.versionNr = jcr.getlVersion();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,Locale.GERMANY);
		dateTimeString = df.format(new Timestamp(jcr.getlZeitpunkt()));
	}
	
	public DocNodeVersion(Node node) throws ValueFormatException, PathNotFoundException, RepositoryException, IOException {
		super(node);
		jcrDocDto = new JCRDocDto(node, false);
	}
	
	@Override
	public String asVisualPath() {
//		return docName;
		return dateTimeString;
	}

	@Override
	public String asPath() {
		return docName;
	}
	
	@Override
	protected void loadFromImpl(Node node) throws RepositoryException {
	}

	@Override
	protected void persistToImpl(Node node) throws RepositoryException {
	}

	@Override
	public List<DocNodeBase> getHierarchy() {
		return new ArrayList<DocNodeBase>();
	}
	
	public JCRDocDto getJCRDocDto() {
		return jcrDocDto;
	}
	
	public long getlVersion() {
		return versionNr;
	}
}
