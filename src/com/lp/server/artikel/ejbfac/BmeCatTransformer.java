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
package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.partner.ejb.BmeCatTransformerBase;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.util.HelperWebshop;

public class BmeCatTransformer extends BmeCatTransformerBase {
	public BmeCatTransformer() {		
	}
	
	private String getIsoLanguage() {
		return "de" ;
	}
	
	public Document transform(ArtikelDto artikelDto, 
			List<BmeCatKommentar> kommentare, 
			List<WebshopShopgroupOnly>shopgroups, 
			List<PriceInfo> priceInfos, MwstsatzDto mwstSatzDto, String currency) {
		Document d = null ;
		
		try {
			d = createDocument() ;

			Element bmecat = prepareBmeCat(d) ;
			d.appendChild(bmecat) ;

			Element header = d.createElement("HEADER") ;
			bmecat.appendChild(header) ;
			
			Element catalog = d.createElement("CATALOG") ;
			header.appendChild(catalog) ;
			
			Element newCat = prepareNewCatalog(d, shopgroups) ;
			bmecat.appendChild(newCat) ;

			Element article = d.createElement("PRODUCT") ;
			article.setAttribute("mode", "new") ;
			newCat.appendChild(article) ;
			
			Element productDetails = prepareProductDetails(d, artikelDto, kommentare) ;
			article.appendChild(productDetails) ;
						
			Element productOrderDetails = prepareProductOrderDetails(d, artikelDto) ;
			article.appendChild(productOrderDetails) ;
			
			List<Element> productPriceDetails = 
					prepareProductPriceDetails(d, artikelDto, priceInfos, mwstSatzDto, currency) ;
			for (Element theElement : productPriceDetails) {
				article.appendChild(theElement) ;				
			}
			
			Element mimeInfo = prepareMimeInfo(d, artikelDto, kommentare);
			if(mimeInfo != null) {
				article.appendChild(mimeInfo) ;
			}
			
			return d ;
		} catch(ParserConfigurationException e) {
			System.out.println("Exception e "  + e.getMessage()) ;
		}
		
		return d ;
	}
	
	private Element prepareBmeCat(Document d) {
		Element bmecat = d.createElement("BMECAT") ;
		bmecat.setAttribute("version", "2005") ;
		return bmecat ;
	}
	
	private Element prepareMimeInfo(Document d,  ArtikelDto artikelDto, List<BmeCatKommentar> kommentare) {
		ArtikelkommentarDto kommentarDto = findImage(kommentare) ;
		if(null == kommentarDto) return null ;
		
		Element mimeInfo = d.createElement("MIME_INFO") ;
		
		Element mime = d.createElement("MIME") ;
		mimeInfo.appendChild(mime) ;
		
		Element mimeType = d.createElement("MIME_TYPE") ;
		mimeType.appendChild(d.createTextNode(kommentarDto.getDatenformatCNr().trim())) ;
		mime.appendChild(mimeType) ;
		
		Element mimeSource = d.createElement("MIME_SOURCE") ;
		mimeSource.appendChild(d.createTextNode(buildImageUrl(kommentarDto))) ;
		mime.appendChild(mimeSource) ; 
		
		Element mimePurpose = d.createElement("MIME_PURPOSE") ;
		mimePurpose.appendChild(d.createTextNode("normal")) ;
		mime.appendChild(mimePurpose) ;
		
		return mimeInfo ;
	}
	
