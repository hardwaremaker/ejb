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
package com.lp.server.util.logger.webservice;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.lp.server.util.Facade;

public class WebserviceCallInterceptor extends Facade  {

	public WebserviceCallInterceptor() {
	}

	@AroundInvoke
	public Object logging(InvocationContext ctx) throws Exception {
		String name = "";
		String meth = "";
		try {
			name = ctx.getClass().getName();
			meth = ctx.getMethod().getName();
			return ctx.proceed();
		} finally {
			StringBuffer params = new StringBuffer();
			Object[] o = ctx.getParameters();
			for (int i=0; i<o.length; i++) {
				params.append((o[i] == null) ? "null" : o[i].toString());
				params.append(", ");
			}
			String s = new String(params);
			if (s.length()>2)
				s = s.substring(0, s.length()-2);
			myLogger.info("" + name + "." + meth + "\tParameter: " + s);
		}
	}
}
