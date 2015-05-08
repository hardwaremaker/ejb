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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentResult implements Serializable {

	private static final long serialVersionUID = -2588416758161586895L;

	private Map<String, List<String>> oldFiles;
	private Map<String, List<String>> newFiles;
	private Map<String, String> newVisualPaths;
	
	private String belegart;

	private int checkedFilesCount;
	private int deletedFilesCount;
	
	public DocumentResult(String belegart) {
		this.belegart = belegart;
		oldFiles = new HashMap<String, List<String>>();
		newFiles = new HashMap<String, List<String>>();
		newVisualPaths = new HashMap<String, String>();
		checkedFilesCount = 0;
	}
	
	public String getBelegart() {
		return belegart;
	}
	
	public void setCheckedFilesCount(int checkedFilesCount) {
		this.checkedFilesCount = checkedFilesCount;
	}
	
	public int getCheckedFilesCount() {
		return checkedFilesCount;
	}
	
	public void setDeletedFilesCount(int deletedFilesCount) {
		this.deletedFilesCount = deletedFilesCount;
	}
	
	public int getDeletedFilesCount() {
		return deletedFilesCount;
	}

	public void addNotFoundFile(String path, String filename) {
		if(oldFiles.containsKey(path))
			oldFiles.get(path).add(filename);
		else {
			List<String> filenames = new ArrayList<String>();
			filenames.add(filename);
			oldFiles.put(path, filenames);
		}
	}

	public void addFoundFile(String path, String visualPath, String filename) {
		if(newFiles.containsKey(path))
			newFiles.get(path).add(filename);
		else {
			List<String> filenames = new ArrayList<String>();
			filenames.add(filename);
			newFiles.put(path, filenames);
			newVisualPaths.put(path, visualPath);
		}
	}
	
	public int getFileCount() {
		return getOldFileCount() + getNewFileCount();
	}

	public int getOldFileCount() {
		int size = 0;
		for(List<String> files:oldFiles.values()) {
			size+= files.size();
		}
		return size;
	}

	public int getNewFileCount() {
		int size = 0;
		for(List<String> files:newFiles.values()) {
			size+= files.size();
		}
		return size;
	}
	
	public boolean contains(String path) {
		return oldFiles.containsKey(path)?true:newFiles.containsKey(path);
	}
	
	public Collection<String> getAllOldPaths() {
		return oldFiles.keySet();
	}
	
	public Collection<String> getAllNewPaths() {
		return newFiles.keySet();
	}
	
	public String getVisualPath(String path) {
		return newVisualPaths.get(path);
	}

	public List<String> getOldFiles(String path) {
		return oldFiles.get(path);
	}
	
	public List<String> getNewFiles(String path) {
		return newFiles.get(path);
	}
	
	public void removeOldFile(String path, String filename) {
		if(oldFiles.containsKey(path)) {
			oldFiles.get(path).remove(filename);
			if(oldFiles.get(path).size() == 0)
				oldFiles.remove(path);
		}
	}
}
