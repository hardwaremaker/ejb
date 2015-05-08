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

import java.io.IOException;
import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemFac;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Implementierung der OpenFactory
 * Schnittstelle.
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
 * @version not attributable Date $Date: 2008/08/05 10:11:00 $
 */
public class Beleg extends Facade implements IOpenFactory {

	protected Context context;

	public Beleg() {
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ofxml:3 add zu docI aus belegpositionDtoI alle unsere HV-Felder; Teil
	 * 3/3.
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
			Document docI) throws RemoteException, EJBExceptionLP {

		Node nodeFeaturesI = docI.createElement(SystemFac.SCHEMA_OF_FEATURES);

		// Feature: iId
		addHVFeature(nodeFeaturesI, docI, SystemFac.SCHEMA_HV_FEATURE_I_ID,
				belegPosDtoI.getIId());

		// Feature: belegIId
		addHVFeature(nodeFeaturesI, docI,
				SystemFac.SCHEMA_HV_FEATURE_BELEG_I_ID, belegPosDtoI
						.getBelegIId());

		// Feature: iSort
		addHVFeature(nodeFeaturesI, docI, SystemFac.SCHEMA_HV_FEATURE_ISORT,
				belegPosDtoI.getISort());

		// Feature: positionsartCNr
		addHVFeature(nodeFeaturesI, docI,
				SystemFac.SCHEMA_HV_FEATURE_POSITIONSART_C_NR, belegPosDtoI
						.getPositionsartCNr());

		// Feature: artikelIId
		addHVFeature(nodeFeaturesI, docI,
				SystemFac.SCHEMA_HV_FEATURE_ARTIKEL_I_ID, belegPosDtoI
						.getArtikelIId());

		// Feature: cBez
		addHVFeature(nodeFeaturesI, docI, SystemFac.SCHEMA_HV_FEATURE_C_BEZ,
				belegPosDtoI.getCBez());

		// Feature: cZusatzbez
		addHVFeature(nodeFeaturesI, docI,
				SystemFac.SCHEMA_HV_FEATURE_C_ZUSATZBEZ, belegPosDtoI
						.getCZusatzbez());

		// Feature: Artikelbezeichnunguebersteuert
		addHVFeature(nodeFeaturesI, docI,
				SystemFac.SCHEMA_HV_FEATURE_ARTIKELBEZEICHNUNGUEBERSTEUERT,
				belegPosDtoI.getBArtikelbezeichnunguebersteuert());

		// Feature: xTextinhalt
		// addHVFeature(nodeFeaturesI,
		// docI,
		// SystemFac.SCHEMA_HV_FEATURE_XTEXTINHALT,
		// belegPosDtoI.getXTextinhalt());
		/** @todo JO PJ 3858 */
		Node nodeFeature = null;
		if (belegPosDtoI.getXTextinhalt() != null
				&& !belegPosDtoI.getXTextinhalt().equals("")) {
			nodeFeature = docI.createElement(SystemFac.SCHEMA_OF_FEATURE);
			Node node = docI
					.createElement(SystemFac.SCHEMA_OF_FEATURE_DESCRIPTION);
			node.appendChild(docI
					.createTextNode(SystemFac.SCHEMA_HV_FEATURE_XTEXTINHALT));
			nodeFeature.appendChild(node);
			node = docI.createElement(SystemFac.SCHEMA_OF_FEATURE_VALUE);
			node.appendChild(docI.createCDATASection(belegPosDtoI
					.getXTextinhalt().toString()));
			nodeFeature.appendChild(node);
			nodeFeaturesI.appendChild(nodeFeature);
		}

		// Feature: mediastandardIId
		addHVFeature(nodeFeaturesI, docI,
				SystemFac.SCHEMA_HV_FEATURE_MEDIASTANDARD_I_ID, ((belegPosDtoI
						.getMediastandardIId() != null) ? belegPosDtoI
						.getMediastandardIId() : ""));

		return nodeFeaturesI;
	}

	protected Node getRequestedDeliveryAsNode(Document docI) {

		// RequestedDelivery: FirstDate
		Node nodeOF = docI
				.createElement(SystemFac.SCHEMA_OF_REQUESTED_DELIVERY);

		addOFElement(nodeOF, docI,
				"JO-WH: SCHEMA_OF_REQUESTEDDELIVERY_FIRST_DATE",
				SystemFac.SCHEMA_OF_REQUESTEDDELIVERY_FIRST_DATE);

		/** @todo JO PJ 3859 */
		// ...
		return nodeOF;
	}

