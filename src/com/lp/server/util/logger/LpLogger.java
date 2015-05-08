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

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

public class LpLogger extends Logger {

	// It's usually a good idea to add a dot suffix to the fully
	// qualified class name. This makes caller localization to work
	// properly even from classes that have almost the same fully
	// qualified class name as MyLogger, e.g. MyLoggerTest.
	static String FQCN = LpLogger.class.getName() + ".";

	// It's enough to instantiate a factory once and for all.
	private static LpLoggerFactory myFactory = new LpLoggerFactory();

	/**
	 * Just calls the parent constuctor.
	 * 
	 * @param name
	 *            String
	 */
	public LpLogger(String name) {
		super(name);
	}

	/**
	 * getInstance.
	 * 
	 * @param clazz
	 *            Class
	 * @return Category
	 */
//	public static Category getInstance(Class<?> clazz) {
//		return Logger.getLogger(clazz.getName(), myFactory);
//	}

	/**
	 * This method overrides {@link Logger#getLogger} by supplying its own
	 * factory type as a parameter.
	 * 
	 * @param name
	 *            String
	 * @return Logger
	 */
	public static Logger getLogger(String name) {
		return Logger.getLogger(name, myFactory);
	}
}
