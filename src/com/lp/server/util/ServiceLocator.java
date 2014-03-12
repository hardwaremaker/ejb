/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.util;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

/**
 * <p>
 * Logistik Pur ServiceLocator
 * </p>
 * <p>
 * Singleton Service Locator implementiert J2EE ServiceLocator Pattern.
 * </p>
 * <br>
 * Wird am Server zum Holen des local Interface verwendet. <br>
 * Wird am Client zum Holen des remote Interface verwendet.
 * <p>
 * Copyright (c) 2004 All Rights Reserved.
 * </p>
 * <p>
 * Logistik Pur Software GmbH
 * </p>
 * 
 * @author uli walch
 * @version 1.0
 */
public class ServiceLocator {
	private static ServiceLocator instance;
	private ILPLogger myLogger = null;
	private InitialContext context;

	/**
	 * Constructor.
	 */
	private ServiceLocator() {
		super();

		init();
	}

	/**
	 * Init Methode.
	 */
	private void init() {
		myLogger = LPLogService.getInstance().getLogger(getClass());

		try {
			this.context = this.getInitialContext();
		} catch (Exception ex) {
			myLogger.error("init", ex);
		}
	}

	/**
	 * Method for initializing the JNDI context for JBoss.
	 * 
	 * @return InitialContext
	 * @throws Exception
	 */
	public InitialContext getInitialContext() throws Exception {
		Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		return new InitialContext(p);
	}

	/**
	 * Get the single instance of this class.
	 * 
	 * @return ServiceLocator
	 */
	public static ServiceLocator getInstance() {
		if (instance == null) {
			instance = new ServiceLocator();
		}

		return instance;
	}

	public static javax.mail.Session getJavaMailSession(String javaMailSessionJndiName) throws ServiceLocatorException {
		javax.mail.Session javaMailSession = null;
		
		Context ctx;
		try {
			ctx = new InitialContext();
			javaMailSession = (javax.mail.Session) ctx.lookup(javaMailSessionJndiName);
		} catch (ClassCastException cce) {
			throw new ServiceLocatorException(cce);
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		}
		return javaMailSession;
	}



}
