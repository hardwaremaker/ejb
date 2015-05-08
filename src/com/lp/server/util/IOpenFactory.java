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
package com.lp.server.util;

import java.rmi.RemoteException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Dieses Interface kuemmert sich um die OpenFactoryschnittstellen (XML).
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; 16.10.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: heidi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 12:59:43 $
 */
public interface IOpenFactory {

	/**
	 * hole 0..n belegositionen als Document in einem String; auch Webservice.
	 * 
	 * @param aIIdBelegPosI
	 *            Integer
	 * @param cNrUserI
	 *            String
	 * @return String
	 */
	public String getPositionAsStringDocumentWS(Integer[] aIIdBelegPosI,
			String cNrUserI);

	/**
	 * fuege zu belegpositionDtoI die speziellen HV-belegpos-feature-felder.
	 * 
	 * @param belegPosDtoI
	 *            BestellpositionDto
	 * @param nodeFeaturesI
	 *            Node
	 * @param docI
	 *            Document
	 * @return Document
	 * @throws Exception
	 * @throws DOMException
	 */
	public Node addBelegPosSpecialFeatures(BelegpositionDto belegPosDtoI,
			Node nodeFeaturesI, Document docI) throws Exception, DOMException;

	/**
	 * hole 0..n belegositionen als Document.
	 * 
	 * @param docI
	 *            Document
	 * @param aIIdBelegPosI
	 *            Integer
	 * @param nodeWhere2AddI
	 *            Document
	 * @param cNrUserI
	 *            String
	 * @return String
	 * @throws Exception
	 */
	public Node getPositionAsNode(Document docI, Integer aIIdBelegPosI[],
			Node nodeWhere2AddI, String cNrUserI) throws Exception;

	/**
	 * hole zu belegPosDtoI die allg. HV-belegpos-feature-felder.
	 * 
	 * @param belegPosDtoI
	 *            BelegpositionDto
	 * @param docI
	 *            BelegpositionDto
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * @return Document
	 */
	public Node getAllgBelegPosFeatures(BelegpositionDto belegPosDtoI,
			Document docI) throws RemoteException, EJBExceptionLP;

	/**
	 * hole zu belegPosDtoI die OF-position.
	 * 
	 * @param belegPosDtoI
	 *            BelegpositionDto
	 * @param cNrUserI
	 *            String
	 * @param docI
	 *            Document
	 * @return Node
	 * @throws Exception
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 * @throws DOMException
	 */
	public Node getOpenFactoryPosition(BelegpositionDto belegPosDtoI,
			String cNrUserI, Document docI) throws Exception, EJBExceptionLP,
			RemoteException, DOMException;

	/**
	 * hole zu belegPosDtoI den OF-price als node.
	 * 
	 * @param docI
	 *            Document
	 * @param belegPosDtoI
	 *            BelegpositionDto
	 * @param cNrUserI
	 *            String
	 * @return Node
	 * @throws Exception
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	public Node getPriceAsNode(Document docI, BelegpositionDto belegPosDtoI,
			String cNrUserI) throws Exception, RemoteException, EJBExceptionLP;

	/**
	 * hole zu belegPosDtoI den OF-discount als node.
	 * 
	 * @param docI
	 *            Document
	 * @param belegPosDtoI
	 *            BelegpositionDto
	 * @return Node
	 * @throws Exception
	 */
	public Node getDiscountsAsNode(Document docI, BelegpositionDto belegPosDtoI)
			throws Exception;

	/**
	 * hole zu belegposDtoI einen item als node.
	 * 
	 * @param docI
	 *            Document
	 * @param cNrUserI
	 *            String
	 * @param belegposDtoI
	 *            BelegpositionDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 * @return Node
	 */
	public Node getItemAsNode(Document docI, String cNrUserI,
			BelegpositionDto belegposDtoI) throws EJBExceptionLP,
			RemoteException;

	/**
	 * hole zu iIdBelegPosI die belegposition.
	 * 
	 * @param iIdBelegPosI
	 *            Integer
	 * @param cNrUserI
	 *            String
	 * @return BelegpositionDto
	 * @throws Exception
	 */
	public BelegpositionDto getNextBelegPosDto(int iIdBelegPosI, String cNrUserI)
			throws Exception;
}
