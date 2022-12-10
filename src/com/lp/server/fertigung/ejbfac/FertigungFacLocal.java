package com.lp.server.fertigung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.ejb.Local;

import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosablieferungResultDto;
import com.lp.server.fertigung.service.LosablieferungTerminalDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LosId;
import com.lp.util.EJBExceptionLP;

@Local
public interface FertigungFacLocal {

	LosablieferungResultDto losAbliefern(CreateLosablieferungModel model);

	void ablieferungAktualisierePreise(CreateLosablieferungModel model);

	LosablieferungResultDto losAbliefernUeberTerminal(LosablieferungTerminalDto losablieferungDto,
			TheClientDto theClientDto);
	void aendereLosgroesseSubtransaction(Integer losId, Integer neueLosgroesse,
			boolean bUeberzaehligesMaterialZurueckgeben, TheClientDto theClientDto);

	LosablieferungDto losAbliefernUeberSoap(LosId losId, BigDecimal abliefermenge, 
			String station,	TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	LosDto prepareLosAbliefernUeberSoap(LosId losId, BigDecimal abliefermenge, String station, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;
}
