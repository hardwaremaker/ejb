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
package com.lp.server.system.jcr.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import net.sf.jasperreports.engine.JasperPrint;

import com.lp.server.system.jcr.service.docnode.DocPath;

public class JCRDocDto implements Serializable {

	private static final long serialVersionUID = -6892843976191201710L;

	private byte[] bData;

	private String sBelegart;
	private long lAnleger;
	private long lPartner;
	private long lZeitpunkt;
	private String sBelegnummer;
	private String sSchlagworte;
	private String sName;
	private String sFilename;
	private String sTable;
	private String sRow;
	private String sMIME;
	private long lVersion = -1;
	private boolean bVersteckt;
	private long lSicherheitsstufe;
	private String sGruppierung;
	private String sPath;
	private JasperPrint jasperPrint;
	private Integer iFolderToken;
	
	private DocPath docPath;
	
	public DocPath getDocPath() {
		return docPath;
	}
	
	public void setDocPath(DocPath path) {
		docPath = path;
		setsPath(docPath.getPathAsString());
	}
	
	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public void setJasperPrint(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}

	public JCRDocDto(Node node, boolean bWithData) throws ValueFormatException,
			PathNotFoundException, RepositoryException, IOException {
		if (bWithData) {
			InputStream stream = null;
			stream = node.getProperty(JCRDocFac.PROPERTY_DATA).getStream();
			node.getPath();
			byte[] b = new byte[stream.available()];
			stream.read(b);
			this.setbData(b);
		}

		this.setsPath(node.getPath());
		setsBelegart(getOptionalProperty(node, JCRDocFac.PROPERTY_BELEGART));
 		this.setlAnleger(node.getProperty(JCRDocFac.PROPERTY_ANLEGER).getLong());
		this.setlPartner(node.getProperty(JCRDocFac.PROPERTY_PARTNER).getLong());
		this.setlZeitpunkt(node.getProperty(JCRDocFac.PROPERTY_ZEITPUNKT)
				.getLong());
		this.setsBelegnummer(node.getProperty(JCRDocFac.PROPERTY_BELEGNUMMER)
				.getString());
		if(node.hasProperty(JCRDocFac.PROPERTY_SCHLAGWORTE))
			this.setsSchlagworte(node.getProperty(JCRDocFac.PROPERTY_SCHLAGWORTE)
				.getString());
		this.setsName(node.getProperty(JCRDocFac.PROPERTY_NAME).getString());
		this.setsFilename(node.getProperty(JCRDocFac.PROPERTY_FILENAME)
				.getString());
		this.setsTable(node.getProperty(JCRDocFac.PROPERTY_TABLE).getString());
		this.setsRow(node.getProperty(JCRDocFac.PROPERTY_ROW).getString());
		this.setsMIME(node.getProperty(JCRDocFac.PROPERTY_MIME).getString());
		this.setlVersion(node.getProperty(JCRDocFac.PROPERTY_VERSION).getLong());
		this.setbVersteckt(node.getProperty(JCRDocFac.PROPERTY_VERSTECKT)
				.getBoolean());
		this.setlSicherheitsstufe(node.getProperty(
				JCRDocFac.PROPERTY_SICHERHEITSSTUFE).getLong());

		setsGruppierung(getOptionalProperty(node, JCRDocFac.PROPERTY_GRUPPIERUNG)) ;
	}

	private String getOptionalProperty(Node node, String propertyName) throws RepositoryException {
		if(node.hasProperty(propertyName)) {
			return node.getProperty(propertyName).getString() ;				
		}
		return null ;
	}
	
	public JCRDocDto() {
		super();
	}

	public byte[] getbData() {
		return bData;
	}

	public void setbData(byte[] bData) {
		this.bData = bData;
	}

	public String getsBelegart() {
		return this.sBelegart;
	}

	public void setsBelegart(String sBelegart) {
		this.sBelegart = sBelegart;
	}

	public long getlAnleger() {
		return this.lAnleger;
	}

	public void setlAnleger(long lAnleger) {
		this.lAnleger = lAnleger;
	}

	public Long getlPartner() {
		return this.lPartner;
	}

	public void setlPartner(long lPartner) {
		this.lPartner = lPartner;
	}

	public long getlZeitpunkt() {
		return this.lZeitpunkt;
	}

	public void setlZeitpunkt(long lZeitpunkt) {
		this.lZeitpunkt = lZeitpunkt;
	}

	public String getsBelegnummer() {
		return this.sBelegnummer;
	}

	public void setsBelegnummer(String sBelegnummer) {
		this.sBelegnummer = sBelegnummer;
	}

	public String getsSchlagworte() {
		return this.sSchlagworte == null ? "" : sSchlagworte;
	}