	private Element prepareProductDetails(Document d, ArtikelDto artikelDto, List<BmeCatKommentar> kommentare) {
		Element details = d.createElement("PRODUCT_DETAILS") ;
		
		Element intPid = d.createElement("INTERNATIONAL_PID") ;
		intPid.setAttribute("type", "heliumv") ;
		intPid.setTextContent(artikelDto.getIId().toString()) ;
		details.appendChild(intPid) ;
		
		Element manPid = d.createElement("MANUFACTURER_PID") ;
		manPid.setTextContent(artikelDto.getCNr()) ;
		details.appendChild(manPid) ;
		
		ArtikelsprDto sprDto = artikelDto.getArtikelsprDto() ;
		String shortDesc = null != sprDto ? getEmptyStringForNull(sprDto.getCBez()) : artikelDto.getCNr() ;
		Element shortDescription = d.createElement("DESCRIPTION_SHORT") ;
		setLanguageAttribute(shortDescription, sprDto != null ? sprDto.getLocaleCNr() : getIsoLanguage()) ;
		shortDescription.appendChild(d.createTextNode(HelperWebshop.unescapeHtml(shortDesc))) ;
		details.appendChild(shortDescription) ;
		
		ArtikelkommentarsprDto kommentarSprDto = findLongDescription(kommentare) ;
		if(kommentarSprDto != null) {
			Element longDescription = createLongTextElement(d, "DESCRIPTION_LONG", kommentarSprDto) ;
			details.appendChild(longDescription) ;
		}
		
		ArtikelkommentarsprDto remarksSprDto = findRemarks(kommentare) ;
		if(remarksSprDto != null) {
			Element remarks = createLongTextElement(d, "DESCRIPTION_LONG", kommentarSprDto) ;
			details.appendChild(remarks) ;
		}
		
		return details ;
	}
	
	private Element createLongTextElement(Document d, String elementName, ArtikelkommentarsprDto sprDto) {
		Element longDescription = d.createElement(elementName) ;
		setLanguageAttribute(longDescription, sprDto.getLocaleCNr()) ;
		
		String longDesc = HelperWebshop.unescapeHtml(getEmptyStringForNull(sprDto.getXKommentar())) ;		
		longDescription.appendChild(d.createTextNode(longDesc)) ;
		
		return longDescription ;
	}
	
	private String buildImageUrl(ArtikelkommentarDto artikelkommentarDto) {
		return artikelkommentarDto.getIId().toString() + 
				"-" + artikelkommentarDto.getArtikelkommentarsprDto().getCDateiname() ;
	}
	
	private ArtikelkommentarsprDto findRemarks(List<BmeCatKommentar> kommentare) {
		for (BmeCatKommentar bmecatKommentar : kommentare) {
			ArtikelkommentarDto artikelkommentarDto = bmecatKommentar.getKommentarDto() ;
			if(artikelkommentarDto.getDatenformatCNr().startsWith("text/html") &&
					!bmecatKommentar.getKommentarartDto().isWebshop()) {
				if(null != artikelkommentarDto.getArtikelkommentarsprDto()) {
					return artikelkommentarDto.getArtikelkommentarsprDto() ;
				}
			}
		}
		
		return null ;
		
	}
	private ArtikelkommentarsprDto findLongDescription(List<BmeCatKommentar> kommentare) {
		for (BmeCatKommentar bmecatKommentar : kommentare) {
			ArtikelkommentarDto artikelkommentarDto = bmecatKommentar.getKommentarDto() ;
			if(artikelkommentarDto.getDatenformatCNr().startsWith("text/html") && 
					bmecatKommentar.getKommentarartDto().isWebshop()) {
				if(null != artikelkommentarDto.getArtikelkommentarsprDto()) {
					return artikelkommentarDto.getArtikelkommentarsprDto() ;
				}
			}
		}
		
		return null ;
	}

	private ArtikelkommentarDto findImage(List<BmeCatKommentar> kommentare) {
		for (BmeCatKommentar bmecatKommentar : kommentare) {
			ArtikelkommentarDto artikelkommentarDto = bmecatKommentar.getKommentarDto() ;
			if(artikelkommentarDto.getDatenformatCNr().startsWith("image/")) {
				if(null != artikelkommentarDto.getArtikelkommentarsprDto()) {
					return artikelkommentarDto ;
				}
			}
		}
		
		return null ;
	}
	
	private void setLanguageAttribute(Element e, String value) {
		e.setAttribute("lang", value.trim()) ;
	}
	
