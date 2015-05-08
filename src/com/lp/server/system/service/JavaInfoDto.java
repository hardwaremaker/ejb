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

import java.io.Serializable;

public class JavaInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8902015770504294500L;
	
	private long freeMemory;
	private long maxMemory;
	private long totMemory;
	private String javaVendor;
	private String javaVersion;
	private String javaVmVendor;
	private String javaVmVersion;
	private String javaVmName;
	private String javaClathPath;
	private String javaLibraryPath;
	private String osName;
	private String osArchitecture;
	private String osVersion;
	private boolean isHeadless;
	private boolean desktopSupported;
	private boolean mailSupported;
	private boolean browserSupported;
	private boolean printerSupported;
	private boolean openFileSupported;
	
	public JavaInfoDto(){
// TODO: ghp, Fuer was ist das gut?		
//		setFreeMemory(freeMemory);
//		setMaxMemory(maxMemory);
//		setTotMemory(totMemory);
//		setJavaVendor(javaVendor);
//		setJavaVersion(javaVersion);
//		setJavaVmVendor(javaVmVendor);
//		setJavaVmVersion(javaVmVersion);
//		setJavaVmName(javaVmName);
//		setDesktopSupported(desktopSupported);
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public long getTotMemory() {
		return totMemory;
	}

	public void setTotMemory(long totMemory) {
		this.totMemory = totMemory;
	}

	public String getJavaVendor() {
		return javaVendor;
	}

	public void setJavaVendor(String javaVendor) {
		this.javaVendor = javaVendor;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getJavaVmVendor() {
		return javaVmVendor;
	}

	public void setJavaVmVendor(String javaVmVendor) {
		this.javaVmVendor = javaVmVendor;
	}

	public String getJavaVmVersion() {
		return javaVmVersion;
	}

	public void setJavaVmVersion(String javaVmVersion) {
		this.javaVmVersion = javaVmVersion;
	}

	public String getJavaVmName() {
		return javaVmName;
	}

	public void setJavaVmName(String javaVmName) {
		this.javaVmName = javaVmName;
	}

	public String getJavaClathPath() {
		return javaClathPath;
	}

	public void setJavaClathPath(String javaClathPath) {
		this.javaClathPath = javaClathPath;
	}

	public String getJavaLibraryPath() {
		return javaLibraryPath;
	}

	public void setJavaLibraryPath(String javaLibraryPath) {
		this.javaLibraryPath = javaLibraryPath;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsArchitecture() {
		return osArchitecture;
	}

	public void setOsArchitecture(String osArchitecture) {
		this.osArchitecture = osArchitecture;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public boolean isHeadless() {
		return isHeadless;
	}

	public void setHeadless(boolean isHeadless) {
		this.isHeadless = isHeadless;
	}

	public boolean getDesktopSupported() {
		return desktopSupported;
	}

	public void setDesktopSupported(boolean desktopSupported) {
		this.desktopSupported = desktopSupported;
	}

	public boolean isMailSupported() {
		return mailSupported;
	}

	public void setMailSupported(boolean mailSupported) {
		this.mailSupported = mailSupported;
	}

	public boolean isBrowserSupported() {
		return browserSupported;
	}

	public void setBrowserSupported(boolean browserSupported) {
		this.browserSupported = browserSupported;
	}

	public boolean isPrinterSupported() {
		return printerSupported;
	}

	public void setPrinterSupported(boolean printerSupported) {
		this.printerSupported = printerSupported;
	}

	public boolean isOpenFileSupported() {
		return openFileSupported;
	}

	public void setOpenFileSupported(boolean openFileSupported) {
		this.openFileSupported = openFileSupported;
	}
	
}
