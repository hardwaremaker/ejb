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
package com.lp.server.system.fastlanereader.ejb;

import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

public class UnknownUseCaseHandler extends UseCaseHandler {
	private static final long serialVersionUID = 1301845951668948268L;

	private Integer usecaseId ;
	
	public UnknownUseCaseHandler(Integer usecaseId) {
		this.usecaseId = usecaseId ;
		this.tableInfo = getDefaultTableInfo() ;

		System.out.println("Installed default UseCaseHandler for " + usecaseId + ".");
	}
	
	@Override
	protected long getRowCountFromDataBase() {
		return 1 ;
	}

	
	@Override
	public TableInfo getTableInfo() {
		return getDefaultTableInfo()  ;
	}

	protected TableInfo getDefaultTableInfo() {
		return new TableInfo(
				new Class[]{Integer.class, Integer.class, String.class}, 
				new Object[]{"id", "UseCaseId", "Grund"}, 
				new String[]{"id", "usecaseid", "grund"}) ;
	}
	
	protected QueryResult getDefaultQueryResult() {
		return new QueryResult(
				new Object[][] {
						{0, usecaseId, "Unbekannt. Bitte support@heliumv.com benachrichtigen"}
				}, 1, 0, 1, 0) ;
	}
	
	@Override
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		return getDefaultQueryResult() ;
	}

	@Override
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		return getDefaultQueryResult() ;
	}

}
