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
package com.lp.server.fertigung.ejb;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

@Entity
@SqlResultSetMapping(name = "erledigtermaterialwertMapping", entities = { @EntityResult(entityClass = com.lp.server.fertigung.ejb.Erledigtermaterialwert.class, fields = {
		@FieldResult(name = "iLossollmaterialIId", column = "LOSSOLLMATERIAL_I_ID"),@FieldResult(name = "nSollmenge", column = "N_SOLLMENGE"),
		@FieldResult(name = "nPreis", column = "N_PREIS"),@FieldResult(name = "nAusmenge", column = "N_AUSMENGE") }) })

		@NamedNativeQuery(name = "getErledigterMaterialwert", query = 	
			"select LOSSOLLMATERIAL_I_ID, N_SOLLMENGE, COALESCE(sum(N_PREIS),0) AS N_PREIS, COALESCE(sum(N_AUSMENGE),0) as N_AUSMENGE from ( "+
			"select S.I_ID AS LOSSOLLMATERIAL_I_ID,S.N_MENGE AS N_SOLLMENGE,I.I_ID,I.N_MENGE as N_ISTMENGE,B.B_ABGANG,I_ID_BUCHUNG, "+
				"COALESCE(U.N_VERBRAUCHTEMENGE,B.N_MENGE) AS N_VERBRAUCHTEMENGE,"+
				"COALESCE(U.N_GESTEHUNGSPREIS,B.N_GESTEHUNGSPREIS) AS N_GESTEHUNGSPREIS, "+
				"B.N_MENGE,B.N_EINSTANDSPREIS, "+
				"CASE WHEN B.B_ABGANG=1 "+
					"THEN COALESCE(U.N_VERBRAUCHTEMENGE*U.N_GESTEHUNGSPREIS,B.N_MENGE*B.N_GESTEHUNGSPREIS) "+
					"ELSE COALESCE(-B.N_MENGE*B.N_EINSTANDSPREIS,-B.N_MENGE*B.N_GESTEHUNGSPREIS) END AS N_PREIS, "+
				"CASE WHEN B.B_ABGANG=1 "+
					"THEN COALESCE(U.N_VERBRAUCHTEMENGE,B.N_MENGE) "+
					"ELSE COALESCE(-B.N_MENGE,-B.N_MENGE) END AS N_AUSMENGE "+
			"from FERT_LOSISTMATERIAL I INNER JOIN FERT_LOSSOLLMATERIAL S ON S.I_ID=I.LOSSOLLMATERIAL_I_ID "+
				"INNER JOIN FERT_LOS L ON L.I_ID=S.LOS_I_ID "+
				"INNER JOIN WW_LAGERBEWEGUNG B ON B.I_BELEGARTPOSITIONID=I.I_ID "+
				"LEFT OUTER JOIN WW_LAGERABGANGURSPRUNG U ON U.I_LAGERBEWEGUNGID=B.I_ID_BUCHUNG "+
			"where B.B_HISTORIE=0 AND B.C_BELEGARTNR='Los' and "+
				"L.I_ID= :losiid "+
				") as foo1 "+
			"group by LOSSOLLMATERIAL_I_ID,N_SOLLMENGE;", resultSetMapping = "erledigtermaterialwertMapping")
			
			
			
public class Erledigtermaterialwert {
	@Id
	@Column(name = "N_PREIS", insertable = false, updatable = false)
	private BigDecimal nPreis;

	public BigDecimal getNPreis() {
		return nPreis;
	}

	@Column(name = "LOSSOLLMATERIAL_I_ID", insertable = false, updatable = false)
	private Integer iLossollmaterialIId;

	public Integer getILossollmaterialIId() {
		return iLossollmaterialIId;
	}
	@Column(name = "N_SOLLMENGE", insertable = false, updatable = false)
	private BigDecimal nSollmenge;

	public BigDecimal getNSollmenge() {
		return nSollmenge;
	}
	@Column(name = "N_AUSMENGE", insertable = false, updatable = false)
	private BigDecimal nAusmenge;

	public BigDecimal getNAusmenge() {
		return nAusmenge;
	}
}
