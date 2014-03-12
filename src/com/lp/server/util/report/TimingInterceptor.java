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
package com.lp.server.util.report;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;

public class TimingInterceptor extends Facade {
	
	public TimingInterceptor() {
	}
	
	@AroundInvoke
	public Object timming(InvocationContext ctx) throws Exception {
		long t1 = System.currentTimeMillis();
		long now;
		String name = "";
		String meth = "";
		try {
			name = ctx.getClass().getName();
			meth = ctx.getMethod().getName();
			return ctx.proceed();
		} finally {
			now = System.currentTimeMillis();
			TheClientDto theClientDto = null;
			String benutzer = "";
			Object[] o = ctx.getParameters();
			for (int i=0; i<o.length; i++) {
				if (o[i] instanceof TheClientDto) {
					theClientDto = (TheClientDto) o[i];
					benutzer = theClientDto.getBenutzername();
					break;
				}
			}
			myLogger.info("" + name + "." + meth + "\tDauer: " + (now-t1) + "\t" + benutzer);
		}
	}
}
