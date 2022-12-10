package com.lp.server.auftrag.service;

import java.io.IOException;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.service.edifact.errors.EdifactException;

@Remote
public interface EdifactOrdersImportFac {
	/**
	 * Eine EDIFACT ORDERS D96A Nachricht importieren
	 * 
	 * @param message
	 * @return
	 */
	EdifactOrdersImportResult importMessage(String message, TheClientDto theClientDto);

	/**
	 * Eine EDIFACT ORDERS D96A Nachricht von einem Web-"Client"
	 * importieren</br>
	 * <p>Die erhaltenen credentials in webAuth werden benutzt um einen
	 * HELIUM V Benutzer zu "simulieren" der sich nicht am System anmeldet.
	 * Ein Import soll auch gelingen, wenn saemtliche Benutzer verwendet werden.</p>
	 * 
	 * @param message
	 * @param webAuth
	 * @return
	 */
	EdifactOrdersImportResult importWebMessage(String message, WebshopAuthHeader webAuth);

	EdifactOrdersImportResult importMessageThrow(String message, TheClientDto theClientDto)
			throws EdifactException, IOException;
}
