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
package com.lp.webapp.heliumv;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lp.server.system.ejb.MemoryWatcherFacLocal;
import com.lp.server.system.ejb.TimestampMemory;

public class MemoryWatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -4439465446149516417L;

	private MemoryWatcherFacLocal memoryWatcherFac = null ;
	private Context context = null;

	public void init(ServletConfig config) throws ServletException {
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}		
	}
	
	private MemoryWatcherFacLocal getMemoryFac() {
		if(null == memoryWatcherFac) {
			try {
				memoryWatcherFac = (MemoryWatcherFacLocal) context
						.lookup("lpserver/MemoryWatcherBean/local");
			} catch (NamingException e) {
				e.printStackTrace();
			}		
		}

		return memoryWatcherFac ;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if ("file".equals(req.getParameter("out"))) {
			PrintWriter out = resp.getWriter();
	        resp.setContentType("application/octet-stream");
	        resp.setHeader("Content-Disposition", "attachment; filename=helium_memorywatch.csv");
 
	        out.println("\"Datum TT.MM.JJJJ.MM.TT HH:MM:SS\",\"Datum ms seit 1.1.1970\",\"Freier Speicher\"") ;
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss") ;
	        
			List<TimestampMemory> values = getMemoryFac().getTimestamps();
			for (TimestampMemory timestampMemory : values) {
				Date d = new Date(timestampMemory.getTimestamp()) ;
				out.println(sdf.format(d) + "," + timestampMemory.getTimestamp() + "," +
						+ timestampMemory.getFreeMemory());
			}
		} else {
			PrintWriter out = resp.getWriter();

			List<TimestampMemory> values = getMemoryFac().getTimestamps();

			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n"
					+ "<HTML>\n"
					+ "<HEAD><TITLE>Helium Memory Watcher</TITLE></HEAD>\n"
					+ "<BODY>\n");

			for (TimestampMemory timestampMemory : values) {
				out.println("<p>" + timestampMemory.getTimestamp() + " "
						+ timestampMemory.getFreeMemory() + "</p>");
			}

			out.println("</BODY></HTML>");
		}
	}
}