	/**
	 * getQuantity
	 * 
	 * @return Node
	 * @param docI
	 *            Document
	 * @param belegPosDtoI
	 *            BelegpositionDto
	 */
	protected Node getQuantityAsNode(Document docI,
			BelegpositionDto belegPosDtoI) {
		// Quantity: QuantityCustomer
		Node nodeQuantityRet = docI.createElement(SystemFac.SCHEMA_OF_QUANTITY);

		addOFElement(nodeQuantityRet, docI, belegPosDtoI.getNMenge(),
				SystemFac.SCHEMA_OF_QUANTITY_CUSTOMER);

		// Quantity: QuantitySupplier
		// WH: dd.09.06 haben wir nicht
		// elem = docI.createElement(SystemFac.SCHEMA_OF_QUANTITY_SUPPLIER);
		// elem.appendChild(docI.createTextNode(
		// "JO-WH: SCHEMA_OF_QUANTITY_SUPPLIER"));
		// nodeWhere2AddI.appendChild(elem);

		// Quantity: UnitCustomer
		addOFElement(nodeQuantityRet, docI, belegPosDtoI.getEinheitCNr(),
				SystemFac.SCHEMA_OF_QUANTITY_UNIT_CUSTOMER);

		// Quantity: UnitSupplier
		// WH: dd.09.06 haben wir nicht
		// elem =
		// docI.createElement(SystemFac.SCHEMA_OF_QUANTITY_UNIT_SUPPLIER);
		// elem.appendChild(docI.createTextNode(
		// "JO-WH: SCHEMA_OF_QUANTITY_UNIT_SUPPLIER"));
		// nodeWhere2AddI.appendChild(elem);

		return nodeQuantityRet;
	}

	protected void addHVFeature(Node nodeFeaturesI, Document docI,
			String sDescrI, Object sValueI) throws DOMException {

		Helper.addHVFeature(nodeFeaturesI, docI, sDescrI, sValueI);
	}

	protected void addOFElement(Node nodeWhere2AddI, Document docI,
			Object oValueI, String sElementI) throws DOMException {

		Helper.addOFElement(nodeWhere2AddI, docI, oValueI, sElementI);
	}

