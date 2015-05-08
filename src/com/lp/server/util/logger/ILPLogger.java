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
package com.lp.server.util.logger;

public interface ILPLogger {

	static public final int MESSAGELENGTH_MAX = 200;

	/**
	 * Log a INFO level message.
	 * 
	 * @param theMessage
	 *            the message to log
	 */
	public void info(String theMessage);

	/**
	 * Log a INFO level message along with an exception
	 * 
	 * @param theMessage
	 *            the message to log
	 * @param theThrowable
	 *            the exception to log
	 */
	public void info(String theMessage, Throwable theThrowable);

	/**
	 * Log an ERROR level message.
	 * 
	 * @param theMessage
	 *            the message to log
	 */
	public void error(String theMessage);

	/**
	 * Log an ERROR level message along with an exception
	 * 
	 * @param theMessage
	 *            the message to log
	 * @param theThrowable
	 *            the exception to log
	 */
	public void error(String theMessage, Throwable theThrowable);

	/**
	 * Log an WARN level message.
	 * 
	 * @param theMessage
	 *            the message to log
	 */
	public void logKritisch(String theMessage);

	/**
	 * Log an INFO level message.
	 * 
	 * @param theMessage
	 *            the message to log
	 */
	public void logData(Object theMessage);

	/**
	 * Log an INFO level message.
	 * 
	 * @param theMessage
	 *            the message to log
	 * @param idUser
	 *            String
	 */
	public void logData(Object theMessage, String idUser);

	/**
	 * Log an INFO level message along with an exception
	 * 
	 * @param theMessage
	 *            the message to log
	 * @param theThrowable
	 *            the exception to log
	 */
	public void logKritisch(String theMessage, Throwable theThrowable);

	/**
	 * Log a WARNING level message.
	 * 
	 * @param theMessage
	 *            the message to log
	 */
	public void warn(String theMessage);

	/**
	 * Log a WARNING level message along with an exception
	 * 
	 * @param theMessage
	 *            the message to log
	 * @param theThrowable
	 *            the exception to log
	 */
	public void warn(String theMessage, Throwable theThrowable);

	/**
	 * Used to log a message when entering a method.
	 * 
	 * @param theMessage
	 *            the message to log
	 */
	public void entry(String theMessage);

	public void entry();

	/**
	 * Used to log a message when exiting a method.
	 * 
	 * @param theMessage
	 *            the message to log
	 */
	public void exit(String theMessage);

	/**
	 * @return true if the Log4j priority level is at least 'debugging'
	 */
	public boolean isDebugEnabled();

	/**
	 * @return true if the Log4j priority level is at least 'info'
	 */
	public boolean isInfoEnabled();

	public void warn(String idUser, String theMessage, Throwable t);

	public void warn(String idUser, String theMessage);
}
