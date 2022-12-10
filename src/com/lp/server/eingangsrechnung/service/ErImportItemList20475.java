package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.eingangsrechnung.service.ErImportError20475.Severity;

public class ErImportItemList20475 implements Serializable {
	private static final long serialVersionUID = 6688873698467564797L;

	private List<ErImportItem20475> items;
	
	public ErImportItemList20475() {
	}
	
	public ErImportItemList20475(List<ErImportItem20475> items) {
		setItems(items);
	}

	public List<ErImportItem20475> getItems() {
		if (items == null) {
			items = new ArrayList<ErImportItem20475>();
		}
		return items;
	}
	
	public void setItems(List<ErImportItem20475> items) {
		this.items = items;
	}
	
	public boolean hasErrors(Severity severity) {
		for (ErImportItem20475 item : getItems()) {
			if (item.hasErrors(severity)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasImportedItems() {
		for (ErImportItem20475 item : getItems()) {
			if (item.isImported()) {
				return true;
			}
		}
		return false;
	}
}
