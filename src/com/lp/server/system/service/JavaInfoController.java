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

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.Desktop.Action;
import java.awt.HeadlessException;


public class JavaInfoController {
	
	public JavaInfoDto getJavaInfo() {
		
		JavaInfoDto javaInfoDto = new JavaInfoDto();
		
		javaInfoDto.setFreeMemory(Runtime.getRuntime().freeMemory() / 1024 / 1024); // MB
		
		javaInfoDto.setMaxMemory(Runtime.getRuntime().maxMemory() / 1024 / 1024); // MB
		
		javaInfoDto.setTotMemory(Runtime.getRuntime().totalMemory() / 1024 / 1024); // MB
		
		javaInfoDto.setJavaVendor(System.getProperty("java.vendor"));
		
		javaInfoDto.setJavaVersion(System.getProperty("java.version"));
		
		javaInfoDto.setJavaVmVendor(System.getProperty("java.vm.vendor"));
		
		javaInfoDto.setJavaVmVersion(System.getProperty("java.vm.version"));
		
		javaInfoDto.setJavaVmName(System.getProperty("java.vm.name"));
		
		javaInfoDto.setJavaClathPath(System.getProperty("java.class.path"));
		
		javaInfoDto.setJavaLibraryPath(System.getProperty("java.library.path"));
		
		javaInfoDto.setOsName(System.getProperty("os.name"));
		
		javaInfoDto.setOsArchitecture(System.getProperty("os.arch"));
		
		javaInfoDto.setOsVersion(System.getProperty("os.version"));
		
		javaInfoDto.setHeadless(GraphicsEnvironment.isHeadless());
		try {
			javaInfoDto.setDesktopSupported(Desktop.isDesktopSupported());
			if(javaInfoDto.getDesktopSupported()) {
				javaInfoDto.setMailSupported(Desktop.getDesktop().isSupported(Action.MAIL));		
				javaInfoDto.setBrowserSupported(Desktop.getDesktop().isSupported(Action.BROWSE));
				javaInfoDto.setPrinterSupported(Desktop.getDesktop().isSupported(Action.PRINT));
				javaInfoDto.setOpenFileSupported(Desktop.getDesktop().isSupported(Action.OPEN));
			}
		} catch(HeadlessException e) {				
		} catch(UnsupportedOperationException e) {
		}
		
		return javaInfoDto;
	}
	
}
