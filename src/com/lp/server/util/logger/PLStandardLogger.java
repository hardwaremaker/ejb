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

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.lp.server.util.Facade;
import com.lp.util.LpLoggerCS;

public class PLStandardLogger implements ILPLogger, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Wrapped Log4j <code>Logger</code> instance.
	 */
//	protected LpLoggerCS logger;
	protected Logger logger ;
	
	/**
	 * Initializes this <code>PLStandardLogger</code> by instantiating a Log4j
	 * <code>Logger</code>.
	 * 
	 * @param clazz
	 *            the name of this logger (usually fully qualified class name of
	 *            this class in which to log)
	 */
	public PLStandardLogger(Class<?> clazz) {
		this.logger = LpLoggerCS.getInstance(clazz);
	}

	/**
	 * 
	 * @see ILPLogger#info(String)
	 * @param theMessage
	 *            String
	 */
	public void info(String theMessage) {
		this.logger.info(theMessage);
	}

	/**
	 * 
	 * @see ILPLogger#info(String, Throwable)
	 * @param theMessage
	 *            String
	 * @param theThrowable
	 *            Throwable
	 */
	public void info(String theMessage, Throwable theThrowable) {
		this.logger.info(theMessage, theThrowable);
	}

	/**
	 * 
	 * @see ILPLogger#error(String)
	 * @param theMessage
	 *            String
	 */
	public void error(String theMessage) {
		this.logger.error(theMessage);
	}

	/**
	 * 
	 * @see ILPLogger#error(String, Throwable)
	 * @param theMessage
	 *            String
	 * @param theThrowable
	 *            Throwable
	 */
	public void error(String theMessage, Throwable theThrowable) {
		this.logger.error(theMessage, theThrowable);
	}

	/**
	 * 
	 * @see ILPLogger#logKritisch(String)
	 * @param theMessage
	 *            String
	 */
	public void logKritisch(String theMessage) {
		this.logger.warn(theMessage);
	}

	/**
	 * 
	 * @see ILPLogger#logData(Object)
	 * @param theMessage
	 *            String
	 */
	public void logData(Object theMessage) {
		if (theMessage != null) {
			this.logger.info("Daten: " + theMessage.toString());
		}
	}

	/**
	 * 
	 * @see ILPLogger#logData(Object)
	 * @param theMessage
	 *            String
	 * @param idUser
	 *            String
	 */
	public void logData(Object theMessage, String idUser) {
		if (theMessage != null) {
			this.logger.info(idUser + " Daten: "
					+ theMessage.toString());
		}
	}

	/**
	 * 
	 * @see ILPLogger#logKritisch(String, Throwable)
	 * @param theMessage
	 *            String
	 * @param theThrowable
	 *            Throwable
	 */
	public void logKritisch(String theMessage, Throwable theThrowable) {
		this.logger.info(theMessage, theThrowable);
	}

	/**
	 * 
	 * @see ILPLogger#warn(String)
	 * @param theMessage
	 *            String
	 */
	public void warn(String theMessage) {
		this.logger.warn(theMessage);
	}

	/**
	 * 
	 * @see ILPLogger#warn(String, Throwable)
	 * @param theMessage
	 *            String
	 * @param theThrowable
	 *            Throwable
	 */
	public void warn(String theMessage, Throwable theThrowable) {
		this.logger.warn(theMessage, theThrowable);
	}

	/**
	 * 
	 * @see ILPLogger#entry(String)
	 * @param theMessage
	 *            String
	 */
	public void entry(String theMessage) {
		// index der aufrufenden methode
		int index = 1;
		String message = " entry: ";
		StackTraceElement[] e = Thread.currentThread().getStackTrace();
		if (e != null && e.length >= (index + 4)) {
			// wenn der aufruf aus der klasse Facade kommt, dann ist das
			// naechste element der "echte" aufrufer
			if (e[index].getClassName().equals(Facade.class.getName())
					|| e[index].getClassName().equals(
							PLStandardLogger.class.getName())
					|| e[index].getClassName().equals(Thread.class.getName())) {
				index += 3;
			}
			message = message + e[index].getMethodName() + ": ";
//			theMessage = theMessage;
			message = theMessage + message;
		}
		this.logger.info(message);
	}

	/**
	 * @see ILPLogger#entry()
	 */
	public void entry() {
		entry("");
	}

	/**
	 * 
	 * @see ILPLogger#exit(String)
	 * @param theMessage
	 *            String
	 */
	public void exit(String theMessage) {
		this.logger.info(theMessage);
	}

	/**
	 * 
	 * @see ILPLogger#isDebugEnabled()
	 * @return boolean
	 */
	public boolean isDebugEnabled() {
		return this.logger.isDebugEnabled();
	}

	/**
	 * 
	 * @see ILPLogger#isInfoEnabled()
	 * @return boolean
	 */
	public boolean isInfoEnabled() {
		return this.logger.isInfoEnabled();
	}

	public void warn(String idUser, String theMessage, Throwable t) {
		// index der aufrufenden methode
		int index = 1;
		String message = idUser + " ";
		StackTraceElement[] e = Thread.currentThread().getStackTrace();
		if (e != null && e.length >= (index + 2)) {
			// wenn der aufruf aus der klasse Facade kommt, dann ist das
			// naechste element der "echte" aufrufer
			if (e[index].getClassName().equals(Facade.class.getName())
					|| e[index].getClassName().equals(
							PLStandardLogger.class.getName())) {
				index++;
			}
			message = message + e[index].getMethodName() + ": ";
//			theMessage = theMessage;
			message = message + theMessage;
		}
		this.logger.warn(message);
	}

	public void warn(String idUser, String theMessage) {
		warn(idUser, theMessage, null);
	}
}
