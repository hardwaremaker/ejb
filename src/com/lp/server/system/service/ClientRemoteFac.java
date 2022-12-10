package com.lp.server.system.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface ClientRemoteFac extends java.rmi.Remote {

	public static String REMOTE_BIND_NAME = "CALLS_FROM_EJB";

	public void publish(PayloadDto payloadDto) throws RemoteException;
	public void neueNachrichtenVerfuegbar() throws RemoteException;
}
