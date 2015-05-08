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
package com.lp.server.artikel.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Locale;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface MaterialFac {

	public static final String FLR_MATERIALZUSCHLAG_MATERIAL_I_ID = "material_i_id";
	public static final String FLR_MATERIALZUSCHLAG_D_GUELTIGAB = "t_gueltigab";
	public static final String FLR_MATERIALZUSCHLAG_N_ZUSCHLAG = "n_zuschlag";
	public static final String FLR_MATERIAL_MATERIALSPRSET = "materialsprset";

	public Integer createMaterial(MaterialDto materialDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeMaterial(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public MaterialzuschlagDto getKursMaterialzuschlagDtoInZielwaehrung(
			Integer materialIId, java.util.Date datGueltigkeitsdatumI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto);

	public Integer materialFindByCNrOhneExc(String materialCNr);

	public BigDecimal getKupferzuschlagInLieferantenwaehrung(
			Integer artikelIId, Integer lieferantIId, TheClientDto theClientDto);

	public BigDecimal getMaterialzuschlagEKInZielwaehrung(Integer artikelIId,
			Integer lieferantIId, Date datGueltigkeitsdatumI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto);

	public BigDecimal getMaterialzuschlagVKInZielwaehrung(Integer artikelIId,
			Date datGueltigkeitsdatumI, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto);

	public void updateMaterial(MaterialDto materialDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MaterialDto materialFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal materialzuschlagFindAktuellenzuschlag(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createMaterialzuschlag(
			MaterialzuschlagDto materialzuschlagDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeMaterialzuschlag(MaterialzuschlagDto materialzuschlagDto)
			throws EJBExceptionLP, RemoteException;

	public void updateMaterialzuschlag(MaterialzuschlagDto materialzuschlagDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public MaterialzuschlagDto materialzuschlagFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;
	public void pflegeMaterialzuschlagsKursUndDatumNachtragen(
			TheClientDto theClientDto);
	public MaterialDto materialFindByPrimaryKey(Integer iId, Locale locDruck,
			TheClientDto theClientDto);
	

}