	private Element prepareProductOrderDetails(Document d, ArtikelDto artikelDto) {
		Element details = d.createElement("PRODUCT_ORDER_DETAILS") ;
		
		Element orderUnit = d.createElement("ORDER_UNIT") ;
		orderUnit.appendChild(d.createTextNode(artikelDto.getEinheitCNr().trim())) ;
		details.appendChild(orderUnit) ;
		
		Element contentUnit = d.createElement("CONTENT_UNIT") ;
		contentUnit.appendChild(d.createTextNode(artikelDto.getEinheitCNr().trim())) ;
		details.appendChild(contentUnit) ;
		
		return details ;
	}
	
	
	private List<Element> prepareProductPriceDetails(Document d, ArtikelDto artikelDto, List<PriceInfo> priceInfos, MwstsatzDto mwstSatzDto, String currency) {
		List<Element> elements = new ArrayList<Element>();
		
		for (PriceInfo priceInfo : priceInfos) {
			Element priceElement = d.createElement("PRODUCT_PRICE_DETAILS") ;
			Element priceDetails = d.createElement("PRODUCT_PRICE") ;
			
			String priceType = "" ;
			
			if(priceInfo.getKundeIId() != null) {
				priceType = priceType.concat("_customer_" + priceInfo.getKundeIId().toString() + "_") ;
			}
			if(priceInfo.getPreislisteIId() != null) {
				priceType = priceType.concat("_list_" + priceInfo.getPreislisteIId() + "_") ;
			}

			priceDetails.setAttribute(
					"price_type", priceType.length() == 0 ? "net_customer" : priceType) ;
			
			Element priceAmount = d.createElement("PRICE_AMOUNT") ;
			BigDecimal price = priceInfo.getPrice() ; 
			priceAmount.appendChild(d.createTextNode(price == null ? "0.0" : price.toString())) ;
			priceDetails.appendChild(priceAmount) ;

			Element priceCurrency = d.createElement("PRICE_CURRENCY") ;
			priceCurrency.appendChild(d.createTextNode(currency)) ;
			priceDetails.appendChild(priceCurrency) ;

			if(mwstSatzDto != null && mwstSatzDto.getFMwstsatz() != null) {
				Element taxElement = d.createElement("TAX") ;
				taxElement.appendChild(d.createTextNode(new Double(mwstSatzDto.getFMwstsatz() / 100.0).toString())) ;
				priceDetails.appendChild(taxElement) ;
			}

			Element amount = d.createElement("LOWER_BOUND") ;
			amount.appendChild(d.createTextNode(priceInfo.getAmount().toString())) ;
			priceDetails.appendChild(amount) ;
			
			priceElement.appendChild(priceDetails) ;
			elements.add(priceElement) ;
		}
		
		return elements ;
	}
	
	private Element prepareNewCatalog(Document d, List<WebshopShopgroupOnly> shopgroups) {
		Element newCat = d.createElement("T_NEW_CATALOG") ;
		
		Element catalogGroupSystem = prepareCatalogGroupSystem(d, shopgroups) ;
		if(catalogGroupSystem != null) {
			newCat.appendChild(catalogGroupSystem) ;
		}
		
		return newCat ;		
	}
	
	private Element prepareCatalogGroupSystem(Document d, List<WebshopShopgroupOnly> shopgroups) {
		if(null == shopgroups || shopgroups.size() == 0) return null ;
		
		Element catalogGroupSystem = d.createElement("CATALOG_GROUP_SYSTEM") ;

		boolean rootProcessed = false ;
		Integer parentId = 0 ;
		for (Iterator<WebshopShopgroupOnly> iterator = shopgroups.iterator(); iterator.hasNext();) {
			WebshopShopgroupOnly webshopShopgroupOnly = iterator.next() ;
			
			Element catalogStructure = d.createElement("CATALOG_STRUCTURE") ;
			if(rootProcessed) {
				catalogStructure.setAttribute("type", iterator.hasNext() ? "node" : "leaf") ;
			} else {
				catalogStructure.setAttribute("type", "root") ;
			}
			
			Element groupName = d.createElement("GROUP_NAME") ;
			groupName.appendChild(d.createTextNode(webshopShopgroupOnly.getName())) ;
			catalogStructure.appendChild(groupName) ;

			Element elementParentId = d.createElement("PARENT_ID") ;
			elementParentId.appendChild(d.createTextNode(parentId.toString())) ;
			catalogStructure.appendChild(elementParentId) ;
			
			Element groupId = d.createElement("GROUP_ID") ;
			groupId.appendChild(d.createTextNode(webshopShopgroupOnly.getId().toString())) ;
			catalogStructure.appendChild(groupId) ;
		
			catalogGroupSystem.appendChild(catalogStructure) ;

			parentId = webshopShopgroupOnly.getId() ;
			rootProcessed = true ;
		}

		return catalogGroupSystem ;
	}
		
}
