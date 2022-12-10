package com.lp.server.partner.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;


@Remote
public interface PartnerimportFac {

	public void importierePartner(PartnerImportDto[] daten,
			TheClientDto theClientDto, boolean bErzeugeKunde, boolean bErzeugeLieferant)
			throws RemoteException;

	public String pruefeCSVImport(PartnerImportDto[] daten,
			TheClientDto theClientDto) throws RemoteException;

	public String pruefeUndImportierePartnerXLS(byte[] xlsDatei,
			boolean bErzeugeKunde, boolean bErzeugeLieferant,
			boolean bImportierenWennKeinFehler, TheClientDto theClientDto);
	
}
