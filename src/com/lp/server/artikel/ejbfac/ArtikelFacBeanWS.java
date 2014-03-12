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
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.artikel.ejb.Artgru;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.BaseRequestResult;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.logger.webservice.WebserviceCallInterceptor;
import com.lp.util.EJBExceptionLP;

// @WebService(serviceName="HeliumVItemService", endpointInterface="com.heliumv.intf.ItemService")
//@WebService(serviceName="HeliumVItemService", endpointInterface="com.lp.server.artikel.ejbfac.IArtikelFacServices")
@WebService(name="IWebshopItemServices", serviceName="HeliumVItemService")
// @SOAPBinding(style = SOAPBinding.Style.RPC)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@Interceptors(WebserviceCallInterceptor.class)
public class ArtikelFacBeanWS extends Facade implements IWebshopItemServices {

	@PersistenceContext
	private EntityManager em;
	
	private TheClientDto webClientDto ;
	private Locale webLocale ;
	private String webWaehrung ;

	public ArtikelFacBeanWS() {
		webClientDto = new TheClientDto();
		setWebClientDefaults(webClientDto) ;
	}

	@Override
	@WebMethod
	public void setMandator(String mandator) {
		if(null == mandator || 0 == mandator.trim().length()) return ;
		
		webClientDto.setMandant(mandator.trim()) ;
	}

	@Override
	@WebMethod
	public void setCurrency(String currency) {
		if(null == currency || 0 == currency.trim().length()) return ;
		
		webWaehrung = currency.trim() ;
		webClientDto.setSMandantenwaehrung(webWaehrung) ;	
	}
	