	public void setsSchlagworte(String sSchlagworte) {
		this.sSchlagworte = sSchlagworte;
	}

	public String getsName() {
		return this.sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getsFilename() {
		return this.sFilename;
	}

	public void setsFilename(String sFilename) {
		this.sFilename = sFilename;
	}

	public String getsTable() {
		return this.sTable;
	}

	public void setsTable(String sTable) {
		this.sTable = sTable;
	}

	public String getsRow() {
		return this.sRow;
	}

	public void setsRow(String sRow) {
		this.sRow = sRow;
	}

	public String getsMIME() {
		return this.sMIME;
	}

	public void setsMIME(String sMIME) {
		this.sMIME = sMIME;
	}

	public long getlVersion() {
		return this.lVersion;
	}

	public void setlVersion(long lVersion) {
		this.lVersion = lVersion;
	}

	public boolean getbVersteckt() {
		return this.bVersteckt;
	}

	public void setbVersteckt(boolean bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public long getlSicherheitsstufe() {
		return this.lSicherheitsstufe;
	}

	public void setlSicherheitsstufe(long lSicherheitsstufe) {
		this.lSicherheitsstufe = lSicherheitsstufe;
	}

	public String getsGruppierung() {
		return this.sGruppierung;
	}

	public void setsGruppierung(String sGruppierung) {
		this.sGruppierung = sGruppierung;
	}

	// SK toString bei diesem Dto fuer TreeView ueberschrieben
	public String toString() {
		return this.sName;
	}

	// public void setsFullNodePath(String sPath){
	// this.sPath = sPath;
	// }

	// public String getsFullNodePath() {
	// if(sPath == null) {
	// sPath = dPath.getPathAsString() ;
	// }
	// return sPath ;
	// return this.sPath;
	// }

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof JCRDocDto)) {
			return false;
		}
		JCRDocDto that = (JCRDocDto) obj;
		if (!(that.getbData() == null ? this.getbData() == null : Arrays.equals(that
				.getbData(),this.getbData()))) {
			return false;
		}
		if (!(that.getsBelegart() == null ? this.getsBelegart() == null : that
				.getsBelegart().equals(this.getsBelegart()))) {
			return false;
		}
		if (!(that.getsBelegnummer() == null ? this.getsBelegnummer() == null
				: that.getsBelegnummer().equals(this.getsBelegnummer()))) {
			return false;
		}
		if (!(that.getsSchlagworte() == null ? this.getsSchlagworte() == null
				: that.getsSchlagworte().equals(this.getsSchlagworte()))) {
			return false;
		}
		if (!(that.getsName() == null ? this.getsName() == null : that
				.getsName().equals(this.getsName()))) {
			return false;
		}
		if (!(that.getsFilename() == null ? this.getsFilename() == null : that
				.getsFilename().equals(this.getsFilename()))) {
			return false;
		}
		if (!(that.getsTable() == null ? this.getsTable() == null : that
				.getsTable().equals(this.getsTable()))) {
			return false;
		}
		if (!(that.getsRow() == null ? this.getsRow() == null : that.getsRow()
				.equals(this.getsRow()))) {
			return false;
		}
		if (!(that.getsMIME() == null ? this.getsMIME() == null : that
				.getsMIME().equals(this.getsMIME()))) {
			return false;
		}
		if (!(that.getsGruppierung() == null ? this.getsGruppierung() == null
				: that.getsGruppierung().equals(this.getsGruppierung()))) {
			return false;
		}
		// if (!(that.getsFullNodePath() == null ? this.getsFullNodePath() ==
		// null : that.getsFullNodePath()
		// .equals(this.getsFullNodePath()))) {
		// return false;
		// }
		if (that.getlAnleger() != this.getlAnleger()) {
			return false;
		}
		if (!(that.getlPartner() == null ? this.getlPartner()==null : that.getlPartner().equals(this.getlPartner()))) {
			return false;
		}
		if (that.getlZeitpunkt() != this.getlZeitpunkt()) {
			return false;
		}
		if (that.getlVersion() != this.getlVersion()) {
			return false;
		}
		if (that.getbVersteckt() != this.getbVersteckt()) {
			return false;
		}
		if (that.getlSicherheitsstufe() != this.getlSicherheitsstufe()) {
			return false;
		}

		return true;
	}

	public int getFolderToken() {
		return iFolderToken;
	}

	public void setFolderToken(Integer token) {
		iFolderToken = token;
	}

	public String getsPath() {
		if(sPath.startsWith("/"))
			sPath = sPath.substring(1);
		return sPath;
	}

	public void setsPath(String sPath) {
		this.sPath = sPath;
	}

}
