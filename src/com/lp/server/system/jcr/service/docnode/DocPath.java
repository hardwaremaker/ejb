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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class DocPath implements Serializable {
	
	private static final long serialVersionUID = -2731550444147601510L;

	public final static String SEPERATOR = "/" ;
	
	protected List<DocNodeBase> paths ;

	public DocPath() {
		initialize();
	}

	public DocPath(DocNodeBase node) {
		this();
		setNode(node);
	}
	
	public DocPath(String path) {
		this();
		if(path == null) throw new IllegalArgumentException("path == null");
		for(String s:path.split("/")) {
			add(new DocNodeLiteral(s));
		}
	}

	public DocPath setNode(DocNodeBase node) {
		initialize();
		paths.addAll(node.getHierarchy());
		return this ;
	}
	
	public DocPath add(List<DocNodeBase> paths) {
		this.paths.addAll(paths) ;
		return this ;
	}
	
	public DocPath add(DocNodeBase node) {
		this.paths.add(node) ;
		return this ;
	}
	
	protected void initialize () {
		paths = new ArrayList<DocNodeBase>() ;
	}
	
	public String getPathAsString() {
		StringBuffer sbPath = new StringBuffer() ;
		
		for (DocNodeBase path : paths) {
			if(sbPath.length() > 0) {
				sbPath.append(SEPERATOR) ;
			}
			
			sbPath.append(path.asEncodedPath()) ;
		}
		
		return sbPath.toString();
	}

	
	public String getVisualPathAsString() {
		StringBuffer sbPath = new StringBuffer() ;
		
		for (DocNodeBase path : paths) {
			if(sbPath.length() > 0) {
				sbPath.append(SEPERATOR) ;
			}
			
			sbPath.append(path.asVisualPath().trim().replaceAll("/", DocNodeBase.SLASH_REPLACEMENT)) ;
		}
		
		return sbPath.toString();
	}

	public DocNodeBase getLastDocNode() {
		return paths.get(paths.size()-1);
	}
	
	public List<DocNodeBase> asDocNodeList() {
		return paths ;
	}
	
	protected String asJson() {
		return "json-serialized" ;
	}
	
	@Override
	public String toString() {
		return getPathAsString();
	}
	
	public void persist(Node jcrNode) throws RepositoryException {
		jcrNode.setProperty("PATH", asJson()) ;
	}

	public DocPath getDeepCopy() {
		
//		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream(
						5 * 1024);
		ObjectOutputStream objectOutStream = null;
		ByteArrayInputStream byteInStream = null;
		ObjectInputStream objectInStream = null;
		try {
			objectOutStream = new ObjectOutputStream(byteOutStream);
			objectOutStream.writeObject(this);
			objectOutStream.flush();

			byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
			objectInStream = new ObjectInputStream(byteInStream);

			DocPath returnPath = (DocPath) objectInStream.readObject();
			objectInStream.close();
			return returnPath;

		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		} finally {
			try {
				if (objectOutStream != null) {
					objectOutStream.flush();
					objectOutStream.close();
				}
				if (byteOutStream != null) {
					byteOutStream.flush();
					byteOutStream.close();
				}
				if (byteInStream != null) {
					byteInStream.close();
				}
				if (objectInStream != null) {
					objectInStream.close();
				}
			} catch (IOException e) {
			}
		}

	}
	
	public int size() {
		return paths.size();
	}
	
	public DocNodeBase getDocNodeAt(int i) {
		return paths.get(i);
	}
	
	public boolean containsNodeName(String nodename) {
		for (DocNodeBase node : paths) {
			if(node.asPath().equals(nodename))
				return true;
		}
		return false;
	}
}
