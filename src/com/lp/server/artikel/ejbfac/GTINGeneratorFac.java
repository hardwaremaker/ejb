package com.lp.server.artikel.ejbfac;

import java.rmi.RemoteException;

import javax.ejb.Local;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.gs1.GTIN13;

@Local
public interface GTINGeneratorFac {

	GTIN13 generiereGTIN13(String companyPrefix, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;
	
	void setDefaultValueItemReference(String companyPrefix, Integer defaultValue, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;
	
	Integer getCurrentValueItemReference(String companyPrefix, String mandantCnr) throws RemoteException, EJBExceptionLP;
}
