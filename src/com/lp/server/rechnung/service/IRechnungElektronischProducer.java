package com.lp.server.rechnung.service;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import com.lp.server.system.service.TheClientDto;

public interface IRechnungElektronischProducer {
	boolean isDummy();
	IRechnungElektronisch create(RechnungDto rechnungDto, TheClientDto theClientDto) throws RemoteException, NamingException;
	
	String toString(IRechnungElektronisch elRechnung, TheClientDto theClientDto);
	void post(IRechnungElektronisch elRechnung, TheClientDto theClientDto);
	void archive(IRechnungElektronisch elRechnung, TheClientDto theClientDto);
}
