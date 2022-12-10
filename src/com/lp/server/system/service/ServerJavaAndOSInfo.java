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
package com.lp.server.system.service;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ServerJavaAndOSInfo implements Externalizable {
	
	private String vendor;
	private String vmVersion;
	private String osName;
	private String osVersion;
	private String vmVendor;
	private String vmName;
	
	public ServerJavaAndOSInfo() {
		setVendor("<undefined>");
		setVmVersion("<undefined>");
		setOsName("<undefined>");
		setOsVersion("<undefined>");
		setVmVendor("<undefined>");
		setVmName("<undefined>");
	}
	
	/**
	 * Es werden dabei die Properties der JVM verwendet, in der diese Methode aufgerufen wird.
	 */
	public void initProperties() {
		setVendor(System.getProperty("java.vendor"));
		setVmVersion(System.getProperty("java.version"));
		setOsName(System.getProperty("os.name"));
		setOsVersion(System.getProperty("os.version"));
		setVmVendor(System.getProperty("java.vm.vendor"));
		setVmName(System.getProperty("java.vm.name"));
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setVendor(in.readUTF());
		setVmVersion(in.readUTF());
		setOsName(in.readUTF());
		setOsVersion(in.readUTF());
		setVmVendor(in.readUTF());
		setVmName(in.readUTF());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(getVendor());
		out.writeUTF(getVmVersion());
		out.writeUTF(getOsName());
		out.writeUTF(getOsVersion());
		out.writeUTF(getVmVendor());
		out.writeUTF(getVmName());
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getVmVersion() {
		return vmVersion;
	}

	public void setVmVersion(String version) {
		this.vmVersion = version;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	
	public String formatString() {
		StringBuffer sb = new StringBuffer();
		sb.append("java.vendor = " + getVendor() + "\n");
		sb.append("java.version = " + getVmVersion() + "\n");
		sb.append("os.name = " + getOsName() + "\n");
		sb.append("os.version = " + getOsVersion() + "\n");
		sb.append("java.vm.vendor = " + getVmVendor() + "\n");
		return sb.toString();
	}

	public String getVmVendor() {
		return vmVendor;
	}

	public void setVmVendor(String vmVendor) {
		this.vmVendor = vmVendor;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
}