	@Override
	@WebMethod
	public void setLocale(String language, String country) {
		if(null == language || 0 == language.trim().length()) return ;
		if(null == country || 0 == country.trim().length()) return ;

		webLocale = new Locale(language, country) ;
		webClientDto.setLocMandant(webLocale) ;
		webClientDto.setUiLoc(webLocale) ;
		webClientDto.setLocKonzern(webLocale) ;
	}
	
	
	@Override
	@WebMethod
	public ItemcategoryResult itemCategoryFindAll() {
		try {
			Query query = em.createNamedQuery("ArtgrufindAllForMandantCnrWebshop");
			query.setParameter(1, webClientDto.getMandant()) ;
			Collection<Artgru> cl = query.getResultList() ;		
			List<Itemcategory> itemgroups = transformToItemgroup(cl, webClientDto) ;
			ItemcategoryResult rc = new ItemcategoryResult(BaseRequestResult.OKAY, "", itemgroups) ;
			return rc ;	
		} catch(RemoteException re) {
			return new ItemcategoryResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} catch(EJBExceptionLP ex) {
			return new ItemcategoryResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}
	}
	
	
	@Override
	@WebMethod
	public ItemcategoryResult itemCategoryFindAllWithDateString(String changedDate) {
		if(null == changedDate) {
			return new ItemcategoryResult(BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null") ;
		}
		if(changedDate.trim().length() == 0) {
			return new ItemcategoryResult(BaseRequestResult.ERROR_NULL_PARAMETER, "Date == empty") ;
		}
	
		Date d = parseDateString(changedDate) ;
		return itemCategoryFindAllWithDate(d) ;		
	}
	
	
	@Override
	@WebMethod
	public ItemcategoryResult itemCategoryFindAllWithDate(Date changedDate) {
		changedDate = normalizeDate(changedDate) ;

		try {
			if(null == changedDate) {
				return new ItemcategoryResult(BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null (or illegal format)") ;
			}
			
			Query query = em.createNamedQuery("ArtgrufindAllForMandantCnrWebshopWithDate");
			query.setParameter(1, webClientDto.getMandant()) ;
			query.setParameter(2, new Timestamp(changedDate.getTime())) ;
			Collection<Artgru> cl = query.getResultList() ;		
			List<Itemcategory> itemgroups = transformToItemgroup(cl, webClientDto) ;
			ItemcategoryResult rc = new ItemcategoryResult(BaseRequestResult.OKAY, "", itemgroups) ;
			return rc ;
		} catch(RemoteException re) {			
			return new ItemcategoryResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} catch(EJBExceptionLP ex) {
			return new ItemcategoryResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}			
	}
	
	@Override
	@WebMethod
	public ItemcnrResult itemlistFindAll(String artgruString) {
		if(null == artgruString || artgruString.trim().length() == 0) {
			return new ItemcnrResult(BaseRequestResult.ERROR_NULL_PARAMETER, "itemgroup == null (or blank/empty)") ;			
		}

		artgruString = artgruString.trim() ;
		
		try {
			Artgru artgru = findArtgruByCnr(artgruString) ;
			
			Query query = em.createNamedQuery("ArtikelfindByArtgruIIdMandantCNr");
			query.setParameter(1, webClientDto.getMandant()) ;
			query.setParameter(2, artgru.getIId()) ;
			Collection<Artikel> cl = query.getResultList() ;		
			List<Itemcnr> itemcnrs  = transformToItemcnr(cl, webClientDto) ;
			
			return new ItemcnrResult(ItemcnrResult.OKAY, "", itemcnrs) ;
		} catch (NoResultException e) {
			return new ItemcnrResult(ItemcnrResult.ERROR_ITEMGROUP_NOT_FOUND, artgruString) ;
		} catch (NonUniqueResultException e) {
			return new ItemcnrResult(ItemcnrResult.ERROR_ITEMGROUP_NOT_UNIQUE, artgruString) ;
		} catch(RemoteException re) {			
			return new ItemcnrResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} catch(EJBExceptionLP ex) {
			return new ItemcnrResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}			
	}

	@Override
	@WebMethod
	public ItemcnrResult itemlistFindAllWithDateString(String itemCategory, String changedDate) {
		if(null == changedDate) {
			return new ItemcnrResult(BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null") ;
		}
		if(changedDate.trim().length() == 0) {
			return new ItemcnrResult(BaseRequestResult.ERROR_NULL_PARAMETER, "Date == empty") ;
		}
	
		Date d = parseDateString(changedDate) ;
		return itemlistFindAllWithDate(itemCategory, d) ;
	}
	
		
	@Override
	@WebMethod
	public ItemcnrResult itemlistFindAllWithDate(String artgruString, Date changedDate) {
		if(null == artgruString || artgruString.trim().length() == 0) {
			return new ItemcnrResult(BaseRequestResult.ERROR_NULL_PARAMETER, "itemgroup == null (or blank/empty)") ;			
		}
		if(null == changedDate) {
			return new ItemcnrResult(BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null (or illegal format)") ;
		}
		
		artgruString = artgruString.trim() ;
		changedDate = normalizeDate(changedDate) ;

		try {
			Artgru artgru = findArtgruByCnr(artgruString) ;
			
			Query query = em.createNamedQuery("ArtikelfindByArtgruIIdMandantCNrWithDate");
			query.setParameter(1, webClientDto.getMandant()) ;
			query.setParameter(2, artgru.getIId()) ;
			query.setParameter(3, new Timestamp(changedDate.getTime())) ;
			Collection<Artikel> cl = query.getResultList() ;		
			List<Itemcnr> itemcnrs  = transformToItemcnr(cl, webClientDto) ;
			
			return new ItemcnrResult(ItemcnrResult.OKAY, "", itemcnrs) ;
		} catch (NoResultException e) {
			return new ItemcnrResult(ItemcnrResult.ERROR_ITEMGROUP_NOT_FOUND, artgruString) ;
		} catch (NonUniqueResultException e) {
			return new ItemcnrResult(ItemcnrResult.ERROR_ITEMGROUP_NOT_UNIQUE, artgruString) ;
		} catch(RemoteException re) {			
			return new ItemcnrResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} catch(EJBExceptionLP ex) {
			return new ItemcnrResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}			
	}

	@Override
	@WebMethod
	public ItemcnrResult itemlistFindAllById(Integer itemCategoryId) {
		if(null == itemCategoryId) {
			return new ItemcnrResult(BaseRequestResult.ERROR_NULL_PARAMETER, "itemCategory == null") ;			
		}
		
		try {
			// Um zu verhindern, dass eine beliebige IID uebergeben wird die beispielsweise nicht im Webshop ist
			Artgru artgru = findArtgruById(itemCategoryId) ;

			Query query = em.createNamedQuery("ArtikelfindByArtgruIIdMandantCNr");
			query.setParameter(1, webClientDto.getMandant()) ;
			query.setParameter(2, artgru.getIId()) ;
			Collection<Artikel> cl = query.getResultList() ;		
			List<Itemcnr> itemcnrs  = transformToItemcnr(cl, webClientDto) ;
			
			return new ItemcnrResult(ItemcnrResult.OKAY, "", itemcnrs) ;
		} catch (NoResultException e) {
			return new ItemcnrResult(ItemcnrResult.ERROR_ITEMGROUP_NOT_FOUND, itemCategoryId.toString()) ;
		} catch (NonUniqueResultException e) {
			return new ItemcnrResult(ItemcnrResult.ERROR_ITEMGROUP_NOT_UNIQUE, itemCategoryId.toString()) ;
		} catch(RemoteException re) {			
			return new ItemcnrResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} catch(EJBExceptionLP ex) {
			return new ItemcnrResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}			
	}

	
	@Override
	@WebMethod
	public ItemcnrResult itemlistFindAllWithDateStringById (Integer itemCategoryId, String changedDate) {
		if(null == changedDate) {
			return new ItemcnrResult(BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null") ;
		}
		if(changedDate.trim().length() == 0) {
			return new ItemcnrResult(BaseRequestResult.ERROR_NULL_PARAMETER, "Date == empty") ;
		}
	
		Date d = parseDateString(changedDate) ;
		return itemlistFindAllWithDateById(itemCategoryId, d) ;		
	}
	
	
	@Override
	@WebMethod
	public ItemcnrResult itemlistFindAllWithDateById(Integer itemCategoryId, Date changedDate) {
		if(null == itemCategoryId ) {
			return new ItemcnrResult(BaseRequestResult.ERROR_NULL_PARAMETER, "itemCategoryId == null") ;			
		}
		if(null == changedDate) {
			return new ItemcnrResult(BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null (or illegal format)") ;
		}
		
		changedDate = normalizeDate(changedDate) ;

		try {
			Artgru artgru = findArtgruById(itemCategoryId) ;
			
			Query query = em.createNamedQuery("ArtikelfindByArtgruIIdMandantCNrWithDate");
			query.setParameter(1, webClientDto.getMandant()) ;
			query.setParameter(2, artgru.getIId()) ;
			query.setParameter(3, new Timestamp(changedDate.getTime())) ;
			Collection<Artikel> cl = query.getResultList() ;		
			List<Itemcnr> itemcnrs  = transformToItemcnr(cl, webClientDto) ;
			
			return new ItemcnrResult(ItemcnrResult.OKAY, "", itemcnrs) ;
		} catch (NoResultException e) {
			return new ItemcnrResult(ItemcnrResult.ERROR_ITEMGROUP_NOT_FOUND, itemCategoryId.toString()) ;
		} catch (NonUniqueResultException e) {
			return new ItemcnrResult(ItemcnrResult.ERROR_ITEMGROUP_NOT_UNIQUE, itemCategoryId.toString()) ;
		} catch(RemoteException re) {			
			return new ItemcnrResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} catch(EJBExceptionLP ex) {
			return new ItemcnrResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}			
	}
	
	
	@Override
	@WebMethod
	public ItemResult itemFind(String itemCnr) {
		if(null == itemCnr ||  itemCnr.trim().length() == 0) {
			return new ItemResult(BaseRequestResult.ERROR_NULL_PARAMETER, "item == null (or blank/empty)") ;						
		}
		
		itemCnr = itemCnr.trim() ;
		
		try {
			ArtikelDto itemDto = getArtikelFac().artikelFindByCNr(itemCnr, webClientDto) ;
			if(!verifyWebshopItem(itemDto)) {
				return new ItemResult(ItemResult.ERROR_ITEM_NOT_FOUND, itemCnr) ;				
			}
			return new ItemResult(BaseRequestResult.OKAY, "", transformToItem(itemDto, webClientDto)) ;
		} catch (NoResultException e) {
			return new ItemResult(ItemResult.ERROR_ITEM_NOT_FOUND, itemCnr) ;
		} catch (NonUniqueResultException e) {
			return new ItemResult(ItemResult.ERROR_ITEM_NOT_UNIQUE, itemCnr) ;
		} catch(RemoteException re) {
			return new ItemResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} catch(EJBExceptionLP ex) {
			return new ItemResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;		
		}
	}

	@Override
	@WebMethod
	public ItemResult itemFindById(Integer iId) {
		if(null == iId) {
			return new ItemResult(BaseRequestResult.ERROR_NULL_PARAMETER, "itemIid == null") ;						
		}
				
		try {
			ArtikelDto itemDto = getArtikelFac().artikelFindByPrimaryKey(iId, webClientDto) ;
			if(!verifyWebshopItem(itemDto)) {
				return new ItemResult(ItemResult.ERROR_ITEM_NOT_FOUND, iId.toString()) ;				
			}
			return new ItemResult(BaseRequestResult.OKAY, "", transformToItem(itemDto, webClientDto)) ;
		} catch (NoResultException e) {
			return new ItemResult(ItemResult.ERROR_ITEM_NOT_FOUND, iId.toString()) ;
		} catch (NonUniqueResultException e) {
			return new ItemResult(ItemResult.ERROR_ITEM_NOT_UNIQUE, iId.toString()) ;
		} catch(RemoteException re) {
			return new ItemResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} catch(EJBExceptionLP ex) {
			return new ItemResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;		
		}
	}
	
	private Date parseDateString(String dateString) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = (Date) formatter.parse(dateString) ;
			return d ;
		} catch(ParseException e) {
		}

		return null ;
	}
	
	private boolean verifyWebshopItem(ArtikelDto itemDto) throws RemoteException {
		ArtgruDto artgruDto = itemDto.getArtgruDto() ;
		if(null == artgruDto) {
			artgruDto = getArtikelFac().artgruFindByPrimaryKey(itemDto.getArtgruIId(), webClientDto) ;
			itemDto.setArtgruDto(artgruDto) ;
		}
		
		if(null == artgruDto) {
			return false ;
		}

		return true ;
	}
	
	
	private Artgru findArtgruByCnr(String artgruString) throws NoResultException, NonUniqueResultException {
		Query query = em.createNamedQuery("ArtgrufindByCnrForMandantCnrWebshop");
		query.setParameter(1, webClientDto.getMandant()) ;
		query.setParameter(2, artgruString.trim()) ;
		return (Artgru) query.getSingleResult() ;				
	}
	
	
	private Artgru findArtgruById(Integer iid) throws NoResultException, NonUniqueResultException {
		Query query = em.createNamedQuery("ArtgrufindByIIdForMandantCnrWebshop");
		query.setParameter(1, webClientDto.getMandant()) ;
		query.setParameter(2, iid) ;
		return (Artgru) query.getSingleResult() ;						
	}
	
	private List<Itemcategory> transformToItemgroup(Collection<Artgru> artgrus, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		List<Itemcategory> itemgroups = new ArrayList<Itemcategory>() ;

		for (Iterator<Artgru> iterator = artgrus.iterator(); iterator.hasNext();) {
			Artgru artgru = (Artgru) iterator.next();
			
			ArtgruDto dto = getArtikelFac().artgruFindByPrimaryKey(artgru.getIId(), theClientDto) ;
			
			Itemcategory itemgroup = new Itemcategory() ;				
			itemgroup.setCBez(getCBez(dto)) ;
			itemgroup.setCnr(dto.getCNr()) ;
			itemgroup.setId(artgru.getIId()) ;
			
			itemgroups.add(itemgroup) ;
		}

		return itemgroups ;
	}
	
	private String getEmptyStringForNull(String value) {
		return value == null ? "" : value ;
	}
	
	private String getCBez(ArtgruDto dto) {
		String cbez = dto.getArtgrusprDto() == null ? dto.getBezeichnung() : dto.getArtgrusprDto().getCBez() ;
		if(null == cbez) {
			cbez = dto.getBezeichnung() ;
		}

		return getEmptyStringForNull(cbez) ;
	}
	
	private List<Itemcnr> transformToItemcnr(Collection<Artikel> items, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		List<Itemcnr> itemcnrs = new ArrayList<Itemcnr>() ;

		for (Iterator<Artikel> iterator = items.iterator(); iterator.hasNext();) {
			Artikel item = (Artikel) iterator.next();
						
			Itemcnr itemcnr = new Itemcnr(item.getCNr(), item.getIId()) ;							
			itemcnrs.add(itemcnr) ;
		}

		return itemcnrs ;
	}
	
	private Item transformToItem(ArtikelDto artikelDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Item item = new Item() ;

		item.setId(artikelDto.getIId()) ;
		item.setSku(artikelDto.getCNr()) ;	
		
		ArtikelsprDto sprDto = artikelDto.getArtikelsprDto() ;
		item.setName((null != sprDto) ? getEmptyStringForNull(sprDto.getCBez()) + "\n" + getEmptyStringForNull(sprDto.getCZbez()) : artikelDto.getCNr()) ;
		item.setShortDescription((null != sprDto) ? getEmptyStringForNull(sprDto.getCKbez()) : artikelDto.getCNr()) ;	
		item.setItemLanguage((null != sprDto) ? sprDto.getLocaleCNr() : theClientDto.getLocMandantAsString()) ;

		item.setInventoryMinQty(new BigDecimal(null != artikelDto.getFLagermindest() ? artikelDto.getFLagermindest() : 0)) ;
		item.setItemGroup(artikelDto.getArtgruDto().getCNr()) ;
		
		Integer mwstsatzbezIId = artikelDto.getMwstsatzbezIId() ;
		if(null != mwstsatzbezIId) {
			MwstsatzDto satzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(mwstsatzbezIId, webClientDto) ;
			item.setTaxClass(satzDto.getIFibumwstcode() == null ? 0 : satzDto.getIFibumwstcode()) ;
		} else {
			item.setTaxClass(0) ;
		}
		BigDecimal price = getVkPreisfindungFac().ermittlePreisbasis(
					artikelDto.getIId(),
					new java.sql.Date(normalizeDate(Calendar.getInstance().getTime()).getTime()),
					null, webWaehrung, webClientDto) ;
		item.setPrice(price) ;
		
		BigDecimal lagerstand = getLagerFac()
				.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), webClientDto) ;
		item.setInventoryQty(lagerstand) ;
		
		return item ;
	}
	
	private Date normalizeDate(Date changedDate) {
		Calendar cal = Calendar.getInstance(webLocale) ;
		cal.setTime(changedDate) ;
		cal.set(Calendar.HOUR_OF_DAY, 0) ;
		cal.set(Calendar.MINUTE, 0) ;
		cal.set(Calendar.SECOND, 0) ;
		cal.set(Calendar.MILLISECOND, 0) ;
		return cal.getTime() ;
	}
	
	protected void setWebClientDefaults(TheClientDto webClientDto) {
		webClientDto.setMandant("001") ;

		webLocale = new Locale("de", "AT") ;
		webClientDto.setLocMandant(webLocale) ;
		webClientDto.setUiLoc(webLocale) ;
		webClientDto.setLocKonzern(webLocale) ;		
		webWaehrung = "EUR" ;
		webClientDto.setSMandantenwaehrung(webWaehrung) ;
	}
}