	public Node getItemAsNode(Document docI, String cNrUserI,
			BelegpositionDto belegposDtoI) throws EJBExceptionLP,
			RemoteException {

		Node nodeRet = null;

		/** @todo JO PJ 3860 */
		if (belegposDtoI.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_IDENT)
				|| belegposDtoI.getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_HANDEINGABE)) {
			nodeRet = getArtikelFac().getItemAsNode(docI,
					belegposDtoI.getArtikelIId(), cNrUserI);
		}
		return nodeRet;
	}

	public Node getPositionAsNode(Document docI, Integer[] aIIdBelegPosI,
			Node nodeWhere2AddI, String cNrUserI) throws Exception {

		// wir machen 0..n Positions.
		Node nodePositions = docI.createElement(SystemFac.SCHEMA_OF_POSITIONS);
		nodeWhere2AddI.appendChild(nodePositions);

		for (int xIIdAnfragePOS = 0; xIIdAnfragePOS < aIIdBelegPosI.length; xIIdAnfragePOS++) {
			BelegpositionDto belegPosDto = getNextBelegPosDto(
					aIIdBelegPosI[xIIdAnfragePOS], cNrUserI);

			try {
				// wir machen 0..n Position.
				// hole OpenFactory-Position Felder
				Node nodePosition = getOpenFactoryPosition(belegPosDto,
						cNrUserI, docI);
				nodePositions.appendChild(nodePosition);

				// in den Features kommen HV spezifische Felder rein.
				Node nodeFeatures = getAllgBelegPosFeatures(belegPosDto, docI);
				nodePosition.appendChild(nodeFeatures);

				// add speziellen belegposfeatures
				nodeFeatures = addBelegPosSpecialFeatures(belegPosDto,
						nodeFeatures, docI);
			} catch (Exception ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}
		}
		return nodeWhere2AddI;
	}

	public String getPositionAsStringDocumentWS(Integer[] aIIdBelegPosI,
			String cNrUserI) {

		String asXML = null;

		Document doc = new DocumentImpl();

		try {
			Node node = getPositionAsNode(doc, aIIdBelegPosI, doc, cNrUserI);
			asXML = Helper.xML2String((Document) node);
		} catch (IOException ex) {
			/**
			 * @todo throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			 * 
			 */
		} catch (Exception ex) {
			;
			/**
			 * @todo throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex)
			 * 
			 */
		}

		return asXML;
	}

	public Node getDiscountsAsNode(Document docI, BelegpositionDto belegPosDtoI)
			throws Exception {

		if (true) {
			throw new Exception("getDiscountsAsNode not implemented!");
		}

		return null;
	}

	public Node getPriceAsNode(Document docI, BelegpositionDto belegPosDtoI,
			String cNrUserI) throws EJBExceptionLP, Exception, RemoteException {

		Node nodePriceOF = docI.createElement(SystemFac.SCHEMA_OF_PRICE);

		// Price: BaseQuantity
		addOFElement(nodePriceOF, docI, belegPosDtoI.getNMenge(), /**
		 * @todo JO
		 *       klaeren PJ 3879
		 */
		SystemFac.SCHEMA_OF_PRICE_BASEQUANTITY);

		// Price: Discounts /** @todo JO PJ 3868 */
		// Element elem =
		// docI.createElement(SystemFac.SCHEMA_OF_PRICE_DISCOUNTS);
		// addDiscounts(elem, docI, belegPosDtoII);

		// Price: VAT /** @todo JO PJ 3870 */
		// if (belegPosDtoII.getMwstsatzIId() != null) {
		// Double dMwst = getMandantFac().mwstsatzFindByPrimaryKey(
		// bSPOSI.getMwstsatzIId(), cNrUserI).getFMwstsatz();
		// addOFElement(nodeWhere2AddI,
		// docI,
		// dMwst,
		// SystemFac.SCHEMA_OF_PRICE_VAT);
		// }

		// Price: Contract /** @todo JO PJ 3869 */
		addOFElement(nodePriceOF, docI, "JO-WH Contract",
				SystemFac.SCHEMA_OF_PRICE_CONTRACT);

		return nodePriceOF;
	}

	public Node getOpenFactoryPosition(BelegpositionDto belegPosDtoI,
			String cNrUserI, Document docI) throws Exception, EJBExceptionLP,
			RemoteException, DOMException {

		// Position: PositionIDCustomer ->klaeren

		Node nodePositionRet = docI.createElement(SystemFac.SCHEMA_OF_POSITION);

		addOFElement(nodePositionRet, docI,
				"JO-WH: SCHEMA_POSITION_ID_CUSTOMER",
				SystemFac.SCHEMA_OF_POSITION_ID_CUSTOMER);

		// Position: PositionIDSupplier ->klaeren
		addOFElement(nodePositionRet, docI,
				"JO-WH: SCHEMA_POSITION_ID_SUPPLIER",
				SystemFac.SCHEMA_OF_POSITION_ID_SUPPLIER);

		// Position: Item
		Node nodeOF = getItemAsNode(docI, cNrUserI, belegPosDtoI);
		if (nodeOF != null) {
			nodePositionRet.appendChild(nodeOF);
		}

		// Position: Quantity
		nodeOF = getQuantityAsNode(docI, belegPosDtoI);
		if (nodeOF != null) {
			nodePositionRet.appendChild(nodeOF);
		}

		// Position: RequestedDelivery
		nodeOF = getRequestedDeliveryAsNode(docI);
		if (nodeOF != null) {
			nodePositionRet.appendChild(nodeOF);
		}

		// Position: Price
		nodeOF = getPriceAsNode(docI, belegPosDtoI, cNrUserI);
		if (nodeOF != null) {
			nodePositionRet.appendChild(nodeOF);
		}

		// Position: Note /** @todo JO PJ 3878 */
		addOFElement(nodePositionRet, docI, "JO-WH: Note",
				SystemFac.SCHEMA_OF_NOTE);

		return nodePositionRet;
	}

	public Node addBelegPosSpecialFeatures(BelegpositionDto belegPosDtoI,
			Node nodeFeaturesI, Document docI) throws Exception, DOMException {

		if (true) {
			throw new Exception("addBelegPosSpecialFeatures not implemented!");
		}

		return null;
	}

	public BelegpositionDto getNextBelegPosDto(int iIdBelegPosI, String cNrUserI)
			throws Exception {

		if (true) {
			throw new Exception("getNextBelegPosDto not implemented!");
		}

		return null;
	}

}
